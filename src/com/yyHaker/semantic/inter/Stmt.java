package com.yyHaker.semantic.inter;

public class Stmt extends Node {

   public Stmt() { }

   public static Stmt Null = new Stmt();

   //下面三条语句处理三地址指令的生成
   public void gen(int b, int a) {} // 调用时参数是语句开始出的标号和语句的下一条指令的标号

   int after = 0;                   // 保存语句的下一条指令的标号

   public static Stmt Enclosing = Stmt.Null;  // used for break statements  空语句序列
}
