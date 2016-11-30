package com.yyHaker.syntax.model;

import java.util.ArrayList;
import java.util.List;

/**
 * NonTerminalSymbol
 *产生式非终结符
 * @author Le Yuan
 * @date 2016/10/20
 */
public class NonTerminalSymbol {
    private String name;                     //符号名称
    private List<String> firstList;       //first集合
    private List<String> followList;     //follow集合
    private List<String> synList;         //同步集合
    private List<NonTerminalSymbol> higherNTS;    //高层结构的NTS

  public  NonTerminalSymbol(String name){
      this.name=name;
       firstList=new ArrayList<String>();
      followList=new ArrayList<String>();
      synList=new ArrayList<String>();
      higherNTS=new ArrayList<NonTerminalSymbol>();
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

    public List<String> getFollowList() {
        return followList;
    }

    public void setFollowList(List<String> followList) {
        this.followList = followList;
    }

    public List<String> getSynList() {
        return synList;
    }

    public void setSynList(List<String> synList) {
        this.synList = synList;
    }

    public List<NonTerminalSymbol> getHigherNTS() {
        return higherNTS;
    }

    public void setHigherNTS(List<NonTerminalSymbol> higherNTS) {
        this.higherNTS = higherNTS;
    }
}
