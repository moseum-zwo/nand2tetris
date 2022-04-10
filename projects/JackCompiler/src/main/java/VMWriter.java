import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VMWriter {

    private final BufferedWriter writer;

    public VMWriter(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file));
    }

    public void writePush(String segment, int index) {
        if (segment.equals("var")) {
            segment = "local";
        } else if (segment.equals("field")) {
            segment = "this";
        }
        writeLine("push " + segment + " " + index);
    }

    public void writePop(String segment, int index) {
        if (segment.equals("var")) {
            segment = "local";
        } else if (segment.equals("field")) {
            segment = "this";
        }
        writeLine("pop " + segment + " " + index);
    }

    public void writeArithmetic(String operation) {
        switch (operation) {
            case "*" -> writeCall("Math.multiply", 2);
            case "/" -> writeCall("Math.divide", 2);
            case "+" -> writeLine("add");
            case "-" -> writeLine("sub");
            case "&lt;" -> writeLine("lt");
            case "&gt;" -> writeLine("gt");
            case "&amp;" -> writeLine("and");
            case "|" -> writeLine("or");
            case "=" -> writeLine("eq");
            case "~" -> writeLine("not");
            default -> writeLine(operation);
        }
    }

    public void writeLabel(String label) {
        writeLine("label " + label);
    }

    public void writeGoto(String label) {
        writeLine("goto " + label);
    }

    public void writeIf(String condition) {
        //TODO
    }

    public void writeIfGoto(String label) {
        writeLine("if-goto " + label);
    }

    public void writeCall(String functionName, int numOfArgs) {
        writeLine("call " + functionName + " " + numOfArgs);
    }

    public void writeFunction(String className, String functionName, int argCounter) {
        writeLine("function " + className + "." + functionName + " " + argCounter);
    }

    public void writeReturn() {
        writeLine("return");
    }

    public void close() throws IOException {
        writer.close();
    }

    private void writeLine(String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeKeywordConstant(String keywordConstant) {
        switch (keywordConstant) {
            case "false" -> writePush("constant", 0);
            case "true" -> writeTrue();
            case "this" -> writePush("pointer", 0);
            default -> throw new IllegalStateException("Unexpected value: " + keywordConstant);
        }
    }

    public void writeConstructorCode(int fields) {
        writePush("constant", fields);
        writeCall("Memory.alloc", 1);
        writePop("pointer", 0);
    }

    private void writeTrue() {
        writePush("constant", 0);
        writeArithmetic("not");
    }
}
