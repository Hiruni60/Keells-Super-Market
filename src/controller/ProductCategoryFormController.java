package controller;

import CrudUtil.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import model.ProductCategory;
import model.employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class ProductCategoryFormController {
    public Pane ProductCategoryPane;
    public TextField txtCatId;
    public TextField txtCatDes;
    public TextField txtCatName;
    public TableView<ProductCategory> tblCategory;
    public TableColumn colCatId;
    public TableColumn colCatName;
    public TableColumn colCatDes;
    public Button btnAdd;

    public void initialize() throws SQLException, ClassNotFoundException {
        colCatId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCatDes.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCatName.setCellValueFactory(new PropertyValueFactory<>("name"));

        loadAllCategories();

    }
    public void CatAddOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       if( CrudUtil.execute("INSERT INTO Product_Category VALUES(?,?,?)",
                txtCatId.getText(),
                txtCatName.getText(),
                txtCatDes.getText()
                )){
           new Alert(Alert.AlertType.CONFIRMATION, "SAVE").show();
       }else {
           new Alert(Alert.AlertType.WARNING, "Empty Result!").show();
       }
        loadAllCategories();

        ClearOnAction();
    }

    private void ClearOnAction() {
        txtCatId.clear();
        txtCatName.clear();
        txtCatDes.clear();

    }

    public void loadAllCategories() throws SQLException, ClassNotFoundException {
        ObservableList<ProductCategory> cats = FXCollections.observableArrayList();
        ResultSet categories = CrudUtil.execute("SELECT * FROM Product_Category");
        while(categories.next()){
            cats.add(
                    new ProductCategory(
                      categories.getString(1),
                      categories.getString(2),
                      categories.getString(3)
                    )
            );
        }
        tblCategory.setItems(cats);
    }

    public void EditOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ProductCategory eu=new ProductCategory(
                txtCatId.getText(),
                txtCatName.getText(),
                txtCatDes.getText()

        );
        boolean isProCatUpdated = CrudUtil.execute("UPDATE Product_Category SET cat_name=?, description=? WHERE cat_id=?",
                eu.getName(),
                eu.getDescription(),
                eu.getId());
        if (isProCatUpdated){
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

        loadAllCategories();
        ClearOnAction();

    }

    public void DeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ProductCategory cate = (ProductCategory) tblCategory.getSelectionModel().getSelectedItem();
        tblCategory.getItems().remove(cate);

       boolean isCatDeleted= CrudUtil.execute("DELETE FROM Product_Category WHERE cat_id=? ",
                cate.getId()

        );
        if (isCatDeleted){
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }

    public void catIdOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Product_Category WHERE cat_id=? ", txtCatId.getText());
            if (result.next()) {
                txtCatName.setText(result.getString(2));
                txtCatDes.setText(result.getString(3));
                new Alert(Alert.AlertType.CONFIRMATION, "SAVE").show();

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty set").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void ProCatRefreshOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        loadAllCategories();
    }

    private Object validate() {
        Pattern idPartern = Pattern.compile("^(C00-)[0-9]{3,5}$");
        Pattern namePartern = Pattern.compile("^[A-Z][A-z]{3,20}$");
       // Pattern desPatern= Pattern.compile("^[A-Z][A-z]{3,45}$");

        if(!idPartern.matcher(txtCatId.getText()).matches()) {
            addError(txtCatId);
            return txtCatId;
        } else {
            removeError(txtCatId);
            if(!namePartern.matcher(txtCatName.getText()).matches()){
                addError(txtCatName);
                return txtCatName;
            }
            else{
                removeError(txtCatName);
               /* if(!desPatern.matcher(txtCatDes.getText()).matches()){
                    addError(txtCatDes);
                    return  txtCatDes;
                }
                else{
                    removeError(txtCatDes);
                }*/
            }
        }

        return true;
    }
    private void addError(TextField txtField) {
        if (txtField.getText().length() > 0) {
            txtField.setStyle("-fx-border-color: red");

        }
        btnAdd.setDisable(true);
    }

    private void removeError(TextField txtField) {
        txtField.setStyle("-fx-border-color: green");

        btnAdd.setDisable(false);

    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        validate();
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response = validate();
            //if the response is a text field
            //that means there is a error
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();// if there is a error just focus it
            } else if (response instanceof Boolean) {

            }

        }
    }


    /*public void ClearOnAction(ActionEvent actionEvent) {
        txtCatId.clear();
        txtCatName.clear();
        txtCatDes.clear();
    }*/
}
