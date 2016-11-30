package com.yyHaker.semantic.main;

import com.yyHaker.semantic.lexer.*;
import com.yyHaker.semantic.parser.*;

import java.io.IOException;
import java.util.List;

public class Main {

	public static void main(String[] args) throws IOException {
	/*	Lexer lex = new Lexer();
		Parser parse = new Parser(lex);
		parse.program();
		System.out.write('\n');

		//List<Token> tokenList=parse.getTokenList();
		System.out.write('\n');*/

	  System.out.println("a + b".split("\\+")[0]);
	  System.out.println("a + b".split("\\+")[1]);
		System.out.println();

	}
}
