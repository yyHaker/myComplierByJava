package com.yyHaker.lexical.scanner;

import com.yyHaker.lexical.model.Error;
import com.yyHaker.lexical.model.Key;
import com.yyHaker.lexical.model.KeyInfor;

import java.util.ArrayList;
import java.util.List;

/**
 * a concrete lexical to check the input character
 *扫描输入的字符
 * 1.预处理  滤掉空格，跳过注释、换行符
 * 2.识别各种字符，输出token序列
 *      关键字、特殊字符、单界符、数学运算符、复合运算符、转义字符、常量类型等等
 * 主要思想是基于模拟DFA的实现，所有逻辑是由代码模拟实现
 * @author yyHaker
 * @create 2016-09-25-19:51
 */
public class MyLexical {
    //记录输出的token词法单元
    private   List<KeyInfor> tokenList=new ArrayList<KeyInfor>();
    //记录错误信息
    private   List<Error> errorList=new ArrayList<Error>();
    //记录输入的总的字符
    private String input;
    //记录输入字符的下标
    private   int  inputIndex;
    //记录当前字符
    private char currentCharacter;
    //记录当前行号
    private int row=1;
    //记录当前状态
    private int state=0;
    //判断某个模块的词法识别是否结束
    private boolean isFinish=true;

    public List<KeyInfor> getTokenList() {
        return tokenList;
    }

    public List<Error> getErrorList() {
        return errorList;
    }

    Scanner scanner=new Scanner();

    /**
     * 读取下一个字符
     * @return  currentCharacter
     */
    private char getChar(){
            inputIndex++;
           if(inputIndex<input.length()){
               currentCharacter=input.charAt(inputIndex);

           }
         return currentCharacter;
    }

    /**
     * 开始词法分析
     * @param scanningInput
     */
    public void startLexical(String scanningInput){
            //基本初始化操作
            input=scanningInput+" ";//此处在输入的文本后自行添加一个字符，以便可以预读取下一个字符
            inputIndex=-1;
            Key  tempKey=new Key();

        while(inputIndex<scanningInput.length()){

              currentCharacter=getChar();

              if(scanner.isSkip(currentCharacter)){//1.预处理:跳过空格、制表符、回退符、回车符、回车换行符
                   if (currentCharacter=='\n'){
                       row++;
                   }
              }
             else if(scanner.isSingleDelimiters(currentCharacter)!=null){//2.判断是否为单界符
                  tempKey=scanner.isSingleDelimiters(currentCharacter);
                  tokenList.add(new KeyInfor(tempKey,row));
              }
             else if(currentCharacter=='/'&&input.charAt(inputIndex+1)=='*') {//3.判断注释
                  isFinish = false;
                  if (input.charAt(inputIndex + 1) == '*') {
                      state = 1;

                      while (inputIndex < scanningInput.length() && (!isFinish)) {
                          switch (state) {
                              case 1:currentCharacter = getChar();
                                   if (currentCharacter == '*') {
                                      state = 2;
                                  }
                                  break;
                              case 2:currentCharacter = getChar();
                                  if (currentCharacter == '*') {
                                      state = 3;
                                  } else {
                                      if (currentCharacter == '\n') {
                                          row++;
                                      }
                                      state = 2;
                                  }
                                  break;
                              case 3:currentCharacter = getChar();
                                  if (currentCharacter == '*') {
                                      state = 3;
                                  } else if (currentCharacter == '/') {
                                      state = 4;
                                  } else {
                                      if (currentCharacter == '\n') {
                                          row++;
                                      }
                                      state = 2;
                                  }
                                  break;
                              case 4:isFinish = true;//跳出循环继续后面的词法分析
                                         break;
                          }
                      }
                      //判断注释是否没有正常封闭
                      if (!isFinish){
                          errorList.add(new Error(row,null,"注释未封闭"));
                      }
                  } else if ((!isFinish)&&input.charAt(inputIndex+1)!='*'){
                      errorList.add(new Error(row, "/", "注释格式不正确"));
                      //跳过该符号，继续下面的词法翻译
                  }
              } else if(isLetter_(currentCharacter)){//4.判断标识符或者关键字
                  state=2;
                  StringBuilder identifier=new StringBuilder();
                  identifier.append(currentCharacter);
                  isFinish=false;
                  while(inputIndex<scanningInput.length()&&(!isFinish)){
                       switch (state){
                           case 1:
                           case 2:if (isLetter_(input.charAt(inputIndex+1))||Character.isDigit(input.charAt(inputIndex+1))){
                                          currentCharacter=getChar();
                                          identifier.append(currentCharacter);
                                          state=2;
                                      }else{
                                         isFinish=true;
                                      }
                                     break;
                       }
                  }
                  if (scanner.isKeyWordOrNull(identifier.toString())!=null){
                      tokenList.add(new KeyInfor(scanner.isKeyWordOrNull(identifier.toString()),row));
                  }else{
                      tokenList.add(new KeyInfor(new Key("<IDN,"+identifier.toString()+">",256,"标识符"),row));
                  }
              }else if(Character.isDigit(currentCharacter)){//5.判断无符号整数和浮点数
                isFinish=false;
                state=3;
                StringBuilder unsignedNumber=new StringBuilder();
                unsignedNumber.append(currentCharacter);
                while(inputIndex<input.length()&&(!isFinish)){
                    switch (state){
                        case 3:currentCharacter=getChar();
                            if(Character.isDigit(currentCharacter)){
                                unsignedNumber.append(currentCharacter);
                                state=3;
                            }else if(currentCharacter=='.'){
                                unsignedNumber.append(currentCharacter);
                                state=4;
                            }else if(currentCharacter=='E'){
                                unsignedNumber.append(currentCharacter);
                                state=6;
                            }else{
                                state=10;
                            }
                            break;
                        case 4:currentCharacter=getChar();
                            if (Character.isDigit(currentCharacter)){
                                unsignedNumber.append(currentCharacter);
                                state=5;
                            }else{
                                state=12;
                            }
                            break;
                        case 5:currentCharacter=getChar();
                            if (Character.isDigit(currentCharacter)){
                                unsignedNumber.append(currentCharacter);
                                state=5;
                            }else if (currentCharacter=='E'){
                                unsignedNumber.append(currentCharacter);
                                state=6;
                            }else{
                                state=11;
                            }
                            break;
                        case 6:currentCharacter=getChar();
                            if (currentCharacter=='+'||currentCharacter=='-'){
                                unsignedNumber.append(currentCharacter);
                                state=7;
                            }else if(Character.isDigit(currentCharacter)){
                                unsignedNumber.append(currentCharacter);
                                state=8;
                            }else{
                                state=12;
                            }
                            break;
                        case 7:currentCharacter=getChar();
                            if (Character.isDigit(currentCharacter)){
                                unsignedNumber.append(currentCharacter);
                                state=8;
                            }else{
                                state=12;
                            }
                            break;
                        case 8:currentCharacter=getChar();
                            if (Character.isDigit(currentCharacter)){
                                unsignedNumber.append(currentCharacter);
                                state=8;
                            }else{
                                state=9;
                            }
                            break;
                        case 9://回退一个字符
                            inputIndex--;
                            isFinish=true;
                            tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),scanner.getConstTypeCode("浮点型常量"),"浮点型常量"),row));
                            break;
                        case 10://回退一个字符
                            inputIndex--;
                            isFinish=true;
                            tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),scanner.getConstTypeCode("INT10"),"INT10"),row));
                            break;
                        case 11://回退一个字符
                            inputIndex--;
                            isFinish=true;
                            tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),scanner.getConstTypeCode("浮点型常量"),"浮点型常量"),row));
                            break;
                        case 12://处理错误
                            errorList.add(new Error(row,unsignedNumber.toString(),"浮点数格式不正确"));
                            inputIndex--;
                            isFinish=true;
                            break;
                    }
                }
            }
              else if(scanner.isMathOperator(currentCharacter+"")!=null&&input.charAt(inputIndex+1)!='*'){//6.判断数学运算符
                    StringBuilder operator=new StringBuilder();
                     operator.append(currentCharacter);
                     currentCharacter=getChar();
                    operator.append(currentCharacter);
                    if ((tempKey=scanner.isMathOperator(operator.toString()))!=null){
                        tokenList.add(new KeyInfor(tempKey,row));
                    }else{
                        inputIndex--;
                        tokenList.add(new KeyInfor(scanner.isMathOperator(input.charAt(inputIndex)+""),row));
                    }
              }

        }
    }

    /**
     * 判断当前字符是否为letter_的形式
     * @param inputchar
     * @return
     */
    public boolean isLetter_(char inputchar){
        return Character.isLetter(inputchar)||(inputchar=='_');
    }


    /**
     * 打印输出tokenList
     */
    public  void printTokenList(){
        for (KeyInfor keyInfor:tokenList){
            System.out.println(keyInfor.toString());
        }
    }

    /**
     * 打印输出errorList
     */
    public void printErrorList(){
        for (Error error:errorList){
            System.out.println(error.toString());
        }
    }
}