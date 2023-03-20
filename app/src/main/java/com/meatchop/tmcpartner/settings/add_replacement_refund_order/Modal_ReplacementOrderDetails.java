package com.meatchop.tmcpartner.settings.add_replacement_refund_order;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class Modal_ReplacementOrderDetails implements Parcelable {
    public String markedDateLong = "",itemsmarked_String="",refunddetails_String = "",replacementdetails_String = "",markeddate="",amountusercanavl="",ordertype="",mobileno="",orderid="",orderplaceddate="",totalrefundedamount="",totalreplacementamount="",vendorkey="",status="";
    public JSONArray itemsmarked_Array = new JSONArray();
    public JSONArray refunddetails_Array = new JSONArray();
    public JSONArray replacementdetails_Array = new JSONArray();

    public Modal_ReplacementOrderDetails(Parcel in) {
        markeddate = in.readString();
        amountusercanavl = in.readString();
        mobileno = in.readString();
        orderid = in.readString();
        orderplaceddate = in.readString();
        totalrefundedamount = in.readString();
        totalreplacementamount = in.readString();
        vendorkey = in.readString();
        status = in.readString();
        itemsmarked_String = in.readString();
        refunddetails_String = in.readString();
        replacementdetails_String = in.readString();
        markedDateLong = in.readString();
        ordertype = in.readString();
    }

    public static final Creator<Modal_ReplacementOrderDetails> CREATOR = new Creator<Modal_ReplacementOrderDetails>() {
        @Override
        public Modal_ReplacementOrderDetails createFromParcel(Parcel in) {
            return new Modal_ReplacementOrderDetails(in);
        }

        @Override
        public Modal_ReplacementOrderDetails[] newArray(int size) {
            return new Modal_ReplacementOrderDetails[size];
        }
    };

    public Modal_ReplacementOrderDetails() {

    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getItemsmarked_String() {
        return itemsmarked_String;
    }

    public void setItemsmarked_String(String itemsmarked_String) {
        this.itemsmarked_String = itemsmarked_String;
    }

    public String getRefunddetails_String() {
        return refunddetails_String;
    }

    public void setRefunddetails_String(String refunddetails_String) {
        this.refunddetails_String = refunddetails_String;
    }

    public String getReplacementdetails_String() {
        return replacementdetails_String;
    }

    public void setReplacementdetails_String(String replacementdetails_String) {
        this.replacementdetails_String = replacementdetails_String;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JSONArray getRefunddetails_Array() {
        return refunddetails_Array;
    }

    public void setRefunddetails_Array(JSONArray refunddetails_Array) {
        this.refunddetails_Array = refunddetails_Array;
    }

    public JSONArray getReplacementdetails_Array() {
        return replacementdetails_Array;
    }

    public void setReplacementdetails_Array(JSONArray replacementdetails_Array) {
        this.replacementdetails_Array = replacementdetails_Array;
    }

    public String getMarkedDateLong() {
        return markedDateLong;
    }

    public void setMarkedDateLong(String markedDateLong) {
        this.markedDateLong = markedDateLong;
    }

    public JSONArray getItemsmarked_Array() {
        return itemsmarked_Array;
    }

    public void setItemsmarked_Array(JSONArray itemsmarked_Array) {
        this.itemsmarked_Array = itemsmarked_Array;
    }

    public String getMarkeddate() {
        return markeddate;
    }

    public void setMarkeddate(String markeddate) {
        this.markeddate = markeddate;
    }

    public String getAmountusercanavl() {
        return amountusercanavl;
    }

    public void setAmountusercanavl(String amountusercanavl) {
        this.amountusercanavl = amountusercanavl;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderplaceddate() {
        return orderplaceddate;
    }

    public void setOrderplaceddate(String orderplaceddate) {
        this.orderplaceddate = orderplaceddate;
    }

    public String getTotalrefundedamount() {
        return totalrefundedamount;
    }

    public void setTotalrefundedamount(String totalrefundedamount) {
        this.totalrefundedamount = totalrefundedamount;
    }

    public String getTotalreplacementamount() {
        return totalreplacementamount;
    }

    public void setTotalreplacementamount(String totalreplacementamount) {
        this.totalreplacementamount = totalreplacementamount;
    }

    public String getVendorkey() {
        return vendorkey;
    }

    public void setVendorkey(String vendorkey) {
        this.vendorkey = vendorkey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(markeddate);
        dest.writeString(amountusercanavl);
        dest.writeString(mobileno);
        dest.writeString(orderid);
        dest.writeString(orderplaceddate);
        dest.writeString(totalrefundedamount);
        dest.writeString(totalreplacementamount);
        dest.writeString(vendorkey);
        dest.writeString(status);
        dest.writeString(itemsmarked_String);
        dest.writeString(refunddetails_String);
        dest.writeString(replacementdetails_String);
        dest.writeString(markedDateLong);
        dest.writeString(ordertype);
    }
}
