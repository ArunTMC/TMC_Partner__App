package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Replacement_RefundClasses;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders.NewOrderScreenFragment_mobile;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter_AutoCompleteMenuItem_ReplacementScreen extends ArrayAdapter<Modal_NewOrderItems> {
    String menulist,ordertype_Old="";
    private List<Modal_NewOrderItems> menuListFull=new ArrayList<>();
    private Context context;
    private Handler handler;
    int currentPosition;
    AddReplacement_Refund_OrdersScreen addReplacement_refund_ordersScreen;


    public Adapter_AutoCompleteMenuItem_ReplacementScreen(@NonNull Context context, @NonNull String menuList, int adapterPosition, String ordertype_Oldd, AddReplacement_Refund_OrdersScreen addReplacement_refund_ordersScreen) {
        super(context, 0);
        this.menulist=menuList;
        convertMenuStringtoJson(menulist);
        this.currentPosition=adapterPosition;
        this.context=context;
        this.addReplacement_refund_ordersScreen = addReplacement_refund_ordersScreen;
        this.ordertype_Old= ordertype_Oldd;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return menuFilter;
    }

    public void setHandler(Handler handler) { this.handler = handler; }
    private void sendHandlerMessage(String bundlestr) {
        //Log.i(Constants.TAG,"createBillDetails in AutoComplete");

        Message msg =  new Message();
        Bundle bundle = new Bundle();
        bundle.putString("dropdown", bundlestr);
        msg.setData(bundle);



        handler.sendMessage(msg);

    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.neworders_autocomplete_text_item_mobile, parent, false
            );
        }

        TextView menuItemName_widget = convertView.findViewById(R.id.menuItemName_widget);
        Modal_NewOrderItems menuuItem = getItem(position);
        menuItemName_widget.setText(menuuItem.getItemname());
        CardView menuItem_CardLayout = convertView.findViewById(R.id.addItem_to_Cart_Layout);

        //Log.d(TAG, "Auto 2  menu in  Menulist"+menuListFull.size());




        menuItem_CardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                //Log.i(TAG," Auto menuListFull on click"+menuListFull.size());
                //Log.d("TAG", "itemInCart in Adapter menuItem gstpercentage" + Objects.requireNonNull(menuuItem).getItemname());
                //Log.d("TAG", "itemInCart in Adapter menuItem gstpercentage" + menuuItem.getTmcprice());

                Modal_NewOrderItems modal_newOrderItems = new Modal_NewOrderItems();
          /*
                if(String.valueOf(menuuItem.getPricetypeforpos()).equals("tmcprice")){
                    modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                    modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                    modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));

                }
                if(String.valueOf(menuuItem.getPricetypeforpos()).equals("tmcpriceperkg")) {
                    modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                    modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                    modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));

                    //Log.d("TAG", "itemInCart in Adapter menuItem getTmcpriceperkg 1 " + menuuItem.getTmcpriceperkg());
                    //Log.d("TAG", "itemInCart in Adapter menuItem getItemFinalPrice 1 " +  menuuItem.getPricePerItem());



                }

           */
                if(String.valueOf(menuuItem.getPricetypeforpos()).equals("tmcprice")){
                    if(ordertype_Old.toUpperCase().equals(Constants.APPORDER) || ordertype_Old.toUpperCase().equals(Constants.PhoneOrder)) {

                        modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcpriceWithMarkupValue()))));
                        modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcpriceWithMarkupValue()))));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));

                    }
                    else{
                        modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                        modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));

                    }

                }
                if(String.valueOf(menuuItem.getPricetypeforpos()).equals("tmcpriceperkg")) {

                    if(ordertype_Old.toUpperCase().equals(Constants.APPORDER) || ordertype_Old.toUpperCase().equals(Constants.PhoneOrder)) {
                        modal_newOrderItems.itemFinalPrice = (String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcpriceWithMarkupValue()))));
                        modal_newOrderItems.itemPrice_quantityBased = (String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcpriceWithMarkupValue()))));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));

                    } else {
                        modal_newOrderItems.itemFinalPrice = (String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                        modal_newOrderItems.itemPrice_quantityBased = (String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getTmcprice()))));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));

                    }
                }




                try{
                    modal_newOrderItems.setDiscountpercentage(String.valueOf(menuuItem.getDiscountpercentage()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu Discout Percentage at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setItemavailability(String.valueOf(menuuItem.getItemavailability()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu item Availa at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try{
                    if(menuuItem.getBarcode().equals("")){
                        modal_newOrderItems.setBarcode("0");

                    }
                    else {
                        modal_newOrderItems.setBarcode(String.valueOf(((menuuItem.getBarcode()))));
                    }
                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu TMC Barcode at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    if(menuuItem.getTmcctgykey().equals("")){
                        modal_newOrderItems.setTmcctgykey("0");

                    }
                    else {
                        modal_newOrderItems.setTmcctgykey(String.valueOf(((menuuItem.getTmcctgykey()))));
                    }
                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu TMC Ctgy Key at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }




                try{
                    if(menuuItem.getInventorydetails().equals("")){
                        modal_newOrderItems.setInventorydetails("nil");

                    }
                    else {
                        modal_newOrderItems.setInventorydetails(String.valueOf(((menuuItem.getInventorydetails()))));
                    }
                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu TMC inventoryDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try{
                    if(menuuItem.getTmcsubctgykey().equals("")){
                        modal_newOrderItems.setTmcsubctgykey("0");

                    }
                    else {
                        modal_newOrderItems.setTmcsubctgykey(String.valueOf(((menuuItem.getTmcsubctgykey()))));
                    }
                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu TMC SubCtgyKey at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try{
                    modal_newOrderItems.setItemavailability(String.valueOf(menuuItem.getItemavailability()));

                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu Item Availability at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setSwiggyprice(String.valueOf(menuuItem.getSwiggyprice()));

                }
                catch(Exception e){
                    modal_newOrderItems.setSwiggyprice("0");
                    Toast.makeText(context,"Can't Get Menu Item ID at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try{
                    modal_newOrderItems.setDunzoprice(String.valueOf(menuuItem.getDunzoprice()));

                }
                catch(Exception e){
                    modal_newOrderItems.setDunzoprice("0");
                    Toast.makeText(context,"Can't Get Menu Item ID at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setBigbasketprice(String.valueOf(menuuItem.getBigbasketprice()));

                }
                catch(Exception e){
                    modal_newOrderItems.setBigbasketprice("0");
                    Toast.makeText(context,"Can't Get Menu Item ID at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setMenuItemId(String.valueOf(menuuItem.getMenuItemId()));

                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu Item ID at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try {
                    modal_newOrderItems.setTmcpriceWithMarkupValue(String.valueOf(menuuItem.getTmcpriceWithMarkupValue()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item TMC Price Markup Value at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setTmcpriceperkgWithMarkupValue(String.valueOf(menuuItem.getTmcpriceperkgWithMarkupValue()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item TMC PricePerKg Markup Value at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try{
                    modal_newOrderItems.setItemname(String.valueOf(menuuItem.getItemname()));

                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu Item Name at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }
                try {
                    modal_newOrderItems.setTmcpriceperkg(String.valueOf(menuuItem.getTmcpriceperkg()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item PriceperKg at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setTmcprice(String.valueOf(menuuItem.getTmcprice()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item TMCPrice at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setGstpercentage(String.valueOf(menuuItem.getGstpercentage()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item GSTPercentage at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setPortionsize(String.valueOf(menuuItem.getPortionsize()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item Portion size at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setGrossweight(String.valueOf(menuuItem.getGrossweight()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item Gross Weight at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setPricetypeforpos(String.valueOf(menuuItem.getPricetypeforpos()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item Pricetype for Pos at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try {
                    modal_newOrderItems.setItemuniquecode(String.valueOf(menuuItem.getItemuniquecode()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item UniqueCode  at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setNetweight(String.valueOf(menuuItem.getNetweight()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu NetWeight  at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setBarcode_AvlDetails(String.valueOf(menuuItem.getBarcode_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu Barcode AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try{
                    modal_newOrderItems.setItemavailability_AvlDetails(String.valueOf(menuuItem.getItemavailability_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu ItemAvailability AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try{
                    modal_newOrderItems.setKey_AvlDetails(String.valueOf(menuuItem.getKey_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu Key AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setLastupdatedtime_AvlDetails(String.valueOf(menuuItem.getLastupdatedtime_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu LastupdatedTime AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setMenuitemkey_AvlDetails(String.valueOf(menuuItem.getMenuitemkey_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu MenuItem AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try{
                    modal_newOrderItems.setReceivedstock_AvlDetails(String.valueOf(menuuItem.getReceivedstock_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu ReceivedStock AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try{
                    modal_newOrderItems.setStockbalance_AvlDetails(String.valueOf(menuuItem.getStockbalance_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu stockBalance AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try{
                    modal_newOrderItems.setStockincomingkey_AvlDetails(String.valueOf(menuuItem.getStockincomingkey_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu stock incoming AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try{
                    modal_newOrderItems.setVendorkey_AvlDetails(String.valueOf(menuuItem.getVendorkey_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu vendorkey AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setAllownegativestock(String.valueOf(menuuItem.getAllownegativestock()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu allownegative AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try {
                    modal_newOrderItems.setQuantity("1");
                    modal_newOrderItems.setSubTotal_perItem("");
                    modal_newOrderItems.setTotal_of_subTotal_perItem("");
                    modal_newOrderItems.setTotalGstAmount("");

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Set Menu Item details at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }
                addItemIntheCart(modal_newOrderItems,modal_newOrderItems.getItemFinalWeight(),modal_newOrderItems.getItemuniquecode());
                //   int last_index =  (AddReplacement_Refund_OrdersScreen.cart_Item_List.size()-1);
                //  AddReplacement_Refund_OrdersScreen.cart_Item_List.set(last_index,modal_newOrderItems);
                //    AddReplacement_Refund_OrdersScreen.adapter_cartItem_recyclerview.notifyDataSetChanged();
                //Log.d("TAG", "itemInCart in Adapter menuItem getTmcpriceperkg 2 " + menuuItem.getTmcpriceperkg());
                //Log.d("TAG", "itemInCart in Adapter menuItem getItemFinalPrice 2 " +  menuuItem.getPricePerItem());



            }

        });

        return convertView;
    }



    private void addItemIntheCart(Modal_NewOrderItems newItem_newOrdersPojoClass, String itemWeight,String itemUniquecode) {
        //     //Log.e(TAG, "Got barcode addItemIntheCart"+isdataFetched);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        boolean IsItemAlreadyAddedinCart ;
        IsItemAlreadyAddedinCart = checkforBarcodeInCart(itemUniquecode);
        if(IsItemAlreadyAddedinCart){
            Modal_NewOrderItems modal_newOrderItems = AddReplacement_Refund_OrdersScreen.cartItem_hashmap.get(itemUniquecode);
            int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
            quantity = quantity + 1;

            double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
            double finalprice = quantity * itemPrice_quantityBased;

            modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
            modal_newOrderItems.setQuantity(String.valueOf(quantity));
            AddReplacement_Refund_OrdersScreen.cartItem_hashmap.put(itemUniquecode,modal_newOrderItems);
            if(checkforBarcodeInCart("empty")) {
                AddReplacement_Refund_OrdersScreen.cart_Item_List.remove("empty");

                AddReplacement_Refund_OrdersScreen.cartItem_hashmap.remove("empty");
            }


        }
        else {
            if(checkforBarcodeInCart("empty")) {
                AddReplacement_Refund_OrdersScreen.cart_Item_List.remove("empty");

                AddReplacement_Refund_OrdersScreen.cartItem_hashmap.remove("empty");
            }

            AddReplacement_Refund_OrdersScreen.cart_Item_List.add(itemUniquecode);
            AddReplacement_Refund_OrdersScreen.cartItem_hashmap.put(itemUniquecode,newItem_newOrdersPojoClass);

        }
        //  AddReplacement_Refund_OrdersScreen.adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();

        sendHandlerMessage("dismissdropdown");

    }




    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: AddReplacement_Refund_OrdersScreen.cart_Item_List) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }
    private void  convertMenuStringtoJson(String menulist) {
        try {
            //converting jsonSTRING into array
            String tmcsubctgykey="";;
            // JSONObject jsonObject = new JSONObject(menulist);
            //JSONArray JArray  = jsonObject.getJSONArray("content");
            JSONArray JArray = new JSONArray(menulist);
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for(;i1<(arrayLength);i1++) {

                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    Modal_NewOrderItems newOrdersPojoClass = new Modal_NewOrderItems();
                    if(json.has("tmcsubctgykey")){
                        newOrdersPojoClass.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                        tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                    }
                    else{
                        newOrdersPojoClass.tmcsubctgykey = "0";
                        tmcsubctgykey = "0";
                        //Log.i("Tag", "TMC tmcsubctgykey Json is Missing"+ String.valueOf(newOrdersPojoClass.getTmcsubctgykey()));
                        Toast.makeText(context,"TMC tmcsubctgykey Json is Missing",Toast.LENGTH_LONG).show();

                    }

                    if(json.has("itemname")){

                        if(tmcsubctgykey.equals("tmcsubctgy_16")){
                            // ItemName =  "Grill House "+String.valueOf(json.get("itemname"));

                            newOrdersPojoClass.itemname = "Grill House "+String.valueOf(json.get("itemname"));
                        }
                        else if(tmcsubctgykey.equals("tmcsubctgy_15")){
                            //  ItemName =  "Ready to Cook "+String.valueOf(json.get("itemname"));

                            newOrdersPojoClass.itemname = "Ready to Cook "+String.valueOf(json.get("itemname"));
                        }
                        else{
                            // ItemName =  String.valueOf(json.get("itemname"));

                            newOrdersPojoClass.itemname = String.valueOf(json.get("itemname"));

                        }

                    }
                    else{
                        newOrdersPojoClass.itemname = "Item Name is Missing";
                        Toast.makeText(context,"TMC itemname Json is Missing",Toast.LENGTH_LONG).show();

                    }

                    //  newOrdersPojoClass.itemname =String.valueOf(json.get("itemname"));


                    try{
                        if(json.has("tmcpriceperkg")){
                            newOrdersPojoClass.tmcpriceperkg =String.valueOf(json.get("tmcpriceperkg"));

                        }
                        else{
                            newOrdersPojoClass.tmcpriceperkg = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.tmcpriceperkg = "";

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("grossweight")){
                            newOrdersPojoClass.grossweight =String.valueOf(json.get("grossweight"));

                        }
                        else{
                            newOrdersPojoClass.grossweight = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.grossweight = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("netweight")){
                            newOrdersPojoClass.netweight =String.valueOf(json.get("netweight"));

                        }
                        else{
                            newOrdersPojoClass.netweight = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.netweight = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("tmcprice")){
                            newOrdersPojoClass.tmcprice =String.valueOf(json.get("tmcprice"));

                        }
                        else{
                            newOrdersPojoClass.tmcprice = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.tmcprice = "";

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("swiggyprice")){
                            newOrdersPojoClass.swiggyprice =String.valueOf(json.get("swiggyprice"));

                        }
                        else{
                            newOrdersPojoClass.swiggyprice = "0";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.swiggyprice = "0";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("dunzoprice")){
                            newOrdersPojoClass.dunzoprice =String.valueOf(json.get("dunzoprice"));

                        }
                        else{
                            newOrdersPojoClass.dunzoprice = "0";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.dunzoprice = "0";

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("bigbasketprice")){
                            newOrdersPojoClass.bigbasketprice =String.valueOf(json.get("bigbasketprice"));

                        }
                        else{
                            newOrdersPojoClass.bigbasketprice = "0";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.bigbasketprice = "0";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("gstpercentage")){
                            newOrdersPojoClass.gstpercentage =String.valueOf(json.get("gstpercentage"));

                        }
                        else{
                            newOrdersPojoClass.gstpercentage = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.gstpercentage = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("portionsize")){
                            newOrdersPojoClass.portionsize =String.valueOf(json.get("portionsize"));

                        }
                        else{
                            newOrdersPojoClass.portionsize = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.portionsize = "";

                        e.printStackTrace();
                    }




                    try{
                        if(json.has("pricetypeforpos")){
                            newOrdersPojoClass.pricetypeforpos =String.valueOf(json.get("pricetypeforpos"));

                        }
                        else{
                            newOrdersPojoClass.pricetypeforpos = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.pricetypeforpos = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("itemuniquecode")){
                            newOrdersPojoClass.itemuniquecode =String.valueOf(json.get("itemuniquecode"));

                        }
                        else{
                            newOrdersPojoClass.itemuniquecode = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.itemuniquecode = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("key")){
                            newOrdersPojoClass.menuItemId =String.valueOf(json.get("key"));

                        }
                        else{
                            newOrdersPojoClass.menuItemId = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.menuItemId = "";

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("itemavailability")){
                            newOrdersPojoClass.itemavailability =String.valueOf(json.get("itemavailability"));

                        }
                        else{
                            newOrdersPojoClass.itemavailability = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.itemavailability = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("barcode_AvlDetails")){
                            newOrdersPojoClass.barcode_AvlDetails =String.valueOf(json.get("barcode_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.barcode_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.barcode_AvlDetails = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("itemavailability_AvlDetails")){
                            newOrdersPojoClass.itemavailability_AvlDetails =String.valueOf(json.get("itemavailability_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.itemavailability_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.itemavailability_AvlDetails = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("key_AvlDetails")){
                            newOrdersPojoClass.key_AvlDetails =String.valueOf(json.get("key_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.key_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.key_AvlDetails = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("lastupdatedtime_AvlDetails")){
                            newOrdersPojoClass.lastupdatedtime_AvlDetails =String.valueOf(json.get("lastupdatedtime_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.lastupdatedtime_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.lastupdatedtime_AvlDetails = "";

                        e.printStackTrace();
                    }




                    try{
                        if(json.has("menuitemkey_AvlDetails")){
                            newOrdersPojoClass.menuitemkey_AvlDetails =String.valueOf(json.get("menuitemkey_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.menuitemkey_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.menuitemkey_AvlDetails = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("receivedstock_AvlDetails")){
                            newOrdersPojoClass.receivedstock_AvlDetails =String.valueOf(json.get("receivedstock_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.receivedstock_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.receivedstock_AvlDetails = "";

                        e.printStackTrace();
                    }




                    try{
                        if(json.has("stockbalance_AvlDetails")){
                            newOrdersPojoClass.stockbalance_AvlDetails =String.valueOf(json.get("stockbalance_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.stockbalance_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.stockbalance_AvlDetails = "";

                        e.printStackTrace();
                    }



                    try{
                        if(json.has("stockincomingkey_AvlDetails")){
                            newOrdersPojoClass.stockincomingkey_AvlDetails =String.valueOf(json.get("stockincomingkey_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.stockincomingkey_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.stockincomingkey_AvlDetails = "";

                        e.printStackTrace();
                    }



                    try{
                        if(json.has("vendorkey_AvlDetails")){
                            newOrdersPojoClass.vendorkey_AvlDetails =String.valueOf(json.get("vendorkey_AvlDetails"));

                        }
                        else{
                            newOrdersPojoClass.vendorkey_AvlDetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.vendorkey_AvlDetails = "";

                        e.printStackTrace();
                    }



                    try{
                        if(json.has("barcode")){
                            newOrdersPojoClass.barcode =String.valueOf(json.get("barcode"));

                        }
                        else{
                            newOrdersPojoClass.barcode = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.barcode = "";

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("tmcctgykey")){
                            newOrdersPojoClass.tmcctgykey =String.valueOf(json.get("tmcctgykey"));

                        }
                        else{
                            newOrdersPojoClass.tmcctgykey = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.tmcctgykey = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("inventorydetails")){
                            newOrdersPojoClass.inventorydetails =String.valueOf(json.get("inventorydetails"));

                        }
                        else{
                            newOrdersPojoClass.inventorydetails = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.inventorydetails = "";

                        e.printStackTrace();
                    }



                    try{
                        if(json.has("allownegativestock")){
                            newOrdersPojoClass.allownegativestock =String.valueOf(json.get("allownegativestock"));

                        }
                        else{
                            newOrdersPojoClass.allownegativestock = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.allownegativestock = "";

                        e.printStackTrace();
                    }
                    try{
                        if(json.has("tmcpriceWithMarkupValue")){
                            newOrdersPojoClass.tmcpriceWithMarkupValue =String.valueOf(json.get("tmcpriceWithMarkupValue"));

                        }
                        else{
                            newOrdersPojoClass.tmcpriceWithMarkupValue = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.tmcpriceWithMarkupValue = "";

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("tmcpriceperkgWithMarkupValue")){
                            newOrdersPojoClass.tmcpriceperkgWithMarkupValue =String.valueOf(json.get("tmcpriceperkgWithMarkupValue"));

                        }
                        else{
                            newOrdersPojoClass.tmcpriceperkgWithMarkupValue = "";
                        }
                    }
                    catch (Exception e) {
                        newOrdersPojoClass.tmcpriceperkgWithMarkupValue = "";

                        e.printStackTrace();
                    }


                    newOrdersPojoClass.discountpercentage ="0";






                    //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                    menuListFull.add(newOrdersPojoClass);

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + menuListFull);



                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                    //Log.d(Constants.TAG, "e: " + e.toString());

                }


            }

            //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + menuListFull);



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Filter menuFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Modal_NewOrderItems> suggestions = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(menuListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Modal_NewOrderItems item : menuListFull) {
                    if (item.getItemname().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }
        @Override
        public CharSequence convertResultToString(Object resultValue) {

            return ((Modal_NewOrderItems) resultValue).getItemname();
        }
    };
}
