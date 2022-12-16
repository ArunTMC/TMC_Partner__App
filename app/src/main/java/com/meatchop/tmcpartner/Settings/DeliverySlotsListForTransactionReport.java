package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlots;

public class DeliverySlotsListForTransactionReport extends AppCompatActivity {
    ListView tomorrowsDeliverySlotList;
    ListView todaysDeliverySlotList;
    LinearLayout loadingpanelmask , loadingPanel,expressLayout;
    public static String vendorkey;
    ArrayList<Modal_DeliverySlots> todaySlot_deliverySlotsArrayList = new ArrayList<>();
    ArrayList<Modal_DeliverySlots> tomorrowSlot_deliverySlotsArrayList = new ArrayList<>();
    Modal_DeliverySlots expressDeliveryModal_DeliverySlots = new Modal_DeliverySlots();
    double screenInches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_slots_transaction_report);
        tomorrowsDeliverySlotList = findViewById(R.id.tomorrowsDeliverySlotList);
        loadingPanel  = findViewById(R.id.loadingPanel);
        loadingpanelmask  = findViewById(R.id.loadingpanelmask);
        todaysDeliverySlotList = findViewById(R.id.todaysDeliverySlotList);
        expressLayout = findViewById(R.id.expressLayout);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));
       // mobileno = (shared.getString("UserPhoneNumber", "+91"));

        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(DeliverySlotsListForTransactionReport.this);
            //Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
                // Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

            getDeliverySlots();

        expressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeliverySlotsListForTransactionReport.this , DeliverySlotsTransactionDetailsReport.class);
                intent.putExtra("deliveryslotkey", expressDeliveryModal_DeliverySlots.getKey());
                intent.putExtra("deliveryslotname", expressDeliveryModal_DeliverySlots.getSlotname());
                intent.putExtra("vendorkey", DeliverySlotsListForTransactionReport.vendorkey);
                intent.putExtra("deliverytime", "120 Mins");
                startActivity(intent);

            }
        });
    }




    private void getDeliverySlots() {
        showProgressBar(true);

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
                                if(slotName.toUpperCase().equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {
                                    expressDeliveryModal_DeliverySlots.slotdatetype = String.valueOf(json.get("slotdatetype"));
                                    expressDeliveryModal_DeliverySlots.key =  String.valueOf(json.get("key"));
                                    expressDeliveryModal_DeliverySlots.status = String.valueOf(json.get("status"));
                                    expressDeliveryModal_DeliverySlots.slotstarttime = String.valueOf(json.get("slotstarttime"));
                                    expressDeliveryModal_DeliverySlots.slotendtime = String.valueOf(json.get("slotendtime"));
                                    expressDeliveryModal_DeliverySlots.slotname = String.valueOf(json.get("slotname"));

                                }
                                else {
                                    if(slotDateType.toUpperCase().equals(Constants.TODAYPREORDER_SLOTNAME)) {

                                        Modal_DeliverySlots modal_deliverySlots = new Modal_DeliverySlots();
                                        modal_deliverySlots.slotdatetype = String.valueOf(json.get("slotdatetype"));
                                        modal_deliverySlots.key = String.valueOf(json.get("key"));
                                        modal_deliverySlots.status = String.valueOf(json.get("status"));
                                        modal_deliverySlots.slotstarttime = String.valueOf(json.get("slotstarttime"));
                                        modal_deliverySlots.slotendtime = String.valueOf(json.get("slotendtime"));
                                        modal_deliverySlots.slotname = String.valueOf(json.get("slotname"));

                                        todaySlot_deliverySlotsArrayList.add(modal_deliverySlots);
                                    }
                                    if(slotDateType.toUpperCase().equals(Constants.TOMORROWPREORDER_SLOTNAME)) {
                                        Modal_DeliverySlots modal_deliverySlots = new Modal_DeliverySlots();
                                        modal_deliverySlots.slotdatetype = String.valueOf(json.get("slotdatetype"));
                                        modal_deliverySlots.key = String.valueOf(json.get("key"));
                                        modal_deliverySlots.status = String.valueOf(json.get("status"));
                                        modal_deliverySlots.slotstarttime = String.valueOf(json.get("slotstarttime"));
                                        modal_deliverySlots.slotendtime = String.valueOf(json.get("slotendtime"));
                                        modal_deliverySlots.slotname = String.valueOf(json.get("slotname"));

                                        tomorrowSlot_deliverySlotsArrayList.add(modal_deliverySlots);
                                    }

                                }



                                if(i - (content.length() - 1) == 0){
                                    setAdapter();
                                }



                            } catch (JSONException e) {

                                showProgressBar(false);

                                e.printStackTrace();
                            }
                        }



                    }
                } catch (JSONException e) {

                    showProgressBar(false);

                 e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                error.printStackTrace();
                showProgressBar(false);


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
        Volley.newRequestQueue(DeliverySlotsListForTransactionReport.this).add(jsonObjectRequest);



    }

    private void setAdapter() {
        try {

            Collections.sort(todaySlot_deliverySlotsArrayList, new Comparator<Modal_DeliverySlots>() {
                public int compare(final Modal_DeliverySlots object1, final Modal_DeliverySlots object2) {
                    return object2.getSlotstarttime().compareTo(object1.getSlotstarttime());
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }


        Adapter_DeliverySlotsTransaction adapter_todaysDeliverySlots = new Adapter_DeliverySlotsTransaction(DeliverySlotsListForTransactionReport.this, todaySlot_deliverySlotsArrayList, Constants.TODAYPREORDER_SLOTNAME);
        todaysDeliverySlotList.setAdapter(adapter_todaysDeliverySlots);
        Helper.getListViewSize(todaysDeliverySlotList, screenInches,0);
//////////////////////////////////
        try {

            Collections.sort(tomorrowSlot_deliverySlotsArrayList, new Comparator<Modal_DeliverySlots>() {
                public int compare(final Modal_DeliverySlots object1, final Modal_DeliverySlots object2) {
                    return object2.getSlotstarttime().compareTo(object1.getSlotstarttime());
                }
            });
        } catch (Exception e) {


            e.printStackTrace();
        }


        Adapter_DeliverySlotsTransaction adapter_tomorrowsDeliverySlots = new Adapter_DeliverySlotsTransaction(DeliverySlotsListForTransactionReport.this, tomorrowSlot_deliverySlotsArrayList, Constants.TOMORROWPREORDER_SLOTNAME);
        tomorrowsDeliverySlotList.setAdapter(adapter_tomorrowsDeliverySlots);
        Helper.getListViewSize(tomorrowsDeliverySlotList, screenInches,0);
        showProgressBar(false);

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


}