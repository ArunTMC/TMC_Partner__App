package com.meatchop.tmcpartner.Settings;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.NewOrders_MenuItem_Fragment;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter_AutoCompleteMenuitemForSwiggyOrders extends ArrayAdapter<Modal_NewOrderItems> {
    List<Modal_MenuItem_Settings>Menulist = new ArrayList<>();;
    private  List<Modal_NewOrderItems> menuListFull=new ArrayList<>();
    private Context context;
    private Handler handler;
    int currentPosition;
    public Adapter_AutoCompleteMenuitemForSwiggyOrders(@NonNull Context context, @NonNull List<Modal_MenuItem_Settings> menuList, int adapterPosition) {
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
            modal_newOrderItems.swiggyAmount = modal_menuItem_settings.getSwiggyprice();
            modal_newOrderItems.pricetypeforpos = modal_menuItem_settings.getPricetypeforpos();
            modal_newOrderItems.itemuniquecode = modal_menuItem_settings.getItemuniquecode();
            modal_newOrderItems.key = modal_menuItem_settings.getKey();
            modal_newOrderItems.tmcsubctgykey = String.valueOf(modal_menuItem_settings.getTmcsubctgykey());
            modal_newOrderItems.menuItemId = String.valueOf(modal_menuItem_settings.getMenuItemId());

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
                modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getSwiggyAmount()))));
                modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(Double.parseDouble(menuuItem.getSwiggyAmount()))));
            //    modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));
                modal_newOrderItems.setItemFinalWeight(String.valueOf(""));



                if (String.valueOf(menuuItem.getPricetypeforpos()).equals("tmcpriceperkg")) {
                    int priceperKg = Integer.parseInt(menuuItem.getSwiggyAmount());
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
                        modal_newOrderItems.itemFinalPrice =  ( String.valueOf(decimalFormat.format(priceperKg)));
                        modal_newOrderItems.itemPrice_quantityBased=( String.valueOf(decimalFormat.format(priceperKg)));
                       // modal_newOrderItems.setItemFinalWeight(String.valueOf(menuuItem.getGrossweight()));
                        modal_newOrderItems.setItemFinalWeight(String.valueOf(""));


                    }

                    if (weight > 1000) {
                        priceperKg = Integer.parseInt(menuuItem.getSwiggyAmount());

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
                    modal_newOrderItems.setSwiggyAmount(String.valueOf(menuuItem.getSwiggyAmount()));

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
                //   int last_index =  (AddSwiggyOrders.cart_Item_List.size()-1);
                //  AddSwiggyOrders.cart_Item_List.set(last_index,modal_newOrderItems);
                //    AddSwiggyOrders.adapter_cartItem_recyclerview.notifyDataSetChanged();
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
            Modal_NewOrderItems modal_newOrderItems = AddSwiggyOrders.cartItem_hashmap.get(itemUniquecode);
            int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
            quantity = quantity + 1;

            double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
            double finalprice = quantity * itemPrice_quantityBased;







            modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
            modal_newOrderItems.setQuantity(String.valueOf(quantity));
            AddSwiggyOrders.cartItem_hashmap.put(itemUniquecode,modal_newOrderItems);
            if(checkforBarcodeInCart("empty")) {
                AddSwiggyOrders.cart_Item_List.remove("empty");

                AddSwiggyOrders.cartItem_hashmap.remove("empty");
            }


        }
        else {
            if(checkforBarcodeInCart("empty")) {
                AddSwiggyOrders.cart_Item_List.remove("empty");

                AddSwiggyOrders.cartItem_hashmap.remove("empty");
            }

            AddSwiggyOrders.cart_Item_List.add(itemUniquecode);
            AddSwiggyOrders.cartItem_hashmap.put(itemUniquecode,newItem_newOrdersPojoClass);

        }
        AddSwiggyOrders.recyclerView.post(new Runnable()
        {
            @Override
            public void run() {
                AddSwiggyOrders.adapter_addSwiggyOrdersRecyclerview.notifyDataSetChanged();
            }
        });


    }




    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: AddSwiggyOrders.cart_Item_List) {
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