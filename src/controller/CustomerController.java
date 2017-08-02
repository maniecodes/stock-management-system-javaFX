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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import stockbase.Database;
import stockmodel.customermodel;
import tools.AlertMaker;


public class CustomerController implements Initializable {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Database db;
    
    static int deleteID;
    public static int CUSTOMER_ID;
    private static String query = "";
    
    private ObservableList<customermodel> data;
    
    @FXML
    private TextField txtCustomerID;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TextField txtCustomerContact;
    @FXML
    private TextField txtCustomerEmail;
    @FXML
    private ComboBox<String> cmbCustomerLocation;
    @FXML
    private TextField txtCustomerAddress;
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
    private RadioButton rbOthers;
    @FXML
    private TableView<customermodel> _tableCustomer;
    @FXML
    private TableColumn<customermodel, Integer> _customerID;
    @FXML
    private TableColumn<customermodel, String> _customerName;
    @FXML
    private TableColumn<customermodel, String> _customerContact;
    @FXML
    private TableColumn<customermodel, String> _customerLocation;
    @FXML
    private TableColumn<customermodel, String> _customerAddress;
    @FXML
    private TableColumn<customermodel, String> _customerEmail;
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
        ObservableList<String> Location = FXCollections.observableArrayList("", "Ikeja",
                "Lagos", "Abuja", "Kano");
        cmbCustomerLocation.setItems(Location);
        cmbCustomerLocation.getSelectionModel().selectFirst();
        
        //txtCustomerID.setText("1");
        data = FXCollections.observableArrayList();
        buildCustomerTable();
        loadFromDatabase();
        setCustomerValueToTextField();
        searchAllCustomer();
    }    

    @FXML
    private void handleNew(ActionEvent event) {
        btnSave.setDisable(false);
        clearField();
        try {
            if(db.getCustomerID() <= 0){
                txtCustomerID.setText("1");
            }
            txtCustomerID.setText(String.valueOf(db.getCustomerID()));

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        con = db.connectDB();
        String customerId = txtCustomerID.getText();
        String customerName = txtCustomerName.getText();
        String customerCon = txtCustomerContact.getText();
        String customerLoc = cmbCustomerLocation.getValue();
        String customerAdd = txtCustomerAddress.getText();
        String customerEmail = txtCustomerEmail.getText();
        
        try{
            String sql = "update stock_customer set customer_name = ?, customer_contact = ?, "
                    + "customer_location = ?, customer_address = ?, customer_email = ? where customer_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, customerName);
            pst.setString(2, customerCon);
            pst.setString(3, customerLoc);
            pst.setString(4, customerAdd);
            pst.setString(5, customerEmail); 
            pst.setInt(6, Integer.parseInt(customerId));
            
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Customer Updated Successfully");
                loadFromDatabase();
                clearField();
            }else{
                AlertMaker.showErrorAlert("Error", "Could Not Update");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void handleSave(ActionEvent event) {
        con = db.connectDB();
        String customerId = txtCustomerID.getText();
        String customerName = txtCustomerName.getText();
        String customerCon = txtCustomerContact.getText();
        String customerLoc = cmbCustomerLocation.getValue();
        String customerAdd = txtCustomerAddress.getText();
        String customerEmail = txtCustomerEmail.getText();
        
        String sql = "insert into stock_customer(customer_id, customer_name,"
                + "customer_contact, customer_location, customer_address, customer_email) values("
                + "null, ?,?,?,?,?)";
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, customerName);
            pst.setString(2, customerCon);
            pst.setString(3, customerLoc);
            pst.setString(4, customerAdd);
            pst.setString(5, customerEmail);  
            if(notEmpty()){
                int success = pst.executeUpdate();
                if(success == 1){
                    AlertMaker.showSimpleAlert("Success", "Customer inserted successfully");
                    clearField();
                    buildCustomerTable();
                    loadFromDatabase();
                }
            }else{
                AlertMaker.showSimpleAlert("Information", "All fields requried");
            }        
                     
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        con = db.connectDB();
        String sql = "delete from stock_customer where customer_id = ?";
        
        try {   
            pst = con.prepareStatement(sql);
            pst.setString(1, txtCustomerID.getText());
            int i = pst.executeUpdate();
            if(i == 1){
                AlertMaker.showSimpleAlert("Information", "Customer deleted successfully");
                loadFromDatabase();
                clearField();
            }else{
                AlertMaker.showErrorAlert("Error", "Could not delete customer");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
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
            setCustomerInfo(txtSearchID.getText());
        }
    }
    
     private boolean notEmpty(){
        return((!txtCustomerID.getText().isEmpty() || !txtCustomerID.getText().equals(""))
                &&((!txtCustomerName.getText().isEmpty() || !txtCustomerName.getText().equals(""))));
    }
    
    private void searchAllCustomer(){
        txtSearchAll.setOnKeyReleased(e ->{
        if(txtSearchAll.getText().equals("")){
           loadFromDatabase();
       }else{
           data.clear();
           String sql = "select * from stock_customer where customer_id like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_customer where customer_name like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_customer where customer_contact like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_customer where customer_location like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_customer where customer_address like '%"+txtSearchAll.getText()+"%'"
                   + " UNION  select * from stock_customer where customer_email like '%"+txtSearchAll.getText()+"%'";

           try {
                pst = con.prepareStatement(sql);

                rs = pst.executeQuery();
                    while(rs.next()){
                         int id = rs.getInt(1);
                String sName = rs.getString(2);
                String sCont = rs.getString(3);
                String sLoc =  rs.getString(4);
                String sAdd =  rs.getString(5);
                String sEmail =  rs.getString(6);
                
                data.add(new customermodel(id, sName, sCont, sLoc, sAdd, sEmail));
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
                _tableCustomer.setItems(data);

            }
        });
    }
    
    
    private void setCustomerInfo(String customer_id){
        try {
            boolean exists = false;
            con = db.connectDB();
            String sql = "select * from stock_customer where customer_id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, customer_id);
            rs = pst.executeQuery();
            while(rs.next()){
                txtCustomerID.setText(rs.getString(1));
                txtCustomerName.setText(rs.getString(2));
                txtCustomerContact.setText(rs.getString(3));
                cmbCustomerLocation.setValue(rs.getString(4));
                txtCustomerAddress.setText(rs.getString(5));
                txtCustomerEmail.setText(rs.getString(6));
                btnEdit.setDisable(false);
                btnDelete.setDisable(false);
                deleteID = rs.getInt(1);
                CUSTOMER_ID = Integer.parseInt(customer_id);
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
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setCustomerValueToTextField(){
        
        _tableCustomer.setOnMouseClicked(new EventHandler<MouseEvent>(){
          @Override
          public void handle(MouseEvent event){
              customermodel cm = _tableCustomer.getItems().get(_tableCustomer.getSelectionModel().getSelectedIndex());

              txtCustomerID.setText(cm.getCustomerid().toString());
              txtCustomerName.setText(cm.getCustomername());
              txtCustomerContact.setText(cm.getCustomercon());
              cmbCustomerLocation.setValue(cm.getCustomerloc());
              txtCustomerAddress.setText(cm.getCustomeradd());
              txtCustomerEmail.setText(cm.getCustomeremail());
              
              btnSave.setDisable(true);
          }
        });
    }
    
    private void buildCustomerTable(){
        _customerID.setCellValueFactory(new PropertyValueFactory<>("customerid"));
        _customerName.setCellValueFactory(new PropertyValueFactory<>("customername"));
        _customerContact.setCellValueFactory(new PropertyValueFactory<>("customercon"));
        _customerLocation.setCellValueFactory(new PropertyValueFactory<>("customerloc"));
        _customerAddress.setCellValueFactory(new PropertyValueFactory<>("customeradd"));
        _customerEmail.setCellValueFactory(new PropertyValueFactory<>("customeremail"));
    
    }
    private void loadFromDatabase(){
        data.clear();
        con = db.connectDB();
        try {     
            pst = con.prepareStatement("select * from stock_customer");
            rs =  pst.executeQuery();
            while(rs.next()){
                int id = rs.getInt(1);
                String sName = rs.getString(2);
                String sCont = rs.getString(3);
                String sLoc =  rs.getString(4);
                String sAdd =  rs.getString(5);
                String sEmail =  rs.getString(6);
                
                data.add(new customermodel(id, sName, sCont, sLoc, sAdd, sEmail));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        _tableCustomer.setItems(data);
        
    } 
    private void clearField(){
        txtCustomerID.setText("");
        txtCustomerName.setText("");
        txtCustomerContact.setText("");
        cmbCustomerLocation.setValue(null);
        txtCustomerAddress.setText("");
        txtCustomerEmail.setText("");
    }

    
}
