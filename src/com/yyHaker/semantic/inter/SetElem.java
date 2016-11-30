package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.symbols.*;

/**
 * 对数组元素的赋值
 */
public class SetElem extends Stmt {

   public Id array;
   public Expr index;
   public Expr expr;

   public SetElem(Access x, Expr y) {
      array = x.array;
      index = x.index;
      expr = y;
      if ( check(x.type, expr.type) == null )
         error("对数组赋值时类型错误");
   }

   /**
    * 类型检查
    * @param p1
    * @param p2
    * @return
    */
   public Type check(Type p1, Type p2) {
      if ( p1 instanceof Array || p2 instanceof Array ) return null;
      else if ( p1 == p2 ) return p2;
      else if ( Type.numeric(p1) && Type.numeric(p2) ) return p2;
      else return null;
   }


   public void gen(int b, int a) {
      String s1 = index.reduce().toString();
      String s2 = expr.reduce().toString();
      emit(array.toString() + " [ " + s1 + " ] = " + s2);
   }

}
