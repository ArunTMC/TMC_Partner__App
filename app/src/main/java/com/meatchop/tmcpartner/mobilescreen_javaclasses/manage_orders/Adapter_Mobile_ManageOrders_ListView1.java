package com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meatchop.tmcpartner.add_updateinventorydetailentries.Add_UpdateInventoryDetailsEntries_AsyncTask;
import com.meatchop.tmcpartner.add_updateinventorydetailentries.Add_UpdateInventoryDetailsEntries_Interface;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTableInterface;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
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
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

public class Adapter_Mobile_ManageOrders_ListView1 extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String changestatusto,orderStatus,OrderKey,ordertype,deliveryPersonName="",userStatus ="";
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay,deliverytype;
    public Mobile_ManageOrders1 mobile_manageOrders1;
    static BottomSheetDialog bottomSheetDialog;
    String deliverypartnerName="";
    BluetoothAdapter mBluetoothAdapter =null;
    String orderPlacedTime ="";
    boolean isOrderPlacedlessThan3MinsBefore = true;
    boolean isCancelledinsidefunctionboolean = false ;
    boolean isOrderCancelled = false;

    boolean orderdetailsnewschema = false , updateweightforonlineorders =false;
    Update_CustomerOrderDetails_TrackingTableInterface mResultCallback_UpdateCustomerOrderDetailsTableInterface;
    Dialog changeWeight_dialog;



    static List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp = new ArrayList<>();
    Add_UpdateInventoryDetailsEntries_Interface mResultCallback_Add_UpdateInventoryEntriesInterface = null;

     LinearLayout new_Order_Linearlayout ;
     LinearLayout confirming_order_Linearlayout ;
     LinearLayout ready_Order_Linearlayout  ;
     LinearLayout cancelled_Order_Linearlayout ;
    public Adapter_Mobile_ManageOrders_ListView1(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, Mobile_ManageOrders1 mobile_manageOrders1, String orderStatus) {
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
        SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
        //orderdetailsnewschema = true;
        updateweightforonlineorders = (shared.getBoolean("updateweightforonlineorders", false));
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
        final TextView deliveryType_text_widget = listViewItem.findViewById(R.id.deliveryType_text_widget);
        final RelativeLayout totalButtonLayout =listViewItem.findViewById(R.id.buttonsRelativeLayout);

       final LinearLayout ordercancellationtimeRefresh_Layout =listViewItem.findViewById(R.id.ordercancellationtimeRefresh_Layout);
        final LinearLayout refreshordercancelationtime_image_layout =listViewItem.findViewById(R.id.refreshordercancelationtime_image_layout);

        final LinearLayout order_item_list_parentLayout =listViewItem.findViewById(R.id.order_item_list_parentLayout);

         new_Order_Linearlayout =listViewItem.findViewById(R.id.new_Order_Linearlayout);
         confirming_order_Linearlayout =listViewItem.findViewById(R.id.confirming_order_Linearlayout);
         ready_Order_Linearlayout =listViewItem.findViewById(R.id.ready_Order_Linearlayout);
         cancelled_Order_Linearlayout =listViewItem.findViewById(R.id.cancelled_Order_Linearlayout);
        final LinearLayout tokenNoLayout =listViewItem.findViewById(R.id.tokenNoLayout);
        final LinearLayout orderstatus_layout =listViewItem.findViewById(R.id.orderstatus_layout);
        final LinearLayout slotDateLayout =listViewItem.findViewById(R.id.slotDateoLayout);
        final LinearLayout slotTimeLayout =listViewItem.findViewById(R.id.slotTimeLayout);
        final LinearLayout deliveryTypeLayout =listViewItem.findViewById(R.id.deliveryTypeLayout);



        final Button ready_for_pickup_delivered_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_delivered_button_widget);

        final Button confirmed_Order_button_widget = listViewItem.findViewById(R.id.accept_Order_button_widget);
        final Button cancel_button_widget = listViewItem.findViewById(R.id.cancel_button_widget);

        final Button ready_for_pickup_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_button_widget);
        final Button pending_order_assignDeliveryperson_button_widget = listViewItem.findViewById(R.id.pending_order_assignDeliveryperson_button_widget);

        final Button other_assignDeliveryperson_button_widget = listViewItem.findViewById(R.id.other_assignDeliveryperson_button_widget);
        final Button cancelled_assignDeliveryperson_button_widget = listViewItem.findViewById(R.id.cancelled_assignDeliveryperson_button_widget);
        final Button generateTokenNo_button_widget = listViewItem.findViewById(R.id.generateTokenNo_button_widget);
        //final Button transit_generateTokenNo_button_widget = listViewItem.findViewById(R.id.transit_generateTokenNo_button_widget);
        final Button mobileprint_button_widget = listViewItem.findViewById(R.id.mobileprint_button_widget);

        try{
        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);
      //  //Log.i("Tag","Order Pos:   "+ mobile_manageOrders1.sorted_OrdersList.get(pos));

            try{
                userStatus = modal_manageOrders_pojo_class.getUserstatus().toString();

                if(userStatus.toUpperCase().equals(Constants.USERSTATUS_FLAGGED)){
                    cardLayout.setBackground(getDrawable(mContext,R.drawable.orange_border_button));

                }
                else{
                    cardLayout.setBackgroundColor(Color.WHITE);
                }

            }
            catch ( Exception e ){
                e.printStackTrace();
            }


        try{
            deliveryPersonName = modal_manageOrders_pojo_class.getDeliveryPartnerName().toString();
        }
        catch (Exception e){
            e.printStackTrace();
            deliveryPersonName="";
        }

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


            ordertype_text_widget.setVisibility(View.VISIBLE);

            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.VISIBLE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);
            //  }

        }
/*

        try{
            String orderid = modal_manageOrders_pojo_class.getOrderid().toString();

            for(int i = 0; i< mobile_manageOrders1.ordersList.size(); i++) {
                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_classd= mobile_manageOrders1.ordersList.get(i);
                String orderidfromOrderList = modal_manageOrders_pojo_classd.getOrderid().toString();
                if(orderid.equals(orderidfromOrderList)) {
                    mobile_manageOrders1.ordersList.remove(i);
                    for (int sortedarrayIterator = 0; sortedarrayIterator < Mobile_ManageOrders1.sorted_OrdersList.size(); sortedarrayIterator++) {
                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_sortedOrdersList = Mobile_ManageOrders1.sorted_OrdersList.get(sortedarrayIterator);
                        String sortedorderid = modal_manageOrders_pojo_class_sortedOrdersList.getOrderid().toString();
                        if (orderid.equals(sortedorderid)) {

                            Mobile_ManageOrders1.sorted_OrdersList.remove(sortedarrayIterator);

                        }


                    }

                }


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


 */


        String orderStatusfromArray = modal_manageOrders_pojo_class.getOrderstatus();
       // //Log.i("tag","orderStatusFromArray"+ orderStatusfromArray);
      //  //Log.i("tag","orderStatus"+ orderStatus);
        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

/*

        orderPlacedTime ="";
         isOrderPlacedlessThan3MinsBefore = true;

        try {
            orderPlacedTime =  modal_manageOrders_pojo_class.getOrderplacedtime();
            Log.d(Constants.TAG, "log modal_manageOrders_pojo_class : " + modal_manageOrders_pojo_class);

            isOrderPlacedlessThan3MinsBefore =modal_manageOrders_pojo_class.isOrderPlacedlessThan3MinsBefore();
            Log.d(Constants.TAG, "log isOrderPlacedlessThan3MinsBeforegg : " + isOrderPlacedlessThan3MinsBefore);

        }
        catch (Exception e){
            e.printStackTrace();
        }



        if(!isOrderPlacedlessThan3MinsBefore)
        {
            totalButtonLayout.setVisibility(View.VISIBLE);
            ordercancellationtimeRefresh_Layout.setVisibility(View.GONE);
        }
        else{
            totalButtonLayout.setVisibility(View.GONE);
            ordercancellationtimeRefresh_Layout.setVisibility(View.VISIBLE);

        }


 */
        totalButtonLayout.setVisibility(View.VISIBLE);
        ordercancellationtimeRefresh_Layout.setVisibility(View.GONE);



        refreshordercancelationtime_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                String orderid = ordersList.get(pos).getOrderid().toString();
                        //checkStatusForTheOrder(orderid);



                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,Constants.api_GetTrackingOrderDetails_orderid+orderid,
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        Log.d(Constants.TAG, "Status response: " + response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("content");
                            for(int i = 0 ;i<jsonArray.length();i++){
                                JSONObject json = jsonArray.getJSONObject(i);

                                String orderStatusS = json.getString("orderstatus");
                                if(orderStatusS.toUpperCase().equals(Constants.CANCELLED_ORDER_STATUS)){
                                    isOrderCancelled = true;
                                    if(orderStatus.equals(Constants.NEW_ORDER_STATUS)) {

                                        int neworderCount = mobile_manageOrders1.newCount - 1;

                                        if (neworderCount > 0) {
                                            mobile_manageOrders1.mobile_new_Order_widget.setText(String.format("%s ( %d )", Constants.NEW_ORDER_STATUS, neworderCount));
                                            mobile_manageOrders1.newCount = neworderCount;
                                        } else {
                                            mobile_manageOrders1.mobile_new_Order_widget.setText(String.format("%s", Constants.NEW_ORDER_STATUS));

                                        }
                                    }
                                    if(orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)) {

                                        int ConfirmedorderCount = mobile_manageOrders1.confirmedCount+1;

                                        if(ConfirmedorderCount>0) {
                                            mobile_manageOrders1.mobile_confirmed_Order_widget.setText(String.format("%s ( %d )", Constants.CONFIRMED_ORDER_STATUS, ConfirmedorderCount));
                                            mobile_manageOrders1.confirmedCount=ConfirmedorderCount;

                                        }
                                        else{
                                            mobile_manageOrders1.mobile_confirmed_Order_widget.setText(String.format("%s", Constants.CONFIRMED_ORDER_STATUS));


                                        }}
                                }
                                else{
                                    isOrderCancelled = false;

                                }
                            }






                            if(isOrderCancelled){
                                for(int i = 0; i< mobile_manageOrders1.ordersList.size(); i++) {
                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= mobile_manageOrders1.ordersList.get(i);
                                    String orderidfromOrderList = modal_manageOrders_pojo_class.getOrderid().toString();
                                    if(orderid.equals(orderidfromOrderList)) {
                                        mobile_manageOrders1.ordersList.remove(i);
                                        for (int sortedarrayIterator = 0; sortedarrayIterator < Mobile_ManageOrders1.sorted_OrdersList.size(); sortedarrayIterator++) {
                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_sortedOrdersList = Mobile_ManageOrders1.sorted_OrdersList.get(sortedarrayIterator);
                                            String sortedorderid = modal_manageOrders_pojo_class_sortedOrdersList.getOrderid().toString();
                                            if (orderid.equals(sortedorderid)) {

                                                Mobile_ManageOrders1.sorted_OrdersList.remove(sortedarrayIterator);

                                            }


                                        }

                                    }


                                }

                            }
                            else{
                                orderPlacedTime =  ordersList.get(pos).getOrderplacedtime();

                                boolean isOrderPlacedlessThan3MinsBefore =   mobile_manageOrders1.CheckWeathertheOrderisPlacedLessThan3Mins(orderPlacedTime);

                                if(!isOrderPlacedlessThan3MinsBefore)
                                {
                                    totalButtonLayout.setVisibility(View.VISIBLE);
                                    ordercancellationtimeRefresh_Layout.setVisibility(View.GONE);

                                    for(int i = 0; i< mobile_manageOrders1.ordersList.size(); i++) {
                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= mobile_manageOrders1.ordersList.get(i);
                                        String orderidfromOrderList = modal_manageOrders_pojo_class.getOrderid().toString();
                                        if(orderid.equals(orderidfromOrderList)) {
                                            mobile_manageOrders1.ordersList.get(i).setOrderPlacedlessThan3MinsBefore(isOrderPlacedlessThan3MinsBefore);
                                            for (int sortedarrayIterator = 0; sortedarrayIterator < Mobile_ManageOrders1.sorted_OrdersList.size(); sortedarrayIterator++) {
                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_sortedOrdersList = Mobile_ManageOrders1.sorted_OrdersList.get(sortedarrayIterator);
                                                String sortedorderid = modal_manageOrders_pojo_class_sortedOrdersList.getOrderid().toString();
                                                if (orderid.equals(sortedorderid)) {

                                                    mobile_manageOrders1.sorted_OrdersList.get(sortedarrayIterator).setOrderPlacedlessThan3MinsBefore(isOrderPlacedlessThan3MinsBefore);

                                                }


                                            }

                                        }


                                    }
                                }
                                else{
                                    totalButtonLayout.setVisibility(View.GONE);
                                    ordercancellationtimeRefresh_Layout.setVisibility(View.VISIBLE);

                                }
                            }



                            notifyDataSetChanged();
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {
                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Token_No_Error_Instruction,
                                R.string.OK_Text,R.string.Empty_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        Toast.makeText(mContext,"Can't Refresh !!! Please Refresh Again",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNo() {

                                    }
                                });
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



                Log.d(Constants.TAG, "log clicked : " + isOrderPlacedlessThan3MinsBefore);


            }
        });




        try {
            deliverytype =  modal_manageOrders_pojo_class.getDeliverytype().toUpperCase();

        }
        catch (Exception e){
            e.printStackTrace();
        }






        if((orderStatusfromArray.toUpperCase().equals(Constants.PICKEDUP_ORDER_STATUS))||orderStatusfromArray.toUpperCase().equals(Constants.DELIVERED_ORDER_STATUS)){
            pending_order_assignDeliveryperson_button_widget.setVisibility(View.GONE);
            generateTokenNo_button_widget.setVisibility(View.GONE);
            //transit_generateTokenNo_button_widget.setVisibility(View.GONE);

            other_assignDeliveryperson_button_widget.setVisibility(View.VISIBLE);

        }



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

        if(orderStatusfromArray.equals(Constants.CONFIRMED_ORDER_STATUS)){
            String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
            if((tokenNofromArray.length()>0)&&(tokenNofromArray != null)&&(!tokenNofromArray.equals(""))){
                pending_order_assignDeliveryperson_button_widget.setVisibility(View.VISIBLE);
                generateTokenNo_button_widget.setVisibility(View.GONE);
                mobileprint_button_widget.setVisibility(View.VISIBLE);

            }
            else{
                pending_order_assignDeliveryperson_button_widget.setVisibility(View.GONE);
                generateTokenNo_button_widget.setVisibility(View.VISIBLE);
                mobileprint_button_widget.setVisibility(View.GONE);
            }
        }
        else{
            String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
            if((tokenNofromArray.length()>0)&&(tokenNofromArray != null)&&(!tokenNofromArray.equals(""))){
                other_assignDeliveryperson_button_widget.setVisibility(View.VISIBLE);
                //transit_generateTokenNo_button_widget.setVisibility(View.GONE);
                generateTokenNo_button_widget.setVisibility(View.GONE);
                mobileprint_button_widget.setVisibility(View.VISIBLE);

            }
            else{
                other_assignDeliveryperson_button_widget.setVisibility(View.GONE);
              //  transit_generateTokenNo_button_widget.setVisibility(View.GONE);
                generateTokenNo_button_widget.setVisibility(View.VISIBLE);
                mobileprint_button_widget.setVisibility(View.GONE);

            }
        }
        try {
            if (orderStatusfromArray.equals(Constants.NEW_ORDER_STATUS)) {
                generateTokenNo_button_widget.setVisibility(View.GONE);
                mobileprint_button_widget.setVisibility(View.GONE);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        if (deliverytype.equals(Constants.STOREPICKUP_DELIVERYTYPE)) {
            ready_for_pickup_button_widget.setVisibility(View.GONE);
            if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                other_assignDeliveryperson_button_widget.setVisibility(View.VISIBLE);
                ready_for_pickup_delivered_button_widget.setVisibility(View.VISIBLE);

            }
            else{
                ready_for_pickup_button_widget.setVisibility(View.VISIBLE);
                ready_for_pickup_delivered_button_widget.setVisibility(View.GONE);
            }
            slotName_text_widget.setVisibility(View.GONE);
            deliveryType_text_widget.setVisibility(View.VISIBLE);
            try {
                deliveryType_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getDeliverytype()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            ready_for_pickup_button_widget.setVisibility(View.VISIBLE);
            ready_for_pickup_delivered_button_widget.setVisibility(View.GONE);
            slotName_text_widget.setVisibility(View.VISIBLE);
            deliveryTypeLayout.setVisibility(View.GONE);
        }



        try{
            if((deliveryPersonName.length()>0)&&(!deliveryPersonName.equals(""))){
                pending_order_assignDeliveryperson_button_widget.setText("Change Delivery Partner");
                other_assignDeliveryperson_button_widget.setText("Change Delivery Partner");
                cancelled_assignDeliveryperson_button_widget.setText("Change Delivery Partner");

            }
            else{
                pending_order_assignDeliveryperson_button_widget.setText("Assign Delivery Partner");
                other_assignDeliveryperson_button_widget.setText("Assign Delivery Partner");
                cancelled_assignDeliveryperson_button_widget.setText("Assign Delivery Partner");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try {
            ordertype_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderType()));
            ordertype =  modal_manageOrders_pojo_class.getOrderType().toUpperCase();
            if(orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)) {
                if (ordertype.equals(Constants.POSORDER)) {
                    tokenNoLayout.setVisibility(View.GONE);
                    slotTimeLayout.setVisibility(View.GONE);
                    slotDateLayout.setVisibility(View.GONE);
                    other_assignDeliveryperson_button_widget.setVisibility(View.GONE);

                    generateTokenNo_button_widget.setVisibility(View.GONE);
               //     transit_generateTokenNo_button_widget.setVisibility(View.GONE);
                } else {
                    tokenNoLayout.setVisibility(View.VISIBLE);
                    slotTimeLayout.setVisibility(View.VISIBLE);
                    slotDateLayout.setVisibility(View.VISIBLE);
                    generateTokenNo_button_widget.setVisibility(View.GONE);
              //      transit_generateTokenNo_button_widget.setVisibility(View.GONE);


                }
                other_assignDeliveryperson_button_widget.setVisibility(View.GONE);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }





        try {

            JSONArray array  = modal_manageOrders_pojo_class.getItemdesp();
            //Log.i("tag","array.length()"+ array.length());
            String b= array.toString();
            modal_manageOrders_pojo_class.setItemdesp_string(b);
            String itemDesp="";
            String subCtgyKey ="";

            for(int i=0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

                if (json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");

                    String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));
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
                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(marinadesObject.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    itemName = itemName + " Marinade Box ";
                    if (itemDesp.length()>0) {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+ "Grill House "+ itemName, quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+"Ready to Cook  "+ itemName, quantity);

                        }
                        else{
                            itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+itemName, quantity);

                        }
                    } else {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", "Grill House "+ itemName, quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", "Ready to Cook  "+ itemName, quantity);

                        }
                        else{
                            itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName, quantity);

                        }

                    }

               //     orderDetails_text_widget.setText(String.format(itemDesp));

                } else {

                    //Log.i("tag", "array.lengrh(i" + json.length());
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


                    String cutname = "";

                    try {
                        if(json.has("cutname")) {
                            cutname =  String.valueOf(json.get("cutname"));
                        }
                        else {
                            cutname = "";
                        }
                    }
                    catch (Exception e){
                        cutname ="";
                        e.printStackTrace();
                    }

                    try{
                        if((cutname.length()>0) && (!cutname.equals(null)) && (!cutname.equals("null"))){
                            cutname = " [ "+cutname + " ] ";
                        }
                        else{
                            //cutname="";
                        }
                    }
                    catch (Exception e ){
                        e.printStackTrace();
                    }


                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(json.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    if (itemDesp.length()>0) {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s ,\n%s %s * %s", itemDesp,  "Grill House "+ itemName ,cutname, quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, "Ready to Cook  "+ itemName,cutname, quantity);

                        }
                        else{
                            itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, itemName,cutname, quantity);

                        }
                    } else {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s  %s * %s",  "Grill House "+ itemName,cutname, quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s  %s * %s",  "Ready to Cook  "+ itemName,cutname, quantity);

                        }
                        else{
                            itemDesp = String.format("%s  %s * %s", itemName,cutname, quantity);

                        }

                    }
                    /*
                    if (itemDesp.length()>0) {

                        itemDesp = String.format("%s ,\n%s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s * %s", itemName, quantity);

                    }

                     */


            //        orderDetails_text_widget.setText(String.format(itemDesp));
                    //Log.i("tag", "array.lengrh(i" + json.length());


                }

            }
            orderDetails_text_widget.setText(String.format(itemDesp));

        }
         catch (JSONException e) {
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



        mobileprint_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobile_manageOrders1.Adjusting_Widgets_Visibility(true);



                try {
                    if (BluetoothPrintDriver.IsNoConnection()) {

                        Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                        AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                    }
                    mobile_manageOrders1.Adjusting_Widgets_Visibility(true);

                    if(!BluetoothPrintDriver.IsNoConnection()){
                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                        if (!mBluetoothAdapter.isEnabled()) {
                            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                            AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                        }
                        else{
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(true);

                            mobile_manageOrders1.printRecipt(modal_manageOrders_pojo_class);


                        }
                    }
                    else{
                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        order_item_list_parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (mContext, MobileScreen_OrderDetails1.class);
                Bundle bundle = new Bundle();
                bundle.putString("From","MobileManageOrders");
                bundle.putParcelable("data", modal_manageOrders_pojo_class);
                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });



        ready_for_pickup_delivered_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Currenttime = getDate_and_time();
                mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
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




                changestatusto =Constants.DELIVERED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
                //Log.i("Tag",""+changestatusto+OrderKey);

                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

                getStockOutGoingDetailsUsingOrderid(orderid);
              //  mobile_manageOrders1.sorted_OrdersList.remove(pos);
               //  notifyDataSetChanged();
            }
        });




        generateTokenNo_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                    changestatusto = "";
                if (BluetoothPrintDriver.IsNoConnection()) {

                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Printer_is_Disconnected_want_to_generate_TokenNo,
                            R.string.Yes_Text, R.string.No_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                                    generatingTokenNo(vendorkey,orderDetailsKey,orderid,customerMobileNo);

                                }

                                @Override
                                public void onNo() {
                                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                }
                            });

                }

                if(!BluetoothPrintDriver.IsNoConnection()){
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (!mBluetoothAdapter.isEnabled()) {
                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Printer_is_Disconnected_want_to_generate_TokenNo,
                                R.string.Yes_Text, R.string.No_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                                        generatingTokenNo(vendorkey,orderDetailsKey, orderid, customerMobileNo);

                                    }

                                    @Override
                                    public void onNo() {
                                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                    }
                                });

                    }else{


                        mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                        generatingTokenNo(vendorkey,orderDetailsKey, orderid, customerMobileNo);

                    }

                }
                else{
                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                }

                //Log.i("tag","orderkey1"+ OrderKey);
            }
        });

/*
        transit_generateTokenNo_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                changestatusto = "";

                generatingTokenNo(vendorkey,orderDetailsKey);

                //Log.i("tag","orderkey1"+ OrderKey);


            }
        });



 */




        //1
        confirmed_Order_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (BluetoothPrintDriver.IsNoConnection()) {

                        Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Printer_is_Disconnected_want_to_generate_TokenNo,
                                R.string.Yes_Text, R.string.No_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        Currenttime = getDate_and_time();
                                        changestatusto =Constants.CONFIRMED_ORDER_STATUS;
                                        OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                                        //Log.i("tag","orderkey1"+ OrderKey);
                                        String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                                        String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                                        String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                                        String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                                        String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();

                                        //Log.i("Tag","0"+OrderKey);
                                        new_Order_Linearlayout.setVisibility(View.GONE);
                                        ready_Order_Linearlayout.setVisibility(View.GONE);
                                        confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                                        cancelled_Order_Linearlayout.setVisibility(View.GONE);
                                        if((tokenNofromArray.length()<=0)||(tokenNofromArray == null)||(tokenNofromArray.equals(""))) {
                                            generatingTokenNo(vendorkey, orderDetailsKey, orderid, customerMobileNo);
                                        }
                                        else{
                                            ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                                            if (BluetoothPrintDriver.IsNoConnection()) {

                                                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                                AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                                            }

                                            if(!BluetoothPrintDriver.IsNoConnection()){
                                                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                                if (!mBluetoothAdapter.isEnabled()) {
                                                    Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                                    AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                                                }
                                                else{
                                                    //AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Please_Wait_Until_Printer_Stop);

                                                    mobile_manageOrders1.printRecipt(modal_manageOrders_pojo_class);

                                                }
                                            }
                                            else{
                                                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                            }
                                        }
                                    }

                                    @Override
                                    public void onNo() {

                                    }
                                });
                    }

                    if(!BluetoothPrintDriver.IsNoConnection()){
                        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                        if (!mBluetoothAdapter.isEnabled()) {
                            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                            new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Printer_is_Disconnected_want_to_generate_TokenNo,
                                    R.string.Yes_Text, R.string.No_Text,
                                    new TMCAlertDialogClass.AlertListener() {
                                        @Override
                                        public void onYes() {
                                            Currenttime = getDate_and_time();
                                            changestatusto =Constants.CONFIRMED_ORDER_STATUS;
                                            OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                                            //Log.i("tag","orderkey1"+ OrderKey);
                                            String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                                            String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                                            String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
                                            String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                                            String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));

                                            //Log.i("Tag","0"+OrderKey);
                                            new_Order_Linearlayout.setVisibility(View.GONE);
                                            ready_Order_Linearlayout.setVisibility(View.GONE);
                                            confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                                            cancelled_Order_Linearlayout.setVisibility(View.GONE);
                                            if((tokenNofromArray.length()<=0)||(tokenNofromArray == null)||(tokenNofromArray.equals(""))) {
                                                generatingTokenNo(vendorkey, orderDetailsKey, orderid, customerMobileNo);
                                            }
                                            else{
                                                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                                                if (BluetoothPrintDriver.IsNoConnection()) {

                                                    Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                                    AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                                                }

                                                if(!BluetoothPrintDriver.IsNoConnection()){
                                                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                                    if (!mBluetoothAdapter.isEnabled()) {
                                                        Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                                        AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                                                    }
                                                    else{
                                                        //AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Please_Wait_Until_Printer_Stop);

                                                        mobile_manageOrders1.printRecipt(modal_manageOrders_pojo_class);

                                                    }
                                                }
                                                else{
                                                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                                }
                                            }
                                        }

                                        @Override
                                        public void onNo() {

                                        }
                                    });
                        }
                        else{
                            Currenttime = getDate_and_time();
                            changestatusto =Constants.CONFIRMED_ORDER_STATUS;
                            OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                            //Log.i("tag","orderkey1"+ OrderKey);
                            String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                            String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                            String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                            String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                            String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();

                            //Log.i("Tag","0"+OrderKey);
                            new_Order_Linearlayout.setVisibility(View.GONE);
                            ready_Order_Linearlayout.setVisibility(View.GONE);
                            confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                            cancelled_Order_Linearlayout.setVisibility(View.GONE);
                            if((tokenNofromArray.length()<=0)||(tokenNofromArray == null)||(tokenNofromArray.equals(""))) {
                                generatingTokenNo(vendorkey, orderDetailsKey, orderid, customerMobileNo);
                            }
                            else{
                                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                                if (BluetoothPrintDriver.IsNoConnection()) {

                                    Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                    AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                                }

                                if(!BluetoothPrintDriver.IsNoConnection()){
                                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                    if (!mBluetoothAdapter.isEnabled()) {
                                        Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                        AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                                    }
                                    else{
                                        //AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Please_Wait_Until_Printer_Stop);

                                        mobile_manageOrders1.printRecipt(modal_manageOrders_pojo_class);

                                    }
                                }
                                else{
                                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                }
                            }
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                //ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);

           //     mobile_manageOrders1.sorted_OrdersList.remove(pos);
               //mobile_manageOrders1.displayorderDetailsinListview(mobile_manageOrders1.orderStatus,mobile_manageOrders1.ordersList,mobile_manageOrders1.slottypefromSpinner);
            //    notifyDataSetChanged();
            }
        });
        cancel_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Currenttime = getDate_and_time();
                mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
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
                changestatusto =Constants.CANCELLED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
                //Log.i("Tag",""+changestatusto+OrderKey);

                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

            //    mobile_manageOrders1.sorted_OrdersList.remove(pos);
            }
        });



        ready_for_pickup_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    mobile_manageOrders1.Adjusting_Widgets_Visibility(true);

                changestatusto =Constants.READY_FOR_PICKUP_ORDER_STATUS;
                Currenttime = getDate_and_time();
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
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);

                String orderType = "";

                try{
                    orderType = (String.format("%s", modal_manageOrders_pojo_class.getOrderType()));
                    if(orderType.equals("")){
                        orderType = (String.format("%s", modal_manageOrders_pojo_class.getOrdertype()));

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(updateweightforonlineorders){

                    if(orderType.toUpperCase().equals(Constants.PhoneOrder)) {
                        ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                        modal_manageOrders_pojo_class.setOrderstatus(changestatusto);
                    }
                    else {


                        try {


                            JSONArray array = modal_manageOrders_pojo_class.getItemdesp();
                            //Log.i("tag","array.length()"+ array.length());
                            String b = array.toString();
                            modal_manageOrders_pojo_class.setItemdesp_string(b);
                            String itemDesp = "";
                            String subCtgyKey = "";
                            int pricePerKgItemCount = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject json = array.getJSONObject(i);
                                String menuIdFromItemDesp = "";
                                if (json.has("menuitemid")) {
                                    menuIdFromItemDesp = json.getString("menuitemid");
                                    for (int j = 0; j < mobile_manageOrders1.MenuItem.size(); j++) {
                                        String menuIdFromMenu = "", pricetypeforpos = "", inventoryDetails_String;
                                        try {

                                            menuIdFromMenu = mobile_manageOrders1.MenuItem.get(j).getMenuItemId().toString();
                                        } catch (Exception e) {
                                            menuIdFromMenu = "";
                                            e.printStackTrace();
                                        }
                                        if (menuIdFromItemDesp.equals(menuIdFromMenu)) {
                                            try {

                                                pricetypeforpos = mobile_manageOrders1.MenuItem.get(j).getPricetypeforpos().toString();
                                            } catch (Exception e) {
                                                pricetypeforpos = "tmcpriceperkg";
                                                e.printStackTrace();
                                            }

                                            try {

                                                inventoryDetails_String = String.valueOf(mobile_manageOrders1.MenuItem.get(j).getInventorydetails().toString());
                                                if (!inventoryDetails_String.equals("nil") && !inventoryDetails_String.equals("")) {
                                                    json.put("inventorydetailsstring", inventoryDetails_String);
                                                    json.put("inventorydetails", new JSONArray(inventoryDetails_String));


                                                } else {

                                                    json.put("inventorydetailsstring", "");
                                                    json.put("inventorydetails", new JSONArray());

                                                }

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }

                                            try {

                                                json.put("tmcctgykey", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getTmcctgykey().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("stockavldetailskey", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getKey_AvlDetails().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }

                                            try {

                                                json.put("stockbalance", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getStockbalance_AvlDetails().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }

                                            try {

                                                json.put("receivedstock", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getReceivedstock_AvlDetails().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }

                                            try {

                                                json.put("barcode", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getBarcode().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("stockincomingkey", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getStockincomingkey_AvlDetails().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("isitemAvailable", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getItemavailability_AvlDetails().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("allownegativestock", String.valueOf(mobile_manageOrders1.MenuItem.get(j).getAllownegativestock().toString()));

                                            } catch (Exception e) {
                                                inventoryDetails_String = "";
                                                e.printStackTrace();
                                            }


                                            if (pricetypeforpos.toString().toUpperCase().equals("TMCPRICEPERKG")) {
                                                pricePerKgItemCount++;
                                            }
                                            else{
                                                if((!inventoryDetails_String.equals("nil") && !inventoryDetails_String.equals(""))){
                                                    pricePerKgItemCount++;

                                                }
                                            }

                                            json.put("pricetypeforpos", pricetypeforpos);

                                        }


                                    }
                                    if (array.length() - i == 1) {
                                        if (pricePerKgItemCount > 0) {
                                            openBottomSheetToChangeWeight(changestatusto, vendorkey, OrderKey, orderid, customerMobileNo, Currenttime, array, modal_manageOrders_pojo_class.getTokenno());

                                        } else {
                                            ChangeStatusOftheOrder(changestatusto, vendorkey, OrderKey, orderid, customerMobileNo, Currenttime);
                                            modal_manageOrders_pojo_class.setOrderstatus(changestatusto);
                                        }
                                    }

                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                }
                else{
                     ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                     modal_manageOrders_pojo_class.setOrderstatus(changestatusto);

                }


             //   mobile_manageOrders1.sorted_OrdersList.remove(pos);
            }
        });




        pending_order_assignDeliveryperson_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));

                try {
                     deliverypartnerName = modal_manageOrders_pojo_class.getDeliveryPartnerName();
                     if(deliverypartnerName.equals(null)){
                         deliverypartnerName="null";

                     }
                    //Log.d(Constants.TAG, "deliverypartnerName: " + deliverypartnerName);

                }
                catch (Exception e){
                    e.printStackTrace();
                    deliverypartnerName="null";
                }
                try {
                    if ((!(deliverypartnerName.length() ==0))&&(!deliverypartnerName.equals("null"))  ) {
                        String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                        showBottomSheetDialog(Orderkey, deliverypartnerName,orderid,customerMobileNo,vendorkey);

                    } else {
                        String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                        showBottomSheetDialog(Orderkey, "null",orderid,customerMobileNo,vendorkey);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }




            }

        });
        other_assignDeliveryperson_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));

                try {
                    deliverypartnerName = modal_manageOrders_pojo_class.getDeliveryPartnerName();
                    if(deliverypartnerName.equals(null)){
                        deliverypartnerName="null";

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    deliverypartnerName="null";
                }
                if((!deliverypartnerName.equals("null"))&&(!deliverypartnerName.equals(null))) {
                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,deliverypartnerName,orderid,customerMobileNo,vendorkey);

                }
                else{
                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,"null",orderid,customerMobileNo,vendorkey);

                }


            }
        });

        cancelled_assignDeliveryperson_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                String orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                String customerMobileNo = (String.format("%s", modal_manageOrders_pojo_class.getUsermobile()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));

                try {
                    deliverypartnerName = modal_manageOrders_pojo_class.getDeliveryPartnerName();
                    if(deliverypartnerName.equals(null)){
                        deliverypartnerName="null";

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    deliverypartnerName="null";
                }
                if(!deliverypartnerName.equals("null")) {
                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,deliverypartnerName,orderid,customerMobileNo,vendorkey);

                }
                else{
                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,"null",orderid,customerMobileNo,vendorkey);

                }

            }
        });

        }
        catch (Exception e){
            notifyDataSetChanged();
            e.printStackTrace();
        }




        return  listViewItem ;

    }

    private void openBottomSheetToChangeWeight(String changestatusto, String vendorkey, String orderKey, String orderid, String customerMobileNo, String currenttime, JSONArray jsonArray, String tokenno) {

        changeWeight_dialog = new Dialog(mContext,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        changeWeight_dialog.setContentView(R.layout.change_weight_for_ordereditems_bottomsheet);
        changeWeight_dialog.setCanceledOnTouchOutside(false);
       LinearLayout listviewchilditemLayout = changeWeight_dialog.findViewById(R.id.listviewchilditemLayout);
        LinearLayout listviewParentLayout = changeWeight_dialog.findViewById(R.id.listviewParentLayout);
        LinearLayout loadingpanelmaskt = changeWeight_dialog.findViewById(R.id.loadingpanelmaskt);
        LinearLayout loadingPanel = changeWeight_dialog.findViewById(R.id.loadingPanel);
        listviewParentLayout.setVisibility(View.VISIBLE);
        listviewchilditemLayout.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.GONE);
        loadingpanelmaskt.setVisibility(View.GONE);



        ListView itemDesp_listview = changeWeight_dialog.findViewById(R.id.itemDesp_listview);
        Button changeReadyForPickup = changeWeight_dialog.findViewById(R.id.changeReadyForPickup);
        TextView tokenNo_textWidget = changeWeight_dialog.findViewById(R.id.tokenNo_textWidget);

        tokenNo_textWidget.setText(tokenno);
        try{

            OrderdItems_desp.clear();
            try {
                for(int i=0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    JSONArray inventorydetails = new JSONArray();
                            String quantityString = "",itemFinalWeight_String ="",subCtgyKey ="",pricetypeforpos ="",grossweightingrams ="",menuitemid ="",inventorydetailsstring ="",itemName = "";
                            double quantity_double =0,itemfinalWeight_double =0;
                    Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();

                    try {
                        if(json.has("pricetypeforpos")) {
                            pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));
                        }
                        else {
                            pricetypeforpos = " ";
                        }

                        manageOrders_pojo_class.pricetypeforpos = pricetypeforpos;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                      if (json.has("netweight")) {
                          manageOrders_pojo_class.netweight = String.valueOf(json.get("netweight"));

                      } else {
                          manageOrders_pojo_class.netweight = "";

                      }

                      try {
                          if (json.has("tmcsubctgykey")) {
                              subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                          } else {
                              subCtgyKey = " ";
                          }

                          manageOrders_pojo_class.tmcSubCtgyKey = subCtgyKey;
                      } catch (Exception e) {
                          e.printStackTrace();
                      }


                      try {
                          if (json.has("menuitemid")) {
                              menuitemid = String.valueOf(json.get("menuitemid"));
                          } else {
                              menuitemid = " ";
                          }

                          manageOrders_pojo_class.menuItemKey = menuitemid;
                      } catch (Exception e) {
                          e.printStackTrace();
                      }

                    try {
                        if (json.has("receivedstock")) {
                            manageOrders_pojo_class.receivedstock = String.valueOf(json.get("receivedstock"));
                        } else {
                            manageOrders_pojo_class.receivedstock = " ";
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if (json.has("barcode")) {
                            manageOrders_pojo_class.barcode = String.valueOf(json.get("barcode"));
                        } else {
                            manageOrders_pojo_class.barcode = " ";
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if (json.has("inventorydetailsstring")) {
                            inventorydetailsstring = String.valueOf(json.get("inventorydetailsstring"));
                        } else {
                            inventorydetailsstring = " ";
                        }

                        manageOrders_pojo_class.inventorydetailsstring = inventorydetailsstring;
                    } catch (Exception e) {
                        manageOrders_pojo_class.inventorydetailsstring = "";
                                e.printStackTrace();
                    }



                    try{
                        if (json.has("tmcctgykey")) {

                            manageOrders_pojo_class.tmcctgykey = String.valueOf(json.get("tmcctgykey"));
                        }
                        else{
                            manageOrders_pojo_class.tmcctgykey  ="";

                        }

                    }
                    catch (Exception e){
                        manageOrders_pojo_class.tmcctgykey  ="";
                        e.printStackTrace();
                    }


                    try{
                        if (json.has("stockavldetailskey")) {
                            manageOrders_pojo_class.stockavldetailskey = String.valueOf(json.get("stockavldetailskey"));

                        }
                        else{
                            manageOrders_pojo_class.stockavldetailskey ="";


                        }


                    }
                    catch (Exception e){
                        manageOrders_pojo_class.stockavldetailskey ="";
                        e.printStackTrace();
                    }

                    try{
                        if (json.has("stockbalance")) {
                            manageOrders_pojo_class.stockbalance = String.valueOf(json.get("stockbalance"));

                        }
                        else{
                            manageOrders_pojo_class.stockbalance = "";
                        }


                    }
                    catch (Exception e){
                        manageOrders_pojo_class.stockbalance  ="";
                        e.printStackTrace();
                    }


                    try{

                        if (json.has("stockincomingkey")) {

                            manageOrders_pojo_class.stockincomingkey = String.valueOf(json.get("stockincomingkey"));
                        }
                        else{
                            manageOrders_pojo_class.stockincomingkey ="";

                        }

                    }
                    catch (Exception e){
                        manageOrders_pojo_class.stockincomingkey ="";
                        e.printStackTrace();
                    }



                    try{

                        if (json.has("isitemAvailable")) {

                            manageOrders_pojo_class.isitemAvailable = String.valueOf(json.get("isitemAvailable"));
                        }
                        else{
                            manageOrders_pojo_class.isitemAvailable ="";

                        }

                    }
                    catch (Exception e){
                        manageOrders_pojo_class.isitemAvailable ="";
                        e.printStackTrace();
                    }



                    try{
                        if(json.has("allownegativestock")) {
                            manageOrders_pojo_class.allownegativestock = String.valueOf(json.get("allownegativestock"));
                        }
                        else{
                            manageOrders_pojo_class.isitemAvailable ="";

                        }

                    }
                    catch (Exception e){
                        manageOrders_pojo_class.isitemAvailable ="";
                        e.printStackTrace();
                    }







                    try {
                        if (json.has("inventorydetails")) {
                            inventorydetails = (JSONArray) json.get("inventorydetails");
                        } else {
                            inventorydetails = new JSONArray();
                        }

                        manageOrders_pojo_class.inventorydetails = inventorydetails;
                    } catch (Exception e) {
                        manageOrders_pojo_class.inventorydetails =  new JSONArray();
                        e.printStackTrace();
                    }




                    try {
                          if (json.has("grossweightingrams")) {
                              grossweightingrams = String.valueOf(json.get("grossweightingrams"));
                          } else {
                              grossweightingrams = " ";
                              if (json.has("grossweight")) {
                                  grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                              } else {
                                  grossweightingrams = " ";
                                  grossweightingrams = " ";
                                  if (json.has("weightingrams")) {
                                      grossweightingrams = String.valueOf(json.get("weightingrams"));

                                  } else {
                                      grossweightingrams = " ";
                                  }
                              }
                          }
                          try {

                              grossweightingrams = grossweightingrams.replace("[^\\d.]", "");
                          } catch (Exception e) {
                              grossweightingrams = "0";
                              e.printStackTrace();
                          }
                          manageOrders_pojo_class.grossweightingrams = grossweightingrams;
                     //     manageOrders_pojo_class.setItemFinalWeight(grossweightingrams);

                      } catch (Exception e) {
                          e.printStackTrace();
                      }

                      if (json.has("grossweight")) {
                          manageOrders_pojo_class.grossweight = String.valueOf(json.get("grossweight"));

                      } else {
                          manageOrders_pojo_class.grossweight = grossweightingrams;
                      }

                    try{
                        itemFinalWeight_String = grossweightingrams.replaceAll("[^\\d.]", "");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    try{
                        itemfinalWeight_double= Double.parseDouble(itemFinalWeight_String);
                    }
                    catch (Exception e){
                        itemfinalWeight_double = 1;
                        e.printStackTrace();
                    }
                    try {
                        if (json.has("quantity")) {
                            manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                            quantityString =  String.valueOf(json.get("quantity"));
                        } else {
                            manageOrders_pojo_class.quantity = "1";
                            quantityString = "1";
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try{
                        quantity_double= Double.parseDouble(quantityString);
                    }
                    catch (Exception e){
                        quantity_double  =1;
                        e.printStackTrace();
                    }

                    try{
                        itemfinalWeight_double = itemfinalWeight_double *  quantity_double ;
                    }
                    catch (Exception e){
                        itemfinalWeight_double  =1;
                        e.printStackTrace();
                    }

                    try{
                        manageOrders_pojo_class.setItemFinalWeight(String.valueOf(itemfinalWeight_double));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    if (json.has("cutname")) {
                        manageOrders_pojo_class.cutname = String.valueOf(json.get("cutname"));

                    } else {
                        manageOrders_pojo_class.cutname = "";
                    }



                      if (subCtgyKey.equals("tmcsubctgy_16")) {
                          //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                          manageOrders_pojo_class.itemName = "Grill House  " + String.valueOf(json.get("itemname"));
                          itemName = "Grill House  " + String.valueOf(json.get("itemname"));
                      } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                          // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                          manageOrders_pojo_class.itemName = "Ready to Cook " + String.valueOf(json.get("itemname"));
                          itemName = "Ready to Cook " + String.valueOf(json.get("itemname"));
                      } else {
                          manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));
                          itemName = String.valueOf(json.get("itemname"));
                      }
                      manageOrders_pojo_class.ItemFinalPrice = String.valueOf(json.get("tmcprice"));
                    //  manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                      manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));
                      manageOrders_pojo_class.isCorrectGrossweight = true;
                      manageOrders_pojo_class.isGrossweightEdited = false;



                    if(pricetypeforpos.toString().toUpperCase().equals("TMCPRICE")) {


                        if(inventorydetails.length()>0) {
                            try {

                                int jsonArrayIterator = 0;
                                int jsonArrayCount = inventorydetails.length();
                                for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {
                                    try {
                                        JSONObject json_InventoryDetails = inventorydetails.getJSONObject(jsonArrayIterator);



                                        String menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");





                                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < mobile_manageOrders1.MenuItem.size(); iterator_menuitemStockAvlDetails++) {

                                            Modal_MenuItem modal_menuItemInventoryDetailsItem = mobile_manageOrders1.MenuItem.get(iterator_menuitemStockAvlDetails);

                                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemInventoryDetailsItem.getKey());

                                            if (menuItemKeyFromInventoryDetails.equals(menuItemKeyFromMenuAvlDetails)) {

                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class1 = new Modal_ManageOrders_Pojo_Class();


                                                modal_manageOrders_pojo_class1.ischilditem = true;
                                                modal_manageOrders_pojo_class1.parentItemName = itemName;


                                                try {
                                                    if (json_InventoryDetails.has("grossweightingrams")) {
                                                        grossweightingrams = String.valueOf(json_InventoryDetails.get("grossweightingrams"));
                                                    } else {
                                                        grossweightingrams = " ";

                                                    }
                                                    try {

                                                        grossweightingrams = grossweightingrams.replace("[^\\d.]", "");
                                                    } catch (Exception e) {
                                                        grossweightingrams = "0";
                                                        e.printStackTrace();
                                                    }
                                                    modal_manageOrders_pojo_class1.grossweightingrams = grossweightingrams;

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                try{
                                                    modal_manageOrders_pojo_class1.grossweight = grossweightingrams;

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }



                                                try{
                                                    itemFinalWeight_String = grossweightingrams.replaceAll("[^\\d.]", "");
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                                try{
                                                    itemfinalWeight_double= Double.parseDouble(itemFinalWeight_String);
                                                }
                                                catch (Exception e){
                                                    itemfinalWeight_double = 1;
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    if (json.has("quantity")) {
                                                        modal_manageOrders_pojo_class1.quantity = String.valueOf(json.get("quantity"));
                                                        quantityString =  String.valueOf(json.get("quantity"));
                                                    } else {
                                                        modal_manageOrders_pojo_class1.quantity = "1";
                                                        quantityString = "1";
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try{
                                                    quantity_double= Double.parseDouble(quantityString);
                                                }
                                                catch (Exception e){
                                                    quantity_double  =1;
                                                    e.printStackTrace();
                                                }

                                                try{
                                                    itemfinalWeight_double = itemfinalWeight_double *  quantity_double ;
                                                }
                                                catch (Exception e){
                                                    itemfinalWeight_double  =1;
                                                    e.printStackTrace();
                                                }

                                                try{
                                                    modal_manageOrders_pojo_class1.setItemFinalWeight(String.valueOf(itemfinalWeight_double));
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }





                                                try {
                                                    if(json.has("pricetypeforpos")) {
                                                        pricetypeforpos = String.valueOf(modal_menuItemInventoryDetailsItem.getPricetypeforpos());
                                                    }
                                                    else {
                                                        pricetypeforpos = " ";
                                                    }

                                                    modal_manageOrders_pojo_class1.pricetypeforpos = pricetypeforpos;
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                                if (json.has("netweight")) {
                                                    modal_manageOrders_pojo_class1.netweight = String.valueOf(modal_menuItemInventoryDetailsItem.getNetweight());

                                                } else {
                                                    modal_manageOrders_pojo_class1.netweight = "";

                                                }

                                                try {
                                                    if (json.has("tmcsubctgykey")) {
                                                        subCtgyKey = String.valueOf(modal_menuItemInventoryDetailsItem.getTmcsubctgykey());
                                                    } else {
                                                        subCtgyKey = " ";
                                                    }

                                                    modal_manageOrders_pojo_class1.tmcSubCtgyKey = subCtgyKey;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("menuitemid")) {
                                                        menuitemid = String.valueOf(modal_menuItemInventoryDetailsItem.getMenuItemId());
                                                    } else {
                                                        menuitemid = " ";
                                                    }

                                                    modal_manageOrders_pojo_class1.menuItemKey = menuitemid;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("receivedstock")) {
                                                        modal_manageOrders_pojo_class1.receivedstock = String.valueOf(modal_menuItemInventoryDetailsItem.getReceivedstock_AvlDetails());
                                                    } else {
                                                        modal_manageOrders_pojo_class1.receivedstock = " ";
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("barcode")) {
                                                        modal_manageOrders_pojo_class1.barcode = String.valueOf(modal_menuItemInventoryDetailsItem.getBarcode());
                                                    } else {
                                                        modal_manageOrders_pojo_class1.barcode = " ";
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }



                                                try{
                                                    modal_manageOrders_pojo_class1.tmcctgykey = String.valueOf(modal_menuItemInventoryDetailsItem.getTmcctgykey());


                                                }
                                                catch (Exception e){
                                                    modal_manageOrders_pojo_class1.tmcctgykey  ="";
                                                    e.printStackTrace();
                                                }


                                                try{
                                                    modal_manageOrders_pojo_class1.stockavldetailskey = String.valueOf(modal_menuItemInventoryDetailsItem.getKey_AvlDetails());


                                                }
                                                catch (Exception e){
                                                    modal_manageOrders_pojo_class1.stockavldetailskey ="";
                                                    e.printStackTrace();
                                                }

                                                try{
                                                    modal_manageOrders_pojo_class1.stockbalance = String.valueOf(modal_menuItemInventoryDetailsItem.getStockbalance_AvlDetails());


                                                }
                                                catch (Exception e){
                                                    modal_manageOrders_pojo_class1.stockbalance  ="";
                                                    e.printStackTrace();
                                                }


                                                try{
                                                    modal_manageOrders_pojo_class1.stockincomingkey = String.valueOf(modal_menuItemInventoryDetailsItem.getStockincomingkey_AvlDetails());


                                                }
                                                catch (Exception e){
                                                    modal_manageOrders_pojo_class1.stockincomingkey ="";
                                                    e.printStackTrace();
                                                }



                                                try{
                                                    modal_manageOrders_pojo_class1.isitemAvailable = String.valueOf(modal_menuItemInventoryDetailsItem.getItemavailability_AvlDetails());


                                                }
                                                catch (Exception e){
                                                    modal_manageOrders_pojo_class1.isitemAvailable ="";
                                                    e.printStackTrace();
                                                }



                                                try{
                                                    modal_manageOrders_pojo_class1.allownegativestock = String.valueOf(modal_menuItemInventoryDetailsItem.getAllownegativestock());


                                                }
                                                catch (Exception e){
                                                    modal_manageOrders_pojo_class1.isitemAvailable ="";
                                                    e.printStackTrace();
                                                }






                                               modal_manageOrders_pojo_class1.ItemFinalPrice = String.valueOf(modal_menuItemInventoryDetailsItem.getTmcprice());
                                           //    modal_manageOrders_pojo_class1.quantity = String.valueOf(json.get("quantity"));
                                               modal_manageOrders_pojo_class1.GstAmount = String.valueOf(json.get("gstamount"));
                                               modal_manageOrders_pojo_class1.isCorrectGrossweight = true;
                                               modal_manageOrders_pojo_class1.isGrossweightEdited = false;

                                                modal_manageOrders_pojo_class1.inventorydetailsstring ="";
                                                modal_manageOrders_pojo_class1.inventorydetails = new JSONArray();






                                                if (subCtgyKey.equals("tmcsubctgy_16")) {
                                                    //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                                                    modal_manageOrders_pojo_class1.itemName = "Grill House  " + String.valueOf(modal_menuItemInventoryDetailsItem.getItemname());

                                                } else if (subCtgyKey.equals("tmcsubctgy_15")) {
                                                    // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                                                    modal_manageOrders_pojo_class1.itemName = "Ready to Cook " + String.valueOf(modal_menuItemInventoryDetailsItem.getItemname());

                                                } else {
                                                    modal_manageOrders_pojo_class1.itemName = String.valueOf(modal_menuItemInventoryDetailsItem.getItemname());

                                                }

                                                OrderdItems_desp.add(modal_manageOrders_pojo_class1);

                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            OrderdItems_desp.add(manageOrders_pojo_class);

                        }


                    }
                    else{
                        OrderdItems_desp.add(manageOrders_pojo_class);

                    }




                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Adapter_Mobile_changeWeight_in_itemDesp adapter_forOrderDetails_listview = new Adapter_Mobile_changeWeight_in_itemDesp(mContext, OrderdItems_desp,"MobileManageOrder",mobile_manageOrders1);
        itemDesp_listview.setAdapter(adapter_forOrderDetails_listview);


        changeReadyForPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Date c = Calendar.getInstance().getTime();


                    List<Modal_ManageOrders_Pojo_Class> orderdItems_desp_local = new ArrayList<>();
                    orderdItems_desp_local.clear();
                    changeWeight_dialog.hide();
                    mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
                    for(int i =0; i<OrderdItems_desp.size(); i++){

                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = OrderdItems_desp.get(i);
                            boolean isCorrectGrossweight = false , isGrossweightEdited = false ;
                            String pricetypeforPOS ="",finalWeight = "", grossweight ="",quantity ="";
                            double finalWeight_double =0, grossweight_double =0,quantity_double =0;
                        try{
                            isCorrectGrossweight = modal_manageOrders_pojo_class.isCorrectGrossweight();

                        }
                        catch (Exception e){
                            isCorrectGrossweight = false;
                            e.printStackTrace();
                        }
                        if(!isCorrectGrossweight){
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);
                            changeWeight_dialog.show();
                            AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.checkGrossweightCheckboxInstruction);
                            break;
                        }
                        try{
                            isGrossweightEdited  = modal_manageOrders_pojo_class.isGrossweightEdited();
                        }
                        catch (Exception e){
                            isGrossweightEdited = false ;
                            e.printStackTrace();
                        }


                        try{
                            pricetypeforPOS = String.valueOf(modal_manageOrders_pojo_class.getPricetypeforpos().toLowerCase());
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                        try{
                            finalWeight    = String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight());

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                        try{
                            grossweight    = String.valueOf(modal_manageOrders_pojo_class.getGrossweightingrams());

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            quantity    = String.valueOf(modal_manageOrders_pojo_class.getQuantity());

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            grossweight = grossweight.replaceAll("[^\\d.]", "");

                            if(!grossweight.equals("")) {
                                grossweight_double = Double.parseDouble(grossweight);
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                        try{
                            quantity = quantity.replaceAll("[^\\d.]", "");

                            if(!quantity.equals("")) {
                                quantity_double = Double.parseDouble(quantity);
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{

                            grossweight_double = grossweight_double *quantity_double;
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            finalWeight = finalWeight.replaceAll("[^\\d.]", "");

                            if(!finalWeight.equals("")) {

                                finalWeight_double = Double.parseDouble(finalWeight);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                        try{
                            if(pricetypeforPOS.toLowerCase().equals("tmcpriceperkg")) {
                                if (isCorrectGrossweight) {
                                    if (isGrossweightEdited) {
                                        if (finalWeight_double != grossweight_double) {
                                          //  Toast.makeText(mContext, modal_manageOrders_pojo_class.getItemName(), Toast.LENGTH_SHORT).show();
                                            orderdItems_desp_local.add(modal_manageOrders_pojo_class);

                                        } else {
                                        }
                                    } else {


                                    }
                                } else {
                                    Toast.makeText(mContext, "Please select the Is Current Grossweight is correct check box ", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                        if(OrderdItems_desp.size() -i ==1) {
                            if (orderdItems_desp_local.size() > 0) {
                                CalculateStockBalanceAndAddStockBalaHistory_OutgngDetails(changestatusto, vendorkey, orderid, customerMobileNo, Currenttime, orderdItems_desp_local);

                            }
                            else{
                                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);
                                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Grossweight_is_not_changed,
                                        R.string.Yes_Text,R.string.No_Text,
                                        new TMCAlertDialogClass.AlertListener() {
                                            @Override
                                            public void onYes() {

                                                new_Order_Linearlayout.setVisibility(View.GONE);
                                                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                                                confirming_order_Linearlayout.setVisibility(View.GONE);
                                                cancelled_Order_Linearlayout.setVisibility(View.GONE);
                                                //mobile_manageOrders1.Adjusting_Widgets_Visibility(false);
                                                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

                                                Toast.makeText(mContext, "Pls note Grossweight is not changed ", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNo() {
                                                Toast.makeText(mContext, "Pls change grossweight and change status ", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }
                        }







                    }



                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });



        changeWeight_dialog.show();




    }





    private void CalculateStockBalanceAndAddStockBalaHistory_OutgngDetails(String changestatusto, String vendorkey, String orderid, String customerMobileNo, String currenttime, List<Modal_ManageOrders_Pojo_Class> orderdItems_desp) {
        Date c = Calendar.getInstance().getTime();



        mResultCallback_Add_UpdateInventoryEntriesInterface = new Add_UpdateInventoryDetailsEntries_Interface(){

            @Override
            public void notifySuccess(String requestType, String success) {
                changeWeight_dialog.dismiss();
               // Toast.makeText(mContext, "ddddsuccess  " +success, Toast.LENGTH_SHORT).show();
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);
                //mobile_manageOrders1.Adjusting_Widgets_Visibility(false);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                    }
                });

                Date c = Calendar.getInstance().getTime();


            }

            @Override
            public void notifyError(String requestType, String error) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "error  " +error, Toast.LENGTH_SHORT).show();

                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);
                        AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Error_in_updating_GrossWeight);
                    }
                });

              //  ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

            }
        };


        Add_UpdateInventoryDetailsEntries_AsyncTask asyncTask=new  Add_UpdateInventoryDetailsEntries_AsyncTask(mContext, mResultCallback_Add_UpdateInventoryEntriesInterface,vendorkey,orderid,customerMobileNo,currenttime,orderdItems_desp,mobile_manageOrders1.MenuItem);
        asyncTask.execute();







    }


    private void getStockOutGoingDetailsUsingOrderid(String orderid) {



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getStockOutgoingUsingSalesOrderid+orderid ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                            try {


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
                                        String entryKey = json.getString("key");


                                        ChangeOutGoingTypeInOutgoingTable(entryKey);







                                    } catch (JSONException e) {
                                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);


                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(mContext, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();
                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);



                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();
                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





    }




    private void ChangeOutGoingTypeInOutgoingTable(String entryKey) {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("outgoingtype", Constants.SALES_FULFILLED_OUTGOINGTYPE);
            jsonObject.put("key", entryKey);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateStockOutgoingUsingKey,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);







    }







    private boolean checkStatusForTheOrder(String orderid) {
        isCancelledinsidefunctionboolean = false;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,Constants.api_GetTrackingOrderDetails_orderid+orderid,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                Log.d(Constants.TAG, "Status response: " + response);
                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for(int i = 0 ;i<jsonArray.length();i++){
                        JSONObject json = jsonArray.getJSONObject(i);

                        String orderStatus = json.getString("orderstatus");
                        if(orderStatus.toUpperCase().equals(Constants.CANCELLED_ORDER_STATUS)){
                            isCancelledinsidefunctionboolean = true;

                        }
                        else{
                            isCancelledinsidefunctionboolean = false;

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Token_No_Error_Instruction,
                        R.string.OK_Text,R.string.Empty_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                Toast.makeText(mContext,"Can't Refresh !!! Please Refresh Again",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNo() {

                            }
                        });
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




        return isCancelledinsidefunctionboolean;


    }


    private void showBottomSheetDialog(String orderkey, String deliverypartnerName, String orderid, String customerMobileNo, String vendorkey) {
        try {
            bottomSheetDialog = new BottomSheetDialog(mContext);
            bottomSheetDialog.setContentView(R.layout.mobilescreen_assigndeliverypartner_bottom_sheet_dialog);
             ListView ListView1 = bottomSheetDialog.findViewById(R.id.listview);

            Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(mContext, mobile_manageOrders1.deliveryPartnerList, orderkey, "MobileManageOrders", deliverypartnerName,orderid,customerMobileNo,vendorkey);

            ListView1.setAdapter(adapter_mobile_assignDeliveryPartner1);
            //mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

            bottomSheetDialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }





    private void generatingTokenNo(String vendorkey, String orderDetailsKey, String orderid, String customerMobileNo) {
        mobile_manageOrders1.Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_generateTokenNo+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {


                //Log.d(Constants.TAG, "api: " + Constants.api_generateTokenNo+vendorkey);

                //Log.d(Constants.TAG, "Responsewwwww: " + response);
                try {
                    String tokenNo = response.getString("tokenNumber");
                    UpdateTokenNoInOrderDetails(tokenNo,orderDetailsKey,orderid,customerMobileNo,vendorkey);
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
                    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
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

    private void UpdateTokenNoInOrderDetails(String tokenNo, String orderDetailsKey, String orderid, String customerMobileNo, String vendorkey) {
        mobile_manageOrders1.Adjusting_Widgets_Visibility(true);

       /* JSONObject  jsonObject = new JSONObject();
        try {

                jsonObject.put("key", orderDetailsKey);
                jsonObject.put("tokenno", tokenNo);
                //Log.i("tag","listenertoken"+ "");


        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d(Constants.TAG, "JSONOBJECT: " + e);
            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

        }

        */


        JSONObject jsonObject = new JSONObject();
        String Api_toChangeOrderDetailsUsingOrderid = "";


        if(orderdetailsnewschema){
            if(orderid.length()>1 && vendorkey.length()>1 && customerMobileNo.length()>1){
                try {
                    jsonObject.put("tokenno", tokenNo);
                    jsonObject.put("vendorkey", vendorkey);
                    jsonObject.put("orderid", orderid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Api_toChangeOrderDetailsUsingOrderid = Constants.api_UpdateVendorOrderDetails+ "?vendorkey="+vendorkey+"&orderid="+orderid;
                JSONObject customerDetails_JsonObject = new JSONObject();

                try {
                    customerDetails_JsonObject.put("tokenno", tokenNo);
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
                Toast.makeText(mContext, "orderid :"+orderid+" , vendorkey: "+vendorkey+" , customerMobileNo : "+ customerMobileNo, Toast.LENGTH_SHORT).show();
            }

        }
        else {
            try {
                jsonObject.put("key", orderDetailsKey);
                jsonObject.put("tokenno", tokenNo);


            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(Constants.TAG, "JSONOBJECT: " + e);


            }
            Log.d(Constants.TAG, "Request Payload: " + jsonObject);



            Api_toChangeOrderDetailsUsingOrderid = Constants.api_Update_OrderDetails;

        }






        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);
//"?key="+OrderKey+"&orderstatus="+changestatusto+"&currentTime="+Currenttime
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_toChangeOrderDetailsUsingOrderid,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i = 0; i< mobile_manageOrders1.ordersList.size(); i++){
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= mobile_manageOrders1.ordersList.get(i);
                    String orderid_local = modal_manageOrders_pojo_class.getOrderid();
                    if(orderid.equals(orderid_local)){
                        modal_manageOrders_pojo_class.setTokenno(tokenNo);

                        try {
                            for (int sortedarrayIterator = 0; sortedarrayIterator < Mobile_ManageOrders1.sorted_OrdersList.size(); sortedarrayIterator++) {
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_sortedOrdersList = Mobile_ManageOrders1.sorted_OrdersList.get(sortedarrayIterator);
                                String sortedArrayorderid_local = modal_manageOrders_pojo_class_sortedOrdersList.getOrderid();
                                if (orderid.equals(sortedArrayorderid_local)) {
                                    modal_manageOrders_pojo_class_sortedOrdersList.setTokenno(tokenNo);

                                }
                            }
                        }
                        catch (Exception e){
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                            mobile_manageOrders1.displayorderDetailsinListview(mobile_manageOrders1.orderStatus,mobile_manageOrders1.ordersList,mobile_manageOrders1.slottypefromSpinner);

                            e.printStackTrace();
                        }


                        if(changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                            ChangeStatusOftheOrder(changestatusto, vendorkey,OrderKey,orderid,customerMobileNo, Currenttime);
                        }
                        else{
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);
                            notifyDataSetChanged();

                            //mobile_manageOrders1.displayorderDetailsinListview(mobile_manageOrders1.orderStatus,mobile_manageOrders1.ordersList,mobile_manageOrders1.slottypefromSpinner);

                        }
                        if (BluetoothPrintDriver.IsNoConnection()) {

                            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                            AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                        }
                        mobile_manageOrders1.Adjusting_Widgets_Visibility(true);

                        if(!BluetoothPrintDriver.IsNoConnection()){
                            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                            if (!mBluetoothAdapter.isEnabled()) {
                                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                                AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Printer_is_Disconnected);

                            }
                            else{
                               //AlertDialogClass.showDialog(mobile_manageOrders1.getActivity(),R.string.Please_Wait_Until_Printer_Stop);
                                    mobile_manageOrders1.printRecipt(modal_manageOrders_pojo_class);
                                

                            }




                        }
                        else{
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                        }
                    }
                }

                //Log.d(Constants.TAG, "Responsewwwww: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
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
    private void initUpdateCustomerOrderDetailsInterface(Context mContext) {

        mResultCallback_UpdateCustomerOrderDetailsTableInterface  = new Update_CustomerOrderDetails_TrackingTableInterface() {
            @Override
            public void notifySuccess(String requestType, String success) {
                try{
                    Toast.makeText(mContext, "Succesfully Updated the token no in Customer Details", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                try{
                    Toast.makeText(mContext, "Failed to Updated the token No in Customer Details", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
    }


    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();


        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy" ,Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
    }

    private void ChangeStatusOftheOrder(String changestatusto, String vendorkey, String orderkey, String orderid, String customerMobileNo, String currenttime) {
        mobile_manageOrders1.Adjusting_Widgets_Visibility(true);
        JSONObject jsonObject = new JSONObject();
        String Api_toChangeOrderDetailsUsingOrderid = "";

        try {


            if(orderdetailsnewschema){
                if(orderid.length()>1 && vendorkey.length()>1 && customerMobileNo.length()>1){
                    try {


                        if (changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                            jsonObject.put("vendorkey", vendorkey);
                            jsonObject.put("orderstatus", changestatusto);
                            jsonObject.put("orderconfirmedtime", currenttime);
                            jsonObject.put("orderid", orderid);

                            //Log.i("tag","listenertoken"+ "");
                        }
                        if (changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                            jsonObject.put("vendorkey", vendorkey);
                            jsonObject.put("orderstatus", changestatusto);
                            jsonObject.put("orderid", orderid);
                            jsonObject.put("orderreadytime", currenttime);

                            //Log.i("tag","listenertoken"+ "");
                        }
                        if (changestatusto.equals(Constants.CANCELLED_ORDER_STATUS)) {
                            jsonObject.put("vendorkey", vendorkey);
                            jsonObject.put("orderstatus", changestatusto);
                            jsonObject.put("ordercancelledtime", currenttime);
                            jsonObject.put("orderid", orderid);
                            //Log.i("tag","listenertoken"+ "");
                        }
                        if (changestatusto.equals(Constants.DELIVERED_ORDER_STATUS)) {
                            jsonObject.put("vendorkey", vendorkey);
                            jsonObject.put("orderstatus", changestatusto);
                            jsonObject.put("orderdeliverytime", currenttime);
                            jsonObject.put("orderid", orderid);
                            ////Log.i("tag","listenertoken"+ "");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Api_toChangeOrderDetailsUsingOrderid = Constants.api_UpdateVendorTrackingOrderDetails+ "?vendorkey="+vendorkey+"&orderid="+orderid;
                    JSONObject customerDetails_JsonObject = new JSONObject();

                    try {


                        if (changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                            customerDetails_JsonObject.put("usermobileno", customerMobileNo);
                            customerDetails_JsonObject.put("orderstatus", changestatusto);
                            customerDetails_JsonObject.put("orderconfirmedtime", currenttime);
                            customerDetails_JsonObject.put("orderid", orderid);
                            //Log.i("tag","listenertoken"+ "");
                        }
                        if (changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                            customerDetails_JsonObject.put("usermobileno", customerMobileNo);

                            customerDetails_JsonObject.put("orderstatus", changestatusto);
                            customerDetails_JsonObject.put("orderid", orderid);
                            customerDetails_JsonObject.put("orderreadytime", currenttime);

                            //Log.i("tag","listenertoken"+ "");
                        }
                        if (changestatusto.equals(Constants.CANCELLED_ORDER_STATUS)) {
                            customerDetails_JsonObject.put("usermobileno", customerMobileNo);
                            customerDetails_JsonObject.put("orderstatus", changestatusto);
                            customerDetails_JsonObject.put("ordercancelledtime", currenttime);
                            customerDetails_JsonObject.put("orderid", orderid);
                            //Log.i("tag","listenertoken"+ "");
                        }
                        if (changestatusto.equals(Constants.DELIVERED_ORDER_STATUS)) {
                            customerDetails_JsonObject.put("usermobileno", customerMobileNo);
                            customerDetails_JsonObject.put("orderstatus", changestatusto);
                            customerDetails_JsonObject.put("orderdeliverytime", currenttime);
                            customerDetails_JsonObject.put("orderid", orderid);
                            ////Log.i("tag","listenertoken"+ "");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
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


                if (changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                    jsonObject.put("key", orderkey);
                    jsonObject.put("orderstatus", changestatusto);
                    jsonObject.put("orderconfirmedtime", currenttime);

                    //Log.i("tag","listenertoken"+ "");
                }
                if (changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                    jsonObject.put("key", orderkey);
                    jsonObject.put("orderstatus", changestatusto);

                    jsonObject.put("orderreadytime", currenttime);

                    //Log.i("tag","listenertoken"+ "");
                }
                if (changestatusto.equals(Constants.CANCELLED_ORDER_STATUS)) {
                    jsonObject.put("key", orderkey);
                    jsonObject.put("orderstatus", changestatusto);
                    jsonObject.put("ordercancelledtime", currenttime);

                    //Log.i("tag","listenertoken"+ "");
                }
                if (changestatusto.equals(Constants.DELIVERED_ORDER_STATUS)) {
                    jsonObject.put("key", orderkey);
                    jsonObject.put("orderstatus", changestatusto);
                    jsonObject.put("orderdeliverytime", currenttime);
                    ////Log.i("tag","listenertoken"+ "");
                }
            }






        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);
//"?key="+OrderKey+"&orderstatus="+changestatusto+"&currentTime="+Currenttime
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_toChangeOrderDetailsUsingOrderid,
                jsonObject, new Response.Listener<JSONObject>() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i = 0; i< mobile_manageOrders1.ordersList.size(); i++){
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= mobile_manageOrders1.ordersList.get(i);
                    String orderidfromtrackingDetails = modal_manageOrders_pojo_class.getOrderid();
                    if(orderid.equals(orderidfromtrackingDetails)){
                        modal_manageOrders_pojo_class.setOrderstatus(changestatusto);

                        if(changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                            modal_manageOrders_pojo_class.setOrderconfirmedtime(Currenttime);
                            modal_manageOrders_pojo_class.setOrderconfirmedtime_in_long(String.valueOf(mobile_manageOrders1.getLongValuefortheDate(Currenttime)));
                        }
                        if(changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                            modal_manageOrders_pojo_class.setOrderreadytime(Currenttime);
                            modal_manageOrders_pojo_class.setOrderreadytime_in_long(String.valueOf(mobile_manageOrders1.getLongValuefortheDate(Currenttime)));

                        }




                        try {
                            for (int sortedarrayIterator = 0; sortedarrayIterator < Mobile_ManageOrders1.sorted_OrdersList.size(); sortedarrayIterator++) {
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_sortedOrdersList =Mobile_ManageOrders1.sorted_OrdersList.get(sortedarrayIterator);
                                String sortedArrayorderid = modal_manageOrders_pojo_class_sortedOrdersList.getOrderid();
                                if (orderid.equals(sortedArrayorderid)) {
                                    modal_manageOrders_pojo_class_sortedOrdersList.setOrderstatus(changestatusto);
                                    if (changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                                        modal_manageOrders_pojo_class_sortedOrdersList.setOrderconfirmedtime(Currenttime);
                                        modal_manageOrders_pojo_class_sortedOrdersList.setOrderconfirmedtime_in_long(String.valueOf(mobile_manageOrders1.getLongValuefortheDate(Currenttime)));
                                        int neworderCount = mobile_manageOrders1.newCount-1;

                                        if(neworderCount>0) {
                                            mobile_manageOrders1.mobile_new_Order_widget.setText(String.format("%s ( %d )", Constants.NEW_ORDER_STATUS, neworderCount));
                                            mobile_manageOrders1.newCount=neworderCount;
                                        }
                                        else{
                                            mobile_manageOrders1.mobile_new_Order_widget.setText(String.format("%s", Constants.NEW_ORDER_STATUS));

                                        }

                                        int ConfirmedorderCount = mobile_manageOrders1.confirmedCount+1;

                                        if(ConfirmedorderCount>0) {
                                            mobile_manageOrders1.mobile_confirmed_Order_widget.setText(String.format("%s ( %d )", Constants.CONFIRMED_ORDER_STATUS, ConfirmedorderCount));
                                            mobile_manageOrders1.confirmedCount=ConfirmedorderCount;

                                        }
                                        else{
                                            mobile_manageOrders1.mobile_confirmed_Order_widget.setText(String.format("%s", Constants.CONFIRMED_ORDER_STATUS));

                                        }



                                    }
                                    if (changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                                        modal_manageOrders_pojo_class_sortedOrdersList.setOrderreadytime(Currenttime);
                                        modal_manageOrders_pojo_class_sortedOrdersList.setOrderreadytime_in_long(String.valueOf(mobile_manageOrders1.getLongValuefortheDate(Currenttime)));
                                        int orderCount = mobile_manageOrders1.confirmedCount-1;

                                        if(orderCount>0) {
                                            mobile_manageOrders1.mobile_confirmed_Order_widget.setText(String.format("%s ( %d )", Constants.CONFIRMED_ORDER_STATUS, orderCount));
                                            mobile_manageOrders1.confirmedCount =orderCount;
                                        }
                                        else{
                                            mobile_manageOrders1.mobile_confirmed_Order_widget.setText(String.format("%s", Constants.CONFIRMED_ORDER_STATUS));

                                        }
                                        int readyorderCount = mobile_manageOrders1.readyForPickupCount+1;

                                        //3
                                        if(readyorderCount>0) {
                                            mobile_manageOrders1.mobile_ready_Order_widget.setText(String.format("%s ( %d )", Constants.READY_FOR_PICKUP_ORDER_STATUS, readyorderCount));
                                            mobile_manageOrders1.readyForPickupCount =readyorderCount;
                                        }
                                        else{
                                            mobile_manageOrders1.mobile_ready_Order_widget.setText(String.format("%s", Constants.READY_FOR_PICKUP_ORDER_STATUS));

                                        }

                                    }
                                    Mobile_ManageOrders1.sorted_OrdersList.remove(sortedarrayIterator);

                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                            mobile_manageOrders1.displayorderDetailsinListview(mobile_manageOrders1.orderStatus,mobile_manageOrders1.ordersList,mobile_manageOrders1.slottypefromSpinner);

                        }
                        notifyDataSetChanged();
                        mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                    }
                }
                //Log.d(Constants.TAG, "Responsewwwww: " + response);
               // mobile_manageOrders1.displayorderDetailsinListview(mobile_manageOrders1.orderStatus,mobile_manageOrders1.ordersList,mobile_manageOrders1.slottypefromSpinner);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
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
 /*    if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
            if (deliverytype.equals(Constants.STOREPICKUP_DELIVERYTYPE)) {
                ready_for_pickup_button_widget.setVisibility(View.GONE);
                ready_for_pickup_delivered_button_widget.setVisibility(View.VISIBLE);
                deliveryTypeLayout.setVisibility(View.VISIBLE);
                try {
                    deliveryType_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getDeliverytype()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
            else{
                ready_for_pickup_button_widget.setVisibility(View.VISIBLE);
                ready_for_pickup_delivered_button_widget.setVisibility(View.GONE);
                deliveryTypeLayout.setVisibility(View.GONE);

            }
        }
        else{
            ready_for_pickup_button_widget.setVisibility(View.VISIBLE);
            ready_for_pickup_delivered_button_widget.setVisibility(View.GONE);
            deliveryTypeLayout.setVisibility(View.GONE);

        }
        */