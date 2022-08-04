package com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Phone_Orders_List;
import com.meatchop.tmcpartner.Settings.searchOrdersUsingMobileNumber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_AssignDeliveryPartner extends ArrayAdapter<AssignDeliveryPartner_PojoClass>{
  Context mContext;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
        String OrderKey,customerMobileNo,orderid,vendorkey,fromActivityName="";
 boolean orderdetailsnewschema =false;
    Update_CustomerOrderDetails_TrackingTableInterface mResultCallback_UpdateCustomerOrderDetailsTableInterface;


    public Adapter_AssignDeliveryPartner(Context mContext, List<AssignDeliveryPartner_PojoClass> deliveryPartnerList, String orderKey, String vendorKey, String customerMobileNo, String orderid, String fromActivityNamee) {
        super(mContext, R.layout.assigning_deliverypartner_list_item1, deliveryPartnerList);

        this.mContext=mContext;
        this.OrderKey=orderKey;
        this.vendorkey=vendorKey;

        this.deliveryPartnerList=deliveryPartnerList;
        this.customerMobileNo=customerMobileNo;
        this.orderid=orderid;
        this.fromActivityName = fromActivityNamee;
      //  Toast.makeText(mContext, "5  -" +fromActivityName, Toast.LENGTH_SHORT).show();

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
            @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.assigning_deliverypartner_list_item1, (ViewGroup) view, false);

            SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
         //   Toast.makeText(mContext, "6  -" +fromActivityName, Toast.LENGTH_SHORT).show();

            if (fromActivityName.equals("PosManageOrders")) {
                orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));

            }
            else{
                orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));

            }

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

                //Log.i(Constants.TAG,"deliveryPartnerKey"+deliveryPartnerKey);


                assignPartner_widget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


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


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_toChangeOrderDetailsUsingOrderid,
                            jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            try {
                                String msg = String.valueOf(response.get("message"));
                                //Log.d(Constants.TAG, "Response: " + msg);
                                if(msg.equals("success")){
                                 /*   Intent intent =new Intent(mContext, Pos_Dashboard_Screen.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                    mContext.startActivity(intent);

                                  */

                                      try {
                                        Intent intent = new Intent();
                                        if (fromActivityName.equals("PosManageOrders")) {


                                            for(int i = 0; i< Pos_ManageOrderFragment.sorted_OrdersList.size(); i++){
                                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Pos_ManageOrderFragment.sorted_OrdersList.get(i);
                                                String TrackingTableorderid  = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                                if(TrackingTableorderid.equals(orderid)){
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);

                                                }
                                            }
                                            for(int i = 0; i< Pos_ManageOrderFragment.ordersList.size(); i++){
                                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Pos_ManageOrderFragment.ordersList.get(i);
                                                String TrackingTableorderid  = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                                if(TrackingTableorderid.equals(orderid)){
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);

                                                }
                                            }
                                            try {
                                                Pos_ManageOrderFragment.manageOrdersListViewAdapter.notifyDataSetChanged();
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            intent = new Intent(mContext, Pos_Dashboard_Screen.class);


                                        }
                                        else if(fromActivityName.equals("AppSearchOrders") || fromActivityName.equals("AppSearchOrders")){


                                            for(int i = 0; i< searchOrdersUsingMobileNumber.sorted_OrdersList.size(); i++){
                                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = searchOrdersUsingMobileNumber.sorted_OrdersList.get(i);
                                                String TrackingTableorderid  = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                                if(TrackingTableorderid.equals(orderid)){
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);

                                                }
                                            }
                                            for(int i = 0; i< searchOrdersUsingMobileNumber.ordersList.size(); i++){
                                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = searchOrdersUsingMobileNumber.ordersList.get(i);
                                                String TrackingTableorderid  = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                                if(TrackingTableorderid.equals(orderid)){
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);

                                                }
                                            }
                                            try {
                                                searchOrdersUsingMobileNumber.adapter_PosSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            intent = new Intent(mContext, searchOrdersUsingMobileNumber.class);

                                        }


                                        else if(fromActivityName.equals("PhoneSearchOrders")){


                                            for(int i = 0; i< Phone_Orders_List.sorted_OrdersList.size(); i++){
                                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Phone_Orders_List.sorted_OrdersList.get(i);
                                                String TrackingTableorderid  = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                                if(TrackingTableorderid.equals(orderid)){
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);

                                                }
                                            }
                                            for(int i = 0; i< Phone_Orders_List.ordersList.size(); i++){
                                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = Phone_Orders_List.ordersList.get(i);
                                                String TrackingTableorderid  = modal_manageOrders_forOrderDetailList1.getOrderid().toString();
                                                if(TrackingTableorderid.equals(orderid)){
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerName(deliveryPartnerName);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerKey(deliveryPartnerKey);
                                                    modal_manageOrders_forOrderDetailList1.setDeliveryPartnerMobileNo(deliveryPartnerMobileNo);

                                                }
                                            }
                                            try {
                                                Phone_Orders_List.adapter_PosSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            intent = new Intent(mContext, Phone_Orders_List.class);

                                        }


                                        else {
                                            intent = new Intent(mContext, Pos_Dashboard_Screen.class);

                                        }
                                          intent.putExtra("key", OrderKey);
                                          intent.putExtra("deliveryusermobileno", deliveryPartnerMobileNo);
                                          intent.putExtra("deliveryuserkey", deliveryPartnerKey);
                                          intent.putExtra("deliveryusername", deliveryPartnerName);
                                          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        mContext.startActivity(intent);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Log.d(Constants.TAG, "Response: " + response);
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

