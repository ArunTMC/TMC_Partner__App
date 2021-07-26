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
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListData;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListItem;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListSection;
import com.pos.printer.PrinterFunctions;

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

public class App_Sales_Report_Subctgywise extends AppCompatActivity {
    LinearLayout PrintReport_Layout, generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    TextView deliveryChargeAmount_textwidget,totalSales_headingText, cashSales, cardSales, upiSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;
    String vendorKey;
    String finalCashAmount_pdf, finalRazorpayAmount_pdf, finalPhonepeAmount_pdf, finalPaytmAmount_pdf;
    String finalpreorderCashAmount_pdf, finalpreorderRazorpayAmount_pdf, finalpreorderPhonepeAmount_pdf, finalpreorderPaytmAmount_pdf;
    Adapter_Pos_Sales_Report adapter = new Adapter_Pos_Sales_Report();
    TextView Phonepe, Razorpay, Paytm, cashOnDelivery;
    TextView appOrdersCount_textwidget, preorder_cashOnDelivery, preorder_Phonepe, preorder_Razorpay, preorder_paytmSales;


    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;


    public static List<Modal_OrderDetails> SubCtgyKey_List;
    public static HashMap<String, Modal_OrderDetails> SubCtgyKey_hashmap = new HashMap();

    public static List<String> paymentModeArray;
    public static HashMap<String, Modal_OrderDetails> paymentModeHashmap = new HashMap();
    ;

    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();


    public static List<String> preorder_paymentModeArray;
    public static HashMap<String, Modal_OrderDetails> preorder_paymentModeHashmap = new HashMap();
    ;

    public static List<String> array_of_orderId;

    public static List<String> preorderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails> preorderpaymentMode_DiscountHashmap = new HashMap();
    ;



    public static List<String> paymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails> paymentMode_DiscountHashmap = new HashMap();
    ;

    public static List<String> SubCtgywiseTotalArray;
    public static HashMap<String, String> SubCtgywiseTotalHashmap = new HashMap();
    ;

    public static List<String> preorderpaymentMode_DeliveryChargeOrderid;
    public static HashMap<String, Modal_OrderDetails> preorderpaymentMode_DeliveryChargeHashmap = new HashMap();
    ;



    public static List<String> paymentMode_DeliveryChargeOrderid;
    public static HashMap<String, Modal_OrderDetails> paymentMode_DeliveryChargeHashmap = new HashMap();
    ;


    List<ListData> dataList = new ArrayList<>();

    public static List<String> tmcSubCtgykey;
    String portName = "USB";
    int portSettings = 0, totalGstAmount = 0;

    public static HashMap<String, List<Modal_OrderDetails>> tmcSubCtgywise_sorted_hashmap = new HashMap();
    double screenInches;
    String CurrentDate, PreviousDateString;
    String DateString;

    double CouponDiscount = 0;
    double deliveryamount = 0;
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
        final_sales = findViewById(R.id.final_sales);
        totalSales_headingText = findViewById(R.id.totalSales_headingText);
        cashOnDelivery = findViewById(R.id.cashOnDelivery);
        Razorpay = findViewById(R.id.Razorpay);
        Paytm = findViewById(R.id.paytmSales);
        Phonepe = findViewById(R.id.Phonepe);
        cashSales = findViewById(R.id.cashSales);
        cardSales = findViewById(R.id.cardSales);
        upiSales = findViewById(R.id.upiSales);
        scrollView = findViewById(R.id.scrollView);
        totalSales_headingText = findViewById(R.id.totalSales_headingText);
        deliveryChargeAmount_textwidget = findViewById(R.id.deliveryChargeAmount_textwidget);
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        preorder_cashOnDelivery = findViewById(R.id.preorder_cashOnDelivery);
        preorder_Phonepe = findViewById(R.id.preorder_Phonepe);
        preorder_Razorpay = findViewById(R.id.preorder_Razorpay);
        preorder_paytmSales = findViewById(R.id.preorder_paytmSales);

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        tmcSubCtgykey = new ArrayList<>();
        SubCtgyKey_List = new ArrayList<>();
        paymentModeArray = new ArrayList<>();
        SubCtgywiseTotalArray = new ArrayList<>();
        array_of_orderId = new ArrayList<>();
        preorder_paymentModeArray = new ArrayList<>();
        preorderpaymentMode_DiscountOrderid = new ArrayList<>();
        paymentMode_DiscountOrderid = new ArrayList<>();
        preorderpaymentMode_DeliveryChargeOrderid = new ArrayList<>();
        paymentMode_DeliveryChargeOrderid = new ArrayList<>();

        CurrentDate = getDate();
        PreviousDateString = getDatewithNameofthePreviousDay();

        dateSelector_text.setText(CurrentDate);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);

        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        vendorKey = sharedPreferences.getString("VendorKey", "vendor_1");
        CurrentDate = getDate();
        DateString = getDate();

        dateSelector_text.setText(CurrentDate);

        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        paymentModeHashmap.clear();
        paymentModeArray.clear();
        array_of_orderId.clear();
        preorder_paymentModeHashmap.clear();
        preorder_paymentModeArray.clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();

        preorderpaymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountHashmap.clear();
        paymentMode_DeliveryChargeHashmap.clear();
        paymentMode_DeliveryChargeOrderid.clear();

        preorderpaymentMode_DeliveryChargeOrderid.clear();
        preorderpaymentMode_DeliveryChargeHashmap.clear();

        SubCtgywiseTotalArray.clear();
        SubCtgywiseTotalHashmap.clear();
        getPreOrderForSelectedDate(PreviousDateString, DateString, vendorKey);
        scrollView.fullScroll(View.FOCUS_UP);

        PrintReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (screenInches > 8) {
                    try {
                        Thread t = new Thread() {
                            public void run() {
                                printReport();
                            }
                        };
                        t.start();
                    } catch (Exception e) {
                        Toast.makeText(App_Sales_Report_Subctgywise.this, "Printer is Not Working !! Please Restart the Device", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }
                } else {
                    Toast.makeText(App_Sales_Report_Subctgywise.this, "Cant Find a Printer", Toast.LENGTH_LONG).show();
                }
            }
        });


        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(App_Sales_Report_Subctgywise.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission " + writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(App_Sales_Report_Subctgywise.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    Adjusting_Widgets_Visibility(true);

                    try {
                        exportReport();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(App_Sales_Report_Subctgywise.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
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


    }

    private void printReport() {
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
                PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");
                //Log.i("tag", "The Meat Chop");


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
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Report : APP SALES REPORT" + "\n");


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
                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


                for (int i = 0; i < paymentModeArray.size(); i++) {
                    String Payment_Amount = "", key = paymentModeArray.get(i);
                    Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
                    //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);

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
                    //Log.i("tag", "Printer log key key  " + key + "Rs : " + Payment_Amount);


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
                    //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
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

          catch(Exception e ){
            e.printStackTrace();
            Toast.makeText(App_Sales_Report_Subctgywise.this,"Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

        }
    }









    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog


        datepicker = new DatePickerDialog(App_Sales_Report_Subctgywise.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi year: " + year);
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi monthOfYear: " + monthOfYear);
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi dayOfMonth: " + dayOfMonth);


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
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
                            tmcSubCtgykey.clear();
                            paymentMode_DeliveryChargeHashmap.clear();
                            paymentMode_DeliveryChargeOrderid.clear();

                            preorderpaymentMode_DeliveryChargeOrderid.clear();
                            preorderpaymentMode_DeliveryChargeHashmap.clear();

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
                            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            getPreOrderForSelectedDate(PreviousDateString,DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();
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


    private void getPreOrderForSelectedDate(String previousDaydate,String currentDate, String vendorKey) {
        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        paymentModeHashmap.clear();
        paymentModeArray.clear();
        array_of_orderId.clear();
        SubCtgywiseTotalArray.clear();
        SubCtgywiseTotalHashmap.clear();
        preorder_paymentModeArray.clear();
        preorder_paymentModeHashmap.clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();
        dataList.clear();
        preorderpaymentMode_DiscountOrderid.clear();
        preorderpaymentMode_DiscountHashmap.clear();

        paymentMode_DeliveryChargeHashmap.clear();
        paymentMode_DeliveryChargeOrderid.clear();

        preorderpaymentMode_DeliveryChargeOrderid.clear();
        preorderpaymentMode_DeliveryChargeHashmap.clear();

        //    addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);
        CouponDiscount=0;
        deliveryamount=0;
        Adjusting_Widgets_Visibility(true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsUsingSlotDate_forReport + "?slotdate="+currentDate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDaydate,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        try {
                            String paymentMode = "", ordertype = "", orderid = "", slotname = "";

                            //converting jsonSTRING into array
                            JSONArray JArray = response.getJSONArray("content");
                            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: getPreOrderForSelectedDate" + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            Log.d("Constants.TAG", " getPreOrderForSelectedDate" + arrayLength);

                            if(arrayLength>0){

                                for (; i1 < (arrayLength); i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                        //    //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
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

                                        if (json.has("slotname")) {
                                            try {
                                                modal_orderDetails.slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                                slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                                //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("slotname")));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails.slotname = String.valueOf("");

                                            }

                                        } else {
                                            modal_orderDetails.slotname = String.valueOf("");
                                            //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("slotname")));


                                        }

                                        if ((ordertype.equals(Constants.APPORDER)) && (slotname.equals(Constants.PREORDER_SLOTNAME) )) {
                                            if (json.has("paymentmode")) {

                                                try {
                                                    paymentMode = String.valueOf(json.get("paymentmode")).toUpperCase();
                                                    modal_orderDetails.paymentmode = String.valueOf(json.get("paymentmode")).toUpperCase();
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


                                                    //Log.d(Constants.TAG, "orderid has been succesfully  retrived");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {

                                                //Log.d(Constants.TAG, "There is no orderid: ");


                                            }

                                            if (!array_of_orderId.contains(orderid)) {
                                                array_of_orderId.add(orderid);


                                                if (json.has("deliveryamount")) {

                                                    modal_orderDetails.deliveryamount = String.valueOf(json.get("deliveryamount"));
                                                    try {
                                                        String deliveryamount_string = String.valueOf(json.get("deliveryamount"));
                                                        try {
                                                            if ( deliveryamount_string.equals("")) {
                                                                deliveryamount_string = "0";

                                                                double  deliveryamount_double = Double.parseDouble(deliveryamount_string);
                                                                deliveryamount =  deliveryamount +  deliveryamount_double;

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
                                                                        double deliveryCharge_doublefromArray = Double.parseDouble(DeliveryCharge);
                                                                        double deliveryCharge_double = Double.parseDouble(deliveryamount_string);

                                                                        deliveryCharge_double = deliveryCharge_double + deliveryCharge_doublefromArray;
                                                                        modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryCharge_double));
                                                                    } else {
                                                                        Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                        modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryamount_string));
                                                                        preorderpaymentMode_DeliveryChargeHashmap.put(paymentMode, modal_orderDetails1);
                                                                    }


                                                                } else {
                                                                    //Log.d(Constants.TAG, "orderid already availabe");

                                                                }
                                                            } else {

                                                                double deliveryamount_double = Double.parseDouble(deliveryamount_string);
                                                                deliveryamount = deliveryamount + deliveryamount_double;


                                                                if (!preorderpaymentMode_DeliveryChargeOrderid.contains(orderid)) {
                                                                    preorderpaymentMode_DeliveryChargeOrderid.add(orderid);
                                                                    boolean isAlreadyAvailable = checkIfpreorderPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(paymentMode);
                                                                    if (isAlreadyAvailable) {
                                                                        Modal_OrderDetails modal_orderDetails1 = preorderpaymentMode_DeliveryChargeHashmap.get(paymentMode);
                                                                        String DeliveryCharge = modal_orderDetails1.getDeliveryamount();
                                                                        double deliveryCharge_doublefromArray = Double.parseDouble(DeliveryCharge);
                                                                        double deliveryCharge_double = Double.parseDouble(deliveryamount_string);
                                                                        deliveryCharge_double = deliveryCharge_double + deliveryCharge_doublefromArray;
                                                                        modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryCharge_double));
                                                                    } else {
                                                                        Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                        modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryamount_string));
                                                                        preorderpaymentMode_DeliveryChargeHashmap.put(paymentMode, modal_orderDetails1);
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
                                                    String deliveryamount_string = String.valueOf("0");
                                                    double deliveryCharge_double = Double.parseDouble(deliveryamount_string);

                                                    deliveryamount = deliveryamount + deliveryCharge_double;


                                                    modal_orderDetails.deliveryamount = "There is no deliveryamount";

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
                                                                    //Log.d(Constants.TAG, "orderid already availabe");

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

                                                    CouponDiscount = CouponDiscount + CouponDiscount_double;


                                                    modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                                }


                                                try {


                                                    if ((ordertype.equals(Constants.APPORDER)) && (slotname.equals(Constants.PREORDER_SLOTNAME)) ) {
                                                        getItemDetailsFromItemDespArray(modal_orderDetails, paymentMode, slotname);
                                                    } else {
                                                        //Log.d(Constants.TAG, "This order is not an Apporder e: ");

                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                Toast.makeText(App_Sales_Report_Subctgywise.this, "- ", Toast.LENGTH_LONG).show();
                                                //Log.d(Constants.TAG, "repeated orderid e: "+orderid);

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
                                        SubCtgywiseTotalArray.clear();
                                        SubCtgywiseTotalHashmap.clear();
                                        CouponDiscount = 0;
                                        deliveryamount=0;
                                        paymentMode_DeliveryChargeHashmap.clear();
                                        paymentMode_DeliveryChargeOrderid.clear();

                                        preorderpaymentMode_DeliveryChargeOrderid.clear();
                                        preorderpaymentMode_DeliveryChargeHashmap.clear();


                                        Helper.getListViewSize(posSalesReport_Listview, screenInches);

                                        tmcSubCtgykey.clear();
                                        addFinalPaymentAmountDetails(paymentModeArray, paymentModeHashmap);
                                        //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                                        //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                                        //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

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
                                            SubCtgywiseTotalArray.clear();
                                            SubCtgywiseTotalHashmap.clear();
                                            CouponDiscount = 0;
                                            deliveryamount=0;
                                            Helper.getListViewSize(posSalesReport_Listview, screenInches);
                                            tmcSubCtgykey.clear();
                                            paymentMode_DeliveryChargeHashmap.clear();
                                            paymentMode_DeliveryChargeOrderid.clear();

                                            preorderpaymentMode_DeliveryChargeOrderid.clear();
                                            preorderpaymentMode_DeliveryChargeHashmap.clear();
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
                                SubCtgywiseTotalArray.clear();
                                SubCtgywiseTotalHashmap.clear();
                                CouponDiscount = 0;
                                deliveryamount=0;
                                paymentMode_DeliveryChargeHashmap.clear();
                                paymentMode_DeliveryChargeOrderid.clear();

                                preorderpaymentMode_DeliveryChargeOrderid.clear();
                                preorderpaymentMode_DeliveryChargeHashmap.clear();

                                Helper.getListViewSize(posSalesReport_Listview, screenInches);

                                tmcSubCtgykey.clear();
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
                            deliveryamount=0;
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
                            dataList.clear();

                            paymentMode_DeliveryChargeHashmap.clear();
                            paymentMode_DeliveryChargeOrderid.clear();

                            preorderpaymentMode_DeliveryChargeOrderid.clear();
                            preorderpaymentMode_DeliveryChargeHashmap.clear();

                            tmcSubCtgykey.clear();
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
                Toast.makeText(App_Sales_Report_Subctgywise.this, "There is no Pre Order On this Date ", Toast.LENGTH_LONG).show();
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
                SubCtgywiseTotalArray.clear();
                SubCtgywiseTotalHashmap.clear();
                dataList.clear();

                paymentMode_DeliveryChargeHashmap.clear();
                paymentMode_DeliveryChargeOrderid.clear();

                preorderpaymentMode_DeliveryChargeOrderid.clear();
                preorderpaymentMode_DeliveryChargeHashmap.clear();

                error.printStackTrace();

                Helper.getListViewSize(posSalesReport_Listview, screenInches);

                getOrderForSelectedDate(DateString, vendorKey);

                addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);

                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailswithDate_forReport + "?orderplaceddate=" + dateString+"&vendorkey="+vendorKey, null,
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
                            Log.d("Constants.TAG", "getOrderForSelectedDate Response: " + arrayLength);


                            for (; i1 < (arrayLength); i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
//                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
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
                                    if (json.has("slotname")) {
                                        try {
                                            modal_orderDetails.slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            slotname = String.valueOf(json.get("slotname")).toUpperCase();
                                            //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("slotname")));

                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }

                                    } else {
                                        //Log.d(Constants.TAG, "There is no slotname: " + String.valueOf(json.get("orderid")));


                                    }
                                    if ((ordertype.equals(Constants.APPORDER))) {
                                        if(slotname.equals(Constants.EXPRESS_DELIVERY_SLOTNAME) || (slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME))||(slotname.equals(""))){

                                        if(json.has("deliverytype"))
                                        {
                                            try{
                                                modal_orderDetails.deliverytype = String.valueOf(json.get("deliverytype"));
                                                deliverytype =  String.valueOf(json.get("deliverytype"));

                                                if(deliverytype.equals(Constants.STOREPICKUP_DELIVERYTYPE)){
                                                    if(slotname.equals("")) {
                                                        slotname = String.valueOf(Constants.EXPRESSDELIVERY_SLOTNAME);
                                                        modal_orderDetails.slotname = String.valueOf(Constants.EXPRESSDELIVERY_SLOTNAME);
                                                    }
                                                    //Log.d(Constants.TAG, "deliverytype: " + String.valueOf(json.get("orderid")));

                                                }
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








                                            if (json.has("paymentmode")) {

                                                try {
                                                    paymentMode = String.valueOf(json.get("paymentmode")).toUpperCase();
                                                    modal_orderDetails.paymentmode = String.valueOf(json.get("paymentmode")).toUpperCase();
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


                                                    //Log.d(Constants.TAG, "orderid has been succesfully  retrived");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {

                                                //Log.d(Constants.TAG, "There is no orderid: ");


                                            }

                                            if(!array_of_orderId.contains(orderid)){
                                                array_of_orderId.add(orderid);

                                            }
                                            else{
                                                Toast.makeText(App_Sales_Report_Subctgywise.this, "- ", Toast.LENGTH_LONG).show();

                                            }





                                            if (json.has("deliveryamount")) {

                                                modal_orderDetails.deliveryamount = String.valueOf(json.get("deliveryamount"));
                                                try {
                                                    String deliveryamount_string = String.valueOf(json.get("deliveryamount"));
                                                    try {
                                                        if (deliveryamount_string.equals("")) {
                                                            deliveryamount_string = "0";

                                                            double deliveryamount_double = Double.parseDouble(deliveryamount_string);
                                                            deliveryamount = deliveryamount + deliveryamount_double;

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
                                                                    double DeliveryCharge_double = Double.parseDouble(deliveryamount_string);

                                                                    DeliveryCharge_double = DeliveryCharge_double + DeliveryCharge_doublefromArray;
                                                                    modal_orderDetails1.setDeliveryamount(String.valueOf(DeliveryCharge_double));
                                                                } else {
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryamount_string));
                                                                    paymentMode_DeliveryChargeHashmap.put(paymentMode, modal_orderDetails1);
                                                                }


                                                            } else {
                                                                //Log.d(Constants.TAG, "mode already availabe");

                                                            }
                                                        } else {

                                                            double deliveryamount_double = Double.parseDouble(deliveryamount_string);
                                                            deliveryamount = deliveryamount + deliveryamount_double;


                                                            if (!paymentMode_DeliveryChargeOrderid.contains(orderid)) {
                                                                paymentMode_DeliveryChargeOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = checkIfPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(paymentMode);
                                                                if (isAlreadyAvailable) {
                                                                    Modal_OrderDetails modal_orderDetails1 = paymentMode_DeliveryChargeHashmap.get(paymentMode);
                                                                    String DeliveryCharge = modal_orderDetails1.getDeliveryamount();
                                                                    double DeliveryCharge_doublefromArray = Double.parseDouble(DeliveryCharge);
                                                                    double DeliveryCharge_double = Double.parseDouble(deliveryamount_string);

                                                                    DeliveryCharge_double = DeliveryCharge_double + DeliveryCharge_doublefromArray;
                                                                    modal_orderDetails1.setDeliveryamount(String.valueOf(DeliveryCharge_double));
                                                                } else {
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setDeliveryamount(String.valueOf(deliveryamount_string));
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
                                                String deliveryamount_string = String.valueOf("0");
                                                double DeliveryCharge_double = Double.parseDouble(deliveryamount_string);

                                                deliveryamount = deliveryamount + DeliveryCharge_double;


                                                modal_orderDetails.deliveryamount = "There is no deliveryamount";

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
                                                                //Log.d(Constants.TAG, "mode already availabe");

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
                                        //Log.d(Constants.TAG, "This order is not an Apporder e: " );

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
                            paymentMode_DeliveryChargeHashmap.clear();
                            paymentMode_DeliveryChargeOrderid.clear();
                            paymentMode_DiscountHashmap.clear();
                            paymentMode_DiscountOrderid.clear();
                            SubCtgywiseTotalArray.clear();
                            SubCtgywiseTotalHashmap.clear();
                            CouponDiscount=0;
                            deliveryamount=0;
                            tmcSubCtgykey.clear();

                            Log.d(Constants.TAG, "orderid");



                            dataList.clear();
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
                    paymentMode_DeliveryChargeHashmap.clear();
                    paymentMode_DeliveryChargeOrderid.clear();
                    paymentMode_DiscountHashmap.clear();
                    paymentMode_DiscountOrderid.clear();
                    SubCtgywiseTotalArray.clear();
                    SubCtgywiseTotalHashmap.clear();
                    CouponDiscount=0;
                    deliveryamount=0;
                    tmcSubCtgykey.clear();



                    dataList.clear();
                    Helper.getListViewSize(posSalesReport_Listview, screenInches);


                    addFinalPaymentAmountDetails(paymentModeArray,paymentModeHashmap);


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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(App_Sales_Report_Subctgywise.this).add(jsonObjectRequest);

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
                            Log.i("TAG", "Key : "+ String.valueOf(itemname));
                            Log.i("TAG", "Key : "+ String.valueOf(weightinGrams));

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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAdapter() {
        try {
            adapter = new Adapter_Pos_Sales_Report(App_Sales_Report_Subctgywise.this, dataList,false);
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

        double razorpayDeliveryCharge_amount=0,paytmDeliveryCharge_amount=0,cash_on_del_DeliveryCharge_amount=0,phonepeDeliveryCharge_amount=0,razorpaypreorderDeliveryCharge =0,
        cash_on_del_preorderDeliveryCharge=0,paytmpreorderDeliveryCharge=0,phonepepreorderDeliveryCharge=0;
        appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));



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





        totalAmount_with_Coupondiscount_double = totalAmountWithOutGst-CouponDiscount+deliveryamount;
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
            deliveryChargeAmount_textwidget.setText(String.valueOf(decimalFormat.format(deliveryamount)));


            preorder_cashOnDelivery.setText(String.valueOf(decimalFormat.format(preordercash_amount)));
            preorder_Razorpay.setText(String.valueOf(decimalFormat.format(preorderRazorpay_amount)));
            preorder_paytmSales.setText(String.valueOf(decimalFormat.format(preorderPaytm_amount)));
            preorder_Phonepe.setText(String.valueOf(decimalFormat.format(preorderphonepe_amount)));



            totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmount)));

            finalBillDetails.add("TOTAL : ");
            FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            finalBillDetails.add("DISCOUNT : ");
            FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(CouponDiscount));
            finalBillDetails.add("Delivery Charges : ");
            FinalBill_hashmap.put("Delivery Charges : ", String.valueOf(deliveryamount));
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
        double newweight,gstAmount = 0;
        try {
            JSONArray jsonArray = modal_orderDetailsfromResponse.getItemdesp();

            for(int i=0; i < jsonArray.length(); i++) {
                //Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                JSONObject json = jsonArray.getJSONObject(i);
                //Log.d(Constants.TAG, "this json" +json.toString());

                Modal_OrderDetails modal_orderDetails_ItemDesp = new Modal_OrderDetails();

                if(json.has("menuitemid")) {
                    modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));
                    String menuid= String.valueOf(json.get("menuitemid")+"#");


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
                            if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                Log.d(Constants.TAG, "newOrderWeightInGrams grossweight 1: "+"#"+String.valueOf(newOrderWeightInGrams));

                            }
                        }
                        else {
                            if (json.has("grossweightingrams")) {
                                try {
                                    newOrderWeightInGrams =  String.valueOf(json.get("grossweightingrams"));
                                    if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))) {
                                        newOrderWeightInGrams =newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                                        modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams+"g");

                                        modal_orderDetails_ItemDesp.grossweight = String.valueOf(newOrderWeightInGrams);
                                        if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                            Log.d(Constants.TAG, "newOrderWeightInGrams grossweightingrams 1: "+"#"+String.valueOf(newOrderWeightInGrams));

                                        }

                                    }
                                    else {
                                        if (json.has("netweight")) {

                                            try {

                                                newOrderWeightInGrams = String.valueOf(json.get("netweight"));
                                                if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))&&(!newOrderWeightInGrams.contains("to"))&&(!newOrderWeightInGrams.contains("-"))) {
                                                    newOrderWeightInGrams = newOrderWeightInGrams.replaceAll("[^\\d.]", "");
                                                    String lastThree_weightInGrams = null;
                                                    if (newOrderWeightInGrams != null && newOrderWeightInGrams.length() >= 3) {
                                                        lastThree_weightInGrams = newOrderWeightInGrams.substring(newOrderWeightInGrams.length() - 3);
                                                    }
                                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf(lastThree_weightInGrams + "g");

                                                    modal_orderDetails_ItemDesp.netweight = String.valueOf(lastThree_weightInGrams);
                                                    if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                                        Log.d(Constants.TAG, "newOrderWeightInGrams netweight 1: "+"#"+String.valueOf(newOrderWeightInGrams));

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


                                                        if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                                            Log.d(Constants.TAG, "newOrderWeightInGrams portionsize 1: "+"#"+String.valueOf(newOrderWeightInGrams));

                                                        }

                                                    }
                                                }


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                modal_orderDetails_ItemDesp.weightingrams = String.valueOf("");

                                                modal_orderDetails_ItemDesp.netweight = String.valueOf("");
                                                Log.d(Constants.TAG, "newOrderWeightInGrams e 1: "+"#"+String.valueOf(newOrderWeightInGrams));
                                                if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                                    Log.d(Constants.TAG, "newOrderWeightInGrams e 1: "+"#"+String.valueOf(newOrderWeightInGrams));

                                                }

                                            }
                                        }
                                        else{
                                            if (json.has("portionsize")) {

                                                try {

                                                    newOrderWeightInGrams = String.valueOf(json.get("portionsize"));

                                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                    modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                    if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                                        Log.d(Constants.TAG, "newOrderWeightInGrams portionsize 2: "+"#"+String.valueOf(newOrderWeightInGrams));

                                                    }
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
                                        if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))&&(!newOrderWeightInGrams.contains("to"))&&(!newOrderWeightInGrams.contains("-"))) {

                                            String lastThree_weightInGrams = null;
                                            if (newOrderWeightInGrams != null && newOrderWeightInGrams.length() >= 3) {
                                                lastThree_weightInGrams = newOrderWeightInGrams.substring(newOrderWeightInGrams.length() - 3);
                                            }
                                            modal_orderDetails_ItemDesp.weightingrams = String.valueOf(lastThree_weightInGrams + "g");
                                            modal_orderDetails_ItemDesp.netweight = String.valueOf(lastThree_weightInGrams);

                                            if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                                Log.d(Constants.TAG, "newOrderWeightInGrams netweight 2: "+"#"+String.valueOf(newOrderWeightInGrams));

                                            }
                                        }

                                        else{
                                            if (json.has("portionsize")) {

                                                try {

                                                    newOrderWeightInGrams = String.valueOf(json.get("portionsize"));

                                                    modal_orderDetails_ItemDesp.weightingrams = String.valueOf(newOrderWeightInGrams);

                                                    modal_orderDetails_ItemDesp.portionsize = String.valueOf(newOrderWeightInGrams);
                                                    if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                                        Log.d(Constants.TAG, "newOrderWeightInGrams portionsize 3: "+"#"+String.valueOf(newOrderWeightInGrams));

                                                    }
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
                                            if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                                Log.d(Constants.TAG, "newOrderWeightInGrams portionsize 4: "+"#"+String.valueOf(newOrderWeightInGrams));

                                            }

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
                        if(menuid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                            Log.d(Constants.TAG, "newOrderWeightInGrams 12  "+"#"+String.valueOf(newOrderWeightInGrams));

                        }
                    }
                    else{
                        newOrderWeightInGrams ="";
                    }



                    modal_orderDetails_ItemDesp.itemname = String.valueOf(json.get("itemname"));
                    Log.d(Constants.TAG, "menuitemid   itemname: "+"#"+String.valueOf(json.get("itemname")+"#"));

                    modal_orderDetails_ItemDesp.ordertype = modal_orderDetailsfromResponse.getOrdertype();
                    modal_orderDetails_ItemDesp.paymentmode = modal_orderDetailsfromResponse.getPaymentmode();





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
                        Log.d(Constants.TAG, "menuitemid   marinade: "+"#"+String.valueOf(marinadesObject.get("menuitemid")+"#"));

                        String marinadeitemName = String.valueOf(marinadesObject.get("itemname"));
                        Log.d(Constants.TAG, "menuitemid   marinadeitemName: "+"#"+String.valueOf(marinadesObject.get("itemname")+"#"));


                        String marinadesssubCtgyKey = "";
                        try {
                            if (marinadesObject.has("tmcsubctgykey")) {
                                marinadesssubCtgyKey = String.valueOf(marinadesObject.get("tmcsubctgykey"));
                                if (marinadesssubCtgyKey.equals("") || marinadesssubCtgyKey.equals("0")) {
                                    marinade_modal_orderDetails_ItemDesp.tmcsubctgykey =String.valueOf("Miscellaneous");
                                    marinadesssubCtgyKey = String.valueOf("Miscellaneous");
                                } else {
                                    marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf(marinadesObject.get("tmcsubctgykey"));
                                    marinadesssubCtgyKey = String.valueOf(marinadesObject.get("tmcsubctgykey"));

                                }

                            } else {
                                marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                                marinadesssubCtgyKey = String.valueOf("Miscellaneous");
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();

                            marinade_modal_orderDetails_ItemDesp.tmcsubctgykey = String.valueOf("Miscellaneous");
                            marinadesssubCtgyKey = String.valueOf("Miscellaneous");


                        }


                        try {
                            if (marinadesObject.has("grossweight")) {
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
                                                    if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))&&(!newOrderWeightInGrams.contains("to"))&&(!newOrderWeightInGrams.contains("-"))) {

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

                                            if(((!newOrderWeightInGrams.equals("")))&&(!newOrderWeightInGrams.equals("0"))&&(!newOrderWeightInGrams.contains("to"))&&(!newOrderWeightInGrams.contains("-"))) {

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
                        marinade_modal_orderDetails_ItemDesp.itemname = marinadeitemName+" - Marinade ";


                        try {
                            if(SubCtgywiseTotalArray.contains(marinadesssubCtgyKey)) {
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(marinadesssubCtgyKey);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                if (isAlreadyAvailabe) {
                                    String  SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(marinadesssubCtgyKey);
                                    double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                    SubCtgywisetotalDouble = SubCtgywisetotalDouble+marinadesObjectpayableAmount;
                                    SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseTotalHashmap.replace(marinadesssubCtgyKey,SubCtgywisetotalString);
                                    }
                                    else{
                                        SubCtgywiseTotalHashmap.remove(marinadesssubCtgyKey);
                                        SubCtgywiseTotalHashmap.put(marinadesssubCtgyKey,SubCtgywisetotalString);
                                    }
                                }
                                else{
                                    SubCtgywiseTotalHashmap.put(marinadesssubCtgyKey,String.valueOf(marinadesObjectpayableAmount));

                                }
                            }
                            else{
                                SubCtgywiseTotalArray.add(marinadesssubCtgyKey);
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(marinadesssubCtgyKey);

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                                if (isAlreadyAvailabe) {
                                    String  SubCtgywisetotalString = SubCtgywiseTotalHashmap.get(marinadesssubCtgyKey);
                                    double SubCtgywisetotalDouble = Double.parseDouble(SubCtgywisetotalString);
                                    SubCtgywisetotalDouble = SubCtgywisetotalDouble+marinadesObjectpayableAmount;
                                    SubCtgywisetotalString = String.valueOf(SubCtgywisetotalDouble);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        SubCtgywiseTotalHashmap.replace(marinadesssubCtgyKey,SubCtgywisetotalString);
                                    }
                                    else{
                                        SubCtgywiseTotalHashmap.remove(marinadesssubCtgyKey);
                                        SubCtgywiseTotalHashmap.put(marinadesssubCtgyKey,SubCtgywisetotalString);
                                    }
                                }
                                else{
                                    SubCtgywiseTotalHashmap.put(marinadesssubCtgyKey,String.valueOf(marinadesObjectpayableAmount));

                                }
                            }


                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

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
                            if(menuitemid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                Log.d(Constants.TAG, "newOrderWeightInGrams 14  "+"#"+String.valueOf(oldOrder_WeightInGrams));

                                Log.d(Constants.TAG, "newOrderWeightInGrams 15  "+"#"+String.valueOf(doubleoldOrder_WeightInGrams));
                                Log.d(Constants.TAG, "newOrderWeightInGrams 16  "+"#"+String.valueOf(newweight));

                                Log.d(Constants.TAG, "newOrderWeightInGrams 17  "+"#"+String.valueOf(intNewOrder_WeightInGrams));
                                Log.d(Constants.TAG, "newOrderWeightInGrams 18  "+"#"+String.valueOf(intOldOrder_WeightInGrams));


                            }
                            tmcprice = tmcprice + tmcprice_from_HashMap;
                            quantity = quantity + quantity_from_HashMap;
                            gstAmount = gstAmount + gstAmount_from_HashMap;

                            modal_orderDetails_itemDespfrom_hashMap.setWeightingrams(String.valueOf((intOldOrder_WeightInGrams)));
                            modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                            modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf((tmcprice)));
                            modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount)));
                            if(menuitemid.contains("5932fd977777")){
                                Log.d(Constants.TAG, "menuitemid:hash:"+"#"+String.valueOf(menuitemid)+"#");

                            }

                        } else {
                            if(menuitemid.contains("5932fd977777")){
                                Log.d(Constants.TAG, "menuitemid:hash:"+"#"+String.valueOf(menuitemid)+"#");

                            }
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
                                //Log.d(Constants.TAG, "this json pre 3 " + String.valueOf(oldOrder_WeightInGrams));
                                if(menuitemid.contains("da6d4e81-4c4b-42c5-aabd-64e78059e615")){
                                    Log.d(Constants.TAG, "newOrderWeightInGrams 141  "+"#"+String.valueOf(oldOrder_WeightInGrams));

                                    Log.d(Constants.TAG, "newOrderWeightInGrams 151  "+"#"+String.valueOf(doubleoldOrder_WeightInGrams));
                                    Log.d(Constants.TAG, "newOrderWeightInGrams 161  "+"#"+String.valueOf(newweight));

                                    Log.d(Constants.TAG, "newOrderWeightInGrams 171  "+"#"+String.valueOf(intNewOrder_WeightInGrams));
                                    Log.d(Constants.TAG, "newOrderWeightInGrams 181  "+"#"+String.valueOf(intOldOrder_WeightInGrams));


                                }

                                modal_orderDetails_itemDespfrom_hashMap.setWeightingrams(String.valueOf((intOldOrder_WeightInGrams)));
                                if(menuitemid.contains("5932fd977777")){
                                    Log.d(Constants.TAG, "menuitemid:hash2:"+"#"+String.valueOf(menuitemid)+"#");

                                }
                                modal_orderDetails_itemDespfrom_hashMap.setQuantity(String.valueOf((quantity)));
                                modal_orderDetails_itemDespfrom_hashMap.setTmcprice(String.valueOf((tmcprice)));
                                modal_orderDetails_itemDespfrom_hashMap.setGstamount(String.valueOf((gstAmount)));


                            } else {
                                //calculateSubCtgywiseTotal(modal_orderDetails_ItemDesp);
                                if(menuitemid.contains("5932fd977777")){
                                    Log.d(Constants.TAG, "menuitemid:hash3 :"+"#"+String.valueOf(menuitemid)+"#");

                                }
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
        order_item_list.clear();
        order_item_list.addAll(orderItem_hashmap.keySet());
        return order_item_list;
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



    private boolean checkIfPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentMode_DeliveryChargeHashmap.containsKey(menuitemid);
    }



    private boolean checkIfpreorderPaymentModeDeliveryChargedetailisAlreadyAvailableInArray(String menuitemid) {
        return preorderpaymentMode_DeliveryChargeHashmap.containsKey(menuitemid);
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
        if ((Order_Item_List == null) || (Order_Item_List.size() <= 0)) {
            return;
        }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state " + state + " extstoragedir " + extstoragedir);
        File fol = new File(extstoragedir, "testpdf");
        File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "APP Sales Report_" + System.currentTimeMillis() + ".pdf";
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

            String rsunit = "Rs.",tmcprice;
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);

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
         /*   for (int i = 0; i < Order_Item_List.size(); i++) {
                String key = Order_Item_List.get(i);

                Modal_OrderDetails itemRow = OrderItem_hashmap.get(key);
                String itemName = itemRow.getItemname();
                //Log.i(Constants.TAG, "size" + (itemRow.getItemname()));


          */

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


            //}
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

                itemqtycell = new PdfPCell(new Phrase(key + ":   "));
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