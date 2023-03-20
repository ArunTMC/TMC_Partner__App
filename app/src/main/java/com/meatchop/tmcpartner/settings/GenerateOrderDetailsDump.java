package com.meatchop.tmcpartner.settings;

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
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableService;

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
import java.util.TimeZone;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class GenerateOrderDetailsDump extends AppCompatActivity {
        private Spinner daysCountSpinner;
        private   String[] daysCountSpinnerArrayList = new String[] {
               "1", "2", "3", "4", "5", "6", "7","8","9","10"
        };
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        private Switch switchfor_singleDayDump;
        private LinearLayout dateSelectorLayout,daysCountSpinner_layout,generateReport_Layout,fromdateSelectorLayout,todateSelectorLayout;
        private TextView orderinstruction,dateSelector_text,generateDataInstruction,fromdateSelector_text,todateSelector_text;
        private DatePickerDialog datepicker;
        private DatePickerDialog fromdatepicker;

        private DatePickerDialog todatepicker;

    List<Modal_ManageOrders_Pojo_Class> ordersList;
        public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
        public static List<String> array_of_orderId;
        double screenInches;
        String PreviousDateString,DateString,TodaysDate,vendorKey,vendorname,OrderDetailsResultjsonString,CurrentDate,LastDate,fromdatestring,todatestring,currentdateLong,oldDayLong;
        public static LinearLayout loadingpanelmask;
        public static LinearLayout loadingPanel;
        public LinearLayout newOrdersSync_Layout;
        TextView appOrdersCount_textwidget, mobile_nameofFacility_Textview;
        ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
        EditText mobile_search_barEditText;
        ListView manageOrders_ListView;
        boolean isSpinnerTouch =false;
        static boolean isSearchButtonClicked = false;
        Workbook wb;
        Sheet sheet = null;

    String selectedStartDate = "";
    String selectedEndDate = "";
        private static String[] columns = {"Order Details Key", "Order Placed Time","OrderType","User Mobile","Slot Name","Slot Date", "Slot Time Range",  "Item Desp","DeliveryType", "Orderid","Payment Mode", "Payable Amount","Coupon Discount Amount","Coupon Key","Vendor Key","Delivery Charge","Delivery Distance","User Address","Order Confirmed Time",
                "Order Ready Time","Order PickedUp Time", "Order Delivered Time ", "Token No", "Order Status" , "latitude" , "Longitude" , "Address key" , "user key"};
        int spinnerselecteditem=1;
        int spinnerselecteditem_Count =1;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;



    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_order_details_dump_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        switchfor_singleDayDump = findViewById(R.id.switchfor_singleDayDump);
        daysCountSpinner = (Spinner) findViewById(R.id.daysCountSpinner);
        daysCountSpinner_layout = findViewById(R.id.daysCountSpinner_layout);
        orderinstruction = findViewById(R.id.orderinstruction);
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
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_orderId = new ArrayList<>();



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
            vendorKey = (shared.getString("VendorKey", ""));
            vendorname = (shared.getString("VendorName", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
           // orderdetailsnewschema = false;

            try {
                ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
                screenInches = screenSizeOfTheDevice.getDisplaySize(GenerateOrderDetailsDump .this);
                //Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                e.printStackTrace();
                try {
                    DisplayMetrics dm = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(dm);
                    double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                    double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                    screenInches = Math.sqrt(x + y);
                  //  Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

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
                //String date = getDate();
               // String sevendaysbackdate = getOldDatewithNameusingCurrentDate(date,"-7");
               // currentdateLong = getLongValuefortheDate(date);
               // oldDayLong = getLongValuefortheDate(sevendaysbackdate);

               // isSearchButtonClicked = false;

                ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();
                if(orderdetailsnewschema){
                    String oldformat = convertnewFormatDateintoOldFormat(TodaysDate);
                    dateSelector_text.setText(oldformat);
                    fromdateSelector_text.setText(oldformat);
                    todateSelector_text.setText(oldformat);

                }
                else{

                    dateSelector_text.setText(TodaysDate);
                    //  getOrderDetailsUsingOrderOrderPlacedDate(TodaysDate, vendorKey, orderStatus);
                    fromdateSelector_text.setText(TodaysDate);
                    todateSelector_text.setText(TodaysDate);

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
                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GenerateOrderDetailsDump.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    // If do not grant write external storage permission.
                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(GenerateOrderDetailsDump.this, "Click Allow and then Generate Again", Toast.LENGTH_SHORT).show();

                        // Request user to grant write external storage permission.
                        ActivityCompat.requestPermissions(GenerateOrderDetailsDump.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);


                    } else {
                        showProgressBar(true);

                        try {
                            AddDatatoExcelSheet(ordersList,"orderDetailsfrom   "+fromdatestring);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

 */




                    if (SDK_INT >= Build.VERSION_CODES.R) {

                        if(Environment.isExternalStorageManager()){
                            try {
                                showProgressBar(true);

                                AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);


                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
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


                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GenerateOrderDetailsDump.this, WRITE_EXTERNAL_STORAGE);
                        //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(GenerateOrderDetailsDump.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {
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
        });

        switchfor_singleDayDump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    generateDataInstruction.setText("#Note : Select From Date and To Date . Can't Generate More than 7 Days ");
              //      daysCountSpinner_layout.setVisibility(View.VISIBLE);
                    fromdateSelectorLayout.setVisibility(View.VISIBLE);
                    todateSelectorLayout.setVisibility(View.VISIBLE);
                    dateSelectorLayout.setVisibility(View.GONE);
                    newOrdersSync_Layout.setVisibility(View.GONE);
                }
                else{
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


     /*   daysCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        // Toast.makeText(GenerateOrderDetailsDump.this, String.valueOf(datebasedOn_SpinnerCount), Toast.LENGTH_LONG).show();
                         String previousDayDateForSpinnerCountDate = getDatewithNameofthePreviousDayfromSelectedDay2(datebasedOn_SpinnerCount);
                        // Toast.makeText(GenerateOrderDetailsDump.this, String.valueOf(previousDayDateForSpinnerCountDate), Toast.LENGTH_LONG).show();
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
                array_of_orderId.clear();
                spinnerselecteditem = 1 ;
                spinnerselecteditem_Count=1;
                showProgressBar(true);
                todatestring=dateSelector_text.getText().toString();
                fromdatestring = todatestring;
                PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);


                isSearchButtonClicked = false;

                if(orderdetailsnewschema){
                    String newformat = convertOldFormatDateintoNewFormat(todatestring);
                    dateSelector_text.setText(todatestring);
                    fromdateSelector_text.setText(todatestring);
                    todateSelector_text.setText(todatestring);
                    callVendorOrderDetailsSeviceAndInitCallBack(newformat,newformat,vendorKey);

                }
                else{

                    dateSelector_text.setText(todatestring);
                    fromdateSelector_text.setText(todatestring);
                    todateSelector_text.setText(todatestring);
                    //  Adjusting_Widgets_Visibility(true);

                    getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString,todatestring, vendorKey);

                }




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
                    Toast.makeText(GenerateOrderDetailsDump.this, "Please Select Date First", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(GenerateOrderDetailsDump.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
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
                        AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
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
                        AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);
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
        fromdatepicker = new DatePickerDialog(GenerateOrderDetailsDump.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();

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
                         //   PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            fromdateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            fromdatestring = fromdateSelector_text.getText().toString();
                            selectedStartDate = fromdatestring;
                            selectedEndDate = getDatewithNameoftheseventhDayFromSelectedStartDate(DateString);

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

        c.add(Calendar.MONTH, -12);
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
        todatepicker = new DatePickerDialog(GenerateOrderDetailsDump.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();

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
                         //   PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

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
        SimpleDateFormat sdformat = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
        sdformat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
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
        c.add(Calendar.DATE, -30);
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

        spinnerselecteditem = 1 ;
        spinnerselecteditem_Count=1;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(GenerateOrderDetailsDump.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();

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
                            fromdatestring = dateSelector_text.getText().toString();
                            if(orderdetailsnewschema){
                                String FromdateAsNewFormat =convertOldFormatDateintoNewFormat(fromdatestring);
                                String TodateAsNewFormat =convertOldFormatDateintoNewFormat(todatestring);

                                callVendorOrderDetailsSeviceAndInitCallBack(FromdateAsNewFormat, TodateAsNewFormat,vendorKey);


                            }
                            else{


                                //  Adjusting_Widgets_Visibility(true);

                                getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, DateString, vendorKey);

                            }

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
        mVolleyService = new VendorOrdersTableService(mResultCallback,GenerateOrderDetailsDump.this);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingFromToSlotDate_vendorkey + "?fromslotdate="+FromDate+"&vendorkey="+vendorKey+"&toslotdate="+ToDate;
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingFromToSlotDate_vendorkey + "?fromslotdate="+FromDate+"&vendorkey="+vendorKey+"&toslotdate="+ToDate;

        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }





    private void getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(String previousDateString, String todaysdate, String vendorKey) {
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        showProgressBar(true);




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
                    Toast.makeText(GenerateOrderDetailsDump.this, "There is no Order     " + todaysdate, Toast.LENGTH_LONG).show();
                    if(array_of_orderId.size()<=0){
                   loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.GONE);
                    orderinstruction.setText("No Order today");

                    orderinstruction.setVisibility(View.VISIBLE);
                    appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));
                }
                    if(todaysdate.equals(todatestring)) {
                        //   Toast.makeText(GenerateOrderDetailsDump.this, String.valueOf(spinnerselecteditem_Count), Toast.LENGTH_LONG).show();
                        //    Toast.makeText(GenerateOrderDetailsDump.this, String.valueOf("spinnerselecteditem  "+spinnerselecteditem), Toast.LENGTH_LONG).show();

                        // appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));
                        DisplayOrderListDatainListView(ordersList);
                    }
                    else{
                        String nextday = getTomorrowsDate(todaysdate);
                        calculate_the_dateandgetData(nextday,todatestring);

                    }


//
                    showProgressBar(false);
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
        Volley.newRequestQueue(GenerateOrderDetailsDump.this).add(jsonObjectRequest);







    }

    private String convertOldFormatDateintoNewFormat(String todaysdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
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


    private long getMillisecondsFromDate(String dateString) {
        Calendar calendarr = Calendar.getInstance();



        calendarr.add(Calendar.DATE,-1);



        long milliseconds = calendarr.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try{
            //formatting the dateString to convert it into a Date
            Date date = sdf.parse(dateString);

            Calendar calendar = Calendar.getInstance();
            //Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
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

        SimpleDateFormat dayname = new SimpleDateFormat("EEE",Locale.ENGLISH);
        dayname.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String Currentday = dayname.format(c);




        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDate = df.format(c);
        String date = Currentday+", "+CurrentDate;


        return date;
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


                    if(json.has("couponkey")){
                        manageOrdersPojoClass.couponkey = String.valueOf(json.get("couponkey"));

                    }
                    else{
                        manageOrdersPojoClass.couponkey ="";
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



                    try {
                        if (ordertype.toUpperCase().equals(Constants.APPORDER)) {
                            if (json.has("deliveryamount")) {

                                String deliveryamount =  String.valueOf(json.get("deliveryamount"));
                                if(!deliveryamount.equals(null)&&(!deliveryamount.equals("null"))){
                                    manageOrdersPojoClass.deliveryamount = String.valueOf(json.get("deliveryamount"));

                                }
                                else {
                                    manageOrdersPojoClass.deliveryamount ="";

                                }
                            } else {
                                manageOrdersPojoClass.deliveryamount = "";
                            }

                        }
                    }catch (Exception E){
                        manageOrdersPojoClass.deliveryamount ="-";
                        E.printStackTrace();
                    }





                    ordersList.add(manageOrdersPojoClass);

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }


            }
            spinnerselecteditem_Count--;
            if(todaysdate.equals(todatestring)) {
             //   Toast.makeText(GenerateOrderDetailsDump.this, String.valueOf(spinnerselecteditem_Count), Toast.LENGTH_LONG).show();
            //    Toast.makeText(GenerateOrderDetailsDump.this, String.valueOf("spinnerselecteditem  "+spinnerselecteditem), Toast.LENGTH_LONG).show();

                // appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));
                DisplayOrderListDatainListView(ordersList);
            }
            else{
                String nextday = getTomorrowsDate(todaysdate);
                calculate_the_dateandgetData(nextday,todatestring);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            showProgressBar(false);
        }
    }

    private void DisplayOrderListDatainListView(List<Modal_ManageOrders_Pojo_Class> ordersList) {
        try {

            if (ordersList.size() > 0) {
                Collections.sort(ordersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                    public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                        String orderplacedtime_1 = object1.getOrderplacedtime_in_long();
                        String orderplacedtime_2 = object2.getOrderplacedtime_in_long();

                        if((orderplacedtime_1.equals(""))||(orderplacedtime_1.equals("null"))||(orderplacedtime_1.equals(null))){
                            orderplacedtime_1=String.valueOf(0);
                        }
                        if((orderplacedtime_2.equals(""))||(orderplacedtime_2.equals("null"))||(orderplacedtime_2.equals(null))){
                            orderplacedtime_2=String.valueOf(0);
                        }

                        Long i2 = Long.valueOf(orderplacedtime_2);
                        Long i1 = Long.valueOf(orderplacedtime_1);

                        return i1.compareTo(i2);
                    }
                });



               appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));




                Adapter_Edit_Or_CancelTheOrders adapter_edit_or_cancelTheOrders = new Adapter_Edit_Or_CancelTheOrders(GenerateOrderDetailsDump.this, ordersList, GenerateOrderDetailsDump.this,false);
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
                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));

                Adapter_Edit_Or_CancelTheOrders adapter_edit_or_cancelTheOrders = new Adapter_Edit_Or_CancelTheOrders(GenerateOrderDetailsDump.this, ordersList, GenerateOrderDetailsDump.this,false);
                manageOrders_ListView.setAdapter(adapter_edit_or_cancelTheOrders);


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


    private void AddDatatoExcelSheet(List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList, String name) {


        Log.d(Constants.TAG, "prepareDataForExcelSheet type  addData: " + name);


        sheet = wb.createSheet(name);
        int rowNum = 1;

        if(sorted_OrdersList.size()>0){
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

                for (int i = 0; i < columns.length; i++) {
                    headercell = headerRow.createCell(i);
                    headercell.setCellValue(columns[i]);
                    headercell.setCellStyle(headerCellStyle);
                }
                Log.d(Constants.TAG, "prepareDataForExcelSheet type  itemRow: " + String.valueOf( itemRow.getItemdesp()));


                try {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(itemRow.getOrderdetailskey());
                    row.createCell(1).setCellValue(itemRow.getOrderplacedtime());
                    row.createCell(2).setCellValue(itemRow.getOrderType());
                    row.createCell(3).setCellValue(itemRow.getUsermobile());
                    row.createCell(4).setCellValue(itemRow.getSlotname());
                    row.createCell(5).setCellValue(itemRow.getSlotdate());
                    row.createCell(6).setCellValue(itemRow.getSlottimerange());
                    row.createCell(7).setCellValue(String.valueOf(itemRow.getItemdesp()));

                    row.createCell(8).setCellValue(itemRow.getDeliverytype());

                    row.createCell(9).setCellValue(itemRow.getOrderid());
                    row.createCell(11).setCellValue(itemRow.getPayableamount());
                    row.createCell(10).setCellValue(String.valueOf(itemRow.getPaymentmode()));
                    row.createCell(12).setCellValue(String.valueOf(itemRow.getCoupondiscamount()));
                    row.createCell(13).setCellValue(String.valueOf(itemRow.getCouponkey()));

                    row.createCell(14).setCellValue(String.valueOf(itemRow.getVendorkey()));
                    if(!(String.valueOf(itemRow.getDeliveryamount()).equals("null"))) {
                        row.createCell(15).setCellValue(String.valueOf(itemRow.getDeliveryamount()));
                    }
                    else{
                        row.createCell(15).setCellValue("");

                    }


                    if(!(String.valueOf(itemRow.getDeliverydistance()).equals("null"))) {
                        row.createCell(16).setCellValue(String.valueOf(itemRow.getDeliverydistance()));
                    }
                    else{
                        row.createCell(16).setCellValue("");

                    }

                        if(!(String.valueOf(itemRow.getUseraddress()).equals("null"))){
                        row.createCell(17).setCellValue(String.valueOf(itemRow.getUseraddress()));
                    }
                    else{
                        row.createCell(17).setCellValue("");

                    }


                    row.createCell(18).setCellValue(itemRow.getOrderconfirmedtime());
                    row.createCell(19).setCellValue(itemRow.getOrderreadytime());
                    row.createCell(20).setCellValue(itemRow.getOrderpickeduptime());
                    row.createCell(21).setCellValue(itemRow.getOrderdeliveredtime());
                    row.createCell(22).setCellValue(itemRow.getTokenno());
                    row.createCell(23).setCellValue(String.valueOf(itemRow.getOrderstatus()));

                    if(!(String.valueOf(itemRow.getDeliveryPartnerName()).equals("null"))){
                        row.createCell(24).setCellValue(String.valueOf(itemRow.getDeliveryPartnerName()));
                    }
                    else{
                        row.createCell(24).setCellValue("");

                    }



                    if(!(String.valueOf(itemRow.getDeliveryPartnerMobileNo()).equals("null"))){
                        row.createCell(25).setCellValue(String.valueOf(itemRow.getDeliveryPartnerMobileNo()));
                    }
                    else{
                        row.createCell(25).setCellValue("");

                    }


                    if(!(String.valueOf(itemRow.getUseraddresslat()).equals("null"))){
                        row.createCell(26).setCellValue(String.valueOf(itemRow.getUseraddresslat()));
                    }
                    else{
                        row.createCell(26).setCellValue("");

                    }


                    if(!(String.valueOf(itemRow.getUseraddresslon()).equals("null"))){
                        row.createCell(27).setCellValue(String.valueOf(itemRow.getUseraddresslon()));
                    }
                    else{
                        row.createCell(27).setCellValue("");

                    }


                    if(!(String.valueOf(itemRow.getUseraddresskey()).equals("null"))){
                        row.createCell(28).setCellValue(String.valueOf(itemRow.getUseraddresskey()));
                    }
                    else{
                        row.createCell(28).setCellValue("");

                    }
                    if(!(String.valueOf(itemRow.getUserkey()).equals("null"))){
                        row.createCell(29).setCellValue(String.valueOf(itemRow.getUserkey()));
                    }
                    else{
                        row.createCell(29).setCellValue("");

                    }







                } catch (Exception e) {
                    e.printStackTrace();
                }


                sheet.setColumnWidth(0, (10 * 600));
                sheet.setColumnWidth(1, (10 * 600));
                sheet.setColumnWidth(2, (10 * 600));
                sheet.setColumnWidth(3, (10 * 600));
                sheet.setColumnWidth(4, (10 * 600));
                sheet.setColumnWidth(5, (10 * 600));
                sheet.setColumnWidth(6, (10 * 600));
                sheet.setColumnWidth(7, (10 * 600));
                sheet.setColumnWidth(8, (10 * 600));
                sheet.setColumnWidth(9, (10 * 600));
                sheet.setColumnWidth(10, (10 * 600));
                sheet.setColumnWidth(11, (20 * 600));
                sheet.setColumnWidth(12, (10 * 600));
                sheet.setColumnWidth(13, (10 * 600));
                sheet.setColumnWidth(14, (10 * 600));
                sheet.setColumnWidth(15, (10 * 600));
                sheet.setColumnWidth(16, (10 * 600));
                sheet.setColumnWidth(17, (10 * 600));
                sheet.setColumnWidth(18, (10 * 600));
                sheet.setColumnWidth(19, (10 * 600));
                sheet.setColumnWidth(20, (10 * 800));
                sheet.setColumnWidth(21, (10 * 800));
                sheet.setColumnWidth(22, (10 * 800));
                sheet.setColumnWidth(23, (10 * 800));
                sheet.setColumnWidth(24, (10 * 800));
                sheet.setColumnWidth(25, (10 * 800));
                //
                sheet.setColumnWidth(26, (10 * 800));
                sheet.setColumnWidth(27, (10 * 800));
                sheet.setColumnWidth(28, (10 * 800));
                sheet.setColumnWidth(29, (10 * 800));





                Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList  1  : " + sorted_OrdersList.size());
                Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum:  1  " + rowNum);

                    if (rowNum > sorted_OrdersList.size()) {
                        Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + sorted_OrdersList.size());
                        Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);

                        GenerateExcelSheet();
                    }
                    else {
                       //   Toast.makeText(GenerateOrderDetailsDump.this,"here is no data to create",Toast.LENGTH_LONG).show();


                }


            }
        }
           else{
                  Toast.makeText(GenerateOrderDetailsDump.this,"There is no data to create sheet",Toast.LENGTH_LONG).show();

            }

    }

    private void GenerateExcelSheet() {
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/Order Details Dump Sheet/";
        File dir = new File(path);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");

        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "OrderDetailsDump"+ System.currentTimeMillis()  +".xls");


        //   File file = new File(getExternalFilesDir(null), "Onlineorderdetails.xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            showProgressBar(false);
          //  Toast.makeText(getApplicationContext(), "File can't be  Created", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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

            Toast.makeText(getApplicationContext(), "File Created", Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            Toast.makeText(getApplicationContext(), "File can't Created Permission Denied", Toast.LENGTH_LONG).show();

            e.printStackTrace();
            showProgressBar(false);


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





    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.VISIBLE);
        mobile_search_button.setVisibility(View.VISIBLE);
        mobile_search_close_btn.setVisibility(View.GONE);
        mobile_search_barEditText.setVisibility(View.GONE);
    }

    private void showSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.GONE);
        mobile_search_button.setVisibility(View.GONE);
        mobile_search_close_btn.setVisibility(View.VISIBLE);
        mobile_search_barEditText.setVisibility(View.VISIBLE);
    }




    private String getTomorrowsDate(String datestring) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
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

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String  tomorrowdayDate = df1.format(c1);
        String tomorrowAsString = PreviousdayDay+", "+tomorrowdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return tomorrowAsString;

    }



    private String getDatewithNameoftheseventhDayFromSelectedStartDate(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

        calendar.add(Calendar.DATE, 10);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }



    private String convertnewFormatDateintoOldFormat(String todaysdate) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String CurrentDay = day.format(date);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(date);

            CurrentDate = CurrentDay + ", " + CurrentDate;



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }



    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();


        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String PreviousdayDate = df.format(c1);
            return PreviousdayDate;

        }
        else {
            SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
            previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String PreviousdayDay = previousday.format(c1);

            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String PreviousdayDate = df1.format(c1);
            PreviousdayDate = PreviousdayDay + ", " + PreviousdayDate;


            return PreviousdayDate;
        }
    }



    private String getDatewithNameofthePreviousDayfromSelectedDay(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }



    private String getDatewithNameofthePreviousDayfromSelectedDay2(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }



    private String getOldDatewithNameusingCurrentDate(String sDate,String Count) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }



    private String getDate() {
        Date c = Calendar.getInstance().getTime();



        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDate = df.format(c);



        //CurrentDate = CurrentDay+", "+CurrentDate;


        return CurrentDate;
    }


    private String getDatewithNameoftheDay() {

        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();

        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(c);
            return CurrentDate;

        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            String CurrentDay = day.format(c);

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;


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

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


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