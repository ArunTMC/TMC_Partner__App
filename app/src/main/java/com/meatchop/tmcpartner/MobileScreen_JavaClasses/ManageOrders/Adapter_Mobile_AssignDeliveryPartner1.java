package com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.CustomerOrder_TrackingDetails.Update_CustomerOrderDetails_TrackingTableInterface;
import com.meatchop.tmcpartner.CustomerOrder_TrackingDetails.Update_CustomerOrderDetails_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Adapter_Mobile_SearchOrders_usingMobileNumber_ListView;
import com.meatchop.tmcpartner.Settings.Edit_Or_CancelOrder_OrderDetails_Screen;
import com.meatchop.tmcpartner.Settings.Edit_Or_CancelTheOrders;
import com.meatchop.tmcpartner.Settings.Phone_Orders_List;
import com.meatchop.tmcpartner.Settings.searchOrdersUsingMobileNumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_Mobile_AssignDeliveryPartner1 extends ArrayAdapter<AssignDeliveryPartner_PojoClass> {
        Context mContext;
        List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
        String OrderKey,IntentFrom,deliverypartnerName,orderid,customerMobileNo,vendorkey;

    boolean orderdetailsnewschema = false;
    Update_CustomerOrderDetails_TrackingTableInterface mResultCallback_UpdateCustomerOrderDetailsTableInterface;

/*
public Adapter_Mobile_AssignDeliveryPartner1(Context mContext, List<AssignDeliveryPartner_PojoClass> deliveryPartnerList, String orderKey, String intentFrom, String deliverypartnerName) {
        super(mContext, R.layout.mobile_screen_assigning_deliverypartner_listitem1, deliveryPartnerList);
        this.OrderKey=orderKey;
        this.mContext=mContext;
        this.deliveryPartnerList=deliveryPartnerList;
        this.IntentFrom=intentFrom;
        this.deliverypartnerName=deliverypartnerName;
        }


 */
    public Adapter_Mobile_AssignDeliveryPartner1(Context mContext, List<AssignDeliveryPartner_PojoClass> deliveryPartnerList, String orderkey, String intentFrom, String deliverypartnerName, String orderid, String customerMobileNo, String vendorkey) {
        super(mContext, R.layout.mobile_screen_assigning_deliverypartner_listitem1, deliveryPartnerList);
        this.OrderKey=orderkey;
        this.orderid=orderid;
        this.customerMobileNo=customerMobileNo;
        this.vendorkey=vendorkey;

        this.mContext=mContext;
        this.deliveryPartnerList=deliveryPartnerList;
        this.IntentFrom=intentFrom;
        this.deliverypartnerName=deliverypartnerName;

    }


    public int getCount() {
        return super.getCount();
        }

@Nullable
@Override
public AssignDeliveryPartner_PojoClass getItem(int position) {
        return super.getItem(position);
        }

@Override
public int getPosition(@Nullable AssignDeliveryPartner_PojoClass item) {
        return super.getPosition(item);
        }

public View getView(final int pos, View view, ViewGroup v) {
@SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.mobile_screen_assigning_deliverypartner_listitem1, (ViewGroup) view, false);
    SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
    if (IntentFrom.equals("MobileManageOrders")) {
        orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));

    }
    else{
        orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));

    }        //orderdetailsnewschema = true;
    //orderdetailsnewschema = true;



final TextView deliveryPartner_name_widget = listViewItem.findViewById(R.id.deliveryPartner_name_widget);
final TextView deliveryPartner_mobileNo_widget = listViewItem.findViewById(R.id.deliveryPartner_mobileNo_widget);
final TextView deliveryPartner_status = listViewItem.findViewById(R.id.deliveryPartner_status);
final Button assignPartner_widget = listViewItem.findViewById(R.id.assignPartner_widget);

final AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass =deliveryPartnerList.get(pos);
        deliveryPartner_mobileNo_widget.setText(assignDeliveryPartner_pojoClass.getDeliveryPartnerMobileNo());
        deliveryPartner_name_widget.setText(assignDeliveryPartner_pojoClass.getDeliveryPartnerName());
        deliveryPartner_status.setText(assignDeliveryPartner_pojoClass.getDeliveryPartnerStatus());
        String deliveryPartnerKey = assignDeliveryPartner_pojoClass.getDeliveryPartnerKey();
        String deliveryPartnerMobileNo = assignDeliveryPartner_pojoClass.getDeliveryPartnerMobileNo();
        String deliveryPartnerName = assignDeliveryPartner_pojoClass.getDeliveryPartnerName();

    if(deliverypartnerName.equals("null")){
            assignPartner_widget.setText("Assign");
        }
        else{
            if(deliveryPartnerName.equals(deliverypartnerName)){
                assignPartner_widget.setText("Assigned");
                assignPartner_widget.setBackgroundColor(Color.parseColor("#FF5F22"));
            }
            else{
                assignPartner_widget.setText("Assign");
            }
        }
      //  Log.i(Constants.TAG,"deliveryPartnerKey"+deliveryPartnerKey);
assignPartner_widget.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
    try {
        if (IntentFrom.equals("MobileManageOrders")) {
            try {
                Adapter_Mobile_ManageOrders_ListView1.bottomSheetDialog.dismiss();


                Mobile_ManageOrders1.loadingpanelmask.setVisibility(View.VISIBLE);
                Mobile_ManageOrders1.loadingPanel.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    try {
        if (IntentFrom.equals("AppOrdersList")) {

            try {

                Adapter_Mobile_SearchOrders_usingMobileNumber_ListView.bottomSheetDialog.dismiss();


                searchOrdersUsingMobileNumber.loadingpanelmask.setVisibility(View.VISIBLE);
                searchOrdersUsingMobileNumber.loadingPanel.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    try {
        if (IntentFrom.equals("PhoneOrdersList")) {

            try {

                Adapter_Mobile_SearchOrders_usingMobileNumber_ListView.bottomSheetDialog.dismiss();


                Phone_Orders_List.loadingpanelmask.setVisibility(View.VISIBLE);
                Phone_Orders_List.loadingPanel.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    try {
        if (IntentFrom.contains("orderdetails")) {
            try {


                MobileScreen_OrderDetails1.bottomSheetDialog.dismiss();

                MobileScreen_OrderDetails1.loadingpanelmask.setVisibility(View.VISIBLE);
                MobileScreen_OrderDetails1.loadingPanel.setVisibility(View.VISIBLE);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    } catch (Exception e) {
        e.printStackTrace();
    }



    try {
        if (IntentFrom.contains("EditOrders")) {
            try {


                Edit_Or_CancelOrder_OrderDetails_Screen.bottomSheetDialog.dismiss();

                Edit_Or_CancelOrder_OrderDetails_Screen.loadingpanelmask.setVisibility(View.VISIBLE);
                Edit_Or_CancelOrder_OrderDetails_Screen.loadingPanel.setVisibility(View.VISIBLE);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    } catch (Exception e) {
        e.printStackTrace();
    }




    JSONObject jsonObject = new JSONObject();
    String Api_toChangeOrderDetailsUsingOrderid = "";

    try {


        if(orderdetailsnewschema){
            if(orderid.length()>1 && vendorkey.length()>1 && customerMobileNo.length()>1){



                    try {
                        jsonObject.put("vendorkey", vendorkey);
                        jsonObject.put("orderid", orderid);
                        jsonObject.put("deliveryuserkey", deliveryPartnerKey);
                        jsonObject.put("deliveryusermobileno", deliveryPartnerMobileNo);
                        jsonObject.put("deliveryusername", deliveryPartnerName);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(Constants.TAG, "JSONOBJECT: " + e);

                    }



                Api_toChangeOrderDetailsUsingOrderid = Constants.api_UpdateVendorTrackingOrderDetails+ "?vendorkey="+vendorkey+"&orderid="+orderid;
                JSONObject customerDetails_JsonObject = new JSONObject();





                    try {
                        customerDetails_JsonObject.put("usermobileno", customerMobileNo);
                        customerDetails_JsonObject.put("orderid", orderid);
                        customerDetails_JsonObject.put("deliveryuserkey", deliveryPartnerKey);
                        customerDetails_JsonObject.put("deliveryusermobileno", deliveryPartnerMobileNo);
                        customerDetails_JsonObject.put("deliveryusername", deliveryPartnerName);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(Constants.TAG, "JSONOBJECT: " + e);

                    }

                String apiToUpdateCustomerOrderDetails = Constants.api_UpdateCustomerTrackingOrderDetails +"?usermobileno="+customerMobileNo+"&orderid="+orderid;

                initUpdateCustomerOrderDetailsInterface(mContext);
                Update_CustomerOrderDetails_TrackingTable_AsyncTask asyncTask_TO_update =new Update_CustomerOrderDetails_TrackingTable_AsyncTask(mContext, mResultCallback_UpdateCustomerOrderDetailsTableInterface,customerDetails_JsonObject,apiToUpdateCustomerOrderDetails );
                asyncTask_TO_update.execute();


            }
            else{
                Toast.makeText(mContext, "orderid :"+orderid+" , vendorkey: "+vendorkey+" , customerMobileNo : "+ customerMobileNo, Toast.LENGTH_SHORT).show();
            }

        }
        else {

            Api_toChangeOrderDetailsUsingOrderid = Constants.api_updateTrackingOrderTable;

            try {
                jsonObject.put("key", OrderKey);
                jsonObject.put("deliveryuserkey", deliveryPartnerKey);
                jsonObject.put("deliveryusermobileno", deliveryPartnerMobileNo);
                jsonObject.put("deliveryusername", deliveryPartnerName);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "JSONOBJECT: " + e);

            }
        }






    } catch (Exception e) {
        e.printStackTrace();
        //Log.d(Constants.TAG, "JSONOBJECT: " + e);

    }


     //   Log.d(Constants.TAG, "Request Payload: " + jsonObject);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_toChangeOrderDetailsUsingOrderid,
        jsonObject, new Response.Listener<JSONObject>() {
@Override
public void onResponse(@NonNull JSONObject response) {
        try {
        String msg = String.valueOf(response.get("message"));
       // Log.d(Constants.TAG, "Response: " + msg);
        if(msg.equals("success")){

            /*Intent intent = new Intent(mContext, MobileScreen_Dashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);


             */
    /*    Intent intent =new Intent(mContext, MobileScreen_Dashboard.class);
        mContext.startActivity(intent);

     */
            try {
                if (IntentFrom.equals("MobileManageOrders")) {
                    try {
                        Adapter_Mobile_ManageOrders_ListView1.bottomSheetDialog.dismiss();

                        for (int i = 0; i < Mobile_ManageOrders1.sorted_OrdersList.size(); i++) {
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Mobile_ManageOrders1.sorted_OrdersList.get(i);
                            String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                            if (orderid_local.equals(orderid)) {
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                            }
                        }
                        Mobile_ManageOrders1.loadingpanelmask.setVisibility(View.GONE);
                        Mobile_ManageOrders1.loadingPanel.setVisibility(View.GONE);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                if (IntentFrom.equals("AppOrdersList")) {

                    try {


                        for (int i = 0; i < searchOrdersUsingMobileNumber.sorted_OrdersList.size(); i++) {
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = searchOrdersUsingMobileNumber.sorted_OrdersList.get(i);
                            String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                            if (orderid_local.equals(orderid)) {
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                            }
                        }

                        Adapter_Mobile_SearchOrders_usingMobileNumber_ListView.bottomSheetDialog.dismiss();

                        searchOrdersUsingMobileNumber.loadingpanelmask.setVisibility(View.GONE);
                        searchOrdersUsingMobileNumber.loadingPanel.setVisibility(View.GONE);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }



            try {
                if (IntentFrom.equals("PhoneOrdersList")) {

                    try {

                        Adapter_Mobile_SearchOrders_usingMobileNumber_ListView.bottomSheetDialog.dismiss();

                        for (int i = 0; i < Phone_Orders_List.sorted_OrdersList.size(); i++) {
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Phone_Orders_List.sorted_OrdersList.get(i);
                            String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                            if (orderid_local.equals(orderid)) {
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                            }
                        }
                        Phone_Orders_List.loadingpanelmask.setVisibility(View.GONE);
                        Phone_Orders_List.loadingPanel.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }




            try {
                if (IntentFrom.equals("EditOrders")) {

                    try {

                        Log.i("tag","deliveryPartnerName"+ deliveryPartnerName);

                        for (int i = 0; i < Edit_Or_CancelTheOrders.ordersList.size(); i++) {
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.ordersList.get(i);
                            String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                            if (orderid_local.equals(orderid)) {
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                                Edit_Or_CancelOrder_OrderDetails_Screen.modal_manageOrders_pojo_class.setDeliveryPartnerName(deliveryPartnerName);
                                Edit_Or_CancelOrder_OrderDetails_Screen.modal_manageOrders_pojo_class.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                                Edit_Or_CancelOrder_OrderDetails_Screen.modal_manageOrders_pojo_class.setDeliveryPartnerKey(deliveryPartnerKey);

                            }
                        }

                        if(Edit_Or_CancelTheOrders.sorted_OrdersList.size()>0){
                            for (int i = 0; i < Edit_Or_CancelTheOrders.sorted_OrdersList.size(); i++) {
                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Edit_Or_CancelTheOrders.sorted_OrdersList.get(i);
                                String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                if (orderid_local.equals(orderid)) {
                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                                }
                            }
                        }
                        Log.i("tag","deliveryPartnerName   2  "+ deliveryPartnerName);

                        Edit_Or_CancelOrder_OrderDetails_Screen.deliveryPersonMobileNotext_widget.setText(deliveryPartnerMobileNo);
                        Edit_Or_CancelOrder_OrderDetails_Screen. deliveryPersonNametext_widget.setText(deliveryPartnerName);
                        Edit_Or_CancelOrder_OrderDetails_Screen.deliverypartnerName = deliveryPartnerName;
                        Edit_Or_CancelOrder_OrderDetails_Screen.loadingpanelmask.setVisibility(View.GONE);
                        Edit_Or_CancelOrder_OrderDetails_Screen.loadingPanel.setVisibility(View.GONE);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try {
                //if (IntentFrom.contains("orderdetails")) {
                    try {
                        if (IntentFrom.contains("AppOrdersList")) {
                            try {

                                MobileScreen_OrderDetails1.bottomSheetDialog.dismiss();

                                for (int i = 0; i < searchOrdersUsingMobileNumber.sorted_OrdersList.size(); i++) {
                                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = searchOrdersUsingMobileNumber.sorted_OrdersList.get(i);
                                    String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                    if (orderid_local.equals(orderid)) {
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                                        MobileScreen_OrderDetails1.deliveryPartner_name_widget.setText(deliveryPartnerName);
                                        MobileScreen_OrderDetails1.deliveryPartner_mobileNo_widget.setText(deliveryPartnerMobileNo);

                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (IntentFrom.contains("PhoneOrdersList")) {
                            try {

                                MobileScreen_OrderDetails1.bottomSheetDialog.dismiss();

                                for (int i = 0; i < Phone_Orders_List.sorted_OrdersList.size(); i++) {
                                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Phone_Orders_List.sorted_OrdersList.get(i);
                                    String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                    if (orderid_local.equals(orderid)) {
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                                        MobileScreen_OrderDetails1.deliveryPartner_name_widget.setText(deliveryPartnerName);
                                        MobileScreen_OrderDetails1.deliveryPartner_mobileNo_widget.setText(deliveryPartnerMobileNo);

                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if (IntentFrom.contains("MobileManageOrders")) {
                            try {
                                MobileScreen_OrderDetails1.bottomSheetDialog.dismiss();

                                for (int i = 0; i < Mobile_ManageOrders1.sorted_OrdersList.size(); i++) {
                                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Mobile_ManageOrders1.sorted_OrdersList.get(i);
                                    String orderid_local = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                    if (orderid_local.equals(orderid)) {
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                        modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);
                                        MobileScreen_OrderDetails1.deliveryPartner_name_widget.setText(deliveryPartnerName);
                                        MobileScreen_OrderDetails1.deliveryPartner_mobileNo_widget.setText(deliveryPartnerMobileNo);
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    MobileScreen_OrderDetails1.loadingpanelmask.setVisibility(View.GONE);
                    MobileScreen_OrderDetails1.loadingPanel.setVisibility(View.GONE);

            //    }



            }
            catch (Exception e){
                e.printStackTrace();
            }




        }
        } catch (JSONException e) {
        e.printStackTrace();
        }
     //   Log.d(Constants.TAG, "Response: " + response);
        }
        }, new Response.ErrorListener() {
@Override
public void onErrorResponse(@NonNull VolleyError error) {
        Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
        Log.d(Constants.TAG, "Error: " + error.getMessage());
        Log.d(Constants.TAG, "Error: " + error.toString());

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

    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    Volley.newRequestQueue(mContext).add(jsonObjectRequest);

        }
        });




        return  listViewItem ;

        }


    private void initUpdateCustomerOrderDetailsInterface(Context mContext) {

        mResultCallback_UpdateCustomerOrderDetailsTableInterface  = new Update_CustomerOrderDetails_TrackingTableInterface() {
            @Override
            public void notifySuccess(String requestType, String success) {
                try{
                    Toast.makeText(mContext, "Succesfully Added the Delivery Partner in Customer Details", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                try{
                    Toast.makeText(mContext, "Failed to Added the Delivery Partner  in Customer Details", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
    }




        }

