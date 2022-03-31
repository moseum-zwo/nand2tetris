import org.junit.jupiter.api.Test;
import token.Identifier;
import token.Keyword;
import token.Symbol;
import token.Token;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class CompilationEngineTest {

    @Test
    void testCompilation() throws IOException {
        List<Token> tokens = new ArrayList<>(List.of(new Keyword("class"), new Identifier("MyClass"), new Symbol("{"),
                new Keyword("field"), new Keyword("int"), new Identifier("x"), new Symbol(","), new Identifier("y"), new Symbol(";"),
                new Symbol("}")));
        CompilationEngine compilationEngine = new CompilationEngine(new File("src/test/resources/outputTemp.xml"), tokens);
        compilationEngine.writeXMLFile();
    }
}