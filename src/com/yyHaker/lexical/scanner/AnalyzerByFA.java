package com.yyHaker.lexical.scanner;

import com.yyHaker.lexical.model.*;
import com.yyHaker.lexical.model.Error;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;

/**
 * AnalyzerByFA
 *扫描输入的字符
 * 1.预处理  滤掉空格，跳过注释、换行符
 * 2.识别各种字符，输出token序列
 *      关键字、特殊字符、单界符、数学运算符、复合运算符、常量类型等等
 * 实现的主要思想是基于DFA:通过将完整的DFA用三元组的格式写在文件中，然后实现
 * 基本自动化判断逻辑，根据导入的DFA来进行词法分析
 * 存储结构:主要是用了一个Node类的数据结构
 * @author Le Yuan
 * @date 2016/10/15
 */
public class AnalyzerByFA {

    //记录输出的指定格式 (设置为静态，便于语法分析器调用)
    public static List<TokenString> tokenStringList=new ArrayList<TokenString>();

    //记录输出的token词法单元
    private static List<KeyInfor> tokenList=new ArrayList<KeyInfor>();
    //记录错误信息
    private   List<Error> errorList=new ArrayList<Error>();
    //记录输入的总的字符
    private   String input;
    //记录输入字符的下标
    private   int  inputIndex;
    //记录当前字符
    private char currentCharacter;
    //记录当前行号
    private int row=1;

    //输入框中的文本字符串
     public static  String  codeText;

    private List<Node> nodeList;  //存放节点的集合
    private List<Integer> finiteStateNodeList;//终极状态代号的集合

    public List<KeyInfor> getTokenList() {
        return tokenList;
    }

    public List<Error> getErrorList() {
        return errorList;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public List<Integer> getFiniteStateNodeList() {
        return finiteStateNodeList;
    }

    com.yyHaker.lexical.scanner.Scanner myScanner=new com.yyHaker.lexical.scanner.Scanner();

    public AnalyzerByFA() {
        nodeList=new ArrayList<Node>();
        finiteStateNodeList=new ArrayList<Integer>();
    }

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
     * 判断终态集合中是否存在代号
     * @param state
     * @return
     */
    public boolean isExistFiniteState(int state){
        for (int  a:finiteStateNodeList) {
            if (a==state) return true;
        }
        return false;
    }

    /*public boolean isMatchRegex(String myString,String regex){
          myString.matches(regex);
    }*/

    /**
     * 判断节点集合中是否存在代号和currentNode相同的node
     * @param currentNode
     * @return Node
     */
    public Node getNodeFromNodeList(int currentNode){
        for (Node myNode:nodeList) {
            if (myNode.getCurrentState()==currentNode) return myNode;
        }
        return null;
    }

    /**
     * 从FA文件中读取状态
     * @param file
     */
    public void getStateFromFA(File file){
        try {
            FileInputStream fis=new FileInputStream(file);
            java.util.Scanner in=new Scanner(fis);
            while (in.hasNextLine()){
                String line=in.nextLine().trim();
                String []tokens=line.split("~");

                int currentState;
                String regex;
                int nextState;

                //获得currentState
                if (tokens[0].contains("#")){
                    tokens[0]=tokens[0].substring(0,tokens[0].length()-1);
                    currentState=Integer.parseInt(tokens[0]);
                     if(!isExistFiniteState(currentState)) {
                       finiteStateNodeList.add(currentState);
                    }
                }else {
                    currentState=Integer.parseInt(tokens[0]);
                }


                 //获得regex
                 regex=tokens[1];

                //获得nextState
                if (tokens[2].contains("#")){
                    tokens[2]=tokens[2].substring(0,tokens[2].length()-1);
                    nextState=Integer.parseInt(tokens[2]);
                    if(!isExistFiniteState(nextState)) {
                        finiteStateNodeList.add(nextState);
                    }
                }else {
                    nextState=Integer.parseInt(tokens[2]);
                }


                //添加nextNode和currentNode
                Node nextNode;
                if (getNodeFromNodeList(nextState)==null){
                    nextNode=new Node(nextState);
                    nodeList.add(nextNode);
                }else{
                    nextNode=getNodeFromNodeList(nextState);
                }

                Node currentNode;
                if (getNodeFromNodeList(currentState)==null){
                    currentNode=new Node(currentState);
                    currentNode.getStateTransitionList().put(regex,nextNode);
                    nodeList.add(currentNode);
                 }else{
                    currentNode=getNodeFromNodeList(currentState);
                    currentNode.getStateTransitionList().put(regex,nextNode);
                }

               // System.out.println(currentState+","+regex+","+nextState);
            }
            //初始化每一个状态是否为终极状态
            for (Node node: nodeList){
                if(isExistFiniteState(node.getCurrentState())){
                    node.setFiniteState(true);
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void startLexical(String scanningInput){
        //基本初始化操作
        row=1;
        input=scanningInput+" ";//此处在输入的文本后自行添加一个字符，以便可以预读取下一个字符
        inputIndex=-1;
        int stateCode=0;
        //初始化为初始状态
        Node tempNode=getNodeFromNodeList(stateCode);
        while (inputIndex<input.length()){
               currentCharacter=getChar();
              //预处理
            if(myScanner.isSkip(currentCharacter)){//1.预处理:跳过空格、制表符、回退符、回车符、回车换行符
                if (currentCharacter=='\n'){
                    row++;
                }
            }else  if (myScanner.isMathOperator(currentCharacter+"")!=null&&input.charAt(inputIndex+1)!='*'){
                //判断数学运算符,记录为状态28
                    StringBuilder myoperator=new StringBuilder();
                    Key tempKey;
                    myoperator.append(currentCharacter);
                    currentCharacter=getChar();
                    myoperator.append(currentCharacter);
                    if ((tempKey=myScanner.isMathOperator(myoperator.toString()))!=null){
                        tokenList.add(new KeyInfor(tempKey,row));
                    }else{
                        inputIndex--;//回退一个字符
                        tokenList.add(new KeyInfor(myScanner.isMathOperator(input.charAt(inputIndex)+""),row));
                    }

            } else {
                    StringBuilder tokenString=new StringBuilder();
                     //根据DFA状态转换进行词法分析，根据给定的输入进入下一状态
                    while(getNodeFromNodeList(stateCode).getNextStateNode(currentCharacter)!=null){
                        tokenString.append(currentCharacter);
                        tempNode=getNodeFromNodeList(stateCode).getNextStateNode(currentCharacter);
                        stateCode=tempNode.getCurrentState();
                        if (inputIndex<input.length()){
                            currentCharacter=getChar();
                        }else{
                            break;
                        }
                    }

                    //循环跳出时的情况:1.不能再继续匹配2.已经读到最后一个字符
                   if(inputIndex<input.length()){
                       inputIndex--;
                       //错误处理
                       /*while (inputIndex<input.length()&&input.charAt(inputIndex+1)!=' '&&input.charAt(inputIndex+1)!='\n'){
                            tokenString.append(getChar());
                       }*/
                   }

                   //对于stateCode和tokenString给出相应处理
                   dealWithResult(stateCode,tokenString.toString());
                  //回到初始状态，继续向后分析
                   stateCode=0;
            }

        }
    }

    /**
     * 对于stateCode和tokenString给出相应处理
     * @param stateCode
     * @param tokenString
     */
    public void dealWithResult(int stateCode,String tokenString){
        if (isExistFiniteState(stateCode)){
             switch (stateCode){
                 case 12: if (myScanner.isKeyWordOrNull(tokenString)!=null){//关键字
                             tokenList.add(new KeyInfor(myScanner.isKeyWordOrNull(tokenString),row));
                         }else{
                             tokenList.add(new KeyInfor(new Key("<IDN," + tokenString+ ">", 256, "标识符"), row));
                         }
                     break;     //关键字、标识符、INT10、浮点型常量、浮点型常量(科学计数法)、INT8、INT16、字符型常量、字符串型常量、界符、运算符/界符
                 case 13:
                 case 21:
                 case 29:tokenList.add(new KeyInfor(new Key(tokenString,myScanner.getConstTypeCode("INT10"),"INT10"),row));
                     break;
                 case 15: tokenList.add(new KeyInfor(new Key(tokenString,myScanner.getConstTypeCode("浮点型常量"),"浮点型常量"),row));
                     break;
                 case 19:tokenList.add(new KeyInfor(new Key(tokenString,myScanner.getConstTypeCode("浮点型常量"),"浮点型常量(科学计数法)"),row));
                     break;
                 case 30:  tokenList.add(new KeyInfor(new Key(tokenString,myScanner.getConstTypeCode("INT8"),"INT8"),row));
                     break;
                 case 32:  tokenList.add(new KeyInfor(new Key(tokenString,myScanner.getConstTypeCode("INT16"),"INT16"),row));
                     break;
                 case 40:
                     //错误处理
                     StringBuilder stringBuilder=new StringBuilder(tokenString);
                     while (inputIndex<input.length()&&input.charAt(inputIndex+1)!=' '&&input.charAt(inputIndex+1)!='\n'){
                         stringBuilder.append(getChar());
                     }
                     tokenString=stringBuilder.toString();
                     errorList.add(new Error(row,tokenString,"八进制数格式错误"));
                     break;
                 case 23: if (myScanner.isSingleDelimiters(tokenString.charAt(0))!=null){//单界符
                         tokenList.add(new KeyInfor(myScanner.isSingleDelimiters(tokenString.charAt(0)),row));
                     }
                     break;
                 case 35:tokenList.add(new KeyInfor(new Key(tokenString,myScanner.getConstTypeCode("字符型常量"),"字符型常量"),row));
                     break;
                 case 38:tokenList.add(new KeyInfor(new Key(tokenString,myScanner.getConstTypeCode("字符串型常量"),"字符串型常量"),row));
                     break;
                 case 27://注释格式正确
                     break;

             }
        }else{

            //错误处理
                      StringBuilder stringBuilder=new StringBuilder(tokenString);
                       while (inputIndex<input.length()&&input.charAt(inputIndex+1)!=' '&&input.charAt(inputIndex+1)!='\n'){
                             stringBuilder.append(getChar());
                       }
                       tokenString=stringBuilder.toString();

            switch (stateCode){
                case 0:errorList.add(new Error(row,tokenString,"不能识别的非法字符"));
                    break;
                case 14:
                case 16:
                case 17:  errorList.add(new Error(row,tokenString,"浮点数格式不正确"));
                    break;
                case 31: errorList.add(new Error(row,tokenString,"十六进制数格式错误"));
                    break;
                case 33:if (myScanner.isSingleDelimiters(tokenString.charAt(0))!=null){//单界符
                    tokenList.add(new KeyInfor(myScanner.isSingleDelimiters(tokenString.charAt(0)),row));
                }
                    break;
                case 34:  errorList.add(new Error(row,tokenString,"字符常量格式错误"));
                    break;
                case 36:if (myScanner.isSingleDelimiters(tokenString.charAt(0))!=null){//单界符
                    tokenList.add(new KeyInfor(myScanner.isSingleDelimiters(tokenString.charAt(0)),row));
                }
                    break;
                case 37:errorList.add(new Error(row,tokenString,"字符串型常量格式错误"));
                    break;
                case 24:if(myScanner.isMathOperator(tokenString)!=null){
                    tokenList.add(new KeyInfor(myScanner.isMathOperator(tokenString),row));
                  }
                    break;
                case 25:
                case 26:  errorList.add(new Error(row, tokenString, "注释未封闭"));
                    break;
            }

        }

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

    /**(待完善)
     * 设置tokenStringList，便于语法分析调用
     */
    public void setTokenStringList(){
          for (KeyInfor keyInfor:tokenList){
               tokenStringList.add(changeKeyInforToTokenString(keyInfor));
          }
    }

    /**
     * 将keyInfor转变为指定格式的token序列
     *  keyInfor.type:关键字、标识符、INT10、浮点型常量、浮点型常量(科学计数法)、INT8、INT16、
     *  字符型常量、字符串型常量、界符、运算符/界符
     *
     * @param keyInfor
     * @return
     */
    public TokenString changeKeyInforToTokenString(KeyInfor keyInfor){
        TokenString tokenString;
         Key key=keyInfor.getKey();
        if (key.getType().equals("关键字")){
             tokenString=new TokenString(key.getName(),key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("标识符")){
            tokenString=new TokenString("IDN",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("INT10")){
            tokenString=new TokenString("INT10",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("浮点型常量")){
            tokenString=new TokenString("FLOAT",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("浮点型常量(科学计数法)")){
            tokenString=new TokenString("FLOAT",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("INT8")){
            tokenString=new TokenString("INT8",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("INT16")){
            tokenString=new TokenString("INT16",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("字符型常量")){
            tokenString=new TokenString("CHAR",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("字符串型常量")){
            tokenString=new TokenString("STRING",key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("界符")){
            tokenString=new TokenString(key.getName(),key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        if (key.getType().equals("运算符/界符")){
            tokenString=new TokenString(key.getName(),key.getName(),keyInfor.getRow());
            return  tokenString;
        }
        return  null;
    }

    public static void main(String []args){
        //AnalyzerByFA analyzerByFA=new AnalyzerByFA();
        //analyzerByFA.getStateFromFA(new File("FAtable.txt"));
         //System.out.println(analyzerByFA.getNodeFromNodeList(0).getNextStateNode('A').toString());

        /* for (Node node:analyzerByFA.getNodeList()){
             //System.out.println(node.getCurrentState()+"---"+node.isFiniteState());
             System.out.println(node.toString());
         }*/
        // System.out.println(('/'+"").matches("[/]"));
         //
    }
}
