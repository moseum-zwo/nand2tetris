import java.io.File;
import java.io.IOException;

public class VMTranslator {

    public static void main(String[] args) throws IOException {

        Parser parser = new Parser(new File(args[0]));
        CodeWriter codeWriter = new CodeWriter(new File(args[0].replace(".vm", ".asm")));

        while (parser.hasMoreCommands()) {
            parser.advance();
            if (!parser.isComment) {
                switch (parser.commandType()) {
                    case C_ARITHMETIC -> codeWriter.writeArithmetic(parser.arg1());
                    case C_POP, C_PUSH -> codeWriter.writePushPop(parser.commandType(), parser.arg1(), parser.arg2());
                }
            }
        }
        codeWriter.close();
    }
}
