package com.yyHaker.lexical.model;

/**
 * the description of lexical error
 *
 * @author yyHaker
 * @create 2016-10-03-14:14
 */
public class Error {
    private int row;
    private String strError;
    private String errorKind;

    public Error() {
    }

    public Error(int row, String strError, String errorKind) {
        this.row = row;
        this.strError = strError;
        this.errorKind = errorKind;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getStrError() {
        return strError;
    }

    public void setStrError(String strError) {
        this.strError = strError;
    }

    public String getErrorKind() {
        return errorKind;
    }

    public void setErrorKind(String errorKind) {
        this.errorKind = errorKind;
    }

    @Override
    public String toString() {
        return "Error{" +
                "row=" + row +
                ", strError='" + strError + '\'' +
                ", errorKind='" + errorKind + '\'' +
                '}';
    }
}
