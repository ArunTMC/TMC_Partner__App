package com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meatchop.tmcpartner.Constants.TAG;

public class Pos_OrderDetailsScreen extends AppCompatActivity {
TextView mobileNotext_widget,ordertypetext_widget,orderplacedtime_textwidget,orderConfirmedtime_textwidget,orderReaytime_textwidget,orderpickeduptime_textwidget,orderDeliveredtime_textwidget,orderIdtext_widget,orderStatustext_widget,paymentTypetext_widget,slotNametext_widget,slotDatetext_widget
        ,deliveryPartner_name_widget,deliveryPartner_mobileNo_widget,delivery_type_widget,slotTime_Range_textwidget;
TextView distancebetweencustomer_vendortext_widget,discounttext_widget,addresstype_textwidget,AddressLine2_textwidget,landmark_textwidget,AddressLine1_textwidget,total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;
Button changeDeliveryPartner;
    Adapter_forOrderDetails_Listview adapter_forOrderDetails_listview;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp;
    ListView itemDesp_listview;
    LinearLayout deliveryPartnerAssignLayout;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
    String ordertype,coupondiscountAmount,useraddreskey,vendorLongitude,vendorLatitude,customerlatitude,customerLongitutde,deliverydistance;
    ScrollView parentScrollView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos__order_details_screen_activity);
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
        deliveryPartnerAssignLayout = findViewById(R.id.deliveryPartnerAssignLayout);

        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);


        vendorLatitude = (shared.getString("VendorLatitude", "12.9406"));
        vendorLongitude = (shared.getString("VendorLongitute", "80.1496"));


        OrderdItems_desp = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = bundle.getParcelable("data");
        ordertype = String.valueOf(modal_manageOrders_pojo_class.getOrderType());
            try {
                if(ordertype.equals(Constants.APPORDER)) {

                    customerlatitude = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslat());
                    customerLongitutde = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslon());
                    try {
                        deliverydistance = String.valueOf(modal_manageOrders_pojo_class.getDeliverydistance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if ((!deliverydistance.equals("0") && (deliverydistance.length() > 0) && (!deliverydistance.equals("null")))) {
                        distancebetweencustomer_vendortext_widget.setText(deliverydistance+"Km");

                    } else {
                        try {
                            CalculateDistanceviaApi(distancebetweencustomer_vendortext_widget);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

            parentScrollView.fullScroll(View.FOCUS_UP);

        }
        catch (Exception e){
            e.printStackTrace();
        }




        changeDeliveryPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Pos_OrderDetailsScreen.this,AssigningDeliveryPartner.class);
                intent.putExtra("TrackingTableKey",modal_manageOrders_pojo_class.getKeyfromtrackingDetails());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(intent);
            }
        });



        String itemDespString = modal_manageOrders_pojo_class.getItemdesp_string();
        try {
            JSONArray jsonArray = new JSONArray(itemDespString);
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if(json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                    Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname");
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
                if (json.has("netweight")) {
                    manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("netweight"));

                }
                else{
                    manageOrders_pojo_class.ItemFinalWeight = "";

                }

                manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));
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

    }

    private void CalculateDistanceviaApi(TextView distancebetweencustomer_vendortext_widget) throws JSONException {
        Log.i("Tag", "Latlangcal");


            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + customerlatitude + "," + customerLongitutde + "&destinations=" + vendorLatitude + "," + vendorLongitude + "&mode=driving&language=en-EN&sensor=false&key=AIzaSyDYkZGOckF609Cjt6mnyNX9QhTY9-kAqGY";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    try {

                        Log.d(Constants.TAG, "response " + response.toString());

                        JSONArray rowsArray = (JSONArray) response.get("rows");
                        Log.d(Constants.TAG, "rows" + rowsArray.toString());
                        JSONObject elements = rowsArray.getJSONObject(0);
                        Log.d(Constants.TAG, "elements" + elements.toString());

                        JSONArray elementsArray = (JSONArray) elements.get("elements");
                        Log.d(Constants.TAG, "elementsArray" + elementsArray.toString());

                        JSONObject distance = elementsArray.getJSONObject(0);
                        Log.d(Constants.TAG, "distance" + distance.toString());
                        JSONObject jsondistance = distance.getJSONObject("distance");
                        Log.d(Constants.TAG, "jsondistance :" + jsondistance);

                        String distanceinString = jsondistance.getString("text");
                        Log.d(Constants.TAG, "distanceinString :" + distanceinString);
                        distancebetweencustomer_vendortext_widget.setText(distanceinString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                    Log.d(Constants.TAG, "Error2: " + error.getMessage());
                    Log.d(Constants.TAG, "Error3: " + error.toString());

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
                Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);
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
            Log.i(TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
            Log.i(TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);

            getOrderAmountDetails.setTotalAmountWithoutGst(String.valueOf(decimalFormat.format(old_total_Amount)));




            try{
                taxes_and_chargesfromArray = Double.parseDouble(getOrderAmountDetails.getGstAmount());

            }
            catch (Exception e){
                e.printStackTrace();
            }
            //find total GST amount
            Log.i(TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);
            //taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);



            Log.i(TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
            Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
            Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
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
        int new_totalAmount_withGst = (int) Math.ceil(new_to_pay_Amount);

        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst)+".00");
        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(this, Pos_Dashboard_Screen.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
    }
}


