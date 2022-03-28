import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;

public class CodeWriter {

    private final BufferedWriter writer;

    private String filename;

    private int jumpCounter;

    private int callCounter;

    public CodeWriter(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file));
        this.filename = "Sys";
        this.jumpCounter = 0;
        this.callCounter = 0;
    }

    /**
     * Writes to the output file the assembly code that implements the given arithmetic command.
     *
     * @param command
     */
    public void writeArithmetic(String command) {
        switch (command) {
            case "add" -> writeAssemblyInstructions("// " + command,
                    "@SP",
                    "A=M-1",
                    "D=M",
                    "@SP",
                    "M=M-1",
                    "A=M-1",
                    "M=M+D");
            case "sub" -> writeAssemblyInstructions("// " + command,
                    "@SP",
                    "A=M-1",
                    "D=M",
                    "@SP",
                    "M=M-1",
                    "A=M-1",
                    "M=M-D");
            case "neg" -> writeAssemblyInstructions("// " + command,
                    "@SP",
                    "A=M-1",
                    "M=-M");
            case "eq" -> writeTemplate1(command, "JNE");
            case "gt" -> writeTemplate1(command, "JLE");
            case "lt" -> writeTemplate1(command, "JGE");
            case "and" -> writeTemplate2(command, "M=M&D");
            case "or" -> writeTemplate2(command, "M=M|D");
            case "not" -> writeAssemblyInstructions("// " + command,
                    "@SP",
                    "A=M-1",
                    "M=!M");
        }
    }

    /**
     * Writes to the output file the assembly code that implements the given command, where command is either C_PUSH or
     * C_POP.
     *
     * @param command
     * @param segment
     * @param index
     */
    public void writePushPop(CommandType command, String segment, int index) {
        writeCommentForPushPop(command, segment, index);
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

    public void setFileName(String fileName) {
        this.filename = fileName.substring(0, fileName.lastIndexOf("."));
    }

    public void writeInit() {
        //Bootstrap code
        // SP=256
        // Call Sys.init
        writeAssemblyInstructions("// Booting",
                "@256",
                "D=A",
                "@SP",
                "M=D");
        writeCall("Sys.init", 0);
    }

    public void writeLabel(String label) {
        writeLineNotIndented("(" + filename + "$" + label + ")");
    }

    public void writeGoto(String label) {
        writeAssemblyInstructions("// goto " + filename + "$" + label,
                "@" + filename + "$" + label,
                "0;JMP");
    }

    public void writeIf(String label) {
        writeAssemblyInstructions("// if-goto " + label,
                "@SP",
                "AM=M-1",
                "D=M",
                "@" + filename + "$" +  label,
                "D;JNE");   //TODO: Möglw. falsch. Nicht klar, ob das immer die richtige Bedingung für "false" ist.
    }

    public void writeFunction(String functionName, int numVars) {
        // (functionName)
        // numVars-times: push 0
        writeLineIndented("// function " + functionName + " " + numVars);
        writeLineIndented("(" + functionName + ")");
        initializeLocalVariables(numVars);
    }

    private void initializeLocalVariables(int numVars) {
        int localVarNumber = 1;
        while (localVarNumber <= numVars) {
            writeLineIndented("//Initialize localVar " + localVarNumber + " of " + numVars);
            writeLineIndented("@SP");
            writeLineIndented("A=M");
            writeLineIndented("M=0");
            writeLineIndented("@SP");
            writeLineIndented("M=M+1");
            localVarNumber++;
        }
    }

    public void writeCall(String functionName, int numArgs) {
        //TODO
        // push returnAddress
        // push LCL
        // push ARG
        // push THIS
        // push THAT
        // ARG = SP - 5 - nArgs
        // LCL = SP
        // goto functionName
        // (returnAddress)
        String newLabel = functionName + "$ret." + callCounter++;
        writeAssemblyInstructions("// call " + functionName + " " + numArgs,
                " // push returnAddress",
                "@" + newLabel,
                "D=A",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1",
                "  // push LCL",
                "@LCL",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1",
                "  // push ARG",
                "@ARG",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1",
                "  // push THIS",
                "@THIS",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1",
                " // push THAT",
                "@THAT",
                "D=M",
                "@SP",
                "A=M",
                "M=D",
                "@SP",
                "M=M+1",
                " // set ARG",
                "@SP",
                "D=M",
                "@5",
                "D=D-A",
                "@" + numArgs,
                "D=D-A",
                "@ARG",
                "M=D",
                "@SP",
                "D=M",
                "@LCL",
                "M=D",
                "@" + functionName,
                "0;JMP"
                );
        writeLineNotIndented("(" + newLabel + ")");
    }

    public void writeReturn() {
        // tempVar endFrame = LCL
        // tempVar retAddr = *(endFrame - 5) // gets return address
        // *ARG = pop()                      // put the last value in the working stack into arg0
        // SP = ARG + 1
        // THAT = *(endFrame - 1)
        // THIS = *(endFrame - 2)
        // ARG = *(endFrame - 3)
        // LCL = *(endFrame - 4)
        // goto retAddr
        writeAssemblyInstructions("// return",
                " // tempVar endFrame = LCL",
                "@LCL",
                "D=M",
                "@R5",  //TODO: replace with "temp"
                "M=D",
                " // tempVar retAddr = *(endFrame - 5)",
                "@LCL",
                "D=M",
                "@5",
                "A=D-A",
                "D=M",
                "@R6",  //TODO: replace with "temp"
                "M=D",
                " // *ARG = pop()",
                "@SP",
                "AM=M-1",
                "D=M",
                "@ARG",
                "A=M",
                "M=D",
                " // SP = ARG + 1",
                "@ARG",
                "D=M+1",
                "@SP",
                "M=D",
                " // THAT = *(endFrame - 1)",
                "@R5",
                "A=M-1",
                "D=M",
                "@THAT",
                "M=D",
                " // THIS = *(endFrame - 2)",
                "@R5",
                "D=M",
                "@2",
                "A=D-A",
                "D=M",
                "@THIS",
                "M=D",
                " // ARG = *(endFrame - 3)",
                "@R5",
                "D=M",
                "@3",
                "A=D-A",
                "D=M",
                "@ARG",
                "M=D",
                " // LCL = *(endFrame - 4)",
                "@R5",
                "D=M",
                "@4",
                "A=D-A",
                "D=M",
                "@LCL",
                "M=D",
                " // goto retAddr",
                "@R6",
                "A=M",
                "0;JMP"
        );
    }

    private void writeTemplate1(String command, String jumpType) {
        writeAssemblyInstructions("// " + command,
                "@SP",
                "AM=M-1",
                "D=M",
                "A=A-1",
                "D=M-D",
                "@" + filename + "$FALSE." + jumpCounter,
                "D;" + jumpType,
                "@SP",
                "A=M-1",
                "M=-1",
                "@" + filename + "$CONTINUE." + jumpCounter,
                "0;JMP"
                );
        writeLabel("FALSE." + jumpCounter);
        writeAssemblyInstructions(
                "@SP",
                "A=M-1",
                "M=0"
                );
        writeLabel("CONTINUE." + jumpCounter);
        jumpCounter++;
    }

    private void writeTemplate2(String command, String compareType) {
        writeAssemblyInstructions("// " + command,
                "@SP",
                "AM=M-1",
                "D=M",
                "A=A-1",
                compareType);
    }

    private void writePush(String segment, int index) {
        switch (segment) {
            case "constant" -> writeAssemblyInstructions("@" + index,
                    "D=A",
                    "@SP",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M+1");
            case "pointer" -> {
                String thisOrThat = index == 0 ? "THIS" : "THAT";
                writeAssemblyInstructions("@" + thisOrThat,
                        "D=M",
                        "@SP",
                        "A=M",
                        "M=D",
                        "@SP",
                        "M=M+1");
            }
            case "static" -> {
                String variableName = filename + "." + index;
                writeAssemblyInstructions("@" + variableName,
                        "D=M",
                        "@SP",
                        "A=M",
                        "M=D",
                        "@SP",
                        "M=M+1");
            }
            case "temp" -> writeAssemblyInstructions("@" + (5 + index),
                    "D=M",
                    "@SP",
                    "A=M",
                    "M=D",
                    "@SP",
                    "M=M+1");
            default -> writeAssemblyInstructions("@" + index,
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
            case "temp" -> writeAssemblyInstructions("@" + (5 + index),
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
                writeAssemblyInstructions("@SP",
                        "A=M-1",
                        "D=M",
                        "@" + thisOrThat,
                        "M=D",
                        "@SP",
                        "M=M-1");
            }
            case "static" -> {
                String variableName = filename + "." + index;
                writeAssemblyInstructions("@SP",
                        "A=M-1",
                        "D=M",
                        "@" + variableName,
                        "M=D",
                        "@SP",
                        "M=M-1");
            }
            default -> writeAssemblyInstructions("@" + index,
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

    private void writeCommentForPushPop(CommandType command, String segment, int index) {
        String typeAsString = command == CommandType.C_PUSH ? "push" : "pop";
        writeLineIndented(MessageFormat.format("// {0} {1} {2}", typeAsString, segment, index));
    }

    private void writeAssemblyInstructions(String... assemblerCode) {
        for (String line : assemblerCode) {
            writeLineIndented(line);
        }
        try {
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLineIndented(String line) {
        try {
            writer.write("    " + line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLineNotIndented(String line) {
        try {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
