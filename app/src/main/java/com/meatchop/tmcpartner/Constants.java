package com.meatchop.tmcpartner;

public class Constants {
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

    public static final String EXPRESS_DELIVERY_SLOTNAME ="EXPRESS DELIVERY";
    public static final String EXPRESSDELIVERY_SLOTNAME ="EXPRESSDELIVERY";


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




    public static final String STOREMANAGER_ROLENAME ="STOREMANAGER";
    public static final String ASSISTANTSTOREMANAGER_ROLENAME ="ASSISTANTSTOREMANAGER";



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
    public static final String api_ToSendTextMsgtoUser ="https://alerts.sinfini.com/api/web2sms.php?workingkey=Ae428c4a11138a24e41b45a0fce3576fb&sender=TMCHOP";
    public static final String api_GetTrackingOrderDetails_AppOrders ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdetailsapporders";


     public static final String api_GetTrackingOrderDetailsforDate_Vendorkey_forReport ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdertailsfordate";
    public static final String api_GetTrackingOrderDetailsforDate_Vendorkey ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/trackorderdertailsfordatenew";

    public static final String api_GetDeliverySlots = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/slotsforstoreid";



    public static final String api_addOrderDetailsInOrderDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/placeorder?modulename=PlaceOrder";
    public static final String api_addOrderDetailsInOrderItemDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/orderitem?modulename=OrderItem";
    public static final String api_addOrderDetailsInOrderTrackingDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/ordertracking?modulename=TrackOrder";
    public static final String api_addOrderDetailsInPaymentDetailsTable ="https://08klj9r8hb.execute-api.ap-south-1.amazonaws.com/dev/add/payment?modulename=Payment";
    public static final  String api_getDeliveryPartnerList = "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/deliveryuser?vendorkey=";
    public static final String api_updateMenuItemDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/menu?modulename=Menu";
    public static final String api_updateMarinadeMenuItemPriceDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/menu?modulename=MarinadeMenu";

    public static final String api_UpdateTokenNO_OrderDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/orderdetails?modulename=PlaceOrder";
    public static final String api_Update_DeliverySlotDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/deliveryslotdetails?modulename=VendorSlot";
    public static final String api_Update_DeliverySlots ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/deliveryslots?modulename=DeliverySlot";

    public static final String api_Update_DeliverySlotDetails_inmobileappData ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/deliveryuser?modulename=DeviceAppData";

    public static final String api_updateMarinadeMenuItemDetails ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/marinademenu?modulename=Menu";
    public static final String api_updateTrackingOrderTable ="https://cex0daaea6.execute-api.ap-south-1.amazonaws.com/dev/update/trackingorderdetails?modulename=TrackOrder";
    public static final String api_assignedorderdetailsfordeliveryuser ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/assignedorderdetailsfordeliveryusernew";

    public static final String api_generateTokenNo ="https://j1bqgiv357.execute-api.ap-south-1.amazonaws.com/prod/generatingtokennumber?key=";
    public static final String api_ResetTokenNo ="https://j1bqgiv357.execute-api.ap-south-1.amazonaws.com/prod/resetingtokennumber?key=";
    public static final String api_GetTokenNoUsingKey ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/key?tablename=OrderTokenNumber&key=";

    public static final String api_GetuserAddressUsingKey ="https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/record/key?tablename=Address&key=";

}