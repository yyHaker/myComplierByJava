package com.yyHaker.syntax.property;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * SyntaxErrorProperty
 *
 * @author Le Yuan
 * @date 2016/10/29
 */
public class SyntaxErrorProperty {
    private final SimpleStringProperty tokenError=new SimpleStringProperty();
    private final SimpleIntegerProperty row=new SimpleIntegerProperty();
    private final SimpleStringProperty errorKind=new SimpleStringProperty();

    public SyntaxErrorProperty(int row,String tokenError,String errorKind) {
        setTokenError(tokenError);
        setRow(row);
        setErrorKind(errorKind);
    }

    public String getTokenError() {
        return tokenError.get();
    }

    public SimpleStringProperty tokenErrorProperty() {
        return tokenError;
    }

    public void setTokenError(String tokenError) {
        this.tokenError.set(tokenError);
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

    public String getErrorKind() {
        return errorKind.get();
    }

    public SimpleStringProperty errorKindProperty() {
        return errorKind;
    }

    public void setErrorKind(String errorKind) {
        this.errorKind.set(errorKind);
    }
}
