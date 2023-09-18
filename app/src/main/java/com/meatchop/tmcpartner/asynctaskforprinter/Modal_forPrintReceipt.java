package com.meatchop.tmcpartner.asynctaskforprinter;

import android.os.Parcel;
import android.os.Parcelable;

import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.settings.Modal_Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Modal_forPrintReceipt implements Parcelable {
    String userMobile, itemTotalwithoutGst, taxAmount, payableAmount, orderid, payment_mode, discountAmount_StringGlobal, ordertype,
            orderplacedTime,tokenno;
    double otalredeempointsusergetfromorder ,totalamountUserHaveAsCredit;
    String vendorKey="",vendorType="",vendorName ="",StoreAddressLine3="",StoreAddressLine1="",
            redeemPoints_String="",StoreLanLine="",StoreAddressLine2 ="" , deliveryAmount ="" , amountRecieved_String ="" , balanceAmount_String = "";

    List<String> cart_Item_List = new ArrayList<>() ;
    HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap<>();
    Modal_Address selected_Address_modal = new Modal_Address();

    boolean isPhoneOrderSelected;


    protected Modal_forPrintReceipt(Parcel in) {
        userMobile = in.readString();
        itemTotalwithoutGst = in.readString();
        taxAmount = in.readString();
        payableAmount = in.readString();
        orderid = in.readString();
        payment_mode = in.readString();
        discountAmount_StringGlobal = in.readString();
        ordertype = in.readString();
        orderplacedTime = in.readString();
        tokenno = in.readString();
        otalredeempointsusergetfromorder = in.readDouble();
        totalamountUserHaveAsCredit = in.readDouble();
        vendorKey = in.readString();
        vendorType = in.readString();
        vendorName = in.readString();
        StoreAddressLine3 = in.readString();
        StoreAddressLine1 = in.readString();
        redeemPoints_String = in.readString();
        StoreLanLine = in.readString();
        StoreAddressLine2 = in.readString();
        deliveryAmount = in.readString();
        amountRecieved_String = in.readString();
        balanceAmount_String = in.readString();
        cart_Item_List = in.createStringArrayList();
        isPhoneOrderSelected = in.readByte() != 0;
    }

    public static final Creator<Modal_forPrintReceipt> CREATOR = new Creator<Modal_forPrintReceipt>() {
        @Override
        public Modal_forPrintReceipt createFromParcel(Parcel in) {
            return new Modal_forPrintReceipt(in);
        }

        @Override
        public Modal_forPrintReceipt[] newArray(int size) {
            return new Modal_forPrintReceipt[size];
        }
    };

    public Modal_forPrintReceipt() {

    }

    public Modal_Address getSelected_Address_modal() {
        return selected_Address_modal;
    }

    public void setSelected_Address_modal(Modal_Address selected_Address_modal) {
        this.selected_Address_modal = selected_Address_modal;
    }











    public double getTotalredeempointsusergetfromorder() {
        return otalredeempointsusergetfromorder;
    }

    public void setTotalredeempointsusergetfromorder(double otalredeempointsusergetfromorder) {
        this.otalredeempointsusergetfromorder = otalredeempointsusergetfromorder;
    }

    public double getTotalamountUserHaveAsCredit() {
        return totalamountUserHaveAsCredit;
    }

    public void setTotalamountUserHaveAsCredit(double totalamountUserHaveAsCredit) {
        this.totalamountUserHaveAsCredit = totalamountUserHaveAsCredit;
    }

    public String getAmountRecieved_String() {
        return amountRecieved_String;
    }

    public void setAmountRecieved_String(String amountRecieved_String) {
        this.amountRecieved_String = amountRecieved_String;
    }

    public String getBalanceAmount_String() {
        return balanceAmount_String;
    }

    public void setBalanceAmount_String(String balanceAmount_String) {
        this.balanceAmount_String = balanceAmount_String;
    }

    public String getDeliveryAmount() {
        return deliveryAmount;
    }

    public void setDeliveryAmount(String deliveryAmount) {
        this.deliveryAmount = deliveryAmount;
    }

    public boolean getisPhoneOrderSelected() {
        return isPhoneOrderSelected;
    }

    public void setPhoneOrderSelected(boolean phoneOrderSelected) {
        isPhoneOrderSelected = phoneOrderSelected;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
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

    public String getDiscountAmount_StringGlobal() {
        return discountAmount_StringGlobal;
    }

    public void setDiscountAmount_StringGlobal(String discountAmount_StringGlobal) {
        this.discountAmount_StringGlobal = discountAmount_StringGlobal;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getOrderplacedTime() {
        return orderplacedTime;
    }

    public void setOrderplacedTime(String orderplacedTime) {
        this.orderplacedTime = orderplacedTime;
    }

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public String getVendorKey() {
        return vendorKey;
    }

    public void setVendorKey(String vendorKey) {
        this.vendorKey = vendorKey;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getStoreAddressLine3() {
        return StoreAddressLine3;
    }

    public void setStoreAddressLine3(String storeAddressLine3) {
        StoreAddressLine3 = storeAddressLine3;
    }

    public String getStoreAddressLine1() {
        return StoreAddressLine1;
    }

    public void setStoreAddressLine1(String storeAddressLine1) {
        StoreAddressLine1 = storeAddressLine1;
    }

    public String getRedeemPoints_String() {
        return redeemPoints_String;
    }

    public void setRedeemPoints_String(String redeemPoints_String) {
        this.redeemPoints_String = redeemPoints_String;
    }

    public String getStoreLanLine() {
        return StoreLanLine;
    }

    public void setStoreLanLine(String storeLanLine) {
        StoreLanLine = storeLanLine;
    }

    public String getStoreAddressLine2() {
        return StoreAddressLine2;
    }

    public void setStoreAddressLine2(String storeAddressLine2) {
        StoreAddressLine2 = storeAddressLine2;
    }

    public List<String> getCart_Item_List() {
        return cart_Item_List;
    }

    public void setCart_Item_List(List<String> cart_Item_List) {
        this.cart_Item_List = cart_Item_List;
    }

    public HashMap<String, Modal_NewOrderItems> getCartItem_hashmap() {
        return cartItem_hashmap;
    }

    public void setCartItem_hashmap(HashMap<String, Modal_NewOrderItems> cartItem_hashmap) {
        this.cartItem_hashmap = cartItem_hashmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userMobile);
        dest.writeString(itemTotalwithoutGst);
        dest.writeString(taxAmount);
        dest.writeString(payableAmount);
        dest.writeString(orderid);
        dest.writeString(payment_mode);
        dest.writeString(discountAmount_StringGlobal);
        dest.writeString(ordertype);
        dest.writeString(orderplacedTime);
        dest.writeString(tokenno);
        dest.writeDouble(otalredeempointsusergetfromorder);
        dest.writeDouble(totalamountUserHaveAsCredit);
        dest.writeString(vendorKey);
        dest.writeString(vendorType);
        dest.writeString(vendorName);
        dest.writeString(StoreAddressLine3);
        dest.writeString(StoreAddressLine1);
        dest.writeString(redeemPoints_String);
        dest.writeString(StoreLanLine);
        dest.writeString(StoreAddressLine2);
        dest.writeString(deliveryAmount);
        dest.writeString(amountRecieved_String);
        dest.writeString(balanceAmount_String);
        dest.writeStringList(cart_Item_List);
        dest.writeByte((byte) (isPhoneOrderSelected ? 1 : 0));
    }
}
