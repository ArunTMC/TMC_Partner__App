package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Add_CustomerOrder_TrackingTableInterface;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Add_CustomerOrder_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;
import com.pos.printer.AsyncEscPosPrint;
import com.pos.printer.AsyncEscPosPrinter;
import com.pos.printer.AsyncUsbEscPosPrint;
import com.pos.printer.Modal_USBPrinter;
import com.pos.printer.PrinterFunctions;
import com.pos.printer.usb.UsbPrintersConnectionsLocal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import static com.meatchop.tmcpartner.Constants.TAG;
import static com.meatchop.tmcpartner.Constants.api_Update_MenuItemStockAvlDetails;

public class AddBigBasketOrder extends AppCompatActivity {
    static RecyclerView recyclerView;
    public static HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap();
    public static List<String> cart_Item_List = new ArrayList<>();
    static Adapter_AddBigBasketOrdersRecyclerview adapter_addBigBasketOrdersRecyclerview;
    List<Modal_MenuItem_Settings>MenuItem = new ArrayList<>();
    LinearLayout loadingPanel,loadingpanelmask;
    String vendorKey="";
    public TextView total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;
    Button procced_to_pay_widget;
    EditText bigbasketOrdersCustomermobileno,customermobileno_edittextwidget;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0,totalAmounttopay,finalamounttoPay;
    int new_totalAmount_withGst;
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay,OrderTypefromSpinner;

    String finaltoPayAmount="",usermobileNo="";
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    int netTotaL=0;
    boolean isinventorycheck = false;

    private  boolean isStockOutGoingAlreadyCalledForthisItem =false;
    public static List<String> StockBalanceChangedForThisItemList = new ArrayList<>();

    boolean isTryingtoChangeWeight=false;
    private  boolean isOrderDetailsMethodCalled =false;
    private  boolean isOrderTrackingDetailsMethodCalled =false;
    private  boolean isPaymentDetailsMethodCalled =false;
    boolean isproceedtoPay_Clicked =false, ispaymentMode_Clicked =false,isPrintedSecondTime=false;
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.AddBigBasketOrder";

    String printerType_sharedPreference = "";
    String printerStatus_sharedPreference = "";

    Modal_USBPrinter modal_usbPrinter = new Modal_USBPrinter();

    String vendorName ="";
    boolean orderdetailsnewschema = false;
    Add_CustomerOrder_TrackingTableInterface mResultCallback_Add_CustomerOrder_TrackingTableInterface = null;
    boolean  isCustomerOrdersTableServiceCalled = false;
    Context mContext;
    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
    boolean localDBcheck = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_big_basket_order);



        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        customermobileno_edittextwidget = findViewById(R.id.customermobileno_edittextwidget);
        procced_to_pay_widget = findViewById(R.id.procced_to_pay_widget);
        bigbasketOrdersCustomermobileno = findViewById(R.id.editTextPhone);
        total_item_Rs_text_widget = findViewById(R.id.total_amount_text_widget);
        total_Rs_to_Pay_text_widget = findViewById(R.id.total_Rs_to_Pay_text_widget);
        taxes_and_Charges_rs_text_widget = findViewById(R.id.taxes_and_Charges_rs_text_widget);
        recyclerView = findViewById(R.id.recyclerView);
        mContext = AddBigBasketOrder.this;

        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = shared.getString("VendorKey","");
            vendorName = shared.getString("VendorName", "");
            isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));
            usermobileNo = (shared.getString("UserPhoneNumber", "+"));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
           // orderdetailsnewschema = true;
            localDBcheck = (shared.getBoolean("localdbcheck", false));

        }
        catch(Exception e){
            e.printStackTrace();
        }

        try{
            SharedPreferences shared_PF_PrinterData = getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
            printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));
            printerStatus_sharedPreference   = (shared_PF_PrinterData.getString("printerStatus", ""));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(localDBcheck) {
            getDataFromSQL();
        }
        else{
            getMenuItemFromSharedPreferences();
        }


        //completemenuItem= getMenuItemfromString(MenuItems);
        createEmptyRowInListView("empty");

        CallAdapter();



        procced_to_pay_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (customermobileno_edittextwidget.getText().toString().length() == 10) {
                    showProgressBar(true);

                    if (cart_Item_List.size() > 0 && cartItem_hashmap.size() > 0) {

                        if ((!total_item_Rs_text_widget.getText().toString().equals("0")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals("0")) &&(!total_item_Rs_text_widget.getText().toString().equals("0.0")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals("0.0"))&&(!total_item_Rs_text_widget.getText().toString().equals("0.00")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals("0.00"))&&(!total_item_Rs_text_widget.getText().toString().equals("")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals(""))) {
                            if (checkforBarcodeInCart("empty")) {
                                AddBigBasketOrder.cart_Item_List.remove("empty");

                                AddBigBasketOrder.cartItem_hashmap.remove("empty");
                            }

                            if (checkAllPriceperkgItemWeightWasEdited()){
                                long sTime = System.currentTimeMillis();
                                Currenttime = getDate_and_time();
                                usermobileNo = "+91"+customermobileno_edittextwidget.getText().toString();
                                PlaceOrdersinDatabaseaAndPrintRecipt(Constants.BIGBASKETORDER_PAYMENTMODE, sTime, Currenttime, cart_Item_List);
                            }
                            else {
                                showProgressBar(false);

                                AlertDialogClass.showDialog(AddBigBasketOrder.this, R.string.Orders_weight_Cant_be_leave_unedited);

                            }

                        }
                        else {
                            showProgressBar(false);

                            AlertDialogClass.showDialog(AddBigBasketOrder.this, R.string.Cant_place_order);

                        }
                    } else {
                        AlertDialogClass.showDialog(AddBigBasketOrder.this, R.string.Cart_is_empty);

                    }

                } else {
                    AlertDialogClass.showDialog(AddBigBasketOrder.this, R.string.Enter_the_mobile_no_text);

                }
            }
        });






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
            MenuItem.clear();
            try {
                // if (cursor.moveToFirst()) {

                Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {
                            Modal_MenuItem_Settings modal_newOrderItems = new Modal_MenuItem_Settings();
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


                            if (!isinventorycheck) {

                                String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                        receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                                modal_newOrderItems.setBarcode_AvlDetails(barcode_AvlDetails);
                                modal_newOrderItems.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                modal_newOrderItems.setKey_AvlDetails(key_AvlDetails);
                                modal_newOrderItems.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                modal_newOrderItems.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                modal_newOrderItems.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                modal_newOrderItems.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                modal_newOrderItems.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                modal_newOrderItems.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                modal_newOrderItems.setAllownegativestock(allownegativestock_AvlDetails);





                            }
                            else{

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


                            }

                            MenuItem.add(modal_newOrderItems);
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
        finally{
            try{
                if(tmcMenuItemSQL_db_manager != null){
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    void createEmptyRowInListView(String empty) {
        Modal_NewOrderItems newOrdersPojoClass = new Modal_NewOrderItems();
        newOrdersPojoClass.itemname = "";
        newOrdersPojoClass.tmcpriceperkg = "";
        newOrdersPojoClass.grossweight = "";
        newOrdersPojoClass.netweight = "";
        newOrdersPojoClass.tmcprice = "";
        newOrdersPojoClass.gstpercentage = "";
        newOrdersPojoClass.portionsize = "";
        newOrdersPojoClass.pricetypeforpos = "";
        newOrdersPojoClass.itemFinalWeight="";
        newOrdersPojoClass.pricePerItem ="";
        newOrdersPojoClass.quantity="";
        newOrdersPojoClass.itemFinalPrice = "0";
        newOrdersPojoClass.gstpercentage = "0";
        newOrdersPojoClass.discountpercentage = "0";
        newOrdersPojoClass.istmcpriceperkgitemedited="FALSE";
        newOrdersPojoClass.itemuniquecode=empty;
        cart_Item_List.add(empty);
        cartItem_hashmap.put(empty,newOrdersPojoClass);
    }


    @Override
    protected void onStart() {
        cart_Item_List.clear();

        cartItem_hashmap.clear();
        StockBalanceChangedForThisItemList.clear();

        new_to_pay_Amount = 0;
        old_taxes_and_charges_Amount = 0;
        old_total_Amount = 0;
        createEmptyRowInListView("empty");
        CallAdapter();
        //discountAmount = "0";

        // discount_Edit_widget.setText("0");
        finaltoPayAmount = "0";
        //  discount_rs_text_widget.setText(discountAmount);
        OrderTypefromSpinner = "POS Order";
        //   orderTypeSpinner.setSelection(0);
        total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
        taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



        customermobileno_edittextwidget.setText("");
        bigbasketOrdersCustomermobileno.setText("");
        isPrintedSecondTime = false;
        ispaymentMode_Clicked = false;
        isOrderDetailsMethodCalled = false;

        isPaymentDetailsMethodCalled = false;
        isOrderTrackingDetailsMethodCalled = false;
        totalAmounttopay=0;
        finalamounttoPay=0;


        showProgressBar(false);

        super.onStart();
    }

    @Override
    public void onBackPressed() {
        cart_Item_List.clear();

        cartItem_hashmap.clear();
        StockBalanceChangedForThisItemList.clear();

        new_to_pay_Amount = 0;
        old_taxes_and_charges_Amount = 0;
        old_total_Amount = 0;
        createEmptyRowInListView("empty");
        CallAdapter();
        //discountAmount = "0";

        // discount_Edit_widget.setText("0");
        finaltoPayAmount = "0";
        //  discount_rs_text_widget.setText(discountAmount);
        OrderTypefromSpinner = "POS Order";
        //   orderTypeSpinner.setSelection(0);
        total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
        taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



        customermobileno_edittextwidget.setText("");
        bigbasketOrdersCustomermobileno.setText("");
        isPrintedSecondTime = false;
        ispaymentMode_Clicked = false;
        isOrderDetailsMethodCalled = false;

        isPaymentDetailsMethodCalled = false;
        isOrderTrackingDetailsMethodCalled = false;
        totalAmounttopay=0;
        finalamounttoPay=0;


        showProgressBar(false);

        super.onBackPressed();
    }



    public void add_amount_ForBillDetails() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for(String Key :cart_Item_List){
            Modal_NewOrderItems newOrderItems = cartItem_hashmap.get(Key);

            //find total amount with out GST
            double new_total_amountfromArray = 0, discountpercentageDecimal=0, discountPercentage=0,newsavedAmount=0,taxes_and_chargesfromArray=0;
            if (newOrderItems != null) {
                try {
                    String itemFinalPrice_string = String.valueOf(newOrderItems.getItemFinalPrice());
                    new_total_amountfromArray = Double.parseDouble(itemFinalPrice_string);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                String discountPercentage_string;
                try {
                    discountPercentage_string = String.valueOf(newOrderItems.getDiscountpercentage());

                    discountPercentage = Double.parseDouble(discountPercentage_string);

                }
                catch (Exception e ){
                    e.printStackTrace();
                }


                //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);

                try {
                    discountpercentageDecimal = (100 - discountPercentage) / 100;

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    newsavedAmount = new_total_amountfromArray / discountpercentageDecimal;

                }
                catch (Exception e){
                    e.printStackTrace();

                }

                try{
                    newOrderItems.setSavedAmount(String.valueOf(decimalFormat.format(newsavedAmount)));

                }
                catch (Exception e ){
                    e.printStackTrace();

                }


                try{
                    new_total_amount = new_total_amountfromArray;
                    old_total_Amount = old_total_Amount + new_total_amount;
                    //Log.i(TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
                    //Log.i(TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);


                }
                catch (Exception e ){
                    e.printStackTrace();

                }


                try{
                    taxes_and_chargesfromArray = Double.parseDouble(newOrderItems.getGstpercentage());
                    //Log.i(TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);

                    taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);

                }
                catch (Exception e ){
                    e.printStackTrace();

                }



                try{
                    newOrderItems.setGstAmount(String.valueOf(decimalFormat.format(taxes_and_chargesfromArray)));

                    //Log.i(TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
                    //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
                    //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
                    new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
                    old_taxes_and_charges_Amount = old_taxes_and_charges_Amount + new_taxes_and_charges_Amount;

                }
                catch (Exception e ){
                    e.printStackTrace();

                }


                try {
                    //find subtotal
                    double subTotal_perItem = new_total_amount + new_taxes_and_charges_Amount;


                    newOrderItems.setSubTotal_perItem(String.valueOf(decimalFormat.format(subTotal_perItem)));


                    //find total payable Amount
                    new_to_pay_Amount = (old_total_Amount + old_taxes_and_charges_Amount);

                }
                catch (Exception e){
                    e.printStackTrace();

                }


                //find total GST amount

                try{
                    double subTotal_perItem = new_total_amount + new_taxes_and_charges_Amount;


                    newOrderItems.setSubTotal_perItem(String.valueOf(decimalFormat.format(subTotal_perItem)));


                    //find total payable Amount
                    new_to_pay_Amount = (old_total_Amount + old_taxes_and_charges_Amount);

                }catch (Exception e){
                    e.printStackTrace();
                }

                //find subtotal
            }
        }





        try{

            total_item_Rs_text_widget.setText(decimalFormat.format(old_total_Amount));
            taxes_and_Charges_rs_text_widget.setText(decimalFormat.format(old_taxes_and_charges_Amount));

            new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);
            finaltoPayAmount = String.valueOf(new_totalAmount_withGst)+".00";
            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst)+".00");
            totalAmounttopay=new_totalAmount_withGst;
        }catch (Exception e){
            e.printStackTrace();
        }

        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }










    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: AddBigBasketOrder.cart_Item_List) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }

    private boolean checkAllPriceperkgItemWeightWasEdited() {
        boolean valuetoreturn =false;
        for (int i = 0; i < cart_Item_List.size(); i++) {
            String itemUniqueCode = cart_Item_List.get(i);
            Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);
            assert modal_newOrderItems != null;
            if(modal_newOrderItems.getIstmcpriceperkgitemedited().equals("TRUE")){
                if(i==(cart_Item_List.size()-1)){
                    valuetoreturn= true;
                }
            }
            else {
                valuetoreturn= false;
            }

        }
        return  valuetoreturn;
    }
    void CallAdapter() {
        //Log.e(TAG, "AdapterCalled  ");


        //  adapter_cartItem_listview= new Adapter_CartItem_Listview(AddBigBasketOrder.this,cartItem_hashmap, MenuItems,AddBigBasketOrder.this);
        //  listview.setAdapter(adapter_cartItem_listview);
        adapter_addBigBasketOrdersRecyclerview = new Adapter_AddBigBasketOrdersRecyclerview(getApplicationContext(),cartItem_hashmap, MenuItem, AddBigBasketOrder.this);
        adapter_addBigBasketOrdersRecyclerview.setHandler(newHandler());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        int last_index=AddBigBasketOrder.cartItem_hashmap.size()-1;

        recyclerView.setAdapter(adapter_addBigBasketOrdersRecyclerview);
        recyclerView.scrollToPosition(last_index);



    }

    private void PlaceOrdersinDatabaseaAndPrintRecipt(String paymentMode, long sTime, String currenttime, List<String> cart_Item_list) {
        showProgressBar(true);

        if (ispaymentMode_Clicked) {
            return;
        }
        else {


            if(!isCustomerOrdersTableServiceCalled){
                try{
                    if(orderdetailsnewschema){
                        String customerMobileNo = bigbasketOrdersCustomermobileno.getText().toString();
                        initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);

                        String  CouponDiscountAmount ="0";
                        String payableAmount =  total_Rs_to_Pay_text_widget.getText().toString();
                        isCustomerOrdersTableServiceCalled =true;
                        Add_CustomerOrder_TrackingTable_AsyncTask asyncTask=new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, AddBigBasketOrder.cart_Item_List, AddBigBasketOrder.cartItem_hashmap, Constants.BIGBASKETORDER_PAYMENTMODE,CouponDiscountAmount,Currenttime,customerMobileNo,Constants.BigBasket,vendorKey,vendorName, sTime,payableAmount);
                        asyncTask.execute();

                    }

                }
                catch (Exception e){
                    e.printStackTrace();

                }
            }



            ispaymentMode_Clicked = true;
            if (!isOrderDetailsMethodCalled) {

                PlaceOrder_in_OrderDetails(AddBigBasketOrder.cart_Item_List, paymentMode, sTime);
            }
            if (!isOrderTrackingDetailsMethodCalled) {

                PlaceOrder_in_OrderTrackingDetails(sTime, currenttime);
            }



        }


    }


    private void initAndPlaceOrderinCustomerOrder_TrackingInterface(Context mContext) {
        mResultCallback_Add_CustomerOrder_TrackingTableInterface = new Add_CustomerOrder_TrackingTableInterface() {


            @Override
            public void notifySuccess(String requestType, String success) {
                isCustomerOrdersTableServiceCalled = false;
            }

            @Override
            public void notifyError(String requestType, String error) {
                isCustomerOrdersTableServiceCalled = false;

                // Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        };


    }
    private void PlaceOrder_in_OrderDetails(List<String> cart_Item_List1, String Payment_mode, long sTime) {
        if(isOrderDetailsMethodCalled){
            return;
        }

        isOrderDetailsMethodCalled = true;
        String newOrderId = String.valueOf(sTime);
        SharedPreferences sh
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);


        String merchantorderid = "";
        String couponid = "";
        String CouponDiscountAmount = "0";
        String DeliveryAmount = "";

        String orderid = String.valueOf(sTime);
        String orderplacedTime = Currenttime;
        String tokenno = "";
        String userid = "";
        String ordertype = Constants.BigBasket;
        String deliverytype = Constants.STOREPICKUP_DELIVERYTYPE;
        String slotdate = "";
        deliverytype = Constants.HOME_DELIVERY_DELIVERYTYPE;
        slotdate  = CurrentDate;

        double totalgrossweightingrams_doubleFromLoop = 0, totalgrossFromInsideAndOutsideLoop = 0;

        String slotname = "";
        if(orderdetailsnewschema){
            slotname = "";

        }
        else{
            slotname = "EXPRESSDELIVERY";

        }

        String orderPlacedDate = getDate();

        String slottimerange = "";
        String UserMobile = "+91" + customermobileno_edittextwidget.getText().toString();
        String vendorkey = sh.getString("VendorKey", "vendor_1");
        String vendorName = sh.getString("VendorName", "TMCHasthinapuram");
        String itemTotalwithoutGst = total_item_Rs_text_widget.getText().toString();
        String payableAmount = total_Rs_to_Pay_text_widget.getText().toString();
        String taxAmount = taxes_and_Charges_rs_text_widget.getText().toString();
        PlaceOrder_in_PaymentTransactionDetails(sTime, Payment_mode, payableAmount, UserMobile);

        JSONArray itemDespArray = new JSONArray();

        for (int i = 0; i < cart_Item_List.size(); i++) {
            String itemUniqueCode = cart_Item_List.get(i);
            Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);






            String stockIncomingKey_AvlDetails = "";
            if (Objects.requireNonNull(modal_newOrderItems).getStockincomingkey_AvlDetails() != null) {
                stockIncomingKey_AvlDetails = modal_newOrderItems.getStockincomingkey_AvlDetails();
            } else {
                stockIncomingKey_AvlDetails = "";
            }

            String key_AvlDetails = "";
            if (Objects.requireNonNull(modal_newOrderItems).getKey_AvlDetails() != null) {
                key_AvlDetails = modal_newOrderItems.getKey_AvlDetails();
            } else {
                key_AvlDetails = "";
            }



            String menuItemKey = "";
            if (Objects.requireNonNull(modal_newOrderItems).getMenuItemId() != null) {
                menuItemKey = modal_newOrderItems.getMenuItemId();
            } else {
                menuItemKey = "";
                if (Objects.requireNonNull(modal_newOrderItems).getKey() != null) {
                    menuItemKey = modal_newOrderItems.getKey();
                } else {
                    menuItemKey = "";
                    if (Objects.requireNonNull(modal_newOrderItems).getMenuitemkey_AvlDetails() != null) {
                        menuItemKey = modal_newOrderItems.getMenuitemkey_AvlDetails();
                    } else {
                        menuItemKey = "";
                    }
                }
            }



            String receivedStock_AvlDetails = "";
            if (Objects.requireNonNull(modal_newOrderItems).getReceivedstock_AvlDetails() != null) {
                receivedStock_AvlDetails = modal_newOrderItems.getReceivedstock_AvlDetails();
            } else {
                receivedStock_AvlDetails = "";
            }

            String grossweight = "";
            if (modal_newOrderItems.getGrossweight() != null) {
                grossweight = modal_newOrderItems.getGrossweight();

            }

            String grossWeightingrams = "";
            try {
                if (!grossweight.equals("")) {
                    grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String ItemUniquecodeofItem = "";
            if ((modal_newOrderItems.getItemuniquecode() != null) && (!modal_newOrderItems.getItemuniquecode().equals("null")) && (!modal_newOrderItems.getItemuniquecode().equals(""))) {
                ItemUniquecodeofItem = String.valueOf(modal_newOrderItems.getItemuniquecode());
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
            if (modal_newOrderItems.getQuantity() != null) {
                quantity = modal_newOrderItems.getQuantity();
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

            double grossWeightWithQuantity_double = 0;
            if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                try {
                    grossWeightWithQuantity_double = quantityDouble;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                try {
                    grossWeightWithQuantity_double = grossweightingrams_double * quantityDouble;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            String barcode = "";
            if (modal_newOrderItems.getBarcode() != null) {
                barcode = String.valueOf(modal_newOrderItems.getBarcode());
            } else {
                if (modal_newOrderItems.getBarcode_AvlDetails() != null) {
                    barcode = String.valueOf(modal_newOrderItems.getBarcode_AvlDetails());
                } else {
                    barcode = "";
                }
            }


            String priceTypeForPOS = "";
            if (modal_newOrderItems.getPricetypeforpos() != null) {
                priceTypeForPOS = String.valueOf(modal_newOrderItems.getPricetypeforpos());
            } else {
                priceTypeForPOS = "";
            }


            String tmcCtgy = "";
            if (modal_newOrderItems.getTmcctgykey() != null) {
                tmcCtgy = String.valueOf(modal_newOrderItems.getTmcctgykey());
            } else {
                tmcCtgy = "";
            }


            String tmcSubCtgyKey = "";
            if (modal_newOrderItems.getTmcsubctgykey() != null) {
                tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcsubctgykey());
            } else {
                tmcSubCtgyKey = "";
            }


            String itemName =
                    String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());

            if (itemName.contains("Grill House")) {
                itemName = itemName.replace("Grill House ", "");
            } else if (itemName.contains("Ready to Cook")) {
                itemName = itemName.replace("Ready to Cook", "");
            } else {
                itemName = itemName;
            }

            //////////////////////

            /*
            if(isinventorycheck) {


                double quantityDouble = 0;
                try {
                    if (quantity.equals("")) {
                        quantity = "1";
                    }
                    quantityDouble = Double.parseDouble(quantity);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                double grossWeightWithQuantity_double = 0;
                if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                    try {
                        grossWeightWithQuantity_double = quantityDouble;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                    try {
                        grossWeightWithQuantity_double = grossweightingrams_double * quantityDouble;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                String barcode = "";
                if (modal_newOrderItems.getBarcode() != null) {
                    barcode = String.valueOf(modal_newOrderItems.getBarcode());
                } else {
                    barcode = "";
                }


                String priceTypeForPOS = "";
                if (modal_newOrderItems.getPricetypeforpos() != null) {
                    priceTypeForPOS = String.valueOf(modal_newOrderItems.getPricetypeforpos());
                } else {
                    priceTypeForPOS = "";
                }


                String tmcCtgy = "";
                if (modal_newOrderItems.getTmcctgykey() != null) {
                    tmcCtgy = String.valueOf(modal_newOrderItems.getTmcctgykey());
                } else {
                    tmcCtgy = "";
                }


                String tmcSubCtgyKey = "";
                if (modal_newOrderItems.getTmcsubctgykey() != null) {
                    tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcsubctgykey());
                } else {
                    tmcSubCtgyKey = "";
                }


                String stockIncomingKey_AvlDetails = "";
                if (Objects.requireNonNull(modal_newOrderItems).getStockincomingkey_AvlDetails() != null) {
                    stockIncomingKey_AvlDetails = modal_newOrderItems.getStockincomingkey_AvlDetails();
                } else {
                    stockIncomingKey_AvlDetails = "";
                }

                String key_AvlDetails = "";
                if (Objects.requireNonNull(modal_newOrderItems).getKey_AvlDetails() != null) {
                    key_AvlDetails = modal_newOrderItems.getKey_AvlDetails();
                } else {
                    key_AvlDetails = "";
                }

                String menuItemKey = "";
                if (Objects.requireNonNull(modal_newOrderItems).getMenuItemId() != null) {
                    menuItemKey = modal_newOrderItems.getMenuItemId();
                } else {
                    menuItemKey = "";
                    if (Objects.requireNonNull(modal_newOrderItems).getKey() != null) {
                        menuItemKey = modal_newOrderItems.getKey();
                    } else {
                        menuItemKey = "";
                        if (Objects.requireNonNull(modal_newOrderItems).getMenuitemkey_AvlDetails() != null) {
                            menuItemKey = modal_newOrderItems.getMenuitemkey_AvlDetails();
                        } else {
                            menuItemKey = "";
                        }
                    }
                }


                String receivedStock_AvlDetails = "";
                if (Objects.requireNonNull(modal_newOrderItems).getReceivedstock_AvlDetails() != null) {
                    receivedStock_AvlDetails = modal_newOrderItems.getReceivedstock_AvlDetails();
                } else {
                    receivedStock_AvlDetails = "";
                }

                String inventoryDetails_firstItem = "";
                try {
                    if ((modal_newOrderItems.getInventorydetails() != null) && (!modal_newOrderItems.getInventorydetails().equals("null")) && (!modal_newOrderItems.getInventorydetails().equals(""))) {
                        inventoryDetails_firstItem = String.valueOf(modal_newOrderItems.getInventorydetails());
                    } else {
                        inventoryDetails_firstItem = "nil";
                    }
                } catch (Exception e) {
                    inventoryDetails_firstItem = "nil";

                    e.printStackTrace();
                }


                boolean allowNegativeStock = false;
                if ((modal_newOrderItems.getAllownegativestock() != null) && (!modal_newOrderItems.getAllownegativestock().equals("null")) && (!modal_newOrderItems.getAllownegativestock().equals(""))) {
                    allowNegativeStock = Boolean.parseBoolean(modal_newOrderItems.getAllownegativestock().toUpperCase());
                } else {
                    allowNegativeStock = false;
                }


                boolean isitemAvailable = false;
                if ((modal_newOrderItems.getItemavailability_AvlDetails() != null) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals("null")) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals(""))) {
                    isitemAvailable = Boolean.parseBoolean(modal_newOrderItems.getItemavailability_AvlDetails().toUpperCase());
                } else {
                    isitemAvailable = false;
                }


                String ItemUniquecodeofItem = "";
                if ((modal_newOrderItems.getItemuniquecode() != null) && (!modal_newOrderItems.getItemuniquecode().equals("null")) && (!modal_newOrderItems.getItemuniquecode().equals(""))) {
                    ItemUniquecodeofItem = String.valueOf(modal_newOrderItems.getItemuniquecode());
                } else {
                    ItemUniquecodeofItem = "";
                }

                String BarcodeofItem = "";
                if ((modal_newOrderItems.getBarcode() != null) && (!modal_newOrderItems.getBarcode().equals("null")) && (!modal_newOrderItems.getBarcode().equals(""))) {
                    BarcodeofItem = String.valueOf(modal_newOrderItems.getBarcode());
                } else {
                    BarcodeofItem = "";
                }

                String priceTypeOfItem = "";
                if ((modal_newOrderItems.getPricetypeforpos() != null) && (!modal_newOrderItems.getPricetypeforpos().equals("null")) && (!modal_newOrderItems.getPricetypeforpos().equals(""))) {
                    priceTypeOfItem = String.valueOf(modal_newOrderItems.getPricetypeforpos());
                } else {
                    priceTypeOfItem = "";
                }


                if (!StockBalanceChangedForThisItemList.contains(ItemUniquecodeofItem)) {
                    totalgrossweightingrams_doubleFromLoop = 0;
                    totalgrossFromInsideAndOutsideLoop = 0;
                    isStockOutGoingAlreadyCalledForthisItem = false;

                    for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                        String hashmapkey = "";
                        hashmapkey = cart_Item_List.get(iterator);
                        //for (Map.Entry<String, Modal_NewOrderItems> cartItem_hashmapData : cartItem_hashmap.get(hashmapkey)) {
                        String menuItemKeyFromInventoryDetails_secondItem = "";
                        String menuItemKeyFromInventoryDetails_firstItem = "";

                        Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                        String ItemUniquecodeFromLoop = "", BarcodeFromLoop = "", grossWeightingrams_FromLoop = "", grossweight_FromLoop = "", quantityFromLoop = "", priceTypeofItemFromLoop = "";
                        double quantityDoubleFromLoop = 0, grossweightingrams_doubleFromLoop = 0;

                        try {
                            try {
                                ItemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            try {
                                priceTypeofItemFromLoop = modal_newOrderItems_insideLoop.getPricetypeforpos().toString();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            try {
                                BarcodeFromLoop = modal_newOrderItems_insideLoop.getBarcode().toString();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            //   if (!BarcodeFromLoop.equals(BarcodeofItem)) {


                            if (ItemUniquecodeofItem.equals(ItemUniquecodeFromLoop)) {

                                try {
                                    grossweight_FromLoop = "0";
                                    if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                        grossweight_FromLoop = modal_newOrderItems_insideLoop.getGrossweight();

                                    } else {
                                        grossweight_FromLoop = "0";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    quantityFromLoop = "0";

                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                        quantityFromLoop = modal_newOrderItems_insideLoop.getQuantity();

                                    } else {
                                        quantityFromLoop = "0";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (!grossweight_FromLoop.equals("")) {
                                        grossWeightingrams_FromLoop = grossweight_FromLoop.replaceAll("[^\\d.]", "");

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                grossweightingrams_doubleFromLoop = 0;
                                try {
                                    if (!grossWeightingrams_FromLoop.equals("")) {
                                        grossweightingrams_doubleFromLoop = Double.parseDouble(grossWeightingrams_FromLoop);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    if (quantityFromLoop.equals("")) {
                                        quantityFromLoop = "1";
                                    }
                                    quantityDoubleFromLoop = Double.parseDouble(quantityFromLoop);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                double grossWeightWithQuantity_doubleFromLoop = 0;
                                try {
                                    grossWeightWithQuantity_doubleFromLoop = grossweightingrams_doubleFromLoop * quantityDoubleFromLoop;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_doubleFromLoop;

                                StockBalanceChangedForThisItemList.add(ItemUniquecodeFromLoop);


                                isStockOutGoingAlreadyCalledForthisItem = true;


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                    if (inventoryDetails_firstItem.equals("nil")) {
                        if (priceTypeOfItem.toUpperCase().equals(Constants.TMCPRICEPERKG)) {


                            if (isStockOutGoingAlreadyCalledForthisItem) {
                                //  try {
                                // totalgrossFromInsideAndOutsideLoop = grossWeightWithQuantity_double + totalgrossweightingrams_doubleFromLoop;
                                // } catch (Exception e) {
                                //       e.printStackTrace();
                                //  }


                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, totalgrossweightingrams_doubleFromLoop, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                            } else {
                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                            }

                        } else {
                            getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                        }


                    }
                    else {
                        try {

                            JSONArray jsonArray = new JSONArray(String.valueOf(inventoryDetails_firstItem));
                            int jsonArrayIterator = 0;
                            int jsonArrayCount = jsonArray.length();
                            for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {

                                try {
                                    String menuItemKeyFromInventoryDetails;
                                    double grossweightinGramsFromInventoryDetails, netweightingramsFromInventoryDetails;
                                    JSONObject json_InventoryDetails = jsonArray.getJSONObject(jsonArrayIterator);
                                    menuItemKeyFromInventoryDetails = "";
                                    grossweightinGramsFromInventoryDetails = 0;
                                    netweightingramsFromInventoryDetails = 0;

                                    try {
                                        menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");
                                    } catch (Exception e) {
                                        menuItemKeyFromInventoryDetails = "";
                                        e.printStackTrace();
                                    }






                                    try {
                                        grossweightinGramsFromInventoryDetails = Double.parseDouble(json_InventoryDetails.getString("grossweightingrams"));
                                    } catch (Exception e) {
                                        grossweightinGramsFromInventoryDetails = 0;
                                        e.printStackTrace();
                                    }


                                    try {
                                        netweightingramsFromInventoryDetails = Double.parseDouble(json_InventoryDetails.getString("netweightingrams"));
                                    } catch (Exception e) {
                                        netweightingramsFromInventoryDetails = 0;
                                        e.printStackTrace();
                                    }


                                    for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItem.size(); iterator_menuitemStockAvlDetails++) {

                                        Modal_MenuItem_Settings modal_menuItemStockAvlDetails = MenuItem.get(iterator_menuitemStockAvlDetails);

                                        String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getKey());

                                        if (menuItemKeyFromInventoryDetails.equals(menuItemKeyFromMenuAvlDetails)) {

                                            ////////for same inventoryDetails Item - starting

                                            for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                                                String hashmapkey = "";
                                                hashmapkey = cart_Item_List.get(iterator);
                                                String itemUniquecodeFromLoop = "", inventoryDetails_secondItem = "", priceTypeOfPOS = "";
                                                String menuItemKeyFromInventoryDetails_secondItem ="";

                                                Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                                                try {
                                                    itemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode().toString();

                                                } catch (Exception e) {
                                                    itemUniquecodeFromLoop = "";
                                                    e.printStackTrace();
                                                }


                                                if (!itemUniquecodeFromLoop.equals(ItemUniquecodeofItem)) {
                                                    try {
                                                        inventoryDetails_secondItem = modal_newOrderItems_insideLoop.getInventorydetails().toString();
                                                    } catch (Exception e) {
                                                        inventoryDetails_secondItem = "nil";
                                                        e.printStackTrace();
                                                    }
                                                    if (!inventoryDetails_secondItem.equals("nil")) {


                                                        try {
                                                            JSONArray jsonArray_secondItem = new JSONArray(String.valueOf(inventoryDetails_secondItem));
                                                            int jsonArrayIterator_secondItem = 0;
                                                            int jsonArrayCount_secondItem = jsonArray_secondItem.length();
                                                            String grossweightinGramsFromInventoryDetails_secondItem, netweightingramsFromInventoryDetails_secondItem;

                                                            for (; jsonArrayIterator_secondItem < (jsonArrayCount_secondItem); jsonArrayIterator_secondItem++) {
                                                                grossweightinGramsFromInventoryDetails_secondItem="0"; netweightingramsFromInventoryDetails_secondItem="0";

                                                                try {
                                                                    JSONObject json_InventoryDetails_secondItem = jsonArray_secondItem.getJSONObject(jsonArrayIterator_secondItem);
                                                                    menuItemKeyFromInventoryDetails_secondItem = "";
                                                                    //grossweightinGramsFromInventoryDetails = 0;
                                                                    netweightingramsFromInventoryDetails = 0;

                                                                    try {
                                                                        menuItemKeyFromInventoryDetails_secondItem = json_InventoryDetails_secondItem.getString("menuitemkey");
                                                                    } catch (Exception e) {
                                                                        menuItemKeyFromInventoryDetails_secondItem = "";
                                                                        e.printStackTrace();
                                                                    }


                                                                    try {
                                                                        if(json_InventoryDetails_secondItem.has("grossweightingrams")) {
                                                                            grossweightinGramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("grossweightingrams"));
                                                                        }
                                                                        else{

                                                                            grossweightinGramsFromInventoryDetails_secondItem = "0";

                                                                        }
                                                                    } catch (Exception e) {
                                                                        grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                        e.printStackTrace();
                                                                    }


                                                                    try {
                                                                        netweightingramsFromInventoryDetails_secondItem =String.valueOf(json_InventoryDetails_secondItem.getString("netweightingrams"));
                                                                    } catch (Exception e) {
                                                                        netweightingramsFromInventoryDetails_secondItem = "0";
                                                                        e.printStackTrace();
                                                                    }


                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                if(menuItemKeyFromInventoryDetails_secondItem.equals(menuItemKeyFromMenuAvlDetails)) {
                                                                    try {
                                                                        priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                                    } catch (Exception e) {
                                                                        priceTypeOfPOS = "";
                                                                        e.printStackTrace();
                                                                    }

                                                                    try {
                                                                        if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {

                                                                            String grossweight_secondItemInventoryDetails = "";

                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            try{
                                                                                grossweight_secondItemInventoryDetails = grossweightinGramsFromInventoryDetails_secondItem;

                                                                            } catch (Exception e){
                                                                                e.printStackTrace();
                                                                                grossweight_secondItemInventoryDetails = "0";
                                                                            }

                                                                            try{
                                                                                if(grossweight_secondItemInventoryDetails.equals("") || (grossweight_secondItemInventoryDetails.equals("0"))){
                                                                                    try{
                                                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight().toString();

                                                                                    } catch (Exception e){
                                                                                        e.printStackTrace();
                                                                                        grossweight_secondItemInventoryDetails = "1";
                                                                                    }
                                                                                }
                                                                            }
                                                                            catch (Exception e){
                                                                                grossweight_secondItemInventoryDetails = "1";

                                                                                e.printStackTrace();
                                                                            }



                                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                                            try {
                                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                            try {
                                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            StockBalanceChangedForThisItemList.add(itemUniquecodeFromLoop);


                                                                            isStockOutGoingAlreadyCalledForthisItem = true;


                                                                        }
                                                                        else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                                            String grossweight_secondItemInventoryDetails = "";

                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                                            } else {
                                                                                grossweight_secondItemInventoryDetails = "";
                                                                            }


                                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                                            try {
                                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                            try {
                                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            StockBalanceChangedForThisItemList.add(itemUniquecodeFromLoop);


                                                                            isStockOutGoingAlreadyCalledForthisItem = true;
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                }
                                            }
                                            ////////for same inventoryDetails Item - Ending


                                            String stockIncomingKey_avlDetail = "", Key_avlDetail = "", receivedStock_avlDetail = "", grossWeight_avlDetail_InventoryDetails = "", itemName_avlDetail_inventoryDetails = "", barcode_avlDetail = "", priceTypeForPOS_avlDetail = "",
                                                    tmcSubCtgy_avlDetail = "", tmcCtgy_avlDetail = "";
                                            double grossWeightWithQuantityDouble_avlDetail_InventoryDetails = 0, grossWeightDouble_avlDetail_InventoryDetails = 0;

                                            try {
                                                stockIncomingKey_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getStockincomingkey_AvlDetails());
                                            } catch (Exception e) {
                                                stockIncomingKey_avlDetail = "nil";
                                                e.printStackTrace();
                                            }

                                            boolean itemAvailability_avlDetail=true;

                                            try {
                                                itemAvailability_avlDetail = Boolean.parseBoolean(String.valueOf(modal_menuItemStockAvlDetails.getItemavailability_AvlDetails()));
                                            } catch (Exception e) {
                                                itemAvailability_avlDetail = true;
                                                e.printStackTrace();
                                            }

                                            try {
                                                Key_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getKey_AvlDetails());

                                            } catch (Exception e) {
                                                Key_avlDetail = "";
                                                e.printStackTrace();
                                            }

                                            try {
                                                barcode_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getBarcode_AvlDetails());

                                            } catch (Exception e) {
                                                barcode_avlDetail = "";
                                                e.printStackTrace();
                                            }

                                            try {
                                                receivedStock_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getReceivedstock_AvlDetails());

                                            } catch (Exception e) {
                                                receivedStock_avlDetail = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                itemName_avlDetail_inventoryDetails = String.valueOf(modal_menuItemStockAvlDetails.getItemname());

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            try {
                                                tmcSubCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcsubctgykey());

                                            } catch (Exception e) {
                                                tmcSubCtgy_avlDetail = "";
                                                e.printStackTrace();
                                            }


                                            try {
                                                tmcCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcctgykey());

                                            } catch (Exception e) {
                                                tmcCtgy_avlDetail = "";
                                                e.printStackTrace();
                                            }


                                            try {
                                                priceTypeForPOS_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getPricetypeforpos());

                                            } catch (Exception e) {
                                                priceTypeForPOS_avlDetail = "";
                                                e.printStackTrace();
                                            }


                                            if (priceTypeOfItem.toUpperCase().equals(Constants.TMCPRICEPERKG)) {


                                                if (isStockOutGoingAlreadyCalledForthisItem) {
                                                    //  try {
                                                    //    totalgrossFromInsideAndOutsideLoop = grossWeightWithQuantity_double + totalgrossweightingrams_doubleFromLoop;
                                                    // } catch (Exception e) {
                                                    //      e.printStackTrace();
                                                    //  }


                                                    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, totalgrossweightingrams_doubleFromLoop, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);


                                                } else {
                                                    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantity_double, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);

                                                }

                                            } else {
                                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossweightinGramsFromInventoryDetails, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);

                                            }

                                            //  getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantity_double, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, isitemAvailable, allowNegativeStock);


                                        }


                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }


            }


             */


            //////////////////////
            if(isinventorycheck) {


                String inventoryDetails_firstItem = "";
                try {
                    if ((modal_newOrderItems.getInventorydetails() != null) && (!modal_newOrderItems.getInventorydetails().equals("null")) && (!modal_newOrderItems.getInventorydetails().equals(""))) {
                        inventoryDetails_firstItem = String.valueOf(modal_newOrderItems.getInventorydetails());
                    } else {
                        inventoryDetails_firstItem = "nil";
                    }
                } catch (Exception e) {
                    inventoryDetails_firstItem = "nil";

                    e.printStackTrace();
                }


                boolean allowNegativeStock = false;
                if ((modal_newOrderItems.getAllownegativestock() != null) && (!modal_newOrderItems.getAllownegativestock().equals("null")) && (!modal_newOrderItems.getAllownegativestock().equals(""))) {
                    allowNegativeStock = Boolean.parseBoolean(modal_newOrderItems.getAllownegativestock().toUpperCase());
                } else {
                    allowNegativeStock = false;
                }


                boolean isitemAvailable = false;
                if ((modal_newOrderItems.getItemavailability_AvlDetails() != null) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals("null")) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals(""))) {
                    isitemAvailable = Boolean.parseBoolean(modal_newOrderItems.getItemavailability_AvlDetails().toUpperCase());
                } else {
                    isitemAvailable = false;
                }



                String BarcodeofItem = "";
                if ((modal_newOrderItems.getBarcode() != null) && (!modal_newOrderItems.getBarcode().equals("null")) && (!modal_newOrderItems.getBarcode().equals(""))) {
                    BarcodeofItem = String.valueOf(modal_newOrderItems.getBarcode());
                } else {
                    BarcodeofItem = "";
                }

                String priceTypeOfItem = "";
                if ((modal_newOrderItems.getPricetypeforpos() != null) && (!modal_newOrderItems.getPricetypeforpos().equals("null")) && (!modal_newOrderItems.getPricetypeforpos().equals(""))) {
                    priceTypeOfItem = String.valueOf(modal_newOrderItems.getPricetypeforpos());
                } else {
                    priceTypeOfItem = "";
                }






                if (!StockBalanceChangedForThisItemList.contains(menuItemKey)) {
                    totalgrossweightingrams_doubleFromLoop = 0;
                    totalgrossFromInsideAndOutsideLoop = 0;
                    isStockOutGoingAlreadyCalledForthisItem = false;

                    try {
                        //  ItemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode();
                        //  BarcodeFromLoop = modal_newOrderItems_insideLoop.getBarcode().toString();
                        //  priceTypeofItemFromLoop = modal_newOrderItems_insideLoop.getPricetypeforpos().toString();
                        //   if (!BarcodeFromLoop.equals(BarcodeofItem)) {
                        if (inventoryDetails_firstItem.equals("nil")) {


                            ////////for same inventoryDetails Item - starting

                            for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                                String hashmapkey = "";
                                hashmapkey = cart_Item_List.get(iterator);
                                String itemUniquecodeFromLoop = "", inventoryDetails_secondItem = "", priceTypeOfPOS = "";
                                String menuItemKeyFromInventoryDetails_secondItem = "",menuItemKeyFrom_secondItem="";

                                Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                                try {
                                    itemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode().toString();

                                } catch (Exception e) {
                                    itemUniquecodeFromLoop = "";
                                    e.printStackTrace();
                                }
                                try {
                                    menuItemKeyFrom_secondItem = modal_newOrderItems_insideLoop.getMenuItemId().toString();

                                } catch (Exception e) {
                                    menuItemKeyFrom_secondItem = "";
                                    e.printStackTrace();
                                }

                                try {
                                    inventoryDetails_secondItem = modal_newOrderItems_insideLoop.getInventorydetails().toString();

                                } catch (Exception e) {
                                    inventoryDetails_secondItem = "";
                                    e.printStackTrace();
                                }

                                try {

                                    if (inventoryDetails_secondItem.equals("nil")) {


                                        if (menuItemKeyFrom_secondItem.equals(menuItemKey)) {

                                            try {
                                                priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                            } catch (Exception e) {
                                                priceTypeOfPOS = "";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {


                                                    String quantity_secondItemInventoryDetails = "";
                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                    } else {
                                                        quantity_secondItemInventoryDetails = "";
                                                    }






                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                    try {
                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                            quantity_secondItemInventoryDetails = "1";
                                                        }
                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }




                                                    try {
                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + quantityDouble_secondItemInventoryDetails;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    //     StockBalanceChangedForThisItemList.add(menuItemKey);


                                                    //  isStockOutGoingAlreadyCalledForthisItem = true;


                                                }
                                                else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                    String grossweight_secondItemInventoryDetails = "";

                                                    String quantity_secondItemInventoryDetails = "";
                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                    } else {
                                                        quantity_secondItemInventoryDetails = "";
                                                    }


                                                    if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                    } else {
                                                        grossweight_secondItemInventoryDetails = "";
                                                    }


                                                    String grossWeightingrams_secondItemInventoryDetails = "";
                                                    try {
                                                        if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                            grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                    try {
                                                        if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                            grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                    try {
                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                            quantity_secondItemInventoryDetails = "1";
                                                        }
                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                    try {
                                                        grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    //  StockBalanceChangedForThisItemList.add(menuItemKey);


                                                    //isStockOutGoingAlreadyCalledForthisItem = true;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }



                                    }
                                    else {

                                        try{
                                            JSONArray jsonArray_secondItem = new JSONArray(String.valueOf(inventoryDetails_secondItem));
                                            int jsonArrayIterator_secondItem = 0;
                                            int jsonArrayCount_secondItem = jsonArray_secondItem.length();
                                            String grossweightinGramsFromInventoryDetails_secondItem, netweightingramsFromInventoryDetails_secondItem,menuitemKeyFromInventoryDetails_secondItem;

                                            for (; jsonArrayIterator_secondItem < (jsonArrayCount_secondItem); jsonArrayIterator_secondItem++) {
                                                grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                netweightingramsFromInventoryDetails_secondItem = "0";
                                                try {
                                                    JSONObject json_InventoryDetails_secondItem = jsonArray_secondItem.getJSONObject(jsonArrayIterator_secondItem);
                                                    menuItemKeyFromInventoryDetails_secondItem = "";
                                                    // grossweightinGramsFromInventoryDetails = 0;
                                                    //netweightingramsFromInventoryDetails = 0;

                                                    try {
                                                        menuItemKeyFromInventoryDetails_secondItem = json_InventoryDetails_secondItem.getString("menuitemkey");
                                                    } catch (Exception e) {
                                                        menuItemKeyFromInventoryDetails_secondItem = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        if (json_InventoryDetails_secondItem.has("grossweightingrams")) {
                                                            grossweightinGramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("grossweightingrams"));
                                                        } else {

                                                            grossweightinGramsFromInventoryDetails_secondItem = "0";

                                                        }
                                                    } catch (Exception e) {
                                                        grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        netweightingramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("netweightingrams"));
                                                    } catch (Exception e) {
                                                        netweightingramsFromInventoryDetails_secondItem = "0";
                                                        e.printStackTrace();
                                                    }


                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }



                                                if (menuItemKeyFromInventoryDetails_secondItem.equals(menuItemKey)) {

                                                    try {
                                                        priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                    } catch (Exception e) {
                                                        priceTypeOfPOS = "";
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {

                                                            String grossweight_secondItemInventoryDetails = "";

                                                            String quantity_secondItemInventoryDetails = "";
                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                            } else {
                                                                quantity_secondItemInventoryDetails = "";
                                                            }
                                                            System.out.println(String.valueOf(quantity_secondItemInventoryDetails)+" resaa quantity_secondItemInventoryDetails  " );


                                                            try {
                                                                grossweight_secondItemInventoryDetails = grossweightinGramsFromInventoryDetails_secondItem;

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                grossweight_secondItemInventoryDetails = "0";
                                                            }

                                                            try {
                                                                if (grossweight_secondItemInventoryDetails.equals("") || (grossweight_secondItemInventoryDetails.equals("0"))) {
                                                                    try {
                                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight().toString();

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                        grossweight_secondItemInventoryDetails = "1";
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                grossweight_secondItemInventoryDetails = "1";

                                                                e.printStackTrace();
                                                            }


                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                            try {
                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                    quantity_secondItemInventoryDetails = "1";
                                                                }
                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                            try {
                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            System.out.println(String.valueOf(grossWeightWithQuantity_double_secondItemInventoryDetails)+" resaa grossWeightWithQuantity_double_secondItemInventoryDetails  ");
                                                            System.out.println(String.valueOf(quantityDouble_secondItemInventoryDetails)+" resaa quantityDouble_secondItemInventoryDetails " );

                                                            try {
                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            System.out.println(String.valueOf(totalgrossweightingrams_doubleFromLoop)+" resaa totalgrossweightingrams_doubleFromLoop  " );


                                                            //   StockBalanceChangedForThisItemList.add(menuItemKey);


                                                            //    isStockOutGoingAlreadyCalledForthisItem = true;


                                                        } else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                            String grossweight_secondItemInventoryDetails = "";

                                                            String quantity_secondItemInventoryDetails = "";
                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                            } else {
                                                                quantity_secondItemInventoryDetails = "";
                                                            }


                                                            if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                            } else {
                                                                grossweight_secondItemInventoryDetails = "";
                                                            }


                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                            try {
                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                    quantity_secondItemInventoryDetails = "1";
                                                                }
                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                            try {
                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }


                                                            try {
                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            //StockBalanceChangedForThisItemList.add(menuItemKey);


                                                            //  isStockOutGoingAlreadyCalledForthisItem = true;
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }



                                            }


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            ////////for same inventoryDetails Item - Ending

                            StockBalanceChangedForThisItemList.add(menuItemKey);


                            isStockOutGoingAlreadyCalledForthisItem = true;

                            if (isStockOutGoingAlreadyCalledForthisItem) {
                                //  try {
                                // totalgrossFromInsideAndOutsideLoop = grossWeightWithQuantity_double + totalgrossweightingrams_doubleFromLoop;
                                // } catch (Exception e) {
                                //       e.printStackTrace();
                                //  }
                                Log.i(TAG, "Before getStock incoming name 1 " + itemName);
                                Log.i(TAG, "Before getStock incoming barcode 1 " + barcode);


                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, totalgrossweightingrams_doubleFromLoop, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                            } else {
                                Log.i(TAG, "Before getStock incoming name 2 " + itemName);
                                Log.i(TAG, "Before getStock incoming barcode 2 " + barcode);

                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                            }




                        }
                        else{
                            try{
                                JSONArray jsonArray = new JSONArray(String.valueOf(inventoryDetails_firstItem));
                                int jsonArrayIterator = 0;
                                int jsonArrayCount = jsonArray.length();
                                for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {
                                    try {
                                        String menuItemKeyFromInventoryDetails = "";
                                        double grossweightinGramsFromInventoryDetails = 0, netweightingramsFromInventoryDetails = 0;
                                        JSONObject json_InventoryDetails = jsonArray.getJSONObject(jsonArrayIterator);

                                        try {
                                            menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");
                                        } catch (Exception e) {
                                            menuItemKeyFromInventoryDetails = "";
                                            e.printStackTrace();
                                        }


                                        try {
                                            grossweightinGramsFromInventoryDetails = Double.parseDouble(json_InventoryDetails.getString("grossweightingrams"));
                                        } catch (Exception e) {
                                            grossweightinGramsFromInventoryDetails = 0;
                                            // e.printStackTrace();
                                        }


                                        try {
                                            netweightingramsFromInventoryDetails = Double.parseDouble(json_InventoryDetails.getString("netweightingrams"));
                                        } catch (Exception e) {
                                            netweightingramsFromInventoryDetails = 0;
                                            // e.printStackTrace();
                                        }
                                        if (!StockBalanceChangedForThisItemList.contains(menuItemKeyFromInventoryDetails)) {
                                            totalgrossweightingrams_doubleFromLoop = 0;
                                            ////////for same inventoryDetails Item - starting
                                            for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItem.size(); iterator_menuitemStockAvlDetails++) {

                                                Modal_MenuItem_Settings modal_menuItemStockAvlDetails = MenuItem.get(iterator_menuitemStockAvlDetails);

                                                String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getKey());
                                                String itemUniquecodeFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getItemuniquecode());
                                                String priceTypeForPOSFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getPricetypeforpos());
                                                String stockIncomingKey_avlDetail = "", Key_avlDetail = "", receivedStock_avlDetail = "", grossWeight_avlDetail_InventoryDetails = "", itemName_avlDetail_inventoryDetails = "", barcode_avlDetail = "", priceTypeForPOS_avlDetail = "",
                                                        tmcSubCtgy_avlDetail = "", tmcCtgy_avlDetail = "";

                                                if (menuItemKeyFromInventoryDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                                    double grossWeightWithQuantityDouble_avlDetail_InventoryDetails = 0, grossWeightDouble_avlDetail_InventoryDetails = 0;

                                                    try {
                                                        stockIncomingKey_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getStockincomingkey_AvlDetails());
                                                    } catch (Exception e) {
                                                        stockIncomingKey_avlDetail = "nil";
                                                        e.printStackTrace();
                                                    }
                                                    boolean itemAvailability_avlDetail = true;

                                                    try {
                                                        itemAvailability_avlDetail = Boolean.parseBoolean(String.valueOf(modal_menuItemStockAvlDetails.getItemavailability_AvlDetails()));
                                                    } catch (Exception e) {
                                                        itemAvailability_avlDetail = true;
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        Key_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getKey_AvlDetails());

                                                    } catch (Exception e) {
                                                        Key_avlDetail = "";
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        barcode_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getBarcode_AvlDetails());

                                                    } catch (Exception e) {
                                                        barcode_avlDetail = "";
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        receivedStock_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getReceivedstock_AvlDetails());

                                                    } catch (Exception e) {
                                                        receivedStock_avlDetail = "";

                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        itemName_avlDetail_inventoryDetails = String.valueOf(modal_menuItemStockAvlDetails.getItemname());

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        tmcSubCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcsubctgykey());

                                                    } catch (Exception e) {
                                                        tmcSubCtgy_avlDetail = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        tmcCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcctgykey());

                                                    } catch (Exception e) {
                                                        tmcCtgy_avlDetail = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        priceTypeForPOS_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getPricetypeforpos());

                                                    } catch (Exception e) {
                                                        priceTypeForPOS_avlDetail = "";
                                                        e.printStackTrace();
                                                    }
                                                    for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                                                        String hashmapkey = "";
                                                        hashmapkey = cart_Item_List.get(iterator);
                                                        String itemUniquecodeFromLoop = "", inventoryDetails_secondItem = "", priceTypeOfPOS = "";
                                                        String menuItemKeyFromInventoryDetails_secondItem = "", menuItemKeyFrom_secondItem = "";

                                                        Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                                                        try {
                                                            itemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode().toString();

                                                        } catch (Exception e) {
                                                            itemUniquecodeFromLoop = "";
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            menuItemKeyFrom_secondItem = modal_newOrderItems_insideLoop.getMenuItemId().toString();

                                                        } catch (Exception e) {
                                                            menuItemKeyFrom_secondItem = "";
                                                            e.printStackTrace();
                                                        }

                                                        try {
                                                            inventoryDetails_secondItem = modal_newOrderItems_insideLoop.getInventorydetails().toString();

                                                        } catch (Exception e) {
                                                            inventoryDetails_secondItem = "";
                                                            e.printStackTrace();
                                                        }

                                                        try {

                                                            if (inventoryDetails_secondItem.equals("nil")) {


                                                                if (menuItemKeyFrom_secondItem.equals(menuItemKeyFromInventoryDetails)) {

                                                                    try {
                                                                        priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                                    } catch (Exception e) {
                                                                        priceTypeOfPOS = "";
                                                                        e.printStackTrace();
                                                                    }

                                                                    try {
                                                                        if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {


                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            //  StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                            // isStockOutGoingAlreadyCalledForthisItem = true;


                                                                        }
                                                                        else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                                            String grossweight_secondItemInventoryDetails = "";

                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                                            } else {
                                                                                grossweight_secondItemInventoryDetails = "";
                                                                            }


                                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                                            try {
                                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                            try {
                                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            //   StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                            // isStockOutGoingAlreadyCalledForthisItem = true;
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }


                                                            }
                                                            else {

                                                                try {
                                                                    JSONArray jsonArray_secondItem = new JSONArray(String.valueOf(inventoryDetails_secondItem));
                                                                    int jsonArrayIterator_secondItem = 0;
                                                                    int jsonArrayCount_secondItem = jsonArray_secondItem.length();
                                                                    String grossweightinGramsFromInventoryDetails_secondItem, netweightingramsFromInventoryDetails_secondItem, menuitemKeyFromInventoryDetails_secondItem;

                                                                    for (; jsonArrayIterator_secondItem < (jsonArrayCount_secondItem); jsonArrayIterator_secondItem++) {
                                                                        grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                        netweightingramsFromInventoryDetails_secondItem = "0";
                                                                        try {
                                                                            JSONObject json_InventoryDetails_secondItem = jsonArray_secondItem.getJSONObject(jsonArrayIterator_secondItem);
                                                                            menuItemKeyFromInventoryDetails_secondItem = "";
                                                                            // grossweightinGramsFromInventoryDetails = 0;
                                                                            netweightingramsFromInventoryDetails = 0;

                                                                            try {
                                                                                menuItemKeyFromInventoryDetails_secondItem = json_InventoryDetails_secondItem.getString("menuitemkey");
                                                                            } catch (Exception e) {
                                                                                menuItemKeyFromInventoryDetails_secondItem = "";
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                if (json_InventoryDetails_secondItem.has("grossweightingrams")) {
                                                                                    grossweightinGramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("grossweightingrams"));
                                                                                } else {

                                                                                    grossweightinGramsFromInventoryDetails_secondItem = "0";

                                                                                }
                                                                            } catch (Exception e) {
                                                                                grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                netweightingramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("netweightingrams"));
                                                                            } catch (Exception e) {
                                                                                netweightingramsFromInventoryDetails_secondItem = "0";
                                                                                //  e.printStackTrace();
                                                                            }


                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }


                                                                        if (menuItemKeyFromInventoryDetails_secondItem.equals(menuItemKeyFromInventoryDetails)) {

                                                                            try {
                                                                                priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                                            } catch (Exception e) {
                                                                                priceTypeOfPOS = "";
                                                                                e.printStackTrace();
                                                                            }

                                                                            try {
                                                                                if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {

                                                                                    String grossweight_secondItemInventoryDetails = "";

                                                                                    String quantity_secondItemInventoryDetails = "";
                                                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                                    } else {
                                                                                        quantity_secondItemInventoryDetails = "";
                                                                                    }


                                                                                    try {
                                                                                        grossweight_secondItemInventoryDetails = grossweightinGramsFromInventoryDetails_secondItem;

                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                        grossweight_secondItemInventoryDetails = "0";
                                                                                    }

                                                                                    try {
                                                                                        if (grossweight_secondItemInventoryDetails.equals("") || (grossweight_secondItemInventoryDetails.equals("0"))) {
                                                                                            try {
                                                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight().toString();

                                                                                            } catch (Exception e) {
                                                                                                e.printStackTrace();
                                                                                                grossweight_secondItemInventoryDetails = "1";
                                                                                            }
                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        grossweight_secondItemInventoryDetails = "1";

                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    String grossWeightingrams_secondItemInventoryDetails = "";
                                                                                    try {
                                                                                        if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                            grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                            grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                            quantity_secondItemInventoryDetails = "1";
                                                                                        }
                                                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                                    try {
                                                                                        grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    try {
                                                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    //   StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                                    //  isStockOutGoingAlreadyCalledForthisItem = true;

                                                                                }
                                                                                else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                                                    String grossweight_secondItemInventoryDetails = "";

                                                                                    String quantity_secondItemInventoryDetails = "";
                                                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                                    } else {
                                                                                        quantity_secondItemInventoryDetails = "";
                                                                                    }


                                                                                    if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                                                    } else {
                                                                                        grossweight_secondItemInventoryDetails = "";
                                                                                    }


                                                                                    String grossWeightingrams_secondItemInventoryDetails = "";
                                                                                    try {
                                                                                        if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                            grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                            grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                            quantity_secondItemInventoryDetails = "1";
                                                                                        }
                                                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                                    try {
                                                                                        grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    try {
                                                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    // Toast.makeText(mContext, "totalgrossweightingrams_doubleFromLoop " + String.valueOf(totalgrossweightingrams_doubleFromLoop), Toast.LENGTH_SHORT).show();


                                                                                    //    StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                                    // isStockOutGoingAlreadyCalledForthisItem = true;
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                        }


                                                                    }


                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                    StockBalanceChangedForThisItemList.add(menuItemKeyFromInventoryDetails);


                                                    isStockOutGoingAlreadyCalledForthisItem = true;



                                                    //  if (priceTypeOfItem.toUpperCase().equals(Constants.TMCPRICEPERKG)) {


                                                    if (isStockOutGoingAlreadyCalledForthisItem) {
                                                        //  try {
                                                        //    totalgrossFromInsideAndOutsideLoop = grossWeightWithQuantity_double + totalgrossweightingrams_doubleFromLoop;
                                                        // } catch (Exception e) {
                                                        //      e.printStackTrace();
                                                        //  }

                                                        Log.i(TAG, "Before getStock incoming name 3 " + itemName);
                                                        Log.i(TAG, "Before getStock incoming barcode 3 " + barcode_avlDetail);

                                                        getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, totalgrossweightingrams_doubleFromLoop, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);


                                                    } else {
                                                        Log.i(TAG, "Before getStock incoming name 4 " + itemName);
                                                        Log.i(TAG, "Before getStock incoming barcode 4 " + barcode_avlDetail);

                                                        getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantity_double, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);

                                                    }

                                                            /*} else {
                                                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossweightinGramsFromInventoryDetails, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);

                                                            }

                                                             */




                                                }
                                            }

                                            ////////for same inventoryDetails Item - Ending





                                        }







                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                    //    }




                }



            }
            ///////////////////////



            ///////////////////////

            
            
            
            
            String price = "";
            if( modal_newOrderItems.getItemPrice_quantityBased()!=null){
                price =  modal_newOrderItems.getItemPrice_quantityBased();
            }
            else{
                price ="";
            }
            //  modal_newOrderItems.getItemPrice_quantityBased();
            String weight = "";
            if(modal_newOrderItems.getItemFinalWeight()!=null){
                weight = modal_newOrderItems.getItemFinalWeight();

            }
            else{
                weight = "";
            }
            String quantity1 = "";
            if( modal_newOrderItems.getQuantity()!=null){
                quantity1 =  modal_newOrderItems.getQuantity();;

            }
            else{
                quantity1 = "";
            }

            String GstAmount = "";
            if(modal_newOrderItems.getGstAmount()!=null){
                GstAmount = (modal_newOrderItems.getGstAmount());
            }
            else{
                GstAmount ="";
            }

            String menuItemId ="";
            if( modal_newOrderItems.getMenuItemId()!=null) {
                menuItemId =    modal_newOrderItems.getMenuItemId();
            }
            else{
                menuItemId ="";
            }
            String netweight ="";
            if( modal_newOrderItems.getNetweight()!=null){
                netweight = modal_newOrderItems.getNetweight();
            }
            else{
                netweight ="";
            }

            String portionsize = "";
            if(modal_newOrderItems.getPortionsize()!=null){
                portionsize = modal_newOrderItems.getPortionsize();
            }
            else{
                portionsize = "";
            }

            String grossweight2 ="";
            if(modal_newOrderItems.getGrossweight()!=null){
                grossweight2    = modal_newOrderItems.getGrossweight();

            }
            else{
                grossweight2 ="";
            }
            String subCtgyKey = "";
            if(modal_newOrderItems.getTmcsubctgykey()!=null){
                subCtgyKey =   modal_newOrderItems.getTmcsubctgykey();
            }
            else{
                subCtgyKey = "";
            }

            String grossWeightingrams2 = "";
            try {
                if (!grossweight2.equals("")) {
                    grossWeightingrams2 = grossweight2.replaceAll("[^\\d.]", "");

                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            double grossweightingrams_double2 =0;
            try{
                if(!grossWeightingrams2.equals("")) {
                    grossweightingrams_double2 = Double.parseDouble(grossWeightingrams2);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            PlaceOrder_in_OrderItemDetails(subCtgyKey,itemName,grossweight2, weight,netweight, quantity1, price, "", GstAmount, vendorkey, Currenttime, sTime, vendorkey, vendorName,grossWeightingrams2,grossweightingrams_double2,orderPlacedDate);


            JSONObject itemdespObject = new JSONObject();
            try {
                itemdespObject.put("menuitemid", menuItemId);
                itemdespObject.put("itemname", itemName);
                itemdespObject.put("tmcprice", Double.parseDouble(price));
                itemdespObject.put("quantity", Integer.parseInt(quantity1));
                itemdespObject.put("checkouturl", "");
                itemdespObject.put("gstamount", Double.parseDouble(GstAmount));
                itemdespObject.put("netweight", netweight);
                itemdespObject.put("portionsize", portionsize);
                itemdespObject.put("tmcsubctgykey", subCtgyKey);
                try {
                    if (weight.equals("") || weight == (null)) {
                        itemdespObject.put("grossweight", grossweight);

                        if(grossweight.equals("")){
                            itemdespObject.put("netweight", netweight);

                        }
                        else
                        {
                            itemdespObject.put("netweight", grossweight);
                            if(grossweightingrams_double!=0) {
                                itemdespObject.put("grossweightingrams", grossweightingrams_double);
                            }


                        }
                        itemdespObject.put("weightingrams", grossweight);

                    } else {
                        itemdespObject.put("grossweight", weight);
                        if(grossweight.equals("")){
                            itemdespObject.put("netweight", netweight);

                        }
                        else
                        {
                            itemdespObject.put("netweight", weight);
                            if(grossweightingrams_double!=0) {
                                itemdespObject.put("grossweightingrams", grossweightingrams_double);
                            }
                        }
                        itemdespObject.put("weightingrams", weight);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            itemDespArray.put(itemdespObject);


        }





        JSONObject jsonObject = new JSONObject();
        double  CouponDiscountAmount_double =0;
        try {


            jsonObject.put("coupondiscount", CouponDiscountAmount);

            jsonObject.put("deliveryamount", 0);
            jsonObject.put("couponkey", "");

            jsonObject.put("ordertype", ordertype);
            jsonObject.put("gstamount", Double.parseDouble(taxAmount));

            jsonObject.put("deliverytype", deliverytype);
            jsonObject.put("slotname", slotname);



            jsonObject.put("slottimerange", "");

            jsonObject.put("orderid", orderid);
            jsonObject.put("orderplacedtime", orderplacedTime);
            jsonObject.put("tokenno", (tokenno));
            jsonObject.put("userid", userid);

            if(orderdetailsnewschema) {
                jsonObject.put("usermobileno", UserMobile);
                jsonObject.put("slotdate", orderPlacedDate);

            }
            else{
                jsonObject.put("slotdate", "");

                jsonObject.put("usermobile", UserMobile);

            }
            jsonObject.put("vendorkey", vendorkey);
            jsonObject.put("vendorname", vendorName);
            jsonObject.put("payableamount", Double.parseDouble(payableAmount));

            jsonObject.put("taxamount", taxAmount);
            jsonObject.put("paymentmode", Payment_mode);
            jsonObject.put("itemdesp", itemDespArray);
            jsonObject.put("couponid", couponid);

            jsonObject.put("orderplaceddate", orderPlacedDate);

            jsonObject.put("merchantorderid", merchantorderid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);



        String Api_To_PlaceOrderInOrderDetails = "";
        if(orderdetailsnewschema){
            Api_To_PlaceOrderInOrderDetails = Constants.api_AddVendorOrderDetails;

        }
        else{
            Api_To_PlaceOrderInOrderDetails = Constants.api_addOrderDetailsInOrderDetailsTable;

        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_To_PlaceOrderInOrderDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        // StartTwice startTwice =new StartTwice(UserMobile,tokenno,itemTotalwithoutGst,taxAmount,payableAmount,orderid,cart_Item_List,cartItem_hashmap,Payment_mode);
                        // startTwice.main();


                        try {
                            if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                                PrintReciptForNewItemUsingUSBPrinter(UserMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, Payment_mode,"0",ordertype);

                            }
                            else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                                //printReciptUsingBluetoothPrinter(UserMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, Payment_mode,"0",ordertype);

                            }
                            else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                                int i = (PrinterFunctions.CheckStatus(portName,portSettings,1));
                                if(i != -1){
                                    printReciptUsingPOSMachinePrinter(UserMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, Payment_mode,"0",ordertype);

                                }
                                else{
                                    new TMCAlertDialogClass(AddBigBasketOrder.this, R.string.app_name, R.string.OrderPlaced_Printer_is_Disconnected,
                                            R.string.OK_Text,R.string.Empty_Text,
                                            new TMCAlertDialogClass.AlertListener() {
                                                @Override
                                                public void onYes() {
                                                    cart_Item_List.clear();
                                                    cartItem_hashmap.clear();
                                                    StockBalanceChangedForThisItemList.clear();

                                                    new_to_pay_Amount = 0;
                                                    old_taxes_and_charges_Amount = 0;
                                                    old_total_Amount = 0;
                                                    createEmptyRowInListView("empty");
                                                    CallAdapter();
                                                    //discountAmount = "0";

                                                    // discount_Edit_widget.setText("0");
                                                    finaltoPayAmount = "0";
                                                    //  discount_rs_text_widget.setText(discountAmount);
                                                    OrderTypefromSpinner = "POS Order";
                                                    //   orderTypeSpinner.setSelection(0);
                                                    total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                                    taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                                    total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



                                                    customermobileno_edittextwidget.setText("");
                                                    bigbasketOrdersCustomermobileno.setText("");
                                                    isPrintedSecondTime = false;
                                                    ispaymentMode_Clicked = false;
                                                    isOrderDetailsMethodCalled = false;

                                                    isPaymentDetailsMethodCalled = false;
                                                    isOrderTrackingDetailsMethodCalled = false;
                                                    totalAmounttopay=0;
                                                    finalamounttoPay=0;
                                                    showProgressBar(false);


                                                }

                                                @Override
                                                public void onNo() {

                                                }
                                            });


                                }

                            }
                            else {
                                new TMCAlertDialogClass(AddBigBasketOrder.this, R.string.app_name, R.string.OrderPlaced_Printer_is_Disconnected,
                                        R.string.OK_Text,R.string.Empty_Text,
                                        new TMCAlertDialogClass.AlertListener() {
                                            @Override
                                            public void onYes() {
                                                cart_Item_List.clear();
                                                cartItem_hashmap.clear();
                                                StockBalanceChangedForThisItemList.clear();

                                                new_to_pay_Amount = 0;
                                                old_taxes_and_charges_Amount = 0;
                                                old_total_Amount = 0;
                                                createEmptyRowInListView("empty");
                                                CallAdapter();
                                                //discountAmount = "0";

                                                // discount_Edit_widget.setText("0");
                                                finaltoPayAmount = "0";
                                                //  discount_rs_text_widget.setText(discountAmount);
                                                OrderTypefromSpinner = "POS Order";
                                                //   orderTypeSpinner.setSelection(0);
                                                total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                                taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                                total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



                                                customermobileno_edittextwidget.setText("");
                                                bigbasketOrdersCustomermobileno.setText("");
                                                isPrintedSecondTime = false;
                                                ispaymentMode_Clicked = false;
                                                isOrderDetailsMethodCalled = false;

                                                isPaymentDetailsMethodCalled = false;
                                                isOrderTrackingDetailsMethodCalled = false;
                                                totalAmounttopay=0;
                                                finalamounttoPay=0;
                                                showProgressBar(false);//Toast.makeText(AddBigBasketOrder.this,"Please Generate Token Number Again",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNo() {

                                            }
                                        });
                            }


                        }
                        catch(Exception e ){
                            e.printStackTrace();


                        }





                     
                    }
                    else{

                        isOrderDetailsMethodCalled = false;
                        showProgressBar(false);
                        Toast.makeText(getApplicationContext(),"OrderDetails is not updated in DB",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    showProgressBar(false);
                    isOrderDetailsMethodCalled = false;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                isOrderDetailsMethodCalled = false;

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
        // Make the request


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);






    }







    private void PrintReciptForNewItemUsingUSBPrinter(String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String payableAmount, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String finalCouponDiscountAmount, String ordertype) {

        modal_usbPrinter = new Modal_USBPrinter();
        try{

            modal_usbPrinter.orderplacedTime = Currenttime;
            modal_usbPrinter.userMobile = userMobile;
            modal_usbPrinter.tokenno = tokenno;
            modal_usbPrinter.itemTotalwithoutGst = itemTotalwithoutGst;
            modal_usbPrinter.taxAmount = taxAmount;
            modal_usbPrinter.payableAmount = payableAmount;
            modal_usbPrinter.orderid = orderid;
            modal_usbPrinter.cart_Item_List = cart_item_list;
            modal_usbPrinter.cartItem_hashmap = cartItem_hashmap;
            modal_usbPrinter.payment_mode = payment_mode;
            modal_usbPrinter.finalCouponDiscountAmount = finalCouponDiscountAmount;
            modal_usbPrinter.ordertype = ordertype;



        }
        catch (Exception e){
            e.printStackTrace();
        }
        UsbConnection usbConnection = UsbPrintersConnectionsLocal.selectFirstConnected(AddBigBasketOrder.this);
        UsbManager usbManager = (UsbManager) AddBigBasketOrder.this.getSystemService(Context.USB_SERVICE);

        if (usbConnection == null || usbManager == null) {
            showProgressBar(false);

          /*  new AlertDialog.Builder(AddBigBasketOrder.this)
                    .setTitle("USB Connection")
                    .setMessage("No USB printer found.")
                    .show();

           */

            new TMCAlertDialogClass(AddBigBasketOrder.this, R.string.app_name, R.string.ReConnect_Instruction,
                    R.string.OK_Text, R.string.Cancel_Text,
                    new TMCAlertDialogClass.AlertListener() {
                        @Override
                        public void onYes() {
                            PrintReciptForNewItemUsingUSBPrinter(userMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_item_list, cartItem_hashmap, payment_mode,finalCouponDiscountAmount,ordertype);

                            return;
                        }

                        @Override
                        public void onNo() {
                            AddBigBasketOrder.cart_Item_List.clear();
                            AddBigBasketOrder.cartItem_hashmap.clear();
                            cart_Item_List.clear();
                            cartItem_hashmap.clear();
                            StockBalanceChangedForThisItemList.clear();

                            new_to_pay_Amount = 0;
                            old_taxes_and_charges_Amount = 0;
                            old_total_Amount = 0;
                            createEmptyRowInListView("empty");
                            CallAdapter();
                            //discountAmount = "0";

                            // discount_Edit_widget.setText("0");
                            finaltoPayAmount = "0";
                            //  discount_rs_text_widget.setText(discountAmount);
                            OrderTypefromSpinner = "POS Order";
                            //   orderTypeSpinner.setSelection(0);
                            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



                            customermobileno_edittextwidget.setText("");
                            bigbasketOrdersCustomermobileno.setText("");
                            isPrintedSecondTime = false;
                            ispaymentMode_Clicked = false;
                            isOrderDetailsMethodCalled = false;

                            isPaymentDetailsMethodCalled = false;
                            isOrderTrackingDetailsMethodCalled = false;
                            totalAmounttopay=0;
                            finalamounttoPay=0;
                            showProgressBar(false);

                        }
                    });
            return;
        }

        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                AddBigBasketOrder.this,
                0,
                new Intent(ACTION_USB_PERMISSION),
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addCategory("Add BigBasket Orders");
        registerReceiver(usbReceiver_newItem, filter);
        usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);





    }



    private final BroadcastReceiver usbReceiver_newItem = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Set<String> category = intent.getCategories();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (AddBigBasketOrder.this) {
                    UsbManager usbManager = (UsbManager) AddBigBasketOrder.this.getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context, new AsyncEscPosPrint.OnPrintFinished() {
                                @Override
                                public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                    Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                    try{
                                        unregisterReceiver(usbReceiver_newItem);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }



                                    try{
                                        if(AsyncEscPosPrinter.getPrinterConnection().isConnected()){
                                            AsyncEscPosPrinter.getPrinterConnection().disconnect();
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    AddBigBasketOrder.cart_Item_List.clear();
                                    AddBigBasketOrder.cartItem_hashmap.clear();
                                    cart_Item_List.clear();
                                    cartItem_hashmap.clear();
                                    StockBalanceChangedForThisItemList.clear();

                                    new_to_pay_Amount = 0;
                                    old_taxes_and_charges_Amount = 0;
                                    old_total_Amount = 0;
                                    createEmptyRowInListView("empty");
                                    CallAdapter();
                                    //discountAmount = "0";

                                    // discount_Edit_widget.setText("0");
                                    finaltoPayAmount = "0";
                                    //  discount_rs_text_widget.setText(discountAmount);
                                    OrderTypefromSpinner = "POS Order";
                                    //   orderTypeSpinner.setSelection(0);
                                    total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                    taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                    total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



                                    customermobileno_edittextwidget.setText("");
                                    bigbasketOrdersCustomermobileno.setText("");
                                    isPrintedSecondTime = false;
                                    ispaymentMode_Clicked = false;
                                    isOrderDetailsMethodCalled = false;

                                    isPaymentDetailsMethodCalled = false;
                                    isOrderTrackingDetailsMethodCalled = false;
                                    totalAmounttopay=0;
                                    finalamounttoPay=0;
                                    showProgressBar(false);

                                }

                                @Override
                                public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                    Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");

                                    try{
                                        unregisterReceiver(usbReceiver_newItem);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }



                                    try{
                                        if(AsyncEscPosPrinter.getPrinterConnection().isConnected()){
                                            AsyncEscPosPrinter.getPrinterConnection().disconnect();
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    if (!isPrintedSecondTime) {
                                        //isPrintedSecondTime = true;
                                        //showProgressBar(true);

                                        HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap();
                                        List<String> cart_Item_List = new ArrayList<>();
                                        String orderplacedTime = "" ; String userMobile = "" ; String tokenno = "" ; String itemTotalwithoutGst = "" ; String taxAmount = "" ; String payableAmount = "" ; String orderid = "" ;  String payment_mode = "" ; String finalCouponDiscountAmount = "" ; String ordertype ="";
                                        try{
                                            cartItem_hashmap = modal_usbPrinter.getCartItem_hashmap();
                                            cart_Item_List = modal_usbPrinter.getCart_Item_List();
                                            orderplacedTime = modal_usbPrinter.getOrderplacedTime();
                                            userMobile = modal_usbPrinter.getUserMobile();
                                            tokenno = modal_usbPrinter.getTokenno();
                                            itemTotalwithoutGst = modal_usbPrinter.getItemTotalwithoutGst();
                                            taxAmount = modal_usbPrinter.getTaxAmount();
                                            payableAmount = modal_usbPrinter.getPayableAmount();
                                            orderid = modal_usbPrinter.getOrderid();
                                            payment_mode = modal_usbPrinter.getPayment_mode();
                                            finalCouponDiscountAmount = modal_usbPrinter.getFinalCouponDiscountAmount();
                                            ordertype = modal_usbPrinter.getOrdertype();


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        String finalOrderplacedTime = orderplacedTime;
                                        String finalUserMobile = userMobile;
                                        String finalTokenno = tokenno;
                                        String finalItemTotalwithoutGst = itemTotalwithoutGst;
                                        String finalTaxAmount = taxAmount;
                                        String finalPayableAmount = payableAmount;
                                        String finalOrderid = orderid;
                                        List<String> finalCart_Item_List = cart_Item_List;
                                        HashMap<String, Modal_NewOrderItems> finalCartItem_hashmap = cartItem_hashmap;
                                        String finalPayment_mode = payment_mode;
                                        String finalCouponDiscountAmount1 = finalCouponDiscountAmount;
                                        String finalOrdertype = ordertype;
                                        new TMCAlertDialogClass(AddBigBasketOrder.this, R.string.app_name, R.string.RePrint_Instruction,
                                                R.string.Yes_Text, R.string.No_Text,
                                                new TMCAlertDialogClass.AlertListener() {
                                                    @Override
                                                    public void onYes() {
                                                        isPrintedSecondTime = true;
                                                        PrintReciptForNewItemUsingUSBPrinter(finalUserMobile, finalTokenno, finalItemTotalwithoutGst, finalTaxAmount, finalPayableAmount, finalOrderid, finalCart_Item_List, finalCartItem_hashmap, finalPayment_mode,finalCouponDiscountAmount1,finalOrdertype);


                                                    }

                                                    @Override
                                                    public void onNo() {
                                                        AddBigBasketOrder.cart_Item_List.clear();
                                                        AddBigBasketOrder.cartItem_hashmap.clear();
                                                        finalCart_Item_List.clear();
                                                        finalCartItem_hashmap.clear();
                                                        StockBalanceChangedForThisItemList.clear();

                                                        new_to_pay_Amount = 0;
                                                        old_taxes_and_charges_Amount = 0;
                                                        old_total_Amount = 0;
                                                        createEmptyRowInListView("empty");
                                                        CallAdapter();
                                                        //discountAmount = "0";

                                                        // discount_Edit_widget.setText("0");
                                                        finaltoPayAmount = "0";
                                                        //  discount_rs_text_widget.setText(discountAmount);
                                                        OrderTypefromSpinner = "POS Order";
                                                        //   orderTypeSpinner.setSelection(0);
                                                        total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                                        taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                                        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



                                                        customermobileno_edittextwidget.setText("");
                                                        bigbasketOrdersCustomermobileno.setText("");
                                                        isPrintedSecondTime = false;
                                                        ispaymentMode_Clicked = false;
                                                        isOrderDetailsMethodCalled = false;

                                                        isPaymentDetailsMethodCalled = false;
                                                        isOrderTrackingDetailsMethodCalled = false;
                                                        totalAmounttopay=0;
                                                        finalamounttoPay=0;
                                                        showProgressBar(false);
                                                    }
                                                });


                                    }
                                    else {
                                        AddBigBasketOrder.cart_Item_List.clear();
                                        AddBigBasketOrder.cartItem_hashmap.clear();
                                        cart_Item_List.clear();
                                        cartItem_hashmap.clear();
                                        StockBalanceChangedForThisItemList.clear();

                                        new_to_pay_Amount = 0;
                                        old_taxes_and_charges_Amount = 0;
                                        old_total_Amount = 0;
                                        createEmptyRowInListView("empty");
                                        CallAdapter();
                                        //discountAmount = "0";

                                        // discount_Edit_widget.setText("0");
                                        finaltoPayAmount = "0";
                                        //  discount_rs_text_widget.setText(discountAmount);
                                        OrderTypefromSpinner = "POS Order";
                                        //   orderTypeSpinner.setSelection(0);
                                        total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                        taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



                                        customermobileno_edittextwidget.setText("");
                                        bigbasketOrdersCustomermobileno.setText("");
                                        isPrintedSecondTime = false;
                                        ispaymentMode_Clicked = false;
                                        isOrderDetailsMethodCalled = false;

                                        isPaymentDetailsMethodCalled = false;
                                        isOrderTrackingDetailsMethodCalled = false;
                                        totalAmounttopay=0;
                                        finalamounttoPay=0;
                                        showProgressBar(false);
                                    }

                                }
                            }
                            )
                                    .execute(getAsyncEscPosPrinterNewItem(new UsbConnection(usbManager, usbDevice)));
                        }
                    }
                }
            }
        }
    };



    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinterNewItem(DeviceConnection printerConnection) {


        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss", Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap();
        List<String> cart_Item_List = new ArrayList<>();
        String orderplacedTime = "";
        String userMobile = "";
        String tokenno = "";
        String itemTotalwithoutGst = "";
        String taxAmount = "";
        String payableAmount = "";
        String orderid = "";
        String payment_mode = "";
        String finalCouponDiscountAmount = "";
        String ordertype = "";
        try {
            cartItem_hashmap = modal_usbPrinter.getCartItem_hashmap();
            cart_Item_List = modal_usbPrinter.getCart_Item_List();
            orderplacedTime = modal_usbPrinter.getOrderplacedTime();
            userMobile = modal_usbPrinter.getUserMobile();
            tokenno = modal_usbPrinter.getTokenno();
            itemTotalwithoutGst = modal_usbPrinter.getItemTotalwithoutGst();
            taxAmount = modal_usbPrinter.getTaxAmount();
            payableAmount = modal_usbPrinter.getPayableAmount();
            orderid = modal_usbPrinter.getOrderid();
            payment_mode = modal_usbPrinter.getPayment_mode();
            finalCouponDiscountAmount = modal_usbPrinter.getFinalCouponDiscountAmount();
            ordertype = modal_usbPrinter.getOrdertype();


        } catch (Exception e) {
            e.printStackTrace();
        }

        String text_to_Print = "";
        String Title = "The Meat Chop";

        String GSTIN = "GSTIN :33AAJCC0055D1Z9";


        if((vendorKey.equals("vendor_4")) ||  (vendorKey.equals("wholesalesvendor_1"))) {


            text_to_Print = "[c]<b><font size='big'>MK Proteins</b>\n\n";
            text_to_Print = text_to_Print + "[c]<b><font size='normal'>Powered By The Meat Chop</b>\n\n";

        }
        else {
            text_to_Print = "[c]<b><font size='big'>The Meat Chop</b>\n\n";

        }
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Fresh Meat and Seafood \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine1 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine2 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Postal Code :" + StoreAddressLine3 + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Contact No :" + StoreLanLine + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + GSTIN + " \n"+ " \n";

        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + orderplacedTime + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'># " + orderid + " \n";
        text_to_Print = text_to_Print + "[L] ----------------------------------------------" + " \n";
        text_to_Print = text_to_Print + "[L]  ITEMNAME * QTY " + " \n";
        text_to_Print = text_to_Print + "[L]  RATE"+"[R]      "+"  SUBTOTAL" +" \n";

        //text_to_Print = text_to_Print + "[L] RATE                                  SUBTOTAL" + " \n";
        text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";


        if (cart_Item_List.size() == cartItem_hashmap.size()) {

            double oldSavedAmount = 0;
            String CouponDiscount = "0";
            String Gstt = " ", subtotal = " ", quantity = " ", price = " ", weight = " ";

            for (int i = 0; i < cart_Item_List.size(); i++) {
                String itemDetails = "";
                double savedAmount;
                String itemUniqueCode = cart_Item_List.get(i);
                Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);

                String itemName = "";
                try {
                    itemName = String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());
                }
                catch (Exception e) {
                    String itemUniqueCode1 = cart_Item_List.get(i);
                    Modal_NewOrderItems modal_newOrderItems1 = cartItem_hashmap.get(itemUniqueCode1);
                    try {
                        itemName = String.valueOf(Objects.requireNonNull(modal_newOrderItems1).getItemname());
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }

                int indexofbraces = itemName.indexOf("(");
                if (indexofbraces >= 0) {
                    itemName = itemName.substring(0, indexofbraces);

                }
                if (itemName.length() > 21) {
                    itemName = itemName.substring(0, 21);
                    itemName = itemName + "...";
                }
                try {
                    price = String.valueOf(modal_newOrderItems.getItemFinalPrice());
                    if (price.equals("null")) {
                        price = "  ";
                    }
                } catch (Exception e) {
                    price = "0";
                    e.printStackTrace();
                }

                try {
                    weight = modal_newOrderItems.getItemFinalWeight().toString();
                } catch (Exception e) {
                    weight = "0";
                    e.printStackTrace();
                }

                try {
                    Gstt = modal_newOrderItems.getGstAmount();

                } catch (Exception e) {
                    Gstt = "0";
                    e.printStackTrace();
                }


                try {
                    savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                } catch (Exception e) {
                    savedAmount = 0;
                    e.printStackTrace();
                }


                try {
                    oldSavedAmount = savedAmount + oldSavedAmount;
                } catch (Exception e) {
                    weight = "0";
                    e.printStackTrace();
                }


                try {
                    subtotal = modal_newOrderItems.getSubTotal_perItem();
                } catch (Exception e) {
                    subtotal = "0";
                    e.printStackTrace();
                }

                try {
                    quantity = modal_newOrderItems.getQuantity();
                } catch (Exception e) {
                    quantity = "0";
                    e.printStackTrace();
                }


                try {
                    itemName = itemName + "  *  " + weight + " ( " + quantity + " ) ";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                text_to_Print = text_to_Print + "[L]  <b><font size='normal'>" + itemName + " </b>\n";

/*
                if (price.length() == 4) {
                    //14spaces
                    price = price + "                ";
                }
                if (price.length() == 5) {
                    //13spaces
                    price = price + "               ";
                }
                if (price.length() == 6) {
                    //12spaces
                    price = price + "              ";
                }
                if (price.length() == 7) {
                    //11spaces
                    price = price + "             ";
                }
                if (price.length() == 8) {
                    //10spaces
                    price = price + "            ";
                }
                if (price.length() == 9) {
                    //9spaces
                    price = price + "           ";
                }
                if (price.length() == 10) {
                    //8spaces
                    price = price + "          ";
                }
                if (price.length() == 11) {
                    //7spaces
                    price = price + "         ";
                }
                if (price.length() == 12) {
                    //6spaces
                    price = price + "        ";
                }
                if (price.length() == 13) {
                    //5spaces
                    price = price + "       ";
                }
                if (price.length() == 14) {
                    //4spaces
                    price = price + "      ";
                }
                if (price.length() == 15) {
                    //3spaces
                    price = price + "     ";
                }
                if (price.length() == 16) {
                    //2spaces
                    price = price + "    ";
                }
                if (price.length() == 17) {
                    //1spaces
                    price = price + "   ";
                }
                if (price.length() == 18) {
                    //1spaces
                    price = price + " ";
                }


                if (Gstt.length() == 7) {
                    //1spaces
                    Gstt = Gstt + " ";
                }
                if (Gstt.length() == 8) {
                    //0space
                    Gstt = Gstt + "";
                }
                if (Gstt.length() == 9) {
                    //no space
                    Gstt = Gstt;
                }
                if (subtotal.length() == 4) {
                    //5spaces
                    subtotal = "       " + subtotal;
                }
                if (subtotal.length() == 5) {
                    //6spaces
                    subtotal = "       " + subtotal;
                }
                if (subtotal.length() == 6) {
                    //8spaces
                    subtotal = "         " + subtotal;
                }
                if (subtotal.length() == 7) {
                    //7spaces
                    subtotal = "        " + subtotal;
                }
                if (subtotal.length() == 8) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 9) {
                    //5spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 10) {
                    //4spaces
                    subtotal = "     " + subtotal;
                }
                if (subtotal.length() == 11) {
                    //3spaces
                    subtotal = "    " + subtotal;
                }
                if (subtotal.length() == 12) {
                    //2spaces
                    subtotal = "   " + subtotal;
                }
                if (subtotal.length() == 13) {
                    //1spaces
                    subtotal = "  " + subtotal;
                }
                if (subtotal.length() == 14) {
                    //no space
                    subtotal = " " + subtotal;
                }


 */
                itemDetails = price + Gstt + subtotal;
                text_to_Print = text_to_Print + "[L]  <font size='normal'>" + price  +"[R]        "+ subtotal +" \n";

               // text_to_Print = text_to_Print + "[L] <font size='normal'>" + itemDetails + " \n";
                text_to_Print = text_to_Print+"[L]<font size='normal'>                                                "+" \n";

            }

            text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";


            finaltoPayAmount = finaltoPayAmount;
            taxAmount = "Rs. " + taxAmount;
            itemTotalwithoutGst = "Rs. " + itemTotalwithoutGst;
/*
            if (itemTotalwithoutGst.length() == 7) {
                //10spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "            ";
            }
            if (itemTotalwithoutGst.length() == 8) {
                //9spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "           ";
            }
            if (itemTotalwithoutGst.length() == 9) {
                //8spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "          ";
            }
            if (itemTotalwithoutGst.length() == 10) {
                //7spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "         ";
            }
            if (itemTotalwithoutGst.length() == 11) {
                //6spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "        ";
            }
            if (itemTotalwithoutGst.length() == 12) {
                //5spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "       ";
            }
            if (itemTotalwithoutGst.length() == 13) {
                //4spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "      ";
            }

            if (taxAmount.length() == 7) {
                //1spaces
                taxAmount = taxAmount + "";
            }
            if (taxAmount.length() == 8) {
                //0space
                taxAmount = taxAmount + "";
            }
            if (taxAmount.length() == 9) {
                //no space
                taxAmount = taxAmount;
            }

            if (finaltoPayAmount.length() == 6) {
                //8spaces
                finaltoPayAmount = "         " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 7) {
                //7spaces
                finaltoPayAmount = "        " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 8) {
                //6spaces
                finaltoPayAmount = "       " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 9) {
                //5spaces
                finaltoPayAmount = "      " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 10) {
                //4spaces
                finaltoPayAmount = "     " + finaltoPayAmount;
            }


 */

          //  text_to_Print = text_to_Print + "[L] " + itemTotalwithoutGst + taxAmount + finaltoPayAmount + " \n";
            text_to_Print = text_to_Print + "[L]  <font size='normal'>" + itemTotalwithoutGst  +"[R]        "+ "Rs. "+finaltoPayAmount +" \n";


            text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";

            CouponDiscount = finalCouponDiscountAmount;
            if (!CouponDiscount.equals("0")) {
                CouponDiscount = "Rs. " + CouponDiscount + ".00";

                if ((!CouponDiscount.equals("Rs.0.0")) && (!CouponDiscount.equals("Rs.0")) && (!CouponDiscount.equals("Rs.0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals("")) && (!CouponDiscount.equals("Rs. .00")) && (!CouponDiscount.equals("Rs..00"))) {

         /*           if (CouponDiscount.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        CouponDiscount = "Discount Amount                     " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        CouponDiscount = "Discount Amount                  " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        CouponDiscount = "Discount Amount                  " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        CouponDiscount = "Discount Amount                 " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        CouponDiscount = " Discount Amount                " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount               " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount              " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount              " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount             " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount             " + CouponDiscount;

                    }


 */
                   // text_to_Print = text_to_Print + "[L]" + CouponDiscount + " \n";
                text_to_Print = text_to_Print+"[L]  Coupon Discount "+"[R]      " +CouponDiscount+" \n";

                text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";

                }
            }

/*
        if (payableAmount.length() > 6) {

                if (payableAmount.length() == 7) {
                    //24spaces
                    //NEW TOTAL =9
                    payableAmount = " NET TOTAL                         Rs. " + payableAmount;
                }
                if (payableAmount.length() == 8) {
                    //23spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                         Rs. " + payableAmount;
                }
                if (payableAmount.length() == 9) {
                    //22spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                       Rs. " + payableAmount;
                }
                if (payableAmount.length() == 10) {
                    //21spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                      Rs. " + payableAmount;
                }
                if (payableAmount.length() == 11) {
                    //20spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                     Rs. " + payableAmount;
                }
                if (payableAmount.length() == 12) {
                    //19spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                    Rs. " + payableAmount;
                }
            } else {
                payableAmount = " NET TOTAL                      Rs.  " + payableAmount;

            }
            */

            //text_to_Print = text_to_Print + "[L]" + payableAmount + " \n";
            text_to_Print = text_to_Print+"[L]  NET TOTAL  "+"[R]       Rs. " +payableAmount+" \n";
            text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";



            text_to_Print = text_to_Print + "[L]  Order Type : " + String.valueOf(ordertype) + " \n";


            text_to_Print = text_to_Print + "[L]  Payment Mode : " + String.valueOf(payment_mode) + " \n";


            text_to_Print = text_to_Print + "[L]  Mobile No : " + String.valueOf(userMobile) + " \n" + " \n";


            text_to_Print = text_to_Print + "[C]    Thank you for choosing us !!!" + " \n";

        }
        else {
            Toast.makeText(AddBigBasketOrder.this, "Size of Cart is not matched", Toast.LENGTH_SHORT).show();
        }


        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);
        return printer.addTextToPrint(text_to_Print);


    }











    private void uploadMenuAvailabilityStatusTranscationinDB(String userPhoneNumber, String menuItemName, boolean availability, String menuItemSubCtgykey, String vendorkey, String dateandtime, String menuItemKey, String message, String menuItemStockAvlDetailskey, boolean allowNegative, String itemStockAvlDetailskey) {


        //Log.d(TAG, " uploaduserDatatoDB.");
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
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addMenuavailabilityTransaction,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                //  showProgressBar(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                showProgressBar(false);

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
        Volley.newRequestQueue(AddBigBasketOrder.this).add(jsonObjectRequest);


    }


    
    
    
    
    private void getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(String stockIncomingKey_avlDetails, String key_avlDetails, String menuItemKey_avlDetails, String receivedStock_AvlDetails, double currentBillingItemWeight_double, String itemName, String barcode, String orderid, String priceTypeForPOS, String tmcCtgy, String tmcSubCtgyKey, boolean isitemAvailable, boolean allowNegativeStock) {

        if((!stockIncomingKey_avlDetails.equals("")) && (!stockIncomingKey_avlDetails.equals(" - ")) &&(!stockIncomingKey_avlDetails.equals("null")) && (!stockIncomingKey_avlDetails.equals(null)) && (!stockIncomingKey_avlDetails.equals("0")) && (!stockIncomingKey_avlDetails.equals(" 0 ")) && (!stockIncomingKey_avlDetails.equals("-")) && (!stockIncomingKey_avlDetails.equals("nil")) ) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final double[] outgoingqty_stockOutGngDetails_Double = {0};

                    final double[] Total_outgoingqty_stockOutGngDetails_Double = {0};
                    final double[] receivedStock_AvlDetails_double = {0};
                    final double[] finalStockBalance_double = {0};
                    final String[] tmcSubCtgyKey_stockOutGngDetails_String = {""};

                    final String[] outgoingtype_stockOutGngDetails_String = {""};
                    final String[] stockincomingkey_stockOutGngDetails_String = {""};
                    final String[] stocktype_stockOutGngDetails_String = {""};
                    final String[] outgoingqty_stockOutGngDetails_String = {""};

                    Total_outgoingqty_stockOutGngDetails_Double[0] = 0;
                    finalStockBalance_double[0] = 0;
                    outgoingqty_stockOutGngDetails_Double[0] = 0;
                    stocktype_stockOutGngDetails_String[0] = "";
                    outgoingtype_stockOutGngDetails_String[0] = "";
                    stockincomingkey_stockOutGngDetails_String[0] = "";
                    outgoingqty_stockOutGngDetails_String[0] = "0";
                    receivedStock_AvlDetails_double[0] = 0;
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
                                        Log.d(TAG, " response: onMobileAppData " + response);

                                        Log.i(TAG, "getStock incoming name" + itemName);

                                        JSONArray JArray = response.getJSONArray("content");

                                        int arrayLength = JArray.length();
                                        for (int i = 0; i < arrayLength; i++) {
                                            JSONObject json = JArray.getJSONObject(i);
                                            outgoingqty_stockOutGngDetails_Double[0] = 0;
                                            stocktype_stockOutGngDetails_String[0] = "";
                                            outgoingtype_stockOutGngDetails_String[0] = "";
                                            stockincomingkey_stockOutGngDetails_String[0] = "";
                                            outgoingqty_stockOutGngDetails_String[0] = "0";
                                            receivedStock_AvlDetails_double[0] = 0;
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
                                            if(!outgoingtype_stockOutGngDetails_String[0].equals(Constants.SALES_CANCELLED_OUTGOINGTYPE)){
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


                                                Log.i(TAG, "getStock incoming stocktype_stockOutGngDetails_String" + stocktype_stockOutGngDetails_String[0]);

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
                                                Log.i(TAG, "getStock incoming outgoingtype_stockOutGngDetails_String" + outgoingtype_stockOutGngDetails_String[0]);




                                                Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_String" + outgoingqty_stockOutGngDetails_String[0]);


                                                try {
                                                    outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


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

                                                Log.i(TAG, "getStock incoming stockincomingkey_stockOutGngDetails_String" + stockincomingkey_stockOutGngDetails_String[0]);


                                                try {
                                                    outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


                                                if (outgoingtype_stockOutGngDetails_String[0].equals(Constants.SUPPLYGAP_OUTGOINGTYPE)) {
                                                    Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 1 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                    Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 1 " + outgoingqty_stockOutGngDetails_Double[0]);


                                                    try {
                                                        //  if (Total_outgoingqty_stockOutGngDetails_Double[0] > outgoingqty_stockOutGngDetails_Double[0]) {
                                                        //    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];
                                                        //     Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 2 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        //     Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 2 " + outgoingqty_stockOutGngDetails_Double[0]);

                                                        // }
                                                        //else {
                                                        //      Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 2.1 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        //      Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 2.1  " + outgoingqty_stockOutGngDetails_Double[0]);

                                                        //       Total_outgoingqty_stockOutGngDetails_Double[0] = outgoingqty_stockOutGngDetails_Double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                                        //   }
                                                        Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                } else {
                                                    try {
                                                        Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 3 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 3 " + outgoingqty_stockOutGngDetails_Double[0]);

                                                        Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] + outgoingqty_stockOutGngDetails_Double[0];
                                                        Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 4 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                        Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 4 " + outgoingqty_stockOutGngDetails_Double[0]);


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                            }
                                        }

                                        Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);


                                        Log.i(TAG, "getStock incoming receivedStock_AvlDetails  " + receivedStock_AvlDetails);

                                        try {
                                            receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedStock_AvlDetails);
                                            Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        double stockBalanceBeforeMinusCurrentItem = 0;
                                        try {
                                            Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double 2  " + receivedStock_AvlDetails_double[0]);
                                            Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double  5  " + Total_outgoingqty_stockOutGngDetails_Double[0]);

                                            stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                            Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 2  " + stockBalanceBeforeMinusCurrentItem);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        try {


                                            Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 3  " + stockBalanceBeforeMinusCurrentItem);

                                            finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem - currentBillingItemWeight_double;

                                            Log.i(TAG, "getStock incoming currentBillingItemWeight_double 4 " + currentBillingItemWeight_double);
                                            Log.i(TAG, "getStock incoming finalStockBalance_double 4 " + finalStockBalance_double[0]);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuItemKey_avlDetails, stockIncomingKey_avlDetails, itemName, barcode);

                                        // UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0]);

                                        AddDataInStockOutGoingTable(currentBillingItemWeight_double, orderid, stockIncomingKey_avlDetails, itemName, barcode, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey);

                                        if (isitemAvailable) {

                                            if (finalStockBalance_double[0] <= 0) {

                                                if (!allowNegativeStock) {


                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], true, false, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                                } else {
                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                                }


                                            } else {
                                                UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                            }
                                        } else {
                                            UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }

                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(@NonNull VolleyError error) {
                            Log.d(TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

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
                            Toast.makeText(AddBigBasketOrder.this, "Error in General App Data code :  " + errorCode, Toast.LENGTH_LONG).show();


                            showProgressBar(false);

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
                    Volley.newRequestQueue(AddBigBasketOrder.this).add(jsonObjectRequest);


                }
            };


            new Thread(runnable).start();//to work in Background

        }
    }

    private void AddDataInStockBalanceTransactionHistory(double finalStockBalance_double, double stockBalanceBeforeMinusCurrentItem, String menuItemKey_avlDetails, String stockIncomingKey_avlDetails, String itemName, String barcode) {

        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode",String.valueOf(barcode));
            jsonObject.put("itemname", String.valueOf(itemName));
            jsonObject.put("transactiontime",String.valueOf(Currenttime));
            jsonObject.put("menuitemkey", String.valueOf(menuItemKey_avlDetails));
            jsonObject.put("newstockbalance",finalStockBalance_double);
            jsonObject.put("oldstockbalance", stockBalanceBeforeMinusCurrentItem);
            jsonObject.put("stockincomingkey",String.valueOf(stockIncomingKey_avlDetails));
            jsonObject.put("usermobileno", String.valueOf(usermobileNo));
            jsonObject.put("vendorkey", String.valueOf(vendorKey));




        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addEntry_StockTransactionHistory
                , jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        showProgressBar(false);
                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(AddBigBasketOrder.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(AddBigBasketOrder.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AddBigBasketOrder.this).add(jsonObjectRequest);



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


        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode",String.valueOf(barcode));
            jsonObject.put("itemname", String.valueOf(itemName));
            jsonObject.put("outgoingdate",String.valueOf(Currenttime));
            jsonObject.put("outgoingtype", String.valueOf(Constants.SALES_FULFILLED_OUTGOINGTYPE));
            jsonObject.put("outgoingqty",grossweightingrams_double);
            jsonObject.put("salesorderid", String.valueOf(orderid));
            jsonObject.put("stocktype",(stockType));
            jsonObject.put("tmcctgykey", String.valueOf(tmcCtgy));
            jsonObject.put("tmcsubctgykey", String.valueOf(tmcSubCtgyKey));
            jsonObject.put("vendorkey", String.valueOf(vendorKey));
            jsonObject.put("userkey", String.valueOf(""));
            jsonObject.put("stockincomingkey", String.valueOf(stockIncomingKey_avlDetails));
            jsonObject.put("inventoryusermobileno", String.valueOf(usermobileNo));



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_addEntry_StockOutGoingDetails
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        // showProgressBar(false);
                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(AddBigBasketOrder.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(AddBigBasketOrder.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AddBigBasketOrder.this).add(jsonObjectRequest);





    }


    private void UpdateStockBalanceinMenuItemStockAvlDetail(String key_avlDetails, double finalStockBalance_double, boolean changeItemAvailability, boolean isitemAvailable, String menuItemKey_avlDetails, String tmcSubCtgyKey, String itemName) {


        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        if(changeItemAvailability){


            //Log.d(TAG, " uploaduserDatatoDB.");
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject2.put("key", menuItemKey_avlDetails);


                jsonObject2.put("itemavailability", String.valueOf(isitemAvailable).toUpperCase());


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMenuItemDetails,
                    jsonObject2, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {
                    //Log.d(Constants.TAG, "Response: " + response);
                    String message = "";
                    try {
                        message = response.getString("message");


                    if(changeItemAvailability) {
                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItem.size(); iterator_menuitemStockAvlDetails++) {

                            Modal_MenuItem_Settings modal_menuItemStockAvlDetails = MenuItem.get(iterator_menuitemStockAvlDetails);

                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuItemId());

                            if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(isitemAvailable));
                                uploadMenuAvailabilityStatusTranscationinDB(usermobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorKey,Currenttime,menuItemKey_avlDetails,message, "", false, "");
                                savedMenuIteminSharedPrefrences(MenuItem,iterator_menuitemStockAvlDetails);

                            }

                        }
                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showProgressBar(false);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "Error: " + error.toString());
                    showProgressBar(false);
                    Toast.makeText(AddBigBasketOrder.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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
            Volley.newRequestQueue(AddBigBasketOrder.this).add(jsonObjectRequest);





            try {
                jsonObject.put("key",key_avlDetails);
                jsonObject.put("lastupdatedtime",Currenttime);
                jsonObject.put("stockbalance", finalStockBalance_double);
                jsonObject.put("itemavailability",isitemAvailable);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                jsonObject.put("key",key_avlDetails);
                jsonObject.put("lastupdatedtime",Currenttime);
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
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_MenuItemStockAvlDetails
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                     String message =  response.getString("message");
                     if(changeItemAvailability){

                     }

                    if(message.equals("success")) {


                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItem.size(); iterator_menuitemStockAvlDetails++) {

                            Modal_MenuItem_Settings modal_menuItemStockAvlDetails = MenuItem.get(iterator_menuitemStockAvlDetails);

                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuitemkey_AvlDetails());

                            if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                if (changeItemAvailability) {

                                    modal_menuItemStockAvlDetails.setItemavailability_AvlDetails(String.valueOf(isitemAvailable));
                                    modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(isitemAvailable));
                                    modal_menuItemStockAvlDetails.setStockbalance_AvlDetails(String.valueOf(finalStockBalance_double));

                                    savedMenuIteminSharedPrefrences(MenuItem,iterator_menuitemStockAvlDetails);

                                }
                                else {
                                    modal_menuItemStockAvlDetails.setStockbalance_AvlDetails(String.valueOf(finalStockBalance_double));

                                    savedMenuIteminSharedPrefrences(MenuItem, iterator_menuitemStockAvlDetails);
                                }
                            }

                        }
                    }

                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(AddBigBasketOrder.this,"Failed to change express delivery slot status in Delivery slots",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(AddBigBasketOrder.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AddBigBasketOrder.this).add(jsonObjectRequest);





    }

    private void savedMenuIteminSharedPrefrences(List<Modal_MenuItem_Settings> menuItem, int iterator_menuitemStockAvlDetails) {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList",json );
        editor.apply();

        try {
            adapter_addBigBasketOrdersRecyclerview.notifyDataSetChanged();
            //            adapter_addBigBasketOrdersRecyclerview.notify();
          //  adapter_addBigBasketOrdersRecyclerview.notifyItemChanged(iterator_menuitemStockAvlDetails);

           // adapter_addBigBasketOrdersRecyclerview.notifyAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }



    private void PlaceOrder_in_OrderItemDetails(String subCtgyKey, String itemnamee, String Grossweight, String itemweightt,
                                                String Netweight, String quantityy, String itemamountt,
                                                String discountamountt,
                                                String gstamountt, String vendorkeyy, String currenttime,
                                                long sTime, String vendorkey, String vendorName, String grossWeightingrams, double grossweightingrams_double, String slotDate){

        String orderid = String.valueOf(sTime);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
            jsonObject.put("itemname", itemnamee);

            jsonObject.put("tmcsubctgykey", subCtgyKey);

            jsonObject.put("quantity", quantityy);
            try {

                if (itemweightt.equals("") || itemweightt == (null)) {
                    jsonObject.put("grossweight", Grossweight);
                    jsonObject.put("netweight", Grossweight);
                    if(Grossweight.equals("")){
                        jsonObject.put("netweight", Netweight);
                        jsonObject.put("grossweightingrams", Grossweight);

                    }
                    else
                    {
                        jsonObject.put("netweight", Grossweight);
                        if(grossweightingrams_double!=0) {
                            jsonObject.put("grossweightingrams", grossweightingrams_double);
                        }
                        else{
                            jsonObject.put("grossweightingrams", Grossweight);

                        }


                    }

                } else {
                    jsonObject.put("grossweight", itemweightt);
                    if(Grossweight.equals("")){
                        jsonObject.put("netweight", Netweight);
                        jsonObject.put("grossweightingrams", itemweightt);

                    }
                    else
                    {
                        jsonObject.put("netweight", itemweightt);
                        if(grossweightingrams_double!=0) {
                            jsonObject.put("grossweightingrams", grossweightingrams_double);
                        }
                        else{
                            jsonObject.put("grossweightingrams", itemweightt);

                        }
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            jsonObject.put("slotdate", slotDate);

            jsonObject.put("discountamount", discountamountt);
            jsonObject.put("gstamount", gstamountt);
            jsonObject.put("vendorid", vendorkeyy);
            jsonObject.put("orderplacedtime", currenttime);
            jsonObject.put("tmcprice", itemamountt);
            jsonObject.put("vendorkey", vendorkey);
            jsonObject.put("vendorname", vendorName);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInOrderItemDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        //   printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);

                    }
                } catch (JSONException e) {
                    //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + error);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);



    }


    private void PlaceOrder_in_OrderTrackingDetails(long sTime,String Currenttiime) {

        if(isOrderTrackingDetailsMethodCalled){
            return;
        }

        isOrderTrackingDetailsMethodCalled = true;

        String orderid = String.valueOf(sTime);
        String orderplacedDate_time = getDate_and_time();
        //Log.d(Constants.TAG, "orderplacedDate_time: " + orderplacedDate_time);
        //Log.d(Constants.TAG, "orderplacedDate_time: " + getDate_and_time());
        //Log.d(Constants.TAG, "orderplacedDate_time: " + Currenttiime);
        //Log.d(Constants.TAG, "orderplacedDate_time: " + Currenttime);

        SharedPreferences sh
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);
        String vendorkey = sh.getString("VendorKey","vendor_1");

        JSONObject  orderTrackingTablejsonObject = new JSONObject();
        try {
            orderTrackingTablejsonObject.put("orderdeliverytime",Currenttime);
            orderTrackingTablejsonObject.put("orderplacedtime",Currenttime);
            if(orderdetailsnewschema){

                orderTrackingTablejsonObject.put("slotdate",getDate());

            }
            orderTrackingTablejsonObject.put("usermobileno","+91" + customermobileno_edittextwidget.getText().toString());
            orderTrackingTablejsonObject.put("orderid",orderid);
            orderTrackingTablejsonObject.put("vendorkey",vendorkey);
            orderTrackingTablejsonObject.put("orderstatus","DELIVERED");

        }


        catch (JSONException e) {
            e.printStackTrace();

        }




        String Api_To_PlaceOrderInTrackingDetails = "";
        if(orderdetailsnewschema){
            Api_To_PlaceOrderInTrackingDetails = Constants.api_AddVendorTrackingOrderDetails;

        }
        else{
            Api_To_PlaceOrderInTrackingDetails = Constants.api_addOrderDetailsInOrderTrackingDetailsTable;

        }






        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api_To_PlaceOrderInTrackingDetails,
                orderTrackingTablejsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {




                String Api_To_PlaceOrderInTrackingDetails = "";
                if(orderdetailsnewschema){
                    Api_To_PlaceOrderInTrackingDetails = Constants.api_AddVendorTrackingOrderDetails;

                }
                else{
                    Api_To_PlaceOrderInTrackingDetails = Constants.api_addOrderDetailsInOrderTrackingDetailsTable;

                }

                //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                try {
                    String message = response.getString("message");
                    if(message .equals( "success")){
                        // printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        isOrderTrackingDetailsMethodCalled = false;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isOrderTrackingDetailsMethodCalled = false;

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                isOrderTrackingDetailsMethodCalled = false;

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
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);





    }



    private void PlaceOrder_in_PaymentTransactionDetails(long sTime, String paymentmode, String transactionAmount, String userMobile) {
        if(isPaymentDetailsMethodCalled){
            return;
        }


        isPaymentDetailsMethodCalled = true;

        String orderid = String.valueOf(sTime);

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
            jsonObject.put("mobileno", userMobile);
            jsonObject.put("merchantorderid", "");
            jsonObject.put("paymentmode", paymentmode);
            jsonObject.put("paymenttype", "");
            jsonObject.put("transactiontime", Currenttime);
            jsonObject.put("transactionamount", transactionAmount);
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("merchantpaymentid", "");
            jsonObject.put("desp", "");



        }


        catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInPaymentDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                try {
                    String message = response.getString("message");
                    if(message .equals( "success")){
                        // printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        isPaymentDetailsMethodCalled = false;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isPaymentDetailsMethodCalled = false;

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                isPaymentDetailsMethodCalled = false;

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }













    private Handler newHandler() {
        Handler.Callback callback = new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String data = bundle.getString("CartItem");

                if (data.equalsIgnoreCase("addNewItem")) {

                }

                if (data.equalsIgnoreCase("addBillDetails")) {
                    //   createBillDetails(cart_Item_List);

                }
                return false;
            }
        };
        return new Handler(callback);
    }



    private void getMenuItemFromSharedPreferences() {
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

    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        if(orderdetailsnewschema) {

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = day.format(c);

            return CurrentDate;

        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDay = day.format(c);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;

            //CurrentDate = CurrentDay+", "+CurrentDate;
            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;
        }
    }



    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDatee = df.format(c);
        CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }

    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);


        }

    }



    private  void printReciptUsingPOSMachinePrinter(String userMobile, String tokenno, String itemTotalwithoutGst, String totaltaxAmount, String payableAmount, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cart_Item_hashmap, String payment_mode, String discountAmountt, String ordertype) {
        try {
            Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[cart_Item_List.size()];
            double oldSavedAmount = 0;
            String CouponDiscount = "0";
            for (int i = 0; i < cart_item_list.size(); i++) {
                double savedAmount;
                String itemUniqueCode = cart_item_list.get(i);
                Modal_NewOrderItems modal_newOrderItems = cart_Item_hashmap.get(itemUniqueCode);
                String itemName = String.valueOf(modal_newOrderItems.getItemname());
                int indexofbraces = itemName.indexOf("(");
                if (indexofbraces >= 0) {
                    itemName = itemName.substring(0, indexofbraces);

                }
                if (itemName.length() > 21) {
                    itemName = itemName.substring(0, 21);
                    itemName = itemName + "...";
                }
                savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                oldSavedAmount = savedAmount + oldSavedAmount;
                String Gst = modal_newOrderItems.getGstAmount();
                String subtotal = modal_newOrderItems.getSubTotal_perItem();
                String quantity = modal_newOrderItems.getQuantity();
                String price = modal_newOrderItems.getItemFinalPrice();
                String weight = modal_newOrderItems.getItemFinalWeight();
                Printer_POJO_ClassArray[i] = new Printer_POJO_Class("", quantity, orderid, itemName, weight, price, "0.00", Gst, subtotal, "cutname");

            }

            Printer_POJO_Class Printer_POJO_ClassArraytotal = new Printer_POJO_Class(itemTotalwithoutGst, discountAmountt, totaltaxAmount, payableAmount, oldSavedAmount);
            PrinterFunctions.PortDiscovery(portName, portSettings);
            PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

            // PrinterFunctions.OpenPort( portName, portSettings);
            //    PrinterFunctions.CheckStatus( portName, portSettings,2);
           /* PrinterFunctions.SelectPrintMode(portName, portSettings, 0);
            PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");


            */
            PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

            if((vendorKey.equals("vendor_4")) ||  (vendorKey.equals("wholesalesvendor_1"))) {


                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "MK Proteins" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

            }
            else {

               PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");

            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Fresh Meat and SeaFood" + "\n");
            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine1 + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine2 + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine3 + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreLanLine + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "GSTIN :33AAJCC0055D1Z9" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, Currenttime + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "# " + orderid + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "ITEM NAME * QTY" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "RATE                GST         SUBTOTAL" + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            for (int i = 0; i < Printer_POJO_ClassArray.length; i++) {

                PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                String itemrate, Gst, subtotal;
                itemrate = "Rs." + Printer_POJO_ClassArray[i].getItemRate();
                Gst = "Rs." + Printer_POJO_ClassArray[i].getGST();
                subtotal = "Rs." + Printer_POJO_ClassArray[i].getSubTotal();
                if (itemrate.length() == 4) {
                    //14spaces
                    itemrate = itemrate + "              ";
                }
                if (itemrate.length() == 5) {
                    //13spaces
                    itemrate = itemrate + "             ";
                }
                if (itemrate.length() == 6) {
                    //12spaces
                    itemrate = itemrate + "            ";
                }
                if (itemrate.length() == 7) {
                    //11spaces
                    itemrate = itemrate + "           ";
                }
                if (itemrate.length() == 8) {
                    //10spaces
                    itemrate = itemrate + "          ";
                }
                if (itemrate.length() == 9) {
                    //9spaces
                    itemrate = itemrate + "         ";
                }
                if (itemrate.length() == 10) {
                    //8spaces
                    itemrate = itemrate + "        ";
                }
                if (itemrate.length() == 11) {
                    //7spaces
                    itemrate = itemrate + "       ";
                }
                if (itemrate.length() == 12) {
                    //6spaces
                    itemrate = itemrate + "      ";
                }
                if (itemrate.length() == 13) {
                    //5spaces
                    itemrate = itemrate + "     ";
                }
                if (itemrate.length() == 14) {
                    //4spaces
                    itemrate = itemrate + "    ";
                }
                if (itemrate.length() == 15) {
                    //3spaces
                    itemrate = itemrate + "   ";
                }
                if (itemrate.length() == 16) {
                    //2spaces
                    itemrate = itemrate + "  ";
                }
                if (itemrate.length() == 17) {
                    //1spaces
                    itemrate = itemrate + " ";
                }
                if (itemrate.length() == 18) {
                    //1spaces
                    itemrate = itemrate + "";
                }


                if (Gst.length() == 7) {
                    //1spaces
                    Gst = Gst + " ";
                }
                if (Gst.length() == 8) {
                    //0space
                    Gst = Gst + "";
                }
                if (Gst.length() == 9) {
                    //no space
                    Gst = Gst;
                }
                if (subtotal.length() == 4) {
                    //5spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 5) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 6) {
                    //8spaces
                    subtotal = "        " + subtotal;
                }
                if (subtotal.length() == 7) {
                    //7spaces
                    subtotal = "       " + subtotal;
                }
                if (subtotal.length() == 8) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 9) {
                    //5spaces
                    subtotal = "     " + subtotal;
                }
                if (subtotal.length() == 10) {
                    //4spaces
                    subtotal = "    " + subtotal;
                }
                if (subtotal.length() == 11) {
                    //3spaces
                    subtotal = "   " + subtotal;
                }
                if (subtotal.length() == 12) {
                    //2spaces
                    subtotal = "  " + subtotal;
                }
                if (subtotal.length() == 13) {
                    //1spaces
                    subtotal = " " + subtotal;
                }
                if (subtotal.length() == 14) {
                    //no space
                    subtotal = "" + subtotal;
                }


                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Printer_POJO_ClassArray[i].getItemName() + "  *  " + Printer_POJO_ClassArray[i].getItemWeight() + "(" + Printer_POJO_ClassArray[i].getQuantity() + ")" + "\n");

                PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, itemrate + Gst + subtotal + "\n\n");
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            String totalRate = "Rs." + Printer_POJO_ClassArraytotal.getTotalRate();
            String totalGst = "Rs." + Printer_POJO_ClassArraytotal.getTotalGST();
            String totalSubtotal = "Rs." + finaltoPayAmount;
            if (totalRate.length() == 7) {
                //10spaces
                totalRate = totalRate + "          ";
            }
            if (totalRate.length() == 8) {
                //9spaces
                totalRate = totalRate + "         ";
            }
            if (totalRate.length() == 9) {
                //8spaces
                totalRate = totalRate + "        ";
            }
            if (totalRate.length() == 10) {
                //7spaces
                totalRate = totalRate + "       ";
            }
            if (totalRate.length() == 11) {
                //6spaces
                totalRate = totalRate + "      ";
            }
            if (totalRate.length() == 12) {
                //5spaces
                totalRate = totalRate + "     ";
            }
            if (totalRate.length() == 13) {
                //4spaces
                totalRate = totalRate + "    ";
            }

            if (totalGst.length() == 7) {
                //1spaces
                totalGst = totalGst + " ";
            }
            if (totalGst.length() == 8) {
                //0space
                totalGst = totalGst + "";
            }
            if (totalGst.length() == 9) {
                //no space
                totalGst = totalGst;
            }

            if (totalSubtotal.length() == 6) {
                //8spaces
                totalSubtotal = "        " + totalSubtotal;
            }
            if (totalSubtotal.length() == 7) {
                //7spaces
                totalSubtotal = "       " + totalSubtotal;
            }
            if (totalSubtotal.length() == 8) {
                //6spaces
                totalSubtotal = "      " + totalSubtotal;
            }
            if (totalSubtotal.length() == 9) {
                //5spaces
                totalSubtotal = "     " + totalSubtotal;
            }
            if (totalSubtotal.length() == 10) {
                //4spaces
                totalSubtotal = "    " + totalSubtotal;
            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, totalRate + totalGst + totalSubtotal + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");





     /*

        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        String SavedAmount = "You just saved Rs."+" on these items"+String.valueOf(Printer_POJO_ClassArraytotal.getOldSavedAmount());

        PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,1,SavedAmount+"\n");


 */
            CouponDiscount = "0";

            CouponDiscount = Printer_POJO_ClassArraytotal.getTotaldiscount();

            if (!CouponDiscount.equals("0")) {
                CouponDiscount = "Rs. " + CouponDiscount + ".00";

                if ((!CouponDiscount.equals("Rs.0.0")) && (!CouponDiscount.equals("Rs.0")) && (!CouponDiscount.equals("Rs.0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals("")) && (!CouponDiscount.equals("Rs. .00")) && (!CouponDiscount.equals("Rs..00"))) {

                    if (CouponDiscount.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        CouponDiscount = "Discount Amount                   " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        CouponDiscount = "Discount Amount                 " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        CouponDiscount = "Discount Amount                " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        CouponDiscount = "Discount Amount               " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        CouponDiscount = " Discount Amount              " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount             " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount            " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount            " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount           " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount           " + CouponDiscount;

                    }


                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, CouponDiscount + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                }
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            String NetTotal = Printer_POJO_ClassArraytotal.getTotalsubtotal();
            try {
                if (!NetTotal.contains(".")) {
                    int netTotaLint = Integer.parseInt(NetTotal);
                    int netdiscountAmountint = Integer.parseInt(discountAmountt);
                    netTotaL = netTotaLint - netdiscountAmountint;

                } else {

                    double nettotalDouble = Double.parseDouble(NetTotal);
                    double discountAmountDouble = Double.parseDouble(discountAmountt);
                    netTotaL = (int) Math.round(nettotalDouble-discountAmountDouble);

                }
                NetTotal = String.valueOf(netTotaL);

            } catch (Exception e) {
                e.printStackTrace();
                NetTotal = Printer_POJO_ClassArraytotal.getTotalsubtotal();

            }
            if (NetTotal.length() > 6) {

                if (NetTotal.length() == 7) {
                    //24spaces
                    //NEW TOTAL =9
                    NetTotal = " NET TOTAL                       Rs. " + NetTotal;
                }
                if (NetTotal.length() == 8) {
                    //23spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                       Rs. " + NetTotal;
                }
                if (NetTotal.length() == 9) {
                    //22spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                      Rs. " + NetTotal;
                }
                if (NetTotal.length() == 10) {
                    //21spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                    Rs. " + NetTotal;
                }
                if (NetTotal.length() == 11) {
                    //20spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                   Rs. " + NetTotal;
                }
                if (NetTotal.length() == 12) {
                    //19spaces
                    //NEW TOTAL =9
                    NetTotal = "  NET TOTAL                  Rs. " + NetTotal;
                }
            } else {
                NetTotal = " NET TOTAL                    Rs.  " + NetTotal;

            }

            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, NetTotal + "\n");

          /*  PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Earned Rewards : " +  String.valueOf((int)(0))+"\n");


           */
            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            if(!ordertype.equals(Constants.POSORDER)) {
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Order Type: ");


                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, ordertype + "\n");
            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Payment Mode: ");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, payment_mode + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "MobileNo : ");


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, userMobile + "           " + "\n");
/*
            PrinterFunctions.SetLineSpacing(portName, portSettings, 120);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 30, "ID : ");


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 50, tokenno + "\n");


 */


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "\n" + "Thank you for choosing us !!!  " + "\n");


            PrinterFunctions.PreformCut(portName, portSettings, 1);
            //  PrinterFunctions.PrintSampleReceipt(portName,portSettings);
            //Log.i("tag", "printer Log    " + PrinterFunctions.PortDiscovery(portName, portSettings));

            //Log.i("tag", "printer Log    " + PrinterFunctions.OpenPort(portName, portSettings));

            //Log.i("tag", "printer Log    " + PrinterFunctions.CheckStatus(portName, portSettings, 2));
           /* if (!isPrintedSecondTime) {
                showProgressBar(false);
                //isPrintedSecondTime = true;
                //showProgressBar(true);


                //       openPrintAgainDialog(userMobile, tokenno, itemTotalwithoutGst, totaltaxAmount, payableAmount, orderid, cart_item_list, cart_Item_hashmap, payment_mode);

                new TMCAlertDialogClass(getApplicationContext(), R.string.app_name, R.string.RePrint_Instruction,
                        R.string.Yes_Text, R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                isPrintedSecondTime = true;

                                printRecipt(userMobile, tokenno, itemTotalwithoutGst, totaltaxAmount, payableAmount, orderid, cart_item_list, cart_Item_hashmap, payment_mode, "0", ordertype);

                            }

                            @Override
                            public void onNo() {


                                cart_Item_List.clear();
                                cart_Item_hashmap.clear();
                                cart_item_list.clear();
                                cartItem_hashmap.clear();
                                        StockBalanceChangedForThisItemList.clear();

                                ispaymentMode_Clicked = false;
                                isOrderDetailsMethodCalled = false;

                                isPaymentDetailsMethodCalled = false;
                                isOrderTrackingDetailsMethodCalled = false;
                                new_to_pay_Amount = 0;
                                old_taxes_and_charges_Amount = 0;
                                old_total_Amount = 0;
                                createEmptyRowInListView("empty");
                                CallAdapter();
                            //    discountAmount = "0";

                             //   discount_Edit_widget.setText("0");
                                finaltoPayAmount = "0";
                            //    discount_rs_text_widget.setText(discountAmount);
                                OrderTypefromSpinner = "POS Order";
                            //    orderTypeSpinner.setSelection(0);
                                total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                swiggyOrdersCustomermobileno.setText("");
                                isPrintedSecondTime = false;
                                showProgressBar(false);

                                totalAmounttopay=0;
                                finalamounttoPay=0;

                              //  redeemPointsLayout.setVisibility(View.GONE);
                                //discountAmountLayout.setVisibility(View.VISIBLE);

                            }
                        });


            } else {

            */
            cart_Item_List.clear();
            cart_Item_hashmap.clear();
            cart_item_list.clear();
            cartItem_hashmap.clear();
            StockBalanceChangedForThisItemList.clear();

            new_to_pay_Amount = 0;
            old_taxes_and_charges_Amount = 0;
            old_total_Amount = 0;
            createEmptyRowInListView("empty");
            CallAdapter();
            //discountAmount = "0";

            // discount_Edit_widget.setText("0");
            finaltoPayAmount = "0";
            //  discount_rs_text_widget.setText(discountAmount);
            OrderTypefromSpinner = "POS Order";
            //   orderTypeSpinner.setSelection(0);
            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));



            customermobileno_edittextwidget.setText("");
            bigbasketOrdersCustomermobileno.setText("");
            isPrintedSecondTime = false;
            ispaymentMode_Clicked = false;
            isOrderDetailsMethodCalled = false;

            isPaymentDetailsMethodCalled = false;
            isOrderTrackingDetailsMethodCalled = false;
            totalAmounttopay=0;
            finalamounttoPay=0;
            showProgressBar(false);
             /*   pointsalreadyredeemDouble=0;
                totalpointsuserhave_afterapplypoints=0;
                pointsenteredToredeem_double=0;
                pointsenteredToredeem="";

                finaltoPayAmountwithRedeemPoints="";
                redeemPoints_String="";
                redeemKey="";
                mobileno_redeemKey="";
                discountAmountalreadyusedtoday="";
                totalpointsredeemedalreadybyuser="";
                totalordervalue_tillnow="";
                totalredeempointsuserhave="";

                redeemed_points_text_widget.setText("");
                redeemPointsLayout.setVisibility(View.GONE);
                discountAmountLayout.setVisibility(View.VISIBLE);

              */


            //  }



        /*
            cart_Item_List.clear();
            cart_Item_hashmap.clear();
            cart_item_list.clear();
                    StockBalanceChangedForThisItemList.clear();

            cartItem_hashmap.clear();
            ispaymentMode_Clicked = false;
            isOrderDetailsMethodCalled = false;

            isPaymentDetailsMethodCalled = false;
            isOrderTrackingDetailsMethodCalled = false;
            new_to_pay_Amount = 0;
            old_taxes_and_charges_Amount = 0;
            old_total_Amount = 0;
            createEmptyRowInListView("empty");
            CallAdapter();
            discountAmount = "0";
            CouponDiscount = "0";
            discount_Edit_widget.setText("0");
            finaltoPayAmount = "0";
            discount_rs_text_widget.setText(discountAmount);

            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

            swiggyOrdersCustomermobileno.setText("");
            isPrintedSecondTime = false;
            showProgressBar(false);




         */

        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),String.valueOf(e),Toast.LENGTH_LONG).show();
        }


    }

}