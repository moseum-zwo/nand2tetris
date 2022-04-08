import token.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class CompilationEngine {

    private BufferedWriter writer;

    private List<Token> tokens;

    private SymbolTable classSymbolTable;

    private SymbolTable subroutineSymbolTable;

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
        checkAndWriteIdentifierClass();

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

    private void writeTokenToFileIdentifierUsed(Token varName) {
        SymbolTableEntry currentEntry;
        try {
            currentEntry = subroutineSymbolTable.findByName(varName.getValue());
        } catch (NoSuchElementException ignored) {
            try {
                currentEntry = classSymbolTable.findByName(varName.getValue());
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("Variable is found in neither symbol table.");
            }
        }
        String sb = varName.getValue() + " | " + "kind: " + currentEntry.getKind() + "; index: " + currentEntry.getIndex() + "; used ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
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
        checkAndWriteIdentifierSubroutineDeclaration();

        //'('
        checkAndWriteSymbol("(");

        //parameterList
        compileParameterList();

        //')'
        checkAndWriteSymbol(")");

        //subroutineBody
        compileSubroutineBody();
    }

    private String checkAndWriteType(String... types) {
        Token type = nextToken();
        if (type.getClass().equals(Keyword.class)) {
            checkToken(type, Keyword.class, types);
        } else if (type.getClass().equals(Identifier.class)) {
            checkToken(type, Identifier.class);
        } else {
            throw new UnexpectedTokenException("Keyword or Identifier expected.");
        }
        writeTokenToFile(type);
        return type.getValue();
    }

    private void compileParameterList() {
        try {
            typeVarNamePossiblyManyTimes("argument");
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void typeVarNamePossiblyManyTimes(String kind) {
        //type
        String type = checkAndWriteType("int", "char", "boolean");

        //varName
        checkAndWriteNewIdentifier(type, kind);

        //(',' type varName)*
        try {
            while (true) {
                //','
                checkAndWriteSymbol(",");

                //type
                checkAndWriteType("int", "char", "boolean");

                //varName
                checkAndWriteNewIdentifier(type, kind);
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
        multipleVariableDeclarationSubroutineLevel("var");

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
        Token staticOrField;
        try {
            //('static' | 'field')
            staticOrField = nextToken();
            checkToken(staticOrField, Keyword.class, "static", "field");
            writeTokenToFile(staticOrField);
        } catch (UnexpectedTokenException e) {
            throw new UnexpectedTokenException("No more variable declarations.");
        }

        //type varName (',' varName)*
        multipleVariableDeclaration(staticOrField.getValue());

        //';'
        checkAndWriteSymbol(";");
    }

    private void multipleVariableDeclaration(String kind) {
        //type
        String type = checkAndWriteType("int", "char", "boolean");

        //varName
        checkAndWriteNewIdentifier(type, kind);

        //(',' type varName)*
        try {
            while (true) {
                //','
                checkAndWriteSymbol(",");

                //varName
                checkAndWriteNewIdentifier(type, kind);
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void multipleVariableDeclarationSubroutineLevel(String kind) {
        //type
        String type = checkAndWriteType("int", "char", "boolean");

        //varName
        checkAndWriteNewIdentifier(type, kind);

        //(',' type varName)*
        try {
            while (true) {
                //','
                checkAndWriteSymbol(",");

                //varName
                checkAndWriteNewIdentifier(type, kind);
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

    private void checkAndWriteNewIdentifier(String type, String kind) {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        try {
            switch (kind) {
                case "field" -> classSymbolTable.addEntryToSymbolTable(new SymbolTableEntry(varName.getValue(), kind, type, classSymbolTable.getAndIncrementFieldCounter()));
                case "static" -> classSymbolTable.addEntryToSymbolTable(new SymbolTableEntry(varName.getValue(), kind, type, classSymbolTable.getAndIncrementStaticCounter()));
                case "argument" -> subroutineSymbolTable.addEntryToSymbolTable(new SymbolTableEntry(varName.getValue(), kind, type, subroutineSymbolTable.getAndIncrementArgCounter()));
                case "var" -> subroutineSymbolTable.addEntryToSymbolTable(new SymbolTableEntry(varName.getValue(), kind, type, subroutineSymbolTable.getAndIncrementVarCounter()));
            }
        } catch (AlreadyInSymbolTableException e) {
            e.printStackTrace();
        }
        SymbolTableEntry currentEntry;
        if (kind.equals("field") || kind.equals("static")) {
            currentEntry = classSymbolTable.getLastEntry();
        } else {
            currentEntry = subroutineSymbolTable.getLastEntry();
        }
        String sb = varName.getValue() + " | " + "kind: " + kind + "; index: " + currentEntry.getIndex() + "; defined ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
    }

    private void checkAndWriteIdentifierBeingUsed() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        SymbolTableEntry currentEntry;
        try {
            currentEntry = subroutineSymbolTable.findByName(varName.getValue());
        } catch (NoSuchElementException ignored) {
            try {
                currentEntry = classSymbolTable.findByName(varName.getValue());
            } catch (NoSuchElementException e) {
                throw new NoSuchElementException("Variable is found in neither symbol table.");
            }
        }
        String sb = varName.getValue() + " | " + "kind: " + currentEntry.getKind() + "; index: " + currentEntry.getIndex() + "; used ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
    }

    private void checkAndWriteIdentifierClass() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        String sb = varName.getValue() + " | " + "kind: class" + "; defined ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
        classSymbolTable = new SymbolTable();
    }

    private void checkAndWriteIdentifierSubroutineDeclaration() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        String sb = varName.getValue() + " | " + "kind: subroutine" + "; defined ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
        subroutineSymbolTable = new SymbolTable();
    }

    private void checkAndWriteIdentifierSubroutineName() {
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
        checkAndWriteIdentifierBeingUsed();

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
            //TODO
            Token nextTokenPeeked = peekNextToken();
            if (nextTokenPeeked.getClass().equals(Symbol.class)) {
                //subroutineCall
                if (nextTokenPeeked.getValue().equals("(") || nextTokenPeeked.getValue().equals(".")) {
                    rollbackToken();
                    compileSubroutineCall();
                } else if (nextTokenPeeked.getValue().equals("[")) {
                    writeTokenToFileIdentifierUsed(token);

                    checkAndWriteSymbol("[");

                    compileExpression();

                    checkAndWriteSymbol("]");
                } else {
                    writeTokenToFileIdentifierUsed(token);
                }
            } else {
                writeTokenToFileIdentifierUsed(token);
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
        checkAndWriteIdentifierSubroutineName();

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
