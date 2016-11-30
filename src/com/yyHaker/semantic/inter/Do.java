package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;

public class Do extends Stmt {

   Expr expr;
   Stmt stmt;

   public Do() {
      expr = null;
      stmt = null;
   }

   public void init(Stmt s, Expr x) {
      expr = x;
      stmt = s;
      if( expr.type != Type.Bool )
         expr.error("在do语句中应该使用bool类型");
   }

   public void gen(int b, int a) {
      after = a;                   //a为do外的第一条指令标号
      int label = newlabel();   // label for expr
      stmt.gen(b,label);
      emitlabel(label);

      expr.jumping(b,0);
   }

}
