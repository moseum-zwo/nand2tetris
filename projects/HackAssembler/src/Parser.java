import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Parser {

    private final Scanner scanner1;

    private final Scanner scanner2;

    public Parser(File file) throws FileNotFoundException {
        this.scanner1 = new Scanner(file);
        this.scanner2 = new Scanner(file);
    }

    public Map<String, Integer> firstPass() {
        HashMap<String, Integer> loops = new HashMap<>();
        int lineCounter = 0;
        while (scanner1.hasNextLine()) {
            String line = readLineAndRemoveWhitespace(scanner1);
            if (!line.isEmpty()) {
                //Distinction
                if (line.startsWith("(")) {
                    line = line.substring(line.indexOf("(") + 1);
                    line = line.substring(0, line.indexOf(")"));
                    loops.put(line, lineCounter);
                } else {
                    lineCounter++;
                }
            }
        }
        return loops;
    }

    List<Command> secondPass() {
        ArrayList<Command> commands = new ArrayList<>();
        while (scanner2.hasNextLine()) {
            String line = readLineAndRemoveWhitespace(scanner2);
            if (!line.isEmpty()) {
                //Distinction
                if (line.startsWith("@")) {
                    commands.add(new ACommand(line.split("@")[1]));
                } else if (line.startsWith("(")) {
                    //ignore
                } else {
                    commands.add(new CCommand(line));
                }
            }
        }
        return commands;
    }

    private String readLineAndRemoveWhitespace(Scanner scanner) {
        return scanner.nextLine().split("//")[0].trim();
    }
}
