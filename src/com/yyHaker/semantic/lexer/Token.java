package com.yyHaker.semantic.lexer;

/**
 * 词法单元的基本类
 */
	public class Token {

	public final int tag;  //标识词法单元的类型
	public Token(int t) {
		tag = t;
	}

	/**
	 * 得到词法单元的类型
	 * @return
	 */
	public String toString() {
		return "" + (char)tag;
	}
}
