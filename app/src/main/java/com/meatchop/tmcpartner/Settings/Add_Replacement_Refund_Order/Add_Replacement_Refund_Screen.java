package com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;







public class Add_Replacement_Refund_Screen extends AppCompatActivity {
    Button fetchOrders_buttonWidget;
    EditText customermobileno_editwidget;
    TextView vendorName_textWidget,orderscount_textwidget,orderinstruction_textview;
    ListView orders_listview;
    String vendorName,ordertype,vendorkey,orderplaceddate ="";

    LinearLayout loadingpanelmask,loadingPanel;
    static List<Modal_ManageOrders_Pojo_Class> ordersDetailsList;
    static List<Modal_ManageOrders_Pojo_Class> finalordersDetailsList ;

    static List<Modal_ManageOrders_Pojo_Class> ordersTrackingList;
    boolean is_gotResultFromOrderTracking = false;
    boolean is_gotResultFromOrderDetails = false;

    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__refund__replacement__screen);

        orderinstruction_textview = findViewById(R.id.orderinstruction_textview);
        loadingPanel = findViewById(R.id.loadingPanel);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        ordersDetailsList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        ordersTrackingList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        finalordersDetailsList = new ArrayList<Modal_ManageOrders_Pojo_Class>();

        fetchOrders_buttonWidget = findViewById(R.id.fetchOrders_buttonWidget);
        customermobileno_editwidget = findViewById(R.id.customermobileno_editwidget);
        orders_listview = findViewById(R.id.orders_listview);
        vendorName_textWidget = findViewById(R.id.vendorName_textWidget);
        orderscount_textwidget= findViewById(R.id.orderscount_textwidget);
        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorName = shared.getString("VendorName","");
            vendorkey  = shared.getString("VendorKey","");
            vendorName_textWidget.setText(String.valueOf(vendorName));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
           // orderdetailsnewschema = true;

        }
        catch(Exception e){
            e.printStackTrace();
        }

        fetchOrders_buttonWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileno = customermobileno_editwidget.getText().toString();
                if(mobileno.length()==10) {
                    finalordersDetailsList.clear();
                    ordersDetailsList.clear();
                    ordersTrackingList.clear();
                    mobileno = "+91" + mobileno;

                    is_gotResultFromOrderDetails =false;
                    is_gotResultFromOrderTracking = false;

                    String UserMobileNumberEncoded  = mobileno;
                    try {
                        UserMobileNumberEncoded = URLEncoder.encode(mobileno, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                    showProgressBar(true);
                    if (orderdetailsnewschema) {
                        String toDate = getDatewithNameoftheDay();
                        String fromDate = getNameDateoftheDay2weeksBack();

                        callVendorOrderDetailsSeviceAndInitCallBack(fromDate, toDate, vendorkey,UserMobileNumberEncoded);


                    }
                    else {

                        String finalMobileno = mobileno;
                       AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO your background code
                        FetchOrdersFromOrderTrackingDatabase(vendorName, finalMobileno);

                            }
                        });

                         FetchOrdersFromOrderDetailsDatabase(vendorName, mobileno);
                    }
                }
            }
        });


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }

    private void FetchOrdersFromOrderTrackingDatabase(String vendorName, String mobileno) {


        final String[] Count = {"0"};


        String userMobileNumber ="",userMobileNumberEncoded ="";
        userMobileNumber = mobileno;
        if (userMobileNumber.length() == 13) {
             userMobileNumberEncoded = userMobileNumber;
            try {
                userMobileNumberEncoded = URLEncoder.encode(userMobileNumber, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsusingMobileno_vendorkey +"?usermobile="+userMobileNumberEncoded+"&vendorkey="+vendorkey, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {


                            try {
                                String jsonString = response.toString();
                                //Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String message = jsonObject.getString("message").toString().toUpperCase();
                                JSONArray JArray = jsonObject.getJSONArray("content");

                                int i1 = 0;
                                int arrayLength = JArray.length();
                                ////Toast.makeText(Add_Replacement_Refund_Screen.this, "Response in Tracking", //Toast.LENGTH_SHORT).show();

                                if(arrayLength>0){
                                    for (;i1<arrayLength;i1++){
                                        String orderStatus ="",orderplacedtime ="";
                                        try {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                                            ////Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));

                                            if (json.has("orderid")) {
                                                manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                                            } else {
                                                manageOrdersPojoClass.orderid = "";
                                            }
                                            if (json.has("orderstatus")) {
                                                manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderstatus"));
                                                orderStatus =  String.valueOf(json.get("orderstatus"));
                                            } else {
                                                orderStatus  = "";
                                                manageOrdersPojoClass.orderstatus = "";
                                            }
                                            ////Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in Tracking"+orderStatus, //Toast.LENGTH_SHORT).show();


                                            if(!orderStatus.toString().toUpperCase().equals(Constants.CANCELLED_ORDER_STATUS)) {


                                                try {
                                                    if (json.has("orderplacedtime")) {
                                                        manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                                                        orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                                                    } else {
                                                        manageOrdersPojoClass.orderplacedtime = "";
                                                        orderplacedtime ="";
                                                    }
                                                } catch (Exception e) {
                                                    manageOrdersPojoClass.orderplacedtime = "";
                                                    orderplacedtime ="";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("slotdate")) {
                                                        manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));
                                                    } else {
                                                        manageOrdersPojoClass.slotdate = "";
                                                    }
                                                } catch (Exception e) {
                                                    manageOrdersPojoClass.slotdate = "";

                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("orderdeliverytime")) {
                                                        manageOrdersPojoClass.orderdeliveredtime = String.valueOf(json.get("orderdeliverytime"));
                                                    } else {
                                                        manageOrdersPojoClass.orderdeliveredtime = "";
                                                    }
                                                } catch (Exception e) {
                                                    manageOrdersPojoClass.orderdeliveredtime = "";

                                                    e.printStackTrace();
                                                }



                                                try {
                                                    String predicteddateanddayandtime = getNameDateoftheDay2weeksBack();
                                                    long predictedLongForDate = 0;
                                                    try {
                                                        if (predicteddateanddayandtime.equals("") || predicteddateanddayandtime.equals("Null")) {
                                                            predictedLongForDate = 0;
                                                        } else {
                                                            predictedLongForDate = Long.parseLong(getLongValuefortheDate(predicteddateanddayandtime));

                                                        }
                                                    } catch (Exception e) {
                                                        predictedLongForDate = 0;

                                                        e.printStackTrace();
                                                    }
                                                    long currentTimeLong = 0;

                                                    try {
                                                        if (predicteddateanddayandtime.equals("") || predicteddateanddayandtime.equals("Null")) {
                                                            currentTimeLong = 0;
                                                        } else {
                                                            currentTimeLong = Long.parseLong(getLongValuefortheDate(orderplacedtime));
                                                        }
                                                    } catch (Exception e) {
                                                        currentTimeLong = 0;

                                                        e.printStackTrace();
                                                    }


                                                    if (predicteddateanddayandtime.length() > 0 && orderplacedtime.length() > 0) {


                                                        if (predictedLongForDate <= currentTimeLong) {

                                                            ordersTrackingList.add(manageOrdersPojoClass);
                                                        }
                                                    } else {
                                                        ordersTrackingList.add(manageOrdersPojoClass);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }





                                        } catch (JSONException e) {
                                            is_gotResultFromOrderTracking = true;

                                            e.printStackTrace();

                                        }


                                        if(arrayLength - i1 == 1 ){
                                            //Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in Tracking"+String.valueOf(ordersTrackingList.size()), //Toast.LENGTH_SHORT).show();

                                            is_gotResultFromOrderTracking = true;
                                           // MergeAndDisplaytheData();
                                        }


                                    }



                                }
                                else{
                                    is_gotResultFromOrderTracking = true;

                                }

                            } catch (Exception e) {
                                //MergeAndDisplaytheData();
                                is_gotResultFromOrderTracking = true;
                                //showProgressBar(false);
                                e.printStackTrace();
                            }


                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    //Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                   // MergeAndDisplaytheData();
                    is_gotResultFromOrderTracking = true;

                    ////Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();
                    params.put("modulename", "Mobile");
                    //params.put("orderplacedtime", "12/26/2020");

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
            Volley.newRequestQueue(Add_Replacement_Refund_Screen.this).add(jsonObjectRequest);






    }


    private void FetchOrdersFromOrderDetailsDatabase(String vendorName, String mobileno) {
        final String[] Count = {"0"};


        String userMobileNumber ="";
        userMobileNumber = mobileno;
        if (userMobileNumber.length() == 13) {
            String userMobileNumberEncoded  = userMobileNumber;
            try {
                userMobileNumberEncoded = URLEncoder.encode(userMobileNumber, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsusingMobileno_vendorkey +"?usermobile="+userMobileNumberEncoded+"&vendorkey="+vendorkey, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {


                            try {
                                String jsonString = response.toString();
                                //Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String message = jsonObject.getString("message").toString().toUpperCase();
                                JSONArray JArray = jsonObject.getJSONArray("content");

                                int i1 = 0;
                                int arrayLength = JArray.length();

                                if(arrayLength>0){
                                    for (;i1<arrayLength;i1++){

                                        try {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                                            ////Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));


                                            if (json.has("coupondiscount")) {
                                                manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscount"));

                                            } else {
                                                manageOrdersPojoClass.coupondiscamount = "";
                                            }

                                            if (json.has("ordertype")) {
                                                manageOrdersPojoClass.ordertype = String.valueOf(json.get("ordertype"));

                                            } else {
                                                manageOrdersPojoClass.ordertype = "";
                                            }
                                            if (json.has("ordertype")) {
                                                manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));

                                            } else {
                                                manageOrdersPojoClass.orderType = "";
                                            }

                                            if (json.has("couponkey")) {
                                                manageOrdersPojoClass.couponkey = String.valueOf(json.get("couponkey"));

                                            } else {
                                                manageOrdersPojoClass.couponkey = "";
                                            }

                                            if (json.has("deliveryamount")) {
                                                manageOrdersPojoClass.deliveryamount = String.valueOf(json.get("deliveryamount"));

                                            } else {
                                                manageOrdersPojoClass.deliveryamount = "";
                                            }



                                            if (json.has("deliverytype")) {
                                                manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));
                                            } else {
                                                manageOrdersPojoClass.deliverytype = "";
                                            }



                                            if (json.has("orderplacedtime")) {
                                                manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                                                try{
                                                    manageOrdersPojoClass.orderplacedtime_in_long = getLongValuefortheDate(String.valueOf(json.get("orderplacedtime")));
                                                }
                                                catch (Exception e){
                                                    manageOrdersPojoClass.orderplacedtime_in_long = "0";
                                                }
                                            } else {
                                                manageOrdersPojoClass.orderplacedtime = "";
                                            }




                                            if (json.has("orderplaceddate")) {
                                                manageOrdersPojoClass.orderplaceddate = String.valueOf(json.get("orderplaceddate"));
                                                orderplaceddate = String.valueOf(json.get("orderplaceddate"));
                                                if(orderplaceddate.equals("")){
                                                    orderplaceddate =   String.valueOf(json.get("orderplacedtime"));
                                                }
                                            } else {
                                                manageOrdersPojoClass.orderplaceddate = "";
                                            }


                                            if (json.has("orderid")) {
                                                manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                                            } else {
                                                manageOrdersPojoClass.orderid = "";
                                            }


                                            if (json.has("payableamount")) {
                                                manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));

                                            } else {
                                                manageOrdersPojoClass.payableamount = "";
                                            }


                                            if (json.has("paymentmode")) {
                                                manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));

                                            } else {
                                                manageOrdersPojoClass.paymentmode = "";
                                            }


                                            if (json.has("tokenno")) {
                                                manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));

                                            } else {
                                                manageOrdersPojoClass.tokenno = "";
                                            }


                                            try {
                                                if (json.has("itemdesp")) {
                                                    JSONArray itemdesp = new JSONArray(json.getString("itemdesp"));

                                                    manageOrdersPojoClass.itemdesp = itemdesp;

                                                } else {
                                                    manageOrdersPojoClass.itemdesp = new JSONArray();
                                                    ////Log.i(Constants.TAG, "Can't Get itemDesp");
                                                }

                                            } catch (Exception e) {
                                                manageOrdersPojoClass.itemdesp = new JSONArray();

                                                e.printStackTrace();
                                            }

                                            if (json.has("useraddress")) {
                                                manageOrdersPojoClass.useraddress = String.valueOf(json.get("useraddress"));

                                            } else {
                                                manageOrdersPojoClass.useraddress = "";
                                            }
                                            if (json.has("userkey")) {
                                                manageOrdersPojoClass.userkey = String.valueOf(json.get("userkey"));

                                            } else {
                                                manageOrdersPojoClass.userkey = "";
                                            }



                                            if (json.has("usermobile")) {
                                                manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobile"));

                                            } else {
                                                manageOrdersPojoClass.usermobile = "";
                                            }


                                            if (json.has("vendorkey")) {
                                                manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));

                                            } else {
                                                manageOrdersPojoClass.vendorkey = "";
                                            }

                                            if (json.has("deliverytype")) {
                                                manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));

                                            } else {
                                                manageOrdersPojoClass.deliverytype = "";
                                            }

                                            if (json.has("slotname")) {
                                                manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                                            } else {
                                                manageOrdersPojoClass.slotname = "";
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


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                } else {


                                                    try {

                                                        manageOrdersPojoClass.slottimerange = String.valueOf(slottime);

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

                                                    } else {
                                                        manageOrdersPojoClass.slottimerange = "";
                                                    }
                                                } catch (Exception e1) {
                                                    manageOrdersPojoClass.slottimerange = "";

                                                    e1.printStackTrace();
                                                }
                                            }



                                            if (json.has("slotdate")) {
                                                manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));

                                            } else {
                                                manageOrdersPojoClass.slotdate = "";
                                            }





                                            if (json.has("vendorname")) {
                                                manageOrdersPojoClass.vendorname = String.valueOf(json.get("vendorname"));

                                            } else {
                                                manageOrdersPojoClass.vendorname = "";
                                            }


                                            //ordersDetailsList.add(manageOrdersPojoClass);
                                            try {
                                                String predicteddateanddayandtime = getNameDateoftheDay2weeksBack();
                                                long predictedLongForDate = 0;
                                              try {
                                                  if (predicteddateanddayandtime.equals("") || predicteddateanddayandtime.equals("Null")) {
                                                      predictedLongForDate = 0;
                                                  } else {
                                                      predictedLongForDate = Long.parseLong(getLongValuefortheDate(predicteddateanddayandtime));

                                                  }
                                              }
                                              catch (Exception e){
                                                  predictedLongForDate = 0;

                                                  e.printStackTrace();
                                              }
                                                long currentTimeLong =  0;

                                                try {
                                                    if (predicteddateanddayandtime.equals("") || predicteddateanddayandtime.equals("Null")) {
                                                        currentTimeLong = 0;
                                                    } else {
                                                        currentTimeLong = Long.parseLong(getLongValuefortheDate(orderplaceddate));
                                                    }
                                                }
                                                catch (Exception e){
                                                    currentTimeLong = 0;

                                                    e.printStackTrace();
                                                }


                                                if (predicteddateanddayandtime.length() > 0 && orderplaceddate.length() > 0) {


                                                    if (predictedLongForDate <= currentTimeLong) {

                                                        ordersDetailsList.add(manageOrdersPojoClass);
                                                    }
                                                }
                                                else {
                                                    ordersDetailsList.add(manageOrdersPojoClass);
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            ////Log.d(Constants.TAG, "convertingJsonStringintoArray ordersDetailsList: " + ordersDetailsList);

                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }


                                        if(arrayLength - i1 == 1 ){

                                            callMergeData();

                                            /*do {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (is_gotResultFromOrderTracking) {
                                                                    is_gotResultFromOrderDetails = true;
                                                                    //Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in details" + String.valueOf(ordersDetailsList.size()), //Toast.LENGTH_SHORT).show();
                                                                    MergeAndDisplaytheData();

                                                                }
                                                            }
                                                        });
                                                    }
                                                }, 500);


                                                if (is_gotResultFromOrderTracking) {
                                                    is_gotResultFromOrderDetails = true;
                                                    //Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in details" + String.valueOf(ordersDetailsList.size()), //Toast.LENGTH_SHORT).show();
                                                    MergeAndDisplaytheData();

                                                }



                                            }
                                            while (!is_gotResultFromOrderTracking);
                                            */

                                        }


                                    }



                                }
                                    else{
                                    is_gotResultFromOrderDetails = true;
                                    //Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in details" + String.valueOf(ordersDetailsList.size()), //Toast.LENGTH_SHORT).show();
                                    MergeAndDisplaytheData();

                                }
                            } catch (Exception e) {
                                MergeAndDisplaytheData();
                                is_gotResultFromOrderDetails = true;
                                showProgressBar(false);
                                e.printStackTrace();
                            }


                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    //Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                    showProgressBar(false);
                    MergeAndDisplaytheData();
                    is_gotResultFromOrderDetails = true;

                    ////Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

                    error.printStackTrace();
                }
            }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();
                    params.put("modulename", "Mobile");
                    //params.put("orderplacedtime", "12/26/2020");

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
            Volley.newRequestQueue(Add_Replacement_Refund_Screen.this).add(jsonObjectRequest);


        } else {
            AlertDialogClass.showDialog(Add_Replacement_Refund_Screen.this, R.string.Enter_the_mobile_no_text);

        }
    }

    private void callMergeData() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (is_gotResultFromOrderTracking) {
                            is_gotResultFromOrderDetails = true;
                            //Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in details" + String.valueOf(ordersDetailsList.size()), //Toast.LENGTH_SHORT).show();
                            MergeAndDisplaytheData();

                        }
                        else{
                            callMergeData();
                        }
                    }
                });
            }
        }, 500);




    }

    private void MergeAndDisplaytheData() {
        //Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in Tracking"+String.valueOf("Merge called"), //Toast.LENGTH_SHORT).show();
    if(ordersDetailsList.size()>0) {
        if (ordersTrackingList.size() > 0) {
            orderinstruction_textview.setVisibility(View.GONE);
            orders_listview.setVisibility(View.VISIBLE);
            try {
                for (int i = 0; i < ordersTrackingList.size(); i++) {
                    String orderidFromTracking = "", orderStatus = "",orderDeliveredTime = "";
                    try{
                        orderidFromTracking = ordersTrackingList.get(i).getOrderid().toString();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        orderStatus = ordersTrackingList.get(i).getOrderstatus().toString();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        orderDeliveredTime = ordersTrackingList.get(i).getOrderdeliveredtime().toString();
                        if(orderDeliveredTime.equals("") || orderDeliveredTime.equals("null") ){
                            orderDeliveredTime = ordersTrackingList.get(i).getSlotdate().toString();

                        }


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    for (int j = 0; j < ordersDetailsList.size(); j++) {
                        String orderidFromDetails = "";
                        orderidFromDetails = ordersDetailsList.get(j).getOrderid().toString();

                        if (orderidFromDetails.equals(orderidFromTracking)) {
                            ordersDetailsList.get(j).setOrderstatus(orderStatus);
                            ordersDetailsList.get(j).setOrderdeliveredtime(orderDeliveredTime);

                            finalordersDetailsList.add(ordersDetailsList.get(j));
                        }

                    }
                    if (ordersTrackingList.size() - i == 1) {

                        DisplayDataInUI(finalordersDetailsList);

                    }
                }


            } catch (Exception e) {
                showProgressBar(false);
                //Toast.makeText(Add_Replacement_Refund_Screen.this, "orderStatus in Tracking"+String.valueOf("Merge error1"), //Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }
        else {
            DisplayDataInUI(ordersDetailsList);

            //Toast.makeText(Add_Replacement_Refund_Screen.this, "Merge 222", //Toast.LENGTH_SHORT).show();
         /*   Collections.sort(ordersDetailsList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                    return object2.getOrderplacedtime_in_long().compareTo(object1.getOrderplacedtime_in_long());
                }
            });
            Adapter_Refund_Replacement_Listview adapter_replacement_refundProcess = new Adapter_Refund_Replacement_Listview(Add_Replacement_Refund_Screen.this, ordersDetailsList, Add_Replacement_Refund_Screen.this);
            orders_listview.setAdapter(adapter_replacement_refundProcess);

            Toast.makeText(this, "Called without status", Toast.LENGTH_SHORT).show();
            try {
                orderscount_textwidget.setText(String.valueOf(ordersDetailsList.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }


            showProgressBar(false);


          */
        }
    }
    else{
        showProgressBar(false);

        try {
            orderscount_textwidget.setText(String.valueOf(ordersDetailsList.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderinstruction_textview.setVisibility(View.VISIBLE);
        orders_listview.setVisibility(View.GONE);
    }




    }

    private void DisplayDataInUI(List<Modal_ManageOrders_Pojo_Class> finalordersDetailsList) {

        Collections.sort(finalordersDetailsList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
            public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                return object2.getOrderplacedtime_in_long().compareTo(object1.getOrderplacedtime_in_long());
            }
        });
        Adapter_Refund_Replacement_Listview adapter_replacement_refundProcess = new Adapter_Refund_Replacement_Listview(Add_Replacement_Refund_Screen.this, finalordersDetailsList, Add_Replacement_Refund_Screen.this);
        orders_listview.setAdapter(adapter_replacement_refundProcess);


        try {
            orderscount_textwidget.setText(String.valueOf(finalordersDetailsList.size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        orderinstruction_textview.setVisibility(View.GONE);
        orders_listview.setVisibility(View.VISIBLE);

        showProgressBar(false);


    }


    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey, String userMobileNo) {
        if(isVendorOrdersTableServiceCalled){
            showProgressBar(false);
            return;
        }
        isVendorOrdersTableServiceCalled = true;
        mResultCallback = new VendorOrdersTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + orderslist_fromResponse);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content",orderslist_fromResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isVendorOrdersTableServiceCalled = false;

               // mobile_jsonString = String.valueOf(jsonObject);
                finalordersDetailsList = orderslist_fromResponse;
                DisplayDataInUI(orderslist_fromResponse);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + "That didn't work!");
                showProgressBar(false);
                isVendorOrdersTableServiceCalled = false;

            }
        };
         mVolleyService = new VendorOrdersTableService(mResultCallback,Add_Replacement_Refund_Screen.this);

       // String orderDetailsURL = Constants.api_GetTestDataFromElasticache + "?fromslotdate=2022-05-23"+"&vendorkey="+vendorKey+"&toslotdate=2022-05-23"+"&usermobileno=+919698137713";
    //    String orderTrackingDetailsURL = Constants.api_GetTestDataFromWriteRedisFromDynamoDB + "?fromslotdate=2022-05-23"+"&vendorkey="+vendorKey+"&toslotdate=2022-05-23"+"&usermobileno="+userMobileNo;



        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingFromToSlotDate_vendorkey_mobileno + "?fromslotdate="+FromDate+"&vendorkey="+vendorKey+"&toslotdate="+ToDate+"&usermobileno="+userMobileNo;
       String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingFromToSlotDate_vendorkey_mobileno + "?fromslotdate="+FromDate+"&vendorkey="+vendorKey+"&toslotdate="+ToDate+"&usermobileno="+userMobileNo;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }




    public String getLongValuefortheDate(String orderplacedtime) {
        String longvalue = "";
        try {
            String time1 = orderplacedtime;
            //   //Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            Date date = sdf.parse(time1);
            long time1long = date.getTime() / 1000;
            longvalue = String.valueOf(time1long);

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                String time1 = orderplacedtime;
                //     //Log.d(TAG, "time1long  "+orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                Date date = sdf.parse(time1);
                long time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);

                //   long differencetime = time2long - time1long;
                //  //Log.d(TAG, "   "+orderplacedtime);

                //    //Log.d(TAG, "time1long  "+time1long);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return longvalue;
    }



    private String getSlotTime(String slottime, String orderplacedtime) {
        String result = "", lastFourDigits = "";
        //   //Log.d(TAG, "slottime  "+slottime);
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

                System.out.println("Time here " + sdff.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + orderplacedtime);
                result = placedtime +" - "+String.valueOf(sdff.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + result);

                result = result.replaceAll("GMT[+]05:30", "");

                //  System.out.println("Time here "+result);
            } catch (ParseException e) {
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


    private void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            //bottomNavigationView.setVisibility(View.VISIBLE);

        }

    }





    private String getDatewithNameoftheDay() {


        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();

        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

           String CurrentDate = df.format(c);
            return CurrentDate;

        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String CurrentDay = day.format(c);

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            String CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;


            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;

        }
    }



    private String getNameDateoftheDay2weeksBack() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-30);



        Date c1 = calendar.getTime();
        if(orderdetailsnewschema) {

            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            String PreviousdayDate = df1.format(c1);


            return PreviousdayDate;

        }
        else {
            SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
            previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            String PreviousdayDay = previousday.format(c1);

            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy HH:mm:ss",Locale.ENGLISH);
            df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String PreviousdayDate = df1.format(c1);
            PreviousdayDate = PreviousdayDay + ", " + PreviousdayDate;


            return PreviousdayDate;
        }
    }




}

/*
    private void FetchOrdersFromOrderTrackingDatabase(String vendorName, String mobileno) {

        String userMobileNumber = "";
        userMobileNumber = mobileno;
        if (userMobileNumber.length() == 13) {
            String userMobileNumberEncoded = userMobileNumber;
            try {
                userMobileNumberEncoded = URLEncoder.encode(userMobileNumber, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsusingMobileno_vendorkey + "?usermobile=" + userMobileNumberEncoded + "&vendorkey=" + vendorkey, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {

                            //Toast.makeText(Add_Replacement_Refund_Screen.this, "Response in Tracking", //Toast.LENGTH_SHORT).show();
                            showProgressBar(true);
                            try {
                                String jsonString = response.toString();
                                //Toast.makeText(Add_Replacement_Refund_Screen.this, "Response in Tracking", //Toast.LENGTH_SHORT).show();

                                Log.d(Constants.TAG, " response: tracking " + response);
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String message = jsonObject.getString("message").toString().toUpperCase();
                                JSONArray JArray = jsonObject.getJSONArray("content");

                                int i1 = 0;
                                int arrayLength = JArray.length();

                                if(message.equals("SUCCESS")) {
                                    for (; i1 < arrayLength; i1++) {
                                        String orderStatus ="";
                                        JSONObject json = JArray.getJSONObject(i1);
                                        Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                                        ////Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));


                                        if (json.has("orderid")) {
                                            manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                                        } else {
                                            manageOrdersPojoClass.orderid = "";
                                        }
                                        if (json.has("orderstatus")) {
                                            manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderstatus"));
                                            orderStatus =  String.valueOf(json.get("orderstatus"));
                                        } else {
                                            orderStatus  = "";
                                            manageOrdersPojoClass.orderstatus = "";
                                        }
                                        if(!orderStatus.toString().toUpperCase().equals(Constants.CANCELLED_ORDER_STATUS)) {


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
                                                String predicteddateanddayandtime = getDatewithNameofthePreviousDay();
                                                long predictedLongForDate = 0;
                                                try {
                                                    if (predicteddateanddayandtime.equals("") || predicteddateanddayandtime.equals("Null")) {
                                                        predictedLongForDate = 0;
                                                    } else {
                                                        predictedLongForDate = Long.parseLong(getLongValuefortheDate(predicteddateanddayandtime));

                                                    }
                                                } catch (Exception e) {
                                                    predictedLongForDate = 0;

                                                    e.printStackTrace();
                                                }
                                                long currentTimeLong = 0;

                                                try {
                                                    if (predicteddateanddayandtime.equals("") || predicteddateanddayandtime.equals("Null")) {
                                                        currentTimeLong = 0;
                                                    } else {
                                                        currentTimeLong = Long.parseLong(getLongValuefortheDate(orderplaceddate));
                                                    }
                                                } catch (Exception e) {
                                                    currentTimeLong = 0;

                                                    e.printStackTrace();
                                                }


                                                if (predicteddateanddayandtime.length() > 0 && orderplaceddate.length() > 0) {


                                                    if (predictedLongForDate <= currentTimeLong) {

                                                        ordersTrackingList.add(manageOrdersPojoClass);
                                                    }
                                                } else {
                                                    ordersTrackingList.add(manageOrdersPojoClass);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }




                                        if(arrayLength- i1 == 1){
                                            is_gotResultFromOrderTracking = true;
                                        }
                                    }
                                }



                                    }
                                    catch (Exception e)
                                    {
                                        is_gotResultFromOrderTracking = false;


                                        e.printStackTrace();
                                    }




                        }

                                }, new com.android.volley.Response.ErrorListener() {
@Override
public void onErrorResponse(@NonNull VolleyError error) {
        //Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());
        //Toast.makeText(Add_Replacement_Refund_Screen.this, "Response in Tracking", //Toast.LENGTH_SHORT).show();


        is_gotResultFromOrderTracking = false;


        //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
        //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
        //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

        error.printStackTrace();
        }
        }) {
@Override
public Map<String, String> getParams() throws AuthFailureError {
final Map<String, String> params = new HashMap<>();
        params.put("modulename", "Mobile");
        //params.put("orderplacedtime", "12/26/2020");

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
        Volley.newRequestQueue(Add_Replacement_Refund_Screen.this).add(jsonObjectRequest);
        }

        }

 */