package com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.add_updateinventorydetailentries.Add_UpdateInventoryDetailsEntries_AsyncTask;
import com.meatchop.tmcpartner.add_updateinventorydetailentries.Add_UpdateInventoryDetailsEntries_Interface;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTableInterface;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Update_CustomerOrderDetails_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.Adapter_Mobile_changeWeight_in_itemDesp;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.R;

import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.pos.printer.PrinterFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
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

public class Adapter_Pos_ManageOrders_ListView extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;

    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    public static List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
    String vendorKey="";
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String deliverytype="",changestatusto,orderStatus,OrderKey,orderStatusfromArray,tokenNO;
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay;
    public Pos_ManageOrderFragment pos_manageOrderFragment;


    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String printerType_sharedPreference = "";
    String printerStatus_sharedPreference = "";
    String userStatus ="";

    BluetoothAdapter mBluetoothAdapter =null;




    boolean orderdetailsnewschema = false  , updateweightforonlineorders =false;

    List<Modal_MenuItem>MenuItem = new ArrayList<>();

    Dialog changeWeight_dialog;

    LinearLayout new_Order_Linearlayout ;
    LinearLayout confirming_order_Linearlayout ;
    LinearLayout ready_Order_Linearlayout  ;
    LinearLayout cancelled_Order_Linearlayout ;
    int adapterPosition ;

    Add_UpdateInventoryDetailsEntries_Interface mResultCallback_Add_UpdateInventoryEntriesInterface = null;


    Update_CustomerOrderDetails_TrackingTableInterface mResultCallback_UpdateCustomerOrderDetailsTableInterface;


    public Adapter_Pos_ManageOrders_ListView(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, Pos_ManageOrderFragment pos_manageOrderFragment, String orderStatus , List<Modal_MenuItem>MenuItemm ) {
        super(mContext, R.layout.pos_manageorders_listview_child, ordersList);
        this.pos_manageOrderFragment=pos_manageOrderFragment;
        OrderdItems_desp =new ArrayList<>();
        this.mContext=mContext;
       this.ordersList=ordersList;
      this.orderStatus=orderStatus;
        this.MenuItem = MenuItemm;
      //  getMenuItemArrayFromSharedPreferences();

    }

    @Override
    public int getCount() {
        return super.getCount();
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
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.pos_manageorders_listview_child, (ViewGroup) view, false);


        final TextView deliveryPartner_name_widget = listViewItem.findViewById(R.id.deliveryPartner_name_widget);
        final TextView deliveryPartner_mobileNo_widget = listViewItem.findViewById(R.id.deliveryPartner_mobileNo_widget);
        final TextView orderid_text_widget = listViewItem.findViewById(R.id.orderid_text_widget);
        //tochangeweight
        final LinearLayout orderDetialsLayout =listViewItem.findViewById(R.id.orderDetailsLayout);
        //tochangeheight
        final CardView cardLayout =listViewItem.findViewById(R.id.cardLayout);
        final LinearLayout deliveryPartnerdetailsLayout =listViewItem.findViewById(R.id.deliveryPartnerAssignLayout);


        //
        final TextView tokenNo_label_widget = listViewItem.findViewById(R.id.tokenNo_label_widget);
        final TextView tokenNo_text_widget = listViewItem.findViewById(R.id.tokenNo_text_widget);
        final TextView orderDetails_text_widget = listViewItem.findViewById(R.id.orderDetails_text_widget);
        final TextView ordertype_text_widget = listViewItem.findViewById(R.id.ordertype_text);
        final TextView orderPlacedtime_text_widget = listViewItem.findViewById(R.id.orderPlacedtime_text_widget);
        final TextView orderstatus_text_widget = listViewItem.findViewById(R.id.orderstatus_text_widget);

        //final Button generateTokenNo_text_widget = listViewItem.findViewById(R.id.generateTokenNo);
      //  final Button readyorder_generateTokenNo_button_widget = listViewItem.findViewById(R.id.transit_generateTokenNo);


        final TextView slotName_text_widget = listViewItem.findViewById(R.id.slotName_text_widget);
        final TextView slotDate_text_widget = listViewItem.findViewById(R.id.slotDate_text_widget);
        //final TextView slotTime_text_widget = listViewItem.findViewById(R.id.slotTime_text_widget);



        final LinearLayout order_item_list_parentLayout =listViewItem.findViewById(R.id.order_item_list_parentLayout);

         new_Order_Linearlayout =listViewItem.findViewById(R.id.new_Order_Linearlayout);
         confirming_order_Linearlayout =listViewItem.findViewById(R.id.confirming_order_Linearlayout);
         ready_Order_Linearlayout =listViewItem.findViewById(R.id.ready_Order_Linearlayout);
         cancelled_Order_Linearlayout =listViewItem.findViewById(R.id.cancelled_Order_Linearlayout);
        final LinearLayout slotDate_linearLayout =listViewItem.findViewById(R.id.slotDate_linearLayout);
       // final LinearLayout slotTimeRange_linearLayout =listViewItem.findViewById(R.id.slotTimeRange_linearLayout);

        final TextView confirmed_Order_button_widget = listViewItem.findViewById(R.id.accept_Order_button_widget);
        final TextView cancel_button_widget = listViewItem.findViewById(R.id.cancel_button_widget);

        final TextView ready_for_pickup_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_button_widget);
        final TextView pending_order_print_button_widget = listViewItem.findViewById(R.id.pending_order_print_button_widget);

        final TextView other_print_button_widget = listViewItem.findViewById(R.id.other_print_button_widget);
        final TextView cancelled_print_button_widget = listViewItem.findViewById(R.id.cancelled_print_button_widget);

        final TextView deliverytype_text_widget =listViewItem.findViewById(R.id.deliverytype_text_widget);

        final TextView ready_for_pickup_delivered_button_widget =listViewItem.findViewById(R.id.ready_for_pickup_delivered_button_widget);

        final TextView changeDeliveryPartner =listViewItem.findViewById(R.id.changeDeliveryPartner);
        SharedPreferences shared_PF_PrinterData = mContext.getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);

        printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));
        printerStatus_sharedPreference   = (shared_PF_PrinterData.getString("printerStatus", ""));
        SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = shared.getString("VendorKey","");
        StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (shared.getString("VendorPincode", ""));
        StoreLanLine = (shared.getString("VendorMobileNumber", ""));
        orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
        //orderdetailsnewschema = true;
        updateweightforonlineorders = (shared.getBoolean("updateweightforonlineorders", false));



        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);
        //Log.i("Tag","Order Pos:   "+ Pos_ManageOrderFragment.sorted_OrdersList.get(pos));


        try{
            userStatus = modal_manageOrders_pojo_class.getUserstatus().toString();

            if(userStatus.toUpperCase().equals(Constants.USERSTATUS_FLAGGED)){
                order_item_list_parentLayout.setBackground(getDrawable(mContext,R.drawable.orange_border_button));

            }
            else{

            }

        }
            catch ( Exception e ){
            e.printStackTrace();
        }


        if(orderStatus.equals(Constants.NEW_ORDER_STATUS)){
            deliveryPartnerdetailsLayout.setVisibility(View.GONE);
           // ordertype_text_widget.setVisibility(View.GONE);

            new_Order_Linearlayout.setVisibility(View.VISIBLE);
            ready_Order_Linearlayout.setVisibility(View.GONE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);
        }


        if(orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)){
            deliveryPartnerdetailsLayout.setVisibility(View.VISIBLE);
          //  ordertype.setVisibility(View.GONE);
            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.GONE);
            confirming_order_Linearlayout.setVisibility(View.VISIBLE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);

        }



        if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)){
            deliveryPartnerdetailsLayout.setVisibility(View.VISIBLE);
          //  ordertype.setVisibility(View.GONE);
            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.VISIBLE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);


        }

        if(orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)){
            deliveryPartnerdetailsLayout.setVisibility(View.GONE);
        //    ordertype.setVisibility(View.GONE);
            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.VISIBLE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);


        }
      if(orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)){

               deliveryPartnerdetailsLayout.setVisibility(View.GONE);

                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);


        }


        orderStatusfromArray = modal_manageOrders_pojo_class.getOrderstatus().toUpperCase();
        String SlotName = modal_manageOrders_pojo_class.getSlotname().toUpperCase();
        tokenNo_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getTokenno()));
        String orderid  = (String.format(" %s", modal_manageOrders_pojo_class.getOrderid()));
        orderid_text_widget.setText(String.format("#"+orderid));
        //Log.i("tag","orderid"+ orderid);
        ordertype_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderType().toUpperCase()));
        orderPlacedtime_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderplacedtime()));
        slotName_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getSlotname()));
        String orderType = String.format(" %s", modal_manageOrders_pojo_class.getOrderType().toUpperCase());





        try{
            if(pos_manageOrderFragment.isSearchButtonClicked) {
                orderstatus_text_widget.setVisibility(View.VISIBLE);
                try {
                    orderstatus_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderstatus()));
                }
                catch (Exception e){
                    e.printStackTrace();
                    orderstatus_text_widget.setText(String.format(" %s", ""));
                    orderstatus_text_widget.setVisibility(View.GONE);

                }
            }
            else{
                orderstatus_text_widget.setVisibility(View.GONE);

            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }







        slotDate_text_widget.setText(String.format(" %s"," - "+ modal_manageOrders_pojo_class.getSlotdate()+" ( "+modal_manageOrders_pojo_class.getSlottimerange()+" ) "));
        slotName_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getSlotname()));


/*

        if(SlotName.equals(Constants.PREORDER_SLOTNAME)) {
            slotDate_text_widget.setVisibility(View.VISIBLE);
            slotDate_text_widget.setText(String.format(" %s"," - "+ modal_manageOrders_pojo_class.getSlotdate()+" ( "+modal_manageOrders_pojo_class.getSlottimerange()+" ) "));
            slotName_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getSlotname()));

        }
        else{
            slotDate_text_widget.setVisibility(View.GONE);

        }


 */






        if((!modal_manageOrders_pojo_class.getOrderstatus().equals(Constants.NEW_ORDER_STATUS))&&(!modal_manageOrders_pojo_class.orderType.toUpperCase().equals(Constants.POSORDER))) {
            deliveryPartnerdetailsLayout.setVisibility(View.VISIBLE);
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

        }
        else{
            deliveryPartnerdetailsLayout.setVisibility(View.GONE);

            deliveryPartner_mobileNo_widget.setText("Not Assigned");
            deliveryPartner_name_widget.setText("Not Assigned");
            changeDeliveryPartner.setText("Assign Delivery Person");
    //        generateTokenNo_text_widget.setVisibility(View.GONE);
      //      readyorder_generateTokenNo_button_widget.setVisibility(View.GONE);
        }

        try {
            ordertype_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderType()));
            orderType = modal_manageOrders_pojo_class.getOrderType().toUpperCase();
            if(orderStatusfromArray.equals(Constants.DELIVERED_ORDER_STATUS)){

                if (orderType.equals(Constants.POSORDER)) {
                tokenNo_label_widget.setVisibility(View.GONE);
                tokenNo_text_widget.setVisibility(View.GONE);

                slotDate_linearLayout.setVisibility(View.GONE);
                other_print_button_widget.setVisibility(View.VISIBLE);
                //    changeDeliveryPartner.setVisibility(View.GONE);
          //      generateTokenNo_text_widget.setVisibility(View.GONE);
            //    readyorder_generateTokenNo_button_widget.setVisibility(View.GONE);
            } else {
                tokenNo_label_widget.setVisibility(View.VISIBLE);
                tokenNo_text_widget.setVisibility(View.VISIBLE);
                slotDate_linearLayout.setVisibility(View.VISIBLE);
              //  generateTokenNo_text_widget.setVisibility(View.GONE);
                //readyorder_generateTokenNo_button_widget.setVisibility(View.GONE);
               //     changeDeliveryPartner.setVisibility(View.VISIBLE);
                    other_print_button_widget.setVisibility(View.VISIBLE);

                }
                changeDeliveryPartner.setVisibility(View.GONE);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        try {
            deliverytype = ( modal_manageOrders_pojo_class.getDeliverytype()).toUpperCase();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (deliverytype.equals(Constants.STOREPICKUP_DELIVERYTYPE)) {
            //Log.i("tag",deliverytype+"     "+Constants.STOREPICKUP_DELIVERYTYPE);
            ready_for_pickup_button_widget.setVisibility(View.GONE);
            if(orderStatusfromArray.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {

                ready_for_pickup_delivered_button_widget.setVisibility(View.VISIBLE);
            }
            else{
                ready_for_pickup_button_widget.setVisibility(View.VISIBLE);
                ready_for_pickup_delivered_button_widget.setVisibility(View.GONE);
            }
            slotName_text_widget.setVisibility(View.GONE);
            deliverytype_text_widget.setVisibility(View.VISIBLE);
            try {
                deliverytype_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getDeliverytype()));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            ready_for_pickup_button_widget.setVisibility(View.VISIBLE);
            ready_for_pickup_delivered_button_widget.setVisibility(View.GONE);
            slotName_text_widget.setVisibility(View.VISIBLE);
            deliverytype_text_widget.setVisibility(View.GONE);
        }











        try {
            JSONArray array  = modal_manageOrders_pojo_class.getItemdesp();
            //Log.i("tag","array.length()"+ array.length());
                    String b= array.toString();
            modal_manageOrders_pojo_class.setItemdesp_string(b);
            notifyDataSetChanged();
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
                  /*  if (itemDesp.length()>0) {

                        itemDesp = String.format("%s  ,\n%s * %s", itemDesp, marinadeitemName + "  with "+itemName, quantity);
                    } else {
                        itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName, quantity);

                    }

                   */


                }
                else {

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
                    //Log.i("tag", "array.lengrh(i" + json.length());


                }
            }
            orderDetails_text_widget.setText(String.format(itemDesp));

        } catch (JSONException e) {
                e.printStackTrace();
            }



        ready_for_pickup_delivered_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                changestatusto =Constants.DELIVERED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
                //Log.i("Tag",""+changestatusto+OrderKey);
                adapterPosition = pos;
                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

                getStockOutGoingDetailsUsingOrderid(orderid);
               // Pos_ManageOrderFragment.sorted_OrdersList.remove(pos);
                notifyDataSetChanged();
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


                pos_manageOrderFragment.loadingPanel.setVisibility(View.VISIBLE);
                pos_manageOrderFragment.loadingpanelmask.setVisibility(View.VISIBLE);
                Intent intent = new Intent(mContext,AssigningDeliveryPartner.class);
                intent.putExtra("TrackingTableKey",modal_manageOrders_pojo_class.getKeyfromtrackingDetails());
                intent.putExtra("orderid",modal_manageOrders_pojo_class.getOrderid());
                intent.putExtra("customerMobileNo",modal_manageOrders_pojo_class.getUsermobile());
                intent.putExtra("vendorkey",modal_manageOrders_pojo_class.getVendorkey());
                intent.putExtra("From","PosManageOrders");


                pos_manageOrderFragment.loadingPanel.setVisibility(View.GONE);
                pos_manageOrderFragment.loadingpanelmask.setVisibility(View.GONE);

                mContext.startActivity(intent);
            }
        });

        order_item_list_parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(mContext, Pos_OrderDetailsScreen.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", modal_manageOrders_pojo_class);
                     bundle.putString("From","PosManageOrders");


                    intent.putExtras(bundle);

                    mContext.startActivity(intent);

            }
        });



        //1
        confirmed_Order_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Currenttime = getDate_and_time();
                changestatusto =Constants.CONFIRMED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
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


                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.VISIBLE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();
                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.useraddress=modal_manageOrders_pojo_class.getUseraddress();
                selectedOrder.orderType=modal_manageOrders_pojo_class.getOrderType();
                selectedOrder.slotname=modal_manageOrders_pojo_class.getSlotname();
                selectedOrder.slotdate=modal_manageOrders_pojo_class.getSlotdate();
                selectedOrder.slottimerange=modal_manageOrders_pojo_class.getSlottimerange();
                selectedOrder.deliverytype = modal_manageOrders_pojo_class.getDeliverytype();
                selectedOrder.notes = modal_manageOrders_pojo_class.getNotes();
                selectedOrder.orderdetailskey = modal_manageOrders_pojo_class.getOrderdetailskey();
                selectedOrder.deliverydistance = modal_manageOrders_pojo_class.getDeliverydistance();
                selectedOrder.deliveryamount = modal_manageOrders_pojo_class.getDeliveryamount();
                selectedOrder.orderplacedtime = modal_manageOrders_pojo_class.getOrderplacedtime();
                selectedBillDetails.add(selectedOrder);
              //  OrderdItems_desp.clear();

                generatingTokenNo(vendorkey,orderDetailsKey,selectedBillDetails,orderid, customerMobileNo);
                adapterPosition = pos;
                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

              //  Pos_ManageOrderFragment.sorted_OrdersList.remove(pos);
                notifyDataSetChanged();
            }
        });
        cancel_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                changestatusto =Constants.CANCELLED_ORDER_STATUS;
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
                //Log.i("Tag",""+changestatusto+OrderKey);
                adapterPosition = pos;
                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
             //   Pos_ManageOrderFragment.sorted_OrdersList.remove(pos);
                notifyDataSetChanged();
            }
        });


/*
        generateTokenNo_text_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();
                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.useraddress=modal_manageOrders_pojo_class.getUseraddress();
                selectedOrder.orderType=modal_manageOrders_pojo_class.getOrderType();
                selectedOrder.slotname=modal_manageOrders_pojo_class.getSlotname();
                selectedOrder.slotdate=modal_manageOrders_pojo_class.getSlotdate();
                selectedOrder.slottimerange=modal_manageOrders_pojo_class.getSlottimerange();
                selectedOrder.deliverytype = modal_manageOrders_pojo_class.getDeliverytype();
                selectedOrder.notes = modal_manageOrders_pojo_class.getNotes();
                selectedOrder.orderdetailskey = modal_manageOrders_pojo_class.getOrderdetailskey();
                selectedOrder.deliverydistance = modal_manageOrders_pojo_class.getDeliverydistance();

                selectedBillDetails.add(selectedOrder);
                generatingTokenNo(vendorkey,orderDetailsKey, selectedBillDetails);

                //Log.i("tag","orderkey1"+ OrderKey);


            }
        });





        readyorder_generateTokenNo_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();
                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.useraddress=modal_manageOrders_pojo_class.getUseraddress();
                selectedOrder.orderType=modal_manageOrders_pojo_class.getOrderType();
                selectedOrder.slotname=modal_manageOrders_pojo_class.getSlotname();
                selectedOrder.slotdate=modal_manageOrders_pojo_class.getSlotdate();
                selectedOrder.slottimerange=modal_manageOrders_pojo_class.getSlottimerange();
                selectedOrder.deliverytype = modal_manageOrders_pojo_class.getDeliverytype();
                selectedOrder.notes = modal_manageOrders_pojo_class.getNotes();
                selectedOrder.orderdetailskey = modal_manageOrders_pojo_class.getOrderdetailskey();
                selectedOrder.deliverydistance = modal_manageOrders_pojo_class.getDeliverydistance();

                selectedBillDetails.add(selectedOrder);
                generatingTokenNo(vendorkey,orderDetailsKey, selectedBillDetails);

                //Log.i("tag","orderkey1"+ OrderKey);
            }
        });




 */
        ready_for_pickup_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changestatusto =Constants.READY_FOR_PICKUP_ORDER_STATUS;
                Currenttime = getDate_and_time();

                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                //Log.i("Tag","0"+OrderKey);
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

           /*     new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);
                modal_manageOrders_pojo_class.setOrderstatus(changestatusto);

                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                adapterPosition = pos;
                //Pos_ManageOrderFragment.sorted_OrdersList.remove(pos);
                notifyDataSetChanged();

            */

                if(updateweightforonlineorders){

                    if(orderType.toUpperCase().equals(Constants.PhoneOrder)){
                        adapterPosition = pos;
                        ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                        notifyDataSetChanged();
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
                                    for (int j = 0; j < MenuItem.size(); j++) {
                                        String menuIdFromMenu = "", pricetypeforpos = "", inventoryDetails_String;
                                        try {

                                            menuIdFromMenu = MenuItem.get(j).getMenuItemId().toString();
                                        } catch (Exception e) {
                                            menuIdFromMenu = "";
                                            e.printStackTrace();
                                        }
                                        if (menuIdFromItemDesp.equals(menuIdFromMenu)) {
                                            try {

                                                pricetypeforpos = MenuItem.get(j).getPricetypeforpos().toString();
                                            } catch (Exception e) {
                                                pricetypeforpos = "tmcpriceperkg";
                                                e.printStackTrace();
                                            }

                                            try {

                                                inventoryDetails_String = String.valueOf(MenuItem.get(j).getInventorydetails().toString());
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

                                                json.put("tmcctgykey", String.valueOf(MenuItem.get(j).getTmcctgykey().toString()));

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("stockavldetailskey", String.valueOf(MenuItem.get(j).getKey_AvlDetails().toString()));

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                            try {

                                                json.put("stockbalance", String.valueOf(MenuItem.get(j).getStockbalance_AvlDetails().toString()));

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                            try {

                                                json.put("receivedstock", String.valueOf(MenuItem.get(j).getReceivedstock_AvlDetails().toString()));

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }

                                            try {

                                                json.put("barcode", String.valueOf(MenuItem.get(j).getBarcode().toString()));

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("stockincomingkey", String.valueOf(MenuItem.get(j).getStockincomingkey_AvlDetails().toString()));

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("isitemAvailable", String.valueOf(MenuItem.get(j).getItemavailability_AvlDetails().toString()));

                                            } catch (Exception e) {

                                                e.printStackTrace();
                                            }


                                            try {

                                                json.put("allownegativestock", String.valueOf(MenuItem.get(j).getAllownegativestock().toString()));

                                            } catch (Exception e) {
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
                                            adapterPosition = pos;
                                            openBottomSheetToChangeWeight(changestatusto, vendorkey, OrderKey, orderid, customerMobileNo, Currenttime, array, modal_manageOrders_pojo_class.getTokenno());

                                        } else {
                                            adapterPosition = pos;
                                            ChangeStatusOftheOrder(changestatusto, vendorkey, OrderKey, orderid, customerMobileNo, Currenttime);
                                            //  Pos_ManageOrderFragment.sorted_OrdersList.remove(pos);
                                            modal_manageOrders_pojo_class.setOrderstatus(changestatusto);
                                            notifyDataSetChanged();

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
                    adapterPosition = pos;
                    ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

                    //Pos_ManageOrderFragment.sorted_OrdersList.remove(pos);
                    modal_manageOrders_pojo_class.setOrderstatus(changestatusto);
                    notifyDataSetChanged();

                }





            }
        });




        pending_order_print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
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
                String orderDetailsKey = "";

                try{
                    orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }



                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();
                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.useraddress=modal_manageOrders_pojo_class.getUseraddress();
                selectedOrder.orderType=modal_manageOrders_pojo_class.getOrderType();
                selectedOrder.slotname=modal_manageOrders_pojo_class.getSlotname();
                selectedOrder.slotdate=modal_manageOrders_pojo_class.getSlotdate();
                selectedOrder.slottimerange=modal_manageOrders_pojo_class.getSlottimerange();
                selectedOrder.deliverytype = modal_manageOrders_pojo_class.getDeliverytype();
                selectedOrder.notes = modal_manageOrders_pojo_class.getNotes();
                selectedOrder.orderplacedtime = modal_manageOrders_pojo_class.getOrderplacedtime();
                selectedOrder.orderdetailskey = modal_manageOrders_pojo_class.getOrderdetailskey();
                selectedOrder.deliverydistance = modal_manageOrders_pojo_class.getDeliverydistance();
                selectedOrder.deliveryamount = modal_manageOrders_pojo_class.getDeliveryamount();

                selectedBillDetails.add(selectedOrder);
                OrderdItems_desp.clear();
                try {
                    String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
                    if ((tokenNofromArray.length() > 0) && (tokenNofromArray != null) && (!tokenNofromArray.equals(""))) {
                        try {
                            if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                                pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedBillDetails);

                            }
                            else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                                 pos_manageOrderFragment.printReciptUsingBluetoothPrinter(selectedBillDetails);

                            }
                            else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                                int i = (PrinterFunctions.CheckStatus(portName,portSettings,1));
                                if(i != -1){
                                    printRecipt(selectedBillDetails);

                                }
                                else{
                                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Pos_Printer_Not_Connected,
                                            R.string.OK_Text,R.string.Empty_Text,
                                            new TMCAlertDialogClass.AlertListener() {
                                                @Override
                                                public void onYes() {
                                                    //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onNo() {

                                                }
                                            });
                                }

                            }
                            else {
                                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Please_select_printer_type,
                                        R.string.OK_Text,R.string.Empty_Text,
                                        new TMCAlertDialogClass.AlertListener() {
                                            @Override
                                            public void onYes() {
                                                //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNo() {

                                            }
                                        });
                            }

/*

                            Thread t = new Thread() {
                                public void run() {

                                }
                            };
                            t.start();

 */
                        } catch (Exception e) {
                            e.printStackTrace();

                            new_to_pay_Amount = 0;
                            old_taxes_and_charges_Amount = 0;
                            old_total_Amount = 0;

                        }
                    } else {
                        generatingTokenNo(vendorkey,orderDetailsKey,selectedBillDetails,orderid, customerMobileNo);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

        });
        other_print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
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
                String orderDetailsKey = "";

                try{
                    orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();
                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.useraddress=modal_manageOrders_pojo_class.getUseraddress();
                selectedOrder.orderType=modal_manageOrders_pojo_class.getOrderType();
                selectedOrder.slotname=modal_manageOrders_pojo_class.getSlotname();
                selectedOrder.slotdate=modal_manageOrders_pojo_class.getSlotdate();
                selectedOrder.slottimerange=modal_manageOrders_pojo_class.getSlottimerange();
                selectedOrder.deliverytype = modal_manageOrders_pojo_class.getDeliverytype();
                selectedOrder.notes = modal_manageOrders_pojo_class.getNotes();
                selectedOrder.orderdetailskey = modal_manageOrders_pojo_class.getOrderdetailskey();
                selectedOrder.deliverydistance = modal_manageOrders_pojo_class.getDeliverydistance();
                selectedOrder.deliveryamount = modal_manageOrders_pojo_class.getDeliveryamount();
                selectedOrder.orderplacedtime = modal_manageOrders_pojo_class.getOrderplacedtime();

                selectedBillDetails.add(selectedOrder);
                OrderdItems_desp.clear();
                try {
                    String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
                    if ((tokenNofromArray.length() > 0) && (tokenNofromArray != null) && (!tokenNofromArray.equals(""))) {
                        try {

                            if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                                pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedBillDetails);

                            }
                            else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                                pos_manageOrderFragment.printReciptUsingBluetoothPrinter(selectedBillDetails);

                            }
                            else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                                int i = (PrinterFunctions.CheckStatus(portName,portSettings,1));
                                if(i != -1){
                                    printRecipt(selectedBillDetails);

                                }
                                else{
                                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Pos_Printer_Not_Connected,
                                            R.string.OK_Text,R.string.Empty_Text,
                                            new TMCAlertDialogClass.AlertListener() {
                                                @Override
                                                public void onYes() {
                                                    //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onNo() {

                                                }
                                            });
                                }

                            }
                            else {
                                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Please_select_printer_type,
                                        R.string.OK_Text,R.string.Empty_Text,
                                        new TMCAlertDialogClass.AlertListener() {
                                            @Override
                                            public void onYes() {
                                                //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNo() {

                                            }
                                        });
                            }

                          /*  //pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedBillDetails);
                            Thread t = new Thread() {
                                public void run() {
                                  //  printRecipt(selectedBillDetails);
                                    //pos_manageOrderFragment.printReciptUsingBluetoothPrinter(selectedBillDetails);


                                }
                            };
                            t.start();

                           */
                        } catch (Exception e) {
                            e.printStackTrace();

                            new_to_pay_Amount = 0;
                            old_taxes_and_charges_Amount = 0;
                            old_total_Amount = 0;

                        }
                    }
                    else {
                        generatingTokenNo(vendorkey,orderDetailsKey,selectedBillDetails,orderid, customerMobileNo);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        cancelled_print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Modal_ManageOrders_Pojo_Class>selectedBillDetails =new ArrayList<>();
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();
                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
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
                String orderDetailsKey = "";

                try{
                    orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                selectedOrder.orderstatus=modal_manageOrders_pojo_class.getOrderstatus();
                selectedOrder.usermobile=modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno=modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount=modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.itemdesp=modal_manageOrders_pojo_class.getItemdesp();
                selectedOrder.coupondiscamount=modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.orderType=modal_manageOrders_pojo_class.getOrderType();
                selectedOrder.orderid=modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode=modal_manageOrders_pojo_class.getPaymentmode();
                selectedOrder.useraddress=modal_manageOrders_pojo_class.getUseraddress();
                selectedOrder.slotname=modal_manageOrders_pojo_class.getSlotname();
                selectedOrder.slotdate=modal_manageOrders_pojo_class.getSlotdate();
                selectedOrder.slottimerange=modal_manageOrders_pojo_class.getSlottimerange();
                selectedOrder.deliverytype = modal_manageOrders_pojo_class.getDeliverytype();
                selectedOrder.notes = modal_manageOrders_pojo_class.getNotes();
                selectedOrder.orderdetailskey = modal_manageOrders_pojo_class.getOrderdetailskey();
                selectedOrder.deliveryamount = modal_manageOrders_pojo_class.getDeliveryamount();
                selectedOrder.deliverydistance = modal_manageOrders_pojo_class.getDeliverydistance();
                selectedOrder.orderplacedtime = modal_manageOrders_pojo_class.getOrderplacedtime();



                selectedBillDetails.add(selectedOrder);
                OrderdItems_desp.clear();

                try {
                    String tokenNofromArray = modal_manageOrders_pojo_class.getTokenno().toString();
                    if ((tokenNofromArray.length() > 0) && (tokenNofromArray != null) && (!tokenNofromArray.equals(""))) {
                        try {
                            if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                                pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedBillDetails);

                            }
                            else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                                pos_manageOrderFragment.printReciptUsingBluetoothPrinter(selectedBillDetails);

                            }
                            else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                                int i = (PrinterFunctions.CheckStatus(portName,portSettings,1));
                                if(i != -1){
                                    printRecipt(selectedBillDetails);

                                }
                                else{
                                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Pos_Printer_Not_Connected,
                                            R.string.OK_Text,R.string.Empty_Text,
                                            new TMCAlertDialogClass.AlertListener() {
                                                @Override
                                                public void onYes() {
                                                    //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onNo() {

                                                }
                                            });
                                }
                            }
                            else {
                                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Please_select_printer_type,
                                        R.string.OK_Text,R.string.Empty_Text,
                                        new TMCAlertDialogClass.AlertListener() {
                                            @Override
                                            public void onYes() {
                                                //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNo() {

                                            }
                                        });
                            }

                           /* //     pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedBillDetails);
                            Thread t = new Thread() {
                                public void run() {
                                   // printRecipt(selectedBillDetails);
                                  //  pos_manageOrderFragment.printReciptUsingBluetoothPrinter(selectedBillDetails);
                                    //pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedBillDetails);

                                }
                            };
                            t.start();

                            */
                        } catch (Exception e) {
                            e.printStackTrace();

                            new_to_pay_Amount = 0;
                            old_taxes_and_charges_Amount = 0;
                            old_total_Amount = 0;

                        }
                    } else {
                        generatingTokenNo(vendorkey,orderDetailsKey,selectedBillDetails,orderid, customerMobileNo);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });






        return  listViewItem ;

    }




    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = mContext.getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(mContext.getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem>>() {
            }.getType();
            MenuItem  = gson.fromJson(json, type);
        }

    }



    private void getStockOutGoingDetailsUsingOrderid(String orderid) {
        boolean isinventorycheck = false;
        SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));

        if (isinventorycheck) {



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getStockOutgoingUsingSalesOrderid + orderid, null,
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
                                /*Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                if(arrayLength>1){
                                    Toast.makeText(mContext, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();


                                }

                                 */

                                for (; i1 < (arrayLength); i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        String entryKey = json.getString("key");


                                        ChangeOutGoingTypeInOutgoingTable(entryKey);


                                    } catch (JSONException e) {
                                        pos_manageOrderFragment.showProgressBar(false);

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                pos_manageOrderFragment.showProgressBar(false);


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            pos_manageOrderFragment.showProgressBar(false);


                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(mContext, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();
                    pos_manageOrderFragment.showProgressBar(false);


                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                } catch (Exception e) {
                    e.printStackTrace();
                    pos_manageOrderFragment.showProgressBar(false);

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }
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
                pos_manageOrderFragment.showProgressBar(false);

                Log.d(Constants.TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                pos_manageOrderFragment.showProgressBar(false);

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












    private void generatingTokenNo(String vendorkey, String orderDetailsKey, List<Modal_ManageOrders_Pojo_Class> selectedOrderr, String orderid, String customerMobileNo) {
        pos_manageOrderFragment.showProgressBar(true);
        pos_manageOrderFragment.showOrderInstructionText(false);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_generateTokenNo+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {


                //Log.d(Constants.TAG, "Responsewwwww: " + response);
                try {
                    String tokenNo = response.getString("tokenNumber");
                    OrderdItems_desp.clear();

                    try {
                        for (int i = 0; i < selectedOrderr.size(); i++) {


                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = selectedOrderr.get(i);
                            String orderid_fromArray = modal_manageOrders_pojo_class.getOrderid();
                            if (orderid.equals(orderid_fromArray)) {
                                modal_manageOrders_pojo_class.setTokenno(tokenNo);
                                pos_manageOrderFragment.showProgressBar(false);
                                notifyDataSetChanged();

                                try {
                                    if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                                        pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedOrderr);

                                    }
                                    else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                                        pos_manageOrderFragment.printReciptUsingBluetoothPrinter(selectedOrderr);

                                    }
                                    else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                                        int ii = (PrinterFunctions.CheckStatus(portName,portSettings,1));
                                        if(ii != -1){
                                            printRecipt(selectedOrderr);

                                        }
                                        else{
                                            new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Pos_Printer_Not_Connected,
                                                    R.string.OK_Text,R.string.Empty_Text,
                                                    new TMCAlertDialogClass.AlertListener() {
                                                        @Override
                                                        public void onYes() {
                                                            //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onNo() {

                                                        }
                                                    });
                                        }
                                    }
                                    else {
                                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Please_select_printer_type,
                                                R.string.OK_Text,R.string.Empty_Text,
                                                new TMCAlertDialogClass.AlertListener() {
                                                    @Override
                                                    public void onYes() {
                                                        //Toast.makeText(mContext,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onNo() {

                                                    }
                                                });
                                    }


                                   /* //pos_manageOrderFragment.printReciptUsingUSBPrinter(selectedOrderr);
                                    Thread t = new Thread() {
                                        public void run() {
                                            //printRecipt(selectedOrderr);
                                         //   pos_manageOrderFragment.printReciptUsingBluetoothPrinter(selectedOrderr);


                                        }
                                    };
                                    t.start();

                                    */
                                }
                                catch(Exception e ){
                                    e.printStackTrace();

                                    new_to_pay_Amount=0;
                                    old_taxes_and_charges_Amount=0;
                                    old_total_Amount=0;

                                }
                            }
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                        try{
                            UpdateTokenNoInOrderDetails(tokenNo,orderDetailsKey,orderid,customerMobileNo,vendorkey);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                } catch (JSONException e) {
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

                    e.printStackTrace();
                }
                //Log.d(Constants.TAG, "Responsewwwww: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                pos_manageOrderFragment.showProgressBar(false);

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


    private void UpdateTokenNoInOrderDetails(String tokenNo, String orderDetailsKey, String orderid, String customerMobileNo, String vendorkey) {

        /*
        JSONObject  jsonObject = new JSONObject();
        try {

            jsonObject.put("key", orderDetailsKey);
            jsonObject.put("tokenno", tokenNo);
            //Log.i("tag","listenertoken"+ "");


        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d(Constants.TAG, "JSONOBJECT: " + e);

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
                try {
                    for (int i = 0; i < Pos_ManageOrderFragment.ordersList.size(); i++) {
                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = Pos_ManageOrderFragment.ordersList.get(i);
                        String Orderdetailsorderid = modal_manageOrders_pojo_class.getOrderid();
                        if (orderid.equals(Orderdetailsorderid)) {
                            try {
                                //    String usermobile = modal_manageOrders_pojo_class.getUsermobile();
                                //    String ordertype = modal_manageOrders_pojo_class.getOrderType().toUpperCase();
                                //    if ((ordertype.equals(Constants.APPORDER)) && ((!tokenNo.equals("")) || (!tokenNo.equals("null")) || (tokenNo.length() > 0))) {
                                //         SendTextMessagetoUserUsingApi(usermobile, tokenNo);
                                //      }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            modal_manageOrders_pojo_class.setTokenno(tokenNo);
                            pos_manageOrderFragment.showProgressBar(false);
                            notifyDataSetChanged();

                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    for (int i = 0; i < Pos_ManageOrderFragment.sorted_OrdersList.size(); i++) {
                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = Pos_ManageOrderFragment.sorted_OrdersList.get(i);
                        String Orderdetailsorderid = modal_manageOrders_pojo_class.getOrderid();
                        if (orderid.equals(Orderdetailsorderid)) {
                            modal_manageOrders_pojo_class.setTokenno(tokenNo);
                            pos_manageOrderFragment.showProgressBar(false);
                            notifyDataSetChanged();

                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                //Log.d(Constants.TAG, "Responsewwwww: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                pos_manageOrderFragment.showProgressBar(false);

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

    private void SendTextMessagetoUserUsingApi(String usermobileNo, String tokenNo) {
    String message = tokenNo;

            if (usermobileNo.length() == 10) {

            } else {
                if (usermobileNo.startsWith("+91")) {
                    usermobileNo = usermobileNo.substring(3);
                }
                StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.api_ToSendTextMsgtoUser + "&to=" +usermobileNo+"&message="+message, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e(" response",response);
                        pos_manageOrderFragment.showProgressBar(false);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e(" response",error.toString());
                        pos_manageOrderFragment.showProgressBar(false);

                    }
                }){

                    @Override
                    public Map<String,String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("content-type","application/fesf");
                        return params;
                    }

                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                Volley.newRequestQueue(mContext).add(stringRequest);

          /*  JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_ToSendTextMsgtoUser + "&to=" +usermobileNo+"&message="+message, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            //Log.d(Constants.TAG, "Response: " + response);

                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {

                    //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "Error: " + error.toString());

                    error.printStackTrace();
                }
            }) {


                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> header = new HashMap<>();
                    header.put("Content-Type", "application/json");

                    return header;
                }
            };

            // Make the request
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);


           */

            }


        }

    public void add_amount_ForBillDetails(List<Modal_ManageOrders_Pojo_Class> orderdItems_desp) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for(int i =0; i<orderdItems_desp.size();i++){
            Modal_ManageOrders_Pojo_Class setOrderAmountDetails = orderdItems_desp.get(i);
            double quantity = Double.parseDouble(setOrderAmountDetails.getQuantity());
            //find total amount with out GST
            double new_total_amountfromArray = Double.parseDouble(setOrderAmountDetails.getItemFinalPrice());
            //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);
            new_total_amount = new_total_amountfromArray;
            double newtotalamount_withoutGST = new_total_amount*quantity;
            old_total_Amount = old_total_Amount + newtotalamount_withoutGST;
            setOrderAmountDetails.setSubTotal_PerItemWithoutGst(String.valueOf(decimalFormat.format(newtotalamount_withoutGST)));
            setOrderAmountDetails.setTotalAmountWithoutGst(String.valueOf(decimalFormat.format(old_total_Amount)));



            //find total GST amount
            double taxes_and_chargesfromArray = Double.parseDouble(setOrderAmountDetails.getGstAmount());
            //taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);

            new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
            double subTotal_perItem=new_total_amount+new_taxes_and_charges_Amount;
            subTotal_perItem = subTotal_perItem*quantity;
            setOrderAmountDetails.setSubTotal_perItem(String.valueOf(decimalFormat.format(subTotal_perItem)));

            new_taxes_and_charges_Amount = new_taxes_and_charges_Amount*quantity;
            old_taxes_and_charges_Amount=old_taxes_and_charges_Amount+new_taxes_and_charges_Amount;
            setOrderAmountDetails.setTotalGstAmount(String.valueOf(decimalFormat.format(old_taxes_and_charges_Amount)));




            new_to_pay_Amount =  (old_total_Amount + old_taxes_and_charges_Amount);
            int new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);

            setOrderAmountDetails.setTotalAmountWithGst(String.valueOf(decimalFormat.format(new_totalAmount_withGst)));

        }
//find total payable Amount

        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }



    private void printRecipt(List<Modal_ManageOrders_Pojo_Class>selectedBillDetails) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        PrinterFunctions.PortDiscovery(portName, portSettings);
        PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

        Currenttime = getDate_and_time();

        Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = selectedBillDetails.get(0);
        String orderid = manageOrders_pojo_class.getOrderid();
        double total_subtotal_double = Double.parseDouble(String.valueOf(manageOrders_pojo_class.getPayableamount()));

        int total_subtotalint = (int) Math.round(total_subtotal_double);
        double total_subtotal = 0;
        String payment_mode = "";
        String userMobile = "";
        String tokenno = "";
        String useraddress ="";
        String OrderType ="";
        String deliverytype ="";
        String notes ="";
        String SlotName = "";
        String SlotDate = "";
        String SlotTimeInRange ="";
        String DeliveryDistance ="";
        String DeliveryAmount ="";
        String orderPlacedTime ="";
        double totalSubtotalItem=0;
        double totalSubtotalItemwithdiscount=0;
        double totalSubtotalItemwithdiscountwithdeliverycharge=0;
        double deliveryamount_double=0;

        try {
            orderPlacedTime = manageOrders_pojo_class.getOrderplacedtime().toString();

        }catch (Exception e ){
            e.printStackTrace();
            orderPlacedTime="--";
        }

        try {
            payment_mode = manageOrders_pojo_class.getPaymentmode();

        }catch (Exception e ){
            e.printStackTrace();
        }

        try {
            DeliveryDistance = manageOrders_pojo_class.getDeliverydistance();

        }catch (Exception e ){
            e.printStackTrace();
        }

        try {
            deliverytype = manageOrders_pojo_class.getDeliverytype();

        }catch (Exception e ){
            e.printStackTrace();
        }


        try {
            DeliveryAmount = manageOrders_pojo_class.getDeliveryamount();

        }catch (Exception e ){
            e.printStackTrace();
        }


       try{
           SlotDate = manageOrders_pojo_class.getSlotdate();

        }catch (Exception e){
           e.printStackTrace();

        }


        try {
            notes = manageOrders_pojo_class.getNotes();

        }
        catch (Exception e){
            e.printStackTrace();

        }



        try {
            SlotName = manageOrders_pojo_class.getSlotname();

        }
        catch (Exception e){
            e.printStackTrace();

        }
        try{
            SlotTimeInRange = manageOrders_pojo_class.getSlottimerange();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            OrderType = manageOrders_pojo_class.getOrderType();

        }catch (Exception e){
        e.printStackTrace();
        }
        try{
            useraddress = manageOrders_pojo_class.getUseraddress();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            tokenno = manageOrders_pojo_class.getTokenno();

        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            userMobile = manageOrders_pojo_class.getUsermobile();

        }catch (Exception e){
            e.printStackTrace();
        }



        double couponDiscount_double = 0;
        try {
            if (OrderType.equals(Constants.APPORDER) || OrderType.equals(Constants.PhoneOrder)) {
                couponDiscount_double = Double.parseDouble(String.valueOf(manageOrders_pojo_class.getCoupondiscamount()));

            }
        }
        catch(Exception e){
            e.printStackTrace();
            couponDiscount_double =0;
        }

        try {
            if (OrderType.equals(Constants.POSORDER)) {
                couponDiscount_double = Double.parseDouble(String.valueOf(manageOrders_pojo_class.getCoupondiscamount()));
            }
        }
        catch (Exception e){
            couponDiscount_double =0;
            e.printStackTrace();
        }


        String itemwithoutGst = "", taxAmount = "";
        try {
            JSONArray jsonArray = manageOrders_pojo_class.getItemdesp();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if (json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                    Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();


                    if (marinadesObject.has("itemname")) {
                        marinades_manageOrders_pojo_class.itemName = marinadesObject.getString("itemname");

                    } else {
                        marinades_manageOrders_pojo_class.itemName = "";

                    }



                    if (marinadesObject.has("tmcprice")) {
                        marinades_manageOrders_pojo_class.ItemFinalPrice = marinadesObject.getString("tmcprice");

                    } else {
                        marinades_manageOrders_pojo_class.ItemFinalPrice = " ";

                    }




                    if (marinadesObject.has("quantity")) {
                        marinades_manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));

                    } else {
                        marinades_manageOrders_pojo_class.quantity = "";

                    }



                    if (marinadesObject.has("gstamount")) {
                        marinades_manageOrders_pojo_class.GstAmount = marinadesObject.getString("gstamount");

                    } else {
                        marinades_manageOrders_pojo_class.GstAmount = "";

                    }


                    if (marinadesObject.has("tmcsubctgykey")) {
                        marinades_manageOrders_pojo_class.tmcSubCtgyKey = marinadesObject.getString("tmcsubctgykey");

                    } else {
                        marinades_manageOrders_pojo_class.tmcSubCtgyKey = "";

                    }




                    if (marinadesObject.has("netweight")) {
                        marinades_manageOrders_pojo_class.ItemFinalWeight = String.valueOf(marinadesObject.get("netweight"));
                        manageOrders_pojo_class.netweight = String.valueOf(marinadesObject.getString("netweight"));

                    } else {
                        marinades_manageOrders_pojo_class.ItemFinalWeight = "";
                        marinades_manageOrders_pojo_class.netweight = "";

                    }
                    if (marinadesObject.has("grossweight")) {
                        marinades_manageOrders_pojo_class.grossweight = String.valueOf(marinadesObject.getString("grossweight"));

                    } else {
                        marinades_manageOrders_pojo_class.grossweight = "";

                    }

                    OrderdItems_desp.add(marinades_manageOrders_pojo_class);

                }
                Modal_ManageOrders_Pojo_Class manageOrders_pojo_classs = new Modal_ManageOrders_Pojo_Class();
                if (json.has("netweight")) {
                    manageOrders_pojo_classs.ItemFinalWeight = String.valueOf(json.get("netweight"));
                    manageOrders_pojo_class.netweight = String.valueOf(json.get("netweight"));


                } else {
                    manageOrders_pojo_classs.ItemFinalWeight = "";
                    manageOrders_pojo_classs.netweight = "";

                }
                if (json.has("grossweight")) {
                    manageOrders_pojo_classs.grossweight = String.valueOf(json.get("grossweight"));

                } else {
                    manageOrders_pojo_classs.grossweight = "";

                }
                if (json.has("grossweightingrams")) {
                    manageOrders_pojo_classs.grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                } else {
                    manageOrders_pojo_classs.grossweightingrams = "";

                }
                if (json.has("itemname")) {
                    manageOrders_pojo_classs.itemName = String.valueOf(json.get("itemname"));

                } else {
                    manageOrders_pojo_classs.itemName = "";

                }


                if (json.has("cutname")) {
                    manageOrders_pojo_classs.cutname = String.valueOf(json.get("cutname"));

                } else {
                    manageOrders_pojo_classs.cutname = "";

                }


                if (json.has("tmcprice")) {
                    manageOrders_pojo_classs.ItemFinalPrice = String.valueOf(json.get("tmcprice"));

                } else {
                    manageOrders_pojo_classs.ItemFinalPrice = "";

                }



                if (json.has("quantity")) {
                    manageOrders_pojo_classs.quantity = String.valueOf(json.get("quantity"));

                } else {
                    manageOrders_pojo_classs.quantity = "";

                }



                if (json.has("gstamount")) {
                    manageOrders_pojo_classs.GstAmount = String.valueOf(json.get("gstamount"));

                } else {
                    manageOrders_pojo_classs.GstAmount = "";

                }


                if (json.has("tmcsubctgykey")) {
                    manageOrders_pojo_classs.tmcSubCtgyKey = String.valueOf(json.get("tmcsubctgykey"));

                } else {
                    manageOrders_pojo_classs.tmcSubCtgyKey = "";

                }





                OrderdItems_desp.add(manageOrders_pojo_classs);
                add_amount_ForBillDetails(OrderdItems_desp);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[OrderdItems_desp.size()];


        for (int i = 0; i < OrderdItems_desp.size(); i++) {

            Modal_ManageOrders_Pojo_Class modal_newOrderItems = OrderdItems_desp.get(i);
            String fullitemName ="",tmcSubCtgyKey="",itemName = "",itemNameAfterBraces = "",Gst="",subtotal="",quantity="",price="",cutname="",weight="",netweight="";
            double gst_double=0,subtotal_double=0,price_double=0;
            try{
                fullitemName = String.valueOf(modal_newOrderItems.getItemName());
            }
            catch (Exception e){
                fullitemName = "";
                e.printStackTrace();
            }

            try{
                tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcSubCtgyKey());

            }
            catch (Exception e){
                tmcSubCtgyKey ="";
                e.printStackTrace();
            }
            try {
                if (tmcSubCtgyKey.equals("tmcsubctgy_6") || tmcSubCtgyKey.equals("tmcsubctgy_3")) {
                    int indexofbraces = fullitemName.indexOf("(");
                    int lastindexofbraces = fullitemName.indexOf(")");
                    int lengthofItemname = fullitemName.length();
                    lastindexofbraces = lastindexofbraces+1;

                    if ((indexofbraces >= 0)&&(lastindexofbraces>=0)&&(lastindexofbraces>indexofbraces)) {
                        itemNameAfterBraces = fullitemName.substring(lastindexofbraces,lengthofItemname);

                        itemName = fullitemName.substring(0, indexofbraces);
                        itemName = itemName+itemNameAfterBraces;
                        fullitemName = fullitemName.substring(0, indexofbraces);
                        fullitemName = fullitemName+itemNameAfterBraces;



                    }

                    if ((indexofbraces >= 0)&&(lastindexofbraces>=0)&&(lastindexofbraces==indexofbraces)) {
                       // itemNameAfterBraces = fullitemName.substring(lastindexofbraces,lengthofItemname);

                        itemName = fullitemName.substring(0, indexofbraces);

                        fullitemName = fullitemName.substring(0, indexofbraces);
                        fullitemName = fullitemName;



                    }

                    if (fullitemName.length() > 21) {
                        itemName = fullitemName.substring(0, 21);
                        itemName = itemName + "...";

                        fullitemName = fullitemName.substring(0, 21);
                        fullitemName = fullitemName + "...";
                    }
                    if (fullitemName.length() <= 21) {
                        itemName = fullitemName;

                        fullitemName = fullitemName;

                    }
                } else {


                    /*
                    int indexofbraces = fullitemName.indexOf("(");
                    if (indexofbraces >= 0) {
                        itemName = fullitemName.substring(0, indexofbraces);

                    }
                    if (fullitemName.length() > 21) {
                        itemName = fullitemName.substring(0, 21);
                        itemName = itemName + "...";
                    }
                    if (fullitemName.length() <= 21) {
                        itemName = fullitemName;

                    }

                     */


                    if(fullitemName.contains("(")){
                        int openbraces = fullitemName.indexOf("(");
                        int closebraces = fullitemName.indexOf(")");
                      //  System.out.println(fullitemName);
                        itemName = fullitemName.substring(openbraces+1,closebraces) ;
                     //   System.out.println(itemName);

                    }
                    if(!itemName.matches("[a-zA-Z0-9]+")){
                        fullitemName = fullitemName.replaceAll(
                                "[^a-zA-Z0-9()]", "");
                        fullitemName = fullitemName.replaceAll(
                                "[()]", " ");
                      //  System.out.println("no english");

                      //  System.out.println(fullitemName);

                    }
                    else{
                        fullitemName = fullitemName.replaceAll(
                                "[^a-zA-Z0-9()]", "");
                       // System.out.println("have English");

                    //    System.out.println(fullitemName);

                    }




                }
            }
            catch (Exception e){
                itemName = fullitemName;

                e.printStackTrace();
            }
            try{
                gst_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getGstAmount()));

            }
            catch(Exception e){
                gst_double =0;
                e.printStackTrace();
            }
            try{
                Gst  = String.valueOf(decimalFormat.format(gst_double));

            }
            catch(Exception e){
                Gst ="0";
                e.printStackTrace();
            }


            try{
                subtotal_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getSubTotal_perItem()));

            }
            catch(Exception e){
                subtotal_double = 0;
                e.printStackTrace();
            }


            try{
                subtotal  = String.valueOf(decimalFormat.format(subtotal_double));

            }
            catch(Exception e){
                subtotal = "";
                e.printStackTrace();
            }



            try{
                quantity  = modal_newOrderItems.getQuantity();

            }
            catch(Exception e){
                quantity = "0";
                e.printStackTrace();
            }


            try{
                 price_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getSubTotal_PerItemWithoutGst()));

            }
            catch(Exception e){
                price_double = 0;
                e.printStackTrace();
            }

            try{
                price = String.valueOf(decimalFormat.format(price_double));
            }
            catch(Exception e){
                price  = "0";
                e.printStackTrace();
            }
            try{
                cutname = modal_newOrderItems.getCutname().toString();
            }
            catch(Exception e){
                cutname = "";
                e.printStackTrace();
            }
            try{
                weight = modal_newOrderItems.getItemFinalWeight().toString();
            }
            catch(Exception e){
                weight = "0";
                e.printStackTrace();
            }

            try{
                taxAmount = modal_newOrderItems.getTotalGstAmount().toString();

            }
            catch(Exception e){
                taxAmount     = "0";
                e.printStackTrace();
            }

            try{
                itemwithoutGst = modal_newOrderItems.getTotalAmountWithoutGst().toString();

            }
            catch(Exception e){
                itemwithoutGst = "0";
                e.printStackTrace();
            }

            try{
                netweight = modal_newOrderItems.getNetweight().toString();
            }
            catch(Exception e){
                netweight = "0";
                e.printStackTrace();
            }




            String grossweight ="";

            try {

                try{
                    grossweight = modal_newOrderItems.getGrossweight().toString();

                }
                catch(Exception e){
                    grossweight = "0";
                    e.printStackTrace();
                }
                //Log.i("tag","grossweight Log   1 "+                grossweight);


                    if ((grossweight.equals(""))||(grossweight.equals(null))||(grossweight.equals(" - "))) {
                        try {
                            grossweight = modal_newOrderItems.getGrossweightingrams();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        //Log.i("tag","grossweight Log   2 "+                grossweight);




                }

            }
            catch (Exception e){
                try {
                    if (grossweight.equals("")) {
                        grossweight = modal_newOrderItems.getGrossweightingrams();
                        //Log.i("tag","grossweight Log   3 "+                grossweight);


                    }
                }
                catch (Exception e1){
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }




            PrinterFunctions.SetLineSpacing(portName, portSettings, 130);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 1, 0, 0,"TokenNo: "+tokenno + "\n");



            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0,"Orderid : "+orderid + "\n");

            if(tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                fullitemName = "Grill House "+fullitemName;
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");


            }
            else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                fullitemName = "Ready to Cook "+fullitemName;

                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName  + "\n");

            }
            else  {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, fullitemName + "\n");

            }
            if((cutname.length()>0) && (!cutname.equals("null")) && (!cutname.equals(null))) {

                PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0,  (cutname.toUpperCase()) + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0,"Grossweight : "+ grossweight + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0,"Netweight  : "+weight + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0,"Quantity : "+quantity +"\n");
            PrinterFunctions.PreformCut(portName,portSettings,1);

            try {
                Printer_POJO_ClassArray[i] = new Printer_POJO_Class(grossweight, quantity, orderid, fullitemName, weight, price, "0.00", Gst, subtotal, cutname, "priceperkg_unitprice", "pricetypeforpos", "priceSuffix");
            }
            catch (Exception exception){
                exception.printStackTrace();
                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.PrintFailed_Instruction,
                        R.string.OK_Text,R.string.Empty_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                try {
                                    Thread t = new Thread() {
                                        public void run() {
                                            printRecipt(selectedBillDetails);

                                        }
                                    };
                                    t.start();
                                } catch (Exception e) {
                                    e.printStackTrace();

                                    new_to_pay_Amount = 0;
                                    old_taxes_and_charges_Amount = 0;
                                    old_total_Amount = 0;

                                }
                            }

                            @Override
                            public void onNo() {
                                new_to_pay_Amount = 0;
                                old_taxes_and_charges_Amount = 0;
                                old_total_Amount = 0;
                            }
                        });
            }
        }
        total_subtotal = Double.parseDouble(itemwithoutGst) + Double.parseDouble(taxAmount);
      //  int new_total_subtotal = (int) Math.round(total_subtotal);
          double new_total_subtotal = 0;
          try{
              if(manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.APPORDER)){
                  new_total_subtotal = Double.parseDouble(decimalFormat.format(total_subtotal));
              }
              else{
                  new_total_subtotal = (Math.round(total_subtotal));
              }
          }
          catch (Exception e){
              e.printStackTrace();
          }


        String couponDiscount_string = String.valueOf(couponDiscount_double);
        String totalSubtotal_string = String.valueOf(new_total_subtotal);
        Printer_POJO_Class Printer_POJO_ClassArraytotal = new Printer_POJO_Class(itemwithoutGst, couponDiscount_string, taxAmount, totalSubtotal_string, useraddress);
        //PrinterFunctions.OpenCashDrawer(portName,portSettings,0,4);

        // PrinterFunctions.OpenPort( portName, portSettings);
        //    PrinterFunctions.CheckStatus( portName, portSettings,2);
       /* PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");


        */
        PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

        if((vendorKey.equals("vendor_4")) ||  (vendorKey.equals("wholesalesvendor_1"))) {


            PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "MK Proteins" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

        }
        else if((vendorKey.equals("vendor_6"))) {


            PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "New NS  Bismillah" + "\n");

          //  PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
          //  PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
          //  PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

        }


        else {

            PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");

        }

        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine1 + "\n");


        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine2 + "\n");


        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine3 + "\n");


        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreLanLine + "\n");


        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "GSTIN :33AAJCC0055D1Z9" + "\n");


        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, Currenttime + "\n");


        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "# " + orderid + "\n");

        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Order Placed Time :  "+orderPlacedTime + "\n");

        PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "ITEM NAME" + "\n");

        PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "RATE                            SUBTOTAL" + "\n");

        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
        for (int i = 0; i < Printer_POJO_ClassArray.length; i++) {

            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            String itemrate, Gst, subtotal;
            itemrate = "Rs." + Printer_POJO_ClassArray[i].getItemRate();
            Gst = "Rs." + Printer_POJO_ClassArray[i].getGST();
            subtotal = "Rs." + Printer_POJO_ClassArray[i].getSubTotal();
            if (itemrate.length() == 4) {
                //21spaces
                itemrate = itemrate + "                       ";
            }
            if (itemrate.length() == 5) {
                //20spaces
                itemrate = itemrate + "                    ";
            }
            if (itemrate.length() == 6) {
                //19spaces
                itemrate = itemrate + "                   ";
            }
            if (itemrate.length() == 7) {
                //18spaces
                itemrate = itemrate + "                  ";
            }
            if (itemrate.length() == 8) {
                //17spaces
                itemrate = itemrate + "                 ";
            }
            if (itemrate.length() == 9) {
                //16spaces
                itemrate = itemrate + "                ";
            }
            if (itemrate.length() == 10) {
                //15spaces
                itemrate = itemrate + "               ";
            }
            if (itemrate.length() == 11) {
                //14spaces
                itemrate = itemrate + "              ";
            }
            if (itemrate.length() == 12) {
                //13spaces
                itemrate = itemrate + "             ";
            }
            if (itemrate.length() == 13) {
                //12spaces
                itemrate = itemrate + "            ";
            }
            if (itemrate.length() == 14) {
                //11spaces
                itemrate = itemrate + "           ";
            }
            if (itemrate.length() == 15) {
                //10spaces
                itemrate = itemrate + "          ";
            }
            if (itemrate.length() == 16) {
                //9spaces
                itemrate = itemrate + "         ";
            }
            if (itemrate.length() == 17) {
                //8spaces
                itemrate = itemrate + "        ";
            }
            if (itemrate.length() == 18) {
                //7spaces
                itemrate = itemrate + "       ";
            }


            if (subtotal.length() == 4) {
                //6spaces
                subtotal = "      " + subtotal;
            }
            if (subtotal.length() == 5) {
                //7spaces
                subtotal = "       " + subtotal;
            }

            if (subtotal.length() == 6) {
                //8spaces
                subtotal = "        " + subtotal;
            }
            if (subtotal.length() == 7) {
                //7spaces
                subtotal = "       " + subtotal;
            }
            if (subtotal.length() == 8) {
                //6spaces
                subtotal = "      " + subtotal;
            }
            if (subtotal.length() == 9) {
                //5spaces
                subtotal = "     " + subtotal;
            }
            if (subtotal.length() == 10) {
                //4spaces
                subtotal = "    " + subtotal;
            }
            if (subtotal.length() == 11) {
                //3spaces
                subtotal = "   " + subtotal;
            }
            if (subtotal.length() == 12) {
                //2spaces
                subtotal = "  " + subtotal;
            }
            if (subtotal.length() == 13) {
                //1spaces
                subtotal = " " + subtotal;
            }
            if (subtotal.length() == 14) {
                //no space
                subtotal = "" + subtotal;
            }


                //PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Printer_POJO_ClassArray[i].getItemName() + "  *  " + Printer_POJO_ClassArray[i].getItemWeight() + "(" + Printer_POJO_ClassArray[i].getQuantity() + ")" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, "ItemName : "+Printer_POJO_ClassArray[i].getItemName()+"\n");


            String cutName = "";
            try{
                cutName = String.valueOf(Printer_POJO_ClassArray[i].getCutname().toString());
            }
            catch (Exception e){
                cutName ="";
                e.printStackTrace();
            }

            if((cutName.length()>0) && (!cutName.equals("null")) && (!cutName.equals(null))) {



                PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Cut Name : " +(cutName.toUpperCase()) + "\n");



            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, "Grossweight : "+Printer_POJO_ClassArray[i].getGrossWeight()+"\n");



            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, "Netweight : "+Printer_POJO_ClassArray[i].getItemWeight());

            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, " ,  Quantity : "+"(" + Printer_POJO_ClassArray[i].getQuantity() + ")" + "\n");




            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, itemrate + subtotal + "\n\n");


        }



        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");




        String totalRate = "Rs." + Printer_POJO_ClassArraytotal.getTotalRate();
        String totalGst = "Rs." + Printer_POJO_ClassArraytotal.getTotalGST();
        String totalSubtotal = "Rs." + Printer_POJO_ClassArraytotal.getTotalsubtotal();
        if (totalRate.length() == 7) {
            //18spaces
            totalRate = totalRate + "                  ";
        }
        if (totalRate.length() == 8) {
            //17spaces
            totalRate = totalRate + "                 ";
        }
        if (totalRate.length() == 9) {
            //15spaces
            totalRate = totalRate + "               ";
        }
        if (totalRate.length() == 10) {
            //14spaces
            totalRate = totalRate + "              ";
        }
        if (totalRate.length() == 11) {
            //13spaces
            totalRate = totalRate + "             ";
        }
        if (totalRate.length() == 12) {
            //12spaces
            totalRate = totalRate + "            ";
        }
        if (totalRate.length() == 13) {
            //11spaces
            totalRate = totalRate + "           ";
        }

        if (totalRate.length() == 14) {
            //10spaces
            totalRate = totalRate + "          ";
        }


        if (totalSubtotal.length() == 4) {
            //6spaces
            totalSubtotal = "       " + totalSubtotal;
        } if (totalSubtotal.length() == 5) {
            //7spaces
            totalSubtotal = "        " + totalSubtotal;
        }

        if (totalSubtotal.length() == 6) {
            //8spaces
            totalSubtotal = "         " + totalSubtotal;
        }
        if (totalSubtotal.length() == 7) {
            //7spaces
            totalSubtotal = "        " + totalSubtotal;
        }
        if (totalSubtotal.length() == 8) {
            //6spaces
            totalSubtotal = "       " + totalSubtotal;
        }
        if (totalSubtotal.length() == 9) {
            //5spaces
            totalSubtotal = "      " + totalSubtotal;
        }
        if (totalSubtotal.length() == 10) {
            //4spaces
            totalSubtotal = "     " + totalSubtotal;
        }


        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, totalRate + totalSubtotal + "\n");

        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);

        String CouponDiscount = Printer_POJO_ClassArraytotal.getCouponDiscount();


        if((!CouponDiscount.equals("0.0"))&&(!CouponDiscount.equals("0"))&&(!CouponDiscount.equals("0.00"))&&(CouponDiscount!=(null))&&(!CouponDiscount.equals(""))) {
            if(OrderType.equals(Constants.APPORDER)|| OrderType.equals(Constants.PhoneOrder)){
                if (CouponDiscount.length() == 4) {
                    //20spaces
                    //NEW TOTAL =4
                    CouponDiscount = "  Coupon Discount                    " + CouponDiscount;
                }
                if (CouponDiscount.length() == 5) {
                    //21spaces
                    //NEW TOTAL =5
                    CouponDiscount = "  Coupon Discount                  " + CouponDiscount;
                }
                if (CouponDiscount.length() == 6) {
                    //20spaces
                    //NEW TOTAL =6
                    CouponDiscount = "  Coupon Discount                 " + CouponDiscount;
                }

                if (CouponDiscount.length() == 7) {
                    //19spaces
                    //NEW TOTAL =7
                    CouponDiscount = "  Coupon Discount                " + CouponDiscount;
                }
                if (CouponDiscount.length() == 8) {
                    //18spaces
                    //NEW TOTAL =8
                    CouponDiscount = "  Coupon Discount               " + CouponDiscount;
                }
                if (CouponDiscount.length() == 9) {
                    //17spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Coupon Discount              " + CouponDiscount;
                }
                if (CouponDiscount.length() == 10) {
                    //16spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Coupon Discount             " + CouponDiscount;
                }
                if (CouponDiscount.length() == 11) {
                    //15spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Coupon Discount            " + CouponDiscount;
                }
                if (CouponDiscount.length() == 12) {
                    //14spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Coupon Discount           " + CouponDiscount;
                }

                if (CouponDiscount.length() == 13) {
                    //13spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Coupon Discount          " + CouponDiscount;
                }
            }

            if(OrderType.equals(Constants.POSORDER)){
                if (CouponDiscount.length() == 4) {
                    //20spaces
                    //NEW TOTAL =4
                    CouponDiscount = "  Discount Amount                   " + CouponDiscount;
                }
                if (CouponDiscount.length() == 5) {
                    //21spaces
                    //NEW TOTAL =5
                    CouponDiscount = "  Discount Amount                 " + CouponDiscount;
                }
                if (CouponDiscount.length() == 6) {
                    //20spaces
                    //NEW TOTAL =6
                    CouponDiscount = "  Discount Amount                " + CouponDiscount;
                }

                if (CouponDiscount.length() == 7) {
                    //19spaces
                    //NEW TOTAL =7
                    CouponDiscount = "  Discount Amount               " + CouponDiscount;
                }
                if (CouponDiscount.length() == 8) {
                    //18spaces
                    //NEW TOTAL =8
                    CouponDiscount = " Discount Amount              " + CouponDiscount;
                }
                if (CouponDiscount.length() == 9) {
                    //17spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Discount Amount             " + CouponDiscount;
                }
                if (CouponDiscount.length() == 10) {
                    //16spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Discount Amount            " + CouponDiscount;
                }
                if (CouponDiscount.length() == 11) {
                    //15spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Discount Amount           " + CouponDiscount;
                }
                if (CouponDiscount.length() == 12) {
                    //14spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Discount Amount          " + CouponDiscount;
                }

                if (CouponDiscount.length() == 13) {
                    //13spaces
                    //NEW TOTAL =9
                    CouponDiscount = "  Discount Amount         " + CouponDiscount;
                }
            }


            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, CouponDiscount + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

        }
        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        double netTotalDouble =Double.parseDouble(Printer_POJO_ClassArraytotal.getTotalsubtotal());
        double CouponDiscount_doublee = Double.parseDouble(Printer_POJO_ClassArraytotal.getCouponDiscount());
        netTotalDouble = netTotalDouble-CouponDiscount_doublee;

     try{
         deliveryamount_double = Double.parseDouble(DeliveryAmount);
     }
     catch(Exception e){
         e.printStackTrace();
         deliveryamount_double =0;
     }

        if(deliveryamount_double>0) {
            try{
                netTotalDouble = netTotalDouble+deliveryamount_double;

            }
            catch(Exception e){
                e.printStackTrace();
            }


            DeliveryAmount = "Rs."+DeliveryAmount;


            if (DeliveryAmount.length() == 3) {
                //26spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount                     " + DeliveryAmount;
            }

            if (DeliveryAmount.length() == 4) {
                //25spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount                    " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 5) {
                //25spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount                   " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 6) {
                //23spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount                  " + DeliveryAmount;
            }

            if (DeliveryAmount.length() == 7) {
                //22spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount                 " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 8) {
                //21spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount                " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 9) {
                //20spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount               " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 10) {
                //19spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount              " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 11) {
                //18spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount             " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 12) {
                //17spaces
                //DeliveryAmount =15
                DeliveryAmount = "  Delivery Amount            " + DeliveryAmount;
            }
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, DeliveryAmount + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


        }
        String NetTotal ="Rs."+String.valueOf(netTotalDouble);
        if(NetTotal.length()==4){
            //27spaces
            //NEW TOTAL =9
            NetTotal = "  NET TOTAL                         "+NetTotal;
        }
        if(NetTotal.length()==5){
            //26spaces
            //NEW TOTAL =9
            NetTotal = "  NET TOTAL                         "+NetTotal;
        }
        if(NetTotal.length()==6){
            //25spaces
            //NEW TOTAL =9
            NetTotal = "  NET TOTAL                        "+NetTotal;
        }

        if(NetTotal.length()==7){
            //24spaces
            //NEW TOTAL =9
            NetTotal = "  NET TOTAL                       "+NetTotal;
        }
        if(NetTotal.length()==8){
            //23spaces
            //NEW TOTAL =9
            NetTotal = "  NET TOTAL                      "+NetTotal;
        }
        if(NetTotal.length()==9){
            //22spaces
            //NEW TOTAL =9
            NetTotal =  "  NET TOTAL                     "+NetTotal;
        }
        if(NetTotal.length()==10){
            //21spaces
            //NEW TOTAL =9
            NetTotal =  "  NET TOTAL                    "+NetTotal;
        }
        if(NetTotal.length()==11){
            //20spaces
            //NEW TOTAL =9
            NetTotal = "  NET TOTAL                   "+NetTotal;
        }
        if(NetTotal.length()==12){
            //19spaces
            //NEW TOTAL =9
            NetTotal = "  NET TOTAL                  "+NetTotal;
        }

        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1,  NetTotal+ "\n");

        PrinterFunctions.SetLineSpacing(portName,portSettings,60);
        PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
        PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"----------------------------------------"+"\n");


        PrinterFunctions.SetLineSpacing(portName,portSettings,60);
        PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
        PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Payment Mode: ");


        PrinterFunctions.SetLineSpacing(portName,portSettings,90);
        PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
        PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,30,0,payment_mode+"\n");


        PrinterFunctions.SetLineSpacing(portName,portSettings,100);
        PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
        PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"MobileNo : ");


        PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
        PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,0,userMobile+"         "+"\n");



        if(OrderType.equals(Constants.APPORDER) || OrderType.equals(Constants.PhoneOrder)) {



            PrinterFunctions.SetLineSpacing(portName,portSettings,200);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,2, 2,0,1,"Token No : "+tokenno+"\n");



            PrinterFunctions.SetLineSpacing(portName,portSettings,100);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,1, 0,30,0,"Notes : ");


            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,1, 0,0,0,notes+"  "+"\n\n");


            PrinterFunctions.SetLineSpacing(portName,portSettings,100);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Slot Name : ");


            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,0,SlotName+"         "+"\n");



            PrinterFunctions.SetLineSpacing(portName,portSettings,100);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Slot Date : ");


            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,0,SlotDate+"         "+"\n");


            if(SlotName.equals(Constants.EXPRESSDELIVERY_SLOTNAME)){
                PrinterFunctions.SetLineSpacing(portName,portSettings,100);
                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Order Placed Time  : ");


                PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
                PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,0,orderPlacedTime+"         "+"\n");

            }


            PrinterFunctions.SetLineSpacing(portName,portSettings,100);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Delivery Time  : ");


            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,0,SlotTimeInRange+"         "+"\n");


            PrinterFunctions.SetLineSpacing(portName,portSettings,60);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Delivery  Type: ");


            PrinterFunctions.SetLineSpacing(portName,portSettings,90);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,deliverytype+"\n");



            PrinterFunctions.SetLineSpacing(portName,portSettings,60);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Distance from Store : ");


            PrinterFunctions.SetLineSpacing(portName,portSettings,90);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,DeliveryDistance+"Km"+"\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Address : " + "\n"+ "\n");




            String Address = (Printer_POJO_ClassArraytotal.getUseraddress());


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Address + "         "+"\n"+ "\n");

        }


        PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
        PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,1, "Thank you for choosing us !!!  "+"\n");



        PrinterFunctions.PreformCut(portName,portSettings,1);
        //  PrinterFunctions.PrintSampleReceipt(portName,portSettings);

        new_to_pay_Amount=0;
        old_taxes_and_charges_Amount=0;
        old_total_Amount=0;



    }



    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
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
                    String quantityString = "",itemFinalWeight_String ="";
                    double quantity_double =0,itemfinalWeight_double =0;
                    String subCtgyKey ="",pricetypeforpos ="",grossweightingrams ="",menuitemid ="",inventorydetailsstring ="",itemName = "";
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
                        manageOrders_pojo_class.tmcctgykey = String.valueOf(json.get("tmcctgykey"));


                    }
                    catch (Exception e){
                        manageOrders_pojo_class.tmcctgykey  ="";
                        e.printStackTrace();
                    }


                    try{
                        manageOrders_pojo_class.stockavldetailskey = String.valueOf(json.get("stockavldetailskey"));


                    }
                    catch (Exception e){
                        manageOrders_pojo_class.stockavldetailskey ="";
                        e.printStackTrace();
                    }

                    try{
                        manageOrders_pojo_class.stockbalance = String.valueOf(json.get("stockbalance"));


                    }
                    catch (Exception e){
                        manageOrders_pojo_class.stockbalance  ="";
                        e.printStackTrace();
                    }


                    try{
                        manageOrders_pojo_class.stockincomingkey = String.valueOf(json.get("stockincomingkey"));


                    }
                    catch (Exception e){
                        manageOrders_pojo_class.stockincomingkey ="";
                        e.printStackTrace();
                    }



                    try{
                        manageOrders_pojo_class.isitemAvailable = String.valueOf(json.get("isitemAvailable"));


                    }
                    catch (Exception e){
                        manageOrders_pojo_class.isitemAvailable ="";
                        e.printStackTrace();
                    }



                    try{
                        manageOrders_pojo_class.allownegativestock = String.valueOf(json.get("allownegativestock"));


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
                        //manageOrders_pojo_class.setItemFinalWeight(grossweightingrams);

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





                                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItem.size(); iterator_menuitemStockAvlDetails++) {

                                            Modal_MenuItem modal_menuItemInventoryDetailsItem = MenuItem.get(iterator_menuitemStockAvlDetails);

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
                                               //     modal_manageOrders_pojo_class1.setItemFinalWeight(grossweightingrams);

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

        Adapter_Mobile_changeWeight_in_itemDesp adapter_forOrderDetails_listview = new Adapter_Mobile_changeWeight_in_itemDesp(mContext, OrderdItems_desp,"AppOrdersList",pos_manageOrderFragment,true);
        itemDesp_listview.setAdapter(adapter_forOrderDetails_listview);


        changeReadyForPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Date c = Calendar.getInstance().getTime();

                    SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS",Locale.ENGLISH);
                    day .setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                    String time  = day.format(c);
                    System.out.println("button clicked time " + time);


                    List<Modal_ManageOrders_Pojo_Class> orderdItems_desp_local = new ArrayList<>();
                    orderdItems_desp_local.clear();
                    changeWeight_dialog.hide();
                    pos_manageOrderFragment.showProgressBar(true);
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
                            pos_manageOrderFragment.showProgressBar(false);
                            changeWeight_dialog.show();
                            AlertDialogClass.showDialog(pos_manageOrderFragment.getActivity(),R.string.checkGrossweightCheckboxInstruction);
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
                                pos_manageOrderFragment.showProgressBar(false);
                                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Grossweight_is_not_changed,
                                        R.string.Yes_Text,R.string.No_Text,
                                        new TMCAlertDialogClass.AlertListener() {
                                            @Override
                                            public void onYes() {

                                                new_Order_Linearlayout.setVisibility(View.GONE);
                                                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                                                confirming_order_Linearlayout.setVisibility(View.GONE);
                                                cancelled_Order_Linearlayout.setVisibility(View.GONE);
                                                //mobile_manageOrders1.showProgressBar(false);
                                                ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

                                                notifyDataSetChanged();
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

        SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String time  = day.format(c);
        System.out.println("CalculateStockBalanceAsync method  time " + time);


        mResultCallback_Add_UpdateInventoryEntriesInterface = new Add_UpdateInventoryDetailsEntries_Interface(){

            @Override
            public void notifySuccess(String requestType, String success) {
                changeWeight_dialog.dismiss();
                // Toast.makeText(mContext, "ddddsuccess  " +success, Toast.LENGTH_SHORT).show();
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.VISIBLE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.GONE);

                //mobile_manageOrders1.showProgressBar(false);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);
                    }
                });

                Date c = Calendar.getInstance().getTime();

                SimpleDateFormat day = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SS",Locale.ENGLISH);
                day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
                String time  = day.format(c);
                System.out.println("Success response method  time " + time);

            }

            @Override
            public void notifyError(String requestType, String error) {

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "error  " +error, Toast.LENGTH_SHORT).show();

                        pos_manageOrderFragment.showProgressBar(false);
                        AlertDialogClass.showDialog(pos_manageOrderFragment.getActivity(),R.string.Error_in_updating_GrossWeight);
                    }
                });

                //  ChangeStatusOftheOrder(changestatusto,vendorkey,OrderKey, orderid, customerMobileNo, Currenttime);

            }
        };


        Add_UpdateInventoryDetailsEntries_AsyncTask asyncTask=new Add_UpdateInventoryDetailsEntries_AsyncTask(mContext, mResultCallback_Add_UpdateInventoryEntriesInterface,vendorkey,orderid,customerMobileNo,currenttime,orderdItems_desp,MenuItem);
        asyncTask.execute();







    }







    private void ChangeStatusOftheOrder(String changestatusto, String vendorkey, String orderkey, String orderid, String customerMobileNo, String currenttime) {
        pos_manageOrderFragment.showProgressBar(true);

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
            @Override
            public void onResponse(@NonNull JSONObject response) {
                for(int i=0;i<pos_manageOrderFragment.ordersList.size();i++){
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class= pos_manageOrderFragment.ordersList.get(i);
                    String Orderid_trackingDetails = modal_manageOrders_pojo_class.getOrderid();
                    if(orderid.equals(Orderid_trackingDetails)){
                        modal_manageOrders_pojo_class.setOrderstatus(changestatusto);
                        if(changestatusto.equals(Constants.CONFIRMED_ORDER_STATUS)){
                            modal_manageOrders_pojo_class.setOrderconfirmedtime(currenttime);

                        }
                        if(changestatusto.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)){
                            modal_manageOrders_pojo_class.setOrderreadytime(currenttime);
                        }
                        if(changestatusto.equals(Constants.DELIVERED_ORDER_STATUS)){
                            modal_manageOrders_pojo_class.setOrderdeliveredtime(currenttime);
                        }
                        try{
                            pos_manageOrderFragment.calculate_and_displayno_of_orders_statuswise();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }


                        try{
                            Pos_ManageOrderFragment.sorted_OrdersList.remove(adapterPosition);

                        }
                        catch (Exception e){
                            pos_manageOrderFragment.displayorderDetailsinListview(pos_manageOrderFragment.orderStatus,pos_manageOrderFragment.ordersList, pos_manageOrderFragment.selected_OrderType);

                            e.printStackTrace();
                        }
                        notifyDataSetChanged();
                      /*  if(changestatusto.equals("CONFIRMED")){
                            Intent intent = new Intent(mContext,AssigningDeliveryPartner.class);
                            intent.putExtra("TrackingTableKey",OrderId);
                            mContext.startActivity(intent);
                        }

                       */
                    }
                }
                //Log.d(Constants.TAG, "Responsewwwww: " + response);
                pos_manageOrderFragment.showProgressBar(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                pos_manageOrderFragment.showProgressBar(false);
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
                    Toast.makeText(mContext, "Succesfully Updated  in Customer Details", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                try{
                    Toast.makeText(mContext, "Failed to Update in Customer Details", Toast.LENGTH_SHORT).show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        };
    }


}
