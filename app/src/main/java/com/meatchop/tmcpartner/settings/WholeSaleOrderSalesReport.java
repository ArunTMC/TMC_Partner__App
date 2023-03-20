package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
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
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.report_activity_model.ListData;
import com.meatchop.tmcpartner.settings.report_activity_model.ListItem;
import com.meatchop.tmcpartner.settings.report_activity_model.ListSection;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableService;
import com.pos.printer.AsyncEscPosPrint;
import com.pos.printer.AsyncEscPosPrinter;
import com.pos.printer.AsyncUsbEscPosPrint;
import com.pos.printer.PrinterFunctions;
import com.pos.printer.usb.UsbPrintersConnectionsLocal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.Set;
import java.util.TimeZone;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class WholeSaleOrderSalesReport extends AppCompatActivity {
    LinearLayout fetchData_Layout,PrintReport_Layout,generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    TextView appOrdersCount_textwidget,instruction_textview,refundAmount_textwidget,replacementAmount_textwidget,vendorName,creditSales,totalSales_headingText,cashSales, cardSales,upiSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;
    String vendorKey,vendorname;

    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List  = new ArrayList<>();
    Adapter_Pos_Sales_Report adapter = new Adapter_Pos_Sales_Report();
    public static List<Modal_OrderDetails> SubCtgyKey_List  = new ArrayList<>();
    public static HashMap<String, Modal_OrderDetails> SubCtgyKey_hashmap = new HashMap();


    public static List<String> wholeSaleOrderpaymentModeArray  = new ArrayList<>();
    public static HashMap<String, Modal_OrderDetails>  wholeSaleOrderpaymentModeHashmap  = new HashMap();;

    public static List<String> wholeSaleOrderpaymentMode_DiscountOrderid = new ArrayList<>();
    public static HashMap<String, Modal_OrderDetails>  wholeSaleOrderpaymentMode_DiscountHashmap  = new HashMap();;



    public static List<String> finalBillDetails = new ArrayList<>();
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();


    public static List<String> SubCtgywiseTotalArray = new ArrayList<>();
    public static HashMap<String, String>  SubCtgywiseTotalHashmap  = new HashMap();;


    List<ListData> dataList = new ArrayList<>();

    public static List<String> tmcSubCtgykey;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;

    public static HashMap<String,  List<Modal_OrderDetails>> tmcSubCtgywise_sorted_hashmap = new HashMap();
    double screenInches;
    String CurrentDate;
    String DateString;
    boolean isgetOrderForSelectedDateCalled=false;
    double CouponDiscount=0;
  
    boolean isgetReplacementOrderForSelectedDateCalled = false;

    boolean isOrderDetailsResponseReceivedForSelectedDate = false;

    boolean isReplacementTransacDetailsResponseReceivedForSelectedDate = false;



    ListView WholeSaleOrderReport_Listview;
    ScrollView scrollView;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.WholeSaleOrderSalesReport";
    String printerType_sharedPreference="";

    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;

    public static List<String> array_of_orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whole_sale_order_report);


        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        vendorName = findViewById(R.id.vendorName);
        fetchData_Layout = findViewById(R.id.fetchData_Layout);
        instruction_textview = findViewById(R.id.instruction_textview);
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);

        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        WholeSaleOrderReport_Listview = findViewById(R.id.WholeSaleOrderReport_Listview);
        generateReport_Layout = findViewById(R.id.generateReport_Layout);
        PrintReport_Layout = findViewById(R.id.PrintReport_Layout);
        totalAmt_without_GST = findViewById(R.id.totalAmt_without_GST);
        totalCouponDiscount_Amt = findViewById(R.id.totalCouponDiscount_Amt);
        totalAmt_with_CouponDiscount = findViewById(R.id.totalAmt_with_CouponDiscount);
        totalGST_Amt = findViewById(R.id.totalGST_Amt);
        final_sales = findViewById(R.id.final_sales);
        cashSales = findViewById(R.id.cashSales);
        cardSales = findViewById(R.id.cardSales);
        upiSales  = findViewById(R.id.upiSales);
        creditSales =  findViewById(R.id.creditSales);
      
        refundAmount_textwidget  = findViewById(R.id.refundAmount_textwidget);
        replacementAmount_textwidget= findViewById(R.id.replacementAmount_textwidget);

        
        scrollView  = findViewById(R.id.scrollView);
        totalSales_headingText = findViewById(R.id.totalRating_headingText);
   
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        tmcSubCtgykey = new ArrayList<>();
        SubCtgyKey_List =new ArrayList<>();
        wholeSaleOrderpaymentModeArray = new ArrayList<>();
        wholeSaleOrderpaymentMode_DiscountOrderid = new ArrayList<>();
        array_of_orderId = new ArrayList<>();


        SubCtgywiseTotalArray = new ArrayList<>();
       
         Order_Item_List.clear();
        array_of_orderId.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        wholeSaleOrderpaymentModeArray .clear();
      
       
        SubCtgywiseTotalArray.clear();
        SubCtgywiseTotalHashmap.clear();
        FinalBill_hashmap.clear();
        CurrentDate = getDate();
        dateSelector_text.setText(CurrentDate);
        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(WholeSaleOrderSalesReport .this);
           // Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
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
       // orderdetailsnewschema = true;



        SharedPreferences shared_PF_PrinterData = getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
        printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));

/*
        CurrentDate = getDate();
        DateString= getDate();

        dateSelector_text.setText(CurrentDate);
        vendorName.setText(vendorname);
        
        getOrderForSelectedDate(CurrentDate, vendorKey);
        
 */
        DateString ="";
        dateSelector_text.setText(Constants.Empty_Date_Format);

        scrollView.setVisibility(View.GONE);
        instruction_textview.setVisibility(View.VISIBLE);


        getTmcSubCtgyList(vendorKey);
        scrollView.fullScroll(View.FOCUS_UP);

        dateSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openDatePicker();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(WholeSaleOrderSalesReport.this,"Loading.... Please Wait",Toast.LENGTH_SHORT).show();
            }
        });

        fetchData_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DateString.equals("")){
                    Toast.makeText(WholeSaleOrderSalesReport.this, "First Select Date !! Before Fetch the Data", Toast.LENGTH_SHORT).show();
                }
                else {

                    scrollView.setVisibility(View.VISIBLE);
                    instruction_textview.setVisibility(View.GONE);

                     Order_Item_List.clear();
                    array_of_orderId.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    wholeSaleOrderpaymentModeArray.clear();
                    wholeSaleOrderpaymentModeHashmap.clear();
                    tmcSubCtgywise_sorted_hashmap.clear();

                    SubCtgywiseTotalArray.clear();
                    tmcSubCtgykey.clear();


                    SubCtgywiseTotalHashmap.clear();
                    Adjusting_Widgets_Visibility(true);

                    if (orderdetailsnewschema) {
                        String dateAsnewFormat = convertOldFormatDateintoNewFormat(DateString);
                        callVendorOrderDetailsSeviceAndInitCallBack(dateAsnewFormat, dateAsnewFormat, vendorKey);

                    } else {

                        getOrderForSelectedDate(DateString, vendorKey);
                    }

                }


            }
        });






        PrintReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(screenInches>Constants.default_mobileScreenSize){

                    try {
                        if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                            printUsingUSBPrinterReport();

                        }
                        else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                          //  printUsingBluetoothPrinterReport();

                        }
                        else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                            printUsingPOSMachineReport();

                        }
                        else {
                            Toast.makeText(WholeSaleOrderSalesReport.this,"ERROR !! There is no Printer Type",Toast.LENGTH_SHORT).show();

                        }


                    }
                    catch(Exception e ){

                        Toast.makeText(WholeSaleOrderSalesReport.this,"ERROR !! Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }
                }
                else{
                    Toast.makeText(WholeSaleOrderSalesReport.this,"Cant Find a Printer",Toast.LENGTH_LONG).show();
                }
                
                
          /*      if(screenInches>8){

                    try {
                        printReport();

                    }
                    catch(Exception e ){

                        Toast.makeText(WholeSaleOrderSalesReport.this,"Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }
                }
                else{
                    Toast.makeText(WholeSaleOrderSalesReport.this,"Cant Find a Printer",Toast.LENGTH_LONG).show();
                }
                
           */
            }
            
           
        });


        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SDK_INT >= Build.VERSION_CODES.R) {

                    if(Environment.isExternalStorageManager()){
                        if(Order_Item_List.size()>0) {

                            try {
                                exportReport();
                                Adjusting_Widgets_Visibility(true);


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                        else{
                            Toast.makeText(WholeSaleOrderSalesReport.this, "There is no data to Export", Toast.LENGTH_SHORT).show();

                        }
                    }
                    else{
                        try {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                            startActivityForResult(intent, 2296);
                        } catch (Exception e) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivityForResult(intent, 2296);
                        }
                    }

                } else {


                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(WholeSaleOrderSalesReport.this, WRITE_EXTERNAL_STORAGE);
                    //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                    // If do not grant write external storage permission.
                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        // Request user to grant write external storage permission.
                        ActivityCompat.requestPermissions(WholeSaleOrderSalesReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                    } else {
                        if(Order_Item_List.size()>0) {
                            Adjusting_Widgets_Visibility(true);

                            try {
                                exportReport();


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                        else{
                            Toast.makeText(WholeSaleOrderSalesReport.this, "There is no data to Export", Toast.LENGTH_SHORT).show();

                        }
                    }
                }




            }
        });








    }


    private String convertOldFormatDateintoNewFormat(String todaysdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDate = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

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
                    processArrayAndgetData(orderslist_fromResponse);
                }
                else{


                    Adjusting_Widgets_Visibility(false);
                    isVendorOrdersTableServiceCalled = false;

                    scrollView.setVisibility(View.GONE);
                    instruction_textview.setVisibility(View.VISIBLE);
                    instruction_textview.setText("There is no Order On this Date");
                    Order_Item_List.clear();
                    array_of_orderId.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    wholeSaleOrderpaymentModeArray.clear();
                    wholeSaleOrderpaymentModeHashmap.clear();
                    tmcSubCtgywise_sorted_hashmap.clear();

                    SubCtgywiseTotalArray.clear();
                    tmcSubCtgykey.clear();
                    SubCtgywiseTotalHashmap.clear();
                    CouponDiscount =0;


                    dataList.clear();
                    adapter.notifyDataSetChanged();
                    ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);

                    addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                    Toast.makeText(WholeSaleOrderSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();



                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

                Adjusting_Widgets_Visibility(false);
                isVendorOrdersTableServiceCalled = false;

                scrollView.setVisibility(View.GONE);
                instruction_textview.setVisibility(View.VISIBLE);
                instruction_textview.setText("There is some error"+String.valueOf(error));

                Toast.makeText(WholeSaleOrderSalesReport.this, "There is Some error on this data", Toast.LENGTH_LONG).show();
                Order_Item_List.clear();
                array_of_orderId.clear();
                OrderItem_hashmap.clear();
                finalBillDetails.clear();
                FinalBill_hashmap.clear();
                wholeSaleOrderpaymentModeArray.clear();
                wholeSaleOrderpaymentModeHashmap.clear();
                tmcSubCtgywise_sorted_hashmap.clear();

                SubCtgywiseTotalArray.clear();
                tmcSubCtgykey.clear();
                SubCtgywiseTotalHashmap.clear();
                CouponDiscount =0;


                dataList.clear();
                adapter.notifyDataSetChanged();
                ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);

                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                isVendorOrdersTableServiceCalled = false;

            }
        };

        CouponDiscount=0;
        mVolleyService = new VendorOrdersTableService(mResultCallback,WholeSaleOrderSalesReport.this);
      //  String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_MultipleOrdertype + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=WHOLESALEORDER";
          String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_SingleOrdertype + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=WHOLESALEORDER";
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }



    private void processArrayAndgetData(List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {

        for(int i =0 ; i< orderslist_fromResponse.size(); i++){
            String paymentMode = "", ordertype = "", orderid = "", slotname = "",deliveryAmount ="" , couponDiscount = "";
            JSONArray itemdesp_JSONArray = new JSONArray();

            Modal_ManageOrders_Pojo_Class orders_pojo_class_fromResponse =  orderslist_fromResponse.get(i);
            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
            try{

                try{
                    paymentMode = String.valueOf(orders_pojo_class_fromResponse.getPaymentmode().toUpperCase());
                    modal_orderDetails.paymentmode = paymentMode;
                }
                catch (Exception e){
                    paymentMode ="";
                    modal_orderDetails.paymentmode = "";

                    e.printStackTrace();
                }

                try{

                    ordertype = String.valueOf(orders_pojo_class_fromResponse.getOrdertype().toUpperCase());
                    modal_orderDetails.ordertype = ordertype;

                }
                catch (Exception e){
                    ordertype = "";
                    modal_orderDetails.ordertype = "";

                    e.printStackTrace();
                }
                if(ordertype.equals("") || ordertype.equals(null) || ordertype.equals("NULL")){
                    try{

                        ordertype = String.valueOf(orders_pojo_class_fromResponse.getOrderType().toUpperCase());
                        modal_orderDetails.ordertype = ordertype;

                    }
                    catch (Exception e){
                        ordertype = "";
                        modal_orderDetails.ordertype = "";

                        e.printStackTrace();
                    }
                }


                try{

                    orderid = String.valueOf(orders_pojo_class_fromResponse.getOrderid());
                    modal_orderDetails.orderid = orderid;

                }
                catch (Exception e){
                    orderid = "";
                    modal_orderDetails.orderid = "";
                    e.printStackTrace();
                }




                try{
                    itemdesp_JSONArray  = orders_pojo_class_fromResponse.getItemdesp();
                    modal_orderDetails.itemdesp = itemdesp_JSONArray;

                }
                catch (Exception e){
                    itemdesp_JSONArray = new JSONArray(orders_pojo_class_fromResponse.getItemdesp_string());
                    modal_orderDetails.itemdesp = itemdesp_JSONArray;

                    e.printStackTrace();
                }




                try{

                    couponDiscount = String.valueOf(orders_pojo_class_fromResponse.getCoupondiscamount());
                    modal_orderDetails.coupondiscount = couponDiscount;

                }
                catch (Exception e){
                    couponDiscount ="";
                    modal_orderDetails.coupondiscount = couponDiscount;

                    e.printStackTrace();
                }


                try{

                    deliveryAmount = String.valueOf(orders_pojo_class_fromResponse.getDeliveryamount());
                    modal_orderDetails.deliveryamount = deliveryAmount;

                }
                catch (Exception e){
                    deliveryAmount ="";
                    modal_orderDetails.deliveryamount = deliveryAmount;

                    e.printStackTrace();
                }



                try{


                    if (!array_of_orderId.contains(orderid)) {
                        array_of_orderId.add(orderid);


                        if ((ordertype.equals(Constants.WholeSaleOrder))) {

                            try {
                                if (couponDiscount.equals("")) {
                                    couponDiscount = "0";

                                    double CouponDiscount_double = Double.parseDouble(couponDiscount);
                                    CouponDiscount = CouponDiscount + CouponDiscount_double;

                                    if (!wholeSaleOrderpaymentMode_DiscountOrderid.contains(orderid)) {
                                        wholeSaleOrderpaymentMode_DiscountOrderid.add(orderid);
                                        boolean isAlreadyAvailable = false;
                                        try {
                                            isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            ;
                                        }
                                        if (isAlreadyAvailable) {
                                            Modal_OrderDetails modal_orderDetails1 = wholeSaleOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                            String discountAmount = Objects.requireNonNull(modal_orderDetails1).getCoupondiscount();
                                            double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                            double discountAmount_double = Double.parseDouble(couponDiscount);

                                            discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                            modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                        } else {
                                            Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                            modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount));
                                            wholeSaleOrderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                        }


                                    } else {
                                        //Log.d(Constants.TAG, "mode already availabe" );

                                    }
                                } else {

                                    double CouponDiscount_double = Double.parseDouble(couponDiscount);
                                    CouponDiscount = CouponDiscount + CouponDiscount_double;


                                    if (!wholeSaleOrderpaymentMode_DiscountOrderid.contains(orderid)) {
                                        wholeSaleOrderpaymentMode_DiscountOrderid.add(orderid);
                                        boolean isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                        if (isAlreadyAvailable) {
                                            Modal_OrderDetails modal_orderDetails1 = wholeSaleOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                            String discountAmount = Objects.requireNonNull(modal_orderDetails1).getCoupondiscount();
                                            double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                            double discountAmount_double = Double.parseDouble(couponDiscount);

                                            discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                            modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                        } else {
                                            Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                            modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount));
                                            wholeSaleOrderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                        }


                                        //Log.d(Constants.TAG, "mode already availabe" );


                                    } else {
                                        //Log.d(Constants.TAG, "mode already availabe" );

                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();


                            }
                        }

                        getItemDetailsFromItemDespArray(modal_orderDetails, paymentMode, ordertype);





                    }
                    else {
                        Toast.makeText(WholeSaleOrderSalesReport.this, "- ", Toast.LENGTH_LONG).show();
                        //Log.d(Constants.TAG, "repeated orderid e: "+orderid);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }







            try{
                if(orderslist_fromResponse.size() - i == 1) {

                    try{
                        if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
                            isOrderDetailsResponseReceivedForSelectedDate = true;
                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            prepareContent();
                            setAdapter();

                        }

                        else{

                            Adjusting_Widgets_Visibility(false);
                            isVendorOrdersTableServiceCalled = false;

                            scrollView.setVisibility(View.GONE);
                            instruction_textview.setVisibility(View.VISIBLE);
                            instruction_textview.setText("There is no Order On this Date");
                            Order_Item_List.clear();
                            array_of_orderId.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            wholeSaleOrderpaymentModeArray.clear();
                            wholeSaleOrderpaymentModeHashmap.clear();
                            tmcSubCtgywise_sorted_hashmap.clear();

                            SubCtgywiseTotalArray.clear();
                            tmcSubCtgykey.clear();
                            SubCtgywiseTotalHashmap.clear();
                            CouponDiscount =0;


                            dataList.clear();
                            adapter.notifyDataSetChanged();
                            ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);

                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            Toast.makeText(WholeSaleOrderSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);

                        }
                    }
                    catch (Exception e ){

                        Adjusting_Widgets_Visibility(false);
                        isVendorOrdersTableServiceCalled = false;

                        scrollView.setVisibility(View.GONE);
                        instruction_textview.setVisibility(View.VISIBLE);
                        instruction_textview.setText("There is no Order On this Date");
                        Order_Item_List.clear();
                        array_of_orderId.clear();
                        OrderItem_hashmap.clear();
                        finalBillDetails.clear();
                        FinalBill_hashmap.clear();
                        wholeSaleOrderpaymentModeArray.clear();
                        wholeSaleOrderpaymentModeHashmap.clear();
                        tmcSubCtgywise_sorted_hashmap.clear();

                        SubCtgywiseTotalArray.clear();
                        tmcSubCtgykey.clear();
                        SubCtgywiseTotalHashmap.clear();
                        CouponDiscount =0;


                        dataList.clear();
                        adapter.notifyDataSetChanged();
                        ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);

                        addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                        Toast.makeText(WholeSaleOrderSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                        Adjusting_Widgets_Visibility(false);
                        e.printStackTrace();
                    }


                }
            }
            catch (Exception e ){
                e.printStackTrace();
            }



        }


    }




    private void getOrderForSelectedDate(String dateString, String vendorKey) {
        if(isgetOrderForSelectedDateCalled){
            return;
        }
        isgetOrderForSelectedDateCalled = true;
         Order_Item_List.clear();
        array_of_orderId.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        wholeSaleOrderpaymentModeArray.clear();
        wholeSaleOrderpaymentModeHashmap.clear();
        tmcSubCtgywise_sorted_hashmap.clear();
    
        SubCtgywiseTotalArray.clear();
        tmcSubCtgykey.clear();
        CouponDiscount =0;
       
        SubCtgywiseTotalHashmap.clear();
        Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetWholeSalesOrderswithDate_forReport + "?orderplaceddate=" + dateString+"&vendorkey="+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        try {
                            String paymentMode="",ordertype = "",orderid ="";
                            ;

                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (; i1 < (arrayLength); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
//                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
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
                                        //Log.d(Constants.TAG, "There is no itemdesp: " );
                                    }

                                    if (json.has("orderid")) {
                                        try {
                                            modal_orderDetails.orderid = String.valueOf(json.get("orderid"));
                                            orderid = String.valueOf(json.get("orderid"));
                                            //Log.d(Constants.TAG, "orderid"  + String.valueOf(json.get("orderid")));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    } else {

                                        modal_orderDetails.orderid = "There is no orderid";


                                    }


                                    if (json.has("ordertype")) {
                                        try {
                                            modal_orderDetails.ordertype = String.valueOf(json.get("ordertype"));
                                            ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        modal_orderDetails.ordertype = "There is no OrderType";
                                        //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                    }
                                    if (json.has("paymentmode")) {
                                        try {
                                            paymentMode = (String.valueOf(json.get("paymentmode")).toUpperCase());

                                            modal_orderDetails.paymentmode = (String.valueOf(json.get("paymentmode")).toUpperCase());
                                            //Log.d(Constants.TAG, "PaymentMode: " + String.valueOf(json.get("paymentmode")));

                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }

                                    } else {

                                        modal_orderDetails.paymentmode = "There is no payment mode";
                                        //Log.d(Constants.TAG, "There is no PaymentMode: " + String.valueOf(json.get("ordertype")));


                                    }

                                    if (!array_of_orderId.contains(orderid)){
                                        array_of_orderId.add(orderid);
                                    if ((ordertype.equals(Constants.WholeSaleOrder))) {
                                        try {
                                            if (json.has("coupondiscount")) {

                                                modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                    try {
                                                        if (couponDiscount_string.equals("")) {
                                                            couponDiscount_string = "0";

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            CouponDiscount = CouponDiscount + CouponDiscount_double;

                                                            if (!wholeSaleOrderpaymentMode_DiscountOrderid.contains(orderid)) {
                                                                wholeSaleOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = false;
                                                                try {
                                                                    isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                    ;
                                                                }
                                                                if (isAlreadyAvailable) {
                                                                    Modal_OrderDetails modal_orderDetails1 = wholeSaleOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                } else {
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    wholeSaleOrderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                                                }


                                                            } else {
                                                                //Log.d(Constants.TAG, "mode already availabe" );

                                                            }
                                                        } else {

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            CouponDiscount = CouponDiscount + CouponDiscount_double;


                                                            if (!wholeSaleOrderpaymentMode_DiscountOrderid.contains(orderid)) {
                                                                wholeSaleOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                                if (isAlreadyAvailable) {
                                                                    Modal_OrderDetails modal_orderDetails1 = wholeSaleOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                } else {
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    wholeSaleOrderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                                                }


                                                                //Log.d(Constants.TAG, "mode already availabe" );


                                                            } else {
                                                                //Log.d(Constants.TAG, "mode already availabe" );

                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();


                                                    }


                                                    //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                String couponDiscount_string = String.valueOf("0");
                                                double CouponDiscount_double = Double.parseDouble(couponDiscount_string);

                                                CouponDiscount = CouponDiscount + CouponDiscount_double;


                                                modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                            }


                                            //Log.d(Constants.TAG, "This orders payment mode: " +paymentMode);


                                            getItemDetailsFromItemDespArray(modal_orderDetails, paymentMode, ordertype);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
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
                        try{
                            if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {

                             /*   Adjusting_Widgets_Visibility(false);
                                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                                // Adapter_Pos_Sales_Report adapater_pos_sales_report = new Adapter_Pos_Sales_Report(WholeSaleOrderSalesReport.this, Order_Item_List, OrderItem_hashmap, tmcSubCtgykey,SubCtgyKey_List);
                                // WholeSaleOrderReport_Listview.setAdapter(adapater_pos_sales_report);
                                //sort_the_array_CtgyWise();

                                prepareContent();
                                setAdapter();

                              */
                                isOrderDetailsResponseReceivedForSelectedDate = true;
                                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                                prepareContent();
                                setAdapter();

                            }

                            else{
                                 Order_Item_List.clear();
                                 array_of_orderId.clear();
                                OrderItem_hashmap.clear();
                                finalBillDetails.clear();
                                FinalBill_hashmap.clear();
                                wholeSaleOrderpaymentModeArray.clear();
                                wholeSaleOrderpaymentModeHashmap.clear();
                                tmcSubCtgywise_sorted_hashmap.clear();

                                SubCtgywiseTotalArray.clear();
                                tmcSubCtgykey.clear();
                                SubCtgywiseTotalHashmap.clear();



                                dataList.clear();
                                adapter.notifyDataSetChanged();
                                ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);

                                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                                Toast.makeText(WholeSaleOrderSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                                Adjusting_Widgets_Visibility(false);

                            }
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
                    try {
                        try {
                            Collections.sort(Order_Item_List, new Comparator<String>() {
                                public int compare(final String object1, final String object2) {
                                    return object1.compareTo(object2);
                                }
                            });
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                        addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                        prepareContent();
                        setAdapter();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(WholeSaleOrderSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);
                     Order_Item_List.clear();
        array_of_orderId.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    wholeSaleOrderpaymentModeArray.clear();
                    wholeSaleOrderpaymentModeHashmap.clear();
                    tmcSubCtgywise_sorted_hashmap.clear();
                  
                    SubCtgywiseTotalArray.clear();
                    tmcSubCtgykey.clear();
                    SubCtgywiseTotalHashmap.clear();
                    dataList.clear();
                    ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);
                    adapter.notifyDataSetChanged();
                    addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);

                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                    error.printStackTrace();
                }
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
        Volley.newRequestQueue(WholeSaleOrderSalesReport.this).add(jsonObjectRequest);

    }

    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(WholeSaleOrderSalesReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {



                            isVendorOrdersTableServiceCalled = false;

                            scrollView.setVisibility(View.GONE);
                            instruction_textview.setVisibility(View.VISIBLE);
                            instruction_textview.setText("After Selecting the Date !! Click Fetch Data");
                            Order_Item_List.clear();
                            array_of_orderId.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            wholeSaleOrderpaymentModeArray.clear();
                            wholeSaleOrderpaymentModeHashmap.clear();
                            tmcSubCtgywise_sorted_hashmap.clear();

                            SubCtgywiseTotalArray.clear();
                            tmcSubCtgykey.clear();
                            SubCtgywiseTotalHashmap.clear();
                            CouponDiscount =0;


                            dataList.clear();
                            adapter.notifyDataSetChanged();
                            ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);

                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            Adjusting_Widgets_Visibility(false);


                            String month_in_String = getMonthString(monthOfYear);
                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay =   getDayString(dayOfWeek);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            isgetOrderForSelectedDateCalled = false;
                          
                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                           // getOrderForSelectedDate(DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();
    }
    private void getItemDetailsFromItemDespArray(Modal_OrderDetails modal_orderDetailsfromResponse, String paymentMode, String ordertype) {
        //   DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String newOrderWeightInGrams = "",tmcprice_string="";
        double newweight,gstAmount;

        try {
            JSONArray jsonArray = modal_orderDetailsfromResponse.getItemdesp();

            for(int i=0; i < jsonArray.length(); i++) {
                //Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                JSONObject json = jsonArray.getJSONObject(i);
                //Log.d(Constants.TAG, "this json" +json.toString());
                boolean isItemFoundinMenu = false;

                Modal_OrderDetails modal_orderDetails_ItemDesp = new Modal_OrderDetails();

                if(json.has("menuitemid")) {
                    modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));
                    String menuitemidd = String.valueOf(json.get("menuitemid"));
                    try {
                        newOrderWeightInGrams = String.valueOf(json.get("weightingrams"));
                    }
                    catch (Exception e){
                        try {
                            newOrderWeightInGrams = String.valueOf(json.get("grossweightingrams"));
                        }
                        catch (Exception e1){

                            e1.printStackTrace();

                            try {
                                newOrderWeightInGrams = String.valueOf(json.get("grossweight"));
                            }
                            catch (Exception e2){

                                e2.printStackTrace();
                            }

                        }

                        e.printStackTrace();
                    }
                    String ItemName = "";
                    if(json.has("itemname")){
                        ItemName = String.valueOf(json.get("itemname"));
                    }



                    if(!newOrderWeightInGrams.contains("Pcs")&&(!(newOrderWeightInGrams.contains("Unit")))&&(!(newOrderWeightInGrams.contains("Kg")))&&(!(newOrderWeightInGrams.contains("kg")))&&(!(newOrderWeightInGrams.contains("pcs")))&&(!(newOrderWeightInGrams.contains("pc")))&&(!(newOrderWeightInGrams.contains("Set")))&&(!(newOrderWeightInGrams.contains("set")))) {
                        newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                    }
                    else{
                        newOrderWeightInGrams ="";
                    }

                    try {
                        for (int menuiterator = 0; menuiterator < MenuItem.size(); menuiterator++) {
                            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(menuiterator);

                            String menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());

                            if(menuitemidd.equals("")){
                                String ItemNamefromMenu = String.valueOf(modal_menuItemSettings.getItemname().toString());
                                if(ItemName.equals(ItemNamefromMenu)){
                                    menuitemidd=menuItemId;
                                    modal_orderDetails_ItemDesp.menuitemid=menuitemidd;
                                }
                            }
                            String reportname = String.valueOf(modal_menuItemSettings.getReportname());

                            if (menuItemId.equals(menuitemidd)) {
                                isItemFoundinMenu =true;

                                if ((!reportname.equals(""))&&(!reportname.equals("null"))&&(!reportname.equals("\r"))) {
                                    modal_orderDetails_ItemDesp.itemname = String.valueOf(reportname);
                                    //  itemname = String.valueOf(reportname);
                                }
                                else{
                                    modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                                    //   itemname = String.valueOf(json.get("itemname"));
                                }

                            }

                        }
                        if(!isItemFoundinMenu){
                            modal_orderDetails_ItemDesp.pricetypeforpos = String.valueOf("tmcprice");

                            modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                            // itemname = String.valueOf(json.get("itemname"));

                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    //modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                    modal_orderDetails_ItemDesp.ordertype = modal_orderDetailsfromResponse.getOrdertype();
                    modal_orderDetails_ItemDesp.paymentmode = modal_orderDetailsfromResponse.getPaymentmode();
                    String subCtgyKey = "";
                    try {
                        if (json.has("tmcsubctgykey")) {
                            subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                            if (subCtgyKey.equals("") || subCtgyKey.equals("0")) {
                                modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                subCtgyKey = String.valueOf("Miscellaneous");
                            } else {
                                modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                                subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));

                            }

                        } else {
                            modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                            subCtgyKey = String.valueOf("Miscellaneous");
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();

                        modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                        subCtgyKey = String.valueOf("Miscellaneous");


                    }
                    double tmcprice = Double.parseDouble(String.valueOf(json.get("tmcprice")));
                    int quantity = Integer.parseInt(String.valueOf(json.get("quantity")));
                    try {
                        newweight = Double.parseDouble(String.valueOf(newOrderWeightInGrams));

                    }
                    catch (Exception e){
                        newweight = 0;


                    }
                    try{
                        newweight = newweight*quantity;
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                    try {
                        gstAmount = Double.parseDouble(String.valueOf(json.get("gstamount")));

                    }
                    catch (Exception e){
                        gstAmount = 0;


                    }

                    try{
                        tmcprice = quantity*tmcprice;

                        tmcprice_string = String.valueOf(((tmcprice)));


                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    try {
                        if(SubCtgywiseTotalArray.contains(subCtgyKey)) {
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(subCtgyKey);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                String  SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(subCtgyKey);
                                double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                SubCtgywisetotalDouble = SubCtgywisetotalDouble+tmcprice;
                                SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    SubCtgywiseTotalHashmap.replace(subCtgyKey,SubCtgywisetotalString);
                                }
                                else{
                                    SubCtgywiseTotalHashmap.remove(subCtgyKey);
                                    SubCtgywiseTotalHashmap.put(subCtgyKey,SubCtgywisetotalString);
                                }
                            }
                            else{
                                SubCtgywiseTotalHashmap.put(subCtgyKey,tmcprice_string);

                            }
                        }
                        else{
                            SubCtgywiseTotalArray.add(subCtgyKey);
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(subCtgyKey);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                String  SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(subCtgyKey);
                                double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                SubCtgywisetotalDouble = SubCtgywisetotalDouble+tmcprice;
                                SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    SubCtgywiseTotalHashmap.replace(subCtgyKey,SubCtgywisetotalString);
                                }
                                else{
                                    SubCtgywiseTotalHashmap.remove(subCtgyKey);
                                    SubCtgywiseTotalHashmap.put(subCtgyKey,SubCtgywisetotalString);
                                }
                            }
                            else{
                                SubCtgywiseTotalHashmap.put(subCtgyKey,tmcprice_string);

                            }
                        }


                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }




                    String gstAmount_string = String.valueOf(((json.get("gstamount"))));

                    modal_orderDetails_ItemDesp.tmcprice = String.valueOf(tmcprice_string);
                    modal_orderDetails_ItemDesp.quantity = String.valueOf(json.get("quantity"));
                    modal_orderDetails_ItemDesp.gstamount = String.valueOf(gstAmount_string);
                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newweight);

                    if(ordertype.equals(Constants.WholeSaleOrder)) {
                        if (wholeSaleOrderpaymentModeArray.contains(paymentMode)) {
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);

                            } catch (Exception e) {
                                e.printStackTrace();
                                
                            }
                            if (isAlreadyAvailabe) {
                                Modal_OrderDetails modal_orderDetails = wholeSaleOrderpaymentModeHashmap.get(paymentMode);
                                if (paymentMode.equals(Constants.CASH_ON_DELIVERY) || paymentMode.equals(Constants.CASH)) {
                                    double cash_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCashOndeliverySales());
                                    double cash_amount = tmcprice + cash_amount_fromhashmap;
                                    double newTotalCashAmount = cash_amount + gstAmount;
                                    modal_orderDetails.setCashOndeliverySales(String.valueOf((newTotalCashAmount)));


                                }
                                if (paymentMode.equals(Constants.CARD) || paymentMode.equals(Constants.Card)) {
                                    double card_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCardSales());
                                    //Log.d(Constants.TAG, "This orders payment mode tmcprice Item desp: " +tmcprice );

                                    double card_amount = tmcprice + card_amount_fromhashmap;
                                    double newTotalcardAmount = card_amount + gstAmount;
                                    modal_orderDetails.setCardSales(String.valueOf((newTotalcardAmount)));


                                }
                                if (paymentMode.equals(Constants.UPI) || paymentMode.equals(Constants.Upi)) {
                                    double upi_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getUpiSales());
                                    double upi_amount = tmcprice + upi_amount_fromhashmap;
                                    double newTotalupiAmount = upi_amount + gstAmount;
                                    modal_orderDetails.setUpiSales(String.valueOf((newTotalupiAmount)));


                                }
                                if (paymentMode.equals(Constants.CREDIT) || paymentMode.equals(Constants.Credit)) {
                                    double credit_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCreditSales());
                                    double credit_amount = tmcprice + credit_amount_fromhashmap;
                                    double newTotalcreditAmount = credit_amount + gstAmount;
                                    modal_orderDetails.setCreditSales(String.valueOf((newTotalcreditAmount)));


                                }



                            } else {
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                if (paymentMode.equals(Constants.CASH_ON_DELIVERY) || paymentMode.equals(Constants.CASH)) {
                                    double cash_amount = tmcprice;
                                    double Gst_cash_amount = gstAmount;
                                    double newTotalCashAmount = cash_amount + Gst_cash_amount;

                                    modal_orderDetails.setCashOndeliverySales(String.valueOf((newTotalCashAmount)));


                                }
                                if (paymentMode.equals(Constants.CARD) || paymentMode.equals(Constants.Card)) {
                                    double card_amount = tmcprice;
                                    double Gst_card_amount = gstAmount;
                                    double newTotalcardAmount = card_amount + Gst_card_amount;
                                    //Log.d(Constants.TAG, "This orders payment mode tmcprice Item desp: " +tmcprice );

                                    modal_orderDetails.setCardSales(String.valueOf((newTotalcardAmount)));


                                }
                                if (paymentMode.equals(Constants.UPI) || paymentMode.equals(Constants.Upi)) {
                                    double upi_amount = tmcprice;
                                    double Gst_upi_amount = gstAmount;
                                    double newTotalupiAmount = upi_amount + Gst_upi_amount;

                                    modal_orderDetails.setUpiSales(String.valueOf((newTotalupiAmount)));


                                }
                                if (paymentMode.equals(Constants.CREDIT) || paymentMode.equals(Constants.Credit)) {
                                    double credit_amount = tmcprice;
                                    double Gst_credit_amount = gstAmount;
                                    double newTotalcreditAmount = credit_amount + Gst_credit_amount;

                                    modal_orderDetails.setCreditSales(String.valueOf((newTotalcreditAmount)));


                                }


                                wholeSaleOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                            }
                        } else {
                            wholeSaleOrderpaymentModeArray.add(paymentMode);
                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                            if (paymentMode.equals(Constants.CASH_ON_DELIVERY) || paymentMode.equals(Constants.CASH)) {


                                double cash_amount = tmcprice;
                                double Gst_cash_amount = gstAmount;

                                double newTotalCashAmount = cash_amount + Gst_cash_amount;
                                modal_orderDetails.setCashOndeliverySales(String.valueOf((newTotalCashAmount)));


                            }
                            if (paymentMode.equals(Constants.CARD) || paymentMode.equals(Constants.Card)) {
                                double newTotalcardAmount = tmcprice + gstAmount;
                                //Log.d(Constants.TAG, "This orders payment mode tmcprice Item desp: " +tmcprice );

                                modal_orderDetails.setCardSales(String.valueOf((newTotalcardAmount)));


                            }
                            if (paymentMode.equals(Constants.UPI) || paymentMode.equals(Constants.Upi)) {
                                double newTotalupiAmount = tmcprice + gstAmount;

                                modal_orderDetails.setUpiSales(String.valueOf((newTotalupiAmount)));


                            }
                            if (paymentMode.equals(Constants.CREDIT) || paymentMode.equals(Constants.Credit)) {
                                double newTotalcreditAmount = tmcprice + gstAmount;

                                modal_orderDetails.setCreditSales(String.valueOf((newTotalcreditAmount)));


                            }




                            wholeSaleOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                        }
                    }


                    String menuitemid = String.valueOf(menuitemidd);
                




                    if (Order_Item_List.contains(menuitemid)) {
                        boolean isItemAlreadyOrdered = false;
                        try{
                            isItemAlreadyOrdered = checkIfMenuItemisAlreadyAvailableInArray(menuitemid);

                        }catch(Exception e ){
                            e.printStackTrace();;
                        }
                        if (isItemAlreadyOrdered) {

                            //  calculateSubCtgywiseTotal(modal_orderDetails_ItemDesp);

                            Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(menuitemid);
                            double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getTmcprice());
                            int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                            double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());

                            String oldOrder_WeightInGrams = modal_orderDetails_itemDespfrom_hashMap.getWeightingrams();
                            if(oldOrder_WeightInGrams.equals("")){
                                oldOrder_WeightInGrams="0";
                            }
                            double doubleoldOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                            int intOldOrder_WeightInGrams = (int) Math.round(doubleoldOrder_WeightInGrams);

                            int intNewOrder_WeightInGrams = (int) Math.round(newweight);

                            intOldOrder_WeightInGrams = intOldOrder_WeightInGrams +intNewOrder_WeightInGrams;
                            tmcprice = tmcprice + tmcprice_from_HashMap;
                            quantity = quantity + quantity_from_HashMap;
                            gstAmount = gstAmount + gstAmount_from_HashMap;

                            modal_orderDetails_itemDespfrom_hashMap.setWeightingrams(String.valueOf((intOldOrder_WeightInGrams)));
                            modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                            modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf((tmcprice)));
                            modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount)));


                        } else {
                            //  calculateSubCtgywiseTotal(modal_orderDetails_ItemDesp);
                            OrderItem_hashmap.put(menuitemid, modal_orderDetails_ItemDesp);

                        }
                    } else {
                        try {
                            Order_Item_List.add(menuitemid);
                            if (!tmcSubCtgykey.contains(subCtgyKey)) {
                                tmcSubCtgykey.add(subCtgyKey);
                            }
                            boolean isItemAlreadyOrdered = false;
                            try{
                                isItemAlreadyOrdered = checkIfMenuItemisAlreadyAvailableInArray(menuitemid);

                            }catch(Exception e ){
                                e.printStackTrace();;
                            }
                            if (isItemAlreadyOrdered) {
                                // calculateSubCtgywiseTotal(modal_orderDetails_ItemDesp);

                                Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(menuitemid);
                                double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getTmcprice());
                                int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                                double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());

                                tmcprice = tmcprice + tmcprice_from_HashMap;
                                quantity = quantity + quantity_from_HashMap;
                                gstAmount = gstAmount + gstAmount_from_HashMap;

                                String oldOrder_WeightInGrams = modal_orderDetails_itemDespfrom_hashMap.getWeightingrams();
                                if (oldOrder_WeightInGrams.equals("")) {
                                    oldOrder_WeightInGrams = "0";
                                }
                                double doubleoldOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                                int intOldOrder_WeightInGrams = (int) Math.round(doubleoldOrder_WeightInGrams);

                                int intNewOrder_WeightInGrams = (int) Math.round(newweight);

                                intOldOrder_WeightInGrams = intOldOrder_WeightInGrams + intNewOrder_WeightInGrams;
                                //Log.d(Constants.TAG, "this json pre 3 " + String.valueOf(oldOrder_WeightInGrams));


                                modal_orderDetails_itemDespfrom_hashMap.setWeightingrams(String.valueOf((intOldOrder_WeightInGrams)));

                                modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                                modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf((tmcprice)));
                                modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount)));


                            } else {
                                //calculateSubCtgywiseTotal(modal_orderDetails_ItemDesp);

                                OrderItem_hashmap.put(menuitemid, modal_orderDetails_ItemDesp);
                            }
                        }catch (Exception e ){
                            e.printStackTrace();
                        }
                    }


                    try{
                        if(OrderItem_hashmap.size()>1){
                            try{
                                OrderItem_hashmap = sortByComparator(OrderItem_hashmap);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                    //  add_amount_ForBillDetails(OrderdItems_desp);
                }
                else{
                    //Log.d(Constants.TAG, "this order have no menuitemId " + String.valueOf(json.get("itemname")));
                    Adjusting_Widgets_Visibility(false);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
            }
            catch(Exception e){
                e.printStackTrace();
            }





            for (String SubCtgykey : tmcSubCtgykey) {
                int i_value = 0;
                String subCtgyTotal ="0";
                double subCtgyTotaldouble = 0;
                String SubCtgyName = "", ItemName_Quantity_weight, Price, menuid;
                Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
                try{
                    subCtgyTotal = SubCtgywiseTotalHashmap.get(SubCtgykey);
                    subCtgyTotaldouble = Double.parseDouble(subCtgyTotal);
                    subCtgyTotal = decimalFormat.format(subCtgyTotaldouble);
                }
                catch (Exception e){
                    subCtgyTotal= "0";
                    e.printStackTrace();
                }
                try {
                    SubCtgyName = Objects.requireNonNull(subCtgyName_object).getTmcsubctgyname();
                } catch (Exception e) {
                    SubCtgyName = "";
                    //Log.d(Constants.TAG, "before for " + e.getMessage());

                }



                try {
                    Order_Item_List = getSortedIdFromHashMap(Order_Item_List,OrderItem_hashmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }




                for (int j = 0; j < Order_Item_List.size(); j++) {
                    menuid = Order_Item_List.get(j);
                    Modal_OrderDetails itemDetailsfromHashmap = OrderItem_hashmap.get(menuid);
                    String subCtgyKey_fromHashmap = itemDetailsfromHashmap.getTmcsubctgykey();
                    if (i_value != 0) {
                        if (subCtgyKey_fromHashmap.equals(SubCtgykey)) {
                            double weightinGrams = 0;
                        
                            try {
                                if ((!(itemDetailsfromHashmap.getWeightingrams().equals(""))) && ((itemDetailsfromHashmap.getWeightingrams() != (null)))) {
                                    weightinGrams = Double.parseDouble(Objects.requireNonNull(itemDetailsfromHashmap).getWeightingrams());

                                }

                            } catch (Exception e) {
                                weightinGrams = 0;
                            }
                            String itemname = String.valueOf(itemDetailsfromHashmap.getItemname());

                            if(itemname.equals("Fresh Goat Meat - Curry Cut")){
                                Log.i("TAG", "Key : "+ String.valueOf(itemDetailsfromHashmap.getMenuitemid()));
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



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        Adjusting_Widgets_Visibility(false);

        try {
            adapter = new Adapter_Pos_Sales_Report(WholeSaleOrderSalesReport.this, dataList,false);
            WholeSaleOrderReport_Listview.setAdapter(adapter);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {

            ReportListviewSizeHelper.getListViewSize(WholeSaleOrderReport_Listview, screenInches);
            scrollView.fullScroll(View.FOCUS_UP);
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }





    private void addOrderedItemAmountDetails(List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {

        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        try {

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            double totalAmountWithOutGstwithoutDiscount = 0;
            double totalAmountWithGstwithoutDiscount_without_refund_replacement =0;
            double totalAmountWithOutGstwithoutDiscount_refund_replacement =0;
            double totalAmountWithOutGst = 0;
            double discountAmount = 0;
            double GST = 0;
            double totalAmount = 0;

            double creditPayment_Amount = 0;
            double cardPayment_Amount = 0;
            double upiPayment_Amount = 0;
            double cashPayment_Amount = 0;
            double cardDiscount_Amount = 0;
            double upiDiscount_Amount = 0;
            double cashDiscount_Amount = 0;
            double creditDiscount_Amount = 0;

            double phoneOrder_creditPayment_Amount = 0;
            double phoneOrder_cardPayment_Amount = 0;
            double phoneOrder_upiPayment_Amount = 0;
            double phoneOrder_cashPayment_Amount = 0;
            double phoneOrder_cardDiscount_Amount = 0;
            double phoneOrder_upiDiscount_Amount = 0;
            double phoneOrder_cashDiscount_Amount = 0;
            double phoneOrder_creditDiscount_Amount = 0;


            double swiggyOrder_Payment_Amount = 0;
            double swiggyOrder_Discount_Amount = 0;


            double dunzoOrder_Payment_Amount = 0;
            double dunzoOrder_Discount_Amount = 0;

            double bigBasketOrder_Payment_Amount = 0;
            double bigBasketOrder_Discount_Amount = 0;


            double totalRefundAmount = 0;
            double totalReplacementAmount = 0;

            appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));



            String TMCsubCtgyKey = "",subCtgyTotal="";
            for (int i = 0; i < order_item_list.size(); i++) {
                String menuItemId = order_item_list.get(i);
                Modal_OrderDetails modal_orderDetails_amountDetails = orderItem_hashmap.get(menuItemId);
                double totalAmountWithOutGst_from_array = Double.parseDouble(modal_orderDetails_amountDetails.getTmcprice());
                totalAmountWithOutGst = totalAmountWithOutGst + totalAmountWithOutGst_from_array;

                double Gst_from_array = Double.parseDouble(modal_orderDetails_amountDetails.getGstamount());
                GST = GST + Gst_from_array;

                totalAmount = totalAmountWithOutGst + GST;


            }
            for(String paymentmode :wholeSaleOrderpaymentModeArray){
                Modal_OrderDetails modal_orderDetails = wholeSaleOrderpaymentModeHashmap.get(paymentmode);
                Modal_OrderDetails Payment_Modewise_discount = wholeSaleOrderpaymentMode_DiscountHashmap.get(paymentmode);


                if ((paymentmode.toUpperCase().equals("CASH ON DELIVERY")) || (paymentmode.toUpperCase().equals("CASH"))) {
                    cashPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    cashDiscount_Amount = Double.parseDouble(discount_String);
                    cashPayment_Amount = cashPayment_Amount-cashDiscount_Amount;


                }
                if ((paymentmode.toUpperCase().equals("CARD"))) {
                    cardPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCardSales());
                    //Log.d(Constants.TAG, "This orders payment mode tmcprice: " +cardPayment_Amount);

                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    cardDiscount_Amount = Double.parseDouble(discount_String);
                    cardPayment_Amount = cardPayment_Amount-cardDiscount_Amount;
                    //Log.d(Constants.TAG, "This orders payment mode tmcprice 1 : " +cardPayment_Amount);

                }
                if ((paymentmode.toUpperCase().equals("UPI"))) {
                    upiPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getUpiSales());
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    upiDiscount_Amount = Double.parseDouble(discount_String);
                    upiPayment_Amount = upiPayment_Amount-upiDiscount_Amount;
                }
                if ((paymentmode.toUpperCase().equals("CREDIT"))) {
                    creditPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales());
                    String discount_String = "";
                    try{
                        discount_String =   String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    }
                    catch (Exception e){
                        discount_String ="0";
                        e.printStackTrace();
                    }

                    creditDiscount_Amount = Double.parseDouble(discount_String);
                    creditPayment_Amount = creditPayment_Amount-creditDiscount_Amount;
                }

            }

            Log.d(Constants.TAG, "This orders payment mode tmcprice: " +totalAmount);

            try {
                discountAmount = cardDiscount_Amount+cashDiscount_Amount+creditDiscount_Amount+upiDiscount_Amount+phoneOrder_cardDiscount_Amount+phoneOrder_cashDiscount_Amount+phoneOrder_upiDiscount_Amount+phoneOrder_creditDiscount_Amount+swiggyOrder_Discount_Amount+dunzoOrder_Discount_Amount+bigBasketOrder_Discount_Amount;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                totalAmountWithOutGstwithoutDiscount = totalAmountWithOutGst-discountAmount;
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                totalAmountWithOutGstwithoutDiscount_refund_replacement = totalAmountWithOutGstwithoutDiscount - totalRefundAmount - totalReplacementAmount;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                totalAmountWithGstwithoutDiscount_without_refund_replacement = totalAmountWithOutGstwithoutDiscount_refund_replacement+GST;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                cashPayment_Amount = cashPayment_Amount- (totalReplacementAmount+totalRefundAmount);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
             
                totalAmt_without_GST.setText(String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
                totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format(discountAmount)));
                totalAmt_with_CouponDiscount.setText(String.valueOf(decimalFormat.format(totalAmountWithGstwithoutDiscount_without_refund_replacement)));
                totalGST_Amt.setText(String.valueOf(decimalFormat.format(GST)));
                final_sales.setText(String.valueOf(decimalFormat.format(totalAmountWithGstwithoutDiscount_without_refund_replacement)));
                upiSales.setText(String.valueOf(decimalFormat.format(upiPayment_Amount)));
                cashSales.setText(String.valueOf(decimalFormat.format(cashPayment_Amount)));
                cardSales.setText(String.valueOf(decimalFormat.format(cardPayment_Amount)));
                creditSales.setText(String.valueOf(decimalFormat.format(creditPayment_Amount)));

                totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmountWithGstwithoutDiscount_without_refund_replacement)));

               
                finalBillDetails.add("TOTAL : ");
                FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
              /*  finalBillDetails.add("DISCOUNT : ");
                FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(decimalFormat.format(discountAmount)));
                finalBillDetails.add("REFUND : ");
                FinalBill_hashmap.put("REFUND : ", String.valueOf(decimalFormat.format(totalRefundAmount)));
                finalBillDetails.add("REPLACEMENT : ");
                FinalBill_hashmap.put("REPLACEMENT : ", String.valueOf(decimalFormat.format(totalReplacementAmount)));

             */
                finalBillDetails.add("SUBTOTAL : ");
                FinalBill_hashmap.put("SUBTOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGstwithoutDiscount)));
                finalBillDetails.add("GST : ");
                FinalBill_hashmap.put("GST : ", String.valueOf(decimalFormat.format(GST)));
                finalBillDetails.add("FINAL SALES : ");
                FinalBill_hashmap.put("FINAL SALES : ", String.valueOf(decimalFormat.format(totalAmountWithGstwithoutDiscount_without_refund_replacement)));
                //   sort_list_tmcSubCtgyWise(Order_Item_List, OrderItem_hashmap);

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        catch ( Exception e){
            e.printStackTrace();
        }


            }


    @Override
    protected void onStart() {
        super.onStart();
        getTmcSubCtgyList(vendorKey);

    }

    private void getTmcSubCtgyList(String vendorKey) {


        String Api_to_Call = "";
        if(Constants.isNewSbCtgyTable_APIUsed.equals(Constants.YES)) {
            Api_to_Call = Constants.api_getListofTMCTileForVendorkey+vendorKey;
        }
        else{
            Api_to_Call = Constants.api_getListofSubCtgy;

        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Api_to_Call,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            //Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

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
                                    SubCtgyKey_hashmap.put(subCtgyKey,modal_orderDetails);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                String subCtgyKey  = String.valueOf("Miscellaneous");
                                //Log.d(Constants.TAG, "subCtgyKey: " + subCtgyKey);
                                String subCtgyName = String.valueOf("Miscellaneous Item");

                                modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                modal_orderDetails.tmcsubctgyname = subCtgyName;
                                //  tmcSubCtgykey.add(subCtgyKey);
                                SubCtgyKey_hashmap.put(subCtgyKey,modal_orderDetails);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        })
        {
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


    private void printUsingUSBPrinterReport() {

        UsbConnection usbConnection = UsbPrintersConnectionsLocal.selectFirstConnected(WholeSaleOrderSalesReport.this);
        UsbManager usbManager = (UsbManager) WholeSaleOrderSalesReport.this.getSystemService(Context.USB_SERVICE);
        if (usbConnection == null || usbManager == null) {
            Adjusting_Widgets_Visibility(false);

          /*  new AlertDialog.Builder(AddSwiggyOrders.this)
                    .setTitle("USB Connection")
                    .setMessage("No USB printer found.")
                    .show();

           */

            new TMCAlertDialogClass(WholeSaleOrderSalesReport.this, R.string.app_name, R.string.ReConnect_Instruction,
                    R.string.OK_Text, R.string.Cancel_Text,
                    new TMCAlertDialogClass.AlertListener() {
                        @Override
                        public void onYes() {
                            printUsingUSBPrinterReport();
                            return;
                        }

                        @Override
                        public void onNo() {
                            Toast.makeText(WholeSaleOrderSalesReport.this, "Can't Find USB Printer", Toast.LENGTH_SHORT).show();
                            Adjusting_Widgets_Visibility(false);

                        }
                    });
            return;
        }

        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                WholeSaleOrderSalesReport.this,
                0,
                new Intent(ACTION_USB_PERMISSION),
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addCategory("WholeSale Sales Report");
        registerReceiver(usbReceiver_WholeSaleReport, filter);
        usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);





    }


    private final BroadcastReceiver usbReceiver_WholeSaleReport = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Set<String> category = intent.getCategories();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (WholeSaleOrderSalesReport.this) {
                    UsbManager usbManager = (UsbManager) WholeSaleOrderSalesReport.this.getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context, new AsyncEscPosPrint.OnPrintFinished() {
                                @Override
                                public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                    Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                    try{
                                        unregisterReceiver(usbReceiver_WholeSaleReport);
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

                                    Toast.makeText(WholeSaleOrderSalesReport.this, "Error in AsyncPOS USB Printer", Toast.LENGTH_SHORT).show();
                                    Adjusting_Widgets_Visibility(false);

                                }

                                @Override
                                public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                    Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");

                                    try{
                                        unregisterReceiver(usbReceiver_WholeSaleReport);
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


                                    Toast.makeText(WholeSaleOrderSalesReport.this, "Printed Succesfully", Toast.LENGTH_SHORT).show();
                                    Adjusting_Widgets_Visibility(false);



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

    public AsyncEscPosPrinter getAsyncEscPosPrinterNewItem(DeviceConnection printerConnection) {
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
     //   text_to_Print = "[c]<b><font size='big'>The Meat Chop</b>\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Fresh Meat and Seafood \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine1 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine2 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Postal Code :" + StoreAddressLine3 + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Contact No :" + StoreLanLine + " \n";
        text_to_Print = text_to_Print + "[c] <font size='normal'>" + GSTIN + " \n"+ " \n";
        text_to_Print = text_to_Print + "[L] ----------------------------------------------" + " \n";
        text_to_Print = text_to_Print + "[L] <font size='tall'>Report Name : WholeSale Sales Report " + " \n";
        text_to_Print = text_to_Print + "[L] <font size='normal'>Current Date : " + DateString + " \n";
        text_to_Print = text_to_Print + "[L] ----------------------------------------------" + " \n";
        for (int subCtgyKeyCount = 0; subCtgyKeyCount < tmcSubCtgykey.size(); subCtgyKeyCount++) {
            String SubCtgyName, menuid, SubCtgykey;

            SubCtgykey = tmcSubCtgykey.get(subCtgyKeyCount);
            Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
            SubCtgyName = subCtgyName_object.getTmcsubctgyname();
            text_to_Print = text_to_Print +"\n"+ "[L] <font size='wide'>" + SubCtgyName + "\n"+ "\n";

            for (int i = 0; i < Order_Item_List.size(); i++) {
                menuid = Order_Item_List.get(i);
                Modal_OrderDetails itemRow = OrderItem_hashmap.get(menuid);


                String subCtgyKey_fromHashmap = itemRow.getTmcsubctgykey();
                if (subCtgyKey_fromHashmap.equals(SubCtgykey)) {
                    String itemName = (itemRow.getItemname());

                    int indexofbraces = itemName.indexOf("(");
                    if (indexofbraces >= 0) {
                        itemName = (itemName.substring(0, indexofbraces));

                    }
                    if (itemName.length() > 19) {
                        itemName = (itemName.substring(0, 19));
                        itemName = itemName + "..";
                    }


                    String KilogramString = "", Quantity = "", itemPrice = "",itemName_weight ="";
                    try {
                        KilogramString = itemRow.getWeightingrams();
                        if (KilogramString != null && (!KilogramString.equals("")) && (!(KilogramString.equals("0.00Kg"))) && (!(KilogramString.equals("0")))) {
                            Quantity = KilogramString + "g";
                        } else {
                            Quantity = itemRow.getQuantity() + "pc";
                        }
                    } catch (Exception e) {
                        Quantity = itemRow.getQuantity() + "pc";

                    }
                    try {
                        itemPrice = String.valueOf(itemRow.getTmcprice());

                    } catch (Exception e) {
                        e.printStackTrace();
                        itemPrice = "";
                    }
                    try {
                        itemName_weight = itemName + "-" + Quantity;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                 /*   if (itemName_weight.length() == 10) {
                        //16spaces
                        itemName_weight = itemName_weight + "                  ";
                    }
                    if (itemName_weight.length() == 11) {
                        //15spaces
                        itemName_weight = itemName_weight + "                 ";
                    }
                    if (itemName_weight.length() == 12) {
                        //14spaces
                        itemName_weight = itemName_weight + "                ";
                    }
                    if (itemName_weight.length() == 13) {
                        //13spaces
                        itemName_weight = itemName_weight + "               ";
                    }
                    if (itemName_weight.length() == 14) {
                        //12spaces
                        itemName_weight = itemName_weight + "              ";
                    }
                    if (itemName_weight.length() == 15) {
                        //11spaces
                        itemName_weight = itemName_weight + "             ";
                    }
                    if (itemName_weight.length() == 16) {
                        //10spaces
                        itemName_weight = itemName_weight + "            ";
                    }
                    if (itemName_weight.length() == 17) {
                        //9spaces
                        itemName_weight = itemName_weight + "           ";
                    }
                    if (itemName_weight.length() == 18) {
                        //8spaces
                        itemName_weight = itemName_weight + "          ";
                    }
                    if (itemName_weight.length() == 19) {
                        //7spaces
                        itemName_weight = itemName_weight + "         ";
                    }
                    if (itemName_weight.length() == 20) {
                        //6spaces
                        itemName_weight = itemName_weight + "        ";
                    }
                    if (itemName_weight.length() == 21) {
                        //5spaces
                        itemName_weight = itemName_weight + "       ";
                    }
                    if (itemName_weight.length() == 22) {
                        //4spaces
                        itemName_weight = itemName_weight + "      ";
                    }
                    if (itemName_weight.length() == 23) {
                        //3spaces
                        itemName_weight = itemName_weight + "     ";
                    }
                    if (itemName_weight.length() == 24) {
                        //2spaces
                        itemName_weight = itemName_weight + "    ";
                    }
                    if (itemName_weight.length() == 25) {
                        //1spaces
                        itemName_weight = itemName_weight + "   ";
                    }
                    if (itemName_weight.length() == 26) {
                        //0spaces
                        itemName_weight = itemName_weight + "  ";
                    }
                    if (itemName_weight.length() == 27) {
                        //0spaces
                        itemName_weight = itemName_weight + " ";
                    }
                    if (itemName_weight.length() == 28) {
                        //0spaces
                        itemName_weight = itemName_weight + "";
                    }
                    if (itemName_weight.length() == 29) {
                        //0spaces
                        itemName_weight = itemName_weight + "";
                    }
                    if (itemName_weight.length() == 30) {
                        //0spaces
                        itemName_weight = itemName_weight + "";
                    }



                    itemPrice = "Rs. "+itemPrice;
                    if (itemPrice.length() == 8) {
                        //4spaces
                        itemPrice = "     " + itemPrice;
                    }
                    if (itemPrice.length() == 9) {
                        //3spaces
                        itemPrice = "    " + itemPrice;
                    }
                    if (itemPrice.length() == 10) {
                        //2spaces
                        itemPrice = "   " + itemPrice;
                    }
                    if (itemPrice.length() == 11) {
                        //1spaces
                        itemPrice = "  " + itemPrice;
                    }
                    if (itemPrice.length() == 12) {
                        //0spaces
                        itemPrice = " " + itemPrice;
                    }
                    if (itemPrice.length() == 13) {
                        //0spaces
                        itemPrice = "" + itemPrice;
                    }
                    if (itemPrice.length() == 14) {
                        //no space
                        itemPrice = "" + itemPrice;
                    }


                  */
                    text_to_Print = text_to_Print + "[L]<font size='normal'>  " + itemName_weight +"[R]"+ itemPrice +" \n"+" \n";


                //    text_to_Print = text_to_Print + "[L]<font size='normal'>" + itemName_weight + itemPrice +" \n"+" \n";


                }
            }
        }
        text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";

        text_to_Print = text_to_Print + "[L] <font size='wide'>" + " Final Sales Break Up" + " \n";
        text_to_Print = text_to_Print + "[L]<font size='normal'> ----------------------------------------------" + "\n";
        for (int i = 0; i < wholeSaleOrderpaymentModeArray.size(); i++) {
            double payment_AmountDouble = 0;
            double payment_AmountDiscDouble = 0;

            String Payment_Amount = "", key = wholeSaleOrderpaymentModeArray.get(i);
            Modal_OrderDetails modal_orderDetails = wholeSaleOrderpaymentModeHashmap.get(key);
            Modal_OrderDetails Payment_Modewise_discount = wholeSaleOrderpaymentMode_DiscountHashmap.get(key);

            //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");



            if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {
                try {
                    payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    payment_AmountDiscDouble = Double.parseDouble(discount_String);
                    payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble ;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Cash Sales";
                } catch (Exception e) {
                    e.printStackTrace();
                    payment_AmountDouble = 0.00;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Cash Sales";

                }
            }
            if ((key.toUpperCase().equals("CARD"))) {
                try {
                    payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCardSales());
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    payment_AmountDiscDouble = Double.parseDouble(discount_String);
                    payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Card Sales";

                } catch (Exception e) {
                    e.printStackTrace();
                    payment_AmountDouble = 0.00;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Card Sales";

                }
            }
            if ((key.toUpperCase().equals("UPI"))) {
                try {
                    payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getUpiSales());
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    payment_AmountDiscDouble = Double.parseDouble(discount_String);
                    payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Upi Sales";

                } catch (Exception e) {
                    payment_AmountDouble = 0.00;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Upi Sales";

                    e.printStackTrace();

                }
            }
            if ((key.toUpperCase().equals("CREDIT"))) {
                try {
                    payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales());
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    payment_AmountDiscDouble = Double.parseDouble(discount_String);
                    payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Credit Sales";

                } catch (Exception e) {
                    e.printStackTrace();
                    payment_AmountDouble = 0.00;
                    Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                    key = "Credit Sales";

                }
            }

           // text_to_Print = text_to_Print + "[L] <b><font size='normal'>" + key + "     " + "Rs : " + Payment_Amount + "</b>\n";
            text_to_Print = text_to_Print + "[R] <b><font size='normal'>" + key + "[R]" + "Rs : " + Payment_Amount + "</b>\n";

        }

        for (int j = 0; j < finalBillDetails.size(); j++) {
            String key = finalBillDetails.get(j);
            String value = FinalBill_hashmap.get(key);
            value = "RS : " + value;
           /* if (Objects.requireNonNull(value).length() == 7) {
                //7spaces
                value = key + "       " + value;
            }
            if (value.length() == 8) {
                //6spaces
                value = key + "      " + value;
            }
            if (value.length() == 9) {
                //5spaces
                value = key + "     " + value;
            }
            if (value.length() == 10) {
                //4spaces
                value = key + "    " + value;
            }
            if (value.length() == 11) {
                //3spaces
                value = key + "   " + value;
            }
            if (value.length() == 12) {
                //2spaces
                value = key + "  " + value;
            }
            if (value.length() == 13) {
                //1spaces
                value = key + " " + value;
            }
            if (value.length() == 14) {
                //no space
                value = key + "" + value;
            }


            */
          //  text_to_Print = text_to_Print + "[L] <b><font size='normal'>" + value + "</b>\n";
            text_to_Print = text_to_Print + "[R] <b><font size='normal'>" +key+"[R]"+ value + "</b>\n";

        }
        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);
        return printer.addTextToPrint(text_to_Print);
    }



    private void printUsingPOSMachineReport() {
        try {
            Printer_POJO_Class[] PrinterSuCtgyNameArray = new Printer_POJO_Class[tmcSubCtgykey.size()];

            Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[Order_Item_List.size()];
            for (int subCtgyCount = 0; subCtgyCount < tmcSubCtgykey.size(); subCtgyCount++) {
                int i_value = 0;

                String SubCtgyName, menuid, SubCtgykey;
                SubCtgykey = tmcSubCtgykey.get(subCtgyCount).toString();
                Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
                SubCtgyName = subCtgyName_object.getTmcsubctgyname();
                for (int i = 0; i < Order_Item_List.size(); i++) {
                    menuid = Order_Item_List.get(i);
                    Modal_OrderDetails itemRow = OrderItem_hashmap.get(menuid);


                    String subCtgyKey_fromHashmap = itemRow.getTmcsubctgykey();
                    if (subCtgyKey_fromHashmap.equals(SubCtgykey)) {
                        String itemName = (itemRow.getItemname());
                        int indexofbraces = itemName.indexOf("(");
                        if (indexofbraces >= 0) {
                            itemName = (itemName.substring(0, indexofbraces));

                        }
                        if (itemName.length() > 19) {
                            itemName = (itemName.substring(0, 19));
                            itemName = itemName + "..";
                        }


                        String KilogramString = "", Quantity = "", TMCprice = "";
                        try {
                            KilogramString = itemRow.getWeightingrams();
                            if (KilogramString != null && (!KilogramString.equals("")) && (!(KilogramString.equals("0.00Kg"))) && (!(KilogramString.equals("0")))) {
                                Quantity = KilogramString + "g";
                            } else {
                                Quantity = itemRow.getQuantity() + "pc";
                            }
                        } catch (Exception e) {
                            Quantity = itemRow.getQuantity() + "pc";

                        }
                        try {
                            TMCprice = String.valueOf(itemRow.getTmcprice());

                        } catch (Exception e) {
                            e.printStackTrace();
                            TMCprice = "";
                        }
                        try {
                            itemName = itemName + "-" + Quantity;
                            Printer_POJO_ClassArray[i] = new Printer_POJO_Class(SubCtgyName, itemName, TMCprice);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                PrinterSuCtgyNameArray[subCtgyCount] = new Printer_POJO_Class(SubCtgyName);


            }


            PrinterFunctions.PortDiscovery(portName, portSettings);

            PrinterFunctions.SelectPrintMode(portName, portSettings, 0);
        /*    PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");
            //Log.i("tag", "The Meat Chop");


         */

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



            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Report : WholeSale SALES REPORT" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Current Date : " + DateString + "\n");
            //Log.i("tag", "Printer log" + CurrentDate);


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
            for (int subCtgyKeyCount = 0; subCtgyKeyCount < PrinterSuCtgyNameArray.length; subCtgyKeyCount++) {
                String subCtgyname = PrinterSuCtgyNameArray[subCtgyKeyCount].getSubCtgyName();
                PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 1, 0, 1, 0, 1, 0, 30, 0, "" + "\n" + subCtgyname + "\n" + "\n");
                //Log.i("tag", "Printer log subCtgyname " + subCtgyname);


                for (int i = 0; i < Printer_POJO_ClassArray.length; i++) {

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    String itemName_weight, itemPrice, subCtgyNameFromItemDesp;
                    subCtgyNameFromItemDesp = Printer_POJO_ClassArray[i].getSubCtgyName();
                    if (subCtgyNameFromItemDesp.equals(subCtgyname)) {
                        itemName_weight = Printer_POJO_ClassArray[i].getItemname_report();

                        itemPrice = "Rs. " + Printer_POJO_ClassArray[i].getTmcprice_report();
                        if (itemName_weight.length() == 10) {
                            //16spaces
                            itemName_weight = itemName_weight + "                ";
                        }
                        if (itemName_weight.length() == 11) {
                            //15spaces
                            itemName_weight = itemName_weight + "               ";
                        }
                        if (itemName_weight.length() == 12) {
                            //14spaces
                            itemName_weight = itemName_weight + "              ";
                        }
                        if (itemName_weight.length() == 13) {
                            //13spaces
                            itemName_weight = itemName_weight + "             ";
                        }
                        if (itemName_weight.length() == 14) {
                            //12spaces
                            itemName_weight = itemName_weight + "            ";
                        }
                        if (itemName_weight.length() == 15) {
                            //11spaces
                            itemName_weight = itemName_weight + "           ";
                        }
                        if (itemName_weight.length() == 16) {
                            //10spaces
                            itemName_weight = itemName_weight + "          ";
                        }
                        if (itemName_weight.length() == 17) {
                            //9spaces
                            itemName_weight = itemName_weight + "         ";
                        }
                        if (itemName_weight.length() == 18) {
                            //8spaces
                            itemName_weight = itemName_weight + "        ";
                        }
                        if (itemName_weight.length() == 19) {
                            //7spaces
                            itemName_weight = itemName_weight + "       ";
                        }
                        if (itemName_weight.length() == 20) {
                            //6spaces
                            itemName_weight = itemName_weight + "      ";
                        }
                        if (itemName_weight.length() == 21) {
                            //5spaces
                            itemName_weight = itemName_weight + "     ";
                        }
                        if (itemName_weight.length() == 22) {
                            //4spaces
                            itemName_weight = itemName_weight + "    ";
                        }
                        if (itemName_weight.length() == 23) {
                            //3spaces
                            itemName_weight = itemName_weight + "   ";
                        }
                        if (itemName_weight.length() == 24) {
                            //2spaces
                            itemName_weight = itemName_weight + "  ";
                        }
                        if (itemName_weight.length() == 25) {
                            //1spaces
                            itemName_weight = itemName_weight + " ";
                        }
                        if (itemName_weight.length() == 26) {
                            //0spaces
                            itemName_weight = itemName_weight + "";
                        }
                        if (itemName_weight.length() == 27) {
                            //0spaces
                            itemName_weight = itemName_weight + "";
                        }
                        if (itemName_weight.length() == 28) {
                            //0spaces
                            itemName_weight = itemName_weight + "";
                        }
                        if (itemName_weight.length() == 29) {
                            //0spaces
                            itemName_weight = itemName_weight + "";
                        }
                        if (itemName_weight.length() == 30) {
                            //0spaces
                            itemName_weight = itemName_weight + "";
                        }


                        if (itemPrice.length() == 8) {
                            //4spaces
                            itemPrice = "    " + itemPrice;
                        }
                        if (itemPrice.length() == 9) {
                            //3spaces
                            itemPrice = "   " + itemPrice;
                        }
                        if (itemPrice.length() == 10) {
                            //2spaces
                            itemPrice = "  " + itemPrice;
                        }
                        if (itemPrice.length() == 11) {
                            //1spaces
                            itemPrice = " " + itemPrice;
                        }
                        if (itemPrice.length() == 12) {
                            //0spaces
                            itemPrice = "" + itemPrice;
                        }
                        if (itemPrice.length() == 13) {
                            //0spaces
                            itemPrice = "" + itemPrice;
                        }
                        if (itemPrice.length() == 14) {
                            //no space
                            itemPrice = "" + itemPrice;
                        }


                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 48, itemName_weight);
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 50, itemPrice + "\n");
                        //Log.i("tag", "Printer log itemName_weight itemPrice  " + itemName_weight + "" + itemPrice + "");

                    }
                }
                //  PrinterFunctions.PrintSampleReceipt(portName,portSettings);


            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "Final Sales Break Up" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n"+ "\n");



            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "WHOLESALE Order Sales " + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            for (int i = 0; i < wholeSaleOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = wholeSaleOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = wholeSaleOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = wholeSaleOrderpaymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");



                if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble ;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Cash Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Cash Sales";

                    }
                }
                if ((key.toUpperCase().equals("CARD"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCardSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

                    }
                }
                if ((key.toUpperCase().equals("UPI"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getUpiSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Upi Sales";

                    } catch (Exception e) {
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Upi Sales";

                        e.printStackTrace();

                    }
                }
                if ((key.toUpperCase().equals("CREDIT"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Credit Sales";

                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Credit Sales";

                    }
                }

                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, key + "     " + "Rs : " + Payment_Amount + "\n");
                //Log.i("tag", "Printer log key key  " + key + "Rs : " + Payment_Amount);

            }


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");








            for (int j = 0; j < finalBillDetails.size(); j++) {
                String key = finalBillDetails.get(j);
                String value = FinalBill_hashmap.get(key);
                value = "RS : " + value;
                if (Objects.requireNonNull(value).length() == 7) {
                    //7spaces
                    value = key + "       " + value;
                }
                if (value.length() == 8) {
                    //6spaces
                    value = key + "      " + value;
                }
                if (value.length() == 9) {
                    //5spaces
                    value = key + "     " + value;
                }
                if (value.length() == 10) {
                    //4spaces
                    value = key + "    " + value;
                }
                if (value.length() == 11) {
                    //3spaces
                    value = key + "   " + value;
                }
                if (value.length() == 12) {
                    //2spaces
                    value = key + "  " + value;
                }
                if (value.length() == 13) {
                    //1spaces
                    value = key + " " + value;
                }
                if (value.length() == 14) {
                    //no space
                    value = key + "" + value;
                }
                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, value + "\n");
                //Log.i("tag", "Printer log key key" + value);


            }

            PrinterFunctions.PreformCut(portName, portSettings, 1);
        }
        catch(Exception e){
            e.printStackTrace();
            Toast.makeText(WholeSaleOrderSalesReport.this,"Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    if(Order_Item_List.size()>0) {
                        Adjusting_Widgets_Visibility(true);

                        try {
                            exportReport();


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    else{
                        Toast.makeText(WholeSaleOrderSalesReport.this, "There is no data to Export", Toast.LENGTH_SHORT).show();

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
                    if(Order_Item_List.size()>0) {
                        Adjusting_Widgets_Visibility(true);

                        try {
                            exportReport();


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    else{
                        Toast.makeText(WholeSaleOrderSalesReport.this, "There is no data to Export", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }






    public static HashMap<String,Modal_OrderDetails> sortByComparator(
            HashMap<String,Modal_OrderDetails> unsortMap) {

        List<Map.Entry<String,Modal_OrderDetails>> list = new LinkedList<Map.Entry<String,Modal_OrderDetails>>(
                unsortMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String,Modal_OrderDetails>> () {
            public int compare(Map.Entry<String,Modal_OrderDetails> o1, Map.Entry<String,Modal_OrderDetails> o2) {
                return o1.getValue().getItemname().compareTo(o2.getValue().getItemname());
            }
        });

        HashMap<String,Modal_OrderDetails> sortedMap = new LinkedHashMap<String,Modal_OrderDetails>();
        for (Map.Entry<String,Modal_OrderDetails> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private List<String> getSortedIdFromHashMap(List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {
         Order_Item_List.clear();
        array_of_orderId.clear();
        order_item_list.addAll(orderItem_hashmap.keySet());
        return order_item_list;
    }

    private boolean checkIfMenuItemisAlreadyAvailableInArray(String menuitemid) {
        return OrderItem_hashmap.containsKey(menuitemid);
    }
    
    
    
    private boolean checkIfPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return wholeSaleOrderpaymentModeHashmap.containsKey(menuitemid);
    }


    private boolean checkIfSubCtgywiseTotalisAlreadyAvailableInArray(String menuitemid) {
        return SubCtgywiseTotalHashmap.containsKey(menuitemid);
    }


    private boolean checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return wholeSaleOrderpaymentMode_DiscountHashmap.containsKey(menuitemid);
    }
    
    private String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        CurrentDate = df.format(c);

        CurrentDate = CurrentDay + ", " + CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
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
        }  else if (value == 2) {
            return "Mon";
        } else if (value == 3) {
            return "Tue";
        } else if (value == 4) {
            return "Wed";
        } else if (value == 5) {
            return "Thu";
        } else if (value == 6) {
            return "Fri";
        }
        else if (value == 7) {
            return "Sat";
        }
        return "";
    }
    public void exportReport() {
        if ((Order_Item_List == null) || (Order_Item_List.size() <= 0)) {
            Adjusting_Widgets_Visibility(false);

            return;
        }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state " + state + " extstoragedir " + extstoragedir);
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/WholeSale Order Sales Report /";

        //  File fol = new File(extstoragedir, path);
        File folder = new File(path);
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "WholeSale Order Sales Report_" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);

            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // document = new PdfDocument(new PdfWriter("MyFirstInvoice.pdf"));

            Document layoutDocument = new Document();
            PdfWriter.getInstance(layoutDocument, fOut);
            layoutDocument.open();

            addVendorDetails(layoutDocument);
            addItemRows(layoutDocument);

            layoutDocument.close();
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

            //Log.i("error", e.getLocalizedMessage());
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
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("WHOLESALE ORDER SALES REPORT");
            titlepara.setSpacingBefore(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_CENTER);
            layoutDocument.add(titlepara);

            String vendorname = "Vendor: " + Vendorname;
            com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
            vendorpara.setSpacingBefore(20);
            vendorpara.setAlignment(Element.ALIGN_LEFT);
            layoutDocument.add(vendorpara);

            com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date: " + DateString);
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
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            PdfPCell itemcell = new PdfPCell(new Phrase("Item"));
            itemcell.setBorder(Rectangle.NO_BORDER);
            itemcell.setBackgroundColor(BaseColor.GREEN);
            itemcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            itemcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            itemcell.setPaddingLeft(10);
            itemcell.setFixedHeight(30);
            table.addCell(itemcell);

            PdfPCell qtycell = new PdfPCell(new Phrase("Quantity"));
            qtycell.setBorder(Rectangle.NO_BORDER);
            qtycell.setBackgroundColor(BaseColor.GREEN);
            qtycell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qtycell.setFixedHeight(30);
            table.addCell(qtycell);

            PdfPCell pricecell = new PdfPCell(new Phrase("Price"));
            pricecell.setBorder(Rectangle.NO_BORDER);
            pricecell.setBackgroundColor(BaseColor.GREEN);
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

                String SubCtgyName, menuid;
                Modal_OrderDetails subCtgyName_object = SubCtgyKey_hashmap.get(SubCtgykey);
                SubCtgyName = subCtgyName_object.getTmcsubctgyname();
                for (int j = 0; j < Order_Item_List.size(); j++) {
                    menuid = Order_Item_List.get(j);
                    Modal_OrderDetails itemRow = OrderItem_hashmap.get(menuid);
                    String itemName = itemRow.getItemname();
                    String subCtgyKey_fromHashmap = "";
                    try {
                        subCtgyKey_fromHashmap = itemRow.getTmcsubctgykey();
                    } catch (Exception e) {
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
                            String KilogramString = "", Quantity = "";
                            try {
                                KilogramString = itemRow.getWeightingrams();
                                //   if(KilogramString != null &&(!KilogramString.equals(""))&&(!(KilogramString.equals("0.00Kg")))&&(!(KilogramString.equals("0")))) {
                                Quantity = KilogramString;
                                   /* }
                                    else{
                                        Quantity =  itemRow.getQuantity();
                                    }

                                    */
                            } catch (Exception e) {
                                //  Quantity =  itemRow.getQuantity();
                                e.printStackTrace();
                            }
                            if (!Quantity.equals("0.0") && (!Quantity.equals("0.00")) && (!Quantity.equals("0"))) {

                                double weightinGrams = Double.parseDouble(Quantity);
                                double kilogram = weightinGrams * 0.001;
                                Quantity = String.valueOf(decimalFormat.format(kilogram) + "Kg");


                            } else {
                                Quantity = itemRow.getQuantity() + "Pc";

                            }

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


                        PdfPCell priceSubCtgycell = new PdfPCell(new Phrase(""));
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


            //}
            layoutDocument.add(table);
            if(wholeSaleOrderpaymentModeArray.size()>0) {

                PdfPTable tablePaymentModetitle = new PdfPTable(1);
                tablePaymentModetitle.setWidthPercentage(100);
                tablePaymentModetitle.setSpacingBefore(20);


                PdfPCell paymentModertitle;
                paymentModertitle = new PdfPCell(new Phrase("WholeSale Order Sales"));
                paymentModertitle.setBorder(Rectangle.NO_BORDER);
                paymentModertitle.setHorizontalAlignment(Element.ALIGN_RIGHT);
                paymentModertitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
                paymentModertitle.setFixedHeight(25);
                paymentModertitle.setPaddingRight(20);
                tablePaymentModetitle.addCell(paymentModertitle);
                layoutDocument.add(tablePaymentModetitle);


                PdfPTable tablePaymentMode = new PdfPTable(4);
                tablePaymentMode.setWidthPercentage(100);
                tablePaymentMode.setSpacingBefore(20);
                PdfPCell paymentModeemptycell;
                PdfPCell paymentModeemptycellone;
                PdfPCell paymentModeitemkeycell;
                PdfPCell paymentModeitemValueCell;


                for (int i = 0; i < wholeSaleOrderpaymentModeArray.size(); i++) {
                    double payment_AmountDouble = 0;
                    double payment_AmountDiscDouble = 0;
                    String replacmentFromTextview = "", refundFromTextview = "";
                    double replacment_doubleFromTextview = 0, refund_doubleFromTextview = 0;

                    String Payment_Amount = "", key = wholeSaleOrderpaymentModeArray.get(i);
                    Modal_OrderDetails modal_orderDetails = wholeSaleOrderpaymentModeHashmap.get(key);
                    Modal_OrderDetails Payment_Modewise_discount = wholeSaleOrderpaymentMode_DiscountHashmap.get(key);

                    //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


                    if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {


                        try {
                            payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                            String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                            payment_AmountDiscDouble = Double.parseDouble(discount_String);
                            payment_AmountDouble = payment_AmountDouble - (payment_AmountDiscDouble);
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Cash Sales";
                        } catch (Exception e) {
                            e.printStackTrace();
                            payment_AmountDouble = 0.00;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Cash Sales";

                        }
                    }
                    if ((key.toUpperCase().equals("CARD"))) {
                        try {
                            payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCardSales());
                            String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                            payment_AmountDiscDouble = Double.parseDouble(discount_String);
                            payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Card Sales";

                        } catch (Exception e) {
                            e.printStackTrace();
                            payment_AmountDouble = 0.00;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Card Sales";

                        }
                    }
                    if ((key.toUpperCase().equals("UPI"))) {
                        try {
                            payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getUpiSales());
                            String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                            payment_AmountDiscDouble = Double.parseDouble(discount_String);
                            payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Upi Sales";

                        } catch (Exception e) {
                            payment_AmountDouble = 0.00;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Upi Sales";

                            e.printStackTrace();

                        }
                    }


                    if ((key.toUpperCase().equals("CREDIT"))) {
                        try {
                            payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales());
                            String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                            payment_AmountDiscDouble = Double.parseDouble(discount_String);
                            payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Credit Sales";

                        } catch (Exception e) {
                            payment_AmountDouble = 0.00;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Credit Sales";

                            e.printStackTrace();

                        }
                    }


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

                    paymentModeitemkeycell = new PdfPCell(new Phrase(key + " :  "));
                    paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemkeycell.setMinimumHeight(25);
                    paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tablePaymentMode.addCell(paymentModeitemkeycell);


                    paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + (Payment_Amount)));
                    paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemValueCell.setMinimumHeight(25);
                    paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeitemValueCell.setPaddingRight(10);
                    tablePaymentMode.addCell(paymentModeitemValueCell);


                }
                layoutDocument.add(tablePaymentMode);
            }

            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(20);
            PdfPCell emptycell;
            PdfPCell emptycellone;
            PdfPCell emptycelltwo;
            for (int i = 0; i < finalBillDetails.size(); i++) {
                String key = finalBillDetails.get(i);
                String value = FinalBill_hashmap.get(key);
                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);

                //Log.d("ExportReportActivity", "itemTotalRowsList value " + value);



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

                itemqtycell = new PdfPCell(new Phrase(key+":   "));
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

}