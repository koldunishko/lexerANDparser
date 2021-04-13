/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.coderistan.compiler;

enum ND_TYPE {
    degisken,
    integer,
    atama,
    toplama,
    cikarma,
    carpma,
    bolme,
    print,
    ifade
}

enum TOKEN {
    esittir,
    degisken,
    op_toplama,
    op_cikarma,
    op_carpma,
    op_bolme,
    sm_noktalivirgul,
    sm_solparantez,
    sm_sagparantez,
    kw_print,
    integer,
    son
}

class Node {

    ND_TYPE nd_type;
    Node left, right;
    String value;

    public Node(ND_TYPE nd_type, Node left, Node right, String value) {
        this.nd_type = nd_type;
        this.left = left;
        this.right = right;
        this.value = value;
    }

}

class Token {

    public int satir, sutun;
    public TOKEN type;
    public String value;
    public boolean isOp;
    public int oncelik;

    public Token(int satir, int sutun, TOKEN type, String value, boolean isOp, int oncelik) {
        this.satir = satir;
        this.sutun = sutun;
        this.type = type;
        this.value = value;
        this.isOp = isOp;
        this.oncelik = oncelik;
    }
}

public class utils {

}
