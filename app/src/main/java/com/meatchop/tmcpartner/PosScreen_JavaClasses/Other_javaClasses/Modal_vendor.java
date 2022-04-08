package com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses;

public class Modal_vendor {
    String pincode="",addressline1 ="", addressline2 ="", vendortype="",vendormobile="",status="",key="",inventorycheck = "",istestvendor ="",vendorname="";
    String get,defaultprintertype="",vendorfssaino ="",locationlat="",locationlong="",inventorycheckpos="" , minimumscreensizeforpos ="";


    public String getGet(String name) {
        String value="";
        if(name.equals("vendortype")){
            value = this.vendortype;
        }
        else if(name.equals("vendormobile")){
            value = this.vendormobile;
        }
        else if(name.equals("minimumscreensizeforpos")){
            value = this.minimumscreensizeforpos;
        }
        else if(name.equals("status")){
            value = this.status;
        }
        else if(name.equals("key")){
            value = this.key;
        }
        else if(name.equals("inventorycheck")){
            value = this.inventorycheck;
        }
        else if(name.equals("istestvendor")){
            value = this.istestvendor;
        }
        else if(name.equals("name")){
            value = this.vendorname;
        }

        else if(name.equals("addressline1")){
            value = this.addressline1;
        }

        else if(name.equals("addressline2")){
            value = this.addressline2;
        }

        else if(name.equals("pincode")){
            value = this.pincode;
        }


        else if(name.equals("locationlat")){
            value = this.locationlat;
        }

        else if(name.equals("vendorfssaino")){
            value = this.vendorfssaino;
        }

        else if(name.equals("locationlong")){
            value = this.locationlong;
        }

        else if(name.equals("inventorycheckpos")){
            value = this.inventorycheckpos;
        }
        else if(name.equals("defaultprintertype")){
            value = this.defaultprintertype;
        }

        return value;
    }

    public String getDefaultprintertype() {
        return defaultprintertype;
    }

    public void setDefaultprintertype(String defaultprintertype) {
        this.defaultprintertype = defaultprintertype;
    }

    public String getMinimumscreensizeforpos() {
        return minimumscreensizeforpos;
    }

    public void setMinimumscreensizeforpos(String minimumscreensizeforpos) {
        this.minimumscreensizeforpos = minimumscreensizeforpos;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }

    public String getVendorfssaino() {
        return vendorfssaino;
    }

    public void setVendorfssaino(String vendorfssaino) {
        this.vendorfssaino = vendorfssaino;
    }

    public String getLocationlat() {
        return locationlat;
    }

    public void setLocationlat(String locationlat) {
        this.locationlat = locationlat;
    }

    public String getLocationlong() {
        return locationlong;
    }

    public void setLocationlong(String locationlong) {
        this.locationlong = locationlong;
    }

    public String getInventorycheckpos() {
        return inventorycheckpos;
    }

    public void setInventorycheckpos(String inventorycheckpos) {
        this.inventorycheckpos = inventorycheckpos;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getVendortype() {
        return vendortype;
    }

    public void setVendortype(String vendortype) {
        this.vendortype = vendortype;
    }

    public String getVendormobile() {
        return vendormobile;
    }

    public void setVendormobile(String vendormobile) {
        this.vendormobile = vendormobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInventorycheck() {
        return inventorycheck;
    }

    public void setInventorycheck(String inventorycheck) {
        this.inventorycheck = inventorycheck;
    }

    public String getIstestvendor() {
        return istestvendor;
    }

    public void setIstestvendor(String istestvendor) {
        this.istestvendor = istestvendor;
    }
}
