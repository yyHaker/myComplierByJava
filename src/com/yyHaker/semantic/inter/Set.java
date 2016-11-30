package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.symbols.*;

/**
 * 左部为标识符，右部为一个表达式的赋值语句
 */
public class Set extends Stmt {

   public Id id;
   public Expr expr;

   public Set(Id i, Expr x) {
      id = i;
      expr = x;
      if ( check(id.type, expr.type) == null )
         error("赋值语句类型不匹配");
   }

   /**
    * 类型检查
    * @param p1
    * @param p2
    * @return
    */
   public Type check(Type p1, Type p2) {
      if ( Type.numeric(p1) && Type.numeric(p2) ) return p2;
      else if ( p1 == Type.Bool && p2 == Type.Bool ) return p2;
      else return null;
   }

   //生成三地址指令
   public void gen(int b, int a) {
      emit( id.toString() + " = " + expr.gen().toString() );
   }

}
