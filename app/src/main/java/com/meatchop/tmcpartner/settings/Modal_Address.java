package com.meatchop.tmcpartner.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class Modal_Address implements Parcelable {
    String displayno = "",deliveryCharge ="",tagas,updatedtime,contactpersonmobileno="",addresstype= "", landmark="",pincode="",locationlong= "", vendorname="",userkey= "",locationlat="",key= "", deliverydistance="",vendorkey= "",addressline1="",addressline2="",contactpersonname="";
    boolean isAddressSelected = false;
    boolean isNewAddress = false;
    boolean isAddressEdited = false;

    public Modal_Address() {
        displayno = "";deliveryCharge ="";tagas="";updatedtime="";contactpersonmobileno="";addresstype= ""; landmark="";pincode="";
        locationlong= ""; vendorname="";userkey= "";locationlat="";key= ""; deliverydistance="";vendorkey= "";addressline1="";addressline2="";
        contactpersonname="";
        isAddressSelected = false;
        isNewAddress = false;
        isAddressEdited = false;
    }


    public Modal_Address(Parcel in) {
        displayno = in.readString();
        deliveryCharge = in.readString();
        tagas = in.readString();
        updatedtime = in.readString();
        contactpersonmobileno = in.readString();
        addresstype = in.readString();
        landmark = in.readString();
        pincode = in.readString();
        locationlong = in.readString();
        vendorname = in.readString();
        userkey = in.readString();
        locationlat = in.readString();
        key = in.readString();
        deliverydistance = in.readString();
        vendorkey = in.readString();
        addressline1 = in.readString();
        addressline2 = in.readString();
        contactpersonname = in.readString();
        isAddressSelected = in.readByte() != 0;
        isNewAddress = in.readByte() != 0;
        isAddressEdited = in.readByte() != 0;
    }

    public static final Creator<Modal_Address> CREATOR = new Creator<Modal_Address>() {
        @Override
        public Modal_Address createFromParcel(Parcel in) {
            return new Modal_Address(in);
        }

        @Override
        public Modal_Address[] newArray(int size) {
            return new Modal_Address[size];
        }
    };

    public String getDisplayno() {
        return displayno;
    }

    public void setDisplayno(String displayno) {
        this.displayno = displayno;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public boolean isNewAddress() {
        return isNewAddress;
    }

    public void setNewAddress(boolean newAddress) {
        isNewAddress = newAddress;
    }

    public boolean isAddressEdited() {
        return isAddressEdited;
    }

    public boolean getisAddressEdited() {
        return isAddressEdited;
    }

    public void setAddressEdited(boolean addressEdited) {
        isAddressEdited = addressEdited;
    }

    public boolean getisNewAddress() {
        return isNewAddress;
    }

    public void setIsNewAddress(boolean newAddress) {
        isNewAddress = newAddress;
    }

    public boolean getisAddressSelected() {
        return isAddressSelected;
    }

    public String getTagas() {
        return tagas;
    }

    public void setTagas(String tagas) {
        this.tagas = tagas;
    }

    public boolean isAddressSelected() {
        return isAddressSelected;
    }

    public void setAddressSelected(boolean addressSelected) {
        isAddressSelected = addressSelected;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getContactpersonmobileno() {
        return contactpersonmobileno;
    }

    public String getUpdatedtime() {
        return updatedtime;
    }

    public void setUpdatedtime(String updatedtime) {
        this.updatedtime = updatedtime;
    }

    public void setContactpersonmobileno(String contactpersonmobileno) {
        this.contactpersonmobileno = contactpersonmobileno;
    }

    public String getAddresstype() {
        return addresstype;
    }

    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocationlong() {
        return locationlong;
    }

    public void setLocationlong(String locationlong) {
        this.locationlong = locationlong;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getLocationlat() {
        return locationlat;
    }

    public void setLocationlat(String locationlat) {
        this.locationlat = locationlat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDeliverydistance() {
        return deliverydistance;
    }

    public void setDeliverydistance(String deliverydistance) {
        this.deliverydistance = deliverydistance;
    }

    public String getVendorkey() {
        return vendorkey;
    }

    public void setVendorkey(String vendorkey) {
        this.vendorkey = vendorkey;
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

    public String getContactpersonname() {
        return contactpersonname;
    }

    public void setContactpersonname(String contactpersonname) {
        this.contactpersonname = contactpersonname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(displayno);
        dest.writeString(deliveryCharge);
        dest.writeString(tagas);
        dest.writeString(updatedtime);
        dest.writeString(contactpersonmobileno);
        dest.writeString(addresstype);
        dest.writeString(landmark);
        dest.writeString(pincode);
        dest.writeString(locationlong);
        dest.writeString(vendorname);
        dest.writeString(userkey);
        dest.writeString(locationlat);
        dest.writeString(key);
        dest.writeString(deliverydistance);
        dest.writeString(vendorkey);
        dest.writeString(addressline1);
        dest.writeString(addressline2);
        dest.writeString(contactpersonname);
        dest.writeByte((byte) (isAddressSelected ? 1 : 0));
        dest.writeByte((byte) (isNewAddress ? 1 : 0));
        dest.writeByte((byte) (isAddressEdited ? 1 : 0));
    }
}
