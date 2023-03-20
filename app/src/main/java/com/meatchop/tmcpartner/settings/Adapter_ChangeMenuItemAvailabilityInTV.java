package com.meatchop.tmcpartner.settings;

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

public class Adapter_ChangeMenuItemAvailabilityInTV extends ArrayAdapter<Modal_MenuItem_Settings> {
    Context mContext;

    List<Modal_MenuItem_Settings> menuList;
    ChangeMenuItem_Availabilty_InTV_Settings changeMenuItem_availabilty_inTVSettings;
    public Adapter_ChangeMenuItemAvailabilityInTV(Context mContext, List<Modal_MenuItem_Settings> menuList, ChangeMenuItem_Availabilty_InTV_Settings changeMenuItem_availabilty_inTVSettings) {
        super(mContext, R.layout.settings_toggle_switch_child, menuList);
        this.changeMenuItem_availabilty_inTVSettings = changeMenuItem_availabilty_inTVSettings;
        this.mContext=mContext;
        this.menuList = menuList;



    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Modal_MenuItem_Settings getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_MenuItem_Settings item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.settings_toggle_switch_child, (ViewGroup) view, false);
        final TextView itemName_widget = listViewItem.findViewById(R.id.child);
        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch menuItemVisibilitySwitch = listViewItem.findViewById(R.id.menuItemAvailabiltySwitch);

        Modal_MenuItem_Settings modal_manageOrders_pojo_class = menuList.get(pos);
        // itemName_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
        menuItemVisibilitySwitch.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
        String  itemAvailability =String.valueOf(modal_manageOrders_pojo_class.getShowinmenuboard()).toUpperCase();
        if(itemAvailability.equals("TRUE")){
            menuItemVisibilitySwitch.setChecked(true);
        }
        if(itemAvailability.equals("FALSE")){
            menuItemVisibilitySwitch.setChecked(false);

        }

        menuItemVisibilitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String menuItemkey = String.valueOf(modal_manageOrders_pojo_class.getKey());
                String itemuniquecode = String.valueOf(modal_manageOrders_pojo_class.getItemuniquecode());
                if(b){


                    changeMenuItem_availabilty_inTVSettings.ChangeMenuitemAvailabilityStatus(menuItemkey,"TRUE",itemuniquecode);
                    modal_manageOrders_pojo_class.setShowinmenuboard("TRUE");
                    notifyDataSetChanged();

                 //   Toast.makeText(mContext,"TRUE",Toast.LENGTH_LONG).show();

                }
                else{


                    changeMenuItem_availabilty_inTVSettings.ChangeMenuitemAvailabilityStatus(menuItemkey,"FALSE",itemuniquecode);
                    modal_manageOrders_pojo_class.setShowinmenuboard("FALSE");
                    notifyDataSetChanged();

                //    Toast.makeText(mContext,"FALSE",Toast.LENGTH_LONG).show();

                }
            }
        });

        return  listViewItem ;
    }

}
