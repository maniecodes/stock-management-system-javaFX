/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import stockbase.Database;
import stockmodel.customermodel;
import stockmodel.productmodel;
import stockmodel.purchasemodel;
import stockmodel.salesmodel;
import stockmodel.suppliermodel;
import tools.AlertMaker;

/**
 * FXML Controller class
 *
 * @author Manasseh
 */
public class PurchaseController implements Initializable {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Database db;
    
    static int deleteID;
    public static int SALES_ID;
    
    private ObservableList<productmodel> productdata;
    private ObservableList<suppliermodel> supplierdata;
    private ObservableList<purchasemodel> data;
    
    @FXML
    private TextField txtPurchaseID;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtProductPrice;
    @FXML
    private TextField txtProductCode;
    @FXML
    private TextField txtStockQuantity;
    @FXML
    private ComboBox<String> cmbPurchasePayment;
    @FXML
    private TextField txtSupplierName;
    @FXML
    private TextField txtSupplierContact;
    @FXML
    private TextField txtPurchaseTotal;
    @FXML
    private TextField txtPurchaseAmount;
    @FXML
    private TextField txtPurchaseBalance;
    @FXML
    private DatePicker datePurchase;
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
    private JFXTextField txtSearchSupplier;
    @FXML
    private TableView<suppliermodel> _tableSupplier;
    @FXML
    private TableColumn<suppliermodel, Integer> _supplierID;
    @FXML
    private TableColumn<suppliermodel, String> _supplierName;
    @FXML
    private TableColumn<suppliermodel, String> _supplierContact;
    @FXML
    private TableView<purchasemodel> _tablePurchase;
    @FXML
    private TableColumn<purchasemodel, Integer> _purchaseID;
    @FXML
    private TableColumn<purchasemodel, String> _purchaseProductName;
    @FXML
    private TableColumn<purchasemodel, String> _purchaseProductCode;
    @FXML
    private TableColumn<purchasemodel, Integer> _purchaseProductQty;
    @FXML
    private TableColumn<purchasemodel, Double> _purchaseProductPrice;
    @FXML
    private TableColumn<purchasemodel, Double> _purchaseTotal;
    @FXML
    private TableColumn<purchasemodel, Double> _purchasePaid;
    @FXML
    private TableColumn<purchasemodel, Double> _purchaseBalance;
    @FXML
    private TableColumn<purchasemodel, String> _purchaseDate;
    @FXML
    private Button btnUpdate;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        db = new Database();
        ObservableList<String>  Payment = FXCollections.observableArrayList("","Cash","Check","Bank Transfer", "Credit","Others");
        cmbPurchasePayment.setItems(Payment);
        cmbPurchasePayment.getSelectionModel().selectFirst();
        
        productdata = FXCollections.observableArrayList();
        buildProductTable();
        loadProductDatabase();
        setProductValueToTextField();
        searchAllProduct();
        
        supplierdata = FXCollections.observableArrayList();
        buildSupplierTable();
        loadSupplierDatabase();
        setSupplierValueToTextField();
        searchAllSupplier();
        
        data = FXCollections.observableArrayList();
        buildPurchaseTable();
        loadFromDatabase();
        setPurchaseValueToTextField();
        //searchAllPurchase();
       
        
    }    
    
     @FXML
    private void handleNew(ActionEvent event) {
        btnSave.setDisable(false);
        //clearField();
        try {
            txtPurchaseID.setText(String.valueOf(db.getPurchaseID()));

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
       con = db.connectDB();
        int pid = 0;
        int cid = 0;
        int sid = 0;
        String purchaseId = txtPurchaseID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String supplierName = txtSupplierName.getText();
        String supplierContact = txtSupplierContact.getText();
        String purchaseDate = datePurchase.getValue().toString();
        String stockQty = txtStockQuantity.getText();
        String productPrice = txtProductPrice.getText();
        String purchaseTotal = txtPurchaseTotal.getText();
        String purchasePayment = cmbPurchasePayment.getValue();
        String purchaseAmount = txtPurchaseAmount.getText();
        String purchaseBalance = txtPurchaseBalance.getText();
       String sql2 = "select product_id from stock_product where product_name ='" +txtProductName.getText()+ "'";
        try {
            pst = con.prepareStatement(sql2);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     pid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        String fullname = txtSupplierName.getText();
              int start = fullname.indexOf(' ');
              int end = fullname.lastIndexOf(' ');
              
              String firstname = "";
              String lastname = "";
              
              if(start >= 0){
                   firstname = fullname.substring(0, start);
                   //System.out.println(firstname);
                   if(end > 0){
                        lastname = fullname.substring(end + 1, fullname.length());
                        //System.out.println(lastname);
                   }
              }
        
        
        String sql3 = "select supplier_id from stock_supplier where supplier_firstname ='"+firstname+"' and supplier_lastname = '"+lastname+"'";
        try {
            pst = con.prepareStatement(sql3);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     cid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        String sql4 = "select stock_id from stocks where stock_pn ='"+txtProductName.getText()+"'";
        try {
            pst = con.prepareStatement(sql4);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     sid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        try {
            
            String sql = "select stock_qty from stocks where stock_pn = '"+txtProductName.getText()+"'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()){
                int stockqty = Integer.parseInt(rs.getString(1));               
                int qty = Integer.parseInt(txtStockQuantity.getText());
                int myoldQty;
                String oldstkQty = "select purchase_qty from stock_purchase where purchase_id = '"+txtPurchaseID.getText()+"'";
                pst = con.prepareStatement(oldstkQty);
                rs = pst.executeQuery();
                if(rs.next()){
                    myoldQty = Integer.parseInt(rs.getString(1));
                    int myStock =  myoldQty - qty;
                   
                    Double price = Double.parseDouble(txtProductPrice.getText()); 
                    int balQty = stockqty - myStock;
                    String query = "UPDATE stocks s INNER JOIN stock_product b ON (s.stock_id = b.product_id) SET s.stock_qty = '"+balQty+"' WHERE s.stock_pn ='"+txtProductName.getText()+"'";
                    pst = con.prepareStatement(query);
                    pst.executeUpdate();                    
                    Double total = qty * price;
                    txtPurchaseTotal.setText(total.toString()); 

                        
                    String query2 = "update stock_purchase set  purchase_pn = ?, purchase_pc = ?, purchase_sn = ?, purchase_sc= ?, purchase_date =?,"
                      + "purchase_qty =?, purchase_price =?, purchase_total =?, purchase_ptype =?, purchase_apaid =?, purchase_balance =?,"
                      + "purchase_pid =?, purchase_cid =?, purchase_sid =? where purchase_id = ?";
                      try { 
                         pst = con.prepareStatement(query2);
                          pst.setString(1, productName);
                          pst.setString(2, productCode);
                          pst.setString(3, supplierName);
                          pst.setString(4, supplierContact);
                          pst.setString(5, purchaseDate);
                          pst.setString(6, stockQty);
                          pst.setString(7, productPrice);
                          pst.setString(8, purchaseTotal);
                          pst.setString(9, purchasePayment);
                          pst.setString(10, purchaseAmount);
                          pst.setString(11, purchaseBalance);
                          pst.setString(12, String.valueOf(pid));
                          pst.setString(13, String.valueOf(cid));
                          pst.setString(14, String.valueOf(sid));
                          pst.setInt(15, Integer.parseInt(purchaseId));
                          int i = pst.executeUpdate();
         try{                 
         purchasemodel sm = _tablePurchase.getItems().get(_tablePurchase.getSelectionModel().getSelectedIndex());
            //get product name of the product table if its equals to the product name in the textfield then it will do nothing
            String query1 = "select product_name from stock_product where product_name ='"+sm.getPurchasepn()+"'";
            pst = con.prepareStatement(query1);
            rs = pst.executeQuery();
            if(rs.next()){
                System.out.println(rs.getString(1));
                if(rs.getString(1).equals(txtProductName.getText())){
                    System.out.println("Igot here");
                }else{
                    //if its not equal then it will perform some update on stock quantity
                    String oldProduct = rs.getString(1);
                    String oldQty = "select stock_qty from stocks where stock_pn ='"+oldProduct+"'";
                    pst = con.prepareStatement(oldQty);
                    ResultSet rs3 = pst.executeQuery();
                    if(rs3.next()){
                        Integer oldStck = Integer.parseInt(rs3.getString(1));
                        System.out.println("Product  TableName: "+sm.getPurchasepn());
                        System.out.println("Product  ID: "+sm.getPurchaseid());
                        
                        String oldQ = "select purchase_qty from stock_purchase where purchase_pn='"+txtProductName.getText()+"' and purchase_id ='"+sm.getPurchaseid()+"'";
                        pst = con.prepareStatement(oldQ);
                        System.out.println("Why"+pst);
                        rs = pst.executeQuery();
                        System.out.println("no idea"+rs);
                        if(rs.next()){
                            System.out.println("I am here");
                            Integer oldQuant = Integer.parseInt(rs.getString(1));
                            Integer calQty = oldStck - oldQuant;
                            System.out.println("oldStk" + oldStck);
                            System.out.println("oldQ" + oldQuant);
                            System.out.println("I have "+ (oldStck + oldQuant));
                            //Reversal of stock, adding stock issued back to the main stock. Mayb the customer want another product.
                            String upOld = "update stocks set stock_qty = '"+calQty+"' where stock_pn = '"+oldProduct+"'";
                            pst = con.prepareStatement(upOld);
                            pst.executeUpdate();
                        }  
                        
                        
                          if(i == 1){
                              AlertMaker.showSimpleAlert("Information", "Stock Updated Successfully");
                              loadFromDatabase();
                              clearField();
                          }else{
                              AlertMaker.showErrorAlert("Error", "Could Not Update");
                          }
                          }
                }
                }
                    } catch (SQLException ex) {
                          Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                      }
                
                
            
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }}}
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
        String purchaseId = txtPurchaseID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String supplierName = txtSupplierName.getText();
        String supplierContact = txtSupplierContact.getText();
        String purchaseDate = datePurchase.getValue().toString();
        String stockQty = txtStockQuantity.getText();
        String productPrice = txtProductPrice.getText();
        String purchaseTotal = txtPurchaseTotal.getText();
        String purchasePayment = cmbPurchasePayment.getValue();
        String purchaseAmount = txtPurchaseAmount.getText();
        String purchaseBalance = txtPurchaseBalance.getText();
        String sql2 = "select product_id from stock_product where product_name ='" +txtProductName.getText()+ "'";
        try {
            pst = con.prepareStatement(sql2);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     pid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        String fullname = txtSupplierName.getText();
              int start = fullname.indexOf(' ');
              int end = fullname.lastIndexOf(' ');
              
              String firstname = "";
              String lastname = "";
              
              if(start >= 0){
                   firstname = fullname.substring(0, start);
                   //System.out.println(firstname);
                   if(end > 0){
                        lastname = fullname.substring(end + 1, fullname.length());
                        //System.out.println(lastname);
                   }
              }
        
        
        String sql3 = "select supplier_id from stock_supplier where supplier_firstname ='"+firstname+"' and supplier_lastname = '"+lastname+"'";
        try {
            pst = con.prepareStatement(sql3);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     cid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        String sql4 = "select stock_id from stocks where stock_pn ='"+txtProductName.getText()+"'";
        try {
            pst = con.prepareStatement(sql4);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     sid = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String sql = "insert into stock_purchase (purchase_id, purchase_pn, purchase_pc, purchase_sn, purchase_sc, purchase_date,"
                    + "purchase_qty, purchase_price, purchase_total, purchase_ptype, purchase_apaid, purchase_balance,"
                    + "purchase_pid, purchase_cid, purchase_sid) values(null, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {  
            pst = con.prepareStatement(sql);
            pst.setString(1, productName);
            pst.setString(2, productCode);
            pst.setString(3, supplierName);
            pst.setString(4, supplierContact);
            pst.setString(5, purchaseDate);
            pst.setString(6, stockQty);
            pst.setString(7, productPrice);
            pst.setString(8, purchaseTotal);
            pst.setString(9, purchasePayment);
            pst.setString(10, purchaseAmount);
            pst.setString(11, purchaseBalance);
            pst.setInt(12, pid);
            pst.setInt(13, cid);
            pst.setInt(14, sid);
            
            pst.execute();
            AlertMaker.showSimpleAlert("Success", "Purchase inserted successfully");
            clearField();
            buildPurchaseTable();
            loadFromDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        con = db.connectDB();
        String sql = "delete from stock_purchase where purchase_id = ?";
        
        try {   
            pst = con.prepareStatement(sql);
            pst.setString(1, txtPurchaseID.getText());
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Purchase deleted successfully");
                loadFromDatabase();
                clearField();
            }else{
                AlertMaker.showErrorAlert("Error", "Could not delete purchase");
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
    private void calTotal(ActionEvent event) {
        calculateTotal();
        
    }
    
    private void calculateTotal(){
        con = db.connectDB();
        System.out.println("I GOT HERE");
        try {
            String sql = "SELECT DISTINCT s.stock_qty from stocks s JOIN stock_product p ON (select product_id from stock_product where product_name ='"+txtProductName.getText()+"') = s.stock_pid";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()){
                int stockqty = Integer.parseInt(rs.getString(1));               
                int qty = Integer.parseInt(txtStockQuantity.getText());
                
            Double price = Double.parseDouble(txtProductPrice.getText()); 
            int balQty = stockqty + qty;
            String sql2 = "UPDATE stocks s INNER JOIN stock_product b ON (s.stock_pn = b.product_name) SET s.stock_qty = '"+balQty+"' WHERE s.stock_pn ='"+txtProductName.getText()+"'";
            pst = con.prepareStatement(sql2);
            pst.executeUpdate();                    
            Double total = qty * price;
            txtPurchaseTotal.setText(total.toString());           
  
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void calBal(ActionEvent event) {
        Double total = Double.parseDouble(txtPurchaseTotal.getText());
        Double amount = Double.parseDouble(txtPurchaseAmount.getText());
        Double bal = total - amount;
        txtPurchaseBalance.setText(bal.toString());
    }
    
    private void loadFromDatabase(){
        data.clear();
        con = db.connectDB();
        try {     
            pst = con.prepareStatement("select * from stock_purchase");
            rs =  pst.executeQuery();
            while(rs.next()){
                Integer id = rs.getInt(1);
                String pName = rs.getString(2);
                String pCod = rs.getString(3);
                Integer sQty = Integer.parseInt(rs.getString(7));
                Double pPrice = rs.getDouble(8);
                Double sTotal = rs.getDouble(9);
                Double sPaid = rs.getDouble(11);
                Double sBal = rs.getDouble(12);
                String sDate = rs.getString(6);
                data.add(new purchasemodel(id, pName, pCod, sQty, pPrice, sTotal, sPaid, sBal, sDate));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tablePurchase.setItems(data);
        
    } 
    
    
    private void setPurchaseValueToTextField(){
        
        _tablePurchase.setOnMouseClicked((MouseEvent event) -> {
            System.out.println("im here dude");
            try {
                purchasemodel sm = _tablePurchase.getItems().get(_tablePurchase.getSelectionModel().getSelectedIndex());
                con = db.connectDB();
                String sql = "select s.purchase_sn, s.purchase_sc "
                        + "from stock_purchase s join stock_supplier p on s.purchase_cid = p.supplier_id "
                        + "where s.purchase_id ='"+ sm.getPurchaseid()+"'";
                pst = con.prepareStatement(sql);
                rs = pst.executeQuery();
                String sql2 = "select purchase_ptype from stock_purchase "
                        + "where purchase_id ='"+sm.getPurchaseid()+"'";
                try {
                    if (rs.next()) {
                        txtSupplierName.setText(rs.getString(1));
                        txtSupplierContact.setText(rs.getString(2));
                        pst = con.prepareStatement(sql2);
                        ResultSet rs1 = pst.executeQuery();
                        if (rs1.next()) {
                            txtPurchaseID.setText(sm.getPurchaseid().toString());
                            txtProductName.setText(sm.getPurchasepn());
                            txtProductCode.setText(sm.getPurchasepc());
                            txtStockQuantity.setText(sm.getPurchaseqty().toString());
                            txtProductPrice.setText(sm.getPurchaseprice().toString());
                            txtPurchaseTotal.setText(sm.getPurchasetotal().toString());
                            cmbPurchasePayment.setValue(rs1.getString(1));
                            txtPurchaseAmount.setText(sm.getPurchaseap().toString());
                            txtPurchaseBalance.setText(sm.getPurchasebal().toString());
                            datePurchase.setValue(LocalDate.parse(sm.getPurchasedate()));
                            btnSave.setDisable(true);
                        }
                    }
                }catch (SQLException ex) {
                    Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }catch (SQLException ex) {
                Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
  
    private void buildPurchaseTable(){
        _purchaseID.setCellValueFactory(new PropertyValueFactory<>("purchaseid"));
        _purchaseProductName.setCellValueFactory(new PropertyValueFactory<>("purchasepn"));
        _purchaseProductCode.setCellValueFactory(new PropertyValueFactory<>("purchasepc"));
        _purchaseProductQty.setCellValueFactory(new PropertyValueFactory<>("purchaseqty"));
        _purchaseProductPrice.setCellValueFactory(new PropertyValueFactory<>("purchaseprice"));
        _purchaseTotal.setCellValueFactory(new PropertyValueFactory<>("purchasetotal"));
        _purchasePaid.setCellValueFactory(new PropertyValueFactory<>("purchaseap"));
        _purchaseBalance.setCellValueFactory(new PropertyValueFactory<>("purchasebal"));
        _purchaseDate.setCellValueFactory(new PropertyValueFactory<>("purchasedate"));
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
             
            productmodel pm = _tableProduct.getItems().get(_tableProduct.getSelectionModel().getSelectedIndex());

            txtProductName.setText(pm.getProductname());
            txtProductCode.setText(pm.getProductcode());
            txtProductPrice.setText(String.valueOf(pm.getProductprice()));
            }
        });
    }
    
    
    private void loadSuppleirDatabase(){
        supplierdata.clear();
        con = db.connectDB();
        try {
            pst = con.prepareStatement("select supplier_id, supplier_firstname, supplier_lastname, supplier_contact from stock_supplier");
            rs = pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String fname = rs.getString(2);
                String lname = rs.getString(3);
                String contact = rs.getString(4);
                String name = fname + " "+ lname;
                supplierdata.add(new suppliermodel(id, name, contact));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PurchaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableSupplier.setItems(supplierdata);
    }
    private void buildSupplierTable(){
        _supplierID.setCellValueFactory(new PropertyValueFactory<>("supplierid"));
        _supplierName.setCellValueFactory(new PropertyValueFactory<>("suppliername"));
        _supplierContact.setCellValueFactory(new PropertyValueFactory<>("suppliercontact"));
    } 
    
    private void loadSupplierDatabase(){
        supplierdata.clear();
        con = db.connectDB();
        try {
            pst = con.prepareStatement("select supplier_id, supplier_firstname, supplier_lastname, supplier_contact from stock_supplier");
            rs = pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String fname = rs.getString(2);
                String lname = rs.getString(3);
                String contact = rs.getString(4);
                String name = fname + " " + lname;
                supplierdata.add(new suppliermodel(id, name, contact));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableSupplier.setItems(supplierdata);
    }
    
    private void setSupplierValueToTextField(){
        
        _tableSupplier.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
        public void handle(MouseEvent event){
             
            suppliermodel pm = _tableSupplier.getItems().get(_tableSupplier.getSelectionModel().getSelectedIndex());

            txtSupplierName.setText(pm.getSuppliername());
            txtSupplierContact.setText(pm.getSuppliercontact());
            }
        });
    }
    
      private void searchAllSupplier(){
        txtSearchSupplier.setOnKeyReleased(e ->{
        if(txtSearchSupplier.getText().equals("")){
           loadSupplierDatabase();
       }else{
           supplierdata.clear();
           String sql = "select * from stock_supplier where supplier_id like '%"+txtSearchSupplier.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_firstname like '%"+txtSearchSupplier.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_lastname like '%"+txtSearchSupplier.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_contact like '%"+txtSearchSupplier.getText()+"%'";

           try {
                pst = con.prepareStatement(sql);

                rs = pst.executeQuery();
                while(rs.next()){
                    int id = rs.getInt(1);
                    String sName = rs.getString(2);
                    String sCont = rs.getString(3);
                    supplierdata.add(new suppliermodel(id, sName, sCont ));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(SalesController.class.getName()).log(Level.SEVERE, null, ex);
            }
                _tableSupplier.setItems(supplierdata);

            }
        });
    }
      
      private void clearField(){
        txtPurchaseID.setText("");
        txtProductName.setText("");
        txtProductCode.setText("");
        txtSupplierName.setText("");
        txtSupplierContact.setText("");
        datePurchase.setValue(null);
        txtStockQuantity.setText("");
        txtProductPrice.setText("");     
        txtPurchaseTotal.setText("");
        cmbPurchasePayment.setValue(null);
        txtPurchaseAmount.setText("");
        txtPurchaseBalance.setText("");
      }
    
}
