package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_DeliverySlotsTransaction extends ArrayAdapter<Modal_DeliverySlots> {

    ArrayList<Modal_DeliverySlots> deliverySlotsArrayList = new ArrayList<>();
    Context mContext;
    String slotDateType ="";


    public Adapter_DeliverySlotsTransaction(Context mContext, ArrayList<Modal_DeliverySlots> deliverySlotsArrayListt, String slotDateType) {
        super(mContext, R.layout.activity_deliveryslotsitemlist, deliverySlotsArrayListt);

        this.mContext = mContext;
        this.deliverySlotsArrayList = deliverySlotsArrayListt;
        this.slotDateType = slotDateType;
    }

    @Nullable
    @Override
    public Modal_DeliverySlots getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_DeliverySlots item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.activity_deliveryslotsitemlist, (ViewGroup) view, false);

        final TextView deliverytiming = listViewItem.findViewById(R.id.deliverytiming);
        final LinearLayout deliveryTiming_layout = listViewItem.findViewById(R.id.deliveryTiming_layout);

        Modal_DeliverySlots modal_deliverySlots = deliverySlotsArrayList.get(pos);

        if(slotDateType.toUpperCase().equals(Constants.TODAYPREORDER_SLOTNAME)) {
            if(modal_deliverySlots.getSlotdatetype().toUpperCase().equals(Constants.TODAYPREORDER_SLOTNAME)) {
                deliverytiming.setText(modal_deliverySlots.getSlotstarttime() +" - "+ modal_deliverySlots.getSlotendtime());
            }
        }
        if(slotDateType.toUpperCase().equals(Constants.TOMORROWPREORDER_SLOTNAME)) {
            if(modal_deliverySlots.getSlotdatetype().toUpperCase().equals(Constants.TOMORROWPREORDER_SLOTNAME)) {
                deliverytiming.setText(modal_deliverySlots.getSlotstarttime() +" - "+ modal_deliverySlots.getSlotendtime());

            }
        }

        deliveryTiming_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext , DeliverySlotsTransactionDetailsReport.class);
                intent.putExtra("deliveryslotkey", modal_deliverySlots.getKey());
                intent.putExtra("deliveryslotname", modal_deliverySlots.getSlotname());
                intent.putExtra("vendorkey", DeliverySlotsListForTransactionReport.vendorkey);
                intent.putExtra("deliverytime", modal_deliverySlots.getSlotstarttime() +" - "+ modal_deliverySlots.getSlotendtime());
                Toast.makeText(mContext, "Slot name : "+ modal_deliverySlots.getSlotdatetype(), Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "Slot time : "+ modal_deliverySlots.getSlotstarttime() +" - "+ modal_deliverySlots.getSlotendtime(), Toast.LENGTH_SHORT).show();
                mContext.startActivity(intent);


            }
        });



        return listViewItem;


    }

}