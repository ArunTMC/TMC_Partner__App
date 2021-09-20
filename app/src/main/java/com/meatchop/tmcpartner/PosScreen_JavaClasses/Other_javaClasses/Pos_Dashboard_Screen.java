package com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Pos_ManageOrderFragment;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.NewOrders_MenuItem_Fragment;
import com.meatchop.tmcpartner.Settings.SettingsFragment;
import com.meatchop.tmcpartner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meatchop.tmcpartner.R.id.manage_order_navigatioBar_widget;
import static com.meatchop.tmcpartner.R.id.new_order_navigatioBar_widget;
import static com.meatchop.tmcpartner.R.id.settings_navigatioBar_widget;


public class Pos_Dashboard_Screen extends AppCompatActivity implements OnNavigationItemSelectedListener {
    Fragment mfragment;
    BottomNavigationView bottomNavigationView;
    NewOrders_MenuItem_Fragment newOrders_menuItem_fragment;
    SettingsFragment settingsFragment;
    public static String completemenuItem="";
    public static String completeMarinademenuItem="";

    LinearLayout loadingPanel,loadingpanelmask;
    int gettingMenuItemRetryCount = 5;
    String vendorkey;
    String MenuItemKey,UserRole;
    List<Modal_MenuItem> MarinadeMenuList=new ArrayList<>();
    String errorCode = "0";
    Dialog dialog ;
    Button restartAgain;
    TextView title;
    List<Modal_MenuItem> MenuList=new ArrayList<>();
    boolean isMenuListSavedLocally = false;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos__dashboard__screen_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
//
        SharedPreferences shared =getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));

        bottomNavigationView = findViewById(R.id.bottomnav);
        dialog = new Dialog(Pos_Dashboard_Screen.this);

        dialog.setContentView(R.layout.print_again);
        dialog.setTitle("Poor Internet Connection . Please Try Again !!!! ");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

         restartAgain = (Button) dialog.findViewById(R.id.printAgain);
         title = (TextView) dialog.findViewById(R.id.title);

        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        bottomNavigationView.setOnNavigationItemSelectedListener(Pos_Dashboard_Screen.this);
        Adjusting_Widgets_Visibility();
        checkForInternetConnectionAndGetMenuItemAndMobileAppData();



        newOrders_menuItem_fragment = new NewOrders_MenuItem_Fragment();
        settingsFragment = new SettingsFragment();

    }

    private void checkForInternetConnectionAndGetMenuItemAndMobileAppData() {
        try {
            if (isConnected()) {
                //Toast.makeText(getApplicationContext(), "Internet Connected", Toast.LENGTH_SHORT).show();
                completemenuItem = getMenuItemusingStoreId(vendorkey);
                completeMarinademenuItem =  getMarinadeMenuItemusingStoreId(vendorkey);
                getDatafromMobileApp();
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(dialog.isShowing()){

                            }
                            else {
                                title.setText("Poor Internet Connection .Please Try Again !!!! ");
                                restartAgain.setText("Click to Retry !!!");

                                restartAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();

                                        checkForInternetConnectionAndGetMenuItemAndMobileAppData();

                                    }
                                });


                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }




    }

    private void getDatafromMobileApp() {

        SharedPreferences preferences =getSharedPreferences("RedeemData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        SharedPreferences preferencess =getSharedPreferences("PartnerAppAccessDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editorr = preferencess.edit();
        editorr.clear();
        editorr.apply();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetMobileAppData, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try{


                        SharedPreferences preferences =getSharedPreferences("RedeemData",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        SharedPreferences preferencess =getSharedPreferences("PartnerAppAccessDetails",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorr = preferencess.edit();
                        editorr.clear();
                        editorr.apply();

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        try {

                            Log.d(Constants.TAG, " response: " + response);
                            try {
                                String jsonString =response.toString();
                                Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray JArray  = jsonObject.getJSONArray("content");
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);

                                        JSONArray array  = json.getJSONArray("redeemdata ");

                                        for(int i=0; i < array.length(); i++) {
                                            JSONObject redeemdata_json = array.getJSONObject(i);
                                            String maxpointsinaday = redeemdata_json.getString("maxpointsinaday");
                                            String minordervalueforredeem = redeemdata_json.getString("minordervalueforredeem");
                                            String pointsfor100rs = redeemdata_json.getString("pointsfor100rs");
                                            Log.d("Constants.TAG", "maxpointsinaday Response: " + maxpointsinaday);
                                            Log.d("Constants.TAG", "minordervalueforredeem Response: " + minordervalueforredeem);
                                            Log.d("Constants.TAG", "pointsfor100rs Response: " + pointsfor100rs);
                                            saveredeemDetailsinSharePreferences(maxpointsinaday,minordervalueforredeem,pointsfor100rs);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }



                                    try{
                                        JSONObject json = JArray.getJSONObject(i1);

                                        JSONArray array  = json.getJSONArray("partnerappacessdetails");

                                        for(int i=0; i < array.length(); i++) {
                                            JSONObject parrtnerappacessdetails_data_json = array.getJSONObject(i);
                                            String admin_role = parrtnerappacessdetails_data_json.getString("admin");
                                            String cashier_role = parrtnerappacessdetails_data_json.getString("cashier");
                                            String deliverymanager_role = parrtnerappacessdetails_data_json.getString("deliverymanager");
                                            String reportsviewer_role = parrtnerappacessdetails_data_json.getString("reportsviewer");
                                            String storemanager_role = parrtnerappacessdetails_data_json.getString("storemanager");
                                            Log.d("Constants.TAG", "admin_role Response: " + admin_role);
                                            Log.d("Constants.TAG", "cashier_role Response: " + cashier_role);
                                            Log.d("Constants.TAG", "deliverymanager_role Response: " + deliverymanager_role);
                                            Log.d("Constants.TAG", "reportsviewer_role Response: " + reportsviewer_role);
                                            Log.d("Constants.TAG", "storemanager_role Response: " + storemanager_role);

                                            savepartnerappacessdetailsinSharePreferences(admin_role,cashier_role,deliverymanager_role,reportsviewer_role,storemanager_role);
                                            //   AlertDialogClass.showDialog(MobileScreen_Dashboard.this, Constants.Order_Value_should_be_above+" "+minordervalueforredeem+" rs",0);

                                        }
                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                SharedPreferences preferences =getSharedPreferences("RedeemData",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                SharedPreferences preferencess =getSharedPreferences("PartnerAppAccessDetails",Context.MODE_PRIVATE);
                SharedPreferences.Editor editorr = preferencess.edit();
                editorr.clear();
                editorr.apply();

                if (error instanceof TimeoutError) {
                    errorCode = "Time Out Error";
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
                Toast.makeText(Pos_Dashboard_Screen.this,"Error in General App Data code :  "+errorCode,Toast.LENGTH_LONG).show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(dialog.isShowing()){

                            }
                            else {
                                title.setText(new StringBuilder().append("").append(errorCode).append(" .  Please Try Again !!!! ").toString());
                                restartAgain.setText("Click to Retry !!!");

                                restartAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                        checkForInternetConnectionAndGetMenuItemAndMobileAppData();


                                    }
                                });


                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Mobile");
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
        Volley.newRequestQueue(Pos_Dashboard_Screen.this).add(jsonObjectRequest);





    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void Adjusting_Widgets_Visibility() {
        loadingPanel.setVisibility(View.VISIBLE);
        loadingpanelmask.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.GONE);

    }


    private void loadMyFragment(Fragment fm) {if (fm != null) {
try {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.frame, fm);
    transaction.commit();
}
catch (Exception e){
    e.printStackTrace();
}
    }
    }



    private String getMenuItemusingStoreId(String vendorkey) {
        completemenuItem="";
        MenuList.clear();
        SharedPreferences preferences =getSharedPreferences("MenuList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        //Log.d(TAG, "starting:getfullMenuItemUsingStoreID ");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItems+"?storeid="+vendorkey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
           try {
               try{


               gettingMenuItemRetryCount = 0;
               completemenuItem="";
               MenuList.clear();
               SharedPreferences preferences =getSharedPreferences("MenuList",Context.MODE_PRIVATE);
               SharedPreferences.Editor editor = preferences.edit();
               editor.clear();
               editor.apply();

               }
               catch (Exception e){
                   e.printStackTrace();
               }
               //Log.d(TAG, "gettingMenuItemRetryCount: " + gettingMenuItemRetryCount);

               //Log.d(TAG, "starting:onResponse ");

               //Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());

               completemenuItem = new String(String.valueOf(response));
               try {
                   JSONArray JArray = response.getJSONArray("content");
                   //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                   int i1 = 0;
                   int arrayLength = JArray.length();
                   //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


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
                               //Log.d(Constants.TAG, "There is no key for this Menu: " );


                           }

                           if(json.has("applieddiscountpercentage")){
                               modal_menuItem.applieddiscountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                           }
                           else{
                               modal_menuItem.applieddiscountpercentage = "";
                               //Log.d(Constants.TAG, "There is no applieddiscountpercentage for this Menu: " +MenuItemKey );


                           }
                           if(json.has("barcode")){
                               modal_menuItem.barcode = String.valueOf(json.get("barcode"));

                           }
                           else{
                               modal_menuItem.barcode = "";
                               //Log.d(Constants.TAG, "There is no barcode for this Menu: " +MenuItemKey );


                           }
                           if(json.has("swiggyprice")){
                               modal_menuItem.swiggyprice = String.valueOf(json.get("swiggyprice"));
                               if(String.valueOf(json.get("swiggyprice")).equals("")){
                                   modal_menuItem.swiggyprice = "0";

                               }else  if(String.valueOf(json.get("swiggyprice")).equals("\r")) {

                                   modal_menuItem.dunzoprice = "0";

                               }

                           }
                           else{
                               modal_menuItem.swiggyprice = "0";
                               Log.d(Constants.TAG, "There is no swiggyprice for this Menu: " +MenuItemKey );


                           }


                           if(json.has("bigbasketprice")){
                               modal_menuItem.bigbasketprice = String.valueOf(json.get("bigbasketprice"));
                               if(String.valueOf(json.get("bigbasketprice")).equals("")){
                                   modal_menuItem.bigbasketprice = "0";

                               }else  if(String.valueOf(json.get("bigbasketprice")).equals("\r")) {

                                   modal_menuItem.dunzoprice = "0";

                               }

                           }
                           else{
                               modal_menuItem.bigbasketprice = "0";
                               Log.d(Constants.TAG, "There is no bigbasketprice for this Menu: " +MenuItemKey );


                           }


                           if(json.has("dunzoprice")){
                               modal_menuItem.dunzoprice= String.valueOf(json.get("dunzoprice"));
                               if(String.valueOf(json.get("dunzoprice")).equals("")){
                                   modal_menuItem.dunzoprice = "0";

                               }else  if(String.valueOf(json.get("dunzoprice")).equals("\r")) {

                                   modal_menuItem.dunzoprice = "0";

                               }
                           }
                           else{
                               modal_menuItem.dunzoprice = "0";
                               Log.d(Constants.TAG, "There is no dunzoprice for this Menu: " +MenuItemKey );


                           }




                           if(json.has("grossweightingrams")){
                               modal_menuItem.grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                           }
                           else{
                               modal_menuItem.grossweightingrams = "";
                               //Log.d(Constants.TAG, "There is no grossweightingrams for this Menu: " +MenuItemKey );


                           }




                           if(json.has("displayno")){
                               modal_menuItem.displayno = String.valueOf(json.get("displayno"));

                           }
                           else{
                               modal_menuItem.displayno = "";
                               //Log.d(Constants.TAG, "There is no displayno for this Menu: " +MenuItemKey );


                           }
                           if(json.has("gstpercentage")){
                               modal_menuItem.gstpercentage = String.valueOf(json.get("gstpercentage"));

                           }
                           else{
                               modal_menuItem.gstpercentage = "";
                               //Log.d(Constants.TAG, "There is no gstpercentage for this Menu: " +MenuItemKey );


                           }
                           if(json.has("itemavailability")){
                               modal_menuItem.itemavailability = String.valueOf(json.get("itemavailability"));

                           }
                           else{
                               modal_menuItem.itemavailability = "";
                               //Log.d(Constants.TAG, "There is no itemavailability for this Menu: " +MenuItemKey );


                           }
                           if(json.has("itemname")){
                               modal_menuItem.itemname = String.valueOf(json.get("itemname"));

                           }
                           else{
                               modal_menuItem.itemname = "";
                               //Log.d(Constants.TAG, "There is no ItemName for this Menu: " +MenuItemKey );


                           }

                           if(json.has("pricetypeforpos")){
                               modal_menuItem.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                           }
                           else{
                               modal_menuItem.pricetypeforpos = "";
                               //Log.d(Constants.TAG, "There is no pricetypeforpos for this Menu: " +MenuItemKey );


                           }



                           if(json.has("itemuniquecode")){
                               modal_menuItem.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                           }
                           else{
                               modal_menuItem.itemuniquecode = "";
                               //Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " +MenuItemKey );


                           }


                           if(json.has("reportname")){
                               modal_menuItem.reportname = String.valueOf(json.get("reportname"));

                           }
                           else{
                               modal_menuItem.reportname = "";
                               //Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " +MenuItemKey );


                           }

                           if(json.has("menuboarddisplayname")){
                               modal_menuItem.menuboarddisplayname = String.valueOf(json.get("menuboarddisplayname"));

                           }
                           else{
                               modal_menuItem.menuboarddisplayname = "";
                               //Log.d(Constants.TAG, "There is no menuboarddisplayname for this Menu: " +MenuItemKey );


                           }

                           if(json.has("showinmenuboard")){
                               modal_menuItem.showinmenuboard = String.valueOf(json.get("showinmenuboard"));

                           }
                           else{
                               modal_menuItem.showinmenuboard = "";
                               //Log.d(Constants.TAG, "There is no showinmenuboard for this Menu: " +MenuItemKey );


                           }
                           if(json.has("grossweight")){
                               modal_menuItem.grossweight = String.valueOf(json.get("grossweight"));

                           }
                           else{
                               modal_menuItem.grossweight = "";
                               //Log.d(Constants.TAG, "There is no grossweight for this Menu: " +MenuItemKey );


                           }
                           if(json.has("tmcctgykey")){
                               modal_menuItem.tmcctgykey = String.valueOf(json.get("tmcctgykey"));

                           }
                           else{
                               modal_menuItem.tmcctgykey = "";
                               //Log.d(Constants.TAG, "There is no tmcctgykey for this Menu: " +MenuItemKey );


                           }
                           if(json.has("tmcprice")){
                               modal_menuItem.tmcprice = String.valueOf(json.get("tmcprice"));

                           }
                           else{
                               modal_menuItem.tmcprice = "";
                               //Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );


                           }
                           if(json.has("tmcpriceperkg")){
                               modal_menuItem.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));

                           }
                           else{
                               modal_menuItem.tmcpriceperkg = "";
                               //Log.d(Constants.TAG, "There is no tmcpriceperkg for this Menu: " +MenuItemKey );


                           }
                           if(json.has("tmcsubctgykey")){
                               modal_menuItem.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));

                           }
                           else{
                               modal_menuItem.tmcsubctgykey = "";
                               //Log.d(Constants.TAG, "There is no tmcsubctgykey for this Menu: " +MenuItemKey );


                           }

                           if(json.has("key")){
                               modal_menuItem.menuItemId= String.valueOf(json.get("key"));

                           }
                           else{
                               modal_menuItem.menuItemId = "";
                               //Log.d(Constants.TAG, "There is no key for this Menu: "  );


                           }


                           if(json.has("netweight")){
                               modal_menuItem.netweight= String.valueOf(json.get("netweight"));

                           }
                           else{
                               modal_menuItem.netweight = "";
                               //Log.d(Constants.TAG, "There is no netweight for this Menu: "  );


                           }    if(json.has("portionsize")){
                               modal_menuItem.portionsize= String.valueOf(json.get("portionsize"));

                           }
                           else{
                               modal_menuItem.portionsize = "";
                               //Log.d(Constants.TAG, "There is no portionsize for this Menu: "  );


                           }




                           MenuList.add(modal_menuItem);

                           //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


                       } catch (JSONException e) {
                           e.printStackTrace();
                           //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                           //Log.d(Constants.TAG, "e: " + e.getMessage());
                           //Log.d(Constants.TAG, "e: " + e.toString());

                       }


                   }

                   //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


               } catch (JSONException e) {
                   e.printStackTrace();
               }
               saveMenuIteminSharedPreference(MenuList);


               //Log.d(TAG, "sending :Response " + completemenuItem);


               //Log.d(TAG, "MenuItems: " + completemenuItem.toString());
           }
           catch (Exception e){
               e.printStackTrace();
           }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                if (gettingMenuItemRetryCount>0){
                    gettingMenuItemRetryCount = gettingMenuItemRetryCount-1;
               //     getMenuItemusingStoreId();
                    //Log.d(TAG, "gettingMenuItemRetryCount: " + gettingMenuItemRetryCount);


                }
                //Log.d(TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(TAG, "Error: " + error.getMessage());
                //Log.d(TAG, "Error: " + error.toString());
                Log.d("RVA", "error:" + error);
                completemenuItem="";
                MenuList.clear();
                SharedPreferences preferences =getSharedPreferences("MenuList",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

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
                Toast.makeText(Pos_Dashboard_Screen.this,"Error in Getting  Menu Item error code :  "+errorCode,Toast.LENGTH_LONG).show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(dialog.isShowing()){

                            }
                            else {
                                title.setText(new StringBuilder().append("").append(errorCode).append(" .  Please Try Again !!!! ").toString());
                                restartAgain.setText("Click to Retry !!!");

                                restartAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SharedPreferences preferences =getSharedPreferences("MenuList",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.clear();
                                        editor.apply();


                                        dialog.cancel();
                                        checkForInternetConnectionAndGetMenuItemAndMobileAppData();


                                    }
                                });


                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });



                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid", vendorkey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(Pos_Dashboard_Screen.this).add(jsonObjectRequest);

        return completemenuItem;
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
           loadingpanelmask.setVisibility(View.GONE);
           loadingPanel.setVisibility(View.GONE);
           bottomNavigationView.setVisibility(View.VISIBLE);

               loadMyFragment(new Pos_ManageOrderFragment());



       }
       catch (Exception e){
           e.printStackTrace();
       }
    }


    private void saveredeemDetailsinSharePreferences(String maxpointsinaday, String minordervalueforredeem, String pointsfor100rs) {
        final SharedPreferences sharedPreferences = getSharedPreferences("RedeemData", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("maxpointsinaday", maxpointsinaday);
        editor.putString("minordervalueforredeem", minordervalueforredeem);
        editor.putString("pointsfor100rs", pointsfor100rs);
        editor.putBoolean("fetchedindashboard",true );

        editor.apply();





    }

    private void savepartnerappacessdetailsinSharePreferences(String admin_role, String cashier_role, String deliverymanager_role, String reportsviewer_role, String storemanager_role) {
        final SharedPreferences sharedPreferences = getSharedPreferences("PartnerAppAccessDetails", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.ADMIN_ROLENAME, admin_role);
        editor.putString(Constants.CASHIER_ROLENAME, cashier_role);
        editor.putString(Constants.DELIVERYMANAGER_ROLENAME, deliverymanager_role);

        editor.putString(Constants.REPORTSVIEWER_ROLENAME, reportsviewer_role);
        editor.putString(Constants.STOREMANAGER_ROLENAME,storemanager_role );
        editor.apply();


    }




    private String getMarinadeMenuItemusingStoreId(String vendorKey) {
        //Log.d(TAG, "starting:getfullMenuItemUsingStoreID ");
        completeMarinademenuItem="";
        MarinadeMenuList.clear();
        SharedPreferences preferences =getSharedPreferences("MarinadeMenuList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMarinadeMenuItems,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{
                    //Log.d(TAG, "starting:onResponse ");
                    completeMarinademenuItem="";
                    MarinadeMenuList.clear();
                    SharedPreferences preferences =getSharedPreferences("MarinadeMenuList",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    //Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());

                    try {
                        JSONArray JArray = response.getJSONArray("content");
                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


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
                                    //Log.d(Constants.TAG, "There is no key for this Menu: " );


                                }

                                if(json.has("applieddiscountpercentage")){
                                    modal_menuItem.applieddiscountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                                }
                                else{
                                    modal_menuItem.applieddiscountpercentage = "";
                                    //Log.d(Constants.TAG, "There is no applieddiscountpercentage for this Menu: " +MenuItemKey );


                                }
                                if(json.has("barcode")){
                                    modal_menuItem.barcode = String.valueOf(json.get("barcode"));

                                }
                                else{
                                    modal_menuItem.barcode = "";
                                    //Log.d(Constants.TAG, "There is no barcode for this Menu: " +MenuItemKey );


                                }

                                if(json.has("displayno")){
                                    modal_menuItem.displayno = String.valueOf(json.get("displayno"));

                                }
                                else{
                                    modal_menuItem.displayno = "";
                                    //Log.d(Constants.TAG, "There is no displayno for this Menu: " +MenuItemKey );


                                }
                                if(json.has("gstpercentage")){
                                    modal_menuItem.gstpercentage = String.valueOf(json.get("gstpercentage"));

                                }
                                else{
                                    modal_menuItem.gstpercentage = "";
                                    //Log.d(Constants.TAG, "There is no gstpercentage for this Menu: " +MenuItemKey );


                                }
                                if(json.has("itemavailability")){
                                    modal_menuItem.itemavailability = String.valueOf(json.get("itemavailability"));

                                }
                                else{
                                    modal_menuItem.itemavailability = "";
                                    //Log.d(Constants.TAG, "There is no itemavailability for this Menu: " +MenuItemKey );


                                }
                                if(json.has("itemname")){
                                    modal_menuItem.itemname = String.valueOf(json.get("itemname"));

                                }
                                else{
                                    modal_menuItem.itemname = "";
                                    //Log.d(Constants.TAG, "There is no ItemName for this Menu: " +MenuItemKey );


                                }


                                if(json.has("pricetypeforpos")){
                                    modal_menuItem.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                                }
                                else{
                                    modal_menuItem.pricetypeforpos = "";
                                    //Log.d(Constants.TAG, "There is no pricetypeforpos for this Menu: " +MenuItemKey );


                                }


                                if(json.has("itemuniquecode")){
                                    modal_menuItem.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                                }
                                else{
                                    modal_menuItem.itemuniquecode = "";
                                    //Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " +MenuItemKey );


                                }

                                if(json.has("menuboarddisplayname")){
                                    modal_menuItem.menuboarddisplayname = String.valueOf(json.get("menuboarddisplayname"));

                                }
                                else{
                                    modal_menuItem.menuboarddisplayname = "";
                                    //Log.d(Constants.TAG, "There is no menuboarddisplayname for this Menu: " +MenuItemKey );


                                }

                                if(json.has("showinmenuboard")){
                                    modal_menuItem.showinmenuboard = String.valueOf(json.get("showinmenuboard"));

                                }
                                else{
                                    modal_menuItem.showinmenuboard = "";
                                    //Log.d(Constants.TAG, "There is no showinmenuboard for this Menu: " +MenuItemKey );


                                }

                                if(json.has("grossweight")){
                                    modal_menuItem.grossweight = String.valueOf(json.get("grossweight"));

                                }
                                else{
                                    modal_menuItem.grossweight = "";
                                    //Log.d(Constants.TAG, "There is no grossweight for this Menu: " +MenuItemKey );


                                }

                                if(json.has("grossweightingrams")){
                                    modal_menuItem.grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                                }
                                else{
                                    modal_menuItem.grossweightingrams = "";
                                    //Log.d(Constants.TAG, "There is no grossweightingrams for this Menu: " +MenuItemKey );


                                }


                                if(json.has("netweight")){
                                    modal_menuItem.netweight= String.valueOf(json.get("netweight"));

                                }
                                else{
                                    modal_menuItem.netweight = "";
                                    //Log.d(Constants.TAG, "There is no netweight for this Menu: "  );


                                }    if(json.has("portionsize")){
                                    modal_menuItem.portionsize= String.valueOf(json.get("portionsize"));

                                }
                                else{
                                    modal_menuItem.portionsize = "";
                                    //Log.d(Constants.TAG, "There is no portionsize for this Menu: "  );


                                }



                                if(json.has("tmcctgykey")){
                                    modal_menuItem.tmcctgykey = String.valueOf(json.get("tmcctgykey"));

                                }
                                else{
                                    modal_menuItem.tmcctgykey = "";
                                    //Log.d(Constants.TAG, "There is no tmcctgykey for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcprice")){
                                    modal_menuItem.tmcprice = String.valueOf(json.get("tmcprice"));

                                }
                                else{
                                    modal_menuItem.tmcprice = "";
                                    //Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcpriceperkg")){
                                    modal_menuItem.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));

                                }
                                else{
                                    modal_menuItem.tmcpriceperkg = "";
                                    //Log.d(Constants.TAG, "There is no tmcpriceperkg for this Menu: " +MenuItemKey );


                                }
                                if(json.has("tmcsubctgykey")){
                                    modal_menuItem.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));

                                }
                                else{
                                    modal_menuItem.tmcsubctgykey = "";
                                    //Log.d(Constants.TAG, "There is no tmcsubctgykey for this Menu: " +MenuItemKey );


                                }

                                if(json.has("key")){
                                    modal_menuItem.menuItemId= String.valueOf(json.get("key"));

                                }
                                else{
                                    modal_menuItem.menuItemId = "";
                                    //Log.d(Constants.TAG, "There is no key for this Menu: "  );


                                }





                                MarinadeMenuList.add(modal_menuItem);

                                //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MarinadeMenuList);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                //Log.d(Constants.TAG, "e: " + e.getMessage());
                                //Log.d(Constants.TAG, "e: " + e.toString());

                            }


                        }

                        //Log.d(Constants.TAG, "convertingJsonStringintoArray MarinademenuListFull: " + MarinadeMenuList);


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
                //Log.d(TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(TAG, "Error: " + error.getMessage());
                //Log.d(TAG, "Error: " + error.toString());
              //  Toast.makeText(Pos_Dashboard_Screen.this,"Error in Getting Marinade Menu Item ",Toast.LENGTH_LONG).show();
                Log.d("RVA", "error:" + error);
                completeMarinademenuItem="";
                MarinadeMenuList.clear();
                SharedPreferences preferences =getSharedPreferences("MarinadeMenuList",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
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
                Toast.makeText(Pos_Dashboard_Screen.this,"Error in Getting Marinade Menu Item error code :  "+errorCode,Toast.LENGTH_LONG).show();
               // Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(dialog.isShowing()){

                            }
                            else {
                                title.setText(new StringBuilder().append("").append(errorCode).append(" .  Please Try Again !!!! ").toString());
                                restartAgain.setText("Click to Retry !!!");

                                restartAgain.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                        checkForInternetConnectionAndGetMenuItemAndMobileAppData();

                                    }
                                });


                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
        RetryPolicy policy = new DefaultRetryPolicy(60000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(Pos_Dashboard_Screen.this).add(jsonObjectRequest);
        return "";
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












    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case manage_order_navigatioBar_widget:
                try {
                        mfragment = new Pos_ManageOrderFragment();
                        // loadMyFragment(mfragment);
                        SharedPreferences sharedPreferences
                                = getSharedPreferences("CurrentSelectedStatus",
                                MODE_PRIVATE);

                        SharedPreferences.Editor myEdit
                                = sharedPreferences.edit();


                        myEdit.putString(
                                "currentstatus",
                                Constants.NEW_ORDER_STATUS);
                        myEdit.apply();

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame, new Pos_ManageOrderFragment());
                        transaction.commit();

                        Toast.makeText(Pos_Dashboard_Screen.this, "Clicked on Manage Orders Button", Toast.LENGTH_LONG).show();




                }
                catch(Exception e ){
                    e.printStackTrace();
                }
                break;
            case new_order_navigatioBar_widget:
                try {
                    if (!completemenuItem.isEmpty()) {
                        mfragment = new NewOrders_MenuItem_Fragment();
                        //loadMyFragment(mfragment);
                        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                        transaction1.replace(R.id.frame, NewOrders_MenuItem_Fragment.newInstance(completemenuItem));
                        transaction1.commit();
                        Toast.makeText(Pos_Dashboard_Screen.this, "Clicked on New Orders Button", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(Pos_Dashboard_Screen.this, "MenuItem is Not Loaded", Toast.LENGTH_LONG).show();
                        //Toast.makeText(Pos_Dashboard_Screen.this,"Check for Internet connection",Toast.LENGTH_LONG).show();

                    }
                }
                catch (Exception e ){
                    e.printStackTrace();
                }
                break;

            case settings_navigatioBar_widget:
                try {
                    if (isMenuListSavedLocally) {
                        mfragment = new SettingsFragment();
                        //loadMyFragment(mfragment);
                        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                        transaction2.replace(R.id.frame, new SettingsFragment());
                        transaction2.commit();
                        Toast.makeText(Pos_Dashboard_Screen.this, "Clicked on Settings Button", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(Pos_Dashboard_Screen.this, "MenuItem is Not Loaded", Toast.LENGTH_LONG).show();
                        //Toast.makeText(Pos_Dashboard_Screen.this,"Check for Internet connection",Toast.LENGTH_LONG).show();

                    }

                }
                catch (Exception e ){
                    e.printStackTrace();
                }



                break;

        }


        return true;

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

}