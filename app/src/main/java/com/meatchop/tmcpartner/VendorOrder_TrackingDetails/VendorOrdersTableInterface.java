package com.meatchop.tmcpartner.VendorOrder_TrackingDetails;

import com.android.volley.VolleyError;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface VendorOrdersTableInterface {

    public void notifySuccess(String requestType,   List<Modal_ManageOrders_Pojo_Class> ordersList);
    public void notifyError(String requestType, VolleyError error);


}
