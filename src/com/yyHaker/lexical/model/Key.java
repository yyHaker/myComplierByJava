package com.yyHaker.lexical.model;

/**
 * a keyword  base class
 *所识别key的信息
 * @author yyHaker
 * @create 2016-09-25-14:01
 */
public class Key {
     private String name; //名称
     private int code;       //种别码
     private String type ; //类型

    public Key() {
    }

    public Key(String name, int code, String type) {
        this.name = name;
        this.code = code;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setType(String type){
        this.type = type;
    }

}
