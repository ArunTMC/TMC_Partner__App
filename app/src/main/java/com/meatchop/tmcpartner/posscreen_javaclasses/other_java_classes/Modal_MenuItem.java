package com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes;

import android.os.Parcel;
import android.os.Parcelable;

public class Modal_MenuItem implements Parcelable {
    public String tmcpriceWithMarkupValue ="0"  ,tmcpriceperkgWithMarkupValue ="0" ,appmarkuppercentage = "" ,showinpos = "" ,showinapp = "" ,allownegativestock = "" , barcode_AvlDetails = "" ,itemavailability_AvlDetails = "" ,key_AvlDetails = "" ,lastupdatedtime_AvlDetails = "" ,menuitemkey_AvlDetails = "" ,receivedstock_AvlDetails = "" ,stockbalance_AvlDetails = "" ,stockincomingkey_AvlDetails = "" ,vendorkey_AvlDetails = "" ,
            inventorydetails = "" ,itemcutdetails = "" ,itemweightdetails = "" ,bigbasketprice = "" ,dunzoprice = "" ,swiggyprice = "" ,reportname = "" ,grossweightingrams = "" ,applieddiscountpercentage = "" ,barcode = "" ,checkoutimageurl = "" ,displayno = "" ,grossweight = "" ,gstpercentage = "" ,imageurl = "" ,itemavailability = "" ,
            localDB_id = "" ,itemcalories = "" ,itemname = "" ,itemuniquecode = "" ,key = "" ,marinadelinkedcodes = "" ,menuboarddisplayname = "" ,menutype = "" ,netweight = "" ,portionsize = "" ,preparationsteps = "" ,
            menuItemId = "" ,preparationtime = "" ,pricetypeforpos = "" ,showinmenuboard = "" ,tmcctgykey = "" ,tmcprice = "" ,tmcpriceperkg = "" ,tmcsubctgykey = "" ,vendorkey = "" ,vendorname = "" ,wholesaleprice;


    public String getLocalDB_id() {
        return localDB_id;
    }

    public void setLocalDB_id(String localDB_id) {
        this.localDB_id = localDB_id;
    }

    public String getTmcpriceWithMarkupValue() {
        return tmcpriceWithMarkupValue;
    }

    public void setTmcpriceWithMarkupValue(String tmcpriceWithMarkupValue) {
        this.tmcpriceWithMarkupValue = tmcpriceWithMarkupValue;
    }

    public String getTmcpriceperkgWithMarkupValue() {
        return tmcpriceperkgWithMarkupValue;
    }

    public void setTmcpriceperkgWithMarkupValue(String tmcpriceperkgWithMarkupValue) {
        this.tmcpriceperkgWithMarkupValue = tmcpriceperkgWithMarkupValue;
    }

    public String getAppmarkuppercentage() {
        return appmarkuppercentage;
    }

    public void setAppmarkuppercentage(String appmarkuppercentage) {
        this.appmarkuppercentage = appmarkuppercentage;
    }

    public String getShowinpos() {
        return showinpos;
    }

    public void setShowinpos(String showinpos) {
        this.showinpos = showinpos;
    }

    public String getWholesaleprice() {
        return wholesaleprice;
    }

    public void setWholesaleprice(String wholesaleprice) {
        this.wholesaleprice = wholesaleprice;
    }

    public String getShowinapp() {
        return showinapp;
    }

    public void setShowinapp(String showinapp) {
        this.showinapp = showinapp;
    }

    public String getInventorydetails() {
        return inventorydetails;
    }

    public void setInventorydetails(String inventorydetails) {
        this.inventorydetails = inventorydetails;
    }

    public String getAllownegativestock() {
        return allownegativestock;
    }

    public void setAllownegativestock(String allownegativestock) {
        this.allownegativestock = allownegativestock;
    }

    public String getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(String menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getBarcode_AvlDetails() {
        return barcode_AvlDetails;
    }

    public void setBarcode_AvlDetails(String barcode_AvlDetails) {
        this.barcode_AvlDetails = barcode_AvlDetails;
    }

    public String getItemavailability_AvlDetails() {
        return itemavailability_AvlDetails;
    }

    public void setItemavailability_AvlDetails(String itemavailability_AvlDetails) {
        this.itemavailability_AvlDetails = itemavailability_AvlDetails;
    }

    public String getKey_AvlDetails() {
        return key_AvlDetails;
    }

    public void setKey_AvlDetails(String key_AvlDetails) {
        this.key_AvlDetails = key_AvlDetails;
    }

    public String getLastupdatedtime_AvlDetails() {
        return lastupdatedtime_AvlDetails;
    }

    public void setLastupdatedtime_AvlDetails(String lastupdatedtime_AvlDetails) {
        this.lastupdatedtime_AvlDetails = lastupdatedtime_AvlDetails;
    }

    public String getMenuitemkey_AvlDetails() {
        return menuitemkey_AvlDetails;
    }

    public void setMenuitemkey_AvlDetails(String menuitemkey_AvlDetails) {
        this.menuitemkey_AvlDetails = menuitemkey_AvlDetails;
    }

    public String getReceivedstock_AvlDetails() {
        return receivedstock_AvlDetails;
    }

    public void setReceivedstock_AvlDetails(String receivedstock_AvlDetails) {
        this.receivedstock_AvlDetails = receivedstock_AvlDetails;
    }

    public String getStockbalance_AvlDetails() {
        return stockbalance_AvlDetails;
    }

    public void setStockbalance_AvlDetails(String stockbalance_AvlDetails) {
        this.stockbalance_AvlDetails = stockbalance_AvlDetails;
    }

    public String getStockincomingkey_AvlDetails() {
        return stockincomingkey_AvlDetails;
    }

    public void setStockincomingkey_AvlDetails(String stockincomingkey_AvlDetails) {
        this.stockincomingkey_AvlDetails = stockincomingkey_AvlDetails;
    }

    public String getVendorkey_AvlDetails() {
        return vendorkey_AvlDetails;
    }

    public void setVendorkey_AvlDetails(String vendorkey_AvlDetails) {
        this.vendorkey_AvlDetails = vendorkey_AvlDetails;
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
        reportname = in.readString();
        swiggyprice = in.readString();
        dunzoprice = in.readString();
        bigbasketprice = in.readString();
        itemweightdetails = in.readString();
        itemcutdetails = in.readString();
        barcode_AvlDetails = in.readString();
        itemavailability_AvlDetails = in.readString();
        key_AvlDetails = in.readString();
        lastupdatedtime_AvlDetails = in.readString();
        menuitemkey_AvlDetails = in.readString();
        receivedstock_AvlDetails  = in.readString();
        stockbalance_AvlDetails = in.readString();
        stockincomingkey_AvlDetails = in.readString();
        vendorkey_AvlDetails  = in.readString();
        inventorydetails = in.readString();
        allownegativestock = in.readString();
        wholesaleprice  = in.readString();
        appmarkuppercentage   = in.readString();
        tmcpriceperkgWithMarkupValue = in.readString();
        tmcpriceWithMarkupValue = in.readString();
        localDB_id = in.readString();
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

    public Modal_MenuItem() { }


    public String getItemcutdetails() { return itemcutdetails;  }

    public void setItemcutdetails(String itemcutdetails) { this.itemcutdetails = itemcutdetails;  }

    public String getItemweightdetails() {    return itemweightdetails;  }

    public void setItemweightdetails(String itemweightdetails) {  this.itemweightdetails = itemweightdetails;  }

    public String getApplieddiscountpercentage() {
        return applieddiscountpercentage;
    }

    public void setApplieddiscountpercentage(String applieddiscountpercentage) {
        this.applieddiscountpercentage = applieddiscountpercentage;    }

    public String getDunzoprice() {
        return dunzoprice;
    }

    public void setDunzoprice(String dunzoprice) {
        this.dunzoprice = dunzoprice;
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

    public String getReportname() {
        return reportname;
    }

    public void setReportname(String reportname) {
        this.reportname = reportname;
    }

    public String getSwiggyprice() {
        return swiggyprice;
    }

    public void setSwiggyprice(String swiggyprice) {
        this.swiggyprice = swiggyprice;
    }

    public String getBigbasketprice() {
        return bigbasketprice;
    }

    public void setBigbasketprice(String bigbasketprice) {
        this.bigbasketprice = bigbasketprice;
    }

    @Override
    public void writeToParcel(Parcel parcel  , int i) {
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
        parcel.writeString(reportname);
        parcel.writeString(swiggyprice);
        parcel.writeString(dunzoprice);
        parcel.writeString(bigbasketprice);
        parcel.writeString(itemweightdetails);
        parcel.writeString(itemcutdetails);
        parcel.writeString(barcode_AvlDetails);
        parcel.writeString(itemavailability_AvlDetails);
        parcel.writeString(key_AvlDetails);
        parcel.writeString(lastupdatedtime_AvlDetails);
        parcel.writeString(menuitemkey_AvlDetails);
        parcel.writeString(receivedstock_AvlDetails);
        parcel.writeString(stockbalance_AvlDetails);
        parcel.writeString(stockincomingkey_AvlDetails);
        parcel.writeString(vendorkey_AvlDetails);
        parcel.writeString(inventorydetails);
        parcel.writeString(allownegativestock);
        parcel.writeString(wholesaleprice);
        parcel.writeString(localDB_id);
        parcel.writeString(appmarkuppercentage);
        parcel.writeString(tmcpriceperkgWithMarkupValue);
        parcel.writeString(tmcpriceWithMarkupValue);
    }
}
