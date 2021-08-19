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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Pos_OrderDetailsScreen;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
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

public class DeliveryPartnerSettlementReport extends AppCompatActivity {
    Spinner deliveryPartnerSelectionSpinner;
    private ArrayList<Modal_DeliveryPartner> deliveryPartner_arrayList;
    LinearLayout orderDetailsLayout,PrintReport_Layout,viewOrdersList_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel,getOrdersList_Layout;
    DatePickerDialog datepicker;
    TextView totaldeliveredOrdersCount,totalOrdersCount,preorder_cashOnDelivery,preorder_Phonepe,preorder_Razorpay,preorder_paytmSales;
    TextView Phonepe,totalSales_headingText,Razorpay,Paytm, cashOnDelivery,upiSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;

    TextView deliveryChargeAmount_textwidget,deliverypartnerName_textwidget,cashSales, cardSales,ordersInstruction;
    String vendorKey,CurrentDay_date,assignedOrdersString="";
    double screenInches;
    String CurrentDate;
    String DateString;
    List<Modal_DeliveryPartner> assignedOrdersList;


    public static List<String> OrderIdCount;
    public static List<String> delivered_OrderIdCount;


    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;


    public static List<String> paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  paymentModeHashmap  = new HashMap();;

    public static List<String> preorder_paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  preorder_paymentModeHashmap  = new HashMap();;


    public static List<String> preorderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  preorderpaymentMode_DiscountHashmap  = new HashMap();;


    public static List<String> preorderpaymentMode_DeliveryChargeOrderid;
    public static HashMap<String, Modal_OrderDetails> preorderpaymentMode_DeliveryChargeHashmap = new HashMap();
    



    public static List<String> paymentMode_DeliveryChargeOrderid;
    public static HashMap<String, Modal_OrderDetails> paymentMode_DeliveryChargeHashmap = new HashMap();
    
    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();


    public static List<String> paymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  paymentMode_DiscountHashmap  = new HashMap();;


    double CouponDiscount=0,Gst_from_array =0 ,CouponDiscount_preorder = 0,deliveryCharges=0,deliveryCharges_preorder=0,totalDeliveryCharges=0,totalCouponDiscount=0;

    private JSONArray result = new JSONArray();
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    String deliveryPartnerStatus,deliveryPartnerKey,deliveryPartnerMobileNo,deliveryPartnerName;
    String finalCashAmount_pdf,finalRazorpayAmount_pdf,finalPhonepeAmount_pdf,finalPaytmAmount_pdf;
    String finalpreorderCashAmount_pdf,finalpreorderRazorpayAmount_pdf,finalpreorderPhonepeAmount_pdf,finalpreorderPaytmAmount_pdf;

    boolean isSpinnerClicked = false;
    public String DeliveryPersonList;
    public static JSONArray result_JArray =new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_partner_settlement_report_activity);

        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        deliveryPartnerSelectionSpinner = findViewById(R.id.deliveryPartnerSelectionSpinner);
        deliveryPartner_arrayList=new ArrayList<>();

        loadingPanel=findViewById(R.id.loadingPanel);

        loadingpanelmask = findViewById(R.id.loadingpanelmask);

        deliverypartnerName_textwidget = findViewById(R.id.deliverypartnerName_textwidget);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        viewOrdersList_Layout = findViewById(R.id.viewOrdersList_Layout);
        PrintReport_Layout = findViewById(R.id.PrintReport_Layout);
        totalAmt_without_GST = findViewById(R.id.totalAmt_without_GST);
        totalCouponDiscount_Amt = findViewById(R.id.totalCouponDiscount_Amt);
        totalAmt_with_CouponDiscount = findViewById(R.id.totalAmt_with_CouponDiscount);
        totalGST_Amt = findViewById(R.id.totalGST_Amt);
        final_sales = findViewById(R.id.final_sales);
        cashSales = findViewById(R.id.cashSales);
        cardSales = findViewById(R.id.cardSales);
        upiSales  = findViewById(R.id.upiSales);
        totalSales_headingText = findViewById(R.id.totalSales_headingText);
        totalOrdersCount = findViewById(R.id.totalOrdersCount);
        totaldeliveredOrdersCount = findViewById(R.id.totaldeliveredOrdersCount);
        cashOnDelivery = findViewById(R.id.cashOnDelivery);
        Razorpay = findViewById(R.id.Razorpay);
        Paytm  = findViewById(R.id.paytmSales);
        Phonepe  = findViewById(R.id.Phonepe);
        getOrdersList_Layout=findViewById(R.id.getOrdersList_Layout);
        ordersInstruction = findViewById(R.id.ordersInstruction);
        orderDetailsLayout = findViewById(R.id.orderDetailsLayout);
        totalAmt_without_GST = findViewById(R.id.totalAmt_without_GST);
        totalCouponDiscount_Amt = findViewById(R.id.totalCouponDiscount_Amt);
        totalAmt_with_CouponDiscount = findViewById(R.id.totalAmt_with_CouponDiscount);
        totalGST_Amt = findViewById(R.id.totalGST_Amt);
        final_sales = findViewById(R.id.final_sales);
        totalSales_headingText = findViewById(R.id.totalSales_headingText);
        deliveryChargeAmount_textwidget = findViewById(R.id.deliveryChargeAmount_textwidget);


        preorder_cashOnDelivery = findViewById(R.id.preorder_cashOnDelivery);
        preorder_Phonepe  = findViewById(R.id.preorder_Phonepe);
        preorder_Razorpay = findViewById(R.id.preorder_Razorpay);
        preorder_paytmSales = findViewById(R.id.preorder_paytmSales);
        delivered_OrderIdCount = new ArrayList<>();
        OrderIdCount = new ArrayList<>();
        assignedOrdersList = new ArrayList<>();
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        paymentModeArray = new ArrayList<>();
        preorder_paymentModeArray = new ArrayList<>();
        preorderpaymentMode_DiscountOrderid = new ArrayList<>();
        paymentMode_DiscountOrderid = new ArrayList<>();
        preorderpaymentMode_DeliveryChargeOrderid = new ArrayList<>();
        paymentMode_DeliveryChargeOrderid = new ArrayList<>();

        OrderIdCount.clear();
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
        preorderpaymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountHashmap.clear();

        paymentMode_DeliveryChargeHashmap.clear();
        paymentMode_DeliveryChargeOrderid.clear();

        preorderpaymentMode_DeliveryChargeOrderid.clear();
        preorderpaymentMode_DeliveryChargeHashmap.clear();

        SharedPreferences sh
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);


        vendorKey = sh.getString("VendorKey","");

        getVendorwiseDeliveryPartner();
        try {
            SharedPreferences shared2 = getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
            DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));

          //  ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        CurrentDay_date =getDay_Date_and_time();
        CurrentDate = getDay_Date_and_time();
        dateSelector_text.setText(CurrentDate);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);

        DateString= getDay_Date_and_time();

        dateSelector_text.setText(CurrentDate);

        orderDetailsLayout .setVisibility( View.GONE);
        ordersInstruction.setVisibility(View.VISIBLE);
        getOrdersList_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delivered_OrderIdCount.clear();
                OrderIdCount.clear();
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
                preorderpaymentMode_DiscountOrderid.clear();
                preorderpaymentMode_DiscountHashmap.clear();
                paymentMode_DeliveryChargeHashmap.clear();
                paymentMode_DeliveryChargeOrderid.clear();

                preorderpaymentMode_DeliveryChargeOrderid.clear();
                preorderpaymentMode_DeliveryChargeHashmap.clear();
                deliveryCharges=0;
                deliveryCharges_preorder=0;
                CouponDiscount = 0;
                CouponDiscount_preorder = 0;
                getOrderForSelectedDateandSelectedDeliveryPartner(DateString, vendorKey,deliveryPartnerKey,deliveryPartnerMobileNo);

                addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap,OrderIdCount);
                orderDetailsLayout .setVisibility( View.VISIBLE);
                ordersInstruction.setVisibility(View.GONE);

            }
        });

        deliveryPartnerSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                    deliveryPartnerStatus = getVendorData(position, "status");
                    deliveryPartnerKey = getVendorData(position, "key");
                    deliveryPartnerMobileNo = getVendorData(position, "mobileno");
                    deliveryPartnerName = getVendorData(position, "name");
                    deliverypartnerName_textwidget.setText(deliveryPartnerName);
                    OrderIdCount.clear();
                    delivered_OrderIdCount.clear();
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
                    preorderpaymentMode_DiscountOrderid.clear();
                    preorderpaymentMode_DiscountHashmap.clear();
                    paymentMode_DeliveryChargeHashmap.clear();
                    paymentMode_DeliveryChargeOrderid.clear();
    
                    preorderpaymentMode_DeliveryChargeOrderid.clear();
                    preorderpaymentMode_DeliveryChargeHashmap.clear();
                    deliveryCharges=0;
                    deliveryCharges_preorder=0;
                        
                    
                    CouponDiscount = 0;
                    CouponDiscount_preorder = 0;
                     //      getOrderForSelectedDateandSelectedDeliveryPartner(DateString, vendorKey,deliveryPartnerKey,deliveryPartnerMobileNo);

                  //       addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap,OrderIdCount);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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
        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeliveryPartnerSettlementReport.this,"Loading.... Please Wait",Toast.LENGTH_SHORT).show();
            }
        });
        PrintReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screenInches>8){
                    // printReport();
                }
                else{
                    Toast.makeText(DeliveryPartnerSettlementReport.this,"Cant Find a Printer",Toast.LENGTH_LONG).show();
                }
            }
        });





        if(screenInches>8){
            viewOrdersList_Layout.setVisibility(View.GONE);
        }

        viewOrdersList_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screenInches>8){
                    Toast.makeText(DeliveryPartnerSettlementReport.this,"Please use Partner app in your mobile phone to see the Order Details ",Toast.LENGTH_SHORT).show();

                }
                else{
                    if((!assignedOrdersString.equals(""))&&OrderItem_hashmap.size()>0&&Order_Item_List.size()>0){
                        Intent intent = new Intent(DeliveryPartnerSettlementReport.this, GetDeliverypartnersAssignedOrders.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("deliveryPartnerMobileNo", deliveryPartnerMobileNo);

                        bundle.putString("assignedOrdersString", assignedOrdersString);

                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(DeliveryPartnerSettlementReport.this,"No Order has been assigned for this Partner on requested Date",Toast.LENGTH_SHORT).show();
                    }

                }



                /*int writeExternalStoragePermission = ContextCompat.checkSelfPermission(DeliveryPartnerSettlementReport.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission " + writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(DeliveryPartnerSettlementReport.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    try {
                        if(screenInches<8){
                            Adjusting_Widgets_Visibility(true);

                            exportReport();
                        }
                        else{
                            Toast.makeText(DeliveryPartnerSettlementReport.this,"Cant Find a Pdf reader",Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                }

                 */
            }
        });




    }


/*
    @Override
    protected void onResume() {
        super.onResume();
        getVendorwiseDeliveryPartner();
        OrderIdCount.clear();
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
        preorderpaymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountHashmap.clear();
        CouponDiscount = 0;
        CouponDiscount_preorder =0;

        //  addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap,OrderIdCount);



    }

 */





    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(DeliveryPartnerSettlementReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
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
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                         //   getOrderForSelectedDateandSelectedDeliveryPartner(DateString, vendorKey,deliveryPartnerKey,deliveryPartnerMobileNo);
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();
    }

    private void getOrderForSelectedDateandSelectedDeliveryPartner(String dateString, String vendorKey, String deliveryPartnerKey, String deliveryPartnerMobileNo) {
        isSpinnerClicked = false;
        OrderIdCount.clear();
        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        paymentModeHashmap.clear();
        paymentModeArray.clear();
        delivered_OrderIdCount.clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();
        preorder_paymentModeHashmap.clear();
        preorder_paymentModeArray.clear();
        preorderpaymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountHashmap.clear();
        CouponDiscount=0;
        CouponDiscount_preorder =0;
        paymentMode_DeliveryChargeHashmap.clear();
        paymentMode_DeliveryChargeOrderid.clear();

        preorderpaymentMode_DeliveryChargeOrderid.clear();
        preorderpaymentMode_DeliveryChargeHashmap.clear();
        deliveryCharges=0;
        deliveryCharges_preorder=0;
        delivered_OrderIdCount.clear();
        addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap,OrderIdCount);
        Adjusting_Widgets_Visibility(true);

        String deliveryUserMobileNumberEncoded  = deliveryPartnerMobileNo;
        try {
            deliveryUserMobileNumberEncoded = URLEncoder.encode(deliveryPartnerMobileNo, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_assignedorderdetailsfordeliveryuser+"?deliveryusermobileno="+deliveryUserMobileNumberEncoded+"&date="+dateString+"&vendorkey="+vendorKey,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            //converting jsonSTRING into arrayco
                            result_JArray = response.getJSONArray("content");
                            assignedOrdersString = response.toString();

                            ConvertjsonToArray(result_JArray);



                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);
                            Toast.makeText(DeliveryPartnerSettlementReport.this, "No Order has delivered On this Date ", Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);
                            OrderIdCount.clear();
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
                            preorderpaymentMode_DiscountOrderid.clear();
                            preorderpaymentMode_DiscountHashmap.clear();
                            CouponDiscount=0;
                            CouponDiscount_preorder =0;
                            delivered_OrderIdCount.clear();
                            paymentMode_DeliveryChargeHashmap.clear();
                            paymentMode_DeliveryChargeOrderid.clear();

                            preorderpaymentMode_DeliveryChargeOrderid.clear();
                            preorderpaymentMode_DeliveryChargeHashmap.clear();
                            deliveryCharges=0;
                            deliveryCharges_preorder=0;

                            addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap, OrderIdCount);
                            //getOrderForSelectedDate(DateString, vendorKey);

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
                Toast.makeText(DeliveryPartnerSettlementReport.this, "No Order has delivered On this Date ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                Adjusting_Widgets_Visibility(false);
                OrderIdCount.clear();
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
                preorderpaymentMode_DiscountOrderid.clear();
                preorderpaymentMode_DiscountHashmap.clear();
                delivered_OrderIdCount.clear();
                paymentMode_DeliveryChargeHashmap.clear();
                paymentMode_DeliveryChargeOrderid.clear();

                preorderpaymentMode_DeliveryChargeOrderid.clear();
                preorderpaymentMode_DeliveryChargeHashmap.clear();
                deliveryCharges=0;
                deliveryCharges_preorder=0;
                //getOrderForSelectedDate(DateString, vendorKey);

                addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap, OrderIdCount);

                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", vendorKey);
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
        Volley.newRequestQueue(DeliveryPartnerSettlementReport.this).add(jsonObjectRequest);




    }

    @Override
    protected void onResume() {
        super.onResume();


        if(result_JArray.length()>0) {
            Adjusting_Widgets_Visibility(true);
            delivered_OrderIdCount.clear();
            OrderIdCount.clear();
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
            preorderpaymentMode_DiscountOrderid.clear();
            preorderpaymentMode_DiscountHashmap.clear();
            CouponDiscount=0;
            CouponDiscount_preorder =0;
            paymentMode_DeliveryChargeHashmap.clear();
            paymentMode_DeliveryChargeOrderid.clear();

            preorderpaymentMode_DeliveryChargeOrderid.clear();
            preorderpaymentMode_DeliveryChargeHashmap.clear();
            deliveryCharges=0;
            deliveryCharges_preorder=0;
            ConvertjsonToArray(result_JArray);
        }
    }

    public void ConvertjsonToArray(JSONArray JArray) {
        String paymentMode = "", ordertype = "", orderid = "", slotname = "",orderstatus ="";

        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
        int i1 = 0;
        int arrayLength = JArray.length();
        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
        Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

        if(arrayLength>0){

            for (; i1 < (arrayLength); i1++) {

                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                    //  //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderstatus")));
                    JSONArray itemdesp;

                    if (json.has("ordertype")) {
                        try {
                            modal_orderDetails.ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                            ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                            //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        modal_orderDetails.ordertype = "There is no OrderType";
                        //Log.d(Constants.TAG, "There is no OrderType: " + String.valueOf(json.get("ordertype")));


                    }

                    if (json.has("orderstatus")) {
                        try {
                            modal_orderDetails.orderstatus = String.valueOf(json.get("orderstatus")).toUpperCase();
                            orderstatus = String.valueOf(json.get("orderstatus")).toUpperCase();
                            //Log.d(Constants.TAG, "orderstatus: " + String.valueOf(json.get("orderstatus")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        modal_orderDetails.orderstatus = "There is no orderstatus";
                        //Log.d(Constants.TAG, "There is no orderstatus: " + String.valueOf(json.get("orderstatus")));


                    }

                    if (json.has("slotname")) {
                        try {
                            modal_orderDetails.slotname = String.valueOf(json.get("slotname")).toUpperCase();
                            slotname = String.valueOf(json.get("slotname")).toUpperCase();
                            //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("slotname")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        modal_orderDetails.slotname = "There is no slotname";
                        //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("slotname")));


                    }

                    if (json.has("paymentmode")) {

                        try {
                            paymentMode = String.valueOf(json.get("paymentmode"));
                            modal_orderDetails.paymentmode = String.valueOf(json.get("paymentmode"));
                            //Log.d(Constants.TAG, "PaymentMode: " + String.valueOf(json.get("paymentmode")));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        modal_orderDetails.paymentmode = "There is no payment mode";
                        //Log.d(Constants.TAG, "There is no PaymentMode: " + String.valueOf(json.get("ordertype")));


                    }


                    if (json.has("itemdesp")) {

                        try {

                            itemdesp = json.getJSONArray("itemdesp");
                            modal_orderDetails.itemdesp = itemdesp;

                            //Log.d(Constants.TAG, "itemdesp has been succesfully  retrived");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        //Log.d(Constants.TAG, "There is no itemdesp: ");


                    }
                    if (json.has("orderid")) {
                        try {


                            orderid = String.valueOf(json.get("orderid"));
                            modal_orderDetails.orderid = String.valueOf(json.get("orderid"));
                            if (OrderIdCount.contains(orderid)) {
                                //Log.d(Constants.TAG, "orderid is already added");
                                Log.d(Constants.TAG, "orderid is already added");

                            } else {
                                OrderIdCount.add(orderid);
                                Log.d(Constants.TAG, "orderid has been succesfully  retrived  " + OrderIdCount.size());

                            }

                            //Log.d(Constants.TAG, "orderid has been succesfully  retrived");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        //Log.d(Constants.TAG, "There is no orderid: ");
                        Log.d(Constants.TAG, "There is no orderid: ");


                    }





                    if(orderstatus.equals(Constants.DELIVERED_ORDER_STATUS)) {

                        if (slotname.equals(Constants.PREORDER_SLOTNAME)) {


                        if (json.has("deliveryamount")) {

                            modal_orderDetails.deliveryamount = String.valueOf(json.get("deliveryamount"));
                            try {
                                String deliveryCharges_preorder_string = String.valueOf(json.get("deliveryamount"));
                                try {
                                    if (deliveryCharges_preorder_string.equals("")) {
                                        deliveryCharges_preorder_string = "0";

                                        double deliveryCharges_preorder_double = Double.parseDouble(deliveryCharges_preorder_string);
                                        deliveryCharges_preorder = deliveryCharges_preorder + deliveryCharges_preorder_double;

                                        if (!preorderpaymentMode_DeliveryChargeOrderid.contains(orderid)) {
                                            preorderpaymentMode_DeliveryChargeOrderid.add(orderid);
                                            boolean isAlreadyAvailable = false;
                                            try {
                                                isAlreadyAvailable = checkIfpreorderPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(paymentMode);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                ;
                                            }
                                            if (isAlreadyAvailable) {
                                                Modal_OrderDetails modal_orderDetails1 = preorderpaymentMode_DeliveryChargeHashmap.get(paymentMode);
                                                String DeliveryCharge = modal_orderDetails1.getDeliveryamount();
                                                double DeliveryCharge_doublefromArray = Double.parseDouble(DeliveryCharge);
                                                double DeliveryCharge_double = Double.parseDouble(deliveryCharges_preorder_string);

                                                DeliveryCharge_double = DeliveryCharge_double + DeliveryCharge_doublefromArray;
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(DeliveryCharge_double));
                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryCharges_preorder_string));
                                                preorderpaymentMode_DeliveryChargeHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                        } else {
                                            //Log.d(Constants.TAG, "mode already availabe");

                                        }
                                    } else {

                                        double deliveryCharges_preorder_double = Double.parseDouble(deliveryCharges_preorder_string);
                                        deliveryCharges_preorder = deliveryCharges_preorder + deliveryCharges_preorder_double;


                                        if (!preorderpaymentMode_DeliveryChargeOrderid.contains(orderid)) {
                                            preorderpaymentMode_DeliveryChargeOrderid.add(orderid);
                                            boolean isAlreadyAvailable = checkIfpreorderPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(paymentMode);
                                            if (isAlreadyAvailable) {
                                                Modal_OrderDetails modal_orderDetails1 = preorderpaymentMode_DeliveryChargeHashmap.get(paymentMode);
                                                String DeliveryCharge = modal_orderDetails1.getDeliveryamount();
                                                double DeliveryCharge_doublefromArray = Double.parseDouble(DeliveryCharge);
                                                double DeliveryCharge_double = Double.parseDouble(deliveryCharges_preorder_string);

                                                DeliveryCharge_double = DeliveryCharge_double + DeliveryCharge_doublefromArray;
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(DeliveryCharge_double));
                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryCharges_preorder_string));
                                                preorderpaymentMode_DeliveryChargeHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                            //Log.d(Constants.TAG, "mode already availabe");


                                        } else {
                                            //Log.d(Constants.TAG, "mode already availabe");

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
                            String deliveryCharges_preorder_string = String.valueOf("0");
                            double DeliveryCharge_double = Double.parseDouble(deliveryCharges_preorder_string);

                            deliveryCharges_preorder = deliveryCharges_preorder + DeliveryCharge_double;


                            modal_orderDetails.deliveryamount = "There is no deliveryCharges_preorder";

                        }


                        if (json.has("coupondiscount")) {

                            modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                            try {
                                String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                try {
                                    if (couponDiscount_string.equals("")) {
                                        couponDiscount_string = "0";

                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                        CouponDiscount_preorder = CouponDiscount_preorder + CouponDiscount_double;

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
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount" + discountAmount);
                                                }
                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount discountAmount_double" + String.valueOf(discountAmount_double));
                                                }
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 1" + String.valueOf(discountAmount_double));
                                                }
                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 2" + String.valueOf(couponDiscount_string));
                                                }
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                preorderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                        } else {
                                            //Log.d(Constants.TAG, "orderid already availabe");

                                        }
                                    } else {

                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                        CouponDiscount_preorder = CouponDiscount_preorder + CouponDiscount_double;

                                        if (paymentMode.equals("PAYTM")) {
                                            //Log.i("TAG", "discountAmount 3 CouponDiscount_double" + String.valueOf(CouponDiscount_double));
                                        }

                                        if (paymentMode.equals("PAYTM")) {
                                            //Log.i("TAG", "discountAmount 3.1 CouponDiscount" + String.valueOf(CouponDiscount));
                                        }
                                        if (!preorderpaymentMode_DiscountOrderid.contains(orderid)) {
                                            preorderpaymentMode_DiscountOrderid.add(orderid);
                                            boolean isAlreadyAvailable = checkIfpreorderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                            if (isAlreadyAvailable) {
                                                Modal_OrderDetails modal_orderDetails1 = preorderpaymentMode_DiscountHashmap.get(paymentMode);
                                                String discountAmount = modal_orderDetails1.getDiscountAmount();
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 4 " + String.valueOf(discountAmount));
                                                }

                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);
                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount discountAmount_double" + String.valueOf(discountAmount_double));
                                                }

                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 2" + String.valueOf(couponDiscount_string));
                                                }
                                                preorderpaymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                            //Log.d(Constants.TAG, "mode already availabe");


                                        } else {
                                            //Log.d(Constants.TAG, "orderid already availabe");

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

                            CouponDiscount_preorder = CouponDiscount_preorder + CouponDiscount_double;


                            modal_orderDetails.coupondiscount = "There is no coupondiscount";

                        }

                    }


                    if (((slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) || (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME))))
                    {

                        if (json.has("deliveryamount")) {

                            modal_orderDetails.deliveryamount = String.valueOf(json.get("deliveryamount"));
                            try {
                                String deliveryCharges_string = String.valueOf(json.get("deliveryamount"));
                                try {
                                    if (deliveryCharges_string.equals("")) {
                                        deliveryCharges_string = "0";

                                        double deliveryCharges_double = Double.parseDouble(deliveryCharges_string);
                                        deliveryCharges = deliveryCharges + deliveryCharges_double;

                                        if (!paymentMode_DeliveryChargeOrderid.contains(orderid)) {
                                            paymentMode_DeliveryChargeOrderid.add(orderid);
                                            boolean isAlreadyAvailable = false;
                                            try {
                                                isAlreadyAvailable = checkIfPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(paymentMode);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                ;
                                            }
                                            if (isAlreadyAvailable) {
                                                Modal_OrderDetails modal_orderDetails1 = paymentMode_DeliveryChargeHashmap.get(paymentMode);
                                                String DeliveryCharge = modal_orderDetails1.getDeliveryamount();
                                                double DeliveryCharge_doublefromArray = Double.parseDouble(DeliveryCharge);
                                                double DeliveryCharge_double = Double.parseDouble(deliveryCharges_string);

                                                DeliveryCharge_double = DeliveryCharge_double + DeliveryCharge_doublefromArray;
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(DeliveryCharge_double));
                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryCharges_string));
                                                paymentMode_DeliveryChargeHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                        } else {
                                            //Log.d(Constants.TAG, "mode already availabe");

                                        }
                                    } else {

                                        double deliveryCharges_double = Double.parseDouble(deliveryCharges_string);
                                        deliveryCharges = deliveryCharges + deliveryCharges_double;


                                        if (!paymentMode_DeliveryChargeOrderid.contains(orderid)) {
                                            paymentMode_DeliveryChargeOrderid.add(orderid);
                                            boolean isAlreadyAvailable = checkIfPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(paymentMode);
                                            if (isAlreadyAvailable) {
                                                Modal_OrderDetails modal_orderDetails1 = paymentMode_DeliveryChargeHashmap.get(paymentMode);
                                                String DeliveryCharge = modal_orderDetails1.getDeliveryamount();
                                                double DeliveryCharge_doublefromArray = Double.parseDouble(DeliveryCharge);
                                                double DeliveryCharge_double = Double.parseDouble(deliveryCharges_string);

                                                DeliveryCharge_double = DeliveryCharge_double + DeliveryCharge_doublefromArray;
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(DeliveryCharge_double));
                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryCharges_string));
                                                paymentMode_DeliveryChargeHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                            //Log.d(Constants.TAG, "mode already availabe");


                                        } else {
                                            //Log.d(Constants.TAG, "mode already availabe");

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
                            String deliveryCharges_string = String.valueOf("0");
                            double DeliveryCharge_double = Double.parseDouble(deliveryCharges_string);

                            deliveryCharges = deliveryCharges + DeliveryCharge_double;


                            modal_orderDetails.deliveryamount = "There is no deliveryCharges";

                        }




                        if (json.has("coupondiscount")) {

                            modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                            try {
                                String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                try {
                                    if (couponDiscount_string.equals("")) {
                                        couponDiscount_string = "0";

                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                        CouponDiscount= CouponDiscount + CouponDiscount_double;

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
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount" + discountAmount);
                                                }
                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount discountAmount_double" + String.valueOf(discountAmount_double));
                                                }
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 1" + String.valueOf(discountAmount_double));
                                                }
                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 2" + String.valueOf(couponDiscount_string));
                                                }
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                paymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                        } else {
                                            //Log.d(Constants.TAG, "orderid already availabe");

                                        }
                                    } else {

                                        double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                        CouponDiscount = CouponDiscount + CouponDiscount_double;

                                        if (paymentMode.equals("PAYTM")) {
                                            //Log.i("TAG", "discountAmount 3 CouponDiscount_double" + String.valueOf(CouponDiscount_double));
                                        }

                                        if (paymentMode.equals("PAYTM")) {
                                            //Log.i("TAG", "discountAmount 3.1 CouponDiscount" + String.valueOf(CouponDiscount));
                                        }
                                        if (!paymentMode_DiscountOrderid.contains(orderid)) {
                                            paymentMode_DiscountOrderid.add(orderid);
                                            boolean isAlreadyAvailable = checkIfpreorderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                            if (isAlreadyAvailable) {
                                                Modal_OrderDetails modal_orderDetails1 = paymentMode_DiscountHashmap.get(paymentMode);
                                                String discountAmount = modal_orderDetails1.getDiscountAmount();
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 4 " + String.valueOf(discountAmount));
                                                }

                                                double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                double discountAmount_double = Double.parseDouble(couponDiscount_string);
                                                discountAmount_double = discountAmount_double + discountAmount_doublefromArray;
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(discountAmount_double));
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount discountAmount_double" + String.valueOf(discountAmount_double));
                                                }

                                            } else {
                                                Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                modal_orderDetails1.setDiscountAmount(String.valueOf(couponDiscount_string));
                                                if (paymentMode.equals("PAYTM")) {
                                                    //Log.i("TAG", "discountAmount 2" + String.valueOf(couponDiscount_string));
                                                }
                                                paymentMode_DiscountHashmap.put(paymentMode, modal_orderDetails1);
                                            }


                                            //Log.d(Constants.TAG, "mode already availabe");


                                        } else {
                                            //Log.d(Constants.TAG, "orderid already availabe");

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

                            CouponDiscount= CouponDiscount + CouponDiscount_double;


                            modal_orderDetails.coupondiscount = "There is no coupondiscount";

                        }

                }




                    if (json.has("deliverydistance")) {

                        String deliverydistance = String.valueOf(json.get("deliverydistance"));
                        if (!deliverydistance.equals(null) && (!deliverydistance.equals("null"))) {
                            modal_orderDetails.deliverydistance = String.valueOf(json.get("deliverydistance"));

                        } else {
                            modal_orderDetails.deliverydistance = "0";

                        }
                    } else {
                        modal_orderDetails.deliverydistance = "0";
                    }







                        if(!delivered_OrderIdCount.contains(orderid)){
                            delivered_OrderIdCount.add(orderid);

                        }
                        else{
                            Log.d(Constants.TAG, "orderid is already added");

                        }
                        getItemDetailsFromItemDespArray(modal_orderDetails, paymentMode, slotname);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DeliveryPartnerSettlementReport.this, "No Order has delivered On this Date ", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);
                    OrderIdCount.clear();
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
                    preorderpaymentMode_DiscountOrderid.clear();
                    preorderpaymentMode_DiscountHashmap.clear();
                    delivered_OrderIdCount.clear();
                    CouponDiscount = 0;
                    CouponDiscount_preorder =0;

                    paymentMode_DeliveryChargeHashmap.clear();
                    paymentMode_DeliveryChargeOrderid.clear();


                    deliveryCharges=0;
                    addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap,OrderIdCount);
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }
                if (arrayLength - 1 == i1) {
                    if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {
                        //        getOrderForSelectedDate(DateString, vendorKey);
                        addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap, OrderIdCount);
                        Adjusting_Widgets_Visibility(false);

                    } else {
                        Toast.makeText(DeliveryPartnerSettlementReport.this, "No Order has delivered On this Date ", Toast.LENGTH_LONG).show();
                        Adjusting_Widgets_Visibility(false);
                        OrderIdCount.clear();
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
                        preorderpaymentMode_DiscountOrderid.clear();
                        preorderpaymentMode_DiscountHashmap.clear();
                        delivered_OrderIdCount.clear();
                        CouponDiscount = 0;
                        CouponDiscount_preorder =0;
                        paymentMode_DeliveryChargeHashmap.clear();
                        paymentMode_DeliveryChargeOrderid.clear();


                        deliveryCharges=0;

                        addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap, OrderIdCount);

                        //          getOrderForSelectedDate(DateString, vendorKey);

                    }
                }
            }
        }else{

            Toast.makeText(DeliveryPartnerSettlementReport.this, "No Order has delivered On this Date ", Toast.LENGTH_LONG).show();
            Adjusting_Widgets_Visibility(false);
            OrderIdCount.clear();
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
            preorderpaymentMode_DiscountOrderid.clear();
            preorderpaymentMode_DiscountHashmap.clear();
            CouponDiscount = 0;
            CouponDiscount_preorder =0;
            paymentMode_DeliveryChargeHashmap.clear();
            paymentMode_DeliveryChargeOrderid.clear();


            deliveryCharges=0;
            addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap, OrderIdCount);

            //    getOrderForSelectedDate(DateString, vendorKey);
        }

    }


    private void ConvertStringintoDeliveryPartnerListArray(String deliveryPersonList) {
        if ((!deliveryPersonList.equals("") )|| (!deliveryPersonList.equals(null))) {
            try {
                String ordertype = "#", orderid = "";
                //  sorted_OrdersList.clear();

                //converting jsonSTRING into array
                JSONObject jsonObject = new JSONObject(deliveryPersonList);
                JSONArray JArray = jsonObject.getJSONArray("content");
                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                int i1 = 0;
                int arrayLength = JArray.length();
                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                for (; i1 < (arrayLength); i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        Modal_DeliveryPartner modal_deliveryPartner = new Modal_DeliveryPartner();
                        modal_deliveryPartner.deliveryPartnerStatus =String.valueOf(json.get("status"));
                        modal_deliveryPartner.deliveryPartnerKey =String.valueOf(json.get("key"));
                        modal_deliveryPartner.deliveryPartnerMobileNo =String.valueOf(json.get("mobileno"));
                        modal_deliveryPartner.deliveryPartnerName =String.valueOf(json.get("name"));

                        // //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                        deliveryPartner_arrayList.add(modal_deliveryPartner);



                        Adapter_DeliveryPartnerList_Spinner adapter_deliveryPartnerList_spinner= new Adapter_DeliveryPartnerList_Spinner(DeliveryPartnerSettlementReport.this,deliveryPartner_arrayList,DeliveryPartnerSettlementReport.this);

                        deliveryPartnerSelectionSpinner.setAdapter(adapter_deliveryPartnerList_spinner);

                        Adjusting_Widgets_Visibility(false);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                try{
                    Collections.sort(deliveryPartner_arrayList, new Comparator<Modal_DeliveryPartner>() {
                        public int compare(Modal_DeliveryPartner result1, Modal_DeliveryPartner result2) {
                            return result1.getDeliveryPartnerName().compareTo(result2.getDeliveryPartnerName());
                        }
                    });
                }
                catch (Exception e ){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }













    private void getVendorwiseDeliveryPartner() {
        Adjusting_Widgets_Visibility(true);

        deliveryPartner_arrayList.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryPartnerList+vendorKey  , null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //converting jsonSTRING into array
                            result  = response.getJSONArray("content");

                            JSONArray JArray  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1=0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for(;i1<(arrayLength);i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_DeliveryPartner modal_deliveryPartner = new Modal_DeliveryPartner();
                                    modal_deliveryPartner.deliveryPartnerStatus =String.valueOf(json.get("status"));
                                    modal_deliveryPartner.deliveryPartnerKey =String.valueOf(json.get("key"));
                                    modal_deliveryPartner.deliveryPartnerMobileNo =String.valueOf(json.get("mobileno"));
                                    modal_deliveryPartner.deliveryPartnerName =String.valueOf(json.get("name"));

                                    // //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                                    deliveryPartner_arrayList.add(modal_deliveryPartner);



                                    Adapter_DeliveryPartnerList_Spinner adapter_deliveryPartnerList_spinner= new Adapter_DeliveryPartnerList_Spinner(DeliveryPartnerSettlementReport.this,deliveryPartner_arrayList,DeliveryPartnerSettlementReport.this);

                                    deliveryPartnerSelectionSpinner.setAdapter(adapter_deliveryPartnerList_spinner);

                                    Adjusting_Widgets_Visibility(false);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Adjusting_Widgets_Visibility(false);

                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }





                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);

                            e.printStackTrace();
                        }




                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);

                //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());
                deliveryPartner_arrayList.clear();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", "vendor_1");
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
        Volley.newRequestQueue(DeliveryPartnerSettlementReport.this).add(jsonObjectRequest);
    }


    private void getItemDetailsFromItemDespArray(Modal_OrderDetails modal_orderDetailsfromResponse, String paymentMode, String slotname) {
        //  DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String newOrderWeightInGrams;
        double newweight = 0,gstAmount = 0,tmcprice=0;
        int quantity=0;
        try {
            JSONArray jsonArray = modal_orderDetailsfromResponse.getItemdesp();

            for(int i=0; i < jsonArray.length(); i++) {
                //Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                JSONObject json = jsonArray.getJSONObject(i);
                //Log.d(Constants.TAG, "this json" +json.toString());

                Modal_OrderDetails modal_orderDetails_ItemDesp = new Modal_OrderDetails();


                if(json.has("menuitemid")) {
                    modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));
                    newOrderWeightInGrams =  String.valueOf(json.get("netweight"));
                    // newOrderWeightInGrams =newOrderWeightInGrams.replaceAll("[^\\d.]", "");

                    modal_orderDetails_ItemDesp.netweight = String.valueOf(newOrderWeightInGrams);
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
                        if(marinadesObject.has("netweight")){
                            try {
                                newOrderWeightInGrams = String.valueOf(marinadesObject.get("netweight"));
                                // newOrderWeightInGrams =newOrderWeightInGrams.replaceAll("[^\\d.]", "");

                                marinade_modal_orderDetails_ItemDesp.netweight = String.valueOf(newOrderWeightInGrams);
                            }
                            catch (Exception e ){
                                e.printStackTrace();
                            }

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
                            boolean isAlreadyAvailable = false;
                            try{
                                isAlreadyAvailable = checkIfMenuItemisAlreadyAvailableInArray(marinadeitemmenuItemId);

                            }catch(Exception e ){
                                e.printStackTrace();;
                            }
                            if(isAlreadyAvailable) {

                                Modal_OrderDetails modal_orderDetails_itemDespfrom_hashMap = OrderItem_hashmap.get(marinadeitemmenuItemId);
                                //Log.i("tag", "TMCPRICEFROMHashmap" + modal_orderDetails_itemDespfrom_hashMap.getFinalAmount());
                                double tmcprice_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getFinalAmount());
                                int quantity_from_HashMap = Integer.parseInt(modal_orderDetails_itemDespfrom_hashMap.getQuantity());
                                double gstAmount_from_HashMap = Double.parseDouble(modal_orderDetails_itemDespfrom_hashMap.getGstamount());
                                try {
                                    payableAmount_marinade = marinadesObjectpayableAmount + tmcprice_from_HashMap;
                                    quantity_marinade = marinadesObjectquantity + quantity_from_HashMap;
                                    gstAmount_marinade = gstAmount + gstAmount_from_HashMap;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity_marinade)));
                                modal_orderDetails_itemDespfrom_hashMap.setFinalAmount(String.valueOf((payableAmount_marinade)));
                                modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount_marinade)));

                            }
                            else{
                                OrderItem_hashmap.put(marinadeitemmenuItemId, marinade_modal_orderDetails_ItemDesp);

                            }

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
                            //Log.d(Constants.TAG, "this json pre 3 " +String.valueOf(oldOrder_WeightInGrams));


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
                    //Log.d(Constants.TAG, "this order have no menuitemId " + String.valueOf(json.get("itemname")));
                    Adjusting_Widgets_Visibility(false);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }



    private void addFinalPaymentAmountDetails(List<String> paymentModeArray, HashMap<String, Modal_OrderDetails> paymentModeHashmap, List<String> OrderIdCount) {
        FinalBill_hashmap.clear();
        finalBillDetails.clear();
        if(paymentModeArray.size()<=0&&preorder_paymentModeArray.size()<=0){
            delivered_OrderIdCount.clear();
            OrderIdCount.clear();
        }

        totalOrdersCount.setText(String.valueOf(OrderIdCount.size()));
        totaldeliveredOrdersCount.setText(String.valueOf(delivered_OrderIdCount.size()));
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double phonepe_amount = 0,phonepe_Discount_amount = 0 ,cash_amount = 0,cash_Discount_amount = 0 ,Paytm_amount=0, Razorpay_amount=0,PaytmDiscount_amount = 0,
                RazorpayDiscount_amount=0,totalAmount=0,GST=0,totalAmountWithOutGst=0,totalAmount_with_Coupondiscount_deliveryCharges_double=0;
        double preorderphonepe_amount = 0,preorderphonepe_Discount_amount = 0 ,preordercash_amount = 0,preordercash_Discount_amount = 0 ,preorderPaytm_amount=0, preorderRazorpay_amount=0,preorderPaytmDiscount_amount = 0,
                preorderRazorpayDiscount_amount=0,preordertotalAmount=0;

        double razorpayDeliveryCharge_amount=0,paytmDeliveryCharge_amount=0,cash_on_del_DeliveryCharge_amount=0,phonepeDeliveryCharge_amount=0,razorpaypreorderDeliveryCharge =0,
                cash_on_del_preorderDeliveryCharge=0,paytmpreorderDeliveryCharge=0,phonepepreorderDeliveryCharge=0;




        for(String PaymentModefromArray : paymentModeArray) {
            Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(PaymentModefromArray);
            Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(PaymentModefromArray);
            Modal_OrderDetails Payment_Modewise_DeliveryCharge = paymentMode_DeliveryChargeHashmap.get(PaymentModefromArray);

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
                    //Log.d(Constants.TAG, "before for " );
                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    razorpayDeliveryCharge_amount = Double.parseDouble(deliveryCharge_String);


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
                    //Log.d(Constants.TAG, "before for " );

                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    paytmDeliveryCharge_amount = Double.parseDouble(deliveryCharge_String);

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

                    //Log.d(Constants.TAG, "before for " );
                    //Log.d(Constants.TAG, "before for " );
                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    cash_on_del_DeliveryCharge_amount = Double.parseDouble(deliveryCharge_String);


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

                    //Log.d(Constants.TAG, "before for " );
                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    phonepeDeliveryCharge_amount = Double.parseDouble(deliveryCharge_String);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        for(String preorderPaymentModefromArray : preorder_paymentModeArray) {
            Modal_OrderDetails modal_orderDetails = preorder_paymentModeHashmap.get(preorderPaymentModefromArray);
            Modal_OrderDetails Payment_Modewise_discount = preorderpaymentMode_DiscountHashmap.get(preorderPaymentModefromArray);
            Modal_OrderDetails Payment_Modewise_DeliveryCharge = preorderpaymentMode_DeliveryChargeHashmap.get(preorderPaymentModefromArray);

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
                    //Log.d(Constants.TAG, "before for " );
                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    razorpaypreorderDeliveryCharge = Double.parseDouble(deliveryCharge_String);

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
                    //Log.d(Constants.TAG, "before for " );
                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    paytmpreorderDeliveryCharge = Double.parseDouble(deliveryCharge_String);


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

                    //Log.d(Constants.TAG, "before for " );
                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    cash_on_del_preorderDeliveryCharge = Double.parseDouble(deliveryCharge_String);

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

                    //Log.d(Constants.TAG, "before for " );
                    String deliveryCharge_String = Payment_Modewise_DeliveryCharge.getDeliveryamount().toString();
                    phonepepreorderDeliveryCharge = Double.parseDouble(deliveryCharge_String);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }



        try{
            cash_amount = cash_amount-cash_Discount_amount+cash_on_del_DeliveryCharge_amount;
            finalCashAmount_pdf=String.valueOf(cash_amount);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            Razorpay_amount = Razorpay_amount-RazorpayDiscount_amount+razorpayDeliveryCharge_amount;
            finalRazorpayAmount_pdf=String.valueOf(Razorpay_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            Paytm_amount = Paytm_amount-PaytmDiscount_amount+paytmDeliveryCharge_amount;
            finalPaytmAmount_pdf=String.valueOf(Paytm_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            phonepe_amount = phonepe_amount-phonepe_Discount_amount+phonepeDeliveryCharge_amount;
            finalPhonepeAmount_pdf=String.valueOf(phonepe_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }







//////



        try{
            preordercash_amount = preordercash_amount-preordercash_Discount_amount+cash_on_del_preorderDeliveryCharge;
            finalpreorderCashAmount_pdf=String.valueOf(preordercash_amount);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            preorderRazorpay_amount = preorderRazorpay_amount-preorderRazorpayDiscount_amount+razorpaypreorderDeliveryCharge;
            finalpreorderRazorpayAmount_pdf=String.valueOf(preorderRazorpay_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            preorderPaytm_amount = preorderPaytm_amount-preorderPaytmDiscount_amount+paytmpreorderDeliveryCharge;
            finalpreorderPaytmAmount_pdf=String.valueOf(preorderPaytm_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            preorderphonepe_amount = preorderphonepe_amount-preorderphonepe_Discount_amount+phonepepreorderDeliveryCharge;
            finalpreorderPhonepeAmount_pdf=String.valueOf(preorderphonepe_amount);

        }
        catch (Exception e){
            e.printStackTrace();
        }


        totalDeliveryCharges =deliveryCharges+deliveryCharges_preorder;
        totalCouponDiscount = CouponDiscount+CouponDiscount_preorder;
        totalAmount_with_Coupondiscount_deliveryCharges_double = (totalAmountWithOutGst-CouponDiscount)+totalDeliveryCharges;
        totalAmount = totalAmount_with_Coupondiscount_deliveryCharges_double+GST;



        try{
           /* totalAmt_without_GST.setText(String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format(CouponDiscount)));
            totalAmt_with_CouponDiscount.setText(String.valueOf(decimalFormat.format(totalAmount_with_Coupondiscount_double)));
            totalGST_Amt.setText(String.valueOf(decimalFormat.format(GST)));
            final_sales.setText(String.valueOf(decimalFormat.format(totalAmount)));

            */
            final_sales.setText(String.valueOf(decimalFormat.format(totalAmount)));
            totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format(totalCouponDiscount)));
            deliveryChargeAmount_textwidget.setText(String.valueOf(decimalFormat.format(totalDeliveryCharges)));
            cashOnDelivery.setText(String.valueOf(decimalFormat.format(cash_amount)));
            Razorpay.setText(String.valueOf(decimalFormat.format(Razorpay_amount)));
            Paytm.setText(String.valueOf(decimalFormat.format(Paytm_amount)));
            Phonepe.setText(String.valueOf(decimalFormat.format(phonepe_amount)));



            preorder_cashOnDelivery.setText(String.valueOf(decimalFormat.format(preordercash_amount)));
            preorder_Razorpay.setText(String.valueOf(decimalFormat.format(preorderRazorpay_amount)));
            preorder_paytmSales.setText(String.valueOf(decimalFormat.format(preorderPaytm_amount)));
            preorder_Phonepe.setText(String.valueOf(decimalFormat.format(preorderphonepe_amount)));



            totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmount)));
            finalBillDetails.add("Amount Received as Delivery Charge: ");
            FinalBill_hashmap.put("Amount Received as Delivery Charge: ", "Rs. "+String.valueOf(decimalFormat.format(totalDeliveryCharges)));

            finalBillDetails.add("Total Amount Received : ");
            FinalBill_hashmap.put("Total Amount Received : ", "Rs. "+String.valueOf(decimalFormat.format(totalAmount)));

            finalBillDetails.add("Total No.of.Orders Delivered : ");
            FinalBill_hashmap.put("Total No.of.Orders Delivered : ", String.valueOf((OrderIdCount.size())));

         /*   finalBillDetails.add("DISCOUNT : ");
            FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(CouponDiscount));
            finalBillDetails.add("SUBTOTAL : ");
            FinalBill_hashmap.put("SUBTOTAL : ", String.valueOf(decimalFormat.format(totalAmount_with_Coupondiscount_double)));
            finalBillDetails.add("GST : ");
            FinalBill_hashmap.put("GST : ", String.valueOf(decimalFormat.format(GST)));
            finalBillDetails.add("FINAL SALES : ");
            FinalBill_hashmap.put("FINAL SALES : ", String.valueOf(decimalFormat.format(totalAmount)));


          */
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }



    public String getDate_and_time() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);


        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDate = df.format(c);


        return CurrentDate;
    }


    public String getDay_Date_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDate = df.format(c);



        String formattedDate = CurrentDay+", "+CurrentDate;
        return formattedDate;
    }


    //Doing the same with this method as we did with getName()
    private String getVendorData(int position,String fieldName){
        String data="";
        try {
            JSONObject json = result.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
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



    private boolean checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentMode_DiscountHashmap.containsKey(menuitemid);
    }



    private boolean checkIfpreorderPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return preorderpaymentMode_DiscountHashmap.containsKey(menuitemid);
    }


    private boolean checkIfPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentModeHashmap.containsKey(menuitemid);
    }

    private boolean checkIfPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentMode_DeliveryChargeHashmap.containsKey(menuitemid);
    }



    private boolean checkIfpreorderPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(String menuitemid) {
        return preorderpaymentMode_DeliveryChargeHashmap.containsKey(menuitemid);
    }



    private boolean checkIfpreorderPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return preorder_paymentModeHashmap.containsKey(menuitemid);
    }


    private boolean checkIfMenuItemisAlreadyAvailableInArray(String menuitemid) {
        return OrderItem_hashmap.containsKey(menuitemid);
    }



    private void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
        }

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
            String filename = "DeliveryPartner Settlement Report_" + System.currentTimeMillis()  +".pdf";
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
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("DELIVERY PARTNER SETTLEMENT SALES REPORT");
            titlepara.setSpacingBefore(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_CENTER);
            layoutDocument.add(titlepara);

            String vendorname = "Vendor: " + Vendorname;
            com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
            vendorpara.setSpacingBefore(20);
            vendorpara.setSpacingAfter(5);

            vendorpara.setAlignment(Element.ALIGN_LEFT);
            layoutDocument.add(vendorpara);

            com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date:"  + DateString);
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
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);


            PdfPCell itemcell = new PdfPCell(new Phrase("DELIVERY PARTNER NAME      "+deliveryPartnerName));
            itemcell.setBorder(Rectangle.NO_BORDER);
            itemcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            itemcell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            itemcell.setFixedHeight(30);
            table.addCell(itemcell);
            layoutDocument.add(table);



            PdfPTable table2 = new PdfPTable(1);
            table2.setWidthPercentage(100);

            PdfPCell qtycell = new PdfPCell(new Phrase("DELIVERY PARTNER NUMBER      "+deliveryPartnerMobileNo));
            qtycell.setBorder(Rectangle.NO_BORDER);
            qtycell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            qtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qtycell.setFixedHeight(30);
            table2.addCell(qtycell);
            layoutDocument.add(table2);

/*

            PdfPTable table2 = new PdfPTable(1);
            table2.setWidthPercentage(100);

            PdfPCell pricecell = new PdfPCell(new Phrase("DELIVERY PARTNER NUMBER"));
            pricecell.setBorder(Rectangle.NO_BORDER);
            pricecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pricecell.setFixedHeight(30);
            pricecell.setPaddingRight(10);
            table2.addCell(pricecell);



            PdfPCell itempricecell1 = new PdfPCell(new Phrase(deliveryPartnerMobileNo));
            itempricecell1.setBorder(Rectangle.NO_BORDER);
            itempricecell1.setMinimumHeight(30);
            itempricecell1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            itempricecell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            itempricecell1.setPaddingRight(10);
            table2.addCell(itempricecell1);




            layoutDocument.add(table2);


            PdfPCell itemnamecell = null; PdfPCell itemqtycell = null;
            PdfPCell itempricecell = null;
            for (int i=0; i<Order_Item_List.size(); i++) {
                String key = Order_Item_List.get(i);

                Modal_OrderDetails itemRow = OrderItem_hashmap.get(key);
                String itemName = itemRow.getItemname()+" - "+itemRow.getNetweight();
                //Log.i(Constants.TAG,"size"+(itemRow.getItemname()));
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



 */

            PdfPTable tablePaymentModetitle = new PdfPTable(1);
            tablePaymentModetitle.setWidthPercentage(100);
            tablePaymentModetitle.setSpacingBefore(20);


            PdfPCell paymentModertitle;
            paymentModertitle = new PdfPCell(new Phrase("EXPRESS DELIVERY"));
            paymentModertitle.setBorder(Rectangle.NO_BORDER);
            paymentModertitle.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentModertitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            paymentModertitle.setFixedHeight(25);
            paymentModertitle.setPaddingLeft(25);
            paymentModertitle.setPaddingRight(20);
            paymentModertitle.setBackgroundColor(BaseColor.LIGHT_GRAY);

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
                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
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



                paymentModeemptycellone = new PdfPCell(new Phrase(""));
                paymentModeemptycellone.setBorder(Rectangle.NO_BORDER);
                paymentModeemptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                paymentModeemptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                paymentModeemptycellone.setFixedHeight(25);
                tablePaymentMode.addCell(paymentModeemptycellone);


            }
            layoutDocument.add(tablePaymentMode);


            PdfPTable tablepreorderPaymentModetitle = new PdfPTable(1);
            tablepreorderPaymentModetitle.setWidthPercentage(100);
            tablepreorderPaymentModetitle.setSpacingBefore(20);


            PdfPCell paymentModePreordertitle;
            paymentModePreordertitle = new PdfPCell(new Phrase("PREORDER"));
            paymentModePreordertitle.setBorder(Rectangle.NO_BORDER);
            paymentModePreordertitle.setHorizontalAlignment(Element.ALIGN_CENTER);
            paymentModePreordertitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            paymentModePreordertitle.setFixedHeight(25);
            paymentModePreordertitle.setPaddingLeft(25);
            paymentModePreordertitle.setBackgroundColor(BaseColor.LIGHT_GRAY);

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
                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


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


                PreorderpaymentModeemptycellone = new PdfPCell(new Phrase(""));
                PreorderpaymentModeemptycellone.setBorder(Rectangle.NO_BORDER);
                PreorderpaymentModeemptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                PreorderpaymentModeemptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                PreorderpaymentModeemptycellone.setFixedHeight(25);
                tablepreorderPaymentMode.addCell(PreorderpaymentModeemptycellone);



            }
            layoutDocument.add(tablepreorderPaymentMode);





            PdfPTable tablefinaltitle = new PdfPTable(1);
            tablefinaltitle.setWidthPercentage(100);
            tablefinaltitle.setSpacingBefore(20);



            PdfPCell finaltitle;
            finaltitle = new PdfPCell(new Phrase("FINAL"));
            finaltitle.setBorder(Rectangle.NO_BORDER);
            finaltitle.setHorizontalAlignment(Element.ALIGN_CENTER);
            finaltitle.setVerticalAlignment(Element.ALIGN_MIDDLE);
            finaltitle.setPaddingLeft(25);
            finaltitle.setBackgroundColor(BaseColor.LIGHT_GRAY);
            finaltitle.setFixedHeight(25);
            finaltitle.setPaddingRight(20);
            tablefinaltitle.addCell(finaltitle);
            layoutDocument.add(tablefinaltitle);



            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(20);
            PdfPCell emptycell; PdfPCell emptycellone; PdfPCell itemqtycell; PdfPCell itempricecell;
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


                itemqtycell = new PdfPCell(new Phrase(key));
                itemqtycell.setBorderColor(BaseColor.LIGHT_GRAY);
                itemqtycell.setBorder(Rectangle.NO_BORDER);
                itemqtycell.setMinimumHeight(25);
                itemqtycell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itemqtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table1.addCell(itemqtycell);



                itempricecell = new PdfPCell(new Phrase(value));
                itempricecell.setBorderColor(BaseColor.LIGHT_GRAY);
                itempricecell.setBorder(Rectangle.NO_BORDER);
                itempricecell.setMinimumHeight(25);
                itempricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                itempricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itempricecell.setPaddingRight(10);
                table1.addCell(itempricecell);

                emptycellone = new PdfPCell(new Phrase(""));
                emptycellone.setBorder(Rectangle.NO_BORDER);
                emptycellone.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycellone.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emptycellone.setFixedHeight(25);
                table1.addCell(emptycellone);

            }
            layoutDocument.add(table1);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}
