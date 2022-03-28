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
    // push constant 4,000
    @4000
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop pointer 0
    @SP
    A=M-1
    D=M
    @THIS
    M=D
    @SP
    M=M-1

    // push constant 5,000
    @5000
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop pointer 1
    @SP
    A=M-1
    D=M
    @THAT
    M=D
    @SP
    M=M-1

    // call Sys.main 0
     // push returnAddress
    @Sys.main$ret.1
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
    @Sys.main
    0;JMP

(Sys.main$ret.1)
    // pop temp 1
    @6
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

(Sys$LOOP)
    // goto LOOP
    @LOOP
    0;JMP

    // function Sys.main 5
    (Sys.main)
    //Initialize localVar 1 of 5
    @SP
    A=M
    M=0
    @SP
    M=M+1
    //Initialize localVar 2 of 5
    @SP
    A=M
    M=0
    @SP
    M=M+1
    //Initialize localVar 3 of 5
    @SP
    A=M
    M=0
    @SP
    M=M+1
    //Initialize localVar 4 of 5
    @SP
    A=M
    M=0
    @SP
    M=M+1
    //Initialize localVar 5 of 5
    @SP
    A=M
    M=0
    @SP
    M=M+1
    // push constant 4,001
    @4001
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop pointer 0
    @SP
    A=M-1
    D=M
    @THIS
    M=D
    @SP
    M=M-1

    // push constant 5,001
    @5001
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop pointer 1
    @SP
    A=M-1
    D=M
    @THAT
    M=D
    @SP
    M=M-1

    // push constant 200
    @200
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop local 1
    @1
    D=A
    @LCL
    D=D+M
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

    // push constant 40
    @40
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop local 2
    @2
    D=A
    @LCL
    D=D+M
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

    // push constant 6
    @6
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop local 3
    @3
    D=A
    @LCL
    D=D+M
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

    // push constant 123
    @123
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // call Sys.add12 1
     // push returnAddress
    @Sys.add12$ret.2
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
    @Sys.add12
    0;JMP

(Sys.add12$ret.2)
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

    // push local 0
    @0
    D=A
    @LCL
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push local 1
    @1
    D=A
    @LCL
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push local 2
    @2
    D=A
    @LCL
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push local 3
    @3
    D=A
    @LCL
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push local 4
    @4
    D=A
    @LCL
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

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

    // function Sys.add12 0
    (Sys.add12)
    // push constant 4,002
    @4002
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop pointer 0
    @SP
    A=M-1
    D=M
    @THIS
    M=D
    @SP
    M=M-1

    // push constant 5,002
    @5002
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop pointer 1
    @SP
    A=M-1
    D=M
    @THAT
    M=D
    @SP
    M=M-1

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

    // push constant 12
    @12
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

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

