package com.yyHaker.lexical.scanner;

import com.yyHaker.lexical.model.Key;

import java.util.ArrayList;
import java.util.List;

/**
 * a class to define and check  character
 * 定义了关键字、特殊字符、单界符、数学运算符、复合运算符、转义字符、常量类型等集合，
 * 提供相应的判断方法
 * 并将这些字符的信息存储为表
 * @author yyHaker
 * @create 2016-09-25-11:08
 */
public class Scanner {
    //关键字
    private String[] keyWord = new  String[]
            {
                    "auto",
                    "double",
                    "int",
                    "struct",
                    "break",
                    "else",
                    "long",
                    "switch",
                    "case",
                    "enum",
                    "register",
                    "typedef",
                    "char",
                    "extern ",
                    "return",
                    "union",
                    "const",
                    "float",
                    "short",
                    "unsigned",
                    "continue",
                    "for",
                    "signed",
                    "void",
                    "default",
                    "goto",
                    "sizeof",
                    "volatile",
                    "do",
                    "if",
                    "while",
                    "static",
                    "interrupt",
                    "sizeof",
                    "then",
                    "call",
                    "proc",
                    "record"
            };
    //特殊字符
   private String[] sepcialWord = new String[]
            {
                    "NULL"
            };
    //单界符delimiters;
    private char[] singleDelimiters = new char[]
            {
                    '{',
                    '}',
                    '[',
                    ']',
                    '(',
                    ')',
                    ',',
                    '.',
                    ';',
                    '#',
                    '?',
                    ':',
                    '"',
                    '\''
            };
       //数学运算符号
      private String[]  MathOperator = new String[]
            {
                    "<<",
                    ">>",
                    "<",
                    "<=",
                    ">",
                    ">=",
                    "=",
                    "==",
                    "|",
                    "||",
                    "|=",
                    "^",
                    "^=",
                    "&",
                    "&&",
                    "&=",
                    "%",
                    "%=",
                    "+",
                    "++",
                    "+=",
                    "-",
                    "--",
                    "-=",
                    "->",
                    "/",
                    "/=",
                    "*",
                    "*=",
                    "!",
                    "!="
            };
          //位运算符与等号的组合
         private String[] multipleOperator = new String[]
            {
                    "<<=",
                    ">>="
            };
         //常量类型
         private  String[] constType = new String[]
            {
                    "INT10",
                    "INT16",
                    "INT8",
                    "浮点型常量",
                    "字符型常量",
                    "字符串型常量"
            };
          //转义字符
         private String[] convertOperator = new String[]
            {
                    "n",
                    "r",
                    "0",
                    "t",
                    "v",
                    "b",
                    "f",
                    "a",
                    "\"",
                    "'",
                    "\\"
            };

    /**
     * 判断是否跳过相应的空格、制表符、回退符、回车符、回车换行符
     * @param temp
     * @return
     */
    public boolean isSkip(char temp){
            return temp==' '||temp=='\t'||temp=='\b'||temp=='\r'||temp=='\n';
        }

    /**
     * 判断该字符串是否为关键字或者NULL
     * @param string
     * @return
     */
     public Key isKeyWordOrNull(String string){
         for(int i=0;i<keyWord.length;i++){
             if (string.equals(keyWord[i])){
                 return new Key(keyWord[i],i+257,"关键字");
             }
         }
         if (string.equals("NULL")){
             return new Key("NULL",1+keyWord.length+257,"空值");
         }
         return null;
     }

    /**
     * 判断该字符是否为单界符
     * @param ch
     * @return
     */
    public Key isSingleDelimiters(char ch){
        for (int i=0;i<singleDelimiters.length;i++){
            if (ch==singleDelimiters[i]){
                return new Key(singleDelimiters[i]+"",keyWord.length+258+i,"界符");
            }
        }
        return null;
    }

    /**
     * 判断该字符串是否是运算符或者界符
     * @param string
     * @return
     */
    public Key isMathOperator(String string){
        for (int i=0;i<MathOperator.length;i++){
            if (string.equals(MathOperator[i])){
                return new Key(MathOperator[i],keyWord.length+singleDelimiters.length+258+i,"运算符/界符");
            }
        }
        return null;
    }

    /**
     * 判断是否是复合运算符<<=或者>>=
     * @param string
     * @return
     */
    public Key isMutipleOperators(String string){
         for(int i=0;i<multipleOperator.length;i++){
             if (string.equals(multipleOperator[i])){
                 return new Key(multipleOperator[i],keyWord.length+singleDelimiters.length+MathOperator.length+258+i,"复合运算符");
             }
         }
        return null;
    }

    /**
     * 获得常量类型的种别码
     * @param string
     * @return
     */
    public int getConstTypeCode(String string){
        for (int i=0;i<constType.length;i++){
            if (string.equals(constType[i])){
                return keyWord.length+singleDelimiters.length+MathOperator.length+multipleOperator.length+258+i;
            }
        }
        return 0;
    }

    /**
     * 判断是否是转义字符
     * @param string
     * @return
     */
     public Key isConvertOperator(String string){
         for (int i=0;i<convertOperator.length;i++){
             if (string.equals(convertOperator[i])){
                 return new Key(convertOperator[i],keyWord.length+singleDelimiters.length+
                         MathOperator.length+multipleOperator.length+constType.length+258+i,"转义字符");
             }
         }
         return null;
     }

    /**
     * 将各种词符号的种别码存储到list
     * @return
     */
    public List<String> writeToToken(){
        List<String>  tempKey=new ArrayList<String>();
        int code=0;
        tempKey.add("标识符 "+256);
        tempKey.add("-----------------------关键字------------------------");
        for (int i=0;i<keyWord.length;i++){
            code=257+i;
            tempKey.add(keyWord[i]+" "+code);
        }
        code++;
        tempKey.add("NULL"+" "+code);
        tempKey.add("-----------------------单个界符----------------------");
        for (int i=0;i<singleDelimiters.length;i++){
            code++;
            tempKey.add(singleDelimiters[i]+" "+code);
        }
        tempKey.add("------------------------运算符------------------------");
        for (int i=0;i<MathOperator.length;i++){
            code++;
            tempKey.add(MathOperator[i]+" "+code);
        }
        for (int i=0;i<multipleOperator.length;i++){
            code++;
            tempKey.add(multipleOperator[i]+" "+code);
        }
        tempKey.add("----------------------常量----------------------------");
        for (int i=0;i<constType.length;i++){
            code++;
            tempKey.add(constType[i]+" "+code);
        }
        tempKey.add("----------------------转义字符-------------------------");
        for (int i=0;i<convertOperator.length;i++){
            code++;
            String a="\\"+convertOperator[i];
            tempKey.add(a+" "+code);
        }
        return tempKey;
    }

    /**
     * Constructor
     */
    public Scanner(){
        writeToToken();
    }

}
