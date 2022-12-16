package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeMenuItem_Price_Settings extends AppCompatActivity {
    String wholeSalePrice,bigBasketPrice,dunzoPrice,swiggyPrice,pricetypeforPos,menuitemKey, itemUniqueCode, itemName, grossweight,grossweightingrams,portionsize,netweight,
            appPrice, posPrice, appliedDiscountPercentage,finalweight,posPrice_pricePerKg,appPrice_PricePerKg , appMarkupPercentageString ;

    String MarinadeepricetypeforPos,MarinadeemenuitemKey, MarinadeeitemUniqueCode, MarinadeeitemName, Marinadeegrossweight,Marinadeegrossweightingrams,Marinadeeportionsize,Marinadeenetweight,
            MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage,Marinadeefinalweight;


    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    LinearLayout loadingPanel, loadingpanelmask,tmcUnitPrice_layout,tmcpriceperkg_layout;
    TextView appPrice_withoutMarkup_text_widget_appLayout,appPricePerKg_textview_widget_appLayout,wholeSale_price_label,bigBasket_price_label,dunzo_price_label,swiggy_price_label,TMCpriceperkg_app_price_label,TMCpriceperkg_appSelling_price_label,TMCpriceperkg_pos_price_label,TMCprice_pos_price_label,
            TMCprice_selling_price_label,  TMCpriceperkg_posSelling_price_label,pricetype_ofItem_text_widget,itemUniqueCode_text_widget, itemName_text_widget, grossweight_text_widget, appPrice_text_widget, posPrice_text_widget,posPrice_text_widget_posLayout,appPrice_text_widget_appLayout;
    EditText selling_price_common_edittext_widget,appMarkupPercentage_text_widget,wholesale_selling_price_text_widget_wholesaleLayout,appliedDiscountPercentage_text_widget, selling_price_text_widget,pos_selling_price_text_widget_posLayout,app_selling_price_text_widget_appLayout,swiggy_selling_price_text_widget_swiggyLayout,dunzo_selling_price_text_widget_dunzoLayout,bigbasket_selling_price_text_widget_bigbasketLayout;
    Button saveDetails,computeAppandPosPrice;
    double sellingprice = 0,sellingprice_withoutDiscount = 0,  sellingPriceWithoutDiscount = 0, grossweightdouble = 0, posPricedouble = 0, appPricedouble = 0,posPricedouble__priceperKg =0, appPricedouble_tmcprice =0, appSellingPrice_priceperKg =0, posSellingPrice_priceperKg =0;
    int discount_percentage =0 ,appMarkupPercentageInt =0;
    double Marinadesellingprice = 0,Marinadesellingprice_withoutDiscount = 0,  MarinadesellingPriceWithoutDiscount = 0, Marinadegrossweightdouble = 0, MarinadeposPricedouble = 0, MarinadeappPricedouble = 0;

    public static List<Modal_MenuItem_Settings> marinadeMenuList=new ArrayList<>();

    boolean isTMCPrice= false;
    boolean isAppPrice_PricePerKgChanged =false;
    boolean isPosPrice_PricePerKgChanged = false;
    double screenInches;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_menu_item__price__settings_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
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
        tmcpriceperkg_layout = findViewById(R.id.tmcpriceperkg_layout);
        tmcUnitPrice_layout =findViewById(R.id.tmcUnitPrice_layout);
        swiggy_selling_price_text_widget_swiggyLayout =findViewById(R.id.swiggy_selling_price_text_widget_swiggyLayout);
        dunzo_selling_price_text_widget_dunzoLayout = findViewById(R.id.dunzo_selling_price_text_widget_dunzoLayout);
        bigbasket_selling_price_text_widget_bigbasketLayout  =findViewById(R.id.bigbasket_selling_price_text_widget_bigbasketLayout);
        wholesale_selling_price_text_widget_wholesaleLayout  =findViewById(R.id.wholesale_selling_price_text_widget_wholesaleLayout);

        appPricePerKg_textview_widget_appLayout = findViewById(R.id.appPricePerKg_textview_widget_appLayout);
        pos_selling_price_text_widget_posLayout = findViewById(R.id.Pos_selling_price_text_widget_posLayout);
        app_selling_price_text_widget_appLayout = findViewById(R.id.app_selling_price_text_widget_appLayout);
        posPrice_text_widget_posLayout = findViewById(R.id.posPrice_text_widget_posLayout);
        appPrice_text_widget_appLayout = findViewById(R.id.appPrice_text_widget_appLayout);
        selling_price_common_edittext_widget = findViewById(R.id.selling_price_common_edittext_widget);
        appMarkupPercentage_text_widget = findViewById(R.id.appMarkupPercentage_text_widget);
        selling_price_common_edittext_widget  = findViewById(R.id.selling_price_common_edittext_widget);
        appPrice_withoutMarkup_text_widget_appLayout = findViewById(R.id.appPrice_withoutMarkup_text_widget_appLayout);


        TMCprice_selling_price_label =findViewById(R.id.TMCprice_selling_price_label);
        TMCprice_pos_price_label = findViewById(R.id.TMCprice_pos_price_label);
        swiggy_price_label =findViewById(R.id.swiggy_price_label);
        dunzo_price_label = findViewById(R.id.dunzo_price_label);
        bigBasket_price_label = findViewById(R.id.bigbasket_price_label);
        wholeSale_price_label = findViewById(R.id.wholeSale_price_label);
        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(ChangeMenuItem_Price_Settings.this);
            //  Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
                // Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }

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
        /*appliedDiscountPercentage_text_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    computeAppandPosPrice.performClick();

                }
                return false;
            }
        });

         */


        if(isTMCPrice){
            tmcUnitPrice_layout.setVisibility(View.VISIBLE);
            tmcpriceperkg_layout.setVisibility(View.GONE);
            TMCprice_selling_price_label.setText("Selling Price  ( TMC Price ):-   ");
            TMCprice_pos_price_label .setText("POS Price (  TMC Price )  :-   ");
            swiggy_price_label.setText("Swiggy  Price (  TMC Price  )  :-   ");
            dunzo_price_label.setText("Dunzo  Price (  TMC Price  )  :-   ");
            bigBasket_price_label.setText("BigBasket  Price (  TMC Price  )  :-   ");
            wholeSale_price_label.setText("WholeSale  Price (  TMC Price  )  :-   ");

        }
        else{
            tmcUnitPrice_layout.setVisibility(View.GONE);
            tmcpriceperkg_layout.setVisibility(View.VISIBLE);
            TMCprice_selling_price_label.setText("Selling Price  ( Price Per Kg ):-   ");
            TMCprice_pos_price_label .setText("POS Price (  Price Per Kg )  :-   ");
            swiggy_price_label.setText("Swiggy  Price (  Price Per Kg  )  :-   ");
            dunzo_price_label.setText("Dunzo  Price (  Price Per Kg  )  :-   ");
            bigBasket_price_label.setText("BigBasket  Price (  Price Per Kg  )  :-   ");
            wholeSale_price_label.setText("WholeSale  Price (  Price Per Kg  )  :-   ");

        }



        selling_price_text_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    computeAppandPosPrice.performClick();

                }
                return false;
            }
        });
        selling_price_common_edittext_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    //  isPosPrice_PricePerKgChanged = true;
                    //  isAppPrice_PricePerKgChanged = false;
                    computeAppandPosPrice.performClick();

                }
                return false;
            }
        });

        pos_selling_price_text_widget_posLayout.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    isPosPrice_PricePerKgChanged = true;
                    isAppPrice_PricePerKgChanged = false;
                    computeAppandPosPrice.performClick();

                }
                return false;
            }
        });


        app_selling_price_text_widget_appLayout.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    isPosPrice_PricePerKgChanged = false;
                    isAppPrice_PricePerKgChanged = true;
                    computeAppandPosPrice.performClick();

                }
                return false;
            }
        });

        computeAppandPosPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTMCPrice) {
                    if (selling_price_common_edittext_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0  && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0 && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0)  {

                        //   if (selling_price_text_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0  && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0 && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0)  {
                        if (appPrice_text_widget.getText().length() > 0 && posPrice_text_widget.getText().toString().length() > 0) {
                            Adjusting_Widgets_Visibility(true);
                            try {

                                //sellingprice = Double.parseDouble(selling_price_text_widget.getText().toString());

                                sellingprice = Double.parseDouble(selling_price_common_edittext_widget.getText().toString());
                                if(sellingprice>0) {
                                    discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                    appMarkupPercentageInt = Integer.parseInt(appMarkupPercentage_text_widget.getText().toString());

                                    if (discount_percentage <= 100) {
                                        CalculateMarinadeeAppPriceAndPosPrice(sellingprice, discount_percentage);
                                        CalculateAppPriceAndPosPrice(sellingprice, discount_percentage,appMarkupPercentageInt);
                                    } else {
                                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Discount Percentage can't be greater than 100 %", Toast.LENGTH_SHORT).show();

                                        Adjusting_Widgets_Visibility(false);

                                    }
                                }
                                else{
                                    Adjusting_Widgets_Visibility(false);

                                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ChangeMenuItem_Price_Settings.this, "Can't Change the Price", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Adjusting_Widgets_Visibility(false);

                            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Adjusting_Widgets_Visibility(false);

                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //if (isPosPrice_PricePerKgChanged) {
                    isPosPrice_PricePerKgChanged = false;

                    if (selling_price_common_edittext_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0  && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0 && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {

                        // if (pos_selling_price_text_widget_posLayout.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0  && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0 && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                        if (posPrice_text_widget_posLayout.getText().toString().length() > 0) {
                            Adjusting_Widgets_Visibility(true);
                            try {
                                posSellingPrice_priceperKg = Double.parseDouble(selling_price_common_edittext_widget.getText().toString());

                                //  posSellingPrice_priceperKg = Double.parseDouble(pos_selling_price_text_widget_posLayout.getText().toString());
                                if(posSellingPrice_priceperKg>0) {
                                    discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());

                                    if (discount_percentage <= 100) {
                                        CalculatePosPrice(posSellingPrice_priceperKg, discount_percentage);


                                    } else {
                                        Adjusting_Widgets_Visibility(false);

                                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Discount Percentage can't be greater than 100 %", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                else {
                                    Adjusting_Widgets_Visibility(false);

                                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "POS Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

                                }
                            } catch (Exception e) {
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ChangeMenuItem_Price_Settings.this, "Can't Change the Price", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        }
                        else {
                            Adjusting_Widgets_Visibility(false);

                            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Adjusting_Widgets_Visibility(false);

                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                    //  }
                    //    else if(isAppPrice_PricePerKgChanged){
                    isAppPrice_PricePerKgChanged = false;
                    if (selling_price_common_edittext_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {

                        //if (app_selling_price_text_widget_appLayout.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                        if (appPrice_text_widget_appLayout.getText().toString().length() > 0) {
                            try {
                                appSellingPrice_priceperKg = Double.parseDouble(selling_price_common_edittext_widget.getText().toString());
                                if(appSellingPrice_priceperKg>0) {
                                    Adjusting_Widgets_Visibility(true);

                                    discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                    appMarkupPercentageInt = Integer.parseInt(appMarkupPercentage_text_widget.getText().toString());

                                    if (discount_percentage <= 100) {
                                        CalculateAppPrice(appSellingPrice_priceperKg, discount_percentage ,appMarkupPercentageInt);
                                        CalculateMarinadeeAppPriceAndPosPrice(appSellingPrice_priceperKg, discount_percentage );

                                    } else {
                                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Discount Percentage can't be greater than 100 %", Toast.LENGTH_SHORT).show();
                                        Adjusting_Widgets_Visibility(false);

                                    }
                                }
                                else{
                                    Adjusting_Widgets_Visibility(false);

                                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "APP Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(ChangeMenuItem_Price_Settings.this, "Can't Change the Price", Toast.LENGTH_SHORT).show();

                                Adjusting_Widgets_Visibility(false);

                            }
                        }
                        else {
                            Adjusting_Widgets_Visibility(false);

                            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Adjusting_Widgets_Visibility(false);

                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                    //  }

                }
            }
        });
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTMCPrice) {

                    if (selling_price_common_edittext_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {

                        //  if (selling_price_text_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                        if (appPrice_text_widget.getText().length() > 0 && posPrice_text_widget.getText().toString().length() > 0) {
                            try {
                                sellingprice = Double.parseDouble(selling_price_common_edittext_widget.getText().toString());
                                discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                swiggyPrice = String.valueOf(swiggy_selling_price_text_widget_swiggyLayout.getText().toString());
                                dunzoPrice = String.valueOf(dunzo_selling_price_text_widget_dunzoLayout.getText().toString());
                                bigBasketPrice = String.valueOf(bigbasket_selling_price_text_widget_bigbasketLayout.getText().toString());
                                wholeSalePrice= String.valueOf(wholesale_selling_price_text_widget_wholesaleLayout.getText().toString());
                                appMarkupPercentageInt = Integer.parseInt(appMarkupPercentage_text_widget.getText().toString());

                                CalculateMarinadeeAppPriceAndPosPrice(sellingprice, discount_percentage);
                                CalculateAppPriceAndPosPriceWihoutMarkupPrice(sellingprice, discount_percentage);
                                // CalculateAppPriceAndPosPrice(sellingprice, discount_percentage, appMarkupPercentageInt);
                                if(sellingprice>0) {
                                    ChangeMarinadeMenuItemPriceInDB(MarinadeemenuitemKey, MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage);

                                    ChangeMenuItemPriceInDB(menuitemKey, appPrice, posPrice, appliedDiscountPercentage, swiggyPrice,dunzoPrice,bigBasketPrice,wholeSalePrice);
                                }
                                else{
                                    Adjusting_Widgets_Visibility(false);

                                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "Selling Price  can't be Zero", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ChangeMenuItem_Price_Settings.this, "There was a Problem in Changing the Price", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        } else {
                            Adjusting_Widgets_Visibility(false);

                            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Adjusting_Widgets_Visibility(false);

                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else{

                    if (selling_price_common_edittext_widget.getText().length() > 0  && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {

                        // if (pos_selling_price_text_widget_posLayout.getText().length() > 0 &&  app_selling_price_text_widget_appLayout.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && wholesale_selling_price_text_widget_wholesaleLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                        if (appPrice_text_widget_appLayout.getText().length() > 0 && posPrice_text_widget_posLayout.getText().toString().length() > 0) {
                            try {
                                //  appSellingPrice_priceperKg = Double.parseDouble(app_selling_price_text_widget_appLayout.getText().toString());
                                //posSellingPrice_priceperKg = Double.parseDouble(pos_selling_price_text_widget_posLayout.getText().toString());
                                appSellingPrice_priceperKg = Double.parseDouble(selling_price_common_edittext_widget.getText().toString());
                                posSellingPrice_priceperKg = Double.parseDouble(selling_price_common_edittext_widget.getText().toString());

                                discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                swiggyPrice = String.valueOf(swiggy_selling_price_text_widget_swiggyLayout.getText().toString());
                                dunzoPrice = String.valueOf(dunzo_selling_price_text_widget_dunzoLayout.getText().toString());
                                bigBasketPrice = String.valueOf(bigbasket_selling_price_text_widget_bigbasketLayout.getText().toString());
                                wholeSalePrice= String.valueOf(wholesale_selling_price_text_widget_wholesaleLayout.getText().toString());
                                appMarkupPercentageInt = Integer.parseInt(appMarkupPercentage_text_widget.getText().toString());

                                CalculateMarinadeeAppPriceAndPosPrice(appSellingPrice_priceperKg, discount_percentage);

                                CalculateAppPriceWithOutAppMarkupPrice_forUpdate(appSellingPrice_priceperKg, discount_percentage);


                                CalculatePosPrice(posSellingPrice_priceperKg, discount_percentage);
                                if(appSellingPrice_priceperKg>0) {
                                    if (posSellingPrice_priceperKg > 0) {

                                        ChangeMarinadeMenuItemPriceInDB(MarinadeemenuitemKey, MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage);

                                        ChangeMenuItemPriceInDB(menuitemKey, appPrice_PricePerKg, posPrice_pricePerKg, appliedDiscountPercentage, swiggyPrice, dunzoPrice, bigBasketPrice, wholeSalePrice);

                                    }
                                    else{
                                        Adjusting_Widgets_Visibility(false);

                                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "POS Selling Price  can't be Zero", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    Adjusting_Widgets_Visibility(false);

                                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "APP Selling Price  can't be Zero", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ChangeMenuItem_Price_Settings.this, "There was a Problem in Changing the Price", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        } else {
                            Adjusting_Widgets_Visibility(false);

                            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Adjusting_Widgets_Visibility(false);

                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





    }

    private void CalculateAppPriceWithOutAppMarkupPrice_forUpdate(double app_sellingprice, int discount_percentage) {

        if(app_sellingprice>0) {
            Adjusting_Widgets_Visibility(true);

            double sellingPrice_withoutDiscount = 0;
            double discountAmount = 0 ;
            int weightindouble = 0;
            int appPriceint = 0 , sellingPriceInt =0 ;

            try {
                discountAmount = (discount_percentage * app_sellingprice) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sellingPrice_withoutDiscount = app_sellingprice - discountAmount;

            } catch (Exception e) {
                e.printStackTrace();
            }





            try {
                if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                    weightindouble = Integer.parseInt(finalweight);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (pricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                    if (weightindouble != 0) {
                        appPricedouble_tmcprice = (sellingPrice_withoutDiscount * weightindouble) / 1000;

                    }
                    else {
                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
                    }
                }

                else{
                    appPricedouble_tmcprice  = sellingPrice_withoutDiscount;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                appPriceint = (int) Math.round(appPricedouble_tmcprice);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                appPrice_PricePerKg = (String.valueOf(appPriceint));
                appliedDiscountPercentage = (String.valueOf(discount_percentage));
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        else{


            Toast.makeText(ChangeMenuItem_Price_Settings.this, "App Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }

        Adjusting_Widgets_Visibility(false);




    }

    private void CalculatePosPrice(double pos_sellingprice, int discount_percentage) {
        if(pos_sellingprice >0) {
            Adjusting_Widgets_Visibility(true);

            double sellingPrice_withoutDiscount = 0;
            double discountAmount = 0;
            int weightindouble = 0;
            int posPriceint = 0;

            try {
                discountAmount = (discount_percentage * pos_sellingprice) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sellingPrice_withoutDiscount = pos_sellingprice - discountAmount;

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                    weightindouble = Integer.parseInt(finalweight);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (pricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                    if (weightindouble != 0) {

                        posPricedouble__priceperKg = sellingPrice_withoutDiscount;

                    } else {
                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


       /*\ try{
            if (pricetypeforPos.equals(Constants.TMCPRICE)) {
                appPricedouble = sellingPrice_withoutDiscount;
                posPricedouble = sellingPrice_withoutDiscount;

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        */

            try {
                posPriceint = (int) Math.round(posPricedouble__priceperKg);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                posPrice_pricePerKg = (String.valueOf(posPriceint));
                appliedDiscountPercentage = (String.valueOf(discount_percentage));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                posPrice_text_widget_posLayout.setText(String.valueOf(posPriceint));


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else{


            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Pos Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }
        Adjusting_Widgets_Visibility(false);
    }


    private void CalculateAppPrice(double app_sellingprice, int discount_percentage, int appMarkupPercentageInt) {
        if(app_sellingprice>0) {
            Adjusting_Widgets_Visibility(true);

            double sellingPrice_withoutDiscount = 0, tmcpricewithoutdiscount =0;
            double discountAmount = 0 , appMarkupPercentagePrice =0;
            int weightindouble = 0;
            int appPriceint = 0 , sellingPriceInt =0 ;


            //    Log.d("123456  - ", " app_sellingprice  "+app_sellingprice);



            //  Log.d("123456  - ", " discount_percentage   "+discount_percentage);
            try {
                discountAmount = (discount_percentage * app_sellingprice) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sellingPrice_withoutDiscount = app_sellingprice - discountAmount;

            } catch (Exception e) {
                e.printStackTrace();
            }
            try{
                tmcpricewithoutdiscount = (sellingPrice_withoutDiscount);

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                appMarkupPercentagePrice = (appMarkupPercentageInt * sellingPrice_withoutDiscount) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sellingPrice_withoutDiscount = sellingPrice_withoutDiscount + appMarkupPercentagePrice;

            } catch (Exception e) {
                e.printStackTrace();
            }





            try {
                if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                    weightindouble = Integer.parseInt(finalweight);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (pricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                    if (weightindouble != 0) {
                        appPricedouble_tmcprice = (sellingPrice_withoutDiscount * weightindouble) / 1000;
                        try{
                            tmcpricewithoutdiscount = ((tmcpricewithoutdiscount * weightindouble) / 1000);
                            appPrice_withoutMarkup_text_widget_appLayout.setText(String.valueOf((int) Math.round(tmcpricewithoutdiscount)));

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        // appPricedouble_priceperKg = sellingPrice_withoutDiscount;

                    } else {
                        Toast.makeText(ChangeMenuItem_Price_Settings.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



            try {
                appPriceint = (int) Math.round(appPricedouble_tmcprice);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("123456  - ", " appPricedouble_priceperKg 1  "+ appPricedouble_tmcprice);

            try {
                sellingPriceInt = (int) Math.round(sellingPrice_withoutDiscount);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                appPrice_PricePerKg = (String.valueOf(appPriceint));
                appliedDiscountPercentage = (String.valueOf(discount_percentage));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                appPrice_text_widget_appLayout.setText(String.valueOf(appPriceint));
                appPricePerKg_textview_widget_appLayout .setText(String.valueOf(sellingPriceInt));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{


            Toast.makeText(ChangeMenuItem_Price_Settings.this, "App Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }

        Adjusting_Widgets_Visibility(false);


    }
    private void CalculateMarinadeeAppPriceAndPosPrice(double sellingprice, int discount_percentage) {
        double sellingPrice_withoutDiscount = 0;
        double discountAmount = 0;
        int weightindouble = 0;
        int posPriceint = 0;
        int appPriceint = 0;

        try {
            discountAmount = (discount_percentage * sellingprice) / 100;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            sellingPrice_withoutDiscount = sellingprice - discountAmount;

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (Marinadeefinalweight.matches("[0-9]+") && (Marinadeefinalweight.length() > 0)) {
                weightindouble = Integer.parseInt(Marinadeefinalweight);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (MarinadeepricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                if (weightindouble != 0) {

                    MarinadeappPricedouble = (sellingPrice_withoutDiscount * weightindouble) / 1000;
                    MarinadeposPricedouble = sellingPrice_withoutDiscount;

                } else {
                    Toast.makeText(ChangeMenuItem_Price_Settings.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (MarinadeepricetypeforPos.equals(Constants.TMCPRICE)) {
                MarinadeappPricedouble = sellingPrice_withoutDiscount;
                MarinadeposPricedouble = sellingPrice_withoutDiscount;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            appPriceint = (int) Math.round(MarinadeappPricedouble);
            posPriceint = (int) Math.round(MarinadeposPricedouble);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            MarinadeeappPrice = (String.valueOf(appPriceint));
            MarinadeeposPrice = (String.valueOf(posPriceint));
            MarinadeeappliedDiscountPercentage = (String.valueOf(discount_percentage));
        } catch (Exception e) {
            e.printStackTrace();
        }







    }


    private void CalculateAppPriceAndPosPriceWihoutMarkupPrice(double sellingprice, int discount_percentage) {
        if(sellingprice>0) {
            Adjusting_Widgets_Visibility(true);

            double sellingPrice_withoutDiscount=0 , appMarkupPercentagePrice =0 ;
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

      /*  try{
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

       */
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
                appPriceint = (int) Math.round(appPricedouble);
                posPriceint = (int) Math.round(posPricedouble);

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


        }
        else{


            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }

        Adjusting_Widgets_Visibility(false);




    }


    private void CalculateAppPriceAndPosPrice(double sellingprice, int discount_percentage, int appMarkupPercentageInt) {
        if(sellingprice>0) {
            Adjusting_Widgets_Visibility(true);

            double sellingPrice_withoutDiscount=0 , appMarkupPercentagePrice =0 ,sellingPrice_withoutDiscount_MarkupPrice =0;
            double discountAmount = 0,tmcpricewithoutdiscount=0;
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
                tmcpricewithoutdiscount = sellingPrice_withoutDiscount;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                appMarkupPercentagePrice = (appMarkupPercentageInt * sellingPrice_withoutDiscount) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("123456  - ", " appMarkupPercentagePrice  "+appMarkupPercentagePrice);

            try {
                sellingPrice_withoutDiscount_MarkupPrice = sellingPrice_withoutDiscount + appMarkupPercentagePrice;

            } catch (Exception e) {
                e.printStackTrace();
            }


            try{
                if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                    weightindouble = Integer.parseInt(finalweight);
                    try{
                        tmcpricewithoutdiscount = ((tmcpricewithoutdiscount * weightindouble) / 1000);
                        appPrice_withoutMarkup_text_widget_appLayout.setText(String.valueOf((int) Math.round(tmcpricewithoutdiscount)));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

      /*  try{
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

       */
            try{
                if (pricetypeforPos.equals(Constants.TMCPRICE)) {
                    appPricedouble = sellingPrice_withoutDiscount_MarkupPrice;
                    posPricedouble = sellingPrice_withoutDiscount;

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                appPriceint = (int) Math.round(appPricedouble);
                posPriceint = (int) Math.round(posPricedouble);

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
        }
        else{


            Toast.makeText(ChangeMenuItem_Price_Settings.this, "Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }

        Adjusting_Widgets_Visibility(false);




    }


    private void ChangeMenuItemPriceInDB(String menuitemKey, String appPrice, String posPrice, String appliedDiscountPercentage, String swiggyPrice, String dunzoPrice, String bigBasketPrice, String wholeSalePrice) {
        Adjusting_Widgets_Visibility(true);

        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", menuitemKey);
            jsonObject.put("tmcprice", appPrice);
            jsonObject.put("tmcpriceperkg", posPrice);
            jsonObject.put("swiggyprice", swiggyPrice);
            jsonObject.put("dunzoprice", dunzoPrice);
            jsonObject.put("bigbasketprice", bigBasketPrice);
            jsonObject.put("wholesaleprice", wholeSalePrice);
            try {
                jsonObject.put("appmarkuppercentage", appMarkupPercentageInt);
            }
            catch (Exception e){
                jsonObject.put("appmarkuppercentage", 0);

                e.printStackTrace();
            }
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
                ChangeMenuItemPriceInSharedPreferenes(menuitemKey, appPrice, posPrice, appliedDiscountPercentage,swiggyPrice,dunzoPrice,bigBasketPrice,wholeSalePrice);


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





    private void ChangeMenuItemPriceInSharedPreferenes(String menuitemKey, String appPrice, String posPrice, String appliedDiscountPercentage, String swiggyPrice, String dunzoPrice, String bigBasketPrice, String wholeSalePrice) {
        for (int i = 0; i < MenuItem.size(); i++) {
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String MenuItemkey = modal_menuItemSettings.getKey();
            if (MenuItemkey.equals(menuitemKey)) {
                modal_menuItemSettings.setTmcprice(appPrice);
                modal_menuItemSettings.setTmcpriceperkg(posPrice);
                modal_menuItemSettings.setApplieddiscountpercentage(appliedDiscountPercentage);
                modal_menuItemSettings.setSwiggyprice(swiggyPrice);
                modal_menuItemSettings.setDunzoprice(dunzoPrice);
                modal_menuItemSettings.setBigbasketprice(bigBasketPrice);
                modal_menuItemSettings.setWholesaleprice(wholeSalePrice);
                modal_menuItemSettings.setAppmarkuppercentage(String.valueOf(appMarkupPercentageInt));


                try{

                    //Log.d("Constants.TAG", " mobileScreen_dashboard.completemenuItem 1 " +  mobileScreen_dashboard.completemenuItem);

                    //    Log.d("Constants.TAG", "completemenuItem json  " + json);
                    //  Log.d("Constants.TAG",  "       N               " );
                    String json = new Gson().toJson(MenuItem);
                    if(screenInches>Constants.default_mobileScreenSize){
                        Pos_Dashboard_Screen.completemenuItem =json;

                    }
                    else{
                        MobileScreen_Dashboard.completemenuItem =json;

                    }
                    //  Log.d("Constants.TAG", " mobileScreen_dashboard.completemenuItem 2 " +  mobileScreen_dashboard.completemenuItem);

                }
                catch(Exception e){
                    e.printStackTrace();
                }





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
        int finalsellingprice , finalAppSellingPrice,finalPosSellingPrice;
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
                        posPrice_pricePerKg = String.valueOf(modal_menuItemSettings.getTmcpriceperkg());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        appPrice = String.valueOf(modal_menuItemSettings.getTmcprice());
                        //  appPrice_PricePerKg =  String.valueOf(modal_menuItemSettings.getTmcprice());
                        appPrice_withoutMarkup_text_widget_appLayout.setText(String.valueOf(appPrice));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        appMarkupPercentageString =  String.valueOf(modal_menuItemSettings.getAppmarkuppercentage());
                        appMarkupPercentageInt = Integer.parseInt(appMarkupPercentageString);

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
                        wholeSalePrice = String.valueOf(modal_menuItemSettings.getWholesaleprice());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        swiggyPrice = String.valueOf(modal_menuItemSettings.getSwiggyprice());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try{
                        dunzoPrice = String.valueOf(modal_menuItemSettings.getDunzoprice());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        bigBasketPrice = String.valueOf(modal_menuItemSettings.getBigbasketprice());

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


                    try{
                        if(pricetypeforPos.equals(Constants.TMCPRICE)){
                            sellingprice = CalculateSellingPrice(appPrice, appliedDiscountPercentage);
                            appPrice = CalculateAppPriceWithMarkupPrice(appPrice,appliedDiscountPercentage,appMarkupPercentageInt);
                            isTMCPrice =true;

                        }
                        if(pricetypeforPos.equals(Constants.TMCPRICEPERKG)){
                            //appSellingPrice_priceperKg = CalculateSellingPriceforTMCPricePerKgforApp(finalweight,appPrice, appliedDiscountPercentage);
                            //  posSellingPrice_priceperKg = CalculateSellingPriceforTMCPricePerKgforPOS(posPrice, appliedDiscountPercentage);
                            sellingprice = CalculateSellingPrice(posPrice, appliedDiscountPercentage);
                            posSellingPrice_priceperKg = CalculateSellingPriceforTMCPricePerKgforPOS(posPrice, appliedDiscountPercentage);
                            appSellingPrice_priceperKg = CalculateTMCPricePerKgforAppWithDiscountAppMarkupPrice(posPrice, appliedDiscountPercentage,appMarkupPercentageString);
                            int weightindouble =0;

                            try{
                                if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                                    weightindouble = Integer.parseInt(finalweight);

                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }


                            try{

                                appPrice = String.valueOf((int) Math.round((appSellingPrice_priceperKg / 1000) * weightindouble));
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                            isTMCPrice =false;
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }





                    itemUniqueCode_text_widget.setText(itemUniqueCode);
                    itemName_text_widget.setText(itemName);
                    appPrice_text_widget.setText(appPrice);
                    posPrice_text_widget.setText(posPrice);
                    wholesale_selling_price_text_widget_wholesaleLayout.setText(wholeSalePrice);

                    swiggy_selling_price_text_widget_swiggyLayout.setText(swiggyPrice);
                    dunzo_selling_price_text_widget_dunzoLayout.setText(dunzoPrice);
                    bigbasket_selling_price_text_widget_bigbasketLayout.setText(bigBasketPrice);
                    appliedDiscountPercentage_text_widget.setText(appliedDiscountPercentage);
                    appPrice_text_widget_appLayout.setText(appPrice);


                    posPrice_text_widget_posLayout.setText(posPrice_pricePerKg);
                    selling_price_common_edittext_widget.setText(posPrice_pricePerKg);
                    appMarkupPercentage_text_widget .setText(appMarkupPercentageString);
                    try{
                        finalsellingprice = (int) Math.round(sellingprice);
                    }
                    catch (Exception e){
                        finalsellingprice=00;
                        e.printStackTrace();
                    }


                    try{
                        finalAppSellingPrice = (int) Math.round(appSellingPrice_priceperKg);
                    }
                    catch (Exception e){
                        finalAppSellingPrice=00;
                        e.printStackTrace();
                    }


                    try{
                        finalPosSellingPrice = (int) Math.round(posSellingPrice_priceperKg);
                    }
                    catch (Exception e){
                        finalPosSellingPrice=00;
                        e.printStackTrace();
                    }


                    selling_price_common_edittext_widget .setText(String.valueOf(finalsellingprice));
                    selling_price_text_widget.setText(String.valueOf(finalsellingprice));
                    appPricePerKg_textview_widget_appLayout .setText(String.valueOf(finalAppSellingPrice));
                    app_selling_price_text_widget_appLayout.setText(String.valueOf(finalAppSellingPrice));
                    pos_selling_price_text_widget_posLayout.setText(String.valueOf(finalPosSellingPrice));
                    //Log.d(Constants.TAG, "displaying_menuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));
                    Adjusting_Widgets_Visibility(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


    }

    private String CalculateAppPriceWithMarkupPrice(String appPrice, String appliedDiscountPercentage, int appMarkupPercentageInt) {
        double appPrice_double = 0;            int appPriceint = 0;

        try {
            appPrice_double = Double.parseDouble(appPrice);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(appPrice_double>0) {


            double  appMarkupPercentagePrice =0 ,appPriceWith_MarkupPrice =0;


            try {
                appMarkupPercentagePrice = (appMarkupPercentageInt * appPrice_double) / 100;
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                appPriceWith_MarkupPrice = appPrice_double + appMarkupPercentagePrice;

            } catch (Exception e) {
                e.printStackTrace();
            }



            try{
                if (pricetypeforPos.equals(Constants.TMCPRICE)) {
                    appPricedouble = appPriceWith_MarkupPrice;

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                appPriceint = (int) Math.round(appPricedouble);

            }
            catch (Exception e){
                e.printStackTrace();
            }



        }
        else{


            Toast.makeText(ChangeMenuItem_Price_Settings.this, "TMC Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }



        return  String.valueOf(appPriceint);

    }

    private double CalculateTMCPricePerKgforAppWithDiscountAppMarkupPrice( String price_String, String appliedDiscountPercentage_String, String appMarkupPercentageString) {

        double price = 0;
        double discount = 0 , appMarkupPrice =0 , appMarkupPricePercentage =0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try{
            price = Double.parseDouble(price_String);

        }
        catch(Exception  e){
            e.printStackTrace();
        }




        try{
            appMarkupPricePercentage = Double.parseDouble(appMarkupPercentageString);

        }
        catch(Exception  e){
            e.printStackTrace();
        }

        try {
            appMarkupPrice = (appMarkupPricePercentage * price) / 100;
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            price = price + appMarkupPrice ;
        }
        catch (Exception e){
            e.printStackTrace();
        }




       /* try {
            discount = (discount * price) / 100;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            price = price - discount ;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        */


        try{
            price = Double.parseDouble(decimalFormat.format(price));

        }
        catch(Exception  e){
            e.printStackTrace();
        }




        return price;

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
                        if ((!netweight.equals("")) && (!netweight.equals("0")) && (!netweight.equals(null)) && (!netweight.equals("null"))) {
                            weight = netweight+" (Netwt)";
                        }
                        else {
                            weight = "      There is no weight ";
                        }
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


    private double CalculateSellingPriceforTMCPricePerKgforPOS(String posPrice_String, String appliedDiscountPercentage_String) {

        double posPrice =0;
        double pos_SellingPrice = 0;
        double discount = 0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        try{
            posPrice = Double.parseDouble(posPrice_String);

        }
        catch(Exception  e){
            e.printStackTrace();
            posPrice =0;
        }
        try{
            discount = Double.parseDouble(appliedDiscountPercentage_String);

        }
        catch(Exception  e){
            e.printStackTrace();
            discount =0;
        }

        try{
            pos_SellingPrice = posPrice/(1-(discount/100));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            pos_SellingPrice = Double.parseDouble(decimalFormat.format(pos_SellingPrice));

        }
        catch(Exception  e){
            e.printStackTrace();
        }

        return pos_SellingPrice;

    }



    private double CalculateSellingPriceforTMCPricePerKgforApp(String finalweight, String appPrice_String, String appliedDiscountPercentage_String) {
        double app_SellingPrice = 0;

        double appPrice =0;
        int weightindouble =0;
        double discount = 0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try{
            appPrice = Double.parseDouble(appPrice_String);

        }
        catch(Exception  e){
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

            appPrice = (appPrice * 1000) /weightindouble ;
        }
        catch(Exception e){
            e.printStackTrace();
        }


        try{
            discount = Double.parseDouble(appliedDiscountPercentage_String);

        }
        catch(Exception  e){
            e.printStackTrace();
        }



        try{
            app_SellingPrice = appPrice/(1-(discount/100));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            app_SellingPrice = Double.parseDouble(decimalFormat.format(app_SellingPrice));

        }
        catch(Exception  e){
            e.printStackTrace();
        }




        return app_SellingPrice;


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
                        int appprice_int = (int) Math.round(appPrice_double);
                        appPrice = String.valueOf(appprice_int);

                    }
                    catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(ChangeMenuItem_Price_Settings.this,"Enter  APP Price in correct format",Toast.LENGTH_LONG).show();

                    }
                    try {
                        posPrice = posPrice_text_widget.getText().toString();
                        double posPrice_double = Double.parseDouble(posPrice);
                        int posPriceint = (int) Math.round(posPrice_double);
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
