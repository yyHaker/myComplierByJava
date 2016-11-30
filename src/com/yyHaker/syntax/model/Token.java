package com.yyHaker.syntax.model;

/**
 * Token
 *词法分析得到的token序列
 * @author Le Yuan
 * @date 2016/10/20
 */
public class Token {
    private String name;//token名字
    private String value;//token的值

    public Token() {
    }

    public Token(String name, String value) {
        this.name = name;
        this.value = value;
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
}
