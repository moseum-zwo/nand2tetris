import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {

    private final Scanner scanner;

    private String currentCommand;

    private CommandType commandType;

    private String arg1;

    private Integer arg2;

    boolean isComment;

    public Parser(File file) throws FileNotFoundException {
        this.scanner = new Scanner(file);
    }

    public boolean hasMoreCommands() {
        return this.scanner.hasNextLine();
    }

    public void advance() {
        String currentLine = this.scanner.nextLine().trim();
        isComment = currentLine.startsWith("//") || currentLine.length() == 0;
        if (!isComment) {
            String[] elements = currentLine.split(" ");
            Arrays.setAll(elements, i -> elements[i].trim());
            currentCommand = elements[0];
            commandType = switch (currentCommand) {
                case "push" -> CommandType.C_PUSH;
                case "pop" -> CommandType.C_POP;
                case "add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not" -> CommandType.C_ARITHMETIC;
                case "label" -> CommandType.C_LABEL;
                case "if-goto" -> CommandType.C_IF;
                case "goto" -> CommandType.C_GOTO;
                case "function" -> CommandType.C_FUNCTION;
                case "return" -> CommandType.C_RETURN;
                case "call" -> CommandType.C_CALL;
                default -> throw new RuntimeException("Parser, unhandled Command: " + currentCommand);
            };
            determineArg1(elements);
            determineArg2(elements);
        }
    }

    private void determineArg1(String[] elements) {
        if (commandType == CommandType.C_ARITHMETIC) {
            arg1 = elements[0];
        } else if (commandType == CommandType.C_RETURN) {
            arg1 = null;
        } else {
            arg1 = elements[1];
        }
    }

    private void determineArg2(String[] elements) {
        if (commandType == CommandType.C_PUSH || commandType == CommandType.C_POP || commandType == CommandType.C_FUNCTION || commandType == CommandType.C_CALL) {
            arg2 = Integer.parseInt(elements[2]);
        } else {
            arg2 = null;
        }
    }

    public CommandType commandType() {
        return commandType;
    }

    /**
     * @return Returns the first argument of the current command. In the case of C_ARITHMETIC, the command itself
     * (add, sub, etc.) is returned. Should not be called if the current command is C_RETURN.
     */
    public String arg1() {
        return arg1;
    }

    /**
     * @return Returns the second argument of the current command. Should be called only if the current command is
     * C_PUSH, C_POP, C_FUNCTION or C_CALL.
     */
    public Integer arg2() {
        return arg2;
    }
}
