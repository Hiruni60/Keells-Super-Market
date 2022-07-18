package controller;

import CrudUtil.CrudUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CashierLoginFormController {
    public AnchorPane ManagerPane;
    public Button btnLogin;
    public TextField txtUsername;
    public TextField txtPassword;
    public AnchorPane CashierPane;
    public ImageView btnBack;

    public void loginOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        String name=txtUsername.getText();
        String pw=txtPassword.getText();
        HashMap<String,String> usrPwd = new HashMap<>();

        ResultSet rs = CrudUtil.execute("SELECT name,password,status FROM employee");
        while(rs.next()){
            if(rs.getString(3).equals("Cashier")){
                usrPwd.put(rs.getString(1),rs.getString(2));
            }

        }

        for (String usrname: usrPwd.keySet()) {
            if(txtUsername.getText().equals(usrname)){
                System.out.println("matching username found");
                for (String pwd: usrPwd.values()) {
                    if(txtPassword.getText().equals(pwd)){
                        System.out.println("matching pwd found");
                        setRUI("BillingForm");
                        return;
                    }else {
                        (new Alert(Alert.AlertType.WARNING, "Your UserName or Password Incorrect", new ButtonType[]{ButtonType.CLOSE})).show();
                        txtUsername.clear();
                        txtPassword.clear();
                    }
                }
            }else {
                (new Alert(Alert.AlertType.WARNING, "Your UserName or Password Incorrect", new ButtonType[]{ButtonType.CLOSE})).show();
                txtUsername.clear();
                txtPassword.clear();
            }
        }
//        if (name.equalsIgnoreCase("user") && pw.equalsIgnoreCase("1234")) {
//
//            setRUI("BillingForm");
//
//        }else {
//            new Alert(Alert.AlertType.WARNING, "You are non authorized person do not tri to access this software ! ").show();
//        }
    }


    public void BackOnMousePressed(MouseEvent mouseEvent) throws IOException {
        setRUI("MainForm");
    }

    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  CashierPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.setTitle("");
    }
}
