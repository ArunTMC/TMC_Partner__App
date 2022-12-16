package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DeliverySlotsTransactionDetailsReport extends AppCompatActivity {
    ListView listView;
    LinearLayout dateSelectorLayout , newTransactionSync_Layout ,loadingpanelmask , loadingPanel;
    TextView slotname_text_widget ,deliverytiming_textview ,slottime_text_widget ,dateSelector_text ;
    String slotKey = "" , slotname = "" , vendorkey = "" , deliverytime="";
    double screenInches;
    String CurrentDate, PreviousDateString;
    String DateString;
    DatePickerDialog datepicker;
    ArrayList<Modal_DeliverySlotTransactions> deliverySlotTransactionsArrayList = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_slots_transaction_details_report);
        slotname_text_widget = findViewById(R.id.slotname_text_widget);
        dateSelectorLayout = findViewById(R.id.dateSelectorLayout);
        slottime_text_widget = findViewById(R.id.slottime_text_widget);
        newTransactionSync_Layout = findViewById(R.id.newTransactionSync_Layout);
        loadingPanel = findViewById(R.id.loadingPanel);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        dateSelector_text = findViewById(R.id.dateSelector_text);

        listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        slotKey = intent.getExtras().getString("deliveryslotkey");
        slotname = intent.getExtras().getString("deliveryslotname");
        vendorkey = intent.getExtras().getString("vendorkey");
        vendorkey = intent.getExtras().getString("vendorkey");
        deliverytime = intent.getExtras().getString("deliverytime");

        slottime_text_widget.setText(deliverytime);
        slotname_text_widget.setText(slotname);


        try {
            ScreenSizeOfTheDevice screenSizeOfTheDevice = new ScreenSizeOfTheDevice();
            screenInches = screenSizeOfTheDevice.getDisplaySize(DeliverySlotsTransactionDetailsReport.this);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
                double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
                screenInches = Math.sqrt(x + y);

            } catch (Exception e1) {
                e1.printStackTrace();
            }


        }




        loadingpanelmask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DeliverySlotsTransactionDetailsReport.this, "Loading.... Please Wait", Toast.LENGTH_SHORT).show();
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


        datepicker = new DatePickerDialog(DeliverySlotsTransactionDetailsReport.this,
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
                            String TodateAsNewFormat =convertOldFormatDateintoNewFormat(DateString);

                            getTransactionForSelectedDate(slotKey,TodateAsNewFormat, vendorkey);

                        }
                        catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }, year, month, day);
        datepicker.show();
    }

    private void getTransactionForSelectedDate(String slotKey, String dateString, String vendorkey) {
        deliverySlotTransactionsArrayList .clear();


        showProgressBar(true);
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetDeliverySlotTransactions + "?transactiontime=" + dateString + "&vendorkey=" + vendorkey + "&slotkey=" + slotKey, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {
                            try {
                                JSONArray JArray = response.getJSONArray("content");
                                //Log.d(Constants.TAG, "convertingJsonStringintoArray Response: " + JArray);
                                int i1 = 0;
                                int arrayLength = JArray.length();
                                //Log.d("Constants.TAG", "convertingJsonStringintoArray Response: " + arrayLength);
                                if (arrayLength == 0) {
                                    showProgressBar(false);
                                    return;
                                }
                                else{
                                for (; i1 < (arrayLength); i1++) {
                                    Modal_DeliverySlotTransactions modal_deliverySlotTransactions = new Modal_DeliverySlotTransactions();
                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);

                                        try {
                                            if (json.has("deliverytime")) {
                                                modal_deliverySlotTransactions.setDeliverytime(String.valueOf(json.get("deliverytime")));

                                            } else {
                                                modal_deliverySlotTransactions.setDeliverytime(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setDeliverytime(String.valueOf(""));


                                        }

                                        try {
                                            if (json.has("slotname")) {
                                                modal_deliverySlotTransactions.setSlotname(String.valueOf(json.get("slotname")));

                                            } else {
                                                modal_deliverySlotTransactions.setSlotname(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setSlotname(String.valueOf(""));


                                        }


                                        try {
                                            if (json.has("mobileno")) {
                                                modal_deliverySlotTransactions.setMobileno(String.valueOf(json.get("mobileno")));

                                            } else {
                                                modal_deliverySlotTransactions.setMobileno(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setMobileno(String.valueOf(""));


                                        }


                                        try {
                                            if (json.has("key")) {
                                                modal_deliverySlotTransactions.setKey(String.valueOf(json.get("key")));

                                            } else {
                                                modal_deliverySlotTransactions.setKey(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setKey(String.valueOf(""));


                                        }

                                        try {
                                            if (json.has("vendorkey")) {
                                                modal_deliverySlotTransactions.setVendorkey(String.valueOf(json.get("vendorkey")));

                                            } else {
                                                modal_deliverySlotTransactions.setVendorkey(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setVendorkey(String.valueOf(""));


                                        }


                                        try {
                                            if (json.has("slotkey")) {
                                                modal_deliverySlotTransactions.setSlotkey(String.valueOf(json.get("slotkey")));

                                            } else {
                                                modal_deliverySlotTransactions.setSlotkey(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setSlotkey(String.valueOf(""));


                                        }


                                        try {
                                            if (json.has("slotdatetype")) {
                                                modal_deliverySlotTransactions.setSlotdatetype(String.valueOf(json.get("slotdatetype")));

                                            } else {
                                                modal_deliverySlotTransactions.setSlotdatetype(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setSlotdatetype(String.valueOf(""));


                                        }


                                        try {
                                            if (json.has("slotstatus")) {
                                                modal_deliverySlotTransactions.setSlotstatus(String.valueOf(json.get("slotstatus")));

                                            } else {
                                                modal_deliverySlotTransactions.setSlotstatus(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setSlotstatus(String.valueOf(""));


                                        }


                                        try {
                                            if (json.has("transactionstatus")) {
                                                modal_deliverySlotTransactions.setTransactionstatus(String.valueOf(json.get("transactionstatus")));

                                            } else {
                                                modal_deliverySlotTransactions.setTransactionstatus(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setTransactionstatus(String.valueOf(""));


                                        }

                                        try {
                                            if (json.has("transactiontime")) {
                                                modal_deliverySlotTransactions.setTransactiontime(String.valueOf(json.get("transactiontime")));

                                            } else {
                                                modal_deliverySlotTransactions.setTransactiontime(String.valueOf(""));
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_deliverySlotTransactions.setTransactiontime(String.valueOf(""));


                                        }


                                        deliverySlotTransactionsArrayList.add(modal_deliverySlotTransactions);


                                    } catch (Exception e1) {
                                        e1.printStackTrace();
                                    }


                                    if (i1 - (arrayLength - 1) == 0) {
                                        setAdapter();
                                    }


                                }
                            }
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }



                        }

                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(@NonNull VolleyError error) {
                    Toast.makeText(DeliverySlotsTransactionDetailsReport.this, "Availabilty of the Item has not changed yet on this date", Toast.LENGTH_LONG).show();
                    showProgressBar(false);

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
            Volley.newRequestQueue(DeliverySlotsTransactionDetailsReport.this).add(jsonObjectRequest);
        }
        catch (Exception e){
            e.printStackTrace();
        }







    }

    private void setAdapter() {


        Adapter_DeliverySlotTransactionDetails adapter_deliverySlotTransactionDetails = new Adapter_DeliverySlotTransactionDetails(DeliverySlotsTransactionDetailsReport.this , deliverySlotTransactionsArrayList );
        listView.setAdapter(adapter_deliverySlotTransactionDetails);
        showProgressBar(false);


    }

    private void showProgressBar(boolean show) {

        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else {
            loadingpanelmask.setVisibility(View.GONE);
            loadingPanel.setVisibility(View.GONE);
        }
    }






    private String convertOldFormatDateintoNewFormat(String todaysdate) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        try {
            Date date = sdf.parse(todaysdate);


            SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
            day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

            CurrentDate = day.format(date);



        } catch (ParseException e) {
            e.printStackTrace();
        }
        return CurrentDate;

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




}