package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerLoginFormController {
    public AnchorPane ManagerPane;
    public Button btnLogin;
    public TextField txtUsername;
    public TextField txtPassword;
    public ImageView btnBack;
    Stage stage;

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        if (this.txtUsername.getText().equals("Admin") && this.txtPassword.getText().equals("1234")) {

            setRUI("DashboardForm");
        }else{

            (new Alert(Alert.AlertType.WARNING, "Your UserName or Password Incorrect", new ButtonType[]{ButtonType.CLOSE})).show();
            txtUsername.clear();
            txtPassword.clear();
        }

    }

    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  ManagerPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.setTitle("Welcome Form");
    }


    public void BackOnMousePressed(MouseEvent mouseEvent) throws IOException {
        setRUI("MainForm");
    }
}
