import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;

public class CodeWriter {

    private final BufferedWriter writer;

    private final String filename;

    private int jumpCounter;

    public CodeWriter(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file));
        this.filename = file.getName().replace(".asm", "");
        this.jumpCounter = 0;
    }

    /**
     * Writes to the output file the assembly code that implements the given arithmetic command.
     *
     * @param command
     */
    public void writeArithmetic(String command) {
        switch (command) {
            case "add" -> writeLine("// " + command,
                    "@SP",
                    "A=M-1",
                    "D=M",
                    "@SP",
                    "M=M-1",
                    "A=M-1",
                    "M=M+D");
            case "sub" -> writeLine("// " + command,
                    "@SP",
                    "A=M-1",
                    "D=M",
                    "@SP",
                    "M=M-1",
                    "A=M-1",
                    "M=M-D");
            case "neg" -> writeLine("// " + command,
                    "@SP",
                    "A=M-1",
                    "M=-M");
            case "eq" -> writeTemplate1(command, "JNE");
            case "gt" -> writeTemplate1(command, "JLE");
            case "lt" -> writeTemplate1(command, "JGE");
            case "and" -> writeTemplate2(command, "M=M&D");
            case "or" -> writeTemplate2(command, "M=M|D");
            case "not" -> writeLine("// " + command,
                    "@SP",
                    "A=M-1",
                    "M=!M");
        }
    }

    private void writeTemplate2(String command, String compareType) {
        writeLine("// " + command,
                "@SP",
                "AM=M-1",
                "D=M",
                "A=A-1",
                compareType);
    }

    private void writeTemplate1(String command, String jumpType) {
        writeLine("// " + command,
                "@SP",
                "AM=M-1",
                "D=M",
                "A=A-1",
                "D=M-D",
                "@FALSE" + jumpCounter,
                "D;" + jumpType,
                "@SP",
                "A=M-1",
                "M=-1",
                "@CONTINUE" + jumpCounter,
                "0;JMP",
                "(FALSE" + jumpCounter + ")",
                "@SP",
                "A=M-1",
                "M=0",
                "(CONTINUE" + jumpCounter + ")");
        jumpCounter++;
    }

    /**
     * Writes to the output file the assembly code that implements the given command, wher command is either C_PUSH or
     * C_POP.
     *
     * @param command
     * @param segment
     * @param index
     */
    public void writePushPop(CommandType command, String segment, int index) throws IOException {
        writeComment(command, segment, index);
        segment = switch (segment) {
            case "local" -> "LCL";
            case "argument" -> "ARG";
            case "this" -> "THIS";
            case "that" -> "THAT";
            default -> segment;
        };
        switch (command) {
            case C_PUSH -> writePush(segment, index);
            case C_POP -> writePop(segment, index);
        }
    }

    private void writePush(String segment, int index) {
        switch (segment) {
            case "constant" -> writeLine("@" + index,
                    "D=A",
                    "@SP",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M+1");
            case "pointer" -> {
                String thisOrThat = index == 0 ? "THIS" : "THAT";
                writeLine("@" + thisOrThat,
                        "D=M",
                        "@SP",
                        "A=M",
                        "M=D",
                        "@SP",
                        "M=M+1");
            }
            case "static" -> {
                String variableName = filename + "." + index;
                writeLine("@" + variableName,
                        "D=M",
                        "@SP",
                        "A=M",
                        "M=D",
                        "@SP",
                        "M=M+1");
            }
            case "temp" -> writeLine("@" + (5 + index),
                    "D=M",
                    "@SP",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M+1");
            default -> writeLine("@" + index,
                    "D=A",
                    "@" + segment,
                    "D=D+M",
                    "A=D",
                    "D=M",
                    "@SP",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M+1");
        }
    }

    private void writePop(String segment, int index) {
        switch (segment) {
            case "temp" -> writeLine("@" + (5 + index),
                    "D=A",
                    "@R13",
                    "M=D",
                    "@SP",
                    "A=M-1",
                    "D=M",
                    "@R13",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M-1");
            case "pointer" -> {
                String thisOrThat = index == 0 ? "THIS" : "THAT";
                writeLine("@SP",
                        "A=M-1",
                        "D=M",
                        "@" + thisOrThat,
                        "M=D",
                        "@SP",
                        "M=M-1");
            }
            case "static" -> {
                String variableName = filename + "." + index;
                writeLine("@SP",
                        "A=M-1",
                        "D=M",
                        "@" + variableName,
                        "M=D",
                        "@SP",
                        "M=M-1");
            }
            default -> writeLine("@" + index,
                    "D=A",
                    "@" + segment,
                    "D=D+M",
                    "@R13",
                    "M=D",
                    "@SP",
                    "A=M-1",
                    "D=M",
                    "@R13",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M-1");
        }
    }

    private void writeComment(CommandType command, String segment, int index) throws IOException {
        String typeAsString = command == CommandType.C_PUSH ? "push" : "pop";
        writer.write(MessageFormat.format("// {0} {1} {2}", typeAsString, segment, index));
        writer.newLine();
    }

    private void writeLine(String... assemblerCode) {
        for (String line : assemblerCode) {
            try {
                writer.write(line);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
