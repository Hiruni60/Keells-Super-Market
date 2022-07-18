package controller;

import CrudUtil.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.employee;
import net.sf.jasperreports.engine.part.PartEvaluationTime;
import net.sf.jasperreports.parts.PartComponentsExtensionsRegistryFactory;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.regex.Pattern;

public class EmployeesFormController {

    public TextField txtEmpId;
    public TextField txtEmpName;
    public TextField txtEmpPassword;
    public TextField txtEmpStatus;
    public ComboBox<String> cmbEmpGender;

    public TableView tblEmployee;
    public TableColumn colEmpId;
    public TableColumn colEmpName;
    public TableColumn colEmpPassword;
    public TableColumn colEmpGender;
    public TableColumn colEmpStatus;
    public Button btnAdd;

    public void initialize() throws SQLException, ClassNotFoundException {
        cmbEmpGender.getItems().add("Male");
        cmbEmpGender.getItems().add("Female");

        colEmpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmpPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colEmpGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colEmpStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadAllEmployee();
    }

    public void EmpAddOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        saveEmployee();
        loadAllEmployee();
        clearOnAction();
    }

    private void clearOnAction() {
        txtEmpId.clear();
        txtEmpName.clear();
        txtEmpPassword.clear();
        cmbEmpGender.setValue(null);
        txtEmpStatus.clear();
    }

    private void saveEmployee() {
        employee em = new employee(
                txtEmpId.getText(),
                txtEmpName.getText(),
                txtEmpPassword.getText(),
                txtEmpStatus.getText(),
                cmbEmpGender.getValue()
        );
        try {
            if (CrudUtil.execute("INSERT INTO employee VALUES (?,?,?,?,?)",
                    em.getId(), em.getName(), em.getPassword(), em.getStatus(), em.getGender())) {
                new Alert(Alert.AlertType.CONFIRMATION, "SAVE").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.CONFIRMATION, "Empty Result").show();
        }
    }

    private void loadAllEmployee() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM employee");
        ObservableList<employee> oblist = FXCollections.observableArrayList();
        while (resultSet.next()) {
            oblist.add(
                    new employee(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getString(5)
                    )
            );
        }
        tblEmployee.setItems(oblist);
        tblEmployee.refresh();
    }

    public void EditOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        employee eu = new employee(
                txtEmpId.getText(),
                txtEmpName.getText(),
                txtEmpPassword.getText(),
                txtEmpStatus.getText(),
                cmbEmpGender.getValue()
        );
        boolean isEmUpdated = CrudUtil.execute("UPDATE employee SET name=?, password=?, gender=?, status=? WHERE em_id=?",
                eu.getName(),
                eu.getPassword(),
                eu.getStatus(),
                eu.getGender(),
                eu.getId());
        if (isEmUpdated) {
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }

        loadAllEmployee();
        clearOnAction();

    }


    public void DeleteOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        employee emp = (employee) tblEmployee.getSelectionModel().getSelectedItem();
        tblEmployee.getItems().remove(emp);

        boolean isEmDeleted=CrudUtil.execute("DELETE FROM employee WHERE em_id=? ",
                emp.getId()

        );
        if (isEmDeleted) {
            new Alert(Alert.AlertType.CONFIRMATION, "Deleted!").show();
        } else {
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }


    }

    public void empidOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM employee WHERE em_id=? ", txtEmpId.getText());
            if (result.next()) {
                txtEmpName.setText(result.getString(2));
                txtEmpPassword.setText(result.getString(3));
                cmbEmpGender.setValue(result.getString(4));
                txtEmpStatus.setText(result.getString(5));


            } else {
                new Alert(Alert.AlertType.WARNING, "Empty set").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void EmRefreshOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        loadAllEmployee();
    }

    private Object validate() {
        Pattern idPattern = Pattern.compile("^(E00-)[0-9]{3,5}$");
        Pattern namePartern = Pattern.compile("^[A-Z][A-z]{3,20}$");
        Pattern statusPatern= Pattern.compile("^[A-Z][A-z ]{3,20}$");

        if (!idPattern.matcher(txtEmpId.getText()).matches()) {
            addError(txtEmpId);
            return txtEmpId;
        } else {
            removeError(txtEmpId);
            if(!namePartern.matcher(txtEmpName.getText()).matches()){
                addError(txtEmpName);
                return txtEmpName;
            }
            else{
                removeError(txtEmpName);
                if(!statusPatern.matcher(txtEmpStatus.getText()).matches()){
                    addError(txtEmpStatus);
                    return  txtEmpStatus;
                }
                else{
                    removeError(txtEmpStatus);
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
}