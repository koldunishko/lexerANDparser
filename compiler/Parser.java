package org.coderistan.compiler;

import java.util.Queue;

// Bu sınıf, tokenleri içeren lexer sonuçlarını Queue nesnesi olarak alır
public class Parser {

    private Queue<Token> lex;
    private Token token;
    Node t = null;

    public Parser(Queue<Token> lex) {
        this.lex = lex;
    }

    private void getToken() {
        if (lex.isEmpty()) {
            throw new RuntimeException("Token yok");
        } else {
            this.token = lex.remove();
        }
    }

    public void expect(String beklenen) {
        if (beklenen.equalsIgnoreCase(token.value)) {
            getToken();
            return;
        } else {
            throw new RuntimeException(String.format("Syntax hatası: satır %d sutun %d", token.satir, token.sutun));
        }
    }

    Node deyim(int oncelik) {
        Node result = null;
        if (token.type == TOKEN.integer) {
            result = new Node(ND_TYPE.integer, null, null, token.value);
            getToken();
        } else if (token.type == TOKEN.degisken) {
            result = new Node(ND_TYPE.degisken, null, null, token.value);
            getToken();
        } else if (token.type == TOKEN.sm_solparantez) {
            expect("(");
            result = deyim(0);
            expect(")");

        } else {
            throw new RuntimeException(String.format("Syntax hatası: satır %d sutun %d", token.satir, token.sutun));
        }

        while (token.isOp && token.oncelik > oncelik) {
            Token yedek = token;
            getToken();
            Node temp = deyim(yedek.oncelik);
            result = new Node(getOp(yedek), result, temp, null);
        }

        return result;
    }

    ND_TYPE getOp(Token t) {
        if (t.type == TOKEN.op_toplama) {
            return ND_TYPE.toplama;
        } else if (t.type == TOKEN.op_cikarma) {
            return ND_TYPE.cikarma;
        } else if (t.type == TOKEN.op_carpma) {
            return ND_TYPE.carpma;
        } else if (t.type == TOKEN.op_bolme) {
            return ND_TYPE.bolme;
        } else {
            System.out.println("HATA: " + t.type);
            return null;
        }
    }

    Node ifade() {
        Node result = null;
        Node yaprak = null;
        Node sag_taraf = null;

        if (token.type == TOKEN.degisken) {
            // atama ifadesi var
            yaprak = new Node(ND_TYPE.degisken, null, null, token.value);
            getToken();
            expect("=");
            sag_taraf = deyim(0);
            expect(";");
            result = new Node(ND_TYPE.atama, yaprak, sag_taraf, null);
        } else if (token.type == TOKEN.kw_print) {
            // bu bir keyword, parantez bekleniyor
            getToken();
            expect("(");
            sag_taraf = deyim(0);
            result = new Node(ND_TYPE.print, sag_taraf, null, null);
            expect(")");
            expect(";");
        } else {
            throw new RuntimeException("Beklenmeyen token: "+token.type);
        }

        return result;
    }

    public void start() {

        getToken();

        while (token.type != TOKEN.son) {
            t = new Node(ND_TYPE.ifade, t, ifade(), null);
        }

    }

    public void printTree(Node tree) {
        if (tree == null) {
            return;
        }

        System.out.print(tree.nd_type + " ");
        if (tree.value != null) {
            System.out.println(tree.value);
        } else {
            System.out.println("");
        }
        printTree(tree.left);
        printTree(tree.right);
    }

}
