package com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.meatchop.tmcpartner.Constants.TAG;

public class Adapter_AutoCompleteMenuItem extends ArrayAdapter<Modal_NewOrderItems> {
    String menulist;
    private  List<Modal_NewOrderItems> menuListFull=new ArrayList<>();
    private  Context context;
    private Handler handler;
    int currentPosition;
public Adapter_AutoCompleteMenuItem(@NonNull Context context, @NonNull String menuList, int adapterPosition) {
        super(context, 0);
        this.menulist=menuList;
        convertMenuStringtoJson(menulist);
        this.currentPosition=adapterPosition;
        this.context=context;
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



            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            //Log.i(TAG," Auto menuListFull on click"+menuListFull.size());
            //Log.d("TAG", "itemInCart in Adapter menuItem gstpercentage" + Objects.requireNonNull(menuuItem).getItemname());
            //Log.d("TAG", "itemInCart in Adapter menuItem gstpercentage" + menuuItem.getTmcprice());

            Modal_NewOrderItems modal_newOrderItems = new Modal_NewOrderItems();
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
            try{
                modal_newOrderItems.setDiscountpercentage(String.valueOf(menuuItem.getDiscountpercentage()));

            }
            catch(Exception e ){
                Toast.makeText(context,"Can't Get Menu Discout Percentage at AutoComplete Menu Adapter ",Toast.LENGTH_LONG).show();

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
         //   int last_index =  (NewOrders_MenuItem_Fragment.cart_Item_List.size()-1);
          //  NewOrders_MenuItem_Fragment.cart_Item_List.set(last_index,modal_newOrderItems);
        //    NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();
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
            Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cartItem_hashmap.get(itemUniquecode);
            int quantity = Integer.parseInt(modal_newOrderItems.getQuantity());
            quantity = quantity + 1;

            double itemPrice_quantityBased = Double.parseDouble(modal_newOrderItems.getItemPrice_quantityBased());
            double finalprice = quantity * itemPrice_quantityBased;

            modal_newOrderItems.setItemFinalPrice(String.valueOf(decimalFormat.format(finalprice)));
            modal_newOrderItems.setQuantity(String.valueOf(quantity));
            NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,modal_newOrderItems);
            if(checkforBarcodeInCart("empty")) {
                NewOrders_MenuItem_Fragment.cart_Item_List.remove("empty");

                NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
            }


        }
        else {
            if(checkforBarcodeInCart("empty")) {
                NewOrders_MenuItem_Fragment.cart_Item_List.remove("empty");

                NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
            }

            NewOrders_MenuItem_Fragment.cart_Item_List.add(itemUniquecode);
            NewOrders_MenuItem_Fragment.cartItem_hashmap.put(itemUniquecode,newItem_newOrdersPojoClass);

        }
        NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();


    }




    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: NewOrders_MenuItem_Fragment.cart_Item_List) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }
    private void    convertMenuStringtoJson(String menulist) {
        try {
            //converting jsonSTRING into array
            String tmcsubctgykey="";;
            JSONObject jsonObject = new JSONObject(menulist);
            JSONArray JArray  = jsonObject.getJSONArray("content");
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
                    newOrdersPojoClass.tmcpriceperkg =String.valueOf(json.get("tmcpriceperkg"));
                    newOrdersPojoClass.grossweight =String.valueOf(json.get("grossweight"));
                    newOrdersPojoClass.netweight =String.valueOf(json.get("netweight"));
                    newOrdersPojoClass.tmcprice =String.valueOf(json.get("tmcprice"));
                    newOrdersPojoClass.gstpercentage =String.valueOf(json.get("gstpercentage"));
                    newOrdersPojoClass.portionsize =String.valueOf(json.get("portionsize"));
                    newOrdersPojoClass.pricetypeforpos =String.valueOf(json.get("pricetypeforpos"));
                    newOrdersPojoClass.itemuniquecode =String.valueOf(json.get("itemuniquecode"));
                    newOrdersPojoClass.menuItemId =String.valueOf(json.get("key"));
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