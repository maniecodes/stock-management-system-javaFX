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
public class suppliermodel {
    private SimpleIntegerProperty supplierid;
    private SimpleStringProperty suppliername;
    private SimpleStringProperty suppliercontact;
    private SimpleStringProperty supplierlocation;
    private SimpleStringProperty supplieraddress;
    private SimpleStringProperty supplieremail;
    
    public suppliermodel(Integer supplierid, String suppliername,
            String suppliercontact, String supplierlocation, String supplieraddress,
            String supplieremail){
        this.supplierid = new SimpleIntegerProperty(supplierid);
        this.suppliername = new SimpleStringProperty(suppliername);
        this.suppliercontact = new SimpleStringProperty(suppliercontact);
        this.supplierlocation = new SimpleStringProperty(supplierlocation);
        this.supplieraddress = new SimpleStringProperty(supplieraddress);
        this.supplieremail = new SimpleStringProperty(supplieremail);
    }
    
        public suppliermodel(Integer supplierid, String suppliername,
            String suppliercontact){
            this.supplierid = new SimpleIntegerProperty(supplierid);
            this.suppliername = new SimpleStringProperty(suppliername);
            this.suppliercontact = new SimpleStringProperty(suppliercontact);
        }
   
    
    public Integer getSupplierid() {
        return supplierid.get();
    }

    public void setSupplierid(Integer supplierid) {
        this.supplierid.setValue(supplierid);
    }

    public String getSuppliername() {
        return suppliername.get();
    }

    public void setSuppliername(String suppliername) {
        this.suppliername.setValue(suppliername);
    }

    /*public String getSupplierlastname() {
        return supplierlastname.get();
    }

    public void setSupplierlastname(String supplierlastname) {
        this.supplierlastname.setValue(supplierlastname);
    }*/

    public String getSuppliercontact() {
        return suppliercontact.get();
    }

    public void setSuppliercontact(String suppliercontact) {
        this.suppliercontact.setValue(suppliercontact);
    }

    public String getSupplierlocation() {
        return supplierlocation.get();
    }

    public void setSupplierlocation(String supplierlocation) {
        this.supplierlocation.setValue(supplierlocation);
    }

    public String getSupplieraddress() {
        return supplieraddress.get();
    }

    public void setSupplieraddress(String supplieraddress) {
        this.supplieraddress.setValue(supplieraddress);
    }

    public String getSupplieremail() {
        return supplieremail.get();
    }

    public void setSupplieremail(String supplieremail) {
        this.supplieremail.setValue(supplieremail);
    }
    
}
