package com.yyHaker.lexical.model;

/**
 * TokenString
 *
 * @author Le Yuan
 * @date 2016/10/28
 */
public class TokenString {
    private String name;
    private String value;
    private int row;


    public TokenString() {
    }

    public TokenString(String name, String value, int row) {
        this.name = name;
        this.value = value;
        this.row = row;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
