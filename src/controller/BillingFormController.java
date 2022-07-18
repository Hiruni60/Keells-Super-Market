package controller;

import CrudUtil.CrudUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Cart;
import model.Product;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class BillingFormController {
    public TextField txtProductId;
    public TextField txtQty;
    public TableColumn colBillId;
    public TableColumn colTotal;
    public TableView<Product> tblProduct1;
    public TableColumn colProId1;
    public TableColumn colProName1;
    public TableColumn colProCategory1;
    public TableColumn colProPrice1;
    public TableColumn colProQty1;
    public TableView<Cart> tblOrder;
    public TableColumn colProId2;
    public TableColumn colProName2;
    public TableColumn colProCategory2;
    public TableColumn colProPrice2;
    public TableColumn colProQty2;
    public TextField txtTotal;
    public TextField txtBillId;
    public TextField txtItemQty;
    public ImageView btnBack;
    public AnchorPane billPane;

    public void AddtoCartOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       ResultSet rs = CrudUtil.execute("SELECT name,category,price,qty FROM product WHERE pro_id=?",txtProductId.getText());
       if(rs.next()){
           ResultSet cat = CrudUtil.execute("SELECT cat_name FROM product_category WHERE cat_id=?",rs.getString(2));
           int qty = Integer.parseInt(txtQty.getText());
           CrudUtil.execute("UPDATE Product SET qty=? WHERE pro_id=?",rs.getInt(4)-qty,txtProductId.getText());
           String category;
           if(cat.next()){
               category=cat.getString(1);
           }
           else {category=null;}
           Double tot = rs.getDouble(3)*qty;
           tblOrder.getItems().add(new Cart(
                   txtProductId.getText(),
                   rs.getString(1),
                   category,
                   rs.getDouble(3),
                   qty,
                   tot
           ));

       }
       loadAllProducts();

       txtProductId.clear();
       txtQty.clear();

    }

    public void initialize() throws SQLException, ClassNotFoundException {

        colProId1.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProCategory1.setCellValueFactory(new PropertyValueFactory<>("category"));
        colProPrice1.setCellValueFactory(new PropertyValueFactory<>("price"));
        colProQty1.setCellValueFactory(new PropertyValueFactory<>("qty"));

        //refreshCombos();

        loadAllProducts();


        colProId2.setCellValueFactory(new PropertyValueFactory<>("proId"));
        colProName2.setCellValueFactory(new PropertyValueFactory<>("proName"));
        colProCategory2.setCellValueFactory(new PropertyValueFactory<>("proCategory"));
        colProPrice2.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colProQty2.setCellValueFactory(new PropertyValueFactory<>("sellQty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));



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
        tblProduct1.setItems(products);
    }


    public void checktotalOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Double tot = 0.0;
        int items = 0;
        ObservableList<Cart> orders = FXCollections.observableArrayList(tblOrder.getItems());
        for (Cart c: orders
             ) {
            tot+=c.getTotal();
            items += c.getSellQty();
        }
        txtTotal.setText(String.valueOf(tot));
        //tblOrder.getItems().clear();
        CrudUtil.execute("INSERT INTO Orders(items,total) VALUES(?,?)",items,tot);
        ResultSet rs = CrudUtil.execute("SELECT bill_ID,items FROM ORDERS ORDER BY bill_ID DESC LIMIT 1");
        if(rs.next()){
            txtBillId.setText("B"+String.valueOf(rs.getInt(1)));
            txtItemQty.setText(String.valueOf(rs.getInt(2)));
        }
        // select *from getLastRecord ORDER BY id DESC LIMIT 1;

    }

    public void printOnAction(ActionEvent actionEvent) {
        System.out.println("print bill");
       String bilId  =txtBillId.getText();
      double total=Double.parseDouble(txtTotal.getText());
        HashMap total2=new HashMap();
        total2.put("Bill",bilId);
        total2.put("total",total);

        ObservableList<Cart> tableRecords = tblOrder.getItems(); // bean collection

        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/report/bill.jasper"));
            ObservableList<Cart> items = tblOrder.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport,total2, new JRBeanCollectionDataSource(items));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    public void refreshOnAction(ActionEvent actionEvent) {
        tblOrder.getItems().clear();
        txtBillId.clear();
        txtItemQty.clear();
        txtTotal.clear();
    }


        public void BackOnMousePressed(MouseEvent mouseEvent) throws IOException {
            setRUI("MainForm");
        }
        private void setRUI(String location) throws IOException {
            Stage stage=(Stage)  billPane.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
            //stage.setTitle("Welcome Form");
        }
   /* private void calculateTotal() {
        double total=0;
        for (Cart tm : tmlist) {
            total = total + tm.getTotal();
        }
        txtTotal.setText(String.valueOf(total));
        System.out.println(total);
    }*/
}
