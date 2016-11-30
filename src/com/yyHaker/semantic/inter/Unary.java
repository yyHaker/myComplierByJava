package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;
/**
 * 单目运算符
 * ++ -- !
 */
public class Unary extends Op {

   public Expr expr;

   public Unary(Token tok, Expr x) {    // handles minus, for ! see Not
      super(tok, null);
      expr = x;
      type = Type.max(Type.Int, expr.type);   //expr不是int float char 返回null,否则返回int
      if (type == null )
         error("单目运算符的类型不匹配，无法运算");
   }

   /**
    * 把表达式的子表达式规约为地址，并将表达式的运算符作用于这些地址
    * @return
    */
   public Expr gen() {
      return new Unary(op, expr.reduce());
   }

   public String toString() {
      return op.toString()+" "+expr.toString();
   }
}
