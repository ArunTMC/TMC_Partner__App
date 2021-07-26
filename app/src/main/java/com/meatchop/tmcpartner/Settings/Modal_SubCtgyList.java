package com.meatchop.tmcpartner.Settings;

public class Modal_SubCtgyList {
    String key,subCtgyName,displayNo;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubCtgyName() {
        return subCtgyName;
    }

    public void setSubCtgyName(String subCtgyName) {
        this.subCtgyName = subCtgyName;
    }

    public String getDisplayNo() {
        return displayNo;
    }

    public void setDisplayNo(String displayNo) {
        this.displayNo = displayNo;
    }

    @Override
    public String toString() {
        return subCtgyName;
    }

}
