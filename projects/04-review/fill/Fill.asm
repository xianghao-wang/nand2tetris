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

@COLOR
M=0

@8192
D=A
@N
M=D

(key.start)
    @KBD
    D=M
    @color.black
    D;JNE
    @color.white
    D;JMP

    // choose color
    (color.black)
    @COLOR
    M=-1
    @color.end
    0;JMP
    (color.white)
    @COLOR
    M=0
    (color.end)

    // fill
    @i
    M=0

    (draw.loop)
    @N
    D=M
    @i
    D=D-M
    @draw.end
    D;JEQ

    @SCREEN
    D=A
    @i
    D=D+M
    @ADDR
    M=D
    @COLOR
    D=M
    @ADDR
    A=M
    M=D

    @i
    M=M+1
    @draw.loop
    0;JMP

    (draw.end)

    @key.start
    0;JMP
