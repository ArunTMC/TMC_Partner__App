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
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListData;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListItem;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListSection;
import com.meatchop.tmcpartner.R;
import com.pos.printer.PrinterFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class PosSalesReport extends AppCompatActivity {
    LinearLayout PrintReport_Layout,generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    TextView totalSales_headingText,cashSales, cardSales,upiSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;
    String vendorKey;
    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;
    Adapater_Pos_Sales_Report adapter = new Adapater_Pos_Sales_Report();
    public static List<Modal_OrderDetails> SubCtgyKey_List;
    public static HashMap<String, Modal_OrderDetails> SubCtgyKey_hashmap = new HashMap();

    public static List<String> paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  paymentModeHashmap  = new HashMap();;

    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();


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
    String CurrentDate;
    String DateString;
    boolean isgetOrderForSelectedDateCalled=false;
    double CouponDiscount=0;
    ListView posSalesReport_Listview;
    ScrollView scrollView;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_sales_report);
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

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        tmcSubCtgykey = new ArrayList<>();
        SubCtgyKey_List =new ArrayList<>();
        paymentModeArray = new ArrayList<>();
        SubCtgywiseTotalArray = new ArrayList<>();
        paymentMode_DiscountOrderid = new ArrayList<>();

        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        paymentModeArray .clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();
        SubCtgywiseTotalArray.clear();
        SubCtgywiseTotalHashmap.clear();
        FinalBill_hashmap.clear();
        CurrentDate = getDate();
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


        getOrderForSelectedDate(CurrentDate, vendorKey);
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
                Toast.makeText(PosSalesReport.this,"Loading.... Please Wait",Toast.LENGTH_SHORT).show();
            }
        });
        PrintReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(screenInches>8){

                    try {
                                printReport();

                    }
                    catch(Exception e ){

                        Toast.makeText(PosSalesReport.this,"Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }
                }
                else{
                    Toast.makeText(PosSalesReport.this,"Cant Find a Printer",Toast.LENGTH_LONG).show();
                }
            }
        });
        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(PosSalesReport.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission " + writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(PosSalesReport.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    Adjusting_Widgets_Visibility(true);
                    try {
                        exportReport();
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
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
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Report : POS SALES REPORT" + "\n");


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
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");


                if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Cash Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

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
            Toast.makeText(PosSalesReport.this,"Printer is Not Working !! Please Restart the Device",Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getTmcSubCtgyList(vendorKey);

    }

    private void getTmcSubCtgyList(String vendorKey) {

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
        datepicker = new DatePickerDialog(PosSalesReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            String month_in_String = getMonthString(monthOfYear);
                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay =   getDayString(dayOfWeek);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            isgetOrderForSelectedDateCalled = false;

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            getOrderForSelectedDate(DateString, vendorKey);
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
        isgetOrderForSelectedDateCalled = true;
        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        FinalBill_hashmap.clear();
        paymentModeArray.clear();
        paymentModeHashmap.clear();
        tmcSubCtgywise_sorted_hashmap.clear();
        tmcSubCtgykey.clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();
        SubCtgywiseTotalArray.clear();
        SubCtgywiseTotalHashmap.clear();
        Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey_forReport + "?orderplaceddate=" + dateString+"&vendorkey="+vendorKey, null,
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


                                    if(json.has("itemdesp")) {
                                        try{

                                            itemdesp = json.getJSONArray("itemdesp");
                                            modal_orderDetails.itemdesp = itemdesp;

                                            //Log.d(Constants.TAG, "itemdesp has been succesfully  retrived" );

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }

                                    }
                                    else
                                    {

                                        //Log.d(Constants.TAG, "There is no itemdesp: " );


                                    }

                                    if(json.has("orderid")) {
                                        try{
                                            modal_orderDetails.orderid = String.valueOf(json.get("orderid"));
                                            orderid = String.valueOf(json.get("orderid"));
                                            //Log.d(Constants.TAG, "orderid"  + String.valueOf(json.get("orderid")));

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }


                                    }
                                    else
                                    {

                                        modal_orderDetails.coupondiscount = "There is no orderid";


                                    }





                                    if(json.has("ordertype")) {
                                        try{
                                            modal_orderDetails.ordertype = String.valueOf(json.get("ordertype"));
                                            ordertype  = String.valueOf(json.get("ordertype")).toUpperCase();
                                            //Log.d(Constants.TAG, "OrderType: " + String.valueOf(json.get("ordertype")));

                                        }
                                        catch (Exception e ){
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
                                            paymentMode = (String.valueOf(json.get("paymentmode")).toUpperCase());

                                            modal_orderDetails.paymentmode = (String.valueOf(json.get("paymentmode")).toUpperCase());
                                            //Log.d(Constants.TAG, "PaymentMode: " + String.valueOf(json.get("paymentmode")));

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();

                                        }

                                    }
                                    else
                                    {

                                        modal_orderDetails.paymentmode = "There is no payment mode";
                                        //Log.d(Constants.TAG, "There is no PaymentMode: " + String.valueOf(json.get("ordertype")));


                                    }


                                    if(ordertype.equals(Constants.POSORDER)){
                                        try{
                                            if (json.has("coupondiscount")) {

                                                modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                    try {
                                                        if (couponDiscount_string.equals("")) {
                                                            couponDiscount_string = "0";

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            CouponDiscount = CouponDiscount + CouponDiscount_double;

                                                            if(!paymentMode_DiscountOrderid.contains(orderid)){
                                                                paymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = false;
                                                                try{
                                                                    isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                                }catch(Exception e ){
                                                                    e.printStackTrace();;
                                                                }
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 = paymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    paymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
                                                                }





                                                            }
                                                            else{
                                                                //Log.d(Constants.TAG, "mode already availabe" );

                                                            }
                                                        } else {

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            CouponDiscount = CouponDiscount + CouponDiscount_double;


                                                            if(!paymentMode_DiscountOrderid.contains(orderid)){
                                                                paymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 = paymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    paymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
                                                                }



                                                                //Log.d(Constants.TAG, "mode already availabe" );


                                                            }
                                                            else{
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


                                            getItemDetailsFromItemDespArray(modal_orderDetails,paymentMode);

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }
                                    }
                                    else{
                                        //Log.d(Constants.TAG, "This order is not an Posorder e: " );

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

                                Adjusting_Widgets_Visibility(false);
                                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                                // Adapater_Pos_Sales_Report adapater_pos_sales_report = new Adapater_Pos_Sales_Report(PosSalesReport.this, Order_Item_List, OrderItem_hashmap, tmcSubCtgykey,SubCtgyKey_List);
                                // posSalesReport_Listview.setAdapter(adapater_pos_sales_report);
                                //sort_the_array_CtgyWise();

                                prepareContent();
                                setAdapter();
                            }

                            else{
                                Order_Item_List.clear();
                                OrderItem_hashmap.clear();
                                finalBillDetails.clear();
                                FinalBill_hashmap.clear();
                                paymentModeArray.clear();
                                paymentModeHashmap.clear();
                                tmcSubCtgywise_sorted_hashmap.clear();
                                SubCtgywiseTotalArray.clear();
                                SubCtgywiseTotalHashmap.clear();
                                tmcSubCtgykey.clear();
                                dataList.clear();
                                ReportListviewSizeHelper.getListViewSize(posSalesReport_Listview, screenInches);

                                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                                Toast.makeText(PosSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
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
                Toast.makeText(PosSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                Order_Item_List.clear();
                OrderItem_hashmap.clear();
                finalBillDetails.clear();
                FinalBill_hashmap.clear();
                paymentModeArray.clear();
                paymentModeHashmap.clear();
                SubCtgywiseTotalArray.clear();
                SubCtgywiseTotalHashmap.clear();
                tmcSubCtgywise_sorted_hashmap.clear();
                tmcSubCtgykey.clear();
                dataList.clear();

                ReportListviewSizeHelper.getListViewSize(posSalesReport_Listview, screenInches);

                addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);

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
        Volley.newRequestQueue(PosSalesReport.this).add(jsonObjectRequest);

    }

    private void sort_the_array_CtgyWise() {
        for(int i =0 ; i<tmcSubCtgykey.size();i++){
            String key =tmcSubCtgykey.get(i);
            //Log.d(Constants.TAG, "before for " +key);
            getDatafromHashmap(key);
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
                  Collections.sort(Order_Item_List, new Comparator<String>() {
                      public int compare(final String object1, final String object2) {
                          return object1.compareTo(object2);
                      }
                  });
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
      }catch (Exception e){
          e.printStackTrace();
      }
    }

    private void setAdapter() {
            try {
                adapter = new Adapater_Pos_Sales_Report(PosSalesReport.this, dataList);
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

    private void getDatafromHashmap(String key) {
        try {
            SubCtgyKey_List.clear();
            for (int j = 0; j < Order_Item_List.size(); j++) {
                String menuid = Order_Item_List.get(j);
                Modal_OrderDetails orderDetailsFromHashmap = OrderItem_hashmap.get(menuid);
                String keyFromHashMap = Objects.requireNonNull(orderDetailsFromHashmap).getTmcsubctgykey();
                //Log.d(Constants.TAG, "beforeequals " + orderDetailsFromHashmap.getTmcsubctgykey());
                Modal_OrderDetails sortedOrderDetails = new Modal_OrderDetails();

                sortedOrderDetails.itemname = orderDetailsFromHashmap.getItemname();
                sortedOrderDetails.menuitemid = orderDetailsFromHashmap.getMenuitemid();
                sortedOrderDetails.weightingrams = orderDetailsFromHashmap.getWeightingrams();
                sortedOrderDetails.quantity = orderDetailsFromHashmap.getQuantity();
                sortedOrderDetails.gstamount = orderDetailsFromHashmap.getGstamount();
                sortedOrderDetails.tmcprice = orderDetailsFromHashmap.getTmcprice();
                sortedOrderDetails.tmcsubctgykey = orderDetailsFromHashmap.getTmcsubctgykey();
                sortedOrderDetails.ordertype = orderDetailsFromHashmap.getOrdertype();
                sortedOrderDetails.paymentmode = orderDetailsFromHashmap.getPaymentmode();

                if (key.equals(keyFromHashMap)) {
                    //Log.d(Constants.TAG, "inequals " + orderDetailsFromHashmap.getTmcsubctgykey());
                    addDatatoArray(key, sortedOrderDetails);
                    //Log.d(Constants.TAG, "SubCtgyKey_List" + SubCtgyKey_List.size());


                }
                // secondary_tmcSubCtgywise_sorted_hashmap.put(menuid,SubCtgyKey_List);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        }

    private void addDatatoArray(String key, Modal_OrderDetails sortedOrderDetails) {
        try {
            SubCtgyKey_List.add(sortedOrderDetails);

            tmcSubCtgywise_sorted_hashmap.put(key, SubCtgyKey_List);
            //Log.d(Constants.TAG, "tmcSubCtgywise_sorted_hashmap" + tmcSubCtgywise_sorted_hashmap.size());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    private void addOrderedItemAmountDetails(List<String> order_item_list, HashMap<String, Modal_OrderDetails> orderItem_hashmap) {


       try {

            DecimalFormat decimalFormat = new DecimalFormat("0.00");
           double totalAmountWithOutGstwithoutDiscount = 0;
           double totalAmountWithGstwithoutDiscount =0;
            double totalAmountWithOutGst = 0;
            double discountAmount = 0;
            double GST = 0;
            double totalAmount = 0;
            double cardPayment_Amount = 0;
            double upiPayment_Amount = 0;
            double cashPayment_Amount = 0;
           double cardDiscount_Amount = 0;
           double upiDiscount_Amount = 0;
           double cashDiscount_Amount = 0;
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
           for(String paymentmode :paymentModeArray){
               Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(paymentmode);
               Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(paymentmode);


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

           }
           Log.d(Constants.TAG, "This orders payment mode tmcprice: " +totalAmount);

           try {
               discountAmount = cardDiscount_Amount+cashDiscount_Amount+upiDiscount_Amount;
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
               totalAmountWithGstwithoutDiscount = totalAmountWithOutGstwithoutDiscount+GST;
           }
           catch (Exception e){
               e.printStackTrace();
           }


           try{
                totalAmt_without_GST.setText(String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
                totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format(discountAmount)));
                totalAmt_with_CouponDiscount.setText(String.valueOf(decimalFormat.format(totalAmountWithOutGstwithoutDiscount)));
                totalGST_Amt.setText(String.valueOf(decimalFormat.format(GST)));
                final_sales.setText(String.valueOf(decimalFormat.format(totalAmountWithGstwithoutDiscount)));
                upiSales.setText(String.valueOf(decimalFormat.format(upiPayment_Amount)));
                cashSales.setText(String.valueOf(decimalFormat.format(cashPayment_Amount)));
                cardSales.setText(String.valueOf(decimalFormat.format(cardPayment_Amount)));
                totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmountWithGstwithoutDiscount)));


                finalBillDetails.add("TOTAL : ");
                FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
                finalBillDetails.add("DISCOUNT : ");
                FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(decimalFormat.format(discountAmount)));
                finalBillDetails.add("SUBTOTAL : ");
                FinalBill_hashmap.put("SUBTOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGstwithoutDiscount)));
                finalBillDetails.add("GST : ");
                FinalBill_hashmap.put("GST : ", String.valueOf(decimalFormat.format(GST)));
                finalBillDetails.add("FINAL SALES : ");
                FinalBill_hashmap.put("FINAL SALES : ", String.valueOf(decimalFormat.format(totalAmountWithGstwithoutDiscount)));
                //   sort_list_tmcSubCtgyWise(Order_Item_List, OrderItem_hashmap);

            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        catch ( Exception e){
            e.printStackTrace();
        }




       /*
                try {
                    TMCsubCtgyKey = String.valueOf(modal_orderDetails_amountDetails.getTmcsubctgykey().toString());
                    for (int count = 0; count < tmcSubCtgykey.size(); count++) {
                        double totalSubCtgyAmount = 0;
                        String totalAmountfromHashmap;
                        String subCtgykeyfromArray = tmcSubCtgykey.get(i);
                        if (subCtgykeyfromArray.equals(TMCsubCtgyKey)) {
                            totalSubCtgyAmount = 0;

                            if (SubCtgywiseTotalArray.contains(TMCsubCtgyKey)) {
                                boolean isAvailableInHashmap = checkIfSubCtgywiseTotalisAlreadyAvailableInArray(TMCsubCtgyKey);
                                if (isAvailableInHashmap) {
                                    try {
                                        totalAmountfromHashmap = SubCtgywiseTotalHashmap.get(TMCsubCtgyKey);
                                    } catch (Exception e) {
                                        totalAmountfromHashmap = "0";
                                        e.printStackTrace();
                                    }
                                    try {
                                        totalSubCtgyAmount = totalAmountWithOutGst_from_array + Double.parseDouble(totalAmountfromHashmap);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            SubCtgywiseTotalHashmap.replace(TMCsubCtgyKey, String.valueOf(decimalFormat.format(totalSubCtgyAmount)));
                                        } else {
                                            SubCtgywiseTotalHashmap.remove(TMCsubCtgyKey);
                                            SubCtgywiseTotalHashmap.put(TMCsubCtgyKey, String.valueOf(decimalFormat.format(totalSubCtgyAmount)));
                                        }

                                    } catch (Exception e) {
                                        totalSubCtgyAmount = totalAmountWithOutGst_from_array + 0;
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            SubCtgywiseTotalHashmap.replace(TMCsubCtgyKey, String.valueOf(decimalFormat.format(totalSubCtgyAmount)));
                                        } else {
                                            SubCtgywiseTotalHashmap.remove(TMCsubCtgyKey);
                                            SubCtgywiseTotalHashmap.put(TMCsubCtgyKey, String.valueOf(decimalFormat.format(totalSubCtgyAmount)));
                                        }

                                        e.printStackTrace();
                                    }
                                } else {
                                    SubCtgywiseTotalHashmap.put(TMCsubCtgyKey, String.valueOf(decimalFormat.format(totalSubCtgyAmount)));

                                }
                            } else {
                                SubCtgywiseTotalArray.add(TMCsubCtgyKey);
                                SubCtgywiseTotalHashmap.put(TMCsubCtgyKey, String.valueOf(decimalFormat.format(totalSubCtgyAmount)));
                            }
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


 */









    }

    private void getItemDetailsFromItemDespArray(Modal_OrderDetails modal_orderDetailsfromResponse, String paymentMode) {
     //   DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String newOrderWeightInGrams,tmcprice_string="";
        double newweight,gstAmount;
        try {
            JSONArray jsonArray = modal_orderDetailsfromResponse.getItemdesp();

            for(int i=0; i < jsonArray.length(); i++) {
                //Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                JSONObject json = jsonArray.getJSONObject(i);
                //Log.d(Constants.TAG, "this json" +json.toString());

                Modal_OrderDetails modal_orderDetails_ItemDesp = new Modal_OrderDetails();

                if(json.has("menuitemid")) {
                    modal_orderDetails_ItemDesp.menuitemid = String.valueOf(json.get("menuitemid"));
                     newOrderWeightInGrams =  String.valueOf(json.get("weightingrams"));
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


                        if(paymentModeArray.contains(paymentMode)){
                            boolean isAlreadyAvailabe = false;

                            try{
                                 isAlreadyAvailabe = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);

                            }catch(Exception e ){
                                e.printStackTrace();;
                            }
                            if(isAlreadyAvailabe){
                                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(paymentMode);
                                if(paymentMode.equals(Constants.CASH_ON_DELIVERY)||paymentMode.equals(Constants.CASH)){
                                    double cash_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCashOndeliverySales());
                                    double  cash_amount = tmcprice+cash_amount_fromhashmap;
                                    double newTotalCashAmount = cash_amount+gstAmount;
                                    modal_orderDetails.setCashOndeliverySales(String.valueOf((newTotalCashAmount)));


                                }
                                if(paymentMode.equals(Constants.CARD)||paymentMode.equals(Constants.Card)){
                                    double card_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCardSales());
                                    //Log.d(Constants.TAG, "This orders payment mode tmcprice Item desp: " +tmcprice );

                                    double  card_amount = tmcprice+card_amount_fromhashmap;
                                    double newTotalcardAmount = card_amount+gstAmount;
                                    modal_orderDetails.setCardSales(String.valueOf((newTotalcardAmount)));


                                }
                                if(paymentMode.equals(Constants.UPI)||paymentMode.equals(Constants.Upi)){
                                    double upi_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getUpiSales());
                                    double  upi_amount = tmcprice+upi_amount_fromhashmap;
                                    double newTotalupiAmount = upi_amount+gstAmount;
                                    modal_orderDetails.setUpiSales(String.valueOf((newTotalupiAmount)));


                                }
                            }
                            else{
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                if(paymentMode.equals(Constants.CASH_ON_DELIVERY)||paymentMode.equals(Constants.CASH)){
                                    double  cash_amount = tmcprice;
                                    double Gst_cash_amount = gstAmount;
                                    double newTotalCashAmount = cash_amount+Gst_cash_amount;

                                    modal_orderDetails.setCashOndeliverySales(String.valueOf((newTotalCashAmount)));


                                }
                                if(paymentMode.equals(Constants.CARD)||paymentMode.equals(Constants.Card)){
                                    double  card_amount = tmcprice;
                                    double Gst_card_amount = gstAmount;
                                    double newTotalcardAmount = card_amount+Gst_card_amount;
                                    //Log.d(Constants.TAG, "This orders payment mode tmcprice Item desp: " +tmcprice );

                                    modal_orderDetails.setCardSales(String.valueOf((newTotalcardAmount)));


                                }
                                if(paymentMode.equals(Constants.UPI)||paymentMode.equals(Constants.Upi)){
                                    double  upi_amount = tmcprice;
                                    double Gst_upi_amount = gstAmount;
                                    double newTotalupiAmount = upi_amount+Gst_upi_amount;

                                    modal_orderDetails.setUpiSales(String.valueOf((newTotalupiAmount)));


                                }
                                paymentModeHashmap.put(paymentMode,modal_orderDetails);
                            }
                        }
                        else{
                            paymentModeArray.add(paymentMode);
                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                            if(paymentMode.equals(Constants.CASH_ON_DELIVERY)||paymentMode.equals(Constants.CASH)){


                                double  cash_amount = tmcprice;
                                double Gst_cash_amount = gstAmount;

                                double newTotalCashAmount = cash_amount+Gst_cash_amount;
                                modal_orderDetails.setCashOndeliverySales(String.valueOf((newTotalCashAmount)));


                            }
                            if(paymentMode.equals(Constants.CARD)||paymentMode.equals(Constants.Card)){
                                double newTotalcardAmount = tmcprice + gstAmount;
                                //Log.d(Constants.TAG, "This orders payment mode tmcprice Item desp: " +tmcprice );

                                modal_orderDetails.setCardSales(String.valueOf((newTotalcardAmount)));


                            }
                            if(paymentMode.equals(Constants.UPI)||paymentMode.equals(Constants.Upi)){
                                double newTotalupiAmount = tmcprice + gstAmount;

                                modal_orderDetails.setUpiSales(String.valueOf((newTotalupiAmount)));


                            }
                            paymentModeHashmap.put(paymentMode,modal_orderDetails);
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

    private void calculateSubCtgywiseTotal(Modal_OrderDetails modal_orderDetails_itemDesp) {
        String SubCtgyKey;
        try {
            SubCtgyKey = String.valueOf(modal_orderDetails_itemDesp.getTmcsubctgykey());
        }
        catch (Exception e) {
            e.printStackTrace();

        }

        try{

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    private boolean checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentMode_DiscountHashmap.containsKey(menuitemid);
    }


    private boolean checkIfSubCtgywiseTotalisAlreadyAvailableInArray(String menuitemid) {
        return SubCtgywiseTotalHashmap.containsKey(menuitemid);
    }

    private boolean checkIfPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentModeHashmap.containsKey(menuitemid);
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
            String filename = "POS Sales Report_" + System.currentTimeMillis() + ".pdf";
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
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("POS SALES REPORT");
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

                for (String SubCtgykey:tmcSubCtgykey){
                    int i_value=0;

                    String SubCtgyName,menuid;
                    Modal_OrderDetails subCtgyName_object =  SubCtgyKey_hashmap.get(SubCtgykey);
                    SubCtgyName = subCtgyName_object.getTmcsubctgyname();
                    for (int j = 0; j <Order_Item_List.size() ; j++) {
                        menuid = Order_Item_List.get(j);
                        Modal_OrderDetails itemRow = OrderItem_hashmap.get(menuid);
                        String itemName = itemRow.getItemname();
                        String  subCtgyKey_fromHashmap = "";
                        try {
                             subCtgyKey_fromHashmap = itemRow.getTmcsubctgykey();
                        }
                        catch (Exception e){
                            e.printStackTrace();

                        }
                        if (subCtgyKey_fromHashmap.equals(SubCtgykey)) {

                            if(i_value!=0) {

                                itemnamecell = new PdfPCell(new Phrase((itemName)));
                                itemnamecell.setBorder(Rectangle.BOTTOM);
                                itemnamecell.setBorderColor(BaseColor.LIGHT_GRAY);
                                itemnamecell.setMinimumHeight(30);
                                itemnamecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                itemnamecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                itemnamecell.setPaddingLeft(10);
                                String KilogramString ="",Quantity="";
                                try {
                                     KilogramString = itemRow.getWeightingrams();
                                 //   if(KilogramString != null &&(!KilogramString.equals(""))&&(!(KilogramString.equals("0.00Kg")))&&(!(KilogramString.equals("0")))) {
                                        Quantity = KilogramString;
                                   /* }
                                    else{
                                        Quantity =  itemRow.getQuantity();
                                    }

                                    */
                                }
                                catch(Exception e){
                                  //  Quantity =  itemRow.getQuantity();
                                    e.printStackTrace();
                                }
                                if(!Quantity.equals("0.0")&&(!Quantity.equals("0.00"))&&(!Quantity.equals("0"))){

                                    double weightinGrams =Double.parseDouble(Quantity);
                                    double kilogram = weightinGrams * 0.001;
                                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                    Quantity  = String.valueOf(decimalFormat.format(kilogram) + "Kg");


                                }
                                else {
                                    Quantity =  itemRow.getQuantity()+"Pc";

                                }

                                itemqtycell = new PdfPCell(new Phrase("" + Quantity));
                                itemqtycell.setBorder(Rectangle.BOTTOM);
                                itemqtycell.setBorderColor(BaseColor.LIGHT_GRAY);
                                itemqtycell.setMinimumHeight(30);
                                itemqtycell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                itemqtycell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                                String totalval = rsunit + String.format(itemRow.getTmcprice());
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
                        }
                        if(i_value==0) {
                            i_value = 1;
                            j=j-1;
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



                         PdfPTable tablePaymentMode = new PdfPTable(4);
                         tablePaymentMode.setWidthPercentage(100);
                         tablePaymentMode.setSpacingBefore(20);
                         PdfPCell paymentModeemptycell;
                         PdfPCell paymentModeemptycellone;
                         PdfPCell paymentModeitemkeycell;
                         PdfPCell paymentModeitemValueCell;


            for (int i = 0; i < paymentModeArray.size(); i++) {
                double payment_AmountDouble =0;
                double payment_AmountDiscDouble =0;

                String Payment_Amount="",key = paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");






                if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble-payment_AmountDiscDouble;
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
                if ((key.toUpperCase().equals("CARD"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCardSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble-payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Card Sales";

                    }
                    catch(Exception e){
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
                        payment_AmountDouble = payment_AmountDouble-payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Upi Sales";

                    }
                    catch(Exception e){
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Upi Sales";

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

                             paymentModeitemkeycell = new PdfPCell(new Phrase(key+" :  "));
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


/*
        FinalBill_hashmap.clear();
        finalBillDetails.clear();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double card_amount=0,upi_amount=0,cash_amount = 0,cash_Discount_amount = 0 , Razorpay_amount=0,RazorpayDiscount_amount=0,totalAmount=0,GST=0,totalAmountWithOutGst=0;

        for(String PaymentModefromArray : paymentModeArray) {
            Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(PaymentModefromArray);

            if (PaymentModefromArray.equals(Constants.CASH_ON_DELIVERY)||(PaymentModefromArray.equals(Constants.CASH))) {


                try {
                    String tmcpriceperkg = String.valueOf(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                    cash_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(cash_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;
                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array = Double.parseDouble(gst_String);
                    GST = GST + GST_array;
                    //Log.d(Constants.TAG, "before for ");


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (PaymentModefromArray.equals(Constants.CARD)) {
                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getCardSales());
                    card_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(card_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;


                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);

                    GST = GST + GST_array;

                    //Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            if (PaymentModefromArray.equals(Constants.UPI)) {
                try {
                    String tmcpriceperkg = String.valueOf(modal_orderDetails.getUpiSales());
                    upi_amount = Double.parseDouble(tmcpriceperkg);
                    int intAmount = (int) Math.ceil(upi_amount);
                    totalAmountWithOutGst = totalAmountWithOutGst + intAmount;


                    String gst_String = String.valueOf(modal_orderDetails.getGstamount());
                    double GST_array  = Double.parseDouble(gst_String);

                    GST = GST + GST_array;

                    //Log.d(Constants.TAG, "before for " );


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        totalAmount = totalAmountWithOutGst+GST;


        try{
            totalAmt_without_GST.setText(String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            totalCouponDiscount_Amt.setText(String.valueOf(decimalFormat.format("0.00")));
            totalAmt_with_CouponDiscount.setText(String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            totalGST_Amt.setText(String.valueOf(decimalFormat.format(GST)));
            final_sales.setText(String.valueOf(decimalFormat.format(totalAmount)));
            cashSales.setText(String.valueOf(decimalFormat.format(cash_amount)));
            cardSales.setText(String.valueOf(decimalFormat.format(card_amount)));
            upiSales.setText(String.valueOf(decimalFormat.format(upi_amount)));

            totalSales_headingText.setText(String.valueOf(decimalFormat.format(totalAmount)));

            finalBillDetails.add("TOTAL : ");
            FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            finalBillDetails.add("DISCOUNT : ");
            FinalBill_hashmap.put("DISCOUNT : ", String.valueOf("0.00"));
            finalBillDetails.add("SUBTOTAL : ");
            FinalBill_hashmap.put("SUBTOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
            finalBillDetails.add("GST : ");
            FinalBill_hashmap.put("GST : ", String.valueOf(decimalFormat.format(GST)));
            finalBillDetails.add("FINAL SALES : ");
            FinalBill_hashmap.put("FINAL SALES : ", String.valueOf(decimalFormat.format(totalAmount)));

        }
        catch (Exception e){
            e.printStackTrace();
        }
*/

