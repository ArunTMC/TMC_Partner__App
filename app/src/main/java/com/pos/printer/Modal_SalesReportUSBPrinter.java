package com.pos.printer;

public class Modal_SalesReportUSBPrinter {
    String cutname,orderID; String itemName; String itemWeight; String itemRate; String discount;String GST; String subTotal;String quantity;String grossWeight;
    String SlotDate,SlotName,SlotTimeInRange,totalRate,couponDiscount,totaldiscount,totalGST,totalsubtotal;
    double oldSavedAmount;
    String SubCtgyName ,itemname_report,tmcprice_report;


    public String getCutname() {
        return cutname;
    }

    public void setCutname(String cutname) {
        this.cutname = cutname;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(String itemWeight) {
        this.itemWeight = itemWeight;
    }

    public String getItemRate() {
        return itemRate;
    }

    public void setItemRate(String itemRate) {
        this.itemRate = itemRate;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(String grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getSlotDate() {
        return SlotDate;
    }

    public void setSlotDate(String slotDate) {
        SlotDate = slotDate;
    }

    public String getSlotName() {
        return SlotName;
    }

    public void setSlotName(String slotName) {
        SlotName = slotName;
    }

    public String getSlotTimeInRange() {
        return SlotTimeInRange;
    }

    public void setSlotTimeInRange(String slotTimeInRange) {
        SlotTimeInRange = slotTimeInRange;
    }

    public String getTotalRate() {
        return totalRate;
    }

    public void setTotalRate(String totalRate) {
        this.totalRate = totalRate;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getTotaldiscount() {
        return totaldiscount;
    }

    public void setTotaldiscount(String totaldiscount) {
        this.totaldiscount = totaldiscount;
    }

    public String getTotalGST() {
        return totalGST;
    }

    public void setTotalGST(String totalGST) {
        this.totalGST = totalGST;
    }

    public String getTotalsubtotal() {
        return totalsubtotal;
    }

    public void setTotalsubtotal(String totalsubtotal) {
        this.totalsubtotal = totalsubtotal;
    }

    public double getOldSavedAmount() {
        return oldSavedAmount;
    }

    public void setOldSavedAmount(double oldSavedAmount) {
        this.oldSavedAmount = oldSavedAmount;
    }

    public String getSubCtgyName() {
        return SubCtgyName;
    }

    public void setSubCtgyName(String subCtgyName) {
        SubCtgyName = subCtgyName;
    }

    public String getItemname_report() {
        return itemname_report;
    }

    public void setItemname_report(String itemname_report) {
        this.itemname_report = itemname_report;
    }

    public String getTmcprice_report() {
        return tmcprice_report;
    }

    public void setTmcprice_report(String tmcprice_report) {
        this.tmcprice_report = tmcprice_report;
    }
}
