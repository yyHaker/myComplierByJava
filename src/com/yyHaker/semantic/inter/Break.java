package com.yyHaker.semantic.inter;

public class Break extends Stmt {

   Stmt stmt;

   public Break() {
      if( Stmt.Enclosing == Stmt.Null )
         error("未封闭的break语句");
       stmt = Stmt.Enclosing;
   }

   public void gen(int b, int a) {
      emit( "goto L" + stmt.after);
   }
}
