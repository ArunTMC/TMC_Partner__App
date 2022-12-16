package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListData;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListItem;
import com.meatchop.tmcpartner.Settings.report_Activity_model.ListSection;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;

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
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlots;

public class SlotwiseAppOrderslist extends AppCompatActivity {
    LinearLayout PrintReport_Layout, generateReport_Layout, dateSelectorLayout, loadingpanelmask, loadingPanel;
    DatePickerDialog datepicker;
    String vendorKey,orderDetailsJsonString="";
    TextView  fetchData,appOrdersPacksCount_textwidget,dateSelector_text,appOrdersCount_textwidget,listviewInstruction_textview;
    ListView posSalesReport_Listview;

    double screenInches;
    String CurrentDate, PreviousDateString;
    String DateString;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    Spinner SlotrangeSelector_spinner;
    List<String> slotrangeChoosingSpinnerData;
    String selectedTimeRange_spinner = "All";
    int spinner_check = 0;
    List<ListData> dataList = new ArrayList<>();
    Adapter_SlotWiseAppOrders_List adapter = new Adapter_SlotWiseAppOrders_List();

    ArrayList<String> menuItemname_array ;
    public static List<String> tmcSubCtgyName;
    public static HashMap<String, Modal_ManageOrders_Pojo_Class> SubCtgyName_hashmap = new HashMap();

    HashMap<String,Modal_ManageOrders_Pojo_Class> menuItemname_hashmap = new HashMap<>();
    public static HashMap<String, Modal_OrderDetails> SubCtgydetails_hashmap = new HashMap();
    HashMap<String,ArrayList<String>> orderid_hashmap = new HashMap<>();


    public static List<String> menuItemKey_CutWeightdetails = new ArrayList<>();
    public static HashMap<String, Modal_ManageOrders_Pojo_Class> menuItemKey_CutWeightdetailsHashmap = new HashMap();


    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;

    public static List<Modal_ManageOrders_Pojo_Class> orderList = new ArrayList<>();



    String deliveryTimeForExpr_Delivery;

    //    HashMap<String,ArrayList<String>> tokenNo_hashmap = new HashMap<>();
    //  HashMap<String,String> quantity_hashmap = new HashMap<>();
    //   HashMap<String,ArrayList<String>> slottimeRange_hashmap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slotwise_app_orderslist);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        fetchData = findViewById(R.id.fetchData);
        appOrdersPacksCount_textwidget = findViewById(R.id.appOrdersPacksCount_textwidget);
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        posSalesReport_Listview = findViewById(R.id.posSalesReport_Listview);
        listviewInstruction_textview = findViewById(R.id.listviewInstruction_textview);
        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);
        SlotrangeSelector_spinner =  findViewById(R.id.SlotrangeSelector_spinner);
        slotrangeChoosingSpinnerData = new ArrayList<>();
        menuItemname_array =  new ArrayList<>();
        tmcSubCtgyName = new ArrayList<>();
        try {
            CurrentDate = getDate();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        try {
            PreviousDateString = getDatewithNameofthePreviousDay();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        try {

            getTmcSubCtgyList("vendorKey");
            Adjusting_Widgets_Visibility(true);

        }
        catch (Exception e ){
            e.printStackTrace();
        }

        try {


            dateSelector_text.setText(Constants.Empty_Date_Format);
            DateString = (Constants.Empty_Date_Format);
        }
        catch (Exception e ){
            e.printStackTrace();
        }

        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(SlotwiseAppOrderslist .this);
          //  Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);
                //Toast.makeText(this, "DisplayMetrics : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e1){
                e1.printStackTrace();
            }


        }


        try {

            listviewInstruction_textview.setVisibility(View.VISIBLE);
            posSalesReport_Listview.setVisibility(View.GONE);


        }
        catch (Exception e ){
            e.printStackTrace();
        }



        try {



            SharedPreferences sharedPreferences
                    = getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);

            vendorKey = sharedPreferences.getString("VendorKey", "");
            orderdetailsnewschema = (sharedPreferences.getBoolean("orderdetailsnewschema_settings", false));

        }
        catch (Exception e ){
            e.printStackTrace();
        }



        try {

            setDataForFilterSpinners();
        }
        catch (Exception e ){
            e.printStackTrace();
        }


      /*  PrintReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (screenInches > 8) {
                    try {
                        Thread t = new Thread() {
                            public void run() {
                               // printReport();
                            }
                        };
                        t.start();
                    } catch (Exception e) {
                        Toast.makeText(SlotwiseAppOrderslist.this, "Printer is Not Working !! Please Restart the Device", Toast.LENGTH_SHORT).show();

                        e.printStackTrace();

                    }
                } else {
                    Toast.makeText(SlotwiseAppOrderslist.this, "Cant Find a Printer", Toast.LENGTH_LONG).show();
                }
            }
        });


        generateReport_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int writeExternalStoragePermission = ContextCompat.checkSelfPermission(SlotwiseAppOrderslist.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission " + writeExternalStoragePermission);
                // If do not grant write external storage permission.
                if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    // Request user to grant write external storage permission.
                    ActivityCompat.requestPermissions(SlotwiseAppOrderslist.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
                } else {
                    Adjusting_Widgets_Visibility(true);

                    try {
                    //    exportReport();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });


       */


        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orderdetailsnewschema) {
                    String dateAsnewFormat = convertOldFormatDateintoNewFormat(DateString);
                    callVendorOrderDetailsServiceAndInitCallBack(dateAsnewFormat, dateAsnewFormat, vendorKey);


                } else {

                    getOrderForSelectedDate(PreviousDateString,DateString, vendorKey);

                }

            }
        });


        SlotrangeSelector_spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                spinner_check =1;
                return false;
            }
        });


        SlotrangeSelector_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(spinner_check==1) {
                    try {
                        selectedTimeRange_spinner = SlotrangeSelector_spinner.getSelectedItem().toString();

                        if (orderdetailsnewschema) {

                            if (orderList.size() > 0) {
                                Adjusting_Widgets_Visibility(true);

                                processArrayAndgetData( orderList);
                            } else {
                                Toast.makeText(SlotwiseAppOrderslist.this, "There is No Data to Display", Toast.LENGTH_LONG).show();
                            }

                        }
                        else{

                            if (orderDetailsJsonString.length() > 0) {
                                Adjusting_Widgets_Visibility(true);

                                Filterdata(selectedTimeRange_spinner, orderDetailsJsonString);
                            } else {
                                Toast.makeText(SlotwiseAppOrderslist.this, "There is No Data to Display", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    catch (Exception e ){
                        e.printStackTrace();
                    }
                }
                spinner_check=2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SlotwiseAppOrderslist.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
            }
        });


        dateSelectorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    openDatePicker();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void callVendorOrderDetailsServiceAndInitCallBack(String slotDate, String dateAsnewFormat1, String vendorKey) {
        Adjusting_Widgets_Visibility(true);

        if(isVendorOrdersTableServiceCalled){
            Adjusting_Widgets_Visibility(false);
            return;
        }
        isVendorOrdersTableServiceCalled = true;
        mResultCallback = new VendorOrdersTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {
                if(orderslist_fromResponse.size()>0) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("content", orderslist_fromResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    isVendorOrdersTableServiceCalled = false;

                    orderList = orderslist_fromResponse;
                    processArrayAndgetData(orderList);
                }
                else{


                    Adjusting_Widgets_Visibility(false);
                    isVendorOrdersTableServiceCalled = false;

                    try{
                        menuItemname_array.clear();
                        orderid_hashmap.clear();
                        menuItemname_hashmap.clear();
                        SubCtgyName_hashmap.clear();
                        tmcSubCtgyName.clear();
                        orderList.clear();
                        dataList.clear();
                        menuItemKey_CutWeightdetailsHashmap.clear();
                        menuItemKey_CutWeightdetails.clear();
                        listviewInstruction_textview.setVisibility(View.VISIBLE);
                        posSalesReport_Listview.setVisibility(View.GONE);
                          listviewInstruction_textview.setText("There is no orders on this Date");
                              appOrdersCount_textwidget.setText("0");
                                appOrdersPacksCount_textwidget.setText("0");


                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

                Adjusting_Widgets_Visibility(false);
                isVendorOrdersTableServiceCalled = false;

                try{
                    menuItemname_array.clear();
                    orderid_hashmap.clear();
                    menuItemname_hashmap.clear();
                    SubCtgyName_hashmap.clear();
                    tmcSubCtgyName.clear();
                    orderList.clear();
                    dataList.clear();
                    menuItemKey_CutWeightdetailsHashmap.clear();
                    menuItemKey_CutWeightdetails.clear();
                    listviewInstruction_textview.setVisibility(View.VISIBLE);
                    posSalesReport_Listview.setVisibility(View.GONE);
                      listviewInstruction_textview.setText("There is no orders on this Date");
                              appOrdersCount_textwidget.setText("0");
                                appOrdersPacksCount_textwidget.setText("0");


                }
                catch(Exception e){
                    e.printStackTrace();
                }
                Toast.makeText(SlotwiseAppOrderslist.this, "There is Some error on this data", Toast.LENGTH_LONG).show();
                Adjusting_Widgets_Visibility(false);

            }
        };
        try{
            menuItemname_array.clear();
            orderid_hashmap.clear();
            menuItemname_hashmap.clear();
            SubCtgyName_hashmap.clear();
            tmcSubCtgyName.clear();
            orderList.clear();
            dataList.clear();
            menuItemKey_CutWeightdetailsHashmap.clear();
            menuItemKey_CutWeightdetails.clear();

        }
        catch(Exception e){
            e.printStackTrace();
        }
        mVolleyService = new VendorOrdersTableService(mResultCallback,SlotwiseAppOrderslist.this);
        //String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_MultipleOrdertype + "?slotdate="+slotDate+"&vendorkey="+vendorKey+"&ordertype=APPORDER";
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_SingleOrdertype + "?slotdate="+slotDate+"&vendorkey="+vendorKey+"&ordertype=APPORDER";

        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+slotDate+"&vendorkey="+vendorKey;

        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);




    }

    private void processArrayAndgetData(List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {

        try{
            menuItemname_array.clear();
            orderid_hashmap.clear();
            menuItemname_hashmap.clear();
            SubCtgyName_hashmap.clear();
            tmcSubCtgyName.clear();
            menuItemKey_CutWeightdetailsHashmap.clear();
            menuItemKey_CutWeightdetails.clear();
            Adjusting_Widgets_Visibility(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }



        String selectedslot = selectedTimeRange_spinner;
        int arrayLength = orderslist_fromResponse.size();
        for(int i1 =0 ;i1<arrayLength; i1++){

            Modal_ManageOrders_Pojo_Class pojo_class_fromresponseArray = new Modal_ManageOrders_Pojo_Class();

            pojo_class_fromresponseArray = orderslist_fromResponse.get(i1);

            String slottimerange = "",orderid ="",tokenno="", deliverytype="",menuitemkey ="",menuitemkeyCutWeight ="",slotname;




            try {

            try {
                    deliverytype = String.valueOf(pojo_class_fromresponseArray.getDeliverytype());
            }
            catch (Exception e){
                e.printStackTrace();
                deliverytype = "";

            }

                try {
                    slotname = String.valueOf(pojo_class_fromresponseArray.getSlotname());
                }
                catch (Exception e){
                    e.printStackTrace();
                    slotname = "";

                }



            try {
                    slottimerange = String.valueOf(pojo_class_fromresponseArray.getSlottimerange());
                    if (deliverytype.contains(Constants.STOREPICKUP_DELIVERYTYPE)) {
                        slottimerange = Constants.STOREPICKUP_DELIVERYTYPE;
                    }
                    else {
                        if (String.valueOf(slotname).toUpperCase().equals(Constants.EXPRESSDELIVERY_SLOTNAME) || String.valueOf(slotname).toUpperCase().equals(Constants.EXPRESS_DELIVERY_SLOTNAME)) {
                            slottimerange = Constants.EXPRESSDELIVERY_SLOTNAME;
                        }
                    }
               }
            catch (Exception e){
                e.printStackTrace();
                slottimerange = "";

            }


            try{
                if(selectedslot.equals("All")) {
                    try{

                        orderid = String.valueOf(pojo_class_fromresponseArray.getOrderid());


                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    boolean isAllslotAvailable = checkifslottimefororderidislreadyAvailabelinArray("All");
                    try{
                        if (isAllslotAvailable) {

                            ArrayList<String> slottime_array = orderid_hashmap.get("All");
                            slottime_array.add(orderid);


                        } else {

                            ArrayList<String> slottime_array = new ArrayList<>();
                            slottime_array.add(orderid);

                            orderid_hashmap.put("All", slottime_array);
                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                            tokenno = String.valueOf(pojo_class_fromresponseArray.getTokenno());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }



                    try {
                        JSONArray jsonArray = new JSONArray();
                            try{
                                jsonArray = pojo_class_fromresponseArray.getItemdesp();

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }

                            try{

                                if(jsonArray.length()<=0){
                                    jsonArray = new JSONArray(pojo_class_fromresponseArray.getItemdesp_string());

                                }

                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }


                            for (int i = 0; i < jsonArray.length(); i++) {
                                int older_quantity_from_hashmap = 0,
                                        new_quantity = 0;

                                JSONObject itemdespjson = jsonArray.getJSONObject(i);
                                String itemName, tmcSubCtgyKey, tmcsubctgyname = "", quantity = "",weight="",cutname="";
                                try {

                                    if (itemdespjson.has("tmcsubctgykey")) {
                                        tmcSubCtgyKey = itemdespjson.getString("tmcsubctgykey");
                                    } else {
                                        tmcSubCtgyKey = "";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    tmcSubCtgyKey = "";
                                }


                                try {
                                    if (itemdespjson.has("menuitemid")) {
                                        menuitemkey = String.valueOf(itemdespjson.get("menuitemid"));

                                    } else {
                                        menuitemkey = "";
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    menuitemkey = "";

                                }





                                try {
                                    if (itemdespjson.has("itemname")) {
                                        if(tmcSubCtgyKey.equals("tmcsubctgy_16")){
                                            //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                                            itemName = "Grill House "+String.valueOf( itemdespjson.getString("itemname"));

                                        }
                                        else  if(tmcSubCtgyKey.equals("tmcsubctgy_15")){
                                            // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                                            itemName = "Ready to Cook "+String.valueOf( itemdespjson.getString("itemname"));

                                        }
                                        else{
                                            itemName = itemdespjson.getString("itemname");

                                        }
                                    } else {
                                        itemName = "";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    itemName = "";
                                }


                                try {

                                    if (itemdespjson.has("quantity")) {
                                        quantity = (itemdespjson.getString("quantity"));
                                    } else {
                                        quantity = "";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    quantity = "";
                                }





                                try {
                                    tmcsubctgyname = getSubCtgyNameusingSubCtgykey(tmcSubCtgyKey);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    tmcsubctgyname = "";
                                }

                                try {

                                    if (itemdespjson.has("netweight")) {
                                        weight = (itemdespjson.getString("netweight"));

                                    } else {
                                        weight = "";
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    weight = "";
                                }
                                if(weight.equals("") || weight.equals("null")){
                                    try {

                                        if (itemdespjson.has("grossweight")) {
                                            weight = (itemdespjson.getString("grossweight"));

                                        } else {
                                            weight = "";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        weight = "";
                                    }
                                }

                                if(weight.equals("") || weight.equals("null")){
                                    try {

                                        if (itemdespjson.has("portionsize")) {
                                            weight = (itemdespjson.getString("portionsize"));

                                        } else {
                                            weight = "";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        weight = "";
                                    }
                                }

                                try {
                                    if (itemdespjson.has("cutname")) {
                                        cutname = String.valueOf(itemdespjson.get("cutname"));

                                    } else {
                                        cutname = "";
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    cutname = "";

                                }

                                try{
                                    menuitemkeyCutWeight = menuitemkey+weight+cutname;
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                try{
                                    if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                        Log.i("MENUITEM SubCtgy",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);

                                    }

                                    if (!tmcSubCtgyName.contains(tmcsubctgyname)) {
                                        try{
                                            tmcSubCtgyName.add(tmcsubctgyname);
                                            Modal_ManageOrders_Pojo_Class modal = new Modal_ManageOrders_Pojo_Class();
                                            modal.tmcSubCtgyKey = tmcSubCtgyKey;
                                            modal.tmcSubCtgyName = tmcsubctgyname;
                                            modal.totaltmcsubctgyquantity = quantity;
                                            SubCtgyName_hashmap.put(tmcsubctgyname, modal);
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    } else {
                                        try{
                                            Modal_ManageOrders_Pojo_Class modal = SubCtgyName_hashmap.get(tmcsubctgyname);
                                            int quantityfromhashmap = 0;
                                            quantityfromhashmap = Integer.parseInt(modal.getTotaltmcsubctgyquantity());
                                            int newquantity = Integer.parseInt(quantity);
                                            newquantity = quantityfromhashmap + newquantity;
                                            modal.setTotaltmcsubctgyquantity(String.valueOf(newquantity));
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }


                                try{
                                    if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                        Log.i("MENUITEM CUTWEIGHT ",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);

                                    }
                                    if (menuItemKey_CutWeightdetails.contains(menuitemkeyCutWeight)) {

                                        boolean inismenuItemAvailaleinCutWeighthashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                        if (!inismenuItemAvailaleinCutWeighthashmap) {
                                            try{
                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                modal_manageOrders_pojo_class.itemName = itemName;
                                                modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                modal_manageOrders_pojo_class.quantity = quantity;
                                                modal_manageOrders_pojo_class.tokenno = tokenno+" - "+quantity;
                                                modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                modal_manageOrders_pojo_class.cutname = cutname;
                                                menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        } else {
                                            try{
                                                if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                                    Log.i("MENUITEM CUTWEIGHT ",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);

                                                }

                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                int newQuantity = Integer.parseInt(quantity);
                                                quantityfrommap = quantityfrommap + newQuantity;
                                                String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                tokennoFromHashmap = tokennoFromHashmap + "," + tokenno+" - "+quantity;
                                                modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }





                                    }



                                    else {

                                        try{
                                            menuItemKey_CutWeightdetails.add(menuitemkeyCutWeight);

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        boolean inismenuItemAvailaleinhashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                        try{


                                            if (!inismenuItemAvailaleinhashmap) {
                                                try{

                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                    modal_manageOrders_pojo_class.itemName = itemName;
                                                    modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                    modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                    modal_manageOrders_pojo_class.quantity = quantity;
                                                    modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                    modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                    modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                    modal_manageOrders_pojo_class.cutname = cutname;
                                                    menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                try{

                                                    if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                                        Log.i("MENUITEM CUTWEIGHT ",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);

                                                    }
                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                    int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                    int newQuantity = Integer.parseInt(quantity);
                                                    quantityfrommap = quantityfrommap + newQuantity;
                                                    String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                    tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                    modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                    modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
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
                                catch (Exception e){
                                    e.printStackTrace();
                                }




                                try{

                                    if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                        Log.i("MENUITEM",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);

                                    }
                                    if (menuItemname_array.contains(menuitemkey)) {


                                        boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);
                                        if (!inismenuItemAvailaleinhashmap) {
                                            try{
                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                modal_manageOrders_pojo_class.itemName = itemName;
                                                modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                modal_manageOrders_pojo_class.quantity = quantity;
                                                modal_manageOrders_pojo_class.tokenno = tokenno+" - "+quantity;
                                                modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                modal_manageOrders_pojo_class.cutname = cutname;
                                                menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        } else {
                                            try{


                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                int newQuantity = Integer.parseInt(quantity);
                                                quantityfrommap = quantityfrommap + newQuantity;
                                                String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                tokennoFromHashmap = tokennoFromHashmap + "," + tokenno+" - "+quantity;
                                                modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

/*
                                        boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);

                                        if (isItemNamesTokenNoisAvailable) {
                                            ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                            Objects.requireNonNull(tokenNoArray_fromHashmap).add(tokenno);
                                        } else {
                                            ArrayList<String> tokenNoArray = new ArrayList<>();
                                            tokenNoArray.add(tokenno);
                                            tokenNo_hashmap.put(itemName, tokenNoArray);
                                        }

                                        boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                        if (isItemNamesQuantityisAvailable) {
                                            older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                            new_quantity = Integer.parseInt(quantity);
                                            older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                            quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                        } else {
                                            quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                        }


                                        boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                        if (isSlotTimeNameisAvailable) {
                                            ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                            Objects.requireNonNull(slottimeArray_fromHashmap).add(slottimerange);
                                        } else {
                                            ArrayList<String> slottimeArray = new ArrayList<>();
                                            slottimeArray.add(slottimerange);
                                            slottimeRange_hashmap.put(itemName, slottimeArray);
                                        }

*/

                                    }
                                    else {

/*
                                                try{
                                                    menuItemKey_CutWeightdetails.add(menuitemkey);

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                                boolean inismenuItemCutWeightAvailaleinhashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                try{


                                                    if (!inismenuItemCutWeightAvailaleinhashmap) {
                                                        try{

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                            modal_manageOrders_pojo_class.itemName = itemName;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                            modal_manageOrders_pojo_class.quantity = quantity;
                                                            modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.ItemFinalWeight = weight;

                                                            menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);

                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        try{

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                            int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                            int newQuantity = Integer.parseInt(quantity);
                                                            quantityfrommap = quantityfrommap + newQuantity;
                                                            String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                            tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                            modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


 */




                                        try{
                                            menuItemname_array.add(menuitemkey);

                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }

                                        boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);
                                        try{


                                            if (!inismenuItemAvailaleinhashmap) {
                                                try{

                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                    modal_manageOrders_pojo_class.itemName = itemName;
                                                    modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                    modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                    modal_manageOrders_pojo_class.quantity = quantity;
                                                    modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                    modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                    modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                    modal_manageOrders_pojo_class.cutname = cutname;
                                                    menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                try{

                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                    int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                    int newQuantity = Integer.parseInt(quantity);
                                                    quantityfrommap = quantityfrommap + newQuantity;
                                                    String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                    tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                    modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                    modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }
/*
                                        boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);
                                        if (isItemNamesTokenNoisAvailable) {
                                            ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                            tokenNoArray_fromHashmap.add(tokenno);
                                        } else {
                                            ArrayList<String> tokenNoArray = new ArrayList<>();
                                            tokenNoArray.add(tokenno);
                                            tokenNo_hashmap.put(itemName, tokenNoArray);
                                        }

                                        boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                        if (isItemNamesQuantityisAvailable) {
                                            older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                            new_quantity = Integer.parseInt(itemdespjson.getString("quantity"));
                                            older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                            quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                        } else {
                                            quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                        }


                                        boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                        if (isSlotTimeNameisAvailable) {
                                            ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                            slottimeArray_fromHashmap.add(slottimerange);
                                        } else {
                                            ArrayList<String> slottimeArray = new ArrayList<>();
                                            slottimeArray.add(slottimerange);
                                            slottimeRange_hashmap.put(itemName, slottimeArray);
                                        }


*/
                                    }

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);
                }
                else {
                    try{


                        if (slottimerange.equals(selectedslot)) {
                            try{
                                    orderid = String.valueOf(pojo_class_fromresponseArray.getOrderid());



                            }
                            catch (Exception e){
                                orderid = "";

                                e.printStackTrace();
                            }


                            boolean isslotimeAvailable = checkifslottimefororderidislreadyAvailabelinArray(slottimerange);

                            try{


                                if (isslotimeAvailable) {
                                    try{


                                        ArrayList<String> slottime_array = orderid_hashmap.get(slottimerange);
                                        slottime_array.add(orderid);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                }
                                else {
                                    try{


                                        ArrayList<String> slottime_array = new ArrayList<>();
                                        slottime_array.add(orderid);

                                        orderid_hashmap.put(slottimerange, slottime_array);
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }


                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                            try{

                                 tokenno = String.valueOf(pojo_class_fromresponseArray.getTokenno());


                            }
                            catch (Exception e){
                                tokenno = "";

                                e.printStackTrace();
                            }


                            try {

                                JSONArray jsonArray = new JSONArray();
                                try{
                                    jsonArray = pojo_class_fromresponseArray.getItemdesp();

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }

                                try{

                                    if(jsonArray.length()<=0){
                                        jsonArray = new JSONArray(pojo_class_fromresponseArray.getItemdesp_string());

                                    }

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }


                                for (int i = 0; i < jsonArray.length(); i++) {
                                        int older_quantity_from_hashmap = 0,
                                                new_quantity = 0;

                                        JSONObject itemdespjson = jsonArray.getJSONObject(i);
                                        String itemName, tmcSubCtgyKey, tmcsubctgyname = "", quantity = "", weight = "",cutname="";
                                        try {
                                            try {
                                                if (itemdespjson.has("menuitemid")) {
                                                    menuitemkey = String.valueOf(itemdespjson.get("menuitemid"));

                                                } else {
                                                    menuitemkey = "";
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                                menuitemkey = "";

                                            }


                                            if (itemdespjson.has("tmcsubctgykey")) {
                                                tmcSubCtgyKey = itemdespjson.getString("tmcsubctgykey");
                                            } else {
                                                tmcSubCtgyKey = "";
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            tmcSubCtgyKey = "";
                                        }
                                        try {
                                            if (itemdespjson.has("itemname")) {
                                                if(tmcSubCtgyKey.equals("tmcsubctgy_16")){
                                                    //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                                                    itemName = "Grill House "+String.valueOf( itemdespjson.getString("itemname"));

                                                }
                                                else  if(tmcSubCtgyKey.equals("tmcsubctgy_15")){
                                                    // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                                                    itemName = "Ready to Cook "+String.valueOf( itemdespjson.getString("itemname"));

                                                }
                                                else{
                                                    itemName = itemdespjson.getString("itemname");

                                                }
                                            } else {
                                                itemName = "";
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            itemName = "";
                                        }

                                        try {

                                            if (itemdespjson.has("tmcsubctgykey")) {
                                                tmcSubCtgyKey = itemdespjson.getString("tmcsubctgykey");
                                            } else {
                                                tmcSubCtgyKey = "";
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            tmcSubCtgyKey = "";
                                        }
                                        try {

                                            if (itemdespjson.has("quantity")) {
                                                quantity = (itemdespjson.getString("quantity"));
                                            } else {
                                                quantity = "";
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            quantity = "";
                                        }

                                        try {

                                            if (itemdespjson.has("netweight")) {
                                                weight = (itemdespjson.getString("netweight"));

                                            } else {
                                                weight = "";
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            weight = "";
                                        }
                                        if (weight.equals("") || weight.equals("null")) {
                                            try {

                                                if (itemdespjson.has("grossweight")) {
                                                    weight = (itemdespjson.getString("grossweight"));

                                                } else {
                                                    weight = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                weight = "";
                                            }
                                        }

                                        if (weight.equals("") || weight.equals("null")) {
                                            try {

                                                if (itemdespjson.has("portionsize")) {
                                                    weight = (itemdespjson.getString("portionsize"));

                                                } else {
                                                    weight = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                weight = "";
                                            }
                                        }


                                        try {
                                            tmcsubctgyname = getSubCtgyNameusingSubCtgykey(tmcSubCtgyKey);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            tmcsubctgyname = "";
                                        }








                                        try{
                                            if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                                Log.i("MENUITEM SubCtgy",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);

                                            }

                                            if (!tmcSubCtgyName.contains(tmcsubctgyname)) {
                                                try{


                                                    tmcSubCtgyName.add(tmcsubctgyname);
                                                    Modal_ManageOrders_Pojo_Class modal = new Modal_ManageOrders_Pojo_Class();
                                                    modal.tmcSubCtgyKey = tmcSubCtgyKey;
                                                    modal.tmcSubCtgyName = tmcsubctgyname;
                                                    modal.totaltmcsubctgyquantity = quantity;
                                                    SubCtgyName_hashmap.put(tmcsubctgyname, modal);

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                            }
                                            else {
                                                try{


                                                    Modal_ManageOrders_Pojo_Class modal = SubCtgyName_hashmap.get(tmcsubctgyname);
                                                    int quantityfromhashmap = 0;
                                                    quantityfromhashmap = Integer.parseInt(modal.getTotaltmcsubctgyquantity());
                                                    int newquantity = Integer.parseInt(quantity);
                                                    newquantity = quantityfromhashmap + newquantity;
                                                    modal.setTotaltmcsubctgyquantity(String.valueOf(newquantity));
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }




                                        try {
                                            if (itemdespjson.has("cutname")) {
                                                cutname = String.valueOf(itemdespjson.get("cutname"));

                                            } else {
                                                cutname = "";
                                            }
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                            cutname = "";

                                        }

                                        try{
                                            menuitemkeyCutWeight = itemName+weight+cutname;
                                        }
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }





                                        try{
                                            if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                                Log.i("MENUITEM CUTWEIGHT ",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);
                                            }
                                            if (menuItemKey_CutWeightdetails.contains(menuitemkeyCutWeight)) {

                                                boolean inismenuItemAvailaleinCutWeighthashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                if (!inismenuItemAvailaleinCutWeighthashmap) {
                                                    try{
                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                        modal_manageOrders_pojo_class.itemName = itemName;
                                                        modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                        modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                        modal_manageOrders_pojo_class.quantity = quantity;
                                                        modal_manageOrders_pojo_class.tokenno = tokenno+" - "+quantity;
                                                        modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                        modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                        modal_manageOrders_pojo_class.cutname = cutname;
                                                        menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);
                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    try{


                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                        int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                        int newQuantity = Integer.parseInt(quantity);
                                                        quantityfrommap = quantityfrommap + newQuantity;
                                                        String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                        tokennoFromHashmap = tokennoFromHashmap + "," + tokenno+" - "+quantity;
                                                        modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                        modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }





                                            }



                                            else {
                                                try{
                                                    menuItemKey_CutWeightdetails.add(menuitemkeyCutWeight);

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                                boolean inismenuItemAvailaleinhashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                try{


                                                    if (!inismenuItemAvailaleinhashmap) {
                                                        try{

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                            modal_manageOrders_pojo_class.itemName = itemName;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                            modal_manageOrders_pojo_class.quantity = quantity;
                                                            modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                            modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                            modal_manageOrders_pojo_class.cutname = cutname;
                                                            menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);

                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        try{

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                            int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                            int newQuantity = Integer.parseInt(quantity);
                                                            quantityfrommap = quantityfrommap + newQuantity;
                                                            String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                            tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                            modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
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
                                        catch (Exception e){
                                            e.printStackTrace();
                                        }


                                        try{
                                            if(menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")){
                                                Log.i("MENUITEM",itemName + "  ,  "+weight+"  ,  "+cutname+"  ,  "+quantity+"  ,  "+tokenno);

                                            }
                                            if (menuItemname_array.contains(menuitemkey)) {

                                                boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);
                                                try{



                                                    if (!inismenuItemAvailaleinhashmap) {
                                                        try{

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                            modal_manageOrders_pojo_class.itemName = itemName;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                            modal_manageOrders_pojo_class.quantity = quantity;
                                                            modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                            modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                            modal_manageOrders_pojo_class.cutname = cutname;
                                                            menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);

                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }


                                                    else {
                                                        try{



                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                            int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                            int newQuantity = Integer.parseInt(quantity);
                                                            quantityfrommap = quantityfrommap + newQuantity;
                                                            String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                            tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                            modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                        }
                                                        catch (Exception e){

                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }
/*
                                            boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);

                                            if (isItemNamesTokenNoisAvailable) {
                                                ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                                Objects.requireNonNull(tokenNoArray_fromHashmap).add(tokenno);
                                            } else {
                                                ArrayList<String> tokenNoArray = new ArrayList<>();
                                                tokenNoArray.add(tokenno);
                                                tokenNo_hashmap.put(itemName, tokenNoArray);
                                            }

                                            boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                            if (isItemNamesQuantityisAvailable) {
                                                older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                                new_quantity = Integer.parseInt(quantity);
                                                older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                                quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                            } else {
                                                quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                            }


                                            boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                            if (isSlotTimeNameisAvailable) {
                                                ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                                Objects.requireNonNull(slottimeArray_fromHashmap).add(slottimerange);
                                            } else {
                                                ArrayList<String> slottimeArray = new ArrayList<>();
                                                slottimeArray.add(slottimerange);
                                                slottimeRange_hashmap.put(itemName, slottimeArray);
                                            }
*/

                                            }
                                            else {
                                                try{


                                                    menuItemname_array.add(menuitemkey);
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


                                                boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);

                                                try{


                                                    if (!inismenuItemAvailaleinhashmap) {
                                                        try{



                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                            modal_manageOrders_pojo_class.itemName = itemName;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                            modal_manageOrders_pojo_class.quantity = quantity;
                                                            modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                            modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                            modal_manageOrders_pojo_class.cutname = cutname;
                                                            menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);
                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    else {
                                                        try{


                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                            int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                            int newQuantity = Integer.parseInt(quantity);
                                                            quantityfrommap = quantityfrommap + newQuantity;
                                                            String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                            tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                            modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }

/*
                                            boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);
                                            if (isItemNamesTokenNoisAvailable) {
                                                ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                                tokenNoArray_fromHashmap.add(tokenno);
                                            } else {
                                                ArrayList<String> tokenNoArray = new ArrayList<>();
                                                tokenNoArray.add(tokenno);
                                                tokenNo_hashmap.put(itemName, tokenNoArray);
                                            }

                                            boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                            if (isItemNamesQuantityisAvailable) {
                                                older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                                new_quantity = Integer.parseInt(itemdespjson.getString("quantity"));
                                                older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                                quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                            } else {
                                                quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                            }


                                            boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                            if (isSlotTimeNameisAvailable) {
                                                ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                                slottimeArray_fromHashmap.add(slottimerange);
                                            } else {
                                                ArrayList<String> slottimeArray = new ArrayList<>();
                                                slottimeArray.add(slottimerange);
                                                slottimeRange_hashmap.put(itemName, slottimeArray);
                                            }
*/
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
            catch (Exception e){
                e.printStackTrace();

            }




            if(arrayLength-i1==1){
                try {
                    if (menuItemname_hashmap.size() > 0 && menuItemname_array.size() > 0) {
                        //  prepareData(selectedslot,tokenNo_hashmap,quantity_hashmap,slottimeRange_hashmap,menuItemname_array,orderid_hashmap,menuItemname_hashmap, tmcSubCtgyName);
                        try {
                            prepareData(selectedslot, menuItemname_array, orderid_hashmap, menuItemname_hashmap, tmcSubCtgyName);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    else {
                        listviewInstruction_textview.setVisibility(View.VISIBLE);
                        posSalesReport_Listview.setVisibility(View.GONE);
                        Adjusting_Widgets_Visibility(false);
                        listviewInstruction_textview.setText("This Slot have No Orders");
                        appOrdersCount_textwidget.setText("0");
                        appOrdersPacksCount_textwidget.setText("0");
                        spinner_check=0;
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }



        }



    }

    private void getOrderForSelectedDate(String previousDateString, String SlotDate, String vendorKey) {
        Adjusting_Widgets_Visibility(true);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_AppOrders + "?slotdate=" + SlotDate + "&vendorkey=" + vendorKey + "&previousdaydate=" + previousDateString, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            try{
                                orderDetailsJsonString = response.toString();

                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                            try{
                                Filterdata(selectedTimeRange_spinner, orderDetailsJsonString);

                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                            // adapter = new Adapter_AutoCompleteManageOrdersItem(SlotwiseAppOrderslist.this, mobile_jsonString);


                            //    mobile_search_barEditText.setAdapter(adapter);

                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Toast.makeText(SlotwiseAppOrderslist.this, "There is no Orders Yet ", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                    listviewInstruction_textview.setVisibility(View.VISIBLE);
                    posSalesReport_Listview.setVisibility(View.GONE);
                      listviewInstruction_textview.setText("There is no orders on this Date");
                              appOrdersCount_textwidget.setText("0");
                                appOrdersPacksCount_textwidget.setText("0");

                    error.printStackTrace();
                }
            }) {
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
            Volley.newRequestQueue(SlotwiseAppOrderslist.this).add(jsonObjectRequest);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }



    private String getSubCtgyNameusingSubCtgykey(String tmcSubCtgyKey) {
        String subctgyname = "";
        try {
            Modal_OrderDetails modal_orderDetails = SubCtgydetails_hashmap.get(tmcSubCtgyKey);
            subctgyname = Objects.requireNonNull(modal_orderDetails).getTmcsubctgyname();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return subctgyname;
    }

    private void Filterdata(String selectedslot, String jsonString) {
        //      tokenNo_hashmap.clear();
        //    quantity_hashmap.clear();
        //  slottimeRange_hashmap.clear();
        try{
            menuItemname_array.clear();
            orderid_hashmap.clear();
            menuItemname_hashmap.clear();
            SubCtgyName_hashmap.clear();
            tmcSubCtgyName.clear();
            orderList.clear();
            menuItemKey_CutWeightdetailsHashmap.clear();
            menuItemKey_CutWeightdetails.clear();
            Adjusting_Widgets_Visibility(true);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {
            String ordertype="#";

            //converting jsonSTRING into array

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);

            if(arrayLength>0) {
                for (; i1 < (arrayLength); i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                        String slottimerange = "", orderid = "", tokenno = "", deliverytype = "", menuitemkey = "", menuitemkeyCutWeight = "";


                        try {
                            if (json.has("deliverytype")) {
                                deliverytype = String.valueOf(json.get("deliverytype"));

                            } else {
                                deliverytype = "";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            deliverytype = "";

                        }


                        try {
                            if (json.has("slottimerange")) {
                                slottimerange = String.valueOf(json.get("slottimerange"));
                                if (deliverytype.contains(Constants.STOREPICKUP_DELIVERYTYPE)) {
                                    slottimerange = Constants.STOREPICKUP_DELIVERYTYPE;
                                } else {
                                    if (slottimerange.contains("mins")) {
                                        slottimerange = Constants.EXPRESSDELIVERY_SLOTNAME;
                                    }
                                }
                            } else {
                                slottimerange = "";
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            slottimerange = "";

                        }

                        try {
                            if (selectedslot.equals("All")) {
                                try {
                                    if (json.has("orderid")) {
                                        orderid = String.valueOf(json.get("orderid"));

                                    } else {
                                        orderid = "";
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                boolean isAllslotAvailable = checkifslottimefororderidislreadyAvailabelinArray("All");
                                try {
                                    if (isAllslotAvailable) {

                                        ArrayList<String> slottime_array = orderid_hashmap.get("All");
                                        slottime_array.add(orderid);


                                    } else {

                                        ArrayList<String> slottime_array = new ArrayList<>();
                                        slottime_array.add(orderid);

                                        orderid_hashmap.put("All", slottime_array);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (json.has("tokenno")) {
                                        tokenno = String.valueOf(json.get("tokenno"));

                                    } else {
                                        tokenno = "";
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                try {

                                    if (json.has("itemdesp")) {

                                        JSONArray jsonArray = json.getJSONArray("itemdesp");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            int older_quantity_from_hashmap = 0,
                                                    new_quantity = 0;

                                            JSONObject itemdespjson = jsonArray.getJSONObject(i);
                                            String itemName, tmcSubCtgyKey, tmcsubctgyname = "", quantity = "", weight = "", cutname = "";
                                            try {

                                                if (itemdespjson.has("tmcsubctgykey")) {
                                                    tmcSubCtgyKey = itemdespjson.getString("tmcsubctgykey");
                                                } else {
                                                    tmcSubCtgyKey = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                tmcSubCtgyKey = "";
                                            }


                                            try {
                                                if (itemdespjson.has("menuitemid")) {
                                                    menuitemkey = String.valueOf(itemdespjson.get("menuitemid"));

                                                } else {
                                                    menuitemkey = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                menuitemkey = "";

                                            }


                                            try {
                                                if (itemdespjson.has("itemname")) {
                                                    if (tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                                                        //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                                                        itemName = "Grill House " + String.valueOf(itemdespjson.getString("itemname"));

                                                    } else if (tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                                                        // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                                                        itemName = "Ready to Cook " + String.valueOf(itemdespjson.getString("itemname"));

                                                    } else {
                                                        itemName = itemdespjson.getString("itemname");

                                                    }
                                                } else {
                                                    itemName = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                itemName = "";
                                            }


                                            try {

                                                if (itemdespjson.has("quantity")) {
                                                    quantity = (itemdespjson.getString("quantity"));
                                                } else {
                                                    quantity = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                quantity = "";
                                            }


                                            try {
                                                tmcsubctgyname = getSubCtgyNameusingSubCtgykey(tmcSubCtgyKey);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                tmcsubctgyname = "";
                                            }

                                            try {

                                                if (itemdespjson.has("netweight")) {
                                                    weight = (itemdespjson.getString("netweight"));

                                                } else {
                                                    weight = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                weight = "";
                                            }
                                            if (weight.equals("") || weight.equals("null")) {
                                                try {

                                                    if (itemdespjson.has("grossweight")) {
                                                        weight = (itemdespjson.getString("grossweight"));

                                                    } else {
                                                        weight = "";
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    weight = "";
                                                }
                                            }

                                            if (weight.equals("") || weight.equals("null")) {
                                                try {

                                                    if (itemdespjson.has("portionsize")) {
                                                        weight = (itemdespjson.getString("portionsize"));

                                                    } else {
                                                        weight = "";
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    weight = "";
                                                }
                                            }

                                            try {
                                                if (itemdespjson.has("cutname")) {
                                                    cutname = String.valueOf(itemdespjson.get("cutname"));

                                                } else {
                                                    cutname = "";
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                cutname = "";

                                            }

                                            try {
                                                menuitemkeyCutWeight = menuitemkey + weight + cutname;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            try {
                                                if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                    Log.i("MENUITEM SubCtgy", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);

                                                }

                                                if (!tmcSubCtgyName.contains(tmcsubctgyname)) {
                                                    try {
                                                        tmcSubCtgyName.add(tmcsubctgyname);
                                                        Modal_ManageOrders_Pojo_Class modal = new Modal_ManageOrders_Pojo_Class();
                                                        modal.tmcSubCtgyKey = tmcSubCtgyKey;
                                                        modal.tmcSubCtgyName = tmcsubctgyname;
                                                        modal.totaltmcsubctgyquantity = quantity;
                                                        SubCtgyName_hashmap.put(tmcsubctgyname, modal);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    try {
                                                        Modal_ManageOrders_Pojo_Class modal = SubCtgyName_hashmap.get(tmcsubctgyname);
                                                        int quantityfromhashmap = 0;
                                                        quantityfromhashmap = Integer.parseInt(modal.getTotaltmcsubctgyquantity());
                                                        int newquantity = Integer.parseInt(quantity);
                                                        newquantity = quantityfromhashmap + newquantity;
                                                        modal.setTotaltmcsubctgyquantity(String.valueOf(newquantity));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            try {
                                                if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                    Log.i("MENUITEM CUTWEIGHT ", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);

                                                }
                                                if (menuItemKey_CutWeightdetails.contains(menuitemkeyCutWeight)) {

                                                    boolean inismenuItemAvailaleinCutWeighthashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                    if (!inismenuItemAvailaleinCutWeighthashmap) {
                                                        try {
                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                            modal_manageOrders_pojo_class.itemName = itemName;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                            modal_manageOrders_pojo_class.quantity = quantity;
                                                            modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                            modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                            modal_manageOrders_pojo_class.cutname = cutname;
                                                            menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    } else {
                                                        try {
                                                            if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                                Log.i("MENUITEM CUTWEIGHT ", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);

                                                            }

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                            int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                            int newQuantity = Integer.parseInt(quantity);
                                                            quantityfrommap = quantityfrommap + newQuantity;
                                                            String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                            tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                            modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }


                                                } else {

                                                    try {
                                                        menuItemKey_CutWeightdetails.add(menuitemkeyCutWeight);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    boolean inismenuItemAvailaleinhashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                    try {


                                                        if (!inismenuItemAvailaleinhashmap) {
                                                            try {

                                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                                modal_manageOrders_pojo_class.itemName = itemName;
                                                                modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                                modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                                modal_manageOrders_pojo_class.quantity = quantity;
                                                                modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                                modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                                modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                                modal_manageOrders_pojo_class.cutname = cutname;
                                                                menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            try {

                                                                if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                                    Log.i("MENUITEM CUTWEIGHT ", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);

                                                                }
                                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                                int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                                int newQuantity = Integer.parseInt(quantity);
                                                                quantityfrommap = quantityfrommap + newQuantity;
                                                                String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                                tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                                modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                                modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }


                                            try {

                                                if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                    Log.i("MENUITEM", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);

                                                }
                                                if (menuItemname_array.contains(menuitemkey)) {


                                                    boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);
                                                    if (!inismenuItemAvailaleinhashmap) {
                                                        try {
                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                            modal_manageOrders_pojo_class.itemName = itemName;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                            modal_manageOrders_pojo_class.quantity = quantity;
                                                            modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                            modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                            modal_manageOrders_pojo_class.cutname = cutname;
                                                            menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    } else {
                                                        try {


                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                            int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                            int newQuantity = Integer.parseInt(quantity);
                                                            quantityfrommap = quantityfrommap + newQuantity;
                                                            String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                            tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                            modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

/*
                                        boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);

                                        if (isItemNamesTokenNoisAvailable) {
                                            ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                            Objects.requireNonNull(tokenNoArray_fromHashmap).add(tokenno);
                                        } else {
                                            ArrayList<String> tokenNoArray = new ArrayList<>();
                                            tokenNoArray.add(tokenno);
                                            tokenNo_hashmap.put(itemName, tokenNoArray);
                                        }

                                        boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                        if (isItemNamesQuantityisAvailable) {
                                            older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                            new_quantity = Integer.parseInt(quantity);
                                            older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                            quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                        } else {
                                            quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                        }


                                        boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                        if (isSlotTimeNameisAvailable) {
                                            ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                            Objects.requireNonNull(slottimeArray_fromHashmap).add(slottimerange);
                                        } else {
                                            ArrayList<String> slottimeArray = new ArrayList<>();
                                            slottimeArray.add(slottimerange);
                                            slottimeRange_hashmap.put(itemName, slottimeArray);
                                        }

*/

                                                } else {

/*
                                                try{
                                                    menuItemKey_CutWeightdetails.add(menuitemkey);

                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }

                                                boolean inismenuItemCutWeightAvailaleinhashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                try{


                                                    if (!inismenuItemCutWeightAvailaleinhashmap) {
                                                        try{

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                            modal_manageOrders_pojo_class.itemName = itemName;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                            modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                            modal_manageOrders_pojo_class.quantity = quantity;
                                                            modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.ItemFinalWeight = weight;

                                                            menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);

                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    } else {
                                                        try{

                                                            Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                            int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                            int newQuantity = Integer.parseInt(quantity);
                                                            quantityfrommap = quantityfrommap + newQuantity;
                                                            String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                            tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                            modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                            modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                        }
                                                        catch (Exception e){
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                                catch (Exception e){
                                                    e.printStackTrace();
                                                }


 */


                                                    try {
                                                        menuItemname_array.add(menuitemkey);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                    boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);
                                                    try {


                                                        if (!inismenuItemAvailaleinhashmap) {
                                                            try {

                                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                                modal_manageOrders_pojo_class.itemName = itemName;
                                                                modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                                modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                                modal_manageOrders_pojo_class.quantity = quantity;
                                                                modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                                modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                                modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                                modal_manageOrders_pojo_class.cutname = cutname;
                                                                menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            try {

                                                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                                int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                                int newQuantity = Integer.parseInt(quantity);
                                                                quantityfrommap = quantityfrommap + newQuantity;
                                                                String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                                tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                                modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                                modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
/*
                                        boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);
                                        if (isItemNamesTokenNoisAvailable) {
                                            ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                            tokenNoArray_fromHashmap.add(tokenno);
                                        } else {
                                            ArrayList<String> tokenNoArray = new ArrayList<>();
                                            tokenNoArray.add(tokenno);
                                            tokenNo_hashmap.put(itemName, tokenNoArray);
                                        }

                                        boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                        if (isItemNamesQuantityisAvailable) {
                                            older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                            new_quantity = Integer.parseInt(itemdespjson.getString("quantity"));
                                            older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                            quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                        } else {
                                            quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                        }


                                        boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                        if (isSlotTimeNameisAvailable) {
                                            ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                            slottimeArray_fromHashmap.add(slottimerange);
                                        } else {
                                            ArrayList<String> slottimeArray = new ArrayList<>();
                                            slottimeArray.add(slottimerange);
                                            slottimeRange_hashmap.put(itemName, slottimeArray);
                                        }


*/
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        Log.i(Constants.TAG, "Can't Get itemDesp");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);
                            } else {
                                try {


                                    if (slottimerange.equals(selectedslot)) {
                                        try {
                                            if (json.has("orderid")) {
                                                orderid = String.valueOf(json.get("orderid"));

                                            } else {
                                                orderid = "";
                                            }

                                        } catch (Exception e) {
                                            orderid = "";

                                            e.printStackTrace();
                                        }


                                        boolean isslotimeAvailable = checkifslottimefororderidislreadyAvailabelinArray(slottimerange);

                                        try {


                                            if (isslotimeAvailable) {
                                                try {


                                                    ArrayList<String> slottime_array = orderid_hashmap.get(slottimerange);
                                                    slottime_array.add(orderid);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                try {


                                                    ArrayList<String> slottime_array = new ArrayList<>();
                                                    slottime_array.add(orderid);

                                                    orderid_hashmap.put(slottimerange, slottime_array);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }


                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        try {


                                            if (json.has("tokenno")) {
                                                tokenno = String.valueOf(json.get("tokenno"));

                                            } else {
                                                tokenno = "";
                                            }

                                        } catch (Exception e) {
                                            tokenno = "";

                                            e.printStackTrace();
                                        }


                                        try {

                                            if (json.has("itemdesp")) {

                                                JSONArray jsonArray = json.getJSONArray("itemdesp");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    int older_quantity_from_hashmap = 0,
                                                            new_quantity = 0;

                                                    JSONObject itemdespjson = jsonArray.getJSONObject(i);
                                                    String itemName, tmcSubCtgyKey, tmcsubctgyname = "", quantity = "", weight = "", cutname = "";
                                                    try {
                                                        try {
                                                            if (itemdespjson.has("menuitemid")) {
                                                                menuitemkey = String.valueOf(itemdespjson.get("menuitemid"));

                                                            } else {
                                                                menuitemkey = "";
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            menuitemkey = "";

                                                        }


                                                        if (itemdespjson.has("tmcsubctgykey")) {
                                                            tmcSubCtgyKey = itemdespjson.getString("tmcsubctgykey");
                                                        } else {
                                                            tmcSubCtgyKey = "";
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        tmcSubCtgyKey = "";
                                                    }
                                                    try {
                                                        if (itemdespjson.has("itemname")) {
                                                            if (tmcSubCtgyKey.equals("tmcsubctgy_16")) {
                                                                //  itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Grill House ) "), quantity);
                                                                itemName = "Grill House " + String.valueOf(itemdespjson.getString("itemname"));

                                                            } else if (tmcSubCtgyKey.equals("tmcsubctgy_15")) {
                                                                // itemDesp = String.format("%s %s * %s", marinadeitemName + "  with ", itemName+(" ( Ready to Cook ) "), quantity);
                                                                itemName = "Ready to Cook " + String.valueOf(itemdespjson.getString("itemname"));

                                                            } else {
                                                                itemName = itemdespjson.getString("itemname");

                                                            }
                                                        } else {
                                                            itemName = "";
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        itemName = "";
                                                    }

                                                    try {

                                                        if (itemdespjson.has("tmcsubctgykey")) {
                                                            tmcSubCtgyKey = itemdespjson.getString("tmcsubctgykey");
                                                        } else {
                                                            tmcSubCtgyKey = "";
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        tmcSubCtgyKey = "";
                                                    }
                                                    try {

                                                        if (itemdespjson.has("quantity")) {
                                                            quantity = (itemdespjson.getString("quantity"));
                                                        } else {
                                                            quantity = "";
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        quantity = "";
                                                    }

                                                    try {

                                                        if (itemdespjson.has("netweight")) {
                                                            weight = (itemdespjson.getString("netweight"));

                                                        } else {
                                                            weight = "";
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        weight = "";
                                                    }
                                                    if (weight.equals("") || weight.equals("null")) {
                                                        try {

                                                            if (itemdespjson.has("grossweight")) {
                                                                weight = (itemdespjson.getString("grossweight"));

                                                            } else {
                                                                weight = "";
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            weight = "";
                                                        }
                                                    }

                                                    if (weight.equals("") || weight.equals("null")) {
                                                        try {

                                                            if (itemdespjson.has("portionsize")) {
                                                                weight = (itemdespjson.getString("portionsize"));

                                                            } else {
                                                                weight = "";
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            weight = "";
                                                        }
                                                    }


                                                    try {
                                                        tmcsubctgyname = getSubCtgyNameusingSubCtgykey(tmcSubCtgyKey);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        tmcsubctgyname = "";
                                                    }


                                                    try {
                                                        if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                            Log.i("MENUITEM SubCtgy", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);

                                                        }

                                                        if (!tmcSubCtgyName.contains(tmcsubctgyname)) {
                                                            try {


                                                                tmcSubCtgyName.add(tmcsubctgyname);
                                                                Modal_ManageOrders_Pojo_Class modal = new Modal_ManageOrders_Pojo_Class();
                                                                modal.tmcSubCtgyKey = tmcSubCtgyKey;
                                                                modal.tmcSubCtgyName = tmcsubctgyname;
                                                                modal.totaltmcsubctgyquantity = quantity;
                                                                SubCtgyName_hashmap.put(tmcsubctgyname, modal);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        } else {
                                                            try {


                                                                Modal_ManageOrders_Pojo_Class modal = SubCtgyName_hashmap.get(tmcsubctgyname);
                                                                int quantityfromhashmap = 0;
                                                                quantityfromhashmap = Integer.parseInt(modal.getTotaltmcsubctgyquantity());
                                                                int newquantity = Integer.parseInt(quantity);
                                                                newquantity = quantityfromhashmap + newquantity;
                                                                modal.setTotaltmcsubctgyquantity(String.valueOf(newquantity));
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }


                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        if (itemdespjson.has("cutname")) {
                                                            cutname = String.valueOf(itemdespjson.get("cutname"));

                                                        } else {
                                                            cutname = "";
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        cutname = "";

                                                    }

                                                    try {
                                                        menuitemkeyCutWeight = itemName + weight + cutname;
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                            Log.i("MENUITEM CUTWEIGHT ", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);
                                                        }
                                                        if (menuItemKey_CutWeightdetails.contains(menuitemkeyCutWeight)) {

                                                            boolean inismenuItemAvailaleinCutWeighthashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                            if (!inismenuItemAvailaleinCutWeighthashmap) {
                                                                try {
                                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                                    modal_manageOrders_pojo_class.itemName = itemName;
                                                                    modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                                    modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                                    modal_manageOrders_pojo_class.quantity = quantity;
                                                                    modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                                    modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                                    modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                                    modal_manageOrders_pojo_class.cutname = cutname;
                                                                    menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }

                                                            } else {
                                                                try {


                                                                    Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                                    int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                                    int newQuantity = Integer.parseInt(quantity);
                                                                    quantityfrommap = quantityfrommap + newQuantity;
                                                                    String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                                    tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                                    modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                                    modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }


                                                        } else {
                                                            try {
                                                                menuItemKey_CutWeightdetails.add(menuitemkeyCutWeight);

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                            boolean inismenuItemAvailaleinhashmap = checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(menuitemkeyCutWeight);
                                                            try {


                                                                if (!inismenuItemAvailaleinhashmap) {
                                                                    try {

                                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                                        modal_manageOrders_pojo_class.itemName = itemName;
                                                                        modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                                        modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                                        modal_manageOrders_pojo_class.quantity = quantity;
                                                                        modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                                        modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                                        modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                                        modal_manageOrders_pojo_class.cutname = cutname;
                                                                        menuItemKey_CutWeightdetailsHashmap.put(menuitemkeyCutWeight, modal_manageOrders_pojo_class);

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                } else {
                                                                    try {

                                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemKey_CutWeightdetailsHashmap.get(menuitemkeyCutWeight);
                                                                        int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                                        int newQuantity = Integer.parseInt(quantity);
                                                                        quantityfrommap = quantityfrommap + newQuantity;
                                                                        String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                                        tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                                        modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                                        modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                    try {
                                                        if (menuitemkey.equals("83912358-d817-486a-92da-dd60a6d40ef9")) {
                                                            Log.i("MENUITEM", itemName + "  ,  " + weight + "  ,  " + cutname + "  ,  " + quantity + "  ,  " + tokenno);

                                                        }
                                                        if (menuItemname_array.contains(menuitemkey)) {

                                                            boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);
                                                            try {


                                                                if (!inismenuItemAvailaleinhashmap) {
                                                                    try {

                                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                                        modal_manageOrders_pojo_class.itemName = itemName;
                                                                        modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                                        modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                                        modal_manageOrders_pojo_class.quantity = quantity;
                                                                        modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                                        modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                                        modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                                        modal_manageOrders_pojo_class.cutname = cutname;
                                                                        menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                } else {
                                                                    try {


                                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                                        int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                                        int newQuantity = Integer.parseInt(quantity);
                                                                        quantityfrommap = quantityfrommap + newQuantity;
                                                                        String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                                        tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                                        modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                                        modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                                    } catch (Exception e) {

                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
/*
                                            boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);

                                            if (isItemNamesTokenNoisAvailable) {
                                                ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                                Objects.requireNonNull(tokenNoArray_fromHashmap).add(tokenno);
                                            } else {
                                                ArrayList<String> tokenNoArray = new ArrayList<>();
                                                tokenNoArray.add(tokenno);
                                                tokenNo_hashmap.put(itemName, tokenNoArray);
                                            }

                                            boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                            if (isItemNamesQuantityisAvailable) {
                                                older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                                new_quantity = Integer.parseInt(quantity);
                                                older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                                quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                            } else {
                                                quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                            }


                                            boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                            if (isSlotTimeNameisAvailable) {
                                                ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                                Objects.requireNonNull(slottimeArray_fromHashmap).add(slottimerange);
                                            } else {
                                                ArrayList<String> slottimeArray = new ArrayList<>();
                                                slottimeArray.add(slottimerange);
                                                slottimeRange_hashmap.put(itemName, slottimeArray);
                                            }
*/

                                                        } else {
                                                            try {


                                                                menuItemname_array.add(menuitemkey);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }


                                                            boolean inismenuItemAvailaleinhashmap = checkifitmnameinhashmapisAlreadyAvailabelinArray(menuitemkey);

                                                            try {


                                                                if (!inismenuItemAvailaleinhashmap) {
                                                                    try {


                                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = new Modal_ManageOrders_Pojo_Class();
                                                                        modal_manageOrders_pojo_class.itemName = itemName;
                                                                        modal_manageOrders_pojo_class.tmcSubCtgyKey = tmcSubCtgyKey;
                                                                        modal_manageOrders_pojo_class.tmcSubCtgyName = tmcsubctgyname;
                                                                        modal_manageOrders_pojo_class.quantity = quantity;
                                                                        modal_manageOrders_pojo_class.tokenno = tokenno + " - " + quantity;
                                                                        modal_manageOrders_pojo_class.ItemFinalWeight = weight;
                                                                        modal_manageOrders_pojo_class.menuItemKey = menuitemkey;
                                                                        modal_manageOrders_pojo_class.cutname = cutname;
                                                                        menuItemname_hashmap.put(menuitemkey, modal_manageOrders_pojo_class);
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                } else {
                                                                    try {


                                                                        Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(menuitemkey);
                                                                        int quantityfrommap = Integer.parseInt(modal_manageOrders_pojo_class.getQuantity());
                                                                        int newQuantity = Integer.parseInt(quantity);
                                                                        quantityfrommap = quantityfrommap + newQuantity;
                                                                        String tokennoFromHashmap = modal_manageOrders_pojo_class.getTokenno();
                                                                        tokennoFromHashmap = tokennoFromHashmap + "," + tokenno + " - " + quantity;
                                                                        modal_manageOrders_pojo_class.setTokenno(tokennoFromHashmap);
                                                                        modal_manageOrders_pojo_class.setQuantity(String.valueOf(quantityfrommap));
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

/*
                                            boolean isItemNamesTokenNoisAvailable = checkifTokenNoisAlreadyAvailabelinArray(itemName);
                                            if (isItemNamesTokenNoisAvailable) {
                                                ArrayList<String> tokenNoArray_fromHashmap = tokenNo_hashmap.get(itemName);
                                                tokenNoArray_fromHashmap.add(tokenno);
                                            } else {
                                                ArrayList<String> tokenNoArray = new ArrayList<>();
                                                tokenNoArray.add(tokenno);
                                                tokenNo_hashmap.put(itemName, tokenNoArray);
                                            }

                                            boolean isItemNamesQuantityisAvailable = checkifquantityislreadyAvailabelinArray(itemName);
                                            if (isItemNamesQuantityisAvailable) {
                                                older_quantity_from_hashmap = Integer.parseInt(Objects.requireNonNull(quantity_hashmap.get(itemName)));
                                                new_quantity = Integer.parseInt(itemdespjson.getString("quantity"));
                                                older_quantity_from_hashmap = older_quantity_from_hashmap + new_quantity;
                                                quantity_hashmap.put(itemName, String.valueOf(older_quantity_from_hashmap));
                                            } else {
                                                quantity_hashmap.put(itemName, String.valueOf(itemdespjson.getString("quantity")));

                                            }


                                            boolean isSlotTimeNameisAvailable = checkifslottimeisAlreadyAvailabelinArray(itemName);
                                            if (isSlotTimeNameisAvailable) {
                                                ArrayList<String> slottimeArray_fromHashmap = slottimeRange_hashmap.get(itemName);
                                                slottimeArray_fromHashmap.add(slottimerange);
                                            } else {
                                                ArrayList<String> slottimeArray = new ArrayList<>();
                                                slottimeArray.add(slottimerange);
                                                slottimeRange_hashmap.put(itemName, slottimeArray);
                                            }
*/
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            } else {
                                                Log.i(Constants.TAG, "Can't Get itemDesp");
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                        Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                        //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());


                    }


                    if (arrayLength - i1 == 1) {
                        try {
                            if (menuItemname_hashmap.size() > 0 && menuItemname_array.size() > 0) {
                                //  prepareData(selectedslot,tokenNo_hashmap,quantity_hashmap,slottimeRange_hashmap,menuItemname_array,orderid_hashmap,menuItemname_hashmap, tmcSubCtgyName);
                                try {
                                    prepareData(selectedslot, menuItemname_array, orderid_hashmap, menuItemname_hashmap, tmcSubCtgyName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                listviewInstruction_textview.setVisibility(View.VISIBLE);
                                posSalesReport_Listview.setVisibility(View.GONE);
                                Adjusting_Widgets_Visibility(false);
                                listviewInstruction_textview.setText("This Slot have No Orders");
                                appOrdersCount_textwidget.setText("0");
                                appOrdersPacksCount_textwidget.setText("0");
                                spinner_check = 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
            else{
                listviewInstruction_textview.setVisibility(View.VISIBLE);
                posSalesReport_Listview.setVisibility(View.GONE);
                  listviewInstruction_textview.setText("There is no orders on this Date");
                              appOrdersCount_textwidget.setText("0");
                                appOrdersPacksCount_textwidget.setText("0");

                Adjusting_Widgets_Visibility(false);
            }
            //Log.d(Constants.TAG, "convertingJsonStringintoArray orderlist: " + ordersList);

            //saveorderDetailsInLocal(ordersList);

        } catch (JSONException e) {
            e.printStackTrace();

        }



    }

    private void prepareData(String selectedslot, ArrayList<String> menuItemname_array, HashMap<String, ArrayList<String>> orderid_hashmap, HashMap<String, Modal_ManageOrders_Pojo_Class> menuItemname_hashmap, List<String> tmcSubCtgyName) {
        int totalnoofPacks =0;
        try{
            dataList.clear();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        try {
            Collections.sort(tmcSubCtgyName, new Comparator<String>() {
                public int compare(final String object1, final String object2) {
                    return object1.compareTo(object2);
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try {
            Collections.sort(menuItemname_array, new Comparator<String>() {
                public int compare(final String object1, final String object2) {
                    return object1.compareTo(object2);
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }

        try {


            ArrayList<String> orderid = orderid_hashmap.get(selectedslot);

            appOrdersCount_textwidget.setText(String.valueOf(Objects.requireNonNull(orderid).size()));
        }
        catch(Exception e){
            e.printStackTrace();
        }

        String itemName="",quantity="0",subctgynamefromArray="",tokens="",finalweight="",menuItemKey = "";

        try {
            for (String SubCtgyname : tmcSubCtgyName) {
                int i_value = 0;
                try {
                    Modal_ManageOrders_Pojo_Class modal = SubCtgyName_hashmap.get(SubCtgyname);
                    String totalsubctgywisequantity = Objects.requireNonNull(modal).getTotaltmcsubctgyquantity();
                    try{
                        int noofPacks  = Integer.parseInt(totalsubctgywisequantity);
                        totalnoofPacks = totalnoofPacks + noofPacks;
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    try {
                        for (int i = 0; i < menuItemname_array.size(); i++) {
                            try{
                                itemName = menuItemname_array.get(i);
                                Modal_ManageOrders_Pojo_Class modal_manageOrders_pojo_class = menuItemname_hashmap.get(itemName);
                                itemName = Objects.requireNonNull(modal_manageOrders_pojo_class).getItemName();
                                subctgynamefromArray = modal_manageOrders_pojo_class.getTmcSubCtgyName();
                                finalweight = modal_manageOrders_pojo_class.getItemFinalWeight();
                                try {
                                    if (i_value != 0) {

                                        if (subctgynamefromArray.equals(SubCtgyname)) {
                                            try{
                                                menuItemKey = Objects.requireNonNull(modal_manageOrders_pojo_class).getMenuItemKey();

                                                quantity = modal_manageOrders_pojo_class.getQuantity();
                                                tokens = modal_manageOrders_pojo_class.getTokenno();
                                                int newquantity = 0;
                                                newquantity = Integer.parseInt(Objects.requireNonNull(quantity));

                                                if (newquantity > 1) {
                                                    try {


                                                        ListItem listItem = new ListItem();
                                                        listItem.setMessage(itemName );
                                                      //  listItem.setMessage(itemName + " - ( " + finalweight + " )");
                                                        listItem.setTokens(tokens);
                                                        listItem.setMessageLine2(quantity + " Packs");
                                                        listItem.setMenuitemkey(menuItemKey);
                                                        dataList.add(listItem);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    try {


                                                        ListItem listItem = new ListItem();
                                                        listItem.setMessage(itemName );
                                                     //   listItem.setMessage(itemName + " - ( " + finalweight + " )");
                                                        listItem.setTokens(tokens);
                                                        listItem.setMenuitemkey(menuItemKey);

                                                        listItem.setMessageLine2(quantity + " Pack");
                                                        dataList.add(listItem);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            catch(Exception e){
                                                e.printStackTrace();

                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                if (i_value == 0) {
                                    i_value = 1;
                                    i = i - 1;
                                    ListSection listSection = new ListSection();
                                    if (Integer.parseInt(totalsubctgywisequantity) > 1) {
                                        try {

                                            if (!listSection.getTitle().equals(SubCtgyname)) {
                                                listSection.setTitle(SubCtgyname);
                                                listSection.setTotalAmount(String.valueOf(totalsubctgywisequantity) + " Packs");
                                                dataList.add(listSection);

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            listSection.setTitle(SubCtgyname);
                                            listSection.setTotalAmount(String.valueOf(totalsubctgywisequantity) + " Packs");
                                            dataList.add(listSection);

                                        }
                                    } else {
                                        try {

                                            if (!listSection.getTitle().equals(SubCtgyname)) {
                                                listSection.setTitle(SubCtgyname);
                                                listSection.setTotalAmount(String.valueOf(totalsubctgywisequantity) + " Pack");
                                                dataList.add(listSection);

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            listSection.setTitle(SubCtgyname);
                                            listSection.setTotalAmount(String.valueOf(totalsubctgywisequantity) + " Pack");
                                            dataList.add(listSection);

                                        }
                                    }

                                }


                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        try{
            appOrdersPacksCount_textwidget.setText(String.valueOf(totalnoofPacks));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        setAdapter();

        /*
        if (selectedslot.equals("All")) {

            int orderidSize =0;

            ArrayList<String> allorderidArray = orderid_hashmap.get("All");
            orderidSize = allorderidArray.size();

            appOrdersCount_textwidget.setText(String.valueOf(orderidSize));
            for (String itemName : menuItemname_array) {
                ListSection listSection = new ListSection();

                String quantity = quantity_hashmap.get(itemName);
                try {
                    if (!listSection.getTitle().equals(itemName)) {
                        listSection.setTitle(itemName);
                        listSection.setTotalAmount(String.valueOf(quantity));
                        dataList.add(listSection);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listSection.setTitle(itemName);
                    listSection.setTotalAmount(String.valueOf(quantity));
                    dataList.add(listSection);

                }
                try{
                    ArrayList<String > slottimerange_array_fromHashmap = slottimeRange_hashmap.get(itemName);

                    ArrayList<String > tokenno_array_fromHashmap = tokenNo_hashmap.get(itemName);
                    for (int i =0;i<tokenno_array_fromHashmap.size();i++){
                        String tokenno = tokenno_array_fromHashmap.get(i);
                        String slotramnge = slottimerange_array_fromHashmap.get(i);

                        ListItem listItem = new ListItem();
                        listItem.setMessage(" Token No : "+tokenno);

                        listItem.setMessageLine2(slotramnge);
                        dataList.add(listItem);
                    }
                }
                catch (Exception e){

                }
            }


        }
        setAdapter();




 */

    }


    private void setAdapter() {
        try {

            adapter = new Adapter_SlotWiseAppOrders_List(SlotwiseAppOrderslist.this, dataList,true,menuItemKey_CutWeightdetailsHashmap,menuItemKey_CutWeightdetails);
            posSalesReport_Listview.setAdapter(adapter);
            listviewInstruction_textview.setVisibility(View.GONE);
            posSalesReport_Listview.setVisibility(View.VISIBLE);
            Adjusting_Widgets_Visibility(false);
            spinner_check=0;
        }
        catch(Exception e){
            listviewInstruction_textview.setVisibility(View.GONE);
            posSalesReport_Listview.setVisibility(View.VISIBLE);
            Adjusting_Widgets_Visibility(false);
            spinner_check=0;
            e.printStackTrace();
        }

        try {

            ReportListviewSizeHelper.getListViewSize(posSalesReport_Listview, screenInches);
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }


    private void setDataForFilterSpinners() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlots+"?storeid="+vendorKey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        slotrangeChoosingSpinnerData.add("All");
                        slotrangeChoosingSpinnerData.add(Constants.EXPRESSDELIVERY_SLOTNAME);
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);

                                String slotName = String.valueOf(json.get("slotname"));
                                String slotdateType = String.valueOf(json.get("slotdatetype"));
                                if(slotdateType.equals("TODAY")) {
                                    if ((slotName.equals(Constants.EXPRESSDELIVERY_SLOTNAME))) {
                                        deliveryTimeForExpr_Delivery = String.valueOf(json.get("deliverytime"));

                                        slotrangeChoosingSpinnerData.add(deliveryTimeForExpr_Delivery);


                                    } else {
                                        String slotEndTime = String.valueOf(json.get("slotendtime"));
                                        String slotStartTime = String.valueOf(json.get("slotstarttime"));
                                        String timeRange = slotStartTime + " - " + slotEndTime;
                                        if(!timeRange.equals(" - ")) {
                                            slotrangeChoosingSpinnerData.add(timeRange);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        slotrangeChoosingSpinnerData.add(Constants.STOREPICKUP_DELIVERYTYPE);

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SlotwiseAppOrderslist.this,android.R.layout.simple_spinner_item, slotrangeChoosingSpinnerData);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SlotrangeSelector_spinner.setAdapter(arrayAdapter);
                        selectedTimeRange_spinner = "All";
                        Adjusting_Widgets_Visibility(false);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
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
        RetryPolicy policy = new DefaultRetryPolicy(60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        // Make the request
        Volley.newRequestQueue(SlotwiseAppOrderslist.this).add(jsonObjectRequest);




    }


    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog


        datepicker = new DatePickerDialog(SlotwiseAppOrderslist.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi year: " + year);
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi monthOfYear: " + monthOfYear);
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi dayOfMonth: " + dayOfMonth);



                            String month_in_String = getMonthString(monthOfYear);
                            String monthstring = String.valueOf(monthOfYear+1);
                            String datestring =  String.valueOf(dayOfMonth);
                            if(datestring.length()==1){
                                datestring="0"+datestring;
                            }
                            if(monthstring.length()==1){
                                monthstring="0"+monthstring;
                            }


                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay =   getDayString(dayOfWeek);
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);





                            String CurrentDateString =datestring+monthstring+String.valueOf(year);
                            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay(CurrentDateString);
                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);


                            try{
                                menuItemname_array.clear();
                                orderid_hashmap.clear();
                                menuItemname_hashmap.clear();
                                SubCtgyName_hashmap.clear();
                                tmcSubCtgyName.clear();
                                orderList.clear();
                                dataList.clear();
                                menuItemKey_CutWeightdetailsHashmap.clear();
                                menuItemKey_CutWeightdetails.clear();
                                listviewInstruction_textview.setVisibility(View.VISIBLE);
                                posSalesReport_Listview.setVisibility(View.GONE);
                                Adjusting_Widgets_Visibility(false);
                                  listviewInstruction_textview.setText("There is no orders on this Date");
                              appOrdersCount_textwidget.setText("0");
                                appOrdersPacksCount_textwidget.setText("0");


                                appOrdersCount_textwidget.setText("0");
                                appOrdersPacksCount_textwidget.setText("0");
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }


                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        Calendar c = Calendar.getInstance();
        DatePicker datePicker = datepicker.getDatePicker();

        c.set(2022,8,17);
        // Toast.makeText(getApplicationContext(), Calendar.DATE, Toast.LENGTH_LONG).show();
        Log.d(Constants.TAG, "Calendar.DATE " + String.valueOf(Calendar.DATE));
        long oneMonthAhead = c.getTimeInMillis();
        datePicker.setMinDate(oneMonthAhead);

        datepicker.show();
    }

    private void getTmcSubCtgyList(String vendorKey){
        SubCtgydetails_hashmap.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofSubCtgy,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            //Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    String subCtgyKey  = String.valueOf(json.get("key"));
                                    //Log.d(Constants.TAG, "subCtgyKey: " + subCtgyKey);
                                    String subCtgyName = String.valueOf(json.get("subctgyname"));
                                    //Log.d(Constants.TAG, "subCtgyName: " + subCtgyName);
                                    Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                    modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                    modal_orderDetails.tmcsubctgyname = subCtgyName;
                                    //  tmcSubCtgykey.add(subCtgyKey);
                                    SubCtgydetails_hashmap.put(subCtgyKey,modal_orderDetails);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }
                                Modal_OrderDetails modal_orderDetails = new Modal_OrderDetails();
                                String subCtgyKey  = String.valueOf("Miscellaneous");
                                //Log.d(Constants.TAG, "subCtgyKey: " + subCtgyKey);
                                String subCtgyName = String.valueOf("Miscellaneous Item");

                                modal_orderDetails.tmcsubctgykey = subCtgyKey;
                                modal_orderDetails.tmcsubctgyname = subCtgyName;
                                //  tmcSubCtgykey.add(subCtgyKey);
                                SubCtgydetails_hashmap.put(subCtgyKey,modal_orderDetails);




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        })
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("modulename", "Store");
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);







    }

    /*
        private boolean checkifslottimeisAlreadyAvailabelinArray(String slottime) {
            return slottimeRange_hashmap.containsKey(slottime);
        }




        private boolean checkifTokenNoisAlreadyAvailabelinArray(String itemname) {
            return tokenNo_hashmap.containsKey(itemname);
        }


        private boolean checkifquantityislreadyAvailabelinArray(String key) {
            return quantity_hashmap.containsKey(key);
        }



     */
    private boolean checkifslottimefororderidislreadyAvailabelinArray(String key) {
        return orderid_hashmap.containsKey(key);
    }


    private boolean checkifitmnameinhashmapisAlreadyAvailabelinArray(String slottime) {
        return menuItemname_hashmap.containsKey(slottime);
    }

    private boolean checkifmenuitemkeyCutWeightinhashmapisAlreadyAvailabelinArray(String menuitemkey) {
        return menuItemKey_CutWeightdetailsHashmap.containsKey(menuitemkey);
    }

    void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        } else {
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);

        }

    }

    private String getDayString(int value) {
        if (value == 1) {
            return "Sun";
        }  else if (value == 2) {
            return "Mon";
        } else if (value == 3) {
            return "Tue";
        } else if (value == 4) {
            return "Wed";
        } else if (value == 5) {
            return "Thu";
        } else if (value == 6) {
            return "Fri";
        }
        else if (value == 7) {
            return "Sat";
        }
        return "";
    }



    private String getMonthString(int value) {
        if (value == 0) {
            return "Jan";
        } else if (value == 1) {
            return "Feb";
        } else if (value == 2) {
            return "Mar";
        } else if (value == 3) {
            return "Apr";
        } else if (value == 4) {
            return "May";
        } else if (value == 5) {
            return "Jun";
        } else if (value == 6) {
            return "Jul";
        } else if (value == 7) {
            return "Aug";
        } else if (value == 8) {
            return "Sep";
        } else if (value == 9) {
            return "Oct";
        } else if (value == 10) {
            return "Nov";
        } else if (value == 11) {
            return "Dec";
        }
        return "";
    }


    private String convertOldFormatDateintoNewFormat(String todaysdate) {
/*
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }

 */

        Date date = null;

        SimpleDateFormat formatGMT = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);

        formatGMT.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        try
        {
            date  = formatGMT.parse(todaysdate);
        }
        catch (ParseException e)
        {
            //log(Log.ERROR, "DB Insertion error", e.getMessage().toString());
            //logException(e);
            e.printStackTrace();
        }

        try{

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = day.format(date);


        }
        catch (Exception e){
            e.printStackTrace();
        }


        return CurrentDate;

    }



    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();


        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String PreviousdayDay = previousday.format(c1);




        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String  PreviousdayDate = df1.format(c1);

        System.out.println("todays Date  " + CurrentDate);
        System.out.println("PreviousdayDate Date  " + PreviousdayDate);

        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;

        return yesterdayAsString;
    }



    private String getDatewithNameofthePreviousDayfromSelectedDay(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, -1);




        Date c1 = calendar.getTime();
        SimpleDateFormat previousday = new SimpleDateFormat("EEE",Locale.ENGLISH);
        previousday.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df1.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }


    public String getDate_and_time() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);


        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String CurrentDate = df.format(c);


        return CurrentDate;
    }


    private String getDate() {

        Date c = Calendar.getInstance().getTime();

        if(orderdetailsnewschema) {

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = day.format(c);

            return CurrentDate;

        }
        else{
            SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            String CurrentDay = day.format(c);



            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


            CurrentDate = df.format(c);

            CurrentDate = CurrentDay+", "+CurrentDate;

            //CurrentDate = CurrentDay+", "+CurrentDate;
            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;
        }
    }

}