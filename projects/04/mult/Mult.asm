// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//
// This program only needs to handle arguments that satisfy
// R0 >= 0, R1 >= 0, and R0*R1 < 32768.

// Put your code here

    @R0
    D=M
    @n
    M=D    // n = R0

    @R1
    D=M
    @factor
    M=D    // factor = R1

    @R2
    M=0    // initialize product to 0

    @i
    M=1    // i = 1

    @product
    M=0    // product = 0

(LOOP)
    @i
    D=M
    @n
    D=D-M
    @STOP
    D;JGT    // if i > n goto STOP

    @factor
    D=M
    @product
    M=M+D    // product = product + factor

    @i
    M=M+1    // i++

    @LOOP
    0;JMP

(STOP)
    @product
    D=M
    @R2
    M=D    // RAM[2] = product

(END)
    @END
    0;JMP