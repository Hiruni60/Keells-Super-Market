package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFormController {
    public AnchorPane WelcomePane;
    public Button btnManager;
    public Button btnCashier;

    public void ManagerOnAction(ActionEvent actionEvent) throws IOException {

    setRUI("ManagerLoginForm");

    }

    public void CashierOnAction(ActionEvent actionEvent) throws IOException {

        setRUI("CashierLoginForm");

    }

    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  WelcomePane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.setTitle("Main Form");
    }
}
