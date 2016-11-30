package com.yyHaker.semantic.inter;

import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;

/**
 * 算术运算符，双目运算符例如+,*
 */
public class Arith extends Op {

   public Expr expr1, expr2;

   public Arith(Token tok, Expr x1, Expr x2)  {
      super(tok, null);  //tok表示该运算符的词法单元，null表示类型的占位符
      expr1 = x1;
      expr2 = x2;
      type = Type.max(expr1.type, expr2.type);//自动类型转换
      if (type == null )
         error("变量类型错误");

   }

   /**
    * 把表达式的子表达式规约为地址，并将表达式的运算符作用于这些地址
    * @return
    */
   public Expr gen() {
      return new Arith(op, expr1.reduce(), expr2.reduce());
   }

   public String toString() {
      return expr1.toString()+" "+op.toString()+" "+expr2.toString();
   }

}
