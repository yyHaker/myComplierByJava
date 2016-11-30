package com.yyHaker.lexical.view;

import com.yyHaker.lexical.scanner.AnalyzerByFA;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * the main  view  class of the project
 *
 * @author yyHaker
 * @create 2016-10-04-22:01
 */
public class MainApp  extends Application{
     private AnchorPane lexicalRootLayout;
     private  Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage=primaryStage;

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("lexical.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("lexical.fxml"));
        lexicalRootLayout=(AnchorPane) loader.load();

        this.primaryStage.setTitle("My Lexical");
        this.primaryStage.setScene(new Scene(lexicalRootLayout));
        //set icons
        this.primaryStage.getIcons().add(new Image("file:resources/images/lexical.png"));

        // Give the controller access to the main app.
        LexicalController controller=loader  .getController();
        controller.setMainApp(this);
       // System.out.println(controller.getMainApp());

        AnalyzerByFA analyzerByFA=new AnalyzerByFA();
        controller.setAnalyzerByFA(analyzerByFA);

        primaryStage.show();
    }

    /**
     * 便于其他类可以通过该方法访问primaryStage
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Sets the file path of the currently loaded file. The path is persisted in
     * the OS specific registry.
     *
     * @param file the file or null to remove the path
     */
    public void setTextFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());

            // Update the stage title.
            primaryStage.setTitle("MyLexical - " + file.getName());
        } else {
            prefs.remove("filePath");

            // Update the stage title.
            primaryStage.setTitle("MyLexical");
        }
    }

    /**
     * Returns the person file preference, i.e. the file that was last opened.
     * The preference is read from the OS specific registry. If no such
     * preference can be found, null is returned.
     *
     * @return
     */
    public File getTextFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }


    public void showSyntaxAnalysis(){
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Grammar.fxml"));

            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Syntax Analysis");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the application icon.
            dialogStage.getIcons().add(new Image("file:resources/images/lexical.png"));

            // Set  the controller.（加载必要的数据）
             GrammarController grammarController=loader.getController();
              grammarController.prepareWork();

            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void showSyntaxInformation(){
        try {
            // Load the fxml file and create a new stage for the popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("Syntax.fxml"));

            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Syntax Analysis");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the application icon.
            dialogStage.getIcons().add(new Image("file:resources/images/lexical.png"));

            // Set  the controller.（加载必要的数据）
            SyntaxController syntaxController=loader.getController();
            syntaxController.setAllAnalysisInfor();

            dialogStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }





    public static void main(String[] args) {
        launch(args);
    }
}
