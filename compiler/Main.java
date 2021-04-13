package org.coderistan.compiler;

import java.util.Queue;

public class Main {

    public static void main(String[] args) {
        String metin = "islem=((5*2)/2+55*3);print(islem);";

        Lexer l = new Lexer(metin);
        l.start();

        Queue<Token> sonuclar = l.getResult();
        Parser p = new Parser(sonuclar);
        p.start();

        p.printTree(p.t);
    }
}
