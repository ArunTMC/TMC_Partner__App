package com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.Adapter_Mobile_AssignDeliveryPartner1;
import com.meatchop.tmcpartner.settings.searchOrdersUsingMobileNumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AssigningDeliveryPartner extends AppCompatActivity {
ListView deliveryPartners_list_widget;
List<AssignDeliveryPartner_PojoClass>deliveryPartnerList;
String vendorKey,orderKey,vendorname,orderid,customerMobileNo,fromActivityName ="";
private LinearLayout loadingPanel_dailyItemWisereport,loadingpanelmask_dailyItemWisereport;
    TextView deliverypersonList_instructiontextview ;
    TextView deliveryPersonName_TextView;
    ImageView searchicon ;
    ImageView closeicon ;
    EditText deliveryPersonName_editText;
    Adapter_AssignDeliveryPartner adapter_assignDeliveryPartner ;
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



        deliverypersonList_instructiontextview = findViewById(R.id.deliverypersonList_instructiontextview);
        deliveryPersonName_TextView = findViewById(R.id.deliveryPersonName_TextView);
        searchicon = findViewById(R.id.searchicon);
        closeicon = findViewById(R.id.closeicon);
        deliveryPersonName_editText = findViewById(R.id.deliveryPersonName_editText);

        deliveryPersonName_TextView.setVisibility(View.VISIBLE);
        searchicon.setVisibility(View.VISIBLE);
        closeicon.setVisibility(View.GONE);
        deliveryPersonName_editText.setVisibility(View.GONE);
        deliverypersonList_instructiontextview.setVisibility(View.VISIBLE);
        deliverypersonList_instructiontextview.setText("Loading...");





        deliveryPartners_list_widget=findViewById(R.id.deliveryPartners_list_widget);
        deliveryPartnerList = new ArrayList<>();
        SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", ""));
        vendorname = (shared.getString("VendorName", ""));
        fromActivityName = getIntent().getStringExtra("From");
        if(!fromActivityName.equals("DeliveryPartnerSettlementReport")) {
            orderKey = getIntent().getStringExtra("TrackingTableKey");
            orderid = getIntent().getStringExtra("orderid");
            customerMobileNo = getIntent().getStringExtra("customerMobileNo");
        }
     //   Toast.makeText(AssigningDeliveryPartner.this, "3  -" +fromActivityName, Toast.LENGTH_SHORT).show();

        if(vendorKey.equals("")){
            vendorKey = getIntent().getStringExtra("vendorkey");

        }
        getDeliveryPartnerList();




        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryPersonName_TextView.setVisibility(View.GONE);
                searchicon.setVisibility(View.GONE);
                closeicon.setVisibility(View.VISIBLE);
                deliveryPersonName_editText.setVisibility(View.VISIBLE);
                showKeyboard(deliveryPersonName_editText);

            }
        });

        deliveryPersonName_TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryPersonName_TextView.setVisibility(View.GONE);
                searchicon.setVisibility(View.GONE);
                closeicon.setVisibility(View.VISIBLE);
                deliveryPersonName_editText.setVisibility(View.VISIBLE);
                showKeyboard(deliveryPersonName_editText);
            }
        });

        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryPersonName_TextView.setVisibility(View.VISIBLE);
                searchicon.setVisibility(View.VISIBLE);
                closeicon.setVisibility(View.GONE);
                deliveryPersonName_editText.setVisibility(View.GONE);
                hideKeyboard(deliveryPersonName_editText);


                deliverypersonList_instructiontextview.setVisibility(View.GONE);
                deliveryPartners_list_widget.setVisibility(View.VISIBLE);
                deliverypersonList_instructiontextview.setText("");
                deliveryPersonName_editText.setText("");
                try {
                    adapter_assignDeliveryPartner= new Adapter_AssignDeliveryPartner(AssigningDeliveryPartner.this,deliveryPartnerList,orderKey,vendorKey,customerMobileNo,orderid,fromActivityName);
                    deliveryPartners_list_widget.setAdapter(adapter_assignDeliveryPartner);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        deliveryPersonName_editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String deliveryPersonNameFromListener = String.valueOf(s);
                if(!deliveryPersonNameFromListener.equals("")) {
                    try {
                        List<AssignDeliveryPartner_PojoClass> sorteddeliveryPartnerList = new ArrayList<>();
                        try {
                                for (int i = 0; i < deliveryPartnerList.size(); i++) {

                                    AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass = new AssignDeliveryPartner_PojoClass();
                                    assignDeliveryPartner_pojoClass = deliveryPartnerList.get(i);
                                    if (assignDeliveryPartner_pojoClass.getDeliveryPartnerName().toUpperCase().contains(deliveryPersonNameFromListener.toUpperCase())) {
                                        sorteddeliveryPartnerList.add(assignDeliveryPartner_pojoClass);
                                    }

                                    if (i == (deliveryPartnerList.size() - 1)) {
                                        if (sorteddeliveryPartnerList.size() > 0) {

                                            deliverypersonList_instructiontextview.setVisibility(View.GONE);
                                            deliveryPartners_list_widget.setVisibility(View.VISIBLE);
                                            deliverypersonList_instructiontextview.setText("");
                                            // Toast.makeText(mContext, "Toasstt", Toast.LENGTH_SHORT).show();
                                            adapter_assignDeliveryPartner= new Adapter_AssignDeliveryPartner(AssigningDeliveryPartner.this,sorteddeliveryPartnerList,orderKey,vendorKey,customerMobileNo,orderid,fromActivityName);
                                            deliveryPartners_list_widget.setAdapter(adapter_assignDeliveryPartner);

                                        } else {
                                            deliverypersonList_instructiontextview.setVisibility(View.VISIBLE);
                                            deliveryPartners_list_widget.setVisibility(View.GONE);
                                            deliverypersonList_instructiontextview.setText("There is no delivery person in this name");
                                        }
                                    }

                                }



                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        deliverypersonList_instructiontextview.setVisibility(View.GONE);








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

                                    adapter_assignDeliveryPartner= new Adapter_AssignDeliveryPartner(AssigningDeliveryPartner.this,deliveryPartnerList,orderKey,vendorKey,customerMobileNo,orderid,fromActivityName);
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

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    private void showKeyboard(final EditText editText) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
    }

}