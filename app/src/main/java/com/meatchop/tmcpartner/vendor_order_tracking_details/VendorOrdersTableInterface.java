package com.meatchop.tmcpartner.vendor_order_tracking_details;

import com.android.volley.VolleyError;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;

import java.util.List;

public interface VendorOrdersTableInterface {

    public void notifySuccess(String requestType,   List<Modal_ManageOrders_Pojo_Class> ordersList);
    public void notifyError(String requestType, VolleyError error);


}
