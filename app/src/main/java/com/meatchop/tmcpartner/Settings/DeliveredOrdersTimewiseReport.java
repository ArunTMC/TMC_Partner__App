package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;

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

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;
import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlots;

public class DeliveredOrdersTimewiseReport extends AppCompatActivity {
    TextView order_fetching_instruction,fetchData,count_AllOrders, count_onTimeDeliveryOrders, count_30MinsLateOrders, count_60MinsLateOrders, count_moreThan60MinsLateOrders, appOrdersCount_textwidget, dateSelector_text, mobile_orderinstruction, mobile_nameofFacility_Textview;
    LinearLayout generateSlotwise_Report_Layout,listview_Layout, timeslotButtonss_layout, show_allOrders, show_onTimeDeliveryOrders, show_30MinsLateOrders, show_60MinsLateOrders, show_moreThan60MinsLateOrders;
    Button mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget;
    ImageView mobile_search_button, mobile_search_close_btn, applaunchimage;
    EditText mobile_search_barEditText;
    String mobile_jsonString, orderStatus, vendorKey, vendorname, TAG = "Tag";
    String DateString, PreviousDateString, delayedtime = "0",delayedtime1= "0";
    ListView manageOrders_ListView;
    public LinearLayout PrintReport_Layout;
    public LinearLayout generateReport_Layout;
    public LinearLayout dateSelectorLayout;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;
    DatePickerDialog datepicker;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    public static String completemenuItem;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    String Currenttime, FormattedTime, CurrentDate, formattedDate, CurrentDay, TodaysDate, DeliveryPersonList;
    Spinner slotType_Spinner;
    int slottypefromSpinner = 0;
    static Adapter_Delivered_Orders_TimewiseReport adapter_delivered_orders_timewiseReport;
    //  static Adapter_Pos_SearchOrders_usingMobileNumber adapter_PosSearchOrders_usingMobileNumber_listView;
    Workbook wb;
    Sheet sheet = null;
    private static String[] columns = {"Delivery Type", "Token No", "Order Status",
            "Order Placed Time", "Slot Date", "Slot Time Range", "Order Ready Time", "Order Delivered Time ", "Delayed Time ","Orderid", "User Address", "User Mobile"};

    private String SERVER_PATH = "wss://hx9itd7ji2.execute-api.ap-south-1.amazonaws.com/Dev";
    WebSocket webSocket;
    private Context mContext;
    int delayedTimeIntt = 0;

    boolean isSearchButtonClicked = false;
    double screenInches;
    String portName = "USB";
    int portSettings = 0, totalGstAmount = 0;
    public static List<String> array_of_orderId;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    Spinner deliverytimeSelector_spinner;
    boolean isdelayed,isPDF_FIle = false;
    List<String> deliveredtimeArray;
    List<String> slotrangetimeArray = new ArrayList<>();
    String deliveryTimeForExpr_Delivery ="" ;
    HashMap<String,HashMap<String,Integer>> hashMap_sorted_OrdersList = new HashMap<>();

    String selecteddeliveredtime_spinner = "All";
    int spinner_check = 0;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    boolean orderdetailsnewschema = false;
    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean isVendorOrdersTableServiceCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered_orders_timewise_report);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();


        deliveryPartnerList = new ArrayList<>();
        deliverytimeSelector_spinner = findViewById(R.id.deliverytimeSelector_spinner);
        try {
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", ""));
            vendorname = (shared.getString("VendorName", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
            // orderdetailsnewschema = true;


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(DeliveredOrdersTimewiseReport.this);
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
               // Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }
        deliveredtimeArray = new ArrayList<>();


        //
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_orderId = new ArrayList<>();
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        order_fetching_instruction  = findViewById(R.id.order_fetching_instruction);
        slotType_Spinner = findViewById(R.id.slotType_Spinner);
        manageOrders_ListView = findViewById(R.id.manageOrders_ListView);
        mobile_orderinstruction = findViewById(R.id.orderinstruction);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        generateReport_Layout = findViewById(R.id.generatedelayedTimewiseReport_Layout);
        timeslotButtonss_layout = findViewById(R.id.timeslotButtonss_layout);
        listview_Layout = findViewById(R.id.listview_Layout);
        generateSlotwise_Report_Layout = findViewById(R.id.generateSlotwise_Report_Layout);
        //
        mobile_nameofFacility_Textview = findViewById(R.id.nameofFacility_Textview);
        mobile_search_button = findViewById(R.id.search_button);
        mobile_search_barEditText = findViewById(R.id.search_barEdit);
        mobile_search_close_btn = findViewById(R.id.search_close_btn);
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);
        show_allOrders = findViewById(R.id.show_allOrders);
        show_onTimeDeliveryOrders = findViewById(R.id.show_onTimeDeliveryOrders);
        show_30MinsLateOrders = findViewById(R.id.show_30MinsLateOrders);
        show_60MinsLateOrders = findViewById(R.id.show_60MinsLateOrders);
        show_moreThan60MinsLateOrders = findViewById(R.id.show_moreThan60MinsLateOrders);
        fetchData = findViewById(R.id.fetchData);
        count_AllOrders = findViewById(R.id.count_AllOrders);
        count_onTimeDeliveryOrders = findViewById(R.id.count_onTimeDeliveryOrders);
        count_30MinsLateOrders = findViewById(R.id.count_30MinsLateOrders);
        count_60MinsLateOrders = findViewById(R.id.count_60MinsLateOrders);
        count_moreThan60MinsLateOrders = findViewById(R.id.count_moreThan60MinsLateOrders);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        getslotTimeFromDB();
        setDataForSpinner();
        mobile_nameofFacility_Textview.setText(vendorname);


        try {
           // TodaysDate = getDate();
           // PreviousDateString = getDatewithNameofthePreviousDay();
            //Now we are creating sheet

          //  Adjusting_Widgets_Visibility(true);
         //   String Todaysdate = getDatewithNameoftheDay();
         //   PreviousDateString = getDatewithNameofthePreviousDay();

            isSearchButtonClicked = false;
            orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;
            ordersList.clear();
            sorted_OrdersList.clear();
            array_of_orderId.clear();
            selecteddeliveredtime_spinner = "All";
           // dateSelector_text.setText(Todaysdate);

            dateSelector_text.setText(Constants.Empty_Date_Format);
            DateString = (Constants.Empty_Date_Format);
            order_fetching_instruction.setText("# Please Select the Date and Click Fetch Data Button");
        } catch (Exception e) {
            e.printStackTrace();
        }


        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();
                order_fetching_instruction.setText("# To Fetch Date for Same Date Click Fetch Button again / ");
                if (DateString.equals(Constants.Empty_Date_Format)) {
                    Toast.makeText(DeliveredOrdersTimewiseReport.this, "Select the Date First !!! ", Toast.LENGTH_SHORT).show();
                } else {
                    if (orderdetailsnewschema) {
                        String dateAsOldFormat = convertOldFormatDateintoNewFormat(DateString);
                       // dateSelector_text.setText(dateAsOldFormat);
                        callVendorOrderDetailsSeviceAndInitCallBack(dateAsOldFormat, dateAsOldFormat, vendorKey);


                    } else {
                       // dateSelector_text.setText(DateString);
                        PreviousDateString = getDatewithNameofthePreviousDay2(DateString);

                        getOrderDetailsUsingOrderSlotDate(PreviousDateString, DateString, vendorKey, orderStatus);

                    }
                }
            }
        });

        deliverytimeSelector_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (spinner_check > 1) {
                    selecteddeliveredtime_spinner = deliverytimeSelector_spinner.getSelectedItem().toString();
                    displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
                }
                spinner_check = 2;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        generateSlotwise_Report_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (SDK_INT >= Build.VERSION_CODES.R) {

                        if (Environment.isExternalStorageManager()) {
                            try {

                                prepareDataForPDF();

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


                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(DeliveredOrdersTimewiseReport.this, WRITE_EXTERNAL_STORAGE);
                        //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(DeliveredOrdersTimewiseReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                        } else {
                            Adjusting_Widgets_Visibility(true);
                            try {
                                prepareDataForPDF();


                            } catch (Exception e) {
                                e.printStackTrace();
                                ;
                            }
                        }
                    }


                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
/*
        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();

                Adjusting_Widgets_Visibility(true);
                String Todaysdate = dateSelector_text.getText().toString();
                PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(Todaysdate);

                isSearchButtonClicked = false;
                orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;

                if(orderdetailsnewschema){
                    String dateAsNEWFormat =convertOldFormatDateintoNewFormat(Todaysdate);
                    callVendorOrderDetailsSeviceAndInitCallBack(dateAsNEWFormat,dateAsNEWFormat,vendorKey);


                }
                else{
                    dateSelector_text.setText(Todaysdate);

                    getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);

                }




               // getOrderDetailsUsingOrderSlotDate(PreviousDateString, Todaysdate, vendorKey, orderStatus);

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
                showslotbuttons(true);
                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = mobile_search_barEditText.getText().toString().length();
                isSearchButtonClicked = true;
                showslotbuttons(false);
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
                Toast.makeText(DeliveredOrdersTimewiseReport.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });
       // generateReport_Layout.setVisibility(View.GONE);
        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Adjusting_Widgets_Visibility(true);

           //     AddDatatoExcelSheet(sorted_OrdersList, type, generateSheet);
                try {
                    wb = new HSSFWorkbook();
                    //Now we are creating sheet




                } catch (Exception e) {
                    e.printStackTrace();
                }
                prepareDataForExcelSheet();
            }
        });

        show_allOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showslotbuttons(false);
                selecteddeliveredtime_spinner = "All";
                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
            }
        });

        show_onTimeDeliveryOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showslotbuttons(false);

                selecteddeliveredtime_spinner = "On Time Delivery ";
                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
            }
        });
        show_30MinsLateOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showslotbuttons(false);

                selecteddeliveredtime_spinner = "0 - 30 Mins Late ";
                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
            }
        });
        show_60MinsLateOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showslotbuttons(false);

                selecteddeliveredtime_spinner = "30 - 60 Mins Late ";
                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
            }
        });
        show_moreThan60MinsLateOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showslotbuttons(false);

                selecteddeliveredtime_spinner = " > 60 Mins Late ";
                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
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
                isSearchButtonClicked = true;

                String mobileNo = (editable.toString());
                if (!mobileNo.equals("")) {
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
                                modal_manageOrders_forOrderDetailList1.slottime_in_long = modal_manageOrders_forOrderDetailList.getSlottime_in_long();

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
                            if (screenInches > 8) {

                                adapter_delivered_orders_timewiseReport = new Adapter_Delivered_Orders_TimewiseReport(DeliveredOrdersTimewiseReport.this, sorted_OrdersList, orderstatus);
                                manageOrders_ListView.setAdapter(adapter_delivered_orders_timewiseReport);
                            } else {
                                adapter_delivered_orders_timewiseReport = new Adapter_Delivered_Orders_TimewiseReport(DeliveredOrdersTimewiseReport.this, sorted_OrdersList, orderstatus);
                                manageOrders_ListView.setAdapter(adapter_delivered_orders_timewiseReport);
                            }
                        } else {
                            manageOrders_ListView.setVisibility(View.GONE);
                            mobile_orderinstruction.setVisibility(View.VISIBLE);
                            mobile_orderinstruction.setText("No orders found for this Mobile number");


                        }
                    } catch (Exception E) {
                        E.printStackTrace();
                    }


                } else {
                    manageOrders_ListView.setVisibility(View.GONE);
                    mobile_orderinstruction.setVisibility(View.VISIBLE);
                    mobile_orderinstruction.setText("No orders found for this Mobile number");

                }

            }
        });


    }

    private void getslotTimeFromDB() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlots+"?storeid="+vendorKey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        slotrangetimeArray.add("All");
                        slotrangetimeArray.add(Constants.EXPRESS_DELIVERY_SLOTNAME);
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);

                                String slotName = String.valueOf(json.get("slotname").toString().toUpperCase());
                                String slotdateType = String.valueOf(json.get("slotdatetype"));
                                if(slotdateType.equals("TODAY")) {
                                    if ((slotName.equals(Constants.EXPRESS_DELIVERY_SLOTNAME))||(slotName.equals(Constants.EXPRESSDELIVERY_SLOTNAME))) {
                                        deliveryTimeForExpr_Delivery = String.valueOf(json.get("deliverytime")).toUpperCase();

                                    }
                                    else {
                                        String slotEndTime = String.valueOf(json.get("slotendtime"));
                                        String slotStartTime = String.valueOf(json.get("slotstarttime"));
                                        String timeRange = slotStartTime + " - " + slotEndTime;
                                        if(!timeRange.equals(" - ")) {
                                            slotrangetimeArray.add(timeRange);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());


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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(DeliveredOrdersTimewiseReport.this).add(jsonObjectRequest);





    }

    private void prepareDataForPDF() {

        Collections.sort(slotrangetimeArray, new Comparator<String>() {
            public int compare(final String object1, final String object2) {
                String string_1 = object1;
                String string_2 = object2;


                return string_1.compareTo(string_2);
            }
        });



        hashMap_sorted_OrdersList.clear();
        isPDF_FIle = true;
        Log.d(Constants.TAG, "prepareDataFor PDF Response: " );

        boolean generatePdf = false;

        Log.d(Constants.TAG, "prepareDataFor PDF size: " + deliveredtimeArray.size() );
        Log.d(Constants.TAG, "prepareDataFor PDF ordersList size: " +ordersList.size() );

        for (int j = 0; j< slotrangetimeArray.size(); j++) {
            String slotrangetime = slotrangetimeArray.get(j);
            Log.d(Constants.TAG, "prepareDataFor PDF type: " + slotrangetime);

              HashMap<String, Integer> deliverytypewise_ordersHashmap = new HashMap<>();

            for (int k = 0; k < ordersList.size(); k++) {
                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(k);
                String slotTimeRangeFromOrderDetails = modal_manageOrders_pojo_class.getSlottimerange();
                String slotNameFromOrderDetails = modal_manageOrders_pojo_class.getSlotname();

                if(slotNameFromOrderDetails.equals(Constants.EXPRESS_DELIVERY_SLOTNAME) || slotNameFromOrderDetails.equals(Constants.EXPRESSDELIVERY_SLOTNAME)){
                    slotTimeRangeFromOrderDetails = Constants.EXPRESS_DELIVERY_SLOTNAME;
                }

                String deliverytype = modal_manageOrders_pojo_class.getTypebasedonDeliveredTime();

                if(slotrangetime.equals(slotTimeRangeFromOrderDetails)){
                    if(deliverytypewise_ordersHashmap.containsKey(deliverytype)){
                        int count = deliverytypewise_ordersHashmap.get(deliverytype);
                        count = count +1 ;
                        deliverytypewise_ordersHashmap.put(deliverytype,count);
                    }
                    else{
                        deliverytypewise_ordersHashmap.put(deliverytype,1);
                    }
                }
                if(slotrangetime.equals("All")){
                    if(deliverytypewise_ordersHashmap.containsKey(deliverytype)){
                        int count = deliverytypewise_ordersHashmap.get(deliverytype);
                        count = count +1 ;
                        deliverytypewise_ordersHashmap.put(deliverytype,count);
                    }
                    else{
                        deliverytypewise_ordersHashmap.put(deliverytype,1);
                    }
                }
                try {
                    if (ordersList.size()  - k == 1) {
                        if(hashMap_sorted_OrdersList.containsKey(slotrangetime)){
                            HashMap<String, Integer> deliverytypewise_ordersHashmap_1 = hashMap_sorted_OrdersList.get(slotrangetime);
                            if(deliverytypewise_ordersHashmap_1.containsKey(deliverytype)){
                                int count = deliverytypewise_ordersHashmap_1.get(deliverytype);
                                count = count +1 ;
                                deliverytypewise_ordersHashmap_1.put(deliverytype,count);
                            }
                            else{
                                deliverytypewise_ordersHashmap_1.put(deliverytype,1);
                            }
                        }
                        else{
                            hashMap_sorted_OrdersList.put(slotrangetime, deliverytypewise_ordersHashmap);

                        }


                        /*
                        if(sorted_OrdersHashmap.containsKey(deliverytype)){
                          int count =   sorted_OrdersHashmap.get(deliverytype);
                            count = count + sorted_OrdersList.size();
                            sorted_OrdersHashmap.put(deliverytype, count);

                        }
                        else {
                            sorted_OrdersHashmap.put(deliverytype, sorted_OrdersList.size());
                        }


                         */
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
            if(slotrangetimeArray.size() -j ==1){
                Log.d(Constants.TAG, "prepareDataFor PDF hashMap_sorted_OrdersList: " + hashMap_sorted_OrdersList.size());
                Log.d(Constants.TAG, "prepareDataFor PDF sorted_OrdersList: " + sorted_OrdersList.size());
                Log.d(Constants.TAG, "prepareDataFor PDF slotrangetime: " + slotrangetime);
               // Adjusting_Widgets_Visibility(false);
             //   hashMap_sorted_OrdersList.put(slotrangetime,sorted_OrdersHashmap);
                AddDatatoPDFFile();
            }
        }
        Adjusting_Widgets_Visibility(false);



            }

    private void AddDatatoPDFFile() {
        if ((hashMap_sorted_OrdersList == null)) {
            return;
        }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state "+state+" extstoragedir "+extstoragedir);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();

        File folder = new File(path);
        //  File folder = new File(fol, "pdf");
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "Delivered Orders Slotwise" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                Document layoutDocument = new Document();
                PdfWriter.getInstance(layoutDocument, fOut);
                layoutDocument.open();
                addVendorDetails(layoutDocument);
                addItemRows(layoutDocument);

                layoutDocument.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // document = new PdfDocument(new PdfWriter("MyFirstInvoice.pdf"));


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

            Log.i("error", e.getLocalizedMessage());
        } catch (Exception ex) {
            Adjusting_Widgets_Visibility(false);

            ex.printStackTrace();
        }

    }

    private void addItemRows(Document layoutDocument) {
        RoundRectangle roundRectange = new RoundRectangle();
        if(hashMap_sorted_OrdersList.size()>0){



            PdfPTable inner_table = new PdfPTable(deliveredtimeArray.size()+1);
            inner_table.setHeaderRows(1);
            inner_table.setWidthPercentage(95);

            PdfPTable allSlotinner_table = new PdfPTable(deliveredtimeArray.size()+1);
            allSlotinner_table.setHeaderRows(1);
            allSlotinner_table.setWidthPercentage(95);





            PdfPCell slotrangetimecell = new PdfPCell(new Phrase(""));

            slotrangetimecell.setBorder(Rectangle.NO_BORDER);
            slotrangetimecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            slotrangetimecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            slotrangetimecell.setPaddingLeft(10);
            slotrangetimecell.setFixedHeight(30);
            slotrangetimecell.setBorderWidthBottom(01);
            inner_table.addCell(slotrangetimecell);
            allSlotinner_table.addCell(slotrangetimecell);


            for(int i =0 ;i<deliveredtimeArray.size();i++) {
                String deliveryType = deliveredtimeArray.get(i);
                PdfPCell slotrangetimecel11 = new PdfPCell(new Phrase(deliveryType));
                slotrangetimecel11.setBorder(Rectangle.NO_BORDER);
                slotrangetimecel11.setBackgroundColor(BaseColor.LIGHT_GRAY);
                slotrangetimecel11.setHorizontalAlignment(Element.ALIGN_LEFT);
                slotrangetimecel11.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    if(deliveredtimeArray.size()-i !=1) {
                        slotrangetimecel11.setBorderWidthRight(01);
                    }

                slotrangetimecel11.setFixedHeight(30);
                slotrangetimecel11.setNoWrap(true);
                slotrangetimecel11.setBorderWidthBottom(01);

                inner_table.addCell(slotrangetimecel11);

                allSlotinner_table.addCell(slotrangetimecell);

            }







            for (int j = 0; j< slotrangetimeArray.size(); j++) {
                String slotrangetime = slotrangetimeArray.get(j);
            if(slotrangetime.toString().toUpperCase().equals("ALL")){
                if (hashMap_sorted_OrdersList.containsKey(slotrangetime)) {
                    PdfPCell allslotrangetimecell = new PdfPCell(new Phrase(slotrangetime));
                    allslotrangetimecell.setBorder(Rectangle.NO_BORDER);
                    allslotrangetimecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    allslotrangetimecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    allslotrangetimecell.setPaddingLeft(10);
                    allslotrangetimecell.setPaddingRight(20);
                    allslotrangetimecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    allslotrangetimecell.setBorderWidthBottom(01);
                    allslotrangetimecell.setBorderWidthRight(01);
                    allslotrangetimecell.setFixedHeight(30);
                    allSlotinner_table.addCell(allslotrangetimecell);


                    HashMap<String, Integer> deliverytypewise_ordersHashmap_1 = hashMap_sorted_OrdersList.get(slotrangetime);

                    for (int i = 0; i < deliveredtimeArray.size(); i++) {
                        String deliveryType = deliveredtimeArray.get(i);

                        if (deliverytypewise_ordersHashmap_1.containsKey(deliveryType)) {

                            int count = deliverytypewise_ordersHashmap_1.get(deliveryType);
                            PdfPCell deliveryTypecell = new PdfPCell(new Phrase(String.valueOf(count)));
                            deliveryTypecell.setBorder(Rectangle.NO_BORDER);
                            deliveryTypecell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            deliveryTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            deliveryTypecell.setPaddingLeft(10);
                            deliveryTypecell.setFixedHeight(30);
                            deliveryTypecell.setBorderWidthBottom(01);
                            if (deliveredtimeArray.size() - i != 1) {
                                deliveryTypecell.setBorderWidthRight(01);
                            }
                            allSlotinner_table.addCell(deliveryTypecell);


                        } else {
                            int count = 0;
                            PdfPCell deliveryTypecell = new PdfPCell(new Phrase(String.valueOf(" - ")));
                            deliveryTypecell.setBorder(Rectangle.NO_BORDER);
                            deliveryTypecell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            deliveryTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            deliveryTypecell.setPaddingLeft(10);
                            deliveryTypecell.setFixedHeight(30);
                            deliveryTypecell.setBorderWidthBottom(01);
                            if (deliveredtimeArray.size() - i != 1) {
                                deliveryTypecell.setBorderWidthRight(01);
                            }
                            allSlotinner_table.addCell(deliveryTypecell);
                        }
                    }

                }
            }
            else
            {
                if (hashMap_sorted_OrdersList.containsKey(slotrangetime)) {
                    PdfPCell slotrangetimecell2 = new PdfPCell(new Phrase(slotrangetime));
                    slotrangetimecell2.setBorder(Rectangle.NO_BORDER);
                    slotrangetimecell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    slotrangetimecell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    slotrangetimecell2.setPaddingLeft(10);
                    slotrangetimecell2.setPaddingRight(20);
                    slotrangetimecell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    slotrangetimecell2.setBorderWidthBottom(01);
                    slotrangetimecell2.setBorderWidthRight(01);
                    slotrangetimecell2.setFixedHeight(30);
                    inner_table.addCell(slotrangetimecell2);


                    HashMap<String, Integer> deliverytypewise_ordersHashmap_1 = hashMap_sorted_OrdersList.get(slotrangetime);

                    for (int i = 0; i < deliveredtimeArray.size(); i++) {
                        String deliveryType = deliveredtimeArray.get(i);

                        if (deliverytypewise_ordersHashmap_1.containsKey(deliveryType)) {

                            int count = deliverytypewise_ordersHashmap_1.get(deliveryType);
                            PdfPCell deliveryTypecell = new PdfPCell(new Phrase(String.valueOf(count)));
                            deliveryTypecell.setBorder(Rectangle.NO_BORDER);
                            deliveryTypecell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            deliveryTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            deliveryTypecell.setPaddingLeft(10);
                            deliveryTypecell.setFixedHeight(30);
                            deliveryTypecell.setBorderWidthBottom(01);
                            if (deliveredtimeArray.size() - i != 1) {
                                deliveryTypecell.setBorderWidthRight(01);
                            }
                            inner_table.addCell(deliveryTypecell);


                        } else {
                            int count = 0;
                            PdfPCell deliveryTypecell = new PdfPCell(new Phrase(String.valueOf(" - ")));
                            deliveryTypecell.setBorder(Rectangle.NO_BORDER);
                            deliveryTypecell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            deliveryTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            deliveryTypecell.setPaddingLeft(10);
                            deliveryTypecell.setFixedHeight(30);
                            deliveryTypecell.setBorderWidthBottom(01);
                            if (deliveredtimeArray.size() - i != 1) {
                                deliveryTypecell.setBorderWidthRight(01);
                            }
                            inner_table.addCell(deliveryTypecell);
                        }
                    }

                }
            }
                if(slotrangetimeArray.size() -1 == j){
                    try {
                       PdfPTable outertable = new PdfPTable(1);
                        PdfPCell  outercell = new PdfPCell(inner_table);
                        outercell.setCellEvent(roundRectange);
                        outercell.setBorder(Rectangle.NO_BORDER);
                        outercell.setPadding(8);
                        outertable.addCell(outercell);
                        outertable.setWidthPercentage(100);


                        layoutDocument.add(outertable);


                       /*PdfPTable allSlotoutertable1 = new PdfPTable(1);
                       PdfPCell  allSlotoutercell1 = new PdfPCell(allSlotinner_table);
                        allSlotoutercell1.setCellEvent(roundRectange);
                        allSlotoutercell1.setBorder(Rectangle.NO_BORDER);
                        allSlotoutercell1.setPadding(8);
                        allSlotoutertable1.addCell(allSlotoutercell1);
                        allSlotoutertable1.setWidthPercentage(100);

                        */
                        layoutDocument.add(allSlotinner_table);




                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }


            }
        }
    }

    private void addVendorDetails(Document layoutDocument) {
        try {
            SharedPreferences sharedPreferences
                    = getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);

            String Vendorname = sharedPreferences.getString("VendorName", "");

            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 22, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("DELIVERED ORDERS SLOTWISE REPORT");
            titlepara.setSpacingBefore(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_CENTER);
            layoutDocument.add(titlepara);

            String vendorname = "Vendor: " + Vendorname;
            com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
            vendorpara.setSpacingBefore(20);
            vendorpara.setAlignment(Element.ALIGN_LEFT);
            layoutDocument.add(vendorpara);

            com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date: " + getDate());
            datepara.setAlignment(Element.ALIGN_LEFT);
            datepara.setSpacingBefore(5);
            datepara.setSpacingAfter(20);
            layoutDocument.add(datepara);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showslotbuttons(boolean show) {
        if (!show) {
            listview_Layout.setVisibility(View.VISIBLE);
            timeslotButtonss_layout.setVisibility(View.GONE);
        } else {
            listview_Layout.setVisibility(View.GONE);
            timeslotButtonss_layout.setVisibility(View.VISIBLE);
        }
    }

    private void setDataForSpinner() {
       // deliveredtimeChoosingSpinnerData.add("All");

        deliveredtimeArray.add("On Time Delivery ");
        deliveredtimeArray.add("0 - 30 Mins Late ");
        deliveredtimeArray.add("30 - 60 Mins Late ");
        deliveredtimeArray.add(" > 60 Mins Late ");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(DeliveredOrdersTimewiseReport.this, android.R.layout.simple_spinner_item, deliveredtimeArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliverytimeSelector_spinner.setAdapter(arrayAdapter);
        selecteddeliveredtime_spinner = "All";


    }
    private void prepareDataForExcelSheet() {
        Log.d(Constants.TAG, "prepareDataForExcelSheet Response: " );
        isPDF_FIle = false;

        boolean generateSheet = false;
        sorted_OrdersList.clear();
        Log.d(Constants.TAG, "prepareDataForExcelSheet size: " + deliveredtimeArray.size() );
        Log.d(Constants.TAG, "prepareDataForExcelSheet ordersList size: " +ordersList.size() );

        for (int j = 0; j< deliveredtimeArray.size(); j++){
            String type = deliveredtimeArray.get(j);
            Log.d(Constants.TAG, "prepareDataForExcelSheet type: " +type );

            sorted_OrdersList.clear();
            for(int k = 0 ; k<ordersList.size();k++){
                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = ordersList.get(k);
                String deliverytype = modal_manageOrders_pojo_class.getTypebasedonDeliveredTime();
                Log.d(Constants.TAG, "prepareDataForExcelSheet deliverytype: " +deliverytype );
                if(type.equals(deliverytype)){

                    Log.d(Constants.TAG, "prepareDataForExcelSheet success: "  );

                    sorted_OrdersList.add(modal_manageOrders_pojo_class);
                }
            }
            if(j == deliveredtimeArray.size()-1){
                generateSheet =true;
                Log.d(Constants.TAG, "prepareDataForExcelSheet true: "  );

            }

            Log.d(Constants.TAG, "prepareDataForExcelSheet sorted_OrdersList size: " +sorted_OrdersList.size() );
            Log.d(Constants.TAG, "prepareDataForExcelSheet sorted_OrdersList size: " + deliveredtimeArray.size() );
            Log.d(Constants.TAG, "prepareDataForExcelSheet sorted_OrdersList size: " +j );

            AddDatatoExcelSheet(sorted_OrdersList,type,generateSheet);
        }

    }


    private void AddDatatoExcelSheet(List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList, String type, boolean generateSheet) {


        Log.d(Constants.TAG, "prepareDataForExcelSheet type  addData: " + type);


        sheet = wb.createSheet(type);
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
            String delayedtime = itemRow.getDelayedtime();
            boolean isdelayed = itemRow.isIsdelayed();

            if (isdelayed) {
                delayedtime = delayedtime.replaceAll("[-]", "");


            } else {
                delayedtime = "0";
            }

            delayedtime = delayedtime + "Mins ";
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
                row.createCell(8).setCellValue(delayedtime);

                row.createCell(9).setCellValue(itemRow.getOrderid());
                row.createCell(10).setCellValue(itemRow.getUseraddress());
                row.createCell(11).setCellValue(itemRow.getUsermobile());
            } catch (Exception e) {
                e.printStackTrace();
            }


            sheet.setColumnWidth(0, (10 * 200));
            sheet.setColumnWidth(1, (10 * 200));


            Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList  1  : " + sorted_OrdersList.size());
            Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum:  1  " + rowNum);

            if (generateSheet) {
                int size = sorted_OrdersList.size();
                if(size==1){

                }

                if (rowNum == size+1) {
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + sorted_OrdersList.size());
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);

                    if (SDK_INT >= Build.VERSION_CODES.R) {

                        if(Environment.isExternalStorageManager()){
                            try {
                                GenerateExcelSheet();


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


                        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(DeliveredOrdersTimewiseReport.this, WRITE_EXTERNAL_STORAGE);
                        //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                        // If do not grant write external storage permission.
                        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                            // Request user to grant write external storage permission.
                            ActivityCompat.requestPermissions(DeliveredOrdersTimewiseReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
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
                else {
                     // Toast.makeText(mContext,+sorted_OrdersList.size(),Toast.LENGTH_LONG).show();
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + sorted_OrdersList.size());
                    Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);

                }
            }


        }
    }
else{
    if( generateSheet ) {

            Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + sorted_OrdersList.size());
            Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " +rowNum);



        if (SDK_INT >= Build.VERSION_CODES.R) {

            if(Environment.isExternalStorageManager()){
                try {
                    GenerateExcelSheet();


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


            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(DeliveredOrdersTimewiseReport.this, WRITE_EXTERNAL_STORAGE);
            //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
            // If do not grant write external storage permission.
            if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(DeliveredOrdersTimewiseReport.this, new String[]{WRITE_EXTERNAL_STORAGE},
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
        //..  Toast.makeText(mContext,+sorted_OrdersList.size(),Toast.LENGTH_LONG).show();

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

                    if(isPDF_FIle) {
                        try {
                            GenerateExcelSheet();

                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    }
                    else{
                        try {
                            AddDatatoPDFFile();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
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




    private void GenerateExcelSheet() {
       String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMC Partner/Delivered Orders Timewise Report /";
        File    dir = new File(path);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");

        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "DeliveredTimeWise_Report_"+ System.currentTimeMillis()  +".xls");


     //   File file = new File(getExternalFilesDir(null), "Onlineorderdetails.xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Adjusting_Widgets_Visibility(false);
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
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);

            Toast.makeText(getApplicationContext(), "File can't be  Created", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
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


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(DeliveredOrdersTimewiseReport.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();

                            String month_in_String = getMonthString(monthOfYear);
                            String monthstring = String.valueOf(monthOfYear + 1);
                            String datestring = String.valueOf(dayOfMonth);

                            if (datestring.length() == 1) {
                                datestring = "0" + datestring;
                            }
                            if (monthstring.length() == 1) {
                                monthstring = "0" + monthstring;
                            }


                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay = getDayString(dayOfWeek);
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString = datestring + monthstring + String.valueOf(year);
                            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay + ", " + dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(CurrentDay + ", " + dayOfMonth + " " + month_in_String + " " + year);
                            manageOrders_ListView.setVisibility(View.GONE);
                            order_fetching_instruction.setVisibility(View.VISIBLE);
                            order_fetching_instruction.setText("After Selecting the Date !! Click Fetch Data");


                            //getOrderForSelectedDate(DateString, vendorKey);
/*
                            if(orderdetailsnewschema){
                                String dateAsNEWFormat = convertOldFormatDateintoNewFormat(DateString);
                                dateSelector_text.setText(DateString);
                                callVendorOrderDetailsSeviceAndInitCallBack(dateAsNEWFormat,dateAsNEWFormat,vendorKey);


                            }
                            else{
                                dateSelector_text.setText(DateString);

                                getOrderDetailsUsingOrderSlotDate(PreviousDateString, DateString, vendorKey, orderStatus);

                            }

 */



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }

    private void getOrderDetailsUsingOrderSlotDate(String previousDaydate, String SlotDate, String vendorKey, String selectedStatus) {
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );
        Adjusting_Widgets_Visibility(true);

        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();


        SharedPreferences sharedPreferences
                = DeliveredOrdersTimewiseReport.this.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforSlotDate_Vendorkey + "?slotdate=" + SlotDate + "&vendorkey=" + vendorKey + "&previousdaydate=" + previousDaydate, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            mobile_jsonString = response.toString();
                            order_fetching_instruction .setText("Fetching Process Completed");
                            convertingJsonStringintoArray(selectedStatus, mobile_jsonString);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(DeliveredOrdersTimewiseReport.this, "There is no Order  on " + SlotDate, Toast.LENGTH_LONG).show();
                    ordersList.clear();
                    array_of_orderId.clear();
                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.GONE);
                    mobile_orderinstruction.setText("No Order today");
                    order_fetching_instruction .setText("Fetching Process Completed");
                    mobile_orderinstruction.setVisibility(View.VISIBLE);

//                adapter_mobileSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();
                    Adjusting_Widgets_Visibility(false);
                    // appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

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
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(DeliveredOrdersTimewiseReport.this).add(jsonObjectRequest);


    }


    private void convertingJsonStringintoArray(String orderStatus, String jsonString) {
        try {
            String deliverytype="",slottimeString = "", ordertype = "#", orderid = "", orderdeliveredTime = "", orderplacedtime = "", slotdate = "", slottime = "", slottime_in_long = "", orderplacedtime_in_long = "", orderdeliveredtime_in_long = "";
            sorted_OrdersList.clear();
            Adjusting_Widgets_Visibility(true);
            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray = jsonObject.getJSONArray("content");
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1 = 0;
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for (; i1 < (arrayLength); i1++) {

                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                    if (json.has("ordertype")) {
                        manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                        ordertype = String.valueOf(json.get("ordertype")).toUpperCase();
                    } else {
                        ordertype = "#";
                        manageOrdersPojoClass.orderType = "";
                    }


                    if (json.has("orderStatus")) {
                        manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderStatus"));
                        orderStatus = String.valueOf(json.get("orderStatus")).toUpperCase();
                    } else {
                        manageOrdersPojoClass.orderstatus = "";
                    }
                    if ((ordertype.equals(Constants.APPORDER)) && (orderStatus.equals(Constants.DELIVERED_ORDER_STATUS))) {

                        if (json.has("orderid")) {
                            manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));
                            orderid = String.valueOf(json.get("orderid"));
                        } else {
                            manageOrdersPojoClass.orderid = "";
                        }

                        if (!array_of_orderId.contains(orderid)) {
                            array_of_orderId.add(orderid);
                            count_AllOrders.setText(String.valueOf(array_of_orderId.size()));
                            //   appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

                        }


                        if (json.has("notes")) {
                            manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                        } else {
                            manageOrdersPojoClass.notes = "";
                        }


                        if (json.has("orderplacedtime")) {
                            manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                            orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                            orderplacedtime_in_long = getLongValuefortheDate(orderplacedtime);
                            manageOrdersPojoClass.orderplacedtime_in_long = (orderplacedtime_in_long);
                        } else {
                            orderplacedtime = "nil  " + orderid;
                            manageOrdersPojoClass.orderplacedtime = orderplacedtime;

                            manageOrdersPojoClass.orderplacedtime_in_long = (orderplacedtime);

                        }


                        if (json.has("payableamount")) {
                            manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));

                        } else {
                            manageOrdersPojoClass.payableamount = "";
                        }


                        if (json.has("paymentmode")) {
                            manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));

                        } else {
                            manageOrdersPojoClass.paymentmode = "";
                        }


                        if (json.has("tokenno")) {
                            manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));

                        } else {
                            manageOrdersPojoClass.tokenno = "";
                        }


                        try {
                            if (json.has("itemdesp")) {
                                JSONArray itemdesp = json.getJSONArray("itemdesp");

                                manageOrdersPojoClass.itemdesp = itemdesp;

                            } else {
                                //Log.i(Constants.TAG, "Can't Get itemDesp");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        if (json.has("usermobile")) {
                            manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobile"));

                        } else {
                            manageOrdersPojoClass.usermobile = "";
                        }


                        if (json.has("vendorkey")) {
                            manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));

                        } else {
                            manageOrdersPojoClass.vendorkey = "vendor_1";
                        }


                        if (json.has("orderdetailskey")) {
                            manageOrdersPojoClass.orderdetailskey = String.valueOf(json.get("orderdetailskey"));

                        } else {
                            manageOrdersPojoClass.orderdetailskey = "";
                        }


                        if (json.has("orderdeliveredtime")) {
                            manageOrdersPojoClass.orderdeliveredtime = String.valueOf(json.get("orderdeliveredtime"));
                            orderdeliveredTime = String.valueOf(json.get("orderdeliveredtime"));
                            orderdeliveredtime_in_long = getLongValuefortheDate(orderdeliveredTime);
                            manageOrdersPojoClass.orderdeliveredtime_in_long = orderdeliveredtime_in_long;
                        } else {
                            orderdeliveredTime = "nil " + orderid;
                            manageOrdersPojoClass.orderdeliveredtime = orderdeliveredTime;
                            manageOrdersPojoClass.orderdeliveredtime_in_long = orderdeliveredTime;

                        }


                        if (json.has("useraddresskey")) {
                            manageOrdersPojoClass.useraddresskey = String.valueOf(json.get("useraddresskey"));

                        } else {
                            manageOrdersPojoClass.useraddresskey = "";
                        }


                        if (json.has("orderreadytime")) {
                            manageOrdersPojoClass.orderreadytime = String.valueOf(json.get("orderreadytime"));

                        } else {
                            manageOrdersPojoClass.orderreadytime = "";
                        }


                        if (json.has("orderpickeduptime")) {
                            manageOrdersPojoClass.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                        } else {
                            manageOrdersPojoClass.orderpickeduptime = "";
                        }


                        if (json.has("orderconfirmedtime")) {
                            manageOrdersPojoClass.orderconfirmedtime = String.valueOf(json.get("orderconfirmedtime"));

                        } else {
                            manageOrdersPojoClass.orderconfirmedtime = "";
                        }


                        if (json.has("coupondiscount")) {
                            manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscount"));

                        } else {
                            manageOrdersPojoClass.coupondiscamount = "";
                        }


                        if (json.has("deliverytype")) {
                            manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));
                            deliverytype =  String.valueOf(json.get("deliverytype")).toString().toUpperCase();
                        } else {
                            deliverytype ="";
                            manageOrdersPojoClass.deliverytype = "";
                        }


                        if (json.has("slottimerange")) {
                            manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));
                            slottime = String.valueOf(json.get("slottimerange"));
                        } else {
                            slottime = "nil  " + orderid;
                            manageOrdersPojoClass.slottimerange = "";
                        }


                        if (json.has("slotdate")) {
                            manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));
                            slotdate = String.valueOf(json.get("slotdate"));

                        } else {
                            manageOrdersPojoClass.slotdate = "";
                            slotdate = "nil " + orderid;
                        }


                        if (json.has("slotname")) {
                            manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                        } else {
                            manageOrdersPojoClass.slotname = "";
                        }


                        if (json.has("useraddresslat")) {
                            manageOrdersPojoClass.useraddresslat = String.valueOf(json.get("useraddresslat"));

                        } else {
                            manageOrdersPojoClass.useraddresslat = "";
                        }


                        if (json.has("useraddresslong")) {
                            manageOrdersPojoClass.useraddresslon = String.valueOf(json.get("useraddresslong"));

                        } else {
                            manageOrdersPojoClass.useraddresslon = "";
                        }

                        if (json.has("keyfromtrackingDetails")) {
                            manageOrdersPojoClass.keyfromtrackingDetails = String.valueOf(json.get("keyfromtrackingDetails"));

                        } else {
                            manageOrdersPojoClass.keyfromtrackingDetails = "";
                        }

                        try {
                            if (ordertype.toUpperCase().equals(Constants.APPORDER)) {
                                if (json.has("useraddress")) {

                                    String addresss = String.valueOf(json.get("useraddress"));
                                    if (!addresss.equals(null) && (!addresss.equals("null"))) {
                                        manageOrdersPojoClass.useraddress = String.valueOf(json.get("useraddress"));

                                    } else {
                                        manageOrdersPojoClass.useraddress = "";

                                    }
                                } else {
                                    manageOrdersPojoClass.useraddress = "";
                                }

                            }
                        } catch (Exception E) {
                            manageOrdersPojoClass.useraddress = "-";
                            E.printStackTrace();
                        }


                        if (!String.valueOf(json.get("orderStatus")).equals("NEW")) {

                            if (json.has("deliveryusername")) {
                                manageOrdersPojoClass.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));

                            }
                            if (json.has("deliveryuserkey")) {
                                manageOrdersPojoClass.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));
                                ;

                            }
                            if (json.has("deliveryusermobileno")) {
                                manageOrdersPojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));

                            }


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




                        if (ordertype.toUpperCase().equals(Constants.APPORDER)) {

                            ordersList.add(manageOrdersPojoClass);
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }


            displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
            //   appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

        } catch (JSONException e) {
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);
        }
    }

    private boolean checkDeliveredStatusOftheOrder(String orderdeliveredtime, String slottime, String orderdeliveredtime_in_long, String slottime_in_long) {
        boolean result = true;
        delayedtime = "0";
        try {


            String endtm = slottime.toString();
            //       System.out.println("slottime before parse DATE"+endtm);

            String sttime = orderdeliveredtime.toString();
            //        System.out.println("orderdeliveredtime before parse DATE"+sttime);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date1 = simpleDateFormat.parse(sttime);
            Date date2 = simpleDateFormat.parse(endtm);

            long difference = date2.getTime() - date1.getTime();
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60 * 24));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            //     Log.i("=== Hours "," :: "+hours+":");
            //     Log.i("=== Hours days "," :: "+hours+":"+days);

            hours = (hours < 0 ? -hours : hours);
            //    Log.i("=== Hours  "," :: "+hours+":"+min);

            delayedtime = String.valueOf(min);
            if (delayedtime.contains("-")) {
                result = true;

            } else {
                result = false;
            }

        } catch (Exception j) {
            j.printStackTrace();
            try {


                String endtm = slottime.toString();
                //         System.out.println("slottime before parse DATE"+endtm);

                String sttime = orderdeliveredtime.toString();
                //        System.out.println("orderdeliveredtime before parse DATE"+sttime);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

                Date date1 = sdf.parse(sttime);
                Date date2 = simpleDateFormat.parse(endtm);

                long difference = date2.getTime() - date1.getTime();
                int days = (int) (difference / (1000 * 60 * 60 * 24));
                int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60 * 24));
                int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
                ////        Log.i("=== Hours "," :: "+hours+":");
                //       Log.i("=== Hours days "," :: "+hours+":"+days);

                hours = (hours < 0 ? -hours : hours);
                //         Log.i("=== Hours  "," :: "+hours+":"+min);

                delayedtime = String.valueOf(min);
                if (delayedtime.contains("-")) {
                    result = true;

                } else {
                    result = false;
                }

            } catch (Exception jx) {
                jx.printStackTrace();

            }
        }
        return result;
/*
        long orderplacedtimelong =Long.parseLong(orderdeliveredtime_in_long);
        long slottimelong = Long.parseLong(slottime_in_long);
        long millis  = slottimelong-orderplacedtimelong;
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = millis / (1000 * 60 * 60);

        StringBuilder b = new StringBuilder();
        b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) :
                String.valueOf(hours));
        b.append(":");
        b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) :
                String.valueOf(minutes));
        b.append(":");
        b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) :
                String.valueOf(seconds));

        Log.d(TAG, "slottime b orderdeliveredtime_in_long  "+orderdeliveredtime_in_long);
        Log.d(TAG, "slottime b orderdeliveredtime "+orderdeliveredtime);

        Log.d(TAG, "slottime b slottime_in_long  "+slottime_in_long);
        Log.d(TAG, "slottime b slottime_  "+slottime);
        Log.d(TAG, "slottime b  "+b);
        return  result;

 */
    }

    private String getSlotTime(String slottime, String orderplacedtime, String slotdate) {
        String result = "", lastFourDigits = "";
        //   Log.d(TAG, "slottime  "+slottime);
        if (slottime.contains("mins")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

                final Date date = sdf.parse(orderplacedtime);
                final Calendar calendar = Calendar.getInstance();
              String timeoftheSlot ="";
              try {
                  timeoftheSlot = (slottime.replaceAll("[^\\d.]", ""));
              }
              catch(Exception e){
                  e.printStackTrace();
              }
                int timeoftheSlotDouble =0;
                try {
                    timeoftheSlotDouble = Integer.parseInt(timeoftheSlot);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                calendar.setTime(date);
                calendar.add(Calendar.MINUTE, timeoftheSlotDouble);

                //     System.out.println("Time here " + sdf.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + orderplacedtime);
                result = String.valueOf(calendar.getTime());
                System.out.println("Time here 90 mins" + result);

                result = result.replaceAll("GMT[+]05:30", "");

                //  System.out.println("Time here "+result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (slottime.length() > 5) {
                lastFourDigits = slottime.substring(slottime.length() - 5);
            } else {
                lastFourDigits = slottime;
            }

            result = slotdate + " " + lastFourDigits + ":00";

        }
        return result;
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

    private void displayorderDetailsinListview(String orderStatus, @NotNull List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner, String selectedTimeRange_spinner) {
        Adjusting_Widgets_Visibility(true);
        if(ordersList.size()>0) {
            for (int iterator = 0; iterator < ordersList.size(); iterator++) {
                String deliverytype = "", slottimeString = "", ordertype = "#", orderid = "", orderdeliveredTime = "", orderplacedtime = "", slotdate = "", slottime = "", slottime_in_long = "", orderplacedtime_in_long = "", orderdeliveredtime_in_long = "";
                Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = ordersList.get(iterator);

                try {
                    orderid = String.valueOf(manageOrdersPojoClass.getOrderid()).toString();
                } catch (Exception e) {
                    orderid = "";
                    e.printStackTrace();
                }


                try {
                    deliverytype = String.valueOf(manageOrdersPojoClass.getDeliverytype()).toString().toUpperCase();
                } catch (Exception e) {
                    deliverytype = "";
                    e.printStackTrace();
                }
                try {
                    orderdeliveredTime = String.valueOf(manageOrdersPojoClass.getOrderdeliveredtime());
                    orderdeliveredtime_in_long = getLongValuefortheDate(orderdeliveredTime);
                    manageOrdersPojoClass.orderdeliveredtime_in_long = orderdeliveredtime_in_long;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    orderplacedtime = String.valueOf(manageOrdersPojoClass.getOrderplacedtime());
                    orderplacedtime_in_long = getLongValuefortheDate(orderplacedtime);
                    manageOrdersPojoClass.orderplacedtime_in_long = orderplacedtime_in_long;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    slottime = String.valueOf(manageOrdersPojoClass.getSlottimerange()).toString();
                } catch (Exception e) {
                    slottime = "";
                    e.printStackTrace();
                }
                if (slottime.equals("")) {
                    try {
                        slottime = String.valueOf(manageOrdersPojoClass.getSlotTimeRangeFromDB()).toString();
                    } catch (Exception e) {
                        slottime = "";
                        e.printStackTrace();
                    }
                }


                if (!array_of_orderId.contains(orderid)) {
                    array_of_orderId.add(orderid);
                    count_AllOrders.setText(String.valueOf(array_of_orderId.size()));

                }



                try {
                    slotdate = String.valueOf(manageOrdersPojoClass.getSlotdate());
                } catch (Exception e) {
                    slotdate = "nil " + orderid;
                    e.printStackTrace();
                }


                if (!slotdate.contains("nil") && !slottime.contains("nil")) {
                    if (deliverytype.contains(Constants.STOREPICKUP_DELIVERYTYPE)) {
                        slottime = "09:00 - 18:00";
                    }
                    slottimeString = getSlotTime(slottime, orderplacedtime, slotdate);
                    slottime_in_long = getLongValuefortheDate(slottimeString);
                    manageOrdersPojoClass.slottime_in_long = slottime_in_long;
                    Log.d(TAG, "slottime  " + slottimeString);
                }


                try {
                    isdelayed = checkDeliveredStatusOftheOrder(orderdeliveredTime, slottimeString, orderdeliveredtime_in_long, slottime_in_long);
                    Log.i("==== isdelayed  ", " :: " + isdelayed);
                    manageOrdersPojoClass.setIsdelayed(isdelayed);
                    Log.i("==== isdelayed 2 ", " :: " + isdelayed);
                    manageOrdersPojoClass.delayedtime = delayedtime;


                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (isdelayed) {
                    delayedtime = delayedtime.replaceAll("[-]", "");


                    try {
                        delayedTimeIntt = 0;
                        delayedTimeIntt = Integer.parseInt(delayedtime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!isdelayed) {
                    manageOrdersPojoClass.typebasedonDeliveredTime = String.valueOf("On Time Delivery ");


                } else if (delayedTimeIntt <= 30) {
                    manageOrdersPojoClass.typebasedonDeliveredTime = String.valueOf("0 - 30 Mins Late ");


                } else if (delayedTimeIntt > 30 && delayedTimeIntt <= 60) {

                    manageOrdersPojoClass.typebasedonDeliveredTime = String.valueOf("30 - 60 Mins Late ");


                } else if (delayedTimeIntt > 60) {
                    manageOrdersPojoClass.typebasedonDeliveredTime = String.valueOf(" > 60 Mins Late ");


                } else {
                    Log.i("Tag", "Count Status not matched ");

                }

            }


            int onTimeDelivery_Count = 0, thirtyMinsLateDeliveryCount = 0, sixtyMinsLateDeliveryCount = 0, morethanSixtyMinsLateDeliveryCount = 0;
            int delayedTimeIntt = 0;

            //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());
            sorted_OrdersList.clear();
            String TodaysDate = getDatewithNameoftheDay();
            String TomorrowsDate = getTomorrowsDate();
            //Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

            //Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);

            for (int i = 0; i < ordersList.size(); i++) {
                try {
                    //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                    String slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange().toUpperCase();
                    String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();
                    boolean isdelayed = modal_manageOrders_forOrderDetailList.isIsdelayed();
                    String delayedtime = modal_manageOrders_forOrderDetailList.getDelayedtime();
                    if (isdelayed) {
                        delayedtime = delayedtime.replaceAll("[-]", "");


                        try {
                            delayedTimeIntt = 0;
                            delayedTimeIntt = Integer.parseInt(delayedtime);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (!isdelayed) {
                        onTimeDelivery_Count++;

                        Log.i("Tag", "Count New : " + onTimeDelivery_Count);

                    } else if (delayedTimeIntt <= 30) {
                        thirtyMinsLateDeliveryCount++;


                        Log.i("Tag", "Count confirmed : " + thirtyMinsLateDeliveryCount);

                    } else if (delayedTimeIntt > 30 && delayedTimeIntt <= 60) {
                        sixtyMinsLateDeliveryCount++;


                        Log.i("Tag", "Count ready : " + sixtyMinsLateDeliveryCount);

                    } else if (delayedTimeIntt > 60) {
                        morethanSixtyMinsLateDeliveryCount++;


                        Log.i("Tag", "Count transit : " + morethanSixtyMinsLateDeliveryCount);

                    } else {
                        Log.i("Tag", "Count Status not matched ");

                    }

                    try {

                        count_onTimeDeliveryOrders.setText(String.format(" %s ", onTimeDelivery_Count));

                        count_30MinsLateOrders.setText(String.format(" %s ", thirtyMinsLateDeliveryCount));

                        count_60MinsLateOrders.setText(String.format(" %s ", sixtyMinsLateDeliveryCount));

                        count_moreThan60MinsLateOrders.setText(String.format(" %s ", morethanSixtyMinsLateDeliveryCount));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (selectedTimeRange_spinner.equals("All")) {


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
                        modal_manageOrders_forOrderDetailList1.typebasedonDeliveredTime = selectedTimeRange_spinner;
                        modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();

                        modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                        modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                        modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                        modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                        modal_manageOrders_forOrderDetailList1.slottime_in_long = modal_manageOrders_forOrderDetailList.getSlottime_in_long();
                        modal_manageOrders_forOrderDetailList1.delayedtime = modal_manageOrders_forOrderDetailList.getDelayedtime();
                        modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();

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
                    }


                    if (selectedTimeRange_spinner.equals("On Time Delivery ")) {
                        if (!isdelayed) {


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
                            modal_manageOrders_forOrderDetailList1.delayedtime = modal_manageOrders_forOrderDetailList.getDelayedtime();
                            modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                            modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();


                            modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                            modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                            modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                            modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                            modal_manageOrders_forOrderDetailList1.slottime_in_long = modal_manageOrders_forOrderDetailList.getSlottime_in_long();


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
                        }
                    }

                    if (selectedTimeRange_spinner.equals("0 - 30 Mins Late ")) {
                        if (isdelayed) {
                            delayedtime = delayedtime.replaceAll("[-]", "");

                            int delayedTimeInt = 0;
                            try {
                                delayedTimeInt = Integer.parseInt(delayedtime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (delayedTimeInt <= 30) {


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
                                modal_manageOrders_forOrderDetailList1.delayedtime = modal_manageOrders_forOrderDetailList.getDelayedtime();
                                modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                                modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();


                                modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                                modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                                modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                                modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                                modal_manageOrders_forOrderDetailList1.slottime_in_long = modal_manageOrders_forOrderDetailList.getSlottime_in_long();


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
                            }
                        }

                    }


                    if (selectedTimeRange_spinner.equals("30 - 60 Mins Late ")) {
                        if (isdelayed) {
                            delayedtime = delayedtime.replaceAll("[-]", "");

                            int delayedTimeInt = 0;
                            try {
                                delayedTimeInt = Integer.parseInt(delayedtime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (delayedTimeInt > 30 && delayedTimeInt <= 60) {


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
                                modal_manageOrders_forOrderDetailList1.delayedtime = modal_manageOrders_forOrderDetailList.getDelayedtime();
                                modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();


                                modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                                modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                                modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                                modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                                modal_manageOrders_forOrderDetailList1.slottime_in_long = modal_manageOrders_forOrderDetailList.getSlottime_in_long();


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
                            }
                        }

                    }


                    if (selectedTimeRange_spinner.equals(" > 60 Mins Late ")) {
                        if (isdelayed) {
                            delayedtime = delayedtime.replaceAll("[-]", "");

                            int delayedTimeInt = 0;
                            try {
                                delayedTimeInt = Integer.parseInt(delayedtime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (delayedTimeInt > 60) {


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
                                modal_manageOrders_forOrderDetailList1.delayedtime = modal_manageOrders_forOrderDetailList.getDelayedtime();
                                modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                                modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();


                                modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                                modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                                modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                                modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                                modal_manageOrders_forOrderDetailList1.slottime_in_long = modal_manageOrders_forOrderDetailList.getSlottime_in_long();


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
                            }
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            try {
                if (sorted_OrdersList.size() > 0) {
                    Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                            String tokenNo_1 = object1.getTokenno();
                            String tokenNo_2 = object2.getTokenno();

                            if ((tokenNo_1.equals("")) || (tokenNo_1.equals("null")) || (tokenNo_1.equals(null))) {
                                tokenNo_1 = String.valueOf(0);
                            }
                            if ((tokenNo_2.equals("")) || (tokenNo_2.equals("null")) || (tokenNo_2.equals(null))) {
                                tokenNo_2 = String.valueOf(0);
                            }

                            Long i2 = Long.valueOf(tokenNo_2);
                            Long i1 = Long.valueOf(tokenNo_1);

                            return i2.compareTo(i1);
                        }
                    });


                    appOrdersCount_textwidget.setText(String.valueOf(sorted_OrdersList.size()));


                    if (screenInches > Constants.default_mobileScreenSize) {

                        adapter_delivered_orders_timewiseReport = new Adapter_Delivered_Orders_TimewiseReport(DeliveredOrdersTimewiseReport.this, sorted_OrdersList, orderStatus);
                        manageOrders_ListView.setAdapter(adapter_delivered_orders_timewiseReport);
                    } else {
                        adapter_delivered_orders_timewiseReport = new Adapter_Delivered_Orders_TimewiseReport(DeliveredOrdersTimewiseReport.this, sorted_OrdersList, orderStatus);
                        manageOrders_ListView.setAdapter(adapter_delivered_orders_timewiseReport);
                    }


                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.VISIBLE);
                    mobile_orderinstruction.setVisibility(View.GONE);


                } else {
                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.GONE);
                    mobile_orderinstruction.setText("There is No data for this Slot");
                    mobile_orderinstruction.setVisibility(View.VISIBLE);
                    appOrdersCount_textwidget.setText(String.valueOf(sorted_OrdersList.size()));

                }
            } catch (Exception e) {
                e.printStackTrace();
                if (sorted_OrdersList.size() > 0) {
                    if (screenInches > 8) {

                        adapter_delivered_orders_timewiseReport = new Adapter_Delivered_Orders_TimewiseReport(DeliveredOrdersTimewiseReport.this, sorted_OrdersList, orderStatus);
                        manageOrders_ListView.setAdapter(adapter_delivered_orders_timewiseReport);
                    } else {
                        adapter_delivered_orders_timewiseReport = new Adapter_Delivered_Orders_TimewiseReport(DeliveredOrdersTimewiseReport.this, sorted_OrdersList, orderStatus);
                        manageOrders_ListView.setAdapter(adapter_delivered_orders_timewiseReport);
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
       if(isVendorOrdersTableServiceCalled)
       {
           Adjusting_Widgets_Visibility(false);

           return;
       }
        isVendorOrdersTableServiceCalled = true;

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
                isVendorOrdersTableServiceCalled = false;
                //displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner,selectedTimeRange_spinner, selected_DeliveryDistanceRange_spinner, selected_StatusFrom_spinner);
                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selecteddeliveredtime_spinner);
                order_fetching_instruction .setText("Fetching Process Completed");
                //convertingJsonStringintoArray(orderStatus,mobile_jsonString);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                isVendorOrdersTableServiceCalled = false;
                order_fetching_instruction .setText("Fetching Process Completed");
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                Adjusting_Widgets_Visibility(false);
            }
        };
        mContext = DeliveredOrdersTimewiseReport.this;
        mVolleyService = new VendorOrdersTableService(mResultCallback,mContext);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_AppOrder_PhoneOrder + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype1=APPORDER"+"&ordertype2=PHONEORDER";

       // String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_MultipleOrdertype + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=APPORDER";
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey_status +"?slotdate="+FromDate+"&vendorkey="+vendorKey+"&orderstatus=DELIVERED";
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

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
            next_day = day_1 + ", " + nex;
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
        String PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay + ", " + PreviousdayDate;
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
        else{
            SimpleDateFormat day = new SimpleDateFormat("EEE");
            CurrentDay = day.format(c);



            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(c);

            CurrentDate = CurrentDay+", "+CurrentDate;

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
    void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

            manageOrders_ListView.setVisibility(View.GONE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);

        }

    }


    void showProgressBar(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            manageOrders_ListView.setVisibility(View.GONE);

        } else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);
        }

    }


    void showOrderInstructionText(boolean show) {
        if (show) {


            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.GONE);
            mobile_orderinstruction.setVisibility(View.VISIBLE);


        } else {
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
    }

    private void showSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.GONE);
        mobile_search_button.setVisibility(View.GONE);
        mobile_search_close_btn.setVisibility(View.VISIBLE);
        mobile_search_barEditText.setVisibility(View.VISIBLE);
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
        }, 0);
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
        } else if (value == 2) {
            return "Mon";
        } else if (value == 3) {
            return "Tue";
        } else if (value == 4) {
            return "Wed";
        } else if (value == 5) {
            return "Thu";
        } else if (value == 6) {
            return "Fri";
        } else if (value == 7) {
            return "Sat";
        }
        return "";
    }
     public class RoundRectangle implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect,
                               PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                    rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.stroke();
        }
    }
    @Override
    public void onBackPressed() {
        if(listview_Layout.getVisibility()==View.VISIBLE){
            listview_Layout.setVisibility(View.GONE);
            timeslotButtonss_layout.setVisibility(View.VISIBLE);
        }
        else{
            super.onBackPressed();

        }
        Log.i("tag", String.valueOf(listview_Layout.getVisibility()));

    }
}