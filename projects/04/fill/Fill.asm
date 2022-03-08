// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

	@black
	M=0

	@SCREEN
	D=A
	@addr
	M=D

	@24575
	D=A
	@eos
	M=D

(PAINT)
	@addr
	D=M
	@eos
	D=D-M
	@START
	D;JGT    // if addr > n goto STOP
    
    @black
    D=M      // equal to 0 if white, 1 otherwise
    @addr
    A=M
    M=D    // RAM[addr] = black
    
    @addr
    M=M+1    // addr = addr + 1
    
    @PAINT
    0;JMP

(START)
	@KBD
	D=M

	@BLACK
	D;JNE    // Jump, if key is pressed

	@WHITE
	D;JEQ    // Jump, if no key is pressed

(BLACK)
	@black
	M=-1

	@SCREEN
	D=A
	@addr
	M=D

	@PAINT
	0;JMP

(WHITE)	
	@black
	M=0

	@SCREEN
	D=A
	@addr
	M=D

	@PAINT
	0;JMP