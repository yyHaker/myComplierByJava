package com.yyHaker.syntax.Analyzer;

import com.yyHaker.lexical.model.TokenString;
import com.yyHaker.lexical.scanner.AnalyzerByFA;
import com.yyHaker.lexical.view.LexicalController;
import com.yyHaker.syntax.model.GrammarProduction;
import com.yyHaker.syntax.model.Token;
import com.yyHaker.syntax.utis.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * GrammaticalAnalysis
 *语法分析类
 * @author Le Yuan
 * @date 2016/10/27
 */
public class GrammaticalAnalysis {
     //词法单元集合
    public static List<Token> tokenValueList=new ArrayList<Token>();

    //存储first集合
    public   static  List<String> allFirstList=new ArrayList<>();

    public static  List<String>  allFollowList=new ArrayList<>();

    public static  List<String>  selectList=new ArrayList<>();

    public static  StringBuffer  syntaxTreeBuffer;

    public static boolean isCaculate=false;

    /**
     * 完成预测分析法的准备工作，构造非终结符的FIRST集、FOLLOW集、SELECT集
     */
    public static void preparationWork() {
        //从文件读入文法(控制只执行一次)
         if (!LexicalController.isLoadGrammar) {
             //读入文法
             MyHelper.singleGrammarProductionList=FileUtils.getSingleGrammarProdctionFromFile(new File("grammar2.txt"));
             System.out.println("文法加载成功");
          }

            /*  if (MyHelper.singleGrammarProductionList.size()==0){
                  //读入文法
                  MyHelper.singleGrammarProductionList=FileUtils.getSingleGrammarProdctionFromFile(new File("grammar4.txt"));
                  System.out.println("文法加载成功");
              }else {
                  System.out.println("文法已经加载，不能重复加载");
              }*/
            //计算文法的first集合、follow集合、select集合、预测分析表只执行一次
          if (!isCaculate){
              //获得非终结符集
              MyHelper.getNonTerminalSymbolFromGrammar();
              //获得终结符集
              MyHelper.getTerminalSymbolFromGrammar();
              //求非终结符的first集合、follow集合，终结符的first集合、以及每个产生式的select集合
              getFirstList();
              getFollowList();
              getSelectList();
              getHigherNTS();
              getSYNList();
              getAnalysisTable();
              //测试自动导入测试用例
              //getToken();
              if (!MyHelper.isLL1()){
                  System.out.println("该文法不是LL(1)型文法,不能够被识别");
                  return;
              }
              isCaculate=true;
          }

         //测是分析的句子是
         System.out.println("测试分析的句子是:");
        System.out.println(MyHelper.ListToString(getSentence(AnalyzerByFA.tokenStringList)));

        //初始化
         syntaxTreeBuffer=new StringBuffer();

        //测试分析
        System.out.println("语法分析结果：");
        if (AnalyzerByFA.tokenStringList.size()>0){
            MyHelper.myParser(AnalyzerByFA.tokenStringList);
        }else {
            System.out.println("词法分析tokenString为空");
        }

    }

    /**
     * 获取终结符、非终结符的First集
     */
    public static void getFirstList() {
        //终结符的first集合即为它本身
        System.out.println();
        System.out.println("-----------------终结符的FIRST集:---------------------");
        for (String key : MyHelper.terminalSymbolMap.keySet()) {
            MyHelper.terminalSymbolMap.get(key).getFirstList().add(key);
            List<String> mFirst = MyHelper.terminalSymbolMap.get(key).getFirstList();
            System.out.println("FIRST(" + key + ") = " + mFirst);

            allFirstList.add("FIRST(" + key + ") = " + mFirst);
        }
        System.out.println();

        //非终结符的first集合由myHelper中的方法计算得到
        System.out.println("非终结符的FIRST集：");
        for (String key : MyHelper.nonTerminalSymbolMap.keySet()) {
            List<String> mFirstList = MyHelper.getNTSFirstList(MyHelper.nonTerminalSymbolMap.get(key));
            MyHelper.nonTerminalSymbolMap.get(key).setFirstList(mFirstList);
            System.out.println("FIRST(" + key + ") = " + mFirstList);

            allFirstList.add("FIRST(" + key + ") = " + mFirstList);
        }
        System.out.println();
    }

    /**
     * 获取非终结符的Follow集
     */
    public static void getFollowList() {
        System.out.println("非终结符的FOLLOW集：");
        //给文法的开始符号添加#
        MyHelper.nonTerminalSymbolMap.get(MyHelper.singleGrammarProductionList.get(0).getLeftPart()).getFollowList().add("#");

        MyHelper.whetherChanged = true;
        while (MyHelper.whetherChanged) {
            MyHelper.whetherChanged = false;
            MyHelper.getNTSFollowList();
        }

        for (String key : MyHelper.nonTerminalSymbolMap.keySet()) {
            List<String> mFollow = MyHelper.nonTerminalSymbolMap.get(key).getFollowList();
            System.out.println("FOLLOW(" + key + ") = " + mFollow);

            allFollowList.add("FOLLOW(" + key + ") = " + mFollow);
        }
        System.out.println();
    }

    /**
     * 获取非终结符Select集
     */
    public static void getSelectList() {
        System.out.println("文法表达式的SELECT集：");
        for (int i = 0; i < MyHelper.singleGrammarProductionList.size(); i++) {
            GrammarProduction grammar = MyHelper.singleGrammarProductionList.get(i);
            List<String> mSelect = MyHelper.getSelectList(grammar);

            //加到GrammarProduction的域中去
            if (mSelect != null) {
                for (String str : mSelect) {
                    grammar.getSelectList().add(str);
                }
            }
            System.out.println("SELECT(" + grammar.getProduction() + ") = " + grammar.getSelectList());

            selectList.add("SELECT(" + grammar.getProduction() + ") = " + grammar.getSelectList());
        }
        System.out.println();
    }

    /**
     * 获取高层结构的集合
     */
    public static void getHigherNTS(){
        MyHelper.whetherChanged = true;
        while (MyHelper.whetherChanged) {
            MyHelper.whetherChanged = false;
            MyHelper.calcHigherNTSList();
        }

    /*    System.out.println("---------------每个非终结符的更高的层次结构集合");
        for(String aString : MyHelper.nonTerminalSymbolMap.keySet()){
            List<NonTerminalSymbol> list = MyHelper.nonTerminalSymbolMap.get(aString).getHigherNTS();
            System.out.print(aString+":\t\t");
            for(int i = 0;i < list.size();i++)
                System.out.print(list.get(i).getName()+"\t");
            System.out.println();
        }*/
    }

    /**
     *  计算每个非终结符的同步记号集
*/
    public  static void getSYNList(){
        for (String keyString : MyHelper.nonTerminalSymbolMap.keySet()) {
            MyHelper.getSynchList(MyHelper.nonTerminalSymbolMap.get(keyString));
        }
    }

    /**
     * 得到预测分析表
     */
    public static void getAnalysisTable() {
        System.out.println("预测分析表M");
        MyHelper.getAnalysisTable();
        for (String nts : MyHelper.forecastAnalysisTable.keySet()) {
            System.out.print(nts + "\t");
            for (String ts : MyHelper.forecastAnalysisTable.get(nts).keySet()) {
                System.out.print(ts + "->" + MyHelper.ListToString(MyHelper.forecastAnalysisTable.get(nts).get(ts)) + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * 获取token串，并进行相应的处理
     */
    public static void getToken() {


       /* FileUtils.getTokenFromFile(new File("token.txt"));
        for (int i = 0; i < FileUtils.tokenList.size(); i++) {
            String[] splitString = FileUtils.tokenList.get(i).split("@");
            Token token = new Token();
            if (splitString.length == 2) {
                token.setName(splitString[0]);
                token.setValue(splitString[1]);
                tokenValueList.add(token);
            }
        }*/



    }

    /**
     * 由tokenStringList序列构造待分析的句子
     */
    public static List<String> getSentence(List<TokenString> tokenStrings) {
        List<String> sentence = new ArrayList<String>();
        for (int i = 0; i < tokenStrings.size(); i++) {
            sentence.add(tokenStrings.get(i).getName());
        }
        sentence.add("#");
        return sentence;
    }



    public static void main(String []args){
        preparationWork();
        //getToken();
        //System.out.println("待分析的句子：" + MyHelper.ListToString(getSentence()) + "\n");
        //System.out.println("语法分析结果：");
        //MyHelper.myParser(getSentence());
    }
}
