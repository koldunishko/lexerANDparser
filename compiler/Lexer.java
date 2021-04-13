package org.coderistan.compiler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Lexer {

    private final Queue<Token> lexer = new LinkedList<>();
    private String metin;
    private boolean finish = false;
    private int bas = 0, pos = 0, satir_no = 1, sutun_no = 0;
    private final List<String> keywords = Arrays.asList("print");
    private final String alfabe = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String rakamlar = "0123456789";
    private final String operator = "+-*/=";
    private final String symbol = "();";
    private String karakter = "";

    public Lexer(String metin) {
        this.metin = metin;
    }

    private void lexOperator() {
        TOKEN op_type = null;
        int oncelik = -1;

        try {
            karakter = this.getKarakter();
        } catch (Exception e) {
            this.token(TOKEN.son, false, -1);
            return;
        }

        this.nextPos();

        if (operator.contains(karakter)) {
            switch (karakter) {
                case "+":
                    op_type = TOKEN.op_toplama;
                    oncelik = 2;
                    break;
                case "-":
                    op_type = TOKEN.op_cikarma;
                    oncelik = 2;
                    break;
                case "*":
                    op_type = TOKEN.op_carpma;
                    oncelik = 5;
                    break;
                case "/":
                    op_type = TOKEN.op_bolme;
                    oncelik = 5;
                    break;
                case "=":
                    op_type = TOKEN.esittir;
                    oncelik = -1;
                    break;
                default:
                    break;
            }

            this.token(op_type, true, oncelik);
        }
    }

    private void lexNumber() {
        while (true) {
            try {
                karakter = this.getKarakter();
            } catch (Exception e) {
                this.token(TOKEN.integer, false, -1);
                this.token(TOKEN.son, false, -1);
                return;
            }

            if (rakamlar.contains(karakter)) {
                this.nextPos();
            } else {
                this.token(TOKEN.integer, false, -1);
                return;
            }
        }
    }

    private void nextPos(){
        this.sutun_no++;
        this.pos++;
    }
    
    private void lexSymbol() {
        try {
            karakter = this.getKarakter();
        } catch (Exception e) {
            this.token(TOKEN.son, false, -1);
            return;
        }

        nextPos();
        switch (karakter) {
            case "(":
                this.token(TOKEN.sm_solparantez, false, -1);
                break;
            case ")":
                this.token(TOKEN.sm_sagparantez, false, -1);
                break;
            case ";":
                this.token(TOKEN.sm_noktalivirgul, false, -1);
                break;
            default:
                throw new RuntimeException("Lexical analiz hatası");
        }
    }

    private void lexVariable() {
        while (true) {
            try {
                karakter = this.getKarakter();
            } catch (Exception e) {
                if (keywords.contains(getKelime())) {
                    switch (getKelime()) {
                        case "print":
                            this.token(TOKEN.kw_print, false, -1);
                            break;
                    }
                } else {
                    this.token(TOKEN.degisken, false, -1);
                }
                this.token(TOKEN.son, false, -1);
                return;
            }

            if (alfabe.contains(karakter)) {
                this.nextPos();
            } else {

                if (keywords.contains(getKelime())) {
                    switch (getKelime()) {
                        case "print":
                            this.token(TOKEN.kw_print, false, -1);
                            break;
                    }
                } else {
                    this.token(TOKEN.degisken, false, -1);
                }
                return;
            }

        }
    }

    private String getKarakter() {
        try {
            String s = "" + this.metin.charAt(pos);
            if (s.equalsIgnoreCase("\n")) {
                satir_no++;
            }

            return s;
        } catch (Exception e) {
            finish = true;
            throw new RuntimeException("Metin sonu");
        }
    }

    private String getKelime() {
        return this.metin.substring(bas, pos);
    }

    private void token(TOKEN token_type, boolean isOp, int oncelik) {
        if (bas == pos && token_type != TOKEN.son) {
            return;
        }
        this.lexer.add(new Token(satir_no, sutun_no, token_type, this.metin.substring(bas, pos), isOp, oncelik));
        this.bas = this.pos;
    }

    public void start() {
        while (!finish) {
            try {
                karakter = this.getKarakter();
            } catch (Exception e) {
                finish = true;
            }

            if (alfabe.contains(karakter)) {
                // değişken için token oluşturma
                this.lexVariable();
            } else if (rakamlar.contains(karakter)) {
                // sayılar için token oluşturma
                this.lexNumber();
            } else if (operator.contains(karakter)) {
                // operatorler için token oluşturma
                this.lexOperator();
            } else if (symbol.contains(karakter)) {
                // noktalı virgül için token oluşturma
                this.lexSymbol();
            } else if (" \t\n".contains(karakter)) {
                this.nextPos();
                this.bas = this.pos;
                this.sutun_no = 0;
                continue;
            } else {
                finish = true;
                throw new RuntimeException("Bilinmeyen karakter");
            }
        }
    }

    // işlem bittikten sonra sonuçları bu metot ile alabiliriz
    public Queue<Token> getResult() {
        return lexer;
    }

    public boolean isFinish() {
        return finish;
    }
}
