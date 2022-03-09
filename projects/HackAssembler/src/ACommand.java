public class ACommand implements Command {

    private String label;

    public ACommand(String line) {
        this.label = line;
    }

    public String getLabel() {
        return label;
    }

    String interpretAddress(Integer address) {
        StringBuilder sb = new StringBuilder(Integer.toBinaryString(address));
        while (sb.length() < 15) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }
}
