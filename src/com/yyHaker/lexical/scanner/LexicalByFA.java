package com.yyHaker.lexical.scanner;

import com.yyHaker.lexical.model.Error;
import com.yyHaker.lexical.model.Key;
import com.yyHaker.lexical.model.KeyInfor;
import com.yyHaker.lexical.view.LexicalController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * 在此定义状态码：
 * 初始状态               0
 *  letter_                 1
 *  digit                     2
 *  delimiters            3
 *  AnnotationHead  4
 *  Operator             5
 *
 * a lexical based on FA
 * The FA table is stored by a int Array
 * 主要思想是基于模拟DFA的实现，所有逻辑是由代码模拟实现
 * @author yyHaker
 * @create 2016-10-13-8:41
 */
public class LexicalByFA {
    //存取状态转换表
    private int [][]stateFA;
    /**
     * Constructor
     */
    public LexicalByFA() {
        stateFA=new int[10][10];
    }
    /**-------------------------------------------------------------------------------------**/
     //记录输出的token词法单元
     private List<KeyInfor> tokenList=new ArrayList<KeyInfor>();
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
    //记录父状态
     private int parentState;
    //记录字符状态
     private  int charState;
    //记录当前子状态
    private  int currentState;
     //判断某个模块的词法识别是否结束
     private boolean isFinish=true;
    //记录当前整数值
    private  int tempValue;
    //记录转换状态
    private HashMap<String,List<String>> DFAList=new HashMap<String, List<String>>();

     public List<KeyInfor> getTokenList() {
     return tokenList;
     }

     public List<Error> getErrorList() {
     return errorList;
     }

    com.yyHaker.lexical.scanner.Scanner myScanner=new com.yyHaker.lexical.scanner.Scanner();

     /**
     * 读取下一个字符,下标为当前字符的下标
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
     * 判断当前字符是否为letter_的形式
     * @param inputchar
     * @return
     */
    public boolean isLetter_(char inputchar){
        return Character.isLetter(inputchar)||(inputchar=='_');
    }

    /**
     * 判断当前字符是否是一个m-n之间的一个数
     * @param myChar
     * @param m
     * @param n
     * @return
     */
     public boolean charIsBetweenDigit(char myChar,int m,int n){
         if (Character.isDigit(myChar)){
             int value=Integer.parseInt(myChar+"");
             return value>=m&&value<=n;
         }
          return false;
     }

    /**
     * 判断一个字符是否是16进制字母字符
     * @param myChar
     * @return
     */
    public boolean charisLetterBetweenAF(char myChar){
        return myChar=='a'||myChar=='b'||myChar=='c'||myChar=='d'||myChar=='e'||myChar=='f'
                   ||myChar=='A'||myChar=='B'||myChar=='C'||myChar=='D'||myChar=='E'||myChar=='F';
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


    /**
     *从给定的文件中初始化状态转换表
     * (此方法由MainApp中的LexicalController对象调用)
     * @param file
     */
    public void getStateFromFA(File file) {
        try {
            //循环下标
            int i=0,j=0;
            int countRow=0;
            //记录数组的第二维度数值下标
            int m=0;


            FileInputStream fis = new FileInputStream(file);
            Scanner in = new Scanner(fis);

            /**
             * 实现将文本中的数据初始化到二维数组中
             */
             in.nextLine();//直接跳过第一行
            while (in.hasNextLine()) {
                i=0;
                m=0;
                //System.out.println(in.nextLine());
                String temp=in.nextLine().trim();
                char []ctemp=temp.toCharArray();
                while(i<ctemp.length){
                    if (Character.isDigit(ctemp[i])){
                         stateFA[j][m]=Integer.parseInt(ctemp[i]+"");
                        //System.out.print(ctemp[i]);
                        m++;
                    }
                    i++;
                }
                j++;
                //System.out.println();
                //System.out.println(temp);
                //记录行数
              countRow=j;
            }
            /**
             * 测试状态数组
             */
            System.out.println("--------------------");
           for (i=0;i<countRow;i++){
               for (j=1;j<countRow;j++){
                   System.out.print(stateFA[i][j]+" ");
               }
               System.out.println();
           }

            in.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始词法分析：
     * 扫描输入的字符
     * 1.预处理  滤掉空格，跳过注释、换行符
     * 2.识别各种字符，输出token序列
     *      关键字、特殊字符、单界符、数学运算符、复合运算符、转义字符、常量类型等等
     * @param scanningInput
     */
    public void  startLexical(String scanningInput){
        //基本初始化操作
        input=scanningInput+" ";//此处在输入的文本后自行添加一个字符，以便可以预读取下一个字符
        inputIndex=-1;
        Key tempKey=new Key();

        //初始状态为0
        parentState=0;
        charState=0;
        while (inputIndex<input.length()){
             currentCharacter=getChar();

              //状态转换
               if (isLetter_(currentCharacter)){
                    charState=1;
                   currentState=stateFA[parentState][charState];
                   System.out.println(currentCharacter+"----"+currentState);
               }else if(Character.isDigit(currentCharacter)){
                   charState=2;
                   currentState=stateFA[parentState][charState];
                   System.out.println(currentCharacter+"----"+currentState);
               }else if(myScanner.isSingleDelimiters(currentCharacter)!=null){
                    charState=3;
                   currentState=stateFA[parentState][charState];
                   System.out.println(currentCharacter+"----"+currentState);
               }else if (currentCharacter=='/'&&input.charAt(inputIndex+1)=='*'){
                   charState=4;
                   currentState=stateFA[parentState][charState];
                   System.out.println(currentCharacter+"----"+currentState);
               }else if (myScanner.isMathOperator(currentCharacter+"")!=null&&input.charAt(inputIndex+1)!='*'){
                   charState=5;
                   currentState=stateFA[parentState][charState];
                   System.out.println(currentCharacter+"----"+currentState);
               }else  if(myScanner.isSkip(currentCharacter)){//1.预处理:跳过空格、制表符、回退符、回车符、回车换行符
                       if (currentCharacter=='\n'){
                           row++;
                       }
                       //currentCharacter=getChar();
                     currentState=0;
                   }
               else {
                   errorList.add(new Error(row,currentCharacter+"","非法字符"));
               }

            switch (currentState){
                case 0:
                    break;
                case 1:   if(isLetter_(currentCharacter)) {//4.判断标识符或者关键字
                                state = 12;
                                StringBuilder identifier = new StringBuilder();
                                identifier.append(currentCharacter);
                                isFinish = false;
                                while (inputIndex < scanningInput.length() && (!isFinish)) {
                                    switch (state) {
                                        case 11:
                                        case 12:
                                            if (isLetter_(input.charAt(inputIndex + 1)) || Character.isDigit(input.charAt(inputIndex + 1))) {
                                                currentCharacter = getChar();
                                                identifier.append(currentCharacter);
                                                state = 12;
                                            } else {
                                                isFinish = true;
                                            }
                                            break;
                                    }
                                }
                                if (myScanner.isKeyWordOrNull(identifier.toString()) != null) {
                                    tokenList.add(new KeyInfor(myScanner.isKeyWordOrNull(identifier.toString()), row));
                                } else {
                                    tokenList.add(new KeyInfor(new Key("<IDN," + identifier.toString() + ">", 256, "标识符"), row));
                                }
                            }
                    break;
                case 2:    if(Character.isDigit(currentCharacter)){//5.判断无符号整数(包括八进制、十进制、十六进制)和浮点数
                                    isFinish=false;
                                    state=13;
                                    StringBuilder unsignedNumber=new StringBuilder();
                                    unsignedNumber.append(currentCharacter);
                                        if (currentCharacter=='0'&&(Character.isDigit(input.charAt(inputIndex+1))||input.charAt(inputIndex+1)=='x'||input.charAt(inputIndex+1)=='X')){
                                            state=29;
                                        }else{
                                             state=13;
                                        }
                                    while(inputIndex<input.length()&&(!isFinish)){
                                        switch (state){
                                            case 13:currentCharacter=getChar();
                                                if(Character.isDigit(currentCharacter)){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=13;
                                                }else if(currentCharacter=='.'){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=14;
                                                }else if(currentCharacter=='E'||currentCharacter=='e'){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=16;
                                                }else{
                                                    state=20;
                                                }
                                                break;
                                            case 14:currentCharacter=getChar();
                                                if (Character.isDigit(currentCharacter)){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=15;
                                                }else{
                                                    state=22;
                                                }
                                                break;
                                            case 15:currentCharacter=getChar();
                                                if (Character.isDigit(currentCharacter)){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=15;
                                                }else if (currentCharacter=='E'||currentCharacter=='e'){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=16;
                                                }else{
                                                    state=21;
                                                }
                                                break;
                                            case 16:currentCharacter=getChar();
                                                if (currentCharacter=='+'||currentCharacter=='-'){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=17;
                                                }else if(Character.isDigit(currentCharacter)){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=18;
                                                }else{
                                                    state=22;
                                                }
                                                break;
                                            case 17:currentCharacter=getChar();
                                                if (Character.isDigit(currentCharacter)){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=18;
                                                }else{
                                                    state=22;
                                                }
                                                break;
                                            case 18:currentCharacter=getChar();
                                                if (Character.isDigit(currentCharacter)){
                                                    unsignedNumber.append(currentCharacter);
                                                    state=18;
                                                }else{
                                                    state=19;
                                                }
                                                break;
                                            case 19://回退一个字符
                                                inputIndex--;
                                                isFinish=true;
                                                tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),myScanner.getConstTypeCode("浮点型常量"),"浮点型常量"),row));
                                                break;
                                            case 20://回退一个字符
                                                inputIndex--;
                                                isFinish=true;
                                                tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),myScanner.getConstTypeCode("INT10"),"INT10"),row));
                                                break;
                                            case 21://回退一个字符
                                                inputIndex--;
                                                isFinish=true;
                                                tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),myScanner.getConstTypeCode("浮点型常量"),"浮点型常量"),row));
                                                break;
                                            case 22://处理错误
                                                errorList.add(new Error(row,unsignedNumber.toString(),"浮点数格式不正确"));
                                                inputIndex--;
                                                isFinish=true;
                                                break;
                                            case 29:currentCharacter=getChar();
                                                        if (charIsBetweenDigit(currentCharacter,1,7)){
                                                            state=30;
                                                            unsignedNumber.append(currentCharacter);
                                                        }else if (currentCharacter=='x'||currentCharacter=='X'){
                                                             state=31;
                                                             unsignedNumber.append(currentCharacter);
                                                        }else if (currentCharacter=='0'||currentCharacter=='8'||currentCharacter=='9'){
                                                                errorList.add(new Error(row,unsignedNumber.toString()+currentCharacter,"八进制数格式错误"));
                                                               //结束当前分析
                                                               isFinish=true;
                                                        }
                                                break;
                                            case 30:currentCharacter=getChar();
                                                          if (charIsBetweenDigit(currentCharacter,0,7)){
                                                              state=30;
                                                              unsignedNumber.append(currentCharacter);
                                                          }else if (charIsBetweenDigit(currentCharacter,8,9)){
                                                              errorList.add(new Error(row,unsignedNumber.toString()+currentCharacter,"八进制数格式错误"));
                                                              //结束当前分析
                                                              isFinish=true;
                                                          }else{
                                                              tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),myScanner.getConstTypeCode("INT8"),"INT8"),row));
                                                              inputIndex--;
                                                              isFinish=true;
                                                          }
                                                break;
                                            case 31:currentCharacter=getChar();
                                                      if (charIsBetweenDigit(currentCharacter,1,9)||charisLetterBetweenAF(currentCharacter)){
                                                          state=32;
                                                          unsignedNumber.append(currentCharacter);
                                                      }else if (currentCharacter=='0'||(Character.isLetter(currentCharacter)&&!charisLetterBetweenAF(currentCharacter))){
                                                            errorList.add(new Error(row,unsignedNumber.toString()+currentCharacter,"十六进制数格式错误"));
                                                            isFinish=true;
                                                      }else{
                                                          inputIndex--;
                                                          isFinish=true;
                                                      }
                                                break;
                                            case 32:currentCharacter=getChar();
                                                         if (charIsBetweenDigit(currentCharacter,0,9)||charisLetterBetweenAF(currentCharacter)){
                                                             state=32;
                                                             unsignedNumber.append(currentCharacter);
                                                         }else if (Character.isLetter(currentCharacter)&&!charisLetterBetweenAF(currentCharacter)){
                                                             errorList.add(new Error(row,unsignedNumber.toString()+currentCharacter,"十六进制数格式错误"));
                                                             isFinish=true;
                                                         }else{
                                                             tokenList.add(new KeyInfor(new Key(unsignedNumber.toString(),myScanner.getConstTypeCode("INT16"),"INT16"),row));
                                                             inputIndex--;
                                                             isFinish=true;
                                                         }
                                                break;
                                        }
                                    }
                                }
                    break;
                case 3:if(myScanner.isSingleDelimiters(currentCharacter)!=null){//2.判断是否为单界符,接受状态设为23
                                tempKey=myScanner.isSingleDelimiters(currentCharacter);
                                tokenList.add(new KeyInfor(tempKey,row));

                                //在此处进一步判断字符常量和字符串常量
                                 StringBuilder myStr=new StringBuilder();
                                  if (currentCharacter=='\''){
                                       myStr.append(currentCharacter);
                                       isFinish=false;
                                        state=33;
                                        while (inputIndex<scanningInput.length()&&!isFinish){
                                            switch (state){
                                                case 33:currentCharacter=getChar();
                                                            myStr.append(currentCharacter);
                                                            state=34;
                                                    break;
                                                case 34:currentCharacter=getChar();
                                                            if (currentCharacter=='\''){
                                                                myStr.append(currentCharacter);
                                                                state=35;
                                                            }else{
                                                                errorList.add(new Error(row,myStr.toString(),"字符常量格式错误"));
                                                                isFinish=true;
                                                                inputIndex--;
                                                            }
                                                    break;
                                                case  35:tokenList.add(new KeyInfor(new Key(myStr.toString(),myScanner.getConstTypeCode("字符型常量"),"字符型常量"),row));
                                                             isFinish=true;
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                      //判断是否字符串太短
                                      if(!isFinish&&state!=35){
                                          errorList.add(new Error(row,myStr.toString(),"字符常量格式错误"));
                                      }
                                   }
                            //判断字符串常量
                             StringBuilder  str=new StringBuilder();
                            if (currentCharacter=='"'){
                                 str.append(currentCharacter);
                                 isFinish=false;
                                state=36;
                                 while (inputIndex<scanningInput.length()&&!isFinish){
                                      switch (state){
                                          case 36:currentCharacter=getChar();
                                                      str.append(currentCharacter);
                                                      if (currentCharacter=='"'){
                                                         state=37;
                                                      }
                                              break;
                                          case 37:tokenList.add(new KeyInfor(new Key(str.toString(),myScanner.getConstTypeCode("字符串型常量"),"字符串型常量"),row));
                                                        isFinish=true;
                                              break;
                                          default:
                                              break;
                                      }
                                 }
                                //判断是否正常退出
                                 if (!isFinish&&state!=37){
                                     errorList.add(new Error(row,str.toString(),"字符串型常量格式错误"));
                                 }
                            }


                            }
                    break;
                case 4:if(currentCharacter=='/'&&input.charAt(inputIndex+1)=='*') {//3.判断注释
                                isFinish = false;
                                if (input.charAt(inputIndex + 1) == '*') {
                                    state = 24;
                                    while (inputIndex < scanningInput.length() && (!isFinish)) {
                                        switch (state) {
                                            case 24:
                                                currentCharacter = getChar();
                                                if (currentCharacter == '*') {
                                                    state = 25;
                                                }
                                                break;
                                            case 25:
                                                currentCharacter = getChar();
                                                if (currentCharacter == '*') {
                                                    state = 26;
                                                } else {
                                                    if (currentCharacter == '\n') {
                                                        row++;
                                                    }
                                                    state = 25;
                                                }
                                                break;
                                            case 26:
                                                currentCharacter = getChar();
                                                if (currentCharacter == '*') {
                                                    state = 26;
                                                } else if (currentCharacter == '/') {
                                                    state = 27;
                                                } else {
                                                    if (currentCharacter == '\n') {
                                                        row++;
                                                    }
                                                    state = 25;
                                                }
                                                break;
                                            case 27:
                                                isFinish = true;//跳出循环继续后面的词法分析
                                                break;
                                        }
                                    }
                                    //判断注释是否没有正常封闭
                                    if (!isFinish) {
                                        errorList.add(new Error(row, null, "注释未封闭"));
                                    }
                                }
                            }
                    break;
                case 5:   if(myScanner.isMathOperator(currentCharacter+"")!=null&&input.charAt(inputIndex+1)!='*'){//6.判断数学运算符,记录为状态28
                            StringBuilder myoperator=new StringBuilder();
                            myoperator.append(currentCharacter);
                            currentCharacter=getChar();
                            myoperator.append(currentCharacter);
                            if ((tempKey=myScanner.isMathOperator(myoperator.toString()))!=null){
                                tokenList.add(new KeyInfor(tempKey,row));
                            }else{
                                inputIndex=inputIndex-1;//回退一个字符
                                tokenList.add(new KeyInfor(myScanner.isMathOperator(input.charAt(inputIndex)+""),row));
                            }
                        }
                    break;
                default:
                    break;
            }
        }

        //结束分析
    }

    /**
     * 输出词法分析DFA转换表
     */
    public void printDFA(){
        List<String> IDNorKey=new ArrayList<String>();
        IDNorKey.add("<0,Letter_,12>");
        IDNorKey.add("<12,Letter or digit,12");
        DFAList.put("IDNorKey",IDNorKey);

        List<String> unsignedNum=new ArrayList<String>();
        unsignedNum.add("<0,digit,13>");
        unsignedNum.add("<13,digit,13>");
        unsignedNum.add("<13,*,14>");
        unsignedNum.add("<13,others,20>");
        unsignedNum.add("<13,E or e,16>");
        unsignedNum.add("<14,digit,15>");
        unsignedNum.add("<15,digit,15>");
        unsignedNum.add("<15,E or e,16>");
        unsignedNum.add("<15,others,21>");
        unsignedNum.add("<16,+ or -,17>");
        unsignedNum.add("<16,digit,18>");
        unsignedNum.add("<17,digit,18>");
        unsignedNum.add("<18,digit,18>");
        unsignedNum.add("<18,others,19>");
        DFAList.put("unsignedNum",unsignedNum);

        List<String> annotation=new ArrayList<String>();
        annotation.add("<0,/,24>");
        annotation.add("<24,*,25>");
        annotation.add("<25,others,25>");
        annotation.add("<25,*,26>");
        annotation.add("<26,*,26>");
        annotation.add("<26,others,25>");
        annotation.add("<26,/,27>");
        DFAList.put("annotation",annotation);

        List<String> delimiters=new ArrayList<String>();
        delimiters.add("<0,delimiters,23>");
        DFAList.put("delimiters",delimiters);

        List<String>  operator=new ArrayList<String>();
        operator.add("<0,operator,28");
        DFAList.put("operator",operator);

    }
}
