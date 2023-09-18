package com.meatchop.tmcpartner.settings;

import android.os.Parcel;
import android.os.Parcelable;

public class Modal_CreditOrderDetails implements Parcelable {
    String  usermobileno, totalamountincredityettobepaid,lastupdatedtime,vendorkey,totalAddedAmount="0",totalPaidAmount ="0", totalCancelledAmount = "0",
            totalAmountGivenAsCredit;

    protected Modal_CreditOrderDetails(Parcel in) {
        usermobileno = in.readString();
        totalamountincredityettobepaid = in.readString();
        lastupdatedtime = in.readString();
        vendorkey = in.readString();
        totalAddedAmount = in.readString();
        totalPaidAmount = in.readString();
        totalCancelledAmount = in.readString();
        totalAmountGivenAsCredit = in.readString();
    }

    public static final Creator<Modal_CreditOrderDetails> CREATOR = new Creator<Modal_CreditOrderDetails>() {
        @Override
        public Modal_CreditOrderDetails createFromParcel(Parcel in) {
            return new Modal_CreditOrderDetails(in);
        }

        @Override
        public Modal_CreditOrderDetails[] newArray(int size) {
            return new Modal_CreditOrderDetails[size];
        }
    };

    public Modal_CreditOrderDetails() {

    }

    public String getTotalAddedAmount() {
        return totalAddedAmount;
    }

    public void setTotalAddedAmount(String totalAddedAmount) {
        this.totalAddedAmount = totalAddedAmount;
    }

    public String getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public void setTotalPaidAmount(String totalPaidAmount) {
        this.totalPaidAmount = totalPaidAmount;
    }

    public String getUsermobileno() {
        return usermobileno;
    }

    public void setUsermobileno(String usermobileno) {
        this.usermobileno = usermobileno;
    }

    public String getTotalAmountGivenAsCredit() {
        return totalAmountGivenAsCredit;
    }

    public void setTotalAmountGivenAsCredit(String totalAmountGivenAsCredit) {
        this.totalAmountGivenAsCredit = totalAmountGivenAsCredit;
    }

    public String getTotalamountincredityettobepaid() {
        return totalamountincredityettobepaid;
    }

    public void setTotalamountincredityettobepaid(String totalamountincredityettobepaid) {
        this.totalamountincredityettobepaid = totalamountincredityettobepaid;
    }

    public String getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(String lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
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

    public String getTotalCancelledAmount() {
        return totalCancelledAmount;
    }

    public void setTotalCancelledAmount(String totalCancelledAmount) {
        this.totalCancelledAmount = totalCancelledAmount;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(usermobileno);
        dest.writeString(totalamountincredityettobepaid);
        dest.writeString(lastupdatedtime);
        dest.writeString(vendorkey);
        dest.writeString(totalAddedAmount);
        dest.writeString(totalPaidAmount);
        dest.writeString(totalCancelledAmount);
        dest.writeString(totalAmountGivenAsCredit);
    }
}
