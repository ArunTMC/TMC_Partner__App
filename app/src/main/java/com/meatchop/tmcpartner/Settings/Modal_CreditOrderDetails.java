package com.meatchop.tmcpartner.Settings;

public class Modal_CreditOrderDetails {
    String  usermobileno,totalamountincredit,lastupdatedtime,vendorkey,totalAddedAmount="0",totalPaidAmount ="0";

    public String getTotalAddedAmount() {
        return totalAddedAmount;
    }

    public void setTotalAddedAmount(String totalAddedAmount) {
        this.totalAddedAmount = totalAddedAmount;
    }

    public String getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(String totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public String getUsermobileno() {
        return usermobileno;
    }

    public void setUsermobileno(String usermobileno) {
        this.usermobileno = usermobileno;
    }

    public String getTotalamountincredit() {
        return totalamountincredit;
    }

    public void setTotalamountincredit(String totalamountincredit) {
        this.totalamountincredit = totalamountincredit;
    }

    public String getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(String lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public String getVendorkey() {
        return vendorkey;
    }

    public void setVendorkey(String vendorkey) {
        this.vendorkey = vendorkey;
    }
}
