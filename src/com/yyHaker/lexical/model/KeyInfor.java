package com.yyHaker.lexical.model;

/**
 * a class to descript the key and row
 *
 * @author yyHaker
 * @create 2016-09-25-20:32
 */
public class KeyInfor {
    private Key key;
    private int row;

    public KeyInfor() {
    }

    public KeyInfor(Key key, int row) {
        this.key = key;
        this.row = row;
    }

    public Key getKey() {
        return key;
    }

    public int getRow() {
        return row;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
        public String toString() {
       return key.getName()+" ,"+key.getCode()+" ,"+key.getType()+" ,"+row;
    }
}
