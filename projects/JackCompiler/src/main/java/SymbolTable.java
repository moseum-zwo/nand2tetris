import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SymbolTable {

    private final List<SymbolTableEntry> entries;

    private int varCounter;

    private int argCounter;

    private int staticCounter;

    private int fieldCounter;

    public SymbolTable() {
        entries = new ArrayList<>();
        this.varCounter = 0;
        this.argCounter = 0;
        this.staticCounter = 0;
        this.fieldCounter = 0;
    }

    public void addEntryToSymbolTable(SymbolTableEntry entry) throws AlreadyInSymbolTableException {
        if (entries.contains(entry)) {
            throw new AlreadyInSymbolTableException("Symbol table already contains entry: " + entry);
        } else {
            this.entries.add(entry);
        }
    }

    public SymbolTableEntry findByName(String name) throws NoSuchElementException {
        //When compiling error-free Jack code, each symbol not found in the symbol tables can be assumed to be
        // either a subroutine name or a class name.
        return entries.stream().filter(entry -> entry.getName().equals(name)).findAny().orElseThrow();
    }

    public int numOfLocalVariables() {
        return entries.stream().filter(entry -> entry.getKind().equals("var")).toList().size();
    }

    public int numOfFieldVariables() {
        return entries.stream().filter(entry -> entry.getKind().equals("field")).toList().size();
    }

    public int getAndIncrementVarCounter() {
        return varCounter++;
    }

    public int getAndIncrementArgCounter() {
        return argCounter++;
    }

    public int getAndIncrementStaticCounter() {
        return staticCounter++;
    }

    public int getAndIncrementFieldCounter() {
        return fieldCounter++;
    }

    public SymbolTableEntry getLastEntry() {
        return entries.get(entries.size() - 1);
    }
}
