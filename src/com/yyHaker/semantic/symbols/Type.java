package com.yyHaker.semantic.symbols;
import  com.yyHaker.semantic.lexer.*;

public class Type extends Word {

   public int width = 0;          // width is used for storage allocation

   public Type(String s, int tag, int w) {
      super(s, tag);
      width = w;
   }

   public static final Type
      Int   = new Type( "int",   Tag.BASIC, 4 ),
      Float = new Type( "float", Tag.BASIC, 4 ),
      Char  = new Type( "char",  Tag.BASIC, 1 ),
      Bool  = new Type( "bool",  Tag.BASIC, 1 ),
      Double=new Type("double",Tag.BASIC,1),
           Proc=new Type("proc",Tag.BASIC,10);
   /**
    * 判断类型是否为数值类型的
    * char   int   float
    * @param p
    * @return
    */
   public static boolean numeric(Type p) {
      if (p == Type.Char || p == Type.Int || p == Type.Float) return true;
      else return false;
   }

   /**
    * 实现基本的自动类型转换
    * int->float类型
    * @param p1
    * @param p2
    * @return
    */
   public static Type max(Type p1, Type p2 ) {
      if ( ! numeric(p1) || ! numeric(p2) ) return null;
      else if ( p1 == Type.Float || p2 == Type.Float ) return Type.Float;
      else if ( p1 == Type.Int   || p2 == Type.Int   ) return Type.Int;
      else return Type.Char;
   }

   }
