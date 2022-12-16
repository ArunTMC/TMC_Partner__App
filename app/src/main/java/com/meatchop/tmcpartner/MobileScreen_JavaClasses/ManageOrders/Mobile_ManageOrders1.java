package com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Modal_MenuItem;
import com.meatchop.tmcpartner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.meatchop.tmcpartner.Settings.DeviceListActivity;
import com.meatchop.tmcpartner.Settings.Modal_MenuItem_Settings;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import okhttp3.WebSocket;

import static android.content.Context.MODE_PRIVATE;
import static androidx.appcompat.content.res.AppCompatResources.getDrawable;
import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mobile_ManageOrders1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mobile_ManageOrders1 extends Fragment {
    TextView mobile_orderinstruction, mobile_nameofFacility_Textview;
    Button mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;
    Adapter_AutoCompleteManageOrdersItem adapter;
    String DeliveryPersonList="",mobile_jsonString,orderStatus=Constants.NEW_ORDER_STATUS,vendorKey,vendorname,TAG = "Tag";
    ListView manageOrders_ListView;
    static LinearLayout loadingPanel;
    static LinearLayout loadingpanelmask;
    LinearLayout newOrdersSync_Layout;
    List<Modal_ManageOrders_Pojo_Class> websocket_OrdersList;
    List<Modal_MenuItem>MenuItem = new ArrayList<>();
    static List<Modal_ManageOrders_Pojo_Class> ordersList;
    Button todaysOrder,tomorrowsOrder;
    static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay,TodaysDate;
    List<String> slotnameChoosingSpinnerData;
    Spinner slotType_Spinner;
    int slottypefromSpinner=0;
    static Adapter_Mobile_ManageOrders_ListView1 adapterMobileManageOrdersListView;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    HorizontalScrollView horizontalScrollview;
    private String SERVER_PATH = "wss://hx9itd7ji2.execute-api.ap-south-1.amazonaws.com/Dev";
    WebSocket webSocket;
    private Context mContext;
    BottomNavigationView bottomNavigationView;


    boolean isSearchButtonClicked = false;
    boolean isnewOrdersSyncButtonClicked = false;

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
    String printerName = "";
    String printerStatus= "";

    boolean isPrinterCnnectedfromSP = false;
    String printerNamefromSP = "";
    String orderPlacedTime= "";
    int newCount=0,confirmedCount=0,readyForPickupCount=0,transitCount=0,deliveredCount=0;

    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;


    public Mobile_ManageOrders1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mobile_ManageOrders1.
     */
    // TODO: Rename and change types and number of parameters
    public static Mobile_ManageOrders1 newInstance(String param1, String param2) {
        Mobile_ManageOrders1 fragment = new Mobile_ManageOrders1();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();
      //  initiateSocketConnection();

    }


    private void initiateSocketConnection() {
        //Log.i("SocketConnection","t   ");


           // OkHttpClient client = new OkHttpClient();
          //  okhttp3.Request request = new okhttp3.Request.Builder().url(SERVER_PATH).addHeader("VendorKey",vendorKey).build();
            //Log.i("SocketConnection","  "+request.toString());
         //   webSocket = client.newWebSocket(request, new SocketListener());
            //Log.i("SocketConnection","  "+webSocket.queueSize());



    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getWindow().getContext();

        new NukeSSLCerts();
        NukeSSLCerts.nuke();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        deliveryPartnerList = new ArrayList<>();
        bottomNavigationView = ((MobileScreen_Dashboard) requireActivity()).findViewById(R.id.bottomnav);
        manageOrders_ListView = view.findViewById(R.id.manageOrders_ListView);
        mobile_orderinstruction =view.findViewById(R.id.orderinstruction);
        horizontalScrollview = view.findViewById(R.id.horizontalScrollview);
        loadingpanelmask = view.findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = view.findViewById(R.id.loadingPanel_dailyItemWisereport);
        todaysOrder = view.findViewById(R.id.todaysOrder);
        tomorrowsOrder = view.findViewById(R.id.tomorrowsOrder);

        try {
            SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", ""));
            vendorname = (shared.getString("VendorName", ""));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
            SharedPreferences shared2 = requireContext().getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
            DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));

            if(deliveryPartnerList.equals("")){
                getDeliveryPartnerList();

            }
            else{
                ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        websocket_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        slotnameChoosingSpinnerData= new ArrayList<>();




        slotType_Spinner=view.findViewById(R.id.slotType_Spinner);
        applaunchimage = view.findViewById(R.id.applaunchimage);


        //
        mobile_nameofFacility_Textview = view.findViewById(R.id.nameofFacility_Textview);
        mobile_search_button =view.findViewById(R.id.search_button);
        mobile_search_barEditText = view.findViewById(R.id.search_barEdit);
        mobile_search_close_btn = view.findViewById(R.id.search_close_btn);

        //
        mobile_new_Order_widget = view.findViewById(R.id.new_Order_widget);
        mobile_confirmed_Order_widget =view.findViewById(R.id.confirmed_Order_widget);
        mobile_ready_Order_widget = view.findViewById(R.id.ready_Order_widget);
        mobile_transist_Order_widget = view.findViewById(R.id.transist_Order_widget);
        mobile_delivered_Order_widget = view.findViewById(R.id.delivered_Order_widget);

        newOrdersSync_Layout = view.findViewById(R.id.newOrdersSync_Layout);
        slottypefromSpinner = 0;
        todaysOrder.setBackground(getDrawable(requireContext(),R.drawable.orange_selected_button_background));
        todaysOrder.setTextColor(Color.WHITE);

        tomorrowsOrder.setBackground(getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        tomorrowsOrder.setTextColor(Color.BLACK);

        String Todaysdate = getDatewithNameoftheDay();
        isSearchButtonClicked =false;
        String PreviousDaydate =  getDatewithNameofthePreviousDay();
        Adjusting_Widgets_Visibility(false);
        getMenuItemArrayFromSharedPreferences();
        if(orderdetailsnewschema){
            Adjusting_Widgets_Visibility(true);

                 callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);


        }
        else{
            Adjusting_Widgets_Visibility(true);

            getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

        }

//
    //    setDataForSpinner();
        mobile_nameofFacility_Textview.setText(vendorname);

        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Loading ..... Please wait",Toast.LENGTH_SHORT).show();
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
                                modal_manageOrders_forOrderDetailList1.vendorkey = modal_manageOrders_forOrderDetailList.getVendorkey();
                                modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                                modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                                modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                                modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();
                                modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus();


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
                            mobile_orderinstruction.setVisibility(View.GONE);

                            adapterMobileManageOrdersListView = new Adapter_Mobile_ManageOrders_ListView1(mContext, sorted_OrdersList, Mobile_ManageOrders1.this, orderstatus);
                            manageOrders_ListView.setAdapter(adapterMobileManageOrdersListView);
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


        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    pos_dashboard_screen.getCompleteMenuItem();



                    if (slottypefromSpinner == 0) {
                        Adjusting_Widgets_Visibility(true);
                        isSearchButtonClicked = false;
                        //Log.d(Constants.TAG, "SearchButtonClicked ");

                        String Todaysdate = getDatewithNameoftheDay();
                        String PreviousDaydate = getDatewithNameofthePreviousDay();

                        if (orderdetailsnewschema) {

                            callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);

                        } else {
                            getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

                        }



                    }
                    if (slottypefromSpinner == 1) {
                        isSearchButtonClicked = false;

                        Adjusting_Widgets_Visibility(true);
                        //Log.d(Constants.TAG, "SearchButtonClicked " );


                        String Todaysdate = getDatewithNameoftheDay();
                        String TomorrowsDate = getTomorrowsDate();

                        if (orderdetailsnewschema) {


                            callVendorOrderDetailsSeviceAndInitCallBack(TomorrowsDate,TomorrowsDate,vendorKey);
                        } else {
                           getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);

                        }



                    }

            }
        });
        todaysOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slottypefromSpinner = 0;
                todaysOrder.setBackground(getDrawable(requireContext(),R.drawable.orange_selected_button_background));
                todaysOrder.setTextColor(Color.WHITE);

                tomorrowsOrder.setBackground(getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
                tomorrowsOrder.setTextColor(Color.BLACK);

                Adjusting_Widgets_Visibility(true);
                String Todaysdate = getDatewithNameoftheDay();
                isSearchButtonClicked =false;
                String PreviousDaydate = getDatewithNameofthePreviousDay();
                if (orderdetailsnewschema) {

                    callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);

                }
                else{
                    getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

                }

            }
        });
        tomorrowsOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slottypefromSpinner =  1;
                tomorrowsOrder.setBackground(getDrawable(requireContext(),R.drawable.orange_selected_button_background));
                tomorrowsOrder.setTextColor(Color.WHITE);

                todaysOrder.setBackground(getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
                todaysOrder.setTextColor(Color.BLACK);

                Adjusting_Widgets_Visibility(true);
                String TomorrowsDate = getTomorrowsDate();
                isSearchButtonClicked =false;
                String Todaysdate = getDatewithNameoftheDay();
                if (orderdetailsnewschema) {

                    callVendorOrderDetailsSeviceAndInitCallBack(TomorrowsDate,TomorrowsDate,vendorKey);

                }
                else{
                    getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);

                }

            }
        });

        slotType_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                slottypefromSpinner = position;

                if(slottypefromSpinner == 0 ) {
                    Adjusting_Widgets_Visibility(true);
                    String Todaysdate = getDatewithNameoftheDay();
                    isSearchButtonClicked =false;
                    String PreviousDaydate = getDatewithNameofthePreviousDay();
                    if (orderdetailsnewschema) {


                        callVendorOrderDetailsSeviceAndInitCallBack(Todaysdate,Todaysdate,vendorKey);

                    }
                    else{
                        getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);

                    }
                }
                if(slottypefromSpinner == 1 ) {
                    Adjusting_Widgets_Visibility(true);
                    String TomorrowsDate = getTomorrowsDate();
                    isSearchButtonClicked =false;
                    String Todaysdate = getDatewithNameoftheDay();
                    if (orderdetailsnewschema) {

                        callVendorOrderDetailsSeviceAndInitCallBack(TomorrowsDate,TomorrowsDate,vendorKey);

                    }
                    else{
                        getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);

                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mobile_new_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus=Constants.NEW_ORDER_STATUS;
                Adjusting_Widgets_Visibility(true);
                saveCurrentStatusInSharedPref(orderStatus);
                isSearchButtonClicked =false;

                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

                selecting_The_Order_Status(mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget);



            }
        });

        mobile_confirmed_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adjusting_Widgets_Visibility(true);
                isSearchButtonClicked =false;

                orderStatus=Constants.CONFIRMED_ORDER_STATUS;
                saveCurrentStatusInSharedPref(orderStatus);

                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

                selecting_The_Order_Status(mobile_confirmed_Order_widget, mobile_new_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget);




            }
        });

        mobile_ready_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus=Constants.READY_FOR_PICKUP_ORDER_STATUS;

                Adjusting_Widgets_Visibility(true);
                saveCurrentStatusInSharedPref(orderStatus);
                isSearchButtonClicked =false;

                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

                selecting_The_Order_Status(mobile_ready_Order_widget, mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget);



            }
        });

        mobile_transist_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus=Constants.PICKEDUP_ORDER_STATUS;

                Adjusting_Widgets_Visibility(true);
                saveCurrentStatusInSharedPref(orderStatus);
                isSearchButtonClicked =false;

                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

                selecting_The_Order_Status(mobile_transist_Order_widget, mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_delivered_Order_widget);


            }
        });

        mobile_delivered_Order_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderStatus=Constants.DELIVERED_ORDER_STATUS;
                isSearchButtonClicked =false;

                Adjusting_Widgets_Visibility(true);
                         /* SharedPreferences sh
                        = mContext.getSharedPreferences("OrderDetailsFromSharedPreferences",
                        MODE_PRIVATE);

                Gson gson = new Gson();
                String json = sh.getString("orderDetails", "");
                Type type = new TypeToken<List<Modal_ManageOrders_Pojo_Class>>() {}.getType();
                List<Modal_ManageOrders_Pojo_Class> arrayList = gson.fromJson(json, type);
                if (json==null) {
                    Toast.makeText(mContext,"First Turn on the OrderSyncing Button in settings",Toast.LENGTH_LONG).show();
                } else {
                    displayorderDetailsinListview(orderStatus,arrayList);

                }

                */
                saveCurrentStatusInSharedPref(orderStatus);

                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

                selecting_The_Order_Status(mobile_delivered_Order_widget, mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget);

            }
        });

        mobile_search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked =false;
                horizontalScrollview.setVisibility(View.VISIBLE);

                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = mobile_search_barEditText.getText().toString().length();
                isSearchButtonClicked =true;
                horizontalScrollview.setVisibility(View.GONE);
                showKeyboard(mobile_search_barEditText);
                showSearchBarEditText();
            }
        });

    }
    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey) {
        if(isVendorOrdersTableServiceCalled){
            Adjusting_Widgets_Visibility(false);
            return;
        }
        isVendorOrdersTableServiceCalled = true;
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
                displayorderDetailsinListview(orderStatus,orderslist_fromResponse, slottypefromSpinner);

                //convertingJsonStringintoArray(orderStatus,mobile_jsonString);
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d(TAG, "Volley requester " + requestType);
                Log.d(TAG, "Volley JSON post" + "That didn't work!");
                Adjusting_Widgets_Visibility(false);
                isVendorOrdersTableServiceCalled = false;

            }
        };
        ordersList.clear();
        sorted_OrdersList.clear();
        mVolleyService = new VendorOrdersTableService(mResultCallback,mContext);
       // String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_type + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=APPORDER";
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_AppOrder_PhoneOrder + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype1=APPORDER"+"&ordertype2=PHONEORDER";
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;
        if(isnewOrdersSyncButtonClicked){
            return;
        }

        isnewOrdersSyncButtonClicked=true;
        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }









    private void getDeliveryPartnerList() {
        SharedPreferences preferences =mContext.getSharedPreferences("DeliveryPersonList",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getDeliveryPartnerList+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {
                            //converting jsonSTRING into array
                            String DeliveryPersonListString = response.toString();
                            ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);

                            SharedPreferences sharedPreferences
                                    = mContext.getSharedPreferences("DeliveryPersonList",
                                    MODE_PRIVATE);

                            SharedPreferences.Editor myEdit
                                    = sharedPreferences.edit();


                            myEdit.putString(
                                    "DeliveryPersonListString",
                                    DeliveryPersonListString);
                            myEdit.apply();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                SharedPreferences preferences =mContext.getSharedPreferences("DeliveryPersonList",Context.MODE_PRIVATE);
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
                Toast.makeText(mContext,"Error in Delivery Partner list :  "+errorCode,Toast.LENGTH_LONG).show();



                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", vendorKey);
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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);
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
    protected void onPostExecute(Void result){
        if(isAdded()){
            Toast.makeText(mContext,getResources().getString(R.string.app_name),Toast.LENGTH_LONG).show();
        }
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
                        mobile_jsonString =response.toString();
                       convertingJsonStringintoArray(selectedStatus, mobile_jsonString);

                       // adapter = new Adapter_AutoCompleteManageOrdersItem(mContext, mobile_jsonString);


                        //    mobile_search_barEditText.setAdapter(adapter);

                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                isnewOrdersSyncButtonClicked=false;
                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);

                error.printStackTrace();
            }
        })
        {
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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);




    }

    /*
    private void getOrderDetailsUsingApi(String date, String vendorKey, String selectedStatus) {
        if(isnewOrdersSyncButtonClicked){
            return;
        }


        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );



        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey + "?orderplaceddate="+date+"&vendorkey="+vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        mobile_jsonString=response.toString();
                        convertingJsonStringintoArray(selectedStatus,mobile_jsonString);

                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


                Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

    }

*/
    private void setDataForSpinner() {
        slotnameChoosingSpinnerData.clear();
        String TodaysDate = getDatewithNameoftheDay();
        TodaysDate = "Today's Date : "+TodaysDate;
        String TomorrowsDate = getTomorrowsDate();
        TomorrowsDate = "Tomorrow's Date : "+TomorrowsDate;

       // slotnameChoosingSpinnerData.add("Preorder and Express Delivery");
        slotnameChoosingSpinnerData.add(TodaysDate);
        slotnameChoosingSpinnerData.add(TomorrowsDate);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_spinner_item, slotnameChoosingSpinnerData);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        slotType_Spinner.setAdapter(arrayAdapter);

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
    private void Adusting_Widgets_Visibility() {
        bottomNavigationView.setVisibility(View.GONE);

        sorted_OrdersList.clear();
        manageOrders_ListView.setVisibility(View.GONE);
        mobile_orderinstruction.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.VISIBLE);
        loadingpanelmask.setVisibility(View.VISIBLE);




    }


    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    private void selecting_The_Order_Status(TextView selected_widget, TextView order_widget1, TextView order_widget2, TextView order_widget3, TextView order_widget4) {

        selected_widget.setBackground(getDrawable(requireContext(),R.drawable.orange_selected_button_background));
        selected_widget.setTextColor(Color.WHITE);

        order_widget1.setBackground(getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget1.setTextColor(Color.BLACK);

        order_widget2.setBackground(getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget2.setTextColor(Color.BLACK);

        order_widget3.setBackground(getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget3.setTextColor(Color.BLACK);

        order_widget4.setBackground(getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
        order_widget4.setTextColor(Color.BLACK);
     //   Adjusting_Widgets_Visibility(true);

    }


    @Override
    public void onResume() {
        super.onResume();


        SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", ""));

        SharedPreferences shared2 = requireContext().getSharedPreferences("CurrentSelectedStatus", MODE_PRIVATE);
        orderStatus = (shared2.getString("currentstatus", ""));
        TodaysDate=getDate();
        if(orderStatus.equals(Constants.NEW_ORDER_STATUS)){
            selecting_The_Order_Status(mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget);

        }
        if(orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)){
            selecting_The_Order_Status(mobile_confirmed_Order_widget, mobile_new_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget);

        }
        if(orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)){
            selecting_The_Order_Status(mobile_ready_Order_widget, mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_transist_Order_widget, mobile_delivered_Order_widget);

        }
        if(orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)){
            selecting_The_Order_Status(mobile_transist_Order_widget, mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_delivered_Order_widget);

        }
        if(orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)){
            selecting_The_Order_Status(mobile_delivered_Order_widget, mobile_new_Order_widget, mobile_confirmed_Order_widget, mobile_ready_Order_widget, mobile_transist_Order_widget);

        }
      //  getOrderDetailsUsingOrderPlacedDate(TodaysDate,vendorKey,orderStatus);

        //ordersList.clear();
        //  sorted_OrdersList.clear();
        //  TodaysDate=getDate();
        //  getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);

    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        mobile_nameofFacility_Textview.setVisibility(View.VISIBLE);
        mobile_search_button.setVisibility(View.VISIBLE);
        mobile_search_close_btn.setVisibility(View.GONE);
        mobile_search_barEditText.setVisibility(View.GONE);
    }

    private void showSearchBarEditText() {
        mobile_nameofFacility_Textview.setVisibility(View.GONE);
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
                InputMethodManager mgr = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Log.i(TAG,"Manage_Orders fragment Destroyed");
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //receive your data here
            //Log.v(Constants.TAG, "BroadCastSucess");
            sorted_OrdersList.clear();
            ordersList.clear();
            mobile_jsonString = intent.getStringExtra("response");
            convertingJsonStringintoArray(orderStatus, mobile_jsonString);

            //Log.v(Constants.TAG, "BroadCastSucess"+ mobile_jsonString);
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


    void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {

            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            manageOrders_ListView.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.GONE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);
            bottomNavigationView.setVisibility(View.VISIBLE);

        }

    }

    private void convertingJsonStringintoArray(String orderStatus, String jsonString) {
        Adjusting_Widgets_Visibility(true);
        try {
            String ordertype="#";

            ordersList.clear();
            sorted_OrdersList.clear();
            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
            if(arrayLength<=0){
                Adjusting_Widgets_Visibility(false);

            }

            for(;i1<(arrayLength);i1++) {

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


                    if ((ordertype.equals(Constants.APPORDER)) || (ordertype.equals(Constants.PhoneOrder))){

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

                    if (json.has("useraddresskey")) {
                        manageOrdersPojoClass.useraddresskey = String.valueOf(json.get("useraddresskey"));

                    } else {
                        manageOrdersPojoClass.useraddresskey = "";
                    }


                        if (json.has("orderplacedtime")) {
                        manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                            orderPlacedTime = String.valueOf(json.get("orderplacedtime"));
                        } else {
                        manageOrdersPojoClass.orderplacedtime = "";
                        }

                        try{
                           boolean isOrderPlacedlessThan3MinsBefore = CheckWeathertheOrderisPlacedLessThan3Mins(orderPlacedTime);
                            manageOrdersPojoClass.isOrderPlacedlessThan3MinsBefore = isOrderPlacedlessThan3MinsBefore;
                            Log.d(Constants.TAG, "log getfromsearch : " + isOrderPlacedlessThan3MinsBefore);


                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            manageOrdersPojoClass.orderplacedtime_in_long = getLongValuefortheDate(String.valueOf(json.get("orderplacedtime")));
                        }
                        catch (Exception e){
                            manageOrdersPojoClass.orderplacedtime_in_long = "";
                        }


                        if (json.has("orderconfirmedtime")) {
                        manageOrdersPojoClass.orderconfirmedtime = String.valueOf(json.get("orderconfirmedtime"));

                        } else {
                        manageOrdersPojoClass.orderconfirmedtime = "";
                        }

                        try{
                            manageOrdersPojoClass.orderconfirmedtime_in_long = getLongValuefortheDate(String.valueOf(json.get("orderconfirmedtime")));
                        }
                        catch (Exception e){
                            manageOrdersPojoClass.orderconfirmedtime_in_long = "";
                        }


                        if (json.has("orderreadytime")) {
                        manageOrdersPojoClass.orderreadytime = String.valueOf(json.get("orderreadytime"));

                        } else {
                        manageOrdersPojoClass.orderreadytime = "";
                        }


                        try{
                            manageOrdersPojoClass.orderreadytime_in_long = getLongValuefortheDate( String.valueOf(json.get("orderreadytime")));
                        }
                        catch (Exception e){
                            manageOrdersPojoClass.orderreadytime_in_long = "";
                        }



                        if (json.has("orderpickeduptime")) {
                        manageOrdersPojoClass.orderpickeduptime = String.valueOf(json.get("orderpickeduptime"));

                        } else {
                        manageOrdersPojoClass.orderpickeduptime = "";
                        }


                        try{
                            manageOrdersPojoClass.orderpickeduptime_in_long = getLongValuefortheDate(String.valueOf(json.get("orderpickeduptime")));
                        }
                        catch (Exception e){
                            manageOrdersPojoClass.orderpickeduptime_in_long = "";
                        }



                        if (json.has("orderdeliveredtime")) {
                        manageOrdersPojoClass.orderdeliveredtime = String.valueOf(json.get("orderdeliveredtime"));

                        } else {
                        manageOrdersPojoClass.orderdeliveredtime = "";
                        }


                        try{
                            manageOrdersPojoClass.orderdeliveredtime_in_long = getLongValuefortheDate(String.valueOf(json.get("orderdeliveredtime")));
                        }
                        catch (Exception e){
                            manageOrdersPojoClass.orderdeliveredtime_in_long = "";
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

                        if (json.has("slotname")) {
                            manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                        } else {
                            manageOrdersPojoClass.slotname = "";
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





                    if (json.has("notes")) {
                        manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                    } else {
                        manageOrdersPojoClass.notes = "";
                    }


                    if (json.has("slotdate")) {
                        manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));

                    } else {
                        manageOrdersPojoClass.slotdate = "";
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
            displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);


        } catch (JSONException e) {
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);
            Toast.makeText(mContext, String.valueOf(e), Toast.LENGTH_SHORT).show();
            isnewOrdersSyncButtonClicked=false;

        }
    }



    private String getTomorrowsDate() {
        Date todaysDate = Calendar.getInstance().getTime();
        System.out.println("nextDate " + todaysDate);

        String next_day = "";
        SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        //dateFormatForDisplaying.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



        if (orderdetailsnewschema) {

            dateFormatForDisplaying = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        } else {
             dateFormatForDisplaying = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);

        }
        dateFormatForDisplaying.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        //calander_view.setCurrentDayBackgroundColor(context.getResources().getColor(R.color.gray_color));
        String date_format = dateFormatForDisplaying.format(todaysDate);
        final Calendar calendar = Calendar.getInstance();
        try {
            Date date = dateFormatForDisplaying.parse(date_format);
            calendar.setTime(date);
            calendar.add(Calendar.DATE, 1);
            String nex = dateFormatForDisplaying.format(calendar.getTime());
            Date d1 = dateFormatForDisplaying.parse(nex);

            if (orderdetailsnewschema) {

                next_day = nex;

            } else {
                next_day = d1+", "+nex;

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return next_day;


    }


    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();


        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd" ,Locale.ENGLISH) ;
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String PreviousdayDate = df.format(c1);
            return PreviousdayDate;

        }
        else {
            SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
            previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String PreviousdayDay = previousday.format(c1);

            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
            df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            String PreviousdayDate = df1.format(c1);

            PreviousdayDate = PreviousdayDay + ", " + PreviousdayDate;


            return PreviousdayDate;
        }
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




            return CurrentDate;

        }
    }





    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);
        if(orderdetailsnewschema){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(c);
            return CurrentDate;

        }
        else {
            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = df.format(c);



            return CurrentDate;
        }
    }


    @SuppressLint("DefaultLocale")
    public  void displayorderDetailsinListview(String orderStatus, List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner) {
        //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());
         newCount=0;confirmedCount=0;readyForPickupCount=0;transitCount=0;deliveredCount=0;
        if(ordersList.size()>0) {
            Adjusting_Widgets_Visibility(true);
            sorted_OrdersList.clear();
            String TodaysDate = getDatewithNameoftheDay();
            String TomorrowsDate = getTomorrowsDate();
            //Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

            //Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);
            if(orderdetailsnewschema){
                TodaysDate = convertnewFormatDateintoOldFormat(TodaysDate);
                TomorrowsDate = convertnewFormatDateintoOldFormat(TomorrowsDate);

            }
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
                        modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                        modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                        modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                        modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();
                        modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus().toUpperCase();


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


                        modal_manageOrders_forOrderDetailList1.orderplacedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderplacedtime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderreadytime_in_long = modal_manageOrders_forOrderDetailList.getOrderreadytime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime_in_long = modal_manageOrders_forOrderDetailList.getOrderpickeduptime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime_in_long = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime_in_long();
                        modal_manageOrders_forOrderDetailList1.isOrderPlacedlessThan3MinsBefore = modal_manageOrders_forOrderDetailList.isOrderPlacedlessThan3MinsBefore();


                        if ((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210")) && (!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                            sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                        }


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
                        modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                        modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                        modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                        modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                        modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                        modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                        modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();
                        modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();
                        modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();

                        modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus().toUpperCase();

                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                        modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();


                        modal_manageOrders_forOrderDetailList1.orderplacedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderplacedtime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderreadytime_in_long = modal_manageOrders_forOrderDetailList.getOrderreadytime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime_in_long = modal_manageOrders_forOrderDetailList.getOrderpickeduptime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime_in_long = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime_in_long();

                        modal_manageOrders_forOrderDetailList1.isOrderPlacedlessThan3MinsBefore = modal_manageOrders_forOrderDetailList.isOrderPlacedlessThan3MinsBefore();

                        if ((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210")) && (!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                            sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                        }
                    }

                }
            } else {
                for (int i = 0; i < ordersList.size(); i++) {
                    //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                    String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                    //Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);
                    String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();

                    if (orderStatus.equals(orderstatusfromOrderList)) {
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
                        modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                        modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                        modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                        modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();
                        modal_manageOrders_forOrderDetailList1.useraddresskey = modal_manageOrders_forOrderDetailList.getUseraddresskey();
                        modal_manageOrders_forOrderDetailList1.deliveryamount = modal_manageOrders_forOrderDetailList.getDeliveryamount();

                        modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                        modal_manageOrders_forOrderDetailList1.userstatus = modal_manageOrders_forOrderDetailList.getUserstatus().toUpperCase();

                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                        modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();


                        modal_manageOrders_forOrderDetailList1.orderplacedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderplacedtime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderconfirmedtime_in_long = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderreadytime_in_long = modal_manageOrders_forOrderDetailList.getOrderreadytime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderpickeduptime_in_long = modal_manageOrders_forOrderDetailList.getOrderpickeduptime_in_long();
                        modal_manageOrders_forOrderDetailList1.orderdeliveredtime_in_long = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime_in_long();
                        modal_manageOrders_forOrderDetailList1.isOrderPlacedlessThan3MinsBefore = modal_manageOrders_forOrderDetailList.isOrderPlacedlessThan3MinsBefore();


                        if ((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210")) && (!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                            sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                        }
                    }

                }
            }

            for (int i = 0; i < ordersList.size(); i++) {
                //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                if (orderstatusfromOrderList.equals(Constants.NEW_ORDER_STATUS)) {
                    newCount++;

                  //  Log.i("Tag", "Count New : " + newCount);

                } else if (orderstatusfromOrderList.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                    confirmedCount++;


                  //  Log.i("Tag", "Count confirmed : " + confirmedCount);

                } else if (orderstatusfromOrderList.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                    readyForPickupCount++;

                   // Log.i("Tag", "Count ready : " + readyForPickupCount);

                } else if (orderstatusfromOrderList.equals(Constants.PICKEDUP_ORDER_STATUS)) {
                    transitCount++;


                  //  Log.i("Tag", "Count transit : " + transitCount);

                } else if (orderstatusfromOrderList.equals(Constants.DELIVERED_ORDER_STATUS)) {
                    deliveredCount++;

                    //Log.i("Tag", "Count delivered : " + deliveredCount);

                } else {
                    Log.i("Tag", "Count Status not matched ");

                }
            }
            //1
            if (newCount > 0) {
                mobile_new_Order_widget.setText(String.format("%s ( %d )", Constants.NEW_ORDER_STATUS, newCount));
            } else {
                mobile_new_Order_widget.setText(String.format("%s", Constants.NEW_ORDER_STATUS));


            }
            //2
            if (confirmedCount > 0) {
                mobile_confirmed_Order_widget.setText(String.format("%s ( %d )", Constants.CONFIRMED_ORDER_STATUS, confirmedCount));
            } else {
                mobile_confirmed_Order_widget.setText(String.format("%s", Constants.CONFIRMED_ORDER_STATUS));

            }
            //3
            if (readyForPickupCount > 0) {
                mobile_ready_Order_widget.setText(String.format("%s ( %d )", Constants.READY_FOR_PICKUP_ORDER_STATUS, readyForPickupCount));
            } else {
                mobile_ready_Order_widget.setText(String.format("%s", Constants.READY_FOR_PICKUP_ORDER_STATUS));

            }

            //4
            if (transitCount > 0) {
                mobile_transist_Order_widget.setText(String.format("%s ( %d )", Constants.PICKEDUP_ORDER_STATUS, transitCount));
            } else {
                mobile_transist_Order_widget.setText(String.format("%s", Constants.PICKEDUP_ORDER_STATUS));

            }

            //5

            if (deliveredCount > 0) {
                mobile_delivered_Order_widget.setText(String.format("%s ( %d )", Constants.DELIVERED_ORDER_STATUS, deliveredCount));
            } else {
                mobile_delivered_Order_widget.setText(String.format("%s", Constants.DELIVERED_ORDER_STATUS));

            }


            try {
                if (sorted_OrdersList.size() > 0) {
                    if (orderStatus.equals(Constants.NEW_ORDER_STATUS)) {
                        Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                            public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                                return object2.getOrderplacedtime_in_long().compareTo(object1.getOrderplacedtime_in_long());
                            }
                        });
                    }
                    if (orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                        Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                            public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                                return object2.getOrderconfirmedtime_in_long().compareTo(object1.getOrderconfirmedtime_in_long());
                            }
                        });
                    }

                    if (orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                        Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                            public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                                return object2.getOrderreadytime_in_long().compareTo(object1.getOrderreadytime_in_long());
                            }
                        });
                    }

                    if (orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)) {
                        Collections.sort(sorted_OrdersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
                            public int compare(final Modal_ManageOrders_Pojo_Class object1, final Modal_ManageOrders_Pojo_Class object2) {
                                return object2.getOrderpickeduptime_in_long().compareTo(object1.getOrderpickeduptime_in_long());
                            }
                        });
                    }


                    if (orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)) {
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
                    }

                    isnewOrdersSyncButtonClicked = false;

                    adapterMobileManageOrdersListView = new Adapter_Mobile_ManageOrders_ListView1(mContext, sorted_OrdersList, Mobile_ManageOrders1.this, orderStatus);
                    manageOrders_ListView.setAdapter(adapterMobileManageOrdersListView);


                    mobile_orderinstruction.setVisibility(View.GONE);
                    Adjusting_Widgets_Visibility(false);

                } else {


                    isnewOrdersSyncButtonClicked = false;

                    if (orderStatus.equals(Constants.NEW_ORDER_STATUS)) {
                        mobile_orderinstruction.setText("No New Orders");

                        mobile_orderinstruction.setVisibility(View.VISIBLE);


                    }
                    if (orderStatus.equals(Constants.CONFIRMED_ORDER_STATUS)) {
                        mobile_orderinstruction.setVisibility(View.VISIBLE);
                        mobile_orderinstruction.setText("No Confirmed Orders ");


                    }
                    if (orderStatus.equals(Constants.READY_FOR_PICKUP_ORDER_STATUS)) {
                        mobile_orderinstruction.setVisibility(View.VISIBLE);
                        mobile_orderinstruction.setText("No  Order is Ready for Pickup");


                    }
                    if (orderStatus.equals(Constants.PICKEDUP_ORDER_STATUS)) {
                        mobile_orderinstruction.setVisibility(View.VISIBLE);
                        mobile_orderinstruction.setText("No Transit Orders");


                    }
                    if (orderStatus.equals(Constants.DELIVERED_ORDER_STATUS)) {
                        mobile_orderinstruction.setVisibility(View.VISIBLE);
                        mobile_orderinstruction.setText("No Delivered Orders ");


                    }
                    Adjusting_Widgets_Visibility(false);

                }
            } catch (Exception e) {
                isnewOrdersSyncButtonClicked = false;
                Adjusting_Widgets_Visibility(false);

                e.printStackTrace();
            }
        }
        else{

              mobile_new_Order_widget.setText(String.format("%s", Constants.NEW_ORDER_STATUS));
              mobile_confirmed_Order_widget.setText(String.format("%s", Constants.CONFIRMED_ORDER_STATUS));
             mobile_ready_Order_widget.setText(String.format("%s", Constants.READY_FOR_PICKUP_ORDER_STATUS));
             mobile_transist_Order_widget.setText(String.format("%s", Constants.PICKEDUP_ORDER_STATUS));
             mobile_delivered_Order_widget.setText(String.format("%s", Constants.DELIVERED_ORDER_STATUS));



            isnewOrdersSyncButtonClicked = false;
            Adjusting_Widgets_Visibility(false);
            Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();

        }

//callAdapter();


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




    public  boolean CheckWeathertheOrderisPlacedLessThan3Mins(String orderPlacedTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        Date date = null;
        try {
            date = dateFormat.parse(orderPlacedTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ////Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);



        calendar.add(Calendar.MINUTE, 3);



        Date c1 = calendar.getTime();
        SimpleDateFormat df1 = new SimpleDateFormat("EEE",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String predictedday = df1.format(c1);



        SimpleDateFormat df2 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df2.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String  predicteddate = df2.format(c1);
        String predicteddateandday = predictedday+", "+predicteddate;


        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        df3.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



        String  predictedtime = df3.format(c1);
        String predicteddateanddayandtime = predictedday+", "+predicteddate+" "+predictedtime;


        long predictedLongForDate = Long.parseLong(getLongValuefortheDate(predicteddateanddayandtime));
        String  currentTime = getDate_and_time();

        long currentTimeLong = Long.parseLong(getLongValuefortheDate(currentTime));
        if(currentTimeLong>=predictedLongForDate){//current time is greater or equals order placed time + 3 minutes

            return false;

        }
        else{

            Log.d(Constants.TAG, "log currentTimeLong : " +currentTimeLong);
            Log.d(Constants.TAG, "log predictedLongForDate : " +predictedLongForDate);
            return true;
        }

    }




  /*  private Long getLongValuefortheDate(String orderplacedtime) {
        long time1long =  0;
        String longvalue="";
        try {
            String time1 = orderplacedtime;
            //   Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = sdf.parse(time1);
            time1long = date.getTime() / 1000;
            longvalue = String.valueOf(time1long);

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
                time1long = date.getTime() / 1000;
                longvalue = String.valueOf(time1long);

                //   long differencetime = time2long - time1long;
                //  Log.d(TAG, "   "+orderplacedtime);

                //    Log.d(TAG, "time1long  "+time1long);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return time1long;
    }


    */





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


    public void printRecipt(Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class) {

        loadingPanel.setVisibility(View.VISIBLE);
        loadingpanelmask.setVisibility(View.VISIBLE);

        manageOrders_ListView.setVisibility(View.GONE);

        if (BluetoothPrintDriver.IsNoConnection()) {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);

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
        Adjusting_Widgets_Visibility(true);
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
            OrderType= modal_manageOrders_pojo_class.getOrderType().toUpperCase();
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
                Adjusting_Widgets_Visibility(true);

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
/*
            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
            BluetoothPrintDriver.SetFontEnlarge((byte) 0x04);
            BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
            BluetoothPrintDriver.SetAlignMode((byte) 49);
            BluetoothPrintDriver.printString(Title);
            BluetoothPrintDriver.BT_Write("\r");
            BluetoothPrintDriver.LF();


 */

            if(vendorKey.equals("vendor_4")){

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
                if(!CouponDiscount.contains(".")){
                    CouponDiscount = CouponDiscount+".00";
                }
                couponDiscount_double = Double.parseDouble (CouponDiscount);
                if ((OrderType.equals(Constants.APPORDER)) || ((OrderType.equals(Constants.PhoneOrder)))) {
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
            BluetoothPrintDriver.printString("Order Type : "+OrderType);
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




            Adjusting_Widgets_Visibility(false);

        }
        catch ( Exception e){
            Adjusting_Widgets_Visibility(false);

            e.printStackTrace();
        }



    }
    public String getLongValuefortheDate(String orderplacedtime) {
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

        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
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

                result = result.replaceAll("IST[+]05:30", "");

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        bottomNavigationView = ((Moblie_Dashboard_Screen) getActivity()).findViewById(R.id.bottomnav);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,new IntentFilter("YOUR"));

        return inflater.inflate(R.layout.mobile__manage_orders_fragment1, container, false);
    }
}



























/*


    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, okhttp3.Response response) {
            super.onOpen(webSocket, response);


            runOnUiThread(() -> {
                webSocket.send("12/26/2020,vendor_1");

                Toast.makeText(mContext,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();
                //   initializeView();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            websocket_OrdersList.clear();
            orderinstruction.setVisibility(View.INVISIBLE);

            runOnUiThread(() -> {
                //Log.i("result","t   "+text);
                JSONArray array = null;
                try {
                    array = new JSONArray(text);
                    //Log.i(" array.length()", String.valueOf(array.length()));

                    for(int i=0; i < array.length(); i++)
                    {
                        JSONObject json = array.getJSONObject(i);
                        Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                        manageOrdersPojoClass.orderid =String.valueOf(json.get("orderid"));
                        if(String.valueOf(json.get("orderid")).equals("There is no order Today"))
                        {
                            orderinstruction.setVisibility(View.VISIBLE);
                            Toast.makeText(mContext,
                                    "There is no order yet",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));
                            manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));
                            manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));
                            manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));
                            manageOrdersPojoClass.taxamount = String.valueOf(json.get("taxamount"));
                            manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobile"));
                            manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));
                            manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscamount"));
                         //   manageOrdersPojoClass.itemdesp = String.valueOf(json.get("itemdesp"));
                            manageOrdersPojoClass.orderstatus = String.valueOf(json.get("orderstatus"));
                            manageOrdersPojoClass.keyfromtrackingDetails = String.valueOf(json.get("keyfromtrackingOrderTable"));
                            websocket_OrdersList.add(manageOrdersPojoClass);


                            System.out.println(json.getString("orderid"));
                            System.out.println(json.getString("orderplacedtime"));
                            System.out.println(json.getString("payableamount"));

                            System.out.println(json.getString("paymentmode"));
                            System.out.println(json.getString("tokenno"));
                            System.out.println(json.getString("taxamount"));

                            System.out.println(json.getString("usermobile"));
                            System.out.println(json.getString("vendorkey"));
                            System.out.println(json.getString("coupondiscamount"));

                            System.out.println(json.getString("itemdesp"));
                            System.out.println(json.getString("orderstatus"));
                        }

                    }
              //      SavingtheOrderArrayLocally(websocket_OrdersList);



                } catch (Exception ex) {
                    StringWriter stringWriter = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stringWriter));
                    //Log.e("exception ::: ", stringWriter.toString());
                }
            });

        }

    }













    private void SavingtheOrderArrayLocally(List<Modal_ManageOrders_Pojo_Class> websocket_ordersList) {
        if(ordersList.size()==0){
            for(int i =0; i<websocket_ordersList.size();i++) {
                final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =websocket_ordersList.get(i);
                modal_manageOrders_pojo_class.orderid =modal_manageOrders_pojo_class.getOrderid();
                modal_manageOrders_pojo_class.orderplacedtime =modal_manageOrders_pojo_class.getOrderplacedtime();
                modal_manageOrders_pojo_class.payableamount =modal_manageOrders_pojo_class.getPayableamount();
                modal_manageOrders_pojo_class.paymentmode =modal_manageOrders_pojo_class.getPaymentmode();
                modal_manageOrders_pojo_class.tokenno =modal_manageOrders_pojo_class.getTokenno();
                modal_manageOrders_pojo_class.taxamount =modal_manageOrders_pojo_class.getTaxamount();
                modal_manageOrders_pojo_class.usermobile =modal_manageOrders_pojo_class.getUsermobile();
                modal_manageOrders_pojo_class.vendorkey =modal_manageOrders_pojo_class.getVendorkey();
                modal_manageOrders_pojo_class.coupondiscamount =modal_manageOrders_pojo_class.getCoupondiscamount();
        //        modal_manageOrders_pojo_class.itemdesp =modal_manageOrders_pojo_class.getItemdesp();
                modal_manageOrders_pojo_class.orderstatus =modal_manageOrders_pojo_class.getOrderstatus();
                modal_manageOrders_pojo_class.keyfromtrackingDetails =modal_manageOrders_pojo_class.getKeyfromtrackingDetails();

                ordersList.add(modal_manageOrders_pojo_class);




            }
            SortingtheOrderAccordingtoStatus(orderStatus, websocket_OrdersList);

        }
        else {
            for (int i = 0; i < websocket_ordersList.size(); i++) {
                final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = websocket_ordersList.get(i);
                String orderid_from_websocket_ordersList =modal_manageOrders_pojo_class.getOrderid();
                for(int j =0;j<ordersList.size();j++){
                    final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderList =ordersList.get(j);
                    String orderid_from_ordersList =modal_manageOrders_forOrderList.getOrderid();
                    if(orderid_from_websocket_ordersList.equals(orderid_from_ordersList))
                    {
                        ordersList.remove(j);
                        //Log.i(Constants.TAG,orderid_from_ordersList+" this order is removed ");

                    }
                    else{
                        //Log.i(Constants.TAG,orderid_from_ordersList+" this order is not repeated ");
                    }

                }

                modal_manageOrders_pojo_class.orderid =modal_manageOrders_pojo_class.getOrderid();
                modal_manageOrders_pojo_class.orderplacedtime =modal_manageOrders_pojo_class.getOrderplacedtime();
                modal_manageOrders_pojo_class.payableamount =modal_manageOrders_pojo_class.getPayableamount();
                modal_manageOrders_pojo_class.paymentmode =modal_manageOrders_pojo_class.getPaymentmode();
                modal_manageOrders_pojo_class.tokenno =modal_manageOrders_pojo_class.getTokenno();
                modal_manageOrders_pojo_class.taxamount =modal_manageOrders_pojo_class.getTaxamount();
                modal_manageOrders_pojo_class.usermobile =modal_manageOrders_pojo_class.getUsermobile();
                modal_manageOrders_pojo_class.vendorkey =modal_manageOrders_pojo_class.getVendorkey();
                modal_manageOrders_pojo_class.coupondiscamount =modal_manageOrders_pojo_class.getCoupondiscamount();
              //  modal_manageOrders_pojo_class.itemdesp =modal_manageOrders_pojo_class.getItemdesp();
                modal_manageOrders_pojo_class.orderstatus =modal_manageOrders_pojo_class.getOrderstatus();
                modal_manageOrders_pojo_class.keyfromtrackingDetails =modal_manageOrders_pojo_class.getKeyfromtrackingDetails();

                ordersList.add(modal_manageOrders_pojo_class);



            }
            SortingtheOrderAccordingtoStatus(orderStatus, ordersList);

        }
    }

    private void SortingtheOrderAccordingtoStatus(String orderStatus, List<Modal_ManageOrders_Pojo_Class> ordersList) {
        sorted_OrdersList.clear();
        for(int i =0; i<ordersList.size();i++) {
            final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(i);
            if (orderStatus.equals(modal_manageOrders_pojo_class.getOrderstatus())) {
                modal_manageOrders_pojo_class.orderid =modal_manageOrders_pojo_class.getOrderid();
                modal_manageOrders_pojo_class.orderplacedtime =modal_manageOrders_pojo_class.getOrderplacedtime();
                modal_manageOrders_pojo_class.payableamount =modal_manageOrders_pojo_class.getPayableamount();
                modal_manageOrders_pojo_class.paymentmode =modal_manageOrders_pojo_class.getPaymentmode();
                modal_manageOrders_pojo_class.tokenno =modal_manageOrders_pojo_class.getTokenno();
                modal_manageOrders_pojo_class.taxamount =modal_manageOrders_pojo_class.getTaxamount();
                modal_manageOrders_pojo_class.usermobile =modal_manageOrders_pojo_class.getUsermobile();
                modal_manageOrders_pojo_class.vendorkey =modal_manageOrders_pojo_class.getVendorkey();
                modal_manageOrders_pojo_class.coupondiscamount =modal_manageOrders_pojo_class.getCoupondiscamount();
             //   modal_manageOrders_pojo_class.itemdesp =modal_manageOrders_pojo_class.getItemdesp();
                modal_manageOrders_pojo_class.orderstatus =modal_manageOrders_pojo_class.getOrderstatus();
                modal_manageOrders_pojo_class.keyfromtrackingDetails =modal_manageOrders_pojo_class.getKeyfromtrackingDetails();

                sorted_OrdersList.add(modal_manageOrders_pojo_class);

            }
            else {
                //Log.i(Constants.TAG,"Status of this  Order is "+modal_manageOrders_pojo_class.getOrderstatus());
            }

        }
        if(sorted_OrdersList.size()==0){
            loadingPanel_dailyItemWisereport.setVisibility(View.GONE);
            loadingpanelmask_dailyItemWisereport.setVisibility(View.GONE);
            bottomNavigationView.setVisibility(View.VISIBLE);
            orderinstruction.setVisibility(View.VISIBLE);

            Toast.makeText(mContext,
                    "There is no "+orderStatus+" Order!",
                    Toast.LENGTH_SHORT).show();

        }
        loadingPanel_dailyItemWisereport.setVisibility(View.GONE);
        loadingpanelmask_dailyItemWisereport.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        manageOrders_ListView.setVisibility(View.VISIBLE);

        manageOrdersListViewAdapter = new Adapter_Mobile_ManageOrders_ListView1(mContext, sorted_OrdersList, this.orderStatus);
        manageOrders_ListView.setAdapter(manageOrdersListViewAdapter);

    }

 */