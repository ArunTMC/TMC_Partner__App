package com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Pos_ManageOrderFragment;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Modal_MenuItem;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.NewOrders_MenuItem_Fragment;
import com.meatchop.tmcpartner.Settings.SettingsFragment;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.meatchop.tmcpartner.Constants.TAG;

public class MobileScreen_Dashboard extends AppCompatActivity {
    Fragment mfragment;
    BottomNavigationView bottomNavigationView;
    LinearLayout loadingPanel,loadingpanelmask;
    Fragment  CurrentFragment;
    Mobile_ManageOrders1 mobile_manageOrders1;
    NewOrders_MenuItem_Fragment newOrders_menuItem_fragment;
    List<Modal_MenuItem> MenuList=new ArrayList<>();
    List<Modal_MenuItem> MarinadeMenuList=new ArrayList<>();

    boolean isMenuListSavedLocally = false;
    String MenuItemKey,vendorKey;

    public static String completemenuItem="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_screen__dashboard);
        loadMyFragment(new Mobile_ManageOrders1());
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        bottomNavigationView = findViewById(R.id.bottomnav);
        Adjusting_Widgets_Visibility(true);

        newOrders_menuItem_fragment = new NewOrders_MenuItem_Fragment();
        mobile_manageOrders1  = new Mobile_ManageOrders1();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
                    vendorKey = (shared.getString("VendorKey", "vendor_1"));
                    completemenuItem = getMenuItemusingStoreId(vendorKey);
                    getMarinadeMenuItemusingStoreId(vendorKey);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.manage_order_navigatioBar_widget:
                    try{
                        Adjusting_Widgets_Visibility(true);

                        mfragment = new Mobile_ManageOrders1();
                       // Toast.makeText(MobileScreen_Dashboard.this,"Clicked on Manage Orders Button",Toast.LENGTH_LONG).show();

                        Adjusting_Widgets_Visibility(false);

                        loadMyFragment(mfragment);
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                        break;
                    case R.id.new_order_navigatioBar_widget:
                        try{

                            Toast.makeText(MobileScreen_Dashboard.this,"Clicked on New Orders Button",Toast.LENGTH_LONG).show();

                           /* if(!completemenuItem.isEmpty()) {
                                mfragment = new NewOrders_MenuItem_Fragment();
                                //loadMyFragment(mfragment);
                                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                                transaction1.replace(R.id.frame, NewOrders_MenuItem_Fragment.newInstance(completemenuItem));
                                transaction1.commit();


                                Toast.makeText(MobileScreen_Dashboard.this,"Clicked on New Orders Button",Toast.LENGTH_LONG).show();

                            }
                            else{
                                Toast.makeText(MobileScreen_Dashboard.this,"MenuItem is Not Loaded",Toast.LENGTH_LONG).show();
                                //Toast.makeText(Pos_Dashboard_Screen.this,"Check for Internet connection",Toast.LENGTH_LONG).show();

                            }

                            */
                        }catch(Exception e){
                            e.printStackTrace();
                        }



                        break;

                    case R.id.settings_navigatioBar_widget:
                        try{
                            if (isMenuListSavedLocally) {
                                mfragment = new SettingsFragment();
                                //loadMyFragment(mfragment);
                                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction()                    .addToBackStack(null)
                                        .addToBackStack(null);
                                transaction2 .replace(R.id.frame,  new SettingsFragment());

                                transaction2.commit();
                        //        Toast.makeText(MobileScreen_Dashboard.this,"Clicked on Settings Button",Toast.LENGTH_LONG).show();

                            }
                            else{
                                Toast.makeText(MobileScreen_Dashboard.this,"MenuItem is Not Loaded",Toast.LENGTH_LONG).show();
                                //Toast.makeText(Pos_Dashboard_Screen.this,"Check for Internet connection",Toast.LENGTH_LONG).show();

                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        break;

                }
                return true;
            }
        });


    }


    private String getMenuItemusingStoreId(String vendorKey) {
        Log.d(TAG, "starting:getfullMenuItemUsingStoreID ");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItems,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{
                    Log.d(TAG, "starting:onResponse ");

                    Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());

                    completemenuItem = new String(String.valueOf(response));
                    try {
                        JSONArray JArray = response.getJSONArray("content");
                        Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                        for (; i1 < (arrayLength); i1++) {

                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_MenuItem modal_menuItem = new Modal_MenuItem();
                                if(json.has("key")){
                                    modal_menuItem.key = String.valueOf(json.get("key"));
                                    MenuItemKey = String.valueOf(json.get("key"));
                                }
                                else{
                                    modal_menuItem.key = "";
                                    Log.d(Constants.TAG, "There is no key for this Menu: " );


                                }

                                if(json.has("applieddiscountpercentage")){
                                    modal_menuItem.applieddiscountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                                }
                                else{
                                    modal_menuItem.applieddiscountpercentage = "";
                                    Log.d(Constants.TAG, "There is no applieddiscountpercentage for this Menu: " +MenuItemKey );


                                }
                                if(json.has("barcode")){
                                    modal_menuItem.barcode = String.valueOf(json.get("barcode"));

                                }
                                else{
                                    modal_menuItem.barcode = "";
                                    Log.d(Constants.TAG, "There is no barcode for this Menu: " +MenuItemKey );


                                }

                                if(json.has("displayno")){
                                    modal_menuItem.displayno = String.valueOf(json.get("displayno"));

                                }
                                else{
                                    modal_menuItem.displayno = "";
                                    Log.d(Constants.TAG, "There is no displayno for this Menu: " +MenuItemKey );


                                }
                                if(json.has("gstpercentage")){
                                    modal_menuItem.gstpercentage = String.valueOf(json.get("gstpercentage"));

                                }
                                else{
                                    modal_menuItem.gstpercentage = "";
                                    Log.d(Constants.TAG, "There is no gstpercentage for this Menu: " +MenuItemKey );


                                }
                                if(json.has("itemavailability")){
                                    modal_menuItem.itemavailability = String.valueOf(json.get("itemavailability"));

                                }
                                else{
                                    modal_menuItem.itemavailability = "";
                                    Log.d(Constants.TAG, "There is no itemavailability for this Menu: " +MenuItemKey );


                                }
                                if(json.has("itemname")){
                                    modal_menuItem.itemname = String.valueOf(json.get("itemname"));

                                }
                                else{
                                    modal_menuItem.itemname = "";
                                    Log.d(Constants.TAG, "There is no ItemName for this Menu: " +MenuItemKey );


                                }


                                if(json.has("pricetypeforpos")){
                                    modal_menuItem.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                                }
                                else{
                                    modal_menuItem.pricetypeforpos = "";
                                    Log.d(Constants.TAG, "There is no pricetypeforpos for this Menu: " +MenuItemKey );


                                }


                                if(json.has("itemuniquecode")){
                                    modal_menuItem.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                                }
                                else{
                                    modal_menuItem.itemuniquecode = "";
                                    Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " +MenuItemKey );


                                }

                                if(json.has("menuboarddisplayname")){
                                    modal_menuItem.menuboarddisplayname = String.valueOf(json.get("menuboarddisplayname"));

                                }
                                else{
                                    modal_menuItem.menuboarddisplayname = "";
                                    Log.d(Constants.TAG, "There is no menuboarddisplayname for this Menu: " +MenuItemKey );


                                }

                                if(json.has("showinmenuboard")){
                                    modal_menuItem.showinmenuboard = String.valueOf(json.get("showinmenuboard"));

                                }
                                else{
                                    modal_menuItem.showinmenuboard = "";
                                    Log.d(Constants.TAG, "There is no showinmenuboard for this Menu: " +MenuItemKey );


                                }

                                if(json.has("grossweight")){
                                    modal_menuItem.grossweight = String.valueOf(json.get("grossweight"));

                                }
                                else{
                                    modal_menuItem.grossweight = "";
                                    Log.d(Constants.TAG, "There is no grossweight for this Menu: " +MenuItemKey );


                                }

                                if(json.has("grossweightingrams")){
                                    modal_menuItem.grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                                }
                                else{
                                    modal_menuItem.grossweightingrams = "";
                                    Log.d(Constants.TAG, "There is no grossweightingrams for this Menu: " +MenuItemKey );


                                }


                                if(json.has("netweight")){
                                    modal_menuItem.netweight= String.valueOf(json.get("netweight"));

                                }
                                else{
                                    modal_menuItem.netweight = "";
                                    Log.d(Constants.TAG, "There is no netweight for this Menu: "  );


                                }    if(json.has("portionsize")){
                                    modal_menuItem.portionsize= String.valueOf(json.get("portionsize"));

                                }
                                else{
                                    modal_menuItem.portionsize = "";
                                    Log.d(Constants.TAG, "There is no portionsize for this Menu: "  );


                                }



                                if(json.has("tmcctgykey")){
                                    modal_menuItem.tmcctgykey = String.valueOf(json.get("tmcctgykey"));

                                }
                                else{
                                    modal_menuItem.tmcctgykey = "";
                                    Log.d(Constants.TAG, "There is no tmcctgykey for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcprice")){
                                    modal_menuItem.tmcprice = String.valueOf(json.get("tmcprice"));

                                }
                                else{
                                    modal_menuItem.tmcprice = "";
                                    Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcpriceperkg")){
                                    modal_menuItem.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));

                                }
                                else{
                                    modal_menuItem.tmcpriceperkg = "";
                                    Log.d(Constants.TAG, "There is no tmcpriceperkg for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcsubctgykey")){
                                    modal_menuItem.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));

                                }
                                else{
                                    modal_menuItem.tmcsubctgykey = "";
                                    Log.d(Constants.TAG, "There is no tmcsubctgykey for this Menu: " +MenuItemKey );


                                }

                                if(json.has("key")){
                                    modal_menuItem.menuItemId= String.valueOf(json.get("key"));

                                }
                                else{
                                    modal_menuItem.menuItemId = "";
                                    Log.d(Constants.TAG, "There is no key for this Menu: "  );


                                }





                                MenuList.add(modal_menuItem);

                                Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                Log.d(Constants.TAG, "e: " + e.getMessage());
                                Log.d(Constants.TAG, "e: " + e.toString());

                            }


                        }

                        Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    saveMenuIteminSharedPreference(MenuList);

                    Log.d(TAG, "sending :Response "+completemenuItem);







                    Log.d(TAG, "MenuItems: " + completemenuItem.toString());



                }catch(Exception e){
                    e.printStackTrace();
                }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(TAG, "Error: " + error.getLocalizedMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.toString());
              Toast.makeText(MobileScreen_Dashboard.this,"Error in Getting Menu Item ",Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid",vendorKey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);

        return completemenuItem;
    }






    private void getMarinadeMenuItemusingStoreId(String vendorKey) {
        Log.d(TAG, "starting:getfullMenuItemUsingStoreID ");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMarinadeMenuItems,
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

                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_MenuItem modal_menuItem = new Modal_MenuItem();
                                if(json.has("key")){
                                    modal_menuItem.key = String.valueOf(json.get("key"));
                                    MenuItemKey = String.valueOf(json.get("key"));
                                }
                                else{
                                    modal_menuItem.key = "";
                                    Log.d(Constants.TAG, "There is no key for this Menu: " );


                                }

                                if(json.has("applieddiscountpercentage")){
                                    modal_menuItem.applieddiscountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                                }
                                else{
                                    modal_menuItem.applieddiscountpercentage = "";
                                    Log.d(Constants.TAG, "There is no applieddiscountpercentage for this Menu: " +MenuItemKey );


                                }
                                if(json.has("barcode")){
                                    modal_menuItem.barcode = String.valueOf(json.get("barcode"));

                                }
                                else{
                                    modal_menuItem.barcode = "";
                                    Log.d(Constants.TAG, "There is no barcode for this Menu: " +MenuItemKey );


                                }

                                if(json.has("displayno")){
                                    modal_menuItem.displayno = String.valueOf(json.get("displayno"));

                                }
                                else{
                                    modal_menuItem.displayno = "";
                                    Log.d(Constants.TAG, "There is no displayno for this Menu: " +MenuItemKey );


                                }
                                if(json.has("gstpercentage")){
                                    modal_menuItem.gstpercentage = String.valueOf(json.get("gstpercentage"));

                                }
                                else{
                                    modal_menuItem.gstpercentage = "";
                                    Log.d(Constants.TAG, "There is no gstpercentage for this Menu: " +MenuItemKey );


                                }
                                if(json.has("itemavailability")){
                                    modal_menuItem.itemavailability = String.valueOf(json.get("itemavailability"));

                                }
                                else{
                                    modal_menuItem.itemavailability = "";
                                    Log.d(Constants.TAG, "There is no itemavailability for this Menu: " +MenuItemKey );


                                }
                                if(json.has("itemname")){
                                    modal_menuItem.itemname = String.valueOf(json.get("itemname"));

                                }
                                else{
                                    modal_menuItem.itemname = "";
                                    Log.d(Constants.TAG, "There is no ItemName for this Menu: " +MenuItemKey );


                                }


                                if(json.has("pricetypeforpos")){
                                    modal_menuItem.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                                }
                                else{
                                    modal_menuItem.pricetypeforpos = "";
                                    Log.d(Constants.TAG, "There is no pricetypeforpos for this Menu: " +MenuItemKey );


                                }


                                if(json.has("itemuniquecode")){
                                    modal_menuItem.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                                }
                                else{
                                    modal_menuItem.itemuniquecode = "";
                                    Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " +MenuItemKey );


                                }

                                if(json.has("menuboarddisplayname")){
                                    modal_menuItem.menuboarddisplayname = String.valueOf(json.get("menuboarddisplayname"));

                                }
                                else{
                                    modal_menuItem.menuboarddisplayname = "";
                                    Log.d(Constants.TAG, "There is no menuboarddisplayname for this Menu: " +MenuItemKey );


                                }

                                if(json.has("showinmenuboard")){
                                    modal_menuItem.showinmenuboard = String.valueOf(json.get("showinmenuboard"));

                                }
                                else{
                                    modal_menuItem.showinmenuboard = "";
                                    Log.d(Constants.TAG, "There is no showinmenuboard for this Menu: " +MenuItemKey );


                                }

                                if(json.has("grossweight")){
                                    modal_menuItem.grossweight = String.valueOf(json.get("grossweight"));

                                }
                                else{
                                    modal_menuItem.grossweight = "";
                                    Log.d(Constants.TAG, "There is no grossweight for this Menu: " +MenuItemKey );


                                }

                                if(json.has("grossweightingrams")){
                                    modal_menuItem.grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                                }
                                else{
                                    modal_menuItem.grossweightingrams = "";
                                    Log.d(Constants.TAG, "There is no grossweightingrams for this Menu: " +MenuItemKey );


                                }


                                if(json.has("netweight")){
                                    modal_menuItem.netweight= String.valueOf(json.get("netweight"));

                                }
                                else{
                                    modal_menuItem.netweight = "";
                                    Log.d(Constants.TAG, "There is no netweight for this Menu: "  );


                                }    if(json.has("portionsize")){
                                    modal_menuItem.portionsize= String.valueOf(json.get("portionsize"));

                                }
                                else{
                                    modal_menuItem.portionsize = "";
                                    Log.d(Constants.TAG, "There is no portionsize for this Menu: "  );


                                }



                                if(json.has("tmcctgykey")){
                                    modal_menuItem.tmcctgykey = String.valueOf(json.get("tmcctgykey"));

                                }
                                else{
                                    modal_menuItem.tmcctgykey = "";
                                    Log.d(Constants.TAG, "There is no tmcctgykey for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcprice")){
                                    modal_menuItem.tmcprice = String.valueOf(json.get("tmcprice"));

                                }
                                else{
                                    modal_menuItem.tmcprice = "";
                                    Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcpriceperkg")){
                                    modal_menuItem.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));

                                }
                                else{
                                    modal_menuItem.tmcpriceperkg = "";
                                    Log.d(Constants.TAG, "There is no tmcpriceperkg for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcsubctgykey")){
                                    modal_menuItem.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));

                                }
                                else{
                                    modal_menuItem.tmcsubctgykey = "";
                                    Log.d(Constants.TAG, "There is no tmcsubctgykey for this Menu: " +MenuItemKey );


                                }

                                if(json.has("key")){
                                    modal_menuItem.menuItemId= String.valueOf(json.get("key"));

                                }
                                else{
                                    modal_menuItem.menuItemId = "";
                                    Log.d(Constants.TAG, "There is no key for this Menu: "  );


                                }





                                MarinadeMenuList.add(modal_menuItem);

                                Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MarinadeMenuList);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                Log.d(Constants.TAG, "e: " + e.getMessage());
                                Log.d(Constants.TAG, "e: " + e.toString());

                            }


                        }

                        Log.d(Constants.TAG, "convertingJsonStringintoArray MarinademenuListFull: " + MarinadeMenuList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    saveMarinadeMenuIteminSharedPreference(MarinadeMenuList);





                }catch(Exception e){
                    e.printStackTrace();
                }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(TAG, "Error: " + error.getLocalizedMessage());
                Log.d(TAG, "Error: " + error.getMessage());
                Log.d(TAG, "Error: " + error.toString());
                Toast.makeText(MobileScreen_Dashboard.this,"Error in Getting Marinade Menu Item ",Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid",vendorKey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);

    }





    private void saveMarinadeMenuIteminSharedPreference(List<Modal_MenuItem> menuList) {
        try {
            final SharedPreferences sharedPreferences = getSharedPreferences("MarinadeMenuList", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(menuList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("MarinadeMenuList", json);
            editor.apply();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private void saveMenuIteminSharedPreference(List<Modal_MenuItem> menuList) {
        try {
            final SharedPreferences sharedPreferences = getSharedPreferences("MenuList", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(menuList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("MenuList", json);
            editor.apply();
            isMenuListSavedLocally = true;
          //  loadMyFragment(new Mobile_ManageOrders1());
            bottomNavigationView.setSelectedItemId(R.id.manage_order_navigatioBar_widget);

           /* loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);

            */
            Adjusting_Widgets_Visibility(false);

            CurrentFragment = new Mobile_ManageOrders1();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.GONE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            //bottomNavigationView.setVisibility(View.VISIBLE);

        }

    }

    private void loadMyFragment(Fragment fm) {if (fm != null) {
        try{
            SharedPreferences sharedPreferences
                    = getSharedPreferences("CurrentSelectedStatus",
                    MODE_PRIVATE);

            SharedPreferences.Editor myEdit
                    = sharedPreferences.edit();


            myEdit.putString(
                    "currentstatus",
                    Constants.NEW_ORDER_STATUS);
            myEdit.apply();
            CurrentFragment = fm;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame, fm)
                    .addToBackStack(null)
                    .commit();

        }catch(Exception e){
            e.printStackTrace();
        }

    }
    }

    @Override
    public void onBackPressed() {

        new TMCAlertDialogClass(this, R.string.app_name, R.string.Exit_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                            finish();


                    }

                    @Override
                    public void onNo() {

                    }
                });

        }




        /*
        getSupportFragmentManager().popBackStack();


         */
   // }

}