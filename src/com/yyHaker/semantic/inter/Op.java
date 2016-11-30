package com.yyHaker.semantic.inter;


import com.yyHaker.semantic.lexer.*;
import com.yyHaker.semantic.symbols.*;

public class Op extends Expr {

   public Op(Token tok, Type p)  {
      super(tok, p); //tok表示运算符，p表示类型
   }


   /**
    * 生成一个项，生成一个指令把这个项的值赋值给一个新的临时名字，并返回这个临时名字
    * @return
    */
   public Expr reduce() {
      Expr x = gen();
      Temp t = new Temp(type);
      emit( t.toString() + " = " + x.toString() );
      return t;
   }

}
