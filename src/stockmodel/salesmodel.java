/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class salesmodel {
    private SimpleIntegerProperty salesid;
    private SimpleStringProperty salespn;
    private SimpleStringProperty  salespc;
    private SimpleStringProperty salescn;
    private SimpleStringProperty salescc;
    private SimpleStringProperty salesdate;
    private SimpleIntegerProperty salesqty;
    private SimpleDoubleProperty salesprice;
    private SimpleDoubleProperty salestotal;
    private SimpleStringProperty salespt;
    private SimpleDoubleProperty salesap;
    private SimpleDoubleProperty salesbal;
    
    public salesmodel(Integer salesid, String salespn, String salespc, String salescn, String salescc, 
            String salesdate, Integer salesqty, Double salesprice, Double salestotal, String salespt,
            Double salesap, Double salesbal){
        this.salesid = new SimpleIntegerProperty(salesid);
        this.salespn = new SimpleStringProperty(salespn);
        this.salespc = new SimpleStringProperty(salespc);
        this.salescn = new SimpleStringProperty(salescn);
        this.salescc = new SimpleStringProperty(salescc);
        this.salesdate = new SimpleStringProperty(salesdate);
        this.salesqty = new SimpleIntegerProperty(salesqty);
        this.salesprice = new SimpleDoubleProperty(salesprice);
        this.salestotal = new SimpleDoubleProperty(salestotal);
        this.salespt = new SimpleStringProperty(salespt);
        this.salesap = new SimpleDoubleProperty(salesap);
        this.salesbal = new SimpleDoubleProperty(salesbal);  
    }
    
    public salesmodel(Integer salesid, String salespn, String salespc, 
             Integer salesqty, Double salesprice, Double salestotal, 
            Double salesap, Double salesbal,String salesdate){
        this.salesid = new SimpleIntegerProperty(salesid);
        this.salespn = new SimpleStringProperty(salespn);
        this.salespc = new SimpleStringProperty(salespc);
        this.salesqty = new SimpleIntegerProperty(salesqty);
        this.salesprice = new SimpleDoubleProperty(salesprice);
        this.salestotal = new SimpleDoubleProperty(salestotal);
        this.salesap = new SimpleDoubleProperty(salesap);
        this.salesbal = new SimpleDoubleProperty(salesbal);
        this.salesdate = new SimpleStringProperty(salesdate);
    }

    public Integer getSalesid() {
        return salesid.get();
    }

    public void setSalesid(Integer salesid) {
        this.salesid.setValue(salesid);
    }

    public String getSalespn() {
        return salespn.get();
    }

    public void setSalespn(String salespn) {
        this.salespn.setValue(salespn);
    }

    public String getSalespc() {
        return salespc.get();
    }

    public void setSalespc(String salespc) {
        this.salespc.setValue(salespc);
    }

    public String getSalescn() {
        return salescn.get();
    }

    public void setSalescn(String salescn) {
        this.salescn.setValue(salescn);
    }

    public String getSalescc() {
        return salescc.get();
    }

    public void setSalescc(String salescc) {
        this.salescc.setValue(salescc);
    }

    public String getSalesdate() {
        return salesdate.get();
    }

    public void setSalesdate(String salesdate) {
        this.salesdate.setValue(salesdate);
    }

    public Integer getSalesqty() {
        return salesqty.get();
    }

    public void setSalesqty(Integer salesqty) {
        this.salesqty.setValue(salesqty);
    }

    public Double getSalesprice() {
        return salesprice.get();
    }

    public void setSalesprice(Double salesprice) {
        this.salesprice.setValue(salesprice);
    }

    public Double getSalestotal() {
        return salestotal.get();
    }

    public void setSalestotal(Double salestotal) {
        this.salestotal.setValue(salestotal);
    }

    public String getSalespt() {
        return salespt.get();
    }

    public void setSalespt(String salespt) {
        this.salespt.setValue(salespt);
    }

    public Double getSalesap() {
        return salesap.get();
    }

    public void setSalesap(Double salesap) {
        this.salesap.setValue(salesap);
    }

    public Double getSalesbal() {
        return salesbal.get();
    }

    public void setSalesbal(Double salesbal) {
        this.salesbal.setValue(salesbal);
    }
    
    

    
    
}
