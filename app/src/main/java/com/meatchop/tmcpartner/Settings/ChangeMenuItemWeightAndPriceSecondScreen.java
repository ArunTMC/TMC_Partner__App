package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChangeMenuItemWeightAndPriceSecondScreen extends AppCompatActivity {
    ListView itemweightDetails_Listview,itemCutDetails_Listview;
    List<Modal_MenuItemWeightDetails> weightDetailsArray = new ArrayList<>();
    List<Modal_MenuItemCutDetails> cutDetailsArray = new ArrayList<>();
    MobileScreen_Dashboard mobileScreen_dashboard;
    String menuitemKey = "";
    LinearLayout loadingPanel, loadingpanelmask,two_box_linearlayout;
    List<Modal_MenuItemCutDetails> MenuItemCutDetailsArrayDefaultList = new ArrayList<>();
    List<Modal_MenuItemWeightDetails> MenuItemWeightDetailsArrayDefaultList = new ArrayList<>();

    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    List<Modal_MenuItem_Settings> marinadeMenuList=new ArrayList<>();


    String MarinadeepricetypeforPos,MarinadeemenuitemKey, MarinadeeitemUniqueCode, MarinadeeitemName, Marinadeegrossweight,Marinadeegrossweightingrams,Marinadeeportionsize,Marinadeenetweight,
            MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage,Marinadeefinalweight;

    double Marinadesellingprice = 0,Marinadesellingprice_withoutDiscount = 0,  MarinadesellingPriceWithoutDiscount = 0, Marinadegrossweightdouble = 0, MarinadeposPricedouble = 0, MarinadeappPricedouble = 0;

    String appliedDiscountPercentage="",posPrice="",posPrice_pricePerKg="",appPrice="",appPrice_PricePerKg="",portionsize="",netweight="",grossweight="",grossweightingrams="",swiggyPrice="",dunzoPrice="",bigBasketPrice="",itemName="",
            itemUniqueCode ="",pricetypeforPos="",finalweight="",itemcutdetailsString="",itemweightdetailsString="",weightDetailDisplayno="";;
    TextView defaultnetweight_textonly_widget,defaultGrossweight_textonly_widget,defaultportionsize_textonly_widget,itemName_textview_widget,itemUniqueCode_text_widget,grossweight_textview_widget,netweight_textview_widget,portionsize_textview_widget,pricetype_textview_widget,app_price_textview_widget,pos_price_textview_widget,
            selling_price_text_widget,posPrice_text_widget,appPrice_text_widget,appPrice_text_widget_appLayout,posPrice_text_widget_posLayout,swiggy_selling_price_text_widget_swiggyLayout,dunzo_selling_price_text_widget_dunzoLayout,
            bigbasket_selling_price_text_widget_bigbasketLayout,swiggy_price_label,dunzo_price_label,bigbasket_price_label;

    LinearLayout tmcpriceperkg_layout,tmcUnitPrice_layout, cutDetailsListLayout,defaultitemweightdetailslayout,itemweightdetailslistlayout,itemPriceDetailsLayout,add_new_weightDetails_button_Layout,add_new_cutDetails_button_Layout;

     ImageView addnewweightDetails_imageview,addnewcutDetails_imageview;
    double sellingprice = 0,appSellingPrice_priceperKg=0,posSellingPrice_priceperKg=0, posPricedouble = 0, appPricedouble = 0,posPricedouble__priceperKg =0,appPricedouble_priceperKg =0;
    boolean isTMCPrice = false;
    boolean isAppPrice_PricePerKgChanged =false;
    boolean isPosPrice_PricePerKgChanged = false;
    Button saveDetails,computeAppandPosPrice,updateChangesinMenutableDB;
    double screenInches;

    int discount_percentage =0;

    boolean isNetweight_singleEditText = false;

    Button pricedetailsScrollButton,weightdetailsScrollButton,cutdetailsScrollButton;
    ArrayList<String> weightDetailsKey_arrayList = new ArrayList<>();

    private final ArrayList<String> weightDetailsGrossweight_arrayList = new ArrayList<>();
    private ArrayAdapter weightDetailsName_aAdapter;


    ArrayList<String> cutDetailsKey_arrayList = new ArrayList<>();

    private final ArrayList<String> cutDetailsName_arrayList = new ArrayList<>();
    private ArrayAdapter cutDetailsName_aAdapter;

    public static BottomSheetDialog bottomSheetDialog;

    String MenuItemCutdetailsString="",MenuItemWeightdetailsString="";

    String isdefault_bottomsheetDialog="", netweight_bottomsheetDialog="",netweightingrams_bottomsheetDialog="",portionsize_bottomsheetDialog="",
            grossweight_bottomsheetDialog="",weightkey_bottomsheetDialog="";


    String isdefault_bottomsheetDialog_cutDetails="", netweight_bottomsheetDialog_cutDetails="",netweightingrams_bottomsheetDialog_cutDetails="",portionsize_bottomsheetDialog_cutDetails="",
            grossweight_bottomsheetDialog_cutDetails="",cutDisplayNo_bottomsheetDialog_cutDetails="",cutkey_bottomsheetDialog_cutDetails="",cutImage_bottomsheetDialog_cutDetails="",cutdesp_bottomsheetDialog_cutDetails="",cutname_bottomsheetDialog_cutDetails="";
    ScrollView wholescrollview;
    EditText netweight_second_textview_widget,netweight_first_textview_widget,appliedDiscountPercentage_text_widget,pos_selling_price_text_widget_posLayout,app_selling_price_text_widget_appLayout,defaultgrossweight_text_widget,defaultnetweight_textview_widget,defaultportionsize_textview_widget;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_menu_item_weight_and_price_second_screen);
        itemweightDetails_Listview = findViewById(R.id.itemweightDetails_Listview);
        itemCutDetails_Listview = findViewById(R.id.itemCutDetails_Listview);

        loadingPanel = findViewById(R.id.loadingPanel);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);

        weightDetailsArray.clear();
        cutDetailsArray.clear();
        marinadeMenuList.clear();
        MenuItem.clear();


        itemName_textview_widget = findViewById(R.id.itemName_textview_widget);
        itemUniqueCode_text_widget = findViewById(R.id.itemUniqueCode_text_widget);
        grossweight_textview_widget = findViewById(R.id.grossweight_textview_widget);
        netweight_textview_widget = findViewById(R.id.netweight_textview_widget);
        portionsize_textview_widget = findViewById(R.id.portionsize_textview_widget);
        pricetype_textview_widget = findViewById(R.id.pricetype_textview_widget);
        app_price_textview_widget = findViewById(R.id.app_price_textview_widget);
        pos_price_textview_widget = findViewById(R.id.pos_price_textview_widget);

        computeAppandPosPrice = findViewById(R.id.computeAppandPosPrice);

        swiggy_price_label = findViewById(R.id.swiggy_price_label);
        dunzo_price_label = findViewById(R.id.dunzo_price_label);
        bigbasket_price_label = findViewById(R.id.bigbasket_price_label);

        swiggy_selling_price_text_widget_swiggyLayout =findViewById(R.id.swiggy_selling_price_text_widget_swiggyLayout);
        dunzo_selling_price_text_widget_dunzoLayout = findViewById(R.id.dunzo_selling_price_text_widget_dunzoLayout);
        bigbasket_selling_price_text_widget_bigbasketLayout  =findViewById(R.id.bigbasket_selling_price_text_widget_bigbasketLayout);



        posPrice_text_widget_posLayout = findViewById(R.id.posPrice_text_widget_posLayout);
        appPrice_text_widget_appLayout = findViewById(R.id.appPrice_text_widget_appLayout);
        appliedDiscountPercentage_text_widget = findViewById(R.id.appliedDiscountPercentage_text_widget);

        netweight_first_textview_widget = findViewById(R.id.netweight_first_textview_widget);
        netweight_second_textview_widget = findViewById(R.id.netweight_second_textview_widget);


        pos_selling_price_text_widget_posLayout = findViewById(R.id.Pos_selling_price_text_widget_posLayout);
        app_selling_price_text_widget_appLayout = findViewById(R.id.app_selling_price_text_widget_appLayout);
        selling_price_text_widget = findViewById(R.id.tmcpriceselling_price_text_widget);


        posPrice_text_widget = findViewById(R.id.posPrice_text_widget);
        appPrice_text_widget = findViewById(R.id.appPrice_text_widget);
        tmcUnitPrice_layout = findViewById(R.id.tmcUnitPrice_layout);
        tmcpriceperkg_layout = findViewById(R.id.tmcpriceperkg_layout);

        defaultitemweightdetailslayout  = findViewById(R.id.defaultitemweightdetailslayout);
        itemweightdetailslistlayout  = findViewById(R.id.itemweightdetailslistlayout);

        defaultportionsize_textview_widget = findViewById(R.id.defaultportionsize_textview_widget);
        defaultnetweight_textview_widget = findViewById(R.id.defaultnetweight_textview_widget);
        defaultgrossweight_text_widget = findViewById(R.id.defaultgrossweight_text_widget);
        defaultportionsize_textonly_widget  = findViewById(R.id.defaultportionsize_textonly_widget);
        defaultnetweight_textonly_widget  = findViewById(R.id.defaultnetweight_textonly_widget);
        defaultGrossweight_textonly_widget  = findViewById(R.id.defaultGrossweight_textonly_widget);


        itemPriceDetailsLayout =  findViewById(R.id.itemPriceDetailsLayout);
        cutDetailsListLayout = findViewById(R.id.cutDetailsListLayout);

        pricedetailsScrollButton =  findViewById(R.id.pricedetailsScrollButton);
        cutdetailsScrollButton  =  findViewById(R.id.cutdetailsScrollButton);
        weightdetailsScrollButton = findViewById(R.id.weightdetailsScrollButton);

        two_box_linearlayout = findViewById(R.id.two_box_linearlayout);
        addnewcutDetails_imageview = findViewById(R.id.addnewcutDetails_imageview);

        addnewweightDetails_imageview = findViewById(R.id.addnewweightDetails_imageview);
        add_new_weightDetails_button_Layout = findViewById(R.id.add_new_weightDetails_button_Layout);
        add_new_cutDetails_button_Layout = findViewById(R.id.add_new_cutDetails_button_Layout);
        updateChangesinMenutableDB  = findViewById(R.id.updateChangesinMenutableDB);

        wholescrollview =  findViewById(R.id.wholescrollview);
        wholescrollview.setSmoothScrollingEnabled(true);
        showProgressBar(true);

        getMarinadeeMenuItemArrayFromSharedPreferences();
        getMenuItemArrayFromSharedPreferences();
        getMenuItemCutDetailsFromSharedPreferences();
        getMenuItemWeightDetailsFromSharedPreferences();

        try {
            menuitemKey = getIntent().getStringExtra("menuItemKey");
            getMenuItembasedOnkey(menuitemKey);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Error in getting Menu Item from Cache", Toast.LENGTH_LONG).show();

        }

        if(isTMCPrice){
            tmcUnitPrice_layout.setVisibility(View.VISIBLE);
            tmcpriceperkg_layout.setVisibility(View.GONE);

            swiggy_price_label.setText("Swiggy  Price (  TMC Price  )  :-   ");
            dunzo_price_label.setText("Dunzo  Price (  TMC Price  )  :-   ");
            bigbasket_price_label.setText("BigBasket  Price (  TMC Price  )  :-   ");

        }
        else{
            tmcUnitPrice_layout.setVisibility(View.GONE);
            tmcpriceperkg_layout.setVisibility(View.VISIBLE);
            swiggy_price_label.setText("Swiggy  Price (  Price Per Kg  )  :-   ");
            dunzo_price_label.setText("Dunzo  Price (  Price Per Kg  )  :-   ");
            bigbasket_price_label.setText("BigBasket  Price (  Price Per Kg  )  :-   ");

       }
        weightdetailsScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View targetView = findViewById(R.id.itemweightdetailslistlayout);
                targetView.getParent().requestChildFocus(targetView,targetView);
            }
        });
        pricedetailsScrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wholescrollview.scrollTo(0,0);
                // View targetView = findViewById(R.id.itemPriceDetailsLayout);
                //    targetView.getParent().requestChildFocus(targetView,targetView);


            }
        });

        updateChangesinMenutableDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTMCPrice) {
                    if (selling_price_text_widget.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                        if (appPrice_text_widget.getText().length() > 0 && posPrice_text_widget.getText().toString().length() > 0) {
                            try {
                                showProgressBar(true);

                                sellingprice = Double.parseDouble(selling_price_text_widget.getText().toString());
                                discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                swiggyPrice = String.valueOf(swiggy_selling_price_text_widget_swiggyLayout.getText().toString());
                                dunzoPrice = String.valueOf(dunzo_selling_price_text_widget_dunzoLayout.getText().toString());
                                bigBasketPrice = String.valueOf(bigbasket_selling_price_text_widget_bigbasketLayout.getText().toString());

                                CalculateMarinadeeAppPriceAndPosPrice(sellingprice, discount_percentage);

                                CalculateAppPriceAndPosPrice(sellingprice, discount_percentage);
                                if(sellingprice>0) {

                                     //ChangeMarinadeMenuItemPriceInDB(MarinadeemenuitemKey, MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage);
                                    ChangeMenuItemDataInDB(appPrice, posPrice);
                                   // ChangeMenuItemPriceInDB(menuitemKey, appPrice, posPrice, appliedDiscountPercentage, swiggyPrice,dunzoPrice,bigBasketPrice);
                                }
                                else{
                                    showProgressBar(false);

                                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Selling Price  can't be Zero", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                showProgressBar(false);
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "There was a Problem in Changing the Price", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        } else {
                            showProgressBar(false);

                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showProgressBar(false);

                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if (pos_selling_price_text_widget_posLayout.getText().length() > 0 &&  app_selling_price_text_widget_appLayout.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                        if (appPrice_text_widget_appLayout.getText().length() > 0 && posPrice_text_widget_posLayout.getText().toString().length() > 0) {
                            try {
                                showProgressBar(true);

                                appSellingPrice_priceperKg = Double.parseDouble(app_selling_price_text_widget_appLayout.getText().toString());
                                discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());


                                posSellingPrice_priceperKg = Double.parseDouble(pos_selling_price_text_widget_posLayout.getText().toString());
                                swiggyPrice = String.valueOf(swiggy_selling_price_text_widget_swiggyLayout.getText().toString());
                                dunzoPrice = String.valueOf(dunzo_selling_price_text_widget_dunzoLayout.getText().toString());
                                bigBasketPrice = String.valueOf(bigbasket_selling_price_text_widget_bigbasketLayout.getText().toString());

                                CalculateMarinadeeAppPriceAndPosPrice(appSellingPrice_priceperKg, discount_percentage);

                                CalculateAppPrice(appSellingPrice_priceperKg, discount_percentage);
                                CalculatePosPrice(posSellingPrice_priceperKg, discount_percentage);
                                if(appSellingPrice_priceperKg>0) {
                                    if (posSellingPrice_priceperKg > 0) {
                                        ChangeMenuItemDataInDB(appPrice_PricePerKg,posPrice_pricePerKg);

                                       // ChangeMarinadeMenuItemPriceInDB(MarinadeemenuitemKey, MarinadeeappPrice, MarinadeeposPrice, MarinadeeappliedDiscountPercentage);

                                        //ChangeMenuItemPriceInDB(menuitemKey, appPrice_PricePerKg, posPrice_pricePerKg, appliedDiscountPercentage, swiggyPrice, dunzoPrice, bigBasketPrice);

                                    }
                                    else{
                                        showProgressBar(false);

                                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "POS Selling Price  can't be Zero", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    showProgressBar(false);

                                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "APP Selling Price  can't be Zero", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {
                                showProgressBar(false);
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "There was a Problem in Changing the Price", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        } else {
                            showProgressBar(false);

                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showProgressBar(false);

                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





        cutdetailsScrollButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  wholescrollview.scrollTo(0,0);
                         View targetView = findViewById(R.id.cutDetailsListLayout);
                         targetView.getParent().requestChildFocus(targetView,targetView);
                    }
                });

        defaultgrossweight_text_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    showProgressBar(true);
                    String grossweightfromString = defaultgrossweight_text_widget.getText().toString();
                    grossweightfromString = grossweightfromString.replaceAll("[^\\d.]", "");

                    changeDataInLocalMenuArray("grossweight",grossweightfromString,menuitemKey);


                }
                return false;
            }
        });
        netweight_second_textview_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    showProgressBar(true);

                    String firstnetweightfromString="", secondnetweightfromString ="";
                    try{
                        firstnetweightfromString = netweight_first_textview_widget.getText().toString();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                         secondnetweightfromString = netweight_second_textview_widget.getText().toString();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }





                    String  netweightfromString="";

                    if(firstnetweightfromString.equals("")||firstnetweightfromString.equals(null)){
                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                    }
                    else if(secondnetweightfromString.equals("")||secondnetweightfromString.equals(null)){
                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                    }
                    else{
                        try{
                            firstnetweightfromString = firstnetweightfromString.replaceAll("[^\\d.]", "");
                            firstnetweightfromString = firstnetweightfromString.trim();

                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                        try{
                            secondnetweightfromString = secondnetweightfromString.replaceAll("[^\\d.]", "");
                            secondnetweightfromString = secondnetweightfromString.trim();
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }


                        netweightfromString =firstnetweightfromString +"g - "+secondnetweightfromString;
                        changeDataInLocalMenuArray("netweight",netweightfromString,menuitemKey);


                    }






                }
                return false;
            }
        });


        defaultnetweight_textview_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    showProgressBar(true);
                    String netweightfromString = defaultnetweight_textview_widget.getText().toString();
                    netweightfromString = netweightfromString.replaceAll("[^\\d.]", "");

                    changeDataInLocalMenuArray("netweight",netweightfromString,menuitemKey);


                }
                return false;
            }
        });


        defaultportionsize_textview_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
                    showProgressBar(true);
                    String portionsizefromString = defaultportionsize_textview_widget.getText().toString();
                   // portionsizefromString = portionsizefromString.replaceAll("[^\\d.]", "");

                    changeDataInLocalMenuArray("portionsize",portionsizefromString,menuitemKey);


                }
                return false;
            }
        });




        selling_price_text_widget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //do what you want on the press of 'done'
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
                    if ( selling_price_text_widget.getText().length() > 0  &&  appliedDiscountPercentage_text_widget.getText().length() > 0  && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0)  {
                        if (appPrice_text_widget.getText().length() > 0 && posPrice_text_widget.getText().toString().length() > 0) {
                            showProgressBar(true);
                            try {
                                sellingprice = Double.parseDouble(selling_price_text_widget.getText().toString());
                                if(sellingprice>0) {
                                    discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                    if (discount_percentage <= 100) {
                                        CalculateMarinadeeAppPriceAndPosPrice(sellingprice, discount_percentage);
                                        CalculateAppPriceAndPosPrice(sellingprice, discount_percentage);
                                    } else {
                                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Discount Percentage can't be greater than 100 %", Toast.LENGTH_SHORT).show();

                                        showProgressBar(false);

                                    }
                                }
                                else{
                                    showProgressBar(false);

                                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                showProgressBar(false);
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Can't Change the Price", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            showProgressBar(false);

                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        showProgressBar(false);

                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (isPosPrice_PricePerKgChanged) {
                        isPosPrice_PricePerKgChanged = false;
                        if (pos_selling_price_text_widget_posLayout.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0  && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0 && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                            if (posPrice_text_widget_posLayout.getText().toString().length() > 0) {
                                showProgressBar(true);
                                try {
                                    posSellingPrice_priceperKg = Double.parseDouble(pos_selling_price_text_widget_posLayout.getText().toString());
                                    if(posSellingPrice_priceperKg>0) {
                                        discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                        if (discount_percentage <= 100) {
                                            CalculatePosPrice(posSellingPrice_priceperKg, discount_percentage);


                                        } else {
                                            showProgressBar(false);

                                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Discount Percentage can't be greater than 100 %", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                    else {
                                        showProgressBar(false);

                                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "POS Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (Exception e) {
                                    showProgressBar(false);
                                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Can't Change the Price", Toast.LENGTH_SHORT).show();

                                    e.printStackTrace();
                                }
                            }
                            else {
                                showProgressBar(false);

                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            showProgressBar(false);

                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(isAppPrice_PricePerKgChanged){
                        isAppPrice_PricePerKgChanged = false;
                        if (app_selling_price_text_widget_appLayout.getText().length() > 0 && appliedDiscountPercentage_text_widget.getText().length() > 0   && swiggy_selling_price_text_widget_swiggyLayout.getText().length() > 0  && dunzo_selling_price_text_widget_dunzoLayout.getText().length() > 0 && bigbasket_selling_price_text_widget_bigbasketLayout.getText().length() > 0) {
                            if (appPrice_text_widget_appLayout.getText().toString().length() > 0) {
                                try {
                                    appSellingPrice_priceperKg = Double.parseDouble(app_selling_price_text_widget_appLayout.getText().toString());
                                    if(appSellingPrice_priceperKg>0) {
                                        showProgressBar(true);

                                        discount_percentage = Integer.parseInt(appliedDiscountPercentage_text_widget.getText().toString());
                                        if (discount_percentage <= 100) {
                                            CalculateAppPrice(appSellingPrice_priceperKg, discount_percentage);
                                            CalculateMarinadeeAppPriceAndPosPrice(appSellingPrice_priceperKg, discount_percentage);

                                        } else {
                                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Discount Percentage can't be greater than 100 %", Toast.LENGTH_SHORT).show();
                                            showProgressBar(false);

                                        }
                                    }
                                    else{
                                        showProgressBar(false);

                                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "APP Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Can't Change the Price", Toast.LENGTH_SHORT).show();

                                    showProgressBar(false);

                                }
                            }
                            else {
                                showProgressBar(false);

                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Price Fields can't be empty", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            showProgressBar(false);

                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Mandatory Fields can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });



        add_new_cutDetails_button_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewcutDetails_imageview.performClick();
            }
        });

        add_new_weightDetails_button_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addnewweightDetails_imageview.performClick();
            }
        });


        addnewcutDetails_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(ChangeMenuItemWeightAndPriceSecondScreen.this);
                bottomSheetDialog.setContentView(R.layout.menuitemcut_weightdetailseditactivity);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                LinearLayout weightDetailsLayout =  bottomSheetDialog.findViewById(R.id.weightDetailsLayout);
                LinearLayout cutDetailsLayout =  bottomSheetDialog.findViewById(R.id.cutDetailsLayout);

                CheckBox cutDetails_checkbox = bottomSheetDialog.findViewById(R.id.cutDetails_checkbox);
                EditText edit_cutname = bottomSheetDialog.findViewById(R.id.edit_cutname);
                EditText edit_cutDescription = bottomSheetDialog.findViewById(R.id.edit_cutDescription);
                EditText edit_grossweight_cutdetail = bottomSheetDialog.findViewById(R.id.edit_grossweight_cutdetail);
                EditText cutDetails_netweight_first_textview_widget = bottomSheetDialog.findViewById(R.id.cutDetails_netweight_first_textview_widget);
                EditText cutDetails_netweight_second_textview_widget = bottomSheetDialog.findViewById(R.id.cutDetails_netweight_second_textview_widget);
                EditText edit_portionsize_cutdetail = bottomSheetDialog.findViewById(R.id.edit_portionsize_cutdetail);
                EditText edit_netweight_cutdetail = bottomSheetDialog.findViewById(R.id.edit_netweight_cutdetail);
                Spinner cutDetailsName_spinner = bottomSheetDialog.findViewById(R.id.cutDetailsName_spinner);
                LinearLayout two_cutdetails_editbox_linearlayout =  bottomSheetDialog.findViewById(R.id.two_cutdetails_editbox_linearlayout);
                TextView edit_cutDescription_textview = bottomSheetDialog.findViewById(R.id.edit_cutDescription_textview);

                ListView defaultItemCutDetailsListview =  bottomSheetDialog.findViewById(R.id.defaultItemCutDetailsListview);
                ListView defaultIteWeightDetailsListview =  bottomSheetDialog.findViewById(R.id.defaultIteWeightDetailsListview);
                Button saveCutDetails_button = bottomSheetDialog.findViewById(R.id.saveCutDetails_button);

                LinearLayout cutDetailsCheckbox_LinearLayout =  bottomSheetDialog.findViewById(R.id.cutDetailsCheckbox_LinearLayout);



                assert cutDetailsLayout != null;
                cutDetailsLayout.setVisibility(View.VISIBLE);
                Objects.requireNonNull(weightDetailsLayout).setVisibility(View.GONE);
                defaultItemCutDetailsListview.setVisibility(View.GONE);
                defaultIteWeightDetailsListview.setVisibility(View.GONE);
                cutDetailsName_spinner.setVisibility(View.VISIBLE);
                edit_cutDescription_textview.setVisibility(View.VISIBLE);
                edit_cutDescription.setVisibility(View.GONE);
                Objects.requireNonNull(edit_cutname).setVisibility(View.GONE);

/*
                Adapter_MenuItemCutDetails adapter_menuItemCutDetails = new Adapter_MenuItemCutDetails(ChangeMenuItemWeightAndPriceSecondScreen.this, MenuItemCutDetailsArrayDefaultList, ChangeMenuItemWeightAndPriceSecondScreen.this,"FromAddNewCutDetails");
                defaultItemCutDetailsListview.setAdapter(adapter_menuItemCutDetails);
                helper_weight_cut_Listview.getListViewSize(defaultItemCutDetailsListview, screenInches);




                cutDetails_checkbox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                if(cutDetailsArray.size()>1) {
                    Objects.requireNonNull(cutDetails_checkbox).setChecked(false);
                }
                else{
                    Objects.requireNonNull(cutDetails_checkbox).setChecked(true);
                }



                Objects.requireNonNull(cutDetailsCheckbox_LinearLayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cutDetailsArray.size()>1){
                            if(Objects.requireNonNull(cutDetails_checkbox).isChecked()){
                                cutDetails_checkbox.setChecked(false);

                            }
                            else{
                                cutDetails_checkbox.setChecked(true);

                            }
                        }
                        else{
                            Objects.requireNonNull(cutDetails_checkbox).setChecked(true);
                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "First Additional Cut should be default one", Toast.LENGTH_SHORT).show();

                        }




                    }
                });




 */


                for(int i=0;i<  MenuItemCutDetailsArrayDefaultList.size();i++){
                    Modal_MenuItemCutDetails modal_menuItemCutDetails = MenuItemCutDetailsArrayDefaultList.get(i);
                    String CutDesp = "",Cutname = "" ;

                    try{
                        CutDesp = modal_menuItemCutDetails.getCutdesp().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }


                    try{
                        Cutname  = modal_menuItemCutDetails.getCutname().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }


            try {
                if(Cutname.equals("")){
                    if (!cutDetailsName_arrayList.contains(CutDesp)) {
                        cutDetailsName_arrayList.add(CutDesp);
                    }
                }
                else{
                    if (!cutDetailsName_arrayList.contains(Cutname)) {
                        cutDetailsName_arrayList.add(Cutname);
                    }
                }




            }
            catch (Exception e){
                e.printStackTrace();
            }

            if(i == (MenuItemCutDetailsArrayDefaultList.size()-1)) {
                cutDetailsName_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cutDetailsName_arrayList);
                Objects.requireNonNull(cutDetailsName_spinner).setAdapter(cutDetailsName_aAdapter);
            }
                }

                if(cutDetailsArray.size()>=1){
                    cutDetails_checkbox.setChecked(false);
                }
                else{
                    cutDetails_checkbox.setChecked(true);

                }
                cutDetailsCheckbox_LinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(cutDetailsArray.size()>=1){
                            if(cutDetails_checkbox.isChecked()){
                                cutDetails_checkbox.setChecked(false);

                            }
                            else{
                                cutDetails_checkbox.setChecked(true);

                            }
                        }
                        else{
                            cutDetails_checkbox.setChecked(true);

                        }
                    }
                });



                Objects.requireNonNull(cutDetailsName_spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String cutdesp = getMenuItemCutData(position,"cutdesp");
                        String cutdisplayno = getMenuItemCutData(position,"cutdisplayno");
                        String cutimagename = getMenuItemCutData(position,"cutimagename");
                        String cutkey = getMenuItemCutData(position,"cutkey");
                        cutkey_bottomsheetDialog_cutDetails = getMenuItemCutData(position,"cutkey");
                        cutImage_bottomsheetDialog_cutDetails= getMenuItemCutData(position,"cutimagename");
                        cutDisplayNo_bottomsheetDialog_cutDetails  = getMenuItemCutData(position,"cutdisplayno");
                        String isdefault = getMenuItemCutData(position,"isdefault");
                        String cutname = getMenuItemCutData(position,"cutname");
                        String netweight = getMenuItemCutData(position,"netweight");
                        String netweightingrams = getMenuItemCutData(position,"netweightingrams");
                        String portionsize = getMenuItemCutData(position,"portionsize");
                        String grossweight = getMenuItemCutData(position,"grossweight");
                        if(netweight.equals("")){
                            netweight = "000g - 000g";
                        }


                        Objects.requireNonNull(edit_cutDescription).setText(cutdesp.toString());
                        Objects.requireNonNull(edit_grossweight_cutdetail).setText(grossweight.toString());
                        Objects.requireNonNull(edit_cutDescription_textview).setText(cutdesp.toString());
                        Objects.requireNonNull(edit_cutname).setText(cutname.toString());
                        Objects.requireNonNull(edit_portionsize_cutdetail).setText(portionsize.toString());
                        Objects.requireNonNull(edit_netweight_cutdetail).setText(netweight.toString());
                        Objects.requireNonNull(cutDetails_netweight_first_textview_widget).setText(netweight.toString());
                        Objects.requireNonNull(cutDetails_netweight_second_textview_widget).setText(netweight.toString());





                        try {

                            if (netweight.contains("to") || netweight.contains("-")) {
                                Objects.requireNonNull(two_cutdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                                edit_netweight_cutdetail.setVisibility(View.GONE);


                                if (netweight.contains("to")) {
                                    String[] split = netweight.split("to");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                    cutDetails_netweight_second_textview_widget.setText(secondSubString);
                                } else {
                                    String[] split = netweight.split("-");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    cutDetails_netweight_first_textview_widget.setText(firstSubString);
                                    cutDetails_netweight_second_textview_widget.setText(secondSubString);


                                }


                            } else {
                                two_cutdetails_editbox_linearlayout.setVisibility(View.GONE);
                                edit_netweight_cutdetail.setVisibility(View.VISIBLE);



                                edit_netweight_cutdetail.setText(netweight);

                            }


                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Objects.requireNonNull(saveCutDetails_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cutname_bottomsheetDialog_cutDetails =  Objects.requireNonNull(edit_cutname).getText().toString();
                        cutdesp_bottomsheetDialog_cutDetails = Objects.requireNonNull(edit_cutDescription_textview).getText().toString();
                        grossweight_bottomsheetDialog_cutDetails = Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString();
                        netweight_bottomsheetDialog_cutDetails = Objects.requireNonNull(edit_netweight_cutdetail).getText().toString();
                        portionsize_bottomsheetDialog_cutDetails = Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString();

                        isdefault_bottomsheetDialog_cutDetails = String.valueOf(cutDetails_checkbox.isChecked()).toUpperCase();
                        // changeDataInLocalArray(cutname_bottomsheetDialog,cutdesp_bottomsheetDialog,grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,cutkey_bottomsheetDialog);
                        // if(!Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString().equals("")){
                        if((!Objects.requireNonNull(cutDetails_netweight_first_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(cutDetails_netweight_first_textview_widget).getText().toString().equals(""))&&(!Objects.requireNonNull(cutDetails_netweight_second_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(cutDetails_netweight_second_textview_widget).getText().toString().equals(""))) {


                            //   if(!Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("")){


                            grossweight_bottomsheetDialog_cutDetails = Objects.requireNonNull(edit_grossweight_cutdetail).getText().toString();


                            String firstnetweightfromString = "", secondnetweightfromString = "";
                            try {
                                firstnetweightfromString = cutDetails_netweight_first_textview_widget.getText().toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                secondnetweightfromString = cutDetails_netweight_second_textview_widget.getText().toString();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (firstnetweightfromString.equals("") || firstnetweightfromString.equals(null)) {
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                            } else if (secondnetweightfromString.equals("") || secondnetweightfromString.equals(null)) {
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

                            } else {
                                try {
                                    firstnetweightfromString = firstnetweightfromString.replaceAll("[^\\d.]", "");
                                    firstnetweightfromString = firstnetweightfromString.trim();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    secondnetweightfromString = secondnetweightfromString.replaceAll("[^\\d.]", "");
                                    secondnetweightfromString = secondnetweightfromString.trim();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            netweight_bottomsheetDialog_cutDetails = firstnetweightfromString + "g - " + secondnetweightfromString + "g";

                            //   netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight).getText().toString();
                            if(!Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("null") && !Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString().equals("")){

                                portionsize_bottomsheetDialog_cutDetails = Objects.requireNonNull(edit_portionsize_cutdetail).getText().toString();

                            }
                                isdefault_bottomsheetDialog_cutDetails = String.valueOf(cutDetails_checkbox.isChecked()).toUpperCase();
                            if(!cutDetailsKey_arrayList.contains(cutkey_bottomsheetDialog_cutDetails)) {

                                AddCutDetailsDataInLocalArray(cutname_bottomsheetDialog_cutDetails, cutdesp_bottomsheetDialog_cutDetails, grossweight_bottomsheetDialog_cutDetails, netweight_bottomsheetDialog_cutDetails, portionsize_bottomsheetDialog_cutDetails, isdefault_bottomsheetDialog_cutDetails, cutkey_bottomsheetDialog_cutDetails);
                            }
                            else{
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "This Cut Detail is Already in the list", Toast.LENGTH_SHORT).show();

                            }
                          /*  }
                            else
                            {
                                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Portion size can't be Empty", Toast.LENGTH_SHORT).show();
                            }

                           */

                        }
                        else
                        {
                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "NetWeight can't be Empty", Toast.LENGTH_SHORT).show();
                        }


                       /*
                       }else
                        {
                            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Grossweight can't be Empty", Toast.LENGTH_SHORT).show();
                        }


                        */



                    }
                });




                bottomSheetDialog.show();

            }
        });

        addnewweightDetails_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 isdefault_bottomsheetDialog=""; netweight_bottomsheetDialog="";netweightingrams_bottomsheetDialog="";portionsize_bottomsheetDialog="";
                        grossweight_bottomsheetDialog="";weightkey_bottomsheetDialog="";

                bottomSheetDialog = new BottomSheetDialog(ChangeMenuItemWeightAndPriceSecondScreen.this);
                bottomSheetDialog.setContentView(R.layout.menuitemcut_weightdetailseditactivity);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                LinearLayout weightDetailsLayout =  bottomSheetDialog.findViewById(R.id.weightDetailsLayout);
                CheckBox weightDetails_checkbox = bottomSheetDialog.findViewById(R.id.weightDetails_checkbox);
                EditText weightDetails_edit_grossweight = bottomSheetDialog.findViewById(R.id.edit_grossweight);
                EditText weightDetails_edit_netweight = bottomSheetDialog.findViewById(R.id.edit_netweight_weightDetails);
                EditText weightDetails_edit_portionsize = bottomSheetDialog.findViewById(R.id.edit_portionsize_weightDetails);
                Button saveWeightDetails_button = bottomSheetDialog.findViewById(R.id.saveWeightDetails_button);

                LinearLayout two_weightdetails_editbox_linearlayout =  bottomSheetDialog.findViewById(R.id.two_weightdetails_editbox_linearlayout);
                EditText weightDetails_netweight_first_textview_widget = bottomSheetDialog.findViewById(R.id.weightDetails_netweight_first_textview_widget);
                EditText weightDetails_netweight_second_textview_widget = bottomSheetDialog.findViewById(R.id.weightDetails_netweight_second_textview_widget);

                Spinner weightDetailsName_spinner = bottomSheetDialog.findViewById(R.id.weightDetailsName_spinner);


                Objects.requireNonNull(two_weightdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                Objects.requireNonNull(weightDetails_edit_netweight).setVisibility(View.GONE);


                LinearLayout cutDetailsLayout =  bottomSheetDialog.findViewById(R.id.cutDetailsLayout);
                LinearLayout weightDetailsCheckbox_LinearLayout =  bottomSheetDialog.findViewById(R.id.weightDetailsCheckbox_LinearLayout);


                assert cutDetailsLayout != null;
                cutDetailsLayout.setVisibility(View.GONE);
                Objects.requireNonNull(weightDetailsLayout).setVisibility(View.VISIBLE);

                weightDetailsName_spinner.setVisibility(View.VISIBLE);
                weightDetails_edit_grossweight.setVisibility(View.GONE);



                for(int i=0;i<  MenuItemWeightDetailsArrayDefaultList.size();i++){
                    Modal_MenuItemWeightDetails modal_menuItemWeightDetails = MenuItemWeightDetailsArrayDefaultList.get(i);
                    String portionsize = "",grossweight = "",netweight = "" ;

                    try{
                        portionsize = modal_menuItemWeightDetails.getPortionsize().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }


                    try{
                        grossweight  = modal_menuItemWeightDetails.getGrossweight().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }


                    try{
                        netweight  = modal_menuItemWeightDetails.getNetweight().toString();

                    }
                    catch (Exception  e){
                        e.printStackTrace();
                    }





                    try {
                        if (!weightDetailsGrossweight_arrayList.contains(grossweight)) {
                            weightDetailsGrossweight_arrayList.add(grossweight);
                        }

                        weightDetailsName_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, weightDetailsGrossweight_arrayList);
                        Objects.requireNonNull(weightDetailsName_spinner).setAdapter(weightDetailsName_aAdapter);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

                if(weightDetailsArray.size()>=1){
                    weightDetails_checkbox.setChecked(false);
                }
                else{
                    weightDetails_checkbox.setChecked(true);

                }
                weightDetailsCheckbox_LinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(weightDetailsArray.size()>=1){
                            if(weightDetails_checkbox.isChecked()){
                                weightDetails_checkbox.setChecked(false);

                            }
                            else{
                                weightDetails_checkbox.setChecked(true);

                            }
                        }
                        else{
                            weightDetails_checkbox.setChecked(true);

                        }
                    }
                });



                Objects.requireNonNull(weightDetailsName_spinner).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String grossweight = getMenuItemWeightData(position,"grossweight");
                        String grossweightingrams = getMenuItemWeightData(position,"grossweightingrams");
                        String netweight = getMenuItemWeightData(position,"netweight");
                        String weightkey = getMenuItemWeightData(position,"weightkey");
                      String weight = getMenuItemWeightData(position,"weight");
                        weightkey_bottomsheetDialog = getMenuItemWeightData(position,"weightkey");
                        weightDetailDisplayno = getMenuItemWeightData(position,"weightdisplayno");
                        String displayno = getMenuItemWeightData(position,"weightdisplayno");
                        String portionsize = getMenuItemWeightData(position,"portionsize");

                        try{
                            Objects.requireNonNull(weightDetails_edit_grossweight).setText(grossweight);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            Objects.requireNonNull(weightDetails_netweight_first_textview_widget).setText(netweight);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            Objects.requireNonNull(weightDetails_netweight_second_textview_widget).setText(netweight);

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                        try{
                            Objects.requireNonNull(weightDetails_edit_portionsize).setText(portionsize);

                        }
                        catch (Exception e){
                            e.printStackTrace();




                        }






                        try {

                            if (netweight.contains("to") || netweight.contains("-")) {

                                Objects.requireNonNull(two_weightdetails_editbox_linearlayout).setVisibility(View.VISIBLE);
                                Objects.requireNonNull(weightDetails_edit_netweight).setVisibility(View.GONE);

                                if (netweight.contains("to")) {
                                    String[] split = netweight.split("to");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    weightDetails_netweight_first_textview_widget.setText(firstSubString);
                                    weightDetails_netweight_second_textview_widget.setText(secondSubString);
                                } else {
                                    String[] split = netweight.split("-");
                                    String firstSubString = split[0];
                                    String secondSubString = split[1];
                                    firstSubString = firstSubString.trim();
                                    secondSubString = secondSubString.trim();
                                    weightDetails_netweight_first_textview_widget.setText(firstSubString);
                                    weightDetails_netweight_second_textview_widget.setText(secondSubString);


                                }


                            } else {
                                Objects.requireNonNull(two_weightdetails_editbox_linearlayout).setVisibility(View.GONE);
                                Objects.requireNonNull(weightDetails_edit_netweight).setVisibility(View.VISIBLE);


                                weightDetails_edit_netweight.setText(netweight);

                            }


                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });






            Objects.requireNonNull(saveWeightDetails_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(!Objects.requireNonNull(weightDetails_edit_grossweight).getText().toString().equals("null") && !Objects.requireNonNull(weightDetails_edit_grossweight).getText().toString().equals("")){
            if((!Objects.requireNonNull(weightDetails_netweight_first_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(weightDetails_netweight_first_textview_widget).getText().toString().equals(""))&&(!Objects.requireNonNull(weightDetails_netweight_second_textview_widget).getText().toString().equals("null") && !Objects.requireNonNull(weightDetails_netweight_second_textview_widget).getText().toString().equals(""))){


           // if(!Objects.requireNonNull(weightDetails_edit_portionsize).getText().toString().equals("null") && !Objects.requireNonNull(weightDetails_edit_portionsize).getText().toString().equals("")){







            grossweight_bottomsheetDialog = Objects.requireNonNull(weightDetails_edit_grossweight).getText().toString();
                grossweight_bottomsheetDialog = grossweight_bottomsheetDialog.replaceAll("[^\\d.]", "");


            String firstnetweightfromString="", secondnetweightfromString ="";
            try{
            firstnetweightfromString = weightDetails_netweight_first_textview_widget.getText().toString();
            }
            catch (Exception e){
            e.printStackTrace();
            }

            try{
            secondnetweightfromString = weightDetails_netweight_second_textview_widget.getText().toString();

            }
            catch (Exception e){
            e.printStackTrace();
            }






            if(firstnetweightfromString.equals("")||firstnetweightfromString.equals(null)){
            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

            }
            else if(secondnetweightfromString.equals("")||secondnetweightfromString.equals(null)){
            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, " NetWeight Edit box can't be empty", Toast.LENGTH_SHORT).show();

            }
            else {
            try {
            firstnetweightfromString = firstnetweightfromString.replaceAll("[^\\d.]", "");
            firstnetweightfromString = firstnetweightfromString.trim();

            } catch (Exception e) {
            e.printStackTrace();
            }

            try {
            secondnetweightfromString = secondnetweightfromString.replaceAll("[^\\d.]", "");
            secondnetweightfromString = secondnetweightfromString.trim();
            } catch (Exception e) {
            e.printStackTrace();
            }
            }

            netweight_bottomsheetDialog =firstnetweightfromString +"g - "+secondnetweightfromString;

            //   netweight_bottomsheetDialog = Objects.requireNonNull(edit_netweight).getText().toString();
            portionsize_bottomsheetDialog = Objects.requireNonNull(weightDetails_edit_portionsize).getText().toString();
            //weightkey_bottomsheetDialog = String.valueOf(weightDetailsArray.size()+1);
            isdefault_bottomsheetDialog = String.valueOf(weightDetails_checkbox.isChecked()).toUpperCase();
                if(!weightDetailsKey_arrayList.contains(weightkey_bottomsheetDialog)) {

                    AddDataInLocalweightDetailsArray(grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,weightkey_bottomsheetDialog,weightDetailDisplayno);
                }
                else{
                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "This Weight Detail is Already in the list", Toast.LENGTH_SHORT).show();

                }
           // AddDataInLocalweightDetailsArray(grossweight_bottomsheetDialog,netweight_bottomsheetDialog,portionsize_bottomsheetDialog,isdefault_bottomsheetDialog,weightkey_bottomsheetDialog);
           /* }
            else
            {
            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Portion size can't be Empty", Toast.LENGTH_SHORT).show();
            }

            */

            }
            else
            {
            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "NetWeight can't be Empty", Toast.LENGTH_SHORT).show();
            }

            }
            else
            {
            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Grossweight can't be Empty", Toast.LENGTH_SHORT).show();
            }




            }
            });



                bottomSheetDialog.show();



            }
        });


    }

    private void AddDataInLocalweightDetailsArray(String grossweight_bottomsheetDialog, String netweight_bottomsheetDialog, String portionsize_bottomsheetDialog, String isdefault_bottomsheetDialog, String weightkey_bottomsheetDialog, String weightDetailDisplayno) {
        String grossweightingrams_bottomsheetDialog="";
        if((netweight.toUpperCase().contains("PCS"))||(netweight.toUpperCase().contains("PC"))){
            try{

                netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog+"Pcs");
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if((netweight.toUpperCase().contains("G"))||(netweight.toUpperCase().contains("GRAMS"))||(netweight.toUpperCase().contains("GMS"))){
            try{
                netweight_bottomsheetDialog = String.valueOf(netweight_bottomsheetDialog+"g");

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if((grossweight.toUpperCase().contains("G"))||(grossweight.toUpperCase().contains("GRAMS"))||(grossweight.toUpperCase().contains("GMS"))){
            try{
                grossweight_bottomsheetDialog = String.valueOf(grossweight_bottomsheetDialog+"g");


            } catch (Exception e){
                e.printStackTrace();
            }
        }

        if((portionsize.toUpperCase().contains("PCS"))||(portionsize.toUpperCase().contains("PC"))){
            try{
                if(!portionsize_bottomsheetDialog.equals("")) {

                    portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog  );
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        else  if((portionsize.toUpperCase().contains("G"))||(portionsize.toUpperCase().contains("GRAMS"))||(portionsize.toUpperCase().contains("GMS"))){
            try{
                if(!portionsize_bottomsheetDialog.equals("")) {

                    portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog );
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            portionsize_bottomsheetDialog.trim();
            if(!portionsize_bottomsheetDialog.equals("")) {

                portionsize_bottomsheetDialog = String.valueOf(portionsize_bottomsheetDialog );
            }
        }

        try{
            grossweightingrams_bottomsheetDialog = String.valueOf(grossweight_bottomsheetDialog);
            grossweightingrams_bottomsheetDialog= grossweightingrams_bottomsheetDialog.replaceAll("[^\\d.]", "");

        }
        catch (Exception e){
            grossweightingrams ="0";
            e.printStackTrace();
        }



        try {

            if (netweight_bottomsheetDialog.contains("to") || netweight_bottomsheetDialog.contains("-")) {



                if (netweight_bottomsheetDialog.contains("to")) {
                    String[] split = netweight_bottomsheetDialog.split("to");
                    String firstSubString = split[0];
                    String secondSubString = split[1];
                    firstSubString = firstSubString.trim();
                    secondSubString = secondSubString.trim();
                    netweightingrams_bottomsheetDialog = secondSubString;

                } else {
                    String[] split = netweight_bottomsheetDialog.split("-");
                    String firstSubString = split[0];
                    String secondSubString = split[1];
                    firstSubString = firstSubString.trim();
                    secondSubString = secondSubString.trim();
                    netweightingrams_bottomsheetDialog = secondSubString;


                }


            } else {


                netweightingrams_bottomsheetDialog = netweight_bottomsheetDialog;


            }


        }
        catch (Exception e){
            e.printStackTrace();
        }


        Modal_MenuItemWeightDetails modal_menuItemWeightDetails = new Modal_MenuItemWeightDetails();
        modal_menuItemWeightDetails.grossweight = grossweight_bottomsheetDialog;
        modal_menuItemWeightDetails.netweight = netweight_bottomsheetDialog;
        modal_menuItemWeightDetails.portionsize = portionsize_bottomsheetDialog;
        modal_menuItemWeightDetails.weightkey = weightkey_bottomsheetDialog;
        modal_menuItemWeightDetails.isdefault = isdefault_bottomsheetDialog;
        modal_menuItemWeightDetails.weight = grossweight_bottomsheetDialog;
        modal_menuItemWeightDetails.weightdisplayno = weightDetailDisplayno;
        modal_menuItemWeightDetails.grossweightingrams = grossweightingrams_bottomsheetDialog;
        modal_menuItemWeightDetails.netweightingrams = netweightingrams_bottomsheetDialog;




        try{
            weightDetailsKey_arrayList.add(weightkey_bottomsheetDialog);
            weightDetailsArray.add(modal_menuItemWeightDetails);
            itemweightdetailsString ="weight added";
            bottomSheetDialog.cancel();

        }
        catch (Exception e){
            e.printStackTrace();
        }


        /*
        if(isdefault_bottomsheetDialog.toUpperCase().equals("TRUE")){
            try{
                grossweight = String.valueOf(grossweight_bottomsheetDialog);

            }
            catch (Exception e){
                grossweight ="";
                e.printStackTrace();
            }
            try{
                grossweightingrams = String.valueOf(grossweightingrams_bottomsheetDialog);

            }
            catch (Exception e){
                grossweightingrams ="00";
                e.printStackTrace();
            }

            try{
                portionsize = String.valueOf(portionsize_bottomsheetDialog);

            }
            catch (Exception e){
                portionsize ="";
                e.printStackTrace();
            }

            try{
                netweight = String.valueOf(netweight_bottomsheetDialog);

            }
            catch (Exception e){
                netweight ="";
                e.printStackTrace();
            }



        }

        try{
            isPosPrice_PricePerKgChanged = false;
            isAppPrice_PricePerKgChanged = true;
            computeAppandPosPrice.performClick();

x
        }
        catch (Exception e){
            e.printStackTrace();
        }


         */
        try{
            FormatAndDisplaytheDataa();
            bottomSheetDialog.cancel();



        }
        catch (Exception e){
            e.printStackTrace();
        }





    }
    private void AddCutDetailsDataInLocalArray(String cutname_bottomsheetDialog_cutDetails, String cutDesp_bottomsheetDialog_cutDetails1, String grossweight_bottomsheetDialog_cutDetails, String netweight_bottomsheetDialog_cutDetails, String portionsize_bottomsheetDialog_cutDetails, String isdefault_bottomsheetDialog_cutDetails, String cutkey_bottomsheetDialog_cutDetails) {

        String grossweightingrams_bottomsheetDialog="",netweightingrams_bottomsheetDialog="";
        try{
            try {

                if (netweight_bottomsheetDialog_cutDetails.contains("to") || netweight_bottomsheetDialog_cutDetails.contains("-")) {



                    if (netweight_bottomsheetDialog_cutDetails.contains("to")) {
                        String[] split = netweight_bottomsheetDialog_cutDetails.split("to");
                        String firstSubString = split[0];
                        String secondSubString = split[1];
                        firstSubString = firstSubString.trim();
                        secondSubString = secondSubString.trim();
                        netweightingrams_bottomsheetDialog = secondSubString;

                    } else {
                        String[] split = netweight_bottomsheetDialog_cutDetails.split("-");
                        String firstSubString = split[0];
                        String secondSubString = split[1];
                        firstSubString = firstSubString.trim();
                        secondSubString = secondSubString.trim();
                        netweightingrams_bottomsheetDialog = secondSubString;


                    }


                } else {


                    netweightingrams_bottomsheetDialog = netweight_bottomsheetDialog_cutDetails;


                }


            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                netweightingrams_bottomsheetDialog = netweightingrams_bottomsheetDialog.replaceAll("[^\\d.]", "");

            } catch (Exception e) {
                netweightingrams_bottomsheetDialog = "";
                e.printStackTrace();
            }
        }
        catch (Exception e ){
            e.printStackTrace();
        }


        Modal_MenuItemCutDetails modal_menuItemCutDetails = new Modal_MenuItemCutDetails();
        modal_menuItemCutDetails.grossweight = grossweight_bottomsheetDialog_cutDetails;

        if(!cutname_bottomsheetDialog_cutDetails.equals("")){

            modal_menuItemCutDetails.cutname = cutname_bottomsheetDialog_cutDetails;

        }
        else{
            modal_menuItemCutDetails.cutname ="";
        }



        if(!netweight_bottomsheetDialog_cutDetails.equals("000g - 000g")){
            modal_menuItemCutDetails.netweight = netweight_bottomsheetDialog_cutDetails;
            modal_menuItemCutDetails.netweightingrams = netweightingrams_bottomsheetDialog;
        }
        else{
            modal_menuItemCutDetails.netweight ="";
            modal_menuItemCutDetails.netweightingrams ="";
        }



        modal_menuItemCutDetails.portionsize = portionsize_bottomsheetDialog_cutDetails;
        modal_menuItemCutDetails.cutdisplayno = cutDisplayNo_bottomsheetDialog_cutDetails;
        modal_menuItemCutDetails.isdefault = isdefault_bottomsheetDialog_cutDetails;
        modal_menuItemCutDetails.cutdesp = cutDesp_bottomsheetDialog_cutDetails1;
        modal_menuItemCutDetails.cutkey = cutkey_bottomsheetDialog_cutDetails;
        modal_menuItemCutDetails.cutimagename = cutImage_bottomsheetDialog_cutDetails;

        try {
            cutDetailsKey_arrayList.add(cutkey_bottomsheetDialog_cutDetails);
            cutDetailsArray.add(modal_menuItemCutDetails);
            itemcutdetailsString = "cutdetails added";
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            FormatAndDisplaytheDataa();
            bottomSheetDialog.cancel();


        } catch (Exception e) {
            e.printStackTrace();
        }




    }




    private void ChangeMarinadeMenuItemPriceInDB(String menuitemKey, String appPrice, String posPrice, String appliedDiscountPercentage) {
        showProgressBar(true);

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
                showProgressBar(false);
                Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Price was Not Updated. Check Your Network Connection,T", Toast.LENGTH_LONG).show();

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






    private void ChangeMenuItemDataInDB(String appPrice, String posPrice) {
        showProgressBar(true);


        try {
            JSONArray jsonArray_CutDetails = new JSONArray();
            JSONArray jsonArray_weightDetails = new JSONArray();
            if(cutDetailsArray.size()>0){
                for (int j = 0; j < cutDetailsArray.size(); j++) {
                    Modal_MenuItemCutDetails modal_menuItemCutDetails = cutDetailsArray.get(j);
                    JSONObject jsonObject_CutDetails = new JSONObject();
                    String cutdesp="",cutdisplayno="",cutimagename="",cutkey="",cutname="",isdefault="",netweight="",netweightingrams="",portionsize="";
                    try{
                        cutdesp = String.valueOf( modal_menuItemCutDetails.getCutdesp().toString());
                        if(cutdesp.equals(null)||cutdesp.equals("null")){
                            cutdesp="-";
                        }
                    }
                    catch(Exception e){
                        cutdesp="--";
                        e.printStackTrace();
                    }



                    try{
                        cutdisplayno = String.valueOf( modal_menuItemCutDetails.getCutdisplayno().toString());
                        if(cutdisplayno.equals(null)||cutdisplayno.equals("null")){
                            cutdisplayno="-";
                        }
                    }
                    catch(Exception e){
                        cutdisplayno="--";
                        e.printStackTrace();
                    }

                    try{
                        cutimagename = String.valueOf( modal_menuItemCutDetails.getCutimagename().toString());
                        if(cutimagename.equals(null)||cutimagename.equals("null")){
                            cutimagename="-";
                        }
                    }
                    catch(Exception e){
                        cutimagename="--";
                        e.printStackTrace();
                    }

                    try{
                        cutkey = String.valueOf( modal_menuItemCutDetails.getCutkey().toString());
                        if(cutkey.equals(null)||cutkey.equals("null")){
                            cutkey="-";
                        }
                    }
                    catch(Exception e){
                        cutkey="--";
                        e.printStackTrace();
                    }



                    try{
                        cutname = String.valueOf( modal_menuItemCutDetails.getCutname().toString());
                        if(cutname.equals(null)||cutname.equals("null")){
                            cutname="-";
                        }
                    }
                    catch(Exception e){
                        cutname="--";
                        e.printStackTrace();
                    }

                    try{
                        isdefault = String.valueOf( modal_menuItemCutDetails.getIsdefault().toString());
                        if(isdefault.equals(null)||isdefault.equals("null")){
                            isdefault="-";
                        }
                    }
                    catch(Exception e){
                        isdefault="--";
                        e.printStackTrace();
                    }

                    try{
                        netweight = String.valueOf( modal_menuItemCutDetails.getNetweight().toString());
                        if(netweight.equals(null)||netweight.equals("null")){
                            netweight="-";
                        }
                    }
                    catch(Exception e){
                        netweight="--";
                        e.printStackTrace();
                    }

                    try{
                        netweightingrams = String.valueOf( modal_menuItemCutDetails.getNetweightingrams().toString());
                        if(netweightingrams.equals(null)||netweightingrams.equals("null")){
                            netweightingrams="0";
                        }
                    }
                    catch(Exception e){
                        netweightingrams="00";
                        e.printStackTrace();
                    }

                    try{
                        portionsize = String.valueOf( modal_menuItemCutDetails.getPortionsize().toString());
                        if(portionsize.equals(null)||portionsize.equals("null")){
                            portionsize="-";
                        }
                    }
                    catch(Exception e){
                        portionsize="--";
                        e.printStackTrace();
                    }


                    try {
                        jsonObject_CutDetails.put("cutdesp",cutdesp);
                        jsonObject_CutDetails.put("cutdisplayno",cutdisplayno );
                        jsonObject_CutDetails.put("cutimagename",cutimagename);
                        jsonObject_CutDetails.put("cutkey",cutkey);
                        jsonObject_CutDetails.put("cutname", cutname);
                        jsonObject_CutDetails.put("isdefault", isdefault);
                        jsonObject_CutDetails.put("netweight",netweight);

                        netweightingrams = netweightingrams.replaceAll("[^\\d.]", "");
                        if(!netweightingrams.equals("")){
                            jsonObject_CutDetails.put("netweightingrams", Integer.parseInt(netweightingrams));

                        }
                        jsonObject_CutDetails.put("portionsize", portionsize);
                        jsonArray_CutDetails.put(jsonObject_CutDetails);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
            if(weightDetailsArray.size()>0){
                for (int j = 0; j < weightDetailsArray.size(); j++) {
                    Modal_MenuItemWeightDetails modal_menuItemWeightDetails = weightDetailsArray.get(j);
                    JSONObject jsonObject_weightDetails = new JSONObject();
                    String displayno="",grossweightingrams="0",weightkey="",weight="",isdefault="",netweight="",netweightingrams="0",portionsize="",grossweight="";
                    try{
                        displayno = String.valueOf( modal_menuItemWeightDetails.getWeightdisplayno().toString());
                        if(displayno.equals(null)||displayno.equals("null")){
                            displayno="-";
                        }
                    }
                    catch(Exception e){
                        displayno="--";
                        e.printStackTrace();
                    }
                    try{
                        grossweight = String.valueOf( modal_menuItemWeightDetails.getGrossweight().toString());
                        if(grossweight.equals(null)||grossweight.equals("null")){
                            grossweight="-";
                        }
                    }
                    catch(Exception e){
                        grossweight="--";
                        e.printStackTrace();
                    }



                    try{
                        grossweightingrams = String.valueOf( modal_menuItemWeightDetails.getGrossweightingrams().toString());
                        if(grossweightingrams.equals(null)||grossweightingrams.equals("null")){
                            grossweightingrams="0";
                        }
                    }
                    catch(Exception e){
                        grossweightingrams="00";
                        e.printStackTrace();
                    }

                    try{
                        weightkey = String.valueOf( modal_menuItemWeightDetails.getWeightkey().toString());
                        if(weightkey.equals(null)||weightkey.equals("null")){
                            weightkey="-";
                        }
                    }
                    catch(Exception e){
                        weightkey="--";
                        e.printStackTrace();
                    }

                    try{
                        weight = String.valueOf( modal_menuItemWeightDetails.getWeight().toString());
                        if(weight.equals(null)||weight.equals("null")){
                            weight="-";
                        }
                    }
                    catch(Exception e){
                        weight="--";
                        e.printStackTrace();
                    }


                    try{
                        isdefault = String.valueOf( modal_menuItemWeightDetails.getIsdefault().toString());
                        if(isdefault.equals(null)||isdefault.equals("null")){
                            isdefault="-";
                        }
                    }
                    catch(Exception e){
                        isdefault="--";
                        e.printStackTrace();
                    }

                    try{
                        netweight = String.valueOf( modal_menuItemWeightDetails.getNetweight().toString());
                        if(netweight.equals(null)||netweight.equals("null")){
                            netweight="-";
                        }
                    }
                    catch(Exception e){
                        netweight="--";
                        e.printStackTrace();
                    }

                    try{
                        netweightingrams = String.valueOf( modal_menuItemWeightDetails.getNetweightingrams().toString());
                        if(netweightingrams.equals(null)||netweightingrams.equals("null")){
                            netweightingrams="0";
                        }
                    }
                    catch(Exception e){
                        netweightingrams="00";
                        e.printStackTrace();
                    }

                    try{
                        portionsize = String.valueOf( modal_menuItemWeightDetails.getPortionsize().toString());
                        if(portionsize.equals(null)||portionsize.equals("null")){
                            portionsize="-";
                        }
                    }
                    catch(Exception e){
                        portionsize="--";
                        e.printStackTrace();
                    }

                    try {
                        jsonObject_weightDetails.put("displayno", displayno);
                        jsonObject_weightDetails.put("grossweight", grossweight);
                        grossweightingrams = grossweightingrams.replaceAll("[^\\d.]", "");
                        if(grossweightingrams.equals("")){
                            grossweightingrams = "000";
                        }
                        jsonObject_weightDetails.put("grossweightingrams", Integer.parseInt(grossweightingrams));
                        jsonObject_weightDetails.put("isdefault",isdefault);
                        jsonObject_weightDetails.put("netweight",netweight);

                        netweightingrams = netweightingrams.replaceAll("[^\\d.]", "");
                        if(netweightingrams.equals("")){
                            netweightingrams = "000";
                        }
                        jsonObject_weightDetails.put("netweightingrams", Integer.parseInt(netweightingrams));
                        jsonObject_weightDetails.put("portionsize",portionsize);
                        jsonObject_weightDetails.put("weight",weight);

                        jsonObject_weightDetails.put("weightkey", weightkey);
                        jsonArray_weightDetails.put(jsonObject_weightDetails);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }



            //Log.d(TAG, " uploaduserDatatoDB.");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("key", menuitemKey);
             /*   jsonObject.put("tmcprice", appPrice);
                jsonObject.put("tmcpriceperkg", posPrice);
                jsonObject.put("grossweight", grossweight);
                jsonObject.put("portionsize", portionsize);

                grossweightingrams = grossweightingrams.replaceAll("[^\\d.]", "");
                if(grossweightingrams.equals("")){
                    grossweightingrams = "00000";
                }
                jsonObject.put("grossweightingrams", Integer.parseInt(grossweightingrams));

                jsonObject.put("netweight", netweight);
                jsonObject.put("swiggyprice", swiggyPrice);
                jsonObject.put("dunzoprice", dunzoPrice);
                jsonObject.put("bigbasketprice", bigBasketPrice);
                jsonObject.put("applieddiscountpercentage", appliedDiscountPercentage);

              */
                if(weightDetailsArray.size()>0) {
                    jsonObject.put("itemweightdetails", jsonArray_weightDetails);

                }

                if(cutDetailsArray.size()>0) {
                    jsonObject.put("itemcutdetails", jsonArray_CutDetails);

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMenuItemDetails,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {
                    //Log.d(Constants.TAG, "Response: " + response);
                     ChangeMenuItemDataInSharedPreferenes(menuitemKey,jsonObject);
                    showProgressBar(false);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    showProgressBar(false);
                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Price was Not Updated. Check Your Network Connection,T", Toast.LENGTH_LONG).show();

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
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void ChangeMenuItemDataInSharedPreferenes(String menuitemKey, JSONObject jsonObject) {




        try {
            for (int i = 0; i < MenuItem.size(); i++) {
                Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
                String menuItemId = modal_menuItemSettings.getMenuItemId();
                if (menuItemId.equals(menuitemKey)) {

                    try {
                        if(jsonObject.has("applieddiscountpercentage")) {
                            modal_menuItemSettings.setApplieddiscountpercentage(jsonObject.getString("applieddiscountpercentage"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    try {
                        if(jsonObject.has("tmcprice")) {
                            modal_menuItemSettings.setTmcprice(jsonObject.getString("tmcprice"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if(jsonObject.has("tmcpriceperkg")) {
                            modal_menuItemSettings.setTmcpriceperkg(jsonObject.getString("tmcpriceperkg"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if(jsonObject.has("grossweight")) {
                            modal_menuItemSettings.setGrossweight(jsonObject.getString("grossweight"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if(jsonObject.has("netweight")) {
                            modal_menuItemSettings.setNetweight(jsonObject.getString("netweight"));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if(jsonObject.has("grossweightingrams")) {
                            modal_menuItemSettings.setGrossweightingrams(jsonObject.getString("grossweightingrams"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                    try {
                        if(jsonObject.has("swiggyprice")) {
                            modal_menuItemSettings.setSwiggyprice(jsonObject.getString("swiggyprice"));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if(jsonObject.has("dunzoprice")) {
                            modal_menuItemSettings.setDunzoprice(jsonObject.getString("dunzoprice"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if(jsonObject.has("bigBasketPrice")) {
                            modal_menuItemSettings.setBigbasketprice(jsonObject.getString("bigBasketPrice"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if(jsonObject.has("itemweightdetails")) {
                            modal_menuItemSettings.setItemweightdetails(jsonObject.getString("itemweightdetails"));
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if(jsonObject.has("itemcutdetails")) {
                            modal_menuItemSettings.setItemcutdetails(jsonObject.getString("itemcutdetails"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                savedMenuIteminSharedPrefrences(MenuItem);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



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

    private void savedMarinadeMenuIteminSharedPrefrences(List<Modal_MenuItem_Settings> menuItem) {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MarinadeMenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MarinadeMenuList", json);
        editor.apply();
    }



    private String getMenuItemWeightData(int position, String fieldName) {
        String data="";
        try {
            JSONArray jsonArray = new JSONArray(MenuItemWeightdetailsString);
            JSONObject json = jsonArray.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }


    //Doing the same with this method as we did with getName()
    private String getMenuItemCutData(int position, String fieldName){
        String data="";
        try {
            JSONArray jsonArray = new JSONArray(MenuItemCutdetailsString);
            JSONObject json = jsonArray.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;

    }

    private void savedMenuIteminSharedPrefrences(List<Modal_MenuItem_Settings> menuItem) {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList", json);
        editor.apply();
    }

    private void changeDataInLocalMenuArray(String variableName, String variableValue, String menuitemKey)
    {
        showProgressBar(true);
        if(variableName.equals("grossweight")){
            try{
                grossweight = String.valueOf(variableValue+"g");

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                grossweightingrams = String.valueOf(variableValue);

            } catch (Exception e){
                e.printStackTrace();
            }
        }

        else if(variableName.equals("netweight")) {
                try{
                    if((netweight.toUpperCase().contains("PCS"))||(netweight.toUpperCase().contains("PC"))){
                        try{

                            netweight = String.valueOf(variableValue+"Pcs");

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    if((netweight.toUpperCase().contains("G"))||(netweight.toUpperCase().contains("GRAMS"))||(netweight.toUpperCase().contains("GMS"))){
                        try{
                            netweight = String.valueOf(variableValue+"g");

                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


        }

        else if(variableName.equals("portionsize")) {

            if((portionsize.toUpperCase().contains("PCS"))||(portionsize.toUpperCase().contains("PC"))){
                try{
                    portionsize = String.valueOf(variableValue);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            if((portionsize.toUpperCase().contains("G"))||(portionsize.toUpperCase().contains("GRAMS"))||(portionsize.toUpperCase().contains("GMS"))){
                try{
                    portionsize = String.valueOf(variableValue);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }



        }





        try{
            FormatAndDisplaytheDataa();



        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            isPosPrice_PricePerKgChanged = false;
            isAppPrice_PricePerKgChanged = true;
            computeAppandPosPrice.performClick();


        }
        catch (Exception e){
            e.printStackTrace();
        }




        /*
        for (int i = 0; i < MenuItem.size(); i++) {
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String menuItemId = modal_menuItemSettings.getMenuItemId();
            if (menuItemId.equals(menuitemKey)) {
                if(variableName.equals("grossweight")){
                    modal_menuItemSettings.setGrossweight(variableValue+"g");
                    modal_menuItemSettings.setGrossweightingrams(variableValue);
                }
                else if(variableName.equals("netweight")) {

                    modal_menuItemSettings.setNetweight(variableValue);

                }

                else if(variableName.equals("portionsize")) {
                    modal_menuItemSettings.setPortionsize(variableValue);


                }
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

                saveMenuIteminSharedPreference(MenuItem);


            }
            }
         */






            }

    private void showProgressBar(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

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
                    showProgressBar(false);
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




    private void getMenuItembasedOnkey(String key) {
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
                        appPrice_PricePerKg =  String.valueOf(modal_menuItemSettings.getTmcprice());

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
                        pricetypeforPos = String.valueOf(modal_menuItemSettings.getPricetypeforpos().toUpperCase());
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
                        if(pricetypeforPos.equals(Constants.TMCPRICE)){
                            sellingprice = CalculateSellingPrice(appPrice, appliedDiscountPercentage);
                            isTMCPrice =true;

                        }
                        if(pricetypeforPos.equals(Constants.TMCPRICEPERKG)){
                            appSellingPrice_priceperKg = CalculateSellingPriceforTMCPricePerKgforApp(finalweight,appPrice, appliedDiscountPercentage);
                            posSellingPrice_priceperKg = CalculateSellingPriceforTMCPricePerKgforPOS(posPrice, appliedDiscountPercentage);

                            isTMCPrice =false;
                        }



                        try{
                            itemcutdetailsString = String.valueOf(modal_menuItemSettings.getItemcutdetails());
                            if (!itemcutdetailsString.equals("") && !itemcutdetailsString.equals("nil")) {

                                addCutDetailsinArray(itemcutdetailsString);
                            }
                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }


                        try{
                            itemweightdetailsString = String.valueOf(modal_menuItemSettings.getItemweightdetails());

                            if (!itemweightdetailsString.equals("") && !itemweightdetailsString.equals("nil")) {
                                addweightDetailsinArray(itemweightdetailsString);
                            }
                        }
                        catch (Exception e){

                            e.printStackTrace();
                        }



                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    FormatAndDisplaytheDataa();


                    //Log.d(Constants.TAG, "displaying_menuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));


                //    showProgressBar(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


    }

    public  void FormatAndDisplaytheDataa(){
        try {
            if (pricetypeforPos.equals(Constants.TMCPRICE)) {
                cutDetailsListLayout.setVisibility(View.GONE);
                itemweightdetailslistlayout.setVisibility(View.GONE);
                defaultitemweightdetailslayout.setVisibility(View.VISIBLE);
                defaultGrossweight_textonly_widget.setVisibility(View.GONE);
                defaultnetweight_textonly_widget.setVisibility(View.GONE);
                try {


                    if (((!portionsize.toUpperCase().contains("PCS")) && (!portionsize.toUpperCase().contains("PC")) && (!portionsize.toUpperCase().contains("G")) && (!portionsize.toUpperCase().contains("GMS")) && (!portionsize.toUpperCase().contains("GRAMS"))) || (portionsize.toUpperCase().contains("SERVINGS"))) {
                        //defaultportionsize_textonly_widget.setVisibility(View.VISIBLE);
                       // defaultportionsize_textonly_widget.setText(portionsize);
                       // defaultportionsize_textview_widget.setVisibility(View.GONE);
                        defaultportionsize_textview_widget.setVisibility(View.VISIBLE);
                        defaultportionsize_textview_widget.setText(portionsize);
                        defaultportionsize_textonly_widget.setVisibility(View.GONE);

                    } else {
                        defaultportionsize_textview_widget.setVisibility(View.VISIBLE);
                        defaultportionsize_textview_widget.setText(portionsize);
                        defaultportionsize_textonly_widget.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (pricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                try {

                    if (!itemcutdetailsString.equals("") && !itemcutdetailsString.equals("nil")) {
                        itemCutDetails_Listview.setVisibility(View.VISIBLE);
                        addnewcutDetails_imageview.setVisibility(View.GONE);
                        add_new_cutDetails_button_Layout.setVisibility(View.VISIBLE);

                       // addCutDetailsinArray(itemcutdetailsString);

                    } else {

                        addnewcutDetails_imageview.setVisibility(View.VISIBLE);
                        add_new_cutDetails_button_Layout.setVisibility(View.GONE);

                        itemCutDetails_Listview.setVisibility(View.GONE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!itemweightdetailsString.equals("") && !itemweightdetailsString.equals("nil")) {
                       /// addweightDetailsinArray(itemweightdetailsString);
                        itemweightDetails_Listview.setVisibility(View.VISIBLE);
                        addnewweightDetails_imageview.setVisibility(View.GONE);
                        add_new_weightDetails_button_Layout.setVisibility(View.VISIBLE);
                        defaultGrossweight_textonly_widget.setVisibility(View.VISIBLE);
                        defaultgrossweight_text_widget.setVisibility(View.GONE);


                        defaultportionsize_textonly_widget.setVisibility(View.VISIBLE);
                        defaultportionsize_textonly_widget.setText(portionsize);
                       defaultportionsize_textview_widget.setVisibility(View.GONE);


                    } else {
                        itemweightDetails_Listview.setVisibility(View.GONE);
                        addnewweightDetails_imageview.setVisibility(View.VISIBLE);
                        add_new_weightDetails_button_Layout.setVisibility(View.GONE);
                        defaultnetweight_textonly_widget.setVisibility(View.GONE);
                        defaultGrossweight_textonly_widget.setVisibility(View.GONE);
                        defaultgrossweight_text_widget.setVisibility(View.VISIBLE);

                        defaultportionsize_textview_widget.setVisibility(View.VISIBLE);
                        defaultportionsize_textview_widget.setText(portionsize);
                        defaultportionsize_textonly_widget.setVisibility(View.GONE);


                        try {


                            if (((!portionsize.toUpperCase().contains("PCS")) && (!portionsize.toUpperCase().contains("PC")) && (!portionsize.toUpperCase().contains("G")) && (!portionsize.toUpperCase().contains("GMS")) && (!portionsize.toUpperCase().contains("GRAMS"))) || (portionsize.toUpperCase().contains("SERVINGS"))) {
                                //defaultportionsize_textonly_widget.setVisibility(View.VISIBLE);
                               // defaultportionsize_textonly_widget.setText(portionsize);
                               // defaultportionsize_textview_widget.setVisibility(View.GONE);
                                defaultportionsize_textview_widget.setVisibility(View.VISIBLE);
                                defaultportionsize_textview_widget.setText(portionsize);
                                defaultportionsize_textonly_widget.setVisibility(View.GONE);

                            } else {
                                defaultportionsize_textview_widget.setVisibility(View.VISIBLE);
                                defaultportionsize_textview_widget.setText(portionsize);
                                defaultportionsize_textonly_widget.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


/*
        try{
            if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                grossweight_textview_widget.setText(finalweight+" gms");


            }
            else{
                grossweight_textview_widget.setText(finalweight);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



 */


            try {

                if (netweight.contains("to") || netweight.contains("-")) {
                    two_box_linearlayout.setVisibility(View.VISIBLE);
                    defaultnetweight_textview_widget.setVisibility(View.GONE);


                    isNetweight_singleEditText = false;
                    if (netweight.contains("to")) {
                        String[] split = netweight.split("to");
                        String firstSubString = split[0];
                        String secondSubString = split[1];
                        firstSubString = firstSubString.trim();
                        secondSubString = secondSubString.trim();
                        netweight_first_textview_widget.setText(firstSubString);
                        netweight_second_textview_widget.setText(secondSubString);
                    } else {
                        String[] split = netweight.split("-");
                        String firstSubString = split[0];
                        String secondSubString = split[1];
                        firstSubString = firstSubString.trim();
                        secondSubString = secondSubString.trim();
                        netweight_first_textview_widget.setText(firstSubString);
                        netweight_second_textview_widget.setText(secondSubString);


                    }


                } else {
                    two_box_linearlayout.setVisibility(View.GONE);
                    defaultnetweight_textview_widget.setVisibility(View.VISIBLE);
                    isNetweight_singleEditText = true;


                    defaultnetweight_textview_widget.setText(netweight);

                }

                if (!itemweightdetailsString.equals("") && !itemweightdetailsString.equals("nil")) {
                    two_box_linearlayout.setVisibility(View.GONE);
                    defaultnetweight_textview_widget.setVisibility(View.GONE);
                    defaultnetweight_textonly_widget.setVisibility(View.VISIBLE);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            pricetype_textview_widget.setText(String.valueOf(pricetypeforPos).toLowerCase());
            grossweight_textview_widget.setText(String.valueOf(grossweight));
            netweight_textview_widget.setText(String.valueOf(netweight));
            portionsize_textview_widget.setText(String.valueOf(portionsize));

            itemUniqueCode_text_widget.setText(itemUniqueCode);
            itemName_textview_widget.setText(itemName);
            app_price_textview_widget.setText(appPrice + " ₹");
            pos_price_textview_widget.setText(posPrice + " ₹");
            appliedDiscountPercentage_text_widget.setText(String.valueOf(appliedDiscountPercentage));
            appPrice_text_widget_appLayout.setText(appPrice_PricePerKg);
            posPrice_text_widget_posLayout.setText(posPrice_pricePerKg);

            defaultnetweight_textonly_widget.setText(netweight);
            defaultgrossweight_text_widget.setText(grossweight);
            defaultGrossweight_textonly_widget.setText(grossweight);
            defaultportionsize_textview_widget.setText(portionsize);


            showProgressBar(false);
            //swiggy_selling_price_text_widget_swiggyLayout.setText(swiggyPrice);
            // dunzo_selling_price_text_widget_dunzoLayout.setText(dunzoPrice);
            //  bigbasket_selling_price_text_widget_bigbasketLayout.setText(bigBasketPrice);

            int finalsellingprice, finalAppSellingPrice, finalPosSellingPrice;

            try {
                finalsellingprice = (int) Math.ceil(sellingprice);
            } catch (Exception e) {
                finalsellingprice = 00;
                e.printStackTrace();
            }


            try {
                finalAppSellingPrice = (int) Math.ceil(appSellingPrice_priceperKg);
            } catch (Exception e) {
                finalAppSellingPrice = 00;
                e.printStackTrace();
            }


            try {
                finalPosSellingPrice = (int) Math.ceil(posSellingPrice_priceperKg);
            } catch (Exception e) {
                finalPosSellingPrice = 00;
                e.printStackTrace();
            }


            selling_price_text_widget.setText(String.valueOf(finalsellingprice));
            appPrice_text_widget.setText(String.valueOf(appPrice + " ₹"));
            posPrice_text_widget.setText(String.valueOf(posPrice + " ₹"));
            swiggy_selling_price_text_widget_swiggyLayout.setText(swiggyPrice);
            dunzo_selling_price_text_widget_dunzoLayout.setText(dunzoPrice);
            bigbasket_selling_price_text_widget_bigbasketLayout.setText(bigBasketPrice);

            app_selling_price_text_widget_appLayout.setText(String.valueOf(finalAppSellingPrice));
            pos_selling_price_text_widget_posLayout.setText(String.valueOf(finalPosSellingPrice));


            Adapter_MenuItemCutDetails adapter_menuItemCutDetails = new Adapter_MenuItemCutDetails(ChangeMenuItemWeightAndPriceSecondScreen.this, cutDetailsArray, ChangeMenuItemWeightAndPriceSecondScreen.this, MenuItemCutdetailsString);
            itemCutDetails_Listview.setAdapter(adapter_menuItemCutDetails);
            helper_weight_cut_Listview.getListViewSize(itemCutDetails_Listview, screenInches);


            Adapter_MenuItemWeightDetails adapter_menuItemWeightDetails = new Adapter_MenuItemWeightDetails(ChangeMenuItemWeightAndPriceSecondScreen.this, weightDetailsArray, ChangeMenuItemWeightAndPriceSecondScreen.this,MenuItemWeightdetailsString);
            itemweightDetails_Listview.setAdapter(adapter_menuItemWeightDetails);
            helper_weight_cut_Listview.getListViewSize(itemweightDetails_Listview, screenInches);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addweightDetailsinArray(String itemweightdetailsStringg) {
        weightDetailsArray.clear();
        weightDetailsKey_arrayList.clear();

        try {
            JSONArray jsonarray =  new JSONArray(itemweightdetailsStringg);

        for (int i = 0; i < jsonarray.length(); i++) {
            try{
                JSONObject jsonObject = jsonarray.getJSONObject(i);
                    try{
                        Modal_MenuItemWeightDetails modal_menuItemWeightDetails = new Modal_MenuItemWeightDetails();
                        try{
                            if(jsonObject.has("grossweight")){


                                modal_menuItemWeightDetails.grossweight = String.valueOf(jsonObject.get("grossweight"));


                            }
                            else{
                                modal_menuItemWeightDetails.grossweight = "";
                            }
                        }
                        catch (Exception e){
                            modal_menuItemWeightDetails.grossweight = "";

                            e.printStackTrace();
                        }


                        try{
                            if(jsonObject.has("grossweightingrams")){


                                modal_menuItemWeightDetails.grossweightingrams = String.valueOf(jsonObject.get("grossweightingrams"));


                            }
                            else{
                                modal_menuItemWeightDetails.grossweightingrams = "";
                            }
                        }
                        catch (Exception e){
                            modal_menuItemWeightDetails.grossweightingrams = "";

                            e.printStackTrace();
                        }


                        try{
                            if(jsonObject.has("isdefault")){


                                modal_menuItemWeightDetails.isdefault = String.valueOf(jsonObject.get("isdefault"));


                            }
                            else{
                                modal_menuItemWeightDetails.isdefault = "";
                            }
                        }
                        catch (Exception e){
                            modal_menuItemWeightDetails.isdefault = "";

                            e.printStackTrace();
                        }

                        try{
                            if(jsonObject.has("netweight")){


                                modal_menuItemWeightDetails.netweight = String.valueOf(jsonObject.get("netweight"));


                            }
                            else{
                                modal_menuItemWeightDetails.netweight = "";
                            }
                        }
                        catch (Exception e){
                            modal_menuItemWeightDetails.netweight = "";

                            e.printStackTrace();
                        }



                        try{
                            if(jsonObject.has("netweightingrams")){


                                modal_menuItemWeightDetails.netweightingrams = String.valueOf(jsonObject.get("netweightingrams"));


                            }
                            else{
                                modal_menuItemWeightDetails.netweightingrams = "";
                            }
                        }
                        catch (Exception e){
                            modal_menuItemWeightDetails.netweightingrams = "";

                            e.printStackTrace();
                        }

                        try{
                            if(jsonObject.has("portionsize")){


                                modal_menuItemWeightDetails.portionsize = String.valueOf(jsonObject.get("portionsize"));


                            }
                            else{
                                modal_menuItemWeightDetails.portionsize = "";
                            }
                        }
                        catch (Exception e){
                            modal_menuItemWeightDetails.portionsize = "";

                            e.printStackTrace();
                        }




                        try{
                            if(jsonObject.has("weightdisplayno")){


                                modal_menuItemWeightDetails.weightdisplayno = String.valueOf(jsonObject.get("weightdisplayno"));


                            }
                            else{
                                modal_menuItemWeightDetails.weightdisplayno = "";
                            }
                        }
                        catch (Exception e){
                            modal_menuItemWeightDetails.weightdisplayno = "";

                            e.printStackTrace();
                        }



                         try{
                            if(jsonObject.has("weightkey")){


                                modal_menuItemWeightDetails.weightkey = String.valueOf(jsonObject.get("weightkey"));

                                weightDetailsKey_arrayList.add(String.valueOf(jsonObject.get("weightkey")));

                            }
                            else{
                                weightDetailsKey_arrayList.add("");

                                modal_menuItemWeightDetails.weightkey = "";
                            }
                        }
                        catch (Exception e){
                            weightDetailsKey_arrayList.add("");

                            modal_menuItemWeightDetails.weightkey = "";

                            e.printStackTrace();
                        }

                        weightDetailsArray.add(modal_menuItemWeightDetails);

                       /* if(jsonarray.length()-1==i){
                            Adapter_MenuItemWeightDetails adapter_menuItemWeightDetails = new Adapter_MenuItemWeightDetails(ChangeMenuItemWeightAndPriceSecondScreen.this,weightDetailsArray,ChangeMenuItemWeightAndPriceSecondScreen.this);
                            itemweightDetails_Listview.setAdapter(adapter_menuItemWeightDetails);

                            helper_weight_cut_Listview.getListViewSize(itemweightDetails_Listview, screenInches);

                        }

                        */



                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


            }
            catch (JSONException e){
                e.printStackTrace();
            }


        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }

    private void  addCutDetailsinArray(String itemcutdetailsStringg) {
        cutDetailsArray.clear();

        cutDetailsName_arrayList.clear();

        try {
            JSONArray jsonarray =  new JSONArray(itemcutdetailsStringg);

            for (int i = 0; i < jsonarray.length(); i++) {
                try{
                    JSONObject jsonObject = jsonarray.getJSONObject(i);
                    try{
                        Modal_MenuItemCutDetails modalMenuItemCutDetails = new Modal_MenuItemCutDetails();
                        try{
                            if(jsonObject.has("grossweight")){


                                modalMenuItemCutDetails.grossweight = String.valueOf(jsonObject.get("grossweight"));


                            }
                            else{
                                modalMenuItemCutDetails.grossweight = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.grossweight = "";

                            e.printStackTrace();
                        }


                        try{
                            if(jsonObject.has("netweightingrams")){


                                modalMenuItemCutDetails.netweightingrams = String.valueOf(jsonObject.get("netweightingrams"));


                            }
                            else{
                                modalMenuItemCutDetails.netweightingrams = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.netweightingrams = "";

                            e.printStackTrace();
                        }


                        try{
                            if(jsonObject.has("isdefault")){


                                modalMenuItemCutDetails.isdefault = String.valueOf(jsonObject.get("isdefault"));


                            }
                            else{
                                modalMenuItemCutDetails.isdefault = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.isdefault = "";

                            e.printStackTrace();
                        }

                        try{
                            if(jsonObject.has("netweight")){


                                modalMenuItemCutDetails.netweight = String.valueOf(jsonObject.get("netweight"));


                            }
                            else{
                                modalMenuItemCutDetails.netweight = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.netweight = "";

                            e.printStackTrace();
                        }



                        try{
                            if(jsonObject.has("netweightingrams")){


                                modalMenuItemCutDetails.netweightingrams = String.valueOf(jsonObject.get("netweightingrams"));


                            }
                            else{
                                modalMenuItemCutDetails.netweightingrams = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.netweightingrams = "";

                            e.printStackTrace();
                        }

                        try{
                            if(jsonObject.has("portionsize")){


                                modalMenuItemCutDetails.portionsize = String.valueOf(jsonObject.get("portionsize"));


                            }
                            else{
                                modalMenuItemCutDetails.portionsize = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.portionsize = "";

                            e.printStackTrace();
                        }




                        try{
                            if(jsonObject.has("cutdisplayno")){


                                modalMenuItemCutDetails.cutdisplayno = String.valueOf(jsonObject.get("cutdisplayno"));


                            }
                            else{
                                modalMenuItemCutDetails.cutdisplayno = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.cutdisplayno = "";

                            e.printStackTrace();
                        }



                        try{
                            if(jsonObject.has("cutname")){


                                modalMenuItemCutDetails.cutname = String.valueOf(jsonObject.get("cutname"));


                            }
                            else{


                                modalMenuItemCutDetails.cutname = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.cutname = "";

                            e.printStackTrace();
                        }



                        try{
                            if(jsonObject.has("cutimagename")){


                                modalMenuItemCutDetails.cutimagename = String.valueOf(jsonObject.get("cutimagename"));


                            }
                            else{
                                modalMenuItemCutDetails.cutimagename = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.cutimagename = "";

                            e.printStackTrace();
                        }

                        try{
                            if(jsonObject.has("cutdesp")){


                                modalMenuItemCutDetails.cutdesp = String.valueOf(jsonObject.get("cutdesp"));


                            }
                            else{
                                modalMenuItemCutDetails.cutdesp = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.cutdesp = "";

                            e.printStackTrace();
                        }



                        try{
                            if(jsonObject.has("cutkey")){


                                modalMenuItemCutDetails.cutkey = String.valueOf(jsonObject.get("cutkey"));


                            }
                            else{
                                modalMenuItemCutDetails.cutkey = "";
                            }
                        }
                        catch (Exception e){
                            modalMenuItemCutDetails.cutkey = "";

                            e.printStackTrace();
                        }

                        cutDetailsArray.add(modalMenuItemCutDetails);

                       /* if(jsonarray.length()-1==i){
                            Adapter_MenuItemCutDetails adapter_menuItemCutDetails = new Adapter_MenuItemCutDetails(ChangeMenuItemWeightAndPriceSecondScreen.this,cutDetailsArray,ChangeMenuItemWeightAndPriceSecondScreen.this);
                            itemCutDetails_Listview.setAdapter(adapter_menuItemCutDetails);

                            helper_weight_cut_Listview.getListViewSize(itemCutDetails_Listview, screenInches);

                        }


                        */


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                }
                catch (JSONException e){
                    e.printStackTrace();
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            finalweight  = grossweight_textview_widget.getText().toString();
            finalweight = finalweight.replaceAll("[^\\d.]", "");
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
                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
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
            appPriceint = (int) Math.ceil(MarinadeappPricedouble);
            posPriceint = (int) Math.ceil(MarinadeposPricedouble);

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

    private void CalculateAppPriceAndPosPrice(double sellingprice, int discount_percentage) {
        if(sellingprice>0) {
            showProgressBar(true);

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
                finalweight  = grossweight_textview_widget.getText().toString();
                finalweight = finalweight.replaceAll("[^\\d.]", "");
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
                    Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
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

            try{
                app_price_textview_widget.setText(String.valueOf(appPriceint));
                pos_price_textview_widget.setText(String.valueOf(posPriceint));


            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else{


            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }

        showProgressBar(false);




    }


    public void CalculatePosPrice(double pos_sellingprice, int discount_percentage) {
        if(pos_sellingprice >0) {
            showProgressBar(true);

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
                finalweight  = grossweight_textview_widget.getText().toString();
                finalweight = finalweight.replaceAll("[^\\d.]", "");
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
                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
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
                posPriceint = (int) Math.ceil(posPricedouble__priceperKg);

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

            try{
                pos_price_textview_widget.setText(String.valueOf(posPriceint));


            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        else{


            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "Pos Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }
        showProgressBar(false);
    }


    public void CalculateAppPrice(double app_sellingprice, int discount_percentage) {
        if(app_sellingprice>0) {
            showProgressBar(true);

            double sellingPrice_withoutDiscount = 0;
            double discountAmount = 0;
            int weightindouble = 0;
            int appPriceint = 0;

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
                finalweight  = grossweight_textview_widget.getText().toString();
                        finalweight = finalweight.replaceAll("[^\\d.]", "");

                if (finalweight.matches("[0-9]+") && (finalweight.length() > 0)) {
                    weightindouble = Integer.parseInt(finalweight);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (pricetypeforPos.equals(Constants.TMCPRICEPERKG)) {

                    if (weightindouble != 0) {
                        appPricedouble_priceperKg = (sellingPrice_withoutDiscount * weightindouble) / 1000;

                        // appPricedouble_priceperKg = sellingPrice_withoutDiscount;

                    } else {
                        Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "weight Can't be Zero", Toast.LENGTH_SHORT).show();
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
                appPriceint = (int) Math.ceil(appPricedouble_priceperKg);

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


            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                app_price_textview_widget.setText(String.valueOf(appPriceint));


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{


            Toast.makeText(ChangeMenuItemWeightAndPriceSecondScreen.this, "App Selling Price Can't be Zero", Toast.LENGTH_SHORT).show();

        }

        showProgressBar(false);


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
        grossweight_textview_widget.setText(weight);

        return weight;
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
    private void getMenuItemCutDetailsFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuItemCutDetails", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuItemCutDetailsString", "");
        MenuItemCutdetailsString = sharedPreferencesMenuitem.getString("MenuItemCutDetailsString", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(), "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItemCutDetails>>() {
            }.getType();
            MenuItemCutDetailsArrayDefaultList = gson.fromJson(json, type);
        }
    }

    private void getMenuItemWeightDetailsFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuItemWeightDetails", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuItemWeightDetailsString", "");
        MenuItemWeightdetailsString = sharedPreferencesMenuitem.getString("MenuItemWeightDetailsString", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(), "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItemWeightDetails>>() {
            }.getType();
            MenuItemWeightDetailsArrayDefaultList = gson.fromJson(json, type);
        }
    }


    private void saveMenuIteminSharedPreference(List<Modal_MenuItem_Settings> menuItem) {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList", json);
        editor.apply();
        FormatAndDisplaytheDataa();
      //  getMenuItembasedOnkey(menuitemKey);
        showProgressBar(false);
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


}