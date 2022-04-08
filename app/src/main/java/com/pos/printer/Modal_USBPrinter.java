package com.pos.printer;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Modal_USBPrinter {


    public  HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap<>();
    public  List<String> cart_Item_List = new ArrayList<>();
    public  String orderplacedTime = "" ; public  String userMobile = "" ; public  String tokenno = "" ; public  String itemTotalwithoutGst = "" ; public  String taxAmount = "" ; public  String payableAmount = "" ; public  String orderid = "" ; public String payment_mode = "" ; public  String finalCouponDiscountAmount = "" ; public  String ordertype ="";

    public String itemDesp_String ="",orderstatus ="" ,useraddress ="",slotname="" ,slotdate ="",slottimerange ="",deliverytype ="",notes ="",orderplacedtime ="",orderdetailskey ="",deliverydistance ="",deliveryamount="";
    public JSONArray itemdesp = new JSONArray();

    public String getItemDesp_String() {
        return itemDesp_String;
    }

    public void setItemDesp_String(String itemDesp_String) {
        this.itemDesp_String = itemDesp_String;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
    }

    public String getSlotdate() {
        return slotdate;
    }

    public void setSlotdate(String slotdate) {
        this.slotdate = slotdate;
    }

    public String getSlottimerange() {
        return slottimerange;
    }

    public void setSlottimerange(String slottimerange) {
        this.slottimerange = slottimerange;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderplacedtime() {
        return orderplacedtime;
    }

    public void setOrderplacedtime(String orderplacedtime) {
        this.orderplacedtime = orderplacedtime;
    }

    public String getOrderdetailskey() {
        return orderdetailskey;
    }

    public void setOrderdetailskey(String orderdetailskey) {
        this.orderdetailskey = orderdetailskey;
    }

    public String getDeliverydistance() {
        return deliverydistance;
    }

    public void setDeliverydistance(String deliverydistance) {
        this.deliverydistance = deliverydistance;
    }

    public String getDeliveryamount() {
        return deliveryamount;
    }

    public void setDeliveryamount(String deliveryamount) {
        this.deliveryamount = deliveryamount;
    }

    public JSONArray getItemdesp() {
        return itemdesp;
    }

    public void setItemdesp(JSONArray itemdesp) {
        this.itemdesp = itemdesp;
    }

    public  HashMap<String, Modal_NewOrderItems> getCartItem_hashmap() {
        return cartItem_hashmap;
    }

    public  void setCartItem_hashmap(HashMap<String, Modal_NewOrderItems> cartItem_hashmap) {
        cartItem_hashmap = cartItem_hashmap;
    }

    public  List<String> getCart_Item_List() {
        return cart_Item_List;
    }

    public  void setCart_Item_List(List<String> cart_Item_List) {
        cart_Item_List = cart_Item_List;
    }

    public String getOrderplacedTime() {
        return orderplacedTime;
    }

    public void setOrderplacedTime(String orderplacedTime) {
        this.orderplacedTime = orderplacedTime;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public String getItemTotalwithoutGst() {
        return itemTotalwithoutGst;
    }

    public void setItemTotalwithoutGst(String itemTotalwithoutGst) {
        this.itemTotalwithoutGst = itemTotalwithoutGst;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getFinalCouponDiscountAmount() {
        return finalCouponDiscountAmount;
    }

    public void setFinalCouponDiscountAmount(String finalCouponDiscountAmount) {
        this.finalCouponDiscountAmount = finalCouponDiscountAmount;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }
}
