package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_vendor;
import com.meatchop.tmcpartner.R;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class GenerateUserDetailsReport extends AppCompatActivity {
    private Switch switchfor_singleDayDump;
    private LinearLayout dateSelectorLayout,daysCountSpinner_layout,generateReport_Layout,fromdateSelectorLayout,todateSelectorLayout;
    private TextView orderinstruction,dateSelector_text,generateDataInstruction,fromdateSelector_text,todateSelector_text, usercreated_withaddress_textview
    , totalAccountCreatedtextview,no_of_users_hastinapuram_textview,no_of_users_velachery_textview,generate_shareVelacherySheet,generate_viewVelacherySheet,
    totalAccountCreatedLabel, usercreated_withaddress_Label,no_of_users_velachery_Label,no_of_users_hastinapuram_Label,  generate_viewHastinapuramSheet,generate_shareHastinapuramSheet,generate_shareusersWithAddress,fetchUserData_button, userWithAddressViewSheet,userWithAddressShareSheet,
            datewise_viewOnlyUserDetails,datewise_shareOnlyUserDetails;
    private DatePickerDialog datepicker;
    private DatePickerDialog fromdatepicker;

    private DatePickerDialog todatepicker;

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
    String[] columns = {"Mobile Number", "Name","App Version","Device Type","Created Time","Updated Time", "Address Type", "Address", "LandMark", "Latitude", "Longitude", "Delivery Distance", "Vendor Name", "Vendor Key", "Email", "ContactPerson Mobile No", "Contact Person Name", "user Key"};

    String[] columnswithSno = {"S.no","Mobile Number", "Name", "Address Type", "Address", "LandMark", "Latitude", "Longitude", "Delivery Distance", "Vendor Name", "Vendor Key", "Email", "ContactPerson Mobile No", "Contact Person Name", "user Key"};
    String[] userSheet_columns = {"Mobile Number", "Name", "Email ","Created Time", "App Version", "Device OS", "Updated Time", "Does User Added Address Yet","FCM Token", "Authorization Code", "Key"};

    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
   ListView userCount_listview;


    List<Modal_vendor> vendorList=new ArrayList<>();
    List<Modal_User>  userList=new ArrayList<>();
    List<Modal_Address> addressList = new ArrayList<>();

    List<String> mergedMobileNoList_StringArray =new ArrayList<>();
    HashMap<String,  HashMap<String,  List<Modal_MergedUser_AddressDetails>>> vendorFilteredMobileNo_hashmap = new HashMap();

    HashMap<String,  List<Modal_MergedUser_AddressDetails>> mergedMobileNo_hashmap = new HashMap();
    List<String> vendorFilteredMobileNoList_StringArray =new ArrayList<>();
    LinearLayout datewise_Userdetails_ParentLayout;
    LinearLayout allusersScreenParentLayout,searchlayout;

    TextView getUserDataVelachery , getUserData_mkproteins;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_user_details_report);

        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        switchfor_singleDayDump = findViewById(R.id.switchfor_singleDayDump);
        daysCountSpinner_layout = findViewById(R.id.daysCountSpinner_layout);
        orderinstruction = findViewById(R.id.orderinstruction);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        generateDataInstruction = findViewById(R.id.generateDataInstruction);
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
        usercreated_withaddress_textview =  findViewById(R.id.usercreated_withaddress_textview);
        totalAccountCreatedtextview =  findViewById(R.id.totalAccountCreatedtextview);
        orderinstruction.setVisibility(View.GONE);
        mobile_nameofFacility_Textview.setText(vendorname);
        daysCountSpinner_layout.setVisibility(View.GONE);
        no_of_users_velachery_textview = findViewById(R.id.no_of_users_velachery_textview);
        no_of_users_hastinapuram_textview=  findViewById(R.id.no_of_users_hastinapuram_textview);
        generate_viewVelacherySheet  = findViewById(R.id.generate_viewVelacherySheet);
        generate_viewHastinapuramSheet = findViewById(R.id.generate_viewHastinapuramSheet);
        generate_shareHastinapuramSheet = findViewById(R.id.generate_shareHastinapuramSheet);
        generate_shareVelacherySheet = findViewById(R.id.generate_shareVelacherySheet);
        datewise_Userdetails_ParentLayout = findViewById(R.id.datewise_Userdetails_ParentLayout);
        userCount_listview = findViewById(R.id.userCount_listview);
        fetchUserData_button = findViewById(R.id.fetchUserData_button);
        userWithAddressViewSheet = findViewById(R.id.userWithAddressViewSheet);
        userWithAddressShareSheet = findViewById(R.id.userWithAddressShareSheet);
        datewise_viewOnlyUserDetails = findViewById(R.id.datewise_viewOnlyUserDetails);
        datewise_shareOnlyUserDetails = findViewById(R.id.datewise_shareOnlyUserDetails);
        allusersScreenParentLayout = findViewById(R.id.allusersScreenParentLayout);
        searchlayout = findViewById(R.id.searchlayout);
        totalAccountCreatedLabel = findViewById(R.id.totalAccountCreatedLabel);
        usercreated_withaddress_Label = findViewById(R.id.usercreated_withaddress_Label);
        no_of_users_hastinapuram_Label = findViewById(R.id.no_of_users_hastinapuram_Label);
        no_of_users_velachery_Label  = findViewById(R.id.no_of_users_velachery_Label);
        getUserDataVelachery = findViewById(R.id.getUserDataVelachery);
        getUserData_mkproteins = findViewById(R.id.getUserData_mkproteins);


        try {

            switchfor_singleDayDump.setChecked(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", ""));
            vendorname = (shared.getString("VendorName", ""));
            getVendorItemFromSharedPreferences();

            try {
                ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
                screenInches = screenSizeOfTheDevice.getDisplaySize(GenerateUserDetailsReport .this);
              //  Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
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


        }

        catch (Exception e){
            e.printStackTrace();
        }


        try{
            TodaysDate = getDatewithNameoftheDay();
            PreviousDateString = getDatewithNameofthePreviousDay();
            todatestring = getDatewithNameoftheDay();

            dateSelector_text.setText(TodaysDate);
            fromdateSelector_text.setText(TodaysDate);
            todateSelector_text.setText(TodaysDate);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        getUserData_mkproteins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 userList=new ArrayList<>();
                 addressList = new ArrayList<>();

                 mergedMobileNoList_StringArray =new ArrayList<>();
                 vendorFilteredMobileNo_hashmap = new HashMap();

                 mergedMobileNo_hashmap = new HashMap();
                 vendorFilteredMobileNoList_StringArray =new ArrayList<>();

                fromdatestring = fromdateSelector_text.getText().toString();
                todatestring = todateSelector_text.getText().toString();
                getUserDetailsUsingDate(fromdatestring, "vendor_4", todatestring,true);
            }
        });


        getUserDataVelachery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                userList=new ArrayList<>();
                addressList = new ArrayList<>();

                mergedMobileNoList_StringArray =new ArrayList<>();
                vendorFilteredMobileNo_hashmap = new HashMap();

                mergedMobileNo_hashmap = new HashMap();
                vendorFilteredMobileNoList_StringArray =new ArrayList<>();

                fromdatestring = fromdateSelector_text.getText().toString();
                todatestring = todateSelector_text.getText().toString();
                getUserDetailsUsingDate(fromdatestring, "vendor_3", todatestring,true);
            }
        });



        switchfor_singleDayDump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked){
                    generateDataInstruction.setText("#Note :To Get Datewise Users Details . Turn On the above switch");
                    allusersScreenParentLayout .setVisibility(View.VISIBLE);
                    searchlayout.setVisibility(View.GONE);
                    totalAccountCreatedLabel .setText("Total User Account Created: ");
                    usercreated_withaddress_Label.setText("Total Users Added Address : ");
                    no_of_users_hastinapuram_Label .setText("Total Users Added \nHasthinapuram Address: ");
                    no_of_users_velachery_Label .setText("Total Users Added \nVelachery Address: ");
                    userList.clear();
                    addressList.clear();
                    mergedMobileNoList_StringArray.clear();
                    vendorFilteredMobileNo_hashmap.clear();
                    mergedMobileNo_hashmap.clear();
                    vendorFilteredMobileNoList_StringArray.clear();
                    usercreated_withaddress_textview.setText("0");
                    no_of_users_hastinapuram_textview.setText("0");
                    no_of_users_velachery_textview.setText("0");
                    totalAccountCreatedtextview.setText("0");


                }
                else{
                    userList.clear();
                    addressList.clear();
                    mergedMobileNoList_StringArray.clear();
                    vendorFilteredMobileNo_hashmap.clear();
                    mergedMobileNo_hashmap.clear();
                    vendorFilteredMobileNoList_StringArray.clear();
                    usercreated_withaddress_textview.setText("0");
                    no_of_users_hastinapuram_textview.setText("0");
                    no_of_users_velachery_textview.setText("0");
                    totalAccountCreatedtextview.setText("0");
                    searchlayout.setVisibility(View.VISIBLE);
                    generateDataInstruction.setText("#Note  : To Get Total Users Details for All Vendors, Turn oFF the Above Switch");
                    allusersScreenParentLayout.setVisibility(View.GONE);
                    totalAccountCreatedLabel .setText("Datewise User Account Created: ");
                    usercreated_withaddress_Label.setText("Datewise Users Added Address : ");
                    no_of_users_hastinapuram_Label .setText("Datewise Users Added \nHasthinapuram Address: ");
                    no_of_users_velachery_Label .setText("Datewise Users Added \nVelachery Address: ");
                }
            }
        });
        datewise_shareOnlyUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingAccess_toGenerateSheet("nil","share","onlyUser","UserDetailsOnly");

            }
        });
        datewise_viewOnlyUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingAccess_toGenerateSheet("nil","view","onlyUser","UserDetailsOnly");

            }
        });


        userWithAddressViewSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingAccess_toGenerateSheet("nil","view","mergedArray","UserDetails_withAddress");



            }
        });


        userWithAddressShareSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingAccess_toGenerateSheet("nil","share","mergedArray","UserDetails_withAddress");



            }
        });

        fetchUserData_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userList.clear();
                addressList.clear();
                mergedMobileNoList_StringArray.clear();
                vendorFilteredMobileNo_hashmap.clear();
                mergedMobileNo_hashmap.clear();
                vendorFilteredMobileNoList_StringArray.clear();
                usercreated_withaddress_textview.setText("0");
                no_of_users_hastinapuram_textview.setText("0");
                no_of_users_velachery_textview.setText("0");
                totalAccountCreatedtextview.setText("0");
               // getUserDetailsUsingDate(todatestring, vendorKey, todatestring,false);

            }
        });


        generate_shareVelacherySheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar(true);
                if(vendorFilteredMobileNo_hashmap.size()>0){
                    if(checkIfFilteredHashmapHavethisVendorkey("vendor_3")){
                        gettingAccess_toGenerateSheet("vendor_3","share","vendorFiltered","UserDetails_VelacheryAddress");
                    }
                    else{
                        showProgressBar(false);
                        Toast.makeText(GenerateUserDetailsReport.this, "no vendor_3 in hashmap", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showProgressBar(false);
                    Toast.makeText(GenerateUserDetailsReport.this, "no data in hashmap", Toast.LENGTH_SHORT).show();

                }
            }
        });


        generate_viewVelacherySheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar(true);
                if(vendorFilteredMobileNo_hashmap.size()>0){
                    if(checkIfFilteredHashmapHavethisVendorkey("vendor_3")){
                        gettingAccess_toGenerateSheet("vendor_3","view", "vendorFiltered","UserDetails_VelacheryAddress");
                    }
                    else{
                        showProgressBar(false);
                        Toast.makeText(GenerateUserDetailsReport.this, "no vendor_3 in hashmap", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showProgressBar(false);
                    Toast.makeText(GenerateUserDetailsReport.this, "no data in hashmap", Toast.LENGTH_SHORT).show();

                }
            }
        });

        generate_shareHastinapuramSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar(true);
                if(vendorFilteredMobileNo_hashmap.size()>0){
                    if(checkIfFilteredHashmapHavethisVendorkey("vendor_1")){
                        gettingAccess_toGenerateSheet("vendor_1","share", "vendorFiltered","UserDetails_HastinapuramAddress");
                    }
                    else{
                        showProgressBar(false);
                        Toast.makeText(GenerateUserDetailsReport.this, "no vendor_1 in hashmap", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showProgressBar(false);
                    Toast.makeText(GenerateUserDetailsReport.this, "no data in hashmap", Toast.LENGTH_SHORT).show();

                }
            }
        });

        generate_viewHastinapuramSheet .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar(true);
                if(vendorFilteredMobileNo_hashmap.size()>0){
                    if(checkIfFilteredHashmapHavethisVendorkey("vendor_1")){
                        gettingAccess_toGenerateSheet("vendor_1","view", "vendorFiltered","UserDetails_HastinapuramAddress");
                    }
                    else{
                        showProgressBar(false);
                        Toast.makeText(GenerateUserDetailsReport.this, "no vendor_1 in hashmap", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    showProgressBar(false);
                    Toast.makeText(GenerateUserDetailsReport.this, "no data in hashmap", Toast.LENGTH_SHORT).show();

                }
            }
        });

        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showProgressBar(true);
                if(switchfor_singleDayDump.isChecked()) {
                    todatestring = dateSelector_text.getText().toString();
                    fromdatestring = todatestring;
                    PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);


                    isSearchButtonClicked = false;
                    userList.clear();
                    addressList.clear();
                    mergedMobileNoList_StringArray.clear();
                    vendorFilteredMobileNo_hashmap.clear();
                    mergedMobileNo_hashmap.clear();
                    vendorFilteredMobileNoList_StringArray.clear();
                    usercreated_withaddress_textview.setText("0");
                    no_of_users_hastinapuram_textview.setText("0");
                    no_of_users_velachery_textview.setText("0");
                    totalAccountCreatedtextview.setText("0");

                    getUserDetailsUsingDate(todatestring, vendorKey, todatestring,true);
                }
                else{
                    todatestring = todateSelector_text.getText().toString();
                    fromdatestring = fromdateSelector_text.getText().toString();;
                    PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);


                    isSearchButtonClicked = false;
                    userList.clear();
                    addressList.clear();
                    mergedMobileNoList_StringArray.clear();
                    vendorFilteredMobileNo_hashmap.clear();
                    mergedMobileNo_hashmap.clear();
                    vendorFilteredMobileNoList_StringArray.clear();
                    usercreated_withaddress_textview.setText("0");
                    no_of_users_hastinapuram_textview.setText("0");
                    no_of_users_velachery_textview.setText("0");
                    totalAccountCreatedtextview.setText("0");
                    getUserDetailsUsingDate(fromdatestring, vendorKey, todatestring,true);
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

            //    DisplayOrderListDatainListView( ordersList);
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*  if(ordersList.size()>0) {
                    int textlength = mobile_search_barEditText.getText().toString().length();
                    isSearchButtonClicked = true;

                    showKeyboard(mobile_search_barEditText);
                    showSearchBarEditText();
                }
                else{
                   Toast.makeText(GenerateUserDetailsReport.this, "Please Select Date First", Toast.LENGTH_SHORT).show();

                }

                */
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
                Toast.makeText(GenerateUserDetailsReport.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
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






    }

    private void gettingAccess_toGenerateSheet(String vendorKey, String functiontodowithSheet, String calledFrom, String sheetName) {
        try {
            wb = new HSSFWorkbook();
            //Now we are creating sheet

        } catch (Exception e) {
            e.printStackTrace();
        }
            if (SDK_INT >= Build.VERSION_CODES.R) {

                if(Environment.isExternalStorageManager()){
                    try {
                        showProgressBar(true);
                        if (calledFrom.equals("vendorFiltered")){
                            AddDatatoVendorwiseExcelSheet(vendorFilteredMobileNo_hashmap, sheetName, vendorKey, functiontodowithSheet);
                        }
                        else if(calledFrom.equals("mergedArray")){
                            AddDatatoMergedData_ExcelSheet(mergedMobileNo_hashmap,mergedMobileNoList_StringArray, sheetName, functiontodowithSheet);

                        }
                        else  if(calledFrom.equals("onlyUser")){
                            AddDataOnlyUser_ExcelSheet(userList, sheetName, functiontodowithSheet);

                        }

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

            }
            else {


                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(GenerateUserDetailsReport.this, WRITE_EXTERNAL_STORAGE);
                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(GenerateUserDetailsReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    showProgressBar(true);
                    try {
                        if (calledFrom.equals("vendorFiltered")) {

                            AddDatatoVendorwiseExcelSheet(vendorFilteredMobileNo_hashmap, sheetName, vendorKey, functiontodowithSheet);
                        }
                        else if(calledFrom.equals("mergedArray")) {
                            AddDatatoMergedData_ExcelSheet(mergedMobileNo_hashmap,mergedMobileNoList_StringArray, sheetName, functiontodowithSheet);

                        }
                        else  if(calledFrom.equals("onlyUser")){
                            AddDataOnlyUser_ExcelSheet(userList, sheetName, functiontodowithSheet);

                        }
                        } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                }
            }




    }

    private void AddDataOnlyUser_ExcelSheet(List<Modal_User> userList, String sheetname, String functiontodowithSheet) {
        try {
            sheet = wb.createSheet(sheetname);
            int rowNum = 1;
            Cell headercell = null;

            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(HSSFColor.RED.index);

            CellStyle headerCellStyle = wb.createCellStyle();
            headerCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);


            org.apache.poi.ss.usermodel.Font contentFont = wb.createFont();
            contentFont.setBold(false);
            contentFont.setFontHeightInPoints((short) 10);
            contentFont.setColor(HSSFColor.BLACK.index);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFont(contentFont);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);


            //Now column and row
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < userSheet_columns.length; i++) {
                headercell = headerRow.createCell(i);
                headercell.setCellValue(userSheet_columns[i]);
                headercell.setCellStyle(headerCellStyle);
            }
            // String[] userSheet_columns = {"Mobile Number", "Name", "Email ","Created Time", "App Version", "Device OS", "Updated Time", "User Added Address","FCM Token", "Authorization Code", "Key"};

            for (int iterator1 = 0; iterator1 < userList.size(); iterator1++) {
                Modal_User modal_user = userList.get(iterator1);
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(String.valueOf(modal_user.getMobileno()));
                row.createCell(1).setCellValue(String.valueOf(modal_user.getName()));
                row.createCell(2).setCellValue(String.valueOf(modal_user.getEmail()));
                row.createCell(3).setCellValue(String.valueOf(modal_user.getCreatedtime()));
                row.createCell(4).setCellValue(String.valueOf(modal_user.getAppversion()));
                row.createCell(5).setCellValue(String.valueOf(modal_user.getDeviceos()));
                row.createCell(6).setCellValue(String.valueOf(modal_user.getUpdatedtime()));
                row.createCell(7).setCellValue(String.valueOf(modal_user.getIsUserAddedAddress()));
                row.createCell(8).setCellValue(String.valueOf(modal_user.getFcmtoken()));
                row.createCell(9).setCellValue(String.valueOf(modal_user.getAuthorizationcode()));
                row.createCell(10).setCellValue(String.valueOf(modal_user.getKey()));


                sheet.setColumnWidth(0, (10 * 500));
                sheet.setColumnWidth(1, (10 * 500));
                sheet.setColumnWidth(2, (10 * 600));
                sheet.setColumnWidth(3, (10 * 800));
                sheet.setColumnWidth(4, (10 * 500));
                sheet.setColumnWidth(5, (10 * 500));
                sheet.setColumnWidth(6, (10 * 800));
                sheet.setColumnWidth(7, (10 * 800));
                sheet.setColumnWidth(8, (10 * 600));
                sheet.setColumnWidth(9, (10 * 500));
                sheet.setColumnWidth(10, (10 * 600));



                row.getCell(0).setCellStyle(cellStyle);
                row.getCell(1).setCellStyle(cellStyle);
                row.getCell(2).setCellStyle(cellStyle);
                row.getCell(3).setCellStyle(cellStyle);
                row.getCell(4).setCellStyle(cellStyle);
                row.getCell(5).setCellStyle(cellStyle);
                row.getCell(6).setCellStyle(cellStyle);
                row.getCell(7).setCellStyle(cellStyle);
                row.getCell(8).setCellStyle(cellStyle);
                row.getCell(9).setCellStyle(cellStyle);
                row.getCell(10).setCellStyle(cellStyle);



                if (iterator1 == (userList.size() - 1)) {
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + userList.size());
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);
                    Toast.makeText(this, "Only user", Toast.LENGTH_SHORT).show();

                    GenerateExcelSheet(functiontodowithSheet);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
            showProgressBar(false);

            Toast.makeText(this, "There is a Error in generating the sheet", Toast.LENGTH_SHORT).show();
        }


        }

    private void AddDatatoMergedData_ExcelSheet(HashMap<String, List<Modal_MergedUser_AddressDetails>> mergedMobileNo_hashmap, List<String> mergedMobileNoList_stringArray, String sheetname, String functiontodowithSheet) {
        try{

        sheet = wb.createSheet(sheetname );
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

            int rowNum = 1;
           // int user_count =0;
        for(int iterator1 =0 ;iterator1<mergedMobileNoList_stringArray.size();iterator1++){
            List<Modal_MergedUser_AddressDetails> mobilenoArrayList = new ArrayList<>();
           // user_count = iterator1+1;
            String mobile_no = mergedMobileNoList_stringArray.get(iterator1);
            if(mergedMobileNo_hashmap.containsKey(mobile_no)) {
                mobilenoArrayList = mergedMobileNo_hashmap.get(mobile_no);


                //Now column and row
                Row headerRow = sheet.createRow(0);

                for (int i = 0; i < columns.length; i++) {
                    headercell = headerRow.createCell(i);
                    headercell.setCellValue(columns[i]);
                    headercell.setCellStyle(headerCellStyle);
                }

                for (int iterator = 0; iterator < mobilenoArrayList.size(); iterator++) {
                    Modal_MergedUser_AddressDetails modal_mergedUser_addressDetails = mobilenoArrayList.get(iterator);
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  itemRow: " + String.valueOf(modal_mergedUser_addressDetails.getMobileno_userDetail()));
                    Row row = sheet.createRow(rowNum++);
                   // row.createCell(0).setCellValue(String.valueOf(user_count));

                    row.createCell(0).setCellValue(modal_mergedUser_addressDetails.getMobileno_userDetail());
                    row.createCell(1).setCellValue(modal_mergedUser_addressDetails.getName_userDetail());
                    row.createCell(2).setCellValue(modal_mergedUser_addressDetails.getAppversion_userDetail());
                    row.createCell(3).setCellValue(modal_mergedUser_addressDetails.getDeviceos_userDetail());
                    row.createCell(4).setCellValue(modal_mergedUser_addressDetails.getCreatedtime_userDetail());
                    row.createCell(5).setCellValue(modal_mergedUser_addressDetails.getUpdatedtime_userDetail());

                    row.createCell(6).setCellValue(modal_mergedUser_addressDetails.getAddresstype_addressDetail());
                    row.createCell(7).setCellValue(modal_mergedUser_addressDetails.getAddressline1_addressDetail() + " " + modal_mergedUser_addressDetails.getAddressline2_addressDetail() + " - " + modal_mergedUser_addressDetails.getPincode_addressDetail());
                    row.createCell(8).setCellValue(modal_mergedUser_addressDetails.getLandmark_addressDetail());
                    row.createCell(9).setCellValue(modal_mergedUser_addressDetails.getLocationlat_addressDetail());
                    row.createCell(10).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getLocationlong_addressDetail()));

                    row.createCell(11).setCellValue(modal_mergedUser_addressDetails.getDeliverydistance_addressDetail());

                    row.createCell(12).setCellValue(modal_mergedUser_addressDetails.getVendorname_addressDetail());
                    row.createCell(13).setCellValue(modal_mergedUser_addressDetails.getVendorkey_addressDetail());
                    row.createCell(14).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getEmail_userDetail()));
                    row.createCell(15).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getContactpersonmobileno_addressDetail()));
                    row.createCell(16).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getContactpersonname_addressDetail()));
                    row.createCell(17).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getKey_userDetail()));

                    sheet.setColumnWidth(0, (10 * 500));
                    sheet.setColumnWidth(1, (10 * 500));
                    sheet.setColumnWidth(2, (10 * 600));
                    sheet.setColumnWidth(3, (10 * 600));
                    sheet.setColumnWidth(4, (10 * 600));
                    sheet.setColumnWidth(5, (10 * 600));
                    sheet.setColumnWidth(6, (10 * 600));
                    sheet.setColumnWidth(7, (20 * 1000));
                    sheet.setColumnWidth(8, (10 * 500));
                    sheet.setColumnWidth(9, (10 * 500));
                    sheet.setColumnWidth(10, (10 * 500));
                    sheet.setColumnWidth(11, (10 * 500));
                    sheet.setColumnWidth(12, (10 * 600));
                    sheet.setColumnWidth(13, (10 * 500));
                    sheet.setColumnWidth(14, (10 * 600));
                    sheet.setColumnWidth(15, (10 * 600));
                    sheet.setColumnWidth(16, (10 * 600));
                    sheet.setColumnWidth(17, (20 * 600));

                }

            }
            if (iterator1 == (mergedMobileNoList_stringArray.size()-1)) {
                Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + mobilenoArrayList.size());
                Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);
                Toast.makeText(this, "Merged Sheet", Toast.LENGTH_SHORT).show();

                GenerateExcelSheet(functiontodowithSheet);
            }
        }




        }
        catch (Exception e){
            showProgressBar(false);
            e.printStackTrace();
            Toast.makeText(this, "There is a Error in generating the sheet", Toast.LENGTH_SHORT).show();
        }

    }

    private void AddDatatoVendorwiseExcelSheet(HashMap<String, HashMap<String,  List<Modal_MergedUser_AddressDetails>>> vendorFilteredMobileNo_hashmap, String sheetname, String vendorKey, String functiontodowithSheet) {

        try{
        Log.d(Constants.TAG, "prepareDataForExcelSheet type  addData: " + sheetname);

        sheet = wb.createSheet(sheetname + " _ " + vendorKey);
        int rowNum = 1;
        int count = 1;
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

        if (vendorFilteredMobileNo_hashmap.containsKey(vendorKey)) {
            List<Modal_MergedUser_AddressDetails> mobilenoArrayList = new ArrayList<>();
            HashMap<String, List<Modal_MergedUser_AddressDetails>> innerHashmap_2 = new HashMap<>();
            Set<String> mobilenoArrayList_setString;

            innerHashmap_2 = vendorFilteredMobileNo_hashmap.get(vendorKey);
            mobilenoArrayList_setString = innerHashmap_2.keySet();
           // int user_count =0;

            for (Map.Entry<String, List<Modal_MergedUser_AddressDetails>> entry : innerHashmap_2.entrySet()) {
                mobilenoArrayList = entry.getValue();
                count++;



            //Now column and row
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columns.length; i++) {
                headercell = headerRow.createCell(i);
                headercell.setCellValue(columns[i]);
                headercell.setCellStyle(headerCellStyle);
            }

            for (int iterator = 0; iterator < mobilenoArrayList.size(); iterator++) {
                Modal_MergedUser_AddressDetails modal_mergedUser_addressDetails = mobilenoArrayList.get(iterator);
                Log.d(Constants.TAG, "prepareDataForExcelSheet type  itemRow: " + String.valueOf(modal_mergedUser_addressDetails.getMobileno_userDetail()));
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(modal_mergedUser_addressDetails.getMobileno_userDetail());
                row.createCell(1).setCellValue(modal_mergedUser_addressDetails.getName_userDetail());
                row.createCell(2).setCellValue(modal_mergedUser_addressDetails.getAppversion_userDetail());
                row.createCell(3).setCellValue(modal_mergedUser_addressDetails.getDeviceos_userDetail());
                row.createCell(4).setCellValue(modal_mergedUser_addressDetails.getCreatedtime_userDetail());
                row.createCell(5).setCellValue(modal_mergedUser_addressDetails.getUpdatedtime_userDetail());

                row.createCell(6).setCellValue(modal_mergedUser_addressDetails.getAddresstype_addressDetail());
                row.createCell(7).setCellValue(modal_mergedUser_addressDetails.getAddressline1_addressDetail() + " " + modal_mergedUser_addressDetails.getAddressline2_addressDetail() + " - " + modal_mergedUser_addressDetails.getPincode_addressDetail());
                row.createCell(8).setCellValue(modal_mergedUser_addressDetails.getLandmark_addressDetail());
                row.createCell(9).setCellValue(modal_mergedUser_addressDetails.getLocationlat_addressDetail());
                row.createCell(10).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getLocationlong_addressDetail()));

                row.createCell(11).setCellValue(modal_mergedUser_addressDetails.getDeliverydistance_addressDetail());

                row.createCell(12).setCellValue(modal_mergedUser_addressDetails.getVendorname_addressDetail());
                row.createCell(13).setCellValue(modal_mergedUser_addressDetails.getVendorkey_addressDetail());
                row.createCell(14).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getEmail_userDetail()));
                row.createCell(15).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getContactpersonmobileno_addressDetail()));
                row.createCell(16).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getContactpersonname_addressDetail()));
                row.createCell(17).setCellValue(String.valueOf(modal_mergedUser_addressDetails.getKey_userDetail()));

                sheet.setColumnWidth(0, (10 * 500));
                sheet.setColumnWidth(1, (10 * 500));
                sheet.setColumnWidth(2, (10 * 600));
                sheet.setColumnWidth(3, (10 * 600));
                sheet.setColumnWidth(4, (10 * 600));
                sheet.setColumnWidth(5, (10 * 600));
                sheet.setColumnWidth(6, (10 * 600));
                sheet.setColumnWidth(7, (20 * 1000));
                sheet.setColumnWidth(8, (10 * 500));
                sheet.setColumnWidth(9, (10 * 500));
                sheet.setColumnWidth(10, (10 * 500));
                sheet.setColumnWidth(11, (10 * 500));
                sheet.setColumnWidth(12, (10 * 600));
                sheet.setColumnWidth(13, (10 * 500));
                sheet.setColumnWidth(14, (10 * 600));
                sheet.setColumnWidth(15, (10 * 600));
                sheet.setColumnWidth(16, (10 * 600));
                sheet.setColumnWidth(17, (20 * 600));

                if (count > mobilenoArrayList_setString.size()) {
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + mobilenoArrayList.size());
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);
                    Toast.makeText(this, "Vendorwise sheet", Toast.LENGTH_SHORT).show();

                    GenerateExcelSheet(functiontodowithSheet);
                }
            }
        }
    }
    }
    catch (Exception e){
        e.printStackTrace();
        showProgressBar(false);

        Toast.makeText(this, "There is a Error in generating the sheet", Toast.LENGTH_SHORT).show();
    }

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


    private void GenerateExcelSheet(String functiontodowithSheet) {
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/User Details Dump/";
        File dir = new File(path);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");

        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "UserDetailsDump"+ System.currentTimeMillis()  +".xls");


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
        if(functiontodowithSheet.equals("share")) {
            try {
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/xls");
                share.putExtra(Intent.EXTRA_STREAM, pdfUri);
                startActivity(Intent.createChooser(share, "Share"));
            }
            catch (Exception e){
                Toast.makeText(this, "Intent Cannot be passed ", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }
        else{
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setDataAndType(pdfUri, "application/vnd.ms-excel");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(this, "Application not found", Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Toast.makeText(this, "Intent Cannot be passed ", Toast.LENGTH_SHORT).show();

                e.printStackTrace();
            }
        }
    }

    private void GetAddressTable(String vendorKey, boolean isDatewise) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getAllAddressUsingVendorkeywithPagenation+"?vendorkey="+vendorKey, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);


                        //Log.d(Constants.TAG, "Response: " + response);
                        String jsonString = response.toString();
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray JArray = jsonObject.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (; i1 < (arrayLength); i1++) {
                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_Address modalAddress = new Modal_Address();


                                if (json.has("contactpersonmobileno")) {
                                    modalAddress.contactpersonmobileno = String.valueOf(json.get("contactpersonmobileno"));
                                } else {
                                    modalAddress.contactpersonmobileno = "";
                                }




                                if (json.has("addresstype")) {
                                    modalAddress.addresstype = String.valueOf(json.get("addresstype"));
                                } else {
                                    modalAddress.addresstype = "";
                                }




                                if (json.has("pincode")) {
                                    modalAddress.pincode = String.valueOf(json.get("pincode"));
                                } else {
                                    modalAddress.pincode = "";
                                }



                                if (json.has("vendorname")) {
                                    modalAddress.vendorname = String.valueOf(json.get("vendorname"));
                                } else {
                                    modalAddress.vendorname = "";
                                }


                                if (json.has("vendorkey")) {
                                    modalAddress.vendorkey = String.valueOf(json.get("vendorkey"));
                                } else {
                                    modalAddress.vendorkey = "";
                                }


                                if (json.has("locationlong")) {
                                    modalAddress.locationlong = String.valueOf(json.get("locationlong"));
                                } else {
                                    modalAddress.locationlong = "";
                                }



                                if (json.has("userkey")) {
                                    modalAddress.userkey = String.valueOf(json.get("userkey"));
                                } else {
                                    modalAddress.userkey = "";
                                }



                                if (json.has("locationlat")) {
                                    modalAddress.locationlat = String.valueOf(json.get("locationlat"));
                                } else {
                                    modalAddress.locationlat = "";
                                }




                                if (json.has("deliverydistance")) {
                                    modalAddress.deliverydistance = String.valueOf(json.get("deliverydistance"));
                                } else {
                                    modalAddress.deliverydistance = "";
                                }




                                if (json.has("vendorkey")) {
                                    modalAddress.vendorkey = String.valueOf(json.get("vendorkey"));
                                } else {
                                    modalAddress.vendorkey = "";
                                }

                                if (json.has("landmark")) {
                                    modalAddress.landmark = String.valueOf(json.get("landmark"));
                                } else {
                                    modalAddress.landmark = "";
                                }


                                if (json.has("key")) {
                                    modalAddress.key = String.valueOf(json.get("key"));
                                } else {
                                    modalAddress.key = "";
                                }


                                if (json.has("addressline1")) {
                                    modalAddress.addressline1 = String.valueOf(json.get("addressline1"));
                                } else {
                                    modalAddress.addressline1 = "";
                                }


                                if (json.has("addressline2")) {
                                    modalAddress.addressline2 = String.valueOf(json.get("addressline2"));
                                } else {
                                    modalAddress.addressline2 = "";
                                }


                                if (json.has("contactpersonname")) {
                                    modalAddress.contactpersonname = String.valueOf(json.get("contactpersonname"));
                                } else {
                                    modalAddress.contactpersonname = "";
                                }



                                addressList.add(modalAddress);

                            }


                            if (addressList.size() > 0 && userList.size() > 0) {
                                MergeUserTableAndAddressTable(isDatewise);
                            }
                            else{
                                showProgressBar(false);

                                Toast.makeText(GenerateUserDetailsReport.this,"There is no data",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            showProgressBar(false);

                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                showProgressBar(false);

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                error.printStackTrace();
            }
        }) {


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

    private void MergeUserTableAndAddressTable(boolean isDatewise) {
    boolean isAddressIdentified = false;
        vendorFilteredMobileNo_hashmap.clear();
        mergedMobileNo_hashmap.clear();
      for(int userIterator =0 ; userIterator<userList.size();userIterator++) {
          Modal_User modal_user = userList.get(userIterator);
          String key_userList = "";
          List<Modal_MergedUser_AddressDetails> mergedMobileNoList_array =new ArrayList<>();
          HashMap<String,  List<Modal_MergedUser_AddressDetails>> innerHashmap_mobilenumber = new HashMap<>();

          String mobileNumber_userList = "";
          try {
              isAddressIdentified =false;
         //     mergedMobileNoList_array.clear();
          } catch (Exception e) {
              e.printStackTrace();
          }
          try {
              mobileNumber_userList = modal_user.getMobileno().toString();


          } catch (Exception e) {
              e.printStackTrace();
          }
          if (!mergedMobileNoList_StringArray.contains(mobileNumber_userList)) {

              try {
                  key_userList = modal_user.getKey().toString();
              } catch (Exception e) {
                  key_userList = "";
                  e.printStackTrace();
              }
              for (int addressIterator = 0; addressIterator < addressList.size(); addressIterator++) {
                  Modal_Address modal_address = addressList.get(addressIterator);
                  List<Modal_MergedUser_AddressDetails> vendorFilteredMobileNoList_array = new ArrayList<>();

                  isAddressIdentified = false;
                  String userkey_addressList = "";
                  try {
                      userkey_addressList = modal_address.getUserkey().toString();
                  } catch (Exception e) {
                      userkey_addressList = "";
                      e.printStackTrace();
                  }

                  if (!userkey_addressList.equals("")) {
                      if (key_userList.equals(userkey_addressList)) {
                          if (!mergedMobileNoList_StringArray.contains(mobileNumber_userList)) {

                              mergedMobileNoList_StringArray.add(mobileNumber_userList);
                          }
                          modal_user.setIsUserAddedAddress("YES");
                          try {

                              Modal_MergedUser_AddressDetails modal_mergedUser_addressDetails = new Modal_MergedUser_AddressDetails();

                              //userDetails
                              try {
                                  modal_mergedUser_addressDetails.setKey_userDetail(modal_user.getKey().toString());
                                  modal_mergedUser_addressDetails.setAuthorizationcode_userDetail(modal_user.getAuthorizationcode().toString());
                                  modal_mergedUser_addressDetails.setFcmtoken_userDetail(modal_user.getFcmtoken().toString());
                                  modal_mergedUser_addressDetails.setCreatedtime_userDetail(modal_user.getCreatedtime().toString());
                                  modal_mergedUser_addressDetails.setMobileno_userDetail(modal_user.getMobileno().toString());
                                  modal_mergedUser_addressDetails.setAppversion_userDetail(modal_user.getAppversion().toString());
                                  modal_mergedUser_addressDetails.setDeviceos_userDetail(modal_user.getDeviceos().toString());
                                  modal_mergedUser_addressDetails.setEmail_userDetail(modal_user.getEmail().toString());
                                  modal_mergedUser_addressDetails.setName_userDetail(modal_user.getName().toString());
                                  modal_mergedUser_addressDetails.setUpdatedtime_userDetail(modal_user.getUpdatedtime().toString());

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }

                              //addressDetails
                              try {
                                  modal_mergedUser_addressDetails.setContactpersonname_addressDetail(modal_address.getContactpersonname().toString());
                                  modal_mergedUser_addressDetails.setContactpersonmobileno_addressDetail(modal_address.getContactpersonmobileno().toString());
                                  modal_mergedUser_addressDetails.setAddresstype_addressDetail(modal_address.getAddresstype().toString());
                                  modal_mergedUser_addressDetails.setLandmark_addressDetail(modal_address.getLandmark().toString());
                                  modal_mergedUser_addressDetails.setPincode_addressDetail(modal_address.getPincode().toString());
                                  modal_mergedUser_addressDetails.setLocationlat_addressDetail(modal_address.getLocationlat().toString());
                                  modal_mergedUser_addressDetails.setLocationlong_addressDetail(modal_address.getLocationlong().toString());
                                  modal_mergedUser_addressDetails.setVendorkey_addressDetail(modal_address.getVendorkey().toString());
                                  modal_mergedUser_addressDetails.setVendorname_addressDetail(modal_address.getVendorname().toString());
                                  modal_mergedUser_addressDetails.setKey_addressDetail(modal_address.getKey().toString());
                                  modal_mergedUser_addressDetails.setDeliverydistance_addressDetail(modal_address.getDeliverydistance().toString());
                                  modal_mergedUser_addressDetails.setAddressline1_addressDetail(modal_address.getAddressline1().toString());
                                  modal_mergedUser_addressDetails.setAddressline2_addressDetail(modal_address.getAddressline2().toString());
                                  modal_mergedUser_addressDetails.setUserkey_addressDetail(modal_address.getUserkey().toString());

                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                              try {
                                  //if(!vendorFilteredMobileNoList_StringArray.contains(mobileNumber_userList)){
                                  //     vendorFilteredMobileNoList_StringArray.add(mobileNumber_userList);
                                  String vendorkey = (modal_address.getVendorkey().toString());
                                  if (!vendorkey.equals("vendor_2")) {
                                      boolean isVendorkeyAddedinHashmap = checkIfFilteredHashmapHavethisVendorkey(vendorkey);
                                      if (isVendorkeyAddedinHashmap) {
                                          innerHashmap_mobilenumber = vendorFilteredMobileNo_hashmap.get(vendorkey);
                                          if (Objects.requireNonNull(innerHashmap_mobilenumber).containsKey(mobileNumber_userList)) {
                                              vendorFilteredMobileNoList_array = innerHashmap_mobilenumber.get(mobileNumber_userList);
                                              Objects.requireNonNull(vendorFilteredMobileNoList_array).add(modal_mergedUser_addressDetails);

                                          } else {
                                              vendorFilteredMobileNoList_array.add(modal_mergedUser_addressDetails);
                                              innerHashmap_mobilenumber.put(mobileNumber_userList, vendorFilteredMobileNoList_array);
                                              vendorFilteredMobileNo_hashmap.put(vendorkey, innerHashmap_mobilenumber);

                                          }
                                          vendorFilteredMobileNoList_StringArray.add(mobileNumber_userList);


                                      } else {

                                          vendorFilteredMobileNoList_StringArray.add(mobileNumber_userList);
                                          vendorFilteredMobileNoList_array.add(modal_mergedUser_addressDetails);
                                          innerHashmap_mobilenumber.put(mobileNumber_userList, vendorFilteredMobileNoList_array);

                                          vendorFilteredMobileNo_hashmap.put(vendorkey, innerHashmap_mobilenumber);

                                      }
                                  }

                                  //}
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                              isAddressIdentified = true;
                              mergedMobileNoList_array.add(modal_mergedUser_addressDetails);


                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                          //  }


                      }
                      if (isAddressIdentified) {
                          mergedMobileNo_hashmap.put(mobileNumber_userList, mergedMobileNoList_array);

                      }


                      if ((addressList.size() - addressIterator == 1) && (userList.size() - userIterator == 1)) {
                          showProgressBar(false);
                          usercreated_withaddress_textview.setText(String.valueOf(mergedMobileNo_hashmap.size()));
                          totalAccountCreatedtextview.setText(String.valueOf(userList.size()));
                          try {
                              for (int i = 0; i < vendorList.size(); i++) {
                                  String vendorkeyfromlist = vendorList.get(i).getKey().toString();
                                  if (vendorkeyfromlist.equals("vendor_1")) {
                                      if (checkIfFilteredHashmapHavethisVendorkey(vendorkeyfromlist)) {

                                          List<Modal_MergedUser_AddressDetails> mobilenoArrayList = new ArrayList<>();
                                          HashMap<String, List<Modal_MergedUser_AddressDetails>> innerHashmap_2 = new HashMap<>();

                                          innerHashmap_2 = vendorFilteredMobileNo_hashmap.get(vendorkeyfromlist);
                                          Log.d("Constants.TAG", "Response: " + String.valueOf(innerHashmap_2));

                                          no_of_users_hastinapuram_textview.setText(String.valueOf(innerHashmap_2.size()));
                                      }
                                  } else if (vendorkeyfromlist.equals("vendor_3")) {
                                      if (checkIfFilteredHashmapHavethisVendorkey(vendorkeyfromlist)) {
                                          List<Modal_MergedUser_AddressDetails> mobilenoArrayList = new ArrayList<>();
                                          HashMap<String, List<Modal_MergedUser_AddressDetails>> innerHashmap_2 = new HashMap<>();
                                          innerHashmap_2 = vendorFilteredMobileNo_hashmap.get(vendorkeyfromlist);
                                          no_of_users_velachery_textview.setText(String.valueOf(innerHashmap_2.size()));
                                      }

                                  } else {


                                  }

                              }
                          } catch (Exception e) {
                              e.printStackTrace();
                          }

                          //   orderinstruction.setText(String.valueOf(vendorFilteredMobileNoList_StringArray.size()+" ,   "+String.valueOf(vendorFilteredMobileNo_hashmap.size())));
                          //  orderinstruction.setText(String.format("string array : %dHASHMAP : %d", St ring.valueOf(mergedMobileNoList_StringArray.size()))));
                      }

                  }
              }
          }
      }




    }
    private boolean checkIfFilteredHashmapHavethisVendorkey(String vendorkey) {
        return vendorFilteredMobileNo_hashmap.containsKey(vendorkey);
    }
    private void getUserDetailsUsingDate(String fromdatestring, String vendorKey, String todatestring, boolean isDatewise) {
     String   startDateString_AsNewFormat = convertOldFormatDateintoNewFormat(fromdatestring);
     String   endDateString_AsNewFormat = convertOldFormatDateintoNewFormat(todatestring);


        showProgressBar(true);


        String api ="";
        try {
            if (isDatewise) {
                api = Constants.api_getListofUsersForCreatedTime + startDateString_AsNewFormat  +"&tocreateddate="+endDateString_AsNewFormat;
            } else {
                api = Constants.api_getAllUserswithPagenation;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            api = Constants.api_getAllUserswithPagenation;
        }
        if(api.equals("")){
            api = Constants.api_getAllUserswithPagenation;

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api  ,null,
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
                                    Modal_User modal_user = new Modal_User();
                                    try{
                                        if(json.has("uniquekey")){
                                            modal_user.setKey(String.valueOf(json.get("uniquekey")));
                                        }
                                        else{
                                            modal_user.setKey(String.valueOf(""));
                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setKey(String.valueOf(""));
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("appversion")){
                                            modal_user.setAppversion(String.valueOf(json.get("appversion")));

                                        }
                                        else{
                                            modal_user.setAppversion(String.valueOf(json.get("")));
                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setAppversion(String.valueOf(json.get("")));
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("authorizationcode")){
                                            modal_user.setAuthorizationcode(String.valueOf(json.get("authorizationcode")));

                                        }
                                        else{
                                            modal_user.setAuthorizationcode(String.valueOf(""));
                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setAuthorizationcode(String.valueOf(""));

                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("createdtime")){
                                            modal_user.setCreatedtime(String.valueOf(json.get("createdtime")));

                                        }
                                        else{
                                            modal_user.setCreatedtime(String.valueOf(""));;

                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setCreatedtime(String.valueOf(""));

                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("deviceos")){
                                            modal_user.setDeviceos(String.valueOf(json.get("deviceos")));

                                        }
                                        else{
                                            modal_user.setDeviceos(String.valueOf(""));

                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setDeviceos(String.valueOf(""));

                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("email")){
                                            modal_user.setEmail(String.valueOf(json.get("email")));

                                        }
                                        else{
                                            modal_user.setEmail(String.valueOf(""));

                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setEmail(String.valueOf(""));

                                        e.printStackTrace();
                                    }

                                    try{
                                        if(json.has("fcmtoken")){
                                            modal_user.setFcmtoken(String.valueOf(json.get("fcmtoken")));

                                        }
                                        else{
                                            modal_user.setFcmtoken(String.valueOf(""));

                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setFcmtoken(String.valueOf(""));
                                        e.printStackTrace();
                                    }

                                    try{
                                        if(json.has("mobileno")){
                                            modal_user.setMobileno(String.valueOf(json.get("mobileno")));

                                        }
                                        else{
                                            modal_user.setMobileno(String.valueOf(""));
                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setMobileno(String.valueOf(""));

                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("name")){
                                            modal_user.setName(String.valueOf(json.get("name")));

                                        }
                                        else{
                                            modal_user.setName(String.valueOf(""));

                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setName(String.valueOf(""));

                                        e.printStackTrace();
                                    }

                                    try{
                                        if(json.has("updatedtime")){
                                            modal_user.setUpdatedtime(String.valueOf(json.get("updatedtime")));

                                        }
                                        else{
                                            modal_user.setUpdatedtime(String.valueOf(""));

                                        }
                                    }
                                    catch (Exception e){
                                        modal_user.setUpdatedtime(String.valueOf(""));

                                        e.printStackTrace();
                                    }

                                    userList.add(modal_user);




                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }


                                    if (arrayLength - 1 == i1) {
                                      //  if(todatestring.equals(createdTime)) {

                                            if (userList.size() > 0) {
                                            GetAddressTable(vendorKey,isDatewise);

                                        } else {
                                            Toast.makeText(GenerateUserDetailsReport.this, "No new user account created today ", Toast.LENGTH_SHORT).show();
                                            showProgressBar(false);

                                        }

                                   // }  else{
                                         //  String nextday = getTomorrowsDate(createdTime);

                                         //   calculate_the_dateandgetData(nextday,todatestring);

                                     //   }
                                }



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showProgressBar(false);

                        }


                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);

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
                    try {
                       // AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);

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
                       // AddDatatoExcelSheet(ordersList,"orderDetailsfrom"+fromdatestring);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
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

    private void openFromDatePicker() {

       // spinnerselecteditem = 1 ;
       // spinnerselecteditem_Count=1;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        fromdatepicker = new DatePickerDialog(GenerateUserDetailsReport.this,
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

        c.add(Calendar.MONTH, -3);
        // Toast.makeText(getApplicationContext(), Calendar.DATE, Toast.LENGTH_LONG).show();
        Log.d(Constants.TAG, "Calendar.DATE " + String.valueOf(Calendar.DATE));
        long oneMonthAhead = c.getTimeInMillis();
        datePicker.setMaxDate(System.currentTimeMillis() - 1000);
        datePicker.setMinDate(oneMonthAhead);





        fromdatepicker.show();


    }

    private void openToDatePicker() {



        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        todatepicker = new DatePickerDialog(GenerateUserDetailsReport.this,
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

                            userList.clear();
                            addressList.clear();
                            mergedMobileNoList_StringArray.clear();
                            vendorFilteredMobileNo_hashmap.clear();
                            mergedMobileNo_hashmap.clear();
                            vendorFilteredMobileNoList_StringArray.clear();
                            usercreated_withaddress_textview.setText("0");
                            no_of_users_hastinapuram_textview.setText("0");
                            no_of_users_velachery_textview.setText("0");
                            totalAccountCreatedtextview.setText("0");

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
                            calculate_the_dateandgetData(fromdatestring,todatestring);
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
        SimpleDateFormat sdformat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
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
        c.add(Calendar.MONTH, -3);
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
      //  previousday =  getDatewithNameofthePreviousDayfromSelectedDay2(fromdateString);
        getUserDetailsUsingDate(fromdateString, vendorKey,toDateString,true);

    }


    private void openDatePicker() {

           final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(GenerateUserDetailsReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                          //  ordersList.clear();
                          //  sorted_OrdersList.clear();
                           // array_of_orderId.clear();

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

                            getUserDetailsUsingDate(todatestring, vendorKey, todatestring,true);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

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

    private void getVendorItemFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = getApplicationContext().getSharedPreferences("VendorList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("VendorList", "");
        if (json.isEmpty()) {
            Toast.makeText(getApplicationContext(),"There is something error in vendorlist",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_vendor>>() {
            }.getType();
            vendorList  = gson.fromJson(json, type);
        }

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


    private long getMillisecondsFromDate(String dateString) {
        Calendar calendarr = Calendar.getInstance();



        calendarr.add(Calendar.DATE,-1);



        long milliseconds = calendarr.getTimeInMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

        calendar.add(Calendar.DATE, 6);




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



    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String PreviousdayDay = previousday.format(c1);


        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  PreviousdayDate = df1.format(c1);
        PreviousdayDate = PreviousdayDay+", "+PreviousdayDate;
        // System.out.println("todays Date  " + CurrentDate);
        System.out.println("PreviousdayDate Date  " + PreviousdayDate);


        return PreviousdayDate;
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

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
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }


    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDay = day.format(c);



        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;


        //CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
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
          //  manageOrders_ListView.setVisibility(View.GONE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
        //    manageOrders_ListView.setVisibility(View.VISIBLE);
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