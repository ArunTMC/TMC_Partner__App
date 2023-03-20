package com.meatchop.tmcpartner.mobilescreen_javaclasses.other_classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.mobile_neworders.NewOrderScreenFragment_mobile;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.replacement_refund_classes.ReplacementRefundListFragment;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_vendor;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_WholeSaleCustomers;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.NewOrders_MenuItem_Fragment;
import com.meatchop.tmcpartner.settings.Modal_MenuItemCutDetails;
import com.meatchop.tmcpartner.settings.Modal_MenuItemStockAvlDetails;
import com.meatchop.tmcpartner.settings.Modal_MenuItemWeightDetails;
import com.meatchop.tmcpartner.settings.Modal_PosAppMobileData;
import com.meatchop.tmcpartner.settings.Modal_SubCtgyList;
import com.meatchop.tmcpartner.settings.SettingsFragment;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.sqlite.PosAppMobileDataSQL_DB_Manager;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;
import com.meatchop.tmcpartner.sqlite.TMCSubCtgyItemSQL_DB_Manager;
import com.meatchop.tmcpartner.sqlite.VendorSQL_DB_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MobileScreen_Dashboard extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    LinearLayout loadingPanel,loadingpanelmask,loadingpanelmask_userAccess;
    Fragment  CurrentFragment;
    Fragment mfragment;

    Mobile_ManageOrders1 mobile_manageOrders1;
    NewOrders_MenuItem_Fragment newOrders_menuItem_fragment;

    List<Modal_MenuItemCutDetails> MenuItemCutDetails=new ArrayList<>();
    List<Modal_MenuItem> MenuList=new ArrayList<>();
    List<Modal_MenuItem> MarinadeMenuList=new ArrayList<>();
    List<Modal_MenuItemStockAvlDetails> MenuItemStockAvlDetails=new ArrayList<>();
    ArrayList<Modal_MenuItemCutDetails> menuItemCutDetailsArray = new ArrayList<>();
    List<Modal_MenuItemWeightDetails> MenuItemWeightDetails=new ArrayList<>();

    boolean isMenuListSavedLocally = false;

    boolean isinventorycheck = false;
    boolean localDBcheck = false;
    boolean orderdetailsnewschema = false,updateweightforonlineorders =false;

    String MenuItemKey,vendorKey,tmcSubctgykey,tmcSubctgyname,tmcctgyname;

    public static String completemenuItem="",completeMarinademenuItem="",completemenuItemStockAvlDetails="";
    String UserRole,vendorEntryKey="",minimumscreensizeforpos ="" , defaultprintertype ="";

    String errorCode = "0";
    Dialog dialog ;
    Button restartAgain;
    TextView title;
    MenuView.ItemView replacement_navigatioBar_widget;
    String vendorType ="";
    List<Modal_WholeSaleCustomers> wholeSaleCustomersArrayList=new ArrayList<>();
    String menuitemupdationtime , posappdataupdationtime ="" , vendordetailsupdationtime ="" , subCtgyupdationtime ="";
    String menuitem_SqlDb_SyncTime , posappdataSqlDb_SyncTime ="" , vendordetailsSqlDb_SyncTime  ="" , subCtgySqlDb_SyncTime  ="" ;
    long menuitemUpdationTime_Long =0,posappdataupdationtime_Long =0 , vendordetailsupdationtime_Long =0 ,subCtgysupdationtime_Long =0;
    long menuitem_SqlDb_SyncTime_Long =0 , posappdataSqlDb_SyncTime_Long =0 , vendordetailsSqlDb_SyncTime_Long =0 ,  subCtgySqlDb_SyncTime_Long =0;

    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
    TMCSubCtgyItemSQL_DB_Manager tmcSubCtgyItemSQL_db_manager ;
    PosAppMobileDataSQL_DB_Manager posAppMobileDataSQL_db_manager;
    VendorSQL_DB_Manager vendorSQL_db_manager;
    boolean isAnyOnlineSyncNeeded = false , isOnlineSyncNeededForMenu = false ,isCalledFromSQLtimeSync =false;
    private static final String TAG = MobileScreen_Dashboard.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView();

        setContentView(R.layout.activity_mobile_screen__dashboard);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        bottomNavigationView = findViewById(R.id.bottomnav);
        loadingpanelmask_userAccess = findViewById(R.id.loadingpanelmask_userAccess);


        Adjusting_Widgets_Visibility(true);
        String size =  getDisplaySize(MobileScreen_Dashboard.this);
        SavePrinterConnectionDatainSharedPreferences();
        newOrders_menuItem_fragment = new NewOrders_MenuItem_Fragment();
        mobile_manageOrders1  = new Mobile_ManageOrders1();
        replacement_navigatioBar_widget = findViewById(R.id.replacement_navigatioBar_widget);




        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences SqlDbSyncDetails_shared = getSharedPreferences("SqlDbSyncDetails", MODE_PRIVATE);
                    menuitem_SqlDb_SyncTime =SqlDbSyncDetails_shared.getString("menuitem_SqlDb_SyncTime", "");
                    posappdataSqlDb_SyncTime =SqlDbSyncDetails_shared.getString("posappdataSqlDb_SyncTime", "");
                    vendordetailsSqlDb_SyncTime =SqlDbSyncDetails_shared.getString("vendordetailsSqlDb_SyncTime", "");
                    subCtgySqlDb_SyncTime =SqlDbSyncDetails_shared.getString("subCtgySqlDb_SyncTime", "");



                    SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
                    vendorKey = (shared.getString("VendorKey", ""));
                    isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));
                    minimumscreensizeforpos = String.valueOf(Constants.default_mobileScreenSize);
                    vendorType = shared.getString("VendorType","");
                    orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
                    localDBcheck = (shared.getBoolean("localdbcheck", false));


                    if(vendorKey.equals("vendor_4") || vendorKey.equals("vendor_5")){
                        Constants.isNewSbCtgyTable_APIUsed = Constants.YES;
                    }
                    else{
                        Constants.isNewSbCtgyTable_APIUsed = Constants.NO;

                    }
                  // orderdetailsnewschema = false;
                    UserRole = shared.getString("userrole", "");
                    dialog = new Dialog(MobileScreen_Dashboard.this);
                    vendorEntryKey = shared.getString("VendorKey", "");
                    dialog.setContentView(R.layout.print_again);
                    dialog.setTitle("Poor Internet Connection . Please Try Again !!!! ");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    restartAgain = (Button) dialog.findViewById(R.id.printAgain);
                    title = (TextView) dialog.findViewById(R.id.title);
                    tmcSubctgykey = getIntent().getStringExtra("tmcSubctgykey");
                    tmcSubctgyname = getIntent().getStringExtra("tmcSubctgyname");
                    tmcctgyname = getIntent().getStringExtra("tmcctgyname");


                    checkForInternetConnectionAndGetMenuItemAndMobileAppData();


                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //

   //     goToNotificationSettings();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.manage_order_navigatioBar_widget:
                    try{
                        Adjusting_Widgets_Visibility(true);
                        if(!UserRole.equals(Constants.REPORTSVIEWER_ROLENAME)){
                            if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)){
                                Adjusting_Widgets_Visibility(false);
                                bottomNavigationView.setVisibility(View.VISIBLE);

                                Toast.makeText(MobileScreen_Dashboard.this,"You Don't have access to this screen",Toast.LENGTH_LONG).show();

                            }
                            else{
                                mfragment = new Mobile_ManageOrders1();
                                loadMyFragment(mfragment);
                                Adjusting_Widgets_Visibility(false);
                            }



                        }
                        else{

                           // loadMyFragment(new Mobile_ManageOrders1());
                            Adjusting_Widgets_Visibility(false);
                            bottomNavigationView.setVisibility(View.VISIBLE);

                            Toast.makeText(MobileScreen_Dashboard.this,"You Don't have access to this screen",Toast.LENGTH_LONG).show();

                        }


                    }catch(Exception e){
                        e.printStackTrace();
                    }

                        break;
                    case R.id.replacement_navigatioBar_widget:
                        try{

                                if (localDBcheck) {


                                    if ((UserRole.equals(Constants.STOREMANAGER_ROLENAME)) || (UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.ADMIN_ROLENAME))) {
                                        mfragment = new ReplacementRefundListFragment();
                                        //loadMyFragment(mfragment);
                                        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                                        transaction1.replace(R.id.frame, new ReplacementRefundListFragment());

                                        transaction1.commit();
                                    } else {
                                        Toast.makeText(MobileScreen_Dashboard.this, "You Don't have Access to replace/refund the Order", Toast.LENGTH_LONG).show();
                                        bottomNavigationView.setVisibility(View.VISIBLE);

                                    }

                                }
                                else {
                                    if (!completemenuItem.isEmpty()) {

                                        if ((UserRole.equals(Constants.STOREMANAGER_ROLENAME)) || (UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.ADMIN_ROLENAME))) {
                                            mfragment = new ReplacementRefundListFragment();
                                            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                                            transaction1.replace(R.id.frame, new ReplacementRefundListFragment());

                                            transaction1.commit();
                                        } else {
                                            Toast.makeText(MobileScreen_Dashboard.this, "You Don't have Access to replace/refund the Order", Toast.LENGTH_LONG).show();
                                            bottomNavigationView.setVisibility(View.VISIBLE);

                                        }
                                    }
                                }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        break;

                    case R.id.new_order_navigatioBar_widget:
                        try{

                                if (localDBcheck) {

                                    if ((UserRole.equals(Constants.STOREMANAGER_ROLENAME)) || (UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.ADMIN_ROLENAME))) {
                                        mfragment = new NewOrderScreenFragment_mobile();
                                        //loadMyFragment(mfragment);
                                        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();

                                        transaction1.replace(R.id.frame, mfragment);


                                        //transaction1.replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(completemenuItem));
                                        transaction1.commit();

                                    } else {
                                        bottomNavigationView.setVisibility(View.VISIBLE);

                                        Toast.makeText(MobileScreen_Dashboard.this, "You Don't have Access to place Order", Toast.LENGTH_LONG).show();

                                    }

                                }
                                else {
                                    if (!completemenuItem.isEmpty()) {


                                        if ((UserRole.equals(Constants.STOREMANAGER_ROLENAME)) || (UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.ADMIN_ROLENAME))) {
                                            mfragment = new NewOrderScreenFragment_mobile();
                                            //loadMyFragment(mfragment);
                                            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();

                                            final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

                                            Gson gson = new Gson();
                                            String json = sharedPreferencesMenuitem.getString("MenuList", "");
                                            if (json.isEmpty()) {
                                                transaction1.replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(completemenuItem));
                                            } else {
                                                transaction1.replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(json));

                                            }


                                            //transaction1.replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(completemenuItem));
                                            transaction1.commit();

                                        } else {
                                            bottomNavigationView.setVisibility(View.VISIBLE);

                                            Toast.makeText(MobileScreen_Dashboard.this, "You Don't have Access to place Order", Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        Toast.makeText(MobileScreen_Dashboard.this, "MenuItem is Not Loaded", Toast.LENGTH_LONG).show();
                                        //Toast.makeText(Pos_Dashboard_Screen.this,"Check for Internet connection",Toast.LENGTH_LONG).show();

                                    }
                                }


                        }catch(Exception e){
                            e.printStackTrace();
                        }



                        break;

                    case R.id.settings_navigatioBar_widget:
                        try{
                            if(localDBcheck){
                                mfragment = new SettingsFragment();
                                //loadMyFragment(mfragment);
                                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction().addToBackStack(null)
                                        .addToBackStack(null);
                                transaction2.replace(R.id.frame, new SettingsFragment());

                                transaction2.commit();

                            }
                            else {


                                if (isMenuListSavedLocally) {
                                    mfragment = new SettingsFragment();
                                    //loadMyFragment(mfragment);
                                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction().addToBackStack(null)
                                            .addToBackStack(null);
                                    transaction2.replace(R.id.frame, new SettingsFragment());

                                    transaction2.commit();
                                    //        Toast.makeText(MobileScreen_Dashboard.this,"Clicked on Settings Button",Toast.LENGTH_LONG).show();

                                } else {
                                    Toast.makeText(MobileScreen_Dashboard.this, "MenuItem is Not Loaded", Toast.LENGTH_LONG).show();
                                    //Toast.makeText(Pos_Dashboard_Screen.this,"Check for Internet connection",Toast.LENGTH_LONG).show();

                                }
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Clear the Activity's bundle of the subsidiary fragments' bundles.
        outState.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
     /*   try{

                tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(this);
                try {
                    tmcMenuItemSQL_db_manager.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                posAppMobileDataSQL_db_manager = new PosAppMobileDataSQL_DB_Manager(this);
                try {
                    posAppMobileDataSQL_db_manager.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }





                vendorSQL_db_manager = new VendorSQL_DB_Manager(this);
                try {
                    vendorSQL_db_manager.open();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
        catch (Exception e){
            e.printStackTrace();
        }

      */

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

    private void checkForInternetConnectionAndGetMenuItemAndMobileAppData() {
        try {
            if (isConnected()) {
               // localDBcheck = false;
                checkForAccessPermissionInDataBase();
                if (vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)) {
                    getWholeSalesOrderCustomersList();
                } else {
                    getDeliveryPartnerList();
                    //  completeMarinademenuItem = getMarinadeMenuItemusingStoreId(vendorKey);

                }
                if(localDBcheck) {

                    getSqlSynDBDetails();

                }
                else {



                    getDatafromVendorTable(vendorEntryKey);



                }

                //  ConnectPrinter();

                // getMenuItemCutDetails();
                // getMenuItemWeightDetails();
      /*      Thread thread1 = new Thread() {

                    @Override
                    public void run() {
                        Log.i("1111111111Start time ",getDate_and_time());

                        completemenuItem = getMenuItemusingStoreId(vendorKey);
                    }
                };
                thread1.start();

                Thread thread2 = new Thread() {

                    @Override
                    public void run() {
                        getDatafromMobileApp();
                    }
                };
                thread2.start();

                Thread thread3 = new Thread() {

                    @Override
                    public void run() {
                        getDeliveryPartnerList();
                    }
                };
                thread3.start();

                Thread thread4 = new Thread() {

                    @Override
                    public void run() {
                        completeMarinademenuItem =  getMarinadeMenuItemusingStoreId(vendorKey);
                    }
                };
                thread4.start();


                */



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

    private void getSqlSynDBDetails() {

        isAnyOnlineSyncNeeded = false;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetSqlDBSyncDetails+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        // Log.d(Constants.TAG, "Elasticache  response Time of POS Mobile App table: " + getDate_and_time());

                        try {
                            String jsonString =response.toString();
                            //Log.d(Constants.TAG, " response: onMobileAppData " + response);
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray JArray  = jsonObject.getJSONArray("content");
                            ////Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1=0;
                            int arrayLength = JArray.length();
                            ////Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for(;i1<(arrayLength);i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);

                                    try{
                                        menuitemupdationtime = (json.getString("menuitemupdationtime"));

                                    }
                                    catch (Exception e){

                                        e.printStackTrace();
                                    }

                                    try{
                                        vendordetailsupdationtime = (json.getString("vendordetailsupdationtime"));

                                    }
                                    catch (Exception e){

                                        e.printStackTrace();
                                    }


                                    try{
                                        posappdataupdationtime = (json.getString("posappdataupdationtime"));

                                    }
                                    catch (Exception e){

                                        e.printStackTrace();
                                    }

                                    try{
                                        subCtgyupdationtime = (json.getString("subctgydataupdationtime"));

                                    }
                                    catch (Exception e){

                                        e.printStackTrace();
                                    }



                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                menuitemUpdationTime_Long  = getLongValuefortheDate(menuitemupdationtime);
                                menuitem_SqlDb_SyncTime_Long  = getLongValuefortheDate(menuitem_SqlDb_SyncTime);
                               Log.i("menuitemsync", menuitemupdationtime);
                               Log.i("menuitemlong  sync", String.valueOf(menuitemUpdationTime_Long));

                                Log.i("menuitem_SqlDbtime sync", menuitem_SqlDb_SyncTime);
                                Log.i("menuitem_SqlDblong sync", String.valueOf(menuitem_SqlDb_SyncTime_Long));

                                if(menuitem_SqlDb_SyncTime_Long >= menuitemUpdationTime_Long){
                                   ConnectSqlDb("FetchMenuItemData");
                                    isOnlineSyncNeededForMenu = false;
                                }
                                else{
                                    isAnyOnlineSyncNeeded = true;
                                    isOnlineSyncNeededForMenu = true;
                                    ConnectSqlDb("ConnectToMenuItemTable");


                                }

                                posappdataupdationtime_Long  = getLongValuefortheDate(posappdataupdationtime);
                                posappdataSqlDb_SyncTime_Long  = getLongValuefortheDate(posappdataSqlDb_SyncTime);

                                Log.i("posAppsync", posappdataupdationtime);
                                Log.i("posAppslong  sync", String.valueOf(posappdataupdationtime_Long));

                                Log.i("posApps_SqlDbtime sync", posappdataSqlDb_SyncTime);
                                Log.i("posApps_SqlDblong sync", String.valueOf(posappdataSqlDb_SyncTime_Long));



                                if(posappdataSqlDb_SyncTime_Long >= posappdataupdationtime_Long){
                                    ConnectSqlDb("FetchPOSAppData");
                                }
                                else {
                                    isAnyOnlineSyncNeeded = true;
                                    ConnectSqlDb("ConnectToPOSAppDataTable");

                                }



                                vendordetailsupdationtime_Long  = getLongValuefortheDate(vendordetailsupdationtime);
                                vendordetailsSqlDb_SyncTime_Long  = getLongValuefortheDate(vendordetailsSqlDb_SyncTime);
                                Log.i("vendor  sync", vendordetailsupdationtime);
                                Log.i("vendorlong  sync", String.valueOf(vendordetailsupdationtime_Long));

                                Log.i("vendor_SqlDbtime sync", vendordetailsSqlDb_SyncTime);
                                Log.i("vendor_SqlDblong sync", String.valueOf(vendordetailsSqlDb_SyncTime_Long));



                                if(vendordetailsSqlDb_SyncTime_Long >= vendordetailsupdationtime_Long){
                                    ConnectSqlDb("FetchVendorData");
                                }
                                else{
                                    isAnyOnlineSyncNeeded = true;
                                    isCalledFromSQLtimeSync = true;
                                    ConnectSqlDb("ConnectToVendorTable");

                                }



                                subCtgysupdationtime_Long  = getLongValuefortheDate(subCtgyupdationtime);
                                subCtgySqlDb_SyncTime_Long  = getLongValuefortheDate(subCtgySqlDb_SyncTime);
                                Log.i("subctgy  sync", subCtgyupdationtime);
                                Log.i("subctgylong  sync", String.valueOf(subCtgysupdationtime_Long));

                                Log.i("subctgy_SqlDbtime sync", subCtgySqlDb_SyncTime);
                                Log.i("subctgy_SqlDblong sync", String.valueOf(subCtgySqlDb_SyncTime_Long));



                                if(subCtgySqlDb_SyncTime_Long >= subCtgysupdationtime_Long){
                                    ConnectSqlDb("FetchSubctgyData");
                                }
                                else{
                                    isAnyOnlineSyncNeeded = true;
                                    ConnectSqlDb("ConnectToSubCtgyTable");

                                }







                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());


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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in SqlDBSyncDetails code :  "+errorCode,Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);











    }

    @SuppressLint("Range")
    private void ConnectSqlDb(String ProcessToDo) {


        if(tmcMenuItemSQL_db_manager== null) {
            tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(this);
            try {
                tmcMenuItemSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(posAppMobileDataSQL_db_manager== null) {

            posAppMobileDataSQL_db_manager = new PosAppMobileDataSQL_DB_Manager(this);
            try {
                posAppMobileDataSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(vendorSQL_db_manager== null) {

            vendorSQL_db_manager = new VendorSQL_DB_Manager(this);
            try {
                vendorSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if(tmcSubCtgyItemSQL_db_manager == null) {

            tmcSubCtgyItemSQL_db_manager = new TMCSubCtgyItemSQL_DB_Manager(this);
            try {
                tmcSubCtgyItemSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /*try{

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
        Cursor cursor = TMCMenuItemSQLDBManager.FetchAllTable();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Toast.makeText(MobileScreen_Dashboard.this, "Table Name=> " + cursor.getString(0), Toast.LENGTH_LONG).show();
                cursor.moveToNext();
            }
        }
        }
        catch (Exception e){
            e.printStackTrace();
        }

         */


        try{
        if(ProcessToDo.equals("FetchMenuItemData")){
            Cursor cursor = tmcMenuItemSQL_db_manager.Fetch();
            MenuList.clear();
            try {
               // if (cursor.moveToFirst()) {

                Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));
                isOnlineSyncNeededForMenu = false;
                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {
                            Modal_MenuItem modal_menuItem = new Modal_MenuItem();
                            modal_menuItem.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_menuItem.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_menuItem.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.localDB_id)));
                            modal_menuItem.setApplieddiscountpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.applieddiscountpercentage)));
                            modal_menuItem.setBarcode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode)));
                            modal_menuItem.setCheckoutimageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.checkoutimageurl)));
                            modal_menuItem.setDisplayno(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.displayno)));
                            modal_menuItem.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_menuItem.setGstpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.gstpercentage)));
                            modal_menuItem.setImageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.imageurl)));
                            modal_menuItem.setItemavailability(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability)));
                            modal_menuItem.setItemcalories(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemcalories)));
                            modal_menuItem.setItemuniquecode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemuniquecode)));
                            modal_menuItem.setMarinadelinkedcodes(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.marinadelinkedcodes)));
                            modal_menuItem.setMenuboarddisplayname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuboarddisplayname)));
                            modal_menuItem.setMenutype(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menutype)));
                            modal_menuItem.setNetweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.netweight)));
                            modal_menuItem.setPortionsize(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.portionsize)));
                            modal_menuItem.setPricetypeforpos(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.pricetypeforpos)));
                            modal_menuItem.setShowinmenuboard(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.showinmenuboard)));
                            modal_menuItem.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcctgykey)));
                            modal_menuItem.setTmcpriceperkg(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg)));
                            modal_menuItem.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_menuItem.setTmcsubctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcsubctgykey)));
                            modal_menuItem.setVendorkey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey)));
                            modal_menuItem.setVendorname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorname)));
                            modal_menuItem.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_menuItem.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_menuItem.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_menuItem.setGrossweightingrams(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweightingrams)));
                            modal_menuItem.setReportname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.reportname)));
                            modal_menuItem.setSwiggyprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.swiggyprice)));
                            modal_menuItem.setDunzoprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.dunzoprice)));
                            modal_menuItem.setBigbasketprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.bigbasketprice)));
                            modal_menuItem.setWholesaleprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.wholesaleprice)));
                            modal_menuItem.setAppmarkuppercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)));
                            modal_menuItem.setTmcpriceperkgWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkgWithMarkupValue)));
                            modal_menuItem.setTmcpriceWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceWithMarkupValue)));
                            modal_menuItem.setKey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem.setInventorydetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.inventoryDetails)));
                            modal_menuItem.setGrossweightingrams(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweightingrams)));


                            if (!isinventorycheck) {

                                String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                        receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                                modal_menuItem.setBarcode_AvlDetails(barcode_AvlDetails);
                                modal_menuItem.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                modal_menuItem.setKey_AvlDetails(key_AvlDetails);
                                modal_menuItem.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                modal_menuItem.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                modal_menuItem.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                modal_menuItem.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                modal_menuItem.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                modal_menuItem.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                modal_menuItem.setAllownegativestock(allownegativestock_AvlDetails);


                                Modal_MenuItemStockAvlDetails modal_menuItemStockAvlDetails = new Modal_MenuItemStockAvlDetails();
                                modal_menuItemStockAvlDetails.setBarcode(barcode_AvlDetails);
                                modal_menuItemStockAvlDetails.setItemavailability(itemavailability_AvlDetails);
                                modal_menuItemStockAvlDetails.setKey(key_AvlDetails);
                                modal_menuItemStockAvlDetails.setMenuitemkey(menuitemkey_AvlDetails);
                                modal_menuItemStockAvlDetails.setStockincomingkey(stockincomingkey_AvlDetails);
                                modal_menuItemStockAvlDetails.setVendorkey(vendorkey_AvlDetails);
                                modal_menuItemStockAvlDetails.setStockbalance(stockbalance_AvlDetails);
                                modal_menuItemStockAvlDetails.setAllownegativestock(allownegativestock_AvlDetails);
                                modal_menuItemStockAvlDetails.setLastupdatedtime(lastupdatedtime_AvlDetails);
                                modal_menuItemStockAvlDetails.setReceivedstock(receivedstock_AvlDetails);
                                MenuItemStockAvlDetails.add(modal_menuItemStockAvlDetails);




                            }
                            else{

                                modal_menuItem.setBarcode_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode_AvlDetails)));
                                modal_menuItem.setItemavailability_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability_AvlDetails)));
                                modal_menuItem.setKey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.key_AvlDetails)));
                                modal_menuItem.setLastupdatedtime_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.lastupdatedtime_AvlDetails)));
                                modal_menuItem.setMenuitemkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId_AvlDetails)));
                                modal_menuItem.setReceivedstock_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.receivedStock_AvlDetails)));
                                modal_menuItem.setStockbalance_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockBalance_AvlDetails)));
                                modal_menuItem.setStockincomingkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockIncomingKey_AvlDetails)));
                                modal_menuItem.setVendorkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey_AvlDetails)));
                                modal_menuItem.setAllownegativestock(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.allowNegativeStock_AvlDetails)));

                                ////// for TMCPrice
                                try{
                                    double tmcprice_double =0 ,  tmcpriceWithAppMarkupValue = 0 , appMarkupPercn_value =0;
                                    try {
                                        if (!cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)).equals("") && (!String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice))).toUpperCase().equals("NULL"))) {
                                            tmcprice_double = Double.parseDouble(String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice))));

                                        } else {
                                            tmcprice_double = 0;
                                            ////Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        tmcprice_double =0;
                                    }
                                    if (!cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)).equals("") && (!String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage))).toUpperCase().equals("NULL"))) {
                                        int markupPercentageInt = Integer.parseInt(String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage))));

                                        try{
                                            appMarkupPercn_value  = (markupPercentageInt * tmcprice_double )/100;
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }


                                        try{
                                            tmcpriceWithAppMarkupValue  = appMarkupPercn_value + tmcprice_double;
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }


                                        modal_menuItem.tmcpriceWithMarkupValue = String.valueOf((int) Double.parseDouble(String.valueOf(Math.ceil(tmcpriceWithAppMarkupValue))));
                                    }
                                    else{
                                        modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);

                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                ////// for TMCPrice PerKG

                                try{
                                    double tmcpriceperkg_double =0 ,  tmcpriceperkgWithAppMarkupValue = 0 , appMarkupPercn_value =0;
                                    try {
                                        if (!cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg)).equals("") && (!String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg))).toUpperCase().equals("NULL"))) {
                                            tmcpriceperkg_double = Double.parseDouble(String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg))));

                                        } else {
                                            tmcpriceperkg_double = 0;
                                            ////Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        tmcpriceperkg_double =0;
                                    }
                                    if (!cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)).equals("") && (!String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage))).toUpperCase().equals("NULL"))) {

                                        int markupPercentageInt =Integer.parseInt(String.valueOf(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage))));

                                        try{
                                            appMarkupPercn_value  = (markupPercentageInt * tmcpriceperkg_double  )/100;
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }


                                        try{
                                            tmcpriceperkgWithAppMarkupValue  = appMarkupPercn_value + tmcpriceperkg_double;
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf((int) Double.parseDouble(String.valueOf(Math.round(tmcpriceperkgWithAppMarkupValue))));
                                    }
                                    else{
                                        modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);

                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }



                            }
                            if(modal_menuItem.itemuniquecode.equals("1407")){
                                Log.i(" cursor count  ::  ", String.valueOf(modal_menuItem.getTmcpriceperkgWithMarkupValue()));
                            }
                            MenuList.add(modal_menuItem);
                        }
                        while (cursor.moveToNext());


                    }



                }
                else{
                    isOnlineSyncNeededForMenu = true;
                    isAnyOnlineSyncNeeded = true;
                    completemenuItem = getMenuItemusingStoreId(vendorKey);


                }




              //  }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(ProcessToDo.equals("FetchPOSAppData")) {
            String maxpointsinaday="",minordervalueforredeem ="",pointsfor100rs="" ,admin_role = "" ,cashier_role ="" ,
                    deliverymanager_role ="" , reportsviewer_role ="" , storemanager_role ="";
            Cursor cursor = posAppMobileDataSQL_db_manager.Fetch();
           try{
               if(cursor.getCount()>0){

                   if(cursor.moveToFirst()) {
                       do {
                           orderdetailsnewschema = Boolean.parseBoolean((cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.orderdetailsnewschema))));
                           updateweightforonlineorders = Boolean.parseBoolean((cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.updateweightforonlineorders))));
                           maxpointsinaday  = (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.redeemdata_maxpointsinaday)));
                           minordervalueforredeem = (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.redeemdata_minordervalueforredeem)));
                           pointsfor100rs  = (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.redeemdata_pointsfor100rs)));
                           admin_role  = (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.appacessdetails_admin)));
                           cashier_role = (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.appacessdetails_cashier)));
                           deliverymanager_role  = (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.appacessdetails_deliverymanager)));
                           reportsviewer_role =  (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.appacessdetails_reportsviewer)));
                           storemanager_role  = (cursor.getString(cursor.getColumnIndex(PosAppMobileDataSQL_DB_Manager.appacessdetails_storemanager)));

                       }
                       while (cursor.moveToNext());

                   }
                   saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema, updateweightforonlineorders);
                   saveredeemDetailsinSharePreferences(maxpointsinaday,minordervalueforredeem,pointsfor100rs);
                   savepartnerappacessdetailsinSharePreferences(admin_role,cashier_role,deliverymanager_role,reportsviewer_role,storemanager_role);



               }
               else{
                   isAnyOnlineSyncNeeded = true;
                   getDatafromMobileApp();



               }
           }
           catch (Exception e){
               e.printStackTrace();
           }


        }
        else if(ProcessToDo.equals("FetchVendorData")) {

            Cursor cursor = vendorSQL_db_manager.Fetch();
            try{
                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {

                            try{
                                isinventorycheck = Boolean.parseBoolean((cursor.getString(cursor.getColumnIndex(VendorSQL_DB_Manager.inventorycheckpos))));


                            }
                            catch (Exception e){

                                isinventorycheck =     false;
                                e.printStackTrace();
                            }



                            try{
                                localDBcheck = Boolean.parseBoolean((cursor.getString(cursor.getColumnIndex(VendorSQL_DB_Manager.localdbcheck))));


                            }
                            catch (Exception e){

                                localDBcheck =     false;
                                e.printStackTrace();
                            }


                            try{
                                if(cursor.getString(cursor.getColumnIndex(VendorSQL_DB_Manager.defaultprintertype)) . equals("")) {
                                    defaultprintertype = String.valueOf(cursor.getString(cursor.getColumnIndex(VendorSQL_DB_Manager.defaultprintertype)));
                                    if (defaultprintertype.equals(Constants.PDF_PrinterType)) {
                                        defaultprintertype = String.valueOf(Constants.PDF_PrinterType);

                                    } else {
                                        defaultprintertype = String.valueOf(Constants.Bluetooth_PrinterType);
                                    }
                                }
                                else{
                                    defaultprintertype = String.valueOf(Constants.Bluetooth_PrinterType);

                                }

                            }
                            catch (Exception e){

                                defaultprintertype = String.valueOf(Constants.Bluetooth_PrinterType);


                                e.printStackTrace();
                            }




                            try{
                                minimumscreensizeforpos =  String.valueOf(cursor.getString(cursor.getColumnIndex(VendorSQL_DB_Manager.minimumscreensizeforpos)));

                            }
                            catch (Exception e){

                                minimumscreensizeforpos = String.valueOf(Constants.default_mobileScreenSize);


                                e.printStackTrace();
                            }


                        }
                        while (cursor.moveToNext());

                    }
                    saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema, updateweightforonlineorders);



                }
                else{
                    isAnyOnlineSyncNeeded = true;
                    isCalledFromSQLtimeSync = true;
                    getDatafromVendorTable(vendorEntryKey);



                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            if(!isAnyOnlineSyncNeeded) {
                saveMenuIteminSharedPreference(MenuList);
                saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                String MenuList_String = new Gson().toJson(MenuList);
                completemenuItem = MenuList_String;
            }

        }
        else if(ProcessToDo.equals("FetchSubctgyData")) {

            Cursor cursor = tmcSubCtgyItemSQL_db_manager.Fetch();
            try{
                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {



                        }
                        while (cursor.moveToNext());

                    }



                }
                else{
                    isAnyOnlineSyncNeeded = true;



                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            if(!isAnyOnlineSyncNeeded) {
                saveMenuIteminSharedPreference(MenuList);
                saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                String MenuList_String = new Gson().toJson(MenuList);
                completemenuItem = MenuList_String;
            }

        }

        else if(ProcessToDo.equals("ConnectToPOSAppDataTable")) {

            try{
                if(posAppMobileDataSQL_db_manager.deleteTable(true)>=0){
                    posAppMobileDataSQL_db_manager.open();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


          /*  try{
                posAppMobileDataSQL_db_manager.dropTable(true);
            }
            catch (Exception e){
                e.printStackTrace();
            }

           */

            getDatafromMobileApp();
        }
        else if(ProcessToDo.equals("ConnectToVendorTable")) {
            try{
                if(vendorSQL_db_manager.deleteTable(true)>=0){
                    vendorSQL_db_manager.open();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            /*try{
                vendorSQL_db_manager.dropTable(true);
            }
            catch (Exception e){
                e.printStackTrace();
            }

             */
            getDatafromVendorTable(vendorEntryKey);

        }
        else if(ProcessToDo.equals("ConnectToMenuItemTable")){
            try{
             if(tmcMenuItemSQL_db_manager.deleteTable(true)>=0){
                 tmcMenuItemSQL_db_manager.open();
             }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            completemenuItem = getMenuItemusingStoreId(vendorKey);

        }
        else if(ProcessToDo.equals("ConnectToSubCtgyTable")) {

                try{
                    if(tmcSubCtgyItemSQL_db_manager.deleteTable(true)>=0){
                        tmcSubCtgyItemSQL_db_manager.open();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }




                getTMCSubCtgy();
            }

        }
        catch (Exception e){
        e.printStackTrace();
    }
        finally {

            try{
                if(tmcMenuItemSQL_db_manager != null){
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }
                if(vendorSQL_db_manager != null){
                    vendorSQL_db_manager.close();
                    vendorSQL_db_manager = null;
                }
                if(posAppMobileDataSQL_db_manager != null){
                    posAppMobileDataSQL_db_manager.close();
                    posAppMobileDataSQL_db_manager = null;
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

    private void getTMCSubCtgy() {
        String Api_to_Call = "";
        if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)) {
            Api_to_Call = Constants.api_getListofTMCTileForVendorkey+vendorKey;
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
                         JSONArray  result  = response.getJSONArray("content");

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

                                    //Log.d(Constants.TAG, "subctgyname from subCtgy: " + subctgyname);
                                    Modal_SubCtgyList modal_subCtgyList = new Modal_SubCtgyList();
                                    modal_subCtgyList.setKey(key);
                                    modal_subCtgyList.setSubCtgyName(subctgyname);
                                    modal_subCtgyList.setDisplayNo(displayNo);
                                    modal_subCtgyList.setTmcctgykey(ctgykey);
                                    modal_subCtgyList.setTmcctgyname(ctgyname);


                                    try {
                                        if(localDBcheck) {


                                            if(tmcSubCtgyItemSQL_db_manager == null) {

                                                tmcSubCtgyItemSQL_db_manager = new TMCSubCtgyItemSQL_DB_Manager(getApplicationContext());
                                                try {
                                                    tmcSubCtgyItemSQL_db_manager.open();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }



                                            long id = tmcSubCtgyItemSQL_db_manager.insert(modal_subCtgyList);


                                            modal_subCtgyList.setLocalDB_id(String.valueOf(id));
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    finally {
                                        try {
                                            if (jArray.length() - i == 1) {
                                                tmcSubCtgyItemSQL_db_manager = null;
                                                tmcSubCtgyItemSQL_db_manager.close();

                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
            Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);

        }



        private void getWholeSalesOrderCustomersList() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofWholeSaleCustomers,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        ////Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            ////Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            ////Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {
                                Modal_WholeSaleCustomers modal_wholeSaleCustomers = new Modal_WholeSaleCustomers();
                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    try{
                                        if(json.has("name")) {
                                            modal_wholeSaleCustomers.setCustomerName(String.valueOf(json.get("name")));
                                        }
                                        else{
                                            modal_wholeSaleCustomers.setCustomerName(String.valueOf(""));
                                        }
                                    }
                                    catch (Exception e) {
                                        modal_wholeSaleCustomers.setCustomerName(String.valueOf(""));

                                        e.printStackTrace();
                                    }



                                    try{
                                        if(json.has("mobileno")) {
                                            modal_wholeSaleCustomers.setMobileno(String.valueOf(json.get("mobileno")));
                                        }
                                        else{
                                            modal_wholeSaleCustomers.setMobileno(String.valueOf(""));
                                        }
                                    }
                                    catch (Exception e) {
                                        modal_wholeSaleCustomers.setMobileno(String.valueOf(""));

                                        e.printStackTrace();
                                    }



                                    wholeSaleCustomersArrayList.add(modal_wholeSaleCustomers);

                                    saveWholeSalesCustomerDetailsInSharedPreference(wholeSaleCustomersArrayList);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ////Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    ////Log.d(Constants.TAG, "e: " + e.getMessage());
                                    ////Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                ////Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                ////Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Store");
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void checkForAccessPermissionInDataBase() {
          SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        String mobileno = (shared.getString("UserPhoneNumber", "+91"));

        String UserMobileNumberEncoded  = "";
        try {
            UserMobileNumberEncoded = URLEncoder.encode(mobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_checkUserhaveForPartnerApp+"?mobileno="+UserMobileNumberEncoded,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        ////Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            ////Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            ////Log.d(TAG, "Response: " + arrayLength);
                            boolean isUserhaveAccess = false;

                            if(arrayLength>0){
                                for(;i1<=(arrayLength-1);i1++) {
                                    try {
                                        JSONObject json = result.getJSONObject(i1);
                                        try {

                                            if (json.has("appname")) {
                                                if (String.valueOf(json.getString("appname")).toUpperCase().equals(Constants.AppName_in_AppUserAccess)) {
                                                    isUserhaveAccess = true;
                                                }
                                            } else {
                                                isUserhaveAccess = false;
                                            }

                                        } catch (Exception e) {
                                            isUserhaveAccess = false;
                                            e.printStackTrace();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        ////Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                        ////Log.d(Constants.TAG, "e: " + e.getMessage());
                                        ////Log.d(Constants.TAG, "e: " + e.toString());

                                    }


                                }

                            }
                            else
                            {
                                isUserhaveAccess = false;

                            }


                            if(isUserhaveAccess){
                                loadingpanelmask_userAccess.setVisibility(View.GONE);

                            }
                            else{
                                showAccessRejectedDialog();

                            }


                        } catch (JSONException e) {

                            showAccessRejectedDialog();


                            e.printStackTrace();
                        }
                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                ////Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                ////Log.d(Constants.TAG, "Error: " + error.toString());
                showAccessRejectedDialog();

                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Store");
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }

    private void showAccessRejectedDialog() {
        loadingpanelmask_userAccess.setVisibility(View.VISIBLE);

        new TMCAlertDialogClass(MobileScreen_Dashboard.this, R.string.app_name, R.string.UserMobileNo_DoesNot_HaveAccess,
                R.string.OK_Text, R.string.Empty_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        showAccessRejectedDialog();


                    }

                    @Override
                    public void onNo() {

                    }
                });

    }


    private void saveWholeSalesCustomerDetailsInSharedPreference(List<Modal_WholeSaleCustomers> wholeSaleCustomersArrayList) {

        try {
            final SharedPreferences sharedPreferences = getSharedPreferences("WholeSaleCustomerDetails", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(wholeSaleCustomersArrayList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("WholeSaleCustomerDetails", json);
            editor.apply();

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }
    private void getDatafromVendorTable(String vendorEntryKey) {
//api_GetTestDataFromElasticache2
//api_GetVendorUsingUserKey
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetVendorUsingUserKey +vendorEntryKey,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        ////Log.d(Constants.TAG, "Response: " + response);
                        try {
                           // Log.d(Constants.TAG, "Elasticache  response Time of vendor table: " + getDate_and_time());

                           JSONObject resultobject  = response.getJSONObject("content");
                            JSONObject resultitem = resultobject.getJSONObject("Item");
                            ////Log.d(Constants.TAG, "Response: " + result);
                            JSONArray result = new JSONArray();
                            result.put(resultitem);
                            int i1=0;
                            int arrayLength = result.length();
                            ////Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {
                                Modal_vendor modal_vendor = new Modal_vendor();
                                try {
                                    JSONObject json = result.getJSONObject(i1);


                                   try{
                                       isinventorycheck=      Boolean.parseBoolean(String.valueOf(json.get("inventorycheckpos")));

                                   }
                                   catch (Exception e){

                                       isinventorycheck =     false;


                                       e.printStackTrace();
                                   }



                                    try{
                                        localDBcheck =      Boolean.parseBoolean(String.valueOf(json.get("localdbcheck")));

                                    }
                                    catch (Exception e){

                                        localDBcheck =     false;
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("defaultprintertype")) {
                                            defaultprintertype = String.valueOf(json.get("defaultprintertype"));
                                            if (defaultprintertype.equals(Constants.PDF_PrinterType)) {
                                                defaultprintertype = String.valueOf(Constants.PDF_PrinterType);

                                            } else {
                                                defaultprintertype = String.valueOf(Constants.Bluetooth_PrinterType);
                                            }
                                        }
                                        else{
                                            defaultprintertype = String.valueOf(Constants.Bluetooth_PrinterType);

                                        }

                                    }
                                    catch (Exception e){

                                        defaultprintertype = String.valueOf(Constants.Bluetooth_PrinterType);


                                        e.printStackTrace();
                                    }




                                    try{
                                        minimumscreensizeforpos =  String.valueOf(json.get("minimumscreensizeforpos"));

                                    }
                                    catch (Exception e){

                                        minimumscreensizeforpos = String.valueOf(Constants.default_mobileScreenSize);


                                        e.printStackTrace();
                                    }





                                    if(!isCalledFromSQLtimeSync){


                                        isOnlineSyncNeededForMenu = true;
                                        isAnyOnlineSyncNeeded =true;
                                        getDatafromMobileApp();
                                        completemenuItem = getMenuItemusingStoreId(vendorKey);
                                        if(!localDBcheck){

                                            try{
                                                int i = posAppMobileDataSQL_db_manager.deleteTable(false);
                                                try {
                                                    posAppMobileDataSQL_db_manager.close();
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }


                                            try{
                                                int i = vendorSQL_db_manager.deleteTable(false);
                                                try {
                                                    vendorSQL_db_manager.close();
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            try{
                                                int i =  tmcSubCtgyItemSQL_db_manager.deleteTable(false);
                                                try {
                                                    tmcSubCtgyItemSQL_db_manager.close();
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            try{
                                                int i= tmcMenuItemSQL_db_manager.deleteTable(false);
                                                try {
                                                    tmcMenuItemSQL_db_manager.close();
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                    else{
                                        isCalledFromSQLtimeSync = false;
                                    }




                                    modal_vendor.setInventorycheckpos(String.valueOf(isinventorycheck));
                                    modal_vendor.setLocaldbcheck(String.valueOf(localDBcheck));
                                    modal_vendor.setDefaultprintertype(String.valueOf(defaultprintertype));
                                    modal_vendor.setMinimumscreensizeforpos(String.valueOf(minimumscreensizeforpos));

                                    try{
                                        if(localDBcheck){
                                            try {


                                                vendorSQL_db_manager = new VendorSQL_DB_Manager(getApplicationContext());
                                                try{
                                                    vendorSQL_db_manager.open();
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }




                                                vendorSQL_db_manager.insert(modal_vendor);
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            finally {
                                                try {
                                                    if (arrayLength - i1 == 1) {
                                                        vendorSQL_db_manager = null;
                                                        tmcSubCtgyItemSQL_db_manager.close();

                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            }

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema, updateweightforonlineorders);
                                    if(isAnyOnlineSyncNeeded) {
                                        if(!isOnlineSyncNeededForMenu) {
                                            saveMenuIteminSharedPreference(MenuList);
                                            saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                                            String MenuList_String = new Gson().toJson(MenuList);
                                            completemenuItem = MenuList_String;
                                        }
                                    }

                                } catch (JSONException e) {

                                    e.printStackTrace();

                                    saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema, updateweightforonlineorders);

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema, updateweightforonlineorders);

                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                ////Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                ////Log.d(Constants.TAG, "Error: " + error.toString());
                saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema, updateweightforonlineorders);

                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Store");
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);



    }


    private String  getMenuItemStockAvlDetails() {
        //Log.d(TAG, "starting:getfullMenuItemStockavldetailsUsingStoreID ");
        completemenuItemStockAvlDetails="";
        MenuItemStockAvlDetails.clear();
        SharedPreferences preferences =getSharedPreferences("MenuItemStockAvlDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        //api_getListofMenuItemStockAvlDetails
        //api_GetTestDataFromWriteRedisFromDynamoDB
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItemStockAvlDetails+vendorKey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{
                    completemenuItemStockAvlDetails="";
                    MenuItemStockAvlDetails.clear();
                    SharedPreferences preferences =getSharedPreferences("MenuItemStockAvlDetails",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    //Log.d(TAG, "starting:onResponse ");

                    //Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());
                //    Log.d(Constants.TAG, "Elasticache response Time of Menu Stock Avl table: " + getDate_and_time());

                    try {
                        JSONArray JArray = response.getJSONArray("content");
                        completemenuItemStockAvlDetails = new String(String.valueOf(JArray));

                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                        String Key="";
                        for (; i1 < (arrayLength); i1++) {

                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_MenuItemStockAvlDetails modal_menuItemStockAvlDetails = new Modal_MenuItemStockAvlDetails();
                                if(json.has("key")){
                                    modal_menuItemStockAvlDetails.key = String.valueOf(json.get("key"));
                                    Key =    String.valueOf(json.get("key"));
                                }
                                else{
                                    modal_menuItemStockAvlDetails.key = "";
                                    //Log.d(Constants.TAG, "There is no key for this Menu: " );


                                }

                                if(json.has("barcode")){
                                    modal_menuItemStockAvlDetails.barcode = String.valueOf(json.get("barcode"));

                                }
                                else{
                                    modal_menuItemStockAvlDetails.barcode = "";
                                    //Log.d(Constants.TAG, "There is no barcode for this Menu: " +Key );


                                }
                                if(json.has("itemavailability")){
                                    modal_menuItemStockAvlDetails.itemavailability = String.valueOf(json.get("itemavailability"));

                                }
                                else{
                                    modal_menuItemStockAvlDetails.itemavailability = "";
                                    //Log.d(Constants.TAG, "There is no itemavailability for this Menu: " +Key );


                                }
                                if(json.has("lastupdatedtime")){
                                    modal_menuItemStockAvlDetails.lastupdatedtime = String.valueOf(json.get("lastupdatedtime"));
                                    if(String.valueOf(json.get("lastupdatedtime")).equals("")){
                                        modal_menuItemStockAvlDetails.lastupdatedtime = "0";

                                    }else  if(String.valueOf(json.get("lastupdatedtime")).equals("\r")) {

                                        modal_menuItemStockAvlDetails.lastupdatedtime = "0";

                                    }

                                }
                                else{
                                    modal_menuItemStockAvlDetails.lastupdatedtime = "0";
                                    //Log.d(Constants.TAG, "There is no lastupdatedtime for this Menu: " +Key );


                                }


                                if(json.has("menuitemkey")){
                                    modal_menuItemStockAvlDetails.menuitemkey = String.valueOf(json.get("menuitemkey"));
                                    if(String.valueOf(json.get("menuitemkey")).equals("")){
                                        modal_menuItemStockAvlDetails.menuitemkey = "0";

                                    }else  if(String.valueOf(json.get("menuitemkey")).equals("\r")) {

                                        modal_menuItemStockAvlDetails.menuitemkey = "0";

                                    }

                                }
                                else{
                                    modal_menuItemStockAvlDetails.menuitemkey = "0";
                                    //Log.d(Constants.TAG, "There is no menuitemkey for this Menu: " +Key );


                                }


                                if(json.has("stockbalance")){
                                    modal_menuItemStockAvlDetails.stockbalance= String.valueOf(json.get("stockbalance"));
                                    if(String.valueOf(json.get("stockbalance")).equals("")){
                                        modal_menuItemStockAvlDetails.stockbalance = "0";

                                    }else  if(String.valueOf(json.get("stockbalance")).equals("\r")) {

                                        modal_menuItemStockAvlDetails.stockbalance = "0";

                                    }
                                }
                                else{
                                    modal_menuItemStockAvlDetails.stockbalance = "0";
                                    //Log.d(Constants.TAG, "There is no stockbalance for this Menu: " +Key );


                                }

                                if(json.has("stockincomingkey")){
                                    modal_menuItemStockAvlDetails.stockincomingkey = String.valueOf(json.get("stockincomingkey"));

                                }
                                else{
                                    modal_menuItemStockAvlDetails.stockincomingkey = "";
                                    //Log.d(Constants.TAG, "There is no stockincomingkey for this Menu: " +Key );


                                }
                                if(json.has("vendorkey")){
                                    modal_menuItemStockAvlDetails.vendorkey = String.valueOf(json.get("vendorkey"));

                                }
                                else{
                                    modal_menuItemStockAvlDetails.vendorkey = "";
                                    //Log.d(Constants.TAG, "There is no vendorkey for this Menu: " +Key );


                                }


                                MenuItemStockAvlDetails.add(modal_menuItemStockAvlDetails);

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


                    saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                    //Log.d(TAG, "sending :Response "+completemenuItem);







                    //Log.d(TAG, "MenuItems: " + completemenuItem.toString());



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
                completemenuItemStockAvlDetails="";
                MenuItemStockAvlDetails.clear();
                SharedPreferences preferences =getSharedPreferences("MenuItemStockAvlDetails",Context.MODE_PRIVATE);
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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in Getting  Menuitem stock Avl details  error code :  "+errorCode,Toast.LENGTH_LONG).show();

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
                                        SharedPreferences preferences =getSharedPreferences("MenuItemStockAvlDetails",Context.MODE_PRIVATE);
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
                params.put("storeid",vendorKey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);

        return completemenuItemStockAvlDetails;
    }

    private void saveMenuItemStockAvlDetailsinSharedPreference(List<Modal_MenuItemStockAvlDetails> menuItemStockAvlDetails) {
        if(localDBcheck){

        }
        else {
            try {
                final SharedPreferences sharedPreferences = getSharedPreferences("MenuItemStockAvlDetails", MODE_PRIVATE);

                Gson gson = new Gson();
                String json = gson.toJson(menuItemStockAvlDetails);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MenuItemStockAvlDetails", json);
                editor.apply();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    public void goToNotificationSettings() {

        String packageName = getPackageName();

        try {
            Intent intent = new Intent();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {

                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, packageName);
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {

                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra("android.provider.extra.APP_PACKAGE", packageName);

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", packageName);
                intent.putExtra("app_uid", getApplicationInfo().uid);

            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {

                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + packageName));

            } else {
                return;
            }

            startActivity(intent);

        } catch (Exception e) {
            // log goes here

        }

    }

    private void getDatafromMobileApp() {
//api_GetPOSMobileAppData
        //api_GetTestDataForPOSMobileApp
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetPOSMobileAppData, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                       // Log.d(Constants.TAG, "Elasticache  response Time of POS Mobile App table: " + getDate_and_time());

                        try {
                            String jsonString =response.toString();
                            //Log.d(Constants.TAG, " response: onMobileAppData " + response);
                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray JArray  = jsonObject.getJSONArray("content");
                            ////Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1=0;
                            int arrayLength = JArray.length();
                            ////Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for(;i1<(arrayLength);i1++) {
                                Modal_PosAppMobileData modal_posAppMobileData = new Modal_PosAppMobileData();
                                try {
                                    JSONObject json = JArray.getJSONObject(i1);

                                    try{
                                        orderdetailsnewschema = (json.getBoolean("orderdetailsnewschema"));

                                        updateweightforonlineorders = (json.getBoolean("updateweightforonlineorders"));
                                        modal_posAppMobileData.setOrderdetailsnewschema(String.valueOf(orderdetailsnewschema));
                                        modal_posAppMobileData.setUpdateweightforonlineorders(String.valueOf(updateweightforonlineorders));
                                       //updateweightforonlineorders = true;
                                      // orderdetailsnewschema = false;
                                        saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema,updateweightforonlineorders);




                                    }
                                    catch (Exception e){
                                       // updateweightforonlineorders = false;

                                        saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(isinventorycheck,minimumscreensizeforpos,orderdetailsnewschema, updateweightforonlineorders);
                                        e.printStackTrace();
                                    }

                                    
                                    JSONArray array  = json.getJSONArray("redeemdata ");

                                    for(int i=0; i < array.length(); i++) {
                                        JSONObject redeemdata_json = array.getJSONObject(i);
                                        String maxpointsinaday = redeemdata_json.getString("maxpointsinaday");
                                        String minordervalueforredeem = redeemdata_json.getString("minordervalueforredeem");
                                        String pointsfor100rs = redeemdata_json.getString("pointsfor100rs");
                                        modal_posAppMobileData.setRedeemdata_maxpointsinaday(String.valueOf(maxpointsinaday));
                                        modal_posAppMobileData.setRedeemdata_minordervalueforredeem(String.valueOf(minordervalueforredeem));
                                        modal_posAppMobileData.setRedeemdata_pointsfor100rs(String.valueOf(pointsfor100rs));



                                        //Log.d("Constants.TAG", "maxpointsinaday Response: " + maxpointsinaday);
                                        //Log.d("Constants.TAG", "minordervalueforredeem Response: " + minordervalueforredeem);
                                        //Log.d("Constants.TAG", "pointsfor100rs Response: " + pointsfor100rs);
                                        saveredeemDetailsinSharePreferences(maxpointsinaday,minordervalueforredeem,pointsfor100rs);
                                     //   AlertDialogClass.showDialog(MobileScreen_Dashboard.this, Constants.Order_Value_should_be_above+" "+minordervalueforredeem+" rs",0);

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


                                        modal_posAppMobileData.setAppacessdetails_admin(String.valueOf(admin_role));
                                        modal_posAppMobileData.setAppacessdetails_cashier(String.valueOf(cashier_role));
                                        modal_posAppMobileData.setAppacessdetails_deliverymanager(String.valueOf(deliverymanager_role));
                                        modal_posAppMobileData.setAppacessdetails_reportsviewer(String.valueOf(reportsviewer_role));
                                        modal_posAppMobileData.setAppacessdetails_storemanager(String.valueOf(storemanager_role));

                                        savepartnerappacessdetailsinSharePreferences(admin_role,cashier_role,deliverymanager_role,reportsviewer_role,storemanager_role);

                                    }
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                                if (localDBcheck) {
                                    try {

                                        if (posAppMobileDataSQL_db_manager == null) {

                                            posAppMobileDataSQL_db_manager = new PosAppMobileDataSQL_DB_Manager(getApplicationContext());
                                            try {
                                                posAppMobileDataSQL_db_manager.open();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                        try {

                                            posAppMobileDataSQL_db_manager.insert(modal_posAppMobileData);


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (posAppMobileDataSQL_db_manager != null) {
                                            posAppMobileDataSQL_db_manager.close();
                                            posAppMobileDataSQL_db_manager = null;
                                        }
                                    }
                                }

                                if(isAnyOnlineSyncNeeded) {
                                    if(!isOnlineSyncNeededForMenu) {
                                        saveMenuIteminSharedPreference(MenuList);
                                        saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                                        String MenuList_String = new Gson().toJson(MenuList);
                                        completemenuItem = MenuList_String;
                                    }
                                }


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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in General App Data code :  "+errorCode,Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);





    }

    private void saveorderdetailsnewschemainSharedPreferences(boolean orderdetailsnewschema) {
        final SharedPreferences sharedPreferences = getSharedPreferences("orderdetailsnewschema", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("orderdetailsnewschema", orderdetailsnewschema);

        editor.apply();


    }


    private void getDeliveryPartnerList() {
        SharedPreferences preferences =getSharedPreferences("DeliveryPersonList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryPartnerList+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //converting jsonSTRING into array
                           String DeliveryPersonListString = response.toString();
                            SharedPreferences sharedPreferences
                                    = getSharedPreferences("DeliveryPersonList",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor myEdit
                                    = sharedPreferences.edit();


                            myEdit.putString(
                                    "DeliveryPersonListString",
                                    DeliveryPersonListString);
                            myEdit.apply();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                SharedPreferences preferences =getSharedPreferences("DeliveryPersonList",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in Delivery Partner list :  "+errorCode,Toast.LENGTH_LONG).show();

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
                params.put("vendorkey", vendorKey);
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
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);
    }




    private void getMenuItemWeightDetails() {

        MenuItemWeightDetails.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetMenuItemWeightDetails, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //Log.d(Constants.TAG, " response: onMobileAppData " + response);
                            JSONArray JArray = response.getJSONArray("content");

                            int arrayLength = JArray.length();
                            for(int i =0; i<arrayLength;i++){
                                JSONObject json = JArray.getJSONObject(i);

                                Modal_MenuItemWeightDetails modal_menuItemWeightDetails = new Modal_MenuItemWeightDetails();
                                try {
                                    if (json.has("key")) {
                                        modal_menuItemWeightDetails.weightkey = (json.getString("key"));
                                    } else {
                                        modal_menuItemWeightDetails.weightkey = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemWeightDetails.weightkey= "";

                                    e.printStackTrace();
                                }

                                try {
                                    if (json.has("isdefault")) {
                                        modal_menuItemWeightDetails.isdefault=("false");
                                    } else {
                                        modal_menuItemWeightDetails.isdefault=("false");
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    modal_menuItemWeightDetails.isdefault=("false");

                                }


                                try {
                                    if (json.has("portionsize")) {
                                        modal_menuItemWeightDetails.portionsize=(json.getString("portionsize"));
                                    } else {
                                        modal_menuItemWeightDetails.portionsize = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemWeightDetails.portionsize = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("netweight")) {
                                        modal_menuItemWeightDetails.netweight=(json.getString("netweight"));
                                    } else {
                                        modal_menuItemWeightDetails.netweight = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemWeightDetails.netweight = "";

                                    e.printStackTrace();
                                }

                                try {
                                    if (json.has("grossweight")) {
                                        modal_menuItemWeightDetails.grossweight=(json.getString("grossweight"));
                                    } else {
                                        modal_menuItemWeightDetails.grossweight = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemWeightDetails.grossweight = "";

                                    e.printStackTrace();
                                }

                                try {
                                    if (json.has("grossweightingrams")) {
                                        modal_menuItemWeightDetails.grossweightingrams=(json.getString("grossweightingrams"));
                                    } else {
                                        modal_menuItemWeightDetails.grossweightingrams = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemWeightDetails.grossweightingrams = "";

                                    e.printStackTrace();
                                }



                                try {
                                    if (json.has("displayno")) {
                                        modal_menuItemWeightDetails.weightdisplayno = (json.getString("displayno"));
                                    } else {
                                        modal_menuItemWeightDetails.weightdisplayno = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemWeightDetails.weightdisplayno = "";

                                    e.printStackTrace();
                                }



                                try {
                                    if (json.has("weight")) {
                                        modal_menuItemWeightDetails.weight=(json.getString("weight"));
                                    } else {
                                        modal_menuItemWeightDetails.weight = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemWeightDetails.weight = "";

                                    e.printStackTrace();
                                }





                                MenuItemWeightDetails.add(modal_menuItemWeightDetails);
                            }

                            Collections.sort(MenuItemWeightDetails, new Comparator<Modal_MenuItemWeightDetails>() {
                                public int compare(final Modal_MenuItemWeightDetails object1, final Modal_MenuItemWeightDetails object2) {
                                    String displayno1 = object1.getWeightdisplayno();
                                    String displayno2 = object2.getWeightdisplayno();

                                    if((displayno1.equals(""))||(displayno1.equals("null"))||(displayno1.equals(null))){
                                        displayno1=String.valueOf(0);
                                    }
                                    if((displayno2.equals(""))||(displayno2.equals("null"))||(displayno2.equals(null))){
                                        displayno2=String.valueOf(0);
                                    }

                                    Long i2 = Long.valueOf(displayno2);
                                    Long i1 = Long.valueOf(displayno1);

                                    return i1.compareTo(i2);
                                }
                            });


                            String menuItemWeightDetailsString = new Gson().toJson(MenuItemWeightDetails);
                            SharedPreferences sharedPreferences
                                    = getSharedPreferences("MenuItemWeightDetails",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor myEdit
                                    = sharedPreferences.edit();


                            myEdit.putString(
                                    "MenuItemWeightDetailsString",
                                    menuItemWeightDetailsString);
                            myEdit.apply();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                SharedPreferences preferences =getSharedPreferences("MenuItemWeightDetails",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in General App Data code :  "+errorCode,Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);







    }
    private void getMenuItemCutDetails() {
        menuItemCutDetailsArray.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetMenuItemCutDetails, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //Log.d(Constants.TAG, " response: onMobileAppData " + response);
                            JSONArray JArray = response.getJSONArray("content");

                            int arrayLength = JArray.length();
                            for(int i =0; i<arrayLength;i++){
                                JSONObject json = JArray.getJSONObject(i);

                                Modal_MenuItemCutDetails modal_menuItemCutDetails = new Modal_MenuItemCutDetails();
                                try {
                                    if (json.has("key")) {
                                        modal_menuItemCutDetails.cutkey = (json.getString("key"));
                                    } else {
                                        modal_menuItemCutDetails.cutkey = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.cutkey = "";

                                    e.printStackTrace();
                                }

                                try {
                                    if (json.has("isdefault")) {
                                        modal_menuItemCutDetails.isdefault=("false");
                                    } else {
                                        modal_menuItemCutDetails.isdefault=("false");
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    modal_menuItemCutDetails.isdefault=("false");

                                }


                                try {
                                    if (json.has("portionsize")) {
                                        modal_menuItemCutDetails.portionsize=(json.getString("portionsize"));
                                    } else {
                                        modal_menuItemCutDetails.portionsize = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.portionsize = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("netweight")) {
                                        modal_menuItemCutDetails.netweight=(json.getString("netweight"));
                                    } else {
                                        modal_menuItemCutDetails.netweight = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.netweight = "";

                                    e.printStackTrace();
                                }

                                try {
                                    if (json.has("grossweight")) {
                                        modal_menuItemCutDetails.grossweight=(json.getString("grossweight"));
                                    } else {
                                        modal_menuItemCutDetails.grossweight = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.grossweight = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("desp")) {
                                        modal_menuItemCutDetails.cutdesp = (json.getString("desp"));

                                    } else {
                                        modal_menuItemCutDetails.cutdesp = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.cutdesp = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("cutname")) {
                                        modal_menuItemCutDetails.cutname = (json.getString("cutname"));

                                    } else {
                                        modal_menuItemCutDetails.cutname = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.cutname = "";

                                    e.printStackTrace();
                                }

                                try {
                                    if (json.has("cutimagename")) {
                                        modal_menuItemCutDetails.cutimagename = (json.getString("cutimagename"));
                                    } else {
                                        modal_menuItemCutDetails.cutimagename = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.cutimagename = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("displayno")) {
                                        modal_menuItemCutDetails.cutdisplayno = (json.getString("displayno"));
                                    } else {
                                        modal_menuItemCutDetails.cutdisplayno = "";
                                    }
                                }
                                catch (Exception e){
                                    modal_menuItemCutDetails.cutdisplayno = "";

                                    e.printStackTrace();
                                }




                                menuItemCutDetailsArray.add(modal_menuItemCutDetails);
                            }


                            Collections.sort(menuItemCutDetailsArray, new Comparator<Modal_MenuItemCutDetails>() {
                                public int compare(final Modal_MenuItemCutDetails object1, final Modal_MenuItemCutDetails object2) {
                                    String displayno1 = object1.getCutdisplayno();
                                    String displayno2 = object2.getCutdisplayno();

                                    if((displayno1.equals(""))||(displayno1.equals("null"))||(displayno1.equals(null))){
                                        displayno1=String.valueOf(0);
                                    }
                                    if((displayno2.equals(""))||(displayno2.equals("null"))||(displayno2.equals(null))){
                                        displayno2=String.valueOf(0);
                                    }

                                    Long i2 = Long.valueOf(displayno2);
                                    Long i1 = Long.valueOf(displayno1);

                                    return i1.compareTo(i2);
                                }
                            });


                            String menuItemCutDetailsString = new Gson().toJson(menuItemCutDetailsArray);
                            SharedPreferences sharedPreferences
                                    = getSharedPreferences("MenuItemCutDetails",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor myEdit
                                    = sharedPreferences.edit();


                            myEdit.putString(
                                    "MenuItemCutDetailsString",
                                    menuItemCutDetailsString);
                            myEdit.apply();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                SharedPreferences preferences =getSharedPreferences("MenuItemCutDetails",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in General App Data code :  "+errorCode,Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);





    }






    private String getMenuItemusingStoreId(String vendorKey) {
        //Log.d(TAG, "starting:getfullMenuItemUsingStoreID ");
        completemenuItem="";
        MenuList.clear();
        SharedPreferences preferences =getSharedPreferences("MenuList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        //api_getListofMenuItems   ?storeid
        //api_GetTestDataFromWriteRedisFromDynamoDB ?vendorkey
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItems+"?storeid="+vendorKey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                Log.d(Constants.TAG, "Fetch menuitem from SQL " );

                try{
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
                try{
                    //Log.d(TAG, "starting:onResponse ");

                    //Log.d(TAG, "response for addMenuListAdaptertoListView: " + response.length());

                    try {
                       // completemenuItem = new String(String.valueOf(response));
                    //    Log.d(Constants.TAG, "Elasticache response Time of Menu item table: " + getDate_and_time());

                        JSONArray JArray = response.getJSONArray("content");
                   //    completemenuItem = new String(String.valueOf(JArray));

                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                        for (; i1 < (arrayLength); i1++) {
                            boolean showinPOS = false;

                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_MenuItem modal_menuItem = new Modal_MenuItem();

                                try {
                                    if (json.has("showinpos")) {
                                        String showinPOS_String = "";
                                        showinPOS_String = String.valueOf(json.get("showinpos").toString().toUpperCase());
                                        if (showinPOS_String.equals("")) {
                                            showinPOS = true;

                                        }
                                        else if (showinPOS_String.equals("TRUE")) {
                                            showinPOS = true;
                                        }
                                        else if (showinPOS_String.equals("FALSE")) {
                                            showinPOS = false;

                                        }
                                        else if (showinPOS_String.equals("NULL")) {
                                            showinPOS = true;
                                        }
                                    } else {
                                        showinPOS = true;
                                        //Log.d(Constants.TAG, "There is no showinpos for this Menu: " + MenuItemKey);


                                    }
                                }
                                catch (Exception e){
                                    showinPOS = true;

                                    e.printStackTrace();
                                }

                                if (showinPOS) {

                                    if (json.has("key")) {
                                        modal_menuItem.key = String.valueOf(json.get("key"));
                                        MenuItemKey = String.valueOf(json.get("key"));
                                    } else {
                                        modal_menuItem.key = "";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }
                                    if (json.has("appmarkuppercentage")) {
                                        modal_menuItem.appmarkuppercentage = String.valueOf(json.get("appmarkuppercentage"));
                                    } else {
                                        modal_menuItem.appmarkuppercentage = "0";
                                        ////Log.d(Constants.TAG, "There is no key for this Menu: " );


                                    }


                                    if (json.has("applieddiscountpercentage")) {
                                        modal_menuItem.applieddiscountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                                    } else {
                                        modal_menuItem.applieddiscountpercentage = "0";
                                        //Log.d(Constants.TAG, "There is no applieddiscountpercentage for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("showinapp")) {
                                        modal_menuItem.showinapp = String.valueOf(json.get("showinapp").toString().toUpperCase());

                                    } else {
                                        modal_menuItem.showinapp = "TRUE";
                                        //Log.d(Constants.TAG, "There is no showinapp for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("barcode")) {
                                        modal_menuItem.barcode = String.valueOf(json.get("barcode"));

                                    } else {
                                        modal_menuItem.barcode = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }



                                    if (json.has("marinadelinkedcodes")) {
                                        modal_menuItem.marinadelinkedcodes = String.valueOf(json.get("marinadelinkedcodes"));

                                    } else {
                                        modal_menuItem.marinadelinkedcodes = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("vendorkey")) {
                                        modal_menuItem.vendorkey = String.valueOf(json.get("vendorkey"));

                                    } else {
                                        modal_menuItem.vendorkey = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("imageurl")) {
                                        modal_menuItem.imageurl = String.valueOf(json.get("imageurl"));

                                    } else {
                                        modal_menuItem.imageurl = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("menutype")) {
                                        modal_menuItem.menutype = String.valueOf(json.get("menutype"));

                                    } else {
                                        modal_menuItem.menutype = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }


                                    if (json.has("itemcalories")) {
                                        modal_menuItem.itemcalories = String.valueOf(json.get("itemcalories"));

                                    } else {
                                        modal_menuItem.itemcalories = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }

                                    if (json.has("vendorname")) {
                                        modal_menuItem.vendorname = String.valueOf(json.get("vendorname"));

                                    } else {
                                        modal_menuItem.vendorname = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }

                                    if (json.has("checkoutimageurl")) {
                                        modal_menuItem.checkoutimageurl = String.valueOf(json.get("checkoutimageurl"));

                                    } else {
                                        modal_menuItem.checkoutimageurl = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }




                                    if (json.has("swiggyprice")) {
                                        modal_menuItem.swiggyprice = String.valueOf(json.get("swiggyprice"));
                                        if (String.valueOf(json.get("swiggyprice")).contains("\r")) {

                                            modal_menuItem.swiggyprice = String.valueOf(json.get("swiggyprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getSwiggyprice()).equals("")) {
                                            modal_menuItem.swiggyprice = "0";

                                        }


                                    } else {
                                        modal_menuItem.swiggyprice = "0";
                                        //Log.d(Constants.TAG, "There is no swiggyprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("bigbasketprice")) {
                                        modal_menuItem.bigbasketprice = String.valueOf(json.get("bigbasketprice"));
                                        if (String.valueOf(json.get("bigbasketprice")).contains("\r")) {

                                            modal_menuItem.bigbasketprice = String.valueOf(json.get("bigbasketprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getBigbasketprice()).equals("")) {
                                            modal_menuItem.bigbasketprice = "0";

                                        }
                                    } else {
                                        modal_menuItem.bigbasketprice = "0";
                                        //Log.d(Constants.TAG, "There is no bigbasketprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("dunzoprice")) {
                                        modal_menuItem.dunzoprice = String.valueOf(json.get("dunzoprice"));
                                        if (String.valueOf(json.get("dunzoprice")).contains("\r")) {

                                            modal_menuItem.dunzoprice = String.valueOf(json.get("dunzoprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getDunzoprice()).equals("")) {
                                            modal_menuItem.dunzoprice = "0";

                                        }
                                    } else {
                                        modal_menuItem.dunzoprice = "0";
                                        //Log.d(Constants.TAG, "There is no dunzoprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("wholesaleprice")) {
                                        modal_menuItem.wholesaleprice = String.valueOf(json.get("wholesaleprice"));
                                        if (String.valueOf(json.get("wholesaleprice")).contains("\r")) {

                                            modal_menuItem.wholesaleprice = String.valueOf(json.get("wholesaleprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getWholesaleprice()).equals("")) {
                                            modal_menuItem.wholesaleprice = "0";

                                        }
                                    } else {
                                        modal_menuItem.wholesaleprice = "0";
                                        //Log.d(Constants.TAG, "There is no wholesaleprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("displayno")) {
                                        modal_menuItem.displayno = String.valueOf(json.get("displayno"));

                                    } else {
                                        modal_menuItem.displayno = "";
                                        //Log.d(Constants.TAG, "There is no displayno for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("gstpercentage")) {
                                        modal_menuItem.gstpercentage = String.valueOf(json.get("gstpercentage"));

                                    } else {
                                        modal_menuItem.gstpercentage = "";
                                        //Log.d(Constants.TAG, "There is no gstpercentage for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("itemavailability")) {
                                        modal_menuItem.itemavailability = String.valueOf(json.get("itemavailability"));

                                    } else {
                                        modal_menuItem.itemavailability = "";
                                        //Log.d(Constants.TAG, "There is no itemavailability for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("itemname")) {
                                        modal_menuItem.itemname = String.valueOf(json.get("itemname"));

                                    } else {
                                        modal_menuItem.itemname = "";
                                        //Log.d(Constants.TAG, "There is no ItemName for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("reportname")) {
                                        modal_menuItem.reportname = String.valueOf(json.get("reportname"));

                                    } else {
                                        modal_menuItem.reportname = "";
                                        ////Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " +MenuItemKey );


                                    }

                                    if (json.has("pricetypeforpos")) {
                                        modal_menuItem.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                                    } else {
                                        modal_menuItem.pricetypeforpos = "";
                                        //Log.d(Constants.TAG, "There is no pricetypeforpos for this Menu: " + MenuItemKey);


                                    }



                                    ////// for TMCPrice
                                    try{
                                        double tmcprice_double =0 ,  tmcpriceWithAppMarkupValue = 0 , appMarkupPercn_value =0;
                                        try {
                                            if (json.has("tmcprice")) {
                                                tmcprice_double = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                                            } else {
                                                tmcprice_double = 0;
                                                ////Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            tmcprice_double =0;
                                        }
                                        if(json.has("appmarkuppercentage")) {
                                            int markupPercentageInt = 0;
                                            try {
                                                markupPercentageInt = Integer.parseInt(String.valueOf(json.get("appmarkuppercentage")));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (markupPercentageInt > 0) {

                                                try {
                                                    appMarkupPercn_value = (markupPercentageInt * tmcprice_double) / 100;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    tmcpriceWithAppMarkupValue = appMarkupPercn_value + tmcprice_double;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                modal_menuItem.tmcpriceWithMarkupValue = String.valueOf((int) Double.parseDouble(String.valueOf(Math.ceil(tmcpriceWithAppMarkupValue))));
                                            }
                                            else{
                                                modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);

                                            }

                                        }
                                        else{
                                            modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);

                                        }




                                        try{

                                            if(tmcpriceWithAppMarkupValue<=0){
                                                tmcprice_double = 0;
                                                tmcprice_double = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                                                modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);
                                            }

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }


                                    }
                                    catch (Exception e){

                                        try {

                                            double tmcprice_double = 0;
                                            tmcprice_double = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                                            modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);
                                        }
                                        catch (Exception er) {
                                            Toast.makeText(getApplicationContext(),"Error 1 in getting tmcprice_double entry on dashboard",Toast.LENGTH_LONG).show();

                                            er.printStackTrace();
                                        }
                                        Toast.makeText(getApplicationContext(),"Error 2 in getting tmcprice_double entry on dashboard",Toast.LENGTH_LONG).show();

                                        e.printStackTrace();
                                    }






                                    ////// for TMCPrice PerKG

                                    try{
                                        double tmcpriceperkg_double =0 ,  tmcpriceperkgWithAppMarkupValue = 0 , appMarkupPercn_value =0;
                                        try {
                                            if (json.has("tmcpriceperkg")) {
                                                tmcpriceperkg_double = Double.parseDouble(String.valueOf(json.get("tmcpriceperkg")));

                                            } else {
                                                tmcpriceperkg_double = 0;
                                                ////Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            tmcpriceperkg_double =0;
                                        }
                                        if(json.has("appmarkuppercentage")) {
                                            int markupPercentageInt = 0;
                                            try {
                                                markupPercentageInt = Integer.parseInt(String.valueOf(json.get("appmarkuppercentage")));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (markupPercentageInt > 0) {

                                                try {
                                                    appMarkupPercn_value = (markupPercentageInt * tmcpriceperkg_double) / 100;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    tmcpriceperkgWithAppMarkupValue = appMarkupPercn_value + tmcpriceperkg_double;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf((int) Double.parseDouble(String.valueOf(Math.round(tmcpriceperkgWithAppMarkupValue))));
                                            }
                                            else{
                                                modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);

                                            }
                                        }
                                        else{
                                            modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);

                                        }




                                        try{

                                            if(tmcpriceperkgWithAppMarkupValue<=0){
                                                tmcpriceperkg_double = 0;
                                                tmcpriceperkg_double = Double.parseDouble(String.valueOf(json.get("tmcpriceperkg")));

                                                modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);
                                            }

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                    catch (Exception e){
                                        try {
                                            double tmcpriceperkg_double = 0;
                                            tmcpriceperkg_double = Double.parseDouble(String.valueOf(json.get("tmcpriceperkg")));
                                            modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);
                                        }
                                        catch (Exception er){
                                            Toast.makeText(getApplicationContext(),"Error 1 in getting tmcpriceperkg_double entry on dashboard",Toast.LENGTH_LONG).show();

                                            er.printStackTrace();
                                        }

                                        Toast.makeText(getApplicationContext(),"Error 2 in getting tmcpriceperkg_double entry on dashboard",Toast.LENGTH_LONG).show();


                                        e.printStackTrace();

                                    }





                                    if (json.has("itemuniquecode")) {
                                        modal_menuItem.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                                    } else {
                                        modal_menuItem.itemuniquecode = "";
                                        //Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("menuboarddisplayname")) {
                                        modal_menuItem.menuboarddisplayname = String.valueOf(json.get("menuboarddisplayname"));

                                    } else {
                                        modal_menuItem.menuboarddisplayname = "";
                                        //Log.d(Constants.TAG, "There is no menuboarddisplayname for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("showinmenuboard")) {
                                        modal_menuItem.showinmenuboard = String.valueOf(json.get("showinmenuboard"));

                                    } else {
                                        modal_menuItem.showinmenuboard = "";
                                        //Log.d(Constants.TAG, "There is no showinmenuboard for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("grossweight")) {
                                        modal_menuItem.grossweight = String.valueOf(json.get("grossweight"));

                                    } else {
                                        modal_menuItem.grossweight = "";
                                        //Log.d(Constants.TAG, "There is no grossweight for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("grossweightingrams")) {
                                        modal_menuItem.grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                                    } else {
                                        modal_menuItem.grossweightingrams = "";
                                        //Log.d(Constants.TAG, "There is no grossweightingrams for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("netweight")) {
                                        modal_menuItem.netweight = String.valueOf(json.get("netweight"));

                                    } else {
                                        modal_menuItem.netweight = "";
                                        //Log.d(Constants.TAG, "There is no netweight for this Menu: ");


                                    }
                                    if (json.has("portionsize")) {
                                        modal_menuItem.portionsize = String.valueOf(json.get("portionsize"));

                                    } else {
                                        modal_menuItem.portionsize = "";
                                        //Log.d(Constants.TAG, "There is no portionsize for this Menu: ");


                                    }


                                    if (json.has("tmcctgykey")) {
                                        modal_menuItem.tmcctgykey = String.valueOf(json.get("tmcctgykey"));

                                    } else {
                                        modal_menuItem.tmcctgykey = "";
                                        //Log.d(Constants.TAG, "There is no tmcctgykey for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("tmcprice")) {
                                        modal_menuItem.tmcprice = String.valueOf(json.get("tmcprice"));

                                    } else {
                                        modal_menuItem.tmcprice = "";
                                        //Log.d(Constants.TAG, "There is no tmcprice for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("tmcpriceperkg")) {
                                        modal_menuItem.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));

                                    } else {
                                        modal_menuItem.tmcpriceperkg = "";
                                        //Log.d(Constants.TAG, "There is no tmcpriceperkg for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("tmcsubctgykey")) {
                                        modal_menuItem.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));

                                    } else {
                                        modal_menuItem.tmcsubctgykey = "";
                                        //Log.d(Constants.TAG, "There is no tmcsubctgykey for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("key")) {
                                        modal_menuItem.menuItemId = String.valueOf(json.get("key"));

                                    } else {
                                        modal_menuItem.menuItemId = "";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }

                                    if (json.has("itemweightdetails")) {
                                        try {
                                            modal_menuItem.itemweightdetails = String.valueOf(json.get("itemweightdetails"));

                                        } catch (Exception e) {
                                            modal_menuItem.itemweightdetails = "nil";

                                            e.printStackTrace();
                                        }

                                    } else {
                                        modal_menuItem.itemweightdetails = "nil";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }


                                    if (json.has("itemcutdetails")) {
                                        try {
                                            modal_menuItem.itemcutdetails = String.valueOf(json.get("itemcutdetails"));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_menuItem.itemcutdetails = "nil";

                                        }

                                    } else {
                                        modal_menuItem.itemcutdetails = "nil";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }

                                    if (json.has("inventorydetails")) {
                                        try {
                                            modal_menuItem.inventorydetails = String.valueOf(json.get("inventorydetails"));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_menuItem.inventorydetails = "nil";

                                        }

                                    } else {
                                        modal_menuItem.inventorydetails = "nil";
                                        //Log.d(Constants.TAG, "There is no inventorydetails for this Menu: ");


                                    }

                                        String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                                receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                                        modal_menuItem.setBarcode_AvlDetails(barcode_AvlDetails);
                                        modal_menuItem.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                        modal_menuItem.setKey_AvlDetails(key_AvlDetails);
                                        modal_menuItem.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                        modal_menuItem.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                        modal_menuItem.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                        modal_menuItem.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                        modal_menuItem.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                        modal_menuItem.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                        modal_menuItem.setAllownegativestock(allownegativestock_AvlDetails);

                                    if(modal_menuItem.itemuniquecode.equals("1407")){
                                        Log.i(" cursor count  ::  ", String.valueOf(modal_menuItem.getTmcpriceperkgWithMarkupValue()));
                                    }



                                   // localDBcheck = false;
                                    try {
                                        if (localDBcheck) {
                                            try {
                                                if (tmcMenuItemSQL_db_manager == null) {
                                                    tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(getApplicationContext());
                                                    try {
                                                        tmcMenuItemSQL_db_manager.open();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                long id = tmcMenuItemSQL_db_manager.insert(modal_menuItem);
                                                modal_menuItem.setLocalDB_id(String.valueOf(id));
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            finally {
                                                try {
                                                    if(arrayLength - i1 == 1) {
                                                        if (tmcMenuItemSQL_db_manager != null) {
                                                            tmcMenuItemSQL_db_manager.close();
                                                            tmcMenuItemSQL_db_manager = null;
                                                        }
                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }

                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    MenuList.add(modal_menuItem);

                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                //Log.d(Constants.TAG, "e: " + e.getMessage());
                                //Log.d(Constants.TAG, "e: " + e.toString());

                            }

                            if(arrayLength - i1 ==1){
                                if(isinventorycheck) {
                                    completemenuItem = getMenuAvlDetailsUsingVendorkey(vendorKey);
                                }
                                else{
                                    saveMenuIteminSharedPreference(MenuList);
                                    saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);
                                    String MenuList_String = new Gson().toJson(MenuList);
                                    completemenuItem = MenuList_String;
                                }
                                if(localDBcheck) {
                                    SharedPreferences sharedPreferences
                                            = getSharedPreferences("SqlDbSyncDetails",
                                            MODE_PRIVATE);

                                    SharedPreferences.Editor myEdit
                                            = sharedPreferences.edit();
                                      myEdit.putString(
                                            "menuitem_SqlDb_SyncTime",
                                            menuitemupdationtime
                                    );
                                    myEdit.putString(
                                            "posappdataSqlDb_SyncTime",
                                            posappdataupdationtime
                                    );
                                    myEdit.putString(
                                            "vendordetailsSqlDb_SyncTime",
                                            vendordetailsupdationtime
                                    );


                                    myEdit.apply();

                                }

                            }
                        }

                        //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in Getting  Menu Item error code :  "+errorCode,Toast.LENGTH_LONG).show();

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



    private String  getMenuAvlDetailsUsingVendorkey(String vendorKey) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItemStockAvlDetails+vendorKey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{

                    JSONArray JArray = response.getJSONArray("content");




                    for(int i =0; i<MenuList.size();i++) {
                        Modal_MenuItem modal_menuItem = MenuList.get(i);
                        String menuitemKeyFromArray ="";
                        menuitemKeyFromArray = modal_menuItem.getKey();



                        String barcode_AvlDetails ="",itemavailability_AvlDetails="",key_AvlDetails="",lastupdatedtime_AvlDetails="",menuitemkey_AvlDetails="",
                                receivedstock_AvlDetails="",stockbalance_AvlDetails="",stockincomingkey_AvlDetails="",vendorkey_AvlDetails="",allownegativestock_AvlDetails="";



                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                        for (; i1 < (arrayLength); i1++) {


                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                if(json.has("menuitemkey")){
                                    String menuItemKeyFromMenuAvlDetails = json.getString("menuitemkey");

                                    if(menuItemKeyFromMenuAvlDetails.equals(menuitemKeyFromArray)){
                                        if(json.has("barcode")){
                                             barcode_AvlDetails = json.getString("barcode");



                                        }
                                        else{
                                            barcode_AvlDetails ="";
                                        }

                                        if(json.has("itemavailability")){
                                            itemavailability_AvlDetails = json.getString("itemavailability");



                                        }
                                        else{
                                            itemavailability_AvlDetails ="";
                                        }


                                        if(json.has("key")){
                                            key_AvlDetails = json.getString("key");



                                        }
                                        else{
                                            key_AvlDetails ="";
                                        }



                                        if(json.has("lastupdatedtime")){
                                            lastupdatedtime_AvlDetails = json.getString("lastupdatedtime");



                                        }
                                        else{
                                            lastupdatedtime_AvlDetails ="";
                                        }


                                        if(json.has("menuitemkey")){
                                            menuitemkey_AvlDetails = json.getString("menuitemkey");



                                        }
                                        else{
                                            menuitemkey_AvlDetails ="";
                                        }


                                        if(json.has("receivedstock")){
                                            receivedstock_AvlDetails = json.getString("receivedstock");



                                        }
                                        else{
                                            receivedstock_AvlDetails ="";
                                        }


                                        if(json.has("stockbalance")){
                                            stockbalance_AvlDetails = json.getString("stockbalance");



                                        }
                                        else{
                                            stockbalance_AvlDetails ="";
                                        }


                                        if(json.has("stockincomingkey")){
                                            stockincomingkey_AvlDetails = json.getString("stockincomingkey");



                                        }
                                        else{
                                            stockincomingkey_AvlDetails ="";
                                        }


                                        if(json.has("vendorkey")){
                                            vendorkey_AvlDetails = json.getString("vendorkey");



                                        }
                                        else{
                                            vendorkey_AvlDetails ="";
                                        }

                                        if(json.has("allownegativestock")){
                                            allownegativestock_AvlDetails = json.getString("allownegativestock");



                                        }
                                        else{
                                            allownegativestock_AvlDetails ="";
                                        }

                                        MenuList.get(i).setBarcode_AvlDetails(barcode_AvlDetails);
                                        MenuList.get(i).setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                        MenuList.get(i).setKey_AvlDetails(key_AvlDetails);
                                        MenuList.get(i).setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                        MenuList.get(i).setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                        MenuList.get(i).setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                        MenuList.get(i).setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                        MenuList.get(i).setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                        MenuList.get(i).setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                        MenuList.get(i).setAllownegativestock(allownegativestock_AvlDetails);



                                        if(localDBcheck) {
                                            try {
                                                try {

                                                    if (tmcMenuItemSQL_db_manager == null) {
                                                        tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(getApplicationContext());
                                                        try {
                                                            tmcMenuItemSQL_db_manager.open();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    int result = tmcMenuItemSQL_db_manager.update(MenuList.get(i));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            finally {
                                                try {
                                                    if (MenuList.size() - i ==1){
                                                        if (tmcMenuItemSQL_db_manager != null) {
                                                            tmcMenuItemSQL_db_manager.close();
                                                            tmcMenuItemSQL_db_manager = null;
                                                        }
                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }


                                        Modal_MenuItemStockAvlDetails modal_menuItemStockAvlDetails = new Modal_MenuItemStockAvlDetails();
                                        modal_menuItemStockAvlDetails.setBarcode(barcode_AvlDetails);
                                        modal_menuItemStockAvlDetails.setItemavailability(itemavailability_AvlDetails);
                                        modal_menuItemStockAvlDetails.setKey(key_AvlDetails);
                                        modal_menuItemStockAvlDetails.setMenuitemkey(menuitemkey_AvlDetails);
                                        modal_menuItemStockAvlDetails.setStockincomingkey(stockincomingkey_AvlDetails);
                                        modal_menuItemStockAvlDetails.setVendorkey(vendorkey_AvlDetails);
                                        modal_menuItemStockAvlDetails.setStockbalance(stockbalance_AvlDetails);
                                        modal_menuItemStockAvlDetails.setAllownegativestock(allownegativestock_AvlDetails);
                                        modal_menuItemStockAvlDetails.setLastupdatedtime(lastupdatedtime_AvlDetails);
                                        modal_menuItemStockAvlDetails.setReceivedstock(receivedstock_AvlDetails);
                                        MenuItemStockAvlDetails.add(modal_menuItemStockAvlDetails);

                                    }
                                }
                                else{
                                    Toast.makeText(MobileScreen_Dashboard.this, "There is no menuItemkey for an Item", Toast.LENGTH_SHORT).show();

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }



                        if (MenuList.size() - i ==1){

                            if(localDBcheck) {

                                isOnlineSyncNeededForMenu = false;
                                if (isAnyOnlineSyncNeeded) {

                                    saveMenuIteminSharedPreference(MenuList);
                                    saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                                    String MenuList_String = new Gson().toJson(MenuList);
                                    completemenuItem = MenuList_String;
                                }
                            }
                            else{

                                saveMenuIteminSharedPreference(MenuList);
                                saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                                String MenuList_String = new Gson().toJson(MenuList);
                                completemenuItem = MenuList_String;
                            }

                        }


                    }

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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in Getting MenuList : error code :  "+errorCode,Toast.LENGTH_LONG).show();

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


    private String getMarinadeMenuItemusingStoreId(String vendorKey) {
        //Log.d(TAG, "starting:getfullMenuItemUsingStoreID ");
        completeMarinademenuItem="";
        MarinadeMenuList.clear();
        SharedPreferences preferences =getSharedPreferences("MarinadeMenuList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMarinadeMenuItems+"?storeid="+vendorKey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{
                    completeMarinademenuItem="";
                    MarinadeMenuList.clear();
                    SharedPreferences preferences =getSharedPreferences("MarinadeMenuList",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    //Log.d(TAG, "starting:onResponse ");

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
                                    modal_menuItem.applieddiscountpercentage = "0";
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
                Toast.makeText(MobileScreen_Dashboard.this,"Error in Getting Marinade Menu Item error code :  "+errorCode,Toast.LENGTH_LONG).show();
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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(MobileScreen_Dashboard.this).add(jsonObjectRequest);

        return "";
    }





    private void saveredeemDetailsinSharePreferences(String maxpointsinaday, String minordervalueforredeem, String pointsfor100rs) {
        final SharedPreferences sharedPreferences = getSharedPreferences("RedeemData", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("maxpointsinaday", maxpointsinaday);
        editor.putString("minordervalueforredeem", minordervalueforredeem);
        editor.putString("pointsfor100rs", pointsfor100rs);
        editor.putBoolean("fetchedindashboard", true);


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
       /* if(UserRole.equals(Constants.REPORTSVIEWER_ROLENAME)){
            loadMyFragment(new SettingsFragment());
            bottomNavigationView.setSelectedItemId(R.id.settings_navigatioBar_widget);
        }
        else{
            loadMyFragment(new Mobile_ManageOrders1());
            bottomNavigationView.setSelectedItemId(R.id.manage_order_navigatioBar_widget);

        }

        */

    }

    private void saveMarinadeMenuIteminSharedPreference(List<Modal_MenuItem> menuList) {
        try {
            final SharedPreferences sharedPreferences = getSharedPreferences("MarinadeMenuList", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(menuList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("MarinadeMenuList", json);
            editor.apply();
           // Log.i("Start time ",getDate_and_time());
            String time = getDate_and_time();
            System.out.println("1111111111End time "+time);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("111Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String CurrentDatee = df.format(c);
        String  CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss.SS",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



        String    FormattedTime = dfTime.format(c);
        String   formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }



    private void saveInventoryCodePermisionAndMinimumScreenSizeforPOSinSharedPreference(boolean isinventorycheck, String minimumscreensizeforpos, boolean isorderdetailsnewschema, boolean updateWeightforonlineorders) {

        SharedPreferences printerDatasharedPreferences
                = getSharedPreferences("PrinterConnectionData",
                MODE_PRIVATE);

        SharedPreferences.Editor printerDatamyEdit
                = printerDatasharedPreferences.edit();

        printerDatamyEdit.putString(
                "printerType",
                defaultprintertype);

        printerDatamyEdit.putString(
                "printerStatus",
                "Success");
        printerDatamyEdit.apply();




        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putBoolean(
                "inventoryCheckBool",
                isinventorycheck
        );


        myEdit.putString(
                "MinimumScreenSizeForPos",
                minimumscreensizeforpos
        );
        myEdit.putBoolean(
                "orderdetailsnewschema",
                orderdetailsnewschema
        );
        myEdit.putBoolean(
                "orderdetailsnewschema_settings",
                orderdetailsnewschema
        );

        myEdit.putBoolean(
                "updateweightforonlineorders",
                updateweightforonlineorders
        );
        myEdit.putBoolean(
                "localdbcheck",
                localDBcheck
        );

        myEdit.apply();

    }
    private void saveMenuIteminSharedPreference(List<Modal_MenuItem> menuList) {


        try {
            if(localDBcheck) {
                Gson gson = new Gson();
                String json = gson.toJson(menuList);
                completemenuItem = json;


            }
            else{


                final SharedPreferences sharedPreferences = getSharedPreferences("MenuList", MODE_PRIVATE);

                Gson gson = new Gson();
                String json = gson.toJson(menuList);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MenuList", json);
                editor.apply();
                completemenuItem = json;
            }

            isMenuListSavedLocally = true;
            if(UserRole.equals(Constants.REPORTSVIEWER_ROLENAME)){
               // CurrentFragment = new SettingsFragment();
              //  loadMyFragment(new SettingsFragment());
                bottomNavigationView.setSelectedItemId(R.id.settings_navigatioBar_widget);
            }
            else{

                if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)){
                  //  CurrentFragment = new NewOrderScreenFragment_mobile();

                  //  loadMyFragment(new NewOrderScreenFragment_mobile());
                    bottomNavigationView.setSelectedItemId(R.id.new_order_navigatioBar_widget);
                }
                else {
                   /* CurrentFragment = new Mobile_ManageOrders1();

                    loadMyFragment(new Mobile_ManageOrders1());
                    bottomNavigationView.setSelectedItemId(R.id.manage_order_navigatioBar_widget);

                    */


              //      CurrentFragment = new NewOrderScreenFragment_mobile();
                   /* mfragment = new NewOrderScreenFragment_mobile();
                    //loadMyFragment(mfragment);
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();

                    if (json.isEmpty()) {
                        transaction1.replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(completemenuItem),TAG);
                    } else {
                        transaction1.replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(json),TAG);

                    }




                    //transaction1.replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(completemenuItem));
                    transaction1.commit();

                    */
                    //loadMyFragment(new NewOrderScreenFragment_mobile());
                    bottomNavigationView.setSelectedItemId(R.id.manage_order_navigatioBar_widget);
                }
            }





            Adjusting_Widgets_Visibility(false);

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
    static String getDisplaySize(Activity activity) {
        double x = 0, y = 0;
        int mWidthPixels, mHeightPixels;
        try {
            WindowManager windowManager = activity.getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getMetrics(displayMetrics);
            Point realSize = new Point();
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
            mWidthPixels = realSize.x;
            mHeightPixels = realSize.y;
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            x = Math.pow(mWidthPixels / dm.xdpi, 2);
            y = Math.pow(mHeightPixels / dm.ydpi, 2);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return String.format(Locale.US, "%.2f", Math.sqrt(x + y));
    }

    private void SavePrinterConnectionDatainSharedPreferences() {
        SharedPreferences sharedPreferences
                = getSharedPreferences("PrinterConnectionData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "printerStatus",
                "");
        myEdit.putString(
                "printerName",
                "");
        myEdit.putBoolean(
                "isPrinterConnected",
                false);
        myEdit.apply();





    }




    public static long getLongValuefortheDate(String time) {
        long longvalue = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            Date date = sdf.parse(time);
            long time1long = date.getTime() / 1000;
            longvalue = (time1long);

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                //     Log.d(TAG, "time1long  "+orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);

                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                Date date = sdf.parse(time);
                long time1long = date.getTime() / 1000;
                longvalue = (time1long);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return longvalue;
    }





    private void loadMyFragment(Fragment fm) {
        if (fm != null) {
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
           // CurrentFragment = fm;

            if(fm.equals(new NewOrderScreenFragment_mobile())){
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, NewOrderScreenFragment_mobile.newInstance(completemenuItem) , TAG)

                        .addToBackStack(null)
                        .commit();

            }
            else{
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame, fm ,TAG)

                        .addToBackStack(null)
                        .commit();
            }

        }catch(Exception e){
            onResume();
            e.printStackTrace();
        }

    }
    }

    @Override
    protected void onDestroy() {


        try{
            if(localDBcheck) {

                if (tmcMenuItemSQL_db_manager != null) {
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }
                if (vendorSQL_db_manager != null) {
                    vendorSQL_db_manager.close();
                    vendorSQL_db_manager = null;
                }
                if (posAppMobileDataSQL_db_manager != null) {
                    posAppMobileDataSQL_db_manager.close();
                    posAppMobileDataSQL_db_manager = null;
                }
                if (tmcSubCtgyItemSQL_db_manager != null) {
                    tmcSubCtgyItemSQL_db_manager.close();
                    tmcSubCtgyItemSQL_db_manager = null;
                }

            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();


    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
           // tmcMenuItemSQL_db_manager.close();
          //  vendorSQL_db_manager.close();
          //  posAppMobileDataSQL_db_manager.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        try{
        ////    tmcMenuItemSQL_db_manager.close();
         //   vendorSQL_db_manager.close();
         //   posAppMobileDataSQL_db_manager.close();
        }
        catch (Exception e){
            e.printStackTrace();
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