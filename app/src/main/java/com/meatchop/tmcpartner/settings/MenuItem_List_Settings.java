package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;
import com.meatchop.tmcpartner.sqlite.TMCSubCtgyItemSQL_DB_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MenuItem_List_Settings extends AppCompatActivity {
    LinearLayout loadingPanel,loadingpanelmask;
    Spinner subCtgyItem_spinner;
    ArrayAdapter adapter_subCtgy_spinner;
    String vendorkey,deliverySlotKey ;
    ListView MenuItemsListView;
   public static List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    TextView headingTextview;
    String IntentFrom ;
    public static List<Modal_MenuItem_Settings> displaying_menuItems;
    //public static List<Modal_MenuItem_Settings> completemenuItem;
    public static List<Modal_SubCtgyList> subCtgyName_arrayList;
    JSONArray result;
    TextView title,subtitle_Textview;
    ImageView searchButton,searchBarCloseButton;
    EditText searchbarEdit;
    String SubCtgyKey ="";
    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
    boolean localDBcheck = false , isinventorycheck =false;
    TMCSubCtgyItemSQL_DB_Manager tmcSubCtgyItemSQL_db_manager ;
    boolean isSearchButtonClicked = false ;
    public static List<Modal_MenuItem_Settings> filteredsubctgywise_menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_item_list_settings_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();


        IntentFrom = getIntent().getExtras().getString("ClickedOn","");
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        subCtgyItem_spinner = findViewById(R.id.subCtgyItem);
        MenuItemsListView = findViewById(R.id.MenuItemsListView);
        headingTextview = findViewById(R.id.headingTextview);
        searchButton =findViewById(R.id.search_button);
        subtitle_Textview =findViewById(R.id.subtitle_Textview);
        searchBarCloseButton =findViewById(R.id.searchBarCloseButton);
        searchbarEdit =findViewById(R.id.searchbarEdit);



        Adjusting_Widgets_Visibility(true);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorkey = (shared.getString("VendorKey", ""));
        localDBcheck = (shared.getBoolean("localdbcheck", false));
        isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));

         SubCtgyKey = "";
        displaying_menuItems = new ArrayList<>();
        subCtgyName_arrayList = new ArrayList<>();
        filteredsubctgywise_menuItems = new ArrayList<>();




        if(localDBcheck){
            getDataFromSQL();
        }
        else{
            getMenuItemArrayFromSharedPreferences();
            getMenuCategoryList();

        }


        if(IntentFrom.equals("ChangeMenuItemPrice")) {
            headingTextview.setText("Select Menu Item to Change Price ");
        }
        if(IntentFrom.equals("MenuAvailabilityTransactionDetails")) {
            headingTextview.setText("Select Item to get its On/Off Transaction");
        }
        if(IntentFrom.equals("ChangeMenuItemPriceAndWeight")) {
            Adjusting_Widgets_Visibility(true);
            //getItemCutDetails();

            headingTextview.setText("Select Menu to Change Price & Weight");
        }

        searchBarCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(searchbarEdit);
                closeSearchBarEditText();
                searchbarEdit.setText("");
                isSearchButtonClicked =false;
                //   callAdapter(displaying_menuItems);
                getMenuItemsbasedOnSubCtgy(SubCtgyKey);

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = searchbarEdit.getText().toString().length();
                isSearchButtonClicked =true;
                showKeyboard(searchbarEdit);
                showSearchBarEditText();
            }
        });

        searchbarEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filteredsubctgywise_menuItems.clear();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filteredsubctgywise_menuItems.clear();
                isSearchButtonClicked =true;
                String itemnameFromEditText = (editable.toString());
                if(!itemnameFromEditText.equals("")) {
                    String relationtypeFromInventoryDetails = "";

                    for (int i = 0; i < displaying_menuItems.size(); i++) {
                        try {

                            final Modal_MenuItem_Settings modal_menuItem_sedisplaying_menuItemsttings = new Modal_MenuItem_Settings();
                            final Modal_MenuItem_Settings modal_menuItemSettings = displaying_menuItems.get(i);
                            String inventoryDetails = modal_menuItemSettings.getInventorydetails().toString();

                            String itemName = modal_menuItemSettings.getItemname();
                            if (itemName.toUpperCase().contains(itemnameFromEditText.toUpperCase())) {
                                if(!inventoryDetails.equals("") &&(! inventoryDetails.toString().equals("nil") )  ) {

                                    try {
                                        modal_menuItem_sedisplaying_menuItemsttings.relationtypeFromInventoryDetails = modal_menuItemSettings.getRelationtypeFromInventoryDetails();
                                        relationtypeFromInventoryDetails = modal_menuItemSettings.getRelationtypeFromInventoryDetails();
                                    } catch (Exception e) {
                                        relationtypeFromInventoryDetails = "CHILD";
                                        modal_menuItem_sedisplaying_menuItemsttings.relationtypeFromInventoryDetails = "CHILD";
                                        e.printStackTrace();
                                    }
                                    if(!relationtypeFromInventoryDetails.toUpperCase().equals("PARENT")) {



                                        modal_menuItem_sedisplaying_menuItemsttings.key = String.valueOf(modal_menuItemSettings.getKey());
                                        modal_menuItem_sedisplaying_menuItemsttings.key_AvlDetails = String.valueOf(modal_menuItemSettings.getKey_AvlDetails());
                                        modal_menuItem_sedisplaying_menuItemsttings.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                                        modal_menuItem_sedisplaying_menuItemsttings.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                                        modal_menuItem_sedisplaying_menuItemsttings.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                                        modal_menuItem_sedisplaying_menuItemsttings.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                                        modal_menuItem_sedisplaying_menuItemsttings.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                                        modal_menuItem_sedisplaying_menuItemsttings.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                                        modal_menuItem_sedisplaying_menuItemsttings.itemavailability_AvlDetails = String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails());
                                        modal_menuItem_sedisplaying_menuItemsttings.allownegativestock = String.valueOf(modal_menuItemSettings.getAllownegativestock());
                                        modal_menuItem_sedisplaying_menuItemsttings.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                                        modal_menuItem_sedisplaying_menuItemsttings.inventorydetails = String.valueOf(modal_menuItemSettings.getInventorydetails());
                                        modal_menuItem_sedisplaying_menuItemsttings.stockbalance_AvlDetails = String.valueOf(modal_menuItemSettings.getStockbalance_AvlDetails());

                                        try {
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = String.valueOf(modal_menuItemSettings.getMarinadeKey());
                                            modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = true;
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = String.valueOf(modal_menuItemSettings.getMarinadeItemAvailability());
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = String.valueOf(modal_menuItemSettings.getMarinadeBarcode());
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = String.valueOf(modal_menuItemSettings.getMarinadeItemUniqueCode());
                                        } catch (Exception e) {
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = "";
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = "";
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = "";
                                            modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = "";
                                            modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = false;
                                        }
                                        if (String.valueOf(modal_menuItemSettings.getItemavailability()).equals("TRUE")) {
                                       //     total_no_item_availability = total_no_item_availability + 1;
                                        }

                                        filteredsubctgywise_menuItems.add(modal_menuItem_sedisplaying_menuItemsttings);

                                    }

                                }
                                else{


                                    modal_menuItem_sedisplaying_menuItemsttings.key = String.valueOf(modal_menuItemSettings.getKey());
                                    modal_menuItem_sedisplaying_menuItemsttings.key_AvlDetails = String.valueOf(modal_menuItemSettings.getKey_AvlDetails());
                                    modal_menuItem_sedisplaying_menuItemsttings.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                                    modal_menuItem_sedisplaying_menuItemsttings.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                                    modal_menuItem_sedisplaying_menuItemsttings.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                                    modal_menuItem_sedisplaying_menuItemsttings.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                                    modal_menuItem_sedisplaying_menuItemsttings.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                                    modal_menuItem_sedisplaying_menuItemsttings.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                                    modal_menuItem_sedisplaying_menuItemsttings.itemavailability_AvlDetails = String.valueOf(modal_menuItemSettings.getItemavailability_AvlDetails());
                                    modal_menuItem_sedisplaying_menuItemsttings.allownegativestock = String.valueOf(modal_menuItemSettings.getAllownegativestock());
                                    modal_menuItem_sedisplaying_menuItemsttings.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                                    modal_menuItem_sedisplaying_menuItemsttings.inventorydetails = String.valueOf(modal_menuItemSettings.getInventorydetails());
                                    modal_menuItem_sedisplaying_menuItemsttings.stockbalance_AvlDetails = String.valueOf(modal_menuItemSettings.getStockbalance_AvlDetails());

                                    try {
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = String.valueOf(modal_menuItemSettings.getMarinadeKey());
                                        modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = true;
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = String.valueOf(modal_menuItemSettings.getMarinadeItemAvailability());
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = String.valueOf(modal_menuItemSettings.getMarinadeBarcode());
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = String.valueOf(modal_menuItemSettings.getMarinadeItemUniqueCode());
                                    } catch (Exception e) {
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeKey = "";
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeItemAvailability = "";
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeBarcode = "";
                                        modal_menuItem_sedisplaying_menuItemsttings.marinadeItemUniqueCode = "";
                                        modal_menuItem_sedisplaying_menuItemsttings.isMarinadeItem = false;
                                    }
                                    if (String.valueOf(modal_menuItemSettings.getItemavailability()).equals("TRUE")) {
                                    //    total_no_item_availability = total_no_item_availability + 1;
                                    }

                                    filteredsubctgywise_menuItems.add(modal_menuItem_sedisplaying_menuItemsttings);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    try {
                        callAdapter(filteredsubctgywise_menuItems);
                    } catch (Exception E) {
                        E.printStackTrace();
                    }


                }
                else{
                    callAdapter(filteredsubctgywise_menuItems);


                }

            }
        });




        subCtgyItem_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d(Constants.TAG, "displaying_menuItems: " + displaying_menuItems.size());

                 SubCtgyKey= getSubCtgyData(i,"key");
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMenuItemsbasedOnSubCtgy(SubCtgyKey);

                    }
                }, 500);            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });








    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @SuppressLint("Range")
    private void getDataFromSQL() {
        Adjusting_Widgets_Visibility(true);
        if(tmcMenuItemSQL_db_manager== null) {
            tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(getApplicationContext());
            try {
                tmcMenuItemSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if(tmcSubCtgyItemSQL_db_manager == null) {

            tmcSubCtgyItemSQL_db_manager = new TMCSubCtgyItemSQL_DB_Manager(this);
            try {
                tmcSubCtgyItemSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        try{
            Cursor cursor = tmcMenuItemSQL_db_manager.Fetch();
            MenuItem.clear();
            try {
                // if (cursor.moveToFirst()) {

                Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {
                            Modal_MenuItem_Settings modal_menuItem_settings = new Modal_MenuItem_Settings();
                            modal_menuItem_settings.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_menuItem_settings.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_menuItem_settings.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem_settings.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.localDB_id)));
                            modal_menuItem_settings.setApplieddiscountpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.applieddiscountpercentage)));
                            modal_menuItem_settings.setBarcode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode)));
                            modal_menuItem_settings.setCheckoutimageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.checkoutimageurl)));
                            modal_menuItem_settings.setDisplayno(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.displayno)));
                            modal_menuItem_settings.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_menuItem_settings.setGstpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.gstpercentage)));
                            modal_menuItem_settings.setItemavailability(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability)));
                            modal_menuItem_settings.setItemuniquecode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemuniquecode)));
                            modal_menuItem_settings.setNetweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.netweight)));
                            modal_menuItem_settings.setPortionsize(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.portionsize)));
                            modal_menuItem_settings.setPricetypeforpos(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.pricetypeforpos)));
                            modal_menuItem_settings.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcctgykey)));
                            modal_menuItem_settings.setTmcpriceperkg(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg)));
                            modal_menuItem_settings.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_menuItem_settings.setTmcsubctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcsubctgykey)));
                            modal_menuItem_settings.setVendorkey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey)));
                            modal_menuItem_settings.setVendorname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorname)));
                            modal_menuItem_settings.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem_settings.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_menuItem_settings.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_menuItem_settings.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem_settings.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_menuItem_settings.setSwiggyprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.swiggyprice)));
                            modal_menuItem_settings.setDunzoprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.dunzoprice)));
                            modal_menuItem_settings.setBigbasketprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.bigbasketprice)));
                            modal_menuItem_settings.setWholesaleprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.wholesaleprice)));
                            modal_menuItem_settings.setAppmarkuppercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)));
                            modal_menuItem_settings.setTmcpriceperkgWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkgWithMarkupValue)));
                            modal_menuItem_settings.setTmcpriceWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceWithMarkupValue)));
                            modal_menuItem_settings.setKey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_menuItem_settings.setInventorydetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.inventoryDetails)));
                            modal_menuItem_settings.setGrossweightingrams(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweightingrams)));


                            if (!isinventorycheck) {

                                String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                        receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                                modal_menuItem_settings.setBarcode_AvlDetails(barcode_AvlDetails);
                                modal_menuItem_settings.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                modal_menuItem_settings.setKey_AvlDetails(key_AvlDetails);
                                modal_menuItem_settings.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                modal_menuItem_settings.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                modal_menuItem_settings.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                modal_menuItem_settings.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                modal_menuItem_settings.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                modal_menuItem_settings.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                modal_menuItem_settings.setAllownegativestock(allownegativestock_AvlDetails);





                            }
                            else{

                               modal_menuItem_settings.setBarcode_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode_AvlDetails)));
                               modal_menuItem_settings.setItemavailability_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability_AvlDetails)));
                               modal_menuItem_settings.setKey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.key_AvlDetails)));
                               modal_menuItem_settings.setLastupdatedtime_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.lastupdatedtime_AvlDetails)));
                               modal_menuItem_settings.setMenuitemkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId_AvlDetails)));
                               modal_menuItem_settings.setReceivedstock_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.receivedStock_AvlDetails)));
                               modal_menuItem_settings.setStockbalance_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockBalance_AvlDetails)));
                               modal_menuItem_settings.setStockincomingkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockIncomingKey_AvlDetails)));
                               modal_menuItem_settings.setVendorkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey_AvlDetails)));
                               modal_menuItem_settings.setAllownegativestock(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.allowNegativeStock_AvlDetails)));


                            }

                            MenuItem.add(modal_menuItem_settings);
                        }
                        while (cursor.moveToNext());

                    }



                }
                else{
                    Toast.makeText(MenuItem_List_Settings.this, "There is no menuItem Please Refresh the App", Toast.LENGTH_SHORT).show();
                    Adjusting_Widgets_Visibility(false);
                }




                //  }
            }
            catch (Exception e){
                e.printStackTrace();
                Adjusting_Widgets_Visibility(false);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);
        }


        try{
            Cursor cursor = tmcSubCtgyItemSQL_db_manager.Fetch();
            try{
                if(cursor.getCount()>0) {

                    if (cursor.moveToFirst()) {
                        do {
                            Modal_SubCtgyList modal_subCtgyList = new Modal_SubCtgyList();
                            modal_subCtgyList.setKey(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.key)));
                            modal_subCtgyList.setTmcctgyname(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.tmcctgyname)));
                            modal_subCtgyList.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.tmcctgykey)));
                            modal_subCtgyList.setSubCtgyName(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.subctgyname)));
                            modal_subCtgyList.setDisplayNo(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.displayno)));
                            modal_subCtgyList.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.localDB_id)));

                            subCtgyName_arrayList.add(modal_subCtgyList);
                        }
                        while (cursor.moveToNext());

                        try{
                            Collections.sort(subCtgyName_arrayList, new Comparator<Modal_SubCtgyList>() {
                                public int compare(Modal_SubCtgyList result1, Modal_SubCtgyList result2) {
                                    return result1.getDisplayNo().compareTo(result2.getDisplayNo());
                                }
                            });
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }


                        adapter_subCtgy_spinner = new ArrayAdapter<Modal_SubCtgyList>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subCtgyName_arrayList);
                        subCtgyItem_spinner.setAdapter(adapter_subCtgy_spinner);


                    }
                }
                else{
                    getMenuCategoryList();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

/*
    private void getItemCutDetails() {

        showProgressBar(true);
        TomorrowsPreOrdersSlotList.clear();
        TodaysPreOrdersSlotList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlots+"?storeid="+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    if (content != null) {
                        for (int i = 0; i < content.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);
                                String slotName = String.valueOf(json.get("slotname"));
                                String slotDateType = String.valueOf(json.get("slotdatetype"));
                                slotDateType =slotDateType.toUpperCase();
                                slotName = slotName.toUpperCase();
                                if(slotName.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)){
                                    deliverySlotKey= String.valueOf(json.get("key"));
                                    String status =String.valueOf(json.get("status"));
                                    status = status.toUpperCase();

                                    if(status.equals("ACTIVE")){
                                        isActiveinDeliverySlots=true;

                                    }
                                    if(status.equals("INACTIVE")){
                                        isActiveinDeliverySlots=false;

                                    }


                                    if(isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                                        expressDeliveryAvailabiltySwitch.setChecked(true);
                                    }
                                    else if(!isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slot details",Toast.LENGTH_LONG).show();
                                    }
                                    else if(!isActiveinDeliverySlots&&isActiveinDeliverySlotDetails){
                                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slots ",Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        expressDeliveryAvailabiltySwitch.setChecked(false);

                                    }










                                }
                                try {
                                    if (slotName.equals(Constants.PREORDER_SLOTNAME) && (slotDateType.equals(Constants.TODAYPREORDER_SLOTNAME))) {
                                        Modal_DeliverySlots modal_deliverySlots = new Modal_DeliverySlots();
                                        modal_deliverySlots.key = String.valueOf(json.get("key"));
                                        modal_deliverySlots.status = String.valueOf(json.get("status"));
                                        modal_deliverySlots.slotdatetype = String.valueOf(json.get("slotdatetype"));
                                        modal_deliverySlots.slotstarttime = String.valueOf(json.get("slotstarttime"));
                                        modal_deliverySlots.slotendtime = String.valueOf(json.get("slotendtime"));
                                        try {
                                            TodaysPreOrdersSlotList.add(modal_deliverySlots);
                                            Collections.sort(TodaysPreOrdersSlotList, new Comparator<Modal_DeliverySlots>() {
                                                public int compare(final Modal_DeliverySlots object1, final Modal_DeliverySlots object2) {
                                                    return object2.getSlotstarttime().compareTo(object1.getSlotstarttime());
                                                }
                                            });
                                        } catch (Exception e) {
                                            TodaysPreOrdersSlotList.add(modal_deliverySlots);

                                            e.printStackTrace();
                                        }


                                        Adapter_TodaysDeliverySlots adapter_todaysDeliverySlots = new Adapter_TodaysDeliverySlots(ChangeDelivery_Slot_Availability_Status.this, TodaysPreOrdersSlotList, ChangeDelivery_Slot_Availability_Status.this);
                                        todaysDeliverySlotListview.setAdapter(adapter_todaysDeliverySlots);
                                        Helper.getListViewSize(todaysDeliverySlotListview, screenInches);


                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    showProgressBar(false);
                                }
                                try {


                                    if (slotName.equals(Constants.PREORDER_SLOTNAME) && (slotDateType.equals(Constants.TOMORROWPREORDER_SLOTNAME))) {
                                        Modal_DeliverySlots modal_deliverySlots = new Modal_DeliverySlots();
                                        modal_deliverySlots.key = String.valueOf(json.get("key"));
                                        modal_deliverySlots.status = String.valueOf(json.get("status"));
                                        modal_deliverySlots.slotdatetype = String.valueOf(json.get("slotdatetype"));
                                        modal_deliverySlots.slotstarttime = String.valueOf(json.get("slotstarttime"));
                                        modal_deliverySlots.slotendtime = String.valueOf(json.get("slotendtime"));

                                        TomorrowsPreOrdersSlotList.add(modal_deliverySlots);

                                        try {
                                            Collections.sort(TomorrowsPreOrdersSlotList, new Comparator<Modal_DeliverySlots>() {
                                                public int compare(final Modal_DeliverySlots object1, final Modal_DeliverySlots object2) {
                                                    return object2.getSlotstarttime().compareTo(object1.getSlotstarttime());
                                                }
                                            });
                                        } catch (Exception e) {
                                            TomorrowsPreOrdersSlotList.add(modal_deliverySlots);

                                            e.printStackTrace();
                                        }
                                        Adapter_TomorrowsDeliverySlots adapter_tomorrowsDeliverySlots = new Adapter_TomorrowsDeliverySlots(ChangeDelivery_Slot_Availability_Status.this, TomorrowsPreOrdersSlotList, ChangeDelivery_Slot_Availability_Status.this);
                                        tomorrowsDeliverySlotListview.setAdapter(adapter_tomorrowsDeliverySlots);
                                        Helper.getListViewSize(tomorrowsDeliverySlotListview, screenInches);
                                        showProgressBar(false);

                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    showProgressBar(false);
                                }



                            } catch (JSONException e) {
                                TomorrowsPreOrdersSlotList.clear();
                                TodaysPreOrdersSlotList.clear();
                                showProgressBar(false);

                                e.printStackTrace();
                            }
                        }



                    }
                } catch (JSONException e) {
                    if(isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                        expressDeliveryAvailabiltySwitch.setChecked(true);
                    }
                    else if(!isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slot details",Toast.LENGTH_LONG).show();
                    }
                    else if(!isActiveinDeliverySlots&&isActiveinDeliverySlotDetails){
                        Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slots ",Toast.LENGTH_LONG).show();
                    }
                    else{
                        expressDeliveryAvailabiltySwitch.setChecked(false);

                    }


                    showProgressBar(false);



                    TomorrowsPreOrdersSlotList.clear();
                    TodaysPreOrdersSlotList.clear();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                TomorrowsPreOrdersSlotList.clear();
                TodaysPreOrdersSlotList.clear();
                error.printStackTrace();
                showProgressBar(false);

                if(isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                    expressDeliveryAvailabiltySwitch.setChecked(true);
                }
                else if(!isActiveinDeliverySlotDetails&&isActiveinDeliverySlots){
                    Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slot details",Toast.LENGTH_LONG).show();
                }
                else if(!isActiveinDeliverySlots&&isActiveinDeliverySlotDetails){
                    Toast.makeText(ChangeDelivery_Slot_Availability_Status.this,"INACTIVE in Delivery slots ",Toast.LENGTH_LONG).show();
                }
                else{
                    expressDeliveryAvailabiltySwitch.setChecked(false);

                }





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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(ChangeDelivery_Slot_Availability_Status.this).add(jsonObjectRequest);




    }


 */


    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem_Settings>>() {
            }.getType();
            MenuItem  = gson.fromJson(json, type);
        }

    }






    private void getMenuCategoryList() {



        String Api_to_Call = "";
        if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)) {
            Api_to_Call = Constants.api_getListofTMCTileForVendorkey+vendorkey;
        }
        else{
            Api_to_Call = Constants.api_getListofSubCtgy;

        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_to_Call,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    //Log.d(Constants.TAG, "response from subCtgy: " + response.get("content"));
                    result  = response.getJSONArray("content");

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);
                                String ctgyname = "" , ctgykey ="",key ="",subctgyname ="",displayNo="";

                                if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)){
                                    ctgyname =  String.valueOf(json.get("tmcctgyname"));
                                    ctgykey = String.valueOf(json.get("tmcctgykey"));
                                    key = String.valueOf(json.get("tmcsubctgykey"));
                                    subctgyname = String.valueOf(json.get("tmcsubctgyname"));
                                    displayNo = String.valueOf(json.get("displayno"));
                                }
                                else{
                                    ctgyname =  String.valueOf(json.get("tmcctgyname"));
                                    ctgykey = String.valueOf(json.get("tmcctgykey"));
                                    key = String.valueOf(json.get("key"));
                                    subctgyname = String.valueOf(json.get("subctgyname"));
                                    displayNo = String.valueOf(json.get("displayno"));
                                }
                                //Log.d(Constants.TAG, "subctgyname from subCtgy: " + subctgyname);
                                Modal_SubCtgyList  modal_subCtgyList = new Modal_SubCtgyList();
                                modal_subCtgyList.setKey(key);
                                modal_subCtgyList.setSubCtgyName(subctgyname);
                                modal_subCtgyList.setTmcctgyname(ctgyname);
                                modal_subCtgyList.setTmcctgykey(ctgykey);
                                modal_subCtgyList.setDisplayNo(displayNo);
                                //Log.d(Constants.TAG, "subctgyname from subCtgy: " + subctgyname);

                                if(localDBcheck) {

                                    try {


                                        if (tmcSubCtgyItemSQL_db_manager == null) {

                                            tmcSubCtgyItemSQL_db_manager = new TMCSubCtgyItemSQL_DB_Manager(MenuItem_List_Settings.this);
                                            try {
                                                tmcSubCtgyItemSQL_db_manager.open();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }


                                        try {
                                            if (tmcSubCtgyItemSQL_db_manager.deleteTable(true) >= 0) {
                                                tmcSubCtgyItemSQL_db_manager.open();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        long id = tmcSubCtgyItemSQL_db_manager.insert(modal_subCtgyList);


                                        modal_subCtgyList.setLocalDB_id(String.valueOf(id));


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        try {
                                            if (jArray.length() - i == 1) {
                                                tmcSubCtgyItemSQL_db_manager = null;
                                                tmcSubCtgyItemSQL_db_manager.close();

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                                if (!subCtgyName_arrayList.contains(modal_subCtgyList)) {
                                    subCtgyName_arrayList.add(modal_subCtgyList);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try{
                    Collections.sort(subCtgyName_arrayList, new Comparator<Modal_SubCtgyList>() {
                        public int compare(Modal_SubCtgyList result1, Modal_SubCtgyList result2) {
                            return result1.getDisplayNo().compareTo(result2.getDisplayNo());
                        }
                    });
                }
                catch (Exception e ){
                    e.printStackTrace();
                }

                adapter_subCtgy_spinner = new ArrayAdapter<Modal_SubCtgyList>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, subCtgyName_arrayList);
                subCtgyItem_spinner.setAdapter(adapter_subCtgy_spinner);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
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
                params.put("modulename", "SubCategory");

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(MenuItem_List_Settings.this).add(jsonObjectRequest);

    }

    private String getSubCtgyData(int position, String fieldName){
        String data="";
      //  if(localDBcheck){

            try {
                Modal_SubCtgyList modal_subCtgyList =  subCtgyName_arrayList.get(position);
                if(fieldName.equals("subctgyname")){
                    data = modal_subCtgyList.getSubCtgyName();
                }
                else if(fieldName.equals("key")){
                    data = modal_subCtgyList.getKey();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
   /*     }
        else{
            try {
                JSONObject json = result.getJSONObject(position);
                data = json.getString(fieldName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    */

        return data;
    }


    private void getMenuItemsbasedOnSubCtgy(String subCtgykey) {

        displaying_menuItems.clear();
        for(int i=0;i<MenuItem.size();i++){
            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(i);
            String menuSubCtgy = modal_menuItemSettings.getTmcsubctgykey();
            if(menuSubCtgy.equals(subCtgykey)) {
                String MenuItemName;
                Modal_MenuItem_Settings selected_CtgyItems = new Modal_MenuItem_Settings();
                selected_CtgyItems.key=String.valueOf(modal_menuItemSettings.getKey());
                selected_CtgyItems.localDB_id =String.valueOf(modal_menuItemSettings.getLocalDB_id());

                selected_CtgyItems.itemname = String.valueOf(modal_menuItemSettings.getItemname());
                selected_CtgyItems.menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                selected_CtgyItems.tmcsubctgykey = String.valueOf(modal_menuItemSettings.getTmcsubctgykey());
                selected_CtgyItems.itemavailability = String.valueOf(modal_menuItemSettings.getItemavailability());
                selected_CtgyItems.barcode = String.valueOf(modal_menuItemSettings.getBarcode());
                selected_CtgyItems.itemuniquecode = String.valueOf(modal_menuItemSettings.getItemuniquecode());
                selected_CtgyItems.showinmenuboard = String.valueOf(modal_menuItemSettings.getShowinmenuboard());
                selected_CtgyItems.displayno = String.valueOf(modal_menuItemSettings.getDisplayno());
                selected_CtgyItems.inventorydetails = String.valueOf(modal_menuItemSettings.getInventorydetails()
                );
                displaying_menuItems.add(selected_CtgyItems);
                //Log.d(Constants.TAG, "displaying_menuItems: " + String.valueOf(modal_menuItemSettings.getItemname()));


                try{






                    Collections.sort(displaying_menuItems, new Comparator<Modal_MenuItem_Settings>() {
                        public int compare(final Modal_MenuItem_Settings object1, final Modal_MenuItem_Settings object2) {
                            String i2 = String.valueOf(object2.getItemname());
                            String i1 = String.valueOf(object1.getItemname());
                            return i1.compareTo(i2);
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                }




            }
            if(MenuItem.size()-i == 1){
                callAdapter(displaying_menuItems);
            }


        }





    }

    private void callAdapter(List<Modal_MenuItem_Settings> menuItems) {
        if(menuItems.size()>0) {
            Adapter_ChangeMenuItem_Price adapter_changeMenuItem_price = new Adapter_ChangeMenuItem_Price(MenuItem_List_Settings.this, menuItems, MenuItem_List_Settings.this, IntentFrom);

            MenuItemsListView.setAdapter(adapter_changeMenuItem_price);
            Adjusting_Widgets_Visibility(false);
        }
        else{
            Adjusting_Widgets_Visibility(false);

        }
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


    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        subtitle_Textview.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        searchBarCloseButton.setVisibility(View.GONE);
        searchbarEdit.setVisibility(View.GONE);
    }

    private void showSearchBarEditText() {
        subtitle_Textview.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        searchBarCloseButton.setVisibility(View.VISIBLE);
        searchbarEdit.setVisibility(View.VISIBLE);

    }
    private void showKeyboard(final EditText editText) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
    }



}