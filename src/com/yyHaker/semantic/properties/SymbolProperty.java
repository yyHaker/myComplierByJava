package com.yyHaker.semantic.properties;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * SymbolProperty
 * 符号表的信息类
 * @author Le Yuan
 * @date 2016/11/9
 */
public class SymbolProperty {
     private  final SimpleStringProperty symbol=new SimpleStringProperty();   //符号
     private final  SimpleStringProperty type=new SimpleStringProperty();      //类型
     private  final SimpleIntegerProperty offset=new SimpleIntegerProperty();  //偏移量

    public SymbolProperty(String symbol,String type,int offset) {
         setSymbol(symbol);
        setType(type);
        setOffset(offset);
    }

    public String getSymbol() {
        return symbol.get();
    }

    public SimpleStringProperty symbolProperty() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol.set(symbol);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getOffset() {
        return offset.get();
    }

    public SimpleIntegerProperty offsetProperty() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset.set(offset);
    }
}
