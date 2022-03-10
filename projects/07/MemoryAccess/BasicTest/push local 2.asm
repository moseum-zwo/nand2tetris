// push local 2
@2      // addr = segPointer + i
D=A
@LCL
D=D+M   // =>addr
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
