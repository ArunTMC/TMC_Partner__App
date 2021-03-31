package com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses;

import android.os.Parcel;
import android.os.Parcelable;

public class Modal_MenuItem implements Parcelable {
    public String grossweightingrams,applieddiscountpercentage,barcode,checkoutimageurl,displayno,grossweight,gstpercentage,imageurl,itemavailability,
            itemcalories,itemname,itemuniquecode,key,marinadelinkedcodes,menuboarddisplayname,menutype,netweight,portionsize,preparationsteps,
            menuItemId,preparationtime,pricetypeforpos,showinmenuboard,tmcctgykey,tmcprice,tmcpriceperkg,tmcsubctgykey,vendorkey,vendorname;

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    protected Modal_MenuItem(Parcel in) {
        applieddiscountpercentage = in.readString();
        barcode = in.readString();
        checkoutimageurl = in.readString();
        displayno = in.readString();
        grossweight = in.readString();
        gstpercentage = in.readString();
        imageurl = in.readString();
        itemavailability = in.readString();
        itemcalories = in.readString();
        itemname = in.readString();
        itemuniquecode = in.readString();
        key = in.readString();
        marinadelinkedcodes = in.readString();
        menuboarddisplayname = in.readString();
        menutype = in.readString();
        netweight = in.readString();
        portionsize = in.readString();
        preparationsteps = in.readString();
        preparationtime = in.readString();
        pricetypeforpos = in.readString();
        showinmenuboard = in.readString();
        tmcctgykey = in.readString();
        tmcprice = in.readString();
        tmcpriceperkg = in.readString();
        tmcsubctgykey = in.readString();
        vendorkey = in.readString();
        vendorname = in.readString();
        menuItemId=in.readString();
        grossweightingrams = in.readString();
    }

    public static final Creator<Modal_MenuItem> CREATOR = new Creator<Modal_MenuItem>() {
        @Override
        public Modal_MenuItem createFromParcel(Parcel in) {
            return new Modal_MenuItem(in);
        }

        @Override
        public Modal_MenuItem[] newArray(int size) {
            return new Modal_MenuItem[size];
        }
    };

    public Modal_MenuItem() {

    }

    public String getApplieddiscountpercentage() {
        return applieddiscountpercentage;
    }

    public void setApplieddiscountpercentage(String applieddiscountpercentage) {
        this.applieddiscountpercentage = applieddiscountpercentage;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCheckoutimageurl() {
        return checkoutimageurl;
    }

    public void setCheckoutimageurl(String checkoutimageurl) {
        this.checkoutimageurl = checkoutimageurl;
    }

    public String getDisplayno() {
        return displayno;
    }

    public void setDisplayno(String displayno) {
        this.displayno = displayno;
    }

    public String getGrossweight() {
        return grossweight;
    }

    public void setGrossweight(String grossweight) {
        this.grossweight = grossweight;
    }

    public String getGstpercentage() {
        return gstpercentage;
    }

    public void setGstpercentage(String gstpercentage) {
        this.gstpercentage = gstpercentage;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getItemavailability() {
        return itemavailability;
    }

    public void setItemavailability(String itemavailability) {
        this.itemavailability = itemavailability;
    }

    public String getItemcalories() {
        return itemcalories;
    }

    public void setItemcalories(String itemcalories) {
        this.itemcalories = itemcalories;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemuniquecode() {
        return itemuniquecode;
    }

    public void setItemuniquecode(String itemuniquecode) {
        this.itemuniquecode = itemuniquecode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMarinadelinkedcodes() {
        return marinadelinkedcodes;
    }

    public void setMarinadelinkedcodes(String marinadelinkedcodes) {
        this.marinadelinkedcodes = marinadelinkedcodes;
    }

    public String getMenuboarddisplayname() {
        return menuboarddisplayname;
    }

    public void setMenuboarddisplayname(String menuboarddisplayname) {
        this.menuboarddisplayname = menuboarddisplayname;
    }

    public String getMenutype() {
        return menutype;
    }

    public void setMenutype(String menutype) {
        this.menutype = menutype;
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

    public String getPreparationsteps() {
        return preparationsteps;
    }

    public void setPreparationsteps(String preparationsteps) {
        this.preparationsteps = preparationsteps;
    }

    public String getPreparationtime() {
        return preparationtime;
    }

    public void setPreparationtime(String preparationtime) {
        this.preparationtime = preparationtime;
    }

    public String getPricetypeforpos() {
        return pricetypeforpos;
    }

    public void setPricetypeforpos(String pricetypeforpos) {
        this.pricetypeforpos = pricetypeforpos;
    }

    public String getShowinmenuboard() {
        return showinmenuboard;
    }

    public void setShowinmenuboard(String showinmenuboard) {
        this.showinmenuboard = showinmenuboard;
    }

    public String getTmcctgykey() {
        return tmcctgykey;
    }

    public void setTmcctgykey(String tmcctgykey) {
        this.tmcctgykey = tmcctgykey;
    }

    public String getTmcprice() {
        return tmcprice;
    }

    public void setTmcprice(String tmcprice) {
        this.tmcprice = tmcprice;
    }

    public String getTmcpriceperkg() {
        return tmcpriceperkg;
    }

    public void setTmcpriceperkg(String tmcpriceperkg) {
        this.tmcpriceperkg = tmcpriceperkg;
    }

    public String getTmcsubctgykey() {
        return tmcsubctgykey;
    }

    public void setTmcsubctgykey(String tmcsubctgykey) {
        this.tmcsubctgykey = tmcsubctgykey;
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

    @Override
    public int describeContents() {
        return 0;
    }

    public String getGrossweightingrams() {
        return grossweightingrams;
    }

    public void setGrossweightingrams(String grossweightingrams) {
        this.grossweightingrams = grossweightingrams;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(applieddiscountpercentage);
        parcel.writeString(barcode);
        parcel.writeString(checkoutimageurl);
        parcel.writeString(displayno);
        parcel.writeString(grossweight);
        parcel.writeString(gstpercentage);
        parcel.writeString(imageurl);
        parcel.writeString(itemavailability);
        parcel.writeString(itemcalories);
        parcel.writeString(itemname);
        parcel.writeString(itemuniquecode);
        parcel.writeString(key);
        parcel.writeString(marinadelinkedcodes);
        parcel.writeString(menuboarddisplayname);
        parcel.writeString(menutype);
        parcel.writeString(netweight);
        parcel.writeString(portionsize);
        parcel.writeString(preparationsteps);
        parcel.writeString(preparationtime);
        parcel.writeString(pricetypeforpos);
        parcel.writeString(showinmenuboard);
        parcel.writeString(tmcctgykey);
        parcel.writeString(tmcprice);
        parcel.writeString(tmcpriceperkg);
        parcel.writeString(tmcsubctgykey);
        parcel.writeString(vendorkey);
        parcel.writeString(vendorname);
        parcel.writeString(menuItemId);
        parcel.writeString(grossweightingrams);
    }
}
