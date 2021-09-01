package com.meatchop.tmcpartner.Settings;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Modal_RatingOrderDetails implements Serializable {
    String totalratingForthisOrder,createdtime,deliveryrating,feedback, itemrating_json,itemrating,key,orderid,qualityrating,userkey,usermobileno,vendorkey,vendorname,itemkey,itemname,rating;

    public Modal_RatingOrderDetails() {
    }


    public String getTotalratingForthisOrder() {
        return totalratingForthisOrder;
    }

    public void setTotalratingForthisOrder(String totalratingForthisOrder) {
        this.totalratingForthisOrder = totalratingForthisOrder;
    }

    public String getItemrating() {
        return itemrating;
    }

    public void setItemrating(String itemrating) {
        this.itemrating = itemrating;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getDeliveryrating() {
        return deliveryrating;
    }

    public void setDeliveryrating(String deliveryrating) {
        this.deliveryrating = deliveryrating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getItemrating_json() {
        return itemrating_json;
    }

    public void setItemrating_json(String itemrating_json) {
        this.itemrating_json = itemrating_json;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getQualityrating() {
        return qualityrating;
    }

    public void setQualityrating(String qualityrating) {
        this.qualityrating = qualityrating;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
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

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getItemkey() {
        return itemkey;
    }

    public void setItemkey(String itemkey) {
        this.itemkey = itemkey;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
