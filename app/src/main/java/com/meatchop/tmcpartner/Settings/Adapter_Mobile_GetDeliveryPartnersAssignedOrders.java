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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.MobileScreen_OrderDetails1;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Mobile_ManageOrders1;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_Mobile_GetDeliveryPartnersAssignedOrders extends ArrayAdapter<Modal_ManageOrders_Pojo_Class>{
Context mContext;
String portName = "USB";
int portSettings=0,totalGstAmount=0;
List<Modal_ManageOrders_Pojo_Class> ordersList;
String changestatusto,orderStatus,OrderKey,ordertype;
String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay;
public GetDeliverypartnersAssignedOrders mobile_manageOrders1;




public Adapter_Mobile_GetDeliveryPartnersAssignedOrders(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, GetDeliverypartnersAssignedOrders mobile_manageOrders1) {
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
            final LinearLayout tokenNoLayout =listViewItem.findViewById(R.id.tokenNoLayout);
            final LinearLayout orderstatus_layout =listViewItem.findViewById(R.id.orderstatus_layout);
            final LinearLayout slotDateLayout =listViewItem.findViewById(R.id.slotDateoLayout);
            final LinearLayout ordertypeLayout =listViewItem.findViewById(R.id.ordertypeLayout);
            final RelativeLayout buttonsRelativeLayout =listViewItem.findViewById(R.id.buttonsRelativeLayout);




            final Button confirmed_Order_button_widget = listViewItem.findViewById(R.id.accept_Order_button_widget);
            final Button cancel_button_widget = listViewItem.findViewById(R.id.cancel_button_widget);

            final Button ready_for_pickup_button_widget = listViewItem.findViewById(R.id.ready_for_pickup_button_widget);
            final Button pending_order_print_button_widget = listViewItem.findViewById(R.id.pending_order_print_button_widget);

            final Button other_print_button_widget = listViewItem.findViewById(R.id.other_print_button_widget);
            final Button cancelled_print_button_widget = listViewItem.findViewById(R.id.cancelled_print_button_widget);
            final Button generateTokenNo_button_widget = listViewItem.findViewById(R.id.generateTokenNo_button_widget);
            final Button transit_generateTokenNo_button_widget = listViewItem.findViewById(R.id.transit_generateTokenNo_button_widget);

            final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(pos);
            Log.i("Tag","Order Pos:   "+ mobile_manageOrders1.ordersList.get(pos));



            try {
                orderstatus_text_widget.setText(String.format(" %s", modal_manageOrders_pojo_class.getOrderstatus()));
                orderStatus = modal_manageOrders_pojo_class.getOrderstatus();

            }
            catch (Exception e){
                e.printStackTrace();
                orderStatus="";
            }
            buttonsRelativeLayout.setVisibility(View.GONE);
            ordertypeLayout.setVisibility(View.GONE);
            ordertype_text_widget.setVisibility(View.GONE);
            new_Order_Linearlayout.setVisibility(View.GONE);
            generateTokenNo_button_widget.setVisibility(View.GONE);
            transit_generateTokenNo_button_widget.setVisibility(View.GONE);
            if(orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)){
                ready_for_pickup_button_widget.setVisibility(View.VISIBLE);
            }
            else{
                ready_for_pickup_button_widget.setVisibility(View.GONE);
            }

            order_item_list_parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (mContext, MobileScreen_OrderDetails1.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("From","MobileGetDeliveryPartnerAssignedOrder");
                    bundle.putParcelable("data", modal_manageOrders_pojo_class);
                    intent.putExtras(bundle);

                    mContext.startActivity(intent);
                }
            });





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


            return  listViewItem ;

        }

}