package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.AssignDeliveryPartner_PojoClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableInterface;
import com.meatchop.tmcpartner.VendorOrder_TrackingDetails.VendorOrdersTableService;

import org.jetbrains.annotations.NotNull;
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
import java.util.Map;
import java.util.Objects;

import static com.meatchop.tmcpartner.Constants.api_GetDeliverySlots;

public class PlotOrdersLocationWithTokenNo extends AppCompatActivity implements OnMapReadyCallback {
    public static List<Modal_ManageOrders_Pojo_Class> ordersList;
    TextView fetchData,appOrdersCount_textwidget,dateSelector_text,mobile_orderinstruction, mobile_nameofFacility_Textview;
    ImageView mobile_search_button, mobile_search_close_btn,applaunchimage;
    EditText mobile_search_barEditText;
    ListView manageOrders_ListView;
    public LinearLayout mapView_Layout;
    public LinearLayout dateSelectorLayout;
    public RelativeLayout listview_Layout;
    public static LinearLayout loadingpanelmask;
    public static LinearLayout loadingPanel;
    public LinearLayout newOrdersSync_Layout;

    DatePickerDialog datepicker;

    public static List<String> array_of_orderId;
    static boolean isSearchButtonClicked = false,isnewOrdersSyncButtonClicked=false;
    private  String OrderDetailsResultjsonString,CurrentDate,CurrentDay,TodaysDate,DateString,PreviousDateString,vendorKey,vendorname;
    private double screenInches;
    public static List<Modal_ManageOrders_Pojo_Class> sorted_OrdersList;
    public static List<String> selectedOrders;
    GoogleMap map ;
    Marker marker,marker1=null;
    LatLng CustomerLatlng;
    double vendorLatitude,vendorLongitude;
    private  String DeliveryPersonList;
    private  List<AssignDeliveryPartner_PojoClass> deliveryPartnerList;
    Spinner SlotrangeSelector_spinner;
    List<String> slotrangeChoosingSpinnerData;
    String selectedTimeRange_spinner = "All";
    int spinner_check = 0;

    List<String> deliverydistanceChoosingSpinnerData;
    String selected_DeliveryDistanceRange_spinner = "All";
    int deliverydistancespinner_check = 0;
    public Button drawMap_Layout;
    public LinearLayout finalButtons_filter_Layout;

    Button selectAll_button,unSelectAll_button;
    boolean isFilterChanged = false;
    int slottypefromSpinner=0;
    Spinner deliverDistance_Spinner;
    AdapterPlotOrdersLocation adapterPlotOrdersLocation;
    String deliveryTimeForExpr_Delivery;


    VendorOrdersTableInterface mResultCallback = null;
    VendorOrdersTableService mVolleyService;
    boolean orderdetailsnewschema = false;
    boolean  isVendorOrdersTableServiceCalled = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_orders_location_with_token_no);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        Objects.requireNonNull(mapFragment).getMapAsync(PlotOrdersLocationWithTokenNo.this);

        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = (shared.getString("VendorKey", ""));
            vendorname = (shared.getString("VendorName", ""));
            vendorLatitude  = Double.parseDouble(shared.getString("VendorLatitude", ""));
            vendorLongitude  = Double.parseDouble(shared.getString("VendorLongitute", ""));
            orderdetailsnewschema = (shared.getBoolean("orderdetailsnewschema_settings", false));
           // orderdetailsnewschema = true;



            try {
                ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
                screenInches = screenSizeOfTheDevice.getDisplaySize(PlotOrdersLocationWithTokenNo .this);
               //Toast.makeText(this, "ScreenSizeOfTheDevice : "+String.valueOf(screenInches), Toast.LENGTH_SHORT).show();
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

        }
        catch (Exception e){
            e.printStackTrace();
        }


        try{

            SharedPreferences shared2 = getSharedPreferences("DeliveryPersonList", MODE_PRIVATE);
            DeliveryPersonList = (shared2.getString("DeliveryPersonListString", ""));

            ConvertStringintoDeliveryPartnerListArray(DeliveryPersonList);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        SlotrangeSelector_spinner =  findViewById(R.id.SlotrangeSelector_spinner);
        slotrangeChoosingSpinnerData = new ArrayList<>();

        deliverDistance_Spinner =  findViewById(R.id.deliveerydistanse_Spinner);
        deliverydistanceChoosingSpinnerData = new ArrayList<>();

        selectedOrders = new ArrayList<>();
        sorted_OrdersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();
        deliveryPartnerList = new ArrayList<>();
        array_of_orderId = new ArrayList<>();
        appOrdersCount_textwidget = findViewById(R.id.appOrdersCount_textwidget);
        fetchData =  findViewById(R.id.fetchData);
        finalButtons_filter_Layout = findViewById(R.id.finalButtons_filter_Layout);

        manageOrders_ListView = findViewById(R.id.manageOrders_ListView);
        mobile_orderinstruction = findViewById(R.id.orderinstruction);
        dateSelector_text = findViewById(R.id.dateSelector_text);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        drawMap_Layout = findViewById(R.id.drawMap_Layout);
        //
        mobile_nameofFacility_Textview = findViewById(R.id.nameofFacility_Textview);
        mobile_search_button = findViewById(R.id.search_button);
        mobile_search_barEditText = findViewById(R.id.search_barEdit);
        mobile_search_close_btn = findViewById(R.id.search_close_btn);
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);
        mapView_Layout  = findViewById(R.id.mapView_Layout);
        listview_Layout =  findViewById(R.id.listview_Layout);
        selectAll_button  = findViewById(R.id.selectAll_button);
        unSelectAll_button  = findViewById(R.id.unSelectAll_button);

        loadingpanelmask = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel = findViewById(R.id.loadingPanel_dailyItemWisereport);

        try{



            mapView_Layout.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.GONE);
            mobile_orderinstruction.setVisibility(View.VISIBLE);
            mobile_orderinstruction.setText("Select Date to Get Orders List");
            mobile_nameofFacility_Textview.setText(vendorname);

            dateSelector_text.setText(Constants.Empty_Date_Format);
            DateString = (Constants.Empty_Date_Format);

            setDataForFilterSpinners();

            sorted_OrdersList.clear();
            selectedTimeRange_spinner = "All";
            selected_DeliveryDistanceRange_spinner = "All";
            finalButtons_filter_Layout.setVisibility(View.GONE);
            isSearchButtonClicked = false;
            ordersList.clear();
            array_of_orderId.clear();
            selectedOrders.clear();
            sorted_OrdersList.clear();
            /*TodaysDate = getDatewithNameoftheDay();
            PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(TodaysDate);

            if(orderdetailsnewschema){

                String dateAsOldFormat =convertnewFormatDateintoOldFormat(TodaysDate);
                dateSelector_text.setText(dateAsOldFormat);
                callVendorOrderDetailsSeviceAndInitCallBack(TodaysDate,TodaysDate,vendorKey);


            }
            else{
                dateSelector_text.setText(TodaysDate);

                getOrderDetailsUsingOrderSlotDate(PreviousDateString,TodaysDate, vendorKey);

            }

             */


        }
        catch (Exception e){
            e.printStackTrace();
        }


        unSelectAll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(sorted_OrdersList.size()>0){
                        showProgressBar(true);

                        for(int i=0;i<sorted_OrdersList.size();i++){
                            sorted_OrdersList.get(i).setIsOrdersChecked("false");
                            selectedOrders.clear();

                            adapterPlotOrdersLocation.notifyDataSetChanged();
                        }
                        showProgressBar(false);

                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        selectAll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                   if(sorted_OrdersList.size()>0){
                       showProgressBar(true);
                       selectedOrders.clear();
                       for(int i=0;i<sorted_OrdersList.size();i++){
                           sorted_OrdersList.get(i).setIsOrdersChecked("true");
                           selectedOrders.add(sorted_OrdersList.get(i).getTokenno());
                           adapterPlotOrdersLocation.notifyDataSetChanged();

                       }
                       showProgressBar(false);
                   }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

/*
        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ordersList.clear();
                sorted_OrdersList.clear();
                array_of_orderId.clear();
                selectedOrders.clear();
                sorted_OrdersList.clear();
                showProgressBar(true);
                String  todatestring=dateSelector_text.getText().toString();

                PreviousDateString = getDatewithNameofthePreviousDayfromSelectedDay2(todatestring);

                isSearchButtonClicked = false;

                if(orderdetailsnewschema){
                    String newformat = convertOldFormatDateintoNewFormat(todatestring);


                    callVendorOrderDetailsSeviceAndInitCallBack(newformat,newformat,vendorKey);
                }
                else{

                    getOrderDetailsUsingOrderSlotDate(PreviousDateString,todatestring, vendorKey);

                }





            }
        });

 */

        SlotrangeSelector_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(spinner_check>1) {
                    selectedTimeRange_spinner = SlotrangeSelector_spinner.getSelectedItem().toString();
                    selectedOrders.clear();
                    displayorderDetailsinListview( ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);
                    isFilterChanged = true;
                }
                spinner_check=2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        deliverDistance_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if(deliverydistancespinner_check>1) {
                    selected_DeliveryDistanceRange_spinner = deliverDistance_Spinner.getSelectedItem().toString();
                    selectedOrders.clear();
                    displayorderDetailsinListview( ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);
                    isFilterChanged = true;
                }
                deliverydistancespinner_check=2;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        drawMap_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedOrders.size()>0) {

                    map.clear();
                    double Latitude=0, Longitude=0;

                    for (String tokenno : selectedOrders) {
                        for (int i = 0; i < ordersList.size(); i++) {
                            String tokennofromArray = ordersList.get(i).getTokenno();
                            MarkerOptions CustomersMarker = new MarkerOptions();

                            if (tokenno.equals(tokennofromArray)) {
                                try {
                                    Latitude = Double.parseDouble(ordersList.get(i).getUseraddresslat());

                                } catch (Exception r) {
                                    r.printStackTrace();
                                }
                                try {
                                    Longitude = Double.parseDouble(ordersList.get(i).getUseraddresslon());

                                } catch (Exception r) {
                                    r.printStackTrace();
                                }
                                CustomerLatlng = new LatLng(Latitude, Longitude);
                                CustomersMarker.title(tokenno);
                                IconGenerator icg = new IconGenerator(PlotOrdersLocationWithTokenNo.this);
                                icg.setColor(getResources().getColor(R.color.TMC_Black)); // green background
                                icg.setTextAppearance(R.style.WhiteText);
                                Bitmap bm = icg.makeIcon(tokenno);

                                CustomersMarker.position(CustomerLatlng);


                                CustomersMarker.icon(BitmapDescriptorFactory.fromBitmap(bm));
                                map.addMarker(CustomersMarker);

                                listview_Layout.setVisibility(View.GONE);
                                mapView_Layout.setVisibility(View.VISIBLE);
                            }

                        }

                    }
                    LatLng    vendorrLatlng = new LatLng(vendorLatitude, vendorLongitude);

                    IconGenerator icg = new IconGenerator(PlotOrdersLocationWithTokenNo.this);
                    icg.setColor(getResources().getColor(R.color.TMC_Orange)); // green background
                    icg.setTextAppearance(R.style.WhiteText);
                    Bitmap bm = icg.makeIcon("STORE");

                    MarkerOptions vendormarker = new MarkerOptions();
                    vendormarker.position(vendorrLatlng);

                    map.moveCamera(CameraUpdateFactory.zoomTo(10));

                    vendormarker.title(vendorname);
                    vendormarker.icon(BitmapDescriptorFactory.fromBitmap(bm));
                    map.addMarker(vendormarker);

                    map.moveCamera(CameraUpdateFactory.newLatLng(vendorrLatlng));

                }
                else{
                    Toast.makeText(PlotOrdersLocationWithTokenNo.this, "No Orders has been selected", Toast.LENGTH_LONG).show();

                }
            }
        });


        mobile_search_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked = false;
                selectedOrders.clear();

                displayorderDetailsinListview( ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);
            }
        });
        mobile_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int textlength = mobile_search_barEditText.getText().toString().length();
                isSearchButtonClicked = true;
                selectedOrders.clear();

                showKeyboard(mobile_search_barEditText);
                showSearchBarEditText();
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


        fetchData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (DateString.equals(Constants.Empty_Date_Format)) {
                    Toast.makeText(PlotOrdersLocationWithTokenNo.this, "Select the Date First !!! ", Toast.LENGTH_SHORT).show();
                } else {
                     showProgressBar(true);

                     if (orderdetailsnewschema) {
                         String NewFormat = convertOldFormatDateintoNewFormat(DateString);

                         callVendorOrderDetailsSeviceAndInitCallBack(NewFormat, NewFormat, vendorKey);


                     } else {


                         //  Adjusting_Widgets_Visibility(true);
                         PreviousDateString = getDatewithNameofthePreviousDay2(DateString);

                         getOrderDetailsUsingOrderSlotDate(PreviousDateString, DateString, vendorKey);

                     }
                 }
            }
        });

        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlotOrdersLocationWithTokenNo.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
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
                                modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();

                                modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                                modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                                modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                                modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                                modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                            //    deliverydistancefromarray = Double.parseDouble( modal_manageOrders_forOrderDetailList.getDeliverydistance());


                                modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                                modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                                modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                                modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                                try {
                                    modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                                } catch (Exception e) {
                                    modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                                }

                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);


                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }
                    try {
                        SetAdapterForListView(sorted_OrdersList);

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





    }

    private void displayorderDetailsinListview( @NotNull List<Modal_ManageOrders_Pojo_Class> ordersList, int slottypefromSpinner, String selectedTimeRange_spinner, String selected_DeliveryDistanceRange_spinner) {
        showProgressBar(true);


        sorted_OrdersList.clear();

        double selectedDeliveryDistance,deliverydistancefromarray,minimumdeliverydistance;
        if(selected_DeliveryDistanceRange_spinner.equals("All")){
            selectedDeliveryDistance = 0;
            minimumdeliverydistance = 0;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals(" Less than 3 Kms")){
            selectedDeliveryDistance = 2.9;
            minimumdeliverydistance = 0;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals(" 3 to 5 Kms ")){
            selectedDeliveryDistance = 4.9;
            minimumdeliverydistance = 3;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals(" 5 to 10 Kms ")){
            selectedDeliveryDistance = 9.9;
            minimumdeliverydistance = 5;
        }
        else if(selected_DeliveryDistanceRange_spinner.equals("More than 10 Kms")){
            selectedDeliveryDistance = 10;
            minimumdeliverydistance = 10;
        }
        else{
            selectedDeliveryDistance = 0;
            minimumdeliverydistance = 0;
            Toast.makeText(PlotOrdersLocationWithTokenNo.this, "Delivery Distance Filter is not applied", Toast.LENGTH_LONG).show();

        }

        for (int i = 0; i < ordersList.size(); i++) {
            try {
                //Log.d(Constants.TAG, "displayorderDetailsinListview ordersList: " + ordersList.get(i));

                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList1 = new Modal_ManageOrders_Pojo_Class();
                final Modal_ManageOrders_Pojo_Class modal_manageOrders_forOrderDetailList = ordersList.get(i);
                String slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange().toUpperCase();
                String slotname = String.valueOf(modal_manageOrders_forOrderDetailList.getSlotname()).toUpperCase();
                String deliveryType = String.valueOf(modal_manageOrders_forOrderDetailList.getDeliverytype()).toUpperCase();
                String ordertype = String.valueOf(modal_manageOrders_forOrderDetailList.getOrderType()).toUpperCase();
                Log.i(" slotname n   " ,slotname);
                Log.i(" slottimerange n " , slottimerange);

                if(((selectedTimeRange_spinner.equals(Constants.EXPRESS_DELIVERY_SLOTNAME))||(selectedTimeRange_spinner.equals(Constants.EXPRESSDELIVERY_SLOTNAME)))&&(slottimerange.equals(deliveryTimeForExpr_Delivery.toUpperCase()))) {
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
                    modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();

                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                    modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                    modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                    deliverydistancefromarray = Double.parseDouble( modal_manageOrders_forOrderDetailList.getDeliverydistance());

                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    try {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                    } catch (Exception e) {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                    }

                    if (selected_DeliveryDistanceRange_spinner.equals("All")) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }
                    else{
                        if(selectedDeliveryDistance==10){
                            if(selectedDeliveryDistance<deliverydistancefromarray){
                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                            }
                        }
                        else{
                            if((selectedDeliveryDistance>=deliverydistancefromarray) && ( minimumdeliverydistance <= deliverydistancefromarray)){
                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                            }
                        }
                    }
                }




                if(((selectedTimeRange_spinner.equals(Constants.STOREPICKUP_DELIVERYTYPE)))&&(deliveryType.equals(Constants.STOREPICKUP_DELIVERYTYPE))&&(ordertype.equals(Constants.APPORDER))) {
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
                    modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();

                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                    modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                    modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                    deliverydistancefromarray = Double.parseDouble( modal_manageOrders_forOrderDetailList.getDeliverydistance());

                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    try {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                    } catch (Exception e) {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                    }

                    if (selected_DeliveryDistanceRange_spinner.equals("All")) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }
                    else{
                        if(selectedDeliveryDistance==10){
                            if(selectedDeliveryDistance<deliverydistancefromarray){
                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                            }
                        }
                        else{
                            if((selectedDeliveryDistance>=deliverydistancefromarray) && ( minimumdeliverydistance <= deliverydistancefromarray)){
                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                            }
                        }
                    }
                }



                if(selectedTimeRange_spinner.equals(slottimerange)||selectedTimeRange_spinner.equals("All")) {
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
                    modal_manageOrders_forOrderDetailList1.notes = modal_manageOrders_forOrderDetailList.getNotes();

                    modal_manageOrders_forOrderDetailList1.orderdetailskey = modal_manageOrders_forOrderDetailList.getOrderdetailskey();
                    modal_manageOrders_forOrderDetailList1.slotdate = modal_manageOrders_forOrderDetailList.getSlotdate();
                    modal_manageOrders_forOrderDetailList1.slotname = modal_manageOrders_forOrderDetailList.getSlotname();
                    modal_manageOrders_forOrderDetailList1.slottimerange = modal_manageOrders_forOrderDetailList.getSlottimerange();
                    modal_manageOrders_forOrderDetailList1.deliverydistance = modal_manageOrders_forOrderDetailList.getDeliverydistance();
                    deliverydistancefromarray = Double.parseDouble( modal_manageOrders_forOrderDetailList.getDeliverydistance());


                    modal_manageOrders_forOrderDetailList1.orderconfirmedtime = modal_manageOrders_forOrderDetailList.getOrderconfirmedtime();
                    modal_manageOrders_forOrderDetailList1.orderreadytime = modal_manageOrders_forOrderDetailList.getOrderreadytime();
                    modal_manageOrders_forOrderDetailList1.orderpickeduptime = modal_manageOrders_forOrderDetailList.getOrderpickeduptime();
                    modal_manageOrders_forOrderDetailList1.orderdeliveredtime = modal_manageOrders_forOrderDetailList.getOrderdeliveredtime();
                    try {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = Integer.parseInt(modal_manageOrders_forOrderDetailList.getTokenno());
                    } catch (Exception e) {
                        modal_manageOrders_forOrderDetailList1.intTokenNo = 0;

                    }

                    if (selected_DeliveryDistanceRange_spinner.equals("All")) {

                        sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);
                    }
                    else{
                        if(selectedDeliveryDistance==10){
                            if(selectedDeliveryDistance<deliverydistancefromarray){
                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                            }
                        }
                        else{
                            if((selectedDeliveryDistance>=deliverydistancefromarray) && ( minimumdeliverydistance <= deliverydistancefromarray)){
                                sorted_OrdersList.add(modal_manageOrders_forOrderDetailList1);

                            }

                        }
                    }
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }


        }

        try {
            if (sorted_OrdersList.size() > 0) {
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


                finalButtons_filter_Layout.setVisibility(View.VISIBLE);

                appOrdersCount_textwidget.setText(String.valueOf(sorted_OrdersList.size()));


                SetAdapterForListView(sorted_OrdersList);




            }
            else{
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                mobile_orderinstruction.setText("There is No data for this Slot");
                mobile_orderinstruction.setVisibility(View.VISIBLE);
                appOrdersCount_textwidget.setText(String.valueOf(sorted_OrdersList.size()));
                finalButtons_filter_Layout.setVisibility(View.GONE);

            }
        }
        catch (Exception e){
            e.printStackTrace();
            if (sorted_OrdersList.size() > 0) {
                SetAdapterForListView(sorted_OrdersList);
                finalButtons_filter_Layout.setVisibility(View.VISIBLE);

            } else {
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                mobile_orderinstruction.setText("No Order today");
                finalButtons_filter_Layout.setVisibility(View.GONE);

                mobile_orderinstruction.setVisibility(View.VISIBLE);

            }
        }

//callAdapter();
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

    private void callVendorOrderDetailsSeviceAndInitCallBack(String FromDate, String ToDate, String vendorKey) {
        showProgressBar(true);

        if(isVendorOrdersTableServiceCalled){
            showProgressBar(false);
            return;
        }
        isVendorOrdersTableServiceCalled = true;
        mResultCallback = new VendorOrdersTableInterface() {
            @Override
            public void notifySuccess(String requestType, List<Modal_ManageOrders_Pojo_Class> orderslist_fromResponse) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + orderslist_fromResponse);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("content",orderslist_fromResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isVendorOrdersTableServiceCalled = false;
                ordersList =  orderslist_fromResponse;

                mobile_orderinstruction.setVisibility(View.GONE);
                // addDateinDatesArray(FromDate,ToDate);
                displayorderDetailsinListview( ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);

            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                Log.d("TAG", "Volley requester " + requestType);
                Log.d("TAG", "Volley JSON post" + "That didn't work!");
                showProgressBar(false);
                isVendorOrdersTableServiceCalled = false;

            }
        };
        ordersList.clear();
        sorted_OrdersList.clear();
        array_of_orderId.clear();

        showProgressBar(true);
        mVolleyService = new VendorOrdersTableService(mResultCallback,PlotOrdersLocationWithTokenNo.this);
        String orderDetailsURL = Constants.api_GetVendorOrderDetailsUsingslotDate_vendorkey_type + "?slotdate="+FromDate+"&vendorkey="+vendorKey+"&ordertype=APPORDER";
        String orderTrackingDetailsURL = Constants.api_GetVendorTrackingDetailsUsingslotDate_vendorkey + "?slotdate="+FromDate+"&vendorkey="+vendorKey;

        mVolleyService.getVendorOrderDetails(orderDetailsURL,orderTrackingDetailsURL);

    }



    private void getOrderDetailsUsingOrderSlotDate(String previousDaydate, String SlotDate, String vendorKey) {
        showProgressBar(true);

        if(isnewOrdersSyncButtonClicked){
            showProgressBar(false);
            return;

        }

        isnewOrdersSyncButtonClicked=true;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Called: " );

        ordersList.clear();
        sorted_OrdersList.clear();



        SharedPreferences sharedPreferences
                = getSharedPreferences("OrderDetailsFromSharedPreferences",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTrackingOrderDetails_AppOrders + "?slotdate="+SlotDate+"&vendorkey="+vendorKey+"&previousdaydate="+previousDaydate,null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                        OrderDetailsResultjsonString =response.toString();
                        convertingJsonStringintoArray( OrderDetailsResultjsonString);

                        // adapter = new Adapter_AutoCompleteManageOrdersItem(PlotOrdersLocationWithTokenNo.this, mobile_jsonString);

                        //    mobile_search_barEditText.setAdapter(adapter);

                    }

                },new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Toast.makeText(PlotOrdersLocationWithTokenNo.this,"There is no Orders Yet ",Toast.LENGTH_LONG).show();
                showProgressBar(false);
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                isnewOrdersSyncButtonClicked=false;
                SetAdapterForListView(ordersList);

                error.printStackTrace();
            }
        })
        {
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
        Volley.newRequestQueue(PlotOrdersLocationWithTokenNo.this).add(jsonObjectRequest);




    }



    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(PlotOrdersLocationWithTokenNo.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            ordersList.clear();
                            sorted_OrdersList.clear();
                            array_of_orderId.clear();
                            selectedOrders.clear();
                            sorted_OrdersList.clear();
                            displayorderDetailsinListview( ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);

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
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();

    }



    private void convertingJsonStringintoArray(String orderDetailsResultjsonString) {
        try {
            String ordertype="#",orderid="";
            sorted_OrdersList.clear();
            showProgressBar(true);
            //converting jsonSTRING into array
            JSONObject jsonObject = new JSONObject(orderDetailsResultjsonString);
            JSONArray JArray  = jsonObject.getJSONArray("content");
            //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
            int i1=0;
            int arrayLength = JArray.length();
            //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


            for(;i1<(arrayLength);i1++) {

                try {
                    JSONObject json = JArray.getJSONObject(i1);
                    Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                    finalButtons_filter_Layout.setVisibility(View.VISIBLE);

                    //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                    if(json.has("ordertype")){
                        manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));
                        ordertype = String.valueOf(json.get("ordertype"));
                    }
                    else{
                        ordertype="#";
                        manageOrdersPojoClass.orderType ="";
                    }
                    if(json.has("orderid")){
                        manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));
                        orderid = String.valueOf(json.get("orderid"));
                    }
                    else{
                        manageOrdersPojoClass.orderid ="";
                    }

                    if (!array_of_orderId.contains(orderid)) {
                        array_of_orderId.add(orderid);
                    }


                    if(json.has("notes")){
                        manageOrdersPojoClass.notes = String.valueOf(json.get("notes"));

                    }
                    else{
                        manageOrdersPojoClass.notes ="";
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
                            //Log.i(Constants.TAG, "Can't Get itemDesp");
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
                    if(json.has("deliverytype")){
                        manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));

                    }
                    else{
                        manageOrdersPojoClass.deliverytype ="";
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








                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                    //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                    Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                }


            }

            displayorderDetailsinListview( ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);


        } catch (JSONException e) {
            e.printStackTrace();
            showProgressBar(false);
            finalButtons_filter_Layout.setVisibility(View.GONE);

            isnewOrdersSyncButtonClicked=false;
        }
    }




    private void SetAdapterForListView(List<Modal_ManageOrders_Pojo_Class> ordersList) {
        try {
            if (ordersList.size() > 0) {
                Collections.sort(ordersList, new Comparator<Modal_ManageOrders_Pojo_Class>() {
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



                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));

                isnewOrdersSyncButtonClicked=false;


                 adapterPlotOrdersLocation = new AdapterPlotOrdersLocation(PlotOrdersLocationWithTokenNo.this, ordersList, PlotOrdersLocationWithTokenNo.this,isSearchButtonClicked);
                manageOrders_ListView.setAdapter(adapterPlotOrdersLocation);


                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.VISIBLE);
                mobile_orderinstruction.setVisibility(View.GONE);


            }
            else{
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                mobile_orderinstruction.setText("There is No Order ");
                mobile_orderinstruction.setVisibility(View.VISIBLE);
                appOrdersCount_textwidget.setText(String.valueOf(ordersList.size()));
                isnewOrdersSyncButtonClicked=false;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            if (ordersList.size() > 0) {

                isnewOrdersSyncButtonClicked=false;
                AdapterPlotOrdersLocation adapterPlotOrdersLocation = new AdapterPlotOrdersLocation(PlotOrdersLocationWithTokenNo.this, ordersList, PlotOrdersLocationWithTokenNo.this,isSearchButtonClicked);
                manageOrders_ListView.setAdapter(adapterPlotOrdersLocation);



                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.VISIBLE);
                mobile_orderinstruction.setVisibility(View.GONE);


            } else {
                loadingpanelmask.setVisibility(View.GONE);
                loadingPanel.setVisibility(View.GONE);
                manageOrders_ListView.setVisibility(View.GONE);
                mobile_orderinstruction.setText("There is No Order ");
                isnewOrdersSyncButtonClicked=false;
                mobile_orderinstruction.setVisibility(View.VISIBLE);

            }
        }

    }


    private String getDatewithNameoftheDay() {
        Date c = Calendar.getInstance().getTime();
        if(orderdetailsnewschema) {

            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = day.format(c);

            return CurrentDate;

        }
        else {


            SimpleDateFormat day = new SimpleDateFormat("EEE");
            CurrentDay = day.format(c);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(c);

            CurrentDate = CurrentDay + ", " + CurrentDate;

            //CurrentDate = CurrentDay+", "+CurrentDate;
            System.out.println("todays Date  " + CurrentDate);


            return CurrentDate;
        }
    }

    private String getDatewithNameofthePreviousDay2(String sDate) {



        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = null;
        try {
            date = dateFormat.parse(sDate);
        } catch (ParseException e2) {
            e2.printStackTrace();
        }
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi sDate: " + sDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi date: " + date);

        calendar.add(Calendar.DATE, -1);




        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;



    }

    private String convertOldFormatDateintoNewFormat(String todaysdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
            CurrentDate = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }
    private String convertnewFormatDateintoOldFormat(String todaysdate) {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("EEE");
            String CurrentDay = day.format(date);


            SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
            CurrentDate = df.format(date);

            CurrentDate = CurrentDay + ", " + CurrentDate;



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

    }

    private String getDatewithNameofthePreviousDayfromSelectedDay2(String sDate) {

        if(orderdetailsnewschema) {


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
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



            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
            String yesterdayAsString = df1.format(c1);
            //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

            return yesterdayAsString;


        }
        else {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
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

            SimpleDateFormat previousday = new SimpleDateFormat("EEE");
            String PreviousdayDay = previousday.format(c1);


            SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
            String PreviousdayDate = df1.format(c1);
            String yesterdayAsString = PreviousdayDay + ", " + PreviousdayDate;
            //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

            return yesterdayAsString;
        }
    }


    private String getDatewithNameofthePreviousDayfromSelectedDay(String sDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
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

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);



        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);
        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;
        //Log.d(Constants.TAG, "getOrderDetailsUsingApi yesterdayAsString: " + PreviousdayDate);

        return yesterdayAsString;
    }

    void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
            manageOrders_ListView.setVisibility(View.GONE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
            manageOrders_ListView.setVisibility(View.VISIBLE);
        }

    }




    private void setDataForFilterSpinners() {
        showProgressBar(true);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, api_GetDeliverySlots+"?storeid="+vendorKey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {

                    JSONArray content = (JSONArray) response.get("content");
                    JSONArray jArray = (JSONArray) content;
                    if (jArray != null) {
                        slotrangeChoosingSpinnerData.add("All");
                        slotrangeChoosingSpinnerData.add(Constants.EXPRESS_DELIVERY_SLOTNAME);
                        for (int i = 0; i < jArray.length(); i++) {
                            try {
                                JSONObject json = content.getJSONObject(i);

                                String slotName = String.valueOf(json.get("slotname")).toUpperCase();
                                String slotdateType = String.valueOf(json.get("slotdatetype"));
                                if(slotdateType.equals("TODAY")) {
                                    if ((slotName.equals(Constants.EXPRESS_DELIVERY_SLOTNAME))||(slotName.equals(Constants.EXPRESSDELIVERY_SLOTNAME))) {
                                         deliveryTimeForExpr_Delivery = String.valueOf(json.get("deliverytime")).toUpperCase();

                                    //    slotrangeChoosingSpinnerData.add(deliveryTimeForExpr_Delivery);


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

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PlotOrdersLocationWithTokenNo.this,android.R.layout.simple_spinner_item, slotrangeChoosingSpinnerData);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        SlotrangeSelector_spinner.setAdapter(arrayAdapter);
                        selectedTimeRange_spinner = "All";
                        showProgressBar(false);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    showProgressBar(false);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                showProgressBar(false);

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
        Volley.newRequestQueue(PlotOrdersLocationWithTokenNo.this).add(jsonObjectRequest);








        //seting data for DeliveryDistansce Spinner
        deliverydistanceChoosingSpinnerData.add("All");
        deliverydistanceChoosingSpinnerData.add(" Less than 3 Kms");
        deliverydistanceChoosingSpinnerData.add(" 3 to 5 Kms ");
        deliverydistanceChoosingSpinnerData.add(" 5 to 10 Kms ");
        deliverydistanceChoosingSpinnerData.add("More than 10 Kms");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(PlotOrdersLocationWithTokenNo.this,android.R.layout.simple_spinner_item, deliverydistanceChoosingSpinnerData);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliverDistance_Spinner.setAdapter(arrayAdapter);
        selected_DeliveryDistanceRange_spinner = "All";



    }






    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void closeSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.VISIBLE);
        mobile_search_button.setVisibility(View.VISIBLE);
        mobile_search_close_btn.setVisibility(View.GONE);
        mobile_search_barEditText.setVisibility(View.GONE);
        newOrdersSync_Layout.setVisibility(View.VISIBLE);
    }

    private void showSearchBarEditText() {
        dateSelectorLayout.setVisibility(View.GONE);
        mobile_search_button.setVisibility(View.GONE);
        mobile_search_close_btn.setVisibility(View.VISIBLE);
        mobile_search_barEditText.setVisibility(View.VISIBLE);
        newOrdersSync_Layout.setVisibility(View.GONE);
    }
    private void showKeyboard(final EditText editText) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                editText.setSelection(editText.getText().length());
            }
        },0);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.moveCamera(CameraUpdateFactory.zoomTo(10));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
         map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);

    }


    @Override
    public void onBackPressed() {
        int i = mapView_Layout.getVisibility();
        if(i==0){
            mapView_Layout.setVisibility(View.GONE);
            listview_Layout.setVisibility(View.VISIBLE);
        }
        else if(i==8) {
            if(isSearchButtonClicked){
                hideKeyboard(mobile_search_barEditText);
                closeSearchBarEditText();
                mobile_search_barEditText.setText("");
                isSearchButtonClicked = false;
                selectedOrders.clear();

                displayorderDetailsinListview( ordersList, slottypefromSpinner, selectedTimeRange_spinner,selected_DeliveryDistanceRange_spinner);
            }
            else {
                super.onBackPressed();
            }
        }

    }
}
    
    
    
    
