package com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.GetDeliverypartnersAssignedOrders;
import com.meatchop.tmcpartner.Settings.Helper;
import com.meatchop.tmcpartner.Settings.searchOrdersUsingMobileNumber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.meatchop.tmcpartner.Constants.TAG;

public class MobileScreen_OrderDetails1 extends AppCompatActivity {
    static  TextView mobileNotext_widget,ordertypetext_widget,orderplacedtime_textwidget,orderConfirmedtime_textwidget,orderReaytime_textwidget,orderpickeduptime_textwidget,orderDeliveredtime_textwidget,orderIdtext_widget,orderStatustext_widget,paymentTypetext_widget,slotNametext_widget,slotDatetext_widget
            ,deliveryPartner_name_widget,deliveryPartner_mobileNo_widget,delivery_type_widget,slotTime_Range_textwidget;
    TextView distancebetweencustomer_vendortext_widget,discounttext_widget,addresstype_textwidget,AddressLine2_textwidget,landmark_textwidget,AddressLine1_textwidget,total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;
    Button changeDeliveryPartner;
    Adapter_Mobile_orderDetails_itemDesp_listview1 adapter_forOrderDetails_listview;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp;
    ListView itemDesp_listview;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
    String coupondiscountAmount,useraddreskey,vendorLongitude,vendorLatitude,customerlatitude,customerLongitutde,
            deliverydistance,deliverypartnerKey,DeliveryPersonList,deliverypartnerName="",deliveryPartnerNumber="",ordertype,fromActivityName;
    double screenInches;
    LinearLayout showlocation,deliveryPartnerAssignLayout;
    public static BottomSheetDialog bottomSheetDialog;
    static LinearLayout loadingPanel;
    static LinearLayout loadingpanelmask;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_screen__order_details1);
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
        ordertypetext_widget = findViewById(R.id.ordertypetext_widget);
        distancebetweencustomer_vendortext_widget = findViewById(R.id.distancebetweencustomer_vendortext_widget);
        mobileNotext_widget = findViewById(R.id.mobileNotext_widget);
        showlocation = findViewById(R.id.showlocation);
        deliveryPartnerAssignLayout = findViewById(R.id.deliveryPartnerAssignLayout);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        deliveryPartnerList = new ArrayList<>();

        try {
            SharedPreferences shared = getApplicationContext().getSharedPreferences("Vendor//LoginData", MODE_PRIVATE);


            vendorLatitude = (shared.getString("VendorLatitude", "12.9406"));
            vendorLongitude = (shared.getString("VendorLongitute", "80.1496"));
        }
        catch (Exception e){
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
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);

        Bundle bundle = getIntent().getExtras();
        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = bundle.getParcelable("data");
        fromActivityName = bundle.getString("From");
        try{
            deliveryPartnerNumber=getIntent().getStringExtra("deliveryusermobileno");
            deliverypartnerName=getIntent().getStringExtra("deliveryusername");
            if(deliverypartnerName.equals(null)){
                deliverypartnerName="null";

            }
            if((deliverypartnerName.equals(""))||(deliveryPartnerNumber.equals(""))){
                try{
                    deliverypartnerName = String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerName());
                    deliveryPartnerNumber = String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerMobileNo());

                }
                catch(Exception r){
                    if(deliverypartnerName.equals(null)){
                        deliverypartnerName="null";

                    }
                    r.printStackTrace();
                }

            }


        }
        catch (Exception e){
            e.printStackTrace();
            try {
                deliveryPartnerNumber = String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerMobileNo());
                deliverypartnerName = String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerName());
                if(deliverypartnerName.equals(null)){
                    deliverypartnerName="null";

                }
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }


        try{
            ordertype = String.valueOf(modal_manageOrders_pojo_class.getOrderType().toUpperCase());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(ordertype.equals(Constants.POSORDER)){
            showlocation.setVisibility(View.GONE);
        }
        try{
            if(ordertype.equals(Constants.APPORDER)) {
                deliveryPartnerAssignLayout.setVisibility(View.VISIBLE);
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
            }else{
                deliveryPartnerAssignLayout.setVisibility(View.GONE);

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




        if(!order_status.equals(Constants.NEW_ORDER_STATUS)&&(!order_type.equals("POSORDER"))) {

            try {
                deliveryPartner_mobileNo_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerMobileNo()));

                changeDeliveryPartner.setText("Change Delivery Person");
                deliveryPartner_name_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getDeliveryPartnerName()));
                delivery_type_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getDeliverytype()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            deliveryPartner_mobileNo_widget.setText("Not Assigned");
            deliveryPartner_name_widget.setText("Not Assigned");
            changeDeliveryPartner.setText("Assign Delivery Person");
            delivery_type_widget.setText(String.valueOf(""));


        }}
        catch (Exception e){
        e.printStackTrace();
    }
        if((!modal_manageOrders_pojo_class.getOrderstatus().equals(Constants.NEW_ORDER_STATUS))||(!fromActivityName.equals("MobileGetDeliveryPartnerAssignedOrder"))) {


            if(!deliveryPartnerNumber.equals("null")){
                deliveryPartner_mobileNo_widget.setText(deliveryPartnerNumber);
            }
            else{
                deliveryPartner_mobileNo_widget.setText("Not Assigned");

            }
            if(!deliverypartnerName.equals("null")){
                deliveryPartner_name_widget.setText(deliverypartnerName);

            }
            else{
                deliveryPartner_name_widget.setText("Not Assigned");

            }
            changeDeliveryPartner.setText("Change Delivery Person");

        }
        else{
            deliveryPartner_mobileNo_widget.setText("Not Assigned");
            deliveryPartner_name_widget.setText("Not Assigned");
            changeDeliveryPartner.setText("Assign Delivery Person");

        }



        showlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customerlatitude = String.valueOf(modal_manageOrders_pojo_class.getUseraddresslat());
                customerLongitutde =String.valueOf(modal_manageOrders_pojo_class.getUseraddresslon());

                OpenGoogleMapAndDrawRoute(customerlatitude,customerLongitutde);

            }
        });


        changeDeliveryPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           /*     Intent intent = new Intent(MobileScreen_OrderDetails1.this, MobileScreen_AssignDeliveryPartner1.class);
                intent.putExtra("TrackingTableKey",modal_manageOrders_pojo_class.getKeyfromtrackingDetails());
                intent.putExtra("IntentFrom",fromActivityName);

                startActivityForResult(intent,1234);
*/
                if(!deliverypartnerName.equals("null")) {

                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,deliverypartnerName);

                }
                else{

                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,"null");

                }




            }
        });

        orderIdtext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderid()));
        orderStatustext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getOrderstatus()));
        paymentTypetext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getPaymentmode()));
        slotNametext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotdate()));
        slotDatetext_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getSlotname()));

        delivery_type_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getDeliverytype()));




        String itemDespString = modal_manageOrders_pojo_class.getItemdesp_string();
        try {
            JSONArray jsonArray = new JSONArray(itemDespString);
            for(int i=0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

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



                if(json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                    Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname");
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
        adapter_forOrderDetails_listview = new Adapter_Mobile_orderDetails_itemDesp_listview1(MobileScreen_OrderDetails1.this, OrderdItems_desp);
        itemDesp_listview.setAdapter(adapter_forOrderDetails_listview);
        Helper.getListViewSize(itemDesp_listview, screenInches);

    }

    private void showBottomSheetDialog(String orderkey, String deliverypartnerName) {

        bottomSheetDialog = new BottomSheetDialog(MobileScreen_OrderDetails1.this);
        bottomSheetDialog.setContentView(R.layout.mobilescreen_assigndeliverypartner_bottom_sheet_dialog);

        ListView ListView1 = bottomSheetDialog.findViewById(R.id.listview);

        Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(MobileScreen_OrderDetails1.this, deliveryPartnerList,orderkey,fromActivityName+"orderdetails",deliverypartnerName);

        ListView1.setAdapter(adapter_mobile_assignDeliveryPartner1);

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
        Volley.newRequestQueue(MobileScreen_OrderDetails1.this).add(jsonObjectRequest);
    }


    @SuppressLint("QueryPermissionsNeeded")
    void OpenGoogleMapAndDrawRoute(String userLatitude, String userLongitude) {
      /*  Uri MapUri = Uri.parse("google.navigation:q="+ userLatitude + ","  + userLongitude);
        Intent MapIntent = new Intent(Intent.ACTION_VIEW,MapUri);
        MapIntent.setComponent(new ComponentName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity"));

        try {
            if((MapIntent.resolveActivity(getPackageManager()))!=null){
                startActivity(MapIntent);
            }
        }catch (NullPointerException nu){
            //Log.e(Constants.TAG, "NullPointerException", nu.getCause());

            Toast.makeText(MobileScreen_OrderDetails1.this,"Can't Open Map",Toast.LENGTH_LONG).show();
        }

       */

/*
        String url = "https://www.google.com/maps/search/?api=1&query="+userLatitude+","+userLongitude;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(url));
        startActivity(intent);

 */

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(userLatitude+","+userLongitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);



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
        int new_totalAmount_withGst = (int) Math.ceil(new_to_pay_Amount);

        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst)+".00");
        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(fromActivityName.equals("MobileManageOrders")) {
            Intent i = new Intent(this, MobileScreen_Dashboard.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }



        if(fromActivityName.equals("MobileGetDeliveryPartnerAssignedOrder")) {
            Intent i = new Intent(this, GetDeliverypartnersAssignedOrders.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }



        if(fromActivityName.equals("MobileSearchOrders")) {
            Intent i = new Intent(this, searchOrdersUsingMobileNumber.class);

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }


    }
}


