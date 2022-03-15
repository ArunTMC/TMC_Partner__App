package com.meatchop.tmcpartner.Settings;

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

import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter_AutoCompleteMenuitemForDunzoOrders extends ArrayAdapter<Modal_NewOrderItems> {
    List<Modal_MenuItem_Settings> Menulist = new ArrayList<>();;
    private  List<Modal_NewOrderItems> menuListFull=new ArrayList<>();
    private Context context;
    private Handler handler;
    int currentPosition;
    public Adapter_AutoCompleteMenuitemForDunzoOrders(@NonNull Context context, @NonNull List<Modal_MenuItem_Settings> menuList, int adapterPosition) {
        super(context, 0);
        this.Menulist=menuList;
        this.currentPosition=adapterPosition;
        this.context=context;
        AddDatatoMenulistfull();
    }


    private void AddDatatoMenulistfull() {
        for(int i =0; i<Menulist.size();i++){
            Modal_MenuItem_Settings modal_menuItem_settings = Menulist.get(i);
            Modal_NewOrderItems modal_newOrderItems = new Modal_NewOrderItems ();
            modal_newOrderItems.itemname = modal_menuItem_settings.getItemname();
            modal_newOrderItems.tmcpriceperkg = modal_menuItem_settings.getTmcpriceperkg();

            modal_newOrderItems.grossweight = modal_menuItem_settings.getGrossweight().toString().replaceAll("[^\\d.]", "");
            modal_newOrderItems.netweight = modal_menuItem_settings.getNetweight();
            modal_newOrderItems.tmcprice = modal_menuItem_settings.getTmcpriceperkg();
            modal_newOrderItems.tmcpriceperkg = modal_menuItem_settings.getTmcprice();
            modal_newOrderItems.gstpercentage = modal_menuItem_settings.getGstpercentage();
            modal_newOrderItems.portionsize = modal_menuItem_settings.getPortionsize();
            modal_newOrderItems.pricetypeforpos = modal_menuItem_settings.getPricetypeforpos();
            modal_newOrderItems.itemuniquecode = modal_menuItem_settings.getItemuniquecode();
            modal_newOrderItems.key = modal_menuItem_settings.getKey();
            modal_newOrderItems.tmcsubctgykey = String.valueOf(modal_menuItem_settings.getTmcsubctgykey());
            modal_newOrderItems.menuItemId = String.valueOf(modal_menuItem_settings.getMenuItemId());
            modal_newOrderItems.swiggyprice = modal_menuItem_settings.getSwiggyprice();
            modal_newOrderItems.bigbasketprice = modal_menuItem_settings.getBigbasketprice();
            modal_newOrderItems.dunzoprice = modal_menuItem_settings.getDunzoprice();

            modal_newOrderItems.itemavailability = String.valueOf(modal_menuItem_settings.getItemavailability());

            modal_newOrderItems.barcode_AvlDetails = String.valueOf(modal_menuItem_settings.getBarcode_AvlDetails());

            modal_newOrderItems.itemavailability_AvlDetails = String.valueOf(modal_menuItem_settings.getItemavailability_AvlDetails());

            modal_newOrderItems.key_AvlDetails = String.valueOf(modal_menuItem_settings.getKey_AvlDetails());


            modal_newOrderItems.lastupdatedtime_AvlDetails = String.valueOf(modal_menuItem_settings.getLastupdatedtime_AvlDetails());


            modal_newOrderItems.menuitemkey_AvlDetails = String.valueOf(modal_menuItem_settings.getMenuitemkey_AvlDetails());
            modal_newOrderItems.receivedstock_AvlDetails = String.valueOf(modal_menuItem_settings.getReceivedstock_AvlDetails());
            modal_newOrderItems.stockbalance_AvlDetails = String.valueOf(modal_menuItem_settings.getStockbalance_AvlDetails());
            modal_newOrderItems.stockincomingkey_AvlDetails = String.valueOf(modal_menuItem_settings.getStockincomingkey_AvlDetails());
            modal_newOrderItems.vendorkey_AvlDetails = String.valueOf(modal_menuItem_settings.getVendorkey_AvlDetails());


            modal_newOrderItems.barcode = String.valueOf(modal_menuItem_settings.getBarcode());
            modal_newOrderItems.tmcctgykey = String.valueOf(modal_menuItem_settings.getTmcctgykey());
            modal_newOrderItems.inventorydetails = String.valueOf(modal_menuItem_settings.getInventorydetails());
            modal_newOrderItems.allownegativestock = String.valueOf(modal_menuItem_settings.getAllownegativestock());



            modal_newOrderItems.discountpercentage ="0";




            menuListFull.add(modal_newOrderItems);
        }

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
                    R.layout.neworders_autocomplete_text_item, parent, false
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

                double item_total =0;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                //Log.i(TAG," Auto menuListFull on click"+menuListFull.size());
                //Log.d("TAG", "itemInCart in Adapter menuItem gstpercentage" + Objects.requireNonNull(menuuItem).getItemname());
                //Log.d("TAG", "itemInCart in Adapter menuItem gstpercentage" + menuuItem.getTmcprice());

                Modal_NewOrderItems modal_newOrderItems = new Modal_NewOrderItems();
                if (String.valueOf(menuuItem.getPricetypeforpos()).equals("tmcpriceperkg")) {

                    modal_newOrderItems.istmcpriceperkgitemedited = "FALSE";
                }
                else{
                    modal_newOrderItems.istmcpriceperkgitemedited = "TRUE";

                }
                //Log.d("TAG", "itemInCart in Adapter menuItem getTmcpriceperkg 1 " + menuuItem.getTmcpriceperkg());
                //Log.d("TAG", "itemInCart in Adapter menuItem getItemFinalPrice 1 " +  menuuItem.getPricePerItem());
                modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getDunzoprice()))));
                modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getDunzoprice()))));
                //    modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));
                modal_newOrderItems.setItemFinalWeight(String.valueOf(""));



                if (String.valueOf(menuuItem.getPricetypeforpos()).equals("tmcpriceperkg")) {
                    int priceperKg = Integer.parseInt(String.valueOf(menuuItem.getDunzoprice()));
                    String weight_string =String.valueOf(menuuItem.getGrossweight());
                    weight_string =weight_string .replaceAll("[^\\d.]", "");

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

                        item_total =0;
                        modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(item_total)));
                        modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(item_total)));
                        // modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(""));

                        //Log.e("TAg", "weight item_total" + item_total);



                    }

                    if (weight == 1000) {


                        priceperKg=0;
                        priceperKg = Integer.parseInt(menuuItem.getDunzoprice());

                        modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(priceperKg)));
                        modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(priceperKg)));
                        // modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(""));


                    }

                    if (weight > 1000) {
                        priceperKg = Integer.parseInt(menuuItem.getDunzoprice());

                        int itemquantity = weight - 1000;

                        item_total = (priceperKg * itemquantity) / 1000;
                        item_total = Double.parseDouble(decimalFormat.format(item_total));



                        double total = priceperKg + item_total;
                        total = Double.parseDouble(decimalFormat.format((total)));
                        total =0.0;
                        modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(total)));
                        modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(total)));
                        //   modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(""));


                    }


                }


                try{
                    modal_newOrderItems.setDiscountpercentage(String.valueOf(menuuItem.getDiscountpercentage()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu Discout Percentage at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();
                    modal_newOrderItems.setDiscountpercentage("0");
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
                    modal_newOrderItems.setMenuItemId(String.valueOf(menuuItem.getMenuItemId()));

                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu Item ID at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

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
                    modal_newOrderItems.setSwiggyprice(String.valueOf(menuuItem.getSwiggyAmount()));

                }
                catch (Exception e){
                    //    Toast.makeText(context,"Can't Get Menu Item Swiggy Price at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try {
                    modal_newOrderItems.setBigbasketprice(String.valueOf(menuuItem.getBigbasketAmount()));

                }
                catch (Exception e){
                    //    Toast.makeText(context,"Can't Get Menu Item Swiggy Price at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }
                try {
                    modal_newOrderItems.setDunzoprice(String.valueOf(menuuItem.getDunzoprice()));

                }
                catch (Exception e){
                    //    Toast.makeText(context,"Can't Get Menu Item Swiggy Price at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

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
                    modal_newOrderItems.setTmcctgykey(String.valueOf(menuuItem.getTmcctgykey()));

                }
                catch(Exception e){
                    Toast.makeText(context,"Can't Get Menu Item ctgykey at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }


                try {
                    modal_newOrderItems.setItemcutdetails(String.valueOf(menuuItem.getItemcutdetails()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item CutDetails  at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setItemweightdetails(String.valueOf(menuuItem.getItemweightdetails()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item Weight  at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try {
                    modal_newOrderItems.setInventorydetails(String.valueOf(menuuItem.getInventorydetails()));

                }
                catch (Exception e){
                    Toast.makeText(context,"Can't Get Menu Item inventorydetails  at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }



                try{
                    modal_newOrderItems.setBarcode_AvlDetails(String.valueOf(menuuItem.getBarcode_AvlDetails()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu Barcode AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }
                try{
                    modal_newOrderItems.setItemavailability(String.valueOf(menuuItem.getItemavailability()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu ItemAvailability AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

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
                    modal_newOrderItems.setKey_AvlDetails("nil");
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
                    Toast.makeText(context,"Can't Get Menu allownegativestock at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

                }

                try{
                    modal_newOrderItems.setKey(String.valueOf(menuuItem.getKey()));

                }
                catch(Exception e ){
                    Toast.makeText(context,"Can't Get Menu key AvlDetails at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

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
                //   int last_index =  (AddDunzoOrders.cart_Item_List.size()-1);
                //  AddDunzoOrders.cart_Item_List.set(last_index,modal_newOrderItems);
                //    AddDunzoOrders.adapter_cartItem_recyclerview.notifyDataSetChanged();
                //Log.d("TAG", "itemInCart in Adapter menuItem getTmcpriceperkg 2 " + menuuItem.getTmcpriceperkg());
                //Log.d("TAG", "itemInCart in Adapter menuItem getItemFinalPrice 2 " +  menuuItem.getPricePerItem());


                sendHandlerMessage("dismissdropdown");

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
            Modal_NewOrderItems modal_newOrderItems = AddDunzoOrders.cartItem_hashmap.get(itemUniquecode);
            int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
            quantity = quantity + 1;

            double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
            double finalprice = quantity * itemPrice_quantityBased;







            modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
            modal_newOrderItems.setQuantity(String.valueOf(quantity));
            AddDunzoOrders.cartItem_hashmap.put(itemUniquecode,modal_newOrderItems);
            if(checkforBarcodeInCart("empty")) {
                AddDunzoOrders.cart_Item_List.remove("empty");

                AddDunzoOrders.cartItem_hashmap.remove("empty");
            }


        }
        else {
            if(checkforBarcodeInCart("empty")) {
                AddDunzoOrders.cart_Item_List.remove("empty");

                AddDunzoOrders.cartItem_hashmap.remove("empty");
            }

            AddDunzoOrders.cart_Item_List.add(itemUniquecode);
            AddDunzoOrders.cartItem_hashmap.put(itemUniquecode,newItem_newOrdersPojoClass);

        }
        AddDunzoOrders.recyclerView.post(new Runnable()
        {
            @Override
            public void run() {
                AddDunzoOrders.adapter_addDunzoOrdersRecyclerview.notifyDataSetChanged();
            }
        });


    }




    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: AddDunzoOrders.cart_Item_List) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }
    /*
    private void   convertMenuStringtoJson(String menulist) {
        try {
            //converting jsonSTRING into array
            List  <Modal_NewOrderItems> MenuItem = new ArrayList<>();
            Gson gson = new Gson();
            if (menulist.isEmpty()) {
                Toast.makeText(context,"There is something error",Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<Modal_MenuItem_Settings>>() {
                }.getType();
                MenuItem  = gson.fromJson(menulist, type);
            }

            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);

            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for(int i1=0;i1<(MenuItem.size());i1++) {

                try {
                    Modal_NewOrderItems datafromArray = new Modal_NewOrderItems();

                    Modal_NewOrderItems newOrdersPojoClass = new Modal_NewOrderItems();
                    newOrdersPojoClass.itemname =String.valueOf(datafromArray.getItemname());
                    newOrdersPojoClass.tmcpriceperkg =String.valueOf(datafromArray.getTmcpriceperkg());
                    newOrdersPojoClass.grossweight =String.valueOf(datafromArray.getGrossweight());
                    newOrdersPojoClass.netweight =String.valueOf(datafromArray.getNetweight());
                    newOrdersPojoClass.tmcprice =String.valueOf(datafromArray.getTmcprice());
                    newOrdersPojoClass.gstpercentage =String.valueOf(datafromArray.getGstpercentage());
                    newOrdersPojoClass.portionsize =String.valueOf(datafromArray.getPortionsize());
                    newOrdersPojoClass.pricetypeforpos =String.valueOf(datafromArray.getPricetypeforpos());
                    newOrdersPojoClass.itemuniquecode =String.valueOf(datafromArray.getItemuniquecode());
                    newOrdersPojoClass.menuItemId =String.valueOf(datafromArray.getMenuItemId());
                    newOrdersPojoClass.discountpercentage ="0";
                   try{
                        newOrdersPojoClass.tmcsubctgykey = String.valueOf(datafromArray.getTmcsubctgykey());

                    }
                    catch (Exception e){
                        newOrdersPojoClass.tmcsubctgykey = "0";
                        Toast.makeText(context,"TMC tmcsubctgykey Json is Missing",Toast.LENGTH_LONG).show();

                    }
                    //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                    menuListFull.add(newOrdersPojoClass);

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + menuListFull);



                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                    //Log.d(Constants.TAG, "e: " + e.toString());

                }


            }

            //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + menuListFull);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/
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