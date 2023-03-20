package com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.HashMap;
import java.util.List;

public class DiffUtilCallBack extends DiffUtil.Callback{


    private  HashMap<String,Modal_NewOrderItems> cartItem_hashmap_old = new HashMap<>();
    private  HashMap<String,Modal_NewOrderItems> cartItem_hashmap_new = new HashMap<>();
    List<String> cart_Item_List_new ;
    List<String> cart_Item_List_old;
    public DiffUtilCallBack(HashMap<String,Modal_NewOrderItems> mcartItem_hashmap_old, List<String> mcart_Item_List_new, HashMap<String,Modal_NewOrderItems> mcartItem_hashmap_new ,List<String> mcart_Item_List_old) {
        this.cartItem_hashmap_old = mcartItem_hashmap_old;
        this.cartItem_hashmap_new = mcartItem_hashmap_new;
        this.cart_Item_List_new = mcart_Item_List_new;
        this.cart_Item_List_old = mcart_Item_List_old;
    }


    @Override
    public int getOldListSize() {
        return cart_Item_List_old.size();
    }

    @Override
    public int getNewListSize() {
        return cart_Item_List_new.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        boolean result = false;
        if((cartItem_hashmap_old.get(cart_Item_List_old.get(oldItemPosition)).getItemFinalPrice() == cartItem_hashmap_new.get(
                cart_Item_List_new.get(newItemPosition)).getItemFinalPrice()) && (cartItem_hashmap_old.get(cart_Item_List_old.get(oldItemPosition)).getItemFinalWeight() == cartItem_hashmap_new.get(
                cart_Item_List_new.get(newItemPosition)).getItemFinalWeight()) && (cartItem_hashmap_old.get(cart_Item_List_old.get(oldItemPosition)).getBarcode() == cartItem_hashmap_new.get(
                cart_Item_List_new.get(newItemPosition)).getBarcode())){
            result = true;
        }
        else{
            result = false;
        }
        return result;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        boolean result = false;
        if((cartItem_hashmap_old.get(cart_Item_List_old.get(oldItemPosition)).getItemFinalPrice() == cartItem_hashmap_new.get(
                cart_Item_List_new.get(newItemPosition)).getItemFinalPrice()) && (cartItem_hashmap_old.get(cart_Item_List_old.get(oldItemPosition)).getItemFinalWeight() == cartItem_hashmap_new.get(
                cart_Item_List_new.get(newItemPosition)).getItemFinalWeight()) && (cartItem_hashmap_old.get(cart_Item_List_old.get(oldItemPosition)).getBarcode() == cartItem_hashmap_new.get(
                cart_Item_List_new.get(newItemPosition)).getBarcode())){
            result = true;
        }
        else{
            result = false;
        }
        return result;
    }


    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        Log.d("TAG", "getChangePayload: oldItemPosition  " +oldItemPosition);
        Log.d("TAG", "getChangePayload: newItemPosition  " +newItemPosition);


        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
