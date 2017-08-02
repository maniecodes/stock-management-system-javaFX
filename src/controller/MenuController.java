/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import static java.time.temporal.TemporalAdjusters.firstDayOfYear;
import static java.time.temporal.TemporalAdjusters.lastDayOfYear;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import stockbase.Database;

/**
 * FXML Controller class
 *
 * @author Manasseh
 */
public class MenuController implements Initializable {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Database db;
    
    @FXML
    private Label lblDailySales;
    @FXML
    private Label lblCreditDailySales;
    @FXML
    private Label lblDailySalesProfit;
    @FXML
    private Label lblYearlySales;
    @FXML
    private Label lblDailyPurchase;
    @FXML
    private Label lblCreditDailyPurchase;
    @FXML
    private Label lblDailyPurchaseBal;
    @FXML
    private Label lblYearlyPurchase;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        db = new Database();
        getDailySales();
        getDailyPurchase();
    }    
    
    public void getDailySales(){
        con = db.connectDB();
        try {
            String sql = "select sum(coalesce(sales_total,0)) as total from stock_sales where sales_date <= NOW() and sales_date >= Date_add(Now(),interval - 1 day)";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getString(1)!= null){
                    Double totaSales = Double.parseDouble(rs.getString(1));
                    String totalSales = NumberFormat.getNumberInstance(Locale.US).format(totaSales);
                    String tSales= "₦" + totalSales;
                    lblDailySales.setText(tSales); 
                    getCreditSales();
                }else{
                    lblDailySales.setText("₦"+ 0.00);
                }
                 
            }
           } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getCreditSales(){
        
        con = db.connectDB();
        try {
            String sql = "select sum(coalesce(sales_apaid,0)) as total from stock_sales where sales_date <= NOW() and sales_date >= Date_add(Now(),interval - 1 day) group by sales_date";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                
                Double paidSales = Double.parseDouble(rs.getString(1));
                String tSales = lblDailySales.getText();
                tSales = tSales.startsWith("₦") ? tSales.substring(1) : tSales;
                
                NumberFormat format = NumberFormat.getNumberInstance();
                Number totalSales = format.parse(tSales);
 
                Double  crediSales = totalSales.doubleValue() - paidSales;
                String creditSales = NumberFormat.getNumberInstance(Locale.US).format(crediSales);
                String cSales = "₦"+ creditSales;
                 
                lblCreditDailySales.setText(cSales);
                dailySalesProfit();
            }
           } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void dailySalesProfit(){
        try {
            String tSales = lblDailySales.getText();
            String cSales = lblCreditDailySales.getText();
            tSales = tSales.startsWith("₦") ? tSales.substring(1) : tSales;
            
            NumberFormat format = NumberFormat.getNumberInstance();
            Number totalSales = format.parse(tSales);

            cSales = cSales.startsWith("₦") ? cSales.substring(1) : cSales;
            
            NumberFormat format2 = NumberFormat.getNumberInstance();
            Number creditSales = format2.parse(cSales);
            
            Double pft = totalSales.doubleValue() - creditSales.doubleValue();
            String profit = NumberFormat.getNumberInstance(Locale.US).format(pft);
            String dailyProfit = "₦"+ profit;
            lblDailySalesProfit.setText(dailyProfit);
            totalSales();
        } catch (ParseException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        
    }
    
    private void  totalSales(){
        try {
            con = db.connectDB();
            LocalDate now = LocalDate.now(); 
            LocalDate firstDay = now.with(firstDayOfYear()); 
            LocalDate lastDay = now.with(lastDayOfYear());
            String sql = "SELECT sum(COALESCE(`sales_total`, 0)) as sales FROM stock_sales WHERE (sales_date BETWEEN '"+firstDay+"' AND '"+lastDay+"')";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                Double ySales = Double.parseDouble(rs.getString(1));
                String yearSales = NumberFormat.getNumberInstance(Locale.US).format(ySales);
                String yearlySales = "₦" + yearSales;
                lblYearlySales.setText(yearlySales);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     public void getDailyPurchase(){
        con = db.connectDB();
        try {
            String sql = "select sum(coalesce(purchase_total,0)) as total from stock_purchase where purchase_date <= NOW() and purchase_date >= Date_add(Now(),interval - 1 day)";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                if(rs.getString(1)!= null){
                    Double totaPurchase = Double.parseDouble(rs.getString(1));
                    String totalPurchase = NumberFormat.getNumberInstance(Locale.US).format(totaPurchase);
                    String tPurchase = "₦" + totalPurchase;
                    lblDailyPurchase.setText(tPurchase); 
                    getCreditPurchase();
                }else{
                    lblDailyPurchase.setText("₦"+ 0.00);
            
                }
                
                }
           } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    private void getCreditPurchase(){

        con = db.connectDB();
        try {
            String sql = "select sum(coalesce(purchase_apaid,0)) as total from stock_purchase where purchase_date <= NOW() and purchase_date >= Date_add(Now(),interval - 1 day) group by purchase_date";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                
                Double paidPurchase = Double.parseDouble(rs.getString(1));
                String tPurchase = lblDailyPurchase.getText();
                tPurchase = tPurchase.startsWith("₦") ? tPurchase.substring(1) : tPurchase;
                
                NumberFormat format = NumberFormat.getNumberInstance();
                Number totalPurchase = format.parse(tPurchase);
 
                Double  crediPurchase = totalPurchase.doubleValue() - paidPurchase;
                String creditPurchase = NumberFormat.getNumberInstance(Locale.US).format(crediPurchase);
                String cPurchase = "₦"+ creditPurchase;
                 
                lblCreditDailyPurchase.setText(cPurchase);
                dailyPurchaseBal();
            }
           } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void dailyPurchaseBal(){
        try {
            String tPurchase = lblDailyPurchase.getText();
            String cPurchase = lblCreditDailyPurchase.getText();
            tPurchase = tPurchase.startsWith("₦") ? tPurchase.substring(1) : tPurchase;
            
            NumberFormat format = NumberFormat.getNumberInstance();
            Number totalPurchase = format.parse(tPurchase);

            cPurchase = cPurchase.startsWith("₦") ? cPurchase.substring(1) : cPurchase;
            
            NumberFormat format2 = NumberFormat.getNumberInstance();
            Number creditPurchase = format2.parse(cPurchase);
            
            Double bal = totalPurchase.doubleValue() - creditPurchase.doubleValue();
            String balance = NumberFormat.getNumberInstance(Locale.US).format(bal);
            String dailyBalance = "₦"+ balance;
            lblDailyPurchaseBal.setText(dailyBalance);
            totalPurchase();
        } catch (ParseException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        
    }
    
    private void  totalPurchase(){
        try {
            con = db.connectDB();
            LocalDate now = LocalDate.now(); 
            LocalDate firstDay = now.with(firstDayOfYear()); 
            LocalDate lastDay = now.with(lastDayOfYear());
            String sql = "SELECT sum(COALESCE(`purchase_total`, 0)) as purchase FROM stock_purchase WHERE (purchase_date BETWEEN '"+firstDay+"' AND '"+lastDay+"')";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            if(rs.next()){
                Double yPurchase = Double.parseDouble(rs.getString(1));
                String yearPurchase = NumberFormat.getNumberInstance(Locale.US).format(yPurchase);
                String yearlyPurchase = "₦" + yearPurchase;
                lblYearlyPurchase.setText(yearlyPurchase);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
