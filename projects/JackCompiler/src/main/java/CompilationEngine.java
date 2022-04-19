import token.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class CompilationEngine {

    private BufferedWriter xmlWriter;

    private VMWriter vmWriter;

    private List<Token> tokens;

    private SymbolTable classSymbolTable;

    private SymbolTable subroutineSymbolTable;

    private final String className;

    private int pointer;

    private int labelCount;

    public CompilationEngine(File outputFile, List<Token> tokens) throws IOException {
        this.xmlWriter = new BufferedWriter(new FileWriter(outputFile + ".xml"));
        this.vmWriter = new VMWriter(new File(outputFile + ".vm"));
        this.tokens = tokens;
        this.className = outputFile.getName();
        this.pointer = 0;
        this.labelCount = 0;
    }

    public void writeXMLFile() throws IOException {
        writeLine("<tokens>");
        compileClassDeclaration();
        writeLine("</tokens>");
        xmlWriter.close();
        vmWriter.close();
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
        writeTokenToXMLFile(classKeywordToken);
    }

    private void writeTokenToXMLFile(Token token) {
        writeLine(token.getOpeningTag() + " " + token.getValue() + " " + token.getClosingTag());
    }

    private SymbolTableEntry writeTokenToFileIdentifierUsed(Token varName) {
        SymbolTableEntry currentEntry = lookForEntryInSymbolTable(varName);
        String sb = varName.getValue() + " | " + "kind: " + currentEntry.getKind() + "; index: " + currentEntry.getIndex() + "; used ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
        return currentEntry;
    }

    private void writeLine(String str) {
        try {
            xmlWriter.write(str);
            xmlWriter.newLine();
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
        String subroutineType;
        try {
            //('constructor' | 'function' | 'method')
            Token constructorOrFunctionOrMethod = nextToken();
            checkToken(constructorOrFunctionOrMethod, Keyword.class, "constructor", "function", "method");
            writeTokenToXMLFile(constructorOrFunctionOrMethod);
            subroutineType = constructorOrFunctionOrMethod.getValue();
        } catch (UnexpectedTokenException e) {
            throw new UnexpectedTokenException("No more subroutine declarations.");
        }

        //basic technique for accessing object data:
        // push 8000
        // pop pointer 0
        // push 17
        // pop this 0

        //constructor contract:
        // (i) arrange memory block to store the new object,
        // (ii) return its base address to caller (push to stack)

        //How does constructor know about object size? -> consult classSymbolTable and count "field" entries.
        // How to create space on heap?
        // push "numOfFieldEntries"
        // call Memory.alloc 1
        // pop pointer 0

        //initialize object:
        // push argument 0
        // pop this 0
        // push argument 1
        // pop this 1

        //return this
        // push pointer 0
        // return

        //type
        checkAndWriteType("void", "int", "char", "boolean");

        //subroutineName
        String functionName = checkAndWriteIdentifierSubroutineDeclaration();

        //'('
        checkAndWriteSymbol("(");

        //parameterList
        compileParameterList();

        //')'
        checkAndWriteSymbol(")");

        //subroutineBody
        compileSubroutineBody(subroutineType, functionName);
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
        writeTokenToXMLFile(type);
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

    private void compileSubroutineBody(String subroutineType, String functionName) {
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

        vmWriter.writeFunction(className, functionName, subroutineSymbolTable.numOfLocalVariables());

        switch (subroutineType) {
            case "constructor" -> {
                int fields = classSymbolTable.numOfFieldVariables();
                vmWriter.writeConstructorCode(fields);
            }
            case "method" -> {
                try {
                    subroutineSymbolTable.addEntryToSymbolTable(new SymbolTableEntry("this",
                            "argument",
                            className,
                            subroutineSymbolTable.getAndIncrementArgCounter())
                    );
                } catch (AlreadyInSymbolTableException e) {
                    e.printStackTrace();
                }
                vmWriter.writePush("argument", 0);
                vmWriter.writePop("pointer", 0);
            }
            case "function" -> vmWriter.writePop("temp", 0);
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
            writeTokenToXMLFile(staticOrField);
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
        writeTokenToXMLFile(token);
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

    private SymbolTableEntry checkAndWriteIdentifierBeingUsed() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        SymbolTableEntry currentEntry = lookForEntryInSymbolTable(varName);
        String sb = varName.getValue() + " | " + "kind: " + currentEntry.getKind() + "; index: " + currentEntry.getIndex() + "; used ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
        return currentEntry;
    }

    private SymbolTableEntry lookForEntryInSymbolTable(Token varName) {
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
        return currentEntry;
    }

    private void checkAndWriteIdentifierClass() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        String sb = varName.getValue() + " | " + "kind: class" + "; defined ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
        classSymbolTable = new SymbolTable();
    }

    private String checkAndWriteIdentifierSubroutineDeclaration() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        String sb = varName.getValue() + " | " + "kind: subroutine" + "; defined ";
        writeLine(varName.getOpeningTag() + " " + sb + varName.getClosingTag());
        subroutineSymbolTable = new SymbolTable();
        return varName.getValue();
    }

    private String checkAndWriteIdentifierSubroutineName() {
        Token varName = nextToken();
        checkToken(varName, Identifier.class);
        writeTokenToXMLFile(varName);
        return varName.getValue();
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
            case "let":
                compileLet();
                break;
            case "if":
                compileIf();
                break;
            case "while":
                compileWhile();
                break;
            case "do":
                compileDo();
                break;
            case "return":
                compileReturn();
                break;
            default:
                throw new RuntimeException("Unexpected Identifier for statement");
        }
    }

    private void compileLet() {
        //'let'
        checkAndWriteKeyword("let");

        //varName
        SymbolTableEntry entry = checkAndWriteIdentifierBeingUsed();

        boolean isArray = false;

        //('[' expression ']')?
        try {
            //'['
            checkAndWriteSymbol("[");

            //%%

            vmWriter.writePush(entry.getKind(), entry.getIndex());

            //expression
            compileExpression();

            vmWriter.writeArithmetic("+");

            //']'
            checkAndWriteSymbol("]");

            isArray = true;
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }

        //'='
        checkAndWriteSymbol("=");

        //expression
        compileExpression();

        if (isArray) {

        } else {
            vmWriter.writePop(entry.getKind(), entry.getIndex());
        }

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

        String ifFalse = "IF_FALSE" + labelCount;
        String ifTrue = "IF_TRUE" + labelCount;
        labelCount++;
        vmWriter.writeArithmetic("not");
        vmWriter.writeIfGoto(ifFalse);

        //')'
        checkAndWriteSymbol(")");

        //'{'
        checkAndWriteSymbol("{");

        //statements1
        compileStatements();

        //'}'
        checkAndWriteSymbol("}");

        if (peekNextToken().getValue().equals("else")) {
            vmWriter.writeGoto(ifTrue);
        }
        vmWriter.writeLabel(ifFalse);

        //('else' '{' statements '}')?
        try {
            //'else'
            checkAndWriteKeyword("else");

            //'{'
            checkAndWriteSymbol("{");

            //statements
            compileStatements();
            vmWriter.writeLabel(ifTrue);

            //'}'
            checkAndWriteSymbol("}");
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void compileWhile() {
        //'while'
        checkAndWriteKeyword("while");

        String whileLabel = "WHILE" + labelCount;
        String endWhileLabel = "END_WHILE" + labelCount;
        labelCount++;
        vmWriter.writeLabel(whileLabel);

        //'('
        checkAndWriteSymbol("(");

        //expression
        compileExpression();

        vmWriter.writeArithmetic("not");
        vmWriter.writeIfGoto(endWhileLabel);

        //')'
        checkAndWriteSymbol(")");

        //'{'
        checkAndWriteSymbol("{");

        //statements
        compileStatements();

        vmWriter.writeGoto(whileLabel);
        vmWriter.writeLabel(endWhileLabel);

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

        vmWriter.writePop("temp", 0);
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
        } else {
            vmWriter.writePush("constant", 0);
        }

        vmWriter.writeReturn();

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
                writeTokenToXMLFile(op);

                //term
                compileTerm();
                vmWriter.writeArithmetic(op.getValue());
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
    }

    private void compileTerm() {
        //integerConstant | stringConstant | keywordConstant | varName | varName '[' expression ']' | subroutineCall | '(' expression ')' | unaryOp term
        Token token = nextToken();

        if (token.getClass().equals(IntegerConstant.class)) {  //integer constant; number
            writeTokenToXMLFile(token);
            vmWriter.writePush("constant", Integer.parseInt(token.getValue()));
        } else if (token.getClass().equals(StringConstant.class)) { //string constant
            writeTokenToXMLFile(token);
        } else if (token.getClass().equals(Keyword.class) &&    //keyword constant
                (token.getValue().equals("true") || token.getValue().equals("false") || token.getValue().equals("null") || token.getValue().equals("this"))) {
            writeTokenToXMLFile(token);
            vmWriter.writeKeywordConstant(token.getValue());
        } else if (token.getClass().equals(Symbol.class) && token.getValue().equals("(")) {     //'(' expression ')'
            writeTokenToXMLFile(token);

            compileExpression();

            checkAndWriteSymbol(")");
        } else if (token.getClass().equals(Symbol.class)) {     //unary op; "op exp"
            if (token.getValue().equals("~")) {
                writeTokenToXMLFile(token);

                compileTerm();
                vmWriter.writeArithmetic("not");
            } else if (token.getValue().equals("-")) {
                writeTokenToXMLFile(token);

                compileTerm();
                vmWriter.writeArithmetic("neg");
            }
        } else if (token.getClass().equals(Identifier.class)) {
            Token nextTokenPeeked = peekNextToken();
            if (nextTokenPeeked.getClass().equals(Symbol.class)) {
                if (nextTokenPeeked.getValue().equals("(") || nextTokenPeeked.getValue().equals(".")) {     //subroutineCall
                    rollbackToken();
                    compileSubroutineCall();
                } else if (nextTokenPeeked.getValue().equals("[")) {    //array; varName '[' expression ']'
                    writeTokenToFileIdentifierUsed(token);

                    //%%

                    SymbolTableEntry entry = lookForEntryInSymbolTable(token);

                    vmWriter.writePush(entry.getKind(), entry.getIndex());

                    checkAndWriteSymbol("[");

                    compileExpression();

                    vmWriter.writeArithmetic("+");

                    vmWriter.writePop("temp", 0);

                    vmWriter.writePop("pointer", 1);

                    vmWriter.writePush("temp", 0);

                    vmWriter.writePop("that", 0);

                    checkAndWriteSymbol("]");
                } else {        //varName; variable
                    SymbolTableEntry entry = writeTokenToFileIdentifierUsed(token);
                    vmWriter.writePush(entry.getKind(), entry.getIndex());
                }
            } else {        //varName; variable
                writeTokenToFileIdentifierUsed(token);
            }
        }
    }

    private void compileSubroutineCall() {
        StringBuilder functionName = new StringBuilder();
        int argumentCounter = 0;
        //((className | varName) '.')?
        try {
            //(className | varName)
            Token classNameOrVarName = nextToken();
            checkToken(classNameOrVarName, Identifier.class);

            //'.'
            Token dot = nextToken();
            checkToken(dot, Symbol.class, ".");

            writeTokenToXMLFile(classNameOrVarName);
            writeTokenToXMLFile(dot);

            //method-call code if object is of different kind.
            try {
                SymbolTableEntry entry = lookForEntryInSymbolTable(classNameOrVarName); //is a variable referenced in symbol table -> varName
                functionName.append(entry.getType()).append(".");
                argumentCounter++;
                vmWriter.writePush(entry.getKind(), entry.getIndex());
            } catch (NoSuchElementException ignored) {
                //not a variable -> className -> function or constructor
                functionName.append(classNameOrVarName.getValue()).append(".");
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
            rollbackToken();
            functionName.append(className).append(".");

            //Push pointer 0, don't forget to pop it to temp 0 if it is actually a function.
            vmWriter.writePush("pointer", 0);
            argumentCounter++;
        }

        //subroutineName
        String s = checkAndWriteIdentifierSubroutineName();
        functionName.append(s);

        //'('
        checkAndWriteSymbol("(");

        //expressionList
        Token firstTokenExpression = peekNextToken();
        if (!firstTokenExpression.getValue().equals(")")) {
            argumentCounter = argumentCounter + compileExpressionList();
        }

        //')'
        checkAndWriteSymbol(")");

        vmWriter.writeCall(functionName.toString(), argumentCounter);
    }

    private int compileExpressionList() {
        try {
            return compileExpressionListAvailable();
        } catch (UnexpectedTokenException e) {
            rollbackToken();
        }
        //no arguments
        return 0;
    }

    private int compileExpressionListAvailable() {

        int argumentCounter = 1;

        //expression
        compileExpression();

        //(',' expression)*
        try {
            while (true) {
                //';'
                checkAndWriteSymbol(",");

                //expression
                compileExpression();
                argumentCounter++;
            }
        } catch (UnexpectedTokenException e) {
            rollbackToken();
            return argumentCounter;
        }
    }
}
