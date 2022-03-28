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
    // push constant 4
    @4
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // call Main.fibonacci 1
     // push returnAddress
    @Main.fibonacci$ret.1
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
    @1
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Main.fibonacci
    0;JMP

(Main.fibonacci$ret.1)
(Sys$WHILE)
    // goto Sys$WHILE
    @Sys$WHILE
    0;JMP

    // function Main.fibonacci 0
    (Main.fibonacci)
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

    // push constant 2
    @2
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // lt
    @SP
    AM=M-1
    D=M
    A=A-1
    D=M-D
    @Main$FALSE.0
    D;JGE
    @SP
    A=M-1
    M=-1
    @Main$CONTINUE.0
    0;JMP

(Main$FALSE.0)
    @SP
    A=M-1
    M=0

(Main$CONTINUE.0)
    // if-goto IF_TRUE
    @SP
    AM=M-1
    D=M
    @Main$IF_TRUE
    D;JNE

    // goto Main$IF_FALSE
    @Main$IF_FALSE
    0;JMP

(Main$IF_TRUE)
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

(Main$IF_FALSE)
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

    // push constant 2
    @2
    D=A
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

    // call Main.fibonacci 1
     // push returnAddress
    @Main.fibonacci$ret.2
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
    @1
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Main.fibonacci
    0;JMP

(Main.fibonacci$ret.2)
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

    // push constant 1
    @1
    D=A
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

    // call Main.fibonacci 1
     // push returnAddress
    @Main.fibonacci$ret.3
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
    @1
    D=D-A
    @ARG
    M=D
    @SP
    D=M
    @LCL
    M=D
    @Main.fibonacci
    0;JMP

(Main.fibonacci$ret.3)
    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

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

