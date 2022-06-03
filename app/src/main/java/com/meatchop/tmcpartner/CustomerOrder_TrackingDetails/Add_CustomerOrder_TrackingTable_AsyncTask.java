package com.meatchop.tmcpartner.CustomerOrder_TrackingDetails;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Add_CustomerOrder_TrackingTable_AsyncTask extends AsyncTask<String, String, List<Modal_NewOrderItems>> {

    List<String> cart_item_list = new ArrayList<>();
    HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap<>();
    String paymentMode ="";
    String CouponDiscountAmount =""; String currenttime ="";
    boolean isAddCustomerDetailsFinished = false  , isAddCustomerTrackingDetailsFinished = false;
    String orderPlacedDate = "",ordertype="";
    String customermobileno ="";
    int new_totalAmount_withoutGst= 0;
    long sTime;
    String finaltoPayAmountinmethod = "",vendorKey="",vendorName="";
    Context mContext = null;
    String addApiStatus = "";

    Add_CustomerOrder_TrackingTableInterface mResultCallback_Add_CustomerOrder_TrackingTableInterface = null;


    public Add_CustomerOrder_TrackingTable_AsyncTask(Context mContext, Add_CustomerOrder_TrackingTableInterface mResultCallback_Add_CustomerOrder_TrackingTableInterface, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String paymentMode, String discountAmount, String currenttime, String customermobileno, String ordertype, String vendorKey, String vendorName, long sTime, String finaltoPayAmountinmethod) {

        this. cart_item_list = cart_item_list;
        this.cartItem_hashmap = cartItem_hashmap;
        this.paymentMode = paymentMode;
        this.CouponDiscountAmount = discountAmount;
        this.currenttime = currenttime;
        this.customermobileno = customermobileno;
        this.sTime = sTime;
        this.orderPlacedDate =getDate();
        this.finaltoPayAmountinmethod = finaltoPayAmountinmethod;
        this.ordertype = ordertype;
        this.vendorKey = vendorKey;
        this.vendorName = vendorName;
        this. mContext = mContext;
        this.mResultCallback_Add_CustomerOrder_TrackingTableInterface = mResultCallback_Add_CustomerOrder_TrackingTableInterface;
        this.  isAddCustomerDetailsFinished = false;
        this.isAddCustomerTrackingDetailsFinished = false;


    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

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



            JSONObject itemdespObject = new JSONObject();
            try {
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

        }
        else{
            StoreCoupon = "STORECOUPON";

        }

        JSONObject jsonObject = new JSONObject();
        double  CouponDiscountAmount_double =0;
        try {
            try {
                if (!CouponDiscountAmount.equals("")) {
                    CouponDiscountAmount = (CouponDiscountAmount.replaceAll("[^\\d.]", ""));
                    CouponDiscountAmount_double = Double.parseDouble(CouponDiscountAmount);
                }
                else{
                    CouponDiscountAmount_double =0;
                }


            }
            catch (Exception e){
                CouponDiscountAmount_double =0;
                e.printStackTrace();
            }
            if(CouponDiscountAmount_double>0){
                jsonObject.put("coupondiscount", CouponDiscountAmount_double);

            }
            else{
                jsonObject.put("coupondiscount", CouponDiscountAmount);

            }
            jsonObject.put("deliveryamount", 0);
            jsonObject.put("couponkey", StoreCoupon);

            jsonObject.put("ordertype", ordertype);
            jsonObject.put("gstamount", Double.parseDouble("0"));
            String deliverytype ="",slotname ="";
            if(ordertype.toUpperCase().equals(Constants.POSORDER) || ordertype.toUpperCase().equals(Constants.WholeSaleOrder)){
                deliverytype = Constants.STOREPICKUP_DELIVERYTYPE;
            }
            else{
                deliverytype = Constants.HOME_DELIVERY_DELIVERYTYPE;
                slotname = Constants.EXPRESSDELIVERY_SLOTNAME;
            }

            jsonObject.put("deliverytype", deliverytype);
            jsonObject.put("slotname", slotname);
            jsonObject.put("slotdate", orderPlacedDate);
            jsonObject.put("slottimerange", "");

            jsonObject.put("orderid", String.valueOf(sTime));
            jsonObject.put("orderplacedtime", currenttime);
            jsonObject.put("tokenno", (""));
            jsonObject.put("userid","");

            jsonObject.put("usermobileno", "+91"+customermobileno);
            jsonObject.put("vendorkey", vendorKey);
            jsonObject.put("vendorname", vendorName);
            jsonObject.put("payableamount", Double.parseDouble(finaltoPayAmountinmethod));

            jsonObject.put("paymentmode", paymentMode);
            jsonObject.put("itemdesp", itemDespArray);
            jsonObject.put("couponid","" );

            jsonObject.put("orderplaceddate", orderPlacedDate);

            jsonObject.put("merchantorderid", "");


        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_AddCustomerOrderDetails ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {

                        addApiStatus =addApiStatus + "OrderDetails_Success";
                        isAddCustomerDetailsFinished = true;
                        AddEntryInOrderTrackingDetails(currenttime,orderPlacedDate,customermobileno,sTime,vendorKey);


                    }
                    else{
                        addApiStatus =addApiStatus + "OrderDetails_ApiFailed";
                        isAddCustomerDetailsFinished = true;
                        AddEntryInOrderTrackingDetails(currenttime,orderPlacedDate,customermobileno,sTime,vendorKey);

                    }
                } catch (JSONException e) {
                    addApiStatus =addApiStatus + "OrderDetails_JSONException";
                    isAddCustomerDetailsFinished = true;
                    e.printStackTrace();
                    AddEntryInOrderTrackingDetails(currenttime,orderPlacedDate,customermobileno,sTime,vendorKey);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                addApiStatus =addApiStatus + "OrderDetails_VolleyError";
                error.printStackTrace();
                isAddCustomerDetailsFinished = true;
                AddEntryInOrderTrackingDetails(currenttime,orderPlacedDate,customermobileno,sTime,vendorKey);

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

        isAddCustomerDetailsFinished = true;
        AddEntryInOrderTrackingDetails(currenttime,orderPlacedDate,customermobileno,sTime,vendorKey);

        e.printStackTrace();
    }

       // sendResponse();







        return null;

    }

    private void AddEntryInOrderTrackingDetails(String currenttime, String orderPlacedDate, String customermobileno, long sTime, String vendorKey) {

        try {
            JSONObject orderTrackingTablejsonObject = new JSONObject();
            try {
                orderTrackingTablejsonObject.put("orderdeliverytime", currenttime);
                orderTrackingTablejsonObject.put("orderplacedtime", currenttime);
                orderTrackingTablejsonObject.put("slotdate", orderPlacedDate);

                orderTrackingTablejsonObject.put("usermobileno", "+91" + customermobileno);
                orderTrackingTablejsonObject.put("orderid", String.valueOf(sTime));
                orderTrackingTablejsonObject.put("vendorkey", vendorKey);
                orderTrackingTablejsonObject.put("orderstatus", "DELIVERED");

            } catch (JSONException e) {
                e.printStackTrace();

            }

            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST, Constants.api_AddCustomerTrackingOrderDetails,
                    orderTrackingTablejsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                    try {
                        String message = response.getString("message");
                        if (message.equals("success")) {
                            isAddCustomerTrackingDetailsFinished = true;
                            addApiStatus =addApiStatus + "TrackingDetails_Success";
                            sendResponse();
                        } else {
                            addApiStatus =addApiStatus + "TrackingDetails_ApiFailed";
                            isAddCustomerTrackingDetailsFinished = true;
                            sendResponse();
                        }
                    } catch (JSONException e) {
                        addApiStatus =addApiStatus + "TrackingDetails_JSONEXception";
                        isAddCustomerTrackingDetailsFinished = true;
                        e.printStackTrace();
                        sendResponse();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    addApiStatus =addApiStatus + "TrackingDetails_VolleyError";
                    isAddCustomerTrackingDetailsFinished = true;
                    sendResponse();
                    error.printStackTrace();
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
            jsonObjectRequest2.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Make the request
            Volley.newRequestQueue(mContext).add(jsonObjectRequest2);

        }
        catch (Exception e){
            addApiStatus =addApiStatus + "TrackingDetails_CatchError";
            isAddCustomerTrackingDetailsFinished = true;
            sendResponse();
            e.printStackTrace();
        }






    }

    private void sendResponse() {

        if(isAddCustomerDetailsFinished && isAddCustomerTrackingDetailsFinished){
                mResultCallback_Add_CustomerOrder_TrackingTableInterface.notifySuccess("",addApiStatus);



        }
        else{
            mResultCallback_Add_CustomerOrder_TrackingTableInterface.notifyError("",addApiStatus);

        }



    }


    @Override
    protected void onPostExecute( List<Modal_NewOrderItems> ordersList ) {
        super.onPostExecute(ordersList);

    }

    private String getDate() {


        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
        String CurrentDate = day.format(c);

        return CurrentDate;

    }
}
