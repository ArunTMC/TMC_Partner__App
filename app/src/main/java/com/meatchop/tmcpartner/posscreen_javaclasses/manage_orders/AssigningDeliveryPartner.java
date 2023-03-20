package com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssigningDeliveryPartner extends AppCompatActivity {
ListView deliveryPartners_list_widget;
List<AssignDeliveryPartner_PojoClass>deliveryPartnerList;
String vendorKey,orderKey,vendorname,orderid,customerMobileNo,fromActivityName ="";
private LinearLayout loadingPanel_dailyItemWisereport,loadingpanelmask_dailyItemWisereport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assigning_delivery_partner_activity1);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        loadingPanel_dailyItemWisereport = findViewById(R.id.loadingPanel_dailyItemWisereport);
        loadingpanelmask_dailyItemWisereport = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
        loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);

        deliveryPartners_list_widget=findViewById(R.id.deliveryPartners_list_widget);
        deliveryPartnerList = new ArrayList<>();
        SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", ""));
        vendorname = (shared.getString("VendorName", ""));
        orderKey = getIntent().getStringExtra("TrackingTableKey");
        orderid = getIntent().getStringExtra("orderid");
        customerMobileNo = getIntent().getStringExtra("customerMobileNo");
        fromActivityName = getIntent().getStringExtra("From");
     //   Toast.makeText(AssigningDeliveryPartner.this, "3  -" +fromActivityName, Toast.LENGTH_SHORT).show();

        if(vendorKey.equals("")){
            vendorKey = getIntent().getStringExtra("vendorkey");

        }
        getDeliveryPartnerList();
    }

    private void getDeliveryPartnerList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryPartnerList+vendorKey  , null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //converting jsonSTRING into array
                            JSONArray JArray  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1=0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for(;i1<(arrayLength);i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass = new AssignDeliveryPartner_PojoClass();
                                    assignDeliveryPartner_pojoClass.deliveryPartnerStatus =String.valueOf(json.get("status"));
                                    assignDeliveryPartner_pojoClass.deliveryPartnerKey =String.valueOf(json.get("key"));
                                    assignDeliveryPartner_pojoClass.deliveryPartnerMobileNo =String.valueOf(json.get("mobileno"));
                                    assignDeliveryPartner_pojoClass.deliveryPartnerName =String.valueOf(json.get("name"));

                                   // //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                                    deliveryPartnerList.add(assignDeliveryPartner_pojoClass);
                                    //Toast.makeText(AssigningDeliveryPartner.this, "4  -" +fromActivityName, Toast.LENGTH_SHORT).show();

                                    Adapter_AssignDeliveryPartner adapter_assignDeliveryPartner= new Adapter_AssignDeliveryPartner(AssigningDeliveryPartner.this,deliveryPartnerList,orderKey,vendorKey,customerMobileNo,orderid,fromActivityName);

                                    deliveryPartners_list_widget.setAdapter(adapter_assignDeliveryPartner);
                                    loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                    loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);




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




                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey",  vendorKey);
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
        Volley.newRequestQueue(AssigningDeliveryPartner.this).add(jsonObjectRequest);
    }


}