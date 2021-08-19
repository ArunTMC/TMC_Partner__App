package com.meatchop.tmcpartner.Settings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_LoginScreen;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.AbsListView.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    Button on, off;
    Context mContext;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch autoRefreshingSwitch;
    LinearLayout addDunzoOrders_Placing_layout,generateOrderItemDetailsLayout,consolidatedSalesReportWeekwise, login_as_another_vendor, manageordersLinearLayout, slotwiseAppOrderList, plotOrdersLocation_layout, testlayout, editPaymentModeOftheOrder, delivered_orders_timewiseReport, changeMenuItemStatus, logout, consolidatedSalesReport, PosSalesReport, AppSalesReport, changeMenuItemVisibilityinTv, managemenuLayout, changeMenuItemPrice, changeDeliverySlotdetails, deliveryPartnerSettlementReport, searchOrdersUsingMobileNumbers, posOrdersList, generateCustomerMobileno_BillvalueReport, loadingpanelmask, loadingPanel;
    String UserRole, MenuItems, UserPhoneNumber, vendorkey, vendorName;
    TextView progressbarInstruction,userMobileNo, resetTokenNO_text, storeName, App_Sales_Report_text, Pos_Sales_Report_text;
    LinearLayout mobilePrinterConnectLayout,menuItemAvailabiltyStatusReport,orderTrackingDetailsDump_report,GeneralConfiguration_linearLayout,dataAnalyticsLinearLayout,viewordersLinearLayout,MenuTransactionDetailsLayout, salesLinearLayout, orderDetailsDump_report, cancelledOrdersLayout, resetTokenNoLayout, generateUserDetailsLayout,swiggyOrderPlacing_layout;
    Button resetTokenNoButton;
    ScrollView settings_scrollview;
    BottomNavigationView bottomNavigationView;

    double screenInches;
    List<String>allowedModules_array;


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
    String printerNamefromSP = "";
    String printerStatusfromSP = "";


    Workbook wb;
    Sheet sheet = null;
    private static String[] columnsHeading_userDetails = {"S.No", "User Key", "MobileNo", " Name", "Email", "Created time", "App Version", "deviceos", "updatedtime", "Fcm Token", "User Address Key","AddressLine 1","AddressLine 2","LandMark","PinCode","Address Type","Delivery Distance","Location Latitude","Location Longitutde","Vendor Name","Contact Person Mobile no", "Contact Person name"};
    private static String[] columnsHeading_orderItemDetails = {"S.No", "Key", "Applied Discount Percentage", " Cut Name", "Cut Price", "Discount Amount", "Grossweight in Grams", "Gst Amount","Orderid ", "Item Name", "TmcPrice","Net Weight","Portion Size","Quantity","Order Placed Time", "Slot Date","Slot Name","Marinade Item Details","Tmc Subctgykey", "Vendor Key","Vendor Name"};

    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;

    Button generateUserDetailsButton,generateOrderItemDetailsButton,connect_printer_button_widget;
    List<Modal_User> UserTableArray = new ArrayList<>();
    List<Modal_Address> AddressTableArray = new ArrayList<>();
    List<ModalOrderItemDetails> OrderItemDetailsTableArray = new ArrayList<>();

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
        //  args.putString("menuItem", data);

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        deliveryPartnerSettlementReport = view.findViewById(R.id.deliveryPartnerSettlementReport);
        searchOrdersUsingMobileNumbers = view.findViewById(R.id.searchOrdersUsingMobileNumbers);
        userMobileNo = view.findViewById(R.id.userMobileNo);
        settings_scrollview = view.findViewById(R.id.settings_scrollview);
        autoRefreshingSwitch = view.findViewById(R.id.autoRefreshingSwitch);
        changeMenuItemStatus = view.findViewById(R.id.changeMenuItemStatus);
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
        MenuTransactionDetailsLayout = view.findViewById(R.id.getMenuTransactionDetailsLayout);
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
        //  bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);

        //  final SharedPreferences sharedPreferencesMenuitem = requireContext().getSharedPreferences("MenuList", MODE_PRIVATE);
        //  MenuItems = sharedPreferencesMenuitem.getString("MenuList", "");

        SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        UserPhoneNumber = (shared.getString("UserPhoneNumber", "+91"));
        vendorkey = shared.getString("VendorKey", "");
        vendorName = shared.getString("VendorName", "");
        UserRole = shared.getString("userrole", "");


        userMobileNo.setText(UserPhoneNumber);
        storeName.setText(vendorName);
        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        screenInches = Math.sqrt(x + y);
        getTokenNo(vendorkey);
        //  initializeCache();

        salesLinearLayout.setVisibility(GONE);
        managemenuLayout.setVisibility(GONE);
        viewordersLinearLayout.setVisibility(GONE);
        manageordersLinearLayout.setVisibility(GONE);
        dataAnalyticsLinearLayout.setVisibility(GONE);


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


        if (screenInches > Constants.default_mobileScreenSize) {
            //if Pos
            editPaymentModeOftheOrder.setVisibility(GONE);
            plotOrdersLocation_layout.setVisibility(GONE);
            delivered_orders_timewiseReport.setVisibility(View.GONE);
            slotwiseAppOrderList.setVisibility(View.GONE);
            mobilePrinterConnectLayout.setVisibility(GONE);
            dataAnalyticsLinearLayout.setVisibility(GONE);

            if(UserRole.equals(Constants.CASHIER_ROLENAME)){
                swiggyOrderPlacing_layout.setVisibility(VISIBLE);
             addDunzoOrders_Placing_layout.setVisibility(VISIBLE);


            }


        } else {
            //if Mobile
            addDunzoOrders_Placing_layout.setVisibility(GONE);
            swiggyOrderPlacing_layout.setVisibility(GONE);
            if(UserRole.equals(Constants.DELIVERYMANAGER_ROLENAME)){
                salesLinearLayout.setVisibility(VISIBLE);
                    consolidatedSalesReport.setVisibility(GONE);
                    PosSalesReport.setVisibility(GONE);
                    AppSalesReport.setVisibility(GONE);
                    consolidatedSalesReportWeekwise.setVisibility(GONE);
                    deliveryPartnerSettlementReport.setVisibility(GONE);
                    delivered_orders_timewiseReport.setVisibility(VISIBLE);


            }
        }

            if (UserPhoneNumber.equals("+919597580128")) {
            testlayout.setVisibility(VISIBLE);
        } else {
            testlayout.setVisibility(GONE);

        }


        if (UserPhoneNumber.equals("+918451023780")) {
            generateUserDetailsLayout.setVisibility(VISIBLE);
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
        } else {
            // bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);
        }
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

        generateUserDetailsButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {

        GetUserTable();

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
                ConnectPrinter();

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


        MenuTransactionDetailsLayout.setOnClickListener(new OnClickListener() {
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
        resetTokenNoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getConfirmationtoResetTokenNo(vendorkey);
            }
        });


    }

    private void GetOrderItemDetailsAndGenerateSheet() {

        Adjusting_Widgets_Visibility(true);
        progressbarInstruction.setText("Fetching OrderItem Details...");
        UserTableArray.clear();
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
                                    }
                                    else{
                                        modalOrderItemDetails.orderid ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
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
                                    }
                                    else{
                                        modalOrderItemDetails.quantity ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
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
                                    }
                                    else{
                                        modalOrderItemDetails.tmcprice ="";
                                    }
                                }
                                catch(Exception e ){
                                    e.printStackTrace();
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


        try {
            wb = new HSSFWorkbook();
            //Now we are creating sheet

        } catch (Exception e) {
            e.printStackTrace();
        }


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
                    AddDatatoUserDetailsExcelSheet();

                }
                else if(whichsheetoGenerate.equals("OrderItemDetailsSheet")){
                    AddDatatoOrderItemDetailsExcelSheet();

                }
            } catch (Exception e) {
                e.printStackTrace();
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
            row.createCell(11).setCellValue(itemRow.getTmcprice());
            row.createCell(10).setCellValue(String.valueOf(itemRow.getNetweight()));
            row.createCell(12).setCellValue(String.valueOf(itemRow.getPortionsize()));
            row.createCell(13).setCellValue(String.valueOf(itemRow.getQuantity()));
            row.createCell(14).setCellValue(String.valueOf(itemRow.getOrderplacedtime()));
            row.createCell(15).setCellValue(String.valueOf(itemRow.getSlotdate()));
            row.createCell(16).setCellValue(itemRow.getSlotname());
            row.createCell(17).setCellValue(itemRow.getMarinadeitemdetails());
            row.createCell(18).setCellValue(itemRow.getTmcsubctgykey());
            row.createCell(19).setCellValue(itemRow.getVendorkey());
            row.createCell(20).setCellValue(itemRow.getVendorname());




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
        for (int useriterator = 0; useriterator < UserTableArray.size(); useriterator++) {
            Row row = sheet.createRow(rowNum++);
            if(isPrintedSecondtime) {
                row = sheet.createRow(rowNum++);

                isPrintedSecondtime =false;
            }
            isFirstAddress = false;
            Modal_User modal_user = UserTableArray.get(useriterator);
            String userkeyfromUserArray = modal_user.getKey();

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

            for (int addressiterator = 0; addressiterator < AddressTableArray.size(); addressiterator++) {
                Modal_Address modal_address = AddressTableArray.get(addressiterator);
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
                        isPrintedSecondtime =true;

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

            int lastindex = UserTableArray.size() - 1;
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
                "VendorLongitute",
                ""
        );

        myEdit.apply();


        Intent i = new Intent(mContext, Pos_LoginScreen.class);
        startActivity(i);
        getActivity().finish();


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
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    //if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothPrintDriver.STATE_CONNECTED:
                            printerConnectionStatus_Textwidget.setText(R.string.title_connected_to);
                            printerConnectionStatus_Textwidget.append(mConnectedDeviceName);
                            isPrinterCnnected =true;
                            printerStatus = "Connected";
                            printerName = mConnectedDeviceName;
                           // setTitle(R.string.title_connected_to);
                            //setTitle(mConnectedDeviceName);
                            SaveDatainSharedPreferences(isPrinterCnnected,printerName,printerStatus);

                            break;
                        case BluetoothPrintDriver.STATE_CONNECTING:
                            printerConnectionStatus_Textwidget.setText(R.string.title_connecting);
                          // setTitle(R.string.title_connecting);
                            isPrinterCnnected =false;
                            printerStatus = "Connecting";
                            printerName = mConnectedDeviceName;
                            SaveDatainSharedPreferences(isPrinterCnnected,printerName,printerStatus);

                            break;
                        case BluetoothPrintDriver.STATE_LISTEN:
                            printerConnectionStatus_Textwidget.setText("state listen");

                        case BluetoothPrintDriver.STATE_NONE:
                            printerConnectionStatus_Textwidget.setText(R.string.title_not_connected);
                          //  setTitle(R.string.title_not_connected);
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
                    //finish();
                }
        }
    }




    private void SaveDatainSharedPreferences(boolean isPrinterCnnected, String printerName, String printerStatus) {
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
                isPrinterCnnected);
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

        AWSMobileClient.getInstance().signOut();


        SharedPreferences sharedPreferences
                = requireContext().getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();

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
}