    // push constant 0
    @0
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop local 0
    @0
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

(LOOP_START)
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

    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

    // pop local 0
    @0
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

    // pop argument 0
    @0
    D=A
    @ARG
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

    // if-goto LOOP_START
    @SP
    AM=M-1
    D=M
    @LOOP_START
    D;JGT

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

