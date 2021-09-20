package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Modal_RatingOrderDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity_Mobile_NewOrdersScreen extends AppCompatActivity {
    public static HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap();
    public static List<String> cart_Item_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__mobile__new_orders_screen);

        cart_Item_List = new ArrayList<>();
        cart_Item_List.clear();
        cartItem_hashmap.clear();

        try{
            Bundle b = getIntent().getExtras();
            cart_Item_List = (ArrayList<String>) b.getSerializable("cart_Item_List");
            cartItem_hashmap = (HashMap<String, Modal_NewOrderItems>) b.getSerializable("cartItem_hashmap");

            Toast.makeText(CartActivity_Mobile_NewOrdersScreen.this, "cart_Item_List  "+String.valueOf(cart_Item_List.size()), Toast.LENGTH_SHORT).show();
            Toast.makeText(CartActivity_Mobile_NewOrdersScreen.this, "cartItem_hashmap   "+String.valueOf(cartItem_hashmap.size()), Toast.LENGTH_SHORT).show();


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}