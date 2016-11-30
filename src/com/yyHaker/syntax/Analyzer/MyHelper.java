package com.yyHaker.syntax.Analyzer;

import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import com.yyHaker.lexical.model.TokenString;
import com.yyHaker.lexical.property.ErrorProperty;
import com.yyHaker.syntax.model.*;
import com.yyHaker.syntax.property.SyntaxErrorProperty;
import com.yyHaker.syntax.utis.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * MyHelper
 *语法分析的辅助类，包括获得终结符和非终结符、计算终结符、非终结符的first集合和非终结符的follow集合以及每个
 * 产生式的select集合、构造预测分析表
 *
 * 预测分析表:保存在一个二维的哈希表中，这个HashMap的key是某一个非终结符，value是其对应的一个一维HashMap，
 * 这个一维HashMap的key是终结符，value是某非终结符遇到某终结符时所对应的操作（一个产生式的右部或是同步记号）
 *
 * 终结符和非终结符:分别保存在一个一维的哈希表中，其值作为HashMap的key,而其值所对应的对象作为HashMap的的value
 *
 * 产生式:所有经过程序处理的仅包含一个产生式右部的产生式保存在一个LIST中
 *
 * 这些定义的域可以给其他类共享，相当于一个内存存储
 * @author Le Yuan
 * @date 2016/10/20
 */
public class MyHelper {
    //终结符
    public static HashMap<String, TerminalSymbol> terminalSymbolMap = new HashMap<String, TerminalSymbol>();
    //非终结符
    public static HashMap<String, NonTerminalSymbol> nonTerminalSymbolMap = new HashMap<String, NonTerminalSymbol>();
     //预测分析表(二维的hashMap)
    public static HashMap<String, HashMap<String, List<String>>> forecastAnalysisTable = new HashMap<String, HashMap<String, List<String>>>();
    //产生式
    public static List<GrammarProduction> singleGrammarProductionList = new ArrayList<GrammarProduction>();
    //设置一个标记
    public static boolean whetherChanged=true;
   //设置同步标记
    public static String SYN = "synch";

    //存储语法错误信息的list
    public static  List<SyntaxErrorProperty>  errorPropertyList=new ArrayList<>();


    //存储推导元素的treeNode
    public static TreeNode myTreeNode;

    private static  int level=1;
    /**
     * 从grammarLineList中截取获得单个产生式，并存入到产生式列表中
     * @param grammarLineList
     */
    public static void getSingleGrammarProduction(List<String> grammarLineList){

        for (int i=0;i< grammarLineList.size();i++){
            String  newString= grammarLineList.get(i).replace('|','@');
            if (newString.contains("@")){
                String []productions=newString.split("@");
                String firstProduction=productions[0];
                String productionHead=firstProduction.split("->")[0];
                singleGrammarProductionList.add(new GrammarProduction(firstProduction));
                for (int j=1;j<productions.length;j++){
                    singleGrammarProductionList.add(new GrammarProduction(productionHead+"->"+productions[j]));
                }
            }else{
                singleGrammarProductionList.add(new GrammarProduction(newString));
            }
        }
        System.out.println("----------------------------文法产生式------------------------");
        for (int i=0;i<singleGrammarProductionList.size();i++){
            System.out.println(singleGrammarProductionList.get(i).getProduction());
        }
        System.out.println();
    }

    /**
     * 从文法singleGrammarProductionList中获取非终结符，并且存入到map中
     * 并设置grammarProduction的leftPart和rightPart
     */
    public static void getNonTerminalSymbolFromGrammar(){
        for (int i=0;i<singleGrammarProductionList.size();i++){
             GrammarProduction grammarProduction=singleGrammarProductionList.get(i);
             String []parts=grammarProduction.getProduction().split("->");
             //设置每一个产生式的头部
             grammarProduction.setLeftPart(parts[0]);
             String []right=parts[1].split(" ");
            //将right数组转换成list
             List<String> rightParts=new ArrayList<String>();
             for (int j=0;j<right.length;j++){
                 rightParts.add(right[j]);
             }
             //设置每一个产生式的体，体是一个list
             grammarProduction.setRightPart(rightParts);
            //添加非终结符
            if (!nonTerminalSymbolMap.containsKey(parts[0]))
                nonTerminalSymbolMap.put(parts[0],new NonTerminalSymbol(parts[0]));
        }

        //输出非终结符
          System.out.println("----------------------------非终结符-----------------------------");
          for (String nonTerminalSymbol:nonTerminalSymbolMap.keySet()){
               System.out.print(nonTerminalSymbol+"\t");
          }
          System.out.println();
    }


    /**
     * 从文法中获得终结符，遍历所有的单个产生式，对每个产生式的右部分进行遍历，如果不是非终结符那么就是终结符
     */
    public static void getTerminalSymbolFromGrammar(){
         for (int i=0;i<singleGrammarProductionList.size();i++){
               List<String> rightPartList=singleGrammarProductionList.get(i).getRightPart();
                for (String rightPart:rightPartList){
                    if (!nonTerminalSymbolMap.containsKey(rightPart)){
                         if (!terminalSymbolMap.containsKey(rightPart)){
                             terminalSymbolMap.put(rightPart,new TerminalSymbol(rightPart));
                         }
                    }
                }
         }
       //输出终结符
        System.out.println("----------------------终结符----------------------------");
        for (String terminalSymbol:terminalSymbolMap.keySet()){
            System.out.print(terminalSymbol+"\t");
        }

    }

    /**
     * 获得非终结符的first集
     * @param nonTerminalSymbol
     * @return
     */
    public static List<String> getNTSFirstList(NonTerminalSymbol nonTerminalSymbol){
        List<GrammarProduction> expList = getNTSGrammarProductionList(nonTerminalSymbol);
        List<String> mFirst = new ArrayList<String>();
        List<String> right = null;

        mFirst = innitNTSFirst(expList, mFirst);// 初始化工作,每个产生式的第一个右部如果是终结符，直接加入first集

        for (int i = 0; i < expList.size(); i++) {
            right = expList.get(i).getRightPart();
            if (right.size() > 0) {
                if (nonTerminalSymbolMap.containsKey(right.get(0))) {// 若右部表达式的第一位为非终结符则递归
                    combineList(mFirst, getNTSFirstList(new NonTerminalSymbol(right.get(0))));

                        int j = 0;
                       while (j < right.size() - 1 && isEmptyExp(right.get(j))) { // 若X→Y1…Yn∈P，且Y1...Yi-1⇒*ε，把yi的first集加进去
                        combineList(mFirst, getNTSFirstList(new NonTerminalSymbol(right.get(j + 1))));
                        j++;
                    }
                }
            }
        }
         //$ 表示空字符
    /* if (mFirst.contains("$"))
            mFirst.remove("$");*/
        //将first集合添加进入非终结符
        combineList(nonTerminalSymbol.getFirstList(), mFirst);
        return mFirst;
    }

    /**
     * 求一个文法串的first集合(调用之前前提终结符和非终结符的first集合必须初始化)
     * @param right
     * @return
     */
     public static  List<String> getNTSFirstStringList(List<String> right){
         List<String> mFirst = new ArrayList<String>();

         if (right.size() > 0) { // a->X1X2X3X4....Xn,   初始化,FIRST(α)= FIRST(X1);
             String curString = right.get(0);
             if (terminalSymbolMap.get(curString) != null)
                 combineList(mFirst, terminalSymbolMap.get(right.get(0)).getFirstList());
             else {
                 combineList(mFirst, getNTSFirstList(new NonTerminalSymbol(curString)));
             }
         }

         if (right.size() > 0) {
             if (nonTerminalSymbolMap.get(right.get(0)) != null) { // 第一个符号为非终结符
                 if (right.size() == 1) { // 直接将这个非终结符的first集加入right的first集
                     combineList(mFirst, nonTerminalSymbolMap.get(right.get(0))
                             .getFirstList());
                 } else if (right.size() > 1) {
                     int j = 0;
                     while (j < right.size() - 1 && isEmptyExp(right.get(j))) { //first(X1X2X3X4X5......Xn)
                         String tString = right.get(j + 1);
                         if (nonTerminalSymbolMap.get(tString) != null) {
                             combineList(mFirst,getNTSFirstList(new NonTerminalSymbol(
                                     tString)));
                         } else {
                             combineList(mFirst, terminalSymbolMap.get(tString)
                                     .getFirstList());
                         }
                         j++;
                     }
                 }
             }
         }
         return mFirst;
     }

    /**
     * 求非终结符的FOLLOW集,最终将follow集存入相应的
     *  NonTerminalSymbol的followList域中
     */
    public static void   getNTSFollowList() {
        for (int i = 0; i < singleGrammarProductionList.size(); i++) {
            String left = singleGrammarProductionList.get(i).getLeftPart();
            List<String> right = singleGrammarProductionList.get(i).getRightPart();

            for (int j = 0; j < right.size(); j++) {
                String cur = right.get(j);

                if (terminalSymbolMap.containsKey(cur)) // 若是终结符则忽略
                    continue;

                if (j < right.size() - 1) {
                    List<String> nList = new ArrayList<String>();
                    for (int m = j + 1; m < right.size(); m++)
                        nList.add(right.get(m));
                    List<String> mfirst = getNTSFirstStringList(nList);
                    for (String str : mfirst) {
                        if (!nonTerminalSymbolMap.get(cur).getFollowList().contains(str)) {
                            combineList(nonTerminalSymbolMap.get(cur).getFollowList(), mfirst);
                            whetherChanged = true;
                            break;
                        }
                    }

                    boolean isNull = true; // 判断A→αBβ，且β⇒*ε，其中β能否为空
                    for (int k = 0; k < nList.size(); k++) {
                        if (!isEmptyExp(nList.get(k))) {
                            isNull = false;
                            break;
                        }
                    }
                    if (isNull == true) {//把follow(A)中的所有符号添加到follow(B)中
                        for (String str : nonTerminalSymbolMap.get(left).getFollowList()) {
                            if (!nonTerminalSymbolMap.get(cur).getFollowList().contains(str)) {
                                combineList(nonTerminalSymbolMap.get(cur).getFollowList(), nonTerminalSymbolMap.get(left).getFollowList());
                                whetherChanged = true;
                                break;
                            }
                        }
                    }
                } else if (j == right.size() - 1) { // 若该非终结符位于右部表达式最后一个字符  A->aB,那么把follow(A)中的所有符号添加到follow(B)中
                    List<String> mfollow = nonTerminalSymbolMap.get(left).getFollowList();
                    for (String str : mfollow) {
                        if (!nonTerminalSymbolMap.get(cur).getFollowList().contains(str)) {
                            combineList(nonTerminalSymbolMap.get(cur).getFollowList(), mfollow);
                            whetherChanged = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 求一个文法表达式的SELECT集,产生式右部如果为空,直接返回null
     *
     * @param g
     *            一个文法表达式
     * @return SELECT集
     */
    public static List<String> getSelectList(GrammarProduction g) {
        List<String> mselect = new ArrayList<String>();
        if (g.getRightPart().size() > 0) {

            if (g.getRightPart().get(0).equals("$")) { // 含有空表达式
                mselect = nonTerminalSymbolMap.get(g.getLeftPart()).getFollowList();
                return mselect;  //SELECT( A→ε )=FOLLOW( A )
            } else {
                mselect = getNTSFirstStringList(g.getRightPart());
                boolean isNull = true;
                for (int i = 0; i < g.getRightPart().size(); i++) { // 如果右部可以推出空
                    if (!isEmptyExp(g.getRightPart().get(i))) {
                        isNull = false;
                        break;
                    }
                }
                if (isNull) {
                    combineList(mselect,nonTerminalSymbolMap.get(g.getLeftPart()).getFollowList());
                }
                return mselect;
            }
        }
        return null;
    }

    /**
     * 计算非终结符号的同步记号集，也就是说当非终结符碰到这些同步记号集合中的符号时是无法获得产生式的，
     * 故应该将此非终结符号弹出
     */
    public static void getSynchList(NonTerminalSymbol nts) {
        combineList(nts.getSynList(), nts.getFollowList()); // 把FOLLOW(A)的元素放入A的同步记号集合
        combineList(nts.getSynList(), nts.getFirstList()); // 把FIRST(A)的元素放入A的同步记号集合

        List<NonTerminalSymbol> higher = nts.getHigherNTS();
        for (int j = 0; j < higher.size(); j++) { // 把高层结构的First集加入A的同步记号集
            combineList(nts.getSynList(), higher.get(j).getFirstList());
        }

        List<String> mSynList = nts.getSynList();

        for (int i = 0; i < singleGrammarProductionList.size(); i++) { // 将select集中的元素从A的同步记号集中除去
            GrammarProduction grammar = singleGrammarProductionList.get(i);
            if (grammar.getLeftPart().equals(nts.getName())) {
                List<String> mSelect = grammar.getSelectList();
                for (int j = 0; j < mSelect.size(); j++) {
                    if (mSynList.contains(mSelect.get(j)))
                        mSynList.remove(mSelect.get(j));
                }
            }
        }
    }


    /**
     * 构造预测分析表:遍历每一个单个产生式，如果多个文法表达式的左部相同，则将其合并；否则加入到分析表中；
     * 并且将同步记号也加入到分析表中
     */
    public static void getAnalysisTable() {
        List<String> synString = new ArrayList<String>();
        synString.add(SYN);

        for (int i = 0; i <singleGrammarProductionList.size(); i++) {
              GrammarProduction grammar = singleGrammarProductionList.get(i);
            // 如果多个文法表达式的左部相同，则将其合并，例如:
            //additive_expression'->+ multiplicative_expression additive_expression'|- multiplicative_expression additive_expression'|++|--|
            if (forecastAnalysisTable.get(grammar.getLeftPart()) != null) {
                for (String sel : grammar.getSelectList()) {
                    forecastAnalysisTable.get(grammar.getLeftPart()).put(sel, grammar.getRightPart());
                }
            } else {
                HashMap<String, List<String>> inMap = new HashMap<String, List<String>>();
                for (String sel : grammar.getSelectList())
                    inMap.put(sel, grammar.getRightPart());
                forecastAnalysisTable.put(grammar.getLeftPart(), inMap);
            }

            NonTerminalSymbol nts = nonTerminalSymbolMap.get(grammar.getLeftPart());
            List<String> mSynList = nts.getSynList();
            for (int j = 0; j < mSynList.size(); j++) {
                   //将同步记号集加入分析表中
                forecastAnalysisTable.get(grammar.getLeftPart()).put(mSynList.get(j), synString);
            }
        }
    }


    /**
     * LL（1）语法分析控制程序，输出分析树
     *基本思想:
     * 设置ip使它指向w的第一个符号，其中ip 是输入指针；
     *  令X=栈顶符号；
    *      while  ( X ≠ ＄ )｛     /  /      栈非空
    *       if ( X 等于ip所指向的符号a)  执行栈的弹出操作，将ip向前移动一个位置；
    *      else  if ( X是一个终结符号)  error ( ) ;
    *      else  if ( M[X，a]是一个报错条目)  error ( ) ;
    *      else  if ( M[X，a] = X →Y1Y2 … Yk  )｛
    *         输出产生式 X →Y1Y2 … Yk  ；
    *         弹出栈顶符号；
    *          将Yk，Yk-1 …，Yi 压入栈中，其中Y1位于栈顶。
    *     }
    *     令X=栈顶符号
    *    }
     *    相应的error处理：
     *    如果M[A,a]是空，表示检测到错误，根据恐慌模式，忽略输入符号a
     *    如果M[A,a]是synch，则弹出栈顶的非终结符A，试图继续分析后面的语法成分
     *     如果栈顶的终结符和输入符号不匹配，则弹出栈顶的终结符
     *
     *    @param sentence 输入符号串
     */
    public static void myParser(List<TokenString> tokenStrings) {
        errorPropertyList.clear();

          //添加一个结尾符号
         tokenStrings.add(new TokenString("#","#",tokenStrings.get(tokenStrings.size()-1).getRow()));
        Stack<String> stack = new Stack<String>();
        int index = 0;
        String curCharacter = null;
        TokenString token=null;
        stack.push("#");
        stack.push("program");
        String myStr=null;
      /*  //初始化treeNode,父节点
        myTreeNode=new TreeNode("program");*/

        //存放树节点的Stack
        Stack<TreeNode> treeStack=new Stack<>();

        //记录每一层的字母符号
        int []charCount=new int[1000];
        charCount[0]=1;
        charCount[1]=1;
        //int level=1;
        level=1;
        charCount[level]=1000;

        //存取标识符的表
        List<IDN> idnList=new ArrayList<>();

        while (!stack.peek().equals("#")) { //如果栈不为空
            if (index < tokenStrings.size()) // 注意下标，否则越界，读取下一个词法单元
                       token=tokenStrings.get(index);

                       curCharacter =token.getName();
            if (terminalSymbolMap.containsKey(stack.peek()) || stack.peek().equals("#")) {
                if (stack.peek().equals(curCharacter)) {//如果栈顶的终结符和输入符号匹配，且不是最后一个#字符则弹出栈顶的终结符，输入符号指向下一个
                    if (!stack.peek().equals("#")) {
                        String tempStr=myStr;//记录上一个终结符
                       myStr= stack.pop();
                        index++;

                        //迁移到语义分析中进行
                           //增加检测标识符的检错机制,标识符可能是声明函数(function)或者变量(variable)
                         /* if (myStr.equals("IDN")){
                                if (treeStack.peek().getName().equals("D")){//正在声明
                                    IDN idn=getIDNByNmae(idnList,token.getValue());
                                      if (tempStr.equals("proc")){//正在进行函数声明
                                          if (idn!=null&&idn.getType().equals("function")){
                                              errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),token.getValue(),"函数"+token.getValue()+"重复声明"));
                                          }else if (idn==null){
                                              idnList.add(new IDN(token.getValue(),"function"));
                                          }
                                      }else {//正在变量声明
                                          if (idn!=null&&idn.getType().equals("variable")){
                                              errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),token.getValue(),"变量"+token.getValue()+"重复声明"));
                                          }else  if (idn==null){
                                              idnList.add(new IDN(token.getValue(),"variable"));
                                          }
                                      }

                                }else {//正在使用标识符
                                       IDN idn=getIDNByNmae(idnList,token.getValue());
                                       if (idn==null){
                                            if (tempStr.equals("call")){
                                                errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),token.getValue(),"函数"+token.getValue()+"未声明，不能调用"));
                                            }else {
                                                errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),token.getValue(),"使用了未声明的变量"+token.getValue()));
                                            }
                                       }
                                }
                          }*/


                         if (treeStack.size()>0){
                             TreeNode node=new TreeNode(myStr);
                             treeStack.peek().getSubNodes().add(node);

                         }

                        //int currentLevel=level;
                              charCount[level]--;
                             int currentLevel=level;
                              //递归回退
                              if (charCount[level]==0){
                                  level--;
                                  countCharNum(charCount);
                              }
                              //语法树回退几步stackTree就pop几次
                               for (int i=0;i<currentLevel-level;i++){
                                   if (treeStack.size()>0){
                                       treeStack.pop();
                                   }
                               }


                    }
                    //否则直接结束

                } else {
                    //如果栈顶的终结符和输入符号不匹配，则弹出栈顶的终结符
                     myStr=stack.pop();
                    System.err.println("当前栈顶符号" + myStr + "与" + curCharacter + "不匹配");
                    errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),curCharacter,"没有符号与" + curCharacter + "匹配，期待符号"+myStr));
                }
            } else {
                //查预测分析表
                List<String> exp = forecastAnalysisTable.get(stack.peek()).get(curCharacter);
                if (exp != null) {
                    if (exp.get(0).equals(SYN)) {//如果M[A,a]是synch，则弹出栈顶的非终结符A，试图继续分析后面的语法成分
                         myStr=stack.pop();
                         System.err.println("遇到SYNCH，从栈顶弹出非终结符" + myStr);
                        errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),curCharacter,"没有符号与" + curCharacter + "匹配"));
                    } else {
                          System.out.println(stack.peek() + "->" + ListToString(exp));
                          myStr=stack.pop();

                        //构造分析树
                        if (level==1){
                            myTreeNode=new TreeNode(myStr);
                            treeStack.push(myTreeNode);
                        }else {
                             if (treeStack.size()>0){
                                 TreeNode treeNode=new TreeNode(myStr);
                                 treeStack.peek().getSubNodes().add(treeNode);
                                 treeStack.push(treeNode);
                             }

                        }

                          //添加到buffer中
                           //if (!ListToString(exp).contains("$")){
                               GrammaticalAnalysis.syntaxTreeBuffer.append(getEmptyByNum(level)+myStr+" ("+token.getRow()+")"+"\r\n");
                           //}
                           /*else{
                               System.out.println("有$");
                           }*/

                              // System.out.println("@"+getEmptyByNum(level-1)+myStr+" ("+token.getRow()+")");

                          //exp入栈(反向放入)
                        for (int j = exp.size() - 1; j > -1; j--) {
                            if (!exp.get(j).equals("$") && !exp.get(j).equals(SYN))
                                stack.push(exp.get(j));
                        }

                          charCount[level]=charCount[level]-1;
                          level++;//每次替换添加一层
                         charCount[level]=exp.size();//记录当前层的符号个数




                           if (exp.get(0).equals("$")){
                                treeStack.peek().getSubNodes().add(new TreeNode("$"));
                                int currentLevel=level;
                                charCount[level]--;
                                 if (charCount[level]==0){
                                     level--;
                                     countCharNum(charCount);
                                 }
                                   if (level==1){
                                       break;
                                   }
                                 for (int i=0;i<currentLevel-level;i++){
                                     treeStack.pop();
                                 }
                           }

                    }
                } else {//没有到达最后一个字符, 如果M[A,a]是空，表示检测到错误，根据恐慌模式，忽略输入符号a; 当碰到最后一个字符不是'#',报错
                    if (index < tokenStrings.size()-1){
                        System.err.println("忽略" + curCharacter);
                        errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),curCharacter,"没有符号与" + curCharacter + "匹配"+",被忽略"));
                        index++;
                    }else {
                        System.err.println("语法错误，没有正常结束' ！");
                        errorPropertyList.add(new SyntaxErrorProperty(token.getRow(),"","语法错误,没有正常结束！"));
                        break;
                    }
                }
            }
        }


    }


    /**
     * 从给定的标识符中根据名字查找标识符，若存在返回标识符，
     * 否则返回null
     * @param idnList
     * @param name
     * @return
     */
    public static IDN getIDNByNmae(List<IDN> idnList ,String name){
        for (IDN idn:idnList){
            if (idn.getName().equals(name)){
                return idn;
            }
        }
        return null;
    }

    /**
     * 递归计算当前层数
     * @param charCount
     * @param level
     */
    public static void  countCharNum(int []charCount){
        if (charCount[level]==0){
            level--;
            if (level>-1){
                countCharNum(charCount);
            }

        }
    }

   /**
     * 根据名字查找相应的node
     * @param name
     * @param treeNode
    * @param level
     * @return
     */
    public static  TreeNode getTreeNodeByNameAndLevel(String name,TreeNode treeNode,int level){
           List<TreeNode> treeNodeList;
         if (level==1) return treeNode;
          for (int i=0;i<level-1;i++){
               treeNode.getSubNodes();
          }
            return  null;
    }


    /**
     * 产生指定数量的空格
     * @param num
     * @return
     */
    public static String getEmptyByNum(int num){
        StringBuffer stringBuffer=new StringBuffer();
        for (int i=0;i<num;i++){
            stringBuffer.append(" ");
        }
        return stringBuffer.toString();
    }



    /**
     * 获得指定非终结符的文法产生式集合
     * @param nonTerminalSymbol
     * @return
     */
    public  static List<GrammarProduction> getNTSGrammarProductionList(NonTerminalSymbol nonTerminalSymbol){
        List<GrammarProduction> ntsGrammarProductionList=new ArrayList<GrammarProduction>();
        for (int i=0;i<singleGrammarProductionList.size();i++){
             if (nonTerminalSymbol.getName().equals(singleGrammarProductionList.get(i).getLeftPart())){
                  ntsGrammarProductionList.add(singleGrammarProductionList.get(i));
             }
        }
     return ntsGrammarProductionList;
    }

    /**
     * 将两个List合并 List1 U List2，最后返回list1
     *
     * @param mfirst1
     *            List1
     * @param mfirst2
     *            List2
     * @return List1
     */
    public static List<String> combineList(List<String> mfirst1, List<String> mfirst2) {
        for (int i = 0; i < mfirst2.size(); i++) {
            if (!mfirst1.contains(mfirst2.get(i)))
                mfirst1.add(mfirst2.get(i));
        }
        return mfirst1;
    }

    /**
     * 初始化First集，即我们将遍历所有的以非终结符开头的文法产生式，
     * 每个产生式的第一个右部如果是终结符，那么加入到该非终结符的first集中
     *
     * @param expList
     * @param mFirst
     */
    public static List<String> innitNTSFirst(List<GrammarProduction> expList, List<String> mFirst) {
        List<String> right = null;
        for (int i = 0; i < expList.size(); i++) { // 初始化FIRST集
            right = expList.get(i).getRightPart();
            if (right.size() > 0) {
                if (terminalSymbolMap.containsKey(right.get(0))) { // 若右部表达式的第一位为终结符则加入nts的FIRST集
                    mFirst.add(right.get(0));
                }
            }
        }
        return mFirst;
    }

    /**
     * 判断一个文法符号能否通过N步推导出$，X->$
     *
     * @param nts
     *            文法符号
     * @return 是否
     */
    public static boolean isEmptyExp(String nts) {
        if (nonTerminalSymbolMap.get(nts) != null) {
            List<GrammarProduction> expList = getNTSGrammarProductionList(nonTerminalSymbolMap.get(nts));
            for (int i = 0; i < expList.size(); i++) {
                GrammarProduction mGrammar = expList.get(i);
                if (mGrammar.getProduction().equals(nts + "->$")) // 是否包含X->$表达式
                    return true;
                else {
                    boolean flag = false;
                    List<String> right = mGrammar.getRightPart();
                    for (int j = 0; j < right.size(); j++) {
                        if (!isEmptyExp(right.get(j))) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag == false) {
                        return true;
                    }
                    flag = false;
                }
            }
            return false;
        } else { // 终结符不能推出空，直接返回false
            return false;
        }
    }


    /**
     * 将两个List<NonTerminalSymbol>合并 List1 U List2
     *
     * @para list1
     * @param list2
     * @return List1
     */
    public static List<NonTerminalSymbol> combine(List<NonTerminalSymbol> list1, List<NonTerminalSymbol> list2) {
        for (int i = 0; i < list2.size(); i++) {
            if (!list1.contains(list2.get(i)))
                list1.add(list2.get(i));
        }
        return list1;
    }

    /**
     * 将List的每项合并为字符串，每项之间以空格分开
     *
     * @param strings
     *            list
     * @return string
     */
    public static String ListToString(List<String> strings) {
        String myStr = "";
        for (int i = 0; i < strings.size(); i++) {
            myStr += strings.get(i) + " ";
        }
        return myStr;
    }

    /**
     * 判断两个非终结符哪个是更高层的结构
     *
     * @param nts1
     * @param nts2
     * @return
     */
    public static boolean isHigher(NonTerminalSymbol nts1, NonTerminalSymbol nts2) {
        List<GrammarProduction> mExp = getNTSGrammarProductionList(nts1);
        for (int i = 0; i < mExp.size(); i++) {
            List<String> mRight = mExp.get(i).getRightPart();
            for (int j = 0; j < mRight.size(); j++) {
                if (mRight.get(j).equals(nts2.getName()))
                    return true;
            }
        }
        return false;
    }

    /**
     * 计算非终结符的高层结构集,得到终结符集合和非终结符结合后就可调用来初始化
     * 每一个非终结符的higherNTS
     */
    public static void calcHigherNTSList() {
        for (String s : nonTerminalSymbolMap.keySet()) {
            NonTerminalSymbol sNonTerminalSymbol = nonTerminalSymbolMap.get(s);
            for (String r : nonTerminalSymbolMap.keySet()) {
                NonTerminalSymbol rNonTerminalSymbol = nonTerminalSymbolMap.get(r);
                if (isHigher(sNonTerminalSymbol, rNonTerminalSymbol)) {
                    if (!rNonTerminalSymbol.getHigherNTS().contains(sNonTerminalSymbol)) {
                        rNonTerminalSymbol.getHigherNTS().add(sNonTerminalSymbol);
                        combine(rNonTerminalSymbol.getHigherNTS(), sNonTerminalSymbol.getHigherNTS());
                        whetherChanged = true;
                    }
                }
            }
        }
    }


    /**
     * 判断一个文法是否为LL（1）文法
     *
     * @return 是否
     */
    public static boolean isLL1() {
        for (String keyString : nonTerminalSymbolMap.keySet()) {
            List<GrammarProduction> glist = getNTSGrammarProductionList(nonTerminalSymbolMap.get(keyString));
            if (glist.size() > 1) {
                for (int i = 0; i < glist.size(); i++) { // 文法相同左部的SELECT集不相交即为LL(1)文法
                    for (int j = 1; j < glist.size(); j++) {
                        if (i != j) {
                               if (isListIntersect(glist.get(i).getSelectList(),glist.get(j).getSelectList())){
                                   System.out.println("以"+keyString+"为左部的select集相交！该文法不是LL(1)型文法!");
                                    return false;
                                  // return  true;
                               }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 判断两个集合是否相交
     * @param list1
     * @param lis2
     * @return
     */

    public static boolean isListIntersect(List list1,List lis2){
        for (int i=0;i<list1.size();i++){
            if (lis2.contains(list1.get(i))){
                return true;
            }
        }
        return false;
    }





//dec	init_declarator	const_list'		initializer_list'	BREAK	additive_expression	ELSE	parameter_list'	ID	IF	==	--	postfix_expression'	$	(	FOR	)	*	+	,	-	/	;	VOID	<	!=	=	>			selection_statement	CHARACTER	[	]	statement_list	++	expression	DIGIT	declaration	declarator	CONTINUE	additive_expression'	program'	WHILE	{	relational_expression	parameter_list	}	declarator_func	statement_list'	jump_statement	multiplicative_expression'
//FLOAT	CHAR	INT	RETURN	FRACTION	BREAK	ELSE	CHARACTER	ID	[	]	IF	==	++	--	$	(	FOR	)	*	+	,	DIGIT	-	/	CONTINUE	WHILE	{	;	VOID	<	!=	}	=	>
/*
 public static void main(String []args){
     FileUtils.getGrammarFromFile(new File("myGrammar1.txt"));
      getSingleGrammarProduction(FileUtils.grammarLineList);
      getNonTerminalSymbolFromGrammar();
       getTerminalSymbolFromGrammar();

   */
/*  System.out.println();
       System.out.println("每个非终结符的first集合------------------------------------------");
       for (String key:nonTerminalSymbolMap.keySet()){
           List<String> firstList=getNTSFirstList(nonTerminalSymbolMap.get(key));
            System.out.print(nonTerminalSymbolMap.get(key).getName()+":");
            for (String str:firstList){
                System.out.print(" "+str+" ");
            }
            System.out.println();
       }*//*

 }
*/

}

