package sample;

import com.sun.javafx.scene.control.skin.LabeledText;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserCore;
import com.teamdev.jxbrowser.chromium.BrowserType;
import com.teamdev.jxbrowser.chromium.internal.Environment;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    Path basePath=null;
    private Desktop desktop = Desktop.getDesktop();
    private MenuBar menuBar = new MenuBar();
    private Controller controller=new Controller();
//    final VBox vb = new VBox();

    @Override
    public void init() throws Exception {
        // On Mac OS X Chromium engine must be initialized in non-UI thread.
        if (Environment.isMac()) {
            BrowserCore.initialize();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        GridPane gridPane=new GridPane();



        Browser browser = new Browser(BrowserType.LIGHTWEIGHT);
        BrowserView view = new BrowserView(browser);
        BorderPane borderPane=new BorderPane(view);


        Scene scene = new Scene(borderPane, 700, 500);
//        Scene scene = new Scene(new VBox( (view)), 700, 500);
       final FileChooser fileChooser = new FileChooser();


            Menu menu=new Menu("file");
            menuBar.getMenus().addAll(menu);
            borderPane.setCenter(view);
            borderPane.setTop(menuBar);
        final Button openButton = new Button("Open a epub...");
        borderPane.setBottom(openButton);
      final ListView<Object> list = new ListView<>();


            borderPane.setLeft(list);
                openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
if (list.getItems().size()>0){

    list.getItems().remove(0,list.getItems().size()-1);

}
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
//                            openFile(file);
                            try {
                                Path dirpath= controller.unzip(new FileInputStream(file));

                                Path opf = controller.getOpf(dirpath);
                                basePath=opf.getParent();
                                java.util.List<String> fileList = controller.listFiles(new File(opf.toString()));


                                for (String listItem:fileList) {


                                    list.getItems().add(listItem);
                                }


                                 } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }
                });

//        fileChooser.setTitle("Open Resource File");
//        fileChooser.showOpenDialog(primaryStage);





        primaryStage.setScene(scene);
        primaryStage.show();

      browser.loadURL("http://www.google.com");

        list.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String filename=((LabeledText) event.getPickResult().getIntersectedNode()).getText();
                browser.loadURL("file://"+basePath.toString()+"/"+filename);

            }
        });
    }
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                    Main.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
