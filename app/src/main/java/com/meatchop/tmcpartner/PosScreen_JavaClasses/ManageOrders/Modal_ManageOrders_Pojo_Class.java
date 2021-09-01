package com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders;


import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class  Modal_ManageOrders_Pojo_Class implements Parcelable {
public String couponkey,orderplaceddate,userkey,paymenttranscationimageurl,deliveryamount,typebasedonDeliveredTime,delayedtime,slottime_in_long,orderplacedtime_in_long,orderconfirmedtime_in_long,orderreadytime_in_long,orderpickeduptime_in_long, orderdeliveredtime_in_long,deliverydistance,tmcSubCtgyKey,itemuniquecode,netweight,grossweight,useraddresslat,useraddresslon,ordertype ,orderconfirmedtime,orderplacedtime,orderreadytime,orderpickeduptime,orderdeliveredtime,orderdetailskey,orderid,payableamount,paymentmode,tokenno,taxamount,usermobile,vendorkey,coupondiscamount,orderstatus;
public String totaltmcsubctgyquantity,tmcSubCtgyName,grossweightingrams,useraddress,useraddresskey,slottimerange,subTotal_perItem,itemdesp_string,itemName,price,quantity,keyfromtrackingDetails,notes,GstAmount,orderType;
public   String subTotal_PerItemWithoutGst,totalGstAmount,totalAmountWithoutGst,totalAmountWithGst,deliverytype,slotname,slotdate,deliveryPartnerKey,deliveryPartnerMobileNo,deliveryPartnerStatus,deliveryPartnerName,subtotalperItem,ItemFinalPrice,ItemFinalWeight, TotalofSubtotalPerItem;
public JSONArray itemdesp;
    public int intTokenNo;
public boolean isdelayed,isOrderPlacedlessThan3MinsBefore;
      String  isFromEditOrders,isFromGenerateCustomermobile_billvaluereport,isFromCancelledOrders,isOrdersChecked;

    public String getPaymenttranscationimageurl() {
        return paymenttranscationimageurl;
    }

    public void setPaymenttranscationimageurl(String paymenttranscationimageurl) {
        this.paymenttranscationimageurl = paymenttranscationimageurl;
    }

    public String getTotaltmcsubctgyquantity() {
        return totaltmcsubctgyquantity;
    }

    public void setTotaltmcsubctgyquantity(String totaltmcsubctgyquantity) {
        this.totaltmcsubctgyquantity = totaltmcsubctgyquantity;
    }

    public String getOrderconfirmedtime_in_long() {
        return orderconfirmedtime_in_long;
    }

    public void setOrderconfirmedtime_in_long(String orderconfirmedtime_in_long) {
        this.orderconfirmedtime_in_long = orderconfirmedtime_in_long;
    }

    public String getOrderreadytime_in_long() {
        return orderreadytime_in_long;
    }

    public void setOrderreadytime_in_long(String orderreadytime_in_long) {
        this.orderreadytime_in_long = orderreadytime_in_long;
    }

    public String getOrderpickeduptime_in_long() {
        return orderpickeduptime_in_long;
    }

    public void setOrderpickeduptime_in_long(String orderpickeduptime_in_long) {
        this.orderpickeduptime_in_long = orderpickeduptime_in_long;
    }

    public String getTmcSubCtgyName() {
        return tmcSubCtgyName;
    }

    public void setTmcSubCtgyName(String tmcSubCtgyName) {
        this.tmcSubCtgyName = tmcSubCtgyName;
    }

    public String getIsOrdersChecked() {
        return isOrdersChecked;
    }

    public void setIsOrdersChecked(String isOrdersChecked) {
        this.isOrdersChecked = isOrdersChecked;
    }

    public String getIsFromCancelledOrders() {
        return isFromCancelledOrders;
    }

    public void setIsFromCancelledOrders(String isFromCancelledOrders) {
        this.isFromCancelledOrders = isFromCancelledOrders;
    }

    public String getCouponkey() {
        return couponkey;
    }

    public void setCouponkey(String couponkey) {
        this.couponkey = couponkey;
    }

    public String getOrderplaceddate() {
        return orderplaceddate;
    }

    public void setOrderplaceddate(String orderplaceddate) {
        this.orderplaceddate = orderplaceddate;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public boolean isOrderPlacedlessThan3MinsBefore() {
        return isOrderPlacedlessThan3MinsBefore;
    }

    public void setOrderPlacedlessThan3MinsBefore(boolean orderPlacedlessThan3MinsBefore) {
        isOrderPlacedlessThan3MinsBefore = orderPlacedlessThan3MinsBefore;
    }

    public String getIsFromEditOrders() {
        return isFromEditOrders;
    }

    public void setIsFromEditOrders(String isFromEditOrders) {
        this.isFromEditOrders = isFromEditOrders;
    }

    public String getIsFromGenerateCustomermobile_billvaluereport() {
        return isFromGenerateCustomermobile_billvaluereport;
    }

    public void setIsFromGenerateCustomermobile_billvaluereport(String isFromGenerateCustomermobile_billvaluereport) {
        this.isFromGenerateCustomermobile_billvaluereport = isFromGenerateCustomermobile_billvaluereport;
    }

    public String getDelayedtime() {
        return delayedtime;
    }

    public String getTypebasedonDeliveredTime() {
        return typebasedonDeliveredTime;
    }

    public void setTypebasedonDeliveredTime(String typebasedonDeliveredTime) {
        this.typebasedonDeliveredTime = typebasedonDeliveredTime;
    }

    public void setDelayedtime(String delayedtime) {
        this.delayedtime = delayedtime;
    }

    public boolean isIsdelayed() {
        return isdelayed;
    }

    public void setIsdelayed(boolean isdelayed) {
        this.isdelayed = isdelayed;
    }

    public String getSlottime_in_long() {
        return slottime_in_long;
    }

    public void setSlottime_in_long(String slottime_in_long) {
        this.slottime_in_long = slottime_in_long;
    }

    public String getOrderplacedtime_in_long() {
        return orderplacedtime_in_long;
    }

    public void setOrderplacedtime_in_long(String orderplacedtime_in_long) {
        this.orderplacedtime_in_long = orderplacedtime_in_long;
    }

    public String getOrderdeliveredtime_in_long() {
        return orderdeliveredtime_in_long;
    }

    public void setOrderdeliveredtime_in_long(String orderdeliveredtime_in_long) {
        this.orderdeliveredtime_in_long = orderdeliveredtime_in_long;
    }

    public String getGrossweightingrams() {
        return grossweightingrams;
    }

    public void setGrossweightingrams(String grossweightingrams) {
        this.grossweightingrams = grossweightingrams;
    }

    public String getDeliverydistance() {
        return deliverydistance;
    }

    public void setDeliverydistance(String deliverydistance) {
        this.deliverydistance = deliverydistance;
    }

    public int getIntTokenNo() {
        return intTokenNo;
    }

    public void setIntTokenNo(int intTokenNo) {
        this.intTokenNo = intTokenNo;
    }

    public String getTmcSubCtgyKey() {
        return tmcSubCtgyKey;
    }

    public void setTmcSubCtgyKey(String tmcSubCtgyKey) {
        this.tmcSubCtgyKey = tmcSubCtgyKey;
    }

    public String getItemuniquecode() {
        return itemuniquecode;
    }

    public void setItemuniquecode(String itemuniquecode) {
        this.itemuniquecode = itemuniquecode;
    }

    public String getNetweight() {
        return netweight;
    }

    public void setNetweight(String netweight) {
        this.netweight = netweight;
    }

    public String getGrossweight() {
        return grossweight;
    }

    public void setGrossweight(String grossweight) {
        this.grossweight = grossweight;
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

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getUseraddresskey() {
        return useraddresskey;
    }

    public void setUseraddresskey(String useraddresskey) {
        this.useraddresskey = useraddresskey;
    }

    public String getOrderconfirmedtime() {
        return orderconfirmedtime;
    }

    public void setOrderconfirmedtime(String orderconfirmedtime) {
        this.orderconfirmedtime = orderconfirmedtime;
    }

    public String getOrderreadytime() {
        return orderreadytime;
    }

    public void setOrderreadytime(String orderreadytime) {
        this.orderreadytime = orderreadytime;
    }

    public String getSubTotal_PerItemWithoutGst() {
        return subTotal_PerItemWithoutGst;
    }

    public void setSubTotal_PerItemWithoutGst(String subTotal_PerItemWithoutGst) {
        this.subTotal_PerItemWithoutGst = subTotal_PerItemWithoutGst;
    }

    public String getOrderpickeduptime() {
        return orderpickeduptime;
    }

    public void setOrderpickeduptime(String orderpickeduptime) {
        this.orderpickeduptime = orderpickeduptime;
    }

    public String getOrderdeliveredtime() {
        return orderdeliveredtime;
    }

    public void setOrderdeliveredtime(String orderdeliveredtime) {
        this.orderdeliveredtime = orderdeliveredtime;
    }

    public String getOrderdetailskey() {
        return orderdetailskey;
    }

    public void setOrderdetailskey(String orderdetailskey) {
        this.orderdetailskey = orderdetailskey;
    }

    public String getSlottimerange() {
        return slottimerange;
    }

    public void setSlottimerange(String slottimerange) {
        this.slottimerange = slottimerange;
    }

    public String getSubTotal_perItem() {
        return subTotal_perItem;
    }

    public void setSubTotal_perItem(String subTotal_perItem) {
        this.subTotal_perItem = subTotal_perItem;
    }

    public String getTotalGstAmount() {
        return totalGstAmount;
    }

    public void setTotalGstAmount(String totalGstAmount) {
        this.totalGstAmount = totalGstAmount;
    }

    public String getTotalAmountWithoutGst() {
        return totalAmountWithoutGst;
    }

    public void setTotalAmountWithoutGst(String totalAmountWithoutGst) {
        this.totalAmountWithoutGst = totalAmountWithoutGst;
    }

    public String getTotalAmountWithGst() {
        return totalAmountWithGst;
    }

    public void setTotalAmountWithGst(String totalAmountWithGst) {
        this.totalAmountWithGst = totalAmountWithGst;
    }

    public String getItemdesp_string() {
        return itemdesp_string;
    }

    public void setItemdesp_string(String itemdesp_string) {
        this.itemdesp_string = itemdesp_string;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
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

    public String getDeliveryamount() {
        return deliveryamount;
    }

    public void setDeliveryamount(String deliveryamount) {
        this.deliveryamount = deliveryamount;
    }

    protected Modal_ManageOrders_Pojo_Class(Parcel in) {
        orderid = in.readString();
        orderplacedtime = in.readString();
        payableamount = in.readString();
        paymentmode = in.readString();
        tokenno = in.readString();
        taxamount = in.readString();
        usermobile = in.readString();
        vendorkey = in.readString();
        coupondiscamount = in.readString();
        orderstatus = in.readString();
        itemName = in.readString();
        price = in.readString();
        quantity = in.readString();
        keyfromtrackingDetails = in.readString();
        notes = in.readString();
        GstAmount = in.readString();
        orderType = in.readString();
        deliveryPartnerKey = in.readString();
        deliveryPartnerMobileNo = in.readString();
        deliveryPartnerStatus = in.readString();
        deliveryPartnerName = in.readString();
        subtotalperItem = in.readString();
        ItemFinalPrice = in.readString();
        ItemFinalWeight = in.readString();
        slotname = in.readString();
        slotdate = in.readString();
        deliverytype = in.readString();
        slottimerange = in.readString();
        useraddress = in.readString();
        useraddresskey = in.readString();

        orderconfirmedtime = in.readString();
        orderplacedtime = in.readString();
        orderreadytime = in.readString();
        orderpickeduptime = in.readString();
        orderdeliveredtime  = in.readString();
        itemdesp_string = in.readString();
        TotalofSubtotalPerItem = in.readString();
        useraddresslat = in.readString();
        useraddresslon = in.readString();
        intTokenNo = in.readInt();
        deliverydistance = in.readString();
        orderdetailskey = in.readString();
        isFromGenerateCustomermobile_billvaluereport = in.readString();
        isFromEditOrders = in.readString();
        isFromCancelledOrders= in.readString();
        deliveryamount = in.readString();
        paymenttranscationimageurl = in.readString();

    }

    public static final Creator<Modal_ManageOrders_Pojo_Class> CREATOR = new Creator<Modal_ManageOrders_Pojo_Class>() {
        @Override
        public Modal_ManageOrders_Pojo_Class createFromParcel(Parcel in) {
            return new Modal_ManageOrders_Pojo_Class(in);
        }

        @Override
        public Modal_ManageOrders_Pojo_Class[] newArray(int size) {
            return new Modal_ManageOrders_Pojo_Class[size];
        }
    };

    public JSONArray getItemdesp() {
        return itemdesp;
    }

    public void setItemdesp(JSONArray itemdesp) {
        this.itemdesp = itemdesp;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSubtotalperItem() {
        return subtotalperItem;
    }

    public String getItemFinalPrice() {
        return ItemFinalPrice;
    }

    public void setItemFinalPrice(String itemFinalPrice) {
        ItemFinalPrice = itemFinalPrice;
    }

    public String getItemFinalWeight() {
        return ItemFinalWeight;
    }

    public void setItemFinalWeight(String itemFinalWeight) {
        ItemFinalWeight = itemFinalWeight;
    }

    public String getTotalofSubtotalPerItem() {
        return TotalofSubtotalPerItem;
    }

    public void setTotalofSubtotalPerItem(String totalofSubtotalPerItem) {
        TotalofSubtotalPerItem = totalofSubtotalPerItem;
    }

    public void setSubtotalperItem(String subtotalperItem) {
        this.subtotalperItem = subtotalperItem;
    }

    public String getGstAmount() {
        return GstAmount;
    }

    public void setGstAmount(String gstAmount) {
        GstAmount = gstAmount;
    }

    public String getDeliveryPartnerKey() {
        return deliveryPartnerKey;
    }

    public void setDeliveryPartnerKey(String deliveryPartnerKey) {
        this.deliveryPartnerKey = deliveryPartnerKey;
    }

    public String getDeliveryPartnerMobileNo() {
        return deliveryPartnerMobileNo;
    }

    public void setDeliveryPartnerMobileNo(String deliveryPartnerMobileNo) {
        this.deliveryPartnerMobileNo = deliveryPartnerMobileNo;
    }

    public String getDeliveryPartnerStatus() {
        return deliveryPartnerStatus;
    }

    public void setDeliveryPartnerStatus(String deliveryPartnerStatus) {
        this.deliveryPartnerStatus = deliveryPartnerStatus;
    }

    public String getDeliveryPartnerName() {
        return deliveryPartnerName;
    }

    public void setDeliveryPartnerName(String deliveryPartnerName) {
        this.deliveryPartnerName = deliveryPartnerName;
    }

    public String getNotes() {
        return notes;
    }

    public String getKeyfromtrackingDetails() {
        return keyfromtrackingDetails;
    }

    public void setKeyfromtrackingDetails(String keyfromtrackingDetails) {
        this.keyfromtrackingDetails = keyfromtrackingDetails;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Modal_ManageOrders_Pojo_Class() {
    }




    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getOrderplacedtime() {
        return orderplacedtime;
    }

    public void setOrderplacedtime(String orderplacedtime) {
        this.orderplacedtime = orderplacedtime;
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

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public String getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(String taxamount) {
        this.taxamount = taxamount;
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

    public String getCoupondiscamount() {
        return coupondiscamount;
    }

    public void setCoupondiscamount(String coupondiscamount) {
        this.coupondiscamount = coupondiscamount;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(orderid);
        parcel.writeString(orderplacedtime);
        parcel.writeString(payableamount);
        parcel.writeString(paymentmode);
        parcel.writeString(tokenno);
        parcel.writeString(taxamount);
        parcel.writeString(usermobile);
        parcel.writeString(vendorkey);
        parcel.writeString(coupondiscamount);
        parcel.writeString(orderstatus);
        parcel.writeString(itemName);
        parcel.writeString(price);
        parcel.writeString(quantity);
        parcel.writeString(keyfromtrackingDetails);
        parcel.writeString(notes);
        parcel.writeString(GstAmount);
        parcel.writeString(orderType);
        parcel.writeString(deliveryPartnerKey);
        parcel.writeString(deliveryPartnerMobileNo);
        parcel.writeString(deliveryPartnerStatus);
        parcel.writeString(deliveryPartnerName);
        parcel.writeString(subtotalperItem);
        parcel.writeString(ItemFinalPrice);
        parcel.writeString(ItemFinalWeight);
        parcel.writeString(slotdate);
        parcel.writeString(slotname);

        parcel.writeString(deliverytype);
        parcel.writeString(slottimerange);
        parcel.writeString(useraddress);
        parcel.writeString(useraddresskey);
        parcel.writeString(orderconfirmedtime);
        parcel.writeString(orderplacedtime);
        parcel.writeString(orderreadytime);
        parcel.writeString(orderpickeduptime);
        parcel.writeString(orderdeliveredtime);
        parcel.writeString(itemdesp_string);
        parcel.writeString(TotalofSubtotalPerItem);
        parcel.writeString(useraddresslat);
        parcel.writeString(useraddresslon);
        parcel.writeInt(intTokenNo);
        parcel.writeString(deliverydistance);
        parcel.writeString(orderdetailskey);
        parcel.writeString(isFromGenerateCustomermobile_billvaluereport);
        parcel.writeString(isFromEditOrders);
        parcel.writeString(isFromCancelledOrders);
        parcel.writeString(deliveryamount);
        parcel.writeString(paymenttranscationimageurl);
    }
}
