package com.meatchop.tmcpartner.CustomerOrder_TrackingDetails;

import com.android.volley.VolleyError;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.Settings.Modal_OrderDetails;

import java.util.List;

public interface Add_CustomerOrder_TrackingTableInterface {

    public void notifySuccess(String requestType,   String success);
    public void notifyError(String requestType, String error);



}
