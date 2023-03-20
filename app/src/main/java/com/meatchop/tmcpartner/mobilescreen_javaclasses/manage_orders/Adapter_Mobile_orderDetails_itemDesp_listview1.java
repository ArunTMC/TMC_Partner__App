package com.meatchop.tmcpartner.mobilescreen_javaclasses.manage_orders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import java.util.List;

public class Adapter_Mobile_orderDetails_itemDesp_listview1 extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
        Context mContext;

        List<Modal_ManageOrders_Pojo_Class> ordersList;

public Adapter_Mobile_orderDetails_itemDesp_listview1(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList) {
        super(mContext, R.layout.mobile_screen_manage_orders_orderdetails_listview_item1, ordersList);

        this.mContext=mContext;
        this.ordersList=ordersList;



        }



    @Override
public int getCount() {
        return super.getCount();
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
@SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.mobile_screen_manage_orders_orderdetails_listview_item1, (ViewGroup) view, false);
final TextView itemName_widget = listViewItem.findViewById(R.id.itemName_widget);
final TextView itemWeight_widget = listViewItem.findViewById(R.id.itemWeight_widget);
final TextView itemQty_widget = listViewItem.findViewById(R.id.itemQty_widget);
final TextView itemGst_widget = listViewItem.findViewById(R.id.itemGst_widget);
final TextView itemSubtotal_widget = listViewItem.findViewById(R.id.itemSubtotal_widget);

    final TextView cutName_widget = listViewItem.findViewById(R.id.cutName_widget);

    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(pos);
        itemName_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemName()));
        itemWeight_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight()));
        itemQty_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getQuantity()));
        itemGst_widget.setText(String.valueOf(modal_manageOrders_pojo_class.getGstAmount()));
        String cutname ="";
        try {
            cutname = String.valueOf(modal_manageOrders_pojo_class.getCutname());
        }
        catch (Exception e){
            cutname = "";
            e.printStackTrace();
        }
        if((!cutname.equals("null"))  &&(!cutname.equals("NULL"))  && (!cutname.equals(null))){
            cutName_widget.setText(cutname);

        }
        else{
            cutName_widget.setText("");

        }

    double subtotal = (Double.parseDouble(modal_manageOrders_pojo_class.getItemFinalPrice()));
    double quantity = (Double.parseDouble(modal_manageOrders_pojo_class.getQuantity()));
    subtotal= subtotal*quantity;
    itemSubtotal_widget.setText(String.valueOf(subtotal));
        return  listViewItem ;
        }

        }
