package com.meatchop.tmcpartner.settings;

public class Modal_RaisedTicketsRatingDetails {

    String key,orderid,ticketraisedtime,userkey,usermobileno,vendorkey,vendorname,description,status,ticketclosedtime,qualityrating,deliveryrating,feedback;
    Modal_OrderDetails_Tracking_RatingDetails modal_orderDetails_tracking_ratingDetails;
    boolean isOrderDetailsScreenOpened = false;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getQualityrating() {
        return qualityrating;
    }

    public void setQualityrating(String qualityrating) {
        this.qualityrating = qualityrating;
    }

    public String getDeliveryrating() {
        return deliveryrating;
    }

    public void setDeliveryrating(String deliveryrating) {
        this.deliveryrating = deliveryrating;
    }

    public boolean isOrderDetailsScreenOpened() {
        return isOrderDetailsScreenOpened;
    }

    public boolean getisOrderDetailsScreenOpened() {
        return isOrderDetailsScreenOpened;
    }

    public void setOrderDetailsScreenOpened(boolean orderDetailsScreenOpened) {
        isOrderDetailsScreenOpened = orderDetailsScreenOpened;
    }

    public Modal_OrderDetails_Tracking_RatingDetails getModal_orderDetails_tracking_ratingDetails() {
        return modal_orderDetails_tracking_ratingDetails;
    }

    public void setModal_orderDetails_tracking_ratingDetails(Modal_OrderDetails_Tracking_RatingDetails modal_orderDetails_tracking_ratingDetails) {
        this.modal_orderDetails_tracking_ratingDetails = modal_orderDetails_tracking_ratingDetails;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTicketclosedtime() {
        return ticketclosedtime;
    }

    public void setTicketclosedtime(String ticketclosedtime) {
        this.ticketclosedtime = ticketclosedtime;
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

    public String getTicketraisedtime() {
        return ticketraisedtime;
    }

    public void setTicketraisedtime(String ticketraisedtime) {
        this.ticketraisedtime = ticketraisedtime;
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
}

