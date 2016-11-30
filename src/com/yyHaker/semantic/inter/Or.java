package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;
public class Or extends Logical {

   /**
    * 构造一个语法树的节点，其运算符为tok，而运算分量为x1和x2
    * @param tok
    * @param x1
    * @param x2
    */
   public Or(Token tok, Expr x1, Expr x2) {
      super(tok, x1, x2);
   }

   /**
    *
    * @param t true出口
    * @param f false出口
    */
   public void jumping(int t, int f) {
      int label = t != 0 ? t : newlabel();
      expr1.jumping(label, 0);
      expr2.jumping(t,f);

      if( t == 0 )              //如果t为0,那么label被设置为一个新的标号,并在B1和B2的代码被生成之后再生成这个新标号
         emitlabel(label);
   }

}
