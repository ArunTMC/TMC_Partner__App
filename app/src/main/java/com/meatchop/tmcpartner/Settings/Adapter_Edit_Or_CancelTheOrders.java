package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_Edit_Or_CancelTheOrders extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String changestatusto,orderStatus,OrderKey,deliveryPersonName="";
    String Currenttime,MenuItems,orderStatusFromArray,FormattedTime,CurrentDate,formattedDate,CurrentDay,deliverytype;
    public Edit_Or_CancelTheOrders edit_or_cancelTheOrders;
    public static BottomSheetDialog bottomSheetDialog;
    public GenerateOrderDetailsDump generateOrderDetailsDump;
    public GenerateCustomerMobileNo_BillValueReport GenerateCustomerMobileNo_BillValueReport;
    private  boolean isFromGenerateCustomermobile_billvaluereport;
    private  boolean isFromEditOrders;
    private  boolean isFromCancelledOrders;
    public Adapter_Edit_Or_CancelTheOrders(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, Edit_Or_CancelTheOrders edit_or_cancelTheOrders1 ) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);

        this.edit_or_cancelTheOrders = edit_or_cancelTheOrders1;
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.isFromEditOrders = true;
        this.isFromGenerateCustomermobile_billvaluereport=false;
        this.isFromCancelledOrders = false;
    }

    public Adapter_Edit_Or_CancelTheOrders(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, GenerateOrderDetailsDump generateOrderDetailsDump1, boolean b) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);
        this.generateOrderDetailsDump = generateOrderDetailsDump1;
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.isFromEditOrders=false;
        this.isFromGenerateCustomermobile_billvaluereport=false;
        this.isFromCancelledOrders = false;

    }

    public Adapter_Edit_Or_CancelTheOrders(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, boolean b) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.isFromGenerateCustomermobile_billvaluereport=true;
        this.isFromEditOrders = false;
        this.isFromCancelledOrders = false;

    }

    public Adapter_Edit_Or_CancelTheOrders(CancelledOrders mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, boolean b, boolean b1) {
        super(mContext, R.layout.mobile_manage_orders_listview_item1,  ordersList);
        this.mContext=mContext;
        this.ordersList=ordersList;
        this.isFromGenerateCustomermobile_billvaluereport=false;
        this.isFromEditOrders = false;
        this.isFromCancelledOrders = true;
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
        final TextView payableAmount_text_widget = listViewItem.findViewById(R.id.payableAmount_text_widget);
        final TextView slottime_label_widget = listViewItem.findViewById(R.id.slottime_label_widget);


        final LinearLayout order_item_list_parentLayout =listViewItem.findViewById(R.id.order_item_list_parentLayout);
        final LinearLayout tokenNoLayout =listViewItem.findViewById(R.id.tokenNoLayout);
        final LinearLayout slotTimeLayout =listViewItem.findViewById(R.id.slotTimeLayout);
        final LinearLayout slotNameLayout =listViewItem.findViewById(R.id.slotNameLayout);
        final LinearLayout deliveryTypeLayout =listViewItem.findViewById(R.id.deliveryTypeLayout);
        final LinearLayout orderstatus_layout =listViewItem.findViewById(R.id.orderstatus_layout);

        final LinearLayout payableAmountLayout =listViewItem.findViewById(R.id.payableAmountLayout);

        final RelativeLayout buttonsRelativeLayout =listViewItem.findViewById(R.id.buttonsRelativeLayout);

        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);
        orderStatus = modal_manageOrders_pojo_class.getOrderstatus().toUpperCase();
        buttonsRelativeLayout.setVisibility(View.GONE);
        modal_manageOrders_pojo_class.setIsFromEditOrders(String.valueOf(isFromEditOrders));
        modal_manageOrders_pojo_class.setIsFromCancelledOrders(String.valueOf(isFromCancelledOrders));
        modal_manageOrders_pojo_class.setIsFromGenerateCustomermobile_billvaluereport(String.valueOf(isFromGenerateCustomermobile_billvaluereport));
        if(isFromGenerateCustomermobile_billvaluereport){
            payableAmountLayout.setVisibility(View.VISIBLE);
            slotTimeLayout.setVisibility(View.GONE);
            ordertype_text_widget.setVisibility(View.VISIBLE);
            deliveryTypeLayout.setVisibility(View.GONE);
            orderstatus_layout.setVisibility(View.GONE);
            slotNameLayout.setVisibility(View.GONE);

        }
        else{
            payableAmountLayout.setVisibility(View.GONE);
            slotTimeLayout.setVisibility(View.VISIBLE);
            ordertype_text_widget.setVisibility(View.GONE);
            deliveryTypeLayout.setVisibility(View.VISIBLE);
            orderstatus_layout.setVisibility(View.VISIBLE);
            slotNameLayout.setVisibility(View.VISIBLE);
        }


        try {
            payableAmount_text_widget.setText(String.format("Rs . %s", String.valueOf(modal_manageOrders_pojo_class.getPayableamount())));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            deliverytype =  modal_manageOrders_pojo_class.getDeliverytype().toUpperCase();
            deliveryType_text_widget.setText(String.valueOf(deliverytype));
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
        if((modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.POSORDER))||(modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.SwiggyOrder))||(modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.PhoneOrder))){
            slotTimeLayout.setVisibility(View.GONE);
            tokenNoLayout.setVisibility(View.GONE);
            slotdate_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderdeliveredtime()));

        }

        if((modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.SwiggyOrder))||(modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.PhoneOrder))){
            slotTimeLayout.setVisibility(View.VISIBLE);
            slottime_label_widget.setText("Order Type");
            slottime_text_widget.setText(modal_manageOrders_pojo_class.getOrderType().toUpperCase());
        }
        try {

            JSONArray array  = modal_manageOrders_pojo_class.getItemdesp();
            //Log.i("tag","array.length()"+ array.length());
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

                    //Log.i("tag", "array.lengrh(i" + json.length());

                    String itemName = String.valueOf(json.get("itemname"));
                    String price = String.valueOf(json.get("tmcprice"));
                    String quantity = String.valueOf(json.get("quantity"));
                    if (itemDesp.length()>0) {

                        itemDesp = String.format("%s ,\n%s * %s", itemDesp, itemName, quantity);
                    } else {
                        itemDesp = String.format("%s * %s", itemName, quantity);

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
                Intent intent = new Intent (mContext, Edit_Or_CancelOrder_OrderDetails_Screen.class);
                Bundle bundle = new Bundle();
                bundle.putString("From","AppOrdersList");

                bundle.putParcelable("data", modal_manageOrders_pojo_class);
                intent.putExtras(bundle);

                mContext.startActivity(intent);
            }
        });




        return listViewItem;
    }
    }
