package com.yyHaker.semantic.inter;


import com.yyHaker.semantic.lexer.*;
import com.yyHaker.semantic.symbols.*;

/**
 * 临时变量
 */
public class Temp extends Expr {

   static int count = 0;
   int number = 0;

   public Temp(Type p) {     //p表示type
      super(Word.temp, p);
      number = ++count;
   }

   public String toString() {
      return "t" + number;
   }
}
