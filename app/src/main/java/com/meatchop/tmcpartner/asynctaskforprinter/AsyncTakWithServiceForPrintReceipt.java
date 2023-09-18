package com.meatchop.tmcpartner.asynctaskforprinter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.settings.Modal_Address;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncTakWithServiceForPrintReceipt extends AsyncTask<Void, Void, Void> {
    Context mContext;
    Modal_forPrintReceipt modal_forPrintReceipt = new Modal_forPrintReceipt();
    AsyncTaskListener_forPrintReceipt mListener;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    List<String> cart_Item_List = new ArrayList<>() ;
    HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap<>();
    BroadcastReceiver myReceiverforPrintingService;
    Modal_Address modal_address;
    public AsyncTakWithServiceForPrintReceipt(Context context, AsyncTaskListener_forPrintReceipt callBackAsyncTaskListener_forPrintReceipt, Modal_forPrintReceipt modal_forPrintReceipt, BroadcastReceiver myReceiverforPrintingService , Modal_Address selected_Address_modal) {
        mContext = context;
        mListener = callBackAsyncTaskListener_forPrintReceipt;
        this.modal_address = selected_Address_modal;
        this.modal_forPrintReceipt = modal_forPrintReceipt;
        this.cart_Item_List = modal_forPrintReceipt.getCart_Item_List();
        this.cartItem_hashmap = modal_forPrintReceipt.getCartItem_hashmap();
        this.myReceiverforPrintingService =myReceiverforPrintingService;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Create and start the service

        Intent serviceIntent = new Intent(mContext, BackgroundServiceForPrintingReceipt.class);
        serviceIntent.putExtra("modalData",modal_forPrintReceipt);
        serviceIntent.putExtra("hashmapdata",cartItem_hashmap);
        serviceIntent.putExtra("selectedAddress",modal_address);



        mContext.startService(serviceIntent);

        return null;
    }




}