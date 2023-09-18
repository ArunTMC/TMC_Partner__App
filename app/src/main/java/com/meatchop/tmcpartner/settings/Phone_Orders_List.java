package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableService;
import com.pos.printer.AsyncEscPosPrint;
import com.pos.printer.AsyncEscPosPrinter;
import com.pos.printer.AsyncUsbEscPosPrint;
import com.pos.printer.Modal_USBPrinter;
import com.pos.printer.usb.UsbPrintersConnectionsLocal;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

public class Phone_Orders_List extends AppCompatActivity {
    TextView fetchData, ordersCount_textwidget,dateSelector_text,mobile_orderinstruction, mobile_nameofFacility_Textview;
    Button mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;
    ListView manageOrders_ListView;
    public LinearLayout PrintReport_Layout;
    public LinearLayout generateReport_Layout;
    public LinearLayout dateSelectorLayout;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;
    DatePickerDialog datepicker;
    Workbook wb;
    Sheet sheet=null;
    private Context mContext;

    boolean isSearchButtonClicked = false;
    double screenInches;
    int portSettings=0,totalGstAmount=0;
    public static List<String> array_of_orderId;
    public static List<Modal_ManageOrders_Pojo_Class> ordersList;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.POSOrdersList";

    Modal_USBPrinter modal_usbPrinter = new Modal_USBPrinter();
    boolean isUSBPrintReciptMethodCalled = false;

    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
   
    boolean orderdetailsnewschema = false;
    boolean isPrinterCnnectedfromSP = false;
    boolean isPrinterCnnected = false;
   
    static Adapter_Mobile_SearchOrders_usingMobileNumber_ListView adapter_mobileSearchOrders_usingMobileNumber_listView;
    public static Adapter_Pos_SearchOrders_usingMobileNumber adapter_PosSearchOrders_usingMobileNumber_listView;

    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBtAdapter;

    String mConnectedDeviceName ;
    TextView printerConnectionStatus_Textwidget;
    String printerName = "";
    String printerStatus= "";
    String printerNamefromSP = "";
    String printerStatusfromSP= "";
    public static String completemenuItem;
    String mobile_jsonString,orderStatus,vendorKey,vendorname,TAG = "Tag",CurrentDate ="",CurrentDay ="";
    String DateString,PreviousDateString;
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String portName = "USB",printerType_sharedPreference="";
    private static String[] columns = {"Delivery Type","Token No", "Order Status",
            "Order Placed Time","Slot Date","Slot Time Range","Order Ready Time","Order Delivered Time ","Orderid","User Address","User Mobile"};
   boolean isDeliveryPartnerMethodCalled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone__orders__list);
        deliveryPartnerList = new ArrayList<>();
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

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            SharedPreferences shared2 = getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
            String DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));
            SharedPreferences shared_PF_PrinterData = getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
            printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));

            //ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);

            if(DeliveryPersonList.equals("")){
                getDeliveryPartnerList(false,"","","","","");

            }
            else{
                ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);

            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(Phone_Orders_List .this);
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


        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_orderId = new ArrayList<>();
        ordersCount_textwidget = findViewById(R.id.ordersCount_textwidget);
        fetchData  = findViewById(R.id.fetchData);


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


        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);

        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        mobile_nameofFacility_Textview.setText(vendorname);


        isSearchButtonClicked = false;
        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();

        dateSelector_text.setText(Constants.Empty_Date_Format);
        DateString = (Constants.Empty_Date_Format);

        try{
            wb=new HSSFWorkbook();
            //Now we are creating sheet

            sheet = wb.createSheet("ApporderDetails");


        }
        catch (Exception e){
            e.printStackTrace();
        }

        mobile_search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked = false;

                displayorderDetailsinListview(orderStatus, ordersList);
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
                Toast.makeText(Phone_Orders_List.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });
        generateReport_Layout.setVisibility(View.GONE);




        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isSearchButtonClicked = false;
                if (DateString.equals(Constants.Empty_Date_Format)) {
                    Toast.makeText(Phone_Orders_List.this, "Select the Date First !!! ", Toast.LENGTH_SHORT).show();
                } else {
                    Adjusting_Widgets_Visibility(true);

                    if (orderdetailsnewschema) {
                        String dateAsNewFormat = convertOldFormatDateintoNewFormat(DateString);
                        callVendorOrderDetailsSeviceAndInitCallBack(dateAsNewFormat, dateAsNewFormat, vendorKey);


                    } else {

                        getOrderDetailsUsingOrderOrderPlacedDate(DateString, vendorKey);

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
                                modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus();

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
                            try {
                                ordersCount_textwidget.setText(String.valueOf(sorted_OrdersList.size()));
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            if(screenInches>Constants.default_mobileScreenSize){


                                adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(Phone_Orders_List.this, sorted_OrdersList, Phone_Orders_List.this);
                                manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                            } else {
                                adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Phone_Orders_List.this, sorted_OrdersList, Phone_Orders_List.this,orderStatus);
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


    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(Phone_Orders_List.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();
                            displayorderDetailsinListview(orderStatus, ordersList);
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
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            manageOrders_ListView.setVisibility(View.GONE);
                            mobile_orderinstruction.setVisibility(View.VISIBLE);
                            mobile_orderinstruction.setText("After Selecting the Date !! Click Fetch Data");



                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }


    private void getOrderDetailsUsingOrderOrderPlacedDate(String SlotDate, String vendorKey) {
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );
        Adjusting_Widgets_Visibility(true);

        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();




        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey + "?orderplaceddate="+SlotDate+"&vendorkey="+vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            mobile_jsonString = response.toString();

                            convertingJsonStringintoArray( mobile_jsonString);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Phone_Orders_List.this, "There is no Order  on " + SlotDate, Toast.LENGTH_LONG).show();
                    ordersList.clear();
                    array_of_orderId.clear();

                    Adjusting_Widgets_Visibility(false);
                    ordersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));
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
        Volley.newRequestQueue(Phone_Orders_List.this).add(jsonObjectRequest);




    }

    private void convertingJsonStringintoArray(String jsonString) {
        try {
            ordersList.clear();
            sorted_OrdersList.clear();
            array_of_orderId.clear();

            String ordertype="#",orderid="";
            sorted_OrdersList.clear();
            Adjusting_Widgets_Visibility(true);
            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            int arrayLength = JArray.length();
            Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


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
                    if (ordertype.toUpperCase().equals(Constants.PhoneOrder)) {
                        if(json.has("orderid")){
                            manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));
                            orderid = String.valueOf(json.get("orderid"));
                        }
                        else{
                            manageOrdersPojoClass.orderid ="";
                        }


                        if (json.has("deliveryamount")) {
                            manageOrdersPojoClass.deliveryamount = String.valueOf(json.get("deliveryamount"));

                        } else {
                            manageOrdersPojoClass.deliveryamount = "";
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
                   /* if(json.has("slottimerange")){
                        manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                    }
                    else{
                        manageOrdersPojoClass.slottimerange ="";
                    }

                    */

                        if(json.has("slottimerange")){
                            manageOrdersPojoClass.slotTimeRangeFromDB = String.valueOf(json.get("slottimerange"));

                        }
                        else{
                            manageOrdersPojoClass.slotTimeRangeFromDB ="";
                        }


                        try {
                            String slottime = "";
                            slottime = String.valueOf(String.valueOf(json.get("slottimerange")));
                            String estimated_Slottime = "";
                            if (String.valueOf(String.valueOf(json.get("slotname"))).toUpperCase().equals(Constants.EXPRESSDELIVERY_SLOTNAME)) {
                                String orderPlacedTime = String.valueOf(json.get("orderplacedtime"));

                                estimated_Slottime = getSlotTime(slottime, orderPlacedTime);


                                try {
                                    manageOrdersPojoClass.slottimerange = String.valueOf(estimated_Slottime);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {


                                try {

                                    manageOrdersPojoClass.slottimerange = String.valueOf(slottime);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            try {
                                if (json.has("slottimerange")) {
                                    manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                                } else {
                                    manageOrdersPojoClass.slottimerange = "";
                                }
                            } catch (Exception e1) {
                                manageOrdersPojoClass.slottimerange = "";

                                e1.printStackTrace();
                            }
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
                            if (ordertype.toUpperCase().equals(Constants.PhoneOrder)) {
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
                            if (ordertype.toUpperCase().equals(Constants.PhoneOrder)) {


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
                    }
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                } catch (JSONException e) {
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

            displayorderDetailsinListview(orderStatus,ordersList);
            //   appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

        } catch (JSONException e) {
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);
        }
    }



    public void displayorderDetailsinListview(String orderStatus, @NotNull List<Modal_ManageOrders_Pojo_Class> ordersList) {


        ordersCount_textwidget.setText(String.valueOf(ordersList.size()));

        //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());
        sorted_OrdersList.clear();

        if(ordersList.size()>0) {
            for (int i = 0; i < ordersList.size(); i++) {
                try {
                    //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);

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
                    modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                    modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();

                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                    modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                    modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus();


                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    try {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                    } catch (Exception e) {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                    }


                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);


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





                    if (screenInches > Constants.default_mobileScreenSize) {

                        adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(Phone_Orders_List.this, sorted_OrdersList, Phone_Orders_List.this);
                        manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                    } else {
                        adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Phone_Orders_List.this, sorted_OrdersList, Phone_Orders_List.this,orderStatus);
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

                        adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(Phone_Orders_List.this, sorted_OrdersList, Phone_Orders_List.this);
                        manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                    } else {
                        adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(Phone_Orders_List.this, sorted_OrdersList, Phone_Orders_List.this,orderStatus);
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


    private String getSlotTime(String slottime, String orderplacedtime) {
        String result = "", lastFourDigits = "";
        //   Log.d(TAG, "slottime  "+slottime);
        if (slottime.contains("mins")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



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
                SimpleDateFormat sdff = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
                sdff.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


                String placedtime = String.valueOf(sdff.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, timeoftheSlotDouble);

                System.out.println("Time here " + sdff.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + orderplacedtime);
                result = placedtime +" - "+String.valueOf(sdff.format(calendar.getTime()));
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

            //  result = slotdate + " " + lastFourDigits + ":00";

        }
        return result;
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
            modal_usbPrinter.deliveryamount =selectedOrder. getDeliveryamount();



        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            UsbConnection usbConnection = UsbPrintersConnectionsLocal.selectFirstConnected(Phone_Orders_List.this);
            UsbManager usbManager = (UsbManager) Phone_Orders_List.this.getSystemService(Context.USB_SERVICE);

            if (usbConnection == null || usbManager == null) {
            /*    new AlertDialog.Builder(Pos_Orders_List.this)
                        .setTitle("USB Connection")
                        .setMessage("No USB printer found.")
                        .show();

             */
                isUSBPrintReciptMethodCalled = false;

                new TMCAlertDialogClass(Phone_Orders_List.this, R.string.app_name, R.string.ReConnect_Instruction,
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
                    Phone_Orders_List.this,
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
                                    Adjusting_Widgets_Visibility(false);
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
                                    Adjusting_Widgets_Visibility(false);

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
        Adjusting_Widgets_Visibility(true);

        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);

       // SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");

        String OrderPlacedtime = "";
        String Orderid = "";
        String CouponDiscount ="";
        String OrderType = "";
        String PayableAmountfromArray = "";
        String PayableAmount = "" , PayableAmountwithOutRoundOFF ="";
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
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
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
            OrderType = modal_usbPrinter.getOrdertype().toUpperCase();
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
            text_to_Print = text_to_Print + "[c]  <font size='normal'>Fresh Meat and Seafood \n";
        }
        if((vendorKey.equals("vendor_6"))) {


            text_to_Print = "[c]<b><font size='big'>New NS Bismillah </b>\n\n";
            //text_to_Print = text_to_Print + "[c]<b><font size='normal'>Powered By The Meat Chop</b>\n\n";
            text_to_Print = text_to_Print + "[c]  <font size='normal'>Fresh Chicken and Mutton \n";
        }
        else {
            text_to_Print = "[c]<b><font size='big'>The Meat Chop</b>\n\n";
            text_to_Print = text_to_Print + "[c]  <font size='normal'>Fresh Meat and Seafood \n";
        }
        //   text_to_Print = "[c]<b><font size='big'>The Meat Chop</b>\n";

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

        PayableAmountwithOutRoundOFF = "Rs." + String.valueOf(decimalFormat.format(totalAmountFromAddingSubtotal));
        try{
            if(modal_usbPrinter.getOrdertype().toUpperCase().equals(Constants.APPORDER)){
                totalAmountFromAddingSubtotal = Double.parseDouble(decimalFormat.format(totalAmountFromAddingSubtotal));
            }
            else{
                totalAmountFromAddingSubtotal = (Math.round(totalAmountFromAddingSubtotal));
            }
        }
        catch (Exception e){
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


        text_to_Print = text_to_Print+"[L]  " +PayableAmountwithOutRoundOFF+" [R] "+PayableAmount+" \n";

        text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";

        try{

            couponDiscount_double = Double.parseDouble (CouponDiscount);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            totalAmountFromAddingSubtotalWithDiscount =  totalAmountFromAddingSubtotal - couponDiscount_double ;

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            deliveryAmount_double = Double.parseDouble(DeliveryAmount);
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
            text_to_Print = text_to_Print+"[L]  Delivery Amount "+"[R]      " +"Rs."+DeliveryAmount+".00"+" \n";

            //    text_to_Print = text_to_Print+"[L]" +DeliveryAmount+" \n";

            text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";


        }
        if ((!CouponDiscount.equals("0.0")) && (!CouponDiscount.equals("0")) && (!CouponDiscount.equals("0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals(""))) {
            couponDiscount_double = Double.parseDouble (CouponDiscount);
            if ((OrderType.equals(Constants.APPORDER)) ) {
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
                text_to_Print = text_to_Print+"[L]  Coupon Discount "+"[R]      " +"Rs."+CouponDiscount+".00"+" \n";

            }

            if ((OrderType.equals(Constants.POSORDER)) || (OrderType.equals(Constants.PhoneOrder))) {
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
                text_to_Print = text_to_Print+"[L]  Discount Amount "+"[R]      " +"Rs."+CouponDiscount+".00"+" \n";

            }


            //  text_to_Print = text_to_Print+"[L]" +CouponDiscount+" \n";

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

        text_to_Print = text_to_Print+"[L]  <b>Order Type : " +OrderType+" </b>\n";

        text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";


        text_to_Print = text_to_Print+"[L]  <b>Payment Mode : " +PaymentMode+" </b>\n";

        text_to_Print = text_to_Print+"[L]  Mobile No : " +MobileNumber+" \n"+" \n";

        if(OrderType.equals(Constants.PhoneOrder)) {



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


        }
        text_to_Print = text_to_Print+"[L] " +" \n";

        text_to_Print = text_to_Print+"[c] <b>   Thank You For Choosing Us !!!! " +"</b> \n";

        text_to_Print = text_to_Print+"[L] " +" \n";
        text_to_Print = text_to_Print+"[L] " +" \n";


        // AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);
        return printer.addTextToPrint(text_to_Print);






    }



    private void ConnectPrinter() {


        try{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                Toast.makeText(Phone_Orders_List.this, "Bluetooth is not Supported", Toast.LENGTH_LONG).show();
                return;
            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the chat session

            } else {
                if (mChatService == null) {

                    setupChat();

                }
                Intent serverIntent = null;
                //showBottomSheetDialog();
                // Launch the DeviceListActivity to see devices and do scan
                serverIntent = new Intent(Phone_Orders_List.this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }



        }
        catch (Exception e){
            e.printStackTrace();
        }



    }




    private void setupChat() {

        Log.d("TAG", "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver(this, mHandler);
    }





    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:
                            printerConnectionStatus_Textwidget.setText(R.string.title_connected_to);
                            printerConnectionStatus_Textwidget.append(mConnectedDeviceName);
                            isPrinterCnnected =true;
                            printerStatus = "Connected";
                            printerName = mConnectedDeviceName;
                            setTitle(R.string.title_connected_to);
                            setTitle(mConnectedDeviceName);
                            SaveDatainSharedPreferences(isPrinterCnnected,printerName,printerStatus);

                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:
                            printerConnectionStatus_Textwidget.setText(R.string.title_connecting);
                            setTitle(R.string.title_connecting);
                            isPrinterCnnected =false;
                            printerStatus = "Connecting";
                            printerName = mConnectedDeviceName;
                            SaveDatainSharedPreferences(isPrinterCnnected,printerName,printerStatus);

                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                            printerConnectionStatus_Textwidget.setText("state listen");

                        case BluetoothPrintDriver.STATE_NONE:
                            printerConnectionStatus_Textwidget.setText(R.string.title_not_connected);
                            setTitle(R.string.title_not_connected);
                            isPrinterCnnected =false;
                            printerStatus = "Not Connected";
                            printerName = mConnectedDeviceName;
                            SaveDatainSharedPreferences(isPrinterCnnected,printerName,printerStatus);

                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    break;
                case MESSAGE_READ:
                    String ErrorMsg = null;
                    byte[] readBuf = (byte[]) msg.obj;
                    float Voltage = 0;
                    //  if(D) Log.i(TAG, "readBuf[0]:"+readBuf[0]+"  readBuf[1]:"+readBuf[1]+"  readBuf[2]:"+readBuf[2]);
                    if(readBuf[2]==0)
                        ErrorMsg = "NO ERROR!         ";
                    else
                    {
                        if((readBuf[2] & 0x02) != 0)
                            ErrorMsg = "ERROR: No printer connected!";
                        if((readBuf[2] & 0x04) != 0)
                            ErrorMsg = "ERROR: No paper!  ";
                        if((readBuf[2] & 0x08) != 0)
                            ErrorMsg = "ERROR: Voltage is too low!  ";
                        if((readBuf[2] & 0x40) != 0)
                            ErrorMsg = "ERROR: Printer Over Heat!  ";
                    }
                    Voltage = (float) ((readBuf[0]*256 + readBuf[1])/10.0);
                    //if(D) Log.i(TAG, "Voltage: "+Voltage);
                    //   DisplayToast(ErrorMsg+"                                        "+"Battery voltage£º"+Voltage+" V");
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
            //     printerConnectionStatus_Textwidget.setText(String.valueOf(mBluetoothAdapter.getState()));
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  if(D) Log.d(TAG, "onActivityResult " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    try {
                        String address = data.getExtras()
                                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                        // Get the BLuetoothDevice object
                        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                        // Attempt to connect to the device
                        mChatService.connect(device);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                    Intent serverIntent = null;
                    //showBottomSheetDialog();
                    // Launch the DeviceListActivity to see devices and do scan
                    serverIntent = new Intent(Phone_Orders_List.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d("TAG", "BT not enabled");
                    Toast.makeText(this, "bt_not_enabled_leaving", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }



    private void SaveDatainSharedPreferences(boolean isPrinterCnnected, String printerName, String printerStatus) {
        SharedPreferences sharedPreferences
                = getSharedPreferences("PrinterConnectionData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "printerStatus",
                printerStatus);
        myEdit.putString(
                "printerName",
                printerName);
        myEdit.putBoolean(
                "isPrinterConnected",
                isPrinterCnnected);
        myEdit.apply();





    }



    public void printBillUsingBluetoothPrinter(Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class) {

        loadingPanel.setVisibility(View.VISIBLE);
        loadingpanelmask.setVisibility(View.VISIBLE);

        manageOrders_ListView.setVisibility(View.GONE);

        if (BluetoothPrintDriver.IsNoConnection()) {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);

            Toast.makeText(Phone_Orders_List.this,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

            new TMCAlertDialogClass(Phone_Orders_List.this, R.string.app_name, R.string.Do_You_Want_to_Connect_Printer_Now,
                    R.string.Yes_Text, R.string.No_Text,
                    new TMCAlertDialogClass.AlertListener() {
                        @Override
                        public void onYes() {
                            ConnectPrinter();

                        }

                        @Override
                        public void onNo() {

                        }
                    });
        }

        if(!BluetoothPrintDriver.IsNoConnection()){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(Phone_Orders_List.this,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                new TMCAlertDialogClass(Phone_Orders_List.this, R.string.app_name, R.string.Bluetooth_turnedOff_Information,
                        R.string.Yes_Text, R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                ConnectPrinter();

                            }

                            @Override
                            public void onNo() {

                            }
                        });
            }
        }

        String Title = "The Meat Chop";
     /*    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
        String StoreAddressLine2 = "Hasthinapuram Chromepet";
        String StoreAddressLine3 = "Chennai - 600044";
        String StoreLanLine = "PH No :4445568499";

      */
        String GSTIN = "GSTIN :33AAJCC0055D1Z9";
        String CurrentTime = getDate_and_time();

        String OrderPlacedtime = "";
        String Orderid = "";
        String CouponDiscount ="";
        String OrderType = "";
        String PayableAmountfromArray = "";
        String PayableAmount = "" , PayableAmountwithOutRoundOFF ="";
        String PaymentMode = "";
        String MobileNumber ="";
        String TokenNo="";
        String Notes ="";
        String Slotname ="";
        String SlotDate = "";
        String DeliveryTime = "";
        String DeliveryType ="";
        String DistanceFromStore ="";
        String Address =  "";
        double totalAmountFromAddingSubtotal=0;
        double couponDiscount_double=0;
        double deliveryAmount_double=0;
        double totalAmountFromAddingSubtotalWithDiscount =0;
        double totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt =0;
        String DeliveryAmount =  "";
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        try {
            OrderPlacedtime= modal_manageOrders_pojo_class.getOrderplacedtime();
        }
        catch (Exception e){
            e.printStackTrace();

        }
        try {
            Orderid= "# "+modal_manageOrders_pojo_class.getOrderid();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            CouponDiscount= modal_manageOrders_pojo_class.getCoupondiscamount();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            OrderType= modal_manageOrders_pojo_class.getOrderType();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            PayableAmountfromArray= modal_manageOrders_pojo_class.getPayableamount();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            PaymentMode= modal_manageOrders_pojo_class.getPaymentmode();
        }
        catch (Exception e){
            e.printStackTrace();

        }
        try {
            MobileNumber=modal_manageOrders_pojo_class.getUsermobile();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            TokenNo= modal_manageOrders_pojo_class.getTokenno();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            Notes= modal_manageOrders_pojo_class.getNotes();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            Slotname= modal_manageOrders_pojo_class.getSlotname();
        }
        catch (Exception e){
            e.printStackTrace();

        }




        try {
            SlotDate= modal_manageOrders_pojo_class.getSlotdate();
        }
        catch (Exception e){
            e.printStackTrace();

        }
        try {
            DeliveryTime=modal_manageOrders_pojo_class.getSlottimerange();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            DeliveryType= modal_manageOrders_pojo_class.getDeliverytype();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            DistanceFromStore= modal_manageOrders_pojo_class.getDeliverydistance();
        }
        catch (Exception e){
            e.printStackTrace();

        }

        try {
            Address= modal_manageOrders_pojo_class.getUseraddress();
        }
        catch (Exception e){
            e.printStackTrace();

        }





        try {



            try {

                JSONArray array = modal_manageOrders_pojo_class.getItemdesp();
                //Log.i("tag","array.length()"+ array.length());
                String b = array.toString();
                modal_manageOrders_pojo_class.setItemdesp_string(b);
                String itemDesp = "";


                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);



                    if (json.has("marinadeitemdesp")) {
                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x05);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
                        BluetoothPrintDriver.SetLineSpacing((byte) 100);
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        try{
                            BluetoothPrintDriver.printString("TokenNo : "+ TokenNo);
                            BluetoothPrintDriver.BT_Write("\r");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(Phone_Orders_List.this,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                        }
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 80);
                        BluetoothPrintDriver.printString("Order Id : "+ Orderid);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                        String finalitemname = "", finalitemNetweight = "", finalgrossweight = "",finalQuantity ="";
                        JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                        Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                        marinades_manageOrders_pojo_class.itemName = marinadesObject.getString("itemname");
                        marinades_manageOrders_pojo_class.ItemFinalPrice = marinadesObject.getString("tmcprice");
                        marinades_manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                        marinades_manageOrders_pojo_class.GstAmount = marinadesObject.getString("gstamount");
                        if (json.has("netweight")) {
                            marinades_manageOrders_pojo_class.ItemFinalWeight = marinadesObject.getString("netweight");

                        } else {
                            marinades_manageOrders_pojo_class.ItemFinalWeight = "";

                        }

                        String fullitemName = String.valueOf(marinadesObject.getString("itemname"));
                        String itemName = "";
                        String itemNameAfterBraces = "";

                        String tmcSubCtgyKey = String.valueOf(marinadesObject.getString("tmcsubctgykey"));
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
                                if (fullitemName.length() <= 21) {
                                    itemName = fullitemName;

                                    fullitemName = fullitemName;

                                }

                            } else {
                             /*   int indexofbraces = fullitemName.indexOf("(");
                                if (indexofbraces >= 0) {
                                    itemName = fullitemName.substring(0, indexofbraces);

                                }
                                if (fullitemName.length() > 21) {
                                    itemName = fullitemName.substring(0, 21);
                                    itemName = itemName + "...";
                                }
                                if (fullitemName.length() <= 21) {
                                    itemName = fullitemName;

                                }

                              */


                                if(fullitemName.contains("(")){
                                    int openbraces = fullitemName.indexOf("(");
                                    int closebraces = fullitemName.indexOf(")");
                                    System.out.println(fullitemName);
                                    itemName = fullitemName.substring(openbraces+1,closebraces) ;
                                    System.out.println(itemName);

                                }
                                if(!itemName.matches("[a-zA-Z0-9]+")){
                                    fullitemName = fullitemName.replaceAll(
                                            "[^a-zA-Z0-9()]", "");
                                    fullitemName = fullitemName.replaceAll(
                                            "[()]", " ");
                                    System.out.println("no english");

                                    System.out.println(fullitemName);

                                }
                                else{
                                    fullitemName = fullitemName.replaceAll(
                                            "[^a-zA-Z0-9()]", "");
                                    System.out.println("have English");

                                    System.out.println(fullitemName);

                                }






                            }
                        } catch (Exception e) {
                            itemName = fullitemName;

                            e.printStackTrace();
                        }
                        if(tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                            BluetoothPrintDriver.SetLineSpacing((byte) 100);
                            BluetoothPrintDriver.printString(" Grill House  "+fullitemName );
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }
                        else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                            BluetoothPrintDriver.SetLineSpacing((byte) 100);
                            BluetoothPrintDriver.printString("Ready to Cook "+fullitemName);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }
                        else  {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                            BluetoothPrintDriver.SetLineSpacing((byte) 100);
                            BluetoothPrintDriver.printString(fullitemName);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }
                        try {
                            finalitemNetweight = marinadesObject.getString("netweight");
                            //Log.i("tag","grossweight Log    "+                grossweight);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            finalQuantity = json.getString("quantity");
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try {
                            finalgrossweight = marinadesObject.getString("grossweight");


                            if ((finalgrossweight.equals(""))||(finalgrossweight.equals(null))||(finalgrossweight.equals(" - "))) {
                                try {
                                    finalgrossweight = marinadesObject.getString("grossweightingrams");
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
                                    finalgrossweight = marinadesObject.getString("grossweightingrams");
                                    //Log.i("tag","grossweight Log   3 "+                grossweight);


                                }
                            }
                            catch (Exception e1){
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetLineSpacing((byte) 60);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 80);
                        BluetoothPrintDriver.printString("Grossweight : "+finalgrossweight);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetLineSpacing((byte) 60);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 80);
                        BluetoothPrintDriver.printString("Netweight : "+finalitemNetweight);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetLineSpacing((byte) 60);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 80);
                        BluetoothPrintDriver.printString("Quantity : "+finalQuantity);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.FeedAndCutPaper((byte)66,(byte)40);

                    }
                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetFontEnlarge((byte) 0x05);
                    BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
                    BluetoothPrintDriver.SetLineSpacing((byte) 100);
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    try{
                        BluetoothPrintDriver.printString("TokenNo : "+ TokenNo);
                        BluetoothPrintDriver.BT_Write("\r");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(Phone_Orders_List.this,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                    }
                    BluetoothPrintDriver.LF();

                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("Order Id : "+ Orderid);
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();


                    String finalCutName ="",finalitemname = "", finalitemNetweight = "", finalgrossweight = "",finalQuantity ="";


                    Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    if (json.has("netweight")) {
                        manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("netweight"));

                    } else {
                        manageOrders_pojo_class.ItemFinalWeight = "";

                    }




                    manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));
                    manageOrders_pojo_class.ItemFinalPrice = String.valueOf(json.get("tmcprice"));
                    manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                    manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));


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


                            }

                        } else {
                           /* int indexofbraces = fullitemName.indexOf("(");
                            if (indexofbraces >= 0) {
                                itemName = fullitemName.substring(0, indexofbraces);

                            }
                            if (fullitemName.length() > 21) {
                                itemName = fullitemName.substring(0, 21);
                                itemName = itemName + "...";
                            }
                            if (fullitemName.length() <= 21) {
                                itemName = fullitemName;

                            }

                            */

                            if(fullitemName.contains("(")){
                                int openbraces = fullitemName.indexOf("(");
                                int closebraces = fullitemName.indexOf(")");
                                System.out.println(fullitemName);
                                itemName = fullitemName.substring(openbraces+1,closebraces) ;
                                System.out.println(itemName);

                            }
                            if(!itemName.matches("[a-zA-Z0-9]+")){
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                fullitemName = fullitemName.replaceAll(
                                        "[()]", " ");
                                System.out.println("no english");

                                System.out.println(fullitemName);

                            }
                            else{
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                System.out.println("have English");

                                System.out.println(fullitemName);

                            }







                        }
                    } catch (Exception e) {
                        itemName = fullitemName;

                        e.printStackTrace();
                    }
                    BluetoothPrintDriver.Begin();

                    if(tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.SetLineSpacing((byte) 100);
                        BluetoothPrintDriver.printString(" Grill House  "+fullitemName );
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
                    }
                    else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.SetLineSpacing((byte) 100);
                        BluetoothPrintDriver.printString("Ready to Cook "+fullitemName);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
                    }
                    else  {
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.SetLineSpacing((byte) 100);
                        BluetoothPrintDriver.printString(fullitemName);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
                    }


                    try {
                        if(json.has("cutname")){
                            finalCutName = json.getString("cutname");

                        }
                        else{
                            finalCutName = "";
                        }
                        //Log.i("tag","grossweight Log    "+                grossweight);
                    }
                    catch (Exception e){
                        finalCutName ="";
                        e.printStackTrace();
                    }



                    try {
                        finalitemNetweight = json.getString("netweight");
                        //Log.i("tag","grossweight Log    "+                grossweight);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        finalQuantity = json.getString("quantity");
                    }
                    catch (Exception e){
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


                    if((finalCutName.length()>0) && (!finalCutName.equals(null)) && (!finalCutName.equals("null"))){


                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetLineSpacing((byte) 55);
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.printString("----------------------------------------------");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                        BluetoothPrintDriver.Begin();

                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        //   BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.SetLineSpacing((byte) 60);

                        BluetoothPrintDriver.printString( (finalCutName.toUpperCase()));
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetLineSpacing((byte) 60);
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.printString("----------------------------------------------");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                    }


                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetLineSpacing((byte) 60);
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("Grossweight : "+finalgrossweight);
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();


                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetLineSpacing((byte) 60);
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("Netweight : "+finalitemNetweight);
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();


                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetLineSpacing((byte) 60);
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("Quantity : "+finalQuantity);
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();
                    BluetoothPrintDriver.LF();

                    BluetoothPrintDriver.FeedAndCutPaper((byte)66,(byte)40);


                }

            } catch (JSONException e) {
                e.printStackTrace();
            }





            BluetoothPrintDriver.Begin();

            if((vendorKey.equals("vendor_4")) ||  (vendorKey.equals("wholesalesvendor_1"))) {

                Title = "MK Proteins";

                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x04);
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
                BluetoothPrintDriver.SetAlignMode((byte) 49);
                BluetoothPrintDriver.printString(Title);
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();


                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                BluetoothPrintDriver.SetAlignMode((byte) 49);
                BluetoothPrintDriver.printString("Powered by The Meat Chop");
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();
            }
            if((vendorKey.equals("vendor_6"))) {

                Title = "New NS Bismillah";

                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x04);
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
                BluetoothPrintDriver.SetAlignMode((byte) 49);
                BluetoothPrintDriver.printString(Title);
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();


              /*  BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                BluetoothPrintDriver.SetAlignMode((byte) 49);
                BluetoothPrintDriver.printString("Powered by The Meat Chop");
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();

               */
            }


            else {
                Title = "The Meat Chop";

                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x04);
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
                BluetoothPrintDriver.SetAlignMode((byte) 49);
                BluetoothPrintDriver.printString(Title);
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();



            }
            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.printString(StoreAddressLine1);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.printString(StoreAddressLine2);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.SetLineSpacing((byte) 80);
            BluetoothPrintDriver.printString(StoreAddressLine3);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.printString(StoreLanLine);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.printString(CurrentTime);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.SetLineSpacing((byte) 130);
            BluetoothPrintDriver.printString(GSTIN);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 80);
            BluetoothPrintDriver.printString("Order Placed time : " + OrderPlacedtime);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 80);
            BluetoothPrintDriver.printString("Order Id : " + Orderid);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetLineSpacing((byte) 55);
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.printString("----------------------------------------------");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetLineSpacing((byte) 120);
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.printString("ItemName  ");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetLineSpacing((byte) 55);
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.printString("RATE                                  SUBTOTAL");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 80);
            BluetoothPrintDriver.printString("----------------------------------------------");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();
            try {

                JSONArray array = modal_manageOrders_pojo_class.getItemdesp();
                //Log.i("tag","array.length()"+ array.length());
                String b = array.toString();
                modal_manageOrders_pojo_class.setItemdesp_string(b);
                String itemDesp = "";


                for (int i = 0; i < array.length(); i++) {
                    JSONObject json = array.getJSONObject(i);

                    if (json.has("marinadeitemdesp")) {
                        String itemDespName_Weight_quantity = "", itemwise_price = "", itemwise_Subtotal = "";
                        JSONObject marinadesObject = json.getJSONObject("marinadeitemdesp");
                        Modal_ManageOrders_Pojo_Class marinades_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                        marinades_manageOrders_pojo_class.itemName = marinadesObject.getString("itemname");
                        marinades_manageOrders_pojo_class.ItemFinalPrice = marinadesObject.getString("tmcprice");
                        marinades_manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                        marinades_manageOrders_pojo_class.GstAmount = marinadesObject.getString("gstamount");
                        if (json.has("netweight")) {
                            marinades_manageOrders_pojo_class.ItemFinalWeight = marinadesObject.getString("netweight");

                        } else {
                            marinades_manageOrders_pojo_class.ItemFinalWeight = "";

                        }

                        String fullitemName = String.valueOf(marinadesObject.getString("itemname"));
                        String itemName = "";
                        String itemNameAfterBraces = "";

                        String tmcSubCtgyKey = String.valueOf(marinadesObject.getString("tmcsubctgykey"));
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
                                if (fullitemName.length() <= 21) {
                                    itemName = fullitemName;

                                    fullitemName = fullitemName;

                                }
                            } else {
                              /*  int indexofbraces = fullitemName.indexOf("(");
                                if (indexofbraces >= 0) {
                                    itemName = fullitemName.substring(0, indexofbraces);

                                }
                                if (fullitemName.length() > 21) {
                                    itemName = fullitemName.substring(0, 21);
                                    itemName = itemName + "...";
                                }
                                if (fullitemName.length() <= 21) {
                                    itemName = fullitemName;

                                }



                               */



                                if(fullitemName.contains("(")){
                                    int openbraces = fullitemName.indexOf("(");
                                    int closebraces = fullitemName.indexOf(")");
                                    System.out.println(fullitemName);
                                    itemName = fullitemName.substring(openbraces+1,closebraces) ;
                                    System.out.println(itemName);

                                }
                                if(!itemName.matches("[a-zA-Z0-9]+")){
                                    fullitemName = fullitemName.replaceAll(
                                            "[^a-zA-Z0-9()]", "");
                                    fullitemName = fullitemName.replaceAll(
                                            "[()]", " ");
                                    System.out.println("no english");

                                    System.out.println(fullitemName);

                                }
                                else{
                                    fullitemName = fullitemName.replaceAll(
                                            "[^a-zA-Z0-9()]", "");
                                    System.out.println("have English");

                                    System.out.println(fullitemName);

                                }




                            }
                        } catch (Exception e) {
                            itemName = fullitemName;

                            e.printStackTrace();
                        }

                        String  finalitemNetweight = "", finalgrossweight = "",finalQuantity ="";





                        try {
                            finalgrossweight = marinadesObject.getString("grossweight");


                            if ((finalgrossweight.equals(""))||(finalgrossweight.equals(null))||(finalgrossweight.equals(" - "))) {
                                try {
                                    finalgrossweight = marinadesObject.getString("grossweightingrams");
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
                                    finalgrossweight = marinadesObject.getString("grossweightingrams");
                                    //Log.i("tag","grossweight Log   3 "+                grossweight);


                                }
                            }
                            catch (Exception e1){
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }



                        try {
                            finalitemNetweight = marinadesObject.getString("netweight");


                            if ((finalitemNetweight.equals(""))||(finalitemNetweight.equals(null))||(finalitemNetweight.equals(" - "))) {
                                try {
                                    finalitemNetweight = marinadesObject.getString("netweight");
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
                                    finalitemNetweight = marinadesObject.getString("netweight");
                                    //Log.i("tag","grossweight Log   3 "+                grossweight);


                                }
                            }
                            catch (Exception e1){
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }


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





                        if(tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                            itemDespName_Weight_quantity = String.valueOf("Grill House  "+fullitemName);
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                            if(!finalgrossweight.equals("")) {


                                itemDespName_Weight_quantity = String.valueOf("Grossweight : " + finalgrossweight);
                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 85);
                                BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                            }
                            itemDespName_Weight_quantity = String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")");
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }
                        else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {

                            itemDespName_Weight_quantity = String.valueOf("Ready to Cook "+fullitemName) ;
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                            if(!finalgrossweight.equals("")) {


                                itemDespName_Weight_quantity = String.valueOf("Grossweight : " + finalgrossweight);
                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 85);
                                BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                            }
                            itemDespName_Weight_quantity = String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")");
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }
                        else  {
                            itemDespName_Weight_quantity = String.valueOf(fullitemName)  ;
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();

                            if(!finalgrossweight.equals("")) {


                                itemDespName_Weight_quantity = String.valueOf("Grossweight : " + finalgrossweight);
                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 85);
                                BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                            }
                            itemDespName_Weight_quantity = String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")");
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }




                        itemwise_price = marinadesObject.getString("tmcprice");
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

                        itemwise_Subtotal = String.valueOf(itemwise_Subtotal_double);
                        itemwise_price = String.valueOf(itemwise_Subtotal_double);


                        itemwise_price = "Rs. " + itemwise_price;
                        itemwise_Subtotal = "Rs." + itemwise_Subtotal;

                        if (itemwise_price.length() == 4) {
                            //21spaces
                            itemwise_price = itemwise_price + "                              ";
                        }
                        if (itemwise_price.length() == 5) {
                            //20spaces
                            itemwise_price = itemwise_price + "                             ";
                        }
                        if (itemwise_price.length() == 6) {
                            //19spaces
                            itemwise_price = itemwise_price + "                           ";
                        }
                        if (itemwise_price.length() == 7) {
                            //18spaces
                            itemwise_price = itemwise_price + "                          ";
                        }
                        if (itemwise_price.length() == 8) {
                            //17spaces
                            itemwise_price = itemwise_price + "                        ";
                        }
                        if (itemwise_price.length() == 9) {
                            //16spaces
                            itemwise_price = itemwise_price + "                       ";
                        }
                        if (itemwise_price.length() == 10) {
                            //15spaces
                            itemwise_price = itemwise_price + "                       ";
                        }
                        if (itemwise_price.length() == 11) {
                            //14spaces
                            itemwise_price = itemwise_price + "                      ";
                        }
                        if (itemwise_price.length() == 12) {
                            //13spaces
                            itemwise_price = itemwise_price + "                     ";
                        }
                        if (itemwise_price.length() == 13) {
                            //12spaces
                            itemwise_price = itemwise_price + "                    ";
                        }
                        if (itemwise_price.length() == 14) {
                            //11spaces
                            itemwise_price = itemwise_price + "                   ";
                        }
                        if (itemwise_price.length() == 15) {
                            //10spaces
                            itemwise_price = itemwise_price + "                  ";
                        }
                        if (itemwise_price.length() == 16) {
                            //9spaces
                            itemwise_price = itemwise_price + "                 ";
                        }
                        if (itemwise_price.length() == 17) {
                            //8spaces
                            itemwise_price = itemwise_price + "                ";
                        }
                        if (itemwise_price.length() == 18) {
                            //7spaces
                            itemwise_price = itemwise_price + "               ";
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

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetLineSpacing((byte) 60);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 80);

                        BluetoothPrintDriver.printString(itemwise_price + itemwise_Subtotal);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                    }
                    String itemDespName_Weight_quantity = "", itemwise_price = "", itemwise_Subtotal = "";
                    ;

                    Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    if (json.has("netweight")) {
                        manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("netweight"));

                    } else {
                        manageOrders_pojo_class.ItemFinalWeight = "";

                    }

                    manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));
                    manageOrders_pojo_class.ItemFinalPrice = String.valueOf(json.get("tmcprice"));
                    manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                    manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));


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
                            if (fullitemName.length() <= 21) {
                                itemName = fullitemName;

                                fullitemName = fullitemName;

                            }
                        } else {
                         /*   int indexofbraces = fullitemName.indexOf("(");
                            if (indexofbraces >= 0) {
                                itemName = fullitemName.substring(0, indexofbraces);

                            }
                            if (fullitemName.length() > 21) {
                                itemName = fullitemName.substring(0, 21);
                                itemName = itemName + "...";
                            }
                            if (fullitemName.length() <= 21) {
                                itemName = fullitemName;

                            }

                          */




                            if(fullitemName.contains("(")){
                                int openbraces = fullitemName.indexOf("(");
                                int closebraces = fullitemName.indexOf(")");
                                System.out.println(fullitemName);
                                itemName = fullitemName.substring(openbraces+1,closebraces) ;
                                System.out.println(itemName);

                            }
                            if(!itemName.matches("[a-zA-Z0-9]+")){
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                fullitemName = fullitemName.replaceAll(
                                        "[()]", " ");
                                System.out.println("no english");

                                System.out.println(fullitemName);

                            }
                            else{
                                fullitemName = fullitemName.replaceAll(
                                        "[^a-zA-Z0-9()]", "");
                                System.out.println("have English");

                                System.out.println(fullitemName);

                            }




                        }
                    } catch (Exception e) {
                        itemName = fullitemName;

                        e.printStackTrace();
                    }




                    String  finalCutName = "",finalitemNetweight = "", finalgrossweight = "",finalQuantity ="";


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
                            finalCutName = "";
                        }
                        //Log.i("tag","grossweight Log    "+                grossweight);
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

                        itemDespName_Weight_quantity = String.valueOf("Grill House  "+fullitemName);
                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                        if((finalCutName.length()>0) && (!finalCutName.equals("null")) && (!finalCutName.equals(null))) {
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetLineSpacing((byte) 60);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            // BluetoothPrintDriver.SetFontEnlarge((byte) 0x01);

                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 80);
                            BluetoothPrintDriver.printString("Cut Name : " + (finalCutName.toUpperCase()));
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }

                        if(!finalgrossweight.equals("")) {

                            itemDespName_Weight_quantity = String.valueOf("Grossweight : " + finalgrossweight);
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                        }
                        itemDespName_Weight_quantity = String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")");
                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                    }
                    else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {

                        itemDespName_Weight_quantity = String.valueOf("Ready to Cook  "+fullitemName);
                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                        if((finalCutName.length()>0) && (!finalCutName.equals("null")) && (!finalCutName.equals(null))) {
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetLineSpacing((byte) 60);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            // BluetoothPrintDriver.SetFontEnlarge((byte) 0x01);

                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 80);
                            BluetoothPrintDriver.printString("Cut Name : " + (finalCutName.toUpperCase()));
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }

                        if(!finalgrossweight.equals("")) {

                            itemDespName_Weight_quantity = String.valueOf("Grossweight : " + finalgrossweight);
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                        }
                        itemDespName_Weight_quantity = String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")");
                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
                    }
                    else  {
                        itemDespName_Weight_quantity = String.valueOf(fullitemName)  ;
                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                        if((finalCutName.length()>0) && (!finalCutName.equals("null")) && (!finalCutName.equals(null))) {
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetLineSpacing((byte) 60);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            // BluetoothPrintDriver.SetFontEnlarge((byte) 0x01);

                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 80);
                            BluetoothPrintDriver.printString("Cut Name : " + (finalCutName.toUpperCase()));
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }


                        if(!finalgrossweight.equals("")) {

                            itemDespName_Weight_quantity = String.valueOf("Grossweight : " + finalgrossweight);
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                        }
                        itemDespName_Weight_quantity = String.valueOf("Netweight : "+ finalitemNetweight+" , "+"Quantity : " + "(" + String.valueOf(finalQuantity) + ")");
                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(itemDespName_Weight_quantity);

                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
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

                    itemwise_Subtotal = String.valueOf(itemwise_Subtotal_double);
                    itemwise_price = String.valueOf(itemwise_Subtotal_double);

                    itemwise_price = "Rs. " + itemwise_price;
                    itemwise_Subtotal = "Rs." + itemwise_Subtotal;


                    if (itemwise_price.length() == 4) {
                        //21spaces
                        itemwise_price = itemwise_price + "                              ";
                    }
                    if (itemwise_price.length() == 5) {
                        //20spaces
                        itemwise_price = itemwise_price + "                             ";
                    }
                    if (itemwise_price.length() == 6) {
                        //19spaces
                        itemwise_price = itemwise_price + "                           ";
                    }
                    if (itemwise_price.length() == 7) {
                        //18spaces
                        itemwise_price = itemwise_price + "                          ";
                    }
                    if (itemwise_price.length() == 8) {
                        //17spaces
                        itemwise_price = itemwise_price + "                        ";
                    }
                    if (itemwise_price.length() == 9) {
                        //16spaces
                        itemwise_price = itemwise_price + "                       ";
                    }
                    if (itemwise_price.length() == 10) {
                        //15spaces
                        itemwise_price = itemwise_price + "                       ";
                    }
                    if (itemwise_price.length() == 11) {
                        //14spaces
                        itemwise_price = itemwise_price + "                      ";
                    }
                    if (itemwise_price.length() == 12) {
                        //13spaces
                        itemwise_price = itemwise_price + "                     ";
                    }
                    if (itemwise_price.length() == 13) {
                        //12spaces
                        itemwise_price = itemwise_price + "                    ";
                    }
                    if (itemwise_price.length() == 14) {
                        //11spaces
                        itemwise_price = itemwise_price + "                   ";
                    }
                    if (itemwise_price.length() == 15) {
                        //10spaces
                        itemwise_price = itemwise_price + "                  ";
                    }
                    if (itemwise_price.length() == 16) {
                        //9spaces
                        itemwise_price = itemwise_price + "                 ";
                    }
                    if (itemwise_price.length() == 17) {
                        //8spaces
                        itemwise_price = itemwise_price + "                ";
                    }
                    if (itemwise_price.length() == 18) {
                        //7spaces
                        itemwise_price = itemwise_price + "               ";
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


                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString(itemwise_price + itemwise_Subtotal);

                    BluetoothPrintDriver.LF();

                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("                                               ");
                    BluetoothPrintDriver.LF();




                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 65);
            BluetoothPrintDriver.printString("----------------------------------------------");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();



            PayableAmountwithOutRoundOFF = "Rs." + String.valueOf(decimalFormat.format(totalAmountFromAddingSubtotal));
            try{
                if(modal_manageOrders_pojo_class.getOrderType().toUpperCase().equals(Constants.APPORDER)){
                    totalAmountFromAddingSubtotal = Double.parseDouble(decimalFormat.format(totalAmountFromAddingSubtotal));
                }
                else{
                    totalAmountFromAddingSubtotal = (Math.round(totalAmountFromAddingSubtotal));
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


            PayableAmount = "Rs." + String.valueOf(totalAmountFromAddingSubtotal);
            if (PayableAmount.length() == 4) {
                //21spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                                     " + PayableAmount;
            }
            if (PayableAmount.length() == 5) {
                //20spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                                  " + PayableAmount;
            }
            if (PayableAmount.length() == 6) {
                //19spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                                 " + PayableAmount;
            }
            if (PayableAmount.length() == 7) {
                //18spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                                " + PayableAmount;
            }
            if (PayableAmount.length() == 8) {
                //17spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                               " + PayableAmount;
            }
            if (PayableAmount.length() == 9) {
                //16spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                              " + PayableAmount;
            }
            if (PayableAmount.length() == 10) {
                //15spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                             " + PayableAmount;
            }
            if (PayableAmount.length() == 11) {
                //14spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                            " + PayableAmount;
            }
            if (PayableAmount.length() == 12) {
                //13spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                           " + PayableAmount;
            }
            if (PayableAmount.length() == 13) {
                //12spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                          " + PayableAmount;
            }
            if (PayableAmount.length() == 14) {
                //11spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                         " + PayableAmount;
            }
            if (PayableAmount.length() == 15) {
                //10spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                        " + PayableAmount;
            }
            if (PayableAmount.length() == 16) {
                //9spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                       " + PayableAmount;
            }
            if (PayableAmount.length() == 17) {
                //8spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                       " + PayableAmount;
            }
            if (PayableAmount.length() == 18) {
                //7spaces
                PayableAmount = PayableAmountwithOutRoundOFF + "                     " + PayableAmount;
            }


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 65);
            BluetoothPrintDriver.printString(PayableAmount);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 65);
            BluetoothPrintDriver.printString("----------------------------------------------");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            if ((!CouponDiscount.equals("0.0")) && (!CouponDiscount.equals("0")) && (!CouponDiscount.equals("0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals(""))) {
                if (OrderType.equals(Constants.APPORDER)) {
                    if (CouponDiscount.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        CouponDiscount = "Coupon Discount                           " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        CouponDiscount = "Coupon Discount                        " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        CouponDiscount = "Coupon Discount                        " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        CouponDiscount = "Coupon Discount                       " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        CouponDiscount = "Coupon Discount                      " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Coupon Discount                     " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Coupon Discount                    " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Coupon Discount                   " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Coupon Discount                  " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Coupon Discount                 " + CouponDiscount;
                    }
                }

                if ((OrderType.equals(Constants.POSORDER)) || (OrderType.equals(Constants.PhoneOrder))) {
                    if (CouponDiscount.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        CouponDiscount = "Discount Amount                          " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        CouponDiscount = "Discount Amount                        " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        CouponDiscount = "Discount Amount                       " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        CouponDiscount = "Discount Amount                      " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        CouponDiscount = " Discount Amount                     " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount                    " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount                   " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount                  " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount                 " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount                " + CouponDiscount;
                    }
                }
                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetLineSpacing((byte) 65);
                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                BluetoothPrintDriver.SetAlignMode((byte) 0);
                BluetoothPrintDriver.printString(CouponDiscount);
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();

                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetLineSpacing((byte) 65);
                BluetoothPrintDriver.SetAlignMode((byte) 0);
                BluetoothPrintDriver.printString("----------------------------------------------");
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();




            }
            try{
                totalAmountFromAddingSubtotalWithDiscount =  totalAmountFromAddingSubtotal - couponDiscount_double ;

            }
            catch (Exception e){
                e.printStackTrace();
            }


            try{
                deliveryAmount_double = Double.parseDouble(DeliveryAmount);
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
                DeliveryAmount = "Rs."+DeliveryAmount+".00";

                if (DeliveryAmount.length() == 2) {
                    //25spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                       " + DeliveryAmount;
                }

                if (DeliveryAmount.length() == 3) {
                    //25spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                        " + DeliveryAmount;
                }


                if (DeliveryAmount.length() == 4) {
                    //25spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                         " + DeliveryAmount;
                }
                if (DeliveryAmount.length() == 5) {
                    //24spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                        " + DeliveryAmount;
                }
                if (DeliveryAmount.length() == 6) {
                    //23spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                       " + DeliveryAmount;
                }

                if (DeliveryAmount.length() == 7) {
                    //22spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                      " + DeliveryAmount;
                }
                if (DeliveryAmount.length() == 8) {
                    //21spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                     " + DeliveryAmount;
                }
                if (DeliveryAmount.length() == 9) {
                    //20spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                    " + DeliveryAmount;
                }
                if (DeliveryAmount.length() == 10) {
                    //19spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                   " + DeliveryAmount;
                }
                if (DeliveryAmount.length() == 11) {
                    //18spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                  " + DeliveryAmount;
                }
                if (DeliveryAmount.length() == 12) {
                    //17spaces
                    //DeliveryAmount =15
                    DeliveryAmount = "Delivery Amount                 " + DeliveryAmount;
                }

                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetAlignMode((byte) 0);
                BluetoothPrintDriver.SetLineSpacing((byte) 65);
                BluetoothPrintDriver.printString(DeliveryAmount);
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();


                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetLineSpacing((byte) 65);
                BluetoothPrintDriver.SetAlignMode((byte) 0);
                BluetoothPrintDriver.printString("----------------------------------------------");
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();


            }




            String NetTotal = "Rs." + String.valueOf(totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt);
            if (NetTotal.length() == 4) {
                //27spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                               " + NetTotal;
            }
            if (NetTotal.length() == 5) {
                //26spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                               " + NetTotal;
            }
            if (NetTotal.length() == 6) {
                //25spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                               " + NetTotal;
            }

            if (NetTotal.length() == 7) {
                //24spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                              " + NetTotal;
            }
            if (NetTotal.length() == 8) {
                //23spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                            " + NetTotal;
            }
            if (NetTotal.length() == 9) {
                //22spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                           " + NetTotal;
            }
            if (NetTotal.length() == 10) {
                //21spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                          " + NetTotal;
            }
            if (NetTotal.length() == 11) {
                //20spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                         " + NetTotal;
            }
            if (NetTotal.length() == 12) {
                //19spaces+4spaces
                //NEW TOTAL =9
                NetTotal = "NET TOTAL                       " + NetTotal;
            }


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 65);
            BluetoothPrintDriver.printString(NetTotal);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetLineSpacing((byte) 65);
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.printString("----------------------------------------------");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 90);
            BluetoothPrintDriver.printString("OrderType : "+OrderType);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetLineSpacing((byte) 65);
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.printString("----------------------------------------------");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 90);
            BluetoothPrintDriver.printString("Payment Mode : "+PaymentMode);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();

            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 110);
            BluetoothPrintDriver.printString("Mobile No : "+MobileNumber);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();



            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 110);
            BluetoothPrintDriver.SetBold((byte) 0x08);//´ÖÌå
            BluetoothPrintDriver.SetCharacterFont((byte)48);
            BluetoothPrintDriver.SetFontEnlarge((byte) 0x07);
            BluetoothPrintDriver.SetFontEnlarge((byte) 0x30);
            BluetoothPrintDriver.printString("TOKENNO: "+TokenNo);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();





            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 90);
            BluetoothPrintDriver.printString("Slot Name : "+Slotname);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();





            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 90);
            BluetoothPrintDriver.printString("Slot Date : "+SlotDate);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();

            if(Slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME)){

                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetAlignMode((byte) 0);
                BluetoothPrintDriver.SetLineSpacing((byte) 90);
                BluetoothPrintDriver.printString("Order Placed time : "+OrderPlacedtime);
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();


            }
            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 90);
            BluetoothPrintDriver.printString("Delivery time : "+DeliveryTime);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 90);
            BluetoothPrintDriver.printString("Delivery type : "+DeliveryType);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 90);
            BluetoothPrintDriver.printString("Distance from Store : "+DistanceFromStore+" Kms");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 80);
            BluetoothPrintDriver.printString("Address : ");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 120);
            BluetoothPrintDriver.printString(Address);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


            if(!Notes.equals("")) {

                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetAlignMode((byte) 0);
                BluetoothPrintDriver.SetLineSpacing((byte) 70);
                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x01);
                BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                BluetoothPrintDriver.printString("Notes :" + Notes);
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();
            }
            else{
                BluetoothPrintDriver.Begin();
                BluetoothPrintDriver.SetAlignMode((byte) 0);
                BluetoothPrintDriver.SetLineSpacing((byte) 30);
                BluetoothPrintDriver.printString("");
                BluetoothPrintDriver.BT_Write("\r");
                BluetoothPrintDriver.LF();
            }


            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 0);
            BluetoothPrintDriver.SetLineSpacing((byte) 120);
            BluetoothPrintDriver.printString("                                          ");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();



            BluetoothPrintDriver.Begin();
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.SetLineSpacing((byte) 65);
            BluetoothPrintDriver.printString("Thank You For Choosing Us !!!! ");
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.LF();
            BluetoothPrintDriver.FeedAndCutPaper((byte)66,(byte)50);






        }
        catch ( Exception e){
            Adjusting_Widgets_Visibility(false);

            e.printStackTrace();
        }

        Adjusting_Widgets_Visibility(false);

    }

    public void getDeliveryPartnerList(boolean openBottomSheet, String orderkey, String deliverypartnerName, String orderid, String customerMobileNo, String vendorkey) {
        if(isDeliveryPartnerMethodCalled){
            return;
        }
        isDeliveryPartnerMethodCalled = true;


        SharedPreferences preferences =getSharedPreferences("DeliveryPersonList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryPartnerList+vendorkey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //converting jsonSTRING into array
                            String DeliveryPersonListString = response.toString();
                            ConvertStringintoDeliveryPartnerListArray(DeliveryPersonListString);

                            SharedPreferences sharedPreferences
                                    = getSharedPreferences("DeliveryPersonList",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor myEdit
                                    = sharedPreferences.edit();


                            myEdit.putString(
                                    "DeliveryPersonListString",
                                    DeliveryPersonListString);
                            myEdit.apply();
                            isDeliveryPartnerMethodCalled = false;

                        } catch (Exception e) {
                            isDeliveryPartnerMethodCalled = false;
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                SharedPreferences preferences =getSharedPreferences("DeliveryPersonList",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                String errorCode = "";
                if (error instanceof TimeoutError) {
                    errorCode = "Time Out Error";
                } else if (error instanceof NoConnectionError) {
                    errorCode = "No Connection Error";

                } else if (error instanceof AuthFailureError) {
                    errorCode = "Auth_Failure Error";
                } else if (error instanceof ServerError) {
                    errorCode = "Server Error";
                } else if (error instanceof NetworkError) {
                    errorCode = "Network Error";
                } else if (error instanceof ParseError) {
                    errorCode = "Parse Error";
                }
                Toast.makeText(getApplicationContext(),"Error in Delivery Partner list :  "+errorCode,Toast.LENGTH_LONG).show();
                isDeliveryPartnerMethodCalled = false;


                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", vendorkey);
                //params.put("orderplacedtime", "12/26/2020");

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
        Volley.newRequestQueue(Phone_Orders_List.this).add(jsonObjectRequest);
    }



    private void ConvertStringintoDeliveryPartnerListArray(String deliveryPersonList) {
        if ((!deliveryPersonList.equals("") )|| (!deliveryPersonList.equals(null))) {
            try {
                String ordertype = "#", orderid = "";
                //  sorted_OrdersList.clear();

                //converting jsonSTRING into array
                JSONObject jsonObject = new JSONObject(deliveryPersonList);
                JSONArray JArray = jsonObject.getJSONArray("content");
                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                int i1 = 0;
                int arrayLength = JArray.length();
                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                for (; i1 < (arrayLength); i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass = new AssignDeliveryPartner_PojoClass();
                        assignDeliveryPartner_pojoClass.deliveryPartnerStatus = String.valueOf(json.get("status"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerKey = String.valueOf(json.get("key"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("mobileno"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerName = String.valueOf(json.get("name"));

                        // //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                        deliveryPartnerList.add(assignDeliveryPartner_pojoClass);

                        //  Adapter_Mobile_AssignDeliveryPartner1 adapter_mobile_assignDeliveryPartner1 = new Adapter_Mobile_AssignDeliveryPartner1(MobileScreen_AssignDeliveryPartner1.this, deliveryPartnerList, orderKey,IntentFrom);

                        //deliveryPartners_list_widget.setAdapter(adapter_mobile_assignDeliveryPartner1);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                }
                try{
                    Collections.sort(deliveryPartnerList, new Comparator<AssignDeliveryPartner_PojoClass>() {
                        public int compare(AssignDeliveryPartner_PojoClass result1, AssignDeliveryPartner_PojoClass result2) {
                            return result1.getDeliveryPartnerName().compareTo(result2.getDeliveryPartnerName());
                        }
                    });
                }
                catch (Exception e ){
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        if(orderdetailsnewschema) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(c);


            return CurrentDate;
        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDay = day.format(c);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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
    private String getDatewithNameofthePreviousDay2(String sDate) {



        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


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

    private String convertOldFormatDateintoNewFormat(String todaysdate) {
/*
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }

 */

        Date date = null;

        SimpleDateFormat formatGMT = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);

        formatGMT.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        try
        {
            date  = formatGMT.parse(todaysdate);
        }
        catch (ParseException e)
        {
            //log(Log.ERROR, "DB Insertion error", e.getMessage().toString());
            //logException(e);
            e.printStackTrace();
        }

        try{

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



            CurrentDate = day.format(date);


        }
        catch (Exception e){
            e.printStackTrace();
        }


        return CurrentDate;

    }


    private String convertnewFormatDateintoOldFormat(String todaysdate) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDay = day.format(date);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



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
                displayorderDetailsinListview(orderStatus,ordersList);

                //convertingJsonStringintoArray(orderStatus,mobile_jsonString);
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                Adjusting_Widgets_Visibility(false);
            }
        };
        mContext = Phone_Orders_List.this;
        mVolleyService = new VendorOrdersTableService(mResultCallback,mContext);
      //  String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_type + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=PHONEORDER";
          String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_SingleOrdertype + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=PHONEORDER";
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }
    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String CurrentDatee = df.format(c);
        CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String FormattedTime = dfTime.format(c);
        String formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }
}