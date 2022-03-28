    // function SimpleFunction.test 2
(SimpleFunction.test)
    //Initialize localVar 1 of 2
    @SP
    A=M
    M=0
    @SP
    M=M+1
    //Initialize localVar 2 of 2
    @SP
    A=M
    M=0
    @SP
    M=M+1
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

    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

    // not
    @SP
    A=M-1
    M=!M

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

    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

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

