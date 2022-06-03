package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_AutoCompleteManageOrdersItem;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;
import com.pos.printer.AsyncEscPosPrint;
import com.pos.printer.AsyncEscPosPrinter;
import com.pos.printer.AsyncUsbEscPosPrint;
import com.pos.printer.Modal_USBPrinter;
import com.pos.printer.usb.UsbPrintersConnectionsLocal;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.WebSocket;

public class Pos_Orders_List extends AppCompatActivity {
    TextView fetchData,appOrdersCount_textwidget,dateSelector_text,mobile_orderinstruction, mobile_nameofFacility_Textview;
    Button mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;
    Adapter_AutoCompleteManageOrdersItem adapter;

    String mobile_jsonString,orderStatus,vendorKey,vendorname,TAG = "Tag";
    String DateString,PreviousDateString;
    ListView manageOrders_ListView;
    public LinearLayout PrintReport_Layout;
    public LinearLayout generateReport_Layout;
    public LinearLayout dateSelectorLayout;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;
    DatePickerDialog datepicker;

    List<Modal_ManageOrders_Pojo_Class> websocket_OrdersList;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    public static String completemenuItem;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay,TodaysDate,DeliveryPersonList;
    List<String> slotnameChoosingSpinnerData;
    Spinner slotType_Spinner;
    int slottypefromSpinner=0;
    static Adapter_Mobile_SearchOrders_usingMobileNumber_ListView adapter_mobileSearchOrders_usingMobileNumber_listView;
    static Adapter_Pos_SearchOrders_usingMobileNumber adapter_PosSearchOrders_usingMobileNumber_listView;
    Workbook wb;
    Sheet sheet=null;
    private static String[] columns = {"Delivery Type","Token No", "Order Status",
            "Order Placed Time","Slot Date","Slot Time Range","Order Ready Time","Order Delivered Time ","Orderid","User Address","User Mobile"};

    private String SERVER_PATH = "wss://hx9itd7ji2.execute-api.ap-south-1.amazonaws.com/Dev";
    WebSocket webSocket;
    private Context mContext;

    boolean isSearchButtonClicked = false;
    double screenInches;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    public static List<String> array_of_orderId;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.POSOrdersList";

    Modal_USBPrinter modal_usbPrinter = new Modal_USBPrinter();
    boolean isUSBPrintReciptMethodCalled = false;


    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";


    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos__orders__list);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", ""));
            vendorname = (shared.getString("VendorName", ""));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
           // orderdetailsnewschema = true;

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(Pos_Orders_List .this);
        //    Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
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

        //
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        websocket_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        slotnameChoosingSpinnerData = new ArrayList<>();
        array_of_orderId = new ArrayList<>();
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        fetchData  = findViewById(R.id.fetchData);


        slotType_Spinner = findViewById(R.id.slotType_Spinner);
        applaunchimage = findViewById(R.id.applaunchimage);
        manageOrders_ListView = findViewById(R.id.manageOrders_ListView);
        mobile_orderinstruction = findViewById(R.id.orderinstruction);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        generateReport_Layout = findViewById(R.id.generateReport_Layout);
        //
        mobile_nameofFacility_Textview = findViewById(R.id.nameofFacility_Textview);
        mobile_search_button = findViewById(R.id.search_button);
        mobile_search_barEditText = findViewById(R.id.search_barEdit);
        mobile_search_close_btn = findViewById(R.id.search_close_btn);

        //
        mobile_new_Order_widget = findViewById(R.id.new_Order_widget);
        mobile_confirmed_Order_widget = findViewById(R.id.confirmed_Order_widget);
        mobile_ready_Order_widget = findViewById(R.id.ready_Order_widget);
        mobile_transist_Order_widget = findViewById(R.id.transist_Order_widget);
        mobile_delivered_Order_widget = findViewById(R.id.delivered_Order_widget);

        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        mobile_nameofFacility_Textview.setText(vendorname);
        try{
          //  TodaysDate = getDate();
          //  PreviousDateString = getDatewithNameofthePreviousDay();
            //Now we are creating sheet

           //Adjusting_Widgets_Visibility(true);
         //   String Todaysdate = getDatewithNameoftheDay();
        //    PreviousDateString = getDatewithNameofthePreviousDay();

            isSearchButtonClicked = false;
            orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;
            ordersList.clear();
            sorted_OrdersList.clear();
            array_of_orderId.clear();

            dateSelector_text.setText(Constants.Empty_Date_Format);
            DateString = (Constants.Empty_Date_Format);
           /* if(orderdetailsnewschema){
                String dateAsOldFormat =convertnewFormatDateintoOldFormat(Todaysdate);
                dateSelector_text.setText(dateAsOldFormat);
                callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);


            }
            else{
                dateSelector_text.setText(TodaysDate);

           //     getOrderDetailsUsingOrderOrderPlacedDate(Todaysdate, vendorKey, orderStatus);

            }


            */

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            wb=new HSSFWorkbook();
            //Now we are creating sheet

            sheet = wb.createSheet("ApporderDetails");


        }
        catch (Exception e){
            e.printStackTrace();
        }

        /*
        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();

                Adjusting_Widgets_Visibility(true);
                String Todaysdate =  dateSelector_text.getText().toString();
                PreviousDateString = getDatewithNameofthePreviousDay();

                isSearchButtonClicked = false;
                orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;

                if(orderdetailsnewschema){
                   String dateAsNewFormat =convertOldFormatDateintoNewFormat(Todaysdate);
                   // dateSelector_text.setText(dateAsOldFormat);
                    callVendorOrderDetailsSeviceAndInitCallBack(dateAsNewFormat,dateAsNewFormat,vendorKey);


                }
                else{
                    dateSelector_text.setText(TodaysDate);

                    getOrderDetailsUsingOrderOrderPlacedDate(TodaysDate, vendorKey, orderStatus);

                }

               // getOrderDetailsUsingOrderOrderPlacedDate(TodaysDate, vendorKey, orderStatus);

            }
        });

         */
        mobile_search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked = false;

                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner);
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = mobile_search_barEditText.getText().toString().length();
                isSearchButtonClicked = true;

                showKeyboard(mobile_search_barEditText);
                showSearchBarEditText();
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
        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Pos_Orders_List.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });
        generateReport_Layout.setVisibility(View.GONE);
        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();

                Adjusting_Widgets_Visibility(true);
                String Todaysdate = dateSelector_text.getText().toString();
                PreviousDateString = getDatewithNameofthePreviousDay();

                isSearchButtonClicked = false;
                orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;
                getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);
           */
                Adjusting_Widgets_Visibility(true);

                AddDatatoExcelSheet();
            }
        });

        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isSearchButtonClicked = false;
                orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;
                if (DateString.equals(Constants.Empty_Date_Format)) {
                    Toast.makeText(Pos_Orders_List.this, "Select the Date First !!! ", Toast.LENGTH_SHORT).show();
                } else {
                    Adjusting_Widgets_Visibility(true);

                    if (orderdetailsnewschema) {
                        String dateAsNewFormat = convertOldFormatDateintoNewFormat(DateString);
                        // dateSelector_text.setText(dateAsOldFormat);
                        callVendorOrderDetailsSeviceAndInitCallBack(dateAsNewFormat, dateAsNewFormat, vendorKey);


                    } else {
                        //  dateSelector_text.setText(Todaysdate);
                        PreviousDateString = getDatewithNameofthePreviousDay2(DateString);

                        getOrderDetailsUsingOrderOrderPlacedDate(DateString, vendorKey, orderStatus);

                    }

                }
            }
        });

        mobile_search_barEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sorted_OrdersList.clear();

            }

            @Override
            public void afterTextChanged(Editable editable) {
                sorted_OrdersList.clear();
                isSearchButtonClicked =true;
                String mobileNo = (editable.toString());
                if(!mobileNo.equals("")) {
                    String orderstatus = "";

                    for (int i = 0; i < ordersList.size(); i++) {
                        try {
                            //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));
                            mobileNo = mobileNo;
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                            String mobilenumber = modal_manageOrders_forOrderDetailList.getUsermobile();
                            //Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                            //Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + mobilenumber);
                            //Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + mobileNo);
                            if (mobilenumber.contains("+91" + mobileNo)) {

                                //Log.d(Constants.TAG, "displayorderDetailsinListview orderid: " + modal_manageOrders_forOrderDetailList.getOrderid());
                                orderstatus = modal_manageOrders_forOrderDetailList.getOrderstatus();
                                modal_manageOrders_forOrderDetailList1.orderid = modal_manageOrders_forOrderDetailList.getOrderid();
                                modal_manageOrders_forOrderDetailList1.orderplacedtime = modal_manageOrders_forOrderDetailList.getOrderplacedtime();
                                modal_manageOrders_forOrderDetailList1.payableamount = modal_manageOrders_forOrderDetailList.getPayableamount();
                                modal_manageOrders_forOrderDetailList1.paymentmode = modal_manageOrders_forOrderDetailList.getPaymentmode();
                                modal_manageOrders_forOrderDetailList1.tokenno = modal_manageOrders_forOrderDetailList.getTokenno();
                                modal_manageOrders_forOrderDetailList1.taxamount = modal_manageOrders_forOrderDetailList.getTaxamount();
                                modal_manageOrders_forOrderDetailList1.usermobile = modal_manageOrders_forOrderDetailList.getUsermobile();
                                modal_manageOrders_forOrderDetailList1.vendorkey = modal_manageOrders_forOrderDetailList.getVendorkey();
                                modal_manageOrders_forOrderDetailList1.coupondiscamount = modal_manageOrders_forOrderDetailList.getCoupondiscamount();
                                modal_manageOrders_forOrderDetailList1.itemdesp = modal_manageOrders_forOrderDetailList.getItemdesp();
                                modal_manageOrders_forOrderDetailList1.keyfromtrackingDetails = modal_manageOrders_forOrderDetailList.getKeyfromtrackingDetails();
                                modal_manageOrders_forOrderDetailList1.deliveryPartnerKey = modal_manageOrders_forOrderDetailList.getDeliveryPartnerKey();
                                modal_manageOrders_forOrderDetailList1.deliveryPartnerMobileNo = modal_manageOrders_forOrderDetailList.getDeliveryPartnerMobileNo();
                                modal_manageOrders_forOrderDetailList1.deliveryPartnerName = modal_manageOrders_forOrderDetailList.getDeliveryPartnerName();
                                modal_manageOrders_forOrderDetailList1.orderType = modal_manageOrders_forOrderDetailList.getOrderType();
                                modal_manageOrders_forOrderDetailList1.orderstatus = modal_manageOrders_forOrderDetailList.getOrderstatus();
                                modal_manageOrders_forOrderDetailList1.deliverytype = modal_manageOrders_forOrderDetailList.getDeliverytype();
                                modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                                modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                                modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                                modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                                modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();

                                modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();

                                modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                                modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                                modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                                modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();

                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    try {
                        if (sorted_OrdersList.size() > 0) {
                            manageOrders_ListView.setVisibility(View.VISIBLE);
                            mobile_orderinstruction.setVisibility(View.GONE);
                            if(screenInches>Constants.default_mobileScreenSize){

                                adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(Pos_Orders_List.this, sorted_OrdersList, Pos_Orders_List.this);
                                manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                            }else {
                                adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Pos_Orders_List.this, sorted_OrdersList, Pos_Orders_List.this);
                                manageOrders_ListView.setAdapter(adapter_mobileSearchOrders_usingMobileNumber_listView);
                            }
                        } else {
                            manageOrders_ListView.setVisibility(View.GONE);
                            mobile_orderinstruction.setVisibility(View.VISIBLE);
                            mobile_orderinstruction.setText("No orders found for this Mobile number");


                        }
                    } catch (Exception E) {
                        E.printStackTrace();
                    }


                }
                else{
                    manageOrders_ListView.setVisibility(View.GONE);
                    mobile_orderinstruction.setVisibility(View.VISIBLE);
                    mobile_orderinstruction.setText("No orders found for this Mobile number");

                }

            }
        });








    }



    private void AddDatatoExcelSheet() {
        int rowNum = 1;



        for (int ii = 0; ii < ordersList.size(); ii++) {

            Modal_ManageOrders_Pojo_Class itemRow = ordersList.get(ii);



            Cell headercell = null;

            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(HSSFColor.RED.index);

            CellStyle headerCellStyle = wb.createCellStyle();
            headerCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            headerCellStyle.setFont(headerFont);


            org.apache.poi.ss.usermodel.Font contentFont = wb.createFont();
            contentFont.setBold(false);
            contentFont.setFontHeightInPoints((short) 10);
            contentFont.setColor(HSSFColor.BLACK.index);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFont(contentFont);


            //Now column and row
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                headercell = headerRow.createCell(i);
                headercell.setCellValue(columns[i]);
                headercell.setCellStyle(headerCellStyle);
            }

            try {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(itemRow.getDeliverytype());
                row.createCell(1).setCellValue(itemRow.getTokenno());
                row.createCell(2).setCellValue(itemRow.getOrderstatus());
                row.createCell(3).setCellValue(itemRow.getOrderplacedtime());
                row.createCell(4).setCellValue(itemRow.getSlotdate());
                row.createCell(5).setCellValue(itemRow.getSlottimerange());
                row.createCell(6).setCellValue(itemRow.getOrderreadytime());
                row.createCell(7).setCellValue(itemRow.getOrderdeliveredtime());
                row.createCell(8).setCellValue(itemRow.getOrderid());
                row.createCell(9).setCellValue(itemRow.getUseraddress());
                row.createCell(10).setCellValue(itemRow.getUsermobile());
            }
            catch (Exception e){
                e.printStackTrace();
            }



            sheet.setColumnWidth(0, (10 * 200));
            sheet.setColumnWidth(1, (10 * 200));


            if (rowNum == ordersList.size()-1) {
//                Toast.makeText(getApplicationContext(),+ordersList.size(),Toast.LENGTH_LONG).show();

                GenerateExcelSheet();
            }


        }

    }

    private void GenerateExcelSheet() {


        File file = new File(getExternalFilesDir(null),"Onlineorderdetails.xls");
        FileOutputStream outputStream =null;

        try {
            outputStream=new FileOutputStream(file);
            wb.write(outputStream);
            Adjusting_Widgets_Visibility(false);

            Toast.makeText(getApplicationContext(),"File Created",Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);

            Toast.makeText(getApplicationContext(),"File can't be  Created",Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }












    }

    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(Pos_Orders_List.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();
                            displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner);
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
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            manageOrders_ListView.setVisibility(View.GONE);
                            mobile_orderinstruction.setVisibility(View.VISIBLE);
                            mobile_orderinstruction.setText("After Selecting the Date !! Click Fetch Data");


                          /*  //getOrderForSelectedDate(DateString, vendorKey);
                            if(orderdetailsnewschema){
                                String dateAsNewFormat =convertOldFormatDateintoNewFormat(DateString);
                                callVendorOrderDetailsSeviceAndInitCallBack(dateAsNewFormat,dateAsNewFormat,vendorKey);


                            }
                            else{
                                getOrderDetailsUsingOrderOrderPlacedDate(DateString, vendorKey, orderStatus);

                            }
                            //getOrderDetailsUsingOrderOrderPlacedDate(DateString, vendorKey, orderStatus);


                           */
                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }

    private void getOrderDetailsUsingOrderOrderPlacedDate(String SlotDate, String vendorKey, String selectedStatus) {
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );
        Adjusting_Widgets_Visibility(true);

        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();


        SharedPreferences sharedPreferences
                = Pos_Orders_List.this.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey + "?orderplaceddate="+SlotDate+"&vendorkey="+vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            mobile_jsonString = response.toString();

                            convertingJsonStringintoArray(selectedStatus, mobile_jsonString);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Pos_Orders_List.this, "There is no Order  on " + SlotDate, Toast.LENGTH_LONG).show();
                    ordersList.clear();
                    array_of_orderId.clear();

//                adapter_mobileSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();
                    Adjusting_Widgets_Visibility(false);
                    appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                    error.printStackTrace();

                /*if(orderStatus.equals("TODAYS"+Constants.PREORDER_SLOTNAME)){

                    Adjusting_Widgets_Visibility(true);
                    String TomorrowsDate = getTomorrowsDate();
                    isSearchButtonClicked =false;
                    orderStatus="TOMORROWS"+Constants.PREORDER_SLOTNAME;
                    getOrderDetailsUsingOrderSlotDate(TomorrowsDate, vendorKey, orderStatus);
                }
                if(orderStatus.equals("TOMORROWS"+Constants.PREORDER_SLOTNAME)){

                    // saveorderDetailsInLocal(ordersList);
                    Adjusting_Widgets_Visibility(false);

                }

                 */
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        })
        {
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(Pos_Orders_List.this).add(jsonObjectRequest);




    }












    private void convertingJsonStringintoArray(String orderStatus, String jsonString) {
        try {
            String ordertype="#",orderid="";
            sorted_OrdersList.clear();

            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for(;i1<(arrayLength);i1++) {

                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));

                    if(json.has("ordertype")){
                        manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                    }
                    else{
                        ordertype="#";
                        manageOrdersPojoClass.orderType ="";
                    }

                    if((ordertype.toUpperCase().equals(Constants.POSORDER))||(ordertype.toUpperCase().equals(Constants.DunzoOrder))||(ordertype.toUpperCase().equals(Constants.SwiggyOrder))||(ordertype.toUpperCase().equals(Constants.PhoneOrder)) ||(ordertype.toUpperCase().equals(Constants.BigBasket))){


                        if(json.has("orderid")){
                        manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));
                        orderid = String.valueOf(json.get("orderid"));
                    }
                    else{
                        manageOrdersPojoClass.orderid ="";
                    }

                    if (!array_of_orderId.contains(orderid)) {
                        array_of_orderId.add(orderid);
                    }


                    if(json.has("notes")){
                        manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                    }
                    else{
                        manageOrdersPojoClass.notes ="";
                    }



                    if(json.has("orderplacedtime")){
                        manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));

                    }
                    else{
                        manageOrdersPojoClass.orderplacedtime ="";
                    }




                    if(json.has("payableamount")){
                        manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));

                    }
                    else{
                        manageOrdersPojoClass.payableamount ="";
                    }


                    if(json.has("paymentmode")){
                        manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));

                    }
                    else{
                        manageOrdersPojoClass.paymentmode ="";
                    }




                    if(json.has("tokenno")){
                        manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));

                    }
                    else{
                        manageOrdersPojoClass.tokenno ="";
                    }





                    try {
                        if (json.has("itemdesp")) {
                            JSONArray itemdesp = json.getJSONArray("itemdesp");

                            manageOrdersPojoClass.itemdesp = itemdesp;

                        } else {
                            //Log.i(Constants.TAG, "Can't Get itemDesp");
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    if(json.has("orderStatus")){
                        manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderStatus"));

                    }
                    else{
                        manageOrdersPojoClass.orderstatus ="";
                    }


                    if(json.has("usermobile")){
                        manageOrdersPojoClass.usermobile =  String.valueOf(json.get("usermobile"));

                    }
                    else{
                        manageOrdersPojoClass.usermobile ="";
                    }


                    if(json.has("vendorkey")){
                        manageOrdersPojoClass.vendorkey =  String.valueOf(json.get("vendorkey"));

                    }
                    else{
                        manageOrdersPojoClass.vendorkey ="vendor_1";
                    }


                    if(json.has("orderdetailskey")){
                        manageOrdersPojoClass.orderdetailskey =  String.valueOf(json.get("orderdetailskey"));

                    }
                    else{
                        manageOrdersPojoClass.orderdetailskey ="";
                    }


                    if(json.has("orderdeliveredtime")){
                        manageOrdersPojoClass.orderdeliveredtime =  String.valueOf(json.get("orderdeliveredtime"));

                    }
                    else{
                        manageOrdersPojoClass.orderdeliveredtime ="";
                    }
                    if(json.has("useraddresskey")){
                        manageOrdersPojoClass.useraddresskey =  String.valueOf(json.get("useraddresskey"));

                    }
                    else{
                        manageOrdersPojoClass.useraddresskey ="";
                    }


                    if(json.has("orderreadytime")){
                        manageOrdersPojoClass.orderreadytime = String.valueOf(json.get("orderreadytime"));

                    }
                    else{
                        manageOrdersPojoClass.orderreadytime ="";
                    }


                    if(json.has("orderpickeduptime")){
                        manageOrdersPojoClass.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                    }
                    else{
                        manageOrdersPojoClass.orderpickeduptime ="";
                    }


                    if(json.has("orderconfirmedtime")){
                        manageOrdersPojoClass.orderconfirmedtime =  String.valueOf(json.get("orderconfirmedtime"));

                    }
                    else{
                        manageOrdersPojoClass.orderconfirmedtime ="";
                    }


                    if(json.has("coupondiscount")){
                        manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscount"));

                    }
                    else{
                        manageOrdersPojoClass.coupondiscamount ="";
                    }


                    if(json.has("deliverytype")){
                        manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));

                    }
                    else{
                        manageOrdersPojoClass.deliverytype ="";
                    }



                    if(json.has("slottimerange")){
                        manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                    }
                    else{
                        manageOrdersPojoClass.slottimerange ="";
                    }



                    if(json.has("slotdate")){
                        manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));

                    }
                    else{
                        manageOrdersPojoClass.slotdate ="";
                    }


                    if(json.has("slotname")){
                        manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                    }
                    else{
                        manageOrdersPojoClass.slotname ="";
                    }
                    if(json.has("slottimerange")){
                        manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                    }
                    else{
                        manageOrdersPojoClass.slottimerange ="";
                    }
                    if(json.has("deliverytype")){
                        manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));

                    }
                    else{
                        manageOrdersPojoClass.deliverytype ="";
                    }


                    if(json.has("useraddresslat")){
                        manageOrdersPojoClass.useraddresslat =  String.valueOf(json.get("useraddresslat"));

                    }
                    else{
                        manageOrdersPojoClass.useraddresslat ="";
                    }


                    if(json.has("useraddresslong")){
                        manageOrdersPojoClass.useraddresslon =  String.valueOf(json.get("useraddresslong"));

                    }
                    else{
                        manageOrdersPojoClass.useraddresslon ="";
                    }

                    if(json.has("keyfromtrackingDetails")){
                        manageOrdersPojoClass.keyfromtrackingDetails = String.valueOf(json.get("keyfromtrackingDetails"));

                    }
                    else{
                        manageOrdersPojoClass.keyfromtrackingDetails ="";
                    }

                    try {
                        if((ordertype.toUpperCase().equals(Constants.POSORDER))||(ordertype.toUpperCase().equals(Constants.DunzoOrder))||(ordertype.toUpperCase().equals(Constants.SwiggyOrder))||(ordertype.toUpperCase().equals(Constants.PhoneOrder)) ||(ordertype.toUpperCase().equals(Constants.BigBasket))){
                            if (json.has("useraddress")) {

                                String addresss =  String.valueOf(json.get("useraddress"));
                                if(!addresss.equals(null)&&(!addresss.equals("null"))){
                                    manageOrdersPojoClass.useraddress = String.valueOf(json.get("useraddress"));

                                }
                                else {
                                    manageOrdersPojoClass.useraddress ="";

                                }
                            } else {
                                manageOrdersPojoClass.useraddress = "";
                            }

                        }
                    }catch (Exception E){
                        manageOrdersPojoClass.useraddress ="-";
                        E.printStackTrace();
                    }





                        if((ordertype.toUpperCase().equals(Constants.POSORDER))||(ordertype.toUpperCase().equals(Constants.DunzoOrder))||(ordertype.toUpperCase().equals(Constants.SwiggyOrder))||(ordertype.toUpperCase().equals(Constants.PhoneOrder)) ||(ordertype.toUpperCase().equals(Constants.BigBasket))){

                        ordersList.add(manageOrdersPojoClass);
                    }
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                }} catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }


            }

            //Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);
        /*    if(orderStatus.equals("TODAYS"+Constants.PREORDER_SLOTNAME)){
                Adjusting_Widgets_Visibility(true);
                String TomorrowsDate = getTomorrowsDate();
                isSearchButtonClicked =false;
                orderStatus="TOMORROWS"+Constants.PREORDER_SLOTNAME;
                getOrderDetailsUsingOrderSlotDate(TomorrowsDate, vendorKey, orderStatus);
            }
            if(orderStatus.equals("TOMORROWS"+Constants.PREORDER_SLOTNAME)){

               // saveorderDetailsInLocal(ordersList);
                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

            }

         */

            displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







    private void displayorderDetailsinListview(String orderStatus, @NotNull List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner) {


        appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));

        //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());
        sorted_OrdersList.clear();
        String TodaysDate = getDatewithNameoftheDay();
        String TomorrowsDate = getTomorrowsDate();
        //Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

        //Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);
        if(ordersList.size()>0) {
            for (int i = 0; i < ordersList.size(); i++) {
                try {
                    //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                    String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                    String slotDate = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotdate());
                    String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();


                    //Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

                    //Log.d(Constants.TAG, "displayorderDetailsinListview slotDate: " + slotDate);

                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);

                    modal_manageOrders_forOrderDetailList1.orderid = modal_manageOrders_forOrderDetailList.getOrderid();
                    modal_manageOrders_forOrderDetailList1.orderplacedtime = modal_manageOrders_forOrderDetailList.getOrderplacedtime();
                    modal_manageOrders_forOrderDetailList1.payableamount = modal_manageOrders_forOrderDetailList.getPayableamount();
                    modal_manageOrders_forOrderDetailList1.paymentmode = modal_manageOrders_forOrderDetailList.getPaymentmode();
                    modal_manageOrders_forOrderDetailList1.tokenno = modal_manageOrders_forOrderDetailList.getTokenno();
                    modal_manageOrders_forOrderDetailList1.taxamount = modal_manageOrders_forOrderDetailList.getTaxamount();
                    modal_manageOrders_forOrderDetailList1.usermobile = modal_manageOrders_forOrderDetailList.getUsermobile();
                    modal_manageOrders_forOrderDetailList1.vendorkey = modal_manageOrders_forOrderDetailList.getVendorkey();
                    modal_manageOrders_forOrderDetailList1.coupondiscamount = modal_manageOrders_forOrderDetailList.getCoupondiscamount();
                    modal_manageOrders_forOrderDetailList1.itemdesp = modal_manageOrders_forOrderDetailList.getItemdesp();
                    modal_manageOrders_forOrderDetailList1.keyfromtrackingDetails = modal_manageOrders_forOrderDetailList.getKeyfromtrackingDetails();
                    modal_manageOrders_forOrderDetailList1.deliveryPartnerKey = modal_manageOrders_forOrderDetailList.getDeliveryPartnerKey();
                    modal_manageOrders_forOrderDetailList1.deliveryPartnerMobileNo = modal_manageOrders_forOrderDetailList.getDeliveryPartnerMobileNo();
                    modal_manageOrders_forOrderDetailList1.deliveryPartnerName = modal_manageOrders_forOrderDetailList.getDeliveryPartnerName();
                    modal_manageOrders_forOrderDetailList1.orderType = modal_manageOrders_forOrderDetailList.getOrderType();
                    modal_manageOrders_forOrderDetailList1.orderstatus = modal_manageOrders_forOrderDetailList.getOrderstatus();
                    modal_manageOrders_forOrderDetailList1.deliverytype = modal_manageOrders_forOrderDetailList.getDeliverytype();
                    modal_manageOrders_forOrderDetailList1.vendorkey = modal_manageOrders_forOrderDetailList.getVendorkey();
                    modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                    modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                    modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                    modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();

                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                    modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();


                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    try {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                    } catch (Exception e) {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                    }

                    if ((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210")) && (!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            try {
                if (sorted_OrdersList.size() > 0) {
                    if (orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)) {
                        Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                            public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                                return object2.getOrderdeliveredtime().compareTo(object1.getOrderdeliveredtime());
                            }
                        });
                    }





/*
                for (int i=0; i<ordersList.size()-1; i++) {
                    for (int j=0; j<ordersList.size()-1; j++) {
                        if (Integer.parseInt(ordersList.get(j).getTokenno()) > Integer.parseInt(ordersList.get(j+1).getTokenno())) {
                            ordersList.add(j, ordersList.get(j + 1)); //This line inserts the smaller value
                            ordersList.remove(j+2);                //into the first index and pushes the
                        }                                           //indices down 1. So I need to remove
                        //j+2 not j+1.


                        System.out.println(ordersList.toString() + j + "  << Second Loop Interation");
                    }
                    System.out.println(ordersList.toString() + i + " << First Loop interation");

                }

        */

                    if (screenInches > Constants.default_mobileScreenSize) {

                        adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(Pos_Orders_List.this, sorted_OrdersList, Pos_Orders_List.this);
                        manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                    } else {
                        adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Pos_Orders_List.this, sorted_OrdersList, Pos_Orders_List.this);
                        manageOrders_ListView.setAdapter(adapter_mobileSearchOrders_usingMobileNumber_listView);
                    }


                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.VISIBLE);
                    mobile_orderinstruction.setVisibility(View.GONE);


                } else {
                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.GONE);
                    mobile_orderinstruction.setText("No Order today");

                    mobile_orderinstruction.setVisibility(View.VISIBLE);

                }
            } catch (Exception e) {
                e.printStackTrace();
                if (sorted_OrdersList.size() > 0) {
                    if (screenInches > Constants.default_mobileScreenSize) {

                        adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(Pos_Orders_List.this, sorted_OrdersList, Pos_Orders_List.this);
                        manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                    } else {
                        adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Pos_Orders_List.this, sorted_OrdersList, Pos_Orders_List.this);
                        manageOrders_ListView.setAdapter(adapter_mobileSearchOrders_usingMobileNumber_listView);
                    }

                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.VISIBLE);
                    mobile_orderinstruction.setVisibility(View.GONE);


                } else {
                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.GONE);
                    mobile_orderinstruction.setText("No Order today");

                    mobile_orderinstruction.setVisibility(View.VISIBLE);

                }
            }

        }
        else{
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.GONE);
            mobile_orderinstruction.setText("No Order today");

            mobile_orderinstruction.setVisibility(View.VISIBLE);
        }
//callAdapter();
    }



    public void PrintReciptUsingUSBPrinter(List<Modal_ManageOrders_Pojo_Class> selectedBillDetails) {
        if(isUSBPrintReciptMethodCalled){
            return;
        }
        isUSBPrintReciptMethodCalled = true;


        Modal_ManageOrders_Pojo_Class selectedOrder = selectedBillDetails.get(0);

        modal_usbPrinter = new Modal_USBPrinter();
        try{

            modal_usbPrinter.orderstatus = selectedOrder.getOrderstatus();
            modal_usbPrinter.userMobile = selectedOrder.getUsermobile();
            modal_usbPrinter.tokenno = selectedOrder.getTokenno();
            modal_usbPrinter.payableAmount = selectedOrder.getPayableamount();
            modal_usbPrinter.itemdesp = selectedOrder.getItemdesp();
            modal_usbPrinter.orderid = selectedOrder.getOrderid();
            modal_usbPrinter.payment_mode = selectedOrder.getPaymentmode();
            modal_usbPrinter.finalCouponDiscountAmount = selectedOrder.getCoupondiscamount();
            modal_usbPrinter.useraddress = selectedOrder.getUseraddress();
            modal_usbPrinter.ordertype = selectedOrder.getOrderType();
            modal_usbPrinter.slotname = selectedOrder.getSlotname();
            modal_usbPrinter.slotdate= selectedOrder.getSlotdate();
            modal_usbPrinter.slottimerange =selectedOrder. getSlottimerange();
            modal_usbPrinter.deliverytype = selectedOrder.getDeliverytype();
            modal_usbPrinter.notes = selectedOrder.getNotes();
            modal_usbPrinter.orderplacedtime = selectedOrder.getOrderplacedtime();
            modal_usbPrinter.orderdetailskey = selectedOrder.getOrderdetailskey();
            modal_usbPrinter.deliverydistance =selectedOrder.getDeliverydistance();
            modal_usbPrinter.payment_mode =selectedOrder. getPaymentmode();



        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            UsbConnection usbConnection = UsbPrintersConnectionsLocal.selectFirstConnected(Pos_Orders_List.this);
            UsbManager usbManager = (UsbManager) Pos_Orders_List.this.getSystemService(Context.USB_SERVICE);

            if (usbConnection == null || usbManager == null) {
            /*    new AlertDialog.Builder(Pos_Orders_List.this)
                        .setTitle("USB Connection")
                        .setMessage("No USB printer found.")
                        .show();

             */
                isUSBPrintReciptMethodCalled = false;

                new TMCAlertDialogClass(Pos_Orders_List.this, R.string.app_name, R.string.ReConnect_Instruction,
                        R.string.OK_Text, R.string.Cancel_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                PrintReciptUsingUSBPrinter(selectedBillDetails);
                            }

                            @Override
                            public void onNo() {

                            }
                        });

                return;
            }

            PendingIntent permissionIntent = PendingIntent.getBroadcast(
                    Pos_Orders_List.this,
                    0,
                    new Intent(ACTION_USB_PERMISSION),
                    android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
            );
            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
            registerReceiver(usbReceiver, filter);
            usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context, new AsyncEscPosPrint.OnPrintFinished() {
                                @Override
                                public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                    Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                    isUSBPrintReciptMethodCalled = false;
                                    showProgressBar(false);
                                    try{
                                        unregisterReceiver(usbReceiver);
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
                                }

                                @Override
                                public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                    Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");
                                    try{
                                        unregisterReceiver(usbReceiver);
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
                                    isUSBPrintReciptMethodCalled = false;
                                    showProgressBar(false);

                                }
                            }
                            )
                                    .execute(getAsyncEscPosPrinter(new UsbConnection(usbManager, usbDevice)));
                        }
                    }
                }
            }
        }
    };



    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinter(DeviceConnection printerConnection) {
        showProgressBar(true);

        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        String OrderPlacedtime = "";
        String Orderid = "";
        String CouponDiscount ="";
        String OrderType = "";
        String PayableAmountfromArray = "";
        String PayableAmount = "";
        String PaymentMode = "";
        String MobileNumber ="";
        String TokenNo="";
        String Notes ="";
        String Slotname ="";
        String SlotDate = "";
        String DeliveryTime = "";
        String DeliveryType ="";
        String DeliveryAmount =  "";
        String DistanceFromStore ="";
        String Address =  "";
        JSONArray itemdesp = new JSONArray();

        double totalAmountFromAddingSubtotal=0;
        double couponDiscount_double=0;
        double deliveryAmount_double=0;
        double totalAmountFromAddingSubtotalWithDiscount =0;
        double totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt =0;



        try{
            itemdesp = modal_usbPrinter.getItemdesp();
            OrderPlacedtime = modal_usbPrinter.getOrderplacedtime();
            Orderid = modal_usbPrinter.getOrderid();
            CouponDiscount = modal_usbPrinter.getFinalCouponDiscountAmount();
            OrderType = modal_usbPrinter.getOrdertype();
            PayableAmount = modal_usbPrinter.getPayableAmount();
            PaymentMode = modal_usbPrinter.getPayment_mode();
            MobileNumber = modal_usbPrinter.getUserMobile();
            TokenNo = modal_usbPrinter.getTokenno();
            Notes = modal_usbPrinter.getNotes();
            Slotname = modal_usbPrinter.getSlotname();
            SlotDate = modal_usbPrinter.getSlotdate();
            DeliveryTime = modal_usbPrinter.getSlottimerange();
            DeliveryType = modal_usbPrinter.getDeliverytype();
            DeliveryAmount = modal_usbPrinter.getDeliveryamount();
            DistanceFromStore = modal_usbPrinter.getDeliverydistance();
            Address = modal_usbPrinter.getUseraddress();
        }
        catch (Exception e){
            e.printStackTrace();
        }



        String b = itemdesp.toString();
        modal_usbPrinter.setItemDesp_String(b);
        String itemDesp = "";




        String text_to_Print = "";


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
        text_to_Print = text_to_Print + "[c]    <font size='normal'>" + StoreAddressLine1 ;
        text_to_Print = text_to_Print + "<font size='normal'>" + StoreAddressLine2 + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine3 + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Contact No :" + StoreLanLine + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + GSTIN + " \n"+ " \n";
        text_to_Print = text_to_Print+"[L]  <font size='normal'>OrderId : "+Orderid +" \n";
        text_to_Print = text_to_Print+"[L]  <font size='normal'>Order Placed Time : "+OrderPlacedtime +" \n";
        text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";
        text_to_Print = text_to_Print+"[L]  ITEMNAME * QTY " +" \n";

        //text_to_Print = text_to_Print+"[L] RATE                                  SUBTOTAL" +" \n";
        text_to_Print = text_to_Print+"[L]  RATE"+"[R]      "+"  SUBTOTAL" +" \n";

        text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";


        try {
            String itemdespStrig = itemdesp.toString();
            modal_usbPrinter.setItemDesp_String(itemdespStrig);
            String itemDesp_string = "";

            for (int i = 0; i < itemdesp.length(); i++) {
                JSONObject json = itemdesp.getJSONObject(i);


                String itemDespName_Weight_quantity = "", itemwise_price = "", itemwise_Subtotal = "";


                String fullitemName = String.valueOf(json.getString("itemname"));
                String itemName = "";
                String itemNameAfterBraces = "";

                String tmcSubCtgyKey = String.valueOf(json.getString("tmcsubctgykey"));
                try {
                    if (tmcSubCtgyKey.equals("tmcsubctgy_6") || tmcSubCtgyKey.equals("tmcsubctgy_3")) {
                        int indexofbraces = fullitemName.indexOf("(");
                        int lastindexofbraces = fullitemName.indexOf(")");
                        int lengthofItemname = fullitemName.length();
                        lastindexofbraces = lastindexofbraces + 1;

                        if ((indexofbraces >= 0) && (lastindexofbraces >= 0) && (lastindexofbraces > indexofbraces)) {
                            itemNameAfterBraces = fullitemName.substring(lastindexofbraces, lengthofItemname);

                            itemName = fullitemName.substring(0, indexofbraces);
                            itemName = itemName + itemNameAfterBraces;
                            fullitemName = fullitemName.substring(0, indexofbraces);
                            fullitemName = fullitemName + itemNameAfterBraces;


                        }

                        if ((indexofbraces >= 0) && (lastindexofbraces >= 0) && (lastindexofbraces == indexofbraces)) {
                            // itemNameAfterBraces = fullitemName.substring(lastindexofbraces,lengthofItemname);

                            itemName = fullitemName.substring(0, indexofbraces);

                            fullitemName = fullitemName.substring(0, indexofbraces);
                            fullitemName = fullitemName;


                        }

                        if (fullitemName.length() > 21) {
                            itemName = fullitemName.substring(0, 21);
                            itemName = itemName + "...";

                            fullitemName = fullitemName.substring(0, 21);
                            fullitemName = fullitemName + "...";
                        }
                        if (fullitemName.length() < 21) {
                            itemName = fullitemName;

                            fullitemName = fullitemName;

                        }
                    } else {
                        int indexofbraces = fullitemName.indexOf("(");
                        if (indexofbraces >= 0) {
                            itemName = fullitemName.substring(0, indexofbraces);

                        }
                        if (fullitemName.length() > 21) {
                            itemName = fullitemName.substring(0, 21);
                            itemName = itemName + "...";
                        }
                        if (fullitemName.length() < 21) {
                            itemName = fullitemName;

                        }
                    }
                } catch (Exception e) {
                    itemName = fullitemName;

                    e.printStackTrace();
                }



                String finalCutName="", finalitemNetweight = "", finalgrossweight = "",finalQuantity ="";


                try {
                    finalQuantity = json.getString("quantity");


                    if ((finalQuantity.equals(""))||(finalQuantity.equals(null))||(finalQuantity.equals(" - "))) {
                        try {
                            finalQuantity = json.getString("quantity");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        //Log.i("tag","grossweight Log   2 "+                grossweight);




                    }

                }
                catch (Exception e){
                    try {
                        if (finalQuantity.equals("")) {
                            finalQuantity = json.getString("quantity");
                            //Log.i("tag","grossweight Log   3 "+                grossweight);


                        }
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }




                try {
                    finalitemNetweight = json.getString("netweight");


                    if ((finalitemNetweight.equals(""))||(finalitemNetweight.equals(null))||(finalitemNetweight.equals(" - "))) {
                        try {
                            finalitemNetweight = json.getString("netweight");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        //Log.i("tag","grossweight Log   2 "+                grossweight);




                    }

                }
                catch (Exception e){
                    try {
                        if (finalitemNetweight.equals("")) {
                            finalitemNetweight = json.getString("netweight");
                            //Log.i("tag","grossweight Log   3 "+                grossweight);


                        }
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }

                try {

                    if(json.has("cutname")){
                        finalCutName = json.getString("cutname");

                    }
                    else{
                        finalCutName ="";
                    }


                }
                catch (Exception e){

                    finalCutName ="";
                    e.printStackTrace();
                }



                try {
                    finalitemNetweight = json.getString("netweight");


                    if ((finalitemNetweight.equals(""))||(finalitemNetweight.equals(null))||(finalitemNetweight.equals(" - "))) {
                        try {
                            finalitemNetweight = json.getString("netweight");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        //Log.i("tag","grossweight Log   2 "+                grossweight);




                    }

                }
                catch (Exception e){
                    try {
                        if (finalitemNetweight.equals("")) {
                            finalitemNetweight = json.getString("netweight");
                            //Log.i("tag","grossweight Log   3 "+                grossweight);


                        }
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
                try {
                    finalgrossweight = json.getString("grossweight");


                    if ((finalgrossweight.equals(""))||(finalgrossweight.equals(null))||(finalgrossweight.equals(" - "))) {
                        try {
                            finalgrossweight = json.getString("grossweightingrams");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        //Log.i("tag","grossweight Log   2 "+                grossweight);




                    }

                }
                catch (Exception e){
                    try {
                        if (finalgrossweight.equals("")) {
                            finalgrossweight = json.getString("grossweightingrams");
                            //Log.i("tag","grossweight Log   3 "+                grossweight);


                        }
                    }
                    catch (Exception e1){
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }

                if(tmcSubCtgyKey.equals("tmcsubctgy_16")) {

                    text_to_Print = text_to_Print+"[L]  <b><font size='normal'>Grill House  "+fullitemName +" </b>\n";


                    if((finalCutName.length()>0) && (!finalCutName.equals("null")) && (!finalCutName.equals(null))) {

                        text_to_Print = text_to_Print+"[L]  <font size='normal'>Cut Name : "+finalCutName.toUpperCase() +" \n";

                    }
                    if(!finalgrossweight.equals("")) {
                        text_to_Print = text_to_Print+"[L]  <font size='normal'>Grossweight : "+finalgrossweight +" \n";

                    }
                    text_to_Print = text_to_Print+"[L]  <font size='normal'>"+ String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")") +" \n";

                }
                else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                    text_to_Print = text_to_Print+"[L]<b>  <font size='normal'>Ready to Cook  "+fullitemName +" </b>\n";

                    if((finalCutName.length()>0) && (!finalCutName.equals("null")) && (!finalCutName.equals(null))) {
                        text_to_Print = text_to_Print+"[L]  <font size='normal'>Cut Name : "+finalCutName.toUpperCase() +" \n";


                    }
                    if(!finalgrossweight.equals("")) {
                        text_to_Print = text_to_Print+"[L]  <font size='normal'>Grossweight : "+finalgrossweight +" \n";

                    }



                    text_to_Print = text_to_Print+"[L]  <font size='normal'>"+ String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")") +" \n";

                }
                else  {

                    text_to_Print = text_to_Print+"[L]  <b><font size='normal'>"+String.valueOf(fullitemName)  +"</b> \n";



                    if((finalCutName.length()>0) && (!finalCutName.equals("null")) && (!finalCutName.equals(null))) {
                        text_to_Print = text_to_Print+"[L]  <font size='normal'>Cut Name : "+finalCutName.toUpperCase() +" \n";

                    }




                    if(!finalgrossweight.equals("")) {

                        text_to_Print = text_to_Print+"[L]  <font size='normal'>Grossweight : "+finalgrossweight +" \n";

                    }
                    text_to_Print = text_to_Print+"[L]  <font size='normal'>"+ String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")") +" \n";

                }



                itemwise_price = json.getString("tmcprice");

                double itemwise_price_double = 0;
                double quantity_double = 0;
                double itemwise_Subtotal_double = 0;
                try {
                    itemwise_price_double = Double.parseDouble(itemwise_price);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    quantity_double = Double.parseDouble(String.valueOf(json.get("quantity")));
                } catch (Exception e) {
                    e.printStackTrace();
                    itemwise_price_double = 0;
                }


                try {
                    itemwise_Subtotal_double = itemwise_price_double * quantity_double;
                } catch (Exception e) {
                    e.printStackTrace();
                }





                try{
                    totalAmountFromAddingSubtotal =  totalAmountFromAddingSubtotal + itemwise_Subtotal_double ;
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                totalAmountFromAddingSubtotal = Math.round(totalAmountFromAddingSubtotal);
                itemwise_Subtotal = String.valueOf(Math.round(itemwise_Subtotal_double));
                itemwise_price = String.valueOf(Math.round(itemwise_Subtotal_double));

                itemwise_price = "Rs. " + itemwise_price;
                itemwise_Subtotal = "Rs." + itemwise_Subtotal;

/*
                if (itemwise_price.length() == 4) {
                    //21spaces
                    itemwise_price = itemwise_price + "                            ";
                }
                if (itemwise_price.length() == 5) {
                    //20spaces
                    itemwise_price = itemwise_price + "                           ";
                }
                if (itemwise_price.length() == 6) {
                    //19spaces
                    itemwise_price = itemwise_price + "                         ";
                }
                if (itemwise_price.length() == 7) {
                    //18spaces
                    itemwise_price = itemwise_price + "                        ";
                }
                if (itemwise_price.length() == 8) {
                    //17spaces
                    itemwise_price = itemwise_price + "                      ";
                }
                if (itemwise_price.length() == 9) {
                    //16spaces
                    itemwise_price = itemwise_price + "                     ";
                }
                if (itemwise_price.length() == 10) {
                    //15spaces
                    itemwise_price = itemwise_price + "                     ";
                }
                if (itemwise_price.length() == 11) {
                    //14spaces
                    itemwise_price = itemwise_price + "                    ";
                }
                if (itemwise_price.length() == 12) {
                    //13spaces
                    itemwise_price = itemwise_price + "                   ";
                }
                if (itemwise_price.length() == 13) {
                    //12space
                    itemwise_price = itemwise_price + "                  ";
                }
                if (itemwise_price.length() == 14) {
                    //11spaces
                    itemwise_price = itemwise_price + "                 ";
                }
                if (itemwise_price.length() == 15) {
                    //10spaces
                    itemwise_price = itemwise_price + "                ";
                }
                if (itemwise_price.length() == 16) {
                    //9spaces
                    itemwise_price = itemwise_price + "               ";
                }
                if (itemwise_price.length() == 17) {
                    //8space
                    itemwise_price = itemwise_price + "              ";
                }
                if (itemwise_price.length() == 18) {
                    //7spaces
                    itemwise_price = itemwise_price + "             ";
                }




                if (itemwise_Subtotal.length() == 4) {
                    //6spaces
                    itemwise_Subtotal = "      " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 5) {
                    //7spaces
                    itemwise_Subtotal = "       " + itemwise_Subtotal;
                }

                if (itemwise_Subtotal.length() == 6) {
                    //8spaces
                    itemwise_Subtotal = "        " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 7) {
                    //7spaces
                    itemwise_Subtotal = "       " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 8) {
                    //6spaces
                    itemwise_Subtotal = "      " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 9) {
                    //5spaces
                    itemwise_Subtotal = "     " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 10) {
                    //4spaces
                    itemwise_Subtotal = "    " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 11) {
                    //3spaces
                    itemwise_Subtotal = "   " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 12) {
                    //2spaces
                    itemwise_Subtotal = "  " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 13) {
                    //1spaces
                    itemwise_Subtotal = " " + itemwise_Subtotal;
                }
                if (itemwise_Subtotal.length() == 14) {
                    //no space
                    itemwise_Subtotal = "" + itemwise_Subtotal;
                }



 */


              //  text_to_Print = text_to_Print+"[L]<font size='normal'>"+itemwise_price + itemwise_Subtotal +" \n";

                text_to_Print = text_to_Print+"[L]  <font size='normal'>"+itemwise_price + "[R] "+itemwise_Subtotal +" \n";

                text_to_Print = text_to_Print+"[L]  <font size='normal'>                                                "+" \n";



            }
            text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";

        } catch (JSONException e) {
            e.printStackTrace();
        }



        PayableAmount = "Rs." + String.valueOf(totalAmountFromAddingSubtotal);
      /*  if (PayableAmount.length() == 4) {
            //21spaces
            PayableAmount = PayableAmount + "                                   " + PayableAmount;
        }
        if (PayableAmount.length() == 5) {
            //20spaces
            PayableAmount = PayableAmount + "                                " + PayableAmount;
        }
        if (PayableAmount.length() == 6) {
            //19spaces
            PayableAmount = PayableAmount + "                               " + PayableAmount;
        }
        if (PayableAmount.length() == 7) {
            //18spaces
            PayableAmount = PayableAmount + "                              " + PayableAmount;
        }
        if (PayableAmount.length() == 8) {
            //17spaces
            PayableAmount = PayableAmount + "                             " + PayableAmount;
        }
        if (PayableAmount.length() == 9) {
            //16spaces
            PayableAmount = PayableAmount + "                            " + PayableAmount;
        }
        if (PayableAmount.length() == 10) {
            //15spaces
            PayableAmount = PayableAmount + "                           " + PayableAmount;
        }
        if (PayableAmount.length() == 11) {
            //14spaces
            PayableAmount = PayableAmount + "                          " + PayableAmount;
        }
        if (PayableAmount.length() == 12) {
            //13spaces
            PayableAmount = PayableAmount + "                         " + PayableAmount;
        }
        if (PayableAmount.length() == 13) {
            //12spaces
            PayableAmount = PayableAmount + "                        " + PayableAmount;
        }
        if (PayableAmount.length() == 14) {
            //11spaces
            PayableAmount = PayableAmount + "                       " + PayableAmount;
        }
        if (PayableAmount.length() == 15) {
            //10spaces
            PayableAmount = PayableAmount + "                      " + PayableAmount;
        }
        if (PayableAmount.length() == 16) {
            //9spaces
            PayableAmount = PayableAmount + "                     " + PayableAmount;
        }
        if (PayableAmount.length() == 17) {
            //8spaces
            PayableAmount = PayableAmount + "                     " + PayableAmount;
        }
        if (PayableAmount.length() == 18) {
            //7spaces
            PayableAmount = PayableAmount + "                   " + PayableAmount;
        }


       */


      //  text_to_Print = text_to_Print+"[L]" +PayableAmount+" \n";


        text_to_Print = text_to_Print+"[L]  " +PayableAmount+" [R] "+PayableAmount+" \n";

        text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";

        if ((!CouponDiscount.equals("0.0")) && (!CouponDiscount.equals("0")) && (!CouponDiscount.equals("0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals(""))) {
            couponDiscount_double = Double.parseDouble (CouponDiscount);
            if (OrderType.equals(Constants.APPORDER)) {
           /*     if (CouponDiscount.length() == 4) {
                    //20spaces
                    //NEW TOTAL =4
                    CouponDiscount = "Coupon Discount                         " + CouponDiscount;
                }
                if (CouponDiscount.length() == 5) {
                    //21spaces
                    //NEW TOTAL =5
                    CouponDiscount = "Coupon Discount                      " + CouponDiscount;
                }
                if (CouponDiscount.length() == 6) {
                    //20spaces
                    //NEW TOTAL =6
                    CouponDiscount = "Coupon Discount                      " + CouponDiscount;
                }

                if (CouponDiscount.length() == 7) {
                    //19spaces
                    //NEW TOTAL =7
                    CouponDiscount = "Coupon Discount                     " + CouponDiscount;
                }
                if (CouponDiscount.length() == 8) {
                    //18spaces
                    //NEW TOTAL =8
                    CouponDiscount = "Coupon Discount                    " + CouponDiscount;
                }
                if (CouponDiscount.length() == 9) {
                    //17spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Coupon Discount                   " + CouponDiscount;
                }
                if (CouponDiscount.length() == 10) {
                    //16spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Coupon Discount                  " + CouponDiscount;
                }
                if (CouponDiscount.length() == 11) {
                    //15spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Coupon Discount                   " + CouponDiscount;
                }
                if (CouponDiscount.length() == 12) {
                    //14spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Coupon Discount                " + CouponDiscount;
                }

                if (CouponDiscount.length() == 13) {
                    //13spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Coupon Discount               " + CouponDiscount;
                }

            */
                text_to_Print = text_to_Print+"[L]  Coupon Discount "+"[R]      " +CouponDiscount+" \n";

            }

            if (OrderType.equals(Constants.POSORDER)) {
                couponDiscount_double = Double.parseDouble (CouponDiscount);
/*
                if (CouponDiscount.length() == 4) {
                    //20spaces
                    //NEW TOTAL =4
                    CouponDiscount = "Discount Amount                        " + CouponDiscount;
                }
                if (CouponDiscount.length() == 5) {
                    //21spaces
                    //NEW TOTAL =5
                    CouponDiscount = "Discount Amount                      " + CouponDiscount;
                }
                if (CouponDiscount.length() == 6) {
                    //20spaces
                    //NEW TOTAL =6
                    CouponDiscount = "Discount Amount                     " + CouponDiscount;
                }

                if (CouponDiscount.length() == 7) {
                    //19spaces
                    //NEW TOTAL =7
                    CouponDiscount = "Discount Amount                    " + CouponDiscount;
                }
                if (CouponDiscount.length() == 8) {
                    //18spaces
                    //NEW TOTAL =8
                    CouponDiscount = " Discount Amount                   " + CouponDiscount;
                }
                if (CouponDiscount.length() == 9) {
                    //17spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Discount Amount                  " + CouponDiscount;
                }
                if (CouponDiscount.length() == 10) {
                    //16spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Discount Amount                 " + CouponDiscount;
                }
                if (CouponDiscount.length() == 11) {
                    //15spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Discount Amount                " + CouponDiscount;
                }
                if (CouponDiscount.length() == 12) {
                    //14spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Discount Amount               " + CouponDiscount;
                }

                if (CouponDiscount.length() == 13) {
                    //13spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Discount Amount              " + CouponDiscount;
                }

 */
                text_to_Print = text_to_Print+"[L]  Discount Amount "+"[R]      " +CouponDiscount+" \n";

            }


          //  text_to_Print = text_to_Print+"[L]" +CouponDiscount+" \n";

            text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";





        }

        try{
            totalAmountFromAddingSubtotalWithDiscount =  totalAmountFromAddingSubtotal - couponDiscount_double ;

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt = totalAmountFromAddingSubtotalWithDiscount+deliveryAmount_double;

        }
        catch(Exception e){
            e.printStackTrace();
        }

        if( deliveryAmount_double>0) {
        /*    if (DeliveryAmount.length() == 4) {
                //25spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                       " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 5) {
                //24spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                      " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 6) {
                //23spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                     " + DeliveryAmount;
            }

            if (DeliveryAmount.length() == 7) {
                //22spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                    " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 8) {
                //21spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                   " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 9) {
                //20spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                  " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 10) {
                //19spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                 " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 11) {
                //18spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount                " + DeliveryAmount;
            }
            if (DeliveryAmount.length() == 12) {
                //17spaces
                //DeliveryAmount =15
                DeliveryAmount = "Delivery Amount               " + DeliveryAmount;
            }


         */
            text_to_Print = text_to_Print+"[L]  Delivery Amount "+"[R]      " +DeliveryAmount+" \n";

            //    text_to_Print = text_to_Print+"[L]" +DeliveryAmount+" \n";

            text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";


        }

        String NetTotal = "Rs." + String.valueOf(Math.round(totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt));
        /*if (NetTotal.length() == 4) {
            //27spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                             " + NetTotal;
        }
        if (NetTotal.length() == 5) {
            //26spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                             " + NetTotal;
        }
        if (NetTotal.length() == 6) {
            //25spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                            " + NetTotal;
        }

        if (NetTotal.length() == 7) {
            //24spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                            " + NetTotal;
        }
        if (NetTotal.length() == 8) {
            //23spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                          " + NetTotal;
        }
        if (NetTotal.length() == 9) {
            //22spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                         " + NetTotal;
        }
        if (NetTotal.length() == 10) {
            //21spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                        " + NetTotal;
        }
        if (NetTotal.length() == 11) {
            //20spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                       " + NetTotal;
        }
        if (NetTotal.length() == 12) {
            //19spaces+4spaces
            //NEW TOTAL =9
            NetTotal = "NET TOTAL                     " + NetTotal;
        }

         */


        text_to_Print = text_to_Print+"[L]  NET TOTAL  "+"[R]      " +NetTotal+" \n";

     //   text_to_Print = text_to_Print+"[L]" +NetTotal+" \n";

        text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";



        text_to_Print = text_to_Print+"[L]  <b>Payment Mode : " +PaymentMode+" </b>\n";

        text_to_Print = text_to_Print+"[L]  Mobile No : " +MobileNumber+" \n"+" \n";
/*
        text_to_Print = text_to_Print+"[c]<font size='big'> TOKENNO: " +TokenNo+" \n";



        text_to_Print = text_to_Print+"[L]<font size='normal'>Slot Name : " +Slotname+" \n";


        text_to_Print = text_to_Print+"[L]Slot Date : " +SlotDate+" \n";



        if(Slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME)){
            text_to_Print = text_to_Print+"[L]Order Placed time : " +OrderPlacedtime+" \n";


        }
        text_to_Print = text_to_Print+"[L]Delivery time : " +DeliveryTime+" \n";

        text_to_Print = text_to_Print+"[L]Delivery type : " +DeliveryType+" \n";
        text_to_Print = text_to_Print+"[L]Distance from Store  : " +DistanceFromStore+" Kms"+" \n";

        text_to_Print = text_to_Print+"[L]Address : " +" \n";
        text_to_Print = text_to_Print+"[L] "+ Address +" \n";



        if(!Notes.equals("")) {
            text_to_Print = text_to_Print+"[c]<b><font size='big'>Notes : " +Notes+" </b>\n\n";

        }
        else{
            text_to_Print = text_to_Print+"[L] " +" \n";


        }

 */
        text_to_Print = text_to_Print+"[L] " +" \n";

        text_to_Print = text_to_Print+"[c] <b>   Thank You For Choosing Us !!!! " +"</b> \n";

        text_to_Print = text_to_Print+"[L] " +" \n";
        text_to_Print = text_to_Print+"[L] " +" \n";


       // AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);
        return printer.addTextToPrint(text_to_Print);






    }






    private String getTomorrowsDate() {

        Date todaysDate = Calendar.getInstance().getTime();
        System.out.println("nextDate " + todaysDate);

        String next_day = "";
        //calander_view.setCurrentDayBackgroundColor(context.getResources().getColor(R.color.gray_color));
        SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        String date_format = dateFormatForDisplaying.format(todaysDate);
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        final Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormatForDisplaying.parse(date_format);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            String nex = dateFormatForDisplaying.format(calendar.getTime());
            Date d1 = dateFormatForDisplaying.parse(nex);
            String day_1 = simpleDateformat.format(d1);
            next_day = day_1+", "+nex;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return next_day;


    }




    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();
        if(orderdetailsnewschema) {


            calendar.add(Calendar.DATE, -1);


            Date c1 = calendar.getTime();
            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");




            String PreviousdayDate = day.format(c1);


            return PreviousdayDate;

        }
        else {


            calendar.add(Calendar.DATE, -1);


            Date c1 = calendar.getTime();

            SimpleDateFormat previousday = new SimpleDateFormat("EEE");
            String PreviousdayDay = previousday.format(c1);


            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
            String PreviousdayDate = df1.format(c1);
            PreviousdayDate = PreviousdayDay + ", " + PreviousdayDate;
            //System.out.println("todays Date  " + CurrentDate);
            // System.out.println("PreviousdayDate Date  " + PreviousdayDate);


            return PreviousdayDate;
        }
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



    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        if(orderdetailsnewschema) {

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = day.format(c);

            return CurrentDate;

        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE");
            CurrentDay = day.format(c);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;

            //CurrentDate = CurrentDay+", "+CurrentDate;
            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;
        }
    }


    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        if(orderdetailsnewschema) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = df.format(c);


            return CurrentDate;
        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE");
            CurrentDay = day.format(c);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;


            //CurrentDate = CurrentDay+", "+CurrentDate;
            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;
        }
    }





    void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

            manageOrders_ListView.setVisibility(View.GONE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);

        }

    }


    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            manageOrders_ListView.setVisibility(View.GONE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);
        }

    }



    void showOrderInstructionText(boolean show) {
        if(show){


            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.GONE);
            mobile_orderinstruction.setVisibility(View.VISIBLE);



        }
        else{
            mobile_orderinstruction.setVisibility(View.GONE);

        }
    }





    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.VISIBLE);
        mobile_search_button.setVisibility(View.VISIBLE);
        mobile_search_close_btn.setVisibility(View.GONE);
        mobile_search_barEditText.setVisibility(View.GONE);
        newOrdersSync_Layout.setVisibility(View.VISIBLE);

    }

    private void showSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.GONE);
        mobile_search_button.setVisibility(View.GONE);
        mobile_search_close_btn.setVisibility(View.VISIBLE);
        mobile_search_barEditText.setVisibility(View.VISIBLE);
        newOrdersSync_Layout.setVisibility(View.GONE);
    }
    private void showKeyboard(final EditText editText) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
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
    private String getDatewithNameofthePreviousDay2(String sDate) {



        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e2) {
            e2.printStackTrace();
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

    private String convertOldFormatDateintoNewFormat(String todaysdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }


    private String convertnewFormatDateintoOldFormat(String todaysdate) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("EEE");
            CurrentDay = day.format(date);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(date);

            CurrentDate = CurrentDay + ", " + CurrentDate;



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }

    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey) {
        Adjusting_Widgets_Visibility(true);
        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();

        mResultCallback = new VendorOrdersTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + orderslist_fromResponse);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content",orderslist_fromResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mobile_jsonString = String.valueOf(jsonObject);
                ordersList = orderslist_fromResponse;
                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

                //convertingJsonStringintoArray(orderStatus,mobile_jsonString);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                Adjusting_Widgets_Visibility(false);
            }
        };
        mContext = Pos_Orders_List.this;
        mVolleyService = new VendorOrdersTableService(mResultCallback,mContext);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_type + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=POSORDER";
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }



}