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
import android.os.Build;
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
import com.android.volley.Response;
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
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListData;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListItem;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListSection;

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
import java.util.Map;
import java.util.Objects;

public class ConsolidatedReportSubCtgywise extends AppCompatActivity {
    LinearLayout generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    TextView totalSales_headingText, appsales, possales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;
    String vendorKey, ordertype, slotname, DateString;
    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;
    double screenInches;
    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();
    Adapter_ConsolidatedSalesReport_listview adapater_pos_sales_report;
    double oldpayableamount = 0;

    public static List<String> ordertypeArray;
    public static HashMap<String, Modal_OrderDetails> ordertypeHashmap = new HashMap();
    ;

    public static List<Modal_OrderDetails> SubCtgyKey_List;
    public static HashMap<String, Modal_OrderDetails> SubCtgyKey_hashmap = new HashMap();

    public static List<String> Pos_couponDiscountOrderidArray;
    public static HashMap<String, String> Pos_couponDiscount_hashmap = new HashMap();


    public static List<String> SubCtgywiseTotalArray;
    public static HashMap<String, String> SubCtgywiseTotalHashmap = new HashMap();
    ;
    public static List<String> tmcSubCtgykey;
    List<ListData> dataList = new ArrayList<>();
    boolean isgetPreOrderForSelectedDateCalled = false;
    boolean isgetOrderForSelectedDateCalled = false;

    public static List<String> couponDiscountOrderidArray;
    public static HashMap<String, String> couponDiscount_hashmap = new HashMap();
    ScrollView scrollView;
    double itemDespTotalAmount = 0;
    String CurrentDate, CouponDiscout, pos_CouponDiscount, PreviousDateString;
    ListView consolidatedSalesReport_Listview;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consolidated_report_sub_ctgywise);


        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        consolidatedSalesReport_Listview = findViewById(R.id.consolidatedSalesReport_Listview);
        generateReport_Layout = findViewById(R.id.generateReport_Layout);
        totalAmt_without_GST = findViewById(R.id.totalAmt_without_GST);
        totalCouponDiscount_Amt = findViewById(R.id.totalCouponDiscount_Amt);
        totalAmt_with_CouponDiscount = findViewById(R.id.totalAmt_with_CouponDiscount);
        totalGST_Amt = findViewById(R.id.totalGST_Amt);
        final_sales = findViewById(R.id.final_sales);
        appsales = findViewById(R.id.appSales);
        possales = findViewById(R.id.posSales);
        totalSales_headingText = findViewById(R.id.totalSales_headingText);
        scrollView = findViewById(R.id.scrollView);

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        tmcSubCtgykey = new ArrayList<>();
        SubCtgyKey_List = new ArrayList<>();
        SubCtgywiseTotalArray = new ArrayList<>();
        couponDiscountOrderidArray = new ArrayList<>();
        Pos_couponDiscountOrderidArray = new ArrayList<>();
        ordertypeArray = new ArrayList<>();

        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        ordertypeArray.clear();
        ordertypeHashmap.clear();
        couponDiscount_hashmap.clear();
        couponDiscountOrderidArray.clear();
        Pos_couponDiscount_hashmap.clear();
        Pos_couponDiscountOrderidArray.clear();
        SubCtgywiseTotalArray.clear();
        SubCtgywiseTotalHashmap.clear();
        tmcSubCtgykey.clear();

        CurrentDate = getDate_and_time();
        dateSelector_text.setText(CurrentDate);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);

        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        vendorKey = sharedPreferences.getString("VendorKey", "");
        DateString = getDate_and_time();
        PreviousDateString = getDatewithNameofthePreviousDay();

        dateSelector_text.setText(DateString);
        try {
            Order_Item_List.clear();
            OrderItem_hashmap.clear();
            finalBillDetails.clear();
            FinalBill_hashmap.clear();
            couponDiscountOrderidArray.clear();
            ordertypeArray.clear();
            ordertypeHashmap.clear();
            couponDiscount_hashmap.clear();
            Pos_couponDiscount_hashmap.clear();
            Pos_couponDiscountOrderidArray.clear();
            SubCtgywiseTotalArray.clear();
            SubCtgywiseTotalHashmap.clear();
            dataList.clear();
            tmcSubCtgykey.clear();

            getOrderForSelectedDate(PreviousDateString, DateString, vendorKey);

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


        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(ConsolidatedReportSubCtgywise.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(ConsolidatedReportSubCtgywise.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    Adjusting_Widgets_Visibility(true);
                    try {
                        exportReport();

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                }
            }
        });


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

                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                }
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void openDatePicker() {
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
        Pos_couponDiscount_hashmap.clear();
        Pos_couponDiscountOrderidArray.clear();
        dataList.clear();

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
                            Pos_couponDiscount_hashmap.clear();
                            Pos_couponDiscountOrderidArray.clear();
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
                            dataList.clear();
                            tmcSubCtgykey.clear();

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
                            dateSelector_text.setText(CurrentDay + ", " + dayOfMonth + " " + month_in_String + " " + year);
                            DateString = (CurrentDay + ", " + dayOfMonth + " " + month_in_String + " " + year);
                            isgetPreOrderForSelectedDateCalled = false;
                            isgetOrderForSelectedDateCalled = false;
                            //getOrderForSelectedDate(DateString, vendorKey);
                            getOrderForSelectedDate(PreviousDateString, DateString, vendorKey);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, year, month, day);
        datepicker.show();
    }








    private void getOrderForSelectedDate(String previousDateString, String dateString, String vendorKey) {

        if(isgetOrderForSelectedDateCalled){
            return;
        }

        isgetOrderForSelectedDateCalled = true;
        Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_AppOrders_and_PosOrders + "?slotdate="+dateString+"&vendorkey="+vendorKey+"&previousdaydate="+previousDateString,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        try {
                            String orderid = "",deliverytype="";
                            double discount_double=0,discountfromHashmap_double=0;
                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (int i1 =0 ; i1 < JArray.length(); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                    //   //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                                    JSONArray itemdesp;



                                    if (json.has("itemdesp")) {

                                        try{

                                            itemdesp = json.getJSONArray("itemdesp");
                                            modal_orderDetails.itemdesp = itemdesp;

                                            //Log.d(Constants.TAG, "itemdesp has been succesfully  retrived" );

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }

                                    } else {

                                        //Log.d(Constants.TAG, "There is no itemdesp: ");


                                    }

                                    if(json.has("orderid"))
                                    {
                                        try{
                                            modal_orderDetails.orderid = String.valueOf(json.get("orderid"));
                                            orderid =  String.valueOf(json.get("orderid"));
                                            //Log.d(Constants.TAG, "orderid: " + String.valueOf(json.get("orderid")));

                                        }catch (Exception e){
                                            e.printStackTrace();

                                        }

                                    }
                                    else
                                    {
                                        modal_orderDetails.orderid = "There is no orderid";
                                        //Log.d(Constants.TAG, "There is no orderid: " + String.valueOf(json.get("orderid")));


                                    }



                                    if(json.has("payableamount")){
                                        try{

                                            double newpayableamount = Double.parseDouble(String.valueOf(json.get("payableamount")));

                                            //Log.i(Constants.TAG,"Consolidated Report old oldpayableamount  "+String.valueOf(oldpayableamount));

                                            oldpayableamount = newpayableamount+oldpayableamount;
                                            //Log.i(Constants.TAG,"Consolidated Report  new payableAmountorderid            "+orderid  );

                                            //Log.i(Constants.TAG,"Consolidated Report  new payableamount   "+newpayableamount);

                                            //Log.i(Constants.TAG,"Consolidated Report  old 2 oldpayableamount  "  +oldpayableamount);
                                            //Log.i(Constants.TAG,"Consolidated Report  old 2                                          "  );

                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }



                                    if(json.has("slotname")) {
                                        try{
                                            slotname =  String.valueOf(json.get("slotname")).toUpperCase();
                                            modal_orderDetails.slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            //Log.d(Constants.TAG, "OrderType: " + slotname);

                                        }catch(Exception e ){
                                            e.printStackTrace();
                                        }

                                    }
                                    else {
                                        modal_orderDetails.slotname = "There is no slotname";
                                        //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("ordertype")));

                                    }

                                    if(json.has("ordertype")) {
                                        try{
                                            ordertype =  String.valueOf(json.get("ordertype")).toUpperCase();
                                            modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                                            //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                        }catch(Exception e ){
                                            e.printStackTrace();
                                        }

                                    }
                                    else
                                    {
                                        modal_orderDetails.ordertype = "There is no OrderType";
                                        //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                                    }
                                    if(json.has("paymentmode"))
                                    {
                                        try{
                                            modal_orderDetails.paymentmode = String.valueOf(json.get("paymentmode"));

                                        }catch(Exception e ){
                                            e.printStackTrace();
                                        }
                                        //Log.d(Constants.TAG, "PaymentMode: " + String.valueOf(json.get("paymentmode")));

                                    }
                                    else
                                    {
                                        modal_orderDetails.paymentmode = "There is no payment mode";
                                        //Log.d(Constants.TAG, "There is no PaymentMode: " + String.valueOf(json.get("ordertype")));


                                    }
                                    try {
                                        if(ordertype.equals(Constants.APPORDER)){


                                            if(json.has("deliverytype"))
                                            {
                                                try{
                                                    modal_orderDetails.deliverytype = String.valueOf(json.get("deliverytype"));
                                                    deliverytype =  String.valueOf(json.get("deliverytype"));



                                                    //Log.d(Constants.TAG, "deliverytype 1: " + String.valueOf(json.get("orderid")));

                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                    //Log.d(Constants.TAG, "deliverytype:2 " + String.valueOf(json.get("orderid")));

                                                }

                                            }
                                            else
                                            {
                                                modal_orderDetails.deliverytype = "There is no deliverytype";
                                                //Log.d(Constants.TAG, " deliverytype3: " + String.valueOf(json.get("orderid")));


                                            }


                                            if (slotname.equals(Constants.PREORDER_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        CouponDiscout = String.valueOf(json.get("coupondiscount"));
                                                        if (CouponDiscout.equals("")) {
                                                            CouponDiscout = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!couponDiscountOrderidArray.contains(orderid)) {
                                                                couponDiscountOrderidArray.add(orderid);
                                                                couponDiscount_hashmap.put(orderid, CouponDiscout);
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

                                            else if (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME) || slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {


                                                if (json.has("coupondiscount")) {
                                                    try {
                                                        modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                        CouponDiscout = String.valueOf(json.get("coupondiscount"));
                                                        if (CouponDiscout.equals("")) {
                                                            CouponDiscout = "0";
                                                        }
                                                        //Log.d(Constants.TAG, "coupondiscount" + String.valueOf(json.get("coupondiscount")));
                                                        if (!orderid.equals("")) {
                                                            if (!couponDiscountOrderidArray.contains(orderid)) {
                                                                couponDiscountOrderidArray.add(orderid);
                                                                couponDiscount_hashmap.put(orderid, CouponDiscout);
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
                                        }

                                        else{
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
                                                getItemDetailsFromItemDespArray(modal_orderDetails, ordertype, orderid);


                                            }
                                        }


                                    }catch(Exception e ){
                                        e.printStackTrace();
                                        Toast.makeText(ConsolidatedReportSubCtgywise.this,"can't Process this ItemDesp ",Toast.LENGTH_LONG).show();
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


                                try{
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

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                Adjusting_Widgets_Visibility(false);
                                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                                prepareContent();
                                setAdapter();

                               /* adapater_pos_sales_report = new Adapter_ConsolidatedSalesReport_listview(ConsolidatedReportSubCtgywise.this, Order_Item_List, OrderItem_hashmap);
                                consolidatedSalesReport_Listview.setAdapter(adapater_pos_sales_report);
                                ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);
                                scrollView.fullScroll(View.FOCUS_UP);


                                */


                                //sort_the_array_CtgyWise();
                                //  prepareContent();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                        else{
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
                            Pos_couponDiscount_hashmap.clear();
                            Pos_couponDiscountOrderidArray.clear();
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
                            dataList.clear();
                            tmcSubCtgykey.clear();

                            ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);

                            Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);

                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(ConsolidatedReportSubCtgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);


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

                        Adjusting_Widgets_Visibility(false);
                        addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                       /* adapater_pos_sales_report = new Adapter_ConsolidatedSalesReport_listview(ConsolidatedReportSubCtgywise.this, Order_Item_List, OrderItem_hashmap);
                        consolidatedSalesReport_Listview.setAdapter(adapater_pos_sales_report);
                        ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);
                        scrollView.fullScroll(View.FOCUS_UP);

                        */


                        //sort_the_array_CtgyWise();
                        prepareContent();
                         setAdapter();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                else{ Order_Item_List.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    couponDiscountOrderidArray.clear();
                    ordertypeArray.clear();
                    ordertypeHashmap.clear();
                    couponDiscount_hashmap.clear();
                    oldpayableamount = 0;
                    itemDespTotalAmount = 0;
                    Pos_couponDiscount_hashmap.clear();
                    Pos_couponDiscountOrderidArray.clear();
                    SubCtgywiseTotalArray.clear();
                    SubCtgywiseTotalHashmap.clear();
                    dataList.clear();
                    tmcSubCtgykey.clear();


                    ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);

                    addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 10000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(ConsolidatedReportSubCtgywise.this).add(jsonObjectRequest);

    }







    private void getItemDetailsFromItemDespArray(Modal_OrderDetails modal_orderDetailsfromResponse, String ordertype, String orderid) {

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        // String newOrderWeightInGrams;
        double newweight,gstAmount = 0,tmcprice=0;
        String menuitemidd = "",subCtgyKey="",itemname="",quantityString ="",tmcprice_string="";
        int quantity=0;
        try {
            JSONArray jsonArray = modal_orderDetailsfromResponse.getItemdesp();

            for(int i=0; i < jsonArray.length(); i++) {
                //Log.d(Constants.TAG, "this  jsonArray.length()" + jsonArray.length());

                JSONObject json = jsonArray.getJSONObject(i);
                //Log.d(Constants.TAG, "this json" + json.toString());

                Modal_OrderDetails modal_orderDetails_ItemDesp = new Modal_OrderDetails();

                //addOrderedItemAmountDetails(json,paymentMode);

                if (json.has("menuitemid")) {
                    menuitemidd = String.valueOf(json.get("menuitemid"));

                    modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));
                    try {
                        modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                        itemname = String.valueOf(json.get("itemname"));
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

                    if (json.has("tmcprice")) {
                        try {
                            tmcprice_string = (String.valueOf(json.get("tmcprice")));
                            tmcprice = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    if (json.has("quantity")) {

                        try {
                            quantity = Integer.parseInt(String.valueOf(json.get("quantity")));
                            quantityString =String.valueOf(json.get("quantity"));
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

                    if (json.has("marinadeitemdesp")) {
                        //Log.i(Constants.TAG, "There is  Marinade ItemDesp  ");
                        Modal_OrderDetails marinade_modal_orderDetails_ItemDesp = new Modal_OrderDetails();

                        double marinadesObjectquantity = 1, marinadesObjectgstAmount = 0, marinadesObjectpayableAmount = 0;
                        JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                        String marinadeitemmenuItemId = String.valueOf(marinadesObject.get("menuitemid"));
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
                                marinadesObjectquantity = Double.parseDouble(String.valueOf(json.get("quantity")));

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
                                marinadesubCtgyKey = String.valueOf(marinadesObject.get("tmcsubctgykey"));
                                if (marinadesubCtgyKey.equals("") || marinadesubCtgyKey.equals("0")) {
                                    marinade_modal_orderDetails_ItemDesp.tmcsubctgykey =String.valueOf("Miscellaneous");
                                    marinadesubCtgyKey = String.valueOf("Miscellaneous");
                                } else {
                                    marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf(marinadesObject.get("tmcsubctgykey"));
                                    marinadesubCtgyKey = String.valueOf(marinadesObject.get("tmcsubctgykey"));

                                }

                            } else {
                                marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                marinadesubCtgyKey = String.valueOf("Miscellaneous");
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();

                            marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                            marinadesubCtgyKey = String.valueOf("Miscellaneous");


                        }

                        marinade_modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));

                        marinade_modal_orderDetails_ItemDesp.tmcprice = (String.valueOf(marinadesObjectpayableAmount));
                        marinade_modal_orderDetails_ItemDesp.gstamount = String.valueOf(marinadesObjectgstAmount);
                        marinade_modal_orderDetails_ItemDesp.quantity = String.valueOf(json.get("quantity"));
                        marinade_modal_orderDetails_ItemDesp.itemname = marinadeitemName+" - Marinade ";
                        try {
                            if(SubCtgywiseTotalArray.contains(marinadesubCtgyKey)) {
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(marinadesubCtgyKey);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                if (isAlreadyAvailabe) {
                                    String  SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(marinadesubCtgyKey);
                                    double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                    SubCtgywisetotalDouble = SubCtgywisetotalDouble+marinadesObjectpayableAmount;
                                    SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseTotalHashmap.replace(marinadesubCtgyKey,SubCtgywisetotalString);
                                    }
                                    else{
                                        SubCtgywiseTotalHashmap.remove(marinadesubCtgyKey);
                                        SubCtgywiseTotalHashmap.put(marinadesubCtgyKey,SubCtgywisetotalString);
                                    }
                                }
                                else{
                                    SubCtgywiseTotalHashmap.put(marinadesubCtgyKey,String.valueOf(marinadesObjectpayableAmount));

                                }
                            }
                            else{
                                SubCtgywiseTotalArray.add(marinadesubCtgyKey);
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(marinadesubCtgyKey);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                if (isAlreadyAvailabe) {
                                    String  SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(marinadesubCtgyKey);
                                    double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                    SubCtgywisetotalDouble = SubCtgywisetotalDouble+marinadesObjectpayableAmount;
                                    SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseTotalHashmap.replace(marinadesubCtgyKey,SubCtgywisetotalString);
                                    }
                                    else{
                                        SubCtgywiseTotalHashmap.remove(marinadesubCtgyKey);
                                        SubCtgywiseTotalHashmap.put(marinadesubCtgyKey,SubCtgywisetotalString);
                                    }
                                }
                                else{

                                    SubCtgywiseTotalHashmap.put(marinadesubCtgyKey,String.valueOf(marinadesObjectpayableAmount));

                                }
                            }


                        }
                        catch(Exception e){
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
                                if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {
                                    double pos_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getPosSales());

                                    double pos_amount = marinadesObjectpayableAmount + pos_amount_fromhashmap;

                                    double newTotalPosAmount = pos_amount;
                                    modal_orderDetails.setPosSales(String.valueOf(newTotalPosAmount));


                                }
                                if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {
                                    double appsales_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getAppSales());
                                    double Appsales_amount = marinadesObjectpayableAmount + appsales_amount_fromhashmap;
                                    double newTotalAppSalesAmount = Appsales_amount;
                                    modal_orderDetails.setAppSales(String.valueOf(newTotalAppSalesAmount));


                                }

                            }
                              /*  else{
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                    if(ordertype.equals(Constants.POSORDER)||ordertype.equals("posorder")){
                                        double  posSales_amount = marinadesObjectpayableAmount;
                                        double Gst_posSales_amount = marinadesObjectgstAmount;
                                        double newTotalCashAmount = posSales_amount;

                                        modal_orderDetails.setPosSales(String.valueOf(decimalFormat.format(newTotalCashAmount)));


                                    }
                                    if(ordertype.equals(Constants.APPORDER)||ordertype.equals("apporder")){
                                        double  appSalesAmount = marinadesObjectpayableAmount;
                                        double Gst_app_Sales_amount = marinadesObjectgstAmount;
                                        double newTotalAppSalesAmount = appSalesAmount;

                                        modal_orderDetails.setAppSales(String.valueOf(decimalFormat.format(newTotalAppSalesAmount)));


                                    }

                                    ordertypeHashmap.put(ordertype,modal_orderDetails);
                                }

                               */
                        } else {
                            ordertypeArray.add(ordertype);
                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                            if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {
                                double posSales_amount = marinadesObjectpayableAmount;
                                double Gst_posSales_amount = marinadesObjectgstAmount;
                                double newTotalCashAmount = posSales_amount;

                                modal_orderDetails.setPosSales(String.valueOf((newTotalCashAmount)));


                            }
                            if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {
                                double appSalesAmount = marinadesObjectpayableAmount;
                                double Gst_app_Sales_amount = marinadesObjectgstAmount;
                                double newTotalAppSalesAmount = appSalesAmount;

                                modal_orderDetails.setAppSales(String.valueOf((newTotalAppSalesAmount)));


                            }

                            ordertypeHashmap.put(ordertype, modal_orderDetails);
                        }


                        if (Order_Item_List.contains(marinadeitemmenuItemId)) {
                            double payableAmount_marinade = 0, quantity_marinade = 0, gstAmount_marinade = 0;
                            Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(marinadeitemmenuItemId);
                            double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getFinalAmount());
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
                            modal_orderDetails_itemDespfrom_hashMap.setFinalAmount(String.valueOf((payableAmount_marinade)));
                            modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount_marinade)));


                        } else {
                            Order_Item_List.add(marinadeitemmenuItemId);
                            if (!tmcSubCtgykey.contains(marinadesubCtgyKey)) {
                                tmcSubCtgykey.add(marinadesubCtgyKey);
                            }
                            OrderItem_hashmap.put(marinadeitemmenuItemId, marinade_modal_orderDetails_ItemDesp);
                        }


                    } else {
                        //Log.i(Constants.TAG, "There is no Marinade ItemDesp  ");
                        if(String.valueOf(ordertype).equals(Constants.APPORDER)) {
                            if (itemname.contains("Chicken Curry Cut (Skinless)")) {
                                Log.d(Constants.TAG, "Chicken Curry log orderid" + orderid.toString());
                                Log.d(Constants.TAG, "Chicken Curry log quantityString" + quantityString.toString());
                                Log.d(Constants.TAG, "Chicken Curry log tmcprice" +  String.valueOf(tmcprice));

                                //  Log.d(Constants.TAG, "Chicken Curry log menuitemid" + String.valueOf(menuItemId));

                            }
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
                                SubCtgywiseTotalHashmap.put(subCtgyKey,String.valueOf(tmcprice));

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
                                SubCtgywiseTotalHashmap.put(subCtgyKey,String.valueOf(tmcprice));

                            }
                        }


                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }


                    if (ordertypeArray.contains(ordertype)) {
                        boolean isAlreadyAvailabe = false;

                        try {
                            isAlreadyAvailabe = checkIfPaymentdetailisAlreadyAvailableInArray(ordertype);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        if (isAlreadyAvailabe) {
                            Modal_OrderDetails modal_orderDetails = ordertypeHashmap.get(ordertype);
                            if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {
                                double pos_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getPosSales());

                                double pos_amount = tmcprice + pos_amount_fromhashmap;

                                double newTotalPosAmount = pos_amount;
                                modal_orderDetails.setPosSales(String.valueOf((newTotalPosAmount)));


                            }
                            if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {
                                double appsales_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getAppSales());
                                double Appsales_amount = tmcprice + appsales_amount_fromhashmap;
                                double newTotalAppSalesAmount = Appsales_amount;
                                modal_orderDetails.setAppSales(String.valueOf((newTotalAppSalesAmount)));


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
                    } else {
                        ordertypeArray.add(ordertype);
                        Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                        if (ordertype.equals(Constants.POSORDER) || ordertype.equals("posorder")) {

                            modal_orderDetails.setPosSales(String.valueOf((tmcprice)));


                        }
                        if (ordertype.equals(Constants.APPORDER) || ordertype.equals("apporder")) {

                            modal_orderDetails.setAppSales(String.valueOf((tmcprice)));


                        }

                        ordertypeHashmap.put(ordertype, modal_orderDetails);
                    }


                    String menuitemid = String.valueOf(json.get("menuitemid"));
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

                } else {
                    //Log.d(Constants.TAG, "this order have no menuitemId " + String.valueOf(json.get("itemname")));
                    Adjusting_Widgets_Visibility(false);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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




    private void addOrderedItemAmountDetails(List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        double totalAmountWithhGst = 0;
        double discountAmount = 0;
        double pos_discountAmount = 0;

        double GST = 0;
        double totalAmount = 0;
        double posorder_Amount = 0;
        double apporder_Amount = 0;


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



            for (int i = 0; i < order_item_list.size(); i++) {
                String menuItemId = order_item_list.get(i);
                Modal_OrderDetails modal_orderDetails_amountDetails = orderItem_hashmap.get(menuItemId);
                double totalAmountWithhGst_from_array = Double.parseDouble(modal_orderDetails_amountDetails.getTmcprice());
                totalAmountWithhGst = totalAmountWithhGst + totalAmountWithhGst_from_array;
                double Gst_from_array = Double.parseDouble(modal_orderDetails_amountDetails.getGstamount());
                GST = GST + Gst_from_array;
                String ordertype = (modal_orderDetails_amountDetails.getOrdertype());





            }
            for(String ordertype :ordertypeArray){
                Modal_OrderDetails modal_orderDetails = ordertypeHashmap.get(ordertype);
                try {
                    if ((ordertype.toUpperCase().equals(Constants.POSORDER)) || (ordertype.equals("posorder"))) {
                        posorder_Amount = Double.parseDouble(((Objects.requireNonNull(modal_orderDetails).getPosSales())));
                        ////Log.i(Constants.TAG,"Consolidated Report  new posorder_Amount   " +posorder_Amount);
                        posorder_Amount = posorder_Amount-pos_discountAmount;
                    }

                    if ((ordertype.toUpperCase().equals(Constants.APPORDER))||(ordertype.equals("apporder"))) {
                        apporder_Amount = Double.parseDouble((Objects.requireNonNull(modal_orderDetails).getAppSales()));
                        apporder_Amount = apporder_Amount-discountAmount;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            double totalAmountWithoutGst = totalAmountWithhGst-GST;
            discountAmount = discountAmount+pos_discountAmount;
            double totalAmt_with_CouponDiscount_double = totalAmountWithoutGst-discountAmount;
            double totalAmt_with_CouponDiscount__Gstdouble = totalAmountWithhGst-discountAmount;
            totalAmt_without_GST.setText(String.valueOf(decimalFormat.format(totalAmountWithoutGst)));

            //Log.i(Constants.TAG, "Consolidated Report  new totalAmt_with_CouponDiscount__Gstdouble   " + String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__Gstdouble)));


            totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format(discountAmount)));
            totalAmt_with_CouponDiscount.setText(String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount_double)));
            totalGST_Amt.setText(String.valueOf(decimalFormat.format(GST)));
            final_sales.setText(String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__Gstdouble)));
            appsales.setText(String.valueOf(decimalFormat.format(apporder_Amount)));
            possales.setText(String.valueOf(decimalFormat.format(posorder_Amount)));
            totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__Gstdouble)));

            finalBillDetails.add("TOTAL : ");
            FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithoutGst)));
            finalBillDetails.add("DISCOUNT : ");
            FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(decimalFormat.format(discountAmount)));
            finalBillDetails.add("SUBTOTAL : ");
            FinalBill_hashmap.put("SUBTOTAL : ", String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount_double)));
            finalBillDetails.add("GST : ");
            FinalBill_hashmap.put("GST : ", String.valueOf(decimalFormat.format(GST)));
            finalBillDetails.add("FINAL SALES : ");
            FinalBill_hashmap.put("FINAL SALES : ", String.valueOf(decimalFormat.format(totalAmt_with_CouponDiscount__Gstdouble)));
            //   sort_list_tmcSubCtgyWise(Order_Item_List, OrderItem_hashmap);
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    }

    private void getTmcSubCtgyList(String vendorKey){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofSubCtgy,null,
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

                                    String subCtgyKey  = String.valueOf(json.get("key"));
                                    //Log.d(Constants.TAG, "subCtgyKey: " + subCtgyKey);
                                    String subCtgyName = String.valueOf(json.get("subctgyname"));
                                    //Log.d(Constants.TAG, "subCtgyName: " + subCtgyName);
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


    @Override
    protected void onStart() {
        super.onStart();
        getTmcSubCtgyList(vendorKey);

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
                //    Log.d(Constants.TAG, "SubCtgykey " + SubCtgykey);

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
                    // Log.d(Constants.TAG, "before for " + e.getMessage());

                }

                try {
                  Order_Item_List = getSortedIdFromHashMap(Order_Item_List,OrderItem_hashmap);
                }
                catch(Exception e){
                    e.printStackTrace();
                }




                for (int j = 0; j < Order_Item_List.size(); j++) {
                    menuid = Order_Item_List.get(j);
                    //  Log.d(Constants.TAG, "SubCtgykey menuid " + menuid);
                    //  Log.d(Constants.TAG, "SubCtgykey w " + SubCtgykey);
                    Log.d(Constants.TAG, "menuitemid:hash4 :"+"#"+String.valueOf(menuid)+"#");

                    Modal_OrderDetails itemDetailsfromHashmap = OrderItem_hashmap.get(menuid);
                    // Log.d(Constants.TAG, "SubCtgykey itemDetailsfromHashmap " + itemDetailsfromHashmap.getItemname());
                    String subCtgyKey_fromHashmap ="";
                    try {
                        subCtgyKey_fromHashmap = itemDetailsfromHashmap.getTmcsubctgykey();
                    }
                    catch(Exception e){
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<String> getSortedIdFromHashMap(List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {
       order_item_list.clear();
        order_item_list.addAll(orderItem_hashmap.keySet());
        return order_item_list;
    }

    private void setAdapter() {
        try {
            Adapter_Pos_Sales_Report  adapter = new Adapter_Pos_Sales_Report(ConsolidatedReportSubCtgywise.this, dataList);
            consolidatedSalesReport_Listview.setAdapter(adapter);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {

            ReportListviewSizeHelper.getListViewSize(consolidatedSalesReport_Listview, screenInches);
            scrollView.fullScroll(View.FOCUS_UP);
        }
        catch(Exception e){
            e.printStackTrace();
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
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }

    }
    private String getMonthString(int value) {
        if (value == 0) {
            return "Jan";
        } else if (value == 1) {
            return "Feb";
        } else if (value ==2) {
            return "Mar";
        } else if (value ==3) {
            return "Apr";
        } else if (value ==4) {
            return "May";
        } else if (value ==5) {
            return "Jun";
        } else if (value ==6) {
            return "Jul";
        } else if (value ==7) {
            return "Aug";
        } else if (value ==8) {
            return "Sep";
        } else if (value ==9) {
            return "Oct";
        } else if (value ==10) {
            return "Nov";
        } else if (value ==11) {
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
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, -1);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }



    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat dayname = new SimpleDateFormat("EEE");
        String Currentday = dayname.format(c);




        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDate = df.format(c);
        String date = Currentday+", "+CurrentDate;


        return date;
    }





    private String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        String  CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }
    public void exportReport() {
        if ((Order_Item_List == null) || (Order_Item_List.size() <= 0)) { return; }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state "+state+" extstoragedir "+extstoragedir);
        File fol = new File(extstoragedir, "testpdf");
        File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "Consolidated Sales Report_" + System.currentTimeMillis()  +".pdf";
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

            String Vendorname  = sharedPreferences.getString("VendorName","");

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
            String rsunit = "Rs.",tmcprice;
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
            for (String SubCtgykey : tmcSubCtgykey) {
                int i_value = 0;
                String subCtgyTotal ="0";
                double subCtgyTotaldouble = 0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");

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

                    try{
                        subCtgyTotal = SubCtgywiseTotalHashmap.get(SubCtgykey);
                        subCtgyTotaldouble = Double.parseDouble(subCtgyTotal);
                        subCtgyTotal = decimalFormat.format(subCtgyTotaldouble);
                    }
                    catch (Exception e){
                        subCtgyTotal= "0";
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
                itemqtycell = new PdfPCell(new Phrase("" +Quantity ));
                itemqtycell.setBorder(Rectangle.BOTTOM);
                itemqtycell.setBorderColor(BaseColor.LIGHT_GRAY);
                itemqtycell.setMinimumHeight(30);
                itemqtycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                itemqtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                double totalval = Double.parseDouble( itemRow.getTmcprice());
                tmcprice =  decimalFormat.format(totalval);
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

            double posorder_Amount = 0;
            double apporder_Amount = 0;
            String Payment_Amount = "0";
            double discountAmount = 0;
            for (String orderid : couponDiscountOrderidArray) {
                String Discount_amount = couponDiscount_hashmap.get(orderid);
                double CouponDiscount_double = Double.parseDouble(Discount_amount);
                discountAmount = discountAmount + CouponDiscount_double;

            }
            for(String ordertype :ordertypeArray){

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

                    if ((ordertype.toUpperCase().equals(Constants.APPORDER))||(ordertype.equals("apporder"))) {
                        apporder_Amount = Double.parseDouble(decimalFormat.format(Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getAppSales())));
                        apporder_Amount = apporder_Amount-discountAmount;
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
                }
                catch (Exception e){
                    e.printStackTrace();
                }













            }
            layoutDocument.add(tablePaymentMode);




            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(20);
            PdfPCell emptycell; PdfPCell emptycellone; PdfPCell emptycelltwo;
            for (int i=0; i<finalBillDetails.size(); i++) {
                String key = finalBillDetails.get(i);
                String value= FinalBill_hashmap.get(key);
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