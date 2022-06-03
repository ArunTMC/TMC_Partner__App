package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Pos_OrderDetailsScreen;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_DatewiseRatingReport extends ArrayAdapter<Modal_RatingOrderDetails> {
    Context mContext;
    Modal_OrderDetails orderTrackingDetailstable_Object;
    Modal_ManageOrders_Pojo_Class totalOrderDetailsObject;
    Modal_OrderDetails orderDetailsNewtable_Object;
    List<Modal_RatingOrderDetails> ratingList;
    String vendorKey,createdTime,orderId,mobileNumber,itemName,itemQuantity,feedBack,qualityrating,deliveryrating;
    DatewiseRatingreport_SecondScreen datewiseRatingreport_secondScreen;
    public static BottomSheetDialog bottomSheetDialog;
    List<Modal_OrderDetails>orderDetails = new ArrayList<>();

    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;



    public Adapter_DatewiseRatingReport(Context context, List<Modal_RatingOrderDetails> ratingList, DatewiseRatingreport_SecondScreen DatewiseRatingreport_secondScreen) {
        super(context, R.layout.datewiseratingreportlistitem);

        datewiseRatingreport_secondScreen = DatewiseRatingreport_secondScreen;
        this.mContext = context;
        this.ratingList = ratingList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup view) {
        @SuppressLint("ViewHolder")
        final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.datewiseratingreportlistitem, (ViewGroup) view, false);
        final TextView createdTimetextview = listViewItem.findViewById(R.id.createdTimetextview);
        final TextView orderIdtextview = listViewItem.findViewById(R.id.orderIdtextview);
        final TextView mobileNumbertextview = listViewItem.findViewById(R.id.mobileNumbertextview);
        final TextView itemDetailsTextview = listViewItem.findViewById(R.id.itemDetailsTextview);
        final TextView feedBackTextview = listViewItem.findViewById(R.id.feedBackTextview);
        final RatingBar qualityratingBar = listViewItem.findViewById(R.id.qualityratingBar);
        final RatingBar deliveryratingBar = listViewItem.findViewById(R.id.deliveryratingBar);
        final CardView ratingCardView = listViewItem.findViewById(R.id.ratingCardView);


        final Button addEnquiryStatusButton = listViewItem.findViewById(R.id.addEnquiryStatusButton);

        try {
            SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", ""));

            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
          //  orderdetailsnewschema = true;
                  }
        catch (Exception e){
            e.printStackTrace();
        }

        orderDetails.clear();

        Modal_RatingOrderDetails modal_ratingOrderDetails = ratingList.get(position);
        deliveryrating = modal_ratingOrderDetails.getDeliveryrating();
        createdTime = modal_ratingOrderDetails.getCreatedtime();
        mobileNumber = modal_ratingOrderDetails.getUsermobileno();
        feedBack = modal_ratingOrderDetails.getFeedback();
        qualityrating = modal_ratingOrderDetails.getQualityrating();
        deliveryrating = modal_ratingOrderDetails.getDeliveryrating();
        itemName = modal_ratingOrderDetails.getItemname();
        orderId = modal_ratingOrderDetails.getOrderid();

        createdTimetextview.setText(String.valueOf(createdTime));
        orderIdtextview.setText(String.valueOf(orderId));
        mobileNumbertextview.setText(String.valueOf(mobileNumber));
        feedBackTextview.setText(String.valueOf(feedBack));
        itemDetailsTextview.setText(String.valueOf(itemName));


        qualityratingBar.setRating(Float.parseFloat(qualityrating));
        deliveryratingBar.setRating(Float.parseFloat(deliveryrating));

        ratingCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderId.length()>0) {
                    orderId = ratingList.get(position).getOrderid();

                    datewiseRatingreport_secondScreen.showProgressBar(true);
                    if(orderdetailsnewschema){

                        callVendorOrderDetailsSeviceAndInitCallBack(orderId,vendorKey);


                    }
                    else{
                        orderDetailsNewtable_Object = getOrderDetailsFromOrderDetailsTable(orderId);

                    }


                }
                else{
                    
                }
            }
        });



        addEnquiryStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(mContext);
                bottomSheetDialog.setContentView(R.layout.add_enquiry_status);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                Button apply_discount_buttonWidget = bottomSheetDialog.findViewById(R.id.apply_discount_buttonWidget);
                final TextView orderidtext_bottomsheet =  bottomSheetDialog.findViewById(R.id.orderidtext_bottomsheet);
                final TextView MobileNo_text_bottomsheet =  bottomSheetDialog.findViewById(R.id.MobileNo_text_bottomsheet);
                final TextView itemName_text_bottomsheet =  bottomSheetDialog.findViewById(R.id.itemName_text_bottomsheet);
                final Spinner enquiryStatusSpinner_bottomsheet =  bottomSheetDialog.findViewById(R.id.enquiryStatusSpinner_bottomsheet);

                final EditText editTextTextMultiLine_bottomsheet =  bottomSheetDialog.findViewById(R.id.editTextTextMultiLine_bottomsheet);


                orderidtext_bottomsheet.setText(ratingList.get(position).getOrderid());
                MobileNo_text_bottomsheet.setText(ratingList.get(position).getUsermobileno());
                itemName_text_bottomsheet.setText(ratingList.get(position).getItemname());


                String[] ordertype=mContext.getResources().getStringArray(R.array.RatingEnquiryStatus);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ordertype);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                Objects.requireNonNull(enquiryStatusSpinner_bottomsheet).setAdapter(arrayAdapter);



                bottomSheetDialog.show();
            }
        });




        return listViewItem;
    }

    private Modal_ManageOrders_Pojo_Class combineBothObjects(Modal_OrderDetails orderDetailsNewtable_object, Modal_OrderDetails orderTrackingDetailstable_object) {
        Modal_ManageOrders_Pojo_Class totalModalOrderDetails = new Modal_ManageOrders_Pojo_Class();
    totalModalOrderDetails.coupondiscamount = orderDetailsNewtable_object.getCoupondiscount();
        totalModalOrderDetails.couponkey = orderDetailsNewtable_object.getCouponkey();
        totalModalOrderDetails.deliveryamount = orderDetailsNewtable_object.getDeliveryamount();
        totalModalOrderDetails.deliverytype = orderDetailsNewtable_object.getDeliverytype();
        totalModalOrderDetails.GstAmount = orderDetailsNewtable_object.getGstamount();
        totalModalOrderDetails.itemdesp = orderDetailsNewtable_object.getItemdesp();
        JSONArray array = orderDetailsNewtable_object.getItemdesp();
        //Log.i("tag","array.length()"+ array.length());
        String b = array.toString();
        totalModalOrderDetails.setItemdesp_string(b);
        totalModalOrderDetails.orderdetailskey = orderDetailsNewtable_object.getOrderdetailskey();
        totalModalOrderDetails.notes = orderDetailsNewtable_object.getNotes();
        totalModalOrderDetails.orderid = orderDetailsNewtable_object.getOrderid();
        totalModalOrderDetails.orderplaceddate = orderDetailsNewtable_object.getOrderplaceddate();
        totalModalOrderDetails.orderplacedtime = orderDetailsNewtable_object.getOrderplacedtime();
        totalModalOrderDetails.orderType = orderDetailsNewtable_object.getOrdertype();
        totalModalOrderDetails.ordertype = orderDetailsNewtable_object.getOrdertype();
        totalModalOrderDetails.paymentmode = orderDetailsNewtable_object.getPaymentmode();

        totalModalOrderDetails.payableamount = orderDetailsNewtable_object.getPayableamount();
        totalModalOrderDetails.slotdate = orderDetailsNewtable_object.getSlotdate();
        totalModalOrderDetails.slotname = orderDetailsNewtable_object.getSlotname();
        totalModalOrderDetails.slottimerange = orderDetailsNewtable_object.getSlottimerange();
        totalModalOrderDetails.tokenno = orderDetailsNewtable_object.getTokenno();
        totalModalOrderDetails.useraddress = orderDetailsNewtable_object.getUseraddress();
        totalModalOrderDetails.userkey = orderDetailsNewtable_object.getUserkey();
        totalModalOrderDetails.usermobile = orderDetailsNewtable_object.getUsermobile();
        totalModalOrderDetails.vendorkey = orderDetailsNewtable_object.getVendorkey();

        totalModalOrderDetails.deliverydistance = orderTrackingDetailstable_object.getDeliverydistanceinkm();
        totalModalOrderDetails.deliveryPartnerKey = orderTrackingDetailstable_object.getDeliveryuserkey();
       // totalModalOrderDetails. = orderDetailsNewtable_object.getDeliveryuserlat();
       // totalModalOrderDetails.deliveryuserlong = orderDetailsNewtable_object.getDeliveryuserlong();
        totalModalOrderDetails.deliveryPartnerMobileNo = orderTrackingDetailstable_object.getDeliveryusermobileno();
        totalModalOrderDetails.deliveryPartnerName = orderTrackingDetailstable_object.getDeliveryusername();
        totalModalOrderDetails.keyfromtrackingDetails = orderTrackingDetailstable_object.getOrdertrackingdetailskey();

        totalModalOrderDetails.orderconfirmedtime = orderTrackingDetailstable_object.getOrderconfirmedtime();

        totalModalOrderDetails.orderdeliveredtime = orderTrackingDetailstable_object.getOrderdeliverytime();

        totalModalOrderDetails.orderpickeduptime = orderTrackingDetailstable_object.getOrderpickeduptime();
        totalModalOrderDetails.orderplacedtime = orderTrackingDetailstable_object.getOrderplacedtime();
        totalModalOrderDetails.orderreadytime = orderTrackingDetailstable_object.getOrderreadytime();
        totalModalOrderDetails.orderstatus = orderTrackingDetailstable_object.getOrderstatus();
        totalModalOrderDetails.useraddresslat = orderTrackingDetailstable_object.getUseraddresslat();
        totalModalOrderDetails.useraddresslon = orderTrackingDetailstable_object.getUseraddresslong();




        Intent intent = new Intent (mContext, MobileScreen_OrderDetails1.class);
        Bundle bundle = new Bundle();
        bundle.putString("From","DatewiseRatingReport");
        bundle.putParcelable("data", totalModalOrderDetails);
        intent.putExtras(bundle);

        mContext.startActivity(intent);
            datewiseRatingreport_secondScreen.showProgressBar(false);
        return totalModalOrderDetails;
    }

    private Modal_OrderDetails getOrderTrackingDetailsFromOrderDetailsTable(String orderId, Modal_OrderDetails modal_orderDetailsneworderdetails) {
        Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_orderid+orderId ,null,
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
                                    Toast.makeText(mContext, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();

                                }

                                for(;i1<(arrayLength);i1++) {
                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);

                                        try{
                                            if(json.has("deliverydistanceinkm")){
                                                modal_orderDetails.deliverydistanceinkm = json.getString("deliverydistanceinkm");
                                            }
                                            else{
                                                modal_orderDetails.deliverydistanceinkm  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliverydistanceinkm  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("deliveryuserkey")){
                                                modal_orderDetails.deliveryuserkey = json.getString("deliveryuserkey");
                                            }
                                            else{
                                                modal_orderDetails.deliveryuserkey  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliveryuserkey  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("deliveryuserlat")){
                                                modal_orderDetails.deliveryuserlat = json.getString("deliveryuserlat");
                                            }
                                            else{
                                                modal_orderDetails.deliveryuserlat  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliveryuserlat  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("deliveryuserlong")){
                                                modal_orderDetails.deliveryuserlong = json.getString("deliveryuserlong");
                                            }
                                            else{
                                                modal_orderDetails.deliveryuserlong  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliveryuserlong  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("deliveryusermobileno")){
                                                modal_orderDetails.deliveryusermobileno = json.getString("deliveryusermobileno");
                                            }
                                            else{
                                                modal_orderDetails.deliveryusermobileno  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliveryusermobileno  ="-";
                                            e.printStackTrace();
                                        }




                                        try{
                                            if(json.has("deliveryusername")){
                                                modal_orderDetails.deliveryusername = json.getString("deliveryusername");
                                            }
                                            else{
                                                modal_orderDetails.deliveryusername  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliveryusername  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("key")){
                                                modal_orderDetails.ordertrackingdetailskey = json.getString("key");
                                            }
                                            else{
                                                modal_orderDetails.ordertrackingdetailskey  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.ordertrackingdetailskey  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderconfirmedtime")){
                                                modal_orderDetails. orderconfirmedtime= json.getString("orderconfirmedtime");
                                            }
                                            else{
                                                modal_orderDetails.orderconfirmedtime  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderconfirmedtime  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderdeliverytime")){
                                                modal_orderDetails.orderdeliverytime = json.getString("orderdeliverytime");
                                            }
                                            else{
                                                modal_orderDetails.orderdeliverytime  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderdeliverytime  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderid")){
                                                modal_orderDetails.orderid = json.getString("orderid");
                                            }
                                            else{
                                                modal_orderDetails. orderid ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderid  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderpickeduptime")){
                                                modal_orderDetails.orderpickeduptime = json.getString("orderpickeduptime");
                                            }
                                            else{
                                                modal_orderDetails.orderpickeduptime  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderpickeduptime  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderplacedtime")){
                                                modal_orderDetails.orderplacedtime = json.getString("orderplacedtime");
                                            }
                                            else{
                                                modal_orderDetails. orderplacedtime ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. orderplacedtime ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderreadytime")){
                                                modal_orderDetails.orderreadytime = json.getString("orderreadytime");
                                            }
                                            else{
                                                modal_orderDetails. orderreadytime ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. orderreadytime ="-";
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(json.has("orderstatus")){
                                                modal_orderDetails.orderstatus = json.getString("orderstatus");
                                            }
                                            else{
                                                modal_orderDetails. orderstatus ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. orderstatus ="-";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("slotdate")){
                                                modal_orderDetails.slotdate = json.getString("slotdate");
                                            }
                                            else{
                                                modal_orderDetails. slotdate ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. slotdate ="-";
                                            e.printStackTrace();
                                        }




                                        try{
                                            if(json.has("useraddresskey")){
                                                modal_orderDetails.useraddresskey = json.getString("useraddresskey");
                                            }
                                            else{
                                                modal_orderDetails. useraddresskey ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. useraddresskey ="-";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("useraddresslat")){
                                                modal_orderDetails.useraddresslat = json.getString("useraddresslat");
                                            }
                                            else{
                                                modal_orderDetails. useraddresslat ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. useraddresslat ="-";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("useraddresslong")){
                                                modal_orderDetails.useraddresslong = json.getString("useraddresslong");
                                            }
                                            else{
                                                modal_orderDetails. useraddresslong ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. useraddresslong ="-";
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(json.has("usermobileno")){
                                                modal_orderDetails.usermobile = json.getString("usermobileno");
                                            }
                                            else{
                                                modal_orderDetails. usermobile="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. usermobile ="-";
                                            e.printStackTrace();
                                        }




                                        try{
                                            if(json.has("vendorkey")){
                                                modal_orderDetails.vendorkey = json.getString("vendorkey");
                                            }
                                            else{
                                                modal_orderDetails. vendorkey ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. vendorkey="-";
                                            e.printStackTrace();
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();



                        }


                        totalOrderDetailsObject = combineBothObjects(modal_orderDetailsneworderdetails,modal_orderDetails);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
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
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





        return modal_orderDetails;


    }
    private void callVendorOrderDetailsSeviceAndInitCallBack(String orderId ,String vendorKey) {
        if(isVendorOrdersTableServiceCalled){
            datewiseRatingreport_secondScreen.showProgressBar(false);

            return;
        }
        isVendorOrdersTableServiceCalled = true;
        mResultCallback = new VendorOrdersTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {
                 JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content",orderslist_fromResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isVendorOrdersTableServiceCalled = false;


                processTheArrayandCallTheIntent(orderslist_fromResponse);
                //convertingJsonStringintoArray(orderStatus,mobile_jsonString);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                datewiseRatingreport_secondScreen.showProgressBar(false);

                isVendorOrdersTableServiceCalled = false;

            }
        };
        mVolleyService = new VendorOrdersTableService(mResultCallback,mContext);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingOrderid_vendorkey + "?vendorkey="+vendorKey+"&orderid="+orderId;
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingOrderid_vendorkey + "?vendorkey="+vendorKey+"&orderid="+orderId;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }

    private void processTheArrayandCallTheIntent(List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {

        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_1 = new Modal_ManageOrders_Pojo_Class();
        if(orderslist_fromResponse.size()>0) {
            for (Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class : orderslist_fromResponse) {
                modal_manageOrders_pojo_class_1 = modal_manageOrders_pojo_class;
            }


            Intent intent = new Intent(mContext, MobileScreen_OrderDetails1.class);
            Bundle bundle = new Bundle();
            bundle.putString("From", "DatewiseRatingReport");
            bundle.putParcelable("data", modal_manageOrders_pojo_class_1);
            intent.putExtras(bundle);

            mContext.startActivity(intent);
        }
        else{
            Toast.makeText(mContext, "There is no order for this orderid ", Toast.LENGTH_SHORT).show();
        }
        datewiseRatingreport_secondScreen.showProgressBar(false);



    }

    private Modal_OrderDetails getOrderDetailsFromOrderDetailsTable(String orderId) {
        Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsusingOrderid+orderId ,null,
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
                                    Toast.makeText(mContext, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();

                                }

                                for(;i1<(arrayLength);i1++) {
                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);

                                        try{
                                            if(json.has("coupondiscount")){
                                                modal_orderDetails.coupondiscount = json.getString("coupondiscount");
                                            }
                                            else{
                                                modal_orderDetails.coupondiscount  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.coupondiscount  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("couponkey")){
                                                modal_orderDetails.couponkey = json.getString("couponkey");
                                            }
                                            else{
                                                modal_orderDetails.couponkey  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.couponkey  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("deliveryamount")){
                                                modal_orderDetails.deliveryamount = json.getString("deliveryamount");
                                            }
                                            else{
                                                modal_orderDetails.deliveryamount  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliveryamount  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("deliverytype")){
                                                modal_orderDetails.deliverytype = json.getString("deliverytype");
                                            }
                                            else{
                                                modal_orderDetails.deliverytype  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.deliverytype  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("gstamount")){
                                                modal_orderDetails.gstamount = json.getString("gstamount");
                                            }
                                            else{
                                                modal_orderDetails.gstamount  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.gstamount  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("itemdesp")){
                                                JSONArray itemdesp = json.getJSONArray("itemdesp");

                                                modal_orderDetails.itemdesp= itemdesp;
                                            }
                                            else{


                                            }
                                        }
                                        catch(Exception e ){
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("key")){
                                                modal_orderDetails.orderdetailskey = json.getString("key");
                                            }
                                            else{
                                                modal_orderDetails.orderdetailskey  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderdetailskey  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("notes")){
                                                modal_orderDetails.notes = json.getString("notes");
                                            }
                                            else{
                                                modal_orderDetails.notes  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.notes  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderid")){
                                                modal_orderDetails. orderid= json.getString("orderid");
                                            }
                                            else{
                                                modal_orderDetails.orderid  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderid  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderplaceddate")){
                                                modal_orderDetails.orderplaceddate = json.getString("orderplaceddate");
                                            }
                                            else{
                                                modal_orderDetails.orderplaceddate  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderplaceddate  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("orderplacedtime")){
                                                modal_orderDetails.orderplacedtime = json.getString("orderplacedtime");
                                            }
                                            else{
                                                modal_orderDetails. orderplacedtime ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.orderplacedtime  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("ordertype")){
                                                modal_orderDetails.ordertype = json.getString("ordertype");
                                            }
                                            else{
                                                modal_orderDetails.ordertype  ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails.ordertype  ="-";
                                            e.printStackTrace();
                                        }

                                        try{
                                            if(json.has("payableamount")){
                                                modal_orderDetails.payableamount = json.getString("payableamount");
                                            }
                                            else{
                                                modal_orderDetails. payableamount ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. payableamount ="-";
                                            e.printStackTrace();
                                        }



                                        try{

                                        if(json.has("paymentmode")){
                                            modal_orderDetails.paymentmode = json.getString("paymentmode");
                                        }
                                        else{
                                            modal_orderDetails. paymentmode ="-";
                                        }
                                    }
                                    catch(Exception e ){
                                        modal_orderDetails. paymentmode ="-";
                                        e.printStackTrace();
                                    }



                                    try{
                                            if(json.has("slotdate")){
                                                modal_orderDetails.slotdate = json.getString("slotdate");
                                            }
                                            else{
                                                modal_orderDetails. slotdate ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. slotdate ="-";
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(json.has("slotname")){
                                                modal_orderDetails.slotname = json.getString("slotname");
                                            }
                                            else{
                                                modal_orderDetails. slotname ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. slotname ="-";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("slottimerange")){
                                                modal_orderDetails.slottimerange = json.getString("slottimerange");
                                            }
                                            else{
                                                modal_orderDetails. slottimerange ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. slottimerange ="-";
                                            e.printStackTrace();
                                        }




                                        try{
                                            if(json.has("tokenno")){
                                                modal_orderDetails.tokenno = json.getString("tokenno");
                                            }
                                            else{
                                                modal_orderDetails. tokenno ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. tokenno ="-";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("useraddress")){
                                                modal_orderDetails.useraddress = json.getString("useraddress");
                                            }
                                            else{
                                                modal_orderDetails. useraddress ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. useraddress ="-";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("userkey")){
                                                modal_orderDetails.userkey = json.getString("userkey");
                                            }
                                            else{
                                                modal_orderDetails. userkey ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. userkey ="-";
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(json.has("useraddress")){
                                                modal_orderDetails.useraddress = json.getString("useraddress");
                                            }
                                            else{
                                                modal_orderDetails. useraddress ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. useraddress ="-";
                                            e.printStackTrace();
                                        }




                                        try{
                                            if(json.has("usermobile")){
                                                modal_orderDetails.usermobile = json.getString("usermobile");
                                            }
                                            else{
                                                modal_orderDetails. usermobile ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. usermobile ="-";
                                            e.printStackTrace();
                                        }



                                        try{
                                            if(json.has("vendorkey")){
                                                modal_orderDetails.vendorkey = json.getString("vendorkey");
                                            }
                                            else{
                                                modal_orderDetails. vendorkey ="-";
                                            }
                                        }
                                        catch(Exception e ){
                                            modal_orderDetails. vendorkey="-";
                                            e.printStackTrace();
                                        }



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();

                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();



                        }

                        orderTrackingDetailstable_Object = getOrderTrackingDetailsFromOrderDetailsTable(orderId,modal_orderDetails);


                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
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
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);







        return modal_orderDetails;
    }

    @Override
    public int getCount() {
        return ratingList.size();
    }

    @Nullable
    @Override
    public Modal_RatingOrderDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_RatingOrderDetails item) {
        return super.getPosition(item);
    }

}
