package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_AssignDeliveryPartner1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_ManageOrders_ListView1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_orderDetails_itemDesp_listview1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.GenericArrayType;
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
    public String vendorLatitude;
    public String customerlatitude;
    public String customerLongitutde;
    public String paymentmode;
    public String paymentModeString;
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
    LinearLayout showlocation,deliverypersonName_Layout,deliverypersonMobileNO_Layout,tokenNo_Layout,distanceInKm_layout,confirmedTimeLayout,
            readyTimeLayout,pickedTimeLayout,slotdateLayout,slotTimeLayout,AddressLayout;
    public static BottomSheetDialog bottomSheetDialog;
    public static LinearLayout loadingPanel;
    public static LinearLayout loadingpanelmask;
    Button changePaymentMode_button,cancelOrder_button,changeDeliveryPartner;
    private  String isFromEditOrders,isFromGenerateCustomermobile_billvaluereport,isFromCancelledOrders;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    public static Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class;
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
        try{
            isFromEditOrders = modal_manageOrders_pojo_class.getIsFromEditOrders().toString().toUpperCase();
        }
        catch(Exception e ){
            e.printStackTrace();
            isFromEditOrders = "TRUE";
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
        try {
            orderStatustext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderstatus()));

            slotNametext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotdate()));
            slotDatetext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotname()));

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
            orderid = String.valueOf((modal_manageOrders_pojo_class.getOrderid()));
            orderIdtext_widget.setText(String.valueOf(orderid));

        }
        catch (Exception e){
            e.printStackTrace();
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
            if (deliverypartnerName.equals(null)) {
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

            changePaymentMode_button.setVisibility(View.VISIBLE);
            cancelOrder_button.setVisibility(View.VISIBLE);
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
        try {
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






            try{


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
        catch (Exception e){
            e.printStackTrace();
        }

        cancelOrder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentTime = getDate_and_time();
                new TMCAlertDialogClass(Edit_Or_CancelOrder_OrderDetails_Screen.this, R.string.app_name, R.string.OrderCancellingInstruction,
                        R.string.Yes_Text,R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
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

                }
            }
        });


        changePaymentMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                Adjusting_Widgets_Visibility(true);

                Change_Payment_Mode_Of_the_Order(orderdetailsKey,paymentModeString,orderid);
            }
        });



        bottomSheetDialog.show();
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
                    msg = String.valueOf(response.get("message"));
                    if(msg.equals("success")) {

                        Log.d(Constants.TAG, "Responsewwwww: " + response);

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
           if(Edit_Or_CancelTheOrders.isSearchButtonClicked){
               for (int i = 0; i < Edit_Or_CancelTheOrders.sorted_OrdersList.size(); i++) {
                   final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.sorted_OrdersList.get(i);
                   String orderkey = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                   if (orderkey.equals(orderid)) {
                       if(havetoChange_variable.equals("PaymentMode")) {
                           modal_manageOrders_forOrderDetailList1.setPaymentmode(changing_Value);
                           paymentTypetext_widget.setText(changing_Value);
                       }
                       if(havetoChange_variable.equals("OrderStatus")) {
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
                    if(havetoChange_variable.equals("PaymentMode")) {
                        modal_manageOrders_forOrderDetailList1.setPaymentmode(changing_Value);
                        paymentTypetext_widget.setText(changing_Value);
                    }
                    if(havetoChange_variable.equals("OrderStatus")) {
                        modal_manageOrders_forOrderDetailList1.setOrderstatus(changing_Value);
                        orderStatustext_widget.setText(changing_Value);
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