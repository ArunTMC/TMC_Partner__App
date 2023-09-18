package com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.settings.DeviceListActivity;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.settings.Modal_MenuItemStockAvlDetails;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.vendor_order_tracking_details.VendorOrdersTableService;
import com.pos.printer.AsyncEscPosPrint;
import com.pos.printer.AsyncEscPosPrinter;
import com.pos.printer.AsyncUsbEscPosPrint;
import com.pos.printer.Modal_USBPrinter;
import com.pos.printer.usb.UsbPrintersConnectionsLocal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static android.content.Context.MODE_PRIVATE;
import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Pos_ManageOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class Pos_ManageOrderFragment extends Fragment {
    TextView orderinstruction,nameofFacility_Textview;
    Button new_Order_widget, confirmed_Order_widget,ready_Order_widget,transist_Order_widget,delivered_Order_widget;
    ImageView search_button,search_close_btn;
    EditText search_barEditText;
    String jsonString,orderStatus,vendorKey,vendorname,slotName = "EXPRESS DELIVERY";
    ListView manageOrders_ListView;
    LinearLayout loadingPanel,loadingpanelmask,newOrdersSync_Layout,preorderdate_layout;
    List<Modal_ManageOrders_Pojo_Class> websocket_OrdersList;
    static List<Modal_ManageOrders_Pojo_Class> ordersList;
    Pos_Dashboard_Screen pos_dashboard_screen;
    public static String completemenuItem;
    static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay,TodaysDate;
    SwipeRefreshLayout swipeRefreshLayout;
    List<String> slotnameChoosingSpinnerData;
    Spinner slotType_Spinner;
    Button todaysOrder_widget,tomorrowsorder_widget;
    static Adapter_Pos_ManageOrders_ListView manageOrdersListViewAdapter;
    private String SERVER_PATH = "wss://hx9itd7ji2.execute-api.ap-south-1.amazonaws.com/Dev";
     WebSocket webSocket;
    private Context mContext;
    BottomNavigationView bottomNavigationView;
    int selected_OrderType =0;
    String portName = "USB";
    RadioGroup radioBtn_Grp;
    int portSettings=0,totalGstAmount=0;
    boolean isRadioButtonSelected =false;
    boolean isSearchButtonClicked = false;
    boolean isnewOrdersSyncButtonClicked = false;
    boolean isUSBPrintReciptMethodCalled = false;


    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";

    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;


    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter mBtAdapter;

    String mConnectedDeviceName ;
    boolean isPrinterCnnected = false;
    String printerName = "",TAG = "Tag",mobile_jsonString="";;
    String printerStatus= "";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.ManageOrders";

    Modal_USBPrinter modal_usbPrinter = new Modal_USBPrinter();
    boolean orderdetailsnewschema = false  ,localDBcheck = false;
    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean  isVendorOrdersTableServiceCalled = false;



    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
    boolean isinventorycheck =false;
    List<Modal_MenuItem>MenuItem = new ArrayList<>();
    public Pos_ManageOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * =@param param2 Parameter 2.
     * @return A new instance of fragment Pos_ManageOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Pos_ManageOrderFragment newInstance( String param2) {
        Pos_ManageOrderFragment fragment = new Pos_ManageOrderFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getWindow().getContext();
        //getMenuItemusingStoreId();
        new NukeSSLCerts();
        NukeSSLCerts.nuke();



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bottomNavigationView = ((Pos_Dashboard_Screen) requireActivity()).findViewById(R.id.bottomnav);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,new IntentFilter("YOUR"));

        return inflater.inflate(R.layout.pos_manage_order_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        websocket_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        slotnameChoosingSpinnerData= new ArrayList<>();
       // swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        todaysOrder_widget = view.findViewById(R.id.todaysOrder_widget);
        tomorrowsorder_widget = view.findViewById(R.id.tomorrowsorder_widget);
        preorderdate_layout = view.findViewById(R.id.preorderdate_layout);
        radioBtn_Grp =view.findViewById(R.id.radioBtn_Grp);

        slotType_Spinner=view.findViewById(R.id.slotType_Spinner);
        manageOrders_ListView = view.findViewById(R.id.manageOrders_ListView);
        orderinstruction =view.findViewById(R.id.orderinstruction);
        //
        nameofFacility_Textview = view.findViewById(R.id.nameofFacility_Textview);
        search_button=view.findViewById(R.id.search_button);
        search_barEditText = view.findViewById(R.id.search_barEdit);
        search_close_btn = view.findViewById(R.id.search_close_btn);

        //
        new_Order_widget = view.findViewById(R.id.new_Order_widget);
        confirmed_Order_widget =view.findViewById(R.id.confirmed_Order_widget);
        ready_Order_widget = view.findViewById(R.id.ready_Order_widget);
        transist_Order_widget = view.findViewById(R.id.transist_Order_widget);
        delivered_Order_widget = view.findViewById(R.id.delivered_Order_widget);


        loadingpanelmask = view.findViewById(R.id.loadingpanelmask);
        loadingPanel = view.findViewById(R.id.loadingPanel);
        newOrdersSync_Layout = view.findViewById(R.id.newOrdersSync_Layout);

        SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", ""));
        vendorname = (shared.getString("VendorName", ""));
        StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (shared.getString("VendorPincode", ""));
        StoreLanLine = (shared.getString("VendorMobileNumber", ""));
        orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
        localDBcheck = (shared.getBoolean("localdbcheck", false));
        isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));

        //orderdetailsnewschema = true;


        nameofFacility_Textview.setText(vendorname);
        SharedPreferences shared2 = requireContext().getSharedPreferences("CurrentSelectedStatus", MODE_PRIVATE);
        orderStatus = (shared2.getString("currentstatus", ""));
        TodaysDate=getDate();

        MenuItem.clear();
        try{

            if(localDBcheck) {
                getDataFromSQL();
            }
            else{
                //   MenuItems=getData();

                //   completemenuItem= getMenuItemfromString(MenuItems);
                getMenuItemArrayFromSharedPreferences();
            }

        }

        catch (Exception e){
            e.printStackTrace();
        }



        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Loading ..... Please wait",Toast.LENGTH_SHORT).show();

            }
        });

        modal_usbPrinter = new Modal_USBPrinter();
        try{

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name","Itemname");
            jsonArray.put(jsonObject);
            modal_usbPrinter.orderstatus = "selectedOrder.getOrderstatus()";
            modal_usbPrinter.userMobile = "selectedOrder.getUsermobile()";
            modal_usbPrinter.tokenno = "selectedOrder.getTokenno()";
            modal_usbPrinter.payableAmount = "selectedOrder.getPayableamount()";
            modal_usbPrinter.itemdesp = jsonArray;
            modal_usbPrinter.orderid = "selectedOrder.getOrderid()";
            modal_usbPrinter.payment_mode = "selectedOrder.getPaymentmode()";
            modal_usbPrinter.finalCouponDiscountAmount = "selectedOrder.getCoupondiscamount()";
            modal_usbPrinter.useraddress = "selectedOrder.getUseraddress()";
            modal_usbPrinter.ordertype = "selectedOrder.getOrderType()";
            modal_usbPrinter.slotname = "selectedOrder.getSlotname()";
            modal_usbPrinter.slotdate= "selectedOrder.getSlotdate()";
            modal_usbPrinter.slottimerange ="selectedOrder. getSlottimerange()";
            modal_usbPrinter.deliverytype = "selectedOrder.getDeliverytype()";
            modal_usbPrinter.notes = "selectedOrder.getNotes()";
            modal_usbPrinter.orderplacedtime =" selectedOrder.getOrderplacedtime()";
            modal_usbPrinter.orderdetailskey = "selectedOrder.getOrderdetailskey()";
            modal_usbPrinter.deliverydistance ="selectedOrder.getDeliverydistance()";
            modal_usbPrinter.payment_mode ="selectedOrder. getPaymentmode()";
            modal_usbPrinter.deliveryamount ="selectedOrder. getDeliveryamount()";



        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(selected_OrderType == 0 ){
            showProgressBar(true);
            showOrderInstructionText(false);
            isSearchButtonClicked =false;


            String Todaysdate = getDatewithNameoftheDay();
            isSearchButtonClicked =false;
            String PreviousDaydate = getDatewithNameofthePreviousDay();

            if(orderdetailsnewschema){
                callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);


            }
            else{
                getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

            }




        }
        if(selected_OrderType == 1 ) {
            showProgressBar(true);
            showOrderInstructionText(false);
            isSearchButtonClicked =false;

            String Todaysdate = getDatewithNameoftheDay();

            String TomorrowsDate = getTomorrowsDate();


            if(orderdetailsnewschema){
                callVendorOrderDetailsSeviceAndInitCallBack(TomorrowsDate,TomorrowsDate,vendorKey);


            }
            else{
                getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);

            }




        }
     /*   if(selected_OrderType == 2 ) {
            showProgressBar(true);
            showOrderInstructionText(false);
            isSearchButtonClicked =false;
            String Todaysdate = getDatewithNameoftheDay();

            String TomorrowsDate = getTomorrowsDate();
            getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);
        }

      */
/*
       setDataForSpinner();

        slotType_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_OrderType = position;
                //Log.i("Spinner","position   "+position);
                if(selected_OrderType == 0 ){
                    showProgressBar();

                    getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);

                }
                if(selected_OrderType == 1 ) {
                    showProgressBar();

                    String Todaysdate = getDatewithNameoftheDay();
                    getOrderDetailsUsingOrderSlotDate(Todaysdate, vendorKey, orderStatus);
                }
                if(selected_OrderType == 2 ) {
                    showProgressBar();

                    String TomorrowsDate = getTomorrowsDate();
                    getOrderDetailsUsingOrderSlotDate(TomorrowsDate, vendorKey, orderStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

 */

        todaysOrder_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todaysOrder_widget.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_selected_button_background));
                todaysOrder_widget.setTextColor(Color.WHITE);


                tomorrowsorder_widget.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
                tomorrowsorder_widget.setTextColor(Color.BLACK);
                selected_OrderType =0;

                preorderdate_layout.setVisibility(View.GONE);
                showProgressBar(true);
                showOrderInstructionText(false);
                isSearchButtonClicked =false;
                String Todaysdate = getDatewithNameoftheDay();
                String PreviousDaydate = getDatewithNameofthePreviousDay();

                if(orderdetailsnewschema){
                    callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);


                }
                else{
                    getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

                }






            }
        });
        tomorrowsorder_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preorderdate_layout.setVisibility(View.GONE);
                tomorrowsorder_widget.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_selected_button_background));
                tomorrowsorder_widget.setTextColor(Color.WHITE);
                todaysOrder_widget.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.orange_non_selected_button_background));
                todaysOrder_widget.setTextColor(Color.BLACK);
                selected_OrderType =1;
                        showProgressBar(true);
                        isSearchButtonClicked =false;
                        String Todaysdate = getDatewithNameoftheDay();

                        String TomorrowsDate = getTomorrowsDate();
                if(orderdetailsnewschema){
                    callVendorOrderDetailsSeviceAndInitCallBack(TomorrowsDate,TomorrowsDate,vendorKey);


                }
                else{
                    getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);

                }

            }
        });
/*
        radioBtn_Grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.todaysDate:
                        isRadioButtonSelected =true;
                        selected_OrderType =1;
                        showProgressBar(true);
                        showOrderInstructionText(false);
                        isSearchButtonClicked =false;
                        String PreviousDaydate = getDatewithNameofthePreviousDay();

                        String Todaysdate = getDatewithNameoftheDay();
                        getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);


                        break;
                    case R.id.tomorrowsdate:
                        isRadioButtonSelected =true;

                        selected_OrderType =2;
                        showProgressBar(true);
                        showOrderInstructionText(false);
                        isSearchButtonClicked =false;
                        String Todaysdate1 = getDatewithNameoftheDay();

                        String TomorrowsDate = getTomorrowsDate();
                        getOrderDetailsUsingOrderSlotDate(Todaysdate1, TomorrowsDate, vendorKey, orderStatus);



                        break;

                }
            }
        });


 */
        search_barEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
                                modal_manageOrders_forOrderDetailList1.vendorkey = modal_manageOrders_forOrderDetailList.getVendorkey();
                                modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                                modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                                modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                                modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();

                                modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                                modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                                modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                                modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                                modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                                modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();
                                modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();


                                modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                                modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                                modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                                modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();

                                modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus();

                                modal_manageOrders_forOrderDetailList1.orderplacedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderplacedtime_in_long();
                                modal_manageOrders_forOrderDetailList1.orderconfirmedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime_in_long();
                                modal_manageOrders_forOrderDetailList1.orderreadytime_in_long = modal_manageOrders_forOrderDetailList.getOrderreadytime_in_long();
                                modal_manageOrders_forOrderDetailList1.orderpickeduptime_in_long = modal_manageOrders_forOrderDetailList.getOrderpickeduptime_in_long();
                                modal_manageOrders_forOrderDetailList1.orderdeliveredtime_in_long = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime_in_long();

                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    try {
                        if (sorted_OrdersList.size() > 0) {
                            manageOrders_ListView.setVisibility(View.VISIBLE);
                            orderinstruction.setVisibility(View.GONE);

                            manageOrdersListViewAdapter = new Adapter_Pos_ManageOrders_ListView(mContext, sorted_OrdersList, Pos_ManageOrderFragment.this, orderstatus,MenuItem);
                            manageOrders_ListView.setAdapter(manageOrdersListViewAdapter);
                        } else {
                            manageOrders_ListView.setVisibility(View.GONE);
                            orderinstruction.setVisibility(View.VISIBLE);
                            orderinstruction.setText("No orders found for this Mobile number");


                        }
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
        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sorted_OrdersList.clear();


                if(selected_OrderType == 0 ){
                    showProgressBar(true);
                    showOrderInstructionText(false);
                    isSearchButtonClicked =false;

                    String PreviousDaydate = getDatewithNameofthePreviousDay();

                    String Todaysdate = getDatewithNameoftheDay();
                    if(orderdetailsnewschema){
                        callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);


                    }
                    else{
                        getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

                    }
                }
                if(selected_OrderType == 1 ) {
                    showProgressBar(true);
                    showOrderInstructionText(false);
                    isSearchButtonClicked =false;
                    String Todaysdate = getDatewithNameoftheDay();

                    String TomorrowsDate = getTomorrowsDate();

                    if(orderdetailsnewschema){
                        callVendorOrderDetailsSeviceAndInitCallBack(TomorrowsDate,TomorrowsDate,vendorKey);


                    }
                    else{
                        getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);

                    }



                }

            }
        });


        new_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus=Constants.NEW_ORDER_STATUS;
                showProgressBar(true);
                sorted_OrdersList.clear();
                showOrderInstructionText(false);
                isSearchButtonClicked =false;

                sorted_OrdersList.clear();


                if(selected_OrderType == 0 ){

                    String PreviousDaydate = getDatewithNameofthePreviousDay();

                    String Todaysdate = getDatewithNameoftheDay();
                    if(orderdetailsnewschema){
                        callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);


                    }
                    else{
                        getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

                    }
                }


                if(selected_OrderType == 1 ) {
                    String Todaysdate = getDatewithNameoftheDay();

                    String TomorrowsDate = getTomorrowsDate();

                    if(orderdetailsnewschema){
                        callVendorOrderDetailsSeviceAndInitCallBack(TomorrowsDate,TomorrowsDate,vendorKey);


                    }
                    else{
                        getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);

                    }

                }






               saveCurrentStatusInSharedPref(orderStatus);

                //displayorderDetailsinListview(orderStatus,ordersList,slottypefromSpinner);

                selecting_The_Order_Status(new_Order_widget, confirmed_Order_widget,ready_Order_widget,transist_Order_widget,delivered_Order_widget);

            }
        });


        confirmed_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar(true);
                sorted_OrdersList.clear();
                showOrderInstructionText(false);
                isSearchButtonClicked =false;

                orderStatus=Constants.CONFIRMED_ORDER_STATUS;
                saveCurrentStatusInSharedPref(orderStatus);

                displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);

                selecting_The_Order_Status(confirmed_Order_widget,new_Order_widget,ready_Order_widget,transist_Order_widget,delivered_Order_widget);
            }
        });

        ready_Order_widget.setOnClickListener(view1 -> {
            orderStatus=Constants.READY_FOR_PICKUP_ORDER_STATUS;
            showOrderInstructionText(false);

             showProgressBar(true);
            sorted_OrdersList.clear();
            saveCurrentStatusInSharedPref(orderStatus);
            isSearchButtonClicked =false;

            displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);

            selecting_The_Order_Status(ready_Order_widget,new_Order_widget, confirmed_Order_widget,transist_Order_widget,delivered_Order_widget);
        });

        transist_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus=Constants.PICKEDUP_ORDER_STATUS;
                saveCurrentStatusInSharedPref(orderStatus);
                showOrderInstructionText(false);

                showProgressBar(true);
                sorted_OrdersList.clear();
                isSearchButtonClicked =false;

                displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);

                selecting_The_Order_Status(transist_Order_widget,new_Order_widget, confirmed_Order_widget,ready_Order_widget,delivered_Order_widget);
            }
        });

        delivered_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus=Constants.DELIVERED_ORDER_STATUS;
                sorted_OrdersList.clear();
                saveCurrentStatusInSharedPref(orderStatus);

                 showProgressBar(true);
                showOrderInstructionText(false);
                isSearchButtonClicked =false;

                displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);

                selecting_The_Order_Status(delivered_Order_widget,new_Order_widget, confirmed_Order_widget,ready_Order_widget,transist_Order_widget);
            }
        });

        search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(search_barEditText);

                search_barEditText.setText("");
                isSearchButtonClicked =false;

                closeSearchBarEditText();
                displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);

            }
        });
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = search_barEditText.getText().toString().length();
                isSearchButtonClicked =true;

                showKeyboard(search_barEditText);
                showSearchBarEditText();
            }
        });


    }


    @SuppressLint("Range")
    private void getDataFromSQL() {

        if(tmcMenuItemSQL_db_manager== null) {
            tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(mContext);
            try {
                tmcMenuItemSQL_db_manager.open();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try{
            Cursor cursor = tmcMenuItemSQL_db_manager.Fetch();
            MenuItem.clear();
            try {
                // if (cursor.moveToFirst()) {

                Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {
                            Modal_MenuItem modal_newOrderItems = new Modal_MenuItem();
                            modal_newOrderItems.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_newOrderItems.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_newOrderItems.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.localDB_id)));
                            modal_newOrderItems.setApplieddiscountpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.applieddiscountpercentage)));
                            modal_newOrderItems.setBarcode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode)));
                            modal_newOrderItems.setCheckoutimageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.checkoutimageurl)));
                            modal_newOrderItems.setDisplayno(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.displayno)));
                            modal_newOrderItems.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_newOrderItems.setGstpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.gstpercentage)));
                            modal_newOrderItems.setItemavailability(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability)));
                            modal_newOrderItems.setItemuniquecode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemuniquecode)));
                            modal_newOrderItems.setNetweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.netweight)));
                            modal_newOrderItems.setPortionsize(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.portionsize)));
                            modal_newOrderItems.setPricetypeforpos(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.pricetypeforpos)));
                            modal_newOrderItems.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcctgykey)));
                            modal_newOrderItems.setTmcpriceperkg(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg)));
                            modal_newOrderItems.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_newOrderItems.setTmcsubctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcsubctgykey)));
                            modal_newOrderItems.setVendorkey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey)));
                            modal_newOrderItems.setVendorname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorname)));
                            modal_newOrderItems.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                            modal_newOrderItems.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                            modal_newOrderItems.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                            modal_newOrderItems.setSwiggyprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.swiggyprice)));
                            modal_newOrderItems.setDunzoprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.dunzoprice)));
                            modal_newOrderItems.setBigbasketprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.bigbasketprice)));
                            modal_newOrderItems.setWholesaleprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.wholesaleprice)));
                            modal_newOrderItems.setAppmarkuppercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)));
                            modal_newOrderItems.setTmcpriceperkgWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkgWithMarkupValue)));
                            modal_newOrderItems.setTmcpriceWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceWithMarkupValue)));
                            modal_newOrderItems.setKey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                            modal_newOrderItems.setInventorydetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.inventoryDetails)));


                            if (!isinventorycheck) {

                                String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                        receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                                modal_newOrderItems.setBarcode_AvlDetails(barcode_AvlDetails);
                                modal_newOrderItems.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                modal_newOrderItems.setKey_AvlDetails(key_AvlDetails);
                                modal_newOrderItems.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                modal_newOrderItems.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                modal_newOrderItems.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                modal_newOrderItems.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                modal_newOrderItems.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                modal_newOrderItems.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                modal_newOrderItems.setAllownegativestock(allownegativestock_AvlDetails);


                                Modal_MenuItemStockAvlDetails modal_menuItemStockAvlDetails = new Modal_MenuItemStockAvlDetails();
                                modal_menuItemStockAvlDetails.setBarcode(barcode_AvlDetails);
                                modal_menuItemStockAvlDetails.setItemavailability(itemavailability_AvlDetails);
                                modal_menuItemStockAvlDetails.setKey(key_AvlDetails);
                                modal_menuItemStockAvlDetails.setMenuitemkey(menuitemkey_AvlDetails);
                                modal_menuItemStockAvlDetails.setStockincomingkey(stockincomingkey_AvlDetails);
                                modal_menuItemStockAvlDetails.setVendorkey(vendorkey_AvlDetails);
                                modal_menuItemStockAvlDetails.setStockbalance(stockbalance_AvlDetails);
                                modal_menuItemStockAvlDetails.setAllownegativestock(allownegativestock_AvlDetails);
                                modal_menuItemStockAvlDetails.setLastupdatedtime(lastupdatedtime_AvlDetails);
                                modal_menuItemStockAvlDetails.setReceivedstock(receivedstock_AvlDetails);
                            //    MenuItemStockAvlDetails.add(modal_menuItemStockAvlDetails);




                            }
                            else{


                                if(String.valueOf(modal_newOrderItems.getBarcode().toString()).equals("999001100")){
                                    Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
                                }

                                modal_newOrderItems.setBarcode_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode_AvlDetails)));
                                modal_newOrderItems.setItemavailability_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability_AvlDetails)));
                                modal_newOrderItems.setKey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.key_AvlDetails)));
                                modal_newOrderItems.setLastupdatedtime_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.lastupdatedtime_AvlDetails)));
                                modal_newOrderItems.setMenuitemkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId_AvlDetails)));
                                modal_newOrderItems.setReceivedstock_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.receivedStock_AvlDetails)));
                                modal_newOrderItems.setStockbalance_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockBalance_AvlDetails)));
                                modal_newOrderItems.setStockincomingkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockIncomingKey_AvlDetails)));
                                modal_newOrderItems.setVendorkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey_AvlDetails)));
                                modal_newOrderItems.setAllownegativestock(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.allowNegativeStock_AvlDetails)));


                            }

                            MenuItem.add(modal_newOrderItems);
                        }
                        while (cursor.moveToNext());


                    }



                }
                else{
                    Toast.makeText(mContext, "There is no menuItem Please Refresh the App", Toast.LENGTH_SHORT).show();

                }




                //  }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                if(tmcMenuItemSQL_db_manager != null){
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = mContext.getApplicationContext().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(mContext.getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem>>() {
            }.getType();
            MenuItem  = gson.fromJson(json, type);
        }

    }


    private void saveCurrentStatusInSharedPref(String orderStatus) {
        SharedPreferences sharedPreferences
                = requireContext() .getSharedPreferences("CurrentSelectedStatus",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();


        myEdit.putString(
                "currentstatus",
                orderStatus);
        myEdit.apply();


    }





    @Override
    public void onResume() {
        super.onResume();
      //  setDataForSpinner();

        SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", ""));

        SharedPreferences shared2 = requireContext().getSharedPreferences("CurrentSelectedStatus", MODE_PRIVATE);
        orderStatus = (shared2.getString("currentstatus", ""));
        TodaysDate=getDate();
        if(orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)){
            selecting_The_Order_Status(confirmed_Order_widget,new_Order_widget,ready_Order_widget,transist_Order_widget,delivered_Order_widget);

        }
        if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)){
            selecting_The_Order_Status(ready_Order_widget,new_Order_widget, confirmed_Order_widget,transist_Order_widget,delivered_Order_widget);

        }
        if(orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)){
            selecting_The_Order_Status(transist_Order_widget,new_Order_widget, confirmed_Order_widget,ready_Order_widget,delivered_Order_widget);

        }
        if(orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)){
            selecting_The_Order_Status(delivered_Order_widget,new_Order_widget, confirmed_Order_widget,ready_Order_widget,transist_Order_widget);

        }
       // showProgressBar();


        //ordersList.clear();
      //  sorted_OrdersList.clear();
      //  TodaysDate=getDate();
      //  getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);

    }

    private void setDataForSpinner() {
        slotnameChoosingSpinnerData.clear();

        String TodaysDate = getDate();
        TodaysDate = "Preorder - Today's Date  :       "+TodaysDate;
        String TomorrowsDate = getTomorrowsDate();
        TomorrowsDate = "Preorder - Tomorrow's Date  :       "+TomorrowsDate;

        slotnameChoosingSpinnerData.add("Preorder and Express Delivery");
        slotnameChoosingSpinnerData.add(TodaysDate);
        slotnameChoosingSpinnerData.add(TomorrowsDate);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, slotnameChoosingSpinnerData);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slotType_Spinner.setAdapter(arrayAdapter);

    }


    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey) {
        if(isVendorOrdersTableServiceCalled){
            showProgressBar(false);
            return;

        }
        isVendorOrdersTableServiceCalled = true;
        if(isnewOrdersSyncButtonClicked){
            showProgressBar(false);

            return;
        }

        isnewOrdersSyncButtonClicked=true;
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
                isVendorOrdersTableServiceCalled = false;
                mobile_jsonString = String.valueOf(jsonObject);
                ordersList = orderslist_fromResponse;
                displayorderDetailsinListview(orderStatus,orderslist_fromResponse, selected_OrderType);

                //convertingJsonStringintoArray(orderStatus,mobile_jsonString);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                isVendorOrdersTableServiceCalled = false;
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                showProgressBar(false);
            }
        };
        ordersList.clear();
        sorted_OrdersList.clear();
        mVolleyService = new VendorOrdersTableService(mResultCallback,mContext);
      //  String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_type + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=APPORDER";
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_AppOrder_PhoneOrder + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype1=APPORDER"+"&ordertype2=PHONEORDER";

        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;

        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }


    private void getOrderDetailsUsingOrderSlotDate(String previousDaydate, String SlotDate, String vendorKey, String selectedStatus) {

        if(isnewOrdersSyncButtonClicked){
            return;
        }

        isnewOrdersSyncButtonClicked=true;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        ordersList.clear();
        sorted_OrdersList.clear();



        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_AppOrders + "?slotdate="+SlotDate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDaydate,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        jsonString=response.toString();
                        convertingJsonStringintoArray(selectedStatus,jsonString);
                          // adapter = new Adapter_AutoCompleteManageOrdersItem(mContext, jsonString);


                       //     mobile_search_barEditText.setAdapter(adapter);

                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                showOrderInstructionText(true);
                isnewOrdersSyncButtonClicked=false;
                displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);

                Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getCause());

                error.printStackTrace();
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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);




    }
 /*   private void getOrderDetailsUsingApi(String date, String vendorKey, String selectedStatus) {
       if(isnewOrdersSyncButtonClicked){
           return;
       }


        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        ordersList.clear();
        sorted_OrdersList.clear();



        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



     //   JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey + "?orderplaceddate="+date+"&vendorkey="+vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        jsonString=response.toString();
                        convertingJsonStringintoArray(selectedStatus,jsonString);

                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


                Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                showOrderInstructionText(true);
                isnewOrdersSyncButtonClicked=false;

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

    }
*/

    private void initiateSocketConnection() {
        //Log.i("SocketConnection","t   ");
        runOnUiThread(() -> {

            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder().url(SERVER_PATH).addHeader("VendorKey",vendorKey).build();
            //Log.i("SocketConnection","  "+request.toString());
           webSocket = client.newWebSocket(request, new SocketListener());

        });

    }


    public void printReciptUsingUSBPrinter(List<Modal_ManageOrders_Pojo_Class> selectedBillDetails) {
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
        try {
        UsbConnection usbConnection = UsbPrintersConnectionsLocal.selectFirstConnected(mContext);
        UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        try {
            if (usbConnection == null || usbManager == null) {


                isUSBPrintReciptMethodCalled = false;
                /*
                new AlertDialog.Builder(mContext)
                        .setTitle("USB Connection")
                        .setMessage("No USB printer found.")
                        .show();

                 */


                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.ReConnect_Instruction,
                        R.string.OK_Text, R.string.Cancel_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                printReciptUsingUSBPrinter(selectedBillDetails);
                            }

                            @Override
                            public void onNo() {

                            }
                        });

                return;
            }
        }
        catch (Exception e){

            e.printStackTrace();
        }
        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                mContext,
                0,
                new Intent(ACTION_USB_PERMISSION),
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        mContext.registerReceiver(usbReceiver, filter);
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
                synchronized (context) {
                    UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context, new AsyncEscPosPrint.OnPrintFinished() {
                                @Override
                                public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                    isUSBPrintReciptMethodCalled = false;
                                    showProgressBar(false);
                                    try{
                                        mContext.unregisterReceiver(usbReceiver);
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

                                    Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                }

                                @Override
                                public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                    isUSBPrintReciptMethodCalled = false;
                                    showProgressBar(false);
                                    try{
                                        mContext.unregisterReceiver(usbReceiver);
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
                                    Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");

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

        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss",Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

        for (int i = 0; i < itemdesp.length(); i++) {
            String text_to_Print_miniBill = "";

            try {
                JSONObject json = itemdesp.getJSONObject(i);
                text_to_Print_miniBill = "";
                text_to_Print_miniBill = "[c]<b><font size='big'> Token No : "+TokenNo+"</b>\n\n";
                text_to_Print_miniBill = text_to_Print_miniBill+"[L]<b><font size='normal'>  Orderid "+Orderid +" </b>\n\n";


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

                    }
                    else {

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

                    text_to_Print_miniBill = text_to_Print_miniBill+"[L]  <b><font size='wide'>Grill House  "+fullitemName+"</b>\n"+ "\n";

                }
                else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                    text_to_Print_miniBill = text_to_Print_miniBill+"[L]  <b><font size='wide'>Ready to Cook  "+fullitemName+"</b>\n"+ "\n";

                }
                else  {

                    text_to_Print_miniBill = text_to_Print_miniBill+"[L]  <b><font size='wide'>"+fullitemName+"</b>\n"+"\n";

                }

                String finalitemname = "", finalCutName="",finalitemNetweight = "", finalgrossweight = "",finalQuantity ="";


                try {
                    if(json.has("cutname")){
                        finalCutName = json.getString("cutname");

                    }
                    else{
                        finalCutName ="";
                    }
                    //Log.i("tag","grossweight Log    "+                grossweight);
                }
                catch (Exception e){
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

                    text_to_Print_miniBill = text_to_Print_miniBill+"[L]<font size='normal'> ---------------------------------------------- \n";

                    text_to_Print_miniBill = text_to_Print_miniBill+"[L]  <font size='normal'>"+finalCutName.toUpperCase() +" \n";

                    text_to_Print_miniBill = text_to_Print_miniBill+"[L]<font size='normal'> ---------------------------------------------- \n";


                }
                text_to_Print_miniBill = text_to_Print_miniBill+"[L] <font size='normal'>Grossweight : "+finalgrossweight +" \n";
                text_to_Print_miniBill = text_to_Print_miniBill+"[L]  <font size='normal'>Netweight : "+finalitemNetweight +" \n";
                text_to_Print_miniBill = text_to_Print_miniBill+"[L]  <font size='normal'>Quantity : "+ finalQuantity +" \n";


                printer.addTextToPrint(text_to_Print_miniBill);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        String text_to_Print = "";


        String GSTIN = "GSTIN :33AAJCC0055D1Z9";


        if((vendorKey.equals("vendor_4")) ||  (vendorKey.equals("wholesalesvendor_1"))) {


            text_to_Print = "[c]<b><font size='big'>MK Proteins</b>\n\n";
            text_to_Print = text_to_Print + "[c]<b><font size='normal'>Powered By The Meat Chop</b>\n\n";

        }
        else if((vendorKey.equals("vendor_6"))) {


            text_to_Print = "[c]<b><font size='big'>New NS Bismillah</b>\n\n";
            text_to_Print = text_to_Print + "[c]<b><font size='normal'>Powered By The Meat Chop</b>\n\n";

        }
        else {
            text_to_Print = "[c]<b><font size='big'>The Meat Chop</b>\n\n";

        }


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



                itemwise_Subtotal = String.valueOf(itemwise_Subtotal_double);
                itemwise_price = String.valueOf(itemwise_Subtotal_double);

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


                text_to_Print = text_to_Print+"[L]  <font size='normal'>"+itemwise_price + "[R] "+itemwise_Subtotal +" \n";

                text_to_Print = text_to_Print+"[L]  <font size='normal'>                                                "+" \n";


            }
            text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";

        } catch (JSONException e) {
            e.printStackTrace();
        }



        PayableAmount = "Rs." + String.valueOf(totalAmountFromAddingSubtotal);
       /*
        if (PayableAmount.length() == 4) {
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



        text_to_Print = text_to_Print+"[L]  " +PayableAmount+" [R] "+PayableAmount+" \n";

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
            DeliveryAmount = "Rs."+DeliveryAmount+".00";

           /* if (DeliveryAmount.length() == 4) {
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

        if ((!CouponDiscount.equals("0.0")) && (!CouponDiscount.equals("0")) && (!CouponDiscount.equals("0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals(""))) {
            //couponDiscount_double = Double.parseDouble (CouponDiscount);
            if(!CouponDiscount.contains("Rs.")){
                CouponDiscount = "Rs. "+CouponDiscount;

            }
            if(!CouponDiscount.contains(".00")){
                CouponDiscount = CouponDiscount+".00";

            }
            if (OrderType.equals(Constants.APPORDER) ) {
             /*   if (CouponDiscount.length() == 4) {
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
                    CouponDiscount = "Coupon Discount                  " + CouponDiscount;
                }
                if (CouponDiscount.length() == 12) {
                    //14spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Coupon Discount                 " + CouponDiscount;
                }

                if (CouponDiscount.length() == 13) {
                    //13spaces
                    //NEW TOTAL =9
                    CouponDiscount = "Coupon Discount               " + CouponDiscount;
                }
                */

                text_to_Print = text_to_Print+"[L]  Coupon Discount "+"[R]      " +CouponDiscount+" \n";


            }

            if ((OrderType.equals(Constants.POSORDER))|| (OrderType.equals(Constants.PhoneOrder))) {
               // couponDiscount_double = Double.parseDouble (CouponDiscount);
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


            //    text_to_Print = text_to_Print+"[L]" +CouponDiscount+" \n";

            text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";





        }

        String NetTotal = "Rs." + String.valueOf(totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt);
       /* if (NetTotal.length() == 4) {
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


        //text_to_Print = text_to_Print+"[L]" +NetTotal+" \n";

        text_to_Print = text_to_Print+"[L]  ----------------------------------------------" +" \n";


        text_to_Print = text_to_Print+"[L]  <b>Order Type : " +OrderType+" </b>\n";

        text_to_Print = text_to_Print+"[L]  <b>Payment Mode : " +PaymentMode+" </b>\n";

        text_to_Print = text_to_Print+"[L]  Mobile No : " +MobileNumber+" \n"+" \n";

        text_to_Print = text_to_Print+"[c]  <b><font size='big'> TOKEN NO: "+TokenNo+"</b>\n";

        text_to_Print = text_to_Print+"[L]  <font size='normal'>                                                "+" \n";


        text_to_Print = text_to_Print+"[L]  <font size='normal'>Slot Name : " +Slotname+"\n";


        text_to_Print = text_to_Print+"[L]  <font size='normal'>Slot Date : " +SlotDate+" \n";



        if(Slotname.equals(Constants.EXPRESSDELIVERY_SLOTNAME)){
            text_to_Print = text_to_Print+"[L]  Order Placed time : " +OrderPlacedtime+" \n";


        }
        text_to_Print = text_to_Print+"[L]  Delivery time : " +DeliveryTime+" \n";

        text_to_Print = text_to_Print+"[L]  <b>Delivery type : " +DeliveryType+"</b> \n";
        text_to_Print = text_to_Print+"[L]  Distance from Store  : " +DistanceFromStore+" Kms"+" \n";

        text_to_Print = text_to_Print+"[L]  Address : " +" \n";
        text_to_Print = text_to_Print+"[L]   "+ Address +" \n";



        if(!Notes.equals("")) {
            text_to_Print = text_to_Print+"[c]  <b><font size='big'>Notes : " +Notes+" </b>\n\n";


        }
        else{
            text_to_Print = text_to_Print+"[L] " +" \n";


        }
        text_to_Print = text_to_Print+"[L] " +" \n";

        text_to_Print = text_to_Print+"[c]   <b>Thank You For Choosing Us !!!! " +"</b> \n";

        text_to_Print = text_to_Print+"[L] " +" \n";
        text_to_Print = text_to_Print+"[L] " +" \n";


        //AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);
        return printer.addTextToPrint(text_to_Print);






    }







    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            runOnUiThread(() -> {
                webSocket.send("12/26/2020,vendor_1");

                Toast.makeText(mContext,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();
            //   initializeView();
            });


        }



        private String getDatewithNameoftheDay() {
            Date c = Calendar.getInstance().getTime();

            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDay = day.format(c);

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            CurrentDate = df.format(c);

            CurrentDate = CurrentDay+", "+CurrentDate;
            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;
        }


        @SuppressLint("LongLogTag")
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            //Log.i("result","t   "+text);
            Boolean statusChangedLocally = false;
            // websocket_OrdersList.clear();
            //orderinstruction.setVisibility(View.INVISIBLE);

            //Log.i("result","t   "+text);
            JSONArray array = null;
            try {
                array = new JSONArray(text);
                //Log.i(" array.length()", String.valueOf(array.length()));

                for(int i=0; i < array.length(); i++)
                {
                    JSONObject json = array.getJSONObject(i);
                    String orderidfromsocketarray =String.valueOf(json.get("orderid"));
                    String orderstatusfromsocketarray =String.valueOf(json.get("orderstatus"));
                    //Log.i("orderid from socketresponse", orderidfromsocketarray);
                    //Log.i("status from socketresponse", orderstatusfromsocketarray);
                    for(int j=0;j<ordersList.size();j++)
                    {
                        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(j);
                        String orderidfromlocal =modal_manageOrders_pojo_class.getOrderid();
                        //Log.i("orderid from local array", orderidfromlocal);

                        if(orderidfromlocal.equals(orderidfromsocketarray)){
                            statusChangedLocally =true;
                            //Log.i(" in if", String.valueOf(statusChangedLocally));

                            modal_manageOrders_pojo_class.setOrderstatus(orderstatusfromsocketarray);
                            ordersList.set(j,modal_manageOrders_pojo_class);
                            displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);
                        }


                    }
                    if(!statusChangedLocally){
                        //Log.i(" in if !! ", String.valueOf(statusChangedLocally));

                        //getorderDetailsDataFromDynamoDB(orderidfromsocketarray,orderstatusfromsocketarray);
                    }
                }




            } catch (Exception ex) {
                StringWriter stringWriter = new StringWriter();
                ex.printStackTrace(new PrintWriter(stringWriter));
                //Log.e("exception ::: ", stringWriter.toString());
            }




        }

    }


    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        nameofFacility_Textview.setVisibility(View.VISIBLE);
        search_button.setVisibility(View.VISIBLE);
        search_close_btn.setVisibility(View.GONE);
        search_barEditText.setText("");

        isSearchButtonClicked =false;

        search_barEditText.setVisibility(View.GONE);
        displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);

    }

    private void showSearchBarEditText() {
        nameofFacility_Textview.setVisibility(View.GONE);
        search_button.setVisibility(View.GONE);
        search_close_btn.setVisibility(View.VISIBLE);
        search_barEditText.setVisibility(View.VISIBLE);
    }
    private void showKeyboard(final EditText editText) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //receive your data here
            //Log.v(Constants.TAG, "BroadCastSucess");
            sorted_OrdersList.clear();
            ordersList.clear();
             jsonString = intent.getStringExtra("response");
            convertingJsonStringintoArray(orderStatus,jsonString);
            
            //Log.v(Constants.TAG, "BroadCastSucess"+jsonString);
        }
    };

    private void saveorderDetailsInLocal(List<Modal_ManageOrders_Pojo_Class> ordersList) {
        Gson gson = new Gson();
        String json = gson.toJson(ordersList);

            SharedPreferences sharedPreferences
                    = mContext.getSharedPreferences("OrderDetailsFromSharedPreferences",
                    MODE_PRIVATE);

            SharedPreferences.Editor myEdit
                    = sharedPreferences.edit();


            myEdit.putString(
                    "orderDetails",
                    json);


            myEdit.apply();


    }


    public void printReciptUsingBluetoothPrinter(List<Modal_ManageOrders_Pojo_Class> selectedBillDetails) {
        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = selectedBillDetails.get(0);

        if (BluetoothPrintDriver.IsNoConnection()) {
            //  Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

            new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Do_You_Want_to_Connect_Printer_Now,
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
                Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Bluetooth_turnedOff_Information,
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

        String GSTIN = "GSTIN :33AAJCC0055D1Z9";
        String CurrentTime =getDate_and_time();

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
        double totalAmountFromAddingSubtotal=0;
        double couponDiscount_double=0;
        double deliveryAmount_double=0;
        double totalAmountFromAddingSubtotalWithDiscount =0;
        double totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt =0;

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
            DeliveryAmount= modal_manageOrders_pojo_class.getDeliveryamount();
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
                            Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
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
                                if (fullitemName.length() < 21) {
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
                            BluetoothPrintDriver.printString("Grill House  "+fullitemName);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                        }
                        else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                            BluetoothPrintDriver.SetLineSpacing((byte) 100);
                            BluetoothPrintDriver.printString("Ready to Cook  "+fullitemName);
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
                            finalQuantity = String.valueOf(json.get("quantity"));
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
                        Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();
                    }

                    BluetoothPrintDriver.LF();

                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("Order Id : "+ Orderid);
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();




                    String finalitemname = "", finalCutName="",finalitemNetweight = "", finalgrossweight = "",finalQuantity ="";


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
                            if (fullitemName.length() < 21) {
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
                        BluetoothPrintDriver.printString("Grill House  "+fullitemName);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
                    }
                    else if(tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                        BluetoothPrintDriver.SetLineSpacing((byte) 100);
                        BluetoothPrintDriver.printString("Ready to Cook  "+fullitemName);
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
                            finalCutName ="";
                        }
                        //Log.i("tag","grossweight Log    "+                grossweight);
                    }
                    catch (Exception e){
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
                    BluetoothPrintDriver.printString("Grossweight : " + finalgrossweight);
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
            BluetoothPrintDriver.printString("ItemName ");
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

                            itemDespName_Weight_quantity = String.valueOf("Grill House  "+fullitemName) ;
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

                            itemDespName_Weight_quantity = String.valueOf("Ready to Cook  "+fullitemName);
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
                        else  {
                            itemDespName_Weight_quantity = String.valueOf(fullitemName) ;
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
                            itemwise_price = itemwise_price + "                         ";
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


            PayableAmount = "Rs." + String.valueOf(totalAmountFromAddingSubtotal);
            if (PayableAmount.length() == 4) {
                //21spaces
                PayableAmount = PayableAmount + "                                     " + PayableAmount;
            }
            if (PayableAmount.length() == 5) {
                //20spaces
                PayableAmount = PayableAmount + "                                  " + PayableAmount;
            }
            if (PayableAmount.length() == 6) {
                //19spaces
                PayableAmount = PayableAmount + "                                 " + PayableAmount;
            }
            if (PayableAmount.length() == 7) {
                //18spaces
                PayableAmount = PayableAmount + "                                " + PayableAmount;
            }
            if (PayableAmount.length() == 8) {
                //17spaces
                PayableAmount = PayableAmount + "                               " + PayableAmount;
            }
            if (PayableAmount.length() == 9) {
                //16spaces
                PayableAmount = PayableAmount + "                              " + PayableAmount;
            }
            if (PayableAmount.length() == 10) {
                //15spaces
                PayableAmount = PayableAmount + "                             " + PayableAmount;
            }
            if (PayableAmount.length() == 11) {
                //14spaces
                PayableAmount = PayableAmount + "                            " + PayableAmount;
            }
            if (PayableAmount.length() == 12) {
                //13spaces
                PayableAmount = PayableAmount + "                           " + PayableAmount;
            }
            if (PayableAmount.length() == 13) {
                //12spaces
                PayableAmount = PayableAmount + "                          " + PayableAmount;
            }
            if (PayableAmount.length() == 14) {
                //11spaces
                PayableAmount = PayableAmount + "                         " + PayableAmount;
            }
            if (PayableAmount.length() == 15) {
                //10spaces
                PayableAmount = PayableAmount + "                        " + PayableAmount;
            }
            if (PayableAmount.length() == 16) {
                //9spaces
                PayableAmount = PayableAmount + "                       " + PayableAmount;
            }
            if (PayableAmount.length() == 17) {
                //8spaces
                PayableAmount = PayableAmount + "                       " + PayableAmount;
            }
            if (PayableAmount.length() == 18) {
                //7spaces
                PayableAmount = PayableAmount + "                     " + PayableAmount;
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
                couponDiscount_double = Double.parseDouble (CouponDiscount);
                if (OrderType.equals(Constants.APPORDER) || OrderType.equals(Constants.PhoneOrder)) {
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

                if (OrderType.equals(Constants.POSORDER)) {
                    couponDiscount_double = Double.parseDouble (CouponDiscount);

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
                totalAmountFromAddingSubtotalWithDiscountanddeliveryAmnt = totalAmountFromAddingSubtotalWithDiscount+deliveryAmount_double;

            }
            catch(Exception e){
                e.printStackTrace();
            }

            if( deliveryAmount_double>0) {
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
                NetTotal = "NET TOTAL                              " + NetTotal;
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
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.SetLineSpacing((byte) 110);
            BluetoothPrintDriver.SetBold((byte) 0x08);//´ÖÌå
            BluetoothPrintDriver.SetCharacterFont((byte)0);
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

            e.printStackTrace();
        }





    }




    private void ConnectPrinter() {


        try{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                Toast.makeText(mContext, "Bluetooth is not Supported", Toast.LENGTH_LONG).show();
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
                serverIntent = new Intent(mContext, DeviceListActivity.class);
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
        mChatService = new BluetoothPrintDriver(mContext, mHandler);
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
                            // printerConnectionStatus_Textwidget.setText(R.string.title_connected_to);
                            //   printerConnectionStatus_Textwidget.append(mConnectedDeviceName);
                            isPrinterCnnected =true;
                            printerStatus = "Connected";
                            printerName = mConnectedDeviceName;
                            // setTitle(R.string.title_connected_to);
                            //setTitle(mConnectedDeviceName);
                            SaveDatainSharedPreferences(isPrinterCnnected,printerName,printerStatus);

                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:
                            //printerConnectionStatus_Textwidget.setText(R.string.title_connecting);
                            // setTitle(R.string.title_connecting);
                            isPrinterCnnected =false;
                            printerStatus = "Connecting";
                            printerName = mConnectedDeviceName;
                            SaveDatainSharedPreferences(isPrinterCnnected,printerName,printerStatus);

                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                            // printerConnectionStatus_Textwidget.setText("state listen");

                        case BluetoothPrintDriver.STATE_NONE:
                            //  printerConnectionStatus_Textwidget.setText(R.string.title_not_connected);
                            ///setTitle(R.string.title_not_connected);
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
                    Toast.makeText(mContext, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(mContext, msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
            //     printerConnectionStatus_Textwidget.setText(String.valueOf(mBluetoothAdapter.getState()));
        }
    };



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    serverIntent = new Intent(mContext, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d("TAG", "BT not enabled");
                    Toast.makeText(mContext, "bt_not_enabled_leaving", Toast.LENGTH_SHORT).show();
                    //  finish();
                }
        }
    }




    private void convertingJsonStringintoArray(String orderStatus, String jsonString) {
        try {
            ordersList.clear();
            sorted_OrdersList.clear();
            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            String ordertype="#";
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
                    if(ordertype.equals(Constants.APPORDER) || ordertype.equals(Constants.PhoneOrder))  {


                        if (json.has("orderid")) {
                            manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                        } else {
                            manageOrdersPojoClass.orderid = "";
                        }

                        if (json.has("deliveryamount")) {
                            manageOrdersPojoClass.deliveryamount = String.valueOf(json.get("deliveryamount"));

                        } else {
                            manageOrdersPojoClass.deliveryamount = "";
                        }




                        if (json.has("orderplacedtime")) {
                            manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));

                        } else {
                            manageOrdersPojoClass.orderplacedtime = "";
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

                        if (json.has("orderStatus")) {
                            manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderStatus"));

                        } else {
                            manageOrdersPojoClass.orderstatus = "";
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

                        } else {
                            manageOrdersPojoClass.orderdeliveredtime = "";
                        }
                        if (json.has("useraddresskey")) {
                            manageOrdersPojoClass.useraddresskey = String.valueOf(json.get("useraddresskey"));

                        } else {
                            manageOrdersPojoClass.useraddresskey = "";
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

                        } else {
                            manageOrdersPojoClass.deliverytype = "";
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


                        if (json.has("slotdate")) {
                            manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));

                        } else {
                            manageOrdersPojoClass.slotdate = "";
                        }


                        if (json.has("slotname")) {
                            manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                        } else {
                            manageOrdersPojoClass.slotname = "";
                        }

                        /*if (json.has("slottimerange")) {
                            manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                        } else {
                            manageOrdersPojoClass.slottimerange = "";
                        }

                         */

                        if (json.has("notes")) {
                            manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                        } else {
                            manageOrdersPojoClass.notes = "";
                        }


                        if (json.has("keyfromtrackingDetails")) {
                            manageOrdersPojoClass.keyfromtrackingDetails = String.valueOf(json.get("keyfromtrackingDetails"));

                        } else {
                            manageOrdersPojoClass.keyfromtrackingDetails = "";
                        }

                        try {
                            if (ordertype.toUpperCase().equals(Constants.APPORDER) || ordertype.toUpperCase().equals(Constants.PhoneOrder)) {
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
                        try {
                            if (ordertype.toUpperCase().equals(Constants.APPORDER) || ordertype.toUpperCase().equals(Constants.PhoneOrder) ) {


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
                        ordersList.add(manageOrdersPojoClass);

                        //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isnewOrdersSyncButtonClicked=false;

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());



            }


            }

                //Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);

                //saveorderDetailsInLocal(ordersList);
                displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);


        } catch (JSONException e) {
            e.printStackTrace();
            isnewOrdersSyncButtonClicked=false;

        }
    }


    private void SaveDatainSharedPreferences(boolean isPrinterCnnected, String printerName, String printerStatus) {
        SharedPreferences sharedPreferences
                = getActivity().getSharedPreferences("PrinterConnectionData",
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



    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        CurrentDate = df.format(c);



        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDate+" "+FormattedTime;
        return formattedDate;
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

            CurrentDay = day.format(c);

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;


            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;

        }
    }


    public void displayorderDetailsinListview(String orderStatus, List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner) {
        //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());

        if(ordersList.size()>0) {
            sorted_OrdersList.clear();
            String TodaysDate = getDate();
            String TomorrowsDate = getTomorrowsDate();

            if (orderdetailsnewschema) {
                TodaysDate = convertnewFormatDateintoOldFormat(TodaysDate);
                TomorrowsDate = convertnewFormatDateintoOldFormat(TomorrowsDate);

            }
            //Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

            //Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);

            if (slottypefromSpinner == 0) {
                for (int i = 0; i < ordersList.size(); i++) {
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

                    if ((orderStatus.equals(orderstatusfromOrderList)) && (slotDate.equals(TodaysDate))) {
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
                        modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();
                        modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                        modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                        modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                        modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();
                        modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                        modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();
                        modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus();

                        modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                        modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                        modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                        modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                        modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                   /* if((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210"))&&(!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {
                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                    }

                    */
                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                    }

                }
            } else if (slottypefromSpinner == 1) {
                for (int i = 0; i < ordersList.size(); i++) {
                    //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                    String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                    String slotDate = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotdate());
                    String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();


                    //Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);

                    //Log.d(Constants.TAG, "displayorderDetailsinListview slotDate: " + slotDate);

                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);

                    if ((orderStatus.equals(orderstatusfromOrderList)) && (slotDate.equals(TomorrowsDate))) {
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
                        modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();
                        modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                        modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();
                        modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus();

                        modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                        modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                        modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                        modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();
                        modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                        modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                        modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();

                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                    }

                }
            } else {
                for (int i = 0; i < ordersList.size(); i++) {
                    //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                    String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();

                    String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);

                    if ((orderStatus.equals(orderstatusfromOrderList))) {
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
                        modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();
                        modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                        modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                        modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                        modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();
                        modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                        modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();

                        modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus();

                        modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                    }

                }
            }
                calculate_and_displayno_of_orders_statuswise();


            if (sorted_OrdersList.size() > 0) {
                if (orderStatus.equals(Constants.NEW_ORDER_STATUS)) {
                    Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                            return object2.getOrderplacedtime().compareTo(object1.getOrderplacedtime());
                        }
                    });
                }


                if (orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                    Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                            return object2.getOrderplacedtime().compareTo(object1.getOrderplacedtime());
                        }
                    });
                }


                if (orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                    Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                            return object2.getOrderreadytime().compareTo(object1.getOrderreadytime());
                        }
                    });
                }


                if (orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)) {
                    Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                            return object2.getOrderpickeduptime().compareTo(object1.getOrderpickeduptime());
                        }
                    });
                }


                if (orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)) {
                    Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                        public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                            return object2.getOrderdeliveredtime().compareTo(object1.getOrderdeliveredtime());
                        }
                    });
                }
            /*
            if (orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)) {
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
            }



             */


                isnewOrdersSyncButtonClicked = false;

                manageOrdersListViewAdapter = new Adapter_Pos_ManageOrders_ListView(mContext, sorted_OrdersList, Pos_ManageOrderFragment.this, orderStatus,MenuItem);
                manageOrders_ListView.setAdapter(manageOrdersListViewAdapter);
                showProgressBar(false);


            } else {
                showOrderInstructionText(true);
                isnewOrdersSyncButtonClicked = false;

                // showProgressBar(false);
            }

        }
        else{


            new_Order_widget.setText(String.format("%s", Constants.NEW_ORDER_STATUS));
            confirmed_Order_widget.setText(String.format("%s", Constants.CONFIRMED_ORDER_STATUS));
            ready_Order_widget.setText(String.format("%s", Constants.READY_FOR_PICKUP_ORDER_STATUS));
            transist_Order_widget.setText(String.format("%s", Constants.PICKEDUP_ORDER_STATUS));
            delivered_Order_widget.setText(String.format("%s", Constants.DELIVERED_ORDER_STATUS));




            showProgressBar(false);
            isnewOrdersSyncButtonClicked = false;
            showOrderInstructionText(true);

        }

//callAdapter();
    }

    public void calculate_and_displayno_of_orders_statuswise() {
        int newCount=0,confirmedCount=0,readyForPickupCount=0,transitCount=0,deliveredCount=0;


        for (int i = 0; i < ordersList.size(); i++) {
            //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

            final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
            String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
            if (orderstatusfromOrderList.equals(Constants.NEW_ORDER_STATUS)) {
                newCount++;

                Log.i("Tag", "Count New : " + newCount);

            } else if (orderstatusfromOrderList.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                confirmedCount++;


                Log.i("Tag", "Count confirmed : " + confirmedCount);

            } else if (orderstatusfromOrderList.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                readyForPickupCount++;


                Log.i("Tag", "Count ready : " + readyForPickupCount);

            } else if (orderstatusfromOrderList.equals(Constants.PICKEDUP_ORDER_STATUS)) {
                transitCount++;


                Log.i("Tag", "Count transit : " + transitCount);

            } else if (orderstatusfromOrderList.equals(Constants.DELIVERED_ORDER_STATUS)) {
                deliveredCount++;


                Log.i("Tag", "Count delivered : " + deliveredCount);

            } else {
                Log.i("Tag", "Count Status not matched ");

            }
        }


        if (newCount > 0) {
            new_Order_widget.setText(String.format("%s ( %d )", Constants.NEW_ORDER_STATUS, newCount));
        } else {
            new_Order_widget.setText(String.format("%s", Constants.NEW_ORDER_STATUS));

        }


        if (confirmedCount > 0) {
            confirmed_Order_widget.setText(String.format("%s ( %d )", Constants.CONFIRMED_ORDER_STATUS, confirmedCount));
        } else {
            confirmed_Order_widget.setText(String.format("%s", Constants.CONFIRMED_ORDER_STATUS));

        }


        if (readyForPickupCount > 0) {
            ready_Order_widget.setText(String.format("%s ( %d )", Constants.READY_FOR_PICKUP_ORDER_STATUS, readyForPickupCount));
        } else {
            ready_Order_widget.setText(String.format("%s", Constants.READY_FOR_PICKUP_ORDER_STATUS));

        }


        if (transitCount > 0) {
            transist_Order_widget.setText(String.format("%s ( %d )", Constants.PICKEDUP_ORDER_STATUS, transitCount));
        } else {
            transist_Order_widget.setText(String.format("%s", Constants.PICKEDUP_ORDER_STATUS));

        }

        if (deliveredCount > 0) {
            delivered_Order_widget.setText(String.format("%s ( %d )", Constants.DELIVERED_ORDER_STATUS, deliveredCount));
        } else {
            delivered_Order_widget.setText(String.format("%s", Constants.DELIVERED_ORDER_STATUS));

        }

    }

    void showOrderInstructionText(boolean show) {
        if(show){
            if(orderStatus.equals(Constants.NEW_ORDER_STATUS)){
                orderinstruction.setText("No New Orders");


            }
            if(orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                orderinstruction.setText("No Confirmed Orders ");

            }
            if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                orderinstruction.setText("No  Order is Ready for Pickup");

            }
            if(orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)) {
                orderinstruction.setText("No Picked Up Orders");

            }
            if(orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)) {
                orderinstruction.setText("No Delivered Orders ");

            }


            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            orderinstruction.setVisibility(View.VISIBLE);



        }
        else{
            orderinstruction.setVisibility(View.GONE);

        }
    }


    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            manageOrders_ListView.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);
        }

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


    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    private void selecting_The_Order_Status(TextView selected_widget, TextView order_widget1, TextView order_widget2, TextView order_widget3, TextView order_widget4) {

        selected_widget.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_selected_button_background));
        selected_widget.setTextColor(Color.WHITE);


        order_widget1.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget1.setTextColor(Color.BLACK);

        order_widget2.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget2.setTextColor(Color.BLACK);

        order_widget3.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget3.setTextColor(Color.BLACK);

        order_widget4.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget4.setTextColor(Color.BLACK);

    }



    private String getTomorrowsDate() {

        Date todaysDate = Calendar.getInstance().getTime();
        System.out.println("nextDate " + todaysDate);

        String next_day = "";
        SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        dateFormatForDisplaying.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        if (orderdetailsnewschema) {

            dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        } else {
            dateFormatForDisplaying = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);

        }
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
            if (orderdetailsnewschema) {

                next_day = nex;

            } else {
                next_day = day_1+", "+nex;

            }
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

    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);
        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(c);
            return CurrentDate;

        }
        else {
            SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(c);

            System.out.println("Current  " + CurrentDate);


            return CurrentDate;
        }
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



}


/*   SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", "vendor_1"));
        ordersList.clear();
        sorted_OrdersList.clear();
        TodaysDate=getDate();
        Adusting_Widgets_Visibility();
        getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);
        TodaysDate=getDate();


      */
     /*   swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);
            }
        });

      */