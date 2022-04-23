package com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dantsu.escposprinter.connection.DeviceConnection;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.Printer_POJO_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.Modal_MenuItemStockAvlDetails;
import com.meatchop.tmcpartner.Settings.Modal_MenuItem_Settings;
import com.pos.printer.Modal_USBPrinter;

import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.pos.printer.PrinterFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.pos.printer.AsyncEscPosPrint;
import com.pos.printer.AsyncUsbEscPosPrint;
import com.pos.printer.AsyncEscPosPrinter;
import com.pos.printer.usb.UsbPrintersConnectionsLocal;

import static android.content.Context.MODE_PRIVATE;
import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.meatchop.tmcpartner.Constants.TAG;
import static com.meatchop.tmcpartner.Constants.api_Update_MenuItemStockAvlDetails;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewOrders_MenuItem_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOrders_MenuItem_Fragment extends Fragment {
    private RecyclerView recyclerView;
    ListView listview;
    boolean isdataFetched = false;
    List<Modal_NewOrderItems> Category_List;

    public static List<Modal_NewOrderItems> menuItem;
    public static List<Modal_NewOrderItems> completemenuItem;
    Context mContext;
    public TextView redeemed_points_text_widget,total_item_Rs_text_widget,taxes_and_Charges_rs_text_widget,total_Rs_to_Pay_text_widget;
    Button procced_to_pay_widget,redeemPoints_button_widget,discount_button_widget,check_redeemPoints_widget;
    EditText mobileNo_Edit_widget;
    String Currenttime,MenuItems,FormattedTime,CurrentDate,formattedDate,CurrentDay,OrderTypefromSpinner;
    String portName = "USB";
    int portSettings=0,totalGstAmount=0;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0;
    public static HashMap<String,Modal_NewOrderItems> cartItem_hashmap = new HashMap();
    public static List<String> cart_Item_List;


    AutoCompleteTextView autoComplete_customerNameText_widget;

    static Adapter_CartItem_Recyclerview adapter_cartItem_recyclerview;
    static Adapter_CartItem_Listview adapter_cartItem_listview;
    TextView discount_Edit_widget,discount_rs_text_widget,customername_labelWidget;
    String discountAmount ="" ;
    String finaltoPayAmount="",maxpointsinaday_String="",minordervalueforredeem_String="",pointsfor100rs_String="",totalamounttoPaywithoutredeempoints="";
    String vendorKey="",vendorType="",usermobileNo="",finaltoPayAmountwithRedeemPoints="",
            redeemPoints_String="",redeemKey="",mobileno_redeemKey="",discountAmountalreadyusedtoday=""
            ,totalpointsredeemedalreadybyuser="",totalordervalue_tillnow="",totalredeempointsuserhave="";
    double maxpointsinaday_double,minordervalueforredeem_double,pointsfor100rs_double,totalAmounttopay,finalamounttoPay;
    double totalredeempointsusergetfromorder=0;
    double pointsalreadyredeemDouble,totalpointsuserhave_afterapplypoints,pointsenteredToredeem_double=0;
    String pointsenteredToredeem="";
    String ordertype;
    int new_totalAmount_withGst;
    int netTotaL;
    LinearLayout loadingPanel,loadingpanelmask,discountAmountLayout,redeemPointsLayout;

    boolean ispointsApplied_redeemClicked=false;
    boolean isProceedtoCheckoutinRedeemdialogClicked =false;
    boolean isRedeemDialogboxOpened=false;
    boolean isUpdateRedeemPointsMethodCalled=false;
    boolean isUpdateRedeemPointsWithoutKeyMethodCalled=false;
    boolean isMobileAppDataFetchedinDashboard=false;
    boolean isDiscountApplied=false;
    List<Modal_MenuItemStockAvlDetails> MenuItemStockAvlDetails=new ArrayList<>();
    private  boolean isStockOutGoingAlreadyCalledForthisItem =false;
    public static List<String> StockBalanceChangedForThisItemList;
    boolean isUpdateCouponTransactionMethodCalled=false;
    private  boolean isOrderDetailsMethodCalled =false;
    private  boolean isOrderTrackingDetailsMethodCalled =false;
    private  boolean isPaymentDetailsMethodCalled =false;
    boolean isproceedtoPay_Clicked =false, ispaymentMode_Clicked =false,isPrintedSecondTime=false;
    Spinner orderTypeSpinner;

    String balanceAmount_String= "0",amountRecieved_String= "0";
    double balanceAmount_double =0,amountRecieved_double =0;
    CheckBox useStoreNumberCheckBox;

    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String printerType_sharedPreference = "";
    String printerStatus_sharedPreference = "";

    double totalamountUserHaveAsCredit =0;
    public  boolean isAddOrUpdateCreditOrderDetailsIsCalled = false;
    public  boolean  isUpdateCreditOrderDetailsIsCalled = false;


    boolean isinventorycheck = false;

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
    long sTime =0;
    String finaltoPayAmountinmethod="";
    String mConnectedDeviceName ;
    boolean isPrinterCnnected = false;
    String printerName = "";
    String printerStatus= "";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.BillingScreen";

    Modal_USBPrinter modal_usbPrinter = new Modal_USBPrinter();
    List<Modal_WholeSaleCustomers> wholeSaleCustomersArrayList=new ArrayList<>();

    public NewOrders_MenuItem_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewOrders_MenuItem_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewOrders_MenuItem_Fragment newInstance(String data) {
        Bundle args = new Bundle();
        args.putString("menuItem", data);

        NewOrders_MenuItem_Fragment fragment = new NewOrders_MenuItem_Fragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getData() {

        return getArguments().getString("menuItem");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getWindow().getContext();
        cart_Item_List = new ArrayList<>();
        StockBalanceChangedForThisItemList = new ArrayList<>();
        menuItem = new ArrayList<>();
        Category_List = new ArrayList<>();

        completemenuItem = new ArrayList<>();

        cart_Item_List.clear();
        cartItem_hashmap.clear();
        StockBalanceChangedForThisItemList.clear();
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        //Log.d(TAG, "starting: ");
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingpanelmask = view.findViewById(R.id.loadingpanelmask);
        loadingPanel = view.findViewById(R.id.loadingPanel);
        orderTypeSpinner = view.findViewById(R.id.orderTypeSpinner);
        procced_to_pay_widget = view.findViewById(R.id.procced_to_pay_widget);
        mobileNo_Edit_widget = view.findViewById(R.id.Customer_mobileNo_Edit_widget);
        total_item_Rs_text_widget = view.findViewById(R.id.total_amount_text_widget);
        total_Rs_to_Pay_text_widget = view.findViewById(R.id.total_Rs_to_Pay_text_widget);
        taxes_and_Charges_rs_text_widget = view.findViewById(R.id.taxes_and_Charges_rs_text_widget);
        discount_Edit_widget  = view.findViewById(R.id.discount_Edit_widget);
        discount_button_widget = view.findViewById(R.id.discount_widget);
        discount_rs_text_widget = view.findViewById(R.id.discount_rs_text_widget);
        redeemPoints_button_widget = view.findViewById(R.id.redeemPoints_widget);
        redeemed_points_text_widget = view.findViewById(R.id.redeemed_points_text_widget);
        check_redeemPoints_widget = view.findViewById(R.id.check_redeemPoints_widget);
        redeemPointsLayout = view.findViewById(R.id.redeemPointsLayout);
        discountAmountLayout = view.findViewById(R.id.discountAmountLayout);
        useStoreNumberCheckBox = view.findViewById(R.id.useStoreNumberCheckBox);
        customername_labelWidget  = view.findViewById(R.id.customername_labelWidget);
        autoComplete_customerNameText_widget  = view.findViewById(R.id.autoComplete_customerNameText_widget);

        try{
            SharedPreferences shared_PF_PrinterData = mContext.getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
            printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));
            printerStatus_sharedPreference   = (shared_PF_PrinterData.getString("printerStatus", ""));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = shared.getString("VendorKey","");
            usermobileNo = (shared.getString("UserPhoneNumber", "+91"));
            vendorType = shared.getString("VendorType","");

            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));
            isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));

        }
        catch(Exception e){
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
        if(vendorType.equals(Constants.WholeSales_VendorType)){
            redeemPoints_button_widget.setVisibility(View.GONE);
            useStoreNumberCheckBox.setVisibility(View.GONE);
            check_redeemPoints_widget.setVisibility(View.GONE);
            autoComplete_customerNameText_widget.setVisibility(View.VISIBLE);
            customername_labelWidget.setVisibility(View.VISIBLE);
            getWholeSaleCustomerArrayFromSharedPreferences();

        }
        else{
            redeemPoints_button_widget.setVisibility(View.VISIBLE);
            useStoreNumberCheckBox.setVisibility(View.VISIBLE);
            check_redeemPoints_widget.setVisibility(View.VISIBLE);
            autoComplete_customerNameText_widget.setVisibility(View.GONE);
            customername_labelWidget.setVisibility(View.GONE);
        }


        addDatatoOrderTypeSpinner();
        redeemPointsLayout.setVisibility(View.GONE);
        //discountlayout visible
        discountAmountLayout.setVisibility(View.GONE);
        String dummytime = getDate_and_time();
             add_amount_ForBillDetails();

        useStoreNumberCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    if(vendorKey.equals("vendor_1")){
                        Objects.requireNonNull(mobileNo_Edit_widget).setText("8939189102");

                    }
                    else if(vendorKey.equals("vendor_2")){
                        Objects.requireNonNull(mobileNo_Edit_widget).setText("9597580128");

                    }
                    else{
                        Objects.requireNonNull(mobileNo_Edit_widget).setText(StoreLanLine);

                    }
                  //  mobileNo_Edit_widget.setText(StoreLanLine);
                }
                else{
                    mobileNo_Edit_widget.setText("");
                }
            }
        });
        try{
            if(maxpointsinaday_double==0||minordervalueforredeem_double==0||pointsfor100rs_double==0||(!isMobileAppDataFetchedinDashboard)){


                showProgressBar(true);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetPOSMobileAppData, null,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {


                                    try {

                                        Log.d(Constants.TAG, " response: " + response);
                                        try {
                                            String jsonString =response.toString();
                                            Log.d(Constants.TAG, " response: onMobileAppData " + response);
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
                                                            Toast.makeText(mContext,"Can't  Details", Toast.LENGTH_LONG).show();

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
                            Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                            Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                            Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());
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


        check_redeemPoints_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deliveryUserMobileNumber ="+91"+mobileNo_Edit_widget.getText().toString();

                if (deliveryUserMobileNumber.length() == 13) {
                    String deliveryUserMobileNumberEncoded  = deliveryUserMobileNumber;
                    try {
                        deliveryUserMobileNumberEncoded = URLEncoder.encode(deliveryUserMobileNumber, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    showProgressBar(true);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetRedeemPointsDetailsFortheUser+"?transactiondate="+CurrentDate+"&mobileno="+deliveryUserMobileNumberEncoded, null,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {


                                    try {
                                        String jsonString = response.toString();
                                        Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                        JSONObject jsonObject = new JSONObject(jsonString);

                                        String message = jsonObject.getString("message").toString().toUpperCase();
                                        JSONArray JArray = jsonObject.getJSONArray("content");
                                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                        int i1 = 0;
                                        int arrayLength = JArray.length();
                                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                        if ((message.equals("SUCCESS"))&&(arrayLength>0)){


                                            for (; i1 < (arrayLength); i1++) {

                                                try {
                                                    Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                                    Log.d("Constants.TAG", "redeem points Response: object 0 " + JArray.getJSONObject(0));
                                                    //  Log.d("Constants.TAG", "redeem points Response: array 0 " + JArray.getJSONArray(0));
                                                    JSONObject jsonObject1 = JArray.getJSONObject(i1);
                                                    try {
                                                        redeemKey = String.valueOf(jsonObject1.get("key"));
                                                    }
                                                    catch(Exception e){
                                                        e.printStackTrace();
                                                        redeemKey ="";
                                                    }

                                                    try {
                                                        mobileno_redeemKey = String.valueOf(jsonObject1.get("mobileno"));
                                                    }
                                                    catch(Exception e){
                                                        e.printStackTrace();
                                                        mobileno_redeemKey ="";
                                                    }

                                                    try {
                                                        discountAmountalreadyusedtoday = String.valueOf(jsonObject1.get("discountamountalreadyusedtoday"));
                                                    }
                                                    catch(Exception e){
                                                        e.printStackTrace();
                                                        discountAmountalreadyusedtoday ="";
                                                    }


                                                    try {
                                                        totalpointsredeemedalreadybyuser = String.valueOf(jsonObject1.get("pointsredeemed"));
                                                    }
                                                    catch(Exception e){
                                                        e.printStackTrace();
                                                        totalpointsredeemedalreadybyuser ="";
                                                    }


                                                    try {
                                                        totalordervalue_tillnow = String.valueOf(jsonObject1.get("totalordervalue"));
                                                    }
                                                    catch(Exception e){
                                                        e.printStackTrace();
                                                        totalordervalue_tillnow ="";
                                                    }


                                                    try {
                                                        totalredeempointsuserhave = String.valueOf(jsonObject1.get("totalredeempoints"));
                                                    }
                                                    catch(Exception e){
                                                        e.printStackTrace();
                                                        totalredeempointsuserhave ="";
                                                    }





                                                    Log.d("Constants.TAG", "redeem points Response: jsonObject1 0 " + jsonObject1.get("key"));
                                                    isProceedtoCheckoutinRedeemdialogClicked = false;
                                                    ispointsApplied_redeemClicked = false;


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if(!redeemKey.equals("")) {
                                                OpenRedeemDialogScreen(false);
                                            }
                                            else{
                                                isProceedtoCheckoutinRedeemdialogClicked = false;
                                                ispointsApplied_redeemClicked = false;
                                                AlertDialogClass.showDialog(getActivity(), Constants.RedeemPointsDetailsTryAgainInstruction , 0);
                                                showProgressBar(false);

                                            }

                                        }

                                        else{
                                            isProceedtoCheckoutinRedeemdialogClicked = false;
                                            ispointsApplied_redeemClicked = false;
                                            AlertDialogClass.showDialog(getActivity(), Constants.RedeemPointsDetailsIsNotExistedInstruction , 0);
                                            showProgressBar(false);

                                        }



                                    } catch (Exception e) {
                                        showProgressBar(false);

                                        e.printStackTrace();
                                    }


                                }

                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(@NonNull VolleyError error) {
                            Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());
                            isProceedtoCheckoutinRedeemdialogClicked = false;
                            ispointsApplied_redeemClicked = false;
                            AlertDialogClass.showDialog(getActivity(), Constants.RedeemPointsDetailsTryAgainInstruction , 0);
                            showProgressBar(false);

                            Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                            Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                            Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

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


                } else {
                    AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

                }
            }
        });


        redeemPoints_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDiscountApplied) {
                    discountAmount = discount_Edit_widget.getText().toString();
                }
                else{
                    discountAmount ="0";
                }
                if (discountAmount.equals("") || discountAmount.equals("0")) {
                   //  totalAmounttopay = Double.parseDouble(total_Rs_to_Pay_text_widget.getText().toString());
                    String deliveryUserMobileNumber ="+91"+mobileNo_Edit_widget.getText().toString();

                    if (totalAmounttopay >= minordervalueforredeem_double) {
                        if (deliveryUserMobileNumber.length() == 13) {
                            String deliveryUserMobileNumberEncoded  = deliveryUserMobileNumber;
                            try {
                                deliveryUserMobileNumberEncoded = URLEncoder.encode(deliveryUserMobileNumber, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            showProgressBar(true);
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetRedeemPointsDetailsFortheUser+"?transactiondate="+CurrentDate+"&mobileno="+deliveryUserMobileNumberEncoded, null,
                                    new com.android.volley.Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(@NonNull JSONObject response) {


                                            try {
                                                String jsonString = response.toString();
                                                Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                                JSONObject jsonObject = new JSONObject(jsonString);

                                                String message = jsonObject.getString("message").toString().toUpperCase();
                                                    JSONArray JArray = jsonObject.getJSONArray("content");
                                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                                int i1 = 0;
                                                int arrayLength = JArray.length();
                                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                                if ((message.equals("SUCCESS"))&&(arrayLength>0)){


                                                    for (; i1 < (arrayLength); i1++) {

                                                    try {
                                                        Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                                        Log.d("Constants.TAG", "redeem points Response: object 0 " + JArray.getJSONObject(0));
                                                        //  Log.d("Constants.TAG", "redeem points Response: array 0 " + JArray.getJSONArray(0));
                                                        JSONObject jsonObject1 = JArray.getJSONObject(i1);
                                                        try {
                                                            redeemKey = String.valueOf(jsonObject1.get("key"));
                                                        }
                                                        catch(Exception e){
                                                            e.printStackTrace();
                                                            redeemKey ="";
                                                        }

                                                        try {
                                                            mobileno_redeemKey = String.valueOf(jsonObject1.get("mobileno"));
                                                        }
                                                        catch(Exception e){
                                                            e.printStackTrace();
                                                            mobileno_redeemKey ="";
                                                        }

                                                        try {
                                                            discountAmountalreadyusedtoday = String.valueOf(jsonObject1.get("discountamountalreadyusedtoday"));
                                                        }
                                                        catch(Exception e){
                                                            e.printStackTrace();
                                                            discountAmountalreadyusedtoday ="";
                                                        }


                                                        try {
                                                            totalpointsredeemedalreadybyuser = String.valueOf(jsonObject1.get("pointsredeemed"));
                                                        }
                                                        catch(Exception e){
                                                            e.printStackTrace();
                                                            totalpointsredeemedalreadybyuser ="";
                                                        }


                                                        try {
                                                            totalordervalue_tillnow = String.valueOf(jsonObject1.get("totalordervalue"));
                                                        }
                                                        catch(Exception e){
                                                            e.printStackTrace();
                                                            totalordervalue_tillnow ="";
                                                        }


                                                        try {
                                                            totalredeempointsuserhave = String.valueOf(jsonObject1.get("totalredeempoints"));
                                                        }
                                                        catch(Exception e){
                                                            e.printStackTrace();
                                                            totalredeempointsuserhave ="";
                                                        }





                                                        Log.d("Constants.TAG", "redeem points Response: jsonObject1 0 " + jsonObject1.get("key"));
                                                        isProceedtoCheckoutinRedeemdialogClicked = false;
                                                        ispointsApplied_redeemClicked = false;


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                    if(!redeemKey.equals("")) {
                                                        OpenRedeemDialogScreen(true);
                                                    }
                                                    else{
                                                        isProceedtoCheckoutinRedeemdialogClicked = false;
                                                        ispointsApplied_redeemClicked = false;
                                                        AlertDialogClass.showDialog(getActivity(), Constants.RedeemPointsDetailsTryAgainInstruction , 0);
                                                        showProgressBar(false);

                                                    }

                                                }

                                                else{
                                                    isProceedtoCheckoutinRedeemdialogClicked = false;
                                                    ispointsApplied_redeemClicked = false;
                                                    AlertDialogClass.showDialog(getActivity(), Constants.RedeemPointsDetailsIsNotExistedInstruction , 0);
                                                    showProgressBar(false);

                                                }



                                            } catch (Exception e) {
                                                showProgressBar(false);

                                                e.printStackTrace();
                                            }


                                        }

                                    }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(@NonNull VolleyError error) {
                                    Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());
                                    isProceedtoCheckoutinRedeemdialogClicked = false;
                                    ispointsApplied_redeemClicked = false;
                                    AlertDialogClass.showDialog(getActivity(), Constants.RedeemPointsDetailsTryAgainInstruction , 0);
                                    showProgressBar(false);

                                    Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                                    Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                                    Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

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


                        } else {
                            AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

                        }
                    } else {
                        AlertDialogClass.showDialog(getActivity(), Constants.Order_Value_should_be_above + " " + minordervalueforredeem_String + " rs", 0);

                    }
                } else {
                    AlertDialogClass.showDialog(getActivity(), Constants.RedeemPoints_and_Discount_Instruction, 0);


                }
            }
        });


        orderTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrderTypefromSpinner = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected: " + OrderTypefromSpinner, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                OrderTypefromSpinner = "POS Order";
            }
        });


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        discount_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (cart_Item_List.size() > 0 && cartItem_hashmap.size() > 0) {
                        if((redeemed_points_text_widget.getText().toString().equals(""))||(redeemed_points_text_widget.getText().toString().equals("0"))){
                             finaltoPayAmount =total_item_Rs_text_widget.getText().toString();
                            double toPayAmtdouble = Double.parseDouble(finaltoPayAmount);

                            //Toast.makeText(mContext, "total  "+finaltoPayAmount, Toast.LENGTH_LONG).show();

                            if (toPayAmtdouble>0) {

                                discountAmount = discount_Edit_widget.getText().toString();
                                if (!discountAmount.equals("") &&(!discountAmount.equals("0"))) {
                                    double discountAmountdouble = Double.parseDouble(discountAmount);
                                    double toPayAmt = Double.parseDouble(finaltoPayAmount);
                                    if (toPayAmt > discountAmountdouble) {
                                        toPayAmt = toPayAmt - discountAmountdouble;
                                        int toPayAmountInt = (int) Math.round((toPayAmt));
                                        totalAmounttopay = toPayAmt;
                                        isDiscountApplied=true;
                                        discount_rs_text_widget.setText(discountAmount);

                                        total_Rs_to_Pay_text_widget.setText(String.valueOf(toPayAmountInt));
                                    }
                                    else{
                                        AlertDialogClass.showDialog(getActivity(), Constants.DiscountAmountInstruction, 0);

                                    }
                                }
                                else{
                               //     add_amount_ForBillDetails();
                                    discountAmount ="0";
                                    isDiscountApplied=false;
                                    discount_rs_text_widget.setText("0");
                                    checkIfNewUser();
                                }
                            }
                            else{
                                AlertDialogClass.showDialog(getActivity(), Constants.CantApplyDiscountbelowzeroOrdervalueInstruction, 0);

                            }
                        }
                        else{
                            AlertDialogClass.showDialog(getActivity(), Constants.RedeemPoints_and_Discount_Instruction2, 0);

                        }
                    }
                    else{
                        AlertDialogClass.showDialog(getActivity(), Constants.CantApplyDiscountInstruction, 0);

                    }
                }

                catch(Exception e ){
                        discountAmount = "0";
                    discount_rs_text_widget.setText(discountAmount);

                        e.printStackTrace();
                    }
                }


        });




        procced_to_pay_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (mobileNo_Edit_widget.getText().toString().length() == 10) {
                        showProgressBar(true);


                        if (cart_Item_List.size() > 0 && cartItem_hashmap.size() > 0) {

                        if ((!total_item_Rs_text_widget.getText().toString().equals("0")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals("0"))&&(!total_item_Rs_text_widget.getText().toString().equals("0.0")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals("0.0"))&&(!total_item_Rs_text_widget.getText().toString().equals("0.00")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals("0.00"))&&(!total_item_Rs_text_widget.getText().toString().equals("")) && (!total_Rs_to_Pay_text_widget.getText().toString().equals(""))) {
                            if (checkforBarcodeInCart("empty")) {
                                NewOrders_MenuItem_Fragment.cart_Item_List.remove("empty");

                                NewOrders_MenuItem_Fragment.cartItem_hashmap.remove("empty");
                            }
                            //GetStockBalanceForEachIteminCart();
                            long sTime = System.currentTimeMillis();
                            Currenttime = getDate_and_time();

                            //Log.i(TAG, "call adapter cart_Item " + cart_Item_List.size());

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Dialog dialog = new Dialog(getActivity());
                                            dialog.setContentView(R.layout.select_payment_mode_layout);
                                            dialog.setTitle("Select the Payment Mode ");
                                            dialog.setCanceledOnTouchOutside(true);

                                            Button via_credit = (Button) dialog.findViewById(R.id.via_credit);

                                            Button via_cash = (Button) dialog.findViewById(R.id.via_cash);
                                            Button via_card = (Button) dialog.findViewById(R.id.via_card);
                                            Button via_upi = (Button) dialog.findViewById(R.id.via_upi);
                                            TextView totalbillAmount = (TextView) dialog.findViewById(R.id.totalbillAmount);
                                            TextView balance_Amount = (TextView) dialog.findViewById(R.id.balance_Amount);
                                            EditText amount_Recieved_EditText = (EditText) dialog.findViewById(R.id.amount_Recieved_EditText);
                                            Button calculateBalanceAmount = (Button) dialog.findViewById(R.id.CalculateBalanceAmount);
                                            Button checkOut = (Button) dialog.findViewById(R.id.checkOut);
                                            LinearLayout paymentMode_selectionLayout = (LinearLayout) dialog.findViewById(R.id.paymentMode_selectionLayout);
                                            LinearLayout balanceAmountCalculate_Layout = (LinearLayout) dialog.findViewById(R.id.balanceAmountCalculate_Layout);
                                            paymentMode_selectionLayout.setVisibility(View.VISIBLE);
                                            balanceAmountCalculate_Layout.setVisibility(View.GONE);
                                            //Log.d(TAG, "Currenttime: " + Currenttime);

                                            //Log.i(TAG, "date and time " + sTime);
                                            String totalAmount_String  =  total_Rs_to_Pay_text_widget.getText().toString();
                                            double totalAmount_double = Double.parseDouble(totalAmount_String);
                                            totalbillAmount .setText(totalAmount_String);

                                            if ((!isProceedtoCheckoutinRedeemdialogClicked) && (isRedeemDialogboxOpened)) {
                                                Toast.makeText(mContext, "Redeem Points Not Applied", Toast.LENGTH_LONG).show();

                                            }

                                            calculateBalanceAmount.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    balanceAmount_String = "0";
                                                    amountRecieved_String = "0";
                                                    balanceAmount_double = 0;
                                                    amountRecieved_double = 0;
                                                    try {
                                                        amountRecieved_String = amount_Recieved_EditText.getText().toString();
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        amountRecieved_String = "0";
                                                        Toast.makeText(mContext, "can't get amountRecieved_String", Toast.LENGTH_LONG).show();
                                                    }
                                                    if (amountRecieved_String.length() > 0) {
                                                        try {
                                                            amountRecieved_double = Double.parseDouble(amountRecieved_String);
                                                        } catch (Exception e) {
                                                            Toast.makeText(mContext, "can't get amountRecieved_double", Toast.LENGTH_LONG).show();
                                                            amountRecieved_double = 0;

                                                            e.printStackTrace();
                                                        }
                                                        if (amountRecieved_double > 0) {
                                                            try {
                                                                balanceAmount_double = amountRecieved_double - totalAmount_double;
                                                            } catch (Exception e) {
                                                                Toast.makeText(mContext, "can't get balanceAmount_double", Toast.LENGTH_LONG).show();

                                                                e.printStackTrace();
                                                            }
                                                            try {
                                                                balanceAmount_String = String.valueOf(balanceAmount_double);
                                                            } catch (Exception e) {
                                                                Toast.makeText(mContext, "can't get balanceAmount_String", Toast.LENGTH_LONG).show();

                                                                e.printStackTrace();
                                                            }
                                                            try {
                                                                if (balanceAmount_double < 0) {
                                                                    balance_Amount.setTextColor(Color.RED);
                                                                } else {
                                                                    balance_Amount.setTextColor(Color.BLACK);

                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(mContext, "can't change balance_Amount color ", Toast.LENGTH_LONG).show();

                                                            }
                                                            try {
                                                                balance_Amount.setText(balanceAmount_String);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                                Toast.makeText(mContext, "can't get balance_Amount", Toast.LENGTH_LONG).show();

                                                            }
                                                        }
                                                        else{
                                                            AlertDialogClass.showDialog(getActivity(), Constants.RecievedAmountShouldBeGreaterthanZero , 0);

                                                        }
                                                    }
                                                    else {
                                                        AlertDialogClass.showDialog(getActivity(), Constants.RecievedAmountCantbeEmpty , 0);

                                                    }
                                                }
                                            });
                                                checkOut.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (amountRecieved_String.length() > 0) {
                                                            if (amountRecieved_double > 0) {

                                                            if (balanceAmount_double < 0) {
                                                              //  Toast.makeText(mContext, "Recieved Amount Should not be Less than total Amount", Toast.LENGTH_LONG).show();
                                                                AlertDialogClass.showDialog(getActivity(), Constants.RecievedAmountShouldBeGreaterthanTotalAmount , 0);

                                                            } else {
                                                                dialog.dismiss();

                                                                PlaceOrdersinDatabaseaAndPrintRecipt("CASH ON DELIVERY", sTime, Currenttime, cart_Item_List);
                                                            }
                                                            }
                                                            else{
                                                                AlertDialogClass.showDialog(getActivity(), Constants.RecievedAmountShouldBeGreaterthanZero , 0);

                                                            }
                                                        } else {
                                                            AlertDialogClass.showDialog(getActivity(), Constants.RecievedAmountCantbeEmpty, 0);

                                                        }
                                                    }
                                                });

                                            via_card.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();

                                                    PlaceOrdersinDatabaseaAndPrintRecipt("CARD", sTime, Currenttime, cart_Item_List);


                                                }
                                            });


                                            via_credit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();

                                                    PlaceOrdersinDatabaseaAndPrintRecipt("CREDIT", sTime, Currenttime, cart_Item_List);


                                                }
                                            });


                                            via_cash.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                  //  dialog.dismiss();

                                                    paymentMode_selectionLayout.setVisibility(View.GONE);
                                                    balanceAmountCalculate_Layout.setVisibility(View.VISIBLE);
                                                    //PlaceOrdersinDatabaseaAndPrintRecipt("CASH ON DELIVERY", sTime, Currenttime, cart_Item_List);


                                                }
                                            });


                                            via_upi.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialog.dismiss();

                                                    PlaceOrdersinDatabaseaAndPrintRecipt("UPI", sTime, Currenttime, cart_Item_List);


                                                }
                                            });


                                            dialog.show();
                                            showProgressBar(false);

                                        } catch (WindowManager.BadTokenException e) {
                                            showProgressBar(false);

                                            e.printStackTrace();
                                        }
                                    }
                                });




                        } else {
                            showProgressBar(false);

                            AlertDialogClass.showDialog(getActivity(), R.string.Cant_place_order);

                        }


                        } else {
                            AlertDialogClass.showDialog(getActivity(), R.string.Cart_is_empty);

                        }

                    } else {
                        AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

                    }


            }
        });





    }

    private void checkIfNewUser() {
        discountAmount ="0";
        isDiscountApplied=false;
        final String[] Count = {"0"};
        discount_rs_text_widget.setText("0");
        String deliveryUserMobileNumber ="";
        deliveryUserMobileNumber = "+91"+mobileNo_Edit_widget.getText().toString();
        if (deliveryUserMobileNumber.length() == 13) {
            String deliveryUserMobileNumberEncoded  = deliveryUserMobileNumber;
            try {
                deliveryUserMobileNumberEncoded = URLEncoder.encode(deliveryUserMobileNumber, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            showProgressBar(true);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsusingMobilenowithIndex +"?usermobile="+deliveryUserMobileNumberEncoded, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {


                            try {
                                String jsonString = response.toString();
                                Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String message = jsonObject.getString("message").toString().toUpperCase();
                                JSONArray JArray = jsonObject.getJSONArray("content");
                                 Count[0] = jsonObject.getString("contentlength");

                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1 = 0;
                                int arrayLength = JArray.length();
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                if ((message.equals("SUCCESS"))&&(arrayLength>0)){


                                    OpenDiscountDialogScreen(false, Count[0]);


                                }

                                else{

                                    OpenDiscountDialogScreen(true, Count[0]);

                                  //  AlertDialogClass.showDialog(getActivity(), Constants.RedeemPointsDetailsIsNotExistedInstruction , 0);
                                    showProgressBar(false);

                                }



                            } catch (Exception e) {
                                showProgressBar(false);
                                discountAmount ="0";
                                isDiscountApplied=false;
                                discount_rs_text_widget.setText("0");
                                OpenDiscountDialogScreen(true, Count[0]);

                                e.printStackTrace();
                            }


                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());
                    OpenDiscountDialogScreen(true, Count[0]);

                    showProgressBar(false);

                    Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getLocalizedMessage());
                    Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.getMessage());
                    Log.d(Constants.TAG, "getDeliveryPartnerList Error: " + error.toString());

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


        } else {
            AlertDialogClass.showDialog(getActivity(), R.string.Enter_the_mobile_no_text);

        }
    }

    private void OpenDiscountDialogScreen(boolean isnewUser, String count) {

        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  Dialog dialog = new Dialog(getActivity());
                                  dialog.setContentView(R.layout.apply_discount_layout);
                                  showProgressBar(false);
                                  TextView usermobileno_textwidget = (TextView) dialog.findViewById(R.id.usermobileno_textwidget);
                                  TextView orderid_count_textwidget = (TextView) dialog.findViewById(R.id.orderid_count_textwidget);
                                  TextView couponDiscount_detailsTextWidget = (TextView) dialog.findViewById(R.id.couponDiscount_detailsTextWidget);
                                  TextView storeDiscount_detailsTextWidget = (TextView) dialog.findViewById(R.id.storeDiscount_detailsTextWidget);
                                  Button applyDiscountButton = (Button) dialog.findViewById(R.id.applyDiscountButton);
                                  EditText discount_edit_textwidget = (EditText) dialog.findViewById(R.id.discount_edit_textwidget);

                                  orderid_count_textwidget.setText(count);
                                  if(isnewUser){
                                      couponDiscount_detailsTextWidget.setVisibility(View.VISIBLE);
                                      storeDiscount_detailsTextWidget.setVisibility(View.GONE);

                                  }
                                  else{
                                      couponDiscount_detailsTextWidget.setVisibility(View.GONE);
                                      storeDiscount_detailsTextWidget.setVisibility(View.VISIBLE);

                                  }

                                  usermobileno_textwidget.setText(mobileNo_Edit_widget.getText().toString());


                                  applyDiscountButton.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          discountAmount = discount_edit_textwidget.getText().toString();
                                          if(discountAmount.equals("") || discountAmount.equals(" ") || discountAmount.equals("null") || discountAmount.equals("NULL") || discountAmount.equals(null)){
                                              discountAmount = "0";
                                          }
                                          double discountAmountdouble = Double.parseDouble(discountAmount);
                                          finaltoPayAmount =total_item_Rs_text_widget.getText().toString();
                                          double toPayAmt = Double.parseDouble(finaltoPayAmount);
                                          // Toast.makeText(mContext, "total  "+finaltoPayAmount, Toast.LENGTH_LONG).show();

                                          if (toPayAmt > discountAmountdouble) {
                                              toPayAmt = toPayAmt - discountAmountdouble;
                                              int toPayAmountInt = (int) Math.round((toPayAmt));
                                              totalAmounttopay = toPayAmt;
                                              isDiscountApplied=true;
                                              discount_rs_text_widget.setText(discountAmount);

                                              total_Rs_to_Pay_text_widget.setText(String.valueOf(toPayAmountInt));

                                              dialog.cancel();

                                          }
                                          else{
                                              AlertDialogClass.showDialog(getActivity(), Constants.DiscountAmountInstruction, 0);
                                              discountAmount ="0";
                                              isDiscountApplied=false;
                                              discount_rs_text_widget.setText("0");
                                          }
                                      }
                                  });



                                  dialog.show();
                              } catch (WindowManager.BadTokenException e) {
                                  showProgressBar(false);
                                  discountAmount ="0";
                                  isDiscountApplied=false;
                                  discount_rs_text_widget.setText("0");
                                  e.printStackTrace();
                              }
                          }
        });


    }

    private void GetStockBalanceForEachIteminCart() {

        for (int i = 0; i < cart_Item_List.size(); i++) {

            String itemUniqueCode = cart_Item_List.get(i);
            Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);
            String itemName = String.valueOf(modal_newOrderItems.getItemname());





        }







        }

    private void OpenRedeemDialogScreen(boolean showButtons) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Dialog dialog = new Dialog(getActivity());
                    dialog.setContentView(R.layout.redeem_pointswise_discount_layout);
                    showProgressBar(false);
                    isRedeemDialogboxOpened = true;


                    Currenttime = getDate_and_time();
                    //Log.d(TAG, "Currenttime: " + Currenttime);
                    TextView usermobileno_textwidget = (TextView) dialog.findViewById(R.id.usermobileno_textwidget);
                    TextView totalNoofPoints_textwidget = (TextView) dialog.findViewById(R.id.totalNoofPoints_textwidget);
                    TextView points_you_can_redeemToday_textwidget = (TextView) dialog.findViewById(R.id.points_you_can_redeemToday_textwidget);
                    EditText points_to_redeem_edittext = (EditText) dialog.findViewById(R.id.points_to_redeem_edittext);
                    Button applyRedeemPoints = (Button) dialog.findViewById(R.id.applyRedeemPoints);
                    TextView totalbillAmount_textwidget = (TextView) dialog.findViewById(R.id.totalbillAmount_textwidget);
                    TextView redeemedpoints_textwidget = (TextView) dialog.findViewById(R.id.redeemedpoints_textwidget);
                    TextView finalAmounttopay_textwidget = (TextView) dialog.findViewById(R.id.finalAmounttopay_textwidget);
                    TextView points_user_already_redeemed = (TextView) dialog.findViewById(R.id.points_user_already_redeemed);
                    LinearLayout billDetailsLayout = (LinearLayout) dialog.findViewById(R.id.billDetailsLayout);
                    LinearLayout enterpointsLayout = (LinearLayout) dialog.findViewById(R.id.enterpointsLayout);
                    Button proceedtoCheckoutWidget = (Button) dialog.findViewById(R.id.proceedtoCheckoutWidget);
                    TextView total_noof_points_allowedPerDay_textWidget = (TextView) dialog.findViewById(R.id.total_noof_points_allowedPerDay_textWidget);
                    if(showButtons){
                        proceedtoCheckoutWidget.setVisibility(View.VISIBLE);
                        applyRedeemPoints.setVisibility(View.VISIBLE);
                        billDetailsLayout.setVisibility(View.VISIBLE);
                        enterpointsLayout.setVisibility(View.VISIBLE);
                        dialog.setCanceledOnTouchOutside(false);

                    }
                    else{
                        proceedtoCheckoutWidget.setVisibility(View.GONE);
                        applyRedeemPoints.setVisibility(View.GONE);
                        billDetailsLayout.setVisibility(View.GONE);
                        enterpointsLayout.setVisibility(View.GONE);
                        dialog.setCanceledOnTouchOutside(true);
                    }
                    pointsalreadyredeemDouble = Double.parseDouble(totalpointsredeemedalreadybyuser);

                    double pointsredeemedtodayDouble =  Double.parseDouble(discountAmountalreadyusedtoday);
                    double pointsallowedtouseToday = maxpointsinaday_double-pointsredeemedtodayDouble;
                    usermobileno_textwidget.setText(mobileno_redeemKey);
                    totalNoofPoints_textwidget.setText(totalredeempointsuserhave);
                    totalbillAmount_textwidget.setText(String.valueOf(totalAmounttopay));
                    finalAmounttopay_textwidget.setText(String.valueOf(totalAmounttopay));
                    points_user_already_redeemed.setText(totalpointsredeemedalreadybyuser);
                    redeemedpoints_textwidget.setText("0  Points");

                    total_noof_points_allowedPerDay_textWidget.setText(String.format("( %s Points allowed / day ) ", maxpointsinaday_String));
                    points_you_can_redeemToday_textwidget.setText(String.valueOf(Math.round((pointsallowedtouseToday)))+" Points");
                    applyRedeemPoints.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            double totalpointsuseralreadyhave = Double.parseDouble(totalredeempointsuserhave);
                            pointsenteredToredeem = points_to_redeem_edittext.getText().toString();
                            if(pointsenteredToredeem.equals("")|| pointsenteredToredeem == null){
                                pointsenteredToredeem ="0";
                            }
                            pointsenteredToredeem_double =  Double.parseDouble(pointsenteredToredeem);
                            if (totalpointsuseralreadyhave >= pointsenteredToredeem_double) {
                                pointsalreadyredeemDouble = pointsalreadyredeemDouble + pointsenteredToredeem_double;
                                totalpointsuserhave_afterapplypoints =totalpointsuseralreadyhave-pointsenteredToredeem_double;

                                 finalamounttoPay = totalAmounttopay - pointsenteredToredeem_double;
                                if (pointsenteredToredeem_double <= pointsallowedtouseToday) {
                                    ispointsApplied_redeemClicked = true;
                                    redeemedpoints_textwidget.setText(new StringBuilder().append(String.valueOf(Math.round(pointsenteredToredeem_double))).append("  Points").toString());

                                    finalAmounttopay_textwidget.setText(String.valueOf(finalamounttoPay));
                                    //redeemPointsKey_fromRedeem = redeemKey;


                                } else {
                                    AlertDialogClass.showDialog(getActivity(), Constants.PointusercanRedeemtoday , 0);

                                }
                            }
                            else {
                                AlertDialogClass.showDialog(getActivity(), "User got  " + String.valueOf(totalredeempointsuserhave) + "  Points only", 0);
                            }
                        }
                    });


                    proceedtoCheckoutWidget.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(ispointsApplied_redeemClicked) {
                                isProceedtoCheckoutinRedeemdialogClicked = true;

                                finaltoPayAmountwithRedeemPoints = finalAmounttopay_textwidget.getText().toString();
                                redeemPoints_String =  pointsenteredToredeem ;
                                redeemed_points_text_widget.setText(redeemPoints_String);
                              //  totalpointsredeemedalreadybyuser=String.valueOf(pointsalreadyredeemDouble);
                                totalredeempointsuserhave = String.valueOf(totalpointsuserhave_afterapplypoints);
                                if(pointsenteredToredeem_double>0){
                                    discountAmountLayout.setVisibility(View.GONE);
                                    redeemPointsLayout.setVisibility(View.VISIBLE);
                                }
                                else{
                                    redeemPoints_String ="0";
                                    totalpointsredeemedalreadybyuser="0";
                                    totalredeempointsuserhave="0";
                                    pointsalreadyredeemDouble=0;
                                    isProceedtoCheckoutinRedeemdialogClicked=false;
                                    //discountlayout visible
                                    discountAmountLayout.setVisibility(View.GONE);
                                    redeemPointsLayout.setVisibility(View.GONE);
                                }
                                total_Rs_to_Pay_text_widget.setText(finaltoPayAmountwithRedeemPoints);
                                dialog.cancel();
                            }
                            else{
                                AlertDialogClass.showDialog(getActivity(), Constants.FirstApplyRedeemPoints_Instruction,0);

                            }
                        }
                    });


                    dialog.show();
                    showProgressBar(false);

                } catch (WindowManager.BadTokenException e) {
                    showProgressBar(false);

                    e.printStackTrace();
                }
            }
        });


    }

    private void addDatatoOrderTypeSpinner() {

        String[] ordertype=getResources().getStringArray(R.array.OrderType);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ordertype);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        orderTypeSpinner.setAdapter(arrayAdapter);



    }




    private void getWholeSaleCustomerArrayFromSharedPreferences() {
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

        if(wholeSaleCustomersArrayList.size()>0){
            Adapter_AutoCompleteWholeSaleCustomers adapter_autoCompleteWholeSaleCustomers = new Adapter_AutoCompleteWholeSaleCustomers(mContext,wholeSaleCustomersArrayList,NewOrders_MenuItem_Fragment.this);
            //adapter_autoCompleteWholeSaleCustomers.setHandler(newHandler());


            autoComplete_customerNameText_widget.setAdapter(adapter_autoCompleteWholeSaleCustomers);

        }

    }


    private void  PlaceOrdersinDatabaseaAndPrintRecipt(String paymentMode, long sTime, String currenttime, List<String> cart_Item_list) {
        showProgressBar(true);

        if (ispaymentMode_Clicked) {
            return;
        }
        else {
            usermobileNo = "+91" + mobileNo_Edit_widget.getText().toString();

            ispaymentMode_Clicked = true;
            String payableAmount = total_Rs_to_Pay_text_widget.getText().toString();

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
                                    autoComplete_customerNameText_widget.setText("");
                                    autoComplete_customerNameText_widget.dismissDropDown();
                                    StockBalanceChangedForThisItemList.clear();
                                    cart_Item_List.clear();
                                    cartItem_hashmap.clear();
                                    ispaymentMode_Clicked = false;
                                    isOrderDetailsMethodCalled = false;

                                    isPaymentDetailsMethodCalled = false;
                                    isOrderTrackingDetailsMethodCalled = false;
                                    new_to_pay_Amount = 0;
                                    old_taxes_and_charges_Amount = 0;
                                    old_total_Amount = 0;
                                    createEmptyRowInListView("empty");
                                    CallAdapter();
                                    discountAmount = "0";
                                    isDiscountApplied = false;
                                    discount_Edit_widget.setText("0");
                                    finaltoPayAmount = "0";
                                    discount_rs_text_widget.setText(discountAmount);
                                    OrderTypefromSpinner = "POS Order";
                                    orderTypeSpinner.setSelection(0);
                                    total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                    taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                    total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                    mobileNo_Edit_widget.setText("");
                                    isPrintedSecondTime = false;
                                    showProgressBar(false);

                                    ispointsApplied_redeemClicked=false;
                                    isProceedtoCheckoutinRedeemdialogClicked =false;
                                    isRedeemDialogboxOpened=false;
                                    isUpdateRedeemPointsMethodCalled=false;
                                    isUpdateCouponTransactionMethodCalled=false;
                                    isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                    totalAmounttopay=0;
                                    finalamounttoPay=0;

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
                                    //discountlayout visible
                                    discountAmountLayout.setVisibility(View.GONE);
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
            if(String.valueOf(paymentMode).toUpperCase().equals(Constants.CREDIT)){
                GetDatafromCreditOrderDetailsTable(paymentMode,sTime,currenttime);
            }
            else{
                if (!isOrderDetailsMethodCalled) {

                    PlaceOrder_in_OrderDetails(NewOrders_MenuItem_Fragment.cart_Item_List, paymentMode, sTime);
                }
                if (!isOrderTrackingDetailsMethodCalled) {

                    PlaceOrder_in_OrderTrackingDetails(sTime, currenttime);
                }
            }


            
            if(isProceedtoCheckoutinRedeemdialogClicked){
                if((!redeemPoints_String.equals(""))&&(!redeemPoints_String.equals("0"))){
                    String transactiontime = getDate_and_time();
                    double totalredeempointsusergetfromorderr=0;
                    double redeempointsuserapplied=Double.parseDouble(redeemPoints_String);
                    double finalamountwithredeempointsint = Double.parseDouble(finaltoPayAmountwithRedeemPoints);
                    double totalpointredeembyuserint =  Double.parseDouble(totalpointsredeemedalreadybyuser);
                    double totalordervalue_tillnowint =  Double.parseDouble(totalordervalue_tillnow);
                    double totalredeempointsuserhaveint =  Double.parseDouble(totalredeempointsuserhave);

                    double finalamountwithredeempointsdouble = Double.parseDouble(finaltoPayAmountwithRedeemPoints);
                    double pointsfor100rs_double = Double.parseDouble(pointsfor100rs_String);

                    try {
                        totalordervalue_tillnowint = totalordervalue_tillnowint + finalamountwithredeempointsint;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try {

                        totalordervalue_tillnow = String.valueOf(totalordervalue_tillnowint);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {

                        totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*finalamountwithredeempointsdouble)/100);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {

                        totalredeempointsusergetfromorderr =   Math.round((pointsfor100rs_double*finalamountwithredeempointsdouble)/100);


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {

                        totalredeempointsuserhaveint = totalredeempointsuserhaveint+totalredeempointsusergetfromorderr;


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {


                        totalredeempointsuserhave = String.valueOf(totalredeempointsuserhaveint);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {


                        totalpointredeembyuserint = totalpointredeembyuserint+redeempointsuserapplied;

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {


                        totalpointsredeemedalreadybyuser=String.valueOf(totalpointredeembyuserint);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {


                        updateRedeemPointsDetailsInDBWithkey(redeemKey,totalpointredeembyuserint,totalordervalue_tillnowint,totalredeempointsuserhaveint);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {


                        addDatatoCouponTransactioninDB(redeemPoints_String,"REDEEM",mobileno_redeemKey,String.valueOf(sTime),CurrentDate,transactiontime,vendorKey);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }





                }
            }
            else{

                try{
                    totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                }
                catch (Exception e){
                    e.printStackTrace();
                }

                String UserMobile = "+91" + mobileNo_Edit_widget.getText().toString();

              //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
             //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);


            }
        }


    }

    private void GetDatafromCreditOrderDetailsTable(String paymentMode, long sTime, String currenttime) {
        totalamountUserHaveAsCredit = 0;
        String mobileno = "+91" + mobileNo_Edit_widget.getText().toString();

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

                                            if (!isOrderDetailsMethodCalled) {

                                                PlaceOrder_in_OrderDetails(NewOrders_MenuItem_Fragment.cart_Item_List, paymentMode, sTime);
                                            }
                                            if (!isOrderTrackingDetailsMethodCalled) {

                                                PlaceOrder_in_OrderTrackingDetails(sTime, currenttime);
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

                                    if (!isOrderDetailsMethodCalled) {

                                        PlaceOrder_in_OrderDetails(NewOrders_MenuItem_Fragment.cart_Item_List, paymentMode, sTime);
                                    }
                                    if (!isOrderTrackingDetailsMethodCalled) {

                                        PlaceOrder_in_OrderTrackingDetails(sTime, currenttime);
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


    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: NewOrders_MenuItem_Fragment.cart_Item_List) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }
    private void printReciptUsingBluetoothPrinter(String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String finaltoPayAmountinmethod, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String discountAmount, String ordertype) {


        if (BluetoothPrintDriver.IsNoConnection()) {
            //  Toast.makeText(mContext,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

            new TMCAlertDialogClass(mContext, R.string.app_name, R.string.OrderPlaced_Printer_is_Disconnected,
                    R.string.OK_Text, R.string.Empty_Text,
                    new TMCAlertDialogClass.AlertListener() {
                        @Override
                        public void onYes() {
                            //ConnectPrinter();
                            turnoffProgressBarAndResetArray();
                            return;

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

                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.OrderPlaced_Printer_is_Disconnected,
                        R.string.OK_Text, R.string.Empty_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                               // ConnectPrinter();
                                turnoffProgressBarAndResetArray();
                                return;
                            }

                            @Override
                            public void onNo() {

                            }
                        });
            }
        }


        showProgressBar(true);

        try {
            new Thread(new Runnable() {
                public void run() {


                    double oldSavedAmount = 0;
                    String CouponDiscount = "0";


                    String Title = "The Meat Chop";

                    String GSTIN = "GSTIN :33AAJCC0055D1Z9";
                    String CurrentTime = getDate_and_time();


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


                            savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                            oldSavedAmount = savedAmount + oldSavedAmount;
                            String Gst = "Rs."+modal_newOrderItems.getGstAmount();
                            String subtotal ="Rs."+ modal_newOrderItems.getSubTotal_perItem();
                            String quantity = modal_newOrderItems.getQuantity();
                            String itemrate = "Rs."+modal_newOrderItems.getItemFinalPrice();
                            String weight = modal_newOrderItems.getItemFinalWeight();
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
                            BluetoothPrintDriver.printString(itemDespName_Weight_quantity);
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();



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
                                subtotal = "        " + subtotal;
                            }
                            if (subtotal.length() == 5) {
                                //6spaces
                                subtotal = "        " + subtotal;
                            }
                            if (subtotal.length() == 6) {
                                //8spaces
                                subtotal = "          " + subtotal;
                            }
                            if (subtotal.length() == 7) {
                                //7spaces
                                subtotal = "         " + subtotal;
                            }
                            if (subtotal.length() == 8) {
                                //6spaces
                                subtotal = "        " + subtotal;
                            }
                            if (subtotal.length() == 9) {
                                //5spaces
                                subtotal = "       " + subtotal;
                            }
                            if (subtotal.length() == 10) {
                                //4spaces
                                subtotal = "      " + subtotal;
                            }
                            if (subtotal.length() == 11) {
                                //3spaces
                                subtotal = "     " + subtotal;
                            }
                            if (subtotal.length() == 12) {
                                //2spaces
                                subtotal = "    " + subtotal;
                            }
                            if (subtotal.length() == 13) {
                                //1spaces
                                subtotal = "   " + subtotal;
                            }
                            if (subtotal.length() == 14) {
                                //no space
                                subtotal = "  " + subtotal;
                            }

                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString(itemrate+ Gst + subtotal);
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
                        String totalSubtotal = "Rs." + finaltoPayAmount;
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
                        BluetoothPrintDriver.printString(totalRate+ totalGst + totalSubtotal);
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
                                    CouponDiscount = " Discount Amount                      " + CouponDiscount;
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
                                BluetoothPrintDriver.printString(CouponDiscount);
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
                            BluetoothPrintDriver.printString("Old Amount need to be Paid : " +String.valueOf(Math.round(totalamountUserHaveAsCredit)) + " \n");
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


                            BluetoothPrintDriver.Begin();
                            BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                            BluetoothPrintDriver.SetAlignMode((byte) 0);
                            BluetoothPrintDriver.SetLineSpacing((byte) 85);
                            BluetoothPrintDriver.printString("total Amount need to be Paid = (Old amount + Current Bill Amount ) \n");
                            BluetoothPrintDriver.BT_Write("\r");
                            BluetoothPrintDriver.LF();


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
                            BluetoothPrintDriver.printString("total Amount need to be Paid : " +  String.valueOf(Math.round(totalamountUserHaveAsCredit+payableamountdoublePrint))+ " \n");
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
                        BluetoothPrintDriver.printString("ordertype : "+ordertype);
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
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString("User Mobile : " +userMobile);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.BT_Write("\n");

                        BluetoothPrintDriver.LF();






                        BluetoothPrintDriver.Begin();
                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetAlignMode((byte) 49);
                        BluetoothPrintDriver.SetLineSpacing((byte) 85);
                        BluetoothPrintDriver.printString("Thank you for choosing us !!!");
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();


                        BluetoothPrintDriver.FeedAndCutPaper((byte)66,(byte)50);


                        if (!isPrintedSecondTime) {

                            turnoffProgressBar(orderplacedTime,userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_item_list, cartItem_hashmap, payment_mode,discountAmount,ordertype);
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



    private void turnoffProgressBar(String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String finaltoPayAmountinmethod, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String discountAmount, String ordertype) {
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

                                printReciptUsingBluetoothPrinter(orderplacedTime, userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_Item_List, cartItem_hashmap, payment_mode, discountAmount, ordertype);

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

                StockBalanceChangedForThisItemList.clear();
                isStockOutGoingAlreadyCalledForthisItem =false;

                autoComplete_customerNameText_widget.setText("");
                autoComplete_customerNameText_widget.dismissDropDown();
                cart_Item_List.clear();

                cartItem_hashmap.clear();
                ispaymentMode_Clicked = false;
                isOrderDetailsMethodCalled = false;

                isPaymentDetailsMethodCalled = false;
                isOrderTrackingDetailsMethodCalled = false;
                new_to_pay_Amount = 0;
                old_taxes_and_charges_Amount = 0;
                old_total_Amount = 0;
                createEmptyRowInListView("empty");
                CallAdapter();
                discountAmount = "0";
                isDiscountApplied = false;
                discount_Edit_widget.setText("0");
                finaltoPayAmount = "0";
                discount_rs_text_widget.setText(discountAmount);
                OrderTypefromSpinner = "POS Order";
                orderTypeSpinner.setSelection(0);
                total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                mobileNo_Edit_widget.setText("");
                isPrintedSecondTime = false;
                showProgressBar(false);
                useStoreNumberCheckBox.setChecked(false);
                ispointsApplied_redeemClicked=false;
                isProceedtoCheckoutinRedeemdialogClicked =false;
                isRedeemDialogboxOpened=false;
                isUpdateRedeemPointsMethodCalled=false;
                isUpdateCouponTransactionMethodCalled=false;
                isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                totalAmounttopay=0;
                finalamounttoPay=0;
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
                //discountlayout visible
                discountAmountLayout.setVisibility(View.GONE);

                return;

            }
        });



    }





    private  void printRecipt(String userMobile, String tokenno, String itemTotalwithoutGst, String totaltaxAmount, String payableAmount, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cart_Item_hashmap, String payment_mode, String discountAmountt, String ordertype) {


                try {
                    Printer_POJO_Class[] Printer_POJO_ClassArray = new Printer_POJO_Class[cart_Item_List.size()];
                    double oldSavedAmount = 0;
                    String CouponDiscount = "0";
                    String Gstt="",subtotall="",quantity="",price="",weight="";
                    double gst_double=0,subtotal_double=0,price_double=0;
                    for (int i = 0; i < cart_item_list.size(); i++) {
                        double savedAmount;
                        String itemUniqueCode = cart_item_list.get(i);
                        Modal_NewOrderItems modal_newOrderItems = cart_Item_hashmap.get(itemUniqueCode);
                        String itemName = String.valueOf(modal_newOrderItems.getItemname());
                        int indexofbraces = itemName.indexOf("(");
                        if (indexofbraces >= 0) {
                            itemName = itemName.substring(0, indexofbraces);

                        }
                        if (itemName.length() > 21) {
                            itemName = itemName.substring(0, 21);
                            itemName = itemName + "...";
                        }
                        try{
                            price = String.valueOf( modal_newOrderItems.getItemFinalPrice());
                            if(price.equals("null")){
                                price  = "  ";
                            }
                        }
                        catch(Exception e){
                            price  = "0";
                            e.printStackTrace();
                        }

                        try{
                            weight =  modal_newOrderItems.getItemFinalWeight().toString();
                        }
                        catch(Exception e){
                            weight = "0";
                            e.printStackTrace();
                        }

                        try{
                            Gstt = modal_newOrderItems.getGstAmount();

                        }
                        catch(Exception e){
                            Gstt  = "0";
                            e.printStackTrace();
                        }





                        try{
                            savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                        }
                        catch(Exception e){
                            savedAmount = 0;
                            e.printStackTrace();
                        }


                        try{
                            oldSavedAmount = savedAmount + oldSavedAmount;
                        }
                        catch(Exception e){
                            weight = "0";
                            e.printStackTrace();
                        }



                        try{
                            subtotall = modal_newOrderItems.getSubTotal_perItem();
                        }
                        catch(Exception e){
                            subtotall = "0";
                            e.printStackTrace();
                        }

                        try{
                            quantity = modal_newOrderItems.getQuantity();
                        }
                        catch(Exception e){
                            quantity = "0";
                            e.printStackTrace();
                        }



                        Printer_POJO_ClassArray[i] = new Printer_POJO_Class("", quantity, orderid, itemName, weight, price, "0.00", Gstt, subtotall, "cutname");

                    }

                    Printer_POJO_Class Printer_POJO_ClassArraytotal = new Printer_POJO_Class(itemTotalwithoutGst, discountAmountt, totaltaxAmount, payableAmount, oldSavedAmount);
                    PrinterFunctions.PortDiscovery(portName, portSettings);
                    PrinterFunctions.OpenCashDrawer(portName, portSettings, 0, 4);

                    // PrinterFunctions.OpenPort( portName, portSettings);
                    //    PrinterFunctions.CheckStatus( portName, portSettings,2);
                    PrinterFunctions.SelectPrintMode(portName, portSettings, 0);

                    if((vendorKey.equals("vendor_4")) ||  (vendorKey.equals("wholesalesvendor_1"))) {


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "MK Proteins" + "\n");

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Powered by the The Meat Chop" + "\n");

                    }
                    else {

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 180);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 2, 1, 0, 1, "The Meat Chop" + "\n");

                    }


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "Fresh Meat and SeaFood" + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine1 + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine2 + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreAddressLine3 + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, StoreLanLine + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "GSTIN :33AAJCC0055D1Z9" + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, Currenttime + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 1, "# " + orderid + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 40);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "ITEM NAME * QTY" + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 70);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "RATE                GST         SUBTOTAL" + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
                    for (int i = 0; i < Printer_POJO_ClassArray.length; i++) {

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        String itemrate, Gst, subtotal;
                        itemrate = "Rs." + Printer_POJO_ClassArray[i].getItemRate();
                        Gst = "Rs." + Printer_POJO_ClassArray[i].getGST();
                        subtotal = "Rs." + Printer_POJO_ClassArray[i].getSubTotal();
                        if (itemrate.length() == 4) {
                            //14spaces
                            itemrate = itemrate + "              ";
                        }
                        if (itemrate.length() == 5) {
                            //13spaces
                            itemrate = itemrate + "             ";
                        }
                        if (itemrate.length() == 6) {
                            //12spaces
                            itemrate = itemrate + "            ";
                        }
                        if (itemrate.length() == 7) {
                            //11spaces
                            itemrate = itemrate + "           ";
                        }
                        if (itemrate.length() == 8) {
                            //10spaces
                            itemrate = itemrate + "          ";
                        }
                        if (itemrate.length() == 9) {
                            //9spaces
                            itemrate = itemrate + "         ";
                        }
                        if (itemrate.length() == 10) {
                            //8spaces
                            itemrate = itemrate + "        ";
                        }
                        if (itemrate.length() == 11) {
                            //7spaces
                            itemrate = itemrate + "       ";
                        }
                        if (itemrate.length() == 12) {
                            //6spaces
                            itemrate = itemrate + "      ";
                        }
                        if (itemrate.length() == 13) {
                            //5spaces
                            itemrate = itemrate + "     ";
                        }
                        if (itemrate.length() == 14) {
                            //4spaces
                            itemrate = itemrate + "    ";
                        }
                        if (itemrate.length() == 15) {
                            //3spaces
                            itemrate = itemrate + "   ";
                        }
                        if (itemrate.length() == 16) {
                            //2spaces
                            itemrate = itemrate + "  ";
                        }
                        if (itemrate.length() == 17) {
                            //1spaces
                            itemrate = itemrate + " ";
                        }
                        if (itemrate.length() == 18) {
                            //1spaces
                            itemrate = itemrate + "";
                        }


                        if (Gst.length() == 7) {
                            //1spaces
                            Gst = Gst + " ";
                        }
                        if (Gst.length() == 8) {
                            //0space
                            Gst = Gst + "";
                        }
                        if (Gst.length() == 9) {
                            //no space
                            Gst = Gst;
                        }
                        if (subtotal.length() == 4) {
                            //5spaces
                            subtotal = "      " + subtotal;
                        }
                        if (subtotal.length() == 5) {
                            //6spaces
                            subtotal = "      " + subtotal;
                        }
                        if (subtotal.length() == 6) {
                            //8spaces
                            subtotal = "        " + subtotal;
                        }
                        if (subtotal.length() == 7) {
                            //7spaces
                            subtotal = "       " + subtotal;
                        }
                        if (subtotal.length() == 8) {
                            //6spaces
                            subtotal = "      " + subtotal;
                        }
                        if (subtotal.length() == 9) {
                            //5spaces
                            subtotal = "     " + subtotal;
                        }
                        if (subtotal.length() == 10) {
                            //4spaces
                            subtotal = "    " + subtotal;
                        }
                        if (subtotal.length() == 11) {
                            //3spaces
                            subtotal = "   " + subtotal;
                        }
                        if (subtotal.length() == 12) {
                            //2spaces
                            subtotal = "  " + subtotal;
                        }
                        if (subtotal.length() == 13) {
                            //1spaces
                            subtotal = " " + subtotal;
                        }
                        if (subtotal.length() == 14) {
                            //no space
                            subtotal = "" + subtotal;
                        }


                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, Printer_POJO_ClassArray[i].getItemName() + "  *  " + Printer_POJO_ClassArray[i].getItemWeight() + "(" + Printer_POJO_ClassArray[i].getQuantity() + ")" + "\n");

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 80);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, itemrate + Gst + subtotal + "\n\n");
                    }

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                    String totalRate = "Rs." + Printer_POJO_ClassArraytotal.getTotalRate();
                    String totalGst = "Rs." + Printer_POJO_ClassArraytotal.getTotalGST();
                    String totalSubtotal = "Rs." + finaltoPayAmount;
                    if (totalRate.length() == 7) {
                        //10spaces
                        totalRate = totalRate + "          ";
                    }
                    if (totalRate.length() == 8) {
                        //9spaces
                        totalRate = totalRate + "         ";
                    }
                    if (totalRate.length() == 9) {
                        //8spaces
                        totalRate = totalRate + "        ";
                    }
                    if (totalRate.length() == 10) {
                        //7spaces
                        totalRate = totalRate + "       ";
                    }
                    if (totalRate.length() == 11) {
                        //6spaces
                        totalRate = totalRate + "      ";
                    }
                    if (totalRate.length() == 12) {
                        //5spaces
                        totalRate = totalRate + "     ";
                    }
                    if (totalRate.length() == 13) {
                        //4spaces
                        totalRate = totalRate + "    ";
                    }

                    if (totalGst.length() == 7) {
                        //1spaces
                        totalGst = totalGst + " ";
                    }
                    if (totalGst.length() == 8) {
                        //0space
                        totalGst = totalGst + "";
                    }
                    if (totalGst.length() == 9) {
                        //no space
                        totalGst = totalGst;
                    }

                    if (totalSubtotal.length() == 6) {
                        //8spaces
                        totalSubtotal = "        " + totalSubtotal;
                    }
                    if (totalSubtotal.length() == 7) {
                        //7spaces
                        totalSubtotal = "       " + totalSubtotal;
                    }
                    if (totalSubtotal.length() == 8) {
                        //6spaces
                        totalSubtotal = "      " + totalSubtotal;
                    }
                    if (totalSubtotal.length() == 9) {
                        //5spaces
                        totalSubtotal = "     " + totalSubtotal;
                    }
                    if (totalSubtotal.length() == 10) {
                        //4spaces
                        totalSubtotal = "    " + totalSubtotal;
                    }


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, totalRate + totalGst + totalSubtotal + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");





     /*

        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
        String SavedAmount = "You just saved Rs."+" on these items"+String.valueOf(Printer_POJO_ClassArraytotal.getOldSavedAmount());

        PrinterFunctions. PrintText(portName,portSettings,0, 0,1,0,0, 0,30,1,SavedAmount+"\n");


 */
                    CouponDiscount = "0";

                    CouponDiscount = Printer_POJO_ClassArraytotal.getTotaldiscount();

                    if (!CouponDiscount.equals("0")) {
                        CouponDiscount = "Rs. " + CouponDiscount + ".00";

                        if ((!CouponDiscount.equals("Rs.0.0")) && (!CouponDiscount.equals("Rs.0")) && (!CouponDiscount.equals("Rs.0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals("")) && (!CouponDiscount.equals("Rs. .00")) && (!CouponDiscount.equals("Rs..00"))) {

                            if (CouponDiscount.length() == 4) {
                                //20spaces
                                //NEW TOTAL =4
                                CouponDiscount = "Discount Amount                   " + CouponDiscount;
                            }
                            if (CouponDiscount.length() == 5) {
                                //21spaces
                                //NEW TOTAL =5
                                CouponDiscount = "Discount Amount                 " + CouponDiscount;
                            }
                            if (CouponDiscount.length() == 6) {
                                //20spaces
                                //NEW TOTAL =6
                                CouponDiscount = "Discount Amount                " + CouponDiscount;
                            }

                            if (CouponDiscount.length() == 7) {
                                //19spaces
                                //NEW TOTAL =7
                                CouponDiscount = "Discount Amount               " + CouponDiscount;
                            }
                            if (CouponDiscount.length() == 8) {
                                //18spaces
                                //NEW TOTAL =8
                                CouponDiscount = " Discount Amount              " + CouponDiscount;
                            }
                            if (CouponDiscount.length() == 9) {
                                //17spaces
                                //NEW TOTAL =9
                                CouponDiscount = " Discount Amount             " + CouponDiscount;
                            }
                            if (CouponDiscount.length() == 10) {
                                //16spaces
                                //NEW TOTAL =9
                                CouponDiscount = " Discount Amount            " + CouponDiscount;
                            }
                            if (CouponDiscount.length() == 11) {
                                //15spaces
                                //NEW TOTAL =9
                                CouponDiscount = "Discount Amount            " + CouponDiscount;
                            }
                            if (CouponDiscount.length() == 12) {
                                //14spaces
                                //NEW TOTAL =9
                                CouponDiscount = "Discount Amount           " + CouponDiscount;
                            }

                            if (CouponDiscount.length() == 13) {
                                //13spaces
                                //NEW TOTAL =9
                                CouponDiscount = "Discount Amount           " + CouponDiscount;

                            }


                            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, CouponDiscount + "\n");


                            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                        }
                    }




                    String redeemPoints_String_print="";
                    if (!redeemPoints_String.equals("0")) {
                        redeemPoints_String_print = "Rs. " + redeemPoints_String + ".00";

                        if ((!redeemPoints_String_print.equals("Rs.0.0")) && (!redeemPoints_String_print.equals("Rs.0")) && (!redeemPoints_String_print.equals("Rs.0.00")) && (redeemPoints_String_print != (null)) && (!redeemPoints_String_print.equals("")) && (!redeemPoints_String_print.equals("Rs. .00")) && (!redeemPoints_String_print.equals("Rs..00"))) {

                            if (redeemPoints_String_print.length() == 4) {
                                //20spaces
                                //NEW TOTAL =4
                                redeemPoints_String_print = "Points Redeemed                    " + redeemPoints_String_print;
                            }
                            if (redeemPoints_String_print.length() == 5) {
                                //21spaces
                                //NEW TOTAL =5
                                redeemPoints_String_print = "Points Redeemed                  " + redeemPoints_String_print;
                            }
                            if (redeemPoints_String_print.length() == 6) {
                                //20spaces
                                //NEW TOTAL =6
                                redeemPoints_String_print = "Points Redeemed                 " + redeemPoints_String_print;
                            }

                            if (redeemPoints_String_print.length() == 7) {
                                //19spaces
                                //NEW TOTAL =7
                                redeemPoints_String_print = "Points Redeemed                " + redeemPoints_String_print;
                            }
                            if (redeemPoints_String_print.length() == 8) {
                                //18spaces
                                //NEW TOTAL =8
                                redeemPoints_String_print = "Points Redeemed               " + redeemPoints_String_print;
                            }
                            if (redeemPoints_String_print.length() == 9) {
                                //17spaces
                                //NEW TOTAL =9
                                redeemPoints_String_print = "Points Redeemed               " + redeemPoints_String_print;
                            }
                            if (redeemPoints_String_print.length() == 10) {
                                //16spaces
                                //NEW TOTAL =9
                                redeemPoints_String_print = "Points Redeemed             " + redeemPoints_String_print;
                            }
                            if (redeemPoints_String_print.length() == 11) {
                                //15spaces
                                //NEW TOTAL =9
                                redeemPoints_String_print =" Points Redeemed             " + redeemPoints_String_print;
                            }
                            if (redeemPoints_String_print.length() == 12) {
                                //14spaces
                                //NEW TOTAL =9
                                redeemPoints_String_print = "Points Redeemed            " + redeemPoints_String_print;
                            }

                            if (redeemPoints_String_print.length() == 13) {
                                //13spaces
                                //NEW TOTAL =9
                                redeemPoints_String_print = "Points Redeemed            " + redeemPoints_String_print;

                            }


                            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, redeemPoints_String_print + "\n");


                            PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                        }}

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    String NetTotal = Printer_POJO_ClassArraytotal.getTotalsubtotal();
          /*  try {
                if (!NetTotal.contains(".")) {
                    int netTotaLint = Integer.parseInt(NetTotal);
                    int netdiscountAmountint = Integer.parseInt(discountAmountt);
                    netTotaL = netTotaLint - netdiscountAmountint;

                } else {

                    double nettotalDouble = Double.parseDouble(NetTotal);
                    double discountAmountDouble = Double.parseDouble(discountAmountt);
                    //netTotaL = (Integer.parseInt(String.valueOf(nettotalDouble))) - (Integer.parseInt(String.valueOf(discountAmountDouble)));
                    netTotaL = (int) Math.round(nettotalDouble-discountAmountDouble);

                }
                NetTotal = String.valueOf(netTotaL);

            } catch (Exception e) {
                e.printStackTrace();
                NetTotal = Printer_POJO_ClassArraytotal.getTotalsubtotal();

            }

           */

                    if (NetTotal.length() > 6) {

                        if (NetTotal.length() == 7) {
                            //24spaces
                            //NEW TOTAL =9
                            NetTotal = " NET TOTAL                       Rs. " + NetTotal;
                        }
                        if (NetTotal.length() == 8) {
                            //23spaces
                            //NEW TOTAL =9
                            NetTotal = "  NET TOTAL                       Rs. " + NetTotal;
                        }
                        if (NetTotal.length() == 9) {
                            //22spaces
                            //NEW TOTAL =9
                            NetTotal = "  NET TOTAL                      Rs. " + NetTotal;
                        }
                        if (NetTotal.length() == 10) {
                            //21spaces
                            //NEW TOTAL =9
                            NetTotal = "  NET TOTAL                    Rs. " + NetTotal;
                        }
                        if (NetTotal.length() == 11) {
                            //20spaces
                            //NEW TOTAL =9
                            NetTotal = "  NET TOTAL                   Rs. " + NetTotal;
                        }
                        if (NetTotal.length() == 12) {
                            //19spaces
                            //NEW TOTAL =9
                            NetTotal = "  NET TOTAL                  Rs. " + NetTotal;
                        }
                    } else {
                        NetTotal = " NET TOTAL                    Rs.  " + NetTotal;

                    }

                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, NetTotal + "\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
                    try {
                        if (payment_mode.toUpperCase().equals(Constants.CASH_ON_DELIVERY)) {
                            if((!amountRecieved_String.equals("null"))&&(!balanceAmount_String.equals("null"))) {
                                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Amount Given by Customer : ");


                                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, amountRecieved_String + " Rs " + "\n");

                                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");


                                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Balance Amount given : ");


                                PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, balanceAmount_String + " Rs" + "\n");

                                PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                                PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                                PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
                            }
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Earned Rewards : " +  String.valueOf((int)(totalredeempointsusergetfromorder))+"\n");

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");
                    if(payment_mode.toString().toUpperCase().equals(Constants.CREDIT)){
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Old Amount need to be Paid : " +  String.valueOf(Math.round(totalamountUserHaveAsCredit))+"\n");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 1, 30, 0, " total Amount need to be Paid = (Old amount + Current Bill Amount ) \n");




                        String payableamountPrint = "";
                        try{
                            payableamountPrint = Printer_POJO_ClassArraytotal.getTotalsubtotal();
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



                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "total Amount need to be Paid : " +  String.valueOf(Math.round(totalamountUserHaveAsCredit+payableamountdoublePrint))+"\n");

                        PrinterFunctions.SetLineSpacing(portName, portSettings, 50);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "----------------------------------------" + "\n");

                    }
                    if(!ordertype.equals(Constants.POSORDER)) {
                        PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Order Type: ");


                        PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                        PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                        PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, ordertype + "\n");
                    }

                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "Payment Mode: ");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 90);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 30, 0, payment_mode + "\n");


                    PrinterFunctions.SetLineSpacing(portName, portSettings, 60);
                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 30, 0, "MobileNo : ");


                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 0, userMobile + "           " + "\n");


/*
            PrinterFunctions.SetLineSpacing(portName, portSettings, 120);
            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 30, "ID : ");


            PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
            PrinterFunctions.PrintText(portName, portSettings, 0, 0, 0, 0, 0, 0, 0, 50, tokenno + "\n");


 */


                    PrinterFunctions.SelectCharacterFont(portName, portSettings, 0);
                    PrinterFunctions.PrintText(portName, portSettings, 0, 0, 1, 0, 0, 0, 0, 1, "\n" + "Thank you for choosing us !!!  " + "\n");


                    PrinterFunctions.PreformCut(portName, portSettings, 1);
                    //  PrinterFunctions.PrintSampleReceipt(portName,portSettings);
                    //Log.i("tag", "printer Log    " + PrinterFunctions.PortDiscovery(portName, portSettings));

                    //Log.i("tag", "printer Log    " + PrinterFunctions.OpenPort(portName, portSettings));

                    //Log.i("tag", "printer Log    " + PrinterFunctions.CheckStatus(portName, portSettings, 2));
                    if (!isPrintedSecondTime) {
                        showProgressBar(false);
                        //isPrintedSecondTime = true;
                        //showProgressBar(true);


                        //       openPrintAgainDialog(userMobile, tokenno, itemTotalwithoutGst, totaltaxAmount, payableAmount, orderid, cart_item_list, cart_Item_hashmap, payment_mode);

                        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.RePrint_Instruction,
                                R.string.Yes_Text, R.string.No_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        isPrintedSecondTime = true;

                                        printRecipt(userMobile, tokenno, itemTotalwithoutGst, totaltaxAmount, payableAmount, orderid, cart_item_list, cart_Item_hashmap, payment_mode, discountAmount, ordertype);

                                    }

                                    @Override
                                    public void onNo() {
                                        StockBalanceChangedForThisItemList.clear();
                                        isStockOutGoingAlreadyCalledForthisItem =false;

                                        autoComplete_customerNameText_widget.setText("");
                                        autoComplete_customerNameText_widget.dismissDropDown();
                                        cart_Item_List.clear();
                                        cart_Item_hashmap.clear();
                                        cart_item_list.clear();
                                        cartItem_hashmap.clear();
                                        ispaymentMode_Clicked = false;
                                        isOrderDetailsMethodCalled = false;

                                        isPaymentDetailsMethodCalled = false;
                                        isOrderTrackingDetailsMethodCalled = false;
                                        new_to_pay_Amount = 0;
                                        old_taxes_and_charges_Amount = 0;
                                        old_total_Amount = 0;
                                        createEmptyRowInListView("empty");
                                        CallAdapter();
                                        discountAmount = "0";
                                        isDiscountApplied = false;
                                        discount_Edit_widget.setText("0");
                                        finaltoPayAmount = "0";
                                        discount_rs_text_widget.setText(discountAmount);
                                        OrderTypefromSpinner = "POS Order";
                                        orderTypeSpinner.setSelection(0);
                                        total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                        taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                        mobileNo_Edit_widget.setText("");
                                        isPrintedSecondTime = false;
                                        showProgressBar(false);
                                        useStoreNumberCheckBox.setChecked(false);
                                        ispointsApplied_redeemClicked=false;
                                        isProceedtoCheckoutinRedeemdialogClicked =false;
                                        isRedeemDialogboxOpened=false;
                                        isUpdateRedeemPointsMethodCalled=false;
                                        isUpdateCouponTransactionMethodCalled=false;
                                        isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                        totalAmounttopay=0;
                                        finalamounttoPay=0;
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
                                        //discountlayout visible
                                        discountAmountLayout.setVisibility(View.GONE);

                                    }
                                });


                    } else {
                        autoComplete_customerNameText_widget.setText("");
                        autoComplete_customerNameText_widget.dismissDropDown();
                        cart_Item_List.clear();
                        StockBalanceChangedForThisItemList.clear();
                        StockBalanceChangedForThisItemList.clear();
                        isStockOutGoingAlreadyCalledForthisItem =false;

                        cart_Item_hashmap.clear();
                        cart_item_list.clear();
                        cartItem_hashmap.clear();

                        new_to_pay_Amount = 0;
                        old_taxes_and_charges_Amount = 0;
                        old_total_Amount = 0;
                        createEmptyRowInListView("empty");
                        CallAdapter();
                        discountAmount = "0";
                        isDiscountApplied = false;
                        discount_Edit_widget.setText("0");
                        finaltoPayAmount = "0";
                        discount_rs_text_widget.setText(discountAmount);
                        OrderTypefromSpinner = "POS Order";
                        orderTypeSpinner.setSelection(0);
                        total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                        taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));
                        useStoreNumberCheckBox.setChecked(false);




                        mobileNo_Edit_widget.setText("");
                        isPrintedSecondTime = false;
                        ispaymentMode_Clicked = false;
                        isOrderDetailsMethodCalled = false;

                        isPaymentDetailsMethodCalled = false;
                        isOrderTrackingDetailsMethodCalled = false;
                        ispointsApplied_redeemClicked=false;
                        isProceedtoCheckoutinRedeemdialogClicked =false;
                        isRedeemDialogboxOpened=false;
                        isUpdateRedeemPointsMethodCalled=false;
                        isUpdateCouponTransactionMethodCalled=false;
                        isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                        totalAmounttopay=0;
                        finalamounttoPay=0;

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
                        //discountlayout visible
                        discountAmountLayout.setVisibility(View.GONE);
                        showProgressBar(false);

                    }



        /*
         autoComplete_customerNameText_widget.setText("");
         autoComplete_customerNameText_widget.dismissDropDown();
            cart_Item_List.clear();
            cart_Item_hashmap.clear();
            cart_item_list.clear();
            cartItem_hashmap.clear();
            ispaymentMode_Clicked = false;
            isOrderDetailsMethodCalled = false;

            isPaymentDetailsMethodCalled = false;
            isOrderTrackingDetailsMethodCalled = false;
            new_to_pay_Amount = 0;
            old_taxes_and_charges_Amount = 0;
            old_total_Amount = 0;
            createEmptyRowInListView("empty");
            CallAdapter();
            discountAmount = "0";
            CouponDiscount = "0";
            isDiscountApplied = false;
            discount_Edit_widget.setText("0");
            finaltoPayAmount = "0";
            discount_rs_text_widget.setText(discountAmount);

            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

            mobileNo_Edit_widget.setText("");
            isPrintedSecondTime = false;
            showProgressBar(false);




         */

                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(mContext,String.valueOf(e),Toast.LENGTH_LONG).show();
                }





    }

    private void updateRedeemPointsDetailsInDBWithkey(String redeemPointsKey, double totalpointsredeemedbyuser, double totalordervalue, double totalredeempoints) {
        if(isUpdateRedeemPointsMethodCalled){
            return;
        }
        isUpdateRedeemPointsMethodCalled =true;



        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", redeemPointsKey);

            jsonObject.put("pointsredeemed", (int) Math.round(totalpointsredeemedbyuser));
            jsonObject.put("totalordervalue",  (int) Math.round( totalordervalue));
            jsonObject.put("totalredeempoints",  (int) Math.round( totalredeempoints));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateRedeemPointsTablewithkey,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        Log.d(Constants.TAG, "Points Redeem details uploaded Succesfully " + response);
                    }
                    else{
                        Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + response);

                    }
                } catch (JSONException e) {
                    Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + response);
                    isUpdateRedeemPointsMethodCalled=false;
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + error.toString());
                isUpdateRedeemPointsMethodCalled=false;
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
                        Log.d(Constants.TAG, "Points Redeem details uploaded Succesfully " + response);
                    }
                    else{
                        Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + response);

                    }
                } catch (JSONException e) {
                    Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + response);
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
                Log.d(Constants.TAG, "Failed during uploading Points Redeem  details" + error.toString());
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

            jsonObject.put("vendordiscountamount", Double.parseDouble(coupondiscountamount));

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
                        Log.d(Constants.TAG, "Response for Coupon details: " + response);

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

    public void cancelRedeemPointsFromOrder() {

        ispointsApplied_redeemClicked=false;
        isProceedtoCheckoutinRedeemdialogClicked=false;
        AlertDialogClass.showDialog(getActivity(), Constants.AddedRedeemPointsCancelledInstruction , 0);
        finaltoPayAmount = String.valueOf(totalAmounttopay);

        total_Rs_to_Pay_text_widget.setText( String.valueOf(totalAmounttopay));
        redeemPoints_String =  "0" ;
        redeemed_points_text_widget.setText(redeemPoints_String);

            //discountlayout visible
                                    discountAmountLayout.setVisibility(View.GONE);
            redeemPointsLayout.setVisibility(View.GONE);


    }


    private void openPrintAgainDialog(String userMobile, String tokenno, String itemTotalwithoutGst, String totaltaxAmount, String payableAmount, String orderid, List<String> cart_Item_List, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode) {



    runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.print_again);
                        dialog.setTitle("Do you Want to Print Again !!!! ");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(false);

                        Button printAgain = (Button) dialog.findViewById(R.id.printAgain);


                        printAgain.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                isPrintedSecondTime = true;
                                showProgressBar(true);


                                printRecipt(userMobile, tokenno, itemTotalwithoutGst, totaltaxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, payment_mode, discountAmount, ordertype);

                                dialog.cancel();
                            }
                        });


                        dialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        }



    void CallAdapter() {
        //Log.e(TAG, "AdapterCalled  ");


      //  adapter_cartItem_listview= new Adapter_CartItem_Listview(mContext,cartItem_hashmap, MenuItems,NewOrders_MenuItem_Fragment.this);
      //  listview.setAdapter(adapter_cartItem_listview);
        adapter_cartItem_recyclerview = new Adapter_CartItem_Recyclerview(mContext,cartItem_hashmap, MenuItems,NewOrders_MenuItem_Fragment.this,completemenuItem);
        adapter_cartItem_recyclerview.setHandler(newHandler());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        int last_index=NewOrders_MenuItem_Fragment.cartItem_hashmap.size()-1;

        recyclerView.setAdapter(adapter_cartItem_recyclerview);
        recyclerView.scrollToPosition(last_index);



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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.neworders_menu_item_fragment, container, false);
        rootView.setTag("RecyclerViewFragment");
        //Log.d(TAG, "onCreateView: ");
        listview = rootView.findViewById(R.id.listview);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        try{

     //       getMenuItemStockAvlDetailsArrayAndMenuItemFromSharedPreferences();
            MenuItems=getData();

            //Log.i(TAG, "call adapter cart_Item " + getData());

            completemenuItem= getMenuItemfromString(MenuItems);
        }

        catch (Exception e){
            e.printStackTrace();
        }


        createEmptyRowInListView("empty");

        CallAdapter();

        return rootView;


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
                            newOrdersPojoClass.key= String.valueOf(json.get("key"));

                        }
                        else{
                            newOrdersPojoClass.key = "Key for this menu is missing";
                            Toast.makeText(mContext,"TMC menuItemId Json is Missing",Toast.LENGTH_LONG).show();

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
                            newOrdersPojoClass.barcode = String.valueOf(json.get("barcode"));
                            barcodefromMenuItem  = String.valueOf(json.get("barcode"));
                        }
                        else{
                            newOrdersPojoClass.barcode = "barcode for this menu is missing";
                            Toast.makeText(mContext,"TMC barcode Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        for(int iterator =0;iterator<MenuItemStockAvlDetails.size();iterator++){
                            Modal_MenuItemStockAvlDetails modal_menuItemStockAvlDetails = MenuItemStockAvlDetails.get(iterator);
                            String barcodeFromMenuAvlDetails = modal_menuItemStockAvlDetails.getBarcode().toString();

                            if(barcodeFromMenuAvlDetails.equals(barcodefromMenuItem)){
                                newOrdersPojoClass.stockincomingkey = modal_menuItemStockAvlDetails.getStockincomingkey().toString();
                            }


                        }

                        if(json.has("gstpercentage")){
                            newOrdersPojoClass.gstpercentage = String.valueOf(json.get("gstpercentage"));

                        }
                        else{
                            newOrdersPojoClass.gstpercentage = "";
                            Toast.makeText(mContext,"TMC gstpercentage Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("applieddiscountpercentage")){
                            newOrdersPojoClass.discountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                        }
                        else{
                            newOrdersPojoClass.discountpercentage = "0";
                            Toast.makeText(mContext,"TMC applieddiscountpercentage Json is Missing",Toast.LENGTH_LONG).show();

                        }




                        if(json.has("itemweightdetails")){
                            try{
                                newOrdersPojoClass.itemweightdetails= String.valueOf(json.get("itemweightdetails"));

                            }
                            catch (Exception e){
                                newOrdersPojoClass.itemweightdetails = "nil";

                                e.printStackTrace();
                            }

                        }
                        else{
                            newOrdersPojoClass.itemweightdetails = "nil";
                            Log.d(Constants.TAG, "There is no key for this Menu: "  );


                        }


                        if(json.has("itemavailability")){
                            try{
                                newOrdersPojoClass.itemavailability= String.valueOf(json.get("itemavailability"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.itemavailability= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.itemavailability = "nil";
                            Log.d(Constants.TAG, "There is no itemavailability for this Menu: "  );


                        }

                        if(json.has("itemcutdetails")){
                            try{
                                newOrdersPojoClass.itemcutdetails= String.valueOf(json.get("itemcutdetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.itemcutdetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.itemcutdetails = "nil";
                            Log.d(Constants.TAG, "There is no key for this Menu: "  );


                        }

                        if(json.has("tmcctgykey")){
                            newOrdersPojoClass.tmcctgykey = String.valueOf(json.get("tmcctgykey"));
                            tmcsubctgykey = String.valueOf(json.get("tmcctgykey"));
                        }
                        else{
                            newOrdersPojoClass.tmcsubctgykey = "0";
                            tmcsubctgykey = "0";
                            Toast.makeText(mContext,"TMC tmcctgykey Json is Missing",Toast.LENGTH_LONG).show();
                            //Log.i("Tag", "TMC tmcsubctgykey Json is Missing"+ String.valueOf(newOrdersPojoClass.getTmcsubctgykey()));

                        }


                        if(json.has("key")){
                            newOrdersPojoClass.menuItemId= String.valueOf(json.get("key"));

                        }
                        else{
                            newOrdersPojoClass.menuItemId = "Key for this menu is missing";
                            Toast.makeText(mContext,"TMC menuItemId Json is Missing",Toast.LENGTH_LONG).show();

                        }


                        if(json.has("swiggyprice")){
                            newOrdersPojoClass.swiggyprice= String.valueOf(json.get("swiggyprice"));

                        }
                        else{
                            newOrdersPojoClass.swiggyprice = "0";
                            Toast.makeText(mContext,"TMC swiggyprice Json is Missing",Toast.LENGTH_LONG).show();

                        }

                        if(json.has("dunzoprice")){
                            newOrdersPojoClass.dunzoprice= String.valueOf(json.get("dunzoprice"));

                        }
                        else{
                            newOrdersPojoClass.dunzoprice = "0";
                            Toast.makeText(mContext,"TMC dunzoprice Json is Missing",Toast.LENGTH_LONG).show();

                        }

                        if(json.has("bigbasketprice")){
                            newOrdersPojoClass.bigbasketprice= String.valueOf(json.get("bigbasketprice"));

                        }
                        else{
                            newOrdersPojoClass.bigbasketprice = "0";
                            Toast.makeText(mContext,"TMC bigbasketprice Json is Missing",Toast.LENGTH_LONG).show();

                        }




                        if(json.has("inventorydetails")){
                            try{
                                newOrdersPojoClass.inventorydetails= String.valueOf(json.get("inventorydetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.inventorydetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.inventorydetails = "nil";
                            Log.d(Constants.TAG, "There is no inventorydetails for this Menu: "  );


                        }




                        if(json.has("barcode_AvlDetails")){
                            try{
                                newOrdersPojoClass.barcode_AvlDetails= String.valueOf(json.get("barcode_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.barcode_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.barcode_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no barcode_AvlDetails for this Menu: "  );


                        }




                        if(json.has("itemavailability_AvlDetails")){
                            try{
                                newOrdersPojoClass.itemavailability_AvlDetails= String.valueOf(json.get("itemavailability_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.itemavailability_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.itemavailability_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no itemavailability_AvlDetails for this Menu: "  );


                        }


                        if(json.has("key_AvlDetails")){
                            try{
                                newOrdersPojoClass.key_AvlDetails= String.valueOf(json.get("key_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.key_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.key_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no key_AvlDetails for this Menu: "  );


                        }


                        if(json.has("lastupdatedtime_AvlDetails")){
                            try{
                                newOrdersPojoClass.lastupdatedtime_AvlDetails= String.valueOf(json.get("lastupdatedtime_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.lastupdatedtime_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.lastupdatedtime_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no lastupdatedtime_AvlDetails for this Menu: "  );


                        }



                        if(json.has("menuitemkey_AvlDetails")){
                            try{
                                newOrdersPojoClass.menuitemkey_AvlDetails= String.valueOf(json.get("menuitemkey_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.menuitemkey_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.menuitemkey_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no menuitemkey_AvlDetails for this Menu: "  );


                        }


                        if(json.has("stockbalance_AvlDetails")){
                            try{
                                newOrdersPojoClass.stockbalance_AvlDetails= String.valueOf(json.get("stockbalance_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.stockbalance_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.stockbalance_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no stockbalance_AvlDetails for this Menu: "  );


                        }


                        if(json.has("receivedstock_AvlDetails")){
                            try{
                                newOrdersPojoClass.receivedstock_AvlDetails= String.valueOf(json.get("receivedstock_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.receivedstock_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.receivedstock_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no receivedstock_AvlDetails for this Menu: "  );


                        }

                        if(json.has("stockincomingkey_AvlDetails")){
                            try{
                                newOrdersPojoClass.stockincomingkey_AvlDetails= String.valueOf(json.get("stockincomingkey_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.stockincomingkey_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.stockincomingkey_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no stockincomingkey_AvlDetails for this Menu: "  );


                        }

                        if(json.has("vendorkey_AvlDetails")){
                            try{
                                newOrdersPojoClass.vendorkey_AvlDetails= String.valueOf(json.get("vendorkey_AvlDetails"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.vendorkey_AvlDetails= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.vendorkey_AvlDetails = "nil";
                            Log.d(Constants.TAG, "There is no vendorkey_AvlDetails for this Menu: "  );


                        }


                        if(json.has("allownegativestock")){
                            try{
                                newOrdersPojoClass.allownegativestock= String.valueOf(json.get("allownegativestock"));

                            }
                            catch (Exception e){
                                e.printStackTrace();
                                newOrdersPojoClass.allownegativestock= "nil";

                            }

                        }
                        else{
                            newOrdersPojoClass.allownegativestock = "nil";
                            Log.d(Constants.TAG, "There is no vendorkey_AvlDetails for this Menu: "  );


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

        MenuItems=getData();

        //Log.i(TAG, "call adapter cart_Item " + getData());

        completemenuItem= getMenuItemfromString(MenuItems);
    }

    public void add_amount_ForBillDetails() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        for(String Key :cart_Item_List){
            Modal_NewOrderItems newOrderItems = cartItem_hashmap.get(Key);

            //find total amount with out GST
            double new_total_amountfromArray = 0, discountpercentageDecimal=0, discountPercentage=0,newsavedAmount=0,taxes_and_chargesfromArray=0;
            if (newOrderItems != null) {
              try {
                  String itemFinalPrice_string = String.valueOf(newOrderItems.getItemFinalPrice());
                  new_total_amountfromArray = Double.parseDouble(itemFinalPrice_string);
              }
              catch (Exception e){
                  e.printStackTrace();
              }
                String discountPercentage_string;
              try {
                   discountPercentage_string = String.valueOf(newOrderItems.getDiscountpercentage());

                   discountPercentage = Double.parseDouble(discountPercentage_string);

              }
              catch (Exception e ){
                  e.printStackTrace();
              }


                //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);

              try {
                   discountpercentageDecimal = (100 - discountPercentage) / 100;

              }
              catch (Exception e){
                  e.printStackTrace();
              }
              try {
                   newsavedAmount = new_total_amountfromArray / discountpercentageDecimal;

              }
              catch (Exception e){
                  e.printStackTrace();

              }

              try{
                  newOrderItems.setSavedAmount(String.valueOf(decimalFormat.format(newsavedAmount)));

              }
              catch (Exception e ){
                  e.printStackTrace();

              }


                try{
                    new_total_amount = new_total_amountfromArray;
                    old_total_Amount = old_total_Amount + new_total_amount;
                    //Log.i(TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
                    //Log.i(TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);


                }
                catch (Exception e ){
                    e.printStackTrace();

                }


                try{
                     taxes_and_chargesfromArray = Double.parseDouble(newOrderItems.getGstpercentage());
                    //Log.i(TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);

                    taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);

                }
                catch (Exception e ){
                    e.printStackTrace();

                }



                try{
                    newOrderItems.setGstAmount(String.valueOf(decimalFormat.format(taxes_and_chargesfromArray)));

                    //Log.i(TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
                    //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
                    //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
                    new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
                    old_taxes_and_charges_Amount = old_taxes_and_charges_Amount + new_taxes_and_charges_Amount;

                }
                catch (Exception e ){
                    e.printStackTrace();

                }


                try {
                    //find subtotal
                    double subTotal_perItem = new_total_amount + new_taxes_and_charges_Amount;


                    newOrderItems.setSubTotal_perItem(String.valueOf(decimalFormat.format(subTotal_perItem)));


                    //find total payable Amount
                    new_to_pay_Amount = (old_total_Amount + old_taxes_and_charges_Amount);

                }
                catch (Exception e){
                    e.printStackTrace();

                }


                //find total GST amount

                try{
                    double subTotal_perItem = new_total_amount + new_taxes_and_charges_Amount;


                    newOrderItems.setSubTotal_perItem(String.valueOf(decimalFormat.format(subTotal_perItem)));


                    //find total payable Amount
                    new_to_pay_Amount = (old_total_Amount + old_taxes_and_charges_Amount);

                }catch (Exception e){
                    e.printStackTrace();
                }

                //find subtotal
             }
        }





        try{

            total_item_Rs_text_widget.setText(decimalFormat.format(old_total_Amount));
            taxes_and_Charges_rs_text_widget.setText(decimalFormat.format(old_taxes_and_charges_Amount));

             new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);
            finaltoPayAmount = String.valueOf(new_totalAmount_withGst)+".00";
            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_totalAmount_withGst)+".00");
            totalAmounttopay=new_totalAmount_withGst;
        }catch (Exception e){
            e.printStackTrace();
        }

        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


    }


    void createEmptyRowInListView(String empty) {
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

    private Handler newHandler() {
        Handler.Callback callback = new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String data = bundle.getString("CartItem");
                if(data.equals("")){
                    String data1 = bundle.getString("dropdown");
                    if (data1.equalsIgnoreCase("addNewItem")) {

                    }

                    if (data1.equalsIgnoreCase("addBillDetails")) {
                        //   createBillDetails(cart_Item_List);

                    }
                    if (String.valueOf(data1).equalsIgnoreCase("dropdown")) {
                        //Log.e(TAG, "dismissDropdown");
                        //Log.e(Constants.TAG, "createBillDetails in CartItem 0 ");

                        String mobileno = bundle.getString("mobileno");

                        mobileNo_Edit_widget.setText(mobileno);

                        autoComplete_customerNameText_widget.clearFocus();

                        autoComplete_customerNameText_widget.dismissDropDown();


                    }
                }
                else{
                    if (data.equalsIgnoreCase("addNewItem")) {

                    }

                    if (data.equalsIgnoreCase("addBillDetails")) {
                        //   createBillDetails(cart_Item_List);

                    }
                    if (String.valueOf(data).equalsIgnoreCase("dropdown")) {
                        //Log.e(TAG, "dismissDropdown");
                        //Log.e(Constants.TAG, "createBillDetails in CartItem 0 ");







                    }
                }

                return false;
            }
        };
        return new Handler(callback);
    }


    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
       String CurrentDatee = df.format(c);
        CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
        FormattedTime = dfTime.format(c);
        formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
    }



    private void PlaceOrder_in_OrderDetails(List<String> cart_Item_List, String Payment_mode, long sTime) {
      if(isOrderDetailsMethodCalled){
          return;
      }

        isOrderDetailsMethodCalled = true;
        String newOrderId = String.valueOf(sTime);
            SharedPreferences sh
                    = mContext.getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);


            String merchantorderid = "";
            String couponid = "";

            String CouponDiscountAmount = "";
        if(isDiscountApplied) {
            CouponDiscountAmount = discount_rs_text_widget.getText().toString();
        }
        else{
            CouponDiscountAmount ="0";
        }
            String DeliveryAmount = "";

            String orderid = String.valueOf(sTime);
            String orderplacedTime = Currenttime;
            String tokenno = "";
            String userid = "";
             ordertype = Constants.POSORDER;
            String deliverytype = Constants.STOREPICKUP_DELIVERYTYPE;
            String slotdate = "";
            if((OrderTypefromSpinner.equals(Constants.PhoneOrder))){
                ordertype = OrderTypefromSpinner;
                deliverytype = Constants.HOME_DELIVERY_DELIVERYTYPE;
                slotdate  = CurrentDate;
            }

            String slotname = "EXPRESSDELIVERY";

            String orderPlacedDate = CurrentDate;
        double totalgrossweightingrams_doubleFromLoop = 0, totalgrossFromInsideAndOutsideLoop = 0;

            String slottimerange = "";
            String UserMobile = "+91" + mobileNo_Edit_widget.getText().toString();
            String vendorkey = sh.getString("VendorKey", "");
            String vendorName = sh.getString("VendorName", "");
            String itemTotalwithoutGst = total_item_Rs_text_widget.getText().toString();
            String payableAmount = total_Rs_to_Pay_text_widget.getText().toString();
          try {
              double payableamount_double = Double.parseDouble(payableAmount);

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

                                      autoComplete_customerNameText_widget.setText("");
                                      autoComplete_customerNameText_widget.dismissDropDown();
                                      cart_Item_List.clear();
                                      cartItem_hashmap.clear();
                                      ispaymentMode_Clicked = false;
                                      isOrderDetailsMethodCalled = false;

                                      isPaymentDetailsMethodCalled = false;
                                      isOrderTrackingDetailsMethodCalled = false;
                                      new_to_pay_Amount = 0;
                                      old_taxes_and_charges_Amount = 0;
                                      old_total_Amount = 0;
                                      createEmptyRowInListView("empty");
                                      CallAdapter();
                                      discountAmount = "0";
                                      isDiscountApplied = false;
                                      discount_Edit_widget.setText("0");
                                      finaltoPayAmount = "0";
                                      discount_rs_text_widget.setText(discountAmount);
                                      OrderTypefromSpinner = "POS Order";
                                      orderTypeSpinner.setSelection(0);
                                      total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                      taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                      total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                      mobileNo_Edit_widget.setText("");
                                      isPrintedSecondTime = false;
                                      showProgressBar(false);

                                      ispointsApplied_redeemClicked=false;
                                      isProceedtoCheckoutinRedeemdialogClicked =false;
                                      isRedeemDialogboxOpened=false;
                                      isUpdateRedeemPointsMethodCalled=false;
                                      isUpdateCouponTransactionMethodCalled=false;
                                      isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                      totalAmounttopay=0;
                                      finalamounttoPay=0;

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
                                      useStoreNumberCheckBox.setChecked(false);
                                      StockBalanceChangedForThisItemList.clear();
                                      isStockOutGoingAlreadyCalledForthisItem =false;
                                      redeemed_points_text_widget.setText("");
                                      redeemPointsLayout.setVisibility(View.GONE);
                                      //discountlayout visible
                                    discountAmountLayout.setVisibility(View.GONE);
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
            String taxAmount = taxes_and_Charges_rs_text_widget.getText().toString();
                PlaceOrder_in_PaymentTransactionDetails(sTime, Payment_mode, payableAmount, UserMobile);

            JSONArray itemDespArray = new JSONArray();

            for (int i = 0; i < cart_Item_List.size(); i++) {
                String itemUniqueCode = cart_Item_List.get(i);
                Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);
                String itemName =
                        String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());

                if(itemName.contains("Grill House")){
                    itemName =  itemName.replace("Grill House ","");
                }
                else if(itemName.contains("Ready to Cook")){
                    itemName =  itemName.replace("Ready to Cook","");
                }
                else{
                    itemName=itemName;
                }
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
                String quantity = "";
                if( modal_newOrderItems.getQuantity()!=null){
                    quantity =  modal_newOrderItems.getQuantity();;

                }
                else{
                    quantity = "";
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

                String grossweight ="";
                        if(modal_newOrderItems.getGrossweight()!=null){
                            grossweight    = modal_newOrderItems.getGrossweight();

                        }
                        else{
                            grossweight ="";
                        }
                String subCtgyKey = "";
                        if(modal_newOrderItems.getTmcsubctgykey()!=null){
                            subCtgyKey =   modal_newOrderItems.getTmcsubctgykey();
                        }
                        else{
                            subCtgyKey = "";
                        }

                String grossWeightingrams = "";
               try {
                   if (!grossweight.equals("")) {
                       grossWeightingrams = grossweight.replaceAll("[^\\d.]", "");

                   }
               }
               catch(Exception e){
                   e.printStackTrace();
               }
                double grossweightingrams_double =0;
                try{
                    if(!grossWeightingrams.equals("")) {
                        grossweightingrams_double = Double.parseDouble(grossWeightingrams);
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }



//////////////////////
                if(isinventorycheck) {


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


                    String ItemUniquecodeofItem = "";
                    if ((modal_newOrderItems.getItemuniquecode() != null) && (!modal_newOrderItems.getItemuniquecode().equals("null")) && (!modal_newOrderItems.getItemuniquecode().equals(""))) {
                        ItemUniquecodeofItem = String.valueOf(modal_newOrderItems.getItemuniquecode());
                    } else {
                        ItemUniquecodeofItem = "";
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


                        if (inventoryDetails_firstItem.equals("nil")) {
                            if (priceTypeOfItem.toUpperCase().equals(Constants.TMCPRICEPERKG)) {


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

                            } else {
                                getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_AvlDetails, key_AvlDetails, menuItemKey, receivedStock_AvlDetails, grossWeightWithQuantity_double, itemName, barcode, orderid, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey, isitemAvailable, allowNegativeStock);

                            }


                        }
                        else {
                            try {

                                JSONArray jsonArray = new JSONArray(String.valueOf(inventoryDetails_firstItem));
                                int jsonArrayIterator = 0;
                                int jsonArrayCount = jsonArray.length();
                                for (; jsonArrayIterator < (jsonArrayCount); jsonArrayIterator++) {

                                    try {
                                        String menuItemKeyFromInventoryDetails;
                                        double grossweightinGramsFromInventoryDetails, netweightingramsFromInventoryDetails;
                                        JSONObject json_InventoryDetails = jsonArray.getJSONObject(jsonArrayIterator);
                                        menuItemKeyFromInventoryDetails = "";
                                        grossweightinGramsFromInventoryDetails = 0;
                                        netweightingramsFromInventoryDetails = 0;

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
                                            e.printStackTrace();
                                        }


                                        try {
                                            netweightingramsFromInventoryDetails = Double.parseDouble(json_InventoryDetails.getString("netweightingrams"));
                                        } catch (Exception e) {
                                            netweightingramsFromInventoryDetails = 0;
                                            e.printStackTrace();
                                        }


                                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < completemenuItem.size(); iterator_menuitemStockAvlDetails++) {

                                            Modal_NewOrderItems modal_menuItemStockAvlDetails = completemenuItem.get(iterator_menuitemStockAvlDetails);

                                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getKey());

                                            if (menuItemKeyFromInventoryDetails.equals(menuItemKeyFromMenuAvlDetails)) {

                                          ////////for same inventoryDetails Item - starting

                                                for (int iterator = 0; iterator < cart_Item_List.size(); iterator++) {
                                                    String hashmapkey = "";
                                                    hashmapkey = cart_Item_List.get(iterator);
                                                    String itemUniquecodeFromLoop = "", inventoryDetails_secondItem = "", priceTypeOfPOS = "";
                                                    String menuItemKeyFromInventoryDetails_secondItem ="";

                                                    Modal_NewOrderItems modal_newOrderItems_insideLoop = cartItem_hashmap.get(hashmapkey);
                                                    try {
                                                        itemUniquecodeFromLoop = modal_newOrderItems_insideLoop.getItemuniquecode().toString();

                                                    } catch (Exception e) {
                                                        itemUniquecodeFromLoop = "";
                                                        e.printStackTrace();
                                                    }


                                                    if (!itemUniquecodeFromLoop.equals(ItemUniquecodeofItem)) {
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
                                                                     grossweightinGramsFromInventoryDetails_secondItem="0"; netweightingramsFromInventoryDetails_secondItem="0";

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
                                                                if(json_InventoryDetails_secondItem.has("grossweightingrams")) {
                                                                    grossweightinGramsFromInventoryDetails_secondItem = String.valueOf(json_InventoryDetails_secondItem.getString("grossweightingrams"));
                                                                }
                                                                else{

                                                                    grossweightinGramsFromInventoryDetails_secondItem = "0";

                                                                }
                                                            } catch (Exception e) {
                                                                grossweightinGramsFromInventoryDetails_secondItem = "0";
                                                                e.printStackTrace();
                                                            }


                                                            try {
                                                                netweightingramsFromInventoryDetails_secondItem =String.valueOf(json_InventoryDetails_secondItem.getString("netweightingrams"));
                                                            } catch (Exception e) {
                                                                netweightingramsFromInventoryDetails_secondItem = "0";
                                                                e.printStackTrace();
                                                            }


                                                            } catch (Exception e) {
                                                            e.printStackTrace();
                                                            }

                                                        if(menuItemKeyFromInventoryDetails_secondItem.equals(menuItemKeyFromMenuAvlDetails)) {
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


                                                                    try{
                                                                        grossweight_secondItemInventoryDetails = grossweightinGramsFromInventoryDetails_secondItem;

                                                                    } catch (Exception e){
                                                                        e.printStackTrace();
                                                                        grossweight_secondItemInventoryDetails = "0";
                                                                    }

                                                                    try{
                                                                        if(grossweight_secondItemInventoryDetails.equals("") || (grossweight_secondItemInventoryDetails.equals("0"))){
                                                                            try{
                                                                                grossweight_secondItemInventoryDetails = modal_newOrderItems_insideLoop.getGrossweight().toString();

                                                                            } catch (Exception e){
                                                                                e.printStackTrace();
                                                                                grossweight_secondItemInventoryDetails = "1";
                                                                            }
                                                                        }
                                                                    }
                                                                    catch (Exception e){
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
                                                boolean itemAvailability_avlDetail=true;

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


                                                if (priceTypeOfItem.toUpperCase().equals(Constants.TMCPRICEPERKG)) {


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

                                                } else {
                                                    getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossweightinGramsFromInventoryDetails, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, itemAvailability_avlDetail, allowNegativeStock);

                                                }

                                                //  getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(stockIncomingKey_avlDetail, Key_avlDetail, menuItemKeyFromMenuAvlDetails, receivedStock_avlDetail, grossWeightWithQuantity_double, itemName_avlDetail_inventoryDetails, barcode_avlDetail, orderid, priceTypeForPOS_avlDetail, tmcCtgy_avlDetail, tmcSubCtgy_avlDetail, isitemAvailable, allowNegativeStock);


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
                    ///////////////////////



                PlaceOrder_in_OrderItemDetails(subCtgyKey,itemName,grossweight, weight,netweight, quantity, price, "", GstAmount, vendorkey, Currenttime, sTime, vendorkey, vendorName,grossWeightingrams,grossweightingrams_double);


                JSONObject itemdespObject = new JSONObject();
                try {
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
            if((redeemPoints_String .equals("0"))||(redeemPoints_String.equals("0.00"))||(redeemPoints_String.equals(""))) {
                StoreCoupon = "";


            }
            else{
                StoreCoupon = "REDEEM";

            }
        }
        else{
            StoreCoupon = "STORECOUPON";

        }

        if((CouponDiscountAmount .equals("0"))||(CouponDiscountAmount.equals("0.00"))||(CouponDiscountAmount.equals(""))){


            if((redeemPoints_String .equals("0"))||(redeemPoints_String.equals("0.00"))||(redeemPoints_String.equals(""))) {
                CouponDiscountAmount = "";

            }
            else{
                CouponDiscountAmount = redeemPoints_String;
            }


        }

        if(StoreCoupon.equals("STORECOUPON")){
            String transactiontime = getDate_and_time();


            addDatatoCouponTransactioninDB(CouponDiscountAmount,"STORECOUPON",UserMobile,String.valueOf(sTime),CurrentDate,transactiontime,vendorKey);


        }

            if(String.valueOf(Payment_mode).toUpperCase().equals(Constants.CREDIT)){
                    double payableAmount_double = 0;
                    String usermobileno = "";
                    try{
                        usermobileno = "+91"+mobileNo_Edit_widget.getText().toString();
                    }
                    catch (Exception e) {
                    e.printStackTrace();
                    }
                    
                        try{
                            if((!payableAmount.equals("null")) && (!payableAmount.equals("")) && (!payableAmount.equals(null)) ){
                                payableAmount_double  = Double.parseDouble(payableAmount);

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
                    if (!CouponDiscountAmount.equals("")&&(!CouponDiscountAmount.equals("0"))) {
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
                jsonObject.put("deliveryamount", 0);
                jsonObject.put("couponkey", StoreCoupon);

                jsonObject.put("ordertype", ordertype);
                jsonObject.put("gstamount", Double.parseDouble(taxAmount));

                jsonObject.put("deliverytype", deliverytype);
                jsonObject.put("slotname", slotname);
                jsonObject.put("slotdate", "");
                jsonObject.put("slottimerange", "");

                jsonObject.put("orderid", orderid);
                jsonObject.put("orderplacedtime", orderplacedTime);
                jsonObject.put("tokenno", (tokenno));
                jsonObject.put("userid", userid);

                jsonObject.put("usermobile", UserMobile);
                jsonObject.put("vendorkey", vendorkey);
                jsonObject.put("vendorname", vendorName);
                jsonObject.put("payableamount", Double.parseDouble(payableAmount));

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

        String finalCouponDiscountAmount = CouponDiscountAmount;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInOrderDetailsTable,
                    jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(@NonNull JSONObject response) {

                    //Log.d(Constants.TAG, "Response: " + response);
                    try {
                        String message = response.getString("message");

                        //addDatatoOrderDetailsResponse(message,orderid,UserMobile,vendorkey,payableAmount,orderplacedTime);
                        if (message.equals("success")) {
                            // StartTwice startTwice =new StartTwice(UserMobile,tokenno,itemTotalwithoutGst,taxAmount,payableAmount,orderid,cart_Item_List,cartItem_hashmap,Payment_mode);
                            // startTwice.main();
                            try {
                                if(printerType_sharedPreference.equals(Constants.USB_PrinterType)){
                                    PrintReciptForNewItemUsingUSBPrinter(orderplacedTime, UserMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, Payment_mode, finalCouponDiscountAmount, ordertype);

                                }
                                else if(printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)){
                                      printReciptUsingBluetoothPrinter(orderplacedTime, UserMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, Payment_mode, finalCouponDiscountAmount, ordertype);

                                }
                                else if(printerType_sharedPreference.equals(Constants.POS_PrinterType)){
                                    int i = (PrinterFunctions.CheckStatus(portName,portSettings,1));
                                    if(i != -1){
                                        printRecipt(UserMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, Payment_mode, finalCouponDiscountAmount,ordertype);

                                    }
                                    else{
                                            new TMCAlertDialogClass(mContext, R.string.app_name, R.string.OrderPlaced_Printer_is_Disconnected,
                                                    R.string.OK_Text,R.string.Empty_Text,
                                                    new TMCAlertDialogClass.AlertListener() {
                                                        @Override
                                                        public void onYes() {
                                                            StockBalanceChangedForThisItemList.clear();
                                                            isStockOutGoingAlreadyCalledForthisItem =false;

                                                            NewOrders_MenuItem_Fragment.cart_Item_List.clear();
                                                            NewOrders_MenuItem_Fragment.cartItem_hashmap.clear();

                                                            ispaymentMode_Clicked = false;
                                                            isOrderDetailsMethodCalled = false;

                                                            isPaymentDetailsMethodCalled = false;
                                                            isOrderTrackingDetailsMethodCalled = false;
                                                            new_to_pay_Amount = 0;
                                                            old_taxes_and_charges_Amount = 0;
                                                            old_total_Amount = 0;
                                                            createEmptyRowInListView("empty");
                                                            CallAdapter();
                                                            discountAmount = "0";
                                                            isDiscountApplied = false;
                                                            discount_Edit_widget.setText("0");
                                                            finaltoPayAmount = "0";
                                                            discount_rs_text_widget.setText(discountAmount);
                                                            OrderTypefromSpinner = "POS Order";
                                                            orderTypeSpinner.setSelection(0);
                                                            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                                            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                                            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                                            mobileNo_Edit_widget.setText("");
                                                            isPrintedSecondTime = false;
                                                            showProgressBar(false);
                                                            useStoreNumberCheckBox.setChecked(false);
                                                            ispointsApplied_redeemClicked=false;
                                                            isProceedtoCheckoutinRedeemdialogClicked =false;
                                                            isRedeemDialogboxOpened=false;
                                                            isUpdateRedeemPointsMethodCalled=false;
                                                            isUpdateCouponTransactionMethodCalled=false;
                                                            isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                                            totalAmounttopay=0;
                                                            finalamounttoPay=0;
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
                                                            //discountlayout visible
                                                            discountAmountLayout.setVisibility(View.GONE);                                                        }

                                                        @Override
                                                        public void onNo() {

                                                        }
                                                    });


                                    }

                                }
                                else {
                                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.OrderPlaced_Printer_is_Disconnected,
                                            R.string.OK_Text,R.string.Empty_Text,
                                            new TMCAlertDialogClass.AlertListener() {
                                                @Override
                                                public void onYes() {
                                                    StockBalanceChangedForThisItemList.clear();
                                                    isStockOutGoingAlreadyCalledForthisItem =false;

                                                    NewOrders_MenuItem_Fragment.cart_Item_List.clear();
                                                    NewOrders_MenuItem_Fragment.cartItem_hashmap.clear();

                                                    ispaymentMode_Clicked = false;
                                                    isOrderDetailsMethodCalled = false;

                                                    isPaymentDetailsMethodCalled = false;
                                                    isOrderTrackingDetailsMethodCalled = false;
                                                    new_to_pay_Amount = 0;
                                                    old_taxes_and_charges_Amount = 0;
                                                    old_total_Amount = 0;
                                                    createEmptyRowInListView("empty");
                                                    CallAdapter();
                                                    discountAmount = "0";
                                                    isDiscountApplied = false;
                                                    discount_Edit_widget.setText("0");
                                                    finaltoPayAmount = "0";
                                                    discount_rs_text_widget.setText(discountAmount);
                                                    OrderTypefromSpinner = "POS Order";
                                                    orderTypeSpinner.setSelection(0);
                                                    total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                                    taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                                    total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                                    mobileNo_Edit_widget.setText("");
                                                    isPrintedSecondTime = false;
                                                    showProgressBar(false);
                                                    useStoreNumberCheckBox.setChecked(false);
                                                    ispointsApplied_redeemClicked=false;
                                                    isProceedtoCheckoutinRedeemdialogClicked =false;
                                                    isRedeemDialogboxOpened=false;
                                                    isUpdateRedeemPointsMethodCalled=false;
                                                    isUpdateCouponTransactionMethodCalled=false;
                                                    isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                                    totalAmounttopay=0;
                                                    finalamounttoPay=0;
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
                                                    //discountlayout visible
                                                    discountAmountLayout.setVisibility(View.GONE);
                                                }

                                                @Override
                                                public void onNo() {

                                                }
                                            });
                                }


                            }
                            catch(Exception e ){
                                e.printStackTrace();


                            }
/*
                            autoComplete_customerNameText_widget.setText("");
                                    autoComplete_customerNameText_widget.dismissDropDown();
                            StockBalanceChangedForThisItemList.clear();
                            cart_Item_List.clear();
                            cartItem_hashmap.clear();
                            ispaymentMode_Clicked = false;
                            isOrderDetailsMethodCalled = false;

                            isPaymentDetailsMethodCalled = false;
                            isOrderTrackingDetailsMethodCalled = false;
                            new_to_pay_Amount = 0;
                            old_taxes_and_charges_Amount = 0;
                            old_total_Amount = 0;
                            createEmptyRowInListView("empty");
                            CallAdapter();
                            discountAmount = "0";
                            useStoreNumberCheckBox.setChecked(false);
                            isDiscountApplied = false;
                            discount_Edit_widget.setText("0");
                            finaltoPayAmount = "0";
                            discount_rs_text_widget.setText(discountAmount);
                            OrderTypefromSpinner = "POS Order";
                            orderTypeSpinner.setSelection(0);
                            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                            mobileNo_Edit_widget.setText("");
                            isPrintedSecondTime = false;
                            showProgressBar(false);

                            ispointsApplied_redeemClicked=false;
                            isProceedtoCheckoutinRedeemdialogClicked =false;
                            isRedeemDialogboxOpened=false;
                            isUpdateRedeemPointsMethodCalled=false;
                            isUpdateCouponTransactionMethodCalled=false;
                            isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                            totalAmounttopay=0;
                            finalamounttoPay=0;

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
                            //discountlayout visible
                                    discountAmountLayout.setVisibility(View.GONE);
*/



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
    private void updateDatainCreditOrderDetailsTable(double payableAmount_double, String orderid, String usermobileno, String orderplacedTime) {
        if(isUpdateCreditOrderDetailsIsCalled){
            return;
        }
        isUpdateCreditOrderDetailsIsCalled =true;

        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("usermobileno", usermobileno);
            jsonObject.put("lastupdatedtime", orderplacedTime);
            jsonObject.put("totalamountincredit", payableAmount_double);
            jsonObject.put("vendorkey", vendorKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_UpdateCreditOrderDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {


                    }
                    else{

                    }
                } catch (JSONException e) {
                    isUpdateCreditOrderDetailsIsCalled=false;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                isUpdateCreditOrderDetailsIsCalled=false;

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


     */
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

    private void getStockItemOutGoingDetailsAndUpdateMenuItemStockAvlDetails(String stockIncomingKey_avlDetails, String key_avlDetails, String menuItemKey_avlDetails, String receivedStock_AvlDetails, double currentBillingItemWeight_double, String itemName, String barcode, String orderid, String priceTypeForPOS, String tmcCtgy, String tmcSubCtgyKey, boolean isitemAvailable, boolean allowNegativeStock) {
        if((!stockIncomingKey_avlDetails.equals("")) && (!stockIncomingKey_avlDetails.equals(" - ")) &&(!stockIncomingKey_avlDetails.equals("null")) && (!stockIncomingKey_avlDetails.equals(null)) && (!stockIncomingKey_avlDetails.equals("0")) && (!stockIncomingKey_avlDetails.equals(" 0 ")) && (!stockIncomingKey_avlDetails.equals("-")) && (!stockIncomingKey_avlDetails.equals("nil"))) {
            showProgressBar(true);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    final String[] outgoingtype_stockOutGngDetails_String = {""};
                    final String[] stockincomingkey_stockOutGngDetails_String = {""};
                    final String[] stocktype_stockOutGngDetails_String = {""};
                    final String[] outgoingqty_stockOutGngDetails_String = {""};
                    final String[] tmcSubCtgyKey_stockOutGngDetails_String = {""};

                    final double[] outgoingqty_stockOutGngDetails_Double = {0};
                    final double[] Total_outgoingqty_stockOutGngDetails_Double = {0};
                    final double[] receivedStock_AvlDetails_double = {0};
                    final double[] finalStockBalance_double = {0};


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofStockOutGoingDetailsForStockIncmgKey + stockIncomingKey_avlDetails, null,
                            new com.android.volley.Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(@NonNull JSONObject response) {


                                    try {
                                        Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                        JSONArray JArray = response.getJSONArray("content");

                                        int arrayLength = JArray.length();
                                        for (int i = 0; i < arrayLength; i++) {
                                            JSONObject json = JArray.getJSONObject(i);
                                            outgoingqty_stockOutGngDetails_Double[0] = 0;
                                            outgoingqty_stockOutGngDetails_Double[0] = 0;
                                            stocktype_stockOutGngDetails_String[0] = "";
                                            outgoingtype_stockOutGngDetails_String[0] = "";
                                            stockincomingkey_stockOutGngDetails_String[0] = "";
                                            outgoingqty_stockOutGngDetails_String[0] = "0";
                                            receivedStock_AvlDetails_double[0] = 0;
                                            tmcSubCtgyKey_stockOutGngDetails_String[0] = "0";

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
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


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

                                            try {
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            if (outgoingtype_stockOutGngDetails_String[0].equals(Constants.SUPPLYGAP_OUTGOINGTYPE)) {

                                                try {
                                                    //    if (Total_outgoingqty_stockOutGngDetails_Double[0] > outgoingqty_stockOutGngDetails_Double[0]) {
                                                    //    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];

                                                    //   } else {
                                                    //       Total_outgoingqty_stockOutGngDetails_Double[0] = outgoingqty_stockOutGngDetails_Double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                                    // }
                                                    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] - outgoingqty_stockOutGngDetails_Double[0];

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                            } else {
                                                try {
                                                    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] + outgoingqty_stockOutGngDetails_Double[0];


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        }
                                        }


                                        try {
                                            receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedStock_AvlDetails);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        double stockBalanceBeforeMinusCurrentItem = 0;
                                        try {
                                            stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        try {
                                            finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem - currentBillingItemWeight_double;
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        AddDataInStockBalanceTransactionHistory(finalStockBalance_double[0], stockBalanceBeforeMinusCurrentItem, menuItemKey_avlDetails, stockIncomingKey_avlDetails, itemName, barcode, menuItemKey_avlDetails);


                                        AddDataInStockOutGoingTable(currentBillingItemWeight_double, orderid, stockIncomingKey_avlDetails, itemName, barcode, priceTypeForPOS, tmcCtgy, tmcSubCtgyKey);
                                        if (isitemAvailable) {

                                            if (finalStockBalance_double[0] <= 0) {

                                                if (!allowNegativeStock) {


                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], true, false, menuItemKey_avlDetails,tmcSubCtgyKey_stockOutGngDetails_String[0],itemName);

                                                } else {
                                                    UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                                }


                                            } else {
                                                UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                            }
                                        } else {
                                            UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                        }

                                    } catch (Exception e) {
                                        UpdateStockBalanceinMenuItemStockAvlDetail(key_avlDetails, finalStockBalance_double[0], false, isitemAvailable, menuItemKey_avlDetails, tmcSubCtgyKey_stockOutGngDetails_String[0], itemName);

                                        e.printStackTrace();
                                    }


                                }

                            }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(@NonNull VolleyError error) {
                            Log.d(Constants.TAG, " response: onMobileAppData error " + error.getLocalizedMessage());

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
            turnoffProgressBarAndResetArray();
            Toast.makeText(mContext, "No  Menu Item Stock  details for " + itemName, Toast.LENGTH_LONG).show();

        }


    }


    private void AddDataInStockBalanceTransactionHistory(double finalStockBalance_double, double stockBalanceBeforeMinusCurrentItem, String menuItemKey_avlDetails, String stockIncomingKey_avlDetails, String itemName, String barcode, String menuItemKeyAvlDetaildds) {

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
                     //   showProgressBar(false);
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
        showProgressBar(true);
        JSONObject  jsonObject = new JSONObject();
    if(changeItemAvailability){


        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject2 = new JSONObject();
        try {
            jsonObject2.put("key", menuItemKey_avlDetails);


            jsonObject2.put("itemavailability", String.valueOf(isitemAvailable).toUpperCase());


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
                            modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(isitemAvailable));
                            uploadMenuAvailabilityStatusTranscationinDB(usermobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorKey,Currenttime,menuItemKey_avlDetails,message, "", false, "");
                            savedMenuIteminSharedPrefrences(completemenuItem,iterator_menuitemStockAvlDetails);

                        }

                    }
                }

              //  showProgressBar(false);

            }
        }, new Response.ErrorListener() {
             @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                showProgressBar(false);
                Toast.makeText(mContext,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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




        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_MenuItemStockAvlDetails
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );

                            if(message.equals("success")) {


                            for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < completemenuItem.size(); iterator_menuitemStockAvlDetails++) {

                                Modal_NewOrderItems modal_menuItemStockAvlDetails = completemenuItem.get(iterator_menuitemStockAvlDetails);

                                String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuitemkey_AvlDetails());
                                if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {

                                    if(changeItemAvailability) {
                                    uploadMenuAvailabilityStatusTranscationinDB(usermobileNo, itemName, isitemAvailable, tmcSubCtgyKey, vendorKey, Currenttime, menuItemKey_avlDetails, message, key_avlDetails, false, key_avlDetails);

                                        modal_menuItemStockAvlDetails.setItemavailability_AvlDetails(String.valueOf(isitemAvailable));
                                        modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(isitemAvailable));
                                        modal_menuItemStockAvlDetails.setStockbalance_AvlDetails(String.valueOf(finalStockBalance_double));

                                        savedMenuIteminSharedPrefrences(completemenuItem,iterator_menuitemStockAvlDetails);

                                    }
                                    else{
                                        modal_menuItemStockAvlDetails.setStockbalance_AvlDetails(String.valueOf(finalStockBalance_double));

                                        savedMenuIteminSharedPrefrences(completemenuItem,iterator_menuitemStockAvlDetails);
                                    }
                                }
                            }
                        }

                                showProgressBar(false);




                } catch (JSONException e) {
                    // showProgressBar(false);
                    Toast.makeText(mContext,"Failed to change express delivery slot status in Delivery slots",Toast.LENGTH_LONG).show();
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
                Toast.makeText(mContext,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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

    private void savedMenuIteminSharedPrefrences(List<Modal_NewOrderItems> menuItem, int iterator_menuitemStockAvlDetails) {
        final SharedPreferences sharedPreferencesMenuitem = mContext.getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList",json );
        editor.apply();
        try {
            adapter_cartItem_recyclerview.notifyDataSetChanged();
            adapter_cartItem_recyclerview.notify();
            adapter_cartItem_recyclerview.notifyItemChanged(iterator_menuitemStockAvlDetails);

            adapter_cartItem_recyclerview.notifyAll();
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
            jsonObject.put("outgoingtype", String.valueOf(Constants.SALES_FULFILLED_OUTGOINGTYPE));
            jsonObject.put("outgoingqty",grossweightingrams_double);
            jsonObject.put("salesorderid", String.valueOf(orderid));
            jsonObject.put("stocktype",(stockType));
            jsonObject.put("tmcctgykey", String.valueOf(tmcCtgy));
            jsonObject.put("tmcsubctgykey", String.valueOf(tmcSubCtgyKey));
            jsonObject.put("vendorkey", String.valueOf(vendorKey));
            jsonObject.put("userkey", String.valueOf(""));
            jsonObject.put("inventoryusermobileno", String.valueOf(usermobileNo));
            jsonObject.put("stockincomingkey", String.valueOf(stockIncomingKey_avlDetails));



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









    private void addDatatoOrderDetailsResponse(String message, String orderid, String userMobile, String vendorkey, String payableAmount, String orderplacedTime) {
        JSONObject jsonObject = new JSONObject();

        try{

        jsonObject.put("message", message);
        jsonObject.put("orderid", orderid);
        jsonObject.put("userMobile", userMobile);
        jsonObject.put("vendorkey", vendorkey);
        jsonObject.put("payableAmount", payableAmount);
        jsonObject.put("orderplacedTime", orderplacedTime);

    } catch (JSONException e) {
        e.printStackTrace();
    }


    //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsResponseTable,
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

    private void PlaceOrder_in_OrderItemDetails(String subCtgyKey, String itemnamee, String Grossweight, String itemweightt,
                                                String Netweight, String quantityy, String itemamountt,
                                                String discountamountt,
                                                String gstamountt, String vendorkeyy, String currenttime,
                                                long sTime, String vendorkey, String vendorName, String grossWeightingrams, double grossweightingrams_double){

            String orderid = String.valueOf(sTime);

            JSONObject jsonObject = new JSONObject();
            try {
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


    private void PlaceOrder_in_OrderTrackingDetails(long sTime,String Currenttiime) {

        if(isOrderTrackingDetailsMethodCalled){
            return;
        }

        isOrderTrackingDetailsMethodCalled = true;

        String orderid = String.valueOf(sTime);
        String orderplacedDate_time = getDate_and_time();
        //Log.d(Constants.TAG, "orderplacedDate_time: " + orderplacedDate_time);
        //Log.d(Constants.TAG, "orderplacedDate_time: " + getDate_and_time());
        //Log.d(Constants.TAG, "orderplacedDate_time: " + Currenttiime);
        //Log.d(Constants.TAG, "orderplacedDate_time: " + Currenttime);

        SharedPreferences sh
                = mContext.getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);
        String vendorkey = sh.getString("VendorKey","");

        JSONObject  orderTrackingTablejsonObject = new JSONObject();
        try {
            orderTrackingTablejsonObject.put("orderdeliverytime",Currenttime);
            orderTrackingTablejsonObject.put("orderplacedtime",Currenttime);

            orderTrackingTablejsonObject.put("usermobileno","+91" + mobileNo_Edit_widget.getText().toString());
            orderTrackingTablejsonObject.put("orderid",orderid);
            orderTrackingTablejsonObject.put("vendorkey",vendorkey);
            orderTrackingTablejsonObject.put("orderstatus","DELIVERED");

        }


        catch (JSONException e) {
            e.printStackTrace();

        }


        //Log.d(Constants.TAG, "orderplacedDate_time Payload  : " + orderTrackingTablejsonObject);
        //Log.d(Constants.TAG, "orderplacedDate_time: " + orderplacedDate_time);
        //Log.d(Constants.TAG, "orderplacedDate_time: " + getDate_and_time());
        //Log.d(Constants.TAG, "orderplacedDate_time: " + Currenttiime);
        //Log.d(Constants.TAG, "orderplacedDate_time: " + Currenttime);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInOrderTrackingDetailsTable,
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



    private void saveredeemDetailsinSharePreferences(String maxpointsinaday, String minordervalueforredeem, String pointsfor100rs) {
        final SharedPreferences sharedPreferences = mContext.getSharedPreferences("RedeemData", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("maxpointsinaday", maxpointsinaday);
        editor.putString("minordervalueforredeem", minordervalueforredeem);
        editor.putString("pointsfor100rs", pointsfor100rs);


        editor.apply();





    }



    private void PrintReciptForNewItemUsingUSBPrinter(String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String payableAmount, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String finalCouponDiscountAmount, String ordertype) {




        modal_usbPrinter = new Modal_USBPrinter();
        try{

            modal_usbPrinter.orderplacedTime = orderplacedTime;
            modal_usbPrinter.userMobile = userMobile;
            modal_usbPrinter.tokenno = tokenno;
            modal_usbPrinter.itemTotalwithoutGst = itemTotalwithoutGst;
            modal_usbPrinter.taxAmount = taxAmount;
            modal_usbPrinter.payableAmount = payableAmount;
            modal_usbPrinter.orderid = orderid;
            modal_usbPrinter.cart_Item_List = cart_item_list;
            modal_usbPrinter.cartItem_hashmap = cartItem_hashmap;
            modal_usbPrinter.payment_mode = payment_mode;
            modal_usbPrinter.finalCouponDiscountAmount = finalCouponDiscountAmount;
            modal_usbPrinter.ordertype = ordertype;



        }
        catch (Exception e){
            e.printStackTrace();
        }

        UsbConnection usbConnection = UsbPrintersConnectionsLocal.selectFirstConnected(mContext);
        UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);

        if (usbConnection == null || usbManager == null) {
            showProgressBar(false);

          /*  new AlertDialog.Builder(mContext)
                    .setTitle("USB Connection")
                    .setMessage("No USB printer found.")
                    .show();

           */

            new TMCAlertDialogClass(mContext, R.string.app_name, R.string.ReConnect_Instruction,
                    R.string.OK_Text, R.string.Cancel_Text,
                    new TMCAlertDialogClass.AlertListener() {
                        @Override
                        public void onYes() {
                            PrintReciptForNewItemUsingUSBPrinter(orderplacedTime, userMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, payment_mode, finalCouponDiscountAmount, ordertype);

                            return;
                        }

                        @Override
                        public void onNo() {
                            StockBalanceChangedForThisItemList.clear();
                            isStockOutGoingAlreadyCalledForthisItem =false;

                            NewOrders_MenuItem_Fragment.cart_Item_List.clear();
                            NewOrders_MenuItem_Fragment.cartItem_hashmap.clear();

                            ispaymentMode_Clicked = false;
                            isOrderDetailsMethodCalled = false;

                            isPaymentDetailsMethodCalled = false;
                            isOrderTrackingDetailsMethodCalled = false;
                            new_to_pay_Amount = 0;
                            old_taxes_and_charges_Amount = 0;
                            old_total_Amount = 0;
                            createEmptyRowInListView("empty");
                            CallAdapter();
                            discountAmount = "0";
                            isDiscountApplied = false;
                            discount_Edit_widget.setText("0");
                            finaltoPayAmount = "0";
                            discount_rs_text_widget.setText(discountAmount);
                            OrderTypefromSpinner = "POS Order";
                            orderTypeSpinner.setSelection(0);
                            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                            mobileNo_Edit_widget.setText("");
                            isPrintedSecondTime = false;
                            useStoreNumberCheckBox.setChecked(false);
                            ispointsApplied_redeemClicked=false;
                            isProceedtoCheckoutinRedeemdialogClicked =false;
                            isRedeemDialogboxOpened=false;
                            isUpdateRedeemPointsMethodCalled=false;
                            isUpdateCouponTransactionMethodCalled=false;
                            isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                            totalAmounttopay=0;
                            finalamounttoPay=0;
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
                            //discountlayout visible
                            discountAmountLayout.setVisibility(View.GONE);
                            showProgressBar(false);

                        }
                    });
            return;
        }

        PendingIntent permissionIntent = PendingIntent.getBroadcast(
                mContext,
                0,
                new Intent(ACTION_USB_PERMISSION),
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S ? PendingIntent.FLAG_MUTABLE : 0
        );
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addCategory("Billing Screen");
        mContext.registerReceiver(usbReceiver_newItem, filter);
        usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);



    }
    private final BroadcastReceiver usbReceiver_newItem = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Set<String> category = intent.getCategories();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (mContext) {
                    UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            new AsyncUsbEscPosPrint(
                                    context, new AsyncEscPosPrint.OnPrintFinished() {
                                        @Override
                                        public void onError(AsyncEscPosPrinter asyncEscPosPrinter, int codeException) {
                                            Log.e("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : An error occurred !");
                                            try{
                                                mContext.unregisterReceiver(usbReceiver_newItem);
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

                                            StockBalanceChangedForThisItemList.clear();
                                            isStockOutGoingAlreadyCalledForthisItem =false;

                                            NewOrders_MenuItem_Fragment.cart_Item_List.clear();
                                            NewOrders_MenuItem_Fragment.cartItem_hashmap.clear();

                                            ispaymentMode_Clicked = false;
                                            isOrderDetailsMethodCalled = false;

                                            isPaymentDetailsMethodCalled = false;
                                            isOrderTrackingDetailsMethodCalled = false;
                                            new_to_pay_Amount = 0;
                                            old_taxes_and_charges_Amount = 0;
                                            old_total_Amount = 0;
                                            createEmptyRowInListView("empty");
                                            CallAdapter();
                                            discountAmount = "0";
                                            isDiscountApplied = false;
                                            discount_Edit_widget.setText("0");
                                            finaltoPayAmount = "0";
                                            discount_rs_text_widget.setText(discountAmount);
                                            OrderTypefromSpinner = "POS Order";
                                            orderTypeSpinner.setSelection(0);
                                            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                            mobileNo_Edit_widget.setText("");
                                            isPrintedSecondTime = false;

                                            useStoreNumberCheckBox.setChecked(false);
                                            ispointsApplied_redeemClicked=false;
                                            isProceedtoCheckoutinRedeemdialogClicked =false;
                                            isRedeemDialogboxOpened=false;
                                            isUpdateRedeemPointsMethodCalled=false;
                                            isUpdateCouponTransactionMethodCalled=false;
                                            isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                            totalAmounttopay=0;
                                            finalamounttoPay=0;
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
                                            //discountlayout visible
                                            discountAmountLayout.setVisibility(View.GONE);
                                            showProgressBar(false);
                                        }

                                        @Override
                                        public void onSuccess(AsyncEscPosPrinter asyncEscPosPrinter) {
                                            Log.i("Async.OnPrintFinished", "AsyncEscPosPrint.OnPrintFinished : Print is finished !");

                                            try{
                                                mContext.unregisterReceiver(usbReceiver_newItem);
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

                                            if (!isPrintedSecondTime) {
                                                //isPrintedSecondTime = true;
                                                //showProgressBar(true);

                                                HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap();
                                                List<String> cart_Item_List = new ArrayList<>();
                                                String orderplacedTime = "" ; String userMobile = "" ; String tokenno = "" ; String itemTotalwithoutGst = "" ; String taxAmount = "" ; String payableAmount = "" ; String orderid = "" ;  String payment_mode = "" ; String finalCouponDiscountAmount = "" ; String ordertype ="";
                                                try{
                                                    cartItem_hashmap = modal_usbPrinter.getCartItem_hashmap();
                                                    cart_Item_List = modal_usbPrinter.getCart_Item_List();
                                                    orderplacedTime = modal_usbPrinter.getOrderplacedTime();
                                                    userMobile = modal_usbPrinter.getUserMobile();
                                                    tokenno = modal_usbPrinter.getTokenno();
                                                    itemTotalwithoutGst = modal_usbPrinter.getItemTotalwithoutGst();
                                                    taxAmount = modal_usbPrinter.getTaxAmount();
                                                    payableAmount = modal_usbPrinter.getPayableAmount();
                                                    orderid = modal_usbPrinter.getOrderid();
                                                    payment_mode = modal_usbPrinter.getPayment_mode();
                                                    finalCouponDiscountAmount = modal_usbPrinter.getFinalCouponDiscountAmount();
                                                    ordertype = modal_usbPrinter.getOrdertype();


                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                                String finalOrderplacedTime = orderplacedTime;
                                                String finalUserMobile = userMobile;
                                                String finalTokenno = tokenno;
                                                String finalItemTotalwithoutGst = itemTotalwithoutGst;
                                                String finalTaxAmount = taxAmount;
                                                String finalPayableAmount = payableAmount;
                                                String finalOrderid = orderid;
                                                List<String> finalCart_Item_List = cart_Item_List;
                                                HashMap<String, Modal_NewOrderItems> finalCartItem_hashmap = cartItem_hashmap;
                                                String finalPayment_mode = payment_mode;
                                                String finalCouponDiscountAmount1 = finalCouponDiscountAmount;
                                                String finalOrdertype = ordertype;
                                                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.RePrint_Instruction,
                                                        R.string.Yes_Text, R.string.No_Text,
                                                        new TMCAlertDialogClass.AlertListener() {
                                                            @Override
                                                            public void onYes() {
                                                                isPrintedSecondTime = true;
                                                                PrintReciptForNewItemUsingUSBPrinter(finalOrderplacedTime, finalUserMobile, finalTokenno, finalItemTotalwithoutGst, finalTaxAmount, finalPayableAmount, finalOrderid, finalCart_Item_List, finalCartItem_hashmap, finalPayment_mode, finalCouponDiscountAmount1, finalOrdertype);


                                                            }

                                                            @Override
                                                            public void onNo() {
                                                                StockBalanceChangedForThisItemList.clear();
                                                                isStockOutGoingAlreadyCalledForthisItem =false;

                                                                NewOrders_MenuItem_Fragment.cart_Item_List.clear();
                                                                NewOrders_MenuItem_Fragment.cartItem_hashmap.clear();

                                                                ispaymentMode_Clicked = false;
                                                                isOrderDetailsMethodCalled = false;

                                                                isPaymentDetailsMethodCalled = false;
                                                                isOrderTrackingDetailsMethodCalled = false;
                                                                new_to_pay_Amount = 0;
                                                                old_taxes_and_charges_Amount = 0;
                                                                old_total_Amount = 0;
                                                                createEmptyRowInListView("empty");
                                                                CallAdapter();
                                                                discountAmount = "0";
                                                                isDiscountApplied = false;
                                                                discount_Edit_widget.setText("0");
                                                                finaltoPayAmount = "0";
                                                                discount_rs_text_widget.setText(discountAmount);
                                                                OrderTypefromSpinner = "POS Order";
                                                                orderTypeSpinner.setSelection(0);
                                                                total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                                                taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                                                total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                                                                mobileNo_Edit_widget.setText("");
                                                                isPrintedSecondTime = false;

                                                                useStoreNumberCheckBox.setChecked(false);
                                                                ispointsApplied_redeemClicked=false;
                                                                isProceedtoCheckoutinRedeemdialogClicked =false;
                                                                isRedeemDialogboxOpened=false;
                                                                isUpdateRedeemPointsMethodCalled=false;
                                                                isUpdateCouponTransactionMethodCalled=false;
                                                                isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                                                totalAmounttopay=0;
                                                                finalamounttoPay=0;
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
                                                                //discountlayout visible
                                                                discountAmountLayout.setVisibility(View.GONE);
                                                                showProgressBar(false);
                                                            }
                                                        });


                                            }
                                            else {
                                                autoComplete_customerNameText_widget.setText("");
                                                autoComplete_customerNameText_widget.dismissDropDown();
                                                cart_Item_List.clear();
                                                StockBalanceChangedForThisItemList.clear();
                                                StockBalanceChangedForThisItemList.clear();
                                                isStockOutGoingAlreadyCalledForthisItem =false;

                                                NewOrders_MenuItem_Fragment.cart_Item_List.clear();
                                                NewOrders_MenuItem_Fragment.cartItem_hashmap.clear();

                                                cartItem_hashmap.clear();

                                                new_to_pay_Amount = 0;
                                                old_taxes_and_charges_Amount = 0;
                                                old_total_Amount = 0;
                                                createEmptyRowInListView("empty");
                                                CallAdapter();
                                                discountAmount = "0";
                                                isDiscountApplied = false;
                                                discount_Edit_widget.setText("0");
                                                finaltoPayAmount = "0";
                                                discount_rs_text_widget.setText(discountAmount);
                                                OrderTypefromSpinner = "POS Order";
                                                orderTypeSpinner.setSelection(0);
                                                total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                                                taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                                                total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));
                                                useStoreNumberCheckBox.setChecked(false);




                                                mobileNo_Edit_widget.setText("");
                                                isPrintedSecondTime = false;
                                                ispaymentMode_Clicked = false;
                                                isOrderDetailsMethodCalled = false;

                                                isPaymentDetailsMethodCalled = false;
                                                isOrderTrackingDetailsMethodCalled = false;
                                                ispointsApplied_redeemClicked=false;
                                                isProceedtoCheckoutinRedeemdialogClicked =false;
                                                isRedeemDialogboxOpened=false;
                                                isUpdateRedeemPointsMethodCalled=false;
                                                isUpdateCouponTransactionMethodCalled=false;
                                                isUpdateRedeemPointsWithoutKeyMethodCalled=false;
                                                totalAmounttopay=0;
                                                finalamounttoPay=0;

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
                                                //discountlayout visible
                                                discountAmountLayout.setVisibility(View.GONE);
                                                showProgressBar(false);

                                            }

                                        }
                                    }
                            )
                                    .execute(getAsyncEscPosPrinterNewItem(new UsbConnection(usbManager, usbDevice)));
                        }
                    }
                }
            }
        }
    };



    @SuppressLint("SimpleDateFormat")
    public AsyncEscPosPrinter getAsyncEscPosPrinterNewItem(DeviceConnection printerConnection) {


        SimpleDateFormat format = new SimpleDateFormat("'on' yyyy-MM-dd 'at' HH:mm:ss");
        HashMap<String, Modal_NewOrderItems> cartItem_hashmap = new HashMap();
        List<String> cart_Item_List = new ArrayList<>();
        String orderplacedTime = "";
        String userMobile = "";
        String tokenno = "";
        String itemTotalwithoutGst = "";
        String taxAmount = "";
        String payableAmount = "";
        String orderid = "";
        String payment_mode = "";
        String finalCouponDiscountAmount = "";
        String ordertype = "";
        try {
            cartItem_hashmap = modal_usbPrinter.getCartItem_hashmap();
            cart_Item_List = modal_usbPrinter.getCart_Item_List();
            orderplacedTime = modal_usbPrinter.getOrderplacedTime();
            userMobile = modal_usbPrinter.getUserMobile();
            tokenno = modal_usbPrinter.getTokenno();
            itemTotalwithoutGst = modal_usbPrinter.getItemTotalwithoutGst();
            taxAmount = modal_usbPrinter.getTaxAmount();
            payableAmount = modal_usbPrinter.getPayableAmount();
            orderid = modal_usbPrinter.getOrderid();
            payment_mode = modal_usbPrinter.getPayment_mode();
            finalCouponDiscountAmount = modal_usbPrinter.getFinalCouponDiscountAmount();
            ordertype = modal_usbPrinter.getOrdertype();


        } catch (Exception e) {
            e.printStackTrace();
        }

        String text_to_Print = "";
        String Title = "The Meat Chop";

        String GSTIN = "GSTIN :33AAJCC0055D1Z9";

        if((vendorKey.equals("vendor_4")) ||  (vendorKey.equals("wholesalesvendor_1"))) {


            text_to_Print = "[c]<b><font size='big'>MK Proteins</b>\n\n";
            text_to_Print = text_to_Print + "[c]<b><font size='normal'>Powered By The Meat Chop</b>\n\n";

        }
        else {
            text_to_Print = "[c]<b><font size='big'>The Meat Chop</b>\n\n";

        }
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Fresh Meat and Seafood \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine1 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + StoreAddressLine2 + "\n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Postal Code :" + StoreAddressLine3 + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>Contact No :" + StoreLanLine + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + GSTIN + " \n"+ " \n";

        text_to_Print = text_to_Print + "[c]  <font size='normal'>" + orderplacedTime + " \n";
        text_to_Print = text_to_Print + "[c]  <font size='normal'># " + orderid + " \n";
        text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";
        text_to_Print = text_to_Print + "[L]  ITEMNAME * QTY " + " \n";
        //text_to_Print = text_to_Print + "[L] RATE                                  SUBTOTAL" + " \n";
        text_to_Print = text_to_Print+"[L]  RATE"+"[R]      "+"  SUBTOTAL" +" \n";

        text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";


        if (cart_Item_List.size() == cartItem_hashmap.size()) {

            double oldSavedAmount = 0;
            String CouponDiscount = "0";
            String Gstt = " ", subtotal = " ", quantity = " ", price = " ", weight = " ";

            for (int i = 0; i < cart_Item_List.size(); i++) {
                String itemDetails = "";
                double savedAmount;
                String itemUniqueCode = cart_Item_List.get(i);
                Modal_NewOrderItems modal_newOrderItems = cartItem_hashmap.get(itemUniqueCode);

                String itemName = "";
                try {
                    itemName = String.valueOf(Objects.requireNonNull(modal_newOrderItems).getItemname());
                }
                catch (Exception e) {
                    String itemUniqueCode1 = cart_Item_List.get(i);
                    Modal_NewOrderItems modal_newOrderItems1 = cartItem_hashmap.get(itemUniqueCode1);
                    try {
                        itemName = String.valueOf(Objects.requireNonNull(modal_newOrderItems1).getItemname());
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }

                int indexofbraces = itemName.indexOf("(");
                if (indexofbraces >= 0) {
                    itemName = itemName.substring(0, indexofbraces);

                }
                if (itemName.length() > 21) {
                    itemName = itemName.substring(0, 21);
                    itemName = itemName + "...";
                }
                try {
                    price = String.valueOf(modal_newOrderItems.getItemFinalPrice());
                    if (price.equals("null")) {
                        price = "  ";
                    }
                } catch (Exception e) {
                    price = "0";
                    e.printStackTrace();
                }

                try {
                    weight = modal_newOrderItems.getItemFinalWeight().toString();
                } catch (Exception e) {
                    weight = "0";
                    e.printStackTrace();
                }

                try {
                    Gstt = modal_newOrderItems.getGstAmount();

                } catch (Exception e) {
                    Gstt = "0";
                    e.printStackTrace();
                }


                try {
                    savedAmount = Double.parseDouble(modal_newOrderItems.getSavedAmount());
                } catch (Exception e) {
                    savedAmount = 0;
                    e.printStackTrace();
                }


                try {
                    oldSavedAmount = savedAmount + oldSavedAmount;
                } catch (Exception e) {
                    oldSavedAmount = 0;
                    e.printStackTrace();
                }


                try {
                    subtotal = modal_newOrderItems.getSubTotal_perItem();
                } catch (Exception e) {
                    subtotal = "0";
                    e.printStackTrace();
                }

                try {
                    quantity = modal_newOrderItems.getQuantity();
                } catch (Exception e) {
                    quantity = "0";
                    e.printStackTrace();
                }


                try {
                    itemName = itemName + "  *  " + weight + " ( " + quantity + " ) ";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                text_to_Print = text_to_Print + "[L]  <b><font size='normal'>" + itemName + " </b>\n";


           /*     if (price.length() == 4) {
                    //14spaces
                    price = price + "                ";
                }
                if (price.length() == 5) {
                    //13spaces
                    price = price + "               ";
                }
                if (price.length() == 6) {
                    //12spaces
                    price = price + "              ";
                }
                if (price.length() == 7) {
                    //11spaces
                    price = price + "             ";
                }
                if (price.length() == 8) {
                    //10spaces
                    price = price + "            ";
                }
                if (price.length() == 9) {
                    //9spaces
                    price = price + "           ";
                }
                if (price.length() == 10) {
                    //8spaces
                    price = price + "          ";
                }
                if (price.length() == 11) {
                    //7spaces
                    price = price + "         ";
                }
                if (price.length() == 12) {
                    //6spaces
                    price = price + "        ";
                }
                if (price.length() == 13) {
                    //5spaces
                    price = price + "       ";
                }
                if (price.length() == 14) {
                    //4spaces
                    price = price + "      ";
                }
                if (price.length() == 15) {
                    //3spaces
                    price = price + "     ";
                }
                if (price.length() == 16) {
                    //2spaces
                    price = price + "    ";
                }
                if (price.length() == 17) {
                    //1spaces
                    price = price + "   ";
                }
                if (price.length() == 18) {
                    //1spaces
                    price = price + " ";
                }


                if (Gstt.length() == 7) {
                    //1spaces
                    Gstt = Gstt + " ";
                }
                if (Gstt.length() == 8) {
                    //0space
                    Gstt = Gstt + "";
                }
                if (Gstt.length() == 9) {
                    //no space
                    Gstt = Gstt;
                }
                if (subtotal.length() == 4) {
                    //5spaces
                    subtotal = "       " + subtotal;
                }
                if (subtotal.length() == 5) {
                    //6spaces
                    subtotal = "       " + subtotal;
                }
                if (subtotal.length() == 6) {
                    //8spaces
                    subtotal = "         " + subtotal;
                }
                if (subtotal.length() == 7) {
                    //7spaces
                    subtotal = "        " + subtotal;
                }
                if (subtotal.length() == 8) {
                    //6spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 9) {
                    //5spaces
                    subtotal = "      " + subtotal;
                }
                if (subtotal.length() == 10) {
                    //4spaces
                    subtotal = "     " + subtotal;
                }
                if (subtotal.length() == 11) {
                    //3spaces
                    subtotal = "    " + subtotal;
                }
                if (subtotal.length() == 12) {
                    //2spaces
                    subtotal = "   " + subtotal;
                }
                if (subtotal.length() == 13) {
                    //1spaces
                    subtotal = "  " + subtotal;
                }
                if (subtotal.length() == 14) {
                    //no space
                    subtotal = " " + subtotal;
                }


            */
                itemDetails = price + Gstt + subtotal;
                text_to_Print = text_to_Print + "[L]  <font size='normal'>" + price  +"[R]        "+ subtotal +" \n";

               // text_to_Print = text_to_Print + "[L] <font size='normal'>" + itemDetails + " \n";
                text_to_Print = text_to_Print+"[L]<font size='normal'>                                                "+" \n";

            }

            text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";


            finaltoPayAmount =  finaltoPayAmount;
            taxAmount = "Rs. " + taxAmount;
            itemTotalwithoutGst = "Rs. " + itemTotalwithoutGst;
/*
            if (itemTotalwithoutGst.length() == 7) {
                //10spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "            ";
            }
            if (itemTotalwithoutGst.length() == 8) {
                //9spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "           ";
            }
            if (itemTotalwithoutGst.length() == 9) {
                //8spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "          ";
            }
            if (itemTotalwithoutGst.length() == 10) {
                //7spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "         ";
            }
            if (itemTotalwithoutGst.length() == 11) {
                //6spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "        ";
            }
            if (itemTotalwithoutGst.length() == 12) {
                //5spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "       ";
            }
            if (itemTotalwithoutGst.length() == 13) {
                //4spaces
                itemTotalwithoutGst = itemTotalwithoutGst + "      ";
            }

            if (taxAmount.length() == 7) {
                //1spaces
                taxAmount = taxAmount + "";
            }
            if (taxAmount.length() == 8) {
                //0space
                taxAmount = taxAmount + "";
            }
            if (taxAmount.length() == 9) {
                //no space
                taxAmount = taxAmount;
            }

            if (finaltoPayAmount.length() == 6) {
                //8spaces
                finaltoPayAmount = "         " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 7) {
                //7spaces
                finaltoPayAmount = "        " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 8) {
                //6spaces
                finaltoPayAmount = "       " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 9) {
                //5spaces
                finaltoPayAmount = "      " + finaltoPayAmount;
            }
            if (finaltoPayAmount.length() == 10) {
                //4spaces
                finaltoPayAmount = "     " + finaltoPayAmount;
            }

 */

            text_to_Print = text_to_Print + "[L]  <font size='normal'>" + itemTotalwithoutGst  +"[R]        "+ "Rs. "+finaltoPayAmount +" \n";

          //  text_to_Print = text_to_Print + "[L] " + itemTotalwithoutGst + taxAmount + finaltoPayAmount + " \n";


            text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + "\n";

            CouponDiscount = finalCouponDiscountAmount;
            if (!CouponDiscount.equals("0")) {
                CouponDiscount = "Rs. " + CouponDiscount + ".00";

                if ((!CouponDiscount.equals("Rs.0.0")) && (!CouponDiscount.equals("Rs.0")) && (!CouponDiscount.equals("Rs.0.00")) && (CouponDiscount != (null)) && (!CouponDiscount.equals("")) && (!CouponDiscount.equals("Rs. .00")) && (!CouponDiscount.equals("Rs..00"))) {
/*
                    if (CouponDiscount.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        CouponDiscount = "Discount Amount                     " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        CouponDiscount = "Discount Amount                  " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        CouponDiscount = "Discount Amount                  " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        CouponDiscount = "Discount Amount                 " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        CouponDiscount = " Discount Amount                " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount               " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        CouponDiscount = " Discount Amount              " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount              " + CouponDiscount;
                    }
                    if (CouponDiscount.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount             " + CouponDiscount;
                    }

                    if (CouponDiscount.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        CouponDiscount = "Discount Amount             " + CouponDiscount;

                    }


 */
                   // text_to_Print = text_to_Print + "[L]" + CouponDiscount + " \n";
                    text_to_Print = text_to_Print+"[L]  Coupon Discount "+"[R]      " +CouponDiscount+" \n";

                    text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";

                }
            }

            String redeemPoints_String_print = "";
            if (!redeemPoints_String.equals("0")) {
                redeemPoints_String_print = "Rs. " + redeemPoints_String + ".00";

                if ((!redeemPoints_String_print.equals("Rs.0.0")) && (!redeemPoints_String_print.equals("Rs.0")) && (!redeemPoints_String_print.equals("Rs.0.00")) && (redeemPoints_String_print != (null)) && (!redeemPoints_String_print.equals("")) && (!redeemPoints_String_print.equals("Rs. .00")) && (!redeemPoints_String_print.equals("Rs..00"))) {

                  /*  if (redeemPoints_String_print.length() == 4) {
                        //20spaces
                        //NEW TOTAL =4
                        redeemPoints_String_print = "Points Redeemed                     " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 5) {
                        //21spaces
                        //NEW TOTAL =5
                        redeemPoints_String_print = "Points Redeemed                   " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 6) {
                        //20spaces
                        //NEW TOTAL =6
                        redeemPoints_String_print = "Points Redeemed                  " + redeemPoints_String_print;
                    }

                    if (redeemPoints_String_print.length() == 7) {
                        //19spaces
                        //NEW TOTAL =7
                        redeemPoints_String_print = "Points Redeemed                 " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 8) {
                        //18spaces
                        //NEW TOTAL =8
                        redeemPoints_String_print = "Points Redeemed                " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 9) {
                        //17spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed                " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 10) {
                        //16spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed              " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 11) {
                        //15spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = " Points Redeemed              " + redeemPoints_String_print;
                    }
                    if (redeemPoints_String_print.length() == 12) {
                        //14spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed             " + redeemPoints_String_print;
                    }

                    if (redeemPoints_String_print.length() == 13) {
                        //13spaces
                        //NEW TOTAL =9
                        redeemPoints_String_print = "Points Redeemed             " + redeemPoints_String_print;

                    }

                   */
                    text_to_Print = text_to_Print+"[L]  Points Redeemed "+"[R]      " +redeemPoints_String_print+" \n";

                   // text_to_Print = text_to_Print + "[L]" + redeemPoints_String_print + " \n";
                    text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + " \n";

                }
            }


            /*if (payableAmount.length() > 6) {

               if (payableAmount.length() == 7) {
                    //24spaces
                    //NEW TOTAL =9
                    payableAmount = " NET TOTAL                         Rs. " + payableAmount;
                }
                if (payableAmount.length() == 8) {
                    //23spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                         Rs. " + payableAmount;
                }
                if (payableAmount.length() == 9) {
                    //22spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                       Rs. " + payableAmount;
                }
                if (payableAmount.length() == 10) {
                    //21spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                      Rs. " + payableAmount;
                }
                if (payableAmount.length() == 11) {
                    //20spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                     Rs. " + payableAmount;
                }
                if (payableAmount.length() == 12) {
                    //19spaces
                    //NEW TOTAL =9
                    payableAmount = "  NET TOTAL                    Rs. " + payableAmount;
                }


            } else {
                payableAmount = " NET TOTAL                      Rs.  " + payableAmount;

            }

             */

            text_to_Print = text_to_Print+"[L]  NET TOTAL  "+"[R]       Rs. " +payableAmount+" \n";

           // text_to_Print = text_to_Print + "[L]" + payableAmount + " \n";
            text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + "\n";

            try {
                if (payment_mode.toUpperCase().equals(Constants.CASH_ON_DELIVERY)) {
                    if ((!amountRecieved_String.equals("null")) && (!balanceAmount_String.equals("null"))) {
                        text_to_Print = text_to_Print + "[L]  Amount Given by Customer : " + amountRecieved_String + " Rs " + " \n";

                        text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + "\n";

                        text_to_Print = text_to_Print + "[L]  Balance Amount given : " + balanceAmount_String + " Rs " + "\n";

                        text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + "\n";

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            text_to_Print = text_to_Print + "[L]  Earned Rewards : " + String.valueOf((int) (totalredeempointsusergetfromorder)) + " \n";

            text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + "\n";
            if(payment_mode.toString().toUpperCase().equals(Constants.CREDIT)){
                text_to_Print = text_to_Print + "[L]  Old Amount need to Pay :  Rs." + String.valueOf(Math.round(totalamountUserHaveAsCredit)) + " \n";

                //text_to_Print = text_to_Print + "[L]  New Amount need to be Paid = (Old amount + Current Bill Amount )  \n";
                    String payableamountPrint = "";
                    try{
                        payableamountPrint = modal_usbPrinter.getPayableAmount().toString();
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


                text_to_Print = text_to_Print + "[L]  New Amount need to Pay :  Rs." +  String.valueOf(Math.round(totalamountUserHaveAsCredit+payableamountdoublePrint))+ " \n";
                text_to_Print = text_to_Print + "[L]  ----------------------------------------------" + "\n";

            }
            text_to_Print = text_to_Print + "[L]  Order Type : " + String.valueOf(ordertype) + " \n";


            text_to_Print = text_to_Print + "[L]  Payment Mode : " + String.valueOf(payment_mode) + " \n";


            text_to_Print = text_to_Print + "[L]  Mobile No : " + String.valueOf(userMobile) + " \n" + " \n";


            text_to_Print = text_to_Print + "[C]    Thank you for choosing us !!!" + " \n";

        } else {
            Toast.makeText(mContext, "Size of Cart is not matched", Toast.LENGTH_SHORT).show();
        }


        AsyncEscPosPrinter printer = new AsyncEscPosPrinter(printerConnection, 203, 48f, 44);
        return printer.addTextToPrint(text_to_Print);


    }







}
















/*
 public class StartTwice extends Thread {
        String userMobile;  String tokenno;
         String itemTotalwithoutGst; String taxAmount;
          String payableAmount; String orderid;
          List<String> cart_item_list;  HashMap<String, Modal_NewOrderItems> cartItem_hashmap;
           String payment_mode;
        public StartTwice(String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String payableAmount, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode) {
        this.userMobile  =userMobile;
        this.tokenno = tokenno;
        this.itemTotalwithoutGst = itemTotalwithoutGst;
        this.taxAmount = taxAmount;
        this.payableAmount = payableAmount;
        this.orderid = orderid;
        this.cart_item_list = cart_item_list;
            this.payment_mode = payment_mode;
            this.cartItem_hashmap = cartItem_hashmap;



        }

        public void run() {
            System.out.println("running...");
             printRecipt(userMobile, tokenno, itemTotalwithoutGst, taxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, payment_mode);

        }

        public  void main() {

            StartTwice start1 = new StartTwice( userMobile,  tokenno,  itemTotalwithoutGst,  taxAmount,  payableAmount,  orderid,  cart_item_list,  cartItem_hashmap,  payment_mode);
            System.out.println("Thread id: " + start1.getId());
            start1.start();

             start1 = new StartTwice( userMobile,  tokenno,  itemTotalwithoutGst,  taxAmount,  payableAmount,  orderid,  cart_item_list,  cartItem_hashmap,  payment_mode);
            System.out.println("Thread id: " + start1.getId());
            start1.start();
        }



    }


 */

































































































//add amount for bill details
/*
        for(int i = 0; i<cart_Item_List.size();i++) {

            Modal_NewOrderItems modal_newOrderItems = cart_Item_List.get(i);
            String pricetype_of_pos = String.valueOf(modal_newOrderItems.getPricetypeforpos());

            if(pricetype_of_pos.equals("tmcprice")) {
                int new_total_amountfromArray = Integer.parseInt(modal_newOrderItems.getPricePerItem());
                //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);

                new_total_amount = new_total_amountfromArray;
                old_total_Amount = old_total_Amount + new_total_amount;
                //Log.i(TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
                //Log.i(TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);


                int taxes_and_chargesfromArray = Integer.parseInt(modal_newOrderItems.getGstpercentage());
                //Log.i(TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);

                taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);


                modal_newOrderItems.setGstAmount(String.valueOf(taxes_and_chargesfromArray));
                //Log.i(TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
                //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
                //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
                new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
                int subTotal_perItem=new_total_amount+new_taxes_and_charges_Amount;
                modal_newOrderItems.setSubTotal_perItem(String.valueOf(subTotal_perItem));
                old_taxes_and_charges_Amount = old_taxes_and_charges_Amount + new_taxes_and_charges_Amount;
                //Log.i(TAG, "add_amount_ForBillDetails new_taxes_and_charges_Amount" + new_taxes_and_charges_Amount);
                //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);


            }
            if (pricetype_of_pos.equals("tmcpriceperkg")) {
                int new_total_amountfromArray = Integer.parseInt(modal_newOrderItems.getPricePerItem());
                //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromArray" + new_total_amountfromArray);

                new_total_amount = new_total_amountfromArray;
                old_total_Amount = old_total_Amount + new_total_amount;
                //Log.i(TAG, "add_amount_ForBillDetails new_total_amount" + new_total_amount);
                //Log.i(TAG, "add_amount_ForBillDetails old_total_Amount" + old_total_Amount);


                int taxes_and_chargesfromArray = Integer.parseInt(modal_newOrderItems.getGstpercentage());
                //Log.i(TAG, "add_amount_ForBillDetails taxes_and_chargesfromadapter" + taxes_and_chargesfromArray);

                taxes_and_chargesfromArray = ((taxes_and_chargesfromArray * new_total_amountfromArray) / 100);
                //Log.i(TAG, "add_amount_ForBillDetails taxes_and_charges " + taxes_and_chargesfromArray);
                //Log.i(TAG, "add_amount_ForBillDetails new_total_amountfromadapter" + new_total_amountfromArray);
                //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);
                new_taxes_and_charges_Amount = taxes_and_chargesfromArray;
                int subTotal_perItem=new_total_amount+new_taxes_and_charges_Amount;
                modal_newOrderItems.setSubTotal_perItem(String.valueOf(subTotal_perItem));

                old_taxes_and_charges_Amount = old_taxes_and_charges_Amount + new_taxes_and_charges_Amount;
                //Log.i(TAG, "add_amount_ForBillDetails new_taxes_and_charges_Amount" + new_taxes_and_charges_Amount);
                //Log.i(TAG, "add_amount_ForBillDetails old_taxes_and_charges_Amount" + old_taxes_and_charges_Amount);


            }



            modal_newOrderItems.setTotalGstAmount(String.valueOf(old_taxes_and_charges_Amount));

            modal_newOrderItems.setTotal_pricePerItem(String.valueOf(old_total_Amount));


                    new_to_pay_Amount = old_total_Amount + old_taxes_and_charges_Amount;
            modal_newOrderItems.setTotal_of_subTotal_perItem(String.valueOf(new_to_pay_Amount));
            total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
            taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
            total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));
        }
        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;

 */








/*
    public void getMenuItemUsingBarCode(String barcode,int position) {


            //Log.e(TAG, "Got barcode isBarcodeEntered getMenuItemUsingBarCode" + isdataFetched);
            isdataFetched = true;

            if (barcode.length() == 13) {
                //Log.e(TAG, "Got barcode isBarcodeEntered getMenuItemUsingBarCode" + isdataFetched);
                String itemWeight;
                //Log.e(TAG, "1 barcode " + barcode);
                //Log.e(TAG, "Got barcode isBarcodeEntered getMenuItemUsingBarCode" + isdataFetched);

                for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {

                    Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                    if ((String.valueOf(modal_newOrderItems.getItemuniquecode())).equals(barcode)) {


                        Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                        newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                        newItem_newOrdersPojoClass.grossweight = modal_newOrderItems.getGrossweight();
                        newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                        newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                        newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();

                        newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                        newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                        newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                        newItem_newOrdersPojoClass.itemuniquecode = modal_newOrderItems.getItemuniquecode();
                        newItem_newOrdersPojoClass.pricePerItem = modal_newOrderItems.getTmcprice();
                        newItem_newOrdersPojoClass.itemPrice_quantityBased = modal_newOrderItems.getTmcprice();
                        newItem_newOrdersPojoClass.quantity = "1";
                        newItem_newOrdersPojoClass.subTotal_perItem = "";
                        newItem_newOrdersPojoClass.total_of_subTotal_perItem = "";
                        newItem_newOrdersPojoClass.totalGstAmount = "";
                        newItem_newOrdersPojoClass.itemFinalPrice = "";

                        if (modal_newOrderItems.getGrossweight().equals("") && modal_newOrderItems.getNetweight().equals("")) {
                            //Log.e(Constants.TAG, "getPortionsize " + (String.format(" %s", modal_newOrderItems.getPortionsize())));
                            newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getPortionsize());

                            //     itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getPortionsize()));
                            add_amount_ForBillDetails();
                            itemWeight = String.valueOf(modal_newOrderItems.getPortionsize());
                        } else if (modal_newOrderItems.getNetweight().equals("")) {

                            //Log.e(Constants.TAG, "getGrossweight " + (String.format(" %s", modal_newOrderItems.getGrossweight())));

                            newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());
                            //   itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getGrossweight()));
                            add_amount_ForBillDetails();
                            itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());


                        } else if (modal_newOrderItems.getGrossweight().equals("")) {
                            //Log.e(Constants.TAG, "getNetweight " + (String.format(" %s", modal_newOrderItems.getNetweight())));
                            newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getNetweight());

                            //     itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getNetweight()));
                            add_amount_ForBillDetails();
                            itemWeight = String.valueOf(modal_newOrderItems.getNetweight());


                        } else {
                            //Log.e(Constants.TAG, "getGrossweight " + (String.format(" %s", modal_newOrderItems.getGrossweight())));
                            //   itemWeightTextview_widget.setText(String.valueOf(modal_newOrderItems.getGrossweight()));

                            newItem_newOrdersPojoClass.itemFinalWeight = (modal_newOrderItems.getGrossweight());
                            add_amount_ForBillDetails();
                            itemWeight = String.valueOf(modal_newOrderItems.getGrossweight());


                        }

                        addItemIntheCart(newItem_newOrdersPojoClass, itemWeight, barcode);


                    }


                }
            }
            if (barcode.length() == 14) {
                int item_total;
                String itemuniquecode = barcode.substring(0, 9);
                String itemWeight = barcode.substring(9, 14);
                //Log.e(TAG, "1 barcode uniquecode" + itemuniquecode);
                //Log.e(TAG, "1 barcode itemweight" + itemWeight);

                for (int i = 0; i < NewOrders_MenuItem_Fragment.completemenuItem.size(); i++) {

                    Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.completemenuItem.get(i);

                    if (String.valueOf(modal_newOrderItems.getItemuniquecode()).equals(itemuniquecode)) {

                        Modal_NewOrderItems newItem_newOrdersPojoClass = new Modal_NewOrderItems();
                        newItem_newOrdersPojoClass.itemname = modal_newOrderItems.getItemname();
                        newItem_newOrdersPojoClass.tmcpriceperkg = modal_newOrderItems.getTmcpriceperkg();
                        newItem_newOrdersPojoClass.grossweight = modal_newOrderItems.getGrossweight();
                        newItem_newOrdersPojoClass.netweight = modal_newOrderItems.getNetweight();
                        newItem_newOrdersPojoClass.tmcprice = modal_newOrderItems.getTmcprice();
                        newItem_newOrdersPojoClass.gstpercentage = modal_newOrderItems.getGstpercentage();
                        newItem_newOrdersPojoClass.portionsize = modal_newOrderItems.getPortionsize();
                        newItem_newOrdersPojoClass.pricetypeforpos = modal_newOrderItems.getPricetypeforpos();
                        newItem_newOrdersPojoClass.itemuniquecode = (modal_newOrderItems.getItemuniquecode());
                        newItem_newOrdersPojoClass.itemPrice_quantityBased = modal_newOrderItems.getTmcpriceperkg();

                        newItem_newOrdersPojoClass.quantity = "1";
                        newItem_newOrdersPojoClass.subTotal_perItem = "";
                        newItem_newOrdersPojoClass.total_of_subTotal_perItem = "";
                        newItem_newOrdersPojoClass.totalGstAmount = "";
                        newItem_newOrdersPojoClass.itemFinalPrice = "";


/*
                    if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcpriceperkg")) {
                        int priceperKg=Integer.parseInt(modal_newOrderItems.getTmcpriceperkg());
                        int weight = Integer.parseInt(itemWeight);
                        if (weight < 1000) {
                            item_total = (priceperKg * weight);
                            //Log.e("TAG", "adapter 9 item_total price_per_kg" + priceperKg);

                            //Log.e("TAG", "adapter 9 item_total weight" + weight);

                            //Log.e("TAG", "adapter 9 item_total " + priceperKg * weight);

                            item_total = item_total / 1000;
                            //Log.e("TAG", "adapter 9 item_total " + item_total);

                            //Log.e("TAg", "weight2" + weight);
                            cart_Item_List.get(position).setPricePerItem(String.valueOf(item_total));
                            cart_Item_List.get(position).setItemFinalWeight(String.valueOf(weight));

                           // itemPrice_Widget.setText(String.valueOf(item_total));
                            add_amount_ForBillDetails();

                            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                        }

                        if (weight == 1000) {

                         //   itemPrice_Widget.setText(String.valueOf(priceperKg));
                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                            //Log.e("TAG", "adapter 10" + itemInCart.get(getAdapterPosition()).getItemname());
                            //Log.e("TAG", "adapter 10" + itemInCart.get(getAdapterPosition()).getItemFinalWeight());
                            //Log.e("TAG", "adapter 10" + itemInCart.get(getAdapterPosition()).getItemFinalPrice());
                            //Log.e("TAG", "adapter 10" + itemInCart.get(getAdapterPosition()).getTmcpriceperkg());

                            itemInCart.get(getAdapterPosition()).setPricePerItem(String.valueOf(priceperKg));
                            itemInCart.get(getAdapterPosition()).setItemFinalWeight(String.valueOf(weight));

                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                            //Log.e("TAG", "adapter 10.1" + itemInCart.get(getAdapterPosition()).getItemname());
                            //Log.e("TAG", "adapter 10.1" + itemInCart.get(getAdapterPosition()).getItemFinalWeight());
                            //Log.e("TAG", "adapter 10.1" + itemInCart.get(getAdapterPosition()).getItemFinalPrice());
                            //Log.e("TAG", "adapter 10.1" + itemInCart.get(getAdapterPosition()).getTmcpriceperkg());


                            newOrders_menuItem_fragment.add_amount_ForBillDetails();
                            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                        }

                        if (weight > 1000) {
                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                            //Log.e("TAG", "adapter 11" + itemInCart.get(getAdapterPosition()).getItemname());
                            //Log.e("TAG", "adapter 11" + itemInCart.get(getAdapterPosition()).getItemFinalWeight());
                            //Log.e("TAG", "adapter 11" + itemInCart.get(getAdapterPosition()).getItemFinalPrice());
                            //Log.e("TAG", "adapter 11" + itemInCart.get(getAdapterPosition()).getTmcpriceperkg());

                            //Log.e("TAg", "weight3" + weight);

                            int itemquantity = weight - 1000;
                            //Log.e("TAg", "weight itemquantity" + itemquantity);

                            item_total = (price_per_kg * itemquantity) / 1000;


                            //Log.e("TAg", "weight item_total" + item_total);

                            itemInCart.get(getAdapterPosition()).setPricePerItem(String.valueOf(priceperKg + item_total));
                            itemInCart.get(getAdapterPosition()).setItemFinalWeight(String.valueOf(weight));
                            //Log.e("TAG", "Cart adapter price_per_kg +" + priceperKg);
                            //Log.e("TAG", "adapter 11.1" + itemInCart.get(getAdapterPosition()).getItemname());
                            //Log.e("TAG", "adapter 11.1" + itemInCart.get(getAdapterPosition()).getItemFinalWeight());
                            //Log.e("TAG", "adapter 11.1" + itemInCart.get(getAdapterPosition()).getItemFinalPrice());
                            //Log.e("TAG", "adapter 11.1" + itemInCart.get(getAdapterPosition()).getTmcpriceperkg());

                            itemPrice_Widget.setText(String.valueOf(priceperKg + item_total));
                            //Log.e("TAg", "weight item_total+price" + item_total + priceperKg);
                            newOrders_menuItem_fragment.add_amount_ForBillDetails();
                            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

                        }



                        // newItem_newOrdersPojoClass.pricePerItem = modal_newOrderItems.getTmcpriceperkg();
                        //Log.e("TAG", "adapter 12" + itemInCart.get(getAdapterPosition()).getItemname());
                        //Log.e("TAG", "adapter 12" + itemInCart.get(getAdapterPosition()).getItemFinalWeight());
                        //Log.e("TAG", "adapter 12" + itemInCart.get(getAdapterPosition()).getPricePerItem());
                        //Log.e("TAG", "adapter 12" + itemInCart.get(getAdapterPosition()).getTmcpriceperkg());

                    }



                        if (String.valueOf(modal_newOrderItems.getPricetypeforpos()).equals("tmcprice")) {
                            newItem_newOrdersPojoClass.pricePerItem = modal_newOrderItems.getTmcprice();

                        }

                        //Log.e(TAG, "Got barcode getMenuItemUsingBarCode" + isdataFetched);

                        newItem_newOrdersPojoClass.itemFinalWeight = itemWeight;
                        addItemIntheCart(newItem_newOrdersPojoClass, itemWeight, itemuniquecode);


                    }


                }

            }

    }


    private void addItemIntheCart(Modal_NewOrderItems newItem_newOrdersPojoClass, String itemWeight,String itemUniquecode) {
      isdataFetched=true;
        String old_ItemUniqueCode = "";
        int repeatedItemIndex=-1;
        if(cart_Item_List.size()>1){
            for (int i =0; i<cart_Item_List.size();i++) {
                Modal_NewOrderItems modal_newOrderItems = cart_Item_List.get(i);

                 old_ItemUniqueCode = modal_newOrderItems.getItemuniquecode().toString();
                if(old_ItemUniqueCode.equals(itemUniquecode)){
               /* //Log.e(TAG, "newItem_uniqueCode  "+old_ItemUniqueCode);
                //Log.e(TAG, "itemUniquecode "+itemUniquecode);
                quantity  = Integer.parseInt(modal_newOrderItems.getQuantity());
                quantity=quantity+1;

                modal_newOrderItems.setQuantity(String.valueOf(quantity));
                adapter_cartItem_recyclerview.notifyDataSetChanged();



                    repeatedItemIndex=i;
                    deleteRepeatedIndex();
                    break;

                }
            else{
                addItemsNormallyIntheCart(newItem_newOrdersPojoClass,itemWeight);
                    break;

                }



        }
            if(repeatedItemIndex!=-1){
                int quantity =0;
                int itemPrice =0;
                Modal_NewOrderItems modal_newOrderItems = cart_Item_List.get(repeatedItemIndex);

                quantity  = Integer.parseInt(modal_newOrderItems.getQuantity());
                quantity=quantity+1;
                modal_newOrderItems.setQuantity(String.valueOf(quantity));
                itemPrice=Integer.parseInt(newItem_newOrdersPojoClass.getItemPrice_quantityBased());
                modal_newOrderItems.setSubTotal_perItem(String.valueOf(itemPrice));

                modal_newOrderItems.setQuantity(String.valueOf(quantity));
                adapter_cartItem_recyclerview.notifyDataSetChanged();

            }



        }else{
            //Log.e(TAG, "2nd else "+itemUniquecode);

            addItemsNormallyIntheCart(newItem_newOrdersPojoClass,itemWeight);

        }
    }



    private void deleteRepeatedIndex() {
        int last_ItemInCart = cart_Item_List.size() - 1;
        cart_Item_List.remove(last_ItemInCart);
        adapter_cartItem_recyclerview.notifyDataSetChanged();
    }

    public  void addItemsNormallyIntheCart(Modal_NewOrderItems newItem_newOrdersPojoClass, String itemWeight) {
        //Log.e(TAG, "Got barcode addItemIntheCart"+isdataFetched);
        int last_ItemInCart = NewOrders_MenuItem_Fragment.cart_Item_List.size() - 1;
        //Log.e(TAG, "barcode uniquecode last_ItemInCart" + last_ItemInCart);
        //Log.e(TAG, "barcode uniquecode itemWeight" + itemWeight);
        //Log.e(TAG, "barcode uniquecode getItemFinalPrice" + newItem_newOrdersPojoClass.getPricePerItem());
        //Log.e(TAG, "barcode uniquecode getItemFinalWeight" + newItem_newOrdersPojoClass.getItemFinalWeight());

        Modal_NewOrderItems modal_newOrderItems = NewOrders_MenuItem_Fragment.cart_Item_List.get(last_ItemInCart);
        if (String.valueOf(modal_newOrderItems.getItemname()).equals("")) {
            //Log.e(TAG, "barcode in if  " + modal_newOrderItems.getItemname());

            modal_newOrderItems.setItemname(newItem_newOrdersPojoClass.getItemname());
            modal_newOrderItems.setTmcpriceperkg(newItem_newOrdersPojoClass.getTmcpriceperkg());
            modal_newOrderItems.setGrossweight(newItem_newOrdersPojoClass.getGrossweight());
            modal_newOrderItems.setNetweight(newItem_newOrdersPojoClass.getNetweight());
            modal_newOrderItems.setTmcprice(newItem_newOrdersPojoClass.getTmcprice());
            modal_newOrderItems.setGstpercentage(newItem_newOrdersPojoClass.getGstpercentage());
            modal_newOrderItems.setPortionsize(newItem_newOrdersPojoClass.getPortionsize());
            modal_newOrderItems.setPricetypeforpos(newItem_newOrdersPojoClass.getPricetypeforpos());
            modal_newOrderItems.setPricePerItem(newItem_newOrdersPojoClass.getPricePerItem());
            modal_newOrderItems.setQuantity(newItem_newOrdersPojoClass.getQuantity());
            modal_newOrderItems.setItemuniquecode(newItem_newOrdersPojoClass.getItemuniquecode());
            modal_newOrderItems.setItemFinalWeight(newItem_newOrdersPojoClass.getItemFinalWeight());
            newItem_newOrdersPojoClass.setSubTotal_perItem(newItem_newOrdersPojoClass.getSubTotal_perItem());
            newItem_newOrdersPojoClass.setTotal_of_subTotal_perItem(newItem_newOrdersPojoClass.getTotal_of_subTotal_perItem());
            newItem_newOrdersPojoClass.setGstAmount(newItem_newOrdersPojoClass.getGstAmount());
            newItem_newOrdersPojoClass.setItemPrice_quantityBased(newItem_newOrdersPojoClass.getItemPrice_quantityBased());


            //Log.e(TAG, "barcode in if before cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());

            // NewOrders_MenuItem_Fragment.cart_Item_List.add(last_ItemInCart,modal_newOrderItems);
            //Log.e(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
            //    newOrders_menuItem_fragment.add_amount_ForBillDetails();
            // cart_Item_List.add(last_ItemInCart,modal_newOrderItems);
            //Log.i(TAG, "barcode in if after cart_Item_List.size()" + NewOrders_MenuItem_Fragment.cart_Item_List.size());
            //Log.e(TAG, "Got barcode addItemIntheCart" +
                    "" +
                    ""+isdataFetched);

            NewOrders_MenuItem_Fragment.adapter_cartItem_recyclerview.notifyDataSetChanged();

        }
    }


 */



   /*  new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Exit_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        printRecipt(userMobile, tokenno, itemTotalwithoutGst, totaltaxAmount, payableAmount, orderid, cart_Item_List, cartItem_hashmap, payment_mode);

                    }

                    @Override
                    public void onNo() {
                     autoComplete_customerNameText_widget.setText("");
                                    autoComplete_customerNameText_widget.dismissDropDown();
                        cart_Item_List.clear();
                        cartItem_hashmap.clear();

                        new_to_pay_Amount = 0;
                        old_taxes_and_charges_Amount = 0;
                        old_total_Amount = 0;
                        createEmptyRowInListView("empty");
                        isproceedtoPay_Clicked = false;
                        ispaymentMode_Clicked = false;
                        CallAdapter();

                        total_item_Rs_text_widget.setText(String.valueOf(old_total_Amount));
                        taxes_and_Charges_rs_text_widget.setText(String.valueOf((old_taxes_and_charges_Amount)));
                        total_Rs_to_Pay_text_widget.setText(String.valueOf(new_to_pay_Amount));

                        mobileNo_Edit_widget.setText("");
                    }
                });

       */

/*
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

 */