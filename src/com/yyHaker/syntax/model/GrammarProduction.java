package com.yyHaker.syntax.model;

import java.util.ArrayList;
import java.util.List;

/**
 * GrammarProduction
 *文法产生式的model类
 * @author Le Yuan
 * @date 2016/10/20
 */
public class GrammarProduction {
     private String  production;  //产生式
     private String  leftPart;       //产生式头
     private List<String> rightPart;  //产生式体集合
     private  List<String> selectList; //产生式的select集合

    public GrammarProduction(String production) {
        this.production=production;
        rightPart=new ArrayList<String>();
        selectList=new ArrayList<String>();
    }

    public String getProduction() {
        return production;
    }

    public void setProduction(String production) {
        this.production = production;
    }

    public String getLeftPart() {
        return leftPart;
    }

    public void setLeftPart(String leftPart) {
        this.leftPart = leftPart;
    }

    public List<String> getRightPart() {
        return rightPart;
    }

    public void setRightPart(List<String> rightPart) {
        this.rightPart = rightPart;
    }

    public List<String> getSelectList() {
        return selectList;
    }

    public void setSelectList(List<String> selectList) {
        this.selectList = selectList;
    }
}
