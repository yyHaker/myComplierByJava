package com.yyHaker.lexical.view;

import com.yyHaker.lexical.model.Error;
import com.yyHaker.lexical.property.ErrorProperty;
import com.yyHaker.semantic.lexer.Lexer;
import com.yyHaker.semantic.lexer.Token;
import com.yyHaker.semantic.parser.Parser;
import com.yyHaker.semantic.properties.SymbolProperty;
import com.yyHaker.syntax.Analyzer.GrammaticalAnalysis;
import com.yyHaker.syntax.Analyzer.MyHelper;
import com.yyHaker.syntax.model.TreeNode;
import com.yyHaker.syntax.property.SyntaxErrorProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

/**
 * GrammarController
 *the controller of the grammar.fxml
 * @author Le Yuan
 * @date 2016/10/29
 */
public class GrammarController {
    //treeView
    @FXML private  TextArea syntaxArea;

    @FXML private TableView<SyntaxErrorProperty> syntaxErrorTable;

    @FXML private TableColumn<SyntaxErrorProperty,String> tokenError;
    @FXML private TableColumn<SyntaxErrorProperty,String> errorKind;
    @FXML private TableColumn<SyntaxErrorProperty,Integer> row;

    @FXML private   TreeView    syntaxTree;

    //符号表
     @FXML private TableView<SymbolProperty> symbolTable;

     @FXML private  TableColumn<SymbolProperty,String>   symbolTableColumn;
     @FXML private  TableColumn<SymbolProperty,String>   typeColumn;
     @FXML private  TableColumn<SymbolProperty,String>   offsetColumn;


      //三地址指令
     @FXML private  TextArea triplesArea;
     //四元式
     @FXML private  TextArea  fourElementArea;



    //菜单上的BeginSemantic事件，开始语义分析
     @FXML protected  void beginSemantic(){
         try {
             //清空错误列表
             com.yyHaker.semantic.inter.Node.semanticErrorList.clear();
             //清空符号表
             com.yyHaker.semantic.inter.Node.symbolPropertyList.clear();
             //清空三元式列表
             com.yyHaker.semantic.inter.Node.triplesString=new StringBuffer("");
             //清空四元式列表
             com.yyHaker.semantic.inter.Node.fourElementsString=new StringBuffer("");

             Parser.used=0;
             Lexer.line=1;
             com.yyHaker.semantic.inter.Node.lexline=0;
             com.yyHaker.semantic.inter.Node.labels=0;

             //开始分析
             Lexer lex = new Lexer();
             Parser parse = new Parser(lex);
             parse.program();
             System.out.write('\n');

             com.yyHaker.semantic.inter.Node.addCallFunc();

             triplesArea.clear();
             triplesArea.appendText(com.yyHaker.semantic.inter.Node.triplesString.toString());

             fourElementArea.clear();
             fourElementArea.appendText(com.yyHaker.semantic.inter.Node.fourElementsString.toString());

             //添加语义错误信息
             setSemanticErrorToTable();

             setDataToSymbolTable(com.yyHaker.semantic.inter.Node.symbolPropertyList);
         }catch (IOException e){
             e.printStackTrace();
         }

     }

     protected  void setDataToSymbolTable(List<SymbolProperty> symbolPropertyList){
         if(symbolPropertyList!=null){
             ObservableList<SymbolProperty> data= FXCollections.observableArrayList();
             symbolTableColumn.setCellValueFactory(new PropertyValueFactory<SymbolProperty,String>("symbol"));
             typeColumn.setCellValueFactory(new PropertyValueFactory<SymbolProperty,String>("type"));
             offsetColumn.setCellValueFactory(new PropertyValueFactory<SymbolProperty,String>("offset"));
             data.clear();
             for (SymbolProperty symbolProperty:symbolPropertyList){
                 data.add(symbolProperty);
             }
           symbolTable.setItems(data);
         }
     }

     protected  void setSemanticErrorToTable(){
         for (SyntaxErrorProperty syntaxErrorProperty: com.yyHaker.semantic.inter.Node.semanticErrorList){
                MyHelper.errorPropertyList.add(syntaxErrorProperty);
         }
         setSyntaxErrorListToTable(MyHelper.errorPropertyList);
     }



    protected void setSynTaxToSyntaxArea(String syntaxString){
        syntaxArea.clear();
         syntaxArea.setText(syntaxString);
    }




    /**
     * 将errorList显示到表格
     * @param errorList
     */
    public void setSyntaxErrorListToTable(List<SyntaxErrorProperty> errorList){
        if(errorList!=null){
            ObservableList<SyntaxErrorProperty> errorData= FXCollections.observableArrayList();
            row.setCellValueFactory(new PropertyValueFactory<SyntaxErrorProperty,Integer>("row"));
            errorKind.setCellValueFactory(new PropertyValueFactory<SyntaxErrorProperty,String>("errorKind"));
            tokenError.setCellValueFactory(new PropertyValueFactory<SyntaxErrorProperty,String>("tokenError"));

            errorData.clear();
            for (SyntaxErrorProperty syntaxErrorProperty:errorList){
                errorData.add(syntaxErrorProperty);
            }
            syntaxErrorTable.setItems(errorData);
        }
    }


    public void setDataToTreeView(TreeNode treeNode){
        TreeItem<String> root = new TreeItem<String>(treeNode.getName());
        root.setExpanded(true);

        setTreeNode(treeNode,root);
        //TreeView<String> treeView = new TreeView<String>(root);
        syntaxTree.setRoot(root);
    }


    /**
     * 递归添加树的数据
     * @param treeNode
     * @param rootString
     */
    public void setTreeNode(TreeNode treeNode,TreeItem<String> rootString){
           if (treeNode.getSubNodes().size()>0){
                 for (TreeNode tn:treeNode.getSubNodes()){
                         TreeItem<String> treeItem=new TreeItem<String>(tn.getName());
                         rootString.getChildren().add(treeItem);
                        setTreeNode(tn,treeItem);
                 }
           }
    }


    /**
     * 准备必须的语法分析数据
     */
    public void  prepareWork(){
        //语法分析
        GrammaticalAnalysis.preparationWork();
        setSynTaxToSyntaxArea(GrammaticalAnalysis.syntaxTreeBuffer.toString());
        setSyntaxErrorListToTable(MyHelper.errorPropertyList);
       setDataToTreeView(MyHelper.myTreeNode);
    }


}
