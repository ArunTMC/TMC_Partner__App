package com.meatchop.tmcpartner.settings;

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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.TimeZone;

public class Edit_Or_CancelTheOrders extends AppCompatActivity {
private  String OrderDetailsResultjsonString,CreditResultjsonString,CurrentDate,CurrentDay,TodaysDate,DateString,PreviousDateString,vendorKey,vendorname;
private double screenInches;
public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
public static List<Modal_ManageOrders_Pojo_Class> ordersList;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_CreditedOrdersList;
    public static List<Modal_ManageOrders_Pojo_Class> CreditedordersList;
    boolean isChecked = false;
    boolean isDisplayorderListmethodCalled =false;

    TextView fetchData,appOrdersCount_textwidget,dateSelector_text,mobile_orderinstruction, mobile_nameofFacility_Textview;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;
    ListView manageOrders_ListView;
    public LinearLayout PrintReport_Layout;
    public LinearLayout generateReport_Layout;
    public LinearLayout dateSelectorLayout;
    public LinearLayout creditOrdersTextLayout,creditOrderscheckboxLayout;
    public LinearLayout creditOrderscheckLayout;
    public static CheckBox showcreditorderscheckbox;
    DatePickerDialog datepicker;

    static Adapter_Edit_Or_CancelTheOrders adapter_edit_or_cancelTheOrders;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;
    public static List<String> array_of_creditedOrderId;

    public static List<String> array_of_orderId;
    static boolean isSearchButtonClicked = false;



    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_or_cancel_the_orders);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", "vendor_1"));
            vendorname = (shared.getString("VendorName", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
          //  orderdetailsnewschema = true;

            try {
                ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
                screenInches = screenSizeOfTheDevice.getDisplaySize(Edit_Or_CancelTheOrders .this);
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
                //    Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

                }
                catch (Exception e1){
                    e1.printStackTrace();
                }


            }

        }
        catch (Exception e){
            e.printStackTrace();
        }



        CreditedordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_CreditedOrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_creditedOrderId = new ArrayList<>();

        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        array_of_orderId = new ArrayList<>();
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        fetchData = findViewById(R.id.fetchData);

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

        creditOrderscheckLayout = findViewById(R.id.creditOrderscheckLayout);
        creditOrdersTextLayout = findViewById(R.id.creditOrdersTextLayout);
        showcreditorderscheckbox = findViewById(R.id.showcreditorderscheckbox);

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        //showProgressBar(true);

        mobile_nameofFacility_Textview.setText(vendorname);


        try{
            CreditedordersList.clear();
            sorted_CreditedOrdersList.clear();
            array_of_creditedOrderId.clear();

        
            //Now we are creating sheet

           // Adjusting_Widgets_Visibility(true);
           

            isSearchButtonClicked = false;
            ordersList.clear();
            sorted_OrdersList.clear();
            array_of_orderId.clear();

            dateSelector_text.setText(Constants.Empty_Date_Format);
            DateString = (Constants.Empty_Date_Format);
            dateSelector_text.setText(DateString);
          /*
            String Todaysdate = getDatewithNameoftheDay();
            PreviousDateString = getDatewithNameofthePreviousDay();
           TodaysDate = getDate();
            PreviousDateString = getDatewithNameofthePreviousDay();
            if(orderdetailsnewschema){
                String dateAsOldFormat =convertnewFormatDateintoOldFormat(Todaysdate);
                dateSelector_text.setText(dateAsOldFormat);
                callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);


            }
            else{
              //  dateSelector_text.setText(Todaysdate);

                    getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString,Todaysdate, vendorKey);

            }
            
            */
        }
        catch (Exception e){
            e.printStackTrace();
        }


        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DateString.equals(Constants.Empty_Date_Format)) {
                    Toast.makeText(Edit_Or_CancelTheOrders.this, "Select the Date First !!! ", Toast.LENGTH_SHORT).show();
                } else {

                    ordersList.clear();
                    sorted_OrdersList.clear();
                    array_of_orderId.clear();

                    Adjusting_Widgets_Visibility(true);
                    String todatestring = dateSelector_text.getText().toString();
                    try {
                        PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    isSearchButtonClicked = false;
                    getCreditOrders();

                    if (orderdetailsnewschema) {
                        String dateAsOldFormat = convertOldFormatDateintoNewFormat(todatestring);
                        callVendorOrderDetailsSeviceAndInitCallBack(dateAsOldFormat, dateAsOldFormat, vendorKey);


                    }
                    else
                        {
                        // dateSelector_text.setText(TodaysDate);

                        getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, todatestring, vendorKey);

                    }
                }
            }
        });
        
        /*
        
        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showcreditorderscheckbox.isChecked()){
                    CreditedordersList.clear();
                    sorted_CreditedOrdersList.clear();
                    array_of_creditedOrderId.clear();
                    Adjusting_Widgets_Visibility(true);
                  //  getCreditOrders();

                }
                else {


                    ordersList.clear();
                    sorted_OrdersList.clear();
                    array_of_orderId.clear();

                    Adjusting_Widgets_Visibility(true);
                    String todatestring = dateSelector_text.getText().toString();

                    PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);


                    isSearchButtonClicked = false;

                    if(orderdetailsnewschema){
                        String dateAsOldFormat =convertOldFormatDateintoNewFormat(todatestring);
                        callVendorOrderDetailsSeviceAndInitCallBack(dateAsOldFormat,dateAsOldFormat,vendorKey);


                    }
                    else{
                       // dateSelector_text.setText(TodaysDate);

                        getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, todatestring, vendorKey);

                    }

                }
            }
        });


         */

        creditOrderscheckLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox : "+showcreditorderscheckbox.isChecked(), Toast.LENGTH_SHORT).show();
                Log.d(Constants.TAG, "checkbox : "+showcreditorderscheckbox.isChecked());
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked = false;
                isDisplayorderListmethodCalled =false;
                if(showcreditorderscheckbox.isChecked()){
                    isChecked = true;
                    showcreditorderscheckbox.setChecked(false);
                    dateSelectorLayout.setVisibility(View.VISIBLE);
                    creditOrdersTextLayout.setVisibility(View.GONE);
                    DisplayOrderListDatainListView(ordersList,false);

                }
                else{
                    isChecked = false;

                    creditOrdersTextLayout.setVisibility(View.VISIBLE);
                    dateSelectorLayout.setVisibility(View.GONE);
                    showcreditorderscheckbox.setChecked(true);
                    DisplayOrderListDatainListView(CreditedordersList,true);

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
               // Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox 1 : "+showcreditorderscheckbox.isChecked(), Toast.LENGTH_SHORT).show();
                Log.d(Constants.TAG, "checkbox 1 : "+showcreditorderscheckbox.isChecked());
                isDisplayorderListmethodCalled =false;

                if(showcreditorderscheckbox.isChecked()){
                    DisplayOrderListDatainListView(CreditedordersList,true);
                }
                else {
                    DisplayOrderListDatainListView(ordersList,false);
                }
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int textlength = mobile_search_barEditText.getText().toString().length();
                isSearchButtonClicked = true;
                showSearchBarEditText();

                showKeyboard(mobile_search_barEditText);
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
                Toast.makeText(Edit_Or_CancelTheOrders.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });







        mobile_search_barEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sorted_OrdersList.clear();
                sorted_CreditedOrdersList.clear();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                sorted_OrdersList.clear();
                sorted_CreditedOrdersList.clear();

                isSearchButtonClicked =true;
                String mobileNo = (editable.toString());
                if(!mobileNo.equals("")) {
                    String orderstatus = "";
                    //Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox 2 : "+showcreditorderscheckbox.isChecked(), Toast.LENGTH_SHORT).show();
                    Log.d(Constants.TAG, "checkbox 2 : "+showcreditorderscheckbox.isChecked());

                    if(showcreditorderscheckbox.isChecked()) {


                        for (int i = 0; i < CreditedordersList.size(); i++) {
                            try {
                                //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));
                                mobileNo = mobileNo;
                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = CreditedordersList.get(i);
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
                                    modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();

                                    modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();

                                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();

                                    sorted_CreditedOrdersList.add(modal_manageOrders_forOrderDetailList1);


                                }


                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }
                    else {
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
                                    modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();

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
                    }
                    try {
                        //Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox 3 : "+showcreditorderscheckbox.isChecked(), Toast.LENGTH_SHORT).show();
                        Log.d(Constants.TAG, "checkbox 3 : "+showcreditorderscheckbox.isChecked());
                        isDisplayorderListmethodCalled =false;

                        if(showcreditorderscheckbox.isChecked()){
                            DisplayOrderListDatainListView(sorted_CreditedOrdersList,true);

                        }
                        else{
                            DisplayOrderListDatainListView(sorted_OrdersList,false);

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

    private void getCreditOrders() {

        Adjusting_Widgets_Visibility(true);

        CreditedordersList.clear();
        sorted_CreditedOrdersList.clear();
       array_of_creditedOrderId.clear();
        String apiToCallCreditOrdersFromOrderDetails = "";

        if (orderdetailsnewschema) {
            apiToCallCreditOrdersFromOrderDetails = Constants.api_GetVendorOrderDetailsUsingpaymentmode_vendorkey + "?paymentmode=CREDIT"+"&vendorkey="+vendorKey;
        }
        else{
            apiToCallCreditOrdersFromOrderDetails = Constants.api_GetOrderDetailsusingPaymentmode_vendorkey + "?paymentmode=CREDIT"+"&vendorkey="+vendorKey;

        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,apiToCallCreditOrdersFromOrderDetails ,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            CreditResultjsonString = response.toString();
                           // Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox 4 : "+showcreditorderscheckbox.isChecked(), Toast.LENGTH_SHORT).show();
                            Log.d(Constants.TAG, "checkbox 4 : "+showcreditorderscheckbox.isChecked());

                            convertingJsonStringintoArray( CreditResultjsonString,true);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Edit_Or_CancelTheOrders.this, "There is no Credited Orders " , Toast.LENGTH_LONG).show();

                    CreditedordersList.clear();
                    sorted_CreditedOrdersList.clear();
                    array_of_creditedOrderId.clear();



                    error.printStackTrace();


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
        Volley.newRequestQueue(Edit_Or_CancelTheOrders.this).add(jsonObjectRequest);











    }


    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(Edit_Or_CancelTheOrders.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();
                            DisplayOrderListDatainListView(ordersList,false);

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
                            manageOrders_ListView.setVisibility(View.GONE);
                            mobile_orderinstruction.setVisibility(View.VISIBLE);
                            mobile_orderinstruction.setText("After Selecting the Date !! Click Fetch Data");

                            /*
                            if(orderdetailsnewschema){
                                String dateAsNewFormat =convertOldFormatDateintoNewFormat(DateString);
                                callVendorOrderDetailsSeviceAndInitCallBack(dateAsNewFormat,dateAsNewFormat,vendorKey);


                            }
                            else{
                                getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(PreviousDateString, DateString, vendorKey);

                            }

                             */

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }



    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey) {
        Adjusting_Widgets_Visibility(true);
        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();

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
               // jsonString = String.valueOf(jsonObject);
                ordersList = orderslist_fromResponse;
                DisplayOrderListDatainListView(ordersList,false);

                //convertingJsonStringintoArray(orderStatus,mobile_jsonString);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + "That didn't work!");
                Adjusting_Widgets_Visibility(false);
            }
        };
       Context mContext = Edit_Or_CancelTheOrders.this;
        mVolleyService = new VendorOrdersTableService(mResultCallback,mContext);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }




    private void getOrderDetailsUsingOrderSlotDateandOrderPlaceddate(String previousDateString, String todaysdate, String vendorKey) {
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );
        Adjusting_Widgets_Visibility(true);

        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();





        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_AppOrders_and_PosOrders + "?slotdate="+todaysdate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDateString,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            OrderDetailsResultjsonString = response.toString();
                            //Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox 5 : "+showcreditorderscheckbox.isChecked(), Toast.LENGTH_SHORT).show();
                            Log.d(Constants.TAG, "checkbox 5 : "+showcreditorderscheckbox.isChecked());

                            convertingJsonStringintoArray( OrderDetailsResultjsonString, false);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(Edit_Or_CancelTheOrders.this, "There is no Order  on " + todaysdate, Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(Edit_Or_CancelTheOrders.this).add(jsonObjectRequest);







    }

    private void convertingJsonStringintoArray(String orderDetailsResultjsonString, boolean isCreditOrdersData) {
        try {
            String ordertype="#",orderid="";
            sorted_OrdersList.clear();
           // Adjusting_Widgets_Visibility(true);
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
                            String orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                            manageOrdersPojoClass.orderplacedtime_in_long = getLongValuefortheDate(orderplacedtime);

                        }
                        else{
                            manageOrdersPojoClass.orderplacedtime_in_long = "";
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
                            if(json.has("usermobileno")){
                                manageOrdersPojoClass.usermobile =  String.valueOf(json.get("usermobileno"));

                            }
                            else{
                                manageOrdersPojoClass.usermobile ="";
                            }                        }


                        if(json.has("vendorkey")){
                            manageOrdersPojoClass.vendorkey =  String.valueOf(json.get("vendorkey"));

                        }
                        else{
                            manageOrdersPojoClass.vendorkey ="vendor_1";
                        }




                    if(json.has("vendorname")){
                        manageOrdersPojoClass.vendorname =  String.valueOf(json.get("vendorname"));

                    }
                    else{
                        manageOrdersPojoClass.vendorname ="";
                    }



                    if(json.has("orderdetailskey")){
                            manageOrdersPojoClass.orderdetailskey =  String.valueOf(json.get("orderdetailskey"));

                        }
                        else{
                            manageOrdersPojoClass.orderdetailskey ="";
                            if(json.has("key")){
                                manageOrdersPojoClass.orderdetailskey =  String.valueOf(json.get("key"));

                            }
                            else{
                                manageOrdersPojoClass.orderdetailskey ="";
                            }
                        }


                        if(json.has("orderdeliverytime")){
                            manageOrdersPojoClass.orderdeliveredtime =  String.valueOf(json.get("orderdeliverytime"));


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




                    String slotdate = "";
                    if(json.has("slotdate")){
                            manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));
                        slotdate = String.valueOf(json.get("slotdate"));
                        }
                        else{
                        slotdate = "";
                            manageOrdersPojoClass.slotdate ="";
                        }


                        if(json.has("slotname")){
                            manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                        }
                        else{
                            manageOrdersPojoClass.slotname ="";
                        }
                      /*  if(json.has("slottimerange")){
                            manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                        }
                        else{
                            manageOrdersPojoClass.slottimerange ="";
                        }

                       */
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


                        if(json.has("userkey")){
                            manageOrdersPojoClass.userkey =  String.valueOf(json.get("userkey"));

                        }
                        else{
                            manageOrdersPojoClass.userkey ="";
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




                                if (json.has("deliveryusername")) {
                                    manageOrdersPojoClass.deliveryPartnerName = String.valueOf(json.get("deliveryusername"));

                                }
                                else{
                                    manageOrdersPojoClass.deliveryPartnerName ="";
                                }
                                if (json.has("deliveryuserkey")) {
                                    manageOrdersPojoClass.deliveryPartnerKey = String.valueOf(json.get("deliveryuserkey"));


                                }
                                else{
                                    manageOrdersPojoClass.deliveryPartnerKey ="";
                                }
                                if (json.has("deliveryusermobileno")) {
                                    manageOrdersPojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("deliveryusermobileno"));

                                }
                                else{
                                    manageOrdersPojoClass.deliveryPartnerMobileNo ="";
                                }




                    Log.d(Constants.TAG, "checkbox 6 : "+isCreditOrdersData);

                   // Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox 6 : "+isCreditOrdersShowing, Toast.LENGTH_SHORT).show();

                        if(isCreditOrdersData) {

                            CreditedordersList.add(manageOrdersPojoClass);
                        }
                        else{
                            ordersList.add(manageOrdersPojoClass);

                        }

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }

                if(arrayLength - i1 == 1){
                    isDisplayorderListmethodCalled =false;

                    if(showcreditorderscheckbox.isChecked()) {
                        DisplayOrderListDatainListView(CreditedordersList,true);

                    }
                    else{
                        if(!isCreditOrdersData) {
                            DisplayOrderListDatainListView(ordersList,false);
                        }

                    }
                }

            }


            Log.d(Constants.TAG, "checkbox 7 : "+showcreditorderscheckbox.isChecked());



        } catch (JSONException e) {
            //Adjusting_Widgets_Visibility(false);

            e.printStackTrace();
        }
    }

    private void DisplayOrderListDatainListView(List<Modal_ManageOrders_Pojo_Class> ordersList,boolean isShowCreditOrdersss) {
        try {
            if(!isDisplayorderListmethodCalled) {
                isDisplayorderListmethodCalled =true;

                Adjusting_Widgets_Visibility(true);
            }
            if (ordersList.size() > 0) {
                if(isShowCreditOrdersss){
                    Collections.sort(ordersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                            String tokenNo_1 = object1.getOrderplacedtime_in_long();
                            String tokenNo_2 = object2.getOrderplacedtime_in_long();

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
                }
                else {
                    Collections.sort(ordersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
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

                }


               // Toast.makeText(Edit_Or_CancelTheOrders.this, "checkbox : "+showcreditorderscheckbox.isChecked(), Toast.LENGTH_SHORT).show();

                Log.d(Constants.TAG, "adapter : "+isChecked);


                 adapter_edit_or_cancelTheOrders = new Adapter_Edit_Or_CancelTheOrders(Edit_Or_CancelTheOrders.this, ordersList, Edit_Or_CancelTheOrders.this,showcreditorderscheckbox.isChecked());
                manageOrders_ListView.setAdapter(adapter_edit_or_cancelTheOrders);

                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));

                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.VISIBLE);
                mobile_orderinstruction.setVisibility(View.GONE);


            }
            else{
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                if(showcreditorderscheckbox.isChecked()){
                    mobile_orderinstruction.setText("There is No Credit Order ");

                }
                else {
                    mobile_orderinstruction.setText("There is No Order .");
                }
                mobile_orderinstruction.setVisibility(View.VISIBLE);
                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));

            }

        }
        catch (Exception e){
            e.printStackTrace();
            if (ordersList.size() > 0) {

                Adapter_Edit_Or_CancelTheOrders adapter_edit_or_cancelTheOrders = new Adapter_Edit_Or_CancelTheOrders(Edit_Or_CancelTheOrders.this, ordersList, Edit_Or_CancelTheOrders.this);
                manageOrders_ListView.setAdapter(adapter_edit_or_cancelTheOrders);


                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.VISIBLE);
                mobile_orderinstruction.setVisibility(View.GONE);

                Adjusting_Widgets_Visibility(false);

            } else {
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                if(showcreditorderscheckbox.isChecked()){
                    mobile_orderinstruction.setText("There is No Credit Order ");

                }
                else {
                    mobile_orderinstruction.setText("There is No Order ");
                }
                mobile_orderinstruction.setVisibility(View.VISIBLE);
               Adjusting_Widgets_Visibility(false);

            }
        }

    }


    private String getTomorrowsDate() {

        Date todaysDate = Calendar.getInstance().getTime();
        System.out.println("nextDate " + todaysDate);

        String next_day = "";
        //calander_view.setCurrentDayBackgroundColor(context.getResources().getColor(R.color.gray_color));
        SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        dateFormatForDisplaying.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String date_format = dateFormatForDisplaying.format(todaysDate);
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E",Locale.ENGLISH);
        simpleDateformat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        // the day of the week abbreviated
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


    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();
        if(orderdetailsnewschema) {


            calendar.add(Calendar.DATE, -1);


            Date c1 = calendar.getTime();
            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));






            String PreviousdayDate = day.format(c1);


            return PreviousdayDate;

        }
        else {


            calendar.add(Calendar.DATE, -1);


            Date c1 = calendar.getTime();

            SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
            previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



            String PreviousdayDay = previousday.format(c1);


            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



            String PreviousdayDate = df1.format(c1);
            PreviousdayDate = PreviousdayDay + ", " + PreviousdayDate;
            //System.out.println("todays Date  " + CurrentDate);
            // System.out.println("PreviousdayDate Date  " + PreviousdayDate);


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


    private String getDatewithNameofthePreviousDayfromSelectedDay2(String sDate) {
        String yesterdayAsString = "";
        try {
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


            String PreviousdayDate = df1.format(c1);
             yesterdayAsString = PreviousdayDay + ", " + PreviousdayDate;
            //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        } catch (Exception e) {
            yesterdayAsString = "";
            e.printStackTrace();
        }
        return yesterdayAsString;

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




    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        if(orderdetailsnewschema) {

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDate = day.format(c);

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
        if(showcreditorderscheckbox.isChecked()){
            dateSelectorLayout.setVisibility(View.GONE);
            mobile_search_button.setVisibility(View.VISIBLE);
            mobile_search_close_btn.setVisibility(View.GONE);
            mobile_search_barEditText.setVisibility(View.GONE);
            creditOrdersTextLayout.setVisibility(View.VISIBLE);

        }
        else{
            dateSelectorLayout.setVisibility(View.VISIBLE);
            mobile_search_button.setVisibility(View.VISIBLE);
            mobile_search_close_btn.setVisibility(View.GONE);
            mobile_search_barEditText.setVisibility(View.GONE);
            creditOrdersTextLayout.setVisibility(View.GONE);
            newOrdersSync_Layout.setVisibility(View.VISIBLE);

        }

    }

    private void showSearchBarEditText() {


            dateSelectorLayout.setVisibility(View.GONE);
            mobile_search_button.setVisibility(View.GONE);
            mobile_search_close_btn.setVisibility(View.VISIBLE);
            mobile_search_barEditText.setVisibility(View.VISIBLE);
            creditOrdersTextLayout.setVisibility(View.GONE);
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

}