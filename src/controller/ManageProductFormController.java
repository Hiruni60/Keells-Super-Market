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
import model.Product;
import model.ProductCategory;
import model.employee;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.regex.Pattern;

public class ManageProductFormController {
    public TextField txtProName;
    public TextField txtProId;
    public TextField txtProQty;
    public TextField txtProPrice;
    public ComboBox txtProCategory;
    public TableView<Product> tblProduct;
    public TableColumn colProId;
    public TableColumn colProName;
    public TableColumn colProCategory;
    public TableColumn colProQty;
    public TableColumn colProPrice;
    public Pane ManageProductPane;
    public Button btnAdd;

    public void initialize() throws SQLException, ClassNotFoundException {

        colProId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colProPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProQty.setCellValueFactory(new PropertyValueFactory<>("qty"));

        refreshCombos();

        loadAllProducts();

    }

    private  void  refreshCombos() throws SQLException, ClassNotFoundException {
        ResultSet ct = CrudUtil.execute("SELECT * FROM Product_Category");
        ObservableList<String> stLst = FXCollections.observableArrayList();
        while(ct.next()){
            stLst.add(ct.getString(2));
        }
        txtProCategory.setItems(stLst);
    }
    public void ProAddOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT cat_id FROM product_category WHERE cat_name=?",txtProCategory.getValue().toString());
        String id = "";
        if(rs.next()){
            id=rs.getString(1);
        }
        if(CrudUtil.execute("INSERT INTO Product VALUES(?,?,?,?,?)",
                txtProId.getText(),
                txtProName.getText(),
                id,
                Double.parseDouble(txtProPrice.getText()),
                Integer.parseInt(txtProQty.getText())
                )) {
            new Alert(Alert.AlertType.CONFIRMATION, "SAVE").show();


        }else {
            new Alert(Alert.AlertType.WARNING, "Empty Result!").show();
        }
        loadAllProducts();
        ClearOnAction();
    }

    private void ClearOnAction() {
        txtProId.clear();
        txtProName.clear();
        txtProCategory.setValue(null);
        txtProPrice.clear();
        txtProQty.clear();
    }

    public void loadAllProducts() throws SQLException, ClassNotFoundException {


       ResultSet pr = CrudUtil.execute("SELECT * FROM Product");
        ObservableList<Product> products = FXCollections.observableArrayList();
        while(pr.next()){
            String catName = "";
            ResultSet rs = CrudUtil.execute("SELECT cat_name FROM product_category WHERE cat_id=?",pr.getString(3));
            if(rs.next()){
                catName=rs.getString(1);
            }
            products.add(
                    new Product(
                            pr.getString(1),
                            pr.getString(2),
                            catName,
                            pr.getDouble(4),
                            pr.getInt(5)
                    )
            );
        }
        tblProduct.setItems(products);
    }

    public void EditOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("SELECT cat_id FROM product_category WHERE cat_name=?",txtProCategory.getValue().toString());
        String id = "";
        if(rs.next()){
            id=rs.getString(1);
        }
        Product eu=new Product(
                txtProId.getText(),
                txtProName.getText(),
                id,
                Double.parseDouble(txtProPrice.getText()),
                Integer.parseInt(txtProQty.getText())
        );
        boolean isProUpdated = CrudUtil.execute("UPDATE Product SET name=?, category=?, price=?, qty=? WHERE pro_id=?",
                eu.getName(),
                eu.getCategory(),
                eu.getQty(),
                eu.getPrice(),
                eu.getId());
        if (isProUpdated){
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

        loadAllProducts();
        ClearOnAction();
    }

    public void DeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Product pro = (Product) tblProduct.getSelectionModel().getSelectedItem();
        tblProduct.getItems().remove(pro);

        boolean isProDeleted=CrudUtil.execute("DELETE FROM Product WHERE pro_id=? ",
                pro.getId()

        );
        if (isProDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

    }

    public void proIdOnAction(ActionEvent actionEvent)  {

        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM Product WHERE pro_id=? ", txtProId.getText());
            if (result.next()) {
                txtProName.setText(result.getString(2));
                txtProCategory.setValue(result.getString(3));
                txtProPrice.setText(result.getString(4));
                txtProQty.setText(result.getString(5));


            } else {
                new Alert(Alert.AlertType.WARNING, "Empty set").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void ProRefreshOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        loadAllProducts();
    }
    private Object validate() {
        Pattern idPartern = Pattern.compile("^(P00-)[0-9]{3,5}$");
        Pattern namePartern = Pattern.compile("^[A-Z][A-z]{3,20}$");
        Pattern qtyPatern= Pattern.compile("^[0-9 ]{3,20}$");

        if(!idPartern.matcher(txtProId.getText()).matches()) {
            addError(txtProId);
            return txtProId;
        } else {
            removeError(txtProId);
            if(!namePartern.matcher(txtProName.getText()).matches()){
                addError(txtProName);
                return txtProName;
            }
            else{
                removeError(txtProName);
                if(!qtyPatern.matcher(txtProQty.getText()).matches()){
                    addError(txtProQty);
                    return  txtProQty;
                }
                else{
                    removeError(txtProQty);
                }
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
        txtProId.clear();
        txtProName.clear();
       // txtProCategory.
        txtProPrice.clear();
        txtProQty.clear();
    }*/
}
