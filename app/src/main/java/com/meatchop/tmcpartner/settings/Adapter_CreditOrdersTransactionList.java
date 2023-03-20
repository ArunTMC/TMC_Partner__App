package com.meatchop.tmcpartner.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_CreditOrdersTransactionList extends ArrayAdapter<Modal_CreditOrdersTransactionDetails> {
    Context mContext;
    List<Modal_CreditOrdersTransactionDetails> transactionDetailsList;
    CreditOrders_MobileNumberwiseTransactionScreen creditOrdersMobileNumberwiseTransactionScreen;
    public Adapter_CreditOrdersTransactionList(Context mContext, List<Modal_CreditOrdersTransactionDetails> transactionDetailsList, CreditOrders_MobileNumberwiseTransactionScreen creditOrdersMobileNumberwiseTransactionScreen) {
        super(mContext, R.layout.creditorders_transaction_list_item, transactionDetailsList);
        this.mContext=mContext;
        this.creditOrdersMobileNumberwiseTransactionScreen = creditOrdersMobileNumberwiseTransactionScreen;
        this.transactionDetailsList = transactionDetailsList;

    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Modal_CreditOrdersTransactionDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_CreditOrdersTransactionDetails item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.creditorders_transaction_list_item, (ViewGroup) view, false);
        final TextView transactionType_textWidget = listViewItem.findViewById(R.id.transactionType_textWidget);
        final TextView transactionTime_textWidget = listViewItem.findViewById(R.id.transactionTime_textWidget);
        final TextView oldCreditValue_textWidget = listViewItem.findViewById(R.id.oldCreditValue_textWidget);
        final TextView transactionValue_label = listViewItem.findViewById(R.id.transactionValue_label);
        final TextView transactionValue_textWidget = listViewItem.findViewById(R.id.transactionValue_textWidget);
        final TextView newCreditValue_textWidget = listViewItem.findViewById(R.id.newCreditValue_textWidget);

        final LinearLayout orderid_layout = listViewItem.findViewById(R.id.orderid_layout);

        final TextView orderid_textWidget = listViewItem.findViewById(R.id.orderid_textWidget);

        Modal_CreditOrdersTransactionDetails modal_creditOrdersTransactionDetails = transactionDetailsList.get(pos);

        String transactionTypeString = (modal_creditOrdersTransactionDetails.getTransactiontype().toString());
     try {
         if (transactionTypeString.toUpperCase().equals(Constants.CREDIT_AMOUNT_PAID)) {
             transactionType_textWidget.setText("AMOUNT PAID");
             transactionValue_label.setText("Amount Paid  ");
             orderid_layout.setVisibility(View.GONE);

         } else if (transactionTypeString.toUpperCase().equals(Constants.CREDIT_AMOUNT_ADDED)) {
             transactionType_textWidget.setText("CREDIT ORDER PLACED");
             transactionValue_label.setText("Order Placed For ");
             orderid_layout.setVisibility(View.VISIBLE);

         } else {
             transactionType_textWidget.setText(transactionTypeString);

         }
         transactionTime_textWidget.setText(modal_creditOrdersTransactionDetails.getTransactiontime().toString());
         oldCreditValue_textWidget.setText(modal_creditOrdersTransactionDetails.getOldamountincredit().toString()+" Rs");
         transactionValue_textWidget.setText(modal_creditOrdersTransactionDetails.getTransactionvalue().toString()+" Rs");
         newCreditValue_textWidget.setText(modal_creditOrdersTransactionDetails.getNewamountincredit().toString()+" Rs");
         orderid_textWidget .setText(modal_creditOrdersTransactionDetails.getOrderid().toString());
     }
     catch (Exception e){
         e.printStackTrace();
     }
        return  listViewItem ;
    }

}
