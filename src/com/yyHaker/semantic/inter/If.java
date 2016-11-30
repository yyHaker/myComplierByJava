package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;

public class If extends Stmt {

   Expr expr;
   Stmt stmt;

   public If(Expr x, Stmt s) {
      expr = x;
      stmt = s;
      if( expr.type != Type.Bool )
         expr.error("在if语句中应该使用bool类型");
   }

   public void gen(int b, int a) {
      int label = newlabel(); // label for the code for stmt
      expr.jumping(0, a);     // fall through on true, goto a on false

      emitlabel(label);
      stmt.gen(label, a);
   }

}
