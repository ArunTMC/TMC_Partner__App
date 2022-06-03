package com.meatchop.tmcpartner.VendorOrder_TrackingDetails;

import com.android.volley.VolleyError;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;

import java.util.List;

public interface VendorTrackingDetailsTableInterface {
    public void VendorTrackingDetailsResult(List<Modal_ManageOrders_Pojo_Class> result);
    public void VendorTrackingDetailsError(VolleyError Error);

}
