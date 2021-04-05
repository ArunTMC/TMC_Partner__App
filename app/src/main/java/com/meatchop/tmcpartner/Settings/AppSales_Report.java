package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListData;
import com.pos.printer.PrinterFunctions;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AppSales_Report extends AppCompatActivity {
    LinearLayout PrintReport_Layout,generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    TextView Phonepe,totalSales_headingText,Razorpay,Paytm, cashOnDelivery,upiSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;
    TextView appOrdersCount_textwidget,preorder_cashOnDelivery,preorder_Phonepe,preorder_Razorpay,preorder_paytmSales;
    String vendorKey;
    List<ListData> dataList = new ArrayList<>();
     String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    Adapter_App_sales_Report adapater_app_sales_report;
    String finalCashAmount_pdf,finalRazorpayAmount_pdf,finalPhonepeAmount_pdf,finalPaytmAmount_pdf;
    String finalpreorderCashAmount_pdf,finalpreorderRazorpayAmount_pdf,finalpreorderPhonepeAmount_pdf,finalpreorderPaytmAmount_pdf;
    boolean isgetOrderForSelectedDateCalled=false;
    boolean isgetPreOrderForSelectedDateCalled=false;

    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;


    public static List<String> paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  paymentModeHashmap  = new HashMap();;

    public static List<String> preorder_paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  preorder_paymentModeHashmap  = new HashMap();;


    public static List<String> preorderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  preorderpaymentMode_DiscountHashmap  = new HashMap();;


    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();


    public static List<String> paymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  paymentMode_DiscountHashmap  = new HashMap();;

    public static List<String> array_of_orderId;



    String CurrentDate,Ordertype;
    ListView posSalesReport_Listview;
    ScrollView scrollView;
    String DateString;
    String PreviousDateString;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    double totalAmountWithOutGst_from_array = 0,totalAmountWithOutGst = 0;
    double cash_GST=0,razorpay_Gst=0,cash_amount=0,Razorpay_amount=0,GST = 0;
    double totalAmount = 0;
    double screenInches ;
    double RazorpayPayment_Amount = 0;
    double CouponDiscount=0,Gst_from_array =0 ,cashPayment_Amount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_sales__report_activity);

        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        posSalesReport_Listview = findViewById(R.id.posSalesReport_Listview);
        generateReport_Layout = findViewById(R.id.generateReport_Layout);
        totalAmt_without_GST = findViewById(R.id.totalAmt_without_GST);
        totalCouponDiscount_Amt = findViewById(R.id.totalCouponDiscount_Amt);
        totalAmt_with_CouponDiscount = findViewById(R.id.totalAmt_with_CouponDiscount);
        totalGST_Amt = findViewById(R.id.totalGST_Amt);
        final_sales = findViewById(R.id.final_sales);
        totalSales_headingText = findViewById(R.id.totalSales_headingText);
        cashOnDelivery = findViewById(R.id.cashOnDelivery);
        Razorpay = findViewById(R.id.Razorpay);
        Paytm  = findViewById(R.id.paytmSales);
        Phonepe  = findViewById(R.id.Phonepe);
        scrollView  = findViewById(R.id.scrollView);
        PrintReport_Layout = findViewById(R.id.PrintReport_Layout);

        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        preorder_cashOnDelivery = findViewById(R.id.preorder_cashOnDelivery);
        preorder_Phonepe  = findViewById(R.id.preorder_Phonepe);
        preorder_Razorpay = findViewById(R.id.preorder_Razorpay);
        preorder_paytmSales = findViewById(R.id.preorder_paytmSales);


        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        paymentModeArray = new ArrayList<>();
        array_of_orderId = new ArrayList<>();
        preorder_paymentModeArray = new ArrayList<>();
        preorderpaymentMode_DiscountOrderid = new ArrayList<>();
        paymentMode_DiscountOrderid = new ArrayList<>();
        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        paymentModeHashmap.clear();
        paymentModeArray.clear();
        array_of_orderId.clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();
        preorder_paymentModeHashmap.clear();
        preorder_paymentModeArray.clear();
        preorderpaymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountHashmap.clear();

        DateString = getDate();
        PreviousDateString = getDatewithNameofthePreviousDay();
        dateSelector_text.setText(DateString);

        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        vendorKey = sharedPreferences.getString("VendorKey", "");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
         screenInches = Math.sqrt(x+y);
        try{

            Order_Item_List.clear();
            OrderItem_hashmap.clear();
            finalBillDetails.clear();
            FinalBill_hashmap.clear();
            paymentModeHashmap.clear();
            paymentModeArray.clear();
            array_of_orderId.clear();
            paymentMode_DiscountHashmap.clear();
            paymentMode_DiscountOrderid.clear();
            preorder_paymentModeHashmap.clear();
            preorder_paymentModeArray.clear();
            preorderpaymentMode_DiscountOrderid.clear();
            preorderpaymentMode_DiscountHashmap.clear();
            getPreOrderForSelectedDate(PreviousDateString,DateString, vendorKey);
            scrollView.fullScroll(View.FOCUS_UP);
        }catch(Exception e ){
            e.printStackTrace();;
        }

        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AppSales_Report.this,"Loading.... Please Wait",Toast.LENGTH_SHORT).show();
            }
        });
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


        PrintReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screenInches>8){
                    try {
                        Thread t = new Thread() {
                            public void run() {
                                printReport();
                            }
                        };
                        t.start();
                    }
                    catch(Exception e ){
                        Toast.makeText(AppSales_Report.this,"Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }                
                }
                else{
                    
                    
                  //  AddDatatoExcelSheet();
                    Toast.makeText(AppSales_Report.this,"Cant Find a Printer",Toast.LENGTH_LONG).show();
                }
            }
        });




        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(AppSales_Report.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                Log.d("ExportInvoiceActivity", "writeExternalStoragePermission " + writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(AppSales_Report.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    Adjusting_Widgets_Visibility(true);

                    try {
                        exportReport();
                    }catch (Exception e ){
                        e.printStackTrace();
                    }                }
            }
        });


    }


    private void getPreOrderForSelectedDate(String previousDaydate,String currentDate, String vendorKey) {

        if(isgetPreOrderForSelectedDateCalled){
            return;
        }
        isgetPreOrderForSelectedDateCalled=true;
        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        paymentModeHashmap.clear();
        paymentModeArray.clear();
        array_of_orderId.clear();
        preorder_paymentModeArray.clear();
        preorder_paymentModeHashmap.clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountHashmap.clear();
    //    addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);
        CouponDiscount=0;
        Adjusting_Widgets_Visibility(true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforSlotDate_Vendorkey_forReport + "?slotdate="+currentDate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDaydate,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        try {
                            String paymentMode = "", ordertype = "", orderid = "", slotname = "";

                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                            if(arrayLength>0){

                            for (; i1 < (arrayLength); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                //    Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                                    JSONArray itemdesp;

                                    if (json.has("ordertype")) {
                                        try {
                                            modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        modal_orderDetails.ordertype = "There is no OrderType";
                                        Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                    }

                                    if (json.has("slotname")) {
                                        try {
                                            modal_orderDetails.slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("slotname")));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_orderDetails.slotname = String.valueOf("");

                                        }

                                    } else {
                                        modal_orderDetails.slotname = String.valueOf("");
                                        Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("slotname")));


                                    }

                                    if ((ordertype.equals(Constants.APPORDER)) && (slotname.equals(Constants.PREORDER_SLOTNAME))) {
                                        if (json.has("paymentmode")) {

                                            try {
                                                paymentMode = String.valueOf(json.get("paymentmode")).toUpperCase();
                                                modal_orderDetails.paymentmode = String.valueOf(json.get("paymentmode")).toUpperCase();
                                                Log.d(Constants.TAG, "PaymentMode: " + String.valueOf(json.get("paymentmode")));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        } else {
                                            modal_orderDetails.paymentmode = "There is no payment mode";
                                            Log.d(Constants.TAG, "There is no PaymentMode: " + String.valueOf(json.get("ordertype")));


                                        }


                                        if (json.has("itemdesp")) {

                                            try {

                                                itemdesp = json.getJSONArray("itemdesp");
                                                modal_orderDetails.itemdesp = itemdesp;

                                                Log.d(Constants.TAG, "itemdesp has been succesfully  retrived");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {

                                            Log.d(Constants.TAG, "There is no itemdesp: ");


                                        }
                                        if (json.has("orderid")) {
                                            try {


                                                orderid = String.valueOf(json.get("orderid"));
                                                modal_orderDetails.orderid = String.valueOf(json.get("orderid"));


                                                Log.d(Constants.TAG, "orderid has been succesfully  retrived");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            Log.d(Constants.TAG, "There is no orderid: ");


                                        }

                                        if (!array_of_orderId.contains(orderid)) {
                                            array_of_orderId.add(orderid);


                                        if (json.has("coupondiscount")) {

                                            modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                            try {
                                                String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    if (couponDiscount_string.equals("")) {
                                                        couponDiscount_string = "0";

                                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                        CouponDiscount = CouponDiscount + CouponDiscount_double;

                                                        if (!preorderpaymentMode_DiscountOrderid.contains(orderid)) {
                                                            preorderpaymentMode_DiscountOrderid.add(orderid);
                                                            boolean isAlreadyAvailable = false;
                                                            try {
                                                                isAlreadyAvailable = checkIfpreorderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                ;
                                                            }
                                                            if (isAlreadyAvailable) {
                                                                Modal_OrderDetails modal_orderDetails1 = preorderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                String discountAmount = modal_orderDetails1.getDiscountAmount();
                                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                            } else {
                                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                                preorderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                                            }


                                                        } else {
                                                            Log.d(Constants.TAG, "orderid already availabe");

                                                        }
                                                    } else {

                                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                        CouponDiscount = CouponDiscount + CouponDiscount_double;


                                                        if (!preorderpaymentMode_DiscountOrderid.contains(orderid)) {
                                                            preorderpaymentMode_DiscountOrderid.add(orderid);
                                                            boolean isAlreadyAvailable = checkIfpreorderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                            if (isAlreadyAvailable) {
                                                                Modal_OrderDetails modal_orderDetails1 = preorderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                String discountAmount = modal_orderDetails1.getDiscountAmount();
                                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);
                                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                            } else {
                                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                                preorderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                                            }


                                                            Log.d(Constants.TAG, "mode already availabe");


                                                        } else {
                                                            Log.d(Constants.TAG, "orderid already availabe");

                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();


                                                }


                                                Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            String couponDiscount_string = String.valueOf("0");
                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);

                                            CouponDiscount = CouponDiscount + CouponDiscount_double;


                                            modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                        }


                                        try {


                                            if ((ordertype.equals(Constants.APPORDER)) && (slotname.equals(Constants.PREORDER_SLOTNAME))) {
                                                getItemDetailsFromItemDespArray(modal_orderDetails, paymentMode, slotname);
                                            } else {
                                                Log.d(Constants.TAG, "This order is not an Apporder e: ");

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(AppSales_Report.this, "- "+orderid, Toast.LENGTH_LONG).show();
                                            Log.d(Constants.TAG, "repeated orderid e: "+orderid);

                                    }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Adjusting_Widgets_Visibility(false);
                                    Toast.makeText(AppSales_Report.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
                                    Adjusting_Widgets_Visibility(false);
                                    Order_Item_List.clear();
                                    OrderItem_hashmap.clear();
                                    finalBillDetails.clear();
                                    FinalBill_hashmap.clear();
                                    paymentModeHashmap.clear();
                                    paymentModeArray.clear();
                                    paymentMode_DiscountHashmap.clear();
                                    paymentMode_DiscountOrderid.clear();
                                    CouponDiscount = 0;
                                    Helper.getListViewSize(posSalesReport_Listview, screenInches);


                                    addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap);
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                                }
                                if (arrayLength - 1 == i1) {
                                    if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
                                        isgetOrderForSelectedDateCalled = false;
                                        getOrderForSelectedDate(DateString, vendorKey);
                                    } else {
                                        Toast.makeText(AppSales_Report.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
                                        Adjusting_Widgets_Visibility(false);
                                        Order_Item_List.clear();
                                        OrderItem_hashmap.clear();
                                        finalBillDetails.clear();
                                        FinalBill_hashmap.clear();
                                        paymentModeHashmap.clear();
                                        paymentModeArray.clear();
                                        paymentMode_DiscountHashmap.clear();
                                        paymentMode_DiscountOrderid.clear();
                                        CouponDiscount = 0;
                                        Helper.getListViewSize(posSalesReport_Listview, screenInches);

                                        isgetOrderForSelectedDateCalled = false;

                                        addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap);

                                        getOrderForSelectedDate(DateString, vendorKey);

                                    }
                                }
                            }
                        }else{

                                Toast.makeText(AppSales_Report.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
                                Adjusting_Widgets_Visibility(false);
                                Order_Item_List.clear();
                                OrderItem_hashmap.clear();
                                finalBillDetails.clear();
                                FinalBill_hashmap.clear();
                                paymentModeHashmap.clear();
                                paymentModeArray.clear();
                                paymentMode_DiscountHashmap.clear();
                                paymentMode_DiscountOrderid.clear();
                                CouponDiscount = 0;
                                Helper.getListViewSize(posSalesReport_Listview, screenInches);

                                isgetOrderForSelectedDateCalled = false;

                                addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap);

                                getOrderForSelectedDate(DateString, vendorKey);
                        }



                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);
                            Toast.makeText(AppSales_Report.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);
                            Order_Item_List.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            paymentModeHashmap.clear();
                            paymentModeArray.clear();
                            paymentMode_DiscountHashmap.clear();
                            paymentMode_DiscountOrderid.clear();
                            CouponDiscount=0;
                            Helper.getListViewSize(posSalesReport_Listview, screenInches);

                            isgetOrderForSelectedDateCalled = false;

                            addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);
                            getOrderForSelectedDate(DateString, vendorKey);

                            e.printStackTrace();
                        }


                       /* if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {

                           Adjusting_Widgets_Visibility(false);
                            //addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            adapater_app_sales_report = new Adapter_App_sales_Report(AppSales_Report.this, Order_Item_List, OrderItem_hashmap);
                            posSalesReport_Listview.setAdapter(adapater_app_sales_report);

                            Helper.getListViewSize(posSalesReport_Listview, screenInches);
                            scrollView.fullScroll(View.FOCUS_UP);
                            addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);

}


                            getOrderForSelectedDate(CurrentDate, vendorKey);


                        }

                        else{
                            Toast.makeText(AppSales_Report.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);
                            Order_Item_List.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            paymentModeHashmap.clear();
                            paymentModeArray.clear();
                            paymentMode_DiscountHashmap.clear();
                            paymentMode_DiscountOrderid.clear();

                            Helper.getListViewSize(posSalesReport_Listview, screenInches);


                            addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);


                        }
                            */
                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(AppSales_Report.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                Adjusting_Widgets_Visibility(false);
                Order_Item_List.clear();
                OrderItem_hashmap.clear();
                finalBillDetails.clear();
                FinalBill_hashmap.clear();
                paymentModeHashmap.clear();
                paymentModeArray.clear();
                paymentMode_DiscountHashmap.clear();
                paymentMode_DiscountOrderid.clear();

                Helper.getListViewSize(posSalesReport_Listview, screenInches);
                isgetOrderForSelectedDateCalled = false;

                getOrderForSelectedDate(DateString, vendorKey);

                addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);

                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
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
        Volley.newRequestQueue(AppSales_Report.this).add(jsonObjectRequest);

    }

    private void printReport() {
        try {
            Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[Order_Item_List.size()];


            for (int i = 0; i < Order_Item_List.size(); i++) {
                String key = Order_Item_List.get(i);

                Modal_OrderDetails itemRow = OrderItem_hashmap.get(key);
                String itemName = "";
                String weight = "";

                String TMCprice = "";

                try {
                    itemName = itemRow.getItemname();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    weight = Objects.requireNonNull(itemRow).getWeightingrams();
                } catch (Exception e) {
                    e.printStackTrace();
                    weight = "";
                }
                if ((weight.equals("g")) || weight.equals(" g")) {
                    String name_quantity = " * (" + itemRow.getQuantity() + ")";
                    weight = (String.valueOf(name_quantity));

                } else {
                    String name_quantity = " *  (" + itemRow.getQuantity() + ")" + " - " + weight;

                    weight = (String.valueOf(name_quantity));
                }

                int indexofbraces = itemName.indexOf("(");
                if (indexofbraces >= 0) {
                    itemName = (itemName.substring(0, indexofbraces));

                }
                if (itemName.length() > 19) {
                    itemName = (itemName.substring(0, 19));
                    itemName = itemName + "..";
                }

                try {
                    TMCprice = String.valueOf(itemRow.getTmcprice());

                } catch (Exception e) {
                    e.printStackTrace();
                    TMCprice = "";
                }
                try {
                    itemName = itemName + weight;
                    Printer_POJO_ClassArray[i] = new Printer_POJO_Class("SubCtgyName", itemName, TMCprice);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            PrinterFunctions.PortDiscovery(portName, portSettings);

            PrinterFunctions.SelectPrintMode(portName, portSettings, 0);
            PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");
            Log.i("tag", "The Meat Chop");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "No 57, Rajendra Prasad Road," + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Hasthinapuram,Chromepet" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Chennai-600044" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "9698137713" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Report : App SALES REPORT" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Current Date : " + DateString + "\n");
            Log.i("tag", "Printer log" + CurrentDate);


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            for (int i = 0; i < Printer_POJO_ClassArray.length; i++) {

                PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                String itemName_weight, itemPrice;
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
                Log.i("tag", "Printer log itemName_weight itemPrice  " + itemName_weight + "" + itemPrice + "");

            }
            //  PrinterFunctions.PrintSampleReceipt(portName,portSettings);


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "Final Sales Break Up" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "EXPRESS DELIVERY  SALES" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            for (int i = 0; i < paymentModeArray.size(); i++) {
                String Payment_Amount = "", key = paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
                Log.d("ExportReportActivity", "itemTotalRowsList name " + key);

                double payment_AmountDouble = 0;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                if ((key.toUpperCase().equals(Constants.CASH_ON_DELIVERY)) || (key.toUpperCase().equals(Constants.CASH))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalCashAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Cash Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

                    }
                }
                if ((key.toUpperCase().equals(Constants.RAZORPAY))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalRazorpayAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Razorpay Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Razorpay Sales";

                    }

                }
                if ((key.toUpperCase().equals(Constants.PAYTM))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalPaytmAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Paytm Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Paytm Sales";

                    }


                }
                if ((key.toUpperCase().equals(Constants.PHONEPE))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalPhonepeAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Phonepe Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Phonepe Sales";

                    }

                }


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, key + "     " + "Rs : " + Payment_Amount + "\n");
                Log.i("tag", "Printer log key key  " + key + "Rs : " + Payment_Amount);


            }

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "PREORDER SALES" + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


            for (int i = 0; i < preorder_paymentModeArray.size(); i++) {
                String Payment_Amount = "", key = preorder_paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = preorder_paymentModeHashmap.get(key);
                Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                double payment_AmountDouble = 0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

                if ((key.toUpperCase().equals(Constants.CASH_ON_DELIVERY)) || (key.toUpperCase().equals(Constants.CASH))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderCashAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Cash Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

                    }
                }
                if ((key.toUpperCase().equals(Constants.RAZORPAY))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderRazorpayAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Razorpay Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Razorpay Sales";

                    }

                }
                if ((key.toUpperCase().equals(Constants.PAYTM))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderPaytmAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Paytm Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Paytm Sales";

                    }


                }
                if ((key.toUpperCase().equals(Constants.PHONEPE))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderPhonepeAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Phonepe Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Phonepe Sales";

                    }

                }


                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, key + "     " + "Rs : " + Payment_Amount + "\n");
                Log.i("tag", "Printer log key key  " + key + "Rs : " + Payment_Amount);


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
                Log.i("tag", "Printer log key key" + value);


            }

            PrinterFunctions.PreformCut(portName, portSettings, 1);
        }
        catch(Exception e ){
            e.printStackTrace();
            Toast.makeText(AppSales_Report.this,"Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

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

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
                // exportInvoice();
                try {
                    exportReport();
                }catch (Exception e ){
                    e.printStackTrace();
                }            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }




    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog


        datepicker = new DatePickerDialog(AppSales_Report.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            Log.d(Constants.TAG, "getOrderDetailsUsingApi year: " + year);
                            Log.d(Constants.TAG, "getOrderDetailsUsingApi monthOfYear: " + monthOfYear);
                            Log.d(Constants.TAG, "getOrderDetailsUsingApi dayOfMonth: " + dayOfMonth);


                            Order_Item_List.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            paymentModeHashmap.clear();
                            paymentModeArray.clear();
                            paymentMode_DiscountHashmap.clear();
                            paymentMode_DiscountOrderid.clear();
                            preorder_paymentModeHashmap.clear();
                            preorder_paymentModeArray.clear();
                            String month_in_String = getMonthString(monthOfYear);
                             String monthstring = String.valueOf(monthOfYear+1);
                             String datestring =  String.valueOf(dayOfMonth);
                            if(datestring.length()==1){
                                datestring="0"+datestring;
                            }
                             if(monthstring.length()==1){
                                 monthstring="0"+monthstring;
                             }


                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay =   getDayString(dayOfWeek);
                            Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);





                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            isgetPreOrderForSelectedDateCalled =false;
                            isgetOrderForSelectedDateCalled = false;

                            getPreOrderForSelectedDate(PreviousDateString,DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();
    }



    private void getOrderForSelectedDate(String dateString, String vendorKey) {
        if(isgetOrderForSelectedDateCalled){
            return;
        }
        isgetOrderForSelectedDateCalled=true;
        Adjusting_Widgets_Visibility(true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey_forReport + "?orderplaceddate=" + dateString+"&vendorkey="+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        try {
                            String paymentMode="",ordertype = "",orderid = "",slotname="",deliverytype="";

                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (; i1 < (arrayLength); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
//                                    Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                                    JSONArray itemdesp;

                                    if (json.has("ordertype")) {
                                        try {
                                            modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        modal_orderDetails.ordertype = "There is no OrderType";
                                        Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                    }
                                    if (json.has("slotname")) {
                                        try {
                                            modal_orderDetails.slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("slotname")));

                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }

                                    } else {
                                        Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("orderid")));


                                    }
                                    if ((ordertype.equals(Constants.APPORDER))) {

                                        if(json.has("deliverytype"))
                                        {
                                            try{
                                                modal_orderDetails.deliverytype = String.valueOf(json.get("deliverytype"));
                                                deliverytype =  String.valueOf(json.get("deliverytype"));

                                                if(deliverytype.equals(Constants.STOREPICKUP_DELIVERYTYPE)){
                                                    slotname =  String.valueOf(Constants.EXPRESSDELIVERY_SLOTNAME);
                                                    modal_orderDetails.slotname =String.valueOf(Constants.EXPRESSDELIVERY_SLOTNAME);
                                                    Log.d(Constants.TAG, "deliverytype: " + String.valueOf(json.get("orderid")));

                                                }
                                                Log.d(Constants.TAG, "deliverytype 1: " + String.valueOf(json.get("orderid")));

                                            }catch (Exception e){
                                                e.printStackTrace();
                                                Log.d(Constants.TAG, "deliverytype:2 " + String.valueOf(json.get("orderid")));

                                            }

                                        }
                                        else
                                        {
                                            modal_orderDetails.deliverytype = "There is no deliverytype";
                                            Log.d(Constants.TAG, " deliverytype3: " + String.valueOf(json.get("orderid")));


                                        }






                                        if(slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME) || (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME))||(slotname.equals(""))){


                                        if (json.has("paymentmode")) {

                                            try {
                                                paymentMode = String.valueOf(json.get("paymentmode")).toUpperCase();
                                                modal_orderDetails.paymentmode = String.valueOf(json.get("paymentmode")).toUpperCase();
                                                Log.d(Constants.TAG, "PaymentMode: " + String.valueOf(json.get("paymentmode")));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                        } else {
                                            modal_orderDetails.paymentmode = "There is no payment mode";
                                            Log.d(Constants.TAG, "There is no PaymentMode: " + String.valueOf(json.get("ordertype")));


                                        }


                                        if (json.has("itemdesp")) {

                                            try {

                                                itemdesp = json.getJSONArray("itemdesp");
                                                modal_orderDetails.itemdesp = itemdesp;

                                                Log.d(Constants.TAG, "itemdesp has been succesfully  retrived");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {

                                            Log.d(Constants.TAG, "There is no itemdesp: ");


                                        }
                                        if (json.has("orderid")) {
                                            try {


                                                orderid = String.valueOf(json.get("orderid"));
                                                modal_orderDetails.orderid = String.valueOf(json.get("orderid"));


                                                Log.d(Constants.TAG, "orderid has been succesfully  retrived");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            Log.d(Constants.TAG, "There is no orderid: ");


                                        }

                                            if(!array_of_orderId.contains(orderid)){
                                                array_of_orderId.add(orderid);

                                            }
                                            else{
                                                Toast.makeText(AppSales_Report.this, "- "+orderid, Toast.LENGTH_LONG).show();

                                            }



                                        if (json.has("coupondiscount")) {

                                            modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                            try {
                                                String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    if (couponDiscount_string.equals("")) {
                                                        couponDiscount_string = "0";

                                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                        CouponDiscount = CouponDiscount + CouponDiscount_double;

                                                        if (!paymentMode_DiscountOrderid.contains(orderid)) {
                                                            paymentMode_DiscountOrderid.add(orderid);
                                                            boolean isAlreadyAvailable = false;
                                                            try {
                                                                isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                ;
                                                            }
                                                            if (isAlreadyAvailable) {
                                                                Modal_OrderDetails modal_orderDetails1 = paymentMode_DiscountHashmap.get(paymentMode);
                                                                String discountAmount = modal_orderDetails1.getDiscountAmount();
                                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                            } else {
                                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                                paymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                                            }


                                                        } else {
                                                            Log.d(Constants.TAG, "mode already availabe");

                                                        }
                                                    } else {

                                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                        CouponDiscount = CouponDiscount + CouponDiscount_double;


                                                        if (!paymentMode_DiscountOrderid.contains(orderid)) {
                                                            paymentMode_DiscountOrderid.add(orderid);
                                                            boolean isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                            if (isAlreadyAvailable) {
                                                                Modal_OrderDetails modal_orderDetails1 = paymentMode_DiscountHashmap.get(paymentMode);
                                                                String discountAmount = modal_orderDetails1.getDiscountAmount();
                                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                            } else {
                                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                                paymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                                            }


                                                            Log.d(Constants.TAG, "mode already availabe");


                                                        } else {
                                                            Log.d(Constants.TAG, "mode already availabe");

                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();


                                                }


                                                Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        } else {
                                            String couponDiscount_string = String.valueOf("0");
                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);

                                            CouponDiscount = CouponDiscount + CouponDiscount_double;


                                            modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                        }


                                    }
                                }
                                    if((ordertype.equals(Constants.APPORDER))&&(slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)||(slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME))||(slotname.equals("")))) {
                                        getItemDetailsFromItemDespArray(modal_orderDetails,paymentMode, slotname);
                                    }
                                    else{
                                        Log.d(Constants.TAG, "This order is not an Apporder e: " );

                                    }




                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Adjusting_Widgets_Visibility(false);

                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                                }


                            }


                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);

                            e.printStackTrace();
                        }

                        if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
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

                            Adjusting_Widgets_Visibility(false);
                            //addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                              adapater_app_sales_report = new Adapter_App_sales_Report(AppSales_Report.this, Order_Item_List, OrderItem_hashmap);
                             posSalesReport_Listview.setAdapter(adapater_app_sales_report);

                            Helper.getListViewSize(posSalesReport_Listview, screenInches);
                            scrollView.fullScroll(View.FOCUS_UP);

                            addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);



                        }

                        else{
                            Toast.makeText(AppSales_Report.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);
                            Order_Item_List.clear();
                            OrderItem_hashmap.clear();
                            finalBillDetails.clear();
                            FinalBill_hashmap.clear();
                            paymentModeHashmap.clear();
                            paymentModeArray.clear();
                            preorder_paymentModeArray.clear();
                            preorder_paymentModeHashmap.clear();
                            paymentMode_DiscountHashmap.clear();
                            paymentMode_DiscountOrderid.clear();
                            CouponDiscount=0;
                            Helper.getListViewSize(posSalesReport_Listview, screenInches);


                            addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);


                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(AppSales_Report.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                Adjusting_Widgets_Visibility(false);

                Helper.getListViewSize(posSalesReport_Listview, screenInches);
                if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
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

                    Adjusting_Widgets_Visibility(false);
                    //addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                    adapater_app_sales_report = new Adapter_App_sales_Report(AppSales_Report.this, Order_Item_List, OrderItem_hashmap);
                    posSalesReport_Listview.setAdapter(adapater_app_sales_report);

                    Helper.getListViewSize(posSalesReport_Listview, screenInches);
                    scrollView.fullScroll(View.FOCUS_UP);

                    addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);



                }
                else{
                    Toast.makeText(AppSales_Report.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);
                    Order_Item_List.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    paymentModeHashmap.clear();
                    paymentModeArray.clear();
                    preorder_paymentModeArray.clear();
                    preorder_paymentModeHashmap.clear();
                    paymentMode_DiscountHashmap.clear();
                    paymentMode_DiscountOrderid.clear();
                    CouponDiscount=0;
                    Helper.getListViewSize(posSalesReport_Listview, screenInches);


                    addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);


                }



                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
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
        Volley.newRequestQueue(AppSales_Report.this).add(jsonObjectRequest);

    }





    private void addFinalPaymentAmountDetails(List<String> paymentModeArray, HashMap<String, Modal_OrderDetails> paymentModeHashmap) {
        FinalBill_hashmap.clear();
        finalBillDetails.clear();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double phonepe_amount = 0,phonepe_Discount_amount = 0 ,cash_amount = 0,cash_Discount_amount = 0 ,Paytm_amount=0, Razorpay_amount=0,PaytmDiscount_amount = 0,
                RazorpayDiscount_amount=0,totalAmount=0,GST=0,totalAmountWithOutGst=0,totalAmount_with_Coupondiscount_double=0;
        double preorderphonepe_amount = 0,preorderphonepe_Discount_amount = 0 ,preordercash_amount = 0,preordercash_Discount_amount = 0 ,preorderPaytm_amount=0, preorderRazorpay_amount=0,preorderPaytmDiscount_amount = 0,
                preorderRazorpayDiscount_amount=0,preordertotalAmount=0;


        appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));



        for(String PaymentModefromArray : paymentModeArray) {
            Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(PaymentModefromArray);
            Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(PaymentModefromArray);

            if (PaymentModefromArray.equals(Constants.RAZORPAY)) {


                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getRazorpaySales());
                    Razorpay_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(Razorpay_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    RazorpayDiscount_amount = Double.parseDouble(discount_String);
                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);
                    GST = GST + GST_array;
                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            if (PaymentModefromArray.equals(Constants.PAYTM)) {


                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getPaytmSales());
                    Paytm_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(Paytm_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    PaytmDiscount_amount = Double.parseDouble(discount_String);
                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);
                    GST = GST + GST_array;
                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }





            if (PaymentModefromArray.equals(Constants.CASH_ON_DELIVERY)) {
                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getCashOndeliverySales());
                    cash_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(cash_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;

                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    cash_Discount_amount = Double.parseDouble(discount_String);

                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);

                    GST = GST + GST_array;

                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (PaymentModefromArray.equals(Constants.PHONEPE)) {
                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getPhonepeSales());
                    phonepe_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(phonepe_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;

                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    phonepe_Discount_amount = Double.parseDouble(discount_String);

                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);

                    GST = GST + GST_array;

                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        for(String preorderPaymentModefromArray : preorder_paymentModeArray) {
            Modal_OrderDetails modal_orderDetails = preorder_paymentModeHashmap.get(preorderPaymentModefromArray);
            Modal_OrderDetails Payment_Modewise_discount = preorderpaymentMode_DiscountHashmap.get(preorderPaymentModefromArray);

            if (preorderPaymentModefromArray.equals(Constants.RAZORPAY)) {


                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getRazorpaySales());
                    preorderRazorpay_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(preorderRazorpay_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    preorderRazorpayDiscount_amount = Double.parseDouble(discount_String);
                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);
                    GST = GST + GST_array;
                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            if (preorderPaymentModefromArray.equals(Constants.PAYTM)) {


                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getPaytmSales());
                    preorderPaytm_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(preorderPaytm_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    preorderPaytmDiscount_amount = Double.parseDouble(discount_String);
                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);
                    GST = GST + GST_array;
                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }





            if (preorderPaymentModefromArray.equals(Constants.CASH_ON_DELIVERY)) {
                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getCashOndeliverySales());
                    preordercash_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(preordercash_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;

                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    preordercash_Discount_amount = Double.parseDouble(discount_String);

                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);

                    GST = GST + GST_array;

                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (preorderPaymentModefromArray.equals(Constants.PHONEPE)) {
                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getPhonepeSales());
                    preorderphonepe_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(preorderphonepe_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;

                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getDiscountAmount());
                    preorderphonepe_Discount_amount = Double.parseDouble(discount_String);

                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);

                    GST = GST + GST_array;

                    Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        try{
            cash_amount = cash_amount-cash_Discount_amount;
            finalCashAmount_pdf=String.valueOf(cash_amount);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            Razorpay_amount = Razorpay_amount-RazorpayDiscount_amount;
            finalRazorpayAmount_pdf=String.valueOf(Razorpay_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            Paytm_amount = Paytm_amount-PaytmDiscount_amount;
            finalPaytmAmount_pdf=String.valueOf(Paytm_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            phonepe_amount = phonepe_amount-phonepe_Discount_amount;
            finalPhonepeAmount_pdf=String.valueOf(phonepe_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }







//////



        try{
            preordercash_amount = preordercash_amount-preordercash_Discount_amount;
            finalpreorderCashAmount_pdf=String.valueOf(preordercash_amount);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            preorderRazorpay_amount = preorderRazorpay_amount-preorderRazorpayDiscount_amount;
            finalpreorderRazorpayAmount_pdf=String.valueOf(preorderRazorpay_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            preorderPaytm_amount = preorderPaytm_amount-preorderPaytmDiscount_amount;
            finalpreorderPaytmAmount_pdf=String.valueOf(preorderPaytm_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            preorderphonepe_amount = preorderphonepe_amount-preorderphonepe_Discount_amount;
            finalpreorderPhonepeAmount_pdf=String.valueOf(preorderphonepe_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }





        totalAmount_with_Coupondiscount_double = totalAmountWithOutGst-CouponDiscount;
        totalAmount = totalAmount_with_Coupondiscount_double+GST;



        try{
            totalAmt_without_GST.setText(String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format(CouponDiscount)));
            totalAmt_with_CouponDiscount.setText(String.valueOf(decimalFormat.format(totalAmount_with_Coupondiscount_double)));
            totalGST_Amt.setText(String.valueOf(decimalFormat.format(GST)));
            final_sales.setText(String.valueOf(decimalFormat.format(totalAmount)));
            cashOnDelivery.setText(String.valueOf(decimalFormat.format(cash_amount)));
            Razorpay.setText(String.valueOf(decimalFormat.format(Razorpay_amount)));
            Paytm.setText(String.valueOf(decimalFormat.format(Paytm_amount)));
            Phonepe.setText(String.valueOf(decimalFormat.format(phonepe_amount)));



            preorder_cashOnDelivery.setText(String.valueOf(decimalFormat.format(preordercash_amount)));
            preorder_Razorpay.setText(String.valueOf(decimalFormat.format(preorderRazorpay_amount)));
            preorder_paytmSales.setText(String.valueOf(decimalFormat.format(preorderPaytm_amount)));
            preorder_Phonepe.setText(String.valueOf(decimalFormat.format(preorderphonepe_amount)));



            totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmount)));

            finalBillDetails.add("TOTAL : ");
            FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            finalBillDetails.add("DISCOUNT : ");
            FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(CouponDiscount));
            finalBillDetails.add("SUBTOTAL : ");
            FinalBill_hashmap.put("SUBTOTAL : ", String.valueOf(decimalFormat.format(totalAmount_with_Coupondiscount_double)));
            finalBillDetails.add("GST : ");
            FinalBill_hashmap.put("GST : ", String.valueOf(decimalFormat.format(GST)));
            finalBillDetails.add("FINAL SALES : ");
            FinalBill_hashmap.put("FINAL SALES : ", String.valueOf(decimalFormat.format(totalAmount)));

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }




    private void getItemDetailsFromItemDespArray(Modal_OrderDetails modal_orderDetailsfromResponse, String paymentMode, String slotname) {
      //  DecimalFormat decimalFormat = new DecimalFormat("0.00");
         String newOrderWeightInGrams = "";
        double newweight = 0,gstAmount = 0,tmcprice=0;
        int quantity=0;
        try {
            JSONArray jsonArray = modal_orderDetailsfromResponse.getItemdesp();

            for(int i=0; i < jsonArray.length(); i++) {
                Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                JSONObject json = jsonArray.getJSONObject(i);
                Log.d(Constants.TAG, "this json" +json.toString());

                Modal_OrderDetails modal_orderDetails_ItemDesp = new Modal_OrderDetails();


                if(json.has("menuitemid")) {
                    modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));
                    try {
                        if (json.has("grossweight")) {
                            newOrderWeightInGrams = String.valueOf(json.get("grossweight"));

                            if((newOrderWeightInGrams.contains("Kg"))||(newOrderWeightInGrams.contains("KG"))||(newOrderWeightInGrams.contains("kg"))){
                                newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");

                                double newOrderWeightInGrams_double = Double.parseDouble(newOrderWeightInGrams);
                                newOrderWeightInGrams_double = newOrderWeightInGrams_double*1000;
                                newOrderWeightInGrams = String.valueOf(newOrderWeightInGrams_double);
                            }


                             newOrderWeightInGrams =newOrderWeightInGrams.replaceAll("[^\\d.]", "");

                            modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams+"g");

                            modal_orderDetails_ItemDesp.grossweight = String.valueOf(newOrderWeightInGrams);
                        }
                        else {
                            if (json.has("grossweightingrams")) {
                                try {
                                    newOrderWeightInGrams =  String.valueOf(json.get("grossweightingrams"));
                                    if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))) {
                                        newOrderWeightInGrams =newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                                        modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams+"g");

                                        modal_orderDetails_ItemDesp.grossweight = String.valueOf(newOrderWeightInGrams);
                                    }
                                    else {
                                        if (json.has("netweight")) {

                                        try {

                                            newOrderWeightInGrams = String.valueOf(json.get("netweight"));
                                            if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))) {
                                            newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                                            String lastThree_weightInGrams = null;
                                            if (newOrderWeightInGrams != null && newOrderWeightInGrams.length() >= 3) {
                                                lastThree_weightInGrams = newOrderWeightInGrams.substring(newOrderWeightInGrams.length() - 3);
                                            }
                                            modal_orderDetails_ItemDesp.weightingrams = String.valueOf(lastThree_weightInGrams + "g");

                                            modal_orderDetails_ItemDesp.netweight = String.valueOf(lastThree_weightInGrams);


                                        }
                                            else{
                                                if (json.has("portionsize")) {

                                                    try {

                                                        newOrderWeightInGrams = String.valueOf(json.get("portionsize"));

                                                        modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                        modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                        modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                                    }

                                                }
                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                            modal_orderDetails_ItemDesp.netweight = String.valueOf("");
                                        }
                                    }
                                        else{
                                            if (json.has("portionsize")) {

                                                try {

                                                    newOrderWeightInGrams = String.valueOf(json.get("portionsize"));

                                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                    modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                    modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                                }

                                            }
                                            }
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                    modal_orderDetails_ItemDesp.grossweight = String.valueOf("");
                                }

                            }

                            else {
                                if (json.has("netweight")) {

                                    try {
                                        newOrderWeightInGrams = String.valueOf(json.get("netweight"));
                                        if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))) {

                                        String lastThree_weightInGrams = null;
                                        if (newOrderWeightInGrams != null && newOrderWeightInGrams.length() >= 3) {
                                            lastThree_weightInGrams = newOrderWeightInGrams.substring(newOrderWeightInGrams.length() - 3);
                                        }
                                        modal_orderDetails_ItemDesp.weightingrams = String.valueOf(lastThree_weightInGrams + "g");
                                        modal_orderDetails_ItemDesp.netweight = String.valueOf(lastThree_weightInGrams);
                                    }

                                        else{
                                            if (json.has("portionsize")) {

                                                try {

                                                    newOrderWeightInGrams = String.valueOf(json.get("portionsize"));

                                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                    modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                    modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                                }

                                            }
                                        }

                                }

                                catch (Exception e) {
                                    e.printStackTrace();
                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                    modal_orderDetails_ItemDesp.netweight = String.valueOf("");
                                }
                            }
                                else{
                                    if (json.has("portionsize")) {

                                        try {

                                            newOrderWeightInGrams = String.valueOf(json.get("portionsize"));

                                            modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                            modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                            modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                        }

                                    }
                                }



                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                        modal_orderDetails_ItemDesp.grossweight = String.valueOf("");

                    }



                    try{
                        modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }


                    try{
                        modal_orderDetails_ItemDesp.ordertype = modal_orderDetailsfromResponse.getOrdertype();

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }


                    try{
                        modal_orderDetails_ItemDesp.paymentmode = modal_orderDetailsfromResponse.getPaymentmode();

                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }




                    if (json.has("marinadeitemdesp")) {
                        Modal_OrderDetails marinade_modal_orderDetails_ItemDesp = new Modal_OrderDetails();
                        double marinadesObjectgstAmount=0,marinadesObjectpayableAmount=0,marinadesObjectquantity;

                        try {
                             marinadesObjectquantity = Double.parseDouble(String.valueOf(json.get("quantity")));
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                            marinadesObjectquantity = 1;
                        }

                        JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                        String marinadeitemmenuItemId = String.valueOf(marinadesObject.get("menuitemid"));
                        String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));
                        try {
                            if (json.has("grossweight")) {
                                newOrderWeightInGrams = String.valueOf(marinadesObject.get("grossweight"));
                                    if((newOrderWeightInGrams.contains("Kg"))||(newOrderWeightInGrams.contains("KG"))||(newOrderWeightInGrams.contains("kg"))){
                                        newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");

                                        double newOrderWeightInGrams_double = Double.parseDouble(newOrderWeightInGrams);
                                        newOrderWeightInGrams_double = newOrderWeightInGrams_double*1000;
                                        newOrderWeightInGrams = String.valueOf(newOrderWeightInGrams_double);
                                }
                                newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");



                                    marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams + "g");

                                marinade_modal_orderDetails_ItemDesp.grossweight = String.valueOf(newOrderWeightInGrams);

                            
                           
                            }
                            else {
                                if (json.has("grossweightingrams")) {
                                    try {
                                        newOrderWeightInGrams =  String.valueOf(marinadesObject.get("grossweightingrams"));
                                        if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))) {
                                            newOrderWeightInGrams =newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                                            marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams+"g");

                                            marinade_modal_orderDetails_ItemDesp.grossweight = String.valueOf(newOrderWeightInGrams);
                                        }
                                        else {
                                            if (marinadesObject.has("netweight")) {

                                                try {
                                                    newOrderWeightInGrams = String.valueOf(marinadesObject.get("netweight"));
                                                    if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))) {

                                                        newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                                                    String lastThree_weightInGrams = null;
                                                    if (newOrderWeightInGrams != null && newOrderWeightInGrams.length() >= 3) {
                                                        lastThree_weightInGrams = newOrderWeightInGrams.substring(newOrderWeightInGrams.length() - 3);
                                                    }
                                                        marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(lastThree_weightInGrams + "g");

                                                    marinade_modal_orderDetails_ItemDesp.netweight = String.valueOf(lastThree_weightInGrams);
                                                }
                                                    else{
                                                        if (marinadesObject.has("portionsize")) {

                                                            try {

                                                                newOrderWeightInGrams = String.valueOf(marinadesObject.get("portionsize"));

                                                                marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                                marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                                marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                                            }

                                                        }
                                                    }

                                            }catch (Exception e) {
                                                e.printStackTrace();
                                                marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                marinade_modal_orderDetails_ItemDesp.netweight = String.valueOf("");
                                            }
                                        }
                                            else{
                                                if (marinadesObject.has("portionsize")) {

                                                    try {

                                                        newOrderWeightInGrams = String.valueOf(marinadesObject.get("portionsize"));

                                                        marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                        marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                        marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                                    }

                                                }
                                            }



                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                        marinade_modal_orderDetails_ItemDesp.grossweight = String.valueOf("");
                                    }

                                }

                                else {
                                    if (marinadesObject.has("netweight")) {
                                        try {
                                            newOrderWeightInGrams = String.valueOf(marinadesObject.get("netweight"));

                                            if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))) {

                                                String lastThree_weightInGrams = null;
                                            if (newOrderWeightInGrams != null && newOrderWeightInGrams.length() >= 3) {
                                                lastThree_weightInGrams = newOrderWeightInGrams.substring(newOrderWeightInGrams.length() - 3);
                                            }
                                            marinade_modal_orderDetails_ItemDesp.netweight = String.valueOf(lastThree_weightInGrams);
                                                marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(lastThree_weightInGrams + "g");

                                        }
                                            else{
                                                if (marinadesObject.has("portionsize")) {

                                                    try {

                                                        newOrderWeightInGrams = String.valueOf(marinadesObject.get("portionsize"));

                                                        marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                        marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                        marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                                    }

                                                }
                                            }


                                    }catch (Exception e) {
                                            e.printStackTrace();
                                            marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                            marinade_modal_orderDetails_ItemDesp.netweight = String.valueOf("");
                                        }

                                    }
                                    else{
                                        if (marinadesObject.has("portionsize")) {

                                            try {

                                                newOrderWeightInGrams = String.valueOf(marinadesObject.get("portionsize"));

                                                marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                marinade_modal_orderDetails_ItemDesp.portionsize = String.valueOf("");
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            marinade_modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                            marinade_modal_orderDetails_ItemDesp.grossweight = String.valueOf("");

                        }


                        if(marinadesObject.has("tmcprice")) {
                            try {
                                marinadesObjectpayableAmount = Double.parseDouble(String.valueOf(marinadesObject.get("tmcprice")));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        if(marinadesObject.has("gstamount")) {
                            try {
                                marinadesObjectgstAmount = Double.parseDouble(String.valueOf(marinadesObject.get("gstamount")));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        if(marinadesObject.has("quantity")) {
                            try {
                                marinadesObjectquantity = Double.parseDouble(String.valueOf(json.get("quantity")));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                            try{
                                marinadesObjectpayableAmount = marinadesObjectpayableAmount+marinadesObjectgstAmount;
                                marinadesObjectpayableAmount = marinadesObjectpayableAmount*marinadesObjectquantity;
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                        marinade_modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));

                        marinade_modal_orderDetails_ItemDesp.tmcprice = (String.valueOf(marinadesObjectpayableAmount));
                        marinade_modal_orderDetails_ItemDesp.gstamount = String.valueOf(marinadesObjectgstAmount);
                        marinade_modal_orderDetails_ItemDesp.quantity = String.valueOf(json.get("quantity"));
                        marinade_modal_orderDetails_ItemDesp.itemname = marinadeitemName;



                        if(slotname.equals(Constants.PREORDER_SLOTNAME)){
                        if(paymentMode.equals(Constants.PAYTM)){
                            double payment_tmcprice=0,payment_gstamount=0;
                            if(!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPaytmSales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                    payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setPaytmSales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }



                            }else{
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = marinadesObjectpayableAmount;
                                    payment_gstamount = marinadesObjectgstAmount;
                                    paymentDetails.setPaytmSales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);
                                }
                                catch (Exception e ){
                                    e.printStackTrace();
                                }
                            }


                        }



                        if(paymentMode.equals(Constants.PHONEPE)){
                            double payment_tmcprice=0,payment_gstamount=0;
                            if(!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPhonepeSales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                    payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }



                            }else{
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = marinadesObjectpayableAmount;
                                    payment_gstamount = marinadesObjectgstAmount;
                                    paymentDetails.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);
                                }
                                catch (Exception e ){
                                    e.printStackTrace();
                                }
                            }


                        }




                        if(paymentMode.equals(Constants.RAZORPAY)){
                            double payment_tmcprice=0,payment_gstamount=0;
                            if(!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getRazorpaySales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                    payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }



                            }else{
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = marinadesObjectpayableAmount;
                                    payment_gstamount = marinadesObjectgstAmount;
                                    paymentDetails.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);
                                }
                                catch (Exception e ){
                                    e.printStackTrace();
                                }
                            }


                        }


                        if(paymentMode.equals(Constants.CASH_ON_DELIVERY)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getCashOndeliverySales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());

                                    payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                    payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = marinadesObjectpayableAmount;
                                    payment_gstamount = marinadesObjectgstAmount;
                                    paymentDetails.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                        }


                        if((slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME))||(slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME))){
                            if(paymentMode.equals(Constants.PAYTM)){
                                double payment_tmcprice=0,payment_gstamount=0;
                                if(!paymentModeArray.contains(paymentMode)) {
                                    paymentModeArray.add(paymentMode);
                                }
                                boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                                if (isItemAlreadyOrdered) {
                                    try {
                                        Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                        double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPaytmSales());
                                        double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                        payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                        payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                        paymentDetailsfrom_hashMap.setPaytmSales(String.valueOf((payment_tmcprice)));
                                        paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }



                                }else{
                                    try {
                                        Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                        payment_tmcprice = marinadesObjectpayableAmount;
                                        payment_gstamount = marinadesObjectgstAmount;
                                        paymentDetails.setPaytmSales(String.valueOf((payment_tmcprice)));
                                        paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                        paymentModeHashmap.put(paymentMode, paymentDetails);
                                    }
                                    catch (Exception e ){
                                        e.printStackTrace();
                                    }
                                }


                            }



                            if(paymentMode.equals(Constants.PHONEPE)){
                                double payment_tmcprice=0,payment_gstamount=0;
                                if(!paymentModeArray.contains(paymentMode)) {
                                    paymentModeArray.add(paymentMode);
                                }
                                boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                                if (isItemAlreadyOrdered) {
                                    try {
                                        Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                        double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPhonepeSales());
                                        double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                        payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                        payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                        paymentDetailsfrom_hashMap.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                        paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }



                                }else{
                                    try {
                                        Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                        payment_tmcprice = marinadesObjectpayableAmount;
                                        payment_gstamount = marinadesObjectgstAmount;
                                        paymentDetails.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                        paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                        paymentModeHashmap.put(paymentMode, paymentDetails);
                                    }
                                    catch (Exception e ){
                                        e.printStackTrace();
                                    }
                                }


                            }




                            if(paymentMode.equals(Constants.RAZORPAY)){
                                double payment_tmcprice=0,payment_gstamount=0;
                                if(!paymentModeArray.contains(paymentMode)) {
                                    paymentModeArray.add(paymentMode);
                                }
                                boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                                if (isItemAlreadyOrdered) {
                                    try {
                                        Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                        double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getRazorpaySales());
                                        double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                        payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                        payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                        paymentDetailsfrom_hashMap.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                        paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }



                                }else{
                                    try {
                                        Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                        payment_tmcprice = marinadesObjectpayableAmount;
                                        payment_gstamount = marinadesObjectgstAmount;
                                        paymentDetails.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                        paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                        paymentModeHashmap.put(paymentMode, paymentDetails);
                                    }
                                    catch (Exception e ){
                                        e.printStackTrace();
                                    }
                                }


                            }


                            if(paymentMode.equals(Constants.CASH_ON_DELIVERY)) {
                                double payment_tmcprice = 0, payment_gstamount = 0;
                                if (!paymentModeArray.contains(paymentMode)) {
                                    paymentModeArray.add(paymentMode);
                                }
                                boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                                if (isItemAlreadyOrdered) {
                                    try {
                                        Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                        double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getCashOndeliverySales());
                                        double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());

                                        payment_tmcprice = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                        payment_gstamount = marinadesObjectgstAmount + gstAmount_from_HashMap;
                                        paymentDetailsfrom_hashMap.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                        paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else {
                                    try {
                                        Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                        payment_tmcprice = marinadesObjectpayableAmount;
                                        payment_gstamount = marinadesObjectgstAmount;
                                        paymentDetails.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                        paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                        paymentModeHashmap.put(paymentMode, paymentDetails);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }





                        if (Order_Item_List.contains(marinadeitemmenuItemId)) {
                            double payableAmount_marinade=0,quantity_marinade=0,gstAmount_marinade=0;
                            Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(marinadeitemmenuItemId);
                            double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getFinalAmount());
                            int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                            double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());
                            try {
                                 payableAmount_marinade = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                 quantity_marinade = marinadesObjectquantity + quantity_from_HashMap;
                                 gstAmount_marinade = gstAmount + gstAmount_from_HashMap;
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity_marinade)));
                            modal_orderDetails_itemDespfrom_hashMap.setFinalAmount(String.valueOf((payableAmount_marinade)));
                            modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount_marinade)));



                        }
                        else{
                            Order_Item_List.add(marinadeitemmenuItemId);

                            OrderItem_hashmap.put(marinadeitemmenuItemId, marinade_modal_orderDetails_ItemDesp);
                        }


                    }



                    if(json.has("tmcprice")) {
                        try {

                            tmcprice = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }






                    if(json.has("quantity")) {

                        try {
                            quantity = Integer.parseInt(String.valueOf(json.get("quantity")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    if(json.has("gstamount")) {

                        try {
                            gstAmount = Double.parseDouble(String.valueOf(json.get("gstamount")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    try {
                        tmcprice = tmcprice+gstAmount;
                        tmcprice = quantity*tmcprice;
                        int inttmcPrice = (int) Math.ceil(tmcprice);

                        String tmcprice_string = String.valueOf(((inttmcPrice)));




                        String gstAmount_string = String.valueOf(((json.get("gstamount"))));

                        modal_orderDetails_ItemDesp.tmcprice = String.valueOf(tmcprice_string);
                        modal_orderDetails_ItemDesp.quantity = String.valueOf(json.get("quantity"));
                        modal_orderDetails_ItemDesp.gstamount = String.valueOf(gstAmount_string);

                    }
                    catch (Exception e){
                        e.printStackTrace();


                    }


                    if(slotname.equals(Constants.PREORDER_SLOTNAME)) {


                        if (paymentMode.equals(Constants.PAYTM)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPaytmSales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setPaytmSales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setPaytmSales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }


                        if (paymentMode.equals(Constants.PHONEPE)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPhonepeSales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }


                        if (paymentMode.equals(Constants.RAZORPAY)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getRazorpaySales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }


                        if (paymentMode.equals(Constants.CASH_ON_DELIVERY)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!preorder_paymentModeArray.contains(paymentMode)) {
                                preorder_paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfpreorderPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = preorder_paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getCashOndeliverySales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());

                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    preorder_paymentModeHashmap.put(paymentMode, paymentDetails);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    }


                    if((slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME))||(slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME))){


                        if (paymentMode.equals(Constants.PAYTM)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!paymentModeArray.contains(paymentMode)) {
                                paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPaytmSales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setPaytmSales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setPaytmSales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    paymentModeHashmap.put(paymentMode, paymentDetails);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }


                        if (paymentMode.equals(Constants.PHONEPE)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!paymentModeArray.contains(paymentMode)) {
                                paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getPhonepeSales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setPhonepeSales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    paymentModeHashmap.put(paymentMode, paymentDetails);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }


                        if (paymentMode.equals(Constants.RAZORPAY)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!paymentModeArray.contains(paymentMode)) {
                                paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getRazorpaySales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());
                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setRazorpaySales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    paymentModeHashmap.put(paymentMode, paymentDetails);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }


                        if (paymentMode.equals(Constants.CASH_ON_DELIVERY)) {
                            double payment_tmcprice = 0, payment_gstamount = 0;
                            if (!paymentModeArray.contains(paymentMode)) {
                                paymentModeArray.add(paymentMode);
                            }
                            boolean isItemAlreadyOrdered = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);
                            if (isItemAlreadyOrdered) {
                                try {
                                    Modal_OrderDetails paymentDetailsfrom_hashMap = paymentModeHashmap.get(paymentMode);
                                    double tmcprice_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getCashOndeliverySales());
                                    double gstAmount_from_HashMap = Double.parseDouble(paymentDetailsfrom_hashMap.getGstamount());

                                    payment_tmcprice = tmcprice + tmcprice_from_HashMap;
                                    payment_gstamount = gstAmount + gstAmount_from_HashMap;
                                    paymentDetailsfrom_hashMap.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                    paymentDetailsfrom_hashMap.setGstamount(String.valueOf((payment_gstamount)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Modal_OrderDetails paymentDetails = new Modal_OrderDetails();

                                    payment_tmcprice = tmcprice;
                                    payment_gstamount = gstAmount;
                                    paymentDetails.setCashOndeliverySales(String.valueOf((payment_tmcprice)));
                                    paymentDetails.setGstamount(String.valueOf((payment_gstamount)));


                                    paymentModeHashmap.put(paymentMode, paymentDetails);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    }




                    String menuitemid = String.valueOf(json.get("menuitemid"));
                    if (Order_Item_List.contains(menuitemid)) {
                        boolean isItemAlreadyOrdered = checkIfMenuItemisAlreadyAvailableInArray(menuitemid);
                        if (isItemAlreadyOrdered) {
                            Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(menuitemid);
                            double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getTmcprice());
                            int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                            double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());
                            String oldOrder_WeightInGrams = modal_orderDetails_itemDespfrom_hashMap.getNetweight();
                        /*    double doubleoldOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                            int intOldOrder_WeightInGrams = (int) Math.ceil(doubleoldOrder_WeightInGrams);

                            int intNewOrder_WeightInGrams = (int) Math.ceil(newweight);

                            intOldOrder_WeightInGrams = intOldOrder_WeightInGrams +intNewOrder_WeightInGrams;


                         */
                            tmcprice = tmcprice + tmcprice_from_HashMap;
                            quantity = quantity + quantity_from_HashMap;
                            gstAmount = gstAmount + gstAmount_from_HashMap;

                              modal_orderDetails_itemDespfrom_hashMap.setNetweight(String.valueOf((oldOrder_WeightInGrams)));
                            modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                            modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf(tmcprice));
                            modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount)));


                        } else {

                            OrderItem_hashmap.put(menuitemid, modal_orderDetails_ItemDesp);

                        }
                    } else {
                        Order_Item_List.add(menuitemid);

                        boolean isItemAlreadyOrdered = checkIfMenuItemisAlreadyAvailableInArray(menuitemid);
                        if (isItemAlreadyOrdered) {

                            Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(menuitemid);
                            double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getTmcprice());
                            int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                            double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());

                            tmcprice = tmcprice + tmcprice_from_HashMap;
                            quantity = quantity + quantity_from_HashMap;
                            gstAmount = gstAmount + gstAmount_from_HashMap;
                            String oldOrder_WeightInGrams = modal_orderDetails_itemDespfrom_hashMap.getNetweight();
                         /*   double doubleoldOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                            int intOldOrder_WeightInGrams = (int) Math.ceil(doubleoldOrder_WeightInGrams);

                            double doubleNewOrder_WeightInGrams = Double.parseDouble(oldOrder_WeightInGrams);
                            int intNewOrder_WeightInGrams = (int) Math.ceil(newweight);

                            intOldOrder_WeightInGrams = intOldOrder_WeightInGrams +intNewOrder_WeightInGrams;
                            Log.d(Constants.TAG, "this json pre 3 " +String.valueOf(oldOrder_WeightInGrams));


                          */

                            modal_orderDetails_itemDespfrom_hashMap.setNetweight(String.valueOf((oldOrder_WeightInGrams)));

                            modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                            modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf((tmcprice)));
                            modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount)));


                        } else {

                            OrderItem_hashmap.put(menuitemid, modal_orderDetails_ItemDesp);
                        }
                    }

                  /*  if(paymentModeArray.size()>0&&paymentModeHashmap.size()>0){

                        double cash_GST=0,razorpay_Gst=0,phonepe_GST=0,paytm_Gst=0,phonepe_amount=0,paytm_amount=0,cash_amount=0,Razorpay_amount=0;
                        for(String PaymentModeArray : paymentModeArray){
                            if(PaymentModeArray.equals(Constants.CASH_ON_DELIVERY)) {
                                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(PaymentModeArray);

                                cash_GST = Double.parseDouble(String.valueOf(Objects.requireNonNull(modal_orderDetails).getGstamount()));
                                totalAmountWithOutGst_from_array = Double.parseDouble(String.valueOf(modal_orderDetails.getCashOndeliverySales()));
                                cash_amount = cash_GST + totalAmountWithOutGst_from_array;
                                totalAmount= cash_amount+totalAmount;
                                totalAmountWithOutGst = totalAmountWithOutGst_from_array+totalAmountWithOutGst;

                            }
                            if(PaymentModeArray.equals(Constants.RAZORPAY)) {
                                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(PaymentModeArray);

                                razorpay_Gst = Double.parseDouble(String.valueOf(Objects.requireNonNull(modal_orderDetails).getGstamount()));
                                totalAmountWithOutGst_from_array = Double.parseDouble(String.valueOf(modal_orderDetails.getRazorpaySales()));
                                Razorpay_amount= razorpay_Gst + totalAmountWithOutGst_from_array;
                                totalAmount= Razorpay_amount+totalAmount;
                                totalAmountWithOutGst = totalAmountWithOutGst_from_array+totalAmountWithOutGst;


                            }

                            if(PaymentModeArray.equals(Constants.PAYTM)) {
                                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(PaymentModeArray);

                                paytm_Gst = Double.parseDouble(String.valueOf(Objects.requireNonNull(modal_orderDetails).getGstamount()));
                                totalAmountWithOutGst_from_array = Double.parseDouble(String.valueOf(modal_orderDetails.getPaytmSales()));
                                paytm_amount = paytm_Gst + totalAmountWithOutGst_from_array;
                                totalAmount= paytm_amount+totalAmount;
                                totalAmountWithOutGst = totalAmountWithOutGst_from_array+totalAmountWithOutGst;

                            }
                            if(PaymentModeArray.equals(Constants.PHONEPE)) {
                                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(PaymentModeArray);

                                phonepe_GST = Double.parseDouble(String.valueOf(Objects.requireNonNull(modal_orderDetails).getGstamount()));
                                totalAmountWithOutGst_from_array = Double.parseDouble(String.valueOf(modal_orderDetails.getPhonepeSales()));
                                phonepe_amount= phonepe_GST + totalAmountWithOutGst_from_array;
                                totalAmount= phonepe_amount+totalAmount;
                                totalAmountWithOutGst = totalAmountWithOutGst_from_array+totalAmountWithOutGst;


                            }
                        }





                    }
                */


                }
                else{
                    Log.d(Constants.TAG, "this order have no menuitemId " + String.valueOf(json.get("itemname")));
                    Adjusting_Widgets_Visibility(false);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


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



    private boolean checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentMode_DiscountHashmap.containsKey(menuitemid);
    }



    private boolean checkIfpreorderPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return preorderpaymentMode_DiscountHashmap.containsKey(menuitemid);
    }


    private boolean checkIfPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentModeHashmap.containsKey(menuitemid);
    }



    private boolean checkIfpreorderPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return preorder_paymentModeHashmap.containsKey(menuitemid);
    }


    private boolean checkIfMenuItemisAlreadyAvailableInArray(String menuitemid) {
        return OrderItem_hashmap.containsKey(menuitemid);
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



    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();


        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);




        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);

        System.out.println("todays Date  " + CurrentDate);
        System.out.println("PreviousdayDate Date  " + PreviousdayDate);

        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;

        return yesterdayAsString;
    }



    private String getDatewithNameofthePreviousDayfromSelectedDay(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, -1);




        Date c1 = calendar.getTime();
        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }


    public String getDate_and_time() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);


        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDate = df.format(c);


        return CurrentDate;
    }


    private String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay + ", " + CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }
    public void exportReport() {
        if ((Order_Item_List == null) || (Order_Item_List.size() <= 0)) { return; }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        Log.d("PdfUtil", "external storage state "+state+" extstoragedir "+extstoragedir);
        File fol = new File(extstoragedir, "testpdf");
        File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "App Sales Report_" + System.currentTimeMillis()  +".pdf";
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
            pdfViewIntent.setDataAndType(Uri.fromFile(file),"application/pdf");
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

            String Vendorname  = sharedPreferences.getString("VendorName","");

            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 22, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("APP SALES REPORT");
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
            String rsunit = "Rs.";
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

            PdfPCell itemnamecell = null; PdfPCell itemqtycell = null;
            PdfPCell itempricecell = null;
            for (int i=0; i<Order_Item_List.size(); i++) {
                String weight="" ,key = Order_Item_List.get(i);

                Modal_OrderDetails itemRow = OrderItem_hashmap.get(key);

                try {
                    weight = Objects.requireNonNull(itemRow).getWeightingrams();
                }
                catch (Exception e ){
                    e.printStackTrace();
                    weight = "";
                }
                if((weight.equals("g"))||weight.equals(" g")){
                    String name_quantity = " ";
                    weight=(String.valueOf(name_quantity));

                }
                else {
                    String name_quantity =  " - " + weight;

                    weight=(String.valueOf(name_quantity));
                }








                String itemName = itemRow.getItemname()+weight;
                Log.i(Constants.TAG,"size"+(itemRow.getItemname()));
                itemnamecell = new PdfPCell(new Phrase((itemName)));
                itemnamecell.setBorder(Rectangle.BOTTOM);
                itemnamecell.setBorderColor(BaseColor.LIGHT_GRAY);
                itemnamecell.setMinimumHeight(30);
                itemnamecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemnamecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itemnamecell.setPaddingLeft(10);




                String Quantity = itemRow.getQuantity();
                itemqtycell = new PdfPCell(new Phrase("" +Quantity ));
                itemqtycell.setBorder(Rectangle.BOTTOM);
                itemqtycell.setBorderColor(BaseColor.LIGHT_GRAY);
                itemqtycell.setMinimumHeight(30);
                itemqtycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemqtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                String totalval = rsunit + String.format( itemRow.getTmcprice());
                itempricecell = new PdfPCell(new Phrase(totalval));
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
            layoutDocument.add(table);



            PdfPTable tablePaymentModetitle = new PdfPTable(1);
            tablePaymentModetitle.setWidthPercentage(100);
            tablePaymentModetitle.setSpacingBefore(20);


            PdfPCell paymentModertitle;
            paymentModertitle = new PdfPCell(new Phrase("EXPRESS DELIVERY"));
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
            String Payment_Amount = "0";
            double payment_AmountDouble =0.0;
            for (int i = 0; i < paymentModeArray.size(); i++) {
                String key = paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
                Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");





                if ((key.toUpperCase().equals(Constants.CASH_ON_DELIVERY)) || (key.toUpperCase().equals(Constants.CASH))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalCashAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Cash Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

                    }
                }
                if ((key.toUpperCase().equals(Constants.RAZORPAY))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalRazorpayAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Razorpay Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Razorpay Sales";

                    }

                }
                if ((key.toUpperCase().equals(Constants.PAYTM))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalPaytmAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Paytm Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Paytm Sales";

                    }


                }
                if ((key.toUpperCase().equals(Constants.PHONEPE))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalPhonepeAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Phonepe Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Phonepe Sales";

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

                paymentModeitemkeycell = new PdfPCell(new Phrase(key+" :  "));
                paymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                paymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                paymentModeitemkeycell.setMinimumHeight(25);
                paymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                paymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tablePaymentMode.addCell(paymentModeitemkeycell);


                paymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + Payment_Amount));
                paymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                paymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                paymentModeitemValueCell.setMinimumHeight(25);
                paymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                paymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                paymentModeitemValueCell.setPaddingRight(10);
                tablePaymentMode.addCell(paymentModeitemValueCell);





            }
            layoutDocument.add(tablePaymentMode);


            PdfPTable tablepreorderPaymentModetitle = new PdfPTable(1);
            tablepreorderPaymentModetitle.setWidthPercentage(100);
            tablepreorderPaymentModetitle.setSpacingBefore(20);


            PdfPCell paymentModePreordertitle;
            paymentModePreordertitle = new PdfPCell(new Phrase("PREORDER"));
            paymentModePreordertitle.setBorder(Rectangle.NO_BORDER);
            paymentModePreordertitle.setHorizontalAlignment(Element.ALIGN_RIGHT);
            paymentModePreordertitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            paymentModePreordertitle.setFixedHeight(25);
            paymentModePreordertitle.setPaddingRight(20);

            tablepreorderPaymentModetitle.addCell(paymentModePreordertitle);
            layoutDocument.add(tablepreorderPaymentModetitle);




            PdfPTable tablepreorderPaymentMode = new PdfPTable(4);
            tablepreorderPaymentMode.setWidthPercentage(100);
            tablepreorderPaymentMode.setSpacingBefore(20);
            PdfPCell PreorderpaymentModeemptycell;
            PdfPCell PreorderpaymentModeitemkeycell;
            PdfPCell PreorderpaymentModeemptycellone;

            PdfPCell PreorderpaymentModeitemValueCell;

            String preorder_Payment_Amount = "0";


            for (int i = 0; i < preorder_paymentModeArray.size(); i++) {
                String key = preorder_paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = preorder_paymentModeHashmap.get(key);
                Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


                DecimalFormat decimalFormat = new DecimalFormat("0.00");




                if ((key.toUpperCase().equals(Constants.CASH_ON_DELIVERY)) || (key.toUpperCase().equals(Constants.CASH))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderCashAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Cash Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

                    }
                }
                if ((key.toUpperCase().equals(Constants.RAZORPAY))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderRazorpayAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Razorpay Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Razorpay Sales";

                    }

                }
                if ((key.toUpperCase().equals(Constants.PAYTM))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderPaytmAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Paytm Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Paytm Sales";

                    }


                }
                if ((key.toUpperCase().equals(Constants.PHONEPE))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(finalpreorderPhonepeAmount_pdf);
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Phonepe Sales";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key="Phonepe Sales";

                    }

                }


                PreorderpaymentModeemptycell = new PdfPCell(new Phrase(""));
                PreorderpaymentModeemptycell.setBorder(Rectangle.NO_BORDER);
                PreorderpaymentModeemptycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                PreorderpaymentModeemptycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                PreorderpaymentModeemptycell.setFixedHeight(25);
                tablepreorderPaymentMode.addCell(PreorderpaymentModeemptycell);

                PreorderpaymentModeemptycellone = new PdfPCell(new Phrase(""));
                PreorderpaymentModeemptycellone.setBorder(Rectangle.NO_BORDER);
                PreorderpaymentModeemptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                PreorderpaymentModeemptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                PreorderpaymentModeemptycellone.setFixedHeight(25);
                tablepreorderPaymentMode.addCell(PreorderpaymentModeemptycellone);

                PreorderpaymentModeitemkeycell = new PdfPCell(new Phrase(key+" :  "));
                PreorderpaymentModeitemkeycell.setBorderColor(BaseColor.LIGHT_GRAY);
                PreorderpaymentModeitemkeycell.setBorder(Rectangle.NO_BORDER);
                PreorderpaymentModeitemkeycell.setMinimumHeight(25);
                PreorderpaymentModeitemkeycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PreorderpaymentModeitemkeycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tablepreorderPaymentMode.addCell(PreorderpaymentModeitemkeycell);


                PreorderpaymentModeitemValueCell = new PdfPCell(new Phrase("Rs. " + Payment_Amount));
                PreorderpaymentModeitemValueCell.setBorderColor(BaseColor.LIGHT_GRAY);
                PreorderpaymentModeitemValueCell.setBorder(Rectangle.NO_BORDER);
                PreorderpaymentModeitemValueCell.setMinimumHeight(25);
                PreorderpaymentModeitemValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                PreorderpaymentModeitemValueCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                PreorderpaymentModeitemValueCell.setPaddingRight(10);
                tablepreorderPaymentMode.addCell(PreorderpaymentModeitemValueCell);





            }
            layoutDocument.add(tablepreorderPaymentMode);





            PdfPTable tablefinaltitle = new PdfPTable(1);
            tablefinaltitle.setWidthPercentage(100);
            tablefinaltitle.setSpacingBefore(20);



            PdfPCell finaltitle;
            finaltitle = new PdfPCell(new Phrase("FINAL"));
            finaltitle.setBorder(Rectangle.NO_BORDER);
            finaltitle.setHorizontalAlignment(Element.ALIGN_RIGHT);
            finaltitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            finaltitle.setFixedHeight(25);
            finaltitle.setPaddingRight(20);
            tablefinaltitle.addCell(finaltitle);
            layoutDocument.add(tablefinaltitle);



            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(20);
            PdfPCell emptycell; PdfPCell emptycellone; PdfPCell emptycelltwo;
            for (int i=0; i<finalBillDetails.size(); i++) {
                String key = finalBillDetails.get(i);
                String value= FinalBill_hashmap.get(key);
                Log.d("ExportReportActivity", "itemTotalRowsList name "+key);

                Log.d("ExportReportActivity", "itemTotalRowsList value "+value);
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
                itempricecell = new PdfPCell(new Phrase("Rs. "+value));
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