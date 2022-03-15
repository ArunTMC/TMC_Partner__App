package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;

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

public class MenuAvailabilityStatusTransaction extends AppCompatActivity {
    LinearLayout loadingPanel,loadingpanelmask;
    Spinner subCtgyItem_spinner;
    ArrayAdapter adapter_subCtgy_spinner;
    String vendorkey,deliverySlotKey ;
    ListView MenuItemsListView;
    List<Modal_MenuItem_Settings> MenuItem = new ArrayList<>();
    TextView dateSelector_text,listviewInstruction,itemName_text_widget;
    double screenInches;
    String CurrentDate, PreviousDateString;
    String DateString;
    DatePickerDialog datepicker;
    String menuItemKey,menuTransactionJsonString,subctgykey,itemName;

    LinearLayout dateSelectorLayout,newOrdersSync_Layout;
    public static List<Modal_MenuItem_Settings> displaying_menuItems;
    //public static List<Modal_MenuItem_Settings> completemenuItem;
    public static List<Modal_MenuAvailabilityStatusTransaction> menuTransaction_array;
    JSONArray result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_availability_status_transaction);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        newOrdersSync_Layout = findViewById(R.id.newOrdersSync_Layout);

        dateSelector_text = findViewById(R.id.dateSelector_text);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        subCtgyItem_spinner = findViewById(R.id.subCtgyItem);
        MenuItemsListView = findViewById(R.id.MenuItemsListView);
        dateSelectorLayout= findViewById(R.id.dateSelectorLayout);
        itemName_text_widget = findViewById(R.id.itemName_text_widget);
        listviewInstruction = findViewById(R.id.listviewInstruction);
        Adjusting_Widgets_Visibility(false);

        displaying_menuItems = new ArrayList<>();
        menuTransaction_array = new ArrayList<>();

        try{
            menuItemKey =  getIntent().getStringExtra("menuItemKey");
        }
        catch (Exception e){
            e.printStackTrace();
            menuItemKey="";
            Toast.makeText(MenuAvailabilityStatusTransaction.this, "menu key is empty", Toast.LENGTH_SHORT).show();

        }

        try{
            subctgykey =  getIntent().getStringExtra("subctgykey");
        }
        catch (Exception e){
            e.printStackTrace();
            subctgykey="";
            Toast.makeText(MenuAvailabilityStatusTransaction.this, "subctgy key is empty", Toast.LENGTH_SHORT).show();

        }

        try{
            itemName =  getIntent().getStringExtra("itemName");
        }
        catch (Exception e){
            e.printStackTrace();
            itemName="";
            Toast.makeText(MenuAvailabilityStatusTransaction.this, "itemName key is empty", Toast.LENGTH_SHORT).show();

        }

        try {
            CurrentDate = getDate();
            DateString = getDate();
        }
        catch (Exception e ){
            e.printStackTrace();
        }

        try {

            SharedPreferences shared = getApplicationContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorkey = (shared.getString("VendorKey", ""));

        }
        catch (Exception e ){
            e.printStackTrace();
        }

        try {

            dateSelector_text.setText(CurrentDate);
        }
        catch (Exception e ){
            e.printStackTrace();
        }


        try {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
            screenInches = Math.sqrt(x + y);

        }
        catch (Exception e ){
            e.printStackTrace();
        }
        try{

            itemName_text_widget.setText(itemName);
        }
        catch (Exception e){
            e.printStackTrace();

        }
        try {
            getTransactionForSelectedDate(menuItemKey,DateString, vendorkey,subctgykey);
        }
        catch (Exception e ){
            e.printStackTrace();
        }




        newOrdersSync_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Adjusting_Widgets_Visibility(true);

                displaying_menuItems.clear();
                menuTransaction_array.clear();
                MenuItemsListView.setVisibility(View.GONE);
                listviewInstruction.setVisibility(View.VISIBLE);
                DateString =  dateSelector_text.getText().toString();

                getTransactionForSelectedDate(menuItemKey,DateString, vendorkey,subctgykey);

            }
        });




        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuAvailabilityStatusTransaction.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
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







    private void openDatePicker() {


        final Calendar cldr = Calendar.getInstance();

        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog


        datepicker = new DatePickerDialog(MenuAvailabilityStatusTransaction.this,
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
                            dateSelector_text.setText(CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);
                            //getOrderForSelectedDate(DateString, vendorKey);
                            DateString = (CurrentDay+", "+dayOfMonth + " " + month_in_String + " " + year);

                            getTransactionForSelectedDate(menuItemKey,DateString, vendorkey,subctgykey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();
    }

    private void getTransactionForSelectedDate(String menuItemKey, String transactiontime, String vendorKey, String subctgykey) {

        displaying_menuItems.clear();
        menuTransaction_array.clear();

        Adjusting_Widgets_Visibility(true);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_MenuAvailabilityTransaction + "?menuItemKey=" + menuItemKey + "&vendorkey=" + vendorKey + "&transactiontime=" + transactiontime+"&subctgykey="+subctgykey, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            //Log.d(Constants.TAG, "getOrderDetailsUsingApi Response: " + response);
                            try{
                                menuTransactionJsonString = response.toString();

                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }

                            try{
                                convertStringtoData(menuTransactionJsonString);

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
                    Toast.makeText(MenuAvailabilityStatusTransaction.this, "Availabilty of the Item has not changed yet on this date", Toast.LENGTH_LONG).show();
                    Adjusting_Widgets_Visibility(false);
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getLocalizedMessage());
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.getMessage());
                    //Log.d(Constants.TAG, "getOrderDetailsUsingApi Error: " + error.toString());
                    listviewInstruction.setText("Availabilty of the Item has not changed yet on this date");
                    listviewInstruction.setVisibility(View.VISIBLE);
                    MenuItemsListView.setVisibility(View.GONE);
                    error.printStackTrace();
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
            Volley.newRequestQueue(MenuAvailabilityStatusTransaction.this).add(jsonObjectRequest);
        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

    private void convertStringtoData(String menuTransactionJsonString) {


            //      tokenNo_hashmap.clear();
            //    quantity_hashmap.clear();
            //  slottimeRange_hashmap.clear();
            try{
                menuTransaction_array.clear();

                Adjusting_Widgets_Visibility(true);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            try {
                String ordertype="#";

                //converting jsonSTRING into array

                JSONObject jsonObject = new JSONObject(menuTransactionJsonString);
                JSONArray JArray  = jsonObject.getJSONArray("content");
                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                int i1=0;
                int arrayLength = JArray.length();
                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);


                for(;i1<(arrayLength);i1++) {

                    try {
                        JSONObject json = JArray.getJSONObject(i1);
                        //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));
                        String  allownegativestock = "" , itemname = "",key ="",menuItemKeyfromdb="",mobileno="",status="",subCtgykey="",transactiontime="",vendorkeyfromdb="",transcationstatus="",issubctgyavailabilitychanged="";
                        try {
                            if (json.has("itemname")) {
                                itemname = String.valueOf(json.get("itemname"));

                            } else {
                                itemname = "";
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            itemname = "";

                        }

                                try{
                                    if (json.has("key")) {
                                        key = String.valueOf(json.get("key"));

                                    } else {
                                        key = "";
                                    }

                                }
                                catch (Exception e){
                                    key = "";
                                    e.printStackTrace();
                                }



                                try{
                                    if (json.has("menuItemKey")) {
                                        menuItemKeyfromdb = String.valueOf(json.get("menuItemKey"));

                                    } else {
                                        try{
                                            if (json.has("menuitemkey")) {
                                                menuItemKeyfromdb = String.valueOf(json.get("menuitemkey"));

                                            } else {
                                                menuItemKeyfromdb = "";
                                            }

                                        }
                                        catch (Exception e){
                                            menuItemKeyfromdb = "";
                                            e.printStackTrace();
                                        }
                                    }

                                }
                                catch (Exception e){
                                    menuItemKeyfromdb = "";
                                    e.printStackTrace();
                                }


                        try{
                            if (json.has("mobileno")) {
                                mobileno = String.valueOf(json.get("mobileno"));

                            } else {
                                mobileno = "";
                            }

                        }
                        catch (Exception e){
                            mobileno = "";
                            e.printStackTrace();
                        }

                        try{
                            if (json.has("transcationstatus")) {
                                transcationstatus = String.valueOf(json.get("transcationstatus"));

                            } else {
                                transcationstatus = "";
                            }

                        }
                        catch (Exception e){
                            transcationstatus = "";
                            e.printStackTrace();
                        }

                        try{
                            if (json.has("subCtgykey")) {
                                subCtgykey = String.valueOf(json.get("subCtgykey"));

                            } else {
                                try{
                                    if (json.has("tmcsubctgykey")) {
                                        subCtgykey = String.valueOf(json.get("tmcsubctgykey"));

                                    } else {
                                        subCtgykey = "";
                                    }

                                }
                                catch (Exception e){
                                    subCtgykey = "";
                                    e.printStackTrace();
                                }                            }

                        }
                        catch (Exception e){
                            subCtgykey = "";
                            e.printStackTrace();
                        }


                        try{
                            if (json.has("status")) {
                                status = String.valueOf(json.get("status"));

                            } else {
                                status = "";
                            }

                        }
                        catch (Exception e){
                            status = "";
                            e.printStackTrace();
                        }


                        try{
                            if (json.has("transactiontime")) {
                                transactiontime = String.valueOf(json.get("transactiontime"));

                            } else {
                                transactiontime = "";
                            }

                        }
                        catch (Exception e){
                            transactiontime = "";
                            e.printStackTrace();
                        }

                        try{
                            if (json.has("vendorkey")) {
                                vendorkeyfromdb = String.valueOf(json.get("vendorkey"));

                            } else {
                                vendorkeyfromdb = "";
                            }

                        }
                        catch (Exception e){
                            vendorkeyfromdb = "";
                            e.printStackTrace();
                        }
                        try{
                            if (json.has("allownegativestock")) {
                                allownegativestock = String.valueOf(json.get("allownegativestock"));

                            } else {
                                allownegativestock = "";
                            }

                        }
                        catch (Exception e){
                            allownegativestock = "";
                            e.printStackTrace();
                        }

                        try{
                            if (json.has("issubctgyavailabilitychanged")) {
                                issubctgyavailabilitychanged = String.valueOf(json.get("issubctgyavailabilitychanged"));

                            } else {
                                issubctgyavailabilitychanged = "false";
                            }

                        }
                        catch (Exception e){
                            issubctgyavailabilitychanged = "false";
                            e.printStackTrace();
                        }
                        //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);


                        Modal_MenuAvailabilityStatusTransaction modal_menuAvailabilityStatusTransaction = new Modal_MenuAvailabilityStatusTransaction();
                        modal_menuAvailabilityStatusTransaction.itemname =itemname;
                        modal_menuAvailabilityStatusTransaction.key = key;
                        modal_menuAvailabilityStatusTransaction.menuItemKeyfromdb = menuItemKeyfromdb;
                        modal_menuAvailabilityStatusTransaction.mobileno = mobileno;
                        modal_menuAvailabilityStatusTransaction.status = status;
                        modal_menuAvailabilityStatusTransaction.allownegativestock = allownegativestock;
                        modal_menuAvailabilityStatusTransaction.issubctgyavailabilitychanged = issubctgyavailabilitychanged;

                        modal_menuAvailabilityStatusTransaction.subCtgykey = subCtgykey;
                        modal_menuAvailabilityStatusTransaction.transactiontime = transactiontime;
                        modal_menuAvailabilityStatusTransaction.transcationstatus =transcationstatus;
                        modal_menuAvailabilityStatusTransaction.vendorkeyfromdb = vendorkeyfromdb;
                        modal_menuAvailabilityStatusTransaction.transactionTimeLong = getTransactionTimeLong(transactiontime);
                        menuTransaction_array.add(modal_menuAvailabilityStatusTransaction);


                    } catch (JSONException e) {
                        e.printStackTrace();

                        Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getLocalizedMessage());
                        Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.getMessage());
                        //Log.d(Constants.TAG, "convertingJsonStringintoArray e: " + e.toString());

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();

            }




            try {
                if (menuTransaction_array.size() > 0 ) {


                    try{
                        Collections.sort(menuTransaction_array, new Comparator<Modal_MenuAvailabilityStatusTransaction>() {
                            public int compare(final Modal_MenuAvailabilityStatusTransaction object1, final Modal_MenuAvailabilityStatusTransaction object2) {
                                String TransactionTimeLong_1 = object1.getTransactionTimeLong();
                                String TransactionTimeLong_2 = object2.getTransactionTimeLong();

                                if ((TransactionTimeLong_1.equals("")) || (TransactionTimeLong_1.equals("null")) || (TransactionTimeLong_1.equals(null))) {
                                    TransactionTimeLong_1 = String.valueOf(0);
                                }
                                if ((TransactionTimeLong_2.equals("")) || (TransactionTimeLong_2.equals("null")) || (TransactionTimeLong_2.equals(null))) {
                                    TransactionTimeLong_2 = String.valueOf(0);
                                }

                                Long i2 = Long.valueOf(TransactionTimeLong_2);
                                Long i1 = Long.valueOf(TransactionTimeLong_1);

                                return i2.compareTo(i1);
                            }
                        });

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }


                    MenuItemsListView.setVisibility(View.VISIBLE);
                    listviewInstruction.setVisibility(View.GONE);
                    try {

                    Adapter_MenuAvailabilityStatusTransaction adapter_menuAvailabilityStatusTransaction = new Adapter_MenuAvailabilityStatusTransaction(MenuAvailabilityStatusTransaction.this,menuTransaction_array);
                        MenuItemsListView.setAdapter(adapter_menuAvailabilityStatusTransaction);
                        Adjusting_Widgets_Visibility(false);

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    MenuItemsListView.setVisibility(View.GONE);
                    listviewInstruction.setVisibility(View.VISIBLE);
                    Adjusting_Widgets_Visibility(false);
                    listviewInstruction.setText("Availabilty of the Item has not changed yet on this date");

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }

    private String getTransactionTimeLong(String transactiontime) {
        String longvalue = "";
        try {
            String time1 = transactiontime;
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
                String time1 = transactiontime;
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


    private void Adjusting_Widgets_Visibility(boolean show) {
        if (show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

            listviewInstruction.setText("Loading Please Wait");

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





    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-1);



        Date c1 = calendar.getTime();


        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);




        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy");
        String  PreviousdayDate = df1.format(c1);

        System.out.println("todays Date  " + CurrentDate);
        System.out.println("PreviousdayDate Date  " + PreviousdayDate);

        String yesterdayAsString = PreviousdayDay+", "+PreviousdayDate;

        return yesterdayAsString;
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


    public String getDate_and_time() {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);


        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        String CurrentDate = df.format(c);


        return CurrentDate;
    }


    private String getDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat day = new SimpleDateFormat("EEE");
        String CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy");
        CurrentDate = df.format(c);

        CurrentDate = CurrentDay + ", " + CurrentDate;
        System.out.println("todays Date  " + CurrentDate);


        return CurrentDate;
    }




}
