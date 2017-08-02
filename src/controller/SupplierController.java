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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import stockbase.Database;
import stockmodel.productmodel;
import stockmodel.suppliermodel;
import tools.AlertMaker;
import tools.Validate;

/**
 * FXML Controller class
 *
 * @author Manasseh
 */
public class SupplierController implements Initializable {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Database db;

    
    static int deleteID;
    public static int SUPPLIER_ID;
    private static String query = "";
    
    @FXML
    private TextField txtSupplierID;
    @FXML
    private TextField txtSupplierFN;
    @FXML
    private TextField txtSupplierContact;
    @FXML
    private TextField txtSupplierEmail;
    @FXML
    private TextField txtSupplierLN;
    @FXML
    private TextField txtSupplierLocation;
    @FXML
    private TextField txtSupplierAddress;
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
    private RadioButton rbDefault;
    @FXML
    private RadioButton rbIkeja;
    @FXML
    private RadioButton rbLagos;
    @FXML
    private RadioButton rbOthers;
    
    private ObservableList<suppliermodel> data;
    
    @FXML
    private TableView<suppliermodel> _tableSupplier;
    @FXML
    private TableColumn<suppliermodel, Integer> _supplierID;
    @FXML
    private TableColumn<suppliermodel, String> _supplierName;
    @FXML
    private TableColumn<suppliermodel, String> _supplierContact;
    @FXML
    private TableColumn<suppliermodel, String> _supplierLocation;
    @FXML
    private TableColumn<suppliermodel, String> _supplierAddress;
    @FXML
    private TableColumn<suppliermodel, String> _supplierEmail;
    @FXML
    private JFXTextField txtSearchAll;
    @FXML
    private TextField txtSearchID;
    @FXML
    private Button btnSearchID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        db = new Database();
        //txtSupplierID.setText("1");
        
        data = FXCollections.observableArrayList();
        buildSupplierTable();
        loadFromDatabase();
        setSupplierValueToTextField();
        searchAllSupplier();
    }    

    @FXML
    private void handleNew(ActionEvent event) {
        btnSave.setDisable(false);
        //clearField();
        try{
            if(db.getSupplierID() <= 0){
                txtSupplierID.setText("1");
            }
            txtSupplierID.setText(String.valueOf(db.getSupplierID()));
        } catch (SQLException ex) {
            Logger.getLogger(SupplierController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        con = db.connectDB();
        String Id = txtSupplierID.getText();
        String Firstname = txtSupplierFN.getText();
        String Lastname = txtSupplierLN.getText();
        String Contact = txtSupplierContact.getText();
        String Location = txtSupplierLocation.getText();
        String Address = txtSupplierAddress.getText();
        String Email = txtSupplierEmail.getText();
        try{
            String sql = "update stock_supplier set supplier_firstname = ?, supplier_lastname = ?, "
                    + "supplier_contact = ?, supplier_location = ?, supplier_address = ?,"
                    + "supplier_email = ? where supplier_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, Firstname);
            pst.setString(2, Lastname);
            pst.setString(3, Contact);
            pst.setString(4, Location);
            pst.setString(5, Address);
            pst.setString(6, Email);
            pst.setInt(7, Integer.parseInt(Id));
            
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
        String Id = txtSupplierID.getText();
        String Firstname = txtSupplierFN.getText();
        String Lastname = txtSupplierLN.getText();
        String Contact = txtSupplierContact.getText();
        String Location = txtSupplierLocation.getText();
        String Address = txtSupplierAddress.getText();
        String Email = txtSupplierEmail.getText();
        
        String sql = "insert into stock_supplier(supplier_id, supplier_firstname, supplier_lastname,"
                + "supplier_contact, supplier_location, supplier_address, supplier_email) values(null,?,?,?,?,?,?)";
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1, Firstname);
            pst.setString(2, Lastname);
            pst.setString(3, Contact);
            pst.setString(4, Location);
            pst.setString(5, Address);
            pst.setString(6, Email);
            if(notEmpty()){
                int success = pst.executeUpdate();
                if(success == 1){
                    AlertMaker.showSimpleAlert("Success", "Product inserted successfully");
                    clearField();
                    buildSupplierTable();
                    loadFromDatabase();
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
        String sql = "delete from stock_supplier where supplier_id = ?";
        
        try {   
            pst = con.prepareStatement(sql);
            pst.setString(1, txtSupplierID.getText());
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Suppleir deleted successfully");
                loadFromDatabase();
                clearField();
            }else{
                AlertMaker.showErrorAlert("Error", "Could not delete supplier");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    @FXML
    private void handleClear(ActionEvent event) {
        clearField();
    }

    @FXML
    private void viewDefault(ActionEvent event) {
        query = "select * from stock_supplier";
        BuildTableData(query);
    }

    @FXML
    private void viewIkeja(ActionEvent event) {
        query = "select * from stock_supplier where supplier_location = 'Ikeja'";
        BuildTableData(query);
    }

    @FXML
    private void viewLagos(ActionEvent event) {
        query = "select * from stock_supplier where supplier_location = 'Lagos'";
        BuildTableData(query);
    }

    @FXML
    private void viewOthers(ActionEvent event) {
        query = "select * from stock_supplier where supplier_location = 'Others'";
        BuildTableData(query);
    }

    @FXML
    private void searchById(ActionEvent event) {
        clearField();
        if(!txtSearchID.getText().isEmpty() && txtSearchID.getText() != null){
            setInfo(txtSearchID.getText());
        }
    }
    
    private boolean notEmpty(){
        return((!txtSupplierID.getText().isEmpty() || !txtSupplierID.getText().equals(""))
                &&((!txtSupplierFN.getText().isEmpty() || !txtSupplierFN.getText().equals(""))));
    }
    
    private void loadFromDatabase(){
        data.clear();
        con = db.connectDB();
        try {     
            pst = con.prepareStatement("select * from stock_supplier");
            rs =  pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                    String sFN = rs.getString(2);
                    String sLN = rs.getString(3);
                    String sCon = rs.getString(4);
                    String sLocation = rs.getString(5);
                    String sAddress = rs.getString(6);
                    String sEmail = rs.getString(7);
                    String names = sFN + " " + sLN;
                    data.add(new suppliermodel(id, names, sCon, sLocation, sAddress, sEmail));
                }
            }catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableSupplier.setItems(data);
        
    }
    
    private void searchAllSupplier(){
        txtSearchAll.setOnKeyReleased(e ->{
        if(txtSearchAll.getText().equals("")){
           loadFromDatabase();
       }else{
           data.clear();
           String sql = "select * from stock_supplier where supplier_id like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_firstname like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_lastname like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_contact like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_location like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_address like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_supplier where supplier_email like '%"+txtSearchAll.getText()+"%'";

           try {
                pst = con.prepareStatement(sql);

                rs = pst.executeQuery();
                //if(rs.next()){
                    while(rs.next()){
                        int id = rs.getInt(1);
                        String sFN = rs.getString(2);
                        String sLN = rs.getString(3);
                        String sCon = rs.getString(4);
                        String sLocation = rs.getString(5);
                        String sAddress = rs.getString(6);
                        String sEmail = rs.getString(7);
                        String names = sFN + " " + sLN;
                        data.add(new suppliermodel(id, names, sCon, sLocation, sAddress, sEmail));
                 }  
                    _tableSupplier.setItems(data);
               /* }else{
                    AlertMaker.showErrorAlert("Error", "Search Does not exist");
                    txtSearchAll.setText("");
                    loadFromDatabase();
                }*/
            } catch (SQLException ex) {
                Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    
    private void buildSupplierTable(){
        _supplierID.setCellValueFactory(new PropertyValueFactory<>("supplierid"));
        _supplierName.setCellValueFactory(new PropertyValueFactory<>("suppliername"));
        _supplierContact.setCellValueFactory(new PropertyValueFactory<>("suppliercontact"));
        _supplierLocation.setCellValueFactory(new PropertyValueFactory<>("supplierlocation"));
        _supplierAddress.setCellValueFactory(new PropertyValueFactory<>("supplieraddress"));
        _supplierEmail.setCellValueFactory(new PropertyValueFactory<>("supplieremail"));
    }
    
     private void clearField(){
        txtSupplierID.setText("");
        txtSupplierFN.setText("");
        txtSupplierLN.setText("");
        txtSupplierContact.setText("");
        txtSupplierLocation.setText("");
        txtSupplierAddress.setText("");
        txtSupplierEmail.setText("");
    }
     
     private void setInfo(String supplier_id){
        try {
            boolean exists = false;
            con = db.connectDB();
            String sql = "select * from stock_supplier where supplier_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, supplier_id);
            rs = pst.executeQuery();
            while(rs.next()){
                txtSupplierID.setText(rs.getString(1));
                txtSupplierFN.setText(rs.getString(2));
                txtSupplierLN.setText(rs.getString(3));
                txtSupplierContact.setText(rs.getString(4));
                txtSupplierLocation.setText(rs.getString(5));
                txtSupplierAddress.setText(rs.getString(6));
                txtSupplierEmail.setText(rs.getString(7));
                
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                deleteID = rs.getInt(1);
                SUPPLIER_ID = Integer.parseInt(supplier_id);
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
                    String sFN = rs.getString(2);
                    String sLN = rs.getString(3);
                    String sCon = rs.getString(4);
                    String sLocation = rs.getString(5);
                    String sAddress = rs.getString(6);
                    String sEmail = rs.getString(7);
                    String names = sFN + " " + sLN;
                    data.add(new suppliermodel(id, names, sCon, sLocation, sAddress, sEmail));
                }
            }else{
                AlertMaker.showErrorAlert("Information", "Empty Search");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductController.class.getName()).log(Level.SEVERE, null, ex);
        }
        buildSupplierTable();
        
        _tableSupplier.setItems(null);
        _tableSupplier.setItems(data);
    }
     
     private void setSupplierValueToTextField(){
        
        _tableSupplier.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
          public void handle(MouseEvent event){
              suppliermodel pm = _tableSupplier.getItems().get(_tableSupplier.getSelectionModel().getSelectedIndex());
              
              String fullname = pm.getSuppliername();
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
              
              txtSupplierID.setText(pm.getSupplierid().toString());
              txtSupplierFN.setText(firstname);
              txtSupplierLN.setText(lastname);
              txtSupplierContact.setText(pm.getSuppliercontact());
              txtSupplierLocation.setText(pm.getSupplierlocation());
              txtSupplierAddress.setText(pm.getSupplieraddress());
              txtSupplierEmail.setText(pm.getSupplieremail());
              
              btnSave.setDisable(true);
          }
        });
    }
    
}
