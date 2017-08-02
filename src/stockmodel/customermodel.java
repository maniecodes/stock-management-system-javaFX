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
public class customermodel {
    private SimpleIntegerProperty customerid;
    private SimpleStringProperty customername;
    private SimpleStringProperty customercon;
    private SimpleStringProperty customerloc;
    private SimpleStringProperty customeradd;
    private SimpleStringProperty customeremail;
    
    public customermodel(Integer customerid, String customername, String customercon, String customerloc,
            String customeradd, String customeremail){
        this.customerid = new SimpleIntegerProperty(customerid);
        this.customername = new SimpleStringProperty(customername);
        this.customercon = new SimpleStringProperty(customercon);
        this.customerloc = new SimpleStringProperty(customerloc);
        this.customeradd = new SimpleStringProperty(customeradd);
        this.customeremail = new SimpleStringProperty(customeremail);
    }
    
     public customermodel(Integer customerid, String customername, String customercon){
        this.customerid = new SimpleIntegerProperty(customerid);
        this.customername = new SimpleStringProperty(customername);
        this.customercon = new SimpleStringProperty(customercon);
    }

    /**
     * @return the customerid
     */
    public Integer getCustomerid() {
        return customerid.get();
    }

    /**
     * @param customerid the customerid to set
     */
    public void setCustomerid(Integer customerid) {
        this.customerid.setValue(customerid);
    }

    /**
     * @return the customername
     */
    public String getCustomername() {
        return customername.get();
    }

    /**
     * @param customername the customername to set
     */
    public void setCustomername(String customername) {
        this.customername.setValue(customername);
    }

    /**
     * @return the customercon
     */
    public String getCustomercon() {
        return customercon.get();
    }

    /**
     * @param customercon the customercon to set
     */
    public void setCustomercon(String customercon) {
        this.customercon.setValue(customercon);
    }

    /**
     * @return the customerloc
     */
    public String getCustomerloc() {
        return customerloc.get();
    }

    /**
     * @param customerloc the customerloc to set
     */
    public void setCustomerloc(String customerloc) {
        this.customerloc.setValue(customerloc);
    }

    /**
     * @return the customeradd
     */
    public String getCustomeradd() {
        return customeradd.get();
    }

    /**
     * @param customeradd the customeradd to set
     */
    public void setCustomeradd(String customeradd) {
        this.customeradd.setValue(customeradd);
    }

    /**
     * @return the customeremail
     */
    public String getCustomeremail() {
        return customeremail.get();
    }

    /**
     * @param customeremail the customeremail to set
     */
    public void setCustomeremail(String customeremail) {
        this.customeremail.setValue(customeremail);
    }
    
}
