package com.meatchop.tmcpartner.add_updateinventorydetailentries;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;
import static com.meatchop.tmcpartner.Constants.api_Update_MenuItemStockAvlDetails;


public class Add_UpdateInventoryDetailsEntries_AsyncTask extends AsyncTask<String, String, List<Modal_ManageOrders_Pojo_Class>> {
    Context mContext;
    Add_UpdateInventoryDetailsEntries_Interface mResultCallback_add_updateInventoryEntriesInterface =null;
    String vendorkey ="";
    String orderid ="" ;
    String customerMobileNo ="" ;
    String currenttime ="" ;
    int updatedStockBalanceCount =0;
    List<Modal_ManageOrders_Pojo_Class> orderdItems_desp = new ArrayList<>();
    List<Modal_MenuItem> MenuItemArray = new ArrayList<>();
    List<String> menuitemkey_of_alreadyCalculatedStockBalanceWeight = new ArrayList<>();
    boolean calledFromNewOrderScreen;
    List<Modal_ManageOrders_Pojo_Class> final_orderdItems_desp = new ArrayList<>();

    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(7);
    Boolean localDBcheck;
    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;



    public Add_UpdateInventoryDetailsEntries_AsyncTask(Context mContext, Add_UpdateInventoryDetailsEntries_Interface mResultCallback_add_updateInventoryEntriesInterface, String vendorkey, String orderid, String customerMobileNo, String currenttime, List<Modal_ManageOrders_Pojo_Class> orderdItems_desp, List<Modal_MenuItem> menuItem) {
        this.mContext = mContext;
        this.mResultCallback_add_updateInventoryEntriesInterface = mResultCallback_add_updateInventoryEntriesInterface;
        this.vendorkey = vendorkey;
        this.orderid = orderid;
        this.customerMobileNo = customerMobileNo;
        this.currenttime = currenttime;
        this.orderdItems_desp = orderdItems_desp;
        this.calledFromNewOrderScreen = false;

        SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        localDBcheck = (shared.getBoolean("localdbcheck", false));
        if(localDBcheck) {
            getDataFromSQL();
        }
        else {
            getMenuItemArrayFromSharedPreferences();
        }
    }


    public Add_UpdateInventoryDetailsEntries_AsyncTask(Context mContext, Add_UpdateInventoryDetailsEntries_Interface mResultCallback_add_updateInventoryEntriesInterface, String vendorkey, String orderid, String customerMobileNo, String currenttime, List<Modal_ManageOrders_Pojo_Class> orderdItems_desp,  boolean calledFromNewOrderScreen) {
        this.mContext = mContext;
        this.mResultCallback_add_updateInventoryEntriesInterface = mResultCallback_add_updateInventoryEntriesInterface;
        this.vendorkey = vendorkey;
        this.orderid = orderid;
        this.customerMobileNo = customerMobileNo;
        this.currenttime = currenttime;
        this.orderdItems_desp = orderdItems_desp;

        SharedPreferences shared = mContext.getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        localDBcheck = (shared.getBoolean("localdbcheck", false));

        if(localDBcheck) {
            getDataFromSQL();
        }
        else {
            getMenuItemArrayFromSharedPreferences();
        }
        this.calledFromNewOrderScreen = true;

    }

    @SuppressLint("Range")
    private void getDataFromSQL() {


        if(tmcMenuItemSQL_db_manager== null) {
            tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(mContext);
            try {
                tmcMenuItemSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try{
            Cursor cursor = tmcMenuItemSQL_db_manager.Fetch();
            MenuItemArray.clear();
            try {
                // if (cursor.moveToFirst()) {

                Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {
                            Modal_MenuItem modal_newOrderItems = new Modal_MenuItem();
                            modal_newOrderItems.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_newOrderItems.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_newOrderItems.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.localDB_id)));
                            modal_newOrderItems.setApplieddiscountpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.applieddiscountpercentage)));
                            modal_newOrderItems.setBarcode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode)));
                            modal_newOrderItems.setCheckoutimageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.checkoutimageurl)));
                            modal_newOrderItems.setDisplayno(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.displayno)));
                            modal_newOrderItems.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_newOrderItems.setGstpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.gstpercentage)));
                            modal_newOrderItems.setItemavailability(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability)));
                            modal_newOrderItems.setItemuniquecode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemuniquecode)));
                            modal_newOrderItems.setNetweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.netweight)));
                            modal_newOrderItems.setPortionsize(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.portionsize)));
                            modal_newOrderItems.setPricetypeforpos(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.pricetypeforpos)));
                            modal_newOrderItems.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcctgykey)));
                            modal_newOrderItems.setTmcpriceperkg(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg)));
                            modal_newOrderItems.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_newOrderItems.setTmcsubctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcsubctgykey)));
                            modal_newOrderItems.setVendorkey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey)));
                            modal_newOrderItems.setVendorname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorname)));
                            modal_newOrderItems.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_newOrderItems.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_newOrderItems.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_newOrderItems.setSwiggyprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.swiggyprice)));
                            modal_newOrderItems.setDunzoprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.dunzoprice)));
                            modal_newOrderItems.setBigbasketprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.bigbasketprice)));
                            modal_newOrderItems.setWholesaleprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.wholesaleprice)));
                            modal_newOrderItems.setAppmarkuppercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)));
                            modal_newOrderItems.setTmcpriceperkgWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkgWithMarkupValue)));
                            modal_newOrderItems.setTmcpriceWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceWithMarkupValue)));
                            modal_newOrderItems.setKey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setInventorydetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.inventoryDetails)));


                                modal_newOrderItems.setBarcode_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode_AvlDetails)));
                                modal_newOrderItems.setItemavailability_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability_AvlDetails)));
                                modal_newOrderItems.setKey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.key_AvlDetails)));
                                modal_newOrderItems.setLastupdatedtime_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.lastupdatedtime_AvlDetails)));
                                modal_newOrderItems.setMenuitemkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId_AvlDetails)));
                                modal_newOrderItems.setReceivedstock_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.receivedStock_AvlDetails)));
                                modal_newOrderItems.setStockbalance_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockBalance_AvlDetails)));
                                modal_newOrderItems.setStockincomingkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockIncomingKey_AvlDetails)));
                                modal_newOrderItems.setVendorkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey_AvlDetails)));
                                modal_newOrderItems.setAllownegativestock(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.allowNegativeStock_AvlDetails)));




                            MenuItemArray.add(modal_newOrderItems);
                        }
                        while (cursor.moveToNext());


                    }



                }
                else{
                    Toast.makeText(mContext, "There is no menuItem Please Refresh the App", Toast.LENGTH_SHORT).show();

                }




                //  }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (tmcMenuItemSQL_db_manager != null) {
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }




    }

    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = mContext.getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(mContext.getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem>>() {
            }.getType();
            MenuItemArray  = gson.fromJson(json, type);
        }

    }


    @Override
    protected List<Modal_ManageOrders_Pojo_Class> doInBackground(String... strings) {
        try{


            Date c = Calendar.getInstance().getTime();

            if(!calledFromNewOrderScreen) {
                CallStockOutgoing callStockOutgoing = new CallStockOutgoing(orderid);
                executor.execute(callStockOutgoing);
            }
            try{
                for(int i =0; i<orderdItems_desp.size();i ++) {
                    JSONArray inventoryDetailsArray = new JSONArray();
                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = orderdItems_desp.get(i);
                    try {
                        inventoryDetailsArray = modal_manageOrders_pojo_class.getInventorydetails();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                  /*  String menuItemKey = "";
                    if (Objects.requireNonNull(modal_manageOrders_pojo_class).getMenuItemKey() != null) {
                        menuItemKey = modal_manageOrders_pojo_class.getMenuItemKey();
                    } else {
                        menuItemKey = "";
                    }

                   */

                   /* if (!menuitemkey_of_alreadyCalculatedStockBalanceWeight.contains(menuItemKey)){
                        menuitemkey_of_alreadyCalculatedStockBalanceWeight.add(menuItemKey);


                    try {
                        inventoryDetailsArray = modal_manageOrders_pojo_class.getInventorydetails();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }



                    String stockIncomingKey_AvlDetails = "";
                    if (Objects.requireNonNull(modal_manageOrders_pojo_class).getStockincomingkey() != null) {
                        stockIncomingKey_AvlDetails = modal_manageOrders_pojo_class.getStockincomingkey();
                    } else {
                        stockIncomingKey_AvlDetails = "";
                    }

                    String key_AvlDetails = "";
                    if (Objects.requireNonNull(modal_manageOrders_pojo_class).getStockavldetailskey() != null) {
                        key_AvlDetails = modal_manageOrders_pojo_class.getStockavldetailskey();
                    } else {
                        key_AvlDetails = "";
                    }


                    String receivedStock_AvlDetails = "";
                    if (Objects.requireNonNull(modal_manageOrders_pojo_class).getReceivedstock() != null) {
                        receivedStock_AvlDetails = modal_manageOrders_pojo_class.getReceivedstock();
                    } else {
                        receivedStock_AvlDetails = "";
                    }

                    String grossweight = "";
                    if (modal_manageOrders_pojo_class.getGrossweight() != null) {
                        grossweight = modal_manageOrders_pojo_class.getGrossweight();

                    }

                    String grossWeightingrams = "";
                    try {
                        if (!grossweight.equals("")) {
                            grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");

                        } else {
                            grossweight = modal_manageOrders_pojo_class.getGrossweight();
                            try {
                                grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");
                            } catch (Exception ee) {
                                ee.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        grossweight = modal_manageOrders_pojo_class.getGrossweight();
                        try {
                            grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }

                    String ItemUniquecodeofItem = "";
                    if ((modal_manageOrders_pojo_class.getItemuniquecode() != null) && (!modal_manageOrders_pojo_class.getItemuniquecode().equals("null")) && (!modal_manageOrders_pojo_class.getItemuniquecode().equals(""))) {
                        ItemUniquecodeofItem = String.valueOf(modal_manageOrders_pojo_class.getItemuniquecode());
                    } else {
                        ItemUniquecodeofItem = "";
                    }
                    double grossweightingrams_double = 0;
                    try {
                        if (!grossWeightingrams.equals("")) {
                            grossweightingrams_double = Double.parseDouble(grossWeightingrams);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String quantity = "";
                    if (modal_manageOrders_pojo_class.getQuantity() != null) {
                        quantity = modal_manageOrders_pojo_class.getQuantity();
                        ;

                    } else {
                        quantity = "";
                    }


                    double quantityDouble = 0;
                    try {
                        if (quantity.equals("")) {
                            quantity = "1";
                        }
                        quantityDouble = Double.parseDouble(quantity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    String finalWeight = "";
                    if (modal_manageOrders_pojo_class.getItemFinalWeight() != null) {
                        finalWeight = modal_manageOrders_pojo_class.getItemFinalWeight();
                        ;

                    } else {
                        finalWeight = "";
                    }


                    try {
                        finalWeight = finalWeight.replaceAll("[^\\d.]", "");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    double finalWeightDouble = 0;
                    try {

                        finalWeightDouble = Double.parseDouble(finalWeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {

                        grossweightingrams_double = finalWeightDouble - grossweightingrams_double;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    double grossWeightWithQuantity_double = 0;
                    if (modal_manageOrders_pojo_class.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                        try {
                            grossWeightWithQuantity_double = quantityDouble;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (modal_manageOrders_pojo_class.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                        try {
                            grossWeightWithQuantity_double = grossweightingrams_double * quantityDouble;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    String barcode = "";
                    if (modal_manageOrders_pojo_class.getBarcode() != null) {
                        barcode = String.valueOf(modal_manageOrders_pojo_class.getBarcode());
                    } else {
                        barcode = "";
                    }


                    String priceTypeForPOS = "";
                    if (modal_manageOrders_pojo_class.getPricetypeforpos() != null) {
                        priceTypeForPOS = String.valueOf(modal_manageOrders_pojo_class.getPricetypeforpos());
                    } else {
                        priceTypeForPOS = "";
                    }


                    String tmcCtgy = "";
                    if (modal_manageOrders_pojo_class.getTmcctgykey() != null) {
                        tmcCtgy = String.valueOf(modal_manageOrders_pojo_class.getTmcctgykey());
                    } else {
                        tmcCtgy = "";
                    }


                    String tmcSubCtgyKey = "";
                    if (modal_manageOrders_pojo_class.getTmcSubCtgyKey() != null) {
                        tmcSubCtgyKey = String.valueOf(modal_manageOrders_pojo_class.getTmcSubCtgyKey());
                    } else {
                        tmcSubCtgyKey = "";
                    }


                    String itemName =
                            String.valueOf(Objects.requireNonNull(modal_manageOrders_pojo_class).getItemName());

                    if (itemName.contains("Grill House")) {
                        itemName = itemName.replace("Grill House ", "");
                    } else if (itemName.contains("Ready to Cook")) {
                        itemName = itemName.replace("Ready to Cook", "");
                    } else {
                        itemName = itemName;
                    }
                    boolean allowNegativeStock = false;
                    if ((modal_manageOrders_pojo_class.getAllownegativestock() != null) && (!modal_manageOrders_pojo_class.getAllownegativestock().equals("null")) && (!modal_manageOrders_pojo_class.getAllownegativestock().equals(""))) {
                        allowNegativeStock = Boolean.parseBoolean(modal_manageOrders_pojo_class.getAllownegativestock().toUpperCase());
                    } else {
                        allowNegativeStock = false;
                    }


                    boolean isitemAvailable = false;
                    if ((modal_manageOrders_pojo_class.getIsitemAvailable() != null) && (!modal_manageOrders_pojo_class.getIsitemAvailable().equals("null")) && (!modal_manageOrders_pojo_class.getIsitemAvailable().equals(""))) {
                        isitemAvailable = Boolean.parseBoolean(modal_manageOrders_pojo_class.getIsitemAvailable().toUpperCase());
                    } else {
                        isitemAvailable = false;
                    }

                    if(i!=orderdItems_desp.size()) {


                        for (int iterator = i + 1; iterator < orderdItems_desp.size(); iterator++) {
                            Modal_ManageOrders_Pojo_Class modal_manageOrders_From_orderedItemDesp = orderdItems_desp.get(iterator);

                            String menuItemKey_OrderedItemDesp = "";
                            try {
                                menuItemKey_OrderedItemDesp = String.valueOf(modal_manageOrders_From_orderedItemDesp.getMenuItemKey());

                            } catch (Exception e) {
                                menuItemKey_OrderedItemDesp = "";
                                e.printStackTrace();
                            }

                            try {

                                if (menuItemKey_OrderedItemDesp.toString().equals(menuItemKey.toString())) {


                                    String grossweight_OrderedItemDesp = "";
                                    if (modal_manageOrders_From_orderedItemDesp.getGrossweight() != null) {
                                        grossweight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getGrossweight();

                                    }

                                    String grossWeightingrams_OrderedItemDesp = "";
                                    try {
                                        if (!grossweight_OrderedItemDesp.equals("")) {
                                            grossWeightingrams_OrderedItemDesp = grossweight_OrderedItemDesp.replaceAll("[^\\d.]", "");

                                        } else {
                                            grossweight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getGrossweight();
                                            try {
                                                grossWeightingrams_OrderedItemDesp = grossweight_OrderedItemDesp.replaceAll("[^\\d.]", "");
                                            } catch (Exception ee) {
                                                ee.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        grossweight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getGrossweight();
                                        try {
                                            grossWeightingrams_OrderedItemDesp = grossweight_OrderedItemDesp.replaceAll("[^\\d.]", "");
                                        } catch (Exception ee) {
                                            ee.printStackTrace();
                                        }
                                    }

                                    double grossweightingrams_OrderedItemDesp_double = 0;
                                    try {
                                        if (!grossWeightingrams_OrderedItemDesp.equals("")) {
                                            grossweightingrams_OrderedItemDesp_double = Double.parseDouble(grossWeightingrams_OrderedItemDesp);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    String quantity_OrderedItemDesp = "";
                                    if (modal_manageOrders_From_orderedItemDesp.getQuantity() != null) {
                                        quantity_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getQuantity();
                                        ;

                                    } else {
                                        quantity_OrderedItemDesp = "";
                                    }


                                    double quantity_OrderedItemDespDouble = 0;
                                    try {
                                        if (quantity_OrderedItemDesp.equals("")) {
                                            quantity_OrderedItemDesp = "1";
                                        }
                                        quantity_OrderedItemDespDouble = Double.parseDouble(quantity_OrderedItemDesp);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    String finalWeight_OrderedItemDesp = "";
                                    if (modal_manageOrders_From_orderedItemDesp.getItemFinalWeight() != null) {
                                        finalWeight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getItemFinalWeight();
                                        ;

                                    } else {
                                        finalWeight_OrderedItemDesp = "";
                                    }


                                    try {
                                        finalWeight_OrderedItemDesp = finalWeight_OrderedItemDesp.replaceAll("[^\\d.]", "");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    double finalWeight_OrderedItemDespDouble = 0;
                                    try {

                                        finalWeight_OrderedItemDespDouble = Double.parseDouble(finalWeight_OrderedItemDesp);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    try {

                                        grossweightingrams_OrderedItemDesp_double = finalWeight_OrderedItemDespDouble - grossweightingrams_OrderedItemDesp_double;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    double grossWeightWithQuantity_OrderedItemDesp_double = 0;
                                    if (modal_manageOrders_From_orderedItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                                        try {
                                            grossWeightWithQuantity_OrderedItemDesp_double = quantityDouble;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (modal_manageOrders_From_orderedItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                                        try {
                                            grossWeightWithQuantity_OrderedItemDesp_double = grossweightingrams_OrderedItemDesp_double * quantity_OrderedItemDespDouble;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    try {
                                        grossWeightWithQuantity_double = grossWeightWithQuantity_double + grossWeightWithQuantity_OrderedItemDesp_double;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }

                    }

                    */
                    if (inventoryDetailsArray.length() == 0) {
                        System.out.println("`Executing` stockIncomingKey_AvlDetails: 0 " + modal_manageOrders_pojo_class.getStockincomingkey());

                        final_orderdItems_desp.add(modal_manageOrders_pojo_class);

                        if(orderdItems_desp.size()-1 == i){
                            processTheFinalArrayAndCallStockOutGoingDetails();
                        }
                        try {
                           // getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            int jsonArrayIterator = 0;
                            int jsonArrayCount = inventoryDetailsArray.length();
                            for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {
                                try {
                                    JSONObject json_InventoryDetails = inventoryDetailsArray.getJSONObject(jsonArrayIterator);
                                    String menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");
                                    for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItemArray.size(); iterator_menuitemStockAvlDetails++) {
                                        Modal_ManageOrders_Pojo_Class modalManageOrdersPojoClass_inventoryDetails = new Modal_ManageOrders_Pojo_Class();
                                        Modal_MenuItem modal_menuItemInventoryDetailsItem = MenuItemArray.get(iterator_menuitemStockAvlDetails);

                                        String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemInventoryDetailsItem.getKey());

                                        String grossweightFromInventoryDetails = "";
                                        String grossWeightingramsFromInventoryDetails = "";

                                        try{
                                            if (json_InventoryDetails.has("grossweightingrams")) {
                                                grossweightFromInventoryDetails = String.valueOf(json_InventoryDetails.getString("grossweightingrams"));

                                            }
                                            else {
                                                grossweightFromInventoryDetails = modal_manageOrders_pojo_class.getGrossweight();
                                                try {
                                                    grossWeightingramsFromInventoryDetails = grossweightFromInventoryDetails.replaceAll("[^\\d.]", "");
                                                } catch (Exception ee) {
                                                    ee.printStackTrace();
                                                }
                                            }
                                            try {
                                                if (!grossweightFromInventoryDetails.equals("")) {
                                                    grossWeightingramsFromInventoryDetails = grossweightFromInventoryDetails.replaceAll("[^\\d.]", "");

                                                } else {
                                                    grossweightFromInventoryDetails = modal_manageOrders_pojo_class.getGrossweight();
                                                    try {
                                                        grossWeightingramsFromInventoryDetails = grossweightFromInventoryDetails.replaceAll("[^\\d.]", "");
                                                    } catch (Exception ee) {
                                                        ee.printStackTrace();
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                grossweightFromInventoryDetails = modal_manageOrders_pojo_class.getGrossweight();
                                                try {
                                                    grossWeightingramsFromInventoryDetails = grossweightFromInventoryDetails.replaceAll("[^\\d.]", "");
                                                } catch (Exception ee) {
                                                    ee.printStackTrace();
                                                }
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        String quantity="";

                                        try {
                                            quantity =String.valueOf(modal_manageOrders_pojo_class.getQuantity());

                                        } catch (Exception ee) {
                                            ee.printStackTrace();
                                        }
                                        String finalweight="";

                                        try {
                                            finalweight =String.valueOf(modal_manageOrders_pojo_class.getItemFinalWeight());

                                        } catch (Exception ee) {
                                            ee.printStackTrace();
                                        }

                                        modalManageOrdersPojoClass_inventoryDetails.setGrossweight(grossweightFromInventoryDetails);
                                        modalManageOrdersPojoClass_inventoryDetails.setGrossweightingrams(grossWeightingramsFromInventoryDetails);
                                        modalManageOrdersPojoClass_inventoryDetails.setQuantity(quantity);
                                        modalManageOrdersPojoClass_inventoryDetails.setItemFinalWeight(finalweight);

                                        if (menuItemKeyFromInventoryDetails.equals(menuItemKeyFromMenuAvlDetails)) {


                                           String stockIncomingKey_AvlDetails = "";
                                            if (Objects.requireNonNull(modal_menuItemInventoryDetailsItem).getStockincomingkey_AvlDetails() != null) {
                                                stockIncomingKey_AvlDetails = modal_menuItemInventoryDetailsItem.getStockincomingkey_AvlDetails();
                                            } else {
                                                stockIncomingKey_AvlDetails = "";
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setStockincomingkey(stockIncomingKey_AvlDetails);

                                            String key_AvlDetails = "";
                                            if (Objects.requireNonNull(modal_menuItemInventoryDetailsItem).getKey_AvlDetails() != null) {
                                                key_AvlDetails = modal_menuItemInventoryDetailsItem.getKey_AvlDetails();
                                            } else {
                                                key_AvlDetails = "";
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setStockavldetailskey(key_AvlDetails);


                                            String menuItemKey = "";
                                            if (Objects.requireNonNull(modal_menuItemInventoryDetailsItem).getMenuItemId() != null) {
                                                menuItemKey = modal_menuItemInventoryDetailsItem.getMenuItemId();
                                            } else {
                                                menuItemKey = "";
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setMenuItemKey(menuItemKey);

                                            String  receivedStock_AvlDetails = "";
                                            if (Objects.requireNonNull(modal_menuItemInventoryDetailsItem).getReceivedstock_AvlDetails() != null) {
                                                receivedStock_AvlDetails = modal_menuItemInventoryDetailsItem.getReceivedstock_AvlDetails();
                                            } else {
                                                receivedStock_AvlDetails = "";
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setReceivedstock(receivedStock_AvlDetails);





                                            String barcode = "";
                                            if (modal_menuItemInventoryDetailsItem.getBarcode() != null) {
                                                barcode = String.valueOf(modal_menuItemInventoryDetailsItem.getBarcode());
                                            } else {
                                                barcode = "";
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setBarcode(barcode);


                                            String priceTypeForPOS = "";
                                            if (modal_menuItemInventoryDetailsItem.getPricetypeforpos() != null) {
                                                priceTypeForPOS = String.valueOf(modal_menuItemInventoryDetailsItem.getPricetypeforpos());
                                            } else {
                                                priceTypeForPOS = "";
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setPricetypeforpos(priceTypeForPOS);


                                            String tmcCtgy = "";
                                            if (modal_menuItemInventoryDetailsItem.getTmcctgykey() != null) {
                                                tmcCtgy = String.valueOf(modal_menuItemInventoryDetailsItem.getTmcctgykey());
                                            } else {
                                                tmcCtgy = "";
                                            }

                                            modalManageOrdersPojoClass_inventoryDetails.setTmcctgykey(tmcCtgy);

                                            String tmcSubCtgyKey = "";
                                            if (modal_menuItemInventoryDetailsItem.getTmcsubctgykey() != null) {
                                                tmcSubCtgyKey = String.valueOf(modal_menuItemInventoryDetailsItem.getTmcsubctgykey());
                                            } else {
                                                tmcSubCtgyKey = "";
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setTmcSubCtgyKey(tmcSubCtgyKey);


                                            String itemName =
                                                    String.valueOf(Objects.requireNonNull(modal_menuItemInventoryDetailsItem).getItemname());

                                            if (itemName.contains("Grill House")) {
                                                itemName = itemName.replace("Grill House ", "");
                                            } else if (itemName.contains("Ready to Cook")) {
                                                itemName = itemName.replace("Ready to Cook", "");
                                            } else {
                                                itemName = itemName;
                                            }

                                            modalManageOrdersPojoClass_inventoryDetails.setItemName(itemName);

                                           boolean allowNegativeStock = false;
                                            if ((modal_menuItemInventoryDetailsItem.getAllownegativestock() != null) && (!modal_menuItemInventoryDetailsItem.getAllownegativestock().equals("null")) && (!modal_menuItemInventoryDetailsItem.getAllownegativestock().equals(""))) {
                                                allowNegativeStock = Boolean.parseBoolean(modal_menuItemInventoryDetailsItem.getAllownegativestock().toUpperCase());
                                            } else {
                                                allowNegativeStock = false;
                                            }

                                            modalManageOrdersPojoClass_inventoryDetails.setAllownegativestock(itemName);

                                            boolean isitemAvailable = false;
                                            if ((modal_menuItemInventoryDetailsItem.getItemavailability_AvlDetails() != null) && (!modal_menuItemInventoryDetailsItem.getItemavailability_AvlDetails().equals("null")) && (!modal_menuItemInventoryDetailsItem.getItemavailability_AvlDetails().equals(""))) {
                                                isitemAvailable = Boolean.parseBoolean(modal_menuItemInventoryDetailsItem.getItemavailability_AvlDetails().toUpperCase());
                                            } else {
                                                isitemAvailable = false;
                                            }
                                            modalManageOrdersPojoClass_inventoryDetails.setIsitemAvailable(itemName);

                                            final_orderdItems_desp.add(modalManageOrdersPojoClass_inventoryDetails);
                                        //    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);


                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }



                                if(orderdItems_desp.size()-1 == i){
                                    processTheFinalArrayAndCallStockOutGoingDetails();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                /*}

                   else{
                        updatedStockBalanceCount++;
                    }

                    */






                }
            }
            catch (Exception e){
                e.printStackTrace();
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }



    private void processTheFinalArrayAndCallStockOutGoingDetails() {

        Date c = Calendar.getInstance().getTime();




        for(int i=0; i < final_orderdItems_desp.size(); i++){
            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = final_orderdItems_desp.get(i);

            String menuItemKey = "";
            if (Objects.requireNonNull(modal_manageOrders_pojo_class).getMenuItemKey() != null) {
                menuItemKey = modal_manageOrders_pojo_class.getMenuItemKey();
            } else {
                menuItemKey = "";
            }

            System.out.println("`Executing` stockIncomingKey_AvlDetails: 1 " + modal_manageOrders_pojo_class.getStockincomingkey());

            if (!menuitemkey_of_alreadyCalculatedStockBalanceWeight.contains(menuItemKey)) {
                menuitemkey_of_alreadyCalculatedStockBalanceWeight.add(menuItemKey);



                String stockIncomingKey_AvlDetails = "";
                if (Objects.requireNonNull(modal_manageOrders_pojo_class).getStockincomingkey() != null) {
                    stockIncomingKey_AvlDetails = modal_manageOrders_pojo_class.getStockincomingkey();
                } else {
                    stockIncomingKey_AvlDetails = "";
                }

                String key_AvlDetails = "";
                if (Objects.requireNonNull(modal_manageOrders_pojo_class).getStockavldetailskey() != null) {
                    key_AvlDetails = modal_manageOrders_pojo_class.getStockavldetailskey();
                } else {
                    key_AvlDetails = "";
                }


                String receivedStock_AvlDetails = "";
                if (Objects.requireNonNull(modal_manageOrders_pojo_class).getReceivedstock() != null) {
                    receivedStock_AvlDetails = modal_manageOrders_pojo_class.getReceivedstock();
                } else {
                    receivedStock_AvlDetails = "";
                }

                String grossweight = "";
                if (modal_manageOrders_pojo_class.getGrossweight() != null) {
                    grossweight = modal_manageOrders_pojo_class.getGrossweight();

                }

                String grossWeightingrams = "";
                try {
                    if (!grossweight.equals("")) {
                        grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");

                    } else {
                        grossweight = modal_manageOrders_pojo_class.getGrossweight();
                        try {
                            grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    grossweight = modal_manageOrders_pojo_class.getGrossweight();
                    try {
                        grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                }

                String ItemUniquecodeofItem = "";
                if ((modal_manageOrders_pojo_class.getItemuniquecode() != null) && (!modal_manageOrders_pojo_class.getItemuniquecode().equals("null")) && (!modal_manageOrders_pojo_class.getItemuniquecode().equals(""))) {
                    ItemUniquecodeofItem = String.valueOf(modal_manageOrders_pojo_class.getItemuniquecode());
                } else {
                    ItemUniquecodeofItem = "";
                }
                double grossweightingrams_double = 0;
                try {
                    if (!grossWeightingrams.equals("")) {
                        grossweightingrams_double = Double.parseDouble(grossWeightingrams);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String quantity = "";
                if (modal_manageOrders_pojo_class.getQuantity() != null) {
                    quantity = modal_manageOrders_pojo_class.getQuantity();
                    ;

                } else {
                    quantity = "";
                }


                double quantityDouble = 0;
                try {
                    if (quantity.equals("")) {
                        quantity = "1";
                    }
                    quantityDouble = Double.parseDouble(quantity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    grossweightingrams_double = grossweightingrams_double * quantityDouble;
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                String finalWeight = "";
                if (modal_manageOrders_pojo_class.getItemFinalWeight() != null) {
                    finalWeight = modal_manageOrders_pojo_class.getItemFinalWeight();
                    ;

                } else {
                    finalWeight = "";
                }


                try {
                    finalWeight = finalWeight.replaceAll("[^\\d.]", "");

                } catch (Exception e) {
                    e.printStackTrace();
                }

                double finalWeightDouble = 0;
                try {

                    finalWeightDouble = Double.parseDouble(finalWeight);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(!calledFromNewOrderScreen){

                    try {

                        grossweightingrams_double = finalWeightDouble - grossweightingrams_double;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

              /*  double grossWeightWithQuantity_double = 0;
                if (modal_manageOrders_pojo_class.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                    try {
                        grossWeightWithQuantity_double = quantityDouble;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (modal_manageOrders_pojo_class.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                    try {
                        grossWeightWithQuantity_double = grossweightingrams_double * quantityDouble;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            */


                String barcode = "";
                if (modal_manageOrders_pojo_class.getBarcode() != null) {
                    barcode = String.valueOf(modal_manageOrders_pojo_class.getBarcode());
                } else {
                    barcode = "";
                }


                String priceTypeForPOS = "";
                if (modal_manageOrders_pojo_class.getPricetypeforpos() != null) {
                    priceTypeForPOS = String.valueOf(modal_manageOrders_pojo_class.getPricetypeforpos());
                } else {
                    priceTypeForPOS = "";
                }


                String tmcCtgy = "";
                if (modal_manageOrders_pojo_class.getTmcctgykey() != null) {
                    tmcCtgy = String.valueOf(modal_manageOrders_pojo_class.getTmcctgykey());
                } else {
                    tmcCtgy = "";
                }


                String tmcSubCtgyKey = "";
                if (modal_manageOrders_pojo_class.getTmcSubCtgyKey() != null) {
                    tmcSubCtgyKey = String.valueOf(modal_manageOrders_pojo_class.getTmcSubCtgyKey());
                } else {
                    tmcSubCtgyKey = "";
                }


                String itemName =
                        String.valueOf(Objects.requireNonNull(modal_manageOrders_pojo_class).getItemName());

                if (itemName.contains("Grill House")) {
                    itemName = itemName.replace("Grill House ", "");
                } else if (itemName.contains("Ready to Cook")) {
                    itemName = itemName.replace("Ready to Cook", "");
                } else {
                    itemName = itemName;
                }
                boolean allowNegativeStock = false;
                if ((modal_manageOrders_pojo_class.getAllownegativestock() != null) && (!modal_manageOrders_pojo_class.getAllownegativestock().equals("null")) && (!modal_manageOrders_pojo_class.getAllownegativestock().equals(""))) {
                    allowNegativeStock = Boolean.parseBoolean(modal_manageOrders_pojo_class.getAllownegativestock().toUpperCase());
                } else {
                    allowNegativeStock = false;
                }


                boolean isitemAvailable = false;
                if ((modal_manageOrders_pojo_class.getIsitemAvailable() != null) && (!modal_manageOrders_pojo_class.getIsitemAvailable().equals("null")) && (!modal_manageOrders_pojo_class.getIsitemAvailable().equals(""))) {
                    isitemAvailable = Boolean.parseBoolean(modal_manageOrders_pojo_class.getIsitemAvailable().toUpperCase());
                } else {
                    isitemAvailable = false;
                }




                    if(i+1 < final_orderdItems_desp.size()) {
                        for (int iterator = i + 1; iterator < final_orderdItems_desp.size(); iterator++) {
                            Modal_ManageOrders_Pojo_Class modal_manageOrders_From_orderedItemDesp = final_orderdItems_desp.get(iterator);

                            String menuItemKey_OrderedItemDesp = "";
                            try {
                                menuItemKey_OrderedItemDesp = String.valueOf(modal_manageOrders_From_orderedItemDesp.getMenuItemKey());

                            } catch (Exception e) {
                                menuItemKey_OrderedItemDesp = "";
                                e.printStackTrace();
                            }

                            try {

                                if (menuItemKey_OrderedItemDesp.toString().equals(menuItemKey.toString())) {


                                    String grossweight_OrderedItemDesp = "";
                                    if (modal_manageOrders_From_orderedItemDesp.getGrossweight() != null) {
                                        grossweight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getGrossweight();

                                    }

                                    String grossWeightingrams_OrderedItemDesp = "";
                                    try {
                                        if (!grossweight_OrderedItemDesp.equals("")) {
                                            grossWeightingrams_OrderedItemDesp = grossweight_OrderedItemDesp.replaceAll("[^\\d.]", "");

                                        } else {
                                            grossweight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getGrossweight();
                                            try {
                                                grossWeightingrams_OrderedItemDesp = grossweight_OrderedItemDesp.replaceAll("[^\\d.]", "");
                                            } catch (Exception ee) {
                                                ee.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        grossweight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getGrossweight();
                                        try {
                                            grossWeightingrams_OrderedItemDesp = grossweight_OrderedItemDesp.replaceAll("[^\\d.]", "");
                                        } catch (Exception ee) {
                                            ee.printStackTrace();
                                        }
                                    }

                                    double grossweightingrams_OrderedItemDesp_double = 0;
                                    try {
                                        if (!grossWeightingrams_OrderedItemDesp.equals("")) {
                                            grossweightingrams_OrderedItemDesp_double = Double.parseDouble(grossWeightingrams_OrderedItemDesp);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    String quantity_OrderedItemDesp = "";
                                    if (modal_manageOrders_From_orderedItemDesp.getQuantity() != null) {
                                        quantity_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getQuantity();
                                        ;

                                    } else {
                                        quantity_OrderedItemDesp = "";
                                    }


                                    double quantity_OrderedItemDespDouble = 0;
                                    try {
                                        if (quantity_OrderedItemDesp.equals("")) {
                                            quantity_OrderedItemDesp = "1";
                                        }
                                        quantity_OrderedItemDespDouble = Double.parseDouble(quantity_OrderedItemDesp);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try{
                                        grossweightingrams_OrderedItemDesp_double = grossweightingrams_OrderedItemDesp_double * quantity_OrderedItemDespDouble;
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    String finalWeight_OrderedItemDesp = "";
                                    if (modal_manageOrders_From_orderedItemDesp.getItemFinalWeight() != null) {
                                        finalWeight_OrderedItemDesp = modal_manageOrders_From_orderedItemDesp.getItemFinalWeight();
                                        ;

                                    } else {
                                        finalWeight_OrderedItemDesp = "";
                                    }


                                    try {
                                        finalWeight_OrderedItemDesp = finalWeight_OrderedItemDesp.replaceAll("[^\\d.]", "");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    double finalWeight_OrderedItemDespDouble = 0;
                                    try {

                                        finalWeight_OrderedItemDespDouble = Double.parseDouble(finalWeight_OrderedItemDesp);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if(!calledFromNewOrderScreen){

                                        try {

                                            grossweightingrams_OrderedItemDesp_double = finalWeight_OrderedItemDespDouble - grossweightingrams_OrderedItemDesp_double;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    /*double grossWeightWithQuantity_OrderedItemDesp_double = 0;
                                    if (modal_manageOrders_From_orderedItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                                        try {
                                            grossWeightWithQuantity_OrderedItemDesp_double = quantityDouble;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else if (modal_manageOrders_From_orderedItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                                        try {
                                            grossWeightWithQuantity_OrderedItemDesp_double = grossweightingrams_OrderedItemDesp_double * quantity_OrderedItemDespDouble;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                     */


                                    try {
                                        grossweightingrams_double = grossweightingrams_double + grossweightingrams_OrderedItemDesp_double;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            if (final_orderdItems_desp.size() - 1 == iterator) {
                                Date c1 = Calendar.getInstance().getTime();




                                CallStockOutgoing callStockOutgoing = new CallStockOutgoing(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossweightingrams_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);
                               // getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                                executor.execute(callStockOutgoing);



                            }

                        }
                    }
                    else{

                        Date c1 = Calendar.getInstance().getTime();


                        CallStockOutgoing callStockOutgoing2 = new CallStockOutgoing(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossweightingrams_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                        executor.execute(callStockOutgoing2);

                        //    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);


                    }





            }
            else{
                updatedStockBalanceCount++;
            }

        }





    }


    private void getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(String stockIncomingKey_avlDetails, String key_avlDetails, String menuItemKey_avlDetails, String receivedStock_AvlDetails, double currentBillingItemWeight_double, String itemName, String barcode, String orderid, String priceTypeForPOS, String tmcCtgy, String tmcSubCtgyKey, boolean isitemAvailable, boolean allowNegativeStock) {


        if(( currentBillingItemWeight_double!=0 ) && ((!stockIncomingKey_avlDetails.equals("")) && (!stockIncomingKey_avlDetails.equals(" - ")) &&(!stockIncomingKey_avlDetails.equals("null")) && (!stockIncomingKey_avlDetails.equals(null)) && (!stockIncomingKey_avlDetails.equals("0")) && (!stockIncomingKey_avlDetails.equals(" 0 ")) && (!stockIncomingKey_avlDetails.equals("-")) && (!stockIncomingKey_avlDetails.equals("nil"))) ) {
           // System.out.println("Total Number of threads  Current thread: 1 : " + Thread.activeCount());

           /* Set<Thread> threads = Thread.getAllStackTraces().keySet();
            System.out.printf("%-15s \t %-15s \t %-15s \t %s\n", "Name", "State", "Priority", "statusss");
            for (Thread t : threads) {
                System.out.printf("%-15s \t %-15s \t %-15d \t %s\n", t.getName(), t.getState(), t.getPriority(), t.isAlive());
            }

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

            */
                    final double[] outgoingqty_stockOutGngDetails_Double = {0};

                    final double[] Total_outgoingqty_stockOutGngDetails_Double = {0};
                    final double[] receivedStock_AvlDetails_double = {0};
                    final double[] finalStockBalance_double = {0};

                    final String[] tmcSubCtgyKey_stockOutGngDetails_String = {""};
                    final String[] outgoingtype_stockOutGngDetails_String = {""};
                    final String[] stockincomingkey_stockOutGngDetails_String = {""};
                    final String[] stocktype_stockOutGngDetails_String = {""};
                    final String[] outgoingqty_stockOutGngDetails_String = {""};
                    final String[] salesorderid_stockOutngDetails_String = {""};
                    final String[] key_stockOutGngDetails_String = {""};

                    Total_outgoingqty_stockOutGngDetails_Double[0] = 0;
                    finalStockBalance_double[0] = 0;
                    outgoingqty_stockOutGngDetails_Double[0] = 0;
                    stocktype_stockOutGngDetails_String[0] = "";
                    outgoingtype_stockOutGngDetails_String[0] = "";
                    stockincomingkey_stockOutGngDetails_String[0] = "";
                    outgoingqty_stockOutGngDetails_String[0] = "0";
                    salesorderid_stockOutngDetails_String[0] = "0";
                    receivedStock_AvlDetails_double[0] = 0;
                    key_stockOutGngDetails_String[0] = "0";


     /*   Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

            }
        };


        new Thread(runnable).start();//to work in Background
        new Handler().postDelayed(runnable, 500 );//where 500 is delayMillis  // to work on mainThread


      */

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofStockOutGoingDetailsForStockIncmgKey + stockIncomingKey_avlDetails, null,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {


                                    try {

                                        JSONArray JArray = response.getJSONArray("content");

                                        int arrayLength = JArray.length();

                                        System.out.println("response from  getStockItemOutGoingDetail2s  method  arrayLength " + String.valueOf(arrayLength));

                                        for (int i = 0; i < arrayLength; i++) {
                                            JSONObject json = JArray.getJSONObject(i);
                                            outgoingqty_stockOutGngDetails_Double[0] = 0;
                                            stocktype_stockOutGngDetails_String[0] = "";
                                            outgoingtype_stockOutGngDetails_String[0] = "";
                                            stockincomingkey_stockOutGngDetails_String[0] = "";
                                            outgoingqty_stockOutGngDetails_String[0] = "0";
                                            receivedStock_AvlDetails_double[0] = 0;
                                            salesorderid_stockOutngDetails_String[0]="0";
                                            key_stockOutGngDetails_String[0] = "0";

                                            try {
                                                if (json.has("outgoingtype")) {
                                                    outgoingtype_stockOutGngDetails_String[0] = (json.getString("outgoingtype"));
                                                } else {
                                                    outgoingtype_stockOutGngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                outgoingtype_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("key")) {
                                                    key_stockOutGngDetails_String[0] = (json.getString("key"));
                                                } else {
                                                    key_stockOutGngDetails_String[0]= "";
                                                }
                                            } catch (Exception e) {
                                                key_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("salesorderid")) {
                                                    salesorderid_stockOutngDetails_String[0] = (json.getString("salesorderid"));
                                                } else {
                                                    salesorderid_stockOutngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                salesorderid_stockOutngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }

                                            /*if(salesorderid_stockOutngDetails_String[0].equals(orderid)){
                                                if(outgoingtype_stockOutGngDetails_String[0].equals(Constants.SALES_ALLOCATED_OUTGOINGTYPE)){
                                                    updateStockOutgngType(key_stockOutGngDetails_String[0]);
                                                }
                                            }

                                             */



                                            if(!outgoingtype_stockOutGngDetails_String[0].equals(Constants.SALES_CANCELLED_OUTGOINGTYPE)){

                                                try {
                                                    if (json.has("outgoingqty")) {
                                                        outgoingqty_stockOutGngDetails_String[0] = (json.getString("outgoingqty"));
                                                    } else {
                                                        outgoingqty_stockOutGngDetails_String[0] = "0";
                                                    }
                                                } catch (Exception e) {
                                                    outgoingqty_stockOutGngDetails_String[0] = "0";

                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("stocktype")) {
                                                        stocktype_stockOutGngDetails_String[0] = (json.getString("stocktype"));
                                                    } else {
                                                        stocktype_stockOutGngDetails_String[0] = "";
                                                    }
                                                } catch (Exception e) {
                                                    stocktype_stockOutGngDetails_String[0] = "";

                                                    e.printStackTrace();
                                                }


                                                //Log.i(TAG, "getStock incoming stocktype_stockOutGngDetails_String" + stocktype_stockOutGngDetails_String[0]);

                                                //Log.i(TAG, "getStock incoming outgoingtype_stockOutGngDetails_String" + outgoingtype_stockOutGngDetails_String[0]);


                                                try {
                                                    if (json.has("tmcsubctgykey")) {
                                                        tmcSubCtgyKey_stockOutGngDetails_String[0] = (json.getString("tmcsubctgykey"));
                                                    } else {
                                                        tmcSubCtgyKey_stockOutGngDetails_String[0] = "";
                                                    }
                                                } catch (Exception e) {
                                                    tmcSubCtgyKey_stockOutGngDetails_String[0] = "";

                                                    e.printStackTrace();
                                                }



                                                //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_String" + outgoingqty_stockOutGngDetails_String[0]);


                                                try {
                                                    outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


                                                try {
                                                    if (json.has("stockincomingkey")) {
                                                        stockincomingkey_stockOutGngDetails_String[0] = (json.getString("stockincomingkey"));
                                                    } else {
                                                        stockincomingkey_stockOutGngDetails_String[0] = "";
                                                    }
                                                } catch (Exception e) {
                                                    stockincomingkey_stockOutGngDetails_String[0] = "";

                                                    e.printStackTrace();
                                                }

                                                //Log.i(TAG, "getStock incoming stockincomingkey_stockOutGngDetails_String" + stockincomingkey_stockOutGngDetails_String[0]);


                                                try {
                                                    outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


                                                if (outgoingtype_stockOutGngDetails_String[0].equals(Constants.SUPPLYGAP_OUTGOINGTYPE)) {
                                                    //Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 1 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                    //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 1 " + outgoingqty_stockOutGngDetails_Double[0]);


                                                    try {
                                                        //  if (Total_outgoingqty_stockOutGngDetails_Double[0] > outgoingqty_stockOutGngDetails_Double[0]) {
                                                        //    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];
                                                        //     //Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 2 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        //     //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 2 " + outgoingqty_stockOutGngDetails_Double[0]);

                                                        // }
                                                        //else {
                                                        //      //Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 2.1 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        //      //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 2.1  " + outgoingqty_stockOutGngDetails_Double[0]);

                                                        //       Total_outgoingqty_stockOutGngDetails_Double[0] = outgoingqty_stockOutGngDetails_Double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                                        //   }
                                                        Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                } else {
                                                    try {
                                                        //Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 3 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 3 " + outgoingqty_stockOutGngDetails_Double[0]);

                                                        Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] + outgoingqty_stockOutGngDetails_Double[0];
                                                        //Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 4 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        //Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 4 " + outgoingqty_stockOutGngDetails_Double[0]);


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                            }
                                        }

                                        //Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);


                                        //Log.i(TAG, "getStock incoming receivedStock_AvlDetails  " + receivedStock_AvlDetails);

                                        try {
                                            receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedStock_AvlDetails);
                                            //Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        double stockBalanceBeforeMinusCurrentItem = 0;
                                        try {
                                            //Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double 2  " + receivedStock_AvlDetails_double[0]);
                                            //Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double  5  " + Total_outgoingqty_stockOutGngDetails_Double[0]);

                                            stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                            //Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 2  " + stockBalanceBeforeMinusCurrentItem);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        try {


                                            //Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 3  " + stockBalanceBeforeMinusCurrentItem);

                                            finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem - currentBillingItemWeight_double;

                                            //Log.i(TAG, "getStock incoming currentBillingItemWeight_double 4 " + currentBillingItemWeight_double);
                                            //Log.i(TAG, "getStock incoming finalStockBalance_double 4 " + finalStockBalance_double[0]);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuItemKey_avlDetails, stockIncomingKey_avlDetails, itemName, barcode);

                                        // UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0]);

                                        AddDataInStockOutGoingTable(currentBillingItemWeight_double, orderid, stockIncomingKey_avlDetails, itemName, barcode, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey);

                                        if (isitemAvailable) {

                                            if (finalStockBalance_double[0] <= 0) {

                                                if (!allowNegativeStock) {


                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], true, false, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                                } else {
                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                                }


                                            } else {
                                                UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                            }
                                        } else {
                                            UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                        }

                                    } catch (Exception e) {
                                        UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                        e.printStackTrace();
                                    }


                                }

                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(@NonNull VolleyError error) {
                            //Log.d(TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

                            String errorCode = "";
                            if (error instanceof TimeoutError) {
                                errorCode = "Time Out Error";
                            } else if (error instanceof NoConnectionError) {
                                errorCode = "No Connection Error";

                            } else if (error instanceof AuthFailureError) {
                                errorCode = "Auth_Failure Error";
                            } else if (error instanceof ServerError) {
                                errorCode = "Server Error";
                            } else if (error instanceof NetworkError) {
                                errorCode = "Network Error";
                            } else if (error instanceof ParseError) {
                                errorCode = "Parse Error";
                            }
                            Toast.makeText(mContext, "Error in General App Data code :  " + errorCode, Toast.LENGTH_LONG).show();



                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            final Map<String, String> params = new HashMap<>();
                            params.put("modulename", "Mobile");
                            //params.put("orderplacedtime", "12/26/2020");

                            return params;
                        }


                        @NonNull
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            final Map<String, String> header = new HashMap<>();
                            header.put("Content-Type", "application/json");

                            return header;
                        }
                    };
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    // Make the request
                    Volley.newRequestQueue(mContext).add(jsonObjectRequest);


               // }
           // };


         //   new Thread(runnable).start();//to work in Background


        }
        else{

       //     Toast.makeText(mContext, "No  Menu Item Stock  details for " + itemName, Toast.LENGTH_LONG).show();
            updatedStockBalanceCount++;


            if(final_orderdItems_desp.size() == updatedStockBalanceCount){

                awaitTerminationAfterShutdown(executor);
                    mResultCallback_add_updateInventoryEntriesInterface.notifySuccess("", "success");



            }
        }
    }


    private void getStockOutGoingDetailsUsingOrderid(String orderid) {



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getStockOutgoingUsingSalesOrderid_Type+"?outgoingtype="+Constants.SALES_ALLOCATED_OUTGOINGTYPE+"&salesorderid="+orderid ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                            try {


                                //converting jsonSTRING into array
                                JSONArray JArray  = response.getJSONArray("content");
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1=0;
                                int arrayLength = JArray.length();
                                /*Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                if(arrayLength>1){
                                    Toast.makeText(mContext, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();


                                }

                                 */

                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        String entryKey = json.getString("key");


                                        updateStockOutgngType(entryKey);







                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();


                            }




                        }
                        catch (Exception e){
                            e.printStackTrace();


                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(mContext, "PaymentMode cnanot be found", Toast.LENGTH_LONG).show();


                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

                    error.printStackTrace();


                }
                catch (Exception e){
                    e.printStackTrace();

                }
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_1");
                params.put("orderplacedtime", "11 Jan 2021");

                return params;
            }



            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");

                return header;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





    }


    private void updateStockOutgngType(String entryKey) {
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("outgoingtype", Constants.SALES_FULFILLED_OUTGOINGTYPE);
            jsonObject.put("key", entryKey);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        System.out.println("updateStockOutgngType  method  time " );

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateStockOutgoingUsingKey,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
           //     mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
            //    mobile_manageOrders1.Adjusting_Widgets_Visibility(false);

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }

    private void AddDataInStockBalanceTransactionHistory(double finalStockBalance_double, double stockBalanceBeforeMinusCurrentItem, String menuItemKey_avlDetails, String stockIncomingKey_avlDetails, String itemName, String barcode) {


        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode",String.valueOf(barcode));
            jsonObject.put("itemname", String.valueOf(itemName));
            jsonObject.put("transactiontime",String.valueOf(currenttime));
            jsonObject.put("menuitemkey", String.valueOf(menuItemKey_avlDetails));
            jsonObject.put("newstockbalance",finalStockBalance_double);
            jsonObject.put("oldstockbalance", stockBalanceBeforeMinusCurrentItem);
            jsonObject.put("stockincomingkey",String.valueOf(stockIncomingKey_avlDetails));
            jsonObject.put("usermobileno", String.valueOf(customerMobileNo));
            jsonObject.put("vendorkey", String.valueOf(vendorkey));




        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addEntry_StockTransactionHistory
                , jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        ////Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        //    showProgressBar(false);
                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                ////Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                ////Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);



    }



    private void UpdateStockBalanceinMenuItemStockAvlDetail(String key_avlDetails, double finalStockBalance_double, boolean changeItemAvailability, boolean isitemAvailable, String menuItemKey_avlDetails, String tmcSubCtgyKey, String itemName) {


        final String[] message = {""};
        //  showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        if(changeItemAvailability){


            ////Log.d(TAG, " uploaduserDatatoDB.");
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject2.put("key", menuItemKey_avlDetails);


                jsonObject2.put("itemavailability", isitemAvailable);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            ////Log.d(Constants.TAG, "Request Payload: " + jsonObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMenuItemDetails,
                    jsonObject2, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {
                    ////Log.d(Constants.TAG, "Response: " + response);



                    //Log.d(TAG, "change menu Item " + response.length());
                    try {
                        message[0] = response.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if(changeItemAvailability) {
                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItemArray.size(); iterator_menuitemStockAvlDetails++) {

                            Modal_MenuItem modal_menuItemStockAvlDetails = MenuItemArray.get(iterator_menuitemStockAvlDetails);

                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuItemId());

                            if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(false));
                                modal_menuItemStockAvlDetails.setItemavailability_AvlDetails(String.valueOf(false));
                                modal_menuItemStockAvlDetails.setAllownegativestock(String.valueOf(false));

                                uploadMenuAvailabilityStatusTranscationinDB(customerMobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorkey,currenttime,menuItemKey_avlDetails, message[0], "", false, "");
                               // savedMenuIteminSharedPrefrences(MenuItemArray,iterator_menuitemStockAvlDetails);

                            }

                        }
                    }

                    //    showProgressBar(false);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    ////Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.toString());



                    Toast.makeText(mContext,"Failed to  update Menu Item",Toast.LENGTH_LONG).show();
                    message[0] ="Volley Error";
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
            RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);

            // Make the request
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);





            try {
                jsonObject.put("key",key_avlDetails);
                jsonObject.put("lastupdatedtime",currenttime);
                jsonObject.put("stockbalance", finalStockBalance_double);
                jsonObject.put("itemavailability",isitemAvailable);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                jsonObject.put("key",key_avlDetails);
                jsonObject.put("lastupdatedtime",currenttime);
                jsonObject.put("stockbalance", finalStockBalance_double);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



/*

        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key",key_avlDetails);
            jsonObject.put("lastupdatedtime",Currenttime);
            jsonObject.put("stockbalance", finalStockBalance_double);



        } catch (JSONException e) {
            e.printStackTrace();
        }

 */
        ////Log.d(Constants.TAG, "Request Payload: " + jsonObject);
        if(!key_avlDetails.equals("")) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_MenuItemStockAvlDetails
                    ,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    try {
                        updatedStockBalanceCount++;
                         message[0] = response.getString("message");
                        if (message[0].equals("success")) {
                            ////Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                            // showProgressBar(false);
                            if(changeItemAvailability) {
                                for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItemArray.size(); iterator_menuitemStockAvlDetails++) {

                                    Modal_MenuItem modal_menuItemStockAvlDetails = MenuItemArray.get(iterator_menuitemStockAvlDetails);

                                    String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuItemId());

                                    if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                        modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(false));
                                        modal_menuItemStockAvlDetails.setItemavailability_AvlDetails(String.valueOf(false));
                                        modal_menuItemStockAvlDetails.setAllownegativestock(String.valueOf(false));
                                        modal_menuItemStockAvlDetails.setStockbalance_AvlDetails(String.valueOf(finalStockBalance_double));

                                        uploadMenuAvailabilityStatusTranscationinDB(customerMobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorkey,currenttime,menuItemKey_avlDetails, message[0], "", false, "");
                                       // savedMenuIteminSharedPrefrences(MenuItemArray,iterator_menuitemStockAvlDetails);

                                    }

                                }
                            }



                        } else {
                            Toast.makeText(mContext, "No  Menu Item Stock Avl details for " + itemName, Toast.LENGTH_LONG).show();

                        }



                    } catch (JSONException e) {
                          Toast.makeText(mContext, "Failed to  update Menu Item Stock Avl details", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    if(final_orderdItems_desp.size() == updatedStockBalanceCount){


                        awaitTerminationAfterShutdown(executor);
                        if(message[0].toLowerCase().equals("success") || message[0].toLowerCase().equals("") ){
                            System.out.println(" notifySuccess  updatedStockBalanceCount " + String.valueOf(updatedStockBalanceCount));
                            System.out.println("notifySuccesss    final_orderdItems_desp " + final_orderdItems_desp);

                            mResultCallback_add_updateInventoryEntriesInterface.notifySuccess("", message[0]);


                        }
                        else{
                            mResultCallback_add_updateInventoryEntriesInterface.notifyError("", message[0]);

                        }

                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    ////Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.toString());
                    updatedStockBalanceCount++;
                    Toast.makeText(mContext, "Failed to  update Menu Item Stock Avl details", Toast.LENGTH_LONG).show();
                    message[0] ="Volley Error";

                    if(final_orderdItems_desp.size() == updatedStockBalanceCount){
                        awaitTerminationAfterShutdown(executor);
                        if(message[0].toLowerCase().equals("success") || message[0].toLowerCase().equals("") ){
                            System.out.println(" notifySuccess  bupdatedStockBalanceCount " + String.valueOf(updatedStockBalanceCount));
                            System.out.println( "notifySuccess    bfinal_orderdItems_desp " + final_orderdItems_desp);

                            mResultCallback_add_updateInventoryEntriesInterface.notifySuccess("", message[0]);


                        }
                        else{
                            mResultCallback_add_updateInventoryEntriesInterface.notifyError("", message[0]);

                        }

                    }

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
            RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(policy);

            // Make the request
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);


        }
        else{
            if(final_orderdItems_desp.size() == updatedStockBalanceCount){
                awaitTerminationAfterShutdown(executor);
                if(message[0].toLowerCase().equals("success") || message[0].toLowerCase().equals("") ){
                    System.out.println(" notifySuccess  cupdatedStockBalanceCount " + String.valueOf(updatedStockBalanceCount));
                    System.out.println(" notifySuccess    cfinal_orderdItems_desp " + final_orderdItems_desp);

                    mResultCallback_add_updateInventoryEntriesInterface.notifySuccess("", message[0]);


                }
                else{
                    mResultCallback_add_updateInventoryEntriesInterface.notifyError("", message[0]);

                }
        }



        }

    }



    public void awaitTerminationAfterShutdown(ThreadPoolExecutor threadPool) {
       // System.out.println("Total Number of threads  Current thread: 2 : " + Thread.activeCount());

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
            //   System.out.println("Total Number of threads  Current thread: 44 : " + Thread.activeCount());

                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }

       // System.out.println("Total Number of threads  Current thread: 3 : " + Thread.activeCount());

    }



    private void AddDataInStockOutGoingTable(double grossweightingrams_double, String orderid, String stockIncomingKey_avlDetails, String itemName, String barcode, String priceTypeForPOS, String tmcCtgy, String tmcSubCtgyKey) {

        String stockType = "";
        try{
            stockType = String.valueOf(priceTypeForPOS).toUpperCase();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{

            if(stockType.equals("TMCPRICE")){
                stockType = "unit";
            }
            else if(stockType.equals("TMCPRICEPERKG")){
                stockType = "grams";
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode",String.valueOf(barcode));
            jsonObject.put("itemname", String.valueOf(itemName));
            jsonObject.put("outgoingdate",String.valueOf(currenttime));
            jsonObject.put("outgoingtype", String.valueOf(Constants.SALES_FULFILLED_OUTGOINGTYPE));
            jsonObject.put("outgoingqty",grossweightingrams_double);
            jsonObject.put("salesorderid", String.valueOf(orderid));
            jsonObject.put("stocktype",(stockType));
            jsonObject.put("tmcctgykey", String.valueOf(tmcCtgy));
            jsonObject.put("tmcsubctgykey", String.valueOf(tmcSubCtgyKey));
            jsonObject.put("vendorkey", String.valueOf(vendorkey));
            jsonObject.put("userkey", String.valueOf(""));
            jsonObject.put("stockincomingkey", String.valueOf(stockIncomingKey_avlDetails));
            jsonObject.put("inventoryusermobileno", String.valueOf(customerMobileNo));



        } catch (JSONException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_addEntry_StockOutGoingDetails
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        ////Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        // showProgressBar(false);
                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                ////Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                ////Log.d(Constants.TAG, "Error: " + error.toString());
                Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





    }

    private void uploadMenuAvailabilityStatusTranscationinDB(String userPhoneNumber, String menuItemName, boolean availability, String menuItemSubCtgykey, String vendorkey, String dateandtime, String menuItemKey, String message, String menuItemStockAvlDetailskey, boolean allowNegative, String itemStockAvlDetailskey) {


        ////Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("itemname", menuItemName);
            jsonObject.put("status", availability);
            jsonObject.put("subCtgykey", menuItemSubCtgykey);
            jsonObject.put("transactiontime", dateandtime);
            jsonObject.put("mobileno", userPhoneNumber);
            jsonObject.put("vendorkey", vendorkey);
            jsonObject.put("menuitemkey", menuItemKey);
            jsonObject.put("transcationstatus", message);
            try {
                if (!menuItemStockAvlDetailskey.equals("")) {
                    jsonObject.put("menuitemstockavldetailskey", menuItemStockAvlDetailskey);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if ((!menuItemStockAvlDetailskey.equals("")) ) {
                    jsonObject.put("allownegativestock", allowNegative);

                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addMenuavailabilityTransaction,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                ////Log.d(Constants.TAG, "Response: " + response);
                //  showProgressBar(false);
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

                return params;
            }
        };



        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }


    private void savedMenuIteminSharedPrefrences(List<Modal_MenuItem> menuItem, int iterator_menuitemStockAvlDetails) {
        final SharedPreferences sharedPreferencesMenuitem = mContext.getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList",json );
        editor.apply();

    }



    public class CallStockOutgoing implements Runnable {
        private String stockIncomingKey_AvlDetails,key_AvlDetails, menuItemKey, receivedStock_AvlDetails, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey;
        boolean isitemAvailable = false , allowNegativeStock  = false ;
        double grossWeightWithQuantity_double = 0;
        String methodToCall ="";


        public CallStockOutgoing(String stockIncomingKey_avlDetails, String key_avlDetails, String menuItemKey, String receivedStock_avlDetails, double grossWeightWithQuantity_double, String itemName, String barcode, String orderid, String priceTypeForPOS, String tmcCtgy, String tmcSubCtgyKey, boolean isitemAvailable, boolean allowNegativeStock) {
            this.stockIncomingKey_AvlDetails = stockIncomingKey_avlDetails;
            this.key_AvlDetails = key_avlDetails;
            this.menuItemKey = menuItemKey;
            this.receivedStock_AvlDetails = receivedStock_avlDetails;
            this.itemName = itemName;
            this.barcode = barcode;
            this.orderid = orderid;
            this.priceTypeForPOS = priceTypeForPOS;
            this.tmcCtgy = tmcCtgy;
            this.tmcSubCtgyKey = tmcSubCtgyKey;
            this.isitemAvailable = isitemAvailable;
            this.allowNegativeStock = allowNegativeStock;
            this.grossWeightWithQuantity_double = grossWeightWithQuantity_double;
            this.methodToCall = "CallUsingStockIncomingKey";


        }

        public CallStockOutgoing(String orderid) {
            this.orderid = orderid;
            this.methodToCall = "CallUsingOrderid";

        }


        public void run() {
            try {

                if(methodToCall.equals("CallUsingOrderid")){
                    getStockOutGoingDetailsUsingOrderid(orderid);
                }
                else if(methodToCall.equals("CallUsingStockIncomingKey")) {
                    System.out.println("`Executing` : " + itemName);
                    System.out.println("`Executing` stockIncomingKey_AvlDetails: 3  " + stockIncomingKey_AvlDetails);

                    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);
                }
                TimeUnit.SECONDS.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
