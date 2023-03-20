package com.meatchop.tmcpartner.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class Adapter_ConsolidatedSalesReport_listview extends ArrayAdapter<Modal_OrderDetails> {
    Context mContext;

    List<String> ordersList;
    HashMap<String, Modal_OrderDetails> OrderItem_hashmap;

    public Adapter_ConsolidatedSalesReport_listview(Context context, List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {
        super(context, R.layout.consolidated_sales_report_listitem);

        this.mContext = context;
        this.ordersList = order_item_list;
        this.OrderItem_hashmap = orderItem_hashmap;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup view) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.consolidated_sales_report_listitem, (ViewGroup) view, false);
        final TextView name_and_quantity_widget = listViewItem.findViewById(R.id.name_and_quantity);
        final TextView price_widget = listViewItem.findViewById(R.id.price);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
            String menuitemId = ordersList.get(position);
            Modal_OrderDetails modal_orderDetails = OrderItem_hashmap.get(menuitemId);
            String name_quantity = modal_orderDetails.getItemname() + " (" + modal_orderDetails.getQuantity() + ")";
            name_and_quantity_widget.setText(String.valueOf(name_quantity));

            price_widget.setText(String.valueOf(decimalFormat.format(Double.parseDouble(modal_orderDetails.getTmcprice()))));



        return listViewItem;
    }

    @Override
    public int getCount() {
        return OrderItem_hashmap.size();
    }

    @Nullable
    @Override
    public Modal_OrderDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_OrderDetails item) {
        return super.getPosition(item);
    }
}