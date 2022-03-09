import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void test() throws FileNotFoundException {
        File file = new File("/home/moritz/Documents/nand2tetris/projects/06/add/MyTest.asm");
        Parser parser = new Parser(file);

        List<Command> commandsFromParser = parser.secondPass();

        System.out.println(commandsFromParser);
        assertEquals(2, commandsFromParser.size());
    }

    @Test
    void test2() {
        String st = "@test";
        System.out.println(st.split("@")[0]);
        System.out.println(st.split("@")[1]);
    }
}