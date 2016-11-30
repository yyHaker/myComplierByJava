package com.yyHaker.lexical.property;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * the description of error property
 *
 * @author yyHaker
 * @create 2016-10-05-20:04
 */
public class ErrorProperty {
    private final SimpleIntegerProperty row=new SimpleIntegerProperty();
    private final SimpleStringProperty strError=new SimpleStringProperty();
    private final SimpleStringProperty errorKind=new SimpleStringProperty();

    public ErrorProperty(int row,String strError,String errorKind){
        setRow(row);
        setErrorKind(errorKind);
        setStrError(strError);
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

    public String getStrError() {
        return strError.get();
    }

    public SimpleStringProperty strErrorProperty() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError.set(strError);
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
