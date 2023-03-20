package com.meatchop.tmcpartner.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import java.util.List;

public class AdapterPlotOrdersLocation extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;
    boolean isSearchButtonClicked =false;
    List<Modal_ManageOrders_Pojo_Class> orderslist;
    PlotOrdersLocationWithTokenNo plotOrdersLocationWithTokenNo;
    public AdapterPlotOrdersLocation(Context mContext, List<Modal_ManageOrders_Pojo_Class> orderslist, PlotOrdersLocationWithTokenNo plotOrdersLocationWithTokenNo,Boolean isSearchButtonClicked) {
        super(mContext, R.layout.plot_order_location_listview_childitem, orderslist);
        this.plotOrdersLocationWithTokenNo = plotOrdersLocationWithTokenNo;
        this.mContext=mContext;
        this.orderslist = orderslist;
        this.isSearchButtonClicked = isSearchButtonClicked;


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
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.plot_order_location_listview_childitem, (ViewGroup) view, false);
        final TextView mobileNo = listViewItem.findViewById(R.id.mobileNo);
        final TextView tokenNo = listViewItem.findViewById(R.id.tokenNo);
        final TextView address = listViewItem.findViewById(R.id.addreess);
        final TextView storepickup_textview = listViewItem.findViewById(R.id.storepickup_textview);

        final CheckBox checkBox = listViewItem.findViewById(R.id.checkBox);
        final LinearLayout checkbox_linearLayout = listViewItem.findViewById(R.id.checkbox_linearLayout);
        final LinearLayout parent_linearLayout = listViewItem.findViewById(R.id.parent_linearLayout);

        String mobileno_String = "nil",tokenno_string = "nil",address_string = "nil",isOrdersChecked ="false";
        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = orderslist.get(pos);

        if(modal_manageOrders_pojo_class.getDeliverytype().toString().toUpperCase().equals(Constants.STOREPICKUP_DELIVERYTYPE)){
            checkBox.setVisibility(View.GONE);
            checkbox_linearLayout.setVisibility(View.GONE);

            storepickup_textview.setVisibility(View.VISIBLE);
        }
        else{
            checkBox.setVisibility(View.VISIBLE);
            checkbox_linearLayout.setVisibility(View.VISIBLE);
            storepickup_textview.setVisibility(View.GONE);
        }

        try {
            mobileno_String   = modal_manageOrders_pojo_class.getUsermobile();
        }
        catch(Exception e){

            e.printStackTrace();

        }


        try {
            isOrdersChecked   = modal_manageOrders_pojo_class.getIsOrdersChecked();
        }
        catch(Exception e){

            e.printStackTrace();

        }



        try {
            tokenno_string   = modal_manageOrders_pojo_class.getTokenno();
        }
        catch(Exception e){

            e.printStackTrace();

        }


        try {
            address_string   = modal_manageOrders_pojo_class.getUseraddress();
        }
        catch(Exception e){

            e.printStackTrace();

        }


        try {
            address.setText(address_string);
            tokenNo.setText(tokenno_string);
            mobileNo.setText(mobileno_String);
        }
        catch(Exception e){

            e.printStackTrace();

        }
        try {

            checkBox.setChecked(Boolean.parseBoolean(isOrdersChecked));
        }
        catch(Exception e){

            e.printStackTrace();

        }

        checkbox_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isorderChecked = checkBox.isChecked();

                if(isorderChecked){
                    checkBox.setChecked(false);
                    plotOrdersLocationWithTokenNo.selectedOrders.remove(modal_manageOrders_pojo_class.getTokenno());
                    plotOrdersLocationWithTokenNo.sorted_OrdersList.get(pos).setIsOrdersChecked("false");
                }
                else
                {
                    checkBox.setChecked(true);
                    plotOrdersLocationWithTokenNo.selectedOrders.add(modal_manageOrders_pojo_class.getTokenno());
                    plotOrdersLocationWithTokenNo.sorted_OrdersList.get(pos).setIsOrdersChecked("true");

                }
                notifyDataSetChanged();
            }
        });

        parent_linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isorderChecked = checkBox.isChecked();

                if(isorderChecked){
                    checkBox.setChecked(false);
                    plotOrdersLocationWithTokenNo.selectedOrders.remove(modal_manageOrders_pojo_class.getTokenno());
                    plotOrdersLocationWithTokenNo.sorted_OrdersList.get(pos).setIsOrdersChecked("false");
                }
                else
                    {
                    checkBox.setChecked(true);
                    plotOrdersLocationWithTokenNo.selectedOrders.add(modal_manageOrders_pojo_class.getTokenno());
                    plotOrdersLocationWithTokenNo.sorted_OrdersList.get(pos).setIsOrdersChecked("true");

                    }
                notifyDataSetChanged();
            }
        });


        return  listViewItem ;
    }




}

