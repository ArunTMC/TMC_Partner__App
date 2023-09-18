package com.meatchop.tmcpartner.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;


import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.NewOrders_MenuItem_Fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Adapter_AutoCompleteDeliveryPartnerName extends ArrayAdapter<Modal_DeliveryPartner> {

    private  Context context;
    List<Modal_DeliveryPartner> deliveryPartnerArrayList=new ArrayList<>();
    private Handler handler;


    public Adapter_AutoCompleteDeliveryPartnerName(@NonNull Context context,  List<Modal_DeliveryPartner> completemenuItem) {
        super(context, 0);
        this.deliveryPartnerArrayList= completemenuItem;
        this.context=context;

    }


    @NonNull
    @Override
    public Filter getFilter() {
        return deliveryPartnerFilter;
    }

    public void setHandler(Handler handler) { this.handler = handler; }


    private void sendHandlerMessage(String bundlestr,Modal_DeliveryPartner modal_deliveryPartner) {
        //Log.i(Constants.TAG,"createBillDetails in AutoComplete");

        Message msg =  new Message();
        Bundle bundle = new Bundle();
        bundle.putString("from", "autoCompleteDeliveryPartnername");
        bundle.putString("key", modal_deliveryPartner.getDeliveryPartnerKey());
        bundle.putString("mobileno", modal_deliveryPartner.getDeliveryPartnerMobileNo());
        bundle.putString("name", modal_deliveryPartner.getDeliveryPartnerName());
        bundle.putString("status", modal_deliveryPartner.getDeliveryPartnerStatus());
        bundle.putString("dropdown", bundlestr);
        msg.setData(bundle);



        handler.sendMessage(msg);

    }



    private Filter deliveryPartnerFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Modal_DeliveryPartner> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(deliveryPartnerArrayList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Modal_DeliveryPartner item : deliveryPartnerArrayList) {
                    if (item.getDeliveryPartnerName().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }
        @Override
        public CharSequence convertResultToString(Object resultValue) {

            return ((Modal_DeliveryPartner) resultValue).getDeliveryPartnerName();
        }
    };



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.deliverypartner_selecting_spinner, parent, false
            );
        }

        TextView deliveryPersonName = (TextView) convertView.findViewById(R.id.deliveryPersonName);
        TextView deliveryPersonMobileno = (TextView) convertView.findViewById(R.id.deliveryPersonMobileno);
        Modal_DeliveryPartner modal_deliveryPartner = deliveryPartnerArrayList.get(position);


        deliveryPersonName.setText(String.valueOf(modal_deliveryPartner.getDeliveryPartnerName()));
        deliveryPersonMobileno.setText(String.valueOf(modal_deliveryPartner.getDeliveryPartnerMobileNo()));
        LinearLayout deliveryPartner_CardLayout = convertView.findViewById(R.id.deliveryPartner_CardLayout);

        //Log.d(TAG, "Auto 2  menu in  Menulist"+menuListFull.size());




        deliveryPartner_CardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendHandlerMessage("dismissdropdowninDeliveryPartnerReport",   deliveryPartnerArrayList.get(position));


            }

        });

        return convertView;
    }



}
