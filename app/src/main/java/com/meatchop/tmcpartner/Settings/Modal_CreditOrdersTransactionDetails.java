package com.meatchop.tmcpartner.Settings;

import java.io.Serializable;

public class Modal_CreditOrdersTransactionDetails implements Serializable {

    String key="",newamountincredit="0",oldamountincredit="0",orderid="",transactiontime="",transactiontype="",transactionvalue="0",usermobileno="",vendorkey="";
    String transactiontimelong = "0";

    public String getTransactiontimelong() {
        return transactiontimelong;
    }

    public void setTransactiontimelong(String transactiontimelong) {
        this.transactiontimelong = transactiontimelong;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNewamountincredit() {
        return newamountincredit;
    }

    public void setNewamountincredit(String newamountincredit) {
        this.newamountincredit = newamountincredit;
    }

    public String getOldamountincredit() {
        return oldamountincredit;
    }

    public void setOldamountincredit(String oldamountincredit) {
        this.oldamountincredit = oldamountincredit;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getTransactiontime() {
        return transactiontime;
    }

    public void setTransactiontime(String transactiontime) {
        this.transactiontime = transactiontime;
    }

    public String getTransactiontype() {
        return transactiontype;
    }

    public void setTransactiontype(String transactiontype) {
        this.transactiontype = transactiontype;
    }

    public String getTransactionvalue() {
        return transactionvalue;
    }

    public void setTransactionvalue(String transactionvalue) {
        this.transactionvalue = transactionvalue;
    }

    public String getUsermobileno() {
        return usermobileno;
    }

    public void setUsermobileno(String usermobileno) {
        this.usermobileno = usermobileno;
    }

    public String getVendorkey() {
        return vendorkey;
    }

    public void setVendorkey(String vendorkey) {
        this.vendorkey = vendorkey;
    }
}
