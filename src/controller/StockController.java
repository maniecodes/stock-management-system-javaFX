/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXTextField;
import static controller.ProductController.deleteID;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import stockbase.Database;
import stockmodel.productmodel;
import stockmodel.stockmodel;
import tools.AlertMaker;

/**
 * FXML Controller class
 *
 * @author Manasseh
 */
public class StockController implements Initializable {

    private ObservableList<productmodel> productdata;
    private ObservableList<stockmodel> data;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Database db;
    private static String query = "";
    private static int PRODUCT_ID;
    static int deleteID;
    
    @FXML
    private TextField txtStockID;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtStockQuantity;
    @FXML
    private TextField txtProductCode;
    @FXML
    private TextField txtProductPrice;
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
    private JFXTextField txtSearchAll;
    @FXML
    private TextField txtSearchID;
    @FXML
    private Button btnSearchID;
    @FXML
    private Button btnDetails;
    @FXML
    private JFXTextField txtSearchProduct;
    @FXML
    private TableView<stockmodel> _tableStock;
    @FXML
    private TableColumn<stockmodel, Integer> _stockID;
    @FXML
    private TableColumn<stockmodel, String> _stockProductName;
    @FXML
    private TableColumn<stockmodel, String> _stockProductCode;
    @FXML
    private TableColumn<stockmodel, Integer> _stockQuantity;
    @FXML
    private TableColumn<stockmodel, Double> _stockProductPrice;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        db = new Database();
        
        //txtStockID.setText("1");
        data = FXCollections.observableArrayList();
        buildStockTable();
        loadFromDatabase();
        setStockValueToTextField();
        searchAllStock();
        
        productdata = FXCollections.observableArrayList();
        buildProductTable();
        loadProductDatabase();
        setProductValueToTextField();
        searchAllProduct();
        
    }    

    @FXML
    private void handleNew(ActionEvent event) {
        btnSave.setDisable(false);
        //clearField();
        try {
            if(db.getStockID() <= 0){
                txtStockID.setText("1");
            }
            txtStockID.setText(String.valueOf(db.getStockID()));

        } catch (SQLException ex) {
            txtStockID.setText("1");
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        int id = 0;
        con = db.connectDB();
        String stockId = txtStockID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String stockQty = txtStockQuantity.getText();
        String productPrice = txtProductPrice.getText();
        String sql2 = "select product_id from stock_product where product_name ='" +txtProductName.getText()+ "'";
        try {
            pst = con.prepareStatement(sql2);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
            }
           
            String sql = "update stocks set stock_pid = ?, stock_pn = ?, stock_pc = ?, "
                    + "stock_qty = ?, stock_pp =? where stock_id = ?";
        try { 
           pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, productName);
            pst.setString(3, productCode);
            pst.setInt(4, Integer.parseInt(stockQty));
            pst.setDouble(5, Double.parseDouble(productPrice));
            pst.setInt(6, Integer.parseInt(stockId));
            
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
    }

    @FXML
    private void handleSave(ActionEvent event) {      
        con = db.connectDB();
        int id = 0;
        String stockId = txtStockID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String stockQty = txtStockQuantity.getText();
        String productPrice = txtProductPrice.getText();
        String sql2 = "select product_id from stock_product where product_name ='" +txtProductName.getText()+ "'";
        try {
            pst = con.prepareStatement(sql2);
        
            rs = pst.executeQuery();
                if(rs.next()){
                     id = rs.getInt(1);
                }
            } catch (SQLException ex) {
                Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
            }
            String sql = "insert into stocks (stock_id, stock_pid, stock_pn, stock_pc,"
                    + "stock_qty, stock_pp) values(null, ?,?,?,?,?)";
        try {  
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, productName);
            pst.setString(3, productCode);
            pst.setString(4, stockQty);
            pst.setString(5, productPrice);
            if(notEmpty()){
                int success = pst.executeUpdate();
                if(success == 1){
                    AlertMaker.showSimpleAlert("Success", "Stock inserted successfully");
                    clearField();
                    buildStockTable();
                    loadFromDatabase();
                }
                
            }else{
                 AlertMaker.showSimpleAlert("Information", "All fields requried");
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        con = db.connectDB();
        String sql = "delete from stocks where stock_id = ?";
        
        try {   
            pst = con.prepareStatement(sql);
            pst.setString(1, txtStockID.getText());
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Stock deleted successfully");
                loadFromDatabase();
                clearField();
            }else{
                AlertMaker.showErrorAlert("Error", "Could not delete stock");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
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
            setStockInfo(txtSearchID.getText());
        }
    }

    private boolean notEmpty(){
        return((!txtStockID.getText().isEmpty() || !txtStockID.getText().equals(""))
                &&((!txtProductName.getText().isEmpty() || !txtProductName.getText().equals(""))
                &&((!txtProductPrice.getText().isEmpty() || !txtProductPrice.getText().equals("")))));
    }
    
    
    private void buildStockTable(){
        _stockID.setCellValueFactory(new PropertyValueFactory<>("stockid"));
        _stockProductName.setCellValueFactory(new PropertyValueFactory<>("stockpn"));
        _stockProductCode.setCellValueFactory(new PropertyValueFactory<>("stockpc"));
        _stockQuantity.setCellValueFactory(new PropertyValueFactory<>("stockqty"));
        _stockProductPrice.setCellValueFactory(new PropertyValueFactory<>("stockprice"));
    }
    
    private void setStockValueToTextField(){
        
        _tableStock.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
          public void handle(MouseEvent event){
              stockmodel sm = _tableStock.getItems().get(_tableStock.getSelectionModel().getSelectedIndex());
              txtStockID.setText(sm.getStockid().toString());
              txtProductName.setText(sm.getStockpn());
              txtProductCode.setText(sm.getStockpc());
              txtStockQuantity.setText(sm.getStockqty().toString());
              txtProductPrice.setText(sm.getStockprice().toString());
              btnSave.setDisable(true);
          }
        });
    }
    
    private void searchAllStock(){
        
        txtSearchAll.setOnKeyReleased(e ->{
            if(txtSearchAll.getText().equals("")){
                loadFromDatabase();
            }else{
                data.clear();
                String sql = "select * from stocks where stock_id like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stocks where stock_pn like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stocks where stock_pc like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stocks where stock_qty like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stocks where stock_pp like '%"+txtSearchAll.getText()+"%'";
                
                try {
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    while(rs.next()){
                        int id = rs.getInt(1);
                        String pName = rs.getString(2);
                        String pCat = rs.getString(3);
                        Integer sQty = Integer.parseInt(rs.getString(4));
                        Double pPrice = rs.getDouble(5);
                        data.add(new stockmodel(id, pName, pCat, sQty, pPrice));
                    }
                    _tableStock.setItems(data);

                } catch (SQLException ex) {
                    Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void loadFromDatabase(){
        data.clear();
        con = db.connectDB();
        try {     
            pst = con.prepareStatement("select * from stocks");
            rs =  pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String pName = rs.getString(2);
                String pCat = rs.getString(3);
                Integer sQty = Integer.parseInt(rs.getString(4));
                Double pPrice = rs.getDouble(5);
                data.add(new stockmodel(id, pName, pCat, sQty, pPrice));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableStock.setItems(data);
        
    } 
    
    private void loadProductDatabase(){
        productdata.clear();
        con = db.connectDB();
        try {
            pst = con.prepareStatement("select product_id, product_name from stock_product");
            rs = pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                productdata.add(new productmodel(id, name));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableProduct.setItems(productdata);
    }
    private void buildProductTable(){
        _productID.setCellValueFactory(new PropertyValueFactory<>("productid"));
        _productName.setCellValueFactory(new PropertyValueFactory<>("productname"));
    }
     private void setProductValueToTextField(){
        
        _tableProduct.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
          public void handle(MouseEvent event){
              try {
                  productmodel pm = _tableProduct.getItems().get(_tableProduct.getSelectionModel().getSelectedIndex());
                  con = db.connectDB();
                  String sql = "select product_code, price from stock_product where product_id ='"+ pm.getProductid()+"'";
                  pst = con.prepareStatement(sql);
                  rs = pst.executeQuery();
                  if(rs.next()){
                        txtProductName.setText(pm.getProductname());
                        txtProductCode.setText(rs.getString(1));
                        txtProductPrice.setText(rs.getString(2));
                  }
              } catch (SQLException ex) {
                  Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
              }
          }
        });
    }
    private void setStockInfo(String stock_id){
        try {
            boolean exists = false;
            con = db.connectDB();
            String sql = "select * from stocks where stock_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, stock_id);
            rs = pst.executeQuery();
            while(rs.next()){
                txtStockID.setText(rs.getString(1));
                txtProductName.setText(rs.getString(2));
                txtProductCode.setText(rs.getString(3));
                txtStockQuantity.setText(rs.getString(4));
                txtProductPrice.setText(rs.getString(5));
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                deleteID = rs.getInt(1);
                PRODUCT_ID = Integer.parseInt(stock_id);
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
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void searchAllProduct(){
        txtSearchProduct.setOnKeyReleased(e ->{
        
            if(txtSearchProduct.getText().equals("")){
                loadProductDatabase();
            }else{
                productdata.clear();
                String sql = "select product_id, product_name from stock_product where product_id like '%"+txtSearchProduct.getText()+"%'"
                        + " UNION  select product_id, product_name from stock_product where product_name like '%"+txtSearchProduct.getText()+"%'";
                
                try {
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    
                    while(rs.next()){
                        int id = rs.getInt(1);
                        String pName = rs.getString(2);
                        productdata.add(new productmodel(id, pName));
                    }
                    _tableProduct.setItems(productdata);

                } catch (SQLException ex) {
                    Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void clearField(){
        txtStockID.setText("");
        txtProductName.setText("");
        txtProductCode.setText("");
        txtStockQuantity.setText("");
        txtProductPrice.setText("");
    }
}
