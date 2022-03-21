package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Replacement_RefundClasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.itextpdf.text.ExceptionConverter;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order.Modal_ReplacementOrderDetails;
import com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order.Replacement_Refund_OrderDetailsScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;


public class Adapter_Replacement_Refund_List extends ArrayAdapter<Modal_ReplacementOrderDetails> {
    Context mContext;
    List<Modal_ReplacementOrderDetails> markedOrdersList;
    ReplacementRefundListFragment replacementRefundListFragment;
    String replacementAmount_String,refundAmount_String,totalAmountUserCanAvl_String;
    double replacementAmount_Double =0 ,refundAmount_Double = 0,totalAmountUserCanAvl_Double = 0, balanceAmount = 0;

    public Adapter_Replacement_Refund_List(Context mContext, List<Modal_ReplacementOrderDetails> markedOrdersList, ReplacementRefundListFragment replacementRefundListFragment) {
        super(mContext, R.layout.replacement_refund_itemslist, markedOrdersList);
        this.mContext=mContext;
        this.markedOrdersList=markedOrdersList;
        this.replacementRefundListFragment = replacementRefundListFragment;
    }


    @Nullable
    @Override
    public Modal_ReplacementOrderDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_ReplacementOrderDetails item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.replacement_refund_itemslist, (ViewGroup) view, false);

        final TextView orderid_text_widget = listViewItem.findViewById(R.id.orderid_text_widget);
        final TextView orderplaceddate_text_widget = listViewItem.findViewById(R.id.orderplaceddate_text_widget);
        final TextView moblieNo_text_widget = listViewItem.findViewById(R.id.moblieNo_text_widget);
        final TextView status_text_widget = listViewItem.findViewById(R.id.status_text_widget);
        final TextView markeddate_text_widget = listViewItem.findViewById(R.id.markeddate_text_widget);

        final TextView amountusercanavl_text_widget = listViewItem.findViewById(R.id.amountusercanavl_text_widget);
        final TextView totalreplacementamount_text_widget = listViewItem.findViewById(R.id.totalreplacementamount_text_widget);
        final TextView totalrefundamount_text_widget = listViewItem.findViewById(R.id.totalrefundamount_text_widget);
        final TextView balanceamount_text_widget = listViewItem.findViewById(R.id.balanceamount_text_widget);
        final TextView markedItemDesp_text_widget = listViewItem.findViewById(R.id.markedItemDesp_text_widget);

        final LinearLayout cardView_parentLayout = listViewItem.findViewById(R.id.cardView_parentLayout);
        final LinearLayout order_item_list_parentLayout = listViewItem.findViewById(R.id.order_item_list_parentLayout);

        Modal_ReplacementOrderDetails  modal_replacementOrderDetails = markedOrdersList.get(pos);
        try{

        if(modal_replacementOrderDetails.getStatus().toString().toUpperCase().equals("COMPLETED")){

            cardView_parentLayout.setBackground(getDrawable(mContext,R.color.TMC_PaleOrange));
        }
        else{
            cardView_parentLayout.setBackground(getDrawable(mContext,R.color.TMC_White));

        }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            orderid_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getOrderid()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderplaceddate_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getOrderplaceddate()));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            moblieNo_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getMobileno()));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            status_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getStatus()));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            markeddate_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getMarkeddate()));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            amountusercanavl_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getAmountusercanavl()));
            totalAmountUserCanAvl_String = String.valueOf(modal_replacementOrderDetails.getAmountusercanavl());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            totalreplacementamount_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getTotalreplacementamount()));
            replacementAmount_String = String.valueOf(modal_replacementOrderDetails.getTotalreplacementamount());

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            totalrefundamount_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getTotalrefundedamount()));
            refundAmount_String = String.valueOf(modal_replacementOrderDetails.getTotalrefundedamount());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            refundAmount_Double = Math.round(Double.parseDouble(refundAmount_String));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            replacementAmount_Double = Math.round(Double.parseDouble(replacementAmount_String));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalAmountUserCanAvl_Double = Math.round(Double.parseDouble(totalAmountUserCanAvl_String));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            balanceAmount = totalAmountUserCanAvl_Double - (replacementAmount_Double+refundAmount_Double);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            balanceamount_text_widget.setText(String.valueOf(Math.round(balanceAmount)));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {

            JSONArray array  = modal_replacementOrderDetails.getItemsmarked_Array();
            if(array.length()==0){
                 array = new JSONArray(modal_replacementOrderDetails.getItemsmarked_String());
            }
            //Log.i("tag","array.length()"+ array.length());
            String arraystring= array.toString();
            modal_replacementOrderDetails.setItemsmarked_String(arraystring);
            String itemDesp="";
            String subCtgyKey ="";

            for(int i=0; i < array.length(); i++) {
                JSONObject json = array.getJSONObject(i);

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
            markedItemDesp_text_widget.setText(String.format(itemDesp));

        } catch (JSONException e) {
            e.printStackTrace();
            modal_replacementOrderDetails.setItemsmarked_String("");

        }

        try{
            JSONArray array  = modal_replacementOrderDetails.getRefunddetails_Array();
            //Log.i("tag","array.length()"+ array.length());
            String arraystring= array.toString();
            modal_replacementOrderDetails.setRefunddetails_String(arraystring);

        }
        catch (Exception e){
            modal_replacementOrderDetails.setRefunddetails_String("");

            e.printStackTrace();
        }

        try{
            JSONArray array  = modal_replacementOrderDetails.getReplacementdetails_Array();
            //Log.i("tag","array.length()"+ array.length());
            String arraystring= array.toString();
            modal_replacementOrderDetails.setReplacementdetails_String(arraystring);

        }
        catch (Exception e){
            modal_replacementOrderDetails.setReplacementdetails_String("");

            e.printStackTrace();
        }

        order_item_list_parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Modal_ReplacementOrderDetails  modal_replacementOrderDetails = markedOrdersList.get(pos);

                Intent intent = new Intent(mContext, AddReplacement_Refund_OrdersScreen.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", modal_replacementOrderDetails);

                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        return listViewItem;

    }



}
