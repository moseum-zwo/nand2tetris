import token.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CompilationEngine {

    private BufferedWriter writer;

    private List<Token> tokens;

    private int pointer;

    public CompilationEngine(File outputFile, List<Token> tokens) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(outputFile));
        this.tokens = tokens;
        this.pointer = 0;
    }

    public void writeXMLFile() throws IOException {
        writeLine("<tokens>");
        compileClassDeclaration();
        writeLine("</tokens>");
        writer.close();
    }

    private void compileClassDeclaration() {
        //class
        checkAndWriteKeyword("class");

        //className
        checkAndWriteIdentifier();

        //{
        checkAndWriteSymbol("{");

        //classVarDec*
        compileClassVariableDeclaration();

        //subroutineDec*
        compileSubroutineDeclarations();

        //}
        checkAndWriteSymbol("}");
    }

    private void checkAndWriteKeyword(String keyword) {
        Token classKeywordToken = nextToken();
        checkToken(classKeywordToken, Keyword.class, keyword);
        writeTokenToFile(classKeywordToken);
    }

    private void writeTokenToFile(Token token) {
        writeLine(token.getOpeningTag() + " " + token.getValue() + " " + token.getClosingTag());
    }

    private void writeLine(String str) {
        try {
            writer.write(str);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkToken(Token token, Class<? extends Token> clazz, String... expectedTokenValue) {
        if (!(token.getClass().equals(clazz))) {
            throw new UnexpectedTokenException(clazz.getSimpleName() + " expected");
        }
        if (expectedTokenValue.length > 0) {
            boolean noMatch = true;
            for (String expectedValue : expectedTokenValue) {
                if (expectedValue.equals(token.getValue())) {
                    noMatch = false;
                }
            }
            if (noMatch) {
                throw new UnexpectedTokenException("Value " + token.getValue() + " is none of the expected values. " +
                        "Expected are: " + Arrays.toString(expectedTokenValue));
            }
        }
    }

    private Token nextToken() {
        return tokens.get(pointer++);
    }

    private Token peekNextToken() {
        return tokens.get(pointer);
    }

    private void compileSubroutineDeclarations() {
        try {
            while (true) {
                compileSubroutineDeclaration();
            }
        } catch (Exception e) {
            rollbackToken();
        }
    }

    private void compileSubroutineDeclaration() {
        try {
            //('constructor' | 'function' | 'method')
            Token constructorOrFunctionOrMethod = nextToken();
            checkToken(constructorOrFunctionOrMethod, Keyword.class, "constructor", "function", "method");
            writeTokenToFile(constructorOrFunctionOrMethod);
        } catch (UnexpectedTokenException e) {
            throw new UnexpectedTokenException("No more subroutine declarations.");
        }

        //type
        checkAndWriteType("void", "int", "char", "boolean");

        //subroutineName
        checkAndWriteIdentifier();

        //'('
        checkAndWriteSymbol("(");

        //parameterList
        compileParameterList();

        //')'
        checkAndWriteSymbol(")");

        //subroutineBody
        compileSubroutineBody();
    }

    private void checkAndWriteType(String... types) {
        Token type = nextToken();
        if (type.getClass().equals(Keyword.class)) {
            checkToken(type, Keyword.class, types);
        } else if (type.getClass().equals(Identifier.class)) {
            checkToken(type, Identifier.class);
        } else {
            throw new UnexpectedTokenException("Keyword or Identifier expected.");
        }
        writeTokenToFile(type);
    }

    private void compileParameterList() {
        try {
            typeVarNamePossiblyManyTimes();
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void typeVarNamePossiblyManyTimes() {
        //type
        checkAndWriteType("int", "char", "boolean");

        //varName
        checkAndWriteIdentifier();

        //(',' type varName)*
        try {
            while (true) {
                //','
                checkAndWriteSymbol(",");

                //type
                checkAndWriteType("int", "char", "boolean");

                //varName
                checkAndWriteIdentifier();
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }

    }

    private void compileSubroutineBody() {
        //'{'
        checkAndWriteSymbol("{");

        //varDec*
        try {
            while (true) {
                compileVariableDeclaration();
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }

        //statements
        compileStatements();

        //'}'
        checkAndWriteSymbol("}");
    }

    private void compileVariableDeclaration() {
        //'var'
        checkAndWriteKeyword("var");

        //type varName (',' varName)*
        multipleVariableDeclaration();

        //';'
        checkAndWriteSymbol(";");
    }

    private void compileClassVariableDeclaration() {
        try {
            while (true) {
                compileLineInClassVariableDeclaration();
            }
        } catch (Exception e) {
            rollbackToken();
        }
    }

    private void compileLineInClassVariableDeclaration() {
        try {
            //('static' | 'field')
            Token staticOrField = nextToken();
            checkToken(staticOrField, Keyword.class, "static", "field");
            writeTokenToFile(staticOrField);
        } catch (UnexpectedTokenException e) {
            throw new UnexpectedTokenException("No more variable declarations.");
        }

        //type varName (',' varName)*
        multipleVariableDeclaration();

        //';'
        checkAndWriteSymbol(";");
    }

    private void multipleVariableDeclaration() {
        //type
        checkAndWriteType("int", "char", "boolean");

        //varName
        checkAndWriteIdentifier();

        //(',' type varName)*
        try {
            while (true) {
                //','
                checkAndWriteSymbol(",");

                //varName
                checkAndWriteIdentifier();
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void checkAndWriteSymbol(String symbol) {
        Token token = nextToken();
        checkToken(token, Symbol.class, symbol);
        writeTokenToFile(token);
    }

    private void checkAndWriteIdentifier() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        writeTokenToFile(varName);
    }

    private void rollbackToken() {
        pointer--;
    }

    private void compileStatements() {
        try {
            while (true) {
                compileStatement();
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void compileStatement() {
        Token statementKeyword = nextToken();
        checkToken(statementKeyword, Keyword.class, "let", "if", "while", "do", "return");
        rollbackToken();
        switch (statementKeyword.getValue()) {
            case "let": compileLet(); break;
            case "if": compileIf(); break;
            case "while": compileWhile(); break;
            case "do": compileDo(); break;
            case "return": compileReturn(); break;
            default: throw new RuntimeException("Unexpected Identifier for statement");
        }
    }

    private void compileLet() {
        //'let'
        checkAndWriteKeyword("let");

        //varName
        checkAndWriteIdentifier();

        //('[' expression ']')?
        try {
            //'['
            checkAndWriteSymbol("[");

            //expression
            compileExpression();

            //']'
            checkAndWriteSymbol("]");
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }

        //'='
        checkAndWriteSymbol("=");

        //expression
        compileExpression();

        //';'
        checkAndWriteSymbol(";");
    }

    private void compileIf() {
        //'if'
        checkAndWriteKeyword("if");

        //'('
        checkAndWriteSymbol("(");

        //expression
        compileExpression();

        //')'
        checkAndWriteSymbol(")");

        //'{'
        checkAndWriteSymbol("{");

        //statements
        compileStatements();

        //'}'
        checkAndWriteSymbol("}");

        //('else' '{' statements '}')?
        try {
            //'else'
            checkAndWriteKeyword("else");

            //'{'
            checkAndWriteSymbol("{");

            //statements
            compileStatements();

            //'}'
            checkAndWriteSymbol("}");
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void compileWhile() {
        //'while'
        checkAndWriteKeyword("while");

        //'('
        checkAndWriteSymbol("(");

        //expression
        compileExpression();

        //')'
        checkAndWriteSymbol(")");

        //'{'
        checkAndWriteSymbol("{");

        //statements
        compileStatements();

        //'}'
        checkAndWriteSymbol("}");
    }

    private void compileDo() {
        //'do'
        checkAndWriteKeyword("do");

        //subroutineCall
        compileSubroutineCall();

        //';'
        checkAndWriteSymbol(";");
    }

    private void compileReturn() {
        //return
        checkAndWriteKeyword("return");

        //expression?
        if (!peekNextToken().getClass().equals(Symbol.class) && !peekNextToken().getValue().equals(";")) {
            try {
                compileExpression();
            } catch (UnexpectedTokenException e) {
                rollbackToken();
            }
        }

        //';'
        checkAndWriteSymbol(";");
    }

    private void compileExpression() {
        //term
        compileTerm();

        //(op term)*
        try {
            while (true) {
                //op
                Token op = nextToken();
                checkToken(op, Symbol.class, "+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "=");
                writeTokenToFile(op);

                //term
                compileTerm();
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void compileTerm() {
//        Token varName = nextToken();
//        writeTokenToFile(varName);

        //integerConstant | stringConstant | keywordConstant | varName | varName '[' expression ']' | subroutineCall | '(' expression ')' | unaryOp term
        Token token = nextToken();
        if (token.getClass().equals(IntegerConstant.class) || token.getClass().equals(StringConstant.class)) {
            writeTokenToFile(token);
        } else if (token.getClass().equals(Keyword.class) &&
                (token.getValue().equals("true") || token.getValue().equals("false") || token.getValue().equals("null") || token.getValue().equals("this"))) {
            writeTokenToFile(token);
        } else if (token.getClass().equals(Symbol.class) && token.getValue().equals("(")) {
            writeTokenToFile(token);

            compileExpression();

            checkAndWriteSymbol(")");
        } else if (token.getClass().equals(Symbol.class) && (token.getValue().equals("~") || token.getValue().equals("-"))) {
            writeTokenToFile(token);

            compileTerm();
        } else if (token.getClass().equals(Identifier.class)) {
            Token nextTokenPeeked = peekNextToken();
            if (nextTokenPeeked.getClass().equals(Symbol.class)) {
                //subroutineCall
                if (nextTokenPeeked.getValue().equals("(") || nextTokenPeeked.getValue().equals(".")) {
                    rollbackToken();
                    compileSubroutineCall();
                } else if (nextTokenPeeked.getValue().equals("[")) {
                    writeTokenToFile(token);

                    checkAndWriteSymbol("[");

                    compileExpression();

                    checkAndWriteSymbol("]");
                } else {
                    writeTokenToFile(token);
                }
            } else {
                writeTokenToFile(token);
            }
        }
    }

    private void compileSubroutineCall() {
        //((className | varName) '.')?
        try {
            //(className | varName)
            Token classNameOrVarName = nextToken();
            checkToken(classNameOrVarName, Identifier.class);

            //'.'
            Token dot = nextToken();
            checkToken(dot, Symbol.class, ".");

            writeTokenToFile(classNameOrVarName);
            writeTokenToFile(dot);
        } catch (UnexpectedTokenException e) {
            rollbackToken();
            rollbackToken();
        }

        //subroutineName
        checkAndWriteIdentifier();

        //'('
        checkAndWriteSymbol("(");

        //expressionList
        Token firstTokenExpression = peekNextToken();
        if (!firstTokenExpression.getValue().equals(")")) {
            compileExpressionList();
        }

        //')'
        checkAndWriteSymbol(")");
    }

    private void compileExpressionList() {
        try {
            compileExpressionListAvailable();
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void compileExpressionListAvailable() {
        //expression
        compileExpression();

        //(',' expression)*
        try {
            while (true) {
                //';'
                checkAndWriteSymbol(",");

                //expression
                compileExpression();
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }
}
