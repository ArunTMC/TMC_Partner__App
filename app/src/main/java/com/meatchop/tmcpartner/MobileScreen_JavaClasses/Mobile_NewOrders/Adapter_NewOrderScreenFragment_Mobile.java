package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Adapter_AutoCompleteMenuItem;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Adapter_CartItem_Recyclerview;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.NewOrders_MenuItem_Fragment;
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
    private Context context;
    private String pricetype_of_pos;
    Adapter_AutoCompleteMenuItem_mobile adapter;
    String Menulist;
    public static HashMap<String,Modal_NewOrderItems> itemInCart_Hashmap = new HashMap();
    private Handler handler;
    String fromActivity="";
    AddReplacement_Refund_OrdersScreen addReplacement_refund_ordersScreen;
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


        }
        else
        {



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


                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(Math.round(item_total)) + ".00");
                                newItem_newOrdersPojoClass.setItemPrice_quantityBased(String.valueOf(item_total));
                                newItem_newOrdersPojoClass.setItemFinalWeight(String.valueOf(weight) + "g");
                                newItem_newOrdersPojoClass.setGrossweight((String.valueOf(weight) + "g"));

                                //Log.e("TAg", "weight item_total" + item_total);

                                //itemPrice_Widget.setText(String.valueOf(item_total));

                                newOrderScreenFragment_mobile.CallAdapter();
                            }

                            if (weight == 1000) {
                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(priceperKg) + ".00");
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


                                newItem_newOrdersPojoClass.setItemFinalPrice(String.valueOf(Math.round(total)) + ".00");
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

                    //  NewOrderScreenFragment_mobile.adapter_cartItem_recyclerview.notifyDataSetChanged();
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
        LinearLayout mobile_removeItem_fromCart_widget, mobile_addNewItem_layout,parentLayout;
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
