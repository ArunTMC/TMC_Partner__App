package com.meatchop.tmcpartner.customerorder_trackingdetails;



    public interface Update_CustomerOrderDetails_TrackingTableInterface {

        public void notifySuccess(String requestType,   String success);
        public void notifyError(String requestType, String error);



    }