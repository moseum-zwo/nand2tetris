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

    // pop pointer 1
    @SP
    A=M-1
    D=M
    @THAT
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

    // pop that 0
    @0
    D=A
    @THAT
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

    // push constant 1
    @1
    D=A
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // pop that 1
    @1
    D=A
    @THAT
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

(MAIN_LOOP_START)
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

    // if-goto COMPUTE_ELEMENT
    @SP
    AM=M-1
    D=M
    @COMPUTE_ELEMENT
    D;JGT

    // goto END_PROGRAM
    @END_PROGRAM
    0;JMP

(COMPUTE_ELEMENT)
    // push that 0
    @0
    D=A
    @THAT
    D=D+M
    A=D
    D=M
    @SP
    A=M
    M=D
    @SP
    M=M+1

    // push that 1
    @1
    D=A
    @THAT
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

    // pop that 2
    @2
    D=A
    @THAT
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

    // push pointer 1
    @THAT
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

    // add
    @SP
    A=M-1
    D=M
    @SP
    M=M-1
    A=M-1
    M=M+D

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

    // goto MAIN_LOOP_START
    @MAIN_LOOP_START
    0;JMP

(END_PROGRAM)
