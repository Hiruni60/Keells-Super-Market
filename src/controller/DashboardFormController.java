package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class DashboardFormController {
    public AnchorPane DashboardPane;
    public Label lblManageProduct;
    public Button btnManageProduct;
    public StackPane ImagePane;
    public Button btnProductCategory;
    public Button btnEmployeeDetails;
    public Button btnIncomeReport;
    public ImageView btnBack;


    /*private void setRUI(String location) throws IOException {
            DashboardPane.setVisible(true);
            Stage stage=(Stage)ImagePane.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
            stage.setTitle("Welcome Form");
        }*/

    public void ManageProductOnAction(ActionEvent actionEvent) throws IOException {
        //setRUI("ManageProductForm");
        ImagePane.getChildren().clear();
        ImagePane.getChildren().add(FXMLLoader.load(getClass().getResource("../view/ManageProductForm.fxml")));
    }

    public void ProductCategoryOnAction(ActionEvent actionEvent) throws IOException {
        ImagePane.getChildren().clear();
        ImagePane.getChildren().add(FXMLLoader.load(getClass().getResource("../view/ProductCategoryForm.fxml")));
    }

    public void EmployeeDetailsOnAction(ActionEvent actionEvent) throws IOException {
        ImagePane.getChildren().clear();
        ImagePane.getChildren().add(FXMLLoader.load(getClass().getResource("../view/EmployeeNew.fxml")));
    }

    public void IncomeReportOnAction(ActionEvent actionEvent) throws IOException {
        ImagePane.getChildren().clear();
        ImagePane.getChildren().add(FXMLLoader.load(getClass().getResource("../view/IncomeReportForm.fxml")));
    }

    public void LogoutOnAction(ActionEvent actionEvent) {

        Stage management = (Stage) DashboardPane.getScene().getWindow();
        management.close();
        //management.setScene(new Scene(FXMLLoader.load(getClass().getResource("../Main/Dashboard.fxml"))));
       // management.setResizable(false);
      //  management.show();
    }

    public void BackOnMousePressed(MouseEvent mouseEvent) throws IOException {
        setRUI("MainForm");
    }
    private void setRUI(String location) throws IOException {
        Stage stage=(Stage)  DashboardPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        //stage.setTitle("Welcome Form");
    }
}
