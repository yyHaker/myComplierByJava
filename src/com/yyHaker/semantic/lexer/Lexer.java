package com.yyHaker.semantic.lexer;


import java.io.*;

import com.yyHaker.lexical.model.TokenString;
import com.yyHaker.lexical.scanner.AnalyzerByFA;
import com.yyHaker.semantic.symbols.*;
import  com.yyHaker.semantic.Utils.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Lexer {

   private int index=0;            //输入字符串的下标
   public static int line = 1;    //记录行号
   public static String inputString;
   char peek = ' ';

   private List<TokenString>  tokenStringList=AnalyzerByFA.tokenStringList;

  public static   Hashtable words = new Hashtable();

   /**
    * 将Word放入Hashtable words中
    * @param w
    */
   void reserve(Word w) {
      words.put(w.lexeme, w);
   }

   public Lexer() {

      reserve( new Word("if",    Tag.IF)    );
      reserve( new Word("else",  Tag.ELSE)  );
      reserve( new Word("while", Tag.WHILE) );
      reserve( new Word("do",    Tag.DO)    );
      reserve( new Word("break", Tag.BREAK) );

      reserve( Word.True );
      reserve( Word.False );

      reserve( Type.Int  );
      reserve( Type.Char  );
      reserve( Type.Bool );
      reserve( Type.Float );
       reserve(Type.Double);
       reserve(Type.Proc);

     //inputString= FileUtils.readFromFile(new File("test2.c"));
      inputString=AnalyzerByFA.codeText;

       //过滤inputString
      inputString= inputString.split("call")[0];
       StringBuffer sb=new StringBuffer(inputString);
       sb.append("\r\n"+"}");
        inputString=sb.toString();
   }

   /**
    * 从输入流读取一个字符
    * @throws IOException
    */
   void readch() throws IOException {
      if (index<inputString.length()){
         peek = inputString.charAt(index);
         index++;
      }
   }

   /**
    * 重载函数  判断c是否等于读入的字符
    * @param c
    * @return
    * @throws IOException
    */
   boolean readch(char c) throws IOException {
      readch();
      if( peek != c )
         return false;
      peek = ' ';
      return true;
   }


   /**
    * 对输入的字符依次扫描，返回Token，被重复调用
    * @return
    * @throws IOException
    */
   public Token scan() throws IOException {
      //忽略换行、空格
      for( ;index<inputString.length() ; readch() ) {
         if( peek == ' ' || peek == '\t'||peek=='\r')
            continue;
         else if( peek == '\n') line = line + 1;
         else break;
      }

      switch( peek ) {
      case '&':
         if( readch('&') ) return Word.and;else return new Token('&');
      case '|':
         if( readch('|') ) return Word.or;   else return new Token('|');
      case '=':
         if( readch('=') ) return Word.eq;   else return new Token('=');
      case '!':
         if( readch('=') ) return Word.ne;   else return new Token('!');
      case '<':
         if( readch('=') ) return Word.le;   else return new Token('<');
      case '>':
         if( readch('=') ) return Word.ge;   else return new Token('>');
      }

      if( Character.isDigit(peek) ) {
         int v = 0;

         do {
            v = 10*v + Character.digit(peek, 10); readch();
         } while( Character.isDigit(peek) );

         if( peek != '.' ) return new Num(v);
         float x = v;
         float d = 10;
         for(;;) {
            readch();
            if( ! Character.isDigit(peek) ) break;
            x = x + Character.digit(peek, 10) / d; d = d*10;
         }
         return new Real(x);
      }


      if( Character.isLetter(peek) ) {
         StringBuffer b = new StringBuffer();
         do {
            b.append(peek); readch();
         } while( Character.isLetterOrDigit(peek) );
         String s = b.toString();

         Word w = (Word)words.get(s);
         if( w != null ) return w;

         w = new Word(s, Tag.ID);
         words.put(s, w);
         return w;
      }

      Token tok = new Token(peek);
      peek = ' ';
      return tok;
   }

   /**
    * 从词法分析中的token序列中获得相应的token，以便语义分析生成三地址指令的处理来进行处理
    * @return
    */
    public Token getNextToken(){
       TokenString tokenString=null;
       for (;index<tokenStringList.size();index++){
         tokenString =tokenStringList.get(index);
           if (tokenString!=null){
              break;
           }
       }

       if (tokenString!=null){
             index= tokenString.getRow();
             //特殊符号
             switch (tokenString.getName()) {
                case "&":   return new Token('&');
                case "&&": return Word.and;
                case "|":  return new Token('|');
                case "||": return Word.or;
                case "=": return new Token('=');
                case "==": return Word.eq;
                case "!": return new Token('!');
                case "!=": return Word.ne;
                case "<": return new Token('<');
                case "<=": return Word.le;
                case ">": return new Token('>');
                case ">=": return Word.ge;
             }

             //整数
             if (tokenString.getName().equals("INT10")){
                   return  new Num(Integer.parseInt(tokenString.getValue()));
             }
            //浮点数
           if (tokenString.getName().equals("FLOAT")){
               return  new Real(Float.parseFloat(tokenString.getValue()));
           }

           //标识符或者关键字

               Word word=(Word) words.get(tokenString.getName());
                 if (word!=null){
                    return  word;   //关键字
                 }else {//标识符
                     if (tokenString.getName().equals("IDN")){
                        word=new Word(tokenString.getName(),Tag.ID);
                        words.put(tokenString.getName(),word);
                        return word;
                     }
                 }

          Token tok = new Token(tokenString.getName().trim().charAt(0));
          return tok;

        }

       return  new Token(' ');
    }
}
