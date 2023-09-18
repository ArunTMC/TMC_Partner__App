package com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTableInterface;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.FetchAddressIntentService;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.DeliveryPartnerSettlementReport;
import com.meatchop.tmcpartner.settings.GetDeliverypartnersAssignedOrders;
import com.meatchop.tmcpartner.settings.Helper;
import com.meatchop.tmcpartner.settings.Pos_Orders_List;
import com.meatchop.tmcpartner.settings.ScreenSizeOfTheDevice;
import com.meatchop.tmcpartner.settings.WholeSaleOrdersList;
import com.meatchop.tmcpartner.settings.searchOrdersUsingMobileNumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Pos_OrderDetailsScreen extends AppCompatActivity {
TextView mobileNotext_widget,ordertypetext_widget,orderplacedtime_textwidget,orderConfirmedtime_textwidget,orderReaytime_textwidget,orderpickeduptime_textwidget,orderDeliveredtime_textwidget,orderIdtext_widget,orderStatustext_widget,paymentTypetext_widget,slotNametext_widget,slotDatetext_widget
        ,googleAddress_textwidget,deliveryPartner_name_widget,deliveryPartner_mobileNo_widget,delivery_type_widget,slotTime_Range_textwidget;
TextView deliveryCharges_text_widget,notes_textwidget,distancebetweencustomer_vendortext_widget,discounttext_widget,addresstype_textwidget,AddressLine2_textwidget,landmark_textwidget,AddressLine1_textwidget,total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;
Button changeDeliveryPartner;
    Adapter_forOrderDetails_Listview adapter_forOrderDetails_listview;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp;
    double screenInches;

    ListView itemDesp_listview;
    LinearLayout deliveryPartnerAssignLayout,refresh_googleAddress_loadinganim_layout,refresh_googleAddress_image_layout,refresh_googleAddress_layout,refreshpaymentmode__loadinganim_layout,refreshpaymentmode_image_layout,refresh_paymentmode_layout;
    static LinearLayout loadingPanel;
    static LinearLayout loadingpanelmask;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
    String orderdetailskey="",ordertype,coupondiscountAmount,deliveryCharges,useraddreskey,vendorkey,vendorLongitude,vendorLatitude,customerlatitude,customerLongitutde,deliverydistance,fromActivityName="";
    ScrollView parentScrollView;
    ResultReceiver resultReceiver;
    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class;

    boolean orderdetailsnewschema = false;
    String orderid ="",customerMobileNo ="";
    Update_CustomerOrderDetails_TrackingTableInterface mResultCallback_UpdateCustomerOrderDetailsTableInterface = null;



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos__order_details_screen_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        orderIdtext_widget = findViewById(R.id.orderIdtext_widget);
        orderStatustext_widget = findViewById(R.id.orderStatustext_widget);
        itemDesp_listview = findViewById(R.id.itemDesp_listview);
        paymentTypetext_widget = findViewById(R.id.paymentTypetext_widget);
        slotNametext_widget = findViewById(R.id.slotNametext_widget);
        slotDatetext_widget = findViewById(R.id.slotDatetext_widget);
        deliveryPartner_name_widget = findViewById(R.id.deliveryPartner_name_widget);
        deliveryPartner_mobileNo_widget = findViewById(R.id.deliveryPartner_mobileNo_widget);
        delivery_type_widget = findViewById(R.id.delivery_type_widget);
        total_item_Rs_text_widget = findViewById(R.id.total_amount_text_widget);
        total_Rs_to_Pay_text_widget = findViewById(R.id.total_Rs_to_Pay_text_widget);
        taxes_and_Charges_rs_text_widget = findViewById(R.id.taxes_and_Charges_rs_text_widget);
        changeDeliveryPartner = findViewById(R.id.changeDeliveryPartner);
        slotTime_Range_textwidget = findViewById(R.id.slotTime_Range_textwidget);
        AddressLine1_textwidget = findViewById(R.id.AddressLine1_textwidget);
        AddressLine2_textwidget = findViewById(R.id.AddressLine2_textwidget);
        landmark_textwidget = findViewById(R.id.landmark_textwidget);
        addresstype_textwidget=findViewById(R.id.addresstype_textwidget);
        orderplacedtime_textwidget = findViewById(R.id.orderplacedtime_textwidget);
        orderConfirmedtime_textwidget = findViewById(R.id.orderConfirmedtime_textwidget);
        orderReaytime_textwidget = findViewById(R.id.orderReaytime_textwidget);
        orderpickeduptime_textwidget = findViewById(R.id.orderpickeduptime_textwidget);
        orderDeliveredtime_textwidget = findViewById(R.id.orderDeliveredtime_textwidget);
        discounttext_widget = findViewById(R.id.discounttext_widget);
        parentScrollView= findViewById(R.id.parentScrollView);
        ordertypetext_widget = findViewById(R.id.ordertypetext_widget);
        distancebetweencustomer_vendortext_widget = findViewById(R.id.distancebetweencustomer_vendortext_widget);
        mobileNotext_widget = findViewById(R.id.mobileNotext_widget);
        notes_textwidget = findViewById(R.id.notes_textwidget);
        deliveryPartnerAssignLayout = findViewById(R.id.deliveryPartnerAssignLayout);
        googleAddress_textwidget = findViewById(R.id.googleAddress_textwidget);
        deliveryCharges_text_widget = findViewById(R.id.deliveryCharges_text_widget);
        refresh_paymentmode_layout = findViewById(R.id.refresh_paymentmode_layout);
        refresh_paymentmode_layout = findViewById(R.id.refresh_paymentmode_layout);
        refreshpaymentmode_image_layout = findViewById(R.id.refreshpaymentmode_image_layout);
        refreshpaymentmode__loadinganim_layout = findViewById(R.id.refreshpaymentmode__loadinganim_layout);
        refresh_googleAddress_loadinganim_layout = findViewById(R.id.refresh_googleAddress_loadinganim_layout);
        refresh_googleAddress_image_layout = findViewById(R.id.refresh_googleAddress_image_layout);
        refresh_googleAddress_layout = findViewById(R.id.refresh_googleAddress_layout);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);



        Bundle bundle = getIntent().getExtras();
        modal_manageOrders_pojo_class = bundle.getParcelable("data");
        fromActivityName = bundle.getString("From");


        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));

        vendorLatitude = (shared.getString("VendorLatitude", ""));
        vendorLongitude = (shared.getString("VendorLongitute", ""));
        if (fromActivityName.equals("PosManageOrders")) {
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));

        }
        else{
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));

        }        //orderdetailsnewschema = true;


        OrderdItems_desp = new ArrayList<>();

        resultReceiver = new AddressResultReceiver(new Handler());

        try{
            customerMobileNo = String.valueOf(modal_manageOrders_pojo_class.getUsermobile());
        }
        catch (Exception e){
            customerMobileNo ="";
            e.printStackTrace();
        }

        try{
            orderid  = String.valueOf(modal_manageOrders_pojo_class.getOrderid());
        }
        catch (Exception e){
            orderid ="";
            e.printStackTrace();
        }


        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(Pos_OrderDetailsScreen.this);
            //    Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
                // Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }




        ordertype = String.valueOf(modal_manageOrders_pojo_class.getOrderType());
            try {
                if(ordertype.equals(Constants.APPORDER) || ordertype.equals(Constants.PhoneOrder)) {
                    try {
                        deliverydistance = String.valueOf(modal_manageOrders_pojo_class.getDeliverydistance());
                        distancebetweencustomer_vendortext_widget.setText(deliverydistance+" Km");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    customerlatitude = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslat());
                    customerLongitutde = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslon());
                    if(((customerLongitutde.equals("null"))||(customerLongitutde.equals(null))||(customerLongitutde.equals(""))||(customerLongitutde.equals("0")))&&(((customerlatitude.equals("null"))||(customerlatitude.equals(null))||(customerlatitude.equals(""))||(customerlatitude.equals("0"))))){

                        if (modal_manageOrders_pojo_class.getUseraddresskey() != null) {
                            getUserAddressAndLat_LongFromAddressTable(modal_manageOrders_pojo_class.getUseraddresskey().toString());
                        } else {
                            Toast.makeText(Pos_OrderDetailsScreen.this, "Userkey cnanot be found", Toast.LENGTH_LONG).show();

                        }
                    }
                    else{

                        getGoogleAddressUsingMapApi(customerlatitude,customerLongitutde);
                    }


                }

        orderIdtext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderid()));
        orderStatustext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderstatus()));
        paymentTypetext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getPaymentmode()));
        slotDatetext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotdate()));
        slotNametext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotname()));
        delivery_type_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getDeliverytype()));
        slotTime_Range_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlottimerange()));
        mobileNotext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getUsermobile()));





                try{
                    String  paymentmode  = (String.valueOf(modal_manageOrders_pojo_class.getPaymentmode()).toUpperCase());
                    try {

                        if(ordertype.equals(Constants.APPORDER) ) {

                            if ((paymentmode.equals(Constants.CASH_ON_DELIVERY)) || paymentmode.equals("cash")) {
                                showProgressBar(true);

                                String orderstatus = String.valueOf(modal_manageOrders_pojo_class.getOrderstatus()).toUpperCase();
                                String time =  "";
                                if((orderstatus.equals(Constants.NEW_ORDER_STATUS))){
                                    time = String.valueOf(modal_manageOrders_pojo_class.getOrderplacedtime());


                                    CheckPaymentModeAccordingtoTime(time,24);
                                }
                                else if ((orderstatus.equals(Constants.CONFIRMED_ORDER_STATUS))){
                                    time = String.valueOf(modal_manageOrders_pojo_class.getOrderconfirmedtime());
                                    CheckPaymentModeAccordingtoTime(time,24);
                                }
                                else{
                                    time = String.valueOf(modal_manageOrders_pojo_class.getOrderreadytime());
                                    CheckPaymentModeAccordingtoTime(time,16);
                                }

                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }




                try{
            if(modal_manageOrders_pojo_class.getNotes()!=null){
                notes_textwidget.setText(modal_manageOrders_pojo_class.getNotes().toUpperCase());
            }else{
                notes_textwidget.setText("");
            }
        }
        catch(Exception e){
            e.printStackTrace();
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
        if(modal_manageOrders_pojo_class.getCoupondiscamount()!=null){
            coupondiscountAmount = String.valueOf(modal_manageOrders_pojo_class.getCoupondiscamount());
            discounttext_widget.setText(coupondiscountAmount);

        }
        else {
            coupondiscountAmount = "0.00";
            discounttext_widget.setText(coupondiscountAmount);

        }
        if(modal_manageOrders_pojo_class.getDeliveryamount()!=null){
            deliveryCharges = String.valueOf(modal_manageOrders_pojo_class.getDeliveryamount());
            deliveryCharges_text_widget.setText(deliveryCharges+".00");

        }
        else {
            deliveryCharges = "0.00";
            deliveryCharges_text_widget.setText(deliveryCharges);

        }
        if(modal_manageOrders_pojo_class.getOrderdeliveredtime()!=null){
            orderDeliveredtime_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderdeliveredtime()));
        }
        else{
            orderDeliveredtime_textwidget.setText("");
        }


            if(modal_manageOrders_pojo_class.getUseraddress()!=null){
                AddressLine1_textwidget.setText(String.valueOf(modal_manageOrders_pojo_class.getUseraddress()));
            }
            else {
                AddressLine1_textwidget.setText(String.valueOf(""));
            }

            if(!order_status.equals(Constants.NEW_ORDER_STATUS)&&(!order_type.equals("POSORDER"))) {

                String mobileno =String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerMobileNo());
                String name =String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerName());

                if(!mobileno.equals("null")){
                    deliveryPartner_mobileNo_widget.setText(mobileno);
                }
                else{
                    deliveryPartner_mobileNo_widget.setText("Not Assigned");

                }
                if(!name.equals("null")){
                    deliveryPartner_name_widget.setText(name);

                }
                else{
                    deliveryPartner_name_widget.setText("Not Assigned");

                }
                changeDeliveryPartner.setText("Change Delivery Person");



            delivery_type_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getDeliverytype()));

        }
        else{
                deliveryPartnerAssignLayout.setVisibility(View.INVISIBLE);
            deliveryPartner_mobileNo_widget.setVisibility(View.GONE);
            deliveryPartner_name_widget.setVisibility(View.GONE);
            changeDeliveryPartner.setVisibility(View.GONE);
            delivery_type_widget.setVisibility(View.GONE);

        }
        if(order_status.equals(Constants.DELIVERED_ORDER_STATUS)){

                    changeDeliveryPartner.setVisibility(View.GONE);

        }


                parentScrollView.fullScroll(View.FOCUS_UP);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        refresh_googleAddress_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_googleAddress_loadinganim_layout.setVisibility(View.VISIBLE);
                refresh_googleAddress_image_layout.setVisibility(View.GONE);
                customerlatitude = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslat());
                customerLongitutde = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslon());
                if(((customerLongitutde.equals("null"))||(customerLongitutde.equals(null))||(customerLongitutde.equals(""))||(customerLongitutde.equals("0")))&&(((customerlatitude.equals("null"))||(customerlatitude.equals(null))||(customerlatitude.equals(""))||(customerlatitude.equals("0"))))){

                    if (modal_manageOrders_pojo_class.getUseraddresskey() != null) {
                        getUserAddressAndLat_LongFromAddressTable(modal_manageOrders_pojo_class.getUseraddresskey().toString());
                    } else {
                        Toast.makeText(Pos_OrderDetailsScreen.this, "Userkey cnanot be found", Toast.LENGTH_LONG).show();

                    }
                }
                else{

                    getGoogleAddressUsingMapApi(customerlatitude,customerLongitutde);
                }


            }
        });

        refresh_paymentmode_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshpaymentmode__loadinganim_layout.setVisibility(View.VISIBLE);
                refreshpaymentmode_image_layout.setVisibility(View.GONE);
                String orderidtoFetchPaymentmode = String.valueOf(modal_manageOrders_pojo_class.getOrderid());
                orderdetailskey = String.valueOf(modal_manageOrders_pojo_class.getOrderdetailskey());
                showProgressBar(true);

                getPaymentModeFromOrderDetails(orderidtoFetchPaymentmode,orderdetailskey,orderid,vendorkey,customerMobileNo);
            }
        });

        changeDeliveryPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderid = "";
                try{
                    orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                String customerMobileNo = "";

                try{
                    customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                String vendorkey = "";

                try{
                    vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }



              //  Toast.makeText(Pos_OrderDetailsScreen.this, "1  -" +fromActivityName, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Pos_OrderDetailsScreen.this,AssigningDeliveryPartner.class);
                intent.putExtra("TrackingTableKey",modal_manageOrders_pojo_class.getKeyfromtrackingDetails());
                intent.putExtra("orderid",modal_manageOrders_pojo_class.getOrderid());
                intent.putExtra("customerMobileNo",modal_manageOrders_pojo_class.getUsermobile());
                intent.putExtra("vendorkey",modal_manageOrders_pojo_class.getVendorkey());
                intent.putExtra("From",fromActivityName);
                //Toast.makeText(Pos_OrderDetailsScreen.this, "2  -" +fromActivityName, Toast.LENGTH_SHORT).show();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(intent);
            }
        });



        String itemDespString = modal_manageOrders_pojo_class.getItemdesp_string();
        try {
            String subCtgyKey ="";
            JSONArray jsonArray = new JSONArray(itemDespString);
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if(json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                    Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    marinades_manageOrders_pojo_class.setOrderType(modal_manageOrders_pojo_class.getOrderType());
                    try {
                        if(marinadesObject.has("tmcsubctgykey")) {
                            subCtgyKey = String.valueOf(marinadesObject.get("tmcsubctgykey"));
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
                            marinades_manageOrders_pojo_class.itemName="Ready to Cook  "+marinadesObject.getString("itemname");

                        }
                        else{
                            marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname");

                        }






                    marinades_manageOrders_pojo_class.ItemFinalPrice= marinadesObject.getString("tmcprice");
                    marinades_manageOrders_pojo_class.quantity =String.valueOf(json.get("quantity"));
                    marinades_manageOrders_pojo_class.GstAmount = marinadesObject.getString("gstamount");
                    if (json.has("netweight")) {
                        marinades_manageOrders_pojo_class.ItemFinalWeight = marinadesObject.getString("netweight");

                    }
                    else{
                        marinades_manageOrders_pojo_class.ItemFinalWeight = "";

                    }

                    OrderdItems_desp.add(marinades_manageOrders_pojo_class);

                }
                Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                manageOrders_pojo_class.setOrderType(modal_manageOrders_pojo_class.getOrderType());
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


                String cutname ="";
                try {
                    if(json.has("cutname")) {
                        cutname = String.valueOf(json.get("cutname"));
                    }
                    else {
                        cutname = "";
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    manageOrders_pojo_class.cutname= String.valueOf(cutname);

                }
                catch (Exception E){
                    E.printStackTrace();
                }





                if(subCtgyKey.equals("tmcsubctgy_16")){
                    //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                   // marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname")+(" ( Grill House ) ");
                    manageOrders_pojo_class.itemName = "Grill House "+String.valueOf(json.get("itemname"));
                }
                else  if(subCtgyKey.equals("tmcsubctgy_15")){
                    // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                 //  marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname")+(" ( Ready to Cook ) ");
                    manageOrders_pojo_class.itemName = "Ready to Cook "+String.valueOf(json.get("itemname"));

                }
                else{
                    manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));

                }
                manageOrders_pojo_class.ItemFinalPrice= String.valueOf(json.get("tmcprice"));
                manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));
                OrderdItems_desp.add(manageOrders_pojo_class);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }






        add_amount_ForBillDetails(OrderdItems_desp);
        adapter_forOrderDetails_listview = new Adapter_forOrderDetails_Listview(Pos_OrderDetailsScreen.this, OrderdItems_desp);
        itemDesp_listview.setAdapter(adapter_forOrderDetails_listview);
        Helper.getListViewSize(itemDesp_listview, screenInches,0);

    }


    public  boolean CheckPaymentModeAccordingtoTime(String time,int time_toCalculate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Log.d(Constants.TAG, "log 1 orderPlacedTime : " + time);


        calendar.add(Calendar.HOUR, time_toCalculate);



        Date c1 = calendar.getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("EEE",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String predictedday = df1.format(c1);



        SimpleDateFormat df2 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df2.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  predicteddate = df2.format(c1);
        String predicteddateandday = predictedday+", "+predicteddate;


        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        df3.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  predictedtime = df3.format(c1);
        String predicteddateanddayandtime = predictedday+", "+predicteddate+" "+predictedtime;

        Log.d(Constants.TAG, "log 1 predicteddateanddayandtime : " + predicteddateanddayandtime);

        long predictedLongForDate = Long.parseLong(getLongValuefortheDate(predicteddateanddayandtime));
        String  currentTime = getDate_and_time();
        Log.d(Constants.TAG, "log 1 currentTime : " +currentTime);

        long currentTimeLong = Long.parseLong(getLongValuefortheDate(currentTime));
        if(currentTimeLong<=predictedLongForDate){//current time is lesser or equals order placed time +  hours
            Log.d(Constants.TAG, "log 1 currentTimeLong : " +currentTimeLong);
            Log.d(Constants.TAG, "log 1 predictedLongForDate : " +predictedLongForDate);
            String orderidtoFetchPaymentmode = (String.valueOf(modal_manageOrders_pojo_class.getOrderid()));

            showProgressBar(true);

            orderdetailskey = String.valueOf(modal_manageOrders_pojo_class.getOrderdetailskey());

            getPaymentModeFromOrderDetails(orderidtoFetchPaymentmode, orderdetailskey, orderid, vendorkey, customerMobileNo);
            return true;

        }
        else{
            showProgressBar(false);
            Toast.makeText(Pos_OrderDetailsScreen.this, "Cash On Delivery", Toast.LENGTH_LONG).show();

            Log.d(Constants.TAG, "log currentTimeLong : " +currentTimeLong);
            Log.d(Constants.TAG, "log predictedLongForDate : " +predictedLongForDate);
            return false;
        }

    }


    public String getLongValuefortheDate(String orderplacedtime) {
        String longvalue = "";
        try {
            String time1 = orderplacedtime;
            //   Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            Date date = sdf.parse(time1);
            long time1long = date.getTime() / 1000;
            longvalue = String.valueOf(time1long);
          /*  String time2 = "Sat, 24 Apr 2021 07:50:28";
            Date date2 = sdf.parse(time2);

            long time2long =  date2.getTime() / 1000;
            Log.d(TAG, "time1 "+time1long + " time2 "+time2long);

           */
            //   long differencetime = time2long - time1long;
            //  Log.d(TAG, "   "+orderplacedtime);

            //   Log.d(TAG, "time1long  "+time1long);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                String time1 = orderplacedtime;
                //     Log.d(TAG, "time1long  "+orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                Date date = sdf.parse(time1);
                long time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);

                //   long differencetime = time2long - time1long;
                //  Log.d(TAG, "   "+orderplacedtime);

                //    Log.d(TAG, "time1long  "+time1long);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return longvalue;
    }


    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDatee = df.format(c);
        String CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String FormattedTime = dfTime.format(c);
        String  formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }










    private void getPaymentModeFromOrderDetails(String orderidtoFetchPaymentmode, String orderdetailskey, String orderid, String vendorkey, String customerMobileNo) {
        String Api_toCallOrderDetailsUsingOrderid = "";
        if(orderdetailsnewschema){
            if(orderid.length()>1 && vendorkey.length()>1 && customerMobileNo.length()>1){
                Api_toCallOrderDetailsUsingOrderid = Constants.api_GetVendorOrderDetailsUsingOrderid_vendorkey+ "?vendorkey="+vendorkey+"&orderid="+orderid;
            }
            else{
                Toast.makeText(Pos_OrderDetailsScreen.this, "orderid :"+orderid+" , vendorkey: "+vendorkey+" , customerMobileNo : "+ customerMobileNo, Toast.LENGTH_SHORT).show();
            }


        }
        else{
            Api_toCallOrderDetailsUsingOrderid = Constants.api_GetOrderDetailsusingOrderid+orderidtoFetchPaymentmode;

        }


        if(!Api_toCallOrderDetailsUsingOrderid.equals("")) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_toCallOrderDetailsUsingOrderid ,null,
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
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                if(arrayLength>1){
                                    Toast.makeText(Pos_OrderDetailsScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();

                                }

                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        String PaymentMode = json.getString("paymentmode");
                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);

                                        refreshpaymentmode__loadinganim_layout.setVisibility(View.GONE);
                                        refreshpaymentmode_image_layout.setVisibility(View.VISIBLE);
                                        paymentTypetext_widget.setText(PaymentMode);


                                    //    Toast.makeText(Pos_OrderDetailsScreen.this, " : "+fromActivityName, Toast.LENGTH_LONG).show();

                                        if(fromActivityName.equals("PosManageOrders")) {
                                            if(Pos_ManageOrderFragment.sorted_OrdersList.size()>0){
                                                for(int i =0; i<Pos_ManageOrderFragment.sorted_OrdersList.size();i++){
                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =Pos_ManageOrderFragment.sorted_OrdersList.get(i);
                                                    String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                                    if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                                        Pos_ManageOrderFragment.manageOrdersListViewAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            if(Pos_ManageOrderFragment.ordersList.size()>0){
                                                for(int i =0; i<Pos_ManageOrderFragment.ordersList.size();i++){
                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =Pos_ManageOrderFragment.ordersList.get(i);
                                                    String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                                    if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                                        Pos_ManageOrderFragment.manageOrdersListViewAdapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                        }


                                        if(fromActivityName.equals("WholeSaleOrdersList")) {
                                            if(WholeSaleOrdersList.sorted_OrdersList.size()>0){
                                                for(int i =0; i<WholeSaleOrdersList.sorted_OrdersList.size();i++){
                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =WholeSaleOrdersList.sorted_OrdersList.get(i);
                                                    String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                                    if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                                        WholeSaleOrdersList.adapter_pos_wholeSaleOrderList.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            if(WholeSaleOrdersList.ordersList.size()>0){
                                                for(int i =0; i<WholeSaleOrdersList.ordersList.size();i++){
                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =WholeSaleOrdersList.ordersList.get(i);
                                                    String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                                    if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                                        WholeSaleOrdersList.adapter_pos_wholeSaleOrderList.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                        }



                                        if(fromActivityName.equals("MobileGetDeliveryPartnerAssignedOrder")) {
                                            try {
                                                if (GetDeliverypartnersAssignedOrders.ordersList.size() > 0) {
                                                    for (int i = 0; i < GetDeliverypartnersAssignedOrders.ordersList.size(); i++) {
                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = GetDeliverypartnersAssignedOrders.ordersList.get(i);

                                                        String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                                        if (Orderid_fromArray.equals(orderidtoFetchPaymentmode)) {
                                                            modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);


                                                            try {
                                                                GetDeliverypartnersAssignedOrders.adapter_mobile_getDeliveryPartnersAssignedOrders.notifyDataSetChanged();

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }



                                                        }
                                                    }
                                                }
                                            }
                                            catch(Exception e){
                                                e.printStackTrace();
                                            }

                                            try{

                                                if(DeliveryPartnerSettlementReport.result_JArray.length()>0){

                                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                                    int i11 = 0;
                                                    int arrayLength1 = DeliveryPartnerSettlementReport.result_JArray.length();
                                                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                                    Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                                                    if(arrayLength1>0) {

                                                        for (; i11 < (arrayLength1); i11++) {

                                                            try {
                                                                JSONObject jsonv = DeliveryPartnerSettlementReport.result_JArray.getJSONObject(i11);
                                                                String orderidd= jsonv.getString("orderid");

                                                                if(orderidd.equals(orderidtoFetchPaymentmode)) {
                                                                    jsonv.put("paymentmode", PaymentMode);


                                                                }
                                                            }
                                                            catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }


                                        }



                                        if(fromActivityName.equals("AppOrdersList") || fromActivityName.equals("AppSearchOrders")) {

                                            if(searchOrdersUsingMobileNumber.sorted_OrdersList.size()>0){
                                                for(int i =0; i<searchOrdersUsingMobileNumber.sorted_OrdersList.size();i++){

                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =searchOrdersUsingMobileNumber.sorted_OrdersList.get(i);
                                                    String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                                    if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);




                                                        try {
                                                            searchOrdersUsingMobileNumber.adapter_mobileSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();

                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }



                                                    }
                                                }
                                            }


                                            if(searchOrdersUsingMobileNumber.ordersList.size()>0){
                                                for(int i =0; i<searchOrdersUsingMobileNumber.ordersList.size();i++){
                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =searchOrdersUsingMobileNumber.ordersList.get(i);

                                                    String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                                    if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);


                                                        try {
                                                            searchOrdersUsingMobileNumber.adapter_mobileSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();

                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }                                                        }
                                                }
                                            }


                                        }
                                        else{


                                          //  Toast.makeText(Pos_OrderDetailsScreen.this, "T   "+fromActivityName, Toast.LENGTH_LONG).show();

                                        }



                                        try {
                                            if ((PaymentMode.equals(Constants.CASH_ON_DELIVERY)) || (PaymentMode.equals("CASH"))) {
                                                getMerchantOrderidDetailsFromPaymentTransactionTable(orderidtoFetchPaymentmode, orderdetailskey);
                                            } else {
                                                showProgressBar(false);

                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            showProgressBar(false);

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                refreshpaymentmode__loadinganim_layout.setVisibility(View.GONE);
                                refreshpaymentmode_image_layout.setVisibility(View.VISIBLE);

                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();
                            refreshpaymentmode__loadinganim_layout.setVisibility(View.GONE);
                            refreshpaymentmode_image_layout.setVisibility(View.VISIBLE);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Pos_OrderDetailsScreen.this, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();



                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


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
                params.put("vendorkey", vendorkey);
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
        Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);



        }
        else{
            Toast.makeText(this, "There is no Api for OrderDetails", Toast.LENGTH_SHORT).show();
        }



    }




    private void getUserAddressAndLat_LongFromAddressTable(String useraddresskeyy) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetAddressUsingAddressKey + useraddresskeyy,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                            try {

                                JSONObject result  = response.getJSONObject("content");
                                JSONObject result2  = result.getJSONObject("Item");
                                customerLongitutde = result2.getString("locationlong");

                                customerlatitude = result2.getString("locationlat");
                                modal_manageOrders_pojo_class.setUseraddresslat(customerlatitude);
                                modal_manageOrders_pojo_class.setUseraddresslon(customerLongitutde);
                                if ((deliverydistance.equals("0") ||(deliverydistance.equals("")) && (deliverydistance.equals("null")))) {
                                    try {
                                        deliverydistance = result2.getString("deliverydistance");
                                        distancebetweencustomer_vendortext_widget.setText(deliverydistance+" Km");
                                        if ((deliverydistance.equals("0") || (deliverydistance.equals("")) && (deliverydistance.equals("null")))) {
                                            try {
                                                CalculateDistanceviaApi(distancebetweencustomer_vendortext_widget);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                getGoogleAddressUsingMapApi(customerlatitude,customerLongitutde);
                            } catch (JSONException e) {
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
                    Toast.makeText(Pos_OrderDetailsScreen.this, "Location cnanot be found", Toast.LENGTH_LONG).show();





                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


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
                params.put("vendorkey", vendorkey);
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
        Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);







    }





    private void getGoogleAddressUsingMapApi(String customerlatitude, String customerLongitutde) {
        double Latitude = Double.parseDouble(customerlatitude);
        double Longitude = Double.parseDouble(customerLongitutde);

        Location location = new Location("providerNA");
        location.setLatitude(Latitude);
        location.setLongitude(Longitude);


        Log.i("tagCustomer", "Location  " + location);

        Intent intent = new Intent(this, FetchAddressIntentService.class);

        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);









    }






    private void CalculateDistanceviaApi(TextView distancebetweencustomer_vendortext_widget) throws JSONException {
        //Log.i("Tag", "Latlangcal");


            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + customerlatitude + "," + customerLongitutde + "&destinations=" + vendorLatitude + "," + vendorLongitude + "&mode=driving&language=en-EN&sensor=false&key=AIzaSyDYkZGOckF609Cjt6mnyNX9QhTY9-kAqGY";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    try {

                        //Log.d(Constants.TAG, "response " + response.toString());

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
            Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);
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

        //int new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);
        double new_totalAmount_withGst = 0;
        try{
            if(modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.APPORDER)){
                new_totalAmount_withGst = Double.parseDouble(decimalFormat.format(new_to_pay_Amount));
            }
            else{
                new_totalAmount_withGst = (Math.round(new_to_pay_Amount));
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

    private void showProgressBar(boolean show) {
        if(show){
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
        }
    }

    private class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT) {

                Log.i("tagCustomer", "Address  " + resultData.getString(Constants.TotalLocationResult));
                Log.i("tagCustomer", "AreaName  " + resultData.getString(Constants.AreaNameOfLocation));

                //  Toast.makeText(MapActivity.this, "My Location Result :  " + resultData.getString(Constant.TotalLocationResult), Toast.LENGTH_LONG).show();
                //  AreaName.setText(resultData.getString(Constants.AreaNameOfLocation));
                googleAddress_textwidget.setText(resultData.getString(Constants.TotalLocationResult));
                refresh_googleAddress_loadinganim_layout.setVisibility(View.GONE);
                refresh_googleAddress_image_layout.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(Pos_OrderDetailsScreen.this, resultData.getString(Constants.TotalLocationResult), Toast.LENGTH_LONG).show();
                googleAddress_textwidget.setText("Can't get Address");
                refresh_googleAddress_loadinganim_layout.setVisibility(View.GONE);
                refresh_googleAddress_image_layout.setVisibility(View.VISIBLE);

            }
        }
    }


    private void getMerchantOrderidDetailsFromPaymentTransactionTable(String orderidtoFetchPaymentmode, String orderdetailskey) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetPaymentTransactionusingOrderid+orderidtoFetchPaymentmode ,null,
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
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                if(arrayLength>1){
                                    Toast.makeText(Pos_OrderDetailsScreen.this, "This orderid have more than 1 orders", Toast.LENGTH_LONG).show();
                                    showProgressBar(false);

                                }
                                else if(arrayLength==0){
                                    Toast.makeText(Pos_OrderDetailsScreen.this, "No Online Payment Transaction so its CashOnDelivery", Toast.LENGTH_LONG).show();
                                    showProgressBar(false);
                                    return;
                                }

                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        String paymentMode,merchantpaymentid,merchantorderid,status,desp,mobileno,userkey,paymenttype,key,orderid;
                                        JSONObject json = JArray.getJSONObject(i1);
                                        try{
                                            if(json.has("paymentmode")){
                                                paymentMode = json.getString("paymentmode");

                                            }
                                            else{
                                                paymentMode = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            paymentMode ="";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("merchantpaymentid")){
                                                merchantpaymentid = json.getString("merchantpaymentid");

                                            }
                                            else{
                                                merchantpaymentid = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            merchantpaymentid ="";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("merchantorderid")){
                                                merchantorderid = json.getString("merchantorderid");

                                            }
                                            else{
                                                merchantorderid = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            merchantorderid ="";
                                            e.printStackTrace();
                                        }
                                        try{
                                            if(json.has("status")){
                                                status = json.getString("status");

                                            }
                                            else{
                                                status = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            status ="";
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(json.has("desp")){
                                                desp = json.getString("desp");

                                            }
                                            else{
                                                desp = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            desp ="";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("mobileno")){
                                                mobileno = json.getString("mobileno");

                                            }
                                            else{
                                                mobileno = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            mobileno ="";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("userkey")){
                                                userkey = json.getString("userkey");

                                            }
                                            else{
                                                userkey = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            userkey ="";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("paymenttype")){
                                                paymenttype = json.getString("paymenttype");

                                            }
                                            else{
                                                paymenttype = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            paymenttype ="";
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(json.has("key")){
                                                key = json.getString("key");

                                            }
                                            else{
                                                key = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            key ="";
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(json.has("orderid")){
                                                orderid = json.getString("orderid");

                                            }
                                            else{
                                                orderid = "";
                                            }
                                        }
                                        catch(Exception e ){
                                            orderid ="";
                                            e.printStackTrace();
                                        }


                                        if(merchantorderid.equals(null)||merchantorderid.equals("null")){
                                            merchantorderid ="";
                                        }
                                        if(merchantpaymentid.equals(null)||merchantpaymentid.equals("null")){
                                            merchantpaymentid ="";
                                        }

                                        if((!merchantorderid.equals(""))&&(!merchantpaymentid.equals(""))&&(status.toUpperCase().equals("SUCCESS"))){
                                            changePaymentModeinOrderDetails(paymentMode,orderidtoFetchPaymentmode,orderdetailskey);
                                        }
                                        else if((!merchantorderid.equals(""))&&(merchantpaymentid.equals(""))&&(status.toUpperCase().equals("SUCCESS"))){
                                            if(paymentMode.toUpperCase().equals(Constants.RAZORPAY)){
                                                getPaymentStatusFromRazorPay(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);
                                            }
                                            else if(paymentMode.toUpperCase().equals(Constants.PAYTM)){
                                                getPaymentStatusFromPaytm(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);

                                            }
                                            else{
                                                showProgressBar(false);
                                                Toast.makeText(Pos_OrderDetailsScreen.this,"Order is :"+paymentMode,Toast.LENGTH_LONG).show();


                                            }

                                        }
                                        else if((!merchantorderid.equals(""))&&(!merchantpaymentid.equals(""))&&(!status.toUpperCase().equals("SUCCESS"))){
                                            if(paymentMode.toUpperCase().equals(Constants.RAZORPAY)){
                                                getPaymentStatusFromRazorPay(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);
                                            }
                                            else if(paymentMode.toUpperCase().equals(Constants.PAYTM)){
                                                getPaymentStatusFromPaytm(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);

                                            }
                                            else{
                                                showProgressBar(false);
                                                Toast.makeText(Pos_OrderDetailsScreen.this,"Order is :"+paymentMode,Toast.LENGTH_LONG).show();


                                            }

                                        }
                                        else if((!merchantorderid.equals(""))&&(merchantpaymentid.equals(""))&&(!status.toUpperCase().equals("SUCCESS"))){
                                            if(paymentMode.toUpperCase().equals(Constants.RAZORPAY)){
                                                getPaymentStatusFromRazorPay(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);
                                            }
                                            else if(paymentMode.toUpperCase().equals(Constants.PAYTM)){
                                                getPaymentStatusFromPaytm(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);

                                            }
                                            else{
                                                showProgressBar(false);
                                                Toast.makeText(Pos_OrderDetailsScreen.this,"Order is :"+paymentMode,Toast.LENGTH_LONG).show();


                                            }
                                        }
                                        else if((merchantorderid.equals(""))&&(merchantpaymentid.equals(""))&&(!status.toUpperCase().equals("SUCCESS"))){
                                            showProgressBar(false);

                                            Toast.makeText(Pos_OrderDetailsScreen.this,"Merchant Order Id is Empty So Can't Fetch payment mode",Toast.LENGTH_LONG).show();

                                        }
                                        else if(!status.toUpperCase().equals("SUCCESS")){
                                            if(paymentMode.toUpperCase().equals(Constants.RAZORPAY)){
                                                getPaymentStatusFromRazorPay(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);
                                            }
                                            else if(paymentMode.toUpperCase().equals(Constants.PAYTM)){
                                                getPaymentStatusFromPaytm(merchantorderid,merchantpaymentid,key,orderdetailskey,orderidtoFetchPaymentmode);

                                            }
                                            else{
                                                showProgressBar(false);
                                                Toast.makeText(Pos_OrderDetailsScreen.this,"Order is :"+paymentMode,Toast.LENGTH_LONG).show();


                                            }
                                        }
                                        else if((paymentMode.toUpperCase().equals(Constants.CASH_ON_DELIVERY))&&status.toUpperCase().equals("SUCCESS")){

                                            showProgressBar(false);
                                            Toast.makeText(Pos_OrderDetailsScreen.this,"Order Placed as Cash On Delivery",Toast.LENGTH_LONG).show();

                                        }
                                        else{
                                            showProgressBar(false);
                                            Toast.makeText(Pos_OrderDetailsScreen.this,"Problem in fetching payment mode  "+paymentMode,Toast.LENGTH_LONG).show();

                                        }







                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        showProgressBar(false);

                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                showProgressBar(false);

                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();

                            showProgressBar(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Pos_OrderDetailsScreen.this, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();

                    showProgressBar(false);


                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                }
                catch (Exception e){
                    showProgressBar(false);

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
        Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);



    }

    private void getPaymentStatusFromPaytm(String merchantorderid, String merchantpaymentid, String key, String orderdetailskey, String orderidtoFetchPaymentmode) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_GetPaymentDetailsFromPaytm+merchantorderid ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, " Response from paytm : " + response);
                            String Status = response.getString("STATUS");
                            if(Status.equals(Constants.PAYTM_SUCCESSSTATUS)){
                                changePaymentModeinOrderDetails("PAYTM",orderidtoFetchPaymentmode,orderdetailskey);
                                changePaymentStatusinPaymentTransaction("SUCCESS",key);
                            }

                            else{
                                showProgressBar(false);

                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();

                            showProgressBar(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Pos_OrderDetailsScreen.this, "No response from Paytm", Toast.LENGTH_LONG).show();

                    showProgressBar(false);


                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();
                    showProgressBar(false);

                }
            }
        })
        {



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
        Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);


    }



    private void getPaymentStatusFromRazorPay(String merchantorderid, String merchantpaymentid, String key, String orderdetailskey, String orderidtoFetchPaymentmode) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_GetPaymentDetailsFromRazorpay+merchantorderid ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, " Response from Razorpay : " + response);
                            String Status = response.getString("status");
                            if(Status.equals(Constants.RAZORPAY_SUCCESSSTATUS)){
                                changePaymentModeinOrderDetails("RAZORPAY",orderidtoFetchPaymentmode,orderdetailskey);
                                changePaymentStatusinPaymentTransaction("SUCCESS",key);
                            }
                            else{
                                showProgressBar(false);

                            }



                        }
                        catch (Exception e){
                            e.printStackTrace();
                            showProgressBar(false);



                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Pos_OrderDetailsScreen.this, "No response from Razorpay", Toast.LENGTH_LONG).show();
                    showProgressBar(false);



                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                }
                catch (Exception e){
                    showProgressBar(false);

                    e.printStackTrace();
                }
            }
        })
        {



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
        Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);

    }



    private void changePaymentModeinOrderDetails(String PaymentMode, String orderidtoFetchPaymentmode, String orderdetailskey) {

/*
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key", orderdetailskey);
            jsonObject.put("paymentmode", PaymentMode);
            Log.i("tag","listenertoken"+ "");






        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);
            showProgressBar(false);

        }

 */

        JSONObject jsonObject = new JSONObject();
        String Api_toChangeOrderDetailsUsingOrderid = "";


        if(orderdetailsnewschema){
            if(orderid.length()>1 && vendorkey.length()>1 && customerMobileNo.length()>1){
                try {
                    jsonObject.put("paymentmode", PaymentMode);
                    jsonObject.put("vendorkey", vendorkey);
                    jsonObject.put("orderid", orderid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Api_toChangeOrderDetailsUsingOrderid = Constants.api_UpdateVendorOrderDetails+ "?vendorkey="+vendorkey+"&orderid="+orderid;
                Context mContext = Pos_OrderDetailsScreen.this;
                JSONObject customerDetails_JsonObject = new JSONObject();

                try {
                    customerDetails_JsonObject.put("paymentmode", PaymentMode);
                    customerDetails_JsonObject.put("orderid", orderid);
                    customerDetails_JsonObject.put("usermobileno", customerMobileNo);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String apiToUpdateCustomerOrderDetails = Constants.api_UpdateCustomerOrderDetails +"?usermobileno="+customerMobileNo+"&orderid="+orderid;

                initUpdateCustomerOrderDetailsInterface(mContext);
                Update_CustomerOrderDetails_TrackingTable_AsyncTask asyncTask_TO_update =new Update_CustomerOrderDetails_TrackingTable_AsyncTask(mContext, mResultCallback_UpdateCustomerOrderDetailsTableInterface,customerDetails_JsonObject,apiToUpdateCustomerOrderDetails );
                asyncTask_TO_update.execute();


            }
            else{
                Toast.makeText(Pos_OrderDetailsScreen.this, "orderid :"+orderid+" , vendorkey: "+vendorkey+" , customerMobileNo : "+ customerMobileNo, Toast.LENGTH_SHORT).show();
            }

        }
        else {
            try {
                jsonObject.put("key", orderdetailskey);
                jsonObject.put("paymentmode", PaymentMode);
                Log.i("tag", "listenertoken" + "");


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "JSONOBJECT: " + e);
                showProgressBar(false);

            }
            Log.d(Constants.TAG, "Request Payload: " + jsonObject);



            Api_toChangeOrderDetailsUsingOrderid = Constants.api_Update_OrderDetails;

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_toChangeOrderDetailsUsingOrderid,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try {



               //     Toast.makeText(Pos_OrderDetailsScreen.this, " : "+fromActivityName, Toast.LENGTH_LONG).show();

                    if(fromActivityName.equals("PosManageOrders")) {
                        if(Pos_ManageOrderFragment.sorted_OrdersList.size()>0){
                            for(int i =0; i<Pos_ManageOrderFragment.sorted_OrdersList.size();i++){
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =Pos_ManageOrderFragment.sorted_OrdersList.get(i);
                                String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                    modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                    Pos_ManageOrderFragment.manageOrdersListViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                        if(Pos_ManageOrderFragment.ordersList.size()>0){
                            for(int i =0; i<Pos_ManageOrderFragment.ordersList.size();i++){
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =Pos_ManageOrderFragment.ordersList.get(i);
                                String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                    modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                    Pos_ManageOrderFragment.manageOrdersListViewAdapter.notifyDataSetChanged();
                                }
                            }
                        }

                    }
                    if(fromActivityName.equals("WholeSaleOrdersList")) {
                        if(WholeSaleOrdersList.sorted_OrdersList.size()>0){
                            for(int i =0; i<WholeSaleOrdersList.sorted_OrdersList.size();i++){
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =WholeSaleOrdersList.sorted_OrdersList.get(i);
                                String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                    modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                    WholeSaleOrdersList.adapter_pos_wholeSaleOrderList.notifyDataSetChanged();
                                }
                            }
                        }

                        if(WholeSaleOrdersList.ordersList.size()>0){
                            for(int i =0; i<WholeSaleOrdersList.ordersList.size();i++){
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =WholeSaleOrdersList.ordersList.get(i);
                                String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                    modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);
                                    WholeSaleOrdersList.adapter_pos_wholeSaleOrderList.notifyDataSetChanged();
                                }
                            }
                        }

                    }



                    if(fromActivityName.equals("MobileGetDeliveryPartnerAssignedOrder")) {
                        try {
                            if (GetDeliverypartnersAssignedOrders.ordersList.size() > 0) {
                                for (int i = 0; i < GetDeliverypartnersAssignedOrders.ordersList.size(); i++) {
                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = GetDeliverypartnersAssignedOrders.ordersList.get(i);

                                    String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                    if (Orderid_fromArray.equals(orderidtoFetchPaymentmode)) {
                                        modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);


                                        try {
                                            GetDeliverypartnersAssignedOrders.adapter_mobile_getDeliveryPartnersAssignedOrders.notifyDataSetChanged();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }



                                    }
                                }
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        try{

                            if(DeliveryPartnerSettlementReport.result_JArray.length()>0){

                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i11 = 0;
                                int arrayLength1 = DeliveryPartnerSettlementReport.result_JArray.length();
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                //  Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                                if(arrayLength1>0) {

                                    for (; i11 < (arrayLength1); i11++) {

                                        try {
                                            JSONObject jsonv = DeliveryPartnerSettlementReport.result_JArray.getJSONObject(i11);
                                            String orderidd= jsonv.getString("orderid");

                                            if(orderidd.equals(orderidtoFetchPaymentmode)) {
                                                jsonv.put("paymentmode", PaymentMode);


                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    }



                    if(fromActivityName.equals("AppOrdersList") || fromActivityName.equals("AppSearchOrders")) {

                        if(searchOrdersUsingMobileNumber.sorted_OrdersList.size()>0){
                            for(int i =0; i<searchOrdersUsingMobileNumber.sorted_OrdersList.size();i++){

                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =searchOrdersUsingMobileNumber.sorted_OrdersList.get(i);
                                String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                    modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);




                                    try {
                                        searchOrdersUsingMobileNumber.adapter_PosSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }



                                }
                            }
                        }


                        if(searchOrdersUsingMobileNumber.ordersList.size()>0){
                            for(int i =0; i<searchOrdersUsingMobileNumber.ordersList.size();i++){
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =searchOrdersUsingMobileNumber.ordersList.get(i);

                                String Orderid_fromArray = modal_manageOrders_pojo_class.getOrderid().toString();
                                if(Orderid_fromArray.equals(orderidtoFetchPaymentmode)){
                                    modal_manageOrders_pojo_class.setPaymentmode(PaymentMode);


                                    try {
                                        searchOrdersUsingMobileNumber.adapter_PosSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }                                                        }
                            }
                        }


                    }
                    else{


                      //  Toast.makeText(Pos_OrderDetailsScreen.this, "T   "+fromActivityName, Toast.LENGTH_LONG).show();

                    }





                }
                catch (Exception e ){
                    e.printStackTrace();
                    showProgressBar(false);

                }



                try{
                    paymentTypetext_widget.setText(PaymentMode);
                }
                catch (Exception e){
                    e.printStackTrace();
                    showProgressBar(false);

                }

                Log.d(Constants.TAG, "Responsewwwww: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);

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
        Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);

    }

    private void initUpdateCustomerOrderDetailsInterface(Context mContext) {

        mResultCallback_UpdateCustomerOrderDetailsTableInterface  = new Update_CustomerOrderDetails_TrackingTableInterface() {
            @Override
            public void notifySuccess(String requestType, String success) {
                try{
                    Toast.makeText(mContext, "Succesfully Updated the Payment Mode in Customer Details", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                try{
                    Toast.makeText(mContext, "Failed to Updated the Payment Mode in Customer Details", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
    }

    private void changePaymentStatusinPaymentTransaction(String status, String key) {

        try {
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("status", status);
                jsonObject.put("key", key);

                Log.i("tag", "listenertoken" + "");


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "JSONOBJECT: " + e);
                showProgressBar(false);

            }
            Log.d(Constants.TAG, "Request Payload: " + jsonObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updatePaymentTransactionTable, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            try {
                                Log.d(Constants.TAG, "Request Payload: response  ");

                                showProgressBar(false);



                            } catch (Exception e) {
                                e.printStackTrace();
                                showProgressBar(false);

                            }


                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    Log.d(Constants.TAG, "Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Error: " + error.toString());


                    showProgressBar(false);

                    error.printStackTrace();
                }
            }) {
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
            Volley.newRequestQueue(Pos_OrderDetailsScreen.this).add(jsonObjectRequest);

        } catch (Exception e) {
            showProgressBar(false);

            e.printStackTrace();
        }
    }





    @Override
    public void onBackPressed() {

        if(fromActivityName.equals("PosSearchOrders")) {
            Intent i = new Intent(this, Pos_Orders_List.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }

        if(fromActivityName.equals("AppSearchOrders")) {
            Intent i = new Intent(this, searchOrdersUsingMobileNumber.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }

        if(fromActivityName.equals("PosManageOrders")) {
            Intent i = new Intent(this, Pos_Dashboard_Screen.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        if(fromActivityName.equals("WholeSaleOrdersList")) {
            Intent i = new Intent(this, WholeSaleOrdersList.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }
        else{
            super.onBackPressed();

        }
    }
}


