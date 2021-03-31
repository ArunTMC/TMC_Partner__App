package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_TodaysDeliverySlots extends ArrayAdapter<Modal_DeliverySlots> {
    Context mContext;

    List<Modal_DeliverySlots> slotlist;
    ChangeDelivery_Slot_Availability_Status changeDelivery_slot_availability_status;
    public Adapter_TodaysDeliverySlots(Context mContext, List<Modal_DeliverySlots> slotlist, ChangeDelivery_Slot_Availability_Status changeDelivery_slot_availability_status) {
        super(mContext, R.layout.settings_toggle_switch_child, slotlist);
        this.changeDelivery_slot_availability_status = changeDelivery_slot_availability_status;
        this.mContext=mContext;
        this.slotlist = slotlist;



    }

    @Override
    public int getCount() {
        return super.getCount();
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
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.settings_toggle_switch_child, (ViewGroup) view, false);
        final TextView itemName_widget = listViewItem.findViewById(R.id.child);
        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch menuItemVisibilitySwitch = listViewItem.findViewById(R.id.menuItemAvailabiltySwitch);

        Modal_DeliverySlots modal_manageOrders_pojo_class = slotlist.get(pos);
        String slotStarttime =String.valueOf(modal_manageOrders_pojo_class.getSlotstarttime());
        String slotEndtime =String.valueOf(modal_manageOrders_pojo_class.getSlotendtime());
        String slottimeDuration = slotStarttime+"  -  "+slotEndtime;
        menuItemVisibilitySwitch.setText(String.valueOf(slottimeDuration));
        String  itemAvailability =String.valueOf(modal_manageOrders_pojo_class.getStatus()).toUpperCase();


        if(itemAvailability.equals("ACTIVE")){
            menuItemVisibilitySwitch.setChecked(true);
        }
        if(itemAvailability.equals("INACTIVE")){
            menuItemVisibilitySwitch.setChecked(false);

        }

        menuItemVisibilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
              if(b){
                  String deliverySlotKey = modal_manageOrders_pojo_class.getKey();
                  changeDelivery_slot_availability_status.changeStatusintheDeliverySlot(deliverySlotKey,"ACTIVE");

              }
              else{
                  String deliverySlotKey = modal_manageOrders_pojo_class.getKey();
                  changeDelivery_slot_availability_status.changeStatusintheDeliverySlot(deliverySlotKey,"INACTIVE");

              }
            }
        });

        return  listViewItem ;
    }




}
