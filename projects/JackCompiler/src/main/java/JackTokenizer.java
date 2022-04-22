import token.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JackTokenizer {

    public enum KEYWORD {CLASS,
        METHOD,FUNCTION,CONSTRUCTOR,
        INT,BOOLEAN,CHAR,VOID,
        VAR,STATIC,FIELD,
        LET,DO,IF,ELSE,WHILE,
        RETURN,
        TRUE,FALSE,NULL,
        THIS}

    private static boolean blockComment = false;

    private static Pattern tokenPatterns;
    private static String keyWordReg;
    private static String symbolReg;
    private static String intReg;
    private static String strReg;
    private static String idReg;

    private static final HashMap<String,KEYWORD> keyWordMap = new HashMap<>();

    static {

        keyWordMap.put("class",KEYWORD.CLASS);keyWordMap.put("constructor",KEYWORD.CONSTRUCTOR);keyWordMap.put("function",KEYWORD.FUNCTION);
        keyWordMap.put("method",KEYWORD.METHOD);keyWordMap.put("field",KEYWORD.FIELD);keyWordMap.put("static",KEYWORD.STATIC);
        keyWordMap.put("var",KEYWORD.VAR);keyWordMap.put("int",KEYWORD.INT);keyWordMap.put("char",KEYWORD.CHAR);
        keyWordMap.put("boolean",KEYWORD.BOOLEAN);keyWordMap.put("void",KEYWORD.VOID);keyWordMap.put("true",KEYWORD.TRUE);
        keyWordMap.put("false",KEYWORD.FALSE);keyWordMap.put("null",KEYWORD.NULL);keyWordMap.put("this",KEYWORD.THIS);
        keyWordMap.put("let",KEYWORD.LET);keyWordMap.put("do",KEYWORD.DO);keyWordMap.put("if",KEYWORD.IF);
        keyWordMap.put("else",KEYWORD.ELSE);keyWordMap.put("while",KEYWORD.WHILE);keyWordMap.put("return",KEYWORD.RETURN);
    }

    public List<Token> tokenizeFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        StringBuilder preprocessed = new StringBuilder();
        List<Token> tokens = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String currentLine = noComment(scanner.nextLine());

            if (!currentLine.isBlank() && !blockComment) {
                preprocessed.append(currentLine).append("\n");
            }
        }

        initRegs();

        Matcher matcher = tokenPatterns.matcher(preprocessed);

        while (matcher.find()){
            Token token;
            String tokenValue = matcher.group();
            if (tokenValue.matches(keyWordReg)) {
                token = new Keyword(tokenValue);
            } else if (tokenValue.matches(symbolReg)) {
                token = new Symbol(tokenValue);
            } else if (tokenValue.matches(strReg)) {
                token = new StringConstant(tokenValue);
            } else if (tokenValue.matches(intReg)) {
                token = new IntegerConstant(tokenValue);
            } else if (tokenValue.matches(idReg)) {
                token = new Identifier(tokenValue);
            } else {
                throw new RuntimeException("No matching pattern");
            }
            tokens.add(token);
        }

        return tokens;
    }

    private void initRegs() {
        keyWordReg = "";

        for (String seg: keyWordMap.keySet()){
            keyWordReg += seg + "|";
        }

        symbolReg = "[&*+()./,\\-\\];~}|{>=\\[<]";
        intReg = "[0-9]+";
        strReg = "\"[^\"\n]*\"";
        idReg = "[a-zA-Z_]\\w*";

        tokenPatterns = Pattern.compile(idReg + "|" + keyWordReg + symbolReg + "|" + intReg + "|" + strReg);
    }

    private String noComment(String currentLine) {
        if (currentLine.contains("//")) {
            currentLine = currentLine.split("//")[0];
        }
        if (currentLine.contains("\t")) {
            currentLine = currentLine.replace("\t", "");
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
        return currentLine;
    }
}
