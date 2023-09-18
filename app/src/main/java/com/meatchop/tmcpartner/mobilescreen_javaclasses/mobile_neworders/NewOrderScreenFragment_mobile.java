package com.meatchop.tmcpartner.mobilescreen_javaclasses.mobile_neworders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Add_CustomerOrder_TrackingTableInterface;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Add_CustomerOrder_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.other_classes.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.orderplacing_microservice_architectures.OrderPlacingModuleHandler_AsyncTask;
import com.meatchop.tmcpartner.orderplacing_microservice_architectures.OrderPlacingModuleHandler_Interface;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_WholeSaleCustomers;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.DeviceListActivity;
import com.meatchop.tmcpartner.settings.Modal_Address;
import com.meatchop.tmcpartner.settings.Modal_DeliverySlabDetails;
import com.meatchop.tmcpartner.settings.Modal_MenuItemStockAvlDetails;
import com.meatchop.tmcpartner.settings.ReportListviewSizeHelper;
import com.meatchop.tmcpartner.settings.ScreenSizeOfTheDevice;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.meatchop.tmcpartner.Constants.TAG;
import static com.meatchop.tmcpartner.Constants.api_Update_MenuItemStockAvlDetails;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrderScreenFragment_mobile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrderScreenFragment_mobile extends Fragment {
    Context mContext;
    private RecyclerView recyclerView;
    public static List<Modal_NewOrderItems> menuItem;
    public static List<Modal_NewOrderItems> completemenuItem;
    String Currenttime,MenuItems;
    TextView mobile_ItemTotal_textwidget,mobile_GST_textwidget,mobile_ToPay_textwidget;
    Button mobile_checkout_button;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0,totalAmounttopay=0;
    int new_totalAmount_withGst,new_totalAmount_withoutGst=0,newGst=0;
    String finaltoPayAmount="",discountAmount="0";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static LinearLayout loadingPanel;
    static LinearLayout loadingpanelmask;

    public static HashMap<String,Modal_NewOrderItems> cartItem_hashmap = new HashMap();
    public static List<String> cart_Item_List;

    static Adapter_NewOrderScreenFragment_Mobile adapterNewOrderScreenFragmentMobile ;
    String FormattedTime,CurrentDate,formattedDate,CurrentDay;
    String vendorKey="",usermobileNo ="",vendorType="",vendorName ="";
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String selectedPaymentMode  ="NONE SELECTED";
    String selectedOrderType  = Constants.POSORDER;

    public  BottomSheetDialog bottomSheetDialog;

    BottomNavigationView bottomNavigationView;
    boolean isUpdateCouponTransactionMethodCalled=false;
    private  boolean isOrderDetailsMethodCalled =false;
    boolean isOrderPlacedinOrderdetails=false;
    private  boolean isOrderTrackingDetailsMethodCalled =false;
    private  boolean isPaymentDetailsMethodCalled =false;
    boolean isMobileAppDataFetchedinDashboard=false;
    boolean isanyProducthaveZeroAsweight=false ,isanyProducthaveZeroAsPrice = false;
    boolean isUpdateRedeemPointsWithoutKeyMethodCalled =false;
    boolean ispaymentMode_Clicked =false;
    boolean isPrintedSecondTime=false;
    static boolean isPhoneOrderSelected=false;
    double screenInches;
    double totalredeempointsusergetfromorder=0,pointsfor100rs_double=0;
    String ordertype="",customermobileno="",customerName ="",maxpointsinaday_String="",minordervalueforredeem_String="",pointsfor100rs_String="";
    double maxpointsinaday_double,minordervalueforredeem_double,finalamounttoPay;
    List<Modal_MenuItemStockAvlDetails> MenuItemStockAvlDetails=new ArrayList<>();
    List<Modal_MenuItem> MenuItemArray=new ArrayList<>();

    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    private BluetoothPrintDriver mChatService = null;

    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

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
    long sTime =0;
    String finaltoPayAmountinmethod="";
    String mConnectedDeviceName ,defaultPrinterType ="";
    boolean isPrinterCnnected = false;
    public boolean shouldGetPrintNow_Global = false;
    String printerName = "";
    String printerStatus= "";
    boolean isPrinterCnnectedfromSP = false;
    String printerNamefromSP = "";
    boolean isinventorycheck = false;

    private  boolean isStockOutGoingAlreadyCalledForthisItem =false;
    public static List<String> StockBalanceChangedForThisItemList = new ArrayList<>();


    double totalamountUserHaveAsCredit =0;
    public  boolean isAddOrUpdateCreditOrderDetailsIsCalled = false;
    List<Modal_WholeSaleCustomers> wholeSaleCustomersArrayList=new ArrayList<>();
    Adapter_AutoCompleteWholeSaleCustomers_Mobile adapter_autoCompleteWholeSaleCustomers;
    HashMap<String,String>wholeSaleCustomersMobileNoStringHashmap = new HashMap<>();

    boolean orderdetailsnewschema = false , localDBcheck =false , isStoreNumberSelected = false;

    Add_CustomerOrder_TrackingTableInterface mResultCallback_Add_CustomerOrder_TrackingTableInterface = null;
    boolean  isCustomerOrdersTableServiceCalled = false;

    public  BottomSheetDialog addressBottomSheet;
    List<Modal_Address> userAddressArrayList=new ArrayList<>();
    List<String> userAddressKeyArrayList =new ArrayList<>();
    boolean isAddressForPhoneOrderSelected = false;
    boolean isNewUser = false;
    boolean isAddress_Added_ForUser = false;
    boolean isUsertype_AlreadyPhone = false;
    boolean updateUserName = false;
    boolean userFetchedManually = false;

    String user_key_toAdd_Address = "";
    ListView address_listView;
    TextView id_addressInstruction;
    LinearLayout loadingpanelmask_addressbottomSheet ;
    LinearLayout loadingPanel_addressbottomSheet ;
    LinearLayout selectfromAddressList_ParentLayout_bottomSheet;
    LinearLayout addNewAddress_widget;
    LinearLayout addNewAddress_ParentLayout_bottomSheet;
    Adapter_AddressList adapter_addressList ;
    TextView fulladdress_textview,deliveryChargeTextWidget;
    ScrollView scrollView;
    AutoCompleteTextView autoCompleteCustomerName_widget;
    String selectedAddress = "",selectedAddressKey = "";
    String userLatitude = "0", userLongitude = "0",deliveryDistance = "0";
    Modal_Address selected_Address_modal = new Modal_Address();


    public List<Modal_DeliverySlabDetails> deliverySlabDetailsArrayList=new ArrayList<>();
    public double maxi_deliverableDistance_inSlabDetails =0;
    public double deliveryAmt_fromMaxiDistance_inSlabDetails =0;
    public String deliveryAmount_for_this_order ="0";
    TextView  toPay_textWidget;
    String tokenNo="00",userStatus ="";
    String applieddiscountpercentage = "0", appMarkupPercentage = "0";
    Spinner orderTypeSpinner_newWidget;
    boolean isCalledFromOrderTypeSpinner = false;

    private static final int REQUEST_CAMERA_PERMISSION = 201;
    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;


    boolean  isOrderPlacingMicroserviceisActive = false;
    OrderPlacingModuleHandler_Interface orderPlacingModuleHandler_interface = null;
    OrderPlacingModuleHandler_AsyncTask orderPlacingModuleHandler_asyncTask = null;
    JSONObject redeemPointsJson = new JSONObject();
    
    



    public NewOrderScreenFragment_mobile() {
        // Required empty public constructor
    }



    public static NewOrderScreenFragment_mobile newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("menuItem", data);

        NewOrderScreenFragment_mobile fragment = new NewOrderScreenFragment_mobile();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getWindow().getContext();
        cart_Item_List = new ArrayList<>();
        completemenuItem = new ArrayList<>();
        cart_Item_List.clear();
        cartItem_hashmap.clear();
        userFetchedManually = false;
        isAddress_Added_ForUser = false;
        isAddressForPhoneOrderSelected = false;
        isNewUser =false;
        updateUserName = false;
        isStoreNumberSelected = false;
        isUsertype_AlreadyPhone =false;
        customermobileno = "";
        customerName ="";
        deliveryDistance ="";
        user_key_toAdd_Address = "";
        userAddressKeyArrayList.clear();
        userAddressArrayList.clear();
        redeemPointsJson = new JSONObject();
        redeemPointsJson = new JSONObject();
        selected_Address_modal = new Modal_Address();
        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(getActivity());
            //Toast.makeText(getActivity(), "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
                //  Toast.makeText(getActivity(), "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }


            try{
            SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = shared.getString("VendorKey","");
            vendorType = shared.getString("VendorType","");
             vendorName = shared.getString("VendorName", "");
            usermobileNo = (shared.getString("UserPhoneNumber", "+91"));
            isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
            isOrderPlacingMicroserviceisActive = (shared.getBoolean("enableorderplacingmicroservice", false));

                //   isOrderPlacingMicroserviceisActive= false;
             localDBcheck = (shared.getBoolean("localdbcheck", false));

                SharedPreferences printerDatasharedPreferences
                        = requireContext().getSharedPreferences("PrinterConnectionData",
                        MODE_PRIVATE);

                defaultPrinterType = printerDatasharedPreferences.getString("printerType",String.valueOf(Constants.Bluetooth_PrinterType));

            //orderdetailsnewschema = true;

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    public String getData() {


        return requireArguments().getString("menuItem");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_new_order_screen_mobile, container, false);
        rootView.setTag("RecyclerViewFragment");
        mobile_ItemTotal_textwidget = rootView.findViewById(R.id.mobile_ItemTotal_textwidget);
        mobile_GST_textwidget = rootView.findViewById(R.id.mobile_GST_textwidget);
        mobile_ToPay_textwidget = rootView.findViewById(R.id.mobile_ToPay_textwidget);
        mobile_checkout_button = rootView.findViewById(R.id.mobile_checkout_button);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        loadingpanelmask = rootView.findViewById(R.id.loadingpanelmask);
        loadingPanel = rootView.findViewById(R.id.loadingPanel);
        orderTypeSpinner_newWidget = rootView.findViewById(R.id.orderTypeSpinner_newWidget);


        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
             //   Log.i(TAG, "scanner if " + getData());


            } else {
                ActivityCompat.requestPermissions(getActivity(), new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
              //  Log.i(TAG, "scanner else " + getData());


            }

        } catch (Exception e) {
         //   Log.i(TAG, "scanner Exception " + getData());

            e.printStackTrace();
        }





        try{


            if(localDBcheck) {
                getDataFromSQL();
            }
            else {

                try {
                    MenuItems = getData();
                    completemenuItem = getMenuItemfromString(MenuItems);
                    getMenuItemArrayFromSharedPreferences();
                } catch (Exception e) {

                }
            }

            //Log.i(TAG, "call adapter cart_Item " + getData());



            //getMenuItemStockAvlDetailsArrayAndMenuItemFromSharedPreferences();


            if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)){
                getwholesaleCustomerlistFromSharedPref();
                if(defaultPrinterType.equals(Constants.PDF_PrinterType)){
                    AskForStoragePermission();

                }
            }
            else{
                //getDeliverySlabDetails();
            }

        }

        catch (Exception e){
            e.printStackTrace();
        }
        try {
            SharedPreferences shared = requireContext().getSharedPreferences("RedeemData", MODE_PRIVATE);
            maxpointsinaday_String = (shared.getString("maxpointsinaday", ""));
            maxpointsinaday_double = Double.parseDouble(maxpointsinaday_String);
            minordervalueforredeem_String = (shared.getString("minordervalueforredeem", ""));
            minordervalueforredeem_double = Double.parseDouble(minordervalueforredeem_String);
            pointsfor100rs_String = (shared.getString("pointsfor100rs", ""));
            pointsfor100rs_double = Double.parseDouble(pointsfor100rs_String);
            isMobileAppDataFetchedinDashboard = (shared.getBoolean("fetchedindashboard", false));
        }
        catch (Exception e){
            e.printStackTrace();
        }

            bottomNavigationView = ((MobileScreen_Dashboard) requireActivity()).findViewById(R.id.bottomnav);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                   if (!recyclerView.canScrollVertically(-1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                            bottomNavigationView.setVisibility(View.VISIBLE);

                    }
                    else if(newState==RecyclerView.SCROLL_STATE_DRAGGING) {
                        if (bottomNavigationView.getVisibility() ==  View.GONE){
                      //  Toast.makeText(mContext, "Swipe downwards to make the Settings Button Visible", Toast.LENGTH_SHORT).show();
                    }
                    }

                    else {
                        bottomNavigationView.setVisibility(View.GONE);
                           Toast.makeText(mContext, "Swipe downwards to make the Settings Button Visible", Toast.LENGTH_SHORT).show();

                    }


                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!recyclerView.canScrollVertically(-1)) {
                       // onScrolledToTop();
                      //  bottomNavigationView.setVisibility(View.VISIBLE);

                    } else if (!recyclerView.canScrollVertically(1)) {
                    //    onScrolledToBottom();
                       // bottomNavigationView.setVisibility(View.GONE);

                    } else if (dy < 0) {
                      //  onScrolledUp();
                      //  Toast.makeText(mContext, "dy : "+String.valueOf(dy), Toast.LENGTH_SHORT).show();
                       // bottomNavigationView.setVisibility(View.GONE);

                    } else if (dy > 0) {
                      //  onScrolledDown();
                      //  Toast.makeText(mContext, "dy : "+String.valueOf(dy), Toast.LENGTH_SHORT).show();

                        //bottomNavigationView.setVisibility(View.VISIBLE);

                    }
                    else if (dy == 0) {
                        //  onScrolledDown();
                      //  Toast.makeText(mContext, "dy : "+String.valueOf(dy), Toast.LENGTH_SHORT).show();


                    }
                }


            });



           /* recyclerView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollY = recyclerView.getScrollX();
                    Toast.makeText(mContext,"int y "+String.valueOf(scrollY),Toast.LENGTH_SHORT).show();
                    //for verticalScrollView
                    if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                        if (screenInches < Constants.default_mobileScreenSize) {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    } else {
                      Toast.makeText(mContext,"Swipe down to make the Settings Button Visible",Toast.LENGTH_SHORT).show();
                        bottomNavigationView.setVisibility(View.GONE);

                    }

                }
            });

            */

        String[] ordertype = new String[0];

        try{
            if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)) {
                ordertype =  getResources().getStringArray(R.array.B2BOrderType);
            }
            else{
                ordertype =  getResources().getStringArray(R.array.OrderType);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> arrayAdapterordertype = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ordertype);
        arrayAdapterordertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Objects.requireNonNull(orderTypeSpinner_newWidget).setAdapter(arrayAdapterordertype);


        Objects.requireNonNull(orderTypeSpinner_newWidget).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedOrderType = parent.getItemAtPosition(position).toString().toUpperCase();
                isCalledFromOrderTypeSpinner = true;
                turnoffProgressBarAndResetArray();
                if(selectedOrderType.equals(Constants.PhoneOrder)){
                    if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)) {
                       // displaySelectedAddress_parentLayout.setVisibility(View.GONE);

                        isPhoneOrderSelected = false;
                        isAddressForPhoneOrderSelected = false;
                        selectedOrderType =  Constants.POSORDER;
                        orderTypeSpinner_newWidget.setSelection(0);
                    }
                    else{

                        if(deliverySlabDetailsArrayList.size()==0){
                            getDeliverySlabDetails();
                            isPhoneOrderSelected = true;
                            selectedOrderType =  Constants.PhoneOrder;

                        }
                        else{
                            isPhoneOrderSelected = true;
                            selectedOrderType =  Constants.PhoneOrder;

                        }

                    }

                }
                else{

                    isPhoneOrderSelected = false;
                    isAddressForPhoneOrderSelected = false;
                    selectedOrderType =  Constants.POSORDER;

                }
                //  Toast.makeText(parent.getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                isPhoneOrderSelected = false;
                isAddressForPhoneOrderSelected = false;

                selectedOrderType =  Constants.POSORDER;
            }
        });



        createEmptyRowInListView("empty");

        CallAdapter();
        try{
            if(maxpointsinaday_double==0||minordervalueforredeem_double==0||pointsfor100rs_double==0||(!isMobileAppDataFetchedinDashboard)){


                showProgressBar(true);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetPOSMobileAppData, null,
                        new com.android.volley.Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(@NonNull JSONObject response) {


                                try {

                                    Log.d(TAG, " response: " + response);
                                    try {
                                        String jsonString =response.toString();
                                        Log.d(TAG, " response: onMobileAppData " + response);
                                        JSONObject jsonObject = new JSONObject(jsonString);
                                        JSONArray JArray  = jsonObject.getJSONArray("content");
                                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                        int i1=0;
                                        int arrayLength = JArray.length();
                                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                                        for(;i1<(arrayLength);i1++) {

                                            try {
                                                JSONObject json = JArray.getJSONObject(i1);

                                                JSONArray array  = json.getJSONArray("redeemdata ");

                                                for(int i=0; i < array.length(); i++) {
                                                    JSONObject redeemdata_json = array.getJSONObject(i);
                                                    maxpointsinaday_String = redeemdata_json.getString("maxpointsinaday");
                                                    minordervalueforredeem_String= redeemdata_json.getString("minordervalueforredeem");
                                                    pointsfor100rs_String = redeemdata_json.getString("pointsfor100rs");
                                                    Log.d("Constants.TAG", "maxpointsinaday Response: " + maxpointsinaday_String);
                                                    Log.d("Constants.TAG", "minordervalueforredeem Response: " + minordervalueforredeem_String);
                                                    Log.d("Constants.TAG", "pointsfor100rs Response: " + pointsfor100rs_String);


                                                    try {
                                                        maxpointsinaday_double = Double.parseDouble(maxpointsinaday_String);
                                                        minordervalueforredeem_double = Double.parseDouble(minordervalueforredeem_String);
                                                        pointsfor100rs_double = Double.parseDouble(pointsfor100rs_String);
                                                        Toast.makeText(mContext,"Can get  Details", Toast.LENGTH_LONG).show();

                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                        showProgressBar(false);
                                                        Toast.makeText(mContext,"Can't get RedeemPoints Details", Toast.LENGTH_LONG).show();
                                                    }

                                                    showProgressBar(false);

                                                    saveredeemDetailsinSharePreferences(maxpointsinaday_String,minordervalueforredeem_String,pointsfor100rs_String);

                                                }
                                            } catch (Exception e) {
                                                showProgressBar(false);
                                                Toast.makeText(mContext,"Can't get RedeemPoints Details", Toast.LENGTH_LONG).show();

                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (Exception e) {
                                        showProgressBar(false);
                                        Toast.makeText(mContext,"Can't get RedeemPoints Details", Toast.LENGTH_LONG).show();

                                         e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    showProgressBar(false);
                                    Toast.makeText(mContext,"Can't get RedeemPoints Details", Toast.LENGTH_LONG).show();

                                    e.printStackTrace();
                                }


                            }

                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(@NonNull VolleyError error) {
                        Log.d(TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                        Log.d(TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                        Log.d(TAG, "getDeliveryPartnerList Error: " + error.toString());
                        showProgressBar(false);
                        Toast.makeText(mContext,"Can't get RedeemPoints Details", Toast.LENGTH_LONG).show();

                        error.printStackTrace();
                    }
                }) {
                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        final Map<String, String> params = new HashMap<>();
                        params.put("modulename", "Mobile");
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
        }
        catch (Exception e){
            e.printStackTrace();
        }
        mobile_checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Bundle b = new Bundle();

                b.putSerializable("cart_Item_List", (Serializable) cart_Item_List);
                b.putSerializable("cartItem_hashmap", (Serializable) cartItem_hashmap);

                Intent   intent = new Intent (mContext,CartActivity_Mobile_NewOrdersScreen.class);
                intent.putExtras(b);
                startActivity(intent);

                 */
                JSONArray itemDespArray = new JSONArray();
             //   GeneratePDF("orderplacedTime", "UserMobile", "tokenno", "itemTotalwithoutGst", "taxAmount", finaltoPayAmountinmethod, "orderid", cart_Item_List, cartItem_hashmap, "Payment_mode", discountAmount, ordertype, itemDespArray);


                if(defaultPrinterType.equals(Constants.PDF_PrinterType)){
                    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);
                    //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                    // If do not grant write external storage permission.
                    if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                        AskForStoragePermission();

                    }
                    else{
                        if(isanyProducthaveZeroAsweight){
                            AlertDialogClass.showDialog(getActivity(), R.string.Orders_weight_Cant_be_leave_unedited);

                        }
                        else {
                            if ((!Objects.requireNonNull(mobile_ItemTotal_textwidget).getText().toString().equals("0")) && (!Objects.requireNonNull(mobile_ToPay_textwidget).getText().toString().equals("0")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("0.0")) && (!mobile_ToPay_textwidget.getText().toString().equals("0.0")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("0.00")) && (!mobile_ToPay_textwidget.getText().toString().equals("0.00")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("")) && (!mobile_ToPay_textwidget.getText().toString().equals(""))) {
                                if (checkforBarcodeInCart("empty")) {
                                    NewOrderScreenFragment_mobile.cart_Item_List.remove("empty");

                                    NewOrderScreenFragment_mobile.cartItem_hashmap.remove("empty");
                                    CallAdapter();

                                }

                                openBottomSheetToCompleteBilling();
                            } else {
                                AlertDialogClass.showDialog(getActivity(), R.string.Cart_is_empty);

                            }
                        }
                    }
                }
                else{
                    if(isanyProducthaveZeroAsweight){
                        AlertDialogClass.showDialog(getActivity(), R.string.Orders_weight_Cant_be_leave_unedited);

                    }
                    else {
                        if ((!Objects.requireNonNull(mobile_ItemTotal_textwidget).getText().toString().equals("0")) && (!Objects.requireNonNull(mobile_ToPay_textwidget).getText().toString().equals("0")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("0.0")) && (!mobile_ToPay_textwidget.getText().toString().equals("0.0")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("0.00")) && (!mobile_ToPay_textwidget.getText().toString().equals("0.00")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("")) && (!mobile_ToPay_textwidget.getText().toString().equals(""))) {
                            if (checkforBarcodeInCart("empty")) {
                                NewOrderScreenFragment_mobile.cart_Item_List.remove("empty");

                                NewOrderScreenFragment_mobile.cartItem_hashmap.remove("empty");
                                CallAdapter();

                            }

                            openBottomSheetToCompleteBilling();
                        } else {
                            AlertDialogClass.showDialog(getActivity(), R.string.Cart_is_empty);

                        }
                    }
                }


            }
        });




        return rootView;
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
            completemenuItem.clear();
            try {
                // if (cursor.moveToFirst()) {

                Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {
                            Modal_NewOrderItems modal_newOrderItems = new Modal_NewOrderItems();
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
                                MenuItemStockAvlDetails.add(modal_menuItemStockAvlDetails);




                            }
                            else{

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

                            completemenuItem.add(modal_newOrderItems);
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
        finally {
            try {
                if (tmcMenuItemSQL_db_manager != null) {
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }



    }


    private void getDeliverySlabDetails() {
        showProgressBar(true);
        deliverySlabDetailsArrayList.clear();
        maxi_deliverableDistance_inSlabDetails =0;
        final double[] localDeliverableDistance = {0};
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetDeliverySlotSlabDetails + vendorKey,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        try {
                            showProgressBar(false);
                            JSONArray JArray = response.getJSONArray("content");
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            if (arrayLength < 1) {
                                Toast.makeText(mContext, "There is no Delivery slab details for this vendor" +
                                        "", Toast.LENGTH_LONG).show();


                            }

                            for (; i1 < (arrayLength); i1++) {
                                Modal_DeliverySlabDetails modal_deliverySlabDetails = new Modal_DeliverySlabDetails();
                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    try{
                                        modal_deliverySlabDetails.setKey(String.valueOf(json.getString("key")));
                                    }
                                    catch (Exception e){

                                        modal_deliverySlabDetails.setKey(String.valueOf(""));
                                        e.printStackTrace();
                                    }

                                    try{
                                        modal_deliverySlabDetails.setDeliverycharge(String.valueOf(json.getString("deliverycharge")));

                                    }
                                    catch (Exception e){
                                        modal_deliverySlabDetails.setDeliverycharge(String.valueOf("0"));
                                        e.printStackTrace();
                                    }

                                    try{
                                        modal_deliverySlabDetails.setMaxkms(String.valueOf(json.getString("maxkms")));

                                    }
                                    catch (Exception e){
                                        modal_deliverySlabDetails.setMaxkms(String.valueOf(""));
                                        e.printStackTrace();
                                    }

                                    try{
                                        localDeliverableDistance[0] = Double.valueOf(String.valueOf(json.getString("maxkms")));
                                    }
                                    catch (Exception e ){
                                        localDeliverableDistance[0] = 0;
                                        e.printStackTrace();
                                    }

                                    try{
                                        if(localDeliverableDistance[0] >=maxi_deliverableDistance_inSlabDetails){
                                            maxi_deliverableDistance_inSlabDetails = localDeliverableDistance[0];
                                            deliveryAmt_fromMaxiDistance_inSlabDetails = Double.valueOf(String.valueOf(json.getString("deliverycharge")));
                                        }
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    try{
                                        modal_deliverySlabDetails.setMinkms(String.valueOf(json.getString("minkms")));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        modal_deliverySlabDetails.setMinkms(String.valueOf(""));
                                    }





                                    try{
                                        modal_deliverySlabDetails.setVendorkey(String.valueOf(json.getString("vendorkey")));

                                    }
                                    catch (Exception e){
                                        modal_deliverySlabDetails.setVendorkey(String.valueOf(""));
                                        e.printStackTrace();
                                    }




                                    deliverySlabDetailsArrayList.add(modal_deliverySlabDetails);



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Toast.makeText(mContext, "in address" , Toast.LENGTH_LONG).show();

                                try {
                                    if(arrayLength - i1 == 1){


                                        showProgressBar(false);
                                    }
                                }

                                catch (Exception e){
                                    showProgressBar(false);
                                    e.printStackTrace();

                                }


                            }

                        } catch (JSONException e) {
                            showProgressBar(false);
                            e.printStackTrace();

                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(mContext, "Error in Delivery slab ", Toast.LENGTH_LONG).show();

                    showProgressBar(false);


                    //Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

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



    private void getwholesaleCustomerlistFromSharedPref() {

        if(vendorType.equals(Constants.WholeSales_VendorType)){
            wholeSaleCustomersArrayList.clear();
            final SharedPreferences sharedPreferencesMenuitem = mContext.getSharedPreferences("WholeSaleCustomerDetails", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = sharedPreferencesMenuitem.getString("WholeSaleCustomerDetails", "");
            if (json.isEmpty()) {
                Toast.makeText( mContext.getApplicationContext(),"There is something error",Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<Modal_WholeSaleCustomers>>() {
                }.getType();
                wholeSaleCustomersArrayList  = gson.fromJson(json, type);
            }

            for(int i =0 ;i< wholeSaleCustomersArrayList.size(); i ++) {
                Modal_WholeSaleCustomers modal_wholeSaleCustomers = wholeSaleCustomersArrayList.get(i);
                String mobileno = "",customerName = "";
                mobileno = String.valueOf(modal_wholeSaleCustomers.getMobileno());
                customerName = String.valueOf(modal_wholeSaleCustomers.getCustomerName());
                if(!customerName.equals("")) {
                    if (!wholeSaleCustomersMobileNoStringHashmap.containsKey(mobileno)) {
                        wholeSaleCustomersMobileNoStringHashmap.put(mobileno, customerName);

                    }
                }
            }






        }


    }


    private void getMenuItemArrayFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = requireActivity().getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuList", "");
        if (json.isEmpty()) {
            Toast.makeText(getActivity(), "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem>>() {
            }.getType();
            MenuItemArray = gson.fromJson(json, type);
        }

    }



    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: NewOrderScreenFragment_mobile.cart_Item_List) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }

    private void openBottomSheetToCompleteBilling() {

        EditText customermobileno_editwidget = bottomSheetDialog.findViewById(R.id.customermobileno_editwidget);
        EditText discount_editWidget = bottomSheetDialog.findViewById(R.id.discount_editWidget);
        CheckBox userstoreNumberCheckboxWidget = bottomSheetDialog.findViewById(R.id.userstoreNumberCheckboxWidget);
        Button apply_discount_buttonWidget = bottomSheetDialog.findViewById(R.id.apply_discount_buttonWidget);
        Button  checkout_button_Widget = bottomSheetDialog.findViewById(R.id.checkout_button_Widget);
        TextView itemtotal_textWidget = bottomSheetDialog.findViewById(R.id.itemtotal_textWidget);
        TextView discountTextWidget = bottomSheetDialog.findViewById(R.id.discountTextWidget);
         toPay_textWidget = bottomSheetDialog.findViewById(R.id.toPay_textWidget);
        Spinner paymentModeSpinner_Widget = bottomSheetDialog.findViewById(R.id.paymentModeSpinner_Widget);
        deliveryChargeTextWidget = bottomSheetDialog.findViewById(R.id.deliveryChargeTextWidget);
        Spinner orderTypeSpinner_Widget = bottomSheetDialog.findViewById(R.id.orderTypeSpinner_Widget);
        Button selectAddress_buttonWidget = bottomSheetDialog.findViewById(R.id.selectAddress_buttonWidget);


        Button fetchUser_buttonWidget = bottomSheetDialog.findViewById(R.id.fetchUser_buttonWidget);

        CheckBox updateUserName_widget = bottomSheetDialog.findViewById(R.id.updateUserName_widget);

         LinearLayout customerName_layout = bottomSheetDialog.findViewById(R.id.customerName_layout);
        LinearLayout displaySelectedAddress_parentLayout = bottomSheetDialog.findViewById(R.id.displaySelectedAddress_parentLayout);
        LinearLayout  addAddress_close_bottom_sheet = addressBottomSheet.findViewById(R.id.addAddress_close_bottom_sheet);
        LinearLayout close_addressbottom_sheet = addressBottomSheet.findViewById(R.id.close_bottom_sheet);

        LinearLayout orderMode_overlapLayout = bottomSheetDialog.findViewById(R.id.orderMode_overlapLayout);

        //loadingpanelmask_addressbottomSheet = addressBottomSheet.findViewById(R.id.loadingpanelmask);
         //loadingPanel_addressbottomSheet = addressBottomSheet.findViewById(R.id.loadingPanel);
       // address_listView = addressBottomSheet.findViewById(R.id.address_listView);
        ImageView swipe_up_arrow = addressBottomSheet.findViewById(R.id.swipe_up_arrow);
        ImageView swipe_down_arrow = addressBottomSheet.findViewById(R.id.swipe_down_arrow);

        Button saveAddress_Button = addressBottomSheet.findViewById(R.id.saveAddress_Button);
        EditText contact_personName_editText = addressBottomSheet.findViewById(R.id.contact_personName_editText);
        EditText contact_personMobileNo_editText = addressBottomSheet.findViewById(R.id.contact_personMobileNo_editText);
        EditText addressLine1_editText = addressBottomSheet.findViewById(R.id.addressLine1_editText);
        EditText addressLine2_editText = addressBottomSheet.findViewById(R.id.addressLine2_editText);
        EditText landmark_editText = addressBottomSheet.findViewById(R.id.landmark_editText);
        EditText pincode_editText = addressBottomSheet.findViewById(R.id.pincode_editText);
        EditText address_type_editText = addressBottomSheet.findViewById(R.id.type_address_editText);
        EditText deliveryDistance_address_editText = addressBottomSheet.findViewById(R.id.deliveryDistance_address_editText);


        ScrollView scrollView = addressBottomSheet.findViewById(R.id.scrollView);
        scrollView.setSmoothScrollingEnabled(true);


        displaySelectedAddress_parentLayout.setVisibility(View.GONE);
        addNewAddress_ParentLayout_bottomSheet.setVisibility(View.GONE);
        selectfromAddressList_ParentLayout_bottomSheet.setVisibility(View.VISIBLE);
        orderMode_overlapLayout.setVisibility(View.VISIBLE);
        orderMode_overlapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




        String itemTotal = "";
        try{
            itemTotal = mobile_ItemTotal_textwidget.getText().toString();
        }
        catch (Exception e){
            itemTotal = "";
            e.printStackTrace();
        }

        String[] paymentType=getResources().getStringArray(R.array.PaymentMode);
        String[] ordertype = new String[0];

        try{
            if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)) {
                ordertype =  getResources().getStringArray(R.array.B2BOrderType);
            }
            else{
                ordertype =  getResources().getStringArray(R.array.OrderType);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ArrayAdapter<String> arrayAdapterpaymentType = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, paymentType);
        arrayAdapterpaymentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Objects.requireNonNull(paymentModeSpinner_Widget).setAdapter(arrayAdapterpaymentType);

        ArrayAdapter<String> arrayAdapterordertype = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ordertype);
        arrayAdapterordertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Objects.requireNonNull(orderTypeSpinner_Widget).setAdapter(arrayAdapterordertype);

        discountAmount ="0";
        deliveryAmount_for_this_order ="0";
        customermobileno="";
        customerName ="";
        userAddressArrayList.clear();
        redeemPointsJson = new JSONObject();
        userAddressKeyArrayList.clear();
        selectedAddressKey = String.valueOf("");
        selectedAddress = String.valueOf("");
        userLatitude = String.valueOf("0");
        userLongitude = String.valueOf("0");
        deliveryDistance ="";
        user_key_toAdd_Address ="";

        fulladdress_textview.setText("");
        customermobileno_editwidget.setText("");
        autoCompleteCustomerName_widget.setText("");
        userstoreNumberCheckboxWidget.setChecked(false);
        discount_editWidget.setText("0");

        selected_Address_modal = new Modal_Address();
        isPhoneOrderSelected = false;
        isAddressForPhoneOrderSelected = false;
        isAddressForPhoneOrderSelected = false;
        updateUserName = false;
        isNewUser = false;
        isAddress_Added_ForUser = false;
        isAddressForPhoneOrderSelected = false;
        isUsertype_AlreadyPhone = false;
        userFetchedManually = false;
        isStoreNumberSelected = false;

        Objects.requireNonNull(itemtotal_textWidget).setText(itemTotal);
        Objects.requireNonNull(toPay_textWidget).setText(itemTotal);
        Objects.requireNonNull(discountTextWidget).setText("0");
        deliveryChargeTextWidget .setText(String.valueOf(deliveryAmount_for_this_order));
        mobile_ToPay_textwidget .setText(itemTotal);
        if(selectedOrderType.equals(Constants.PhoneOrder)){
            orderTypeSpinner_Widget.setSelection(1);
        }
        else{
            orderTypeSpinner_Widget.setSelection(0);
        }
        if(vendorType.equals(Constants.WholeSales_VendorType)){
            customerName_layout.setVisibility(View.VISIBLE);

            userstoreNumberCheckboxWidget.setVisibility(View.GONE);
            fetchUser_buttonWidget.setVisibility(View.GONE);
            updateUserName_widget.setVisibility(View.GONE);

            if(wholeSaleCustomersArrayList.size()>0){
                 adapter_autoCompleteWholeSaleCustomers = new Adapter_AutoCompleteWholeSaleCustomers_Mobile(mContext,wholeSaleCustomersArrayList, NewOrderScreenFragment_mobile.this);
                //adapter_autoCompleteWholeSaleCustomers.setHandler(newHandler());


                autoCompleteCustomerName_widget.setAdapter(adapter_autoCompleteWholeSaleCustomers);

            }

        }
        else{
            userstoreNumberCheckboxWidget.setVisibility(View.VISIBLE);
            customerName_layout.setVisibility(View.VISIBLE);
            fetchUser_buttonWidget.setVisibility(View.VISIBLE);
            updateUserName_widget.setVisibility(View.VISIBLE);

        }


        customermobileno_editwidget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String defaultStoreNumber = "";
                if(vendorKey.equals("vendor_1")){
                    defaultStoreNumber =  ("6380050384");

                }
                else if(vendorKey.equals("vendor_2")){
                    defaultStoreNumber =  ("9597580128");

                }
                else{
                    defaultStoreNumber =  StoreLanLine;

                }
                if(s.toString().equals(defaultStoreNumber) ){
                    userstoreNumberCheckboxWidget.setChecked(true);
                }
                else{
                    userstoreNumberCheckboxWidget.setChecked(false);

                }


                if(isAddressForPhoneOrderSelected){
                    deliveryAmount_for_this_order ="0";
                    isAddressForPhoneOrderSelected = false;
                    isUsertype_AlreadyPhone= false;
                    userFetchedManually = false;
                    updateUserName = false;
                    isNewUser = false;
                    selected_Address_modal = new Modal_Address();
                    fulladdress_textview.setText("Please select an Address");

                    selectedAddressKey = String.valueOf("");
                    selectedAddress = String.valueOf("");
                    userLatitude = String.valueOf("0");
                    userLongitude = String.valueOf("0");
                    deliveryDistance ="";
                    user_key_toAdd_Address ="";
                    updateUserName_widget.setChecked(false);

                    for(int i =0 ; i< userAddressArrayList.size(); i++){
                        userAddressArrayList.get(i).setAddressSelected(false);
                    }
                    add_amount_ForBillDetails();
                }
            }
        });



        fetchUser_buttonWidget .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userFetchedManually = true;
                String mobileno = String.valueOf(customermobileno_editwidget.getText().toString());
                if(mobileno.length()==10) {

                getUserDetailsUsingMobileNo(mobileno);
                }
                else
                {
                    AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

                }

            }
        });
        selectAddress_buttonWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileno = String.valueOf(customermobileno_editwidget.getText().toString());
                if(mobileno.length()==10) {

                    addNewAddress_ParentLayout_bottomSheet.setVisibility(View.GONE);
                    selectfromAddressList_ParentLayout_bottomSheet.setVisibility(View.VISIBLE);

                    addressBottomSheet.show();


                loadingPanel_addressbottomSheet.setVisibility(View.VISIBLE);
                loadingpanelmask_addressbottomSheet.setVisibility(View.VISIBLE);

                    if (customermobileno.equals(mobileno)) {
                        if (userAddressArrayList.size() > 0) {
                            setAddressListAdapter();
                            Toast.makeText(mContext, "adapter", Toast.LENGTH_SHORT).show();
                        } else {
                            customermobileno = mobileno;
                            fulladdress_textview.setText("Please select an Address");
                            userAddressArrayList.clear();
                              redeemPointsJson = new JSONObject();
                            userAddressKeyArrayList.clear();
                            selectedAddressKey = String.valueOf("");
                            selectedAddress = String.valueOf("");
                            userLatitude = String.valueOf("0");
                            userLongitude = String.valueOf("0");
                            deliveryDistance ="";
                            user_key_toAdd_Address ="";

                            selected_Address_modal = new Modal_Address();
                            isAddress_Added_ForUser = false;
                            isAddressForPhoneOrderSelected = false;


                            getUserDetailsUsingMobileNo(mobileno);

                        }

                    } else {
                        customermobileno = mobileno;
                        fulladdress_textview.setText("Please select an Address");
                        userAddressArrayList.clear();
                       redeemPointsJson = new JSONObject();
                        userAddressKeyArrayList.clear();
                        selectedAddressKey = String.valueOf("");
                        selectedAddress = String.valueOf("");
                        userLatitude = String.valueOf("0");
                        userLongitude = String.valueOf("0");
                        deliveryDistance ="";
                        user_key_toAdd_Address ="";

                        selected_Address_modal = new Modal_Address();
                        isAddress_Added_ForUser = false;
                        isAddressForPhoneOrderSelected = false;

                        //showProgressBar(true);
                        getUserDetailsUsingMobileNo(mobileno);

                    }


                }
                else
                {
                    AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

                }




            }
        });





        Objects.requireNonNull(userstoreNumberCheckboxWidget).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isStoreNumberSelected = isChecked;
                if(isChecked){
                    if(vendorKey.equals("vendor_1")){
                        Objects.requireNonNull(customermobileno_editwidget).setText("6380050384");

                    }
                    else if(vendorKey.equals("vendor_2")){
                        Objects.requireNonNull(customermobileno_editwidget).setText("9597580128");

                    }
                    else{
                        Objects.requireNonNull(customermobileno_editwidget).setText(StoreLanLine);

                      }
                }
                else{
                    Objects.requireNonNull(customermobileno_editwidget).setText("");
                }
            }
        });

        Objects.requireNonNull(paymentModeSpinner_Widget).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedPaymentMode = parent.getItemAtPosition(position).toString().toUpperCase();
              //  Toast.makeText(parent.getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                selectedPaymentMode = "NONE SELECTED";
            }
        });


        Objects.requireNonNull(orderTypeSpinner_Widget).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedOrderType = parent.getItemAtPosition(position).toString().toUpperCase();
                if(selectedOrderType.equals(Constants.PhoneOrder)){
                    if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)) {
                        displaySelectedAddress_parentLayout.setVisibility(View.GONE);

                        isPhoneOrderSelected = false;
                        isAddressForPhoneOrderSelected = false;
                        selectedOrderType =  Constants.POSORDER;
                        orderTypeSpinner_Widget.setSelection(0);
                    }
                    else{
                        isPhoneOrderSelected = true;

                        displaySelectedAddress_parentLayout.setVisibility(View.VISIBLE);
                    }

                }
                else{
                    displaySelectedAddress_parentLayout.setVisibility(View.GONE);

                    isPhoneOrderSelected = false;
                        isAddressForPhoneOrderSelected = false;

                }
                //  Toast.makeText(parent.getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                isPhoneOrderSelected = false;
                 isAddressForPhoneOrderSelected = false;

                selectedOrderType =  Constants.POSORDER;
            }
        });



        Objects.requireNonNull(apply_discount_buttonWidget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (cart_Item_List.size() > 0 && cartItem_hashmap.size() > 0) {

                            if ((!itemtotal_textWidget.getText().toString().equals("0")) || (!toPay_textWidget.getText().toString().equals("0")) || (!itemtotal_textWidget.getText().toString().equals("0.00")) || (!toPay_textWidget.getText().toString().equals("0.00"))) {

                                discountAmount = Objects.requireNonNull(discount_editWidget).getText().toString();
                                if ((!discountAmount.equals(""))&&(discountAmount.length()>0)&&(!discountAmount.equals(" "))) {
                                    double discountAmountdouble = Double.parseDouble(discountAmount);
                                    if(discountAmountdouble>0) {

                                        double toPayAmt = Double.parseDouble(String.valueOf(new_totalAmount_withGst));
                                        if (toPayAmt > discountAmountdouble) {
                                            toPayAmt = toPayAmt - discountAmountdouble;
                                            toPayAmt = toPayAmt +Double.parseDouble( deliveryAmount_for_this_order);
                                            int toPayAmountInt = (int) Math.round((toPayAmt));
                                            totalAmounttopay = toPayAmt;
                                            discountTextWidget.setText(discountAmount+".00");
                                            Objects.requireNonNull(toPay_textWidget).setText(String.valueOf(toPayAmountInt) + ".00");
                                        } else {
                                            AlertDialogClass.showDialog(getActivity(), Constants.DiscountAmountInstruction, 0);

                                        }
                                    }
                                    else{
                                        AlertDialogClass.showDialog(getActivity(), Constants.CantApplyDiscountbelowzeroInstruction, 0);

                                    }

                                }
                                else{
                                    AlertDialogClass.showDialog(getActivity(), Constants.CantApplyDiscountbelowzeroInstruction, 0);

                                }
                            }

                    }
                    else{
                        AlertDialogClass.showDialog(getActivity(), Constants.CantApplyDiscountInstruction, 0);

                    }
                }

                catch(Exception e ){
                    discountAmount ="0";

                    discountTextWidget.setText(discountAmount+".00");
                    e.printStackTrace();
                }

            }


        });





        Objects.requireNonNull(checkout_button_Widget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(customermobileno_editwidget).getText().toString().length() == 10) {
                    showProgressBar(true);
                    customermobileno = "";
                    customerName ="";
                    try{
                        customermobileno = "+91"+customermobileno_editwidget.getText().toString();

                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    try{
                        customerName = String.valueOf(autoCompleteCustomerName_widget.getText().toString());

                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }
                    if(vendorType.equals(Constants.WholeSales_VendorType)) {
                        if (wholeSaleCustomersMobileNoStringHashmap.containsKey(String.valueOf(customermobileno))) {
                            String customernameFromHashmap = wholeSaleCustomersMobileNoStringHashmap.get(customermobileno);
                            customernameFromHashmap = String.valueOf(customernameFromHashmap).toUpperCase().trim();
                            if (!customernameFromHashmap.equals(String.valueOf(customerName).toUpperCase().trim())) {
                                addWholeSaleCustomers(customerName, customermobileno);

                            } else {
                                Toast.makeText(mContext, " " + customerName + " is Already Added", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            addWholeSaleCustomers(customerName, customermobileno);
                        }
                    }



                    if (!selectedPaymentMode.equals("NONE SELECTED")) {

                        if (cart_Item_List.size() > 0 && cartItem_hashmap.size() > 0) {
                                   if ((!Objects.requireNonNull(itemtotal_textWidget).getText().toString().equals("0")) && (!Objects.requireNonNull(toPay_textWidget).getText().toString().equals("0")) && (!itemtotal_textWidget.getText().toString().equals("0.0")) && (!toPay_textWidget.getText().toString().equals("0.0")) && (!itemtotal_textWidget.getText().toString().equals("0.00")) && (!toPay_textWidget.getText().toString().equals("0.00")) && (!itemtotal_textWidget.getText().toString().equals("")) && (!toPay_textWidget.getText().toString().equals(""))) {
                                        if (checkforBarcodeInCart("empty")) {
                                            NewOrderScreenFragment_mobile.cart_Item_List.remove("empty");

                                            NewOrderScreenFragment_mobile.cartItem_hashmap.remove("empty");
                                        }

                                        sTime = System.currentTimeMillis();
                                        Currenttime = getDate_and_time();

                                        //Log.i(TAG, "call adapter cart_Item " + cart_Item_List.size());
                                        if (selectedPaymentMode.equals("CASH")) {
                                            selectedPaymentMode = Constants.CASH_ON_DELIVERY;


                                        }
                                       if(isPhoneOrderSelected) {
                                           if (isAddressForPhoneOrderSelected)
                                           {
                                             customermobileno = Objects.requireNonNull(customermobileno_editwidget).getText().toString();
                                            finaltoPayAmountinmethod = toPay_textWidget.getText().toString();


                                               generateTokenNo(vendorKey);



                                            bottomSheetDialog.cancel();
                                            Toast.makeText(getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();
                                           }
                                           else {
                                               AlertDialogClass.showDialog(getActivity(), R.string.Select_an_address);

                                           }
                                       }
                                       else
                                       {
                                           customermobileno = Objects.requireNonNull(customermobileno_editwidget).getText().toString();
                                           finaltoPayAmountinmethod = toPay_textWidget.getText().toString();
                                           PlaceOrdersinDatabaseaAndPrintRecipt(selectedPaymentMode, finaltoPayAmountinmethod, sTime, Currenttime, cart_Item_List);

                                           bottomSheetDialog.cancel();
                                           Toast.makeText(getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();
                                       }



                                   /*    customermobileno = Objects.requireNonNull(customermobileno_editwidget).getText().toString();
                                       finaltoPayAmountinmethod = toPay_textWidget.getText().toString();
                                       PlaceOrdersinDatabaseaAndPrintRecipt(selectedPaymentMode, finaltoPayAmountinmethod, sTime, Currenttime, cart_Item_List);

                                       bottomSheetDialog.cancel();
                                       Toast.makeText(getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();


                                    */

                                   } else {
                                       showProgressBar(false);
                                       AlertDialogClass.showDialog(getActivity(), R.string.Cant_place_order);

                                   }



                        }
                        else{
                            AlertDialogClass.showDialog(getActivity(), R.string.Cart_is_empty);

                        }
                    }


                    else {
                        AlertDialogClass.showDialog(getActivity(), R.string.SelectPaymentMode);

                    }
                } else {
                    AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

                }


            }
        });


        updateUserName_widget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    try {
                        customerName = String.valueOf(autoCompleteCustomerName_widget.getText().toString());

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    if (!customerName.equals("") && !customerName.equals("null") && !customerName.equals("NULL")) {

                        updateUserName = true;
                    } else {
                        updateUserName_widget.setChecked(false);
                        AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_customerName_text);
                    }

                } else {
                    updateUserName = false;

                }
            }
        });


        bottomSheetDialog.show();




        addAddress_close_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAddress_ParentLayout_bottomSheet.setVisibility(View.GONE);
                selectfromAddressList_ParentLayout_bottomSheet.setVisibility(View.VISIBLE);

            }
        });

        close_addressbottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressBottomSheet.cancel();

            }
        });




        swipe_up_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(scrollView.FOCUS_UP);
            }
        });




        swipe_down_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(scrollView.FOCUS_DOWN);

            }
        });



        addNewAddress_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contact_personName_editText.setText("");
                contact_personMobileNo_editText.setText("");
                addressLine1_editText.setText("");
                addressLine2_editText.setText("");
                landmark_editText.setText("");
                pincode_editText.setText("");
                address_type_editText.setText("");
                deliveryDistance_address_editText.setText("");

                if(isNewUser){
                    String userKey = "";
                    try{
                        //userKey =  String.valueOf(UUID.randomUUID())+"-"+String.valueOf(System.currentTimeMillis());
                        userKey =  String.valueOf(System.currentTimeMillis());

                    }
                    catch (Exception e){
                        userKey = "";
                        e.printStackTrace();
                    }

                    if((!String.valueOf(userKey).equals("")) || (!String.valueOf(userKey).toUpperCase().equals("NULL"))){
                        user_key_toAdd_Address =  String.valueOf(userKey);

                    }
                    else{
                        Toast.makeText(mContext, "User Key is Empty", Toast.LENGTH_SHORT).show();
                    }


                }

                selectfromAddressList_ParentLayout_bottomSheet.setVisibility(View.GONE);
                addNewAddress_ParentLayout_bottomSheet.setVisibility(View.VISIBLE);




            }
        });





        saveAddress_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressKey = "";
                try{
                    addressKey =  String.valueOf(UUID.randomUUID());
                }
                catch (Exception e){
                    addressKey = "";
                    e.printStackTrace();
                }

                if((!String.valueOf(addressKey).equals("")) || (!String.valueOf(addressKey).toUpperCase().equals("NULL"))){
                String contactPersonName = String.valueOf(contact_personName_editText.getText());
                String contactPersonMobileNo = String.valueOf(contact_personMobileNo_editText.getText());
                String addressLine1 = String.valueOf(addressLine1_editText.getText());
                String addressLine2 = String.valueOf(addressLine2_editText.getText());
                String landmark = String.valueOf(landmark_editText.getText());
                String pincode = String.valueOf(pincode_editText.getText());
                String type = String.valueOf(address_type_editText.getText());
                String deliveryDistance = String.valueOf(deliveryDistance_address_editText.getText().toString());
                    double deliveryDistanceDouble = 0;
                    try{
                        deliveryDistanceDouble = Double.parseDouble(deliveryDistance);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
              //  Toast.makeText(mContext, "UUID : "+ addressKey, Toast.LENGTH_SHORT).show();
                if(addressLine1.length()>0){
                    if(addressLine2.length()>0){
                        if(pincode.length()>0){
                            if(deliveryDistance.length()>0){
                                if (type.length() > 0) {


                                    Modal_Address modal_address = new Modal_Address();
                                modal_address.setContactpersonname(contactPersonName);
                                modal_address.setContactpersonmobileno(contactPersonMobileNo);
                                modal_address.setAddressline1(addressLine1);
                                modal_address.setAddressline2(addressLine2);
                                modal_address.setLandmark(landmark);
                                modal_address.setPincode(pincode);
                                modal_address.setAddresstype(type);
                                modal_address.setVendorname(vendorName);
                                modal_address.setVendorkey(vendorKey);
                                modal_address.setUserkey(user_key_toAdd_Address);
                                modal_address.setLocationlong("0");
                                modal_address.setLocationlat("0");
                                modal_address.setKey(addressKey);
                                modal_address.setDeliverydistance(deliveryDistance);
                                modal_address.setIsNewAddress(true);
                                if(!userAddressKeyArrayList.contains(addressKey)){
                                    userAddressKeyArrayList.add(addressKey);
                                    userAddressArrayList.add(modal_address);

                                }


                                selectfromAddressList_ParentLayout_bottomSheet.setVisibility(View.VISIBLE);
                                addNewAddress_ParentLayout_bottomSheet.setVisibility(View.GONE);
                                setAddressListAdapter ();

                                }
                                else
                                {
                                    AlertDialogClass.showDialog(getActivity(), R.string.AddressType_is_empty);

                                }


                            }
                            else
                            {
                                AlertDialogClass.showDialog(getActivity(), R.string.DeliveryDistance_is_empty);

                            }
                        }
                        else
                        {
                            AlertDialogClass.showDialog(getActivity(), R.string.Pincode_is_empty);

                        }
                    }
                    else
                    {
                        AlertDialogClass.showDialog(getActivity(), R.string.addressLine2_is_empty);

                    }
                }
                else
                {
                    AlertDialogClass.showDialog(getActivity(), R.string.addressLine1_is_empty);

                }
                }
                else{
                    Toast.makeText(mContext, "Address key is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });







    }

    private void generateTokenNo(String vendorKey) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_generateTokenNo+vendorKey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                Toast.makeText(mContext,"Token Succesfully Generated",Toast.LENGTH_SHORT).show();

                try {
                    String token_no = response.getString("tokenNumber");
                    tokenNo = token_no;
                    //PlaceOrdersinDatabaseaAndPrintRecipt(selectedPaymentMode, finaltoPayAmountinmethod, sTime, Currenttime, cart_Item_List);
                    PlaceOrdersinDatabaseaAndPrintRecipt(selectedPaymentMode, finaltoPayAmountinmethod, sTime, Currenttime, cart_Item_List);

                } catch (JSONException e) {
                    e.printStackTrace();

                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Token_No_Not_Found_Instruction,
                            R.string.OK_Text,R.string.Empty_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    Toast.makeText(mContext,"Please Generate tokenNo in Manage Order",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNo() {

                                }
                            });

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
             //   PlaceOrdersinDatabaseaAndPrintRecipt(selectedPaymentMode, finaltoPayAmountinmethod, sTime, Currenttime, cart_Item_List);

                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Token_No_Error_Instruction,
                        R.string.OK_Text,R.string.Empty_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                Toast.makeText(mContext,"Please Generate tokenNo in Manage Order",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNo() {

                            }
                        });

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);



    }


    private void getUserDetailsUsingMobileNo(String mobileno) {

        String encodedMobileNo = "";
        mobileno = "+91"+mobileno;
        try {
            encodedMobileNo = URLEncoder.encode(mobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getTMCUserDetailsWithMobileNo+"?usermobileno="+encodedMobileNo, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {



                            try {

                                JSONArray JArray = response.getJSONArray("content");
                                int i1 = 0;
                                int arrayLength = JArray.length();
                                if (arrayLength < 1) {
                                    Toast.makeText(mContext, "This mobileno is a new User" +
                                            "", Toast.LENGTH_LONG).show();
                                    try{
                                        customerName = "";
                                    }
                                    catch (Exception e){
                                        customerName ="";
                                        e.printStackTrace();
                                    }
                                    try{
                                        autoCompleteCustomerName_widget.setText(String.valueOf(customerName));
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    isNewUser = true;
                                    isAddress_Added_ForUser = false;
                                    setAddressListAdapter();


                                }

                                for (; i1 < (arrayLength); i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        String uniquekey = "";
                                        if(json.has("key")){
                                            uniquekey =json.getString("key");
                                        }
                                        else{
                                            uniquekey =json.getString("uniquekey");


                                        }
                                        String usertype = "",customer_name ="";
                                        try{
                                            usertype = json.getString("usertype");
                                        }
                                        catch (Exception e){
                                            usertype ="";
                                            e.printStackTrace();
                                        }

                                        try{
                                            customer_name = json.getString("name");
                                        }
                                        catch (Exception e){
                                            customer_name ="";
                                            e.printStackTrace();
                                        }

                                        try{
                                            userStatus = json.getString("userstatus");
                                        }
                                        catch (Exception e){
                                            userStatus ="";
                                            e.printStackTrace();
                                        }

                                        try{
                                            customerName = customer_name;
                                        }
                                        catch (Exception e){
                                            customerName ="";
                                            e.printStackTrace();
                                        }
                                        try{
                                            autoCompleteCustomerName_widget.setText(String.valueOf(customerName));
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(mContext, "Got User details" +
                                                "", Toast.LENGTH_LONG).show();
                                        if(usertype.toUpperCase().contains("PHONE")){
                                            isUsertype_AlreadyPhone = true;

                                        }
                                        else{
                                            isUsertype_AlreadyPhone = false;

                                        }
                                        if(userFetchedManually) {
                                            userFetchedManually = false;

                                        }
                                        else{
                                             getAddressUsingUserKey(uniquekey);

                                        }

                                    }
                                    catch (JSONException e) {
                                        Toast.makeText(mContext, " Error in  user" +String.valueOf(e), Toast.LENGTH_LONG).show();

                                        e.printStackTrace();
                                    }
                                }

                            } catch (JSONException e) {
                                setAddressListAdapter();
                                Toast.makeText(mContext, " Error in getting user" +String.valueOf(e), Toast.LENGTH_LONG).show();

                                e.printStackTrace();

                            }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(mContext, " Error in getting user", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                    showProgressBar(false);
                    setAddressListAdapter();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
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

    private void getAddressUsingUserKey(String key) {

        userAddressKeyArrayList.clear();
        userAddressArrayList.clear();
        redeemPointsJson = new JSONObject();
        isAddressForPhoneOrderSelected = false;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetAddressUsingUserKey + key,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        try {
                            user_key_toAdd_Address = key;

                            JSONArray JArray = response.getJSONArray("content");
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            if (arrayLength < 1) {
                                Toast.makeText(mContext, "There is no address for this user" +
                                        "", Toast.LENGTH_LONG).show();
                                isAddress_Added_ForUser = false;

                                setAddressListAdapter();

                            }

                            for (; i1 < (arrayLength); i1++) {
                                Modal_Address modal_address = new Modal_Address();
                                try {
                                    JSONObject json = JArray.getJSONObject(i1);
                                    String addressKey = "";
                                    try{
                                        modal_address.setKey(String.valueOf(json.getString("key")));
                                        addressKey = (String.valueOf(json.getString("key")));
                                    }
                                    catch (Exception e){
                                        addressKey ="";
                                        modal_address.setKey(String.valueOf(""));
                                        e.printStackTrace();
                                    }

                                    try{
                                        modal_address.setAddressline1(String.valueOf(json.getString("addressline1")));

                                    }
                                    catch (Exception e){
                                        modal_address.setAddressline1(String.valueOf(""));
                                                e.printStackTrace();
                                    }

                                    try{
                                        modal_address.setAddressline2(String.valueOf(json.getString("addressline2")));

                                    }
                                    catch (Exception e){
                                        modal_address.setAddressline2(String.valueOf(""));
                                        e.printStackTrace();
                                    }


                                    try{
                                        modal_address.setAddresstype(String.valueOf(json.getString("addresstype")));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        modal_address.setAddresstype(String.valueOf(""));
                                    }


                                    try{
                                        if(json.has("contactpersonmobileno")) {
                                            if (String.valueOf(json.getString("contactpersonmobileno")).equals("")) {
                                                modal_address.setContactpersonmobileno(String.valueOf(customermobileno));
                                            } else {
                                                modal_address.setContactpersonmobileno(String.valueOf(json.getString("contactpersonmobileno")));
                                            }
                                        }
                                        else{
                                            modal_address.setContactpersonmobileno(String.valueOf(customermobileno));

                                        }
                                    }
                                    catch (Exception e){
                                        modal_address.setContactpersonmobileno(String.valueOf(""));
                                        e.printStackTrace();
                                    }


                                    try{
                                        modal_address.setContactpersonname(String.valueOf(json.getString("contactpersonname")));

                                    }
                                    catch (Exception e){
                                        modal_address.setContactpersonname(String.valueOf(""));
                                        e.printStackTrace();
                                    }


                                    try{
                                        modal_address.setDeliverydistance(String.valueOf(json.getString("deliverydistance")));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        modal_address.setDeliverydistance(String.valueOf(""));
                                    }


                                    try{
                                        modal_address.setLandmark(String.valueOf(json.getString("landmark")));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        modal_address.setLandmark(String.valueOf(""));
                                    }


                                    try{
                                        modal_address.setLocationlat(String.valueOf(json.getString("locationlat")));

                                    }
                                    catch (Exception e){
                                        modal_address.setLocationlat(String.valueOf(""));
                                        e.printStackTrace();
                                    }


                                    try{
                                        modal_address.setLocationlong(String.valueOf(json.getString("locationlong")));

                                    }
                                    catch (Exception e){
                                        modal_address.setLocationlong(String.valueOf(""));
                                        e.printStackTrace();
                                    }


                                    try{
                                        modal_address.setPincode(String.valueOf(json.getString("pincode")));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        modal_address.setPincode(String.valueOf(""));
                                    }


                                    try{
                                        modal_address.setUpdatedtime(String.valueOf(json.getString("updatedtime")));

                                    }
                                    catch (Exception e){
                                        modal_address.setUpdatedtime(String.valueOf(""));
                                        e.printStackTrace();
                                    }

                                    try{

                                        modal_address.setUserkey(String.valueOf(json.getString("userkey")));

                                    }
                                    catch (Exception e){
                                        modal_address.setUserkey(String.valueOf(""));
                                        e.printStackTrace();
                                    }



                                    try{
                                        modal_address.setVendorkey(String.valueOf(json.getString("vendorkey")));

                                    }
                                    catch (Exception e){
                                        modal_address.setVendorkey(String.valueOf(""));
                                        e.printStackTrace();
                                    }


                                    try{

                                        modal_address.setVendorname(String.valueOf(json.getString("vendorname")));

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                        modal_address.setVendorname(String.valueOf(""));
                                    }

                                    if(!userAddressKeyArrayList.contains(addressKey)){
                                        userAddressKeyArrayList.add(addressKey);
                                        userAddressArrayList.add(modal_address);
                                        isAddress_Added_ForUser = true;
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                               // Toast.makeText(mContext, "in address" , Toast.LENGTH_LONG).show();

                                try {
                                    if(arrayLength - i1 == 1){


                                        setAddressListAdapter();
                                      }
                                }

                                catch (Exception e){
                                    setAddressListAdapter();
                                    e.printStackTrace();

                                }


                            }

                        } catch (JSONException e) {
                            setAddressListAdapter();
                            e.printStackTrace();

                        }



                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                try {
                    Toast.makeText(mContext, "Location cnanot be found", Toast.LENGTH_LONG).show();

                    setAddressListAdapter();


                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.getMessage());
                    Log.d(Constants.TAG, "Location cnanot be found Error: " + error.toString());

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

    public void setAddressListAdapter() {
       if(userAddressArrayList.size()>0) {
           id_addressInstruction.setVisibility(View.GONE);
           scrollView.setVisibility(View.VISIBLE);


           adapter_addressList = new Adapter_AddressList(mContext, userAddressArrayList, NewOrderScreenFragment_mobile.this);

            address_listView.setAdapter(adapter_addressList);
            ReportListviewSizeHelper.getListViewSize(address_listView, screenInches);
        }
        else{
            id_addressInstruction.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
        loadingPanel_addressbottomSheet.setVisibility(View.GONE);
        loadingpanelmask_addressbottomSheet.setVisibility(View.GONE);

    }

    private void addWholeSaleCustomers(String customerName, String mobileNo) {
        if(vendorType.equals(Constants.WholeSales_VendorType)) {


            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", customerName);
                jsonObject.put("mobileno", mobileNo);


            } catch (JSONException e) {
                e.printStackTrace();

            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addWholeSalesCustomersTable,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    try {
                        String message = response.getString("message");
                        if(!customerName.equals("")){

                            if (message.equals("success")) {
                                if (wholeSaleCustomersMobileNoStringHashmap.containsKey(mobileNo)) {
                                    for (int i = 0; i < wholeSaleCustomersArrayList.size(); i++) {
                                        Modal_WholeSaleCustomers modal_wholeSaleCustomers = wholeSaleCustomersArrayList.get(i);
                                        String mobilenoFromArray = String.valueOf(modal_wholeSaleCustomers.getMobileno());
                                        if (mobilenoFromArray.equals(mobileNo)) {
                                            modal_wholeSaleCustomers.setCustomerName(customerName);

                                        }
                                    }


                                } else {
                                    Modal_WholeSaleCustomers modal_wholeSaleCustomers = new Modal_WholeSaleCustomers();
                                    modal_wholeSaleCustomers.setMobileno(mobileNo);
                                    modal_wholeSaleCustomers.setCustomerName(customerName);
                                    wholeSaleCustomersArrayList.add(modal_wholeSaleCustomers);

                                }
                                wholeSaleCustomersMobileNoStringHashmap.put(mobileNo, customerName);

                                adapter_autoCompleteWholeSaleCustomers.notifyDataSetChanged();
                                updateDatainSharedPreference(wholeSaleCustomersArrayList);
                                Toast.makeText(mContext, " " + customerName + " is Added Now", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(mContext, "Error in adding " + customerName, Toast.LENGTH_SHORT).show();

                            }
                        }
                    } catch (JSONException e) {
                        Toast.makeText(mContext, "Error in adding " + customerName, Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Toast.makeText(mContext, "Error in adding " + customerName, Toast.LENGTH_SHORT).show();

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
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            // Make the request
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);
        }




    }

    private void updateDatainSharedPreference(List<Modal_WholeSaleCustomers> wholeSaleCustomersArrayList) {


        try {
            final SharedPreferences sharedPreferences = mContext.getSharedPreferences("WholeSaleCustomerDetails", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(wholeSaleCustomersArrayList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("WholeSaleCustomerDetails", json);
            editor.apply();

        }
        catch (Exception e){
            e.printStackTrace();
        }



    }


    private void PlaceOrdersinDatabaseaAndPrintRecipt(String paymentMode, String finaltoPayAmountinmethod, long sTime, String currenttime, List<String> cart_Item_list) {
        showProgressBar(true);

        if (ispaymentMode_Clicked) {
            showProgressBar(false);
            Toast.makeText(mContext, "Try Place order again", Toast.LENGTH_SHORT).show();

            return;
        }
        else {

            ispaymentMode_Clicked = true;
            String payableAmount =finaltoPayAmount;


            if(isPhoneOrderSelected){
                if(userAddressKeyArrayList.size()>0 && userAddressArrayList.size()>0){
                    if(isAddressForPhoneOrderSelected) {
                        for (int iterator = 0; iterator < userAddressArrayList.size(); iterator++) {
                            boolean isAddressNewlyAdded = false;
                            boolean isAddressEdited = false;
                            String key = "", addressline1 = "", addressline2 = "", addresstype = "", contactpersonmobileno = "", contactpersonname = "", landmark = "",
                                    pincode = "", updatedtime = "", userkey = "", vendorkey = "", vendorname = "";
                            double locationlong = 0, deliverydistance = 0, locationlat = 0;
                            Modal_Address modal_address = userAddressArrayList.get(iterator);
                            try {
                                isAddressNewlyAdded = modal_address.getisNewAddress();
                            } catch (Exception e) {
                                isAddressNewlyAdded = false;
                                e.printStackTrace();
                            }

                            try {
                                isAddressEdited = modal_address.getisAddressEdited();
                            } catch (Exception e) {
                                isAddressEdited = false;
                                e.printStackTrace();
                            }


                            try {
                                key = String.valueOf(modal_address.getKey());
                            } catch (Exception e) {
                                key = "";
                                e.printStackTrace();
                            }

                            try {
                                addressline1 = String.valueOf(modal_address.getAddressline1());
                            } catch (Exception e) {
                                addressline1 = "";
                                e.printStackTrace();
                            }
                            try {
                                addressline2 = String.valueOf(modal_address.getAddressline2());
                            } catch (Exception e) {
                                addressline2 = "";
                                e.printStackTrace();
                            }

                            try {
                                addresstype = String.valueOf(modal_address.getAddresstype());
                            } catch (Exception e) {
                                addresstype = "";
                                e.printStackTrace();
                            }

                            try {
                                contactpersonmobileno = String.valueOf(modal_address.getContactpersonmobileno());
                            } catch (Exception e) {
                                contactpersonmobileno = "";
                                e.printStackTrace();
                            }

                            try {
                                contactpersonname = String.valueOf(modal_address.getContactpersonname());
                            } catch (Exception e) {
                                contactpersonname = "";
                                e.printStackTrace();
                            }
                            try {
                                landmark = String.valueOf(modal_address.getLandmark());
                            } catch (Exception e) {
                                landmark = "";
                                e.printStackTrace();
                            }

                            try {
                                pincode = String.valueOf(modal_address.getPincode());
                            } catch (Exception e) {
                                pincode = "";
                                e.printStackTrace();
                            }

                            try {
                                updatedtime = String.valueOf(getDate_and_time());
                            } catch (Exception e) {
                                updatedtime = "";
                                e.printStackTrace();
                            }

                            try {
                                userkey = String.valueOf(modal_address.getUserkey());
                            } catch (Exception e) {
                                userkey = "";
                                e.printStackTrace();
                            }
                            try {
                                updatedtime = String.valueOf(getDate_and_time());
                            } catch (Exception e) {
                                updatedtime = "";
                                e.printStackTrace();
                            }

                            try {
                                vendorkey = String.valueOf(modal_address.getVendorkey());
                            } catch (Exception e) {
                                vendorkey = "";
                                e.printStackTrace();
                            }

                            try {
                                vendorname = String.valueOf(modal_address.getVendorname());
                            } catch (Exception e) {
                                vendorname = "";
                                e.printStackTrace();
                            }

                            try {
                                locationlat = Double.parseDouble(String.valueOf(modal_address.getLocationlat()));
                            } catch (Exception e) {
                                locationlat = 0;
                                e.printStackTrace();
                            }
                            try {
                                locationlong = Double.parseDouble(String.valueOf(modal_address.getLocationlong()));
                            } catch (Exception e) {
                                locationlong = 0;
                                e.printStackTrace();
                            }

                            try {
                                deliverydistance = Double.parseDouble(String.valueOf(modal_address.getDeliverydistance()));
                            } catch (Exception e) {
                                deliverydistance = 0;
                                e.printStackTrace();
                            }


                            try {
                                if (isAddressNewlyAdded) {
                                    try {
                                        JSONObject jsonObject = new JSONObject();
                                        try {


                                            jsonObject.put("key", key);
                                            jsonObject.put("addressline1", addressline1);
                                            jsonObject.put("addressline2", addressline2);
                                            jsonObject.put("addresstype", addresstype);
                                            jsonObject.put("createdtime", getDate_and_time());
                                            jsonObject.put("isvendorremapping", "");
                                            jsonObject.put("contactpersonmobileno", "+91" + contactpersonmobileno);
                                            jsonObject.put("contactpersonname", contactpersonname);
                                            jsonObject.put("deliverydistance", deliverydistance);
                                            jsonObject.put("landmark", landmark);
                                            jsonObject.put("locationlat", locationlat);
                                            jsonObject.put("locationlong", locationlong);
                                            jsonObject.put("pincode", pincode);
                                            jsonObject.put("updatedtime", updatedtime);
                                            jsonObject.put("userkey", userkey);
                                            jsonObject.put("vendorkey", vendorkey);
                                            jsonObject.put("vendorname", vendorname);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Add_Address_For_this_User(jsonObject);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        showProgressBar(false);

                        Toast.makeText(mContext, "Please Select Address to Place Phone Order", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    }
                else{
                    showProgressBar(false);

                    Toast.makeText(mContext, "Please Add Address to Place Phone Order", Toast.LENGTH_SHORT).show();
                    return;
                }


               if( !isUsertype_AlreadyPhone || updateUserName){
                   if(!isStoreNumberSelected) {
                       Add_OR_Update_Entry_inTMCUserTable("PHONE");
                   }
               }


            }
            else
            {
                if(!isStoreNumberSelected) {
                    Add_OR_Update_Entry_inTMCUserTable("WALKIN");
                }

            }





            if(payableAmount.equals("")||payableAmount.equals("0")||payableAmount.equals("0.00")||payableAmount.equals("0.0")){


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.print_again);
                            dialog.setTitle("Last Order is Not Placed .Please Try Again !!!! ");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);

                            Button printAgain = (Button) dialog.findViewById(R.id.printAgain);
                            printAgain.setText("OK");

                            printAgain.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();



                                    customermobileno="";
                                    customerName ="";
                                    userAddressArrayList.clear();
                                       redeemPointsJson = new JSONObject();
                                    userAddressKeyArrayList.clear();
                                    selectedAddressKey = String.valueOf("");
                                    selectedAddress = String.valueOf("");
                                    userLatitude = String.valueOf("0");
                                    userLongitude = String.valueOf("0");
                                    deliveryDistance ="";
                                    user_key_toAdd_Address ="";

                                    fulladdress_textview.setText("");
                                    autoCompleteCustomerName_widget.setText("");

                                    selected_Address_modal = new Modal_Address();
                                    isPhoneOrderSelected = false;
                                    orderTypeSpinner_newWidget.setSelection(0);
                                      isAddressForPhoneOrderSelected = false;
                                    updateUserName = false;
                                    isNewUser = false;
                                    isAddress_Added_ForUser = false;
                                    isAddressForPhoneOrderSelected = false;
                                    isUsertype_AlreadyPhone = false;
                                    userFetchedManually = false;
                                    isStoreNumberSelected = false;

                                    cart_Item_List.clear();
                                    cartItem_hashmap.clear();
                                    ispaymentMode_Clicked = false;
                                    isOrderDetailsMethodCalled = false;
                                    isPaymentDetailsMethodCalled = false;
                                    isOrderTrackingDetailsMethodCalled = false;
                                    isCustomerOrdersTableServiceCalled  = false;
                                    newGst =0;

                                    new_to_pay_Amount = 0;
                                    old_taxes_and_charges_Amount = 0;
                                    old_total_Amount = 0;
                                    createEmptyRowInListView("empty");
                                    CallAdapter();
                                    discountAmount ="0";
                                    deliveryAmount_for_this_order ="0";
                                    showProgressBar(false);
                                    totalAmounttopay=0;
                                    finalamounttoPay=0;
                                    new_totalAmount_withoutGst =0;
                                    finaltoPayAmount = "0";
                                    isPrintedSecondTime = false;
                                    isUpdateCouponTransactionMethodCalled=false;
                                    isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                    /*
                                    discount_Edit_widget.setText("0");
                                    discount_rs_text_widget.setText(discountAmount);
                                    OrderTypefromSpinner =  Constants.POSORDER;
                                    orderTypeSpinner.setSelection(0);
                                    total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                    taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                    total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                    mobileNo_Edit_widget.setText("");

                                    ispointsApplied_redeemClicked=false;
                                    isProceedtoCheckoutinRedeemdialogClicked =false;
                                    isRedeemDialogboxOpened=false;
                                    isUpdateRedeemPointsMethodCalled=false;



                                    pointsalreadyredeemDouble=0;
                                    totalpointsuserhave_afterapplypoints=0;
                                    pointsenteredToredeem_double=0;
                                    pointsenteredToredeem="";

                                    finaltoPayAmountwithRedeemPoints="";
                                    redeemPoints_String="";
                                    redeemKey="";
                                    mobileno_redeemKey="";
                                    discountAmountalreadyusedtoday="";
                                    totalpointsredeemedalreadybyuser="";
                                    totalordervalue_tillnow="";
                                    totalredeempointsuserhave="";

                                    redeemed_points_text_widget.setText("");
                                    redeemPointsLayout.setVisibility(View.GONE);
                                    discountAmountLayout.setVisibility(View.VISIBLE);

 */
                                    return;

                                }
                            });


                            dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            if(defaultPrinterType.equals(Constants.PDF_PrinterType)) {


                try{
                    totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                String UserMobile = "+91" + customermobileno;

                //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();


                if(isOrderPlacingMicroserviceisActive){
                    redeemPointsJson = new JSONObject();
                    try {
                        redeemPointsJson.put("mobileno",UserMobile);
                        redeemPointsJson.put("totalordervalue",totalAmounttopay);
                        redeemPointsJson.put("havetodocalculation",true);
                        redeemPointsJson.put("totalredeempoints",totalredeempointsusergetfromorder);
                        redeemPointsJson.put("vendorname",vendorName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else{

                    updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

                }



                if(String.valueOf(paymentMode).toUpperCase().equals(Constants.CREDIT)){
                    GetDatafromCreditOrderDetailsTable(paymentMode,sTime,currenttime);
                }
                else{


                    if(isOrderPlacingMicroserviceisActive) {

                        try{
                            if(orderdetailsnewschema){

                                if(isPhoneOrderSelected){
                                    ordertype = Constants.PhoneOrder;
                                }
                                else{
                                    ordertype = Constants.POSORDER;

                                }



                                initAndCallingOrderPlacingModuleHandlerInterface(mContext, paymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, payableAmount, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, 0, 0);



                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();

                        }


                    }
                    else{
                        if(!isCustomerOrdersTableServiceCalled){
                            try{
                                if(orderdetailsnewschema){
                                    initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                    if(isPhoneOrderSelected){
                                        ordertype = Constants.PhoneOrder;
                                    }
                                    else{
                                        ordertype = Constants.POSORDER;

                                    }
                                    isCustomerOrdersTableServiceCalled =true;


                                    Add_CustomerOrder_TrackingTable_AsyncTask asyncTask=new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface,NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode,discountAmount,Currenttime,customermobileno,ordertype,vendorKey,vendorName, sTime,finaltoPayAmountinmethod,selected_Address_modal,tokenNo,userStatus,customerName,"0");
                                    asyncTask.execute();

                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();

                            }
                        }

                        if (!isOrderDetailsMethodCalled) {
                            shouldGetPrintNow_Global = false;
                            PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, paymentMode, sTime,finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                        }
                        if (!isOrderTrackingDetailsMethodCalled) {

                            PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                        }
                    }


                }








                return;

            }
            else {


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



                                    try {
                                        totalredeempointsusergetfromorder = Math.round((pointsfor100rs_double * totalAmounttopay) / 100);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    String UserMobile = "+91" + customermobileno;

                                    //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                    //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                                    // updateRedeemPointsDetailsInDBWithoutkey(UserMobile, totalAmounttopay, totalredeempointsusergetfromorder);
                                    if(isOrderPlacingMicroserviceisActive){
                                        redeemPointsJson = new JSONObject();
                                        try {
                                            redeemPointsJson.put("mobileno",UserMobile);
                                            redeemPointsJson.put("totalordervalue",totalAmounttopay);
                                            redeemPointsJson.put("havetodocalculation",true);
                                            redeemPointsJson.put("totalredeempoints",totalredeempointsusergetfromorder);
                                            redeemPointsJson.put("vendorname",vendorName);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                    else{

                                        updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

                                    }


                                    if (String.valueOf(paymentMode).toUpperCase().equals(Constants.CREDIT)) {
                                        GetDatafromCreditOrderDetailsTable(paymentMode, sTime, currenttime);
                                    }
                                    else {
                                        if(isOrderPlacingMicroserviceisActive) {

                                            try{
                                                if(orderdetailsnewschema){

                                                    if(isPhoneOrderSelected){
                                                        ordertype = Constants.PhoneOrder;
                                                    }
                                                    else{
                                                        ordertype = Constants.POSORDER;

                                                    }



                                                    initAndCallingOrderPlacingModuleHandlerInterface(mContext, paymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, payableAmount, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, 0, 0);



                                                }

                                            }
                                            catch (Exception e){
                                                e.printStackTrace();

                                            }


                                        }
                                        else {
                                            if (!isCustomerOrdersTableServiceCalled) {
                                                try {
                                                    if (orderdetailsnewschema) {
                                                        initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                                        if (isPhoneOrderSelected) {
                                                            ordertype = Constants.PhoneOrder;
                                                        } else {
                                                            ordertype = Constants.POSORDER;

                                                        }
                                                        isCustomerOrdersTableServiceCalled = true;


                                                        Add_CustomerOrder_TrackingTable_AsyncTask asyncTask = new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName, "0");
                                                        asyncTask.execute();

                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }
                                            if (!isOrderDetailsMethodCalled) {
                                                shouldGetPrintNow_Global = false;
                                                PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, paymentMode, sTime, finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                                            }
                                            if (!isOrderTrackingDetailsMethodCalled) {

                                                PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                                            }
                                        }
                                    }



                                    return;

                                }
                            });
                }


                if (!BluetoothPrintDriver.IsNoConnection()) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                    if (!mBluetoothAdapter.isEnabled()) {
                        Toast.makeText(mContext, "Printer Is Not Connected", Toast.LENGTH_LONG).show();

                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Bluetooth_turnedOff_Information,
                                R.string.Yes_Text, R.string.No_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        ConnectPrinter();

                                    }

                                    @Override
                                    public void onNo() {
                                        try {
                                            totalredeempointsusergetfromorder = Math.round((pointsfor100rs_double * totalAmounttopay) / 100);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        String UserMobile = "+91" + customermobileno;

                                        //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                        //   Toast.makeText(mContext,"pointsupdateRedeemPointsDetailsInDBWithoutkey :"+se,Toast.LENGTH_LONG).show();
                                        //  (UserMobile, totalAmounttopay, totalredeempointsusergetfromorder);
                                        if(isOrderPlacingMicroserviceisActive){
                                            redeemPointsJson = new JSONObject();
                                            try {
                                                redeemPointsJson.put("mobileno",UserMobile);
                                                redeemPointsJson.put("totalordervalue",totalAmounttopay);
                                                redeemPointsJson.put("havetodocalculation",true);
                                                redeemPointsJson.put("totalredeempoints",totalredeempointsusergetfromorder);
                                                redeemPointsJson.put("vendorname",vendorName);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                        else{

                                            updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

                                        }

                                        if (String.valueOf(paymentMode).toUpperCase().equals(Constants.CREDIT)) {
                                            GetDatafromCreditOrderDetailsTable(paymentMode, sTime, currenttime);
                                        } else {

                                            if(isOrderPlacingMicroserviceisActive) {

                                                try{
                                                    if(orderdetailsnewschema){

                                                        if(isPhoneOrderSelected){
                                                            ordertype = Constants.PhoneOrder;
                                                        }
                                                        else{
                                                            ordertype = Constants.POSORDER;

                                                        }



                                                        initAndCallingOrderPlacingModuleHandlerInterface(mContext, paymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, payableAmount, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, 0, 0);



                                                    }

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();

                                                }


                                            }
                                            else {

                                                if (!isCustomerOrdersTableServiceCalled) {
                                                    try {
                                                        if (orderdetailsnewschema) {
                                                            initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                                            if (isPhoneOrderSelected) {
                                                                ordertype = Constants.PhoneOrder;
                                                            } else {
                                                                ordertype = Constants.POSORDER;

                                                            }
                                                            isCustomerOrdersTableServiceCalled = true;
                                                            Add_CustomerOrder_TrackingTable_AsyncTask asyncTask = new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName, "0");
                                                            asyncTask.execute();

                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                    }
                                                }
                                                if (!isOrderDetailsMethodCalled) {
                                                    shouldGetPrintNow_Global = false;
                                                    PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, paymentMode, sTime, finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                                                }
                                                if (!isOrderTrackingDetailsMethodCalled) {

                                                    PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                                                }
                                            }
                                        }



                                        return;
                                    }
                                });

                    } else {
                        try {
                            totalredeempointsusergetfromorder = Math.round((pointsfor100rs_double * totalAmounttopay) / 100);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String UserMobile = "+91" + customermobileno;

                        //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                        //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                        // updateRedeemPointsDetailsInDBWithoutkey(UserMobile, totalAmounttopay, totalredeempointsusergetfromorder);

                        if(isOrderPlacingMicroserviceisActive){
                            redeemPointsJson = new JSONObject();
                            try {
                                redeemPointsJson.put("mobileno",UserMobile);
                                redeemPointsJson.put("totalordervalue",totalAmounttopay);
                                redeemPointsJson.put("havetodocalculation",true);
                                redeemPointsJson.put("totalredeempoints",totalredeempointsusergetfromorder);
                                redeemPointsJson.put("vendorname",vendorName);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                        else{

                            updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

                        }
                        if (String.valueOf(paymentMode).toUpperCase().equals(Constants.CREDIT)) {
                            GetDatafromCreditOrderDetailsTable(paymentMode, sTime, currenttime);
                        }
                        else {
                            if(isOrderPlacingMicroserviceisActive) {

                                try{
                                    if(orderdetailsnewschema){

                                        if(isPhoneOrderSelected){
                                            ordertype = Constants.PhoneOrder;
                                        }
                                        else{
                                            ordertype = Constants.POSORDER;

                                        }



                                        initAndCallingOrderPlacingModuleHandlerInterface(mContext, paymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, payableAmount, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, 0, 0);



                                    }

                                }
                                catch (Exception e){
                                    e.printStackTrace();

                                }


                            }
                            else {
                                if (!isCustomerOrdersTableServiceCalled) {
                                    try {
                                        if (orderdetailsnewschema) {
                                            initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                            if (isPhoneOrderSelected) {
                                                ordertype = Constants.PhoneOrder;
                                            } else {
                                                ordertype = Constants.POSORDER;

                                            }
                                            isCustomerOrdersTableServiceCalled = true;
                                            Add_CustomerOrder_TrackingTable_AsyncTask asyncTask = new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName, "0");
                                            asyncTask.execute();

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }
                                }
                                if (!isOrderDetailsMethodCalled) {
                                    shouldGetPrintNow_Global = true;
                                    PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, paymentMode, sTime, finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                                }
                                if (!isOrderTrackingDetailsMethodCalled) {

                                    PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                                }
                            }
                        }



                    }


                }

            }



        }


    }

    private void Add_OR_Update_Entry_inTMCUserTable(String usertype) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobileno","+91"+customermobileno);
            jsonObject.put("email","");

           if(updateUserName){
                jsonObject.put("name",customerName);

            }
           else{
               jsonObject.put("name","");

           }
           if(!isPhoneOrderSelected) {
               if (user_key_toAdd_Address.equals("")) {
                   String userKey = "";
                   try {
                       userKey = String.valueOf(UUID.randomUUID());
                   } catch (Exception e) {
                       userKey = "";
                       e.printStackTrace();
                   }

                   if ((!String.valueOf(userKey).equals("")) || (!String.valueOf(userKey).toUpperCase().equals("NULL"))) {
                       user_key_toAdd_Address = String.valueOf(userKey);
                       jsonObject.put("uniquekey",user_key_toAdd_Address);

                   } else {
                       Toast.makeText(mContext, "User Key is Empty", Toast.LENGTH_SHORT).show();
                   }
               }
           }
           else{
               jsonObject.put("uniquekey",user_key_toAdd_Address);

           }
            jsonObject.put("authorizationcode","");
            jsonObject.put("appversion","");
            jsonObject.put("fcmtoken","");
            jsonObject.put("createddate",getDate());
            jsonObject.put("createdtime",getDate_and_time());
            jsonObject.put("updatedtime",getDate_and_time());
            jsonObject.put("usertype",usertype);
            jsonObject.put("deviceos","");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_add_update_TMCUserTable ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

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
        // Make the request


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);







    }

    private void Add_Address_For_this_User(JSONObject jsonObject) {



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_addAddress ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

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
        // Make the request


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





    }

    private void initAndPlaceOrderinCustomerOrder_TrackingInterface(Context mContext) {
        mResultCallback_Add_CustomerOrder_TrackingTableInterface = new Add_CustomerOrder_TrackingTableInterface() {


            @Override
            public void notifySuccess(String requestType, String success) {
                isCustomerOrdersTableServiceCalled = false;
            }

            @Override
            public void notifyError(String requestType, String error) {
                isCustomerOrdersTableServiceCalled = false;

               // Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
            }
        };


    }

    private String getSlotTime(String slottime, String orderplacedtime) {
        String result = "", lastFourDigits = "";
        //   Log.d(TAG, "slottime  "+slottime);
        if (slottime.contains("mins")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
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



    public void CallAdapter() {
        try {
            if (cart_Item_List.size() <= 0) {
                mobile_ItemTotal_textwidget.setText("0.00");
                mobile_GST_textwidget.setText("0.00");


                mobile_ToPay_textwidget.setText("0.00");
            } else {
                try {
                    add_amount_ForBillDetails();
                }
                catch (Exception e){

                }
            }
        }
        catch (Exception e){

        }
        adapterNewOrderScreenFragmentMobile = new Adapter_NewOrderScreenFragment_Mobile(mContext,cartItem_hashmap, MenuItems, NewOrderScreenFragment_mobile.this,completemenuItem);
        adapterNewOrderScreenFragmentMobile.setHandler(newHandler());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        int last_index=NewOrderScreenFragment_mobile.cartItem_hashmap.size()-1;

        recyclerView.setAdapter(adapterNewOrderScreenFragmentMobile);
        recyclerView.scrollToPosition(last_index);
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.billingscreen_mobile_neworderfragment);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        fulladdress_textview = bottomSheetDialog.findViewById(R.id.fulladdress_textview);
         autoCompleteCustomerName_widget = bottomSheetDialog.findViewById(R.id.autoCompleteCustomerName_widget);

        addressBottomSheet = new BottomSheetDialog(mContext);
        addressBottomSheet.setContentView(R.layout.select_address_bottomsheet);
        addressBottomSheet.setCanceledOnTouchOutside(false);
        address_listView = addressBottomSheet.findViewById(R.id.address_listView);
        loadingpanelmask_addressbottomSheet = addressBottomSheet.findViewById(R.id.loadingpanelmask);
        loadingPanel_addressbottomSheet = addressBottomSheet.findViewById(R.id.loadingPanel);
        selectfromAddressList_ParentLayout_bottomSheet = addressBottomSheet.findViewById(R.id.selectfromAddressList_ParentLayout_bottomSheet);
        addNewAddress_ParentLayout_bottomSheet = addressBottomSheet.findViewById(R.id.addNewAddress_ParentLayout_bottomSheet);

        id_addressInstruction  = addressBottomSheet.findViewById(R.id.id_addressInstruction);
        scrollView = addressBottomSheet.findViewById(R.id.scrollView);
        addNewAddress_widget = addressBottomSheet.findViewById(R.id.addNewAddress_layout);
    }

    public void createEmptyRowInListView(String empty) {
        Modal_NewOrderItems newOrdersPojoClass = new Modal_NewOrderItems();
        newOrdersPojoClass.itemname = "";
        newOrdersPojoClass.tmcpriceperkg = "";
        newOrdersPojoClass.grossweight = "";
        newOrdersPojoClass.netweight = "";
        newOrdersPojoClass.tmcprice = "";
        newOrdersPojoClass.gstpercentage = "";
        newOrdersPojoClass.portionsize = "";
        newOrdersPojoClass.pricetypeforpos = "";
        newOrdersPojoClass.itemFinalWeight="";
        newOrdersPojoClass.pricePerItem ="";
        newOrdersPojoClass.quantity="";
        newOrdersPojoClass.itemFinalPrice = "0";
        newOrdersPojoClass.gstpercentage = "0";
        newOrdersPojoClass.discountpercentage = "0";

        newOrdersPojoClass.itemuniquecode=empty;
        cart_Item_List.add(empty);
        cartItem_hashmap.put(empty,newOrdersPojoClass);
    }

    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);


        }

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


            return CurrentDate;
        }
    }

    private Handler newHandler() {
        Handler.Callback callback = new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String data = bundle.getString("CartItem");

                if (data.equalsIgnoreCase("addNewItem")) {

                }

                if (data.equalsIgnoreCase("addBillDetails")) {
                    //   createBillDetails(cart_Item_List);

                }
                return false;
            }
        };
        return new Handler(callback);
    }


    private List<Modal_NewOrderItems> getMenuItemfromString(String menulist) {
        List<Modal_NewOrderItems>MenuList=new ArrayList<>();
        String ItemName = "",tmcsubctgykey="";
        if(!menulist.isEmpty()) {

            try {
                //converting jsonSTRING into array
              // JSONObject jsonObject = new JSONObject(menulist);
              //  JSONArray JArray = jsonObject.getJSONArray("content");

                JSONArray JArray = new JSONArray(menulist);
                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                int i1 = 0;
                int arrayLength = JArray.length();
                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                for (; i1 < (arrayLength); i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        Modal_NewOrderItems newOrdersPojoClass = new Modal_NewOrderItems();
                        if(json.has("tmcsubctgykey")){
                            newOrdersPojoClass.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                            tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));
                        }
                        else{
                            newOrdersPojoClass.tmcsubctgykey = "0";
                            tmcsubctgykey = "0";
                            Toast.makeText(mContext,"TMC tmcsubctgykey Json is Missing",Toast.LENGTH_LONG).show();
                            //Log.i("Tag", "TMC tmcsubctgykey Json is Missing"+ String.valueOf(newOrdersPojoClass.getTmcsubctgykey()));

                        }
                        if(json.has("itemname")){

                            if(tmcsubctgykey.equals("tmcsubctgy_16")){
                                ItemName =  "Grill House "+String.valueOf(json.get("itemname"));

                                newOrdersPojoClass.itemname = "Grill House "+String.valueOf(json.get("itemname"));
                            }
                            else if(tmcsubctgykey.equals("tmcsubctgy_15")){
                                ItemName =  "Ready to Cook "+String.valueOf(json.get("itemname"));

                                newOrdersPojoClass.itemname = "Ready to Cook "+String.valueOf(json.get("itemname"));
                            }
                            else{
                                ItemName =  String.valueOf(json.get("itemname"));

                                newOrdersPojoClass.itemname = String.valueOf(json.get("itemname"));

                            }

                        }
                        else{
                            newOrdersPojoClass.itemname = "Item Name is Missing";
                            Toast.makeText(mContext,"TMC itemname Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("tmcpriceperkg")){
                            try {
                                String tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));
                                double doubleAmount = Double.parseDouble(tmcpriceperkg);
                                int intAmount = (int) Math.round(doubleAmount);

                                //Log.i("Tag", "doubleAmount" + String.valueOf(intAmount));
                                newOrdersPojoClass.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));
                            }catch (Exception e ){
                                Toast.makeText(mContext,"Can't Convert  PriceperKg for "+ItemName+"in Menu Item",Toast.LENGTH_LONG).show();

                            }


                        }
                        else{
                            newOrdersPojoClass.tmcpriceperkg = "0";
                            Toast.makeText(mContext,"TMC PriceperKg Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("grossweight")){
                            newOrdersPojoClass.grossweight = String.valueOf(json.get("grossweight"));

                        }
                        else{
                            newOrdersPojoClass.grossweight = "0";
                            Toast.makeText(mContext,"TMC grossweight Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("netweight")){
                            newOrdersPojoClass.netweight = String.valueOf(json.get("netweight"));

                        }
                        else{
                            newOrdersPojoClass.netweight = "0";
                            Toast.makeText(mContext,"TMC netweight Json is Missing",Toast.LENGTH_LONG).show();

                        }

                        if(json.has("inventorydetails")){
                            newOrdersPojoClass.inventorydetails = String.valueOf(json.get("inventorydetails"));

                        }
                        else{
                            newOrdersPojoClass.inventorydetails = "nil";
                          //  Toast.makeText(mContext,"TMC netweight Json is Missing",Toast.LENGTH_LONG).show();

                        }


                        if(json.has("itemuniquecode")){
                            newOrdersPojoClass.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                        }
                        else{
                            Toast.makeText(mContext,"TMC itemuniquecode Json is Missing",Toast.LENGTH_LONG).show();

                            newOrdersPojoClass.itemuniquecode = "No Item Unique code for this item";
                        }


                        if(json.has("tmcprice")){
                            try {
                                String tmcprice = String.valueOf(json.get("tmcprice"));
                                double doubleAmount = Double.parseDouble(tmcprice);
                                int intAmount = (int) Math.round(doubleAmount);

                                //Log.i("Tag", "doubleAmount" + String.valueOf(intAmount));
                                newOrdersPojoClass.tmcprice = String.valueOf(json.get("tmcprice"));
                            }catch (Exception e ){
                                Toast.makeText(mContext,"Can't Convert  tmcPrice for "+ItemName+"in Menu Item",Toast.LENGTH_LONG).show();

                            }


                        }
                        else{
                            newOrdersPojoClass.tmcpriceperkg = "0";
                            Toast.makeText(mContext,"TMC price Json is Missing",Toast.LENGTH_LONG).show();
                            Toast.makeText(mContext,"TMC tmcpriceperkg Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("key")){
                            newOrdersPojoClass.menuItemId= String.valueOf(json.get("key"));

                        }
                        else{
                            if(json.has("menuItemId")){
                                newOrdersPojoClass.menuItemId= String.valueOf(json.get("menuItemId"));

                            }
                            else{
                                newOrdersPojoClass.menuItemId = "Key for this menu is missing";
                                Toast.makeText(mContext,"TMC menuItemId Json is Missing",Toast.LENGTH_LONG).show();

                            }

                        }
                        if(json.has("portionsize")){
                            newOrdersPojoClass.portionsize = String.valueOf(json.get("portionsize"));

                        }
                        else{
                            newOrdersPojoClass.portionsize = "";
                            Toast.makeText(mContext,"TMC portionsize Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("pricetypeforpos")){
                            newOrdersPojoClass.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                        }
                        else{
                            newOrdersPojoClass.pricetypeforpos = "0";
                            Toast.makeText(mContext,"TMC pricetypeforpos Json is Missing",Toast.LENGTH_LONG).show();

                        }

                        try {
                            newOrdersPojoClass.stockincomingkey = "Nil";
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }




                        String barcodefromMenuItem = "";

                        if(json.has("barcode")){
                            newOrdersPojoClass.barcode= String.valueOf(json.get("barcode"));
                            barcodefromMenuItem  = String.valueOf(json.get("barcode"));

                        }
                        else{
                            newOrdersPojoClass.barcode = "barcode for this menu is missing";
                            Toast.makeText(mContext,"TMC barcode Json is Missing",Toast.LENGTH_LONG).show();

                        }
                    /*
                        for(int iterator =0;iterator<MenuItemStockAvlDetails.size();iterator++){
                            Modal_MenuItemStockAvlDetails modal_menuItemStockAvlDetails = MenuItemStockAvlDetails.get(iterator);
                            String barcodeFromMenuAvlDetails = modal_menuItemStockAvlDetails.getBarcode().toString();

                            if(barcodeFromMenuAvlDetails.equals(barcodefromMenuItem)){
                                newOrdersPojoClass.stockincomingkey = modal_menuItemStockAvlDetails.getStockincomingkey().toString();
                            }


                        }

                     */


                        if(json.has("gstpercentage")){
                            newOrdersPojoClass.gstpercentage = String.valueOf(json.get("gstpercentage"));

                        }
                        else{
                            newOrdersPojoClass.gstpercentage = "";
                            Toast.makeText(mContext,"TMC gstpercentage Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("applieddiscountpercentage")){
                            newOrdersPojoClass.applieddiscountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                        }
                        else{
                            newOrdersPojoClass.applieddiscountpercentage = "0";
                            Toast.makeText(mContext,"TMC applieddiscountpercentage Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("appmarkuppercentage")){
                            newOrdersPojoClass.appmarkuppercentage = String.valueOf(json.get("appmarkuppercentage"));

                        }
                        else{
                            newOrdersPojoClass.appmarkuppercentage = "0";
                            //Toast.makeText(mContext,"TMC appmarkuppercentage Json is Missing",Toast.LENGTH_LONG).show();

                        }

                        if(json.has("tmcpriceperkgWithMarkupValue")){
                            try{
                                newOrdersPojoClass.tmcpriceperkgWithMarkupValue = String.valueOf(json.get("tmcpriceperkgWithMarkupValue"));

                            }
                            catch (Exception e){
                                newOrdersPojoClass.tmcpriceperkgWithMarkupValue = "0";

                                e.printStackTrace();
                            }

                        }
                        else{
                            newOrdersPojoClass.tmcpriceperkgWithMarkupValue = "0";
                            //Log.d(Constants.TAG, "There is no key for this Menu: "  );


                        }
                        if(json.has("tmcpriceWithMarkupValue")){
                            try{
                                newOrdersPojoClass.tmcpriceWithMarkupValue = String.valueOf(json.get("tmcpriceWithMarkupValue"));

                            }
                            catch (Exception e){
                                newOrdersPojoClass.tmcpriceWithMarkupValue = "0";

                                e.printStackTrace();
                            }

                        }
                        else{
                            newOrdersPojoClass.tmcpriceWithMarkupValue = "0";
                            //Log.d(Constants.TAG, "There is no key for this Menu: "  );


                        }

                        newOrdersPojoClass.quantity = "";
                        //Log.d(TAG, "itemname of addMenuListAdaptertoListView: " + newOrdersPojoClass.portionsize);
                        MenuList.add(newOrdersPojoClass);

                        //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                        //Log.d(Constants.TAG, "e: " + e.getMessage());
                        //Log.d(Constants.TAG, "e: " + e.toString());

                    }


                }

                //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return MenuList;
    }

    public void add_amount_ForBillDetails() {
        try {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            for (String Key : cart_Item_List) {
                Modal_NewOrderItems newOrderItems = cartItem_hashmap.get(Key);

                //find total amount with out GST
                double new_total_amountfromArray = 0, discountpercentageDecimal = 0, discountPercentage = 0, newsavedAmount = 0, taxes_and_chargesfromArray = 0;
                if (newOrderItems != null) {
                    try {
                        String itemFinalPrice_string = String.valueOf(newOrderItems.getItemFinalPrice());
                        new_total_amountfromArray = Double.parseDouble(itemFinalPrice_string);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String discountPercentage_string;
                    try {
                        discountPercentage_string = String.valueOf(newOrderItems.getApplieddiscountpercentage());

                        discountPercentage = Double.parseDouble(discountPercentage_string);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);

                    try {
                        discountpercentageDecimal = (100 - discountPercentage) / 100;

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        newsavedAmount = new_total_amountfromArray / discountpercentageDecimal;

                    } catch (Exception e) {
                        e.printStackTrace();

                    }

                    try {
                        newOrderItems.setSavedAmount(String.valueOf(decimalFormat.format(newsavedAmount)));

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    try {
                        new_total_amount = new_total_amountfromArray;
                        old_total_Amount = old_total_Amount + new_total_amount;
                        //Log.i(TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
                        //Log.i(TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);


                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    try {
                        taxes_and_chargesfromArray = Double.parseDouble(newOrderItems.getGstpercentage());
                        //Log.i(TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);

                        taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    try {
                        newOrderItems.setGstAmount(String.valueOf(decimalFormat.format(taxes_and_chargesfromArray)));

                        //Log.i(TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
                        //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
                        //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
                        new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
                        old_taxes_and_charges_Amount = old_taxes_and_charges_Amount + new_taxes_and_charges_Amount;

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    //find total GST amount

                    try {
                        double subTotal_perItem = new_total_amount + new_taxes_and_charges_Amount;


                        newOrderItems.setSubTotal_perItem(String.valueOf(decimalFormat.format(subTotal_perItem)));


                        //find total payable Amount
                        new_to_pay_Amount = (old_total_Amount + old_taxes_and_charges_Amount);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);


                    double deliveryAmount_for_this_order_double = 0;
                    try {
                        deliveryAmount_for_this_order_double = Double.parseDouble(deliveryAmount_for_this_order);
                    } catch (Exception e) {
                        deliveryAmount_for_this_order_double = 0;
                        e.printStackTrace();
                    }
                    double discountAmount_for_this_order_double = 0;

                    try {
                        discountAmount_for_this_order_double = Double.parseDouble(discountAmount);
                    } catch (Exception e) {
                        discountAmount_for_this_order_double = 0;
                        e.printStackTrace();
                    }

                    try {
                        new_to_pay_Amount = (new_to_pay_Amount + deliveryAmount_for_this_order_double) - (discountAmount_for_this_order_double);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //find subtotal
                }
            }


            try {

                mobile_ItemTotal_textwidget.setText(decimalFormat.format(old_total_Amount));
                mobile_GST_textwidget.setText(decimalFormat.format(old_taxes_and_charges_Amount));
                new_totalAmount_withoutGst = (int) Math.round(old_total_Amount);
                newGst = (int) Math.round(old_taxes_and_charges_Amount);
                // new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);
                finaltoPayAmount = String.valueOf((int) Math.round(new_to_pay_Amount)) + ".00";
                mobile_ToPay_textwidget.setText(String.valueOf(finaltoPayAmount));
                //toPay_textWidget.setText(String.valueOf(finaltoPayAmount));

              //  Objects.requireNonNull(toPay_textWidget).setText(finaltoPayAmount);
              //  deliveryChargeTextWidget.setText(String.valueOf(deliveryAmount_for_this_order));
                totalAmounttopay = Double.parseDouble(finaltoPayAmount);
            } catch (Exception e) {
             //   e.printStackTrace();
            }

            old_total_Amount = 0;
            old_taxes_and_charges_Amount = 0;
            new_to_pay_Amount = 0;
        }
        catch (Exception e){

        }

    }


    private void getMenuItemStockAvlDetailsArrayAndMenuItemFromSharedPreferences() {
        final SharedPreferences sharedPreferencesMenuitem = mContext.getSharedPreferences("MenuItemStockAvlDetails", MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferencesMenuitem.getString("MenuItemStockAvlDetails", "");
        if (json.isEmpty()) {
            Toast.makeText(mContext, "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItemStockAvlDetails>>() {
            }.getType();
            MenuItemStockAvlDetails = gson.fromJson(json, type);
        }

   //     MenuItems=getData();

        //Log.i(TAG, "call adapter cart_Item " + getData());

      //  completemenuItem= getMenuItemfromString(MenuItems);
    }


    private void initAndCallingOrderPlacingModuleHandlerInterface(Context mContext, String Payment_mode, String discountAmount_stringGlobal, String orderplacedTime, String UserMobile, String ordertype, String vendorKey, String vendorName, long sTime, String payableAmount, Modal_Address selected_address_modal, String tokenNo, String userStatus, String customerName_string, boolean isinventorycheck, double newamountUserHaveAsCredit, double oldamountUserHaveAsCredit) {
        String taxAmount = String.valueOf(newGst+".00");
        JSONArray itemDespArray = new JSONArray();


        orderPlacingModuleHandler_interface = new OrderPlacingModuleHandler_Interface() {
            @Override
            public void notifySuccess(String requestType, String success) {
                try {

                    try {
                        if(defaultPrinterType.toUpperCase().equals(Constants.PDF_PrinterType)) {

                            GeneratePDF(orderplacedTime, UserMobile, tokenNo, taxAmount, taxAmount, finaltoPayAmountinmethod, String.valueOf(sTime), cart_Item_List, cartItem_hashmap, Payment_mode, discountAmount, ordertype, itemDespArray);

                        }
                        else{

                            if(shouldGetPrintNow_Global) {
                                printRecipt(orderplacedTime, UserMobile, tokenNo, taxAmount, taxAmount, finaltoPayAmountinmethod, String.valueOf(sTime), cart_Item_List, cartItem_hashmap, Payment_mode, discountAmount, ordertype, itemDespArray);
                            }
                            else{



                                    turnoffProgressBarAndResetArray();


                            }
                            isOrderPlacedinOrderdetails = true;
                            //  showProgressBar(false);
                        }



                    }
                    catch(Exception e ){
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    showProgressBar(false);
                    isOrderDetailsMethodCalled = false;

                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, String error) {

                isOrderDetailsMethodCalled = false;
                showProgressBar(false);
                Toast.makeText(mContext,"OrderDetails is not updated in DB",Toast.LENGTH_SHORT).show();

                turnoffProgressBarAndResetArray();
            }

        };

        orderPlacingModuleHandler_asyncTask = new OrderPlacingModuleHandler_AsyncTask(mContext, orderPlacingModuleHandler_interface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, Payment_mode, discountAmount, "0", orderplacedTime, UserMobile, ordertype, vendorKey, vendorName, sTime, payableAmount, isinventorycheck, user_key_toAdd_Address,redeemPointsJson , newamountUserHaveAsCredit , oldamountUserHaveAsCredit,selected_address_modal,tokenNo);

        orderPlacingModuleHandler_asyncTask.execute();


    }



    private void PlaceOrder_in_OrderDetails(List<String> cart_Item_List, String Payment_mode, long sTime, String finaltoPayAmountinmethod, boolean shouldGetPrintNow) {
        if(isOrderDetailsMethodCalled){
            return;
        }
        isOrderPlacedinOrderdetails=false;
            showProgressBar(true);
        isOrderDetailsMethodCalled = true;
        String newOrderId = String.valueOf(sTime);
        SharedPreferences sh
                = mContext.getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        double totalgrossweightingrams_doubleFromLoop = 0, totalgrossFromInsideAndOutsideLoop = 0;

        String merchantorderid = "";
        String couponid = "";
        String CouponDiscountAmount = discountAmount;
        String DeliveryAmount = "";

        String orderid = String.valueOf(sTime);
        String orderplacedTime = Currenttime;
        String tokenno = tokenNo;
        String userid = "";
        ordertype = Constants.POSORDER;
        String deliverytype = Constants.STOREPICKUP_DELIVERYTYPE;
        String slotdate = "";
        String slottimerange = "";
        String slotname = "";
        if(orderdetailsnewschema){
            slotname = "";

        }
        else{
             slotname = "EXPRESSDELIVERY";

        }
        if(isPhoneOrderSelected){
            ordertype = Constants.PhoneOrder;
            deliverytype = Constants.HOME_DELIVERY_DELIVERYTYPE;
            slotname = "EXPRESSDELIVERY";
            slottimerange ="120 mins";
            slotdate  = CurrentDate;
        }

        String orderPlacedDate = getDate();

        String UserMobile = "+91" + customermobileno;
        String vendorkey = sh.getString("VendorKey", "");
        String vendorName = sh.getString("VendorName", "");
        String itemTotalwithoutGst = String.valueOf(new_totalAmount_withoutGst)+ ".00";
       //String payableAmount = finaltoPayAmountinmethod;
        try {
            double payableamount_double = Double.parseDouble(finaltoPayAmountinmethod);

            if(payableamount_double<=0){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.print_again);
                            dialog.setTitle("Last Order is Not Placed .Please Try Again !!!! ");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);

                            Button printAgain = (Button) dialog.findViewById(R.id.printAgain);
                            printAgain.setText("OK");
                            TextView title = (TextView) dialog.findViewById(R.id.title);
                            title.setText("Last Order is Not Placed .Please Try Again !!!! ");
                            printAgain.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();


                                    customermobileno="";
                                    customerName ="";
                                    userAddressArrayList.clear();
                                     redeemPointsJson = new JSONObject();
                                    userAddressKeyArrayList.clear();
                                    selectedAddressKey = String.valueOf("");
                                    selectedAddress = String.valueOf("");
                                    userLatitude = String.valueOf("0");
                                    userLongitude = String.valueOf("0");
                                    deliveryDistance ="";
                                    user_key_toAdd_Address ="";

                                    fulladdress_textview.setText("");
                                    autoCompleteCustomerName_widget.setText("");

                                    selected_Address_modal = new Modal_Address();
                                    isPhoneOrderSelected = false;
                                      isAddressForPhoneOrderSelected = false;
                                    updateUserName = false;
                                    isNewUser = false;
                                    isAddress_Added_ForUser = false;
                                    isAddressForPhoneOrderSelected = false;
                                    isUsertype_AlreadyPhone = false;
                                    userFetchedManually = false;

                                    isStoreNumberSelected = false;
                                    orderTypeSpinner_newWidget.setSelection(0);
                                    cart_Item_List.clear();
                                    cartItem_hashmap.clear();
                                    ispaymentMode_Clicked = false;
                                    isOrderDetailsMethodCalled = false;
                                    isPhoneOrderSelected = false;
                                     isAddressForPhoneOrderSelected = false;

                                    isPaymentDetailsMethodCalled = false;
                                    isCustomerOrdersTableServiceCalled  = false;
                                    isOrderTrackingDetailsMethodCalled = false;
                                    isCustomerOrdersTableServiceCalled  = false;
                                    new_to_pay_Amount = 0;
                                    old_taxes_and_charges_Amount = 0;
                                    newGst =0;
                                    old_total_Amount = 0;
                                    createEmptyRowInListView("empty");
                                    CallAdapter();
                                    discountAmount ="0";
                                       deliveryAmount_for_this_order ="0";

                                    finaltoPayAmount = "0";
                                    new_totalAmount_withoutGst =0;
                                    isPrintedSecondTime = false;
                                    showProgressBar(false);
                                    isUpdateCouponTransactionMethodCalled=false;
                                    totalAmounttopay=0;
                                    isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                    finalamounttoPay=0;
                                  /*  ispointsApplied_redeemClicked=false;
                                    isProceedtoCheckoutinRedeemdialogClicked =false;
                                    isRedeemDialogboxOpened=false;
                                    isUpdateRedeemPointsMethodCalled=false;

                                    discount_rs_text_widget.setText(discountAmount);
                                    OrderTypefromSpinner =  Constants.POSORDER;
                                    orderTypeSpinner.setSelection(0);
                                    total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                    taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                    total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                    discount_Edit_widget.setText("0");
                                    mobileNo_Edit_widget.setText("");
                                    pointsalreadyredeemDouble=0;
                                    totalpointsuserhave_afterapplypoints=0;
                                    pointsenteredToredeem_double=0;
                                    pointsenteredToredeem="";

                                    finaltoPayAmountwithRedeemPoints="";
                                    redeemPoints_String="";
                                    redeemKey="";
                                    mobileno_redeemKey="";
                                    discountAmountalreadyusedtoday="";
                                    totalpointsredeemedalreadybyuser="";
                                    totalordervalue_tillnow="";
                                    totalredeempointsuserhave="";







                                    redeemed_points_text_widget.setText("");
                                    redeemPointsLayout.setVisibility(View.GONE);
                                    discountAmountLayout.setVisibility(View.VISIBLE);

                                     */
                                    return;

                                }
                            });


                            dialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                return;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        String taxAmount = String.valueOf(newGst+".00");
        PlaceOrder_in_PaymentTransactionDetails(sTime, Payment_mode, finaltoPayAmountinmethod, UserMobile);

        JSONArray itemDespArray = new JSONArray();

        for (int i = 0; i < cart_Item_List.size(); i++) {
            String itemUniqueCode = cart_Item_List.get(i);
            Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);

            String stockIncomingKey_AvlDetails = "";
            if (Objects.requireNonNull(modal_newOrderItems).getStockincomingkey_AvlDetails() != null) {
                stockIncomingKey_AvlDetails = modal_newOrderItems.getStockincomingkey_AvlDetails();
            } else {
                stockIncomingKey_AvlDetails = "";
            }

            String key_AvlDetails = "";
            if (Objects.requireNonNull(modal_newOrderItems).getKey_AvlDetails() != null) {
                key_AvlDetails = modal_newOrderItems.getKey_AvlDetails();
            } else {
                key_AvlDetails = "";
            }


            Log.d(Constants.TAG, "Request getStockincomingkey_AvlDetails: " + modal_newOrderItems.getStockincomingkey_AvlDetails());



            String menuItemKey = "";
            if (Objects.requireNonNull(modal_newOrderItems).getMenuItemId() != null) {
                menuItemKey = modal_newOrderItems.getMenuItemId();
            } else {
                menuItemKey = "";
                if (Objects.requireNonNull(modal_newOrderItems).getKey() != null) {
                    menuItemKey = modal_newOrderItems.getKey();
                } else {
                    menuItemKey = "";
                    if (Objects.requireNonNull(modal_newOrderItems).getMenuitemkey_AvlDetails() != null) {
                        menuItemKey = modal_newOrderItems.getMenuitemkey_AvlDetails();
                    } else {
                        menuItemKey = "";
                    }
                }
            }



            String receivedStock_AvlDetails = "";
            if (Objects.requireNonNull(modal_newOrderItems).getReceivedstock_AvlDetails() != null) {
                receivedStock_AvlDetails = modal_newOrderItems.getReceivedstock_AvlDetails();
            } else {
                receivedStock_AvlDetails = "";
            }

            String grossweight = "";
            if (modal_newOrderItems.getGrossweight() != null) {
                grossweight = modal_newOrderItems.getGrossweight();

            }

            String grossWeightingrams = "";
            try {
                if (!grossweight.equals("")) {
                    grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String ItemUniquecodeofItem = "";
            if ((modal_newOrderItems.getItemuniquecode() != null) && (!modal_newOrderItems.getItemuniquecode().equals("null")) && (!modal_newOrderItems.getItemuniquecode().equals(""))) {
                ItemUniquecodeofItem = String.valueOf(modal_newOrderItems.getItemuniquecode());
            } else {
                ItemUniquecodeofItem = "";
            }
            double grossweightingrams_double = 0;
            try {
                if (!grossWeightingrams.equals("")) {
                    grossweightingrams_double = Double.parseDouble(grossWeightingrams);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String quantity = "";
            if (modal_newOrderItems.getQuantity() != null) {
                quantity = modal_newOrderItems.getQuantity();
                ;

            } else {
                quantity = "";
            }


            double quantityDouble = 0;
            try {
                if (quantity.equals("")) {
                    quantity = "1";
                }
                quantityDouble = Double.parseDouble(quantity);
            } catch (Exception e) {
                e.printStackTrace();
            }

            double grossWeightWithQuantity_double = 0;
            if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICE)) {
                try {
                    grossWeightWithQuantity_double = quantityDouble;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (modal_newOrderItems.getPricetypeforpos().toUpperCase().equals(Constants.TMCPRICEPERKG)) {
                try {
                    grossWeightWithQuantity_double = grossweightingrams_double * quantityDouble;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            String barcode = "";
            if (modal_newOrderItems.getBarcode() != null) {
                barcode = String.valueOf(modal_newOrderItems.getBarcode());
            } else {
                barcode = "";
            }


            String priceTypeForPOS = "";
            if (modal_newOrderItems.getPricetypeforpos() != null) {
                priceTypeForPOS = String.valueOf(modal_newOrderItems.getPricetypeforpos());
            } else {
                priceTypeForPOS = "";
            }


            String tmcCtgy = "";
            if (modal_newOrderItems.getTmcctgykey() != null) {
                tmcCtgy = String.valueOf(modal_newOrderItems.getTmcctgykey());
            } else {
                tmcCtgy = "";
            }


            String tmcSubCtgyKey = "";
            if (modal_newOrderItems.getTmcsubctgykey() != null) {
                tmcSubCtgyKey = String.valueOf(modal_newOrderItems.getTmcsubctgykey());
            } else {
                tmcSubCtgyKey = "";
            }


            String itemName =
                    String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());

            if (itemName.contains("Grill House")) {
                itemName = itemName.replace("Grill House ", "");
            } else if (itemName.contains("Ready to Cook")) {
                itemName = itemName.replace("Ready to Cook", "");
            } else {
                itemName = itemName;
            }
//////////////

            /*
            if(isinventorycheck){

            String inventoryDetails = "";
            if ((modal_newOrderItems.getInventorydetails() != null) && (!modal_newOrderItems.getInventorydetails().equals("null")) && (!modal_newOrderItems.getInventorydetails().equals(""))) {
                inventoryDetails = String.valueOf(modal_newOrderItems.getInventorydetails());
            } else {
                inventoryDetails = "nil";
            }


            boolean allowNegativeStock = false;
            if ((modal_newOrderItems.getAllownegativestock() != null) && (!modal_newOrderItems.getAllownegativestock().equals("null")) && (!modal_newOrderItems.getAllownegativestock().equals(""))) {
                allowNegativeStock = Boolean.parseBoolean(modal_newOrderItems.getAllownegativestock().toUpperCase());
            } else {
                allowNegativeStock = false;
            }


            boolean isitemAvailable = false;
            if ((modal_newOrderItems.getItemavailability_AvlDetails() != null) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals("null")) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals(""))) {
                isitemAvailable = Boolean.parseBoolean(modal_newOrderItems.getItemavailability_AvlDetails().toUpperCase());
            } else {
                isitemAvailable = false;
            }
                if (!StockBalanceChangedForThisItemList.contains(ItemUniquecodeofItem)) {

                    totalgrossweightingrams_doubleFromLoop = 0;
                    totalgrossFromInsideAndOutsideLoop = 0;
                    isStockOutGoingAlreadyCalledForthisItem = false;

                    for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                        String hashmapkey = "";
                        hashmapkey = cart_Item_List.get(iterator);
                        //for (Map.Entry<String, Modal_NewOrderItems> cartItem_hashmapData : cartItem_hashmap.get(hashmapkey)) {
                        String menuItemKeyFromInventoryDetails_secondItem = "";
                        String menuItemKeyFromInventoryDetails_firstItem = "";

                        Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                        String ItemUniquecodeFromLoop = "", BarcodeFromLoop = "", grossWeightingrams_FromLoop = "", grossweight_FromLoop = "", quantityFromLoop = "", priceTypeofItemFromLoop = "";
                        double quantityDoubleFromLoop = 0, grossweightingrams_doubleFromLoop = 0;

                        try {
                            ItemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode();
                            BarcodeFromLoop = modal_newOrderItems_insideLoop.getBarcode().toString();
                            priceTypeofItemFromLoop = modal_newOrderItems_insideLoop.getPricetypeforpos().toString();
                            //   if (!BarcodeFromLoop.equals(BarcodeofItem)) {


                            if (ItemUniquecodeofItem.equals(ItemUniquecodeFromLoop)) {

                                try {
                                    grossweight_FromLoop = "0";
                                    if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                        grossweight_FromLoop = modal_newOrderItems_insideLoop.getGrossweight();

                                    } else {
                                        grossweight_FromLoop = "0";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    quantityFromLoop = "0";

                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                        quantityFromLoop = modal_newOrderItems_insideLoop.getQuantity();

                                    } else {
                                        quantityFromLoop = "0";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (!grossweight_FromLoop.equals("")) {
                                        grossWeightingrams_FromLoop = grossweight_FromLoop.replaceAll("[^\\d.]", "");

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                grossweightingrams_doubleFromLoop = 0;
                                try {
                                    if (!grossWeightingrams_FromLoop.equals("")) {
                                        grossweightingrams_doubleFromLoop = Double.parseDouble(grossWeightingrams_FromLoop);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {
                                    if (quantityFromLoop.equals("")) {
                                        quantityFromLoop = "1";
                                    }
                                    quantityDoubleFromLoop = Double.parseDouble(quantityFromLoop);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                double grossWeightWithQuantity_doubleFromLoop = 0;
                                try {
                                    grossWeightWithQuantity_doubleFromLoop = grossweightingrams_doubleFromLoop * quantityDoubleFromLoop;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_doubleFromLoop;

                                StockBalanceChangedForThisItemList.add(ItemUniquecodeFromLoop);


                                isStockOutGoingAlreadyCalledForthisItem = true;


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    if (inventoryDetails.equals("nil")) {

                        getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);
                    }
                    else {
                        try {
                            JSONArray jsonArray = new JSONArray(String.valueOf(inventoryDetails));
                            int jsonArrayIterator = 0;
                            int jsonArrayCount = jsonArray.length();
                            for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {

                                try {
                                    JSONObject json_InventoryDetails = jsonArray.getJSONObject(jsonArrayIterator);
                                    String menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");
                                    for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItemArray.size(); iterator_menuitemStockAvlDetails++) {

                                        Modal_MenuItem modal_menuItemStockAvlDetails = MenuItemArray.get(iterator_menuitemStockAvlDetails);

                                        String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getKey());

                                        if (menuItemKeyFromInventoryDetails.equals(menuItemKeyFromMenuAvlDetails)) {


                                            ////////for same inventoryDetails Item - starting

                                            for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                                                String hashmapkey = "";
                                                hashmapkey = cart_Item_List.get(iterator);
                                                String itemUniquecodeFromLoop = "", inventoryDetails_secondItem = "", priceTypeOfPOS = "";
                                                String menuItemKeyFromInventoryDetails_secondItem = "";

                                                Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                                                try {
                                                    itemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode().toString();

                                                } catch (Exception e) {
                                                    itemUniquecodeFromLoop = "";
                                                    e.printStackTrace();
                                                }


                                                if (!itemUniquecodeFromLoop.equals(itemUniqueCode)) {
                                                    try {
                                                        inventoryDetails_secondItem = modal_newOrderItems_insideLoop.getInventorydetails().toString();
                                                    } catch (Exception e) {
                                                        inventoryDetails_secondItem = "nil";
                                                        e.printStackTrace();
                                                    }
                                                    if (!inventoryDetails_secondItem.equals("nil")) {


                                                        try {
                                                            JSONArray jsonArray_secondItem = new JSONArray(String.valueOf(inventoryDetails_secondItem));
                                                            int jsonArrayIterator_secondItem = 0;
                                                            int jsonArrayCount_secondItem = jsonArray_secondItem.length();
                                                            String grossweightinGramsFromInventoryDetails_secondItem, netweightingramsFromInventoryDetails_secondItem;

                                                            for (; jsonArrayIterator_secondItem < (jsonArrayCount_secondItem); jsonArrayIterator_secondItem++) {
                                                                grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                netweightingramsFromInventoryDetails_secondItem = "0";

                                                                try {
                                                                    JSONObject json_InventoryDetails_secondItem = jsonArray_secondItem.getJSONObject(jsonArrayIterator_secondItem);
                                                                    menuItemKeyFromInventoryDetails_secondItem = "";
                                                                       //grossweightinGramsFromInventoryDetails = 0;
                                                                      //netweightingramsFromInventoryDetails = 0;

                                                                    try {
                                                                        menuItemKeyFromInventoryDetails_secondItem = json_InventoryDetails_secondItem.getString("menuitemkey");
                                                                    } catch (Exception e) {
                                                                        menuItemKeyFromInventoryDetails_secondItem = "";
                                                                        e.printStackTrace();
                                                                    }


                                                                    try {
                                                                        if (json_InventoryDetails_secondItem.has("grossweightingrams")) {
                                                                            grossweightinGramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("grossweightingrams"));
                                                                        } else {

                                                                            grossweightinGramsFromInventoryDetails_secondItem = "0";

                                                                        }
                                                                    } catch (Exception e) {
                                                                        grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                        e.printStackTrace();
                                                                    }


                                                                    try {
                                                                        netweightingramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("netweightingrams"));
                                                                    } catch (Exception e) {
                                                                        netweightingramsFromInventoryDetails_secondItem = "0";
                                                                        e.printStackTrace();
                                                                    }


                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                                if (menuItemKeyFromInventoryDetails_secondItem.equals(menuItemKeyFromMenuAvlDetails)) {
                                                                    try {
                                                                        priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                                    } catch (Exception e) {
                                                                        priceTypeOfPOS = "";
                                                                        e.printStackTrace();
                                                                    }

                                                                    try {
                                                                        if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {

                                                                            String grossweight_secondItemInventoryDetails = "";

                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            try {
                                                                                grossweight_secondItemInventoryDetails = grossweightinGramsFromInventoryDetails_secondItem;

                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                                grossweight_secondItemInventoryDetails = "0";
                                                                            }

                                                                            try {
                                                                                if (grossweight_secondItemInventoryDetails.equals("") || (grossweight_secondItemInventoryDetails.equals("0"))) {
                                                                                    try {
                                                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight().toString();

                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                        grossweight_secondItemInventoryDetails = "1";
                                                                                    }
                                                                                }
                                                                            } catch (Exception e) {
                                                                                grossweight_secondItemInventoryDetails = "1";

                                                                                e.printStackTrace();
                                                                            }


                                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                                            try {
                                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                            try {
                                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            StockBalanceChangedForThisItemList.add(itemUniquecodeFromLoop);


                                                                            isStockOutGoingAlreadyCalledForthisItem = true;


                                                                        } else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                                            String grossweight_secondItemInventoryDetails = "";

                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                                            } else {
                                                                                grossweight_secondItemInventoryDetails = "";
                                                                            }


                                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                                            try {
                                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                            try {
                                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            StockBalanceChangedForThisItemList.add(itemUniquecodeFromLoop);


                                                                            isStockOutGoingAlreadyCalledForthisItem = true;
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                }
                                            }
                                            ////////for same inventoryDetails Item - Ending


                                            String stockIncomingKey_avlDetail = "", Key_avlDetail = "", receivedStock_avlDetail = "", grossWeight_avlDetail_InventoryDetails = "", itemName_avlDetail_inventoryDetails = "", barcode_avlDetail = "", priceTypeForPOS_avlDetail = "",
                                                    tmcSubCtgy_avlDetail = "", tmcCtgy_avlDetail = "";
                                            double grossWeightWithQuantityDouble_avlDetail_InventoryDetails = 0, grossWeightDouble_avlDetail_InventoryDetails = 0;

                                            try {
                                                stockIncomingKey_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getStockincomingkey_AvlDetails());
                                            } catch (Exception e) {
                                                stockIncomingKey_avlDetail = "nil";
                                                e.printStackTrace();
                                            }


                                            try {
                                                Key_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getKey_AvlDetails());

                                            } catch (Exception e) {
                                                Key_avlDetail = "";
                                                e.printStackTrace();
                                            }

                                            try {
                                                barcode_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getBarcode_AvlDetails());

                                            } catch (Exception e) {
                                                barcode_avlDetail = "";
                                                e.printStackTrace();
                                            }

                                            try {
                                                receivedStock_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getReceivedstock_AvlDetails());

                                            } catch (Exception e) {
                                                receivedStock_avlDetail = "";

                                                e.printStackTrace();
                                            }


                                            try {
                                                itemName_avlDetail_inventoryDetails = String.valueOf(modal_menuItemStockAvlDetails.getItemname());

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            try {
                                                tmcSubCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcsubctgykey());

                                            } catch (Exception e) {
                                                tmcSubCtgy_avlDetail = "";
                                                e.printStackTrace();
                                            }


                                            try {
                                                tmcCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcctgykey());

                                            } catch (Exception e) {
                                                tmcCtgy_avlDetail = "";
                                                e.printStackTrace();
                                            }


                                            try {
                                                priceTypeForPOS_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getPricetypeforpos());

                                            } catch (Exception e) {
                                                priceTypeForPOS_avlDetail = "";
                                                e.printStackTrace();
                                            }

//interal closing starts
                                            if (json_InventoryDetails.has("grossweightingrams")) {
                                                try {
                                                    grossWeight_avlDetail_InventoryDetails = String.valueOf(json_InventoryDetails.getString("grossweightingrams"));

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    grossWeightDouble_avlDetail_InventoryDetails = Double.parseDouble(grossWeight_avlDetail_InventoryDetails);
                                                } catch (Exception e) {
                                                    grossWeightDouble_avlDetail_InventoryDetails = 0;
                                                    e.printStackTrace();
                                                }

                                                try {
                                                    grossWeightWithQuantityDouble_avlDetail_InventoryDetails = grossWeightDouble_avlDetail_InventoryDetails * quantityDouble;
                                                } catch (Exception e) {
                                                    grossWeightWithQuantityDouble_avlDetail_InventoryDetails = 0;
                                                    e.printStackTrace();
                                                }


                                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantityDouble_avlDetail_InventoryDetails, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, isitemAvailable, allowNegativeStock);

                                            } else {
                                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantity_double, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, isitemAvailable, allowNegativeStock);


                                            }

 //internal closing ends





                                                if (isStockOutGoingAlreadyCalledForthisItem) {
                                                    //  try {
                                                    //    totalgrossFromInsideAndOutsideLoop = grossWeightWithQuantity_double + totalgrossweightingrams_doubleFromLoop;
                                                    // } catch (Exception e) {
                                                    //      e.printStackTrace();
                                                    //  }


                                                    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, totalgrossweightingrams_doubleFromLoop, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, isitemAvailable, allowNegativeStock);


                                                } else {
                                                    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantity_double, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, isitemAvailable, allowNegativeStock);

                                                }





                                        }


                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
        }
             */

            //////////////////////
            if(isinventorycheck) {


                String inventoryDetails_firstItem = "";
                try {
                    if ((modal_newOrderItems.getInventorydetails() != null) && (!modal_newOrderItems.getInventorydetails().equals("null")) && (!modal_newOrderItems.getInventorydetails().equals(""))) {
                        inventoryDetails_firstItem = String.valueOf(modal_newOrderItems.getInventorydetails());
                    } else {
                        inventoryDetails_firstItem = "nil";
                    }
                } catch (Exception e) {
                    inventoryDetails_firstItem = "nil";

                    e.printStackTrace();
                }


                boolean allowNegativeStock = false;
                if ((modal_newOrderItems.getAllownegativestock() != null) && (!modal_newOrderItems.getAllownegativestock().equals("null")) && (!modal_newOrderItems.getAllownegativestock().equals(""))) {
                    allowNegativeStock = Boolean.parseBoolean(modal_newOrderItems.getAllownegativestock().toUpperCase());
                } else {
                    allowNegativeStock = false;
                }


                boolean isitemAvailable = false;
                if ((modal_newOrderItems.getItemavailability_AvlDetails() != null) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals("null")) && (!modal_newOrderItems.getItemavailability_AvlDetails().equals(""))) {
                    isitemAvailable = Boolean.parseBoolean(modal_newOrderItems.getItemavailability_AvlDetails().toUpperCase());
                } else {
                    isitemAvailable = false;
                }



                String BarcodeofItem = "";
                if ((modal_newOrderItems.getBarcode() != null) && (!modal_newOrderItems.getBarcode().equals("null")) && (!modal_newOrderItems.getBarcode().equals(""))) {
                    BarcodeofItem = String.valueOf(modal_newOrderItems.getBarcode());
                } else {
                    BarcodeofItem = "";
                }

                String priceTypeOfItem = "";
                if ((modal_newOrderItems.getPricetypeforpos() != null) && (!modal_newOrderItems.getPricetypeforpos().equals("null")) && (!modal_newOrderItems.getPricetypeforpos().equals(""))) {
                    priceTypeOfItem = String.valueOf(modal_newOrderItems.getPricetypeforpos());
                } else {
                    priceTypeOfItem = "";
                }






                if (!StockBalanceChangedForThisItemList.contains(menuItemKey)) {
                    totalgrossweightingrams_doubleFromLoop = 0;
                    totalgrossFromInsideAndOutsideLoop = 0;
                    isStockOutGoingAlreadyCalledForthisItem = false;

                    try {
                        //  ItemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode();
                        //  BarcodeFromLoop = modal_newOrderItems_insideLoop.getBarcode().toString();
                        //  priceTypeofItemFromLoop = modal_newOrderItems_insideLoop.getPricetypeforpos().toString();
                        //   if (!BarcodeFromLoop.equals(BarcodeofItem)) {
                        if (inventoryDetails_firstItem.equals("nil")) {


                            ////////for same inventoryDetails Item - starting

                            for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                                String hashmapkey = "";
                                hashmapkey = cart_Item_List.get(iterator);
                                String itemUniquecodeFromLoop = "", inventoryDetails_secondItem = "", priceTypeOfPOS = "";
                                String menuItemKeyFromInventoryDetails_secondItem = "",menuItemKeyFrom_secondItem="";

                                Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                                try {
                                    itemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode().toString();

                                } catch (Exception e) {
                                    itemUniquecodeFromLoop = "";
                                    e.printStackTrace();
                                }
                                try {
                                    menuItemKeyFrom_secondItem = modal_newOrderItems_insideLoop.getMenuItemId().toString();

                                } catch (Exception e) {
                                    menuItemKeyFrom_secondItem = "";
                                    e.printStackTrace();
                                }

                                try {
                                    inventoryDetails_secondItem = modal_newOrderItems_insideLoop.getInventorydetails().toString();

                                } catch (Exception e) {
                                    inventoryDetails_secondItem = "";
                                    e.printStackTrace();
                                }

                                try {

                                    if (inventoryDetails_secondItem.equals("nil")) {


                                        if (menuItemKeyFrom_secondItem.equals(menuItemKey)) {

                                            try {
                                                priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                            } catch (Exception e) {
                                                priceTypeOfPOS = "";
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {


                                                    String quantity_secondItemInventoryDetails = "";
                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                    } else {
                                                        quantity_secondItemInventoryDetails = "";
                                                    }






                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                    try {
                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                            quantity_secondItemInventoryDetails = "1";
                                                        }
                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }




                                                    try {
                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + quantityDouble_secondItemInventoryDetails;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    //     StockBalanceChangedForThisItemList.add(menuItemKey);


                                                    //  isStockOutGoingAlreadyCalledForthisItem = true;


                                                }
                                                else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                    String grossweight_secondItemInventoryDetails = "";

                                                    String quantity_secondItemInventoryDetails = "";
                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                    } else {
                                                        quantity_secondItemInventoryDetails = "";
                                                    }


                                                    if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                    } else {
                                                        grossweight_secondItemInventoryDetails = "";
                                                    }


                                                    String grossWeightingrams_secondItemInventoryDetails = "";
                                                    try {
                                                        if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                            grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                    try {
                                                        if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                            grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                    try {
                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                            quantity_secondItemInventoryDetails = "1";
                                                        }
                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                    try {
                                                        grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    //  StockBalanceChangedForThisItemList.add(menuItemKey);


                                                    //isStockOutGoingAlreadyCalledForthisItem = true;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }



                                    }
                                    else {

                                        try{
                                            JSONArray jsonArray_secondItem = new JSONArray(String.valueOf(inventoryDetails_secondItem));
                                            int jsonArrayIterator_secondItem = 0;
                                            int jsonArrayCount_secondItem = jsonArray_secondItem.length();
                                            String grossweightinGramsFromInventoryDetails_secondItem, netweightingramsFromInventoryDetails_secondItem,menuitemKeyFromInventoryDetails_secondItem;

                                            for (; jsonArrayIterator_secondItem < (jsonArrayCount_secondItem); jsonArrayIterator_secondItem++) {
                                                grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                netweightingramsFromInventoryDetails_secondItem = "0";
                                                try {
                                                    JSONObject json_InventoryDetails_secondItem = jsonArray_secondItem.getJSONObject(jsonArrayIterator_secondItem);
                                                    menuItemKeyFromInventoryDetails_secondItem = "";
                                                    // grossweightinGramsFromInventoryDetails = 0;
                                                    //netweightingramsFromInventoryDetails = 0;

                                                    try {
                                                        menuItemKeyFromInventoryDetails_secondItem = json_InventoryDetails_secondItem.getString("menuitemkey");
                                                    } catch (Exception e) {
                                                        menuItemKeyFromInventoryDetails_secondItem = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        if (json_InventoryDetails_secondItem.has("grossweightingrams")) {
                                                            grossweightinGramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("grossweightingrams"));
                                                        } else {

                                                            grossweightinGramsFromInventoryDetails_secondItem = "0";

                                                        }
                                                    } catch (Exception e) {
                                                        grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        netweightingramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("netweightingrams"));
                                                    } catch (Exception e) {
                                                        netweightingramsFromInventoryDetails_secondItem = "0";
                                                        e.printStackTrace();
                                                    }


                                                }
                                                catch (Exception e) {
                                                    e.printStackTrace();
                                                }



                                                if (menuItemKeyFromInventoryDetails_secondItem.equals(menuItemKey)) {

                                                    try {
                                                        priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                    } catch (Exception e) {
                                                        priceTypeOfPOS = "";
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {

                                                            String grossweight_secondItemInventoryDetails = "";

                                                            String quantity_secondItemInventoryDetails = "";
                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                            } else {
                                                                quantity_secondItemInventoryDetails = "";
                                                            }
                                                            System.out.println(String.valueOf(quantity_secondItemInventoryDetails)+" resaa quantity_secondItemInventoryDetails  " );


                                                            try {
                                                                grossweight_secondItemInventoryDetails = grossweightinGramsFromInventoryDetails_secondItem;

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                grossweight_secondItemInventoryDetails = "0";
                                                            }

                                                            try {
                                                                if (grossweight_secondItemInventoryDetails.equals("") || (grossweight_secondItemInventoryDetails.equals("0"))) {
                                                                    try {
                                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight().toString();

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                        grossweight_secondItemInventoryDetails = "1";
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                grossweight_secondItemInventoryDetails = "1";

                                                                e.printStackTrace();
                                                            }


                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                            try {
                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                    quantity_secondItemInventoryDetails = "1";
                                                                }
                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                            try {
                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            System.out.println(String.valueOf(grossWeightWithQuantity_double_secondItemInventoryDetails)+" resaa grossWeightWithQuantity_double_secondItemInventoryDetails  ");
                                                            System.out.println(String.valueOf(quantityDouble_secondItemInventoryDetails)+" resaa quantityDouble_secondItemInventoryDetails " );

                                                            try {
                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            System.out.println(String.valueOf(totalgrossweightingrams_doubleFromLoop)+" resaa totalgrossweightingrams_doubleFromLoop  " );


                                                            //   StockBalanceChangedForThisItemList.add(menuItemKey);


                                                            //    isStockOutGoingAlreadyCalledForthisItem = true;


                                                        } else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                            String grossweight_secondItemInventoryDetails = "";

                                                            String quantity_secondItemInventoryDetails = "";
                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                            } else {
                                                                quantity_secondItemInventoryDetails = "";
                                                            }


                                                            if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                            } else {
                                                                grossweight_secondItemInventoryDetails = "";
                                                            }


                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                            try {
                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                            try {
                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                    quantity_secondItemInventoryDetails = "1";
                                                                }
                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                            try {
                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }


                                                            try {
                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            //StockBalanceChangedForThisItemList.add(menuItemKey);


                                                            //  isStockOutGoingAlreadyCalledForthisItem = true;
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }



                                            }


                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }

                            ////////for same inventoryDetails Item - Ending

                            StockBalanceChangedForThisItemList.add(menuItemKey);


                            isStockOutGoingAlreadyCalledForthisItem = true;

                            if (isStockOutGoingAlreadyCalledForthisItem) {
                                //  try {
                                // totalgrossFromInsideAndOutsideLoop = grossWeightWithQuantity_double + totalgrossweightingrams_doubleFromLoop;
                                // } catch (Exception e) {
                                //       e.printStackTrace();
                                //  }


                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, totalgrossweightingrams_doubleFromLoop, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                            } else {
                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                            }




                        }
                        else{
                            try{
                                JSONArray jsonArray = new JSONArray(String.valueOf(inventoryDetails_firstItem));
                                int jsonArrayIterator = 0;
                                int jsonArrayCount = jsonArray.length();
                                for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {
                                    try {
                                        String menuItemKeyFromInventoryDetails = "";
                                        double grossweightinGramsFromInventoryDetails = 0, netweightingramsFromInventoryDetails = 0;
                                        JSONObject json_InventoryDetails = jsonArray.getJSONObject(jsonArrayIterator);

                                        try {
                                            menuItemKeyFromInventoryDetails = json_InventoryDetails.getString("menuitemkey");
                                        } catch (Exception e) {
                                            menuItemKeyFromInventoryDetails = "";
                                            e.printStackTrace();
                                        }


                                        try {
                                            grossweightinGramsFromInventoryDetails = Double.parseDouble(json_InventoryDetails.getString("grossweightingrams"));
                                        } catch (Exception e) {
                                            grossweightinGramsFromInventoryDetails = 0;
                                            // e.printStackTrace();
                                        }


                                        try {
                                            netweightingramsFromInventoryDetails = Double.parseDouble(json_InventoryDetails.getString("netweightingrams"));
                                        } catch (Exception e) {
                                            netweightingramsFromInventoryDetails = 0;
                                            // e.printStackTrace();
                                        }
                                        if (!StockBalanceChangedForThisItemList.contains(menuItemKeyFromInventoryDetails)) {
                                            totalgrossweightingrams_doubleFromLoop = 0;
                                            ////////for same inventoryDetails Item - starting
                                            for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItemArray.size(); iterator_menuitemStockAvlDetails++) {

                                                Modal_MenuItem modal_menuItemStockAvlDetails = MenuItemArray.get(iterator_menuitemStockAvlDetails);

                                                String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getKey());
                                                String itemUniquecodeFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getItemuniquecode());
                                                String priceTypeForPOSFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getPricetypeforpos());
                                                String stockIncomingKey_avlDetail = "", Key_avlDetail = "", receivedStock_avlDetail = "", grossWeight_avlDetail_InventoryDetails = "", itemName_avlDetail_inventoryDetails = "", barcode_avlDetail = "", priceTypeForPOS_avlDetail = "",
                                                        tmcSubCtgy_avlDetail = "", tmcCtgy_avlDetail = "";

                                                if (menuItemKeyFromInventoryDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                                    double grossWeightWithQuantityDouble_avlDetail_InventoryDetails = 0, grossWeightDouble_avlDetail_InventoryDetails = 0;

                                                    try {
                                                        stockIncomingKey_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getStockincomingkey_AvlDetails());
                                                    } catch (Exception e) {
                                                        stockIncomingKey_avlDetail = "nil";
                                                        e.printStackTrace();
                                                    }
                                                    boolean itemAvailability_avlDetail = true;

                                                    try {
                                                        itemAvailability_avlDetail = Boolean.parseBoolean(String.valueOf(modal_menuItemStockAvlDetails.getItemavailability_AvlDetails()));
                                                    } catch (Exception e) {
                                                        itemAvailability_avlDetail = true;
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        Key_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getKey_AvlDetails());

                                                    } catch (Exception e) {
                                                        Key_avlDetail = "";
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        barcode_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getBarcode_AvlDetails());

                                                    } catch (Exception e) {
                                                        barcode_avlDetail = "";
                                                        e.printStackTrace();
                                                    }

                                                    try {
                                                        receivedStock_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getReceivedstock_AvlDetails());

                                                    } catch (Exception e) {
                                                        receivedStock_avlDetail = "";

                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        itemName_avlDetail_inventoryDetails = String.valueOf(modal_menuItemStockAvlDetails.getItemname());

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        tmcSubCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcsubctgykey());

                                                    } catch (Exception e) {
                                                        tmcSubCtgy_avlDetail = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        tmcCtgy_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getTmcctgykey());

                                                    } catch (Exception e) {
                                                        tmcCtgy_avlDetail = "";
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        priceTypeForPOS_avlDetail = String.valueOf(modal_menuItemStockAvlDetails.getPricetypeforpos());

                                                    } catch (Exception e) {
                                                        priceTypeForPOS_avlDetail = "";
                                                        e.printStackTrace();
                                                    }
                                                    for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                                                        String hashmapkey = "";
                                                        hashmapkey = cart_Item_List.get(iterator);
                                                        String itemUniquecodeFromLoop = "", inventoryDetails_secondItem = "", priceTypeOfPOS = "";
                                                        String menuItemKeyFromInventoryDetails_secondItem = "", menuItemKeyFrom_secondItem = "";

                                                        Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                                                        try {
                                                            itemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode().toString();

                                                        } catch (Exception e) {
                                                            itemUniquecodeFromLoop = "";
                                                            e.printStackTrace();
                                                        }
                                                        try {
                                                            menuItemKeyFrom_secondItem = modal_newOrderItems_insideLoop.getMenuItemId().toString();

                                                        } catch (Exception e) {
                                                            menuItemKeyFrom_secondItem = "";
                                                            e.printStackTrace();
                                                        }

                                                        try {
                                                            inventoryDetails_secondItem = modal_newOrderItems_insideLoop.getInventorydetails().toString();

                                                        } catch (Exception e) {
                                                            inventoryDetails_secondItem = "";
                                                            e.printStackTrace();
                                                        }

                                                        try {

                                                            if (inventoryDetails_secondItem.equals("nil")) {


                                                                if (menuItemKeyFrom_secondItem.equals(menuItemKeyFromInventoryDetails)) {

                                                                    try {
                                                                        priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                                    } catch (Exception e) {
                                                                        priceTypeOfPOS = "";
                                                                        e.printStackTrace();
                                                                    }

                                                                    try {
                                                                        if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {


                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            //  StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                            // isStockOutGoingAlreadyCalledForthisItem = true;


                                                                        }
                                                                        else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                                            String grossweight_secondItemInventoryDetails = "";

                                                                            String quantity_secondItemInventoryDetails = "";
                                                                            if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                            } else {
                                                                                quantity_secondItemInventoryDetails = "";
                                                                            }


                                                                            if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                                            } else {
                                                                                grossweight_secondItemInventoryDetails = "";
                                                                            }


                                                                            String grossWeightingrams_secondItemInventoryDetails = "";
                                                                            try {
                                                                                if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                    grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                    grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double quantityDouble_secondItemInventoryDetails = 0;
                                                                            try {
                                                                                if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                    quantity_secondItemInventoryDetails = "1";
                                                                                }
                                                                                quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                            try {
                                                                                grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            //   StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                            // isStockOutGoingAlreadyCalledForthisItem = true;
                                                                        }
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }


                                                            }
                                                            else {

                                                                try {
                                                                    JSONArray jsonArray_secondItem = new JSONArray(String.valueOf(inventoryDetails_secondItem));
                                                                    int jsonArrayIterator_secondItem = 0;
                                                                    int jsonArrayCount_secondItem = jsonArray_secondItem.length();
                                                                    String grossweightinGramsFromInventoryDetails_secondItem, netweightingramsFromInventoryDetails_secondItem, menuitemKeyFromInventoryDetails_secondItem;

                                                                    for (; jsonArrayIterator_secondItem < (jsonArrayCount_secondItem); jsonArrayIterator_secondItem++) {
                                                                        grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                        netweightingramsFromInventoryDetails_secondItem = "0";
                                                                        try {
                                                                            JSONObject json_InventoryDetails_secondItem = jsonArray_secondItem.getJSONObject(jsonArrayIterator_secondItem);
                                                                            menuItemKeyFromInventoryDetails_secondItem = "";
                                                                            // grossweightinGramsFromInventoryDetails = 0;
                                                                            netweightingramsFromInventoryDetails = 0;

                                                                            try {
                                                                                menuItemKeyFromInventoryDetails_secondItem = json_InventoryDetails_secondItem.getString("menuitemkey");
                                                                            } catch (Exception e) {
                                                                                menuItemKeyFromInventoryDetails_secondItem = "";
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                if (json_InventoryDetails_secondItem.has("grossweightingrams")) {
                                                                                    grossweightinGramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("grossweightingrams"));
                                                                                } else {

                                                                                    grossweightinGramsFromInventoryDetails_secondItem = "0";

                                                                                }
                                                                            } catch (Exception e) {
                                                                                grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                                e.printStackTrace();
                                                                            }


                                                                            try {
                                                                                netweightingramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("netweightingrams"));
                                                                            } catch (Exception e) {
                                                                                netweightingramsFromInventoryDetails_secondItem = "0";
                                                                                //  e.printStackTrace();
                                                                            }


                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }


                                                                        if (menuItemKeyFromInventoryDetails_secondItem.equals(menuItemKeyFromInventoryDetails)) {

                                                                            try {
                                                                                priceTypeOfPOS = modal_newOrderItems_insideLoop.getPricetypeforpos().toString().toUpperCase();
                                                                            } catch (Exception e) {
                                                                                priceTypeOfPOS = "";
                                                                                e.printStackTrace();
                                                                            }

                                                                            try {
                                                                                if (priceTypeOfPOS.equals(Constants.TMCPRICE)) {

                                                                                    String grossweight_secondItemInventoryDetails = "";

                                                                                    String quantity_secondItemInventoryDetails = "";
                                                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                                    } else {
                                                                                        quantity_secondItemInventoryDetails = "";
                                                                                    }


                                                                                    try {
                                                                                        grossweight_secondItemInventoryDetails = grossweightinGramsFromInventoryDetails_secondItem;

                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                        grossweight_secondItemInventoryDetails = "0";
                                                                                    }

                                                                                    try {
                                                                                        if (grossweight_secondItemInventoryDetails.equals("") || (grossweight_secondItemInventoryDetails.equals("0"))) {
                                                                                            try {
                                                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight().toString();

                                                                                            } catch (Exception e) {
                                                                                                e.printStackTrace();
                                                                                                grossweight_secondItemInventoryDetails = "1";
                                                                                            }
                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        grossweight_secondItemInventoryDetails = "1";

                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    String grossWeightingrams_secondItemInventoryDetails = "";
                                                                                    try {
                                                                                        if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                            grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                            grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                            quantity_secondItemInventoryDetails = "1";
                                                                                        }
                                                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                                    try {
                                                                                        grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    try {
                                                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    //   StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                                    //  isStockOutGoingAlreadyCalledForthisItem = true;

                                                                                }
                                                                                else if (priceTypeOfPOS.equals(Constants.TMCPRICEPERKG)) {

                                                                                    String grossweight_secondItemInventoryDetails = "";

                                                                                    String quantity_secondItemInventoryDetails = "";
                                                                                    if (modal_newOrderItems_insideLoop.getQuantity() != null) {
                                                                                        quantity_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getQuantity();


                                                                                    } else {
                                                                                        quantity_secondItemInventoryDetails = "";
                                                                                    }


                                                                                    if (modal_newOrderItems_insideLoop.getGrossweight() != null) {
                                                                                        grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight();

                                                                                    } else {
                                                                                        grossweight_secondItemInventoryDetails = "";
                                                                                    }


                                                                                    String grossWeightingrams_secondItemInventoryDetails = "";
                                                                                    try {
                                                                                        if (!grossweight_secondItemInventoryDetails.equals("")) {
                                                                                            grossWeightingrams_secondItemInventoryDetails = grossweight_secondItemInventoryDetails.replaceAll("[^\\d.]", "");

                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    double grossweightingrams_double_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (!grossWeightingrams_secondItemInventoryDetails.equals("")) {
                                                                                            grossweightingrams_double_secondItemInventoryDetails = Double.parseDouble(grossWeightingrams_secondItemInventoryDetails);
                                                                                        }
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double quantityDouble_secondItemInventoryDetails = 0;
                                                                                    try {
                                                                                        if (quantity_secondItemInventoryDetails.equals("")) {
                                                                                            quantity_secondItemInventoryDetails = "1";
                                                                                        }
                                                                                        quantityDouble_secondItemInventoryDetails = Double.parseDouble(quantity_secondItemInventoryDetails);
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }

                                                                                    double grossWeightWithQuantity_double_secondItemInventoryDetails = 0;

                                                                                    try {
                                                                                        grossWeightWithQuantity_double_secondItemInventoryDetails = grossweightingrams_double_secondItemInventoryDetails * quantityDouble_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }


                                                                                    try {
                                                                                        totalgrossweightingrams_doubleFromLoop = totalgrossweightingrams_doubleFromLoop + grossWeightWithQuantity_double_secondItemInventoryDetails;
                                                                                    } catch (Exception e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    // Toast.makeText(mContext, "totalgrossweightingrams_doubleFromLoop " + String.valueOf(totalgrossweightingrams_doubleFromLoop), Toast.LENGTH_SHORT).show();


                                                                                    //    StockBalanceChangedForThisItemList.add(menuItemKeyFromMenuAvlDetails);


                                                                                    // isStockOutGoingAlreadyCalledForthisItem = true;
                                                                                }
                                                                            } catch (Exception e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                        }


                                                                    }


                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }


                                                    }

                                                    StockBalanceChangedForThisItemList.add(menuItemKeyFromInventoryDetails);


                                                    isStockOutGoingAlreadyCalledForthisItem = true;



                                                    //  if (priceTypeOfItem.toUpperCase().equals(Constants.TMCPRICEPERKG)) {


                                                    if (isStockOutGoingAlreadyCalledForthisItem) {
                                                        //  try {
                                                        //    totalgrossFromInsideAndOutsideLoop = grossWeightWithQuantity_double + totalgrossweightingrams_doubleFromLoop;
                                                        // } catch (Exception e) {
                                                        //      e.printStackTrace();
                                                        //  }


                                                        getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, totalgrossweightingrams_doubleFromLoop, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);


                                                    } else {
                                                        getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantity_double, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);

                                                    }

                                                            /*} else {
                                                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossweightinGramsFromInventoryDetails, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);

                                                            }

                                                             */




                                                }
                                            }

                                            ////////for same inventoryDetails Item - Ending





                                        }







                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }


                    //    }




                }



            }
            ///////////////////////
///////////

            String price = "";
            if( modal_newOrderItems.getItemPrice_quantityBased()!=null){
                price =  modal_newOrderItems.getItemPrice_quantityBased();
            }
            else{
                price ="";
            }
            //  modal_newOrderItems.getItemPrice_quantityBased();
            String weight = "";
            if(modal_newOrderItems.getItemFinalWeight()!=null){
                weight = modal_newOrderItems.getItemFinalWeight();

            }
            else{
                weight = "";
            }


            String GstAmount = "";
            if(modal_newOrderItems.getGstAmount()!=null){
                GstAmount = (modal_newOrderItems.getGstAmount());
            }
            else{
                GstAmount ="";
            }

            String menuItemId ="";
            if( modal_newOrderItems.getMenuItemId()!=null) {
                menuItemId =    modal_newOrderItems.getMenuItemId();
            }
            else{
                menuItemId ="";
            }
            String netweight ="";
            if( modal_newOrderItems.getNetweight()!=null){
                netweight = modal_newOrderItems.getNetweight();
            }
            else{
                netweight ="";
            }

            String portionsize = "";
            if(modal_newOrderItems.getPortionsize()!=null){
                portionsize = modal_newOrderItems.getPortionsize();
            }
            else{
                portionsize = "";
            }


            String subCtgyKey = "";
            if(modal_newOrderItems.getTmcsubctgykey()!=null){
                subCtgyKey =   modal_newOrderItems.getTmcsubctgykey();
            }
            else{
                subCtgyKey = "";
            }


            if(modal_newOrderItems.getApplieddiscountpercentage()!=null){
                applieddiscountpercentage =   modal_newOrderItems.getApplieddiscountpercentage();
                applieddiscountpercentage = applieddiscountpercentage.replaceAll("[^\\d. ]", "");
            }
            else{
                applieddiscountpercentage = "0";
            }
            try{
                if(modal_newOrderItems.getAppmarkuppercentage()!=null){
                    appMarkupPercentage =   modal_newOrderItems.getAppmarkuppercentage();
                    appMarkupPercentage = appMarkupPercentage.replaceAll("[^\\d. ]", "");
                }
                else{
                    appMarkupPercentage = "0";
                }
            }
            catch (Exception e){
                appMarkupPercentage = "0";
                e.printStackTrace();
            }


            PlaceOrder_in_OrderItemDetails(subCtgyKey,itemName,grossweight, weight,netweight, quantity, price, "", GstAmount, vendorkey, Currenttime, sTime, vendorkey, vendorName,grossWeightingrams,grossweightingrams_double);


            JSONObject itemdespObject = new JSONObject();
            try {
                try {
                    if ((!applieddiscountpercentage.equals("0")) && (!applieddiscountpercentage.equals("")) && (!applieddiscountpercentage.equals("null")) && (!applieddiscountpercentage.equals(null))) {
                        itemdespObject.put("applieddiscpercentage", applieddiscountpercentage);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                if(isPhoneOrderSelected) {
                    itemdespObject.put("appmarkuppercentage", appMarkupPercentage);

                }
                itemdespObject.put("menuitemid", menuItemId);
                itemdespObject.put("itemname", itemName);
                itemdespObject.put("tmcprice", Double.parseDouble(price));
                itemdespObject.put("quantity", Integer.parseInt(quantity));
                itemdespObject.put("checkouturl", "");
                itemdespObject.put("gstamount", Double.parseDouble(GstAmount));
                itemdespObject.put("netweight", netweight);
                itemdespObject.put("portionsize", portionsize);
                itemdespObject.put("tmcsubctgykey", subCtgyKey);
                try {
                    if (weight.equals("") || weight == (null)) {
                        itemdespObject.put("grossweight", grossweight);

                        if(grossweight.equals("")){
                            itemdespObject.put("netweight", netweight);

                        }
                        else
                        {
                            itemdespObject.put("netweight", grossweight);
                            if(grossweightingrams_double!=0) {
                                itemdespObject.put("grossweightingrams", grossweightingrams_double);
                            }


                        }
                        itemdespObject.put("weightingrams", grossweight);

                    } else {
                        itemdespObject.put("grossweight", weight);
                        if(grossweight.equals("")){
                            itemdespObject.put("netweight", netweight);

                        }
                        else
                        {
                            itemdespObject.put("netweight", weight);
                            if(grossweightingrams_double!=0) {
                                itemdespObject.put("grossweightingrams", grossweightingrams_double);
                            }
                        }
                        itemdespObject.put("weightingrams", weight);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemDespArray.put(itemdespObject);


        }
        String StoreCoupon = "";
        if((CouponDiscountAmount.equals("0"))||(CouponDiscountAmount.equals(""))||(CouponDiscountAmount.equals("0.00"))){
            StoreCoupon = "";

        }
        else{
            StoreCoupon = "STORECOUPON";

        }



        if(StoreCoupon.equals("STORECOUPON")){
            String transactiontime = getDate_and_time();


            addDatatoCouponTransactioninDB(CouponDiscountAmount,"STORECOUPON",UserMobile,String.valueOf(sTime),CurrentDate,transactiontime,vendorKey);


        }



        if(String.valueOf(Payment_mode).toUpperCase().equals(Constants.CREDIT)){
            double payableAmount_double = 0;
            String usermobileno = "";
            try{
                usermobileno = "+91"+customermobileno;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try{
                if((!finaltoPayAmountinmethod.equals("null")) && (!finaltoPayAmountinmethod.equals("")) && (!finaltoPayAmountinmethod.equals(null)) ){
                    payableAmount_double  = Double.parseDouble(finaltoPayAmountinmethod);

                }

            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                if(totalamountUserHaveAsCredit == 0){
                    AddOrUpdateDatainCreditOrderDetailsTable(payableAmount_double, orderid,usermobileno,orderplacedTime,"ADD",payableAmount_double);

                }
                else{
                    double newamountUserHaveAsCredit =0;
                    newamountUserHaveAsCredit = payableAmount_double + totalamountUserHaveAsCredit;
                    AddOrUpdateDatainCreditOrderDetailsTable(newamountUserHaveAsCredit, orderid,usermobileno,orderplacedTime,"UPDATE",payableAmount_double);
                }
            }
            catch (Exception e){
                e.printStackTrace();


            }


        }


        JSONObject jsonObject = new JSONObject();
        double  CouponDiscountAmount_double =0;
        try {
            try {
                if (!CouponDiscountAmount.equals("")) {
                    CouponDiscountAmount = (CouponDiscountAmount.replaceAll("[^\\d.]", ""));
                    CouponDiscountAmount_double = Double.parseDouble(CouponDiscountAmount);
                }
                else{
                    CouponDiscountAmount_double =0;
                }


            }
            catch (Exception e){
                CouponDiscountAmount_double =0;
                e.printStackTrace();
            }
            if(CouponDiscountAmount_double>0){
                jsonObject.put("coupondiscount", CouponDiscountAmount_double);

            }
            else{
                jsonObject.put("coupondiscount", CouponDiscountAmount);

            }

            double deliveryAmount_double =0;
            try{
                deliveryAmount_double = Double.parseDouble(deliveryAmount_for_this_order);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            jsonObject.put("couponkey", StoreCoupon);

            jsonObject.put("ordertype", ordertype);
            jsonObject.put("gstamount", Double.parseDouble(taxAmount));

            jsonObject.put("deliverytype", deliverytype);
            jsonObject.put("slotname", slotname);

            jsonObject.put("orderid", orderid);
            jsonObject.put("orderplacedtime", orderplacedTime);
            jsonObject.put("userid", userid);
            if(orderdetailsnewschema) {
                jsonObject.put("usermobileno", UserMobile);
                jsonObject.put("slotdate", orderPlacedDate);

            }
            else{
                jsonObject.put("usermobile", UserMobile);
                jsonObject.put("slotdate", "");

            }

            if(isPhoneOrderSelected) {
                jsonObject.put("slottimerange", slottimerange);
                jsonObject.put("useraddress", selectedAddress);
                jsonObject.put("userkey", user_key_toAdd_Address);
                jsonObject.put("tokenno", (tokenno));
                jsonObject.put("deliveryamount", deliveryAmount_double);
                if(userStatus.toUpperCase().equals(Constants.USERSTATUS_FLAGGED)){
                    jsonObject.put("userstatus", (Constants.USERSTATUS_FLAGGED));
                }
            }
            else{
                jsonObject.put("slottimerange", "");
                jsonObject.put("tokenno", (""));
                jsonObject.put("deliveryamount", 0);

            }
            if ((!customerName.equals("")) && (!customerName.equals("null")) && (!customerName.equals(null)) && (!customerName.equals(" "))) {

                jsonObject.put("username",customerName);

            }

            jsonObject.put("vendorkey", vendorkey);
            jsonObject.put("vendorname", vendorName);
            jsonObject.put("payableamount", Double.parseDouble(finaltoPayAmountinmethod));

            jsonObject.put("taxamount", taxAmount);
            jsonObject.put("paymentmode", Payment_mode);
            jsonObject.put("itemdesp", itemDespArray);
            jsonObject.put("couponid", couponid);

            jsonObject.put("orderplaceddate", orderPlacedDate);

            jsonObject.put("merchantorderid", merchantorderid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        boolean finalShouldGetPrintNow = shouldGetPrintNow;


        String Api_To_PlaceOrderInOrderDetails = "";
        if(orderdetailsnewschema){
            Api_To_PlaceOrderInOrderDetails = Constants.api_AddVendorOrderDetails;

        }
        else{
            Api_To_PlaceOrderInOrderDetails = Constants.api_addOrderDetailsInOrderDetailsTable;

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Api_To_PlaceOrderInOrderDetails ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        // StartTwice startTwice =new StartTwice(UserMobile,tokenno,itemTotalwithoutGst,taxAmount,payableAmount,orderid,cart_Item_List,cartItem_hashmap,Payment_mode);
                        // startTwice.main();
                        if(defaultPrinterType.toUpperCase().equals(Constants.PDF_PrinterType)) {

                            GeneratePDF(orderplacedTime, UserMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_Item_List, cartItem_hashmap, Payment_mode, discountAmount, ordertype, itemDespArray);

                        }
                        else{

                            if(finalShouldGetPrintNow) {
                                printRecipt(orderplacedTime, UserMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_Item_List, cartItem_hashmap, Payment_mode, discountAmount, ordertype, itemDespArray);
                            }
                            else{

                                if(!isinventorycheck){

                                    turnoffProgressBarAndResetArray();
                                }

                            }
                            isOrderPlacedinOrderdetails = true;
                            //  showProgressBar(false);
                        }






                    }
                    else{

                        isOrderDetailsMethodCalled = false;
                        showProgressBar(false);
                        Toast.makeText(mContext,"OrderDetails is not updated in DB",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    showProgressBar(false);
                    isOrderDetailsMethodCalled = false;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                isOrderDetailsMethodCalled = false;

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
        // Make the request


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);






    }




/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    if (Environment.isExternalStorageManager()) {
                        // perform action when allow permission success


                        try {
                            Toast.makeText(mContext, "Permission Granted for storage access!", Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                    }
                }
                else {
                    AskForStoragePermission();
                    Toast.makeText(mContext, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }




 */
    private void AskForStoragePermission() {

        try {

            if (SDK_INT >= Build.VERSION_CODES.R) {

                if (Environment.isExternalStorageManager()) {
                    try {

                       // GeneratePDF(orderplacedTime, userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_item_list, cartItem_hashmap, payment_mode, discountAmount, ordertype, itemDespArray);

                    } catch (Exception e) {
                        e.printStackTrace();
                        ;
                    }
                } else {
                    try {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setData(Uri.parse(String.format("package:%s", mContext.getApplicationContext().getPackageName())));
                        startActivityForResult(intent, 2296);
                    } catch (Exception e) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivityForResult(intent, 2296);
                    }
                }

            } else {


                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);
                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    //showProgressBar(true);
                    try {
                       // GeneratePDF(orderplacedTime, userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_item_list, cartItem_hashmap, payment_mode, discountAmount, ordertype, itemDespArray);


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

    private void GeneratePDF(String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String finaltoPayAmountinmethod, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String discountAmount, String ordertype, JSONArray itemDespArray) {

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
            String filename = "New Orders _Bill Recipt" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            try {
                FileOutputStream fOut = new FileOutputStream(file);
                Document layoutDocument = new Document();
                PdfWriter.getInstance(layoutDocument, fOut);
                layoutDocument.open();
                addVendorDetails(layoutDocument,orderplacedTime, userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_item_list, cartItem_hashmap, payment_mode, discountAmount, ordertype, itemDespArray);

                layoutDocument.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            // document = new PdfDocument(new PdfWriter("MyFirstInvoice.pdf"));



            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            /*Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(pdfViewIntent, "View");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


             */




            try {
                if(!isinventorycheck){

                    turnoffProgressBarAndResetArray();
                }

                Uri pdfUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pdfUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", file);
                } else {
                    pdfUri = Uri.fromFile(file);
                }
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/xls");
                share.putExtra(Intent.EXTRA_STREAM, pdfUri);
                startActivity(Intent.createChooser(share, "Share"));



                // startActivityForResult(intent, OPENPDF_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
            // }
        } catch (IOException e) {
            if(!isinventorycheck){

                turnoffProgressBarAndResetArray();
            }
            Log.i("error", e.getLocalizedMessage());
        } catch (Exception ex) {
            if(!isinventorycheck){

                turnoffProgressBarAndResetArray();
            }
            ex.printStackTrace();
        }

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private void addVendorDetails(Document layoutDocument, String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String finaltoPayAmountinmethod, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String discountAmount, String ordertype, JSONArray itemDespArray) {
        try {
            SharedPreferences sharedPreferences
                    = mContext.getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);

            String VendorAddressline1 = sharedPreferences.getString("VendorAddressline1", "");
            String VendorAddressline2 = sharedPreferences.getString("VendorAddressline2", "");
            String VendorPincode = sharedPreferences.getString("VendorPincode", "");
            String VendorMobileno = sharedPreferences.getString("VendorMobileNumber", "");

            Font font = new Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 39, Font.BOLD);


            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
                    Font.BOLD);

            Font subtitleFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
                    Font.NORMAL);
            Font subtitleFontLabel = new Font(Font.FontFamily.TIMES_ROMAN, 14,
                    Font.BOLD);

            Font itemNameFont = new Font(Font.FontFamily.TIMES_ROMAN, 15,
                    Font.NORMAL);

            Font itemNameFontLabel = new Font(Font.FontFamily.TIMES_ROMAN, 15,
                    Font.BOLD);

            Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 13,
                    Font.NORMAL);

            /*Anchor anchor = new Anchor("First Chapter", catFont);
            anchor.setName("First Chapter");
            Chapter catPart = new Chapter(new Paragraph(anchor), 1);
            layoutDocument.add(catPart);

             */

           /*Paragraph titlepara = new Paragraph();
            // We add one empty line
           // addEmptyLine(titlepara, 1);
            // Lets write a big header
            titlepara.add(new Paragraph("The Meat Chop", catFont));
            titlepara.setSpacingBefore(5);
            titlepara.setAlignment(Element.ALIGN_JUSTIFIED);
            layoutDocument.add(titlepara);
            */


            PdfPTable phraseTitle_table = new PdfPTable(1);


            Phrase phraseTitle = new Phrase("THE MEAT CHOP", titleFont);
            PdfPCell phraseTitlecell = new PdfPCell(phraseTitle);
            phraseTitlecell.setBorder(Rectangle.NO_BORDER);
            phraseTitlecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseTitlecell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseTitlecell.setPaddingLeft(10);
            phraseTitlecell.setPaddingBottom(15);
            phraseTitle_table.addCell(phraseTitlecell);


            Phrase phraseDesp = new Phrase("Fresh Goat Meat and Seafood", normalFont);
            PdfPCell phraseDespcell = new PdfPCell(phraseDesp);
            phraseDespcell.setBorder(Rectangle.NO_BORDER);
            phraseDespcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseDespcell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseDespcell.setPaddingLeft(10);
            phraseTitle_table.addCell(phraseDespcell);


            Phrase phraseAddress = new Phrase(VendorAddressline1 + " " + VendorAddressline2, normalFont);
            PdfPCell phraseAddressCell = new PdfPCell(phraseAddress);
            phraseAddressCell.setBorder(Rectangle.NO_BORDER);
            phraseAddressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseAddressCell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseAddressCell.setPaddingLeft(10);
            phraseTitle_table.addCell(phraseAddressCell);


            Phrase phrasePincode = new Phrase("Pincode : " + VendorPincode, normalFont);
            PdfPCell phrasePincodecell = new PdfPCell(phrasePincode);
            phrasePincodecell.setBorder(Rectangle.NO_BORDER);
            phrasePincodecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phrasePincodecell.setVerticalAlignment(Element.ALIGN_CENTER);
            phrasePincodecell.setPaddingLeft(10);
            phraseTitle_table.addCell(phrasePincodecell);

            Phrase phraseMobileNo = new Phrase(VendorMobileno, normalFont);
            PdfPCell phraseMobilenocell = new PdfPCell(phraseMobileNo);
            phraseMobilenocell.setBorder(Rectangle.NO_BORDER);
            phraseMobilenocell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseMobilenocell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseMobilenocell.setPaddingLeft(10);
            phraseTitle_table.addCell(phraseMobilenocell);


            Phrase phraseGSTIN = new Phrase("GSTIN : 33AAJCC0055D1Z9", normalFont);
            PdfPCell phraseGstincell = new PdfPCell(phraseGSTIN);
            phraseGstincell.setBorder(Rectangle.NO_BORDER);
            phraseGstincell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseGstincell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseGstincell.setPaddingLeft(10);
            phraseTitle_table.addCell(phraseGstincell);


            Phrase phraseorderplacedTime = new Phrase(orderplacedTime, normalFont);
            PdfPCell phraseOrderPlacedTimecell = new PdfPCell(phraseorderplacedTime);
            phraseOrderPlacedTimecell.setBorder(Rectangle.NO_BORDER);
            phraseOrderPlacedTimecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseOrderPlacedTimecell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseOrderPlacedTimecell.setPaddingLeft(10);
            phraseTitle_table.addCell(phraseOrderPlacedTimecell);

            Phrase phraseorderid = new Phrase("#" + orderid, normalFont);
            PdfPCell phraseOrderidcell = new PdfPCell(phraseorderid);
            phraseOrderidcell.setBorder(Rectangle.NO_BORDER);
            phraseOrderidcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseOrderidcell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseOrderidcell.setPaddingLeft(10);
            phraseOrderidcell.setPaddingBottom(10);
            phraseTitle_table.addCell(phraseOrderidcell);


            Phrase phraseDots = new Phrase("                  ");
            PdfPCell phraseDotscell = new PdfPCell(phraseDots);
            phraseDotscell.setBorder(Rectangle.NO_BORDER);
            phraseDotscell.setPaddingLeft(10);
            phraseDotscell.setBorderWidthBottom(01);
            phraseTitle_table.addCell(phraseDotscell);

            layoutDocument.add(phraseTitle_table);

/*
            PdfPTable phrasebody_table = new PdfPTable(1);


            Phrase phrasebodySubTitle1 = new Phrase("Item Name ", subtitleFont);

            PdfPCell subtitle1cell = new PdfPCell(new Phrase(phrasebodySubTitle1));
            subtitle1cell.setBorder(Rectangle.NO_BORDER);
            subtitle1cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            subtitle1cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            subtitle1cell.setPaddingLeft(10);
            subtitle1cell.setPaddingRight(20);
            phrasebody_table.addCell(subtitle1cell);

            Phrase phrasebodySubTitle2 = new Phrase("Weight * Rate ", subtitleFont);
            PdfPCell subtitle2cell = new PdfPCell(new Phrase(phrasebodySubTitle2));
            subtitle2cell.setBorder(Rectangle.NO_BORDER);
            subtitle2cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            subtitle2cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            subtitle2cell.setPaddingLeft(10);
            subtitle2cell.setPaddingRight(20);
            phrasebody_table.addCell(subtitle2cell);
            Phrase phrasebodySubTitle3 = new Phrase("Price ", subtitleFont);
            PdfPCell subtitle3cell = new PdfPCell(new Phrase(phrasebodySubTitle3));
            subtitle3cell.setBorder(Rectangle.NO_BORDER);
            subtitle3cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            subtitle3cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            subtitle3cell.setPaddingLeft(10);
            subtitle3cell.setPaddingRight(20);
            phrasebody_table.addCell(subtitle3cell);


            Phrase phraseEmptyLine = new Phrase("                  ");
            PdfPCell phraseEmptycell = new PdfPCell(phraseEmptyLine);
            phraseEmptycell.setBorder(Rectangle.NO_BORDER);
            phraseEmptycell.setPaddingLeft(10);
            phraseEmptycell.setBorderWidthBottom(01);
            phrasebody_table.addCell(phraseEmptycell);
            layoutDocument.add(phrasebody_table);


 */
            double oldSavedAmount = 0;

            for (int i = 0; i < cart_item_list.size(); i++) {
                double savedAmount;
                String itemUniqueCode = cart_item_list.get(i);
                Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);

                String fullitemName = String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());
                       /* int indexofbraces = itemName.indexOf("(");
                        if (indexofbraces >= 0) {
                            itemName = itemName.substring(0, indexofbraces);

                        }
                        if (itemName.length() > 21) {
                            itemName = itemName.substring(0, 21);
                            itemName = itemName + "...";
                        }

                        */
                String itemName = "";
                String itemNameAfterBraces = "";

                String tmcSubCtgyKey = String.valueOf(Objects.requireNonNull(modal_newOrderItems).getTmcsubctgykey());
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

                        if (fullitemName.length() > 18) {
                            itemName = fullitemName.substring(0, 18);
                            itemName = itemName + "...";

                            fullitemName = fullitemName.substring(0, 18);
                            fullitemName = fullitemName + "...";
                        }
                        if (fullitemName.length() < 18) {
                            itemName = fullitemName;

                            fullitemName = fullitemName;

                        }
                    } else {
                        int indexofbraces = fullitemName.indexOf("(");
                        if (indexofbraces >= 0) {
                            itemName = fullitemName.substring(0, indexofbraces);

                        }
                        if (fullitemName.length() > 18) {
                            itemName = fullitemName.substring(0, 18);
                            itemName = itemName + "...";
                        }
                        if (fullitemName.length() < 18) {
                            itemName = fullitemName;

                        }
                    }
                } catch (Exception e) {
                    itemName = fullitemName;

                    e.printStackTrace();
                }


                savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                oldSavedAmount = savedAmount + oldSavedAmount;
                String Gst = "Rs." + modal_newOrderItems.getGstAmount();
                String subtotal = "Rs." + modal_newOrderItems.getSubTotal_perItem();
                String quantity = modal_newOrderItems.getQuantity();
                String itemrate = "Rs." + modal_newOrderItems.getItemFinalPrice();
                String weight = modal_newOrderItems.getItemFinalWeight();
                String weight_inKG = "";  double weight_inKGDouble = 0;double weight_inGRAMSDouble = 0;String weight_inGrams = "";
                String pricePer_KG = "";


                try{
                    weight_inGrams = (weight.replaceAll("[^\\d.]", ""));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    weight_inGRAMSDouble  = Double.parseDouble(weight_inGrams);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                try{
                    weight_inKGDouble  = weight_inGRAMSDouble /1000 ;
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    weight_inKG  = String.valueOf(decimalFormat.format(weight_inKGDouble));
                }
                catch (Exception e){
                    e.printStackTrace();
                }



                try{
                    pricePer_KG   = String.valueOf(modal_newOrderItems.getTmcpriceperkg());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                String itemDespName_Weight_quantity = "";

                //´ÖÌå
                if (!weight.equals(" ") && weight.length() > 0) {
                    itemDespName_Weight_quantity = String.valueOf(fullitemName + "( " + weight + " )" + " * " + quantity);
                } else {

                    itemDespName_Weight_quantity = String.valueOf(fullitemName + " * " + quantity);
                }
                try{
                PdfPTable phrasebodyItemDetails_table = new PdfPTable(2);

                Phrase phrasebodyitemNameLabel = new Phrase("Item Name : ", itemNameFontLabel);
                PdfPCell itemNameLabelcell = new PdfPCell(new Phrase(phrasebodyitemNameLabel));
                itemNameLabelcell.setBorder(Rectangle.NO_BORDER);
                itemNameLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemNameLabelcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itemNameLabelcell.setPaddingLeft(10);
                itemNameLabelcell.setPaddingRight(20);
                itemNameLabelcell.setPaddingBottom(5);
                phrasebodyItemDetails_table.addCell(itemNameLabelcell);



                Phrase phrasebodyitemName = new Phrase(fullitemName, itemNameFont);
                PdfPCell itemNamecell = new PdfPCell(new Phrase(phrasebodyitemName));
                itemNamecell.setBorder(Rectangle.NO_BORDER);
                itemNamecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemNamecell.setPaddingLeft(10);
                itemNamecell.setPaddingRight(20);
                itemNamecell.setPaddingBottom(5);
                phrasebodyItemDetails_table.addCell(itemNamecell);

               /* Phrase phrasebodyempty = new Phrase("", normalFont);
                PdfPCell emptycell = new PdfPCell(new Phrase(phrasebodyempty));
                emptycell.setBorder(Rectangle.NO_BORDER);
                emptycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycell.setPaddingLeft(10);
                emptycell.setPaddingRight(20);
                emptycell.setPaddingBottom(5);
                phrasebodyItemDetails_table.addCell(emptycell);


                */


                if(weight_inKG.equals("")|| weight_inKG.equals("0") || weight_inKG.equals("null") ||weight_inKG.equals(null) ) {
                    Phrase phrasebodyWeightLabel = new Phrase("Weight : ", subtitleFontLabel);
                    PdfPCell itemWeightLabelcell = new PdfPCell(new Phrase(phrasebodyWeightLabel));
                    itemWeightLabelcell.setBorder(Rectangle.NO_BORDER);
                    itemWeightLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemWeightLabelcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemWeightLabelcell.setPaddingLeft(10);
                    itemWeightLabelcell.setPaddingRight(20);
                    itemWeightLabelcell.setPaddingBottom(5);
                    phrasebodyItemDetails_table.addCell(itemWeightLabelcell);


                    Phrase phrasebodyWeight = new Phrase(weight, subtitleFont);
                    PdfPCell itemWeightcell = new PdfPCell(new Phrase(phrasebodyWeight));
                    itemWeightcell.setBorder(Rectangle.NO_BORDER);
                    itemWeightcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemWeightcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemWeightcell.setPaddingLeft(10);
                    itemWeightcell.setPaddingRight(20);
                    itemWeightLabelcell.setPaddingBottom(5);
                    phrasebodyItemDetails_table.addCell(itemWeightcell);


                   /* Phrase phrasebodyempty2 = new Phrase("", normalFont);
                    PdfPCell emptycell2 = new PdfPCell(new Phrase(phrasebodyempty2));
                    emptycell2.setBorder(Rectangle.NO_BORDER);
                    emptycell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    emptycell2.setPaddingLeft(10);
                    emptycell2.setPaddingRight(20);
                    emptycell2.setPaddingBottom(5);
                    phrasebodyItemDetails_table.addCell(emptycell2);

                    */
                }
                else{
                    Phrase phrasebodyWeightLabel = new Phrase("Weight * PricePerKg : ", subtitleFontLabel);
                    PdfPCell itemWeightLabelcell = new PdfPCell(new Phrase(phrasebodyWeightLabel));
                    itemWeightLabelcell.setBorder(Rectangle.NO_BORDER);
                    itemWeightLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);

                    itemWeightLabelcell.setPaddingLeft(10);
                    itemWeightLabelcell.setPaddingRight(20);
                    itemWeightLabelcell.setPaddingBottom(5);
                    phrasebodyItemDetails_table.addCell(itemWeightLabelcell);


                    Phrase phrasebodyWeight = new Phrase(weight_inKG +"Kg  *  "+ pricePer_KG+" Rs ", subtitleFont);
                    PdfPCell itemWeightcell = new PdfPCell(new Phrase(phrasebodyWeight));
                    itemWeightcell.setBorder(Rectangle.NO_BORDER);
                    itemWeightcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemWeightcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemWeightcell.setPaddingLeft(10);
                    itemWeightcell.setPaddingRight(20);
                    itemWeightLabelcell.setPaddingBottom(5);
                    phrasebodyItemDetails_table.addCell(itemWeightcell);


                   /* Phrase phrasebodyempty2 = new Phrase("", normalFont);
                    PdfPCell emptycell2 = new PdfPCell(new Phrase(phrasebodyempty2));
                    emptycell2.setBorder(Rectangle.NO_BORDER);
                    emptycell2.setHorizontalAlignment(Element.ALIGN_LEFT);
                    emptycell2.setPaddingLeft(10);
                    emptycell2.setPaddingRight(10);
                    emptycell2.setPaddingBottom(5);
                    phrasebodyItemDetails_table.addCell(emptycell2);

                    */
                }

            /*
                Phrase phrasebodyQuantityLabel = new Phrase("Quantity : ", subtitleFontLabel);
                PdfPCell itemQuantityLabelcell = new PdfPCell(new Phrase(phrasebodyQuantityLabel));
                itemQuantityLabelcell.setBorder(Rectangle.NO_BORDER);
                itemQuantityLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemQuantityLabelcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itemQuantityLabelcell.setPaddingLeft(10);
                itemQuantityLabelcell.setPaddingRight(20);
                itemQuantityLabelcell.setPaddingBottom(5);

                phrasebodyItemDetails_table.addCell(itemQuantityLabelcell);


                Phrase phrasebodyQuantity = new Phrase( quantity, subtitleFont);
                PdfPCell itemQuantitycell = new PdfPCell(new Phrase(phrasebodyQuantity));
                itemQuantitycell.setBorder(Rectangle.NO_BORDER);
                itemQuantitycell.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemQuantitycell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itemQuantitycell.setPaddingLeft(10);
                itemQuantitycell.setPaddingBottom(5);

                itemQuantitycell.setPaddingRight(20);
                phrasebodyItemDetails_table.addCell(itemQuantitycell);





                Phrase phrasebodyempty3 = new Phrase("", normalFont);
                PdfPCell emptycell3 = new PdfPCell(new Phrase(phrasebodyempty3));
                emptycell3.setBorder(Rectangle.NO_BORDER);
                emptycell3.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycell3.setPaddingLeft(10);
                emptycell3.setPaddingRight(20);
                emptycell3.setPaddingBottom(5);
                phrasebodyItemDetails_table.addCell(emptycell3);


             */


                Phrase phrasebodyPriceLabel = new Phrase("Price : ", subtitleFontLabel);
                PdfPCell itemPriceLabelcell = new PdfPCell(new Phrase(phrasebodyPriceLabel));
                itemPriceLabelcell.setBorder(Rectangle.NO_BORDER);
                itemPriceLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemPriceLabelcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itemPriceLabelcell.setPaddingLeft(10);
                itemPriceLabelcell.setPaddingRight(20);
                itemPriceLabelcell.setPaddingBottom(20);
                phrasebodyItemDetails_table.addCell(itemPriceLabelcell);


                Phrase phrasebodyPrice = new Phrase(subtotal, subtitleFont);
                PdfPCell itemPricecell = new PdfPCell(new Phrase(phrasebodyPrice));
                itemPricecell.setBorder(Rectangle.NO_BORDER);
                itemPricecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                itemPricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                itemPricecell.setPaddingLeft(10);
                itemPricecell.setPaddingRight(20);
                itemPricecell.setPaddingBottom(20);
                phrasebodyItemDetails_table.addCell(itemPricecell);


            /*
                Phrase phrasebodyempty1 = new Phrase("", normalFont);
                PdfPCell emptycell1 = new PdfPCell(new Phrase(phrasebodyempty1));
                emptycell1.setBorder(Rectangle.NO_BORDER);
                emptycell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                emptycell1.setPaddingLeft(10);
                emptycell1.setPaddingRight(20);
                emptycell1.setPaddingBottom(20);
                phrasebodyItemDetails_table.addCell(emptycell1);


             */



                layoutDocument.add(phrasebodyItemDetails_table);


                }
                catch (Exception e){
                    e.printStackTrace();
                }


/*
                try{
                    PdfPTable phrasebodyItemWeight_table = new PdfPTable(1);

                    Phrase phrasebodyPrice = new Phrase("Price : "+ subtotal, subtitleFont);
                    PdfPCell itemPricecell = new PdfPCell(new Phrase(phrasebodyPrice));
                    itemPricecell.setBorder(Rectangle.NO_BORDER);
                    itemPricecell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemPricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemPricecell.setPaddingLeft(10);
                    itemPricecell.setPaddingRight(20);
                    phrasebodyItemWeight_table.addCell(itemPricecell);


                    Phrase phrasebodyWeight = new Phrase("Weight : "+ weight, subtitleFont);
                    PdfPCell itemWeightcell = new PdfPCell(new Phrase(phrasebodyWeight));
                    itemWeightcell.setBorder(Rectangle.NO_BORDER);
                    itemWeightcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    itemWeightcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemWeightcell.setPaddingLeft(10);
                    itemWeightcell.setPaddingRight(20);
                    phrasebodyItemWeight_table.addCell(itemWeightcell);

                    layoutDocument.add(phrasebodyItemWeight_table);

                }
                catch (Exception e){
                    e.printStackTrace();
                }



 */
            }
            PdfPTable phrase_table = new PdfPTable(1);

            Phrase phraseEmptyLine2 = new Phrase("                  ");
            PdfPCell phraseEmptycell2 = new PdfPCell(phraseEmptyLine2);
            phraseEmptycell2.setBorder(Rectangle.NO_BORDER);
            phraseEmptycell2.setPaddingLeft(10);
            phraseEmptycell2.setBorderWidthBottom(01);
            phrase_table.addCell(phraseEmptycell2);
            layoutDocument.add(phrase_table);

            PdfPTable phrasebody1_table = new PdfPTable(2);

            Phrase phraseSubTotalLabel = new Phrase( "SubTotal : ", normalFont);
            PdfPCell subTotalLabelcell = new PdfPCell(new Phrase(phraseSubTotalLabel));
            subTotalLabelcell.setBorder(Rectangle.NO_BORDER);
            subTotalLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            subTotalLabelcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            subTotalLabelcell.setPaddingLeft(10);
            subTotalLabelcell.setPaddingRight(20);
            subTotalLabelcell.setPaddingBottom(5);
            subTotalLabelcell.setPaddingTop(10);
            phrasebody1_table.addCell(subTotalLabelcell);


            Phrase phraseSubTotal = new Phrase( "Rs. "+new_totalAmount_withGst+".00", normalFont);
            PdfPCell subTotalcell = new PdfPCell(new Phrase(phraseSubTotal));
            subTotalcell.setBorder(Rectangle.NO_BORDER);
            subTotalcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            subTotalcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            subTotalcell.setPaddingLeft(10);
            subTotalcell.setPaddingRight(20);
            subTotalcell.setPaddingBottom(5);
            subTotalLabelcell.setPaddingTop(10);
            phrasebody1_table.addCell(subTotalcell);



            if(!discountAmount.equals("0")){
                Phrase phraseDiscountLabel = new Phrase( "Discount Amount : ", normalFont);
                PdfPCell discountLabelcell = new PdfPCell(new Phrase(phraseDiscountLabel));
                discountLabelcell.setBorder(Rectangle.NO_BORDER);
                discountLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                discountLabelcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                discountLabelcell.setPaddingLeft(10);
                discountLabelcell.setPaddingRight(20);
                discountLabelcell.setPaddingBottom(5);
                phrasebody1_table.addCell(discountLabelcell);


                Phrase phraseDiscount = new Phrase( "Rs. "+discountAmount+".00", normalFont);
                PdfPCell discountcell = new PdfPCell(new Phrase(phraseDiscount));
                discountcell.setBorder(Rectangle.NO_BORDER);
                discountcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                discountcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                discountcell.setPaddingLeft(10);
                discountcell.setPaddingRight(20);
                discountcell.setPaddingBottom(5);
                phrasebody1_table.addCell(discountcell);

            }

            Phrase phraseNetTotalLabel = new Phrase( "NetTotal : ", normalFont);
            PdfPCell netTotalLabelcell = new PdfPCell(new Phrase(phraseNetTotalLabel));
            netTotalLabelcell.setBorder(Rectangle.NO_BORDER);
            netTotalLabelcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            netTotalLabelcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            netTotalLabelcell.setPaddingLeft(10);
            netTotalLabelcell.setPaddingRight(20);
            netTotalLabelcell.setPaddingBottom(3);
            phrasebody1_table.addCell(netTotalLabelcell);


            Phrase phraseNetTotal = new Phrase( "Rs. "+finaltoPayAmountinmethod, normalFont);
            PdfPCell netTotalcell = new PdfPCell(new Phrase(phraseNetTotal));
            netTotalcell.setBorder(Rectangle.NO_BORDER);
            netTotalcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            netTotalcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            netTotalcell.setPaddingLeft(10);
            netTotalcell.setPaddingRight(20);
            netTotalcell.setPaddingBottom(3);
            phrasebody1_table.addCell(netTotalcell);

            layoutDocument.add(phrasebody1_table);


            PdfPTable phrasebody2_table = new PdfPTable(1);

            Phrase phraseEmptyLine1 = new Phrase("                  ");
            PdfPCell phraseEmptycell1 = new PdfPCell(phraseEmptyLine1);
            phraseEmptycell1.setBorder(Rectangle.NO_BORDER);
            phraseEmptycell1.setPaddingLeft(10);
            phraseEmptycell1.setBorderWidthBottom(01);
            phraseEmptycell1.setPaddingBottom(10);
            phrasebody2_table.addCell(phraseEmptycell1);


         /*   Phrase phraseOrderType = new Phrase( "Order Type :  "+ordertype, normalFont);
            PdfPCell orderTypecell = new PdfPCell(new Phrase(phraseOrderType));
            orderTypecell.setBorder(Rectangle.NO_BORDER);
            orderTypecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            orderTypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            orderTypecell.setPaddingLeft(10);
            orderTypecell.setPaddingRight(20);
            orderTypecell.setPaddingBottom(10);
            orderTypecell.setPaddingTop(10);
            phrasebody2_table.addCell(orderTypecell);


          */



            Phrase phrasePaymentMode = new Phrase( "Payment Mode : "+ payment_mode, normalFont);
            PdfPCell paymentModecell = new PdfPCell(new Phrase(phrasePaymentMode));
            paymentModecell.setBorder(Rectangle.NO_BORDER);
            paymentModecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            paymentModecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            paymentModecell.setPaddingLeft(10);
            paymentModecell.setPaddingRight(20);
            paymentModecell.setPaddingBottom(10);
            phrasebody2_table.addCell(paymentModecell);


            Phrase phraseUserMobileNo = new Phrase( "Customer Mobile No : "+ userMobile, normalFont);
            PdfPCell userMobileNocell = new PdfPCell(new Phrase(phraseUserMobileNo));
            userMobileNocell.setBorder(Rectangle.NO_BORDER);
            userMobileNocell.setHorizontalAlignment(Element.ALIGN_LEFT);
            userMobileNocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            userMobileNocell.setPaddingLeft(10);
            userMobileNocell.setPaddingRight(20);
            userMobileNocell.setPaddingBottom(10);
            phrasebody2_table.addCell(userMobileNocell);

            Phrase phraseUsername = new Phrase( "Customer Name : "+ customerName, subtitleFontLabel);
            PdfPCell usernamecell = new PdfPCell(new Phrase(phraseUsername));
            usernamecell.setBorder(Rectangle.NO_BORDER);
            usernamecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            usernamecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            usernamecell.setPaddingLeft(10);
            usernamecell.setPaddingRight(20);
            usernamecell.setPaddingBottom(10);
            phrasebody2_table.addCell(usernamecell);

            double newamountUserHaveAsCredit =0; double finaltoPayAmountinmethod_double = 0;
            try{
                finaltoPayAmountinmethod = finaltoPayAmountinmethod.replaceAll("[^\\d.]", "");
                finaltoPayAmountinmethod_double = Double.parseDouble(finaltoPayAmountinmethod);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                newamountUserHaveAsCredit = finaltoPayAmountinmethod_double + totalamountUserHaveAsCredit;

            }
             catch (Exception e){
                e.printStackTrace();
             }


            Phrase phraseoldCreditAmount = new Phrase( "Amount in Credit before this bill : Rs. "+ String.valueOf(totalamountUserHaveAsCredit), normalFont);
            PdfPCell oldcreditAmtcell = new PdfPCell(new Phrase(phraseoldCreditAmount));
            oldcreditAmtcell.setBorder(Rectangle.NO_BORDER);
            oldcreditAmtcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            oldcreditAmtcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            oldcreditAmtcell.setPaddingLeft(10);
            oldcreditAmtcell.setPaddingRight(20);
            oldcreditAmtcell.setPaddingBottom(10);
            phrasebody2_table.addCell(oldcreditAmtcell);

            Phrase phrasenewCreditAmount = new Phrase( "Amount in Credit after this bill : Rs. "+ String.valueOf(newamountUserHaveAsCredit), normalFont);
            PdfPCell newCreditAmountcell = new PdfPCell(new Phrase(phrasenewCreditAmount));
            newCreditAmountcell.setBorder(Rectangle.NO_BORDER);
            newCreditAmountcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            newCreditAmountcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            newCreditAmountcell.setPaddingLeft(10);
            newCreditAmountcell.setPaddingRight(20);
            newCreditAmountcell.setPaddingBottom(30);
            phrasebody2_table.addCell(newCreditAmountcell);



            Phrase phraseFinalNote = new Phrase("Thank You for Choosing US !!!! ", normalFont);
            PdfPCell phraseFinalNotecell = new PdfPCell(phraseFinalNote);
            phraseFinalNotecell.setBorder(Rectangle.NO_BORDER);
            phraseFinalNotecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            phraseFinalNotecell.setVerticalAlignment(Element.ALIGN_CENTER);
            phraseFinalNotecell.setPaddingLeft(10);
            phrasebody2_table.addCell(phraseFinalNotecell);



            layoutDocument.add(phrasebody2_table);






        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void AddDataInStockOutGoingTable(double grossweightingrams_double, String orderid, String stockIncomingKey_avlDetails, String itemName, String barcode, String priceTypeForPOS, String tmcCtgy, String tmcSubCtgyKey) {

        String stockType = "";
        try{
        stockType = String.valueOf(priceTypeForPOS).toUpperCase();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{

        if(stockType.equals("TMCPRICE")){
            stockType = "unit";
        }
        else if(stockType.equals("TMCPRICEPERKG")){
            stockType = "grams";
        }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode",String.valueOf(barcode));
            jsonObject.put("itemname", String.valueOf(itemName));
            jsonObject.put("outgoingdate",String.valueOf(Currenttime));
            if(ordertype.equals(String.valueOf(Constants.PhoneOrder))){
                jsonObject.put("outgoingtype", String.valueOf(Constants.SALES_ALLOCATED_OUTGOINGTYPE));

            }
            else {
                jsonObject.put("outgoingtype", String.valueOf(Constants.SALES_FULFILLED_OUTGOINGTYPE));

            }
            jsonObject.put("outgoingqty",grossweightingrams_double);
            jsonObject.put("salesorderid", String.valueOf(orderid));
            jsonObject.put("stocktype",(stockType));
            jsonObject.put("tmcctgykey", String.valueOf(tmcCtgy));
            jsonObject.put("tmcsubctgykey", String.valueOf(tmcSubCtgyKey));
            jsonObject.put("vendorkey", String.valueOf(vendorKey));
            jsonObject.put("userkey", String.valueOf(""));
            jsonObject.put("stockincomingkey", String.valueOf(stockIncomingKey_avlDetails));
            jsonObject.put("inventoryusermobileno", String.valueOf(usermobileNo));



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_addEntry_StockOutGoingDetails
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        // showProgressBar(false);
                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





    }

    private void getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(String stockIncomingKey_avlDetails, String key_avlDetails, String menuItemKey_avlDetails, String receivedStock_AvlDetails, double currentBillingItemWeight_double, String itemName, String barcode, String orderid, String priceTypeForPOS, String tmcCtgy, String tmcSubCtgyKey, boolean isitemAvailable, boolean allowNegativeStock) {
    showProgressBar(true);
        if((!stockIncomingKey_avlDetails.equals("")) && (!stockIncomingKey_avlDetails.equals(" - ")) &&(!stockIncomingKey_avlDetails.equals("null")) && (!stockIncomingKey_avlDetails.equals(null)) && (!stockIncomingKey_avlDetails.equals("0")) && (!stockIncomingKey_avlDetails.equals(" 0 ")) && (!stockIncomingKey_avlDetails.equals("-")) && (!stockIncomingKey_avlDetails.equals("nil")) ) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    final double[] outgoingqty_stockOutGngDetails_Double = {0};

                    final double[] Total_outgoingqty_stockOutGngDetails_Double = {0};
                    final double[] receivedStock_AvlDetails_double = {0};
                    final double[] finalStockBalance_double = {0};
                    final String[] tmcSubCtgyKey_stockOutGngDetails_String = {""};

                    final String[] outgoingtype_stockOutGngDetails_String = {""};
                    final String[] stockincomingkey_stockOutGngDetails_String = {""};
                    final String[] stocktype_stockOutGngDetails_String = {""};
                    final String[] outgoingqty_stockOutGngDetails_String = {""};

                    Total_outgoingqty_stockOutGngDetails_Double[0] = 0;
                    finalStockBalance_double[0] = 0;
                    outgoingqty_stockOutGngDetails_Double[0] = 0;
                    stocktype_stockOutGngDetails_String[0] = "";
                    outgoingtype_stockOutGngDetails_String[0] = "";
                    stockincomingkey_stockOutGngDetails_String[0] = "";
                    outgoingqty_stockOutGngDetails_String[0] = "0";
                    receivedStock_AvlDetails_double[0] = 0;
     /*   Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {

            }
        };


        new Thread(runnable).start();//to work in Background
        new Handler().postDelayed(runnable, 500 );//where 500 is delayMillis  // to work on mainThread


      */

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofStockOutGoingDetailsForStockIncmgKey + stockIncomingKey_avlDetails, null,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {


                                    try {
                                       // Log.d(TAG, " response: onMobileAppData " + response);

                                       // Log.i(TAG, "getStock incoming name" + itemName);

                                        JSONArray JArray = response.getJSONArray("content");

                                        int arrayLength = JArray.length();
                                        for (int i = 0; i < arrayLength; i++) {
                                            JSONObject json = JArray.getJSONObject(i);
                                            outgoingqty_stockOutGngDetails_Double[0] = 0;
                                            stocktype_stockOutGngDetails_String[0] = "";
                                            outgoingtype_stockOutGngDetails_String[0] = "";
                                            stockincomingkey_stockOutGngDetails_String[0] = "";
                                            outgoingqty_stockOutGngDetails_String[0] = "0";
                                            receivedStock_AvlDetails_double[0] = 0;



                                            try {
                                                if (json.has("outgoingtype")) {
                                                    outgoingtype_stockOutGngDetails_String[0] = (json.getString("outgoingtype"));
                                                } else {
                                                    outgoingtype_stockOutGngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                outgoingtype_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }
                                            if(!outgoingtype_stockOutGngDetails_String[0].equals(Constants.SALES_CANCELLED_OUTGOINGTYPE)){

                                                try {
                                                    if (json.has("outgoingqty")) {
                                                        outgoingqty_stockOutGngDetails_String[0] = (json.getString("outgoingqty"));
                                                    } else {
                                                        outgoingqty_stockOutGngDetails_String[0] = "0";
                                                    }
                                                } catch (Exception e) {
                                                    outgoingqty_stockOutGngDetails_String[0] = "0";

                                                    e.printStackTrace();
                                                }


                                            try {
                                                if (json.has("stocktype")) {
                                                    stocktype_stockOutGngDetails_String[0] = (json.getString("stocktype"));
                                                } else {
                                                    stocktype_stockOutGngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                stocktype_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }


                                         //   Log.i(TAG, "getStock incoming stocktype_stockOutGngDetails_String" + stocktype_stockOutGngDetails_String[0]);

                                         //   Log.i(TAG, "getStock incoming outgoingtype_stockOutGngDetails_String" + outgoingtype_stockOutGngDetails_String[0]);


                                                try {
                                                    if (json.has("tmcsubctgykey")) {
                                                        tmcSubCtgyKey_stockOutGngDetails_String[0] = (json.getString("tmcsubctgykey"));
                                                    } else {
                                                        tmcSubCtgyKey_stockOutGngDetails_String[0] = "";
                                                    }
                                                } catch (Exception e) {
                                                    tmcSubCtgyKey_stockOutGngDetails_String[0] = "";

                                                    e.printStackTrace();
                                                }



                                        //    Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_String" + outgoingqty_stockOutGngDetails_String[0]);


                                            try {
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                         //   Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


                                            try {
                                                if (json.has("stockincomingkey")) {
                                                    stockincomingkey_stockOutGngDetails_String[0] = (json.getString("stockincomingkey"));
                                                } else {
                                                    stockincomingkey_stockOutGngDetails_String[0] = "";
                                                }
                                            } catch (Exception e) {
                                                stockincomingkey_stockOutGngDetails_String[0] = "";

                                                e.printStackTrace();
                                            }

                                          //  Log.i(TAG, "getStock incoming stockincomingkey_stockOutGngDetails_String" + stockincomingkey_stockOutGngDetails_String[0]);


                                            try {
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                          //  Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


                                            if (outgoingtype_stockOutGngDetails_String[0].equals(Constants.SUPPLYGAP_OUTGOINGTYPE)) {
                                             //   Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 1 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                             //   Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 1 " + outgoingqty_stockOutGngDetails_Double[0]);


                                                try {
                                                    //  if (Total_outgoingqty_stockOutGngDetails_Double[0] > outgoingqty_stockOutGngDetails_Double[0]) {
                                                    //    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];
                                                    //     Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 2 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                    //     Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 2 " + outgoingqty_stockOutGngDetails_Double[0]);

                                                    // }
                                                    //else {
                                                    //      Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 2.1 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                    //      Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 2.1  " + outgoingqty_stockOutGngDetails_Double[0]);

                                                    //       Total_outgoingqty_stockOutGngDetails_Double[0] = outgoingqty_stockOutGngDetails_Double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                                    //   }
                                                    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            } else {
                                                try {
                                                 //   Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 3 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                 //  Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 3 " + outgoingqty_stockOutGngDetails_Double[0]);

                                                    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] + outgoingqty_stockOutGngDetails_Double[0];
                                                //    Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 4 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                 //   Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 4 " + outgoingqty_stockOutGngDetails_Double[0]);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        }
                                    }

                                      //  Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);


                                       // Log.i(TAG, "getStock incoming receivedStock_AvlDetails  " + receivedStock_AvlDetails);

                                        try {
                                            receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedStock_AvlDetails);
                                           // Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        double stockBalanceBeforeMinusCurrentItem = 0;
                                        try {
                                          //  Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double 2  " + receivedStock_AvlDetails_double[0]);
                                          //  Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double  5  " + Total_outgoingqty_stockOutGngDetails_Double[0]);

                                            stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                          //  Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 2  " + stockBalanceBeforeMinusCurrentItem);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        try {


                                            // Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 3  " + stockBalanceBeforeMinusCurrentItem);

                                            finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem - currentBillingItemWeight_double;

                                          //  Log.i(TAG, "getStock incoming currentBillingItemWeight_double 4 " + currentBillingItemWeight_double);
                                           // Log.i(TAG, "getStock incoming finalStockBalance_double 4 " + finalStockBalance_double[0]);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuItemKey_avlDetails, stockIncomingKey_avlDetails, itemName, barcode);

                                        // UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0]);

                                        AddDataInStockOutGoingTable(currentBillingItemWeight_double, orderid, stockIncomingKey_avlDetails, itemName, barcode, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey);

                                        if (isitemAvailable) {

                                            if (finalStockBalance_double[0] <= 0) {

                                                if (!allowNegativeStock) {


                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], true, false, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                                } else {
                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                                }


                                            } else {
                                                UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                            }
                                        } else {
                                            UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                        }

                                    } catch (Exception e) {
                                        UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                        e.printStackTrace();
                                    }


                                }

                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(@NonNull VolleyError error) {
                            Log.d(TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

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
                            Toast.makeText(mContext, "Error in General App Data code :  " + errorCode, Toast.LENGTH_LONG).show();


                            showProgressBar(false);

                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getParams() throws AuthFailureError {
                            final Map<String, String> params = new HashMap<>();
                            params.put("modulename", "Mobile");
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
            };


            new Thread(runnable).start();//to work in Background

        }
        else{
            if(isOrderPlacedinOrderdetails){
                turnoffProgressBarAndResetArray();
            }

            Toast.makeText(mContext, "No  Menu Item Stock  details for " + itemName, Toast.LENGTH_LONG).show();

        }
    }

    private void AddDataInStockBalanceTransactionHistory(double finalStockBalance_double, double stockBalanceBeforeMinusCurrentItem, String menuItemKey_avlDetails, String stockIncomingKey_avlDetails, String itemName, String barcode) {

        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("barcode",String.valueOf(barcode));
            jsonObject.put("itemname", String.valueOf(itemName));
            jsonObject.put("transactiontime",String.valueOf(Currenttime));
            jsonObject.put("menuitemkey", String.valueOf(menuItemKey_avlDetails));
            jsonObject.put("newstockbalance",finalStockBalance_double);
            jsonObject.put("oldstockbalance", stockBalanceBeforeMinusCurrentItem);
            jsonObject.put("stockincomingkey",String.valueOf(stockIncomingKey_avlDetails));
            jsonObject.put("usermobileno", String.valueOf(usermobileNo));
            jsonObject.put("vendorkey", String.valueOf(vendorKey));




        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addEntry_StockTransactionHistory
                , jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                    //    showProgressBar(false);
                    }


                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(mContext,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);



    }



    private void UpdateStockBalanceinMenuItemStockAvlDetail(String key_avlDetails, double finalStockBalance_double, boolean changeItemAvailability, boolean isitemAvailable, String menuItemKey_avlDetails, String tmcSubCtgyKey, String itemName) {


      //  showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        if(changeItemAvailability){


            //Log.d(TAG, " uploaduserDatatoDB.");
            JSONObject jsonObject2 = new JSONObject();
            try {
                jsonObject2.put("key", menuItemKey_avlDetails);


                jsonObject2.put("itemavailability", isitemAvailable);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateMenuItemDetails,
                    jsonObject2, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {
                    //Log.d(Constants.TAG, "Response: " + response);

                    String message ="";
                    Log.d(TAG, "change menu Item " + response.length());
                    try {
                        message = response.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if(changeItemAvailability) {
                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < completemenuItem.size(); iterator_menuitemStockAvlDetails++) {

                            Modal_NewOrderItems modal_menuItemStockAvlDetails = completemenuItem.get(iterator_menuitemStockAvlDetails);

                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuItemId());

                            if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(false));
                                uploadMenuAvailabilityStatusTranscationinDB(usermobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorKey,Currenttime,menuItemKey_avlDetails,message, "", false, "");
                                savedMenuIteminSharedPrefrences(completemenuItem,iterator_menuitemStockAvlDetails);

                            }

                        }
                    }

                //    showProgressBar(false);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "Error: " + error.toString());
                    showProgressBar(false);
                    Toast.makeText(mContext,"Failed to  update Menu Item",Toast.LENGTH_LONG).show();

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
            Volley.newRequestQueue(mContext).add(jsonObjectRequest);





            try {
                jsonObject.put("key",key_avlDetails);
                jsonObject.put("lastupdatedtime",Currenttime);
                jsonObject.put("stockbalance", finalStockBalance_double);
                jsonObject.put("itemavailability",isitemAvailable);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                jsonObject.put("key",key_avlDetails);
                jsonObject.put("lastupdatedtime",Currenttime);
                jsonObject.put("stockbalance", finalStockBalance_double);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



/*

        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("key",key_avlDetails);
            jsonObject.put("lastupdatedtime",Currenttime);
            jsonObject.put("stockbalance", finalStockBalance_double);



        } catch (JSONException e) {
            e.printStackTrace();
        }

 */
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);
if(!key_avlDetails.equals("")) {
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_MenuItemStockAvlDetails
            ,
            jsonObject, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(@NonNull JSONObject response) {

            try {

                String message = response.getString("message");
                if (message.equals("success")) {
                    //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                    // showProgressBar(false);
                    if(changeItemAvailability) {
                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < completemenuItem.size(); iterator_menuitemStockAvlDetails++) {

                            Modal_NewOrderItems modal_menuItemStockAvlDetails = completemenuItem.get(iterator_menuitemStockAvlDetails);

                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuItemId());

                            if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                modal_menuItemStockAvlDetails.setItemavailability_AvlDetails(String.valueOf(isitemAvailable));
                                modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(isitemAvailable));
                                modal_menuItemStockAvlDetails.setStockbalance_AvlDetails(String.valueOf(finalStockBalance_double));
                                modal_menuItemStockAvlDetails.setAllownegativestock(String.valueOf(false));
                                uploadMenuAvailabilityStatusTranscationinDB(usermobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorKey,Currenttime,menuItemKey_avlDetails,message, "", false, "");
                                savedMenuIteminSharedPrefrences(completemenuItem,iterator_menuitemStockAvlDetails);

                            }

                        }
                    }

                } else {
                    Toast.makeText(mContext, "No  Menu Item Stock Avl details for " + itemName, Toast.LENGTH_LONG).show();

                }
                if (!shouldGetPrintNow_Global) {
                    turnoffProgressBarAndResetArray();


                }


            } catch (JSONException e) {
                showProgressBar(false);
                turnoffProgressBarAndResetArray();
                Toast.makeText(mContext, "Failed to  update Menu Item Stock Avl details", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(@NonNull VolleyError error) {
            //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
            //Log.d(Constants.TAG, "Error: " + error.getMessage());
            //Log.d(Constants.TAG, "Error: " + error.toString());
            showProgressBar(false);
            turnoffProgressBarAndResetArray();
            Toast.makeText(mContext, "Failed to  update Menu Item Stock Avl details", Toast.LENGTH_LONG).show();

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
    Volley.newRequestQueue(mContext).add(jsonObjectRequest);


}

    }


    private void savedMenuIteminSharedPrefrences(List<Modal_NewOrderItems> menuItem, int iterator_menuitemStockAvlDetails) {
        final SharedPreferences sharedPreferencesMenuitem = mContext.getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList",json );
        editor.apply();
        try {
            adapterNewOrderScreenFragmentMobile.notifyDataSetChanged();
            adapterNewOrderScreenFragmentMobile.notify();
            adapterNewOrderScreenFragmentMobile.notifyItemChanged(iterator_menuitemStockAvlDetails);

            adapterNewOrderScreenFragmentMobile.notifyAll();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private void uploadMenuAvailabilityStatusTranscationinDB(String userPhoneNumber, String menuItemName, boolean availability, String menuItemSubCtgykey, String vendorkey, String dateandtime, String menuItemKey, String message, String menuItemStockAvlDetailskey, boolean allowNegative, String itemStockAvlDetailskey) {


        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("itemname", menuItemName);
            jsonObject.put("status", availability);
            jsonObject.put("subCtgykey", menuItemSubCtgykey);
            jsonObject.put("transactiontime", dateandtime);
            jsonObject.put("mobileno", userPhoneNumber);
            jsonObject.put("vendorkey", vendorkey);
            jsonObject.put("menuitemkey", menuItemKey);
            jsonObject.put("transcationstatus", message);
            try {
                if (!menuItemStockAvlDetailskey.equals("")) {
                    jsonObject.put("menuitemstockavldetailskey", menuItemStockAvlDetailskey);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if ((!menuItemStockAvlDetailskey.equals("")) ) {
                    jsonObject.put("allownegativestock", allowNegative);

                }

            }
            catch (JSONException e) {
                e.printStackTrace();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addMenuavailabilityTransaction,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                //  showProgressBar(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                showProgressBar(false);

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }
    private void AddOrUpdateDatainCreditOrderDetailsTable(double newamountUserHaveAsCredit, String orderid, String usermobileno, String orderplacedTime, String transactionType, double payableAmountDouble) {


        if(isAddOrUpdateCreditOrderDetailsIsCalled){
            return;
        }
        isAddOrUpdateCreditOrderDetailsIsCalled =true;


        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("usermobileno", usermobileno);
            jsonObject.put("lastupdatedtime", orderplacedTime);
            jsonObject.put("totalamountincredit", Math.round(newamountUserHaveAsCredit));
            jsonObject.put("vendorkey", vendorKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String  apiString = "";
        if(transactionType.toUpperCase().equals("ADD")){
            apiString = Constants. api_addCreditOrderDetailsTable;
        }
        else if(transactionType.toUpperCase().equals("UPDATE")){
            apiString = Constants. api_UpdateCreditOrderDetailsTable;
        }



        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, apiString,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {

                        addCreditOrdersTransactionDetails(orderid,usermobileno,vendorKey,totalamountUserHaveAsCredit,payableAmountDouble,newamountUserHaveAsCredit,orderplacedTime,transactionType);
                    }
                    else{

                    }
                } catch (JSONException e) {
                    isAddOrUpdateCreditOrderDetailsIsCalled =false;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                isAddOrUpdateCreditOrderDetailsIsCalled =false;

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

    }

    private void addCreditOrdersTransactionDetails(String orderid, String usermobileno, String vendorKey, double oldamountUserHaveAsCredit, double payableAmountDouble, double newamountUserHaveAsCredit, String orderplacedTime, String transactionType) {


        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("vendorkey", vendorKey);

            jsonObject.put("usermobileno", usermobileno);
            jsonObject.put("transactiontime", orderplacedTime);
            jsonObject.put("transactiontype", Constants.CREDIT_AMOUNT_ADDED);
            jsonObject.put("orderid", orderid);
            jsonObject.put("oldamountincredit", oldamountUserHaveAsCredit);
            jsonObject.put("transactionvalue", Math.round(payableAmountDouble));
            jsonObject.put("newamountincredit",Math.round( newamountUserHaveAsCredit));

        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addCreditOrdersTransactionDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    isAddOrUpdateCreditOrderDetailsIsCalled =false;

                    if (message.equals("success")) {

                    }
                    else{

                    }
                } catch (JSONException e) {
                    isAddOrUpdateCreditOrderDetailsIsCalled =false;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                isAddOrUpdateCreditOrderDetailsIsCalled =false;

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);







    }

    private void printRecipt(String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String finaltoPayAmountinmethod, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String discountAmount, String ordertype, JSONArray itemDespArray) {
/*
        Handler    handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                tv.append("Hello World");
               ;
            }
        };

       handler.postDelayed(r, 10);


 */



showProgressBar(true);

        String OrderPlacedtime = "";
        String Orderid = "";
        String CouponDiscount ="";
        String OrderType = "";
        String PayableAmountfromArray = "";
        String PayableAmount = "";
        String PaymentMode = "";
        String MobileNumber ="";
        String TokenNo="";
        final String[] Notes = {""};
        final String[] Slotname = {""};
        final String[] SlotDate = {""};
        final String[] DeliveryTime = {""};
        final String[] DeliveryType = {""};
        final String[] DeliveryAmount = {""};
        final String[] DistanceFromStore = {""};
        final String[] Address = {""};

    try {
            new Thread(new Runnable() {
                public void run() {


                if(ordertype.equals(Constants.PhoneOrder)) {


                    try {
                        Slotname[0] = Constants.EXPRESS_DELIVERY_SLOTNAME;
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }




                    try {
                        SlotDate[0] = getDate();
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }
                    try {
                        DeliveryTime[0] =   getSlotTime("120 mins",orderplacedTime);

                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    try {
                        DeliveryType[0] = Constants.HOME_DELIVERY_DELIVERYTYPE;
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    try {
                        DeliveryAmount[0] = selected_Address_modal.getDeliveryCharge();
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    try {
                        DistanceFromStore[0] = selected_Address_modal.getDeliverydistance();
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }

                    try {
                        Address[0] = selected_Address_modal.getAddressline1();
                    }
                    catch (Exception e){
                        e.printStackTrace();

                    }




                    try {

                        JSONArray array = itemDespArray;
                        //Log.i("tag","array.length()"+ array.length());
                        String b = array.toString();
                        //  modal_manageOrders_pojo_class.setItemdesp_string(b);
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
                                try {
                                    BluetoothPrintDriver.printString("TokenNo : " + tokenNo);
                                    BluetoothPrintDriver.BT_Write("\r");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "Printer Is Not Connected", Toast.LENGTH_LONG).show();
                                }

                                BluetoothPrintDriver.LF();

                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 80);
                                BluetoothPrintDriver.printString("Order Id : " + orderid);
                                BluetoothPrintDriver.BT_Write("\r");
                                BluetoothPrintDriver.LF();


                                String finalitemname = "", finalitemNetweight = "", finalgrossweight = "", finalQuantity = "";
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

                                        if (fullitemName.length() > 18) {
                                            itemName = fullitemName.substring(0, 18);
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

                                        if (fullitemName.contains("(")) {
                                            int openbraces = fullitemName.indexOf("(");
                                            int closebraces = fullitemName.indexOf(")");
                                            System.out.println(fullitemName);
                                            itemName = fullitemName.substring(openbraces + 1, closebraces);
                                            System.out.println(itemName);

                                        }
                                        if (!itemName.matches("[a-zA-Z0-9]+")) {
                                            fullitemName = fullitemName.replaceAll(
                                                    "[^a-zA-Z0-9()]", "");
                                            fullitemName = fullitemName.replaceAll(
                                                    "[()]", " ");
                                            System.out.println("no english");

                                            System.out.println(fullitemName);

                                        } else {
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
                                if (tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                    BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                                    BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                                    BluetoothPrintDriver.SetLineSpacing((byte) 100);
                                    BluetoothPrintDriver.printString("Grill House  " + fullitemName);
                                    BluetoothPrintDriver.BT_Write("\r");
                                    BluetoothPrintDriver.LF();
                                } else if (tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                    BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                                    BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                                    BluetoothPrintDriver.SetLineSpacing((byte) 100);
                                    BluetoothPrintDriver.printString("Ready to Cook  " + fullitemName);
                                    BluetoothPrintDriver.BT_Write("\r");
                                    BluetoothPrintDriver.LF();
                                } else {
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
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    finalQuantity = String.valueOf(json.get("quantity"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    finalgrossweight = marinadesObject.getString("grossweight");


                                    if ((finalgrossweight.equals("")) || (finalgrossweight.equals(null)) || (finalgrossweight.equals(" - "))) {
                                        try {
                                            finalgrossweight = marinadesObject.getString("grossweightingrams");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        //Log.i("tag","grossweight Log   2 "+                grossweight);


                                    }

                                } catch (Exception e) {
                                    try {
                                        if (finalgrossweight.equals("")) {
                                            finalgrossweight = marinadesObject.getString("grossweightingrams");
                                            //Log.i("tag","grossweight Log   3 "+                grossweight);


                                        }
                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }
                                    e.printStackTrace();
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
                                BluetoothPrintDriver.printString("Netweight : " + finalitemNetweight);
                                BluetoothPrintDriver.BT_Write("\r");
                                BluetoothPrintDriver.LF();


                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetLineSpacing((byte) 60);
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 80);
                                BluetoothPrintDriver.printString("Quantity : " + finalQuantity);
                                BluetoothPrintDriver.BT_Write("\r");
                                BluetoothPrintDriver.LF();
                                BluetoothPrintDriver.LF();

                                BluetoothPrintDriver.FeedAndCutPaper((byte) 66, (byte) 40);

                            }


                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x05);
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
                            BluetoothPrintDriver.SetLineSpacing((byte) 100);
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            try {
                                BluetoothPrintDriver.printString("TokenNo : " + tokenno);
                                BluetoothPrintDriver.BT_Write("\r");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(mContext, "Printer Is Not Connected", Toast.LENGTH_LONG).show();
                            }

                            BluetoothPrintDriver.LF();

                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 80);
                            BluetoothPrintDriver.printString("Order Id : " + orderid);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                            String finalitemname = "", finalCutName = "", finalitemNetweight = "", finalgrossweight = "", finalQuantity = "";


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

                                    if (fullitemName.length() > 18) {
                                        itemName = fullitemName.substring(0, 18);
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

                                    if (fullitemName.contains("(")) {
                                        int openbraces = fullitemName.indexOf("(");
                                        int closebraces = fullitemName.indexOf(")");
                                        System.out.println(fullitemName);
                                        itemName = fullitemName.substring(openbraces + 1, closebraces);
                                        System.out.println(itemName);

                                    }
                                    if (!itemName.matches("[a-zA-Z0-9]+")) {
                                        fullitemName = fullitemName.replaceAll(
                                                "[^a-zA-Z0-9()]", "");
                                        fullitemName = fullitemName.replaceAll(
                                                "[()]", " ");
                                        System.out.println("no english");

                                        System.out.println(fullitemName);

                                    } else {
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

                            if (tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                                BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                                BluetoothPrintDriver.SetLineSpacing((byte) 100);
                                BluetoothPrintDriver.printString("Grill House  " + fullitemName);
                                BluetoothPrintDriver.BT_Write("\r");
                                BluetoothPrintDriver.LF();
                            } else if (tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetFontEnlarge((byte) 0x02);
                                BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                                BluetoothPrintDriver.SetLineSpacing((byte) 100);
                                BluetoothPrintDriver.printString("Ready to Cook  " + fullitemName);
                                BluetoothPrintDriver.BT_Write("\r");
                                BluetoothPrintDriver.LF();
                            } else {
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
                                if (json.has("cutname")) {
                                    finalCutName = json.getString("cutname");

                                } else {
                                    finalCutName = "";
                                }
                                //Log.i("tag","grossweight Log    "+                grossweight);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                finalitemNetweight = json.getString("netweight");
                                //Log.i("tag","grossweight Log    "+                grossweight);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                finalQuantity = json.getString("quantity");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                finalgrossweight = json.getString("grossweight");


                                if ((finalgrossweight.equals("")) || (finalgrossweight.equals(null)) || (finalgrossweight.equals(" - "))) {
                                    try {
                                        finalgrossweight = json.getString("grossweightingrams");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    //Log.i("tag","grossweight Log   2 "+                grossweight);


                                }

                            } catch (Exception e) {
                                try {
                                    if (finalgrossweight.equals("")) {
                                        finalgrossweight = json.getString("grossweightingrams");
                                        //Log.i("tag","grossweight Log   3 "+                grossweight);


                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            }


                            if ((finalCutName.length() > 0) && (!finalCutName.equals(null)) && (!finalCutName.equals("null"))) {
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

                                BluetoothPrintDriver.printString((finalCutName.toUpperCase()));
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
                            BluetoothPrintDriver.printString("Netweight : " + finalitemNetweight);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetLineSpacing((byte) 60);
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 80);
                            BluetoothPrintDriver.printString("Quantity : " + finalQuantity);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();
                            BluetoothPrintDriver.LF();

                            BluetoothPrintDriver.FeedAndCutPaper((byte) 66, (byte) 40);


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                    double oldSavedAmount = 0;
                    String CouponDiscount = "0";
                    String deliveryCharges = "0";


                    String Title = "The Meat Chop";

                    String GSTIN = "GSTIN :33AAJCC0055D1Z9";
                    String CurrentTime = getDate_and_time();


                    BluetoothPrintDriver.Begin();
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
                    else if(vendorKey.equals("vendor_6")){

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
                    BluetoothPrintDriver.printString("Order Placed time : " + orderplacedTime);
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();


                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("Order Id : " + orderid);
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
                    BluetoothPrintDriver.printString("PricePerKg         Weight*Qty         SUBTOTAL");
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();


                    BluetoothPrintDriver.Begin();
                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                    BluetoothPrintDriver.SetLineSpacing((byte) 80);
                    BluetoothPrintDriver.printString("----------------------------------------------");
                    BluetoothPrintDriver.BT_Write("\r");
                    BluetoothPrintDriver.LF();


                    try {


                        for (int i = 0; i < cart_item_list.size(); i++) {
                            double savedAmount;
                            String itemUniqueCode = cart_item_list.get(i);
                            Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);

                            String fullitemName = String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());
                       /* int indexofbraces = itemName.indexOf("(");
                        if (indexofbraces >= 0) {
                            itemName = itemName.substring(0, indexofbraces);

                        }
                        if (itemName.length() > 21) {
                            itemName = itemName.substring(0, 21);
                            itemName = itemName + "...";
                        }

                        */
                            String itemName = "";
                            String itemNameAfterBraces = "";

                            String tmcSubCtgyKey = String.valueOf(Objects.requireNonNull(modal_newOrderItems).getTmcsubctgykey());
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

                                    if (fullitemName.length() > 18) {
                                        itemName = fullitemName.substring(0, 18);
                                        itemName = itemName + "...";

                                        fullitemName = fullitemName.substring(0, 18);
                                        fullitemName = fullitemName + "...";
                                    }
                                    if (fullitemName.length() < 18) {
                                        itemName = fullitemName;

                                        fullitemName = fullitemName;

                                    }
                                } else {
                                    int indexofbraces = fullitemName.indexOf("(");
                                    if (indexofbraces >= 0) {
                                        itemName = fullitemName.substring(0, indexofbraces);

                                    }
                                    if (fullitemName.length() > 18) {
                                        itemName = fullitemName.substring(0, 18);
                                        itemName = itemName + "...";
                                    }
                                    if (fullitemName.length() < 18) {
                                        itemName = fullitemName;

                                    }
                                }
                            } catch (Exception e) {
                                itemName = fullitemName;

                                e.printStackTrace();
                            }


                            savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                            oldSavedAmount = savedAmount + oldSavedAmount;
                            String Gst = "Rs."+modal_newOrderItems.getGstAmount();
                            String subtotal ="Rs."+ modal_newOrderItems.getSubTotal_perItem();
                            String quantity = modal_newOrderItems.getQuantity();
                            String itemrate = "Rs."+modal_newOrderItems.getItemFinalPrice();
                            String priceperKg = "0", pricePerKgtoPrint ="0", weighttoPrint ="0";

                            try{
                                if(String.valueOf(modal_newOrderItems.getPricetypeforpos()).toUpperCase().equals(Constants.TMCPRICE)){
                                    priceperKg = String.valueOf("Rs." + modal_newOrderItems.getTmcprice());

                                }
                                else{
                                    priceperKg = String.valueOf("Rs." + modal_newOrderItems.getTmcpriceperkg());

                                }
                            }
                            catch (Exception e){
                                priceperKg ="0";
                                e.printStackTrace();
                            }



                            String weight = modal_newOrderItems.getItemFinalWeight();

                            try{
                                if(weight.equals("") || weight.equals("null") || weight.equals(null) || weight.equals("0")){
                                    try {

                                        if((!modal_newOrderItems.getGrossweight().equals("")) && (!modal_newOrderItems.getGrossweight().equals("null")) && (!modal_newOrderItems.getGrossweight().equals(null))){
                                            weight = modal_newOrderItems.getGrossweight().toString();

                                        }
                                        else  if((!modal_newOrderItems.getNetweight().equals("")) && (!modal_newOrderItems.getNetweight().equals("null")) && (!modal_newOrderItems.getNetweight().equals(null))){
                                            weight = modal_newOrderItems.getNetweight().toString();

                                        }
                                        else  if((!modal_newOrderItems.getPortionsize().equals("")) && (!modal_newOrderItems.getNetweight().equals("null")) && (!modal_newOrderItems.getNetweight().equals(null))){
                                            weight = modal_newOrderItems.getPortionsize().toString();

                                        }
                                        else{
                                            weight = modal_newOrderItems.getItemFinalWeight().toString();

                                        }


                                    } catch (Exception e) {
                                        weight = "0";
                                        e.printStackTrace();
                                    }
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }



                            String itemDespName_Weight_quantity = "";

                            //´ÖÌå
                            if(!weight.equals(" ")&&weight.length()>0) {
                                itemDespName_Weight_quantity = String.valueOf(fullitemName + "( " + weight + " )" + " * " + quantity);
                            }
                            else {

                                itemDespName_Weight_quantity = String.valueOf(fullitemName + " * " + quantity);
                            }
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(fullitemName);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();





                            if (weight.length() == 4) {
                                //14spaces
                                weighttoPrint = weight + "             ";
                            }
                            if (weight.length() == 5) {
                                //13spaces
                                weighttoPrint = weight + "            ";
                            }
                            if (weight.length() == 6) {
                                //12spaces
                                weighttoPrint = weight + "           ";
                            }
                            if (weight.length() == 7) {
                                //11spaces
                                weighttoPrint = weight + "          ";
                            }
                            if (weight.length() == 8) {
                                //10spaces
                                weighttoPrint = weight + "         ";
                            }
                            if (weight.length() == 9) {
                                //9spaces
                                weighttoPrint = weight + "        ";
                            }
                            if (weight.length() == 10) {
                                //8spaces
                                weighttoPrint = weight + "       ";
                            }
                            if (weight.length() == 11) {
                                //7spaces
                                weighttoPrint = weight + "      ";
                            }
                            if (weight.length() == 12) {
                                //6spaces
                                weighttoPrint = weight + "     ";
                            }
                            if (weight.length() == 13) {
                                //5spaces
                                weighttoPrint = weight + "  ";
                            }
                            if (weight.length() == 14) {
                                //4spaces
                                weighttoPrint = weight + "   ";
                            }
                            if (weight.length() == 15) {
                                //3spaces
                                weighttoPrint = weight + "  ";
                            }
                            if (weight.length() == 16) {
                                //2spaces
                                weighttoPrint = weight + " ";
                            }
                            if (weight.length() == 17) {
                                //1spaces
                                weighttoPrint = weight + "";
                            }
                            if (weight.length() == 18) {
                                //1spaces
                                weighttoPrint = weight + "";
                            }

                            if (priceperKg.length() == 1) {
                                //14spaces
                                pricePerKgtoPrint = priceperKg + "                ";
                            }
                            if (priceperKg.length() == 2) {
                                //14spaces
                                pricePerKgtoPrint = priceperKg + "               ";
                            }
                            if (priceperKg.length() == 3) {
                                //14spaces
                                pricePerKgtoPrint = priceperKg + "              ";
                            }

                            if (priceperKg.length() == 4) {
                                //14spaces
                                pricePerKgtoPrint = priceperKg + "             ";
                            }
                            if (priceperKg.length() == 5) {
                                //13spaces
                                pricePerKgtoPrint = priceperKg + "            ";
                            }
                            if (priceperKg.length() == 6) {
                                //12spaces
                                pricePerKgtoPrint = priceperKg + "          ";
                            }
                            if (priceperKg.length() == 7) {
                                //11spaces
                                pricePerKgtoPrint = priceperKg + "         ";
                            }
                            if (priceperKg.length() == 8) {
                                //10spaces
                                pricePerKgtoPrint = priceperKg + "         ";
                            }
                            if (priceperKg.length() == 9) {
                                //9spaces
                                pricePerKgtoPrint = priceperKg + "        ";
                            }
                            if (priceperKg.length() == 10) {
                                //8spaces
                                pricePerKgtoPrint = priceperKg + "       ";
                            }
                            if (priceperKg.length() == 11) {
                                //7spaces
                                pricePerKgtoPrint = priceperKg + "      ";
                            }
                            if (priceperKg.length() == 12) {
                                //6spaces
                                pricePerKgtoPrint = priceperKg + "     ";
                            }
                            if (priceperKg.length() == 13) {
                                //5spaces
                                pricePerKgtoPrint = priceperKg + "    ";
                            }
                            if (priceperKg.length() == 14) {
                                //4spaces
                                pricePerKgtoPrint = priceperKg + "   ";
                            }
                            if (priceperKg.length() == 15) {
                                //3spaces
                                pricePerKgtoPrint = priceperKg + "  ";
                            }
                            if (priceperKg.length() == 16) {
                                //2spaces
                                pricePerKgtoPrint = priceperKg + " ";
                            }
                            if (priceperKg.length() == 17) {
                                //1spaces
                                pricePerKgtoPrint = priceperKg + " ";
                            }
                            if (priceperKg.length() == 18) {
                                //1spaces
                                pricePerKgtoPrint = priceperKg + "";
                            }




                            if (itemrate.length() == 4) {
                                //14spaces
                                itemrate = itemrate + "                ";
                            }
                            if (itemrate.length() == 5) {
                                //13spaces
                                itemrate = itemrate + "               ";
                            }
                            if (itemrate.length() == 6) {
                                //12spaces
                                itemrate = itemrate + "              ";
                            }
                            if (itemrate.length() == 7) {
                                //11spaces
                                itemrate = itemrate + "             ";
                            }
                            if (itemrate.length() == 8) {
                                //10spaces
                                itemrate = itemrate + "            ";
                            }
                            if (itemrate.length() == 9) {
                                //9spaces
                                itemrate = itemrate + "           ";
                            }
                            if (itemrate.length() == 10) {
                                //8spaces
                                itemrate = itemrate + "          ";
                            }
                            if (itemrate.length() == 11) {
                                //7spaces
                                itemrate = itemrate + "         ";
                            }
                            if (itemrate.length() == 12) {
                                //6spaces
                                itemrate = itemrate + "        ";
                            }
                            if (itemrate.length() == 13) {
                                //5spaces
                                itemrate = itemrate + "       ";
                            }
                            if (itemrate.length() == 14) {
                                //4spaces
                                itemrate = itemrate + "      ";
                            }
                            if (itemrate.length() == 15) {
                                //3spaces
                                itemrate = itemrate + "     ";
                            }
                            if (itemrate.length() == 16) {
                                //2spaces
                                itemrate = itemrate + "    ";
                            }
                            if (itemrate.length() == 17) {
                                //1spaces
                                itemrate = itemrate + "   ";
                            }
                            if (itemrate.length() == 18) {
                                //1spaces
                                itemrate = itemrate + "  ";
                            }


                            if (Gst.length() == 7) {
                                //1spaces
                                Gst = Gst + "  ";
                            }
                            if (Gst.length() == 8) {
                                //0space
                                Gst = Gst + " ";
                            }
                            if (Gst.length() == 9) {
                                //no space
                                Gst = Gst;
                            }
                            if (subtotal.length() == 4) {
                                //5spaces
                                subtotal = "          " + subtotal;
                            }
                            if (subtotal.length() == 5) {
                                //6spaces
                                subtotal = "          " + subtotal;
                            }
                            if (subtotal.length() == 6) {
                                //8spaces
                                subtotal = "            " + subtotal;
                            }
                            if (subtotal.length() == 7) {
                                //7spaces
                                subtotal = "           " + subtotal;
                            }
                            if (subtotal.length() == 8) {
                                //6spaces
                                subtotal = "          " + subtotal;
                            }
                            if (subtotal.length() == 9) {
                                //5spaces
                                subtotal = "         " + subtotal;
                            }
                            if (subtotal.length() == 10) {
                                //4spaces
                                subtotal = "        " + subtotal;
                            }
                            if (subtotal.length() == 11) {
                                //3spaces
                                subtotal = "         " + subtotal;
                            }
                            if (subtotal.length() == 12) {
                                //2spaces
                                subtotal = "      " + subtotal;
                            }
                            if (subtotal.length() == 13) {
                                //1spaces
                                subtotal = "     " + subtotal;
                            }
                            if (subtotal.length() == 14) {
                                //no space
                                subtotal = "    " + subtotal;
                            }

                          /*  BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemrate+ Gst + subtotal);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                           */
                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(pricePerKgtoPrint+ weight+" * "+quantity + subtotal);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();



                        }


                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 65);
                        BluetoothPrintDriver.printString("----------------------------------------------");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        String totalRate = "Rs." +itemTotalwithoutGst;
                        String totalGst = "Rs." + taxAmount;
                        String totalSubtotal = "Rs." + new_totalAmount_withGst+ ".00";

                        if (totalRate.length() == 7) {
                            //10spaces
                            totalRate = totalRate + "             ";
                        }
                        if (totalRate.length() == 8) {
                            //9spaces
                            totalRate = totalRate + "            ";
                        }
                        if (totalRate.length() == 9) {
                            //8spaces
                            totalRate = totalRate + "           ";
                        }
                        if (totalRate.length() == 10) {
                            //7spaces
                            totalRate = totalRate + "          ";
                        }
                        if (totalRate.length() == 11) {
                            //6spaces
                            totalRate = totalRate + "          ";
                        }
                        if (totalRate.length() == 12) {
                            //5spaces
                            totalRate = totalRate + "        ";
                        }
                        if (totalRate.length() == 13) {
                            //4spaces
                            totalRate = totalRate + "       ";
                        }
                        if (totalRate.length() == 14) {
                            //4spaces
                            totalRate = totalRate + "      ";
                        }

                        if (totalGst.length() == 7) {
                            //1spaces
                            totalGst = totalGst + "   ";
                        }
                        if (totalGst.length() == 8) {
                            //0space
                            totalGst = totalGst + "  ";
                        }
                        if (totalGst.length() == 9) {
                            //no space
                            totalGst = totalGst+" ";
                        }

                        if (totalSubtotal.length() == 6) {
                            //8spaces
                            totalSubtotal = "          " + totalSubtotal;
                        }
                        if (totalSubtotal.length() == 7) {
                            //7spaces
                            totalSubtotal = "         " + totalSubtotal;
                        }
                        if (totalSubtotal.length() == 8) {
                            //6spaces
                            totalSubtotal = "        " + totalSubtotal;
                        }
                        if (totalSubtotal.length() == 9) {
                            //5spaces
                            totalSubtotal = "       " + totalSubtotal;
                        }
                        if (totalSubtotal.length() == 10) {
                            //4spaces
                            totalSubtotal = "      " + totalSubtotal;
                        }

                        if (totalSubtotal.length() == 11) {
                            //4spaces
                            totalSubtotal = "     " + totalSubtotal;
                        }

                        if (totalSubtotal.length() == 12) {
                            //4spaces
                            totalSubtotal = "    " + totalSubtotal;
                        }
                        if (totalSubtotal.length() == 13) {
                            //4spaces
                            totalSubtotal = "   " + totalSubtotal;
                        }
                        if (totalSubtotal.length() == 14) {
                            //4spaces
                            totalSubtotal = "  " + totalSubtotal;
                        }

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(totalRate +"   "+ totalSubtotal);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 65);
                        BluetoothPrintDriver.printString("----------------------------------------------");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();
                        CouponDiscount = "0";

                        CouponDiscount = discountAmount;

                        if (!CouponDiscount.equals("0")) {
                            CouponDiscount = "Rs. " + CouponDiscount + ".00";

                            if ((!CouponDiscount.equals("Rs.0.0")) && (!CouponDiscount.equals("Rs.0")) && (!CouponDiscount.equals("Rs.0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals("")) && (!CouponDiscount.equals("Rs. .00")) && (!CouponDiscount.equals("Rs..00"))) {

                                if (CouponDiscount.length() == 4) {
                                    //20spaces
                                    //NEW TOTAL =4
                                    CouponDiscount = "Discount Amount                           " + CouponDiscount;
                                }
                                if (CouponDiscount.length() == 5) {
                                    //21spaces
                                    //NEW TOTAL =5
                                    CouponDiscount = "Discount Amount                         " + CouponDiscount;
                                }
                                if (CouponDiscount.length() == 6) {
                                    //20spaces
                                    //NEW TOTAL =6
                                    CouponDiscount = "Discount Amount                        " + CouponDiscount;
                                }

                                if (CouponDiscount.length() == 7) {
                                    //19spaces
                                    //NEW TOTAL =7
                                    CouponDiscount = "Discount Amount                       " + CouponDiscount;
                                }
                                if (CouponDiscount.length() == 8) {
                                    //18spaces
                                    //NEW TOTAL =8
                                    CouponDiscount = "Discount Amount                      " + CouponDiscount;
                                }
                                if (CouponDiscount.length() == 9) {
                                    //17spaces
                                    //NEW TOTAL =9
                                    CouponDiscount = "Discount Amount                     " + CouponDiscount;
                                }
                                if (CouponDiscount.length() == 10) {
                                    //16spaces
                                    //NEW TOTAL =9
                                    CouponDiscount = "Discount Amount                    " + CouponDiscount;
                                }
                                if (CouponDiscount.length() == 11) {
                                    //15spaces
                                    //NEW TOTAL =9
                                    CouponDiscount = "Discount Amount                   " + CouponDiscount;
                                }
                                if (CouponDiscount.length() == 12) {
                                    //14spaces
                                    //NEW TOTAL =9
                                    CouponDiscount = "Discount Amount                  " + CouponDiscount;
                                }

                                if (CouponDiscount.length() == 13) {
                                    //13spaces
                                    //NEW TOTAL =9
                                    CouponDiscount = "Discount Amount                 " + CouponDiscount;
                                }

                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 85);
                                BluetoothPrintDriver.printString(" "+CouponDiscount);
                                BluetoothPrintDriver.BT_Write("\r");
                                BluetoothPrintDriver.LF();

                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 65);
                                BluetoothPrintDriver.printString("----------------------------------------------");
                                BluetoothPrintDriver.BT_Write("\r");
                                BluetoothPrintDriver.LF();



                            }


                        }

                        if(ordertype.toUpperCase().equals(Constants.PhoneOrder)) {
                            deliveryCharges = "0";
                            deliveryCharges = deliveryAmount_for_this_order;
                            if (!deliveryCharges.equals("0")) {
                                deliveryCharges = "Rs. " + deliveryCharges;

                                if ((!deliveryCharges.equals("Rs.0.0")) && (!deliveryCharges.equals("Rs.0")) && (!deliveryCharges.equals("Rs.0.00")) && (deliveryCharges != (null)) && (!deliveryCharges.equals("")) && (!deliveryCharges.equals("Rs. .00")) && (!deliveryCharges.equals("Rs..00"))) {

                                    if (deliveryCharges.length() == 4) {
                                        //20spaces
                                        //NEW TOTAL =4
                                        deliveryCharges = "Delivery Charge                           " + deliveryCharges;
                                    }
                                    if (deliveryCharges.length() == 5) {
                                        //21spaces
                                        //NEW TOTAL =5
                                        deliveryCharges = "Delivery Charge                         " + deliveryCharges;
                                    }
                                    if (deliveryCharges.length() == 6) {
                                        //20spaces
                                        //NEW TOTAL =6
                                        deliveryCharges = "Delivery Charge                        " + deliveryCharges;
                                    }

                                    if (deliveryCharges.length() == 7) {
                                        //19spaces
                                        //NEW TOTAL =7
                                        deliveryCharges = "Delivery Charge                       " + deliveryCharges;
                                    }
                                    if (deliveryCharges.length() == 8) {
                                        //18spaces
                                        //NEW TOTAL =8
                                        deliveryCharges = "Delivery Charge                      " + deliveryCharges;
                                    }
                                    if (deliveryCharges.length() == 9) {
                                        //17spaces
                                        //NEW TOTAL =9
                                        deliveryCharges = "Delivery Charge                     " + deliveryCharges;
                                    }
                                    if (deliveryCharges.length() == 10) {
                                        //16spaces
                                        //NEW TOTAL =9
                                        deliveryCharges = "Delivery Charge                    " + deliveryCharges;
                                    }
                                    if (deliveryCharges.length() == 11) {
                                        //15spaces
                                        //NEW TOTAL =9
                                        deliveryCharges = "Delivery Charge                   " + deliveryCharges;
                                    }
                                    if (deliveryCharges.length() == 12) {
                                        //14spaces
                                        //NEW TOTAL =9
                                        CouponDiscount = "Delivery Charge                  " + deliveryCharges;
                                    }

                                    if (deliveryCharges.length() == 13) {
                                        //13spaces
                                        //NEW TOTAL =9
                                        deliveryCharges = "Delivery Charge                 " + deliveryCharges;
                                    }

                                    BluetoothPrintDriver.Begin();
                                    BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                                    BluetoothPrintDriver.SetLineSpacing((byte) 85);
                                    BluetoothPrintDriver.printString(" " + deliveryCharges);
                                    BluetoothPrintDriver.BT_Write("\r");
                                    BluetoothPrintDriver.LF();

                                    BluetoothPrintDriver.Begin();
                                    BluetoothPrintDriver.SetAlignMode((byte) 0);
                                    BluetoothPrintDriver.SetLineSpacing((byte) 65);
                                    BluetoothPrintDriver.printString("----------------------------------------------");
                                    BluetoothPrintDriver.BT_Write("\r");
                                    BluetoothPrintDriver.LF();


                                }


                            }
                        }
                        String NetTotal = "Rs."+finaltoPayAmountinmethod;

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
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString(NetTotal);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 65);
                        BluetoothPrintDriver.printString("----------------------------------------------");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString("Earned Rewards : " +String.valueOf((int)(totalredeempointsusergetfromorder))+" Points");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 65);
                        BluetoothPrintDriver.printString("----------------------------------------------");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        if(payment_mode.toString().toUpperCase().equals(Constants.CREDIT)){

                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString("Old Amount need to Pay : Rs. " +String.valueOf(Math.round(totalamountUserHaveAsCredit)) + " \n");
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                        /*    BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString("total Amount need to be Paid = (Old amount + Current Bill Amount ) \n");
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                         */

                            String payableamountPrint = "";
                            try{
                                payableamountPrint = finaltoPayAmountinmethod;
                            }
                            catch (Exception e){
                                e.printStackTrace();

                            }


                            double payableamountdoublePrint =0;
                            try{
                                payableamountdoublePrint = Math.round(Double.parseDouble(String.valueOf(payableamountPrint)));
                            }
                            catch (Exception e){
                                payableamountdoublePrint = 0;
                                e.printStackTrace();

                            }




                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString("New Amount need to Pay : Rs. " +  String.valueOf(Math.round(totalamountUserHaveAsCredit+payableamountdoublePrint))+ " \n");
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();

                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 65);
                            BluetoothPrintDriver.printString("----------------------------------------------");
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();

                        }

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString("Ordertype : "+ordertype);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 65);
                        BluetoothPrintDriver.printString("----------------------------------------------");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();



                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString("PaymentMode : " +payment_mode);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();




                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 0);
                        BluetoothPrintDriver.SetLineSpacing((byte) 115);
                        BluetoothPrintDriver.printString("User Mobile : " +userMobile);
                        BluetoothPrintDriver.BT_Write("\n");
                        BluetoothPrintDriver.LF();


                        if(ordertype.equals(Constants.PhoneOrder)){


                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetAlignMode((byte) 49);
                            BluetoothPrintDriver.SetLineSpacing((byte) 110);
                            BluetoothPrintDriver.SetBold((byte) 0x08);//´ÖÌå
                            BluetoothPrintDriver.SetCharacterFont((byte)0);
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x07);
                            BluetoothPrintDriver.SetFontEnlarge((byte) 0x30);
                            BluetoothPrintDriver.printString("TOKENNO: "+tokenNo);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();





                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 90);
                            BluetoothPrintDriver.printString("Slot Name : "+ Slotname[0]);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 90);
                            BluetoothPrintDriver.printString("Slot Date : "+ SlotDate[0]);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();

                            if(Slotname[0].equals(Constants.EXPRESSDELIVERY_SLOTNAME)){

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
                            BluetoothPrintDriver.printString("Delivery time : "+ DeliveryTime[0]);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 90);
                            BluetoothPrintDriver.printString("Delivery type : "+ DeliveryType[0]);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 90);
                            BluetoothPrintDriver.printString("Distance from Store : "+ DistanceFromStore[0] +" Kms");
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
                            BluetoothPrintDriver.printString(Address[0]);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                            if(!Notes[0].equals("")) {

                                BluetoothPrintDriver.Begin();
                                BluetoothPrintDriver.SetAlignMode((byte) 0);
                                BluetoothPrintDriver.SetLineSpacing((byte) 70);
                                BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                                BluetoothPrintDriver.SetFontEnlarge((byte) 0x01);
                                BluetoothPrintDriver.SetFontEnlarge((byte) 0x10);
                                BluetoothPrintDriver.printString("Notes :" + Notes[0]);
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


                        }




                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 49);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString("Thank you for choosing us !!!");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                        BluetoothPrintDriver.FeedAndCutPaper((byte)66,(byte)50);


                        if (!isPrintedSecondTime) {

                            turnoffProgressBar(orderplacedTime,userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_item_list, cartItem_hashmap, payment_mode,discountAmount,ordertype,itemDespArray);
                        }
                        else {
                            turnoffProgressBarAndResetArray();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }
            catch (Exception e){
                showProgressBar(false);
                e.printStackTrace();
            }

    }

    private void turnoffProgressBar(String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String finaltoPayAmountinmethod, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String discountAmount, String ordertype, JSONArray itemDespArray) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                showProgressBar(false);

                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.RePrint_Instruction,
                        R.string.Yes_Text, R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                isPrintedSecondTime = true;

                                printRecipt(orderplacedTime,userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_item_list, cartItem_hashmap, payment_mode,discountAmount,ordertype, itemDespArray);

                            }

                            @Override
                            public void onNo() {

                                turnoffProgressBarAndResetArray();

                            }
                        });



                return;

            }
        });
    }

    private void turnoffProgressBarAndResetArray() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                customermobileno="";
                customerName ="";
                userAddressArrayList.clear();
                redeemPointsJson = new JSONObject();
                userAddressKeyArrayList.clear();
                selectedAddressKey = String.valueOf("");
                selectedAddress = String.valueOf("");
                userLatitude = String.valueOf("0");
                userLongitude = String.valueOf("0");
                deliveryDistance ="";
                user_key_toAdd_Address ="";

                fulladdress_textview.setText("");
                autoCompleteCustomerName_widget.setText("");
                if(!isCalledFromOrderTypeSpinner) {
                    orderTypeSpinner_newWidget.setSelection(0);
                }
                isCalledFromOrderTypeSpinner = false;

                selected_Address_modal = new Modal_Address();
                isPhoneOrderSelected = false;
                isAddressForPhoneOrderSelected = false;
                updateUserName = false;
                isNewUser = false;
                isAddress_Added_ForUser = false;
                isAddressForPhoneOrderSelected = false;
                isUsertype_AlreadyPhone = false;
                userFetchedManually = false;
                isStoreNumberSelected = false;

                cart_Item_List.clear();
                cartItem_hashmap.clear();
                ispaymentMode_Clicked = false;
                isOrderDetailsMethodCalled = false;
                shouldGetPrintNow_Global = false;
                isPaymentDetailsMethodCalled = false;
                isOrderTrackingDetailsMethodCalled = false;
                isCustomerOrdersTableServiceCalled  = false;
                new_to_pay_Amount = 0;
                old_taxes_and_charges_Amount = 0;
                newGst =0;

                old_total_Amount = 0;
                createEmptyRowInListView("empty");
                CallAdapter();
                discountAmount ="0";
                 deliveryAmount_for_this_order ="0";

                finaltoPayAmount = "0";
                new_totalAmount_withoutGst =0;
                isPhoneOrderSelected = false;
                isAddressForPhoneOrderSelected = false;

                isPrintedSecondTime = false;
                isUpdateCouponTransactionMethodCalled=false;
                totalAmounttopay=0;
                isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                finalamounttoPay=0;
                selectedPaymentMode="";
                sTime=0;
                finaltoPayAmountinmethod="";
                isStockOutGoingAlreadyCalledForthisItem = false;
                StockBalanceChangedForThisItemList.clear();

                showProgressBar(false);
                return;

            }
        });



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

    private void PlaceOrder_in_OrderItemDetails(String subCtgyKey, String itemnamee, String Grossweight, String itemweightt,
                                                String Netweight, String quantityy, String itemamountt,
                                                String discountamountt,
                                                String gstamountt, String vendorkeyy, String currenttime,
                                                long sTime, String vendorkey, String vendorName, String grossWeightingrams, double grossweightingrams_double){

        String orderid = String.valueOf(sTime);

        JSONObject jsonObject = new JSONObject();
        try {

            try {
                if ((!applieddiscountpercentage.equals("0")) && (!applieddiscountpercentage.equals("")) && (!applieddiscountpercentage.equals("null")) && (!applieddiscountpercentage.equals(null))) {
                    jsonObject.put("applieddiscountpercentage", applieddiscountpercentage);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try {
                if (isPhoneOrderSelected) {
                    jsonObject.put("appmarkuppercentage", appMarkupPercentage);

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            jsonObject.put("orderid", orderid);
            jsonObject.put("itemname", itemnamee);

            jsonObject.put("tmcsubctgykey", subCtgyKey);

            jsonObject.put("quantity", quantityy);
            try {

                if (itemweightt.equals("") || itemweightt == (null)) {
                    jsonObject.put("grossweight", Grossweight);
                    jsonObject.put("netweight", Grossweight);
                    if(Grossweight.equals("")){
                        jsonObject.put("netweight", Netweight);
                        jsonObject.put("grossweightingrams", Grossweight);

                    }
                    else
                    {
                        jsonObject.put("netweight", Grossweight);
                        if(grossweightingrams_double!=0) {
                            jsonObject.put("grossweightingrams", grossweightingrams_double);
                        }
                        else{
                            jsonObject.put("grossweightingrams", Grossweight);

                        }


                    }

                } else {
                    jsonObject.put("grossweight", itemweightt);
                    if(Grossweight.equals("")){
                        jsonObject.put("netweight", Netweight);
                        jsonObject.put("grossweightingrams", itemweightt);

                    }
                    else
                    {
                        jsonObject.put("netweight", itemweightt);
                        if(grossweightingrams_double!=0) {
                            jsonObject.put("grossweightingrams", grossweightingrams_double);
                        }
                        else{
                            jsonObject.put("grossweightingrams", itemweightt);

                        }
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            jsonObject.put("slotdate", getDate());

            jsonObject.put("discountamount", discountamountt);
            jsonObject.put("gstamount", gstamountt);
            jsonObject.put("vendorid", vendorkeyy);
            jsonObject.put("orderplacedtime", currenttime);
            jsonObject.put("tmcprice", itemamountt);
            jsonObject.put("vendorkey", vendorkey);
            jsonObject.put("vendorname", vendorName);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInOrderItemDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        //   printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);

                    }
                } catch (JSONException e) {
                    //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + error);

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);



    }


    private void PlaceOrder_in_OrderTrackingDetails(long sTime, String Currenttiime, String finaltoPayAmountinmethod) {

        if(isOrderTrackingDetailsMethodCalled){
            return;
        }

        isOrderTrackingDetailsMethodCalled = true;

        String orderid = String.valueOf(sTime);
        String orderplacedDate_time = getDate_and_time();
        double userLat_double = 0;
        double userLon_double = 0;
        double deliveryDistance_double =0;
        try{
            deliveryDistance = deliveryDistance.replaceAll("[^\\d.]", "");

        }
        catch (Exception e){
            deliveryDistance ="0";
            e.printStackTrace();
        }

        try{
            deliveryDistance_double = Double.parseDouble(deliveryDistance);
        }
        catch (Exception e){
            deliveryDistance_double =0;
        }


        try{
            userLatitude = userLatitude.replaceAll("[^\\d.]", "");

        }
        catch (Exception e){
            userLatitude ="0";
        e.printStackTrace();
        }

        try{
            userLat_double = Double.parseDouble(userLatitude);
        }
        catch (Exception e){
            userLat_double =0;
        }
        try{
            userLongitude = userLongitude.replaceAll("[^\\d.]", "");

        }
        catch (Exception e){
            userLongitude ="0";
            e.printStackTrace();
        }

        try{
            userLon_double = Double.parseDouble(userLongitude);
        }
        catch (Exception e){
            userLon_double =0;
        }
        SharedPreferences sh
                = mContext.getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);
        String vendorkey = sh.getString("VendorKey","");

        JSONObject  orderTrackingTablejsonObject = new JSONObject();
        try {
            orderTrackingTablejsonObject.put("orderplacedtime",Currenttime);
            if(orderdetailsnewschema){

                orderTrackingTablejsonObject.put("slotdate",getDate());

            }
            orderTrackingTablejsonObject.put("usermobileno","+91" + customermobileno);
            orderTrackingTablejsonObject.put("orderid",orderid);
            orderTrackingTablejsonObject.put("vendorkey",vendorkey);
            if(isPhoneOrderSelected) {
                orderTrackingTablejsonObject.put("orderconfirmedtime",Currenttime);
                orderTrackingTablejsonObject.put("useraddresskey", selectedAddressKey);
                orderTrackingTablejsonObject.put("orderstatus",Constants.CONFIRMED_ORDER_STATUS);

                orderTrackingTablejsonObject.put("useraddresslat", userLat_double);
                orderTrackingTablejsonObject.put("useraddresslong", userLon_double);
                orderTrackingTablejsonObject.put("deliverydistanceinkm",deliveryDistance_double);

/*
                orderTrackingTablejsonObject.put("orderstatus","DELIVERED");
                orderTrackingTablejsonObject.put("orderdeliverytime",Currenttime);



 */
            }
            else{
                orderTrackingTablejsonObject.put("orderstatus","DELIVERED");
                orderTrackingTablejsonObject.put("orderdeliverytime",Currenttime);

            }

        }


        catch (JSONException e) {
            e.printStackTrace();

        }


        String Api_To_PlaceOrderInTrackingDetails = "";
        if(orderdetailsnewschema){
            Api_To_PlaceOrderInTrackingDetails = Constants.api_AddVendorTrackingOrderDetails;

        }
        else{
            Api_To_PlaceOrderInTrackingDetails = Constants.api_addOrderDetailsInOrderTrackingDetailsTable;

        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api_To_PlaceOrderInTrackingDetails,
                orderTrackingTablejsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                try {
                    String message = response.getString("message");
                    if(message .equals( "success")){
                        // printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        isOrderTrackingDetailsMethodCalled = false;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isOrderTrackingDetailsMethodCalled = false;

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                isOrderTrackingDetailsMethodCalled = false;

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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);





    }



    private void PlaceOrder_in_PaymentTransactionDetails(long sTime, String paymentmode, String transactionAmount, String userMobile) {
        if(isPaymentDetailsMethodCalled){
            return;
        }


        isPaymentDetailsMethodCalled = true;

        String orderid = String.valueOf(sTime);

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);
            jsonObject.put("mobileno", userMobile);
            jsonObject.put("merchantorderid", "");
            jsonObject.put("paymentmode", paymentmode);
            jsonObject.put("paymenttype", "");
            jsonObject.put("transactiontime", Currenttime);
            jsonObject.put("transactionamount", transactionAmount);
            jsonObject.put("status", "SUCCESS");
            jsonObject.put("merchantpaymentid", "");
            jsonObject.put("desp", "");



        }


        catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInPaymentDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response for PlaceOrder_in_OrderItemDetails: " + response);
                try {
                    String message = response.getString("message");
                    if(message .equals( "success")){
                        // printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        isPaymentDetailsMethodCalled = false;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isPaymentDetailsMethodCalled = false;

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                isPaymentDetailsMethodCalled = false;

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }


    private void updateRedeemPointsDetailsInDBWithoutkey(String userMobile, double totalAmounttopay, double totalredeempointsusergetfromorder) {


        if(isUpdateRedeemPointsWithoutKeyMethodCalled){
            return;
        }
        isUpdateRedeemPointsWithoutKeyMethodCalled =true;



        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("mobileno", String.valueOf( userMobile));
            jsonObject.put("totalordervalue",(int) Math.round( totalAmounttopay));
            jsonObject.put("totalredeempoints", (int) Math.round( totalredeempointsusergetfromorder));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateRedeemPointsTablewithoutKey,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        Log.d(TAG, "Points Redeem details uploaded Succesfully " + response);
                    }
                    else{
                        Log.d(TAG, "Failed during uploading Points Redeem  details" + response);

                    }
                } catch (JSONException e) {
                    Log.d(TAG, "Failed during uploading Points Redeem  details" + response);
                    isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Log.d(TAG, "Failed during uploading Points Redeem  details" + error.toString());
                isUpdateRedeemPointsWithoutKeyMethodCalled=false;
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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);









    }

    private void addDatatoCouponTransactioninDB(String coupondiscountamount, String coupontype, String mobileno, String orderid, String transactiondate, String transactiontime, String vendorkey) {

        if(isUpdateCouponTransactionMethodCalled){
            return;
        }
        isUpdateCouponTransactionMethodCalled =true;


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("orderid", orderid);

            jsonObject.put("coupondiscountamount", Double.parseDouble(coupondiscountamount));
            jsonObject.put("coupontype", coupontype);
            jsonObject.put("mobileno", mobileno);
            jsonObject.put("transactiondate", transactiondate);
            jsonObject.put("transactiontime", transactiontime);
            jsonObject.put("vendorkey", vendorkey);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addCouponDetailsInCouponTranactionsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        Log.d(TAG, "Response for Coupon details: " + response);

                        //   printRecipt(taxAmount,payableAmount,orderid,cart_Item_List);
                    }
                    else{
                        //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);

                    }
                } catch (JSONException e) {
                    //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + response);
                    isUpdateCouponTransactionMethodCalled=false;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                //Log.d(Constants.TAG, "Failed  while PlaceOrder_in_OrderItemDetails: " + error);
                isUpdateCouponTransactionMethodCalled=false;

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
        // Make the request
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(jsonObjectRequest);




    }


    private void setupChat() {

        Log.d("TAG", "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver(mContext, mHandler);
    }


    @Override
    public void onDestroy() {
        try{
            if(localDBcheck) {
                if (tmcMenuItemSQL_db_manager != null) {
                    tmcMenuItemSQL_db_manager.close();
                    tmcMenuItemSQL_db_manager = null;
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroy();
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




                            try{
                                totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            String UserMobile = "+91" + customermobileno;

                            //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                            //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                            // updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);
                            if(isOrderPlacingMicroserviceisActive){
                                redeemPointsJson = new JSONObject();
                                try {
                                    redeemPointsJson.put("mobileno",UserMobile);
                                    redeemPointsJson.put("totalordervalue",totalAmounttopay);
                                    redeemPointsJson.put("havetodocalculation",true);
                                    redeemPointsJson.put("totalredeempoints",totalredeempointsusergetfromorder);
                                    redeemPointsJson.put("vendorname",vendorName);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            else{

                                updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

                            }


                            if(String.valueOf(selectedPaymentMode).toUpperCase().equals(Constants.CREDIT)){
                                GetDatafromCreditOrderDetailsTable(selectedPaymentMode,sTime,Currenttime);
                            }
                            else{
                                if(isOrderPlacingMicroserviceisActive) {

                                    try{
                                        if(orderdetailsnewschema){

                                            if(isPhoneOrderSelected){
                                                ordertype = Constants.PhoneOrder;
                                            }
                                            else{
                                                ordertype = Constants.POSORDER;

                                            }



                                            initAndCallingOrderPlacingModuleHandlerInterface(mContext, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, 0, 0);



                                        }

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();

                                    }


                                }
                                else {
                                    if (!isCustomerOrdersTableServiceCalled) {
                                        try {
                                            if (orderdetailsnewschema) {
                                                initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                                if (isPhoneOrderSelected) {
                                                    ordertype = Constants.PhoneOrder;
                                                } else {
                                                    ordertype = Constants.POSORDER;

                                                }
                                                isCustomerOrdersTableServiceCalled = true;
                                                Add_CustomerOrder_TrackingTable_AsyncTask asyncTask = new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName, "0");
                                                asyncTask.execute();

                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();

                                        }
                                    }
                                    if (!isOrderDetailsMethodCalled) {
                                        shouldGetPrintNow_Global = true;
                                        PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, selectedPaymentMode, sTime, finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                                    }
                                    if (!isOrderTrackingDetailsMethodCalled) {

                                        PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                                    }
                                }
                            }



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




                                        try{
                                            totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        String UserMobile = "+91" + customermobileno;

                                        //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                        //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                                        // updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);
                                        if(isOrderPlacingMicroserviceisActive){
                                            redeemPointsJson = new JSONObject();
                                            try {
                                                redeemPointsJson.put("mobileno",UserMobile);
                                                redeemPointsJson.put("totalordervalue",totalAmounttopay);
                                                redeemPointsJson.put("havetodocalculation",true);
                                                redeemPointsJson.put("totalredeempoints",totalredeempointsusergetfromorder);
                                                redeemPointsJson.put("vendorname",vendorName);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                        else{

                                            updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

                                        }



                                        if(String.valueOf(selectedPaymentMode).toUpperCase().equals(Constants.CREDIT)){
                                            GetDatafromCreditOrderDetailsTable(selectedPaymentMode,sTime,Currenttime);
                                        }
                                        else{
                                            if(isOrderPlacingMicroserviceisActive) {

                                                try{
                                                    if(orderdetailsnewschema){

                                                        if(isPhoneOrderSelected){
                                                            ordertype = Constants.PhoneOrder;
                                                        }
                                                        else{
                                                            ordertype = Constants.POSORDER;

                                                        }



                                                        initAndCallingOrderPlacingModuleHandlerInterface(mContext, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, 0, 0);



                                                    }

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();

                                                }


                                            }
                                            else {

                                                if (!isCustomerOrdersTableServiceCalled) {
                                                    try {
                                                        if (orderdetailsnewschema) {
                                                            initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                                            if (isPhoneOrderSelected) {
                                                                ordertype = Constants.PhoneOrder;
                                                            } else {
                                                                ordertype = Constants.POSORDER;

                                                            }
                                                            isCustomerOrdersTableServiceCalled = true;
                                                            Add_CustomerOrder_TrackingTable_AsyncTask asyncTask = new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName, "0");
                                                            asyncTask.execute();

                                                        }

                                                    } catch (Exception e) {
                                                        e.printStackTrace();

                                                    }
                                                }
                                                if (!isOrderDetailsMethodCalled) {
                                                    shouldGetPrintNow_Global = false;
                                                    PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, selectedPaymentMode, sTime, finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                                                }
                                                if (!isOrderTrackingDetailsMethodCalled) {

                                                    PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                                                }
                                            }
                                        }




                                        return;

                                    }
                                });
                    }

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
                if (resultCode == RESULT_OK) {
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
                if(defaultPrinterType.equals(Constants.PDF_PrinterType)){
                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                }
                else {


                    if (resultCode == RESULT_OK) {
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
                break;

            default:
                break;

        }
    }



    private void GetDatafromCreditOrderDetailsTable(String paymentMode, long sTime, String currenttime) {
        totalamountUserHaveAsCredit = 0;
        String mobileno =  "+91" + customermobileno;

        try {
            mobileno = URLEncoder.encode(mobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetCreditOrdersUsingMobilenoWithVendorkey +"?usermobileno="+mobileno+"&vendorkey="+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {

                            Log.d(Constants.TAG, " response: " + response);
                            try {
                                String jsonString = response.toString();
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray JArray = jsonObject.getJSONArray("content");
                                int i1 = 0;
                                int arrayLength = JArray.length();
                                if (arrayLength > 0){


                                for (; i1 < (arrayLength); i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        try {
                                            if (json.has("totalamountincredit")) {
                                                totalamountUserHaveAsCredit = Double.parseDouble(json.getString("totalamountincredit"));
                                            } else {
                                                totalamountUserHaveAsCredit = 0;
                                                Toast.makeText(mContext, "Can't get CreditOrder Details", Toast.LENGTH_LONG).show();

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            totalamountUserHaveAsCredit = 0;
                                        }


                                        if(isOrderPlacingMicroserviceisActive) {
                                            if (isPhoneOrderSelected) {
                                                ordertype = Constants.PhoneOrder;
                                            } else {
                                                ordertype = Constants.POSORDER;

                                            }


                                            double payableAmount_double = 0 , newamountUserHaveAsCredit =0;
                                            String usermobileno = "";
                                            try{
                                                usermobileno = "+91"+customermobileno;
                                            }
                                            catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            try{
                                                if((!finaltoPayAmountinmethod.equals("null")) && (!finaltoPayAmountinmethod.equals("")) && (!finaltoPayAmountinmethod.equals(null)) ){
                                                    payableAmount_double  = Double.parseDouble(finaltoPayAmountinmethod);

                                                }

                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }

                                            try {
                                                newamountUserHaveAsCredit = payableAmount_double + totalamountUserHaveAsCredit;
                                                initAndCallingOrderPlacingModuleHandlerInterface(mContext, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, newamountUserHaveAsCredit, totalamountUserHaveAsCredit);

                                            }
                                            catch (Exception e){
                                                e.printStackTrace();


                                            }




                                        }
                                        else {
                                            if (!isCustomerOrdersTableServiceCalled) {
                                                try {
                                                    if (orderdetailsnewschema) {
                                                        initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                                        if (isPhoneOrderSelected) {
                                                            ordertype = Constants.PhoneOrder;
                                                        } else {
                                                            ordertype = Constants.POSORDER;

                                                        }
                                                        isCustomerOrdersTableServiceCalled = true;
                                                        Add_CustomerOrder_TrackingTable_AsyncTask asyncTask = new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName, "0");
                                                        asyncTask.execute();

                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                            if (!isOrderDetailsMethodCalled) {

                                                shouldGetPrintNow_Global = false;
                                                PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, selectedPaymentMode, sTime, finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                                            }
                                            if (!isOrderTrackingDetailsMethodCalled) {

                                                PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(mContext, "Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                        totalamountUserHaveAsCredit = 0;
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else{
                                    totalamountUserHaveAsCredit = 0;


                                    if(isOrderPlacingMicroserviceisActive) {
                                        if (isPhoneOrderSelected) {
                                            ordertype = Constants.PhoneOrder;
                                        } else {
                                            ordertype = Constants.POSORDER;

                                        }


                                        double payableAmount_double = 0 , newamountUserHaveAsCredit =0;
                                        String usermobileno = "";
                                        try{
                                            usermobileno = "+91"+customermobileno;
                                        }
                                        catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try{
                                            if((!finaltoPayAmountinmethod.equals("null")) && (!finaltoPayAmountinmethod.equals("")) && (!finaltoPayAmountinmethod.equals(null)) ){
                                                payableAmount_double  = Double.parseDouble(finaltoPayAmountinmethod);

                                            }

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        try {
                                            newamountUserHaveAsCredit = payableAmount_double + totalamountUserHaveAsCredit;
                                            initAndCallingOrderPlacingModuleHandlerInterface(mContext, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, newamountUserHaveAsCredit, totalamountUserHaveAsCredit);

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();


                                        }



                                        //initAndCallingOrderPlacingModuleHandlerInterface(mContext, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName , isinventorycheck, 0, 0);

                                    }
                                    else {


                                        if (!isCustomerOrdersTableServiceCalled) {
                                            try {
                                                if (orderdetailsnewschema) {
                                                    initAndPlaceOrderinCustomerOrder_TrackingInterface(mContext);
                                                    if (isPhoneOrderSelected) {
                                                        ordertype = Constants.PhoneOrder;
                                                    } else {
                                                        ordertype = Constants.POSORDER;

                                                    }
                                                    isCustomerOrdersTableServiceCalled = true;
                                                    Add_CustomerOrder_TrackingTable_AsyncTask asyncTask = new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, NewOrderScreenFragment_mobile.cart_Item_List, NewOrderScreenFragment_mobile.cartItem_hashmap, selectedPaymentMode, discountAmount, Currenttime, customermobileno, ordertype, vendorKey, vendorName, sTime, finaltoPayAmountinmethod, selected_Address_modal, tokenNo, userStatus, customerName, "0");
                                                    asyncTask.execute();

                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();

                                            }
                                        }
                                        if (!isOrderDetailsMethodCalled) {
                                            shouldGetPrintNow_Global = false;
                                            PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, selectedPaymentMode, sTime, finaltoPayAmountinmethod, shouldGetPrintNow_Global);
                                        }
                                        if (!isOrderTrackingDetailsMethodCalled) {

                                            PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                                        }
                                    }
                            }
                            } catch (Exception e) {
                                Toast.makeText(mContext,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                totalamountUserHaveAsCredit =0;
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            showProgressBar(false);
                            Toast.makeText(mContext,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                            totalamountUserHaveAsCredit =0;
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


                Toast.makeText(mContext,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Mobile");
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


    private void saveredeemDetailsinSharePreferences(String maxpointsinaday, String minordervalueforredeem, String pointsfor100rs) {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences("RedeemData", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("maxpointsinaday", maxpointsinaday);
        editor.putString("minordervalueforredeem", minordervalueforredeem);
        editor.putString("pointsfor100rs", pointsfor100rs);


        editor.apply();





    }



}

