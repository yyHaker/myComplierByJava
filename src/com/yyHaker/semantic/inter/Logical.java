package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;

/**
 * 逻辑运算  or、and、not的父类
 */
public class Logical extends Expr {

   public Expr expr1, expr2;

   /**
    * 构造一个语法树的节点，其运算符为tok，而运算分量为x1和x2
    * @param tok
    * @param x1
    * @param x2
    */
   Logical(Token tok, Expr x1, Expr x2) {
      super(tok, null);                      // null type to start
      expr1 = x1;
      expr2 = x2;
      type = check(expr1.type, expr2.type);
      if (type == null )
         error("逻辑表达式中的类型应该为bool类型");
   }

   /**
    * 保证p1和p2都是bool类型
    * @param p1
    * @param p2
    * @return
    */
   public Type check(Type p1, Type p2) {
      if ( p1 == Type.Bool && p2 == Type.Bool ) return Type.Bool;
      else return null;
   }

   public Expr gen() {
      int f = newlabel();
      int a = newlabel();
      Temp temp = new Temp(type);

      this.jumping(0,f);   //true出口是下一条指令，false出口是一个新的标号
      emit(temp.toString() + " = true");
      emit("goto L" + a);

      emitlabel(f); //生成标号f
      emit(temp.toString() + " = false");  //把false赋值给temp
      emitlabel(a); //代码的结尾片段是a
      return temp;
   }

   public String toString() {
      return expr1.toString()+" "+op.toString()+" "+expr2.toString();
   }

}
