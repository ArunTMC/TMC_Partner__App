package com.meatchop.tmcpartner.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.R;

import java.util.ArrayList;

public class Adapter_DeliverySlotTransactionDetails  extends ArrayAdapter<Modal_DeliverySlotTransactions> {
    ArrayList<Modal_DeliverySlotTransactions> deliverySlotTransactionsArrayList = new ArrayList<>();
    Context mContext;


    public Adapter_DeliverySlotTransactionDetails(Context mContext, ArrayList<Modal_DeliverySlotTransactions> deliverySlotTransactionsArrayListt) {
        super(mContext, R.layout.activity_deliveryslotstransactionlistitem, deliverySlotTransactionsArrayListt);

        this.mContext = mContext;
        this.deliverySlotTransactionsArrayList = deliverySlotTransactionsArrayListt;
    }

    @Nullable
    @Override
    public Modal_DeliverySlotTransactions getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_DeliverySlotTransactions item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.activity_deliveryslotstransactionlistitem, (ViewGroup) view, false);
        try {
            TextView mobilenumber_textview = listViewItem.findViewById(R.id.mobilenumber_textview);
            TextView status_textview = listViewItem.findViewById(R.id.status_textview);
            TextView time_textview = listViewItem.findViewById(R.id.time_textview);
            Modal_DeliverySlotTransactions modal_deliverySlotTransactions = deliverySlotTransactionsArrayList.get(pos);

            String status = "";
            status = String.valueOf(modal_deliverySlotTransactions.getSlotstatus());
            try{
                if(status.equals("ACTIVE")){
                    status = "ON";
                }
                else if(status.equals("INACTIVE")){
                    status ="OFF";
                }
                else{
                    status = status;
                }
            }
            catch (Exception e){
                e.printStackTrace();
                status = status;

            }



            status_textview.setText(String.valueOf("TURNED "+status));
            mobilenumber_textview.setText(String.valueOf(modal_deliverySlotTransactions.getMobileno()));
            time_textview.setText(String.valueOf(modal_deliverySlotTransactions.getTransactiontime()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return listViewItem;
    }



}
