/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import stockbase.Database;


public class LoginController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnLogin;
    @FXML
    private Hyperlink hlForgotPassword;
    @FXML
    private Hyperlink hlDatabaseSetup;
    @FXML
    private Label lblError;

    private Database db;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    @FXML
    private AnchorPane anchorPane;
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db = new Database();
    }   
    
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        if(txtUsername.getText().equals(getUsername())&&txtPassword.getText().equals(getPassword())){
            Stage stage = (Stage)anchorPane.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/stockmanagement/Home.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else{
            System.out.println("Cant connect");
        }
    }   
    
    
    
    
    @FXML
    private void handleCancel(ActionEvent event) {
    }


    @FXML
    private void handleForgotPassword(ActionEvent event) {
    }

    @FXML
    private void handleDatabaseSetup(ActionEvent event) {
    }
    
    
    private String getUsername(){
        String username = "";
        con = db.connectDB();
        try { 
            pst = con.prepareStatement("Select username from stock_user where username = ?");
            pst.setString(1, txtUsername.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                username = rs.getString("username");
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return username;
    }
    
    private String getPassword(){
        String password = "";
        try { 
            con = db.connectDB();
            pst = con.prepareStatement("Select password from stock_user where password = ?");
            pst.setString(1, txtPassword.getText());
            rs = pst.executeQuery();
            if(rs.next()){
                password = rs.getString("password");
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return password;
    }
}
