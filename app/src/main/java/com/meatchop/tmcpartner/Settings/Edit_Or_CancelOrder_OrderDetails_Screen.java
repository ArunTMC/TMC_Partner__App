package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
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
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_AssignDeliveryPartner1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_orderDetails_itemDesp_listview1;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

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
    public String vendorKey;
    public String vendorUserMobileno;
    public String customerLongitutde;
    public String paymentmode;
    public String paymentModeString;
    public String payableAmount;
    public String userkey,UserRole,UserPhoneNumber;

    public String tokenNo;
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
    Button changePaymentMode_button,cancelOrder_button,changeDeliveryPartner;
    private  String isFromEditOrders,isFromGenerateCustomermobile_billvaluereport,isFromCancelledOrders;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    public static Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class;
    boolean isordertrackingcalled = false;
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

        changePaymentMode_button = findViewById(R.id.changePaymentMode_button);
        deliveryPartnerList = new ArrayList<>();


        try {
            SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);


            vendorLatitude = (shared.getString("VendorLatitude", "12.9406"));
            vendorLongitude = (shared.getString("VendorLongitute", "80.1496"));
            UserPhoneNumber = (shared.getString("UserPhoneNumber", "+91"));

           vendorKey = (shared.getString("VendorKey", ""));
            vendorUserMobileno = (shared.getString("UserPhoneNumber", ""));
            UserRole = shared.getString("userrole", "");

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            SharedPreferences shared2 = getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
            DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));

            ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        cancelOrder_button.setVisibility(View.GONE);

        OrderdItems_desp = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);

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


        getMenuItemArrayFromSharedPreferences();



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



        if((isFromEditOrders.equals("TRUE")) && (Edit_Or_CancelTheOrders.showcreditorderscheckbox.isChecked()) &&(!isordertrackingcalled)){
        //    modal_manageOrders_pojo_class = getOrderTrackingDetailsAlso(modal_manageOrders_pojo_class,orderid);
          //  Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "in method" , Toast.LENGTH_LONG).show();

            Adjusting_Widgets_Visibility(true);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_orderid + orderid,null,
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
                                                if (ordertype.toUpperCase().equals(Constants.APPORDER)) {


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





                                            updateDatainLocalArray(modal_manageOrders_pojo_class);

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

            Adjusting_Widgets_Visibility(false);





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
                    if(!deliverypartnerName.equals("null")) {

                        String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                        showBottomSheetDialog_deliveryPartnerList(Orderkey,deliverypartnerName);

                    }
                    else{

                        String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                        showBottomSheetDialog_deliveryPartnerList(Orderkey,"null");

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


                               ChangeStatusOftheOrder(Constants.CANCELLED_ORDER_STATUS,orderTrackingDetailskey,CurrentTime);
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



                                if(!menuitemStockAvlDetailsKey[0].equals("") && !menuitemStockAvlDetailsKey[0].equals(" - ") && !menuitemStockAvlDetailsKey[0].equals(" ") && !menuitemStockAvlDetailsKey[0].equals("nil") && (!menuitemStockAvlDetailsKey[0].equals("null"))&& (menuitemStockAvlDetailsKey[0] != null)){



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
        if((!stockIncomingKey_avlDetails.equals("")) && (!stockIncomingKey_avlDetails.equals(" - ")) &&(!stockIncomingKey_avlDetails.equals("null")) && (!stockIncomingKey_avlDetails.equals(null)) && (!stockIncomingKey_avlDetails.equals("0")) && (!stockIncomingKey_avlDetails.equals(" 0 ")) && (!stockIncomingKey_avlDetails.equals("-")) && (!stockIncomingKey_avlDetails.equals("nil"))) {

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




        if(Edit_Or_CancelTheOrders.showcreditorderscheckbox.isChecked()){
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
            changePaymentMode_button.setVisibility(View.GONE);
            cancelOrder_button.setVisibility(View.GONE);
            changeDeliveryPartner.setVisibility(View.GONE);
        }
        else{
            changeDeliveryPartner.setVisibility(View.VISIBLE);
            if(ordertype.equals(Constants.APPORDER)) {
                if ((UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.STOREMANAGER_ROLENAME)) || (UserRole.equals(Constants.ADMIN_ROLENAME))) {
                    if ((UserRole.equals(Constants.CASHIER_ROLENAME)) || (UserRole.equals(Constants.STOREMANAGER_ROLENAME))) {
                        if ((UserPhoneNumber.equals("+916380050384")) ||(UserPhoneNumber.equals("+919597580128")) || (UserPhoneNumber.equals("+918939189102"))) {
                            cancelOrder_button.setVisibility(View.VISIBLE);
                        } else {
                            cancelOrder_button.setVisibility(View.GONE);

                        }
                    } else {
                        cancelOrder_button.setVisibility(View.VISIBLE);

                    }
                } else {
                    cancelOrder_button.setVisibility(View.GONE);

                }
            }
            else{
                cancelOrder_button.setVisibility(View.GONE);
                if ((UserRole.equals(Constants.ADMIN_ROLENAME)) || ((UserPhoneNumber.equals("+916380050384")))) {
                    cancelOrder_button.setVisibility(View.VISIBLE);
                }
            }

            changePaymentMode_button.setVisibility(View.VISIBLE);
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

        if (ordertype.equals(Constants.APPORDER)) {
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
        }
        else{
            mobileNotext_widget.setText("");

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
            Helper.getListViewSize(itemDesp_listview, screenInches);

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
                                            if (ordertype.toUpperCase().equals(Constants.APPORDER)) {
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
                                            if (ordertype.toUpperCase().equals(Constants.APPORDER)) {


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

    private void showBottomSheetDialog_deliveryPartnerList(String orderkey, String deliverypartnerName) {

        String fromActivityName="";
        bottomSheetDialog = new BottomSheetDialog(Edit_Or_CancelOrder_OrderDetails_Screen.this);
        bottomSheetDialog.setContentView(R.layout.mobilescreen_assigndeliverypartner_bottom_sheet_dialog);

        ListView ListView1 = bottomSheetDialog.findViewById(R.id.listview);

        Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(Edit_Or_CancelOrder_OrderDetails_Screen.this, deliveryPartnerList,orderkey,fromActivityName+"EditOrders",deliverypartnerName);

        ListView1.setAdapter(adapter_mobile_assignDeliveryPartner1);

        bottomSheetDialog.show();
    }

    private void showBottomSheetDialog(String paymentmode, String ordertype ,String orderid) {

        bottomSheetDialog = new BottomSheetDialog(Edit_Or_CancelOrder_OrderDetails_Screen.this);
        bottomSheetDialog.setContentView(R.layout.change_paymentmode_bottomsheet_dialog);
        RadioGroup pos_radioGroup = bottomSheetDialog.findViewById(R.id.posOrders_radiogrp);
        RadioGroup app_radioGroup = bottomSheetDialog.findViewById(R.id.appOrders_radiogrp);

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
        if(ordertype.equals(Constants.POSORDER)){
            app_radioGroup.setVisibility(View.GONE);
            app_radioGroup.setSelected(false);
            pos_radioGroup.setVisibility(View.VISIBLE);

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
                app_radioGroup.check(R.id.credit_pos);

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
                }
            }
        });


        changePaymentMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Adjusting_Widgets_Visibility(true);
                AddNewEntryinPaymentTransaction("","","",modal_manageOrders_pojo_class.getUsermobile().toString() ,orderid,paymentModeString,"","SUCCESS",payableAmount,getDate_and_time(),userkey);

                Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid);
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




    private void ChangeStatusOftheOrder(String changestatusto, String OrderKey, String currenttime) {
        JSONObject  jsonObject = new JSONObject();
        try {
                jsonObject.put("key", OrderKey);
                jsonObject.put("orderstatus", changestatusto);

                 jsonObject.put("ordercancelledtime", currenttime);

                Log.i("tag","listenertoken"+ "");






        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updateTrackingOrderTable,
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


    private void Change_Payment_Mode_Of_the_Order(String orderdetailsKey, String paymentModeString, String orderid) {


        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key", orderdetailsKey);
            jsonObject.put("paymentmode", paymentModeString);
            Log.i("tag","listenertoken"+ "");






        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);
            Adjusting_Widgets_Visibility(false);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updatePaymentMode_OrderDetailsTable,
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

        int new_totalAmount_withGst = (int) Math.ceil(new_to_pay_Amount);


        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst)+".00");
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

        SimpleDateFormat day = new SimpleDateFormat("EEE");
       String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
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
}