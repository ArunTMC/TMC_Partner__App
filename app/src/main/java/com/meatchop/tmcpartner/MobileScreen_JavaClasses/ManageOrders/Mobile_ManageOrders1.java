package com.meatchop.tmcpartner.MobileScreen_JavaClasses.ManageOrders;

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

import android.os.Handler;
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
import com.google.gson.Gson;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;

import static android.content.Context.MODE_PRIVATE;

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
    public static String completemenuItem;
    List<Modal_ManageOrders_Pojo_Class> ordersList;

    static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    String Currenttime,FormattedTime,CurrentDate,formattedDate,CurrentDay,TodaysDate;
    List<String> slotnameChoosingSpinnerData;
    Spinner slotType_Spinner;
    int slottypefromSpinner=0;
    static Adapter_Mobile_ManageOrders_ListView1 adapterMobileManageOrdersListView;
    List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;

    private String SERVER_PATH = "wss://hx9itd7ji2.execute-api.ap-south-1.amazonaws.com/Dev";
    WebSocket webSocket;
    private Context mContext;
    BottomNavigationView bottomNavigationView;


    boolean isSearchButtonClicked = false;
    boolean isnewOrdersSyncButtonClicked = false;

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
        Log.i("SocketConnection","t   ");


            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder().url(SERVER_PATH).addHeader("VendorKey",vendorKey).build();
            Log.i("SocketConnection","  "+request.toString());
         //   webSocket = client.newWebSocket(request, new SocketListener());
            Log.i("SocketConnection","  "+webSocket.queueSize());



    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getWindow().getContext();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        deliveryPartnerList = new ArrayList<>();
        bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);
        manageOrders_ListView = view.findViewById(R.id.manageOrders_ListView);
        mobile_orderinstruction =view.findViewById(R.id.orderinstruction);
        loadingpanelmask = view.findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = view.findViewById(R.id.loadingPanel_dailyItemWisereport);
        Adjusting_Widgets_Visibility(true);

        try {
            SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", "vendor_1"));
            vendorname = (shared.getString("VendorName", ""));
            SharedPreferences shared2 = requireContext().getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
            DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));
            ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);
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

//        getOrderDetailsUsingOrderPlacedDate(TodaysDate, vendorKey, orderStatus);

        setDataForSpinner();
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
                if(slottypefromSpinner==0) {
                    Adjusting_Widgets_Visibility(true);
                    isSearchButtonClicked =false;
                    Log.d(Constants.TAG, "SearchButtonClicked : " );

                    getOrderDetailsUsingOrderPlacedDate(TodaysDate, vendorKey, orderStatus);
                }
                 if(slottypefromSpinner ==1) {
                     Adjusting_Widgets_Visibility(true);
                     isSearchButtonClicked =false;
                     Log.d(Constants.TAG, "SearchButtonClicked ");

                     String Todaysdate = getDatewithNameoftheDay();
                     String PreviousDaydate = getDatewithNameofthePreviousDay();
                    getOrderDetailsUsingOrderSlotDate(PreviousDaydate,Todaysdate, vendorKey, orderStatus);

                }
                if(slottypefromSpinner == 2 ) {
                    isSearchButtonClicked =false;

                    Adjusting_Widgets_Visibility(true);
                    Log.d(Constants.TAG, "SearchButtonClicked " );


                    String Todaysdate = getDatewithNameoftheDay();
                    String TomorrowsDate = getTomorrowsDate();
                    getOrderDetailsUsingOrderSlotDate(Todaysdate,TomorrowsDate, vendorKey, orderStatus);
                }
            }
        });


        slotType_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                slottypefromSpinner = position;
                Log.i("Spinner","position   "+position);
                if(slottypefromSpinner == 0 ){
                    Adjusting_Widgets_Visibility(true);
                    isSearchButtonClicked =false;

                    getOrderDetailsUsingOrderPlacedDate(TodaysDate,vendorKey,orderStatus);

                }
                if(slottypefromSpinner == 1 ) {
                    Adjusting_Widgets_Visibility(true);
                    String Todaysdate = getDatewithNameoftheDay();
                    isSearchButtonClicked =false;
                    String PreviousDaydate = getDatewithNameofthePreviousDay();
                    getOrderDetailsUsingOrderSlotDate(PreviousDaydate, Todaysdate, vendorKey, orderStatus);
                }
                if(slottypefromSpinner == 2 ) {
                    Adjusting_Widgets_Visibility(true);
                    String TomorrowsDate = getTomorrowsDate();
                    isSearchButtonClicked =false;
                    String Todaysdate = getDatewithNameoftheDay();
                    getOrderDetailsUsingOrderSlotDate(Todaysdate, TomorrowsDate, vendorKey, orderStatus);
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

                displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = mobile_search_barEditText.getText().toString().length();
                isSearchButtonClicked =true;

                showKeyboard(mobile_search_barEditText);
                showSearchBarEditText();
            }
        });

    }
    private void ConvertStringintoDeliveryPartnerListArray(String deliveryPersonList) {
        if ((!deliveryPersonList.equals("") )|| (!deliveryPersonList.equals(null))) {
            try {
                String ordertype = "#", orderid = "";
                //  sorted_OrdersList.clear();

                //converting jsonSTRING into array
                JSONObject jsonObject = new JSONObject(deliveryPersonList);
                JSONArray JArray = jsonObject.getJSONArray("content");
                Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                int i1 = 0;
                int arrayLength = JArray.length();
                Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                for (; i1 < (arrayLength); i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        AssignDeliveryPartner_PojoClass assignDeliveryPartner_pojoClass = new AssignDeliveryPartner_PojoClass();
                        assignDeliveryPartner_pojoClass.deliveryPartnerStatus = String.valueOf(json.get("status"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerKey = String.valueOf(json.get("key"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerMobileNo = String.valueOf(json.get("mobileno"));
                        assignDeliveryPartner_pojoClass.deliveryPartnerName = String.valueOf(json.get("name"));

                        // Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
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
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                isnewOrdersSyncButtonClicked=false;

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


    private void setDataForSpinner() {
        slotnameChoosingSpinnerData.clear();
        String TodaysDate = getDatewithNameoftheDay();
        TodaysDate = "Preorder -Today's Date : "+TodaysDate;
        String TomorrowsDate = getTomorrowsDate();
        TomorrowsDate = "Preorder-Tomorrow's Date : "+TomorrowsDate;

        slotnameChoosingSpinnerData.add("Preorder and Express Delivery");
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


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = (shared.getString("VendorKey", "vendor_1"));

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
                InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
    }






    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG,"Manage_Orders fragment Destroyed");
    }

    private void getOrderDetailsUsingOrderPlacedDate(String date, String vendorKey, String selectedStatus) {
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
                        mobile_jsonString =response.toString();
                       convertingJsonStringintoArray(selectedStatus, mobile_jsonString);

                      //  adapter = new Adapter_AutoCompleteManageOrdersItem(mContext, mobile_jsonString);


                    //    mobile_search_barEditText.setAdapter(adapter);

                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(mContext,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                isnewOrdersSyncButtonClicked=false;

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
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //receive your data here
            Log.v(Constants.TAG, "BroadCastSucess");
            sorted_OrdersList.clear();
            ordersList.clear();
            mobile_jsonString = intent.getStringExtra("response");
            convertingJsonStringintoArray(orderStatus, mobile_jsonString);

            Log.v(Constants.TAG, "BroadCastSucess"+ mobile_jsonString);
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
        try {
            String ordertype="#";

            ordersList.clear();
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
                    if(json.has("notes")){
                        manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                    }
                    else{
                        manageOrdersPojoClass.notes ="";
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
            displayorderDetailsinListview(orderStatus,ordersList, slottypefromSpinner);


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
        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay+", "+CurrentDate;



        System.out.println("todays Date  " + CurrentDate);



        return CurrentDate;
    }





    private String getDate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        System.out.println("Current  " + CurrentDate);


        return CurrentDate;
    }


    private void displayorderDetailsinListview(String orderStatus, List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner) {
        Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.size());
        sorted_OrdersList.clear();
        String TodaysDate = getDatewithNameoftheDay();
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
                    modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                    modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                    modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();

                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                    modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                    modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                    modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();


                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
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
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();
                    modal_manageOrders_forOrderDetailList1.useraddresslat = modal_manageOrders_forOrderDetailList.getUseraddresslat();
                    modal_manageOrders_forOrderDetailList1.useraddresslon = modal_manageOrders_forOrderDetailList.getUseraddresslon();
                    modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                    modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();


                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
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
                String orderstatusfromOrderList = modal_manageOrders_forOrderDetailList.getOrderstatus().toUpperCase();
                Log.d(Constants.TAG, "displayorderDetailsinListview orderStatus: " + orderStatus);
                Log.d(Constants.TAG, "displayorderDetailsinListview orderidfromOrderList: " + orderstatusfromOrderList);
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

                    modal_manageOrders_forOrderDetailList1.useraddress = modal_manageOrders_forOrderDetailList.getUseraddress();

                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    if((!modal_manageOrders_forOrderDetailList.getUsermobile().equals("9876543210"))&&(!modal_manageOrders_forOrderDetailList.getUsermobile().equals("+919876543210"))) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }
                }

            }
        }

try {
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

        isnewOrdersSyncButtonClicked=false;

        adapterMobileManageOrdersListView = new Adapter_Mobile_ManageOrders_ListView1(mContext, sorted_OrdersList, Mobile_ManageOrders1.this, orderStatus);
        manageOrders_ListView.setAdapter(adapterMobileManageOrdersListView);

        loadingpanelmask.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.GONE);
        manageOrders_ListView.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        mobile_orderinstruction.setVisibility(View.GONE);


    } else {
        loadingpanelmask.setVisibility(View.GONE);
        loadingPanel.setVisibility(View.GONE);
        manageOrders_ListView.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        isnewOrdersSyncButtonClicked=false;

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

    }
}
catch (Exception e){
    isnewOrdersSyncButtonClicked=false;

    e.printStackTrace();
}


//callAdapter();
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
                Log.i("result","t   "+text);
                JSONArray array = null;
                try {
                    array = new JSONArray(text);
                    Log.i(" array.length()", String.valueOf(array.length()));

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
                    Log.e("exception ::: ", stringWriter.toString());
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
                        Log.i(Constants.TAG,orderid_from_ordersList+" this order is removed ");

                    }
                    else{
                        Log.i(Constants.TAG,orderid_from_ordersList+" this order is not repeated ");
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
                Log.i(Constants.TAG,"Status of this  Order is "+modal_manageOrders_pojo_class.getOrderstatus());
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