import java.net.URL;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 *
 */
public class JavaFxBrowser extends Application {

    private Scene scene;
    MyBrowser myBrowser;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Testing JavaFX and JavaScript");

        myBrowser = new MyBrowser();
        scene = new Scene(myBrowser, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    class User {

        private String name;

        public User(){}

        public void setName(String name){
            this.name = name;
        }
    }

    class MyBrowser extends Region{

        HBox toolbar;

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        String str = "My str";

        public MyBrowser(){

            final URL urlHello = getClass().getResource("hello.html");
            webEngine.load(urlHello.toExternalForm());

            final TextField textField = new TextField ();
            textField.setPromptText("Hello! Who are?");

            Button buttonEnter = new Button("Enter");
            buttonEnter.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent arg0) {
                    User usr = new User();
                    usr.setName(textField.getText());
                    Gson gson = new Gson();
                    webEngine.executeScript( " updateHello(' " + gson.toJson(usr) + " ') " );
                }
            });

            Button buttonClear = new Button("Clear");
            buttonClear.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent arg0) {
                    webEngine.executeScript( "clearHello()" );
                }
            });


            toolbar = new HBox();
            toolbar.setPadding(new Insets(10, 10, 10, 10));
            toolbar.setSpacing(10);
            toolbar.setStyle("-fx-background-color: #336699");
            toolbar.getChildren().addAll(textField, buttonEnter, buttonClear);



            getChildren().add(toolbar);
            getChildren().add(webView);
        }

        @Override
        protected void layoutChildren(){
            double w = getWidth();
            double h = getHeight();
            double toolbarHeight = toolbar.prefHeight(w);
            layoutInArea(webView, 0, 0, w, h-toolbarHeight, 0, HPos.CENTER, VPos.CENTER);
            layoutInArea(toolbar, 0, h-toolbarHeight, w, toolbarHeight, 0, HPos.CENTER, VPos.CENTER);
        }

    }

}