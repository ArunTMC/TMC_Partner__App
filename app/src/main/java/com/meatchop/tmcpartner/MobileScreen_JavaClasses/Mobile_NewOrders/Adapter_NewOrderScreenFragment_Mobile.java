package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.Replacement_RefundClasses.AddReplacement_Refund_OrdersScreen;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.AddSwiggyOrders;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

public class Adapter_NewOrderScreenFragment_Mobile extends RecyclerView.Adapter<Adapter_NewOrderScreenFragment_Mobile.ViewHolder> {
    NewOrderScreenFragment_mobile newOrderScreenFragment_mobile;
    private static Context context;
    private String pricetype_of_pos;
    Adapter_AutoCompleteMenuItem_mobile adapter;
    String Menulist;
    public static HashMap<String,Modal_NewOrderItems> itemInCart_Hashmap = new HashMap();
    private Handler handler;
    String fromActivity="";
    AddReplacement_Refund_OrdersScreen addReplacement_refund_ordersScreen;
    static boolean isTMCproduct = false;
    private static double item_total = 0;
    private double item_weight = 0;

    public Adapter_NewOrderScreenFragment_Mobile(Context mContext, HashMap<String, Modal_NewOrderItems> itemInCart_hashmap, String menuItems, NewOrderScreenFragment_mobile newOrderScreenFragment_mobile) {

        this.newOrderScreenFragment_mobile = newOrderScreenFragment_mobile;
        this.context = mContext;

        this.itemInCart_Hashmap = itemInCart_hashmap;
        this.fromActivity = "FromNewOrders";

        this.Menulist = menuItems;

    }
    public Adapter_NewOrderScreenFragment_Mobile(Context mContext, HashMap<String, Modal_NewOrderItems> itemInCart_hashmap, String menuItems, AddReplacement_Refund_OrdersScreen addReplacement_refund_ordersScreen, String fromActivity) {

        this.addReplacement_refund_ordersScreen = addReplacement_refund_ordersScreen;
        this.context = mContext;

        this.itemInCart_Hashmap = itemInCart_hashmap;
        this.fromActivity = fromActivity;
        this.Menulist = menuItems;

    }

    @NotNull
    @Override
    public Adapter_NewOrderScreenFragment_Mobile.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_neworders_billingscreen_adapterscreen, parent, false);

        return new Adapter_NewOrderScreenFragment_Mobile.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.mobile_itemIndex.setText(String.valueOf(position + 1));



        if (position == (itemInCart_Hashmap.size() - 1)) {
            holder.mobile_addNewItem_layout.setVisibility(View.VISIBLE);
            holder.mobile_autoComplete_widget.setFocusable(true);
            holder.mobile_autoComplete_widget.requestFocus();

        } else {
            holder.mobile_addNewItem_layout.setVisibility(View.GONE);

        }



        int length = holder.mobile_autoComplete_widget.getText().length();
        holder.mobile_autoComplete_widget.setSelection(length);
        //Log.e("TAG", "position" + position);
        Modal_NewOrderItems recylerviewPojoClass = NewOrderScreenFragment_mobile.cartItem_hashmap.get(NewOrderScreenFragment_mobile.cart_Item_List.get(position));


        if (recylerviewPojoClass.getItemuniquecode().equals("empty")) {
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getItemname());
            //Log.e("TAG", "adapter 1 " + recylerviewPojoClass.getGrossweight());
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getPricePerItem());
            //Log.e("TAG", "adapter 1" + recylerviewPojoClass.getTmcpriceperkg());

            holder.mobile_itemPrice_Widget.setText("");
            holder.mobile_itemWeight_widget.setText("");
            holder.mobile_itemQuantity_widget.setText("0");
            holder.mobile_barcode_widget.setText("");

            holder.mobile_autoComplete_widget.setText("");
            holder.barcodescannerLayout.setVisibility(View.VISIBLE);
            holder. barcode_text_parentLayout.setVisibility(View.GONE);

        }
        else
        {
            holder.barcodescannerLayout.setVisibility(View.GONE);
            holder. barcode_text_parentLayout.setVisibility(View.VISIBLE);


            pricetype_of_pos = String.valueOf(Objects.requireNonNull(NewOrderScreenFragment_mobile.cartItem_hashmap.get(NewOrderScreenFragment_mobile.cart_Item_List.get(position))).getPricetypeforpos());


            if (pricetype_of_pos.equals("tmcprice")) {

                holder.mobile_autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                if(!holder.mobile_autoComplete_widget.getText().toString().equals("")&&holder.mobile_autoComplete_widget.getText().length()>3){
                    holder.mobile_autoComplete_widget.setKeyListener(null);
                    holder.mobile_autoComplete_widget.clearFocus();
                    holder.mobile_autoComplete_widget.dismissDropDown();
                    holder.mobile_autoComplete_widget.setBackground(getDrawable(context,R.drawable.grey_color_textview_backgrounf));

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    holder.mobile_itemWeight_widget.setFocusedByDefault(false);
                }
                holder.mobile_barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                if(!holder.mobile_barcode_widget.getText().toString().equals("")&&holder.mobile_barcode_widget.getText().length()>3)
                {
                    holder.mobile_barcode_widget.setKeyListener(null);

                }



                holder.mobile_barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());

                holder.mobile_itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));
                holder.mobile_itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
              //  taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                newOrderScreenFragment_mobile.add_amount_ForBillDetails();
                holder.mobile_itemWeight_widget.setBackground(getDrawable(context,R.drawable.grey_color_textview_backgrounf));
                holder.mobile_itemWeight_widget.setKeyListener(null);

                try {
                    if(recylerviewPojoClass.getGrossweight().equals("")){
                        if (recylerviewPojoClass.getNetweight().equals("")) {

                            holder.mobile_itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getPortionsize()));

                        }
                        else{
                            holder.mobile_itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getNetweight()));

                        }
                    }
                    else{
                        holder.mobile_itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getGrossweight()));

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
                holder.mobile_barcode_widget.setText(recylerviewPojoClass.getItemuniquecode());
                holder.mobile_barcode_widget.setKeyListener(null);

                holder.mobile_autoComplete_widget.setText(recylerviewPojoClass.getItemname());
                holder.mobile_autoComplete_widget.setKeyListener(null);
                holder.mobile_autoComplete_widget.clearFocus();
                holder.mobile_autoComplete_widget.dismissDropDown();
                holder.mobile_autoComplete_widget.setBackground(getDrawable(context,R.drawable.grey_color_textview_backgrounf));

                holder.mobile_itemPrice_Widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalPrice()));

                //taxes_and_charges = Integer.parseInt(recylerviewPojoClass.getGstpercentage());
                holder.mobile_itemWeight_widget.setText(String.valueOf(recylerviewPojoClass.getItemFinalWeight()));
                holder.mobile_itemQuantity_widget.setText(String.valueOf(recylerviewPojoClass.getQuantity()));
                newOrderScreenFragment_mobile.add_amount_ForBillDetails();
            }
            //Log.e(TAG, "Got barcode isBarcodeEn position " + position);
            //Log.e(TAG, "Got barcode newOrderScreenFragment_mobile.cart_Item_List.size() " + newOrderScreenFragment_mobile.cart_Item_List.size());


        }





        holder.mobile_itemWeight_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    double item_total=0;
                    String barcode =NewOrderScreenFragment_mobile.cart_Item_List.get(position);

                    Modal_NewOrderItems newItem_newOrdersPojoClass = (Objects.requireNonNull(NewOrderScreenFragment_mobile.cartItem_hashmap.get(barcode)));
                    String pricetypeforpos = newItem_newOrdersPojoClass.getPricetypeforpos().toString();
                    int quantity = 1;
                    try{
                        quantity = Integer.parseInt(newItem_newOrdersPojoClass.getQuantity());

                    }
                    catch (Exception e){
                        quantity = 1;
                        e.printStackTrace();
                    }

                    int priceperKg = Integer.parseInt(newItem_newOrdersPojoClass.getTmcpriceperkg());
                    String itemWeight = holder.mobile_itemWeight_widget.getText().toString();
                    itemWeight =itemWeight .replaceAll("[^\\d.]", "");
                  newOrderScreenFragment_mobile.isanyProducthaveZeroAsweight=false;
                    if(itemWeight.equals("")){
                        itemWeight="0";
                        newOrderScreenFragment_mobile.isanyProducthaveZeroAsweight=true;
                        new TMCAlertDialogClass(context, R.string.app_name, R.string.Weight_cant_be_empty,
                                R.string.OK_Text,R.string.Empty_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                    }

                                    @Override
                                    public void onNo() {

                                    }
                                });
                        return false;

                    }

                    int weight = Integer.parseInt(itemWeight);
                        if(weight==0){
                            newOrderScreenFragment_mobile.isanyProducthaveZeroAsweight=true;
                            new TMCAlertDialogClass(context, R.string.app_name, R.string.Weight_cant_be_empty,
                                    R.string.OK_Text,R.string.Empty_Text,
                                    new TMCAlertDialogClass.AlertListener() {
                                        @Override
                                        public void onYes() {
                                        }

                                        @Override
                                        public void onNo() {

                                        }
                                    });
                            return false;
                        }

                        if (pricetypeforpos.equals("tmcpriceperkg")) {

                            if (weight < 1000) {
                                item_total = (priceperKg * weight);
                                //Log.e("TAG", "adapter 9 item_total price_per_kg" + priceperKg);

                                //Log.e("TAG", "adapter 9 item_total weight" + weight);

                                //Log.e("TAG", "adapter 9 item_total " + priceperKg * weight);

                                item_total = item_total / 1000;
                                //Log.e("TAG", "adapter 9 item_total " + item_total);

                                //Log.e("TAg", "weight2" + weight);
                                item_total = Double.parseDouble(decimalFormat.format(item_total));

                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(Math.round(item_total*quantity)) + ".00");
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(item_total));
                                newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                //Log.e("TAg", "weight item_total" + item_total);

                                //holder.mobile_itemPrice_Widget.setText(String.valueOf(item_total));

                                newOrderScreenFragment_mobile.CallAdapter();
                            }

                            if (weight == 1000) {
                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(priceperKg*quantity) + ".00");
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(priceperKg));
                                newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);


                                newOrderScreenFragment_mobile.CallAdapter();
                            }

                            if (weight > 1000) {
                                priceperKg = Integer.parseInt(newItem_newOrdersPojoClass.getTmcpriceperkg());

                                //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);

                                //Log.e("TAg", "weight3" + weight);

                                int itemquantity = weight - 1000;
                                //Log.e("TAg", "weight itemquantity" + itemquantity);

                                item_total = (priceperKg * itemquantity) / 1000;
                                item_total = Double.parseDouble(decimalFormat.format(item_total));


                                //Log.e("TAg", "weight item_total" + item_total);

                                double total = priceperKg + item_total;
                                total = Double.parseDouble(decimalFormat.format((total)));


                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(Math.round(total*quantity)) + ".00");
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(total));
                                newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                                newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                // holder.mobile_itemWeight_widget.setText(String.valueOf(total));
                                newOrderScreenFragment_mobile.CallAdapter();
                            }


                        }


                  /*  }

                    else {

                        Toast.makeText(context, "Cant give zero as weight", Toast.LENGTH_LONG).show();

                    }

                   */


                }
                return false;
            }
        });



        holder. barcodescannerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,NewOrderBarcodeScannerScreen.class);
                context.startActivity(i);


            }
        });



        holder.mobile_addNewItem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(newOrderScreenFragment_mobile.isanyProducthaveZeroAsweight) {

                    new TMCAlertDialogClass(context, R.string.app_name, R.string.Weight_cant_be_empty,
                            R.string.OK_Text,R.string.Empty_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                }

                                @Override
                                public void onNo() {

                                }
                            });
                }
                else {
                    if (!holder.mobile_autoComplete_widget.getText().toString().equals("") && (!holder.mobile_itemWeight_widget.getText().toString().equals("") || !holder.mobile_itemQuantity_widget.getText().toString().equals(""))) {


                        newOrderScreenFragment_mobile.createEmptyRowInListView("empty");

                        newOrderScreenFragment_mobile.CallAdapter();

                   /* if(newOrderScreenFragment_mobile.isProceedtoCheckoutinRedeemdialogClicked){
                        newOrderScreenFragment_mobile.cancelRedeemPointsFromOrder();

                    }
                    if((!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals("0"))||(!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals(""))){
                        newOrderScreenFragment_mobile.discount_Edit_widget.setText("0");
                        newOrderScreenFragment_mobile.discount_rs_text_widget.setText("0.00");
                        newOrderScreenFragment_mobile.discountAmount="0";
                    }

                    */

                    } else {
                        Toast.makeText(context, "You have to fill this Item First", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        holder.mobile_removeItem_fromCart_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.e(TAG, "Item" + String.valueOf(NewOrderScreenFragment_mobile.cartItem_hashmap.size() - 1));
                String barcode = NewOrderScreenFragment_mobile.cart_Item_List.get(position);

                if(barcode.equals(""))
                {
                    barcode="empty";
                }
                //Log.i("TAG", "KEY: " + barcode);
                if ((NewOrderScreenFragment_mobile.cartItem_hashmap.size() - 1) == 0) {

                    //Log.i("TAG", "KEY: " + barcode);

                    NewOrderScreenFragment_mobile.cartItem_hashmap.remove(barcode);
                    NewOrderScreenFragment_mobile.cart_Item_List.remove(barcode);
                    newOrderScreenFragment_mobile.add_amount_ForBillDetails();
                    newOrderScreenFragment_mobile.createEmptyRowInListView("empty");
                   // newOrderScreenFragment_mobile.discountAmount ="0";
                  //  newOrderScreenFragment_mobile.discount_Edit_widget .setText("0");
                  //  newOrderScreenFragment_mobile.discount_rs_text_widget .setText("0");
                  //  newOrderScreenFragment_mobile.finaltoPayAmount = "0";

                    newOrderScreenFragment_mobile.CallAdapter();

                    //Log.e(TAG, "Item_not_deleted  " + String.valueOf(NewOrderScreenFragment_mobile.cartItem_hashmap.size() - 1));

                } else {
                    //Log.i("TAG", "KEY: " + barcode);


                    NewOrderScreenFragment_mobile.cartItem_hashmap.remove(barcode);
                    NewOrderScreenFragment_mobile.cart_Item_List.remove(barcode);

                    newOrderScreenFragment_mobile.add_amount_ForBillDetails();
                    newOrderScreenFragment_mobile.CallAdapter();

                    //  NewOrderScreenFragment_mobile.adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();
                    //Log.e(TAG, "Item_deleted  " + String.valueOf(NewOrderScreenFragment_mobile.cartItem_hashmap.size() - 1));


                }
               /* if(newOrderScreenFragment_mobile.isProceedtoCheckoutinRedeemdialogClicked){
                    newOrderScreenFragment_mobile.cancelRedeemPointsFromOrder();

                }
                if((!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals("0"))||(!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals(""))){
                    newOrderScreenFragment_mobile.discount_Edit_widget.setText("0");
                    newOrderScreenFragment_mobile.discount_rs_text_widget.setText("0.00");
                    newOrderScreenFragment_mobile.discountAmount="0";
                }


                */

            }
        });

        holder.mobile_tmcUnitprice_weightAdd_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(! holder.mobile_itemQuantity_widget.getText().toString().equals("")&& !holder.mobile_itemPrice_Widget.getText().toString().equals(""))
                {

                    String barcode = NewOrderScreenFragment_mobile.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = NewOrderScreenFragment_mobile.cartItem_hashmap.get(barcode);
                    int quantity = Integer.parseInt(holder.mobile_itemQuantity_widget.getText().toString());

                    quantity = quantity + 1;
                    holder.mobile_itemQuantity_widget.setText(String.valueOf(quantity));
                    double item_price = Double.parseDouble(Objects.requireNonNull(modal_newOrderItems).getItemPrice_quantityBased());
                    item_price = item_price * quantity;

                    holder.mobile_itemPrice_Widget.setText(decimalFormat.format(item_price));
                    modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));


                    modal_newOrderItems.setQuantity(String.valueOf(quantity));
                  newOrderScreenFragment_mobile.add_amount_ForBillDetails();
                    /*if(newOrderScreenFragment_mobile.isProceedtoCheckoutinRedeemdialogClicked){
                        newOrderScreenFragment_mobile.cancelRedeemPointsFromOrder();

                    }
                    if((!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals("0"))||(!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals(""))){
                        newOrderScreenFragment_mobile.discount_Edit_widget.setText("0");
                        newOrderScreenFragment_mobile.discount_rs_text_widget.setText("0.00");
                        newOrderScreenFragment_mobile.discountAmount="0";
                    }

                  */

                }
                else{
                    Toast.makeText(context,"First u have to Select an Item",Toast.LENGTH_LONG).show();
                }
            }
        });


        holder.mobile_tmcUnitprice_weightMinus_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(! holder.mobile_itemQuantity_widget.getText().toString().equals("")&& !holder.mobile_itemPrice_Widget.getText().toString().equals(""))
                {

                    String barcode = NewOrderScreenFragment_mobile.cart_Item_List.get(position);

                    Modal_NewOrderItems modal_newOrderItems = NewOrderScreenFragment_mobile.cartItem_hashmap.get(barcode);


                    int quantity = Integer.parseInt(holder.mobile_itemQuantity_widget.getText().toString());
                    if (quantity > 1) {
                        quantity = quantity - 1;
                        holder.mobile_itemQuantity_widget.setText(String.valueOf(quantity));
                        double item_price = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
                        item_price = item_price * quantity;
                        modal_newOrderItems.setQuantity(String.valueOf(quantity));
                        holder.mobile_itemPrice_Widget.setText(decimalFormat.format(item_price));
                        modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(item_price)));

                      newOrderScreenFragment_mobile.add_amount_ForBillDetails();

                       /* if(newOrderScreenFragment_mobile.isProceedtoCheckoutinRedeemdialogClicked){
                            newOrderScreenFragment_mobile.cancelRedeemPointsFromOrder();

                        }
                        if((!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals("0"))||(!newOrderScreenFragment_mobile.discount_Edit_widget.getText().toString().equals(""))){
                            newOrderScreenFragment_mobile.discount_Edit_widget.setText("0");
                            newOrderScreenFragment_mobile.discount_rs_text_widget.setText("0.00");
                            newOrderScreenFragment_mobile.discountAmount="0";
                        }

                       */

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

    public static void getMenuItemUsingBarCode(String barcode) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        Log.e("TAG", "barcode  1    " + barcode);

        if(!isTMCproduct){

            Log.e("TAG", "barcode  1.1   " + barcode);

            for (int i = 0; i < NewOrderScreenFragment_mobile.completemenuItem.size(); i++) {
                String itemWeight="";
                //Log.e(TAG, " barcode  1  for" + barcode);

                Modal_NewOrderItems modal_newOrderItems = NewOrderScreenFragment_mobile.completemenuItem.get(i);

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
                        try{

                            newItem_newOrdersPojoClass.dunzoprice = (modal_newOrderItems.getDunzoprice());

                        }
                        catch (Exception e){
                            newItem_newOrdersPojoClass.dunzoprice = "";
                            e.printStackTrace();
                        }

                        try{
                            newItem_newOrdersPojoClass.bigbasketprice = (modal_newOrderItems.getBigbasketprice());


                        }
                        catch (Exception e){
                            newItem_newOrdersPojoClass.bigbasketprice = "";
                            e.printStackTrace();
                        }


                        try{
                            newItem_newOrdersPojoClass.swiggyprice = (modal_newOrderItems.getSwiggyprice());


                        }
                        catch (Exception e){
                            newItem_newOrdersPojoClass.swiggyprice = "";
                            e.printStackTrace();
                        }



                        newItem_newOrdersPojoClass.barcode =String.valueOf(modal_newOrderItems.getBarcode());
                        newItem_newOrdersPojoClass.tmcctgykey =String.valueOf(modal_newOrderItems.getTmcctgykey());

                        try {
                            if (modal_newOrderItems.getApplieddiscountpercentage().equals("")) {
                                newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));

                            } else {
                                newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf(modal_newOrderItems.getApplieddiscountpercentage()));
                            }
                        }
                        catch (Exception e){
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
                        }
                        catch (Exception e){
                            newItem_newOrdersPojoClass.appmarkuppercentage = (String.valueOf("0"));
                            Toast.makeText(context, "Error in getting appmarkuppercentage entry on Recycler", Toast.LENGTH_LONG).show();

                            e.printStackTrace();
                        }

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

                        if(NewOrderScreenFragment_mobile.isPhoneOrderSelected){

                            if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))){
                                newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkgWithMarkupValue();

                            }
                            else{
                                String tmcPriceperKgWithMarkup_calcNow = CalculateTmcPricePerKgValueWithMarkup(modal_newOrderItems);
                                newItem_newOrdersPojoClass.tmcpriceperkg = String.valueOf(tmcPriceperKgWithMarkup_calcNow);
                                modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(tmcPriceperKgWithMarkup_calcNow));
                            }


                            // newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkgWithMarkupValue();
                            if((!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.00"))  && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("")))
                            {
                                newItem_newOrdersPojoClass.tmcprice = String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue());
                                newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((modal_newOrderItems.getTmcpriceWithMarkupValue()))));
                                newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf(((modal_newOrderItems.getTmcpriceWithMarkupValue()))));



                            }
                            else{
                                String tmcPriceWithMarkup_calcNow = CalculateTmcPriceValueWithMarkup(modal_newOrderItems);
                                newItem_newOrdersPojoClass.tmcprice = String.valueOf(tmcPriceWithMarkup_calcNow);
                                newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((tmcPriceWithMarkup_calcNow))));
                                newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf((tmcPriceWithMarkup_calcNow)));
                                modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(tmcPriceWithMarkup_calcNow));
                            }

                        }
                        else{
                            newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                            newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                            newItem_newOrdersPojoClass.itemFinalPrice = (String.valueOf(((modal_newOrderItems.getTmcprice()))));
                            newItem_newOrdersPojoClass.itemPrice_quantityBased = (String.valueOf(((modal_newOrderItems.getTmcprice()))));


                        }


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
                        e.printStackTrace();
                        Toast.makeText(context,"Error in get MenuItem using Barcode ",Toast.LENGTH_LONG).show();

                    }
                }


            }
        }
        else{
            Log.e("TAG", " barcode  2" + barcode);

            if (barcode.length() == 14) {
                // Log.e(TAG, " barcode  3" + barcode);
                try{
                    String itemuniquecode = barcode.substring(0, 9);
                    String itemWeight = barcode.substring(9, 14);
                    //Log.e(TAG, "1 barcode uniquecode" + itemuniquecode);
                    //Log.e(TAG, "1 barcode itemweight" + itemWeight);

                    for (int i = 0; i < NewOrderScreenFragment_mobile.completemenuItem.size(); i++) {

                        Modal_NewOrderItems modal_newOrderItems = NewOrderScreenFragment_mobile.completemenuItem.get(i);

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
                            try{

                                newItem_newOrdersPojoClass.dunzoprice = (modal_newOrderItems.getDunzoprice());

                            }
                            catch (Exception e){
                                newItem_newOrdersPojoClass.dunzoprice = "";
                                e.printStackTrace();
                            }

                            try{
                                newItem_newOrdersPojoClass.bigbasketprice = (modal_newOrderItems.getBigbasketprice());


                            }
                            catch (Exception e){
                                newItem_newOrdersPojoClass.bigbasketprice = "";
                                e.printStackTrace();
                            }


                            try{
                                newItem_newOrdersPojoClass.swiggyprice = (modal_newOrderItems.getSwiggyprice());


                            }
                            catch (Exception e){
                                newItem_newOrdersPojoClass.swiggyprice = "";
                                e.printStackTrace();
                            }

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




                            try {
                                if (modal_newOrderItems.getApplieddiscountpercentage().equals("")) {
                                    newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf("0"));

                                } else {
                                    newItem_newOrdersPojoClass.applieddiscountpercentage = (String.valueOf(modal_newOrderItems.getApplieddiscountpercentage()));
                                }
                            }
                            catch (Exception e){

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
                            }
                            catch (Exception e){
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

                            if(NewOrderScreenFragment_mobile.isPhoneOrderSelected) {


                                if ((!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals("0.0"))  && (!String.valueOf(modal_newOrderItems.getTmcpriceperkgWithMarkupValue()).equals(""))){
                                    newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkgWithMarkupValue();

                                }
                                else{
                                    String tmcPriceperKgWithMarkup_calcNow = CalculateTmcPricePerKgValueWithMarkup(modal_newOrderItems);
                                    newItem_newOrdersPojoClass.tmcpriceperkg = String.valueOf(tmcPriceperKgWithMarkup_calcNow);
                                    modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(tmcPriceperKgWithMarkup_calcNow));
                                }


                                if ((!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.00")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals("0.0")) && (!String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()).equals(""))){
                                    newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcpriceWithMarkupValue();

                                }
                                else{
                                    String tmcPriceWithMarkup_calcNow = CalculateTmcPriceValueWithMarkup(modal_newOrderItems);
                                    newItem_newOrdersPojoClass.tmcprice = String.valueOf(tmcPriceWithMarkup_calcNow);
                                    modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(tmcPriceWithMarkup_calcNow));

                                }




                            }
                            else{
                                newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                                newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();

                            }

                            if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcpriceperkg")) {
                                double priceperKg =0;

                                if(NewOrderScreenFragment_mobile.isPhoneOrderSelected){
                                    priceperKg =  Double.parseDouble(modal_newOrderItems.getTmcpriceperkgWithMarkupValue());

                                }
                                else{
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

                               //     holder.mobile_itemPrice_Widget.setText(String.valueOf(item_total));

                                    //onNewDataArrived(itemInCart);
                                    NewOrderScreenFragment_mobile.adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();

                                }

                                if (weight == 1000) {
                                    newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(priceperKg));
                                    newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(priceperKg));
                                    newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                    newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                    //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);


                                    // onNewDataArrived(itemInCart);

                                    NewOrderScreenFragment_mobile.adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();

                                }

                                if (weight > 1000) {
                                    priceperKg =0;

                                    if(NewOrderScreenFragment_mobile.isPhoneOrderSelected){
                                        priceperKg = Double.parseDouble(modal_newOrderItems.getTmcpriceperkgWithMarkupValue());

                                    }
                                    else{
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

                                  //  holder.mobile_itemPrice_Widget.setText(String.valueOf(total));
                                    //  onNewDataArrived(itemInCart);

                                    NewOrderScreenFragment_mobile.adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();

                                }


                            }


                            if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcprice")) {



                                if(NewOrderScreenFragment_mobile.isPhoneOrderSelected){
                                    newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(modal_newOrderItems.getTmcpriceWithMarkupValue()));

                                    newItem_newOrdersPojoClass.setItemFinalPrice(decimalFormat.format(Double.parseDouble(modal_newOrderItems.getTmcpriceWithMarkupValue())));

                                }
                                else{
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
                    e.printStackTrace();
                    Toast.makeText(context,"Error in get MenuItem using TMC Barcode",Toast.LENGTH_LONG).show();

                }
            }

        }
    }


    private static void addItemIntheCart(String barcode, Modal_NewOrderItems newItem_newOrdersPojoClass, String itemWeight, String itemUniquecode) {
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
                    Modal_NewOrderItems modal_newOrderItems = NewOrderScreenFragment_mobile.cartItem_hashmap.get(barcode);
                    int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                    quantity = quantity + 1;

                    double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
                    double finalprice = quantity * itemPrice_quantityBased;

                    modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
                    modal_newOrderItems.setQuantity(String.valueOf(quantity));
                    NewOrderScreenFragment_mobile.cartItem_hashmap.put(barcode, modal_newOrderItems);
                    if (checkforBarcodeInCart("empty")) {
                        NewOrderScreenFragment_mobile.cart_Item_List.remove("empty");

                        NewOrderScreenFragment_mobile.cartItem_hashmap.remove("empty");
                    }


                } else {
                    if (checkforBarcodeInCart("empty")) {
                        NewOrderScreenFragment_mobile.cart_Item_List.remove("empty");

                        NewOrderScreenFragment_mobile.cartItem_hashmap.remove("empty");
                    }

                    NewOrderScreenFragment_mobile.cart_Item_List.add(barcode);
                    NewOrderScreenFragment_mobile.cartItem_hashmap.put(barcode, newItem_newOrdersPojoClass);

                }
                Modal_NewOrderItems m = NewOrderScreenFragment_mobile.cartItem_hashmap.get(barcode);
                // onNewDataArrived(itemInCart);
                NewOrderScreenFragment_mobile.adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();

            }

            else{


                try {
                    IsItemAlreadyAddedinCart = checkforBarcodeInCart(itemUniquecode);
                } catch (Exception e) {
                    Toast.makeText(context, "Error in boolean IsItemAlreadyAddedin Cart", Toast.LENGTH_LONG).show();

                }
                if (IsItemAlreadyAddedinCart) {
                    Modal_NewOrderItems modal_newOrderItems = NewOrderScreenFragment_mobile.cartItem_hashmap.get(itemUniquecode);
                    int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
                    quantity = quantity + 1;

                    double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
                    double finalprice = quantity * itemPrice_quantityBased;

                    modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
                    modal_newOrderItems.setQuantity(String.valueOf(quantity));
                    NewOrderScreenFragment_mobile.cartItem_hashmap.put(itemUniquecode, modal_newOrderItems);
                    if (checkforBarcodeInCart("empty")) {
                        NewOrderScreenFragment_mobile.cart_Item_List.remove("empty");

                        NewOrderScreenFragment_mobile.cartItem_hashmap.remove("empty");
                    }


                } else {
                    if (checkforBarcodeInCart("empty")) {
                        NewOrderScreenFragment_mobile.cart_Item_List.remove("empty");

                        NewOrderScreenFragment_mobile.cartItem_hashmap.remove("empty");
                    }

                    NewOrderScreenFragment_mobile.cart_Item_List.add(itemUniquecode);
                    NewOrderScreenFragment_mobile.cartItem_hashmap.put(itemUniquecode, newItem_newOrdersPojoClass);

                }
                // onNewDataArrived(itemInCart);

                NewOrderScreenFragment_mobile.adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();

            }
        }
        catch(Exception e ){
            Toast.makeText(context, "Error in boolean pricetypeOfItem ", Toast.LENGTH_LONG).show();

        }


    }



    private static boolean checkforBarcodeInCart(String barcode) {
        String search = barcode;
        for(String str: NewOrderScreenFragment_mobile.cart_Item_List) {
            if(str.trim().equals(search))
                return true;
        }
        return false;
    }





    private static String CalculateTmcPricePerKgValueWithMarkup(Modal_NewOrderItems modal_newOrderItems) {
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
            try{
                tmcpriceperkgWithAppMarkupValueString = String.valueOf( Double.parseDouble(String.valueOf(Math.round(tmcpriceperkgWithAppMarkupValue))));
            }
            catch (Exception e){
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

    private static String CalculateTmcPriceValueWithMarkup(Modal_NewOrderItems modal_newOrderItems) {
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
                tmcpriceWithAppMarkupValueString = (String.valueOf((int) Double.parseDouble(String.valueOf(Math.round(tmcpriceWithAppMarkupValue)))));
            }
            catch (Exception e){
                tmcpriceWithAppMarkupValueString = String.valueOf(tmcpriceWithAppMarkupValue);
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
    public int getItemCount() {
        return itemInCart_Hashmap.size();
    }
    
    
    
    
    public class ViewHolder extends RecyclerView.ViewHolder {

        AutoCompleteTextView mobile_autoComplete_widget;

        LinearLayout mobile_tmcUnitprice_weightAdd_layout, mobile_tmcUnitprice_weightMinus_layout;


        TextView mobile_itemIndex,mobile_barcode_widget, mobile_itemQuantity_widget;

        TextView mobile_itemPrice_Widget;

        EditText mobile_itemWeight_widget;

        ImageView mobile_delete_to_remove_item_widget;
        LinearLayout mobile_removeItem_fromCart_widget, mobile_addNewItem_layout,barcode_text_parentLayout,parentLayout,barcodescannerLayout;
        boolean isTMCproduct = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mobile_autoComplete_widget = itemView.findViewById(R.id.mobile_autoComplete_widget);
            this.mobile_addNewItem_layout = itemView.findViewById(R.id.mobile_addNewItemLayout);
            this.mobile_removeItem_fromCart_widget = itemView.findViewById(R.id.mobile_removeItem_fromCart_widget);
            this.mobile_delete_to_remove_item_widget = itemView.findViewById(R.id.mobile_delete_to_remove_item_widget);
            this.mobile_barcode_widget = itemView.findViewById(R.id.mobile_barcode_textwidget);
            this.mobile_itemPrice_Widget = itemView.findViewById(R.id.mobile_PriceWidget);
            this.mobile_itemWeight_widget = itemView.findViewById(R.id.mobile_weight_textwidget);
            this.mobile_itemQuantity_widget = itemView.findViewById(R.id.mobile_itemQuantity_widget);
            this.mobile_tmcUnitprice_weightAdd_layout = itemView.findViewById(R.id.mobile_tmcUnitprice_weightAdd_layout);
            this.mobile_tmcUnitprice_weightMinus_layout = itemView.findViewById(R.id.mobile_tmcUnitprice_weightMinus_layout);
            this.mobile_itemIndex = itemView.findViewById(R.id.mobile_itemIndex);
            this.barcodescannerLayout = itemView.findViewById(R.id.barcodescannerLayout);

            this.barcode_text_parentLayout = itemView.findViewById(R.id.barcode_text_parentLayout);

            barcodescannerLayout.setVisibility(View.VISIBLE);
            barcode_text_parentLayout.setVisibility(View.GONE);
            adapter = new Adapter_AutoCompleteMenuItem_mobile(context, Menulist,getPosition());
            adapter.setHandler(newHandler());


            mobile_autoComplete_widget.setAdapter(adapter);
            mobile_autoComplete_widget.clearFocus();

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




                        mobile_autoComplete_widget.clearFocus();

                        mobile_autoComplete_widget.dismissDropDown();

                        newOrderScreenFragment_mobile.CallAdapter();

                    }
                    return false;
                }
            };
            return new Handler(callback);
        }

    }

}
