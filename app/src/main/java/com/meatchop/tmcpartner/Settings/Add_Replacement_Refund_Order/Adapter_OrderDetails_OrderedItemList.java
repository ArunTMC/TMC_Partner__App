package com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Adapter_OrderDetails_OrderedItemList extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;

    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String itemname_cutname_weight = "";

    Replacement_Refund_OrderDetailsScreen replacement_refund_orderDetailsScreen;

    public Adapter_OrderDetails_OrderedItemList(Context mContext, List<Modal_ManageOrders_Pojo_Class> ordersList, Replacement_Refund_OrderDetailsScreen replacement_refund_orderDetailsScreen) {
        super(mContext, R.layout.replacement_refund_orderdetails_ordered_item_list, ordersList);

        this.mContext=mContext;
        this.ordersList=ordersList;
        this.replacement_refund_orderDetailsScreen = replacement_refund_orderDetailsScreen;

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
        @SuppressLint("ViewHolder") final View listViewItem = LayoutInflater.from(mContext).inflate(R.layout.replacement_refund_orderdetails_ordered_item_list, (ViewGroup) view, false);

        final TextView itemName_widget = listViewItem.findViewById(R.id.itemName_widget);
        final TextView itemWeight_widget = listViewItem.findViewById(R.id.itemWeight_widget);
        final TextView itemQty_widget = listViewItem.findViewById(R.id.itemQty_widget);
        final TextView itemGst_widget = listViewItem.findViewById(R.id.itemGst_widget);
        final TextView itemSubtotal_widget = listViewItem.findViewById(R.id.itemSubtotal_widget);

        final LinearLayout checkbox_linearLayout = listViewItem.findViewById(R.id.checkbox_linearLayout);

        final CheckBox marker_checkBox = listViewItem.findViewById(R.id.marker_checkBox);

    try {
    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(pos);
        boolean isItemMarkedForReplacement = false;
    try {
        isItemMarkedForReplacement = Boolean.valueOf(String.valueOf(modal_manageOrders_pojo_class.getisItemMarkedForReplacement()));
        } catch (Exception e) {
        isItemMarkedForReplacement = false;
            e.printStackTrace();
        }




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
        if(isItemMarkedForReplacement){
            marker_checkBox.setChecked(true);

        }
        else{
            marker_checkBox.setChecked(false);

        }
    }
    catch (Exception e){
        itemname_cutname_weight = "";
        e.printStackTrace();
    }

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

        checkbox_linearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean checkboxState = false;
            checkboxState =marker_checkBox.isChecked();
            try{
                itemname_cutname_weight = String.valueOf(modal_manageOrders_pojo_class.getItemName())+"_"+String.valueOf(modal_manageOrders_pojo_class.getCutname())+"_"+String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight());
            }
            catch (Exception e){
                itemname_cutname_weight = "";
                e.printStackTrace();
            }
            if(marker_checkBox.isChecked()){
                checkboxState = false;
            }
            else{
                checkboxState = true;


            }

            marker_checkBox.setChecked(checkboxState);
             addItemForMarkedListForReplacement(checkboxState,itemname_cutname_weight, modal_manageOrders_pojo_class);
        }
    });



}
catch (Exception e){
    e.printStackTrace();
}
        return listViewItem;

    }

    private void addItemForMarkedListForReplacement(boolean isAdditemtoArray, String itemname_cutname_weight, Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class) {
        boolean isNameAvailable = checkItemNameInStringArray(itemname_cutname_weight);

        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class1 = new Modal_ManageOrders_Pojo_Class();
try {
     String itemDespString = modal_manageOrders_pojo_class.getItemdesp_string();
    try {
        JSONObject json = new JSONObject();
        try {
            json = new JSONObject(itemDespString);
        } catch (Exception e) {
            e.printStackTrace();
        }

            String subCtgyKey = "";
            modal_manageOrders_pojo_class1 = new Modal_ManageOrders_Pojo_Class();
            try {
                if (json.has("tmcsubctgykey")) {
                    modal_manageOrders_pojo_class1.tmcSubCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                } else {
                    modal_manageOrders_pojo_class1.tmcSubCtgyKey = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("grossweight")) {
                    modal_manageOrders_pojo_class1.grossweight = String.valueOf(json.get("grossweight"));
                } else {
                    modal_manageOrders_pojo_class1.grossweight = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("grossweightingrams")) {
                    modal_manageOrders_pojo_class1.grossweightingrams = String.valueOf(json.get("grossweightingrams"));
                } else {
                    modal_manageOrders_pojo_class1.grossweightingrams = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                if (json.has("gstamount")) {
                    modal_manageOrders_pojo_class1.GstAmount = String.valueOf(json.get("gstamount"));
                } else {
                    modal_manageOrders_pojo_class1.GstAmount = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("itemname")) {
                    modal_manageOrders_pojo_class1.itemName = String.valueOf(json.get("itemname"));
                } else {
                    modal_manageOrders_pojo_class1.itemName = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("menuitemid")) {
                    modal_manageOrders_pojo_class1.menuItemKey = String.valueOf(json.get("menuitemid"));
                } else {
                    modal_manageOrders_pojo_class1.menuItemKey = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("netweight")) {
                    modal_manageOrders_pojo_class1.netweight = String.valueOf(json.get("netweight"));
                } else {
                    modal_manageOrders_pojo_class1.netweight = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("portionsize")) {
                    modal_manageOrders_pojo_class1.portionsize = String.valueOf(json.get("portionsize"));
                } else {
                    modal_manageOrders_pojo_class1.portionsize = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("quantity")) {
                    modal_manageOrders_pojo_class1.quantity = String.valueOf(json.get("quantity"));
                } else {
                    modal_manageOrders_pojo_class1.quantity = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (json.has("tmcprice")) {
                    modal_manageOrders_pojo_class1.price = String.valueOf(json.get("tmcprice"));
                } else {
                    modal_manageOrders_pojo_class1.price = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



    } catch (Exception e) {
        e.printStackTrace();
    }
}
catch (Exception e){
    e.printStackTrace();
}
        if(isNameAvailable){
            boolean isObjectAvailable = checkItemNameInHashmap(itemname_cutname_weight);

            if(isObjectAvailable){
                if(isAdditemtoArray){
                    Toast.makeText(mContext, "Item Already Added", Toast.LENGTH_SHORT).show();
                }
                else{
                    replacement_refund_orderDetailsScreen.itemsSelectedForReplacementStringArray.remove(itemname_cutname_weight);
                    replacement_refund_orderDetailsScreen.itemsSelectedForReplacementhashmap.remove(itemname_cutname_weight);
                }
            }
            else{
                if(isAdditemtoArray){
                    replacement_refund_orderDetailsScreen.itemsSelectedForReplacementhashmap.put(itemname_cutname_weight,modal_manageOrders_pojo_class1);
                }
                else{
                    Toast.makeText(mContext, "Item is not Added", Toast.LENGTH_SHORT).show();

                }
            }

        }
        else{
            if(isAdditemtoArray){
                replacement_refund_orderDetailsScreen.itemsSelectedForReplacementStringArray.add(itemname_cutname_weight);
                replacement_refund_orderDetailsScreen.itemsSelectedForReplacementhashmap.put(itemname_cutname_weight,modal_manageOrders_pojo_class1);
            }
            else{
                Toast.makeText(mContext, "Item is not Added", Toast.LENGTH_SHORT).show();

            }
        }




    }

    private boolean checkItemNameInHashmap(String itemname_cutname_weight) {
        if(replacement_refund_orderDetailsScreen.itemsSelectedForReplacementhashmap.containsKey(itemname_cutname_weight)){
            return  true;
        }
        else{
            return  false;
        }
    }

    private boolean checkItemNameInStringArray(String itemname_cutname_weight) {
        if(replacement_refund_orderDetailsScreen.itemsSelectedForReplacementStringArray.contains(itemname_cutname_weight)){
        return  true;
        }
        else{
            return  false;
        }
    }
}

