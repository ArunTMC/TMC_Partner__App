package com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
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
    String jsonString,orderStatus,vendorKey,slotName = "EXPRESS DELIVERY";
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
    Button expressOrder_widget,preorder_widget;
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




    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        websocket_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        slotnameChoosingSpinnerData= new ArrayList<>();
       // swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        expressOrder_widget = view.findViewById(R.id.expressOrder_widget);
        preorder_widget = view.findViewById(R.id.preorder_widget);
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
        vendorKey = (shared.getString("VendorKey", "vendor_1"));

        SharedPreferences shared2 = requireContext().getSharedPreferences("CurrentSelectedStatus", MODE_PRIVATE);
        orderStatus = (shared2.getString("currentstatus", ""));
        TodaysDate=getDate();


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"Loading ..... Please wait",Toast.LENGTH_SHORT).show();

            }
        });


        if(selected_OrderType == 0 ){
            showProgressBar(true);
            showOrderInstructionText(false);
            isSearchButtonClicked =false;

            getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);

        }
        if(selected_OrderType == 1 ) {
            showProgressBar(true);
            showOrderInstructionText(false);
            isSearchButtonClicked =false;

            String Todaysdate = getDatewithNameoftheDay();
            String PreviousDaydate = getDatewithNameofthePreviousDay();

            getOrderDetailsUsingOrderSlotDate(PreviousDaydate,Todaysdate, vendorKey, orderStatus);
        }
        if(selected_OrderType == 2 ) {
            showProgressBar(true);
            showOrderInstructionText(false);
            isSearchButtonClicked =false;
            String Todaysdate = getDatewithNameoftheDay();

            String TomorrowsDate = getTomorrowsDate();
            getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);
        }
/*
       setDataForSpinner();

        slotType_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selected_OrderType = position;
                Log.i("Spinner","position   "+position);
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

        expressOrder_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expressOrder_widget.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_selected_button_background));
                expressOrder_widget.setTextColor(Color.WHITE);


                preorder_widget.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_non_selected_button_background));
                preorder_widget.setTextColor(Color.BLACK);
                selected_OrderType =0;

                preorderdate_layout.setVisibility(View.GONE);
                showProgressBar(true);
                showOrderInstructionText(false);
                isSearchButtonClicked =false;

                getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);

            }
        });
        preorder_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preorderdate_layout.setVisibility(View.VISIBLE);
                preorder_widget.setBackground(ContextCompat.getDrawable(requireContext(),R.drawable.orange_selected_button_background));
                preorder_widget.setTextColor(Color.WHITE);
                expressOrder_widget.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.orange_non_selected_button_background));
                expressOrder_widget.setTextColor(Color.BLACK);
                if(isRadioButtonSelected) {
                    showProgressBar(true);
                    showOrderInstructionText(false);
                    isSearchButtonClicked =false;

                    if (selected_OrderType == 1 || selected_OrderType == 0) {
                        selected_OrderType = 1;
                        String Todaysdate = getDatewithNameoftheDay();
                        isSearchButtonClicked =false;
                        String PreviousDaydate = getDatewithNameofthePreviousDay();

                        getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);
                    }
                    if (selected_OrderType == 2) {
                        showProgressBar(true);
                        isSearchButtonClicked =false;
                        String Todaysdate = getDatewithNameoftheDay();

                        String TomorrowsDate = getTomorrowsDate();
                        getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);
                    }

                }
            }
        });

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
                            orderinstruction.setVisibility(View.GONE);

                            manageOrdersListViewAdapter = new Adapter_Pos_ManageOrders_ListView(mContext, sorted_OrdersList, Pos_ManageOrderFragment.this, orderstatus);
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

                    getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);

                }
                if(selected_OrderType == 1 ) {
                    showProgressBar(true);
                    showOrderInstructionText(false);
                    isSearchButtonClicked =false;
                    String PreviousDaydate = getDatewithNameofthePreviousDay();

                    String Todaysdate = getDatewithNameoftheDay();
                    getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);
                }
                if(selected_OrderType == 2 ) {
                    showProgressBar(true);
                    showOrderInstructionText(false);
                    isSearchButtonClicked =false;
                    String Todaysdate = getDatewithNameoftheDay();

                    String TomorrowsDate = getTomorrowsDate();
                    getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);
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

                getOrderDetailsUsingApi(TodaysDate,vendorKey,orderStatus);
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
        vendorKey = (shared.getString("VendorKey", "vendor_1"));

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




    private void getOrderDetailsUsingOrderSlotDate(String previousDaydate, String SlotDate, String vendorKey, String selectedStatus) {

        if(isnewOrdersSyncButtonClicked){
            return;
        }


        Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        ordersList.clear();
        sorted_OrdersList.clear();



        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforSlotDate_Vendorkey + "?slotdate="+SlotDate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDaydate,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
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

                Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getCause());

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);




    }
    private void getOrderDetailsUsingApi(String date, String vendorKey, String selectedStatus) {
       if(isnewOrdersSyncButtonClicked){
           return;
       }


        Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        ordersList.clear();
        sorted_OrdersList.clear();



        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetailsforDate_Vendorkey + "?orderplaceddate="+date+"&vendorkey="+vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        jsonString=response.toString();
                        convertingJsonStringintoArray(selectedStatus,jsonString);

                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


                Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                showOrderInstructionText(true);
                isnewOrdersSyncButtonClicked=false;

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(50 * 10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

    }


    private void initiateSocketConnection() {
        Log.i("SocketConnection","t   ");
        runOnUiThread(() -> {

            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder().url(SERVER_PATH).addHeader("VendorKey",vendorKey).build();
            Log.i("SocketConnection","  "+request.toString());
           webSocket = client.newWebSocket(request, new SocketListener());

        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bottomNavigationView = ((Pos_Dashboard_Screen) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver,new IntentFilter("YOUR"));

        return inflater.inflate(R.layout.pos_manage_order_fragment, container, false);
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

            SimpleDateFormat day = new SimpleDateFormat("EEE");
            CurrentDay = day.format(c);

            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(c);

            CurrentDate = CurrentDay+", "+CurrentDate;
            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;
        }


        @SuppressLint("LongLogTag")
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.i("result","t   "+text);
            Boolean statusChangedLocally = false;
            // websocket_OrdersList.clear();
            //orderinstruction.setVisibility(View.INVISIBLE);

            Log.i("result","t   "+text);
            JSONArray array = null;
            try {
                array = new JSONArray(text);
                Log.i(" array.length()", String.valueOf(array.length()));

                for(int i=0; i < array.length(); i++)
                {
                    JSONObject json = array.getJSONObject(i);
                    String orderidfromsocketarray =String.valueOf(json.get("orderid"));
                    String orderstatusfromsocketarray =String.valueOf(json.get("orderstatus"));
                    Log.i("orderid from socketresponse", orderidfromsocketarray);
                    Log.i("status from socketresponse", orderstatusfromsocketarray);
                    for(int j=0;j<ordersList.size();j++)
                    {
                        final Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class =ordersList.get(j);
                        String orderidfromlocal =modal_manageOrders_pojo_class.getOrderid();
                        Log.i("orderid from local array", orderidfromlocal);

                        if(orderidfromlocal.equals(orderidfromsocketarray)){
                            statusChangedLocally =true;
                            Log.i(" in if", String.valueOf(statusChangedLocally));

                            modal_manageOrders_pojo_class.setOrderstatus(orderstatusfromsocketarray);
                            ordersList.set(j,modal_manageOrders_pojo_class);
                            displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);
                        }


                    }
                    if(!statusChangedLocally){
                        Log.i(" in if !! ", String.valueOf(statusChangedLocally));

                        //getorderDetailsDataFromDynamoDB(orderidfromsocketarray,orderstatusfromsocketarray);
                    }
                }




            } catch (Exception ex) {
                StringWriter stringWriter = new StringWriter();
                ex.printStackTrace(new PrintWriter(stringWriter));
                Log.e("exception ::: ", stringWriter.toString());
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
            Log.v(Constants.TAG, "BroadCastSucess");
            sorted_OrdersList.clear();
            ordersList.clear();
             jsonString = intent.getStringExtra("response");
            convertingJsonStringintoArray(orderStatus,jsonString);
            
            Log.v(Constants.TAG, "BroadCastSucess"+jsonString);
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

    private void convertingJsonStringintoArray(String orderStatus, String jsonString) {
        try {
            ordersList.clear();
            sorted_OrdersList.clear();
            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            String ordertype="#";
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

                    if(json.has("notes")){
                        manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                    }
                    else{
                        manageOrdersPojoClass.notes ="";
                    }


                    if(json.has("ordertype")){
                        manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                        ordertype = String.valueOf(json.get("ordertype"));
                    }
                    else{
                        ordertype="#";
                        manageOrdersPojoClass.orderType ="";
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
                    }catch (Exception E){
                            manageOrdersPojoClass.useraddress ="-";
                            E.printStackTrace();
                        }
                        try {
                            if (ordertype.toUpperCase().equals(Constants.APPORDER)) {


                                if (json.has("deliverydistance")) {

                                String deliverydistance =  String.valueOf(json.get("deliverydistance"));
                                if(!deliverydistance.equals(null)&&(!deliverydistance.equals("null"))){
                                    manageOrdersPojoClass.deliverydistance = String.valueOf(json.get("deliverydistance"));

                                }
                                else {
                                    manageOrdersPojoClass.deliverydistance ="0";

                                }
                            } else {
                                manageOrdersPojoClass.deliverydistance = "0";
                            }


                        }
                    }catch (Exception E){
                        manageOrdersPojoClass.deliverydistance ="0";
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

                    Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    isnewOrdersSyncButtonClicked=false;

                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());



            }


            }

                Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);

                //saveorderDetailsInLocal(ordersList);
                displayorderDetailsinListview(orderStatus,ordersList, selected_OrderType);


        } catch (JSONException e) {
            e.printStackTrace();
            isnewOrdersSyncButtonClicked=false;

        }
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




    private String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
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
        System.out.println("PreviousdayDay Date  " + PreviousdayDay);
        System.out.println("PreviousdayDate Date  " + PreviousdayDate);


        return PreviousdayDate;
    }


    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }


    private void displayorderDetailsinListview(String orderStatus, List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner) {
        Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());
        sorted_OrdersList.clear();
        String TodaysDate = getDate();
        String TomorrowsDate = getTomorrowsDate();
        Log.d(Constants.TAG, "displayorderDetailsinListview TomorrowsDate: " + TomorrowsDate);

        Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);

        if (slottypefromSpinner==2){
            for (int i = 0; i < ordersList.size(); i++) {
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

                if ((orderStatus.equals(orderstatusfromOrderList))&&(slotDate.equals(TomorrowsDate))&&(slotname.equals(Constants.PREORDER_SLOTNAME))) {
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

                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                    modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    if((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210"))&&(!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {
                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                    }

                }

            }
        }

    else if(slottypefromSpinner==1){
            for (int i = 0; i < ordersList.size(); i++) {
                Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                String slotDate = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotdate());
                String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();


                Log.d(Constants.TAG, "displayorderDetailsinListview TodaysDate: " + TodaysDate);

                Log.d(Constants.TAG, "displayorderDetailsinListview slotDate: " + slotDate);

                Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);

                if ((orderStatus.equals(orderstatusfromOrderList))&&(slotDate.equals(TodaysDate))&&(slotname.equals(Constants.PREORDER_SLOTNAME))) {
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
                    if((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210"))&&(!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }
                }

            }
        }
    else{
            for (int i = 0; i < ordersList.size(); i++) {
                Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();

                String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);

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



                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    if((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210"))&&(!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }
                }

            }
        }


        if(sorted_OrdersList.size()>0){
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
                        return object2.getOrderconfirmedtime().compareTo(object1.getOrderconfirmedtime());
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



             showProgressBar(false);
            isnewOrdersSyncButtonClicked=false;

            manageOrdersListViewAdapter = new Adapter_Pos_ManageOrders_ListView(mContext, sorted_OrdersList,Pos_ManageOrderFragment.this, orderStatus);
            manageOrders_ListView.setAdapter(manageOrdersListViewAdapter);



        }
        else{
            showOrderInstructionText(true);
            isnewOrdersSyncButtonClicked=false;

            // showProgressBar(false);
            }



//callAdapter();
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




} /*   SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
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