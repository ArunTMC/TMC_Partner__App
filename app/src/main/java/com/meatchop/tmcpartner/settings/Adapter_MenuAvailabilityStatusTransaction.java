package com.meatchop.tmcpartner.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_MenuAvailabilityStatusTransaction extends ArrayAdapter<Modal_MenuAvailabilityStatusTransaction> {
    Context mContext;
    List<Modal_MenuAvailabilityStatusTransaction> ordersList;
    String issubctgyavailabilitychanged="",allowNegativeStock = "" , menuItemKey="",status="",mobileNo="",itemname="",transcationstatus ="",transcationtime ="";




    public Adapter_MenuAvailabilityStatusTransaction(Context mContext, List<Modal_MenuAvailabilityStatusTransaction> ordersList) {
        super(mContext, R.layout.menuavailabilityadapter,  ordersList);

        this.mContext=mContext;
        this.ordersList=ordersList;

    }



    @Nullable
    @Override
    public Modal_MenuAvailabilityStatusTransaction getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_MenuAvailabilityStatusTransaction item) {
        return super.getPosition(item);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.menuavailabilityadapter, (ViewGroup) view, false);

        final TextView negativestocklabel = listViewItem.findViewById(R.id.negativestocklabel);

        final TextView mobilenumber_textview = listViewItem.findViewById(R.id.mobilenumber_textview);
        final TextView status_textview = listViewItem.findViewById(R.id.status_textview);
        final TextView time_textview = listViewItem.findViewById(R.id.time_textview);
        final TextView subctgyitemInstruction_textview = listViewItem.findViewById(R.id.subctgyitemInstruction_textview);

        final TextView allowNegativeStock_status_textview = listViewItem.findViewById(R.id.allowNegativeStock_status_textview);

        final LinearLayout totalLayout =listViewItem.findViewById(R.id.totalLayout);
        
        Modal_MenuAvailabilityStatusTransaction modal_menuAvailabilityStatusTransaction = ordersList.get(pos);


        try {
            menuItemKey = modal_menuAvailabilityStatusTransaction.getMenuItemKeyfromdb();



        }
        catch (Exception e){
            e.printStackTrace();
            menuItemKey="";
        }
        try {
            status = modal_menuAvailabilityStatusTransaction.getStatus().toUpperCase();

        }
        catch (Exception e){
            status="";

            e.printStackTrace();

        }
           try {
               allowNegativeStock = modal_menuAvailabilityStatusTransaction.getAllownegativestock().toUpperCase();

        }
        catch (Exception e){
            allowNegativeStock="";

            e.printStackTrace();

        }


         try {
            mobileNo = modal_menuAvailabilityStatusTransaction.getMobileno();

        }
        catch (Exception e){
            mobileNo = "";
            e.printStackTrace();
        }

        try {
            itemname = modal_menuAvailabilityStatusTransaction.getItemname();

        }
        catch (Exception e){
            e.printStackTrace();
            itemname="";
        }

        try {
            transcationstatus = modal_menuAvailabilityStatusTransaction.getTranscationstatus().toUpperCase();

        }
        catch (Exception e){
            e.printStackTrace();
            transcationstatus="";
        }

        try {
            issubctgyavailabilitychanged = modal_menuAvailabilityStatusTransaction.getIssubctgyavailabilitychanged();

        }
        catch (Exception e){
            e.printStackTrace();
            issubctgyavailabilitychanged="";
        }

         try {
             transcationtime = modal_menuAvailabilityStatusTransaction.getTransactiontime();

        }
        catch (Exception e){
            e.printStackTrace();
            transcationtime="";
        }


        try{
            if(status.equals("TRUE")){
                status = "ON";
            }
            else if(status.equals("FALSE")){
                status ="OFF";
            }
            else{
                status = status;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            status = status;

        }


  try{
            if(allowNegativeStock.equals("TRUE")){
                allowNegativeStock = "ON";
            }
            else if(allowNegativeStock.equals("FALSE")){
                allowNegativeStock ="OFF";
            }
            else{
                allowNegativeStock = allowNegativeStock;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            allowNegativeStock = allowNegativeStock;

        }



        try {
            if ((transcationstatus.equals("SUCCESS"))){
                if(issubctgyavailabilitychanged.toString().toUpperCase().equals("TRUE")) {

                    try {
                        subctgyitemInstruction_textview.setVisibility(View.VISIBLE);
                        totalLayout.setBackgroundColor(mContext.getColor(R.color.TMC_PaleOrange));

                        //
                        //   totalLayout.setBackground(mContext.getResources().getDrawable(R.color.TMC_Orange));
                        subctgyitemInstruction_textview.setText(new StringBuilder().append("All Items in ").append(itemname).append(" has been Turned ").append(status).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    if (menuItemKey.contains("tmcsubctgy")) {



                        try {
                            subctgyitemInstruction_textview.setVisibility(View.VISIBLE);
                            totalLayout.setBackgroundColor(mContext.getColor(R.color.TMC_PaleOrange));

                            //
                            //   totalLayout.setBackground(mContext.getResources().getDrawable(R.color.TMC_Orange));
                            subctgyitemInstruction_textview.setText(new StringBuilder().append("All Items in ").append(itemname).append(" has been Turned ").append(status).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    else {
                        subctgyitemInstruction_textview.setVisibility(View.GONE);
                        totalLayout.setBackgroundColor(mContext.getColor(R.color.TMC_White));
                    }
                }

            }
            else{
                if(issubctgyavailabilitychanged.toString().toUpperCase().equals("TRUE")) {
                    try {
                        subctgyitemInstruction_textview.setVisibility(View.VISIBLE);
                        totalLayout.setBackgroundColor(mContext.getColor(R.color.TMC_PaleOrange));

                        //totalLayout.setBackground(mContext.getResources().getDrawable(R.color.TMC_Orange));
                        subctgyitemInstruction_textview.setText(new StringBuilder().append("Tried to change All  ").append(itemname).append(" Item's Availability but  ").append(transcationstatus).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else{
                    if (menuItemKey.contains("tmcsubctgy")) {



                        try {
                            subctgyitemInstruction_textview.setVisibility(View.VISIBLE);
                            totalLayout.setBackgroundColor(mContext.getColor(R.color.TMC_PaleOrange));

                            //totalLayout.setBackground(mContext.getResources().getDrawable(R.color.TMC_Orange));
                            subctgyitemInstruction_textview.setText(new StringBuilder().append("Tried to change All  ").append(itemname).append(" Item's Availability but  ").append(transcationstatus).toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    else {
                        subctgyitemInstruction_textview.setVisibility(View.VISIBLE);
                        totalLayout.setBackgroundColor(mContext.getColor(R.color.TMC_White));
                        subctgyitemInstruction_textview.setText(new StringBuilder().append("Tried to change All ").append(itemname).append(" Item's Availability but  ").append(transcationstatus).toString());

                    }

                }



            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            mobilenumber_textview.setText(String.format(" %s", mobileNo));
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            time_textview.setText(String.format(" %s", transcationtime));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            status_textview.setText(String.format(" %s", "Turned "+status));
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            if (allowNegativeStock.equals("")) {
                allowNegativeStock_status_textview.setText(String.format(" %s",  allowNegativeStock));
                negativestocklabel.setVisibility(View.GONE);
            } else {
                negativestocklabel.setVisibility(View.VISIBLE);

                allowNegativeStock_status_textview.setText(String.format(" %s", "Turned " + allowNegativeStock));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }




        return  listViewItem ;

    }

}
