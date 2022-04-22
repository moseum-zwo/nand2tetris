import java.util.Objects;

public class SymbolTableEntry {

    private final String name;

    private final String kind;

    private final String type;

    private int index;

    public SymbolTableEntry(String name, String kind, String type, int index) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }

    public String getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolTableEntry that = (SymbolTableEntry) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public void advanceIndexByOne() {
        this.index++;
    }
}
