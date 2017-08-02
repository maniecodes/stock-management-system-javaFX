/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXTextField;
import static controller.StockController.deleteID;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import stockbase.Database;
import stockmodel.customermodel;
import stockmodel.productmodel;
import stockmodel.salesmodel;
import tools.AlertMaker;

public class SalesController implements Initializable {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Database db;
    
    static int deleteID;
    public static int SALES_ID;
    
    private ObservableList<productmodel> productdata;
    private ObservableList<customermodel> customerdata;
    private ObservableList<salesmodel> data;
    
    @FXML
    private TextField txtSalesID;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtProductPrice;
    @FXML
    private TextField txtProductCode;
    @FXML
    private TextField txtStockQuantity;
    @FXML
    private ComboBox<String> cmbSalesPayment;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtCustomerContact;
    @FXML
    private TextField txtSalesTotal;
    @FXML
    private TextField txtSalesAmount;
    @FXML
    private TextField txtSalesBalance;
    @FXML
    private DatePicker dateSales;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnClear;
    @FXML
    private RadioButton rbDept;
    @FXML
    private RadioButton rbClear;
    @FXML
    private TableView<productmodel> _tableProduct;
    @FXML
    private TableColumn<productmodel, Integer> _productID;
    @FXML
    private TableColumn<productmodel, String> _productName;
    @FXML
    private TableColumn<productmodel, String> _productCode;
    @FXML
    private TableColumn<productmodel, Double> _productPrice;
    @FXML
    private JFXTextField txtSearchAll;
    @FXML
    private TextField txtSearchID;
    @FXML
    private Button btnSearchID;
    @FXML
    private JFXTextField txtSearchProduct;
    @FXML
    private JFXTextField txtSearchCustomer;
    @FXML
    private TableView<customermodel> _tableCustomer;
    @FXML
    private TableColumn<customermodel, Integer> _customerID;
    @FXML
    private TableColumn<customermodel, String> _customerName;
    @FXML
    private TableColumn<customermodel, String> _customerContact;
    @FXML
    private TableView<salesmodel> _tableSales;
    @FXML
    private TableColumn<salesmodel, Integer> _salesID;
    @FXML
    private TableColumn<salesmodel, String> _salesProductName;
    @FXML
    private TableColumn<salesmodel, String> _salesProductCode;
    @FXML
    private TableColumn<salesmodel, Integer> _salesProductQty;
    @FXML
    private TableColumn<salesmodel, Double> _salesProductPrice;
    @FXML
    private TableColumn<salesmodel, Double> _salesTotal;
    @FXML
    private TableColumn<salesmodel, Double> _salesPaid;
    @FXML
    private TableColumn<salesmodel, Double> _salesBalance;
    @FXML
    private TableColumn<salesmodel, String> _salesDate;
    @FXML
    private ToggleGroup payment;
    @FXML
    private Button update;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        db = new Database();
        ObservableList<String>  Payment = FXCollections.observableArrayList("","Cash","Check","Bank Transfer", "Credit","Others");
        cmbSalesPayment.setItems(Payment);
        cmbSalesPayment.getSelectionModel().selectFirst();
        
        txtSalesTotal.setEditable(false);
        txtSalesBalance.setEditable(false);
        
        productdata = FXCollections.observableArrayList();
        buildProductTable();
        loadProductDatabase();
        setProductValueToTextField();
        searchAllProduct();
        
        customerdata = FXCollections.observableArrayList();
        buildCustomerTable();
        loadCustomerDatabase();
        setCustomerValueToTextField();
        searchAllCustomer();
        
        data = FXCollections.observableArrayList();
        buildSalesTable();
        loadFromDatabase();
        setSalesValueToTextField();
        searchAllSales();
    }    

    @FXML
    private void handleNew(ActionEvent event) {
        btnSave.setDisable(false);
        btnEdit.setDisable(true);
        //clearField();
        try {
            if(db.getSalesID() <= 0){
                txtSalesID.setText("1");
            }
            txtSalesID.setText(String.valueOf(db.getSalesID()));

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    
    /***
     * Handles update of the stocks.
     * @param event 
     */
    @FXML
    private void handleUpdate(ActionEvent event) {
        System.out.println("WELCOME TO NEW");
        try {
            salesmodel sm = _tableSales.getItems().get(_tableSales.getSelectionModel().getSelectedIndex());
            //get product name of the product table if its equals to the product name in the textfield then it will do nothing
            String query1 = "select product_name from stock_product where product_name ='"+sm.getSalespn()+"'";
            pst = con.prepareStatement(query1);
            rs = pst.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString(1));
                if(rs.getString(1).equals(txtProductName.getText())){
                    System.out.println("I here");
                }else{
                    //if its not equal then it will perform some update on stock quantity
                    String oldProduct = rs.getString(1);
                    String oldQty = "select stock_qty from stocks where stock_pn ='"+oldProduct+"'";
                    pst = con.prepareStatement(oldQty);
                    ResultSet rs3 = pst.executeQuery();
                    if(rs3.next()){
                        Integer oldStck = Integer.parseInt(rs3.getString(1));
                        System.out.println("I am"+sm.getSalespn() + txtSalesID.getText());
                        String oldQ = "select sales_qty from stock_sales where sales_pn='"+sm.getSalespn()+"' and sales_id ='"+txtSalesID.getText()+"'";
                        pst = con.prepareStatement(oldQ);
                        ResultSet rs4 = pst.executeQuery();
                        if(rs4.next()){
                            Integer oldQuant = Integer.parseInt(rs4.getString(1));
                            Integer calQty = oldStck + oldQuant;
                            //Reversal of stock, adding stock issued back to the main stock. Mayb the customer want another product.
                            String upOld = "update stocks set stock_qty = '"+calQty+"' where stock_pn = '"+oldProduct+"'";
                            pst = con.prepareStatement(upOld);
                            pst.executeUpdate();
                        }  
                    }
                  String myqty = "select stock_qty from stocks where stock_pn = '"+txtProductName.getText()+"'";
                  pst = con.prepareStatement(myqty);
                  ResultSet rs1 = pst.executeQuery();
                  if(rs1.next()){
                      Integer stocQty = Integer.parseInt(rs1.getString(1));
                      Integer userQty = Integer.parseInt(txtStockQuantity.getText());
                      Integer calQty = stocQty - userQty;
                      String updateS = "update stocks set stock_qty = '"+calQty+"' where stock_pn = '"+txtProductName.getText()+"'";
                      pst = con.prepareStatement(updateS);
                      pst.executeUpdate();
                      String updation = "update  stock_sales set sales_pn = '"+txtProductName.getText()+"',sales_pc = '"+txtProductCode.getText()+"', "
                              + "sales_cn='"+txtCustomerName.getText()+"', sales_cc='"+txtCustomerContact.getText()+"' sales_qty = '"+txtStockQuantity.getText()+"',"
                              + "sales_price = '"+txtProductPrice.getText()+"', sales_total='"+txtSalesTotal.getText()+"', sales_ptype='"+cmbSalesPayment.getValue()+"',"
                              + "sales_apaid='"+txtSalesAmount.getText()+"',sales_balance='"+txtSalesBalance.getText()+"' where sales_id = '"+txtSalesID.getText()+"'";
                       pst = con.prepareStatement(updation);
                      pst.executeUpdate();
                      loadFromDatabase();
                      AlertMaker.showSimpleAlert("Success", "updated");
                      clearField();

                    }
                }
            }

        }catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        int pid = 0;
        int cid = 0;
        int sid = 0;
        con = db.connectDB();
        String salesId = txtSalesID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String customerName = txtCustomerName.getText();
        String customerContact = txtCustomerContact.getText();
        String salesDate = dateSales.getValue().toString();
        String stockQty = txtStockQuantity.getText();
        String productPrice = txtProductPrice.getText();
        String salesTotal = txtSalesTotal.getText();
        String salesPayment = cmbSalesPayment.getValue();
        String amountPaid = txtSalesAmount.getText();
        String balance = txtSalesBalance.getText();
        String sql2 = "select product_id from stock_product where product_name ='" +txtProductName.getText()+ "'";
        try {
            pst = con.prepareStatement(sql2);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     pid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        String sql3 = "select customer_id from stock_customer where customer_name ='" +txtCustomerName.getText()+ "'";
        try {
            pst = con.prepareStatement(sql3);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     cid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        String sql4 = "select stock_id from stocks where stock_pn ='"+txtProductName.getText()+"'";
        try {
            pst = con.prepareStatement(sql4);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     sid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        try {
            
            //System.out.println("WHAT THE"+txtProductName.getText());
            String sql = "select stock_qty from stocks where stock_pn = '"+txtProductName.getText()+"'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()){
                int stockqty = Integer.parseInt(rs.getString(1));   
                //System.out.println("stock is "+stockqty);
                int qty = Integer.parseInt(txtStockQuantity.getText());
                int myoldQty;
                String oldstkQty = "select sales_qty from stock_sales where sales_id = '"+txtSalesID.getText()+"'";
                pst = con.prepareStatement(oldstkQty);
                rs = pst.executeQuery();
                if(rs.next()){
                    myoldQty = Integer.parseInt(rs.getString(1));
                    //System.out.println("After bought"+ myoldQty);
                    int myStock =  -qty  + myoldQty;
                    //System.out.println("New stock "+ myStock);
                    if((stockqty + myStock) > 0){
                        Double price = Double.parseDouble(txtProductPrice.getText()); 
                        int balQty = stockqty + myStock;
                        String query = "UPDATE stocks s INNER JOIN stock_product b ON (s.stock_pn = b.product_name) SET s.stock_qty = '"+balQty+"' WHERE s.stock_pn ='"+txtProductName.getText()+"'";
                        pst = con.prepareStatement(query);
                        pst.executeUpdate();                    
                        Double total = qty * price;
                        txtSalesTotal.setText(total.toString()); 
                        
                        
                        String query2 = "update stock_sales set  sales_pn = ?, sales_pc = ?, sales_cn = ?, sales_cc= ?, sales_date =?,"
                          + "sales_qty =?, sales_price =?, sales_total =?, sales_ptype =?, sales_apaid =?, sales_balance =?,"
                          + "sales_pid =?, sales_cid =?, sales_sid =? where sales_id = ?";
                          try { 
                             pst = con.prepareStatement(query2);
                              pst.setString(1, productName);
                              pst.setString(2, productCode);
                              pst.setString(3, customerName);
                              pst.setString(4, customerContact);
                              pst.setString(5, salesDate);
                              pst.setString(6, stockQty);
                              pst.setString(7, productPrice);
                              pst.setString(8, salesTotal);
                              pst.setString(9, salesPayment);
                              pst.setString(10, amountPaid);
                              pst.setString(11, balance);
                              pst.setString(12, String.valueOf(pid));
                              pst.setString(13, String.valueOf(cid));
                              pst.setString(14, String.valueOf(sid));
                              pst.setInt(15, Integer.parseInt(salesId));
                              int i = pst.executeUpdate();

                              if(i == 1){
                                    AlertMaker.showSimpleAlert("Information", "Stock Updated Successfully");
                                    loadFromDatabase();
                                    clearField();
                                  
                            }else{
                                AlertMaker.showErrorAlert("Error", "Could Not Update");
                            }
                              
                          } catch (SQLException ex) {
                              Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                          }
                    } else{
                         AlertMaker.showSimpleAlert("Information", " No Enough Stock.");
                    }  
                    
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    @FXML
    private void handleSave(ActionEvent event) {
        con = db.connectDB();
        int pid = 0;
        int cid = 0;
        int sid = 0;
        String salesId = txtSalesID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String customerName = txtCustomerName.getText();
        String customerContact = txtCustomerContact.getText();
        String salesDate = dateSales.getValue().toString();
        String stockQty = txtStockQuantity.getText();
        String productPrice = txtProductPrice.getText();
        String salesTotal = txtSalesTotal.getText();
        String salesPayment = cmbSalesPayment.getValue();
        String salesAmount = txtSalesAmount.getText();
        String salesBalance = txtSalesBalance.getText();
        String sql2 = "select product_id from stock_product where product_name ='" +txtProductName.getText()+ "'";
        try {
            pst = con.prepareStatement(sql2);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     pid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        String sql3 = "select customer_id from stock_customer where customer_name ='"+txtCustomerName.getText()+"'";
        try {
            pst = con.prepareStatement(sql3);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     cid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        String sql4 = "select stock_id from stocks where stock_pn ='"+txtProductName.getText()+"'";
        try {
            pst = con.prepareStatement(sql4);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     sid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String sql = "insert into stock_sales (sales_id, sales_pn, sales_pc, sales_cn,sales_cc, sales_date,"
                    + "sales_qty, sales_price, sales_total, sales_ptype, sales_apaid, sales_balance,"
                    + "sales_pid, sales_cid, sales_sid) values(null, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {  
            pst = con.prepareStatement(sql);
            pst.setString(1, productName);
            pst.setString(2, productCode);
            pst.setString(3, customerName);
            pst.setString(4, customerContact);
            pst.setString(5, salesDate);
            pst.setString(6, stockQty);
            pst.setString(7, productPrice);
            pst.setString(8, salesTotal);
            pst.setString(9, salesPayment);
            pst.setString(10, salesAmount);
            pst.setString(11, salesBalance);
            pst.setInt(12, pid);
            pst.setInt(13, cid);
            pst.setInt(14, sid);
            
            pst.execute();
            AlertMaker.showSimpleAlert("Success", "Sales inserted successfully");
            clearField();
            buildSalesTable();
            loadFromDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
         con = db.connectDB();
        String sql = "delete from stock_sales where sales_id = ?";
        
        try {   
            pst = con.prepareStatement(sql);
            pst.setString(1, txtSalesID.getText());
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Sales deleted successfully");
                loadFromDatabase();
                clearField();
            }else{
                AlertMaker.showErrorAlert("Error", "Could not delete sales");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearField();
    }

    @FXML
    private void searchById(ActionEvent event) {
        clearField();
        if(!txtSearchID.getText().isEmpty() && txtSearchID.getText() != null){
            setSalesInfo(txtSearchID.getText());
        }
    }
    
    @FXML
    private void calTotal(ActionEvent event) {
        calculateTotal();
    }

    private void calculateTotal(){
        //System.out.println("I GOT HERE");
        con = db.connectDB();
        try {
            
            String sql = "SELECT DISTINCT s.stock_qty from stocks s JOIN stock_product p ON (select product_id from stock_product where product_name ='"+txtProductName.getText()+"') = s.stock_pid";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()){
                int stockqty = Integer.parseInt(rs.getString(1));  
                //System.out.println("GET STOCK QTY"+stockqty);
                int qty = Integer.parseInt(txtStockQuantity.getText());
                //System.out.println("GET USERS QTY" + qty);
                //System.out.println("is stockqty greater than qty" + (stockqty >= qty));
                if(stockqty >= qty){          
                    int balQty = stockqty - qty;
                    //System.out.println("Balance" + balQty);
                    if(_tableSales.getSelectionModel().isEmpty() != true){
                        //System.out.println("I DON REACH");
                        Double price = Double.parseDouble(txtProductPrice.getText());  
                        Double total = qty * price;
                        txtSalesTotal.setText(total.toString());
                        Double amount = Double.parseDouble(txtSalesAmount.getText());
                        Double bal = total - amount;
                        txtSalesBalance.setText(bal.toString());
                    }else{
                        String sql2 = "UPDATE stocks s INNER JOIN stock_product b ON (s.stock_pn = b.product_name) SET s.stock_qty = '"+balQty+"' WHERE s.stock_pn ='"+txtProductName.getText()+"'";
                        Double price = Double.parseDouble(txtProductPrice.getText());  
                        pst = con.prepareStatement(sql2);
                        pst.executeUpdate();                    
                        Double total = qty * price;
                        txtSalesTotal.setText(total.toString());
                    }
                               
                }else{
                    AlertMaker.showSimpleAlert("Information", "No Enough Stock.");
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void calBal(ActionEvent event) {
        Double total = Double.parseDouble(txtSalesTotal.getText());
        Double amount = Double.parseDouble(txtSalesAmount.getText());
        Double bal = total - amount;
        txtSalesBalance.setText(bal.toString());
    }
    
    private void setSalesInfo(String sales_id){
        try {
            boolean exists = false;
            con = db.connectDB();
            String sql = "select * from stock_sales where sales_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, sales_id);
            rs = pst.executeQuery();
            while(rs.next()){
                txtSalesID.setText(rs.getString(1));
                txtProductName.setText(rs.getString(2));
                txtProductCode.setText(rs.getString(3));
                txtCustomerName.setText(rs.getString(4));
                txtCustomerContact.setText(rs.getString(5));
      
                dateSales.setValue(LocalDate.parse(rs.getString(6)));
                txtStockQuantity.setText(rs.getString(7));
                txtProductPrice.setText(rs.getString(8));
                txtSalesTotal.setText(rs.getString(9));
                cmbSalesPayment.setValue(rs.getString(10));
                txtSalesAmount.setText(rs.getString(11));
                txtSalesBalance.setText(rs.getString(12));
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                deleteID = rs.getInt(1);
                SALES_ID = Integer.parseInt(sales_id);
                exists = true;
                
            }
            if(!exists){
                AlertMaker.showErrorAlert("Error", "Search result does not exists");
                deleteID = 0;
            }
            con.close();
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private void buildSalesTable(){
        _salesID.setCellValueFactory(new PropertyValueFactory<>("salesid"));
        _salesProductName.setCellValueFactory(new PropertyValueFactory<>("salespn"));
        _salesProductCode.setCellValueFactory(new PropertyValueFactory<>("salespc"));
        _salesProductQty.setCellValueFactory(new PropertyValueFactory<>("salesqty"));
        _salesProductPrice.setCellValueFactory(new PropertyValueFactory<>("salesprice"));
        _salesTotal.setCellValueFactory(new PropertyValueFactory<>("salestotal"));
        _salesPaid.setCellValueFactory(new PropertyValueFactory<>("salesap"));
        _salesBalance.setCellValueFactory(new PropertyValueFactory<>("salesbal"));
        _salesDate.setCellValueFactory(new PropertyValueFactory<>("salesdate"));
    }
     
    private void loadFromDatabase(){
        data.clear();
        con = db.connectDB();
        try {     
            pst = con.prepareStatement("select * from stock_sales");
            rs =  pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String pName = rs.getString(2);
                String pCod = rs.getString(3);
                Integer sQty = Integer.parseInt(rs.getString(7));
                Double pPrice = rs.getDouble(8);
                Double sTotal = rs.getDouble(9);
                Double sPaid = rs.getDouble(11);
                Double sBal = rs.getDouble(12);
                String sDate = rs.getString(6);
                data.add(new salesmodel(id, pName, pCod, sQty, pPrice, sTotal, sPaid, sBal, sDate));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableSales.setItems(data);
        
    } 
    
    
    private void searchAllSales(){
        
        txtSearchAll.setOnKeyReleased(e ->{
            if(txtSearchAll.getText().equals("")){
                loadFromDatabase();
            }else{
                data.clear();
                String sql = "select * from stock_sales where sales_id like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_pn like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_pc like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_date like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_price like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_qty like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_total like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_apaid like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_sales where sales_balance like '%"+txtSearchAll.getText()+"%'";
                
                try {
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    while(rs.next()){
                        int id = rs.getInt(1);
                    String pName = rs.getString(2);
                    String pCod = rs.getString(3);
                    Integer sQty = Integer.parseInt(rs.getString(7));
                    Double pPrice = rs.getDouble(8);
                    Double sTotal = rs.getDouble(9);
                    Double sPaid = rs.getDouble(11);
                    Double sBal = rs.getDouble(12);
                    String sDate = rs.getString(6);
                    data.add(new salesmodel(id, pName, pCod, sQty, pPrice, sTotal, sPaid, sBal, sDate));
                }
                 _tableSales.setItems(data);

                } catch (SQLException ex) {
                    Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void setSalesValueToTextField(){
        
        _tableSales.setOnMouseClicked((MouseEvent event) -> {
            try {
                salesmodel sm = _tableSales.getItems().get(_tableSales.getSelectionModel().getSelectedIndex());
                update.setDisable(true);
                btnEdit.setDisable(false);
                con = db.connectDB();
                String sql = "select s.sales_cn, s.sales_cc "
                        + "from stock_sales s join stock_customer p on s.sales_cid = p.customer_id "
                        + "where s.sales_id ='"+ sm.getSalesid()+"'";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                String sql2 = "select sales_ptype from stock_sales "
                        + "where sales_id ='"+sm.getSalesid()+"'";
                try {
                    if (rs.next()) {
                        txtCustomerName.setText(rs.getString(1));
                        txtCustomerContact.setText(rs.getString(2));
                        pst = con.prepareStatement(sql2);
                        ResultSet rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            txtSalesID.setText(sm.getSalesid().toString());
                            txtProductName.setText(sm.getSalespn());
                            txtProductCode.setText(sm.getSalespc());
                            txtStockQuantity.setText(sm.getSalesqty().toString());
                            txtProductPrice.setText(sm.getSalesprice().toString());
                            txtSalesTotal.setText(sm.getSalestotal().toString());
                            cmbSalesPayment.setValue(rs1.getString(1));
                            txtSalesAmount.setText(sm.getSalesap().toString());
                            txtSalesBalance.setText(sm.getSalesbal().toString());
                            dateSales.setValue(LocalDate.parse(sm.getSalesdate()));
                            btnSave.setDisable(true);
                        }
                    }
                }catch (SQLException ex) {
                    Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }catch (SQLException ex) {
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
     

    private void loadProductDatabase(){
        productdata.clear();
        con = db.connectDB();
        try {
            pst = con.prepareStatement("select product_id, product_name,product_code, price from stock_product");
            rs = pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String code = rs.getString(3);
                Double price = Double.parseDouble(rs.getString(4));
                productdata.add(new productmodel(id, name, code, price));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableProduct.setItems(productdata);
    }
    private void buildProductTable(){
        _productID.setCellValueFactory(new PropertyValueFactory<>("productid"));
        _productName.setCellValueFactory(new PropertyValueFactory<>("productname"));
        _productCode.setCellValueFactory(new PropertyValueFactory<>("productcode"));
        _productPrice.setCellValueFactory(new PropertyValueFactory<>("productprice"));
    } 
    
    private void searchAllProduct(){
        txtSearchProduct.setOnKeyReleased(e ->{
        
            if(txtSearchProduct.getText().equals("")){
                loadProductDatabase();
            }else{
                productdata.clear();
                String sql = "select product_id, product_name, product_code, price from stock_product where product_id like '%"+txtSearchProduct.getText()+"%'"
                        + " UNION  select product_id, product_name, product_code, price  from stock_product where product_name like '%"+txtSearchProduct.getText()+"%'"
                        + " UNION  select product_id, product_name, product_code, price  from stock_product where product_code like '%"+txtSearchProduct.getText()+"%'"
                        + " UNION  select product_id, product_name, product_code, price  from stock_product where price like '%"+txtSearchProduct.getText()+"%'";
                
                try {
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    
                    while(rs.next()){
                        int id = rs.getInt(1);
                        String name = rs.getString(2);
                        String code = rs.getString(3);
                        Double price = Double.parseDouble(rs.getString(4));
                        productdata.add(new productmodel(id, name, code, price));
                    }
                    _tableProduct.setItems(productdata);

                } catch (SQLException ex) {
                    Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void setProductValueToTextField(){
        
        _tableProduct.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
        public void handle(MouseEvent event){
            update.setDisable(false);
            btnEdit.setDisable(true);
            productmodel pm = _tableProduct.getItems().get(_tableProduct.getSelectionModel().getSelectedIndex());

            txtProductName.setText(pm.getProductname());
            txtProductCode.setText(pm.getProductcode());
            txtProductPrice.setText(String.valueOf(pm.getProductprice()));
            }
        });
    }
    
    private void loadCustomerDatabase(){
        customerdata.clear();
        con = db.connectDB();
        try {
            pst = con.prepareStatement("select customer_id, customer_name, customer_contact from stock_customer");
            rs = pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String contact = rs.getString(3);
                customerdata.add(new customermodel(id, name, contact));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableCustomer.setItems(customerdata);
    }
    private void buildCustomerTable(){
        _customerID.setCellValueFactory(new PropertyValueFactory<>("customerid"));
        _customerName.setCellValueFactory(new PropertyValueFactory<>("customername"));
        _customerContact.setCellValueFactory(new PropertyValueFactory<>("customercon"));
    } 
    
    private void searchAllCustomer(){
        txtSearchCustomer.setOnKeyReleased(e ->{
        if(txtSearchCustomer.getText().equals("")){
           loadCustomerDatabase();
       }else{
           customerdata.clear();
           String sql = "select * from stock_customer where customer_id like '%"+txtSearchCustomer.getText()+"%'"
                   + " UNION  select * from stock_customer where customer_name like '%"+txtSearchCustomer.getText()+"%'"
                   + " UNION  select * from stock_customer where customer_contact like '%"+txtSearchCustomer.getText()+"%'";

           try {
                pst = con.prepareStatement(sql);

                rs = pst.executeQuery();
                while(rs.next()){
                    int id = rs.getInt(1);
                    String sName = rs.getString(2);
                    String sCont = rs.getString(3);
                    customerdata.add(new customermodel(id, sName, sCont ));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
                _tableCustomer.setItems(customerdata);

            }
        });
    }
    
     private void setCustomerValueToTextField(){
        
        _tableCustomer.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
          public void handle(MouseEvent event){
              customermodel cm = _tableCustomer.getItems().get(_tableCustomer.getSelectionModel().getSelectedIndex());
              txtCustomerName.setText(cm.getCustomername());
              txtCustomerContact.setText(cm.getCustomercon());
          }
        });
    }
     
     
    private void clearField(){
        txtSalesID.setText("");
        txtProductName.setText("");
        txtProductCode.setText("");
        txtCustomerName.setText("");
        txtCustomerContact.setText("");
        dateSales.setValue(null);
        txtStockQuantity.setText("");
        txtProductPrice.setText("");     
        txtSalesTotal.setText("");
        cmbSalesPayment.setValue(null);
        txtSalesAmount.setText("");
        txtSalesBalance.setText("");
        
    }


    
}
