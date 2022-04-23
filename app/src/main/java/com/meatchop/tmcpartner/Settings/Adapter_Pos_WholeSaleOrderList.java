package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Pos_OrderDetailsScreen;
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.R;
import com.pos.printer.PrinterFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_Pos_WholeSaleOrderList  extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;

    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp = new ArrayList<>();
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;

    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String changestatusto,orderStatus,OrderKey;
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay;
    String deliverytype="";
    double screenInches;
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";

    public  WholeSaleOrdersList wholeSaleOrdersList;
    String printerType_sharedPreference="";
    BluetoothAdapter mBluetoothAdapter =null;


    public Adapter_Pos_WholeSaleOrderList(WholeSaleOrdersList mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, WholeSaleOrdersList wholeSaleOrdersList) {
        super(mContext, R.layout.pos_manageorders_listview_child, ordersList);

        this.wholeSaleOrdersList =  wholeSaleOrdersList;
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.orderStatus="DELIVERED";
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
        SharedPreferences shared_PF_PrinterData = mContext.getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
        printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));
        DisplayMetrics dm = new DisplayMetrics();
        wholeSaleOrdersList.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);
        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        StoreAddressLine1 = (sharedPreferences.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (sharedPreferences.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (sharedPreferences.getString("VendorPincode", ""));
        StoreLanLine = (sharedPreferences.getString("VendorMobileNumber", ""));
        OrderdItems_desp = new ArrayList<>();

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
        //final TextView moblieNo_text_widget = listViewItem.findViewById(R.id.moblieNo_text_widget);
        final TextView tokenNo_text_widget = listViewItem.findViewById(R.id.tokenNo_text_widget);
        final TextView orderDetails_text_widget = listViewItem.findViewById(R.id.orderDetails_text_widget);
        final TextView ordertype_text_widget = listViewItem.findViewById(R.id.ordertype_text);
        final TextView orderPlacedtime_text_widget = listViewItem.findViewById(R.id.orderPlacedtime_text_widget);
        final TextView orderstatus_text_widget = listViewItem.findViewById(R.id.orderstatus_text_widget);
        final TextView ready_for_pickup_delivered_button_widget =listViewItem.findViewById(R.id.ready_for_pickup_delivered_button_widget);
        final TextView deliverytype_text_widget =listViewItem.findViewById(R.id.deliverytype_text_widget);

        //final Button generateTokenNo_text_widget = listViewItem.findViewById(R.id.generateTokenNo);
        // final Button readyorder_generateTokenNo_button_widget = listViewItem.findViewById(R.id.transit_generateTokenNo);
        final TextView slotName_text_widget = listViewItem.findViewById(R.id.slotName_text_widget);
        final TextView slotDate_text_widget = listViewItem.findViewById(R.id.slotDate_text_widget);
        //final TextView slotTime_text_widget = listViewItem.findViewById(R.id.slotTime_text_widget);



        final LinearLayout order_item_list_parentLayout =listViewItem.findViewById(R.id.order_item_list_parentLayout);

        final LinearLayout new_Order_Linearlayout =listViewItem.findViewById(R.id.new_Order_Linearlayout);
        final LinearLayout confirming_order_Linearlayout =listViewItem.findViewById(R.id.confirming_order_Linearlayout);
        final LinearLayout ready_Order_Linearlayout =listViewItem.findViewById(R.id.ready_Order_Linearlayout);
        final LinearLayout cancelled_Order_Linearlayout =listViewItem.findViewById(R.id.cancelled_Order_Linearlayout);
        final LinearLayout slotDate_linearLayout =listViewItem.findViewById(R.id.slotDate_linearLayout);
        // final LinearLayout slotTimeRange_linearLayout =listViewItem.findViewById(R.id.slotTimeRange_linearLayout);

        final TextView confirmed_Order_button_widget = listViewItem.findViewById(R.id.accept_Order_button_widget);
        final TextView cancel_button_widget = listViewItem.findViewById(R.id.cancel_button_widget);

        final TextView ready_for_pickup_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_button_widget);
        final TextView pending_order_print_button_widget = listViewItem.findViewById(R.id.pending_order_print_button_widget);

        final TextView other_print_button_widget = listViewItem.findViewById(R.id.other_print_button_widget);
        final TextView cancelled_print_button_widget = listViewItem.findViewById(R.id.cancelled_print_button_widget);


        SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);


        StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (shared.getString("VendorPincode", ""));
        StoreLanLine = (shared.getString("VendorMobileNumber", ""));


        final TextView changeDeliveryPartner =listViewItem.findViewById(R.id.changeDeliveryPartner);



        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);
        ////Log.i("Tag","Order Pos:   "+ searchOrdersUsingMobileNumber.sorted_OrdersList.get(pos));

        orderStatus = modal_manageOrders_pojo_class.getOrderstatus().toUpperCase();

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

            /*////Log.i("Tag","ItemName   "+String.format(" %s ", modal_manageOrders_pojo_class.getOrder_orderStatus()));

            if(String.format(" %s ", modal_manageOrders_pojo_class.getOrder_orderStatus()).equals("Cancelled")) {
                new_Order_Linearlayout.setVisibility(View.GONE);
                ready_Order_Linearlayout.setVisibility(View.GONE);
                confirming_order_Linearlayout.setVisibility(View.GONE);
                cancelled_Order_Linearlayout.setVisibility(View.VISIBLE);
            }

             */

            //if(String.format(" %s ", modal_manageOrders_pojo_class.getOrder_orderStatus()).equals("Delivered")){

            //               ordertype.setVisibility(View.VISIBLE);
            deliveryPartnerdetailsLayout.setVisibility(View.GONE);

            new_Order_Linearlayout.setVisibility(View.GONE);
            ready_Order_Linearlayout.setVisibility(View.VISIBLE);
            confirming_order_Linearlayout.setVisibility(View.GONE);
            cancelled_Order_Linearlayout.setVisibility(View.GONE);
            //  }

        }

        String orderStatusFromArray = modal_manageOrders_pojo_class.getOrderstatus();
        String SlotName = modal_manageOrders_pojo_class.getSlotname().toUpperCase();
        tokenNo_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getTokenno()));
        String orderid  = (String.format(" %s", modal_manageOrders_pojo_class.getOrderid()));
        orderid_text_widget.setText(String.format("#"+orderid));
        ////Log.i("tag","orderid"+ orderid);
        ordertype_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderType().toUpperCase()));
        orderPlacedtime_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderplacedtime()));
        slotName_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getSlotname()));
        String orderType = String.format(" %s", modal_manageOrders_pojo_class.getOrderType().toUpperCase());

        try {
            orderstatus_text_widget.setVisibility(View.VISIBLE);
            orderstatus_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderstatus()));
        }
        catch (Exception e){
            e.printStackTrace();
            orderstatus_text_widget.setText(String.format(" %s", ""));
            orderstatus_text_widget.setVisibility(View.GONE);

        }
       
      
            slotDate_text_widget.setVisibility(View.GONE);

        

        if(!modal_manageOrders_pojo_class.getOrderstatus().equals(Constants.NEW_ORDER_STATUS)) {

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
            deliveryPartner_mobileNo_widget.setText("Not Assigned");
            deliveryPartner_name_widget.setText("Not Assigned");
            changeDeliveryPartner.setText("Assign Delivery Person");

        }
        try {
            deliverytype = ( modal_manageOrders_pojo_class.getDeliverytype()).toUpperCase();
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
            String b= array.toString();
            modal_manageOrders_pojo_class.setItemdesp_string(b);
            String itemDesp="",subCtgyKey="";

            for(int i=0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);
                if (json.has("marinadeitemdesp")) {
                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
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
                    String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));



                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(marinadesObject.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    itemName = itemName + " Marinade Box ";
                  

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



                } else {

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
                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(json.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                   


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
                  

                }
            }
            orderDetails_text_widget.setText(String.format(itemDesp));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        order_item_list_parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Pos_OrderDetailsScreen.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", modal_manageOrders_pojo_class);
                bundle.putString("From","WholeSaleOrdersList");

                intent.putExtras(bundle);

                mContext.startActivity(intent);

            }
        });
        other_print_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modal_ManageOrders_Pojo_Class selectedOrder = new Modal_ManageOrders_Pojo_Class();

                OrderKey = (String.format("%s", modal_manageOrders_pojo_class.getKeyfromtrackingDetails()));
                String vendorkey = (String.format("%s", modal_manageOrders_pojo_class.getVendorkey()));
                String orderDetailsKey = (String.format("%s", modal_manageOrders_pojo_class.getOrderdetailskey()));
                String ordertype = (String.format("%s", modal_manageOrders_pojo_class.getOrderType()));
                String orderstatus = (String.format("%s", modal_manageOrders_pojo_class.getOrderstatus()));


                selectedOrder.orderstatus = modal_manageOrders_pojo_class.getOrderstatus();

                selectedOrder.usermobile = modal_manageOrders_pojo_class.getUsermobile();
                selectedOrder.tokenno = modal_manageOrders_pojo_class.getTokenno();
                selectedOrder.payableamount = modal_manageOrders_pojo_class.getPayableamount();
                selectedOrder.itemdesp = modal_manageOrders_pojo_class.getItemdesp();
                selectedOrder.orderid = modal_manageOrders_pojo_class.getOrderid();
                selectedOrder.paymentmode = modal_manageOrders_pojo_class.getPaymentmode();
                selectedOrder.coupondiscamount = modal_manageOrders_pojo_class.getCoupondiscamount();
                selectedOrder.useraddress = modal_manageOrders_pojo_class.getUseraddress();
                selectedOrder.orderType = modal_manageOrders_pojo_class.getOrderType();
                selectedOrder.slotname = modal_manageOrders_pojo_class.getSlotname();
                selectedOrder.slotdate = modal_manageOrders_pojo_class.getSlotdate();
                selectedOrder.slottimerange = modal_manageOrders_pojo_class.getSlottimerange();
                selectedOrder.deliverytype = modal_manageOrders_pojo_class.getDeliverytype();
                selectedOrder.notes = modal_manageOrders_pojo_class.getNotes();
                selectedOrder.orderdetailskey = modal_manageOrders_pojo_class.getOrderdetailskey();
                selectedOrder.deliveryamount = modal_manageOrders_pojo_class.getDeliveryamount();
                selectedOrder.orderplacedtime = modal_manageOrders_pojo_class.getOrderplacedtime();

                OrderdItems_desp.clear();
                    try {


                            try {
                                if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                                    wholeSaleOrdersList.printWholeSaleOrderReceiptUsingUSBPrinter(selectedOrder);
                                }
                                else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                                    connectBluetoothPrinterReport(modal_manageOrders_pojo_class);


                                }
                                else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                                    printUsingPOSMachineReport(selectedOrder);

                                }
                                else {
                                    Toast.makeText(mContext,"ERROR !! There is no Printer Type",Toast.LENGTH_SHORT).show();

                                }


                            }
                            catch(Exception e ){

                                Toast.makeText(mContext,"ERROR !! Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

                                e.printStackTrace();

                            }

                        
                        
                        
                       

                    } catch (Exception e) {
                        e.printStackTrace();

                        new_to_pay_Amount = 0;
                        old_taxes_and_charges_Amount = 0;
                        old_total_Amount = 0;

                    }
               
            }
        });



        return  listViewItem ;

    }







    public void add_amount_ForBillDetails(List<Modal_ManageOrders_Pojo_Class> orderdItems_desp) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for(int i =0; i<orderdItems_desp.size();i++){
            Modal_ManageOrders_Pojo_Class setOrderAmountDetails = orderdItems_desp.get(i);
            double quantity = Double.parseDouble(setOrderAmountDetails.getQuantity());
            //find total amount with out GST
            double new_total_amountfromArray = Double.parseDouble(setOrderAmountDetails.getItemFinalPrice());
            ////Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);
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



    private void printUsingPOSMachineReport(Modal_ManageOrders_Pojo_Class manageOrders_pojo_class) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        PrinterFunctions.PortDiscovery(portName, portSettings);
        PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

        Currenttime = getDate_and_time();

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
        String SlotName = "";
        String SlotDate = "";
        String SlotTimeInRange ="";
        String deliverydistance= "";
        String notes ="";
        String DeliveryAmount ="";
        String orderPlacedTime ="";

        String subCtgyKey ="";
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
            deliverydistance = manageOrders_pojo_class.getDeliverydistance();

        }
        catch (Exception e){
            deliverydistance ="";
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
            if (OrderType.equals(Constants.APPORDER)) {
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
                        marinades_manageOrders_pojo_class.itemName = " ";

                    }



                    if (marinadesObject.has("tmcprice")) {
                        marinades_manageOrders_pojo_class.ItemFinalPrice = marinadesObject.getString("tmcprice");

                    } else {
                        marinades_manageOrders_pojo_class.ItemFinalPrice = "  ";

                    }



                    if (marinadesObject.has("quantity")) {
                        marinades_manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));

                    } else {
                        marinades_manageOrders_pojo_class.quantity = " ";

                    }



                    if (marinadesObject.has("gstamount")) {
                        marinades_manageOrders_pojo_class.GstAmount = marinadesObject.getString("gstamount");

                    } else {
                        marinades_manageOrders_pojo_class.GstAmount = "  ";

                    }


                    if (marinadesObject.has("tmcsubctgykey")) {
                        marinades_manageOrders_pojo_class.tmcSubCtgyKey = marinadesObject.getString("tmcsubctgykey");

                    } else {
                        marinades_manageOrders_pojo_class.tmcSubCtgyKey = "0";

                    }




                    if (marinadesObject.has("netweight")) {
                        marinades_manageOrders_pojo_class.ItemFinalWeight = String.valueOf(marinadesObject.get("netweight"));
                        manageOrders_pojo_class.netweight = String.valueOf(marinadesObject.getString("netweight"));

                    } else {
                        marinades_manageOrders_pojo_class.ItemFinalWeight = " - ";
                        marinades_manageOrders_pojo_class.netweight = " - ";

                    }
                    if (marinadesObject.has("grossweight")) {
                        marinades_manageOrders_pojo_class.grossweight = String.valueOf(marinadesObject.getString("grossweight"));

                    } else {
                        marinades_manageOrders_pojo_class.grossweight = " - ";

                    }

                    OrderdItems_desp.add(marinades_manageOrders_pojo_class);

                }
                Modal_ManageOrders_Pojo_Class manageOrders_pojo_classs = new Modal_ManageOrders_Pojo_Class();
                if (json.has("netweight")) {
                    manageOrders_pojo_classs.ItemFinalWeight = String.valueOf(json.get("netweight"));
                    manageOrders_pojo_class.netweight = String.valueOf(json.get("netweight"));


                } else {
                    manageOrders_pojo_classs.ItemFinalWeight = " - ";
                    manageOrders_pojo_classs.netweight = " - ";

                }


                if (json.has("cutname")) {
                    manageOrders_pojo_classs.cutname = String.valueOf(json.get("cutname"));

                } else {
                    manageOrders_pojo_classs.cutname = "";

                }


                if (json.has("grossweight")) {
                    manageOrders_pojo_classs.grossweight = String.valueOf(json.get("grossweight"));

                } else {
                    manageOrders_pojo_classs.grossweight = " - ";

                }
                if (json.has("itemname")) {
                    manageOrders_pojo_classs.itemName = String.valueOf(json.get("itemname"));

                } else {
                    manageOrders_pojo_classs.itemName = " ";

                }



                if (json.has("tmcprice")) {
                    manageOrders_pojo_classs.ItemFinalPrice = String.valueOf(json.get("tmcprice"));

                } else {
                    manageOrders_pojo_classs.ItemFinalPrice = "  ";

                }



                if (json.has("quantity")) {
                    manageOrders_pojo_classs.quantity = String.valueOf(json.get("quantity"));

                } else {
                    manageOrders_pojo_classs.quantity = " ";

                }



                if (json.has("gstamount")) {
                    manageOrders_pojo_classs.GstAmount = String.valueOf(json.get("gstamount"));

                } else {
                    manageOrders_pojo_classs.GstAmount = "  ";

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
            String fullitemName = String.valueOf(modal_newOrderItems.getItemName());
            String itemName = "";
            String itemNameAfterBraces = "";

            String tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcSubCtgyKey());
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
                    /*int indexofbraces = fullitemName.indexOf("(");
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
                        System.out.println(fullitemName);
                        itemName = fullitemName.substring(openbraces+1,closebraces) ;
                        System.out.println(itemName);

                    }
                    if(!itemName.matches("[a-zA-Z0-9]+")){
                        fullitemName = fullitemName.replaceAll(
                                "[^a-zA-Z0-9()]", "");
                        fullitemName = fullitemName.replaceAll(
                                "[()]", " ");
                        System.out.println("no english");

                        System.out.println(fullitemName);

                    }
                    else{
                        fullitemName = fullitemName.replaceAll(
                                "[^a-zA-Z0-9()]", "");
                        System.out.println("have English");

                        System.out.println(fullitemName);

                    }






                }
            }
            catch (Exception e){
                itemName = fullitemName;

                e.printStackTrace();
            }
            String cutname ="";
            try{
                cutname = modal_newOrderItems.getCutname().toString();
            }
            catch(Exception e){
                cutname = "0";
                e.printStackTrace();
            }


            double gst_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getGstAmount()));
            String Gst = String.valueOf(decimalFormat.format(gst_double));
            double subtotal_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getSubTotal_perItem()));

            String subtotal = String.valueOf(decimalFormat.format(subtotal_double));
            String quantity = modal_newOrderItems.getQuantity();
            double price_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getSubTotal_PerItemWithoutGst()));

            String price = String.valueOf(decimalFormat.format(price_double));
            String weight = modal_newOrderItems.getItemFinalWeight();
            taxAmount = modal_newOrderItems.getTotalGstAmount();
            itemwithoutGst = modal_newOrderItems.getTotalAmountWithoutGst();
            String netweight = modal_newOrderItems.getNetweight();
            String grossweight = modal_newOrderItems.getGrossweight();





            PrinterFunctions.SetLineSpacing(portName, portSettings, 130);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 1, 0, 0,"TokenNo: "+tokenno + "\n");



            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0,"Orderid : "+orderid + "\n");
            if(tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, "Grill House "+fullitemName + "\n");


            }
            else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 100);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 1, 0, 30, 0, "Ready to Cook "+fullitemName + "\n");

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


            Printer_POJO_ClassArray[i] = new Printer_POJO_Class(grossweight, quantity, orderid, fullitemName, weight, price, "0.00", Gst, subtotal, cutname);

        }
        total_subtotal = Double.parseDouble(itemwithoutGst) + Double.parseDouble(taxAmount);
        int new_total_subtotal = (int) Math.round(total_subtotal);

        String couponDiscount_string = String.valueOf(couponDiscount_double);
        String totalSubtotal_string = String.valueOf(new_total_subtotal);
        Printer_POJO_Class Printer_POJO_ClassArraytotal = new Printer_POJO_Class(itemwithoutGst, couponDiscount_string, taxAmount, totalSubtotal_string, useraddress);
        //PrinterFunctions.OpenCashDrawer(portName,portSettings,0,4);

        // PrinterFunctions.OpenPort( portName, portSettings);
        //    PrinterFunctions.CheckStatus( portName, portSettings,2);
        PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");


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


        PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "ITEM NAME * QTY" + "\n");

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


            //  PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Printer_POJO_ClassArray[i].getItemName() + "  *  " + Printer_POJO_ClassArray[i].getItemWeight() + "(" + Printer_POJO_ClassArray[i].getQuantity() + ")" + "\n");



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
            if(OrderType.equals(Constants.APPORDER)){
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



        if(OrderType.equals(Constants.APPORDER)) {



            PrinterFunctions.SetLineSpacing(portName,portSettings,200);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,2, 2,0,1,"Token No : "+tokenno+"\n");


            PrinterFunctions.SetLineSpacing(portName,portSettings,100);
            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,"Notes : ");


            PrinterFunctions.SelectCharacterFont(portName,portSettings,0);
            PrinterFunctions. PrintText(portName,portSettings,0, 0,0,0,0, 0,0,0,notes+"  "+"\n");


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
            PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,0,deliverydistance+"Km"+"\n");


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
        ////Log.i("tag","printer Log    "+                PrinterFunctions.PortDiscovery(portName,portSettings));

        ////Log.i("tag","printer Log    "+                PrinterFunctions.OpenPort( portName, portSettings));

        ////Log.i("tag","printer Log    "+        PrinterFunctions.CheckStatus( portName, portSettings,2));

        new_to_pay_Amount=0;
        old_taxes_and_charges_Amount=0;
        old_total_Amount=0;



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
