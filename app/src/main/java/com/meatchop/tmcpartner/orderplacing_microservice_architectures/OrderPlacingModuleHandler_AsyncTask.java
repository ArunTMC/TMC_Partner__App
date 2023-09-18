package com.meatchop.tmcpartner.orderplacing_microservice_architectures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.settings.Modal_Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class OrderPlacingModuleHandler_AsyncTask extends AsyncTask<String, String, List<Modal_NewOrderItems>> {


    List<String> cart_item_list = new ArrayList<>();
    HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap<>();
    String paymentMode ="";
    String CouponDiscountAmount =""; String currenttime ="";
    boolean isOrderPlcaingModuleHandlerFinished = false  , isAddCustomerTrackingDetailsFinished = false;
    String orderPlacedDate = "",ordertype="";
    String customermobileno ="",tokenno ="";
    int new_totalAmount_withoutGst= 0;
    long sTime;
    String finaltoPayAmountinmethod = "",vendorKey="",vendorName="", userkey = "";
    Context mContext = null;
    String addApiStatus = "",userStatus="",userName ="",redeemPoints_String="";
    boolean isInventoryCheck = false;
    OrderPlacingModuleHandler_Interface mResultCallback_OrderPlacingModuleHandler = null;
    Modal_Address selected_Address_modal = new Modal_Address();

    String applieddiscountpercentage ="0",appMarkupPercentage = "0";
    JSONObject redeemPointsJson = new JSONObject();
    double newamountUserHaveAsCredit =0 ;
    double oldamountUserHaveAsCredit =0 ;


    public OrderPlacingModuleHandler_AsyncTask(Context mContext, OrderPlacingModuleHandler_Interface mResultCallback_OrderPlacingModuleHandler, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String paymentMode, String discountAmount, String redeemPoints_Stringg ,String currenttime, String customermobileno, String ordertype, String vendorKey, String vendorName, long sTime, String finaltoPayAmountinmethod, boolean isInventorycheck, String uniqueUserkeyFromDB, JSONObject redeemPointsJsonn, double newamountUserHaveAsCreditt ,double oldamountUserHaveAsCreditt ,  Modal_Address selected_Address_modall , String tokenNo) {

        this. cart_item_list = cart_item_list;
        this.cartItem_hashmap = cartItem_hashmap;
        this.paymentMode = paymentMode;
        this.CouponDiscountAmount = discountAmount;
        this .redeemPoints_String = redeemPoints_Stringg;
        this.currenttime = currenttime;
        this.customermobileno = customermobileno;
        this.sTime = sTime;
        this.orderPlacedDate =getDate();
        this.finaltoPayAmountinmethod = finaltoPayAmountinmethod;
        this.ordertype = ordertype;
        this.vendorKey = vendorKey;
        this.vendorName = vendorName;
        this. mContext = mContext;
        this.mResultCallback_OrderPlacingModuleHandler = mResultCallback_OrderPlacingModuleHandler;
        this.isOrderPlcaingModuleHandlerFinished = false;
        this.isAddCustomerTrackingDetailsFinished = false;
        this.selected_Address_modal = selected_Address_modall;
        this.isInventoryCheck = isInventorycheck;
        this.userkey = uniqueUserkeyFromDB;
        this.redeemPointsJson = redeemPointsJsonn;
        this.newamountUserHaveAsCredit = newamountUserHaveAsCreditt;
        this.oldamountUserHaveAsCredit = oldamountUserHaveAsCreditt;
        this.tokenno = tokenNo ;
    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @SuppressLint("LongLogTag")
    @Override
    protected List<Modal_NewOrderItems> doInBackground(String... strings) {

        try{
            JSONArray itemDespArray = new JSONArray();

            for (int i = 0; i < cart_item_list.size(); i++) {
                String itemUniqueCode = cart_item_list.get(i);
                Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);

                String menuItemKey = "";
                if (Objects.requireNonNull(modal_newOrderItems).getMenuItemId() != null) {
                    menuItemKey = modal_newOrderItems.getMenuItemId();
                } else
                {
                    menuItemKey = "";
                    if (Objects.requireNonNull(modal_newOrderItems).getKey() != null) {
                        menuItemKey = modal_newOrderItems.getKey();
                    } else {
                        menuItemKey = "";
                        if (Objects.requireNonNull(modal_newOrderItems).getMenuitemkey_AvlDetails() != null) {
                            menuItemKey = modal_newOrderItems.getMenuitemkey_AvlDetails();
                        } else {
                            menuItemKey = "";
                        }
                    }
                }

                String grossweight = "";
                if (modal_newOrderItems.getGrossweight() != null) {
                    grossweight = modal_newOrderItems.getGrossweight();

                }

                String grossWeightingrams = "";
                try {
                    if (!grossweight.equals("")) {
                        grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String ItemUniquecodeofItem = "";
                if ((modal_newOrderItems.getItemuniquecode() != null) && (!modal_newOrderItems.getItemuniquecode().equals("null")) && (!modal_newOrderItems.getItemuniquecode().equals(""))) {
                    ItemUniquecodeofItem = String.valueOf(modal_newOrderItems.getItemuniquecode());
                } else {
                    ItemUniquecodeofItem = "";
                }
                double grossweightingrams_double = 0;
                try {
                    if (!grossWeightingrams.equals("")) {
                        grossweightingrams_double = Double.parseDouble(grossWeightingrams);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String quantity = "";
                if (modal_newOrderItems.getQuantity() != null) {
                    quantity = modal_newOrderItems.getQuantity();
                    ;

                } else {
                    quantity = "";
                }


                double quantityDouble = 0;
                try {
                    if (quantity.equals("")) {
                        quantity = "1";
                    }
                    quantityDouble = Double.parseDouble(quantity);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                double grossWeightWithQuantity_double = 0;
                if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                    try {
                        grossWeightWithQuantity_double = quantityDouble;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                    try {
                        grossWeightWithQuantity_double = grossweightingrams_double * quantityDouble;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                String barcode = "";
                if (modal_newOrderItems.getBarcode() != null) {
                    barcode = String.valueOf(modal_newOrderItems.getBarcode());
                } else {
                    barcode = "";
                }


                String priceTypeForPOS = "";
                if (modal_newOrderItems.getPricetypeforpos() != null) {
                    priceTypeForPOS = String.valueOf(modal_newOrderItems.getPricetypeforpos());
                } else {
                    priceTypeForPOS = "";
                }


                String tmcCtgy = "";
                if (modal_newOrderItems.getTmcctgykey() != null) {
                    tmcCtgy = String.valueOf(modal_newOrderItems.getTmcctgykey());
                } else {
                    tmcCtgy = "";
                }


                String tmcSubCtgyKey = "";
                if (modal_newOrderItems.getTmcsubctgykey() != null) {
                    tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcsubctgykey());
                } else {
                    tmcSubCtgyKey = "";
                }


                String itemName =
                        String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());

                if (itemName.contains("Grill House")) {
                    itemName = itemName.replace("Grill House ", "");
                } else if (itemName.contains("Ready to Cook")) {
                    itemName = itemName.replace("Ready to Cook", "");
                } else {
                    itemName = itemName;
                }

                String price = "";
                if( modal_newOrderItems.getItemPrice_quantityBased()!=null){
                    price =  modal_newOrderItems.getItemPrice_quantityBased();
                }
                else{
                    price ="";
                }
                //  modal_newOrderItems.getItemPrice_quantityBased();
                String weight = "";
                if(modal_newOrderItems.getItemFinalWeight()!=null){
                    weight = modal_newOrderItems.getItemFinalWeight();

                }
                else{
                    weight = "";
                }


                String GstAmount = "";
                if(modal_newOrderItems.getGstAmount()!=null){
                    GstAmount = (modal_newOrderItems.getGstAmount());
                }
                else{
                    GstAmount ="";
                }

                String menuItemId ="";
                if( modal_newOrderItems.getMenuItemId()!=null) {
                    menuItemId =    modal_newOrderItems.getMenuItemId();
                }
                else{
                    menuItemId ="";
                }
                String netweight ="";
                if( modal_newOrderItems.getNetweight()!=null){
                    netweight = modal_newOrderItems.getNetweight();
                }
                else{
                    netweight ="";
                }

                String portionsize = "";
                if(modal_newOrderItems.getPortionsize()!=null){
                    portionsize = modal_newOrderItems.getPortionsize();
                }
                else{
                    portionsize = "";
                }


                String subCtgyKey = "";
                if(modal_newOrderItems.getTmcsubctgykey()!=null){
                    subCtgyKey =   modal_newOrderItems.getTmcsubctgykey();
                }
                else{
                    subCtgyKey = "";
                }
                if(modal_newOrderItems.getApplieddiscountpercentage()!=null){
                    applieddiscountpercentage =   modal_newOrderItems.getApplieddiscountpercentage();
                    applieddiscountpercentage = applieddiscountpercentage.replaceAll("[^\\d. ]", "");
                }
                else{
                    applieddiscountpercentage = "0";
                }
                if(modal_newOrderItems.getAppmarkuppercentage()!=null){
                    appMarkupPercentage =   modal_newOrderItems.getAppmarkuppercentage();
                    appMarkupPercentage = appMarkupPercentage.replaceAll("[^\\d. ]", "");
                }
                else{
                    appMarkupPercentage = "0";
                }


                JSONObject itemdespObject = new JSONObject();
                try {

                    try {
                        if ((!applieddiscountpercentage.equals("0")) && (!applieddiscountpercentage.equals("")) && (!applieddiscountpercentage.equals("null")) && (!applieddiscountpercentage.equals(null))) {
                            itemdespObject.put("applieddiscpercentage", applieddiscountpercentage);
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    String inventoryDetailsString = "";
                    if(modal_newOrderItems.getInventorydetails()!=null){
                        inventoryDetailsString =   modal_newOrderItems.getInventorydetails();

                    }
                    else{
                        inventoryDetailsString = "";
                    }


                    try {
                        if ((!inventoryDetailsString.equals("")) &&  (!inventoryDetailsString.equals("null")) &&  (!inventoryDetailsString.equals("nil")) && (!inventoryDetailsString.equals(null))) {
                            itemdespObject.put("inventorydetails", new JSONArray(inventoryDetailsString));
                        }
                    }
                    catch (Exception e){
                        Log.i("Oder plc Async invtrydtl err : ", String.valueOf(inventoryDetailsString));
                        e.printStackTrace();
                    }



                    try {
                        if(ordertype.toUpperCase().equals(Constants.PhoneOrder)) {
                            if ((!appMarkupPercentage.equals("0")) && (!appMarkupPercentage.equals("")) && (!appMarkupPercentage.equals("null")) && (!appMarkupPercentage.equals(null))) {
                                itemdespObject.put("appmarkuppercentage", appMarkupPercentage);
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    itemdespObject.put("menuitemid", menuItemId);
                    itemdespObject.put("itemname", itemName);
                    itemdespObject.put("tmcprice", Double.parseDouble(price));
                    itemdespObject.put("quantity", Integer.parseInt(quantity));
                    itemdespObject.put("checkouturl", "");
                    itemdespObject.put("gstamount", Double.parseDouble(GstAmount));
                    itemdespObject.put("netweight", netweight);
                    itemdespObject.put("portionsize", portionsize);
                    itemdespObject.put("tmcsubctgykey", subCtgyKey);
                    try {
                        if (weight.equals("") || weight == (null)) {
                            itemdespObject.put("grossweight", grossweight);

                            if(grossweight.equals("")){
                                itemdespObject.put("netweight", netweight);

                            }
                            else
                            {
                                itemdespObject.put("netweight", grossweight);
                                if(grossweightingrams_double!=0) {
                                    itemdespObject.put("grossweightingrams", grossweightingrams_double);
                                }


                            }
                            itemdespObject.put("weightingrams", grossweight);

                        } else {
                            itemdespObject.put("grossweight", weight);
                            if(grossweight.equals("")){
                                itemdespObject.put("netweight", netweight);

                            }
                            else
                            {
                                itemdespObject.put("netweight", weight);
                                if(grossweightingrams_double!=0) {
                                    itemdespObject.put("grossweightingrams", grossweightingrams_double);
                                }
                            }
                            itemdespObject.put("weightingrams", weight);

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                itemDespArray.put(itemdespObject);


            }
            String StoreCoupon = "";
            if((CouponDiscountAmount.equals("0"))||(CouponDiscountAmount.equals(""))||(CouponDiscountAmount.equals("0.00"))){

                StoreCoupon = "";
                if((redeemPoints_String .equals("0"))||(redeemPoints_String.equals("0.00"))||(redeemPoints_String.equals(""))) {
                    StoreCoupon = "";

                }
                else{
                    StoreCoupon = "REDEEM";

                }
            }
            else{
                StoreCoupon = "STORECOUPON";

            }

            JSONObject jsonObject = new JSONObject();
            double  CouponDiscountAmount_double =0;
            try {
                try {
                    if((!CouponDiscountAmount .equals("0")) && (!CouponDiscountAmount.equals("0.00")) && (!CouponDiscountAmount.equals(""))){
                        CouponDiscountAmount = (CouponDiscountAmount.replaceAll("[^\\d.]", ""));
                        CouponDiscountAmount_double = Double.parseDouble(CouponDiscountAmount);
                    }
                    else{
                        if((!redeemPoints_String .equals("0")) && (!redeemPoints_String.equals("0.00")) && (!redeemPoints_String.equals(""))) {
                            CouponDiscountAmount =  redeemPoints_String;
                            CouponDiscountAmount = (CouponDiscountAmount.replaceAll("[^\\d.]", ""));
                            CouponDiscountAmount_double = Double.parseDouble(CouponDiscountAmount);

                        }
                        else{
                            CouponDiscountAmount_double = 0;

                        }
                    }


                }
                catch (Exception e){
                    CouponDiscountAmount_double =0;
                    e.printStackTrace();
                }
                if(CouponDiscountAmount_double>0){
                    jsonObject.put("coupondiscount", CouponDiscountAmount_double);

                }
                else if(CouponDiscountAmount.equals("0")){

                    jsonObject.put("coupondiscount", 0);
                }
                else{
                    jsonObject.put("coupondiscount", CouponDiscountAmount);
                }

                jsonObject.put("couponkey", StoreCoupon);

                    jsonObject.put("redeempointdetails", redeemPointsJson);


                jsonObject.put("ordertype", ordertype);
                jsonObject.put("gstamount", Double.parseDouble("0"));
                String deliverytype ="",slotname ="",slottimerange ="";
                if(ordertype.toUpperCase().equals(Constants.POSORDER) || ordertype.toUpperCase().equals(Constants.WholeSaleOrder)){
                    deliverytype = Constants.STOREPICKUP_DELIVERYTYPE;
                }
                else{
                    deliverytype = Constants.HOME_DELIVERY_DELIVERYTYPE;
                    slotname = Constants.EXPRESSDELIVERY_SLOTNAME;
                }
                jsonObject.put("slotdate", orderPlacedDate);

                jsonObject.put("orderid", String.valueOf(sTime));
                jsonObject.put("orderplacedtime", currenttime);

                if(ordertype.toUpperCase().equals(Constants.PhoneOrder)) {
                    deliverytype = Constants.HOME_DELIVERY_DELIVERYTYPE;
                    slotname = "EXPRESSDELIVERY";
                    slottimerange ="120 mins";
                    jsonObject.put("deliverytype", deliverytype);
                    jsonObject.put("slotname", slotname);
                    jsonObject.put("slottimerange", slottimerange);
                    try {
                        jsonObject.put("deliveryamount", Double.parseDouble(selected_Address_modal.getDeliveryCharge()));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                        jsonObject.put("deliveryamount", Double.parseDouble("0"));
                    }
                    jsonObject.put("tokenno", (tokenno));


                    if(userStatus.toUpperCase().equals(Constants.USERSTATUS_FLAGGED)){
                        jsonObject.put("userstatus", (userStatus));

                    }
                    try{
                        jsonObject.put("userkey",String.valueOf(selected_Address_modal.getUserkey()));

                    }
                    catch (Exception e){
                        jsonObject.put("userkey", (""));
                        e.printStackTrace();
                    }

                    try{
                        jsonObject.put("useraddress",String.valueOf(selected_Address_modal.getAddressline1()));

                    }
                    catch (Exception e){
                        jsonObject.put("useraddress", (""));
                        e.printStackTrace();
                    }
                }
                else{
                    jsonObject.put("useraddress", (""));
                    jsonObject.put("userkey", (""));
                    jsonObject.put("userstatus", (""));

                    jsonObject.put("slottimerange", slottimerange);
                    jsonObject.put("deliverytype", deliverytype);
                    jsonObject.put("slotname", slotname);
                    jsonObject.put("deliveryamount", Double.parseDouble("0"));
                    jsonObject.put("tokenno", (""));
                }


                if ((!userName.equals("")) && (!userName.equals("null")) && (!userName.equals(null)) && (!userName.equals(" "))) {

                    jsonObject.put("username",userName);

                }

                if(paymentMode.toUpperCase().equals(Constants.CREDIT)){
                    if(newamountUserHaveAsCredit>0){
                        jsonObject.put("totalamountincredit", newamountUserHaveAsCredit);
                        jsonObject.put("oldamountincredit", oldamountUserHaveAsCredit);

                    }
                    else{
                        jsonObject.put("totalamountincredit", 0);
                        jsonObject.put("oldamountincredit", 0);
                    }
                }
                else{
                    jsonObject.put("totalamountincredit", 0);
                    jsonObject.put("oldamountincredit", 0);
                }


                jsonObject.put("usermobileno", "+91"+customermobileno);
                jsonObject.put("vendorkey", vendorKey);
                jsonObject.put("vendorname", vendorName);
                jsonObject.put("payableamount", Double.parseDouble(finaltoPayAmountinmethod));

                jsonObject.put("paymentmode", paymentMode);
                jsonObject.put("itemdesp", itemDespArray);

                jsonObject.put("orderplaceddate", orderPlacedDate);
                jsonObject.put("merchantpaymentid", "");

                jsonObject.put("merchantorderid", "");
                jsonObject.put("paymentdesp", "");
                jsonObject.put("paymenttype", "");
                jsonObject.put("paymentstatus", "SUCCESS");
                jsonObject.put("inventorycheck", isInventoryCheck);
                try {
                    String userLatitude ="", userLongitude ="",deliveryDistance ="";
                    double userLat_double = 0;
                    double userLon_double = 0;

                    double deliveryDistance_double =0;
                    try{
                        deliveryDistance = String.valueOf(selected_Address_modal.getDeliverydistance());


                    }
                    catch (Exception e){
                        deliveryDistance ="0";
                        e.printStackTrace();
                    }
                    try{
                        deliveryDistance = deliveryDistance.replaceAll("[^\\d.]", "");

                    }
                    catch (Exception e){
                        deliveryDistance ="0";
                        e.printStackTrace();
                    }

                    try{
                        deliveryDistance_double = Double.parseDouble(deliveryDistance);
                    }
                    catch (Exception e){
                        deliveryDistance_double =0;
                    }


                    try{
                        userLatitude = String.valueOf(selected_Address_modal.getLocationlat());

                    }
                    catch (Exception e){
                        userLatitude ="0";
                        e.printStackTrace();
                    }
                    try{
                        userLongitude = String.valueOf(selected_Address_modal.getLocationlong());

                    }
                    catch (Exception e){
                        userLongitude ="0";
                        e.printStackTrace();
                    }
                    try{
                        userLatitude = userLatitude.replaceAll("[^\\d.]", "");

                    }
                    catch (Exception e){
                        userLatitude ="0";
                        e.printStackTrace();
                    }

                    try{
                        userLat_double = Double.parseDouble(userLatitude);
                    }
                    catch (Exception e){
                        userLat_double =0;
                    }
                    try{
                        userLongitude = userLongitude.replaceAll("[^\\d.]", "");

                    }
                    catch (Exception e){
                        userLongitude ="0";
                        e.printStackTrace();
                    }

                    try{
                        userLon_double = Double.parseDouble(userLongitude);
                    }
                    catch (Exception e){
                        userLon_double =0;
                    }



                    if(ordertype.toUpperCase().equals(Constants.PhoneOrder)) {
                        jsonObject.put("orderstatus", Constants.CONFIRMED_ORDER_STATUS);
                        jsonObject.put("orderconfirmedtime", currenttime);
                        jsonObject.put("deliverydistanceinkm",deliveryDistance_double);

                        try{
                            jsonObject.put("useraddresskey", String.valueOf(selected_Address_modal.getKey()));

                        }
                        catch (Exception e){
                            jsonObject.put("useraddresskey", "");

                            e.printStackTrace();
                        }

                        try{
                            jsonObject.put("useraddresslat", userLat_double);

                        }
                        catch (Exception e){
                            jsonObject.put("useraddresslat", 0);

                            e.printStackTrace();
                        }
                        try{
                            jsonObject.put("useraddresslong", userLon_double);

                        }
                        catch (Exception e){
                            jsonObject.put("useraddresslong", "");

                            e.printStackTrace();
                        }

                    }
                    else{
                        jsonObject.put("orderstatus", "DELIVERED");
                        jsonObject.put("orderdeliverytime", currenttime);

                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            //api_AddVendorOrderDetails
            //api_AddCustomerOrderDetails
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_OrderPlacingHandlerApi ,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    Log.d("Order placing Async Response: " , String.valueOf(response));
                    try {
                        String message = "";
                            if(response.has("message")) {
                                message = response.getString("message");
                            }
                        if (message.equals("success")) {

                            addApiStatus =addApiStatus + "OrderDetails_Success";
                            isOrderPlcaingModuleHandlerFinished = true;


                        }
                        else{
                            addApiStatus =addApiStatus + "OrderDetails_ApiFailed";
                            isOrderPlcaingModuleHandlerFinished = true;

                        }
                        sendResponse();
                    } catch (JSONException e) {
                        addApiStatus =addApiStatus + "OrderDetails_JSONException";
                        isOrderPlcaingModuleHandlerFinished = true;
                        e.printStackTrace();
                        sendResponse();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    addApiStatus =addApiStatus + "OrderDetails_VolleyError";
                    error.printStackTrace();
                    isOrderPlcaingModuleHandlerFinished = true;
                    sendResponse();
                }
            }) {
                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");

                    return params;
                }
            };
            // Make the request


            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            Volley.newRequestQueue(mContext).add(jsonObjectRequest);
        }
        catch (Exception e){
            addApiStatus =addApiStatus + "OrderDetails_CatchError";

            isOrderPlcaingModuleHandlerFinished = true;
            sendResponse();
            e.printStackTrace();
        }




        return null;

    }


    private void sendResponse() {

        if(isOrderPlcaingModuleHandlerFinished ){
            mResultCallback_OrderPlacingModuleHandler.notifySuccess("",addApiStatus);



        }
        else{
            mResultCallback_OrderPlacingModuleHandler.notifyError("",addApiStatus);

        }



    }


    @Override
    protected void onPostExecute( List<Modal_NewOrderItems> ordersList ) {
        super.onPostExecute(ordersList);

    }



    private String getDate() {


        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String CurrentDate = day.format(c);

        return CurrentDate;

    }

}
