package com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class TrackingOrderDetails_ServiceClass extends Service {


    public static String str_receiver = "com.example.tmcpartnerapp.PosScreen_JavaClasses.ManageOrders.Pos_ManageOrderFragment";
    Intent intent;
    public static final int notify = 600000;
    private Handler mHandler = new Handler(Looper.getMainLooper());   //run on another Thread to avoid crash
    private Timer mTimer = null;    //timer handling
    List<Modal_ManageOrders_Pojo_Class> orderDetailsList;
    List<Modal_ManageOrders_Pojo_Class> orderTrackingList;
    ArrayList<String> finalOrderList;
    String TodaysDate,ordertype = "EXPRESSDELIVERY";
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast
                    orderDetailsList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
                    orderTrackingList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
                    finalOrderList = new ArrayList<>();
                     TodaysDate=getDate();
                 //   getDataFromOrderDetailsTableforDate("12/26/2020");
                    Toast.makeText(TrackingOrderDetails_ServiceClass.this, "Service is running", Toast.LENGTH_SHORT).show();

                    getDataFromTrackingOrderDetailsTableforDate(TodaysDate);


                }
            });
        }
    }

    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);


        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
         String CurrentDate = df.format(c);

        return CurrentDate;
    }

    private void getDataFromTrackingOrderDetailsTableforDate(String date) {
        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        String vendorKey = (shared.getString("VendorKey", ""));

        JSONObject  jsonObject1 = new JSONObject();
        try {
            jsonObject1.put("vendorkey", vendorKey);
            jsonObject1.put("orderplacedtime", date);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey+"?orderplaceddate="+TodaysDate+"&vendorkey=vendor_1" ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);
                        SendarraytoManageOrdersFragment(response);

          /*
          try {
                            JSONArray JArray  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "Response: " + JArray);

                            int i1=0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    //                                    finalOrderList.add((String) JArray.get(i1));
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClasss = new Modal_ManageOrders_Pojo_Class();
                                    manageOrdersPojoClasss.orderid =String.valueOf(json.get("orderid"));
                                    manageOrdersPojoClasss.orderstatus =String.valueOf(json.get("orderstatus"));
                                    manageOrdersPojoClasss.keyfromtrackingOrderTable =String.valueOf(json.get("keyfromtrackingDetails"));
                                    //Log.d(Constants.TAG, "String.valueOf(json.get(\"orderid\"): " + String.valueOf(json.get("orderid")));

                                    orderTrackingList.add(manageOrdersPojoClasss);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }
                                }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

           */
                        // pos_spinner_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listdata);
                        // pos_vendor_selecting_spinner.setAdapter(pos_spinner_aAdapter);
                     //   combiningBothArray(orderDetailsList,orderTrackingList);
                      //  SendarraytoManageOrdersFragment(finalOrderList);
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
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", vendorKey);
                params.put("orderplacedtime",TodaysDate );

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

/*
    private void getDataFromOrderDetailsTableforDate(String date) {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("orderplacedtime", date);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsforDate+"?orderplacedtime=10:36:19 AM",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        ArrayList<String> listdata = new ArrayList<String>();
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray JArray  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "Response: " + JArray);
                            int i1=0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);

                                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                                    manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                                    manageOrdersPojoClass.orderid =String.valueOf(json.get("orderid"));

                                    manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));
                                    manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));
                                    manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));
                                    manageOrdersPojoClass.taxamount = String.valueOf(json.get("taxamount"));
                                    manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobile"));
                                    manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));
                                    manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscamount"));
                                    manageOrdersPojoClass.itemdesp = String.valueOf(json.get("itemdesp"));
                                    //Log.d(Constants.TAG, "String.valueOf(json.get(\"orderid\")2: " + String.valueOf(json.get("orderid")));

                                    orderDetailsList.add(manageOrdersPojoClass);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // pos_spinner_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listdata);
                       // pos_vendor_selecting_spinner.setAdapter(pos_spinner_aAdapter);
                      //  combiningBothArray(orderDetailsList,orderTrackingList);

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

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void combiningBothArray(List<Modal_ManageOrders_Pojo_Class> orderDetailsList, List<Modal_ManageOrders_Pojo_Class> orderTrackingList) {
   if(orderDetailsList.size()>0&&orderTrackingList.size()>0){
       for (int i = 0; i < orderTrackingList.size(); i++) {
           final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = orderTrackingList.get(i);
           String orderid_from_websocket_ordersList =modal_manageOrders_pojo_class.getOrderid();
           //Log.d(Constants.TAG, "orderid_from_websocket_ordersList " + orderid_from_websocket_ordersList);

           for(int j =0;j<orderDetailsList.size();j++){
               final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList =orderDetailsList.get(j);
               String orderid_from_ordersList =modal_manageOrders_forOrderDetailList.getOrderid();
               //Log.d(Constants.TAG, "orderid_from_ordersList " + orderid_from_ordersList);

               if(orderid_from_websocket_ordersList.equals(orderid_from_ordersList))
               {
                   final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 =orderDetailsList.get(j);

                   modal_manageOrders_forOrderDetailList1.orderid =modal_manageOrders_forOrderDetailList.getOrderid();
                   modal_manageOrders_forOrderDetailList1.orderplacedtime =modal_manageOrders_forOrderDetailList.getOrderplacedtime();
                   modal_manageOrders_forOrderDetailList1.payableamount =modal_manageOrders_forOrderDetailList.getPayableamount();
                   modal_manageOrders_forOrderDetailList1.paymentmode =modal_manageOrders_forOrderDetailList.getPaymentmode();
                   modal_manageOrders_forOrderDetailList1.tokenno =modal_manageOrders_forOrderDetailList.getTokenno();
                   modal_manageOrders_forOrderDetailList1.taxamount =modal_manageOrders_forOrderDetailList.getTaxamount();
                   modal_manageOrders_forOrderDetailList1.usermobile =modal_manageOrders_forOrderDetailList.getUsermobile();
                   modal_manageOrders_forOrderDetailList1.vendorkey =modal_manageOrders_forOrderDetailList.getVendorkey();
                   modal_manageOrders_forOrderDetailList1.coupondiscamount =modal_manageOrders_forOrderDetailList.getCoupondiscamount();
                   modal_manageOrders_forOrderDetailList1.itemdesp =modal_manageOrders_forOrderDetailList.getItemdesp();

                   //
                   modal_manageOrders_forOrderDetailList1.orderstatus =modal_manageOrders_pojo_class.getOrderstatus();
                   modal_manageOrders_forOrderDetailList1.keyfromtrackingOrderTable =modal_manageOrders_pojo_class.getKeyfromtrackingOrderTable();

                //   finalOrderList.add(modal_manageOrders_forOrderDetailList1);

               }
               else{
                   //Log.i(Constants.TAG,orderid_from_ordersList+" this order is not repeated ");
               }

           }




       }
     // SendarraytoManageOrdersFragment(finalOrderList);
   }

    }


 */
    private void SendarraytoManageOrdersFragment(JSONObject response) {
      /*  //Bundle bundle=new Bundle();
      //  bundle.putSerializable("test", (Serializable) finalOrderList);
        //intent.putExtras(bundle);
        //Log.i(Constants.TAG,"SendarraytoManageOrdersFragment ");
      //  intent.putExtra("finalOrderList", (Serializable) finalOrderList);
            intent.putExtra("finalOrderList",finalOrderList);
       // intent.putParcelableArrayListExtra("finalOrderList", (ArrayList<? extends Parcelable>) finalOrderList);
        sendBroadcast(intent);

       */

        Intent intent= new Intent("YOUR");
        intent.putExtra("response",response.toString());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


    }
}