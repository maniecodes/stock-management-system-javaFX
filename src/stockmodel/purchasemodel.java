/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Manasseh
 */
public class purchasemodel {
    private SimpleIntegerProperty purchaseid;
    private SimpleStringProperty purchasepn;
    private SimpleStringProperty  purchasepc;
    private SimpleStringProperty purchasecn;
    private SimpleStringProperty purchasecc;
    private SimpleStringProperty purchasedate;
    private SimpleIntegerProperty purchaseqty;
    private SimpleDoubleProperty purchaseprice;
    private SimpleDoubleProperty purchasetotal;
    private SimpleStringProperty purchasept;
    private SimpleDoubleProperty purchaseap;
    private SimpleDoubleProperty purchasebal;
    
    
    public purchasemodel(Integer purchaseid, String purchasepn, String purchasepc, String purchasecn, String purchasecc, 
            String purchasedate, Integer purchaseqty, Double purchaseprice, Double purchasetotal, String purchasept,
            Double purchaseap, Double purchasebal){
        this.purchaseid = new SimpleIntegerProperty(purchaseid);
        this.purchasepn = new SimpleStringProperty(purchasepn);
        this.purchasepc = new SimpleStringProperty(purchasepc);
        this.purchasecn = new SimpleStringProperty(purchasecn);
        this.purchasecc = new SimpleStringProperty(purchasecc);
        this.purchasedate = new SimpleStringProperty(purchasedate);
        this.purchaseqty = new SimpleIntegerProperty(purchaseqty);
        this.purchaseprice = new SimpleDoubleProperty(purchaseprice);
        this.purchasetotal = new SimpleDoubleProperty(purchasetotal);
        this.purchasept = new SimpleStringProperty(purchasept);
        this.purchaseap = new SimpleDoubleProperty(purchaseap);
        this.purchasebal = new SimpleDoubleProperty(purchasebal);  
    }
    
    public purchasemodel(Integer purchaseid, String purchasepn, String purchasepc,
            Integer purchaseqty, Double purchaseprice, Double purchasetotal,
            Double purchaseap, Double purchasebal, String purchasedate){
        this.purchaseid = new SimpleIntegerProperty(purchaseid);
        this.purchasepn = new SimpleStringProperty(purchasepn);
        this.purchasepc = new SimpleStringProperty(purchasepc);
        this.purchaseqty = new SimpleIntegerProperty(purchaseqty);
        this.purchaseprice = new SimpleDoubleProperty(purchaseprice);
        this.purchasetotal = new SimpleDoubleProperty(purchasetotal);
        this.purchaseap = new SimpleDoubleProperty(purchaseap);
        this.purchasebal = new SimpleDoubleProperty(purchasebal); 
        this.purchasedate = new SimpleStringProperty(purchasedate);
    }


    public Integer getPurchaseid() {
        return purchaseid.get()
                ;
    }

    public void setPurchaseid(Integer purchaseid) {
        this.purchaseid.setValue(purchaseid);
    }

    public String getPurchasepn() {
        return purchasepn.get();
    }

    public void setPurchasepn(String purchasepn) {
        this.purchasepn.setValue(purchasepn);
    }

    public String getPurchasepc() {
        return purchasepc.get();
    }

    public void setPurchasepc(String purchasepc) {
        this.purchasepc.setValue(purchasepc);
    }

    public String getPurchasecn() {
        return purchasecn.get();
    }

    public void setPurchasecn(String purchasecn) {
        this.purchasecn.setValue(purchasecn);
    }

    public String getPurchasecc() {
        return purchasecc.get();
    }

    public void setPurchasecc(String purchasecc) {
        this.purchasecc.setValue(purchasecc);
    }

    public String getPurchasedate() {
        return purchasedate.get();
    }

    public void setPurchasedate(String purchasedate) {
        this.purchasedate.setValue(purchasedate);
    }

    public Integer getPurchaseqty() {
        return purchaseqty.get();
    }

    public void setPurchaseqty(Integer purchaseqty) {
        this.purchaseqty.setValue(purchaseqty);
    }

    public Double getPurchaseprice() {
        return purchaseprice.get();
    }

    public void setPurchaseprice(Double purchaseprice) {
        this.purchaseprice.setValue(purchaseprice);
    }

    public Double getPurchasetotal() {
        return purchasetotal.get();
    }

    public void setPurchasetotal(Double purchasetotal) {
        this.purchasetotal.setValue(purchasetotal);
    }

    public String getPurchasept() {
        return purchasept.get();
    }

    public void setPurchasept(String purchasept) {
        this.purchasept.setValue(purchasept);
    }

    public Double getPurchaseap() {
        return purchaseap.get();
    }

    public void setPurchaseap(Double purchaseap) {
        this.purchaseap.setValue(purchaseap);
    }

    public Double getPurchasebal() {
        return purchasebal.get();
    }

    public void setPurchasebal(Double purchasebal) {
        this.purchasebal.setValue(purchasebal);
    }
    
    
}
