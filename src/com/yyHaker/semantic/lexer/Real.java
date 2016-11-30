package com.yyHaker.semantic.lexer;

/**
 * 浮点数的基本类
 */
public class Real extends Token {

	                                   //tag 从Token继承而来的类型
	public final float value;//值

	public Real(float v) {
		super(Tag.REAL);
		value = v;
	}

	//返回值的字符串形式
	public String toString() {
		return "" + value;
	}
}
