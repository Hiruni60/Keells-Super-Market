package controller;

import CrudUtil.CrudUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Cart;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class IncomeReportFormController {
    public Label IncomeReportPane;
    public TextField txtDate;
    public TextField txtItemSold;
    public TextField txtTotalIncome;
    public Button btnPrintReport;
    public Button btnRefresh;

    public void initialize(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("LL-c-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        txtDate.setText(formattedDate);


    }

    public void printreportOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        System.out.println("print bill");
        String soldItems  =txtItemSold.getText();
        String totalIncome=txtTotalIncome.getText();
        HashMap report=new HashMap();
        report.put("items_sold",soldItems);
        report.put("total_amount",totalIncome);

        try {
            JasperReport compiledReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/report/income.jasper"));
            //ObservableList<Cart> items = tblOrder.getItems();
            JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport,report,new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    public void refreshOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Double total = 0.0;
        int items = 0;
        ResultSet rs = CrudUtil.execute("SELECT items,total FROM Orders");
        while (rs.next()){
            total += rs.getDouble(2);
            items += rs.getInt(1);
        }
        System.out.println(total);
        txtTotalIncome.setText(String.valueOf(total));
        txtItemSold.setText(String.valueOf(items));
    }
}
