package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_Mobile_SearchOrders_usingMobileNumber_ListView extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String changestatusto,orderStatus,OrderKey;
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay;
    public searchOrdersUsingMobileNumber mobile_manageOrders1;


    public Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, searchOrdersUsingMobileNumber mobile_manageOrders1, String orderStatus) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);

        this.mobile_manageOrders1 = mobile_manageOrders1;
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.orderStatus=orderStatus;

    }



    @Nullable
    @Override
    public Modal_ManageOrders_Pojo_Class getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_ManageOrders_Pojo_Class item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.mobile_manage_orders_listview_item1, (ViewGroup) view, false);

        final TextView orderid_text_widget = listViewItem.findViewById(R.id.orderid_text_widget);

        final CardView cardLayout =listViewItem.findViewById(R.id.cardLayout);

     //   final Button changeDeliveryPartner =listViewItem.findViewById(R.id.changeDeliveryPartner);

        //
        final TextView moblieNo_text_widget = listViewItem.findViewById(R.id.moblieNo_text_widget);
        final TextView tokenNo_text_widget = listViewItem.findViewById(R.id.tokenNo_text_widget);
        final TextView orderDetails_text_widget = listViewItem.findViewById(R.id.orderDetails_text_widget);
        final TextView ordertype_text_widget = listViewItem.findViewById(R.id.ordertype_text_widget);
        final TextView slotName_text_widget = listViewItem.findViewById(R.id.slotname_text_widget);
        final TextView slottime_text_widget = listViewItem.findViewById(R.id.slottime_text_widget);
        final TextView slotdate_text_widget = listViewItem.findViewById(R.id.slotdate_text_widget);
        final TextView orderstatus_text_widget = listViewItem.findViewById(R.id.orderstatus_text_widget);


        final LinearLayout order_item_list_parentLayout =listViewItem.findViewById(R.id.order_item_list_parentLayout);

        final LinearLayout new_Order_Linearlayout =listViewItem.findViewById(R.id.new_Order_Linearlayout);
        final LinearLayout confirming_order_Linearlayout =listViewItem.findViewById(R.id.confirming_order_Linearlayout);
        final LinearLayout ready_Order_Linearlayout =listViewItem.findViewById(R.id.ready_Order_Linearlayout);
        final LinearLayout cancelled_Order_Linearlayout =listViewItem.findViewById(R.id.cancelled_Order_Linearlayout);

        final LinearLayout ordertypeLayout =listViewItem.findViewById(R.id.ordertypeLayout);

        final Button confirmed_Order_button_widget = listViewItem.findViewById(R.id.accept_Order_button_widget);
        final Button cancel_button_widget = listViewItem.findViewById(R.id.cancel_button_widget);

        final Button ready_for_pickup_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_button_widget);
        final Button pending_order_print_button_widget = listViewItem.findViewById(R.id.pending_order_print_button_widget);

        final Button other_print_button_widget = listViewItem.findViewById(R.id.other_print_button_widget);
        final Button cancelled_print_button_widget = listViewItem.findViewById(R.id.cancelled_print_button_widget);
        final Button generateTokenNo_button_widget = listViewItem.findViewById(R.id.generateTokenNo_button_widget);
        final Button transit_generateTokenNo_button_widget = listViewItem.findViewById(R.id.transit_generateTokenNo_button_widget);
        ordertypeLayout.setVisibility(View.GONE);
        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);
        Log.i("Tag","Order Pos:   "+ mobile_manageOrders1.sorted_OrdersList.get(pos));
        orderStatus = modal_manageOrders_pojo_class.getOrderstatus().toUpperCase();
        if(orderStatus.equals(Constants.NEW_ORDER_STATUS)){
            ordertype_text_widget.setVisibility(View.VISIBLE);
            new_Order_Linearlayout.setVisibility(View.VISIBLE);
            ready_Order_Linearlayout.setVisibility(View.GONE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);
        }


        if(orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)){
            ordertype_text_widget.setVisibility(View.VISIBLE);
            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.GONE);
            confirming_order_Linearlayout.setVisibility(View.VISIBLE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);

        }



        if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)){
            ordertype_text_widget.setVisibility(View.VISIBLE);
            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.VISIBLE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);


        }

        if(orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)){
            ordertype_text_widget.setVisibility(View.VISIBLE);
            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.VISIBLE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);


        }
        if(orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)){

            /*Log.i("Tag","ItemName   "+String.format(" %s ", modal_manageOrders_pojo_class.getOrder_orderStatus()));

            if(String.format(" %s ", modal_manageOrders_pojo_class.getOrder_orderStatus()).equals("Cancelled")) {
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
            }

             */

            //if(String.format(" %s ", modal_manageOrders_pojo_class.getOrder_orderStatus()).equals("Delivered")){

            ordertype_text_widget.setVisibility(View.VISIBLE);

            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.VISIBLE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);
            //  }

        }
        String orderStatusFromArray = modal_manageOrders_pojo_class.getOrderstatus();

            try {
            if (orderStatusFromArray.equals(Constants.CONFIRMED_ORDER_STATUS)) {
            String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
            if ((tokenNofromArray.length() > 0) && (tokenNofromArray != null) && (!tokenNofromArray.equals(""))) {
            pending_order_print_button_widget.setVisibility(View.VISIBLE);
            } else {
            pending_order_print_button_widget.setVisibility(View.GONE);

            }
            }
            }
            catch (Exception e){
            e.printStackTrace();
            }


            try {
            if (orderStatusFromArray.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
            String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
            if ((tokenNofromArray.length() > 0) && (tokenNofromArray != null) && (!tokenNofromArray.equals(""))) {
            other_print_button_widget.setVisibility(View.VISIBLE);
            } else {
            other_print_button_widget.setVisibility(View.GONE);

            }
            }

            }catch (Exception e){
            e.printStackTrace();
            }



        Log.i("tag","orderStatusFromArray"+ orderStatusFromArray);
        Log.i("tag","orderStatus"+ orderStatus);
        try {
            orderstatus_text_widget.setVisibility(View.VISIBLE);

            orderstatus_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderstatus()));
        }
        catch (Exception e){
            e.printStackTrace();
            orderstatus_text_widget.setText(String.format(" %s", ""));
            orderstatus_text_widget.setVisibility(View.GONE);
        }
/*
        try{
            if(mobile_manageOrders1.isSearchButtonClicked) {
                orderstatus_layout.setVisibility(View.VISIBLE);
                try {
                    orderstatus_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderstatus()));
                }
                catch (Exception e){
                    e.printStackTrace();
                    orderstatus_text_widget.setText(String.format(" %s", ""));

                }
            }
            else{
                orderstatus_layout.setVisibility(View.GONE);

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }


 */
        ordertype_text_widget.setVisibility(View.GONE);



        try {
            moblieNo_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getUsermobile()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            tokenNo_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getTokenno()));
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            orderid_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderid()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            slotName_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getSlotname()));
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try {
            slottime_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getSlottimerange()));
        }
        catch (Exception e){
            e.printStackTrace();
        }




        try {
            slotdate_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getSlotdate()));
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try {
            ordertype_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderType()));
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try {

            JSONArray array  = modal_manageOrders_pojo_class.getItemdesp();
            Log.i("tag","array.length()"+ array.length());
            String b= array.toString();
            modal_manageOrders_pojo_class.setItemdesp_string(b);
            String itemDesp="";


            for(int i=0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                if (json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");

                    String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));



                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(marinadesObject.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    itemName = itemName + " Marinade Box ";
                    if (itemDesp.length()>0) {

                        itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+itemName, quantity);
                    } else {
                        itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName, quantity);

                    }

               //     orderDetails_text_widget.setText(String.format(itemDesp));

                } else {

                    Log.i("tag", "array.lengrh(i" + json.length());

                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(json.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    if (itemDesp.length()>0) {

                        itemDesp = String.format("%s ,\n%s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s * %s", itemName, quantity);

                    }

            //        orderDetails_text_widget.setText(String.format(itemDesp));
                    Log.i("tag", "array.lengrh(i" + json.length());


                }

            }
            orderDetails_text_widget.setText(String.format(itemDesp));

        } catch (JSONException e) {
            e.printStackTrace();
        }




/*        changeDeliveryPartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AssigningDeliveryPartner.class);
                intent.putExtra("TrackingTableKey",modal_manageOrders_pojo_class.getKeyfromtrackingDetails());
                mContext.startActivity(intent);
            }
        });


 */
        order_item_list_parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (mContext, MobileScreen_OrderDetails1.class);
                Bundle bundle = new Bundle();
                bundle.putString("From","MobileSearchOrders");

                bundle.putParcelable("data", modal_manageOrders_pojo_class);
                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });

        generateTokenNo_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));

                generatingTokenNo(vendorkey,orderDetailsKey);

                Log.i("tag","orderkey1"+ OrderKey);
            }
        });


        transit_generateTokenNo_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));

                generatingTokenNo(vendorkey,orderDetailsKey);

                Log.i("tag","orderkey1"+ OrderKey);


            }
        });






        //1
        confirmed_Order_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Currenttime = getDate_and_time();
                changestatusto =Constants.CONFIRMED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                Log.i("tag","orderkey1"+ OrderKey);
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));

                generatingTokenNo(vendorkey,orderDetailsKey);

                Log.i("Tag","0"+OrderKey);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);

                ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);

                mobile_manageOrders1.sorted_OrdersList.remove(pos);
                notifyDataSetChanged();
            }
        });
        cancel_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Currenttime = getDate_and_time();

                changestatusto =Constants.CANCELLED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                Log.i("Tag","0"+OrderKey);
                ChangeStatusOftheOrder(changestatusto,OrderKey, Currenttime);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
                Log.i("Tag",""+changestatusto+OrderKey);

                ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);

                mobile_manageOrders1.sorted_OrdersList.remove(pos);
            }
        });



        ready_for_pickup_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changestatusto =Constants.READY_FOR_PICKUP_ORDER_STATUS;
                Currenttime = getDate_and_time();

                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                Log.i("Tag","0"+OrderKey);

                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);
                modal_manageOrders_pojo_class.setOrderstatus(changestatusto);

                ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);

                mobile_manageOrders1.sorted_OrdersList.remove(pos);
            }
        });




        pending_order_print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();

                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();

                selectedBillDetails.add(selectedOrder);
             //   printRecipt("totaltaxAmount","payableAmount","OrderID",selectedBillDetails);
            }

        });
        other_print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();

                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();

                selectedBillDetails.add(selectedOrder);
              //  printRecipt("totaltaxAmount","payableAmount","OrderID",selectedBillDetails);

            }
        });

        cancelled_print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();

                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();

                selectedBillDetails.add(selectedOrder);
              //  printRecipt("totaltaxAmount","payableAmount","OrderID",selectedBillDetails);

            }
        });






        return  listViewItem ;

    }


    private void generatingTokenNo(String vendorkey, String orderDetailsKey) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_generateTokenNo+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {


                Log.d(Constants.TAG, "api: " + Constants.api_generateTokenNo+vendorkey);

                Log.d(Constants.TAG, "Responsewwwww: " + response);
                try {
                    String tokenNo = response.getString("tokenNumber");
                    UpdateTokenNoInOrderDetails(tokenNo,orderDetailsKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Token_No_Not_Found_Instruction,
                            R.string.OK_Text,R.string.Empty_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNo() {

                                }
                            });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Token_No_Error_Instruction,
                        R.string.OK_Text,R.string.Empty_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNo() {

                            }
                        });

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

    private void UpdateTokenNoInOrderDetails(String tokenNo, String orderDetailsKey) {
        JSONObject  jsonObject = new JSONObject();
        try {

                jsonObject.put("key", orderDetailsKey);
                jsonObject.put("tokenno", tokenNo);
                Log.i("tag","listenertoken"+ "");








        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
//"?key="+OrderKey+"&orderstatus="+changestatusto+"&currentTime="+Currenttime
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_UpdateTokenNO_OrderDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i = 0; i< mobile_manageOrders1.ordersList.size(); i++){
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= mobile_manageOrders1.ordersList.get(i);
                    String key = modal_manageOrders_pojo_class.getOrderdetailskey();
                    if(orderDetailsKey.equals(key)){
                        modal_manageOrders_pojo_class.setTokenno(tokenNo);
                        notifyDataSetChanged();

                    }
                }

                Log.d(Constants.TAG, "Responsewwwww: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Token_No_Not_Updated_Instruction,
                        R.string.OK_Text,R.string.Empty_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNo() {

                            }
                        });
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


    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
    }

    private void ChangeStatusOftheOrder(String changestatusto, String OrderKey, String currenttime) {
        mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            if(changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)){
                jsonObject.put("key", OrderKey);
                jsonObject.put("orderstatus", changestatusto);
                jsonObject.put("orderconfirmedtime", currenttime);
                jsonObject.put("ordercancelledtime", "");
                jsonObject.put("orderreadytime", "");
                jsonObject.put("orderpickeduptime", "");
                jsonObject.put("orderdeliverytime", "");
                jsonObject.put("deliveryuserlat", "");
                jsonObject.put("deliveryuserlong", "");
                Log.i("tag","listenertoken"+ "");
            }
            if(changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)){
                jsonObject.put("key", OrderKey);
                jsonObject.put("orderstatus", changestatusto);
                jsonObject.put("orderconfirmedtime", "");
                jsonObject.put("ordercancelledtime", "");
                jsonObject.put("orderreadytime", Currenttime);
                jsonObject.put("orderpickeduptime", "");
                jsonObject.put("orderdeliverytime", "");
                jsonObject.put("deliveryuserlat", "");
                jsonObject.put("deliveryuserlong", "");
                Log.i("tag","listenertoken"+ "");
            }
            if(changestatusto.equals(Constants.CANCELLED_ORDER_STATUS)){
                jsonObject.put("key", OrderKey);
                jsonObject.put("orderstatus", changestatusto);
                jsonObject.put("orderconfirmedtime", "");
                jsonObject.put("ordercancelledtime", Currenttime);
                jsonObject.put("orderreadytime", "");
                jsonObject.put("orderpickeduptime", "");
                jsonObject.put("orderdeliverytime", "");
                jsonObject.put("deliveryuserlat", "");
                jsonObject.put("deliveryuserlong", "");
                Log.i("tag","listenertoken"+ "");
            }







        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
//"?key="+OrderKey+"&orderstatus="+changestatusto+"&currentTime="+Currenttime
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updateTrackingOrderTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i = 0; i< mobile_manageOrders1.ordersList.size(); i++){
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= mobile_manageOrders1.ordersList.get(i);
                    String keyfromtrackingDetails = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    if(OrderKey.equals(keyfromtrackingDetails)){
                        modal_manageOrders_pojo_class.setOrderstatus(changestatusto);

                        if(changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                            modal_manageOrders_pojo_class.setOrderconfirmedtime(Currenttime);
                        }
                        if(changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                            modal_manageOrders_pojo_class.setOrderreadytime(Currenttime);
                        }
                        notifyDataSetChanged();
                    }
                }
                Log.d(Constants.TAG, "Responsewwwww: " + response);
                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());
                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

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


}
