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

// 0: white; -1: black
@COLOR
M=0

// the screen size
@8192
D=A
@n
M=D

(K_LISTEN)
    @KBD
    D=M

    // choose color
    @BLACK
    D;JNE
    @COLOR
    M=0
    @INIT_DRAW
    0;JMP
    (BLACK)
        @COLOR
        M=-1


    (INIT_DRAW)
    @i
    M=0
    @SCREEN
    D=A
    @ADDR
    M=D

    (LOOP)
        @i
        D=M
        @n
        D=M-D
        @END_LOOP
        D;JEQ

        // continue to draw
        @COLOR
        D=M
        @ADDR
        A=M
        M=D

        @ADDR
        M=M+1
        @i
        M=M+1

        @LOOP
        0;JMP

    (END_LOOP)

    @K_LISTEN
    0;JMP


    


