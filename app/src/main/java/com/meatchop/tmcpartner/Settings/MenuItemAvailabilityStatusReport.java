package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.apache.poi.ss.formula.functions.T;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meatchop.tmcpartner.Constants.TAG;

public class MenuItemAvailabilityStatusReport extends AppCompatActivity {
    LinearLayout loadingPanel,loadingpanelmask;
    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    String SubCtgyKey;
    JSONArray result;
    String vendorkey,SubCtgyName,vendorName,CurrentDate_time;
    TextView date_textWidget,vendorName_textWidget;
    public static List<Modal_SubCtgyList> subCtgyName_arrayList;
    public static HashMap<String, Modal_MenuItem_Settings>  MenuItemHashmap  = new HashMap();
    String errorCode = "0";

    private RecyclerView recycler;
    private RecyclerView.LayoutManager manager;
    private Adapter_MenuItemAvaialabilityStatusReport adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_availability_status_report);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        date_textWidget = findViewById(R.id.date_textWidget);
        vendorName_textWidget = findViewById(R.id.vendorName_textWidget);

        recycler = findViewById(R.id.recyclerView);
        recycler.addItemDecoration(new GridLayoutItemDecoration(4));

        recycler.setHasFixedSize(true);
        manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);

        Adjusting_Widgets_Visibility(true);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));
        vendorName = (shared.getString("VendorName", ""));
        CurrentDate_time =getDate_and_time();
        getMenuItemArrayFromSharedPreferences();

        subCtgyName_arrayList = new ArrayList<>();
        MenuItemHashmap.clear();
        subCtgyName_arrayList.clear();
        date_textWidget.setText(CurrentDate_time);
        vendorName_textWidget.setText(vendorName);

    }


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
            if(MenuItem.size()>0){
                getMenuItemStockAvlDetails();
            }
            else{
                Toast.makeText(getApplicationContext(),"There is no MenuItem Please Restart the APP",Toast.LENGTH_LONG).show();

            }
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
                                String displayNo ="";
                                if(subctgyname.equals("Sea Food")){
                                     displayNo = "1";

                                }
                                else if(subctgyname.equals("Goat")){
                                     displayNo = "2";

                                }
                                else if(subctgyname.equals("Chicken")){
                                     displayNo = "3";

                                }
                                else if(subctgyname.equals("Country Chicken")){
                                     displayNo = "4";

                                }
                                else if(subctgyname.equals("Eggs")){
                                     displayNo = "5";

                                }
                                else if(subctgyname.equals("Dried Fish")){
                                     displayNo = "6";

                                }
                                else if(subctgyname.equals("Ready To Cook")){
                                     displayNo = "7";

                                }
                                else if(subctgyname.equals("Marinades")){
                                    displayNo = "8";

                                }
                                else if(subctgyname.equals("Kitchen Preparation")){
                                    displayNo = "9";

                                }
                                else if(subctgyname.equals("Chicken Kit")){
                                    displayNo = "10";

                                }
                                else if(subctgyname.equals("Mutton Kit")){
                                    displayNo = "11";

                                }
                                else if(subctgyname.equals("Prawn Kit")){
                                    displayNo = "12";

                                }
                                else if(subctgyname.equals("Cereals Spices and more")){
                                    displayNo = "13";

                                }
                                else{
                                    displayNo = "14";

                                }
                                //Log.d(Constants.TAG, "subctgyname from subCtgy: " + subctgyname);
                                Modal_SubCtgyList  modal_subCtgyList = new Modal_SubCtgyList();
                                modal_subCtgyList.setKey(key);
                                modal_subCtgyList.setSubCtgyName(subctgyname);
                                modal_subCtgyList.setDisplayNo(displayNo);
                                if (!subCtgyName_arrayList.contains(subctgyname)) {
                                    subCtgyName_arrayList.add(modal_subCtgyList);

                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                        try{
                            Collections.sort(subCtgyName_arrayList, new Comparator<Modal_SubCtgyList>() {
                                public int compare(final Modal_SubCtgyList object1, final Modal_SubCtgyList object2) {
                                    String tokenNo_1 = object1.getDisplayNo();
                                    String tokenNo_2 = object2.getDisplayNo();

                                    if((tokenNo_1.equals(""))||(tokenNo_1.equals("null"))||(tokenNo_1.equals(null))){
                                        tokenNo_1=String.valueOf(0);
                                    }
                                    if((tokenNo_2.equals(""))||(tokenNo_2.equals("null"))||(tokenNo_2.equals(null))){
                                        tokenNo_2=String.valueOf(0);
                                    }

                                    Long i2 = Long.valueOf(tokenNo_2);
                                    Long i1 = Long.valueOf(tokenNo_1);

                                    return i1.compareTo(i2);
                                }
                            });
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }





                        AddDatatoHashmap(subCtgyName_arrayList);



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //adapter_subCtgy_spinner = new ArrayAdapter<Modal_SubCtgyList>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subCtgyName_arrayList);
                //subCtgyItem_spinner.setAdapter(adapter_subCtgy_spinner);


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
        Volley.newRequestQueue(MenuItemAvailabilityStatusReport.this).add(jsonObjectRequest);

    }
    private void getMenuItemStockAvlDetails() {
        Adjusting_Widgets_Visibility(true);

        Log.d(TAG, "starting:getfullMenuItemStockavldetailsUsingStoreID ");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItemStockAvlDetails+vendorkey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try{
                    Log.d(TAG, "starting:onResponse ");

                    Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());

                    try {
                        JSONArray JArray = response.getJSONArray("content");

                        Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                        for (; i1 < (arrayLength); i1++) {
                            String menuItemkeyFromStockAvl = "";

                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                try{
                                    if(json.has("menuitemkey")){

                                        menuItemkeyFromStockAvl =    String.valueOf(json.get("menuitemkey"));




                                        for(int iterator = 0; iterator<MenuItem.size(); iterator++) {
                                            Modal_MenuItem_Settings modal_menuItem = MenuItem.get(iterator);
                                            String menuItemKey = "";
                                            try {
                                                menuItemKey = modal_menuItem.getKey().toString();
                                            } catch (Exception e) {
                                                menuItemKey = "";
                                                e.printStackTrace();
                                            }

                                            if (menuItemKey.equals(menuItemkeyFromStockAvl)) {


                                                if (json.has("key")) {
                                                    modal_menuItem.key_AvlDetails = String.valueOf(json.get("key"));
                                                } else {
                                                    modal_menuItem.key_AvlDetails = "";
                                                    Log.d(Constants.TAG, "There is no key for this Menu: ");


                                                }

                                                if (json.has("allownegativestock")) {
                                                    modal_menuItem.allownegativestock = String.valueOf(json.get("allownegativestock"));
                                                } else {
                                                    modal_menuItem.allownegativestock = "";
                                                    Log.d(Constants.TAG, "There is no allownegativestock for this Menu: ");


                                                }


                                                if (json.has("barcode")) {
                                                    modal_menuItem.barcode_AvlDetails = String.valueOf(json.get("barcode"));

                                                } else {
                                                    modal_menuItem.barcode_AvlDetails = "";



                                                }
                                                if (json.has("itemavailability")) {
                                                    modal_menuItem.itemavailability_AvlDetails = String.valueOf(json.get("itemavailability"));

                                                } else {
                                                    modal_menuItem.itemavailability_AvlDetails = "";



                                                }
                                                if (json.has("lastupdatedtime")) {
                                                    modal_menuItem.lastupdatedtime_AvlDetails = String.valueOf(json.get("lastupdatedtime"));
                                                    if (String.valueOf(json.get("lastupdatedtime")).equals("")) {
                                                        modal_menuItem.lastupdatedtime_AvlDetails = "0";

                                                    } else if (String.valueOf(json.get("lastupdatedtime")).equals("\r")) {

                                                        modal_menuItem.lastupdatedtime_AvlDetails = "0";

                                                    }

                                                } else {
                                                    modal_menuItem.lastupdatedtime_AvlDetails = "0";


                                                }


                                                if (json.has("menuitemkey")) {
                                                    modal_menuItem.menuitemkey_AvlDetails = String.valueOf(json.get("menuitemkey"));
                                                    if (String.valueOf(json.get("menuitemkey")).equals("")) {
                                                        modal_menuItem.menuitemkey_AvlDetails = "0";

                                                    } else if (String.valueOf(json.get("menuitemkey")).equals("\r")) {

                                                        modal_menuItem.menuitemkey_AvlDetails = "0";

                                                    }

                                                } else {
                                                    modal_menuItem.menuitemkey_AvlDetails = "0";


                                                }


                                                if (json.has("stockbalance")) {
                                                    modal_menuItem.stockbalance_AvlDetails = String.valueOf(json.get("stockbalance"));
                                                    if (String.valueOf(json.get("stockbalance")).equals("")) {
                                                        modal_menuItem.stockbalance_AvlDetails = "0";

                                                    } else if (String.valueOf(json.get("stockbalance")).equals("\r")) {

                                                        modal_menuItem.stockbalance_AvlDetails = "0";

                                                    }
                                                } else {
                                                    modal_menuItem.stockbalance_AvlDetails = "0";


                                                }

                                                if (json.has("stockincomingkey")) {
                                                    modal_menuItem.stockincomingkey_AvlDetails = String.valueOf(json.get("stockincomingkey"));

                                                } else {
                                                    modal_menuItem.stockincomingkey_AvlDetails = "";


                                                }
                                                if (json.has("vendorkey")) {
                                                    modal_menuItem.vendorkey_AvlDetails = String.valueOf(json.get("vendorkey"));

                                                } else {
                                                    modal_menuItem.vendorkey_AvlDetails = "";

                                                }
                                            }
                                        }

                                    }
                                    else{
                                        menuItemkeyFromStockAvl = "";
                                    }
                                }
                                catch (Exception e){
                                    menuItemkeyFromStockAvl = "";
                                    e.printStackTrace();
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                Log.d(Constants.TAG, "e: " + e.getMessage());
                                Log.d(Constants.TAG, "e: " + e.toString());

                            }
                            if(arrayLength - i1 ==1) {
                                final SharedPreferences sharedPreferences = getSharedPreferences("MenuList", MODE_PRIVATE);

                                Gson gson = new Gson();
                                String json = gson.toJson(MenuItem);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("MenuList", json);
                                editor.apply();
                                getMenuCategoryList();
                            }

                        }



                    } catch (JSONException e) {
                        Adjusting_Widgets_Visibility(false);

                        e.printStackTrace();
                    }














                }catch(Exception e){
                    Adjusting_Widgets_Visibility(false);

                    e.printStackTrace();
                }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(TAG, "Error: " + error.getLocalizedMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.toString());

                if (error instanceof TimeoutError) {
                    errorCode = "Timeout Error";
                } else if (error instanceof NoConnectionError) {
                    errorCode = "No Connection Error";

                } else if (error instanceof AuthFailureError) {
                    errorCode = "Auth_Failure Error";
                } else if (error instanceof ServerError) {
                    errorCode = "Server Error";
                } else if (error instanceof NetworkError) {
                    errorCode = "Network Error";
                } else if (error instanceof ParseError) {
                    errorCode = "Parse Error";
                }
                Toast.makeText(MenuItemAvailabilityStatusReport.this,"Error in Getting  Menuitem stock Avl details  error code :  "+errorCode,Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);



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
        RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(MenuItemAvailabilityStatusReport.this).add(jsonObjectRequest);

    }
    private void AddDatatoHashmap(List<Modal_SubCtgyList> subCtgyName_arrayList) {

        for(int i1 =0;i1<subCtgyName_arrayList.size();i1++){
            int total_no_of_item_Available,total_no_of_item;
            double total_no_of_item_Available_inPercentage;
            Modal_SubCtgyList modal_subCtgyList = subCtgyName_arrayList.get(i1);
            String subCtgykey = modal_subCtgyList.getKey();
            total_no_of_item=0;
            total_no_of_item_Available = 0;
            total_no_of_item_Available_inPercentage =0;

            try {
                for (int i2 = 0; i2 < MenuItem.size(); i2++) {
                    Modal_MenuItem_Settings menuItem_settings = MenuItem.get(i2);
                    String tmcsubCtgykey = menuItem_settings.getTmcsubctgykey();
                    Modal_MenuItem_Settings menuItemAvailability_SubCtgywise = new Modal_MenuItem_Settings();
                    if (tmcsubCtgykey.equals(subCtgykey)) {
                        try {


                                String itemAvailabilityforcount = String.valueOf(menuItem_settings.getItemavailability_AvlDetails()).toUpperCase();

                            if(itemAvailabilityforcount.equals("") || itemAvailabilityforcount.equals("NIL") || itemAvailabilityforcount.equals("null") || itemAvailabilityforcount.equals(null) || itemAvailabilityforcount.equals("NULL")) {

                                itemAvailabilityforcount = String.valueOf(menuItem_settings.getItemavailability()).toUpperCase();
                            }
                            else{
                                String allowNegativeStock = String.valueOf(menuItem_settings.getAllownegativestock()).toUpperCase();
                                String stockbalance = String.valueOf(menuItem_settings.getStockbalance_AvlDetails());
                                if(allowNegativeStock.equals("FALSE")){
                                    try{
                                        double stockBalance_double = Double.parseDouble(stockbalance);
                                        if(stockBalance_double<=0){
                                            itemAvailabilityforcount ="FALSE";
                                        }
                                    }
                                    catch (Exception e){
                                        itemAvailabilityforcount ="TRUE";
                                        e.printStackTrace();
                                    }
                                }
                            }
                                total_no_of_item = total_no_of_item+1;
                                if (itemAvailabilityforcount.equals("TRUE")) {
                                    total_no_of_item_Available = total_no_of_item_Available + 1;

                                }



                                try {
                                    total_no_of_item_Available_inPercentage = (Double.parseDouble(String.valueOf((total_no_of_item_Available))) / Double.parseDouble(String.valueOf(total_no_of_item) ));
                                    total_no_of_item_Available_inPercentage = total_no_of_item_Available_inPercentage*100;

                                }catch (Exception e ) {
                                    e.printStackTrace();
                                }
                                //("Out of "+String.valueOf(total_no_of_item)+" Items / "+String.valueOf(total_no_of_item_Available)+" Items Available"+" ( "+String.valueOf(total_no_of_item_Available_inPercentage)+" % ) ");



                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }


                        try{
                            menuItemAvailability_SubCtgywise.setTmcsubctgykey(String.valueOf(subCtgykey));
                            menuItemAvailability_SubCtgywise.setTotalnoofmenuitem(String.valueOf(total_no_of_item));
                            menuItemAvailability_SubCtgywise.setNoofmenuitemavailable(String.valueOf(total_no_of_item_Available));
                            try {
                                int total_no_of_item_Available_inPercentage_int = (int) Math.round(total_no_of_item_Available_inPercentage);
                                menuItemAvailability_SubCtgywise.setNoofmenuitemavailableinpercentage(String.valueOf(total_no_of_item_Available_inPercentage_int));
                            }
                            catch(Exception e){
                                menuItemAvailability_SubCtgywise.setNoofmenuitemavailableinpercentage(String.valueOf(total_no_of_item_Available_inPercentage));

                                e.printStackTrace();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                      /*  boolean isAlreadyAvailable = false;
                        try{
                            isAlreadyAvailable = checkIfsubCtgyisAvailableinHashmap(tmcsubCtgykey);

                        }catch(Exception e ){
                            e.printStackTrace();;
                        }


                       */
                        MenuItemHashmap.put(tmcsubCtgykey,menuItemAvailability_SubCtgywise);




                    }


                }
            }
            catch (Exception e){
                e.printStackTrace();
            }





        }
        try {
            adapter = new Adapter_MenuItemAvaialabilityStatusReport(subCtgyName_arrayList, MenuItemHashmap, this,MenuItemAvailabilityStatusReport.this);
            recycler.setAdapter(adapter);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Adjusting_Widgets_Visibility(false);


    }

    private boolean checkIfsubCtgyisAvailableinHashmap(String tmcsubCtgykey) {
        return MenuItemHashmap.containsKey(tmcsubCtgykey);
    }






 /*   private void getMenuItemsbasedOnSubCtgy(String subCtgykey) {
        total_no_item_availability =0;
        total_no_of_item =0;
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
                selected_CtgyItems.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());

                try {
                    selected_CtgyItems.marinadeKey = String.valueOf(modal_menuItemSettings.getMarinadeKey());
                    selected_CtgyItems.isMarinadeItem =true;
                    selected_CtgyItems.marinadeItemAvailability = String.valueOf(modal_menuItemSettings.getMarinadeItemAvailability());
                    selected_CtgyItems.marinadeBarcode = String.valueOf(modal_menuItemSettings.getMarinadeBarcode());
                    selected_CtgyItems.marinadeItemUniqueCode = String.valueOf(modal_menuItemSettings.getMarinadeItemUniqueCode());
                }
                catch (Exception e){
                    selected_CtgyItems.marinadeKey = "";
                    selected_CtgyItems.marinadeItemAvailability ="";
                    selected_CtgyItems.marinadeBarcode = "";
                    selected_CtgyItems.marinadeItemUniqueCode = "";
                    selected_CtgyItems.isMarinadeItem =false;
                }
                if(String.valueOf(modal_menuItemSettings.getItemavailability()).equals("TRUE")) {
                    total_no_item_availability = total_no_item_availability+1;
                }
                displaying_menuItems.add(selected_CtgyItems);
                //Log.d(Constants.TAG, "displaying_menuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));
                Adjusting_Widgets_Visibility(false);
                try{
                    Collections.sort(displaying_menuItems, new Comparator<Modal_MenuItem_Settings>() {
                        public int compare(final Modal_MenuItem_Settings object1, final Modal_MenuItem_Settings object2) {
                            Long i2 = Long.valueOf(object2.getDisplayno());
                            Long i1 = Long.valueOf(object1.getDisplayno());
                            return i1.compareTo(i2);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                total_no_of_item = displaying_menuItems.size();
                //  itemAvailabilityCount_textWidget.setText("Out of "+String.valueOf(total_no_of_item)+" Items / "+String.valueOf(total_no_item_availability)+" Items Available");
                adapter_Change_menutem_availability_settings = new Adapter_ChangeMenutem_Availability_settings(ChangeMenuItemStatus_Settings.this, displaying_menuItems, ChangeMenuItemStatus_Settings.this);

                MenuItemsListView.setAdapter(adapter_Change_menutem_availability_settings);

            }
            if(displaying_menuItems.size()<=0){
                itemAvailabilityCount_textWidget.setText("There is no MenuItem Under this SubCtgy");

            }


        }





    }


  */


    public String getDate_and_time()
    {
        String CurrentDay,CurrentDate,FormattedTime,formattedDate;
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
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

    void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }
    }

}