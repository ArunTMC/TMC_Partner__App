package com.meatchop.tmcpartner.settings;

import android.Manifest;
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
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.amazonaws.mobile.client.AWSMobileClient;
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
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.mobilescreen_javaclasses.other_classes.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Pos_LoginScreen;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.add_replacement_refund_order.Add_Replacement_Refund_Screen;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;
import static android.widget.AbsListView.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    Button on, off;
    Context mContext;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch autoRefreshingSwitch;
    LinearLayout replacement_refund_transaction_reportLayout,addDunzoOrders_Placing_layout,generateOrderItemDetailsLayout,consolidatedSalesReportWeekwise, login_as_another_vendor,
            changeMenuItemAvail_allowNegativeStock,manageordersLinearLayout, slotwiseAppOrderList, plotOrdersLocation_layout, testlayout, editPaymentModeOftheOrder, delivered_orders_timewiseReport, changeMenuItemStatus, logout, consolidatedSalesReport, PosSalesReport, AppSalesReport, changeMenuItemVisibilityinTv, managemenuLayout, changeMenuItemPrice, changeDeliverySlotdetails, deliveryPartnerSettlementReport, searchOrdersUsingMobileNumbers, posOrdersList, generateCustomerMobileno_BillvalueReport, loadingpanelmask, loadingPanel;
    String UserRole, MenuItems, UserPhoneNumber, vendorkey, vendorName,vendorType;
    TextView progressbarInstruction,userMobileNo, resetTokenNO_text, storeName, App_Sales_Report_text, Pos_Sales_Report_text;
    LinearLayout isNeedToConnectWeigtMachineLinearLayout,refreshMenuItemLayout,deliveryStatusTransactionLayout, temporaryLayout,phone_sales_report,phone_orders_list,wholesale_orders_list,WholeSaleSalesReport,addWholeSaleOrders_placing_layout,mobilenowisecreditOrderslist,changeMenuItemPrice_weight,manageRaisedTickets,addBigbasketOrders_placing_layout,orderRating_report,mobilePrinterConnectLayout,menuItemAvailabiltyStatusReport,orderTrackingDetailsDump_report,GeneralConfiguration_linearLayout,dataAnalyticsLinearLayout,viewordersLinearLayout, menuTransactionDetailsLayout, salesLinearLayout, orderDetailsDump_report, cancelledOrdersLayout, resetTokenNoLayout, generateUserDetailsLayout,swiggyOrderPlacing_layout,add_refund_replace_order_layout;
    Button resetTokenNoButton,refreshMenuItemButton;
    ScrollView settings_scrollview;
    BottomNavigationView bottomNavigationView;

    double screenInches;
    List<String>allowedModules_array = new ArrayList<>();

    List<String>printerType_ArrayList = new ArrayList<>();

    boolean isinventorycheck = false;
    // Local Bluetooth adapter
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

    String mConnectedDeviceName;
    TextView printerConnectionStatus_Textwidget;
    boolean isPrinterCnnected = false;
    String printerName = "";
    String printerStatus = "";
    boolean isPrinterCnnectedfromSP = false;
    String printerNamefromSP = "",whichsheetoGenerate1 = "";
    String printerStatusfromSP = "";
    boolean localDBcheck = false;
    boolean orderdetailsnewschema =false;
    boolean isWeightMachineConnectedBoolean =false;
    boolean isbarcodescannerconnected =false;

    Workbook wb;
    Sheet sheet = null;
    String errorCode ="";


    private static String[] columnsHeading_userDetails = {"S.No", "User Key", "MobileNo", " Name", "Email", "Created time", "App Version", "deviceos", "updatedtime", "Fcm Token", "User Address Key","AddressLine 1","AddressLine 2","LandMark","PinCode","Address Type","Delivery Distance","Location Latitude","Location Longitutde","Vendor Name","Contact Person Mobile no", "Contact Person name"};
    private static String[] columnsHeading_orderItemDetails = {"S.No", "Key", "Applied Discount Percentage", " Cut Name", "Cut Price", "Discount Amount", "Grossweight in Grams", "Gst Amount","Orderid ", "Item Name","Net Weight", "TmcPrice","Quantity","Total Price","Portion Size","Order Placed Time", "Slot Date","Slot Name","Marinade Item Details","Tmc Subctgykey", "Vendor Key","Vendor Name","total"};

    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;
    Button generateUserDetailsButton,generateOrderItemDetailsButton,connect_printer_button_widget;
    List<Modal_User> UserTableArray = new ArrayList<>();
    List<Modal_Address> AddressTableArray = new ArrayList<>();
    List<Modal_User> FilteredUserTableArray = new ArrayList<>();
    List<Modal_Address> FilteredAddressTableArray = new ArrayList<>();
    List<String>AddedUserKey = new ArrayList<>();
    Spinner printerTypeSpinner;
    HashMap<String,Double> orderidTotal = new HashMap<>();
    List<ModalOrderItemDetails> OrderItemDetailsTableArray = new ArrayList<>();
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION.Settings";
    RadioGroup printerTypeRadioGroup;
    LinearLayout printerParentLayout,layout_fetch_orders_from_orderDetails;
    RadioButton usbRadiobutton,bluetoothPrinterRadiobutton,posPrinterRadiobutton,nonePrinterRadiobutton;
    String vendorkey_hastinapuram = "vendor_1",vendorkey_velachery = "vendor_3",vendorkey_usertable = "";
    Switch switch_fetch_orders_from_orderDetails , switchisNeedToConnectWeigtMachine;

    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;
    List<Modal_MenuItem> MenuList=new ArrayList<>();
    List<Modal_MenuItemStockAvlDetails> MenuItemStockAvlDetails=new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String data) {
        Bundle args = new Bundle();
         // args.putString("menuItem", data);

        SettingsFragment fragment = new SettingsFragment();
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
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

         */
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            if (getArguments() != null) {
                getArguments().clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        deliveryPartnerSettlementReport = view.findViewById(R.id.deliveryPartnerSettlementReport);
        searchOrdersUsingMobileNumbers = view.findViewById(R.id.searchOrdersUsingMobileNumbers);
        userMobileNo = view.findViewById(R.id.userMobileNo);
        settings_scrollview = view.findViewById(R.id.settings_scrollview);
        autoRefreshingSwitch = view.findViewById(R.id.autoRefreshingSwitch);
        changeMenuItemStatus = view.findViewById(R.id.changeMenuItemStatus);
        printerTypeSpinner  = view.findViewById(R.id.printerTypeSpinner);
        delivered_orders_timewiseReport = view.findViewById(R.id.delivered_orders_timewiseReport);
        consolidatedSalesReport = view.findViewById(R.id.consolidatedSalesReport);
        PosSalesReport = view.findViewById(R.id.PosSalesReport);
        AppSalesReport = view.findViewById(R.id.AppSalesReport);
        changeMenuItemVisibilityinTv = view.findViewById(R.id.changeMenuItemVisibilityinTv);
        changeMenuItemPrice = view.findViewById(R.id.changeMenuItemPrice);
        logout = view.findViewById(R.id.logout);
        resetTokenNoButton = view.findViewById(R.id.resetTokenNoButton);
        resetTokenNO_text = view.findViewById(R.id.resetTokenNO_text);
        changeDeliverySlotdetails = view.findViewById(R.id.changeDeliverySlotdetails);
        storeName = view.findViewById(R.id.storeName);
        salesLinearLayout = view.findViewById(R.id.salesLinearLayout);
        posOrdersList = view.findViewById(R.id.pos_orders_list);
        editPaymentModeOftheOrder = view.findViewById(R.id.editPaymentModeOftheOrder);
        generateCustomerMobileno_BillvalueReport = view.findViewById(R.id.generateCustomerMobileno_BillvalueReport);
        orderDetailsDump_report = view.findViewById(R.id.orderDetailsDump_report);
        cancelledOrdersLayout = view.findViewById(R.id.cancelledOrdersLayout);
        managemenuLayout = view.findViewById(R.id.managemenuLayout);
        menuTransactionDetailsLayout = view.findViewById(R.id.getMenuTransactionDetailsLayout);
        testlayout = view.findViewById(R.id.testlayout);
        plotOrdersLocation_layout = view.findViewById(R.id.plotOrdersLocation_layout);
        slotwiseAppOrderList = view.findViewById(R.id.slotwiseAppOrderList);
        manageordersLinearLayout = view.findViewById(R.id.manageordersLinearLayout);
        resetTokenNoLayout = view.findViewById(R.id.resetTokenNoLayout);
        connect_printer_button_widget = view.findViewById(R.id.connect_printer_button_widget);
        printerConnectionStatus_Textwidget = view.findViewById(R.id.printerConnectionStatus_Textwidget);
        login_as_another_vendor = view.findViewById(R.id.login_as_another_vendor);
        consolidatedSalesReportWeekwise = view.findViewById(R.id.consolidatedSalesReportWeekwise);
        generateUserDetailsLayout = view.findViewById(R.id.generateUserDetailsLayout);
        generateUserDetailsButton = view.findViewById(R.id.generateUserDetailsButton);
        loadingpanelmask = view.findViewById(R.id.loadingpanelmask);
        loadingPanel = view.findViewById(R.id.loadingPanel);
        progressbarInstruction = view.findViewById(R.id.progressbarInstruction);
        swiggyOrderPlacing_layout= view.findViewById(R.id.addSwiggyOrder_Placing_layout);
        viewordersLinearLayout = view.findViewById(R.id.viewordersLinearLayout);
        dataAnalyticsLinearLayout = view.findViewById(R.id.dataAnalyticsLinearLayout);
        GeneralConfiguration_linearLayout = view.findViewById(R.id.GeneralConfiguration_linearLayout);
        orderTrackingDetailsDump_report = view.findViewById(R.id.orderTrackingDetailsDump_report);
        generateOrderItemDetailsLayout = view.findViewById(R.id.generateOrderItemDetailsLayout);
        generateOrderItemDetailsButton = view.findViewById(R.id.generateOrderItemDetailsButton);
        addDunzoOrders_Placing_layout =  view.findViewById(R.id.addDunzoOrders_Placing_layout);
        menuItemAvailabiltyStatusReport  =  view.findViewById(R.id.menuItemAvailabiltyStatusReport);
        mobilePrinterConnectLayout = view.findViewById(R.id.mobilePrinterConnectLayout);
        orderRating_report  =  view.findViewById(R.id.orderRating_report);
        addBigbasketOrders_placing_layout = view.findViewById(R.id.addBigbasketOrders_placing_layout);
        manageRaisedTickets = view.findViewById(R.id.manageRaisedTickets);
        changeMenuItemPrice_weight = view.findViewById(R.id.changeMenuItemPrice_weight);
        add_refund_replace_order_layout= view.findViewById(R.id.add_refund_replace_order_layout);
        changeMenuItemAvail_allowNegativeStock  = view.findViewById(R.id.changeMenuItemAvail_allowNegativeStock);
        printerTypeRadioGroup = (RadioGroup) view.findViewById(R.id.printerTypeRadioGroup);
        printerParentLayout =  view.findViewById(R.id.printerParentLayout);
        usbRadiobutton = view.findViewById(R.id.usbRadiobutton);
        bluetoothPrinterRadiobutton = view.findViewById(R.id.bluetoothPrinterRadiobutton);
        posPrinterRadiobutton = view.findViewById(R.id.posPrinterRadiobutton);
        nonePrinterRadiobutton = view.findViewById(R.id.nonePrinterRadiobutton);
        mobilenowisecreditOrderslist = view.findViewById(R.id.mobilenowisecreditOrderslist);
        addWholeSaleOrders_placing_layout = view.findViewById(R.id.addWholeSaleOrders_placing_layout);
        WholeSaleSalesReport = view.findViewById(R.id.WholeSaleSalesReport);
        wholesale_orders_list  = view.findViewById(R.id.wholesale_orders_list);
        replacement_refund_transaction_reportLayout = view.findViewById(R.id.replacement_refund_transaction_reportLayout);
        switch_fetch_orders_from_orderDetails  = view.findViewById(R.id.switch_fetch_orders_from_orderDetails);
        layout_fetch_orders_from_orderDetails  = view.findViewById(R.id.layout_fetch_orders_from_orderDetails);
        phone_orders_list = view.findViewById(R.id.phone_orders_list);
        phone_sales_report = view.findViewById(R.id.PhoneSaleSalesReport);
        temporaryLayout = view.findViewById(R.id.temporaryLayout);
        deliveryStatusTransactionLayout = view.findViewById(R.id.deliveryStatusTransactionLayout);
        refreshMenuItemButton   = view.findViewById(R.id.refreshMenuItemButton);
        refreshMenuItemLayout = view.findViewById(R.id.refreshMenuItemLayout);

        //  bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);

        //  final SharedPreferences sharedPreferencesMenuitem = requireContext().getSharedPreferences("MenuList", MODE_PRIVATE);
        //  MenuItems = sharedPreferencesMenuitem.getString("MenuList", "");

        SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        UserPhoneNumber = (shared.getString("UserPhoneNumber", "+91"));
        vendorkey = shared.getString("VendorKey", "");
        vendorName = shared.getString("VendorName", "");
        vendorType = shared.getString("VendorType", "");
        UserRole = shared.getString("userrole", "");
        isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));
        orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
        localDBcheck = (shared.getBoolean("localdbcheck", false));
        isWeightMachineConnectedBoolean = (shared.getBoolean("isweightmachineconnected", false));
        isbarcodescannerconnected = (shared.getBoolean("isbarcodescannerconnected", false));



        SharedPreferences shared_PF_PrinterData = mContext.getSharedPreferences("PrinterConnectionData",MODE_PRIVATE);
        String printerType_sharedPreference = (shared_PF_PrinterData.getString("printerType", ""));
        printerType_sharedPreference = String.valueOf(printerType_sharedPreference.toUpperCase());

        userMobileNo.setText(UserPhoneNumber);
        storeName.setText(vendorName);
        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(getActivity());
            //Toast.makeText(getActivity(), "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
              //  Toast.makeText(getActivity(), "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }

        //  initializeCache();

        salesLinearLayout.setVisibility(GONE);
        managemenuLayout.setVisibility(GONE);
        viewordersLinearLayout.setVisibility(GONE);
        manageordersLinearLayout.setVisibility(GONE);
        dataAnalyticsLinearLayout.setVisibility(GONE);

        if(orderdetailsnewschema){
            switch_fetch_orders_from_orderDetails.setChecked(false);
        }
        else{
            switch_fetch_orders_from_orderDetails.setChecked(true);

        }

        if(localDBcheck) {

            refreshMenuItemLayout.setVisibility(VISIBLE);

        }
        else {
            refreshMenuItemLayout.setVisibility(GONE);

        }

        refreshMenuItemButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectSqlDb();
            }
        });












        layout_fetch_orders_from_orderDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences
                        = mContext.getSharedPreferences("VendorLoginData",
                        MODE_PRIVATE);

                SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();
                if(switch_fetch_orders_from_orderDetails.isChecked()){
                    switch_fetch_orders_from_orderDetails .setChecked(false);
                    myEdit.putBoolean(
                            "orderdetailsnewschema_settings",
                            true
                    );
                    myEdit.apply();

                  //  Toast.makeText(mContext, "first", Toast.LENGTH_SHORT).show();
                }
                else{

                    switch_fetch_orders_from_orderDetails .setChecked(true);

                    myEdit.putBoolean(
                            "orderdetailsnewschema_settings",
                            false
                    );
                    myEdit.apply();
                   // Toast.makeText(mContext, "second", Toast.LENGTH_SHORT).show();

                }

            }
        });



        nonePrinterRadiobutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "None", Toast.LENGTH_SHORT).show();
              //  changeSelectedPrinterType(nonePrinterRadiobutton.getId(),true);
                if(nonePrinterRadiobutton.isSelected()){
                    changeSelectedPrinterType(nonePrinterRadiobutton.getId(),false);
                    // Toast.makeText(mContext, "USB", Toast.LENGTH_SHORT).show();

                }
                else{
                    changeSelectedPrinterType(nonePrinterRadiobutton.getId(),true);
                    //Toast.makeText(mContext, "None", Toast.LENGTH_SHORT).show();

                }
            }
        });
        usbRadiobutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usbRadiobutton.isSelected()){
                    changeSelectedPrinterType(usbRadiobutton.getId(),false);
                   // Toast.makeText(mContext, "USB", Toast.LENGTH_SHORT).show();

                }
                else{
                    changeSelectedPrinterType(usbRadiobutton.getId(),true);
                    //Toast.makeText(mContext, "None", Toast.LENGTH_SHORT).show();

                }

            }
        });
        bluetoothPrinterRadiobutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothPrinterRadiobutton.isSelected()){
                    changeSelectedPrinterType(bluetoothPrinterRadiobutton.getId(),false);
                 //   Toast.makeText(mContext, "none", Toast.LENGTH_SHORT).show();

                }
                else{
                    changeSelectedPrinterType(bluetoothPrinterRadiobutton.getId(),true);
                  //  Toast.makeText(mContext, "bluetooth", Toast.LENGTH_SHORT).show();

                }
            }
        });
        posPrinterRadiobutton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posPrinterRadiobutton.isSelected()){
                    changeSelectedPrinterType(posPrinterRadiobutton.getId(),false);
                    //Toast.makeText(mContext, "none", Toast.LENGTH_SHORT).show();

                }
                else {
                    changeSelectedPrinterType(posPrinterRadiobutton.getId(), true);
                  //  Toast.makeText(mContext, "POS", Toast.LENGTH_SHORT).show();
                }
            }
        });




        try {
            if (BluetoothPrintDriver.IsNoConnection()) {
                printerConnectionStatus_Textwidget.setText(String.valueOf("Not Connected"));

            } else {
                SharedPreferences shared2 = mContext.getSharedPreferences("PrinterConnectionData", MODE_PRIVATE);

                isPrinterCnnectedfromSP = (shared2.getBoolean("isPrinterConnected", false));
                printerNamefromSP = (shared2.getString("printerName", ""));
                printerStatusfromSP = (shared2.getString("printerStatus", ""));
                if (isPrinterCnnectedfromSP) {
                    printerConnectionStatus_Textwidget.setText(String.valueOf(printerStatusfromSP + "-" + printerNamefromSP));

                } else {
                    printerConnectionStatus_Textwidget.setText(String.valueOf("Not Connected"));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(UserRole.length()>0) {
            SharedPreferences sharedpref = mContext.getSharedPreferences("PartnerAppAccessDetails", MODE_PRIVATE);
            String allowedModules_String = sharedpref.getString(UserRole, "");
            if(allowedModules_String.length()>0) {
                String[] myArray = allowedModules_String.split(",");
                allowedModules_array =  Arrays.asList(myArray);

            }
            else {
                Toast.makeText(getActivity(),"Your role is "+UserRole,Toast.LENGTH_LONG).show();

            }


        }
        else{
            Toast.makeText(getActivity(),"You Don't have any User Role Ask Admin to assign the Role",Toast.LENGTH_LONG).show();

        }


        if(allowedModules_array.contains(Constants.SALESMODULE)){
            salesLinearLayout.setVisibility(VISIBLE);

        }
        if(allowedModules_array.contains(Constants.MANAGEMENUMODULE)){
            managemenuLayout.setVisibility(VISIBLE);
        }
        if(allowedModules_array.contains(Constants.VIEWORDERSMODULE)){
            viewordersLinearLayout.setVisibility(VISIBLE);

        }
        if(allowedModules_array.contains(Constants.MANAGEORDERSMODULE)){
            manageordersLinearLayout.setVisibility(VISIBLE);
        }
        if(allowedModules_array.contains(Constants.DATAANALYTICSMODULE)){
            dataAnalyticsLinearLayout.setVisibility(VISIBLE);
        }

        if(vendorType.toUpperCase().equals(Constants.WholeSales_VendorType)){

            changeDeliverySlotdetails.setVisibility(GONE);
            changeMenuItemAvail_allowNegativeStock.setVisibility(GONE);
            changeMenuItemStatus.setVisibility(GONE);
            changeMenuItemPrice_weight.setVisibility(GONE);
            changeMenuItemVisibilityinTv.setVisibility(GONE);
            menuItemAvailabiltyStatusReport.setVisibility(GONE);
            menuTransactionDetailsLayout.setVisibility(GONE);
            searchOrdersUsingMobileNumbers.setVisibility(GONE);
            wholesale_orders_list.setVisibility(GONE);
            slotwiseAppOrderList.setVisibility(GONE);
            plotOrdersLocation_layout.setVisibility(GONE);
            orderRating_report.setVisibility(GONE);
            manageRaisedTickets.setVisibility(GONE);
            AppSalesReport.setVisibility(GONE);
            WholeSaleSalesReport.setVisibility(GONE);
            deliveryPartnerSettlementReport.setVisibility(GONE);
            delivered_orders_timewiseReport.setVisibility(GONE);
            resetTokenNoLayout.setVisibility(GONE);
            addWholeSaleOrders_placing_layout.setVisibility(GONE);
            addBigbasketOrders_placing_layout.setVisibility(GONE);
            addDunzoOrders_Placing_layout.setVisibility(GONE);
            swiggyOrderPlacing_layout.setVisibility(GONE);
            printerParentLayout.setVisibility(GONE);
            phone_orders_list.setVisibility(GONE);
            phone_sales_report.setVisibility(GONE);

        }
        else {
            getTokenNo(vendorkey);

            if (screenInches > Constants.default_mobileScreenSize) {
                //if Pos
                editPaymentModeOftheOrder.setVisibility(GONE);
                plotOrdersLocation_layout.setVisibility(GONE);
                delivered_orders_timewiseReport.setVisibility(View.GONE);
                slotwiseAppOrderList.setVisibility(View.GONE);
                mobilePrinterConnectLayout.setVisibility(VISIBLE);
                dataAnalyticsLinearLayout.setVisibility(GONE);
                add_refund_replace_order_layout.setVisibility(GONE);
                replacement_refund_transaction_reportLayout.setVisibility(GONE);

                printerParentLayout.setVisibility(GONE);
                if (printerType_sharedPreference.equals(Constants.Bluetooth_PrinterType)) {
                    connect_printer_button_widget.setVisibility(VISIBLE);

                } else {
                    connect_printer_button_widget.setVisibility(GONE);

                }
            } else {
                //if Mobile
                replacement_refund_transaction_reportLayout.setVisibility(VISIBLE);
                addWholeSaleOrders_placing_layout.setVisibility(GONE);
                addBigbasketOrders_placing_layout.setVisibility(GONE);
                addDunzoOrders_Placing_layout.setVisibility(GONE);
                swiggyOrderPlacing_layout.setVisibility(GONE);
                printerParentLayout.setVisibility(GONE);
                connect_printer_button_widget.setVisibility(VISIBLE);
                add_refund_replace_order_layout.setVisibility(VISIBLE);
                if ((UserRole.toUpperCase().toString().equals(Constants.STOREMANAGER_ROLENAME)) || (UserRole.toUpperCase().toString().equals(Constants.ADMIN_ROLENAME))) {
                    editPaymentModeOftheOrder.setVisibility(VISIBLE);

                } else {
                    editPaymentModeOftheOrder.setVisibility(GONE);

                }

                if (UserRole.equals(Constants.DELIVERYMANAGER_ROLENAME)) {
                    salesLinearLayout.setVisibility(VISIBLE);
                    consolidatedSalesReport.setVisibility(GONE);
                    PosSalesReport.setVisibility(GONE);
                    AppSalesReport.setVisibility(GONE);
                    phone_sales_report.setVisibility(GONE);
                    consolidatedSalesReportWeekwise.setVisibility(GONE);
                    delivered_orders_timewiseReport.setVisibility(VISIBLE);


                }
            }
            if (!isinventorycheck) {
                changeMenuItemAvail_allowNegativeStock.setVisibility(GONE);
                changeMenuItemStatus.setVisibility(VISIBLE);
            } else {
                changeMenuItemAvail_allowNegativeStock.setVisibility(VISIBLE);
                changeMenuItemStatus.setVisibility(GONE);
            }


            ShowOrHideUI_AccordingTo_UserPhoneNumber();
        }

        if (screenInches < Constants.default_mobileScreenSize) {
            bottomNavigationView = ((MobileScreen_Dashboard) requireActivity()).findViewById(R.id.bottomnav);
            settings_scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollY = settings_scrollview.getScrollY(); //for verticalScrollView
                    if (scrollY == 0) {
                        if (screenInches < Constants.default_mobileScreenSize) {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    } else {
//                        Toast.makeText(mContext,"Swipe down to make the Settings Button Visible",Toast.LENGTH_SHORT).show();
                        bottomNavigationView.setVisibility(View.GONE);

                    }

                }
            });
        }
      // setSettingsUIBasedONManagementRole();

        phone_sales_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Phone_Sales_Report.class);
                startActivity(intent);
            }
        });
        phone_orders_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Phone_Orders_List.class);
                startActivity(intent);
            }
        });

        replacement_refund_transaction_reportLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Replacement_Refund_Transaction_Report.class);
                startActivity(intent);
            }
        });
        wholesale_orders_list.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WholeSaleOrdersList.class);
                startActivity(intent);
            }
        });
        WholeSaleSalesReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WholeSaleOrderSalesReport.class);
                startActivity(intent);
            }
        });
        addWholeSaleOrders_placing_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddWholeSalesOrder.class);
                startActivity(intent);
            }
        });

        add_refund_replace_order_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Add_Replacement_Refund_Screen.class);
                startActivity(intent);
            }
        });

        manageRaisedTickets.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RaisedTicketDetailsForRating.class);
                startActivity(intent);
            }
        });


        mobilenowisecreditOrderslist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreditOrders_MobileNumberwise_Report.class);
                startActivity(intent);
            }
        });





        addBigbasketOrders_placing_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddBigBasketOrder.class);
                startActivity(intent);
            }
        });

        orderRating_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DatewiseRatingreport_FirstScreen.class);
                startActivity(intent);
            }
        });



        menuItemAvailabiltyStatusReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MenuItemAvailabilityStatusReport.class);
                startActivity(intent);
            }
        });


        addDunzoOrders_Placing_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddDunzoOrders.class);
                startActivity(intent);
            }
        });

           // generateOrderItemDetailsLayout.setVisibility(GONE);
        generateOrderItemDetailsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOrderItemDetailsAndGenerateSheet();
            }
        });



        orderTrackingDetailsDump_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GenerateOrderDetailsDump.class);
                startActivity(intent);
            }
        });

        swiggyOrderPlacing_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddSwiggyOrders.class);

                startActivity(intent);
            }
        });
        generateUserDetailsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GenerateUserDetailsReport.class);

                startActivity(intent);
            }
        });
        generateUserDetailsButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            AskPermissionToGenerateSheet("UserDetailsSheet");

            //GetUserTable();

        }
        }
        );


        consolidatedSalesReportWeekwise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ConsolidatedSalesReportWeekwise.class);

                startActivity(intent);

            }
        });


        connect_printer_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    //requestBlePermissions(getActivity(),2);
                    Toast.makeText(mContext, "Please Use android version below 12 ", Toast.LENGTH_SHORT).show();
                }


                else{

               */
                    ConnectPrinter();
               // }


            }
        });


        login_as_another_vendor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Vendor_logoutInstruction,
                        R.string.Yes_Text, R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                LogoutFromtheCurrentVendor();

                            }

                            @Override
                            public void onNo() {

                            }
                        });

            }
        });


        slotwiseAppOrderList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SlotwiseAppOrderslist.class);

                startActivity(intent);

            }
        });


        menuTransactionDetailsLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MenuItem_List_Settings.class);
                intent.putExtra("ClickedOn", "MenuAvailabilityTransactionDetails");
                startActivity(intent);

            }
        });




        plotOrdersLocation_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlotOrdersLocationWithTokenNo.class);
                startActivity(intent);

            }
        });
        testlayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, testActivty.class);
                startActivity(intent);

            }
        });


        cancelledOrdersLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CancelledOrders.class);
                startActivity(intent);

            }
        });


        orderDetailsDump_report.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, GenerateOrderDetailsDump.class);
                startActivity(intent);

            }
        });


        generateCustomerMobileno_BillvalueReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, GenerateCustomerMobileNo_BillValueReport.class);
                startActivity(intent);

            }
        });
        editPaymentModeOftheOrder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(mContext, Edit_Or_CancelTheOrders.class);
                startActivity(intent);

            }
        });

        autoRefreshingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  mContext.startService(new Intent(mContext, TrackingOrderDetails_ServiceClass.class));
                } else {
                    // mContext.stopService(new Intent(mContext, TrackingOrderDetails_ServiceClass.class));
                }
            }
        });
        deliveryPartnerSettlementReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DeliveryPartnerSettlementReport.class);
                startActivity(intent);
            }
        });

        deliveryStatusTransactionLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DeliverySlotsListForTransactionReport.class);
                startActivity(intent);
            }
        });



        delivered_orders_timewiseReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DeliveredOrdersTimewiseReport.class);
                startActivity(intent);
            }
        });
        changeDeliverySlotdetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChangeDelivery_Slot_Availability_Status.class);
                startActivity(intent);
            }
        });

        searchOrdersUsingMobileNumbers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, searchOrdersUsingMobileNumber.class);
                startActivity(intent);
            }
        });


        posOrdersList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, Pos_Orders_List.class);
                startActivity(intent);
            }
        });


        changeMenuItemVisibilityinTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //trimCache(mContext);
                Intent intent = new Intent(mContext, ChangeMenuItem_Availabilty_InTV_Settings.class);
                startActivity(intent);

            }
        });


        AppSalesReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, App_Sales_Report_Subctgywise.class);
                mContext.startActivity(i);
            }
        });
//AppSales_Report
//App_Sales_Report_Subctgywise


        PosSalesReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, PosSalesReport.class);
                mContext.startActivity(i);
            }
        });


        consolidatedSalesReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openConsolidatedSalesReportActivity();
            }
        });


        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getConfirmationtoLogout();
            }
        });


        changeMenuItemAvail_allowNegativeStock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChangeMenuItemStatus_AllowNegativeStock_Settings.class);
                startActivity(intent);


            }
        });




        changeMenuItemStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChangeMenuItemStatus_Settings.class);
                startActivity(intent);


            }
        });
        changeMenuItemPrice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MenuItem_List_Settings.class);
                intent.putExtra("ClickedOn", "ChangeMenuItemPrice");
                startActivity(intent);


            }
        });




        changeMenuItemPrice_weight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MenuItem_List_Settings.class);
                intent.putExtra("ClickedOn", "ChangeMenuItemPriceAndWeight");
                startActivity(intent);


            }
        });




        resetTokenNoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getConfirmationtoResetTokenNo(vendorkey);
            }
        });


    }

    @SuppressLint("Range")
    private void ConnectSqlDb() {
        Adjusting_Widgets_Visibility(true);

        tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(getActivity());
        try{
            tmcMenuItemSQL_db_manager.open();
        }
        catch (Exception e){
            e.printStackTrace();
        }
      /*  try{
            tmcMenuItemSQL_db_manager.dropTable(true);
        }
        catch (Exception e){
            e.printStackTrace();
        }

       */
        try{
            if(tmcMenuItemSQL_db_manager.deleteTable(true)>=0){
                tmcMenuItemSQL_db_manager.open();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            getMenuItemusingStoreId(vendorkey);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    /*

        try{
            Cursor cursor = tmcMenuItemSQL_db_manager.getData("2110");
            if(cursor.moveToFirst()) {
                do {
                    Modal_MenuItem modal_menuItem = new Modal_MenuItem();
                    modal_menuItem.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                    modal_menuItem.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                    modal_menuItem.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                    modal_menuItem.setLocalDB_id(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.localDB_id)));
                    modal_menuItem.setApplieddiscountpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.applieddiscountpercentage)));
                    modal_menuItem.setBarcode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode)));
                    modal_menuItem.setCheckoutimageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.checkoutimageurl)));
                    modal_menuItem.setDisplayno(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.displayno)));
                    modal_menuItem.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                    modal_menuItem.setGstpercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.gstpercentage)));
                    modal_menuItem.setImageurl(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.imageurl)));
                    modal_menuItem.setItemavailability(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability)));
                    modal_menuItem.setItemcalories(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemcalories)));
                    modal_menuItem.setItemuniquecode(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemuniquecode)));
                    modal_menuItem.setMarinadelinkedcodes(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.marinadelinkedcodes)));
                    modal_menuItem.setMenuboarddisplayname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuboarddisplayname)));
                    modal_menuItem.setMenutype(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menutype)));
                    modal_menuItem.setNetweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.netweight)));
                    modal_menuItem.setPortionsize(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.portionsize)));
                    modal_menuItem.setPricetypeforpos(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.pricetypeforpos)));
                    modal_menuItem.setShowinmenuboard(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.showinmenuboard)));
                    modal_menuItem.setTmcctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcctgykey)));
                    modal_menuItem.setTmcpriceperkg(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkg)));
                    modal_menuItem.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                    modal_menuItem.setTmcsubctgykey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcsubctgykey)));
                    modal_menuItem.setVendorkey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey)));
                    modal_menuItem.setVendorname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorname)));
                    modal_menuItem.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                    modal_menuItem.setItemname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemName)));
                    modal_menuItem.setTmcprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcPrice)));
                    modal_menuItem.setMenuItemId(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));
                    modal_menuItem.setGrossweight(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweight)));
                    modal_menuItem.setGrossweightingrams(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.grossweightingrams)));
                    modal_menuItem.setReportname(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.reportname)));
                    modal_menuItem.setSwiggyprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.swiggyprice)));
                    modal_menuItem.setDunzoprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.dunzoprice)));
                    modal_menuItem.setBigbasketprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.bigbasketprice)));
                    modal_menuItem.setWholesaleprice(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.wholesaleprice)));
                    modal_menuItem.setAppmarkuppercentage(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.appmarkuppercentage)));
                    modal_menuItem.setTmcpriceperkgWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceperkgWithMarkupValue)));
                    modal_menuItem.setTmcpriceWithMarkupValue(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.tmcpriceWithMarkupValue)));
                    modal_menuItem.setKey(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId)));


                    if (!isinventorycheck) {

                        String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                        modal_menuItem.setBarcode_AvlDetails(barcode_AvlDetails);
                        modal_menuItem.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                        modal_menuItem.setKey_AvlDetails(key_AvlDetails);
                        modal_menuItem.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                        modal_menuItem.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                        modal_menuItem.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                        modal_menuItem.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                        modal_menuItem.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                        modal_menuItem.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                        modal_menuItem.setAllownegativestock(allownegativestock_AvlDetails);


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

                        modal_menuItem.setBarcode_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.barcode_AvlDetails)));
                        modal_menuItem.setItemavailability_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.itemavailability_AvlDetails)));
                        modal_menuItem.setKey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.key_AvlDetails)));
                        modal_menuItem.setLastupdatedtime_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.lastupdatedtime_AvlDetails)));
                        modal_menuItem.setMenuitemkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.menuItemId_AvlDetails)));
                        modal_menuItem.setReceivedstock_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.receivedStock_AvlDetails)));
                        modal_menuItem.setStockbalance_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockBalance_AvlDetails)));
                        modal_menuItem.setStockincomingkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.stockIncomingKey_AvlDetails)));
                        modal_menuItem.setVendorkey_AvlDetails(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.vendorkey_AvlDetails)));
                        modal_menuItem.setAllownegativestock(cursor.getString(cursor.getColumnIndex(TMCMenuItemSQL_DB_Manager.allowNegativeStock_AvlDetails)));


                    }
                    Log.i(Constants.TAG,"Cache Memory getItemavailability_AvlDetails  :"+modal_menuItem.getItemavailability_AvlDetails());
                    Log.i(Constants.TAG,"Cache Memory getItemavailability  :"+modal_menuItem.getItemavailability());
                    Log.i(Constants.TAG,"Cache Memory  getAllownegativestock :"+modal_menuItem.getAllownegativestock());
                    Log.i(Constants.TAG,"Cache Memory getTmcpriceperkgWithMarkupValue  :"+modal_menuItem.getTmcpriceperkgWithMarkupValue());
                    Log.i(Constants.TAG,"Cache Memory getTmcpriceWithMarkupValue  :"+modal_menuItem.getTmcpriceWithMarkupValue());



                }
                while (cursor.moveToNext());


            }

        }
        catch (Exception  e){
            e.printStackTrace();
        }

*/
    }



    private String getMenuItemusingStoreId(String vendorKey) {
        MenuList.clear();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItems+"?storeid="+vendorKey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try{


                    try {

                        JSONArray JArray = response.getJSONArray("content");

                        int i1 = 0;
                        int arrayLength = JArray.length();


                        for (; i1 < (arrayLength); i1++) {
                            boolean showinPOS = false;

                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_MenuItem modal_menuItem = new Modal_MenuItem();

                                try {
                                    if (json.has("showinpos")) {
                                        String showinPOS_String = "";
                                        showinPOS_String = String.valueOf(json.get("showinpos").toString().toUpperCase());
                                        if (showinPOS_String.equals("")) {
                                            showinPOS = true;

                                        }
                                        else if (showinPOS_String.equals("TRUE")) {
                                            showinPOS = true;
                                        }
                                        else if (showinPOS_String.equals("FALSE")) {
                                            showinPOS = false;

                                        }
                                        else if (showinPOS_String.equals("NULL")) {
                                            showinPOS = true;
                                        }
                                    } else {
                                        showinPOS = true;


                                    }
                                }
                                catch (Exception e){
                                    showinPOS = true;

                                    e.printStackTrace();
                                }

                                if (showinPOS) {

                                    if (json.has("key")) {
                                        modal_menuItem.key = String.valueOf(json.get("key"));

                                    } else {
                                        modal_menuItem.key = "";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }
                                    if (json.has("appmarkuppercentage")) {
                                        modal_menuItem.appmarkuppercentage = String.valueOf(json.get("appmarkuppercentage"));
                                    } else {
                                        modal_menuItem.appmarkuppercentage = "0";
                                        ////Log.d(Constants.TAG, "There is no key for this Menu: " );


                                    }


                                    if (json.has("applieddiscountpercentage")) {
                                        modal_menuItem.applieddiscountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                                    } else {
                                        modal_menuItem.applieddiscountpercentage = "0";
                                        //Log.d(Constants.TAG, "There is no applieddiscountpercentage for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("showinapp")) {
                                        modal_menuItem.showinapp = String.valueOf(json.get("showinapp").toString().toUpperCase());

                                    } else {
                                        modal_menuItem.showinapp = "TRUE";
                                        //Log.d(Constants.TAG, "There is no showinapp for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("barcode")) {
                                        modal_menuItem.barcode = String.valueOf(json.get("barcode"));

                                    } else {
                                        modal_menuItem.barcode = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }



                                    if (json.has("marinadelinkedcodes")) {
                                        modal_menuItem.marinadelinkedcodes = String.valueOf(json.get("marinadelinkedcodes"));

                                    } else {
                                        modal_menuItem.marinadelinkedcodes = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("vendorkey")) {
                                        modal_menuItem.vendorkey = String.valueOf(json.get("vendorkey"));

                                    } else {
                                        modal_menuItem.vendorkey = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("imageurl")) {
                                        modal_menuItem.imageurl = String.valueOf(json.get("imageurl"));

                                    } else {
                                        modal_menuItem.imageurl = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("menutype")) {
                                        modal_menuItem.menutype = String.valueOf(json.get("menutype"));

                                    } else {
                                        modal_menuItem.menutype = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }


                                    if (json.has("itemcalories")) {
                                        modal_menuItem.itemcalories = String.valueOf(json.get("itemcalories"));

                                    } else {
                                        modal_menuItem.itemcalories = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }

                                    if (json.has("vendorname")) {
                                        modal_menuItem.vendorname = String.valueOf(json.get("vendorname"));

                                    } else {
                                        modal_menuItem.vendorname = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }

                                    if (json.has("checkoutimageurl")) {
                                        modal_menuItem.checkoutimageurl = String.valueOf(json.get("checkoutimageurl"));

                                    } else {
                                        modal_menuItem.checkoutimageurl = "";
                                        //Log.d(Constants.TAG, "There is no barcode for this Menu: " + MenuItemKey);
                                    }




                                    if (json.has("swiggyprice")) {
                                        modal_menuItem.swiggyprice = String.valueOf(json.get("swiggyprice"));
                                        if (String.valueOf(json.get("swiggyprice")).contains("\r")) {

                                            modal_menuItem.swiggyprice = String.valueOf(json.get("swiggyprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getSwiggyprice()).equals("")) {
                                            modal_menuItem.swiggyprice = "0";

                                        }


                                    } else {
                                        modal_menuItem.swiggyprice = "0";
                                        //Log.d(Constants.TAG, "There is no swiggyprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("bigbasketprice")) {
                                        modal_menuItem.bigbasketprice = String.valueOf(json.get("bigbasketprice"));
                                        if (String.valueOf(json.get("bigbasketprice")).contains("\r")) {

                                            modal_menuItem.bigbasketprice = String.valueOf(json.get("bigbasketprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getBigbasketprice()).equals("")) {
                                            modal_menuItem.bigbasketprice = "0";

                                        }
                                    } else {
                                        modal_menuItem.bigbasketprice = "0";
                                        //Log.d(Constants.TAG, "There is no bigbasketprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("dunzoprice")) {
                                        modal_menuItem.dunzoprice = String.valueOf(json.get("dunzoprice"));
                                        if (String.valueOf(json.get("dunzoprice")).contains("\r")) {

                                            modal_menuItem.dunzoprice = String.valueOf(json.get("dunzoprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getDunzoprice()).equals("")) {
                                            modal_menuItem.dunzoprice = "0";

                                        }
                                    } else {
                                        modal_menuItem.dunzoprice = "0";
                                        //Log.d(Constants.TAG, "There is no dunzoprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("wholesaleprice")) {
                                        modal_menuItem.wholesaleprice = String.valueOf(json.get("wholesaleprice"));
                                        if (String.valueOf(json.get("wholesaleprice")).contains("\r")) {

                                            modal_menuItem.wholesaleprice = String.valueOf(json.get("wholesaleprice")).replaceAll("\\r\\n|\\r|\\n", "");
                                            ;

                                        }
                                        if (String.valueOf(modal_menuItem.getWholesaleprice()).equals("")) {
                                            modal_menuItem.wholesaleprice = "0";

                                        }
                                    } else {
                                        modal_menuItem.wholesaleprice = "0";
                                        //Log.d(Constants.TAG, "There is no wholesaleprice for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("displayno")) {
                                        modal_menuItem.displayno = String.valueOf(json.get("displayno"));

                                    } else {
                                        modal_menuItem.displayno = "";
                                        //Log.d(Constants.TAG, "There is no displayno for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("gstpercentage")) {
                                        modal_menuItem.gstpercentage = String.valueOf(json.get("gstpercentage"));

                                    } else {
                                        modal_menuItem.gstpercentage = "";
                                        //Log.d(Constants.TAG, "There is no gstpercentage for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("itemavailability")) {
                                        modal_menuItem.itemavailability = String.valueOf(json.get("itemavailability"));

                                    } else {
                                        modal_menuItem.itemavailability = "";
                                        //Log.d(Constants.TAG, "There is no itemavailability for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("itemname")) {
                                        modal_menuItem.itemname = String.valueOf(json.get("itemname"));

                                    } else {
                                        modal_menuItem.itemname = "";
                                        //Log.d(Constants.TAG, "There is no ItemName for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("reportname")) {
                                        modal_menuItem.reportname = String.valueOf(json.get("reportname"));

                                    } else {
                                        modal_menuItem.reportname = "";
                                        ////Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " +MenuItemKey );


                                    }

                                    if (json.has("pricetypeforpos")) {
                                        modal_menuItem.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                                    } else {
                                        modal_menuItem.pricetypeforpos = "";
                                        //Log.d(Constants.TAG, "There is no pricetypeforpos for this Menu: " + MenuItemKey);


                                    }



                                    ////// for TMCPrice
                                    try{
                                        double tmcprice_double =0 ,  tmcpriceWithAppMarkupValue = 0 , appMarkupPercn_value =0;
                                        try {
                                            if (json.has("tmcprice")) {
                                                tmcprice_double = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                                            } else {
                                                tmcprice_double = 0;
                                                ////Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            tmcprice_double =0;
                                        }
                                        if(json.has("appmarkuppercentage")) {
                                            int markupPercentageInt = 0;
                                            try {
                                                markupPercentageInt = Integer.parseInt(String.valueOf(json.get("appmarkuppercentage")));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (markupPercentageInt > 0) {

                                                try {
                                                    appMarkupPercn_value = (markupPercentageInt * tmcprice_double) / 100;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    tmcpriceWithAppMarkupValue = appMarkupPercn_value + tmcprice_double;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                modal_menuItem.tmcpriceWithMarkupValue = String.valueOf((int) Double.parseDouble(String.valueOf(Math.ceil(tmcpriceWithAppMarkupValue))));
                                            }
                                            else{
                                                modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);

                                            }
                                        }
                                        else{
                                            modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);

                                        }
                                    }
                                    catch (Exception e){

                                        try {

                                            double tmcprice_double = 0;
                                            tmcprice_double = Double.parseDouble(String.valueOf(json.get("tmcprice")));

                                            modal_menuItem.tmcpriceWithMarkupValue = String.valueOf(tmcprice_double);
                                        }
                                        catch (Exception er) {
                                            er.printStackTrace();
                                        }
                                        e.printStackTrace();
                                    }
                                    ////// for TMCPrice PerKG

                                    try{
                                        double tmcpriceperkg_double =0 ,  tmcpriceperkgWithAppMarkupValue = 0 , appMarkupPercn_value =0;
                                        try {
                                            if (json.has("tmcpriceperkg")) {
                                                tmcpriceperkg_double = Double.parseDouble(String.valueOf(json.get("tmcpriceperkg")));

                                            } else {
                                                tmcpriceperkg_double = 0;
                                                ////Log.d(Constants.TAG, "There is no tmcprice for this Menu: " +MenuItemKey );
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            tmcpriceperkg_double =0;
                                        }
                                        if(json.has("appmarkuppercentage")) {
                                            int markupPercentageInt = 0;
                                            try {
                                                markupPercentageInt = Integer.parseInt(String.valueOf(json.get("appmarkuppercentage")));

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            if (markupPercentageInt > 0) {

                                                try {
                                                    appMarkupPercn_value = (markupPercentageInt * tmcpriceperkg_double) / 100;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    tmcpriceperkgWithAppMarkupValue = appMarkupPercn_value + tmcpriceperkg_double;
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf((int) Double.parseDouble(String.valueOf(Math.round(tmcpriceperkgWithAppMarkupValue))));
                                            }
                                            else{
                                                modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);

                                            }
                                        }
                                        else{
                                            modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);

                                        }
                                    }
                                    catch (Exception e){
                                        try {
                                            double tmcpriceperkg_double = 0;
                                            tmcpriceperkg_double = Double.parseDouble(String.valueOf(json.get("tmcpriceperkg")));
                                            modal_menuItem.tmcpriceperkgWithMarkupValue = String.valueOf(tmcpriceperkg_double);
                                        }

                                        catch (Exception er){

                                            er.printStackTrace();
                                        }



                                        e.printStackTrace();

                                    }



                                    if (json.has("itemuniquecode")) {
                                        modal_menuItem.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                                    } else {
                                        modal_menuItem.itemuniquecode = "";
                                        //Log.d(Constants.TAG, "There is no itemuniquecode for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("menuboarddisplayname")) {
                                        modal_menuItem.menuboarddisplayname = String.valueOf(json.get("menuboarddisplayname"));

                                    } else {
                                        modal_menuItem.menuboarddisplayname = "";
                                        //Log.d(Constants.TAG, "There is no menuboarddisplayname for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("showinmenuboard")) {
                                        modal_menuItem.showinmenuboard = String.valueOf(json.get("showinmenuboard"));

                                    } else {
                                        modal_menuItem.showinmenuboard = "";
                                        //Log.d(Constants.TAG, "There is no showinmenuboard for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("grossweight")) {
                                        modal_menuItem.grossweight = String.valueOf(json.get("grossweight"));

                                    } else {
                                        modal_menuItem.grossweight = "";
                                        //Log.d(Constants.TAG, "There is no grossweight for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("grossweightingrams")) {
                                        modal_menuItem.grossweightingrams = String.valueOf(json.get("grossweightingrams"));

                                    } else {
                                        modal_menuItem.grossweightingrams = "";
                                        //Log.d(Constants.TAG, "There is no grossweightingrams for this Menu: " + MenuItemKey);


                                    }


                                    if (json.has("netweight")) {
                                        modal_menuItem.netweight = String.valueOf(json.get("netweight"));

                                    } else {
                                        modal_menuItem.netweight = "";
                                        //Log.d(Constants.TAG, "There is no netweight for this Menu: ");


                                    }
                                    if (json.has("portionsize")) {
                                        modal_menuItem.portionsize = String.valueOf(json.get("portionsize"));

                                    } else {
                                        modal_menuItem.portionsize = "";
                                        //Log.d(Constants.TAG, "There is no portionsize for this Menu: ");


                                    }


                                    if (json.has("tmcctgykey")) {
                                        modal_menuItem.tmcctgykey = String.valueOf(json.get("tmcctgykey"));

                                    } else {
                                        modal_menuItem.tmcctgykey = "";
                                        //Log.d(Constants.TAG, "There is no tmcctgykey for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("tmcprice")) {
                                        modal_menuItem.tmcprice = String.valueOf(json.get("tmcprice"));

                                    } else {
                                        modal_menuItem.tmcprice = "";
                                        //Log.d(Constants.TAG, "There is no tmcprice for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("tmcpriceperkg")) {
                                        modal_menuItem.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));

                                    } else {
                                        modal_menuItem.tmcpriceperkg = "";
                                        //Log.d(Constants.TAG, "There is no tmcpriceperkg for this Menu: " + MenuItemKey);


                                    }
                                    if (json.has("tmcsubctgykey")) {
                                        modal_menuItem.tmcsubctgykey = String.valueOf(json.get("tmcsubctgykey"));

                                    } else {
                                        modal_menuItem.tmcsubctgykey = "";
                                        //Log.d(Constants.TAG, "There is no tmcsubctgykey for this Menu: " + MenuItemKey);


                                    }

                                    if (json.has("key")) {
                                        modal_menuItem.menuItemId = String.valueOf(json.get("key"));

                                    } else {
                                        modal_menuItem.menuItemId = "";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }

                                    if (json.has("itemweightdetails")) {
                                        try {
                                            modal_menuItem.itemweightdetails = String.valueOf(json.get("itemweightdetails"));

                                        } catch (Exception e) {
                                            modal_menuItem.itemweightdetails = "nil";

                                            e.printStackTrace();
                                        }

                                    } else {
                                        modal_menuItem.itemweightdetails = "nil";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }


                                    if (json.has("itemcutdetails")) {
                                        try {
                                            modal_menuItem.itemcutdetails = String.valueOf(json.get("itemcutdetails"));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_menuItem.itemcutdetails = "nil";

                                        }

                                    } else {
                                        modal_menuItem.itemcutdetails = "nil";
                                        //Log.d(Constants.TAG, "There is no key for this Menu: ");


                                    }

                                    if (json.has("inventorydetails")) {
                                        try {
                                            modal_menuItem.inventorydetails = String.valueOf(json.get("inventorydetails"));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_menuItem.inventorydetails = "nil";

                                        }

                                    } else {
                                        modal_menuItem.inventorydetails = "nil";
                                        //Log.d(Constants.TAG, "There is no inventorydetails for this Menu: ");

                                    }

                                    String barcode_AvlDetails = "nil", itemavailability_AvlDetails = "nil", key_AvlDetails = "nil", lastupdatedtime_AvlDetails = "nil", menuitemkey_AvlDetails = "nil",
                                            receivedstock_AvlDetails = "nil", stockbalance_AvlDetails = "nil", stockincomingkey_AvlDetails = "nil", vendorkey_AvlDetails = "nil", allownegativestock_AvlDetails = "nil";


                                    modal_menuItem.setBarcode_AvlDetails(barcode_AvlDetails);
                                    modal_menuItem.setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                    modal_menuItem.setKey_AvlDetails(key_AvlDetails);
                                    modal_menuItem.setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                    modal_menuItem.setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                    modal_menuItem.setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                    modal_menuItem.setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                    modal_menuItem.setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                    modal_menuItem.setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                    modal_menuItem.setAllownegativestock(allownegativestock_AvlDetails);



                                    // localDBcheck = false;
                                    if(localDBcheck) {


                                        try {
                                            if (tmcMenuItemSQL_db_manager == null) {
                                                tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(mContext);
                                                try {
                                                    tmcMenuItemSQL_db_manager.open();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            long id = tmcMenuItemSQL_db_manager.insert(modal_menuItem);
                                            modal_menuItem.setLocalDB_id(String.valueOf(id));
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        finally {
                                            try {
                                                if(arrayLength - i1 ==1) {

                                                    if (tmcMenuItemSQL_db_manager != null) {
                                                        tmcMenuItemSQL_db_manager.close();
                                                        tmcMenuItemSQL_db_manager = null;
                                                    }
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                    MenuList.add(modal_menuItem);
                                    //Log.d(Constants.TAG, "convertingJsonStringintoArray menuListFull: " + MenuList);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                //Log.d(Constants.TAG, "e: " + e.getMessage());
                                //Log.d(Constants.TAG, "e: " + e.toString());

                            }

                            if(arrayLength - i1 ==1){
                                     getMenuAvlDetailsUsingVendorkey(vendorKey);


                                if(localDBcheck) {
                                    SharedPreferences sharedPreferences
                                            = mContext.getSharedPreferences("SqlDbSyncDetails",
                                            MODE_PRIVATE);

                                    SharedPreferences.Editor myEdit
                                            = sharedPreferences.edit();
                                    myEdit.putString(
                                            "menuitem_SqlDb_SyncTime",
                                            getDate_and_time_newFormat()
                                    );



                                    myEdit.apply();

                                }

                            }
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }catch(Exception e){
                    e.printStackTrace();
                }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                SharedPreferences preferences = mContext.getSharedPreferences("MenuList",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                if (error instanceof TimeoutError) {
                    errorCode = "Timeout Error";
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



                Toast.makeText(mContext,"Error in Getting  Menu Item error code :  "+errorCode,Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid",vendorKey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

        return "";
    }



    private String  getMenuAvlDetailsUsingVendorkey(String vendorKey) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofMenuItemStockAvlDetails+vendorKey,
                null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try{

                    JSONArray JArray = response.getJSONArray("content");




                    for(int i =0; i<MenuList.size();i++) {
                        Modal_MenuItem modal_menuItem = MenuList.get(i);
                        String menuitemKeyFromArray ="";
                        menuitemKeyFromArray = modal_menuItem.getKey();



                        String barcode_AvlDetails ="",itemavailability_AvlDetails="",key_AvlDetails="",lastupdatedtime_AvlDetails="",menuitemkey_AvlDetails="",
                                receivedstock_AvlDetails="",stockbalance_AvlDetails="",stockincomingkey_AvlDetails="",vendorkey_AvlDetails="",allownegativestock_AvlDetails="";



                        //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                        int i1 = 0;
                        int arrayLength = JArray.length();
                        //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                        for (; i1 < (arrayLength); i1++) {


                            try {
                                JSONObject json = JArray.getJSONObject(i1);
                                if(json.has("menuitemkey")){
                                    String menuItemKeyFromMenuAvlDetails = json.getString("menuitemkey");

                                    if(menuItemKeyFromMenuAvlDetails.equals(menuitemKeyFromArray)){
                                        if(json.has("barcode")){
                                            barcode_AvlDetails = json.getString("barcode");



                                        }
                                        else{
                                            barcode_AvlDetails ="";
                                        }

                                        if(json.has("itemavailability")){
                                            itemavailability_AvlDetails = json.getString("itemavailability");



                                        }
                                        else{
                                            itemavailability_AvlDetails ="";
                                        }


                                        if(json.has("key")){
                                            key_AvlDetails = json.getString("key");



                                        }
                                        else{
                                            key_AvlDetails ="";
                                        }



                                        if(json.has("lastupdatedtime")){
                                            lastupdatedtime_AvlDetails = json.getString("lastupdatedtime");



                                        }
                                        else{
                                            lastupdatedtime_AvlDetails ="";
                                        }


                                        if(json.has("menuitemkey")){
                                            menuitemkey_AvlDetails = json.getString("menuitemkey");



                                        }
                                        else{
                                            menuitemkey_AvlDetails ="";
                                        }


                                        if(json.has("receivedstock")){
                                            receivedstock_AvlDetails = json.getString("receivedstock");



                                        }
                                        else{
                                            receivedstock_AvlDetails ="";
                                        }


                                        if(json.has("stockbalance")){
                                            stockbalance_AvlDetails = json.getString("stockbalance");



                                        }
                                        else{
                                            stockbalance_AvlDetails ="";
                                        }


                                        if(json.has("stockincomingkey")){
                                            stockincomingkey_AvlDetails = json.getString("stockincomingkey");



                                        }
                                        else{
                                            stockincomingkey_AvlDetails ="";
                                        }


                                        if(json.has("vendorkey")){
                                            vendorkey_AvlDetails = json.getString("vendorkey");



                                        }
                                        else{
                                            vendorkey_AvlDetails ="";
                                        }

                                        if(json.has("allownegativestock")){
                                            allownegativestock_AvlDetails = json.getString("allownegativestock");



                                        }
                                        else{
                                            allownegativestock_AvlDetails ="";
                                        }

                                        MenuList.get(i).setBarcode_AvlDetails(barcode_AvlDetails);
                                        MenuList.get(i).setItemavailability_AvlDetails(itemavailability_AvlDetails);
                                        MenuList.get(i).setKey_AvlDetails(key_AvlDetails);
                                        MenuList.get(i).setLastupdatedtime_AvlDetails(lastupdatedtime_AvlDetails);
                                        MenuList.get(i).setMenuitemkey_AvlDetails(menuitemkey_AvlDetails);
                                        MenuList.get(i).setReceivedstock_AvlDetails(receivedstock_AvlDetails);
                                        MenuList.get(i).setStockbalance_AvlDetails(stockbalance_AvlDetails);
                                        MenuList.get(i).setStockincomingkey_AvlDetails(stockincomingkey_AvlDetails);
                                        MenuList.get(i).setVendorkey_AvlDetails(vendorkey_AvlDetails);
                                        MenuList.get(i).setAllownegativestock(allownegativestock_AvlDetails);

                                        if(localDBcheck) {
                                            try {
                                                try {

                                                    if (tmcMenuItemSQL_db_manager == null) {
                                                        tmcMenuItemSQL_db_manager = new TMCMenuItemSQL_DB_Manager(mContext);
                                                        try {
                                                            tmcMenuItemSQL_db_manager.open();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }


                                                try {
                                                    int result = tmcMenuItemSQL_db_manager.update(MenuList.get(i));
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
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
                                }
                                else{
                                    Toast.makeText(mContext, "There is no menuItemkey for an Item", Toast.LENGTH_SHORT).show();

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }



                        if (MenuList.size() - i ==1){
                            saveMenuIteminSharedPreference(MenuList);
                            saveMenuItemStockAvlDetailsinSharedPreference(MenuItemStockAvlDetails);

                                try {
                                    if (tmcMenuItemSQL_db_manager != null) {
                                        tmcMenuItemSQL_db_manager.close();
                                        tmcMenuItemSQL_db_manager = null;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                        }


                    }

                }catch(Exception e){
                    e.printStackTrace();
                }




            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(TAG, "Error: " + error.getMessage());
                //Log.d(TAG, "Error: " + error.toString());

                MenuList.clear();
                SharedPreferences preferences =mContext.getSharedPreferences("MenuList",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                if (error instanceof TimeoutError) {
                    errorCode = "Timeout Error";
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
                Toast.makeText(mContext,"Error in Getting MenuList : error code :  "+errorCode,Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("storeid",vendorKey);

                return params;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


        return "";


    }


    private void saveMenuIteminSharedPreference(List<Modal_MenuItem> menuList) {
        try {


            final SharedPreferences sharedPreferences = mContext.getSharedPreferences("MenuList", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(menuList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("MenuList", json);
            editor.apply();


            Adjusting_Widgets_Visibility(false);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void saveMenuItemStockAvlDetailsinSharedPreference(List<Modal_MenuItemStockAvlDetails> menuItemStockAvlDetails) {

        try {
            final SharedPreferences sharedPreferences = mContext.getSharedPreferences("MenuItemStockAvlDetails", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(menuItemStockAvlDetails);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("MenuItemStockAvlDetails", json);
            editor.apply();

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }



    public static String getDate_and_time_newFormat()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String CurrentDate_time = df.format(c);
        return CurrentDate_time;
    }



    private void setSettingsUIBasedONManagementRole() {
        dataAnalyticsLinearLayout.setVisibility(GONE);
        testlayout.setVisibility(GONE);
        managemenuLayout.setVisibility(VISIBLE);
        temporaryLayout .setVisibility(GONE);
        manageordersLinearLayout.setVisibility(VISIBLE);
        editPaymentModeOftheOrder.setVisibility(GONE);
        posOrdersList.setVisibility(GONE);
        searchOrdersUsingMobileNumbers.setVisibility(GONE);
        phone_orders_list.setVisibility(GONE);
        add_refund_replace_order_layout.setVisibility(GONE);
        resetTokenNoLayout .setVisibility(GONE);
        mobilenowisecreditOrderslist.setVisibility(GONE);
        if(vendorkey.equals("vendor_5")){
            manageordersLinearLayout.setVisibility(GONE);
            posOrdersList.setVisibility(VISIBLE);
            searchOrdersUsingMobileNumbers.setVisibility(VISIBLE);
            managemenuLayout.setVisibility(GONE);
            slotwiseAppOrderList.setVisibility(GONE);
            cancelledOrdersLayout.setVisibility(GONE);
            plotOrdersLocation_layout.setVisibility(GONE);
            orderRating_report.setVisibility(GONE);
            phone_sales_report.setVisibility(GONE);
            deliveryPartnerSettlementReport.setVisibility(GONE);
            delivered_orders_timewiseReport.setVisibility(GONE);
            replacement_refund_transaction_reportLayout.setVisibility(GONE);

        }
    }


    private void ShowOrHideUI_AccordingTo_VendorType() {




    }

    private void ShowOrHideUI_AccordingTo_UserPhoneNumber() {

        if (UserPhoneNumber.equals("+9195975808") || UserPhoneNumber.equals("+917010779096")) {
            changeMenuItemAvail_allowNegativeStock.setVisibility(VISIBLE);
            testlayout.setVisibility(VISIBLE);
            changeMenuItemPrice_weight.setVisibility(GONE);
            changeMenuItemAvail_allowNegativeStock.setVisibility(VISIBLE);
            changeMenuItemStatus.setVisibility(VISIBLE);
            generateUserDetailsButton.setVisibility(VISIBLE);
        }



        //Ashwanth   918110884808
        if ((UserPhoneNumber.equals("+9195975808")) ) {

            viewordersLinearLayout.setVisibility(VISIBLE);


        }
        //Navaneedhan
        //Vimal

        if ((UserPhoneNumber.equals("+916383677365")) || (UserPhoneNumber.equals("")) ) {
            managemenuLayout.setVisibility(VISIBLE);
            changeDeliverySlotdetails.setVisibility(VISIBLE);
            changeMenuItemStatus.setVisibility(GONE);
            if(!isinventorycheck){
                changeMenuItemAvail_allowNegativeStock.setVisibility(GONE);
                changeMenuItemStatus.setVisibility(VISIBLE);
            }
            else{
                changeMenuItemAvail_allowNegativeStock.setVisibility(VISIBLE);
                changeMenuItemStatus.setVisibility(GONE);
            }
            changeMenuItemPrice.setVisibility(GONE);
            changeMenuItemVisibilityinTv.setVisibility(GONE);
            changeMenuItemPrice_weight.setVisibility(GONE);
            menuTransactionDetailsLayout.setVisibility(GONE);
            deliveryPartnerSettlementReport.setVisibility(VISIBLE);

            menuItemAvailabiltyStatusReport.setVisibility(GONE);
            testlayout.setVisibility(GONE);



        }

    }


    @SuppressLint("NonConstantResourceId")
    private void changeSelectedPrinterType(int radiobutton, boolean changeSelectionto) {

        switch(radiobutton){
            case R.id.usbRadiobutton:
                // do operations specific to this selection
                if(changeSelectionto){
                    usbRadiobutton.setSelected(true);
                    usbRadiobutton.setChecked(true);
                    nonePrinterRadiobutton.setChecked(false);
                    nonePrinterRadiobutton.setSelected(false);
                    posPrinterRadiobutton.setSelected(false);
                    posPrinterRadiobutton.setChecked(false);
                    bluetoothPrinterRadiobutton.setSelected(false);
                    bluetoothPrinterRadiobutton.setChecked(false);
                    connectUSBPrinter();
                }
                else{
                    usbRadiobutton.setSelected(false);
                    usbRadiobutton.setChecked(false);
                    nonePrinterRadiobutton.setChecked(true);
                    nonePrinterRadiobutton.setSelected(true);
                    posPrinterRadiobutton.setSelected(false);
                    posPrinterRadiobutton.setChecked(false);
                    bluetoothPrinterRadiobutton.setSelected(false);
                    bluetoothPrinterRadiobutton.setChecked(false);
                    disconnectAllPrinters();
                }


                break;
            case R.id.bluetoothPrinterRadiobutton:
                if(changeSelectionto){
                    bluetoothPrinterRadiobutton.setSelected(true);
                    bluetoothPrinterRadiobutton.setChecked(true);
                    nonePrinterRadiobutton.setChecked(false);
                    nonePrinterRadiobutton.setSelected(false);
                    posPrinterRadiobutton.setSelected(false);
                    posPrinterRadiobutton.setChecked(false);
                    usbRadiobutton.setSelected(false);
                    usbRadiobutton.setChecked(false);
                    connectBluetoothPrinter();
                }
                else{
                    bluetoothPrinterRadiobutton.setSelected(false);
                    bluetoothPrinterRadiobutton.setChecked(false);
                    nonePrinterRadiobutton.setSelected(true);
                    nonePrinterRadiobutton.setChecked(true);
                    posPrinterRadiobutton.setSelected(false);
                    posPrinterRadiobutton.setChecked(false);
                    usbRadiobutton.setSelected(false);
                    usbRadiobutton.setChecked(false);
                    disconnectAllPrinters();

                }
                // do operations specific to this selection
                break;
            case R.id.posPrinterRadiobutton:
                // do operations specific to this selection
                if(changeSelectionto){
                    posPrinterRadiobutton.setSelected(true);
                    posPrinterRadiobutton.setChecked(true);
                    nonePrinterRadiobutton.setChecked(false);
                    nonePrinterRadiobutton.setSelected(false);
                    bluetoothPrinterRadiobutton.setSelected(false);
                    bluetoothPrinterRadiobutton.setChecked(false);
                    usbRadiobutton.setSelected(false);
                    usbRadiobutton.setChecked(false);
                    connectPOSPrinter();
                }
                else{
                    posPrinterRadiobutton.setSelected(false);
                    posPrinterRadiobutton.setChecked(false);
                    nonePrinterRadiobutton.setChecked(true);
                    nonePrinterRadiobutton.setSelected(true);
                    bluetoothPrinterRadiobutton.setSelected(false);
                    bluetoothPrinterRadiobutton.setChecked(false);
                    usbRadiobutton.setSelected(false);
                    usbRadiobutton.setChecked(false);
                    disconnectAllPrinters();

                }
                break;
            case R.id.nonePrinterRadiobutton:
                // do operations specific to this selection
                posPrinterRadiobutton.setSelected(false);
                posPrinterRadiobutton.setChecked(false);
                bluetoothPrinterRadiobutton.setSelected(false);
                bluetoothPrinterRadiobutton.setChecked(false);
                usbRadiobutton.setSelected(false);
                usbRadiobutton.setChecked(false);
                nonePrinterRadiobutton.setChecked(true);
                nonePrinterRadiobutton.setSelected(true);
                disconnectAllPrinters();

                break;
        }

    }

    private void disconnectAllPrinters() {

        try {
            UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(mContext);
            UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);

            if (usbConnection != null || usbManager != null) {
                usbConnection.disconnect();

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            if (!BluetoothPrintDriver.IsNoConnection()) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
                SaveDatainSharedPreferences(false,String.valueOf("None"),"Disconnected","NONE");

    }

    private void connectBluetoothPrinter() {


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

     //   Toast.makeText(mContext, "Bluetooth", Toast.LENGTH_SHORT).show();
    }
    private void connectPOSPrinter() {
        printerConnectionStatus_Textwidget.setText("POS Printer Connected");

        SaveDatainSharedPreferences(true,String.valueOf("POS Machine"),"Connected",Constants.POS_PrinterType);

   //     Toast.makeText(mContext, "POS", Toast.LENGTH_SHORT).show();

    }
    private void connectUSBPrinter() {
        try {
            UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(mContext);
            UsbManager usbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
            try {
                if (usbConnection == null || usbManager == null) {



                    new TMCAlertDialogClass(mContext, R.string.app_name, R.string.ReConnect_Instruction,
                            R.string.OK_Text, R.string.Cancel_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    connectUSBPrinter();

                                }

                                @Override
                                public void onNo() {
                                    changeSelectedPrinterType(usbRadiobutton.getId(),false);
                                    return;
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
            mContext.registerReceiver(usbReceiver_settings, filter);
            usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);

        }
        catch (Exception e){

            e.printStackTrace();
        }
    }


    private void GetOrderItemDetailsAndGenerateSheet() {

        Adjusting_Widgets_Visibility(true);
        progressbarInstruction.setText("Fetching OrderItem Details...");
        OrderItemDetailsTableArray.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getAllOrderItemDetailswithPagenation, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);

                        String jsonString = response.toString();
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray JArray = jsonObject.getJSONArray("content");
                            double tmcprice = 0, totalTmcPrice = 0 , quantity =0;
                            String orderid="",tmcPrice_String ="", totalTmcPrice_String ="", quantity_String ="";
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

                            for (; i1 < (arrayLength); i1++) {

                                JSONObject json = JArray.getJSONObject(i1);

                                ModalOrderItemDetails modalOrderItemDetails = new ModalOrderItemDetails();
                                try{
                                    if(json.has("key")){
                                        modalOrderItemDetails.key = json.getString("key");
                                    }
                                    else{
                                        modalOrderItemDetails.key ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.key ="-";

                                }




                                try{
                                    if(json.has("applieddiscountpercentage")){
                                        modalOrderItemDetails.applieddiscountpercentage = json.getString("applieddiscountpercentage");
                                    }
                                    else{
                                        modalOrderItemDetails.applieddiscountpercentage ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.applieddiscountpercentage ="-";

                                }



                                try{
                                    if(json.has("cutname")){
                                        modalOrderItemDetails.cutname = json.getString("cutname");
                                    }
                                    else{
                                        modalOrderItemDetails.cutname ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.cutname ="-";

                                }


                                try{
                                    if(json.has("cutprice")){
                                        modalOrderItemDetails.cutprice = json.getString("cutprice");
                                    }
                                    else{
                                        modalOrderItemDetails.cutprice ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.cutprice ="-";

                                }


                                try{
                                    if(json.has("discountamount")){
                                        modalOrderItemDetails.discountamount = json.getString("discountamount");
                                    }
                                    else{
                                        modalOrderItemDetails.discountamount ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.discountamount ="-";

                                }


                                try{
                                    if(json.has("grossweightingrams")){
                                        modalOrderItemDetails.grossweightingrams = json.getString("grossweightingrams");
                                    }
                                    else{
                                        modalOrderItemDetails.grossweightingrams ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.grossweightingrams ="-";

                                }


                                try{
                                    if(json.has("gstamount")){
                                        modalOrderItemDetails.gstamount = json.getString("gstamount");
                                    }
                                    else{
                                        modalOrderItemDetails.gstamount ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.gstamount ="-";

                                }


                                try{
                                    if(json.has("itemname")){
                                        modalOrderItemDetails.itemname = json.getString("itemname");
                                    }
                                    else{
                                        modalOrderItemDetails.itemname ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.itemname ="-";

                                }

                                try{
                                    if(json.has("marinadeitemdetails")){
                                        modalOrderItemDetails.marinadeitemdetails = json.getString("marinadeitemdetails");
                                    }
                                    else{
                                        modalOrderItemDetails.marinadeitemdetails ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.marinadeitemdetails ="-";

                                }


                                try{
                                    if(json.has("netweight")){
                                        modalOrderItemDetails.netweight = json.getString("netweight");
                                    }
                                    else{
                                        modalOrderItemDetails.netweight ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.netweight ="-";

                                }

                                try{
                                    if(json.has("orderid")){
                                        modalOrderItemDetails.orderid = json.getString("orderid");
                                        orderid = json.getString("orderid");

                                    }
                                    else{
                                        orderid ="";
                                        modalOrderItemDetails.orderid ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    orderid ="";
                                    modalOrderItemDetails.orderid ="-";

                                }


                                try{
                                    if(json.has("orderplacedtime")){
                                        modalOrderItemDetails.orderplacedtime = json.getString("orderplacedtime");
                                      String time_in_long="";
                                        try{
                                            time_in_long =   getLongValuefortheDate( json.getString("orderplacedtime"));
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
                                        modalOrderItemDetails.orderplacedtimeinLong = time_in_long;
                                    }
                                    else{
                                        modalOrderItemDetails.orderplacedtimeinLong ="";
                                        modalOrderItemDetails.orderplacedtime ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.orderplacedtime ="-";
                                    modalOrderItemDetails.orderplacedtimeinLong ="";

                                }

                                try{
                                    if(json.has("portionsize")){
                                        modalOrderItemDetails.portionsize = json.getString("portionsize");
                                    }
                                    else{
                                        modalOrderItemDetails.portionsize ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.portionsize ="-";

                                }


                                try{
                                    if(json.has("quantity")){
                                        modalOrderItemDetails.quantity = json.getString("quantity");
                                        quantity_String = json.getString("quantity");
                                    }
                                    else{
                                        quantity_String ="1";
                                        modalOrderItemDetails.quantity ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    quantity_String ="1";

                                    modalOrderItemDetails.quantity ="-";

                                }





                                try{
                                    if(json.has("slotdate")){
                                        modalOrderItemDetails.slotdate = json.getString("slotdate");
                                    }
                                    else{
                                        modalOrderItemDetails.slotdate ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.slotdate ="-";

                                }

                                try{
                                    if(json.has("slotname")){
                                        modalOrderItemDetails.slotname = json.getString("slotname");
                                    }
                                    else{
                                        modalOrderItemDetails.slotname ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.slotname ="-";

                                }


                                try{
                                    if(json.has("tmcprice")){
                                        modalOrderItemDetails.tmcprice = json.getString("tmcprice");
                                        tmcPrice_String = json.getString("tmcprice");
                                    }
                                    else{
                                        modalOrderItemDetails.tmcprice ="";
                                        tmcPrice_String ="0";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    tmcPrice_String ="0";
                                    modalOrderItemDetails.tmcprice ="-";

                                }

                                try{
                                    if(json.has("tmcsubctgykey")){
                                        modalOrderItemDetails.tmcsubctgykey = json.getString("tmcsubctgykey");
                                    }
                                    else{
                                        modalOrderItemDetails.tmcsubctgykey ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.tmcsubctgykey ="-";

                                }

                                try{
                                    if(json.has("vendorkey")){
                                        modalOrderItemDetails.vendorkey = json.getString("vendorkey");
                                    }
                                    else{
                                        modalOrderItemDetails.vendorkey ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.vendorkey ="-";

                                }


                                try{
                                    if(json.has("vendorname")){
                                        modalOrderItemDetails.vendorname = json.getString("vendorname");
                                    }
                                    else{
                                        modalOrderItemDetails.vendorname ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
                                    modalOrderItemDetails.vendorname ="-";

                                }

                                try{
                                    tmcprice = Double.parseDouble(tmcPrice_String);
                                }
                                catch (Exception e){
                                    tmcprice =0;
                                    e.printStackTrace();
                                }


                                try{
                                    quantity = Double.parseDouble(quantity_String);
                                }
                                catch (Exception e){
                                    quantity = 0;
                                    e.printStackTrace();
                                }

                                try {
                                    try {
                                        totalTmcPrice = tmcprice * quantity;
                                    } catch (Exception e) {
                                        totalTmcPrice = tmcprice;
                                        e.printStackTrace();
                                    }

                                    try {
                                        totalTmcPrice_String = String.valueOf(totalTmcPrice);
                                    } catch (Exception e) {
                                        totalTmcPrice_String = tmcPrice_String;
                                        e.printStackTrace();
                                    }
                                }
                                catch (Exception e) {
                                    totalTmcPrice_String = tmcPrice_String;

                                    e.printStackTrace();
                                }
                                modalOrderItemDetails.totalTmcPrice = totalTmcPrice_String;
                                if(orderidTotal.containsKey(orderid)){
                                    double totalPrice = 0;
                                    totalPrice = orderidTotal.get(orderid);
                                    totalPrice = totalTmcPrice + totalPrice;
                                    orderidTotal.put(orderid,totalPrice);
                                }
                                else{
                                    orderidTotal.put(orderid,totalTmcPrice);
                                }
                                OrderItemDetailsTableArray.add(modalOrderItemDetails);

                                if(i1==(arrayLength-1)) {
                                    if (OrderItemDetailsTableArray.size() >0) {
                                        try{
                                            Collections.sort(OrderItemDetailsTableArray, new Comparator<ModalOrderItemDetails>() {
                                                public int compare(final ModalOrderItemDetails object1, final ModalOrderItemDetails object2) {
                                                    String tokenNo_1 = object1.getOrderplacedtimeinLong();
                                                    String tokenNo_2 = object2.getOrderplacedtimeinLong();

                                                    if ((tokenNo_1.equals("")) || (tokenNo_1.equals("null")) || (tokenNo_1.equals(null))) {
                                                        tokenNo_1 = String.valueOf(0);
                                                    }
                                                    if ((tokenNo_2.equals("")) || (tokenNo_2.equals("null")) || (tokenNo_2.equals(null))) {
                                                        tokenNo_2 = String.valueOf(0);
                                                    }

                                                    Long i2 = Long.valueOf(tokenNo_2);
                                                    Long i1 = Long.valueOf(tokenNo_1);

                                                    return i1.compareTo(i2);
                                                }
                                            });

                                        }
                                        catch(Exception e){
                                            e.printStackTrace();
                                        }





                                        AskPermissionToGenerateSheet("OrderItemDetailsSheet");
                                    }
                                    else{

                                        Toast.makeText(mContext,"There is no data to Generate Sheet",Toast.LENGTH_LONG).show();



                                    }
                                }
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);
                        }



                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Adjusting_Widgets_Visibility(false);
                error.printStackTrace();
            }
        }) {


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

    private void GetUserTable() {
        Adjusting_Widgets_Visibility(true);
        progressbarInstruction.setText("Fetching User Details...");
        UserTableArray.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getAllUserswithPagenation, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);

                        String jsonString = response.toString();
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray JArray = jsonObject.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (; i1 < (arrayLength); i1++) {

                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_User modalUser = new Modal_User();

                                    if (json.has("authorizationcode")) {
                                        modalUser.authorizationcode = String.valueOf(json.get("authorizationcode"));
                                    } else {
                                        modalUser.authorizationcode = "";
                                    }




                                    if (json.has("fcmtoken")) {
                                        modalUser.fcmtoken = String.valueOf(json.get("fcmtoken"));
                                    } else {
                                        modalUser.fcmtoken = "";
                                    }



                                    if (json.has("createdtime")) {
                                        modalUser.createdtime = String.valueOf(json.get("createdtime"));
                                    } else {
                                        modalUser.createdtime = "";
                                    }




                                    if (json.has("appversion")) {
                                        modalUser.appversion = String.valueOf(json.get("appversion"));
                                    } else {
                                        modalUser.appversion = "";
                                    }



                                    if (json.has("mobileno")) {
                                        modalUser.mobileno = String.valueOf(json.get("mobileno"));
                                    } else {
                                        modalUser.mobileno = "";
                                    }



                                    if (json.has("deviceos")) {
                                        modalUser.deviceos = String.valueOf(json.get("deviceos"));
                                    } else {
                                        modalUser.deviceos = "";
                                    }




                                    if (json.has("email")) {
                                        modalUser.email = String.valueOf(json.get("email"));
                                    } else {
                                        modalUser.email = "";
                                    }




                                    if (json.has("key")) {
                                        modalUser.key = String.valueOf(json.get("key"));
                                    } else {
                                        modalUser.key = "";
                                    }



                                    if (json.has("name")) {
                                        modalUser.name = String.valueOf(json.get("name"));
                                    } else {
                                        modalUser.name = "";
                                    }




                                    if (json.has("updatedtime")) {
                                        modalUser.updatedtime = String.valueOf(json.get("updatedtime"));
                                    } else {
                                        modalUser.updatedtime = "";
                                    }


                                UserTableArray.add(modalUser);
                            if(i1==arrayLength-1){
                                GetAddressTable();

                            }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Adjusting_Widgets_Visibility(false);
                error.printStackTrace();
            }
        }) {


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

    private void GetAddressTable() {
        Adjusting_Widgets_Visibility(true);
        AddressTableArray.clear();
        progressbarInstruction.setText("Fetching Address Details...");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getAllAddresswithPagenation, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);


                        //Log.d(Constants.TAG, "Response: " + response);
                        String jsonString = response.toString();
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONObject jsonObject = new JSONObject(jsonString);
                            JSONArray JArray = jsonObject.getJSONArray("content");
                            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                            int i1 = 0;
                            int arrayLength = JArray.length();
                            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                            for (; i1 < (arrayLength); i1++) {
                                JSONObject json = JArray.getJSONObject(i1);
                                Modal_Address modalAddress = new Modal_Address();


                                    if (json.has("contactpersonmobileno")) {
                                        modalAddress.contactpersonmobileno = String.valueOf(json.get("contactpersonmobileno"));
                                    } else {
                                        modalAddress.contactpersonmobileno = "";
                                    }




                                    if (json.has("addresstype")) {
                                        modalAddress.addresstype = String.valueOf(json.get("addresstype"));
                                    } else {
                                        modalAddress.addresstype = "";
                                    }




                                    if (json.has("pincode")) {
                                        modalAddress.pincode = String.valueOf(json.get("pincode"));
                                    } else {
                                        modalAddress.pincode = "";
                                    }



                                    if (json.has("vendorname")) {
                                        modalAddress.vendorname = String.valueOf(json.get("vendorname"));
                                    } else {
                                        modalAddress.vendorname = "";
                                    }


                                if (json.has("vendorkey")) {
                                    modalAddress.vendorkey = String.valueOf(json.get("vendorkey"));
                                    vendorkey_usertable = String.valueOf(json.get("vendorkey"));
                                } else {
                                    modalAddress.vendorkey = "";
                                }


                                    if (json.has("locationlong")) {
                                        modalAddress.locationlong = String.valueOf(json.get("locationlong"));
                                    } else {
                                        modalAddress.locationlong = "";
                                    }



                                    if (json.has("userkey")) {
                                        modalAddress.userkey = String.valueOf(json.get("userkey"));
                                    } else {
                                        modalAddress.userkey = "";
                                    }



                                    if (json.has("locationlat")) {
                                        modalAddress.locationlat = String.valueOf(json.get("locationlat"));
                                    } else {
                                        modalAddress.locationlat = "";
                                    }




                                    if (json.has("deliverydistance")) {
                                        modalAddress.deliverydistance = String.valueOf(json.get("deliverydistance"));
                                    } else {
                                        modalAddress.deliverydistance = "";
                                    }




                                    if (json.has("vendorkey")) {
                                        modalAddress.vendorkey = String.valueOf(json.get("vendorkey"));
                                    } else {
                                        modalAddress.vendorkey = "";
                                    }

                                if (json.has("landmark")) {
                                    modalAddress.landmark = String.valueOf(json.get("landmark"));
                                } else {
                                    modalAddress.landmark = "";
                                }


                                    if (json.has("key")) {
                                        modalAddress.key = String.valueOf(json.get("key"));
                                    } else {
                                        modalAddress.key = "";
                                    }


                                    if (json.has("addressline1")) {
                                        modalAddress.addressline1 = String.valueOf(json.get("addressline1"));
                                    } else {
                                        modalAddress.addressline1 = "";
                                    }


                                    if (json.has("addressline2")) {
                                        modalAddress.addressline2 = String.valueOf(json.get("addressline2"));
                                    } else {
                                        modalAddress.addressline2 = "";
                                    }


                                    if (json.has("contactpersonname")) {
                                        modalAddress.contactpersonname = String.valueOf(json.get("contactpersonname"));
                                    } else {
                                        modalAddress.contactpersonname = "";
                                    }



                                AddressTableArray.add(modalAddress);

                            }


                            if (AddressTableArray.size() > 0 && UserTableArray.size() > 0) {
                                AskPermissionToGenerateSheet("UserDetailsSheet");
                            }
                            else{

                                Toast.makeText(mContext,"There is no data to Generate Sheet",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Adjusting_Widgets_Visibility(false);
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Adjusting_Widgets_Visibility(false);
                error.printStackTrace();
            }
        }) {


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

    private void AskPermissionToGenerateSheet(String whichsheetoGenerate) {

        whichsheetoGenerate1 = "";
        whichsheetoGenerate1 = whichsheetoGenerate;
        try {
            wb = new HSSFWorkbook();
            //Now we are creating sheet

        } catch (Exception e) {
            e.printStackTrace();
        }



        if (SDK_INT >= Build.VERSION_CODES.R) {

            if(Environment.isExternalStorageManager()){
                try {
                    Adjusting_Widgets_Visibility(true);

                    try {
                        if(whichsheetoGenerate.equals("UserDetailsSheet")){
                            FilterUserAndAddressArray() ;
                        }
                        else if(whichsheetoGenerate.equals("OrderItemDetailsSheet")){
                            AddDatatoOrderItemDetailsExcelSheet();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                }
            }
            else{
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getActivity().getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }

        } else {


            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE);
            //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
            // If do not grant write external storage permission.
            if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
            } else {

                Adjusting_Widgets_Visibility(true);

                try {
                    if(whichsheetoGenerate.equals("UserDetailsSheet")){
                        FilterUserAndAddressArray() ;
                    }
                    else if(whichsheetoGenerate.equals("OrderItemDetailsSheet")){
                        AddDatatoOrderItemDetailsExcelSheet();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

/*

    int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // If do not grant write external storage permission.
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Click Allow and then Generate Again", Toast.LENGTH_SHORT).show();

            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);


        } else {

            Adjusting_Widgets_Visibility(true);

            try {
                if(whichsheetoGenerate.equals("UserDetailsSheet")){
                    FilterUserAndAddressArray() ;
                }
                else if(whichsheetoGenerate.equals("OrderItemDetailsSheet")){
                    AddDatatoOrderItemDetailsExcelSheet();

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

 */
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);


        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                    Adjusting_Widgets_Visibility(true);

                    try {
                        if(whichsheetoGenerate1.equals("UserDetailsSheet")){
                            FilterUserAndAddressArray() ;
                        }
                        else if(whichsheetoGenerate1.equals("OrderItemDetailsSheet")){
                            AddDatatoOrderItemDetailsExcelSheet();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        else {

            if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
                int grantResultsLength = grantResults.length;
                if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "You grant write external storage permission. Please click original button again to continue.", Toast.LENGTH_LONG).show();
                    // exportInvoice();
                    Adjusting_Widgets_Visibility(true);

                    try {
                        if(whichsheetoGenerate1.equals("UserDetailsSheet")){
                            FilterUserAndAddressArray() ;
                        }
                        else if(whichsheetoGenerate1.equals("OrderItemDetailsSheet")){
                            AddDatatoOrderItemDetailsExcelSheet();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, "You denied write external storage permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }





/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

 */

    private void FilterUserAndAddressArray() {
        AddedUserKey.clear();
        FilteredUserTableArray.clear();
        FilteredAddressTableArray.clear();
        for(int i =0; i<AddressTableArray.size();i++) {
            Modal_Address modal_address = AddressTableArray.get(i);
            String vendorFromAddressArray = modal_address.getVendorkey().toString();
            String userkeyFromAddressArray = modal_address.getUserkey().toString();
            String AddresskeyFromAddressArray = modal_address.getKey().toString();

            if(!AddedUserKey.contains(AddresskeyFromAddressArray)) {
                if (vendorFromAddressArray.equals(vendorkey)) {
                    for (int j = 0; j < UserTableArray.size(); j++) {
                        Modal_User modal_user = UserTableArray.get(j);
                        String userKeyFromUserArray = modal_user.getKey().toString();
                        if (userkeyFromAddressArray.equals(userKeyFromUserArray)) {
                            FilteredUserTableArray.add(modal_user);
                            AddedUserKey.add(AddresskeyFromAddressArray);
    
                        }

                    }
                    FilteredAddressTableArray.add(modal_address);
                }
                if (i == (AddressTableArray.size() - 1)) {
                    AddDatatoUserDetailsExcelSheet();

                }
            }
        }
    }

    private void AddDatatoOrderItemDetailsExcelSheet() {
        progressbarInstruction.setText("Generating Sheet ...");
        sheet = wb.createSheet(String.valueOf(System.currentTimeMillis()));
        int rowNum = 1;
        Cell headercell = null;
        if(OrderItemDetailsTableArray.size()>0){
            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(HSSFColor.RED.index);

            CellStyle headerCellStyle = wb.createCellStyle();
            headerCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);


            org.apache.poi.ss.usermodel.Font contentFont = wb.createFont();
            contentFont.setBold(false);
            contentFont.setFontHeightInPoints((short) 10);
            contentFont.setColor(HSSFColor.BLACK.index);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFont(contentFont);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            //Now column and row
            Row headerRow = sheet.createRow(0);

            for (int i = 0; i < columnsHeading_orderItemDetails.length; i++) {
                headercell = headerRow.createCell(i);
                headercell.setCellValue(columnsHeading_orderItemDetails[i]);
                headercell.setCellStyle(headerCellStyle);
            }

            for (int ii = 0; ii < OrderItemDetailsTableArray.size(); ii++) {

                ModalOrderItemDetails itemRow = OrderItemDetailsTableArray.get(ii);

                Log.d(Constants.TAG, "prepareDataForExcelSheet type  itemRow: " + itemRow.getKey());



                try {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rowNum);
            row.createCell(1).setCellValue(itemRow.getKey());
            row.createCell(2).setCellValue(itemRow.getApplieddiscountpercentage());
            row.createCell(3).setCellValue(itemRow.getCutname());
            row.createCell(4).setCellValue(itemRow.getCutprice());
            row.createCell(5).setCellValue(itemRow.getDiscountamount());
            row.createCell(6).setCellValue(itemRow.getGrossweightingrams());
            row.createCell(7).setCellValue(String.valueOf(itemRow.getGstamount()));

            row.createCell(8).setCellValue(itemRow.getOrderid());

            row.createCell(9).setCellValue(itemRow.getItemname());
            row.createCell(10).setCellValue(String.valueOf(itemRow.getNetweight()));
            row.createCell(11).setCellValue(itemRow.getTmcprice());
            row.createCell(12).setCellValue(String.valueOf(itemRow.getQuantity()));
            row.createCell(13).setCellValue(String.valueOf(itemRow.getTotalTmcPrice()));
            row.createCell(14).setCellValue(String.valueOf(itemRow.getPortionsize()));
            row.createCell(15).setCellValue(String.valueOf(itemRow.getOrderplacedtime()));
            row.createCell(16).setCellValue(String.valueOf(itemRow.getSlotdate()));
            row.createCell(17).setCellValue(itemRow.getSlotname());
            row.createCell(18).setCellValue(itemRow.getMarinadeitemdetails());
            row.createCell(19).setCellValue(itemRow.getTmcsubctgykey());
            row.createCell(20).setCellValue(itemRow.getVendorkey());
            row.createCell(21).setCellValue(itemRow.getVendorname());
            if(orderidTotal.containsKey(itemRow.getOrderid())){
                row.createCell(22).setCellValue(orderidTotal.get(itemRow.getOrderid()));

            }
            else{
                row.createCell(22).setCellValue(String.valueOf(itemRow.getTotalTmcPrice()));
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


        sheet.setColumnWidth(0, (10 * 600));
        sheet.setColumnWidth(1, (10 * 600));
        sheet.setColumnWidth(2, (10 * 600));
        sheet.setColumnWidth(3, (10 * 600));
        sheet.setColumnWidth(4, (10 * 600));
        sheet.setColumnWidth(5, (10 * 600));
        sheet.setColumnWidth(6, (10 * 600));
        sheet.setColumnWidth(7, (10 * 600));
        sheet.setColumnWidth(8, (10 * 600));
        sheet.setColumnWidth(9, (10 * 600));
        sheet.setColumnWidth(10, (10 * 600));
        sheet.setColumnWidth(11, (20 * 600));
        sheet.setColumnWidth(12, (10 * 600));
        sheet.setColumnWidth(13, (10 * 600));
        sheet.setColumnWidth(14, (10 * 600));
        sheet.setColumnWidth(15, (10 * 600));
        sheet.setColumnWidth(16, (10 * 600));
        sheet.setColumnWidth(17, (10 * 600));
        sheet.setColumnWidth(18, (10 * 600));
        sheet.setColumnWidth(19, (10 * 600));
        sheet.setColumnWidth(20, (10 * 600));


        Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList  1  : " + OrderItemDetailsTableArray.size());
        Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum:  1  " + rowNum);

        if (rowNum > OrderItemDetailsTableArray.size()) {
            Log.d(Constants.TAG, "prepareDataForExcelSheet type  sorted_OrdersList: " + OrderItemDetailsTableArray.size());
            Log.d(Constants.TAG, "prepareDataForExcelSheet type  rowNum: " + rowNum);

            GenerateExcelSheet("Order ItemDetails Sheet");
        }

        }
        }
        else
        {
        Toast.makeText(mContext,"There is no data to create sheet",Toast.LENGTH_LONG).show();

        }
    }

    private void AddDatatoUserDetailsExcelSheet() {
        AddedUserKey.clear();
        progressbarInstruction.setText("Generating Sheet ...");
        sheet = wb.createSheet(String.valueOf(System.currentTimeMillis()));
        int rowNum = 1;
        Cell headercell = null;

        org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(HSSFColor.RED.index);

        CellStyle headerCellStyle = wb.createCellStyle();
        headerCellStyle.setFillForegroundColor(HSSFColor.BLUE.index);
        headerCellStyle.setFont(headerFont);
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);


        org.apache.poi.ss.usermodel.Font contentFont = wb.createFont();
        contentFont.setBold(false);
        contentFont.setFontHeightInPoints((short) 10);
        contentFont.setColor(HSSFColor.BLACK.index);

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(contentFont);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);

        //Now column and row
        Row headerRow = sheet.createRow(0);

        for (int i = 0; i < columnsHeading_userDetails.length; i++) {
            headercell = headerRow.createCell(i);
            headercell.setCellValue(columnsHeading_userDetails[i]);
            headercell.setCellStyle(headerCellStyle);
        }

        boolean isFirstAddress = false;
        boolean isPrintedSecondtime = false;
        for (int useriterator = 0; useriterator < FilteredUserTableArray.size(); useriterator++) {
            Row row = sheet.createRow(rowNum++);
            if (isPrintedSecondtime) {
             //   row = sheet.createRow(rowNum++);

                isPrintedSecondtime = false;
            }
            isFirstAddress = false;
            Modal_User modal_user = FilteredUserTableArray.get(useriterator);
            String userkeyfromUserArray = modal_user.getKey();
            if (!AddedUserKey.contains(userkeyfromUserArray)){
                AddedUserKey.add(userkeyfromUserArray);
            row.createCell(0).setCellValue(useriterator + 1);
            row.getCell(0).setCellStyle(cellStyle);

            row.createCell(1).setCellValue(userkeyfromUserArray);
            row.createCell(2).setCellValue(modal_user.getMobileno());
            row.getCell(2).setCellStyle(cellStyle);

            row.createCell(3).setCellValue(modal_user.getName());
            row.createCell(4).setCellValue(modal_user.getEmail());


            row.createCell(5).setCellValue(modal_user.getCreatedtime());
            row.createCell(6).setCellValue(modal_user.getAppversion());
            row.createCell(7).setCellValue(modal_user.getDeviceos());
            row.createCell(8).setCellValue(modal_user.getUpdatedtime());
            row.createCell(9).setCellValue(modal_user.getFcmtoken());

                for (int addressiterator = 0; addressiterator < FilteredAddressTableArray.size(); addressiterator++) {
                    Modal_Address modal_address = FilteredAddressTableArray.get(addressiterator);
                    String userkeyfromAddressArray = modal_address.getUserkey();
                    if (userkeyfromUserArray.equals(userkeyfromAddressArray)) {
                        if (!isFirstAddress) {
                            isFirstAddress = true;

                            row.createCell(10).setCellValue(modal_address.getKey());
                            row.createCell(11).setCellValue(modal_address.getAddressline1());
                            row.createCell(12).setCellValue(modal_address.getAddressline2());
                            row.createCell(13).setCellValue(modal_address.getLandmark());
                            row.createCell(14).setCellValue(modal_address.getPincode());
                            row.createCell(15).setCellValue(modal_address.getAddresstype());
                            row.createCell(16).setCellValue(modal_address.getDeliverydistance());
                            row.getCell(16).setCellStyle(cellStyle);

                            row.createCell(17).setCellValue(modal_address.getLocationlat());

                            row.createCell(18).setCellValue(modal_address.getLocationlong());
                            row.createCell(19).setCellValue(modal_address.getVendorname());
                            row.createCell(20).setCellValue(modal_address.getContactpersonmobileno());
                            row.createCell(21).setCellValue(modal_address.getContactpersonname());
                        } else {
                            isPrintedSecondtime = true;

                            row = sheet.createRow(rowNum++);

                            row.createCell(10).setCellValue(modal_address.getKey());
                            row.createCell(11).setCellValue(modal_address.getAddressline1());
                            row.createCell(12).setCellValue(modal_address.getAddressline2());
                            row.createCell(13).setCellValue(modal_address.getLandmark());
                            row.createCell(14).setCellValue(modal_address.getPincode());
                            row.createCell(15).setCellValue(modal_address.getAddresstype());
                            row.createCell(16).setCellValue(modal_address.getDeliverydistance());
                            row.getCell(16).setCellStyle(cellStyle);

                            row.createCell(17).setCellValue(modal_address.getLocationlat());

                            row.createCell(18).setCellValue(modal_address.getLocationlong());
                            row.createCell(19).setCellValue(modal_address.getVendorname());
                            row.createCell(20).setCellValue(modal_address.getContactpersonmobileno());
                            row.createCell(21).setCellValue(modal_address.getContactpersonname());

                        }


                    }


                }
        }

            sheet.setColumnWidth(0, (10 * 300));
            sheet.setColumnWidth(1, (10 * 400));
            sheet.setColumnWidth(2, (10 * 500));
            sheet.setColumnWidth(3, (10 * 500));
            sheet.setColumnWidth(4, (10 * 600));
            sheet.setColumnWidth(5, (10 * 400));
            sheet.setColumnWidth(6, (10 * 300));
            sheet.setColumnWidth(7, (10 * 400));
            sheet.setColumnWidth(8, (10 * 400));
            sheet.setColumnWidth(9, (10 * 800));
            sheet.setColumnWidth(10, (10 * 1000));
            sheet.setColumnWidth(11, (10 * 400));
            sheet.setColumnWidth(12, (10 * 500));
            sheet.setColumnWidth(13, (10 * 500));
            sheet.setColumnWidth(14, (10 * 300));
            sheet.setColumnWidth(15, (10 * 300));
            sheet.setColumnWidth(16, (10 * 300));
            sheet.setColumnWidth(17, (10 * 400));
            sheet.setColumnWidth(18, (10 * 400));
            sheet.setColumnWidth(19, (10 * 600));
            sheet.setColumnWidth(20, (10 * 400));
            sheet.setColumnWidth(21, (10 * 400));

            int lastindex = FilteredUserTableArray.size() - 1;
            if (useriterator == lastindex) {
                GenerateExcelSheet("User Detail Sheet");

            }
        }
    }

    private void GenerateExcelSheet(String FolderName) {


        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/"+FolderName+"/";
        File dir = new File(path);
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e("Failed", "Storage not available or read only");

        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir,  System.currentTimeMillis()  +".xls");


        //   File file = new File(getExternalFilesDir(null), "Onlineorderdetails.xls");
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            wb.write(outputStream);
            Adjusting_Widgets_Visibility(false);
            //  Toast.makeText(getApplicationContext(), "File can't be  Created", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Toast.makeText(mContext, "File Created", Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            Toast.makeText(mContext, "File can't Created Permission Denied", Toast.LENGTH_LONG).show();

            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);


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


    }
    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }






    private void LogoutFromtheCurrentVendor() {
        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);
        Constants.default_mobileScreenSize=7;
        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "userrole",
                "");
        myEdit.putBoolean(
                "VendorLoginStatus",
                false);
        myEdit.putString(
                "VendorKey",
                "");
        myEdit.putString(
                "VendorName",
                ""
        );
        myEdit.putString(
                "VendorAddressline1",
                ""
        );
        myEdit.putString(
                "VendorAddressline2",
                ""
        );
        myEdit.putString(
                "VendorPincode",
                ""
        );
        myEdit.putString(
                "VendorMobileNumber",
                ""
        );

        myEdit.putString(
                "VendorFssaino",
                ""
        );
        myEdit.putString(
                "VendorLatitude",
                ""
        );
        myEdit.putString(
                "MinimumScreenSizeForPos",
                ""
        );


        myEdit.putString(
                "VendorLongitute",
                ""
        );

        myEdit.apply();


        Intent i = new Intent(mContext, Pos_LoginScreen.class);
        startActivity(i);
        getActivity().finish();


    }
    private static final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,

            ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION

    };

    private static final String[] ANDROID_12_BLE_PERMISSIONS = new String[]{
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_SCAN,
            ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public void requestBlePermissions(Activity activity, int requestCode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!EasyPermissions.hasPermissions(activity, ANDROID_12_BLE_PERMISSIONS)) {
                EasyPermissions.requestPermissions(activity, "message", requestCode,ANDROID_12_BLE_PERMISSIONS);
            }
        }




        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            getActivity().requestPermissions(activity, ANDROID_12_BLE_PERMISSIONS, requestCode);
        else
            getActivity().requestPermissions(activity, BLE_PERMISSIONS, requestCode);


         */

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


    void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

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
            try {
                switch (msg.what) {
                    case MESSAGE_STATE_CHANGE:
                        //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                        switch (msg.arg1) {
                            case BluetoothPrintDriver.STATE_CONNECTED:
                                printerConnectionStatus_Textwidget.setText(R.string.title_connected_to);
                                printerConnectionStatus_Textwidget.append(mConnectedDeviceName);
                                isPrinterCnnected = true;
                                printerStatus = "Connected";
                                printerName = mConnectedDeviceName;
                                // setTitle(R.string.title_connected_to);
                                //setTitle(mConnectedDeviceName);
                                SaveDatainSharedPreferences(isPrinterCnnected, printerName, printerStatus, Constants.Bluetooth_PrinterType);

                                break;
                            case BluetoothPrintDriver.STATE_CONNECTING:
                                printerConnectionStatus_Textwidget.setText(R.string.title_connecting);
                                // setTitle(R.string.title_connecting);
                                isPrinterCnnected = false;
                                printerStatus = "Connecting";
                                printerName = mConnectedDeviceName;
                                SaveDatainSharedPreferences(isPrinterCnnected, printerName, printerStatus, Constants.Bluetooth_PrinterType);


                                break;
                            case BluetoothPrintDriver.STATE_LISTEN:
                                printerConnectionStatus_Textwidget.setText("state listen");

                            case BluetoothPrintDriver.STATE_NONE:
                                printerConnectionStatus_Textwidget.setText(R.string.title_not_connected);
                                //  setTitle(R.string.title_not_connected);
                                isPrinterCnnected = false;
                                printerStatus = "Not Connected";
                                printerName = mConnectedDeviceName;
                                try {
                                    changeSelectedPrinterType(nonePrinterRadiobutton.getId(), true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                SaveDatainSharedPreferences(isPrinterCnnected, printerName, printerStatus, Constants.Bluetooth_PrinterType);


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
                        if (readBuf[2] == 0)
                            ErrorMsg = "NO ERROR!         ";
                        else {
                            if ((readBuf[2] & 0x02) != 0)
                                ErrorMsg = "ERROR: No printer connected!";
                            if ((readBuf[2] & 0x04) != 0)
                                ErrorMsg = "ERROR: No paper!  ";
                            if ((readBuf[2] & 0x08) != 0)
                                ErrorMsg = "ERROR: Voltage is too low!  ";
                            if ((readBuf[2] & 0x40) != 0)
                                ErrorMsg = "ERROR: Printer Over Heat!  ";
                        }
                        Voltage = (float) ((readBuf[0] * 256 + readBuf[1]) / 10.0);
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
            catch (Exception e){
                e.printStackTrace();
            }
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
                    try{
                        changeSelectedPrinterType(nonePrinterRadiobutton.getId(),true);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(mContext, "bt_not_enabled_leaving", Toast.LENGTH_SHORT).show();
                    //finish();
                }
        }
    }




    private void SaveDatainSharedPreferences(boolean isPrinterConnected, String printerName, String printerStatus, String printerType) {
        SharedPreferences sharedPreferences
                = mContext.getSharedPreferences("PrinterConnectionData",
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
                isPrinterConnected);

        myEdit.putString(
                "printerType",
                printerType);
        myEdit.apply();





    }




    /*
    private void initializeCache() {
        long size = 0;
        size += getDirSize(mContext.getCacheDir());
        //Log.i(Constants.TAG,"Cache Memory :1 "+size);

        size += getDirSize(mContext.getExternalCacheDir());
        //Log.i(Constants.TAG,"Cache Memory :2 "+size);

    }*/


/*
    public static long getDirSize(File dir){
        long size = 0;
        long size = 0;
        File[] files = cacheDirectory.listFiles();
        for (File f:files) {
            size = size+f.length();
        }
       for (File file : dir.listFiles()) {

            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }


        return size;
    }

    */
    /*
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
          long cacheSize =  getDirSize(dir);

            //Log.i(Constants.TAG,"Cache Memory :"+cacheSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else {
            return false;
        }
    }

    private void getConfirmationtoLogout() {
        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Exit_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        signOutfromAWSandClearSharedPref();

                    }

                    @Override
                    public void onNo() {

                    }
                });
    }

    private void getConfirmationtoResetTokenNo(String vendorkey) {
        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.ResetToken_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        resetTokenNo(vendorkey);

                    }

                    @Override
                    public void onNo() {

                    }
                });
    }

    private void getTokenNo(String vendorkey) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTokenNoUsingKey+vendorkey,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);


                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONObject result  = response.getJSONObject("content");
                            JSONObject result2  = result.getJSONObject("Item");
                            String tokenNumber = result2.getString("tokenNumber");
                            resetTokenNO_text.setText(tokenNumber);
                            Adjusting_Widgets_Visibility(false);



                        } catch (JSONException e) {
                            Adjusting_Widgets_Visibility(false);

                            e.printStackTrace();
                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                Adjusting_Widgets_Visibility(false);

                error.printStackTrace();
            }
        })
        {




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

    private void resetTokenNo(String vendorkey) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_ResetTokenNo+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {


                //Log.d(Constants.TAG, "api: " + Constants.api_ResetTokenNo+vendorkey);

                //Log.d(Constants.TAG, "Responsewwwww: " + response);
                try {
                    String tokenNo = response.getString("tokenNumber");
                    resetTokenNO_text.setText(tokenNo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }
    private final BroadcastReceiver usbReceiver_settings = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (context) {
                    UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (usbManager != null && usbDevice != null) {
                            SaveDatainSharedPreferences(true,String.valueOf(usbDevice.getDeviceName()),"Connected",Constants.USB_PrinterType);
                            printerConnectionStatus_Textwidget.setText("USB Printer Connected");
                        }
                        else{
                            connectUSBPrinter();
                        }
                    }
                    else{
                        connectUSBPrinter();
                    }
                }
            }
        }
    };

    private void openConsolidatedSalesReportActivity() {
        SharedPreferences sharedPreferences
                = requireContext().getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        String VendorName  = sharedPreferences.getString("VendorName","");



        Intent i = new Intent(mContext, ConsolidatedReportSubCtgywise.class);
        i.putExtra("VendorName",VendorName);
        mContext.startActivity(i);



    }



    private void signOutfromAWSandClearSharedPref() {
        try {
            AWSMobileClient.getInstance().signOut();
            Constants.default_mobileScreenSize = 6.3;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences
                = requireContext().getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "MinimumScreenSizeForPos",
                ""
        );
        myEdit.putBoolean(
                "VendorLoginStatus",
                false);
        myEdit.putString(
                "VendorKey",
                "");
        myEdit.putString(
                "VendorName",
                ""
        );
        myEdit.putString(
                "VendorAddressline1",
                ""
        );
        myEdit.putString(
                "VendorAddressline2",
                ""
        );
        myEdit.putString(
                "VendorPincode",
                ""
        );
        myEdit.putString(
                "VendorMobileNumber",
                ""
        );

        myEdit.putString(
                "VendorFssaino",
                ""
        );

        myEdit.apply();
        Intent i = new Intent(mContext, Pos_LoginScreen.class);
        startActivity(i);
        getActivity().finish();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.pos_settings_fragment, container, false);
       // MenuItems=getData();

        rootView.setTag("SettingsFragment");
        return rootView;
    }




    private String getLongValuefortheDate(String orderplacedtime) {
        String longvalue = "";
        try {
            String time1 = orderplacedtime;
            //   Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
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

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
            ConnectPrinter();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(mContext, "Bluetooth access Denied!", Toast.LENGTH_SHORT).show();

    }
}








/*
        if (UserRole.equals(Constants.SUPERADMIN_ROLENAME)) {
            managemenuLayout.setVisibility(VISIBLE);
            manageordersLinearLayout.setVisibility(VISIBLE);
            salesLinearLayout.setVisibility(VISIBLE);


            if (screenInches < 8) {
                delivered_orders_timewiseReport.setVisibility(View.VISIBLE);
                editPaymentModeOftheOrder.setVisibility(VISIBLE);
                orderDetailsDump_report.setVisibility(VISIBLE);
                generateCustomerMobileno_BillvalueReport.setVisibility(VISIBLE);
                cancelledOrdersLayout.setVisibility(VISIBLE);
                deliveryPartnerSettlementReport.setVisibility(VISIBLE);
                plotOrdersLocation_layout.setVisibility(VISIBLE);
                MenuTransactionDetailsLayout.setVisibility(VISIBLE);
                generateUserDetailsLayout.setVisibility(VISIBLE);
                swiggyOrderPlacing_layout.setVisibility(GONE);
            } else {
                swiggyOrderPlacing_layout.setVisibility(VISIBLE);
                deliveryPartnerSettlementReport.setVisibility(VISIBLE);
                testlayout.setVisibility(GONE);
                delivered_orders_timewiseReport.setVisibility(View.GONE);
                editPaymentModeOftheOrder.setVisibility(View.GONE);
                orderDetailsDump_report.setVisibility(GONE);
                generateCustomerMobileno_BillvalueReport.setVisibility(GONE);
                cancelledOrdersLayout.setVisibility(GONE);
                plotOrdersLocation_layout.setVisibility(GONE);
                MenuTransactionDetailsLayout.setVisibility(GONE);
                generateUserDetailsLayout.setVisibility(GONE);


            }
        }
        else if (UserRole.equals(Constants.ADMIN_ROLENAME)) {
            managemenuLayout.setVisibility(VISIBLE);
            manageordersLinearLayout.setVisibility(VISIBLE);
            salesLinearLayout.setVisibility(VISIBLE);
            orderDetailsDump_report.setVisibility(GONE);
            testlayout.setVisibility(GONE);
            plotOrdersLocation_layout.setVisibility(GONE);
            generateCustomerMobileno_BillvalueReport.setVisibility(GONE);
            generateUserDetailsLayout.setVisibility(GONE);

            if (screenInches < 8) {
                delivered_orders_timewiseReport.setVisibility(View.VISIBLE);
                editPaymentModeOftheOrder.setVisibility(VISIBLE);
                cancelledOrdersLayout.setVisibility(VISIBLE);
                deliveryPartnerSettlementReport.setVisibility(VISIBLE);
                MenuTransactionDetailsLayout.setVisibility(VISIBLE);
                swiggyOrderPlacing_layout.setVisibility(GONE);

            } else {
                swiggyOrderPlacing_layout.setVisibility(VISIBLE);

                deliveryPartnerSettlementReport.setVisibility(VISIBLE);
                delivered_orders_timewiseReport.setVisibility(View.GONE);
                editPaymentModeOftheOrder.setVisibility(View.GONE);
                cancelledOrdersLayout.setVisibility(GONE);
                MenuTransactionDetailsLayout.setVisibility(GONE);


            }


        }
        else if (UserRole.equals(Constants.STOREMANAGER_ROLENAME)) {
            managemenuLayout.setVisibility(VISIBLE);

            searchOrdersUsingMobileNumbers.setVisibility(VISIBLE);
            salesLinearLayout.setVisibility(VISIBLE);
            orderDetailsDump_report.setVisibility(GONE);
            cancelledOrdersLayout.setVisibility(GONE);
            generateCustomerMobileno_BillvalueReport.setVisibility(GONE);

            plotOrdersLocation_layout.setVisibility(GONE);
            editPaymentModeOftheOrder.setVisibility(GONE);
            MenuTransactionDetailsLayout.setVisibility(VISIBLE);
            generateUserDetailsLayout.setVisibility(GONE);

            if (screenInches < 8) {
                delivered_orders_timewiseReport.setVisibility(View.VISIBLE);
                slotwiseAppOrderList.setVisibility(VISIBLE);
                swiggyOrderPlacing_layout.setVisibility(GONE);

            } else {
                swiggyOrderPlacing_layout.setVisibility(VISIBLE);

                delivered_orders_timewiseReport.setVisibility(View.GONE);
                editPaymentModeOftheOrder.setVisibility(View.GONE);
                orderDetailsDump_report.setVisibility(GONE);
                generateCustomerMobileno_BillvalueReport.setVisibility(GONE);
                cancelledOrdersLayout.setVisibility(GONE);
                slotwiseAppOrderList.setVisibility(GONE);
                slotwiseAppOrderList.setVisibility(GONE);
            }
        }
        else if (UserRole.equals(Constants.ASSISTANTSTOREMANAGER_ROLENAME)) {
            managemenuLayout.setVisibility(GONE);
            salesLinearLayout.setVisibility(VISIBLE);
            consolidatedSalesReport.setVisibility(GONE);
            PosSalesReport.setVisibility(GONE);
            resetTokenNoLayout.setVisibility(GONE);
            AppSalesReport.setVisibility(GONE);
            deliveryPartnerSettlementReport.setVisibility(GONE);
            generateCustomerMobileno_BillvalueReport.setVisibility(GONE);
            editPaymentModeOftheOrder.setVisibility(GONE);
            generateCustomerMobileno_BillvalueReport.setVisibility(GONE);
            cancelledOrdersLayout.setVisibility(GONE);
            generateUserDetailsLayout.setVisibility(GONE);

            if (screenInches < 8) {
                delivered_orders_timewiseReport.setVisibility(View.VISIBLE);
                slotwiseAppOrderList.setVisibility(VISIBLE);
                plotOrdersLocation_layout.setVisibility(VISIBLE);
                orderDetailsDump_report.setVisibility(VISIBLE);
                searchOrdersUsingMobileNumbers.setVisibility(VISIBLE);
                slotwiseAppOrderList.setVisibility(VISIBLE);
                swiggyOrderPlacing_layout.setVisibility(GONE);

            } else {
                swiggyOrderPlacing_layout.setVisibility(VISIBLE);

                delivered_orders_timewiseReport.setVisibility(View.GONE);
                slotwiseAppOrderList.setVisibility(GONE);
                plotOrdersLocation_layout.setVisibility(GONE);
            }
            testlayout.setVisibility(GONE);

        }
        else if (UserRole.equals(Constants.CASHIER_ROLENAME)) {
            generateUserDetailsLayout.setVisibility(GONE);

            managemenuLayout.setVisibility(GONE);
            searchOrdersUsingMobileNumbers.setVisibility(VISIBLE);
            salesLinearLayout.setVisibility(VISIBLE);
            editPaymentModeOftheOrder.setVisibility(GONE);
            delivered_orders_timewiseReport.setVisibility(View.GONE);
            generateCustomerMobileno_BillvalueReport.setVisibility(GONE);
            cancelledOrdersLayout.setVisibility(GONE);
            plotOrdersLocation_layout.setVisibility(GONE);
            slotwiseAppOrderList.setVisibility(GONE);
            orderDetailsDump_report.setVisibility(GONE);
            swiggyOrderPlacing_layout.setVisibility(GONE);

        }
        else {
            swiggyOrderPlacing_layout.setVisibility(VISIBLE);

            managemenuLayout.setVisibility(GONE);
            plotOrdersLocation_layout.setVisibility(GONE);
            slotwiseAppOrderList.setVisibility(GONE);
            searchOrdersUsingMobileNumbers.setVisibility(GONE);
            salesLinearLayout.setVisibility(GONE);
            editPaymentModeOftheOrder.setVisibility(GONE);
            delivered_orders_timewiseReport.setVisibility(View.GONE);
            orderDetailsDump_report.setVisibility(GONE);
            generateCustomerMobileno_BillvalueReport.setVisibility(GONE);
            cancelledOrdersLayout.setVisibility(GONE);
            generateUserDetailsLayout.setVisibility(GONE);

            Toast.makeText(mContext, "You Don't have any User Role Ask Admin to assign the Role", Toast.LENGTH_LONG).show();


        }


 */

      /*  if(UserRole.length()>0) {
            if (UserRole.toUpperCase().equals(Constants.SUPERADMIN_ROLENAME)) {
                managemenuLayout.setVisibility(VISIBLE);
                viewordersLinearLayout.setVisibility(VISIBLE);
                manageordersLinearLayout.setVisibility(VISIBLE);
                salesLinearLayout.setVisibility(VISIBLE);
                // dataAnalyticsLinearLayout.setVisibility(VISIBLE);
                GeneralConfiguration_linearLayout.setVisibility(VISIBLE);


                if (screenInches > Constants.default_mobileScreenSize) {
                    //if Pos
                    editPaymentModeOftheOrder.setVisibility(GONE);
                    plotOrdersLocation_layout.setVisibility(GONE);
                    delivered_orders_timewiseReport.setVisibility(View.GONE);
                    slotwiseAppOrderList.setVisibility(View.GONE);
                    //total dataAnalytics module
                    dataAnalyticsLinearLayout.setVisibility(GONE);

                } else {
                    //if Mobile
                    swiggyOrderPlacing_layout.setVisibility(GONE);

                }
            } else if (UserRole.toUpperCase().equals(Constants.REPORTSVIEWER_ROLENAME)) {
                managemenuLayout.setVisibility(GONE);
                viewordersLinearLayout.setVisibility(GONE);
                manageordersLinearLayout.setVisibility(GONE);
                salesLinearLayout.setVisibility(GONE);
                GeneralConfiguration_linearLayout.setVisibility(VISIBLE);
                resetTokenNoLayout.setVisibility(GONE);
                if (screenInches > Constants.default_mobileScreenSize) {
                    //if Pos

                    dataAnalyticsLinearLayout.setVisibility(GONE);


                } else {
                    //if Mobile

                    dataAnalyticsLinearLayout.setVisibility(VISIBLE);

                }
            } else if (UserRole.toUpperCase().equals(Constants.STOREMANAGER_ROLENAME)) {
                managemenuLayout.setVisibility(VISIBLE);
                viewordersLinearLayout.setVisibility(VISIBLE);
                manageordersLinearLayout.setVisibility(VISIBLE);
                salesLinearLayout.setVisibility(VISIBLE);
                dataAnalyticsLinearLayout.setVisibility(GONE);
                GeneralConfiguration_linearLayout.setVisibility(VISIBLE);
                if (screenInches > Constants.default_mobileScreenSize) {
                    //if Pos
                    editPaymentModeOftheOrder.setVisibility(GONE);
                    plotOrdersLocation_layout.setVisibility(GONE);
                    delivered_orders_timewiseReport.setVisibility(View.GONE);
                    slotwiseAppOrderList.setVisibility(View.GONE);

                } else {
                    //if Mobile
                    swiggyOrderPlacing_layout.setVisibility(GONE);

                }
            } else if (UserRole.toUpperCase().equals(Constants.DELIVERYMANAGER_ROLENAME)) {

                managemenuLayout.setVisibility(VISIBLE);
                viewordersLinearLayout.setVisibility(VISIBLE);
                manageordersLinearLayout.setVisibility(GONE);
                salesLinearLayout.setVisibility(GONE);
                dataAnalyticsLinearLayout.setVisibility(GONE);
                GeneralConfiguration_linearLayout.setVisibility(VISIBLE);
                resetTokenNoLayout.setVisibility(GONE);
                if (screenInches > Constants.default_mobileScreenSize) {
                    //if Pos
                    editPaymentModeOftheOrder.setVisibility(GONE);
                    plotOrdersLocation_layout.setVisibility(GONE);
                    delivered_orders_timewiseReport.setVisibility(View.GONE);
                    slotwiseAppOrderList.setVisibility(View.GONE);

                } else {
                    //if Mobile
                    swiggyOrderPlacing_layout.setVisibility(GONE);

                }


            } else if (UserRole.toUpperCase().equals(Constants.CASHIER_ROLENAME)) {

                managemenuLayout.setVisibility(VISIBLE);
                viewordersLinearLayout.setVisibility(VISIBLE);
                manageordersLinearLayout.setVisibility(GONE);
                salesLinearLayout.setVisibility(VISIBLE);
                dataAnalyticsLinearLayout.setVisibility(GONE);
                GeneralConfiguration_linearLayout.setVisibility(VISIBLE);
                if (screenInches > Constants.default_mobileScreenSize) {
                    //if Pos
                    editPaymentModeOftheOrder.setVisibility(GONE);
                    plotOrdersLocation_layout.setVisibility(GONE);
                    delivered_orders_timewiseReport.setVisibility(View.GONE);
                    slotwiseAppOrderList.setVisibility(View.GONE);

                } else {
                    //if Mobile
                    swiggyOrderPlacing_layout.setVisibility(GONE);

                }
            }
        }

        else{
            Toast.makeText(getActivity(),"You Don't have any User Role Ask Admin to assign the Role",Toast.LENGTH_LONG).show();

        }


       */
