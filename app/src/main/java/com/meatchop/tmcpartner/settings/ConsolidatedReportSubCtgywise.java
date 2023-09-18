package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.add_replacement_refund_order.Modal_ReplacementTransactionDetails;
import com.meatchop.tmcpartner.settings.report_activity_model.ListData;
import com.meatchop.tmcpartner.settings.report_activity_model.ListItem;
import com.meatchop.tmcpartner.settings.report_activity_model.ListSection;
import com.meatchop.tmcpartner.tmc_testtable_details.Modal_TMCTestTable;
import com.meatchop.tmcpartner.tmc_testtable_details.TMCTestTableInterface;
import com.meatchop.tmcpartner.tmc_testtable_details.TMCTestTable_AsyncTask;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableService;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;
import com.meatchop.tmcpartner.sqlite.TMCSubCtgyItemSQL_DB_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;


public class ConsolidatedReportSubCtgywise extends AppCompatActivity {
    LinearLayout fetchData_Layout,generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel, generateSubCtgyReport_Layout;
    DatePickerDialog datepicker;
    TextView phoneProductSales,totalPhone_MarkupAmount,totalAppSales,totalApp_MarkupAmount,deliveryCharge_label,instruction_textview,
            wholesalesOrderSales,refundAmount_textwidget, replacementAmount_textwidget, noofOrders, noofPacks, vendorName,
            deliveryChargeAmount_textwidget, totalSales_headingText, appsales, possales,creditSales, swiggySales, dunzoSales, 
            bigbasketSales, phoneOrderSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount,
            totalGST_Amt, final_sales,creditProductSales,creditMarkupAmount;
    String vendorKey, vendorname, ordertype, slotname, DateString,paymentMode;
    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;
    double screenInches;
    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();
    Adapter_ConsolidatedSalesReport_listview adapater_pos_sales_report;
    double oldpayableamount = 0;

    public static List<String> ordertypeArray;
    public static HashMap<String, Modal_OrderDetails> ordertypeHashmap = new HashMap();

    public static List<String> replacementTransactiontypeArray = new ArrayList<>();
    public static HashMap<String, List<Modal_ReplacementTransactionDetails>> replacementTransactiontypeHashmap = new HashMap();


    public static List<Modal_OrderDetails> SubCtgyKey_List;
    public static HashMap<String, Modal_OrderDetails> SubCtgyKey_hashmap = new HashMap();

    public static List<String> Pos_couponDiscountOrderidArray;
    public static HashMap<String, String> Pos_couponDiscount_hashmap = new HashMap();

    public static List<String> couponDiscountOrderidArray;
    public static HashMap<String, String> couponDiscount_hashmap = new HashMap();


    public static List<String> deliveryChargeOrderidArray;
    public static HashMap<String, String> deliveryCharge_hashmap = new HashMap();

    public static List<String> phoneorderdeliveryChargeOrderidArray;
    public static HashMap<String, String> phoneorderdeliveryCharge_hashmap = new HashMap();


    public static List<String> creditOrderdeliveryChargeOrderidArray;
    public static HashMap<String, String> creditOrderdeliveryCharge_hashmap = new HashMap();


    public static List<String> phoneOrders_couponDiscountOrderidArray;
    public static HashMap<String, String> phoneOrders_couponDiscount_hashmap = new HashMap();

    public static List<String> swiggyOrders_couponDiscountOrderidArray;
    public static HashMap<String, String> swiggyOrders_couponDiscount_hashmap = new HashMap();

    public static List<String> wholesaleOrders_couponDiscountOrderidArray;
    public static HashMap<String, String> wholesaleOrders_couponDiscount_hashmap = new HashMap();


    public static List<String> dunzoOrders_couponDiscountOrderidArray;
    public static HashMap<String, String> dunzoOrders_couponDiscount_hashmap = new HashMap();

    public static List<String> creditOrders_couponDiscountOrderidArray;
    public static HashMap<String, String> creditOrders_couponDiscount_hashmap = new HashMap();



    public static List<String> bigBasketOrders_couponDiscountOrderidArray;
    public static HashMap<String, String> bigBasketOrders_couponDiscount_hashmap = new HashMap();

    public static List<String> SubCtgywiseTotalArray;
    public static HashMap<String, String> SubCtgywiseTotalHashmap = new HashMap();
    public static HashMap<String, String> SubCtgywiseWeightHashmap = new HashMap();
    public static HashMap<String, String> SubCtgywiseQuantityHashmap = new HashMap();
    public static List<String> tmcSubCtgykey;
    List<ListData> dataList = new ArrayList<>();
    boolean isgetPreOrderForSelectedDateCalled = false;
    boolean isgetOrderForSelectedDateCalled = false;

    boolean isgetReplacementOrderForSelectedDateCalled = false;

    boolean isOrderDetailsResponseReceivedForSelectedDate = false;
    boolean isReplacementTransacDetailsResponseReceivedForSelectedDate = false;

    int no_of_orders = 0;


    int no_of_ItemCount = 0;
    ScrollView scrollView;
    double itemDespTotalAmount = 0;
    String replacementOrderDetailsString, startDateString_forReplacementransaction = "", endDateString_forReplacementransaction = "", CurrentDate,
            CouponDiscount, pos_CouponDiscount,WholeSales_CouponDiscount, Swiggy_CouponDiscount, Dunzo_CouponDiscount, BigBasket_CouponDiscount, 
            PhoneOrder_CouponDiscount,Credit_CouponDiscount, PreviousDateString,
            deliveryamount = "0", deliveryAmountPhoneOrder ="0",deliveryAmountForCreditOrders ="0";
    ListView consolidatedSalesReport_Listview;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";



    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;


    TMCTestTableInterface tmcTestTableInterface = null;
    TMCTestTable_AsyncTask tmcTestTable_asyncTask;
    boolean  isTMCTestTableServiceCalled = false;
    ArrayList<String> repeatedOrderid =  new ArrayList<>();
    LinearLayout orders_pack_layout,final_salesLayout,storename_totalSale_layout;
    View dividerbelowheader,exportreportDivider;

    double totalappMarkupPercentageValue =0,totalPhoneMarkupPercentageValue =0 , totalCreditMarkupPercentageValue =0; 
    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
    boolean localDBcheck = false;
    TMCSubCtgyItemSQL_DB_Manager tmcSubCtgyItemSQL_db_manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidated_report_sub_ctgywise);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        exportreportDivider= findViewById(R.id.exportreportDivider);
        dividerbelowheader = findViewById(R.id.dividerbelowheader);
        storename_totalSale_layout = findViewById(R.id.storename_totalSale_layout);
        final_salesLayout  = findViewById(R.id.final_salesLayout);
        orders_pack_layout = findViewById(R.id.orders_pack_layout);
        phoneProductSales = findViewById(R.id.phoneProductSales);
        totalPhone_MarkupAmount = findViewById(R.id.totalPhone_MarkupAmount);
        vendorName = findViewById(R.id.vendorName);
        totalAppSales = findViewById(R.id.totalAppSales);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        consolidatedSalesReport_Listview = findViewById(R.id.consolidatedSalesReport_Listview);
        generateReport_Layout = findViewById(R.id.generateReport_Layout);
        totalAmt_without_GST = findViewById(R.id.totalAmt_without_GST);
        totalCouponDiscount_Amt = findViewById(R.id.totalCouponDiscount_Amt);
        refundAmount_textwidget = findViewById(R.id.refundAmount_textwidget);
        replacementAmount_textwidget = findViewById(R.id.replacementAmount_textwidget);
        wholesalesOrderSales = findViewById(R.id.wholesalesOrderSales);
        instruction_textview = findViewById(R.id.instruction_textview);
        fetchData_Layout = findViewById(R.id.fetchData_Layout);
        deliveryCharge_label = findViewById(R.id.deliveryCharge_label);

        totalApp_MarkupAmount = findViewById(R.id.totalApp_MarkupAmount);

        totalAmt_with_CouponDiscount = findViewById(R.id.totalAmt_with_CouponDiscount);
        totalGST_Amt = findViewById(R.id.totalGST_Amt);
        final_sales = findViewById(R.id.final_sales);
        appsales = findViewById(R.id.appSales);
        possales = findViewById(R.id.posSales);
        creditSales = findViewById(R.id.creditSales);
        swiggySales = findViewById(R.id.swiggySales);
        phoneOrderSales = findViewById(R.id.phoneOrderSales);
        dunzoSales = findViewById(R.id.dunzoSales);
        bigbasketSales = findViewById(R.id.bigbasketSales);
        totalSales_headingText = findViewById(R.id.totalRating_headingText);
        scrollView = findViewById(R.id.scrollView);
        noofPacks = findViewById(R.id.noofPacks);
        noofOrders = findViewById(R.id.noofOrders);
        deliveryChargeAmount_textwidget = findViewById(R.id.deliveryChargeAmount_textwidget);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        generateSubCtgyReport_Layout = findViewById(R.id.generateSubCtgyReport_Layout);
        creditMarkupAmount = findViewById(R.id.creditMarkupAmount);
        creditProductSales = findViewById(R.id.creditProductSales);


        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        tmcSubCtgykey = new ArrayList<>();
        SubCtgyKey_List = new ArrayList<>();
        SubCtgywiseTotalArray = new ArrayList<>();
        couponDiscountOrderidArray = new ArrayList<>();
        creditOrders_couponDiscountOrderidArray = new ArrayList<>();
        creditOrderdeliveryChargeOrderidArray = new ArrayList<>();

        deliveryChargeOrderidArray = new ArrayList<>();
        phoneorderdeliveryChargeOrderidArray  = new ArrayList<>();
        
        Pos_couponDiscountOrderidArray = new ArrayList<>();
        phoneOrders_couponDiscountOrderidArray = new ArrayList<>();
        swiggyOrders_couponDiscountOrderidArray = new ArrayList<>();
        dunzoOrders_couponDiscountOrderidArray = new ArrayList<>();
        bigBasketOrders_couponDiscountOrderidArray = new ArrayList<>();
        wholesaleOrders_couponDiscountOrderidArray  = new ArrayList<>();
        ordertypeArray = new ArrayList<>();

        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        ordertypeArray.clear();
        ordertypeHashmap.clear();
        couponDiscount_hashmap.clear();
        couponDiscountOrderidArray.clear();
        creditOrders_couponDiscount_hashmap.clear();
        creditOrders_couponDiscountOrderidArray.clear();
        creditOrderdeliveryChargeOrderidArray.clear();
        creditOrderdeliveryCharge_hashmap.clear();
        Pos_couponDiscountOrderidArray.clear();
        phoneOrders_couponDiscount_hashmap.clear();
        phoneOrders_couponDiscountOrderidArray.clear();
        swiggyOrders_couponDiscount_hashmap.clear();
        swiggyOrders_couponDiscountOrderidArray.clear();
        dunzoOrders_couponDiscount_hashmap.clear();
        dunzoOrders_couponDiscountOrderidArray.clear();
        bigBasketOrders_couponDiscountOrderidArray.clear();
        bigBasketOrders_couponDiscount_hashmap.clear();
        wholesaleOrders_couponDiscountOrderidArray.clear();
        wholesaleOrders_couponDiscount_hashmap.clear();

        deliveryCharge_hashmap.clear();
        deliveryCharge_hashmap.clear();
        deliveryChargeOrderidArray.clear();
        phoneorderdeliveryChargeOrderidArray.clear();
        phoneorderdeliveryCharge_hashmap.clear();
        phoneorderdeliveryChargeOrderidArray.clear();
        phoneorderdeliveryCharge_hashmap.clear();
        creditOrders_couponDiscount_hashmap.clear();
        creditOrders_couponDiscountOrderidArray.clear();
        creditOrderdeliveryChargeOrderidArray.clear();
        creditOrderdeliveryCharge_hashmap.clear();
        SubCtgywiseTotalArray.clear();
        SubCtgywiseTotalHashmap.clear();
        SubCtgywiseWeightHashmap.clear();
        SubCtgywiseQuantityHashmap.clear();
        
        tmcSubCtgykey.clear();
        no_of_ItemCount = 0;
        totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;


        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(ConsolidatedReportSubCtgywise.this);
         //   Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
             //   Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }

        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        vendorKey = sharedPreferences.getString("VendorKey", "");
        vendorname = sharedPreferences.getString("VendorName", "");

        StoreAddressLine1 = (sharedPreferences.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (sharedPreferences.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (sharedPreferences.getString("VendorPincode", ""));
        StoreLanLine = (sharedPreferences.getString("VendorMobileNumber", ""));
        orderdetailsnewschema = (sharedPreferences.getBoolean("orderdetailsnewschema_settings", false));
        //orderdetailsnewschema = true;
        localDBcheck = (sharedPreferences.getBoolean("localdbcheck", false));

        vendorName.setText(vendorname);
        if(localDBcheck) {
            getDataFromSQL("FetchTMCMenuItem");
            getDataFromSQL("FetchTMCSubCtgyItem");

        }
        else{
            getMenuItemArrayFromSharedPreferences();
            getTmcSubCtgyList(vendorKey);

        }
        if(screenInches>Constants.default_mobileScreenSize){
            dividerbelowheader.setVisibility(View.GONE);
            generateReport_Layout.setVisibility(View.GONE);
            exportreportDivider.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 30);
            storename_totalSale_layout.setLayoutParams(layoutParams);
            storename_totalSale_layout.setOrientation(LinearLayout.HORIZONTAL);
        }
        else{
            dividerbelowheader.setVisibility(View.VISIBLE);
            generateReport_Layout.setVisibility(View.VISIBLE);
            exportreportDivider.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
            storename_totalSale_layout.setLayoutParams(layoutParams);
            storename_totalSale_layout.setOrientation(LinearLayout.VERTICAL);
        }

        if (vendorKey.equals("vendor_5")) {

            generateSubCtgyReport_Layout.setVisibility(View.VISIBLE);
        }
        else{
            generateSubCtgyReport_Layout.setVisibility(View.GONE);
        }






        /*
        CurrentDate = getDate_and_time();
        dateSelector_text.setText(CurrentDate);
        DateString = getDate_and_time();
        PreviousDateString = getDatewithNameofthePreviousDay();
        startDateString_forReplacementransaction = getstartDate_and_time_TransactionTable();
        endDateString_forReplacementransaction = getendDate_and_time_TransactionTable();

 */
        CurrentDate ="";
        DateString ="";
        dateSelector_text.setText(Constants.Empty_Date_Format);

        scrollView.setVisibility(View.GONE);
        instruction_textview.setVisibility(View.VISIBLE);


        try {
            Order_Item_List.clear();
            OrderItem_hashmap.clear();
            finalBillDetails.clear();
            FinalBill_hashmap.clear();
            couponDiscountOrderidArray.clear();
            ordertypeArray.clear();
            ordertypeHashmap.clear();
            couponDiscount_hashmap.clear();
            creditOrders_couponDiscount_hashmap.clear();
            creditOrders_couponDiscountOrderidArray.clear();
             creditOrderdeliveryChargeOrderidArray.clear();
             creditOrderdeliveryCharge_hashmap.clear();
            Pos_couponDiscountOrderidArray.clear();
            phoneOrders_couponDiscount_hashmap.clear();
            phoneOrders_couponDiscountOrderidArray.clear();
            swiggyOrders_couponDiscount_hashmap.clear();
            swiggyOrders_couponDiscountOrderidArray.clear();
            wholesaleOrders_couponDiscountOrderidArray.clear();
            wholesaleOrders_couponDiscount_hashmap.clear();
            replacementTransactiontypeHashmap.clear();
            replacementTransactiontypeArray.clear();

            dunzoOrders_couponDiscount_hashmap.clear();
            dunzoOrders_couponDiscountOrderidArray.clear();
            bigBasketOrders_couponDiscountOrderidArray.clear();
            bigBasketOrders_couponDiscount_hashmap.clear();
            SubCtgywiseTotalArray.clear();
            SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
            dataList.clear();
            tmcSubCtgykey.clear();
            deliveryCharge_hashmap.clear();
            deliveryChargeOrderidArray.clear();
            phoneorderdeliveryChargeOrderidArray.clear();
            phoneorderdeliveryCharge_hashmap.clear();
            no_of_ItemCount = 0;
            no_of_orders = 0;
            totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

          //  getOrderForSelectedDate(PreviousDateString, DateString, vendorKey);
           // getdataFromReplacementTransaction(startDateString_forReplacementransaction, endDateString_forReplacementransaction, vendorKey);

        } catch (Exception e) {
            e.printStackTrace();
        }


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ConsolidatedReportSubCtgywise.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });


        dateSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openDatePicker();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        fetchData_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DateString.equals("")){
                    Toast.makeText(ConsolidatedReportSubCtgywise.this, "First Select Date !! Before Fetch the Data", Toast.LENGTH_SHORT).show();
                }
                else {


                Adjusting_Widgets_Visibility(true);

                scrollView.setVisibility(View.VISIBLE);
                instruction_textview.setVisibility(View.GONE);

                Order_Item_List.clear();
                OrderItem_hashmap.clear();
                finalBillDetails.clear();
                FinalBill_hashmap.clear();
                couponDiscountOrderidArray.clear();
                ordertypeArray.clear();
                ordertypeHashmap.clear();
                couponDiscount_hashmap.clear();
                dunzoOrders_couponDiscount_hashmap.clear();
                dunzoOrders_couponDiscountOrderidArray.clear();
                oldpayableamount = 0;
                itemDespTotalAmount = 0;
                creditOrders_couponDiscount_hashmap.clear();
                 creditOrders_couponDiscountOrderidArray.clear();
                creditOrderdeliveryChargeOrderidArray.clear();
                creditOrderdeliveryCharge_hashmap.clear();
                Pos_couponDiscountOrderidArray.clear();
                phoneOrders_couponDiscount_hashmap.clear();
                phoneOrders_couponDiscountOrderidArray.clear();
                swiggyOrders_couponDiscount_hashmap.clear();
                swiggyOrders_couponDiscountOrderidArray.clear();
                deliveryCharge_hashmap.clear();
                deliveryCharge_hashmap.clear();
                deliveryChargeOrderidArray.clear();
                 phoneorderdeliveryChargeOrderidArray.clear();
                phoneorderdeliveryCharge_hashmap.clear();
                bigBasketOrders_couponDiscountOrderidArray.clear();
                bigBasketOrders_couponDiscount_hashmap.clear();
                wholesaleOrders_couponDiscountOrderidArray.clear();
                wholesaleOrders_couponDiscount_hashmap.clear();
                replacementTransactiontypeHashmap.clear();
                replacementTransactiontypeArray.clear();
                    SubCtgywiseTotalArray.clear();
                    SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();

                dataList.clear();
                no_of_ItemCount = 0;
                no_of_orders = 0;
                totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);

                isgetPreOrderForSelectedDateCalled = false;
                isgetOrderForSelectedDateCalled = false;
                isgetReplacementOrderForSelectedDateCalled = false;
                //getOrderForSelectedDate(DateString, vendorKey);
                    /*if(vendorKey.equals("vendor_5")){
                        String dateAsnewFormat = convertOldFormatDateintoNewFormat(DateString);
                        Call_andGetValueFromTMCTestTable(dateAsnewFormat, dateAsnewFormat, vendorKey);

                    }
                    else {

                     */
                        if (orderdetailsnewschema) {
                            String dateAsnewFormat = convertOldFormatDateintoNewFormat(DateString);
                            callVendorOrderDetailsSeviceAndInitCallBack(dateAsnewFormat, dateAsnewFormat, vendorKey);
                            getdataFromReplacementTransaction(startDateString_forReplacementransaction, endDateString_forReplacementransaction, vendorKey);

                        } else {

                            getOrderForSelectedDate(PreviousDateString, DateString, vendorKey);
                            getdataFromReplacementTransaction(startDateString_forReplacementransaction, endDateString_forReplacementransaction, vendorKey);

                        }
                    //}



                   }
            }
        });

        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SDK_INT >= Build.VERSION_CODES.R) {

                    if (Environment.isExternalStorageManager()) {
                        try {
                            if(Order_Item_List.size()>0){
                                Adjusting_Widgets_Visibility(true);

                                exportReport();
                            }
                            else{
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Data to Export", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    } else {
                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                            startActivityForResult(intent, 2296);
                        } catch (Exception e) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 2296);
                        }
                    }

                } else {


                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(ConsolidatedReportSubCtgywise.this, WRITE_EXTERNAL_STORAGE);
                    //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                    // If do not grant write external storage permission.
                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        // Request user to grant write external storage permission.
                        ActivityCompat.requestPermissions(ConsolidatedReportSubCtgywise.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                    } else {
                        try {
                            if(Order_Item_List.size()>0){
                                Adjusting_Widgets_Visibility(true);

                                exportReport();
                            }
                            else{
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Data to Export", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    }
                }
            }
        });
        generateSubCtgyReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SDK_INT >= Build.VERSION_CODES.R) {

                    if (Environment.isExternalStorageManager()) {
                        try {
                            if(SubCtgywiseTotalArray.size()>0){
                                Adjusting_Widgets_Visibility(true);

                                exportSubCtgyWiseReport();
                            }
                            else{
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Data to Export", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    } else {
                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                            startActivityForResult(intent, 2296);
                        } catch (Exception e) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 2296);
                        }
                    }

                } else {


                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(ConsolidatedReportSubCtgywise.this, WRITE_EXTERNAL_STORAGE);
                    //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                    // If do not grant write external storage permission.
                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        // Request user to grant write external storage permission.
                        ActivityCompat.requestPermissions(ConsolidatedReportSubCtgywise.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                    } else {
                        try {
                            if(Order_Item_List.size()>0){
                                Adjusting_Widgets_Visibility(true);

                                exportSubCtgyWiseReport();
                            }
                            else{
                                Adjusting_Widgets_Visibility(false);
                                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Data to Export", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    }
                }
            }
        });

    }



    private void Call_andGetValueFromTMCTestTable(String dateAsnewFormat, String dateAsnewFormat1, String vendorKey) {
        Adjusting_Widgets_Visibility(true);

        if (isTMCTestTableServiceCalled) {
            Adjusting_Widgets_Visibility(false);
            return;
        }
        isTMCTestTableServiceCalled = true;
        isReplacementTransacDetailsResponseReceivedForSelectedDate = true;
        isgetReplacementOrderForSelectedDateCalled = true;

         HashMap<String, Modal_TMCTestTable> menuitemwisetotalhashmap = new HashMap();
         ArrayList <String> menuitemId_array = new ArrayList<>();


        List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse = new ArrayList<>();
        tmcTestTableInterface = new TMCTestTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_TMCTestTable> list) {



                for(int i =0; i<list.size(); i ++) {
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");


                    Modal_TMCTestTable modal_tmcTestTable = list.get(i);
                    String subCtgyKey = String.valueOf(modal_tmcTestTable.getSubctgykey());
                    String tmcprice_string = "", menuitemid = "", weight_String = "";
                    double tmcprice = 0, weight = 0;
                    try {
                        tmcprice_string = String.valueOf(modal_tmcTestTable.getPrice());
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    try {
                        tmcprice_string = tmcprice_string.replaceAll("[^\\d.]", "");
                        if (tmcprice_string.equals("")) {
                            tmcprice_string = "0";
                        }
                        tmcprice = Double.parseDouble(tmcprice_string);
                    } catch (Exception e) {
                        tmcprice = Double.parseDouble("0");

                        e.printStackTrace();
                    }


                    try {
                        weight_String = String.valueOf(modal_tmcTestTable.getWeight());
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                    try {
                        weight_String = weight_String.replaceAll("[^\\d.]", "");
                        if (weight_String.equals("")) {
                            weight_String = "0";
                        }
                        weight = Double.parseDouble(weight_String);
                    } catch (Exception e) {
                        weight = Double.parseDouble("0");

                        e.printStackTrace();
                    }


                    try {
                        if (SubCtgywiseTotalArray.contains(subCtgyKey)) {
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(subCtgyKey);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                String SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(subCtgyKey);
                                double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                SubCtgywisetotalDouble = SubCtgywisetotalDouble + tmcprice;
                                SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                if (SDK_INT >= Build.VERSION_CODES.N) {
                                    SubCtgywiseTotalHashmap.replace(subCtgyKey, SubCtgywisetotalString);
                                } else {
                                    SubCtgywiseTotalHashmap.remove(subCtgyKey);
                                    SubCtgywiseTotalHashmap.put(subCtgyKey, SubCtgywisetotalString);
                                }
                            } else {
                                SubCtgywiseTotalHashmap.put(subCtgyKey, String.valueOf(tmcprice));

                            }
                        } else {
                            SubCtgywiseTotalArray.add(subCtgyKey);
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(subCtgyKey);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                String SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(subCtgyKey);
                                double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                SubCtgywisetotalDouble = SubCtgywisetotalDouble + tmcprice;
                                SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                if (SDK_INT >= Build.VERSION_CODES.N) {
                                    SubCtgywiseTotalHashmap.replace(subCtgyKey, SubCtgywisetotalString);
                                } else {
                                    SubCtgywiseTotalHashmap.remove(subCtgyKey);
                                    SubCtgywiseTotalHashmap.put(subCtgyKey, SubCtgywisetotalString);
                                }
                            } else {
                                SubCtgywiseTotalHashmap.put(subCtgyKey, String.valueOf(tmcprice));

                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if (!menuitemId_array.contains(menuitemid)) {
                            menuitemId_array.add(menuitemid);
                            menuitemwisetotalhashmap.put(menuitemid, modal_tmcTestTable);
                        } else {
                            Modal_TMCTestTable modal_tmcTestTable1 = menuitemwisetotalhashmap.get(menuitemid);
                            double tmcprice_insideloop = 0, weight_insideloop = 0;
                            String tmcprice_String_insideloop = "0", weight_String_inside_loop = "0";
                            try {
                                weight_String_inside_loop = String.valueOf(modal_tmcTestTable1.getWeight());
                            } catch (Exception e) {

                                e.printStackTrace();
                            }

                            try {
                                weight_String_inside_loop = weight_String_inside_loop.replaceAll("[^\\d.]", "");
                                if (weight_String_inside_loop.equals("")) {
                                    weight_String_inside_loop = "0";
                                }
                                weight_insideloop = Double.parseDouble(weight_String_inside_loop);
                            } catch (Exception e) {
                                weight_insideloop = Double.parseDouble("0");
                                e.printStackTrace();
                            }

                            try {
                                weight_insideloop = weight_insideloop + weight;


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                weight_insideloop = weight_insideloop + weight;


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                modal_tmcTestTable1.setWeight(String.valueOf(weight_insideloop));


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                tmcprice_String_insideloop = String.valueOf(modal_tmcTestTable1.getPrice());
                            } catch (Exception e) {

                                e.printStackTrace();
                            }
                            try {
                                tmcprice_String_insideloop = tmcprice_String_insideloop.replaceAll("[^\\d.]", "");
                                if (tmcprice_String_insideloop.equals("")) {
                                    tmcprice_String_insideloop = "0";
                                }
                                tmcprice_insideloop = Double.parseDouble(tmcprice_String_insideloop);
                            } catch (Exception e) {
                                tmcprice_insideloop = Double.parseDouble("0");

                                e.printStackTrace();
                            }
                            try {
                                tmcprice_insideloop = tmcprice_insideloop + tmcprice;


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                modal_tmcTestTable1.setPrice(String.valueOf(tmcprice_insideloop));


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                if (SDK_INT >= Build.VERSION_CODES.N) {
                                    menuitemwisetotalhashmap.replace(menuitemid, modal_tmcTestTable1);
                                } else {
                                    menuitemwisetotalhashmap.remove(menuitemid);
                                    menuitemwisetotalhashmap.put(menuitemid, modal_tmcTestTable1);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (list.size() - 1 == i) {
                        try {
                            dataList.clear();
                            try {
                                Collections.sort(tmcSubCtgykey, new Comparator<String>() {
                                    public int compare(final String object1, final String object2) {
                                        return object1.compareTo(object2);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            for (String SubCtgykey : tmcSubCtgykey) {
                                int i_value = 0;
                                String subCtgyTotal = "0";
                                double subCtgyTotaldouble = 0;
                                //    Log.d(Constants.TAG, "SubCtgykey " + SubCtgykey);

                                String SubCtgyName = "", ItemName_Quantity_weight, Price, menuid;
                                Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
                                try {
                                    subCtgyTotal = SubCtgywiseTotalHashmap.get(SubCtgykey);
                                    subCtgyTotaldouble = Double.parseDouble(subCtgyTotal);
                                    subCtgyTotal = decimalFormat.format(subCtgyTotaldouble);
                                } catch (Exception e) {
                                    subCtgyTotal = "0";
                                    e.printStackTrace();
                                }
                                try {
                                    SubCtgyName = Objects.requireNonNull(subCtgyName_object).getTmcsubctgyname();
                                } catch (Exception e) {
                                    SubCtgyName = "";
                                    // Log.d(Constants.TAG, "before for " + e.getMessage());

                                }
                                for (int j = 0; j < menuitemId_array.size(); j++) {
                                    menuid = menuitemId_array.get(j);
                                    //  Log.d(Constants.TAG, "SubCtgykey menuid " + menuid);
                                    //  Log.d(Constants.TAG, "SubCtgykey w " + SubCtgykey);
                                    Log.d(Constants.TAG, "menuitemid:hash4 :" + "#" + String.valueOf(menuid) + "#");

                                    Modal_TMCTestTable itemDetailsfromHashmap = menuitemwisetotalhashmap.get(menuid);
                                    // Log.d(Constants.TAG, "SubCtgykey itemDetailsfromHashmap " + itemDetailsfromHashmap.getItemname());
                                    String subCtgyKey_fromHashmap = "";
                                    try {
                                        subCtgyKey_fromHashmap = itemDetailsfromHashmap.getSubctgykey();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //        Log.d(Constants.TAG, "SubCtgykey subCtgyKey_fromHashmap " + subCtgyKey_fromHashmap );

                                    if (i_value != 0) {
                                        if (subCtgyKey_fromHashmap.equals(SubCtgykey)) {
                                            double weightinGrams = 0;

                                            try {
                                                if ((!(itemDetailsfromHashmap.getWeight().equals(""))) && ((itemDetailsfromHashmap.getWeight() != (null)))) {
                                                    weightinGrams = Double.parseDouble(Objects.requireNonNull(itemDetailsfromHashmap).getWeight());

                                                }

                                            } catch (Exception e) {
                                                weightinGrams = 0;
                                            }
                                            String itemname = String.valueOf(itemDetailsfromHashmap.getItemname());

                                            if (itemname.equals("Fresh Goat Meat - Curry Cut")) {
                                                Log.i("TAG", "Key : " + String.valueOf(itemDetailsfromHashmap.getMenuitemid()));
                                            }
                                            double kilogram = weightinGrams * 0.001;
                                            String KilogramString = String.valueOf(decimalFormat.format(kilogram) + "Kg");

                                            ListItem listItem = new ListItem();
                                            if (KilogramString != null && (!KilogramString.equals("")) && (!(KilogramString.equals("0.00Kg")))) {
                                                listItem.setMessage(itemDetailsfromHashmap.getItemname() + " - " + KilogramString);
                                                listItem.setMessageLine2(String.valueOf(decimalFormat.format(Double.parseDouble(itemDetailsfromHashmap.getPrice()))));
                                                dataList.add(listItem);
                                            } else {
                                                listItem.setMessage(itemDetailsfromHashmap.getItemname() + " ( " + itemDetailsfromHashmap.getQuantity() + " ) ");

                                                listItem.setMessageLine2(String.valueOf(decimalFormat.format(Double.parseDouble(itemDetailsfromHashmap.getPrice()))));
                                                dataList.add(listItem);
                                            }
                                        }
                                    }
                                    if (i_value == 0) {
                                        i_value = 1;
                                        j = j - 1;
                                        ListSection listSection = new ListSection();
                                        try {
                                            if (!listSection.getTitle().equals(SubCtgyName)) {
                                                listSection.setTitle(SubCtgyName);
                                                listSection.setTotalAmount(String.valueOf(subCtgyTotal));
                                                dataList.add(listSection);

                                            }
                                        } catch (Exception e) {
                                            listSection.setTitle(SubCtgyName);
                                            listSection.setTotalAmount(String.valueOf(subCtgyTotal));

                                            dataList.add(listSection);

                                        }
                                    }


                                }
                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }


                        setAdapter();

                        orders_pack_layout.setVisibility(View.GONE);
                        final_salesLayout.setVisibility(View.GONE);

                    }

                }




              /*
                for (int i = 0; i < list.size(); i++) {
                    Modal_TMCTestTable modal_tmcTestTable = list.get(i);
                    Modal_ManageOrders_Pojo_Class modal_orderDetails = new Modal_ManageOrders_Pojo_Class();
                    JSONArray itemdesp_JSONArray = new JSONArray();


                    if (!repeatedOrderid.contains(modal_tmcTestTable.getOrderid())) {


                        try {

                            modal_orderDetails.vendorkey = modal_tmcTestTable.getVendorkey();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.vendorkey = "";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.orderid = modal_tmcTestTable.getOrderid();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.orderid = "";

                            e.printStackTrace();
                        }

                        try {

                            modal_orderDetails.coupondiscamount = modal_tmcTestTable.getDiscount();
                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.coupondiscamount = "0";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.deliveryamount = "0";

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.deliveryamount = "0";

                            e.printStackTrace();
                        }

                        try {

                            modal_orderDetails.coupondiscamount = modal_tmcTestTable.getDiscount();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.coupondiscamount = "0";

                            e.printStackTrace();
                        }
                        try {

                            modal_orderDetails.GstAmount = "0";

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.GstAmount = "0";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.vendorkey = modal_tmcTestTable.getVendorkey();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.vendorkey = "";

                            e.printStackTrace();
                        }

                        for (int j = 0; j < list.size(); j++) {
                            Modal_TMCTestTable secondModal_testTable = list.get(j);
                            if (modal_tmcTestTable.getOrderid().equals(secondModal_testTable.getOrderid())) {
                                JSONObject jsonObject = new JSONObject();

                                try {
                                    jsonObject.put("applieddiscpercentage", "0");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    jsonObject.put("menuitemid", String.valueOf(secondModal_testTable.getMenuitemid()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                String grossWeightingramsString = String.valueOf(secondModal_testTable.getWeight()).replaceAll("[^\\d.]", "");

                                try {
                                    jsonObject.put("grossweight", grossWeightingramsString + "g");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    jsonObject.put("grossweight", grossWeightingramsString);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    jsonObject.put("gstamount", "0");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    jsonObject.put("itemname", modal_tmcTestTable.getItemname());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    jsonObject.put("netweight", grossWeightingramsString + "g");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    jsonObject.put("portionsize", "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    jsonObject.put("quantity", modal_tmcTestTable.getQuantity());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    jsonObject.put("tmcprice", modal_tmcTestTable.getPrice());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    jsonObject.put("pricetype", modal_tmcTestTable.getPricetype());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    jsonObject.put("tmcsubctgykey", modal_tmcTestTable.getSubctgykey());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                itemdesp_JSONArray.put(jsonObject);
                            }
                        }

                        try {

                            modal_orderDetails.itemdesp = itemdesp_JSONArray;

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.itemdesp = new JSONArray();

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.orderplaceddate = modal_tmcTestTable.getSlotdate();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.orderplaceddate = "";

                            e.printStackTrace();
                        }

                        try {

                            modal_orderDetails.orderplacedtime = modal_tmcTestTable.getSlotdate();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.orderplacedtime = "";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.ordertype = modal_tmcTestTable.getOrdertype();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.ordertype = "";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.payableamount = modal_tmcTestTable.getPrice();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.payableamount = "";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.paymentmode = "CASH ON DELIVERY";

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.paymentmode = "";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.slotdate = modal_tmcTestTable.getSlotdate();

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.slotdate = "";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.slotname = "EXPRESSDELIVERY";

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.slotname = "EXPRESSDELIVERY";

                            e.printStackTrace();
                        }


                        try {

                            modal_orderDetails.slottimerange = "120 mins";

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.slottimerange = "";

                            e.printStackTrace();
                        }


                        repeatedOrderid.add(modal_tmcTestTable.getOrderid());
                        orderslist_fromResponse.add(modal_orderDetails);

                    }


                    if ((list.size() - 1) == i) {
                        if(orderslist_fromResponse.size()>0) {

                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("content", orderslist_fromResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            isVendorOrdersTableServiceCalled = false;
                            //mobile_jsonString = String.valueOf(jsonObject);
                            // ordersList = orderslist_fromResponse;
                            processArrayAndgetData(orderslist_fromResponse);
                        }
                        else{

                            Adjusting_Widgets_Visibility(false);
                            isVendorOrdersTableServiceCalled =false;
                            scrollView.setVisibility(View.GONE);
                            instruction_textview.setVisibility(View.VISIBLE);
                            instruction_textview.setText("There is no Order On this Date");
                            Order_Item_List.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            couponDiscountOrderidArray.clear();
                            ordertypeArray.clear();
                            ordertypeHashmap.clear();
                            couponDiscount_hashmap.clear();
                            oldpayableamount = 0;
                            itemDespTotalAmount = 0;
                            creditOrders_couponDiscount_hashmap.clear();
                            creditOrders_couponDiscountOrderidArray.clear();
                            creditOrderdeliveryChargeOrderidArray.clear();
                            creditOrderdeliveryCharge_hashmap.clear();
                            Pos_couponDiscount_hashmap.clear();
                            Pos_couponDiscountOrderidArray.clear();
                            phoneOrders_couponDiscount_hashmap.clear();
                            phoneOrders_couponDiscountOrderidArray.clear();
                            swiggyOrders_couponDiscount_hashmap.clear();
                            swiggyOrders_couponDiscountOrderidArray.clear();
                            dunzoOrders_couponDiscount_hashmap.clear();
                            dunzoOrders_couponDiscountOrderidArray.clear();
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                            dataList.clear();
                            tmcSubCtgykey.clear();
                            deliveryCharge_hashmap.clear();
                            deliveryCharge_hashmap.clear();
                            deliveryChargeOrderidArray.clear();
                            phoneorderdeliveryChargeOrderidArray.clear();
                            phoneorderdeliveryCharge_hashmap.clear();
                            wholesaleOrders_couponDiscountOrderidArray.clear();
                            wholesaleOrders_couponDiscount_hashmap.clear();
                            bigBasketOrders_couponDiscountOrderidArray.clear();
                            bigBasketOrders_couponDiscount_hashmap.clear();
                            no_of_ItemCount = 0;
                            no_of_orders = 0;
                            totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                            ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            //runthread();
                            Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);
                        }
                    }


                }
           */
            }

            @Override
            public void notifyError(String error) {
                Adjusting_Widgets_Visibility(false);
                isVendorOrdersTableServiceCalled =false;
                scrollView.setVisibility(View.GONE);
                instruction_textview.setVisibility(View.VISIBLE);
                instruction_textview.setText("There is some error"+String.valueOf(error));
                Order_Item_List.clear();
                OrderItem_hashmap.clear();
                finalBillDetails.clear();
                FinalBill_hashmap.clear();
                couponDiscountOrderidArray.clear();
                ordertypeArray.clear();
                ordertypeHashmap.clear();
                couponDiscount_hashmap.clear();
                oldpayableamount = 0;
                itemDespTotalAmount = 0;
                creditOrders_couponDiscount_hashmap.clear();
                creditOrders_couponDiscountOrderidArray.clear();
                creditOrderdeliveryChargeOrderidArray.clear();
                creditOrderdeliveryCharge_hashmap.clear();
                Pos_couponDiscount_hashmap.clear();
                Pos_couponDiscountOrderidArray.clear();
                phoneOrders_couponDiscount_hashmap.clear();
                phoneOrders_couponDiscountOrderidArray.clear();
                swiggyOrders_couponDiscount_hashmap.clear();
                swiggyOrders_couponDiscountOrderidArray.clear();
                dunzoOrders_couponDiscount_hashmap.clear();
                dunzoOrders_couponDiscountOrderidArray.clear();
                SubCtgywiseTotalArray.clear();
                SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                dataList.clear();
                tmcSubCtgykey.clear();
                deliveryCharge_hashmap.clear();
                deliveryCharge_hashmap.clear();
                deliveryChargeOrderidArray.clear();
                phoneorderdeliveryChargeOrderidArray.clear();
                phoneorderdeliveryCharge_hashmap.clear();
                wholesaleOrders_couponDiscountOrderidArray.clear();
                wholesaleOrders_couponDiscount_hashmap.clear();
                bigBasketOrders_couponDiscountOrderidArray.clear();
                bigBasketOrders_couponDiscount_hashmap.clear();
                no_of_ItemCount = 0;
                no_of_orders = 0;
                totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                //runthread();
                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is Some Error ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                isVendorOrdersTableServiceCalled = false;
            }




        };

        String url = Constants.api_GetTMCOrderDetails_Safii+"?vendorkey="+vendorKey+"&fromslotdate="+dateAsnewFormat+"&toslotdate="+dateAsnewFormat1;

        TMCTestTable_AsyncTask asyncTask=new TMCTestTable_AsyncTask(getApplication(), tmcTestTableInterface,url);
        asyncTask.execute(url);

    }

    @SuppressLint("Range")
    private void getDataFromSQL(String processToDo) {
        try {
            if (processToDo.equals("FetchTMCMenuItem")) {
                if (tmcMenuItemSQL_db_manager == null) {
                    tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(ConsolidatedReportSubCtgywise.this);
                    try {
                        tmcMenuItemSQL_db_manager.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                try {
                    Cursor cursor = tmcMenuItemSQL_db_manager.Fetch();
                    MenuItem.clear();
                    try {
                        // if (cursor.moveToFirst()) {

                        Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                        Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                        if (cursor.getCount() > 0) {

                            if (cursor.moveToFirst()) {
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


                                    MenuItem.add(modal_newOrderItems);
                                }
                                while (cursor.moveToNext());


                            }


                        }
                        else {
                            Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no menuItem Please Refresh the App", Toast.LENGTH_SHORT).show();

                        }


                        //  }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (processToDo.equals("FetchTMCSubCtgyItem")) {
                if (tmcSubCtgyItemSQL_db_manager == null) {

                    tmcSubCtgyItemSQL_db_manager = new TMCSubCtgyItemSQL_DB_Manager(this);
                    try {
                        tmcSubCtgyItemSQL_db_manager.open();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                try {
                    Cursor cursor = tmcSubCtgyItemSQL_db_manager.Fetch();
                    try {
                        if (cursor.getCount() > 0) {

                            if (cursor.moveToFirst()) {
                                do {
                                    try {

                                        String subCtgyKey = String.valueOf(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.key)));
                                        String subCtgyName = String.valueOf(cursor.getString(cursor.getColumnIndex(TMCSubCtgyItemSQL_DB_Manager.subctgyname)));
                                        Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                        modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                        modal_orderDetails.tmcsubctgyname = subCtgyName;
                                        SubCtgyKey_hashmap.put(subCtgyKey, modal_orderDetails);


                                    } catch (Exception e) {
                                        e.printStackTrace();


                                    }
                                }
                                while (cursor.moveToNext());


                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                String subCtgyKey = String.valueOf("Miscellaneous");
                                String subCtgyName = String.valueOf("Miscellaneous Item");

                                modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                modal_orderDetails.tmcsubctgyname = subCtgyName;
                                SubCtgyKey_hashmap.put(subCtgyKey, modal_orderDetails);


                            }
                        } else {
                            getTmcSubCtgyList(vendorKey);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{

            try{
                if (processToDo.equals("FetchTMCMenuItem")) {

                    if (tmcMenuItemSQL_db_manager != null) {
                        tmcMenuItemSQL_db_manager.close();
                        tmcMenuItemSQL_db_manager = null;
                    }
                }
                else if (processToDo.equals("FetchTMCSubCtgyItem")) {

                    if (tmcSubCtgyItemSQL_db_manager != null) {
                        tmcSubCtgyItemSQL_db_manager.close();
                        tmcSubCtgyItemSQL_db_manager = null;
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }





    }

    private void getdataFromReplacementTransaction(String startdateString_forReplacementransaction, String enddateString_forReplacementransaction, String vendorKey) {
        if (isgetReplacementOrderForSelectedDateCalled) {
            return;
        }
        isgetReplacementOrderForSelectedDateCalled = true;
        isReplacementTransacDetailsResponseReceivedForSelectedDate = false;
        Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetReplacementTransactionDetailsForTransactionTimeVendorkey + "?transactiontime1=" + startdateString_forReplacementransaction + "&vendorkey=" + vendorKey + "&transactiontime2=" + enddateString_forReplacementransaction, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            JSONArray JArray = response.getJSONArray("content");
                            if (JArray.length() > 0) {
                                replacementOrderDetailsString = JArray.toString();
                                convertReplacementTransactionDetailsJsonIntoArray(replacementOrderDetailsString);
                            } else {
                                isReplacementTransacDetailsResponseReceivedForSelectedDate = true;
                            }

                        } catch (JSONException e) {
                            isReplacementTransacDetailsResponseReceivedForSelectedDate = true;
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Orders Yet ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                isgetReplacementOrderForSelectedDateCalled = false;
                isReplacementTransacDetailsResponseReceivedForSelectedDate = true;
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", vendorKey);

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
        Volley.newRequestQueue(ConsolidatedReportSubCtgywise.this).add(jsonObjectRequest);


    }

    private void convertReplacementTransactionDetailsJsonIntoArray(String stringOfArray) {

        isReplacementTransacDetailsResponseReceivedForSelectedDate = false;
        try {
            JSONArray JArray = new JSONArray(stringOfArray);
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for (int i1 = 0; i1 < arrayLength; i1++) {
                Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = new Modal_ReplacementTransactionDetails();
                String transactionStatus = "", transactionType = "";
                List<Modal_ReplacementTransactionDetails> replacementTransactionDetailsArray = new ArrayList<>();


                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    try {
                        if (json.has("discountamount")) {
                            modal_replacementTransactionDetails.setDiscountamount(String.valueOf(json.getString("discountamount")));

                        } else {
                            modal_replacementTransactionDetails.setDiscountamount("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setDiscountamount("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("markeditemdesp")) {
                            modal_replacementTransactionDetails.setMarkeditemdesp_String(String.valueOf(json.getString("markeditemdesp")));

                        } else {
                            modal_replacementTransactionDetails.setMarkeditemdesp_String("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setMarkeditemdesp_String("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("mobileno")) {
                            modal_replacementTransactionDetails.setMobileno(String.valueOf(json.getString("mobileno")));

                        } else {
                            modal_replacementTransactionDetails.setMobileno("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setMobileno("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("orderid")) {
                            modal_replacementTransactionDetails.setOrderid(String.valueOf(json.getString("orderid")));

                        } else {
                            modal_replacementTransactionDetails.setOrderid("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setOrderid("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("refundamount")) {
                            modal_replacementTransactionDetails.setRefundamount(String.valueOf(json.getString("refundamount")));

                        } else {
                            modal_replacementTransactionDetails.setRefundamount("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setRefundamount("");

                        e.printStackTrace();
                    }


                    try {
                        if (json.has("replacementitemdesp")) {
                            modal_replacementTransactionDetails.setReplacementitemdesp_string(String.valueOf(json.getString("replacementitemdesp")));

                        } else {
                            modal_replacementTransactionDetails.setReplacementitemdesp_string("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setReplacementitemdesp_string("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("replacementorderamount")) {
                            modal_replacementTransactionDetails.setReplacementorderamount(String.valueOf(json.getString("replacementorderamount")));

                        } else {
                            modal_replacementTransactionDetails.setReplacementorderamount("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setReplacementorderamount("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("replacementorderid")) {
                            modal_replacementTransactionDetails.setReplacementorderid(String.valueOf(json.getString("replacementorderid")));

                        } else {
                            modal_replacementTransactionDetails.setReplacementorderid("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setReplacementorderid("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("transactionstatus")) {
                            modal_replacementTransactionDetails.setTransactionstatus(String.valueOf(json.getString("transactionstatus")));
                            transactionStatus = String.valueOf(json.getString("transactionstatus"));
                        } else {
                            transactionStatus = "";
                            modal_replacementTransactionDetails.setTransactionstatus("");

                        }
                    } catch (Exception e) {
                        transactionStatus = "";

                        modal_replacementTransactionDetails.setTransactionstatus("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("transactiontime")) {
                            modal_replacementTransactionDetails.setTransactiontime(String.valueOf(json.getString("transactiontime")));

                        } else {
                            modal_replacementTransactionDetails.setTransactiontime("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setTransactiontime("");

                        e.printStackTrace();
                    }

                    try {
                        if (json.has("transactiontype")) {
                            modal_replacementTransactionDetails.setTransactiontype(String.valueOf(json.getString("transactiontype")));
                            transactionType = String.valueOf(json.getString("transactiontype").toString().toUpperCase());

                        } else {
                            modal_replacementTransactionDetails.setTransactiontype("");
                            transactionType = "";

                        }
                    } catch (Exception e) {
                        transactionType = "";

                        modal_replacementTransactionDetails.setTransactiontype("");

                        e.printStackTrace();
                    }
                    try {
                        if (json.has("vendorkey")) {
                            modal_replacementTransactionDetails.setVendorkey(String.valueOf(json.getString("vendorkey")));

                        } else {
                            modal_replacementTransactionDetails.setVendorkey("");

                        }
                    } catch (Exception e) {
                        modal_replacementTransactionDetails.setVendorkey("");

                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
              /*  try {
                    Log.i("TransactionDetailsArray", String.valueOf(replacementTransactionDetailsArray.size()));
                    Log.i("TransactiontypeArray", String.valueOf(replacementTransactiontypeArray.size()));
                    Log.i("TransactiontypeHashmap", String.valueOf(replacementTransactiontypeHashmap.size()));
                    Log.i("transactionArray", String.valueOf(replacementTransactionDetailsArray.size()));

                } catch (Exception e) {
                    e.printStackTrace();
                }

               */
                try {
                    if (transactionStatus.toString().toUpperCase().equals("SUCCESS")) {
                        try {
                            replacementTransactionDetailsArray.add(modal_replacementTransactionDetails);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {

                            if (replacementTransactiontypeArray.contains(transactionType)) {
                                replacementTransactiontypeHashmap.get(transactionType).add(modal_replacementTransactionDetails);
                            } else {
                                replacementTransactiontypeArray.add(transactionType);
                                replacementTransactiontypeHashmap.put(transactionType, replacementTransactionDetailsArray);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if (arrayLength - 1 == i1) {
                                isReplacementTransacDetailsResponseReceivedForSelectedDate = true;

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OPENPDF_ACTIVITY_REQUEST_CODE:
                setResult(RESULT_OK);
                finish();
                break;

            default:
                break;
        }
    }


    public void F(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    try {
                        if(Order_Item_List.size()>0){
                            Adjusting_Widgets_Visibility(true);

                            exportReport();
                        }
                        else{
                            Adjusting_Widgets_Visibility(false);
                            Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Data to Export", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        } else {


            if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
                int grantResultsLength = grantResults.length;
                if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
                    // exportInvoice();
                    try {
                        if(Order_Item_List.size()>0){
                            Adjusting_Widgets_Visibility(true);

                            exportReport();
                        }
                        else{
                            Adjusting_Widgets_Visibility(false);
                            Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Data to Export", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(ConsolidatedReportSubCtgywise.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {


                            Adjusting_Widgets_Visibility(false);
                            isVendorOrdersTableServiceCalled =false;
                            scrollView.setVisibility(View.GONE);
                            instruction_textview.setVisibility(View.VISIBLE);
                            instruction_textview.setText("After Selecting the Date !! Click Fetch Data");
                            Order_Item_List.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            couponDiscountOrderidArray.clear();
                            ordertypeArray.clear();
                            ordertypeHashmap.clear();
                            couponDiscount_hashmap.clear();
                            replacementTransactiontypeHashmap.clear();
                            replacementTransactiontypeArray.clear();
                            oldpayableamount = 0;
                            itemDespTotalAmount = 0;
                            creditOrders_couponDiscount_hashmap.clear();
                            creditOrders_couponDiscountOrderidArray.clear();
                            creditOrderdeliveryChargeOrderidArray.clear();
                            creditOrderdeliveryCharge_hashmap.clear();
                            creditOrders_couponDiscount_hashmap.clear();
                            creditOrders_couponDiscountOrderidArray.clear();
                            creditOrderdeliveryChargeOrderidArray.clear();
                            creditOrderdeliveryCharge_hashmap.clear();
                            Pos_couponDiscount_hashmap.clear();
                            Pos_couponDiscountOrderidArray.clear();
                            phoneOrders_couponDiscount_hashmap.clear();
                            phoneOrders_couponDiscountOrderidArray.clear();
                            swiggyOrders_couponDiscount_hashmap.clear();
                            swiggyOrders_couponDiscountOrderidArray.clear();
                            dunzoOrders_couponDiscount_hashmap.clear();
                            dunzoOrders_couponDiscountOrderidArray.clear();
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                            dataList.clear();
                            tmcSubCtgykey.clear();
                            deliveryCharge_hashmap.clear();
                            deliveryCharge_hashmap.clear();
                             deliveryChargeOrderidArray.clear();
                             phoneorderdeliveryChargeOrderidArray.clear();
                            phoneorderdeliveryCharge_hashmap.clear();
                            wholesaleOrders_couponDiscountOrderidArray.clear();
                            wholesaleOrders_couponDiscount_hashmap.clear();
                            bigBasketOrders_couponDiscountOrderidArray.clear();
                            bigBasketOrders_couponDiscount_hashmap.clear();
                            no_of_ItemCount = 0;
                            no_of_orders = 0;
                            totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                            ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            //runthread();
                            Adjusting_Widgets_Visibility(false);

                            no_of_ItemCount = 0;
                            no_of_orders = 0;
                            totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                            noofOrders.setText(String.valueOf(no_of_orders));

                            noofPacks.setText(String.valueOf(no_of_ItemCount));



                            String month_in_String = getMonthString(monthOfYear);
                            String monthstring = String.valueOf(monthOfYear + 1);
                            String datestring = String.valueOf(dayOfMonth);
                            if (datestring.length() == 1) {
                                datestring = "0" + datestring;
                            }
                            if (monthstring.length() == 1) {
                                monthstring = "0" + monthstring;
                            }


                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay = getDayString(dayOfWeek);
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);


                            String CurrentDateString = datestring + monthstring + String.valueOf(year);
                            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            startDateString_forReplacementransaction = convertNormalDateintoReplacementTransactionDetailsDate(CurrentDateString, "STARTTIME");
                            endDateString_forReplacementransaction = convertNormalDateintoReplacementTransactionDetailsDate(CurrentDateString, "ENDTIME");
                            dateSelector_text.setText(CurrentDay + ", " + dayOfMonth + " " + month_in_String + " " + year);
                            DateString = (CurrentDay + ", " + dayOfMonth + " " + month_in_String + " " + year);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, year, month, day);


        Calendar c = Calendar.getInstance();
        DatePicker datePicker = datepicker.getDatePicker();

        c.add(Calendar.YEAR
                , -2);
        // Toast.makeText(getApplicationContext(), Calendar.DATE, Toast.LENGTH_LONG).show();
        Log.d(Constants.TAG, "Calendar.DATE " + String.valueOf(Calendar.DATE));
        long oneMonthAhead = c.getTimeInMillis();
        datePicker.setMinDate(oneMonthAhead);

        datepicker.show();
    }

    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey) {
        Adjusting_Widgets_Visibility(true);

        if(isVendorOrdersTableServiceCalled){
            Adjusting_Widgets_Visibility(false);
            return;
        }
        isVendorOrdersTableServiceCalled = true;
        mResultCallback = new VendorOrdersTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {

                if(orderslist_fromResponse.size()>0) {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("content", orderslist_fromResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isVendorOrdersTableServiceCalled = false;
                    //mobile_jsonString = String.valueOf(jsonObject);
                    // ordersList = orderslist_fromResponse;
                    processArrayAndgetData(orderslist_fromResponse);
                }
                else{

                    Adjusting_Widgets_Visibility(false);
                    isVendorOrdersTableServiceCalled =false;
                    scrollView.setVisibility(View.GONE);
                    instruction_textview.setVisibility(View.VISIBLE);
                    instruction_textview.setText("There is no Order On this Date");
                    Order_Item_List.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    couponDiscountOrderidArray.clear();
                    ordertypeArray.clear();
                    ordertypeHashmap.clear();
                    couponDiscount_hashmap.clear();
                    oldpayableamount = 0;
                    itemDespTotalAmount = 0;
                    creditOrders_couponDiscount_hashmap.clear();
                    creditOrders_couponDiscountOrderidArray.clear();
                    creditOrderdeliveryChargeOrderidArray.clear();
                    creditOrderdeliveryCharge_hashmap.clear();
                    Pos_couponDiscount_hashmap.clear();
                    Pos_couponDiscountOrderidArray.clear();
                    phoneOrders_couponDiscount_hashmap.clear();
                    phoneOrders_couponDiscountOrderidArray.clear();
                    swiggyOrders_couponDiscount_hashmap.clear();
                    swiggyOrders_couponDiscountOrderidArray.clear();
                    dunzoOrders_couponDiscount_hashmap.clear();
                    dunzoOrders_couponDiscountOrderidArray.clear();
                    SubCtgywiseTotalArray.clear();
                    SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                    dataList.clear();
                    tmcSubCtgykey.clear();
                    deliveryCharge_hashmap.clear();
                    deliveryCharge_hashmap.clear();
                     deliveryChargeOrderidArray.clear();
                    phoneorderdeliveryChargeOrderidArray.clear();
                    phoneorderdeliveryCharge_hashmap.clear();
                    wholesaleOrders_couponDiscountOrderidArray.clear();
                    wholesaleOrders_couponDiscount_hashmap.clear();
                    bigBasketOrders_couponDiscountOrderidArray.clear();
                    bigBasketOrders_couponDiscount_hashmap.clear();
                    no_of_ItemCount = 0;
                    no_of_orders = 0;
                     totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                    ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                    addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                    //runthread();
                    Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

                Adjusting_Widgets_Visibility(false);
                isVendorOrdersTableServiceCalled =false;
                scrollView.setVisibility(View.GONE);
                instruction_textview.setVisibility(View.VISIBLE);
                instruction_textview.setText("There is some error"+String.valueOf(error));
                Order_Item_List.clear();
                OrderItem_hashmap.clear();
                finalBillDetails.clear();
                FinalBill_hashmap.clear();
                couponDiscountOrderidArray.clear();
                ordertypeArray.clear();
                ordertypeHashmap.clear();
                couponDiscount_hashmap.clear();
                oldpayableamount = 0;
                itemDespTotalAmount = 0;
                creditOrders_couponDiscount_hashmap.clear();
                creditOrders_couponDiscountOrderidArray.clear();
                creditOrderdeliveryChargeOrderidArray.clear();
                creditOrderdeliveryCharge_hashmap.clear();
                Pos_couponDiscount_hashmap.clear();
                Pos_couponDiscountOrderidArray.clear();
                phoneOrders_couponDiscount_hashmap.clear();
                phoneOrders_couponDiscountOrderidArray.clear();
                swiggyOrders_couponDiscount_hashmap.clear();
                swiggyOrders_couponDiscountOrderidArray.clear();
                dunzoOrders_couponDiscount_hashmap.clear();
                dunzoOrders_couponDiscountOrderidArray.clear();
                SubCtgywiseTotalArray.clear();
                SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                dataList.clear();
                tmcSubCtgykey.clear();
                deliveryCharge_hashmap.clear();
                deliveryCharge_hashmap.clear();
              deliveryChargeOrderidArray.clear();
                 phoneorderdeliveryChargeOrderidArray.clear();
                phoneorderdeliveryCharge_hashmap.clear();
                   wholesaleOrders_couponDiscountOrderidArray.clear();
                wholesaleOrders_couponDiscount_hashmap.clear();
                bigBasketOrders_couponDiscountOrderidArray.clear();
                bigBasketOrders_couponDiscount_hashmap.clear();
                no_of_ItemCount = 0;
                no_of_orders = 0;
                totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                //runthread();
                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is Some Error ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                isVendorOrdersTableServiceCalled = false;

            }
        };

        mVolleyService = new VendorOrdersTableService(mResultCallback,ConsolidatedReportSubCtgywise.this);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }


    private void processArrayAndgetData(List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {
        isVendorOrdersTableServiceCalled = false;
            for (int i = 0; i < orderslist_fromResponse.size(); i++) {
                String paymentMode = "", ordertype = "", orderid = "", slotname = "", deliveryAmount = "", deliveryType = "", couponDiscount_local = "", payableAmount;
                JSONArray itemdesp_JSONArray = new JSONArray();

                Modal_ManageOrders_Pojo_Class orders_pojo_class_fromResponse = orderslist_fromResponse.get(i);
                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();


                try {

                    ordertype = String.valueOf(orders_pojo_class_fromResponse.getOrdertype().toUpperCase());
                    modal_orderDetails.ordertype = ordertype;

                } catch (Exception e) {
                    ordertype = "";
                    modal_orderDetails.ordertype = "";

                    e.printStackTrace();
                }
                if (ordertype.equals("") || ordertype.equals(null) || ordertype.equals("NULL")) {
                    try {

                        ordertype = String.valueOf(orders_pojo_class_fromResponse.getOrderType().toUpperCase());
                        modal_orderDetails.ordertype = ordertype;

                    } catch (Exception e) {
                        ordertype = "";
                        modal_orderDetails.ordertype = "";

                        e.printStackTrace();
                    }
                }

                try {
                    paymentMode = String.valueOf(orders_pojo_class_fromResponse.getPaymentmode().toUpperCase());
                    modal_orderDetails.paymentmode = paymentMode;

                    if(paymentMode.equals(Constants.CREDIT)){
                        try {

                            ordertype = String.valueOf(Constants.CREDIT);
                            modal_orderDetails.ordertype = ordertype;

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.ordertype = "";

                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    paymentMode = "";
                    modal_orderDetails.paymentmode = "";

                    e.printStackTrace();
                }


                try {

                    orderid = String.valueOf(orders_pojo_class_fromResponse.getOrderid());
                    modal_orderDetails.orderid = orderid;
                    no_of_orders++;
                } catch (Exception e) {
                    orderid = "";
                    modal_orderDetails.orderid = "";
                    e.printStackTrace();
                }


                try {
                    itemdesp_JSONArray = orders_pojo_class_fromResponse.getItemdesp();
                    modal_orderDetails.itemdesp = itemdesp_JSONArray;

                } catch (Exception e) {
                    try {
                        itemdesp_JSONArray = new JSONArray(orders_pojo_class_fromResponse.getItemdesp_string());
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }
                    modal_orderDetails.itemdesp = itemdesp_JSONArray;

                    e.printStackTrace();
                }


                try {

                    slotname = String.valueOf(orders_pojo_class_fromResponse.getSlotname().toUpperCase());
                    modal_orderDetails.slotname = slotname;

                } catch (Exception e) {
                    slotname = "";
                    modal_orderDetails.slotname = slotname;

                    e.printStackTrace();
                }


                try {

                    double newpayableamount = Double.parseDouble(String.valueOf(orders_pojo_class_fromResponse.getPayableamount()));


                    oldpayableamount = newpayableamount + oldpayableamount;

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {

                    CouponDiscount = String.valueOf(orders_pojo_class_fromResponse.getCoupondiscamount());
                    modal_orderDetails.coupondiscount = CouponDiscount;

                } catch (Exception e) {
                    CouponDiscount = "";
                    modal_orderDetails.coupondiscount = CouponDiscount;

                    e.printStackTrace();
                }




                try {

                    deliveryType = String.valueOf(orders_pojo_class_fromResponse.getDeliverytype());
                    modal_orderDetails.deliverytype = deliveryType;

                } catch (Exception e) {
                    deliveryType = "";
                    modal_orderDetails.deliverytype = deliveryType;

                    e.printStackTrace();
                }


                try {
                    if ((ordertype.equals(Constants.CREDIT))) {


                        try {

                            deliveryAmountForCreditOrders = String.valueOf(orders_pojo_class_fromResponse.getDeliveryamount());
                            modal_orderDetails.deliveryamount = deliveryAmountForCreditOrders;

                        } catch (Exception e) {
                            deliveryAmountForCreditOrders = "0";
                            modal_orderDetails.deliveryamount = deliveryAmountForCreditOrders;

                            e.printStackTrace();
                        }
                        try {
                            if ((!deliveryAmountForCreditOrders.equals("")) && (!deliveryAmountForCreditOrders.equals("0")) && (!deliveryAmountForCreditOrders.equals("0.0")) && (!deliveryAmountForCreditOrders.equals("0.00")) ) {
                                //   deliveryamount = "0.00";

                                if (!orderid.equals("")) {
                                    if (!creditOrderdeliveryChargeOrderidArray.contains(orderid)) {
                                        creditOrderdeliveryChargeOrderidArray.add(orderid);
                                        creditOrderdeliveryCharge_hashmap.put(orderid, deliveryAmountForCreditOrders);
                                    } else {


                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }




                    }
                   else if ((ordertype.equals(Constants.APPORDER))) {


                        try {

                            deliveryamount = String.valueOf(orders_pojo_class_fromResponse.getDeliveryamount());
                            modal_orderDetails.deliveryamount = deliveryamount;

                        } catch (Exception e) {
                            deliveryamount = "";
                            modal_orderDetails.deliveryamount = deliveryamount;

                            e.printStackTrace();
                        }
                        try {
                            if ((!deliveryamount.equals("")) && (!deliveryamount.equals("0")) && (!deliveryamount.equals("0.0")) && (!deliveryamount.equals("0.00")) ) {
                                //   deliveryamount = "0.00";

                                if (!orderid.equals("")) {
                                    if (!deliveryChargeOrderidArray.contains(orderid)) {
                                        deliveryChargeOrderidArray.add(orderid);
                                        deliveryCharge_hashmap.put(orderid, deliveryamount);
                                    } else {


                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else  if ((ordertype.equals(Constants.PhoneOrder))) {

                        try {

                            deliveryAmountPhoneOrder = String.valueOf(orders_pojo_class_fromResponse.getDeliveryamount());
                            modal_orderDetails.deliveryamount = deliveryAmountPhoneOrder;

                        } catch (Exception e) {
                            deliveryAmountPhoneOrder = "";
                            modal_orderDetails.deliveryamount = deliveryAmountPhoneOrder;

                            e.printStackTrace();
                        }

                        try {
                            if ((!deliveryAmountPhoneOrder.equals("")) && (!deliveryAmountPhoneOrder.equals("0")) && (!deliveryAmountPhoneOrder.equals("0.00")) && (!deliveryAmountPhoneOrder.equals("0.0"))) {
                              //  deliveryAmountPhoneOrder = "0.00";

                            if (!orderid.equals("")) {
                                if (!phoneorderdeliveryChargeOrderidArray.contains(orderid)) {
                                    phoneorderdeliveryChargeOrderidArray.add(orderid);
                                    phoneorderdeliveryCharge_hashmap.put(orderid, deliveryAmountPhoneOrder);
                                } else {


                                }
                            }
                        }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    if ((ordertype.equals(Constants.CREDIT))) {
                        try {
                            Credit_CouponDiscount = CouponDiscount;

                            if (Credit_CouponDiscount.equals("")) {
                                Credit_CouponDiscount = "0";
                            }
                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                            if (!orderid.equals("")) {
                                if (!creditOrders_couponDiscountOrderidArray.contains(orderid)) {
                                    creditOrders_couponDiscountOrderidArray.add(orderid);
                                    creditOrders_couponDiscount_hashmap.put(orderid, Credit_CouponDiscount);
                                } else {


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if ((ordertype.equals(Constants.APPORDER))) {
                        try {
                            if (CouponDiscount.equals("")) {
                                CouponDiscount = "0";
                            }
                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                            if (!orderid.equals("")) {
                                if (!couponDiscountOrderidArray.contains(orderid)) {
                                    couponDiscountOrderidArray.add(orderid);
                                    couponDiscount_hashmap.put(orderid, CouponDiscount);
                                } else {


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if ((ordertype.equals(Constants.PhoneOrder))) {

                        try {
                            PhoneOrder_CouponDiscount = CouponDiscount;
                            if (PhoneOrder_CouponDiscount.equals("")) {
                                PhoneOrder_CouponDiscount = "0";
                            }
                            if (!orderid.equals("")) {
                                if (!phoneOrders_couponDiscountOrderidArray.contains(orderid)) {
                                    phoneOrders_couponDiscountOrderidArray.add(orderid);
                                    phoneOrders_couponDiscount_hashmap.put(orderid, PhoneOrder_CouponDiscount);
                                } else {


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else if ((ordertype.equals(Constants.WholeSaleOrder))) {


                        try {
                            WholeSales_CouponDiscount = String.valueOf(CouponDiscount);
                            if (WholeSales_CouponDiscount.equals("")) {
                                WholeSales_CouponDiscount = "0";
                            }
                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                            if (!orderid.equals("")) {
                                if (!wholesaleOrders_couponDiscountOrderidArray.contains(orderid)) {
                                    wholesaleOrders_couponDiscountOrderidArray.add(orderid);
                                    wholesaleOrders_couponDiscount_hashmap.put(orderid, WholeSales_CouponDiscount);
                                } else {
                                    //Log.d(Constants.TAG, "This orderid already have an discount");


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else if ((ordertype.equals(Constants.SwiggyOrder))) {

                        try {
                            Swiggy_CouponDiscount = String.valueOf(CouponDiscount);
                            if (Swiggy_CouponDiscount.equals("")) {
                                Swiggy_CouponDiscount = "0";
                            }
                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                            if (!orderid.equals("")) {
                                if (!swiggyOrders_couponDiscountOrderidArray.contains(orderid)) {
                                    swiggyOrders_couponDiscountOrderidArray.add(orderid);
                                    swiggyOrders_couponDiscount_hashmap.put(orderid, Swiggy_CouponDiscount);
                                } else {
                                    //Log.d(Constants.TAG, "This orderid already have an discount");


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else if ((ordertype.equals(Constants.DunzoOrder))) {


                        try {
                            Dunzo_CouponDiscount = String.valueOf(CouponDiscount);
                            if (Dunzo_CouponDiscount.equals("")) {
                                Dunzo_CouponDiscount = "0";
                            }
                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                            if (!orderid.equals("")) {
                                if (!dunzoOrders_couponDiscountOrderidArray.contains(orderid)) {
                                    dunzoOrders_couponDiscountOrderidArray.add(orderid);
                                    dunzoOrders_couponDiscount_hashmap.put(orderid, Dunzo_CouponDiscount);
                                } else {
                                    //Log.d(Constants.TAG, "This orderid already have an discount");


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else if ((ordertype.equals(Constants.BigBasket))) {


                        try {
                            BigBasket_CouponDiscount = String.valueOf(CouponDiscount);
                            if (BigBasket_CouponDiscount.equals("")) {
                                BigBasket_CouponDiscount = "0";
                            }
                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                            if (!orderid.equals("")) {
                                if (!bigBasketOrders_couponDiscountOrderidArray.contains(orderid)) {
                                    bigBasketOrders_couponDiscountOrderidArray.add(orderid);
                                    bigBasketOrders_couponDiscount_hashmap.put(orderid, BigBasket_CouponDiscount);
                                } else {
                                    //Log.d(Constants.TAG, "This orderid already have an discount");


                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    else if ((ordertype.equals(Constants.POSORDER))) {
                        if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME) || slotname.equals("")) {

                            try {
                                pos_CouponDiscount = String.valueOf(CouponDiscount);
                                if (pos_CouponDiscount.equals("")) {
                                    pos_CouponDiscount = "0";
                                }
                                //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                if (!orderid.equals("")) {
                                    if (!Pos_couponDiscountOrderidArray.contains(orderid)) {
                                        Pos_couponDiscountOrderidArray.add(orderid);
                                        Pos_couponDiscount_hashmap.put(orderid, pos_CouponDiscount);
                                    } else {
                                        //Log.d(Constants.TAG, "This orderid already have an discount");


                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }
                    if (ordertype.equals("") || ordertype.equals(null) || ordertype.equals("NULL")) {
                        try {

                            ordertype = String.valueOf(orders_pojo_class_fromResponse.getOrderType().toUpperCase());
                            modal_orderDetails.ordertype = ordertype;

                        } catch (Exception e) {
                            ordertype = "";
                            modal_orderDetails.ordertype = "";

                            e.printStackTrace();
                        }
                    }

                    getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ConsolidatedReportSubCtgywise.this, "can't Process this ItemDesp ", Toast.LENGTH_LONG).show();
                }


                if (orderslist_fromResponse.size() - i == 1) {
                    if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
                        try {


                            try {
                                try {
                                    Collections.sort(Order_Item_List, new Comparator<String>() {
                                        public int compare(final String object1, final String object2) {
                                            return object1.compareTo(object2);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            isOrderDetailsResponseReceivedForSelectedDate = true;
                            runthread();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Order_Item_List.clear();
                        OrderItem_hashmap.clear();
                        finalBillDetails.clear();
                        FinalBill_hashmap.clear();
                        couponDiscountOrderidArray.clear();
                        ordertypeArray.clear();
                        ordertypeHashmap.clear();
                        couponDiscount_hashmap.clear();
                        oldpayableamount = 0;
                        itemDespTotalAmount = 0;
                        creditOrders_couponDiscount_hashmap.clear();
                        creditOrders_couponDiscountOrderidArray.clear();
                        creditOrderdeliveryChargeOrderidArray.clear();
                        creditOrderdeliveryCharge_hashmap.clear();
                        Pos_couponDiscount_hashmap.clear();
                        Pos_couponDiscountOrderidArray.clear();
                        phoneOrders_couponDiscount_hashmap.clear();
                        phoneOrders_couponDiscountOrderidArray.clear();
                        swiggyOrders_couponDiscount_hashmap.clear();
                        swiggyOrders_couponDiscountOrderidArray.clear();
                        dunzoOrders_couponDiscount_hashmap.clear();
                        dunzoOrders_couponDiscountOrderidArray.clear();
                        SubCtgywiseTotalArray.clear();
                        SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                        dataList.clear();
                        tmcSubCtgykey.clear();
                        deliveryCharge_hashmap.clear();
                        deliveryCharge_hashmap.clear();
                         deliveryChargeOrderidArray.clear();
                        phoneorderdeliveryChargeOrderidArray.clear();
                         phoneorderdeliveryCharge_hashmap.clear();
                        wholesaleOrders_couponDiscountOrderidArray.clear();
                        wholesaleOrders_couponDiscount_hashmap.clear();
                        bigBasketOrders_couponDiscountOrderidArray.clear();
                        bigBasketOrders_couponDiscount_hashmap.clear();
                        no_of_ItemCount = 0;
                        no_of_orders = 0;
                        totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                        ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                        addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                        //runthread();
                        Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                        Adjusting_Widgets_Visibility(false);

                    }
                }

            }



    }


    private void getOrderForSelectedDate(String previousDateString, String dateString, String vendorKey) {

        if (isgetOrderForSelectedDateCalled) {
            return;
        }
        //  dateString ="May 2021";
        //  previousDateString = "Fri, 30 Apr 2021";
        //  dateSelector_text.setText("May 2021");
        isgetOrderForSelectedDateCalled = true;
        Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_forReport_AppOrders_and_PosOrders + "?slotdate=" + dateString + "&vendorkey=" + vendorKey + "&previousdaydate=" + previousDateString, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        try {
                            String orderid = "", deliverytype = "";
                            double discount_double = 0, discountfromHashmap_double = 0;
                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (int i1 = 0; i1 < JArray.length(); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                    //   //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                                    JSONArray itemdesp;


                                    if (json.has("itemdesp")) {

                                        try {

                                            itemdesp = json.getJSONArray("itemdesp");
                                            modal_orderDetails.itemdesp = itemdesp;

                                            //Log.d(Constants.TAG, "itemdesp has been succesfully  retrived" );

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {

                                        //Log.d(Constants.TAG, "There is no itemdesp: ");


                                    }

                                    if (json.has("orderid")) {
                                        try {
                                            modal_orderDetails.orderid = String.valueOf(json.get("orderid"));
                                            orderid = String.valueOf(json.get("orderid"));
                                            //Log.d(Constants.TAG, "orderid: " + String.valueOf(json.get("orderid")));
                                            no_of_orders++;
                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }

                                    } else {
                                        modal_orderDetails.orderid = "There is no orderid";
                                        //Log.d(Constants.TAG, "There is no orderid: " + String.valueOf(json.get("orderid")));


                                    }


                                    if (json.has("payableamount")) {
                                        try {

                                            double newpayableamount = Double.parseDouble(String.valueOf(json.get("payableamount")));

                                            //Log.i(Constants.TAG,"Consolidated Report old oldpayableamount  "+String.valueOf(oldpayableamount));

                                            oldpayableamount = newpayableamount + oldpayableamount;
                                            //Log.i(Constants.TAG,"Consolidated Report  new payableAmountorderid            "+orderid  );

                                            //Log.i(Constants.TAG,"Consolidated Report  new payableamount   "+newpayableamount);

                                            //Log.i(Constants.TAG,"Consolidated Report  old 2 oldpayableamount  "  +oldpayableamount);
                                            //Log.i(Constants.TAG,"Consolidated Report  old 2                                          "  );

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }


                                    if (json.has("slotname")) {
                                        try {
                                            slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            modal_orderDetails.slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            //Log.d(Constants.TAG, "OrderType: " + slotname);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        modal_orderDetails.slotname = "There is no slotname";
                                        //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("ordertype")));

                                    }

                                    if (json.has("ordertype")) {
                                        try {
                                            ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        modal_orderDetails.ordertype = "There is no OrderType";
                                        //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                    }

                                    getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                    if (json.has("paymentmode")) {
                                        try {
                                            modal_orderDetails.paymentmode = String.valueOf(json.get("paymentmode"));
                                            paymentMode = String.valueOf(json.get("paymentmode")).toUpperCase();
                                            if(paymentMode.equals(Constants.CREDIT)){
                                                try {
                                                    ordertype = String.valueOf(paymentMode);
                                                    modal_orderDetails.ordertype = String.valueOf(paymentMode);
                                                    //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //Log.d(Constants.TAG, "PaymentMode: " + String.valueOf(json.get("paymentmode")));

                                    } else {
                                        modal_orderDetails.paymentmode = "There is no payment mode";
                                        //Log.d(Constants.TAG, "There is no PaymentMode: " + String.valueOf(json.get("ordertype")));


                                    }


/*

                                    if (json.has("coupondiscount")) {
                                        try {
                                            modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                            CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                            if (CouponDiscount.equals("")) {
                                                CouponDiscount = "0";
                                            }
                                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                            if (!orderid.equals("")) {
                                                if (!couponDiscountOrderidArray.contains(orderid)) {
                                                    couponDiscountOrderidArray.add(orderid);
                                                    couponDiscount_hashmap.put(orderid, CouponDiscount);
                                                } else {
                                                    //Log.d(Constants.TAG, "This orderid already have an discount");


                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                    }
 */



                                    try {

                                         if ((ordertype.equals(Constants.CREDIT))) {

                                                if (json.has("deliveryamount")) {
                                                    try {

                                                        deliveryAmountForCreditOrders = String.valueOf(json.get("deliveryamount"));
                                                        modal_orderDetails.deliveryamount = String.valueOf(json.get("deliveryamount"));


                                                        //Log.d(Constants.TAG, "OrderType: " + slotname);
                                                        if ((!deliveryAmountForCreditOrders.equals("")) && (!deliveryAmountForCreditOrders.equals("0")) && (!deliveryAmountForCreditOrders.equals("0.00")) && (!deliveryAmountForCreditOrders.equals("0.0"))) {
                                                            //deliveryAmountPhoneOrder = "0.00";

                                                            //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                            if (!orderid.equals("")) {
                                                                if (!creditOrderdeliveryChargeOrderidArray.contains(orderid)) {
                                                                    creditOrderdeliveryChargeOrderidArray.add(orderid);
                                                                    creditOrderdeliveryCharge_hashmap.put(orderid, deliveryAmountForCreditOrders);
                                                                } else {
                                                                    //Log.d(Constants.TAG, "This orderid already have an discount");


                                                                }
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.deliveryamount = "0.00";
                                                    //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("ordertype")));

                                                }


                                                if (json.has("deliverytype")) {
                                                    try {
                                                        modal_orderDetails.deliverytype = String.valueOf(json.get("deliverytype"));
                                                        deliverytype = String.valueOf(json.get("deliverytype"));


                                                        //Log.d(Constants.TAG, "deliverytype 1: " + String.valueOf(json.get("orderid")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        //Log.d(Constants.TAG, "deliverytype:2 " + String.valueOf(json.get("orderid")));

                                                    }

                                                } else {
                                                    modal_orderDetails.deliverytype = "There is no deliverytype";
                                                    //Log.d(Constants.TAG, " deliverytype3: " + String.valueOf(json.get("orderid")));


                                                }
                                             if (json.has("ordertype")) {
                                                 try {
                                                     ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                     modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                     //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                 } catch (Exception e) {
                                                     e.printStackTrace();
                                                 }

                                             } else {
                                                 modal_orderDetails.ordertype = "There is no OrderType";
                                                 //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                             }

                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        Credit_CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (Credit_CouponDiscount.equals("")) {
                                                            Credit_CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!creditOrders_couponDiscountOrderidArray.contains(orderid)) {
                                                                creditOrders_couponDiscountOrderidArray.add(orderid);
                                                                creditOrders_couponDiscount_hashmap.put(orderid, Credit_CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);



                                        }


                                        else if (ordertype.equals(Constants.APPORDER)) {

                                            if (json.has("deliveryamount")) {
                                                try {

                                                    deliveryamount = String.valueOf(json.get("deliveryamount"));
                                                    modal_orderDetails.deliveryamount = String.valueOf(json.get("deliveryamount"));


                                                    //Log.d(Constants.TAG, "OrderType: " + slotname);
                                                    if ((!deliveryamount.equals("")) && (!deliveryamount.equals("0")) && (!deliveryamount.equals("0.0")) && (!deliveryamount.equals("0.00")) ) {
                                                        // deliveryamount = "0.00";

                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!deliveryChargeOrderidArray.contains(orderid)) {
                                                                deliveryChargeOrderidArray.add(orderid);
                                                                deliveryCharge_hashmap.put(orderid, deliveryamount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                modal_orderDetails.deliveryamount = "0.00";
                                                //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("ordertype")));

                                            }


                                            if (json.has("deliverytype")) {
                                                try {
                                                    modal_orderDetails.deliverytype = String.valueOf(json.get("deliverytype"));
                                                    deliverytype = String.valueOf(json.get("deliverytype"));


                                                    //Log.d(Constants.TAG, "deliverytype 1: " + String.valueOf(json.get("orderid")));

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    //Log.d(Constants.TAG, "deliverytype:2 " + String.valueOf(json.get("orderid")));

                                                }

                                            } else {
                                                modal_orderDetails.deliverytype = "There is no deliverytype";
                                                //Log.d(Constants.TAG, " deliverytype3: " + String.valueOf(json.get("orderid")));


                                            }


                                           if ((slotname.equals(Constants.PREORDER_SLOTNAME)) || (slotname.equals(Constants.SPECIALDAYPREORDER_SLOTNAME))) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (CouponDiscount.equals("")) {
                                                            CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!couponDiscountOrderidArray.contains(orderid)) {
                                                                couponDiscountOrderidArray.add(orderid);
                                                                couponDiscount_hashmap.put(orderid, CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                               if (json.has("ordertype")) {
                                                   try {
                                                       ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                       modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                       //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                   } catch (Exception e) {
                                                       e.printStackTrace();
                                                   }

                                               } else {
                                                   modal_orderDetails.ordertype = "There is no OrderType";
                                                   //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                               }

                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);

                                            } else if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals("") || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (CouponDiscount.equals("")) {
                                                            CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!couponDiscountOrderidArray.contains(orderid)) {
                                                                couponDiscountOrderidArray.add(orderid);
                                                                couponDiscount_hashmap.put(orderid, CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                               if (json.has("ordertype")) {
                                                   try {
                                                       ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                       modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                       //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                   } catch (Exception e) {
                                                       e.printStackTrace();
                                                   }

                                               } else {
                                                   modal_orderDetails.ordertype = "There is no OrderType";
                                                   //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                               }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        }



                                        else if ((ordertype.equals(Constants.PhoneOrder))) {
                                            if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {

                                                if (json.has("deliveryamount")) {
                                                    try {

                                                        deliveryAmountPhoneOrder = String.valueOf(json.get("deliveryamount"));
                                                        modal_orderDetails.deliveryamount = String.valueOf(json.get("deliveryamount"));


                                                        //Log.d(Constants.TAG, "OrderType: " + slotname);
                                                        if ((!deliveryAmountPhoneOrder.equals("")) && (!deliveryAmountPhoneOrder.equals("0")) && (!deliveryAmountPhoneOrder.equals("0.00")) && (!deliveryAmountPhoneOrder.equals("0.0"))) {
                                                            //deliveryAmountPhoneOrder = "0.00";

                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!phoneorderdeliveryChargeOrderidArray.contains(orderid)) {
                                                                phoneorderdeliveryChargeOrderidArray.add(orderid);
                                                                phoneorderdeliveryCharge_hashmap.put(orderid, deliveryAmountPhoneOrder);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.deliveryamount = "0.00";
                                                    //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("ordertype")));

                                                }


                                                if (json.has("deliverytype")) {
                                                    try {
                                                        modal_orderDetails.deliverytype = String.valueOf(json.get("deliverytype"));
                                                        deliverytype = String.valueOf(json.get("deliverytype"));


                                                        //Log.d(Constants.TAG, "deliverytype 1: " + String.valueOf(json.get("orderid")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        //Log.d(Constants.TAG, "deliverytype:2 " + String.valueOf(json.get("orderid")));

                                                    }

                                                } else {
                                                    modal_orderDetails.deliverytype = "There is no deliverytype";
                                                    //Log.d(Constants.TAG, " deliverytype3: " + String.valueOf(json.get("orderid")));


                                                }


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        PhoneOrder_CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (PhoneOrder_CouponDiscount.equals("")) {
                                                            PhoneOrder_CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!phoneOrders_couponDiscountOrderidArray.contains(orderid)) {
                                                                phoneOrders_couponDiscountOrderidArray.add(orderid);
                                                                phoneOrders_couponDiscount_hashmap.put(orderid, PhoneOrder_CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                                if (json.has("ordertype")) {
                                                    try {
                                                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.ordertype = "There is no OrderType";
                                                    //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                                }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        }else if ((ordertype.equals(Constants.WholeSaleOrder))) {
                                            if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        WholeSales_CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (WholeSales_CouponDiscount.equals("")) {
                                                            WholeSales_CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!wholesaleOrders_couponDiscountOrderidArray.contains(orderid)) {
                                                                wholesaleOrders_couponDiscountOrderidArray.add(orderid);
                                                                wholesaleOrders_couponDiscount_hashmap.put(orderid, WholeSales_CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                                if (json.has("ordertype")) {
                                                    try {
                                                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.ordertype = "There is no OrderType";
                                                    //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                                }
                                                if (json.has("ordertype")) {
                                                    try {
                                                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.ordertype = "There is no OrderType";
                                                    //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                                }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        } else if ((ordertype.equals(Constants.SwiggyOrder))) {
                                            if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        Swiggy_CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (Swiggy_CouponDiscount.equals("")) {
                                                            Swiggy_CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!swiggyOrders_couponDiscountOrderidArray.contains(orderid)) {
                                                                swiggyOrders_couponDiscountOrderidArray.add(orderid);
                                                                swiggyOrders_couponDiscount_hashmap.put(orderid, Swiggy_CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                                if (json.has("ordertype")) {
                                                    try {
                                                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.ordertype = "There is no OrderType";
                                                    //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                                }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        } else if ((ordertype.equals(Constants.DunzoOrder))) {
                                            if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        Dunzo_CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (Dunzo_CouponDiscount.equals("")) {
                                                            Dunzo_CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!dunzoOrders_couponDiscountOrderidArray.contains(orderid)) {
                                                                dunzoOrders_couponDiscountOrderidArray.add(orderid);
                                                                dunzoOrders_couponDiscount_hashmap.put(orderid, Dunzo_CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                                if (json.has("ordertype")) {
                                                    try {
                                                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.ordertype = "There is no OrderType";
                                                    //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                                }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        } else if ((ordertype.equals(Constants.BigBasket))) {
                                            if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        BigBasket_CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (BigBasket_CouponDiscount.equals("")) {
                                                            BigBasket_CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!bigBasketOrders_couponDiscountOrderidArray.contains(orderid)) {
                                                                bigBasketOrders_couponDiscountOrderidArray.add(orderid);
                                                                bigBasketOrders_couponDiscount_hashmap.put(orderid, BigBasket_CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }

                                                if (json.has("ordertype")) {
                                                    try {
                                                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.ordertype = "There is no OrderType";
                                                    //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                                }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        } else {
                                            if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        pos_CouponDiscount = String.valueOf(json.get("coupondiscount"));
                                                        if (pos_CouponDiscount.equals("")) {
                                                            pos_CouponDiscount = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!Pos_couponDiscountOrderidArray.contains(orderid)) {
                                                                Pos_couponDiscountOrderidArray.add(orderid);
                                                                Pos_couponDiscount_hashmap.put(orderid, pos_CouponDiscount);
                                                            } else {
                                                                //Log.d(Constants.TAG, "This orderid already have an discount");


                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {

                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";


                                                }
                                                if (json.has("ordertype")) {
                                                    try {
                                                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                                        //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    modal_orderDetails.ordertype = "There is no OrderType";
                                                    //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                                }
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(ConsolidatedReportSubCtgywise.this, "can't Process this ItemDesp ", Toast.LENGTH_LONG).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Adjusting_Widgets_Visibility(false);

                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                                }


                            }


                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);

                            e.printStackTrace();
                        }

                        if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
                            try {


                                try {
                                    try {
                                        Collections.sort(Order_Item_List, new Comparator<String>() {
                                            public int compare(final String object1, final String object2) {
                                                return object1.compareTo(object2);
                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                isOrderDetailsResponseReceivedForSelectedDate = true;
                                runthread();


                                //addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                                // prepareContent();
                                // setAdapter();

                               /* adapater_pos_sales_report = new Adapter_ConsolidatedSalesReport_listview(ConsolidatedReportSubCtgywise.this, Order_Item_List, OrderItem_hashmap);
                                consolidatedSalesReport_Listview.setAdapter(adapater_pos_sales_report);
                                ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);
                                scrollView.fullScroll(View.FOCUS_UP);


                                */


                                //sort_the_array_CtgyWise();
                                //  prepareContent();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Order_Item_List.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            couponDiscountOrderidArray.clear();
                            ordertypeArray.clear();
                            ordertypeHashmap.clear();
                            couponDiscount_hashmap.clear();
                            oldpayableamount = 0;
                            itemDespTotalAmount = 0;
                            creditOrders_couponDiscount_hashmap.clear();
                            creditOrders_couponDiscountOrderidArray.clear();
                            creditOrderdeliveryChargeOrderidArray.clear();
                            creditOrderdeliveryCharge_hashmap.clear();
                            Pos_couponDiscount_hashmap.clear();
                            Pos_couponDiscountOrderidArray.clear();
                            phoneOrders_couponDiscount_hashmap.clear();
                            phoneOrders_couponDiscountOrderidArray.clear();
                            swiggyOrders_couponDiscount_hashmap.clear();
                            swiggyOrders_couponDiscountOrderidArray.clear();
                            dunzoOrders_couponDiscount_hashmap.clear();
                            dunzoOrders_couponDiscountOrderidArray.clear();
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                            dataList.clear();
                            tmcSubCtgykey.clear();
                            deliveryCharge_hashmap.clear();
                            deliveryCharge_hashmap.clear();
                            deliveryChargeOrderidArray.clear();
                            phoneorderdeliveryChargeOrderidArray.clear();
                            phoneorderdeliveryCharge_hashmap.clear();
                            wholesaleOrders_couponDiscountOrderidArray.clear();
                            wholesaleOrders_couponDiscount_hashmap.clear();
                            bigBasketOrders_couponDiscountOrderidArray.clear();
                            bigBasketOrders_couponDiscount_hashmap.clear();
                            no_of_ItemCount = 0;
                            no_of_orders = 0;
                            totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                            ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            //runthread();
                            Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);

                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();


                if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
                    try {
                        try {
                            Collections.sort(Order_Item_List, new Comparator<String>() {
                                public int compare(final String object1, final String object2) {
                                    return object1.compareTo(object2);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        runthread();

                        //addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                       /* adapater_pos_sales_report = new Adapter_ConsolidatedSalesReport_listview(ConsolidatedReportSubCtgywise.this, Order_Item_List, OrderItem_hashmap);
                        consolidatedSalesReport_Listview.setAdapter(adapater_pos_sales_report);
                        ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);
                        scrollView.fullScroll(View.FOCUS_UP);

                        */


                        //sort_the_array_CtgyWise();
                        // prepareContent();
                        //setAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Order_Item_List.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    couponDiscountOrderidArray.clear();
                    ordertypeArray.clear();
                    ordertypeHashmap.clear();
                    couponDiscount_hashmap.clear();
                    oldpayableamount = 0;
                    itemDespTotalAmount = 0;
                    creditOrders_couponDiscount_hashmap.clear();
                    creditOrders_couponDiscountOrderidArray.clear();
                    creditOrderdeliveryChargeOrderidArray.clear();
                    creditOrderdeliveryCharge_hashmap.clear();
                    Pos_couponDiscount_hashmap.clear();
                    Pos_couponDiscountOrderidArray.clear();
                    phoneOrders_couponDiscount_hashmap.clear();
                    phoneOrders_couponDiscountOrderidArray.clear();
                    swiggyOrders_couponDiscount_hashmap.clear();
                    swiggyOrders_couponDiscountOrderidArray.clear();
                    dunzoOrders_couponDiscount_hashmap.clear();
                    dunzoOrders_couponDiscountOrderidArray.clear();
                    SubCtgywiseTotalArray.clear();
                    SubCtgywiseTotalHashmap.clear();
             SubCtgywiseWeightHashmap.clear();
             SubCtgywiseQuantityHashmap.clear();
                    wholesaleOrders_couponDiscountOrderidArray.clear();
                    wholesaleOrders_couponDiscount_hashmap.clear();
                    bigBasketOrders_couponDiscountOrderidArray.clear();
                    bigBasketOrders_couponDiscount_hashmap.clear();
                    dataList.clear();
                    tmcSubCtgykey.clear();
                    no_of_ItemCount = 0;
                    no_of_orders = 0;
                     totalappMarkupPercentageValue =0;
        totalPhoneMarkupPercentageValue =0;
        totalCreditMarkupPercentageValue =0;

                    deliveryCharge_hashmap.clear();
                    deliveryCharge_hashmap.clear();
                  deliveryChargeOrderidArray.clear();
                  phoneorderdeliveryChargeOrderidArray.clear();
                  phoneorderdeliveryCharge_hashmap.clear();
                    ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                    addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                    // runthread();

                    Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);

                }


                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_5");
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(ConsolidatedReportSubCtgywise.this).add(jsonObjectRequest);

    }






    private void getItemDetailsFromItemDespArray(Modal_OrderDetails modal_orderDetailsfromResponse, String ordertype, String orderid) {

        if (ordertype.equals("") || ordertype.equals(null) || ordertype.equals("NULL")) {
            try {

                ordertype = String.valueOf(modal_orderDetailsfromResponse.getOrdertype().toUpperCase());

            } catch (Exception e) {
                ordertype = "";
                modal_orderDetailsfromResponse.ordertype = "";

                e.printStackTrace();
            }
        }


        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        // String newOrderWeightInGrams;
        double newweight, gstAmount = 0, tmcprice = 0 ,tmcprice_withoutMarkup_value =0 , markupperentage =0, markupValue =0 ;
        String menuitemidd = "", subCtgyKey = "", itemname = "", quantityString = "", tmcprice_string = "";
        int quantity = 0;
        try {
            JSONArray jsonArray = modal_orderDetailsfromResponse.getItemdesp();

            for (int i = 0; i < jsonArray.length(); i++) {
                //Log.d(Constants.TAG, "this  jsonArray.length()" + jsonArray.length());

                JSONObject json = jsonArray.getJSONObject(i);
                //Log.d(Constants.TAG, "this json" + json.toString());
                boolean isItemFoundinMenu = false;

                Modal_OrderDetails modal_orderDetails_ItemDesp = new Modal_OrderDetails();

                //addOrderedItemAmountDetails(json,paymentMode);

                if (json.has("menuitemid")) {
                    menuitemidd = String.valueOf(json.get("menuitemid"));
                    no_of_ItemCount++;
                    modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));
                    try {

                        String ItemName = "";
                        if (json.has("itemname")) {
                            ItemName = String.valueOf(json.get("itemname"));
                        }

                        try {
                            for (int menuiterator = 0; menuiterator < MenuItem.size(); menuiterator++) {
                                Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(menuiterator);
                                String menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                                if (menuitemidd.equals("")) {
                                    String ItemNamefromMenu = String.valueOf(modal_menuItemSettings.getItemname().toString());
                                    if (ItemName.equals(ItemNamefromMenu)) {
                                        menuitemidd = menuItemId;
                                        modal_orderDetails_ItemDesp.menuitemid = menuitemidd;
                                    }
                                }


                                String reportname = String.valueOf(modal_menuItemSettings.getReportname());

                                if (menuItemId.equals(menuitemidd)) {
                                    isItemFoundinMenu = true;

                                    if ((!reportname.equals("")) && (!reportname.equals("null")) && (!reportname.equals("\r"))) {
                                        modal_orderDetails_ItemDesp.itemname = String.valueOf(reportname);
                                        itemname = String.valueOf(reportname);
                                    } else {
                                        modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));String.valueOf(json.get("itemname"));
                                        itemname = String.valueOf(json.get("itemname"));
                                    }

                                    modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf(modal_menuItemSettings.getPricetypeforpos());


                                }

                            }
                            if (!isItemFoundinMenu) {
                              /*  if(vendorKey.equals("vendor_5")){
                                    modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf(json.get("pricetype"));
                                    modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                                    itemname = String.valueOf(json.get("itemname"));
                                }
                                else {

                               */
                                    modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf("tmcprice");

                                    modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                                    itemname = String.valueOf(json.get("itemname"));
                                }
                            //}
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        modal_orderDetails_ItemDesp.ordertype = modal_orderDetailsfromResponse.getOrdertype();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        modal_orderDetails_ItemDesp.paymentmode = modal_orderDetailsfromResponse.getPaymentmode();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if (json.has("tmcsubctgykey")) {
                            subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                            if(vendorKey.equals("vendor_5")){
                                if(SubCtgyKey_hashmap.containsKey(subCtgyKey)){
                                    modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                                    subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                }
                                else{
                                    modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                    subCtgyKey = String.valueOf("Miscellaneous");
                                }
                            }
                            else {
                                if (subCtgyKey.equals("") || subCtgyKey.equals("0")) {
                                    modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                    subCtgyKey = String.valueOf("Miscellaneous");
                                }
                                else {
                                    modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                                    subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));

                                }
                            }
                        } else {
                            modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                            subCtgyKey = String.valueOf("Miscellaneous");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                        subCtgyKey = String.valueOf("Miscellaneous");


                    }

                    if (json.has("tmcprice")) {
                        try {
                            tmcprice_string = (String.valueOf(json.get("tmcprice")));
                            tmcprice = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (json.has("appmarkuppercentage")) {

                        try {
                            markupperentage = Double.parseDouble(String.valueOf(json.get("appmarkuppercentage")));

                        } catch (Exception e) {
                            markupperentage = 0;
                            e.printStackTrace();
                        }
                    }
                    else{
                        markupperentage =0;
                    }



                    if (json.has("quantity")) {

                        try {
                            quantity = Integer.parseInt(String.valueOf(json.get("quantity")));
                            if (quantity > 1) {
                                no_of_ItemCount = (no_of_ItemCount - 1);
                                no_of_ItemCount = (no_of_ItemCount + quantity);
                            }
                            quantityString = String.valueOf(json.get("quantity"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    if (json.has("gstamount")) {

                        try {
                            gstAmount = Double.parseDouble(String.valueOf(json.get("gstamount")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    /*
                    if (json.has("marinadeitemdesp")) {
                        //Log.i(Constants.TAG, "There is  Marinade ItemDesp  ");
                        Modal_OrderDetails marinade_modal_orderDetails_ItemDesp = new Modal_OrderDetails();
                        no_of_ItemCount++;
                        String SubCtgywiseWeightString ="" , SubCtgywiseQuantityString ="";
                        double marinade_grossweightingramsDouble = 0 ;
                        int marinadeQuantityInt = 0;
                        String marinadeQuantityString = "0" ;
                        String marinade_grossweightingramsString ="0";
                        double marinadesObjectquantity = 1, marinadesObjectgstAmount = 0, marinadesObjectpayableAmount = 0;
                        JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                        String marinadeitemmenuItemId = String.valueOf(marinadesObject.get("menuitemid"));
                        String marinadeitemmenuItemName = String.valueOf(marinadesObject.get("itemname"));

                        try {
                            for (int menuiterator = 0; menuiterator < MenuItem.size(); menuiterator++) {
                                Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(menuiterator);
                                String menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                                if (menuitemidd.equals("")) {
                                    String ItemNamefromMenu = String.valueOf(modal_menuItemSettings.getItemname().toString());
                                    if (marinadeitemmenuItemName.equals(ItemNamefromMenu)) {
                                        menuitemidd = menuItemId;
                                        marinade_modal_orderDetails_ItemDesp.menuitemid = menuitemidd;
                                    }
                                }


                                String reportname = String.valueOf(modal_menuItemSettings.getReportname());

                                if (menuItemId.equals(menuitemidd)) {
                                    isItemFoundinMenu = true;

                                    if ((!reportname.equals("")) && (!reportname.equals("null")) && (!reportname.equals("\r"))) {
                                        marinade_modal_orderDetails_ItemDesp.itemname = String.valueOf(reportname);
                                        itemname = String.valueOf(reportname);
                                    } else {
                                        marinade_modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));String.valueOf(json.get("itemname"));
                                        itemname = String.valueOf(json.get("itemname"));
                                    }

                                    marinade_modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf(modal_menuItemSettings.getPricetypeforpos());


                                }

                            }

                                marinade_modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf("tmcprice");

                                marinade_modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                                itemname = String.valueOf(json.get("itemname"));
                            }
                            //}
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }



                        String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));
                        if (marinadesObject.has("tmcprice")) {
                            try {
                                marinadesObjectpayableAmount = Double.parseDouble(String.valueOf(marinadesObject.get("tmcprice")));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (marinadesObject.has("gstamount")) {
                            try {
                                marinadesObjectgstAmount = Double.parseDouble(String.valueOf(marinadesObject.get("gstamount")));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (marinadesObject.has("quantity")) {
                            try {
                                int marinadequantityInt = 0;
                                marinadesObjectquantity = Double.parseDouble(String.valueOf(json.get("quantity")));
                                try {
                                    marinadequantityInt = Integer.parseInt(String.valueOf(json.get("quantity")));
                                } catch (Exception e) {
                                    marinadequantityInt = Integer.parseInt(String.valueOf((int) marinadesObjectquantity));
                                    e.printStackTrace();
                                }
                                if (marinadesObjectquantity > 1) {
                                    no_of_ItemCount = (no_of_ItemCount - 1);
                                    no_of_ItemCount = (no_of_ItemCount + marinadequantityInt);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            marinadesObjectpayableAmount = marinadesObjectpayableAmount + marinadesObjectgstAmount;
                            marinadesObjectpayableAmount = marinadesObjectpayableAmount * marinadesObjectquantity;
                            marinadesObjectgstAmount = marinadesObjectgstAmount * quantity;


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        String marinadesubCtgyKey = "";
                        try {
                            if (marinadesObject.has("tmcsubctgykey")) {


                                if(vendorKey.equals("vendor_5")){
                                    if(SubCtgyKey_hashmap.containsKey(subCtgyKey)){
                                        modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                                        subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                    }
                                    else{
                                        modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                        subCtgyKey = String.valueOf("Miscellaneous");
                                    }
                                }
                                else {
                                marinadesubCtgyKey = String.valueOf(marinadesObject.get("tmcsubctgykey"));
                                if (marinadesubCtgyKey.equals("") || marinadesubCtgyKey.equals("0")) {
                                    marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                    marinadesubCtgyKey = String.valueOf("Miscellaneous");
                                } else {
                                    marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf(marinadesObject.get("tmcsubctgykey"));
                                    marinadesubCtgyKey = String.valueOf(marinadesObject.get("tmcsubctgykey"));

                                }
                                 }
                            }
                            else {
                                marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                marinadesubCtgyKey = String.valueOf("Miscellaneous");
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();

                            marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                            marinadesubCtgyKey = String.valueOf("Miscellaneous");


                        }

                        marinade_modal_orderDetails_ItemDesp.menuitemid = String.valueOf(menuitemidd);

                        marinade_modal_orderDetails_ItemDesp.tmcprice = (String.valueOf(marinadesObjectpayableAmount));
                        marinade_modal_orderDetails_ItemDesp.gstamount = String.valueOf(marinadesObjectgstAmount);
                        marinade_modal_orderDetails_ItemDesp.quantity = String.valueOf(json.get("quantity"));
                        marinade_modal_orderDetails_ItemDesp.itemname = marinadeitemName + " - Marinade ";
                        try {
                            if (SubCtgywiseTotalArray.contains(marinadesubCtgyKey)) {
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(marinadesubCtgyKey);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }

                                if(vendorKey.equals("vendor_5")) {
                                    if (marinade_modal_orderDetails_ItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {

                                         marinade_grossweightingramsDouble = 0 ;
                                         marinadeQuantityInt = 0;
                                         marinadeQuantityString = "0" ;
                                         marinade_grossweightingramsString ="0";
                                        if (marinadesObject.has("grossweightingrams")) {
                                            try {
                                                marinade_grossweightingramsDouble = Double.parseDouble(String.valueOf(marinadesObject.get("grossweightingrams")));

                                            } catch (Exception e) {
                                                marinade_grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            marinade_grossweightingramsDouble = 0;
                                        }

                                        if(marinade_grossweightingramsDouble==0){


                                            if (marinadesObject.has("grossweight")) {
                                                try {
                                                    marinade_grossweightingramsString = String.valueOf(marinadesObject.get("grossweight"));

                                                } catch (Exception e) {
                                                    marinade_grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                marinade_grossweightingramsString = "0";

                                                if (marinadesObject.has("netweight")) {
                                                    try {
                                                        marinade_grossweightingramsString = String.valueOf(marinadesObject.get("netweight"));

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    marinade_grossweightingramsString = "0";
                                                }
                                                try {
                                                    if (marinade_grossweightingramsString.contains("t")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('t'));

                                                    } else if (marinade_grossweightingramsString.contains("-")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('-'));

                                                    }

                                                    marinade_grossweightingramsString  = marinade_grossweightingramsString.replaceAll("[^\\d.]", "");
                                                    try {
                                                        marinade_grossweightingramsDouble = Double.parseDouble(marinade_grossweightingramsString);

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsDouble = 0;
                                                        e.printStackTrace();
                                                    }

                                                }
                                                catch (Exception e){
                                                    marinade_grossweightingramsDouble = 0;
                                                    e.printStackTrace();
                                                }

                                            }

                                        }
                                       

                                        try{
                                            marinadeQuantityString = String.valueOf(marinadesObject.get("quantity"));
                                            marinadeQuantityString  = marinadeQuantityString.replaceAll("[^\\d.]", "");
                                            marinadeQuantityInt = Integer.parseInt(marinadeQuantityString);


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                       



                                    }
                                    else{


                                         marinadeQuantityInt = 0;
                                         marinadeQuantityString = "0" ;
                                        try{
                                            marinadeQuantityString = String.valueOf(marinadesObject.get("quantity"));
                                            marinadeQuantityString  = marinadeQuantityString.replaceAll("[^\\d.]", "");
                                            marinadeQuantityInt = Integer.parseInt(marinadeQuantityString);


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        


                                    }
                                }
                                
                                
                                
                                if (isAlreadyAvailabe) {
                                    String SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(marinadesubCtgyKey);
                                    double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                    SubCtgywisetotalDouble = SubCtgywisetotalDouble + marinadesObjectpayableAmount;
                                    SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                    if(vendorKey.equals("vendor_5")){
                                       
                                       
                                        SubCtgywiseWeightString ="";
                                        SubCtgywiseWeightString = SubCtgywiseWeightHashmap.get(marinadesubCtgyKey);
                                        double SubCtgywiseWeightDouble = Double.parseDouble(SubCtgywiseWeightString);
                                        marinade_grossweightingramsDouble = marinade_grossweightingramsDouble  * marinadeQuantityInt ;
                                        SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + marinade_grossweightingramsDouble;
                                        SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                        SubCtgywiseQuantityString ="";
                                        SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(marinadesubCtgyKey);
                                        int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                        SubCtgywiseQuantityint = SubCtgywiseQuantityint + marinadeQuantityInt;
                                        SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);

                                        if (SDK_INT >= Build.VERSION_CODES.N) {
                                            SubCtgywiseQuantityHashmap.replace(marinadesubCtgyKey , SubCtgywiseQuantityString);
                                            SubCtgywiseWeightHashmap.replace(marinadesubCtgyKey , SubCtgywiseWeightString);

                                        } else {
                                            SubCtgywiseQuantityHashmap.remove(marinadesubCtgyKey);
                                            SubCtgywiseWeightHashmap.remove(marinadesubCtgyKey);

                                            SubCtgywiseWeightHashmap.put(marinadesubCtgyKey , SubCtgywiseWeightString);
                                            SubCtgywiseQuantityHashmap.put(marinadesubCtgyKey , SubCtgywiseQuantityString);

                                        }
                                        
                                    }
                                    if (SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseTotalHashmap.replace(marinadesubCtgyKey, SubCtgywisetotalString);

                                    } 
                                    else {
                                        SubCtgywiseTotalHashmap.remove(marinadesubCtgyKey);
                                        SubCtgywiseTotalHashmap.put(marinadesubCtgyKey, SubCtgywisetotalString);

                                    }
                                }
                                else {
                                    SubCtgywiseTotalHashmap.put(marinadesubCtgyKey, String.valueOf(marinadesObjectpayableAmount));


                                    double grossweightingramsDouble = 0 ;
                                    int QuantityInt = 0;
                                    String QuantityString = "0" ;
                                    String grossweightingramsString ="0";





                                    if(vendorKey.equals("vendor_5")) {
                                        if (modal_orderDetails_ItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {

                                            grossweightingramsDouble = 0 ;
                                            QuantityInt = 0;
                                            QuantityString = "0" ;
                                            grossweightingramsString ="0";
                                            if (json.has("grossweightingrams")) {
                                                try {
                                                    grossweightingramsDouble = Double.parseDouble(String.valueOf(json.get("grossweightingrams")));

                                                } catch (Exception e) {
                                                    grossweightingramsDouble = 0;
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                grossweightingramsDouble = 0;
                                            }

                                            if(grossweightingramsDouble==0){


                                                if (json.has("grossweight")) {
                                                    try {
                                                        grossweightingramsString = String.valueOf(json.get("grossweight"));

                                                    } catch (Exception e) {
                                                        grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else {
                                                    grossweightingramsString = "0";

                                                    if (json.has("netweight")) {
                                                        try {
                                                            grossweightingramsString = String.valueOf(json.get("netweight"));

                                                        } catch (Exception e) {
                                                            grossweightingramsString = "0";
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        grossweightingramsString = "0";
                                                    }
                                                    try {
                                                        if (grossweightingramsString.contains("t")) {
                                                            grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('t'));

                                                        } else if (grossweightingramsString.contains("-")) {
                                                            grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('-'));

                                                        }

                                                        grossweightingramsString  = grossweightingramsString.replaceAll("[^\\d.]", "");
                                                        try {
                                                            grossweightingramsDouble = Double.parseDouble(grossweightingramsString);

                                                        } catch (Exception e) {
                                                            grossweightingramsDouble = 0;
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                    catch (Exception e){
                                                        grossweightingramsDouble = 0;
                                                        e.printStackTrace();
                                                    }

                                                }

                                            }


                                            try{
                                                QuantityString = String.valueOf(json.get("quantity"));
                                                QuantityString  = QuantityString.replaceAll("[^\\d.]", "");
                                                QuantityInt = Integer.parseInt(QuantityString);


                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }




                                        }
                                        else{


                                            QuantityInt = 0;
                                            QuantityString = "0" ;
                                            try{
                                                QuantityString = String.valueOf(json.get("quantity"));
                                                QuantityString  = QuantityString.replaceAll("[^\\d.]", "");
                                                QuantityInt = Integer.parseInt(QuantityString);


                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }



                                        }
                                    }

                                    if(vendorKey.equals("vendor_5")){


                                        SubCtgywiseWeightString ="";
                                        SubCtgywiseWeightString = SubCtgywiseWeightHashmap.get(subCtgyKey);
                                        double SubCtgywiseWeightDouble = Double.parseDouble(SubCtgywiseWeightString);
                                        grossweightingramsDouble = grossweightingramsDouble * QuantityInt;
                                        SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + grossweightingramsDouble;
                                        SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                        SubCtgywiseQuantityString ="";
                                        SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(subCtgyKey);
                                        int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                        SubCtgywiseQuantityint = SubCtgywiseQuantityint + QuantityInt;
                                        SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);



                                    }

                                    if(vendorKey.equals("vendor_5")) {


                                        SubCtgywiseWeightHashmap.put(marinadesubCtgyKey , SubCtgywiseWeightString);
                                        SubCtgywiseQuantityHashmap.put(marinadesubCtgyKey , SubCtgywiseQuantityString);
                                    }

                                   

                                }
                            } 
                            else {
                                SubCtgywiseTotalArray.add(marinadesubCtgyKey);
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(marinadesubCtgyKey);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }



                                if(vendorKey.equals("vendor_5")) {
                                    if (marinade_modal_orderDetails_ItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {

                                        marinade_grossweightingramsDouble = 0 ;
                                        marinadeQuantityInt = 0;
                                        marinadeQuantityString = "0" ;
                                        marinade_grossweightingramsString ="0";
                                        if (marinadesObject.has("grossweightingrams")) {
                                            try {
                                                marinade_grossweightingramsDouble = Double.parseDouble(String.valueOf(marinadesObject.get("grossweightingrams")));

                                            } catch (Exception e) {
                                                marinade_grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            marinade_grossweightingramsDouble = 0;
                                        }

                                        if(marinade_grossweightingramsDouble==0){


                                            if (marinadesObject.has("grossweight")) {
                                                try {
                                                    marinade_grossweightingramsString = String.valueOf(marinadesObject.get("grossweight"));

                                                } catch (Exception e) {
                                                    marinade_grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                marinade_grossweightingramsString = "0";

                                                if (marinadesObject.has("netweight")) {
                                                    try {
                                                        marinade_grossweightingramsString = String.valueOf(marinadesObject.get("netweight"));

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    marinade_grossweightingramsString = "0";
                                                }
                                                try {
                                                    if (marinade_grossweightingramsString.contains("t")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('t'));

                                                    } else if (marinade_grossweightingramsString.contains("-")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('-'));

                                                    }

                                                    marinade_grossweightingramsString  = marinade_grossweightingramsString.replaceAll("[^\\d.]", "");
                                                    try {
                                                        marinade_grossweightingramsDouble = Double.parseDouble(marinade_grossweightingramsString);

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsDouble = 0;
                                                        e.printStackTrace();
                                                    }

                                                }
                                                catch (Exception e){
                                                    marinade_grossweightingramsDouble = 0;
                                                    e.printStackTrace();
                                                }

                                            }

                                        }


                                        try{
                                            marinadeQuantityString = String.valueOf(marinadesObject.get("quantity"));
                                            marinadeQuantityString  = marinadeQuantityString.replaceAll("[^\\d.]", "");
                                            marinadeQuantityInt = Integer.parseInt(marinadeQuantityString);


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }




                                    }
                                    else{


                                        marinadeQuantityInt = 0;
                                        marinadeQuantityString = "0" ;
                                        try{
                                            marinadeQuantityString = String.valueOf(marinadesObject.get("quantity"));
                                            marinadeQuantityString  = marinadeQuantityString.replaceAll("[^\\d.]", "");
                                            marinadeQuantityInt = Integer.parseInt(marinadeQuantityString);


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }



                                    }
                                }

                                
                                
                                if (isAlreadyAvailabe) {
                                    String SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(marinadesubCtgyKey);
                                    double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                    SubCtgywisetotalDouble = SubCtgywisetotalDouble + marinadesObjectpayableAmount;
                                    SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);




                                    if(vendorKey.equals("vendor_5")) {



                                        marinadeQuantityInt = 0;
                                        marinadeQuantityString = "0" ;
                                        try{
                                            marinadeQuantityString = String.valueOf(marinadesObject.get("quantity"));
                                            marinadeQuantityString  = marinadeQuantityString.replaceAll("[^\\d.]", "");
                                            marinadeQuantityInt = Integer.parseInt(marinadeQuantityString);


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }






                                        marinade_grossweightingramsDouble = 0 ;
                                        marinade_grossweightingramsString ="0";
                                        if (marinadesObject.has("grossweightingrams")) {
                                            try {
                                                marinade_grossweightingramsDouble = Double.parseDouble(String.valueOf(marinadesObject.get("grossweightingrams")));

                                            } catch (Exception e) {
                                                marinade_grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            marinade_grossweightingramsDouble = 0;
                                        }

                                        if(marinade_grossweightingramsDouble==0){


                                            if (marinadesObject.has("grossweight")) {
                                                try {
                                                    marinade_grossweightingramsString = String.valueOf(marinadesObject.get("grossweight"));

                                                } catch (Exception e) {
                                                    marinade_grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                marinade_grossweightingramsString = "0";

                                                if (marinadesObject.has("netweight")) {
                                                    try {
                                                        marinade_grossweightingramsString = String.valueOf(marinadesObject.get("netweight"));

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    marinade_grossweightingramsString = "0";
                                                }
                                                try {
                                                    if (marinade_grossweightingramsString.contains("t")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('t'));

                                                    } else if (marinade_grossweightingramsString.contains("-")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('-'));

                                                    }

                                                    marinade_grossweightingramsString  = marinade_grossweightingramsString.replaceAll("[^\\d.]", "");
                                                    try {
                                                        marinade_grossweightingramsDouble = Double.parseDouble(marinade_grossweightingramsString);

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsDouble = 0;
                                                        e.printStackTrace();
                                                    }

                                                }
                                                catch (Exception e){
                                                    marinade_grossweightingramsDouble = 0;
                                                    e.printStackTrace();
                                                }

                                            }

                                        }
                                        
                                        
                                        
                                        
                                        
                                    }


                                    if(vendorKey.equals("vendor_5")){


                                        SubCtgywiseWeightString ="";
                                        SubCtgywiseWeightString = SubCtgywiseWeightHashmap.get(subCtgyKey);
                                        double SubCtgywiseWeightDouble = Double.parseDouble(SubCtgywiseWeightString);
                                        marinade_grossweightingramsDouble = marinade_grossweightingramsDouble  * marinadeQuantityInt ;

                                        SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + marinade_grossweightingramsDouble;
                                        SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                        SubCtgywiseQuantityString ="";
                                        SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(subCtgyKey);
                                        int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                        SubCtgywiseQuantityint = SubCtgywiseQuantityint + marinadeQuantityInt;
                                        SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);



                                    }



                                        if (SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseTotalHashmap.replace(marinadesubCtgyKey, SubCtgywisetotalString);
                                            if(vendorKey.equals("vendor_5")) {

                                                SubCtgywiseQuantityHashmap.replace(marinadesubCtgyKey, SubCtgywiseQuantityString);
                                                SubCtgywiseWeightHashmap.replace(marinadesubCtgyKey, SubCtgywiseWeightString);
                                            }



                                        } 
                                        else {
                                        SubCtgywiseTotalHashmap.remove(marinadesubCtgyKey);
                                        SubCtgywiseTotalHashmap.put(marinadesubCtgyKey, SubCtgywisetotalString);

                                            if(vendorKey.equals("vendor_5")) {

                                                SubCtgywiseQuantityHashmap.remove(marinadesubCtgyKey);
                                                SubCtgywiseWeightHashmap.remove(marinadesubCtgyKey);

                                                SubCtgywiseWeightHashmap.put(marinadesubCtgyKey , SubCtgywiseWeightString);
                                                SubCtgywiseQuantityHashmap.put(marinadesubCtgyKey , SubCtgywiseQuantityString);
                                            }
                                        
                                        
                                    }
                                }
                                else {



                                    if(vendorKey.equals("vendor_5")) {



                                        marinadeQuantityInt = 0;
                                        marinadeQuantityString = "0" ;
                                        try{
                                            marinadeQuantityString = String.valueOf(marinadesObject.get("quantity"));
                                            marinadeQuantityString  = marinadeQuantityString.replaceAll("[^\\d.]", "");
                                            marinadeQuantityInt = Integer.parseInt(marinadeQuantityString);


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }






                                        marinade_grossweightingramsDouble = 0 ;
                                        marinade_grossweightingramsString ="0";
                                        if (marinadesObject.has("grossweightingrams")) {
                                            try {
                                                marinade_grossweightingramsDouble = Double.parseDouble(String.valueOf(marinadesObject.get("grossweightingrams")));

                                            } catch (Exception e) {
                                                marinade_grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            marinade_grossweightingramsDouble = 0;
                                        }

                                        if(marinade_grossweightingramsDouble==0){


                                            if (marinadesObject.has("grossweight")) {
                                                try {
                                                    marinade_grossweightingramsString = String.valueOf(marinadesObject.get("grossweight"));

                                                } catch (Exception e) {
                                                    marinade_grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                marinade_grossweightingramsString = "0";

                                                if (marinadesObject.has("netweight")) {
                                                    try {
                                                        marinade_grossweightingramsString = String.valueOf(marinadesObject.get("netweight"));

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    marinade_grossweightingramsString = "0";
                                                }
                                                try {
                                                    if (marinade_grossweightingramsString.contains("t")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('t'));

                                                    } else if (marinade_grossweightingramsString.contains("-")) {
                                                        marinade_grossweightingramsString = marinade_grossweightingramsString.substring(marinade_grossweightingramsString.indexOf('-'));

                                                    }

                                                    marinade_grossweightingramsString  = marinade_grossweightingramsString.replaceAll("[^\\d.]", "");
                                                    try {
                                                        marinade_grossweightingramsDouble = Double.parseDouble(marinade_grossweightingramsString);

                                                    } catch (Exception e) {
                                                        marinade_grossweightingramsDouble = 0;
                                                        e.printStackTrace();
                                                    }

                                                }
                                                catch (Exception e){
                                                    marinade_grossweightingramsDouble = 0;
                                                    e.printStackTrace();
                                                }

                                            }

                                        }





                                    }


                                    if(vendorKey.equals("vendor_5")){


                                        SubCtgywiseWeightString ="";
                                        SubCtgywiseWeightString = SubCtgywiseWeightHashmap.get(subCtgyKey);
                                        double SubCtgywiseWeightDouble = Double.parseDouble(SubCtgywiseWeightString);
                                        marinade_grossweightingramsDouble = marinade_grossweightingramsDouble  * marinadeQuantityInt ;

                                        SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + marinade_grossweightingramsDouble;
                                        SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                        SubCtgywiseQuantityString ="";
                                        SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(subCtgyKey);
                                        int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                        SubCtgywiseQuantityint = SubCtgywiseQuantityint + marinadeQuantityInt;
                                        SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);



                                    }


                                    if(vendorKey.equals("vendor_5")) {


                                        SubCtgywiseWeightHashmap.put(marinadesubCtgyKey , SubCtgywiseWeightString);
                                        SubCtgywiseQuantityHashmap.put(marinadesubCtgyKey , SubCtgywiseQuantityString);
                                    }

                                    SubCtgywiseTotalHashmap.put(marinadesubCtgyKey, String.valueOf(marinadesObjectpayableAmount));

                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (ordertypeArray.contains(ordertype)) {
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfPaymentdetailisAlreadyAvailableInArray(ordertype);

                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
                            }
                            if (isAlreadyAvailabe) {
                                Modal_OrderDetails modal_orderDetails = ordertypeHashmap.get(ordertype);
                                if (ordertype.equals(Constants.CREDIT) || ordertype.equals("credit")) {
                                    double credit_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCreditSales());

                                    double credit_amount = marinadesObjectpayableAmount + credit_amount_fromhashmap;

                                    double newTotalCreditAmount = credit_amount;
                                    modal_orderDetails.setCreditSales(String.valueOf(newTotalCreditAmount));


                                }
                               else if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {
                                    double pos_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getPosSales());

                                    double pos_amount = marinadesObjectpayableAmount + pos_amount_fromhashmap;

                                    double newTotalPosAmount = pos_amount;
                                    modal_orderDetails.setPosSales(String.valueOf(newTotalPosAmount));


                                }
                                else if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {
                                    double appsales_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getAppSales());
                                    double Appsales_amount = marinadesObjectpayableAmount + appsales_amount_fromhashmap;
                                    double newTotalAppSalesAmount = Appsales_amount;
                                    modal_orderDetails.setAppSales(String.valueOf(newTotalAppSalesAmount));


                                }


                                else  if (ordertype.equals(Constants.PhoneOrder) || ordertype.equals("Phone Order")) {
                                    double phoneOrder_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getPhoneOrderSales());

                                    double phoneOrder_amount = marinadesObjectpayableAmount + phoneOrder_amount_fromhashmap;

                                    double newTotalphoneOrderAmount = phoneOrder_amount;
                                    modal_orderDetails.setPhoneOrderSales(String.valueOf(newTotalphoneOrderAmount));


                                }

                                else  if (ordertype.equals(Constants.WholeSaleOrder) || ordertype.equals("WholeSale Order")) {
                                    double WholeSaleOrder_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getWholeSaleOrderSales());

                                    double WholeSaleOrder_amount = marinadesObjectpayableAmount + WholeSaleOrder_amount_fromhashmap;

                                    double newTotalWholeSaleOrderAmount = WholeSaleOrder_amount;
                                    modal_orderDetails.setWholeSaleOrderSales(String.valueOf(newTotalWholeSaleOrderAmount));


                                }
                                else  if (ordertype.equals(Constants.SwiggyOrder) || ordertype.equals("Swiggy Order")) {
                                    double swiggyOrder_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getSwiggySales());

                                    double swiggyOrder_amount = marinadesObjectpayableAmount + swiggyOrder_amount_fromhashmap;

                                    double newTotalswiggyOrderAmount = swiggyOrder_amount;
                                    modal_orderDetails.setSwiggySales(String.valueOf(newTotalswiggyOrderAmount));


                                }

                                else  if (ordertype.equals(Constants.DunzoOrder) || ordertype.equals("Dunzo Order")) {
                                    double dunzoOrder_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getDunzoSales());

                                    double dunzoOrder_amount = marinadesObjectpayableAmount + dunzoOrder_amount_fromhashmap;

                                    double newTotaldunzoOrderAmount = dunzoOrder_amount;
                                    modal_orderDetails.setDunzoSales(String.valueOf(newTotaldunzoOrderAmount));


                                }
                                else  if (ordertype.equals(Constants.BigBasket) || ordertype.equals("BigBasket Order")) {
                                    double bigBasketOrder_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales());

                                    double bigBasketOrder_amount = marinadesObjectpayableAmount + bigBasketOrder_amount_fromhashmap;

                                    double newTotalbigBasketOrderAmount = bigBasketOrder_amount;
                                    modal_orderDetails.setBigBasketSales(String.valueOf(newTotalbigBasketOrderAmount));


                                }

                            }

                        }
                        else {
                            ordertypeArray.add(ordertype);
                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                            if (ordertype.equals(Constants.CREDIT) || ordertype.equals("credit")) {
                                double creditSales_amount = marinadesObjectpayableAmount;
                                double Gst_creditSales_amount = marinadesObjectgstAmount;
                                double newTotalCreditAmount = creditSales_amount;

                                modal_orderDetails.setCreditSales(String.valueOf((newTotalCreditAmount)));


                            }

                            else if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {
                                double posSales_amount = marinadesObjectpayableAmount;
                                double Gst_posSales_amount = marinadesObjectgstAmount;
                                double newTotalCashAmount = posSales_amount;

                                modal_orderDetails.setPosSales(String.valueOf((newTotalCashAmount)));


                            }
                            else if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {
                                double appSalesAmount = marinadesObjectpayableAmount;
                                double Gst_app_Sales_amount = marinadesObjectgstAmount;
                                double newTotalAppSalesAmount = appSalesAmount;

                                modal_orderDetails.setAppSales(String.valueOf((newTotalAppSalesAmount)));


                            }


                           else if (ordertype.equals(Constants.PhoneOrder) || ordertype.equals("Phone Order")) {
                                double phoneOrderSales_amount = marinadesObjectpayableAmount;
                                double Gst_phoneOrderSales_amount = marinadesObjectgstAmount;
                                double newTotalCashAmount = phoneOrderSales_amount;

                                modal_orderDetails.setPhoneOrderSales(String.valueOf((newTotalCashAmount)));


                            }
                           else if (ordertype.equals(Constants.WholeSaleOrder) || ordertype.equals("WholeSale Order")) {
                                double wholeSaleOrderSales_amount = marinadesObjectpayableAmount;
                                double Gst_wholeSaleOrderSales_amount = marinadesObjectgstAmount;
                                double newTotalAmount = wholeSaleOrderSales_amount;

                                modal_orderDetails.setWholeSaleOrderSales(String.valueOf((newTotalAmount)));


                            }
                           else if (ordertype.equals(Constants.SwiggyOrder) || ordertype.equals("Swiggy Order")) {
                                double swiggyOrderSales_amount = marinadesObjectpayableAmount;
                                double Gst_swiggyOrderSales_amount = marinadesObjectgstAmount;
                                double newTotalAmount = swiggyOrderSales_amount;

                                modal_orderDetails.setSwiggySales(String.valueOf((newTotalAmount)));


                            }


                           else if (ordertype.equals(Constants.DunzoOrder) || ordertype.equals("Dunzo Order")) {
                                double dunzoOrderSales_amount = marinadesObjectpayableAmount;
                                double Gst_dunzoOrderSales_amount = marinadesObjectgstAmount;
                                double newTotalAmount = dunzoOrderSales_amount;

                                modal_orderDetails.setDunzoSales(String.valueOf((newTotalAmount)));


                            }


                           else if (ordertype.equals(Constants.BigBasket) || ordertype.equals("BigBasket Order")) {
                                double bigBasketOrder_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales());

                                double bigBasketOrder_amount = marinadesObjectpayableAmount + bigBasketOrder_amount_fromhashmap;

                                double newTotalbigBasketOrderAmount = bigBasketOrder_amount;
                                modal_orderDetails.setBigBasketSales(String.valueOf(newTotalbigBasketOrderAmount));


                            }


                            ordertypeHashmap.put(ordertype, modal_orderDetails);
                        }


                        if (Order_Item_List.contains(marinadeitemmenuItemId)) {
                            double payableAmount_marinade = 0, quantity_marinade = 0, gstAmount_marinade = 0;
                            Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(marinadeitemmenuItemId);
                            double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getTmcprice());
                            int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                            double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());
                            try {
                                payableAmount_marinade = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                quantity_marinade = marinadesObjectquantity + quantity_from_HashMap;
                                gstAmount_marinade = marinadesObjectgstAmount + gstAmount_from_HashMap;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity_marinade)));
                            modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf((payableAmount_marinade)));
                            modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount_marinade)));


                        }
                        else {
                            Order_Item_List.add(marinadeitemmenuItemId);
                            if (!tmcSubCtgykey.contains(marinadesubCtgyKey)) {
                                tmcSubCtgykey.add(marinadesubCtgyKey);
                            }
                            OrderItem_hashmap.put(marinadeitemmenuItemId, marinade_modal_orderDetails_ItemDesp);
                        }


                    }
                    */


                    try {
                        for (int menuiterator = 0; menuiterator < MenuItem.size(); menuiterator++) {
                            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(menuiterator);
                            String menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());
                            if (menuitemidd.equals("")) {
                                String itemmenuItemName = String.valueOf(json.get("itemname"));

                                String ItemNamefromMenu = String.valueOf(modal_menuItemSettings.getItemname().toString());
                                if (itemmenuItemName.equals(ItemNamefromMenu)) {
                                    menuitemidd = menuItemId;
                                    modal_orderDetails_ItemDesp.menuitemid = menuitemidd;
                                }
                            }


                            String reportname = String.valueOf(modal_menuItemSettings.getReportname());

                            if (menuItemId.equals(menuitemidd)) {
                                isItemFoundinMenu = true;

                                if ((!reportname.equals("")) && (!reportname.equals("null")) && (!reportname.equals("\r"))) {
                                    modal_orderDetails_ItemDesp.itemname = String.valueOf(reportname);
                                    itemname = String.valueOf(reportname);
                                } else {
                                    modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));String.valueOf(json.get("itemname"));
                                    itemname = String.valueOf(json.get("itemname"));
                                }

                                modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf(modal_menuItemSettings.getPricetypeforpos());


                            }

                        }
                        if (!isItemFoundinMenu) {
                              /*  if(vendorKey.equals("vendor_5")){
                                    modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf(json.get("pricetype"));
                                    modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                                    itemname = String.valueOf(json.get("itemname"));
                                }
                                else {

                               */
                            modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf("tmcprice");

                            modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                            itemname = String.valueOf(json.get("itemname"));
                        }
                        //}
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                    try{

                        tmcprice_withoutMarkup_value = tmcprice/(1+(markupperentage/100));

                        markupValue =  tmcprice - tmcprice_withoutMarkup_value ;
                        markupValue = markupValue *quantity;


                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    if(ordertype.equals(Constants.APPORDER)){
                        try{
                            totalappMarkupPercentageValue = markupValue + totalappMarkupPercentageValue;
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else if(ordertype.equals(Constants.PhoneOrder)){
                        try{
                            totalPhoneMarkupPercentageValue = markupValue + totalPhoneMarkupPercentageValue;
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    else if(ordertype.toUpperCase().equals(Constants.CREDIT)){
                        try{
                            totalCreditMarkupPercentageValue = markupValue + totalCreditMarkupPercentageValue;
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }










                    try {
                        tmcprice = tmcprice + gstAmount;
                        tmcprice = tmcprice * quantity;
                        //Log.i(Constants.TAG, "Consolidated Report new itemDespAmountwithquantity  " + tmcprice);

                        //int inttmcPrice = (int) Math.ceil(tmcprice);

                        // String tmcprice_string = String.valueOf(decimalFormat.format((inttmcPrice)));
                        double tmcpricewithgst = tmcprice;
                        //Log.i(Constants.TAG, "Consolidated Report new itemDespAmountwithquantity_withGSt  " + tmcpricewithgst);

                        //Log.i(Constants.TAG, "Consolidated Report  new item desporderid            " + orderid);

                        itemDespTotalAmount = itemDespTotalAmount + tmcpricewithgst;
                        //Log.i(Constants.TAG, "Consolidated Report  new itemDespAmount_totalAmount   " + tmcpricewithgst);
                        //Log.i(Constants.TAG, "Consolidated Report  new itemDespAmount " + itemDespTotalAmount);


                        String gstAmount_string = "";
                        try {
                            gstAmount = gstAmount * quantity;
                            gstAmount_string = String.valueOf(gstAmount);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {


                            modal_orderDetails_ItemDesp.tmcprice = String.valueOf(tmcpricewithgst);
                            modal_orderDetails_ItemDesp.quantity = String.valueOf(json.get("quantity"));
                            modal_orderDetails_ItemDesp.gstamount = String.valueOf(gstAmount_string);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                    if(vendorKey.equals("vendor_5")) {
                        if(!subCtgyKey.equals("tmcsubctgy_4")) {
                            try {
                                String SubCtgywiseWeightString = "", SubCtgywiseQuantityString = "";
                                double grossweightingramsDouble = 0;
                                int QuantityInt = 0;
                                String QuantityString = "0";
                                String grossweightingramsString = "0";
                                grossweightingramsDouble = 0;
                                QuantityInt = 0;
                                QuantityString = "0";
                                grossweightingramsString = "0";
                                if (json.has("grossweightingrams")) {
                                    try {
                                        grossweightingramsDouble = Double.parseDouble(String.valueOf(json.get("grossweightingrams")));

                                    } catch (Exception e) {
                                        grossweightingramsDouble = 0;
                                        e.printStackTrace();
                                    }
                                } else {
                                    grossweightingramsDouble = 0;
                                }

                                if (grossweightingramsDouble == 0) {


                                    if (json.has("grossweight")) {
                                        try {
                                            grossweightingramsString = String.valueOf(json.get("grossweight"));

                                        } catch (Exception e) {
                                            grossweightingramsString = "0";
                                            e.printStackTrace();
                                        }

                                        grossweightingramsString = grossweightingramsString.replaceAll("[^\\d.]", "");
                                        try{
                                            if(grossweightingramsString.equals("")){
                                                grossweightingramsDouble = 0;

                                            }
                                            else {
                                                grossweightingramsDouble = Double.parseDouble(grossweightingramsString);
                                            }
                                        }
                                        catch (Exception e){
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }


                                        if (grossweightingramsDouble == 0) {
                                            if (json.has("netweight")) {
                                                try {
                                                    grossweightingramsString = String.valueOf(json.get("netweight"));

                                                } catch (Exception e) {
                                                    grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                grossweightingramsString = "0";
                                            }
                                        }


                                    } else {
                                        grossweightingramsString = "0";

                                        if (json.has("netweight")) {
                                            try {
                                                grossweightingramsString = String.valueOf(json.get("netweight"));

                                            } catch (Exception e) {
                                                grossweightingramsString = "0";
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            grossweightingramsString = "0";
                                        }


                                    }
                                    try {
                                        if (grossweightingramsString.contains("t")) {
                                            grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('t'));

                                        } else if (grossweightingramsString.contains("-")) {
                                            grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('-'));

                                        } else if (grossweightingramsString.toUpperCase().contains("PC") || grossweightingramsString.toUpperCase().contains("SET") || grossweightingramsString.toUpperCase().contains("PACK")) {
                                            grossweightingramsString = "0";
                                        }

                                        grossweightingramsString = grossweightingramsString.replaceAll("[^\\d.]", "");
                                        try {
                                            grossweightingramsDouble = Double.parseDouble(grossweightingramsString);

                                        } catch (Exception e) {
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }

                                    } catch (Exception e) {
                                        grossweightingramsDouble = 0;
                                        e.printStackTrace();
                                    }
                                }



                                if (grossweightingramsDouble == 0) {
                                    modal_orderDetails_ItemDesp.tmcsubctgykey = "Miscellaneous";
                                    subCtgyKey = "Miscellaneous";
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }



                    try {
                        if (SubCtgywiseTotalArray.contains(subCtgyKey)) {
                            boolean isAlreadyAvailabe = false;

                            String SubCtgywiseWeightString ="" , SubCtgywiseQuantityString ="";
                            double grossweightingramsDouble = 0 ;
                            int QuantityInt = 0;
                            String QuantityString = "0" ;
                            String grossweightingramsString ="0";


                            if(vendorKey.equals("vendor_5")) {
                                if (modal_orderDetails_ItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {

                                    grossweightingramsDouble = 0 ;
                                    QuantityInt = 0;
                                    QuantityString = "0" ;
                                    grossweightingramsString ="0";
                                    if (json.has("grossweightingrams")) {
                                        try {
                                            grossweightingramsDouble = Double.parseDouble(String.valueOf(json.get("grossweightingrams")));

                                        } catch (Exception e) {
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        grossweightingramsDouble = 0;
                                    }

                                    if(grossweightingramsDouble==0){


                                        if (json.has("grossweight")) {
                                            try {
                                                grossweightingramsString = String.valueOf(json.get("grossweight"));

                                            } catch (Exception e) {
                                                grossweightingramsString = "0";
                                                e.printStackTrace();
                                            }
                                        }
                                        else {
                                            grossweightingramsString = "0";

                                            if (json.has("netweight")) {
                                                try {
                                                    grossweightingramsString = String.valueOf(json.get("netweight"));

                                                } catch (Exception e) {
                                                    grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                grossweightingramsString = "0";
                                            }
                                            try {
                                                if (grossweightingramsString.contains("t")) {
                                                    grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('t'));

                                                } else if (grossweightingramsString.contains("-")) {
                                                    grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('-'));

                                                }

                                                grossweightingramsString  = grossweightingramsString.replaceAll("[^\\d.]", "");
                                                try {
                                                    grossweightingramsDouble = Double.parseDouble(grossweightingramsString);

                                                } catch (Exception e) {
                                                    grossweightingramsDouble = 0;
                                                    e.printStackTrace();
                                                }

                                            }
                                            catch (Exception e){
                                                grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }

                                        }

                                    }


                                    try{
                                        QuantityString = String.valueOf(json.get("quantity"));
                                        QuantityString  = QuantityString.replaceAll("[^\\d.]", "");
                                        QuantityInt = Integer.parseInt(QuantityString);


                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }




                                }
                                else{

                                    grossweightingramsDouble = 0 ;
                                    QuantityInt = 0;
                                    QuantityString = "0" ;
                                    grossweightingramsString ="0";
                                    if (json.has("grossweightingrams")) {
                                        try {
                                            grossweightingramsDouble = Double.parseDouble(String.valueOf(json.get("grossweightingrams")));

                                        } catch (Exception e) {
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        grossweightingramsDouble = 0;
                                    }

                                    if(grossweightingramsDouble==0){


                                        if (json.has("grossweight")) {
                                            try {
                                                grossweightingramsString = String.valueOf(json.get("grossweight"));

                                            } catch (Exception e) {
                                                grossweightingramsString = "0";
                                                e.printStackTrace();
                                            }

                                            grossweightingramsString = grossweightingramsString.replaceAll("[^\\d.]", "");
                                            try{
                                                if(grossweightingramsString.equals("")){
                                                    grossweightingramsDouble = 0;

                                                }
                                                else {
                                                    grossweightingramsDouble = Double.parseDouble(grossweightingramsString);
                                                }
                                            }
                                            catch (Exception e){
                                                grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }


                                            if (grossweightingramsDouble == 0) {
                                                if (json.has("netweight")) {
                                                    try {
                                                        grossweightingramsString = String.valueOf(json.get("netweight"));

                                                    } catch (Exception e) {
                                                        grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else {
                                                    grossweightingramsString = "0";
                                                }
                                            }

                                        }
                                        else {
                                            grossweightingramsString = "0";

                                            if (json.has("netweight")) {
                                                try {
                                                    grossweightingramsString = String.valueOf(json.get("netweight"));

                                                } catch (Exception e) {
                                                    grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                grossweightingramsString = "0";
                                            }


                                        }

                                        try {
                                            if (grossweightingramsString.contains("t")) {
                                                grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('t'));

                                            } else if (grossweightingramsString.contains("-")) {
                                                grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('-'));

                                            }
                                            else if (grossweightingramsString.toUpperCase().contains("PC") || grossweightingramsString.toUpperCase().contains("SET")){
                                                grossweightingramsString = "0";
                                            }

                                            grossweightingramsString  = grossweightingramsString.replaceAll("[^\\d.]", "");
                                            try {
                                                grossweightingramsDouble = Double.parseDouble(grossweightingramsString);

                                            } catch (Exception e) {
                                                grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }

                                        }
                                        catch (Exception e){
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }



                                    }



                                    QuantityInt = 0;
                                    QuantityString = "0" ;
                                    try{
                                        QuantityString = String.valueOf(json.get("quantity"));
                                        QuantityString  = QuantityString.replaceAll("[^\\d.]", "");
                                        QuantityInt = Integer.parseInt(QuantityString);


                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    if(subCtgyKey.equals("tmcsubctgy_4")) {
                                        grossweightingramsDouble = 0;
                                    }



                                    }
                            }




                            try {
                                isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(subCtgyKey);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                String SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(subCtgyKey);
                                double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                SubCtgywisetotalDouble = SubCtgywisetotalDouble + tmcprice;
                                SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);


                                if(vendorKey.equals("vendor_5")){


                                    SubCtgywiseWeightString ="";
                                    SubCtgywiseWeightString = SubCtgywiseWeightHashmap.get(subCtgyKey);
                                    double SubCtgywiseWeightDouble = Double.parseDouble(SubCtgywiseWeightString);
                                    grossweightingramsDouble = grossweightingramsDouble * QuantityInt;

                                    SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + grossweightingramsDouble;
                                    SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                    SubCtgywiseQuantityString ="";
                                    SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(subCtgyKey);
                                    int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                    SubCtgywiseQuantityint = SubCtgywiseQuantityint + QuantityInt;
                                    SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);

                                    if (SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseQuantityHashmap.replace(subCtgyKey , SubCtgywiseQuantityString);
                                        SubCtgywiseWeightHashmap.replace(subCtgyKey , SubCtgywiseWeightString);

                                    } else {
                                        SubCtgywiseQuantityHashmap.remove(subCtgyKey);
                                        SubCtgywiseWeightHashmap.remove(subCtgyKey);

                                        SubCtgywiseWeightHashmap.put(subCtgyKey , SubCtgywiseWeightString);
                                        SubCtgywiseQuantityHashmap.put(subCtgyKey , SubCtgywiseQuantityString);

                                    }

                                }
                              
                                
                                
                                
                                
                                if (SDK_INT >= Build.VERSION_CODES.N) {
                                    SubCtgywiseTotalHashmap.replace(subCtgyKey, SubCtgywisetotalString);
                                } else {
                                    SubCtgywiseTotalHashmap.remove(subCtgyKey);
                                    SubCtgywiseTotalHashmap.put(subCtgyKey, SubCtgywisetotalString);
                                }
                            }
                            else {
                                SubCtgywiseTotalHashmap.put(subCtgyKey, String.valueOf(tmcprice));
                                if(vendorKey.equals("vendor_5")){


                                    SubCtgywiseWeightString ="0";
                                    //SubCtgywiseWeightString = SubCtgywiseWeightHashmap.get(subCtgyKey);
                                    double SubCtgywiseWeightDouble = Double.parseDouble(SubCtgywiseWeightString);
                                    grossweightingramsDouble = grossweightingramsDouble * QuantityInt;

                                    SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + grossweightingramsDouble;
                                    SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                    SubCtgywiseQuantityString ="0";
                                    //SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(subCtgyKey);
                                    int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                    SubCtgywiseQuantityint = SubCtgywiseQuantityint + QuantityInt;
                                    SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);

                                    if (SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseQuantityHashmap.replace(subCtgyKey , SubCtgywiseQuantityString);
                                        SubCtgywiseWeightHashmap.replace(subCtgyKey , SubCtgywiseWeightString);

                                    } else {
                                        SubCtgywiseQuantityHashmap.remove(subCtgyKey);
                                        SubCtgywiseWeightHashmap.remove(subCtgyKey);

                                        SubCtgywiseWeightHashmap.put(subCtgyKey , SubCtgywiseWeightString);
                                        SubCtgywiseQuantityHashmap.put(subCtgyKey , SubCtgywiseQuantityString);

                                    }

                                }
                                if(vendorKey.equals("vendor_5")) {


                                    SubCtgywiseWeightHashmap.put(subCtgyKey , SubCtgywiseWeightString);
                                    SubCtgywiseQuantityHashmap.put(subCtgyKey , SubCtgywiseQuantityString);
                                }

                            }
                        } 
                        else {
                            SubCtgywiseTotalArray.add(subCtgyKey);
                            boolean isAlreadyAvailabe = false;


                            String SubCtgywiseWeightString ="" , SubCtgywiseQuantityString ="";
                            double grossweightingramsDouble = 0 ;
                            int QuantityInt = 0;
                            String QuantityString = "0" ;
                            String grossweightingramsString  ="0";





                            if(vendorKey.equals("vendor_5")) {
                                if (modal_orderDetails_ItemDesp.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {

                                    grossweightingramsDouble = 0 ;
                                    QuantityInt = 0;
                                    QuantityString = "0" ;
                                    grossweightingramsString ="0";
                                    if (json.has("grossweightingrams")) {
                                        try {
                                            grossweightingramsDouble = Double.parseDouble(String.valueOf(json.get("grossweightingrams")));

                                        } catch (Exception e) {
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        grossweightingramsDouble = 0;
                                    }

                                    if(grossweightingramsDouble==0){


                                        if (json.has("grossweight")) {
                                            try {
                                                grossweightingramsString = String.valueOf(json.get("grossweight"));

                                            } catch (Exception e) {
                                                grossweightingramsString = "0";
                                                e.printStackTrace();
                                            }

                                            grossweightingramsString = grossweightingramsString.replaceAll("[^\\d.]", "");
                                            try{
                                                if(grossweightingramsString.equals("")){
                                                    grossweightingramsDouble = 0;

                                                }
                                                else {
                                                    grossweightingramsDouble = Double.parseDouble(grossweightingramsString);
                                                }
                                            }
                                            catch (Exception e){
                                                grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }


                                            if (grossweightingramsDouble == 0) {
                                                if (json.has("netweight")) {
                                                    try {
                                                        grossweightingramsString = String.valueOf(json.get("netweight"));

                                                    } catch (Exception e) {
                                                        grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else {
                                                    grossweightingramsString = "0";
                                                }
                                            }



                                        }
                                        else {
                                            grossweightingramsString = "0";

                                            if (json.has("netweight")) {
                                                try {
                                                    grossweightingramsString = String.valueOf(json.get("netweight"));

                                                } catch (Exception e) {
                                                    grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                grossweightingramsString = "0";
                                            }


                                        }

                                        try {
                                            if (grossweightingramsString.contains("t")) {
                                                grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('t'));

                                            } else if (grossweightingramsString.contains("-")) {
                                                grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('-'));

                                            }

                                            grossweightingramsString  = grossweightingramsString.replaceAll("[^\\d.]", "");
                                            try {
                                                grossweightingramsDouble = Double.parseDouble(grossweightingramsString);

                                            } catch (Exception e) {
                                                grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }

                                        }
                                        catch (Exception e){
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }

                                    }


                                    try{
                                        QuantityString = String.valueOf(json.get("quantity"));
                                        QuantityString  = QuantityString.replaceAll("[^\\d.]", "");
                                        QuantityInt = Integer.parseInt(QuantityString);


                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }




                                }
                                else{

                                    grossweightingramsDouble = 0 ;
                                    QuantityInt = 0;
                                    QuantityString = "0" ;
                                    grossweightingramsString ="0";
                                    if (json.has("grossweightingrams")) {
                                        try {
                                            grossweightingramsDouble = Double.parseDouble(String.valueOf(json.get("grossweightingrams")));

                                        } catch (Exception e) {
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }
                                    }
                                    else {
                                        grossweightingramsDouble = 0;
                                    }

                                    if(grossweightingramsDouble==0){


                                        if (json.has("grossweight")) {
                                            try {
                                                grossweightingramsString = String.valueOf(json.get("grossweight"));

                                            } catch (Exception e) {
                                                grossweightingramsString = "0";
                                                e.printStackTrace();
                                            }

                                            grossweightingramsString = grossweightingramsString.replaceAll("[^\\d.]", "");
                                            try{
                                                if(grossweightingramsString.equals("")){
                                                    grossweightingramsDouble = 0;

                                                }
                                                else {
                                                    grossweightingramsDouble = Double.parseDouble(grossweightingramsString);
                                                }
                                            }
                                            catch (Exception e){
                                                grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }


                                            if (grossweightingramsDouble == 0) {
                                                if (json.has("netweight")) {
                                                    try {
                                                        grossweightingramsString = String.valueOf(json.get("netweight"));

                                                    } catch (Exception e) {
                                                        grossweightingramsString = "0";
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else {
                                                    grossweightingramsString = "0";
                                                }
                                            }


                                        }
                                        else {
                                            grossweightingramsString = "0";

                                            if (json.has("netweight")) {
                                                try {
                                                    grossweightingramsString = String.valueOf(json.get("netweight"));

                                                } catch (Exception e) {
                                                    grossweightingramsString = "0";
                                                    e.printStackTrace();
                                                }
                                            }
                                            else {
                                                grossweightingramsString = "0";
                                            }



                                        }



                                        try {
                                            if (grossweightingramsString.contains("t")) {
                                                grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('t'));

                                            } else if (grossweightingramsString.contains("-")) {
                                                grossweightingramsString = grossweightingramsString.substring(grossweightingramsString.indexOf('-'));

                                            }

                                            grossweightingramsString  = grossweightingramsString.replaceAll("[^\\d.]", "");
                                            try {
                                                grossweightingramsDouble = Double.parseDouble(grossweightingramsString);

                                            } catch (Exception e) {
                                                grossweightingramsDouble = 0;
                                                e.printStackTrace();
                                            }

                                        }
                                        catch (Exception e){
                                            grossweightingramsDouble = 0;
                                            e.printStackTrace();
                                        }
                                    }





                                    if(subCtgyKey.equals("tmcsubctgy_4")) {
                                        grossweightingramsDouble = 0;
                                    }

                                    QuantityInt = 0;
                                    QuantityString = "0" ;
                                    try{
                                        QuantityString = String.valueOf(json.get("quantity"));
                                        QuantityString  = QuantityString.replaceAll("[^\\d.]", "");
                                        QuantityInt = Integer.parseInt(QuantityString);


                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }



                                }
                            }







                            try {
                                isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(subCtgyKey);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                String SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(subCtgyKey);
                                double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                SubCtgywisetotalDouble = SubCtgywisetotalDouble + tmcprice;
                                SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);





                                if(vendorKey.equals("vendor_5")){


                                    SubCtgywiseWeightString ="";
                                    SubCtgywiseWeightString = SubCtgywiseWeightHashmap.get(subCtgyKey);
                                    double SubCtgywiseWeightDouble = Double.parseDouble(SubCtgywiseWeightString);
                                    grossweightingramsDouble = grossweightingramsDouble * QuantityInt;

                                    SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + grossweightingramsDouble;
                                    SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                    SubCtgywiseQuantityString ="";
                                    SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(subCtgyKey);
                                    int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                    SubCtgywiseQuantityint = SubCtgywiseQuantityint + QuantityInt;
                                    SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);

                                    if (SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseQuantityHashmap.replace(subCtgyKey , SubCtgywiseQuantityString);
                                        SubCtgywiseWeightHashmap.replace(subCtgyKey , SubCtgywiseWeightString);

                                    } else {
                                        SubCtgywiseQuantityHashmap.remove(subCtgyKey);
                                        SubCtgywiseWeightHashmap.remove(subCtgyKey);

                                        SubCtgywiseWeightHashmap.put(subCtgyKey , SubCtgywiseWeightString);
                                        SubCtgywiseQuantityHashmap.put(subCtgyKey , SubCtgywiseQuantityString);

                                    }

                                }




                                if (SDK_INT >= Build.VERSION_CODES.N) {
                                    SubCtgywiseTotalHashmap.replace(subCtgyKey, SubCtgywisetotalString);
                                } else {
                                    SubCtgywiseTotalHashmap.remove(subCtgyKey);
                                    SubCtgywiseTotalHashmap.put(subCtgyKey, SubCtgywisetotalString);
                                }
                            }
                            else {
                                SubCtgywiseTotalHashmap.put(subCtgyKey, String.valueOf(tmcprice));
                                if(vendorKey.equals("vendor_5")){


                                    SubCtgywiseWeightString ="";
                                    double SubCtgywiseWeightDouble = Double.parseDouble("0");
                                    grossweightingramsDouble = grossweightingramsDouble * QuantityInt;
                                    SubCtgywiseWeightDouble = SubCtgywiseWeightDouble + grossweightingramsDouble;
                                    SubCtgywiseWeightString = String.valueOf(SubCtgywiseWeightDouble);


                                    SubCtgywiseQuantityString ="0";
                                   // SubCtgywiseQuantityString = SubCtgywiseQuantityHashmap.get(subCtgyKey);
                                    int SubCtgywiseQuantityint = Integer.parseInt(SubCtgywiseQuantityString);
                                    SubCtgywiseQuantityint = SubCtgywiseQuantityint + QuantityInt;
                                    SubCtgywiseQuantityString = String.valueOf(SubCtgywiseQuantityint);



                                }



                                if(vendorKey.equals("vendor_5")) {


                                    SubCtgywiseWeightHashmap.put(subCtgyKey , SubCtgywiseWeightString);
                                    SubCtgywiseQuantityHashmap.put(subCtgyKey , SubCtgywiseQuantityString);
                                }
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    if (ordertypeArray.contains(ordertype)) {
                        boolean isAlreadyAvailabe = false;

                        try {
                            isAlreadyAvailabe = checkIfPaymentdetailisAlreadyAvailableInArray(ordertype);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        if (isAlreadyAvailabe)  {
                            Modal_OrderDetails modal_orderDetails = ordertypeHashmap.get(ordertype);

                            if (ordertype.equals(Constants.CREDIT) || ordertype.equals("credit")) {
                                double credit_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCreditSales());

                                double credit_amount = tmcprice + credit_amount_fromhashmap;

                                double newTotalcreditAmount = credit_amount;
                                modal_orderDetails.setCreditSales(String.valueOf((newTotalcreditAmount)));


                            }



                           else if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {
                                double pos_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getPosSales());

                                double pos_amount = tmcprice + pos_amount_fromhashmap;

                                double newTotalPosAmount = pos_amount;
                                modal_orderDetails.setPosSales(String.valueOf((newTotalPosAmount)));


                            }
                           else if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {
                                double appsales_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getAppSales());
                                double Appsales_amount = tmcprice + appsales_amount_fromhashmap;
                                double newTotalAppSalesAmount = Appsales_amount;
                                modal_orderDetails.setAppSales(String.valueOf((newTotalAppSalesAmount)));


                            }


                           else if (ordertype.equals(Constants.PhoneOrder) || ordertype.equals("Phone Order")) {
                                double phoneOrder_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getPhoneOrderSales());
                                double phoneOrder_amount = tmcprice + phoneOrder_amount_fromhashmap;
                                double newTotalphoneOrderAmount = phoneOrder_amount;
                                modal_orderDetails.setPhoneOrderSales(String.valueOf((newTotalphoneOrderAmount)));


                            }
                           else if (ordertype.equals(Constants.WholeSaleOrder) || ordertype.equals("WholeSale Order")) {
                                double wholeSaleOrder_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getWholeSaleOrderSales());
                                double wholeSaleOrder_amount = tmcprice + wholeSaleOrder_amount_fromhashmap;
                                double newTotalwholeSaleOrderAmount = wholeSaleOrder_amount;
                                modal_orderDetails.setWholeSaleOrderSales(String.valueOf((newTotalwholeSaleOrderAmount)));


                            }

                          else  if (ordertype.equals(Constants.SwiggyOrder) || ordertype.equals("Swiggy Order")) {
                                double swiggyOrder_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getSwiggySales());
                                double swiggyOrder_amount = tmcprice + swiggyOrder_amount_fromhashmap;
                                double newTotalswiggyOrderAmount = swiggyOrder_amount;
                                modal_orderDetails.setSwiggySales(String.valueOf((newTotalswiggyOrderAmount)));


                            }


                           else if (ordertype.equals(Constants.DunzoOrder) || ordertype.equals("Dunzo Order")) {
                                double dunzoOrder_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getDunzoSales());
                                double dunzoOrder_amount = tmcprice + dunzoOrder_amount_fromhashmap;
                                double newTotaldunzoOrderAmount = dunzoOrder_amount;
                                modal_orderDetails.setDunzoSales(String.valueOf((newTotaldunzoOrderAmount)));


                            }
                           else if (ordertype.equals(Constants.BigBasket) || ordertype.equals("BigBasket Order")) {
                                double bigBasketOrder_amount_fromhashmap = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales());

                                double bigBasketOrder_amount = tmcprice + bigBasketOrder_amount_fromhashmap;

                                double newTotalbigBasketOrderAmount = bigBasketOrder_amount;
                                modal_orderDetails.setBigBasketSales(String.valueOf(newTotalbigBasketOrderAmount));


                            }

                        }
                           /* else{
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                if(ordertype.equals(Constants.POSORDER)||ordertype.equals("posorder")){

                                    modal_orderDetails.setPosSales(String.valueOf(decimalFormat.format(tmcprice)));


                                }
                                if(ordertype.equals(Constants.APPORDER)||ordertype.equals("apporder")){

                                    modal_orderDetails.setAppSales(String.valueOf(decimalFormat.format(tmcprice)));


                                }

                                ordertypeHashmap.put(ordertype,modal_orderDetails);
                            }

                            */
                    }
                    else {
                        ordertypeArray.add(ordertype);
                        Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();

                        if (ordertype.equals(Constants.CREDIT) || ordertype.equals("credit")) {

                            modal_orderDetails.setCreditSales(String.valueOf((tmcprice)));


                        }

                       else if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {

                            modal_orderDetails.setPosSales(String.valueOf((tmcprice)));


                        }
                       else if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {

                            modal_orderDetails.setAppSales(String.valueOf((tmcprice)));


                        }
                        else if (ordertype.equals(Constants.WholeSaleOrder) || ordertype.equals("WholSale Order")) {

                            modal_orderDetails.setWholeSaleOrderSales(String.valueOf((tmcprice)));


                        }
                        else if (ordertype.equals(Constants.SwiggyOrder) || ordertype.equals("Swiggy Order")) {

                            modal_orderDetails.setSwiggySales(String.valueOf((tmcprice)));


                        }

                       else if (ordertype.equals(Constants.PhoneOrder) || ordertype.equals("Phone Order")) {

                            modal_orderDetails.setPhoneOrderSales(String.valueOf((tmcprice)));


                        }


                       else if (ordertype.equals(Constants.SwiggyOrder) || ordertype.equals("Swiggy Order")) {

                            modal_orderDetails.setSwiggySales(String.valueOf((tmcprice)));


                        }
                       else if (ordertype.equals(Constants.DunzoOrder) || ordertype.equals("Dunzo Order")) {

                            modal_orderDetails.setDunzoSales(String.valueOf((tmcprice)));


                        }

                       else if (ordertype.equals(Constants.BigBasket) || ordertype.equals("BigBasket Order")) {

                            modal_orderDetails.setBigBasketSales(String.valueOf((tmcprice)));


                        }
                        ordertypeHashmap.put(ordertype, modal_orderDetails);
                    }


                    String menuitemid = String.valueOf(menuitemidd);
                    try {
                        if (Order_Item_List.contains(menuitemid)) {
                            boolean isItemAlreadyOrdered = checkIfMenuItemisAlreadyAvailableInArray(menuitemid);
                            if (isItemAlreadyOrdered) {
                                Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(menuitemid);
                                double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getTmcprice());
                                int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                                double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());
                         /*   String oldOrder_WeightInGrams = modal_orderDetails_itemDespfrom_hashMap.getWeightingrams();
                            double doubleoldOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                            int intOldOrder_WeightInGrams = (int) Math.ceil(doubleoldOrder_WeightInGrams);

                            int intNewOrder_WeightInGrams = (int) Math.ceil(newweight);

                            intOldOrder_WeightInGrams = intOldOrder_WeightInGrams +intNewOrder_WeightInGrams;
                          */
                                tmcprice = tmcprice + tmcprice_from_HashMap;
                                quantity = quantity + quantity_from_HashMap;
                                gstAmount = gstAmount + gstAmount_from_HashMap;

                                //  modal_orderDetails_itemDespfrom_hashMap.setWeightingrams(String.valueOf((intOldOrder_WeightInGrams)));
                                modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                                modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf((tmcprice)));
                                modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount)));


                            } /*else {

                                 OrderItem_hashmap.put(menuitemid, modal_orderDetails_ItemDesp);

                             }
                             */
                        } else {
                            if (!tmcSubCtgykey.contains(subCtgyKey)) {
                                tmcSubCtgykey.add(subCtgyKey);
                            }
                            Order_Item_List.add(menuitemid);
                            OrderItem_hashmap.put(menuitemid, modal_orderDetails_ItemDesp);

                           /*  boolean isItemAlreadyOrdered = checkIfMenuItemisAlreadyAvailableInArray(menuitemid);
                             if (isItemAlreadyOrdered) {

                                 Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(menuitemid);
                                 double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getTmcprice());
                                 int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                                 double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());

                                 tmcprice = tmcprice + tmcprice_from_HashMap;
                                 quantity = quantity + quantity_from_HashMap;
                                 gstAmount = gstAmount + gstAmount_from_HashMap;
                                 String oldOrder_WeightInGrams = modal_orderDetails_itemDespfrom_hashMap.getWeightingrams();
                                 double doubleoldOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                         */
                         /*   int intOldOrder_WeightInGrams = (int) Math.ceil(doubleoldOrder_WeightInGrams);


                            double doubleNewOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                            int intNewOrder_WeightInGrams = (int) Math.ceil(newweight);

                            intOldOrder_WeightInGrams = intOldOrder_WeightInGrams +intNewOrder_WeightInGrams;
                            //Log.d(Constants.TAG, "this json pre 3 " +String.valueOf(oldOrder_WeightInGrams));


                            modal_orderDetails_itemDespfrom_hashMap.setWeightingrams(String.valueOf((intOldOrder_WeightInGrams)));

                                 modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                                 modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf(decimalFormat.format(tmcprice)));
                                 modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf(decimalFormat.format(gstAmount)));
                        }


                             } else {
                                                              OrderItem_hashmap.put(menuitemid, modal_orderDetails_ItemDesp);

                             }
                                    */

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        if (OrderItem_hashmap.size() > 1) {
                            try {
                                OrderItem_hashmap = sortByComparator(OrderItem_hashmap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    //Log.d(Constants.TAG, "this order have no menuitemId " + String.valueOf(json.get("itemname")));
                    Adjusting_Widgets_Visibility(false);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public static HashMap<String, Modal_OrderDetails> sortByComparator(
            HashMap<String, Modal_OrderDetails> unsortMap) {

        List<Map.Entry<String, Modal_OrderDetails>> list = new LinkedList<Map.Entry<String, Modal_OrderDetails>>(
                unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Modal_OrderDetails>>() {
            public int compare(Map.Entry<String, Modal_OrderDetails> o1, Map.Entry<String, Modal_OrderDetails> o2) {
                return o1.getValue().getItemname().compareTo(o2.getValue().getItemname());
            }
        });

        HashMap<String, Modal_OrderDetails> sortedMap = new LinkedHashMap<String, Modal_OrderDetails>();
        for (Map.Entry<String, Modal_OrderDetails> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


    private void addOrderedItemAmountDetails(List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {
        FinalBill_hashmap.clear();
        finalBillDetails.clear();

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double deliveryChargee = 0, deliveryCharge_phoneOrder = 0,deliveryCharge_creditOrder =0;
        double totalAmountWithhGst = 0;
        double discountAmount = 0;
        double pos_discountAmount = 0;
        double swiggyDiscount_amount = 0;
        double dunzoDiscount_amount = 0;
        double bigBasketDiscount_amount = 0;
        double creditDiscount_amount =0;
        double wholeSaleDiscount_amount = 0;
        double phoneOrders_discountAmount = 0;

        double GST = 0;
        double totalAmount = 0;
        double posorder_Amount = 0;
        double apporder_Amount = 0;
        double phoneorder_Amount = 0;
        double swiggyorder_Amount = 0;
        double dunzoorder_Amount = 0;
        double creditorder_Amount = 0;
        double bigBasketorder_Amount = 0;
        double wholeSaleorder_Amount = 0;
        double totalRefundAmount = 0;
        double totalReplacementAmount = 0;

        int no_ofOrdersWithdeliveryCharge = 0, no_ofPhoneOrdersWithDeliveryCharge = 0 ,no_ofCreditOrdersWithDeliveryCharge=0;

        try {
            for (String transactionType : replacementTransactiontypeArray) {

                List<Modal_ReplacementTransactionDetails> replacementTransactionDetailsArray = replacementTransactiontypeHashmap.get(transactionType);

                for (int i = 0; i < replacementTransactionDetailsArray.size(); i++) {
                    double refundAmount = 0;
                    double replacementAmount = 0;

                    Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = replacementTransactionDetailsArray.get(i);

                    if (modal_replacementTransactionDetails.getTransactiontype().toUpperCase().equals("REFUND")) {
                        String refundAmountString = "0";
                        try {
                            refundAmountString = modal_replacementTransactionDetails.getRefundamount().toString();

                        } catch (Exception e) {
                            refundAmountString = "0";
                            e.printStackTrace();
                        }
                        try {
                            refundAmount = Double.parseDouble(refundAmountString);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {

                            totalRefundAmount = refundAmount + totalRefundAmount;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                    if (modal_replacementTransactionDetails.getTransactiontype().toUpperCase().equals("REPLACEMENT")) {
                        String replacementAmountString = "0";
                        try {
                            replacementAmountString = modal_replacementTransactionDetails.getReplacementorderamount().toString();

                        } catch (Exception e) {
                            replacementAmountString = "0";
                            e.printStackTrace();
                        }
                        try {
                            replacementAmount = Double.parseDouble(replacementAmountString);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {

                            totalReplacementAmount = replacementAmount + totalReplacementAmount;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (String orderid : couponDiscountOrderidArray) {
                String appDiscount_amount = couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(appDiscount_amount);
                discountAmount = discountAmount + CouponDiscount_double;

            }



            for (String orderid : Pos_couponDiscountOrderidArray) {
                String posDiscount_amount = Pos_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(posDiscount_amount);
                pos_discountAmount = pos_discountAmount + CouponDiscount_double;

            }


            for (String orderid : phoneOrders_couponDiscountOrderidArray) {
                String phoneOrdersDiscount_Amount = phoneOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(phoneOrdersDiscount_Amount);
                phoneOrders_discountAmount = phoneOrders_discountAmount + CouponDiscount_double;

            }


            for (String orderid : wholesaleOrders_couponDiscountOrderidArray) {
                String wholeSaleOrdersDiscount_Amount = wholesaleOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(wholeSaleOrdersDiscount_Amount);
                wholeSaleDiscount_amount = wholeSaleDiscount_amount + CouponDiscount_double;

            }


            for (String orderid : swiggyOrders_couponDiscountOrderidArray) {
                String swiggyOrdersDiscount_Amount = swiggyOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(swiggyOrdersDiscount_Amount);
                swiggyDiscount_amount = swiggyDiscount_amount + CouponDiscount_double;

            }


            for (String orderid : dunzoOrders_couponDiscountOrderidArray) {
                String dunzoOrdersDiscount_Amount = dunzoOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(dunzoOrdersDiscount_Amount);
                dunzoDiscount_amount = dunzoDiscount_amount + CouponDiscount_double;

            }

            for (String orderid : bigBasketOrders_couponDiscountOrderidArray) {
                String bigBasketOrdersDiscount_Amount = bigBasketOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(bigBasketOrdersDiscount_Amount);
                bigBasketDiscount_amount = bigBasketDiscount_amount + CouponDiscount_double;

            }



            for (String orderid : creditOrders_couponDiscountOrderidArray) {
                String creditOrdersDiscount_Amount = creditOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(creditOrdersDiscount_Amount);
                creditDiscount_amount = creditDiscount_amount + CouponDiscount_double;

            }


            for (String orderid : deliveryChargeOrderidArray) {
                String DeliveryChargeString = deliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryChargee = deliveryChargee + deliveryCharge_double;

            }


            for (String orderid : phoneorderdeliveryChargeOrderidArray) {
                String DeliveryChargeString = phoneorderdeliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryCharge_phoneOrder = deliveryCharge_phoneOrder  + deliveryCharge_double;

            }


            for (String orderid : creditOrderdeliveryChargeOrderidArray) {
                String DeliveryChargeString = creditOrderdeliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryCharge_creditOrder = deliveryCharge_creditOrder  + deliveryCharge_double;

            }


            try{
                no_ofOrdersWithdeliveryCharge = deliveryChargeOrderidArray.size();
            }
            catch (Exception e){
                no_ofOrdersWithdeliveryCharge = 0;
                e.printStackTrace();
            }

            try{
                no_ofPhoneOrdersWithDeliveryCharge = phoneorderdeliveryChargeOrderidArray.size();
            }
            catch (Exception e){
                no_ofPhoneOrdersWithDeliveryCharge = 0;
                e.printStackTrace();
            }

            try{
                no_ofCreditOrdersWithDeliveryCharge = creditOrderdeliveryChargeOrderidArray.size();
            }
            catch (Exception e){
                no_ofCreditOrdersWithDeliveryCharge = 0;
                e.printStackTrace();
            }


            for (int i = 0; i < order_item_list.size(); i++) {
                String menuItemId = order_item_list.get(i);
                Modal_OrderDetails modal_orderDetails_amountDetails = orderItem_hashmap.get(menuItemId);
                double totalAmountWithhGst_from_array = Double.parseDouble(modal_orderDetails_amountDetails.getTmcprice());
                totalAmountWithhGst = totalAmountWithhGst + totalAmountWithhGst_from_array;
                double Gst_from_array = Double.parseDouble(modal_orderDetails_amountDetails.getGstamount());
                GST = GST + Gst_from_array;
                String ordertype = (modal_orderDetails_amountDetails.getOrdertype());


            }
            for (String ordertype : ordertypeArray) {
                Modal_OrderDetails modal_orderDetails = ordertypeHashmap.get(ordertype);
                try {

                    if ((ordertype.toUpperCase().equals(Constants.CREDIT)) || (ordertype.equals("credit"))) {
                        creditorder_Amount = Double.parseDouble(((Objects.requireNonNull(modal_orderDetails).getCreditSales())));
                        ////Log.i(Constants.TAG,"Consolidated Report  new posorder_Amount   " +posorder_Amount);
                        creditorder_Amount = creditorder_Amount - creditDiscount_amount;
                    }

                   else if ((ordertype.toUpperCase().equals(Constants.POSORDER)) || (ordertype.equals("posorder"))) {
                        posorder_Amount = Double.parseDouble(((Objects.requireNonNull(modal_orderDetails).getPosSales())));
                        ////Log.i(Constants.TAG,"Consolidated Report  new posorder_Amount   " +posorder_Amount);
                        posorder_Amount = posorder_Amount - pos_discountAmount;
                    }

                   else if ((ordertype.toUpperCase().equals(Constants.APPORDER)) || (ordertype.equals("apporder"))) {
                        apporder_Amount = Double.parseDouble((Objects.requireNonNull(modal_orderDetails).getAppSales()));
                        apporder_Amount = apporder_Amount - discountAmount;
                        apporder_Amount = apporder_Amount + deliveryChargee;
                    }

                   else if ((ordertype.toUpperCase().equals(Constants.PhoneOrder)) || (ordertype.equals("Phone Order"))) {
                        phoneorder_Amount = Double.parseDouble((Objects.requireNonNull(modal_orderDetails).getPhoneOrderSales()));
                        phoneorder_Amount = phoneorder_Amount - phoneOrders_discountAmount;
                        phoneorder_Amount = phoneorder_Amount+deliveryCharge_phoneOrder;
                    }

                   else if ((ordertype.toUpperCase().equals(Constants.WholeSaleOrder)) || (ordertype.equals("WholeSale Order"))) {
                        wholeSaleorder_Amount = Double.parseDouble((Objects.requireNonNull(modal_orderDetails).getWholeSaleOrderSales()));
                        wholeSaleorder_Amount = wholeSaleorder_Amount - wholeSaleDiscount_amount;
                    }

                   else if ((ordertype.toUpperCase().equals(Constants.SwiggyOrder)) || (ordertype.equals("Swiggy Order"))) {
                        swiggyorder_Amount = Double.parseDouble((Objects.requireNonNull(modal_orderDetails).getSwiggySales()));
                        swiggyorder_Amount = swiggyorder_Amount - swiggyDiscount_amount;
                    }

                   else if ((ordertype.toUpperCase().equals(Constants.DunzoOrder)) || (ordertype.equals("Dunzo Order"))) {
                        dunzoorder_Amount = Double.parseDouble((Objects.requireNonNull(modal_orderDetails).getDunzoSales()));
                        dunzoorder_Amount = dunzoorder_Amount - dunzoDiscount_amount;
                    }


                   else if ((ordertype.toUpperCase().equals(Constants.BigBasket)) || (ordertype.equals("BigBasket Order"))) {
                        bigBasketorder_Amount = Double.parseDouble((Objects.requireNonNull(modal_orderDetails).getBigBasketSales()));
                        bigBasketorder_Amount = bigBasketorder_Amount - bigBasketDiscount_amount;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            try {
                posorder_Amount = posorder_Amount - (totalReplacementAmount + totalRefundAmount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            double totalAmountWithoutGst = totalAmountWithhGst - GST;
            discountAmount = discountAmount + pos_discountAmount + wholeSaleDiscount_amount + swiggyDiscount_amount + phoneOrders_discountAmount + dunzoDiscount_amount + bigBasketDiscount_amount + creditDiscount_amount;
            double totalAmt_with_CouponDiscount_double = totalAmountWithoutGst - discountAmount;
            double totalAmt_with_CouponDiscount__deliverycharge = totalAmt_with_CouponDiscount_double + deliveryChargee+deliveryCharge_phoneOrder + deliveryCharge_creditOrder;
            double totalAmt_with_CouponDiscount__deliverycharge_refund_replacement = totalAmt_with_CouponDiscount__deliverycharge - totalRefundAmount - totalReplacementAmount;
            double totalAmt_with_CouponDiscount__deliverycharge_GST_refund_replacement = totalAmt_with_CouponDiscount__deliverycharge_refund_replacement - GST;


            try{
                deliveryCharge_label.setText("(E) Delivery Charge  ( "+String.valueOf(no_ofOrdersWithdeliveryCharge+no_ofPhoneOrdersWithDeliveryCharge+no_ofCreditOrdersWithDeliveryCharge)+" ) ");
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                totalappMarkupPercentageValue  = Double.parseDouble(decimalFormat.format(totalappMarkupPercentageValue));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{

                totalAppSales.setText(String.valueOf(decimalFormat.format(apporder_Amount)));
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                apporder_Amount  = apporder_Amount - totalappMarkupPercentageValue ;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                totalApp_MarkupAmount.setText(String.valueOf(decimalFormat.format(totalappMarkupPercentageValue)));
              //  totalApp_MarkupAmount.setText(String.valueOf(Math.round(totalappMarkupPercentageValue)));
            }
            catch (Exception e){
                e.printStackTrace();
            }




            try{
                totalPhoneMarkupPercentageValue  = Double.parseDouble(decimalFormat.format(totalPhoneMarkupPercentageValue));
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                phoneOrderSales.setText(String.valueOf(decimalFormat.format(phoneorder_Amount)));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                phoneorder_Amount  = phoneorder_Amount - totalPhoneMarkupPercentageValue ;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                totalPhone_MarkupAmount.setText(String.valueOf(decimalFormat.format(Math.round(totalPhoneMarkupPercentageValue))));

                //  totalApp_MarkupAmount.setText(String.valueOf(totalappMarkupPercentageValue));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                phoneProductSales.setText(String.valueOf(decimalFormat.format(phoneorder_Amount)));
            }
            catch (Exception e){
                e.printStackTrace();
            }









            try{
                totalCreditMarkupPercentageValue  = Double.parseDouble(decimalFormat.format(totalCreditMarkupPercentageValue));
            }
            catch (Exception e){
                e.printStackTrace();
            }



            try{
                creditSales.setText(String.valueOf(decimalFormat.format(creditorder_Amount)));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                creditorder_Amount  = creditorder_Amount - totalCreditMarkupPercentageValue ;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                creditMarkupAmount.setText(String.valueOf(decimalFormat.format(Math.round(totalCreditMarkupPercentageValue))));

                //  totalApp_MarkupAmount.setText(String.valueOf(totalappMarkupPercentageValue));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                creditProductSales.setText(String.valueOf(decimalFormat.format(creditorder_Amount)));
            }
            catch (Exception e){
                e.printStackTrace();
            }








            totalAmt_without_GST.setText(String.valueOf(decimalFormat.format(totalAmountWithoutGst)));

            //Log.i(Constants.TAG, "Consolidated Report  new totalAmt_with_CouponDiscount__Gstdouble   " + String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__Gstdouble)));

            replacementAmount_textwidget.setText(String.valueOf(decimalFormat.format(totalReplacementAmount)));
            refundAmount_textwidget.setText(String.valueOf(decimalFormat.format(totalRefundAmount)));

            totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format(discountAmount)));
            totalAmt_with_CouponDiscount.setText(String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__deliverycharge_refund_replacement)));
            totalGST_Amt.setText(String.valueOf(decimalFormat.format(GST)));
            final_sales.setText(String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__deliverycharge_GST_refund_replacement)));
            appsales.setText(String.valueOf(decimalFormat.format(apporder_Amount)));
            possales.setText(String.valueOf(decimalFormat.format(posorder_Amount)));
          //  creditSales.setText(String.valueOf(decimalFormat.format(creditorder_Amount)));
            wholesalesOrderSales.setText(String.valueOf(decimalFormat.format(wholeSaleorder_Amount)));
            swiggySales.setText(String.valueOf(decimalFormat.format(swiggyorder_Amount)));
            dunzoSales.setText(String.valueOf(decimalFormat.format(dunzoorder_Amount)));
            bigbasketSales.setText(String.valueOf(decimalFormat.format(bigBasketorder_Amount)));
            deliveryChargeAmount_textwidget.setText(String.valueOf(decimalFormat.format(deliveryChargee+deliveryCharge_phoneOrder+deliveryCharge_creditOrder)));
         //   phoneOrderSales.setText(String.valueOf(decimalFormat.format(phoneorder_Amount)));
            totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__deliverycharge_GST_refund_replacement))+" .Rs");

            finalBillDetails.add("TOTAL : ");
            FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithoutGst)));
            finalBillDetails.add("DISCOUNT : ");
                FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(decimalFormat.format(discountAmount)));
            finalBillDetails.add("REFUND : ");
            FinalBill_hashmap.put("REFUND : ", String.valueOf(decimalFormat.format(totalRefundAmount)));
            finalBillDetails.add("REPLACEMENT : ");
            FinalBill_hashmap.put("REPLACEMENT : ", String.valueOf(decimalFormat.format(totalReplacementAmount)));
            finalBillDetails.add("DELIVERYCHARGES : ");
            FinalBill_hashmap.put("DELIVERYCHARGES : ", String.valueOf(String.valueOf(decimalFormat.format(deliveryChargee+deliveryCharge_phoneOrder+deliveryCharge_creditOrder))));
            finalBillDetails.add("SUBTOTAL : ");
            FinalBill_hashmap.put("SUBTOTAL : ", String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__deliverycharge_refund_replacement)));
            finalBillDetails.add("GST : ");
            FinalBill_hashmap.put("GST : ", String.valueOf(decimalFormat.format(GST)));
            finalBillDetails.add("FINAL SALES : ");
            FinalBill_hashmap.put("FINAL SALES : ", String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__deliverycharge_GST_refund_replacement)));
            //   sort_list_tmcSubCtgyWise(Order_Item_List, OrderItem_hashmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTmcSubCtgyList(String vendorKey) {



        String Api_to_Call = "";
        if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)) {
            Api_to_Call = Constants.api_getListofTMCTileForVendorkey+vendorKey;
        }
        else{
            Api_to_Call = Constants.api_getListofSubCtgy;

        }




        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_to_Call, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "Response: " + result);
                            int i1 = 0;
                            int arrayLength = result.length();
                            //Log.d("Constants.TAG", "Response: " + arrayLength);


                            for (; i1 <= (arrayLength - 1); i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    String ctgyname = "" , ctgykey ="",subCtgyKey ="",subCtgyName ="",displayNo="";

                                    if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)){
                                        ctgyname =  String.valueOf(json.get("tmcctgyname"));
                                        ctgykey = String.valueOf(json.get("tmcctgykey"));
                                        subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                                        subCtgyName = String.valueOf(json.get("tmcsubctgyname"));
                                        displayNo = String.valueOf(json.get("displayno"));
                                    }
                                    else{
                                        ctgyname =  String.valueOf(json.get("tmcctgyname"));
                                        ctgykey = String.valueOf(json.get("tmcctgykey"));
                                        subCtgyKey = String.valueOf(json.get("key"));
                                        subCtgyName = String.valueOf(json.get("subctgyname"));
                                        displayNo = String.valueOf(json.get("displayno"));
                                    }
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                    modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                    modal_orderDetails.tmcsubctgyname = subCtgyName;
                                    //  tmcSubCtgykey.add(subCtgyKey);
                                    SubCtgyKey_hashmap.put(subCtgyKey, modal_orderDetails);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                String subCtgyKey = String.valueOf("Miscellaneous");
                                //Log.d(Constants.TAG, "subCtgyKey: " + subCtgyKey);
                                String subCtgyName = String.valueOf("Miscellaneous Item");

                                modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                modal_orderDetails.tmcsubctgyname = subCtgyName;
                                //  tmcSubCtgykey.add(subCtgyKey);
                                SubCtgyKey_hashmap.put(subCtgyKey, modal_orderDetails);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Store");
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);


    }




    private void prepareContent() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try {
            dataList.clear();
            try {
                Collections.sort(tmcSubCtgykey, new Comparator<String>() {
                    public int compare(final String object1, final String object2) {
                        return object1.compareTo(object2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            for (String SubCtgykey : tmcSubCtgykey) {
                int i_value = 0;
                String subCtgyTotal = "0" , subCtgyWeight = "0", subCtgyQty = "0";
                double subCtgyTotaldouble = 0 ,subCtgyWeightdouble = 0 , subCtgyQuntitydouble = 0;
                //    Log.d(Constants.TAG, "SubCtgykey " + SubCtgykey);

                String SubCtgyName = "", ItemName_Quantity_weight, Price, menuid;
                Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
                try {
                    subCtgyTotal = SubCtgywiseTotalHashmap.get(SubCtgykey);
                    subCtgyTotaldouble = Double.parseDouble(subCtgyTotal);
                    subCtgyTotal = decimalFormat.format(subCtgyTotaldouble);
                } catch (Exception e) {
                    subCtgyTotal = "0";
                    e.printStackTrace();
                }

                try {
                    subCtgyWeight = SubCtgywiseWeightHashmap.get(SubCtgykey);

                    subCtgyWeight = subCtgyWeight.replaceAll("[^\\d.]", "");


                    subCtgyWeightdouble = Double.parseDouble(subCtgyWeight);

                    subCtgyWeightdouble = subCtgyWeightdouble/1000;

                    subCtgyWeight = decimalFormat.format(subCtgyWeightdouble)+" Kg";
                } catch (Exception e) {
                    subCtgyWeight = "0";
                    e.printStackTrace();
                }

                try {
                    subCtgyQty = SubCtgywiseQuantityHashmap.get(SubCtgykey);
                    subCtgyQty = subCtgyQty.replaceAll("[^\\d.]", "");

                    subCtgyQuntitydouble = Double.parseDouble(subCtgyQty);
                    subCtgyQty = decimalFormat.format(subCtgyQuntitydouble);
                } catch (Exception e) {
                    subCtgyQty = "0";
                    e.printStackTrace();
                }

                try {
                    SubCtgyName = Objects.requireNonNull(subCtgyName_object).getTmcsubctgyname();
                } catch (Exception e) {
                    SubCtgyName = "";
                    // Log.d(Constants.TAG, "before for " + e.getMessage());

                }
                try {

                   // SubCtgyName = SubCtgyName + "--"+ subCtgyQty + "--"+ subCtgyWeight ;

                } catch (Exception e) {
                    e.printStackTrace();
                }



                try {
                    Order_Item_List = getSortedIdFromHashMap(Order_Item_List, OrderItem_hashmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                for (int j = 0; j < Order_Item_List.size(); j++) {
                    menuid = Order_Item_List.get(j);
                    //  Log.d(Constants.TAG, "SubCtgykey menuid " + menuid);
                    //  Log.d(Constants.TAG, "SubCtgykey w " + SubCtgykey);
                    Log.d(Constants.TAG, "menuitemid:hash4 :" + "#" + String.valueOf(menuid) + "#");

                    Modal_OrderDetails itemDetailsfromHashmap = OrderItem_hashmap.get(menuid);
                    // Log.d(Constants.TAG, "SubCtgykey itemDetailsfromHashmap " + itemDetailsfromHashmap.getItemname());
                    String subCtgyKey_fromHashmap = "";
                    try {
                        subCtgyKey_fromHashmap = itemDetailsfromHashmap.getTmcsubctgykey();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //        Log.d(Constants.TAG, "SubCtgykey subCtgyKey_fromHashmap " + subCtgyKey_fromHashmap );

                    if (i_value != 0) {
                        if (subCtgyKey_fromHashmap.equals(SubCtgykey)) {
                            double weightinGrams = 0;
                           /* try{
                               double itemAmountfromHashmap = Double.parseDouble(Objects.requireNonNull(itemDetailsfromHashmap).getTmcprice());
                            //    subCtgyTotal = subCtgyTotal+itemAmountfromHashmap;
                            }
                            catch (Exception e){
                              //  subCtgyTotal =subCtgyTotal+0;
                                e.printStackTrace();
                            }

                            */
                            try {
                                if ((!(itemDetailsfromHashmap.getWeightingrams().equals(""))) && ((itemDetailsfromHashmap.getWeightingrams() != (null)))) {
                                    weightinGrams = Double.parseDouble(Objects.requireNonNull(itemDetailsfromHashmap).getWeightingrams());

                                }

                            } catch (Exception e) {
                                weightinGrams = 0;
                            }
                            String itemname = String.valueOf(itemDetailsfromHashmap.getItemname());

                            if (itemname.equals("Fresh Goat Meat - Curry Cut")) {
                                Log.i("TAG", "Key : " + String.valueOf(itemDetailsfromHashmap.getMenuitemid()));
                            }
                            double kilogram = weightinGrams * 0.001;
                            String KilogramString = String.valueOf(decimalFormat.format(kilogram) + "Kg");

                            ListItem listItem = new ListItem();
                            if (KilogramString != null && (!KilogramString.equals("")) && (!(KilogramString.equals("0.00Kg")))) {
                                listItem.setMessage(itemDetailsfromHashmap.getItemname() + " - " + KilogramString);
                                listItem.setMessageLine2(String.valueOf(decimalFormat.format(Double.parseDouble(itemDetailsfromHashmap.getTmcprice()))));
                                dataList.add(listItem);
                            } else {
                                listItem.setMessage(itemDetailsfromHashmap.getItemname() + " ( " + itemDetailsfromHashmap.getQuantity() + " ) ");

                                listItem.setMessageLine2(String.valueOf(decimalFormat.format(Double.parseDouble(itemDetailsfromHashmap.getTmcprice()))));
                                dataList.add(listItem);
                            }
                        }
                    }
                    if (i_value == 0) {
                        i_value = 1;
                        j = j - 1;
                        ListSection listSection = new ListSection();
                        try {
                            if (!listSection.getTitle().equals(SubCtgyName)) {
                                listSection.setTitle(SubCtgyName);
                                listSection.setTotalAmount(String.valueOf(subCtgyTotal));
                                dataList.add(listSection);

                            }
                        } catch (Exception e) {
                            listSection.setTitle(SubCtgyName);
                            listSection.setTotalAmount(String.valueOf(subCtgyTotal));

                            dataList.add(listSection);

                        }
                    }


                }
            }
            noofOrders.setText(String.valueOf(no_of_orders));
            noofPacks.setText(String.valueOf(no_of_ItemCount));


/*
 for (int i = 0; i <tmcSubCtgykey.size() ; i++) {
            if (i % 7 == 0) {
                ListSection listSection = new ListSection();
                listSection.setTitle("Title: " + i);
                dataList.add(listSection);
            } else {
                ListItem listItem = new ListItem();
                listItem.setMessage(i + " % 7 == 1");
                listItem.setMessageLine2("" + (i % 7 == 1));
                dataList.add(listItem);
            }
        }

 */

            // setAdapter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> getSortedIdFromHashMap(List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {
        order_item_list.clear();
        order_item_list.addAll(orderItem_hashmap.keySet());
        return order_item_list;
    }

    private void setAdapter() {
        Adjusting_Widgets_Visibility(false);
        try {
            Adapter_Pos_Sales_Report adapter = new Adapter_Pos_Sales_Report(ConsolidatedReportSubCtgywise.this, dataList, false);
            consolidatedSalesReport_Listview.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);
            scrollView.fullScroll(View.FOCUS_UP);
        } catch (Exception e) {
            e.printStackTrace();
        }


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


    private boolean checkIfSubCtgywiseTotalisAlreadyAvailableInArray(String menuitemid) {
        return SubCtgywiseTotalHashmap.containsKey(menuitemid);
    }


    private boolean checkIfPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return ordertypeHashmap.containsKey(menuitemid);
    }

    private boolean checkIfMenuItemisAlreadyAvailableInArray(String menuitemid) {
        return OrderItem_hashmap.containsKey(menuitemid);
    }


    void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }

    }

    private String getMonthString(int value) {
        if (value == 0) {
            return "Jan";
        } else if (value == 1) {
            return "Feb";
        } else if (value == 2) {
            return "Mar";
        } else if (value == 3) {
            return "Apr";
        } else if (value == 4) {
            return "May";
        } else if (value == 5) {
            return "Jun";
        } else if (value == 6) {
            return "Jul";
        } else if (value == 7) {
            return "Aug";
        } else if (value == 8) {
            return "Sep";
        } else if (value == 9) {
            return "Oct";
        } else if (value == 10) {
            return "Nov";
        } else if (value == 11) {
            return "Dec";
        }
        return "";
    }


    private String getDayString(int value) {
        if (value == 1) {
            return "Sun";
        } else if (value == 2) {
            return "Mon";
        } else if (value == 3) {
            return "Tue";
        } else if (value == 4) {
            return "Wed";
        } else if (value == 5) {
            return "Thu";
        } else if (value == 6) {
            return "Fri";
        } else if (value == 7) {
            return "Sat";
        }
        return "";
    }




    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();
        if(orderdetailsnewschema) {


            calendar.add(Calendar.DATE, -1);


            Date c1 = calendar.getTime();
            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));





            String PreviousdayDate = day.format(c1);


            return PreviousdayDate;

        }
        else {


            calendar.add(Calendar.DATE, -1);


            Date c1 = calendar.getTime();

            SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
            previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String PreviousdayDay = previousday.format(c1);


            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            String PreviousdayDate = df1.format(c1);
            PreviousdayDate = PreviousdayDay + ", " + PreviousdayDate;
            //System.out.println("todays Date  " + CurrentDate);
            // System.out.println("PreviousdayDate Date  " + PreviousdayDate);


            return PreviousdayDate;
        }
    }


    private String getDatewithNameofthePreviousDayfromSelectedDay(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, -1);


        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String PreviousdayDay = previousday.format(c1);


        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay + ", " + PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }

    private String convertNormalDateintoReplacementTransactionDetailsDate(String sDate, String Time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);


        Date c1 = calendar.getTime();


        SimpleDateFormat df = new SimpleDateFormat();
        if (Time.equals("STARTTIME")) {
            df = new SimpleDateFormat("yyyy-MM-dd 00:00:00",Locale.ENGLISH);
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd 23:59:59",Locale.ENGLISH);


        }

        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String Date = df.format(c1);
        return Date;
    }


    public String getDate_and_time() {


            Date c = Calendar.getInstance().getTime();

            if(orderdetailsnewschema) {

                SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
                day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

                CurrentDate = day.format(c);

                return CurrentDate;

            }
            else{
                SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
                day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                String CurrentDay = day.format(c);



                SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
                df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                CurrentDate = df.format(c);

                CurrentDate = CurrentDay+", "+CurrentDate;

                //CurrentDate = CurrentDay+", "+CurrentDate;
                System.out.println("todays Date  " + CurrentDate);


                return CurrentDate;
            }






    }
    private String convertOldFormatDateintoNewFormat(String todaysdate) {

     /*   SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }

      */

        Date date = null;

        SimpleDateFormat formatGMT = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);

        formatGMT.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        try
        {
            date  = formatGMT.parse(todaysdate);
        }
        catch (ParseException e)
        {
            //log(Log.ERROR, "DB Insertion error", e.getMessage().toString());
            //logException(e);
            e.printStackTrace();
        }

        try{

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDate = day.format(date);


        }
        catch (Exception e){
            e.printStackTrace();
        }




        return CurrentDate;

    }

    public String getstartDate_and_time_TransactionTable() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => 2022-03-01T10:03:14+0530 " + c);


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String FormattedTime = dfTime.format(c);

        return FormattedTime;
    }

    public String getendDate_and_time_TransactionTable() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => 2022-03-01T10:03:14+0530 " + c);


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String FormattedTime = dfTime.format(c);

        return FormattedTime;
    }


    private void runthread() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isReplacementTransacDetailsResponseReceivedForSelectedDate && isgetReplacementOrderForSelectedDateCalled) {

                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            prepareContent();
                            setAdapter();
                        } else {
                            runthread();
                        }
                    }
                });
            }
        }, 15);
    }


    private String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE" ,Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        CurrentDate = df.format(c);

        CurrentDate = CurrentDay + ", " + CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }
    private void exportSubCtgyWiseReport() {
        if ((SubCtgywiseTotalArray == null) || (SubCtgywiseTotalArray.size() <= 0)) {
            Adjusting_Widgets_Visibility(false);
            return;
        }

        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state "+state+" extstoragedir "+extstoragedir);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        File folder = new File(path);
        //  File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "Consolidated SubCtgy Sales Report_" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                Document layoutDocument = new Document();
                PdfWriter.getInstance(layoutDocument, fOut);
                layoutDocument.open();
                try {
                    SharedPreferences sharedPreferences
                            = getSharedPreferences("VendorLoginData",
                            MODE_PRIVATE);

                    String Vendorname = sharedPreferences.getString("VendorName", "");

                    com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 22, Font.BOLDITALIC);
                    com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("CONSOLIDATED SUBCTGY SALES REPORT");
                    titlepara.setSpacingBefore(5);
                    titlepara.setFont(boldFont);
                    titlepara.setAlignment(Element.ALIGN_CENTER);
                    layoutDocument.add(titlepara);

                    String vendorname = "Vendor: " + Vendorname;
                    com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
                    vendorpara.setSpacingBefore(20);
                    vendorpara.setAlignment(Element.ALIGN_LEFT);
                    layoutDocument.add(vendorpara);

                    com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date: " + getDate());
                    datepara.setAlignment(Element.ALIGN_LEFT);
                    datepara.setSpacingBefore(5);
                    datepara.setSpacingAfter(20);
                    layoutDocument.add(datepara);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                addSubCtgyWiseItemRows(layoutDocument);

                layoutDocument.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // document = new PdfDocument(new PdfWriter("MyFirstInvoice.pdf"));


            Adjusting_Widgets_Visibility(false);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Adjusting_Widgets_Visibility(false);

                startActivityForResult(intent, OPENPDF_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
            // }
        } catch (IOException e) {
            Adjusting_Widgets_Visibility(false);

            Log.i("error", e.getLocalizedMessage());
        } catch (Exception ex) {
            Adjusting_Widgets_Visibility(false);

            ex.printStackTrace();
        }


    }

    private void addSubCtgyWiseItemRows(Document layoutDocument) {

        try {
            String rsunit = "Rs.", tmcprice;
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            PdfPCell itemcell = new PdfPCell(new Phrase("Item"));
            itemcell.setBorder(Rectangle.NO_BORDER);
            itemcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            itemcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            itemcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            itemcell.setPaddingLeft(10);
            itemcell.setFixedHeight(30);
            table.addCell(itemcell);

            PdfPCell qtycell = new PdfPCell(new Phrase("Weight / Pcs"));
            qtycell.setBorder(Rectangle.NO_BORDER);
            qtycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            qtycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qtycell.setFixedHeight(30);
            table.addCell(qtycell);

            PdfPCell pricecell = new PdfPCell(new Phrase("Price"));
            pricecell.setBorder(Rectangle.NO_BORDER);
            pricecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pricecell.setFixedHeight(30);
            pricecell.setPaddingRight(10);
            table.addCell(pricecell);

            PdfPCell itemnamecell = null;
            PdfPCell itemqtycell = null;
            PdfPCell itempricecell = null;
            for (String SubCtgykey : tmcSubCtgykey) {
                int i_value = 0;
                String subCtgyTotal = "0" , subCtgyWeight ="0" , subCtgyQty = "";
                double subCtgyTotaldouble = 0 ,subCtgyWeightdouble = 0 ;
                      int  subCtgyQuntityint = 0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                String SubCtgyName = "", menuid;
                Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
                try {
                    if (!subCtgyName_object.equals(null)) {
                        SubCtgyName = Objects.requireNonNull(subCtgyName_object).getTmcsubctgyname();
                    }
                } catch (Exception e) {
                    SubCtgyName = "";

                    e.printStackTrace();

                }
                /*
                for (int j = 0; j < Order_Item_List.size(); j++) {
                    menuid = Order_Item_List.get(j);
                    Modal_OrderDetails itemRow = OrderItem_hashmap.get(menuid);
                    String itemName = "";
                    try {
                        if (!itemRow.equals(null)) {
                            itemName = itemRow.getItemname();
                        }
                    } catch (Exception e) {
                        itemName = "";

                        e.printStackTrace();

                    }
                    String subCtgyKey_fromHashmap = "";
                    try {
                        if (!itemRow.equals(null)) {
                            subCtgyKey_fromHashmap = itemRow.getTmcsubctgykey();
                        }
                    } catch (Exception e) {
                        subCtgyKey_fromHashmap = "";

                        e.printStackTrace();

                    }


                 */
                    try {
                        subCtgyTotal = SubCtgywiseTotalHashmap.get(SubCtgykey);
                        subCtgyTotaldouble = Double.parseDouble(subCtgyTotal);
                        subCtgyTotal = decimalFormat.format(subCtgyTotaldouble) +" Rs";
                    } catch (Exception e) {
                        subCtgyTotal = "0";
                        e.printStackTrace();
                    }

                    try {
                        subCtgyWeight = SubCtgywiseWeightHashmap.get(SubCtgykey);
                        subCtgyWeight = subCtgyWeight.replaceAll("[^\\d.]", "");
                        subCtgyWeightdouble = Double.parseDouble(subCtgyWeight);

                        subCtgyWeightdouble = subCtgyWeightdouble/1000;

                        subCtgyWeight = decimalFormat.format(subCtgyWeightdouble) +" Kg";
                    } catch (Exception e) {
                        subCtgyWeight = "0";
                        e.printStackTrace();
                    }

                    try {
                        subCtgyQty = SubCtgywiseQuantityHashmap.get(SubCtgykey);
                        subCtgyQty = subCtgyQty.replaceAll("[^\\d.]", "");
                        subCtgyQuntityint = Integer.parseInt(subCtgyQty);
                        subCtgyQty = String.valueOf(subCtgyQuntityint) +" Pc";
                    } catch (Exception e) {
                        subCtgyQty = "0";
                        e.printStackTrace();
                    }


                    PdfPCell SubCtgycell = new PdfPCell(new Phrase(SubCtgyName));
                    SubCtgycell.setBorder(Rectangle.NO_BORDER);
                  //  SubCtgycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    SubCtgycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    SubCtgycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    SubCtgycell.setPaddingLeft(10);
                    SubCtgycell.setFixedHeight(30);
                    table.addCell(SubCtgycell);
                    if(SubCtgykey.equals("Miscellaneous") || subCtgyWeightdouble <=0 ){
                        PdfPCell qtySubCtgycell = new PdfPCell(new Phrase(subCtgyQty));
                        qtySubCtgycell.setBorder(Rectangle.NO_BORDER);
                        //qtySubCtgycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        qtySubCtgycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        qtySubCtgycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        qtySubCtgycell.setFixedHeight(30);
                        table.addCell(qtySubCtgycell);
                    }
                    else {
                        PdfPCell qtySubCtgycell = new PdfPCell(new Phrase(subCtgyWeight));
                        qtySubCtgycell.setBorder(Rectangle.NO_BORDER);
                      //  qtySubCtgycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        qtySubCtgycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        qtySubCtgycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        qtySubCtgycell.setFixedHeight(30);
                        table.addCell(qtySubCtgycell);
                    }

                    PdfPCell priceSubCtgycell = new PdfPCell(new Phrase(subCtgyTotal));
                    priceSubCtgycell.setBorder(Rectangle.NO_BORDER);
                   // priceSubCtgycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    priceSubCtgycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    priceSubCtgycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    priceSubCtgycell.setFixedHeight(30);
                    priceSubCtgycell.setPaddingRight(10);
                    table.addCell(priceSubCtgycell);

              //  }
            }
            layoutDocument.add(table);


            PdfPTable tablePaymentMode = new PdfPTable(4);
            tablePaymentMode.setWidthPercentage(100);
            tablePaymentMode.setSpacingBefore(20);
            PdfPCell paymentModeemptycell;
            PdfPCell paymentModeemptycellone;
            PdfPCell paymentModeitemkeycell;
            PdfPCell paymentModeitemValueCell;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            double posorder_discountAmount = 0;
            double apporder_discountAmount = 0;
            double swiggyorder_discountAmount = 0;
            double dunzoorder_discountAmount = 0;
            double bigBasketorder_discountAmount = 0;
            double wholeSaleorder_discountAmount = 0;
            double phoneorder_discountAmount = 0;
            double creditorder_discountAmount = 0;


            double posorder_Amount = 0;
            double apporder_Amount = 0;
            double swiggyorder_Amount = 0;
            double dunzoorder_Amount = 0;
            double phoneorder_Amount = 0;
            double bigBasketorder_Amount = 0;
            double wholeSaleorder_Amount = 0;
            double creditorder_Amount = 0;

            String Payment_Amount = "0";
            double discountAmount = 0;
            double deliveryChargee=0,deliveryCharge_phoneOrder=0,deliveryCharge_creditOrder=0;


            for (String orderid : couponDiscountOrderidArray) {
                String Discount_amount = couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(Discount_amount);
                discountAmount = discountAmount + CouponDiscount_double;

            }


            for (String orderid : creditOrders_couponDiscountOrderidArray) {
                String Discount_amount = creditOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(Discount_amount);
                creditorder_discountAmount = creditorder_discountAmount + CouponDiscount_double;

            }



            for (String orderid : couponDiscountOrderidArray) {
                String appDiscount_amount = couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(appDiscount_amount);
                apporder_discountAmount = apporder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : Pos_couponDiscountOrderidArray) {
                String posDiscount_amount = Pos_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(posDiscount_amount);
                posorder_discountAmount = posorder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : phoneOrders_couponDiscountOrderidArray) {
                String phoneOrdersDiscount_Amount = phoneOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(phoneOrdersDiscount_Amount);
                phoneorder_discountAmount = phoneorder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : wholesaleOrders_couponDiscountOrderidArray) {
                String wholeSaleOrdersDiscount_Amount = wholesaleOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(wholeSaleOrdersDiscount_Amount);
                wholeSaleorder_discountAmount = wholeSaleorder_discountAmount + CouponDiscount_double;

            }
            for (String orderid : swiggyOrders_couponDiscountOrderidArray) {
                String swiggyOrdersDiscount_Amount = swiggyOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(swiggyOrdersDiscount_Amount);
                swiggyorder_discountAmount = swiggyorder_discountAmount + CouponDiscount_double;

            }
            for (String orderid : dunzoOrders_couponDiscountOrderidArray) {
                String dunzoOrdersDiscount_Amount = dunzoOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(dunzoOrdersDiscount_Amount);
                dunzoorder_discountAmount = dunzoorder_discountAmount + CouponDiscount_double;

            }

            for (String orderid : bigBasketOrders_couponDiscountOrderidArray) {
                String bigBasketOrdersDiscount_Amount = bigBasketOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(bigBasketOrdersDiscount_Amount);
                bigBasketorder_discountAmount = bigBasketorder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : deliveryChargeOrderidArray) {
                String DeliveryChargeString = deliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryChargee = deliveryChargee + deliveryCharge_double;

            }


            for (String orderid : creditOrderdeliveryChargeOrderidArray) {
                String DeliveryChargeString = creditOrderdeliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryCharge_creditOrder = deliveryCharge_creditOrder + deliveryCharge_double;

            }





            for (String orderid : phoneorderdeliveryChargeOrderidArray) {
                String DeliveryChargeString = phoneorderdeliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryCharge_phoneOrder = deliveryCharge_phoneOrder  + deliveryCharge_double;

            }



            for (String ordertype : ordertypeArray) {
                String replacmentFromTextview = "",refundFromTextview = "";
                double replacment_doubleFromTextview = 0,refund_doubleFromTextview = 0;

                Modal_OrderDetails modal_orderDetails = ordertypeHashmap.get(ordertype);
                try {
                    paymentModeemptycell = new PdfPCell(new Phrase(""));
                    paymentModeemptycell.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycell.setFixedHeight(25);
                    tablePaymentMode.addCell(paymentModeemptycell);

                    paymentModeemptycellone = new PdfPCell(new Phrase(""));
                    paymentModeemptycellone.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycellone.setFixedHeight(25);
                    tablePaymentMode.addCell(paymentModeemptycellone);

                    if ((ordertype.toUpperCase().equals(Constants.POSORDER)) || (ordertype.equals("posorder"))) {
                        posorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getPosSales())));
                        ////Log.i(Constants.TAG,"Consolidated Report  new posorder_Amount   " +posorder_Amount);
                        posorder_Amount = posorder_Amount - posorder_discountAmount;

                        try{
                            replacmentFromTextview = replacementAmount_textwidget.getText().toString();
                        }
                        catch (Exception e){
                            replacmentFromTextview="0";
                            e.printStackTrace();
                        }

                        try{
                            refundFromTextview = refundAmount_textwidget.getText().toString();

                        }
                        catch (Exception e){
                            refundFromTextview = "0";
                            e.printStackTrace();
                        }
                        try{
                            replacment_doubleFromTextview = Math.round(Double.parseDouble(replacmentFromTextview));
                        }
                        catch (Exception e){
                            replacment_doubleFromTextview =0;
                            e.printStackTrace();
                        }

                        try{
                            refund_doubleFromTextview = Math.round(Double.parseDouble(refundFromTextview));

                        }
                        catch (Exception e){
                            refund_doubleFromTextview = 0;
                            e.printStackTrace();
                        }



                        posorder_Amount = posorder_Amount - (posorder_discountAmount +refund_doubleFromTextview +replacment_doubleFromTextview);

                        paymentModeitemkeycell = new PdfPCell(new Phrase("POS Order  : "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + posorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.APPORDER)) || (ordertype.equals("apporder"))) {
                        apporder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getAppSales())));
                        apporder_Amount = apporder_Amount - apporder_discountAmount;
                        apporder_Amount = apporder_Amount + deliveryChargee;

                        paymentModeitemkeycell = new PdfPCell(new Phrase("APP Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + apporder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.PhoneOrder)) || (ordertype.equals("Phone Order"))) {
                        phoneorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getPhoneOrderSales())));
                        phoneorder_Amount = phoneorder_Amount - phoneorder_discountAmount;
                        phoneorder_Amount = phoneorder_Amount+deliveryCharge_phoneOrder;

                        paymentModeitemkeycell = new PdfPCell(new Phrase("PHONE Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + phoneorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.CREDIT)) || (ordertype.equals("credit"))) {
                        creditorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales())));
                        creditorder_Amount = creditorder_Amount -  creditorder_discountAmount;
                        creditorder_Amount = creditorder_Amount + deliveryCharge_creditOrder;

                        paymentModeitemkeycell = new PdfPCell(new Phrase("CREDIT Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + creditorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }



                    if ((ordertype.toUpperCase().equals(Constants.WholeSaleOrder)) || (ordertype.equals("WholeSale Order"))) {
                        wholeSaleorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getWholeSaleOrderSales())));
                        wholeSaleorder_Amount = wholeSaleorder_Amount - wholeSaleorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("WHOLESALE Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + wholeSaleorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }
                    if ((ordertype.toUpperCase().equals(Constants.SwiggyOrder)) || (ordertype.equals("Swiggy Order"))) {
                        swiggyorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getSwiggySales())));
                        swiggyorder_Amount = swiggyorder_Amount - swiggyorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("SWIGGY Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + swiggyorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.DunzoOrder)) || (ordertype.equals("Dunzo Order"))) {
                        dunzoorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getDunzoSales())));
                        dunzoorder_Amount = dunzoorder_Amount - dunzoorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("DUNZO Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + dunzoorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }


                    if ((ordertype.toUpperCase().equals(Constants.BigBasket)) || (ordertype.equals("BigBasket Order"))) {
                        bigBasketorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales())));
                        bigBasketorder_Amount = bigBasketorder_Amount - bigBasketorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("BIGBASKET Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + bigBasketorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            layoutDocument.add(tablePaymentMode);


            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(20);
            PdfPCell emptycell;
            PdfPCell emptycellone;
            PdfPCell emptycelltwo;
            for (int i = 0; i < finalBillDetails.size(); i++) {
                String key = finalBillDetails.get(i);
                String value = FinalBill_hashmap.get(key);
                //Log.d("ExportReportActivity", "itemTotalRowsList name "+key);

                //Log.d("ExportReportActivity", "itemTotalRowsList value "+value);
                emptycell = new PdfPCell(new Phrase(""));
                emptycell.setBorder(Rectangle.NO_BORDER);
                emptycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emptycell.setFixedHeight(25);
                table1.addCell(emptycell);

                emptycellone = new PdfPCell(new Phrase(""));
                emptycellone.setBorder(Rectangle.NO_BORDER);
                emptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emptycellone.setFixedHeight(25);
                table1.addCell(emptycellone);

                itemqtycell = new PdfPCell(new Phrase(key));
                itemqtycell.setBorderColor(BaseColor.LIGHT_GRAY);
                itemqtycell.setBorder(Rectangle.NO_BORDER);
                itemqtycell.setMinimumHeight(25);
                itemqtycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itemqtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table1.addCell(itemqtycell);


                itempricecell = new PdfPCell(new Phrase("Rs. " + value));
                itempricecell.setBorderColor(BaseColor.LIGHT_GRAY);
                itempricecell.setBorder(Rectangle.NO_BORDER);
                itempricecell.setMinimumHeight(25);
                itempricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itempricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itempricecell.setPaddingRight(10);
                table1.addCell(itempricecell);


            }
            layoutDocument.add(table1);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void exportReport() {
        if ((Order_Item_List == null) || (Order_Item_List.size() <= 0)) {
            Adjusting_Widgets_Visibility(false);
            return;
        }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state "+state+" extstoragedir "+extstoragedir);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        File folder = new File(path);
        //  File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "Consolidated Sales Report_" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                Document layoutDocument = new Document();
                PdfWriter.getInstance(layoutDocument, fOut);
                layoutDocument.open();
                addVendorDetails(layoutDocument);
                addItemRows(layoutDocument);

                layoutDocument.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // document = new PdfDocument(new PdfWriter("MyFirstInvoice.pdf"));


            Adjusting_Widgets_Visibility(false);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Adjusting_Widgets_Visibility(false);

                startActivityForResult(intent, OPENPDF_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
            // }
        } catch (IOException e) {
            Adjusting_Widgets_Visibility(false);

            Log.i("error", e.getLocalizedMessage());
        } catch (Exception ex) {
            Adjusting_Widgets_Visibility(false);

            ex.printStackTrace();
        }
    }

    private void addVendorDetails(Document layoutDocument) {
        try {
            SharedPreferences sharedPreferences
                    = getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);

            String Vendorname = sharedPreferences.getString("VendorName", "");

            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 22, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("CONSOLIDATED SALES REPORT");
            titlepara.setSpacingBefore(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_CENTER);
            layoutDocument.add(titlepara);

            String vendorname = "Vendor: " + Vendorname;
            com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
            vendorpara.setSpacingBefore(20);
            vendorpara.setAlignment(Element.ALIGN_LEFT);
            layoutDocument.add(vendorpara);

            com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date: " + getDate());
            datepara.setAlignment(Element.ALIGN_LEFT);
            datepara.setSpacingBefore(5);
            datepara.setSpacingAfter(20);
            layoutDocument.add(datepara);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addItemRows(Document layoutDocument) {
        try {
            String rsunit = "Rs.", tmcprice;
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

            PdfPCell itemcell = new PdfPCell(new Phrase("Item"));
            itemcell.setBorder(Rectangle.NO_BORDER);
            itemcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            itemcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            itemcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            itemcell.setPaddingLeft(10);
            itemcell.setFixedHeight(30);
            table.addCell(itemcell);

            PdfPCell qtycell = new PdfPCell(new Phrase("Quantity"));
            qtycell.setBorder(Rectangle.NO_BORDER);
            qtycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            qtycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qtycell.setFixedHeight(30);
            table.addCell(qtycell);

            PdfPCell pricecell = new PdfPCell(new Phrase("Price"));
            pricecell.setBorder(Rectangle.NO_BORDER);
            pricecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pricecell.setFixedHeight(30);
            pricecell.setPaddingRight(10);
            table.addCell(pricecell);

            PdfPCell itemnamecell = null;
            PdfPCell itemqtycell = null;
            PdfPCell itempricecell = null;
            for (String SubCtgykey : tmcSubCtgykey) {
                int i_value = 0;
                String subCtgyTotal = "0";
                double subCtgyTotaldouble = 0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                String SubCtgyName = "", menuid;
                Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
                try {
                    if (!subCtgyName_object.equals(null)) {
                        SubCtgyName = Objects.requireNonNull(subCtgyName_object).getTmcsubctgyname();
                    }
                } catch (Exception e) {
                    SubCtgyName = "";

                    e.printStackTrace();

                }
                for (int j = 0; j < Order_Item_List.size(); j++) {
                    menuid = Order_Item_List.get(j);
                    Modal_OrderDetails itemRow = OrderItem_hashmap.get(menuid);
                    String itemName = "";
                    try {
                        if (!itemRow.equals(null)) {
                            itemName = itemRow.getItemname();
                        }
                    } catch (Exception e) {
                        itemName = "";

                        e.printStackTrace();

                    }
                    String subCtgyKey_fromHashmap = "";
                    try {
                        if (!itemRow.equals(null)) {
                            subCtgyKey_fromHashmap = itemRow.getTmcsubctgykey();
                        }
                    } catch (Exception e) {
                        subCtgyKey_fromHashmap = "";

                        e.printStackTrace();

                    }

                    try {
                        subCtgyTotal = SubCtgywiseTotalHashmap.get(SubCtgykey);
                        subCtgyTotaldouble = Double.parseDouble(subCtgyTotal);
                        subCtgyTotal = decimalFormat.format(subCtgyTotaldouble);
                    } catch (Exception e) {
                        subCtgyTotal = "0";
                        e.printStackTrace();
                    }


                    if (subCtgyKey_fromHashmap.equals(SubCtgykey)) {

                        if (i_value != 0) {
                            itemnamecell = new PdfPCell(new Phrase((itemName)));
                            itemnamecell.setBorder(Rectangle.BOTTOM);
                            itemnamecell.setBorderColor(BaseColor.LIGHT_GRAY);
                            itemnamecell.setMinimumHeight(30);
                            itemnamecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            itemnamecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            itemnamecell.setPaddingLeft(10);
                            String Quantity = itemRow.getQuantity();
                            itemqtycell = new PdfPCell(new Phrase("" + Quantity));
                            itemqtycell.setBorder(Rectangle.BOTTOM);
                            itemqtycell.setBorderColor(BaseColor.LIGHT_GRAY);
                            itemqtycell.setMinimumHeight(30);
                            itemqtycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            itemqtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                            double totalval = Double.parseDouble(itemRow.getTmcprice());
                            tmcprice = decimalFormat.format(totalval);
                            tmcprice = rsunit + tmcprice;
                            itempricecell = new PdfPCell(new Phrase(tmcprice));
                            itempricecell.setBorder(Rectangle.BOTTOM);
                            itempricecell.setBorderColor(BaseColor.LIGHT_GRAY);
                            itempricecell.setMinimumHeight(30);
                            itempricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            itempricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            itempricecell.setPaddingRight(10);


                            table.addCell(itemnamecell);
                            table.addCell(itemqtycell);
                            table.addCell(itempricecell);
                        }
                    }
                    if (i_value == 0) {
                        i_value = 1;
                        j = j - 1;
                        PdfPCell SubCtgycell = new PdfPCell(new Phrase(SubCtgyName));
                        SubCtgycell.setBorder(Rectangle.NO_BORDER);
                        SubCtgycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        SubCtgycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        SubCtgycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        SubCtgycell.setPaddingLeft(10);
                        SubCtgycell.setFixedHeight(30);
                        table.addCell(SubCtgycell);

                        PdfPCell qtySubCtgycell = new PdfPCell(new Phrase(""));
                        qtySubCtgycell.setBorder(Rectangle.NO_BORDER);
                        qtySubCtgycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        qtySubCtgycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        qtySubCtgycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        qtySubCtgycell.setFixedHeight(30);
                        table.addCell(qtySubCtgycell);


                        PdfPCell priceSubCtgycell = new PdfPCell(new Phrase(subCtgyTotal));
                        priceSubCtgycell.setBorder(Rectangle.NO_BORDER);
                        priceSubCtgycell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        priceSubCtgycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        priceSubCtgycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        priceSubCtgycell.setFixedHeight(30);
                        priceSubCtgycell.setPaddingRight(10);
                        table.addCell(priceSubCtgycell);


                    }
                }
            }
            layoutDocument.add(table);


            PdfPTable tablePaymentMode = new PdfPTable(4);
            tablePaymentMode.setWidthPercentage(100);
            tablePaymentMode.setSpacingBefore(20);
            PdfPCell paymentModeemptycell;
            PdfPCell paymentModeemptycellone;
            PdfPCell paymentModeitemkeycell;
            PdfPCell paymentModeitemValueCell;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            double posorder_discountAmount = 0;
            double apporder_discountAmount = 0;
            double swiggyorder_discountAmount = 0;
            double dunzoorder_discountAmount = 0;
            double bigBasketorder_discountAmount = 0;
            double wholeSaleorder_discountAmount = 0;
            double phoneorder_discountAmount = 0;
            double creditorder_discountAmount = 0;


            double posorder_Amount = 0;
            double apporder_Amount = 0;
            double swiggyorder_Amount = 0;
            double dunzoorder_Amount = 0;
            double phoneorder_Amount = 0;
            double bigBasketorder_Amount = 0;
            double wholeSaleorder_Amount = 0;
            double creditorder_Amount = 0;

            String Payment_Amount = "0";
            double discountAmount = 0;
            double deliveryChargee=0,deliveryCharge_phoneOrder=0,deliveryCharge_creditOrder=0;


            for (String orderid : couponDiscountOrderidArray) {
                String Discount_amount = couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(Discount_amount);
                discountAmount = discountAmount + CouponDiscount_double;

            }


            for (String orderid : creditOrders_couponDiscountOrderidArray) {
                String Discount_amount = creditOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(Discount_amount);
                creditorder_discountAmount = creditorder_discountAmount + CouponDiscount_double;

            }



            for (String orderid : couponDiscountOrderidArray) {
                String appDiscount_amount = couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(appDiscount_amount);
                apporder_discountAmount = apporder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : Pos_couponDiscountOrderidArray) {
                String posDiscount_amount = Pos_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(posDiscount_amount);
                posorder_discountAmount = posorder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : phoneOrders_couponDiscountOrderidArray) {
                String phoneOrdersDiscount_Amount = phoneOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(phoneOrdersDiscount_Amount);
                phoneorder_discountAmount = phoneorder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : wholesaleOrders_couponDiscountOrderidArray) {
                String wholeSaleOrdersDiscount_Amount = wholesaleOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(wholeSaleOrdersDiscount_Amount);
                wholeSaleorder_discountAmount = wholeSaleorder_discountAmount + CouponDiscount_double;

            }
            for (String orderid : swiggyOrders_couponDiscountOrderidArray) {
                String swiggyOrdersDiscount_Amount = swiggyOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(swiggyOrdersDiscount_Amount);
                swiggyorder_discountAmount = swiggyorder_discountAmount + CouponDiscount_double;

            }
            for (String orderid : dunzoOrders_couponDiscountOrderidArray) {
                String dunzoOrdersDiscount_Amount = dunzoOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(dunzoOrdersDiscount_Amount);
                dunzoorder_discountAmount = dunzoorder_discountAmount + CouponDiscount_double;

            }

            for (String orderid : bigBasketOrders_couponDiscountOrderidArray) {
                String bigBasketOrdersDiscount_Amount = bigBasketOrders_couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(bigBasketOrdersDiscount_Amount);
                bigBasketorder_discountAmount = bigBasketorder_discountAmount + CouponDiscount_double;

            }


            for (String orderid : deliveryChargeOrderidArray) {
                String DeliveryChargeString = deliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryChargee = deliveryChargee + deliveryCharge_double;

            }


            for (String orderid : creditOrderdeliveryChargeOrderidArray) {
                String DeliveryChargeString = creditOrderdeliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryCharge_creditOrder = deliveryCharge_creditOrder + deliveryCharge_double;

            }





            for (String orderid : phoneorderdeliveryChargeOrderidArray) {
                String DeliveryChargeString = phoneorderdeliveryCharge_hashmap.get(orderid);
                double deliveryCharge_double = Double.parseDouble(DeliveryChargeString);
                deliveryCharge_phoneOrder = deliveryCharge_phoneOrder  + deliveryCharge_double;

            }



            for (String ordertype : ordertypeArray) {
                String replacmentFromTextview = "",refundFromTextview = "";
                double replacment_doubleFromTextview = 0,refund_doubleFromTextview = 0;

                Modal_OrderDetails modal_orderDetails = ordertypeHashmap.get(ordertype);
                try {
                    paymentModeemptycell = new PdfPCell(new Phrase(""));
                    paymentModeemptycell.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycell.setFixedHeight(25);
                    tablePaymentMode.addCell(paymentModeemptycell);

                    paymentModeemptycellone = new PdfPCell(new Phrase(""));
                    paymentModeemptycellone.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycellone.setFixedHeight(25);
                    tablePaymentMode.addCell(paymentModeemptycellone);

                    if ((ordertype.toUpperCase().equals(Constants.POSORDER)) || (ordertype.equals("posorder"))) {
                        posorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getPosSales())));
                        ////Log.i(Constants.TAG,"Consolidated Report  new posorder_Amount   " +posorder_Amount);
                        posorder_Amount = posorder_Amount - posorder_discountAmount;

                        try{
                            replacmentFromTextview = replacementAmount_textwidget.getText().toString();
                        }
                        catch (Exception e){
                            replacmentFromTextview="0";
                            e.printStackTrace();
                        }

                        try{
                            refundFromTextview = refundAmount_textwidget.getText().toString();

                        }
                        catch (Exception e){
                            refundFromTextview = "0";
                            e.printStackTrace();
                        }
                        try{
                            replacment_doubleFromTextview = Math.round(Double.parseDouble(replacmentFromTextview));
                        }
                        catch (Exception e){
                            replacment_doubleFromTextview =0;
                            e.printStackTrace();
                        }

                        try{
                            refund_doubleFromTextview = Math.round(Double.parseDouble(refundFromTextview));

                        }
                        catch (Exception e){
                            refund_doubleFromTextview = 0;
                            e.printStackTrace();
                        }



                        posorder_Amount = posorder_Amount - (posorder_discountAmount +refund_doubleFromTextview +replacment_doubleFromTextview);

                        paymentModeitemkeycell = new PdfPCell(new Phrase("POSORDER  : "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + posorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.APPORDER)) || (ordertype.equals("apporder"))) {
                        apporder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getAppSales())));
                        apporder_Amount = apporder_Amount - apporder_discountAmount;
                        apporder_Amount = apporder_Amount + deliveryChargee;

                        paymentModeitemkeycell = new PdfPCell(new Phrase("APPORDER :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + apporder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.PhoneOrder)) || (ordertype.equals("Phone Order"))) {
                        phoneorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getPhoneOrderSales())));
                        phoneorder_Amount = phoneorder_Amount - phoneorder_discountAmount;
                        phoneorder_Amount = phoneorder_Amount+deliveryCharge_phoneOrder;

                        paymentModeitemkeycell = new PdfPCell(new Phrase("PHONE ORDER :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + phoneorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.CREDIT)) || (ordertype.equals("credit"))) {
                        creditorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales())));
                        creditorder_Amount = creditorder_Amount -  creditorder_discountAmount;
                        creditorder_Amount = creditorder_Amount + deliveryCharge_creditOrder;

                        paymentModeitemkeycell = new PdfPCell(new Phrase("Credit Order :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + creditorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }



                    if ((ordertype.toUpperCase().equals(Constants.WholeSaleOrder)) || (ordertype.equals("WholeSale Order"))) {
                        wholeSaleorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getWholeSaleOrderSales())));
                        wholeSaleorder_Amount = wholeSaleorder_Amount - wholeSaleorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("WHOLESALE ORDER :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + wholeSaleorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }
                    if ((ordertype.toUpperCase().equals(Constants.SwiggyOrder)) || (ordertype.equals("Swiggy Order"))) {
                        swiggyorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getSwiggySales())));
                        swiggyorder_Amount = swiggyorder_Amount - swiggyorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("SWIGGY ORDER :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + swiggyorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                    if ((ordertype.toUpperCase().equals(Constants.DunzoOrder)) || (ordertype.equals("Dunzo Order"))) {
                        dunzoorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getDunzoSales())));
                        dunzoorder_Amount = dunzoorder_Amount - dunzoorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("DUNZO ORDER :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + dunzoorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }


                    if ((ordertype.toUpperCase().equals(Constants.BigBasket)) || (ordertype.equals("BigBasket Order"))) {
                        bigBasketorder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales())));
                        bigBasketorder_Amount = bigBasketorder_Amount - bigBasketorder_discountAmount;
                        paymentModeitemkeycell = new PdfPCell(new Phrase("BIGBASKET ORDER :  "));
                        paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemkeycell.setMinimumHeight(25);
                        paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tablePaymentMode.addCell(paymentModeitemkeycell);


                        paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + bigBasketorder_Amount));
                        paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                        paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                        paymentModeitemValueCell.setMinimumHeight(25);
                        paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        paymentModeitemValueCell.setPaddingRight(10);
                        tablePaymentMode.addCell(paymentModeitemValueCell);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            layoutDocument.add(tablePaymentMode);


            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(20);
            PdfPCell emptycell;
            PdfPCell emptycellone;
            PdfPCell emptycelltwo;
            for (int i = 0; i < finalBillDetails.size(); i++) {
                String key = finalBillDetails.get(i);
                String value = FinalBill_hashmap.get(key);
                //Log.d("ExportReportActivity", "itemTotalRowsList name "+key);

                //Log.d("ExportReportActivity", "itemTotalRowsList value "+value);
                emptycell = new PdfPCell(new Phrase(""));
                emptycell.setBorder(Rectangle.NO_BORDER);
                emptycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emptycell.setFixedHeight(25);
                table1.addCell(emptycell);

                emptycellone = new PdfPCell(new Phrase(""));
                emptycellone.setBorder(Rectangle.NO_BORDER);
                emptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emptycellone.setFixedHeight(25);
                table1.addCell(emptycellone);

                itemqtycell = new PdfPCell(new Phrase(key));
                itemqtycell.setBorderColor(BaseColor.LIGHT_GRAY);
                itemqtycell.setBorder(Rectangle.NO_BORDER);
                itemqtycell.setMinimumHeight(25);
                itemqtycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itemqtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table1.addCell(itemqtycell);


                itempricecell = new PdfPCell(new Phrase("Rs. " + value));
                itempricecell.setBorderColor(BaseColor.LIGHT_GRAY);
                itempricecell.setBorder(Rectangle.NO_BORDER);
                itempricecell.setMinimumHeight(25);
                itempricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itempricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itempricecell.setPaddingRight(10);
                table1.addCell(itempricecell);


            }
            layoutDocument.add(table1);


        } catch (Exception ex) {
            ex.printStackTrace();
        }


}
   /* RoundRectangle roundRectange = new RoundRectangle();
                itempricecell.setCellEvent(roundRectange);
    public class RoundRectangle implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect,
                               PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                    rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.stroke();
        }
    }


    */


}