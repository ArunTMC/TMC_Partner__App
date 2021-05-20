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
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

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

import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlots;

public class searchOrdersUsingMobileNumber extends AppCompatActivity {
    TextView appOrdersCount_textwidget,dateSelector_text,mobile_orderinstruction, mobile_nameofFacility_Textview;
    Button mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;

    String mobile_jsonString,orderStatus,vendorKey,vendorname,TAG = "Tag";
    String DateString,PreviousDateString;
    ListView manageOrders_ListView;
   public LinearLayout PrintReport_Layout;
    public LinearLayout generateReport_Layout;
    public LinearLayout dateSelectorLayout;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;
    public LinearLayout filterLayout;
    public LinearLayout addFilters_Layout;
    DatePickerDialog datepicker;

    List<Modal_ManageOrders_Pojo_Class> websocket_OrdersList;
    List<Modal_ManageOrders_Pojo_Class> ordersList;
    public static String completemenuItem;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay,TodaysDate,DeliveryPersonList;
    int slottypefromSpinner=0;
    Spinner deliverDistance_Spinner;
    int deliveryDistancefromSpinner=0;
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
    Spinner SlotrangeSelector_spinner;
    List<String> slottime = new ArrayList<>();
    List<String> slotrangeChoosingSpinnerData;
    String selectedTimeRange_spinner = "All";
    int spinner_check = 0;

    List<String> deliverydistanceChoosingSpinnerData;
    String selected_DeliveryDistanceRange_spinner = "All";
    int deliverydistancespinner_check = 0;

    Button AddFilters_button,clearFilters_button;
    boolean isFilterChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_orders_using_mobile_number);
        deliveryPartnerList = new ArrayList<>();

        try{
           SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
           vendorKey = (shared.getString("VendorKey", "vendor_1"));
           vendorname = (shared.getString("VendorName", ""));

           SharedPreferences shared2 = getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
           DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));

           ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);

       }
       catch (Exception e){
           e.printStackTrace();
       }

         DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);
        SlotrangeSelector_spinner =  findViewById(R.id.SlotrangeSelector_spinner);
        slotrangeChoosingSpinnerData = new ArrayList<>();

        deliverDistance_Spinner =  findViewById(R.id.deliveerydistanse_Spinner);
        deliverydistanceChoosingSpinnerData = new ArrayList<>();

        //
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        websocket_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_orderId = new ArrayList<>();
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);

        applaunchimage = findViewById(R.id.applaunchimage);
        manageOrders_ListView = findViewById(R.id.manageOrders_ListView);
        mobile_orderinstruction = findViewById(R.id.orderinstruction);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        generateReport_Layout = findViewById(R.id.generateReport_Layout);
        addFilters_Layout = findViewById(R.id.addFilters_Layout);

        filterLayout = findViewById(R.id.filterLayout);

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
        AddFilters_button = findViewById(R.id.AddFilters_button);
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);
        clearFilters_button = findViewById(R.id.clearFilters_button);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        Adjusting_Widgets_Visibility(true);

        mobile_nameofFacility_Textview.setText(vendorname);


        try{
            TodaysDate = getDate();
            PreviousDateString = getDatewithNameofthePreviousDay();
            //Now we are creating sheet

            Adjusting_Widgets_Visibility(true);
            String Todaysdate = getDatewithNameoftheDay();
            PreviousDateString = getDatewithNameofthePreviousDay();

            isSearchButtonClicked = false;
            orderStatus = "TODAYS" + Constants.PREORDER_SLOTNAME;
            ordersList.clear();
            sorted_OrdersList.clear();
            array_of_orderId.clear();
            selectedTimeRange_spinner = "All";
            selected_DeliveryDistanceRange_spinner = "All";

            dateSelector_text.setText(Todaysdate);

            getOrderDetailsUsingOrderSlotDate(PreviousDateString,Todaysdate, vendorKey, orderStatus);


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
        setDataForFilterSpinners();

        SlotrangeSelector_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(spinner_check>1) {
                    selectedTimeRange_spinner = SlotrangeSelector_spinner.getSelectedItem().toString();
                //    displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selectedTimeRange_spinner);
                    isFilterChanged = true;
                }
                spinner_check=2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        deliverDistance_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(deliverydistancespinner_check>1) {
                    selected_DeliveryDistanceRange_spinner = deliverDistance_Spinner.getSelectedItem().toString();
              //      displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selected_DeliveryDistanceRange_spinner);
                    isFilterChanged = true;
                }
                deliverydistancespinner_check=2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AddFilters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    filterLayout.setVisibility(View.GONE);
                        if(!isFilterChanged){
                            Toast.makeText(searchOrdersUsingMobileNumber.this,"Filter is not changed",Toast.LENGTH_LONG).show();
                        }


                   displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);
            }
        });


        clearFilters_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected_DeliveryDistanceRange_spinner = "All";
                deliverydistanceChoosingSpinnerData.add("All");
                deliverDistance_Spinner.setSelection(0);
                slotrangeChoosingSpinnerData.add("All");
                selectedTimeRange_spinner= "All";
                SlotrangeSelector_spinner.setSelection(0);
                filterLayout.setVisibility(View.GONE);

                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);

            }
        });

        addFilters_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFilterChanged = false;
                if(filterLayout.getVisibility()==View.GONE){
                    filterLayout.setVisibility(View.VISIBLE);

                }
                else{
                    filterLayout.setVisibility(View.GONE);
                    if(!isFilterChanged){
                        Toast.makeText(searchOrdersUsingMobileNumber.this,"Filter is not changed",Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

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

                displayorderDetailsinListview(orderStatus, ordersList, slottypefromSpinner,selectedTimeRange_spinner, selected_DeliveryDistanceRange_spinner);
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
                                modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();

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

    private void setDataForFilterSpinners() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlots+"?storeid="+vendorKey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        slotrangeChoosingSpinnerData.add("All");
                        slotrangeChoosingSpinnerData.add(Constants.EXPRESS_DELIVERY_SLOTNAME);
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);

                                String slotName = String.valueOf(json.get("slotname"));
                                String slotdateType = String.valueOf(json.get("slotdatetype"));
                                if(slotdateType.equals("TODAY")) {
                                    if (slotName.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {
                                        String deliverytime = String.valueOf(json.get("deliverytime"));

                                        slotrangeChoosingSpinnerData.add(deliverytime);


                                    } else {
                                        String slotEndTime = String.valueOf(json.get("slotendtime"));
                                        String slotStartTime = String.valueOf(json.get("slotstarttime"));
                                        String timeRange = slotStartTime + " - " + slotEndTime;
                                        if(!timeRange.equals(" - ")) {
                                            slotrangeChoosingSpinnerData.add(timeRange);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(searchOrdersUsingMobileNumber.this,android.R.layout.simple_spinner_item, slotrangeChoosingSpinnerData);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SlotrangeSelector_spinner.setAdapter(arrayAdapter);
                        selectedTimeRange_spinner = "All";


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
        Volley.newRequestQueue(searchOrdersUsingMobileNumber.this).add(jsonObjectRequest);








        //seting data for DeliveryDistansce Spinner
        deliverydistanceChoosingSpinnerData.add("All");
        deliverydistanceChoosingSpinnerData.add(" Less than 3 Kms");
        deliverydistanceChoosingSpinnerData.add(" 3 to 5 Kms ");
        deliverydistanceChoosingSpinnerData.add(" 5 to 10 Kms ");
        deliverydistanceChoosingSpinnerData.add("More than 10 Kms");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(searchOrdersUsingMobileNumber.this,android.R.layout.simple_spinner_item, deliverydistanceChoosingSpinnerData);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliverDistance_Spinner.setAdapter(arrayAdapter);
        selected_DeliveryDistanceRange_spinner = "All";



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
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);

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
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );
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
                    Toast.makeText(searchOrdersUsingMobileNumber.this, "There is no Order  on " + SlotDate, Toast.LENGTH_LONG).show();
                    ordersList.clear();
                    array_of_orderId.clear();
                    loadingpanelmask.setVisibility(View.GONE);
                    loadingPanel.setVisibility(View.GONE);
                    manageOrders_ListView.setVisibility(View.GONE);
                    mobile_orderinstruction.setText("No Order today");

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
        Volley.newRequestQueue(searchOrdersUsingMobileNumber.this).add(jsonObjectRequest);




    }








    private void getOrderDetailsUsingOrderPlacedDate(String date, String vendorKey, String selectedStatus) {
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey + "?orderplaceddate="+date+"&vendorkey="+vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        mobile_jsonString =response.toString();
                        try {
                            String ordertype="#";
                            array_of_orderId.clear();

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            //converting jsonSTRING into array
                            JSONObject jsonObject = new JSONObject(mobile_jsonString);
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
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                                }


                            }

                            //Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);

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

                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());

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
            Adjusting_Widgets_Visibility(true);
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
                        ordertype = String.valueOf(json.get("ordertype"));
                    }
                    else{
                        ordertype="#";
                        manageOrdersPojoClass.orderType ="";
                    }
                    if (ordertype.toUpperCase().equals(Constants.APPORDER)) {
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

            displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner,selectedTimeRange_spinner, selected_DeliveryDistanceRange_spinner);
         //   appOrdersCount_textwidget.setText(String.valueOf(array_of_orderId.size()));

        } catch (JSONException e) {
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);
        }
    }







    private void displayorderDetailsinListview(String orderStatus, @NotNull List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner, String selectedTimeRange_spinner, String selected_DeliveryDistanceRange_spinner) {
        Adjusting_Widgets_Visibility(true);


        sorted_OrdersList.clear();

        double selectedDeliveryDistance,deliverydistancefromarray,minimumdeliverydistance;
        if(selected_DeliveryDistanceRange_spinner.equals("All")){
            selectedDeliveryDistance = 0;
            minimumdeliverydistance = 0;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals(" Less than 3 Kms")){
            selectedDeliveryDistance = 2.9;
            minimumdeliverydistance = 0;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals(" 3 to 5 Kms ")){
            selectedDeliveryDistance = 4.9;
            minimumdeliverydistance = 3;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals(" 5 to 10 Kms ")){
            selectedDeliveryDistance = 9.9;
            minimumdeliverydistance = 5;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals("More than 10 Kms")){
            selectedDeliveryDistance = 10;
            minimumdeliverydistance = 10;
        }
        else{
            selectedDeliveryDistance = 0;
            minimumdeliverydistance = 0;
            Toast.makeText(searchOrdersUsingMobileNumber.this, "Delivery Distance Filter is not applied", Toast.LENGTH_LONG).show();

        }

        for (int i = 0; i < ordersList.size(); i++) {
          try {
              //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

              final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
              final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
              String slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange().toUpperCase();
              String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();
             Log.i(" slotname n   " ,slotname);
               Log.i(" slottimerange n " , slottimerange);

              if(selectedTimeRange_spinner.equals(Constants.EXPRESS_DELIVERY_SLOTNAME)&&slottimerange.equals("90 MINS")) {
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
                  modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                  deliverydistancefromarray = Double.parseDouble( modal_manageOrders_forOrderDetailList.getDeliverydistance());

                  modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                  modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                  modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                  modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                  try {
                      modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                  } catch (Exception e) {
                      modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                  }

                  if (selected_DeliveryDistanceRange_spinner.equals("All")) {

                      sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                  }
                  else{
                      if(selectedDeliveryDistance==10){
                          if(selectedDeliveryDistance<deliverydistancefromarray){
                              sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                          }
                      }
                      else{
                          if((selectedDeliveryDistance>=deliverydistancefromarray) && ( minimumdeliverydistance < deliverydistancefromarray)){
                              sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                          }
                      }
                  }
              }

                if(selectedTimeRange_spinner.equals(slottimerange)||selectedTimeRange_spinner.equals("All")) {
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
                    modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                    deliverydistancefromarray = Double.parseDouble( modal_manageOrders_forOrderDetailList.getDeliverydistance());


                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    try {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                    } catch (Exception e) {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                    }

                    if (selected_DeliveryDistanceRange_spinner.equals("All")) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }
                    else{
                        if(selectedDeliveryDistance==10){
                            if(selectedDeliveryDistance<deliverydistancefromarray){
                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                            }
                        }
                        else{
                                if((selectedDeliveryDistance>=deliverydistancefromarray) && ( minimumdeliverydistance < deliverydistancefromarray)){
                                    sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                                }

                        }
                    }
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



                appOrdersCount_textwidget.setText(String.valueOf(sorted_OrdersList.size()));


                setAdapter(sorted_OrdersList);




            }
            else{
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                mobile_orderinstruction.setText("There is No data for this Slot");
                mobile_orderinstruction.setVisibility(View.VISIBLE);
                appOrdersCount_textwidget.setText(String.valueOf(sorted_OrdersList.size()));

            }
        }
        catch (Exception e){
            e.printStackTrace();
            if (sorted_OrdersList.size() > 0) {
                setAdapter(sorted_OrdersList);

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

    private void setAdapter(List<Modal_ManageOrders_Pojo_Class> sorted_ordersList) {

        if(screenInches>8){

            adapter_PosSearchOrders_usingMobileNumber_listView = new Adapter_Pos_SearchOrders_usingMobileNumber(searchOrdersUsingMobileNumber.this, sorted_ordersList, searchOrdersUsingMobileNumber.this, orderStatus);
            manageOrders_ListView.setAdapter(adapter_PosSearchOrders_usingMobileNumber_listView);
        }else {
            adapter_mobileSearchOrders_usingMobileNumber_listView = new Adapter_Mobile_SearchOrders_usingMobileNumber_ListView(searchOrdersUsingMobileNumber.this, sorted_ordersList, searchOrdersUsingMobileNumber.this, orderStatus);
            manageOrders_ListView.setAdapter(adapter_mobileSearchOrders_usingMobileNumber_listView);
        }


        loadingpanelmask.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.GONE);
        manageOrders_ListView.setVisibility(View.VISIBLE);
        mobile_orderinstruction.setVisibility(View.GONE);
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
        //System.out.println("todays Date  " + CurrentDate);
       // System.out.println("PreviousdayDate Date  " + PreviousdayDate);


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


    @Override
    public void onBackPressed() {
        if(filterLayout.getVisibility()==View.VISIBLE){
            filterLayout.setVisibility(View.GONE);
            if(!isFilterChanged){
                Toast.makeText(searchOrdersUsingMobileNumber.this,"Filter is not changed",Toast.LENGTH_LONG).show();
            }

        }
        else {
            super.onBackPressed();
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