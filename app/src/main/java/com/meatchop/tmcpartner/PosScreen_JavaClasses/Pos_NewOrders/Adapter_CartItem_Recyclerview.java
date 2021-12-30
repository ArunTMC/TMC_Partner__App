package com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

import static com.meatchop.tmcpartner.Constants.TAG;

public class Adapter_CartItem_Recyclerview extends RecyclerView.Adapter<Adapter_CartItem_Recyclerview.ViewHolder> {

    private Context context;
    private String pricetype_of_pos;
    Adapter_AutoCompleteMenuItem adapter;
    private double item_total = 0;
    String Menulist;
    private Handler handler;
    int price_per_kg, taxes_and_charges;
    NewOrders_MenuItem_Fragment newOrders_menuItem_fragment;
    public static HashMap<String,Modal_NewOrderItems> itemInCart = new HashMap();

    public Adapter_CartItem_Recyclerview(Context context, HashMap<String, Modal_NewOrderItems> itemInCart, String menuItems, NewOrders_MenuItem_Fragment newOrders_menuItem_fragment) {

        //Log.e(TAG, "Auto call adapter itemInCart itemInCart" + itemInCart.size());
        this.newOrders_menuItem_fragment = newOrders_menuItem_fragment;
        this.context = context;

        this.itemInCart = itemInCart;

        this.Menulist = menuItems;
    }

    @NotNull
    @Override
    public Adapter_CartItem_Recyclerview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.neworders_cart_tem_listview, parent, false);

        return new Adapter_CartItem_Recyclerview.ViewHolder(view);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    private void sendHandlerMessage(String bundlestr) {
        //Log.e(Constants.TAG, "createBillDetails in cartaItem 1");

        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("CartItem", bundlestr);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        //Log.e(TAG, "onBindViewHolder: called.");
        //Log.e("TAG", "adapter       " + newOrders_menuItem_fragment.cart_Item_List.size());
        //Log.e("TAG", "adapter       ");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");


        holder.itemIndex.setText(String.valueOf(position + 1));

         if (position == (itemInCart.size() - 1)) {
            holder.addNewItem_layout.setVisibility(View.VISIBLE);
            holder.barcode_widget.setFocusable(true);
             holder.barcode_widget.requestFocus();

         } else {
            holder.addNewItem_layout.setVisibility(View.GONE);

        }
        int length = holder.autoComplete_widget.getText().length();
        holder.autoComplete_widget.setSelection(length);
        //Log.e("TAG", "position" + position);
                    Modal_NewOrderItems recylerviewPojoClass = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(NewOrders_MenuItem_Fragment.cart_Item_List.get(position));


                    //Log.i("TAG", "HASHMAP" + recylerviewPojoClass.getItemuniquecode());

                    if (recylerviewPojoClass.getItemuniquecode().equals("empty")) {
                        //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getItemname());
                        //Log.e("TAG", "adapter 1 " + recylerviewPojoClass.getGrossweight());
                        //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getPricePerItem());
                        //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getTmcpriceperkg());

                        holder.itemPrice_Widget.setText("");
                        holder.itemWeight_widget.setText("");
                        holder.itemQuantity_widget.setText("");
                        holder.barcode_widget.setText("");

                        holder.autoComplete_widget.setText("");


                    } else {



                        pricetype_of_pos = String.valueOf(recylerviewPojoClass.getPricetypeforpos());


                        if (pricetype_of_pos.equals("tmcprice")) {

                            holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                            if(!holder.autoComplete_widget.getText().toString().equals("")&&holder.autoComplete_widget.getText().length()>3){
                                holder.autoComplete_widget.setKeyListener(null);

                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                holder.barcode_widget.setFocusedByDefault(false);
                            }
                            holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                            if(!holder.barcode_widget.getText().toString().equals("")&&holder.barcode_widget.getText().length()>3){
                                 holder.barcode_widget.setKeyListener(null);

                            }




                            holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));
                            holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                            taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                            newOrders_menuItem_fragment.add_amount_ForBillDetails();
                           try {
                               if(recylerviewPojoClass.getGrossweight().equals("")){
                                   if (recylerviewPojoClass.getNetweight().equals("")) {

                                       holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));

                                   }
                                   else{
                                       holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                                   }
                               }
                               else{
                                   holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));

                               }


                               /*
                               if (recylerviewPojoClass.getPortionsize().equals("")) {
                                   if (recylerviewPojoClass.getNetweight().equals("")) {
                                       holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));

                                   } else {
                                       holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                                   }

                               } else {
                                   holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));
                               }

                                */
                           }
                           catch (Exception e){
                               //Log.e("TAG", "Cant set itemweight/portionsize in recycler adapter");
                                e.printStackTrace();
                           }

                        } else if (pricetype_of_pos.equals("tmcpriceperkg")) {
                            holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                            holder.barcode_widget.setKeyListener(null);

                            holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                            holder.autoComplete_widget.setKeyListener(null);
                            holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));

                            taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                            holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalWeight()));
                            holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                         newOrders_menuItem_fragment.add_amount_ForBillDetails();
                        }
                        //Log.e(TAG, "Got barcode isBarcodeEn position " + position);
                        //Log.e(TAG, "Got barcode newOrders_menuItem_fragment.cart_Item_List.size() " + newOrders_menuItem_fragment.cart_Item_List.size());


                    }
        

                    holder.addNewItem_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!holder.autoComplete_widget.getText().toString().equals("") && (!holder.itemWeight_widget.getText().toString().equals("") || !holder.itemQuantity_widget.getText().toString().equals(""))) {



                                newOrders_menuItem_fragment.createEmptyRowInListView("empty");

                                newOrders_menuItem_fragment.CallAdapter();

                                if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                    newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                                }
                                if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                    newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                    newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                                    newOrders_menuItem_fragment.discountAmount="0";
                                }

                            } else {
                                Toast.makeText(context, "You have to fill this Item First", Toast.LENGTH_LONG).show();
                            }


                        }
                    });


                    holder.removeItem_fromCart_widget.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Log.e(TAG, "Item" + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));
                            String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                            if(barcode.equals(""))
                            {
                                barcode="empty";
                            }
                            //Log.i("TAG", "KEY: " + barcode);
                            if ((NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1) == 0) {

                                //Log.i("TAG", "KEY: " + barcode);

                                NewOrders_MenuItem_Fragment.cartItem_hashmap.remove(barcode);
                                NewOrders_MenuItem_Fragment.cart_Item_List.remove(barcode);
                                newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                newOrders_menuItem_fragment.createEmptyRowInListView("empty");
;                                newOrders_menuItem_fragment.discountAmount ="0";
                                newOrders_menuItem_fragment.discount_Edit_widget .setText("0");
                                newOrders_menuItem_fragment.discount_rs_text_widget .setText("0");
                                newOrders_menuItem_fragment.finaltoPayAmount = "0";

                                newOrders_menuItem_fragment.CallAdapter();

                                //Log.e(TAG, "Item_not_deleted  " + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));

                            } else {
                                //Log.i("TAG", "KEY: " + barcode);


                                NewOrders_MenuItem_Fragment.cartItem_hashmap.remove(barcode);
                                NewOrders_MenuItem_Fragment.cart_Item_List.remove(barcode);

                                newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                newOrders_menuItem_fragment.CallAdapter();

                              //  NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();
                                //Log.e(TAG, "Item_deleted  " + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));


                            }
                            if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                            }
                            if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                                newOrders_menuItem_fragment.discountAmount="0";
                            }


                        }
                    });

                    //Log.e(TAG, "Auto menu in cart adapter 1 : " + Menulist);


                    holder.tmcUnitprice_weightAdd_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(! holder.itemQuantity_widget.getText().toString().equals("")&& !holder.itemPrice_Widget.getText().toString().equals(""))
                            {

                                String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                                Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                                int quantity = Integer.parseInt(holder.itemQuantity_widget.getText().toString());

                                quantity = quantity + 1;
                                holder.itemQuantity_widget.setText(String.valueOf(quantity));
                                double item_price = Double.parseDouble(Objects.requireNonNull(modal_newOrderItems).getItemPrice_quantityBased());
                                item_price = item_price * quantity;

                                holder.itemPrice_Widget.setText(decimalFormat.format(item_price));
                                modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));


                                modal_newOrderItems.setQuantity(String.valueOf(quantity));
                                newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                    newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                                }
                                if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                    newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                    newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                                    newOrders_menuItem_fragment.discountAmount="0";
                                }

                            }
                            else{
                                Toast.makeText(context,"First u have to Select an Item",Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    holder.tmcUnitprice_weightMinus_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(! holder.itemQuantity_widget.getText().toString().equals("")&& !holder.itemPrice_Widget.getText().toString().equals(""))
                            {

                                String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                            Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);


                                int quantity = Integer.parseInt(holder.itemQuantity_widget.getText().toString());
                            if (quantity > 1) {
                                quantity = quantity - 1;
                                holder.itemQuantity_widget.setText(String.valueOf(quantity));
                                double item_price = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
                                item_price = item_price * quantity;
                                modal_newOrderItems.setQuantity(String.valueOf(quantity));
                                holder.itemPrice_Widget.setText(decimalFormat.format(item_price));
                                modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));

                                newOrders_menuItem_fragment.add_amount_ForBillDetails();

                                if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                    newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                                }
                                if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                    newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                    newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                                    newOrders_menuItem_fragment.discountAmount="0";
                                }

                            } else {
                                Toast.makeText(context, "To Remove the Item Click the Delete Icon", Toast.LENGTH_LONG).show();
                            }

                            }
                            else{
                                Toast.makeText(context,"First u have to Select an Item",Toast.LENGTH_LONG).show();
                            }
                        }

                    });



    }


    @Override
    public int getItemCount() {
        return itemInCart.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AutoCompleteTextView autoComplete_widget;

        LinearLayout tmcUnitprice_weightAdd_layout, tmcUnitprice_weightMinus_layout;


        TextView itemIndex,itemWeight_widget, itemQuantity_widget;

        TextView itemPrice_Widget;

        EditText  barcode_widget;

        ImageView minus_to_remove_item_widget;
        LinearLayout removeItem_fromCart_widget, addNewItem_layout,parentLayout;
        boolean isTMCproduct = false;
        boolean isIndiaGateBasmatiRiceproduct = false;

        private EditTextListener EditTextListener = new EditTextListener();

        public ViewHolder(View itemView) {
            super(itemView);
            this.autoComplete_widget = itemView.findViewById(R.id.autoComplete_widget);
            this.itemIndex = itemView.findViewById(R.id.item_NO);
            this.barcode_widget = itemView.findViewById(R.id.barcode_widget);

            this.tmcUnitprice_weightAdd_layout = itemView.findViewById(R.id.tmcUnitprice_weightAdd_layout);
            this.tmcUnitprice_weightMinus_layout = itemView.findViewById(R.id.tmcUnitprice_weightMinus_layout);
            this.itemWeight_widget = itemView.findViewById(R.id.itemWeight_widget);
            this.itemQuantity_widget = itemView.findViewById(R.id.itemQuantity_widget);



            this.itemPrice_Widget = itemView.findViewById(R.id.itemPrice_Widget);

            this.minus_to_remove_item_widget = itemView.findViewById(R.id.minus_to_remove_item_widget);

            this.removeItem_fromCart_widget = itemView.findViewById(R.id.removeItem_fromCart_widget);
            this.addNewItem_layout = itemView.findViewById(R.id.addNewItem_layout);

            this.parentLayout = itemView.findViewById(R.id.parentLayout);



             adapter = new Adapter_AutoCompleteMenuItem(context, Menulist,getPosition());
             adapter.setHandler(newHandler());


             autoComplete_widget.setAdapter(adapter);
            autoComplete_widget.clearFocus();


            barcode_widget.addTextChangedListener(EditTextListener);


        }

        private class EditTextListener implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String s1 = (s.toString());
                if(s1.length()==4){
                    if(s1.equals("9990")){
                        isTMCproduct=true;
                    }
                    else {
                        isTMCproduct=false;

                    }
                }
             /*   if(s1.length()==10){
                    if(s1.equals("6902251011")) {

                        isIndiaGateBasmatiRiceproduct=true;
                    }
                    else{
                        isIndiaGateBasmatiRiceproduct=false;

                    }
                    }

              */
                //Log.e(TAG, "Got barcode isBarcodeEntered in on textchangeddddd"+s1);


                if (s1.length() > 4) {


                        if (isTMCproduct) {
                            if (barcode_widget.getText().toString().length() == 14) {

                                //Log.e(TAG, "Got barcode " + barcode_widget.getText().length());

                                String Barcode = barcode_widget.getText().toString();
                                getMenuItemUsingBarCode(Barcode);
                            }
                        }
                       /* else if(isIndiaGateBasmatiRiceproduct){
                            if (barcode_widget.getText().toString().length() == 12) {

                                //Log.e(TAG, "Got barcode " + barcode_widget.getText().length());

                                String Barcode = barcode_widget.getText().toString();

                                getMenuItemUsingBarCode(Barcode);
                            }
                        }

                        */

                        else {
                            //if (barcode_widget.getText().toString().length() == 13) {

                                //Log.e(TAG, "Got barcode " + barcode_widget.getText().length());

                                String Barcode = barcode_widget.getText().toString();

                                getMenuItemUsingBarCode(Barcode);
                           // }
                        }


                }


            }
        }


        public void getMenuItemUsingBarCode(String barcode) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            //Log.e(TAG, "barcode  1    " + barcode);

            if(!isTMCproduct){

                //Log.e(TAG, " barcode  1   " + barcode);

                for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {
                    String itemWeight="";
                    //Log.e(TAG, " barcode  1  for" + barcode);

                    Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                    if ((String.valueOf(modal_newOrderItems.getBarcode())).equals(barcode)) {

                        try {
                            Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                            newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                            newItem_newOrdersPojoClass.grossweight = modal_newOrderItems.getGrossweight();
                            newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                            newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                            newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                            newItem_newOrdersPojoClass.menuItemId = (modal_newOrderItems.getMenuItemId());

                            newItem_newOrdersPojoClass.barcode = (modal_newOrderItems.getBarcode());
                            newItem_newOrdersPojoClass.key = modal_newOrderItems.getKey();
                            newItem_newOrdersPojoClass.itemavailability = (modal_newOrderItems.getItemavailability());

                            newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                            newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                            newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                            newItem_newOrdersPojoClass.itemuniquecode = modal_newOrderItems.getItemuniquecode();
                            newItem_newOrdersPojoClass.keyforHashmap = barcode;

                            newItem_newOrdersPojoClass.barcode_AvlDetails =String.valueOf(modal_newOrderItems.getBarcode_AvlDetails());
                            newItem_newOrdersPojoClass.itemavailability_AvlDetails =String.valueOf(modal_newOrderItems.getItemavailability_AvlDetails());
                            newItem_newOrdersPojoClass.key_AvlDetails =String.valueOf(modal_newOrderItems.getKey_AvlDetails());
                            newItem_newOrdersPojoClass.lastupdatedtime_AvlDetails =String.valueOf(modal_newOrderItems.getLastupdatedtime_AvlDetails());
                            newItem_newOrdersPojoClass.menuitemkey_AvlDetails =String.valueOf(modal_newOrderItems.getMenuitemkey_AvlDetails());
                            newItem_newOrdersPojoClass.receivedstock_AvlDetails =String.valueOf(modal_newOrderItems.getReceivedstock_AvlDetails());
                            newItem_newOrdersPojoClass.allownegativestock =String.valueOf(modal_newOrderItems.getAllownegativestock());

                            newItem_newOrdersPojoClass.stockbalance_AvlDetails =String.valueOf(modal_newOrderItems.getStockbalance_AvlDetails());
                            newItem_newOrdersPojoClass.stockincomingkey_AvlDetails =String.valueOf(modal_newOrderItems.getStockincomingkey_AvlDetails());
                            newItem_newOrdersPojoClass.vendorkey_AvlDetails =String.valueOf(modal_newOrderItems.getVendorkey_AvlDetails());

                            newItem_newOrdersPojoClass.barcode =String.valueOf(modal_newOrderItems.getBarcode());
                            newItem_newOrdersPojoClass.tmcctgykey =String.valueOf(modal_newOrderItems.getTmcctgykey());



                            newItem_newOrdersPojoClass.discountpercentage = (String.valueOf(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getDiscountpercentage()))));
                            try {
                                if (modal_newOrderItems.getTmcsubctgykey().equals("")) {
                                    newItem_newOrdersPojoClass.tmcsubctgykey="0";

                                } else {
                                    newItem_newOrdersPojoClass.tmcsubctgykey=(String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((modal_newOrderItems.getTmcprice()))));
                            newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf(((modal_newOrderItems.getTmcprice()))));
                            newItem_newOrdersPojoClass.quantity = "1";
                            newItem_newOrdersPojoClass.subTotal_perItem = "";
                            newItem_newOrdersPojoClass.total_of_subTotal_perItem = "";
                            newItem_newOrdersPojoClass.totalGstAmount = "";

                            try{
                                newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            try{
                                newItem_newOrdersPojoClass.grossweight = (modal_newOrderItems.getGrossweight());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            try{
                                newItem_newOrdersPojoClass.netweight = (modal_newOrderItems.getNetweight());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            try{
                                newItem_newOrdersPojoClass.portionsize = (modal_newOrderItems.getPortionsize());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }


                            newItem_newOrdersPojoClass.itemweightdetails = (String.valueOf(((modal_newOrderItems.getItemFinalWeight()))));
                            newItem_newOrdersPojoClass.itemcutdetails = (String.valueOf(((modal_newOrderItems.getItemcutdetails()))));
                            newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(((modal_newOrderItems.getInventorydetails()))));






                            addItemIntheCart(barcode, newItem_newOrdersPojoClass, itemWeight, modal_newOrderItems.getItemuniquecode());

                        }
                        catch (Exception e ){
                            Toast.makeText(context,"Error in get MenuItem using Barcode ",Toast.LENGTH_LONG).show();

                        }
                    }


                }}
            else{
                //Log.e(TAG, " barcode  2" + barcode);

            if (barcode.length() == 14) {
                //Log.e(TAG, " barcode  3" + barcode);
                try{
                String itemuniquecode = barcode.substring(0, 9);
                String itemWeight = barcode.substring(9, 14);
                //Log.e(TAG, "1 barcode uniquecode" + itemuniquecode);
                //Log.e(TAG, "1 barcode itemweight" + itemWeight);

                for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {

                    Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                    if (String.valueOf(modal_newOrderItems.getBarcode()).equals(itemuniquecode)) {

                        Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                        newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                        newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                        newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                        newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                        newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                        newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                        newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                        newItem_newOrdersPojoClass.itemuniquecode = (modal_newOrderItems.getItemuniquecode());
                        newItem_newOrdersPojoClass.menuItemId = (modal_newOrderItems.getMenuItemId());
                        newItem_newOrdersPojoClass.keyforHashmap = barcode;
                        newItem_newOrdersPojoClass.key = modal_newOrderItems.getKey();
                        newItem_newOrdersPojoClass.itemavailability = (modal_newOrderItems.getItemavailability());


                        newItem_newOrdersPojoClass.allownegativestock =String.valueOf(modal_newOrderItems.getAllownegativestock());
                        newItem_newOrdersPojoClass.barcode_AvlDetails =String.valueOf(modal_newOrderItems.getBarcode_AvlDetails());
                        newItem_newOrdersPojoClass.itemavailability_AvlDetails =String.valueOf(modal_newOrderItems.getItemavailability_AvlDetails());
                        newItem_newOrdersPojoClass.key_AvlDetails =String.valueOf(modal_newOrderItems.getKey_AvlDetails());
                        newItem_newOrdersPojoClass.lastupdatedtime_AvlDetails =String.valueOf(modal_newOrderItems.getLastupdatedtime_AvlDetails());
                        newItem_newOrdersPojoClass.menuitemkey_AvlDetails =String.valueOf(modal_newOrderItems.getMenuitemkey_AvlDetails());
                        newItem_newOrdersPojoClass.receivedstock_AvlDetails =String.valueOf(modal_newOrderItems.getReceivedstock_AvlDetails());

                        newItem_newOrdersPojoClass.stockbalance_AvlDetails =String.valueOf(modal_newOrderItems.getStockbalance_AvlDetails());
                        newItem_newOrdersPojoClass.stockincomingkey_AvlDetails =String.valueOf(modal_newOrderItems.getStockincomingkey_AvlDetails());
                        newItem_newOrdersPojoClass.vendorkey_AvlDetails =String.valueOf(modal_newOrderItems.getVendorkey_AvlDetails());

                        newItem_newOrdersPojoClass.barcode =String.valueOf(modal_newOrderItems.getBarcode());
                        newItem_newOrdersPojoClass.tmcctgykey =String.valueOf(modal_newOrderItems.getTmcctgykey());




                        newItem_newOrdersPojoClass.discountpercentage =  ( String.valueOf(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getDiscountpercentage()))));
                       String tmcsubctgykey = (String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                        try {
                            if (modal_newOrderItems.getTmcsubctgykey().equals("")) {
                                newItem_newOrdersPojoClass.tmcsubctgykey = ("0");

                            } else {
                                newItem_newOrdersPojoClass.setTmcsubctgykey(String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        newItem_newOrdersPojoClass.quantity = "1";
                        newItem_newOrdersPojoClass.subTotal_perItem = "";
                        newItem_newOrdersPojoClass.total_of_subTotal_perItem = "";
                        newItem_newOrdersPojoClass.totalGstAmount = "";
                        newItem_newOrdersPojoClass.itemweightdetails = (String.valueOf(((modal_newOrderItems.getItemweightdetails()))));
                        newItem_newOrdersPojoClass.itemcutdetails = (String.valueOf(((modal_newOrderItems.getItemcutdetails()))));
                        newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(((modal_newOrderItems.getInventorydetails()))));



                        if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcpriceperkg")) {
                            int priceperKg = Integer.parseInt(modal_newOrderItems.getTmcpriceperkg());

                            int weight = Integer.parseInt(itemWeight);
                            if (weight < 1000) {
                                item_total = (priceperKg * weight);
                                //Log.e("TAG", "adapter 9 item_total price_per_kg" + priceperKg);

                                //Log.e("TAG", "adapter 9 item_total weight" + weight);

                                //Log.e("TAG", "adapter 9 item_total " + priceperKg * weight);

                                item_total = item_total / 1000;
                                //Log.e("TAG", "adapter 9 item_total " + item_total);

                                //Log.e("TAg", "weight2" + weight);
                                item_total = Double.parseDouble(decimalFormat.format(item_total));


                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(item_total));
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(item_total));
                                newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                //Log.e("TAg", "weight item_total" + item_total);

                                itemPrice_Widget.setText(String.valueOf(item_total));

                                NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                            }

                            if (weight == 1000) {
                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(priceperKg));
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(priceperKg));
                                newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);


                                NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                            }

                            if (weight > 1000) {
                                 priceperKg = Integer.parseInt(modal_newOrderItems.getTmcpriceperkg());

                                //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);

                                //Log.e("TAg", "weight3" + weight);

                                int itemquantity = weight - 1000;
                                //Log.e("TAg", "weight itemquantity" + itemquantity);

                                item_total = (priceperKg * itemquantity) / 1000;
                                item_total = Double.parseDouble(decimalFormat.format(item_total));


                                //Log.e("TAg", "weight item_total" + item_total);

                                double total = priceperKg + item_total;
                                total = Double.parseDouble(decimalFormat.format((total)));


                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(total));
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(total));
                                newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                                newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                itemPrice_Widget.setText(String.valueOf(total));
                                NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                            }


                        }


                        if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcprice")) {
                            newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(modal_newOrderItems.getTmcprice()));

                            newItem_newOrdersPojoClass.setItemFinalPrice(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getTmcprice())));
                          /*
                            if (modal_newOrderItems.getGrossweight().equals("") && modal_newOrderItems.getNetweight().equals("")) {
                                //Log.e(Constants.TAG, "getPortionsize " + (String.format(" %s", modal_newOrderItems.getPortionsize())));
                                newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getPortionsize());

                                itemWeight = String.valueOf(modal_newOrderItems.getPortionsize());
                            } else if (modal_newOrderItems.getNetweight().equals("")) {

                                //Log.e(Constants.TAG, "getGrossweight " + (String.format(" %s", modal_newOrderItems.getGrossweight())));

                                newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());
                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());


                            } else if (modal_newOrderItems.getGrossweight().equals("")) {
                                //Log.e(Constants.TAG, "getNetweight " + (String.format(" %s", modal_newOrderItems.getNetweight())));
                                newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getNetweight());

                                itemWeight = String.valueOf(modal_newOrderItems.getNetweight());


                            } else {
                                //Log.e(Constants.TAG, "getGrossweight " + (String.format(" %s", modal_newOrderItems.getGrossweight())));

                                newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());
                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());


                            }
                            */
                            try{
                                newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            try{
                                newItem_newOrdersPojoClass.grossweight = (modal_newOrderItems.getGrossweight());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            try{
                                newItem_newOrdersPojoClass.netweight = (modal_newOrderItems.getNetweight());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            try{
                                newItem_newOrdersPojoClass.portionsize = (modal_newOrderItems.getPortionsize());

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        }


                        addItemIntheCart(barcode, newItem_newOrdersPojoClass, itemWeight, modal_newOrderItems.getItemuniquecode());


                    }
                }
                }
                catch (Exception e ){
                    Toast.makeText(context,"Error in get MenuItem using TMC Barcode",Toast.LENGTH_LONG).show();

                }
               }

            }
        }


        private void addItemIntheCart(String barcode, Modal_NewOrderItems newItem_newOrdersPojoClass, String itemWeight, String itemUniquecode) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            boolean IsItemAlreadyAddedinCart = false;
            String pricetypeOfItem = "";
            try {
                pricetypeOfItem = newItem_newOrdersPojoClass.getPricetypeforpos().toString().toUpperCase();
            }
            catch (Exception e ) {
                Toast.makeText(context, "Error in boolean pricetypeOfItem ", Toast.LENGTH_LONG).show();

            }


            try {
                if (pricetypeOfItem.equals(Constants.TMCPRICEPERKG)) {
                    try {
                        IsItemAlreadyAddedinCart = checkforBarcodeInCart(barcode);
                    } catch (Exception e) {
                        Toast.makeText(context, "Error in boolean IsItemAlreadyAddedin Cart", Toast.LENGTH_LONG).show();

                    }


                    try {
                        IsItemAlreadyAddedinCart = checkforBarcodeInCart(barcode);
                    } catch (Exception e) {
                        Toast.makeText(context, "Error in boolean IsItemAlreadyAddedin Cart", Toast.LENGTH_LONG).show();

                    }
                    if (IsItemAlreadyAddedinCart) {
                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                        int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                        quantity = quantity + 1;

                        double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
                        double finalprice = quantity * itemPrice_quantityBased;

                        modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
                        modal_newOrderItems.setQuantity(String.valueOf(quantity));
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(barcode, modal_newOrderItems);
                        if (checkforBarcodeInCart("empty")) {
                            NewOrders_MenuItem_Fragment.cart_Item_List.remove("empty");

                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                        }


                    } else {
                        if (checkforBarcodeInCart("empty")) {
                            NewOrders_MenuItem_Fragment.cart_Item_List.remove("empty");

                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                        }

                        NewOrders_MenuItem_Fragment.cart_Item_List.add(barcode);
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(barcode, newItem_newOrdersPojoClass);

                    }
                    Modal_NewOrderItems m = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                    NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                }

                else{


                    try {
                        IsItemAlreadyAddedinCart = checkforBarcodeInCart(itemUniquecode);
                    } catch (Exception e) {
                        Toast.makeText(context, "Error in boolean IsItemAlreadyAddedin Cart", Toast.LENGTH_LONG).show();

                    }
                    if (IsItemAlreadyAddedinCart) {
                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(itemUniquecode);
                        int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                        quantity = quantity + 1;

                        double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
                        double finalprice = quantity * itemPrice_quantityBased;

                        modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
                        modal_newOrderItems.setQuantity(String.valueOf(quantity));
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode, modal_newOrderItems);
                        if (checkforBarcodeInCart("empty")) {
                            NewOrders_MenuItem_Fragment.cart_Item_List.remove("empty");

                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                        }


                    } else {
                        if (checkforBarcodeInCart("empty")) {
                            NewOrders_MenuItem_Fragment.cart_Item_List.remove("empty");

                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                        }

                        NewOrders_MenuItem_Fragment.cart_Item_List.add(itemUniquecode);
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode, newItem_newOrdersPojoClass);

                    }
                    NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                }
            }
            catch(Exception e ){
                    Toast.makeText(context, "Error in boolean pricetypeOfItem ", Toast.LENGTH_LONG).show();

                }


        }






        private Handler newHandler() {
            Handler.Callback callback = new Handler.Callback() {

                @Override
                public boolean handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    String data = bundle.getString("dropdown");

                    if (String.valueOf(data).equalsIgnoreCase("dismissdropdown")) {
                        //Log.e(TAG, "dismissDropdown");
                        //Log.e(Constants.TAG, "createBillDetails in CartItem 0 ");

                        sendHandlerMessage("addBillDetails");




                        autoComplete_widget.clearFocus();

                        autoComplete_widget.dismissDropDown();


                    }
                    return false;
                }
            };
            return new Handler(callback);
        }

    }

    private boolean checkforBarcodeInCart(String barcode) {
        String search = barcode;
        for(String str: NewOrders_MenuItem_Fragment.cart_Item_List) {
            if(str.trim().equals(search))
                return true;
        }
        return false;
    }


}





















































































/*
            barcode_widget.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String s1 = (editable.toString());
                    if(s1.length()==3){
                        if(s1.equals("999")){
                            isTMCproduct=true;
                        }
                        else {
                            isTMCproduct=false;
                        }
                    }
                    //Log.e(TAG, "Got barcode isBarcodeEntered in on textchanged"+s1);

                    //Log.e(TAG, "Got barcode isBarcodeEntered after text changed"+isdataFetched);

                        if (s1.length() > 3) {
                            if(!isdataFetched) {

                                if (isTMCproduct) {
                                if (barcode_widget.getText().toString().length() == 14) {

                                    //Log.e(TAG, "Got barcode " + barcode_widget.getText().length());
                                    //Log.e(TAG, "Got barcode isBarcodeEntered in condition check 13/14" + isdataFetched);

                                    String Barcode = barcode_widget.getText().toString();


                                      getMenuItemUsingBarCode(Barcode);
                                }
                            } else {
                                if (barcode_widget.getText().toString().length() == 13) {

                                    //Log.e(TAG, "Got barcode " + barcode_widget.getText().length());
                                    //Log.e(TAG, "Got barcode isBarcodeEntered in condition check 13/14" + isdataFetched);

                                    String Barcode = barcode_widget.getText().toString();

                                    getMenuItemUsingBarCode(Barcode);
                                }
                            }
                        }

                    }


                }
            });


 */
            /*
        for(int i=0; i<NewOrders_MenuItem_Fragment.cart_Item_List.size();i++) {
            String oldItemBarcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(i);
            if (!oldItemBarcode.equals(null)&&!itemUniquecode.equals(null)) {
                if(!itemUniquecode.equals("empty")) {
                    if (oldItemBarcode.equals(itemUniquecode)) {
                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(itemUniquecode);
                        int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                        quantity = quantity + 1;

                        int itemPrice_quantityBased = Integer.parseInt(modal_newOrderItems.getItemPrice_quantityBased());
                        int finalprice = quantity * itemPrice_quantityBased;
                        modal_newOrderItems.setItemFinalPrice(String.valueOf(finalprice));
                        modal_newOrderItems.setQuantity(String.valueOf(quantity));
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,modal_newOrderItems);
                        NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                    }
                    else{


                            NewOrders_MenuItem_Fragment.cart_Item_List.remove(i);
                            NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();


                          NewOrders_MenuItem_Fragment.cart_Item_List.add(itemUniquecode);
                        NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,newItem_newOrdersPojoClass);

                    }
                }
                else{
                    //Log.i(TAG,"Barcode is empty");

                }

            }
            else {
                //Log.i(TAG,"Barcode cannot be Null");
            }

        }


 */





/*

            //Log.e(TAG, "Got barcode addItemIntheCart"+isdataFetched);
            int last_ItemInCart = NewOrders_MenuItem_Fragment.cart_Item_List.size() - 1;
            //Log.e(TAG, "barcode uniquecode last_ItemInCart" + last_ItemInCart);
            //Log.e(TAG, "barcode uniquecode itemWeight" + itemWeight);
            //Log.e(TAG, "barcode uniquecode getItemFinalPrice" + newItem_newOrdersPojoClass.getPricePerItem());
            //Log.e(TAG, "barcode uniquecode getItemFinalWeight" + newItem_newOrdersPojoClass.getItemFinalWeight());

            Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cart_Item_List.get(last_ItemInCart);
            if (String.valueOf(modal_newOrderItems.getItemname()).equals("")) {
                //Log.e(TAG, "barcode in if  " + modal_newOrderItems.getItemname());

                modal_newOrderItems.setItemname(newItem_newOrdersPojoClass.getItemname());
                modal_newOrderItems.setTmcpriceperkg(newItem_newOrdersPojoClass.getTmcpriceperkg());
                modal_newOrderItems.setGrossweight(newItem_newOrdersPojoClass.getGrossweight());
                modal_newOrderItems.setNetweight(newItem_newOrdersPojoClass.getNetweight());
                modal_newOrderItems.setTmcprice(newItem_newOrdersPojoClass.getTmcprice());
                modal_newOrderItems.setGstpercentage(newItem_newOrdersPojoClass.getGstpercentage());
                modal_newOrderItems.setPortionsize(newItem_newOrdersPojoClass.getPortionsize());
                modal_newOrderItems.setPricetypeforpos(newItem_newOrdersPojoClass.getPricetypeforpos());
                modal_newOrderItems.setPricePerItem(newItem_newOrdersPojoClass.getPricePerItem());
                modal_newOrderItems.setQuantity(newItem_newOrdersPojoClass.getQuantity());
                modal_newOrderItems.setItemuniquecode(newItem_newOrdersPojoClass.getItemuniquecode());
                modal_newOrderItems.setItemFinalWeight(newItem_newOrdersPojoClass.getItemFinalWeight());
                newItem_newOrdersPojoClass.setSubTotal_perItem(newItem_newOrdersPojoClass.getSubTotal_perItem());
                newItem_newOrdersPojoClass.setTotal_of_subTotal_perItem(newItem_newOrdersPojoClass.getTotal_of_subTotal_perItem());
                newItem_newOrdersPojoClass.setGstAmount(newItem_newOrdersPojoClass.getGstAmount());
                newItem_newOrdersPojoClass.setItemPrice_quantityBased(newItem_newOrdersPojoClass.getItemPrice_quantityBased());


                //Log.e(TAG, "barcode in if before cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());

                // NewOrders_MenuItem_Fragment.cart_Item_List.add(last_ItemInCart,modal_newOrderItems);
                //Log.e(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
                //    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                // cart_Item_List.add(last_ItemInCart,modal_newOrderItems);
                //Log.i(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
                //Log.e(TAG, "Got barcode addItemIntheCart" +
                        "" +
                        ""+isdataFetched);
                NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                //Log.i(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
                //Log.i(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());



            }




 */







/*
        Modal_NewOrderItems recylerviewPojoClass = newOrders_menuItem_fragment.cart_Item_List.get(position);


        if(recylerviewPojoClass.getTmcpriceperkg().equals("")&&recylerviewPojoClass.getTmcprice().equals("")){
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getItemname());
            //Log.e("TAG", "adapter 1 " + recylerviewPojoClass.getGrossweight());
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getPricePerItem());
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getTmcpriceperkg());

            holder.itemPrice_Widget.setText(recylerviewPojoClass.getSubTotal_perItem());
            holder.itemWeight_widget.setText(recylerviewPojoClass.getGrossweight());
            holder.itemQuantity_widget.setText(recylerviewPojoClass.getPortionsize());
            holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());

            holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
            isdataFetched = false;
            //Log.e(TAG, "Got barcode isBarcodeEntered on empty data 2 "+isdataFetched);

        }
        else {



            newOrders_menuItem_fragment.add_amount_ForBillDetails();

            pricetype_of_pos = String.valueOf(recylerviewPojoClass.getPricetypeforpos());


            if (pricetype_of_pos.equals("tmcprice")) {

                holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                    holder.barcode_widget.setKeyListener(null);
                recylerviewPojoClass.setItemPrice_quantityBased(String.valueOf(recylerviewPojoClass.getTmcprice()));


                holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                holder.autoComplete_widget.setKeyListener(null);

                holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getPricePerItem()));
                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());

                if (recylerviewPojoClass.getPortionsize().equals("")) {
                    if (recylerviewPojoClass.getNetweight().equals("")) {
                        holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));

                    } else {
                        holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                    }

                } else {
                    holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));
                }

            } else if (pricetype_of_pos.equals("tmcpriceperkg")) {
                holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                holder.barcode_widget.setKeyListener(null);

                holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                holder.autoComplete_widget.setKeyListener(null);
                holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getPricePerItem()));
                recylerviewPojoClass.setItemPrice_quantityBased(String.valueOf(recylerviewPojoClass.getTmcpriceperkg()));

                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));
                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
            }
            if (position == (newOrders_menuItem_fragment.cart_Item_List.size() - 1)) {
                isdataFetched = false;
            }
        }



 */