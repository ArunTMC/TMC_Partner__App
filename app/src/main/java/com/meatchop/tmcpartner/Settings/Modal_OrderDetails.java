package com.meatchop.tmcpartner.Settings;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

public class Modal_OrderDetails implements Parcelable {
    JSONArray itemdesp;
    String orderdeliverytime,orderpickeduptime,orderplacedtime,orderreadytime,useraddresskey,useraddresslat,useraddresslong,deliveryuserkey,deliveryuserlat,
            deliveryuserlong,deliveryusermobileno,deliveryusername,orderconfirmedtime,useraddress,userkey,tokenno,slottimerange,slotdate,payableamount,ordertype,orderplaceddate,notes,orderdetailskey,
            ordertrackingdetailskey,couponkey,deliveryamount,pricetypeforpos,itemFinalWeight,deliverydistance,deliverytype,grossweight,orderstatus,
            slotname,posSales,appSales,UpiSales,phonepeSales,CreditSales,CardSales,orderid,razorpaySales,paytmSales, cashOndeliverySales,tmcsubctgyname,tmcsubctgykey,
            weightingrams,finalAmount,usermobile,barcode,paymentmode,coupondiscount,gstamount,itemname,menuitemid,netweight,portionsize,quantity,
            tmcprice,vendorkey,deliverydistanceinkm;
    String wholeSaleOrderSales,bigBasketSales,dunzoSales,swiggySales,PhoneOrderSales,totalAmount_Price,discountAmount,subtotalAmount_discount_price,total_gstAmount,totalAmount_discount_price_gst;

    protected Modal_OrderDetails(Parcel in) {
        orderdeliverytime = in.readString();
        orderpickeduptime = in.readString();
        orderplacedtime = in.readString();
        orderreadytime = in.readString();
        useraddresskey = in.readString();
        useraddresslat = in.readString();
        useraddresslong = in.readString();
        deliveryuserkey = in.readString();
        deliveryuserlat = in.readString();
        deliveryuserlong = in.readString();
        deliveryusermobileno = in.readString();
        deliveryusername = in.readString();
        orderconfirmedtime = in.readString();
        useraddress = in.readString();
        userkey = in.readString();
        tokenno = in.readString();
        slottimerange = in.readString();
        slotdate = in.readString();
        payableamount = in.readString();
        ordertype = in.readString();
        orderplaceddate = in.readString();
        notes = in.readString();
        orderdetailskey = in.readString();
        ordertrackingdetailskey = in.readString();
        couponkey = in.readString();
        deliveryamount = in.readString();
        pricetypeforpos = in.readString();
        itemFinalWeight = in.readString();
        deliverydistance = in.readString();
        deliverytype = in.readString();
        grossweight = in.readString();
        orderstatus = in.readString();
        slotname = in.readString();
        posSales = in.readString();
        appSales = in.readString();
        UpiSales = in.readString();
        phonepeSales = in.readString();
        CardSales = in.readString();
        orderid = in.readString();
        razorpaySales = in.readString();
        paytmSales = in.readString();
        cashOndeliverySales = in.readString();
        tmcsubctgyname = in.readString();
        tmcsubctgykey = in.readString();
        weightingrams = in.readString();
        finalAmount = in.readString();
        usermobile = in.readString();
        barcode = in.readString();
        paymentmode = in.readString();
        coupondiscount = in.readString();
        gstamount = in.readString();
        itemname = in.readString();
        menuitemid = in.readString();
        netweight = in.readString();
        portionsize = in.readString();
        quantity = in.readString();
        tmcprice = in.readString();
        vendorkey = in.readString();
        deliverydistanceinkm = in.readString();
        dunzoSales = in.readString();
        swiggySales = in.readString();
        PhoneOrderSales = in.readString();
        totalAmount_Price = in.readString();
        discountAmount = in.readString();
        subtotalAmount_discount_price = in.readString();
        total_gstAmount = in.readString();
        totalAmount_discount_price_gst = in.readString();
        bigBasketSales = in.readString();
        CreditSales = in.readString();
        wholeSaleOrderSales = in.readString();
    }

    public static final Creator<Modal_OrderDetails> CREATOR = new Creator<Modal_OrderDetails>() {
        @Override
        public Modal_OrderDetails createFromParcel(Parcel in) {
            return new Modal_OrderDetails(in);
        }

        @Override
        public Modal_OrderDetails[] newArray(int size) {
            return new Modal_OrderDetails[size];
        }
    };

    public Modal_OrderDetails() {

    }

    public String getWholeSaleOrderSales() {
        return wholeSaleOrderSales;
    }

    public void setWholeSaleOrderSales(String wholeSaleOrderSales) {
        this.wholeSaleOrderSales = wholeSaleOrderSales;
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

    public String getUseraddresslong() {
        return useraddresslong;
    }

    public void setUseraddresslong(String useraddresslong) {
        this.useraddresslong = useraddresslong;
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

    public String getOrderconfirmedtime() {
        return orderconfirmedtime;
    }

    public void setOrderconfirmedtime(String orderconfirmedtime) {
        this.orderconfirmedtime = orderconfirmedtime;
    }

    public String getUserkey() {
        return userkey;
    }

    public String getDeliverydistanceinkm() {
        return deliverydistanceinkm;
    }

    public void setDeliverydistanceinkm(String deliverydistanceinkm) {
        this.deliverydistanceinkm = deliverydistanceinkm;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getVendorkey() {
        return vendorkey;
    }

    public void setVendorkey(String vendorkey) {
        this.vendorkey = vendorkey;
    }

    public String getNotes() {
        return notes;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getTokenno() {
        return tokenno;
    }

    public void setTokenno(String tokenno) {
        this.tokenno = tokenno;
    }

    public String getSlottimerange() {
        return slottimerange;
    }

    public void setSlottimerange(String slottimerange) {
        this.slottimerange = slottimerange;
    }

    public String getSlotdate() {
        return slotdate;
    }

    public void setSlotdate(String slotdate) {
        this.slotdate = slotdate;
    }

    public String getPayableamount() {
        return payableamount;
    }

    public void setPayableamount(String payableamount) {
        this.payableamount = payableamount;
    }

    public String getOrderplacedtime() {
        return orderplacedtime;
    }

    public void setOrderplacedtime(String orderplacedtime) {
        this.orderplacedtime = orderplacedtime;
    }

    public String getOrderplaceddate() {
        return orderplaceddate;
    }

    public void setOrderplaceddate(String orderplaceddate) {
        this.orderplaceddate = orderplaceddate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderdetailskey() {
        return orderdetailskey;
    }

    public void setOrderdetailskey(String orderdetailskey) {
        this.orderdetailskey = orderdetailskey;
    }

    public String getOrdertrackingdetailskey() {
        return ordertrackingdetailskey;
    }

    public void setOrdertrackingdetailskey(String ordertrackingdetailskey) {
        this.ordertrackingdetailskey = ordertrackingdetailskey;
    }

    public String getCreditSales() {
        return CreditSales;
    }

    public void setCreditSales(String creditSales) {
        CreditSales = creditSales;
    }

    public String getDunzoSales() {
        return dunzoSales;
    }

    public String getDeliveryamount() {
        return deliveryamount;
    }

    public void setDeliveryamount(String deliveryamount) {
        this.deliveryamount = deliveryamount;
    }

    public void setDunzoSales(String dunzoSales) {
        this.dunzoSales = dunzoSales;
    }

    public String getPricetypeforpos() {
        return pricetypeforpos;
    }

    public String getCouponkey() {
        return couponkey;
    }

    public void setCouponkey(String couponkey) {
        this.couponkey = couponkey;
    }

    public void setPricetypeforpos(String pricetypeforpos) {
        this.pricetypeforpos = pricetypeforpos;
    }

    public String getItemFinalWeight() {
        return itemFinalWeight;
    }

    public void setItemFinalWeight(String itemFinalWeight) {
        this.itemFinalWeight = itemFinalWeight;
    }

    public String getSwiggySales() {
        return swiggySales;
    }

    public void setSwiggySales(String swiggySales) {
        this.swiggySales = swiggySales;
    }

    public String getBigBasketSales() {
        return bigBasketSales;
    }

    public void setBigBasketSales(String bigBasketSales) {
        this.bigBasketSales = bigBasketSales;
    }

    public String getPhoneOrderSales() {
        return PhoneOrderSales;
    }

    public void setPhoneOrderSales(String phoneOrderSales) {
        PhoneOrderSales = phoneOrderSales;
    }

    public String getDeliverydistance() {
        return deliverydistance;
    }

    public void setDeliverydistance(String deliverydistance) {
        this.deliverydistance = deliverydistance;
    }

    public String getDeliverytype() {
        return deliverytype;
    }

    public void setDeliverytype(String deliverytype) {
        this.deliverytype = deliverytype;
    }

    public String getGrossweight() {
        return grossweight;
    }

    public void setGrossweight(String grossweight) {
        this.grossweight = grossweight;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
    }

    public String getPaytmSales() {
        return paytmSales;
    }

    public void setPaytmSales(String paytmSales) {
        this.paytmSales = paytmSales;
    }

    public JSONArray getItemdesp() {
        return itemdesp;
    }

    public String getPosSales() {
        return posSales;
    }

    public void setPosSales(String posSales) {
        this.posSales = posSales;
    }

    public String getPhonepeSales() {
        return phonepeSales;
    }

    public void setPhonepeSales(String phonepeSales) {
        this.phonepeSales = phonepeSales;
    }

    public String getAppSales() {
        return appSales;
    }

    public void setAppSales(String appSales) {
        this.appSales = appSales;
    }

    public String getUpiSales() {
        return UpiSales;
    }

    public void setUpiSales(String upiSales) {
        UpiSales = upiSales;
    }

    public String getCardSales() {
        return CardSales;
    }

    public void setCardSales(String cardSales) {
        CardSales = cardSales;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getFinalAmount() {
        return finalAmount;
    }

    public String getTmcsubctgykey() {
        return tmcsubctgykey;
    }

    public void setTmcsubctgykey(String tmcsubctgykey) {
        this.tmcsubctgykey = tmcsubctgykey;
    }

    public String getTmcsubctgyname() {
        return tmcsubctgyname;
    }

    public void setTmcsubctgyname(String tmcsubctgyname) {
        this.tmcsubctgyname = tmcsubctgyname;
    }

    public String getWeightingrams() {
        return weightingrams;
    }

    public void setWeightingrams(String weightingrams) {
        this.weightingrams = weightingrams;
    }

    public void setFinalAmount(String finalAmount) {
        this.finalAmount = finalAmount;
    }

    public void setItemdesp(JSONArray itemdesp) {
        this.itemdesp = itemdesp;
    }

    public String getRazorpaySales() {
        return razorpaySales;
    }

    public void setRazorpaySales(String razorpaySales) {
        this.razorpaySales = razorpaySales;
    }

    public String getCashOndeliverySales() {
        return cashOndeliverySales;
    }

    public void setCashOndeliverySales(String cashOndeliverySales) {
        this.cashOndeliverySales = cashOndeliverySales;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getCoupondiscount() {
        return coupondiscount;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setCoupondiscount(String coupondiscount) {
        this.coupondiscount = coupondiscount;
    }

    public String getGstamount() {
        return gstamount;
    }

    public String getOrdertype() {
        return ordertype;
    }

    public void setOrdertype(String ordertype) {
        this.ordertype = ordertype;
    }

    public void setGstamount(String gstamount) {
        this.gstamount = gstamount;
    }

    public String getItemname() {
        return itemname;
    }

    public String getTotalAmount_Price() {
        return totalAmount_Price;
    }

    public void setTotalAmount_Price(String totalAmount_Price) {
        this.totalAmount_Price = totalAmount_Price;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getSubtotalAmount_discount_price() {
        return subtotalAmount_discount_price;
    }

    public void setSubtotalAmount_discount_price(String subtotalAmount_discount_price) {
        this.subtotalAmount_discount_price = subtotalAmount_discount_price;
    }

    public String getTotal_gstAmount() {
        return total_gstAmount;
    }

    public void setTotal_gstAmount(String total_gstAmount) {
        this.total_gstAmount = total_gstAmount;
    }

    public String getTotalAmount_discount_price_gst() {
        return totalAmount_discount_price_gst;
    }

    public void setTotalAmount_discount_price_gst(String totalAmount_discount_price_gst) {
        this.totalAmount_discount_price_gst = totalAmount_discount_price_gst;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderdeliverytime);
        dest.writeString(orderpickeduptime);
        dest.writeString(orderplacedtime);
        dest.writeString(orderreadytime);
        dest.writeString(useraddresskey);
        dest.writeString(useraddresslat);
        dest.writeString(useraddresslong);
        dest.writeString(deliveryuserkey);
        dest.writeString(deliveryuserlat);
        dest.writeString(deliveryuserlong);
        dest.writeString(deliveryusermobileno);
        dest.writeString(deliveryusername);
        dest.writeString(orderconfirmedtime);
        dest.writeString(useraddress);
        dest.writeString(userkey);
        dest.writeString(tokenno);
        dest.writeString(slottimerange);
        dest.writeString(slotdate);
        dest.writeString(payableamount);
        dest.writeString(ordertype);
        dest.writeString(orderplaceddate);
        dest.writeString(notes);
        dest.writeString(orderdetailskey);
        dest.writeString(ordertrackingdetailskey);
        dest.writeString(couponkey);
        dest.writeString(deliveryamount);
        dest.writeString(pricetypeforpos);
        dest.writeString(itemFinalWeight);
        dest.writeString(deliverydistance);
        dest.writeString(deliverytype);
        dest.writeString(grossweight);
        dest.writeString(orderstatus);
        dest.writeString(slotname);
        dest.writeString(posSales);
        dest.writeString(appSales);
        dest.writeString(UpiSales);
        dest.writeString(phonepeSales);
        dest.writeString(CardSales);
        dest.writeString(orderid);
        dest.writeString(razorpaySales);
        dest.writeString(paytmSales);
        dest.writeString(cashOndeliverySales);
        dest.writeString(tmcsubctgyname);
        dest.writeString(tmcsubctgykey);
        dest.writeString(weightingrams);
        dest.writeString(finalAmount);
        dest.writeString(usermobile);
        dest.writeString(barcode);
        dest.writeString(paymentmode);
        dest.writeString(coupondiscount);
        dest.writeString(gstamount);
        dest.writeString(itemname);
        dest.writeString(menuitemid);
        dest.writeString(netweight);
        dest.writeString(portionsize);
        dest.writeString(quantity);
        dest.writeString(tmcprice);
        dest.writeString(vendorkey);
        dest.writeString(deliverydistanceinkm);
        dest.writeString(dunzoSales);
        dest.writeString(swiggySales);
        dest.writeString(PhoneOrderSales);
        dest.writeString(totalAmount_Price);
        dest.writeString(discountAmount);
        dest.writeString(subtotalAmount_discount_price);
        dest.writeString(total_gstAmount);
        dest.writeString(totalAmount_discount_price_gst);
        dest.writeString(bigBasketSales);
        dest.writeString(CreditSales);
        dest.writeString(wholeSaleOrderSales);
    }
}
