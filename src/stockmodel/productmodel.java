/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class productmodel {
    
    private SimpleIntegerProperty productid;
    private SimpleStringProperty productname;
    private SimpleStringProperty productcode;
    private SimpleStringProperty productcat;
    private SimpleStringProperty productbrand;
    private SimpleDoubleProperty productprice;
    
    public productmodel(Integer productid, String productname, String productcode,
            String productcat, String productbrand, Double productprice){
        this.productid = new SimpleIntegerProperty(productid);
        this.productname = new SimpleStringProperty(productname);
        this.productcode = new SimpleStringProperty(productcode);
        this.productcat = new SimpleStringProperty(productcat);
        this.productbrand = new SimpleStringProperty(productbrand);
        this.productprice = new SimpleDoubleProperty(productprice);
    }
    public productmodel(Integer productid, String productname){
        this.productid = new SimpleIntegerProperty(productid);
        this.productname = new SimpleStringProperty(productname);
    }
    public productmodel(Integer productid, String productname, String productcode, Double productprice){
        this.productid = new SimpleIntegerProperty(productid);
        this.productname = new SimpleStringProperty(productname);
        this.productcode = new SimpleStringProperty(productcode);
        this.productprice = new SimpleDoubleProperty(productprice);
    }



    /**
     * @return the productid
     */
    public Integer getProductid() {
        return productid.get();
    }

    /**
     * @param productid the productid to set
     */
    public void setProductid(Integer productid) {
        this.productid.setValue(productid);
    }

    /**
     * @return the productname
     */
    public String getProductname() {
        return productname.get();
    }

    /**
     * @param productname the productname to set
     */
    public void setProductname(String productname) {
        this.productname.setValue(productname);
    }

    /**
     * @return the productcode
     */
    public String getProductcode() {
        return productcode.get();
    }

    /**
     * @param productcode the productcode to set
     */
    public void setProductcode(String productcode) {
        this.productcode.setValue(productcode);
    }

    /**
     * @return the productcat
     */
    public String getProductcat() {
        return productcat.get();
    }

    /**
     * @param productcat the productcat to set
     */
    public void setProductcat(String productcat) {
        this.productcat.setValue(productcat);
    }

    /**
     * @return the productbrand
     */
    public String getProductbrand() {
        return productbrand.get();
    }

    /**
     * @param productbrand the productbrand to set
     */
    public void setProductbrand(String productbrand) {
        this.productbrand.setValue(productbrand);
    }

    /**
     * @return the productprice
     */
    public Double getProductprice() {
        return productprice.get();
    }

    /**
     * @param productprice the productprice to set
     */
    public void setProductprice(Double productprice) {
        this.productprice.setValue(productprice);
    }

    


}
