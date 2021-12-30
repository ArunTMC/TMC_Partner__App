package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
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
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_Mobile_AssignDeliveryPartner1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    String changestatusto,orderStatus,OrderKey,deliveryPersonName="";
    String Currenttime,MenuItems,orderStatusFromArray,FormattedTime,CurrentDate,formattedDate,CurrentDay,deliverytype;
    public searchOrdersUsingMobileNumber searchOrdersUsingMobileNumber;
    public  Pos_Orders_List Pos_Orders_List;
    public static  BottomSheetDialog bottomSheetDialog;
    String deliverypartnerName,orderType,CalledFrom;
    BluetoothAdapter mBluetoothAdapter =null;
    String orderPlacedTime ="";
    boolean isOrderPlacedlessThan3MinsBefore = true;

    public Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, searchOrdersUsingMobileNumber searchOrdersUsingMobileNumber, String orderStatus) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);
        CalledFrom = "AppOrdersList";
        this.searchOrdersUsingMobileNumber = searchOrdersUsingMobileNumber;
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.orderStatus=orderStatus;

    }
    public  Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, Pos_Orders_List Pos_Orders_List) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);
        CalledFrom = "PosOrdersList";

        this.Pos_Orders_List =  Pos_Orders_List;
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.orderStatus="DELIVERED";

    }

    public Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Context mContext, List<Modal_ManageOrders_Pojo_Class> sorted_ordersList, String orderStatus) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  sorted_ordersList);

        this.mContext=mContext;
        this.ordersList=sorted_ordersList;
        this.orderStatus="DELIVERED";
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


        final CardView cardLayout =listViewItem.findViewById(R.id.cardLayout);

     //   final Button changeDeliveryPartner =listViewItem.findViewById(R.id.changeDeliveryPartner);

        //
        final TextView orderid_text_widget = listViewItem.findViewById(R.id.orderid_text_widget);

        final TextView moblieNo_text_widget = listViewItem.findViewById(R.id.moblieNo_text_widget);
        final TextView tokenNo_text_widget = listViewItem.findViewById(R.id.tokenNo_text_widget);
        final TextView orderDetails_text_widget = listViewItem.findViewById(R.id.orderDetails_text_widget);
        final TextView ordertype_text_widget = listViewItem.findViewById(R.id.ordertype_text_widget);
        final TextView slotName_text_widget = listViewItem.findViewById(R.id.slotname_text_widget);
        final TextView slottime_text_widget = listViewItem.findViewById(R.id.slottime_text_widget);
        final TextView slotdate_text_widget = listViewItem.findViewById(R.id.slotdate_text_widget);
        final TextView orderstatus_text_widget = listViewItem.findViewById(R.id.orderstatus_text_widget);
        final Button ready_for_pickup_delivered_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_delivered_button_widget);
        final TextView deliveryType_text_widget = listViewItem.findViewById(R.id.deliveryType_text_widget);

        final TextView slotdate_label_widget = listViewItem.findViewById(R.id.slotdate_label_widget);

        final LinearLayout order_item_list_parentLayout =listViewItem.findViewById(R.id.order_item_list_parentLayout);
        final LinearLayout deliveryTypeLayout =listViewItem.findViewById(R.id.deliveryTypeLayout);
        final LinearLayout slotNameLayout =listViewItem.findViewById(R.id.slotNameLayout);

        final LinearLayout slotDateoLayout =listViewItem.findViewById(R.id.slotDateoLayout);

        final LinearLayout new_Order_Linearlayout =listViewItem.findViewById(R.id.new_Order_Linearlayout);
        final LinearLayout confirming_order_Linearlayout =listViewItem.findViewById(R.id.confirming_order_Linearlayout);
        final LinearLayout ready_Order_Linearlayout =listViewItem.findViewById(R.id.ready_Order_Linearlayout);
        final LinearLayout cancelled_Order_Linearlayout =listViewItem.findViewById(R.id.cancelled_Order_Linearlayout);

        final LinearLayout ordertypeLayout =listViewItem.findViewById(R.id.ordertypeLayout);
        final LinearLayout tokenNoLayout =listViewItem.findViewById(R.id.tokenNoLayout);

        final LinearLayout slotTimeLayout =listViewItem.findViewById(R.id.slotTimeLayout);

        final Button confirmed_Order_button_widget = listViewItem.findViewById(R.id.accept_Order_button_widget);
        final Button cancel_button_widget = listViewItem.findViewById(R.id.cancel_button_widget);

        final Button ready_for_pickup_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_button_widget);
        final Button pending_order_assignDeliveryperson_button_widget = listViewItem.findViewById(R.id.pending_order_assignDeliveryperson_button_widget);

        final Button other_assignDeliveryperson_button_widget = listViewItem.findViewById(R.id.other_assignDeliveryperson_button_widget);
        final Button cancelled_assignDeliveryperson_button_widget = listViewItem.findViewById(R.id.cancelled_assignDeliveryperson_button_widget);
        final Button generateTokenNo_button_widget = listViewItem.findViewById(R.id.generateTokenNo_button_widget);


        final RelativeLayout totalButtonLayout =listViewItem.findViewById(R.id.buttonsRelativeLayout);

        final LinearLayout ordercancellationtimeRefresh_Layout =listViewItem.findViewById(R.id.ordercancellationtimeRefresh_Layout);
        final LinearLayout refreshordercancelationtime_image_layout =listViewItem.findViewById(R.id.refreshordercancelationtime_image_layout);


     //   final Button transit_generateTokenNo_button_widget = listViewItem.findViewById(R.id.transit_generateTokenNo_button_widget);



        final Button mobileprint_button_widget = listViewItem.findViewById(R.id.mobileprint_button_widget);



        ordertypeLayout.setVisibility(View.GONE);
        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);
        //Log.i("Tag","Order Pos:   "+ mobile_manageOrders1.sorted_OrdersList.get(pos));


        try {
           orderStatus = modal_manageOrders_pojo_class.getOrderstatus().toUpperCase();
       }
       catch (Exception e){
           e.printStackTrace();
       }




        try {
            orderType= modal_manageOrders_pojo_class.getOrderType().toUpperCase();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            deliverytype =  modal_manageOrders_pojo_class.getDeliverytype().toUpperCase();

        }
        catch (Exception e){
            e.printStackTrace();
        }



        try{
            deliveryPersonName = modal_manageOrders_pojo_class.getDeliveryPartnerName().toString();
        }
        catch (Exception e){
            e.printStackTrace();
            deliveryPersonName="";
        }


/*
        orderPlacedTime ="";
        isOrderPlacedlessThan3MinsBefore = true;
        try {
            orderPlacedTime =  modal_manageOrders_pojo_class.getOrderplacedtime();

            isOrderPlacedlessThan3MinsBefore = CheckWeathertheOrderisPlacedLessThan3Mins(orderPlacedTime);
            Log.d(Constants.TAG, "log isOrderPlacedlessThan3MinsBefore : " + isOrderPlacedlessThan3MinsBefore);

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
        refreshordercancelationtime_image_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                notifyDataSetChanged();
                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

            }
        });



 */
      //  searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

        totalButtonLayout.setVisibility(View.VISIBLE);
        ordercancellationtimeRefresh_Layout.setVisibility(View.GONE);

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


        }
        try {
             orderStatusFromArray = modal_manageOrders_pojo_class.getOrderstatus();
        }
        catch (Exception e){
            e.printStackTrace();
        }



        //Log.i("tag","orderStatusFromArray"+ orderStatusFromArray);
        //Log.i("tag","orderStatus"+ orderStatus);
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
            slotdate_label_widget.setText(String.format(" %s", "Delivery Distance"));
            slotdate_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getDeliverydistance()+"Kms "));
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
            if (orderStatusFromArray.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
                if ((tokenNofromArray.length() > 0) && (tokenNofromArray != null) && (!tokenNofromArray.equals(""))) {
                    pending_order_assignDeliveryperson_button_widget.setVisibility(View.VISIBLE);
                    generateTokenNo_button_widget.setVisibility(View.GONE);
                    mobileprint_button_widget.setVisibility(View.VISIBLE);

                } else {
                    pending_order_assignDeliveryperson_button_widget.setVisibility(View.GONE);
                   generateTokenNo_button_widget.setVisibility(View.VISIBLE);
                    mobileprint_button_widget.setVisibility(View.GONE);

                }
            }
            else{
                try {
                    String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
                    if ((tokenNofromArray.length() > 0) && (tokenNofromArray != null) && (!tokenNofromArray.equals(""))) {
                        other_assignDeliveryperson_button_widget.setVisibility(View.VISIBLE);
                  //      transit_generateTokenNo_button_widget.setVisibility(View.GONE);
                        generateTokenNo_button_widget.setVisibility(View.GONE);
                        mobileprint_button_widget.setVisibility(View.VISIBLE);
                    } else {
                        other_assignDeliveryperson_button_widget.setVisibility(View.GONE);
                        //transit_generateTokenNo_button_widget.setVisibility(View.VISIBLE);
                        generateTokenNo_button_widget.setVisibility(View.VISIBLE);
                        mobileprint_button_widget.setVisibility(View.GONE);


                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (orderStatusFromArray.equals(Constants.NEW_ORDER_STATUS)) {
                generateTokenNo_button_widget.setVisibility(View.GONE);
                mobileprint_button_widget.setVisibility(View.GONE);

            }
        }
        catch(Exception e){
            e.printStackTrace();
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


        if (deliverytype.equals(Constants.STOREPICKUP_DELIVERYTYPE)) {
            ready_for_pickup_button_widget.setVisibility(View.GONE);
            if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {

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

        if(orderType.equals(Constants.POSORDER)){
            tokenNoLayout.setVisibility(View.GONE);
            slotTimeLayout.setVisibility(View.GONE);
            slotDateoLayout.setVisibility(View.GONE);
            slotNameLayout.setVisibility(View.GONE);


        }
        else{
            tokenNoLayout.setVisibility(View.VISIBLE);
            slotTimeLayout.setVisibility(View.VISIBLE);
            slotDateoLayout.setVisibility(View.VISIBLE);
            slotNameLayout.setVisibility(View.VISIBLE);

        }


        if((orderType.equals(Constants.SwiggyOrder))||(orderType.equals(Constants.PhoneOrder))  ||(orderType.equals(Constants.DunzoOrder)) ||(orderType.equals(Constants.BigBasket))){
            tokenNoLayout.setVisibility(View.GONE);
            slotTimeLayout.setVisibility(View.GONE);
            slotdate_label_widget.setText("Order Type");
            slotdate_text_widget.setText(orderType);
        }
        if(orderStatusFromArray.equals(Constants.DELIVERED_ORDER_STATUS)) {
            if (orderType.equals(Constants.POSORDER)) {
                tokenNoLayout.setVisibility(View.GONE);
                slotTimeLayout.setVisibility(View.GONE);
                slotDateoLayout.setVisibility(View.GONE);
                other_assignDeliveryperson_button_widget.setVisibility(View.GONE);

                generateTokenNo_button_widget.setVisibility(View.GONE);
            } else {
                tokenNoLayout.setVisibility(View.VISIBLE);
                slotTimeLayout.setVisibility(View.VISIBLE);
                slotDateoLayout.setVisibility(View.VISIBLE);
                generateTokenNo_button_widget.setVisibility(View.GONE);


            }
            other_assignDeliveryperson_button_widget.setVisibility(View.GONE);

        }



        try {

            JSONArray array  = modal_manageOrders_pojo_class.getItemdesp();
            //Log.i("tag","array.length()"+ array.length());
            String b= array.toString();
            modal_manageOrders_pojo_class.setItemdesp_string(b);
            String itemDesp="",subCtgyKey="",cutname="";


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
                   /* if (itemDesp.length()>0) {

                        itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+itemName, quantity);
                    } else {
                        itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName, quantity);

                    }

                    */

                    if (itemDesp.length()>0) {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+ "Grill House "+itemName, quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+"Ready to Cook  "+itemName, quantity);

                        }
                        else{
                            itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+itemName, quantity);

                        }
                    } else {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", "Grill House "+itemName, quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", "Ready to Cook  "+itemName, quantity);

                        }
                        else{
                            itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName, quantity);

                        }

                    }

               //     orderDetails_text_widget.setText(String.format(itemDesp));

                } else {

                    //Log.i("tag", "array.lengrh(i" + json.length());
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
                  /*  if (itemDesp.length()>0) {

                        itemDesp = String.format("%s ,\n%s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s * %s", itemName, quantity);

                    }

                   */

                    if (itemDesp.length()>0) {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp,  "Grill House "+ itemName,cutname, quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, "Ready to Cook  "+ itemName,cutname, quantity);

                        }
                        else{
                            itemDesp = String.format("%s ,\n%s  %s * %s", itemDesp, itemName,cutname, quantity);

                        }
                    } else {
                        if(subCtgyKey.equals("tmcsubctgy_16")){
                            itemDesp = String.format("%s  %s * %s",  "Grill House "+ itemName, cutname,quantity);

                        }
                        else  if(subCtgyKey.equals("tmcsubctgy_15")){
                            itemDesp = String.format("%s  %s * %s",  "Ready to Cook  "+ itemName,cutname, quantity);

                        }
                        else{
                            itemDesp = String.format("%s  %s * %s", itemName,cutname, quantity);

                        }

                    }
            //        orderDetails_text_widget.setText(String.format(itemDesp));
                    //Log.i("tag", "array.lengrh(i" + json.length());


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
                bundle.putString("From",CalledFrom);

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
                changestatusto ="";

                if (BluetoothPrintDriver.IsNoConnection()) {
                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Printer_is_Disconnected_want_to_generate_TokenNo,
                            R.string.Yes_Text, R.string.No_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                                    generatingTokenNo(vendorkey,orderDetailsKey);

                                }

                                @Override
                                public void onNo() {
                                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

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
                                        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                                        generatingTokenNo(vendorkey,orderDetailsKey);

                                    }

                                    @Override
                                    public void onNo() {
                                        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                    }
                                });


                    }
                    else{

                        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                        generatingTokenNo(vendorkey,orderDetailsKey);

                    }

                }


                //Log.i("tag","orderkey1"+ OrderKey);
            }
        });


      /*  transit_generateTokenNo_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));

                generatingTokenNo(vendorkey,orderDetailsKey);

                //Log.i("tag","orderkey1"+ OrderKey);


            }
        });



      */

        mobileprint_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                        if (BluetoothPrintDriver.IsNoConnection()) {

                            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                            AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                        }

                        if(!BluetoothPrintDriver.IsNoConnection()){
                            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                            if (!mBluetoothAdapter.isEnabled()) {
                                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                            }
                            else{
                                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                                searchOrdersUsingMobileNumber.printBill(modal_manageOrders_pojo_class);

                            }
                        }



                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


        //1
        confirmed_Order_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                    String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                                    String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();

                                    //Log.i("Tag","0"+OrderKey);
                                    new_Order_Linearlayout.setVisibility(View.GONE);
                                    ready_Order_Linearlayout.setVisibility(View.GONE);
                                    confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                                    cancelled_Order_Linearlayout.setVisibility(View.GONE);
                                    if((tokenNofromArray.length()<=0)||(tokenNofromArray == null)||(tokenNofromArray.equals(""))) {
                                        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                                        generatingTokenNo(vendorkey, orderDetailsKey);
                                    }
                                    else{
                                        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                                        ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);
                                        if (BluetoothPrintDriver.IsNoConnection()) {

                                            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                            AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                                        }

                                        if(!BluetoothPrintDriver.IsNoConnection()){
                                            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                            if (!mBluetoothAdapter.isEnabled()) {
                                                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                                AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                                            }
                                            else{
                                                searchOrdersUsingMobileNumber.printBill(modal_manageOrders_pojo_class);

                                            }
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

                                        //Log.i("Tag","0"+OrderKey);
                                        new_Order_Linearlayout.setVisibility(View.GONE);
                                        ready_Order_Linearlayout.setVisibility(View.GONE);
                                        confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                                        cancelled_Order_Linearlayout.setVisibility(View.GONE);
                                        if((tokenNofromArray.length()<=0)||(tokenNofromArray == null)||(tokenNofromArray.equals(""))) {
                                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                                            generatingTokenNo(vendorkey, orderDetailsKey);
                                        }
                                        else{
                                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                                            ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);
                                            if (BluetoothPrintDriver.IsNoConnection()) {

                                                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                                AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                                            }

                                            if(!BluetoothPrintDriver.IsNoConnection()){
                                                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                                if (!mBluetoothAdapter.isEnabled()) {
                                                    Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                                    AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                                                }
                                                else{
                                                    searchOrdersUsingMobileNumber.printBill(modal_manageOrders_pojo_class);

                                                }
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
                        String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                        String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();

                        //Log.i("Tag","0"+OrderKey);
                        new_Order_Linearlayout.setVisibility(View.GONE);
                        ready_Order_Linearlayout.setVisibility(View.GONE);
                        confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                        cancelled_Order_Linearlayout.setVisibility(View.GONE);
                        if((tokenNofromArray.length()<=0)||(tokenNofromArray == null)||(tokenNofromArray.equals(""))) {
                            generatingTokenNo(vendorkey, orderDetailsKey);
                        }
                        else{
                            ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);
                            if (BluetoothPrintDriver.IsNoConnection()) {

                                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                            }

                            if(!BluetoothPrintDriver.IsNoConnection()){
                                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                                if (!mBluetoothAdapter.isEnabled()) {
                                    Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                    AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                                }
                                else{
                                    searchOrdersUsingMobileNumber.printBill(modal_manageOrders_pojo_class);

                                }
                            }
                        }
                    }
                }

            }
        });
        cancel_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Currenttime = getDate_and_time();

                changestatusto =Constants.CANCELLED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
                //Log.i("Tag",""+changestatusto+OrderKey);

                ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);

            }
        });



        ready_for_pickup_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changestatusto =Constants.READY_FOR_PICKUP_ORDER_STATUS;
                Currenttime = getDate_and_time();

                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);

                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);
                modal_manageOrders_pojo_class.setOrderstatus(changestatusto);

                ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);

            }
        });

        ready_for_pickup_delivered_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Currenttime = getDate_and_time();
                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

                changestatusto =Constants.DELIVERED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
                //Log.i("Tag",""+changestatusto+OrderKey);

                ChangeStatusOftheOrder(changestatusto,OrderKey,Currenttime);
                String orderid = "";
                try{
                    orderid = (String.format("%s", modal_manageOrders_pojo_class.getOrderid()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                getStockOutGoingDetailsUsingOrderid(orderid);
                //  mobile_manageOrders1.sorted_OrdersList.remove(pos);
                //  notifyDataSetChanged();
            }
        });



        pending_order_assignDeliveryperson_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    showBottomSheetDialog(Orderkey,deliverypartnerName);

                }
                else{
                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,"null");

                }



            }

        });
        other_assignDeliveryperson_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    showBottomSheetDialog(Orderkey,deliverypartnerName);

                }
                else{
                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,"null");

                }






            }
        });

        cancelled_assignDeliveryperson_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    showBottomSheetDialog(Orderkey,deliverypartnerName);

                }
                else{
                    String Orderkey = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    showBottomSheetDialog(Orderkey,"null");

                }





            }
        });






        return  listViewItem ;

    }



    private void getStockOutGoingDetailsUsingOrderid(String orderid) {



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getStockOutgoingUsingSalesOrderid+orderid ,null,
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
                                        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);


                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();
                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(mContext, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();
                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);



                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();
                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

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
                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                 searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

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












    private void showBottomSheetDialog(String orderkey, String deliverypartnerName) {

          bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.mobilescreen_assigndeliverypartner_bottom_sheet_dialog);

        ListView ListView1 = bottomSheetDialog.findViewById(R.id.listview);

        Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(mContext, searchOrdersUsingMobileNumber.deliveryPartnerList,orderkey,"AppOrdersList", deliverypartnerName);

        ListView1.setAdapter(adapter_mobile_assignDeliveryPartner1);

        bottomSheetDialog.show();
    }

    private void generatingTokenNo(String vendorkey, String orderDetailsKey) {
        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_generateTokenNo+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {


                //Log.d(Constants.TAG, "api: " + Constants.api_generateTokenNo+vendorkey);

                //Log.d(Constants.TAG, "Responsewwwww: " + response);
                try {
                    String tokenNo = response.getString("tokenNumber");
                    UpdateTokenNoInOrderDetails(tokenNo,orderDetailsKey);
                } catch (JSONException e) {
                    searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

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
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);
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
        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);

        JSONObject  jsonObject = new JSONObject();
        try {

                jsonObject.put("key", orderDetailsKey);
                jsonObject.put("tokenno", tokenNo);
                //Log.i("tag","listenertoken"+ "");








        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d(Constants.TAG, "JSONOBJECT: " + e);
            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);
//"?key="+OrderKey+"&orderstatus="+changestatusto+"&currentTime="+Currenttime
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_UpdateTokenNO_OrderDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i = 0; i< searchOrdersUsingMobileNumber.ordersList.size(); i++){
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= searchOrdersUsingMobileNumber.ordersList.get(i);
                    String key = modal_manageOrders_pojo_class.getOrderdetailskey();
                    if(orderDetailsKey.equals(key)){
                        modal_manageOrders_pojo_class.setTokenno(tokenNo);
                        try {
                            for (int sortedarrayIterator = 0; sortedarrayIterator < com.meatchop.tmcpartner.Settings.searchOrdersUsingMobileNumber.sorted_OrdersList.size(); sortedarrayIterator++) {
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_sortedOrdersList = com.meatchop.tmcpartner.Settings.searchOrdersUsingMobileNumber.sorted_OrdersList.get(sortedarrayIterator);
                                String sortedArraykey = modal_manageOrders_pojo_class_sortedOrdersList.getOrderdetailskey();
                                if (orderDetailsKey.equals(sortedArraykey)) {
                                    modal_manageOrders_pojo_class_sortedOrdersList.setTokenno(tokenNo);

                                }
                            }
                        }
                        catch (Exception e){
                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                            searchOrdersUsingMobileNumber.displayorderDetailsinListview(searchOrdersUsingMobileNumber.orderStatus, searchOrdersUsingMobileNumber.ordersList, searchOrdersUsingMobileNumber.slottypefromSpinner, searchOrdersUsingMobileNumber.selectedTimeRange_spinner,searchOrdersUsingMobileNumber.selected_DeliveryDistanceRange_spinner, searchOrdersUsingMobileNumber.selected_StatusFrom_spinner);

                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                        if(changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                            ChangeStatusOftheOrder(changestatusto, OrderKey, Currenttime);
                        }
                        else{
                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                           // searchOrdersUsingMobileNumber.displayorderDetailsinListview(searchOrdersUsingMobileNumber.orderStatus, searchOrdersUsingMobileNumber.ordersList, searchOrdersUsingMobileNumber.slottypefromSpinner, searchOrdersUsingMobileNumber.selectedTimeRange_spinner,searchOrdersUsingMobileNumber.selected_DeliveryDistanceRange_spinner);
                            notifyDataSetChanged();
                        }
                        if (BluetoothPrintDriver.IsNoConnection()) {

                            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                            AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                        }

                        if(!BluetoothPrintDriver.IsNoConnection()){
                            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                            if (!mBluetoothAdapter.isEnabled()) {
                                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);
                                AlertDialogClass.showDialog(searchOrdersUsingMobileNumber,R.string.Printer_is_Disconnected);

                            }
                            else{
                                searchOrdersUsingMobileNumber.printBill(modal_manageOrders_pojo_class);

                            }
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
                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

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
        searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(true);
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
                //Log.i("tag","listenertoken"+ "");
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
                //Log.i("tag","listenertoken"+ "");
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
                //Log.i("tag","listenertoken"+ "");
            }
            if(changestatusto.equals(Constants.DELIVERED_ORDER_STATUS)){
                jsonObject.put("key", OrderKey);
                jsonObject.put("orderstatus", changestatusto);
                jsonObject.put("orderdeliverytime", currenttime);
                ////Log.i("tag","listenertoken"+ "");
            }







        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d(Constants.TAG, "JSONOBJECT: " + e);

        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);
//"?key="+OrderKey+"&orderstatus="+changestatusto+"&currentTime="+Currenttime
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_updateTrackingOrderTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i = 0; i< searchOrdersUsingMobileNumber.ordersList.size(); i++){
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= searchOrdersUsingMobileNumber.ordersList.get(i);
                    String keyfromtrackingDetails = modal_manageOrders_pojo_class.getKeyfromtrackingDetails();
                    if(OrderKey.equals(keyfromtrackingDetails)){
                        modal_manageOrders_pojo_class.setOrderstatus(changestatusto);

                        if(changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                            modal_manageOrders_pojo_class.setOrderconfirmedtime(Currenttime);
                        }
                        if(changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                            modal_manageOrders_pojo_class.setOrderreadytime(Currenttime);
                        }

                        try {
                            for (int sortedarrayIterator = 0; sortedarrayIterator < com.meatchop.tmcpartner.Settings.searchOrdersUsingMobileNumber.sorted_OrdersList.size(); sortedarrayIterator++) {
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class_sortedOrdersList = com.meatchop.tmcpartner.Settings.searchOrdersUsingMobileNumber.sorted_OrdersList.get(sortedarrayIterator);
                                String sortedArraykey = modal_manageOrders_pojo_class_sortedOrdersList.getKeyfromtrackingDetails();
                                if (OrderKey.equals(sortedArraykey)) {
                                    modal_manageOrders_pojo_class_sortedOrdersList.setOrderstatus(changestatusto);
                                    if (changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                                        modal_manageOrders_pojo_class_sortedOrdersList.setOrderconfirmedtime(Currenttime);
                                    }
                                    if (changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                                        modal_manageOrders_pojo_class_sortedOrdersList.setOrderreadytime(Currenttime);
                                    }
                                }




                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                            searchOrdersUsingMobileNumber.displayorderDetailsinListview(searchOrdersUsingMobileNumber.orderStatus, searchOrdersUsingMobileNumber.ordersList, searchOrdersUsingMobileNumber.slottypefromSpinner, searchOrdersUsingMobileNumber.selectedTimeRange_spinner,searchOrdersUsingMobileNumber.selected_DeliveryDistanceRange_spinner, searchOrdersUsingMobileNumber.selected_StatusFrom_spinner);

                        }



                    }
                }
                //Log.d(Constants.TAG, "Responsewwwww: " + response);
                notifyDataSetChanged();
                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

                //searchOrdersUsingMobileNumber.displayorderDetailsinListview(searchOrdersUsingMobileNumber.orderStatus, searchOrdersUsingMobileNumber.ordersList, searchOrdersUsingMobileNumber.slottypefromSpinner, searchOrdersUsingMobileNumber.selectedTimeRange_spinner,searchOrdersUsingMobileNumber.selected_DeliveryDistanceRange_spinner);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                searchOrdersUsingMobileNumber.Adjusting_Widgets_Visibility(false);

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










    private boolean CheckWeathertheOrderisPlacedLessThan3Mins(String orderPlacedTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(orderPlacedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        Log.d(Constants.TAG, "log orderPlacedTime : " + orderPlacedTime);


        calendar.add(Calendar.MINUTE, 3);



        Date c1 = calendar.getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("EEE");
        String predictedday = df1.format(c1);



        SimpleDateFormat df2 = new SimpleDateFormat("d MMM yyyy");
        String  predicteddate = df2.format(c1);
        String predicteddateandday = predictedday+", "+predicteddate;


        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss");
        String  predictedtime = df3.format(c1);
        String predicteddateanddayandtime = predictedday+", "+predicteddate+" "+predictedtime;

        Log.d(Constants.TAG, "log predicteddateanddayandtime : " + predicteddateanddayandtime);

        long predictedLongForDate = getLongValuefortheDate(predicteddateanddayandtime);
        String  currentTime = getDate_and_time();
        Log.d(Constants.TAG, "log currentTime : " +currentTime);

        long currentTimeLong = getLongValuefortheDate(currentTime);
        if(currentTimeLong>=predictedLongForDate){//current time is greater or equals order placed time + 3 minutes
            Log.d(Constants.TAG, "log currentTimeLong : " +currentTimeLong);
            Log.d(Constants.TAG, "log predictedLongForDate : " +predictedLongForDate);

            return false;

        }
        else{

            Log.d(Constants.TAG, "log currentTimeLong : " +currentTimeLong);
            Log.d(Constants.TAG, "log predictedLongForDate : " +predictedLongForDate);
            return true;
        }

    }




    private Long getLongValuefortheDate(String orderplacedtime) {
        long time1long =  0;
        String longvalue="";
        try {
            String time1 = orderplacedtime;
            //   Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = sdf.parse(time1);
            time1long = date.getTime() / 1000;
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

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
                Date date = sdf.parse(time1);
                time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);

                //   long differencetime = time2long - time1long;
                //  Log.d(TAG, "   "+orderplacedtime);

                //    Log.d(TAG, "time1long  "+time1long);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time1long;
    }



































}
