package com.yyHaker.syntax.model;

/**
 * IDN
 *存取标识符的类
 * @author Le Yuan
 * @date 2016/11/3
 */
public class IDN {
     private String name;
     private String type;

    public IDN(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
