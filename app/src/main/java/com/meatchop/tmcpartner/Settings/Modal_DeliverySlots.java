package com.meatchop.tmcpartner.Settings;

public class Modal_DeliverySlots {
    String slotdatetype,key,status,slotstarttime,slotendtime,slotname ;


    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
    }

    public String getSlotdatetype() {
        return slotdatetype;
    }

    public void setSlotdatetype(String slotdatetype) {
        this.slotdatetype = slotdatetype;
    }

    public String getKey() {
        return key;
    }

    public String getSlotstarttime() {
        return slotstarttime;
    }

    public void setSlotstarttime(String slotstarttime) {
        this.slotstarttime = slotstarttime;
    }

    public String getSlotendtime() {
        return slotendtime;
    }

    public void setSlotendtime(String slotendtime) {
        this.slotendtime = slotendtime;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
