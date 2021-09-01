package com.meatchop.tmcpartner;

public class Constants {

    public static final int default_mobileScreenSize  = 8;
    public static final String Enter_the_mobile_no_text ="Enter the Mobile Number";
    public static final String Order_Value_should_be_above  ="Order Value Should Be Above : ";
    public static final String PointusercanRedeemtoday  ="Can't redeem more than users limit per day";
    public static final String FirstApplyRedeemPoints_Instruction  ="You have to First Apply Redeem Points ";
    public static final String RedeemPoints_and_Discount_Instruction  ="To Enter Redeem Points Make Sure Manual discount amount is not more than zero";
    public static final String DiscountAmountInstruction  ="Discount Amount Should Less than the Bill Value";

    public static final String RedeemPoints_and_Discount_Instruction2  ="To Enter discount Points Make Sure redeem point is not more than zero";
    public static final String RedeemPointsDetailsIsNotExistedInstruction  ="This User didn't saved any redeem points yet.";
    public static final String RedeemPointsDetailsTryAgainInstruction  ="Can't get Users Redeem points Details . Try Again.";

    public static final String CantApplyDiscountInstruction  =" Can't Apply discount when Cart is Empty";
    public static final String AddedRedeemPointsCancelledInstruction  ="Redeem Points Added in this Order is cancelled . So Kindly Add again";

    public static final String RecievedAmountShouldBeGreaterthanZero  ="Recieved Amount Should be Greater than Zero";
    public static final String RecievedAmountCantbeEmpty  ="Recieved Amount Column Can't Be Empty";
    public static final String RecievedAmountShouldBeGreaterthanTotalAmount  ="Recieved Amount Should not be Less than total Amount";


    private static final String PACKAGE_NAME = "com.example.googlemap";
    public static final String TotalLocationResult = PACKAGE_NAME +".RESULT_DATA_KEY";
    public static final String RESULT_ADDRESS = PACKAGE_NAME +".RESULT_ADDRESS";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public   static final String LOCATION_DATA_EXTRA =PACKAGE_NAME +".LOCATION_DATA_EXTRA";
    public static final String AreaNameOfLocation =PACKAGE_NAME +".LOCATION_NAME_DATA_EXTRA";
    public static final int USE_ADDRESS_NAME = 1;
    public static final String FETCH_TYPE_EXTRA = PACKAGE_NAME +".FETCH_TYPE_EXTRA";
    public static final int USE_ADDRESS_LOCATION = 1;

    public static final int SUCCESS_RESULT =1;
    public static final int FAILURE_RESULT =0;




    public static final String TAG ="Tag";
    public static final String NEW_ORDER_STATUS ="NEW";
    public static final String CONFIRMED_ORDER_STATUS ="CONFIRMED";
    public static final String READY_FOR_PICKUP_ORDER_STATUS ="READY FOR PICKUP";
    public static final String PICKEDUP_ORDER_STATUS ="PICKED UP";
    public static final String DELIVERED_ORDER_STATUS ="DELIVERED";
    public static final String CANCELLED_ORDER_STATUS ="CANCELLED";
    public static final String PREORDER_SLOTNAME ="PREORDER";
    public static final String TODAYPREORDER_SLOTNAME ="TODAY";
    public static final String TOMORROWPREORDER_SLOTNAME ="TOMORROW";
    public static final String HOME_DELIVERY_DELIVERYTYPE ="HOMEDELIVERY";
    public static final String STOREPICKUP_DELIVERYTYPE ="STOREPICKUP";

    public static final String SPECIALDAYPREORDER_SLOTNAME ="SPECIALDAYPREORDER";

    public static final String EXPRESS_DELIVERY_SLOTNAME ="EXPRESS DELIVERY";
    public static final String EXPRESSDELIVERY_SLOTNAME ="EXPRESSDELIVERY";
    public static final String DUNZOORDER_PAYMENTMODE ="DUNZOPAYMENT";

    public static final String SWIGGYORDER_PAYMENTMODE ="SWIGGYPAYMENT";
    public static final String DunzoOrder ="DUNZOORDER";
    public static final String SwiggyOrder ="SWIGGYORDER";
    public static final String PhoneOrder ="PHONEORDER";

    public static final String POSORDER ="POSORDER";

    public static final String APPORDER ="APPORDER";
    public static final String RAZORPAY ="RAZORPAY";
    public static final String PAYTM ="PAYTM";
    public static final String PHONEPE ="PHONEPE";
    public static final String DELIVERED_STATUS ="DELIVERED";

    public static final String TMCPRICEPERKG ="TMCPRICEPERKG";
    public static final String TMCPRICE ="TMCPRICE";

    public static final String CASH_ON_DELIVERY ="CASH ON DELIVERY";
    public static final String CASH ="CASH";
    public static final String UPI ="UPI";
    public static final String CARD ="CARD";
    public static final String Upi ="Upi";
    public static final String Card ="Card";



    public static final String ADMIN_ROLENAME ="ADMIN";
    public static final String STOREMANAGER_ROLENAME ="STOREMANAGER";
    public static final String ASSISTANTSTOREMANAGER_ROLENAME ="ASSISTANTSTOREMANAGER";
    public static final String SUPERADMIN_ROLENAME ="SUPERADMIN";
    public static final String CASHIER_ROLENAME ="CASHIER";
    public static final String REPORTSVIEWER_ROLENAME ="REPORTSVIEWER";
    public static final String DELIVERYMANAGER_ROLENAME ="DELIVERYMANAGER";

    public static final String SALESMODULE ="salesmodule";
    public static final String MANAGEMENUMODULE ="managemenumodule";
    public static final String MANAGEORDERSMODULE ="manageordersmodule";
    public static final String VIEWORDERSMODULE ="viewordersmodule";
    public static final String DATAANALYTICSMODULE ="dataanalyticsmodule";
    public static final String GENERALMODULE ="generalmodule";
    public static final String PAYTM_SUCCESSSTATUS ="TXN_SUCCESS";
    public static final String RAZORPAY_SUCCESSSTATUS ="paid";


    public static final String api_getListofSubCtgy = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/list?modulename=SubCategory";

    public static final String api_getListofVendors = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/list";
    public static final String api_verifyVendorPassword ="https://9z1mg6qh3a.execute-api.ap-south-1.amazonaws.com/stage/resource";
    public static final String api_updateUserDetails ="https://pyyt2a49ed.execute-api.ap-south-1.amazonaws.com/Dev/resource";
    public static final String api_getListofMenuItems ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/menuforstore";
    public static final String api_getListofMarinadeMenuItems ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/marinademenuforstore";
    public static final String api_GetMenuCategory = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/list";
    public static final  String api_GetDeliverySlotDetails = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/slotdetailsforstore";
   public static final String api_GetTrackingOrderDetailsforSlotDate_Vendorkey_forReport ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailsforslotdate";
    public static final String api_GetTrackingOrderDetailsforSlotDate_Vendorkey ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailsforslotdatenew";
            //"https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/resource";
    //"https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailsforslotdatenew"
    public static final String api_ToSendTextMsgtoUser ="https://alerts.sinfini.com/api/web2sms.php?workingkey=Ae428c4a11138a24e41b45a0fce3576fb&sender=TMCHOP";
    public static final String api_GetTrackingOrderDetails_AppOrders ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailsapporders";
            //"https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/resource";

            //"https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailsapporders";
    public static final String api_GetTrackingOrderDetails_AppOrders_and_PosOrders ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailsappandposorders";

    public static final String api_GetAddressUsingUserKey= "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/key?tablename=Address&key=";

    public static final String api_GetTrackingOrderDetails_orderid ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetails?orderid=";

    public static final String api_GetTrackingOrderDetailswithDate_forReport ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/gettrackingorderdetailsusingdateforreport";
    public static final String api_GetTrackingOrderDetailsUsingSlotDate_forReport ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/gettrackorderdetailsusingslotdateforreport";
    public static final String api_GetTrackingOrderDetails_forReport_AppOrders_and_PosOrders ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/gettrackorderdetailsforposandapporders";
    public static final String api_GetOrderDetailsusingOrderid= "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/orderdetails?orderid=";

    public static final String api_GetRatingDetailsUsingVendorid= "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/ratingorderdetailsforvendorid";



    public static final String api_MenuAvailabilityTransaction ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/getmenuavailabilitytransctionstatus";

     public static final String api_GetTrackingOrderDetailsforDate_Vendorkey_forReport ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdertailsfordate";
    public static final String api_GetTrackingOrderDetailsforDate_Vendorkey ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdertailsfordatenew";
    public static final String api_GetTrackingOrderDetails_cancelledOrders="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailscancelledorders";

    public static final String api_GetDeliverySlots = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/slotsforstoreid";

    public static final String api_addCouponDetailsInCouponTranactionsTable = "https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/coupontrans?modulename=CouponTrans";
    public static final String api_updateRedeemPointsTablewithkey ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/redeempointsdetails?modulename=RedeemPoints";
    public static final String api_updateRedeemPointsTablewithoutKey ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/updareoraddredeempointdetails";

    public static final String api_addOrderDetailsInOrderDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/placeorder?modulename=PlaceOrder";
    public static final String api_addOrderDetailsInOrderItemDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/orderitem?modulename=OrderItem";
    public static final String api_addOrderDetailsInOrderTrackingDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/ordertracking?modulename=TrackOrder";
    public static final String api_addOrderDetailsInPaymentDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/payment?modulename=Payment";
    public static final  String api_getDeliveryPartnerList = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/deliveryuser?vendorkey=";
    public static final String api_updateMenuItemDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/menu?modulename=MenuNew";
    public static final String api_updateMarinadeMenuItemPriceDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/menu?modulename=MarinadeMenuNew";
    public static final String api_addMenuavailabilityTransaction ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/menuavailabilitytransaction?modulename=MenuAvailTrans";

    public static final String api_UpdateTokenNO_OrderDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/orderdetails?modulename=PlaceOrder";
    public static final String api_Update_DeliverySlotDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/deliveryslotdetails?modulename=VendorSlot";
    public static final String api_Update_DeliverySlots ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/deliveryslots?modulename=DeliverySlot";
    public static final String api_updatePaymentTransactionTable ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/paymenttrans?modulename=Payment";
    public static final String api_Update_ChangeMenuItemAvailability_SubCtgywise ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/changemenuitemavailabilitystatussubctgywise";
    public static final String api_updatePaymentMode_OrderDetailsTable ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/orderdetails?modulename=PlaceOrder";

    public static final String api_updateMarinadeMenuItemDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/menu?modulename=MarinadeMenuNew";
    public static final String api_updateTrackingOrderTable ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/trackingorderdetails?modulename=TrackOrder";
    public static final String api_assignedorderdetailsfordeliveryuser ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/assignedorderdetailsfordeliveryusernew";

    public static final String api_generateTokenNo ="https://j1bqgiv357.execute-api.ap-south-1.amazonaws.com/prod/generatingtokennumber?key=";
    public static final String api_ResetTokenNo ="https://j1bqgiv357.execute-api.ap-south-1.amazonaws.com/prod/resetingtokennumber?key=";
    public static final String api_GetTokenNoUsingKey ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/key?tablename=OrderTokenNumber&key=";
    public static final String api_getAllUserswithPagenation ="https://irahdrz4w2.execute-api.ap-south-1.amazonaws.com/stage/resource?modulename=user";
    public static final String api_getAllAddresswithPagenation ="https://irahdrz4w2.execute-api.ap-south-1.amazonaws.com/stage/resource?modulename=address";
    public static final String api_getAllOrderItemDetailswithPagenation ="https://irahdrz4w2.execute-api.ap-south-1.amazonaws.com/stage/resource?modulename=orderitems";
    public static final String api_GetPaymentTransactionusingOrderid= "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/getpaymenttransationfororderid?orderid=";
    public static final String api_GetPaymentDetailsFromRazorpay= "https://us-central1-dosavillage-acc39.cloudfunctions.net/getrazorpayorderstatusfortmc?orderid=";
    public static final String api_GetPaymentDetailsFromPaytm= "https://us-central1-dosavillage-acc39.cloudfunctions.net/getpaytmorderstatusfortmc?orderid=";
    public static final String  api_GetMobileAppData ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/list?modulename=Mobile";
            //"https://1gvpvgme35.execute-api.ap-south-1.amazonaws.com/stage/resource";
            public static final String  api_GetRedeemPointsDetailsFortheUser ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/getredeempointdetailsforuser";

}