package com.meatchop.tmcpartner.settings;

public class Modal_SubCtgyList {
    String key,subCtgyName,displayNo , tmcctgykey , tmcctgyname , localDB_id;

    public String getLocalDB_id() {
        return localDB_id;
    }

    public void setLocalDB_id(String localDB_id) {
        this.localDB_id = localDB_id;
    }

    public String getTmcctgykey() {
        return tmcctgykey;
    }

    public void setTmcctgykey(String tmcctgykey) {
        this.tmcctgykey = tmcctgykey;
    }

    public String getTmcctgyname() {
        return tmcctgyname;
    }

    public void setTmcctgyname(String tmcctgyname) {
        this.tmcctgyname = tmcctgyname;
    }

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
