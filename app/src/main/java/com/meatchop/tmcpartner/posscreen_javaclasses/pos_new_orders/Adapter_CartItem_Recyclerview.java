package com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class Adapter_CartItem_Recyclerview extends RecyclerView.Adapter<Adapter_CartItem_Recyclerview.ViewHolder> {

    private Context context;
    private String pricetype_of_pos;
    Adapter_AutoCompleteMenuItem adapter;
    private double item_total = 0;
    private double item_weight = 0;

    String Menulist,vendorType="" , vendorkey ="";
    private Handler handler;
    int price_per_kg, taxes_and_charges;
    NewOrders_MenuItem_Fragment newOrders_menuItem_fragment;
    public static HashMap<String,Modal_NewOrderItems> itemInCart = new HashMap();
    public static List<Modal_NewOrderItems> completemenuItem;
    public static boolean isbarcodescannerconnected = false , isThreadinSleepState = false;
    int finallyMethodCalledCount = 0;
    boolean isWeightCanBeEdited = true , isweightmachineconnected = false , isListRecycledAfterItemAdded = true;
    public Adapter_CartItem_Recyclerview(Context context, HashMap<String, Modal_NewOrderItems> itemInCart, String menuItems, NewOrders_MenuItem_Fragment newOrders_menuItem_fragment, List<Modal_NewOrderItems> completemenuItem) {

        //Log.e(TAG, "Auto call adapter itemInCart itemInCart" + itemInCart.size());
        this.newOrders_menuItem_fragment = newOrders_menuItem_fragment;
        this.context = context;
        this.completemenuItem = completemenuItem;
        this.Menulist = menuItems;
        SharedPreferences shared = context.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorType = shared.getString("VendorType","");
        vendorkey = shared.getString("VendorKey","");
        isWeightCanBeEdited = (shared.getBoolean("isweighteditable", false));
        isweightmachineconnected = (shared.getBoolean("isweightmachineconnected", false));
        isbarcodescannerconnected =  (shared.getBoolean("isbarcodescannerconnected", false));

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

        Log.i("Adapter", "Got refreshed " + position);

        if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
           newOrders_menuItem_fragment.redeemPoints_String ="0";
           newOrders_menuItem_fragment.totalpointsredeemedalreadybyuser="0";
           newOrders_menuItem_fragment.totalredeempointsuserhave="0";
           newOrders_menuItem_fragment.pointsalreadyredeemDouble=0;
           newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked=false;
           //discountlayout visible
            newOrders_menuItem_fragment.discountAmountLayout.setVisibility(View.GONE);
           newOrders_menuItem_fragment.redeemPointsLayout.setVisibility(View.GONE);

           newOrders_menuItem_fragment.discount_textview_labelwidget.setVisibility(View.VISIBLE);
           newOrders_menuItem_fragment.discount_rs_text_widget.setVisibility(View.VISIBLE);
           newOrders_menuItem_fragment.redeemedpoints_Labeltextwidget.setVisibility(View.GONE);
           newOrders_menuItem_fragment.ponits_redeemed_text_widget.setVisibility(View.GONE);
           newOrders_menuItem_fragment.add_amount_ForBillDetails();
        }


        //Log.e(TAG, "onBindViewHolder: called.");
        //Log.e("TAG", "adapter       " + newOrders_menuItem_fragment.cart_Item_List.size());
        //Log.e("TAG", "adapter       ");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");



        holder.itemIndex.setText(String.valueOf(position + 1));

         if (position == (NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1)) {
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
        recylerviewPojoClass.setLastEntry(false);
        if (recylerviewPojoClass.getPricetypeforpos().toLowerCase().equals("tmcpriceperkg")) {

            if (isWeightCanBeEdited || isweightmachineconnected || !isbarcodescannerconnected) {
               // if(isWeightCanBeEdited) {
                    if (recylerviewPojoClass.getisPriceEdited()) {
                        holder.itemPrice_Widget.setVisibility(View.INVISIBLE);
                        holder.itemprice_edittextwidget.setVisibility(View.VISIBLE);
                    } else {
                        holder.itemPrice_Widget.setVisibility(View.VISIBLE);
                        holder.itemprice_edittextwidget.setVisibility(View.INVISIBLE);
                    }

                    holder.edit_price_layout.setVisibility(View.VISIBLE);

              // }

                if(recylerviewPojoClass.getisWeightEdited()){
                    holder.itemWeight_widget.setVisibility(View.INVISIBLE);
                    holder.itemWeight_edittextwidget.setVisibility(View.VISIBLE);


                }
                else{
                    holder.itemWeight_widget.setVisibility(View.VISIBLE);
                    holder.itemWeight_edittextwidget.setVisibility(View.INVISIBLE);


                }
                 holder.edit_weight_layout.setVisibility(View.VISIBLE);

            }

            else {
                holder.itemWeight_widget.setVisibility(View.VISIBLE);
                holder.itemWeight_edittextwidget.setVisibility(View.INVISIBLE);
                holder.edit_weight_layout.setVisibility(View.INVISIBLE);

                holder.itemPrice_Widget.setVisibility(View.VISIBLE);
                holder.itemprice_edittextwidget.setVisibility(View.INVISIBLE);
                holder.edit_price_layout.setVisibility(View.INVISIBLE);
            }
        }
        else {
            holder.itemWeight_widget.setVisibility(View.VISIBLE);
            holder.itemWeight_edittextwidget.setVisibility(View.INVISIBLE);
            holder.edit_weight_layout.setVisibility(View.INVISIBLE);
            holder.itemPrice_Widget.setVisibility(View.VISIBLE);
            holder.itemprice_edittextwidget.setVisibility(View.INVISIBLE);
            holder.edit_price_layout.setVisibility(View.INVISIBLE);

        }
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
            holder.itemprice_edittextwidget.setText("");

            holder.autoComplete_widget.setText("");
            holder.itemWeight_edittextwidget.setText("");
            holder. barcode_widget.addTextChangedListener(holder.EditTextListener);
        //    holder. itemWeight_edittextwidget.addTextChangedListener(holder.emptyWeightEditTextListener);

        }
        else {

            holder. barcode_widget.removeTextChangedListener(holder.EditTextListener);

            pricetype_of_pos = String.valueOf(recylerviewPojoClass.getPricetypeforpos());


            if (pricetype_of_pos.equals("tmcprice")) {
                holder.gramsTextview.setVisibility(View.GONE);

                holder.autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                if(!holder.autoComplete_widget.getText().toString().equals("")&&holder.autoComplete_widget.getText().length()>3){
                    holder.autoComplete_widget.setKeyListener(null);

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.barcode_widget.setFocusedByDefault(false);
                }
                holder.barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                if(!holder.barcode_widget.getText().toString().equals("")&&holder.barcode_widget.getText().length()>3){
                  //   holder.barcode_widget.setKeyListener(null);

                }
                holder.barcode_widget.setKeyListener(null);

            //    holder.itemWeight_edittextwidget.setKeyListener(null);



                holder.itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));
                holder.itemprice_edittextwidget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));

                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                newOrders_menuItem_fragment.add_amount_ForBillDetails();
               try {
                   if(recylerviewPojoClass.getGrossweight().equals("")){
                       if (recylerviewPojoClass.getNetweight().equals("")) {
                           holder.itemWeight_widget.setVisibility(View.VISIBLE);
                           holder.itemWeight_edittextwidget.setVisibility(View.INVISIBLE);
                           holder.edit_weight_layout.setVisibility(View.INVISIBLE);

                           holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));

                       }
                       else{
                           holder.itemWeight_widget.setVisibility(View.VISIBLE);
                           holder.itemWeight_edittextwidget.setVisibility(View.INVISIBLE);
                           holder.edit_weight_layout.setVisibility(View.INVISIBLE);

                           holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                       }
                   }
                   else{
                       String grossWeightingramsString = String.valueOf(recylerviewPojoClass.getGrossweight());
                     //  String weightinKGString = convertGramsToKilograms(grossWeightingramsString);

                       holder.itemWeight_widget.setText(String.valueOf(grossWeightingramsString) );
                       holder.itemWeight_edittextwidget.setText(String.valueOf(grossWeightingramsString));

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

            }
            else if (pricetype_of_pos.equals("tmcpriceperkg")) {
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
                holder.itemprice_edittextwidget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));

                // if(vendorType.equals(Constants.WholeSales_VendorType)) {

                //}

                if(vendorType.equals(Constants.WholeSales_VendorType)) {
                    //holder.itemWeight_widget.setVisibility(View.GONE);
                   // holder. itemWeight_edittextwidget.setVisibility(View.VISIBLE);
                    holder.   gramsTextview.setVisibility(View.VISIBLE);


                }
                else{
                    //holder.itemWeight_widget.setVisibility(View.VISIBLE);
                   //holder. itemWeight_edittextwidget.setVisibility(View.GONE);
                    holder.   gramsTextview.setVisibility(View.GONE);

                }

                taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                if (isWeightCanBeEdited) {

                    String grossWeightingramsString = String.valueOf(recylerviewPojoClass.getItemFinalWeight());
                    String weightinKGString = convertGramsToKilograms(grossWeightingramsString);


                    holder.itemWeight_edittextwidget.setText(String.valueOf(weightinKGString) + "Kg");

                    holder.itemWeight_widget.setText(String.valueOf(weightinKGString) + "Kg");
                }
                else{
                    holder.itemWeight_edittextwidget.setText(String.valueOf(recylerviewPojoClass.getItemFinalWeight()) );

                    holder.itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalWeight()) );

                }
                holder.itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));

           //     holder. itemWeight_edittextwidget.addTextChangedListener(holder.WeightEditTextListener);


                newOrders_menuItem_fragment.add_amount_ForBillDetails();
            }
            //Log.e(TAG, "Got barcode isBarcodeEn position " + position);
            //Log.e(TAG, "Got barcode newOrders_menuItem_fragment.cart_Item_List.size() " + newOrders_menuItem_fragment.cart_Item_List.size());


        }
        try {
            if (vendorkey.equals("vendor_1")) {
                if (recylerviewPojoClass.getItemuniquecode().equals("1612")){
                    holder.tmcUnitprice_weightAdd_layout.setVisibility(View.INVISIBLE);
                    holder.itemQuantity_widget.setVisibility(View.VISIBLE);
                    holder.tmcUnitprice_weightMinus_layout.setVisibility(View.GONE);
                    holder.itemQuantity_edittextwidget.setVisibility(View.GONE);
                    holder.edit_quantity_layout.setVisibility(View.VISIBLE);
                }
                else{
                    holder.tmcUnitprice_weightAdd_layout.setVisibility(View.VISIBLE);
                    holder.itemQuantity_widget.setVisibility(View.VISIBLE);
                    holder.tmcUnitprice_weightMinus_layout.setVisibility(View.VISIBLE);

                    holder.itemQuantity_edittextwidget.setVisibility(View.GONE);
                    holder.edit_quantity_layout.setVisibility(View.GONE);
                }
            }
            else {
                holder.tmcUnitprice_weightAdd_layout.setVisibility(View.VISIBLE);
                holder.itemQuantity_widget.setVisibility(View.VISIBLE);
                holder.tmcUnitprice_weightMinus_layout.setVisibility(View.VISIBLE);

                holder.itemQuantity_edittextwidget.setVisibility(View.GONE);
                holder.edit_quantity_layout.setVisibility(View.GONE);
            }
        }
        catch (Exception e){
            holder.tmcUnitprice_weightAdd_layout.setVisibility(View.VISIBLE);
            holder.itemQuantity_widget.setVisibility(View.VISIBLE);
            holder.tmcUnitprice_weightMinus_layout.setVisibility(View.VISIBLE);

            holder.itemQuantity_edittextwidget.setVisibility(View.GONE);
            holder.edit_quantity_layout.setVisibility(View.GONE);
            e.printStackTrace();
        }

        holder.itemQuantity_edittextwidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 try {

                     if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                         //do what you want on the press of 'done'

                         InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                         Objects.requireNonNull(imm).hideSoftInputFromWindow(holder.itemQuantity_edittextwidget.getWindowToken(), 0);

                         String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                         Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);





                             String quantity_String = holder.itemQuantity_edittextwidget.getText().toString();

                             quantity_String = quantity_String.replaceAll("[^\\d.]", "");

                             if (quantity_String.equals("")) {
                                 AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.Quantity_cant_be_empty);

                             } else {
                                 int quantity = 0;
                                 try{
                                     quantity = Integer.parseInt(quantity_String);
                                 }
                                 catch (Exception e){
                                     quantity =0;
                                     e.printStackTrace();
                                 }




                                 if (quantity == 0) {
                                     AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.Quantity_cant_be_Zero);

                                 } else {
                                     modal_newOrderItems.setisQuantityEdited(false);



                                     holder.itemQuantity_widget.setText(String.valueOf(quantity));
                                     double item_price = Double.parseDouble(Objects.requireNonNull(modal_newOrderItems).getItemPrice_quantityBased());
                                     item_price = item_price * quantity;

                                     holder.itemPrice_Widget.setText(decimalFormat.format(item_price));
                                     holder.itemprice_edittextwidget.setText(decimalFormat.format(item_price));

                                     modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));


                                     modal_newOrderItems.setQuantity(String.valueOf(quantity));
                                     if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                         newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                                     }
                                     if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                         newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                         newOrders_menuItem_fragment.discount_rs_text_widget.setText("0");
                                         newOrders_menuItem_fragment.discountAmount_StringGlobal ="0";
                                     }
                                     newOrders_menuItem_fragment.add_amount_ForBillDetails();





                                     holder.tmcUnitprice_weightAdd_layout.setVisibility(View.GONE);
                                     holder.itemQuantity_widget.setVisibility(View.VISIBLE);
                                     holder.tmcUnitprice_weightMinus_layout.setVisibility(View.GONE);

                                     holder.itemQuantity_edittextwidget.setVisibility(View.GONE);
                                     holder.edit_quantity_layout.setVisibility(View.VISIBLE);


                                     newOrders_menuItem_fragment.add_amount_ForBillDetails();
                                     NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();




                                 }
                             }
                             // onNewDataArrived(itemInCart);




                     }
                 }
                 catch (Exception e) {
                     e.printStackTrace();
                 }


                 return false;
             }
         });

        holder.edit_quantity_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                if(!modal_newOrderItems.getisWeightEdited()) {
                    if (!modal_newOrderItems.getisPriceEdited()) {


                            holder.itemQuantity_edittextwidget.setFocusable(true);
                            holder.itemQuantity_widget.setVisibility(View.GONE);
                            holder.itemQuantity_edittextwidget.setVisibility(View.VISIBLE);
                            holder.itemQuantity_edittextwidget.setText(String.valueOf(holder.itemQuantity_widget.getText().toString()));
                            modal_newOrderItems.setisQuantityEdited(true);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    holder.itemQuantity_edittextwidget.requestFocus();
                                    InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    mgr.showSoftInput(holder.itemQuantity_edittextwidget, InputMethodManager.SHOW_IMPLICIT);
                                    holder.itemQuantity_edittextwidget.setSelection(holder.itemQuantity_edittextwidget.getText().length());
                                }
                            }, 0);


                    }
                    else {
                        AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.CannotbeEdited_When_EditingPrice);

                    }
                }
                else{
                    AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.CannotbeEdited_When_EditingWeight);

                }
                }
        });
        holder.addNewItem_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // Toast.makeText(context, "Store Address : "+ newOrders_menuItem_fragment.StoreAddressLine1, Toast.LENGTH_LONG).show();



                                if (!holder.autoComplete_widget.getText().toString().equals("") && (!holder.itemWeight_widget.getText().toString().equals("") && (!holder.itemWeight_edittextwidget.getText().toString().equals("")) || !holder.itemQuantity_widget.getText().toString().equals(""))) {

                                    if(!newOrders_menuItem_fragment.isConnectUSBSerialPort) {

                                    if(newOrders_menuItem_fragment.newRowCanBeCreated()) {
                                        newOrders_menuItem_fragment.createEmptyRowInListView("empty");
                                        // Log.i("TAG", "itemCart : " + itemInCart.size());
                                        holder.addNewItem_layout.setVisibility(View.GONE);
                                        // onNewDataArrived(itemInCart);
                                        finallyMethodCalledCount = 0;
                                        isThreadinSleepState = false;
                                        newOrders_menuItem_fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();
                                        isListRecycledAfterItemAdded = true;
                                        if (newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked) {
                                            newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                                        }
                                        if ((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0")) || (!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))) {
                                            newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                            newOrders_menuItem_fragment.discount_rs_text_widget.setText("0");
                                            newOrders_menuItem_fragment.discountAmount_StringGlobal = "0";
                                            newOrders_menuItem_fragment.discountAmount_DoubleGlobal = 0;
                                            newOrders_menuItem_fragment.isDiscountApplied = false;
                                        }
                                    }
                                    else{
                                        //if item cannot be created didnt have to do anythingbuil
                                    }
                                    }
                                    else{
                                        Toast.makeText(context, "You have to fill this Item weight First", Toast.LENGTH_LONG).show();

                                    }
                                }
                                else {
                                    Toast.makeText(context, "You have to fill this Item First", Toast.LENGTH_LONG).show();
                                }




                        }
                    });


        holder.removeItem_fromCart_widget.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("TAG", "Item" + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));
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
;                                newOrders_menuItem_fragment.discountAmount_StringGlobal ="0";
                                newOrders_menuItem_fragment.discount_Edit_widget .setText("0");
                                newOrders_menuItem_fragment.discount_rs_text_widget .setText("0");
                                newOrders_menuItem_fragment.finaltoPayAmount = "0";

                                newOrders_menuItem_fragment.CallAdapter();

                                //Log.e(TAG, "Item_not_deleted  " + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));

                            } else {
                                //Log.i("TAG", "KEY: " + barcode);

                                try {
                                   // NewOrders_MenuItem_Fragment.cartItem_hashmap.get(NewOrders_MenuItem_Fragment.cart_Item_List.get(position-1)).setLastEntry(true);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                NewOrders_MenuItem_Fragment.cartItem_hashmap.remove(barcode);
                                NewOrders_MenuItem_Fragment.cart_Item_List.remove(barcode);
                                newOrders_menuItem_fragment.add_amount_ForBillDetails();

                                 newOrders_menuItem_fragment.CallAdapter();

                                //NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();
                                //Log.e(TAG, "Item_deleted  " + String.valueOf(NewOrders_MenuItem_Fragment.cartItem_hashmap.size() - 1));


                            }
                            if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                            }
                            if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                newOrders_menuItem_fragment.discount_rs_text_widget.setText("0.00");
                                newOrders_menuItem_fragment.discountAmount_StringGlobal ="0";
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
                                holder.itemprice_edittextwidget.setText(decimalFormat.format(item_price));

                                modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));


                                modal_newOrderItems.setQuantity(String.valueOf(quantity));
                                if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                    newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                                }
                                if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                    newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                    newOrders_menuItem_fragment.discount_rs_text_widget.setText("0");
                                    newOrders_menuItem_fragment.discountAmount_StringGlobal ="0";
                                }
                                newOrders_menuItem_fragment.add_amount_ForBillDetails();


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
                                holder.itemprice_edittextwidget.setText(decimalFormat.format(item_price));

                                modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));


                                if(newOrders_menuItem_fragment.isProceedtoCheckoutinRedeemdialogClicked){
                                    newOrders_menuItem_fragment.cancelRedeemPointsFromOrder();

                                }
                                if((!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals("0"))||(!newOrders_menuItem_fragment.discount_Edit_widget.getText().toString().equals(""))){
                                    newOrders_menuItem_fragment.discount_Edit_widget.setText("0");
                                    newOrders_menuItem_fragment.discount_rs_text_widget.setText("0");
                                    newOrders_menuItem_fragment.discountAmount_StringGlobal ="0";
                                }
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



        holder.edit_price_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                if(!modal_newOrderItems.getisQuantityEdited()) {
                    if (!modal_newOrderItems.getisWeightEdited()) {

                        String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();
                        if (pricetypeforpos.equals("tmcpriceperkg")) {
                            holder.itemprice_edittextwidget.setFocusable(true);
                            holder.itemPrice_Widget.setVisibility(View.INVISIBLE);
                            holder.itemprice_edittextwidget.setVisibility(View.VISIBLE);

                            modal_newOrderItems.setisPriceEdited(true);

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    holder.itemprice_edittextwidget.requestFocus();
                                    InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    mgr.showSoftInput(holder.itemprice_edittextwidget, InputMethodManager.SHOW_IMPLICIT);
                                    holder.itemprice_edittextwidget.setSelection(holder.itemprice_edittextwidget.getText().length());
                                }
                            }, 0);

                        }
                    }
                    else {
                        AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.CannotbeEdited_When_EditingWeight);

                    }
                }
                else {
                    AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.CannotbeEdited_When_EditingQuantity);

                }
            }
        });




        holder.edit_weight_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                            Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                            if(!modal_newOrderItems.getisQuantityEdited()) {

                                if(!Objects.requireNonNull(modal_newOrderItems).getisPriceEdited()) {
                            String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();
                            if(pricetypeforpos.equals("tmcpriceperkg")) {
                                holder.itemWeight_edittextwidget.setFocusable(true);
                                holder.itemWeight_widget.setVisibility(View.INVISIBLE);
                                holder.itemWeight_edittextwidget.setVisibility(View.VISIBLE);

                                modal_newOrderItems.setisWeightEdited(true);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        holder.itemWeight_edittextwidget.requestFocus();
                                        InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                        mgr.showSoftInput(holder.itemWeight_edittextwidget, InputMethodManager.SHOW_IMPLICIT);
                                        holder.itemWeight_edittextwidget.setSelection(holder.itemWeight_edittextwidget.getText().length());
                                    }
                                },0);

                            }
                            if(isweightmachineconnected){
                                holder.itemWeight_edittextwidget.setText("");
                                newOrders_menuItem_fragment.connectUSBSerialPort();
                                newOrders_menuItem_fragment.setHandlerForReceivingWeight(holder.newHandler( holder.itemWeight_edittextwidget));
                            }


                            }
                            else{
                            AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.CannotbeEdited_When_EditingPrice);

                            }
                            }
                            else {
                                AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.CannotbeEdited_When_EditingQuantity);

                            }

                        }
                    });


                /*  holder.itemWeight_edittextwidget.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                                String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                                Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                                String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();
                                if(pricetypeforpos.equals("tmcpriceperkg")) {
                                    if(modal_newOrderItems.getisWeightEdittextClicked()) {
                                        modal_newOrderItems.setisWeightEdited(true);


                                    }

                                }

                        }
                    });



                 */



        holder.itemprice_edittextwidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        //do what you want on the press of 'done'

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        Objects.requireNonNull(imm).hideSoftInputFromWindow(holder.itemprice_edittextwidget.getWindowToken(), 0);

                        String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                        String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();


                        if (pricetypeforpos.equals("tmcpriceperkg")) {

                            String price_string = holder.itemprice_edittextwidget.getText().toString();
                            holder.itemPrice_Widget.setText(String.valueOf(price_string));
                            holder.itemQuantity_widget.setText(String.valueOf("1"));

                            price_string = price_string.replaceAll("[^\\d.]", "");

                            if (price_string.equals("")) {
                                AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.Price_cant_be_empty);

                            }
                            else {
                                double priceDouble = Double.parseDouble(price_string);
                                if (priceDouble == 0) {
                                    AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.Price_cant_be_Zero);

                                } else {
                                modal_newOrderItems.setisPriceEdited(false);

                                double priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkg());

                                int price = (int) (priceDouble);
                                item_weight = (1000.0 / priceperKg);

                                item_weight = item_weight * price;
                                int item_weight_int = (int) Math.round(item_weight);


                                modal_newOrderItems.setItemFinalPrice(String.valueOf(price));
                                modal_newOrderItems.setQuantity(String.valueOf("1"));
                                modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(price));

                                modal_newOrderItems.setItemFinalWeight(String.valueOf(item_weight_int) + "g");
                                modal_newOrderItems.setGrossweight((String.valueOf(item_weight_int)) + "g");

                                    if (isWeightCanBeEdited) {

                                        String grossWeightingramsString = String.valueOf(item_weight_int);
                                        String weightinKGString = convertGramsToKilograms(grossWeightingramsString);
                                        holder.itemWeight_widget.setText(String.valueOf(weightinKGString) + "Kg");
                                        holder.itemWeight_edittextwidget.setText(String.valueOf(weightinKGString) + "Kg");

                                    }
                                    else{
                                        holder.itemWeight_widget.setText(String.valueOf(item_weight_int) + "");
                                        holder.itemWeight_edittextwidget.setText(String.valueOf(item_weight_int) + "");

                                    }


                                holder.itemPrice_Widget.setText(decimalFormat.format(price));
                                holder.itemprice_edittextwidget.setText(decimalFormat.format(price));

                                holder.itemPrice_Widget.setVisibility(View.VISIBLE);
                                holder.itemprice_edittextwidget.setVisibility(View.INVISIBLE);
                                newOrders_menuItem_fragment.add_amount_ForBillDetails();

                                //Log.e("TAg", "weight item_total" + item_total);

                                //   holder.itemWeight_edittextwidget.setText(String.valueOf(item_total));

                                NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();
                                // onNewDataArrived(itemInCart) ;
                            }
                            }

                        }
                        // notifyDataSetChanged();


                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                return false;

            }
        });



        holder.itemWeight_edittextwidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {

                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                        //do what you want on the press of 'done'

                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        Objects.requireNonNull(imm).hideSoftInputFromWindow(holder.itemWeight_edittextwidget.getWindowToken(), 0);

                        String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(position);

                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                        String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();


                        if (pricetypeforpos.equals("tmcpriceperkg")) {

                            String weight_string = holder.itemWeight_edittextwidget.getText().toString();

                            weight_string = weight_string.replaceAll("[^\\d.]", "");

                            if (weight_string.equals("")) {
                                AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.Weight_cant_be_empty);

                            } else {
                                double weight = Double.parseDouble(weight_string);
                                int weight_in_Int =0;

                                if (isWeightCanBeEdited) {
                                    String grossWeightinKilogramsString = String.valueOf(weight);
                                    String weightinGramsString = convertKiloGramsTograms(grossWeightinKilogramsString);
                                    weight = Double.parseDouble(weightinGramsString);

                                }



                                if (weight == 0) {
                                    AlertDialogClass.showDialog(newOrders_menuItem_fragment.getActivity(), R.string.Weight_cant_be_Zero);

                                } else {
                                modal_newOrderItems.setisWeightEdited(false);
                                modal_newOrderItems.setisWeightEdittextClicked(false);
                                modal_newOrderItems.setIstmcpriceperkgitemedited("TRUE");

                                double priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkg());
                                double itemtotalwithQuantity = 0;

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
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        quantity = 1;
                                    }
                                    itemtotalwithQuantity = 0;
                                    try {

                                        itemtotalwithQuantity = item_total * quantity;
                                        modal_newOrderItems.setItemFinalPrice(String.valueOf(itemtotalwithQuantity));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        modal_newOrderItems.setItemFinalPrice(String.valueOf(item_total));
                                        modal_newOrderItems.setQuantity(String.valueOf(1));

                                    }
                                    modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(item_total));
                                    weight_in_Int = (int) (weight);
                                    modal_newOrderItems.setItemFinalWeight(String.valueOf(weight_in_Int) + "g");
                                    modal_newOrderItems.setGrossweight((String.valueOf(weight_in_Int)) + "g");
                                    if (isWeightCanBeEdited) {

                                        String grossWeightingramsString = String.valueOf(weight);
                                        String weightinKGString = convertGramsToKilograms(grossWeightingramsString);


                                        holder.itemWeight_widget.setText(String.valueOf(weightinKGString) + "Kg");
                                    }
                                    else{
                                        holder.itemWeight_widget.setText(String.valueOf(weight_in_Int) );

                                    }
                                    //Log.e("TAg", "weight item_total" + item_total);

                                    //   holder.itemWeight_edittextwidget.setText(String.valueOf(item_total));


                                }

                                if (weight == 1000) {
                                    int quantity = 0;
                                    try {
                                        quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        quantity = 1;
                                    }
                                    itemtotalwithQuantity = 0;
                                    try {

                                        itemtotalwithQuantity = priceperKg * quantity;
                                        modal_newOrderItems.setItemFinalPrice(String.valueOf(itemtotalwithQuantity));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        modal_newOrderItems.setItemFinalPrice(String.valueOf(priceperKg));
                                        modal_newOrderItems.setQuantity(String.valueOf(1));

                                    }
                                    modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(priceperKg));
                                    weight_in_Int = (int) (weight);
                                    modal_newOrderItems.setItemFinalWeight(String.valueOf(weight_in_Int) + "g");
                                    modal_newOrderItems.setGrossweight((String.valueOf(weight_in_Int)) + "g");

                                    if (isWeightCanBeEdited) {

                                        String grossWeightingramsString = String.valueOf(weight);
                                        String weightinKGString = convertGramsToKilograms(grossWeightingramsString);
                                        holder.itemWeight_widget.setText(String.valueOf(weightinKGString) + "Kg");
                                    }
                                    else{
                                        holder.itemWeight_widget.setText(String.valueOf(weight_in_Int) + "");

                                    }
                                    //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);


                                }

                                if (weight > 1000) {
                                    priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkg());

                                    //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);

                                    //Log.e("TAg", "weight3" + weight);

                                    double itemquantity = weight - 1000;
                                    //Log.e("TAg", "weight itemquantity" + itemquantity);

                                    item_total = (priceperKg * itemquantity) / 1000;
                                    item_total = Double.parseDouble(decimalFormat.format(item_total));


                                    //Log.e("TAg", "weight item_total" + item_total);

                                    double total = priceperKg + item_total;
                                    total = Double.parseDouble(decimalFormat.format((total)));


                                    int quantity = 0;
                                    try {
                                        quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        quantity = 1;
                                    }
                                    itemtotalwithQuantity = 0;
                                    try {

                                        itemtotalwithQuantity = total * quantity;
                                        modal_newOrderItems.setItemFinalPrice(String.valueOf(itemtotalwithQuantity));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        modal_newOrderItems.setItemFinalPrice(String.valueOf(total));
                                        modal_newOrderItems.setQuantity(String.valueOf(1));

                                    }
                                    modal_newOrderItems.setItemPrice_quantityBased(String.valueOf(total));
                                    weight_in_Int = (int) (weight);

                                    modal_newOrderItems.setItemFinalWeight(String.valueOf(weight_in_Int) + "g");
                                    //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                                    modal_newOrderItems.setGrossweight((String.valueOf(weight_in_Int)) + "g");


                                    if (isWeightCanBeEdited) {

                                        String grossWeightingramsString = String.valueOf(weight);
                                        String weightinKGString = convertGramsToKilograms(grossWeightingramsString);
                                        holder.itemWeight_widget.setText(String.valueOf(weightinKGString) + "Kg");
                                    }
                                    else{
                                        holder.itemWeight_widget.setText(String.valueOf(weight_in_Int) + "");
                                      //  newOrders_menuItem_fragment.  sendWeightToHandlerInRecycler(String.valueOf(weight_in_Int));

                                    }

                                    //  holder.itemWeight_edittextwidget.setText(String.valueOf(total));

                                }




                                if(isweightmachineconnected){
                                    newOrders_menuItem_fragment. result_fromWeightMachine = "";
                                    newOrders_menuItem_fragment.currentTimeLongValue = 0;
                                    newOrders_menuItem_fragment.timeLongValueAfter10Sec = 0;
                                    try {
                                        if (newOrders_menuItem_fragment.port.isOpen()) {
                                            newOrders_menuItem_fragment.port.close();
                                        }
                                        newOrders_menuItem_fragment.result_fromWeightMachine = "";
                                        newOrders_menuItem_fragment.currentTimeLongValue = 0;
                                        newOrders_menuItem_fragment.timeLongValueAfter10Sec = 0;
                                        newOrders_menuItem_fragment. isConnectUSBSerialPort = false;
                                        newOrders_menuItem_fragment. usbIoManager.stop();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                                holder.itemWeight_widget.setVisibility(View.VISIBLE);
                                holder.itemWeight_edittextwidget.setVisibility(View.INVISIBLE);

                                holder.itemPrice_Widget.setText(decimalFormat.format(itemtotalwithQuantity));
                                holder.itemprice_edittextwidget.setText(decimalFormat.format(itemtotalwithQuantity));
                                newOrders_menuItem_fragment.add_amount_ForBillDetails();

                                NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();


                            }
                        }
                            // onNewDataArrived(itemInCart);
                        }



                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return false;

            }
        });






    }

    private String convertKiloGramsTograms(String grossWeightinKilogramsString) {
        String weightinGramsString = "";

        try {
            grossWeightinKilogramsString = grossWeightinKilogramsString.replaceAll("[^\\d.]", "");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        double grossweightInKiloGramDouble = 0;
        try{
            grossweightInKiloGramDouble = Double.parseDouble(grossWeightinKilogramsString);
        }
        catch (Exception e){
            grossweightInKiloGramDouble = 0;
            e.printStackTrace();
        }
        if(grossweightInKiloGramDouble >0 ) {
            try {
                double temp = grossweightInKiloGramDouble * 1000;
                // double rf = Math.round((temp * 10.0) / 10.0);
                weightinGramsString = String.valueOf(temp);
            }
            catch (Exception e){
                weightinGramsString = grossWeightinKilogramsString;

                e.printStackTrace();
            }

        }
        else{
            weightinGramsString = grossWeightinKilogramsString;
        }
        return  weightinGramsString;
    }

    private String convertGramsToKilograms(String grossWeightingramsString) {
        String weightinKGString = "";

        try {
            grossWeightingramsString = grossWeightingramsString.replaceAll("[^\\d.]", "");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        float grossweightInGramDouble = 0;
        try{
            grossweightInGramDouble = Float.parseFloat(grossWeightingramsString);
        }
        catch (Exception e){
            grossweightInGramDouble = 0;
            e.printStackTrace();
        }
        if(grossweightInGramDouble >0 ) {
            try {
                float temp = grossweightInGramDouble / 1000;
                // double rf = Math.round((temp * 10.0) / 10.0);
                weightinKGString = String.valueOf(temp);
            }
            catch (Exception e){
                weightinKGString = grossWeightingramsString;

                e.printStackTrace();
            }

        }
        else{
            weightinKGString = grossWeightingramsString;
        }
        return  weightinKGString;
    }


    @Override
    public int getItemCount() {
        return NewOrders_MenuItem_Fragment.cartItem_hashmap.size();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        AutoCompleteTextView autoComplete_widget;


       // LinearLayout edit_weight_layout , tmcUnitprice_weightAdd_layout , tmcUnitprice_weightMinus_layout , edit_price_layout , removeItem_fromCart_widget;
        TextView itemIndex,itemWeight_widget, itemQuantity_widget;

        TextView itemPrice_Widget,gramsTextview;

        EditText itemWeight_edittextwidget,barcode_widget,itemprice_edittextwidget,itemQuantity_edittextwidget;



        ImageView edit_weight_layout,tmcUnitprice_weightAdd_layout , tmcUnitprice_weightMinus_layout,edit_price_layout,removeItem_fromCart_widget,edit_quantity_layout;
        Button addNewItem_layout;
        boolean isTMCproduct = false;
        boolean isIndiaGateBasmatiRiceproduct = false;

        private EditTextListener EditTextListener = new EditTextListener();
      //  WeightEditTextListener WeightEditTextListener = new WeightEditTextListener();
       // EmptyWeightEditTextListener emptyWeightEditTextListener = new EmptyWeightEditTextListener();


        public ViewHolder(View itemView) {
            super(itemView);
            this.autoComplete_widget = itemView.findViewById(R.id.autoComplete_widget);
            this.itemIndex = itemView.findViewById(R.id.item_NO);
            this.barcode_widget = itemView.findViewById(R.id.barcode_widget);

            this.tmcUnitprice_weightAdd_layout = itemView.findViewById(R.id.tmcUnitprice_weightAdd_layout);
            this.tmcUnitprice_weightMinus_layout = itemView.findViewById(R.id.tmcUnitprice_weightMinus_layout);
            this.itemWeight_widget = itemView.findViewById(R.id.itemWeight_widget);
            this.itemQuantity_widget = itemView.findViewById(R.id.itemQuantity_widget);
            this.gramsTextview = itemView.findViewById(R.id.gramsTextview);
            this.itemWeight_edittextwidget = itemView.findViewById(R.id.itemWeight_edittextwidget);
            this.itemprice_edittextwidget = itemView.findViewById(R.id.itemprice_edittextwidget);
            this.edit_price_layout = itemView.findViewById(R.id.edit_price_layout);
            this.itemPrice_Widget = itemView.findViewById(R.id.itemPrice_Widget);
            this.removeItem_fromCart_widget = itemView.findViewById(R.id.removeItem_fromCart_widget);
            this.addNewItem_layout = itemView.findViewById(R.id.addNewItem_layout);
            this.edit_weight_layout = itemView.findViewById(R.id.edit_weight_layout);
            this.itemQuantity_edittextwidget = itemView.findViewById(R.id.itemQuantity_edittextwidget);
            this.edit_quantity_layout = itemView.findViewById(R.id.edit_quantity_layout);
            itemInCart.putAll(NewOrders_MenuItem_Fragment.cartItem_hashmap);


            try{
                SharedPreferences shared = context.getSharedPreferences("VendorLoginData", MODE_PRIVATE);


            }
            catch(Exception e){
                e.printStackTrace();
            }



            if(isweightmachineconnected || !isbarcodescannerconnected) {
                autoComplete_widget.setEnabled(false);
                autoComplete_widget.setHint("");
                autoComplete_widget.setTextColor(Color.BLACK);
            }
            else{
                autoComplete_widget.setEnabled(true);
                adapter = new Adapter_AutoCompleteMenuItem(context, Menulist, getPosition(), completemenuItem);
                adapter.setHandler(newHandler(itemWeight_edittextwidget));

                autoComplete_widget.setAdapter(adapter);

            }
            autoComplete_widget.clearFocus();
         //  itemWeight_edittextwidget.addTextChangedListener(emptyWeightEditTextListener);







            if(vendorType.equals(Constants.WholeSales_VendorType)) {
               // itemWeight_widget.setVisibility(View.GONE);
                //itemWeight_edittextwidget.setVisibility(View.VISIBLE);
                gramsTextview.setVisibility(View.VISIBLE);


            }
            else{
             //   itemWeight_widget.setVisibility(View.VISIBLE);
              //  itemWeight_edittextwidget.setVisibility(View.GONE);
                gramsTextview.setVisibility(View.GONE);

            }




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


                if(isweightmachineconnected || !isbarcodescannerconnected){
                  //  Log.i("WeightData", " Recyclerview 1 : "+s1);

                    /*
                    Thread thread=new Thread(){
                        @Override
                        public void run()
                        {
                            try
                            {
                                if(!isThreadinSleepState) {
                                    sleep(1500);
                                }
                                isThreadinSleepState = true;
                            }
                            catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                            finally
                            {
                                isThreadinSleepState = false;
                                finallyMethodCalledCount  = finallyMethodCalledCount +1;
                                int barcodeCount = 0 ;
                                barcodeCount = barcode_widget.getText().length();

                                if(barcodeCount == finallyMethodCalledCount) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            String Barcode = barcode_widget.getText().toString();
                                            getMenuItemUsingBarCode(Barcode);
                                        }
                                    });
                                }
                                else{
                                    if(finallyMethodCalledCount > barcodeCount){
                                        finallyMethodCalledCount = barcodeCount;
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                String Barcode = barcode_widget.getText().toString();
                                                getMenuItemUsingBarCode(Barcode);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    };
                    thread.start();


                     */




                    if(s1.length()>=2) {
                        String Barcode = barcode_widget.getText().toString();
                        getMenuItemUsingBarCode(Barcode);
                    }
                    }
                else{
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

                                // Log.e("TAG", "Got barcode " + barcode_widget.getText().length());

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

                            // Log.e(TAG, "Got barcode " + barcode_widget.getText().length());

                            String Barcode = barcode_widget.getText().toString();

                            getMenuItemUsingBarCode(Barcode);
                            // }
                        }


                    }

                }


            }
        }


        class WeightEditTextListener implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                String barcode = NewOrders_MenuItem_Fragment.cart_Item_List.get(getAdapterPosition());

                Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(barcode);
                String pricetypeforpos = modal_newOrderItems.getPricetypeforpos().toString();
                if(pricetypeforpos.equals("tmcpriceperkg")) {
                    if(modal_newOrderItems.getisWeightEdittextClicked()) {
                        modal_newOrderItems.setisWeightEdited(true);


                    }

                }

            }
        }

         class EmptyWeightEditTextListener implements TextWatcher {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                   gramsTextview.setVisibility(View.GONE);

            }
        }


        public void getMenuItemUsingBarCode(String barcode) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            //Log.e(TAG, "barcode  1    " + barcode);
            if(isweightmachineconnected || !isbarcodescannerconnected){
                try{
                    for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {
                        String itemWeight ="0";
                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                        if (String.valueOf(modal_newOrderItems.getItemuniquecode()).equals(barcode)) {

                            Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                            newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                            newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                            newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                            newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                            newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                            newItem_newOrdersPojoClass.itemuniquecode = (modal_newOrderItems.getItemuniquecode());
                            newItem_newOrdersPojoClass.menuItemId = (modal_newOrderItems.getMenuItemId());
                            newItem_newOrdersPojoClass.keyforHashmap = barcode;
                            newItem_newOrdersPojoClass.key = modal_newOrderItems.getKey();
                            newItem_newOrdersPojoClass.pricetypeforpos = (modal_newOrderItems.getPricetypeforpos());

                            newItem_newOrdersPojoClass.itemavailability = (modal_newOrderItems.getItemavailability());
                            try {

                                newItem_newOrdersPojoClass.dunzoprice = (modal_newOrderItems.getDunzoprice());

                            } catch (Exception e) {
                                newItem_newOrdersPojoClass.dunzoprice = "";
                                e.printStackTrace();
                            }

                            try {
                                newItem_newOrdersPojoClass.bigbasketprice = (modal_newOrderItems.getBigbasketprice());


                            } catch (Exception e) {
                                newItem_newOrdersPojoClass.bigbasketprice = "";
                                e.printStackTrace();
                            }


                            try {
                                newItem_newOrdersPojoClass.swiggyprice = (modal_newOrderItems.getSwiggyprice());


                            } catch (Exception e) {
                                newItem_newOrdersPojoClass.swiggyprice = "";
                                e.printStackTrace();
                            }

                            newItem_newOrdersPojoClass.allownegativestock = String.valueOf(modal_newOrderItems.getAllownegativestock());
                            newItem_newOrdersPojoClass.barcode_AvlDetails = String.valueOf(modal_newOrderItems.getBarcode_AvlDetails());
                            newItem_newOrdersPojoClass.itemavailability_AvlDetails = String.valueOf(modal_newOrderItems.getItemavailability_AvlDetails());
                            newItem_newOrdersPojoClass.key_AvlDetails = String.valueOf(modal_newOrderItems.getKey_AvlDetails());
                            newItem_newOrdersPojoClass.lastupdatedtime_AvlDetails = String.valueOf(modal_newOrderItems.getLastupdatedtime_AvlDetails());
                            newItem_newOrdersPojoClass.menuitemkey_AvlDetails = String.valueOf(modal_newOrderItems.getMenuitemkey_AvlDetails());
                            newItem_newOrdersPojoClass.receivedstock_AvlDetails = String.valueOf(modal_newOrderItems.getReceivedstock_AvlDetails());

                            newItem_newOrdersPojoClass.stockbalance_AvlDetails = String.valueOf(modal_newOrderItems.getStockbalance_AvlDetails());
                            newItem_newOrdersPojoClass.stockincomingkey_AvlDetails = String.valueOf(modal_newOrderItems.getStockincomingkey_AvlDetails());
                            newItem_newOrdersPojoClass.vendorkey_AvlDetails = String.valueOf(modal_newOrderItems.getVendorkey_AvlDetails());

                            newItem_newOrdersPojoClass.barcode = String.valueOf(modal_newOrderItems.getBarcode());
                            newItem_newOrdersPojoClass.tmcctgykey = String.valueOf(modal_newOrderItems.getTmcctgykey());



                            try {
                                if (modal_newOrderItems.getInventorydetails().equals("")) {
                                    newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(""));

                                } else {
                                    newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(modal_newOrderItems.getInventorydetails()));
                                }
                            } catch (Exception e) {

                                newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(""));


                                e.printStackTrace();
                            }



                            try {
                                if (modal_newOrderItems.getApplieddiscountpercentage().equals("")) {
                                    newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));

                                } else {
                                    newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf(modal_newOrderItems.getApplieddiscountpercentage()));
                                }
                            } catch (Exception e) {

                                newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));


                                e.printStackTrace();
                            }
                            try {
                                if (modal_newOrderItems.getAppmarkuppercentage().equals("")) {
                                    newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf("0"));
                                    Toast.makeText(context, "There is no appmarkuppercentage entry on Recycler 2 ", Toast.LENGTH_LONG).show();

                                } else {
                                    newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                }
                            } catch (Exception e) {
                                newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf("0"));
                                Toast.makeText(context, "Error in getting appmarkuppercentage entry on Recycler 2 ", Toast.LENGTH_LONG).show();

                                e.printStackTrace();
                            }


                            String tmcsubctgykey = (String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                            try {
                                if (modal_newOrderItems.getTmcsubctgykey().equals("")) {
                                    newItem_newOrdersPojoClass.tmcsubctgykey = ("0");

                                } else {
                                    newItem_newOrdersPojoClass.setTmcsubctgykey(String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            newItem_newOrdersPojoClass.quantity = "1";
                            newItem_newOrdersPojoClass.subTotal_perItem = "";
                            newItem_newOrdersPojoClass.total_of_subTotal_perItem = "";
                            newItem_newOrdersPojoClass.totalGstAmount = "";
                            newItem_newOrdersPojoClass.itemweightdetails = (String.valueOf(((modal_newOrderItems.getItemweightdetails()))));
                            newItem_newOrdersPojoClass.itemcutdetails = (String.valueOf(((modal_newOrderItems.getItemcutdetails()))));
                            newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(((modal_newOrderItems.getInventorydetails()))));

                            if (newOrders_menuItem_fragment.isPhoneOrderSelected) {

                                String priceperkgmarkupvalue = String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue());
                                double priceperkgMarkup_double = 0 , priceperkgNormal_double = 0;
                                String priceperkgNormal_string ="0";
                                try {
                                    priceperkgmarkupvalue = priceperkgmarkupvalue.replaceAll("[^\\d.]", "");
                                    priceperkgMarkup_double = Double.parseDouble(priceperkgmarkupvalue);
                                }
                                catch (Exception e){
                                    priceperkgMarkup_double  = 0;
                                    e.printStackTrace();
                                }

                                try {
                                    priceperkgNormal_string  = String.valueOf(modal_newOrderItems.getTmcpriceperkg());

                                    priceperkgNormal_string = priceperkgNormal_string.replaceAll("[^\\d.]", "");
                                    priceperkgNormal_double = Double.parseDouble(priceperkgNormal_string);
                                }
                                catch (Exception e){
                                    priceperkgNormal_double  = 0;
                                    e.printStackTrace();
                                }
                              //  if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))) {

                                int markupPercentageInt = 0;

                                try{
                                    markupPercentageInt =   Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                if(markupPercentageInt>0) {
                                    if (priceperkgMarkup_double > 0 && priceperkgMarkup_double > priceperkgNormal_double ) {
                                        newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkgWithMarkupValue();

                                    }
                                    else {
                                        String tmcPriceperKgWithMarkup_calcNow = CalculateTmcPricePerKgValueWithMarkup(modal_newOrderItems);
                                        newItem_newOrdersPojoClass.tmcpriceperkg = String.valueOf(tmcPriceperKgWithMarkup_calcNow);
                                        modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(tmcPriceperKgWithMarkup_calcNow));
                                    }
                                }
                                else{
                                    newItem_newOrdersPojoClass.tmcpriceperkg = String.valueOf(modal_newOrderItems.getTmcpriceperkg());
                                    modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(modal_newOrderItems.getTmcpriceperkg()));

                                }
                                String pricemarkupvalue = String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue());
                                double priceMarkup_double = 0 ,priceNormal_double = 0;
                                String priceNormal_string ="0";
                                try {
                                    pricemarkupvalue = pricemarkupvalue.replaceAll("[^\\d.]", "");
                                    priceMarkup_double = Double.parseDouble(pricemarkupvalue);
                                }
                                catch (Exception e){
                                    priceMarkup_double  = 0;
                                    e.printStackTrace();
                                }



                                try {
                                    priceNormal_string  = String.valueOf(modal_newOrderItems.getTmcprice());

                                    priceNormal_string = priceNormal_string.replaceAll("[^\\d.]", "");
                                    priceNormal_double = Double.parseDouble(priceNormal_string);
                                }
                                catch (Exception e){
                                    priceNormal_double  = 0;
                                    e.printStackTrace();
                                }
                                //  if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))) {

                                 markupPercentageInt = 0;

                                try{
                                    markupPercentageInt =   Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                                if(markupPercentageInt>0) {

                                    // if ((!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals(""))) {

                                    if (priceMarkup_double > 0 && priceMarkup_double > priceNormal_double ) {
                                        newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcpriceWithMarkupValue();

                                    }
                                    else {
                                        String tmcPriceWithMarkup_calcNow = CalculateTmcPriceValueWithMarkup(modal_newOrderItems);
                                        newItem_newOrdersPojoClass.tmcprice = String.valueOf(tmcPriceWithMarkup_calcNow);
                                        modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(tmcPriceWithMarkup_calcNow));

                                    }

                                }
                                else{
                                    newItem_newOrdersPojoClass.tmcprice = String.valueOf(modal_newOrderItems.getTmcprice());
                                    modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(modal_newOrderItems.getTmcprice()));

                                }
                            }
                            else {
                                newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                                newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();

                            }

                            try {

                                itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).toLowerCase().equals("tmcpriceperkg")) {
                                double price = 0;

                                if (newOrders_menuItem_fragment.isPhoneOrderSelected) {
                                    price = Double.parseDouble(modal_newOrderItems.getTmcpriceWithMarkupValue());

                                } else {
                                    price = Double.parseDouble(modal_newOrderItems.getTmcprice());

                                }



                               // NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();
                                if (isweightmachineconnected) {

                                    newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf("0"));
                                    newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf("0"));
                                    newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf("0") + "g");
                                    newItem_newOrdersPojoClass.setGrossweight((String.valueOf("0") + "g"));


                                    try {
                                        newItem_newOrdersPojoClass.netweight = ("0");


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        newItem_newOrdersPojoClass.portionsize = ("0");


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                else{

                                    newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(price));
                                    newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(price));



                                    try {
                                        newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        newItem_newOrdersPojoClass.grossweight = (modal_newOrderItems.getGrossweight());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        newItem_newOrdersPojoClass.netweight = (modal_newOrderItems.getNetweight());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        newItem_newOrdersPojoClass.portionsize = (modal_newOrderItems.getPortionsize());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }


                                try {
                                    boolean IsItemAlreadyAddedinCart = checkforBarcodeInCart(barcode);
                                    if(!IsItemAlreadyAddedinCart) {
                                        if (isweightmachineconnected) {
                                            itemPrice_Widget.setText(String.valueOf("0"));
                                            newItem_newOrdersPojoClass.setisWeightEdittextClicked(true);
                                            newItem_newOrdersPojoClass.setisWeightEdited(true);
                                            itemWeight_edittextwidget.setText("");
                                            newOrders_menuItem_fragment.connectUSBSerialPort();
                                            newOrders_menuItem_fragment.setHandlerForReceivingWeight(newHandler(itemWeight_edittextwidget));
                                        }
                                        else{
                                            itemPrice_Widget.setText(String.valueOf(price));
                                            newItem_newOrdersPojoClass.setisWeightEdittextClicked(false);
                                            newItem_newOrdersPojoClass.setisWeightEdited(false);
                                            itemWeight_edittextwidget.setText(itemWeight);
                                            itemWeight_widget.setText(itemWeight);
                                        }
                                    }


                                } catch (Exception e) {
                                    Toast.makeText(context, "Error in boolean IsItemAlreadyAddedin Cart", Toast.LENGTH_LONG).show();

                                }


                            }


                            if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).toLowerCase().equals("tmcprice")) {


                                if (newOrders_menuItem_fragment.isPhoneOrderSelected) {
                                    newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()));

                                    newItem_newOrdersPojoClass.setItemFinalPrice(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getTmcpriceWithMarkupValue())));

                                } else {
                                    newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(modal_newOrderItems.getTmcprice()));

                                    newItem_newOrdersPojoClass.setItemFinalPrice(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getTmcprice())));

                                }





                                try {
                                    newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    newItem_newOrdersPojoClass.grossweight = (modal_newOrderItems.getGrossweight());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    newItem_newOrdersPojoClass.netweight = (modal_newOrderItems.getNetweight());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    newItem_newOrdersPojoClass.portionsize = (modal_newOrderItems.getPortionsize());


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                newItem_newOrdersPojoClass.setisWeightEdittextClicked(false);
                                newItem_newOrdersPojoClass.setisWeightEdited(false);

                            }

                            addItemIntheCart(barcode, newItem_newOrdersPojoClass, itemWeight, modal_newOrderItems.getItemuniquecode());





                        }
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            else {
                if (!isTMCproduct) {

                    // Log.e(TAG, " barcode  1   " + barcode);

                    for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {
                        String itemWeight = "";
                        //Log.e(TAG, " barcode  1  for" + barcode);

                        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                        if ((String.valueOf(modal_newOrderItems.getBarcode())).equals(barcode)) {

                            try {
                                Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                                newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                                newItem_newOrdersPojoClass.grossweight = modal_newOrderItems.getGrossweight();
                                newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                                // newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                                // newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                                newItem_newOrdersPojoClass.menuItemId = (modal_newOrderItems.getMenuItemId());

                                newItem_newOrdersPojoClass.barcode = (modal_newOrderItems.getBarcode());
                                newItem_newOrdersPojoClass.key = modal_newOrderItems.getKey();
                                newItem_newOrdersPojoClass.itemavailability = (modal_newOrderItems.getItemavailability());

                                newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                                newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                                newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                                newItem_newOrdersPojoClass.itemuniquecode = modal_newOrderItems.getItemuniquecode();
                                newItem_newOrdersPojoClass.keyforHashmap = barcode;

                                newItem_newOrdersPojoClass.barcode_AvlDetails = String.valueOf(modal_newOrderItems.getBarcode_AvlDetails());
                                newItem_newOrdersPojoClass.itemavailability_AvlDetails = String.valueOf(modal_newOrderItems.getItemavailability_AvlDetails());
                                newItem_newOrdersPojoClass.key_AvlDetails = String.valueOf(modal_newOrderItems.getKey_AvlDetails());
                                newItem_newOrdersPojoClass.lastupdatedtime_AvlDetails = String.valueOf(modal_newOrderItems.getLastupdatedtime_AvlDetails());
                                newItem_newOrdersPojoClass.menuitemkey_AvlDetails = String.valueOf(modal_newOrderItems.getMenuitemkey_AvlDetails());
                                newItem_newOrdersPojoClass.receivedstock_AvlDetails = String.valueOf(modal_newOrderItems.getReceivedstock_AvlDetails());
                                newItem_newOrdersPojoClass.allownegativestock = String.valueOf(modal_newOrderItems.getAllownegativestock());

                                newItem_newOrdersPojoClass.stockbalance_AvlDetails = String.valueOf(modal_newOrderItems.getStockbalance_AvlDetails());
                                newItem_newOrdersPojoClass.stockincomingkey_AvlDetails = String.valueOf(modal_newOrderItems.getStockincomingkey_AvlDetails());
                                newItem_newOrdersPojoClass.vendorkey_AvlDetails = String.valueOf(modal_newOrderItems.getVendorkey_AvlDetails());
                                try {

                                    newItem_newOrdersPojoClass.dunzoprice = (modal_newOrderItems.getDunzoprice());

                                } catch (Exception e) {
                                    newItem_newOrdersPojoClass.dunzoprice = "";
                                    e.printStackTrace();
                                }

                                try {
                                    newItem_newOrdersPojoClass.bigbasketprice = (modal_newOrderItems.getBigbasketprice());


                                } catch (Exception e) {
                                    newItem_newOrdersPojoClass.bigbasketprice = "";
                                    e.printStackTrace();
                                }


                                try {
                                    newItem_newOrdersPojoClass.swiggyprice = (modal_newOrderItems.getSwiggyprice());


                                } catch (Exception e) {
                                    newItem_newOrdersPojoClass.swiggyprice = "";
                                    e.printStackTrace();
                                }


                                newItem_newOrdersPojoClass.barcode = String.valueOf(modal_newOrderItems.getBarcode());
                                newItem_newOrdersPojoClass.tmcctgykey = String.valueOf(modal_newOrderItems.getTmcctgykey());

                                try {
                                    if (modal_newOrderItems.getApplieddiscountpercentage().equals("")) {
                                        newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));

                                    } else {
                                        newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf(modal_newOrderItems.getApplieddiscountpercentage()));
                                    }
                                } catch (Exception e) {
                                    newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));

                                    e.printStackTrace();
                                }
                                try {
                                    if (modal_newOrderItems.getAppmarkuppercentage().equals("")) {
                                        newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf("0"));
                                        Toast.makeText(context, "There is no appmarkuppercentage entry on Recycler", Toast.LENGTH_LONG).show();

                                    } else {
                                        newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));

                                    }
                                } catch (Exception e) {
                                    newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf("0"));
                                    Toast.makeText(context, "Error in getting appmarkuppercentage entry on Recycler", Toast.LENGTH_LONG).show();

                                    e.printStackTrace();
                                }

                                try {
                                    if (modal_newOrderItems.getTmcsubctgykey().equals("")) {
                                        newItem_newOrdersPojoClass.tmcsubctgykey = "0";

                                    } else {
                                        newItem_newOrdersPojoClass.tmcsubctgykey = (String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                if (NewOrders_MenuItem_Fragment.isPhoneOrderSelected) {
                                    String priceperkgmarkupvalue = String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue());
                                    double priceperkgMarkup_double = 0 ,priceperkgNormal_double = 0;
                                    String priceperkgNormal_string ="0";
                                    try {
                                        priceperkgmarkupvalue = priceperkgmarkupvalue.replaceAll("[^\\d.]", "");
                                        priceperkgMarkup_double = Double.parseDouble(priceperkgmarkupvalue);
                                    }
                                    catch (Exception e){
                                        priceperkgMarkup_double  = 0;
                                        e.printStackTrace();
                                    }


                                    try {
                                        priceperkgNormal_string  = String.valueOf(modal_newOrderItems.getTmcpriceperkg());

                                        priceperkgNormal_string = priceperkgNormal_string.replaceAll("[^\\d.]", "");
                                        priceperkgNormal_double = Double.parseDouble(priceperkgNormal_string);
                                    }
                                    catch (Exception e){
                                        priceperkgNormal_double  = 0;
                                        e.printStackTrace();
                                    }
                                    //  if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))) {

                                    int markupPercentageInt = 0;

                                    try{
                                        markupPercentageInt =   Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if(markupPercentageInt>0) {

                                        if (priceperkgMarkup_double > 0 && priceperkgMarkup_double > priceperkgNormal_double ) {
                                            newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkgWithMarkupValue();

                                        }
                                        else {
                                            String tmcPriceperKgWithMarkup_calcNow = CalculateTmcPricePerKgValueWithMarkup(modal_newOrderItems);
                                            newItem_newOrdersPojoClass.tmcpriceperkg = String.valueOf(tmcPriceperKgWithMarkup_calcNow);
                                            modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(tmcPriceperKgWithMarkup_calcNow));
                                        }
                                    }
                                    else{
                                        newItem_newOrdersPojoClass.tmcpriceperkg = String.valueOf(modal_newOrderItems.getTmcpriceperkg());
                                        modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(modal_newOrderItems.getTmcpriceperkg()));

                                    }
                                    String  pricemarkupvalue = String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue());
                                    double  priceMarkup_double = 0,priceNormal_double = 0;
                                    String priceNormal_string ="0";
                                    try {
                                        pricemarkupvalue = pricemarkupvalue.replaceAll("[^\\d.]", "");
                                        priceMarkup_double = Double.parseDouble(pricemarkupvalue);
                                    }
                                    catch (Exception e){
                                        priceMarkup_double  = 0;
                                        e.printStackTrace();
                                    }



                                    try {
                                        priceNormal_string  = String.valueOf(modal_newOrderItems.getTmcprice());

                                        priceNormal_string = priceNormal_string.replaceAll("[^\\d.]", "");
                                        priceNormal_double = Double.parseDouble(priceNormal_string);
                                    }
                                    catch (Exception e){
                                        priceNormal_double  = 0;
                                        e.printStackTrace();
                                    }
                                    //  if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))) {

                                    markupPercentageInt = 0;

                                    try{
                                        markupPercentageInt =   Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    if(markupPercentageInt>0) {
                                        // newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkgWithMarkupValue();
                                        //if ((!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals(""))) {
                                        if (priceMarkup_double > 0 && priceMarkup_double>priceNormal_double) {
                                            newItem_newOrdersPojoClass.tmcprice = String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue());
                                            newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((modal_newOrderItems.getTmcpriceWithMarkupValue()))));
                                            newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf(((modal_newOrderItems.getTmcpriceWithMarkupValue()))));


                                        } else {
                                            String tmcPriceWithMarkup_calcNow = CalculateTmcPriceValueWithMarkup(modal_newOrderItems);
                                            newItem_newOrdersPojoClass.tmcprice = String.valueOf(tmcPriceWithMarkup_calcNow);
                                            newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((tmcPriceWithMarkup_calcNow))));
                                            newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf((tmcPriceWithMarkup_calcNow)));
                                            modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(tmcPriceWithMarkup_calcNow));
                                        }

                                    }
                                    else{
                                        newItem_newOrdersPojoClass.tmcprice = String.valueOf(modal_newOrderItems.getTmcprice());
                                        newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((modal_newOrderItems.getTmcprice()))));
                                        newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf(((modal_newOrderItems.getTmcprice()))));
                                        newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();

                                    }
                                }

                                else {
                                    newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                                    newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                                    newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((modal_newOrderItems.getTmcprice()))));
                                    newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf(((modal_newOrderItems.getTmcprice()))));


                                }


                                newItem_newOrdersPojoClass.quantity = "1";
                                newItem_newOrdersPojoClass.subTotal_perItem = "";
                                newItem_newOrdersPojoClass.total_of_subTotal_perItem = "";
                                newItem_newOrdersPojoClass.totalGstAmount = "";

                                try {
                                    newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());

                                    itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    newItem_newOrdersPojoClass.grossweight = (modal_newOrderItems.getGrossweight());

                                    itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    newItem_newOrdersPojoClass.netweight = (modal_newOrderItems.getNetweight());

                                    itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    newItem_newOrdersPojoClass.portionsize = (modal_newOrderItems.getPortionsize());

                                    itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                newItem_newOrdersPojoClass.itemweightdetails = (String.valueOf(((modal_newOrderItems.getItemFinalWeight()))));
                                newItem_newOrdersPojoClass.itemcutdetails = (String.valueOf(((modal_newOrderItems.getItemcutdetails()))));
                                newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(((modal_newOrderItems.getInventorydetails()))));


                                addItemIntheCart(barcode, newItem_newOrdersPojoClass, itemWeight, modal_newOrderItems.getItemuniquecode());

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Error in get MenuItem using Barcode ", Toast.LENGTH_LONG).show();

                            }
                        }


                    }
                }
                else {
                    //Log.e(TAG, " barcode  2" + barcode);

                    if (barcode.length() == 14) {
                        // Log.e(TAG, " barcode  3" + barcode);
                        try {
                            String itemuniquecode = barcode.substring(0, 9);
                            String itemWeight = barcode.substring(9, 14);
                            //Log.e(TAG, "1 barcode uniquecode" + itemuniquecode);
                            //Log.e(TAG, "1 barcode itemweight" + itemWeight);

                            for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {

                                Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                                if (String.valueOf(modal_newOrderItems.getBarcode()).equals(itemuniquecode)) {

                                    Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                                    newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                                    newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                                    newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                                    newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                                    newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                                    newItem_newOrdersPojoClass.itemuniquecode = (modal_newOrderItems.getItemuniquecode());
                                    newItem_newOrdersPojoClass.menuItemId = (modal_newOrderItems.getMenuItemId());
                                    newItem_newOrdersPojoClass.keyforHashmap = barcode;
                                    newItem_newOrdersPojoClass.key = modal_newOrderItems.getKey();
                                    newItem_newOrdersPojoClass.itemavailability = (modal_newOrderItems.getItemavailability());
                                    try {

                                        newItem_newOrdersPojoClass.dunzoprice = (modal_newOrderItems.getDunzoprice());

                                    } catch (Exception e) {
                                        newItem_newOrdersPojoClass.dunzoprice = "";
                                        e.printStackTrace();
                                    }

                                    try {
                                        newItem_newOrdersPojoClass.bigbasketprice = (modal_newOrderItems.getBigbasketprice());


                                    } catch (Exception e) {
                                        newItem_newOrdersPojoClass.bigbasketprice = "";
                                        e.printStackTrace();
                                    }


                                    try {
                                        newItem_newOrdersPojoClass.swiggyprice = (modal_newOrderItems.getSwiggyprice());


                                    } catch (Exception e) {
                                        newItem_newOrdersPojoClass.swiggyprice = "";
                                        e.printStackTrace();
                                    }

                                    newItem_newOrdersPojoClass.allownegativestock = String.valueOf(modal_newOrderItems.getAllownegativestock());
                                    newItem_newOrdersPojoClass.barcode_AvlDetails = String.valueOf(modal_newOrderItems.getBarcode_AvlDetails());
                                    newItem_newOrdersPojoClass.itemavailability_AvlDetails = String.valueOf(modal_newOrderItems.getItemavailability_AvlDetails());
                                    newItem_newOrdersPojoClass.key_AvlDetails = String.valueOf(modal_newOrderItems.getKey_AvlDetails());
                                    newItem_newOrdersPojoClass.lastupdatedtime_AvlDetails = String.valueOf(modal_newOrderItems.getLastupdatedtime_AvlDetails());
                                    newItem_newOrdersPojoClass.menuitemkey_AvlDetails = String.valueOf(modal_newOrderItems.getMenuitemkey_AvlDetails());
                                    newItem_newOrdersPojoClass.receivedstock_AvlDetails = String.valueOf(modal_newOrderItems.getReceivedstock_AvlDetails());

                                    newItem_newOrdersPojoClass.stockbalance_AvlDetails = String.valueOf(modal_newOrderItems.getStockbalance_AvlDetails());
                                    newItem_newOrdersPojoClass.stockincomingkey_AvlDetails = String.valueOf(modal_newOrderItems.getStockincomingkey_AvlDetails());
                                    newItem_newOrdersPojoClass.vendorkey_AvlDetails = String.valueOf(modal_newOrderItems.getVendorkey_AvlDetails());

                                    newItem_newOrdersPojoClass.barcode = String.valueOf(modal_newOrderItems.getBarcode());
                                    newItem_newOrdersPojoClass.tmcctgykey = String.valueOf(modal_newOrderItems.getTmcctgykey());


                                    try {
                                        if (modal_newOrderItems.getApplieddiscountpercentage().equals("")) {
                                            newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));

                                        } else {
                                            newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf(modal_newOrderItems.getApplieddiscountpercentage()));
                                        }
                                    } catch (Exception e) {

                                        newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));


                                        e.printStackTrace();
                                    }
                                    try {
                                        if (modal_newOrderItems.getAppmarkuppercentage().equals("")) {
                                            newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf("0"));
                                            Toast.makeText(context, "There is no appmarkuppercentage entry on Recycler 2 ", Toast.LENGTH_LONG).show();

                                        } else {
                                            newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                        }
                                    } catch (Exception e) {
                                        newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf("0"));
                                        Toast.makeText(context, "Error in getting appmarkuppercentage entry on Recycler 2 ", Toast.LENGTH_LONG).show();

                                        e.printStackTrace();
                                    }


                                    String tmcsubctgykey = (String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                                    try {
                                        if (modal_newOrderItems.getTmcsubctgykey().equals("")) {
                                            newItem_newOrdersPojoClass.tmcsubctgykey = ("0");

                                        } else {
                                            newItem_newOrdersPojoClass.setTmcsubctgykey(String.valueOf(((modal_newOrderItems.getTmcsubctgykey()))));
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    newItem_newOrdersPojoClass.quantity = "1";
                                    newItem_newOrdersPojoClass.subTotal_perItem = "";
                                    newItem_newOrdersPojoClass.total_of_subTotal_perItem = "";
                                    newItem_newOrdersPojoClass.totalGstAmount = "";
                                    newItem_newOrdersPojoClass.itemweightdetails = (String.valueOf(((modal_newOrderItems.getItemweightdetails()))));
                                    newItem_newOrdersPojoClass.itemcutdetails = (String.valueOf(((modal_newOrderItems.getItemcutdetails()))));
                                    newItem_newOrdersPojoClass.inventorydetails = (String.valueOf(((modal_newOrderItems.getInventorydetails()))));

                                    if (newOrders_menuItem_fragment.isPhoneOrderSelected) {

                                        String priceperkgmarkupvalue = String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue());
                                        double priceperkgMarkup_double = 0 ,priceperkgNormal_double = 0;
                                        String priceperkgNormal_string ="0";
                                        try {
                                            priceperkgmarkupvalue = priceperkgmarkupvalue.replaceAll("[^\\d.]", "");
                                            priceperkgMarkup_double = Double.parseDouble(priceperkgmarkupvalue);
                                        }
                                        catch (Exception e){
                                            priceperkgMarkup_double  = 0;
                                            e.printStackTrace();
                                        }

                                        try {
                                            priceperkgNormal_string  = String.valueOf(modal_newOrderItems.getTmcpriceperkg());

                                            priceperkgNormal_string = priceperkgNormal_string.replaceAll("[^\\d.]", "");
                                            priceperkgNormal_double = Double.parseDouble(priceperkgNormal_string);
                                        }
                                        catch (Exception e){
                                            priceperkgNormal_double  = 0;
                                            e.printStackTrace();
                                        }
                                        //  if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))) {

                                        int markupPercentageInt = 0;

                                        try{
                                            markupPercentageInt =   Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        if(markupPercentageInt>0) {
                                            if (priceperkgMarkup_double > 0 && priceperkgMarkup_double > priceperkgNormal_double) {


                                                newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkgWithMarkupValue();

                                            } else {
                                                String tmcPriceperKgWithMarkup_calcNow = CalculateTmcPricePerKgValueWithMarkup(modal_newOrderItems);
                                                newItem_newOrdersPojoClass.tmcpriceperkg = String.valueOf(tmcPriceperKgWithMarkup_calcNow);
                                                modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(tmcPriceperKgWithMarkup_calcNow));
                                            }
                                        }
                                        else{
                                            newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                                            modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(modal_newOrderItems.getTmcpriceperkg()));

                                        }

                                        String  pricemarkupvalue = String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue());
                                        double  priceMarkup_double = 0,priceNormal_double = 0;
                                        String priceNormal_string ="0";
                                        try {
                                            pricemarkupvalue = pricemarkupvalue.replaceAll("[^\\d.]", "");
                                            priceMarkup_double = Double.parseDouble(pricemarkupvalue);
                                        }
                                        catch (Exception e){
                                            priceMarkup_double  = 0;
                                            e.printStackTrace();
                                        }



                                        try {
                                            priceNormal_string  = String.valueOf(modal_newOrderItems.getTmcprice());

                                            priceNormal_string = priceNormal_string.replaceAll("[^\\d.]", "");
                                            priceNormal_double = Double.parseDouble(priceNormal_string);
                                        }
                                        catch (Exception e){
                                            priceNormal_double  = 0;
                                            e.printStackTrace();
                                        }
                                        //  if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))) {

                                        markupPercentageInt = 0;

                                        try{
                                            markupPercentageInt =   Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        if(markupPercentageInt>0) {

                                            // if ((!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals(""))) {

                                            if (priceMarkup_double > 0 && priceMarkup_double > priceNormal_double) {
                                                newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcpriceWithMarkupValue();

                                            } else {
                                                String tmcPriceWithMarkup_calcNow = CalculateTmcPriceValueWithMarkup(modal_newOrderItems);
                                                newItem_newOrdersPojoClass.tmcprice = String.valueOf(tmcPriceWithMarkup_calcNow);
                                                modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(tmcPriceWithMarkup_calcNow));

                                            }
                                        }
                                        else{
                                            newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                                            modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(modal_newOrderItems.getTmcprice()));

                                        }

                                    }
                                    else {
                                        newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                                        newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();

                                    }

                                    if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).toLowerCase().equals("tmcpriceperkg")) {
                                        double priceperKg = 0;

                                        if (newOrders_menuItem_fragment.isPhoneOrderSelected) {
                                            priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkgWithMarkupValue());

                                        } else {
                                            priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkg());

                                        }


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

                                            //onNewDataArrived(itemInCart);
                                            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                                        }

                                        if (weight == 1000) {
                                            newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(priceperKg));
                                            newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(priceperKg));
                                            newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                            newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);


                                            // onNewDataArrived(itemInCart);

                                            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                                        }

                                        if (weight > 1000) {
                                            priceperKg = 0;

                                            if (newOrders_menuItem_fragment.isPhoneOrderSelected) {
                                                priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkgWithMarkupValue());

                                            } else {
                                                priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkg());

                                            }
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
                                            //  onNewDataArrived(itemInCart);

                                            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                                        }


                                    }


                                    if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).toLowerCase().equals("tmcprice")) {


                                        if (newOrders_menuItem_fragment.isPhoneOrderSelected) {
                                            newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()));

                                            newItem_newOrdersPojoClass.setItemFinalPrice(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getTmcpriceWithMarkupValue())));

                                        } else {
                                            newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(modal_newOrderItems.getTmcprice()));

                                            newItem_newOrdersPojoClass.setItemFinalPrice(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getTmcprice())));

                                        }


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
                                        try {
                                            newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());

                                            itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            newItem_newOrdersPojoClass.grossweight = (modal_newOrderItems.getGrossweight());

                                            itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            newItem_newOrdersPojoClass.netweight = (modal_newOrderItems.getNetweight());

                                            itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            newItem_newOrdersPojoClass.portionsize = (modal_newOrderItems.getPortionsize());

                                            itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }


                                    addItemIntheCart(barcode, newItem_newOrdersPojoClass, itemWeight, modal_newOrderItems.getItemuniquecode());


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error in get MenuItem using TMC Barcode", Toast.LENGTH_LONG).show();

                        }
                    }

                }
            }
            }


        private void addItemIntheCart(String barcode, Modal_NewOrderItems newItem_newOrdersPojoClass, String itemWeight, String itemUniquecode) {
            if(isListRecycledAfterItemAdded) {
                isListRecycledAfterItemAdded = false;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                boolean IsItemAlreadyAddedinCart = false;
                String pricetypeOfItem = "";
                try {
                    pricetypeOfItem = newItem_newOrdersPojoClass.getPricetypeforpos().toString().toUpperCase();
                } catch (Exception e) {
                    Toast.makeText(context, "Error in boolean pricetypeOfItem 1  ", Toast.LENGTH_LONG).show();

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
                        // onNewDataArrived(itemInCart);
                        NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                    } else {


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
                        // onNewDataArrived(itemInCart);

                        NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                    }
                }
                catch (Exception e) {
                    Toast.makeText(context, "Error in boolean pricetypeOfItem 2 ", Toast.LENGTH_LONG).show();

                }
            }

        }






        public Handler newHandler(EditText itemWeight_edittextwidgetValue) {
            Handler.Callback callback = new Handler.Callback() {

                @Override
                public boolean handleMessage(Message msg) {
                    Bundle bundle = msg.getData();
                    String response_from = bundle.getString("from");

                    if (String.valueOf(response_from).equalsIgnoreCase("autoCompleteMenuItem")) {

                        String data = bundle.getString("dropdown");
                        String pricetype = bundle.getString("pricetype");

                        if (String.valueOf(data).equalsIgnoreCase("dismissdropdown")) {
                            //Log.e(TAG, "dismissDropdown");
                            //Log.e(Constants.TAG, "createBillDetails in CartItem 0 ");

                            sendHandlerMessage("addBillDetails");


                            autoComplete_widget.clearFocus();

                            autoComplete_widget.dismissDropDown();
                            if (String.valueOf(pricetype).equalsIgnoreCase("tmcpriceperkg")) {

                                try {
                                    if (isweightmachineconnected) {
                                        itemWeight_edittextwidgetValue.setText("");
                                        newOrders_menuItem_fragment.connectUSBSerialPort();
                                        newOrders_menuItem_fragment.setHandlerForReceivingWeight(newHandler(itemWeight_edittextwidgetValue));

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }
                    if (String.valueOf(response_from).equalsIgnoreCase("newOrder_MenuItemFragment")) {
                        String weight = bundle.getString("weight");
                        itemWeight_edittextwidgetValue.setText(String.valueOf(weight));

                        itemWeight_edittextwidgetValue.onEditorAction(EditorInfo.IME_ACTION_DONE);

                    }
                    return false;
                }
            };
            return new Handler(callback);
        }

    }

    private String CalculateTmcPricePerKgValueWithMarkup(Modal_NewOrderItems modal_newOrderItems) {
        String tmcpriceperkgWithAppMarkupValueString ="0";
        try{
            double tmcpriceperkg_double =0 ,  tmcpriceperkgWithAppMarkupValue = 0 , appMarkupPercn_value =0;

            try {

                tmcpriceperkg_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getTmcpriceperkg()));


            }
            catch (Exception e){
                e.printStackTrace();
                Toast.makeText(context,"The tmcpriceperkg_double Zero in CalculateTmcPricePerKgValueWithMarkup AutoComplete",Toast.LENGTH_LONG).show();

                tmcpriceperkg_double =0;
            }

            int markupPercentageInt = Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));

            try{
                appMarkupPercn_value  = (markupPercentageInt * tmcpriceperkg_double  )/100;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                tmcpriceperkgWithAppMarkupValue  = appMarkupPercn_value + tmcpriceperkg_double;
            }
            catch (Exception e){
                e.printStackTrace();
            }

          try {

              if (tmcpriceperkgWithAppMarkupValue > 0) {

                  try {
                      tmcpriceperkgWithAppMarkupValueString = String.valueOf(Double.parseDouble(String.valueOf(Math.round(tmcpriceperkgWithAppMarkupValue))));
                  } catch (Exception e) {
                      e.printStackTrace();
                      tmcpriceperkgWithAppMarkupValueString = String.valueOf(tmcpriceperkgWithAppMarkupValue);

                  }
              }
              else{
                  tmcpriceperkgWithAppMarkupValueString = String.valueOf(modal_newOrderItems.getTmcpriceperkg());

              }



          }
          catch (Exception e){
              tmcpriceperkgWithAppMarkupValueString = String.valueOf(modal_newOrderItems.getTmcpriceperkg());

              e.printStackTrace();
          }


        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"There is an error in CalculateTmcPricePerKgValueWithMarkup AutoComplete",Toast.LENGTH_LONG).show();
            tmcpriceperkgWithAppMarkupValueString = String.valueOf(modal_newOrderItems.getTmcpriceperkg());

        }
        return  tmcpriceperkgWithAppMarkupValueString;
    }

    private String CalculateTmcPriceValueWithMarkup(Modal_NewOrderItems modal_newOrderItems) {
        String  tmcpriceWithAppMarkupValueString = "0";
        try{
            double tmcprice_double =0 ,  tmcpriceWithAppMarkupValue = 0 , appMarkupPercn_value =0;

            try {

                tmcprice_double = Double.parseDouble(String.valueOf(modal_newOrderItems.getTmcprice()));


            }
            catch (Exception e){
                e.printStackTrace();
                tmcprice_double =0;
                Toast.makeText(context,"The tmcprice_double Zero in CalculateTmcPriceValueWithMarkup AutoComplete",Toast.LENGTH_LONG).show();

            }
            int markupPercentageInt = Integer.parseInt(String.valueOf(modal_newOrderItems.getAppmarkuppercentage()));

            try{
                appMarkupPercn_value  = (markupPercentageInt * tmcprice_double )/100;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                tmcpriceWithAppMarkupValue  = appMarkupPercn_value + tmcprice_double;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {

                if (tmcpriceWithAppMarkupValue > 0) {


                    try {
                        tmcpriceWithAppMarkupValueString = (String.valueOf((int) Double.parseDouble(String.valueOf(Math.round(tmcpriceWithAppMarkupValue)))));
                    } catch (Exception e) {
                        tmcpriceWithAppMarkupValueString = String.valueOf(tmcpriceWithAppMarkupValue);
                        e.printStackTrace();
                    }
                }
                else{
                    tmcpriceWithAppMarkupValueString = String.valueOf(modal_newOrderItems.getTmcprice());
                }

            }
            catch (Exception e){
                tmcpriceWithAppMarkupValueString = String.valueOf(modal_newOrderItems.getTmcprice());
                e.printStackTrace();
            }



        }
        catch (Exception e){
            Toast.makeText(context,"There is an error in CalculateTmcPriceValueWithMarkup AutoComplete",Toast.LENGTH_LONG).show();
            tmcpriceWithAppMarkupValueString = String.valueOf(modal_newOrderItems.getTmcprice());

            e.printStackTrace();
        }
        return tmcpriceWithAppMarkupValueString;

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    private boolean checkforBarcodeInCart(String barcode) {
        String search = barcode;
        for(String str: NewOrders_MenuItem_Fragment.cart_Item_List) {
            if(str.trim().equals(search))
                return true;
        }
        return false;
    }




     void onNewDataArrived(HashMap<String, Modal_NewOrderItems> cartItem_hashmap_old) {
        try{

        }
        catch(Exception e) {
            e.printStackTrace();
        }
         HashMap<String, Modal_NewOrderItems> cartItem_hashmap_new = NewOrders_MenuItem_Fragment.cartItem_hashmap;


         List<String> cart_Item_List_new =  new ArrayList<>(cartItem_hashmap_new.keySet());
        List<String> cart_Item_List_old =  new ArrayList<>(cartItem_hashmap_old.keySet());

         final DiffUtilCallBack diffCallback = new DiffUtilCallBack(cartItem_hashmap_old, cart_Item_List_new,cartItem_hashmap_new,cart_Item_List_old);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        itemInCart.clear();
        itemInCart.putAll(cartItem_hashmap_new);

        diffResult.dispatchUpdatesTo(this);


         int last_index=NewOrders_MenuItem_Fragment.cartItem_hashmap.size()-1;
         newOrders_menuItem_fragment.recyclerView.scrollToPosition(last_index);



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