import java.util.Map;

public class Code {

    private final Map<String, Integer> symbolTable;

    private int intCounter = 16;

    public Code(Map<String, Integer> symbolTable) {
        this.symbolTable = symbolTable;
    }

    public String interpretCommand(Command command) {
        if (command instanceof CCommand) {
            return interpretCCommand((CCommand) command);
        } else if (command instanceof ACommand) {
            return interpretACommand((ACommand) command);
        } else {
            throw new RuntimeException("Command is none of the expected Type.");
        }
    }

    private String interpretACommand(ACommand command) {
        String label = command.getLabel();
        Integer address;
        try {
            address = Integer.parseInt(label);
        } catch (NumberFormatException ex) {
            address = convertLabelToInteger(label);
        }
        return "0" + command.interpretAddress(address);
    }

    private Integer convertLabelToInteger(String label) {
        if (!symbolTable.containsKey(label)) {
            symbolTable.put(label, intCounter++);
        }
        return symbolTable.get(label);
    }

    private String interpretCCommand(CCommand command) {
        return "111" + command.interpretComputation() + command.interpretDestination() + command.interpretJump();
    }
}
