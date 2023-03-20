package com.meatchop.tmcpartner.settings;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_Mobile_WholeSaleOrderList  extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String orderStatus,OrderKey,orderType;
    String Currenttime,MenuItems,orderStatusFromArray,FormattedTime,CurrentDate,formattedDate,CurrentDay,deliverytype;
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";

    public  WholeSaleOrdersList wholeSaleOrdersList;
    String printerType_sharedPreference="";
    BluetoothAdapter mBluetoothAdapter =null;

    public Adapter_Mobile_WholeSaleOrderList(WholeSaleOrdersList mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, WholeSaleOrdersList wholeSaleOrdersList) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);

        this.wholeSaleOrdersList =  wholeSaleOrdersList;
        this.mContext=mContext;
        this.ordersList=ordersList;
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
        SharedPreferences shared_PF_PrinterData = mContext.getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
        printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));

        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        StoreAddressLine1 = (sharedPreferences.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (sharedPreferences.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (sharedPreferences.getString("VendorPincode", ""));
        StoreLanLine = (sharedPreferences.getString("VendorMobileNumber", ""));

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


        deliveryTypeLayout.setVisibility(View.GONE);
        ordertypeLayout.setVisibility(View.GONE);
        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);


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

        if(orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)){


            ordertype_text_widget.setVisibility(View.VISIBLE);

            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.GONE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);


        }
        else {
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
            ready_Order_Linearlayout.setVisibility(View.GONE);
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
        tokenNoLayout.setVisibility(View.GONE);
        slotTimeLayout.setVisibility(View.GONE);
        slotdate_label_widget.setText("Order Type");
        slotdate_text_widget.setText(orderType);
        other_assignDeliveryperson_button_widget.setVisibility(View.GONE);
        generateTokenNo_button_widget.setVisibility(View.GONE);
        other_assignDeliveryperson_button_widget.setVisibility(View.GONE);
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


        order_item_list_parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (mContext, MobileScreen_OrderDetails1.class);
                Bundle bundle = new Bundle();
                bundle.putString("From","WholeSaleOrdersList");
                final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);

                bundle.putParcelable("data", modal_manageOrders_pojo_class);
                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });



        mobileprint_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    wholeSaleOrdersList.Adjusting_Widgets_Visibility(true);
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);

                    try {
                        if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                            wholeSaleOrdersList.printWholeSaleOrderReceiptUsingUSBPrinter(modal_manageOrders_pojo_class);
                        }
                        else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                            connectBluetoothPrinterReport(modal_manageOrders_pojo_class);

                        }
                        else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                            //printUsingPOSMachineReport(selectedOrder);

                        }
                        else {
                            Toast.makeText(mContext,"ERROR !! There is no Printer Type",Toast.LENGTH_SHORT).show();

                        }


                    }
                    catch(Exception e ){

                        Toast.makeText(mContext,"ERROR !! Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }





                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });






        return listViewItem;
    }

    private void connectBluetoothPrinterReport(Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class) {



        if (BluetoothPrintDriver.IsNoConnection()) {

            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
            wholeSaleOrdersList.Adjusting_Widgets_Visibility(false);

            AlertDialogClass.showDialog(wholeSaleOrdersList,R.string.Printer_is_Disconnected);

        }

        if(!BluetoothPrintDriver.IsNoConnection()){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                wholeSaleOrdersList.Adjusting_Widgets_Visibility(false);

                AlertDialogClass.showDialog(wholeSaleOrdersList,R.string.Printer_is_Disconnected);

            }
            else{
                wholeSaleOrdersList.Adjusting_Widgets_Visibility(true);

                wholeSaleOrdersList.printWholeSaleOrderReceiptUsingBluetoothPrinter(modal_manageOrders_pojo_class);

            }
        }


    }
}
