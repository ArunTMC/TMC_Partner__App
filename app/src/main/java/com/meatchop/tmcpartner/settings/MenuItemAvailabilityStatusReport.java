package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;
import com.meatchop.tmcpartner.sqlite.TMCSubCtgyItemSQL_DB_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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

    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
    boolean localDBcheck = false , isinventorycheck =false;

    TMCSubCtgyItemSQL_DB_Manager tmcSubCtgyItemSQL_db_manager ;
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
        localDBcheck = (shared.getBoolean("localdbcheck", false));
        CurrentDate_time =getDate_and_time();
        if(localDBcheck){
            getDataFromSQL("FetchMenuItem");
        }
        else{
            getMenuItemArrayFromSharedPreferences();
            getMenuCategoryList();
        }


        subCtgyName_arrayList = new ArrayList<>();
        MenuItemHashmap.clear();
        subCtgyName_arrayList.clear();
        date_textWidget.setText(CurrentDate_time);
        vendorName_textWidget.setText(vendorName);

    }

    @SuppressLint("Range")
    private void getDataFromSQL(String methodToCall) {

        try {
            if (methodToCall.equals("FetchMenuItem")) {

                if (tmcMenuItemSQL_db_manager == null) {
                    tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(getApplicationContext());
                    try {
                        tmcMenuItemSQL_db_manager.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                try {
                    Cursor cursor = tmcMenuItemSQL_db_manager.Fetch();
                    MenuItem.clear();
                    try {
                        // if (cursor.moveToFirst()) {

                        Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                        Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                        if (cursor.getCount() > 0) {

                            if (cursor.moveToFirst()) {
                                do {
                                    Modal_MenuItem_Settings modal_menuItem_settings = new Modal_MenuItem_Settings();
                                    modal_menuItem_settings.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                                    modal_menuItem_settings.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                                    modal_menuItem_settings.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                                    modal_menuItem_settings.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.localDB_id)));
                                    modal_menuItem_settings.setApplieddiscountpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.applieddiscountpercentage)));
                                    modal_menuItem_settings.setBarcode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode)));
                                    modal_menuItem_settings.setCheckoutimageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.checkoutimageurl)));
                                    modal_menuItem_settings.setDisplayno(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.displayno)));
                                    modal_menuItem_settings.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                                    modal_menuItem_settings.setGstpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.gstpercentage)));
                                    modal_menuItem_settings.setItemavailability(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability)));
                                    modal_menuItem_settings.setItemuniquecode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemuniquecode)));
                                    modal_menuItem_settings.setNetweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.netweight)));
                                    modal_menuItem_settings.setPortionsize(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.portionsize)));
                                    modal_menuItem_settings.setPricetypeforpos(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.pricetypeforpos)));
                                    modal_menuItem_settings.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcctgykey)));
                                    modal_menuItem_settings.setTmcpriceperkg(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg)));
                                    modal_menuItem_settings.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                                    modal_menuItem_settings.setTmcsubctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcsubctgykey)));
                                    modal_menuItem_settings.setVendorkey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey)));
                                    modal_menuItem_settings.setVendorname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorname)));
                                    modal_menuItem_settings.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                                    modal_menuItem_settings.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                                    modal_menuItem_settings.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                                    modal_menuItem_settings.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                                    modal_menuItem_settings.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                                    modal_menuItem_settings.setSwiggyprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.swiggyprice)));
                                    modal_menuItem_settings.setDunzoprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.dunzoprice)));
                                    modal_menuItem_settings.setBigbasketprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.bigbasketprice)));
                                    modal_menuItem_settings.setWholesaleprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.wholesaleprice)));
                                    modal_menuItem_settings.setAppmarkuppercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)));
                                    modal_menuItem_settings.setTmcpriceperkgWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkgWithMarkupValue)));
                                    modal_menuItem_settings.setTmcpriceWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceWithMarkupValue)));
                                    modal_menuItem_settings.setKey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                                    modal_menuItem_settings.setInventorydetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.inventoryDetails)));
                                    modal_menuItem_settings.setGrossweightingrams(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweightingrams)));


                                    if (!isinventorycheck) {

                                        String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                                receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                                        modal_menuItem_settings.setBarcode_AvlDetails(barcode_AvlDetails);
                                        modal_menuItem_settings.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                        modal_menuItem_settings.setKey_AvlDetails(key_AvlDetails);
                                        modal_menuItem_settings.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                        modal_menuItem_settings.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                        modal_menuItem_settings.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                        modal_menuItem_settings.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                        modal_menuItem_settings.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                        modal_menuItem_settings.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                        modal_menuItem_settings.setAllownegativestock(allownegativestock_AvlDetails);


                                    } else {

                                        modal_menuItem_settings.setBarcode_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode_AvlDetails)));
                                        modal_menuItem_settings.setItemavailability_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability_AvlDetails)));
                                        modal_menuItem_settings.setKey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.key_AvlDetails)));
                                        modal_menuItem_settings.setLastupdatedtime_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.lastupdatedtime_AvlDetails)));
                                        modal_menuItem_settings.setMenuitemkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId_AvlDetails)));
                                        modal_menuItem_settings.setReceivedstock_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.receivedStock_AvlDetails)));
                                        modal_menuItem_settings.setStockbalance_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockBalance_AvlDetails)));
                                        modal_menuItem_settings.setStockincomingkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockIncomingKey_AvlDetails)));
                                        modal_menuItem_settings.setVendorkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey_AvlDetails)));
                                        modal_menuItem_settings.setAllownegativestock(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.allowNegativeStock_AvlDetails)));


                                    }

                                    MenuItem.add(modal_menuItem_settings);
                                }
                                while (cursor.moveToNext());

                                if (MenuItem.size() > 0) {
                                    getMenuItemStockAvlDetails();
                                } else {
                                    Toast.makeText(getApplicationContext(), "There is no MenuItem Please Restart the APP", Toast.LENGTH_LONG).show();

                                }
                            }


                        } else {
                            Toast.makeText(MenuItemAvailabilityStatusReport.this, "There is no menuItem Please Refresh the App", Toast.LENGTH_SHORT).show();

                        }


                        //  }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            else if (methodToCall.equals("FetchSubCtgyItem")) {

                if (tmcSubCtgyItemSQL_db_manager == null) {

                    tmcSubCtgyItemSQL_db_manager = new TMCSubCtgyItemSQL_DB_Manager(this);
                    try {
                        tmcSubCtgyItemSQL_db_manager.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                try {
                    Cursor cursor = tmcSubCtgyItemSQL_db_manager.Fetch();
                    try {
                        if (cursor.getCount() > 0) {

                            if (cursor.moveToFirst()) {
                                do {
                                    Modal_SubCtgyList modal_subCtgyList = new Modal_SubCtgyList();
                                    modal_subCtgyList.setKey(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.key)));
                                    modal_subCtgyList.setTmcctgyname(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.tmcctgyname)));
                                    modal_subCtgyList.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.tmcctgykey)));
                                    modal_subCtgyList.setSubCtgyName(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.subctgyname)));
                                    modal_subCtgyList.setDisplayNo(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.displayno)));
                                    modal_subCtgyList.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.localDB_id)));

                                    subCtgyName_arrayList.add(modal_subCtgyList);
                                }
                                while (cursor.moveToNext());

                                AddDatatoHashmap(subCtgyName_arrayList);

                            }
                        } else {
                            getMenuCategoryList();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(tmcMenuItemSQL_db_manager != null){
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }
                if(tmcSubCtgyItemSQL_db_manager != null){
                    tmcSubCtgyItemSQL_db_manager.close();
                    tmcSubCtgyItemSQL_db_manager = null;
                }


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }




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



        String Api_to_Call = "";
        if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)) {
            Api_to_Call = Constants.api_getListofTMCTileForVendorkey+vendorkey;
        }
        else{
            Api_to_Call = Constants.api_getListofSubCtgy;

        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_to_Call,
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
                                String ctgyname = "" , ctgykey ="",key ="",subctgyname ="",displayNo="";

                                if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)){
                                    ctgyname =  String.valueOf(json.get("tmcctgyname"));
                                    ctgykey = String.valueOf(json.get("tmcctgykey"));
                                    key = String.valueOf(json.get("tmcsubctgykey"));
                                    subctgyname = String.valueOf(json.get("tmcsubctgyname"));
                                    displayNo = String.valueOf(json.get("displayno"));
                                }
                                else{
                                    ctgyname =  String.valueOf(json.get("tmcctgyname"));
                                    ctgykey = String.valueOf(json.get("tmcctgykey"));
                                    key = String.valueOf(json.get("key"));
                                    subctgyname = String.valueOf(json.get("subctgyname"));
                                    displayNo = String.valueOf(json.get("displayno"));
                                }
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
                                modal_subCtgyList.setTmcctgyname(ctgyname);
                                modal_subCtgyList.setTmcctgykey(ctgykey);
                                modal_subCtgyList.setDisplayNo(displayNo);


                                if(localDBcheck) {

                                    try {


                                        if (tmcSubCtgyItemSQL_db_manager == null) {

                                            tmcSubCtgyItemSQL_db_manager = new TMCSubCtgyItemSQL_DB_Manager(MenuItemAvailabilityStatusReport.this);
                                            try {
                                                tmcSubCtgyItemSQL_db_manager.open();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }


                                        try {
                                            if (tmcSubCtgyItemSQL_db_manager.deleteTable(true) >= 0) {
                                                tmcSubCtgyItemSQL_db_manager.open();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        long id = tmcSubCtgyItemSQL_db_manager.insert(modal_subCtgyList);


                                        modal_subCtgyList.setLocalDB_id(String.valueOf(id));


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        try {
                                            if (jArray.length() - i == 1) {
                                                tmcSubCtgyItemSQL_db_manager = null;
                                                tmcSubCtgyItemSQL_db_manager.close();

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }


                                if (!subCtgyName_arrayList.contains(modal_subCtgyList)) {
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
                                if(localDBcheck) {
                                    getDataFromSQL("FetchSubCtgyItem");
                                }
                                else{
                                    final SharedPreferences sharedPreferences = getSharedPreferences("MenuList", MODE_PRIVATE);

                                    Gson gson = new Gson();
                                    String json = gson.toJson(MenuItem);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("MenuList", json);
                                    editor.apply();


                                }
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

        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



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