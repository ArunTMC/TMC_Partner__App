package com.meatchop.tmcpartner.Settings.report_Activity_model;

public class ListItem extends ListData {
    String message, messageLine2,tokens,cutname,menuitemkey;

    public String getMenuitemkey() {   return menuitemkey;   }

    public void setMenuitemkey(String menuitemkey) {this.menuitemkey = menuitemkey;   }

    public String getCutname() {   return cutname;    }

    public void setCutname(String cutname) {
        this.cutname = cutname;
    }

    public String getTokens() {
        return tokens;
    }

    public void setTokens(String tokens) {
        this.tokens = tokens;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageLine2() {
        return messageLine2;
    }

    public void setMessageLine2(String messageLine2) {
        this.messageLine2 = messageLine2;
    }
}
