package com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobileScreen_AssignDeliveryPartner1 extends AppCompatActivity {
    ListView deliveryPartners_list_widget;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    String vendorKey = "vendor_1", orderKey,IntentFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_screen__assign_delivery_partner1);
        deliveryPartners_list_widget = findViewById(R.id.deliveryPartners_list_widget);
        deliveryPartnerList = new ArrayList<>();
        orderKey = getIntent().getStringExtra("TrackingTableKey");
        IntentFrom = getIntent().getStringExtra("IntentFrom");
        addemptydetails(orderKey);
        getDeliveryPartnerList();
    }

    private void addemptydetails(String orderKey) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", orderKey);
            jsonObject.put("deliveryuserkey", "Not Assigned");
            jsonObject.put("deliveryusermobileno", "Not Assigned");
            jsonObject.put("deliveryusername", "Not Assigned");

            Log.i("tag","listenertoken"+ "");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updateTrackingOrderTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try {
                    String msg = String.valueOf(response.get("message"));
                    Log.d(Constants.TAG, "Response: " + msg);
                    if(msg.equals("success")){
                        Log.d(Constants.TAG, "Success: " );

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(MobileScreen_AssignDeliveryPartner1.this).add(jsonObjectRequest);

    }

    private void getDeliveryPartnerList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryPartnerList+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (; i1 < (arrayLength); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass = new AssignDeliveryPartner_PojoClass();
                                    assignDeliveryPartner_pojoClass.deliveryPartnerStatus = String.valueOf(json.get("status"));
                                    assignDeliveryPartner_pojoClass.deliveryPartnerKey = String.valueOf(json.get("key"));
                                    assignDeliveryPartner_pojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("mobileno"));
                                    assignDeliveryPartner_pojoClass.deliveryPartnerName = String.valueOf(json.get("name"));

                                    // Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                                    deliveryPartnerList.add(assignDeliveryPartner_pojoClass);

                                  //  Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(MobileScreen_AssignDeliveryPartner1.this, deliveryPartnerList, orderKey,IntentFrom, deliverypartnerName);

                                    //deliveryPartners_list_widget.setAdapter(adapter_mobile_assignDeliveryPartner1);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_1");
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
        Volley.newRequestQueue(MobileScreen_AssignDeliveryPartner1.this).add(jsonObjectRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, MobileScreen_OrderDetails1.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}