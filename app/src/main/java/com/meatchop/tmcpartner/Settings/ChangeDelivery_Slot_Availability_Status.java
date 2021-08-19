package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlotDetails;
import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlots;
import static com.meatchop.tmcpartner.Constants.api_Update_DeliverySlotDetails;
import static com.meatchop.tmcpartner.Constants.api_Update_DeliverySlots;

public class ChangeDelivery_Slot_Availability_Status extends AppCompatActivity {
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch expressDeliveryAvailabiltySwitch;
    String vendorkey,deliverySlotKey ,deliveryslotkeyfromSlotdetails;
    List<Modal_DeliverySlots>TodaysPreOrdersSlotList;
    List<Modal_DeliverySlots>TomorrowsPreOrdersSlotList;
    ListView todaysDeliverySlotListview,tomorrowsDeliverySlotListview;
    double screenInches;
    boolean isActiveinDeliverySlotDetails=false;
    boolean isActiveinDeliverySlots = false;
    LinearLayout loadingPanel,loadingpanelmask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_delivery__slot__availability__status);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        expressDeliveryAvailabiltySwitch =findViewById(R.id.vendorSlotAvailabiltySwitch);
        todaysDeliverySlotListview = findViewById(R.id.todaysDeliverySlotList);
        tomorrowsDeliverySlotListview = findViewById(R.id.tomorrowsDeliverySlotList);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));
        TodaysPreOrdersSlotList = new ArrayList<>();
        TomorrowsPreOrdersSlotList = new ArrayList<>();
        checkforDeliverySlotDetails();




        expressDeliveryAvailabiltySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    changeStatusintheDeliverySlotDetails("ACTIVE");
                    changeStatusintheDeliverySlot(deliverySlotKey,"ACTIVE");
                    //   changeStatusintheMobiledataDeliverySlot("");
                } else {
                    changeStatusintheDeliverySlotDetails("INACTIVE");
                    changeStatusintheDeliverySlot(deliverySlotKey,"INACTIVE");
                    // changeStatusintheMobiledataDeliverySlot("");


                }
            }
        });










    }

    void changeStatusintheDeliverySlot(String deliverySlotKeyy, String status) {
        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key",deliverySlotKeyy);
            jsonObject.put("status", status);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_DeliverySlots,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        showProgressBar(false);
                    }


                } catch (JSONException e) {
                    showProgressBar(false);
                    Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"Failed to change express delivery slot status in Delivery slots",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeDelivery_Slot_Availability_Status.this).add(jsonObjectRequest);





    }

    private void checkforDeliverySlots() {
        showProgressBar(true);
        TomorrowsPreOrdersSlotList.clear();
        TodaysPreOrdersSlotList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlots+"?storeid="+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    if (content != null) {
                        for (int i = 0; i < content.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);
                                String slotName = String.valueOf(json.get("slotname"));
                                String slotDateType = String.valueOf(json.get("slotdatetype"));
                                slotDateType =slotDateType.toUpperCase();
                                slotName = slotName.toUpperCase();
                                if(slotName.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)){
                                    deliverySlotKey= String.valueOf(json.get("key"));
                                    String status =String.valueOf(json.get("status"));
                                    status = status.toUpperCase();

                                    if(status.equals("ACTIVE")){
                                        isActiveinDeliverySlots=true;

                                    }
                                    if(status.equals("INACTIVE")){
                                        isActiveinDeliverySlots=false;

                                    }


                                    if(isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                                        expressDeliveryAvailabiltySwitch.setChecked(true);
                                    }
                                    else if(!isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slot details",Toast.LENGTH_LONG).show();
                                    }
                                    else if(!isActiveinDeliverySlots&&isActiveinDeliverySlotDetails){
                                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slots ",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        expressDeliveryAvailabiltySwitch.setChecked(false);

                                    }










                                }
                                try {
                                    if (slotName.equals(Constants.PREORDER_SLOTNAME) && (slotDateType.equals(Constants.TODAYPREORDER_SLOTNAME))) {
                                        Modal_DeliverySlots modal_deliverySlots = new Modal_DeliverySlots();
                                        modal_deliverySlots.key = String.valueOf(json.get("key"));
                                        modal_deliverySlots.status = String.valueOf(json.get("status"));
                                        modal_deliverySlots.slotdatetype = String.valueOf(json.get("slotdatetype"));
                                        modal_deliverySlots.slotstarttime = String.valueOf(json.get("slotstarttime"));
                                        modal_deliverySlots.slotendtime = String.valueOf(json.get("slotendtime"));
                                        try {
                                            TodaysPreOrdersSlotList.add(modal_deliverySlots);
                                            Collections.sort(TodaysPreOrdersSlotList, new Comparator<Modal_DeliverySlots>() {
                                                public int compare(final Modal_DeliverySlots object1, final Modal_DeliverySlots object2) {
                                                    return object2.getSlotstarttime().compareTo(object1.getSlotstarttime());
                                                }
                                            });
                                        } catch (Exception e) {
                                            TodaysPreOrdersSlotList.add(modal_deliverySlots);

                                            e.printStackTrace();
                                        }


                                        Adapter_TodaysDeliverySlots adapter_todaysDeliverySlots = new Adapter_TodaysDeliverySlots(ChangeDelivery_Slot_Availability_Status.this, TodaysPreOrdersSlotList, ChangeDelivery_Slot_Availability_Status.this);
                                        todaysDeliverySlotListview.setAdapter(adapter_todaysDeliverySlots);
                                        Helper.getListViewSize(todaysDeliverySlotListview, screenInches);


                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    showProgressBar(false);
                                }
                                try {


                                    if (slotName.equals(Constants.PREORDER_SLOTNAME) && (slotDateType.equals(Constants.TOMORROWPREORDER_SLOTNAME))) {
                                        Modal_DeliverySlots modal_deliverySlots = new Modal_DeliverySlots();
                                        modal_deliverySlots.key = String.valueOf(json.get("key"));
                                        modal_deliverySlots.status = String.valueOf(json.get("status"));
                                        modal_deliverySlots.slotdatetype = String.valueOf(json.get("slotdatetype"));
                                        modal_deliverySlots.slotstarttime = String.valueOf(json.get("slotstarttime"));
                                        modal_deliverySlots.slotendtime = String.valueOf(json.get("slotendtime"));

                                        TomorrowsPreOrdersSlotList.add(modal_deliverySlots);

                                        try {
                                            Collections.sort(TomorrowsPreOrdersSlotList, new Comparator<Modal_DeliverySlots>() {
                                                public int compare(final Modal_DeliverySlots object1, final Modal_DeliverySlots object2) {
                                                    return object2.getSlotstarttime().compareTo(object1.getSlotstarttime());
                                                }
                                            });
                                        } catch (Exception e) {
                                            TomorrowsPreOrdersSlotList.add(modal_deliverySlots);

                                            e.printStackTrace();
                                        }
                                        Adapter_TomorrowsDeliverySlots adapter_tomorrowsDeliverySlots = new Adapter_TomorrowsDeliverySlots(ChangeDelivery_Slot_Availability_Status.this, TomorrowsPreOrdersSlotList, ChangeDelivery_Slot_Availability_Status.this);
                                        tomorrowsDeliverySlotListview.setAdapter(adapter_tomorrowsDeliverySlots);
                                        Helper.getListViewSize(tomorrowsDeliverySlotListview, screenInches);
                                        showProgressBar(false);

                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    showProgressBar(false);
                                }



                            } catch (JSONException e) {
                                TomorrowsPreOrdersSlotList.clear();
                                TodaysPreOrdersSlotList.clear();
                                showProgressBar(false);

                                e.printStackTrace();
                            }
                        }



                    }
                } catch (JSONException e) {
                    if(isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                        expressDeliveryAvailabiltySwitch.setChecked(true);
                    }
                    else if(!isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slot details",Toast.LENGTH_LONG).show();
                    }
                    else if(!isActiveinDeliverySlots&&isActiveinDeliverySlotDetails){
                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slots ",Toast.LENGTH_LONG).show();
                    }
                    else{
                        expressDeliveryAvailabiltySwitch.setChecked(false);

                    }


                    showProgressBar(false);



                    TomorrowsPreOrdersSlotList.clear();
                    TodaysPreOrdersSlotList.clear();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                TomorrowsPreOrdersSlotList.clear();
                TodaysPreOrdersSlotList.clear();
                error.printStackTrace();
                showProgressBar(false);

                if(isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                    expressDeliveryAvailabiltySwitch.setChecked(true);
                }
                else if(!isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                    Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slot details",Toast.LENGTH_LONG).show();
                }
                else if(!isActiveinDeliverySlots&&isActiveinDeliverySlotDetails){
                    Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slots ",Toast.LENGTH_LONG).show();
                }
                else{
                    expressDeliveryAvailabiltySwitch.setChecked(false);

                }





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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeDelivery_Slot_Availability_Status.this).add(jsonObjectRequest);



    }

    private void changeStatusintheDeliverySlotDetails(String status) {
        showProgressBar(true);

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key", deliveryslotkeyfromSlotdetails);
            jsonObject.put("status", status);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_DeliverySlotDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        showProgressBar(false);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    showProgressBar(false);
                    Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeDelivery_Slot_Availability_Status.this).add(jsonObjectRequest);











    }


    private void checkforDeliverySlotDetails() {
        showProgressBar(true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlotDetails+"?storeid="+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);
                                String slotName = String.valueOf(json.get("slotname"));
                                slotName = slotName.toUpperCase();
                                if(slotName.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)){
                                    deliveryslotkeyfromSlotdetails= String.valueOf(json.get("key"));
                                    String status =String.valueOf(json.get("status"));
                                    status = status.toUpperCase();

                                    if(status.equals("ACTIVE")){
                                        isActiveinDeliverySlotDetails=true;
                                    }
                                    if(status.equals("INACTIVE")){
                                        isActiveinDeliverySlotDetails=false;

                                    }
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                    checkforDeliverySlots();

                } catch (JSONException e) {
                    e.printStackTrace();
                    checkforDeliverySlots();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                checkforDeliverySlots();

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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeDelivery_Slot_Availability_Status.this).add(jsonObjectRequest);




    }

    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            todaysDeliverySlotListview.setVisibility(View.GONE);
            tomorrowsDeliverySlotListview.setVisibility(View.GONE);
            expressDeliveryAvailabiltySwitch.setVisibility(View.GONE);
        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            todaysDeliverySlotListview.setVisibility(View.VISIBLE);
            tomorrowsDeliverySlotListview.setVisibility(View.VISIBLE);
            expressDeliveryAvailabiltySwitch.setVisibility(View.VISIBLE);

        }

    }

}