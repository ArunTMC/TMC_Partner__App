package com.meatchop.tmcpartner.CustomerOrder_TrackingDetails;



    public interface Update_CustomerOrderDetails_TrackingTableInterface {

        public void notifySuccess(String requestType,   String success);
        public void notifyError(String requestType, String error);



    }