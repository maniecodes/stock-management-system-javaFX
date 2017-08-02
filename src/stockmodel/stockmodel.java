/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stockmodel;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


public class stockmodel {
    private SimpleIntegerProperty stockid;
    private SimpleStringProperty stockpn;
    private SimpleStringProperty stockpc;
    private SimpleIntegerProperty stockqty;
    private SimpleDoubleProperty stockprice;
    
    public stockmodel(Integer stockid, String stockpn, String stockpc, Integer stockqty,
            Double stockprice){
        this.stockid = new SimpleIntegerProperty(stockid);
        this.stockpn = new SimpleStringProperty(stockpn);
        this.stockpc = new SimpleStringProperty(stockpc);
        this.stockqty = new SimpleIntegerProperty(stockqty);
        this.stockprice = new SimpleDoubleProperty(stockprice);
    }

    public Integer getStockid() {
        return stockid.get();
    }

    public void setStockid(Integer stockid) {
        this.stockid.setValue(stockid);
    }

    public String getStockpn() {
        return stockpn.get();
    }

    public void setStockpn(String stockpn) {
        this.stockpn.setValue(stockpn);
    }

    public String getStockpc() {
        return stockpc.get();
    }

    public void setStockpc(String stockpc) {
        this.stockpc.setValue(stockpc);
    }

    public Integer getStockqty() {
        return stockqty.get();
    }

    public void setStockqty(Integer stockqty) {
        this.stockqty.setValue(stockqty);
    }

    public Double getStockprice() {
        return stockprice.get();
    }

    public void setStockprice(Double stockprice) {
        this.stockprice.setValue(stockprice);
    }
    
    

}
