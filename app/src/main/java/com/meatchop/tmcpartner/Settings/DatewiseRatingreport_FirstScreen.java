package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.meatchop.tmcpartner.Constants;
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
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class DatewiseRatingreport_FirstScreen extends AppCompatActivity {
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;
    TextView deliveryrating5_count,deliveryrating4_count,deliveryrating3_count,deliveryrating2_count,deliveryrating1_count,deliveryratingsCount_textview,deliveryRating_textview,quantityRating_textview,quantityratingsCount_textview,quantityrating5_count,quantityrating4_count,quantityrating3_count,quantityrating2_count,quantityrating1_count,rating5_count,rating4_count,rating3_count,rating2_count,rating1_count,ratingsCount_textview,wholeRating_textview,appOrdersCount_textwidget, mobile_nameofFacility_Textview;
    private TextView orderinstruction,dateSelector_text,generateDataInstruction,fromdateSelector_text,todateSelector_text;
    private DatePickerDialog datepicker;
    private DatePickerDialog fromdatepicker;
    private LinearLayout dateSelectorLayout;
    private LinearLayout daysCountSpinner_layout;
    private LinearLayout fromdateSelectorLayout;
    private LinearLayout todateSelectorLayout , generateExcelSheet;
    private Switch switchfor_singleDayDump;
    private Spinner daysCountSpinner;
    List<Modal_RatingOrderDetails> ratingList;
    List<Modal_RatingOrderDetails> sorted_ratingList;
    public static List<Modal_OrderDetails> sorted_OrdersList;
    public static List<String> array_of_orderId;
    double screenInches;
    String PreviousDateString,DateString,TodaysDate,vendorKey,vendorname,OrderDetailsResultjsonString,CurrentDate,LastDate,fromdatestring,todatestring,currentdateLong,oldDayLong;

    private DatePickerDialog todatepicker;

    static boolean isSearchButtonClicked = false;
    int spinnerselecteditem=1;
    int spinnerselecteditem_Count =1;
    String selectedStartDate = "";
    String selectedEndDate = "";
    public static List<String> itemRatingarray = new  ArrayList<>();
    public static List<String> qualityRatingArray = new  ArrayList<>();
    public static List<String> deliveryRatingArray = new  ArrayList<>();

    public static List<String> RatingArray = new  ArrayList<>();

    String itemrating_String="0", qualityrating_String="0", deliveryrating_String="0", totalrating_String="0";
    double itemrating_double=0, qualityrating_double=0, deliveryrating_double=0, totalitemrating_double=0, totalqualityrating_double=0, totaldeliveryrating_double=0,totalrating_double=0,totalNo_of_stars_expected=0,no_stars_given_in_percentage=0,no_of_stars_recieved_for_the_service=0;
    ProgressBar progress_rating5,progress_rating4,progress_rating3,progress_rating2,progress_rating1,quantityratingprogressbar5,quantityratingprogressbar4,quantityratingprogressbar3,quantityratingprogressbar2,quantityratingprogressbar1
    ,deliveryratingprogressbar5,deliveryratingprogressbar4,deliveryratingprogressbar3,deliveryratingprogressbar2,deliveryratingprogressbar1;

    public static HashMap<String, String>  totalRatingHashmap  = new   HashMap();

    public static HashMap<String, String>  itemRatingHashmap  = new   HashMap();
    public static  HashMap<String, String> qualityRatingHashmap  = new HashMap();
    public static  HashMap<String, String>  deliveryRatingHashmap  = new HashMap();

    Button detailedRatingButton;
    String orderid="",itemnamefromOrderDetails="";

    int iterator =0,isReachedLastItemInLoop_int=0;
    boolean  isgetRatingDetailsCalled = false;


    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;

    Workbook wb;
    Sheet sheet = null;
    private static String[] columns = {"S.No","Orderid ","Mobile Number","Created Time","Item Name","Quantity Rating","Delivery Rating","FeedBack"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datewise_ratingreport__first_screen);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        switchfor_singleDayDump = findViewById(R.id.switchfor_singleDayDump);
        daysCountSpinner = (Spinner) findViewById(R.id.daysCountSpinner);
        daysCountSpinner_layout = findViewById(R.id.daysCountSpinner_layout);
        orderinstruction = findViewById(R.id.orderinstruction);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        generateDataInstruction = findViewById(R.id.generateDataInstruction);
        mobile_nameofFacility_Textview = findViewById(R.id.nameofFacility_Textview);
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        appOrdersCount_textwidget =  findViewById(R.id.appOrdersCount_textwidget);
        fromdateSelectorLayout =  findViewById(R.id.fromdateSelectorLayout);
        todateSelectorLayout =  findViewById(R.id.todateSelectorLayout);
        todateSelector_text = findViewById(R.id.todateSelector_text);
        fromdateSelector_text = findViewById(R.id.fromdateSelector_text);
        ratingList = new ArrayList<Modal_RatingOrderDetails>();
        sorted_OrdersList = new ArrayList<Modal_OrderDetails>();
        array_of_orderId = new ArrayList<>();
        sorted_ratingList = new ArrayList<>();
        wholeRating_textview = findViewById(R.id.wholeRating_textview);
        ratingsCount_textview = findViewById(R.id.ratingsCount_textview);
        generateExcelSheet = findViewById(R.id.generateExcelSheet );



        detailedRatingButton = findViewById(R.id.detailedRatingButton);


        progress_rating5 = findViewById(R.id.progress_rating5);
        progress_rating4 = findViewById(R.id.progress_rating4);
        progress_rating3 = findViewById(R.id.progress_rating3);
        progress_rating2 = findViewById(R.id.progress_rating2);
        progress_rating1 = findViewById(R.id.progress_rating1);


        rating5_count = findViewById(R.id.rating5_count);
        rating4_count = findViewById(R.id.rating4_count);
        rating3_count = findViewById(R.id.rating3_count);
        rating2_count = findViewById(R.id.rating2_count);
        rating1_count = findViewById(R.id.rating1_count);


        quantityRating_textview = findViewById(R.id.quantityRating_textview);
        quantityratingsCount_textview = findViewById(R.id.quantityratingsCount_textview);
        quantityratingprogressbar5 = findViewById(R.id.quantityratingprogressbar5);
        quantityratingprogressbar4 = findViewById(R.id.quantityratingprogressbar4);
        quantityratingprogressbar3 = findViewById(R.id.quantityratingprogressbar3);
        quantityratingprogressbar2 = findViewById(R.id.quantityratingprogressbar2);
        quantityratingprogressbar1 = findViewById(R.id.quantityratingprogressbar1);


        quantityrating5_count = findViewById(R.id.quantityrating5_count);
        quantityrating4_count = findViewById(R.id.quantityrating4_count);
        quantityrating3_count = findViewById(R.id.quantityrating3_count);
        quantityrating2_count = findViewById(R.id.quantityrating2_count);
        quantityrating1_count = findViewById(R.id.quantityrating1_count);


        deliveryRating_textview = findViewById(R.id.deliveryRating_textview);
        deliveryratingsCount_textview = findViewById(R.id.deliveryratingsCount_textview);
        deliveryratingprogressbar5 = findViewById(R.id.deliveryratingprogressbar5);
        deliveryratingprogressbar4 = findViewById(R.id.deliveryratingprogressbar4);
        deliveryratingprogressbar3 = findViewById(R.id.deliveryratingprogressbar3);
        deliveryratingprogressbar2 = findViewById(R.id.deliveryratingprogressbar2);
        deliveryratingprogressbar1 = findViewById(R.id.deliveryratingprogressbar1);


        deliveryrating5_count = findViewById(R.id.deliveryrating5_count);
        deliveryrating4_count = findViewById(R.id.deliveryrating4_count);
        deliveryrating3_count = findViewById(R.id.deliveryrating3_count);
        deliveryrating2_count = findViewById(R.id.deliveryrating2_count);
        deliveryrating1_count = findViewById(R.id.deliveryrating1_count);


//        orderinstruction.setVisibility(View.VISIBLE);
//        orderinstruction.setText("Select Date to get Data");
        mobile_nameofFacility_Textview.setText(vendorname);
        daysCountSpinner_layout.setVisibility(View.GONE);




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
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
            screenInches = Math.sqrt(x + y);


        }
        catch (Exception e){
            e.printStackTrace();
        }


            try{
                TodaysDate = getDatewithNameoftheDay();
                PreviousDateString = getDatewithNameofthePreviousDay();

                dateSelector_text.setText(TodaysDate);
                fromdateSelector_text.setText(TodaysDate);
                todateSelector_text.setText(TodaysDate);

            }
            catch (Exception e){
                e.printStackTrace();
            }




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


        generateExcelSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingList.size()>0){
                    try {
                        wb = new HSSFWorkbook();
                        //Now we are creating sheet

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (SDK_INT >= Build.VERSION_CODES.R) {

                        if(Environment.isExternalStorageManager()){
                            try {
                                AddDatatoExcelSheet(ratingList);

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


                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(DatewiseRatingreport_FirstScreen.this, WRITE_EXTERNAL_STORAGE);
                        //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(DatewiseRatingreport_FirstScreen.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {
                            showProgressBar(true);
                            try {
                                AddDatatoExcelSheet(ratingList);

                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
                            }
                        }
                    }
                }else {
                    Toast.makeText(DatewiseRatingreport_FirstScreen.this, "There iS no Data to Generate Sheet", Toast.LENGTH_SHORT).show();
                }
            }
        });







        detailedRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();

                b.putSerializable("ratingList", (Serializable) sorted_ratingList);
                Intent intent = new Intent(DatewiseRatingreport_FirstScreen.this, DatewiseRatingreport_SecondScreen.class);
                intent.putExtras(b);
                startActivity(intent);



            }
        });







        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemRatingHashmap.clear();
                qualityRatingHashmap.clear();
                deliveryRatingHashmap.clear();
                qualityRatingArray.clear();
                itemRatingarray.clear();
                deliveryRatingArray.clear();
                isReachedLastItemInLoop_int=0;
                ratingList.clear();
                sorted_ratingList.clear();
                sorted_OrdersList.clear();
                RatingArray.clear();
                // deliveryRating_textview.findViewById(R.id.deliveryRating_textview);
                RatingArray.add("1");
                RatingArray.add("2");
                RatingArray.add("3");
                RatingArray.add("4");
                RatingArray.add("5");
                itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0";totalrating_String="0";
                itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;
                no_of_stars_recieved_for_the_service=0;
                SetProgressBarAndCount("MakeZero");



                spinnerselecteditem = 1 ;
                spinnerselecteditem_Count=1;
                showProgressBar(true);
                todatestring=dateSelector_text.getText().toString();
                fromdatestring = todatestring;
                PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);

                isSearchButtonClicked = false;
                SetProgressBarAndCount("MakeZero");

                getRatingDetailsUsingDateAndVendorKey(PreviousDateString, todatestring, vendorKey);

            }
        });


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });












        switchfor_singleDayDump.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                isSearchButtonClicked = false;
                itemRatingHashmap.clear();
                qualityRatingHashmap.clear();
                deliveryRatingHashmap.clear();
                qualityRatingArray.clear();
                itemRatingarray.clear();
                deliveryRatingArray.clear();
                isReachedLastItemInLoop_int=0;
                ratingList.clear();
                sorted_ratingList.clear();
                sorted_OrdersList.clear();
                itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0";totalrating_String="0";
                itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;
                no_of_stars_recieved_for_the_service=0;
                SetProgressBarAndCount("MakeZero");

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

    private void AddDatatoExcelSheet(List<Modal_RatingOrderDetails> ratingList) {
        String createdTime = "",orderId = "",mobileNumber = "",itemName = "",itemQuantity = "",feedBack = "",qualityrating = "",deliveryrating = "";

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        sheet = wb.createSheet(String.valueOf(System.currentTimeMillis()) );
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

        for (int i = 0; i < columns.length; i++) {
            headercell = headerRow.createCell(i);
            headercell.setCellValue(columns[i]);
            headercell.setCellStyle(headerCellStyle);
        }


        for(int position =0 ; position < ratingList.size() ; position++) {

            Modal_RatingOrderDetails modal_ratingOrderDetails = ratingList.get(position);
            deliveryrating = modal_ratingOrderDetails.getDeliveryrating();
            createdTime = modal_ratingOrderDetails.getCreatedtime();
            mobileNumber = modal_ratingOrderDetails.getUsermobileno();
            feedBack = modal_ratingOrderDetails.getFeedback();
            qualityrating = modal_ratingOrderDetails.getQualityrating();
            deliveryrating = modal_ratingOrderDetails.getDeliveryrating();
            itemName = modal_ratingOrderDetails.getItemname();
            orderId = modal_ratingOrderDetails.getOrderid();

            Row row ;

            row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum-1);
            row.createCell(1).setCellValue(orderId);
            row.createCell(2).setCellValue(mobileNumber);
            row.createCell(3).setCellValue(createdTime);
            row.createCell(4).setCellValue(itemName);
            row.createCell(5).setCellValue(qualityrating);
            row.createCell(6).setCellValue(deliveryrating);
            row.createCell(7).setCellValue(feedBack);

            row.getCell(5).setCellStyle(cellStyle);
            row.getCell(6).setCellStyle(cellStyle);


            sheet.setColumnWidth(0, (10 * 250));
            sheet.setColumnWidth(1, (10 * 500));
            sheet.setColumnWidth(2, (10 * 500));
            sheet.setColumnWidth(3, (10 * 700));
            sheet.setColumnWidth(4, (10 * 1200));
            sheet.setColumnWidth(5, (10 * 600));
            sheet.setColumnWidth(6, (10 * 600));
            sheet.setColumnWidth(7, (10 * 1000));
            ;


            if(ratingList.size() - position ==1 ){
                GenerateExcelSheet();

            }

        }


    }


    private void GenerateExcelSheet() {
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/Rating Report Weekwise/";
        File dir = new File(path);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");

        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "Consolidated Report "+ System.currentTimeMillis()  +".xls");


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





    private void openDatePicker() {

        spinnerselecteditem = 1 ;
        spinnerselecteditem_Count=1;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(DatewiseRatingreport_FirstScreen.this,
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
                            ////Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            showProgressBar(true);
                            todatestring=DateString;
                            fromdatestring = dateSelector_text.getText().toString();
                            itemRatingHashmap.clear();
                            qualityRatingHashmap.clear();
                            deliveryRatingHashmap.clear();
                            qualityRatingArray.clear();
                            itemRatingarray.clear();
                            deliveryRatingArray.clear();
                            isReachedLastItemInLoop_int=0;
                            ratingList.clear();
                            sorted_ratingList.clear();
                            sorted_OrdersList.clear();
                            RatingArray.clear();
                            // deliveryRating_textview.findViewById(R.id.deliveryRating_textview);
                            RatingArray.add("1");
                            RatingArray.add("2");
                            RatingArray.add("3");
                            RatingArray.add("4");
                            RatingArray.add("5");
                            itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0";totalrating_String="0";
                            itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;
                            no_of_stars_recieved_for_the_service=0;
                            SetProgressBarAndCount("MakeZero");






                            getRatingDetailsUsingDateAndVendorKey(PreviousDateString, DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }















    private void openFromDatePicker() {

        spinnerselecteditem = 1 ;
        spinnerselecteditem_Count=1;
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        fromdatepicker = new DatePickerDialog(DatewiseRatingreport_FirstScreen.this,
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

                            itemRatingHashmap.clear();
                            qualityRatingHashmap.clear();
                            deliveryRatingHashmap.clear();
                            qualityRatingArray.clear();
                            itemRatingarray.clear();
                            deliveryRatingArray.clear();
                            isReachedLastItemInLoop_int=0;
                            ratingList.clear();
                            sorted_ratingList.clear();
                            sorted_OrdersList.clear();
                            RatingArray.clear();
                            // deliveryRating_textview.findViewById(R.id.deliveryRating_textview);
                            RatingArray.add("1");
                            RatingArray.add("2");
                            RatingArray.add("3");
                            RatingArray.add("4");
                            RatingArray.add("5");
                            itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0";totalrating_String="0";
                            itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;
                            no_of_stars_recieved_for_the_service=0;
                            SetProgressBarAndCount("MakeZero");


                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay =   getDayString(dayOfWeek);
                            ////Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            //   PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            fromdateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            fromdatestring = fromdateSelector_text.getText().toString();
                            selectedStartDate = fromdatestring;
                            selectedEndDate = getDatewithNameoftheseventhDayFromSelectedStartDate(DateString);

                            //       showProgressBar(true);

                            //getRatingDetailsUsingDateAndVendorKey(PreviousDateString, DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);

        Calendar c = Calendar.getInstance();





        DatePicker datePicker = fromdatepicker.getDatePicker();

        c.add(Calendar.DATE, -30);
        // Toast.makeText(getApplicationContext(), Calendar.DATE, Toast.LENGTH_LONG).show();
        //Log.d(Constants.TAG, "Calendar.DATE " + String.valueOf(Calendar.DATE));
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
        todatepicker = new DatePickerDialog(DatewiseRatingreport_FirstScreen.this,
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
                            ////Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            //   PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            todateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            showProgressBar(true);
                            todatestring = todateSelector_text.getText().toString();

                            itemRatingHashmap.clear();
                            qualityRatingHashmap.clear();
                            deliveryRatingHashmap.clear();
                            qualityRatingArray.clear();
                            itemRatingarray.clear();
                            deliveryRatingArray.clear();
                            isReachedLastItemInLoop_int=0;
                            ratingList.clear();
                            sorted_ratingList.clear();
                            sorted_OrdersList.clear();
                            itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0";totalrating_String="0";
                            itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;
                            no_of_stars_recieved_for_the_service=0;
                            SetProgressBarAndCount("MakeZero");


                            calculate_the_dateandgetData(fromdatestring,todatestring);
                            // getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, DateString, vendorKey);
                            // getRatingDetailsUsingDateAndVendorKey(PreviousDateString, DateString, vendorKey);

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














    private void getRatingDetailsUsingDateAndVendorKey(String previousDateString, String todaysdate, String vendorKey) {
        showProgressBar(true);

        itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0"; totalrating_String="0";
        itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;
        totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;no_of_stars_recieved_for_the_service=0;

        //Log.d(Constants.TAG, "previousDateString Response: " + previousDateString);
        //Log.d(Constants.TAG, "todaysdate Response: " + todaysdate);
        //Log.d(Constants.TAG, "vendorKey Response: " + vendorKey);



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetRatingDetailsUsingVendorid + "?vendorkey="+vendorKey+"&createdtime="+todaysdate,null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {

                            OrderDetailsResultjsonString = response.toString();
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + OrderDetailsResultjsonString);
                            int i1=0;

                            JSONArray contentArray = response.getJSONArray("content");
                            if(contentArray.length()==0){
                                if(ratingList.size()<=0) {
                                    //    showProgressBar(false);
                                    showProgressBar(false);
                                    SetProgressBarAndCount("MakeZero");

                                    return;
                                }
                            }
                            else
                                {

                                for(;i1<(contentArray.length());i1++) {
                                    JSONObject json = contentArray.getJSONObject(i1);
                                    Modal_RatingOrderDetails modal_ratingOrderDetails = new Modal_RatingOrderDetails();
                                    try{
                                        if(json.has("createdtime")){
                                            modal_ratingOrderDetails.createdtime = json.getString("createdtime");


                                        }
                                        else{

                                            modal_ratingOrderDetails.createdtime = "";
                                        }
                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                        modal_ratingOrderDetails.createdtime = "";
                                    }




                                    try{
                                        if(json.has("feedback")){
                                            modal_ratingOrderDetails.feedback = json.getString("feedback");


                                        }
                                        else{
                                            modal_ratingOrderDetails.feedback = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.feedback = "";
                                        e.printStackTrace();
                                    }






                                    try{
                                        if(json.has("orderid")){
                                            modal_ratingOrderDetails.orderid = json.getString("orderid");
                                            orderid = json.getString("orderid");
                                        }
                                        else{
                                            orderid = "";
                                            modal_ratingOrderDetails.orderid ="";
                                        }
                                    }
                                    catch(Exception e){
                                        orderid = "";

                                        modal_ratingOrderDetails.orderid ="";
                                        e.printStackTrace();
                                    }



                                    try{
                                        if(json.has("itemrating")){

                                            String itemName="";
                                            String itemrating = "";
                                            itemrating = json.getString("itemrating");
                                            if(itemrating.equals("")||itemrating.equals(null)){

                                                //    new GetItemdetailsfromOrderDetailsNew(orderid).execute();



                                                modal_ratingOrderDetails.itemname = itemnamefromOrderDetails ;
                                                itemName =  itemnamefromOrderDetails ;
                                            }
                                            else {

                                                modal_ratingOrderDetails.itemrating_json = itemrating;
                                                JSONArray itemratingarray = new JSONArray(itemrating);
                                                for (int i = 0; i < itemratingarray.length(); i++) {

                                                    JSONObject itemRatingjson = itemratingarray.getJSONObject(i);
                                                    try {
                                                        if (itemRatingjson.has("rating")) {
                                                            modal_ratingOrderDetails.itemrating = itemRatingjson.getString("rating");
                                                            String itemRating = json.getString("rating");
                                                            if (itemRating.equals("") || itemRating.equals("null") || itemRating.equals(null)) {
                                                                itemRating = "0";
                                                            }
                                                            itemrating_double = Double.parseDouble(itemRating);

                                                            itemRatingarray.add(itemRating);
                                                        } else {
                                                            modal_ratingOrderDetails.itemrating = "";
                                                            String itemRating = "0";

                                                            itemrating_double = Double.parseDouble(itemRating);

                                                            itemRatingarray.add(itemRating);
                                                        }
                                                    } catch (Exception e) {

                                                        modal_ratingOrderDetails.itemrating = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        if (itemRatingjson.has("itemname")) {
                                                            if (itemName.length() > 0) {
                                                                itemName = itemName + " , " + itemRatingjson.getString("itemname");

                                                            } else {
                                                                itemName = itemRatingjson.getString("itemname");
                                                            }
                                                            modal_ratingOrderDetails.itemname = itemName;
                                                        } else {
                                                            modal_ratingOrderDetails.itemname = " - ";
                                                            itemName = " - ";
                                                        }
                                                    } catch (Exception e) {
                                                        modal_ratingOrderDetails.itemname = " - ";
                                                        itemName = " - ";
                                                        e.printStackTrace();
                                                    }


                                                }
                                            }
                                        }
                                        else{
                                            modal_ratingOrderDetails.itemrating_json = " - ";
                                            modal_ratingOrderDetails.itemname =" - ";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.itemname =" - ";

                                        modal_ratingOrderDetails.itemrating_json = " - ";
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("key")){
                                            modal_ratingOrderDetails.key = json.getString("key");

                                        }
                                        else{

                                            modal_ratingOrderDetails.key = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.key = "";
                                        e.printStackTrace();
                                    }



                                    try{
                                        if(json.has("userkey")){
                                            modal_ratingOrderDetails.userkey = json.getString("userkey");


                                        }
                                        else{
                                            modal_ratingOrderDetails.userkey = "";
                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.userkey = "";
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("usermobileno")){
                                            modal_ratingOrderDetails.usermobileno = json.getString("usermobileno");

                                        }
                                        else{

                                            modal_ratingOrderDetails.usermobileno = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.usermobileno = "";
                                        e.printStackTrace();
                                    }



                                    try{
                                        if(json.has("vendorkey")){
                                            modal_ratingOrderDetails.vendorkey = json.getString("vendorkey");

                                        }
                                        else{

                                            modal_ratingOrderDetails.vendorkey = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.vendorkey = "";
                                        e.printStackTrace();
                                    }


                                    try{
                                        if(json.has("vendorname")){
                                            modal_ratingOrderDetails.vendorname = json.getString("vendorname");

                                        }
                                        else{

                                            modal_ratingOrderDetails.vendorname = "";

                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.vendorname = "";
                                        e.printStackTrace();
                                    }





                                    try{
                                        if(json.has("deliveryrating")){
                                            String deliveryRating = json.getString("deliveryrating");
                                            if(deliveryRating.equals("")||deliveryRating.equals("null")||deliveryRating.equals(null)){
                                                deliveryRating = "0";
                                            }
                                            modal_ratingOrderDetails.deliveryrating = deliveryRating;

                                            // deliveryRatingArray.add(deliveryRating);
                                            //deliveryrating_double = Double.parseDouble(deliveryRating);


                                        }
                                        else{

                                            String  deliveryRating = "0";
                                            modal_ratingOrderDetails.deliveryrating = "0";

                                            //deliveryrating_double = Double.parseDouble(deliveryRating);

                                            //   deliveryRatingArray.add(deliveryRating);
                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.deliveryrating  = "0";
                                        e.printStackTrace();
                                    }
                                    try{
                                        if(json.has("qualityrating")){
                                            String qualityrating = json.getString("qualityrating");
                                            if(qualityrating.equals("")||qualityrating.equals("null")||qualityrating.equals(null)){
                                                qualityrating = "0";
                                            }
                                            modal_ratingOrderDetails.qualityrating = qualityrating;


                                            //    qualityRatingArray.add(qualityrating);

                                            //   qualityrating_double = Double.parseDouble(qualityrating);

                                        }
                                        else{
                                            modal_ratingOrderDetails.qualityrating = "0";

                                            String  qualityrating = "0";
                                            //qualityrating_double = Double.parseDouble(qualityrating);

                                            //   qualityRatingArray.add(qualityrating);
                                        }
                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.qualityrating  = "0";
                                        e.printStackTrace();
                                    }




                                    ratingList.add(modal_ratingOrderDetails);

                                }


                                    //Log.d(Constants.TAG, "ratingList:   " +ratingList.size());

                            }



                            if(todaysdate.equals(todatestring)) {
                                //Log.d(Constants.TAG, "ratingList: 2  " +ratingList.size());
                                FilterDatawithVendorKey(vendorKey);

                                //calculateRatingPercentage();
                               // GetItemdetailsfromOrderDetailsNeww(vendorKey);
                                // new GetItemdetailsfromOrderDetailsNew(orderid).execute();
                            }
                            else{
                                String nextday = getTomorrowsDate(todaysdate);
                                calculate_the_dateandgetData(nextday,todatestring);

                            }

                        }
                        catch (Exception e){
                            showProgressBar(false);

                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(DatewiseRatingreport_FirstScreen.this, "There is no rating on  " + todaysdate, Toast.LENGTH_LONG).show();

                    if(todaysdate.equals(todatestring)) {
                        showProgressBar(false);
                        // calculateRatingPercentage();
                       // GetItemdetailsfromOrderDetailsNeww(vendorKey);
                        FilterDatawithVendorKey(vendorKey);

                        // new GetItemdetailsfromOrderDetailsNew(orderid).execute();
                    }
                    else{
                        String nextday = getTomorrowsDate(todaysdate);
                        calculate_the_dateandgetData(nextday,todatestring);

                    }


//
                    // showProgressBar(false);
                    error.printStackTrace();


                }
                catch (Exception e){
                    showProgressBar(false);

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
        Volley.newRequestQueue(DatewiseRatingreport_FirstScreen.this).add(jsonObjectRequest);












    }








    private void GetItemdetailsfromOrderDetailsNeww(String vendorKey) {



        showProgressBar(true);
        for( iterator=0;iterator<ratingList.size();iterator++)    {
            Modal_RatingOrderDetails modal_ratingOrderDetails = ratingList.get(iterator);
            String orderId = modal_ratingOrderDetails.getOrderid();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsusingOrderid + orderId, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            try {
                                //Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                                try {
                                    itemnamefromOrderDetails = "";
                                    String ordertype = "#";

                                    //converting jsonSTRING into array
                                    JSONArray JArray = response.getJSONArray("content");
                                    ////Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                    int i1 = 0;
                                    int arrayLength = JArray.length();
                                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                    if (arrayLength > 1) {
                                        showProgressBar(false);
                                        Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();
                                        return;

                                    }

                                    for (; i1 < (arrayLength); i1++) {
                                        try {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                            try {
                                                if (json.has("coupondiscount")) {
                                                    modal_orderDetails.coupondiscount = json.getString("coupondiscount");
                                                } else {
                                                    modal_orderDetails.coupondiscount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.coupondiscount = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("couponkey")) {
                                                    modal_orderDetails.couponkey = json.getString("couponkey");
                                                } else {
                                                    modal_orderDetails.couponkey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.couponkey = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("deliveryamount")) {
                                                    modal_orderDetails.deliveryamount = json.getString("deliveryamount");
                                                } else {
                                                    modal_orderDetails.deliveryamount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.deliveryamount = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("deliverytype")) {
                                                    modal_orderDetails.deliverytype = json.getString("deliverytype");
                                                } else {
                                                    modal_orderDetails.deliverytype = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.deliverytype = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("gstamount")) {
                                                    modal_orderDetails.gstamount = json.getString("gstamount");
                                                } else {
                                                    modal_orderDetails.gstamount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.gstamount = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("itemdesp")) {
                                                    JSONArray itemdesp = json.getJSONArray("itemdesp");

                                                    modal_orderDetails.itemdesp = itemdesp;


                                                    for (int i = 0; i < itemdesp.length(); i++) {
                                                        ////Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                                                        JSONObject itemdespjson = itemdesp.getJSONObject(i);
                                                        try {
                                                            if (itemdespjson.has("marinadeitemdesp")) {
                                                                JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                                                                if (itemnamefromOrderDetails.length() > 0) {
                                                                    itemnamefromOrderDetails = itemnamefromOrderDetails + " , ";

                                                                } else {
                                                                    itemnamefromOrderDetails = String.valueOf(marinadesObject.get("itemname"));
                                                                }
                                                                modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }


                                                        try {

                                                            if (itemnamefromOrderDetails.length() > 0) {
                                                                itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf(itemdespjson.get("itemname"));

                                                            } else {
                                                                itemnamefromOrderDetails = String.valueOf(itemdespjson.get("itemname"));
                                                            }
                                                            modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                        } catch (Exception e) {
                                                            if (itemnamefromOrderDetails.length() > 0) {
                                                                itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf("");

                                                            } else {
                                                                itemnamefromOrderDetails = String.valueOf((""));
                                                            }
                                                            modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                } else {

                                                    itemnamefromOrderDetails = String.valueOf((""));
                                                    modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                itemnamefromOrderDetails = String.valueOf((""));
                                                modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                            }

                                            try {
                                                if (json.has("key")) {
                                                    modal_orderDetails.orderdetailskey = json.getString("key");
                                                } else {
                                                    modal_orderDetails.orderdetailskey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderdetailskey = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("notes")) {
                                                    modal_orderDetails.notes = json.getString("notes");
                                                } else {
                                                    modal_orderDetails.notes = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.notes = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("orderid")) {
                                                    modal_orderDetails.orderid = json.getString("orderid");
                                                } else {
                                                    modal_orderDetails.orderid = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderid = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("orderplaceddate")) {
                                                    modal_orderDetails.orderplaceddate = json.getString("orderplaceddate");
                                                } else {
                                                    modal_orderDetails.orderplaceddate = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderplaceddate = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("orderplacedtime")) {
                                                    modal_orderDetails.orderplacedtime = json.getString("orderplacedtime");
                                                } else {
                                                    modal_orderDetails.orderplacedtime = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderplacedtime = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("ordertype")) {
                                                    modal_orderDetails.ordertype = json.getString("ordertype");
                                                } else {
                                                    modal_orderDetails.ordertype = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.ordertype = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("payableamount")) {
                                                    modal_orderDetails.payableamount = json.getString("payableamount");
                                                } else {
                                                    modal_orderDetails.payableamount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.payableamount = "-";
                                                e.printStackTrace();
                                            }


                                            try {

                                                if (json.has("paymentmode")) {
                                                    modal_orderDetails.paymentmode = json.getString("paymentmode");
                                                } else {
                                                    modal_orderDetails.paymentmode = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.paymentmode = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("slotdate")) {
                                                    modal_orderDetails.slotdate = json.getString("slotdate");
                                                } else {
                                                    modal_orderDetails.slotdate = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.slotdate = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("slotname")) {
                                                    modal_orderDetails.slotname = json.getString("slotname");
                                                } else {
                                                    modal_orderDetails.slotname = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.slotname = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("slottimerange")) {
                                                    modal_orderDetails.slottimerange = json.getString("slottimerange");
                                                } else {
                                                    modal_orderDetails.slottimerange = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.slottimerange = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("tokenno")) {
                                                    modal_orderDetails.tokenno = json.getString("tokenno");
                                                } else {
                                                    modal_orderDetails.tokenno = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.tokenno = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("useraddress")) {
                                                    modal_orderDetails.useraddress = json.getString("useraddress");
                                                } else {
                                                    modal_orderDetails.useraddress = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.useraddress = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("userkey")) {
                                                    modal_orderDetails.userkey = json.getString("userkey");
                                                } else {
                                                    modal_orderDetails.userkey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.userkey = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("useraddress")) {
                                                    modal_orderDetails.useraddress = json.getString("useraddress");
                                                } else {
                                                    modal_orderDetails.useraddress = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.useraddress = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("usermobile")) {
                                                    modal_orderDetails.usermobile = json.getString("usermobile");
                                                } else {
                                                    modal_orderDetails.usermobile = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.usermobile = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("vendorkey")) {
                                                    modal_orderDetails.vendorkey = json.getString("vendorkey");
                                                } else {
                                                    modal_orderDetails.vendorkey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.vendorkey = "-";
                                                e.printStackTrace();
                                            }

                                            sorted_OrdersList.add(modal_orderDetails);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                        isReachedLastItemInLoop_int++;
                                        //Log.d(Constants.TAG, "iterator: " +iterator);
                                        //Log.d(Constants.TAG, "ratingList: " +ratingList.size());
                                        //Log.d(Constants.TAG, "sorted_OrdersList: res 1   " +sorted_OrdersList.size());

                                        if(isReachedLastItemInLoop_int==(ratingList.size())) {
                                            if ( sorted_OrdersList.size() > 0) {
                                                showProgressBar(true);
                                                FilterDatawithVendorKey(vendorKey);
                                                //Log.d(Constants.TAG, "sorted_OrdersList: res 2   " +sorted_OrdersList.size());

                                                //calculateRatingPercentage();
                                            }
                                            else{
                                                //Log.d(Constants.TAG, "iterator: " +iterator);
                                                //Log.d(Constants.TAG, "sorted_OrdersList: " +sorted_OrdersList.size());
                                                showProgressBar(false);
                                                isgetRatingDetailsCalled = false;

                                                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant get OrderDetails", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else{
                                            //Log.d(Constants.TAG, "iterator: 1  " +iterator);
                                            //Log.d(Constants.TAG, "sorted_OrdersList: 1  " +sorted_OrdersList.size());

                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showProgressBar(false);
                                    isgetRatingDetailsCalled = false;

                                    Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                showProgressBar(false);
                                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();
                                isgetRatingDetailsCalled = false;

                            }

                            // orderTrackingDetailstable_Object = getOrderTrackingDetailsFromOrderDetailsTable(orderId,modal_orderDetails);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    ////Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.toString());
                    showProgressBar(false);
                    Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();
                    isgetRatingDetailsCalled = false;
                    error.printStackTrace();
                }
            }) {
                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Make the request
            Volley.newRequestQueue(DatewiseRatingreport_FirstScreen.this).add(jsonObjectRequest);


        }









    }






    private void FilterDatawithVendorKey(String vendorkey) {
        //Log.d(Constants.TAG, "sorted_ratingList: ratingList 1   " +ratingList.size());

        for(int i =0; i<ratingList.size();i++){
            Modal_RatingOrderDetails modal_ratingOrderDetails = ratingList.get(i);

            String orderid_fromRating  =  modal_ratingOrderDetails.getOrderid();
            //Log.d(Constants.TAG, "sorted_ratingList: ratingList   i  1   " +i);
            //Log.d(Constants.TAG, "sorted_ratingList: res 1 in i   " +sorted_ratingList.size());

           // for(int j =0 ; j<sorted_OrdersList.size();j++){
                //Log.d(Constants.TAG, "sorted_ratingList: sorted_OrdersList 1   " +sorted_OrdersList.size());
                //Log.d(Constants.TAG, "sorted_ratingList: ratingList   j  1   " +j);
                //Log.d(Constants.TAG, "sorted_ratingList: res 1  in j " +sorted_ratingList.size());

             //   Modal_OrderDetails modal_orderDetails= sorted_OrdersList.get(j);
               // String orderid_fromOrderDetails = modal_orderDetails.getOrderid();
               // String vendorkey_fromOrderDetails = modal_orderDetails.getVendorkey();
               // //Log.d(Constants.TAG, "sorted_ratingList: orderid_fromOrderDetail  1   " +orderid_fromOrderDetails);
               // //Log.d(Constants.TAG, "sorted_ratingList: vendorkey_fromOrderDetails 1   " +vendorkey_fromOrderDetails);
                if((vendorKey.equals(vendorkey))){
                    //Log.d(Constants.TAG, "sorted_ratingList: res 1 in if   " +sorted_ratingList.size());

                    //Log.d(Constants.TAG, "sorted_ratingList: sorted_OrdersList 1 in if   " +sorted_ratingList.size());
                    //Log.d(Constants.TAG, "sorted_ratingList: ratingList   j  1  in if " +j);
                    String itemnameRatingList = modal_ratingOrderDetails.getItemname().toString();


                    String itemName = modal_ratingOrderDetails.getItemname().toString();


                    if((itemnameRatingList.length()<=0)||(itemnameRatingList.equals(" - "))||(itemnameRatingList.equals(null))||(itemnameRatingList.equals("null"))){
                        modal_ratingOrderDetails.setItemname(itemName);

                    }
                    sorted_ratingList.add(modal_ratingOrderDetails);
                    //Log.d(Constants.TAG, "sorted_ratingList: res 2  " +sorted_ratingList.size());


             //   }
            }

            if(i==(ratingList.size()-1)){
                //Log.d(Constants.TAG, "sorted_ratingList: res 3   " +sorted_ratingList.size());

                calculateRatingPercentage();
            }


        }








    }

    private void calculateRatingPercentage() {
        //Log.d(Constants.TAG, "sorted_ratingList: calculateRatingPercentage   " +sorted_ratingList.size());


        itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0"; totalrating_String="0";
        itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;
        totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;no_of_stars_recieved_for_the_service=0;

        showProgressBar(true);

        //Log.d(Constants.TAG, "ratingList Response: " + ratingList.size());

        //Log.d(Constants.TAG, "sorted_ratingList Response: " + sorted_ratingList.size());
        // String itemrating_String="0", qualityrating_String="0", deliveryrating_String="0", totalrating_String="0";
        // double itemrating_double=0, qualityrating_double=0, deliveryrating_double=0, totalitemrating_double=0, totalqualityrating_double=0, totaldeliveryrating_double=0,totalrating_double=0;
        // double totalNo_of_stars_expected = 0, totalNo_of_stars_get = 0,no_stars_given_in_percentage=0,no_of_stars_recieved_for_the_service;

        try{
            for(int iterator =0;iterator<sorted_ratingList.size();iterator++){
                Modal_RatingOrderDetails modal_ratingOrderDetails = sorted_ratingList.get(iterator);



                try{
                    String deliveryRating = modal_ratingOrderDetails.getDeliveryrating();
                    if(deliveryRating.equals("")||deliveryRating.equals("null")||deliveryRating.equals(null)){
                        deliveryRating = "0";
                    }

                    deliveryRatingArray.add(deliveryRating);
                    deliveryrating_double = Double.parseDouble(deliveryRating);




                }
                catch(Exception e){
                    modal_ratingOrderDetails.deliveryrating  = "";
                    e.printStackTrace();
                }

                //Log.d(Constants.TAG, "deliveryRatingArray Response: " + deliveryRatingArray.size());


                try{
                    String qualityrating = modal_ratingOrderDetails.getQualityrating();
                    if(qualityrating.equals("")||qualityrating.equals("null")||qualityrating.equals(null)){
                        qualityrating = "0";
                    }

                    qualityRatingArray.add(qualityrating);

                    qualityrating_double = Double.parseDouble(qualityrating);


                }
                catch(Exception e){
                    modal_ratingOrderDetails.qualityrating  = "";
                    e.printStackTrace();
                }

                //Log.d(Constants.TAG, "qualityRatingArray Response: " + qualityRatingArray.size());

              /*  try{

                    if(itemrating_double>0){
                        totalrating_double = (itemrating_double+qualityrating_double+deliveryrating_double);
                        totalrating_double = totalrating_double*100;
                        totalrating_double = totalrating_double/15;
                    }
                    else{
                        totalrating_double = (itemrating_double+qualityrating_double+deliveryrating_double);
                        totalrating_double = totalrating_double*100;
                        totalrating_double = totalrating_double/10;
                    }

                    try{
                        totalrating_double = totalrating_double*5;
                        totalrating_double = totalrating_double/100;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    modal_ratingOrderDetails.totalratingForthisOrder = String.valueOf(totalrating_double);
                    try{
                        for(int j =0; j< RatingArray.size();j++){
                            double ratingArray_value = 0;
                            ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                            if(ratingArray_value==Math.round(totalrating_double)){
                                boolean isAvailableinHashmap = totalRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                                if(isAvailableinHashmap) {
                                    double totalRatingHashmap_value = Double.parseDouble(totalRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                    totalRatingHashmap_value = totalRatingHashmap_value+1;
                                    totalRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(totalRatingHashmap_value));
                                }
                                else{
                                    totalRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(1));

                                }
                            }
                        }


                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                catch(Exception e){
                    e.printStackTrace();
                }







                try{
                    totalrating_double = Double.parseDouble(modal_ratingOrderDetails.getTotalratingForthisOrder().toString())+totalrating_double;


                }
                catch(Exception e){
                    e.printStackTrace();
                }

               */

                try{

                   totalNo_of_stars_expected = (sorted_ratingList.size()*5);
                    ratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                    quantityratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                    deliveryratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                /*

                try{
                    no_stars_given_in_percentage = totalrating_double*100;
                    no_stars_given_in_percentage = no_stars_given_in_percentage/totalNo_of_stars_expected;
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try{
                    no_of_stars_recieved_for_the_service = (no_stars_given_in_percentage/100);
                    no_of_stars_recieved_for_the_service = no_of_stars_recieved_for_the_service*5;
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    wholeRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                SetProgressBarAndCount("ForTotalRating");

*/



            }

        }
        catch(Exception e){

            showProgressBar(false);
            Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progree bar for total rating", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try{
            itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0"; totalrating_String="0";
            itemrating_double=0; qualityrating_double=0; deliveryrating_double=0;totalitemrating_double=0; totalqualityrating_double=0;totaldeliveryrating_double=0;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {

            for (int i = 0; i < itemRatingarray.size(); i++) {
                itemrating_String = itemRatingarray.get(i).toString();
                try {
                    itemrating_double = Double.parseDouble(itemrating_String);
                    totalitemrating_double = itemrating_double + totalitemrating_double;


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try{
                    for(int j =0; j< RatingArray.size();j++){
                        double ratingArray_value = 0;
                        ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                        if(ratingArray_value==itemrating_double){
                            if(itemRatingHashmap.containsKey(String.valueOf(ratingArray_value))){
                                double itemratinghashmap_value = Double.parseDouble(itemRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                itemratinghashmap_value = itemratinghashmap_value+1;
                                itemRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(itemratinghashmap_value));
                            }
                            else{
                                itemRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(1));

                            }
                        }
                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }




            }




            for (int i = 0; i < qualityRatingArray.size(); i++) {
                qualityrating_String = qualityRatingArray.get(i).toString();

                try {
                    qualityrating_double = Double.parseDouble(qualityrating_String);
                    totalqualityrating_double =  qualityrating_double + totalqualityrating_double;
                } catch (Exception e) {
                    e.printStackTrace();
                }





                try{
                    for(int j =0; j< RatingArray.size();j++){
                        double ratingArray_value = 0;
                        ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                        if(ratingArray_value==qualityrating_double){
                            if(qualityRatingHashmap.containsKey(String.valueOf(ratingArray_value))){
                                double itemratinghashmap_value = Double.parseDouble(qualityRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                itemratinghashmap_value = itemratinghashmap_value+1;
                                qualityRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(itemratinghashmap_value));
                            }
                            else{
                                qualityRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(1));

                            }
                        }
                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }


                //Log.d(Constants.TAG, "qualityRatingHashmap Response: " + qualityRatingHashmap.size());



            }
            try{
                no_stars_given_in_percentage = 0;
                no_stars_given_in_percentage = totalqualityrating_double*100;
                no_stars_given_in_percentage = no_stars_given_in_percentage/totalNo_of_stars_expected;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                no_of_stars_recieved_for_the_service =0;
                no_of_stars_recieved_for_the_service = (no_stars_given_in_percentage/100);
                no_of_stars_recieved_for_the_service = no_of_stars_recieved_for_the_service*5;
                if(String.valueOf(no_of_stars_recieved_for_the_service).equals("NaN")){
                    no_of_stars_recieved_for_the_service =0;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                quantityRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
            }
            catch (Exception e){
                e.printStackTrace();
            }





            SetProgressBarAndCount("ForQualityRating");









            for (int i = 0; i < deliveryRatingArray.size(); i++) {
                deliveryrating_String = deliveryRatingArray.get(i).toString();

                try {
                    deliveryrating_double = Double.parseDouble(deliveryrating_String);
                    totaldeliveryrating_double = deliveryrating_double + totaldeliveryrating_double;

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < RatingArray.size(); j++) {
                        double ratingArray_value = 0;
                        ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                        if (ratingArray_value == deliveryrating_double) {
                            if (deliveryRatingHashmap.containsKey(String.valueOf(ratingArray_value))) {
                                double itemratinghashmap_value = Double.parseDouble(deliveryRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                itemratinghashmap_value = itemratinghashmap_value + 1;
                                deliveryRatingHashmap.put(String.valueOf(ratingArray_value), String.valueOf(itemratinghashmap_value));
                            } else {
                                deliveryRatingHashmap.put(String.valueOf(ratingArray_value), String.valueOf(1));

                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Log.d(Constants.TAG, "deliveryRatingHashmap Response: " + deliveryRatingHashmap.size());

            try{
                no_stars_given_in_percentage = 0;
                no_stars_given_in_percentage = totaldeliveryrating_double*100;
                no_stars_given_in_percentage = no_stars_given_in_percentage/totalNo_of_stars_expected;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                no_of_stars_recieved_for_the_service =0;
                no_of_stars_recieved_for_the_service = (no_stars_given_in_percentage/100);
                no_of_stars_recieved_for_the_service = no_of_stars_recieved_for_the_service*5;
                if(String.valueOf(no_of_stars_recieved_for_the_service).equals("NaN")){
                    no_of_stars_recieved_for_the_service =0;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                deliveryRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
            }
            catch (Exception e){
                e.printStackTrace();
            }


            SetProgressBarAndCount("ForDeliveryRating");










            showProgressBar(false);


        }
        catch (Exception e){
            showProgressBar(false);
            Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for quality/delivery rating", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }


    }



    private void SetProgressBarAndCount(String RateType) {
        isgetRatingDetailsCalled = false;

        if(RateType.equals("ForTotalRating")){
            try{
                for(int i =0 ; i<RatingArray.size();i++){
                    double ratingArray_value = 0;
                    ratingArray_value = Double.parseDouble(RatingArray.get(i).toString());
                    boolean isAvailableinHashmap = totalRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                    if(isAvailableinHashmap) {
                        String totalratinghashmapValue = "0";
                        totalratinghashmapValue = totalRatingHashmap.get(String.valueOf(ratingArray_value));

                        if (ratingArray_value == 1) {
                            progress_rating1.setMax((int) (sorted_ratingList.size()));
                            progress_rating1.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating1_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));
                        } else if (ratingArray_value == 2) {
                            progress_rating2.setMax((int) (sorted_ratingList.size()));
                            progress_rating2.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating2_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 3) {
                            progress_rating3.setMax((int) (sorted_ratingList.size()));
                            progress_rating3.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating3_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 4) {
                            progress_rating4.setMax((int) (sorted_ratingList.size()));
                            progress_rating4.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating4_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));

                        } else if (ratingArray_value == 5) {
                            progress_rating5.setMax((int) (sorted_ratingList.size()));
                            progress_rating5.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating5_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        }

                        //Log.d(Constants.TAG, "ForTotalRating totalratinghashmapValue: " + totalratinghashmapValue);

                    }
                    else{
                        //String totalratinghashmapValue =String.valueOf(ratingArray_value);

                    }





                }
            }
            catch (Exception e){
                e.printStackTrace();
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

            }
            showProgressBar(false);

        }
        else if(RateType.equals("ForQualityRating")){
            try{
                for(int i =0 ; i<RatingArray.size();i++){
                    double ratingArray_value = 0;
                    ratingArray_value = Double.parseDouble(RatingArray.get(i).toString());
                    boolean isAvailableinHashmap = qualityRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                    if(isAvailableinHashmap) {
                        String totalratinghashmapValue = "0";
                        totalratinghashmapValue = qualityRatingHashmap.get(String.valueOf(ratingArray_value));

                        if (ratingArray_value == 1) {
                            quantityratingprogressbar1.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar1.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating1_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));
                        } else if (ratingArray_value == 2) {
                            quantityratingprogressbar2.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar2.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating2_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 3) {
                            quantityratingprogressbar3.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar3.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating3_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 4) {
                            quantityratingprogressbar4.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar4.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating4_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));

                        } else if (ratingArray_value == 5) {
                            quantityratingprogressbar5.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar5.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating5_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        }


                        Log.d(Constants.TAG, "ForQualityRating totalratinghashmapValue: " + totalratinghashmapValue);
                    }
                    else{
                        String totalratinghashmapValue =String.valueOf(ratingArray_value);

                    }





                }
            }
            catch (Exception e){
                e.printStackTrace();
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

            }
            showProgressBar(false);

        }
        else if(RateType.equals("ForDeliveryRating")){
            try{
                for(int i =0 ; i<RatingArray.size();i++){
                    double ratingArray_value = 0;
                    ratingArray_value = Double.parseDouble(RatingArray.get(i).toString());
                    boolean isAvailableinHashmap = deliveryRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                    if(isAvailableinHashmap) {
                        String totalratinghashmapValue = "0";
                        totalratinghashmapValue = deliveryRatingHashmap.get(String.valueOf(ratingArray_value));

                        if (ratingArray_value == 1) {
                            deliveryratingprogressbar1.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar1.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating1_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));
                        } else if (ratingArray_value == 2) {
                            deliveryratingprogressbar2.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar2.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating2_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 3) {
                            deliveryratingprogressbar3.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar3.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating3_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 4) {
                            deliveryratingprogressbar4.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar4.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating4_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));

                        } else if (ratingArray_value == 5) {
                            deliveryratingprogressbar5.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar5.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating5_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        }


                        //Log.d(Constants.TAG, "ForDeliveryRating totalratinghashmapValue: " + totalratinghashmapValue);

                    }
                    else{
                        String totalratinghashmapValue =String.valueOf(ratingArray_value);

                    }





                }
            }
            catch (Exception e){
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
            showProgressBar(false);

        }
        else if(RateType.equals("MakeZero")){
            try{
                  itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0";totalrating_String="0";
                 itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;
                  no_of_stars_recieved_for_the_service=0;

                no_of_stars_recieved_for_the_service =0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                wholeRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
                quantityRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
                deliveryRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));




                ratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                quantityratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                deliveryratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));

                progress_rating1.setMax((int) (sorted_ratingList.size()));
                progress_rating1.setProgress((int) Double.parseDouble("0.00"));
                rating1_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                progress_rating2.setMax((int) (sorted_ratingList.size()));
                progress_rating2.setProgress((int) Double.parseDouble("0.00"));
                rating2_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                progress_rating3.setMax((int) (sorted_ratingList.size()));
                progress_rating3.setProgress((int) Double.parseDouble("0.00"));
                rating3_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                progress_rating4.setMax((int) (sorted_ratingList.size()));
                progress_rating4.setProgress((int) Double.parseDouble("0.00"));
                rating4_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                progress_rating5.setMax((int) (sorted_ratingList.size()));
                progress_rating5.setProgress((int) Double.parseDouble("0.00"));
                rating5_count.setText(String.valueOf((int) Double.parseDouble("0.00")));





                quantityratingprogressbar1.setMax((int) (sorted_ratingList.size()));
                quantityratingprogressbar1.setProgress((int) Double.parseDouble("0.00"));
                quantityrating1_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                quantityratingprogressbar2.setMax((int) (sorted_ratingList.size()));
                quantityratingprogressbar2.setProgress((int) Double.parseDouble("0.00"));
                quantityrating2_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                quantityratingprogressbar3.setMax((int) (sorted_ratingList.size()));
                quantityratingprogressbar3.setProgress((int) Double.parseDouble("0.00"));
                quantityrating3_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                quantityratingprogressbar4.setMax((int) (sorted_ratingList.size()));
                quantityratingprogressbar4.setProgress((int) Double.parseDouble("0.00"));
                quantityrating4_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                quantityratingprogressbar5.setMax((int) (sorted_ratingList.size()));
                quantityratingprogressbar5.setProgress((int) Double.parseDouble("0.00"));
                quantityrating5_count.setText(String.valueOf((int) Double.parseDouble("0.00")));







                deliveryratingprogressbar1.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar1.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating1_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                deliveryratingprogressbar2.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar2.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating2_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                deliveryratingprogressbar3.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar3.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating3_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                deliveryratingprogressbar4.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar4.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating4_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                deliveryratingprogressbar5.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar5.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating5_count.setText(String.valueOf((int) Double.parseDouble("0.00")));




                //Log.d(Constants.TAG, "For Zero  : " );






            }
            catch (Exception e){
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
        showProgressBar(false);

    }








/*




    private void GetItemdetailsfromOrderDetailsNeww(String vendorKey) {

Boolean isCompleted=false;
showProgressBar(true);
        for( iterator=0;iterator<ratingList.size();iterator++)    {
            Modal_RatingOrderDetails modal_ratingOrderDetails = ratingList.get(iterator);
            String orderId = modal_ratingOrderDetails.getOrderid();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsusingOrderid + orderId, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            try {
                                //Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                                try {
                                    itemnamefromOrderDetails = "";
                                    String ordertype = "#";

                                    //converting jsonSTRING into array
                                    JSONArray JArray = response.getJSONArray("content");
                                    ////Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                    int i1 = 0;
                                    int arrayLength = JArray.length();
                                    //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                    if (arrayLength > 1) {
                                        showProgressBar(false);
                                        Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();
                                        return;

                                    }

                                    for (; i1 < (arrayLength); i1++) {
                                        try {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                            try {
                                                if (json.has("coupondiscount")) {
                                                    modal_orderDetails.coupondiscount = json.getString("coupondiscount");
                                                } else {
                                                    modal_orderDetails.coupondiscount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.coupondiscount = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("couponkey")) {
                                                    modal_orderDetails.couponkey = json.getString("couponkey");
                                                } else {
                                                    modal_orderDetails.couponkey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.couponkey = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("deliveryamount")) {
                                                    modal_orderDetails.deliveryamount = json.getString("deliveryamount");
                                                } else {
                                                    modal_orderDetails.deliveryamount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.deliveryamount = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("deliverytype")) {
                                                    modal_orderDetails.deliverytype = json.getString("deliverytype");
                                                } else {
                                                    modal_orderDetails.deliverytype = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.deliverytype = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("gstamount")) {
                                                    modal_orderDetails.gstamount = json.getString("gstamount");
                                                } else {
                                                    modal_orderDetails.gstamount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.gstamount = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("itemdesp")) {
                                                    JSONArray itemdesp = json.getJSONArray("itemdesp");

                                                    modal_orderDetails.itemdesp = itemdesp;


                                                    for (int i = 0; i < itemdesp.length(); i++) {
                                                        ////Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                                                        JSONObject itemdespjson = itemdesp.getJSONObject(i);
                                                        try {
                                                            if (itemdespjson.has("marinadeitemdesp")) {
                                                                JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                                                                if (itemnamefromOrderDetails.length() > 0) {
                                                                    itemnamefromOrderDetails = itemnamefromOrderDetails + " , ";

                                                                } else {
                                                                    itemnamefromOrderDetails = String.valueOf(marinadesObject.get("itemname"));
                                                                }
                                                                modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }


                                                        try {

                                                            if (itemnamefromOrderDetails.length() > 0) {
                                                                itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf(itemdespjson.get("itemname"));

                                                            } else {
                                                                itemnamefromOrderDetails = String.valueOf(itemdespjson.get("itemname"));
                                                            }
                                                            modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                        } catch (Exception e) {
                                                            if (itemnamefromOrderDetails.length() > 0) {
                                                                itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf("");

                                                            } else {
                                                                itemnamefromOrderDetails = String.valueOf((""));
                                                            }
                                                            modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                } else {

                                                    itemnamefromOrderDetails = String.valueOf((""));
                                                    modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                itemnamefromOrderDetails = String.valueOf((""));
                                                modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                            }

                                            try {
                                                if (json.has("key")) {
                                                    modal_orderDetails.orderdetailskey = json.getString("key");
                                                } else {
                                                    modal_orderDetails.orderdetailskey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderdetailskey = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("notes")) {
                                                    modal_orderDetails.notes = json.getString("notes");
                                                } else {
                                                    modal_orderDetails.notes = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.notes = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("orderid")) {
                                                    modal_orderDetails.orderid = json.getString("orderid");
                                                } else {
                                                    modal_orderDetails.orderid = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderid = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("orderplaceddate")) {
                                                    modal_orderDetails.orderplaceddate = json.getString("orderplaceddate");
                                                } else {
                                                    modal_orderDetails.orderplaceddate = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderplaceddate = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("orderplacedtime")) {
                                                    modal_orderDetails.orderplacedtime = json.getString("orderplacedtime");
                                                } else {
                                                    modal_orderDetails.orderplacedtime = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.orderplacedtime = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("ordertype")) {
                                                    modal_orderDetails.ordertype = json.getString("ordertype");
                                                } else {
                                                    modal_orderDetails.ordertype = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.ordertype = "-";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (json.has("payableamount")) {
                                                    modal_orderDetails.payableamount = json.getString("payableamount");
                                                } else {
                                                    modal_orderDetails.payableamount = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.payableamount = "-";
                                                e.printStackTrace();
                                            }


                                            try {

                                                if (json.has("paymentmode")) {
                                                    modal_orderDetails.paymentmode = json.getString("paymentmode");
                                                } else {
                                                    modal_orderDetails.paymentmode = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.paymentmode = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("slotdate")) {
                                                    modal_orderDetails.slotdate = json.getString("slotdate");
                                                } else {
                                                    modal_orderDetails.slotdate = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.slotdate = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("slotname")) {
                                                    modal_orderDetails.slotname = json.getString("slotname");
                                                } else {
                                                    modal_orderDetails.slotname = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.slotname = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("slottimerange")) {
                                                    modal_orderDetails.slottimerange = json.getString("slottimerange");
                                                } else {
                                                    modal_orderDetails.slottimerange = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.slottimerange = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("tokenno")) {
                                                    modal_orderDetails.tokenno = json.getString("tokenno");
                                                } else {
                                                    modal_orderDetails.tokenno = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.tokenno = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("useraddress")) {
                                                    modal_orderDetails.useraddress = json.getString("useraddress");
                                                } else {
                                                    modal_orderDetails.useraddress = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.useraddress = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("userkey")) {
                                                    modal_orderDetails.userkey = json.getString("userkey");
                                                } else {
                                                    modal_orderDetails.userkey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.userkey = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("useraddress")) {
                                                    modal_orderDetails.useraddress = json.getString("useraddress");
                                                } else {
                                                    modal_orderDetails.useraddress = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.useraddress = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("usermobile")) {
                                                    modal_orderDetails.usermobile = json.getString("usermobile");
                                                } else {
                                                    modal_orderDetails.usermobile = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.usermobile = "-";
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (json.has("vendorkey")) {
                                                    modal_orderDetails.vendorkey = json.getString("vendorkey");
                                                } else {
                                                    modal_orderDetails.vendorkey = "-";
                                                }
                                            } catch (Exception e) {
                                                modal_orderDetails.vendorkey = "-";
                                                e.printStackTrace();
                                            }

                                            sorted_OrdersList.add(modal_orderDetails);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }



                                        isReachedLastItemInLoop_int++;
                                        //Log.d(Constants.TAG, "iterator: " +iterator);
                                        //Log.d(Constants.TAG, "ratingList: " +ratingList.size());

                                        if(isReachedLastItemInLoop_int==(ratingList.size())) {
                                            if ( sorted_OrdersList.size() > 0) {
                                                showProgressBar(true);
                                                FilterDatawithVendorKey(vendorKey);
                                                //calculateRatingPercentage();
                                            }
                                            else{
                                                //Log.d(Constants.TAG, "iterator: " +iterator);
                                                //Log.d(Constants.TAG, "sorted_OrdersList: " +sorted_OrdersList.size());
                                                showProgressBar(false);
                                                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant get OrderDetails", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                        else{
                                            //Log.d(Constants.TAG, "iterator: 1  " +iterator);
                                            //Log.d(Constants.TAG, "sorted_OrdersList: 1  " +sorted_OrdersList.size());

                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    showProgressBar(false);
                                    Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                showProgressBar(false);
                                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();

                            }

                            // orderTrackingDetailstable_Object = getOrderTrackingDetailsFromOrderDetailsTable(orderId,modal_orderDetails);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    ////Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                    ////Log.d(Constants.TAG, "Error: " + error.toString());
                    showProgressBar(false);
                    Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();

                    error.printStackTrace();
                }
            }) {
                @NonNull
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    final Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Make the request
            Volley.newRequestQueue(DatewiseRatingreport_FirstScreen.this).add(jsonObjectRequest);


        }









    }


    private void calculateRatingPercentage() {


        itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0"; totalrating_String="0";
        itemrating_double=0; qualityrating_double=0; deliveryrating_double=0; totalitemrating_double=0; totalqualityrating_double=0; totaldeliveryrating_double=0;totalrating_double=0;
        totalNo_of_stars_expected=0;no_stars_given_in_percentage=0;no_of_stars_recieved_for_the_service=0;

        showProgressBar(true);

        //Log.d(Constants.TAG, "ratingList Response: " + ratingList.size());

        //Log.d(Constants.TAG, "sorted_ratingList Response: " + sorted_ratingList.size());
       // String itemrating_String="0", qualityrating_String="0", deliveryrating_String="0", totalrating_String="0";
       // double itemrating_double=0, qualityrating_double=0, deliveryrating_double=0, totalitemrating_double=0, totalqualityrating_double=0, totaldeliveryrating_double=0,totalrating_double=0;
       // double totalNo_of_stars_expected = 0, totalNo_of_stars_get = 0,no_stars_given_in_percentage=0,no_of_stars_recieved_for_the_service;

        try{
            for(int iterator =0;iterator<sorted_ratingList.size();iterator++){
                Modal_RatingOrderDetails modal_ratingOrderDetails = sorted_ratingList.get(iterator);



                                try{
                                            String deliveryRating = modal_ratingOrderDetails.getDeliveryrating();
                                            if(deliveryRating.equals("")||deliveryRating.equals("null")||deliveryRating.equals(null)){
                                                deliveryRating = "0";
                                            }

                                            deliveryRatingArray.add(deliveryRating);
                                            deliveryrating_double = Double.parseDouble(deliveryRating);




                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.deliveryrating  = "";
                                        e.printStackTrace();
                                    }
                                    try{
                                            String qualityrating = modal_ratingOrderDetails.getQualityrating();
                                            if(qualityrating.equals("")||qualityrating.equals("null")||qualityrating.equals(null)){
                                                qualityrating = "0";
                                            }

                                            qualityRatingArray.add(qualityrating);

                                            qualityrating_double = Double.parseDouble(qualityrating);


                                    }
                                    catch(Exception e){
                                        modal_ratingOrderDetails.qualityrating  = "";
                                        e.printStackTrace();
                                    }


                                    try{

                                        if(itemrating_double>0){
                                            totalrating_double = (itemrating_double+qualityrating_double+deliveryrating_double);
                                            totalrating_double = totalrating_double*100;
                                            totalrating_double = totalrating_double/15;
                                        }
                                        else{
                                            totalrating_double = (itemrating_double+qualityrating_double+deliveryrating_double);
                                            totalrating_double = totalrating_double*100;
                                            totalrating_double = totalrating_double/10;
                                        }

                                        try{
                                            totalrating_double = totalrating_double*5;
                                            totalrating_double = totalrating_double/100;
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                            modal_ratingOrderDetails.totalratingForthisOrder = String.valueOf(totalrating_double);
                                        try{
                                            for(int j =0; j< RatingArray.size();j++){
                                                double ratingArray_value = 0;
                                                ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                                                if(ratingArray_value==Math.round(totalrating_double)){
                                                    boolean isAvailableinHashmap = totalRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                                                    if(isAvailableinHashmap) {
                                                        double totalRatingHashmap_value = Double.parseDouble(totalRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                                        totalRatingHashmap_value = totalRatingHashmap_value+1;
                                                        totalRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(totalRatingHashmap_value));
                                                    }
                                                    else{
                                                        totalRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(1));

                                                    }
                                                }
                                            }


                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    catch(Exception e){
                                        e.printStackTrace();
                                    }





                try{
                    totalrating_double = Double.parseDouble(modal_ratingOrderDetails.getTotalratingForthisOrder().toString())+totalrating_double;


                }
                catch(Exception e){
                    e.printStackTrace();
                }

                try{

                    totalNo_of_stars_expected = (sorted_ratingList.size()*5);
                    ratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                    quantityratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                   deliveryratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    no_stars_given_in_percentage = totalrating_double*100;
                    no_stars_given_in_percentage = no_stars_given_in_percentage/totalNo_of_stars_expected;
                }
                catch (Exception e){
                    e.printStackTrace();
                }


                try{
                    no_of_stars_recieved_for_the_service = (no_stars_given_in_percentage/100);
                    no_of_stars_recieved_for_the_service = no_of_stars_recieved_for_the_service*5;
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    wholeRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                SetProgressBarAndCount("ForTotalRating");





            }

        }
        catch(Exception e){

            showProgressBar(false);
            Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progree bar for total rating", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try{
             itemrating_String="0"; qualityrating_String="0"; deliveryrating_String="0"; totalrating_String="0";
             itemrating_double=0; qualityrating_double=0; deliveryrating_double=0;totalitemrating_double=0; totalqualityrating_double=0;totaldeliveryrating_double=0;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {

            for (int i = 0; i < itemRatingarray.size(); i++) {
                itemrating_String = itemRatingarray.get(i).toString();
                try {
                    itemrating_double = Double.parseDouble(itemrating_String);
                    totalitemrating_double = itemrating_double + totalitemrating_double;


                } catch (Exception e) {
                    e.printStackTrace();
                }

                try{
                    for(int j =0; j< RatingArray.size();j++){
                        double ratingArray_value = 0;
                        ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                        if(ratingArray_value==itemrating_double){
                            if(itemRatingHashmap.containsKey(String.valueOf(ratingArray_value))){
                                double itemratinghashmap_value = Double.parseDouble(itemRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                itemratinghashmap_value = itemratinghashmap_value+1;
                                itemRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(itemratinghashmap_value));
                            }
                            else{
                                itemRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(1));

                            }
                        }
                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }




            }




            for (int i = 0; i < qualityRatingArray.size(); i++) {
                qualityrating_String = qualityRatingArray.get(i).toString();

                try {
                    qualityrating_double = Double.parseDouble(qualityrating_String);
                    totalqualityrating_double =  qualityrating_double + totalqualityrating_double;
                } catch (Exception e) {
                    e.printStackTrace();
                }





                try{
                    for(int j =0; j< RatingArray.size();j++){
                        double ratingArray_value = 0;
                        ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                        if(ratingArray_value==qualityrating_double){
                            if(qualityRatingHashmap.containsKey(String.valueOf(ratingArray_value))){
                                double itemratinghashmap_value = Double.parseDouble(qualityRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                itemratinghashmap_value = itemratinghashmap_value+1;
                                qualityRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(itemratinghashmap_value));
                            }
                            else{
                                qualityRatingHashmap.put(String.valueOf(ratingArray_value),String.valueOf(1));

                            }
                        }
                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }






            }
            try{
                no_stars_given_in_percentage = 0;
                no_stars_given_in_percentage = totalqualityrating_double*100;
                no_stars_given_in_percentage = no_stars_given_in_percentage/totalNo_of_stars_expected;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                no_of_stars_recieved_for_the_service =0;
                no_of_stars_recieved_for_the_service = (no_stars_given_in_percentage/100);
                no_of_stars_recieved_for_the_service = no_of_stars_recieved_for_the_service*5;
                if(String.valueOf(no_of_stars_recieved_for_the_service).equals("NaN")){
                    no_of_stars_recieved_for_the_service =0;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                quantityRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
            }
            catch (Exception e){
                e.printStackTrace();
            }





            SetProgressBarAndCount("ForQualityRating");









            for (int i = 0; i < deliveryRatingArray.size(); i++) {
                deliveryrating_String = deliveryRatingArray.get(i).toString();

                try {
                    deliveryrating_double = Double.parseDouble(deliveryrating_String);
                    totaldeliveryrating_double = deliveryrating_double + totaldeliveryrating_double;

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    for (int j = 0; j < RatingArray.size(); j++) {
                        double ratingArray_value = 0;
                        ratingArray_value = Double.parseDouble(RatingArray.get(j).toString());
                        if (ratingArray_value == deliveryrating_double) {
                            if (deliveryRatingHashmap.containsKey(String.valueOf(ratingArray_value))) {
                                double itemratinghashmap_value = Double.parseDouble(deliveryRatingHashmap.get(String.valueOf(ratingArray_value)).toString());
                                itemratinghashmap_value = itemratinghashmap_value + 1;
                                deliveryRatingHashmap.put(String.valueOf(ratingArray_value), String.valueOf(itemratinghashmap_value));
                            } else {
                                deliveryRatingHashmap.put(String.valueOf(ratingArray_value), String.valueOf(1));

                            }
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            try{
                no_stars_given_in_percentage = 0;
                no_stars_given_in_percentage = totaldeliveryrating_double*100;
                no_stars_given_in_percentage = no_stars_given_in_percentage/totalNo_of_stars_expected;
            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                no_of_stars_recieved_for_the_service =0;
                no_of_stars_recieved_for_the_service = (no_stars_given_in_percentage/100);
                no_of_stars_recieved_for_the_service = no_of_stars_recieved_for_the_service*5;
                if(String.valueOf(no_of_stars_recieved_for_the_service).equals("NaN")){
                    no_of_stars_recieved_for_the_service =0;
                }
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    deliveryRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
            }
            catch (Exception e){
                e.printStackTrace();
            }


            SetProgressBarAndCount("ForDeliveryRating");










            showProgressBar(false);


        }
        catch (Exception e){
            showProgressBar(false);
            Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for quality/delivery rating", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }


    }

    private void SetProgressBarAndCount(String RateType) {
        if(RateType.equals("ForTotalRating")){
            try{
                for(int i =0 ; i<RatingArray.size();i++){
                    double ratingArray_value = 0;
                    ratingArray_value = Double.parseDouble(RatingArray.get(i).toString());
                    boolean isAvailableinHashmap = totalRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                    if(isAvailableinHashmap) {
                        String totalratinghashmapValue = "0";
                        totalratinghashmapValue = totalRatingHashmap.get(String.valueOf(ratingArray_value));

                        if (ratingArray_value == 1) {
                            progress_rating1.setMax((int) (sorted_ratingList.size()));
                            progress_rating1.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating1_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));
                        } else if (ratingArray_value == 2) {
                            progress_rating2.setMax((int) (sorted_ratingList.size()));
                            progress_rating2.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating2_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 3) {
                            progress_rating3.setMax((int) (sorted_ratingList.size()));
                            progress_rating3.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating3_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 4) {
                            progress_rating4.setMax((int) (sorted_ratingList.size()));
                            progress_rating4.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating4_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));

                        } else if (ratingArray_value == 5) {
                            progress_rating5.setMax((int) (sorted_ratingList.size()));
                            progress_rating5.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            rating5_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        }
                    }
                    else{
                        //String totalratinghashmapValue =String.valueOf(ratingArray_value);

                    }





                }
            }
            catch (Exception e){
                e.printStackTrace();
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

            }
            showProgressBar(false);

        }
        else if(RateType.equals("ForQualityRating")){
            try{
                for(int i =0 ; i<RatingArray.size();i++){
                    double ratingArray_value = 0;
                    ratingArray_value = Double.parseDouble(RatingArray.get(i).toString());
                    boolean isAvailableinHashmap = qualityRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                    if(isAvailableinHashmap) {
                        String totalratinghashmapValue = "0";
                        totalratinghashmapValue = qualityRatingHashmap.get(String.valueOf(ratingArray_value));

                        if (ratingArray_value == 1) {
                            quantityratingprogressbar1.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar1.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating1_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));
                        } else if (ratingArray_value == 2) {
                            quantityratingprogressbar2.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar2.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating2_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 3) {
                            quantityratingprogressbar3.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar3.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating3_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 4) {
                            quantityratingprogressbar4.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar4.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating4_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));

                        } else if (ratingArray_value == 5) {
                            quantityratingprogressbar5.setMax((int) (sorted_ratingList.size()));
                            quantityratingprogressbar5.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            quantityrating5_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        }
                    }
                    else{
                        //String totalratinghashmapValue =String.valueOf(ratingArray_value);

                    }





                }
            }
            catch (Exception e){
                e.printStackTrace();
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

            }
            showProgressBar(false);

        }
        else if(RateType.equals("ForDeliveryRating")){
            try{
                for(int i =0 ; i<RatingArray.size();i++){
                    double ratingArray_value = 0;
                    ratingArray_value = Double.parseDouble(RatingArray.get(i).toString());
                    boolean isAvailableinHashmap = deliveryRatingHashmap.containsKey(String.valueOf(ratingArray_value));
                    if(isAvailableinHashmap) {
                        String totalratinghashmapValue = "0";
                        totalratinghashmapValue = deliveryRatingHashmap.get(String.valueOf(ratingArray_value));

                        if (ratingArray_value == 1) {
                            deliveryratingprogressbar1.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar1.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating1_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));
                        } else if (ratingArray_value == 2) {
                            deliveryratingprogressbar2.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar2.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating2_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 3) {
                            deliveryratingprogressbar3.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar3.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating3_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        } else if (ratingArray_value == 4) {
                            deliveryratingprogressbar4.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar4.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating4_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));

                        } else if (ratingArray_value == 5) {
                            deliveryratingprogressbar5.setMax((int) (sorted_ratingList.size()));
                            deliveryratingprogressbar5.setProgress((int) Double.parseDouble(totalratinghashmapValue));
                            deliveryrating5_count.setText(String.valueOf((int) Double.parseDouble(totalratinghashmapValue)));


                        }
                    }
                    else{
                        //String totalratinghashmapValue =String.valueOf(ratingArray_value);

                    }





                }
            }
            catch (Exception e){
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
            showProgressBar(false);

        }
        else if(RateType.equals("MakeZero")){
            try{

                no_of_stars_recieved_for_the_service =0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                wholeRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
                quantityRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));
                deliveryRating_textview.setText(String.valueOf(decimalFormat.format(no_of_stars_recieved_for_the_service)));




                ratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                quantityratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));
                deliveryratingsCount_textview.setText(String.valueOf(sorted_ratingList.size()));

                progress_rating1.setMax((int) (sorted_ratingList.size()));
                progress_rating1.setProgress((int) Double.parseDouble("0.00"));
                rating1_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


               progress_rating2.setMax((int) (sorted_ratingList.size()));
               progress_rating2.setProgress((int) Double.parseDouble("0.00"));
               rating2_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                progress_rating3.setMax((int) (sorted_ratingList.size()));
                progress_rating3.setProgress((int) Double.parseDouble("0.00"));
                rating3_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                progress_rating4.setMax((int) (sorted_ratingList.size()));
                progress_rating4.setProgress((int) Double.parseDouble("0.00"));
                rating4_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                progress_rating5.setMax((int) (sorted_ratingList.size()));
                progress_rating5.setProgress((int) Double.parseDouble("0.00"));
                rating5_count.setText(String.valueOf((int) Double.parseDouble("0.00")));





                quantityratingprogressbar1.setMax((int) (sorted_ratingList.size()));
               quantityratingprogressbar1.setProgress((int) Double.parseDouble("0.00"));
               quantityrating1_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


               quantityratingprogressbar2.setMax((int) (sorted_ratingList.size()));
               quantityratingprogressbar2.setProgress((int) Double.parseDouble("0.00"));
               quantityrating2_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


               quantityratingprogressbar3.setMax((int) (sorted_ratingList.size()));
               quantityratingprogressbar3.setProgress((int) Double.parseDouble("0.00"));
               quantityrating3_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

               quantityratingprogressbar4.setMax((int) (sorted_ratingList.size()));
               quantityratingprogressbar4.setProgress((int) Double.parseDouble("0.00"));
               quantityrating4_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

               quantityratingprogressbar5.setMax((int) (sorted_ratingList.size()));
               quantityratingprogressbar5.setProgress((int) Double.parseDouble("0.00"));
               quantityrating5_count.setText(String.valueOf((int) Double.parseDouble("0.00")));







                deliveryratingprogressbar1.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar1.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating1_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                deliveryratingprogressbar2.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar2.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating2_count.setText(String.valueOf((int) Double.parseDouble("0.00")));


                deliveryratingprogressbar3.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar3.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating3_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                deliveryratingprogressbar4.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar4.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating4_count.setText(String.valueOf((int) Double.parseDouble("0.00")));

                deliveryratingprogressbar5.setMax((int) (sorted_ratingList.size()));
                deliveryratingprogressbar5.setProgress((int) Double.parseDouble("0.00"));
                deliveryrating5_count.setText(String.valueOf((int) Double.parseDouble("0.00")));










            }
            catch (Exception e){
                showProgressBar(false);
                Toast.makeText(DatewiseRatingreport_FirstScreen.this, "Cant Set Progress bar for total rating", Toast.LENGTH_LONG).show();

                e.printStackTrace();
            }
        }
        showProgressBar(false);

    }










    */


    private String getTomorrowsDate(String datestring) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, 1);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  tomorrowdayDate = df1.format(c1);
        String tomorrowAsString = PreviousdayDay+", "+tomorrowdayDate;
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return tomorrowAsString;

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









    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
           // manageOrders_ListView.setVisibility(View.GONE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
          //  manageOrders_ListView.setVisibility(View.VISIBLE);
        }

    }


    private void calculate_the_dateandgetData(String fromdateString, String toDateString) {

        showProgressBar(true);

        String previousday,nextday;
        previousday =  getDatewithNameofthePreviousDayfromSelectedDay2(fromdateString);
        getRatingDetailsUsingDateAndVendorKey(previousday, fromdateString, vendorKey);

    }



    private String getDatewithNameofthePreviousDayfromSelectedDay(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, -1);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }






    private String getDatewithNameoftheseventhDayFromSelectedStartDate(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, 6);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
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





    private String getDatewithNameofthePreviousDayfromSelectedDay2(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, -1);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }



    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);


        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        PreviousdayDate = PreviousdayDay+", "+PreviousdayDate;
        // System.out.println("todays Date  " + CurrentDate);
        System.out.println("PreviousdayDate Date  " + PreviousdayDate);


        return PreviousdayDate;
    }

    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        String CurrentDay = day.format(c);



        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String  CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;


        //CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }


}





/*
    class GetItemdetailsfromOrderDetailsNew extends AsyncTask<String, Void, Void>
    {

        boolean isCompleted = false;
        String orderid ="";
        public GetItemdetailsfromOrderDetailsNew(String Orderid) {
            this.orderid = Orderid;
        }
        @Override
        protected void onPreExecute() {
            // Here you can show progress bar or something on the similar lines.
            // Since you are in a UI thread here.
            showProgressBar(true);
            Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
            for(int i=0;i<ratingList.size();i++)    {
                Modal_RatingOrderDetails modal_ratingOrderDetails = ratingList.get(i);
                String orderId = modal_ratingOrderDetails.getOrderid();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsusingOrderid + orderId, null,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(@NonNull JSONObject response) {
                                try {
                                    //Log.d(Constants.TAG, "GETADDRESS Response: " + response);

                                    try {
                                        itemnamefromOrderDetails = "";
                                        String ordertype = "#";

                                        //converting jsonSTRING into array
                                        JSONArray JArray = response.getJSONArray("content");
                                        ////Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                        int i1 = 0;
                                        int arrayLength = JArray.length();
                                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                        if (arrayLength > 1) {
                                            Toast.makeText(DatewiseRatingreport_FirstScreen.this, "This orderid Have more than 1 orders", Toast.LENGTH_LONG).show();

                                        }

                                        for (; i1 < (arrayLength); i1++) {
                                            try {
                                                JSONObject json = JArray.getJSONObject(i1);

                                                try {
                                                    if (json.has("coupondiscount")) {
                                                        modal_orderDetails.coupondiscount = json.getString("coupondiscount");
                                                    } else {
                                                        modal_orderDetails.coupondiscount = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.coupondiscount = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("couponkey")) {
                                                        modal_orderDetails.couponkey = json.getString("couponkey");
                                                    } else {
                                                        modal_orderDetails.couponkey = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.couponkey = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("deliveryamount")) {
                                                        modal_orderDetails.deliveryamount = json.getString("deliveryamount");
                                                    } else {
                                                        modal_orderDetails.deliveryamount = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.deliveryamount = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("deliverytype")) {
                                                        modal_orderDetails.deliverytype = json.getString("deliverytype");
                                                    } else {
                                                        modal_orderDetails.deliverytype = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.deliverytype = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("gstamount")) {
                                                        modal_orderDetails.gstamount = json.getString("gstamount");
                                                    } else {
                                                        modal_orderDetails.gstamount = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.gstamount = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("itemdesp")) {
                                                        JSONArray itemdesp = json.getJSONArray("itemdesp");

                                                        modal_orderDetails.itemdesp = itemdesp;


                                                        for (int i = 0; i < itemdesp.length(); i++) {
                                                            ////Log.d(Constants.TAG, "this  jsonArray.length()" +jsonArray.length());

                                                            JSONObject itemdespjson = itemdesp.getJSONObject(i);
                                                            try {
                                                                if (itemdespjson.has("marinadeitemdesp")) {
                                                                    JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                                                                    if (itemnamefromOrderDetails.length() > 0) {
                                                                        itemnamefromOrderDetails = itemnamefromOrderDetails + " , ";

                                                                    } else {
                                                                        itemnamefromOrderDetails = String.valueOf(marinadesObject.get("itemname"));
                                                                    }
                                                                    modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }


                                                            try {

                                                                if (itemnamefromOrderDetails.length() > 0) {
                                                                    itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf(itemdespjson.get("itemname"));

                                                                } else {
                                                                    itemnamefromOrderDetails = String.valueOf(itemdespjson.get("itemname"));
                                                                }
                                                                modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                            } catch (Exception e) {
                                                                if (itemnamefromOrderDetails.length() > 0) {
                                                                    itemnamefromOrderDetails = itemnamefromOrderDetails + " , " + String.valueOf("");

                                                                } else {
                                                                    itemnamefromOrderDetails = String.valueOf((""));
                                                                }
                                                                modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                                e.printStackTrace();
                                                            }


                                                        }

                                                    } else {

                                                        itemnamefromOrderDetails = String.valueOf((""));
                                                        modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    itemnamefromOrderDetails = String.valueOf((""));
                                                    modal_ratingOrderDetails.itemname = itemnamefromOrderDetails;
                                                }

                                                try {
                                                    if (json.has("key")) {
                                                        modal_orderDetails.orderdetailskey = json.getString("key");
                                                    } else {
                                                        modal_orderDetails.orderdetailskey = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.orderdetailskey = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("notes")) {
                                                        modal_orderDetails.notes = json.getString("notes");
                                                    } else {
                                                        modal_orderDetails.notes = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.notes = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("orderid")) {
                                                        modal_orderDetails.orderid = json.getString("orderid");
                                                    } else {
                                                        modal_orderDetails.orderid = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.orderid = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("orderplaceddate")) {
                                                        modal_orderDetails.orderplaceddate = json.getString("orderplaceddate");
                                                    } else {
                                                        modal_orderDetails.orderplaceddate = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.orderplaceddate = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("orderplacedtime")) {
                                                        modal_orderDetails.orderplacedtime = json.getString("orderplacedtime");
                                                    } else {
                                                        modal_orderDetails.orderplacedtime = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.orderplacedtime = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("ordertype")) {
                                                        modal_orderDetails.ordertype = json.getString("ordertype");
                                                    } else {
                                                        modal_orderDetails.ordertype = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.ordertype = "-";
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    if (json.has("payableamount")) {
                                                        modal_orderDetails.payableamount = json.getString("payableamount");
                                                    } else {
                                                        modal_orderDetails.payableamount = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.payableamount = "-";
                                                    e.printStackTrace();
                                                }


                                                try {

                                                    if (json.has("paymentmode")) {
                                                        modal_orderDetails.paymentmode = json.getString("paymentmode");
                                                    } else {
                                                        modal_orderDetails.paymentmode = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.paymentmode = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("slotdate")) {
                                                        modal_orderDetails.slotdate = json.getString("slotdate");
                                                    } else {
                                                        modal_orderDetails.slotdate = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.slotdate = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("slotname")) {
                                                        modal_orderDetails.slotname = json.getString("slotname");
                                                    } else {
                                                        modal_orderDetails.slotname = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.slotname = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("slottimerange")) {
                                                        modal_orderDetails.slottimerange = json.getString("slottimerange");
                                                    } else {
                                                        modal_orderDetails.slottimerange = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.slottimerange = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("tokenno")) {
                                                        modal_orderDetails.tokenno = json.getString("tokenno");
                                                    } else {
                                                        modal_orderDetails.tokenno = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.tokenno = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("useraddress")) {
                                                        modal_orderDetails.useraddress = json.getString("useraddress");
                                                    } else {
                                                        modal_orderDetails.useraddress = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.useraddress = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("userkey")) {
                                                        modal_orderDetails.userkey = json.getString("userkey");
                                                    } else {
                                                        modal_orderDetails.userkey = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.userkey = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("useraddress")) {
                                                        modal_orderDetails.useraddress = json.getString("useraddress");
                                                    } else {
                                                        modal_orderDetails.useraddress = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.useraddress = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("usermobile")) {
                                                        modal_orderDetails.usermobile = json.getString("usermobile");
                                                    } else {
                                                        modal_orderDetails.usermobile = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.usermobile = "-";
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    if (json.has("vendorkey")) {
                                                        modal_orderDetails.vendorkey = json.getString("vendorkey");
                                                    } else {
                                                        modal_orderDetails.vendorkey = "-";
                                                    }
                                                } catch (Exception e) {
                                                    modal_orderDetails.vendorkey = "-";
                                                    e.printStackTrace();
                                                }


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();


                                }

                                // orderTrackingDetailstable_Object = getOrderTrackingDetailsFromOrderDetailsTable(orderId,modal_orderDetails);


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {
                        ////Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                        ////Log.d(Constants.TAG, "Error: " + error.getMessage());
                        ////Log.d(Constants.TAG, "Error: " + error.toString());


                        error.printStackTrace();
                    }
                }) {
                    @NonNull
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        final Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }
                };
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                // Make the request
                Volley.newRequestQueue(DatewiseRatingreport_FirstScreen.this).add(jsonObjectRequest);

                if(i==(ratingList.size()-1)){
                    isCompleted = true;
                }
            }


            if(isCompleted){
                calculateRatingPercentage();

            }

            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(String... params) {



            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

        }

    }


 */
