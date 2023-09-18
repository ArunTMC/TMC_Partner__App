package com.meatchop.tmcpartner.orderplacing_microservice_architectures;

public interface OrderPlacingModuleHandler_Interface {

    public void notifySuccess(String requestType,   String success);
    public void notifyError(String requestType, String error);



}
