package com.meatchop.tmcpartner.mobilescreen_javaclasses.replacement_refund_classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_Replacement_Refund_OrderScreen_List extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;

    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String itemname_cutname_weight = "";

    AddReplacement_Refund_OrdersScreen addReplacement_refund_ordersScreen;

    public Adapter_Replacement_Refund_OrderScreen_List(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, AddReplacement_Refund_OrdersScreen addReplacement_refund_ordersScreen) {
        super(mContext, R.layout.replacement_refund_orderdetails_screen_item_list, ordersList);

        this.mContext=mContext;
        this.ordersList=ordersList;
        this.addReplacement_refund_ordersScreen = addReplacement_refund_ordersScreen;

    }


    @Nullable
    @Override
    public Modal_ManageOrders_Pojo_Class getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable Modal_ManageOrders_Pojo_Class item) {
        return super.getPosition(item);
    }

    public View getView(final int pos, View view, ViewGroup v) {
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.replacement_refund_orderdetails_screen_item_list, (ViewGroup) view, false);

        final TextView itemName_widget = listViewItem.findViewById(R.id.itemName_widget);
        final TextView itemWeight_widget = listViewItem.findViewById(R.id.itemWeight_widget);
        final TextView itemQty_widget = listViewItem.findViewById(R.id.itemQty_widget);
        final TextView itemGst_widget = listViewItem.findViewById(R.id.itemGst_widget);
        final TextView itemSubtotal_widget = listViewItem.findViewById(R.id.itemSubtotal_widget);

        final LinearLayout checkbox_linearLayout = listViewItem.findViewById(R.id.checkbox_linearLayout);

        final CheckBox marker_checkBox = listViewItem.findViewById(R.id.marker_checkBox);

        try {
            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(pos);



            String cutname = "", itemName = "",itemName_cutName = "" ,weight = "";
            try {
                itemName = String.valueOf(modal_manageOrders_pojo_class.getItemName());
            } catch (Exception e) {
                itemName = "";
                e.printStackTrace();
            }

            try {
                weight = String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight());
            } catch (Exception e) {
                weight = "";
                e.printStackTrace();
            }


            try {
                cutname = String.valueOf(modal_manageOrders_pojo_class.getCutname());
            } catch (Exception e) {
                cutname = "";
                e.printStackTrace();
            }
            if ((!cutname.equals("null")) && (!cutname.equals("NULL")) && (!cutname.equals("")) && (!cutname.equals(" ")) &&  (!cutname.equals(null))) {
                itemName_cutName = itemName + " [ " + cutname + " ] ";

            } else {
                itemName_cutName = itemName;

            }

            itemName_widget.setText(String.valueOf(itemName_cutName));

            try{
                itemname_cutname_weight = itemName+"_"+cutname+"_"+weight;
            }
            catch (Exception e){
                itemname_cutname_weight = "";
                e.printStackTrace();
            }




            double subtotal = (Double.parseDouble(modal_manageOrders_pojo_class.getItemFinalPrice()));
            double quantity = (Double.parseDouble(modal_manageOrders_pojo_class.getQuantity()));
            subtotal = subtotal * quantity;
            itemSubtotal_widget.setText(String.valueOf(subtotal));
            itemWeight_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight()));
            itemQty_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getQuantity()));
            itemGst_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getGstAmount()));





        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listViewItem;

    }

}

