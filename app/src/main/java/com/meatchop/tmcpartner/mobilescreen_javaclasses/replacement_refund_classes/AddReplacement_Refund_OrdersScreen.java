 package com.meatchop.tmcpartner.mobilescreen_javaclasses.replacement_refund_classes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Add_CustomerOrder_TrackingTableInterface;
import com.meatchop.tmcpartner.customerorder_trackingdetails.Add_CustomerOrder_TrackingTable_AsyncTask;
import com.meatchop.tmcpartner.posscreen_javaclasses.manage_orders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.posscreen_javaclasses.other_java_classes.Modal_MenuItem;
import com.meatchop.tmcpartner.posscreen_javaclasses.pos_new_orders.Modal_NewOrderItems;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.settings.add_replacement_refund_order.Modal_ReplacementOrderDetails;
import com.meatchop.tmcpartner.settings.DeviceListActivity;
import com.meatchop.tmcpartner.settings.helper_weight_cut_Listview;
import com.meatchop.tmcpartner.TMCAlertDialogClass;
import com.meatchop.tmcpartner.sqlite.TMCMenuItemSQL_DB_Manager;

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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static com.meatchop.tmcpartner.Constants.TAG;
import static com.meatchop.tmcpartner.Constants.api_Update_MenuItemStockAvlDetails;

public class AddReplacement_Refund_OrdersScreen extends AppCompatActivity {
    TextView orderid_textview_widget,status_text_widget,usermobileno_textview_widget,orderplaceddate_textview_widget,markeddate_textview_widget,totalAmtUserGot_textview_widget,replacementorderAmount_textview_widget,
        refundorder_textview_widget,balanceorder_textview_widget;
    TextView mobile_ItemTotal_textwidget,mobile_GST_textwidget,mobile_ToPay_textwidget,refundAmt_editwidget;
    Button mobile_checkout_button,addRefund_buttonWidget;
    double new_total_amount,old_total_Amount=0,sub_total;
    double new_taxes_and_charges_Amount,old_taxes_and_charges_Amount=0;
    double new_to_pay_Amount,old_to_pay_Amount=0,totalAmounttopay=0;
    int new_totalAmount_withGst,new_totalAmount_withoutGst=0,newGst=0;
    String finaltoPayAmount="",discountAmount="0",oldOrderOrderid = "0",ordertype_Old ="";
    String FormattedTime,CurrentDate,formattedDate,CurrentDay;
    String vendorKey="",usermobileNo ="",orderPlacedDate ="",vendorName ="";;
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String selectedPaymentMode  ="CASH ON DELIVERY";
    String selectedOrderType  ="POS Order";
    String Currenttime,MenuItems,customermobileno="",Currenttime_transactiontable ="";
    public static List<Modal_NewOrderItems> completemenuItem;
    ReplacementRefundListFragment replacementRefundListFragment;
    Adapter_Replacement_Refund_List adapter_replacement_refund_list;
    long sTime =0;
    String finaltoPayAmountinmethod="";
    Adapter_Place_New_ReplacementOrder_Mobile adapterPlaceNewReplacementOrder;
    Modal_ReplacementOrderDetails modal_replacementOrderDetails = new Modal_ReplacementOrderDetails();

    String replacementAmount_String,refundAmount_String,totalAmountUserCanAvl_String,ordermarkeddate ="";
    double replacementAmount_Double =0 ,refundAmount_Double = 0,totalAmountUserCanAvl_Double = 0, balanceAmount = 0;
    List<Modal_ManageOrders_Pojo_Class> OrderdItems_desp ;
    ListView orderidItemListview;
    RecyclerView newOrders_recyclerView;
    double screenInches;
    List<Modal_NewOrderItems> MenuItemArray=new ArrayList<>();
    public static HashMap<String,Modal_NewOrderItems> cartItem_hashmap = new HashMap();
    public static List<String> cart_Item_List =new ArrayList<>();
    public static BottomSheetDialog bottomSheetDialog;
    static LinearLayout loadingPanel,replacementParentLayout,refundParentLayout;
    static LinearLayout loadingpanelmask;
    boolean isUpdateCouponTransactionMethodCalled=false;
    private  boolean isOrderDetailsMethodCalled =false;
    private  boolean isOrderTrackingDetailsMethodCalled =false;
    private  boolean isPaymentDetailsMethodCalled =false;

    private  boolean isUpdateReplacementDetailsMethodCalled =false;
    JSONArray markedItemDetailsJSON  = new JSONArray();
    boolean isMobileAppDataFetchedinDashboard=false;
    boolean isanyProducthaveZeroAsweight=false;
    boolean isUpdateRedeemPointsWithoutKeyMethodCalled =false, ispaymentMode_Clicked =false,isPrintedSecondTime=false,isPhoneOrderSelected=false;
    double totalredeempointsusergetfromorder=0,pointsfor100rs_double=0;
    String ordertype="",maxpointsinaday_String="",minordervalueforredeem_String="",pointsfor100rs_String="";
    double maxpointsinaday_double,minordervalueforredeem_double,finalamounttoPay;

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
    String mConnectedDeviceName ;
    boolean isPrinterCnnected = false;
    String printerName = "";
    String printerStatus= "";
    boolean isPrinterCnnectedfromSP = false;
    String printerNamefromSP = "";
    boolean isinventorycheck = false;

    private  boolean isStockOutGoingAlreadyCalledForthisItem =false;
    public static List<String> StockBalanceChangedForThisItemList = new ArrayList<>();
    JSONArray emptyJsonArray = new JSONArray();



    boolean orderdetailsnewschema = false , localDBcheck =false;
    TMCMenuItemSQL_DB_Manager tmcMenuItemSQL_db_manager;

    Add_CustomerOrder_TrackingTableInterface mResultCallback_Add_CustomerOrder_TrackingTableInterface = null;
    boolean  isCustomerOrdersTableServiceCalled = false;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_replacement__refund__orders_screen);
        orderid_textview_widget = findViewById(R.id.orderid_textview_widget);
        status_text_widget = findViewById(R.id.status_text_widget);
        usermobileno_textview_widget = findViewById(R.id.usermobileno_textview_widget);
        orderplaceddate_textview_widget  = findViewById(R.id.orderplaceddate_textview_widget);
        markeddate_textview_widget  = findViewById(R.id.markeddate_textview_widget);
        totalAmtUserGot_textview_widget = findViewById(R.id.totalAmtUserGot_textview_widget);
        replacementorderAmount_textview_widget = findViewById(R.id.replacementorderAmount_textview_widget);
        refundorder_textview_widget = findViewById(R.id.refundorder_textview_widget);
        balanceorder_textview_widget = findViewById(R.id.balanceorder_textview_widget);
        orderidItemListview = findViewById(R.id.orderidItemListview);
        newOrders_recyclerView = findViewById(R.id.newOrders_recyclerView);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        mobile_ItemTotal_textwidget = findViewById(R.id.mobile_ItemTotal_textwidget);
        mobile_GST_textwidget = findViewById(R.id.mobile_GST_textwidget);
        mobile_ToPay_textwidget = findViewById(R.id.mobile_ToPay_textwidget);
        mobile_checkout_button = findViewById(R.id.mobile_checkout_button);
        addRefund_buttonWidget = findViewById(R.id.addRefund_buttonWidget);
        refundAmt_editwidget  = findViewById(R.id.refundAmt_editwidget);
        replacementParentLayout  = findViewById(R.id.replacementParentLayout);
        refundParentLayout  = findViewById(R.id.refundParentLayout);
        selectedPaymentMode  ="CASH ON DELIVERY";
        OrderdItems_desp = new ArrayList<>();
        completemenuItem = new ArrayList<>();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);


        Bundle bundle = getIntent().getExtras();
        modal_replacementOrderDetails = bundle.getParcelable("data");
        SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        vendorKey = shared.getString("VendorKey","");
        vendorName = shared.getString("VendorName", "");
        localDBcheck = (shared.getBoolean("localdbcheck", false));
        isinventorycheck = (shared.getBoolean("inventoryCheckBool", false));
        StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
        StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
        StoreAddressLine3 = (shared.getString("VendorPincode", ""));
        StoreLanLine = (shared.getString("VendorMobileNumber", ""));
        orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema", false));
        mContext = AddReplacement_Refund_OrdersScreen.this;
       // orderdetailsnewschema = true;

        refundAmt_editwidget.clearFocus();
        try{
            turnoffProgressBarAndResetArray();
            if(localDBcheck){
                getDataFromSQL();
            }
            else {
                getMenuItemArrayFromSharedPreferences();
                completemenuItem= getMenuItemfromString(MenuItems);

            }


           // createEmptyRowInListView("empty");
            CallAdapter();

        }

        catch (Exception e){
            e.printStackTrace();
        }

        addRefund_buttonWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredrefundAmount = refundAmt_editwidget.getText().toString();
                enteredrefundAmount = enteredrefundAmount.replaceAll("[^\\d.]", "");
                double refundAmtdouble = Double.parseDouble(enteredrefundAmount);
                if(balanceAmount>=refundAmtdouble){
                    Currenttime_transactiontable  = getDate_and_time_TransactionTable() ;

                    UpdateReplacementOrderDetailsTable(false, Currenttime_transactiontable, sTime,enteredrefundAmount,modal_replacementOrderDetails, emptyJsonArray);

                }
                else{
                    AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.Cant_apply_refundAmount);

                }
            }

        });
        mobile_checkout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                if(isanyProducthaveZeroAsweight){
                    AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.Orders_weight_Cant_be_leave_unedited);

                }
                else {
                    if ((!Objects.requireNonNull(mobile_ItemTotal_textwidget).getText().toString().equals("0")) && (!Objects.requireNonNull(mobile_ToPay_textwidget).getText().toString().equals("0")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("0.0")) && (!mobile_ToPay_textwidget.getText().toString().equals("0.0")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("0.00")) && (!mobile_ToPay_textwidget.getText().toString().equals("0.00")) && (!mobile_ItemTotal_textwidget.getText().toString().equals("")) && (!mobile_ToPay_textwidget.getText().toString().equals(""))) {
                        if (checkforBarcodeInCart("empty")) {
                            AddReplacement_Refund_OrdersScreen.cart_Item_List.remove("empty");

                            AddReplacement_Refund_OrdersScreen.cartItem_hashmap.remove("empty");
                            CallAdapter();

                        }

                        openBottomSheetToCompleteBilling();
                    } else {
                        AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.Cart_is_empty);

                    }
                }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        try{
            if(orderPlacedDate.equals("") || orderPlacedDate.equals("null") || orderPlacedDate.equals(null)){
                orderPlacedDate =  String.valueOf(modal_replacementOrderDetails.getOrderplaceddate());
            }
        }
        catch (Exception e){
            orderPlacedDate ="";
            e.printStackTrace();
        }
        try{
            oldOrderOrderid = String.valueOf(modal_replacementOrderDetails.getOrderid());
        }
        catch (Exception e){
            oldOrderOrderid = "";
            e.printStackTrace();
        }
        try{
            ordertype_Old = String.valueOf(modal_replacementOrderDetails.getOrdertype());
        }
        catch (Exception e){
            oldOrderOrderid = "";
            e.printStackTrace();
        }


        try{
            orderid_textview_widget.setText(String.valueOf(modal_replacementOrderDetails.getOrderid()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            orderplaceddate_textview_widget.setText(String.valueOf(modal_replacementOrderDetails.getOrderplaceddate()));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            ordermarkeddate = (String.valueOf(modal_replacementOrderDetails.getMarkeddate()));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            usermobileno_textview_widget.setText(String.valueOf(modal_replacementOrderDetails.getMobileno()));
            usermobileNo = modal_replacementOrderDetails.getMobileno().toString();
            usermobileNo = usermobileNo.replace("+91","");
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            status_text_widget.setText(String.valueOf(modal_replacementOrderDetails.getStatus()));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            markeddate_textview_widget.setText(String.valueOf(modal_replacementOrderDetails.getMarkeddate()));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            totalAmtUserGot_textview_widget.setText(String.valueOf(modal_replacementOrderDetails.getAmountusercanavl()));
            totalAmountUserCanAvl_String = String.valueOf(modal_replacementOrderDetails.getAmountusercanavl());
        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            replacementorderAmount_textview_widget.setText(String.valueOf(modal_replacementOrderDetails.getTotalreplacementamount()));
            replacementAmount_String = String.valueOf(modal_replacementOrderDetails.getTotalreplacementamount());

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            refundorder_textview_widget.setText(String.valueOf(modal_replacementOrderDetails.getTotalrefundedamount()));
            refundAmount_String = String.valueOf(modal_replacementOrderDetails.getTotalrefundedamount());

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            refundAmount_Double = Math.round(Double.parseDouble(refundAmount_String));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            replacementAmount_Double = Math.round(Double.parseDouble(replacementAmount_String));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalAmountUserCanAvl_Double = Math.round(Double.parseDouble(totalAmountUserCanAvl_String));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            balanceAmount = totalAmountUserCanAvl_Double - (replacementAmount_Double+refundAmount_Double);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(balanceAmount<=0){
            refundParentLayout.setVisibility(View.GONE);
            replacementParentLayout.setVisibility(View.GONE);

        }

        try{
            balanceorder_textview_widget.setText(String.valueOf(Math.round(balanceAmount)));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try {

             markedItemDetailsJSON  = new JSONArray(modal_replacementOrderDetails.getItemsmarked_String());
            //Log.i("tag","array.length()"+ array.length());
            String arraystring= markedItemDetailsJSON.toString();
            String itemDesp="";



                for(int i=0; i < markedItemDetailsJSON.length(); i++) {
                    JSONObject json = markedItemDetailsJSON.getJSONObject(i);
                    String subCtgyKey ="";
                    Modal_ManageOrders_Pojo_Class manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                    manageOrders_pojo_class.itemdesp_string = String.valueOf(json);
                    if (json.has("netweight")) {
                        manageOrders_pojo_class.ItemFinalWeight = String.valueOf(json.get("netweight"));

                    }
                    else{
                        manageOrders_pojo_class.ItemFinalWeight = "";

                    }
                    try {
                        if(json.has("tmcsubctgykey")) {
                            subCtgyKey = String.valueOf(json.get("tmcsubctgykey"));
                        }
                        else {
                            subCtgyKey = " ";
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    if(subCtgyKey.equals("tmcsubctgy_16")){
                        //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                        manageOrders_pojo_class.itemName = "Grill House "+String.valueOf(json.get("itemname"));

                    }
                    else  if(subCtgyKey.equals("tmcsubctgy_15")){
                        // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                        manageOrders_pojo_class.itemName = "Ready to Cook "+String.valueOf(json.get("itemname"));

                    }
                    else{
                        manageOrders_pojo_class.itemName = String.valueOf(json.get("itemname"));

                    }
                    String cutname ="";
                    try {
                        if(json.has("cutname")) {
                            cutname = String.valueOf(json.get("cutname"));
                        }
                        else {
                            cutname = " ";
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        manageOrders_pojo_class.cutname= String.valueOf(cutname);

                    }
                    catch (Exception E){
                        E.printStackTrace();
                    }


                    manageOrders_pojo_class.ItemFinalPrice= String.valueOf(json.get("tmcprice"));
                    manageOrders_pojo_class.quantity = String.valueOf(json.get("quantity"));
                    manageOrders_pojo_class.GstAmount = String.valueOf(json.get("gstamount"));





                    OrderdItems_desp.add(manageOrders_pojo_class);

                }
            Adapter_Replacement_Refund_OrderScreen_List adapter_forOrderDetails_listview= new Adapter_Replacement_Refund_OrderScreen_List(AddReplacement_Refund_OrdersScreen.this, OrderdItems_desp,AddReplacement_Refund_OrdersScreen.this);
            orderidItemListview.setAdapter(adapter_forOrderDetails_listview);
           // Helper.getListViewSize(orderidItemListview, screenInches);
            helper_weight_cut_Listview.getListViewSize(orderidItemListview, screenInches);





        } catch (JSONException e) {
            e.printStackTrace();

        }

        try{
            JSONArray array  = modal_replacementOrderDetails.getRefunddetails_Array();
            //Log.i("tag","array.length()"+ array.length());

        }
        catch (Exception e){

            e.printStackTrace();
        }

        try{
            JSONArray array  = modal_replacementOrderDetails.getReplacementdetails_Array();
            //Log.i("tag","array.length()"+ array.length());

        }
        catch (Exception e){

            e.printStackTrace();
        }

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

            MenuItemArray.clear();
            try {
                // if (cursor.moveToFirst()) {

                Log.i(" cursor Col count ::  ", String.valueOf(cursor.getColumnCount()));
                Log.i(" cursor count  ::  ", String.valueOf(cursor.getCount()));

                if(cursor.getCount()>0){

                    if(cursor.moveToFirst()) {
                        do {
                            Modal_NewOrderItems  modal_newOrderItems = new Modal_NewOrderItems();
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



                            MenuItemArray.add(modal_newOrderItems);
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
        final SharedPreferences sharedPreferencesMenuitem = getSharedPreferences("MenuList", MODE_PRIVATE);

        Gson gson = new Gson();
        MenuItems= sharedPreferencesMenuitem.getString("MenuList", "");
        if (MenuItems.isEmpty()) {
            Toast.makeText(AddReplacement_Refund_OrdersScreen.this, "There is something error", Toast.LENGTH_LONG).show();
        } else {
            Type type = new TypeToken<List<Modal_MenuItem>>() {
            }.getType();
            MenuItemArray = gson.fromJson(MenuItems, type);
        }

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


    public void CallAdapter() {
        if(cart_Item_List.size()<=0 ){
            mobile_ItemTotal_textwidget.setText("0.00");
            mobile_GST_textwidget.setText("0.00");


            mobile_ToPay_textwidget.setText("0.00");
        }
        else {
            add_amount_ForBillDetails();
        }
        try{
            ordertype_Old = String.valueOf(modal_replacementOrderDetails.getOrdertype());
        }
        catch (Exception e){
            ordertype_Old = "";
            e.printStackTrace();
        }
         adapterPlaceNewReplacementOrder = new Adapter_Place_New_ReplacementOrder_Mobile(AddReplacement_Refund_OrdersScreen.this,cartItem_hashmap, MenuItems, AddReplacement_Refund_OrdersScreen.this,"AddReplacement",ordertype_Old,MenuItemArray,localDBcheck);
        adapterPlaceNewReplacementOrder.setHandler(newHandler());
        newOrders_recyclerView.setLayoutManager(new LinearLayoutManager(AddReplacement_Refund_OrdersScreen.this));
        int sizeofCart =  cartItem_hashmap.size();
        int last_index=sizeofCart-1;
        int sizeofRecyclerView = sizeofCart*520;
        newOrders_recyclerView.setMinimumHeight(sizeofRecyclerView);
        newOrders_recyclerView.setAdapter(adapterPlaceNewReplacementOrder);
        newOrders_recyclerView.scrollToPosition(last_index);


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

    private boolean checkforBarcodeInCart(String itemUniquecode) {
        String search = itemUniquecode;
        for(String str: AddReplacement_Refund_OrdersScreen.cart_Item_List) {
            if(str.trim().contains(search))
                return true;
        }
        return false;
    }

    private void openBottomSheetToCompleteBilling() {
        bottomSheetDialog = new BottomSheetDialog(AddReplacement_Refund_OrdersScreen.this);
        bottomSheetDialog.setContentView(R.layout.billingscreen_mobile_neworderfragment);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        try {
            EditText customermobileno_editwidget = bottomSheetDialog.findViewById(R.id.customermobileno_editwidget);
            EditText discount_editWidget = bottomSheetDialog.findViewById(R.id.discount_editWidget);
            CheckBox userstoreNumberCheckboxWidget = bottomSheetDialog.findViewById(R.id.userstoreNumberCheckboxWidget);
            Button apply_discount_buttonWidget = bottomSheetDialog.findViewById(R.id.apply_discount_buttonWidget);
            Button checkout_button_Widget = bottomSheetDialog.findViewById(R.id.checkout_button_Widget);
            TextView itemtotal_textWidget = bottomSheetDialog.findViewById(R.id.itemtotal_textWidget);
            TextView discountTextWidget = bottomSheetDialog.findViewById(R.id.discountTextWidget);
            TextView toPay_textWidget = bottomSheetDialog.findViewById(R.id.toPay_textWidget);
            Spinner paymentModeSpinner_Widget = bottomSheetDialog.findViewById(R.id.paymentModeSpinner_Widget);
            LinearLayout orderMode_selectionLayout = bottomSheetDialog.findViewById(R.id.orderMode_selectionLayout);
            LinearLayout paymentMode_selectionLayout = bottomSheetDialog.findViewById(R.id.paymentMode_selectionLayout);
            LinearLayout replacementPaymentMode_Layout = bottomSheetDialog.findViewById(R.id.replacementPaymentMode_Layout);
            LinearLayout replacementOrderType_Layout = bottomSheetDialog.findViewById(R.id.replacementOrderType_Layout);
            EditText reason_replacementtextview_Widget = bottomSheetDialog.findViewById(R.id.reason_replacementtextview_Widget);
            LinearLayout customerName_layout = bottomSheetDialog.findViewById(R.id.customerName_layout);
            LinearLayout displaySelectedAddress_parentLayout = bottomSheetDialog.findViewById(R.id.displaySelectedAddress_parentLayout);

            LinearLayout reason_for_replacement_Layout = bottomSheetDialog.findViewById(R.id.reason_for_replacement_Layout);

            Spinner orderTypeSpinner_Widget = bottomSheetDialog.findViewById(R.id.orderTypeSpinner_Widget);
            customermobileno_editwidget.setText(usermobileNo);
            customermobileno_editwidget.setFocusable(false);
            customermobileno_editwidget.setEnabled(false);
            userstoreNumberCheckboxWidget.setEnabled(false);
            userstoreNumberCheckboxWidget.setFocusable(false);
            userstoreNumberCheckboxWidget.setVisibility(View.GONE);
            customerName_layout .setVisibility(View.GONE);
            displaySelectedAddress_parentLayout.setVisibility(View.GONE);


            Objects.requireNonNull(itemtotal_textWidget).setText(finaltoPayAmount);
            Objects.requireNonNull(toPay_textWidget).setText(finaltoPayAmount);
            Objects.requireNonNull(discountTextWidget).setText("0");

            String[] paymentType = getResources().getStringArray(R.array.PaymentMode);
            String[] ordertype = getResources().getStringArray(R.array.OrderType);

            ArrayAdapter<String> arrayAdapterpaymentType = new ArrayAdapter<String>(AddReplacement_Refund_OrdersScreen.this, android.R.layout.simple_spinner_item, paymentType);
            arrayAdapterpaymentType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Objects.requireNonNull(paymentModeSpinner_Widget).setAdapter(arrayAdapterpaymentType);
            orderMode_selectionLayout.setVisibility(View.GONE);
            replacementOrderType_Layout.setVisibility(View.VISIBLE);


            ArrayAdapter<String> arrayAdapterordertype = new ArrayAdapter<String>(AddReplacement_Refund_OrdersScreen.this, android.R.layout.simple_spinner_item, ordertype);
            arrayAdapterordertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Objects.requireNonNull(orderTypeSpinner_Widget).setAdapter(arrayAdapterordertype);
            paymentMode_selectionLayout.setVisibility(View.GONE);
            replacementPaymentMode_Layout.setVisibility(View.VISIBLE);


            reason_for_replacement_Layout.setVisibility(View.GONE);


            discountAmount = "0";
            discount_editWidget.setText("0");
            Objects.requireNonNull(userstoreNumberCheckboxWidget).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        if (vendorKey.equals("vendor_1")) {
                            Objects.requireNonNull(customermobileno_editwidget).setText("8939189102");

                        } else if (vendorKey.equals("vendor_2")) {
                            Objects.requireNonNull(customermobileno_editwidget).setText("9597580128");

                        } else {
                            Objects.requireNonNull(customermobileno_editwidget).setText(StoreLanLine);

                        }
                    } else {
                        Objects.requireNonNull(customermobileno_editwidget).setText("");
                    }
                }
            });


            Objects.requireNonNull(apply_discount_buttonWidget).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (cart_Item_List.size() > 0 && cartItem_hashmap.size() > 0) {

                            if ((!itemtotal_textWidget.getText().toString().equals("0")) || (!toPay_textWidget.getText().toString().equals("0")) || (!itemtotal_textWidget.getText().toString().equals("0.00")) || (!toPay_textWidget.getText().toString().equals("0.00"))) {

                                discountAmount = Objects.requireNonNull(discount_editWidget).getText().toString();
                                if ((!discountAmount.equals("")) && (discountAmount.length() > 0) && (!discountAmount.equals(" "))) {
                                    double discountAmountdouble = Double.parseDouble(discountAmount);
                                    if (discountAmountdouble > 0) {

                                        double toPayAmt = Double.parseDouble(finaltoPayAmount);
                                        if (toPayAmt > discountAmountdouble) {
                                            toPayAmt = toPayAmt - discountAmountdouble;
                                            int toPayAmountInt = (int) Math.round((toPayAmt));
                                            totalAmounttopay = toPayAmt;
                                            discountTextWidget.setText(discountAmount + ".00");
                                            Objects.requireNonNull(toPay_textWidget).setText(String.valueOf(toPayAmountInt) + ".00");
                                        } else {
                                            AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, Constants.DiscountAmountInstruction, 0);

                                        }
                                    } else {
                                        AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, Constants.CantApplyDiscountbelowzeroInstruction, 0);

                                    }

                                } else {
                                    AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, Constants.CantApplyDiscountbelowzeroInstruction, 0);

                                }
                            }

                        } else {
                            AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, Constants.CantApplyDiscountInstruction, 0);

                        }
                    } catch (Exception e) {
                        discountAmount = "0";
                        discountTextWidget.setText(discountAmount + ".00");
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
                                        if (totalAmounttopay <= balanceAmount) {
                                        if (checkforBarcodeInCart("empty")) {
                                            AddReplacement_Refund_OrdersScreen.cart_Item_List.remove("empty");

                                            AddReplacement_Refund_OrdersScreen.cartItem_hashmap.remove("empty");
                                        }

                                        sTime = System.currentTimeMillis();
                                        Currenttime = getDate_and_time();
                                             Currenttime_transactiontable  = getDate_and_time_TransactionTable() ;
                                        //Log.i(TAG, "call adapter cart_Item " + cart_Item_List.size());
                                        if (selectedPaymentMode.equals("CASH")) {
                                            selectedPaymentMode = Constants.CASH_ON_DELIVERY;


                                        }
                                        customermobileno = Objects.requireNonNull(customermobileno_editwidget).getText().toString();
                                        finaltoPayAmountinmethod = toPay_textWidget.getText().toString();

                                            PlaceOrdersinDatabaseaAndPrintRecipt(selectedPaymentMode, finaltoPayAmountinmethod, sTime, Currenttime, cart_Item_List);

                                        bottomSheetDialog.cancel();
                                        //  Toast.makeText(AddReplacement_Refund_OrdersScreen.this, "Selected: " + selectedPaymentMode, Toast.LENGTH_LONG).show();

                                        } else {
                                            showProgressBar(false);

                                            AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.Cant_place_Refundorder);

                                        }
                                    } else {
                                        showProgressBar(false);

                                        AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.Cant_place_order);

                                    }



                            } else {
                                AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.Cart_is_empty);

                            }
                        } else {
                            AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.SelectPaymentMode);

                        }
                    } else {
                        AlertDialogClass.showDialog(AddReplacement_Refund_OrdersScreen.this, R.string.Enter_the_mobile_no_text);

                    }


                }
            });


            bottomSheetDialog.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }




    }

    private void addDataInReplacementTransactiondetails(String currenttime, String usermobileNo, String orderid, JSONArray ItemsDespArray, boolean isReplacementType, String transactionType, double Amountinmethod, String message, JSONArray replacementitemdesp) {

        showProgressBar(true);
        double CouponDiscountAmount_double = 0;
                String CouponDiscountAmount ="0";
        CouponDiscountAmount = discountAmount;
        try {
            try {
                if (!CouponDiscountAmount.equals("")) {
                    CouponDiscountAmount = (CouponDiscountAmount.replaceAll("[^\\d.]", ""));
                    CouponDiscountAmount_double = Double.parseDouble(CouponDiscountAmount);
                } else {
                    CouponDiscountAmount_double = 0;
                }


            } catch (Exception e) {
                CouponDiscountAmount_double = 0;
                e.printStackTrace();
            }

        }
        catch (Exception e){
            CouponDiscountAmount_double = 0;
            e.printStackTrace();
        }


        try{
            usermobileNo = (usermobileNo.replaceAll("\\+91", ""));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        JSONObject jsonObject = new JSONObject();

        try {

            if(isReplacementType) {
                jsonObject.put("replacementorderid", orderid);
                jsonObject.put("replacementorderamount", Amountinmethod);
                if(replacementitemdesp.length()>0) {
                    jsonObject.put("replacementitemdesp", replacementitemdesp);
                }
                if (CouponDiscountAmount_double > 0) {
                    jsonObject.put("discountamount", CouponDiscountAmount_double);

                }
            }
            else{
                jsonObject.put("refundamount", Amountinmethod);

            }
            jsonObject.put("markeditemdesp", markedItemDetailsJSON);
            jsonObject.put("ordermarkeddate", ordermarkeddate);
            jsonObject.put("markeditemamount", totalAmountUserCanAvl_Double);

            jsonObject.put("transactiontime", currenttime);
            jsonObject.put("orderid", oldOrderOrderid);
            jsonObject.put("vendorkey", vendorKey);
            jsonObject.put("transactiontype", transactionType);
            jsonObject.put("transactionstatus", message);
            jsonObject.put("mobileno", "+91"+usermobileNo);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addReplacementTransactionDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);
                showProgressBar(false);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(AddReplacement_Refund_OrdersScreen.this, "Replacement Transaction Details is not updated", Toast.LENGTH_LONG).show();
                showProgressBar(false);

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);





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
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC tmcsubctgykey Json is Missing",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC itemname Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("tmcpriceperkg")){
                            try {
                                String tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));
                                double doubleAmount = Double.parseDouble(tmcpriceperkg);
                                int intAmount = (int) Math.round(doubleAmount);

                                //Log.i("Tag", "doubleAmount" + String.valueOf(intAmount));
                                newOrdersPojoClass.tmcpriceperkg = String.valueOf(json.get("tmcpriceperkg"));
                            }catch (Exception e ){
                                Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Can't Convert  PriceperKg for "+ItemName+"in Menu Item",Toast.LENGTH_LONG).show();

                            }


                        }
                        else{
                            newOrdersPojoClass.tmcpriceperkg = "0";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC PriceperKg Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("grossweight")){
                            newOrdersPojoClass.grossweight = String.valueOf(json.get("grossweight"));

                        }
                        else{
                            newOrdersPojoClass.grossweight = "0";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC grossweight Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("netweight")){
                            newOrdersPojoClass.netweight = String.valueOf(json.get("netweight"));

                        }
                        else{
                            newOrdersPojoClass.netweight = "0";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC netweight Json is Missing",Toast.LENGTH_LONG).show();

                        }

                        if(json.has("inventorydetails")){
                            newOrdersPojoClass.inventorydetails = String.valueOf(json.get("inventorydetails"));

                        }
                        else{
                            newOrdersPojoClass.inventorydetails = "nil";
                            //  Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC netweight Json is Missing",Toast.LENGTH_LONG).show();

                        }


                        if(json.has("itemuniquecode")){
                            newOrdersPojoClass.itemuniquecode = String.valueOf(json.get("itemuniquecode"));

                        }
                        else{
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC itemuniquecode Json is Missing",Toast.LENGTH_LONG).show();

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
                                Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Can't Convert  tmcPrice for "+ItemName+"in Menu Item",Toast.LENGTH_LONG).show();

                            }


                        }
                        else{
                            newOrdersPojoClass.tmcpriceperkg = "0";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC price Json is Missing",Toast.LENGTH_LONG).show();
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC tmcpriceperkg Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("key")){
                            newOrdersPojoClass.menuItemId= String.valueOf(json.get("key"));

                        }
                        else{
                            newOrdersPojoClass.key = "Key for this menu is missing";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC menuItemId Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("portionsize")){
                            newOrdersPojoClass.portionsize = String.valueOf(json.get("portionsize"));

                        }
                        else{
                            newOrdersPojoClass.portionsize = "";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC portionsize Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("pricetypeforpos")){
                            newOrdersPojoClass.pricetypeforpos = String.valueOf(json.get("pricetypeforpos"));

                        }
                        else{
                            newOrdersPojoClass.pricetypeforpos = "0";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC pricetypeforpos Json is Missing",Toast.LENGTH_LONG).show();

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
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC barcode Json is Missing",Toast.LENGTH_LONG).show();

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
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC gstpercentage Json is Missing",Toast.LENGTH_LONG).show();

                        }
                        if(json.has("applieddiscountpercentage")){
                            newOrdersPojoClass.discountpercentage = String.valueOf(json.get("applieddiscountpercentage"));

                        }
                        else{
                            newOrdersPojoClass.discountpercentage = "0";
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"TMC applieddiscountpercentage Json is Missing",Toast.LENGTH_LONG).show();

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
                            Dialog dialog = new Dialog(AddReplacement_Refund_OrdersScreen.this);
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
                                    isCustomerOrdersTableServiceCalled  = false;
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

                //  Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                new TMCAlertDialogClass(AddReplacement_Refund_OrdersScreen.this, R.string.app_name, R.string.Do_You_Want_to_Connect_Printer_Now,
                        R.string.Yes_Text, R.string.No_Text,
                        new TMCAlertDialogClass.AlertListener() {
                            @Override
                            public void onYes() {
                                ConnectPrinter();

                            }

                            @Override
                            public void onNo() {
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
                                            Add_CustomerOrder_TrackingTable_AsyncTask asyncTask=new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, cart_Item_List, cartItem_hashmap, selectedPaymentMode,discountAmount,Currenttime,customermobileno,ordertype,vendorKey,vendorName, sTime,finaltoPayAmountinmethod);
                                            asyncTask.execute();

                                        }

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();

                                    }
                                }
                                if (!isOrderDetailsMethodCalled) {

                                    PlaceOrder_in_OrderDetails(AddReplacement_Refund_OrdersScreen.cart_Item_List, paymentMode, sTime,finaltoPayAmountinmethod,false);
                                }
                                if (!isOrderTrackingDetailsMethodCalled) {

                                    PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                                }
                             /*   if (!isUpdateReplacementDetailsMethodCalled) {

                                    UpdateReplacementOrderDetailsTable(true, Currenttime_transactiontable, sTime,finaltoPayAmountinmethod,modal_replacementOrderDetails, emptyJsonArray);
                                }


                              */



                                try{
                                    totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                                String UserMobile = "+91" + customermobileno;

                                //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                //   Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"points :"+se,Toast.LENGTH_LONG).show();
                             //  //updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);



                                return;

                            }
                        });
            }



            if(!BluetoothPrintDriver.IsNoConnection()){
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (!mBluetoothAdapter.isEnabled()) {
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                    new TMCAlertDialogClass(AddReplacement_Refund_OrdersScreen.this, R.string.app_name, R.string.Bluetooth_turnedOff_Information,
                            R.string.Yes_Text, R.string.No_Text,
                            new TMCAlertDialogClass.AlertListener() {
                                @Override
                                public void onYes() {
                                    ConnectPrinter();

                                }

                                @Override
                                public void onNo() {
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
                                                Add_CustomerOrder_TrackingTable_AsyncTask asyncTask=new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, cart_Item_List, cartItem_hashmap, selectedPaymentMode,discountAmount,Currenttime,customermobileno,ordertype,vendorKey,vendorName, sTime,finaltoPayAmountinmethod);
                                                asyncTask.execute();

                                            }

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();

                                        }
                                    }
                                    if (!isOrderDetailsMethodCalled) {

                                        PlaceOrder_in_OrderDetails(AddReplacement_Refund_OrdersScreen.cart_Item_List, paymentMode, sTime,finaltoPayAmountinmethod,false);
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
                                    //   Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"points :"+se,Toast.LENGTH_LONG).show();
                                //   //updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);



                                    return;
                                }
                            });

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
                                Add_CustomerOrder_TrackingTable_AsyncTask asyncTask=new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, cart_Item_List, cartItem_hashmap, selectedPaymentMode,discountAmount,Currenttime,customermobileno,ordertype,vendorKey,vendorName, sTime,finaltoPayAmountinmethod);
                                asyncTask.execute();

                            }

                        }
                        catch (Exception e){
                            e.printStackTrace();

                        }
                    }
                    if (!isOrderDetailsMethodCalled) {

                        PlaceOrder_in_OrderDetails(AddReplacement_Refund_OrdersScreen.cart_Item_List, paymentMode, sTime,finaltoPayAmountinmethod,true);
                    }
                    if (!isOrderTrackingDetailsMethodCalled) {

                        PlaceOrder_in_OrderTrackingDetails(sTime, currenttime, finaltoPayAmountinmethod);
                    }
                    /*if (!isUpdateReplacementDetailsMethodCalled) {

                        UpdateReplacementOrderDetailsTable(true, Currenttime_transactiontable, sTime,finaltoPayAmountinmethod,modal_replacementOrderDetails, itemDespArray);
                    }

                     */




                    try{
                        totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    String UserMobile = "+91" + customermobileno;

                    //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                    //   Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"points :"+se,Toast.LENGTH_LONG).show();
                  // //updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);


                }


            }




        }


    }

    private void UpdateReplacementOrderDetailsTable(boolean isReplacementCalled, String currenttime_transactionFormat, long sTime, String finaltoPayAmountinmethod, Modal_ReplacementOrderDetails modal_replacementOrderDetails_inMethod, JSONArray itemDespArray) {

        if(isUpdateReplacementDetailsMethodCalled){
            return;
        }
        showProgressBar(true);
        isUpdateReplacementDetailsMethodCalled = true;

        JSONObject refunddetailsObject = new JSONObject();
        JSONArray refunddetailsArray = new JSONArray();

        JSONObject replacementdetailsObject = new JSONObject();
        JSONArray replacementdetailsArray = new JSONArray();
        JSONArray replacementdetailsArray_transactionEntry = new JSONArray();


        String totalReplacementvalueString = "0" , totalRefundValueString = "0",totalAmountUserGotString = "0";
        double totalReplacementvalueDouble = 0 , totalRefundValueDouble = 0,finalAmountDouble=0 , totalAmountUserGotDouble = 0,balanceAmountUsergot = 0;
        try{
            finalAmountDouble = Double.parseDouble(finaltoPayAmountinmethod);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalAmountUserGotString =  modal_replacementOrderDetails_inMethod.getAmountusercanavl().toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalReplacementvalueString =  modal_replacementOrderDetails_inMethod.getTotalreplacementamount().toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalRefundValueString =  modal_replacementOrderDetails_inMethod.getTotalrefundedamount().toString();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalReplacementvalueDouble = Double.parseDouble(totalReplacementvalueString);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalRefundValueDouble = Double.parseDouble(totalRefundValueString);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            totalAmountUserGotDouble = Double.parseDouble(totalAmountUserGotString);

        }
        catch (Exception e){
            totalAmountUserGotDouble=0;
            e.printStackTrace();
        }

        try{
            balanceAmountUsergot = totalAmountUserGotDouble - (totalReplacementvalueDouble + totalRefundValueDouble) - finalAmountDouble ;

        }
        catch (Exception e){
            e.printStackTrace();
        }
        //Log.d(TAG, " uploaduserDatatoDB.");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobileno", modal_replacementOrderDetails_inMethod.getMobileno().toString());


            jsonObject.put("orderid", modal_replacementOrderDetails_inMethod.getOrderid().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(isReplacementCalled) {
            replacementdetailsObject = new JSONObject();
            replacementdetailsArray = new JSONArray();
          /*  try {
                replacementdetailsArray = modal_replacementOrderDetails_inMethod.getReplacementdetails_Array();
            }
            catch (Exception e){
                e.printStackTrace();
            }

           */
            try{
                totalReplacementvalueDouble = totalReplacementvalueDouble + finalAmountDouble;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                if((!modal_replacementOrderDetails_inMethod.getReplacementdetails_String().equals("")) && (!modal_replacementOrderDetails_inMethod.getReplacementdetails_String().equals("[]")) && (!modal_replacementOrderDetails_inMethod.getReplacementdetails_String().equals(null))){
                    JSONArray tempreplacementdetailsArray = new JSONArray(modal_replacementOrderDetails_inMethod.getReplacementdetails_String());
                    if(tempreplacementdetailsArray.length()>0){
                        replacementdetailsArray = tempreplacementdetailsArray;
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                replacementdetailsObject.put("replacementdate",Currenttime);
                replacementdetailsObject.put("replacementorderid",String.valueOf(sTime));
                replacementdetailsObject.put("replacementordervalue",finalAmountDouble);
                replacementdetailsArray.put(replacementdetailsObject);
                replacementdetailsArray_transactionEntry.put(replacementdetailsObject);
                  }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                jsonObject.put("replacementdetails", replacementdetailsArray);


                jsonObject.put("totalreplacementamount", totalReplacementvalueDouble);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            try{

                modal_replacementOrderDetails_inMethod.setReplacementdetails_Array(replacementdetailsArray);
                modal_replacementOrderDetails_inMethod.setReplacementdetails_String(replacementdetailsArray.toString());
                modal_replacementOrderDetails_inMethod.setTotalreplacementamount(String.valueOf(totalReplacementvalueDouble));

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            refunddetailsObject = new JSONObject();
            refunddetailsArray = new JSONArray();
          /*  try {
                refunddetailsArray = modal_replacementOrderDetails_inMethod.getRefunddetails_Array();
            }
            catch (Exception e){
                e.printStackTrace();
            }

           */
            try{
                totalRefundValueDouble = totalRefundValueDouble + finalAmountDouble;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                if((!modal_replacementOrderDetails_inMethod.getRefunddetails_String().equals("")) && (!modal_replacementOrderDetails_inMethod.getRefunddetails_String().equals("[]")) && (!modal_replacementOrderDetails_inMethod.getRefunddetails_String().equals(null))){
                    JSONArray temprefunddetailsArray = new JSONArray(modal_replacementOrderDetails_inMethod.getRefunddetails_String());
                    if(temprefunddetailsArray.length()>0){
                        refunddetailsArray = temprefunddetailsArray;
                    }

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try{
                refunddetailsObject.put("refundeddate",Currenttime);
                refunddetailsObject.put("refundedamount",finalAmountDouble);
                refunddetailsArray.put(refunddetailsObject);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            try {
                jsonObject.put("refunddetails", refunddetailsArray);


                jsonObject.put("totalrefundedamount", totalRefundValueDouble);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            try{

                modal_replacementOrderDetails_inMethod.setRefunddetails_Array(refunddetailsArray);
                modal_replacementOrderDetails_inMethod.setRefunddetails_String(refunddetailsArray.toString());
                modal_replacementOrderDetails_inMethod.setTotalrefundedamount(String.valueOf(totalRefundValueDouble));

            }
            catch (Exception e){
                e.printStackTrace();
            }
            try{
                if(balanceAmountUsergot==0){
                    jsonObject.put("status", "COMPLETED");
                    modal_replacementOrderDetails_inMethod.setStatus("COMPLETED");
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JSONArray finalReplacementdetailsArray = replacementdetailsArray;
        double finalAmountDouble1 = finalAmountDouble;
        double finalBalanceAmountUsergot = balanceAmountUsergot;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_UpdateReplacementOrderDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);

                String message = "";
                Log.d(TAG, "change menu Item " + response.length());
                try {
                    message = response.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (isReplacementCalled){
                    addDataInReplacementTransactiondetails(currenttime_transactionFormat, usermobileNo, String.valueOf(sTime), replacementdetailsArray_transactionEntry, true, "REPLACEMENT", finalAmountDouble1,message,itemDespArray);
                }
                else
                {
                    addDataInReplacementTransactiondetails(currenttime_transactionFormat, usermobileNo, String.valueOf(sTime), replacementdetailsArray_transactionEntry, false, "REFUND", finalAmountDouble1,message, itemDespArray);

                }
                updateDatainLocalArray(modal_replacementOrderDetails_inMethod, finalBalanceAmountUsergot);

                showProgressBar(false);
                isUpdateReplacementDetailsMethodCalled = false;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


                if (isReplacementCalled){
                    addDataInReplacementTransactiondetails(Currenttime, usermobileNo, String.valueOf(sTime), finalReplacementdetailsArray, true, "REPLACEMENT", finalAmountDouble1,"Error", itemDespArray);
                }
                else
                {
                    addDataInReplacementTransactiondetails(Currenttime, usermobileNo, String.valueOf(sTime), finalReplacementdetailsArray, false, "REFUND", finalAmountDouble1,"Error", itemDespArray);

                }
                showProgressBar(false);
                Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();
                isUpdateReplacementDetailsMethodCalled = false;

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
        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);



    }

    private void updateDatainLocalArray(Modal_ReplacementOrderDetails modal_replacementOrderDetails_inMethod, double finalBalanceAmountUsergot) {
        try{
            if(finalBalanceAmountUsergot<=0){
                status_text_widget.setText("COMPLETED");
                refundParentLayout.setVisibility(View.GONE);
                replacementParentLayout.setVisibility(View.GONE);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


        try{
            balanceorder_textview_widget.setText(String.valueOf(Math.round(finalBalanceAmountUsergot)));
            balanceAmount = finalBalanceAmountUsergot;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            refundorder_textview_widget.setText(String.valueOf(modal_replacementOrderDetails_inMethod.getTotalrefundedamount()));

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try{
            replacementorderAmount_textview_widget.setText(String.valueOf(modal_replacementOrderDetails_inMethod.getTotalreplacementamount()));

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{
            balanceorder_textview_widget.setText(String.valueOf(Math.round(balanceAmount)));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        refundAmt_editwidget.setText("");


        modal_replacementOrderDetails = modal_replacementOrderDetails_inMethod;
        String orderid_FromLocalModal = "", orderidFromReplacementFragment ="";
        orderid_FromLocalModal = modal_replacementOrderDetails.getOrderid().toString();
        for(int i= 0; i<replacementRefundListFragment.markedOrdersList.size();i++){
            orderidFromReplacementFragment = replacementRefundListFragment.markedOrdersList.get(i).getOrderid().toString();
            try {
                if (orderidFromReplacementFragment.equals(orderid_FromLocalModal)) {
                    replacementRefundListFragment.markedOrdersList.set(i, modal_replacementOrderDetails);
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


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

    private void PlaceOrder_in_OrderDetails(List<String> cart_Item_List, String Payment_mode, long sTime, String finaltoPayAmountinmethod, boolean shouldGetPrintNow) {
        if(isOrderDetailsMethodCalled){
            return;
        }
        showProgressBar(true);
        isOrderDetailsMethodCalled = true;
        String newOrderId = String.valueOf(sTime);
        SharedPreferences sh
                = AddReplacement_Refund_OrdersScreen.this.getSharedPreferences("VendorLoginData",
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

        String slotname = "";
        if(orderdetailsnewschema){
            slotname = "";

        }
        else{
            slotname = "EXPRESSDELIVERY";

        }
        String orderPlacedDate = getDate();

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
                            Dialog dialog = new Dialog(AddReplacement_Refund_OrdersScreen.this);
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
                                    isCustomerOrdersTableServiceCalled  = false;
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

                                        Modal_NewOrderItems modal_menuItemStockAvlDetails = MenuItemArray.get(iterator_menuitemStockAvlDetails);

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
            jsonObject.put("slottimerange", "");

            jsonObject.put("orderid", orderid);
            jsonObject.put("orderplacedtime", orderplacedTime);
            jsonObject.put("tokenno", (tokenno));
            jsonObject.put("userid", userid);
            if(orderdetailsnewschema) {
                jsonObject.put("usermobileno", UserMobile);
                jsonObject.put("slotdate", orderPlacedDate);

            }
            else{
                jsonObject.put("usermobile", UserMobile);
                jsonObject.put("slotdate", "");

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



        String Api_To_PlaceOrderInOrderDetails = "";
        if(orderdetailsnewschema){
            Api_To_PlaceOrderInOrderDetails = Constants.api_AddVendorOrderDetails;

        }
        else{
            Api_To_PlaceOrderInOrderDetails = Constants.api_addOrderDetailsInOrderDetailsTable;

        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Api_To_PlaceOrderInOrderDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                //Log.d(Constants.TAG, "Response: " + response);
                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        // StartTwice startTwice =new StartTwice(UserMobile,tokenno,itemTotalwithoutGst,taxAmount,payableAmount,orderid,cart_Item_List,cartItem_hashmap,Payment_mode);
                        // startTwice.main();
                        if (!isUpdateReplacementDetailsMethodCalled) {

                            UpdateReplacementOrderDetailsTable(true, Currenttime_transactiontable, sTime,finaltoPayAmountinmethod,modal_replacementOrderDetails,itemDespArray);
                        }
                        if(shouldGetPrintNow) {

                            printRecipt(orderplacedTime, UserMobile, tokenno, itemTotalwithoutGst, taxAmount, finaltoPayAmountinmethod, orderid, cart_Item_List, cartItem_hashmap, Payment_mode, discountAmount, ordertype);

                            //  showProgressBar(false);
                        }
                        else{
                            if(isinventorycheck) {
                                turnoffProgressBarAndResetArray();
                            }

                        }





                    }
                    else{

                        isOrderDetailsMethodCalled = false;
                        showProgressBar(false);
                        Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"OrderDetails is not updated in DB",Toast.LENGTH_SHORT).show();
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

        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);






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

        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);




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
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);





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
                            Toast.makeText(AddReplacement_Refund_OrdersScreen.this, "Error in General App Data code :  " + errorCode, Toast.LENGTH_LONG).show();


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
                    Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);


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
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to Add Data in Stock Outgoing table",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);



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
                        for (int iterator_menuitemStockAvlDetails = 0; iterator_menuitemStockAvlDetails < MenuItemArray.size(); iterator_menuitemStockAvlDetails++) {

                            Modal_NewOrderItems modal_menuItemStockAvlDetails = MenuItemArray.get(iterator_menuitemStockAvlDetails);

                            String menuItemKeyFromMenuAvlDetails = String.valueOf(modal_menuItemStockAvlDetails.getMenuItemId());

                            if (menuItemKey_avlDetails.equals(menuItemKeyFromMenuAvlDetails)) {
                                modal_menuItemStockAvlDetails.setItemavailability(String.valueOf(isitemAvailable));
                                uploadMenuAvailabilityStatusTranscationinDB(usermobileNo,itemName,isitemAvailable,tmcSubCtgyKey,vendorKey,Currenttime,menuItemKey_avlDetails,message, "", false, "");
                                savedMenuIteminSharedPrefrences(MenuItemArray,iterator_menuitemStockAvlDetails);

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
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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
            Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);





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
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to change express delivery slot status in Delivery slots",Toast.LENGTH_LONG).show();
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
                Toast.makeText(AddReplacement_Refund_OrdersScreen.this,"Failed to change express delivery slot status inDelivery slot details",Toast.LENGTH_LONG).show();

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
        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);





    }


    private void savedMenuIteminSharedPrefrences(List<Modal_NewOrderItems> menuItem, int iterator_menuitemStockAvlDetails) {

        final SharedPreferences sharedPreferencesMenuitem = AddReplacement_Refund_OrdersScreen.this.getSharedPreferences("MenuList", MODE_PRIVATE);


        Gson gson = new Gson();
        String json = gson.toJson(menuItem);
        SharedPreferences.Editor editor = sharedPreferencesMenuitem.edit();
        editor.putString("MenuList",json );
        editor.apply();
        try {
            adapterPlaceNewReplacementOrder.notifyDataSetChanged();
            adapterPlaceNewReplacementOrder.notify();
            adapterPlaceNewReplacementOrder.notifyItemChanged(iterator_menuitemStockAvlDetails);

            adapterPlaceNewReplacementOrder.notifyAll();
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
        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);


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
                    else if(vendorKey.equals("vendor_6")){

                        Title = "New NS Bismillah";

                        BluetoothPrintDriver.SetBold((byte) 0x01);//´ÖÌå
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x04);
                        BluetoothPrintDriver.SetFontEnlarge((byte) 0x20);
                        BluetoothPrintDriver.SetAlignMode((byte) 49);
                        BluetoothPrintDriver.printString(Title);
                        BluetoothPrintDriver.BT_Write("\r");
                        BluetoothPrintDriver.LF();

                        /*
                        BluetoothPrintDriver.Begin();
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

                new TMCAlertDialogClass(AddReplacement_Refund_OrdersScreen.this, R.string.app_name, R.string.RePrint_Instruction,
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
                isCustomerOrdersTableServiceCalled  = false;
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
                sTime=0;
                finaltoPayAmountinmethod="";
                isStockOutGoingAlreadyCalledForthisItem = false;
                StockBalanceChangedForThisItemList.clear();

                showProgressBar(false);
                return;

            }
        });



    }

    private void setupChat() {

        Log.d("TAG", "setupChat()");
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothPrintDriver(AddReplacement_Refund_OrdersScreen.this, mHandler);
    }

    private void ConnectPrinter() {


        try{
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            // If the adapter is null, then Bluetooth is not supported
            if (mBluetoothAdapter == null) {
                Toast.makeText(AddReplacement_Refund_OrdersScreen.this, "Bluetooth is not Supported", Toast.LENGTH_LONG).show();
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
                serverIntent = new Intent(AddReplacement_Refund_OrdersScreen.this, DeviceListActivity.class);
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

        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);



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
                = AddReplacement_Refund_OrdersScreen.this.getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);
        String vendorkey = sh.getString("VendorKey","");

        JSONObject  orderTrackingTablejsonObject = new JSONObject();
        try {
            orderTrackingTablejsonObject.put("orderdeliverytime",Currenttime);
            orderTrackingTablejsonObject.put("orderplacedtime",Currenttime);
            if(orderdetailsnewschema){

                orderTrackingTablejsonObject.put("slotdate",getDate());

            }
            orderTrackingTablejsonObject.put("usermobileno","+91" + customermobileno);
            orderTrackingTablejsonObject.put("orderid",orderid);
            orderTrackingTablejsonObject.put("vendorkey",vendorkey);
            orderTrackingTablejsonObject.put("orderstatus","DELIVERED");

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
        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);





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

        Volley.newRequestQueue(AddReplacement_Refund_OrdersScreen.this).add(jsonObjectRequest);


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
    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE", Locale.ENGLISH);
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
    public String getDate_and_time_TransactionTable()
    {

        Date c = Calendar.getInstance().getTime();


        SimpleDateFormat dfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String FormattedTime = dfTime.format(c);

        return FormattedTime;
    }


    // The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
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
                                        Add_CustomerOrder_TrackingTable_AsyncTask asyncTask=new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, cart_Item_List, cartItem_hashmap, selectedPaymentMode,discountAmount,Currenttime,customermobileno,ordertype,vendorKey,vendorName, sTime,finaltoPayAmountinmethod);
                                        asyncTask.execute();

                                    }

                                }
                                catch (Exception e){
                                    e.printStackTrace();

                                }
                            }

                            if (!isOrderDetailsMethodCalled) {

                                PlaceOrder_in_OrderDetails(AddReplacement_Refund_OrdersScreen.cart_Item_List, selectedPaymentMode, sTime,finaltoPayAmountinmethod, true);
                            }
                            if (!isOrderTrackingDetailsMethodCalled) {

                                PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                            }
                            /*if (!isUpdateReplacementDetailsMethodCalled) {

                                UpdateReplacementOrderDetailsTable(true, Currenttime_transactiontable, sTime,finaltoPayAmountinmethod,modal_replacementOrderDetails, itemDespArray);
                            }

                             */




                            try{
                                totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            String UserMobile = "+91" + customermobileno;

                            //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                            //   Toast.makeText(AddReplacement_Refund_OrdersScreen.,"points :"+se,Toast.LENGTH_LONG).show();
                           //updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);

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
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this, msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();


                    if (BluetoothPrintDriver.IsNoConnection()) {

                        //  Toast.makeText(AddReplacement_Refund_OrdersScreen.,"Printer Is Not Connected",Toast.LENGTH_LONG).show();

                        new TMCAlertDialogClass(AddReplacement_Refund_OrdersScreen.this, R.string.app_name, R.string.Do_You_Want_to_Connect_Printer_Now,
                                R.string.Yes_Text, R.string.No_Text,
                                new TMCAlertDialogClass.AlertListener() {
                                    @Override
                                    public void onYes() {
                                        ConnectPrinter();

                                    }

                                    @Override
                                    public void onNo() {

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
                                                    Add_CustomerOrder_TrackingTable_AsyncTask asyncTask=new Add_CustomerOrder_TrackingTable_AsyncTask(mContext, mResultCallback_Add_CustomerOrder_TrackingTableInterface, cart_Item_List, cartItem_hashmap, selectedPaymentMode,discountAmount,Currenttime,customermobileno,ordertype,vendorKey,vendorName, sTime,finaltoPayAmountinmethod);
                                                    asyncTask.execute();

                                                }

                                            }
                                            catch (Exception e){
                                                e.printStackTrace();

                                            }
                                        }


                                        if (!isOrderDetailsMethodCalled) {

                                            PlaceOrder_in_OrderDetails(AddReplacement_Refund_OrdersScreen.cart_Item_List, selectedPaymentMode, sTime,finaltoPayAmountinmethod, false);
                                        }
                                        if (!isOrderTrackingDetailsMethodCalled) {

                                            PlaceOrder_in_OrderTrackingDetails(sTime, Currenttime, finaltoPayAmountinmethod);
                                        }

                                        /*if (!isUpdateReplacementDetailsMethodCalled) {

                                            UpdateReplacementOrderDetailsTable(true, Currenttime_transactiontable, sTime,finaltoPayAmountinmethod,modal_replacementOrderDetails, itemDespArray);
                                        }

                                         */


                                        try{
                                            totalredeempointsusergetfromorder =   Math.round((pointsfor100rs_double*totalAmounttopay)/100);

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        String UserMobile = "+91" + customermobileno;

                                        //  String se =   String.valueOf((int)(totalredeempointsusergetfromorder));
                                        //   Toast.makeText(AddReplacement_Refund_OrdersScreen.,"points :"+se,Toast.LENGTH_LONG).show();
                                       //updateRedeemPointsDetailsInDBWithoutkey(UserMobile,totalAmounttopay,totalredeempointsusergetfromorder);



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
                    serverIntent = new Intent(AddReplacement_Refund_OrdersScreen.this, DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d("TAG", "BT not enabled");
                    Toast.makeText(AddReplacement_Refund_OrdersScreen.this, "bt_not_enabled_leaving", Toast.LENGTH_SHORT).show();
                    //  finish();
                }
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




    private void SaveDatainSharedPreferences(boolean isPrinterCnnected, String printerName, String printerStatus) {
        SharedPreferences sharedPreferences
                = AddReplacement_Refund_OrdersScreen.this.getSharedPreferences("PrinterConnectionData",
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
}