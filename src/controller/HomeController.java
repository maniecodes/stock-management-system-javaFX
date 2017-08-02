/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Manasseh
 */
public class HomeController implements Initializable {


    AnchorPane home, product, supplier, stock, customer, sales,purchase;
    
    @FXML
    private AnchorPane holderPane;
    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnProduct;
    @FXML
    private JFXButton btnSupplier;
    @FXML
    private JFXButton btnStock;
    @FXML
    private JFXButton btnSales;
    @FXML
    private JFXButton btnPurchase;
    @FXML
    private JFXButton btnCustomer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            home = FXMLLoader.load(getClass().getResource("/stockmanagement/Menu.fxml"));
            setNode(home);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }    
    
    

    @FXML
    private void homeClick(ActionEvent event) {
         try {
            home = FXMLLoader.load(getClass().getResource("/stockmanagement/Menu.fxml"));
            setNode(home);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void productClick(ActionEvent event) {
        try {
            product = FXMLLoader.load(getClass().getResource("/view/Product.fxml"));
            setNode(product);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void supplierClick(ActionEvent event) {
        try {
            supplier = FXMLLoader.load(getClass().getResource("/view/Supplier.fxml"));
            setNode(supplier);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void stockClick(ActionEvent event) {
        try {
            stock = FXMLLoader.load(getClass().getResource("/view/Stock.fxml"));
            setNode(stock);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void customerClick(ActionEvent event) {
        try {
            customer = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
            setNode(customer);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void salesClick(ActionEvent event) {
        try {
            sales = FXMLLoader.load(getClass().getResource("/view/Sales.fxml"));
            setNode(sales);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void purchaseClick(ActionEvent event) {
        try {
            purchase = FXMLLoader.load(getClass().getResource("/view/Purchase.fxml"));
            setNode(purchase);
        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setNode(Node  node){
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
        
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }


    
}
