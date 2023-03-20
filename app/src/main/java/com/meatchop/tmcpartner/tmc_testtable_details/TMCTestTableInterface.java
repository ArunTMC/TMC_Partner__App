package com.meatchop.tmcpartner.tmc_testtable_details;

import java.util.List;

public interface TMCTestTableInterface {
    public void notifySuccess(String requestType,   List<Modal_TMCTestTable> list);
    public void notifyError( String error);

}
