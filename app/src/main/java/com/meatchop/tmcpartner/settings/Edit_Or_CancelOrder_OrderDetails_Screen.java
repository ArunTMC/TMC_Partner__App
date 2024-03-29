package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTableInterface;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.Adapter_Mobile_AssignDeliveryPartner1;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.Adapter_Mobile_orderDetails_itemDesp_listview1;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_vendor;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableService;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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

import static com.meatchop.tmcpartner.Constants.api_Update_MenuItemStockAvlDetails;

public class Edit_Or_CancelOrder_OrderDetails_Screen extends AppCompatActivity {
    static TextView tokenNotext_widget,mobileNotext_widget,ordertypetext_widget,orderplacedtime_textwidget,orderConfirmedtime_textwidget,orderReaytime_textwidget,orderpickeduptime_textwidget,orderDeliveredtime_textwidget,orderIdtext_widget,orderStatustext_widget,paymentTypetext_widget,slotNametext_widget,slotDatetext_widget
            ,deliveryPartner_name_widget,deliveryPartner_mobileNo_widget,delivery_type_widget,slotTime_Range_textwidget;
    public static  TextView deliveryCharges_text_widget,deliveryPersonMobileNotext_widget,deliveryPersonNametext_widget,distancebetweencustomer_vendortext_widget,discounttext_widget,addresstype_textwidget,AddressLine2_textwidget,landmark_textwidget,AddressLine1_textwidget,total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;
    Adapter_Mobile_orderDetails_itemDesp_listview1 adapter_forOrderDetails_listview;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp;
    ListView itemDesp_listview;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
     public String coupondiscountAmount;
    public String orderid;
    public String vendorLongitude;
    public String customerlatitude;
    public String vendorLatitude;
    public String vendorKey,vendorName;
    public String vendorUserMobileno;
    public String customerLongitutde;
    public String paymentmode;
    public String paymentModeString;
    public String payableAmount , vendorkey ="" ;
    public String userkey,UserRole,UserPhoneNumber;
    int payableAmountDouble = 0;

    public String tokenNo,customerMobileNo ="";
    public String deliverydistance;
    public String orderdetailsKey;
    public String orderTrackingDetailskey;
    public static String deliverypartnerName="";
    public String deliveryPartnerNumber="";
    public String ordertype;
    public String CurrentTime;
    public String DeliveryPersonList;
    public String  deliveryCharges;
    double screenInches;

    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();

    LinearLayout showlocation,deliverypersonName_Layout,deliverypersonMobileNO_Layout,tokenNo_Layout,distanceInKm_layout,confirmedTimeLayout,
            readyTimeLayout,pickedTimeLayout,slotdateLayout,slotTimeLayout,AddressLayout;
    public static BottomSheetDialog bottomSheetDialog;
    public static LinearLayout loadingPanel;
    public static LinearLayout loadingpanelmask;
    Button changePaymentMode_button,cancelOrder_button,changeDeliveryPartner,changeOrderToAnotherStore;
    private  String isFromEditOrders,isFromGenerateCustomermobile_billvaluereport,isFromCancelledOrders;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    public static Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class;
    boolean isordertrackingcalled = false;
    int itemDetailLength = 0;
    int itemDetailLoopCount = 0;
    boolean isUpdateItemDetailsCompleted = false;
    boolean isUpdateOrderTrackingCompleted = false , isDeliveryPartnerMethodCalled = false;
    ArrayList<String> VendorName_arrayList=new ArrayList<>();;
    private JSONArray result;
    private String pos_vendorMobileNumber;
    private String selectedvendorNameString,selectedvendorKeyString,vendortype,vendorNameString;
    List<Modal_vendor> vendorList=new ArrayList<>();

    String toastFromOrderItemDetails ="";

    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false , localDBcheck =false;
    boolean  isVendorOrdersTableServiceCalled = false;

    private  ArrayAdapter vendorlist_aAdapter;
    Update_CustomerOrderDetails_TrackingTableInterface mResultCallback_UpdateCustomerOrderDetailsTableInterface;
    Context mContext;
    double totalamountUserHaveAsCredit = 0;
   TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
   boolean iscancelOrderButtonClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit__or__cancel_order__order_details__screen_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        changeDeliveryPartner = findViewById(R.id.changeDeliveryPartner);
        orderIdtext_widget = findViewById(R.id.orderIdtext_widget);
        orderStatustext_widget = findViewById(R.id.orderStatustext_widget);
        itemDesp_listview = findViewById(R.id.itemDesp_listview);
        paymentTypetext_widget = findViewById(R.id.paymentTypetext_widget);
        slotNametext_widget = findViewById(R.id.mobileslotNametext_widget);
        slotDatetext_widget = findViewById(R.id.mobileslotDatetext_widget);
        deliveryPartner_name_widget = findViewById(R.id.deliveryPartner_name_widget);
        deliveryPartner_mobileNo_widget = findViewById(R.id.deliveryPartner_mobileNo_widget);
        delivery_type_widget = findViewById(R.id.delivery_type_widget);
        total_item_Rs_text_widget = findViewById(R.id.total_amount_text_widget);
        total_Rs_to_Pay_text_widget = findViewById(R.id.total_Rs_to_Pay_text_widget);
        taxes_and_Charges_rs_text_widget = findViewById(R.id.taxes_and_Charges_rs_text_widget);
        tokenNotext_widget = findViewById(R.id.tokenNotext_widget);
        slotTime_Range_textwidget = findViewById(R.id.slotTime_Range_textwidget);
        AddressLine1_textwidget = findViewById(R.id.AddressLine1_textwidget);
        AddressLine2_textwidget = findViewById(R.id.AddressLine2_textwidget);
        landmark_textwidget = findViewById(R.id.landmark_textwidget);
        addresstype_textwidget = findViewById(R.id.addresstype_textwidget);
        orderplacedtime_textwidget = findViewById(R.id.orderplacedtime_textwidget);
        orderConfirmedtime_textwidget = findViewById(R.id.orderConfirmedtime_textwidget);
        orderReaytime_textwidget = findViewById(R.id.orderReaytime_textwidget);
        orderpickeduptime_textwidget = findViewById(R.id.orderpickeduptime_textwidget);
        orderDeliveredtime_textwidget = findViewById(R.id.orderDeliveredtime_textwidget);
        discounttext_widget = findViewById(R.id.discounttext_widget);
        ordertypetext_widget = findViewById(R.id.ordertypetext_widget);
        distancebetweencustomer_vendortext_widget = findViewById(R.id.distancebetweencustomer_vendortext_widget);
        mobileNotext_widget = findViewById(R.id.mobileNotext_widget);
        showlocation = findViewById(R.id.showlocation);
        deliveryPersonNametext_widget = findViewById(R.id.deliveryPersonNametext_widget);
        deliveryPersonMobileNotext_widget = findViewById(R.id.deliveryPersonMobileNotext_widget);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        deliverypersonMobileNO_Layout = findViewById(R.id.deliverypersonMobileNO_Layout);
        deliverypersonName_Layout = findViewById(R.id.deliverypersonName_Layout);
        tokenNo_Layout  = findViewById(R.id.tokenNo_Layout);
        distanceInKm_layout  = findViewById(R.id.distanceInKm_layout);
        confirmedTimeLayout = findViewById(R.id.confirmedTimeLayout);
        readyTimeLayout = findViewById(R.id.readyTimeLayout);
        pickedTimeLayout = findViewById(R.id.pickedTimeLayout);
        slotdateLayout = findViewById(R.id.slotdateLayout);
        slotTimeLayout = findViewById(R.id.slotTimeLayout);
        AddressLayout = findViewById(R.id.AddressLayout);
        cancelOrder_button = findViewById(R.id.cancelOrder_button);
        deliveryCharges_text_widget = findViewById(R.id.deliveryCharges_text_widget);
        changeOrderToAnotherStore  = findViewById(R.id.changeOrderToAnotherStore);
        changePaymentMode_button = findViewById(R.id.changePaymentMode_button);
        deliveryPartnerList = new ArrayList<>();
        getAreawiseVendorName();

        mContext = Edit_Or_CancelOrder_OrderDetails_Screen.this;

        try {
            SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);


            vendorLatitude = (shared.getString("VendorLatitude", "12.9406"));
            vendorLongitude = (shared.getString("VendorLongitute", "80.1496"));
            UserPhoneNumber = (shared.getString("UserPhoneNumber", "+91"));
            vendorName= (shared.getString("VendorName", ""));
            vendorKey = (shared.getString("VendorKey", ""));
            vendorUserMobileno = (shared.getString("UserPhoneNumber", ""));
            UserRole = shared.getString("userrole", "");
            localDBcheck = (shared.getBoolean("localdbcheck", false));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
          //  orderdetailsnewschema = true;

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            SharedPreferences shared2 = getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
            DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));
            if(DeliveryPersonList.equals("")){
                getDeliveryPartnerList(false,"","","","","");

            }
            else{
                ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);

            }
            //ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        cancelOrder_button.setVisibility(View.GONE);

        OrderdItems_desp = new ArrayList<>();

        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(Edit_Or_CancelOrder_OrderDetails_Screen .this);
            //Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
            //    Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }

        Bundle bundle = getIntent().getExtras();
         modal_manageOrders_pojo_class = bundle.getParcelable("data");
        /*try{
            if(modal_manageOrders_pojo_class.get()!=null){
                AddressLine1_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getUseraddress()));
            }
            else{
                AddressLine1_textwidget.setText("");

            }
        }

         */
        if(localDBcheck){
            ConnectSqlDb("FetchMenuItemData");
        }
        else {


            getMenuItemArrayFromSharedPreferences();
        }


        try{
            orderid = String.valueOf((modal_manageOrders_pojo_class.getOrderid()));
            orderIdtext_widget.setText(String.valueOf(orderid));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            isFromEditOrders = modal_manageOrders_pojo_class.getIsFromEditOrders().toString().toUpperCase();
        }
        catch(Exception e ){
            e.printStackTrace();
            isFromEditOrders = "TRUE";
        }
        try{
            if( modal_manageOrders_pojo_class.getIsdataFetchedFromOrderTrackingDetails().toString().toUpperCase().equals("TRUE")){
                isordertrackingcalled = true;
            }
            else{
                isordertrackingcalled = false;

            }
        }
        catch(Exception e ){
            e.printStackTrace();
            isFromEditOrders = "TRUE";
        }

        try{
            payableAmount = modal_manageOrders_pojo_class.getPayableamount().toString();
        }
        catch(Exception e ){
            e.printStackTrace();
            payableAmount = "";
        }

        try{
            userkey = modal_manageOrders_pojo_class.getUserkey().toString();
        }
        catch(Exception e ){
            e.printStackTrace();
            userkey = "";
        }

        changeOrderToAnotherStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String orderDetailsKey =  "",orderTrackingDetailsKey = "",userAddressKey ="";
                        try{
                            orderDetailsKey = modal_manageOrders_pojo_class.getOrderdetailskey().toString();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            orderDetailsKey =  "";
                        }

                        try{
                            userAddressKey = modal_manageOrders_pojo_class.getUseraddresskey().toString();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            userAddressKey =  "";
                        }

                        try{
                            orderTrackingDetailsKey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails().toString();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            orderTrackingDetailsKey = "";
                        }

                displayBottomSheetWithVendorDetails(orderDetailsKey,orderTrackingDetailsKey,userAddressKey);




            }
        });

        try {
            if ((isFromEditOrders.equals("TRUE")) && (Edit_Or_CancelTheOrders.showcreditorderscheckbox.isChecked()) && (!isordertrackingcalled)) {
                //    modal_manageOrders_pojo_class = getOrderTrackingDetailsAlso(modal_manageOrders_pojo_class,orderid);
                //  Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "in method" , Toast.LENGTH_LONG).show();

                Adjusting_Widgets_Visibility(true);
                FetchOrdersFromOrderTrackingDatabase(orderid, true, vendorKey);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            isFromGenerateCustomermobile_billvaluereport = modal_manageOrders_pojo_class.getIsFromGenerateCustomermobile_billvaluereport().toString().toUpperCase();
        }
        catch(Exception e ){
            e.printStackTrace();
            isFromGenerateCustomermobile_billvaluereport = "TRUE";
        }

        try{
            isFromCancelledOrders = modal_manageOrders_pojo_class.getIsFromCancelledOrders().toString().toUpperCase();
        }
        catch(Exception e ){
            e.printStackTrace();
            isFromCancelledOrders = "TRUE";
        }

        setDatainUI();



        changeDeliveryPartner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                    String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                    String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));

                    if(!deliverypartnerName.equals("null")) {


                        String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                        showBottomSheetDialog_deliveryPartnerList(Orderkey,deliverypartnerName,orderid,customerMobileNo,vendorkey);

                    }
                    else{

                        String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                        showBottomSheetDialog_deliveryPartnerList(Orderkey,"null",orderid,customerMobileNo,vendorkey);

                    }
                }
            });









        cancelOrder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TMCAlertDialogClass(Edit_Or_CancelOrder_OrderDetails_Screen.this, R.string.app_name, R.string.OrderCancellingInstruction,
                        R.string.Yes_Text,R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                CurrentTime = getDate_and_time();


                                iscancelOrderButtonClicked = true;


                                try{
                                    Adjusting_Widgets_Visibility(true);

                                    String itemDespString = modal_manageOrders_pojo_class.getItemdesp_string();

                                    try {
                                        JSONArray jsonArray = new JSONArray(itemDespString);
                                        for(int i=0; i < jsonArray.length(); i++) {
                                            JSONObject json = jsonArray.getJSONObject(i);
                                            String subCtgyKey ="",menuitemid ="",inventoryDetailsString="",menuitemidFromMenuItem="",menuitemStockAvlDetailsKey="",stockIncomingKeyFromMenuItem ="";
                                            Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();





                                            try {
                                                if(json.has("menuitemid")) {
                                                    menuitemid = String.valueOf(json.get("menuitemid"));
                                                }
                                                else {
                                                    menuitemid = " ";
                                                }
                                            }
                                            catch (Exception e){
                                                menuitemid ="";
                                                e.printStackTrace();
                                            }

                                            for(int iterator =0 ; iterator<MenuItem.size();iterator++) {

                                                try {
                                                    menuitemidFromMenuItem = MenuItem.get(iterator).getMenuItemId().toString();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                if (menuitemidFromMenuItem.equals(menuitemid)) {
                                                    try {
                                                        inventoryDetailsString = MenuItem.get(iterator).getInventorydetails().toString();
                                                    } catch (Exception e) {
                                                        inventoryDetailsString = "";
                                                        e.printStackTrace();
                                                    }

                                                    if ((!inventoryDetailsString.equals("nil")) && (!inventoryDetailsString.equals("")) && (!inventoryDetailsString.equals("null"))) {

                                                        try {
                                                            JSONArray inventoryDetailsStringjsonArray = new JSONArray(String.valueOf(inventoryDetailsString));
                                                            int jsonArrayIterator = 0;
                                                            int jsonArrayCount = inventoryDetailsStringjsonArray.length();
                                                            for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {

                                                                try {
                                                                    String menuitemidFromMenuItem1 ="",menuitemStockAvlDetailsKey1="",stockIncomingKeyFromMenuItem1="";
                                                                    JSONObject json_InventoryDetails = inventoryDetailsStringjsonArray.getJSONObject(jsonArrayIterator);
                                                                    String menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");
                                                                    for(int iterator1 =0 ; iterator1<MenuItem.size();iterator1++) {

                                                                        try {
                                                                            menuitemidFromMenuItem1 = MenuItem.get(iterator1).getMenuItemId().toString();
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        if (menuitemidFromMenuItem1.equals(menuItemKeyFromInventoryDetails)) {


                                                                            try {
                                                                                menuitemStockAvlDetailsKey1 = MenuItem.get(iterator1).getKey_AvlDetails().toString();
                                                                            } catch (Exception e) {
                                                                                menuitemStockAvlDetailsKey1 = "";
                                                                                e.printStackTrace();
                                                                            }

                                                                            try {
                                                                                stockIncomingKeyFromMenuItem1 = MenuItem.get(iterator1).getStockincomingkey_AvlDetails().toString();
                                                                            } catch (Exception e) {
                                                                                stockIncomingKeyFromMenuItem1 = "";
                                                                                e.printStackTrace();
                                                                            }

                                                                            getMenuItemStockAvlDetailsUsingKeyAndGetStockOutGoingDetails(menuitemStockAvlDetailsKey1, stockIncomingKeyFromMenuItem1);



                                                                        }
                                                                    }

                                                                } catch (Exception e) {
                                                                    inventoryDetailsString = "";
                                                                    e.printStackTrace();
                                                                }


                                                            }
                                                        } catch (JSONException e) {
                                                            inventoryDetailsString = "";
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    else {


                                                        try {
                                                            menuitemStockAvlDetailsKey = MenuItem.get(iterator).getKey_AvlDetails().toString();
                                                        } catch (Exception e) {
                                                            menuitemStockAvlDetailsKey = "";
                                                            e.printStackTrace();
                                                        }

                                                        try {
                                                            stockIncomingKeyFromMenuItem = MenuItem.get(iterator).getStockincomingkey_AvlDetails().toString();
                                                        } catch (Exception e) {
                                                            stockIncomingKeyFromMenuItem = "";
                                                            e.printStackTrace();
                                                        }

                                                        getMenuItemStockAvlDetailsUsingKeyAndGetStockOutGoingDetails(menuitemStockAvlDetailsKey, stockIncomingKeyFromMenuItem);
                                                    }
                                                }

                                            }






                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }






                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                //GetStockOutGoingDetailsEntries(orderid);

                                 orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                                 customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                                vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                              String   paymentMode = (String.format("%s", modal_manageOrders_pojo_class.getPaymentmode()));

                                if(paymentMode.toUpperCase().equals(Constants.CREDIT)){
                                    GetDatafromCreditOrderDetailsTable(orderid,getDate_and_time(),customerMobileNo);
                                }
                                else{
                                    ChangeStatusOftheOrder(Constants.CANCELLED_ORDER_STATUS,orderTrackingDetailskey,CurrentTime,orderid,customerMobileNo,vendorkey);

                                }
                            }

                            @Override
                            public void onNo() {

                            }
                        });

            }
        });



        changePaymentMode_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog(paymentmode,ordertype,orderid);

            }
        });
    }
    @SuppressLint("Range")
    private void ConnectSqlDb(String ProcessToDo) {


        if(tmcMenuItemSQL_db_manager== null) {
            tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(mContext);
            try {
                tmcMenuItemSQL_db_manager.open();
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
                MenuItem.clear();
                try {
                    // if (cursor.moveToFirst()) {

                    Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                    Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));
                    if(cursor.getCount()>0){

                        if(cursor.moveToFirst()) {
                            do {
                                Modal_MenuItem_Settings modal_menuItem = new Modal_MenuItem_Settings();
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
                                modal_menuItem.setItemavailability(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability)));
                                modal_menuItem.setItemuniquecode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemuniquecode)));
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




                                MenuItem.add(modal_menuItem);
                            }
                            while (cursor.moveToNext());


                        }



                    }




                    //  }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }


    private void displayBottomSheetWithVendorDetails(String orderDetailsKey, String orderTrackingDetailsKey, String userAddressKey) {

        bottomSheetDialog = new BottomSheetDialog(Edit_Or_CancelOrder_OrderDetails_Screen.this);
        bottomSheetDialog.setContentView(R.layout.change_vendor_details_for_an_order);
        CheckBox changeAddresstoAnotherstore_Checkbox = bottomSheetDialog.findViewById(R.id.changeAddresstoAnotherstore_Checkbox);
        Spinner vendorName_Spinner = bottomSheetDialog.findViewById(R.id.vendorName_Spinner);
        TextView mobileno_textview = bottomSheetDialog.findViewById(R.id.mobileno_textview);
        TextView orderid_textview = bottomSheetDialog.findViewById(R.id.orderid_textview);
        TextView tokenNo_textview = bottomSheetDialog.findViewById(R.id.tokenNo_textview);
        TextView slotdate_text = bottomSheetDialog.findViewById(R.id.slotdate_text);
        TextView slotname_text = bottomSheetDialog.findViewById(R.id.slotname_text);
        TextView selected_vendorname_text = bottomSheetDialog.findViewById(R.id.selected_vendorname_text);
        TextView vendorname_text = bottomSheetDialog.findViewById(R.id.vendorname_text);
        Button changeOrderToAnotherStoreButton_bottomsheet= bottomSheetDialog.findViewById(R.id.changeOrderToAnotherStoreButton_bottomsheet);
       if((userAddressKey.equals(""))||(userAddressKey.equals(null))||(userAddressKey.equals("null"))||(userAddressKey.equals("-"))){
           changeAddresstoAnotherstore_Checkbox.setVisibility(View.GONE);
        }
        else{
           changeAddresstoAnotherstore_Checkbox.setVisibility(View.VISIBLE);

       }
        vendorlist_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, VendorName_arrayList);
        vendorName_Spinner.setAdapter(vendorlist_aAdapter);


        for(int i =0; i<vendorList.size();i++){
            Modal_vendor modal_vendor = vendorList.get(i);
            String key ="", name ="";
            try {
                key = modal_vendor.getKey();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(key.equals(vendorKey)){
                try {
                    name = modal_vendor.getVendorname().toString();
                    vendorname_text.setText(name);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }



        vendorName_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedvendorNameString =getVendorData(position,"name");
                if (!selectedvendorNameString.toUpperCase().equals(modal_manageOrders_pojo_class.getVendorname().toString().toUpperCase())){
                    selectedvendorKeyString=getVendorData(position,"key");
                    selected_vendorname_text.setText(selectedvendorNameString);

                }
                else{
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "You Can't Select the same Store Name", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });





        try{
            mobileno_textview.setText(modal_manageOrders_pojo_class.getUsermobile().toString());
        }
        catch (Exception e){
            mobileno_textview.setText(" -- ");
            e.printStackTrace();
        }


        try{
            orderid_textview .setText(modal_manageOrders_pojo_class.getOrderid().toString());
        }
        catch (Exception e){
            orderid_textview.setText(" -- ");
            e.printStackTrace();
        }



        try{
            tokenNo_textview .setText(modal_manageOrders_pojo_class.getTokenno().toString());
        }
        catch (Exception e){
            tokenNo_textview.setText(" -- ");

            e.printStackTrace();
        }

        try{
            slotdate_text  .setText(modal_manageOrders_pojo_class.getSlotdate().toString());
        }
        catch (Exception e){

            slotdate_text.setText(" -- ");
            e.printStackTrace();
        }

        try{
            slotname_text  .setText(modal_manageOrders_pojo_class.getSlotname().toString());
        }
        catch (Exception e){

            slotname_text.setText(" -- ");
            e.printStackTrace();
        }





        changeOrderToAnotherStoreButton_bottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vendorName.equals(selectedvendorNameString)){
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Please Select Valid Store Name", Toast.LENGTH_SHORT).show();

                }
                else {
                    if ((selectedvendorKeyString.equals(" nil ")) || (selectedvendorNameString.equals(" nil "))) {
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Please Select Valid Store Name", Toast.LENGTH_SHORT).show();
                    } else {
/*
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO your background code
                                FetchOrdersFromOrderItemDetailsDatabase(orderid);

                            }
                        });

 */
                        bottomSheetDialog.cancel();
                        Adjusting_Widgets_Visibility(true);
                        new AsyncCaller().execute();

                        if(userAddressKey.equals("") || userAddressKey.equals(" ") || userAddressKey.equals("null") || userAddressKey.equals(null)){

                        }
                        else {

                            try {
                                if (changeAddresstoAnotherstore_Checkbox.isChecked()) {
                                    changeVendorDetailsInAddressTable(userAddressKey);

                                } else {

                                }
                            }
                            catch (Exception e ){
                                e.printStackTrace();
                            }
                        }



                        if (orderDetailsKey.equals("")) {
                            FetchOrdersFromOrderDetailsDatabase(orderid);
/*
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO your background code
                                    FetchOrdersFromOrderDetailsDatabase(orderid);

                                }
                            });

 */
                        } else {
                            changeVendorDetailsInOrderDetails(orderDetailsKey);
                        }



                            if (orderTrackingDetailsKey.equals("")) {
                                FetchOrdersFromOrderTrackingDatabase(orderid, false, vendorKey);
/*
                            AsyncTask.execute(new Runnable() {
                                @Override
                                public void run() {
                                    //TODO your background code
                                    FetchOrdersFromOrderTrackingDatabase(orderid, false);

                                }
                            });

 */
                            } else {
                                changeVendorDetailsInOrderTrackingDetails(orderDetailsKey);
                            }


                    }
                }
            }
        });


        bottomSheetDialog.show();






    }

    private void changeVendorDetailsInAddressTable(String userAddressKey) {


        JSONObject  jsonObject = new JSONObject();


        try {
            jsonObject.put("key",userAddressKey);
            jsonObject.put("vendorkey",selectedvendorKeyString);
            jsonObject.put("vendorname",selectedvendorNameString);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateAddressTable
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message").toUpperCase();
                    if(message.equals("SUCCESS")) {

                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Vendor Details updated in Address table", Toast.LENGTH_LONG).show();


                    }
                    else{
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Can't update Vendor Details in Address table", Toast.LENGTH_LONG).show();

                       // Adjusting_Widgets_Visibility(false);
                    }


                } catch (JSONException e) {
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Can't update Vendor Details in Address table", Toast.LENGTH_LONG).show();

                    // showProgressBar(false);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Can't update Vendor Details in Address table", Toast.LENGTH_LONG).show();



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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);




    }

    private void FetchOrdersFromOrderItemDetailsDatabase(String orderid) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderItemDetailsusingOrderid + orderid,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            try{
                                JSONArray JArray  = response.getJSONArray("content");
                                int i1=0;
                                int arrayLength = JArray.length();
                                String orderItemdetailsKey ="";
                                itemDetailLength = arrayLength;
                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        if(json.has("key")){
                                            orderItemdetailsKey = String.valueOf(json.get("key"));
                                        }
                                        else{

                                            orderItemdetailsKey ="";
                                        }




                                    }
                                    catch (Exception e){
                                        orderItemdetailsKey="";

                                        e.printStackTrace();
                                    }




                                 //   Handler handler = new Handler();
                                    String finalOrderItemdetailsKey = orderItemdetailsKey;
                                    int finalarraylength = i1;
                                    itemDetailLoopCount = i1;
                                    if(!finalOrderItemdetailsKey.equals("")){
                                        changeVendorDetailsInOrderItemDetails(finalOrderItemdetailsKey, finalarraylength);

                                    }
                                     /*       handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(!finalOrderItemdetailsKey.equals("")){
                                                        Adjusting_Widgets_Visibility(true);
                                                        changeVendorDetailsInOrderItemDetails(finalOrderItemdetailsKey, finalarraylength);

                                                    }
                                                }
                                            });
                                        }
                                    }, 50);


                                      */

                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                   /* Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in Fetching Orders Item Details " , Toast.LENGTH_LONG).show();


                                }
                            });
                        }
                    }, 50);


                    */
                    toastFromOrderItemDetails = "Error in Fetching Orders Item Details ";
                    //Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in Fetching Orders Item Details " , Toast.LENGTH_LONG).show();

                    error.printStackTrace();


                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_1");
                params.put("orderplacedtime", "11 Jan 2021");

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);





    }

    private void changeVendorDetailsInOrderItemDetails(String finalOrderItemdetailsKey, int finalarraylength) {




        JSONObject  jsonObject = new JSONObject();


        try {
            jsonObject.put("key",finalOrderItemdetailsKey);
            jsonObject.put("vendorkey",selectedvendorKeyString);
            jsonObject.put("vendorname",selectedvendorNameString);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateOrderItemDetailsTable
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message").toUpperCase();
                    if(message.equals("SUCCESS")) {

                    if(finalarraylength==itemDetailLength){
                        isUpdateItemDetailsCompleted = true;
                    }
                      //  Adjusting_Widgets_Visibility(false);

                    }
                    else{
                        toastFromOrderItemDetails = "Can't update Vendor Details in Item Details";

                       //
                        // Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Can't update Vendor Details in Item Details", Toast.LENGTH_LONG).show();
                        isUpdateItemDetailsCompleted = false;

                      //  Adjusting_Widgets_Visibility(false);
                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                toastFromOrderItemDetails = "Can't update Vendor Details in Item Details";
               // Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Can't update Vendor Details in Item Details", Toast.LENGTH_LONG).show();
                isUpdateItemDetailsCompleted = false;

              //  Adjusting_Widgets_Visibility(false);
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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);





    }

    private void FetchOrdersFromOrderTrackingDatabase(String orderid, boolean IsFetchedFromONCreate, String vendorKey) {

        String Api_toGetOrderTrackingDetailsUsingOrderid = "";

        if(orderdetailsnewschema) {
            Api_toGetOrderTrackingDetailsUsingOrderid = Constants.api_GetVendorTrackingDetailsUsingOrderid_vendorkey+"?orderid="+orderid+"&vendorkey="+vendorKey;
        }
        else{
            Api_toGetOrderTrackingDetailsUsingOrderid = Constants.api_GetTrackingOrderDetails_orderid + orderid;
        }


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,Api_toGetOrderTrackingDetailsUsingOrderid ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            try{
                                // Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "in response" , Toast.LENGTH_LONG).show();
                                isordertrackingcalled =true;
                                JSONArray JArray  = response.getJSONArray("content");
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        if(json.has("key")){
                                            modal_manageOrders_pojo_class.keyfromtrackingDetails = String.valueOf(json.get("key"));

                                        }
                                        else{

                                            modal_manageOrders_pojo_class.keyfromtrackingDetails ="";
                                        }


                                        if(json.has("orderstatus")){
                                            modal_manageOrders_pojo_class.orderstatus = String.valueOf(json.get("orderstatus"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderstatus ="";
                                        }


                                        if(json.has("orderdeliverytime")){
                                            modal_manageOrders_pojo_class.orderdeliveredtime =  String.valueOf(json.get("orderdeliverytime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderdeliveredtime ="";
                                        }
                                        if(json.has("useraddresskey")){
                                            modal_manageOrders_pojo_class.useraddresskey =  String.valueOf(json.get("useraddresskey"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.useraddresskey ="";
                                        }


                                        if(json.has("orderreadytime")){
                                            modal_manageOrders_pojo_class.orderreadytime = String.valueOf(json.get("orderreadytime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderreadytime ="";
                                        }


                                        if(json.has("orderpickeduptime")){
                                            modal_manageOrders_pojo_class.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderpickeduptime ="";
                                        }


                                        if(json.has("orderconfirmedtime")){
                                            modal_manageOrders_pojo_class.orderconfirmedtime =  String.valueOf(json.get("orderconfirmedtime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderconfirmedtime ="";
                                        }

                                        if(json.has("useraddresslat")){
                                            modal_manageOrders_pojo_class.useraddresslat =  String.valueOf(json.get("useraddresslat"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.useraddresslat ="";
                                        }


                                        if(json.has("useraddresslong")){
                                            modal_manageOrders_pojo_class.useraddresslon =  String.valueOf(json.get("useraddresslong"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.useraddresslon ="";
                                        }




                                        try {
                                            if (ordertype.toUpperCase().equals(Constants.APPORDER) || ordertype.equals(Constants.PhoneOrder)) {


                                                if (json.has("deliverydistance")) {

                                                    String deliverydistance = String.valueOf(json.get("deliverydistance"));
                                                    if (!deliverydistance.equals(null) && (!deliverydistance.equals("null"))) {
                                                        modal_manageOrders_pojo_class.deliverydistance = String.valueOf(json.get("deliverydistance"));

                                                    } else {
                                                        modal_manageOrders_pojo_class.deliverydistance = "0";

                                                    }
                                                } else {
                                                    modal_manageOrders_pojo_class.deliverydistance = "0";
                                                }


                                            }
                                        } catch (Exception E) {
                                            modal_manageOrders_pojo_class.deliverydistance = "0";
                                            E.printStackTrace();
                                        }


                                        if (json.has("deliveryusername")) {
                                            modal_manageOrders_pojo_class.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.deliveryPartnerName ="";
                                        }
                                        if (json.has("deliveryuserkey")) {
                                            modal_manageOrders_pojo_class.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));
                                            ;

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.deliveryPartnerKey ="";
                                        }
                                        if (json.has("deliveryusermobileno")) {
                                            modal_manageOrders_pojo_class.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.deliveryPartnerMobileNo ="";
                                        }





                                        if(IsFetchedFromONCreate){
                                                updateDatainLocalArray(modal_manageOrders_pojo_class);

                                        }
                                        else{
                                            changeVendorDetailsInOrderTrackingDetails(orderTrackingDetailskey);

                                            /*
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {

                                                            changeVendorDetailsInOrderTrackingDetails(orderTrackingDetailskey);
                                                        }
                                                    });
                                                }
                                            }, 50);

                                             */

                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }


                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {

                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in Fetching Tracking Orders " , Toast.LENGTH_LONG).show();

                    if(IsFetchedFromONCreate) {
                        Adjusting_Widgets_Visibility(false);
                    }
                   /* Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in Fetching Tracking Orders " , Toast.LENGTH_LONG).show();

                                    if(IsFetchedFromONCreate) {
                                        Adjusting_Widgets_Visibility(false);
                                    }
                                }
                            });
                        }
                    }, 50);


                    */

                    error.printStackTrace();


                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_1");
                params.put("orderplacedtime", "11 Jan 2021");

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);






    }


    private void FetchOrdersFromOrderDetailsDatabase(String orderid) {


        String Api_toGetOrderTrackingDetailsUsingOrderid = "";

        if(orderdetailsnewschema) {
            Api_toGetOrderTrackingDetailsUsingOrderid = Constants.api_GetVendorOrderDetailsUsingOrderid_vendorkey+"?orderid="+orderid+"&vendorkey="+vendorKey;
        }
        else{
            Api_toGetOrderTrackingDetailsUsingOrderid = Constants.api_GetOrderDetailsusingOrderid + orderid;
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_toGetOrderTrackingDetailsUsingOrderid,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            try{
                                JSONArray JArray  = response.getJSONArray("content");
                                int i1=0;
                                int arrayLength = JArray.length();
                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        if(json.has("key")){
                                            modal_manageOrders_pojo_class.orderdetailskey = String.valueOf(json.get("key"));
                                            orderdetailsKey = String.valueOf(json.get("key"));
                                        }
                                        else{

                                            modal_manageOrders_pojo_class.orderdetailskey ="";
                                        }




                                    }
                                    catch (Exception e){
                                        modal_manageOrders_pojo_class.orderdetailskey ="";

                                        e.printStackTrace();
                                    }

                                    changeVendorDetailsInOrderDetails(orderdetailsKey);
/*

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                      //  changeVendorDetailsInOrderDetails(orderdetailsKey);
                                                    }
                                                });
                                            }
                                        }, 50);



 */
                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {/*
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in Fetching Orders Details " , Toast.LENGTH_LONG).show();


                                }
                            });
                        }
                    }, 50);

*/
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in Fetching Orders Details " , Toast.LENGTH_LONG).show();


                    error.printStackTrace();


                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_1");
                params.put("orderplacedtime", "11 Jan 2021");

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);






    }

    private void changeVendorDetailsInOrderDetails(String orderDetailsKey) {


        JSONObject  jsonObject = new JSONObject();


        try {
            jsonObject.put("key",orderDetailsKey);
            jsonObject.put("vendorkey",selectedvendorKeyString);
            jsonObject.put("vendorname",selectedvendorNameString);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_Update_OrderDetailsTableNew
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    final boolean[] isProgressBarTurnedOffinDoWhile = {false};
                    String message =  response.getString("message").toUpperCase();
                        if(message.equals("SUCCESS")) {
                            if(itemDetailLoopCount==itemDetailLength) {
                                if(isUpdateItemDetailsCompleted) {
                                    if (isUpdateOrderTrackingCompleted) {
                                        isProgressBarTurnedOffinDoWhile[0] = true;
                                        Adjusting_Widgets_Visibility(false);
                                        updateChangesinLocalArray(orderid,selectedvendorKeyString,"vendorkey");
                                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "This Order has changed to another vendor Succesfully", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Waiting to Update in OrderTrackingDetails", Toast.LENGTH_LONG).show();

                                    }
                                }
                                else{
                                  //  Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Failed to Update in OrderItemDetails", Toast.LENGTH_LONG).show();

                                }
                            }
                            else{
                                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Waiting to Update in OrderItemDetails", Toast.LENGTH_LONG).show();

                            }

                            if(!isProgressBarTurnedOffinDoWhile[0]) {
                                if (isUpdateItemDetailsCompleted) {
                                    if (isUpdateOrderTrackingCompleted) {
                                        isProgressBarTurnedOffinDoWhile[0] = true;
                                        Adjusting_Widgets_Visibility(false);
                                        updateChangesinLocalArray(orderid,selectedvendorKeyString,"vendorkey");

                                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "This Order has changed to another vendor Succesfully", Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Waiting to Update in OrderTrackingDetails", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    if (isUpdateOrderTrackingCompleted) {
                                        isProgressBarTurnedOffinDoWhile[0] = true;
                                        Adjusting_Widgets_Visibility(false);
                                        updateChangesinLocalArray(orderid,selectedvendorKeyString,"vendorkey");

                                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "This Order has changed to another vendor Succesfully", Toast.LENGTH_LONG).show();

                                    } else {
                                        do {
                                            isProgressBarTurnedOffinDoWhile[0] = true;
                                            Adjusting_Widgets_Visibility(false);
                                            updateChangesinLocalArray(orderid,selectedvendorKeyString,"vendorkey");

                                            Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "This Order has changed to another vendor Succesfully", Toast.LENGTH_LONG).show();

                                        }
                                        while (!isUpdateOrderTrackingCompleted);

                                    }
                                }
                            }

                    }
                    else{
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed on Orderdetails on change the Order to Another Store",Toast.LENGTH_LONG).show();

                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed on change on first try catch the Order to Another Store",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed on error response orderdetails  change outgoing type as cancelled",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);




    }


    private void changeVendorDetailsInOrderTrackingDetails(String orderTrackingdetailskey) {





        JSONObject  jsonObject = new JSONObject();


        try {
            jsonObject.put("key",orderTrackingdetailskey);
            jsonObject.put("vendorkey",selectedvendorKeyString);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_Update_OrderTrackingTableNew
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message").toUpperCase();
                    if(message.equals("SUCCESS")) {
                        isUpdateOrderTrackingCompleted = true;



                    }
                    else{
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change the Order tracking to Another Store",Toast.LENGTH_LONG).show();
                        isUpdateOrderTrackingCompleted = false;

                    }


                } catch (JSONException e) {
                    isUpdateOrderTrackingCompleted = false;

                    // showProgressBar(false);
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change the Order to Another Store in Tracking",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change the Order tracking to Another Store in Tracking",Toast.LENGTH_LONG).show();
                isUpdateOrderTrackingCompleted = false;

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);






    }





    private void getMenuItemStockAvlDetailsUsingKeyAndGetStockOutGoingDetails(String menuitemStockAvlDetailsKey, String stockIncomingKeyFromMenuItem) {
        Adjusting_Widgets_Visibility(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {


                final String[] barcode_avlDetails = {""};

                final String[] key_avlDetails = {""};
                final String[] stockincomingKeyAvlDetails = {""};
                final String[] menuitemkey_avlDetails = {""};
                final String[] stockBalance_avlDetails = {""};
                final String[] receivedstock_avlDetails = {""};
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.getApi_getMenuItemStockAvlDetailsUsingKey + menuitemStockAvlDetailsKey, null,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(@NonNull JSONObject response) {
                                try {
                                    Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                                    try {

                                        String ordertype = "#";

                                        //converting jsonSTRING into array
                                        JSONObject JObjectcontent = response.getJSONObject("content");
                                        JSONObject JObjectItem = JObjectcontent.getJSONObject("Item");
                                        JSONArray JArray = new JSONArray();
                                        JArray.put(JObjectItem);
                                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                        int i1 = 0;
                                        int arrayLength = JArray.length();

                                        for (; i1 < (arrayLength); i1++) {

                                            try {
                                                JSONObject json = JArray.getJSONObject(i1);

                                                try {
                                                    key_avlDetails[0] = json.getString("key");
                                                } catch (Exception e) {
                                                    key_avlDetails[0] = "";
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    barcode_avlDetails[0] = json.getString("barcode");
                                                } catch (Exception e) {
                                                    barcode_avlDetails[0] = "";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    stockincomingKeyAvlDetails[0] = json.getString("stockincomingkey");
                                                } catch (Exception e) {
                                                    stockincomingKeyAvlDetails[0] = "";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    menuitemkey_avlDetails[0] = json.getString("menuitemkey");
                                                } catch (Exception e) {
                                                    menuitemkey_avlDetails[0] = "";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    stockBalance_avlDetails[0] = json.getString("stockbalance");
                                                } catch (Exception e) {
                                                    stockBalance_avlDetails[0] = "";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    receivedstock_avlDetails[0] = json.getString("receivedstock");
                                                } catch (Exception e) {
                                                    receivedstock_avlDetails[0] = "";
                                                    e.printStackTrace();
                                                }
                                                String currentTimeAndDate = "";

                                                currentTimeAndDate = getDate_and_time();

                                              getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKeyFromMenuItem, stockincomingKeyAvlDetails[0], receivedstock_avlDetails[0], currentTimeAndDate, menuitemkey_avlDetails[0], barcode_avlDetails[0], key_avlDetails[0]);



                                            } catch (JSONException e) {
                                                Adjusting_Widgets_Visibility(false);

                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Adjusting_Widgets_Visibility(false);


                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Adjusting_Widgets_Visibility(false);


                                }


                            }

                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {
                        try {
                            Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Cant Get MenuItem Stock Avl Details", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);


                            Log.d(Constants.TAG, "Cant Get MenuItem Stock Avl Details " + error.getMessage());

                            error.printStackTrace();


                        } catch (Exception e) {
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);

                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        final Map<String, String> params = new HashMap<>();

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
                Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);
            }
            };


            new Thread(runnable).start();//to work in Background







    }

    private void getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(String stockIncomingKeyFromMenuItem, String stockincomingKeyAvlDetail, String receivedstock_avlDetail, String currentTimeAndDate, String menuitemkey_avlDetail, String barcode_avlDetail, String key_avlDetail) {

        final boolean[] isGotCancelledOrderQuantity = {false};
        final String[] outgoingtype_stockOutGngDetails_String = {""};
        final String[] stockincomingkey_stockOutGngDetails_String = {""};
        final String[] stocktype_stockOutGngDetails_String = {""};
        final String[] outgoingqty_stockOutGngDetails_String = {""};
        final String[] salesOrderId_stockOutGngDetails_String = {""};
        final String[] cancelled_Outgoingqty_OutGngDetails = {""};
        final String[] itemname_stockOutGngDetails_String = {""};
        final String[] stockOutgoingentryKey_outGngDetails = {""};
        final String[] barcode_outGngDetails = {""};


        final double[] outgoingqty_stockOutGngDetails_Double = {0};
        final double[] Total_outgoingqty_stockOutGngDetails_Double = {0};
        final double[] receivedStock_AvlDetails_double = {0};
        final double[] finalStockBalance_double = {0};
        final double[] cancelled_Outgoingqty_OutGngDetails_double= {0};

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofStockOutGoingDetailsForStockIncmgKey + stockincomingKeyAvlDetail, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            Log.d(Constants.TAG, " response: onMobileAppData " + response);
                            JSONArray JArray = response.getJSONArray("content");

                            int arrayLength = JArray.length();
                            for (int i = 0; i < arrayLength; i++) {
                                JSONObject json = JArray.getJSONObject(i);
                                outgoingqty_stockOutGngDetails_Double[0] = 0;
                                outgoingqty_stockOutGngDetails_Double[0] = 0;
                                stocktype_stockOutGngDetails_String[0] = "";
                                outgoingtype_stockOutGngDetails_String[0] = "";
                                stockincomingkey_stockOutGngDetails_String[0] = "";
                                outgoingqty_stockOutGngDetails_String[0] = "0";
                                receivedStock_AvlDetails_double[0] = 0;
                                salesOrderId_stockOutGngDetails_String[0] = "0";
                                barcode_outGngDetails[0] = "0";

                                try {
                                    if (json.has("outgoingtype")) {
                                        outgoingtype_stockOutGngDetails_String[0] = (json.getString("outgoingtype"));
                                    } else {
                                        outgoingtype_stockOutGngDetails_String[0] = "";
                                    }
                                } catch (Exception e) {
                                    outgoingtype_stockOutGngDetails_String[0] = "";

                                    e.printStackTrace();
                                }

                                if(!outgoingtype_stockOutGngDetails_String[0].equals(Constants.SALES_CANCELLED_OUTGOINGTYPE)){
                                try {
                                    if (json.has("stocktype")) {
                                        stocktype_stockOutGngDetails_String[0] = (json.getString("stocktype"));
                                    } else {
                                        stocktype_stockOutGngDetails_String[0] = "";
                                    }
                                } catch (Exception e) {
                                    stocktype_stockOutGngDetails_String[0] = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("itemname")) {
                                        itemname_stockOutGngDetails_String[0] = (json.getString("itemname"));
                                    } else {
                                        itemname_stockOutGngDetails_String[0] = "";
                                    }
                                } catch (Exception e) {
                                    itemname_stockOutGngDetails_String[0] = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("salesorderid")) {
                                        salesOrderId_stockOutGngDetails_String[0] = (json.getString("salesorderid"));
                                    } else {
                                        salesOrderId_stockOutGngDetails_String[0] = "";
                                    }
                                } catch (Exception e) {
                                    salesOrderId_stockOutGngDetails_String[0] = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("barcode")) {
                                        barcode_outGngDetails[0] = (json.getString("barcode"));
                                    } else {
                                        barcode_outGngDetails[0] = "";
                                    }
                                } catch (Exception e) {
                                    barcode_outGngDetails[0] = "";

                                    e.printStackTrace();
                                }


                                try {
                                    if (json.has("outgoingqty")) {
                                        outgoingqty_stockOutGngDetails_String[0] = (json.getString("outgoingqty"));

                                    } else {
                                        outgoingqty_stockOutGngDetails_String[0] = "0";
                                    }
                                } catch (Exception e) {
                                    outgoingqty_stockOutGngDetails_String[0] = "0";

                                    e.printStackTrace();
                                }


                                if (orderid.equals(salesOrderId_stockOutGngDetails_String[0])) {
                                    if (barcode_avlDetail.equals(barcode_outGngDetails[0])) {

                                        cancelled_Outgoingqty_OutGngDetails[0] = outgoingqty_stockOutGngDetails_String[0];


                                        try {
                                            if (json.has("key")) {
                                                stockOutgoingentryKey_outGngDetails[0] = (json.getString("key"));
                                                isGotCancelledOrderQuantity[0] = true;
                                            } else {
                                                stockOutgoingentryKey_outGngDetails[0] = "0";
                                            }
                                        } catch (Exception e) {
                                            stockOutgoingentryKey_outGngDetails[0] = "0";

                                            e.printStackTrace();
                                        }
                                    }
                                }


                                try {
                                    if (json.has("stockincomingkey")) {
                                        stockincomingkey_stockOutGngDetails_String[0] = (json.getString("stockincomingkey"));
                                    } else {
                                        stockincomingkey_stockOutGngDetails_String[0] = "";
                                    }
                                } catch (Exception e) {
                                    stockincomingkey_stockOutGngDetails_String[0] = "";

                                    e.printStackTrace();
                                }

                                try {
                                    outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (outgoingtype_stockOutGngDetails_String[0].equals(Constants.SUPPLYGAP_OUTGOINGTYPE)) {

                                    try {
                                        //  if (Total_outgoingqty_stockOutGngDetails_Double[0] > outgoingqty_stockOutGngDetails_Double[0]) {
                                        //     Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];

                                        //   } else {
                                        //       Total_outgoingqty_stockOutGngDetails_Double[0] = outgoingqty_stockOutGngDetails_Double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                        //   }
                                        Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    try {
                                        Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] + outgoingqty_stockOutGngDetails_Double[0];


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }
                            try {
                                receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedstock_avlDetail);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if(isGotCancelledOrderQuantity[0] ) {




                                try {
                                    cancelled_Outgoingqty_OutGngDetails_double[0] = Double.parseDouble(cancelled_Outgoingqty_OutGngDetails[0]);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                double stockBalanceBeforeMinusCurrentItem = 0;
                                try {
                                    stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem + cancelled_Outgoingqty_OutGngDetails_double[0];
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (!stockIncomingKeyFromMenuItem.equals(stockincomingKeyAvlDetail)) {
                                    try {
                                        receivedStock_AvlDetails_double[0] = receivedStock_AvlDetails_double[0] + cancelled_Outgoingqty_OutGngDetails_double[0];
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetail, finalStockBalance_double[0], currentTimeAndDate, true, receivedStock_AvlDetails_double[0]);
                                    GetStockIncomingEntry(stockIncomingKeyFromMenuItem, stockincomingKeyAvlDetail, cancelled_Outgoingqty_OutGngDetails_double[0]);


                                } else {
                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetail, finalStockBalance_double[0], currentTimeAndDate, false, 0);

                                }

                                AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuitemkey_avlDetail, stockincomingKeyAvlDetail, itemname_stockOutGngDetails_String[0], barcode_avlDetail, currentTimeAndDate);


                                UpdateStockOutGoingDetailsEntry(stockOutgoingentryKey_outGngDetails[0]);
                            }
                            else{


                                cancelled_Outgoingqty_OutGngDetails_double[0] =  GetStockOutGoingDetailsEntries(orderid,barcode_avlDetail,receivedStock_AvlDetails_double[0],currentTimeAndDate,key_avlDetail,Total_outgoingqty_stockOutGngDetails_Double[0],menuitemkey_avlDetail,stockincomingKeyAvlDetail,itemname_stockOutGngDetails_String[0],stockOutgoingentryKey_outGngDetails[0],stockIncomingKeyFromMenuItem );





                            }




                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                String errorCode = "";
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
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in General App Data code :  " + errorCode, Toast.LENGTH_LONG).show();



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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);








    }

    private void GetStockIncomingEntry(String stockIncomingKeyFromMenuItem, String stockincomingKeyAvlDetail, double cancelled_Outgoingqty_OutGngDetails_double) {

        final String[] Oldstock_OldStockIncomingKey = {""};
        final String[] Newstock_OldStockIncomingKey = {""};
        final String[] Totalstock_OldStockIncomingKey = {""};
        final String[] forecastedstock_OldStockIncomingKey = {""};

        final String[] forecastedstock_NewStockIncomingKey = {""};
        final String[] Oldstock_NewStockIncomingKey = {""};
        final String[] Newstock_NewStockIncomingKey = {""};
        final String[] Totalstock_NewStockIncomingKey = {""};

        final boolean[] isGotResponseForFirstStockIncomingKey = {false};
        //Old Stock Incoming Entry

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.getApi_getStockIncomingUsingKey+stockIncomingKeyFromMenuItem ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                            try {

                                isGotResponseForFirstStockIncomingKey[0] =true;

                                JSONObject JObjectcontent = response.getJSONObject("content");
                                JSONObject JObjectItem = JObjectcontent.getJSONObject("Item");
                                JSONArray JArray = new JSONArray();
                                JArray.put(JObjectItem);

                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();

                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);



                                        if(json.has("forecastedstock")) {
                                            try {
                                                forecastedstock_OldStockIncomingKey[0] = json.getString("forecastedstock");
                                            } catch (Exception e) {
                                                forecastedstock_OldStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            forecastedstock_OldStockIncomingKey[0] ="";
                                        }


                                        if(json.has("newstock")) {
                                            try {
                                                Newstock_OldStockIncomingKey[0] = json.getString("newstock");
                                            } catch (Exception e) {
                                                Newstock_OldStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            Newstock_OldStockIncomingKey[0] ="";
                                        }



                                        if(json.has("oldstock")) {
                                            try {
                                                Oldstock_OldStockIncomingKey[0] = json.getString("oldstock");
                                            } catch (Exception e) {
                                                Oldstock_OldStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            Oldstock_OldStockIncomingKey[0] ="";
                                        }


                                        if(json.has("totalstock")) {
                                            try {
                                                Totalstock_OldStockIncomingKey[0] = json.getString("totalstock");
                                            } catch (Exception e) {
                                                Totalstock_OldStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            Totalstock_OldStockIncomingKey[0] ="";
                                        }



                                    } catch (JSONException e) {
                                        Adjusting_Widgets_Visibility(false);

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Adjusting_Widgets_Visibility(false);


                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Cant Get StockIncoming Entry", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);


                    isGotResponseForFirstStockIncomingKey[0] =true;
                    Log.d(Constants.TAG, "Cant Get StockIncoming Entry" + error.getMessage());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();
                    Adjusting_Widgets_Visibility(false);

                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);






        //New Stock Incoming Entry

        JsonObjectRequest jsonObjectRequestt = new JsonObjectRequest(Request.Method.GET, Constants.getApi_getStockIncomingUsingKey+stockincomingKeyAvlDetail ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                            try {


                                JSONObject JObjectcontent = response.getJSONObject("content");
                                JSONObject JObjectItem = JObjectcontent.getJSONObject("Item");
                                JSONArray JArray = new JSONArray();
                                JArray.put(JObjectItem);
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();

                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        if(json.has("forecastedstock")) {
                                            try {
                                                forecastedstock_NewStockIncomingKey[0] = json.getString("forecastedstock");
                                            } catch (Exception e) {
                                                forecastedstock_NewStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            forecastedstock_NewStockIncomingKey[0] ="";
                                        }


                                        if(json.has("newstock")) {
                                            try {
                                                Newstock_NewStockIncomingKey[0] = json.getString("newstock");
                                            } catch (Exception e) {
                                                Newstock_NewStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            Newstock_NewStockIncomingKey[0] ="";
                                        }



                                        if(json.has("oldstock")) {
                                            try {
                                                Oldstock_NewStockIncomingKey[0] = json.getString("oldstock");
                                            } catch (Exception e) {
                                                Oldstock_NewStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            Oldstock_NewStockIncomingKey[0] ="";
                                        }


                                        if(json.has("totalstock")) {
                                            try {
                                                Totalstock_NewStockIncomingKey[0] = json.getString("totalstock");
                                            } catch (Exception e) {
                                                Totalstock_NewStockIncomingKey[0] = "";
                                                e.printStackTrace();
                                            }
                                        }
                                        else{
                                            Totalstock_NewStockIncomingKey[0] ="";
                                        }



                                    } catch (JSONException e) {
                                        Adjusting_Widgets_Visibility(false);

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Adjusting_Widgets_Visibility(false);


                            }

                                try{
                                   int delayTimer = 0;
                                    if(isGotResponseForFirstStockIncomingKey[0]){
                                        delayTimer = 0;
                                    }
                                    else{
                                        delayTimer = 100;

                                    }
                                    final Handler handler = new Handler(Looper.getMainLooper());
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Do something after 100ms


                                            try{

                                                if(forecastedstock_OldStockIncomingKey[0].equals("")){
                                                    double Oldstock_NewStockIncomingKey_double = 0,Totalstock_NewStockIncomingKey_double=0;
                                                    Oldstock_NewStockIncomingKey_double= Double.parseDouble(Oldstock_NewStockIncomingKey[0])+cancelled_Outgoingqty_OutGngDetails_double;
                                                   // Oldstock_NewStockIncomingKey_double  = Double.parseDouble(Oldstock_NewStockIncomingKey[0]);

                                                    Totalstock_NewStockIncomingKey_double  = Oldstock_NewStockIncomingKey_double + Double.parseDouble(Newstock_NewStockIncomingKey[0]);
                                                    updateStockIncomingEntry(stockincomingKeyAvlDetail,0,0,Oldstock_NewStockIncomingKey_double,Totalstock_NewStockIncomingKey_double);
                                                }
                                                else{
                                                    double oldKeyForecastedStock_double = 0,balanceStock=0,Oldstock_NewStockIncomingKey_double =0,Newstock_NewStockIncomingKey_double =0,Totalstock_NewStockIncomingKey_double =0;

                                                    try{
                                                        oldKeyForecastedStock_double = Double.parseDouble(forecastedstock_OldStockIncomingKey[0]);
                                                    }
                                                    catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    if((oldKeyForecastedStock_double)<cancelled_Outgoingqty_OutGngDetails_double){
                                                        balanceStock = cancelled_Outgoingqty_OutGngDetails_double - oldKeyForecastedStock_double;
                                                        oldKeyForecastedStock_double =0;
                                                        Oldstock_NewStockIncomingKey_double = balanceStock;

                                                        Newstock_NewStockIncomingKey_double = Double.parseDouble(Newstock_NewStockIncomingKey[0]);
                                                        Newstock_NewStockIncomingKey_double = Newstock_NewStockIncomingKey_double + oldKeyForecastedStock_double;
                                                        Totalstock_NewStockIncomingKey_double = Newstock_NewStockIncomingKey_double+Oldstock_NewStockIncomingKey_double;

                                                        updateStockIncomingEntry(stockIncomingKeyFromMenuItem,oldKeyForecastedStock_double,0,0,0);
                                                        updateStockIncomingEntry(stockincomingKeyAvlDetail,0,Newstock_NewStockIncomingKey_double,Oldstock_NewStockIncomingKey_double,Totalstock_NewStockIncomingKey_double);

                                                    }
                                                    else{
                                                        balanceStock =  oldKeyForecastedStock_double -cancelled_Outgoingqty_OutGngDetails_double;
                                                        oldKeyForecastedStock_double = balanceStock;
                                                        Oldstock_NewStockIncomingKey_double= Double.parseDouble(Oldstock_NewStockIncomingKey[0]);
                                                        Newstock_NewStockIncomingKey_double = Double.parseDouble(Newstock_NewStockIncomingKey[0]);
                                                        Newstock_NewStockIncomingKey_double = Newstock_NewStockIncomingKey_double + cancelled_Outgoingqty_OutGngDetails_double;
                                                        Totalstock_NewStockIncomingKey_double = Newstock_NewStockIncomingKey_double+Oldstock_NewStockIncomingKey_double;
                                                        updateStockIncomingEntry(stockIncomingKeyFromMenuItem,oldKeyForecastedStock_double,0,0,0);

                                                        updateStockIncomingEntry(stockincomingKeyAvlDetail,0,Newstock_NewStockIncomingKey_double,Oldstock_NewStockIncomingKey_double,Totalstock_NewStockIncomingKey_double);


                                                    }





                                                }



                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    }, delayTimer);

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Adjusting_Widgets_Visibility(false);


                                }


                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Cant Get StockIncoming Entry", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);



                    Log.d(Constants.TAG, "Cant Get StockIncoming Entry" + error.getMessage());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();
                    Adjusting_Widgets_Visibility(false);

                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequestt);





    }

    private void updateStockIncomingEntry(String stockincomingKeyAvlDetail, double forecastedStock, double newstock_newStockIncomingKey_double, double oldstock_newStockIncomingKey_double, double totalstock_newStockIncomingKey_double) {


        JSONObject  jsonObject = new JSONObject();


        try {
            jsonObject.put("key",stockincomingKeyAvlDetail);
            if(forecastedStock>0){
                jsonObject.put("forecastedstock", forecastedStock);


            }
            if(newstock_newStockIncomingKey_double>0){
                jsonObject.put("newstock", newstock_newStockIncomingKey_double);


            }


            if(oldstock_newStockIncomingKey_double>0){
                jsonObject.put("oldstock", oldstock_newStockIncomingKey_double);


            }


            if(totalstock_newStockIncomingKey_double>0){
                jsonObject.put("totalstock", totalstock_newStockIncomingKey_double);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_Update_StockIncomingUsingKey
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );


                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change outgoing type as cancelled",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change outgoing type as cancelled",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);


    }

    private double GetStockOutGoingDetailsEntries(String orderid, String barcode_avlDetail, double receivedStock_AvlDetails_double, String currentTimeAndDate, String key_avlDetail, double Total_outgoingqty_stockOutGngDetails_Double, String menuitemkey_avlDetail, String stockincomingKeyAvlDetail, String itemname_stockOutGngDetails_String, String stockOutgoingentryKey_outGcngDetail, String stockIncomingKeyFromMenuItem) {
        final String[] stockOutgoingentryKey = {""};
        final String[] stockincomingkey = { "" };
        final String[] barcode = { "" };
        final String[] outgoingqty = { "" };
        final String[] stocktype = { "" };
        final String[] itemname = { "" };
        final String[] outgoingType = { "" };

        final double[] outgoingqty_stockOutGngDetails_Double = {0};
        final double[] finalStockBalance_double = {0};


        final String[] stockOutgoingentryKey_outGngDetail = {""};

        final String[] barcodeFromMenuitem = {""};
        final String[] menuitemStockAvlDetailsKey = { "" };


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getStockOutgoingUsingSalesOrderid+orderid ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                            try {

                                String ordertype="#";

                                //converting jsonSTRING into array
                                JSONArray JArray  = response.getJSONArray("content");
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();
                                /*Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                if(arrayLength>1){
                                    Toast.makeText(mContext, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();


                                }

                                 */

                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        stockOutgoingentryKey[0] ="";
                                        stockincomingkey[0] ="";
                                        barcode[0] ="";
                                        outgoingqty[0] ="";
                                        stocktype[0] ="";
                                        itemname[0] ="";
                                        outgoingType[0] ="";


                                        try {
                                            if (json.has("outgoingtype")) {
                                                outgoingType[0] = json.getString("outgoingtype");
                                            } else {
                                                outgoingType[0] = "";
                                            }
                                        } catch (Exception e) {
                                            outgoingType[0] = "";

                                            e.printStackTrace();
                                        }

                                        try {
                                            if (json.has("key")) {
                                                stockOutgoingentryKey[0] = json.getString("key");
                                            } else {
                                                stockOutgoingentryKey[0] = "";
                                            }
                                        } catch (Exception e) {
                                            stockOutgoingentryKey[0] = "";

                                            e.printStackTrace();
                                        }



                                        try {
                                            if (json.has("barcode")) {
                                                barcode[0] = json.getString("barcode");
                                            } else {
                                                barcode[0] = "";
                                            }
                                        } catch (Exception e) {
                                            barcode[0] = "";

                                            e.printStackTrace();
                                        }


                                        try {
                                            if (json.has("outgoingqty")) {
                                                outgoingqty[0] = json.getString("outgoingqty");
                                            } else {
                                                outgoingqty[0] = "";
                                            }
                                        } catch (Exception e) {
                                            outgoingqty[0] = "";

                                            e.printStackTrace();
                                        }


                                        try {
                                            if (json.has("stocktype")) {
                                                stocktype[0] = json.getString("stocktype");
                                            } else {
                                                stocktype[0] = "";
                                            }
                                        } catch (Exception e) {
                                            stocktype[0] = "";

                                            e.printStackTrace();
                                        }


                                        try {
                                            if (json.has("itemname")) {
                                                itemname[0] = json.getString("itemname");
                                            } else {
                                                itemname[0] = "";
                                            }
                                        } catch (Exception e) {
                                            itemname[0] = "";

                                            e.printStackTrace();
                                        }



                                         for(int iterator =0 ; iterator<MenuItem.size();iterator++){

                                             try{
                                                 barcodeFromMenuitem[0] = MenuItem.get(iterator).getBarcode().toString();
                                             }
                                             catch (Exception e){
                                                 e.printStackTrace();
                                             }
                                             if(barcode[0].equals(barcodeFromMenuitem[0])) {

                                                 barcodeFromMenuitem[0] = "";
                                                 menuitemStockAvlDetailsKey[0] ="";
                                                 UpdateStockOutGoingDetailsEntry(stockOutgoingentryKey[0]);
                                                 try {
                                                     if (json.has("stockincomingkey")) {
                                                         stockincomingkey[0] = json.getString("stockincomingkey");
                                                     } else {
                                                         stockincomingkey[0] = "";
                                                     }
                                                 } catch (Exception e) {
                                                     stockincomingkey[0] = "";

                                                     e.printStackTrace();
                                                 }

                                                 try {
                                                     menuitemStockAvlDetailsKey[0] = MenuItem.get(iterator).getKey_AvlDetails().toString();
                                                 } catch (Exception e) {
                                                     menuitemStockAvlDetailsKey[0] ="";
                                                     e.printStackTrace();
                                                 }
                                                 try {
                                                     outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty[0]);

                                                 } catch (Exception e) {
                                                     e.printStackTrace();
                                                 }







                                             }


                                         }


                                       // ChangeOutGoingTypeInOutgoingTable(entryKey);






                                    } catch (JSONException e) {
                                        Adjusting_Widgets_Visibility(false);

                                        e.printStackTrace();
                                    }
                                }



                               /* try {
                                    cancelled_Outgoingqty_OutGngDetails_double[0] = Double.parseDouble(cancelled_Outgoingqty_OutGngDetails[0]);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                */

                                double stockBalanceBeforeMinusCurrentItem = 0;
                                try {
                                    stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double - Total_outgoingqty_stockOutGngDetails_Double;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem +  outgoingqty_stockOutGngDetails_Double[0];
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetail, finalStockBalance_double[0], currentTimeAndDate, false, 0);


                                AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuitemkey_avlDetail, stockincomingKeyAvlDetail,  itemname[0] , barcode_avlDetail, currentTimeAndDate);


                                GetStockIncomingEntry(stockincomingkey[0], stockincomingKeyAvlDetail, outgoingqty_stockOutGngDetails_Double[0]);




/*



                                if(!menuitemStockAvlDetailsKey[0].equals("") && !menuitemStockAvlDetailsKey[0].equals(" nil ") && !menuitemStockAvlDetailsKey[0].equals(" ") && !menuitemStockAvlDetailsKey[0].equals("nil") && (!menuitemStockAvlDetailsKey[0].equals("null"))&& (menuitemStockAvlDetailsKey[0] != null)){



                                    getMenuItemStockAvlDetailsUsingKey(menuitemStockAvlDetailsKey[0], stockincomingkey[0],outgoingqty[0],stocktype[0],itemname[0],barcode[0],stockOutgoingentryKey[0],Total_outgoingqty_stockOutGngDetails_Double[0]);



                                }
                                else{
                                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "This Item "+itemname[0]+" Does Not have Stock Balance Entry ", Toast.LENGTH_SHORT).show();
                                }

 */



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Adjusting_Widgets_Visibility(false);


                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);



                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();
                    Adjusting_Widgets_Visibility(false);

                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);


        return outgoingqty_stockOutGngDetails_Double[0];
    }

    private void getMenuItemStockAvlDetailsUsingKey(String menuitemStockAvlDetailsKey, String stockincomingkey_OutGngDetails, String cancelled_Outgoingqty_OutGngDetails, String stocktype_OutGngDetails, String itemname_OutGngDetails, String barcode_OutGngDetails, String stockOutgoingentryKey_OutGngDetails, double Total_outgoingqty_stockOutGngDetails_Double) {






        final String[] barcode_avlDetails = {""};

        final String[] key_avlDetails = {""};
        final String[] stockincomingKeyAvlDetails = { "" };
        final String[] menuitemkey_avlDetails = { "" };
        final String[] stockBalance_avlDetails = { "" };
        final String[] receivedstock_avlDetails = { "" };
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.getApi_getMenuItemStockAvlDetailsUsingKey+menuitemStockAvlDetailsKey ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                            try {

                                String ordertype="#";

                                //converting jsonSTRING into array
                                JSONObject JObjectcontent  = response.getJSONObject("content");
                                JSONObject JObjectItem  = JObjectcontent.getJSONObject("Item");
                                JSONArray JArray = new JSONArray();
                                JArray.put(JObjectItem);
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();

                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);

                                        try{
                                            key_avlDetails[0] =  json.getString("key");
                                        }
                                        catch (Exception e){
                                            key_avlDetails[0] = "";
                                             e.printStackTrace();
                                        }
                                        try{
                                            barcode_avlDetails[0] =  json.getString("barcode");
                                        }
                                        catch (Exception e){
                                            barcode_avlDetails[0] = "";
                                            e.printStackTrace();
                                        }

                                        try{
                                            stockincomingKeyAvlDetails[0] =  json.getString("stockincomingkey");
                                        }
                                        catch (Exception e){
                                            stockincomingKeyAvlDetails[0] = "";
                                            e.printStackTrace();
                                        }

                                        try{
                                            menuitemkey_avlDetails[0] =  json.getString("menuitemkey");
                                        }
                                        catch (Exception e){
                                            menuitemkey_avlDetails[0] = "";
                                            e.printStackTrace();
                                        }


                                        try{
                                            stockBalance_avlDetails[0] =  json.getString("stockbalance");
                                        }
                                        catch (Exception e){
                                            stockBalance_avlDetails[0] = "";
                                            e.printStackTrace();
                                        }



                                        try{
                                            receivedstock_avlDetails[0] =  json.getString("receivedstock");
                                        }
                                        catch (Exception e){
                                            receivedstock_avlDetails[0] = "";
                                            e.printStackTrace();
                                        }
                                        String currentTimeAndDate = "";

                                        currentTimeAndDate = getDate_and_time();

                                        getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetailsOld(stockincomingkey_OutGngDetails,stockincomingKeyAvlDetails[0],receivedstock_avlDetails[0],currentTimeAndDate, menuitemkey_avlDetails[0],itemname_OutGngDetails, barcode_avlDetails[0],stockOutgoingentryKey_OutGngDetails,cancelled_Outgoingqty_OutGngDetails,key_avlDetails[0]);
                                        /*if(stockincomingkey_OutGngDetails.equals(stockincomingKeyAvlDetails[0])){
                                            try {
                                                receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedstock_avlDetails[0]);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            double stockBalanceBeforeMinusCurrentItem = 0;
                                            try {
                                                stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            try {
                                                OutGoingStock_AvlDetails_double[0] = Double.parseDouble(outgoingqty_OutGngDetails);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem - OutGoingStock_AvlDetails_double[0];
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuitemkey_avlDetails[0], stockincomingKeyAvlDetails[0] , itemname_OutGngDetails, barcode_avlDetails[0],currentTimeAndDate);

                                            UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], currentTimeAndDate);

                                           UpdateStockOutGoingDetailsEntry(stockOutgoingentryKey_OutGngDetails);


                                        }
                                        else{

                                        }
                                        */


                                        // ChangeOutGoingTypeInOutgoingTable(entryKey);






                                    } catch (JSONException e) {
                                        Adjusting_Widgets_Visibility(false);

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Adjusting_Widgets_Visibility(false);


                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Cant Get MenuItem Stock Avl Details", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);



                    Log.d(Constants.TAG, "Cant Get MenuItem Stock Avl Details " + error.getMessage());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();
                    Adjusting_Widgets_Visibility(false);

                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);











    }

    private void getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetailsOld(String stockincomingkey_outGngDetails, String stockIncomingKey_avlDetails, String receivedStock_AvlDetails, String currentTimeAndDate, String menuitemkey_avlDetail, String itemname_outGngDetails, String barcode_avlDetail, String stockOutgoingentryKey_outGngDetails, String cancelled_Outgoingqty_OutGngDetails, String key_avlDetails) {
        if((!stockIncomingKey_avlDetails.equals("")) && (!stockIncomingKey_avlDetails.equals(" nil ")) &&(!stockIncomingKey_avlDetails.equals("null")) && (!stockIncomingKey_avlDetails.equals(null)) && (!stockIncomingKey_avlDetails.equals("0")) && (!stockIncomingKey_avlDetails.equals(" 0 ")) && (!stockIncomingKey_avlDetails.equals("-")) && (!stockIncomingKey_avlDetails.equals("nil"))) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    final String[] outgoingtype_stockOutGngDetails_String = {""};
                    final String[] stockincomingkey_stockOutGngDetails_String = {""};
                    final String[] stocktype_stockOutGngDetails_String = {""};
                    final String[] outgoingqty_stockOutGngDetails_String = {""};

                    final double[] outgoingqty_stockOutGngDetails_Double = {0};
                    final double[] Total_outgoingqty_stockOutGngDetails_Double = {0};
                    final double[] receivedStock_AvlDetails_double = {0};
                    final double[] finalStockBalance_double = {0};
                    final double[] cancelled_Outgoingqty_OutGngDetails_double= {0};

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofStockOutGoingDetailsForStockIncmgKey + stockIncomingKey_avlDetails, null,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {


                                    try {
                                        Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                        JSONArray JArray = response.getJSONArray("content");

                                        int arrayLength = JArray.length();
                                        for (int i = 0; i < arrayLength; i++) {
                                            JSONObject json = JArray.getJSONObject(i);
                                            outgoingqty_stockOutGngDetails_Double[0] = 0;
                                            outgoingqty_stockOutGngDetails_Double[0] = 0;
                                            stocktype_stockOutGngDetails_String[0] = "";
                                            outgoingtype_stockOutGngDetails_String[0] = "";
                                            stockincomingkey_stockOutGngDetails_String[0] = "";
                                            outgoingqty_stockOutGngDetails_String[0] = "0";
                                            receivedStock_AvlDetails_double[0] = 0;


                                            try {
                                                if (json.has("stocktype")) {
                                                    stocktype_stockOutGngDetails_String[0] = (json.getString("stocktype"));
                                                } else {
                                                    stocktype_stockOutGngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                stocktype_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("outgoingtype")) {
                                                    outgoingtype_stockOutGngDetails_String[0] = (json.getString("outgoingtype"));
                                                } else {
                                                    outgoingtype_stockOutGngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                outgoingtype_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("outgoingqty")) {
                                                    outgoingqty_stockOutGngDetails_String[0] = (json.getString("outgoingqty"));
                                                } else {
                                                    outgoingqty_stockOutGngDetails_String[0] = "0";
                                                }
                                            } catch (Exception e) {
                                                outgoingqty_stockOutGngDetails_String[0] = "0";

                                                e.printStackTrace();
                                            }


                                            try {
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("stockincomingkey")) {
                                                    stockincomingkey_stockOutGngDetails_String[0] = (json.getString("stockincomingkey"));
                                                } else {
                                                    stockincomingkey_stockOutGngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                stockincomingkey_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }

                                            try {
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (stocktype_stockOutGngDetails_String[0].equals(Constants.SUPPLYGAP_OUTGOINGTYPE)) {

                                                try {
                                                    if (Total_outgoingqty_stockOutGngDetails_Double[0] > outgoingqty_stockOutGngDetails_Double[0]) {
                                                        Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];

                                                    } else {
                                                        Total_outgoingqty_stockOutGngDetails_Double[0] = outgoingqty_stockOutGngDetails_Double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                                    }


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            } else {
                                                try {
                                                    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] + outgoingqty_stockOutGngDetails_Double[0];


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        }


                                        try {
                                            receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedStock_AvlDetails);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            cancelled_Outgoingqty_OutGngDetails_double[0] = Double.parseDouble(cancelled_Outgoingqty_OutGngDetails);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        double stockBalanceBeforeMinusCurrentItem = 0;
                                        try {
                                            stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        try {
                                            finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem + cancelled_Outgoingqty_OutGngDetails_double[0];
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }





                                        AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuitemkey_avlDetail, stockIncomingKey_avlDetails , itemname_outGngDetails, barcode_avlDetail,currentTimeAndDate);

                                        UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], currentTimeAndDate, false, 0);

                                        UpdateStockOutGoingDetailsEntry(stockOutgoingentryKey_outGngDetails);





                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(@NonNull VolleyError error) {
                            Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                            String errorCode = "";
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
                            Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "Error in General App Data code :  " + errorCode, Toast.LENGTH_LONG).show();



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
                    Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);


                }
            };


            new Thread(runnable).start();//to work in Background


        }




    }

    private void UpdateStockOutGoingDetailsEntry(String stockOutgoingentryKey_outGngDetails) {

        JSONObject  jsonObject = new JSONObject();


        try {
            jsonObject.put("key",stockOutgoingentryKey_outGngDetails);
            jsonObject.put("outgoingtype", Constants.SALES_CANCELLED_OUTGOINGTYPE);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateStockOutgoingUsingKey
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );


                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change outgoing type as cancelled",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change outgoing type as cancelled",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);


    }

    private void UpdateStockBalanceinMenuItemStockAvlDetail(String key_avlDetails, double finalStockBalance_double, String currentTimeAndDate, boolean isChangeReceivedStock, double receivedStockValue_double) {

        JSONObject  jsonObject = new JSONObject();

        if(isChangeReceivedStock) {
            try {
                jsonObject.put("key", key_avlDetails);
                jsonObject.put("lastupdatedtime", currentTimeAndDate);
                jsonObject.put("stockbalance", finalStockBalance_double);
                jsonObject.put("receivedstock", receivedStockValue_double);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else{
            try {
                jsonObject.put("key", key_avlDetails);
                jsonObject.put("lastupdatedtime", currentTimeAndDate);
                jsonObject.put("stockbalance", finalStockBalance_double);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_MenuItemStockAvlDetails
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );


                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change Stock balance",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to change Stock balance",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);


    }

    private void AddDataInStockBalanceTransactionHistory(double finalStockBalance_double, double stockBalanceBeforeMinusCurrentItem, String menuitemkey_avlDetail, String stockincomingKeyAvlDetail, String itemname_outGngDetails, String barcode_avlDetail, String currentTimeAndDate) {

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode",String.valueOf(barcode_avlDetail));
            jsonObject.put("itemname", String.valueOf(itemname_outGngDetails));
            jsonObject.put("transactiontime",String.valueOf(currentTimeAndDate));
            jsonObject.put("menuitemkey", String.valueOf(menuitemkey_avlDetail));
            jsonObject.put("newstockbalance",finalStockBalance_double);
            jsonObject.put("oldstockbalance", stockBalanceBeforeMinusCurrentItem);
            jsonObject.put("stockincomingkey",String.valueOf(stockincomingKeyAvlDetail));
           jsonObject.put("usermobileno", String.valueOf(vendorUserMobileno ));
            jsonObject.put("vendorkey", String.valueOf(vendorKey));




        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addEntry_StockTransactionHistory
                , jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );

                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to Add Data in Stock Balance Transaction History table",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,"Failed to Add Data in Stock Balance Transaction History table",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);






    }


    private void getAreawiseVendorName() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofVendors +"?modulename=Store",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            result  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            //Log.d("Constants.TAG", "Response: " + arrayLength);
                            VendorName_arrayList.add(" nil ");
                            Modal_vendor empty_modal_vendor = new Modal_vendor();
                            empty_modal_vendor.setVendorname(" nil ");
                            empty_modal_vendor.setKey(" nil ");
                            vendorList.add(empty_modal_vendor);
                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    try{
                                        if(json.has("vendortype")) {
                                            vendortype = String.valueOf(json.get("vendortype")).toUpperCase();
                                        }
                                        else{
                                            vendortype = "";
                                        }
                                    }
                                    catch (Exception e){
                                        vendortype = "";
                                        e.printStackTrace();
                                    }
                                    if(!vendortype.toString().equals(Constants.Warehouse_VendorType)) {
                                        try {
                                            vendorNameString = String.valueOf(json.get("name"));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        //Log.d(Constants.TAG, "JsonName: " + pos_vendorNameString);

                                        if ((!VendorName_arrayList.contains(vendorNameString))) {
                                            VendorName_arrayList.add(vendorNameString);

                                        }

                                        Modal_vendor modal_vendor = new Modal_vendor();


                                        try {

                                            if(json.has("name")){

                                                modal_vendor.setVendorname( String.valueOf(json.get("name")));

                                            }
                                            else{
                                                modal_vendor.setVendorname( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setVendorname( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {

                                            if(json.has("vendortype")){

                                                modal_vendor.setVendortype( String.valueOf(json.get("vendortype")));

                                            }
                                            else{
                                                modal_vendor.setVendortype( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setVendortype( String.valueOf(""));
                                            e.printStackTrace();
                                        }



                                        try {

                                            if(json.has("minimumscreensizeforpos")){

                                                modal_vendor.setMinimumscreensizeforpos( String.valueOf(json.get("minimumscreensizeforpos")));

                                            }
                                            else{
                                                modal_vendor.setMinimumscreensizeforpos( String.valueOf(Constants.default_mobileScreenSize));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setMinimumscreensizeforpos( String.valueOf(Constants.default_mobileScreenSize));
                                            e.printStackTrace();
                                        }


                                        try {

                                            if(json.has("vendormobile")){

                                                modal_vendor.setVendormobile( String.valueOf(json.get("vendormobile")));

                                            }
                                            else{
                                                modal_vendor.setVendormobile( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setVendormobile( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {

                                            if(json.has("status")){
                                                modal_vendor.setStatus( String.valueOf(json.get("status")));


                                            }
                                            else{
                                                modal_vendor.setStatus( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_vendor.setStatus( String.valueOf(""));

                                        }


                                        try {
                                            if(json.has("key")){
                                                modal_vendor.setKey( String.valueOf(json.get("key")));


                                            }
                                            else{
                                                modal_vendor.setKey( String.valueOf(""));
                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setKey( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("inventorycheck")){
                                                modal_vendor.setInventorycheck( String.valueOf(json.get("inventorycheck")));


                                            }
                                            else{
                                                modal_vendor.setInventorycheck( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setInventorycheck( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("istestvendor")){

                                                modal_vendor.setIstestvendor( String.valueOf(json.get("istestvendor")));

                                            }
                                            else{
                                                modal_vendor.setIstestvendor( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setIstestvendor( String.valueOf(""));


                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("pincode")){

                                                modal_vendor.setPincode( String.valueOf(json.get("pincode")));

                                            }
                                            else{
                                                modal_vendor.setPincode( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setPincode( String.valueOf(""));


                                            e.printStackTrace();
                                        }

///////////////////////////////////
                                        try {
                                            if(json.has("addressline1")){

                                                modal_vendor.setAddressline1( String.valueOf(json.get("addressline1")));

                                            }
                                            else{
                                                modal_vendor.setAddressline1( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setAddressline1( String.valueOf(""));


                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("addressline2")){

                                                modal_vendor.setAddressline2( String.valueOf(json.get("addressline2")));

                                            }
                                            else{
                                                modal_vendor.setAddressline2( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setAddressline2( String.valueOf(""));


                                            e.printStackTrace();
                                        }



                                        try {
                                            if(json.has("vendorfssaino")){

                                                modal_vendor.setVendorfssaino( String.valueOf(json.get("vendorfssaino")));

                                            }
                                            else{
                                                modal_vendor.setVendorfssaino( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setVendorfssaino( String.valueOf(""));


                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("defaultprintertype")){

                                                modal_vendor.setDefaultprintertype( String.valueOf(json.get("defaultprintertype")));

                                            }
                                            else{
                                                modal_vendor.setDefaultprintertype( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setDefaultprintertype( String.valueOf(""));


                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("locationlat")){

                                                modal_vendor.setLocationlat( String.valueOf(json.get("locationlat")));

                                            }
                                            else{
                                                modal_vendor.setLocationlat( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setLocationlat( String.valueOf(""));


                                            e.printStackTrace();
                                        }




                                        try {
                                            if(json.has("locationlong")){

                                                modal_vendor.setLocationlong( String.valueOf(json.get("locationlong")));

                                            }
                                            else{
                                                modal_vendor.setLocationlong( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setLocationlong( String.valueOf(""));


                                            e.printStackTrace();
                                        }



                                        try {
                                            if(json.has("inventorycheckpos")){

                                                modal_vendor.setInventorycheckpos( String.valueOf(json.get("inventorycheckpos")));

                                            }
                                            else{
                                                modal_vendor.setInventorycheckpos( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setInventorycheckpos( String.valueOf(""));


                                            e.printStackTrace();
                                        }



                                        vendorList.add(modal_vendor);


                                    }
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
                        vendorlist_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, VendorName_arrayList);
                        Adjusting_Widgets_Visibility(false);

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

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




    private void updateDatainLocalArray(Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class) {


        try {
            if (Edit_Or_CancelTheOrders.showcreditorderscheckbox.isChecked()) {
                if (Edit_Or_CancelTheOrders.isSearchButtonClicked) {
                    for (int i = 0; i < Edit_Or_CancelTheOrders.sorted_CreditedOrdersList.size(); i++) {
                        final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.sorted_CreditedOrdersList.get(i);
                        String orderkey = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                        if (orderkey.equals(orderid)) {
                            modal_manageOrders_forOrderDetailList1.setOrderconfirmedtime(modal_manageOrders_pojo_class.getOrderconfirmedtime());
                            modal_manageOrders_forOrderDetailList1.setOrderreadytime(modal_manageOrders_pojo_class.getOrderreadytime());
                            modal_manageOrders_forOrderDetailList1.setOrderpickeduptime(modal_manageOrders_pojo_class.getOrderpickeduptime());

                            modal_manageOrders_forOrderDetailList1.setOrderdeliveredtime(modal_manageOrders_pojo_class.getOrderdeliveredtime());

                            modal_manageOrders_forOrderDetailList1.setKeyfromtrackingDetails(modal_manageOrders_pojo_class.getKeyfromtrackingDetails());
                            modal_manageOrders_forOrderDetailList1.setOrderstatus(modal_manageOrders_pojo_class.getOrderstatus());
                            modal_manageOrders_forOrderDetailList1.setDeliverydistance(modal_manageOrders_pojo_class.getDeliverydistance());
                            modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(modal_manageOrders_pojo_class.getOrderdeliveredtime());
                            modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(modal_manageOrders_pojo_class.getDeliveryPartnerMobileNo());
                            modal_manageOrders_forOrderDetailList1.setUseraddresslon(modal_manageOrders_pojo_class.getUseraddresslon());
                            modal_manageOrders_forOrderDetailList1.setUseraddresslat(modal_manageOrders_pojo_class.getUseraddresslat());
                            modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(modal_manageOrders_pojo_class.getDeliveryPartnerKey());


                            modal_manageOrders_forOrderDetailList1.setIsdataFetchedFromOrderTrackingDetails("TRUE");

                        }
                    }
                }

                for (int i = 0; i < Edit_Or_CancelTheOrders.CreditedordersList.size(); i++) {
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.CreditedordersList.get(i);
                    String orderkey = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                    if (orderkey.equals(orderid)) {
                        modal_manageOrders_forOrderDetailList1.setOrderconfirmedtime(modal_manageOrders_pojo_class.getOrderconfirmedtime());
                        modal_manageOrders_forOrderDetailList1.setOrderreadytime(modal_manageOrders_pojo_class.getOrderreadytime());
                        modal_manageOrders_forOrderDetailList1.setOrderpickeduptime(modal_manageOrders_pojo_class.getOrderpickeduptime());

                        modal_manageOrders_forOrderDetailList1.setOrderdeliveredtime(modal_manageOrders_pojo_class.getOrderdeliveredtime());
                        modal_manageOrders_forOrderDetailList1.setOrderstatus(modal_manageOrders_pojo_class.getOrderstatus());
                        modal_manageOrders_forOrderDetailList1.setDeliverydistance(modal_manageOrders_pojo_class.getDeliverydistance());
                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(modal_manageOrders_pojo_class.getOrderdeliveredtime());
                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(modal_manageOrders_pojo_class.getDeliveryPartnerMobileNo());
                        modal_manageOrders_forOrderDetailList1.setUseraddresslon(modal_manageOrders_pojo_class.getUseraddresslon());
                        modal_manageOrders_forOrderDetailList1.setUseraddresslat(modal_manageOrders_pojo_class.getUseraddresslat());
                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(modal_manageOrders_pojo_class.getDeliveryPartnerKey());
                        modal_manageOrders_forOrderDetailList1.setKeyfromtrackingDetails(modal_manageOrders_pojo_class.getKeyfromtrackingDetails());
                        modal_manageOrders_forOrderDetailList1.setIsdataFetchedFromOrderTrackingDetails("TRUE");


                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Edit_Or_CancelTheOrders.adapter_edit_or_cancelTheOrders.notifyDataSetChanged();

        setDatainUI();


    }




    private void setDatainUI() {


        try {
            orderStatustext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderstatus()));

            slotNametext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotname()));
            slotDatetext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotdate()));

            delivery_type_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getDeliverytype()));
        }catch (Exception e){
            e.printStackTrace();
        }


        try{
            orderdetailsKey = String.valueOf(modal_manageOrders_pojo_class.getOrderdetailskey());

        }
        catch (Exception e){
            orderdetailsKey ="";
            e.printStackTrace();
            Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                    "orderdetailsKey   is  empty", Toast.LENGTH_SHORT).show();
        }

        try{
            orderTrackingDetailskey = String.valueOf(modal_manageOrders_pojo_class.getKeyfromtrackingDetails());

        }
        catch (Exception e){
            e.printStackTrace();
            orderTrackingDetailskey="";
            Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                    "orderTrackingDetailskey   is  empty", Toast.LENGTH_SHORT).show();
        }



        try{
            paymentmode = String.valueOf(modal_manageOrders_pojo_class.getPaymentmode().toUpperCase());
            paymentTypetext_widget.setText(String.valueOf(paymentmode));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            deliveryPartnerNumber = String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerMobileNo());
            deliverypartnerName = String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerName());
            if (deliverypartnerName.equals(null)||deliverypartnerName.equals("null")) {
                deliverypartnerName = "Not Assigned";
                deliveryPartnerNumber = "Not Assigned";
            }

            deliveryPersonMobileNotext_widget.setText(deliveryPartnerNumber);
            deliveryPersonNametext_widget.setText(deliverypartnerName);
        } catch (Exception e1) {
            e1.printStackTrace();
            deliverypartnerName = "Not Assignedd";
            deliveryPartnerNumber = "Not Assignedd";


            deliveryPersonMobileNotext_widget.setText(deliveryPartnerNumber);
            deliveryPersonNametext_widget.setText(deliverypartnerName);
        }

        try {
            ordertype = String.valueOf(modal_manageOrders_pojo_class.getOrderType().toUpperCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isFromEditOrders.equals("FALSE")){
            changeOrderToAnotherStore.setVisibility(View.GONE);
            changePaymentMode_button.setVisibility(View.GONE);
            cancelOrder_button.setVisibility(View.GONE);
            changeDeliveryPartner.setVisibility(View.GONE);
        }
        else{
            changeDeliveryPartner.setVisibility(View.VISIBLE);
            if(ordertype.equals(Constants.APPORDER) || ordertype.equals(Constants.PhoneOrder)) {
                if ((UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.STOREMANAGER_ROLENAME)) || (UserRole.equals(Constants.ADMIN_ROLENAME))) {
                    if ((UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.STOREMANAGER_ROLENAME))) {
                        if ((UserPhoneNumber.equals("+916380050384")) ||(UserPhoneNumber.equals("+919597580128")) || (UserPhoneNumber.equals("+917010623119"))  )
                        {
                            cancelOrder_button.setVisibility(View.VISIBLE);
                        }
                        else {
                            cancelOrder_button.setVisibility(View.GONE);

                        }

                        if ((UserPhoneNumber.equals("+916380050384")) || (UserPhoneNumber.equals("+917010623119")) || (UserPhoneNumber.equals("+918939887159")) ||(UserPhoneNumber.equals("+919597580128"))  )
                        {
                            changePaymentMode_button.setVisibility(View.VISIBLE);
                        }
                        else {
                            changePaymentMode_button.setVisibility(View.GONE);

                        }


                    }
                    else {
                        cancelOrder_button.setVisibility(View.VISIBLE);
                        changePaymentMode_button .setVisibility(View.VISIBLE);
                    }
                } else {
                    cancelOrder_button.setVisibility(View.GONE);
                    changePaymentMode_button .setVisibility(View.GONE);
                }
            }
            else{
                 cancelOrder_button.setVisibility(View.GONE);
                if ((UserRole.equals(Constants.ADMIN_ROLENAME)) || (UserPhoneNumber.equals("+916380050384")) || (UserPhoneNumber.equals("+917010623119")) ) {
                    cancelOrder_button.setVisibility(View.VISIBLE);
                }
            }

           // changePaymentMode_button.setVisibility(View.VISIBLE);
        }
        if (ordertype.equals(Constants.POSORDER)) {
            showlocation.setVisibility(View.GONE);
            tokenNo_Layout.setVisibility(View.GONE);
            distanceInKm_layout.setVisibility(View.GONE);
            slotdateLayout.setVisibility(View.GONE);
            confirmedTimeLayout.setVisibility(View.GONE);
            readyTimeLayout.setVisibility(View.GONE);
            pickedTimeLayout.setVisibility(View.GONE);
            slotTimeLayout.setVisibility(View.GONE);
            AddressLayout .setVisibility(View.GONE);
        }

        if (ordertype.equals(Constants.APPORDER)  || ordertype.equals(Constants.PhoneOrder)) {
            customerlatitude = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslat());
            customerLongitutde = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslon());
            try {
                deliverydistance = String.valueOf(modal_manageOrders_pojo_class.getDeliverydistance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ((!deliverydistance.equals("0") && (deliverydistance.length() > 0) && (!deliverydistance.equals("null")))) {
                distancebetweencustomer_vendortext_widget.setText(deliverydistance + "Km");

            } else {
                try {
                    CalculateDistanceviaApi(distancebetweencustomer_vendortext_widget);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        } else {
            deliverypersonName_Layout.setVisibility(View.GONE);
            deliverypersonMobileNO_Layout.setVisibility(View.GONE);
        }

        String order_status = modal_manageOrders_pojo_class.getOrderstatus().toUpperCase();
        String order_type =modal_manageOrders_pojo_class.getOrderType().toUpperCase();
        ordertypetext_widget.setText(order_type);

        if(modal_manageOrders_pojo_class.getUseraddress()!=null){
            AddressLine1_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getUseraddress()));
        }
        else{
            AddressLine1_textwidget.setText("");

        }
        if(modal_manageOrders_pojo_class.getOrderplacedtime()!=null){
            orderplacedtime_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderplacedtime()));
        }
        else{
            orderplacedtime_textwidget.setText("");

        }
        if(modal_manageOrders_pojo_class.getTokenno()!=null){
            tokenNo = (String.valueOf(modal_manageOrders_pojo_class.getTokenno()));
            tokenNotext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getTokenno()));
        }
        else{
            tokenNotext_widget.setText("");
            tokenNo = "";
        }


        if(modal_manageOrders_pojo_class.getUsermobile()!=null){
            mobileNotext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getUsermobile()));
            customerMobileNo =String.valueOf(modal_manageOrders_pojo_class.getUsermobile());

        }
        else{
            mobileNotext_widget.setText("");
            customerMobileNo = "";
        }

        if(modal_manageOrders_pojo_class.getSlottimerange()!=null){
            slotTime_Range_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlottimerange()));
        }
        else{
            slotTime_Range_textwidget.setText("");

        }
        if(modal_manageOrders_pojo_class.getOrderconfirmedtime()!=null){
            orderConfirmedtime_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderconfirmedtime()));
        }
        else{
            orderConfirmedtime_textwidget.setText("");

        }
        if(modal_manageOrders_pojo_class.getOrderreadytime()!=null){
            orderReaytime_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderreadytime()));
        }
        else{
            orderReaytime_textwidget.setText("");

        }
        if(modal_manageOrders_pojo_class.getOrderpickeduptime()!=null){
            orderpickeduptime_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderpickeduptime()));
        }
        else{
            orderpickeduptime_textwidget.setText("");

        }

        if(modal_manageOrders_pojo_class.getDeliveryamount()!=null){
            deliveryCharges = String.valueOf(modal_manageOrders_pojo_class.getDeliveryamount());
            deliveryCharges_text_widget.setText(deliveryCharges+".00");

        }
        else {
            deliveryCharges = "0.00";
            deliveryCharges_text_widget.setText(deliveryCharges);

        }


        if(modal_manageOrders_pojo_class.getCoupondiscamount()!=null){
            coupondiscountAmount = String.valueOf(modal_manageOrders_pojo_class.getCoupondiscamount());
            discounttext_widget.setText(coupondiscountAmount);
        }
        else {
            coupondiscountAmount = "0.00";
            discounttext_widget.setText(coupondiscountAmount);
        }
        if(modal_manageOrders_pojo_class.getOrderdeliveredtime()!=null){
            orderDeliveredtime_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderdeliveredtime()));
        }
        else{
            orderDeliveredtime_textwidget.setText("");
        }


        try{

            OrderdItems_desp.clear();
            String itemDespString = modal_manageOrders_pojo_class.getItemdesp_string();
            try {
                JSONArray jsonArray = new JSONArray(itemDespString);
                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String subCtgyKey ="";
                    Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    if (json.has("netweight")) {
                        manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("netweight"));

                    }
                    else{
                        manageOrders_pojo_class.ItemFinalWeight = "";

                    }

                    try {
                        if(json.has("tmcsubctgykey")) {
                            subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                        }
                        else {
                            subCtgyKey = " ";
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    if(subCtgyKey.equals("tmcsubctgy_16")){
                        //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                        manageOrders_pojo_class.itemName = "Grill House  "+String.valueOf(json.get("itemname"));

                    }
                    else  if(subCtgyKey.equals("tmcsubctgy_15")){
                        // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                        manageOrders_pojo_class.itemName = "Ready to Cook "+String.valueOf(json.get("itemname"));

                    }
                    else{
                        manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));

                    }
                    manageOrders_pojo_class.ItemFinalPrice= String.valueOf(json.get("tmcprice"));
                    manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                    manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));



                    if(json.has("marinadeitemdesp")) {
                        JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                        Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                        try {
                            if(json.has("tmcsubctgykey")) {
                                subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                            }
                            else {
                                subCtgyKey = " ";
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                            marinades_manageOrders_pojo_class.itemName=" Grill House  "+marinadesObject.getString("itemname");

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                            marinades_manageOrders_pojo_class.itemName=" Ready to Cook "+marinadesObject.getString("itemname");

                        }
                        else{
                            marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname");

                        }
                        marinades_manageOrders_pojo_class.ItemFinalPrice= marinadesObject.getString("tmcprice");
                        marinades_manageOrders_pojo_class.quantity =String.valueOf(json.get("quantity"));//using same marinade quantity for the meat item also
                        marinades_manageOrders_pojo_class.GstAmount = marinadesObject.getString("gstamount");
                        if (json.has("netweight")) {
                            marinades_manageOrders_pojo_class.ItemFinalWeight = marinadesObject.getString("netweight");

                        }
                        else{
                            marinades_manageOrders_pojo_class.ItemFinalWeight = "";

                        }

                        OrderdItems_desp.add(marinades_manageOrders_pojo_class);

                    }

                    OrderdItems_desp.add(manageOrders_pojo_class);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }





            add_amount_ForBillDetails(OrderdItems_desp);
            adapter_forOrderDetails_listview = new Adapter_Mobile_orderDetails_itemDesp_listview1(Edit_Or_CancelOrder_OrderDetails_Screen.this, OrderdItems_desp);
            itemDesp_listview.setAdapter(adapter_forOrderDetails_listview);
            Helper.getListViewSize(itemDesp_listview, screenInches,0);

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    private Modal_ManageOrders_Pojo_Class getOrderTrackingDetailsAlso(Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class, String orderid) {
        Adjusting_Widgets_Visibility(true);
         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_orderid + orderid,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            try{

                                JSONArray JArray  = response.getJSONArray("content");
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        if(json.has("key")){
                                            modal_manageOrders_pojo_class.keyfromtrackingDetails = String.valueOf(json.get("key"));

                                        }
                                        else{

                                            modal_manageOrders_pojo_class.keyfromtrackingDetails ="";
                                        }


                                        if(json.has("orderStatus")){
                                            modal_manageOrders_pojo_class.orderstatus = String.valueOf(json.get("orderStatus"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderstatus ="";
                                        }


                                        if(json.has("usermobile")){
                                            modal_manageOrders_pojo_class.usermobile =  String.valueOf(json.get("usermobile"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.usermobile ="";
                                        }
                                        if(json.has("orderdeliverytime")){
                                            modal_manageOrders_pojo_class.orderdeliveredtime =  String.valueOf(json.get("orderdeliverytime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderdeliveredtime ="";
                                        }
                                        if(json.has("useraddresskey")){
                                            modal_manageOrders_pojo_class.useraddresskey =  String.valueOf(json.get("useraddresskey"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.useraddresskey ="";
                                        }


                                        if(json.has("orderreadytime")){
                                            modal_manageOrders_pojo_class.orderreadytime = String.valueOf(json.get("orderreadytime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderreadytime ="";
                                        }


                                        if(json.has("orderpickeduptime")){
                                            modal_manageOrders_pojo_class.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderpickeduptime ="";
                                        }


                                        if(json.has("orderconfirmedtime")){
                                            modal_manageOrders_pojo_class.orderconfirmedtime =  String.valueOf(json.get("orderconfirmedtime"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.orderconfirmedtime ="";
                                        }

                                        if(json.has("useraddresslat")){
                                            modal_manageOrders_pojo_class.useraddresslat =  String.valueOf(json.get("useraddresslat"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.useraddresslat ="";
                                        }


                                        if(json.has("useraddresslong")){
                                            modal_manageOrders_pojo_class.useraddresslon =  String.valueOf(json.get("useraddresslong"));

                                        }
                                        else{
                                            modal_manageOrders_pojo_class.useraddresslon ="";
                                        }


                                        try {
                                            if (ordertype.toUpperCase().equals(Constants.APPORDER)  || ordertype.equals(Constants.PhoneOrder)) {
                                                if (json.has("useraddress")) {

                                                    String addresss =  String.valueOf(json.get("useraddress"));
                                                    if(!addresss.equals(null)&&(!addresss.equals("null"))){
                                                        modal_manageOrders_pojo_class.useraddress = String.valueOf(json.get("useraddress"));

                                                    }
                                                    else {
                                                        modal_manageOrders_pojo_class.useraddress ="";

                                                    }
                                                } else {
                                                    modal_manageOrders_pojo_class.useraddress = "";
                                                }

                                            }
                                        }catch (Exception E){
                                            modal_manageOrders_pojo_class.useraddress ="-";
                                            E.printStackTrace();
                                        }


                                        try {
                                            if (ordertype.toUpperCase().equals(Constants.APPORDER) || ordertype.equals(Constants.PhoneOrder)) {


                                                if (json.has("deliverydistance")) {

                                                    String deliverydistance = String.valueOf(json.get("deliverydistance"));
                                                    if (!deliverydistance.equals(null) && (!deliverydistance.equals("null"))) {
                                                        modal_manageOrders_pojo_class.deliverydistance = String.valueOf(json.get("deliverydistance"));

                                                    } else {
                                                        modal_manageOrders_pojo_class.deliverydistance = "0";

                                                    }
                                                } else {
                                                    modal_manageOrders_pojo_class.deliverydistance = "0";
                                                }


                                            }
                                        } catch (Exception E) {
                                            modal_manageOrders_pojo_class.deliverydistance = "0";
                                            E.printStackTrace();
                                        }


                                        if(json.has("orderStatus")) {
                                            if (!String.valueOf(json.get("orderStatus")).equals("NEW")) {

                                                if (json.has("deliveryusername")) {
                                                    modal_manageOrders_pojo_class.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));

                                                }
                                                if (json.has("deliveryuserkey")) {
                                                    modal_manageOrders_pojo_class.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));
                                                    ;

                                                }
                                                if (json.has("deliveryusermobileno")) {
                                                    modal_manageOrders_pojo_class.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));

                                                }


                                            }
                                        }






                                    }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }


                                    }

                                    }
                            catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "There is no Credited Orders " , Toast.LENGTH_LONG).show();


                    Adjusting_Widgets_Visibility(false);

                    error.printStackTrace();


                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_1");
                params.put("orderplacedtime", "11 Jan 2021");

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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);






        return modal_manageOrders_pojo_class;
    }

    private void showBottomSheetDialog_deliveryPartnerList(String orderkey, String deliverypartnerName, String orderid, String customerMobileNo, String vendorkey) {
        Adjusting_Widgets_Visibility(true);
        if(deliveryPartnerList.size()>0) {
        String fromActivityName="";
        bottomSheetDialog = new BottomSheetDialog(Edit_Or_CancelOrder_OrderDetails_Screen.this);
        bottomSheetDialog.setContentView(R.layout.mobilescreen_assigndeliverypartner_bottom_sheet_dialog);

        ListView ListView1 = bottomSheetDialog.findViewById(R.id.listview);

        Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(Edit_Or_CancelOrder_OrderDetails_Screen.this, deliveryPartnerList,orderkey,fromActivityName+"EditOrders",deliverypartnerName,orderid,customerMobileNo,vendorkey);


        ListView1.setAdapter(adapter_mobile_assignDeliveryPartner1);

        bottomSheetDialog.show();
        }
        else{

            getDeliveryPartnerList(true , orderkey,deliverypartnerName,orderid,customerMobileNo,vendorkey);

        }
    }

    private void showBottomSheetDialog(String paymentmode, String ordertype ,String orderid) {

        bottomSheetDialog = new BottomSheetDialog(Edit_Or_CancelOrder_OrderDetails_Screen.this);
        bottomSheetDialog.setContentView(R.layout.change_paymentmode_bottomsheet_dialog);
        RadioGroup pos_radioGroup = bottomSheetDialog.findViewById(R.id.posOrders_radiogrp);
        RadioGroup app_radioGroup = bottomSheetDialog.findViewById(R.id.appOrders_radiogrp);
        RadioButton phonepe_phoneorder_radionbutton  = bottomSheetDialog.findViewById(R.id.phonepe_phoneorder);
        Button changePaymentMode = bottomSheetDialog.findViewById(R.id.ChangePaymentMode);

        if(ordertype.equals(Constants.APPORDER)){
            pos_radioGroup.setVisibility(View.GONE);
            pos_radioGroup.setSelected(false);
            app_radioGroup.setVisibility(View.VISIBLE);

            if(paymentmode.equals(Constants.CASH_ON_DELIVERY)){
                app_radioGroup.check(R.id.cash_on_Delivery);
            }
            else if (paymentmode.equals(Constants.PHONEPE)){

                app_radioGroup.check(R.id.phonepe);

            }
            else if(paymentmode.equals(Constants.RAZORPAY)){
                app_radioGroup.check(R.id.razorpay);

            }
            else if(paymentmode.equals(Constants.PAYTM)){
                app_radioGroup.check(R.id.paytm);

            }

            else if(paymentmode.equals(Constants.CREDIT)){
                app_radioGroup.check(R.id.credit);

            }


            else{
                app_radioGroup.check(R.id.cash_on_Delivery);
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                        paymentmode+"   is not correct", Toast.LENGTH_SHORT).show();
            }

        }
        if(ordertype.equals(Constants.POSORDER) || ordertype.equals(Constants.PhoneOrder)){
            app_radioGroup.setVisibility(View.GONE);
            app_radioGroup.setSelected(false);
            pos_radioGroup.setVisibility(View.VISIBLE);
            if(ordertype.equals(Constants.PhoneOrder)){
                phonepe_phoneorder_radionbutton.setVisibility(View.VISIBLE);

            }
            else {
                phonepe_phoneorder_radionbutton.setVisibility(View.GONE);
            }
            if(paymentmode.equals(Constants.CASH_ON_DELIVERY)){
                pos_radioGroup.check(R.id.cash);
            }

            else if(paymentmode.equals(Constants.CARD)){
                pos_radioGroup.check(R.id.card);

            }
            else if(paymentmode.equals(Constants.UPI)){
                pos_radioGroup.check(R.id.upi);

            }
            else if(paymentmode.equals(Constants.CREDIT)){
                pos_radioGroup.check(R.id.credit_pos);

            }
            else if(paymentmode.equals(Constants.PHONEPE)){
                pos_radioGroup.check(R.id.phonepe_phoneorder);

            }




            else{
                pos_radioGroup.check(R.id.cash);
                Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                        paymentmode+"   is not correct", Toast.LENGTH_SHORT).show();
            }
        }

        app_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.cash_on_Delivery:
                        paymentModeString = Constants.CASH_ON_DELIVERY;

                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.phonepe:

                        paymentModeString = Constants.PHONEPE;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.razorpay:
                        paymentModeString = Constants.RAZORPAY;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.paytm:

                        paymentModeString = Constants.PAYTM;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.credit:

                        paymentModeString = Constants.CREDIT;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;

                }
            }

        });

        pos_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.cash:
                        paymentModeString = Constants.CASH_ON_DELIVERY;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.card:

                        paymentModeString = Constants.CARD;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.upi:
                        paymentModeString = Constants.UPI;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.credit_pos:

                        paymentModeString = Constants.CREDIT;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.phonepe_phoneorder:

                        paymentModeString = Constants.PHONEPE;
                        Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this,
                                paymentModeString, Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });


        changePaymentMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Adjusting_Widgets_Visibility(true);
                AddNewEntryinPaymentTransaction("","","",modal_manageOrders_pojo_class.getUsermobile().toString() ,orderid,paymentModeString,"","SUCCESS",payableAmount,getDate_and_time(),userkey);
                String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                 vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                if(paymentModeString.toUpperCase().equals(Constants.CREDIT)){
                    CheckCreditTransactionsForthisOrderid(customerMobileNo,orderid);
                }
                else{
                    Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid,customerMobileNo,vendorkey);

                }
                //Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid,customerMobileNo,vendorkey);
            }
        });



        bottomSheetDialog.show();
    }

    private void AddNewEntryinPaymentTransaction(String desp, String merchantorderid, String merchantpaymentid, String mobileno, String orderid, String paymentmode, String paymenttype, String status, String transactionamount, String transactiontime, String userkey) {
        Adjusting_Widgets_Visibility(true);

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
            jsonObject.put("mobileno", mobileno);
            jsonObject.put("merchantorderid", merchantorderid);
            jsonObject.put("paymentmode", paymentmode);
            jsonObject.put("paymenttype", paymenttype);
            jsonObject.put("transactiontime", transactiontime);
            jsonObject.put("transactionamount", transactionamount);
            jsonObject.put("status", status);
            jsonObject.put("merchantpaymentid", merchantpaymentid);
            jsonObject.put("userkey", userkey);
            jsonObject.put("desp", desp);



        }


        catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInPaymentDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                try {
                    String message = response.getString("message");
                    if(message .equals( "success")){
                        // printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                        Adjusting_Widgets_Visibility(false);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Adjusting_Widgets_Visibility(false);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);







    }




    public void getDeliveryPartnerList(boolean openBottomSheet, String orderkey, String deliverypartnerName, String orderid, String customerMobileNo, String vendorkey) {
        if(isDeliveryPartnerMethodCalled){
            return;
        }
        isDeliveryPartnerMethodCalled = true;


        SharedPreferences preferences =getSharedPreferences("DeliveryPersonList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryPartnerList+vendorkey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //converting jsonSTRING into array
                            String DeliveryPersonListString = response.toString();
                            ConvertStringintoDeliveryPartnerListArray(DeliveryPersonListString);

                            SharedPreferences sharedPreferences
                                    = getSharedPreferences("DeliveryPersonList",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor myEdit
                                    = sharedPreferences.edit();


                            myEdit.putString(
                                    "DeliveryPersonListString",
                                    DeliveryPersonListString);
                            myEdit.apply();
                            isDeliveryPartnerMethodCalled = false;
                            if(openBottomSheet){
                                showBottomSheetDialog_deliveryPartnerList(orderkey,deliverypartnerName,orderid,customerMobileNo,vendorkey);
                            }
                        } catch (Exception e) {
                            isDeliveryPartnerMethodCalled = false;
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
                String errorCode = "";
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
                Toast.makeText(getApplicationContext(),"Error in Delivery Partner list :  "+errorCode,Toast.LENGTH_LONG).show();
                isDeliveryPartnerMethodCalled = false;


                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", vendorkey);
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
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);
    }





    private void ConvertStringintoDeliveryPartnerListArray(String deliveryPersonList) {
        if ((!deliveryPersonList.equals("") )|| (!deliveryPersonList.equals(null))) {
            try {
                String ordertype = "#", orderid = "";
                //  sorted_OrdersList.clear();

                //converting jsonSTRING into array
                JSONObject jsonObject = new JSONObject(deliveryPersonList);
                JSONArray JArray = jsonObject.getJSONArray("content");
                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                int i1 = 0;
                int arrayLength = JArray.length();
                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                for (; i1 < (arrayLength); i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass = new AssignDeliveryPartner_PojoClass();
                        assignDeliveryPartner_pojoClass.deliveryPartnerStatus = String.valueOf(json.get("status"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerKey = String.valueOf(json.get("key"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("mobileno"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerName = String.valueOf(json.get("name"));

                        // //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                        deliveryPartnerList.add(assignDeliveryPartner_pojoClass);

                        //  Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(MobileScreen_AssignDeliveryPartner1.this, deliveryPartnerList, orderKey,IntentFrom);

                        //deliveryPartners_list_widget.setAdapter(adapter_mobile_assignDeliveryPartner1);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                try{
                    Collections.sort(deliveryPartnerList, new Comparator<AssignDeliveryPartner_PojoClass>() {
                        public int compare(AssignDeliveryPartner_PojoClass result1, AssignDeliveryPartner_PojoClass result2) {
                            return result1.getDeliveryPartnerName().compareTo(result2.getDeliveryPartnerName());
                        }
                    });
                }
                catch (Exception e ){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private void ChangeStatusOftheOrder(String changestatusto, String OrderKey, String currenttime, String orderid, String customerMobileNo, String vendorkey) {
        JSONObject  jsonObject = new JSONObject();
        String Api_toChangeStatusinTrackingDetailsUsingOrderid = "";


        if(orderdetailsnewschema){
           try {
               jsonObject.put("orderid", orderid);
               jsonObject.put("orderstatus", changestatusto);
               jsonObject.put("vendorkey", vendorkey);

               jsonObject.put("ordercancelledtime", currenttime);




           } catch (JSONException e) {
               e.printStackTrace();
               Log.d(Constants.TAG, "JSONOBJECT: " + e);

           }


            Api_toChangeStatusinTrackingDetailsUsingOrderid = Constants.api_UpdateVendorTrackingOrderDetails;

            JSONObject customerDetails_JsonObject = new JSONObject();

            try {

                customerDetails_JsonObject.put("orderid", orderid);
                customerDetails_JsonObject.put("orderstatus", changestatusto);
                customerDetails_JsonObject.put("usermobileno", customerMobileNo);

                customerDetails_JsonObject.put("ordercancelledtime", currenttime);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            String apiToUpdateCustomerOrderDetails = Constants.api_UpdateCustomerTrackingOrderDetails +"?usermobileno="+customerMobileNo+"&orderid="+orderid;

            initUpdateCustomerOrderDetailsInterface(mContext);
            Update_CustomerOrderDetails_TrackingTable_AsyncTask asyncTask_TO_update =new Update_CustomerOrderDetails_TrackingTable_AsyncTask(mContext, mResultCallback_UpdateCustomerOrderDetailsTableInterface,customerDetails_JsonObject,apiToUpdateCustomerOrderDetails );
            asyncTask_TO_update.execute();

       }
       else{
           try {
               jsonObject.put("key", OrderKey);
               jsonObject.put("orderstatus", changestatusto);

               jsonObject.put("ordercancelledtime", currenttime);




           } catch (JSONException e) {
               e.printStackTrace();
               Log.d(Constants.TAG, "JSONOBJECT: " + e);

           }


            Api_toChangeStatusinTrackingDetailsUsingOrderid = Constants.api_updateTrackingOrderTable;
       }


        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_toChangeStatusinTrackingDetailsUsingOrderid,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                String msg = null;
                try {
                    msg = String.valueOf(response.get("message"));
                    if(msg.equals("success")) {

                        Log.d(Constants.TAG, "Responsewwwww 1 : " + response);

                        updateChangesinLocalArray(orderid, Constants.CANCELLED_ORDER_STATUS, "OrderStatus");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                Log.d(Constants.TAG, "Response 1 : " + msg);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);

    }
    private void initUpdateCustomerOrderDetailsInterface(Context mContext) {

        mResultCallback_UpdateCustomerOrderDetailsTableInterface  = new Update_CustomerOrderDetails_TrackingTableInterface() {
            @Override
            public void notifySuccess(String requestType, String success) {
                try{
                 //   Toast.makeText(mContext, "Succesfully Updated in Customer Details", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                try{
                   // Toast.makeText(mContext, "Failed to Updated in Customer Details", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
    }


    private void Change_Payment_Mode_Of_the_Order(String orderdetailsKey, String paymentModeString, String orderid, String customerMobileNo, String vendorkey) {

        JSONObject  jsonObject = new JSONObject();
        String Api_toUpdateinOrderDetailsUsingOrderid = "";

        if(orderdetailsnewschema){
            try {
                jsonObject.put("orderid", orderid);
                jsonObject.put("vendorkey", vendorkey);

                jsonObject.put("paymentmode", paymentModeString);




            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "JSONOBJECT: " + e);

            }


            Api_toUpdateinOrderDetailsUsingOrderid = Constants.api_UpdateVendorOrderDetails+"?usermobileno="+ customerMobileNo +"&orderid="+orderid;

            JSONObject customerDetails_JsonObject = new JSONObject();

            try {

                customerDetails_JsonObject.put("orderid", orderid);
                customerDetails_JsonObject.put("usermobileno", customerMobileNo);

                customerDetails_JsonObject.put("paymentmode", paymentModeString);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            String apiToUpdateCustomerOrderDetails = Constants.api_UpdateCustomerOrderDetails +"?usermobileno="+ customerMobileNo +"&orderid="+orderid;

            initUpdateCustomerOrderDetailsInterface(mContext);
            Update_CustomerOrderDetails_TrackingTable_AsyncTask asyncTask_TO_update =new Update_CustomerOrderDetails_TrackingTable_AsyncTask(mContext, mResultCallback_UpdateCustomerOrderDetailsTableInterface,customerDetails_JsonObject,apiToUpdateCustomerOrderDetails );
            asyncTask_TO_update.execute();

        }
        else{
            try {
                jsonObject.put("key", orderdetailsKey);
                jsonObject.put("paymentmode", paymentModeString);
                Log.i("tag","listenertoken"+ "");


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "JSONOBJECT: " + e);
                Adjusting_Widgets_Visibility(false);

            }

            Api_toUpdateinOrderDetailsUsingOrderid = Constants.api_Update_OrderDetails;
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_toUpdateinOrderDetailsUsingOrderid,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                String msg = null;
                try {
                    Log.d(Constants.TAG, "Responsewwwww: " + response);

                    msg = String.valueOf(response.get("message"));
                    if(msg.equals("success")) {

                        paymentmode = paymentModeString;
                        updateChangesinLocalArray(orderid, paymentModeString, "PaymentMode");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                 Log.d(Constants.TAG, "Response: " + msg);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);


    }

    private void CheckCreditTransactionsForthisOrderid(String customerMobileNo, String orderid) {
        String  customerMobileNoo ="";
        try {
            customerMobileNoo = URLEncoder.encode(customerMobileNo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetCreditOrdersTransactionDetailsUsingMobilenoWithOrderid +"?usermobileno="+customerMobileNoo+"&orderid="+orderid, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {

                            //Log.d(Constants.TAG, " response: " + response);
                            try {
                                String jsonString = response.toString();
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray JArray = jsonObject.getJSONArray("content");
                                int i1 = 0;
                                int arrayLength = JArray.length();

                                if (arrayLength > 0){
                                    Toast.makeText(mContext,"This Order have already added in their credit account", Toast.LENGTH_LONG).show();
                                    Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid,customerMobileNo,vendorkey);

                                }
                                else{



                                    GetDatafromCreditOrderDetailsTable(orderid,getDate_and_time(),customerMobileNo);

                                }
                            } catch (Exception e) {

                                Toast.makeText(mContext,"Can't get Credit Order Transaction Details", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Toast.makeText(mContext,"Can't get Credit Order Transaction Details", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


                Toast.makeText(mContext,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);




    }

    private void updateChangesinLocalArray(String orderid, String changing_Value, String havetoChange_variable) {
        try {
            if(Edit_Or_CancelTheOrders.showcreditorderscheckbox.isChecked()){
                if (Edit_Or_CancelTheOrders.isSearchButtonClicked) {
                    for (int i = 0; i < Edit_Or_CancelTheOrders.sorted_CreditedOrdersList.size(); i++) {
                        final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.sorted_CreditedOrdersList.get(i);
                        String orderkey = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                        if (orderkey.equals(orderid)) {
                            if (havetoChange_variable.equals("PaymentMode")) {
                                modal_manageOrders_forOrderDetailList1.setPaymentmode(changing_Value);
                                paymentTypetext_widget.setText(changing_Value);
                            }
                            if (havetoChange_variable.equals("OrderStatus")) {
                                modal_manageOrders_forOrderDetailList1.setOrderstatus(changing_Value);
                                orderStatustext_widget.setText(changing_Value);
                            }
                            if (havetoChange_variable.equals("vendorkey")) {
                                modal_manageOrders_forOrderDetailList1.setVendorkey(changing_Value);
                            }
                        }
                    }
                }

                for (int i = 0; i < Edit_Or_CancelTheOrders.CreditedordersList.size(); i++) {
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.CreditedordersList.get(i);
                    String orderkey = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                    if (orderkey.equals(orderid)) {
                        if (havetoChange_variable.equals("PaymentMode")) {
                            modal_manageOrders_forOrderDetailList1.setPaymentmode(changing_Value);
                            paymentTypetext_widget.setText(changing_Value);
                        }
                        if (havetoChange_variable.equals("OrderStatus")) {
                            modal_manageOrders_forOrderDetailList1.setOrderstatus(changing_Value);
                            orderStatustext_widget.setText(changing_Value);
                        }
                        if (havetoChange_variable.equals("vendorkey")) {
                            modal_manageOrders_forOrderDetailList1.setVendorkey(changing_Value);
                        }
                    }
                }
            }
            else {
                if (Edit_Or_CancelTheOrders.isSearchButtonClicked) {
                    for (int i = 0; i < Edit_Or_CancelTheOrders.sorted_OrdersList.size(); i++) {
                        final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.sorted_OrdersList.get(i);
                        String orderkey = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                        if (orderkey.equals(orderid)) {
                            if (havetoChange_variable.equals("PaymentMode")) {
                                modal_manageOrders_forOrderDetailList1.setPaymentmode(changing_Value);
                                paymentTypetext_widget.setText(changing_Value);
                            }
                            if (havetoChange_variable.equals("OrderStatus")) {
                                modal_manageOrders_forOrderDetailList1.setOrderstatus(changing_Value);
                                orderStatustext_widget.setText(changing_Value);
                            }
                            if (havetoChange_variable.equals("vendorkey")) {
                                modal_manageOrders_forOrderDetailList1.setVendorkey(changing_Value);
                            }
                        }
                    }
                }

                for (int i = 0; i < Edit_Or_CancelTheOrders.ordersList.size(); i++) {
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.ordersList.get(i);
                    String orderkey = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                    if (orderkey.equals(orderid)) {
                        if (havetoChange_variable.equals("PaymentMode")) {
                            modal_manageOrders_forOrderDetailList1.setPaymentmode(changing_Value);
                            paymentTypetext_widget.setText(changing_Value);
                        }
                        if (havetoChange_variable.equals("OrderStatus")) {
                            modal_manageOrders_forOrderDetailList1.setOrderstatus(changing_Value);
                            orderStatustext_widget.setText(changing_Value);
                        }
                        if (havetoChange_variable.equals("vendorkey")) {
                            modal_manageOrders_forOrderDetailList1.setVendorkey(changing_Value);
                        }
                    }
                }
            }
            Adjusting_Widgets_Visibility(false);
        }
        catch (Exception e){
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);

        }

    }


    private void CalculateDistanceviaApi(TextView distancebetweencustomer_vendortext_widget) throws JSONException {
        //Log.i("Tag", "Latlangcal");


        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + customerlatitude + "," + customerLongitutde + "&destinations=" + vendorLatitude + "," + vendorLongitude + "&mode=driving&language=en-EN&sensor=false&key=AIzaSyDYkZGOckF609Cjt6mnyNX9QhTY9-kAqGY";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    //Log.d(Constants.TAG, "CalculateDistanceviaApi " + response.toString());

                    JSONArray rowsArray = (JSONArray) response.get("rows");
                    //Log.d(Constants.TAG, "rows" + rowsArray.toString());
                    JSONObject elements = rowsArray.getJSONObject(0);
                    //Log.d(Constants.TAG, "elements" + elements.toString());

                    JSONArray elementsArray = (JSONArray) elements.get("elements");
                    //Log.d(Constants.TAG, "elementsArray" + elementsArray.toString());

                    JSONObject distance = elementsArray.getJSONObject(0);
                    //Log.d(Constants.TAG, "distance" + distance.toString());
                    JSONObject jsondistance = distance.getJSONObject("distance");
                    //Log.d(Constants.TAG, "jsondistance :" + jsondistance);

                    String distanceinString = jsondistance.getString("text");
                    //Log.d(Constants.TAG, "distanceinString :" + distanceinString);
                    distancebetweencustomer_vendortext_widget.setText(distanceinString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error2: " + error.getMessage());
                //Log.d(Constants.TAG, "Error3: " + error.toString());

                error.printStackTrace();
            }
        });

        // Make the request
        Volley.newRequestQueue(Edit_Or_CancelOrder_OrderDetails_Screen.this).add(jsonObjectRequest);
    }

    private void GetDatafromCreditOrderDetailsTable( String sTime, String currenttime, String mobileno) {
        totalamountUserHaveAsCredit = 0;
        payableAmount = payableAmount.replaceAll("[^\\d.]", "");
        try{
            payableAmountDouble = Integer.parseInt(payableAmount);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            mobileno = URLEncoder.encode(mobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetCreditOrdersUsingMobilenoWithVendorkey +"?usermobileno="+mobileno+"&vendorkey="+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {

                            //Log.d(Constants.TAG, " response: " + response);
                            try {
                                String jsonString = response.toString();
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray JArray = jsonObject.getJSONArray("content");
                                int i1 = 0;
                                int arrayLength = JArray.length();

                                if (arrayLength > 0){
                                    for (; i1 < (arrayLength); i1++) {

                                        try {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            try {
                                                if (json.has("totalamountincredit")) {

                                                    totalamountUserHaveAsCredit = Double.parseDouble(json.getString("totalamountincredit"));



                                                } else {
                                                    totalamountUserHaveAsCredit = 0;
                                                    Toast.makeText(mContext, "Can't get CreditOrder Details", Toast.LENGTH_LONG).show();

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                totalamountUserHaveAsCredit = 0;
                                            }


                                        } catch (Exception e) {
                                            Toast.makeText(mContext, "Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                            totalamountUserHaveAsCredit = 0;
                                            e.printStackTrace();
                                        }
                                        double newamountUserHaveAsCredit =0;


                                        if(iscancelOrderButtonClicked){
                                            newamountUserHaveAsCredit = totalamountUserHaveAsCredit - payableAmountDouble;
                                        }
                                        else{
                                            newamountUserHaveAsCredit = totalamountUserHaveAsCredit + payableAmountDouble;
                                        }



                                        AddOrUpdateDatainCreditOrderDetailsTable(newamountUserHaveAsCredit, orderid,customerMobileNo,currenttime,"ADD",payableAmountDouble);


                                    }
                                }
                                else{
                                    totalamountUserHaveAsCredit = 0;
                                    AddOrUpdateDatainCreditOrderDetailsTable(payableAmountDouble, orderid,customerMobileNo,currenttime,"ADD",payableAmountDouble);

                                }
                            } catch (Exception e) {
                                Toast.makeText(mContext,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                totalamountUserHaveAsCredit =0;
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Toast.makeText(mContext,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                            totalamountUserHaveAsCredit =0;
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


                Toast.makeText(mContext,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();

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
         Volley.newRequestQueue(mContext).add(jsonObjectRequest);



    }




    private void AddOrUpdateDatainCreditOrderDetailsTable(double newamountUserHaveAsCredit, String orderid, String usermobileno, String orderplacedTime, String transactionType, double payableAmountDouble) {


        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("usermobileno", usermobileno);
            jsonObject.put("lastupdatedtime", orderplacedTime);
            jsonObject.put("totalamountincredit", Math.round(newamountUserHaveAsCredit));
            jsonObject.put("vendorkey", vendorKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String  apiString = "";
        if(transactionType.toUpperCase().equals("ADD")){
            apiString = Constants. api_addCreditOrderDetailsTable;
        }
        else if(transactionType.toUpperCase().equals("UPDATE")){
            apiString = Constants. api_UpdateCreditOrderDetailsTable;
        }



        ////Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiString,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        addCreditOrdersTransactionDetails(orderid,usermobileno,vendorKey,totalamountUserHaveAsCredit,payableAmountDouble,newamountUserHaveAsCredit,orderplacedTime,transactionType);

                        if(iscancelOrderButtonClicked){
                            ChangeStatusOftheOrder(Constants.CANCELLED_ORDER_STATUS,orderTrackingDetailskey,CurrentTime,orderid,customerMobileNo,vendorkey);
                        }
                        else{
                            Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid,customerMobileNo,vendorkey);
                        }






                    }
                    else{
                        if(iscancelOrderButtonClicked){
                            ChangeStatusOftheOrder(Constants.CANCELLED_ORDER_STATUS,orderTrackingDetailskey,CurrentTime,orderid,customerMobileNo,vendorkey);
                        }
                        else{
                            Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid,customerMobileNo,vendorkey);
                        }
                        Toast.makeText(mContext,"Can't add this Order to Credit Account ", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    if(iscancelOrderButtonClicked){
                        ChangeStatusOftheOrder(Constants.CANCELLED_ORDER_STATUS,orderTrackingDetailskey,CurrentTime,orderid,customerMobileNo,vendorkey);
                    }
                    else{
                        Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid,customerMobileNo,vendorkey);
                    }

                    Toast.makeText(mContext,"Can't add this Order to Credit Account ", Toast.LENGTH_LONG).show();

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                Toast.makeText(mContext,"Can't add this Order to Credit Account ", Toast.LENGTH_LONG).show();
                if(iscancelOrderButtonClicked){
                    ChangeStatusOftheOrder(Constants.CANCELLED_ORDER_STATUS,orderTrackingDetailskey,CurrentTime,orderid,customerMobileNo,vendorkey);
                }
                else{
                    Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid,customerMobileNo,vendorkey);
                }

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);



    }

    private void addCreditOrdersTransactionDetails(String orderid, String usermobileno, String vendorKey, double oldamountUserHaveAsCredit, double payableAmountDouble, double newamountUserHaveAsCredit, String orderplacedTime, String transactionType) {


        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("vendorkey", vendorKey);

            jsonObject.put("usermobileno", usermobileno);
            jsonObject.put("transactiontime", orderplacedTime);


            if(iscancelOrderButtonClicked){
                jsonObject.put("transactiontype", Constants.CREDIT_AMOUNT_CANCELLED);
            }
            else{
                jsonObject.put("transactiontype", Constants.CREDIT_AMOUNT_ADDED);
            }


            jsonObject.put("orderid", orderid);
            jsonObject.put("oldamountincredit", oldamountUserHaveAsCredit);
            jsonObject.put("transactionvalue", Math.round(payableAmountDouble));
            jsonObject.put("newamountincredit",Math.round( newamountUserHaveAsCredit));

        } catch (JSONException e) {
            e.printStackTrace();
        }



        ////Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addCreditOrdersTransactionDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");

                    if (message.equals("success")) {

                    }
                    else{

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

         Volley.newRequestQueue(mContext).add(jsonObjectRequest);








    }








    public void add_amount_ForBillDetails(List<Modal_ManageOrders_Pojo_Class> orderdItems_desp) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double new_total_amountfromArray =0, quantity=0, taxes_and_chargesfromArray=0;
        for(int i =0; i<orderdItems_desp.size();i++){
            Modal_ManageOrders_Pojo_Class getOrderAmountDetails = orderdItems_desp.get(i);
            //find total amount with out GST
            try {
                new_total_amountfromArray = Double.parseDouble(getOrderAmountDetails.getItemFinalPrice());
                //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);
            }
            catch (Exception e ){
                e.printStackTrace();
            }
            try {
                quantity =  Double.parseDouble(getOrderAmountDetails.getQuantity());

            }
            catch (Exception e){
                quantity = 1;
                e.printStackTrace();
            }

            try{
                new_total_amount = new_total_amountfromArray*quantity;
                old_total_Amount=old_total_Amount+new_total_amount;

            }catch (Exception e){
                e.printStackTrace();
            }
            //Log.i(TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
            //Log.i(TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);

            getOrderAmountDetails.setTotalAmountWithoutGst(String.valueOf(decimalFormat.format(old_total_Amount)));




            try{
                taxes_and_chargesfromArray = Double.parseDouble(getOrderAmountDetails.getGstAmount());

            }
            catch (Exception e){
                e.printStackTrace();
            }
            //find total GST amount
            //Log.i(TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);
            //taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);



            //Log.i(TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
            //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
            //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
            new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
            old_taxes_and_charges_Amount=old_taxes_and_charges_Amount+new_taxes_and_charges_Amount;
            getOrderAmountDetails.setTotalGstAmount(String.valueOf(decimalFormat.format(old_taxes_and_charges_Amount)));




            new_to_pay_Amount =  (old_total_Amount + old_taxes_and_charges_Amount);
            getOrderAmountDetails.setTotalAmountWithGst(String.valueOf(decimalFormat.format(new_to_pay_Amount)));


        }




//find total payable Amount

        total_item_Rs_text_widget.setText(decimalFormat.format(old_total_Amount));
        taxes_and_Charges_rs_text_widget.setText(decimalFormat.format(old_taxes_and_charges_Amount));
        double couponDiscount=0;
        try {
            couponDiscount = Double.parseDouble(coupondiscountAmount);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        new_to_pay_Amount = new_to_pay_Amount-couponDiscount;
        double deliveryCharges_double=0;
        try {
            deliveryCharges_double = Double.parseDouble(deliveryCharges);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        new_to_pay_Amount =new_to_pay_Amount +deliveryCharges_double;

      //  int new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);
        double new_totalAmount_withGst = 0;
        try {
            if (modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.APPORDER)) {
                new_totalAmount_withGst = Double.parseDouble(decimalFormat.format(new_to_pay_Amount));
            } else {
                new_totalAmount_withGst = Math.round(new_to_pay_Amount);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst));
        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
        if(isFromEditOrders.equals("TRUE")) {
            i = new Intent(this, Edit_Or_CancelTheOrders.class);

        }
        else if
             (isFromGenerateCustomermobile_billvaluereport.equals("TRUE")) {
                i = new Intent(this, GenerateCustomerMobileNo_BillValueReport.class);

            }
        else if
        (isFromCancelledOrders.equals("TRUE")) {
            i = new Intent(this, CancelledOrders.class);

        }
        else {
            i = new Intent(this, GenerateOrderDetailsDump.class);


        }
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);


    }




    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String FormattedTime = dfTime.format(c);
        String formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
    }
    private void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            //bottomNavigationView.setVisibility(View.VISIBLE);

        }}






    //Doing the same with this method as we did with getName()
    private String getVendorData(int position,String fieldName){
        String data="";
        try {
            Modal_vendor  vendor = vendorList.get(position);
            data = vendor.getGet(fieldName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, toastFromOrderItemDetails, Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            FetchOrdersFromOrderItemDetailsDatabase(orderid);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, toastFromOrderItemDetails, Toast.LENGTH_SHORT).show();
            //this method will be running on UI thread

        }

    }



}