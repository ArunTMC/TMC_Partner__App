package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_ChangeMenuItem_Price extends ArrayAdapter<Modal_MenuItem_Settings> {
    Context mContext;
    String menuitemKey;
    List<Modal_MenuItem_Settings> menuList;
    MenuItem_List_Settings MenuItem_List_Settings;
    public Adapter_ChangeMenuItem_Price(Context mContext, List<Modal_MenuItem_Settings> menuList, MenuItem_List_Settings MenuItem_List_Settings) {
        super(mContext, R.layout.settings_toggle_switch_child, menuList);
        this.MenuItem_List_Settings = MenuItem_List_Settings;
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
        final LinearLayout MenuitemNameLayout = listViewItem.findViewById(R.id.MenuitemNameLayout);

        Modal_MenuItem_Settings modal_manageOrders_pojo_class = menuList.get(pos);
        itemName_widget.setVisibility(View.VISIBLE);

         itemName_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
        menuItemVisibilitySwitch.setVisibility(View.GONE);
        MenuitemNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuitemKey=String.valueOf(modal_manageOrders_pojo_class.getKey());
                Intent i = new Intent(mContext,ChangeMenuItem_Price_Settings.class);
                i.putExtra("menuItemKey",menuitemKey);
                mContext.startActivity(i);
            }
        });


        return  listViewItem ;
    }

}
