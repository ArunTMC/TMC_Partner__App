package com.meatchop.tmcpartner.Settings;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class Modal_OrderDetails_Tracking_RatingDetails implements Parcelable {

//OrderDetails
JSONArray itemdesp;
String itemdesp_String,coupondiscount ,couponkey,deliveryamount,deliverytype,gstamount,isdeliveryslotfree,key,merchantorderid,notes,orderid,orderplaceddate,orderplacedtime,ordertype,payableamount,paymentmode,slotdate,slotname,slottimerange,tokenno,useraddress,userkey,usermobile,vendorkey,vendorname;
    String itemname,applieddiscountpercentage,checkouturl,cutname,cutprice,grossweightingrams,gstamount_itemdesp,menuitemid,netweight,portionsize,quantity,tmcprice,tmcsubctgykey;


//OrderTrackingDetails
String deliverydistance,deliveryuserkey,deliveryuserlat,deliveryuserlong,deliveryusermobileno,deliveryusername,keyfromtrackingDetails,orderconfirmedtime,
        orderdeliverytime,orderpickeduptime,orderreadytime,orderstatus,useraddresskey,useraddresslat,useraddresslon,
        usermobileno;


//Rating Order details
String itemname_rating,totalratingForthisOrder,createdtime,deliveryrating,feedback, itemrating_json,itemrating,rating_key,qualityrating,itemkey,rating;

    protected Modal_OrderDetails_Tracking_RatingDetails(Parcel in) {
        itemdesp_String = in.readString();
        coupondiscount = in.readString();
        couponkey = in.readString();
        deliveryamount = in.readString();
        deliverytype = in.readString();
        gstamount = in.readString();
        isdeliveryslotfree = in.readString();
        key = in.readString();
        merchantorderid = in.readString();
        notes = in.readString();
        orderid = in.readString();
        orderplaceddate = in.readString();
        orderplacedtime = in.readString();
        ordertype = in.readString();
        payableamount = in.readString();
        paymentmode = in.readString();
        slotdate = in.readString();
        slotname = in.readString();
        slottimerange = in.readString();
        tokenno = in.readString();
        useraddress = in.readString();
        userkey = in.readString();
        usermobile = in.readString();
        vendorkey = in.readString();
        vendorname = in.readString();
        itemname = in.readString();
        applieddiscountpercentage = in.readString();
        checkouturl = in.readString();
        cutname = in.readString();
        cutprice = in.readString();
        grossweightingrams = in.readString();
        gstamount_itemdesp = in.readString();
        menuitemid = in.readString();
        netweight = in.readString();
        portionsize = in.readString();
        quantity = in.readString();
        tmcprice = in.readString();
        tmcsubctgykey = in.readString();
        deliverydistance = in.readString();
        deliveryuserkey = in.readString();
        deliveryuserlat = in.readString();
        deliveryuserlong = in.readString();
        deliveryusermobileno = in.readString();
        deliveryusername = in.readString();
        keyfromtrackingDetails = in.readString();
        orderconfirmedtime = in.readString();
        orderdeliverytime = in.readString();
        orderpickeduptime = in.readString();
        orderreadytime = in.readString();
        orderstatus = in.readString();
        useraddresskey = in.readString();
        useraddresslat = in.readString();
        useraddresslon = in.readString();
        usermobileno = in.readString();
        itemname_rating = in.readString();
        totalratingForthisOrder = in.readString();
        createdtime = in.readString();
        deliveryrating = in.readString();
        feedback = in.readString();
        itemrating_json = in.readString();
        itemrating = in.readString();
        rating_key = in.readString();
        qualityrating = in.readString();
        itemkey = in.readString();
        rating = in.readString();
    }

    public Modal_OrderDetails_Tracking_RatingDetails() {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemdesp_String);
        dest.writeString(coupondiscount);
        dest.writeString(couponkey);
        dest.writeString(deliveryamount);
        dest.writeString(deliverytype);
        dest.writeString(gstamount);
        dest.writeString(isdeliveryslotfree);
        dest.writeString(key);
        dest.writeString(merchantorderid);
        dest.writeString(notes);
        dest.writeString(orderid);
        dest.writeString(orderplaceddate);
        dest.writeString(orderplacedtime);
        dest.writeString(ordertype);
        dest.writeString(payableamount);
        dest.writeString(paymentmode);
        dest.writeString(slotdate);
        dest.writeString(slotname);
        dest.writeString(slottimerange);
        dest.writeString(tokenno);
        dest.writeString(useraddress);
        dest.writeString(userkey);
        dest.writeString(usermobile);
        dest.writeString(vendorkey);
        dest.writeString(vendorname);
        dest.writeString(itemname);
        dest.writeString(applieddiscountpercentage);
        dest.writeString(checkouturl);
        dest.writeString(cutname);
        dest.writeString(cutprice);
        dest.writeString(grossweightingrams);
        dest.writeString(gstamount_itemdesp);
        dest.writeString(menuitemid);
        dest.writeString(netweight);
        dest.writeString(portionsize);
        dest.writeString(quantity);
        dest.writeString(tmcprice);
        dest.writeString(tmcsubctgykey);
        dest.writeString(deliverydistance);
        dest.writeString(deliveryuserkey);
        dest.writeString(deliveryuserlat);
        dest.writeString(deliveryuserlong);
        dest.writeString(deliveryusermobileno);
        dest.writeString(deliveryusername);
        dest.writeString(keyfromtrackingDetails);
        dest.writeString(orderconfirmedtime);
        dest.writeString(orderdeliverytime);
        dest.writeString(orderpickeduptime);
        dest.writeString(orderreadytime);
        dest.writeString(orderstatus);
        dest.writeString(useraddresskey);
        dest.writeString(useraddresslat);
        dest.writeString(useraddresslon);
        dest.writeString(usermobileno);
        dest.writeString(itemname_rating);
        dest.writeString(totalratingForthisOrder);
        dest.writeString(createdtime);
        dest.writeString(deliveryrating);
        dest.writeString(feedback);
        dest.writeString(itemrating_json);
        dest.writeString(itemrating);
        dest.writeString(rating_key);
        dest.writeString(qualityrating);
        dest.writeString(itemkey);
        dest.writeString(rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Modal_OrderDetails_Tracking_RatingDetails> CREATOR = new Creator<Modal_OrderDetails_Tracking_RatingDetails>() {
        @Override
        public Modal_OrderDetails_Tracking_RatingDetails createFromParcel(Parcel in) {
            return new Modal_OrderDetails_Tracking_RatingDetails(in);
        }

        @Override
        public Modal_OrderDetails_Tracking_RatingDetails[] newArray(int size) {
            return new Modal_OrderDetails_Tracking_RatingDetails[size];
        }
    };

    public JSONArray getItemdesp() {
        return itemdesp;
    }

    public void setItemdesp(JSONArray itemdesp) {
        this.itemdesp = itemdesp;
    }

    public String getCoupondiscount() {
        return coupondiscount;
    }

    public String getItemdesp_String() {
        return itemdesp_String;
    }

    public void setItemdesp_String(String itemdesp_String) {
        this.itemdesp_String = itemdesp_String;
    }

    public String getOrderconfirmedtime() {
        return orderconfirmedtime;
    }

    public void setOrderconfirmedtime(String orderconfirmedtime) {
        this.orderconfirmedtime = orderconfirmedtime;
    }

    public void setCoupondiscount(String coupondiscount) {
        this.coupondiscount = coupondiscount;
    }

    public String getCouponkey() {
        return couponkey;
    }

    public void setCouponkey(String couponkey) {
        this.couponkey = couponkey;
    }

    public String getDeliveryamount() {
        return deliveryamount;
    }

    public void setDeliveryamount(String deliveryamount) {
        this.deliveryamount = deliveryamount;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }

    public String getGstamount() {
        return gstamount;
    }

    public void setGstamount(String gstamount) {
        this.gstamount = gstamount;
    }

    public String getIsdeliveryslotfree() {
        return isdeliveryslotfree;
    }

    public void setIsdeliveryslotfree(String isdeliveryslotfree) {
        this.isdeliveryslotfree = isdeliveryslotfree;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMerchantorderid() {
        return merchantorderid;
    }

    public void setMerchantorderid(String merchantorderid) {
        this.merchantorderid = merchantorderid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getOrderplacedtime() {
        return orderplacedtime;
    }

    public void setOrderplacedtime(String orderplacedtime) {
        this.orderplacedtime = orderplacedtime;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getPayableamount() {
        return payableamount;
    }

    public void setPayableamount(String payableamount) {
        this.payableamount = payableamount;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getSlotdate() {
        return slotdate;
    }

    public void setSlotdate(String slotdate) {
        this.slotdate = slotdate;
    }

    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
    }

    public String getSlottimerange() {
        return slottimerange;
    }

    public void setSlottimerange(String slottimerange) {
        this.slottimerange = slottimerange;
    }

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
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

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getApplieddiscountpercentage() {
        return applieddiscountpercentage;
    }

    public void setApplieddiscountpercentage(String applieddiscountpercentage) {
        this.applieddiscountpercentage = applieddiscountpercentage;
    }

    public String getCheckouturl() {
        return checkouturl;
    }

    public void setCheckouturl(String checkouturl) {
        this.checkouturl = checkouturl;
    }

    public String getCutname() {
        return cutname;
    }

    public void setCutname(String cutname) {
        this.cutname = cutname;
    }

    public String getCutprice() {
        return cutprice;
    }

    public void setCutprice(String cutprice) {
        this.cutprice = cutprice;
    }

    public String getGrossweightingrams() {
        return grossweightingrams;
    }

    public void setGrossweightingrams(String grossweightingrams) {
        this.grossweightingrams = grossweightingrams;
    }

    public String getGstamount_itemdesp() {
        return gstamount_itemdesp;
    }

    public void setGstamount_itemdesp(String gstamount_itemdesp) {
        this.gstamount_itemdesp = gstamount_itemdesp;
    }

    public String getMenuitemid() {
        return menuitemid;
    }

    public void setMenuitemid(String menuitemid) {
        this.menuitemid = menuitemid;
    }

    public String getNetweight() {
        return netweight;
    }

    public void setNetweight(String netweight) {
        this.netweight = netweight;
    }

    public String getPortionsize() {
        return portionsize;
    }

    public void setPortionsize(String portionsize) {
        this.portionsize = portionsize;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTmcprice() {
        return tmcprice;
    }

    public void setTmcprice(String tmcprice) {
        this.tmcprice = tmcprice;
    }

    public String getTmcsubctgykey() {
        return tmcsubctgykey;
    }

    public void setTmcsubctgykey(String tmcsubctgykey) {
        this.tmcsubctgykey = tmcsubctgykey;
    }

    public String getDeliverydistance() {
        return deliverydistance;
    }

    public void setDeliverydistance(String deliverydistance) {
        this.deliverydistance = deliverydistance;
    }

    public String getDeliveryuserkey() {
        return deliveryuserkey;
    }

    public void setDeliveryuserkey(String deliveryuserkey) {
        this.deliveryuserkey = deliveryuserkey;
    }

    public String getDeliveryuserlat() {
        return deliveryuserlat;
    }

    public void setDeliveryuserlat(String deliveryuserlat) {
        this.deliveryuserlat = deliveryuserlat;
    }

    public String getDeliveryuserlong() {
        return deliveryuserlong;
    }

    public void setDeliveryuserlong(String deliveryuserlong) {
        this.deliveryuserlong = deliveryuserlong;
    }

    public String getDeliveryusermobileno() {
        return deliveryusermobileno;
    }

    public void setDeliveryusermobileno(String deliveryusermobileno) {
        this.deliveryusermobileno = deliveryusermobileno;
    }

    public String getDeliveryusername() {
        return deliveryusername;
    }

    public void setDeliveryusername(String deliveryusername) {
        this.deliveryusername = deliveryusername;
    }

    public String getKeyfromtrackingDetails() {
        return keyfromtrackingDetails;
    }

    public void setKeyfromtrackingDetails(String keyfromtrackingDetails) {
        this.keyfromtrackingDetails = keyfromtrackingDetails;
    }

    public String getOrderdeliverytime() {
        return orderdeliverytime;
    }

    public void setOrderdeliverytime(String orderdeliverytime) {
        this.orderdeliverytime = orderdeliverytime;
    }

    public String getOrderpickeduptime() {
        return orderpickeduptime;
    }

    public void setOrderpickeduptime(String orderpickeduptime) {
        this.orderpickeduptime = orderpickeduptime;
    }

    public String getOrderreadytime() {
        return orderreadytime;
    }

    public void setOrderreadytime(String orderreadytime) {
        this.orderreadytime = orderreadytime;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getUseraddresskey() {
        return useraddresskey;
    }

    public void setUseraddresskey(String useraddresskey) {
        this.useraddresskey = useraddresskey;
    }

    public String getUseraddresslat() {
        return useraddresslat;
    }

    public void setUseraddresslat(String useraddresslat) {
        this.useraddresslat = useraddresslat;
    }

    public String getUseraddresslon() {
        return useraddresslon;
    }

    public void setUseraddresslon(String useraddresslon) {
        this.useraddresslon = useraddresslon;
    }

    public String getUsermobileno() {
        return usermobileno;
    }

    public void setUsermobileno(String usermobileno) {
        this.usermobileno = usermobileno;
    }

    public String getItemname_rating() {
        return itemname_rating;
    }

    public void setItemname_rating(String itemname_rating) {
        this.itemname_rating = itemname_rating;
    }

    public String getTotalratingForthisOrder() {
        return totalratingForthisOrder;
    }

    public void setTotalratingForthisOrder(String totalratingForthisOrder) {
        this.totalratingForthisOrder = totalratingForthisOrder;
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

    public String getItemrating() {
        return itemrating;
    }

    public void setItemrating(String itemrating) {
        this.itemrating = itemrating;
    }

    public String getRating_key() {
        return rating_key;
    }

    public void setRating_key(String rating_key) {
        this.rating_key = rating_key;
    }

    public String getQualityrating() {
        return qualityrating;
    }

    public void setQualityrating(String qualityrating) {
        this.qualityrating = qualityrating;
    }

    public String getItemkey() {
        return itemkey;
    }

    public void setItemkey(String itemkey) {
        this.itemkey = itemkey;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

