package com.yyHaker.syntax.model;

import java.util.ArrayList;
import java.util.List;

/**
 * TerminalSymbol
 *文法中的终结符号
 * @author Le Yuan
 * @date 2016/10/20
 */
public class TerminalSymbol {
    private String name; //终结符名字
    private List<String> firstList;//first集合

    public TerminalSymbol(String name){
        this.name=name;
        firstList=new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFirstList() {
        return firstList;
    }

    public void setFirstList(List<String> firstList) {
        this.firstList = firstList;
    }
}
