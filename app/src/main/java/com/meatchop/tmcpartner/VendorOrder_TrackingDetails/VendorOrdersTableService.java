package com.meatchop.tmcpartner.VendorOrder_TrackingDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_WholeSaleCustomers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class VendorOrdersTableService   {
    VendorOrdersTableInterface mResultCallback = null;
    Context mContext;
    String vendorLongitude = "",vendorLatitude ="";
    List<Modal_ManageOrders_Pojo_Class> ordersList =new ArrayList<>();
    List<Modal_ManageOrders_Pojo_Class> ordersTrackingList =new ArrayList<>();
    List<Modal_ManageOrders_Pojo_Class> final_ordersList =new ArrayList<>();

    VendorTrackingDetailsTableInterface trackingDetailsTableInterface = null;
    boolean isVendorOrderDetailsFetched = false;

    public VendorOrdersTableService(VendorOrdersTableInterface resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
        isVendorOrderDetailsFetched = false;
    }



    public void getVendorOrderDetails(  String orderDetailsURL, String orderTrackingDetailsURL){

        initVendorTrackingInterface(mContext,orderTrackingDetailsURL);
        try {
            SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);


            vendorLatitude = (shared.getString("VendorLatitude", ""));
            vendorLongitude = (shared.getString("VendorLongitute", ""));
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,orderDetailsURL ,null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            if(mResultCallback != null) {
                                try {
                                    String ordertype = "#";

                                    ordersList.clear();

                                    //converting jsonSTRING into array
                                    JSONArray JArray = response.getJSONArray("content");
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                    int i1 = 0;
                                    int arrayLength = JArray.length();
                                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                                    if(arrayLength>0) {
                                        for (; i1 < (arrayLength); i1++) {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();


                                            try {
                                                if (json.has("coupondiscount")) {
                                                    manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscount"));

                                                } else {
                                                    manageOrdersPojoClass.coupondiscamount = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.coupondiscamount = "";

                                                e.printStackTrace();
                                            }




                                            try {
                                                if (json.has("userstatus")) {
                                                    manageOrdersPojoClass.userstatus = String.valueOf(json.get("userstatus"));
                                                  //  manageOrdersPojoClass.userstatus = Constants.USERSTATUS_FLAGGED;

                                                } else {
                                                    manageOrdersPojoClass.userstatus = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.userstatus = "";

                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("username")) {
                                                    manageOrdersPojoClass.username = String.valueOf(json.get("username"));
                                                    //  manageOrdersPojoClass.userstatus = Constants.USERSTATUS_FLAGGED;

                                                } else {
                                                    manageOrdersPojoClass.username = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.username = "";

                                                e.printStackTrace();
                                            }





                                            try {
                                                if (json.has("couponkey")) {
                                                    manageOrdersPojoClass.couponkey = String.valueOf(json.get("couponkey"));

                                                } else {
                                                    manageOrdersPojoClass.couponkey = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.couponkey = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("deliveryamount")) {
                                                    manageOrdersPojoClass.deliveryamount = String.valueOf(json.get("deliveryamount"));

                                                } else {
                                                    manageOrdersPojoClass.deliveryamount = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.deliveryamount = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("deliverytype")) {
                                                    manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));

                                                } else {
                                                    manageOrdersPojoClass.deliverytype = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.deliverytype = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("gstamount")) {
                                                    manageOrdersPojoClass.GstAmount = String.valueOf(json.get("gstamount"));

                                                } else {
                                                    manageOrdersPojoClass.GstAmount = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.GstAmount = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("itemdesp")) {
                                                    manageOrdersPojoClass.itemdesp = new JSONArray(String.valueOf(json.get("itemdesp")));

                                                } else {
                                                    manageOrdersPojoClass.itemdesp = new JSONArray();
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.itemdesp = new JSONArray();


                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("itemdesp")) {
                                                    manageOrdersPojoClass.itemdesp_string = String.valueOf(json.get("itemdesp"));

                                                } else {
                                                    manageOrdersPojoClass.itemdesp_string = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.itemdesp_string = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("merchantorderid")) {
                                                    manageOrdersPojoClass.merchantorderid = String.valueOf(json.get("merchantorderid"));

                                                } else {
                                                    manageOrdersPojoClass.merchantorderid = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.merchantorderid = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("notes")) {
                                                    manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                                                } else {
                                                    manageOrdersPojoClass.notes = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.notes = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderid")) {
                                                    manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                                                } else {
                                                    manageOrdersPojoClass.orderid = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderid = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderplaceddate")) {
                                                    manageOrdersPojoClass.orderplaceddate = String.valueOf(json.get("orderplaceddate"));

                                                } else {
                                                    manageOrdersPojoClass.orderplaceddate = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderplaceddate = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("orderplacedtime")) {
                                                    manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));

                                                } else {
                                                    manageOrdersPojoClass.orderplacedtime = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderplacedtime = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("ordertype")) {
                                                    manageOrdersPojoClass.ordertype = String.valueOf(json.get("ordertype"));

                                                } else {
                                                    manageOrdersPojoClass.ordertype = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.ordertype = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("ordertype")) {
                                                    manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));

                                                } else {
                                                    manageOrdersPojoClass.orderType = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderType = "";

                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("payableamount")) {
                                                    manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));

                                                } else {
                                                    manageOrdersPojoClass.payableamount = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.payableamount = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("paymentmode")) {
                                                    manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));

                                                } else {
                                                    manageOrdersPojoClass.paymentmode = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.paymentmode = "";

                                                e.printStackTrace();
                                            }






                                            try {
                                                if (json.has("slotname")) {
                                                    manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                                                } else {
                                                    manageOrdersPojoClass.slotname = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.slotname = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("payableamount")) {
                                                    manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));

                                                } else {
                                                    manageOrdersPojoClass.payableamount = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.payableamount = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("paymentmode")) {
                                                    manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));

                                                } else {
                                                    manageOrdersPojoClass.paymentmode = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.paymentmode = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("slotdate")) {
                                                    manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));
                                                    String  slotdate = String.valueOf(json.get("slotdate"));
                                                    slotdate =  convertnewFormatDateintoOldFormat(slotdate);
                                                    manageOrdersPojoClass.slotdate = slotdate;
                                                } else {
                                                    manageOrdersPojoClass.slotdate = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.slotdate = "";

                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("slotname")) {
                                                    manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                                                } else {
                                                    manageOrdersPojoClass.slotname = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.slotname = "";

                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("slottimerange")) {
                                                    manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                                                } else {
                                                    manageOrdersPojoClass.slottimerange = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.slottimerange = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("tokenno")) {
                                                    manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));

                                                } else {
                                                    manageOrdersPojoClass.tokenno = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.tokenno = "";

                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("useraddress")) {
                                                    manageOrdersPojoClass.useraddress = String.valueOf(json.get("useraddress"));

                                                } else {
                                                    manageOrdersPojoClass.useraddress = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.useraddress = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("userkey")) {
                                                    manageOrdersPojoClass.userkey = String.valueOf(json.get("userkey"));

                                                } else {
                                                    manageOrdersPojoClass.userkey = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.userkey = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("usermobileno")) {
                                                    manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobileno"));

                                                } else {
                                                    manageOrdersPojoClass.usermobile = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.usermobile = "";

                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("vendorkey")) {
                                                    manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));

                                                } else {
                                                    manageOrdersPojoClass.vendorkey = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.vendorkey = "";

                                                e.printStackTrace();
                                            }
                                            try {
                                                if (json.has("vendorname")) {
                                                    manageOrdersPojoClass.vendorname = String.valueOf(json.get("vendorname"));

                                                } else {
                                                    manageOrdersPojoClass.vendorname = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.vendorname = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("key")) {
                                                    manageOrdersPojoClass.orderdetailskey = String.valueOf(json.get("key"));

                                                } else {
                                                    manageOrdersPojoClass.orderdetailskey = "";
                                                }
                                            } catch (Exception e) {
                                                manageOrdersPojoClass.orderdetailskey = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                String slottime = "";
                                                slottime = String.valueOf(String.valueOf(json.get("slottimerange")));
                                                String estimated_Slottime = "";
                                                if (String.valueOf(String.valueOf(json.get("slotname"))).toUpperCase().equals(Constants.EXPRESSDELIVERY_SLOTNAME)) {
                                                    String orderPlacedTime = String.valueOf(json.get("orderplacedtime"));

                                                    estimated_Slottime = getSlotTime(slottime, orderPlacedTime);


                                                    try {
                                                        manageOrdersPojoClass.slottimerange = String.valueOf(estimated_Slottime);
                                                        manageOrdersPojoClass.slotTimeRangeFromDB = String.valueOf(estimated_Slottime);


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                } else {


                                                    try {

                                                        manageOrdersPojoClass.slottimerange = String.valueOf(slottime);
                                                        manageOrdersPojoClass.slotTimeRangeFromDB = String.valueOf(slottime);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                                try {
                                                    if (json.has("slottimerange")) {
                                                        manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));
                                                        manageOrdersPojoClass.slotTimeRangeFromDB = String.valueOf(json.get("slottimerange"));

                                                    } else {
                                                        manageOrdersPojoClass.slotTimeRangeFromDB= "";
                                                    }
                                                } catch (Exception e1) {
                                                    manageOrdersPojoClass.slotTimeRangeFromDB= "";

                                                    e1.printStackTrace();
                                                }
                                            }

                                            if ((!manageOrdersPojoClass.getUsermobile().equals("+919876543210"))) {

                                                ordersList.add(manageOrdersPojoClass);
                                            }
                                            if (arrayLength - i1 == 1) {
                                                isVendorOrderDetailsFetched = true;
                                            }
                                        }
                                    }
                                    else{
                                        isVendorOrderDetailsFetched = true;
                                    }

                                } catch (JSONException e) {
                                    isVendorOrderDetailsFetched = true;

                                    e.printStackTrace();
                                }
                            }


                        }

                    },new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Toast.makeText(mContext,"There is nocc Orders Yet ",Toast.LENGTH_LONG).show();
                    if(mResultCallback != null)

                        mResultCallback.notifyError("requestType",error);

                    error.printStackTrace();
                }
            })
            {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();

                    return params;
                }



                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> header = new HashMap<>();
                    header.put("Content-Type", "application/json");

                    return header;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Make the request
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);





        }
        catch(Exception e){
            mResultCallback.notifyError("requestType", (VolleyError) e);


        }
    }

    private void initVendorTrackingInterface(Context mContext, String orderTrackingDetailsURL) {
        trackingDetailsTableInterface = new VendorTrackingDetailsTableInterface() {

            @Override
            public void VendorTrackingDetailsResult(List<Modal_ManageOrders_Pojo_Class> result) {
                ordersTrackingList = result;
                runthread();

            }

            @Override
            public void VendorTrackingDetailsError(VolleyError Error) {
                mResultCallback.notifyError("requestType",Error);

            }
        };

        VendorTrackingDetails_AsyncTask asyncTask=new VendorTrackingDetails_AsyncTask(mContext, trackingDetailsTableInterface,orderTrackingDetailsURL);
        asyncTask.execute(orderTrackingDetailsURL);

    }

    private void runthread() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isVendorOrderDetailsFetched){
                    MergeVendorTrackingAndVendorDetailsData();
                    }
                    else{
                        try {
                            Thread.sleep(50);
                            runthread();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }




                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void MergeVendorTrackingAndVendorDetailsData() throws JSONException {
        JSONArray responseJSONarray = new JSONArray();
        String deliveryDistance = "",ordertype = "",AddressKey = "";
        double deliveryDistance_double =0;
        int orderlistLength = ordersList.size();
        int orderTrackingLength =ordersTrackingList.size();
        if(orderlistLength>0 &&orderTrackingLength>0 ) {
            for (int i = 0; i < orderlistLength; i++) {
                Modal_ManageOrders_Pojo_Class orderDetailsPojoClass = ordersList.get(i);
                String orderid_OrderDetails = orderDetailsPojoClass.getOrderid();
                for (int j = 0; j < orderTrackingLength; j++) {
                    Modal_ManageOrders_Pojo_Class orderTrackingPojoClass = ordersTrackingList.get(j);
                    String orderid_TrackingDetails = orderTrackingPojoClass.getOrderid();

                   /*
                    if (orderid_OrderDetails.equals(orderid_TrackingDetails)) {
                        JSONObject response_JSONObject = new JSONObject();

                        try {

                            response_JSONObject.put("coupondiscount", orderDetailsPojoClass.getCoupondiscamount());

                        } catch (Exception e) {
                            response_JSONObject.put("coupondiscount", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("couponkey", orderDetailsPojoClass.getCouponkey());

                        } catch (Exception e) {
                            response_JSONObject.put("couponkey", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("orderdetailskey", orderDetailsPojoClass.getOrderdetailskey());

                        } catch (Exception e) {
                            response_JSONObject.put("orderdetailskey", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("deliveryamount", orderDetailsPojoClass.getDeliveryamount());

                        } catch (Exception e) {
                            response_JSONObject.put("deliveryamount", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("deliverytype", orderDetailsPojoClass.getDeliverytype());

                        } catch (Exception e) {
                            response_JSONObject.put("deliverytype", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("taxamount", orderDetailsPojoClass.getGstAmount());

                        } catch (Exception e) {
                            response_JSONObject.put("taxamount", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("itemdesp", orderDetailsPojoClass.getItemdesp());

                        } catch (Exception e) {
                            response_JSONObject.put("itemdesp", new JSONArray());
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("merchantorderid", orderDetailsPojoClass.getMerchantorderid());

                        } catch (Exception e) {
                            response_JSONObject.put("merchantorderid", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("notes", orderDetailsPojoClass.getNotes());

                        } catch (Exception e) {
                            response_JSONObject.put("notes", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("orderid", orderDetailsPojoClass.getOrderid());

                        } catch (Exception e) {
                            response_JSONObject.put("orderid", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("orderplaceddate", orderDetailsPojoClass.getOrderplaceddate());

                        } catch (Exception e) {
                            response_JSONObject.put("orderplaceddate", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("orderplacedtime", orderDetailsPojoClass.getOrderplacedtime());

                        } catch (Exception e) {
                            response_JSONObject.put("orderplacedtime", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("ordertype", orderDetailsPojoClass.getOrdertype());

                        } catch (Exception e) {
                            response_JSONObject.put("ordertype", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("payableamount", orderDetailsPojoClass.getPayableamount());

                        } catch (Exception e) {
                            response_JSONObject.put("payableamount", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("paymentmode", orderDetailsPojoClass.getPaymentmode());

                        } catch (Exception e) {
                            response_JSONObject.put("paymentmode", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("slotdate", orderDetailsPojoClass.getSlotdate());

                        } catch (Exception e) {
                            response_JSONObject.put("slotdate", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("slotname", orderDetailsPojoClass.getSlotname());

                        } catch (Exception e) {
                            response_JSONObject.put("slotname", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("slottimerange", orderDetailsPojoClass.getSlottimerange());

                        } catch (Exception e) {
                            response_JSONObject.put("slottimerange", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("tokenno", orderDetailsPojoClass.getTokenno());

                        } catch (Exception e) {
                            response_JSONObject.put("tokenno", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("useraddress", orderDetailsPojoClass.getUseraddress());

                        } catch (Exception e) {
                            response_JSONObject.put("useraddress", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("userkey", orderDetailsPojoClass.getUserkey());

                        } catch (Exception e) {
                            response_JSONObject.put("userkey", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("usermobileno", orderDetailsPojoClass.getUsermobile());

                        } catch (Exception e) {
                            response_JSONObject.put("usermobileno", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("usermobile", orderDetailsPojoClass.getUsermobile());

                        } catch (Exception e) {
                            response_JSONObject.put("usermobile", "");
                            e.printStackTrace();
                        }


                        try {

                            response_JSONObject.put("vendorkey", orderDetailsPojoClass.getVendorkey());

                        } catch (Exception e) {
                            response_JSONObject.put("vendorkey", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("vendorname", orderDetailsPojoClass.getVendorname());

                        } catch (Exception e) {
                            response_JSONObject.put("vendorname", "");
                            e.printStackTrace();
                        }


///////


                        try {

                            response_JSONObject.put("deliverydistance", orderTrackingPojoClass.getDeliverydistance());

                        } catch (Exception e) {
                            response_JSONObject.put("deliverydistance", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("useraddresskey", orderTrackingPojoClass.getUseraddresskey());

                        } catch (Exception e) {
                            response_JSONObject.put("useraddresskey", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("deliveryuserkey", orderTrackingPojoClass.getDeliveryPartnerKey());

                        } catch (Exception e) {
                            response_JSONObject.put("deliveryuserkey", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("deliveryusername", orderTrackingPojoClass.getDeliveryPartnerName());

                        } catch (Exception e) {
                            response_JSONObject.put("deliveryusername", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("orderconfirmedtime", orderTrackingPojoClass.getOrderconfirmedtime());

                        } catch (Exception e) {
                            response_JSONObject.put("orderconfirmedtime", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("orderdeliveredtime", orderTrackingPojoClass.getOrderdeliveredtime());

                        } catch (Exception e) {
                            response_JSONObject.put("orderdeliveredtime", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("orderreadytime", orderTrackingPojoClass.getOrderreadytime());

                        } catch (Exception e) {
                            response_JSONObject.put("orderreadytime", "");
                            e.printStackTrace();
                        }
                        try {

                            response_JSONObject.put("orderpickeduptime", orderTrackingPojoClass.getOrderpickeduptime());

                        } catch (Exception e) {
                            response_JSONObject.put("orderpickeduptime", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("orderStatus", orderTrackingPojoClass.getOrderstatus());

                        } catch (Exception e) {
                            response_JSONObject.put("orderStatus", "");
                            e.printStackTrace();
                        }
//
                        try {

                            response_JSONObject.put("orderstatus", orderTrackingPojoClass.getOrderstatus());

                        } catch (Exception e) {
                            response_JSONObject.put("orderstatus", "");
                            e.printStackTrace();
                        }
//
                        try {

                            response_JSONObject.put("useraddresslat", orderTrackingPojoClass.getUseraddresslat());

                        } catch (Exception e) {
                            response_JSONObject.put("useraddresslat", "");
                            e.printStackTrace();
                        }

                        try {

                            response_JSONObject.put("useraddresslong", orderTrackingPojoClass.getUseraddresslon());

                        } catch (Exception e) {
                            response_JSONObject.put("useraddresslong", "");
                            e.printStackTrace();
                        }


                        responseJSONarray.put(response_JSONObject);


                    }

                    */

                    if (orderid_OrderDetails.equals(orderid_TrackingDetails)){
                       Modal_ManageOrders_Pojo_Class pojoClass_For_FinalOrdersList = new Modal_ManageOrders_Pojo_Class();
                        try{
                            pojoClass_For_FinalOrdersList =    orderDetailsPojoClass;
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                             ordertype = orderDetailsPojoClass.getOrdertype().toString();
                        }
                        catch (Exception e){
                            try {
                                ordertype = orderDetailsPojoClass.getOrderType().toString();
                            }
                            catch (Exception e1){
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }




                        try{
                            ordersList.get(i).setDeliverydistance(orderTrackingPojoClass.getDeliverydistance().toString());
                            pojoClass_For_FinalOrdersList .setDeliverydistance(orderTrackingPojoClass.getDeliverydistance().toString());
                            deliveryDistance = String.valueOf(orderTrackingPojoClass.getDeliverydistance()) ;
                        }
                        catch (Exception e){
                            ordersList.get(i).setDeliverydistance("");
                            pojoClass_For_FinalOrdersList .setDeliverydistance("");
                            deliveryDistance = "0";
                            e.printStackTrace();
                        }


                        try{
                            deliveryDistance = deliveryDistance. replaceAll("[^\\d.]", "");
                        }
                        catch (Exception e){
                            deliveryDistance = "0";

                            e.printStackTrace();
                        }


                        try{
                            deliveryDistance_double = Double.parseDouble(deliveryDistance);

                        }
                        catch (Exception e){
                            deliveryDistance_double =0;
                            e.printStackTrace();
                        }

                        try{
                            ordersList.get(i).setUseraddresskey(orderTrackingPojoClass.getUseraddresskey().toString());
                            pojoClass_For_FinalOrdersList .setUseraddresskey(orderTrackingPojoClass.getUseraddresskey().toString());


                        }
                        catch (Exception e){
                            AddressKey="";

                            ordersList.get(i).setUseraddresskey("");
                            pojoClass_For_FinalOrdersList.setUseraddresskey("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setUsermobile(orderTrackingPojoClass.getUsermobile().toString());
                            pojoClass_For_FinalOrdersList .setUsermobile(orderTrackingPojoClass.getUsermobile().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setUsermobile("");
                            pojoClass_For_FinalOrdersList.setUsermobile("");
                            e.printStackTrace();
                        }

                        try{
                            ordersList.get(i).setDeliveryPartnerKey(orderTrackingPojoClass.getDeliveryPartnerKey().toString());
                           pojoClass_For_FinalOrdersList.setDeliveryPartnerKey(orderTrackingPojoClass.getDeliveryPartnerKey().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setDeliveryPartnerKey("");
                            pojoClass_For_FinalOrdersList .setDeliveryPartnerKey("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setDeliveryPartnerMobileNo(orderTrackingPojoClass.getDeliveryPartnerMobileNo().toString());
                            pojoClass_For_FinalOrdersList.setDeliveryPartnerMobileNo(orderTrackingPojoClass.getDeliveryPartnerMobileNo().toString());
                        }
                        catch (Exception e){
                            pojoClass_For_FinalOrdersList.setDeliveryPartnerMobileNo("");
                            ordersList.get(i).setDeliveryPartnerMobileNo("");

                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setDeliveryPartnerName(orderTrackingPojoClass.getDeliveryPartnerName().toString());
                             pojoClass_For_FinalOrdersList .setDeliveryPartnerName(orderTrackingPojoClass.getDeliveryPartnerName().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setDeliveryPartnerName("");
                            pojoClass_For_FinalOrdersList.setDeliveryPartnerName("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderconfirmedtime(orderTrackingPojoClass.getOrderconfirmedtime().toString());
                            pojoClass_For_FinalOrdersList.setOrderconfirmedtime(orderTrackingPojoClass.getOrderconfirmedtime().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderconfirmedtime("");
                            pojoClass_For_FinalOrdersList.setOrderconfirmedtime("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderconfirmedtime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderconfirmedtime().toString()));
                            pojoClass_For_FinalOrdersList.setOrderconfirmedtime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderconfirmedtime().toString()));
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderconfirmedtime_in_long("0");
                            pojoClass_For_FinalOrdersList .setOrderconfirmedtime_in_long("0");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderdeliveredtime(orderTrackingPojoClass.getOrderdeliveredtime().toString());
                           pojoClass_For_FinalOrdersList .setOrderdeliveredtime(orderTrackingPojoClass.getOrderdeliveredtime().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderdeliveredtime("");
                            pojoClass_For_FinalOrdersList .setOrderdeliveredtime("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderdeliveredtime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderdeliveredtime().toString()));
                           pojoClass_For_FinalOrdersList.setOrderdeliveredtime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderdeliveredtime().toString()));
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderdeliveredtime_in_long("0");
                            pojoClass_For_FinalOrdersList.setOrderdeliveredtime_in_long("0");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderplacedtime(orderTrackingPojoClass.getOrderplacedtime().toString());
                            pojoClass_For_FinalOrdersList.setOrderplacedtime(orderTrackingPojoClass.getOrderplacedtime().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderplacedtime("");
                            pojoClass_For_FinalOrdersList.setOrderplacedtime("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderplacedtime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderplacedtime().toString()));
                            pojoClass_For_FinalOrdersList .setOrderplacedtime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderplacedtime().toString()));
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderplacedtime_in_long("0");
                             pojoClass_For_FinalOrdersList .setOrderplacedtime_in_long("0");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderreadytime(orderTrackingPojoClass.getOrderreadytime().toString());
                            pojoClass_For_FinalOrdersList.setOrderreadytime(orderTrackingPojoClass.getOrderreadytime().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderreadytime("");
                            pojoClass_For_FinalOrdersList.setOrderreadytime("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderreadytime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderreadytime().toString()));
                           pojoClass_For_FinalOrdersList.setOrderreadytime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderreadytime().toString()));
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderreadytime_in_long("0");
                            pojoClass_For_FinalOrdersList.setOrderreadytime_in_long("0");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderpickeduptime(orderTrackingPojoClass.getOrderpickeduptime().toString());
                            pojoClass_For_FinalOrdersList.setOrderpickeduptime(orderTrackingPojoClass.getOrderpickeduptime().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderpickeduptime("");
                            pojoClass_For_FinalOrdersList .setOrderpickeduptime("");
                            e.printStackTrace();
                        }
                        try{
                            ordersList.get(i).setOrderpickeduptime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderpickeduptime().toString()));
                            pojoClass_For_FinalOrdersList  .setOrderpickeduptime_in_long(getLongValuefortheDate(orderTrackingPojoClass.getOrderpickeduptime().toString()));
                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderpickeduptime_in_long("0");
                            pojoClass_For_FinalOrdersList .setOrderpickeduptime_in_long("0");
                            e.printStackTrace();
                        }

                        try{
                            ordersList.get(i).setOrderstatus(orderTrackingPojoClass.getOrderstatus().toString());
                            pojoClass_For_FinalOrdersList.setOrderstatus(orderTrackingPojoClass.getOrderstatus().toString());

                        }
                        catch (Exception e){
                            ordersList.get(i).setOrderstatus("0");
                            pojoClass_For_FinalOrdersList.setOrderstatus("");
                            e.printStackTrace();
                        }


                        try{
                            ordersList.get(i).setUseraddresslat(orderTrackingPojoClass.getUseraddresslat().toString());
                           pojoClass_For_FinalOrdersList .setUseraddresslat(orderTrackingPojoClass.getUseraddresslat().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setUseraddresslat("");
                            pojoClass_For_FinalOrdersList .setUseraddresslat("");
                            e.printStackTrace();
                        }

                        try{
                            ordersList.get(i).setUseraddresslon(orderTrackingPojoClass.getUseraddresslon().toString());
                            pojoClass_For_FinalOrdersList .setUseraddresslon(orderTrackingPojoClass.getUseraddresslon().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setUseraddresslon("");
                            pojoClass_For_FinalOrdersList.setUseraddresslon("");
                            e.printStackTrace();
                        }
/*
                        try{
                            ordersList.get(i).setVendorkey(orderTrackingPojoClass.getVendorkey().toString());
                            pojoClass_For_FinalOrdersList .setVendorkey(orderTrackingPojoClass.getVendorkey().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setVendorkey("");
                             pojoClass_For_FinalOrdersList .setVendorkey("");
                            e.printStackTrace();
                        }

 */

                        try{
                         //  ordersList.get(i).setVendorname(orderTrackingPojoClass.getVendorname().toString());
                          //  pojoClass_For_FinalOrdersList .setVendorname(orderTrackingPojoClass.getVendorname().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setVendorname("");
                            pojoClass_For_FinalOrdersList.setVendorname("");
                            e.printStackTrace();
                        }


                        try{
                            ordersList.get(i).setPaymenttranscationimageurl(orderTrackingPojoClass.getPaymenttranscationimageurl().toString());
                            pojoClass_For_FinalOrdersList .setPaymenttranscationimageurl(orderTrackingPojoClass.getPaymenttranscationimageurl().toString());
                        }
                        catch (Exception e){
                            ordersList.get(i).setPaymenttranscationimageurl("");
                            pojoClass_For_FinalOrdersList.setPaymenttranscationimageurl("");
                            e.printStackTrace();
                        }




                        final_ordersList.add(pojoClass_For_FinalOrdersList);
                    }
                }

                if (orderlistLength - i == 1) {
                    mResultCallback.notifySuccess("requestType", final_ordersList);

                }

            }
        }
        else{
            List<Modal_ManageOrders_Pojo_Class> ordersList =new ArrayList<>();

            mResultCallback.notifySuccess("requestType", final_ordersList);

            }


        }

    public String getLongValuefortheDate(String orderplacedtime) {
        String longvalue = "";

        if(!orderplacedtime.equals("") && !orderplacedtime.equals("null") && !orderplacedtime.equals(null) ) {
            try {
                String time1 = orderplacedtime;
                //   Log.d(TAG, "time1long  "+orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                Date date = sdf.parse(time1);
                long time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);

            } catch (Exception ex) {
                //  ex.printStackTrace();
                try {
                    String time1 = orderplacedtime;

                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy",Locale.ENGLISH);
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                    Date date = sdf.parse(time1);
                    long time1long = date.getTime() / 1000;
                    longvalue = String.valueOf(time1long);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return longvalue;
    }
    private String convertnewFormatDateintoOldFormat(String todaysdate) {

        String CurrentDate ="";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String CurrentDay = day.format(date);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDate = df.format(date);

            CurrentDate = CurrentDay + ", " + CurrentDate;



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }


    private String getSlotTime(String slottime, String orderplacedtime) {
        String result = "", lastFourDigits = "";
        //   Log.d(TAG, "slottime  "+slottime);
        if (slottime.contains("mins")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                final Date date = sdf.parse(orderplacedtime);
                final Calendar calendar = Calendar.getInstance();
                String timeoftheSlot ="";
                try {
                    timeoftheSlot = (slottime.replaceAll("[^\\d.]", ""));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                int timeoftheSlotDouble =0;
                try {
                    timeoftheSlotDouble = Integer.parseInt(timeoftheSlot);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                calendar.setTime(date);
                SimpleDateFormat sdff = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
                sdff.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                String placedtime = String.valueOf(sdff.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, timeoftheSlotDouble);


                result = placedtime +" - "+String.valueOf(sdff.format(calendar.getTime()));

            } catch (ParseException e) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                final Date date;
                try {
                    date = sdf.parse(orderplacedtime);

                final Calendar calendar = Calendar.getInstance();
                String timeoftheSlot ="";
                try {
                    timeoftheSlot = (slottime.replaceAll("[^\\d.]", ""));
                }
                catch(Exception e2){
                    e2.printStackTrace();
                }
                int timeoftheSlotDouble =0;
                try {
                    timeoftheSlotDouble = Integer.parseInt(timeoftheSlot);
                }
                catch(Exception e1){
                    e1.printStackTrace();
                }
                calendar.setTime(date);
                SimpleDateFormat sdff = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
                    sdff.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                    String placedtime = String.valueOf(sdff.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, timeoftheSlotDouble);


                result = placedtime +" - "+String.valueOf(sdff.format(calendar.getTime()));
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                e.printStackTrace();
            }
        } else {
            if (slottime.length() > 5) {
                lastFourDigits = slottime.substring(slottime.length() - 5);
            } else {
                lastFourDigits = slottime;
            }

            //  result = slotdate + " " + lastFourDigits + ":00";

        }
        return result;
    }




}