import token.Token;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JackAnalyzer {

    public static void main(String[] args) throws IOException {

        String inputPath = args[0];
        File path = new File(inputPath);
        File outputFile;
        if (!path.exists()) {
            throw new RuntimeException("File or directory does not exist.");
        }

        JackTokenizer tokenizer = new JackTokenizer();

        if (path.isFile()) {
            tokenizeAndParseInputFile(inputPath, tokenizer);
        } else {
            File[] files = path.listFiles((dir, name) -> name.endsWith(".jack"));
            if (files == null) {
                throw new RuntimeException("Directory does not contain any files ending on \".jack\"");
            }
            for (File file : files) {
                tokenizeAndParseInputFile(file.getPath(), tokenizer);
            }
        }


    }

    private static void tokenizeAndParseInputFile(String inputPath, JackTokenizer tokenizer) throws IOException {
        File outputFile;
        outputFile = new File(inputPath.replace(".jack", ".xml"));
        List<Token> tokens = tokenizer.tokenizeFile(new File(inputPath));
        CompilationEngine compilationEngine = new CompilationEngine(outputFile, tokens);
        compilationEngine.writeXMLFile();
    }
}
