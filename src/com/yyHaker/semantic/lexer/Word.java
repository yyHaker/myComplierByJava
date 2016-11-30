package com.yyHaker.semantic.lexer;


/**
 * 关键字与常见符号的类
 *
 *
 *
 * toString方法返回该符号的字符串形式
 *
 * 定义了以下基本静态常量(Word类型)
 *
 * &&  and
 * ||   or
 * =    eq
 * !=    ne
 * <=    le
 * >=    ge
 *
 * minus  minus
 * true    true
 * false    false
 * t          temp   //临时变量
 */
public class Word extends Token {

   public String lexeme = "";
   public Word(String s, int tag) {
      super(tag);
      lexeme = s;
   }
   public String toString() {
      return lexeme;
   }

   public static final Word

      and = new Word( "&&", Tag.AND ),  or = new Word( "||", Tag.OR ),
      eq  = new Word( "==", Tag.EQ  ),  ne = new Word( "!=", Tag.NE ),
      le  = new Word( "<=", Tag.LE  ),  ge = new Word( ">=", Tag.GE ),

      minus  = new Word( "minus", Tag.MINUS ),
      True   = new Word( "true",  Tag.TRUE  ),
      False  = new Word( "false", Tag.FALSE ),
      temp   = new Word( "t",     Tag.TEMP  );
}
