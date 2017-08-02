/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXTextField;
import tools.AlertMaker;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import stockbase.Database;
import stockmodel.productmodel;
import tools.Validate;

/**
 * FXML Controller class
 *
 * @author Manasseh
 */
public class ProductController implements Initializable {
    
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Statement st;
    private Database db;
    private Validate validate;
    
    static int deleteID;
    public static int PRODUCT_ID;
    private static String query = "";
    
    
    @FXML
    private TextField txtProductID;
    @FXML
    private TextField txtProductName;
    @FXML
    private TextField txtProductCode;
    @FXML
    private TextField txtProductPrice;
    @FXML
    private ComboBox<String> cmbProductCat;
    @FXML
    private ComboBox<String> cmbProductBrand;
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

    
    private ObservableList<productmodel> data;
    @FXML
    private TableView<productmodel> _tableProduct;
    @FXML
    private TableColumn<productmodel, Integer> _productID;
    @FXML
    private TableColumn<productmodel, String> _productName;
    @FXML
    private TableColumn<productmodel, String> _productCode;
    @FXML
    private TableColumn<productmodel, String> _productCategory;
    @FXML
    private TableColumn<productmodel, String> _productBrand;
    @FXML
    private TableColumn<productmodel, Double> _productPrice;
    @FXML
    private JFXTextField txtSearchAll;
    @FXML
    private TextField txtSearchID;
    @FXML
    private Button btnSearchID;
    @FXML
    private RadioButton rbDefault;
    @FXML
    private ToggleGroup viewToggle;
    @FXML
    private RadioButton rbUbiquiti;
    @FXML
    private RadioButton rbMikrotik;
    @FXML
    private RadioButton rbCisco;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         db = new Database();
        // Populate Combo with Category, Brand
        ObservableList<String> Category = FXCollections.observableArrayList(" ", "Routers", "Cables", "Switch", "Access Points");
        cmbProductCat.setItems(Category);
        cmbProductCat.getSelectionModel().selectFirst();
        
        
        ObservableList<String> Brand = FXCollections.observableArrayList(" ", "Aico", "Brandex", "Ubiquiti", "Cisco", "TP-Link", 
                "D-link", "Dintek");
        cmbProductBrand.setItems(Brand);
        cmbProductBrand.getSelectionModel().selectFirst();
        //txtProductID.setText("Click new button:");
        
        
        data = FXCollections.observableArrayList();
        buildProductTable();
        loadFromDatabase();
        setProductValueToTextField();
        searchAllProduct();
    }    

    @FXML
    private void handleNew(ActionEvent event) {
        btnSave.setDisable(false);
        //clearField();
        try {
            if(db.getProductID() <= 0){
                txtProductID.setText("");
            }
            txtProductID.setText(String.valueOf(db.getProductID()));

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        
        con = db.connectDB();
        String productId = txtProductID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String productCategory = cmbProductCat.getValue();
        String productBrand = cmbProductBrand.getValue();
        String productPrice = txtProductPrice.getText();
        try {    
            String sql = "update stock_product set product_name = ?, product_code = ?, "
                    + "product_category = ?, product_brand =?, price = ? where product_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, productName);
            pst.setString(2, productCode);
            pst.setString(3, productCategory);
            pst.setString(4, productBrand);
            pst.setDouble(5, Double.parseDouble(productPrice));
            pst.setInt(6, Integer.parseInt(productId));
            
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Product Updated Successfully");
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
        String productId = txtProductID.getText();
        String productName = txtProductName.getText();
        String productCode = txtProductCode.getText();
        String productCategory = cmbProductCat.getValue();
        String productBrand = cmbProductBrand.getValue();
        String productPrice = txtProductPrice.getText();
        
        String sql = "insert into stock_product(product_id, product_name,"
                + "product_code, product_category, product_brand, price) values("
                + "null, ?,?,?,?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, productName);
            pst.setString(2, productCode);
            pst.setString(3, productCategory);
            pst.setString(4, productBrand);
            pst.setString(5, productPrice);           
            if(notEmpty()){
                if(db.isDouble(txtProductPrice)){
                   int success = pst.executeUpdate();
                    if (success == 1){
                         AlertMaker.showSimpleAlert("Success", "Product inserted successfully");
                         clearField();
                         buildProductTable();
                         loadFromDatabase();
                     }
                }else{
                    txtProductPrice.setText("");
                }
            }else{
                AlertMaker.showSimpleAlert("Information", "All fields requried");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        
        con = db.connectDB();
        String sql = "delete from stock_product where product_id = ?";
        
        try {   
            pst = con.prepareStatement(sql);
            pst.setString(1, txtProductID.getText());
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Product deleted successfully");
                loadFromDatabase();
                clearField();
            }else{
                AlertMaker.showErrorAlert("Error", "Could not delete product");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @FXML
    private void searchById(ActionEvent event) {
        clearField();
        if(!txtSearchID.getText().isEmpty() && txtSearchID.getText() != null){
            setInfo(txtSearchID.getText());
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        clearField();
    }
    
    
   @FXML
    private void viewDefault(ActionEvent event) {
        query = "select * from stock_product";
         BuildTableData(query);
    }

    @FXML
    private void viewUbiquiti(ActionEvent event) {
        query = "select * from stock_product where product_brand = 'Ubiquiti'";
        BuildTableData(query);
    }

    @FXML
    private void viewMikrotik(ActionEvent event) {
        query = "select * from stock_product where product_brand = 'Mikrotik'";
        BuildTableData(query);
    }

    @FXML
    private void viewCisco(ActionEvent event) {
        query = "select * from stock_product where product_brand = 'Cisco'";
        BuildTableData(query);
    }
    
    private void clearField(){
        txtProductID.setText("");
        txtProductName.setText("");
        txtProductCode.setText("");
        cmbProductCat.setValue(null);
        cmbProductBrand.setValue(null);
        txtProductPrice.setText("");
    }
    
    private boolean notEmpty(){
        return ((!txtProductID.getText().isEmpty() || !txtProductID.getText().equals(""))
                &&((!txtProductName.getText().isEmpty() || !txtProductName.getText().equals("")) 
                && ((!txtProductCode.getText().isEmpty() || !txtProductCode.getText().equals("")) 
                && ((!cmbProductCat.getSelectionModel().isEmpty())
                && ((!cmbProductBrand.getSelectionModel().isEmpty())
                && ((!txtProductPrice.getText().isEmpty() || !txtProductPrice.getText().equals(""))))))));
            
    }
    
    private void loadFromDatabase(){
        data.clear();
        con = db.connectDB();
        try {     
            pst = con.prepareStatement("select * from stock_product");
            rs =  pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String pName = rs.getString(2);
                String pCode = rs.getString(3);
                String pCat = rs.getString(4);
                String pBrand = rs.getString(5);
                Double pPrice = rs.getDouble(6);
                data.add(new productmodel(id, pName, pCode, pCat, pBrand, pPrice));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableProduct.setItems(data);
        
    } 
    
    private void buildProductTable(){
        
        _productID.setCellValueFactory(new PropertyValueFactory<>("productid"));
        _productName.setCellValueFactory(new PropertyValueFactory<>("productname"));
        _productCode.setCellValueFactory(new PropertyValueFactory<>("productcode"));
        _productCategory.setCellValueFactory(new PropertyValueFactory<>("productcat"));
        _productBrand.setCellValueFactory(new PropertyValueFactory<>("productbrand"));
        _productPrice.setCellValueFactory(new PropertyValueFactory<>("productprice"));
    }
    
    private void setProductValueToTextField(){
        
        _tableProduct.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
          public void handle(MouseEvent event){
              productmodel pm = _tableProduct.getItems().get(_tableProduct.getSelectionModel().getSelectedIndex());
              txtProductID.setText(pm.getProductid().toString());
              txtProductName.setText(pm.getProductname());
              txtProductCode.setText(pm.getProductcode());
              cmbProductCat.setValue(pm.getProductcat());
              cmbProductBrand.setValue(pm.getProductbrand());
              txtProductPrice.setText(pm.getProductprice().toString());
              btnSave.setDisable(true);
          }
        });
    }
    
    private void searchAllProduct(){
        
        txtSearchAll.setOnKeyReleased(e ->{
            //String sql = "select * from stock_product where product_id = ?";
            if(txtSearchAll.getText().equals("")){
                loadFromDatabase();
            }else{
                data.clear();
                String sql = "select * from stock_product where product_id like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_product where product_name like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_product where product_code like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_product where product_category like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_product where product_brand like '%"+txtSearchAll.getText()+"%'"
                        + " UNION  select * from stock_product where price like '%"+txtSearchAll.getText()+"%'";
                
                try {
                    pst = con.prepareStatement(sql);
                    /*pst.setString(1, txtProductID.getText());
                    pst.executeQuery();*/
                    rs = pst.executeQuery();
                    //
                        while(rs.next()){
                            int id = rs.getInt(1);
                            String pName = rs.getString(2);
                            String pCode = rs.getString(3);
                            String pCat = rs.getString(4);
                            String pBrand = rs.getString(5);
                            Double pPrice = rs.getDouble(6);
                            data.add(new productmodel(id, pName, pCode, pCat, pBrand, pPrice));
                        }
                        _tableProduct.setItems(data);
                    
                } catch (SQLException ex) {
                    Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    private void setInfo(String product_id){
        try {
            boolean exists = false;
            con = db.connectDB();
            String sql = "select * from stock_product where product_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, product_id);
            rs = pst.executeQuery();
            while(rs.next()){
                txtProductID.setText(rs.getString(1));
                txtProductName.setText(rs.getString(2));
                txtProductCode.setText(rs.getString(3));
                cmbProductCat.setValue(rs.getString(4));
                cmbProductBrand.setValue(rs.getString(5));
                txtProductPrice.setText(rs.getString(6));
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                deleteID = rs.getInt(1);
                PRODUCT_ID = Integer.parseInt(product_id);
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
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void BuildTableData(String query){
        try {
            data = FXCollections.observableArrayList();
            con = db.connectDB();
            rs = con.createStatement().executeQuery(query);
            if(rs.next()){
                
                while(rs.next()){
                    int id = rs.getInt(1);
                    String pName = rs.getString(2);
                    String pCode = rs.getString(3);
                    String pCat = rs.getString(4);
                    String pBrand = rs.getString(5);
                    Double pPrice = rs.getDouble(6);
                    data.add(new productmodel(id, pName, pCode, pCat, pBrand, pPrice));
                }
            }else{
                AlertMaker.showErrorAlert("Information", "Empty Search");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        buildProductTable();
        
        _tableProduct.setItems(null);
        _tableProduct.setItems(data);
    }
}