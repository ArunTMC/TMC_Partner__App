package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlots;

public class MenuItem_List_Settings extends AppCompatActivity {
    LinearLayout loadingPanel,loadingpanelmask;
    Spinner subCtgyItem_spinner;
    ArrayAdapter adapter_subCtgy_spinner;
    String vendorkey,deliverySlotKey ;
    ListView MenuItemsListView;
    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    TextView headingTextview;
    String IntentFrom ;
    public static List<Modal_MenuItem_Settings> displaying_menuItems;
    //public static List<Modal_MenuItem_Settings> completemenuItem;
    public static List<String> subCtgyName_arrayList;
    JSONArray result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_list_settings_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();


        IntentFrom = getIntent().getExtras().getString("ClickedOn","");
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        subCtgyItem_spinner = findViewById(R.id.subCtgyItem);
        MenuItemsListView = findViewById(R.id.MenuItemsListView);
        headingTextview = findViewById(R.id.headingTextview);
        Adjusting_Widgets_Visibility(true);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));
        getMenuItemArrayFromSharedPreferences();
        if(IntentFrom.equals("ChangeMenuItemPrice")) {
            headingTextview.setText("Select Menu Item to Change Price ");
        }
        if(IntentFrom.equals("MenuAvailabilityTransactionDetails")) {
            headingTextview.setText("Select Item to get its On/Off Transaction");
        }
        if(IntentFrom.equals("ChangeMenuItemPriceAndWeight")) {
            Adjusting_Widgets_Visibility(true);
            //getItemCutDetails();

            headingTextview.setText("Select Menu to Change Price & Weight");
        }


        getMenuCategoryList();
        displaying_menuItems = new ArrayList<>();
        subCtgyName_arrayList = new ArrayList<>();




        subCtgyItem_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d(Constants.TAG, "displaying_menuItems: " + displaying_menuItems.size());

                String SubCtgyKey=getVendorData(i,"key");
                getMenuItemsbasedOnSubCtgy(SubCtgyKey);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








    }
/*
    private void getItemCutDetails() {

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


 */


    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem_Settings>>() {
            }.getType();
            MenuItem  = gson.fromJson(json, type);
        }

    }






    private void getMenuCategoryList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetMenuCategory,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    //Log.d(Constants.TAG, "response from subCtgy: " + response.get("content"));
                    result  = response.getJSONArray("content");

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);
                                String ctgyname = String.valueOf(json.get("tmcctgyname"));

                                String key = String.valueOf(json.get("key"));
                                String subctgyname = String.valueOf(json.get("subctgyname"));
                                String displayNo = String.valueOf(json.get("displayno"));
                                //Log.d(Constants.TAG, "subctgyname from subCtgy: " + subctgyname);
                                if (!subCtgyName_arrayList.contains(subctgyname)) {
                                    subCtgyName_arrayList.add(subctgyname);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





                adapter_subCtgy_spinner = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subCtgyName_arrayList);
                subCtgyItem_spinner.setAdapter(adapter_subCtgy_spinner);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("modulename", "SubCategory");

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(MenuItem_List_Settings.this).add(jsonObjectRequest);

    }

    private String getVendorData(int position,String fieldName){
        String data="";
        try {
            JSONObject json = result.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    private void getMenuItemsbasedOnSubCtgy(String subCtgykey) {

        displaying_menuItems.clear();
        for(int i=0;i<MenuItem.size();i++){
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String menuSubCtgy = modal_menuItemSettings.getTmcsubctgykey();
            if(menuSubCtgy.equals(subCtgykey)) {
                String MenuItemName;
                Modal_MenuItem_Settings selected_CtgyItems = new Modal_MenuItem_Settings();
                selected_CtgyItems.key=String.valueOf(modal_menuItemSettings.getKey());
                selected_CtgyItems.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                selected_CtgyItems.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                selected_CtgyItems.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                selected_CtgyItems.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                selected_CtgyItems.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                selected_CtgyItems.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                selected_CtgyItems.showinmenuboard = String.valueOf(modal_menuItemSettings.getShowinmenuboard());
                selected_CtgyItems.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                displaying_menuItems.add(selected_CtgyItems);
                //Log.d(Constants.TAG, "displaying_menuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));
                Adjusting_Widgets_Visibility(false);

                try{






                    Collections.sort(displaying_menuItems, new Comparator<Modal_MenuItem_Settings>() {
                        public int compare(final Modal_MenuItem_Settings object1, final Modal_MenuItem_Settings object2) {
                            String i2 = String.valueOf(object2.getItemname());
                            String i1 = String.valueOf(object1.getItemname());
                            return i1.compareTo(i2);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }



                Adapter_ChangeMenuItem_Price adapter_changeMenuItem_price = new Adapter_ChangeMenuItem_Price(MenuItem_List_Settings.this, displaying_menuItems, MenuItem_List_Settings.this,IntentFrom);

                MenuItemsListView.setAdapter(adapter_changeMenuItem_price);

            }


        }





    }


    private void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }
    }





}