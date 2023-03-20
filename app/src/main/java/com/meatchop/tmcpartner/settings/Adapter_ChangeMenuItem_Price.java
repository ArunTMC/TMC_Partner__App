package com.meatchop.tmcpartner.settings;

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
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_ChangeMenuItem_Price extends ArrayAdapter<Modal_MenuItem_Settings> {
    Context mContext;
    String menuitemKey,IntentFrom,tmcsubctgykey,itemName;
    List<Modal_MenuItem_Settings> menuList;
    MenuItem_List_Settings MenuItem_List_Settings;
    MenuAvailabilityStatusTransaction menuAvailabilityStatusTransaction;
    public Adapter_ChangeMenuItem_Price(Context mContext, List<Modal_MenuItem_Settings> menuList, MenuItem_List_Settings MenuItem_List_Settings,String IntentFrom) {
        super(mContext, R.layout.settings_toggle_switch_child, menuList);
        this.MenuItem_List_Settings = MenuItem_List_Settings;
        this.mContext=mContext;
        this.menuList = menuList;
        this.IntentFrom=IntentFrom;


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
                try {
                    menuitemKey = String.valueOf(modal_manageOrders_pojo_class.getMenuItemId());
                }
                catch (Exception e){
                    e.printStackTrace();
                    menuitemKey ="";
                    Toast.makeText(mContext, "menu key is empty", Toast.LENGTH_SHORT).show();
                }


                try {
                    tmcsubctgykey = String.valueOf(modal_manageOrders_pojo_class.getTmcsubctgykey());
                }
                catch (Exception e){
                    e.printStackTrace();
                    tmcsubctgykey ="";
                    Toast.makeText(mContext, "subctgy key is empty", Toast.LENGTH_SHORT).show();
                }

                try {
                    itemName = String.valueOf(modal_manageOrders_pojo_class.getItemname());
                }
                catch (Exception e){
                    e.printStackTrace();
                    itemName ="";
                    Toast.makeText(mContext, "itemName is empty", Toast.LENGTH_SHORT).show();
                }



                if(IntentFrom.equals("ChangeMenuItemPrice")) {
                    Intent i = new Intent(mContext,ChangeMenuItem_Price_Settings.class);

                    i.putExtra("menuItemKey",menuitemKey);
                    mContext.startActivity(i);

                }


                if(IntentFrom.equals("ChangeMenuItemPriceAndWeight")) {
                    Intent i = new Intent(mContext, ChangeMenuItemWeightAndPriceSecondScreen.class);

                    i.putExtra("menuItemKey",menuitemKey);
                    mContext.startActivity(i);

                }



                if(IntentFrom.equals("MenuAvailabilityTransactionDetails")) {
                    Intent i = new Intent(mContext,MenuAvailabilityStatusTransaction.class);
                    i.putExtra("subctgykey",tmcsubctgykey);
                    i.putExtra("itemName",itemName);
                    i.putExtra("menuItemKey",menuitemKey);
                    mContext.startActivity(i);
                }

            }
        });


        return  listViewItem ;
    }

}
