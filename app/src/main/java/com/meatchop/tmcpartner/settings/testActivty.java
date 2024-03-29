package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.asynctaskforprinter.AsyncTakWithServiceForPrintReceipt;
import com.meatchop.tmcpartner.asynctaskforprinter.AsyncTaskListener_forPrintReceipt;
import com.meatchop.tmcpartner.asynctaskforprinter.Modal_forPrintReceipt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class testActivty extends AppCompatActivity {
    public TextView ponits_redeemed_text_widget,end_text_widget,error_text_widget,result_text_widget;
    public TextView ponits_redeemed_text_widgetnew,end_text_widgetnew,error_text_widgetnew,result_text_widgetnew;
    Button uploadNotesnew,kalyanNotes,ACNotes,kalyanNotesnew,ACNotesnew,orderdetailsorderidold,orderdetailsmobilenoold,ordertrackdetailsmobilenoold;
    Button uploadNotes,connect_device_button_widget,checkout_widget,redeemPoints_button_widget,print_button_widget;
    String totalbillAmount = "500",vendorKey,vendorname,finaltoPayAmountwithRedeemPoints,maxpointsinaday_String,minordervalueforredeem_String,
            pointsfor100rs_String,redeemPoints_String,redeemKey,mobileno_redeemKey,discountAmountalreadyusedtoday
    ,totalpointsredeemedalreadybyuser,totalordervalue_tillnow,totalredeempointsuserhave;
    int maxpointsinaday_int,minordervalueforredeem_int,pointsfor100rs_int;
    int pointsalreadyredeemedInteger,totalpointsuserhave_afterapplypoints;
    String pointsenteredToredeem;
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay,OrderTypefromSpinner;
    LinearLayout loadingPanel,loadingpanelmask,discountAmountLayout;
    boolean ispointsApplied_redeemClicked=false,isProcedtochecckoutClicked=false;
    EditText printEditText_widget;
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;
    AsyncTaskListener_forPrintReceipt callBackAsyncTaskListener_forPrintReceipt = null ;

    String starttime="",endtime="";
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;


    String mConnectedDeviceName ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_activty);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        redeemPoints_button_widget = findViewById(R.id.redeemPoints_widget);
        ponits_redeemed_text_widget= findViewById(R.id.ponits_redeemed_text_widget);
        checkout_widget = findViewById(R.id.checkout_widget);
        printEditText_widget = findViewById(R.id.printEditText_widget);
        connect_device_button_widget = findViewById(R.id.connect_device_button_widget);
        uploadNotes = findViewById(R.id.uploadNotes);
        error_text_widget = findViewById(R.id.error_text_widget);
        end_text_widget = findViewById(R.id.end_text_widget);
        print_button_widget = findViewById(R.id.print_button_widget);
        result_text_widget = findViewById(R.id.result_text_widget);
        ACNotes  = findViewById(R.id.ACNotes);
        kalyanNotes  = findViewById(R.id.kalyanNotes);

        uploadNotesnew = findViewById(R.id.uploadNotesnew);
        error_text_widgetnew = findViewById(R.id.error_text_widgetnew);
        end_text_widgetnew = findViewById(R.id.end_text_widgetnew);
        ponits_redeemed_text_widgetnew = findViewById(R.id.ponits_redeemed_text_widgetnew);
        result_text_widgetnew = findViewById(R.id.result_text_widgetnew);
        ACNotesnew  = findViewById(R.id.ACNotesnew);
        kalyanNotesnew  = findViewById(R.id.kalyanNotesnew);
        orderdetailsorderidold = findViewById(R.id.orderdetailsorderidold);
        orderdetailsmobilenoold  = findViewById(R.id.orderdetailsmobilenoold);
        ordertrackdetailsmobilenoold =  findViewById(R.id.ordertrackdetailsmobilenoold);
        /*try{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                Toast.makeText(testActivty.this, "Bluetooth is not Supported", Toast.LENGTH_LONG).show();
                return;
            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the chat session
            } else {
                if (mChatService == null) setupChat();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

         */
        try {
            SharedPreferences sharedd = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (sharedd.getString("VendorKey", "vendor_1"));
            vendorname = (sharedd.getString("VendorName", ""));
            SharedPreferences shared = getSharedPreferences("RedeemData", MODE_PRIVATE);
            maxpointsinaday_String = (shared.getString("maxpointsinaday", ""));
            maxpointsinaday_int = Integer.parseInt(maxpointsinaday_String);
            minordervalueforredeem_String = (shared.getString("minordervalueforredeem", ""));
            minordervalueforredeem_int = Integer.parseInt(minordervalueforredeem_String);
            pointsfor100rs_String = (shared.getString("pointsfor100rs", ""));
            pointsfor100rs_int = Integer.parseInt(pointsfor100rs_String);

        }
        catch (Exception e){
            e.printStackTrace();
        }
   //     getNotesData();
    showProgressBar(false);

        ACNotesnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetchOrderDetailsnewusingmobileno("7010779096");

                showProgressBar(true);
                Modal_forPrintReceipt modal_forPrintReceipt = new Modal_forPrintReceipt();
                modal_forPrintReceipt.setOrderid("grgrgrgre");
                modal_forPrintReceipt.setOrdertype("msmrgorglse");
                Modal_Address selected_Address_modal = new Modal_Address();

                AsyncTakWithServiceForPrintReceipt asyncTaskForPrintingReceipt = new AsyncTakWithServiceForPrintReceipt(getApplicationContext() , callBackAsyncTaskListener_forPrintReceipt,modal_forPrintReceipt,myReceiverforPrintingService , selected_Address_modal);
                asyncTaskForPrintingReceipt.execute();

            }
        });

        kalyanNotesnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetchOrderDetailsnewusingmobileno("9790935376");
                fetchorderFromOrderTrackdetailsmobilenoindex();



            }
        });
        ordertrackdetailsmobilenoold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchorderFromOrderTrackdetailsmobilenoold();

            }
        });

        uploadNotesnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // fetchOrderDetailsnewusingmobileno("9597580128");
              //  fetchOrderDetailsnewusingmobileno("pt_orderid_idx");
                fetchOrderDetailsfrompaymentOrderid();

            }
        });



        orderdetailsmobilenoold .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchorderFromOrderdetailsmobilenoold();

            }
        });


        ACNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fetchOrderDetailsnewDumpusingmobileno("7010779096");
                fetchorderFromOrderdetailsmobilenoindex();

            }
        });
        orderdetailsorderidold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchorderFromOrderdetailsorderidold();

            }
        });

        kalyanNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchorderFromOrderdetailsorderidindex();
              //  fetchOrderDetailsnewDumpusingmobileno("9790935376");

            }
        });
        uploadNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


              //  fetchOrderDetailsnewDumpusingmobileno("9597580128");
                //fetchOrderDetailsnewDumpusingmobileno("pt_attr4_orderid_idx");
              //  fetchOrderDetailsfrompaymentOrderidwithIndex("Paymenttransactions_orderid_idx1");

               // fetchorderFromOrderdetailsorderidindex();
                 /*
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("mobileno", "+919597580128");
                    jsonObject.put("notes", "1.LEARN THE MOVES. Each chess piece can move only a certain way. \n 2.OPEN WITH A PAWN. \n 3.Move the pawn in front of either the king or queen two squares forward. \n 4.GET THE KNIGHTS AND BISHOPS OUT. \n 4.WATCH YOUR BACK! \n 5.DON'T WASTE TIME. \n 6.“CASTLE” EARLY. \n 7.ATTACK IN THE “MIDDLEGAME” \n 8.LOSE PIECES WISELY..");
                    jsonObject.put("createdtime", "Wed, 19 Jan 2022 10:25:36");


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://3xs79s0rob.execute-api.us-west-1.amazonaws.com/dev/notes/add",
                        jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        try {
                            String message = response.getString("message");
                            if (message.equals("success")) {

                            }
                            else{

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {

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
                // Make the request
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);





             */
            }
        });


        print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_to_print = printEditText_widget.getText().toString();


                try {
                    if (BluetoothPrintDriver.IsNoConnection()) {
                        return;
                    }
                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);

              /*  if(mBeiKuan.isChecked()){
                                  BluetoothPrintDriver.SetFontEnlarge((byte)0x10);

                }
                if(mBeiGao.isChecked()){
                    BluetoothPrintDriver.SetFontEnlarge((byte)0x01);
                }
                if(mUnderline.isChecked()){
                    BluetoothPrintDriver.SetUnderline((byte)0x02);//ÏÂ»®Ïß
                }
                if(mBold.isChecked()){
                    BluetoothPrintDriver.SetBold((byte)0x01);//´ÖÌå
                }
                if(mMinifont.isChecked()){
                    BluetoothPrintDriver.SetCharacterFont((byte)0x01);
                }
                if(mHightlight.isChecked()){
                    BluetoothPrintDriver.SetBlackReversePrint((byte)0x01);
                }

               */
                    BluetoothPrintDriver.printString(text_to_print);
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        connect_device_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serverIntent = null;
                //showBottomSheetDialog();
            // Launch the DeviceListActivity to see devices and do scan
                serverIntent = new Intent(testActivty.this, DeviceListActivity.class);
               startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        });






        checkout_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isProcedtochecckoutClicked){
                   String transactiondate = getDatewithNameoftheDay();
                   String transactiontime = getDate_and_time();
                    double totalredeempointsusergetfromorder=0;
                    double redeempointsuserapplied=Double.parseDouble(redeemPoints_String);
                    double finalamountwithredeempointsint = Double.parseDouble(finaltoPayAmountwithRedeemPoints);
                    double totalpointredeembyuserint =  Double.parseDouble(totalpointsredeemedalreadybyuser);
                    double totalordervalue_tillnowint =  Double.parseDouble(totalordervalue_tillnow);
                    double totalredeempointsuserhaveint =  Double.parseDouble(totalredeempointsuserhave);

                    double finalamountwithredeempointsdouble = Double.parseDouble(finaltoPayAmountwithRedeemPoints);
                    double pointsfor100rs_double = Double.parseDouble(pointsfor100rs_String);

                    totalordervalue_tillnowint = totalordervalue_tillnowint+finalamountwithredeempointsint;
                    totalordervalue_tillnow = String.valueOf(totalordervalue_tillnowint);


                    totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*finalamountwithredeempointsdouble)/100);
                    totalredeempointsuserhaveint = totalredeempointsuserhaveint+totalredeempointsusergetfromorder;
                    totalredeempointsuserhave = String.valueOf(totalredeempointsuserhaveint);

                    totalpointredeembyuserint = totalpointredeembyuserint+redeempointsuserapplied;
                    totalpointsredeemedalreadybyuser=String.valueOf(totalpointredeembyuserint);


                    updateRedeemPointsDetailsInDB(redeemKey,totalpointsredeemedalreadybyuser,totalordervalue_tillnow,totalredeempointsuserhave);
                    addDatatoCouponTransactioninDB(redeemPoints_String,"REDEEM",mobileno_redeemKey,"9876543876543",transactiondate,transactiontime,vendorKey);
                    Toast.makeText(testActivty.this, "redeem points applied", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(testActivty.this, "redeem points not applied", Toast.LENGTH_SHORT).show();
                }
            }
        });







        redeemPoints_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalAmounttopay = Integer.parseInt("300");
                if (totalAmounttopay >= minordervalueforredeem_int) {
                    if ("9597580128".length() == 10) {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetRedeemPointsDetailsFortheUser+"?transactiondate=Sat, 12 Jun 2021&mobileno=919597580128", null,
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(@NonNull JSONObject response) {


                                        try {
                                            String jsonString =response.toString();
                                            Log.d(Constants.TAG, " response: redeem points Response " + response);
                                            JSONObject jsonObject = new JSONObject(jsonString);
                                            JSONArray JArray  = jsonObject.getJSONArray("content");
                                        //    Log.d(Constants.TAG, "redeem points Response Response: " + JArray);
                                            int i1=0;
                                            int arrayLength = JArray.length();
                                            Log.d("Constants.TAG", "redeem points Response Response: " + arrayLength);



                                            for(;i1<(arrayLength);i1++) {

                                                try {

                                                    Log.d("Constants.TAG", "redeem points Response: object 0 " + JArray.getJSONObject(0));
                                                   //  Log.d("Constants.TAG", "redeem points Response: array 0 " + JArray.getJSONArray(0));
                                                    JSONObject jsonObject1 = JArray.getJSONObject(i1);
                                                     redeemKey = String.valueOf(jsonObject1.get("key"));
                                                     mobileno_redeemKey = String.valueOf(jsonObject1.get("mobileno"));
                                                     discountAmountalreadyusedtoday = String.valueOf(jsonObject1.get("discountamountalreadyusedtoday"));
                                                     totalpointsredeemedalreadybyuser = String.valueOf(jsonObject1.get("pointsredeemed"));
                                                     totalordervalue_tillnow = String.valueOf(jsonObject1.get("totalordervalue"));
                                                     totalredeempointsuserhave = String.valueOf(jsonObject1.get("totalredeempoints"));

                                                    Log.d("Constants.TAG", "redeem points Response: jsonObject1 0 " + jsonObject1.get("key"));
                                                    isProcedtochecckoutClicked =false;
                                                    ispointsApplied_redeemClicked = false;
                                                    OpenRedeemDialogScreen();


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }

                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(@NonNull VolleyError error) {
                                Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                                Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                                Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                                Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

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
                        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);



                    } else {
                        AlertDialogClass.showDialog(testActivty.this, R.string.Enter_the_mobile_no_text);

                    }
                }
                else{
                    AlertDialogClass.showDialog(testActivty.this, Constants.Order_Value_should_be_above+" "+minordervalueforredeem_String+" rs",0);

                }
            }

        });



        callBackAsyncTaskListener_forPrintReceipt = new AsyncTaskListener_forPrintReceipt() {
            @Override
            public void onTaskCompleted(String result) {
                showProgressBar(false);
            }
        };


    }




    BroadcastReceiver myReceiverforPrintingService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Handle the broadcast message here




        }
    };




    private void fetchOrderDetailsfrompaymentOrderid() {



        ponits_redeemed_text_widgetnew.setText("");
        result_text_widgetnew.setText("");
        error_text_widgetnew.setText("");
        end_text_widgetnew.setText("");
        Log.d("Constants.TAG", "orderdetailsnew starting: " + getDate_and_time());
        ponits_redeemed_text_widgetnew.setText(" starting:   " + getDate_and_time());
        String starttimenew = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_forReport_AppOrders_and_PosOrders + "?slotdate=Sep 2021&vendorkey=vendor_3&previousdaydate=Aug 2021",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                end_text_widgetnew.setText("OrderTrackd ending:   " + getDate_and_time());
                String endtimenew = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widgetnew.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnew error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnew error: " + error);
                error_text_widgetnew.setText("OrderTrackdetailsmobileno error:   " + getDate_and_time()+"   -      "+ error.toString());
                String endtimenew = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);




    }

    private void fetchOrderDetailsfrompaymentOrderidwithIndex(String indexname) {
        //////dumpfirst
        ponits_redeemed_text_widget.setText("");
        result_text_widget.setText("");
        error_text_widget.setText("");
        end_text_widget.setText("");

        Log.d("Constants.TAG", "orderdetailsnewDump starting: " + getDate_and_time());
        ponits_redeemed_text_widget.setText("OrderdetailsorderidDump starting:   " + getDate_and_time());
        starttime = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/paymenttransactionwithorderidtest?orderid=1637987694012&indexname="+indexname,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                end_text_widget.setText("OrderdetailsorderidDump ending:   " + getDate_and_time());
                endtime = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widget.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + error);
                error_text_widget.setText("OrderdetailsorderidDump error:   " + getDate_and_time()+"   -      "+ error.toString());
                endtime = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);



    }

    private void fetchorderFromOrderTrackdetailsmobilenoold() {

        ponits_redeemed_text_widgetnew.setText("");
        result_text_widgetnew.setText("");
        error_text_widgetnew.setText("");
        end_text_widgetnew.setText("");
        Log.d("Constants.TAG", "orderdetailsnew starting: " + getDate_and_time());
        ponits_redeemed_text_widgetnew.setText("OrderTrackdetailsmobileno starting:   " + getDate_and_time());
        String starttimenew = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        String deliveryUserMobileNumberEncoded  = "";
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode("+91"+"7010779096", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/ordertrackingdetailsusigmobileno?mobileno="+deliveryUserMobileNumberEncoded,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                end_text_widgetnew.setText("OrderTrackdetailsmobileno ending:   " + getDate_and_time());
                String endtimenew = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);

                try{
                    JSONArray JArray = response.getJSONArray("content");
                    result_text_widget.setText(new StringBuilder().append(res).append("      ").append(JArray.length()).toString());

                }
                catch (Exception e){
                    result_text_widget.setText(res+"  err  ");

                    e.printStackTrace();
                }

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widgetnew.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnew error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnew error: " + error);
                error_text_widgetnew.setText("OrderTrackdetailsmobileno error:   " + getDate_and_time()+"   -      "+ error.toString());
                String endtimenew = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);





    }

    private void fetchorderFromOrderdetailsmobilenoold() {

        ponits_redeemed_text_widget.setText("");
        result_text_widget.setText("");
        error_text_widget.setText("");
        end_text_widget.setText("");

        Log.d("Constants.TAG", "orderdetailsnewDump starting: " + getDate_and_time());
        ponits_redeemed_text_widget.setText("Orderdetailsorderidold starting:   " + getDate_and_time());
        starttime = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        String deliveryUserMobileNumberEncoded  = "";
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode("+91"+"7010779096", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/orderdetailsusingmobileno?mobileno="+deliveryUserMobileNumberEncoded,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "Orderdetailsorderidold ending: " + getDate_and_time());
                end_text_widget.setText("Orderdetailsorderidold ending:   " + getDate_and_time());
                endtime = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);


                try{
                    JSONArray JArray = response.getJSONArray("content");
                    result_text_widget.setText(new StringBuilder().append(res).append("      ").append(JArray.length()).toString());

                }
                catch (Exception e){
                    result_text_widget.setText(res+"  err  ");

                    e.printStackTrace();
                }

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widget.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + error);
                error_text_widget.setText("Orderdetailsorderidold error:   " + getDate_and_time()+"   -      "+ error.toString());
                endtime = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);


    }

    private void fetchorderFromOrderdetailsorderidold() {
        ponits_redeemed_text_widget.setText("");
        result_text_widget.setText("");
        error_text_widget.setText("");
        end_text_widget.setText("");

        Log.d("Constants.TAG", "orderdetailsnewDump starting: " + getDate_and_time());
        ponits_redeemed_text_widget.setText("Orderdetailsorderidold starting:   " + getDate_and_time());
        starttime = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/orderdetails?orderid=1632378708492",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "Orderdetailsorderidold ending: " + getDate_and_time());
                end_text_widget.setText("Orderdetailsorderidold ending:   " + getDate_and_time());
                endtime = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);
                try{
                    JSONArray JArray = response.getJSONArray("content");
                    result_text_widget.setText(new StringBuilder().append(res).append(JArray.length()).toString());

                }
                catch (Exception e){
                    result_text_widget.setText(res+"  err  ");

                    e.printStackTrace();
                }

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widget.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + error);
                error_text_widget.setText("Orderdetailsorderidold error:   " + getDate_and_time()+"   -      "+ error.toString());
                endtime = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);


    }

    private void fetchorderFromOrderTrackdetailsmobilenoindex() {

        ponits_redeemed_text_widgetnew.setText("");
        result_text_widgetnew.setText("");
        error_text_widgetnew.setText("");
        end_text_widgetnew.setText("");
        Log.d("Constants.TAG", "orderdetailsnew starting: " + getDate_and_time());
        ponits_redeemed_text_widgetnew.setText("OrderTrackdetailsmobileno starting:   " + getDate_and_time());
        String starttimenew = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        String deliveryUserMobileNumberEncoded  = "";
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode("+91"+"7010779096", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/loaadtestdatacrt?mobileno="+deliveryUserMobileNumberEncoded+"&indexname=Ordertrackingdetails_mobileno_idx",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                end_text_widgetnew.setText("OrderTrackdetailsmobileno ending:   " + getDate_and_time());
                String endtimenew = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widgetnew.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnew error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnew error: " + error);
                error_text_widgetnew.setText("OrderTrackdetailsmobileno error:   " + getDate_and_time()+"   -      "+ error.toString());
                String endtimenew = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);





    }

    private void fetchorderFromOrderdetailsmobilenoindex() {
        //////dumpfirst
        ponits_redeemed_text_widget.setText("");
        result_text_widget.setText("");
        error_text_widget.setText("");
        end_text_widget.setText("");

        Log.d("Constants.TAG", "orderdetailsnewDump starting: " + getDate_and_time());
        ponits_redeemed_text_widget.setText("OrderdetailsmobilenoDump starting:   " + getDate_and_time());
        starttime = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        String deliveryUserMobileNumberEncoded  = "";
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode("+91"+"7010779096", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/loadtestdata?mobileno="+deliveryUserMobileNumberEncoded+"&indexname=Orderdetails_usermobile_idx",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                end_text_widget.setText("OrderdetailsmobilenoDump ending:   " + getDate_and_time());
                endtime = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widget.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + error);
                error_text_widget.setText("OrderdetailsmobilenoDump error:   " + getDate_and_time()+"   -      "+ error.toString());
                endtime = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);





    }


    private void fetchorderFromOrderdetailsorderidindex() {
        //////dumpfirst
        ponits_redeemed_text_widget.setText("");
        result_text_widget.setText("");
        error_text_widget.setText("");
        end_text_widget.setText("");

        Log.d("Constants.TAG", "orderdetailsnewDump starting: " + getDate_and_time());
        ponits_redeemed_text_widget.setText("OrderdetailsorderidDump starting:   " + getDate_and_time());
        starttime = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

//slotdate=Sat- 22 Jan 2022&vendorkey=vendor_3
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/vendororderdetails?slotdate=Sat- 22 Jan 2022&vendorkey=vendor_3",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                end_text_widget.setText("OrderdetailsorderidDump ending:   " + getDate_and_time());
                endtime = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widget.setText(response.toString());
                    }
                    else{

                    }
                } catch (JSONException e) {
                    error_text_widget.setText(response.toString());

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnewDump error: " + error);
                error_text_widget.setText("OrderdetailsorderidDump error:   " + getDate_and_time()+"   -      "+ error.toString());
                endtime = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttime,endtime);
                result_text_widget.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);



    }

    private void fetchOrderDetailsnewusingmobileno(String mobileno) {
        ponits_redeemed_text_widgetnew.setText("");
        result_text_widgetnew.setText("");
        error_text_widgetnew.setText("");
        Log.d("Constants.TAG", "orderdetailsnew starting: " + getDate_and_time());
        ponits_redeemed_text_widgetnew.setText("orderdetailsnew starting:   " + getDate_and_time());
        String starttimenew = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        String deliveryUserMobileNumberEncoded  = mobileno;
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode("+91"+mobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/getpaymenttransationfororderid
//https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/loadtestdata?mobileno=
//https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/paymenttransactionwithorderidtest?orderid=716398656404256&indexname="+mobileno
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://l5tqeb0rof.execute-api.ap-south-1.amazonaws.com/dev/get/getpaymenttransationfororderid?orderid=716398656404256",
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                end_text_widgetnew.setText("orderdetailsnewDump ending:   " + getDate_and_time());
               String endtimenew = getDate_and_time();
                String res =   checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        error_text_widgetnew.setText(response.getString("content"));
                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d("Constants.TAG", "orderdetailsnew error: " + getDate_and_time());
                Log.d("Constants.TAG", "orderdetailsnew error: " + error);
                error_text_widgetnew.setText("orderdetailsnewDump error:   " + getDate_and_time()+"   -      "+ error.toString());
               String endtimenew = getDate_and_time();

                String res =  checkDeliveredStatusOftheOrder(starttimenew,endtimenew);
                result_text_widgetnew.setText(res);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);



    }

    private void fetchOrderDetailsnewDumpusingmobileno(String mobileno) {
        ponits_redeemed_text_widget.setText("");
        result_text_widget.setText("");
        error_text_widget.setText("");
        Log.d("Constants.TAG", "orderdetailsnewDump starting: " + getDate_and_time());
        ponits_redeemed_text_widget.setText("orderdetailsnewDump starting:   " + getDate_and_time());
        starttime = getDate_and_time();
        JSONObject jsonObject = new JSONObject();
                //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        String deliveryUserMobileNumberEncoded  = mobileno;
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode("+91"+mobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/paymenttransactionwithorderidtest?orderid=716398656404256&indexname="+mobileno,

               // JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://5i8mj6bo87.execute-api.ap-south-1.amazonaws.com/stage/resource?mobileno="+deliveryUserMobileNumberEncoded,
                        jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d("Constants.TAG", "orderdetailsnewDump ending: " + getDate_and_time());
                        end_text_widget.setText("orderdetailsnewDump ending:   " + getDate_and_time());
                        endtime = getDate_and_time();
                       String res =   checkDeliveredStatusOftheOrder(starttime,endtime);
                        result_text_widget.setText(res);
                        try {
                            String message = response.getString("message");
                            if (message.equals("success")) {
                                error_text_widget.setText(response.getString("content"));
                            }
                            else{

                            }
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {
                        Log.d("Constants.TAG", "orderdetailsnewDump error: " + getDate_and_time());
                        Log.d("Constants.TAG", "orderdetailsnewDump error: " + error);
                        error_text_widget.setText("orderdetailsnewDump error:   " + getDate_and_time()+"   -      "+ error.toString());
                        endtime = getDate_and_time();

                        String res =  checkDeliveredStatusOftheOrder(starttime,endtime);
                        result_text_widget.setText(res);

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
                // Make the request
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);







    }
    private String checkDeliveredStatusOftheOrder(String orderdeliveredtime, String slottime) {
        String result = "";
        try {


            String endtm = slottime.toString();
            //       System.out.println("slottime before parse DATE"+endtm);

            String sttime = orderdeliveredtime.toString();
            //        System.out.println("orderdeliveredtime before parse DATE"+sttime);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date1 = simpleDateFormat.parse(sttime);
            Date date2 = simpleDateFormat.parse(endtm);

            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60 * 24));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

            //     Log.i("=== Hours "," :: "+hours+":");
            //     Log.i("=== Hours days "," :: "+hours+":"+days);

            hours = (hours < 0 ? -hours : hours);
            //    Log.i("=== Hours  "," :: "+hours+":"+min);
            result = String.valueOf(difference);


        } catch (Exception j) {
            j.printStackTrace();
            try {


                String endtm = slottime.toString();
                //         System.out.println("slottime before parse DATE"+endtm);

                String sttime = orderdeliveredtime.toString();
                //        System.out.println("orderdeliveredtime before parse DATE"+sttime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

                Date date1 = sdf.parse(sttime);
                Date date2 = simpleDateFormat.parse(endtm);

                long difference = date2.getTime() - date1.getTime();
                int days = (int) (difference / (1000 * 60 * 60 * 24));
                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60 * 24));
                int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                ////        Log.i("=== Hours "," :: "+hours+":");
                //       Log.i("=== Hours days "," :: "+hours+":"+days);

                hours = (hours < 0 ? -hours : hours);
                //         Log.i("=== Hours  "," :: "+hours+":"+min);
                result = String.valueOf(difference);

            } catch (Exception jx) {
                jx.printStackTrace();

            }
        }
        return result;
/*
        long orderplacedtimelong =Long.parseLong(orderdeliveredtime_in_long);
        long slottimelong = Long.parseLong(slottime_in_long);
        long millis  = slottimelong-orderplacedtimelong;
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = millis / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) :
                String.valueOf(hours));
        b.append(":");
        b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) :
                String.valueOf(minutes));
        b.append(":");
        b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                String.valueOf(seconds));

        Log.d(TAG, "slottime b orderdeliveredtime_in_long  "+orderdeliveredtime_in_long);
        Log.d(TAG, "slottime b orderdeliveredtime "+orderdeliveredtime);

        Log.d(TAG, "slottime b slottime_in_long  "+slottime_in_long);
        Log.d(TAG, "slottime b slottime_  "+slottime);
        Log.d(TAG, "slottime b  "+b);
        return  result;

 */
    }



    private void getNotesData() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://3xs79s0rob.execute-api.us-west-1.amazonaws.com/dev/notes/get?mobileno=%2B919597580128",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);


                        try {
                            String orderid = "", deliverytype = "";
                            double discount_double = 0, discountfromHashmap_double = 0;
                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (int i1 = 0; i1 < JArray.length(); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    try{
                                        String notes = json.getString("notes");
                                       // ponits_redeemed_text_widget.setText(notes);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        })
        {




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
        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  if(D) Log.d(TAG, "onActivityResult " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    try {
                        String address = data.getExtras()
                                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        // Get the BLuetoothDevice object
                        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                        // Attempt to connect to the device
                        mChatService.connect(device);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d("TAG", "BT not enabled");
                    Toast.makeText(this, "bt_not_enabled_leaving", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }





    private void setupChat() {
        Log.d("TAG", "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver(this, mHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        if (mChatService != null) mChatService.stop();
       // if(D) Log.e(TAG, "--- ON DESTROY ---");
    }


    public void DisplayToast(String str)
    {
        Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        //ÉèÖÃtoastÏÔÊ¾µÄÎ»ÖÃ
        toast.setGravity(Gravity.TOP, 0, 100);
        //ÏÔÊ¾¸ÃToast
        toast.show();
    }//DisplayToast


    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:
                            ponits_redeemed_text_widget.setText(R.string.title_connected_to);
                            ponits_redeemed_text_widget.append(mConnectedDeviceName);
                            setTitle(R.string.title_connected_to);
                            setTitle(mConnectedDeviceName);
                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:
                            ponits_redeemed_text_widget.setText(R.string.title_connecting);
                            setTitle(R.string.title_connecting);
                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                            ponits_redeemed_text_widget.setText("state listen");

                        case BluetoothPrintDriver.STATE_NONE:
                            ponits_redeemed_text_widget.setText(R.string.title_not_connected);
                            setTitle(R.string.title_not_connected);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String ErrorMsg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    float Voltage = 0;
                  //  if(D) Log.i(TAG, "readBuf[0]:"+readBuf[0]+"  readBuf[1]:"+readBuf[1]+"  readBuf[2]:"+readBuf[2]);
                    if(readBuf[2]==0)
                        ErrorMsg = "NO ERROR!         ";
                    else
                    {
                        if((readBuf[2] & 0x02) != 0)
                            ErrorMsg = "ERROR: No printer connected!";
                        if((readBuf[2] & 0x04) != 0)
                            ErrorMsg = "ERROR: No paper!  ";
                        if((readBuf[2] & 0x08) != 0)
                            ErrorMsg = "ERROR: Voltage is too low!  ";
                        if((readBuf[2] & 0x40) != 0)
                            ErrorMsg = "ERROR: Printer Over Heat!  ";
                    }
                    Voltage = (float) ((readBuf[0]*256 + readBuf[1])/10.0);
                    //if(D) Log.i(TAG, "Voltage: "+Voltage);
                   DisplayToast(ErrorMsg+"                                        "+"Battery voltage£º"+Voltage+" V");
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };


/*
    private void showBottomSheetDialog() {

        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.mobilescreen_assigndeliverypartner_bottom_sheet_dialog);

        ListView ListView1 = bottomSheetDialog.findViewById(R.id.listview);

        Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(mContext, mobile_manageOrders1.deliveryPartnerList,orderkey,"AppOrdersList", deliverypartnerName);

        ListView1.setAdapter(adapter_mobile_assignDeliveryPartner1);

        bottomSheetDialog.show();
    }

 */

    private void updateRedeemPointsDetailsInDB(String redeemPointsKey, String totalpointsredeemedbyuser, String totalordervalue, String totalredeempoints) {




        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", redeemPointsKey);

            jsonObject.put("pointsredeemed", totalpointsredeemedbyuser);
            jsonObject.put("totalordervalue", totalordervalue);
            jsonObject.put("totalredeempoints", totalredeempoints);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateRedeemPointsTablewithkey,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        Log.d(Constants.TAG, "Points Redeem details uploaded Succesfully " + response);
                    }
                    else{
                        Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + response);

                    }
                } catch (JSONException e) {
                    Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + response);

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + error.toString());

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);







    }

    private void addDatatoCouponTransactioninDB(String coupondiscountamount, String coupontype, String mobileno, String orderid, String transactiondate, String transactiontime, String vendorkey) {



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);

            jsonObject.put("coupondiscountamount", Double.parseDouble(coupondiscountamount));
            jsonObject.put("coupontype", coupontype);
            jsonObject.put("mobileno", mobileno);
            jsonObject.put("transactiondate", transactiondate);
            jsonObject.put("transactiontime", transactiontime);
            jsonObject.put("vendorkey", vendorkey);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addCouponDetailsInCouponTranactionsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);

                        //   printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);

                    }
                } catch (JSONException e) {
                    //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + error);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(testActivty.this).add(jsonObjectRequest);





    }


    private void OpenRedeemDialogScreen() {
        pointsalreadyredeemedInteger=0;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Dialog dialog = new Dialog(testActivty.this);
                    dialog.setContentView(R.layout.redeem_pointswise_discount_layout);
                    dialog.setCanceledOnTouchOutside(true);

                    Currenttime = getDate_and_time();
                    //Log.d(TAG, "Currenttime: " + Currenttime);
                    TextView usermobileno_textwidget = (TextView) dialog.findViewById(R.id.usermobileno_textwidget);
                    TextView totalNoofPoints_textwidget = (TextView) dialog.findViewById(R.id.totalNoofPoints_textwidget);
                    TextView points_you_can_redeemToday_textwidget = (TextView) dialog.findViewById(R.id.points_you_can_redeemToday_textwidget);
                    EditText points_to_redeem_edittext = (EditText) dialog.findViewById(R.id.points_to_redeem_edittext);
                    Button applyRedeemPoints = (Button) dialog.findViewById(R.id.applyRedeemPoints);
                    TextView totalbillAmount_textwidget = (TextView) dialog.findViewById(R.id.totalbillAmount_textwidget);
                    TextView redeemedpoints_textwidget = (TextView) dialog.findViewById(R.id.redeemedpoints_textwidget);
                    TextView finalAmounttopay_textwidget = (TextView) dialog.findViewById(R.id.finalAmounttopay_textwidget);
                    TextView points_user_already_redeemed = (TextView) dialog.findViewById(R.id.points_user_already_redeemed);

                    Button proceedtoCheckoutWidget = (Button) dialog.findViewById(R.id.proceedtoCheckoutWidget);
                    TextView total_noof_points_allowedPerDay_textWidget = (TextView) dialog.findViewById(R.id.total_noof_points_allowedPerDay_textWidget);

                    //         String no_of_points_user_have = getPointsDetilfr omDB();
                    long sTime = System.currentTimeMillis();
                     pointsalreadyredeemedInteger = Integer.parseInt(totalpointsredeemedalreadybyuser);

                    int pointsredeemedtodayInteger = Integer.parseInt(discountAmountalreadyusedtoday);
                    int pointsallowedtouseToday = maxpointsinaday_int-pointsredeemedtodayInteger;
                    usermobileno_textwidget.setText(mobileno_redeemKey);
                    totalNoofPoints_textwidget.setText(totalredeempointsuserhave);
                    totalbillAmount_textwidget.setText(totalbillAmount);
                    finalAmounttopay_textwidget.setText(totalbillAmount);
                    points_user_already_redeemed.setText(totalpointsredeemedalreadybyuser);
                    redeemedpoints_textwidget.setText("0  Points");

                    total_noof_points_allowedPerDay_textWidget.setText(String.format("( %s Points allowed / day ) ", maxpointsinaday_String));
                    points_you_can_redeemToday_textwidget.setText(String.valueOf(pointsallowedtouseToday));
                    applyRedeemPoints.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int totalpointsuseralreadyhave = Integer.parseInt(totalredeempointsuserhave);
                             pointsenteredToredeem = points_to_redeem_edittext.getText().toString();
                            int pointsenteredToredeem_int = Integer.parseInt(pointsenteredToredeem);
                            if (totalpointsuseralreadyhave >= pointsenteredToredeem_int) {
                                pointsalreadyredeemedInteger = pointsalreadyredeemedInteger + pointsenteredToredeem_int;
                                totalpointsuserhave_afterapplypoints =totalpointsuseralreadyhave-pointsenteredToredeem_int;
                                int totalbillAmount_int = Integer.parseInt(totalbillAmount);
                                int finalamounttoPay = totalbillAmount_int - pointsenteredToredeem_int;
                                if (pointsenteredToredeem_int <= pointsallowedtouseToday) {
                                    ispointsApplied_redeemClicked = true;
                                    redeemedpoints_textwidget.setText(pointsenteredToredeem + "  Points");

                                    finalAmounttopay_textwidget.setText(String.valueOf(finalamounttoPay));
                                    //redeemPointsKey_fromRedeem = redeemKey;


                                } else {
                                    AlertDialogClass.showDialog(testActivty.this, Constants.PointusercanRedeemtoday , 0);

                                }
                            }
                            else {
                                AlertDialogClass.showDialog(testActivty.this, "User got  " + String.valueOf(totalredeempointsuserhave) + "  Points only", 0);
                            }
                        }
                    });


                    proceedtoCheckoutWidget.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(ispointsApplied_redeemClicked) {
                                isProcedtochecckoutClicked = true;
                                finaltoPayAmountwithRedeemPoints = finalAmounttopay_textwidget.getText().toString();
                                redeemPoints_String =  pointsenteredToredeem ;
                              //  ponits_redeemed_text_widget.setText(redeemPoints_String);
                                totalpointsredeemedalreadybyuser=String.valueOf(pointsalreadyredeemedInteger);
                                totalredeempointsuserhave = String.valueOf(totalpointsuserhave_afterapplypoints);
                                dialog.cancel();

                            }
                            else{
                                AlertDialogClass.showDialog(testActivty.this, Constants.FirstApplyRedeemPoints_Instruction,0);

                            }
                        }
                    });




                    dialog.show();
                    showProgressBar(false);

                } catch (WindowManager.BadTokenException e) {
                    showProgressBar(false);

                    e.printStackTrace();
                }
            }
        });


    }



    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);


        }

    }

    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);



        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;


        //CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDatee = df.format(c);
        CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss:SSS");
        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }





}