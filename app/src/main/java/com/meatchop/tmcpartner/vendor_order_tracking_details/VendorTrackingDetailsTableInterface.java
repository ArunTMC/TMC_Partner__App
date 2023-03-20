package com.meatchop.tmcpartner.vendor_order_tracking_details;

import com.android.volley.VolleyError;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;

import java.util.List;

public interface VendorTrackingDetailsTableInterface {
    public void VendorTrackingDetailsResult(List<Modal_ManageOrders_Pojo_Class> result);
    public void VendorTrackingDetailsError(VolleyError Error);

}
