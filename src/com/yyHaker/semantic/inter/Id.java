package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;

/**
 * 因为标识符就是一个地址，类ID从Expr中继承了gen和reduce的默认实现
 */
public class Id extends Expr {

	public int offset;     // relative address 标识符的相对地址

	 private Word word;
	public Word getWord() {
		return word;
	}

	public Id(Word id, Type p, int b) {
		super(id, p);     //id为运算符，p为类型
		offset = b;
		word=id;
	}

//	public String toString() {return "" + op.toString() + offset;}
}
