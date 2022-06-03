package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order.Modal_ReplacementTransactionDetails;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;

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

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class GenerateCustomerMobileNo_BillValueReport extends AppCompatActivity {
    private Spinner daysCountSpinner;
    private   String[] daysCountSpinnerArrayList = new String[] {
            "1", "2", "3", "4", "5", "6", "7","8","9","10"
    };
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchfor_singleDayDump;
    private LinearLayout dateSelectorLayout,daysCountSpinner_layout,generateReport_Layout,fromdateSelectorLayout,todateSelectorLayout;
    private TextView dateSelector_text,generateDataInstruction,fromdateSelector_text,todateSelector_text;
    private DatePickerDialog datepicker;
    private DatePickerDialog fromdatepicker;

    private DatePickerDialog todatepicker;

    List<Modal_ManageOrders_Pojo_Class> ordersList;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    public static List<String> array_of_orderId;
    public static List<Modal_ManageOrders_Pojo_Class> OrderstoGenerateSheet;
    public static List<String> array_of_Dates;
    double screenInches;
    String PreviousDateString,DateString,TodaysDate,vendorKey,vendorname,OrderDetailsResultjsonString,CurrentDate,LastDate,fromdatestring,todatestring,currentdateLong,oldDayLong,slotDate;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;
    TextView appOrdersCount_textwidget,orderinstruction, mobile_nameofFacility_Textview;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;
    ListView manageOrders_ListView;
    boolean isSpinnerTouch =false;
    static boolean isSearchButtonClicked = false;
    static boolean isSwitchisOn = false;
    Workbook wb;
    Sheet sheet = null;
    private static String[] columns = {"S.No ","Customer Mobile No","Value of Bill","Ordertype"};
    int spinnerselecteditem=1;
    int spinnerselecteditem_Count =1;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;


    public static List<String> replacementTransactionDateArray = new ArrayList<>();
    public static HashMap<String, List<Modal_ReplacementTransactionDetails>> replacementTransactionDateHashmap = new HashMap();


    boolean isgetReplacementOrderForSelectedDateCalled = false;

    boolean isSheetForLastDateGenerate = false;

    boolean isReplacementTransacDetailsResponseReceivedForSelectedDate = false;
    String replacementOrderDetailsString="", startDateString_AsNewFormat = "",
            endDateString_AsNewFormat = "";

    String selectedStartDate = "";
    String selectedEndDate = "";



    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_customer_mobile_no__bill_value_report);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        switchfor_singleDayDump = findViewById(R.id.switchfor_singleDayDump);
        daysCountSpinner = (Spinner) findViewById(R.id.daysCountSpinner);
        daysCountSpinner_layout = findViewById(R.id.daysCountSpinner_layout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        generateDataInstruction = findViewById(R.id.generateDataInstruction);
        manageOrders_ListView = findViewById(R.id.manageOrders_ListView);
        mobile_nameofFacility_Textview = findViewById(R.id.nameofFacility_Textview);
        mobile_search_button = findViewById(R.id.search_button);
        mobile_search_barEditText = findViewById(R.id.search_barEdit);
        mobile_search_close_btn = findViewById(R.id.search_close_btn);
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);
        generateReport_Layout = findViewById(R.id.generateReport_Layout);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        appOrdersCount_textwidget =  findViewById(R.id.appOrdersCount_textwidget);
        fromdateSelectorLayout =  findViewById(R.id.fromdateSelectorLayout);
        todateSelectorLayout =  findViewById(R.id.todateSelectorLayout);
        todateSelector_text = findViewById(R.id.todateSelector_text);
        fromdateSelector_text = findViewById(R.id.fromdateSelector_text);
        orderinstruction = findViewById(R.id.orderinstruction);
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_orderId = new ArrayList<>();
        OrderstoGenerateSheet = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_Dates = new ArrayList<>();


        orderinstruction.setVisibility(View.VISIBLE);
        orderinstruction.setText("Select Date to get Data");
        mobile_nameofFacility_Textview.setText(vendorname);
        daysCountSpinner_layout.setVisibility(View.GONE);





        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, daysCountSpinnerArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            daysCountSpinner.setAdapter(adapter);
            switchfor_singleDayDump.setChecked(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", "vendor_1"));
            vendorname = (shared.getString("VendorName", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
           // orderdetailsnewschema = false;

            try {
                ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
                screenInches = screenSizeOfTheDevice.getDisplaySize(GenerateCustomerMobileNo_BillValueReport .this);
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
                 //   Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

                }
                catch (Exception e1){
                    e1.printStackTrace();
                }


            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            try{
                TodaysDate = getDatewithNameoftheDay();
                PreviousDateString = getDatewithNameofthePreviousDay();
                //Now we are creating sheet
                String date = getDate();
                String sevendaysbackdate = getOldDatewithNameusingCurrentDate(date,"-7");
                currentdateLong = getLongValuefortheDate2(date);
                oldDayLong = getLongValuefortheDate2(sevendaysbackdate);

                // isSearchButtonClicked = false;

                ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();
                array_of_Dates.clear();

                if(orderdetailsnewschema){
                    String oldformat = convertnewFormatDateintoOldFormat(TodaysDate);
                    dateSelector_text.setText(oldformat);
                    fromdateSelector_text.setText(oldformat);
                    todateSelector_text.setText(oldformat);
                 callVendorOrderDetailsSeviceAndInitCallBack(TodaysDate,TodaysDate,vendorKey);

                }
                else{

                    dateSelector_text.setText(TodaysDate);
                    fromdateSelector_text.setText(TodaysDate);
                    todateSelector_text.setText(TodaysDate);
                    //  Adjusting_Widgets_Visibility(true);
                    todatestring  =TodaysDate;
                    getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(TodaysDate,TodaysDate, vendorKey);

                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e ){

        }

        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ordersList.size()>0){
                    try {
                        wb = new HSSFWorkbook();
                        //Now we are creating sheet

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    /*
                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GenerateCustomerMobileNo_BillValueReport.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    // If do not grant write external storage permission.
                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        // Request user to grant write external storage permission.
                        ActivityCompat.requestPermissions(GenerateCustomerMobileNo_BillValueReport.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, "Click Allow and then Generate Again", Toast.LENGTH_SHORT).show();

                    } else {
                        showProgressBar(true);

                        try {
                            AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                     */



                    if (SDK_INT >= Build.VERSION_CODES.R) {

                        if(Environment.isExternalStorageManager()){
                            if(orderdetailsnewschema){
                                String FromDateoldformat = convertnewFormatDateintoOldFormat(startDateString_AsNewFormat);
                                String Todateoldformat = convertnewFormatDateintoOldFormat(endDateString_AsNewFormat);
                                addDateinDatesArray(FromDateoldformat,Todateoldformat);

                            }
                            else {


                                try {
                                    showProgressBar(true);

                                    AddDatatoExcelSheet(ordersList, "orderDetailsfrom" + fromdatestring);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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


                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GenerateCustomerMobileNo_BillValueReport.this, WRITE_EXTERNAL_STORAGE);
                        //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(GenerateCustomerMobileNo_BillValueReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {

                            if(orderdetailsnewschema){
                                String FromDateoldformat = convertnewFormatDateintoOldFormat(startDateString_AsNewFormat);
                                String Todateoldformat = convertnewFormatDateintoOldFormat(endDateString_AsNewFormat);

                                addDateinDatesArray(FromDateoldformat,Todateoldformat);
                            }
                            else{


                            showProgressBar(true);
                            try {
                                AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);

                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
                            }
                            }
                        }
                    }


                }
            }
        });

        switchfor_singleDayDump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    isSwitchisOn =false;
                    generateDataInstruction.setText("#Note : Select From Date and To Date . Can't Generate More than 7 Days ");
                    //      daysCountSpinner_layout.setVisibility(View.VISIBLE);
                    fromdateSelectorLayout.setVisibility(View.VISIBLE);
                    todateSelectorLayout.setVisibility(View.VISIBLE);
                    dateSelectorLayout.setVisibility(View.GONE);
                    newOrdersSync_Layout.setVisibility(View.INVISIBLE);
                    isSheetForLastDateGenerate = false;
                }
                else{
                    isSwitchisOn =true;
                    generateDataInstruction.setText("#Note : To Generate Data for Multiple Days Turn oFF the Above Switch");
                    newOrdersSync_Layout.setVisibility(View.VISIBLE);
                    fromdateSelectorLayout.setVisibility(View.GONE);
                    todateSelectorLayout.setVisibility(View.GONE);
                    //  daysCountSpinner_layout.setVisibility(View.GONE);
                    dateSelectorLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        daysCountSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) { isSpinnerTouch=true; return false; }});


   /*     daysCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (isSpinnerTouch) {
                    spinnerselecteditem =1;
                    spinnerselecteditem_Count=1;
                    ordersList.clear();
                    sorted_OrdersList.clear();
                    array_of_orderId.clear();
                    CurrentDate = getDate();
                    String Todaysdate = getDatewithNameoftheDay();
                    PreviousDateString = getDatewithNameofthePreviousDay();



                    getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString,Todaysdate, vendorKey);

                    spinnerselecteditem = Integer.parseInt(daysCountSpinner.getSelectedItem().toString());
                    spinnerselecteditem_Count += Integer.parseInt(daysCountSpinner.getSelectedItem().toString());
                    while (spinnerselecteditem>=1) {
                        String datebasedOn_SpinnerCount = getOldDatewithNameusingCurrentDate(CurrentDate, "-" + String.valueOf(spinnerselecteditem));
                        // Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, String.valueOf(datebasedOn_SpinnerCount), Toast.LENGTH_LONG).show();
                        String previousDayDateForSpinnerCountDate = getDatewithNameofthePreviousDayfromSelectedDay2(datebasedOn_SpinnerCount);
                        // Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, String.valueOf(previousDayDateForSpinnerCountDate), Toast.LENGTH_LONG).show();
                        if(spinnerselecteditem==spinnerselecteditem_Count) {
                            LastDate = datebasedOn_SpinnerCount;
                        }
                        getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(previousDayDateForSpinnerCountDate, datebasedOn_SpinnerCount, vendorKey);
                        spinnerselecteditem--;


                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    */




        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersList.clear();
                sorted_OrdersList.clear();

                replacementTransactionDateHashmap.clear();
                replacementTransactionDateArray.clear();

                array_of_orderId.clear();
                spinnerselecteditem = 1 ;
                spinnerselecteditem_Count=1;
                showProgressBar(true);
                todatestring=dateSelector_text.getText().toString();

                PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);

                startDateString_AsNewFormat = convertOldFormatDateintoNewFormat(PreviousDateString);
                endDateString_AsNewFormat = convertOldFormatDateintoNewFormat(TodaysDate);

                isSearchButtonClicked = false;

                if(orderdetailsnewschema){
                    String FromdateAsNewFormat =convertOldFormatDateintoNewFormat(PreviousDateString);
                    String TodateAsNewFormat =convertOldFormatDateintoNewFormat(TodaysDate);

                    callVendorOrderDetailsSeviceAndInitCallBack(FromdateAsNewFormat, TodateAsNewFormat,vendorKey);


                }
                else{


                    //  Adjusting_Widgets_Visibility(true);

                    getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString,todatestring, vendorKey);

                }


                getdataFromReplacementTransaction(startDateString_AsNewFormat, endDateString_AsNewFormat, vendorKey);


            }
        });
        mobile_search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked = false;

                DisplayOrderListDatainListView( ordersList);
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ordersList.size()>0) {
                    int textlength = mobile_search_barEditText.getText().toString().length();
                    isSearchButtonClicked = true;

                    showKeyboard(mobile_search_barEditText);
                    showSearchBarEditText();
                }
                else{
                    Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, "Please Select Date First", Toast.LENGTH_SHORT).show();

                }
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
                Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });


        fromdateSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openFromDatePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        todateSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openToDatePicker();
                } catch (Exception e) {
                    e.printStackTrace();
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
                                modal_manageOrders_forOrderDetailList1.payableamount = modal_manageOrders_forOrderDetailList.getPayableamount();

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

                        DisplayOrderListDatainListView(sorted_OrdersList);

                    } catch (Exception E) {
                        E.printStackTrace();
                    }


                }
                else{
                    manageOrders_ListView.setVisibility(View.GONE);
                    orderinstruction.setVisibility(View.VISIBLE);
                    orderinstruction.setText("No orders found for this Mobile number");

                }

            }
        });










    }

    private void addDateinDatesArray(String fromDateoldformat, String todateoldformat) {

        if(!todateoldformat.equals(fromDateoldformat)){
            if(!array_of_Dates.contains(fromDateoldformat)){
                array_of_Dates.add(fromDateoldformat);
            }
            String nextday = getTomorrowsDate(fromDateoldformat);
            addDateinDatesArray(nextday,todateoldformat);
        }
        else{

            if(!array_of_Dates.contains(fromDateoldformat)){
                array_of_Dates.add(fromDateoldformat);
            }


            showProgressBar(true);
            try {
                AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);

            } catch (Exception e) {
                e.printStackTrace();
                ;
            }
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
                    try {
                        GenerateExcelSheet();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }

            else {

            if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
                int grantResultsLength = grantResults.length;
                if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
                    // exportInvoice();
                    try {
                        GenerateExcelSheet();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }




    private void openFromDatePicker() {

        spinnerselecteditem = 1 ;
        spinnerselecteditem_Count=1;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        fromdatepicker = new DatePickerDialog(GenerateCustomerMobileNo_BillValueReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            replacementTransactionDateHashmap.clear();
                            replacementTransactionDateArray.clear();

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();
                            array_of_Dates.clear();
                            String month_in_String = getMonthString(monthOfYear);
                            String monthstring = String.valueOf(monthOfYear+1);
                            String datestring =  String.valueOf(dayOfMonth);

                            if(datestring.length()==1){
                                datestring="0"+datestring;
                            }
                            if(monthstring.length()==1){
                                monthstring="0"+monthstring;
                            }

                            isSheetForLastDateGenerate = false;


                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay =   getDayString(dayOfWeek);
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            //   PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            fromdateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            fromdatestring = fromdateSelector_text.getText().toString();
                            selectedStartDate = fromdatestring;
                            selectedEndDate = getDatewithNameoftheseventhDayFromSelectedStartDate(DateString);
                            startDateString_AsNewFormat = convertNormalDateintoReplacementTransactionDetailsDate(CurrentDateString, "STARTTIME");


                            //      showProgressBar(true);

//                            getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);



        Calendar c = Calendar.getInstance();



        DatePicker datePicker = fromdatepicker.getDatePicker();

        c.add(Calendar.DATE, -60);
        // Toast.makeText(getApplicationContext(), Calendar.DATE, Toast.LENGTH_LONG).show();
        Log.d(Constants.TAG, "Calendar.DATE " + String.valueOf(Calendar.DATE));
        long oneMonthAhead = c.getTimeInMillis();
        datePicker.setMaxDate(System.currentTimeMillis() - 1000);
        datePicker.setMinDate(oneMonthAhead);

        fromdatepicker.show();


    }

    private void openToDatePicker() {


        spinnerselecteditem = 1 ;
        spinnerselecteditem_Count=1;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        todatepicker = new DatePickerDialog(GenerateCustomerMobileNo_BillValueReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();
                            array_of_Dates.clear();
                            replacementTransactionDateHashmap.clear();
                            replacementTransactionDateArray.clear();

                            String month_in_String = getMonthString(monthOfYear);
                            String monthstring = String.valueOf(monthOfYear+1);
                            String datestring =  String.valueOf(dayOfMonth);
                            isSheetForLastDateGenerate = false;

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
                            //   PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            endDateString_AsNewFormat = convertNormalDateintoReplacementTransactionDetailsDate(CurrentDateString, "ENDTIME");

                            todateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            showProgressBar(true);
                            todatestring = todateSelector_text.getText().toString();

                            if(orderdetailsnewschema){
                                  String FromdateAsNewFormat =convertOldFormatDateintoNewFormat(fromdatestring);
                                String TodateAsNewFormat =convertOldFormatDateintoNewFormat(DateString);

                                callVendorOrderDetailsSeviceAndInitCallBack(FromdateAsNewFormat, TodateAsNewFormat,vendorKey);


                            }
                            else{


                                //  Adjusting_Widgets_Visibility(true);

                                calculate_the_dateandgetData(fromdatestring,todatestring);

                            }


                            getdataFromReplacementTransaction(startDateString_AsNewFormat, endDateString_AsNewFormat, vendorKey);

//                            getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);




        Calendar c = Calendar.getInstance();




        boolean isEndDateisAfterCurrentDate = false;
        Date d2=null,d1 = null;
        long MaxDate = getMillisecondsFromDate(selectedEndDate);
        long MinDate = getMillisecondsFromDate(selectedStartDate);

        String todayDate = getDate_and_time();
        SimpleDateFormat sdformat = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            d2 = sdformat.parse(todayDate);

            d1 = sdformat.parse(selectedEndDate);
            if((d1.compareTo(d2) < 0)||(d1.compareTo(d2) == 0)){
                isEndDateisAfterCurrentDate =false;
            }
            else if(d1.compareTo(d2) > 0){
                isEndDateisAfterCurrentDate =true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        DatePicker datePicker = todatepicker.getDatePicker();
        c.add(Calendar.DATE, -60);
        try {
            if (!isEndDateisAfterCurrentDate) {

                MaxDate = getMillisecondsFromDate(selectedEndDate);

            } else {
                MaxDate = getMillisecondsFromDate(todayDate);

            }
            MinDate = getMillisecondsFromDate(selectedStartDate);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        long oneMonthAhead = c.getTimeInMillis();
        datePicker.setMaxDate(MaxDate);
        datePicker.setMinDate(MinDate);


        todatepicker.show();





    }

    private void calculate_the_dateandgetData(String fromdateString, String toDateString) {


        String previousday,nextday;
        previousday =  getDatewithNameofthePreviousDayfromSelectedDay2(fromdateString);
        getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(previousday, fromdateString, vendorKey);

    }


    private void openDatePicker() {
        array_of_Dates.clear();
        spinnerselecteditem = 1 ;
        spinnerselecteditem_Count=1;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(GenerateCustomerMobileNo_BillValueReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();

                            replacementTransactionDateHashmap.clear();
                            replacementTransactionDateArray.clear();


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
                            //getOrderForSelectedDate(DateString, vendorKey);
                            showProgressBar(true);
                            todatestring=DateString;
                            startDateString_AsNewFormat = convertNormalDateintoReplacementTransactionDetailsDate(CurrentDateString, "STARTTIME");
                            endDateString_AsNewFormat = convertNormalDateintoReplacementTransactionDetailsDate(CurrentDateString, "ENDTIME");

                            if(orderdetailsnewschema){
                                  String FromdateAsNewFormat =convertOldFormatDateintoNewFormat(todatestring);
                                String TodateAsNewFormat =convertOldFormatDateintoNewFormat(todatestring);

                                callVendorOrderDetailsSeviceAndInitCallBack(FromdateAsNewFormat, TodateAsNewFormat,vendorKey);


                            }
                            else{


                                //  Adjusting_Widgets_Visibility(true);

                                getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, DateString, vendorKey);

                            }


                            getdataFromReplacementTransaction(startDateString_AsNewFormat, endDateString_AsNewFormat, vendorKey);



                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }
    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey) {
        if(isVendorOrdersTableServiceCalled){
            showProgressBar(false);
            return;
        }
        isVendorOrdersTableServiceCalled = true;
        mResultCallback = new VendorOrdersTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + orderslist_fromResponse);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content",orderslist_fromResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isVendorOrdersTableServiceCalled = false;
                ordersList =  orderslist_fromResponse;

                 orderinstruction.setVisibility(View.GONE);
               // addDateinDatesArray(FromDate,ToDate);
                DisplayOrderListDatainListView(ordersList);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + "That didn't work!");
                showProgressBar(false);
                isVendorOrdersTableServiceCalled = false;

            }
        };
        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();

        showProgressBar(true);
        mVolleyService = new VendorOrdersTableService(mResultCallback,GenerateCustomerMobileNo_BillValueReport.this);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingFromToSlotDate_vendorkey + "?fromslotdate="+FromDate+"&vendorkey="+vendorKey+"&toslotdate="+ToDate;
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingFromToSlotDate_vendorkey + "?fromslotdate="+FromDate+"&vendorkey="+vendorKey+"&toslotdate="+ToDate;

        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }

    private void getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(String previousDateString, String todaysdate, String vendorKey) {
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );
            isSheetForLastDateGenerate = false;
        showProgressBar(true);


        if(!array_of_Dates.contains(todaysdate)){
            array_of_Dates.add(todaysdate);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_AppOrders_and_PosOrders + "?slotdate="+todaysdate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDateString,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            OrderDetailsResultjsonString = response.toString();
                            orderinstruction.setVisibility(View.GONE);

                            convertingJsonStringintoArray( OrderDetailsResultjsonString,todaysdate);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {

                    if(todaysdate.equals(todatestring)) {

                       // isSheetForLastDateGenerate=true;

                        if(array_of_orderId.size()<=0){
                            loadingpanelmask.setVisibility(View.GONE);
                            loadingPanel.setVisibility(View.GONE);
                            manageOrders_ListView.setVisibility(View.GONE);
                            orderinstruction.setText("No Order today");
                            Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, "There is no Order  on " + todaysdate, Toast.LENGTH_LONG).show();

                            orderinstruction.setVisibility(View.VISIBLE);
                            appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));
                        }
                        else {
                            DisplayOrderListDatainListView(ordersList);
                        }
                    }
                    else{
                        String nextday = getTomorrowsDate(todaysdate);
                        calculate_the_dateandgetData(nextday,todatestring);

                    }





//
                    error.printStackTrace();


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
        Volley.newRequestQueue(GenerateCustomerMobileNo_BillValueReport.this).add(jsonObjectRequest);







    }






    private void convertingJsonStringintoArray(String orderDetailsResultjsonString, String todaysdate) {
        try {
            String ordertype="#",orderid="";
            sorted_OrdersList.clear();
            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(orderDetailsResultjsonString);
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
                        ordertype = String.valueOf(json.get("ordertype"));
                    }
                    else{
                        ordertype="#";
                        manageOrdersPojoClass.orderType ="";
                    }
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
                        String orderplacedlong =  getLongValuefortheDate(String.valueOf(json.get("orderplacedtime")));
                        manageOrdersPojoClass.orderplacedtime_in_long=orderplacedlong;
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
                        slotDate  = String.valueOf(json.get("slotdate"));
                    }
                    else{
                        manageOrdersPojoClass.slotdate =todaysdate;
                        slotDate = todaysdate;
                    }
                    if(slotDate.equals("")){
                        slotDate = todaysdate;
                        manageOrdersPojoClass.slotdate =todaysdate;

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
                        if (ordertype.toUpperCase().equals(Constants.APPORDER)) {
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


                    try {
                        if (ordertype.toUpperCase().equals(Constants.APPORDER)) {


                            if (json.has("deliverydistance")) {

                                String deliverydistance = String.valueOf(json.get("deliverydistance"));
                                if (!deliverydistance.equals(null) && (!deliverydistance.equals("null"))) {
                                    manageOrdersPojoClass.deliverydistance = String.valueOf(json.get("deliverydistance"));

                                } else {
                                    manageOrdersPojoClass.deliverydistance = "0";

                                }
                            } else {
                                manageOrdersPojoClass.deliverydistance = "0";
                            }


                        }
                    } catch (Exception E) {
                        manageOrdersPojoClass.deliverydistance = "0";
                        E.printStackTrace();
                    }



                    if(!String.valueOf(json.get("orderStatus")).equals("NEW")){

                        if(json.has("deliveryusername")){
                            manageOrdersPojoClass.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));

                        }
                        if(json.has("deliveryuserkey")){
                            manageOrdersPojoClass.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));;

                        }
                        if(json.has("deliveryusermobileno")){
                            manageOrdersPojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));

                        }


                    }




                    ordersList.add(manageOrdersPojoClass);
                    Log.d(Constants.TAG, "convertingJsonStringintoArray slotdate: " + ordertype);

                    Log.d(Constants.TAG, "convertingJsonStringintoArray slotdate: " + slotDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }


            }
            spinnerselecteditem_Count--;
            if(todaysdate.equals(todatestring)) {
             //   Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, String.valueOf(spinnerselecteditem_Count), Toast.LENGTH_LONG).show();
             //   Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, String.valueOf("spinnerselecteditem  "+spinnerselecteditem), Toast.LENGTH_LONG).show();

                // appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));
                DisplayOrderListDatainListView(ordersList);
            }
            else{
                String nextday = getTomorrowsDate(todaysdate);
                calculate_the_dateandgetData(nextday,todatestring);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            if(todaysdate.equals(todatestring)) {
                showProgressBar(false);

            }

        }
    }

    private void DisplayOrderListDatainListView(List<Modal_ManageOrders_Pojo_Class> ordersList) {
        final String[] orderid_1 = {""} , orderid_2 = {""};

        try {

            if (ordersList.size() > 0) {
                Collections.sort(ordersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                    public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                        String orderplacedtime_1 = object1.getOrderplacedtime_in_long();
                        String orderplacedtime_2 = object2.getOrderplacedtime_in_long();
                        orderid_1[0] = object1.getOrderid().toString();
                        orderid_2[0] = object2.getOrderid().toString();
                        if((orderplacedtime_1.equals(""))||(orderplacedtime_1.equals("null"))||(orderplacedtime_1.equals(null))){
                            orderplacedtime_1=String.valueOf(0);
                        }
                        if((orderplacedtime_2.equals(""))||(orderplacedtime_2.equals("null"))||(orderplacedtime_2.equals(null))){
                            orderplacedtime_2=String.valueOf(0);
                        }

                        Long i2 = Long.valueOf(orderplacedtime_2);
                        Long i1 = Long.valueOf(orderplacedtime_1);

                        return i2.compareTo(i1);
                    }
                });



                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));




                Adapter_Edit_Or_CancelTheOrders adapter_edit_or_cancelTheOrders = new Adapter_Edit_Or_CancelTheOrders(GenerateCustomerMobileNo_BillValueReport.this, ordersList,true);
                manageOrders_ListView.setAdapter(adapter_edit_or_cancelTheOrders);


                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.VISIBLE);
                orderinstruction.setVisibility(View.GONE);


            }
            else{
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                orderinstruction.setText("There is No Order ");
                orderinstruction.setVisibility(View.VISIBLE);
                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));

            }
        }
        catch (Exception e){
            e.printStackTrace();
            if (ordersList.size() > 0) {
                Toast.makeText(this, String.valueOf(orderid_1[0])+"  "+String.valueOf(orderid_2[0]), Toast.LENGTH_SHORT).show();
                Adapter_Edit_Or_CancelTheOrders adapter_edit_or_cancelTheOrders = new Adapter_Edit_Or_CancelTheOrders(GenerateCustomerMobileNo_BillValueReport.this, ordersList,true);
                manageOrders_ListView.setAdapter(adapter_edit_or_cancelTheOrders);

                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));

                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.VISIBLE);
                orderinstruction.setVisibility(View.GONE);


            } else {
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                orderinstruction.setText("There is No Order ");

                orderinstruction.setVisibility(View.VISIBLE);

            }
        }

    }







    private void AddDatatoExcelSheet(List<Modal_ManageOrders_Pojo_Class> OrdersList, String name) {


       // Log.d(Constants.TAG, "prepareDataForExcelSheet type  addData: " + name);
        for(int i =0;array_of_Dates.size()>i;i++) {
            String date = array_of_Dates.get(i);
            List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList = getorderlistdatewise(OrdersList,date );
            double totalRefundAmount = 0;
            double totalReplacementAmount = 0;

            sheet = wb.createSheet(date);
            int rowNum = 1;
            if(array_of_Dates.size()-i==1 ){
                isSheetForLastDateGenerate=true;
            }
            if (sorted_OrdersList.size() > 0) {
                for (int ii = 0; ii < sorted_OrdersList.size(); ii++) {

                    Modal_ManageOrders_Pojo_Class itemRow = sorted_OrdersList.get(ii);

                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  itemRow: " + itemRow.getSlottime_in_long());

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

                    for (int j = 0; j < columns.length; j++) {
                        headercell = headerRow.createCell(j);
                        headercell.setCellValue(columns[j]);
                        headercell.setCellStyle(headerCellStyle);
                    }

                    try {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(rowNum-1);
                        row.createCell(1).setCellValue(itemRow.getUsermobile());
                        row.createCell(2).setCellValue(itemRow.getPayableamount());
                        row.createCell(3).setCellValue(itemRow.getOrderType());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    sheet.setColumnWidth(0, (10 * 200));
                    sheet.setColumnWidth(1, (10 * 600));
                    sheet.setColumnWidth(2, (10 * 600));
                    sheet.setColumnWidth(3, (10 * 600));
                    sheet.setColumnWidth(4, (10 * 600));


                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList  1  : " + sorted_OrdersList.size());
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum:  1  " + rowNum);


                        if (rowNum > sorted_OrdersList.size()) {


                            try {
                                for (int ij = 0; ij < replacementTransactionDateArray.size(); ij++) {
                                    String transactiondate =replacementTransactionDateArray.get(ij);
                                    List<Modal_ReplacementTransactionDetails> replacementTransactionDetailsArray = replacementTransactionDateHashmap.get(transactiondate);

                                    for (int i1 = 0; i1 < replacementTransactionDetailsArray.size(); i1++) {

                                        Modal_ReplacementTransactionDetails modal_replacementTransactionDetails = replacementTransactionDetailsArray.get(i1);
                                        double refundAmount = 0;
                                        double replacementAmount = 0;

                                        if(transactiondate.equals(date)){
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
                                        else{

                                        }
                                    }

                                    if(replacementTransactionDateArray.size() - ij ==1){
                                        rowNum = rowNum +5;
                                        Row row = sheet.createRow(rowNum++);
                                        row.createCell(0).setCellValue("   ");
                                        row.createCell(1).setCellValue("Replacement Value :  ");
                                        row.createCell(2).setCellValue(" Rs. "+ totalReplacementAmount);

                                        rowNum = rowNum +1;

                                        Row row1 = sheet.createRow(rowNum++);
                                        row1.createCell(0).setCellValue("   ");
                                        row1.createCell(1).setCellValue("Refund Value :  ");
                                        row1.createCell(2).setCellValue(" Rs. "+ totalRefundAmount);

                                    }


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + sorted_OrdersList.size());
                            Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);
                            if(isSheetForLastDateGenerate) {

                                if (SDK_INT >= Build.VERSION_CODES.R) {

                                    if (Environment.isExternalStorageManager()) {
                                        try {
                                            GenerateExcelSheet();


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


                                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GenerateCustomerMobileNo_BillValueReport.this, WRITE_EXTERNAL_STORAGE);
                                    //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                                    // If do not grant write external storage permission.
                                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                                        // Request user to grant write external storage permission.
                                        ActivityCompat.requestPermissions(GenerateCustomerMobileNo_BillValueReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                                    } else {
                                        showProgressBar(true);
                                        try {
                                            GenerateExcelSheet();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            ;
                                        }
                                    }
                                }
                            }
                            else{

                            }
                            // GenerateExcelSheet();
                        } else {
                            //  Toast.makeText(mContext,+sorted_OrdersList.size(),Toast.LENGTH_LONG).show();


                        }


                }
            } else {
                if(isSheetForLastDateGenerate) {

                    if (SDK_INT >= Build.VERSION_CODES.R) {

                        if (Environment.isExternalStorageManager()) {
                            try {
                                GenerateExcelSheet();


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


                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GenerateCustomerMobileNo_BillValueReport.this, WRITE_EXTERNAL_STORAGE);
                        //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(GenerateCustomerMobileNo_BillValueReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {
                            showProgressBar(true);
                            try {
                                GenerateExcelSheet();

                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
                            }
                        }
                    }
                }
                else{

                }
                Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, "There is no data to create sheet", Toast.LENGTH_LONG).show();

            }
        }

    }

    private List<Modal_ManageOrders_Pojo_Class> getorderlistdatewise(List<Modal_ManageOrders_Pojo_Class> ordersList, String date) {
        OrderstoGenerateSheet.clear();
        for (int i = 0; i < ordersList.size(); i++) {
            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(i);
            String slotdate  = modal_manageOrders_pojo_class.getSlotdate();

            if(slotdate.equals(date)){
                Log.e("Failed", "Storage not available or read only");
                OrderstoGenerateSheet.add(modal_manageOrders_pojo_class);



            }
            else{
                Log.e("slotdate", "slotdate "+slotdate);
                Log.e("slotdate", "slotdate id"+modal_manageOrders_pojo_class.getOrderid());
                Log.e("slotdate", "slotdate type"+modal_manageOrders_pojo_class.getOrderType());

            }
        }
        if (OrderstoGenerateSheet.size() > 0) {
            Collections.sort(OrderstoGenerateSheet, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                    String orderplacedtime_1 = object1.getOrderType();
                    String orderplacedtime_2 = object2.getOrderType();

                    if ((orderplacedtime_1.equals("")) || (orderplacedtime_1.equals("null")) || (orderplacedtime_1.equals(null))) {
                        orderplacedtime_1 = String.valueOf(0);
                    }
                    if ((orderplacedtime_2.equals("")) || (orderplacedtime_2.equals("null")) || (orderplacedtime_2.equals(null))) {
                        orderplacedtime_2 = String.valueOf(0);
                    }



                    return orderplacedtime_2.compareTo(orderplacedtime_1);
                }
            });

        }


            return  OrderstoGenerateSheet;
        }
    private void GenerateExcelSheet() {
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/CustomerNo & Bill Value Sheet";
        File dir = new File(path);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");

        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "Customer Mobileno & Bill value.xls");


        //   File file = new File(getExternalFilesDir(null), "Onlineorderdetails.xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            showProgressBar(false);
            isSheetForLastDateGenerate = false;
          /*  StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Uri pdfUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pdfUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", file);
            } else {
                pdfUri = Uri.fromFile(file);
            }
            Intent pdfViewIntent = new Intent(Intent.ACTION_SEND);
            pdfViewIntent.setDataAndType(pdfUri,"application/xls");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(pdfViewIntent, "Share  File via ");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Adjusting_Widgets_Visibility(false);

                startActivityForResult(intent, OPENPDF_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }



           */

            //     startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.fromFile(file), "application/xls"));

           // Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
           // Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
            Objects.requireNonNull(outputStream).close();

        } catch (java.io.IOException e) {
            e.printStackTrace();
            showProgressBar(false);

            Toast.makeText(getApplicationContext(), "File can't be  Created Permission Denied", Toast.LENGTH_LONG).show();

        }
        Uri pdfUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pdfUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", file);
        } else {
            pdfUri = Uri.fromFile(file);
        }
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.setType("application/xls");
        share.putExtra(Intent.EXTRA_STREAM, pdfUri);
        startActivity(Intent.createChooser(share, "Share"));


    }
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }





    private void getdataFromReplacementTransaction(String startdateString_forReplacementransaction, String enddateString_forReplacementransaction, String vendorKey) {
        if (isgetReplacementOrderForSelectedDateCalled) {
            return;
        }
        isgetReplacementOrderForSelectedDateCalled = true;
        isReplacementTransacDetailsResponseReceivedForSelectedDate = false;
        showProgressBar(true);

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
                Toast.makeText(GenerateCustomerMobileNo_BillValueReport.this, "There is no Orders Yet ", Toast.LENGTH_LONG).show();
                showProgressBar(false);
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
        Volley.newRequestQueue(GenerateCustomerMobileNo_BillValueReport.this).add(jsonObjectRequest);


    }

    private void convertReplacementTransactionDetailsJsonIntoArray(String stringOfArray) {
            String date = "";
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
                            date = String.valueOf(json.getString("transactiontime"));
                            date = convertDatetoNormalFormat(date);
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
                try {
                    Log.i("TransactionDetailsArray", String.valueOf(replacementTransactionDetailsArray.size()));
                    Log.i("TransactiontypeArray", String.valueOf(replacementTransactionDateArray.size()));
                    Log.i("TransactiontypeHashmap", String.valueOf(replacementTransactionDateHashmap.size()));
                    Log.i("transactionArray", String.valueOf(replacementTransactionDetailsArray.size()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (transactionStatus.toString().toUpperCase().equals("SUCCESS")) {
                        try {
                            replacementTransactionDetailsArray.add(modal_replacementTransactionDetails);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        try {

                            if (replacementTransactionDateArray.contains(date)) {
                                replacementTransactionDateHashmap.get(date).add(modal_replacementTransactionDetails);
                            } else {
                                replacementTransactionDateArray.add(date);
                                replacementTransactionDateHashmap.put(date, replacementTransactionDetailsArray);
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
    private String convertDatetoNormalFormat(String ndate) {
        String convertedDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(ndate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);


        Date c1 = calendar.getTime();


        SimpleDateFormat df = new SimpleDateFormat();

            df = new SimpleDateFormat("EEE, d MMM yyyy");


        convertedDate = df.format(c1);
        return  convertedDate;
    }


    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        if(isSwitchisOn){
            dateSelectorLayout.setVisibility(View.VISIBLE);
            fromdateSelectorLayout.setVisibility(View.GONE);
            todateSelectorLayout.setVisibility(View.GONE);
        }
        else{
            fromdateSelectorLayout.setVisibility(View.VISIBLE);
            todateSelectorLayout.setVisibility(View.VISIBLE);
            dateSelectorLayout.setVisibility(View.GONE);

        }
        mobile_search_button.setVisibility(View.VISIBLE);
        mobile_search_close_btn.setVisibility(View.GONE);
        mobile_search_barEditText.setVisibility(View.GONE);

    }

    private void showSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.GONE);
        mobile_search_button.setVisibility(View.GONE);
        mobile_search_close_btn.setVisibility(View.VISIBLE);
        mobile_search_barEditText.setVisibility(View.VISIBLE);
        fromdateSelectorLayout.setVisibility(View.GONE);
        todateSelectorLayout.setVisibility(View.GONE);
    }




    private long getMillisecondsFromDate(String dateString) {
        Calendar calendarr = Calendar.getInstance();



        calendarr.add(Calendar.DATE,-1);



        long milliseconds = calendarr.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try{
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(dateString);
            System.out.println("Given Time in milliseconds : "+date.getTime());

            Calendar calendar = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
            System.out.println("Given Time in milliseconds : "+calendar.getTimeInMillis());
            milliseconds = calendar.getTimeInMillis();
        }catch(ParseException e){
            e.printStackTrace();
        }
        return  milliseconds;
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

    private String getTomorrowsDate(String datestring) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, 1);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  tomorrowdayDate = df1.format(c1);
        String tomorrowAsString = PreviousdayDay+", "+tomorrowdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return tomorrowAsString;

    }




    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();


        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String PreviousdayDate = df.format(c1);
            return PreviousdayDate;

        }
        else {
            SimpleDateFormat previousday = new SimpleDateFormat("EEE");
            String PreviousdayDay = previousday.format(c1);

            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
            String PreviousdayDate = df1.format(c1);
            PreviousdayDate = PreviousdayDay + ", " + PreviousdayDate;


            return PreviousdayDate;
        }
    }


    private String convertNormalDateintoReplacementTransactionDetailsDate(String sDate, String Time) {
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


        Date c1 = calendar.getTime();


        SimpleDateFormat df = new SimpleDateFormat();
        if (Time.equals("STARTTIME")) {
            df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        } else {
            df = new SimpleDateFormat("yyyy-MM-dd 23:59:59");

        }

        String Date = df.format(c1);
        return Date;
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



    public String getstartDate_and_time_TransactionTable() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => 2022-03-01T10:03:14+0530 " + c);


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String FormattedTime = dfTime.format(c);

        return FormattedTime;
    }

    public String getendDate_and_time_TransactionTable() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => 2022-03-01T10:03:14+0530 " + c);


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String FormattedTime = dfTime.format(c);

        return FormattedTime;
    }

    private String getDatewithNameofthePreviousDayfromSelectedDay2(String sDate) {
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



    private String getOldDatewithNameusingCurrentDate(String sDate,String Count) {

        if(orderdetailsnewschema) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = dateFormat.parse(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);


            calendar.add(Calendar.DATE, Integer.parseInt(Count));


            Date c1 = calendar.getTime();


            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            String PreviousdayDate = df1.format(c1);
            String yesterdayAsString = PreviousdayDate;
            return yesterdayAsString;
        }
        else{


            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
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

            calendar.add(Calendar.DATE, Integer.parseInt(Count));


            Date c1 = calendar.getTime();

            SimpleDateFormat previousday = new SimpleDateFormat("EEE");
            String PreviousdayDay = previousday.format(c1);


            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
            String PreviousdayDate = df1.format(c1);
            String yesterdayAsString = PreviousdayDay + ", " + PreviousdayDate;
            Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);
            return yesterdayAsString;
        }

    }



    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);
        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = df.format(c);
            return CurrentDate;

        }
        else {
            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(c);

            System.out.println("Current  " + CurrentDate);


            return CurrentDate;
        }
    }


    private String convertnewFormatDateintoOldFormat(String todaysdate) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("EEE");
           String CurrentDay = day.format(date);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(date);

            CurrentDate = CurrentDay + ", " + CurrentDate;



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }


    private String getDatewithNameoftheDay() {

        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();

        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = df.format(c);
            return CurrentDate;

        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE");
           String CurrentDay = day.format(c);

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;


            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;

        }
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




    private String getLongValuefortheDate(String orderplacedtime) {
        String longvalue = "";
        try {
            String time1 = orderplacedtime;
            //   Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = sdf.parse(time1);
            long time1long = date.getTime() / 1000;
            longvalue = String.valueOf(time1long);
          /*  String time2 = "Sat, 24 Apr 2021 07:50:28";
            Date date2 = sdf.parse(time2);

            long time2long =  date2.getTime() / 1000;
            Log.d(TAG, "time1 "+time1long + " time2 "+time2long);

           */
            //   long differencetime = time2long - time1long;
            //  Log.d(TAG, "   "+orderplacedtime);

            //   Log.d(TAG, "time1long  "+time1long);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                String time1 = orderplacedtime;
                //     Log.d(TAG, "time1long  "+orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
                Date date = sdf.parse(time1);
                long time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);

                //   long differencetime = time2long - time1long;
                //  Log.d(TAG, "   "+orderplacedtime);

                //    Log.d(TAG, "time1long  "+time1long);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return longvalue;
    }



    private String getDatewithNameoftheseventhDayFromSelectedStartDate(String sDate) {
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

        calendar.add(Calendar.DATE, 6);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }
    private String getLongValuefortheDate2(String orderplacedtime) {
        String longvalue = "";


        if(orderdetailsnewschema) {
            try {
                String time1 = orderplacedtime;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(time1);
                long time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);

            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    String time1 = orderplacedtime;

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sdf.parse(time1);
                    long time1long = date.getTime() / 1000;
                    longvalue = String.valueOf(time1long);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return longvalue;
        }
        else{
            try {
                String time1 = orderplacedtime;
                Log.d(Constants.TAG, "time1long  " + orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
                Date date = sdf.parse(time1);
                long time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);
          /*  String time2 = "Sat, 24 Apr 2021 07:50:28";
            Date date2 = sdf.parse(time2);

            long time2long =  date2.getTime() / 1000;
            Log.d(TAG, "time1 "+time1long + " time2 "+time2long);

           */
                //   long differencetime = time2long - time1long;
                //  Log.d(TAG, "   "+orderplacedtime);

                //   Log.d(TAG, "time1long  "+time1long);
            } catch (Exception ex) {
                ex.printStackTrace();
                try {
                    String time1 = orderplacedtime;
                    Log.d(Constants.TAG, "time1long  " + orderplacedtime);

                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
                    Date date = sdf.parse(time1);
                    long time1long = date.getTime() / 1000;
                    longvalue = String.valueOf(time1long);

                    //   long differencetime = time2long - time1long;
                    //  Log.d(TAG, "   "+orderplacedtime);

                    //    Log.d(TAG, "time1long  "+time1long);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return longvalue;
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
}