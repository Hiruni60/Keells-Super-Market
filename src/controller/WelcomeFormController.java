package controller;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.naming.ldap.PagedResultsControl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeFormController {

    public ProgressBar MyProgress;

    public Label Percentage;
    public AnchorPane keelsContext;

    //double progress;

    public void initialize() throws IOException {
        proces();



    }

    private void proces() throws IOException {




        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 100);
                    Thread.sleep(50);
                }
                return null;
            }
        };

        MyProgress.progressProperty().bind(task.progressProperty());

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

        MyProgress.progressProperty().addListener((observable, oldValue, newValue) ->{
            if(oldValue!=newValue){
                int presentage = (int)Math.round((Double)newValue*100);
                Percentage.setText(presentage+"%");

                //set Your task for this
                //if(persentage==100)lblComplete.setText("Complete");
            if(presentage==100){
                try {
                    setRUI("MainForm");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }

        });



    }

   private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  keelsContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.setTitle("Welcome Form");
    }

}




