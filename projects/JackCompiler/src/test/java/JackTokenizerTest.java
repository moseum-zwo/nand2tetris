import org.junit.jupiter.api.Test;
import token.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JackTokenizerTest {

    private JackTokenizer jackTokenizer = new JackTokenizer();

    @Test
    void testTokenizing1() throws IOException {
        File tempFile = new File("/home/moritz/Documents/nand2tetris/projects/JackCompiler/src/test/resources/TempFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        writer.write("// Comment");
        writer.newLine();
        writer.write("class MyTest { ");
        writer.newLine();
        writer.write("field int x;");
        writer.newLine();
        writer.write("}");

        writer.close();

        List<Token> tokens = jackTokenizer.tokenizeFile(tempFile);

        tempFile.delete();

        assertThat(tokens).containsExactly(new Keyword("class"),
                new Identifier("MyTest"),
                new Symbol("{"),
                new Keyword("field"),
                new Keyword("int"),
                new Identifier("x"),
                new Symbol(";"),
                new Symbol("}"));
    }

    @Test
    void testTokenizing2() throws IOException {
        File tempFile = new File("/home/moritz/Documents/nand2tetris/projects/JackCompiler/src/test/resources/TempFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String stringConstant = "Hello{} my olde World!";
        writer.write("myVar = arr[777];");
        writer.newLine();
        writer.write("var String string = func(123, a_2);");
        writer.newLine();
        writer.write("let string = \""+ stringConstant +"\";");

        writer.close();

        List<Token> tokens = jackTokenizer.tokenizeFile(tempFile);

        tempFile.delete();

        assertThat(tokens).containsExactly(new Identifier("myVar"),
                new Symbol("="),
                new Identifier("arr"),
                new Symbol("["),
                new IntegerConstant("777"),
                new Symbol("]"),
                new Symbol(";"),
                new Keyword("var"),
                new Identifier("String"),
                new Identifier("string"),
                new Symbol("="),
                new Identifier("func"),
                new Symbol("("),
                new IntegerConstant("123"),
                new Symbol(","),
                new Identifier("a_2"),
                new Symbol(")"),
                new Symbol(";"),
                new Keyword("let"),
                new Identifier("string"),
                new Symbol("="),
                new StringConstant(stringConstant),
                new Symbol(";")
                );
    }

    @Test
    void testTokenizing3() throws IOException {
        File tempFile = new File("/home/moritz/Documents/nand2tetris/projects/JackCompiler/src/test/resources/TempFile.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line1 = "do Output.printString(\"Test 1: expected result: 5; actual result:_\");";
        String line2 = "do Output.printInt(b[2]);";
        String line3 = "do Output.println();";
        writer.write(line1);
        writer.newLine();
        writer.write(line2);
        writer.newLine();
        writer.write(line3);
        writer.newLine();

        writer.close();

        List<Token> tokens = jackTokenizer.tokenizeFile(tempFile);

        System.out.println(tokens);

//        tempFile.delete();
//
//        assertThat(tokens).containsExactly(new Identifier("myVar"),
//                new Symbol("="),
//                new Identifier("arr"),
//                new Symbol("["),
//                new IntegerConstant("777"),
//                new Symbol("]"),
//                new Symbol(";"),
//                new Keyword("var"),
//                new Identifier("String"),
//                new Identifier("string"),
//                new Symbol("="),
//                new Identifier("func"),
//                new Symbol("("),
//                new IntegerConstant("123"),
//                new Symbol(","),
//                new Identifier("a_2"),
//                new Symbol(")"),
//                new Symbol(";"),
//                new Keyword("let"),
//                new Identifier("string"),
//                new Symbol("="),
//                new StringConstant(line1),
//                new Symbol(";")
//                );
    }
}
