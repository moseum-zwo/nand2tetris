import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HackAssembler {
    public static void main(String[] args) throws IOException {
        Map<String, Integer> symbolTable = new HashMap<>();
        addPredefinedSymbols(symbolTable);

        String asmFilename = args[0];
        String hackFilename = asmFilename.replace(".asm", ".hack");
        Parser parser = new Parser(new File(asmFilename));
        Code code = new Code(symbolTable);
        BufferedWriter writer = new BufferedWriter(new FileWriter(hackFilename));

        symbolTable.putAll(parser.firstPass());
        List<Command> commands = parser.secondPass();
        for (Command command : commands) {
            String binaryCommand = code.interpretCommand(command);
            writer.write(binaryCommand);
            writer.newLine();
        }

        writer.close();
    }

    private static void addPredefinedSymbols(Map<String, Integer> symbolTable) {
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
    }
}
