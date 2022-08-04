package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.meatchop.tmcpartner.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_SupplierVehicleName extends BaseAdapter {

    List<String> supplierVehicleNameArray = new ArrayList<>();
    Context mContext;
    SupplierVehicleTime_AddRecord supplierVehicleTime_addRecord;


    public Adapter_SupplierVehicleName(Context context, List<String> supplierVehicleNameArray, SupplierVehicleTime_AddRecord supplierVehicleTime_addRecord1) {
    this.mContext  = context;
    this.supplierVehicleNameArray = supplierVehicleNameArray;
    this.supplierVehicleTime_addRecord = supplierVehicleTime_addRecord1;

    }


    @Override
    public int getCount() {
        return supplierVehicleNameArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.suppliervehicle_addrecord_list_item, (ViewGroup) convertView, false);


        return convertView;
    }
}
