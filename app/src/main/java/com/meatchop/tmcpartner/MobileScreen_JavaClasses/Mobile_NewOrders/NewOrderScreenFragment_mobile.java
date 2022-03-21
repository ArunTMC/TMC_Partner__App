package com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Modal_MenuItem;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Pos_NewOrders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.DeviceListActivity;
import com.meatchop.tmcpartner.Settings.Modal_MenuItemStockAvlDetails;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
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
    String vendorKey="",usermobileNo ="";
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String selectedPaymentMode  ="NONE SELECTED";
    String selectedOrderType  ="POS Order";

    public static BottomSheetDialog bottomSheetDialog;
    BottomNavigationView bottomNavigationView;
    boolean isUpdateCouponTransactionMethodCalled=false;
    private  boolean isOrderDetailsMethodCalled =false;
    private  boolean isOrderTrackingDetailsMethodCalled =false;
    private  boolean isPaymentDetailsMethodCalled =false;
    boolean isMobileAppDataFetchedinDashboard=false;
    boolean isanyProducthaveZeroAsweight=false;
    boolean isUpdateRedeemPointsWithoutKeyMethodCalled =false, ispaymentMode_Clicked =false,isPrintedSecondTime=false,isPhoneOrderSelected=false;
    double screenInches;
    double totalredeempointsusergetfromorder=0,pointsfor100rs_double=0;
    String ordertype="",customermobileno="",maxpointsinaday_String="",minordervalueforredeem_String="",pointsfor100rs_String="";
    double maxpointsinaday_double,minordervalueforredeem_double,finalamounttoPay;
    List<Modal_MenuItemStockAvlDetails> MenuItemStockAvlDetails=new ArrayList<>();
    List<Modal_MenuItem> MenuItemArray=new ArrayList<>();

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
    boolean isPrinterCnnectedfromSP = false;
    String printerNamefromSP = "";
    boolean isinventorycheck = false;

    private  boolean isStockOutGoingAlreadyCalledForthisItem =false;
    public static List<String> StockBalanceChangedForThisItemList = new ArrayList<>();

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



        try{
            SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = shared.getString("VendorKey","");
            usermobileNo = (shared.getString("UserPhoneNumber", "+91"));
            isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }



    public String getData() {

        return getArguments().getString("menuItem");
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

        try{
            MenuItems=getData();

            //Log.i(TAG, "call adapter cart_Item " + getData());

           completemenuItem= getMenuItemfromString(MenuItems);
            //getMenuItemStockAvlDetailsArrayAndMenuItemFromSharedPreferences();


            getMenuItemArrayFromSharedPreferences();

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

        if (screenInches < Constants.default_mobileScreenSize) {
            bottomNavigationView = ((MobileScreen_Dashboard) requireActivity()).findViewById(R.id.bottomnav);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!recyclerView.canScrollVertically(-1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                        if (screenInches < Constants.default_mobileScreenSize) {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }
                    else if(newState==RecyclerView.SCROLL_STATE_DRAGGING) {
                        if (bottomNavigationView.getVisibility() ==  View.GONE){
                        Toast.makeText(mContext, "Swipe downwards to make the Settings Button Visible", Toast.LENGTH_SHORT).show();
                    }
                    }

                    else {
                        bottomNavigationView.setVisibility(View.GONE);


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
        } else {
            // bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);
        }


      createEmptyRowInListView("empty");

        CallAdapter();
        try{
            if(maxpointsinaday_double==0||minordervalueforredeem_double==0||pointsfor100rs_double==0||(!isMobileAppDataFetchedinDashboard)){


                showProgressBar(true);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetMobileAppData, null,
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
        });




        return rootView;
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
        Button checkout_button_Widget = bottomSheetDialog.findViewById(R.id.checkout_button_Widget);
        TextView itemtotal_textWidget = bottomSheetDialog.findViewById(R.id.itemtotal_textWidget);
        TextView discountTextWidget = bottomSheetDialog.findViewById(R.id.discountTextWidget);
        TextView toPay_textWidget = bottomSheetDialog.findViewById(R.id.toPay_textWidget);
        Spinner paymentModeSpinner_Widget = bottomSheetDialog.findViewById(R.id.paymentModeSpinner_Widget);

        Spinner orderTypeSpinner_Widget = bottomSheetDialog.findViewById(R.id.orderTypeSpinner_Widget);

        Objects.requireNonNull(itemtotal_textWidget).setText(finaltoPayAmount);
        Objects.requireNonNull(toPay_textWidget).setText(finaltoPayAmount);
        Objects.requireNonNull(discountTextWidget).setText("0");

        String[] paymentType=getResources().getStringArray(R.array.PaymentMode);
        String[] ordertype=getResources().getStringArray(R.array.OrderType);

        ArrayAdapter<String> arrayAdapterpaymentType = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, paymentType);
        arrayAdapterpaymentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Objects.requireNonNull(paymentModeSpinner_Widget).setAdapter(arrayAdapterpaymentType);

        ArrayAdapter<String> arrayAdapterordertype = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, ordertype);
        arrayAdapterordertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Objects.requireNonNull(orderTypeSpinner_Widget).setAdapter(arrayAdapterordertype);

        discountAmount ="0";
        discount_editWidget.setText("0");
        Objects.requireNonNull(userstoreNumberCheckboxWidget).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    if(vendorKey.equals("vendor_1")){
                        Objects.requireNonNull(customermobileno_editwidget).setText("8939189102");

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
                if(selectedOrderType.equals("PHONEORDER")){
                    isPhoneOrderSelected = true;
                }
                else{
                    isPhoneOrderSelected = false;

                }
                //  Toast.makeText(parent.getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                isPhoneOrderSelected = false;

                selectedOrderType = "POS Order";
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

                                        double toPayAmt = Double.parseDouble(finaltoPayAmount);
                                        if (toPayAmt > discountAmountdouble) {
                                            toPayAmt = toPayAmt - discountAmountdouble;
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
                    discountAmount = "0";
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
                                if(selectedPaymentMode.equals("CASH")){
                                    selectedPaymentMode=Constants.CASH_ON_DELIVERY;


                                }
                                customermobileno = Objects.requireNonNull(customermobileno_editwidget).getText().toString();
                                 finaltoPayAmountinmethod = toPay_textWidget.getText().toString();
                                PlaceOrdersinDatabaseaAndPrintRecipt(selectedPaymentMode,finaltoPayAmountinmethod,sTime,Currenttime,cart_Item_List);

                                   bottomSheetDialog.cancel();
                                Toast.makeText(getContext(), "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();


                            } else {
                                showProgressBar(false);

                                AlertDialogClass.showDialog(getActivity(), R.string.Cant_place_order);

                            }


                        } else {
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














        bottomSheetDialog.show();





    }











    private void PlaceOrdersinDatabaseaAndPrintRecipt(String paymentMode, String finaltoPayAmountinmethod, long sTime, String currenttime, List<String> cart_Item_list) {
        showProgressBar(true);

        if (ispaymentMode_Clicked) {
            return;
        }
        else {

            ispaymentMode_Clicked = true;
            String payableAmount =finaltoPayAmount;

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


                                    cart_Item_List.clear();
                                    cartItem_hashmap.clear();
                                   ispaymentMode_Clicked = false;
                                    isOrderDetailsMethodCalled = false;

                                    isPaymentDetailsMethodCalled = false;
                                    isOrderTrackingDetailsMethodCalled = false;

                                    newGst =0;

                                    new_to_pay_Amount = 0;
                                    old_taxes_and_charges_Amount = 0;
                                    old_total_Amount = 0;
                                    createEmptyRowInListView("empty");
                                    CallAdapter();
                                    discountAmount = "0";
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
                                    OrderTypefromSpinner = "POS Order";
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

                                if (!isOrderDetailsMethodCalled) {

                                    PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, paymentMode, sTime,finaltoPayAmountinmethod,false);
                                }
                                if (!isOrderTrackingDetailsMethodCalled) {

                                    PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                                }




                                try{
                                    totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                                String UserMobile = "+91" + customermobileno;

                                //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                                updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);



                                return;

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

                                    if (!isOrderDetailsMethodCalled) {

                                        PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, paymentMode, sTime,finaltoPayAmountinmethod,false);
                                    }
                                    if (!isOrderTrackingDetailsMethodCalled) {

                                        PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                                    }




                                    try{
                                        totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    String UserMobile = "+91" + customermobileno;

                                    //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                    //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                                    updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);



                                    return;
                                }
                            });

                }
                else{

                    if (!isOrderDetailsMethodCalled) {

                        PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, paymentMode, sTime,finaltoPayAmountinmethod,true);
                    }
                    if (!isOrderTrackingDetailsMethodCalled) {

                        PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                    }




                    try{
                        totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    String UserMobile = "+91" + customermobileno;

                    //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                    //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                    updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);


                }


            }




        }


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



    public void CallAdapter() {
        if(cart_Item_List.size()<=0 ){
            mobile_ItemTotal_textwidget.setText("0.00");
            mobile_GST_textwidget.setText("0.00");


            mobile_ToPay_textwidget.setText("0.00");
        }
        else {
            add_amount_ForBillDetails();
        }
        adapterNewOrderScreenFragmentMobile = new Adapter_NewOrderScreenFragment_Mobile(mContext,cartItem_hashmap, MenuItems, NewOrderScreenFragment_mobile.this);
        adapterNewOrderScreenFragmentMobile.setHandler(newHandler());
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        int last_index=NewOrderScreenFragment_mobile.cartItem_hashmap.size()-1;

        recyclerView.setAdapter(adapterNewOrderScreenFragmentMobile);
        recyclerView.scrollToPosition(last_index);
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.setContentView(R.layout.billingscreen_mobile_neworderfragment);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

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
                            newOrdersPojoClass.discountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                        }
                        else{
                            newOrdersPojoClass.discountpercentage = "0";
                            Toast.makeText(mContext,"TMC applieddiscountpercentage Json is Missing",Toast.LENGTH_LONG).show();

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

            mobile_ItemTotal_textwidget.setText(decimalFormat.format(old_total_Amount));
            mobile_GST_textwidget.setText(decimalFormat.format(old_taxes_and_charges_Amount));
            new_totalAmount_withoutGst= (int) Math.round(old_total_Amount);
            newGst =  (int) Math.round(old_taxes_and_charges_Amount);
            new_totalAmount_withGst = (int) Math.round(new_to_pay_Amount);
            finaltoPayAmount = String.valueOf(new_totalAmount_withGst)+".00";
            mobile_ToPay_textwidget.setText(String.valueOf(new_totalAmount_withGst)+".00");
            totalAmounttopay=new_totalAmount_withGst;
        }catch (Exception e){
            e.printStackTrace();
        }

        old_total_Amount=0;
        old_taxes_and_charges_Amount=0;
        new_to_pay_Amount=0;


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



    private void PlaceOrder_in_OrderDetails(List<String> cart_Item_List, String Payment_mode, long sTime, String finaltoPayAmountinmethod, boolean shouldGetPrintNow) {
        if(isOrderDetailsMethodCalled){
            return;
        }
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
        String tokenno = "";
        String userid = "";
        ordertype = Constants.POSORDER;
        String deliverytype = Constants.STOREPICKUP_DELIVERYTYPE;
        String slotdate = "";
        if(isPhoneOrderSelected){
            ordertype = Constants.PhoneOrder;
            deliverytype = Constants.HOME_DELIVERY_DELIVERYTYPE;
            slotdate  = CurrentDate;
        }

        String slotname = "EXPRESSDELIVERY";

        String orderPlacedDate = CurrentDate;

        String slottimerange = "";
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


                                    cart_Item_List.clear();
                                    cartItem_hashmap.clear();
                                    ispaymentMode_Clicked = false;
                                    isOrderDetailsMethodCalled = false;
                                    isPhoneOrderSelected = false;

                                    isPaymentDetailsMethodCalled = false;
                                    isOrderTrackingDetailsMethodCalled = false;
                                    new_to_pay_Amount = 0;
                                    old_taxes_and_charges_Amount = 0;
                                    newGst =0;
                                    old_total_Amount = 0;
                                    createEmptyRowInListView("empty");
                                    CallAdapter();
                                    discountAmount = "0";

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
                                    OrderTypefromSpinner = "POS Order";
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
                    } else {
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

        }
        else{
            StoreCoupon = "STORECOUPON";

        }



        if(StoreCoupon.equals("STORECOUPON")){
            String transactiontime = getDate_and_time();


            addDatatoCouponTransactioninDB(CouponDiscountAmount,"STORECOUPON",UserMobile,String.valueOf(sTime),CurrentDate,transactiontime,vendorKey);


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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addOrderDetailsInOrderDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        // StartTwice startTwice =new StartTwice(UserMobile,tokenno,itemTotalwithoutGst,taxAmount,payableAmount,orderid,cart_Item_List,cartItem_hashmap,Payment_mode);
                        // startTwice.main();

                        if(shouldGetPrintNow) {

                            printRecipt(orderplacedTime, UserMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_Item_List, cartItem_hashmap, Payment_mode, discountAmount, ordertype);

                            //  showProgressBar(false);
                        }
                        else{
                            if(isinventorycheck){
                            turnoffProgressBarAndResetArray();
                            }

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
                                        Log.d(TAG, " response: onMobileAppData " + response);

                                        Log.i(TAG, "getStock incoming name" + itemName);

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
                                                if (json.has("outgoingqty")) {
                                                    outgoingqty_stockOutGngDetails_String[0] = (json.getString("outgoingqty"));
                                                } else {
                                                    outgoingqty_stockOutGngDetails_String[0] = "0";
                                                }
                                            } catch (Exception e) {
                                                outgoingqty_stockOutGngDetails_String[0] = "0";

                                                e.printStackTrace();
                                            }
                                            if(!outgoingtype_stockOutGngDetails_String[0].equals(Constants.SALES_CANCELLED_OUTGOINGTYPE)){
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


                                            Log.i(TAG, "getStock incoming stocktype_stockOutGngDetails_String" + stocktype_stockOutGngDetails_String[0]);


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
                                            Log.i(TAG, "getStock incoming outgoingtype_stockOutGngDetails_String" + outgoingtype_stockOutGngDetails_String[0]);


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



                                            Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_String" + outgoingqty_stockOutGngDetails_String[0]);


                                            try {
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


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

                                            Log.i(TAG, "getStock incoming stockincomingkey_stockOutGngDetails_String" + stockincomingkey_stockOutGngDetails_String[0]);


                                            try {
                                                outgoingqty_stockOutGngDetails_Double[0] = Double.parseDouble(outgoingqty_stockOutGngDetails_String[0]);


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double" + outgoingqty_stockOutGngDetails_Double[0]);


                                            if (outgoingtype_stockOutGngDetails_String[0].equals(Constants.SUPPLYGAP_OUTGOINGTYPE)) {
                                                Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 1 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 1 " + outgoingqty_stockOutGngDetails_Double[0]);


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
                                                    Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 3 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                    Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 3 " + outgoingqty_stockOutGngDetails_Double[0]);

                                                    Total_outgoingqty_stockOutGngDetails_Double[0] = Total_outgoingqty_stockOutGngDetails_Double[0] + outgoingqty_stockOutGngDetails_Double[0];
                                                    Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double 4 " + Total_outgoingqty_stockOutGngDetails_Double[0]);
                                                    Log.i(TAG, "getStock incoming outgoingqty_stockOutGngDetails_Double 4 " + outgoingqty_stockOutGngDetails_Double[0]);


                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        }
                                    }

                                        Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);


                                        Log.i(TAG, "getStock incoming receivedStock_AvlDetails  " + receivedStock_AvlDetails);

                                        try {
                                            receivedStock_AvlDetails_double[0] = Double.parseDouble(receivedStock_AvlDetails);
                                            Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double  " + receivedStock_AvlDetails_double[0]);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        double stockBalanceBeforeMinusCurrentItem = 0;
                                        try {
                                            Log.i(TAG, "getStock incoming receivedStock_AvlDetails_double 2  " + receivedStock_AvlDetails_double[0]);
                                            Log.i(TAG, "getStock incoming Total_outgoingqty_stockOutGngDetails_Double  5  " + Total_outgoingqty_stockOutGngDetails_Double[0]);

                                            stockBalanceBeforeMinusCurrentItem = receivedStock_AvlDetails_double[0] - Total_outgoingqty_stockOutGngDetails_Double[0];


                                            Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 2  " + stockBalanceBeforeMinusCurrentItem);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                        try {


                                            Log.i(TAG, "getStock incoming stockBalanceBeforeMinusCurrentItem 3  " + stockBalanceBeforeMinusCurrentItem);

                                            finalStockBalance_double[0] = stockBalanceBeforeMinusCurrentItem - currentBillingItemWeight_double;

                                            Log.i(TAG, "getStock incoming currentBillingItemWeight_double 4 " + currentBillingItemWeight_double);
                                            Log.i(TAG, "getStock incoming finalStockBalance_double 4 " + finalStockBalance_double[0]);

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
                        showProgressBar(false);
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
                                modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(isitemAvailable));
                                uploadMenuAvailabilityStatusTranscationinDB(usermobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorKey,Currenttime,menuItemKey_avlDetails,message, "", false, "");
                                savedMenuIteminSharedPrefrences(completemenuItem,iterator_menuitemStockAvlDetails);

                            }

                        }
                    }

                    showProgressBar(false);

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, api_Update_MenuItemStockAvlDetails
                ,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    String message =  response.getString("message");
                    if(message.equals("success")) {
                        //Log.d(Constants.TAG, "Express Slot has been succesfully turned Off: " );
                        showProgressBar(false);
                        turnoffProgressBarAndResetArray();

                    }


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

    private void printRecipt(String orderplacedTime, String userMobile, String tokenno, String itemTotalwithoutGst, String taxAmount, String finaltoPayAmountinmethod, String orderid, List<String> cart_item_list, HashMap<String, Modal_NewOrderItems> cartItem_hashmap, String payment_mode, String discountAmount, String ordertype) {
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

    try {
            new Thread(new Runnable() {
                public void run() {


                    double oldSavedAmount = 0;
                    String CouponDiscount = "0";


                    String Title = "The Meat Chop";

                    String GSTIN = "GSTIN :33AAJCC0055D1Z9";
                    String CurrentTime = getDate_and_time();


                    BluetoothPrintDriver.Begin();

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

                                printRecipt(orderplacedTime,userMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_item_list, cartItem_hashmap, payment_mode,discountAmount,ordertype);

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




                cart_Item_List.clear();
                cartItem_hashmap.clear();
                ispaymentMode_Clicked = false;
                isOrderDetailsMethodCalled = false;

                isPaymentDetailsMethodCalled = false;
                isOrderTrackingDetailsMethodCalled = false;
                new_to_pay_Amount = 0;
                old_taxes_and_charges_Amount = 0;
                newGst =0;

                old_total_Amount = 0;
                createEmptyRowInListView("empty");
                CallAdapter();
                discountAmount = "0";

                finaltoPayAmount = "0";
                new_totalAmount_withoutGst =0;
                isPhoneOrderSelected = false;

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


    private void PlaceOrder_in_OrderTrackingDetails(long sTime, String Currenttiime, String finaltoPayAmountinmethod) {

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

            orderTrackingTablejsonObject.put("usermobileno","+91" + customermobileno);
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
                            if (!isOrderDetailsMethodCalled) {

                                PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, selectedPaymentMode, sTime,finaltoPayAmountinmethod, true);
                            }
                            if (!isOrderTrackingDetailsMethodCalled) {

                                PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                            }




                            try{
                                totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            String UserMobile = "+91" + customermobileno;

                            //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                            //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                            updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

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
                                        if (!isOrderDetailsMethodCalled) {

                                            PlaceOrder_in_OrderDetails(NewOrderScreenFragment_mobile.cart_Item_List, selectedPaymentMode, sTime,finaltoPayAmountinmethod, false);
                                        }
                                        if (!isOrderTrackingDetailsMethodCalled) {

                                            PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                                        }




                                        try{
                                            totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        String UserMobile = "+91" + customermobileno;

                                        //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                        //   Toast.makeText(mContext,"points :"+se,Toast.LENGTH_LONG).show();
                                        updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);



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

