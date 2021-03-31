package com.meatchop.tmcpartner.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.meatchop.tmcpartner.R;

import java.util.ArrayList;

public class Adapter_DeliveryPartnerList_Spinner extends BaseAdapter {
    Context context;
    ArrayList<Modal_DeliveryPartner> deliveryPartner_arrayList;
    LayoutInflater inflter;
DeliveryPartnerSettlementReport deliveryPartnerSettlementReport;
    public Adapter_DeliveryPartnerList_Spinner(Context applicationContext, ArrayList<Modal_DeliveryPartner> deliveryPartner_arrayList,DeliveryPartnerSettlementReport deliveryPartnerSettlementReport) {
        this.context = applicationContext;
        this.deliveryPartner_arrayList = deliveryPartner_arrayList;
        this.deliveryPartnerSettlementReport = deliveryPartnerSettlementReport;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return deliveryPartner_arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.deliverypartner_selecting_spinner, null);
        TextView deliveryPersonName = (TextView) view.findViewById(R.id.deliveryPersonName);

        View divider = (View) view.findViewById(R.id.divider);

        TextView deliveryPersonMobileno = (TextView) view.findViewById(R.id.deliveryPersonMobileno);
        Modal_DeliveryPartner modal_deliveryPartner = deliveryPartner_arrayList.get(i);
        deliveryPersonName.setText(String.valueOf(modal_deliveryPartner.getDeliveryPartnerName()));
        deliveryPersonMobileno.setText(String.valueOf(modal_deliveryPartner.getDeliveryPartnerMobileNo()));





        return view;
    }
}