package com.yyHaker.semantic.lexer;

/**
 * 数字基本类
 */
public class Num extends Token {
                                      //tag 从Token继承而来的类型
	public final int value;  //值

	public Num(int v) {
		super(Tag.NUM);
		value = v;
	}

	//返回值的字符串形式
	public String toString() {
		return "" + value;
	}
}
