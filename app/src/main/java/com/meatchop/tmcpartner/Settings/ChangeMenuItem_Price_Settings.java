package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meatchop.tmcpartner.Constants.TAG;

public class ChangeMenuItem_Price_Settings extends AppCompatActivity {
    String pricetypeforPos,menuitemKey, itemUniqueCode, itemName, grossweight,grossweightingrams,portionsize,netweight,
            appPrice, posPrice, appliedDiscountPercentage,finalweight;

    String MarinadeepricetypeforPos,MarinadeemenuitemKey, MarinadeeitemUniqueCode, MarinadeeitemName, Marinadeegrossweight,Marinadeegrossweightingrams,Marinadeeportionsize,Marinadeenetweight,
            MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage,Marinadeefinalweight;
    
    
    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    LinearLayout loadingPanel, loadingpanelmask;
    TextView pricetype_ofItem_text_widget,itemUniqueCode_text_widget, itemName_text_widget, grossweight_text_widget, appPrice_text_widget, posPrice_text_widget;
    EditText appliedDiscountPercentage_text_widget, selling_price_text_widget;
    Button saveDetails,computeAppandPosPrice;
    double sellingprice = 0,sellingprice_withoutDiscount = 0,  sellingPriceWithoutDiscount = 0, grossweightdouble = 0, posPricedouble = 0, appPricedouble = 0;
    int discount_percentage =0;
    double Marinadesellingprice = 0,Marinadesellingprice_withoutDiscount = 0,  MarinadesellingPriceWithoutDiscount = 0, Marinadegrossweightdouble = 0, MarinadeposPricedouble = 0, MarinadeappPricedouble = 0;

    public static List<Modal_MenuItem_Settings> marinadeMenuList=new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_menu_item__price__settings_activity);

        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        saveDetails = findViewById(R.id.saveDetails);
        selling_price_text_widget = findViewById(R.id.selling_price_text_widget);
        itemUniqueCode_text_widget = findViewById(R.id.itemUniqueCode_text_widget);
        itemName_text_widget = findViewById(R.id.itemName_text_widget);
        grossweight_text_widget = findViewById(R.id.grossweight_text_widget);
        appPrice_text_widget = findViewById(R.id.appPrice_text_widget);
        posPrice_text_widget = findViewById(R.id.posPrice_text_widget);
        pricetype_ofItem_text_widget = findViewById(R.id.pricetype_ofItem_text_widget);
        appliedDiscountPercentage_text_widget = findViewById(R.id.appliedDiscountPercentage_text_widget);
        computeAppandPosPrice = findViewById(R.id.computeAppandPosPrice);
        getMarinadeeMenuItemArrayFromSharedPreferences();
        loadingPanel = findViewById(R.id.loadingPanel);
        Adjusting_Widgets_Visibility(true);
        getMenuItemArrayFromSharedPreferences();
        try {
            menuitemKey = getIntent().getStringExtra("menuItemKey");
            getMenuItembasedOnkey(menuitemKey);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Error in getting Menu Item from Cache", Toast.LENGTH_LONG).show();

        }
        appliedDiscountPercentage_text_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    computeAppandPosPrice.performClick();

                }
                return false;
            }
        });
        computeAppandPosPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selling_price_text_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0 ){
                    if(appPrice_text_widget.getText().length() > 0 && posPrice_text_widget.getText().toString().length() > 0) {
                        Adjusting_Widgets_Visibility(true);
                        try {
                            sellingprice = Double.parseDouble(selling_price_text_widget.getText().toString());
                            discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                            if(discount_percentage<=100) {
                                CalculateMarinadeeAppPriceAndPosPrice(sellingprice, discount_percentage);
                                CalculateAppPriceAndPosPrice(sellingprice, discount_percentage);
                            }
                            else{
                                Toast.makeText(ChangeMenuItem_Price_Settings.this,"Discount Percentage can't be greater than 100 %",Toast.LENGTH_SHORT).show();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(ChangeMenuItem_Price_Settings.this,"Price Fields can't be empty",Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(ChangeMenuItem_Price_Settings.this,"Mandatory Fields can't be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selling_price_text_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0){
                        if(appPrice_text_widget.getText().length() > 0 && posPrice_text_widget.getText().toString().length() > 0) {
                            Adjusting_Widgets_Visibility(true);
                            try {
                                sellingprice = Double.parseDouble(selling_price_text_widget.getText().toString());
                                discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                CalculateMarinadeeAppPriceAndPosPrice(sellingprice, discount_percentage);

                                CalculateAppPriceAndPosPrice(sellingprice, discount_percentage);

                                ChangeMarinadeMenuItemPriceInDB(MarinadeemenuitemKey, MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage);

                                ChangeMenuItemPriceInDB(menuitemKey, appPrice, posPrice, appliedDiscountPercentage);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            Toast.makeText(ChangeMenuItem_Price_Settings.this,"Price Fields can't be empty",Toast.LENGTH_SHORT).show();
                        }
                }
                else{
                    Toast.makeText(ChangeMenuItem_Price_Settings.this,"Mandatory Fields can't be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }



    private void CalculateMarinadeeAppPriceAndPosPrice(double sellingprice, int discount_percentage) {
        double sellingPrice_withoutDiscount=0;
        double discountAmount = 0;
        int weightindouble =0;
        int posPriceint =0;
        int appPriceint = 0;

        try {
            discountAmount = (discount_percentage * sellingprice) / 100;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            sellingPrice_withoutDiscount   =  sellingprice -discountAmount;

        }
        catch ( Exception e){
            e.printStackTrace();
        }
        try{
            if (Marinadeefinalweight.matches("[0-9]+") && (Marinadeefinalweight.length() > 0)) {
                weightindouble = Integer.parseInt(Marinadeefinalweight);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (MarinadeepricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                if (weightindouble != 0) {

                    MarinadeappPricedouble = (sellingPrice_withoutDiscount * weightindouble) / 1000;
                    MarinadeposPricedouble = sellingPrice_withoutDiscount;

                } else {
                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if ( MarinadeepricetypeforPos.equals(Constants.TMCPRICE)) {
                MarinadeappPricedouble = sellingPrice_withoutDiscount;
                MarinadeposPricedouble = sellingPrice_withoutDiscount;

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            appPriceint = (int) Math.ceil( MarinadeappPricedouble);
            posPriceint = (int) Math.ceil( MarinadeposPricedouble);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            MarinadeeappPrice = (String.valueOf(appPriceint));
            MarinadeeposPrice =  (String.valueOf(posPriceint));
            MarinadeeappliedDiscountPercentage = (String.valueOf(discount_percentage));
        }
        catch (Exception e){
            e.printStackTrace();
        }







    }




    private void CalculateAppPriceAndPosPrice(double sellingprice, int discount_percentage) {
        double sellingPrice_withoutDiscount=0;
        double discountAmount = 0;
        int weightindouble =0;
        int posPriceint =0;
        int appPriceint = 0;

        try {
            discountAmount = (discount_percentage * sellingprice) / 100;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            sellingPrice_withoutDiscount   =  sellingprice -discountAmount;

        }
        catch ( Exception e){
            e.printStackTrace();
        }
        try{
            if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                weightindouble = Integer.parseInt(finalweight);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (pricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                if (weightindouble != 0) {

                    appPricedouble = (sellingPrice_withoutDiscount * weightindouble) / 1000;
                    posPricedouble = sellingPrice_withoutDiscount;

                } else {
                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            if (pricetypeforPos.equals(Constants.TMCPRICE)) {
                appPricedouble = sellingPrice_withoutDiscount;
                posPricedouble = sellingPrice_withoutDiscount;

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            appPriceint = (int) Math.ceil(appPricedouble);
            posPriceint = (int) Math.ceil(posPricedouble);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            appPrice = (String.valueOf(appPriceint));
            posPrice =  (String.valueOf(posPriceint));
            appliedDiscountPercentage = (String.valueOf(discount_percentage));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            appPrice_text_widget.setText(String.valueOf(appPriceint));
            posPrice_text_widget.setText(String.valueOf(posPriceint));


        }
        catch (Exception e){
            e.printStackTrace();
        }

        Adjusting_Widgets_Visibility(false);




    }


    private void ChangeMenuItemPriceInDB(String menuitemKey, String appPrice, String posPrice, String appliedDiscountPercentage) {
        Adjusting_Widgets_Visibility(true);

        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", menuitemKey);
            jsonObject.put("tmcprice", appPrice);
            jsonObject.put("tmcpriceperkg", posPrice);
            jsonObject.put("applieddiscountpercentage", appliedDiscountPercentage);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMenuItemDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                ChangeMenuItemPriceInSharedPreferenes(menuitemKey, appPrice, posPrice, appliedDiscountPercentage);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);
                Toast.makeText(ChangeMenuItem_Price_Settings.this, "Price was Not Updated. Check Your Network Connection,T", Toast.LENGTH_LONG).show();

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }



    private void ChangeMarinadeMenuItemPriceInDB(String menuitemKey, String appPrice, String posPrice, String appliedDiscountPercentage) {
        Adjusting_Widgets_Visibility(true);

        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", menuitemKey);
            jsonObject.put("tmcprice", appPrice);
            jsonObject.put("tmcpriceperkg", posPrice);
            jsonObject.put("applieddiscountpercentage", appliedDiscountPercentage);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMarinadeMenuItemPriceDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                ChangeMarinadeMenuItemPriceInSharedPreferenes(menuitemKey, appPrice, posPrice, appliedDiscountPercentage);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);
                Toast.makeText(ChangeMenuItem_Price_Settings.this, "Price was Not Updated. Check Your Network Connection,T", Toast.LENGTH_LONG).show();

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");

                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }


    private void ChangeMarinadeMenuItemPriceInSharedPreferenes(String menuitemKey, String appPrice, String posPrice, String appliedDiscountPercentage) {
        for (int i = 0; i < marinadeMenuList.size(); i++) {
            Modal_MenuItem_Settings modal_menuItemSettings = marinadeMenuList.get(i);
            String MenuItemkey = modal_menuItemSettings.getKey();
            if (MenuItemkey.equals(menuitemKey)) {
                modal_menuItemSettings.setTmcprice(appPrice);
                modal_menuItemSettings.setTmcpriceperkg(posPrice);
                modal_menuItemSettings.setApplieddiscountpercentage(appliedDiscountPercentage);

                savedMarinadeMenuIteminSharedPrefrences(marinadeMenuList);
                // finish();

            }


        }


    }





    private void ChangeMenuItemPriceInSharedPreferenes(String menuitemKey, String appPrice, String posPrice, String appliedDiscountPercentage) {
        for (int i = 0; i < MenuItem.size(); i++) {
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String MenuItemkey = modal_menuItemSettings.getKey();
            if (MenuItemkey.equals(menuitemKey)) {
                modal_menuItemSettings.setTmcprice(appPrice);
                modal_menuItemSettings.setTmcpriceperkg(posPrice);
                modal_menuItemSettings.setApplieddiscountpercentage(appliedDiscountPercentage);

                savedMenuIteminSharedPrefrences(MenuItem);
               // finish();

            }


        }
        Adjusting_Widgets_Visibility(false);


    }
    private void savedMarinadeMenuIteminSharedPrefrences(List<Modal_MenuItem_Settings> menuItem) {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MarinadeMenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MarinadeMenuList", json);
        editor.apply();
    }
    private void savedMenuIteminSharedPrefrences(List<Modal_MenuItem_Settings> menuItem) {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList", json);
        editor.apply();
    }

    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(), "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem_Settings>>() {
            }.getType();
            MenuItem = gson.fromJson(json, type);
        }

    }




    private void getMarinadeeMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MarinadeMenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MarinadeMenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem_Settings>>() {
            }.getType();
            marinadeMenuList  = gson.fromJson(json, type);
        }

    }

    private void getMenuItembasedOnkey(String key) {
        int finalsellingprice;
        for (int i = 0; i < MenuItem.size(); i++) {
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String menuItemId = modal_menuItemSettings.getMenuItemId();
            if (menuItemId.equals(key)) {
                try {
                    try{
                        appliedDiscountPercentage = String.valueOf(modal_menuItemSettings.getApplieddiscountpercentage());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        posPrice = String.valueOf(modal_menuItemSettings.getTmcpriceperkg());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        appPrice = String.valueOf(modal_menuItemSettings.getTmcprice());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        portionsize =  String.valueOf(modal_menuItemSettings.getPortionsize());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        netweight =  String.valueOf(modal_menuItemSettings.getNetweight());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        grossweight = String.valueOf(modal_menuItemSettings.getGrossweight());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        grossweightingrams = String.valueOf(modal_menuItemSettings.getGrossweightingrams());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        itemName = String.valueOf(modal_menuItemSettings.getItemname());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        itemUniqueCode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                        getMarinadeeItemDetailsUsingUniqueCode(itemUniqueCode);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    try{
                        pricetypeforPos = String.valueOf(modal_menuItemSettings.getPricetypeforpos()).toUpperCase();
                        pricetype_ofItem_text_widget.setText(String.valueOf(pricetypeforPos));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                    try{
                        if(pricetypeforPos.equals(Constants.TMCPRICE)){
                            sellingprice = CalculateSellingPrice(appPrice, appliedDiscountPercentage);


                        }
                        if(pricetypeforPos.equals(Constants.TMCPRICEPERKG)){
                            sellingprice = CalculateSellingPrice(posPrice, appliedDiscountPercentage);
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        finalweight = CalculateWeightoftheItem();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                            grossweight_text_widget.setText(finalweight+" gms");


                        }
                        else{
                            grossweight_text_widget.setText(finalweight);

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                    itemUniqueCode_text_widget.setText(itemUniqueCode);
                    itemName_text_widget.setText(itemName);
                    appPrice_text_widget.setText(appPrice);
                    posPrice_text_widget.setText(posPrice);
                    appliedDiscountPercentage_text_widget.setText(appliedDiscountPercentage);
                    try{
                        finalsellingprice = (int) Math.ceil(sellingprice);
                    }
                    catch (Exception e){
                        finalsellingprice=00;
                        e.printStackTrace();
                    }

                    selling_price_text_widget.setText(String.valueOf(finalsellingprice));


                    //Log.d(Constants.TAG, "displaying_menuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));
                    Adjusting_Widgets_Visibility(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


    }

    private void getMarinadeeItemDetailsUsingUniqueCode(String itemUniqueCode) {
        int Marinadeefinalsellingprice;

        for(int i =0 ;i<marinadeMenuList.size();i++){
            Modal_MenuItem_Settings modal_menuItemSettings = marinadeMenuList.get(i);
             MarinadeeitemUniqueCode = modal_menuItemSettings.getItemuniquecode();   
            if(itemUniqueCode.equals(MarinadeeitemUniqueCode)){
                try {

                    try{
                        MarinadeemenuitemKey = String.valueOf(modal_menuItemSettings.getKey());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                    try{
                        MarinadeeappliedDiscountPercentage = String.valueOf(modal_menuItemSettings.getApplieddiscountpercentage());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        MarinadeeposPrice = String.valueOf(modal_menuItemSettings.getTmcpriceperkg());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        MarinadeeappPrice = String.valueOf(modal_menuItemSettings.getTmcprice());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        Marinadeeportionsize =  String.valueOf(modal_menuItemSettings.getPortionsize());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        Marinadeenetweight =  String.valueOf(modal_menuItemSettings.getNetweight());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        Marinadeegrossweight = String.valueOf(modal_menuItemSettings.getGrossweight());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        Marinadeegrossweightingrams = String.valueOf(modal_menuItemSettings.getGrossweightingrams());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        MarinadeeitemName = String.valueOf(modal_menuItemSettings.getItemname());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        MarinadeeitemUniqueCode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    try{
                        MarinadeepricetypeforPos = String.valueOf(modal_menuItemSettings.getPricetypeforpos()).toUpperCase();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                    try{
                        Marinadeefinalweight = CalculateWeightoftheMarinadeeItem();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        if (Marinadeefinalweight.matches("[0-9]+") && (Marinadeefinalweight.length() > 0)) {


                        }
                        else{

                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }




                    //Log.d(Constants.TAG, "displaying_MarinadeemenuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));
                    Adjusting_Widgets_Visibility(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            
            }
        }
        
        
        
        
        
        
    }
    private String CalculateWeightoftheMarinadeeItem() {
        String weight="";
        try {
            if ((!Marinadeegrossweightingrams.equals("")) && (!Marinadeegrossweightingrams.equals("0")) && (!Marinadeegrossweightingrams.equals(null)) && (!Marinadeegrossweightingrams.equals("null"))) {
                Marinadeegrossweightingrams = Marinadeegrossweightingrams.replaceAll("[^\\d.]", "");

                weight = Marinadeegrossweightingrams;
            } else {
                if ((!Marinadeegrossweight.equals("")) && (!Marinadeegrossweight.equals("0")) && (!Marinadeegrossweight.equals(null)) && (!Marinadeegrossweight.equals("null"))) {
                    Marinadeegrossweight = Marinadeegrossweight.replaceAll("[^\\d.]", "");

                    weight = Marinadeegrossweight;
                } else {
                    if ((!Marinadeeportionsize.equals("")) && (!Marinadeeportionsize.equals("0")) && (!Marinadeeportionsize.equals(null)) && (!Marinadeeportionsize.equals("null"))) {
                        weight = Marinadeeportionsize;
                    } else {
                        weight = "there is no weight ";
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return weight;
    }
   

    private String CalculateWeightoftheItem() {
    String weight="";
    try {
        if ((!grossweightingrams.equals("")) && (!grossweightingrams.equals("0")) && (!grossweightingrams.equals(null)) && (!grossweightingrams.equals("null"))) {
            grossweightingrams = grossweightingrams.replaceAll("[^\\d.]", "");

            weight = grossweightingrams;
        } else {
            if ((!grossweight.equals("")) && (!grossweight.equals("0")) && (!grossweight.equals(null)) && (!grossweight.equals("null"))) {
                grossweight = grossweight.replaceAll("[^\\d.]", "");

                weight = grossweight;
            } else {
                if ((!portionsize.equals("")) && (!portionsize.equals("0")) && (!portionsize.equals(null)) && (!portionsize.equals("null"))) {
                    weight = portionsize;
                } else {
                    weight = "there is no weight ";
                }
            }
        }
    }
    catch (Exception e){
        e.printStackTrace();
    }
        grossweight_text_widget.setText(weight);

    return weight;
    }

    private double CalculateSellingPrice(String tmcPrice, String appliedDiscountPercentage) {
        double sellingPrice = 0;
        double tmcprice = 0;
        double discount = 0;


        try {
             tmcprice = Double.parseDouble(tmcPrice);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


        try{
            discount = Double.parseDouble(appliedDiscountPercentage);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            sellingPrice = tmcprice/(1-(discount/100));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            sellingPrice = Double.parseDouble(decimalFormat.format(sellingPrice));
        }
        catch (Exception e){
            e.printStackTrace();
        }





        return sellingPrice;
    }



    private void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }
    }
}












/*

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appPrice_text_widget.getText().length() > 0 && posPrice_text_widget.getText().toString().length() > 0 && appliedDiscountPercentage_text_widget.getText().toString().length() > 0) {
                    Adjusting_Widgets_Visibility(true);
                    try {
                        appPrice = appPrice_text_widget.getText().toString();
                        double appPrice_double = Double.parseDouble(appPrice);
                        int appprice_int = (int) Math.ceil(appPrice_double);
                        appPrice = String.valueOf(appprice_int);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(ChangeMenuItem_Price_Settings.this,"Enter  APP Price in correct format",Toast.LENGTH_LONG).show();

                    }
                    try {
                        posPrice = posPrice_text_widget.getText().toString();
                        double posPrice_double = Double.parseDouble(posPrice);
                        int posPriceint = (int) Math.ceil(posPrice_double);
                        posPrice = String.valueOf(posPriceint);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ChangeMenuItem_Price_Settings.this,"Enter  POS Price in Correct format",Toast.LENGTH_LONG).show();

                    }
                    try {
                        appliedDiscountPercentage = appliedDiscountPercentage_text_widget.getText().toString();
                    }
                    catch (Exception e ){
                        Toast.makeText(ChangeMenuItem_Price_Settings.this,"Enter  Applied Discount Percentage in Correct format",Toast.LENGTH_LONG).show();

                    }
                    ChangeMenuItemPriceInDB(menuitemKey, appPrice, posPrice, appliedDiscountPercentage);
                }
                else{
                    Toast.makeText(ChangeMenuItem_Price_Settings.this,"Fill all the Mandatory Fields",Toast.LENGTH_LONG).show();
                }
            }

        });




 */
