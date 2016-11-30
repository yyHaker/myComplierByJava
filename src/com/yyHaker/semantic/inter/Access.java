package com.yyHaker.semantic.inter;

import  com.yyHaker.semantic.lexer.*;
import  com.yyHaker.semantic.symbols.*;

/**
 * 数组访问
 */
public class Access extends Op {

   public Id array;
   public Expr index;

   /**
    *
    * @param a 平坦化的数组
    * @param i   下标
    * @param p  数组的元素类型
    */
   public Access(Id a, Expr i, Type p) {    // p is element type after
      super(new Word("[]", Tag.INDEX), p);  // flattening the array
      array = a;
      index = i;
   }

   public Expr gen() {
      return new Access(array, index.reduce(), type);
   }

   public void jumping(int t,int f) {
      emitjumps(reduce().toString(),t,f);
   }

   public String toString() {
      return array.toString() + " [ " + index.toString() + " ]";
   }
}
