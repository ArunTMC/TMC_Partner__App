package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.Adapter_Mobile_orderDetails_itemDesp_listview1;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAndRatingDetailsScreen extends AppCompatActivity {
   String vendorLatitude="",vendorLongitude="",vendorName="",vendorKey="",orderid="",itemnamefromOrderDetails="";

    static  TextView mobileNotext_widget,ordertypetext_widget,orderplacedtime_textwidget,orderConfirmedtime_textwidget,orderReadytime_textwidget,orderpickeduptime_textwidget,orderDeliveredtime_textwidget,orderIdtext_widget,orderStatustext_widget,paymentTypetext_widget,slotNametext_widget,slotDatetext_widget
            ,deliveryPartner_name_widget,deliveryPartner_mobileNo_widget,delivery_type_widget,slotTime_Range_textwidget;
    TextView feedBackText_widget, deliveryCharges_text_widget,notestext_widget,googleAddress_textwidget,distancebetweencustomer_vendortext_widget,discounttext_widget,addresstype_textwidget,AddressLine2_textwidget,landmark_textwidget,AddressLine1_textwidget,total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;
    ListView itemDesp_listview;

    static LinearLayout loadingPanel;
    static LinearLayout loadingpanelmask;
    RatingBar qualityratingBar,deliveryratingBar;

    Modal_OrderDetails_Tracking_RatingDetails modal_orderDetails_tracking_ratingDetails;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
    String orderdetailskey,deliveryCharges,coupondiscountAmount;
    boolean isTrackingCalled =false, isOrderDetailsCalled =false, isRatingDetailsCalled =false;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp;
    double screenInches;
    boolean orderdetailsnewschema = false;
    boolean isOrderDetailsScreenOpened =  false;
            RaisedTicketDetailsForRating raisedTicketDetailsForRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_and_rating_details_screen);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();

        try {
            SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);

             vendorKey = shared.getString("VendorKey", "");
            vendorName = shared.getString("VendorName", "");
            vendorLatitude = (shared.getString("VendorLatitude", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));

            vendorLongitude = (shared.getString("VendorLongitute", ""));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderIdtext_widget = findViewById(R.id.orderIdtext_widget);
            orderStatustext_widget = findViewById(R.id.orderStatustext_widget);
            paymentTypetext_widget = findViewById(R.id.paymentTypetext_widget);
            slotNametext_widget = findViewById(R.id.mobileslotNametext_widget);
            slotDatetext_widget = findViewById(R.id.mobileslotDatetext_widget);
            deliveryPartner_name_widget = findViewById(R.id.deliveryPartner_name_widget);
            deliveryPartner_mobileNo_widget = findViewById(R.id.deliveryPartner_mobileNo_widget);
            delivery_type_widget = findViewById(R.id.delivery_type_widget);
            total_item_Rs_text_widget = findViewById(R.id.total_amount_text_widget);
            total_Rs_to_Pay_text_widget = findViewById(R.id.total_Rs_to_Pay_text_widget);
            taxes_and_Charges_rs_text_widget = findViewById(R.id.taxes_and_Charges_rs_text_widget);
            slotTime_Range_textwidget = findViewById(R.id.slotTimeeee_Range_textwidget);
            AddressLine1_textwidget = findViewById(R.id.AddressLine1_textwidget);
            AddressLine2_textwidget = findViewById(R.id.AddressLine2_textwidget);
            landmark_textwidget = findViewById(R.id.landmark_textwidget);
            addresstype_textwidget=findViewById(R.id.addresstype_textwidget);
            orderplacedtime_textwidget = findViewById(R.id.orderplacedtime_textwidget);
            orderConfirmedtime_textwidget = findViewById(R.id.orderConfirmedtime_textwidget);
            orderReadytime_textwidget = findViewById(R.id.orderReadytime_textwidget);
            orderpickeduptime_textwidget = findViewById(R.id.orderpickeduptime_textwidget);
            orderDeliveredtime_textwidget = findViewById(R.id.orderDeliveredtime_textwidget);
            discounttext_widget = findViewById(R.id.discounttext_widget);
            ordertypetext_widget = findViewById(R.id.ordertypetext_widget);
            distancebetweencustomer_vendortext_widget = findViewById(R.id.distancebetweencustomer_vendortext_widget);
            mobileNotext_widget = findViewById(R.id.mobileNotext_widget);
            itemDesp_listview = findViewById(R.id.itemDesp_listview);

            googleAddress_textwidget = findViewById(R.id.googleAddress_textwidget);
            notestext_widget = findViewById(R.id.notestext_widget);
            loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
            loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
            deliveryCharges_text_widget = findViewById(R.id.deliveryCharges_text_widget);
             qualityratingBar = findViewById(R.id.qualityratingBar);
             deliveryratingBar = findViewById(R.id.deliveryratingBar);
            feedBackText_widget  = findViewById(R.id.feedBackText_widget);
            OrderdItems_desp = new ArrayList<>();

            modal_orderDetails_tracking_ratingDetails = new Modal_OrderDetails_Tracking_RatingDetails();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        


        try{

            Bundle bundle = getIntent().getExtras();

            isOrderDetailsScreenOpened = bundle.getBoolean("isOrderDetailsScreenOpened",false);
            if(isOrderDetailsScreenOpened ) {
                modal_orderDetails_tracking_ratingDetails = bundle.getParcelable("data");
                orderid = bundle.getString("orderid");

            }
            else{
                Intent intent = getIntent();

                orderid = intent.getStringExtra("orderid");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            orderid ="";
        }

    if( !isOrderDetailsScreenOpened || modal_orderDetails_tracking_ratingDetails.equals(new Modal_OrderDetails_Tracking_RatingDetails()) || modal_orderDetails_tracking_ratingDetails.equals(null)) {
        if (!orderid.equals("")) {

                getOrderDetailsUsingOrderid(orderid);



        } else {
            Toast.makeText(OrderAndRatingDetailsScreen.this, " Orderid is Empty ", Toast.LENGTH_LONG).show();
        }

    }
    else{
        displaydatainScreen(modal_orderDetails_tracking_ratingDetails, "FromIntent");

        Toast.makeText(OrderAndRatingDetailsScreen.this, " Already Opened ", Toast.LENGTH_LONG).show();

    }








    }

    private void getRatingDetailsUsingOrderid(String orderid) {

        showProgressBar(true);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetRatingDetailsUsingOrderid +orderid,null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {

                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + OrderDetailsResultjsonString);
                            int i1=0;

                            JSONArray contentArray = response.getJSONArray("content");
                            if(contentArray.length()==0){
                                    //    
                                    

                                    return;
                                
                            }
                            else
                            {

                                for(;i1<(contentArray.length());i1++) {
                                    JSONObject json = contentArray.getJSONObject(i1);
                                    try{
                                        if(json.has("createdtime")){
                                            modal_orderDetails_tracking_ratingDetails.createdtime = json.getString("createdtime");


                                        }
                                        else{

                                            modal_orderDetails_tracking_ratingDetails.createdtime = "";
                                        }
                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                        modal_orderDetails_tracking_ratingDetails.createdtime = "";
                                    }




                                    try{
                                        if(json.has("feedback")){
                                            modal_orderDetails_tracking_ratingDetails.feedback = json.getString("feedback");


                                        }
                                        else{
                                            modal_orderDetails_tracking_ratingDetails.feedback = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.feedback = "";
                                        e.printStackTrace();
                                    }








                                    try{
                                        if(json.has("itemrating")){

                                            String itemName="";
                                            String itemrating = "";
                                            itemrating = json.getString("itemrating");
                                            if(itemrating.equals("")||itemrating.equals(null)){

                                                //    new GetItemdetailsfromOrderDetailsNew(orderid).execute();



                                               modal_orderDetails_tracking_ratingDetails.itemname_rating = itemnamefromOrderDetails ;
                                                itemName =  itemnamefromOrderDetails ;
                                            }
                                            else {

                                                modal_orderDetails_tracking_ratingDetails.itemrating_json = itemrating;
                                                JSONArray itemratingarray = new JSONArray(itemrating);
                                                for (int i = 0; i < itemratingarray.length(); i++) {

                                                    JSONObject itemRatingjson = itemratingarray.getJSONObject(i);
                                                    try {
                                                        if (itemRatingjson.has("rating")) {
                                                            modal_orderDetails_tracking_ratingDetails.itemrating = itemRatingjson.getString("rating");
                                                            String itemRating = json.getString("rating");
                                                            if (itemRating.equals("") || itemRating.equals("null") || itemRating.equals(null)) {
                                                                itemRating = "0";
                                                            }
                                                           // itemrating_double = Double.parseDouble(itemRating);


                                                        } else {
                                                            modal_orderDetails_tracking_ratingDetails.itemrating = "";
                                                            String itemRating = "0";

                                                            //itemrating_double = Double.parseDouble(itemRating);


                                                        }
                                                    } catch (Exception e) {

                                                        modal_orderDetails_tracking_ratingDetails.itemrating = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        if (itemRatingjson.has("itemname")) {
                                                            if (itemName.length() > 0) {
                                                                itemName = itemName + " , " + itemRatingjson.getString("itemname");

                                                            } else {
                                                                itemName = itemRatingjson.getString("itemname");
                                                            }
                                                            modal_orderDetails_tracking_ratingDetails.itemname = itemName;
                                                        } else {
                                                            modal_orderDetails_tracking_ratingDetails.itemname = " - ";
                                                            itemName = " - ";
                                                        }
                                                    } catch (Exception e) {
                                                        modal_orderDetails_tracking_ratingDetails.itemname = " - ";
                                                        itemName = " - ";
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }
                                        }
                                        else{
                                            modal_orderDetails_tracking_ratingDetails.itemrating_json = " - ";
                                            modal_orderDetails_tracking_ratingDetails.itemname =" - ";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.itemname =" - ";

                                        modal_orderDetails_tracking_ratingDetails.itemrating_json = " - ";
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("key")){
                                            modal_orderDetails_tracking_ratingDetails.rating_key = json.getString("key");

                                        }
                                        else{

                                            modal_orderDetails_tracking_ratingDetails.rating_key = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.rating_key = "";
                                        e.printStackTrace();
                                    }



                                    try{
                                        if(json.has("userkey")){
                                            modal_orderDetails_tracking_ratingDetails.userkey = json.getString("userkey");


                                        }
                                        else{
                                            modal_orderDetails_tracking_ratingDetails.userkey = "";
                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.userkey = "";
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("usermobileno")){
                                            modal_orderDetails_tracking_ratingDetails.usermobileno = json.getString("usermobileno");

                                        }
                                        else{

                                            modal_orderDetails_tracking_ratingDetails.usermobileno = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.usermobileno = "";
                                        e.printStackTrace();
                                    }



                                    try{
                                        if(json.has("vendorkey")){
                                            modal_orderDetails_tracking_ratingDetails.vendorkey = json.getString("vendorkey");

                                        }
                                        else{

                                            modal_orderDetails_tracking_ratingDetails.vendorkey = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.vendorkey = "";
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("vendorname")){
                                            modal_orderDetails_tracking_ratingDetails.vendorname = json.getString("vendorname");

                                        }
                                        else{

                                            modal_orderDetails_tracking_ratingDetails.vendorname = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.vendorname = "";
                                        e.printStackTrace();
                                    }





                                    try{
                                        if(json.has("deliveryrating")){
                                            String deliveryRating = json.getString("deliveryrating");
                                            if(deliveryRating.equals("")||deliveryRating.equals("null")||deliveryRating.equals(null)){
                                                deliveryRating = "0";
                                            }
                                            modal_orderDetails_tracking_ratingDetails.deliveryrating = deliveryRating;

                                            // deliveryRatingArray.add(deliveryRating);
                                            //deliveryrating_double = Double.parseDouble(deliveryRating);


                                        }
                                        else{

                                            String  deliveryRating = "0";
                                            modal_orderDetails_tracking_ratingDetails.deliveryrating = "0";

                                            //deliveryrating_double = Double.parseDouble(deliveryRating);

                                            //   deliveryRatingArray.add(deliveryRating);
                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.deliveryrating  = "0";
                                        e.printStackTrace();
                                    }
                                    try{
                                        if(json.has("qualityrating")){
                                            String qualityrating = json.getString("qualityrating");
                                            if(qualityrating.equals("")||qualityrating.equals("null")||qualityrating.equals(null)){
                                                qualityrating = "0";
                                            }
                                            modal_orderDetails_tracking_ratingDetails.qualityrating = qualityrating;


                                            //    qualityRatingArray.add(qualityrating);

                                            //   qualityrating_double = Double.parseDouble(qualityrating);

                                        }
                                        else{
                                            modal_orderDetails_tracking_ratingDetails.qualityrating = "0";

                                            String  qualityrating = "0";
                                            //qualityrating_double = Double.parseDouble(qualityrating);

                                            //   qualityRatingArray.add(qualityrating);
                                        }
                                    }
                                    catch(Exception e){
                                        modal_orderDetails_tracking_ratingDetails.qualityrating  = "0";
                                        e.printStackTrace();
                                    }


                                    isRatingDetailsCalled = true;
                                    displaydatainScreen(modal_orderDetails_tracking_ratingDetails, "FromRating");
                                }


                                //Log.d(Constants.TAG, "ratingList:   " +ratingList.size());

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
                    


//
                    // 
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
        Volley.newRequestQueue(OrderAndRatingDetailsScreen.this).add(jsonObjectRequest);






    }

    private void displaydatainScreen(Modal_OrderDetails_Tracking_RatingDetails modal_orderDetails_tracking_ratingDetails, String FromWhatFunction) {
        try{
            String orderid = modal_orderDetails_tracking_ratingDetails.getOrderid().toString();
            changeObjectDataInRatingFirstScreenArray(orderid,modal_orderDetails_tracking_ratingDetails);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            mobileNotext_widget.setText(modal_orderDetails_tracking_ratingDetails.getUsermobile().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderIdtext_widget.setText(modal_orderDetails_tracking_ratingDetails.getOrderid().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            ordertypetext_widget.setText(modal_orderDetails_tracking_ratingDetails.getOrdertype().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            orderStatustext_widget.setText(modal_orderDetails_tracking_ratingDetails.getOrderstatus().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            distancebetweencustomer_vendortext_widget.setText(modal_orderDetails_tracking_ratingDetails.getDeliverydistance().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            paymentTypetext_widget.setText(modal_orderDetails_tracking_ratingDetails.getPaymentmode().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderplacedtime_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getOrderplacedtime().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderConfirmedtime_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getOrderconfirmedtime().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }




        try{
            orderReadytime_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getOrderreadytime().toString());


        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderpickeduptime_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getOrderpickeduptime().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            orderDeliveredtime_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getOrderdeliverytime().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            slotDatetext_widget.setText(modal_orderDetails_tracking_ratingDetails.getSlotdate().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            slotNametext_widget.setText(modal_orderDetails_tracking_ratingDetails.getSlotname().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            slotTime_Range_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getSlottimerange().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            notestext_widget.setText(modal_orderDetails_tracking_ratingDetails.getNotes().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            slotNametext_widget.setText(modal_orderDetails_tracking_ratingDetails.getSlotname().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{

            AddressLine1_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getUseraddress().toString());

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            String qualityrating = modal_orderDetails_tracking_ratingDetails.getQualityrating().toString();
            qualityratingBar.setRating(Float.parseFloat(qualityrating));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            String deliveryrating = modal_orderDetails_tracking_ratingDetails.getDeliveryrating().toString();
            deliveryratingBar.setRating(Float.parseFloat(deliveryrating));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            feedBackText_widget.setText(modal_orderDetails_tracking_ratingDetails.getFeedback().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }




        try{
            String deliverypartnerName  = modal_orderDetails_tracking_ratingDetails.getDeliveryusername().toString();
            if(!deliverypartnerName.equals("null")){
                deliveryPartner_name_widget.setText(deliverypartnerName);

            }
            else{
                deliveryPartner_name_widget.setText("Not Assigned");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            String deliverypartnerMobileNo  = modal_orderDetails_tracking_ratingDetails.getDeliveryusermobileno().toString();
            if(!deliverypartnerMobileNo.equals("null")){
                deliveryPartner_mobileNo_widget.setText(deliverypartnerMobileNo);

            }
            else{
                deliveryPartner_mobileNo_widget.setText("Not Assigned");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            String deliverytype  = modal_orderDetails_tracking_ratingDetails.getDeliverytype().toString();
            if(!deliverytype.equals("null")){
                delivery_type_widget.setText(deliverytype);

            }
            else{
                deliveryPartner_name_widget.setText("  -  ");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            String itemDespString = modal_orderDetails_tracking_ratingDetails.getItemdesp_String();
            JSONArray itemdesp = new JSONArray(itemDespString);


        for (int i = 0; i < itemdesp.length(); i++) {
            ////Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

            JSONObject itemdespjson = itemdesp.getJSONObject(i);
            try {
                if (itemdespjson.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = itemdespjson.getJSONObject("marinadeitemdesp");
                    if (itemnamefromOrderDetails.length() > 0) {
                        itemnamefromOrderDetails = itemnamefromOrderDetails + " , ";

                    } else {
                        itemnamefromOrderDetails = String.valueOf(marinadesObject.get("itemname"));
                    }
                    modal_orderDetails_tracking_ratingDetails.itemname = itemnamefromOrderDetails;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                if (itemnamefromOrderDetails.length() > 0) {
                    itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf(itemdespjson.get("itemname"));

                } else {
                    itemnamefromOrderDetails = String.valueOf(itemdespjson.get("itemname"));
                }
                modal_orderDetails_tracking_ratingDetails.itemname = itemnamefromOrderDetails;
            } catch (Exception e) {
                if (itemnamefromOrderDetails.length() > 0) {
                    itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf("");

                } else {
                    itemnamefromOrderDetails = String.valueOf((""));
                }
                modal_orderDetails_tracking_ratingDetails.itemname = itemnamefromOrderDetails;
                e.printStackTrace();
            }


        }



            OrderdItems_desp.clear();
            try {
                try {
                    JSONArray jsonArray;
                    try {
                        jsonArray = new JSONArray(modal_orderDetails_tracking_ratingDetails.getItemdesp_String());
                    }
                    catch (Exception e ){
                        jsonArray = modal_orderDetails_tracking_ratingDetails.getItemdesp();
                        e.printStackTrace();
                    }

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
                            manageOrders_pojo_class.itemName = "Grill House "+String.valueOf(json.get("itemname"));

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
                            // marinades_manageOrders_pojo_class.itemName=marinadesObject.getString("itemname");
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

                try {
                    ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
                    screenInches = screenSizeOfTheDevice.getDisplaySize(OrderAndRatingDetailsScreen .this);
                 //   Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
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
                add_amount_ForBillDetails(OrderdItems_desp);
                Adapter_Mobile_orderDetails_itemDesp_listview1 adapter_forOrderDetails_listview = new Adapter_Mobile_orderDetails_itemDesp_listview1(OrderAndRatingDetailsScreen.this, OrderdItems_desp);
                itemDesp_listview.setAdapter(adapter_forOrderDetails_listview);
                Helper.getListViewSize(itemDesp_listview, screenInches,0);




            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            showProgressBar(false);

        /*    if(isOrderDetailsCalled && isRatingDetailsCalled && isTrackingCalled){
                showProgressBar(false);

            }

         */
        }
        catch (Exception e){
            e.printStackTrace();
        }





        try{

            //AddressLine1_textwidget.setText(modal_orderDetails_tracking_ratingDetails.getUseraddress().toString());
            // googleAddress_textwidget
        }
        catch (Exception e){

            e.printStackTrace();
        }

    }

    private void changeObjectDataInRatingFirstScreenArray(String orderid, Modal_OrderDetails_Tracking_RatingDetails modal_orderDetails_tracking_ratingDetails) {

        for(int i=0;i<RaisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.size();i++){
            try {
            Modal_RaisedTicketsRatingDetails modal_raisedTicketsRatingDetails = RaisedTicketDetailsForRating.raisedTicketsRatingDetailsArray.get(i);

            try {
                String OrderidFromRatingScreen =  modal_raisedTicketsRatingDetails.getOrderid().toString();
                if(OrderidFromRatingScreen.equals(orderid)){
                    modal_raisedTicketsRatingDetails.setOrderDetailsScreenOpened(true);
                    modal_raisedTicketsRatingDetails.setModal_orderDetails_tracking_ratingDetails(modal_orderDetails_tracking_ratingDetails);
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

       // int new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);
        double new_totalAmount_withGst = Double.parseDouble(decimalFormat.format(new_to_pay_Amount));

        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst));
        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }
/*
    private void add_amount_ForBillDetails(List<Modal_ManageOrders_Pojo_Class> orderdItems_desp) {
    }

 */

    private void getOrderTrackingUsingOrderid(String orderid) {
        showProgressBar(true);



        String Api_toGetOrderTrackingDetailsUsingOrderid = "";

        if(orderid.length()>1 ) {
            if (orderdetailsnewschema) {
                Api_toGetOrderTrackingDetailsUsingOrderid = Constants.api_GetVendorTrackingDetailsUsingOrderid_vendorkey + "?orderid=" + orderid + "&vendorkey=" + vendorKey;
            } else {
                Api_toGetOrderTrackingDetailsUsingOrderid = Constants.api_GetTrackingOrderDetails_orderid + orderid;
            }
        }
        else{
            Toast.makeText(OrderAndRatingDetailsScreen.this, "There is No orderid " , Toast.LENGTH_SHORT).show();

        }

        if(!Api_toGetOrderTrackingDetailsUsingOrderid.equals("")) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_toGetOrderTrackingDetailsUsingOrderid, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            try {
                                Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                                try {
                                    // Toast.makeText(Edit_Or_CancelOrder_OrderDetails_Screen.this, "in response" , Toast.LENGTH_LONG).show();

                                    JSONArray JArray = response.getJSONArray("content");
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                    int i1 = 0;
                                    int arrayLength = JArray.length();
                                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                                    for (; i1 < (arrayLength); i1++) {

                                        try {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            if (json.has("key")) {
                                                modal_orderDetails_tracking_ratingDetails.keyfromtrackingDetails = String.valueOf(json.get("key"));

                                            } else {

                                                modal_orderDetails_tracking_ratingDetails.keyfromtrackingDetails = "";
                                            }


                                            if (json.has("orderstatus")) {
                                                modal_orderDetails_tracking_ratingDetails.orderstatus = String.valueOf(json.get("orderstatus"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.orderstatus = "";
                                            }


                                            if (json.has("orderdeliverytime")) {
                                                modal_orderDetails_tracking_ratingDetails.orderdeliverytime = String.valueOf(json.get("orderdeliverytime"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.orderdeliverytime = "";
                                            }
                                            if (json.has("useraddresskey")) {
                                                modal_orderDetails_tracking_ratingDetails.useraddresskey = String.valueOf(json.get("useraddresskey"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.useraddresskey = "";
                                            }


                                            if (json.has("orderreadytime")) {
                                                modal_orderDetails_tracking_ratingDetails.orderreadytime = String.valueOf(json.get("orderreadytime"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.orderreadytime = "";
                                            }


                                            if (json.has("orderpickeduptime")) {
                                                modal_orderDetails_tracking_ratingDetails.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.orderpickeduptime = "";
                                            }


                                            if (json.has("orderconfirmedtime")) {
                                                modal_orderDetails_tracking_ratingDetails.orderconfirmedtime = String.valueOf(json.get("orderconfirmedtime"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.orderconfirmedtime = "";
                                            }


                                            if (json.has("useraddresslat")) {
                                                modal_orderDetails_tracking_ratingDetails.useraddresslat = String.valueOf(json.get("useraddresslat"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.useraddresslat = "";
                                            }


                                            if (json.has("useraddresslong")) {
                                                modal_orderDetails_tracking_ratingDetails.useraddresslon = String.valueOf(json.get("useraddresslong"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.useraddresslon = "";
                                            }


                                            try {


                                                if (json.has("deliverydistanceinkm")) {

                                                    String deliverydistance = String.valueOf(json.get("deliverydistanceinkm"));
                                                    if (!deliverydistance.equals(null) && (!deliverydistance.equals("null"))) {
                                                        modal_orderDetails_tracking_ratingDetails.deliverydistance = String.valueOf(json.get("deliverydistanceinkm"));

                                                    } else {
                                                        modal_orderDetails_tracking_ratingDetails.deliverydistance = "0";

                                                    }
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.deliverydistance = "0";
                                                }


                                            } catch (Exception E) {
                                                modal_orderDetails_tracking_ratingDetails.deliverydistance = "0";
                                                E.printStackTrace();
                                            }


                                            if (json.has("deliveryusername")) {
                                                modal_orderDetails_tracking_ratingDetails.deliveryusername = String.valueOf(json.get("deliveryusername"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.deliveryusername = "";
                                            }
                                            if (json.has("deliveryuserkey")) {
                                                modal_orderDetails_tracking_ratingDetails.deliveryuserkey = String.valueOf(json.get("deliveryuserkey"));
                                                ;

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.deliveryuserkey = "";
                                            }
                                            if (json.has("deliveryusermobileno")) {
                                                modal_orderDetails_tracking_ratingDetails.deliveryusermobileno = String.valueOf(json.get("deliveryusermobileno"));

                                            } else {
                                                modal_orderDetails_tracking_ratingDetails.deliveryusermobileno = "";
                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();


                                        }


                                        if (arrayLength - 1 == i1) {
                                            isTrackingCalled = true;
                                            getRatingDetailsUsingOrderid(orderid);

                                            //      displaydatainScreen(modal_orderDetails_tracking_ratingDetails,"FromTracking");
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
                    try {
                        Toast.makeText(OrderAndRatingDetailsScreen.this, "There is no Credited Orders ", Toast.LENGTH_LONG).show();


                        error.printStackTrace();


                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }) {
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
            Volley.newRequestQueue(OrderAndRatingDetailsScreen.this).add(jsonObjectRequest);

        }
        else{

            Toast.makeText(OrderAndRatingDetailsScreen.this, "There is No Apiii " , Toast.LENGTH_SHORT).show();

        }

    }

    private void getOrderDetailsUsingOrderid(String orderid) {

        showProgressBar(true);


        String Api_toCallOrderDetailsUsingOrderid = "";
        if(orderdetailsnewschema){
            if(orderid.length()>1 ){
                Api_toCallOrderDetailsUsingOrderid = Constants.api_GetVendorOrderDetailsUsingOrderid_vendorkey+ "?vendorkey="+vendorKey+"&orderid="+orderid;
            }
            else{
                Toast.makeText(OrderAndRatingDetailsScreen.this, "orderid :"+orderid+" , vendorkey: "+vendorKey , Toast.LENGTH_SHORT).show();
            }


        }
        else{
            Api_toCallOrderDetailsUsingOrderid = Constants.api_GetOrderDetailsusingOrderid+orderid;

        }


        if(!Api_toCallOrderDetailsUsingOrderid.equals("")) {


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_toCallOrderDetailsUsingOrderid , null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            try {
                                Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                                try {

                                    String ordertype = "#";

                                    //converting jsonSTRING into array
                                    JSONArray JArray = response.getJSONArray("content");
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                    int i1 = 0;
                                    int arrayLength = JArray.length();
                                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                    if (arrayLength > 1) {
                                        Toast.makeText(OrderAndRatingDetailsScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();

                                    }

                                    for (; i1 < (arrayLength); i1++) {

                                        try {

                                            JSONObject json = JArray.getJSONObject(i1);

                                            try {
                                                if (json.has("coupondiscount")) {
                                                    modal_orderDetails_tracking_ratingDetails.coupondiscount = json.getString("coupondiscount");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.coupondiscount = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.coupondiscount = "";
                                            }


                                            try {
                                                if (json.has("couponkey")) {
                                                    modal_orderDetails_tracking_ratingDetails.couponkey = json.getString("couponkey");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.couponkey = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.couponkey = "";
                                            }


                                            try {
                                                if (json.has("deliveryamount")) {
                                                    modal_orderDetails_tracking_ratingDetails.deliveryamount = json.getString("deliveryamount");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.deliveryamount = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.deliveryamount = "";
                                            }


                                            try {
                                                if (json.has("deliverytype")) {
                                                    modal_orderDetails_tracking_ratingDetails.deliverytype = json.getString("deliverytype");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.deliverytype = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.deliverytype = "";
                                            }


                                            try {
                                                if (json.has("gstamount")) {
                                                    modal_orderDetails_tracking_ratingDetails.gstamount = json.getString("gstamount");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.gstamount = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.gstamount = "";
                                            }


                                            try {
                                                if (json.has("isdeliveryslotfree")) {
                                                    modal_orderDetails_tracking_ratingDetails.isdeliveryslotfree = json.getString("isdeliveryslotfree");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.isdeliveryslotfree = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.isdeliveryslotfree = "";
                                            }


                                            try {
                                                if (json.has("key")) {
                                                    modal_orderDetails_tracking_ratingDetails.key = json.getString("key");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.key = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.key = "";
                                            }


                                            try {
                                                if (json.has("merchantorderid")) {
                                                    modal_orderDetails_tracking_ratingDetails.merchantorderid = json.getString("merchantorderid");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.merchantorderid = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.merchantorderid = "";
                                            }


                                            try {
                                                if (json.has("notes")) {
                                                    modal_orderDetails_tracking_ratingDetails.notes = json.getString("notes");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.notes = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.notes = "";
                                            }


                                            try {
                                                if (json.has("orderid")) {
                                                    modal_orderDetails_tracking_ratingDetails.orderid = json.getString("orderid");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.orderid = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.orderid = "";
                                            }


                                            try {
                                                if (json.has("orderplaceddate")) {
                                                    modal_orderDetails_tracking_ratingDetails.orderplaceddate = json.getString("orderplaceddate");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.orderplaceddate = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.orderplaceddate = "";
                                            }


                                            try {
                                                if (json.has("orderplacedtime")) {
                                                    modal_orderDetails_tracking_ratingDetails.orderplacedtime = json.getString("orderplacedtime");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.orderplacedtime = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.orderplacedtime = "";
                                            }


                                            try {
                                                if (json.has("ordertype")) {
                                                    modal_orderDetails_tracking_ratingDetails.ordertype = json.getString("ordertype");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.ordertype = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.ordertype = "";
                                            }


                                            try {
                                                if (json.has("payableamount")) {
                                                    modal_orderDetails_tracking_ratingDetails.payableamount = json.getString("payableamount");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.payableamount = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.payableamount = "";
                                            }


                                            try {
                                                if (json.has("paymentmode")) {
                                                    modal_orderDetails_tracking_ratingDetails.paymentmode = json.getString("paymentmode");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.paymentmode = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.paymentmode = "";
                                            }


                                            try {
                                                if (json.has("slotdate")) {
                                                    modal_orderDetails_tracking_ratingDetails.slotdate = json.getString("slotdate");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.slotdate = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.slotdate = "";
                                            }

///////////////////////////////////////////

                                            try {
                                                if (json.has("slottimerange")) {
                                                    modal_orderDetails_tracking_ratingDetails.slottimerange = json.getString("slottimerange");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.slottimerange = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.slottimerange = "";
                                            }


                                            try {
                                                if (json.has("tokenno")) {
                                                    modal_orderDetails_tracking_ratingDetails.tokenno = json.getString("tokenno");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.tokenno = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.tokenno = "";
                                            }


                                            try {
                                                if (json.has("useraddress")) {
                                                    modal_orderDetails_tracking_ratingDetails.useraddress = json.getString("useraddress");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.useraddress = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.useraddress = "";
                                            }


                                            try {
                                                if (json.has("userkey")) {
                                                    modal_orderDetails_tracking_ratingDetails.userkey = json.getString("userkey");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.userkey = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.userkey = "";
                                            }


                                            try {
                                                if (json.has("usermobile")) {
                                                    modal_orderDetails_tracking_ratingDetails.usermobile = json.getString("usermobile");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.usermobile = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.usermobile = "";
                                            }


                                            try {
                                                if (json.has("vendorkey")) {
                                                    modal_orderDetails_tracking_ratingDetails.vendorkey = json.getString("vendorkey");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.vendorkey = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.vendorkey = "";
                                            }


                                            try {
                                                if (json.has("vendorname")) {
                                                    modal_orderDetails_tracking_ratingDetails.vendorname = json.getString("vendorname");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.vendorname = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.vendorname = "";
                                            }


                                            try {
                                                if (json.has("slotname")) {
                                                    modal_orderDetails_tracking_ratingDetails.slotname = json.getString("slotname");
                                                } else {
                                                    modal_orderDetails_tracking_ratingDetails.slotname = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_tracking_ratingDetails.slotname = "";
                                            }


                                            try {
                                                if (json.has("itemdesp")) {
                                                    JSONArray itemdesp = json.getJSONArray("itemdesp");

                                                    modal_orderDetails_tracking_ratingDetails.itemdesp = itemdesp;
                                                    modal_orderDetails_tracking_ratingDetails.itemdesp_String = itemdesp.toString();


                                                } else {

                                                }
                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }


                                        } catch (JSONException e) {


                                            e.printStackTrace();
                                        }


                                        if (arrayLength - 1 == i1) {

                                            isOrderDetailsCalled = true;
                                            getOrderTrackingUsingOrderid(orderid);
                                            //displaydatainScreen(modal_orderDetails_tracking_ratingDetails, "FromOrderDetails");
                                        }
                                    }


                                } catch (JSONException e) {


                                    e.printStackTrace();


                                }


                            } catch (Exception e) {


                                e.printStackTrace();


                            }


                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    try {
                        Toast.makeText(OrderAndRatingDetailsScreen.this, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();


                        Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                        Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                        error.printStackTrace();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }) {
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
            Volley.newRequestQueue(OrderAndRatingDetailsScreen.this).add(jsonObjectRequest);

        }
        else{
            Toast.makeText(OrderAndRatingDetailsScreen.this, "There is No Api " , Toast.LENGTH_SHORT).show();

        }









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
            } else {
                Toast.makeText(OrderAndRatingDetailsScreen.this, resultData.getString(Constants.TotalLocationResult), Toast.LENGTH_LONG).show();
                googleAddress_textwidget.setText("Can't get Address");

            }
        }
    }
}