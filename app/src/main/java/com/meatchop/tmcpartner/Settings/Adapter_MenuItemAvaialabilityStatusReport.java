package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Adapter_MenuItemAvaialabilityStatusReport extends RecyclerView.Adapter<Adapter_MenuItemAvaialabilityStatusReport.ViewHolder> {

    private Context context;
    MenuItemAvailabilityStatusReport menuItemAvailabilityStatusReport;
    List<Modal_SubCtgyList> subCtgyName_arrayList = new ArrayList<>();
    HashMap<String, Modal_MenuItem_Settings> menuItemHashmap = new HashMap<>();




    public Adapter_MenuItemAvaialabilityStatusReport(List<Modal_SubCtgyList> subCtgyName_arrayListt, HashMap<String, Modal_MenuItem_Settings> menuItemHashmapp,Context contextt, MenuItemAvailabilityStatusReport menuItemAvailabilityStatusReportt) {
        menuItemAvailabilityStatusReport = menuItemAvailabilityStatusReportt;
        menuItemHashmap = menuItemHashmapp;
        subCtgyName_arrayList = subCtgyName_arrayListt;
        context = contextt;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_availability_status_report_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


                    Modal_SubCtgyList modal_subCtgyList = subCtgyName_arrayList.get(position);
                    String subctgykey = modal_subCtgyList.getKey();
                    String subCtgyName = modal_subCtgyList.getSubCtgyName();
                    String total_No_of_MenuItems="0", no_of_menuItems_available="0",no_of_menuitems_Available_in_percentage="0";
        try {
            for (int i2 = 0; i2 < menuItemHashmap.size(); i2++) {
                Modal_MenuItem_Settings menuItem_settings = menuItemHashmap.get(subctgykey);
                String tmcsubCtgykey = Objects.requireNonNull(menuItem_settings).getTmcsubctgykey();
                if (tmcsubCtgykey.equals(subctgykey)) {
                    try {

                        total_No_of_MenuItems = menuItem_settings.getTotalnoofmenuitem();
                        no_of_menuItems_available = menuItem_settings.getNoofmenuitemavailable();
                        no_of_menuitems_Available_in_percentage = menuItem_settings.getNoofmenuitemavailableinpercentage();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e ) {
            e.printStackTrace();
        }




        holder.subCtgyName.setText(subCtgyName+" : ");
       holder.availabilityCount.setText(String.format("%s/%s - %s%%", no_of_menuItems_available,total_No_of_MenuItems,no_of_menuitems_Available_in_percentage));
  //      holder.availabilityinPercentage.setText(String.format("( %s out of %s )", no_of_menuItems_available,total_No_of_MenuItems));

                    }
    @Override
    public int getItemCount() {
        return subCtgyName_arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView subCtgyName;
        private final TextView availabilityCount;
        private final TextView availabilityinPercentage;
        public ViewHolder(View itemView) {
            super(itemView);
            availabilityinPercentage = itemView.findViewById(R.id.availabilityinPercentage);
            availabilityCount = itemView.findViewById(R.id.availabilityCount);
            subCtgyName = itemView.findViewById(R.id.subCtgyName);


        }
    }
}