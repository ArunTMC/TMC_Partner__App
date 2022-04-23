package com.meatchop.tmcpartner.Settings;

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
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
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
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order.Modal_ReplacementTransactionDetails;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListData;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListItem;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListSection;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class PosSalesReport extends AppCompatActivity {
    LinearLayout PrintReport_Layout,generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    TextView refundAmount_textwidget,replacementAmount_textwidget,vendorName,bigBasketSales,dunzoSales,swiggySales,creditSales,phoneordercreditSales,phoneordercashSales,phoneordercardSales,phoneorderupiSales,totalSales_headingText,cashSales, cardSales,upiSales, dateSelector_text, totalAmt_without_GST, totalCouponDiscount_Amt, totalAmt_with_CouponDiscount, totalGST_Amt, final_sales;
    String replacementOrderDetailsString,startDateString_forReplacementransaction ="",endDateString_forReplacementransaction ="",vendorKey,vendorname;

    public static HashMap<String, Modal_OrderDetails> OrderItem_hashmap = new HashMap();
    public static List<String> Order_Item_List;
    Adapter_Pos_Sales_Report adapter = new Adapter_Pos_Sales_Report();
    public static List<Modal_OrderDetails> SubCtgyKey_List;
    public static HashMap<String, Modal_OrderDetails> SubCtgyKey_hashmap = new HashMap();

    public static List<String> paymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  paymentModeHashmap  = new HashMap();;


    public static List<String> phoneOrderpaymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  phoneOrderpaymentModeHashmap  = new HashMap();;

    public static List<String> phoneOrderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  phoneOrderpaymentMode_DiscountHashmap  = new HashMap();;


    public static List<String> swiggyOrderpaymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  swiggyOrderpaymentModeHashmap  = new HashMap();;

    public static List<String> swiggyOrderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  swiggyOrderpaymentMode_DiscountHashmap  = new HashMap();;


    public static List<String> dunzoOrderpaymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  dunzoOrderpaymentModeHashmap  = new HashMap();;

    public static List<String> dunzoOrderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  dunzoOrderpaymentMode_DiscountHashmap  = new HashMap();


    public static List<String> bigBasketOrderpaymentModeArray;
    public static HashMap<String, Modal_OrderDetails>  bigBasketOrderpaymentModeHashmap  = new HashMap();;

    public static List<String> bigBasketOrderpaymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  bigBasketOrderpaymentMode_DiscountHashmap  = new HashMap();;



    public static List<String> finalBillDetails;
    public static HashMap<String, String> FinalBill_hashmap = new HashMap();


    public static List<String> paymentMode_DiscountOrderid;
    public static HashMap<String, Modal_OrderDetails>  paymentMode_DiscountHashmap  = new HashMap();;

    public static List<String> SubCtgywiseTotalArray;
    public static HashMap<String, String>  SubCtgywiseTotalHashmap  = new HashMap();;

    public static List<String> replacementTransactiontypeArray = new ArrayList<>();
    public static HashMap<String, List<Modal_ReplacementTransactionDetails>> replacementTransactiontypeHashmap = new HashMap();


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
    double PhoneOrderCouponDiscount=0;
    double swiggyOrderCouponDiscount =0;
    double dunzoOrderCouponDiscount =0;
    double bigbasketOrderCouponDiscount =0;

    boolean isgetReplacementOrderForSelectedDateCalled = false;

    boolean isOrderDetailsResponseReceivedForSelectedDate = false;

    boolean isReplacementTransacDetailsResponseReceivedForSelectedDate = false;


    ScrollView scrollView;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";


    ListView posSalesReport_Listview;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.PosSalesReport";
    String printerType_sharedPreference="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos_sales_report);
        getMenuItemArrayFromSharedPreferences();
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        vendorName = findViewById(R.id.vendorName);

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
        creditSales =  findViewById(R.id.creditSales);
        dunzoSales = findViewById(R.id.dunzoSales);
        bigBasketSales = findViewById(R.id.bigBasketSales);
        refundAmount_textwidget  = findViewById(R.id.refundAmount_textwidget);
        replacementAmount_textwidget= findViewById(R.id.replacementAmount_textwidget);

        swiggySales = findViewById(R.id.swiggySales);
        scrollView  = findViewById(R.id.scrollView);
        totalSales_headingText = findViewById(R.id.totalRating_headingText);
        phoneordercreditSales  = findViewById(R.id.phoneordercreditSales);
        phoneorderupiSales = findViewById(R.id.phoneorderupiSales);
        phoneordercashSales = findViewById(R.id.phoneordercashSales);
        phoneordercardSales = findViewById(R.id.phoneordercardSales);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Order_Item_List = new ArrayList<>();
        finalBillDetails = new ArrayList<>();
        tmcSubCtgykey = new ArrayList<>();
        SubCtgyKey_List =new ArrayList<>();
        paymentModeArray = new ArrayList<>();
        phoneOrderpaymentModeArray = new ArrayList<>();
        swiggyOrderpaymentModeArray = new ArrayList<>();
        dunzoOrderpaymentModeArray = new ArrayList<>();
        bigBasketOrderpaymentModeArray =   new ArrayList<>();
        bigBasketOrderpaymentMode_DiscountOrderid = new ArrayList<>();


        SubCtgywiseTotalArray = new ArrayList<>();
        paymentMode_DiscountOrderid = new ArrayList<>();
        phoneOrderpaymentMode_DiscountOrderid = new ArrayList<>();
        swiggyOrderpaymentMode_DiscountOrderid = new ArrayList<>();
        dunzoOrderpaymentMode_DiscountOrderid = new ArrayList<>();

        Order_Item_List.clear();
        OrderItem_hashmap.clear();
        finalBillDetails.clear();
        paymentModeArray .clear();
        swiggyOrderpaymentModeArray.clear();
        swiggyOrderpaymentModeHashmap.clear();
        dunzoOrderpaymentModeArray.clear();
        dunzoOrderpaymentModeHashmap.clear();

        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();

        bigBasketOrderpaymentModeArray.clear();
        bigBasketOrderpaymentMode_DiscountOrderid.clear();
        bigBasketOrderpaymentModeHashmap.clear();
        bigBasketOrderpaymentMode_DiscountHashmap.clear();
        replacementTransactiontypeHashmap.clear();
        replacementTransactiontypeArray.clear();

        phoneOrderpaymentMode_DiscountOrderid.clear();
        phoneOrderpaymentMode_DiscountHashmap.clear();
        swiggyOrderpaymentMode_DiscountOrderid.clear();
        swiggyOrderpaymentMode_DiscountHashmap.clear();
        dunzoOrderpaymentMode_DiscountOrderid.clear();
        dunzoOrderpaymentMode_DiscountHashmap.clear();
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

        vendorKey = sharedPreferences.getString("VendorKey", "");
        vendorname = sharedPreferences.getString("VendorName", "");

        StoreAddressLine1 = (sharedPreferences.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (sharedPreferences.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (sharedPreferences.getString("VendorPincode", ""));
        StoreLanLine = (sharedPreferences.getString("VendorMobileNumber", ""));

        SharedPreferences shared_PF_PrinterData = getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
        printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));
       // printerType_sharedPreference = String.valueOf(printerType_sharedPreference.toUpperCase());





        CurrentDate = getDate();
        DateString= getDate();

        dateSelector_text.setText(CurrentDate);
        vendorName.setText(vendorname);
        startDateString_forReplacementransaction = getstartDate_and_time_TransactionTable();
        endDateString_forReplacementransaction = getendDate_and_time_TransactionTable();
        getdataFromReplacementTransaction(startDateString_forReplacementransaction,endDateString_forReplacementransaction, vendorKey);

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
                if(Order_Item_List.size()>0) {
                    if (screenInches > Constants.default_mobileScreenSize) {

                        try {
                            if (printerType_sharedPreference.equals(Constants.USB_PrinterType)) {
                                printUsingUSBPrinterReport();

                            } else if (printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)) {
                                printUsingBluetoothPrinterReport();

                            } else if (printerType_sharedPreference.equals(Constants.POS_PrinterType)) {
                                printUsingPOSMachineReport();

                            } else {
                                Toast.makeText(PosSalesReport.this, "ERROR !! There is no Printer Type", Toast.LENGTH_SHORT).show();

                            }


                        }
                        catch (Exception e) {

                            Toast.makeText(PosSalesReport.this, "ERROR !! Printer is Not Working !! Please Restart the Device", Toast.LENGTH_SHORT).show();

                            e.printStackTrace();

                        }
                    } else {
                        Toast.makeText(PosSalesReport.this, "Cant Find a Printer", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(PosSalesReport.this, "There is no data to Print", Toast.LENGTH_SHORT).show();

                }
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


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                        else{
                            Toast.makeText(PosSalesReport.this, "There is no data to Print", Toast.LENGTH_SHORT).show();

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


                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(PosSalesReport.this, WRITE_EXTERNAL_STORAGE);
                    //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                    // If do not grant write external storage permission.
                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        // Request user to grant write external storage permission.
                        ActivityCompat.requestPermissions(PosSalesReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                    } else {
                        Adjusting_Widgets_Visibility(true);
                        try {
                            if(Order_Item_List.size()>0) {

                                try {
                                    exportReport();


                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            }
                            else{
                                Toast.makeText(PosSalesReport.this, "There is no data to Print", Toast.LENGTH_SHORT).show();

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

    private void printUsingBluetoothPrinterReport() {
    }

    private void printUsingUSBPrinterReport() {

        UsbConnection usbConnection = UsbPrintersConnectionsLocal.selectFirstConnected(PosSalesReport.this);
        UsbManager usbManager = (UsbManager) PosSalesReport.this.getSystemService(Context.USB_SERVICE);
        if (usbConnection == null || usbManager == null) {
            Adjusting_Widgets_Visibility(false);

          /*  new AlertDialog.Builder(AddSwiggyOrders.this)
                    .setTitle("USB Connection")
                    .setMessage("No USB printer found.")
                    .show();

           */

            new TMCAlertDialogClass(PosSalesReport.this, R.string.app_name, R.string.ReConnect_Instruction,
                    R.string.OK_Text, R.string.Cancel_Text,
                    new TMCAlertDialogClass.AlertListener() {
                        @Override
                        public void onYes() {
                            printUsingUSBPrinterReport();
                            return;
                        }

                        @Override
                        public void onNo() {
                            Toast.makeText(PosSalesReport.this, "Can't Find USB Printer", Toast.LENGTH_SHORT).show();
                            Adjusting_Widgets_Visibility(false);

                        }
                    });
            return;
        }

        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                PosSalesReport.this,
                0,
                new Intent(ACTION_USB_PERMISSION),
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addCategory("POS Sales Report");
        registerReceiver(usbReceiver_PosReport, filter);
        usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);





    }


    private final BroadcastReceiver usbReceiver_PosReport = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Set<String> category = intent.getCategories();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (PosSalesReport.this) {
                    UsbManager usbManager = (UsbManager) PosSalesReport.this.getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context, new AsyncEscPosPrint.OnPrintFinished() {
                                @Override
                                public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                    Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                    try{
                                        unregisterReceiver(usbReceiver_PosReport);
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

                                    Toast.makeText(PosSalesReport.this, "Error in AsyncPOS USB Printer", Toast.LENGTH_SHORT).show();
                                    Adjusting_Widgets_Visibility(false);

                                }

                                @Override
                                public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                    Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");

                                    try{
                                        unregisterReceiver(usbReceiver_PosReport);
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


                                    Toast.makeText(PosSalesReport.this, "Printed Succesfully", Toast.LENGTH_SHORT).show();
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

        //text_to_Print = "[c]<b><font size='big'>The Meat Chop</b>\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Fresh Meat and Seafood \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine1 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine2 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Postal Code :" + StoreAddressLine3 + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Contact No :" + StoreLanLine + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + GSTIN + " \n"+ " \n";
        text_to_Print = text_to_Print + "[L] ----------------------------------------------" + " \n";
        text_to_Print = text_to_Print + "[L] <font size='tall'>Report Name : POS Sales Report " + " \n";
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
                    /*
                    if (itemName_weight.length() == 10) {
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


                    //text_to_Print = text_to_Print + "[L]<font size='normal'>  " + itemName_weight + itemPrice +" \n"+" \n";

                    text_to_Print = text_to_Print + "[L]<font size='normal'>  " + itemName_weight +"[R]"+ itemPrice +" \n"+" \n";

                }
            }
        }
        text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";

        text_to_Print = text_to_Print + "[L] <font size='wide'>" + " Final Sales Break Up" + " \n";
        text_to_Print = text_to_Print + "[L]<font size='normal'> ----------------------------------------------" + "\n";
        if(paymentModeArray.size()>0){
        text_to_Print = text_to_Print + "[L] <b><font size='normal'> POS Order Sales " + "</b>\n";
        text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";

        for (int i = 0; i < paymentModeArray.size(); i++) {
            double payment_AmountDouble = 0;
            double payment_AmountDiscDouble = 0;

            String Payment_Amount = "", key = paymentModeArray.get(i);
            Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
            Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(key);
            String replacmentFromTextview = "",refundFromTextview = "";
            double replacment_doubleFromTextview = 0,refund_doubleFromTextview = 0;

            //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
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


            if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {
                try {
                    payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                    String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                    payment_AmountDiscDouble = Double.parseDouble(discount_String);
                    payment_AmountDouble = payment_AmountDouble - (payment_AmountDiscDouble + refund_doubleFromTextview + replacment_doubleFromTextview ) ;
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

            text_to_Print = text_to_Print + "[R] <b><font size='normal'>" + key + "[R]" + "Rs : " + Payment_Amount + "</b>\n";

        }

            text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";
        }

        if(phoneOrderpaymentModeArray.size()>0) {
            text_to_Print = text_to_Print + "[L] <b><font size='normal'> Phone Order Sales " + "</b>\n";
            text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";

            for (int i = 0; i < phoneOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = phoneOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = phoneOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = phoneOrderpaymentMode_DiscountHashmap.get(key);

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

              //  text_to_Print = text_to_Print + "[L] <b><font size='normal'>" + key + "     " + "Rs : " + Payment_Amount + "</b>\n";
                text_to_Print = text_to_Print + "[R] <b><font size='normal'>" + key + "[R]" + "Rs : " + Payment_Amount + "</b>\n";

            }

            text_to_Print = text_to_Print + "[L] ----------------------------------------------" + " \n";
        }

        if(swiggyOrderpaymentModeArray.size()>0) {
            text_to_Print = text_to_Print + "[L] <font size='normal'> Swiggy Order Sales " + "\n";
            text_to_Print = text_to_Print + "[L] <font size='normal'> ----------------------------------------------" + " \n";

            for (int i = 0; i < swiggyOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = swiggyOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = swiggyOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = swiggyOrderpaymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");


                if ((key.toUpperCase().equals(Constants.SWIGGYORDER_PAYMENTMODE))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getSwiggySales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Swiggy Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Swiggy Sales";

                    }
                }

                text_to_Print = text_to_Print + "[R] <b><font size='normal'>" + key + "[R]" + "Rs : " + Payment_Amount + "</b>\n";

               // text_to_Print = text_to_Print + "[L] <b><font size='normal'>" + key + "     " + "Rs : " + Payment_Amount + "</b>\n";

            }

            text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";
        }

        if(dunzoOrderpaymentModeArray.size()>0) {
            text_to_Print = text_to_Print + "[L] <b><font size='normal'> Dunzo Order Sales " + "</b>\n";
            text_to_Print = text_to_Print + "[L] <font size='normal'> ----------------------------------------------" + " \n";

            for (int i = 0; i < dunzoOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = dunzoOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = dunzoOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = dunzoOrderpaymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");


                if ((key.toUpperCase().equals(Constants.DUNZOORDER_PAYMENTMODE))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getDunzoSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Dunzo Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Dunzo Sales";

                    }
                }
                //text_to_Print = text_to_Print + "[L] <b><font size='normal'>" + key + "     " + "Rs : " + Payment_Amount + "</b>\n";
                text_to_Print = text_to_Print + "[R] <b><font size='normal'>" + key + "[R]" + "Rs : " + Payment_Amount + "</b>\n";

            }

            text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";
        }

        if(bigBasketOrderpaymentModeArray.size()>0) {
            text_to_Print = text_to_Print + "[L] <b><font size='normal'> Big Basket Order Sales " + "</b>\n";
            text_to_Print = text_to_Print + "[L] <font size='normal'> ----------------------------------------------" + " \n";


            for (int i = 0; i < bigBasketOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = bigBasketOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = bigBasketOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = bigBasketOrderpaymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");


                if ((key.toUpperCase().equals(Constants.BIGBASKETORDER_PAYMENTMODE))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "BigBasket Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "BigBasket Sales";

                    }
                }

                text_to_Print = text_to_Print + "[R] <b><font size='normal'>" + key + "[R]" + "Rs : " + Payment_Amount + "</b>\n";

              //  text_to_Print = text_to_Print + "[L] <b><font size='normal'>" + key + "     " + "Rs : " + Payment_Amount + "</b>\n";

            }
            text_to_Print = text_to_Print + "[L] <font size='normal'>----------------------------------------------" + " \n";

        }

        for (int j = 0; j < finalBillDetails.size(); j++) {
            String key = finalBillDetails.get(j);
            String value = FinalBill_hashmap.get(key);
            value = "RS : " + value;
          /*  if (Objects.requireNonNull(value).length() == 7) {
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

           // text_to_Print = text_to_Print + "[R] <b><font size='normal'>" + value + "</b>\n";
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
            /*PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
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
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n"+ "\n");



            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "POS Order Sales " + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            for (int i = 0; i < paymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(key);
                String replacmentFromTextview = "",refundFromTextview = "";
                double replacment_doubleFromTextview = 0,refund_doubleFromTextview = 0;

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
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


                if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - (payment_AmountDiscDouble + refund_doubleFromTextview + replacment_doubleFromTextview ) ;
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



            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "Phone Order Sales " + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            for (int i = 0; i < phoneOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = phoneOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = phoneOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = phoneOrderpaymentMode_DiscountHashmap.get(key);

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






            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "Swiggy Order Sales " + "\n");


            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            for (int i = 0; i < swiggyOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = swiggyOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = swiggyOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = swiggyOrderpaymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");


                if ((key.toUpperCase().equals(Constants.SWIGGYORDER_PAYMENTMODE)) ) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getSwiggySales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Swiggy Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Swiggy Sales";

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
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "Dunzo Order Sales " + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            for (int i = 0; i < dunzoOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = dunzoOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = dunzoOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = dunzoOrderpaymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");


                if ((key.toUpperCase().equals(Constants.DUNZOORDER_PAYMENTMODE)) ) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getDunzoSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Dunzo Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "Dunzo Sales";

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
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 2, "\n" + "BigBasket Sales " + "\n");

            PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

            for (int i = 0; i < bigBasketOrderpaymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;

                String Payment_Amount = "", key = bigBasketOrderpaymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = bigBasketOrderpaymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = bigBasketOrderpaymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);
                DecimalFormat decimalFormat = new DecimalFormat("0.00");


                if ((key.toUpperCase().equals(Constants.BIGBASKETORDER_PAYMENTMODE)) ) {
                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "BigBasket Sales";
                    } catch (Exception e) {
                        e.printStackTrace();
                        payment_AmountDouble = 0.00;
                        Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                        key = "BigBasket Sales";

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
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    if(Order_Item_List.size()>0) {

                        try {
                            exportReport();


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    else{
                        Toast.makeText(PosSalesReport.this, "There is no data to Print", Toast.LENGTH_SHORT).show();

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

                        try {
                            exportReport();


                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    else{
                        Toast.makeText(PosSalesReport.this, "There is no data to Print", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    private void openDatePicker() {

        replacementTransactiontypeHashmap.clear();
        replacementTransactiontypeArray.clear();

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
                            startDateString_forReplacementransaction = convertNormalDateintoReplacementTransactionDetailsDate(DateString,"STARTTIME");
                            endDateString_forReplacementransaction = convertNormalDateintoReplacementTransactionDetailsDate(DateString, "ENDTIME");

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            getOrderForSelectedDate(DateString, vendorKey);
                            getdataFromReplacementTransaction(startDateString_forReplacementransaction,endDateString_forReplacementransaction, vendorKey);

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
        phoneOrderpaymentModeArray.clear();
        phoneOrderpaymentModeHashmap.clear();
        paymentMode_DiscountHashmap.clear();
        paymentMode_DiscountOrderid.clear();
        phoneOrderpaymentMode_DiscountOrderid.clear();
        phoneOrderpaymentMode_DiscountHashmap.clear();
        swiggyOrderpaymentModeArray.clear();
        swiggyOrderpaymentModeHashmap.clear();
        swiggyOrderpaymentMode_DiscountOrderid.clear();
        swiggyOrderpaymentMode_DiscountHashmap.clear();
        dunzoOrderpaymentModeArray.clear();
        dunzoOrderpaymentModeHashmap.clear();
        dunzoOrderpaymentMode_DiscountOrderid.clear();
        dunzoOrderpaymentMode_DiscountHashmap.clear();
        SubCtgywiseTotalArray.clear();
        tmcSubCtgykey.clear();
        dataList.clear();
        bigBasketOrderpaymentModeArray.clear();
        bigBasketOrderpaymentMode_DiscountOrderid.clear();
        bigBasketOrderpaymentModeHashmap.clear();
        bigBasketOrderpaymentMode_DiscountHashmap.clear();
        SubCtgywiseTotalHashmap.clear();
        Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailswithDate_forReport + "?orderplaceddate=" + dateString+"&vendorkey="+vendorKey, null,
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

                                        modal_orderDetails.orderid = "There is no orderid";


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


                                    if((ordertype.equals(Constants.POSORDER)))
                                    {
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


                                             getItemDetailsFromItemDespArray(modal_orderDetails,paymentMode,ordertype);

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(ordertype.equals(Constants.PhoneOrder)){
                                        try{
                                            if (json.has("coupondiscount")) {

                                                modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                    try {
                                                        if (couponDiscount_string.equals("")) {
                                                            couponDiscount_string = "0";

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            PhoneOrderCouponDiscount = PhoneOrderCouponDiscount + CouponDiscount_double;

                                                            if(!phoneOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                phoneOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = false;
                                                                try{
                                                                    isAlreadyAvailable = checkIfPhoneOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                                }catch(Exception e ){
                                                                    e.printStackTrace();;
                                                                }
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =  phoneOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    phoneOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
                                                                }





                                                            }
                                                            else{
                                                                //Log.d(Constants.TAG, "mode already availabe" );

                                                            }
                                                        } else {

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            PhoneOrderCouponDiscount = PhoneOrderCouponDiscount + CouponDiscount_double;


                                                            if(! phoneOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                phoneOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = checkIfPhoneOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =  phoneOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    phoneOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
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

                                                PhoneOrderCouponDiscount = PhoneOrderCouponDiscount + CouponDiscount_double;


                                                modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                            }


                                            //Log.d(Constants.TAG, "This orders payment mode: " +paymentMode);


                                            getItemDetailsFromItemDespArray(modal_orderDetails,paymentMode, ordertype);

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }
                                    }



                                    else if(ordertype.equals(Constants.SwiggyOrder)){
                                        try{
                                            if (json.has("coupondiscount")) {

                                                modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                    try {
                                                        if (couponDiscount_string.equals("")) {
                                                            couponDiscount_string = "0";

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            swiggyOrderCouponDiscount = swiggyOrderCouponDiscount + CouponDiscount_double;

                                                            if(!swiggyOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                swiggyOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = false;
                                                                try{
                                                                    isAlreadyAvailable = checkIfSwiggyOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                                }catch(Exception e ){
                                                                    e.printStackTrace();;
                                                                }
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =  swiggyOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    swiggyOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
                                                                }





                                                            }
                                                            else{
                                                                //Log.d(Constants.TAG, "mode already availabe" );

                                                            }
                                                        } else {

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            swiggyOrderCouponDiscount = swiggyOrderCouponDiscount + CouponDiscount_double;


                                                            if(! swiggyOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                swiggyOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = checkIfSwiggyOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =  swiggyOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    swiggyOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
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

                                                swiggyOrderCouponDiscount = swiggyOrderCouponDiscount + CouponDiscount_double;


                                                modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                            }


                                            //Log.d(Constants.TAG, "This orders payment mode: " +paymentMode);


                                            getItemDetailsFromItemDespArray(modal_orderDetails,paymentMode, ordertype);

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(ordertype.equals(Constants.DunzoOrder)){
                                        try{
                                            if (json.has("coupondiscount")) {

                                                modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                    try {
                                                        if (couponDiscount_string.equals("")) {
                                                            couponDiscount_string = "0";

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            dunzoOrderCouponDiscount = dunzoOrderCouponDiscount + CouponDiscount_double;

                                                            if(!dunzoOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                dunzoOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = false;
                                                                try{
                                                                    isAlreadyAvailable = checkIfDunzoOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                                }catch(Exception e ){
                                                                    e.printStackTrace();;
                                                                }
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =   dunzoOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    dunzoOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
                                                                }





                                                            }
                                                            else{
                                                                //Log.d(Constants.TAG, "mode already availabe" );

                                                            }
                                                        } else {

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            dunzoOrderCouponDiscount =  dunzoOrderCouponDiscount + CouponDiscount_double;


                                                            if(!  dunzoOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                dunzoOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = checkIfDunzoOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =  dunzoOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    dunzoOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
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

                                                dunzoOrderCouponDiscount = dunzoOrderCouponDiscount + CouponDiscount_double;


                                                modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                            }


                                            //Log.d(Constants.TAG, "This orders payment mode: " +paymentMode);


                                            getItemDetailsFromItemDespArray(modal_orderDetails,paymentMode, ordertype);

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
                                        }
                                    }
                                    else if(ordertype.equals(Constants.BigBasket)){
                                        try{
                                            if (json.has("coupondiscount")) {

                                                modal_orderDetails.coupondiscount = String.valueOf(json.get("coupondiscount"));
                                                try {
                                                    String couponDiscount_string = String.valueOf(json.get("coupondiscount"));
                                                    try {
                                                        if (couponDiscount_string.equals("")) {
                                                            couponDiscount_string = "0";

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            bigbasketOrderCouponDiscount = bigbasketOrderCouponDiscount + CouponDiscount_double;

                                                            if(!bigBasketOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                bigBasketOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = false;
                                                                try{
                                                                    isAlreadyAvailable = checkIfBigBasketOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);

                                                                }catch(Exception e ){
                                                                    e.printStackTrace();;
                                                                }
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =   bigBasketOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    bigBasketOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
                                                                }





                                                            }
                                                            else{
                                                                //Log.d(Constants.TAG, "mode already availabe" );

                                                            }
                                                        } else {

                                                            double CouponDiscount_double = Double.parseDouble(couponDiscount_string);
                                                            bigbasketOrderCouponDiscount =  bigbasketOrderCouponDiscount + CouponDiscount_double;


                                                            if(!bigBasketOrderpaymentMode_DiscountOrderid.contains(orderid)){
                                                                bigBasketOrderpaymentMode_DiscountOrderid.add(orderid);
                                                                boolean isAlreadyAvailable = checkIfBigBasketOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(paymentMode);
                                                                if(isAlreadyAvailable){
                                                                    Modal_OrderDetails modal_orderDetails1 =  bigBasketOrderpaymentMode_DiscountHashmap.get(paymentMode);
                                                                    String discountAmount = modal_orderDetails1.getCoupondiscount();
                                                                    double discountAmount_doublefromArray = Double.parseDouble(discountAmount);
                                                                    double discountAmount_double = Double.parseDouble(couponDiscount_string);

                                                                    discountAmount_double = discountAmount_double+discountAmount_doublefromArray;
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(discountAmount_double));
                                                                }
                                                                else{
                                                                    Modal_OrderDetails modal_orderDetails1 = new Modal_OrderDetails();
                                                                    modal_orderDetails1.setCoupondiscount(String.valueOf(couponDiscount_string));
                                                                    bigBasketOrderpaymentMode_DiscountHashmap.put(paymentMode,modal_orderDetails1);
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

                                                bigbasketOrderCouponDiscount = bigbasketOrderCouponDiscount + CouponDiscount_double;


                                                modal_orderDetails.coupondiscount = "There is no coupondiscount";

                                            }


                                            //Log.d(Constants.TAG, "This orders payment mode: " +paymentMode);


                                            getItemDetailsFromItemDespArray(modal_orderDetails,paymentMode, ordertype);

                                        }
                                        catch (Exception e ){
                                            e.printStackTrace();
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
                                // Adapter_Pos_Sales_Report adapater_pos_sales_report = new Adapter_Pos_Sales_Report(PosSalesReport.this, Order_Item_List, OrderItem_hashmap, tmcSubCtgykey,SubCtgyKey_List);
                                // posSalesReport_Listview.setAdapter(adapater_pos_sales_report);
                                //sort_the_array_CtgyWise();

                                prepareContent();
                                setAdapter();

                              */
                                isOrderDetailsResponseReceivedForSelectedDate = true;
                                runthread();

                            }

                            else{
                                Order_Item_List.clear();
                                OrderItem_hashmap.clear();
                                finalBillDetails.clear();
                                FinalBill_hashmap.clear();
                                paymentModeArray.clear();
                                paymentModeHashmap.clear();
                                tmcSubCtgywise_sorted_hashmap.clear();
                                phoneOrderpaymentModeArray.clear();
                                phoneOrderpaymentModeHashmap.clear();
                                paymentMode_DiscountHashmap.clear();
                                paymentMode_DiscountOrderid.clear();
                                phoneOrderpaymentMode_DiscountOrderid.clear();
                                phoneOrderpaymentMode_DiscountHashmap.clear();
                                SubCtgywiseTotalArray.clear();
                                tmcSubCtgykey.clear();
                                SubCtgywiseTotalHashmap.clear();
                                swiggyOrderpaymentModeArray.clear();
                                swiggyOrderpaymentModeHashmap.clear();
                                swiggyOrderpaymentMode_DiscountOrderid.clear();
                                swiggyOrderpaymentMode_DiscountHashmap.clear();
                                dunzoOrderpaymentModeArray.clear();
                                dunzoOrderpaymentModeHashmap.clear();
                                dunzoOrderpaymentMode_DiscountOrderid.clear();
                                dunzoOrderpaymentMode_DiscountHashmap.clear();

                                bigBasketOrderpaymentModeArray.clear();
                                bigBasketOrderpaymentMode_DiscountOrderid.clear();
                                bigBasketOrderpaymentModeHashmap.clear();
                                bigBasketOrderpaymentMode_DiscountHashmap.clear();
                                dataList.clear();
                                ReportListviewSizeHelper.getListViewSize(posSalesReport_Listview, screenInches);
                                adapter.notifyDataSetChanged();
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
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                else {
                    Toast.makeText(PosSalesReport.this, "There is no Order On this Date ", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);
                    Order_Item_List.clear();
                    OrderItem_hashmap.clear();
                    finalBillDetails.clear();
                    FinalBill_hashmap.clear();
                    paymentModeArray.clear();
                    paymentModeHashmap.clear();
                    tmcSubCtgywise_sorted_hashmap.clear();
                    phoneOrderpaymentModeArray.clear();
                    phoneOrderpaymentModeHashmap.clear();
                    paymentMode_DiscountHashmap.clear();
                    dataList.clear();
                    paymentMode_DiscountOrderid.clear();
                    phoneOrderpaymentMode_DiscountOrderid.clear();
                    phoneOrderpaymentMode_DiscountHashmap.clear();
                    SubCtgywiseTotalArray.clear();
                    tmcSubCtgykey.clear();
                    SubCtgywiseTotalHashmap.clear();
                    swiggyOrderpaymentModeArray.clear();
                    swiggyOrderpaymentModeHashmap.clear();
                    swiggyOrderpaymentMode_DiscountOrderid.clear();
                    swiggyOrderpaymentMode_DiscountHashmap.clear();
                    dunzoOrderpaymentModeArray.clear();
                    dunzoOrderpaymentModeHashmap.clear();
                    dunzoOrderpaymentMode_DiscountOrderid.clear();
                    dunzoOrderpaymentMode_DiscountHashmap.clear();

                    bigBasketOrderpaymentModeArray.clear();
                    bigBasketOrderpaymentMode_DiscountOrderid.clear();
                    bigBasketOrderpaymentModeHashmap.clear();
                    bigBasketOrderpaymentMode_DiscountHashmap.clear();
                    ReportListviewSizeHelper.getListViewSize(posSalesReport_Listview, screenInches);
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
        Volley.newRequestQueue(PosSalesReport.this).add(jsonObjectRequest);

    }

    private void getdataFromReplacementTransaction(String startdateString_forReplacementransaction, String enddateString_forReplacementransaction, String vendorKey) {
        if(isgetReplacementOrderForSelectedDateCalled){
            return;
        }
        isgetReplacementOrderForSelectedDateCalled = true;
        isReplacementTransacDetailsResponseReceivedForSelectedDate = false;
        Adjusting_Widgets_Visibility(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetReplacementTransactionDetailsForTransactionTimeVendorkey + "?transactiontime1="+startdateString_forReplacementransaction+"&vendorkey="+vendorKey+"&transactiontime2="+enddateString_forReplacementransaction,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            JSONArray JArray = response.getJSONArray("content");
                            if(JArray.length()>0) {
                                replacementOrderDetailsString = JArray.toString();
                                convertReplacementTransactionDetailsJsonIntoArray(replacementOrderDetailsString);
                            }
                            else{
                                isReplacementTransacDetailsResponseReceivedForSelectedDate = true;
                            }

                        } catch (JSONException e) {
                            isReplacementTransacDetailsResponseReceivedForSelectedDate = true;
                            e.printStackTrace();
                        }


                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(PosSalesReport.this,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                isgetReplacementOrderForSelectedDateCalled=false;
                isReplacementTransacDetailsResponseReceivedForSelectedDate = true;
                error.printStackTrace();
            }
        })
        {
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
        Volley.newRequestQueue(PosSalesReport.this).add(jsonObjectRequest);




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
                    try{
                        if(json.has("discountamount")){
                            modal_replacementTransactionDetails.setDiscountamount(String.valueOf(json.getString("discountamount")));

                        }
                        else{
                            modal_replacementTransactionDetails.setDiscountamount("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setDiscountamount("");

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("markeditemdesp")){
                            modal_replacementTransactionDetails.setMarkeditemdesp_String(String.valueOf(json.getString("markeditemdesp")));

                        }
                        else{
                            modal_replacementTransactionDetails.setMarkeditemdesp_String("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setMarkeditemdesp_String("");

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("mobileno")){
                            modal_replacementTransactionDetails.setMobileno(String.valueOf(json.getString("mobileno")));

                        }
                        else{
                            modal_replacementTransactionDetails.setMobileno("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setMobileno("");

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("orderid")){
                            modal_replacementTransactionDetails.setOrderid(String.valueOf(json.getString("orderid")));

                        }
                        else{
                            modal_replacementTransactionDetails.setOrderid("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setOrderid("");

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("refundamount")){
                            modal_replacementTransactionDetails.setRefundamount(String.valueOf(json.getString("refundamount")));

                        }
                        else{
                            modal_replacementTransactionDetails.setRefundamount("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setRefundamount("");

                        e.printStackTrace();
                    }


                    try{
                        if(json.has("replacementitemdesp")){
                            modal_replacementTransactionDetails.setReplacementitemdesp_string(String.valueOf(json.getString("replacementitemdesp")));

                        }
                        else{
                            modal_replacementTransactionDetails.setReplacementitemdesp_string("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setReplacementitemdesp_string("");

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("replacementorderamount")){
                            modal_replacementTransactionDetails.setReplacementorderamount(String.valueOf(json.getString("replacementorderamount")));

                        }
                        else{
                            modal_replacementTransactionDetails.setReplacementorderamount("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setReplacementorderamount("");

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("replacementorderid")){
                            modal_replacementTransactionDetails.setReplacementorderid(String.valueOf(json.getString("replacementorderid")));

                        }
                        else{
                            modal_replacementTransactionDetails.setReplacementorderid("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setReplacementorderid("");

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("transactionstatus")){
                            modal_replacementTransactionDetails.setTransactionstatus(String.valueOf(json.getString("transactionstatus")));
                            transactionStatus = String.valueOf(json.getString("transactionstatus"));
                        }
                        else{
                            transactionStatus ="";
                            modal_replacementTransactionDetails.setTransactionstatus("");

                        }
                    }
                    catch (Exception e){
                        transactionStatus ="";

                        modal_replacementTransactionDetails.setTransactionstatus("");

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("transactiontime")){
                            modal_replacementTransactionDetails.setTransactiontime(String.valueOf(json.getString("transactiontime")));

                        }
                        else{
                            modal_replacementTransactionDetails.setTransactiontime("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setTransactiontime("");

                        e.printStackTrace();
                    }

                    try{
                        if(json.has("transactiontype")){
                            modal_replacementTransactionDetails.setTransactiontype(String.valueOf(json.getString("transactiontype")));
                            transactionType =String.valueOf(json.getString("transactiontype").toString().toUpperCase());

                        }
                        else{
                            modal_replacementTransactionDetails.setTransactiontype("");
                            transactionType ="";

                        }
                    }
                    catch (Exception e){
                        transactionType ="";

                        modal_replacementTransactionDetails.setTransactiontype("");

                        e.printStackTrace();
                    }
                    try{
                        if(json.has("vendorkey")){
                            modal_replacementTransactionDetails.setVendorkey(String.valueOf(json.getString("vendorkey")));

                        }
                        else{
                            modal_replacementTransactionDetails.setVendorkey("");

                        }
                    }
                    catch (Exception e){
                        modal_replacementTransactionDetails.setVendorkey("");

                        e.printStackTrace();
                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                try{
                    Log.i("TransactionDetailsArray",String.valueOf(replacementTransactionDetailsArray.size()));
                    Log.i("TransactiontypeArray",String.valueOf(replacementTransactiontypeArray.size()));
                    Log.i("TransactiontypeHashmap",String.valueOf(replacementTransactiontypeHashmap.size()));
                    Log.i("transactionArray",String.valueOf(replacementTransactionDetailsArray.size()));

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    if(transactionStatus.toString().toUpperCase().equals("SUCCESS")) {
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
                        try{
                            if(arrayLength-1==i1){
                                isReplacementTransacDetailsResponseReceivedForSelectedDate = true;

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
        catch (Exception e){
            e.printStackTrace();
        }

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
        Adjusting_Widgets_Visibility(false);

        try {
                adapter = new Adapter_Pos_Sales_Report(PosSalesReport.this, dataList,false);
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



           try{
               for (String transactionType : replacementTransactiontypeArray) {

                   List<Modal_ReplacementTransactionDetails> replacementTransactionDetailsArray = replacementTransactiontypeHashmap.get(transactionType);

                   for(int i=0 ;i <replacementTransactionDetailsArray.size(); i++){
                       double refundAmount = 0;
                       double replacementAmount = 0;

                       Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = replacementTransactionDetailsArray.get(i);

                       if(modal_replacementTransactionDetails.getTransactiontype().toUpperCase().equals("REFUND")){
                           String refundAmountString ="0";
                           try{
                               refundAmountString = modal_replacementTransactionDetails.getRefundamount().toString();

                           }
                           catch (Exception e){
                               refundAmountString ="0";
                               e.printStackTrace();
                           }
                           try {
                               refundAmount = Double.parseDouble(refundAmountString);

                           }
                           catch (Exception e){
                               e.printStackTrace();
                           }

                           try{

                               totalRefundAmount  = refundAmount + totalRefundAmount;
                           }
                           catch (Exception e){
                               e.printStackTrace();
                           }

                       }


                       if(modal_replacementTransactionDetails.getTransactiontype().toUpperCase().equals("REPLACEMENT")){
                           String replacementAmountString ="0";
                           try{
                               replacementAmountString = modal_replacementTransactionDetails.getReplacementorderamount().toString();

                           }
                           catch (Exception e){
                               replacementAmountString ="0";
                               e.printStackTrace();
                           }
                           try {
                               replacementAmount = Double.parseDouble(replacementAmountString);

                           }
                           catch (Exception e){
                               e.printStackTrace();
                           }

                           try{

                               totalReplacementAmount  = replacementAmount + totalReplacementAmount;
                           }
                           catch (Exception e){
                               e.printStackTrace();
                           }
                       }


                   }


               }

           }
           catch (Exception e){
               e.printStackTrace();
           }

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
               if ((paymentmode.toUpperCase().equals("CREDIT"))) {
                   creditPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales());
                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   creditDiscount_Amount = Double.parseDouble(discount_String);
                   creditPayment_Amount = creditPayment_Amount-creditDiscount_Amount;
               }

           }

           for(String paymentmode :phoneOrderpaymentModeArray){
               Modal_OrderDetails modal_orderDetails = phoneOrderpaymentModeHashmap.get(paymentmode);
               Modal_OrderDetails Payment_Modewise_discount = phoneOrderpaymentMode_DiscountHashmap.get(paymentmode);


               if ((paymentmode.toUpperCase().equals("CASH ON DELIVERY")) || (paymentmode.toUpperCase().equals("CASH"))) {
                   phoneOrder_cashPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   phoneOrder_cashDiscount_Amount = Double.parseDouble(discount_String);
                   phoneOrder_cashPayment_Amount = phoneOrder_cashPayment_Amount-phoneOrder_cashDiscount_Amount;


               }
               if ((paymentmode.toUpperCase().equals("CARD"))) {
                   phoneOrder_cardPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCardSales());
                   //Log.d(Constants.TAG, "This orders payment mode tmcprice: " +cardPayment_Amount);

                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   phoneOrder_cardDiscount_Amount = Double.parseDouble(discount_String);
                   phoneOrder_cardPayment_Amount = phoneOrder_cardPayment_Amount-phoneOrder_cardDiscount_Amount;
                   //Log.d(Constants.TAG, "This orders payment mode tmcprice 1 : " +cardPayment_Amount);

               }
               if ((paymentmode.toUpperCase().equals("UPI"))) {
                   phoneOrder_upiPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getUpiSales());
                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   phoneOrder_upiDiscount_Amount = Double.parseDouble(discount_String);
                   phoneOrder_upiPayment_Amount = phoneOrder_upiPayment_Amount-phoneOrder_upiDiscount_Amount;
               }
               if ((paymentmode.toUpperCase().equals("CREDIT"))) {
                   phoneOrder_creditPayment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCreditSales());
                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   phoneOrder_creditDiscount_Amount = Double.parseDouble(discount_String);
                   phoneOrder_creditPayment_Amount = phoneOrder_creditPayment_Amount-phoneOrder_creditDiscount_Amount;
               }

           }


           for(String paymentmode :swiggyOrderpaymentModeArray){
               Modal_OrderDetails modal_orderDetails = swiggyOrderpaymentModeHashmap.get(paymentmode);
               Modal_OrderDetails Payment_Modewise_discount = swiggyOrderpaymentMode_DiscountHashmap.get(paymentmode);


               if ((paymentmode.toUpperCase().equals(Constants.SWIGGYORDER_PAYMENTMODE))) {
                   swiggyOrder_Payment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getSwiggySales());
                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   swiggyOrder_Discount_Amount = Double.parseDouble(discount_String);
                   swiggyOrder_Payment_Amount = swiggyOrder_Payment_Amount-swiggyOrder_Discount_Amount;


               }

           }
           for(String paymentmode :dunzoOrderpaymentModeArray){
               Modal_OrderDetails modal_orderDetails = dunzoOrderpaymentModeHashmap.get(paymentmode);
               Modal_OrderDetails Payment_Modewise_discount = dunzoOrderpaymentMode_DiscountHashmap.get(paymentmode);


               if ((paymentmode.toUpperCase().equals(Constants.DUNZOORDER_PAYMENTMODE))) {
                   dunzoOrder_Payment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getDunzoSales());
                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   dunzoOrder_Discount_Amount = Double.parseDouble(discount_String);
                   dunzoOrder_Payment_Amount = dunzoOrder_Payment_Amount-dunzoOrder_Discount_Amount;


               }

           }



           for(String paymentmode :bigBasketOrderpaymentModeArray){
               Modal_OrderDetails modal_orderDetails = bigBasketOrderpaymentModeHashmap.get(paymentmode);
               Modal_OrderDetails Payment_Modewise_discount = bigBasketOrderpaymentMode_DiscountHashmap.get(paymentmode);


               if ((paymentmode.toUpperCase().equals(Constants.BIGBASKETORDER_PAYMENTMODE))) {
                   bigBasketOrder_Payment_Amount = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales());
                   String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                   bigBasketOrder_Discount_Amount = Double.parseDouble(discount_String);
                   bigBasketOrder_Payment_Amount = bigBasketOrder_Payment_Amount-bigBasketOrder_Discount_Amount;


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
               replacementAmount_textwidget.setText(String.valueOf(decimalFormat.format(totalReplacementAmount)));
               refundAmount_textwidget.setText(String.valueOf(decimalFormat.format(totalRefundAmount)));

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

               phoneorderupiSales.setText(String.valueOf(decimalFormat.format(phoneOrder_upiPayment_Amount)));
               phoneordercashSales.setText(String.valueOf(decimalFormat.format(phoneOrder_cashPayment_Amount)));
               phoneordercardSales.setText(String.valueOf(decimalFormat.format(phoneOrder_cardPayment_Amount)));
                phoneordercreditSales.setText(String.valueOf(decimalFormat.format(phoneOrder_creditPayment_Amount)));


               swiggySales.setText(String.valueOf(decimalFormat.format(swiggyOrder_Payment_Amount)));
               dunzoSales.setText(String.valueOf(decimalFormat.format(dunzoOrder_Payment_Amount)));
               bigBasketSales.setText(String.valueOf(decimalFormat.format(bigBasketOrder_Payment_Amount)));

               finalBillDetails.add("TOTAL : ");
                FinalBill_hashmap.put("TOTAL : ", String.valueOf(decimalFormat.format(totalAmountWithOutGst)));
                finalBillDetails.add("DISCOUNT : ");
                FinalBill_hashmap.put("DISCOUNT : ", String.valueOf(decimalFormat.format(discountAmount)));
               finalBillDetails.add("REFUND : ");
               FinalBill_hashmap.put("REFUND : ", String.valueOf(decimalFormat.format(totalRefundAmount)));
               finalBillDetails.add("REPLACEMENT : ");
               FinalBill_hashmap.put("REPLACEMENT : ", String.valueOf(decimalFormat.format(totalReplacementAmount)));
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

    private void getItemDetailsFromItemDespArray(Modal_OrderDetails modal_orderDetailsfromResponse, String paymentMode, String ordertype) {
     //   DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String newOrderWeightInGrams,tmcprice_string="";
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
                     newOrderWeightInGrams =  String.valueOf(json.get("weightingrams"));
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

                        if(ordertype.equals(Constants.POSORDER)) {
                            if (paymentModeArray.contains(paymentMode)) {
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfPaymentdetailisAlreadyAvailableInArray(paymentMode);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ;
                                }
                                if (isAlreadyAvailabe) {
                                    Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(paymentMode);
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


                                    paymentModeHashmap.put(paymentMode, modal_orderDetails);
                                }
                            } else {
                                paymentModeArray.add(paymentMode);
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




                                paymentModeHashmap.put(paymentMode, modal_orderDetails);
                            }
                        }
                        if(ordertype.equals(Constants.PhoneOrder)){
                            if (phoneOrderpaymentModeArray.contains(paymentMode)) {
                                boolean isAlreadyAvailabe = false;

                                try {
                                    isAlreadyAvailabe = checkIfPhoneOrderPaymentdetailisAlreadyAvailableInArray(paymentMode);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ;
                                }
                                if (isAlreadyAvailabe) {
                                    Modal_OrderDetails modal_orderDetails = phoneOrderpaymentModeHashmap.get(paymentMode);
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
                                        double credit_amount_fromhashmap = Double.parseDouble(modal_orderDetails.getCreditSales());
                                        double credit_amount = tmcprice + credit_amount_fromhashmap;
                                        double newTotalcreditAmount = credit_amount + gstAmount;
                                        modal_orderDetails.setCreditSales(String.valueOf((newTotalcreditAmount)));


                                    }
                                    phoneOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                                }
                            } else {
                                phoneOrderpaymentModeArray.add(paymentMode);
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

                                phoneOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                            }
                        }


                    if(ordertype.equals(Constants.SwiggyOrder)){
                        if (swiggyOrderpaymentModeArray.contains(paymentMode)) {
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfSwiggyOrderPaymentdetailisAlreadyAvailableInArray(paymentMode);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                Modal_OrderDetails modal_orderDetails = swiggyOrderpaymentModeHashmap.get(paymentMode);
                                if (paymentMode.equals(Constants.SWIGGYORDER_PAYMENTMODE) ) {
                                    double amount_fromhashmap = Double.parseDouble(modal_orderDetails.getSwiggySales());
                                    double amount = tmcprice + amount_fromhashmap;
                                    double newTotalCashAmount = amount + gstAmount;
                                    modal_orderDetails.setSwiggySales(String.valueOf((newTotalCashAmount)));


                                }
                                else{
                                   Toast.makeText(getApplicationContext(),"There is another Payment mode fro swiggy order",Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                if (paymentMode.equals(Constants.SWIGGYORDER_PAYMENTMODE) ) {
                                    double amount = tmcprice;
                                    double Gst_amount = gstAmount;
                                    double newTotalAmount = amount + Gst_amount;

                                    modal_orderDetails.setSwiggySales(String.valueOf((newTotalAmount)));


                                }else{
                                    Toast.makeText(getApplicationContext(),"There is another Payment mode fro swiggy order",Toast.LENGTH_LONG).show();

                                }
                                swiggyOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                            }
                        } else {
                            swiggyOrderpaymentModeArray.add(paymentMode);
                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                            if (paymentMode.equals(Constants.SWIGGYORDER_PAYMENTMODE) ) {


                                double amount = tmcprice;
                                double Gst_amount = gstAmount;

                                double newTotalAmount = amount + Gst_amount;
                                modal_orderDetails.setSwiggySales(String.valueOf((newTotalAmount)));



                            }else{
                                Toast.makeText(getApplicationContext(),"There is another Payment mode fro swiggy order",Toast.LENGTH_LONG).show();

                            }
                            swiggyOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                        }
                    }




                    if(ordertype.equals(Constants.DunzoOrder)){
                        if (dunzoOrderpaymentModeArray.contains(paymentMode)) {
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfDunzoOrderPaymentdetailisAlreadyAvailableInArray(paymentMode);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                Modal_OrderDetails modal_orderDetails = dunzoOrderpaymentModeHashmap.get(paymentMode);
                                if (paymentMode.equals(Constants.DUNZOORDER_PAYMENTMODE) ) {
                                    double amount_fromhashmap = Double.parseDouble(modal_orderDetails.getDunzoSales());
                                    double amount = tmcprice + amount_fromhashmap;
                                    double newTotalCashAmount = amount + gstAmount;
                                    modal_orderDetails.setDunzoSales(String.valueOf((newTotalCashAmount)));


                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"There is another Payment mode for dunzo order",Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                if (paymentMode.equals(Constants.DUNZOORDER_PAYMENTMODE) ) {
                                    double amount = tmcprice;
                                    double Gst_amount = gstAmount;
                                    double newTotalAmount = amount + Gst_amount;

                                    modal_orderDetails.setDunzoSales(String.valueOf((newTotalAmount)));


                                }else{
                                    Toast.makeText(getApplicationContext(),"There is another Payment mode for dunzo order",Toast.LENGTH_LONG).show();

                                }
                                dunzoOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                            }
                        } else {
                            dunzoOrderpaymentModeArray.add(paymentMode);
                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                            if (paymentMode.equals(Constants.DUNZOORDER_PAYMENTMODE) ) {


                                double amount = tmcprice;
                                double Gst_amount = gstAmount;

                                double newTotalAmount = amount + Gst_amount;
                                modal_orderDetails.setDunzoSales(String.valueOf((newTotalAmount)));



                            }else{
                                Toast.makeText(getApplicationContext(),"There is another Payment mode for dunzo order",Toast.LENGTH_LONG).show();

                            }
                            dunzoOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                        }
                    }





                    if(ordertype.equals(Constants.BigBasket)){
                        if (bigBasketOrderpaymentModeArray.contains(paymentMode)) {
                            boolean isAlreadyAvailabe = false;

                            try {
                                isAlreadyAvailabe = checkIfBigBasketOrderPaymentdetailisAlreadyAvailableInArray(paymentMode);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            if (isAlreadyAvailabe) {
                                Modal_OrderDetails modal_orderDetails = bigBasketOrderpaymentModeHashmap.get(paymentMode);
                                if (paymentMode.equals(Constants.BIGBASKETORDER_PAYMENTMODE) ) {
                                    double amount_fromhashmap = Double.parseDouble(modal_orderDetails.getBigBasketSales());
                                    double amount = tmcprice + amount_fromhashmap;
                                    double newTotalCashAmount = amount + gstAmount;
                                    modal_orderDetails.setBigBasketSales(String.valueOf((newTotalCashAmount)));


                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"There is another Payment mode for bigbasket order",Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                if (paymentMode.equals(Constants.BIGBASKETORDER_PAYMENTMODE) ) {
                                    double amount = tmcprice;
                                    double Gst_amount = gstAmount;
                                    double newTotalAmount = amount + Gst_amount;

                                    modal_orderDetails.setBigBasketSales(String.valueOf((newTotalAmount)));


                                }else{
                                    Toast.makeText(getApplicationContext(),"There is another Payment mode for bigbasket order",Toast.LENGTH_LONG).show();

                                }
                                bigBasketOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                            }
                        } else {
                            bigBasketOrderpaymentModeArray.add(paymentMode);
                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                            if (paymentMode.equals(Constants.BIGBASKETORDER_PAYMENTMODE) ) {


                                double amount = tmcprice;
                                double Gst_amount = gstAmount;

                                double newTotalAmount = amount + Gst_amount;
                                modal_orderDetails.setBigBasketSales(String.valueOf((newTotalAmount)));



                            }else{
                                Toast.makeText(getApplicationContext(),"There is another Payment mode for bigBasket order",Toast.LENGTH_LONG).show();

                            }
                            bigBasketOrderpaymentModeHashmap.put(paymentMode, modal_orderDetails);
                        }
                    }



                    String menuitemid = String.valueOf(menuitemidd);
                   /* if(menuitemid.equals("")) {
                        String ItemName2 = "";
                        if (json.has("itemname")) {
                            ItemName2 = String.valueOf(json.get("itemname"));
                        }

                        for (int menuiterator = 0; menuiterator < MenuItem.size(); menuiterator++) {
                            Modal_MenuItem_Settings modal_menuItemSettings = MenuItem.get(menuiterator);

                            String menuItemId = String.valueOf(modal_menuItemSettings.getMenuItemId());

                            if (menuitemidd.equals("")) {
                                String ItemNamefromMenu = String.valueOf(modal_menuItemSettings.getItemname().toString());
                                if (ItemName2.equals(ItemNamefromMenu)) {
                                    menuitemid = menuItemId;

                                }
                            }

                        }



                    }

                    */










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

    private boolean checkIfPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentMode_DiscountHashmap.containsKey(menuitemid);
    }

    private boolean checkIfPhoneOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return phoneOrderpaymentMode_DiscountHashmap.containsKey(menuitemid);
    }

    private boolean checkIfDunzoOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return dunzoOrderpaymentMode_DiscountHashmap.containsKey(menuitemid);
    }

    private boolean checkIfBigBasketOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return bigBasketOrderpaymentMode_DiscountHashmap.containsKey(menuitemid);
    }

    private boolean checkIfSwiggyOrderPaymentModeDiscountdetailisAlreadyAvailableInArray(String menuitemid) {
        return swiggyOrderpaymentMode_DiscountHashmap.containsKey(menuitemid);
    }

    private boolean checkIfSubCtgywiseTotalisAlreadyAvailableInArray(String menuitemid) {
        return SubCtgywiseTotalHashmap.containsKey(menuitemid);
    }

    private boolean checkIfPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return paymentModeHashmap.containsKey(menuitemid);
    }


    private boolean checkIfPhoneOrderPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return phoneOrderpaymentModeHashmap.containsKey(menuitemid);
    }

    private boolean checkIfSwiggyOrderPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return swiggyOrderpaymentModeHashmap.containsKey(menuitemid);
    }
    private boolean checkIfDunzoOrderPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return dunzoOrderpaymentModeHashmap.containsKey(menuitemid);
    }
    private boolean checkIfBigBasketOrderPaymentdetailisAlreadyAvailableInArray(String menuitemid) {
        return bigBasketOrderpaymentModeHashmap.containsKey(menuitemid);
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

    private String convertNormalDateintoReplacementTransactionDetailsDate(String sDate, String Time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
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
        if(Time.equals("STARTTIME")) {
            df = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00Z");
        }
        else{
            df = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59Z");

        }

        String Date = df.format(c1);
        return Date;
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

    private void runthread() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isReplacementTransacDetailsResponseReceivedForSelectedDate && isgetReplacementOrderForSelectedDateCalled){
                            addOrderedItemAmountDetails(Order_Item_List, OrderItem_hashmap);
                            prepareContent();
                            setAdapter();
                        }
                        else{
                            runthread();
                        }
                    }
                });
            }
        }, 15);
    }

    public String getstartDate_and_time_TransactionTable()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => 2022-03-01T10:03:14+0530 " + c);


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00Z");
        String FormattedTime = dfTime.format(c);

        return FormattedTime;
    }

    public String getendDate_and_time_TransactionTable()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => 2022-03-01T10:03:14+0530 " + c);


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd'T'23:59:59Z");
        String FormattedTime = dfTime.format(c);

        return FormattedTime;
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
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/POS Sales Report /";

      //  File fol = new File(extstoragedir, path);
        File folder = new File(path);
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
         /*   for (int i = 0; i < Order_Item_List.size(); i++) {
                String key = Order_Item_List.get(i);

                Modal_OrderDetails itemRow = OrderItem_hashmap.get(key);
                String itemName = itemRow.getItemname();
                //Log.i(Constants.TAG, "size" + (itemRow.getItemname()));


          */

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
            if(paymentModeArray.size()>0) {

            PdfPTable tablePaymentModetitle = new PdfPTable(1);
            tablePaymentModetitle.setWidthPercentage(100);
            tablePaymentModetitle.setSpacingBefore(20);


            PdfPCell paymentModertitle;
            paymentModertitle = new PdfPCell(new Phrase("POS Order Sales"));
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


            for (int i = 0; i < paymentModeArray.size(); i++) {
                double payment_AmountDouble = 0;
                double payment_AmountDiscDouble = 0;
                String replacmentFromTextview = "", refundFromTextview = "";
                double replacment_doubleFromTextview = 0, refund_doubleFromTextview = 0;

                String Payment_Amount = "", key = paymentModeArray.get(i);
                Modal_OrderDetails modal_orderDetails = paymentModeHashmap.get(key);
                Modal_OrderDetails Payment_Modewise_discount = paymentMode_DiscountHashmap.get(key);

                //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


                if ((key.toUpperCase().equals("CASH ON DELIVERY")) || (key.toUpperCase().equals("CASH"))) {

                    try {
                        replacmentFromTextview = replacementAmount_textwidget.getText().toString();
                    } catch (Exception e) {
                        replacmentFromTextview = "0";
                        e.printStackTrace();
                    }

                    try {
                        refundFromTextview = refundAmount_textwidget.getText().toString();

                    } catch (Exception e) {
                        refundFromTextview = "0";
                        e.printStackTrace();
                    }
                    try {
                        replacment_doubleFromTextview = Math.round(Double.parseDouble(replacmentFromTextview));
                    } catch (Exception e) {
                        replacment_doubleFromTextview = 0;
                        e.printStackTrace();
                    }

                    try {
                        refund_doubleFromTextview = Math.round(Double.parseDouble(refundFromTextview));

                    } catch (Exception e) {
                        refund_doubleFromTextview = 0;
                        e.printStackTrace();
                    }

                    try {
                        payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getCashOndeliverySales());
                        String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                        payment_AmountDiscDouble = Double.parseDouble(discount_String);
                        payment_AmountDouble = payment_AmountDouble - (payment_AmountDiscDouble + refund_doubleFromTextview + replacment_doubleFromTextview);
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
           if(phoneOrderpaymentModeArray.size()>0) {
               PdfPTable tablePaymentModetitle1 = new PdfPTable(1);
               tablePaymentModetitle1.setWidthPercentage(100);
               tablePaymentModetitle1.setSpacingBefore(20);


               PdfPCell paymentModertitle1;
               paymentModertitle1 = new PdfPCell(new Phrase("Phone Order Sales"));
               paymentModertitle1.setBorder(Rectangle.NO_BORDER);
               paymentModertitle1.setHorizontalAlignment(Element.ALIGN_RIGHT);
               paymentModertitle1.setVerticalAlignment(Element.ALIGN_MIDDLE);
               paymentModertitle1.setFixedHeight(25);
               paymentModertitle1.setPaddingRight(20);
               tablePaymentModetitle1.addCell(paymentModertitle1);
               layoutDocument.add(tablePaymentModetitle1);


               PdfPTable tablePaymentMode1 = new PdfPTable(4);
               tablePaymentMode1.setWidthPercentage(100);
               tablePaymentMode1.setSpacingBefore(20);
               PdfPCell paymentModeemptycell1;
               PdfPCell paymentModeemptycellone1;
               PdfPCell paymentModeitemkeycell1;
               PdfPCell paymentModeitemValueCell1;


               for (int i = 0; i < phoneOrderpaymentModeArray.size(); i++) {
                   double payment_AmountDouble = 0;
                   double payment_AmountDiscDouble = 0;

                   String Payment_Amount = "", key = phoneOrderpaymentModeArray.get(i);
                   Modal_OrderDetails modal_orderDetails = phoneOrderpaymentModeHashmap.get(key);
                   Modal_OrderDetails Payment_Modewise_discount = phoneOrderpaymentMode_DiscountHashmap.get(key);

                   //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


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


                   paymentModeemptycell1 = new PdfPCell(new Phrase(""));
                   paymentModeemptycell1.setBorder(Rectangle.NO_BORDER);
                   paymentModeemptycell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                   paymentModeemptycell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                   paymentModeemptycell1.setFixedHeight(25);
                   tablePaymentMode1.addCell(paymentModeemptycell1);

                   paymentModeemptycellone1 = new PdfPCell(new Phrase(""));
                   paymentModeemptycellone1.setBorder(Rectangle.NO_BORDER);
                   paymentModeemptycellone1.setHorizontalAlignment(Element.ALIGN_LEFT);
                   paymentModeemptycellone1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                   paymentModeemptycellone1.setFixedHeight(25);
                   tablePaymentMode1.addCell(paymentModeemptycellone1);

                   paymentModeitemkeycell1 = new PdfPCell(new Phrase(key + " :  "));
                   paymentModeitemkeycell1.setBorderColor(BaseColor.LIGHT_GRAY);
                   paymentModeitemkeycell1.setBorder(Rectangle.NO_BORDER);
                   paymentModeitemkeycell1.setMinimumHeight(25);
                   paymentModeitemkeycell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                   paymentModeitemkeycell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                   tablePaymentMode1.addCell(paymentModeitemkeycell1);


                   paymentModeitemValueCell1 = new PdfPCell(new Phrase("Rs. " + (Payment_Amount)));
                   paymentModeitemValueCell1.setBorderColor(BaseColor.LIGHT_GRAY);
                   paymentModeitemValueCell1.setBorder(Rectangle.NO_BORDER);
                   paymentModeitemValueCell1.setMinimumHeight(25);
                   paymentModeitemValueCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                   paymentModeitemValueCell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                   paymentModeitemValueCell1.setPaddingRight(10);
                   tablePaymentMode1.addCell(paymentModeitemValueCell1);


               }
               layoutDocument.add(tablePaymentMode1);
           }


            if(swiggyOrderpaymentModeArray.size()>0) {
                PdfPTable tablePaymentModetitle2 = new PdfPTable(1);
                tablePaymentModetitle2.setWidthPercentage(100);
                tablePaymentModetitle2.setSpacingBefore(20);


                PdfPCell paymentModertitle2;
                paymentModertitle2 = new PdfPCell(new Phrase("Swiggy Order Sales"));
                paymentModertitle2.setBorder(Rectangle.NO_BORDER);
                paymentModertitle2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                paymentModertitle2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                paymentModertitle2.setFixedHeight(25);
                paymentModertitle2.setPaddingRight(20);
                tablePaymentModetitle2.addCell(paymentModertitle2);
                layoutDocument.add(tablePaymentModetitle2);


                PdfPTable tablePaymentMode2 = new PdfPTable(4);
                tablePaymentMode2.setWidthPercentage(100);
                tablePaymentMode2.setSpacingBefore(20);
                PdfPCell paymentModeemptycell2;
                PdfPCell paymentModeemptycellone2;
                PdfPCell paymentModeitemkeycell2;
                PdfPCell paymentModeitemValueCell2;


                for (int i = 0; i < swiggyOrderpaymentModeArray.size(); i++) {
                    double payment_AmountDouble = 0;
                    double payment_AmountDiscDouble = 0;

                    String Payment_Amount = "", key = swiggyOrderpaymentModeArray.get(i);
                    Modal_OrderDetails modal_orderDetails = swiggyOrderpaymentModeHashmap.get(key);
                    Modal_OrderDetails Payment_Modewise_discount = swiggyOrderpaymentMode_DiscountHashmap.get(key);

                    //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


                    if ((key.toUpperCase().equals(Constants.SWIGGYORDER_PAYMENTMODE))) {
                        try {
                            payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getSwiggySales());
                            String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                            payment_AmountDiscDouble = Double.parseDouble(discount_String);
                            payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Swiggy Sales";
                        } catch (Exception e) {
                            e.printStackTrace();
                            payment_AmountDouble = 0.00;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Swiggy Sales";

                        }
                    }


                    paymentModeemptycell2 = new PdfPCell(new Phrase(""));
                    paymentModeemptycell2.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycell2.setFixedHeight(25);
                    tablePaymentMode2.addCell(paymentModeemptycell2);

                    paymentModeemptycellone2 = new PdfPCell(new Phrase(""));
                    paymentModeemptycellone2.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycellone2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycellone2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycellone2.setFixedHeight(25);
                    tablePaymentMode2.addCell(paymentModeemptycellone2);

                    paymentModeitemkeycell2 = new PdfPCell(new Phrase(key + " :  "));
                    paymentModeitemkeycell2.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemkeycell2.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemkeycell2.setMinimumHeight(25);
                    paymentModeitemkeycell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemkeycell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tablePaymentMode2.addCell(paymentModeitemkeycell2);


                    paymentModeitemValueCell2 = new PdfPCell(new Phrase("Rs. " + (Payment_Amount)));
                    paymentModeitemValueCell2.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemValueCell2.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemValueCell2.setMinimumHeight(25);
                    paymentModeitemValueCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemValueCell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeitemValueCell2.setPaddingRight(10);
                    tablePaymentMode2.addCell(paymentModeitemValueCell2);


                }
                layoutDocument.add(tablePaymentMode2);

            }



            if(dunzoOrderpaymentModeArray.size()>0) {
                PdfPTable tablePaymentModetitle3 = new PdfPTable(1);
                tablePaymentModetitle3.setWidthPercentage(100);
                tablePaymentModetitle3.setSpacingBefore(20);


                PdfPCell paymentModertitle3;
                paymentModertitle3 = new PdfPCell(new Phrase("Dunzo Order Sales"));
                paymentModertitle3.setBorder(Rectangle.NO_BORDER);
                paymentModertitle3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                paymentModertitle3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                paymentModertitle3.setFixedHeight(25);
                paymentModertitle3.setPaddingRight(20);
                tablePaymentModetitle3.addCell(paymentModertitle3);
                layoutDocument.add(tablePaymentModetitle3);


                PdfPTable tablePaymentMode3 = new PdfPTable(4);
                tablePaymentMode3.setWidthPercentage(100);
                tablePaymentMode3.setSpacingBefore(20);
                PdfPCell paymentModeemptycell3;
                PdfPCell paymentModeemptycellone3;
                PdfPCell paymentModeitemkeycell3;
                PdfPCell paymentModeitemValueCell3;


                for (int i = 0; i < dunzoOrderpaymentModeArray.size(); i++) {
                    double payment_AmountDouble = 0;
                    double payment_AmountDiscDouble = 0;

                    String Payment_Amount = "", key = dunzoOrderpaymentModeArray.get(i);
                    Modal_OrderDetails modal_orderDetails = dunzoOrderpaymentModeHashmap.get(key);
                    Modal_OrderDetails Payment_Modewise_discount = dunzoOrderpaymentMode_DiscountHashmap.get(key);

                    //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


                    if ((key.toUpperCase().equals(Constants.DUNZOORDER_PAYMENTMODE))) {
                        try {
                            payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getDunzoSales());
                            String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                            payment_AmountDiscDouble = Double.parseDouble(discount_String);
                            payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Dunzo Sales";
                        } catch (Exception e) {
                            e.printStackTrace();
                            payment_AmountDouble = 0.00;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "Dunzo Sales";

                        }
                    }


                    paymentModeemptycell3 = new PdfPCell(new Phrase(""));
                    paymentModeemptycell3.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycell3.setFixedHeight(25);
                    tablePaymentMode3.addCell(paymentModeemptycell3);

                    paymentModeemptycellone3 = new PdfPCell(new Phrase(""));
                    paymentModeemptycellone3.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycellone3.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycellone3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycellone3.setFixedHeight(25);
                    tablePaymentMode3.addCell(paymentModeemptycellone3);

                    paymentModeitemkeycell3 = new PdfPCell(new Phrase(key + " :  "));
                    paymentModeitemkeycell3.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemkeycell3.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemkeycell3.setMinimumHeight(25);
                    paymentModeitemkeycell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemkeycell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tablePaymentMode3.addCell(paymentModeitemkeycell3);


                    paymentModeitemValueCell3 = new PdfPCell(new Phrase("Rs. " + (Payment_Amount)));
                    paymentModeitemValueCell3.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemValueCell3.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemValueCell3.setMinimumHeight(25);
                    paymentModeitemValueCell3.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemValueCell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeitemValueCell3.setPaddingRight(10);
                    tablePaymentMode3.addCell(paymentModeitemValueCell3);


                }
                layoutDocument.add(tablePaymentMode3);
            }

            if(bigBasketOrderpaymentModeArray.size()>0) {
                PdfPTable tablePaymentModetitle4 = new PdfPTable(1);
                tablePaymentModetitle4.setWidthPercentage(100);
                tablePaymentModetitle4.setSpacingBefore(20);


                PdfPCell paymentModertitle4;
                paymentModertitle4 = new PdfPCell(new Phrase("BigBasket Order Sales"));
                paymentModertitle4.setBorder(Rectangle.NO_BORDER);
                paymentModertitle4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                paymentModertitle4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                paymentModertitle4.setFixedHeight(25);
                paymentModertitle4.setPaddingRight(20);
                tablePaymentModetitle4.addCell(paymentModertitle4);
                layoutDocument.add(tablePaymentModetitle4);


                PdfPTable tablePaymentMode4 = new PdfPTable(4);
                tablePaymentMode4.setWidthPercentage(100);
                tablePaymentMode4.setSpacingBefore(20);
                PdfPCell paymentModeemptycell4;
                PdfPCell paymentModeemptycellone4;
                PdfPCell paymentModeitemkeycell4;
                PdfPCell paymentModeitemValueCell4;


                for (int i = 0; i < bigBasketOrderpaymentModeArray.size(); i++) {
                    double payment_AmountDouble = 0;
                    double payment_AmountDiscDouble = 0;

                    String Payment_Amount = "", key = bigBasketOrderpaymentModeArray.get(i);
                    Modal_OrderDetails modal_orderDetails = bigBasketOrderpaymentModeHashmap.get(key);
                    Modal_OrderDetails Payment_Modewise_discount = bigBasketOrderpaymentMode_DiscountHashmap.get(key);

                    //Log.d("ExportReportActivity", "itemTotalRowsList name " + key);


                    if ((key.toUpperCase().equals(Constants.BIGBASKETORDER_PAYMENTMODE))) {
                        try {
                            payment_AmountDouble = Double.parseDouble(Objects.requireNonNull(modal_orderDetails).getBigBasketSales());
                            String discount_String = String.valueOf(Objects.requireNonNull(Payment_Modewise_discount).getCoupondiscount());
                            payment_AmountDiscDouble = Double.parseDouble(discount_String);
                            payment_AmountDouble = payment_AmountDouble - payment_AmountDiscDouble;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "BigBasket Sales";
                        } catch (Exception e) {
                            e.printStackTrace();
                            payment_AmountDouble = 0.00;
                            Payment_Amount = String.valueOf(decimalFormat.format(payment_AmountDouble));
                            key = "BigBasket Sales";

                        }
                    }


                    paymentModeemptycell4 = new PdfPCell(new Phrase(""));
                    paymentModeemptycell4.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycell4.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycell4.setFixedHeight(25);
                    tablePaymentMode4.addCell(paymentModeemptycell4);

                    paymentModeemptycellone4 = new PdfPCell(new Phrase(""));
                    paymentModeemptycellone4.setBorder(Rectangle.NO_BORDER);
                    paymentModeemptycellone4.setHorizontalAlignment(Element.ALIGN_LEFT);
                    paymentModeemptycellone4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeemptycellone4.setFixedHeight(25);
                    tablePaymentMode4.addCell(paymentModeemptycellone4);

                    paymentModeitemkeycell4 = new PdfPCell(new Phrase(key + " :  "));
                    paymentModeitemkeycell4.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemkeycell4.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemkeycell4.setMinimumHeight(25);
                    paymentModeitemkeycell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemkeycell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tablePaymentMode4.addCell(paymentModeitemkeycell4);


                    paymentModeitemValueCell4 = new PdfPCell(new Phrase("Rs. " + (Payment_Amount)));
                    paymentModeitemValueCell4.setBorderColor(BaseColor.LIGHT_GRAY);
                    paymentModeitemValueCell4.setBorder(Rectangle.NO_BORDER);
                    paymentModeitemValueCell4.setMinimumHeight(25);
                    paymentModeitemValueCell4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    paymentModeitemValueCell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    paymentModeitemValueCell4.setPaddingRight(10);
                    tablePaymentMode4.addCell(paymentModeitemValueCell4);


                }
                layoutDocument.add(tablePaymentMode4);
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

