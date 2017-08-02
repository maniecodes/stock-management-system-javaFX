/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockbase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 *
 * @author Manasseh
 */
public class Database {
    
    private PreparedStatement ps;
    private ResultSet rs;
    private Connection conn;
    private Statement st;
    
    public Database(){
        
    }
    
    public Connection connectDB() {
        try {
            String host = "localhost";
            String username = "root";
            String password = "";
            String database = "stock";
            String port = "3306";

            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            

            return connection;
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }
    public boolean isUser(String user, String pass) {
        try {
            conn = connectDB();
            String query = "select * from stock_user where username=? and password=? ";
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
    
    public int getProductID() throws SQLException {

        String query = "select product_id  from stock_product ORDER BY product_id asc";
        conn = connectDB();
        st = conn.createStatement();
        rs = st.executeQuery(query);
        rs.last();
        return rs.getInt(1) + 1;

    } 
    public int getSupplierID() throws SQLException {

        String query = "select supplier_id  from stock_supplier ORDER BY supplier_id asc";
        conn = connectDB();
        st = conn.createStatement();
        rs = st.executeQuery(query);
        rs.last();
        return rs.getInt(1) + 1;
    } 
    public int getStockID() throws SQLException {

        String query = "select stock_id  from stocks ORDER BY stock_id asc";
        conn = connectDB();
        st = conn.createStatement();
        rs = st.executeQuery(query);
        rs.last();
        return rs.getInt(1) + 1;
    } 
    public int getCustomerID() throws SQLException {

        String query = "select customer_id  from stock_customer ORDER BY customer_id asc";
        conn = connectDB();
        st = conn.createStatement();
        rs = st.executeQuery(query);
        rs.last();
        return rs.getInt(1) + 1;
    } 
    public int getSalesID() throws SQLException {

        String query = "select sales_id  from stock_sales ORDER BY sales_id asc";
        conn = connectDB();
        st = conn.createStatement();
        rs = st.executeQuery(query);
        rs.last();
        return rs.getInt(1) + 1;
    } 
    
     public int getPurchaseID() throws SQLException {

        String query = "select purchase_id  from stock_purchase ORDER BY purchase_id asc";
        conn = connectDB();
        st = conn.createStatement();
        rs = st.executeQuery(query);
        rs.last();
        return rs.getInt(1) + 1;
    } 
    
    
    public boolean isDouble(TextField tf) {
        boolean b = false;
        if (!(tf.getText() == null || tf.getText().length() == 0)) {
            try {
                // Do all the validation you need here such as
                double value = Double.parseDouble(tf.getText());
                if (value > 0.5) {
                    b = true;
                }
            } catch (NumberFormatException e) {
               tools.AlertMaker.showErrorAlert("Invalid Input", "Only Numbers are allowed here");
            }
        }
        return b;
    }
    
}
