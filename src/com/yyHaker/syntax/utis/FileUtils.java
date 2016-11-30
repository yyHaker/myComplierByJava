package com.yyHaker.syntax.utis;


import com.yyHaker.syntax.model.GrammarProduction;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUtils
 *文件操作类
 * @author Le Yuan
 * @date 2016/10/20
 */
public class FileUtils {
     public static List<String> grammarLineList =new ArrayList<String>();
     public static List<String> tokenList=new ArrayList<String>();
    /**
     * 从文件中获得grammar,一行一行的存入grammarLineList中
     * @param file
     */
     public  static  void getGrammarFromFile(File file){
         try {
             BufferedReader br=new BufferedReader(new FileReader(file));
             String grammarLine=br.readLine();
             while (grammarLine!=null){
                 if (grammarLine.equals("\n"))
                     continue;
                 grammarLineList.add(grammarLine);
                 grammarLine=br.readLine();
             }
              br.close();
          } catch (FileNotFoundException e){
             System.out.println("文法文件找不到");
              e.printStackTrace();
         } catch (IOException e){
             e.printStackTrace();
         }
     }

     public static void getTokenFromFile(File file){
         try {
              BufferedReader br=new BufferedReader(new FileReader(file));
             String token=br.readLine();
             while (token!=null){
                 tokenList.add(token);
                 token=br.readLine();
             }
             br.close();
         }catch (FileNotFoundException e){
             System.out.println("词法文件找不到");
             e.printStackTrace();
         }catch (IOException e){
             e.printStackTrace();
         }
     }


    /**
     * 从指定的文法文件中获得文法产生式的集合
     * @param file
     * @return
     */
     public static  List<GrammarProduction>   getSingleGrammarProdctionFromFile(File file) {
           List<GrammarProduction> grammarProductionList=new ArrayList<>();
         try {
             BufferedReader br = new BufferedReader(new FileReader(file));
             String grammarLine = br.readLine();
             while (grammarLine != null) {
                 if (grammarLine.equals("\n"))
                     continue;
                 grammarProductionList.add(new GrammarProduction(grammarLine));
                 grammarLine = br.readLine();
             }
             br.close();
             return  grammarProductionList;
         } catch (FileNotFoundException e) {
             System.out.println("文法文件找不到");
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         }

         return null;
     }
}
