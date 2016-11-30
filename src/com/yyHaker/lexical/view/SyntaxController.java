package com.yyHaker.lexical.view;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;
import com.yyHaker.lexical.view.MainApp;
import com.yyHaker.syntax.Analyzer.GrammaticalAnalysis;
import com.yyHaker.syntax.Analyzer.MyHelper;
import com.yyHaker.syntax.model.GrammarProduction;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.ArrayList;

/**
 * SyntaxController
 *the controller of the Syntax view
 * @author Le Yuan
 * @date 2016/10/28
 */
public class SyntaxController {
    @FXML private   TableView<String[]>  analysisTable;
    @FXML private   TableView<String[]> grammarTable;
    @FXML private  TableView<String[]> firstListTable;
    @FXML private  TableView<String[]> followListTable;
    @FXML private  TableView<String[]> selectListTable;



    /**
     * 将数据添加到指定的表格中
     * @param data
     * @param columnNames
     * @param tableView
     */

   public void setArrayDataToTable(ObservableList<String[]> data, java.util.List<String> columnNames,TableView<String[]> tableView){
       int columnSize=columnNames.size();
         //tableView.getItems().clear();
       for (final String columnName:columnNames){
           //根据名字创建一个列
           TableColumn<String[],String> column=new TableColumn<>(columnName);
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String[], String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<String[], String> param) {
                     int index=columnNames.indexOf(columnName);
                    String value=param.getValue()[index];
                    return new ReadOnlyStringWrapper(value);
                }
            });
            if (columnSize==1){
                column.setPrefWidth(tableView.getPrefWidth());
            }
           tableView.getColumns().add(column);
       }

       tableView.setItems(data);
   }


    /**
     * 设置文法数据到table
     */
    public void setGrammarProductionToTable(){
        //获得observerList
        java.util.List<String[]> data=new ArrayList<String[]>();

        if (MyHelper.singleGrammarProductionList.size()>0){
            for (GrammarProduction production:MyHelper.singleGrammarProductionList){
                String []str=new String[1];
                 str[0]=production.getProduction();
                data.add(str);
            }
        }
        //获得列的名称
        java.util.List<String> columnNames=new ArrayList<String>();
        columnNames.add("所使用的文法");
        setArrayDataToTable(FXCollections.observableList(data),columnNames,grammarTable);

    }

    /**
     * 将预测分析表的数据显示到table
     */
    public void setAnalysisInforToTable(){
         int rowSize=100;
         int columnSize=100;
         java.util.List<String> columnNames=new ArrayList<String>();
          columnNames.add("非终结符");
          for (String columnName:MyHelper.terminalSymbolMap.keySet()){
                  columnNames.add(columnName);
          }

        java.util.List<String[]> data=new ArrayList<>();

        for (String nts:MyHelper.forecastAnalysisTable.keySet()){
              String []productionArray=new String[columnSize];
              int i=0;
               productionArray[0]=nts;
               for (String inputChar:MyHelper.terminalSymbolMap.keySet()){
                   i++;
                   if (MyHelper.forecastAnalysisTable.get(nts).get(inputChar)!=null){
                        String str=MyHelper.ListToString(MyHelper.forecastAnalysisTable.get(nts).get(inputChar));
                        if (str.trim().equalsIgnoreCase("synch")){
                            productionArray[i]=str;
                        }else{
                            String production=nts+"->"+str;
                            productionArray[i]=production;
                        }
                   }else{
                       productionArray[i]="";
                   }

               }
             data.add(productionArray);
        }

        setArrayDataToTable(FXCollections.observableList(data),columnNames,analysisTable);
    }

    /**
     * 将first集合，follow集合，select集合显示到表格中
     */
    public void setFirstListToTable(){
          // GrammaticalAnalysis.allFirstList
        //获得observerList
        java.util.List<String[]> data=new ArrayList<String[]>();

        if (GrammaticalAnalysis.allFirstList.size()>0){
            for (String first:GrammaticalAnalysis.allFirstList){
                String []str=new String[1];
                str[0]=first;
                data.add(str);
            }
        }
        //获得列的名称
        java.util.List<String> columnNames=new ArrayList<String>();
        columnNames.add("终结符和非终结符的first集合");
        setArrayDataToTable(FXCollections.observableList(data),columnNames,firstListTable);

    }

    public void setFollowListToTable(){
        // GrammaticalAnalysis.allFirstList
        //获得observerList
        java.util.List<String[]> data=new ArrayList<String[]>();
        //获得数据list
        java.util.List<String> myFollowList=new ArrayList<String>();
        if (GrammaticalAnalysis.allFollowList.size()>0){
            for (String follow:GrammaticalAnalysis.allFollowList){
                String []str=new String[1];
                str[0]=follow;
                data.add(str);
            }
        }
        //获得列的名称
        java.util.List<String> columnNames=new ArrayList<String>();
        columnNames.add("非终结符的follow集合");
        setArrayDataToTable(FXCollections.observableList(data),columnNames,followListTable);
    }

    public void setSelectListToTable(){
        // GrammaticalAnalysis.allFirstList
        //获得observerList
        java.util.List<String[]> data=new ArrayList<String[]>();

        if (GrammaticalAnalysis.selectList.size()>0){
            for (String select:GrammaticalAnalysis.selectList){
                String []str=new String[1];
                str[0]=select;
                data.add(str);
            }
        }
        //获得列的名称
        java.util.List<String> columnNames=new ArrayList<String>();
        columnNames.add("select集合");
        setArrayDataToTable(FXCollections.observableList(data),columnNames,selectListTable);
    }


    /**
     * 准备必须的语法分析数据
     */
    public void  prepareWork(){
        GrammaticalAnalysis.preparationWork();
    }

    /**
     *MainApp主程序调用，设置所有数据
     */
    public void setAllAnalysisInfor(){
        //prepareWork();

         setGrammarProductionToTable();
         setAnalysisInforToTable();
        setFirstListToTable();
        setFollowListToTable();
        setSelectListToTable();
    }

}
