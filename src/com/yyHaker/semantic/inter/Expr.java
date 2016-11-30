package com.yyHaker.semantic.inter;

import com.yyHaker.semantic.lexer.*;
import com.yyHaker.semantic.symbols.*;

public class Expr extends Node {

   public Token op;  //节点上的运算符
   public Type type;//节点上的类型

   Expr(Token tok, Type p) {
      op = tok;
      type = p;
   }

   /**
    * 生成一个项
    * @return
    */
   public Expr gen() {
      return this;
   }

   public Expr reduce() {
      return this;
   }

   public void jumping(int t, int f) {
      emitjumps(toString(), t, f);
   }


   public void emitjumps(String test, int t, int f) {
      if( t != 0 && f != 0 ) { //如果t和f都不是特殊符号0
         emit("if " + test + " goto L" + t);
         emit("goto L" + f);
      }
      else if( t != 0 ) emit("if " + test + " goto L" + t);
      else if( f != 0 ) emit("ifFalse " + test + " goto L" + f);
      else ; // nothing since both t and f fall through
   }

   public String toString() {
      return op.toString();
   }



}
