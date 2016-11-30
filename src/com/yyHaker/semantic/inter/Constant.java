package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;
public class Constant extends Expr {

   /**
    * 在抽象语法树中构造一个标号为tok、类型为p的子节点
    * @param tok
    * @param p
    */
   public Constant(Token tok, Type p) { //tok表示词法单元， p表示类型
      super(tok, p);
   }

   /**
    * 根据一个整数创建一个常量对象
    * @param i
    */
   public Constant(int i) {
      super(new Num(i), Type.Int);
   }

   public static final Constant
      True  = new Constant(Word.True,  Type.Bool),
      False = new Constant(Word.False, Type.Bool);

   /**
    *
    * @param t 标号
    * @param f 标号
    */
   public void jumping(int t, int f) {
      if ( this == True && t != 0 )
         emit("goto L" + t);
      else if ( this == False && f != 0)
         emit("goto L" + f);
   }

}
