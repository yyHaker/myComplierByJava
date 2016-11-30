package com.yyHaker.lexical.property;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * a description of the token property
 *
 * @author yyHaker
 * @create 2016-10-05-18:58
 */
public class TokenProperty {
    private final SimpleStringProperty name=new SimpleStringProperty();
    private final SimpleIntegerProperty code=new SimpleIntegerProperty();
    private final SimpleStringProperty type=new SimpleStringProperty();
    private final SimpleIntegerProperty row=new SimpleIntegerProperty();


    public TokenProperty(String name, int code, String type, int row){
        setName(name);
        setCode(code);
        setType(type);
        setRow(row);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCode() {
        return code.get();
    }

    public SimpleIntegerProperty codeProperty() {
        return code;
    }

    public void setCode(int code) {
        this.code.set(code);
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

    public int getRow() {
        return row.get();
    }

    public SimpleIntegerProperty rowProperty() {
        return row;
    }

    public void setRow(int row) {
        this.row.set(row);
    }
}
