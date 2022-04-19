import org.junit.jupiter.api.Test;
import token.*;

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
        CompilationEngine compilationEngine = new CompilationEngine(new File("src/test/resources/outputTemp"), tokens);
        compilationEngine.writeXMLFile();
    }

    @Test
    void testCodeWriting() throws IOException {
        List<Token> tokens = new ArrayList<>(List.of(new Keyword("class"), new Identifier("Test"), new Symbol("{"),
                new Keyword("field"), new Keyword("int"), new Identifier("x"), new Symbol(","), new Identifier("y"), new Symbol(","), new Identifier("z"), new Symbol(";"),
                new Keyword("method"), new Keyword("int"), new Identifier("testMethod"), new Symbol("("), new Symbol(")"), new Symbol("{"),
                new Keyword("let"), new Identifier("x"), new Symbol("="), new IntegerConstant("73"), new Symbol(";"),
                new Keyword("let"), new Identifier("y"), new Symbol("="), new IntegerConstant("75"), new Symbol(";"),
                new Keyword("let"), new Identifier("z"), new Symbol("="), new IntegerConstant("77"), new Symbol(";"),
                new Keyword("let"), new Identifier("x"), new Symbol("="), new Identifier("x"), new Symbol("+"), new Identifier("g"), new Symbol("("), new IntegerConstant("2"), new Symbol(","), new Identifier("y"), new Symbol(","), new Symbol("-"), new Identifier("z"), new Symbol(")"), new Symbol("*"), new IntegerConstant("5"), new Symbol(";"),
                new Symbol("}"),
                new Symbol("}")));
        CompilationEngine compilationEngine = new CompilationEngine(new File("src/test/resources/outputTemp"), tokens);
        compilationEngine.writeXMLFile();
    }

    @Test
    void testCodeWriting2() throws IOException {
        List<Token> tokens = new ArrayList<>(List.of(new Keyword("class"), new Identifier("Test"), new Symbol("{"),
                new Keyword("field"), new Identifier("Array"), new Identifier("arr"), new Symbol(","), new Identifier("bar"), new Symbol(";"),
                new Keyword("method"), new Keyword("void"), new Identifier("testMethod"), new Symbol("("), new Symbol(")"), new Symbol("{"),
                new Keyword("let"), new Identifier("arr"), new Symbol("["), new IntegerConstant("2"), new Symbol("]"), new Symbol("="), new Identifier("bar"), new Symbol("["), new IntegerConstant("3"), new Symbol("]"), new Symbol(";"),
                new Symbol("}"),
                new Symbol("}")));
        CompilationEngine compilationEngine = new CompilationEngine(new File("src/test/resources/outputTemp"), tokens);
        compilationEngine.writeXMLFile();
    }
}