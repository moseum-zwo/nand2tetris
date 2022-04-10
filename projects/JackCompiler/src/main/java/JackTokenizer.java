import token.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JackTokenizer {

    private static String CARRIER = "";

    private static boolean blockComment = false;

    public List<Token> tokenizeFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        List<Token> tokens = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String currentLine = scanner.nextLine();
            if (currentLine.contains("//")) {
                currentLine = currentLine.split("//")[0];
            }
            String currentLineHelper = "";
            if (currentLine.contains("/*")) {
                currentLineHelper = currentLine.split("/\\*")[0];
                blockComment = true;
            }
            if (currentLine.contains("*/")) {
                currentLineHelper = currentLineHelper + " " + currentLine.substring(currentLine.indexOf("*/") + 2);
                blockComment = false;
            }
            if (!currentLineHelper.equals("")) {
                currentLine = currentLineHelper;
            }

            if (!currentLine.isBlank() && !blockComment) {
                String[] words = currentLine.split(" ");
                for (String currentWord : words) {
                    if (currentWord.contains("\t")) {
                        currentWord = currentWord.replace("\t", "");
                    }
                    analyzeCurrentWord(tokens, currentWord);
                }
            }
        }
        return tokens;
    }

    private void analyzeCurrentWord(List<Token> tokens, String currentWord) {
        if (isKeyword(currentWord)) {
            tokens.add(new Keyword(currentWord));
        } else if (isIntegerConstant(currentWord)) {
            tokens.add(new IntegerConstant(currentWord));
        } else if (isStringConstant(currentWord)) {
            tokens.add(new StringConstant(currentWord));
        } else if (isFirstWordOfStringConstant(currentWord)) {
            CARRIER = currentWord;
        } else if (isLastWordOfStringConstant(currentWord)) {
            String word = CARRIER + " " + currentWord;
            tokens.add(new StringConstant(word.substring(1, word.length() - 1)));
            CARRIER = "";
        } else if (isSymbol(currentWord)) {
            tokens.add(new Symbol(currentWord));
        } else if (lastCharIsSemicolon(currentWord)) {
            analyzeCurrentWord(tokens, currentWord.substring(0, currentWord.length() - 1));
            tokens.add(new Symbol(";"));
        } else if (containsSymbol(currentWord)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < currentWord.length(); i++) {
                String currentLetter = currentWord.substring(i, i + 1);
                if (!isSymbol(currentLetter)) {
                    sb.append(currentLetter);
                } else {
                    //end identifier
                    analyzeCurrentWord(tokens, sb.toString());  //wrong
                    tokens.add(new Symbol(currentLetter));
                    sb = new StringBuilder();
                }
            }
            if (!sb.toString().isBlank()) {
                analyzeCurrentWord(tokens, sb.toString());
            }
        } else if (isPartOfStringConstant()) {
            CARRIER = CARRIER + " " + currentWord;
        } else {
            if (!currentWord.isEmpty()) {
                tokens.add(new Identifier(currentWord));
            }
        }
    }

    private boolean isLastWordOfStringConstant(String currentWord) {
        return currentWord.endsWith("\"") && !currentWord.substring(0, currentWord.length() - 1).contains("\"");
    }

    private boolean isFirstWordOfStringConstant(String currentWord) {
        return currentWord.startsWith("\"") && !currentWord.substring(1).contains("\"");
    }

    private boolean isPartOfStringConstant() {
        return !CARRIER.equals("");
    }

    private boolean lastCharIsSemicolon(String word) {
        return word.endsWith(";");
    }

    private boolean isStringConstant(String word) {
        return word.startsWith("\"") && word.endsWith("\"");
    }

    private boolean containsSymbol(String currentWord) {
        return Symbol.allSymbols().stream().anyMatch(currentWord::contains);
    }

    private boolean isSymbol(String word) {
        return Symbol.allSymbols().contains(word);
    }

    private boolean isIntegerConstant(String word) {
        try {
            Integer.parseInt(word);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isKeyword(String word) {
        return Keyword.allKeywords().contains(word);
    }
}
