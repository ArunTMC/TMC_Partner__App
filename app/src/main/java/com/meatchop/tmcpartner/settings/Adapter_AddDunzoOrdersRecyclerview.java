package com.meatchop.tmcpartner.settings;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Adapter_AddDunzoOrdersRecyclerview extends RecyclerView.Adapter<Adapter_AddDunzoOrdersRecyclerview.ViewHolder> {
    private Context context;
    private String pricetype_of_pos;
    Adapter_AutoCompleteMenuitemForDunzoOrders adapter;
    private double item_total = 0;
    private Handler handler;
    int price_per_kg, taxes_and_charges;
    AddDunzoOrders newOrders_menuItem_fragment;
    public static HashMap<String, Modal_NewOrderItems> itemInCart = new HashMap();
    List<Modal_MenuItem_Settings> Menulist;
    public Adapter_AddDunzoOrdersRecyclerview(Context context, HashMap<String, Modal_NewOrderItems> itemInCart, List<Modal_MenuItem_Settings>  menuItems, AddDunzoOrders newOrders_menuItem_fragment) {

        //Log.e(TAG, "Auto call adapter itemInCart itemInCart" + itemInCart.size());
        this.newOrders_menuItem_fragment = newOrders_menuItem_fragment;
        this.context = context;

        this.itemInCart = itemInCart;

        this.Menulist = menuItems;
    }
    @NonNull
    @Override
    public Adapter_AddDunzoOrdersRecyclerview.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.neworders_cart_tem_listview, parent, false);

        return new Adapter_AddDunzoOrdersRecyclerview.ViewHolder(view);
    }






    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Adapter_AddDunzoOrdersRecyclerview.ViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");


        holder.itemIndex.setText(String.valueOf(position + 1));

        if (position == (itemInCart.size() - 1)) {
            holder.addNewItem_layout.setVisibility(View.VISIBLE);
            holder.barcode_widget.setFocusable(false);


        } else {

            holder.addNewItem_layout.setVisibility(View.GONE);
        }




        int length = holder.autoComplete_widget.getText().length();
        holder.autoComplete_widget.setSelection(length);
        //Log.e("TAG", "position" + position);
        Modal_NewOrderItems recylerviewPojoClass = AddDunzoOrders.cartItem_hashmap.get(AddDunzoOrders.cart_Item_List.get(position));


        //Log.i("TAG", "HASHMAP" + recylerviewPojoClass.getItemuniquecode());

        if (recylerviewPojoClass.getItemuniquecode().equals("empty")) {
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getItemname());
            //Log.e("TAG", "adapter 1 " + recylerviewPojoClass.getGrossweight());
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getPricePerItem());
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getTmcpriceperkg());

            holder.itemPrice_Widget.setText("");
            holder.itemWeight_edittextwidget.setText("");
            holder.itemQuantity_widget.setText("");
            holder.barcode_widget.setText("");

            holder.autoComplete_widget.setText("");


        } else {




            pricetype_of_pos = String.valueOf(recylerviewPojoClass.getPricetypeforpos());


            if (pricetype_of_pos.equals("tmcprice")) {
                holder.gramsTextview.setVisibility(View.INVISIBLE);
                holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                if(!holder.autoComplete_widget.getText().toString().equals("")&&holder.autoComplete_widget.getText().length()>3){
                    holder.autoComplete_widget.setKeyListener(null);
                    holder.autoComplete_widget.clearFocus();
                    holder.autoComplete_widget.dismissDropDown();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        holder.autoComplete_widget.setFocusedByDefault(false);
                    }                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.barcode_widget.setFocusedByDefault(false);
                }
                holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                holder.barcode_widget.setKeyListener(null);






                holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));
                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                newOrders_menuItem_fragment.add_amount_ForBillDetails();
                try {
                    holder.itemWeight_edittextwidget.setVisibility(View.GONE);
                    holder.itemWeight_widget.setVisibility(View.VISIBLE);

                    if (recylerviewPojoClass.getPortionsize().equals("")) {
                        if (recylerviewPojoClass.getNetweight().equals("")) {
                            holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));

                        } else {
                            holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                        }

                    } else {
                        holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));
                    }
                }
                catch (Exception e){
                    //Log.e("TAG", "Cant set itemweight/portionsize in recycler adapter");

                }
                //  holder.itemWeight_edittextwidget.setKeyListener(null);

            } else if (pricetype_of_pos.equals("tmcpriceperkg")) {
                holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                holder.barcode_widget.setKeyListener(null);

                holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                if(!holder.autoComplete_widget.getText().toString().equals("")&&holder.autoComplete_widget.getText().length()>3){
                    holder.autoComplete_widget.setKeyListener(null);
                    holder.autoComplete_widget.clearFocus();
                    holder.autoComplete_widget.dismissDropDown();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        holder.autoComplete_widget.setFocusedByDefault(false);
                    }
                }






                holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));
                holder.itemWeight_widget.setVisibility(View.GONE);
                holder. itemWeight_edittextwidget.setVisibility(View.VISIBLE);
                holder.   gramsTextview.setVisibility(View.VISIBLE);
                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                holder.itemWeight_edittextwidget.setText(String.valueOf(recylerviewPojoClass.getItemFinalWeight()));
                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                newOrders_menuItem_fragment.add_amount_ForBillDetails();
            }
            //Log.e(TAG, "Got barcode isBarcodeEn position " + position);
            //Log.e(TAG, "Got barcode newOrders_menuItem_fragment.cart_Item_List.size() " + newOrders_menuItem_fragment.cart_Item_List.size());





        }


        holder.itemWeight_edittextwidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    String barcode = AddDunzoOrders.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = AddDunzoOrders.cartItem_hashmap.get(barcode);
                    String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();


                    if(pricetypeforpos.equals("tmcpriceperkg")){
                        String weight_string = holder.itemWeight_edittextwidget.getText().toString();
                        weight_string =weight_string .replaceAll("[^\\d.]", "");
                        modal_newOrderItems.setIstmcpriceperkgitemedited("TRUE");
                        int priceperKg = Integer.parseInt(modal_newOrderItems.getDunzoprice());

                        int weight = Integer.parseInt(weight_string);
                        if (weight < 1000) {
                            item_total = (priceperKg * weight);
                            //Log.e("TAG", "adapter 9 item_total price_per_kg" + priceperKg);

                            //Log.e("TAG", "adapter 9 item_total weight" + weight);

                            //Log.e("TAG", "adapter 9 item_total " + priceperKg * weight);

                            item_total = item_total / 1000;
                            //Log.e("TAG", "adapter 9 item_total " + item_total);

                            //Log.e("TAg", "weight2" + weight);
                            item_total = Double.parseDouble(decimalFormat.format(item_total));
                            int quantity = 0;
                            try {
                                quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                quantity = 1;
                            }
                            double itemtotalwithQuantity  =0;
                                try {

                                    itemtotalwithQuantity = item_total * quantity;
                                    modal_newOrderItems.setItemFinalPrice(String.valueOf(itemtotalwithQuantity));

                                }
                                catch (Exception e ){
                                    e.printStackTrace();
                                    modal_newOrderItems.setItemFinalPrice(String.valueOf(item_total));
                                    modal_newOrderItems.setQuantity(String.valueOf(1));

                                }
                            modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(item_total));
                            modal_newOrderItems.setItemFinalWeight(String.valueOf(weight)  + "g");
                            modal_newOrderItems.setGrossweight((String.valueOf(weight) ) + "g");

                            //Log.e("TAg", "weight item_total" + item_total);

                            //   holder.itemWeight_edittextwidget.setText(String.valueOf(item_total));

                            AddDunzoOrders.adapter_addDunzoOrdersRecyclerview.notifyDataSetChanged();

                        }

                        if (weight == 1000) {
                            int quantity = 0;
                            try {
                                quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                quantity = 1;
                            }
                            double itemtotalwithQuantity  =0;
                            try {

                                itemtotalwithQuantity = priceperKg * quantity;
                                modal_newOrderItems.setItemFinalPrice(String.valueOf(itemtotalwithQuantity));

                            }
                            catch (Exception e ){
                                e.printStackTrace();
                                modal_newOrderItems.setItemFinalPrice(String.valueOf(priceperKg));
                                modal_newOrderItems.setQuantity(String.valueOf(1));

                            }
                            modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(priceperKg));
                            modal_newOrderItems.setItemFinalWeight(String.valueOf(weight) + "g");
                            modal_newOrderItems.setGrossweight((String.valueOf(weight)) + "g");

                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);


                            AddDunzoOrders.adapter_addDunzoOrdersRecyclerview.notifyDataSetChanged();

                        }

                        if (weight > 1000) {
                            priceperKg = Integer.parseInt(modal_newOrderItems.getDunzoprice());

                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);

                            //Log.e("TAg", "weight3" + weight);

                            int itemquantity = weight - 1000;
                            //Log.e("TAg", "weight itemquantity" + itemquantity);

                            item_total = (priceperKg * itemquantity) / 1000;
                            item_total = Double.parseDouble(decimalFormat.format(item_total));


                            //Log.e("TAg", "weight item_total" + item_total);

                            double total = priceperKg + item_total;
                            total = Double.parseDouble(decimalFormat.format((total)));


                            int quantity = 0;
                            try {
                                quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                            }
                            catch (Exception e){
                                e.printStackTrace();
                                quantity = 1;
                            }
                            double itemtotalwithQuantity  =0;
                            try {

                                itemtotalwithQuantity = total * quantity;
                                modal_newOrderItems.setItemFinalPrice(String.valueOf(itemtotalwithQuantity));

                            }
                            catch (Exception e ){
                                e.printStackTrace();
                                modal_newOrderItems.setItemFinalPrice(String.valueOf(total));
                                modal_newOrderItems.setQuantity(String.valueOf(1));

                            }
                            modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(total));
                            modal_newOrderItems.setItemFinalWeight(String.valueOf(weight) + "g");
                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                            modal_newOrderItems.setGrossweight((String.valueOf(weight)) + "g");

                            //  holder.itemWeight_edittextwidget.setText(String.valueOf(total));
                            AddDunzoOrders.adapter_addDunzoOrdersRecyclerview.notifyDataSetChanged();

                        }

                    }






                }
                return false;
            }
        });





        holder.addNewItem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.autoComplete_widget.getText().toString().equals("") && (!holder.itemWeight_edittextwidget.getText().toString().equals("") || !holder.itemQuantity_widget.getText().toString().equals(""))) {



                    newOrders_menuItem_fragment.createEmptyRowInListView("empty");

                    newOrders_menuItem_fragment.CallAdapter();

                    
                  /*  if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                        newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                        newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                        newOrders_menuItem_fragment.discountAmount="0";
                    }

                   */

                } else {
                    Toast.makeText(context, "You have to fill this Item First", Toast.LENGTH_LONG).show();
                }


            }
        });


        holder.removeItem_fromCart_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "Item" + String.valueOf(AddDunzoOrders.cartItem_hashmap.size() - 1));
                String barcode = AddDunzoOrders.cart_Item_List.get(position);

                if(barcode.equals(""))
                {
                    barcode="empty";
                }
                //Log.i("TAG", "KEY: " + barcode);
                if ((AddDunzoOrders.cartItem_hashmap.size() - 1) == 0) {

                    //Log.i("TAG", "KEY: " + barcode);

                    AddDunzoOrders.cartItem_hashmap.remove(barcode);
                    AddDunzoOrders.cart_Item_List.remove(barcode);
                    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                    newOrders_menuItem_fragment.createEmptyRowInListView("empty");
                    // newOrders_menuItem_fragment.discountAmount ="0";
                    //  newOrders_menuItem_fragment.discount_Edit_widget .setText("0");
                    //   newOrders_menuItem_fragment.discount_rs_text_widget .setText("0");
                    newOrders_menuItem_fragment.finaltoPayAmount = "0";

                    newOrders_menuItem_fragment.CallAdapter();

                    //Log.e(TAG, "Item_not_deleted  " + String.valueOf(AddDunzoOrders.cartItem_hashmap.size() - 1));

                } else {
                    //Log.i("TAG", "KEY: " + barcode);


                    AddDunzoOrders.cartItem_hashmap.remove(barcode);
                    AddDunzoOrders.cart_Item_List.remove(barcode);

                    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                    newOrders_menuItem_fragment.CallAdapter();

                    //  AddDunzoOrders.adapter_cartItem_recyclerview.notifyDataSetChanged();
                    //Log.e(TAG, "Item_deleted  " + String.valueOf(AddDunzoOrders.cartItem_hashmap.size() - 1));


                }
              /*
                if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                    newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                    newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                    newOrders_menuItem_fragment.discountAmount="0";
                }


               */

            }
        });

        //Log.e(TAG, "Auto menu in cart adapter 1 : " + Menulist);





        holder.tmcUnitprice_weightAdd_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(! holder.itemQuantity_widget.getText().toString().equals("")&& !holder.itemPrice_Widget.getText().toString().equals(""))
                {

                    String barcode = AddDunzoOrders.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = AddDunzoOrders.cartItem_hashmap.get(barcode);
                    int quantity = Integer.parseInt(holder.itemQuantity_widget.getText().toString());

                    quantity = quantity + 1;
                    holder.itemQuantity_widget.setText(String.valueOf(quantity));
                    double item_price = Double.parseDouble(Objects.requireNonNull(modal_newOrderItems).getItemPrice_quantityBased());
                    item_price = item_price * quantity;

                    holder.itemPrice_Widget.setText(decimalFormat.format(item_price));
                    modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));


                    modal_newOrderItems.setQuantity(String.valueOf(quantity));
                    newOrders_menuItem_fragment.add_amount_ForBillDetails();
                /*
                    if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                        newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                        newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                        newOrders_menuItem_fragment.discountAmount="0";
                    }

                 */

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

                    String barcode = AddDunzoOrders.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = AddDunzoOrders.cartItem_hashmap.get(barcode);


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



                    } else {
                        Toast.makeText(context, "To Remove the Item Click the Delete Icon", Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(context,"First u have to Select an Item",Toast.LENGTH_LONG).show();
                }
            }

        });

        if(recylerviewPojoClass.getPricetypeforpos().equals("tmcpriceperkg")){
            if(recylerviewPojoClass.getIstmcpriceperkgitemedited().equals("FALSE")) {
                holder.tmcUnitprice_weightAdd_layout.setVisibility(View.INVISIBLE);
                holder.tmcUnitprice_weightMinus_layout.setVisibility(View.INVISIBLE);
                holder.itemQuantity_widget.setVisibility(View.INVISIBLE);
                holder.itemPrice_Widget.setVisibility(View.INVISIBLE);
                holder.removeItem_fromCart_widget.setVisibility(View.INVISIBLE);
            }
            else{
                holder.tmcUnitprice_weightAdd_layout.setVisibility(View.VISIBLE);
                holder.tmcUnitprice_weightMinus_layout.setVisibility(View.VISIBLE);
                holder.itemQuantity_widget.setVisibility(View.VISIBLE);
                holder.itemPrice_Widget.setVisibility(View.VISIBLE);
                holder.removeItem_fromCart_widget.setVisibility(View.VISIBLE);
            }

        }
        else{
            holder.tmcUnitprice_weightAdd_layout.setVisibility(View.VISIBLE);
            holder.tmcUnitprice_weightMinus_layout.setVisibility(View.VISIBLE);
            holder.itemQuantity_widget.setVisibility(View.VISIBLE);
            holder.itemPrice_Widget.setVisibility(View.VISIBLE);
            holder.removeItem_fromCart_widget.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return itemInCart.size();
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




    public class ViewHolder extends RecyclerView.ViewHolder {
        AutoCompleteTextView autoComplete_widget;

        LinearLayout tmcUnitprice_weightAdd_layout, tmcUnitprice_weightMinus_layout;


        TextView itemIndex,itemWeight_widget, itemQuantity_widget;

        TextView itemPrice_Widget,gramsTextview;

        EditText itemWeight_edittextwidget,barcode_widget;

        ImageView minus_to_remove_item_widget;
        LinearLayout removeItem_fromCart_widget, addNewItem_layout;
        ConstraintLayout parentLayout;
        boolean isTMCproduct = false;
        boolean isIndiaGateBasmatiRiceproduct = false;

        private Adapter_AddDunzoOrdersRecyclerview.ViewHolder.EditTextListener EditTextListener = new Adapter_AddDunzoOrdersRecyclerview.ViewHolder.EditTextListener();

        public ViewHolder(View itemView) {
            super(itemView);
            this.autoComplete_widget = itemView.findViewById(R.id.autoComplete_widget);
            this.itemIndex = itemView.findViewById(R.id.item_NO);
            this.itemWeight_edittextwidget = itemView.findViewById(R.id.itemWeight_edittextwidget);
            this.barcode_widget =  itemView.findViewById(R.id.barcode_widget);
            this.tmcUnitprice_weightAdd_layout = itemView.findViewById(R.id.tmcUnitprice_weightAdd_layout);
            this.tmcUnitprice_weightMinus_layout = itemView.findViewById(R.id.tmcUnitprice_weightMinus_layout);
            this.itemWeight_widget = itemView.findViewById(R.id.itemWeight_widget);
            this.itemQuantity_widget = itemView.findViewById(R.id.itemQuantity_widget);

            this.gramsTextview = itemView.findViewById(R.id.gramsTextview);


            this.itemPrice_Widget = itemView.findViewById(R.id.itemPrice_Widget);

            this.minus_to_remove_item_widget = itemView.findViewById(R.id.minus_to_remove_item_widget);

            this.removeItem_fromCart_widget = itemView.findViewById(R.id.removeItem_fromCart_widget);
            this.addNewItem_layout = itemView.findViewById(R.id.addNewItem_layout);

            this.parentLayout = itemView.findViewById(R.id.parentLayout);
            itemWeight_widget.setVisibility(View.GONE);
            itemWeight_edittextwidget.setVisibility(View.VISIBLE);
            gramsTextview.setVisibility(View.VISIBLE);
            adapter = new Adapter_AutoCompleteMenuitemForDunzoOrders(context, Menulist,getPosition());
            adapter.setHandler(newHandler());


            autoComplete_widget.setAdapter(adapter);
            autoComplete_widget.clearFocus();



            //  itemWeight_edittextwidget.addTextChangedListener(EditTextListener);


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

                //   String s1 = (s.toString());
                //do what you want on the press of 'done'
                String barcode = AddDunzoOrders.cart_Item_List.get(getPosition());
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                Modal_NewOrderItems modal_newOrderItems = AddDunzoOrders.cartItem_hashmap.get(barcode);
                String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();


                if(pricetypeforpos.equals("tmcpriceperkg")){
                    String weight_string = modal_newOrderItems.getItemFinalWeight();
                    weight_string =weight_string .replaceAll("[^\\d.]", "");

                    int priceperKg = Integer.parseInt(modal_newOrderItems.getDunzoprice());

                    int weight = Integer.parseInt(weight_string);
                    if (weight < 1000) {
                        item_total = (priceperKg * weight);
                        //Log.e("TAG", "adapter 9 item_total price_per_kg" + priceperKg);

                        //Log.e("TAG", "adapter 9 item_total weight" + weight);

                        //Log.e("TAG", "adapter 9 item_total " + priceperKg * weight);

                        item_total = item_total / 1000;
                        //Log.e("TAG", "adapter 9 item_total " + item_total);

                        //Log.e("TAg", "weight2" + weight);
                        item_total = Double.parseDouble(decimalFormat.format(item_total));


                        modal_newOrderItems.setItemFinalPrice(String.valueOf(item_total));
                        modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(item_total));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(weight)  + "g");
                        modal_newOrderItems.setGrossweight((String.valueOf(weight) ) + "g");

                        //Log.e("TAg", "weight item_total" + item_total);

                        //   itemWeight_edittextwidget.setText(String.valueOf(weight));
                        AddDunzoOrders.recyclerView.post(new Runnable()
                        {
                            @Override
                            public void run() {
                                AddDunzoOrders.adapter_addDunzoOrdersRecyclerview.notifyDataSetChanged();
                            }
                        });


                    }

                    if (weight == 1000) {
                        modal_newOrderItems.setItemFinalPrice(String.valueOf(priceperKg));
                        modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(priceperKg));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(weight) + "g");
                        modal_newOrderItems.setGrossweight((String.valueOf(weight)) + "g");

                        //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                        //itemWeight_edittextwidget.setText(String.valueOf(weight));


                        AddDunzoOrders.recyclerView.post(new Runnable()
                        {
                            @Override
                            public void run() {
                                AddDunzoOrders.adapter_addDunzoOrdersRecyclerview.notifyDataSetChanged();
                            }
                        });
                    }

                    if (weight > 1000) {
                        priceperKg = Integer.parseInt(modal_newOrderItems.getDunzoprice());

                        //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);

                        //Log.e("TAg", "weight3" + weight);

                        int itemquantity = weight - 1000;
                        //Log.e("TAg", "weight itemquantity" + itemquantity);

                        item_total = (priceperKg * itemquantity) / 1000;
                        item_total = Double.parseDouble(decimalFormat.format(item_total));


                        //Log.e("TAg", "weight item_total" + item_total);

                        double total = priceperKg + item_total;
                        total = Double.parseDouble(decimalFormat.format((total)));


                        modal_newOrderItems.setItemFinalPrice(String.valueOf(total));
                        modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(total));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(weight) + "g");
                        //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                        modal_newOrderItems.setGrossweight((String.valueOf(weight)) + "g");

                        //   itemWeight_edittextwidget.setText(String.valueOf(weight));
                        AddDunzoOrders.recyclerView.post(new Runnable()
                        {
                            @Override
                            public void run() {
                                AddDunzoOrders.adapter_addDunzoOrdersRecyclerview.notifyDataSetChanged();
                            }
                        });



                    }

                }




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
}


