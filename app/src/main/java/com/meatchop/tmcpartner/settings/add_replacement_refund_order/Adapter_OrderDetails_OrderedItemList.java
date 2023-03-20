package com.meatchop.tmcpartner.settings.add_replacement_refund_order;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Adapter_OrderDetails_OrderedItemList extends ArrayAdapter<Modal_ManageOrders_Pojo_Class> {
    Context mContext;

    List<Modal_ManageOrders_Pojo_Class> ordersList;
    String itemname_cutname_weight = "";
    List<String> quantityCountArray = new ArrayList<>();

    Replacement_Refund_OrderDetailsScreen replacement_refund_orderDetailsScreen;
     CheckBox marker_checkBox ;
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
        final LinearLayout selecteditemParentLayout = listViewItem.findViewById(R.id.selecteditemParentLayout);
        final TextView selectedItemPriceTextWidget = listViewItem.findViewById(R.id.selectedItemPriceTextWidget);
        final TextView selectedItemQuantityTextWidget = listViewItem.findViewById(R.id.selectedItemQuantityTextWidget);

        final LinearLayout checkbox_linearLayout = listViewItem.findViewById(R.id.checkbox_linearLayout);

          marker_checkBox = listViewItem.findViewById(R.id.marker_checkBox);

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


        try{
            if(isItemMarkedForReplacement){
                marker_checkBox.setChecked(true);
                if(quantity>1){
                    double selected_quantity = (Double.parseDouble(modal_manageOrders_pojo_class.getSelectedQuantity()));

                    if(selected_quantity != quantity){
                        double selected_subtotal = (Double.parseDouble(modal_manageOrders_pojo_class.getItemFinalPrice()));

                        selecteditemParentLayout.setVisibility(View.VISIBLE);
                        selectedItemQuantityTextWidget.setText(String.valueOf(modal_manageOrders_pojo_class.getSelectedQuantity()));
                        selected_subtotal = selected_subtotal * selected_quantity;
                        selectedItemPriceTextWidget.setText(String.valueOf(selected_subtotal));
                    }
                    else{
                        selecteditemParentLayout.setVisibility(View.GONE);

                    }


                }
                else{
                    selecteditemParentLayout.setVisibility(View.GONE);

                }

            }
            else{
                marker_checkBox.setChecked(false);

            }
        }
        catch (Exception e){
            itemname_cutname_weight = "";
            e.printStackTrace();
        }


        checkbox_linearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (!Replacement_Refund_OrderDetailsScreen.isAlreadyMarkedForReplacement) {


                boolean checkboxState = false;
                // checkboxState =marker_checkBox.isChecked();
                checkboxState = Boolean.valueOf(String.valueOf(ordersList.get(pos).getisItemMarkedForReplacement()));

                //checkboxState = ordersList.get(pos).getisItemMarkedForReplacement();
                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(pos);

                try {
                    itemname_cutname_weight = String.valueOf(modal_manageOrders_pojo_class.getItemName()) + "_" + String.valueOf(modal_manageOrders_pojo_class.getCutname()) + "_" + String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight());
                } catch (Exception e) {
                    itemname_cutname_weight = "";
                    e.printStackTrace();
                }
                if (checkboxState) {
                    checkboxState = false;
                } else {
                    checkboxState = true;


                }
                if (checkboxState) {
                    String Qty_SelectedItem = "0";
                    Qty_SelectedItem = String.valueOf(modal_manageOrders_pojo_class.getQuantity());
                    Qty_SelectedItem = Qty_SelectedItem.replaceAll("[^\\d.]", "");
                    int Qty_SelectedItem_Int = 0;
                    try {
                        Qty_SelectedItem_Int = Integer.parseInt(Qty_SelectedItem);
                    } catch (Exception e) {
                        Qty_SelectedItem_Int = 0;
                        e.printStackTrace();
                    }


                    if (Qty_SelectedItem_Int > 1) {
                        OpenDialogToChooseQtyWise(pos, modal_manageOrders_pojo_class, checkboxState, itemname_cutname_weight);
                    } else {
                        ordersList.get(pos).setItemMarkedForReplacement(checkboxState);
                        ordersList.get(pos).setSelectedQuantity("1");
                        notifyDataSetChanged();

                        //marker_checkBox.setChecked(checkboxState);
                        addItemForMarkedListForReplacement(checkboxState, itemname_cutname_weight, modal_manageOrders_pojo_class);

                    }
                } else {
                    ordersList.get(pos).setItemMarkedForReplacement(checkboxState);
                    ordersList.get(pos).setSelectedQuantity("1");


                    //  marker_checkBox.setChecked(checkboxState);
                    boolean isObjectAvailable = checkItemNameInHashmap(itemname_cutname_weight);
                    if (isObjectAvailable) {
                        replacement_refund_orderDetailsScreen.itemsSelectedForReplacementStringArray.remove(itemname_cutname_weight);
                        replacement_refund_orderDetailsScreen.itemsSelectedForReplacementhashmap.remove(itemname_cutname_weight);
                    }
                    notifyDataSetChanged();
                }
            }
            else{
                Toast.makeText(mContext, "Already Marked For Replacement", Toast.LENGTH_SHORT).show();
            }
        }
    });



}
catch (Exception e){
    e.printStackTrace();
}
        return listViewItem;

    }

    private void OpenDialogToChooseQtyWise(int pos, Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class, boolean checkboxState, String itemname_cutname_weight) {
        quantityCountArray.clear();
        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.choose_qtywise_replacementorder_layout);
        TextView selectedItemName = dialog.findViewById(R.id.selectedItemName);
        TextView orderedQuantityTextWidget = dialog.findViewById(R.id.orderedQuantityTextWidget);
        TextView orderedItemPriceTextWidget = dialog.findViewById(R.id.orderedItemPriceTextWidget);
        TextView selectedItemPriceTextWidget = dialog.findViewById(R.id.selectedItemPriceTextWidget);
        Spinner quantitySelectingSpinner = dialog.findViewById(R.id.quantitySelectingSpinner);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        TextView saveButton = dialog.findViewById(R.id.saveButton);

        selectedItemName.setText(String.valueOf(modal_manageOrders_pojo_class.getItemName()));
        orderedQuantityTextWidget.setText(String.valueOf(modal_manageOrders_pojo_class.getQuantity()));
        final double[] subtotal = {(Double.parseDouble(modal_manageOrders_pojo_class.getItemFinalPrice()))};
        double quantity = (Double.parseDouble(modal_manageOrders_pojo_class.getQuantity()));
        subtotal[0] = subtotal[0] * quantity;
        orderedItemPriceTextWidget.setText("Rs. "+String.valueOf(subtotal[0]));
        for (int i= 1; i <= quantity; i++){
            quantityCountArray.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, quantityCountArray );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySelectingSpinner.setAdapter(arrayAdapter);
        final String[] selectedQuantityString = {""};
        quantitySelectingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 selectedQuantityString[0] = quantitySelectingSpinner.getSelectedItem().toString();
                double quantity = (Double.parseDouble(selectedQuantityString[0]));
                double subtotal = (Double.parseDouble(modal_manageOrders_pojo_class.getItemFinalPrice()));

                double subtotal_selectedQuantity = 0;
                subtotal_selectedQuantity = subtotal * quantity;
                selectedItemPriceTextWidget.setText("Rs. "+String.valueOf(subtotal_selectedQuantity));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                ordersList.get(pos).setItemMarkedForReplacement(false);
                ordersList.get(pos).setSelectedQuantity("1");
                boolean isObjectAvailable = checkItemNameInHashmap(itemname_cutname_weight);
                if(isObjectAvailable) {

                    replacement_refund_orderDetailsScreen.itemsSelectedForReplacementStringArray.remove(itemname_cutname_weight);
                    replacement_refund_orderDetailsScreen.itemsSelectedForReplacementhashmap.remove(itemname_cutname_weight);
                }
                notifyDataSetChanged();

            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersList.get(pos).setItemMarkedForReplacement(checkboxState);
                ordersList.get(pos).setSelectedQuantity(selectedQuantityString[0]);
                notifyDataSetChanged();
                
                modal_manageOrders_pojo_class.setItemMarkedForReplacement(checkboxState);
                modal_manageOrders_pojo_class.setSelectedQuantity(selectedQuantityString[0]);

                addItemForMarkedListForReplacement(checkboxState, itemname_cutname_weight, modal_manageOrders_pojo_class);


                dialog.cancel();
            }
        });

        dialog.show();


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
            double  quantity_Double =0;
            modal_manageOrders_pojo_class1 = new Modal_ManageOrders_Pojo_Class();

            try{

                    if (json.has("quantity")) {
                        modal_manageOrders_pojo_class1.quantity = String.valueOf(json.get("quantity"));
                        try{

                            quantity_Double = Double.parseDouble(String.valueOf(json.get("quantity"))) ;

                        }
                        catch (Exception e){
                            quantity_Double =1;
                            e.printStackTrace();
                        }
                    } else {
                        quantity_Double =1;
                        modal_manageOrders_pojo_class1.quantity = "1";
                    }
                } catch (Exception e) {
                    quantity_Double =1;
                    modal_manageOrders_pojo_class1.quantity = "1";
                    e.printStackTrace();
                }

            if(quantity_Double>1){
                try{
                    modal_manageOrders_pojo_class1.quantity = String.valueOf(modal_manageOrders_pojo_class.getSelectedQuantity());

                }
                catch (Exception e){
                    modal_manageOrders_pojo_class1.quantity = "1";
                    e.printStackTrace();
                }

            }

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
                if (json.has("tmcprice")) {
                    modal_manageOrders_pojo_class1.price = String.valueOf(json.get("tmcprice"));
                } else {
                    modal_manageOrders_pojo_class1.price = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        try {
            if (json.has("cutname")) {
                modal_manageOrders_pojo_class1.cutname = String.valueOf(json.get("cutname"));
            } else {
                modal_manageOrders_pojo_class1.cutname = "";
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

