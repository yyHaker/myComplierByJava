package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.symbols.*;

public class While extends Stmt {

   Expr expr;
   Stmt stmt;

   public While() {
      expr = null;
      stmt = null;
   }

   public void init(Expr x, Stmt s) {
      expr = x;  stmt = s;
      if( expr.type != Type.Bool )
         expr.error("while语句中应该使用bool类型");
   }

   public void gen(int b, int a) {  //a为while循环外的第一条指令标号
      after = a;                // save label a,当用break跳出语句时使用
      expr.jumping(0, a);

      int label = newlabel();   // label for stmt
      emitlabel(label);
      stmt.gen(label, b);
      emit("goto L" + b);
   }

}
