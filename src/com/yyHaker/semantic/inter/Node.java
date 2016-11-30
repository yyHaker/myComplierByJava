package com.yyHaker.semantic.inter;
import  com.yyHaker.semantic.lexer.*;
import com.yyHaker.semantic.properties.SymbolProperty;
import com.yyHaker.syntax.property.SyntaxErrorProperty;

import java.util.ArrayList;
import java.util.List;


public class Node {
   //存储三元式指令
   public static StringBuffer triplesString=new StringBuffer();
   //存储四元式信息
   public static  StringBuffer fourElementsString=new StringBuffer();
   //存储语义的错误信息
   public static List<SyntaxErrorProperty>  semanticErrorList=new ArrayList<>();
   //存储符号表信息
    public  static  List<SymbolProperty> symbolPropertyList=new ArrayList<>();
    public static int lexline = 0; //所处行号
   Node() {
      lexline = Lexer.line;
   }
   void error(String s) {
       semanticErrorList.add(new SyntaxErrorProperty(lexline," ",s));
     // throw new Error("near line "+lexline+": "+s);
   }

   public static int labels = 0; //三地址指令的标签


   /**
    * 下面三个用于生成三地址指令
    */

   /**
    * 产生一个新的标号
    * @return
    */
   public int newlabel() {
      return ++labels;
   }

   /**
    * 产生三元式标签
    * @param i
    */
   public void emitlabel(int i) {
      System.out.print("L" + i + ":");

      triplesString.append("L" + i + ":");

      fourElementsString.append("L" + i + ":");
   }

   /**
    * 产生三元式三地址指令
    * @param s
    */
   public void emit(String s) {
      System.out.println("\t" + s);

      triplesString.append("\t"+s+"\n");

       fourElementsString.append("\t"+changeTriplesToFourElements(s)+"\n");
   }

   /*L1:	ifFalse i < 7 goto L2   (<,i,7,L2)
   L3:	i = i + 1                         (+,i,1,i)
   L4:	t1 = 4 * 4                      (*,4,4,t1)
   a [ t1 ] = 6                          (=,6,-,a[t1])
   L5:	j = 5                              (=,5,-,j)
   L6:	ifFalse i >= j goto L1        (>=,i,j,L1)
   L7:	goto L2                           (-,-,-,,L2)
	goto L1                                (-,-,-,L1)
   L2:*/

   public String changeTriplesToFourElements(String triples){
      try {
         StringBuffer sb=new StringBuffer();
         String temp1="";
         String temp2="";
         if (triples.contains("ifFalse")){   //ifFalse i < 7 goto L2
            if (triples.contains("<=")){
               sb.append("( > ,");
               temp1=triples.split("<=")[0];
               temp2=triples.split("<=")[1];
            }else if (triples.contains("<")){
               sb.append("( >=, ");
               temp1=triples.split("<")[0];
               temp2=triples.split("<")[1];
            }else if (triples.contains(">=")){
               sb.append("( < ,");
               temp1=triples.split(">=")[0];
               temp2=triples.split(">=")[1];
            }else if (triples.contains(">")){
               sb.append("( <= ,");
               temp1=triples.split(">")[0];
               temp2=triples.split(">")[1];
            }else if (triples.contains("==")){
               sb.append("( != ,");
               temp1=triples.split("==")[0];
               temp2=triples.split("==")[1];
            }else if (triples.contains("!=")){
               sb.append("( == ,");
               temp1=triples.split("!=")[0];
               temp2=triples.split("!=")[1];
            }
            sb.append(temp1.split(" ")[1]+",");
            sb.append(temp2.split("goto")[0]+",");
            sb.append(temp2.split("goto")[1]+" )");
         }else if (triples.contains("if")){  //if  i<7 goto L2
            if (triples.contains("<=")){
               sb.append("( <= ,");
               temp1=triples.split("<=")[0];
               temp2=triples.split("<=")[1];
            }else if (triples.contains("<")){
               sb.append("( <, ");
               temp1=triples.split("<")[0];
               temp2=triples.split("<")[1];
            }else if (triples.contains(">=")){
               sb.append("( >= ,");
               temp1=triples.split(">=")[0];
               temp2=triples.split(">=")[1];
            }else if (triples.contains(">")){
               sb.append("( > ,");
               temp1=triples.split(">")[0];
               temp2=triples.split(">")[1];
            }else if (triples.contains("==")){
               sb.append("( == ,");
               temp1=triples.split("==")[0];
               temp2=triples.split("==")[1];
            }else if (triples.contains("!=")){
               sb.append("( != ,");
               temp1=triples.split("!=")[0];
               temp2=triples.split("!=")[1];
            }
            sb.append(temp1.split(" ")[1]+",");
            sb.append(temp2.split("goto")[0]+", ");
            sb.append(temp2.split("goto")[1]+" )");
         } else if (triples.contains("goto")){ //goto L1
            sb.append("(-.-.-."+triples.split(" ")[1]+" )");
         }else if (triples.contains("=")){ //i = i + 1   or a [ t1 ] = 6
            if (triples.contains("+")||triples.contains("-")||triples.contains("*")||triples.contains("/")){
               temp1=triples.split("=")[0];
               temp2=triples.split("=")[1];
               String temp3=" ";
               String temp4=" ";
               if (triples.contains("+")){
                  sb.append("( + ,");
                  temp3=temp2.split("\\+")[0];
                  temp4=temp2.split("\\+")[1];
               }else if (triples.contains("-")){
                  sb.append("( - ,");
                  temp3=temp2.split("-")[0];
                  temp4=temp2.split("-")[1];
               }else if (triples.contains("*")){
                  sb.append("( * ,");
                  temp3=temp2.split("\\*")[0];
                  temp4=temp2.split("\\*")[1];
               }else if (triples.contains("/")){
                  sb.append("( / ,");
                  temp3=temp2.split("/")[0];
                  temp4=temp2.split("/")[1];
               }
               sb.append(temp3+" , "+temp4+" , "+temp1+")");

            }else {  //a [ t1 ] = 6
               sb.append("( = , "+triples.split("=")[1]+", - ,"+triples.split("=")[0]+" )");
            }

         }
         return sb.toString();
      }catch (Exception e){
         e.printStackTrace();
         return " ";
      }

   }

   public static  void addCallFunc(){
      triplesString.append("\t"+"call func 1,2"+"\n");
      fourElementsString.append("\t"+"(call,func,1,2)"+"\n");
   }

}
