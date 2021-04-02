package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders.Adapter_AutoCompleteManageOrdersItem;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class searchOrdersUsingMobileNumber extends AppCompatActivity {
    TextView appOrdersCount_textwidget,dateSelector_text,mobile_orderinstruction, mobile_nameofFacility_Textview;
    Button mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;
    Adapter_AutoCompleteManageOrdersItem adapter;

    String mobile_jsonString,orderStatus,vendorKey,vendorname,TAG = "Tag";
    String DateString,PreviousDateString;
    ListView manageOrders_ListView;
    LinearLayout PrintReport_Layout,generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel,newOrdersSync_Layout;
    DatePickerDialog datepicker;

    List<Modal_ManageOrders_Pojo_Class> websocket_OrdersList;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    public static String completemenuItem;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay,TodaysDate;
    List<String> slotnameChoosingSpinnerData;
    Spinner slotType_Spinner;
    int slottypefromSpinner=0;
    static Adapter_Mobile_SearchOrders_usingMobileNumber_ListView adapter_mobileSearchOrders_usingMobileNumber_listView;
    static Adapter_Pos_SearchOrders_usingMobileNumber adapter_PosSearchOrders_usingMobileNumber_listView;

    private String SERVER_PATH = "wss://hx9itd7ji2.execute-api.ap-south-1.amazonaws.com/Dev";
    WebSocket webSocket;
    private Context mContext;

    boolean isSearchButtonClicked = false;
    double screenInches;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    public static List<String> array_of_orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_orders_using_mobile_number);
        SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", "vendor_1"));
        vendorname = (shared.getString("VendorName", ""));

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);


        //
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        websocket_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        slotnameChoosingSpinnerData = new ArrayList<>();
        array_of_orderId = new ArrayList<>();
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);


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
        TodaysDate = getDate();
        PreviousDateString = getDatewithNameofthePreviousDay();

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        mobile_nameofFacility_Textview.setText(vendorname);

        Adjusting_Widgets_Visibility(true);
        String Todaysdate = getDatewithNameoftheDay();
        PreviousDateString = getDatewithNameofthePreviousDay();

        isSearchButtonClicked = false;
        orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;
        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();

        dateSelector_text.setText(Todaysdate);
        getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);


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
                getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);

            }
        });
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
                Toast.makeText(searchOrdersUsingMobileNumber.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });
        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();

                Adjusting_Widgets_Visibility(true);
                String Todaysdate = dateSelector_text.getText().toString();
                PreviousDateString = getDatewithNameofthePreviousDay();

                isSearchButtonClicked = false;
                orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;
                getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);
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
                            Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));
                            mobileNo = mobileNo;
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                            String mobilenumber = modal_manageOrders_forOrderDetailList.getUsermobile();
                            Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                            Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + mobilenumber);
                            Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + mobileNo);
                            if (mobilenumber.contains("+91" + mobileNo)) {

                                Log.d(Constants.TAG, "displayorderDetailsinListview orderid: " + modal_manageOrders_forOrderDetailList.getOrderid());
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
                            if(screenInches>8){

                                adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(searchOrdersUsingMobileNumber.this, sorted_OrdersList, searchOrdersUsingMobileNumber.this, orderstatus);
                                manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                            }else {
                                adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(searchOrdersUsingMobileNumber.this, sorted_OrdersList, searchOrdersUsingMobileNumber.this, orderstatus);
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
        datepicker = new DatePickerDialog(searchOrdersUsingMobileNumber.this,
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
                            Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);

                            getOrderDetailsUsingOrderSlotDate(PreviousDateString,DateString, vendorKey, orderStatus);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

}

    private void getOrderDetailsUsingOrderSlotDate(String previousDaydate,String SlotDate, String vendorKey, String selectedStatus) {
        Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );
        Adjusting_Widgets_Visibility(true);

        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();


        SharedPreferences sharedPreferences
                = searchOrdersUsingMobileNumber.this.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforSlotDate_Vendorkey + "?slotdate="+SlotDate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDaydate,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        mobile_jsonString =response.toString();

                        convertingJsonStringintoArray(selectedStatus, mobile_jsonString);




                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(searchOrdersUsingMobileNumber.this,"There is no Order  on "+SlotDate,Toast.LENGTH_LONG).show();
                ordersList.clear();
                array_of_orderId.clear();

                adapter_mobileSearchOrders_usingMobileNumber_listView.notifyDataSetChanged();
                Adjusting_Widgets_Visibility(false);
                appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

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
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());

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
        Volley.newRequestQueue(searchOrdersUsingMobileNumber.this).add(jsonObjectRequest);




    }








    private void getOrderDetailsUsingOrderPlacedDate(String date, String vendorKey, String selectedStatus) {
        Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey + "?orderplaceddate="+date+"&vendorkey="+vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        mobile_jsonString =response.toString();
                        try {
                            String ordertype="#";
                            array_of_orderId.clear();

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            //converting jsonSTRING into array
                            JSONObject jsonObject = new JSONObject(mobile_jsonString);
                            JSONArray JArray  = jsonObject.getJSONArray("content");
                            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1=0;
                            int arrayLength = JArray.length();
                            Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for(;i1<(arrayLength);i1++) {

                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));


                                    if(json.has("orderid")){
                                        manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                                    }
                                    else{
                                        manageOrdersPojoClass.orderid ="";
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
                                            Log.i(Constants.TAG, "Can't Get itemDesp");
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


                                    if(json.has("ordertype")){
                                        manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                                        ordertype = String.valueOf(json.get("ordertype"));
                                    }
                                    else{
                                        ordertype="#";
                                        manageOrdersPojoClass.orderType ="";
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

                                    if (ordertype.toUpperCase().equals(Constants.APPORDER)) {

                                        ordersList.add(manageOrdersPojoClass);
                                    }
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                                }


                            }

                            Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);

                            Adjusting_Widgets_Visibility(true);
                            String Todaysdate = getDatewithNameoftheDay();
                            PreviousDateString = getDatewithNameofthePreviousDay();

                            isSearchButtonClicked =false;
                            orderStatus = "TODAYS"+Constants.PREORDER_SLOTNAME;

                            getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);

                            //  saveorderDetailsInLocal(ordersList);
                        //    displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(true);
                            String Todaysdate = getDatewithNameoftheDay();
                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();

                            PreviousDateString = getDatewithNameofthePreviousDay();

                            isSearchButtonClicked =false;
                            orderStatus = "TODAYS"+Constants.PREORDER_SLOTNAME;
                            getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);

                        }

                        //  adapter = new Adapter_AutoCompleteManageOrdersItem(searchOrdersUsingMobileNumber.this, mobile_jsonString);


                        //    mobile_search_barEditText.setAdapter(adapter);
                       /* Adjusting_Widgets_Visibility(true);
                        String Todaysdate = getDatewithNameoftheDay();
                        isSearchButtonClicked =false;
                        orderStatus = "TODAYS"+Constants.PREORDER_SLOTNAME;
                        getOrderDetailsUsingOrderSlotDate(Todaysdate, vendorKey, orderStatus);

                        */
                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(searchOrdersUsingMobileNumber.this,"There is no Orders Yet on "+date,Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(true);
                String Todaysdate = getDatewithNameoftheDay();
                PreviousDateString = getDatewithNameofthePreviousDay();

                isSearchButtonClicked =false;
                orderStatus = "TODAYS"+Constants.PREORDER_SLOTNAME;
                getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);

                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

                error.printStackTrace();
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 20000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(searchOrdersUsingMobileNumber.this).add(jsonObjectRequest);

    }




    private void convertingJsonStringintoArray(String orderStatus, String jsonString) {
        try {
            String ordertype="#",orderid="";
            sorted_OrdersList.clear();

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
                    Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));


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
                            Log.i(Constants.TAG, "Can't Get itemDesp");
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

                    if(json.has("ordertype")){
                        manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                        ordertype = String.valueOf(json.get("ordertype"));
                    }
                    else{
                        ordertype="#";
                        manageOrdersPojoClass.orderType ="";
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


                    if (ordertype.toUpperCase().equals(Constants.APPORDER)) {

                        ordersList.add(manageOrdersPojoClass);
                    }
                    Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }


            }

            Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);
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
            appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }







    private void displayorderDetailsinListview(String orderStatus, @NotNull List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner) {



        Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());
        sorted_OrdersList.clear();
        String TodaysDate = getDatewithNameoftheDay();
        String TomorrowsDate = getTomorrowsDate();
        Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

        Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);

        for (int i = 0; i < ordersList.size(); i++) {
          try {
              Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

              final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
              final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
              String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
              String slotDate = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotdate());
              String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();


              Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

              Log.d(Constants.TAG, "displayorderDetailsinListview slotDate: " + slotDate);

              Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
              Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);

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
              }
              catch (Exception e)
              {
                  modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

              }

              if ((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210")) && (!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                  sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
              }

          }
          catch (Exception e){
              e.printStackTrace();
          }


        }

        try {
            if (sorted_OrdersList.size() > 0) {
                   Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                           String tokenNo_1 = object1.getTokenno();
                            String tokenNo_2 = object2.getTokenno();

                            if((tokenNo_1.equals(""))||(tokenNo_1.equals("null"))||(tokenNo_1.equals(null))){
                                tokenNo_1=String.valueOf(0);
                            }
                            if((tokenNo_2.equals(""))||(tokenNo_2.equals("null"))||(tokenNo_2.equals(null))){
                                tokenNo_2=String.valueOf(0);
                            }

                            Long i2 = Long.valueOf(tokenNo_2);
                            Long i1 = Long.valueOf(tokenNo_1);

                            return i2.compareTo(i1);
                        }
                    });





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

                if(screenInches>8){

                    adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(searchOrdersUsingMobileNumber.this, sorted_OrdersList, searchOrdersUsingMobileNumber.this, orderStatus);
                    manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                }else {
                    adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(searchOrdersUsingMobileNumber.this, sorted_OrdersList, searchOrdersUsingMobileNumber.this, orderStatus);
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
        catch (Exception e){
            e.printStackTrace();
            if (sorted_OrdersList.size() > 0) {
                if(screenInches>8){

                    adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(searchOrdersUsingMobileNumber.this, sorted_OrdersList, searchOrdersUsingMobileNumber.this, orderStatus);
                    manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
                }else {
                    adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(searchOrdersUsingMobileNumber.this, sorted_OrdersList, searchOrdersUsingMobileNumber.this, orderStatus);
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


//callAdapter();
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



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);


        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        PreviousdayDate = PreviousdayDay+", "+PreviousdayDate;
        System.out.println("todays Date  " + CurrentDate);
        System.out.println("PreviousdayDate Date  " + PreviousdayDate);


        return PreviousdayDate;
    }



    private String getDatewithNameofthePreviousDayfromSelectedDay(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
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

        calendar.add(Calendar.DATE, -1);




        Date c1 = calendar.getTime();

       SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
             String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }



    private String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);



        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;

        //CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }


    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);



        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;


        //CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
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



}