import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class VMTranslator {

    public static void main(String[] args) throws IOException {

        String inputPath = args[0];
        File path = new File(inputPath);
        File outputFile;
        if (!path.exists()) {
            throw new RuntimeException("File or directory does not exist.");
        }
        if (path.isFile()) {
            outputFile = new File(inputPath.replace(".vm", ".asm"));
        } else {
            outputFile = new File(inputPath + "/" + inputPath.substring(inputPath.lastIndexOf("/") + 1) + ".asm");
        }

        CodeWriter codeWriter = new CodeWriter(outputFile);
        codeWriter.writeInit();

        if (path.isFile()) {
            parseVMFileAndWriteToAssemblyFile(path, codeWriter);
        } else if (path.isDirectory()) {
            File[] files = path.listFiles((dir, name) -> name.endsWith(".vm"));
            if (files == null) {
                throw new RuntimeException("Directory does not contain any files ending on \".vm\"");
            }
            for (File file : files) {
                parseVMFileAndWriteToAssemblyFile(file, codeWriter);
            }
        }

        codeWriter.close();
    }

    private static void parseVMFileAndWriteToAssemblyFile(File file, CodeWriter codeWriter) throws FileNotFoundException {
        Parser parser = new Parser(file);
        codeWriter.setFileName(file.getName());
        while (parser.hasMoreCommands()) {
            parser.advance();
            if (!parser.isComment) {
                switch (parser.commandType()) {
                    case C_ARITHMETIC -> codeWriter.writeArithmetic(parser.arg1());
                    case C_POP, C_PUSH -> codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                    case C_LABEL -> codeWriter.writeLabel(parser.arg1());
                    case C_IF -> codeWriter.writeIf(parser.arg1());
                    case C_GOTO -> codeWriter.writeGoto(parser.arg1());
                    case C_FUNCTION -> codeWriter.writeFunction(parser.arg1(), parser.arg2());
                    case C_RETURN -> codeWriter.writeReturn();
                    case C_CALL -> codeWriter.writeCall(parser.arg1(), parser.arg2());
                    default -> throw new RuntimeException("Main; unhandled CommandType: "+ parser.commandType());
                }
            }
        }
    }
}
