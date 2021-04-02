package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListData;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListItem;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListSection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class App_Sales_Report_Subctgywise extends AppCompatActivity {
    LinearLayout PrintReport_Layout,generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    TextView totalSales_headingText,cashSales, cardSales,upiSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;
    String vendorKey;
    String finalCashAmount_pdf,finalRazorpayAmount_pdf,finalPhonepeAmount_pdf,finalPaytmAmount_pdf;
    String finalpreorderCashAmount_pdf,finalpreorderRazorpayAmount_pdf,finalpreorderPhonepeAmount_pdf,finalpreorderPaytmAmount_pdf;
    Adapater_Pos_Sales_Report adapter = new Adapater_Pos_Sales_Report();
    TextView Phonepe,Razorpay,Paytm, cashOnDelivery;
    TextView appOrdersCount_textwidget,preorder_cashOnDelivery,preorder_Phonepe,preorder_Razorpay,preorder_paytmSales;





    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;


    public static List<Modal_OrderDetails> SubCtgyKey_List;
    public static HashMap<String, Modal_OrderDetails> SubCtgyKey_hashmap = new HashMap();

    public static List<String> paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  paymentModeHashmap  = new HashMap();;

    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();




    public static List<String> preorder_paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  preorder_paymentModeHashmap  = new HashMap();;


    public static List<String> preorderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  preorderpaymentMode_DiscountHashmap  = new HashMap();;

    public static List<String> array_of_orderId;




    public static List<String> paymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  paymentMode_DiscountHashmap  = new HashMap();;

    public static List<String> SubCtgywiseTotalArray;
    public static HashMap<String, String>  SubCtgywiseTotalHashmap  = new HashMap();;


    List<ListData> dataList = new ArrayList<>();

    public static List<String> tmcSubCtgykey;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;

    public static HashMap<String,  List<Modal_OrderDetails>> tmcSubCtgywise_sorted_hashmap = new HashMap();
    double screenInches;
    String CurrentDate,PreviousDateString;
    String DateString;

    double CouponDiscount=0;
    ListView posSalesReport_Listview;
    ScrollView scrollView;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app__sales__report__subctgywise);

        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        posSalesReport_Listview = findViewById(R.id.posSalesReport_Listview);
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
        scrollView  = findViewById(R.id.scrollView);
        totalSales_headingText = findViewById(R.id.totalSales_headingText);

        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        preorder_cashOnDelivery = findViewById(R.id.preorder_cashOnDelivery);
        preorder_Phonepe  = findViewById(R.id.preorder_Phonepe);
        preorder_Razorpay = findViewById(R.id.preorder_Razorpay);
        preorder_paytmSales = findViewById(R.id.preorder_paytmSales);

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        tmcSubCtgykey = new ArrayList<>();
        SubCtgyKey_List =new ArrayList<>();
        paymentModeArray = new ArrayList<>();
        SubCtgywiseTotalArray = new ArrayList<>();
        paymentMode_DiscountOrderid = new ArrayList<>();
        array_of_orderId = new ArrayList<>();
        preorder_paymentModeArray = new ArrayList<>();
        preorderpaymentMode_DiscountOrderid = new ArrayList<>();

        CurrentDate = getDate();
        PreviousDateString = getDatewithNameofthePreviousDay();

        dateSelector_text.setText(CurrentDate);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);

        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        vendorKey = sharedPreferences.getString("VendorKey", "vendor_1");
        CurrentDate = getDate();
        DateString= getDate();

        dateSelector_text.setText(CurrentDate);

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




    }




    @Override
    protected void onStart() {
        super.onStart();
        getTmcSubCtgyList(vendorKey);

    }

    private void getTmcSubCtgyList(String vendorKey){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofSubCtgy,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    String subCtgyKey  = String.valueOf(json.get("key"));
                                    Log.d(Constants.TAG, "subCtgyKey: " + subCtgyKey);
                                    String subCtgyName = String.valueOf(json.get("subctgyname"));
                                    Log.d(Constants.TAG, "subCtgyName: " + subCtgyName);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                    modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                    modal_orderDetails.tmcsubctgyname = subCtgyName;
                                    //  tmcSubCtgykey.add(subCtgyKey);
                                    SubCtgyKey_hashmap.put(subCtgyKey,modal_orderDetails);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                String subCtgyKey  = String.valueOf("Miscellaneous");
                                Log.d(Constants.TAG, "subCtgyKey: " + subCtgyKey);
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

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

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


    private void getPreOrderForSelectedDate(String previousDaydate,String currentDate, String vendorKey) {
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
                                                Toast.makeText(App_Sales_Report_Subctgywise.this, "- ", Toast.LENGTH_LONG).show();
                                                Log.d(Constants.TAG, "repeated orderid e: "+orderid);

                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Adjusting_Widgets_Visibility(false);
                                        Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
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
                                            getOrderForSelectedDate(DateString, vendorKey);
                                        } else {
                                            Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
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

                                            getOrderForSelectedDate(DateString, vendorKey);

                                        }
                                    }
                                }
                            }else{

                                Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
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

                                getOrderForSelectedDate(DateString, vendorKey);
                            }



                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);
                            Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
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
                Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(App_Sales_Report_Subctgywise.this).add(jsonObjectRequest);

    }


    private void getOrderForSelectedDate(String dateString, String vendorKey) {

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
                                                Toast.makeText(App_Sales_Report_Subctgywise.this, "- ", Toast.LENGTH_LONG).show();

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

                            Adjusting_Widgets_Visibility(false);
                            addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);

                            //addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            //adapater_app_sales_report = new Adapter_App_sales_Report(App_Sales_Report_Subctgywise.this, Order_Item_List, OrderItem_hashmap);
                         //   posSalesReport_Listview.setAdapter(adapater_app_sales_report);

                            prepareContent();
                            setAdapter();
                            Helper.getListViewSize(posSalesReport_Listview, screenInches);
                            scrollView.fullScroll(View.FOCUS_UP);




                        }

                        else{
                            Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
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
                Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                Adjusting_Widgets_Visibility(false);

                Helper.getListViewSize(posSalesReport_Listview, screenInches);
                if (Order_Item_List.size() > 0 && OrderItem_hashmap.size() > 0) {

                    Adjusting_Widgets_Visibility(false);
                    //addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
             //       adapater_app_sales_report = new Adapter_App_sales_Report(App_Sales_Report_Subctgywise.this, Order_Item_List, OrderItem_hashmap);
             //       posSalesReport_Listview.setAdapter(adapater_app_sales_report);

                    Helper.getListViewSize(posSalesReport_Listview, screenInches);
                    scrollView.fullScroll(View.FOCUS_UP);

                    addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);



                }
                else{
                    Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(App_Sales_Report_Subctgywise.this).add(jsonObjectRequest);

    }

    private void prepareContent() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try {
            dataList.clear();
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
                    Log.d(Constants.TAG, "before for " + e.getMessage());

                }
                for (int j = 0; j < Order_Item_List.size(); j++) {
                    menuid = Order_Item_List.get(j);
                    Modal_OrderDetails itemDetailsfromHashmap = OrderItem_hashmap.get(menuid);
                    String subCtgyKey_fromHashmap = itemDetailsfromHashmap.getTmcsubctgykey();
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        try {
            adapter = new Adapater_Pos_Sales_Report(App_Sales_Report_Subctgywise.this, dataList);
            posSalesReport_Listview.setAdapter(adapter);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {

            ReportListviewSizeHelper.getListViewSize(posSalesReport_Listview, screenInches);
            scrollView.fullScroll(View.FOCUS_UP);
        }
        catch(Exception e){
            e.printStackTrace();
        }


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
        //   DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String newOrderWeightInGrams = null,tmcprice_string="";
        double newweight,gstAmount;
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
                    if(!newOrderWeightInGrams.contains("Pcs")&&(!(newOrderWeightInGrams.contains("Unit")))&&(!(newOrderWeightInGrams.contains("Kg")))&&(!(newOrderWeightInGrams.contains("kg")))&&(!(newOrderWeightInGrams.contains("pcs")))&&(!(newOrderWeightInGrams.contains("pc")))&&(!(newOrderWeightInGrams.contains("Set")))&&(!(newOrderWeightInGrams.contains("set")))) {
                        newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                    }
                    else{
                        newOrderWeightInGrams ="";
                    }



                    modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
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
                            int intOldOrder_WeightInGrams = (int) Math.ceil(doubleoldOrder_WeightInGrams);

                            int intNewOrder_WeightInGrams = (int) Math.ceil(newweight);

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
                                int intOldOrder_WeightInGrams = (int) Math.ceil(doubleoldOrder_WeightInGrams);

                                int intNewOrder_WeightInGrams = (int) Math.ceil(newweight);

                                intOldOrder_WeightInGrams = intOldOrder_WeightInGrams + intNewOrder_WeightInGrams;
                                Log.d(Constants.TAG, "this json pre 3 " + String.valueOf(oldOrder_WeightInGrams));


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





                    //  add_amount_ForBillDetails(OrderdItems_desp);
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



    private boolean checkIfSubCtgywiseTotalisAlreadyAvailableInArray(String menuitemid) {
        return SubCtgywiseTotalHashmap.containsKey(menuitemid);
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

}