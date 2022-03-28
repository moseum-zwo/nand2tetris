    // Booting
    @256
    D=A
    @SP
    M=D

    // call Sys.init 0
     // push returnAddress
    @Sys.init$ret.0
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push LCL
    @LCL
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push ARG
    @ARG
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push THIS
    @THIS
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // push THAT
    @THAT
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // set ARG
    @SP
    D=M
    @5
    D=D-A
    @0
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Sys.init
    0;JMP

(Sys.init$ret.0)
    // function Sys.init 0
    (Sys.init)
    // push constant 6
    @6
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push constant 8
    @8
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // call Class1.set 2
     // push returnAddress
    @Class1.set$ret.1
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push LCL
    @LCL
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push ARG
    @ARG
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push THIS
    @THIS
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // push THAT
    @THAT
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // set ARG
    @SP
    D=M
    @5
    D=D-A
    @2
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Class1.set
    0;JMP

(Class1.set$ret.1)
    // pop temp 0
    @5
    D=A
    @R13
    M=D
    @SP
    A=M-1
    D=M
    @R13
    A=M
    M=D
    @SP
    M=M-1

    // push constant 23
    @23
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push constant 15
    @15
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // call Class2.set 2
     // push returnAddress
    @Class2.set$ret.2
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push LCL
    @LCL
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push ARG
    @ARG
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push THIS
    @THIS
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // push THAT
    @THAT
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // set ARG
    @SP
    D=M
    @5
    D=D-A
    @2
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Class2.set
    0;JMP

(Class2.set$ret.2)
    // pop temp 0
    @5
    D=A
    @R13
    M=D
    @SP
    A=M-1
    D=M
    @R13
    A=M
    M=D
    @SP
    M=M-1

    // call Class1.get 0
     // push returnAddress
    @Class1.get$ret.3
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push LCL
    @LCL
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push ARG
    @ARG
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push THIS
    @THIS
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // push THAT
    @THAT
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // set ARG
    @SP
    D=M
    @5
    D=D-A
    @0
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Class1.get
    0;JMP

(Class1.get$ret.3)
    // call Class2.get 0
     // push returnAddress
    @Class2.get$ret.4
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push LCL
    @LCL
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push ARG
    @ARG
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
      // push THIS
    @THIS
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // push THAT
    @THAT
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1
     // set ARG
    @SP
    D=M
    @5
    D=D-A
    @0
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Class2.get
    0;JMP

(Class2.get$ret.4)
(Sys$WHILE)
    // goto Sys$WHILE
    @Sys$WHILE
    0;JMP

    // function Class1.set 0
    (Class1.set)
    // push argument 0
    @0
    D=A
    @ARG
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop static 0
    @SP
    A=M-1
    D=M
    @Class1.0
    M=D
    @SP
    M=M-1

    // push argument 1
    @1
    D=A
    @ARG
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop static 1
    @SP
    A=M-1
    D=M
    @Class1.1
    M=D
    @SP
    M=M-1

    // push constant 0
    @0
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // return
     // tempVar endFrame = LCL
    @LCL
    D=M
    @R5
    M=D
     // tempVar retAddr = *(endFrame - 5)
    @LCL
    D=M
    @5
    A=D-A
    D=M
    @R6
    M=D
     // *ARG = pop()
    @SP
    AM=M-1
    D=M
    @ARG
    A=M
    M=D
     // SP = ARG + 1
    @ARG
    D=M+1
    @SP
    M=D
     // THAT = *(endFrame - 1)
    @R5
    A=M-1
    D=M
    @THAT
    M=D
     // THIS = *(endFrame - 2)
    @R5
    D=M
    @2
    A=D-A
    D=M
    @THIS
    M=D
     // ARG = *(endFrame - 3)
    @R5
    D=M
    @3
    A=D-A
    D=M
    @ARG
    M=D
     // LCL = *(endFrame - 4)
    @R5
    D=M
    @4
    A=D-A
    D=M
    @LCL
    M=D
     // goto retAddr
    @R6
    A=M
    0;JMP

    // function Class1.get 0
    (Class1.get)
    // push static 0
    @Class1.0
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push static 1
    @Class1.1
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // sub
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M-D

    // return
     // tempVar endFrame = LCL
    @LCL
    D=M
    @R5
    M=D
     // tempVar retAddr = *(endFrame - 5)
    @LCL
    D=M
    @5
    A=D-A
    D=M
    @R6
    M=D
     // *ARG = pop()
    @SP
    AM=M-1
    D=M
    @ARG
    A=M
    M=D
     // SP = ARG + 1
    @ARG
    D=M+1
    @SP
    M=D
     // THAT = *(endFrame - 1)
    @R5
    A=M-1
    D=M
    @THAT
    M=D
     // THIS = *(endFrame - 2)
    @R5
    D=M
    @2
    A=D-A
    D=M
    @THIS
    M=D
     // ARG = *(endFrame - 3)
    @R5
    D=M
    @3
    A=D-A
    D=M
    @ARG
    M=D
     // LCL = *(endFrame - 4)
    @R5
    D=M
    @4
    A=D-A
    D=M
    @LCL
    M=D
     // goto retAddr
    @R6
    A=M
    0;JMP

    // function Class2.set 0
    (Class2.set)
    // push argument 0
    @0
    D=A
    @ARG
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop static 0
    @SP
    A=M-1
    D=M
    @Class2.0
    M=D
    @SP
    M=M-1

    // push argument 1
    @1
    D=A
    @ARG
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop static 1
    @SP
    A=M-1
    D=M
    @Class2.1
    M=D
    @SP
    M=M-1

    // push constant 0
    @0
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // return
     // tempVar endFrame = LCL
    @LCL
    D=M
    @R5
    M=D
     // tempVar retAddr = *(endFrame - 5)
    @LCL
    D=M
    @5
    A=D-A
    D=M
    @R6
    M=D
     // *ARG = pop()
    @SP
    AM=M-1
    D=M
    @ARG
    A=M
    M=D
     // SP = ARG + 1
    @ARG
    D=M+1
    @SP
    M=D
     // THAT = *(endFrame - 1)
    @R5
    A=M-1
    D=M
    @THAT
    M=D
     // THIS = *(endFrame - 2)
    @R5
    D=M
    @2
    A=D-A
    D=M
    @THIS
    M=D
     // ARG = *(endFrame - 3)
    @R5
    D=M
    @3
    A=D-A
    D=M
    @ARG
    M=D
     // LCL = *(endFrame - 4)
    @R5
    D=M
    @4
    A=D-A
    D=M
    @LCL
    M=D
     // goto retAddr
    @R6
    A=M
    0;JMP

    // function Class2.get 0
    (Class2.get)
    // push static 0
    @Class2.0
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push static 1
    @Class2.1
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // sub
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M-D

    // return
     // tempVar endFrame = LCL
    @LCL
    D=M
    @R5
    M=D
     // tempVar retAddr = *(endFrame - 5)
    @LCL
    D=M
    @5
    A=D-A
    D=M
    @R6
    M=D
     // *ARG = pop()
    @SP
    AM=M-1
    D=M
    @ARG
    A=M
    M=D
     // SP = ARG + 1
    @ARG
    D=M+1
    @SP
    M=D
     // THAT = *(endFrame - 1)
    @R5
    A=M-1
    D=M
    @THAT
    M=D
     // THIS = *(endFrame - 2)
    @R5
    D=M
    @2
    A=D-A
    D=M
    @THIS
    M=D
     // ARG = *(endFrame - 3)
    @R5
    D=M
    @3
    A=D-A
    D=M
    @ARG
    M=D
     // LCL = *(endFrame - 4)
    @R5
    D=M
    @4
    A=D-A
    D=M
    @LCL
    M=D
     // goto retAddr
    @R6
    A=M
    0;JMP

