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

public class Adapter_ChangeMenutem_Availability_settings extends ArrayAdapter<Modal_MenuItem_Settings> {
    Context mContext;

    List<Modal_MenuItem_Settings> menuList;
    ChangeMenuItemStatus_Settings changeMenuItemStatus_Settings;
    public Adapter_ChangeMenutem_Availability_settings(Context mContext, List<Modal_MenuItem_Settings> menuList, ChangeMenuItemStatus_Settings changeMenuItemStatus_Settings) {
    super(mContext, R.layout.settings_toggle_switch_child, menuList);
    this.changeMenuItemStatus_Settings=changeMenuItemStatus_Settings;
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
        @SuppressLint("UseSwitchCompatOrMaterialCode") final Switch menuItemAvailabiltySwitch = listViewItem.findViewById(R.id.menuItemAvailabiltySwitch);

        Modal_MenuItem_Settings modal_manageOrders_pojo_class = menuList.get(pos);
       // itemName_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
        menuItemAvailabiltySwitch.setText(String.valueOf(modal_manageOrders_pojo_class.getItemname()));
      String  itemAvailability =String.valueOf(modal_manageOrders_pojo_class.getItemavailability()).toUpperCase();
      if(itemAvailability.equals("TRUE")){
          menuItemAvailabiltySwitch.setChecked(true);
      }
      if(itemAvailability.equals("FALSE")){
          menuItemAvailabiltySwitch.setChecked(false);

      }

        menuItemAvailabiltySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    changeMenuItemStatus_Settings. Adjusting_Widgets_Visibility(true);

                    String menuItemkey = String.valueOf(modal_manageOrders_pojo_class.getKey());
                    String itemuniquecode = String.valueOf(modal_manageOrders_pojo_class.getItemuniquecode());
                 /*   for(int i =0; i<changeMenuItemStatus_Settings.marinadeMenuList.size();i++){
                        Modal_MenuItem_Settings modal_menuItemSettings = changeMenuItemStatus_Settings.marinadeMenuList.get(i);
                        String marinadeItem_itemuniquecode = modal_menuItemSettings.getItemuniquecode();
                        String marinadeItemKey = modal_menuItemSettings.getKey();
                        if(marinadeItem_itemuniquecode.equals(itemuniquecode)){

                            changeMenuItemStatus_Settings.ChangeMarinadeMenuitemAvailabilityStatus(marinadeItemKey,"TRUE",itemuniquecode);
                        }
                    }

                  */
                    boolean isMarinadeItem = false;
                    try {
                         isMarinadeItem = modal_manageOrders_pojo_class.getisMarinadeItem();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        isMarinadeItem = false;
                    }
                    if(isMarinadeItem){
                        String marinadeItem_itemuniquecode = modal_manageOrders_pojo_class.getMarinadeItemUniqueCode();
                        String marinadeItemKey = modal_manageOrders_pojo_class.getMarinadeKey();
                        if(marinadeItem_itemuniquecode.equals(itemuniquecode)){

                            changeMenuItemStatus_Settings.ChangeMarinadeMenuitemAvailabilityStatus(marinadeItemKey,"TRUE",itemuniquecode);
                        }

                    }


                    changeMenuItemStatus_Settings.ChangeMenuitemAvailabilityStatus(menuItemkey,"TRUE",itemuniquecode);
                    modal_manageOrders_pojo_class.setItemavailability("TRUE");
                    notifyDataSetChanged();


                } else {
                    changeMenuItemStatus_Settings. Adjusting_Widgets_Visibility(true);

                    String menuItemkey = String.valueOf(modal_manageOrders_pojo_class.getKey());
                    String itemuniquecode = String.valueOf(modal_manageOrders_pojo_class.getItemuniquecode());
                   /* for(int i =0; i<changeMenuItemStatus_Settings.marinadeMenuList.size();i++){
                        Modal_MenuItem_Settings modal_menuItemSettings = changeMenuItemStatus_Settings.marinadeMenuList.get(i);
                        String marinadeItem_itemuniquecode = modal_menuItemSettings.getItemuniquecode();
                        String marinadeItemKey = modal_menuItemSettings.getKey();

                        if(marinadeItem_itemuniquecode.equals(itemuniquecode)){
                            changeMenuItemStatus_Settings.ChangeMarinadeMenuitemAvailabilityStatus(marinadeItemKey,"FALSE",itemuniquecode);
                        }
                        }
                    */

                    boolean isMarinadeItem = false;
                    try {
                        isMarinadeItem = modal_manageOrders_pojo_class.getisMarinadeItem();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        isMarinadeItem = false;
                    }


                    if(isMarinadeItem){
                        String marinadeItem_itemuniquecode = modal_manageOrders_pojo_class.getMarinadeItemUniqueCode();
                        String marinadeItemKey = modal_manageOrders_pojo_class.getMarinadeKey();
                        if(marinadeItem_itemuniquecode.equals(itemuniquecode)){

                            changeMenuItemStatus_Settings.ChangeMarinadeMenuitemAvailabilityStatus(marinadeItemKey,"FALSE",itemuniquecode);
                        }

                    }
                    changeMenuItemStatus_Settings.ChangeMenuitemAvailabilityStatus(menuItemkey,"FALSE", itemuniquecode);

                    modal_manageOrders_pojo_class.setItemavailability("FALSE");
                    notifyDataSetChanged();

            }}
        });
        return  listViewItem ;
    }

}
