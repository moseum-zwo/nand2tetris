import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CodeTest {

    @Test
    void aCommandTest() {
        Map<String, Integer> symbolTable = new HashMap<>();
        Code code = new Code(symbolTable);

        ACommand aCommand1 = new ACommand("5");
        String s = code.interpretCommand(aCommand1);
        assertEquals("0000000000000101", s);

        ACommand aCommand2 = new ACommand("0");
        s = code.interpretCommand(aCommand2);
        assertEquals("0000000000000000", s);

        System.out.println(Integer.parseInt("27"));
    }
}