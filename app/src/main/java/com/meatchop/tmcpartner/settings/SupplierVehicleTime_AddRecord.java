package com.meatchop.tmcpartner.settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class SupplierVehicleTime_AddRecord extends AppCompatActivity {
    TextView vendorNameTextWidget,date_selector;
    ListView supplierVehicle_listview;
    Button saveDetails;
    String UserPhoneNumber ="",vendorkey ="",vendorName="";
    DatePickerDialog datepicker;
    LinearLayout loadingpanelmask,loadingPanel;
    List<String> supplierVehicleNameArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_vehicle_time__add_record);
        SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        UserPhoneNumber = (shared.getString("UserPhoneNumber", "+91"));
        vendorkey = shared.getString("VendorKey", "");
        vendorName = shared.getString("VendorName", "");


        vendorNameTextWidget = findViewById(R.id.vendorNameTextWidget);
        date_selector = findViewById(R.id.date_selector);
        supplierVehicle_listview = findViewById(R.id.supplierVehicle_listview);
        saveDetails = findViewById(R.id.saveDetails);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);


        vendorNameTextWidget.setText(vendorName);
        getSupplierVehicleDetails();
        date_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDatePicker();

            }
        });

    }

    private void getSupplierVehicleDetails() {
        supplierVehicleNameArray.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetSupplierVehicleDetails,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONArray result  = response.getJSONArray("content");
                            for(int i =0; i<result.length(); i++){
                                JSONObject jsonObject = result.getJSONObject(i);
                                String vehicleName = "";
                                try{
                                    if(jsonObject.has("vehiclename")) {
                                        vehicleName = String.valueOf(jsonObject.get("vehiclename"));
                                        supplierVehicleNameArray.add(vehicleName);
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                  if((result.length() - 1 )==i){
                                      callsupplierVehicleNameDetailsAdapter();
                                  }
                            }




                        } catch (JSONException e) {
                           // Adjusting_Widgets_Visibility(false);

                            e.printStackTrace();
                        }

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());
                //Adjusting_Widgets_Visibility(false);

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
        Volley.newRequestQueue(SupplierVehicleTime_AddRecord.this).add(jsonObjectRequest);

    }

    private void callsupplierVehicleNameDetailsAdapter() {
        Adapter_SupplierVehicleName adapter_supplierVehicleName = new Adapter_SupplierVehicleName(SupplierVehicleTime_AddRecord.this,supplierVehicleNameArray,SupplierVehicleTime_AddRecord.this);
        supplierVehicle_listview.setAdapter(adapter_supplierVehicleName);


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

    private void openDatePicker() {



        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        datepicker = new DatePickerDialog(SupplierVehicleTime_AddRecord.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {

                            String month_in_String = getMonthString(monthOfYear);
                            String monthstring = String.valueOf(monthOfYear + 1);
                            String datestring = String.valueOf(dayOfMonth);
                            if (datestring.length() == 1) {
                                datestring = "0" + datestring;
                            }
                            if (monthstring.length() == 1) {
                                monthstring = "0" + monthstring;
                            }

                            Calendar myCalendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);

                            int dayOfWeek = myCalendar.get(Calendar.DAY_OF_WEEK);

                            String CurrentDay = getDayString(dayOfWeek);
                            //Log.d(Constants.TAG, "dayOfWeek Response: " + dayOfWeek);


                           String DateString = (CurrentDay + ", " + dayOfMonth + " " + month_in_String + " " + year);

                            date_selector.setText(DateString);

                            getSupplierVehicleArrivalDetailsForReport(DateString,vendorkey);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, year, month, day);






        Calendar c = Calendar.getInstance();



        DatePicker datePicker = datepicker.getDatePicker();

        c.add(Calendar.DATE, -30);
        // Toast.makeText(getApplicationContext(), Calendar.DATE, Toast.LENGTH_LONG).show();
        Log.d(Constants.TAG, "Calendar.DATE " + String.valueOf(Calendar.DATE));
        long oneMonthAhead = c.getTimeInMillis();
        datePicker.setMaxDate(System.currentTimeMillis() - 1000);
        datePicker.setMinDate(oneMonthAhead);

        datepicker.show();
    }

    private void getSupplierVehicleArrivalDetailsForReport(String dateString, String vendorkey) {

        String newDateFormat = convertOldFormatDateintoNewFormat(dateString);

    }


    private String getMonthString(int value) {
        if (value == 0) {
            return "Jan";
        } else if (value == 1) {
            return "Feb";
        } else if (value ==2) {
            return "Mar";
        } else if (value ==3) {
            return "Apr";
        } else if (value ==4) {
            return "May";
        } else if (value ==5) {
            return "Jun";
        } else if (value ==6) {
            return "Jul";
        } else if (value ==7) {
            return "Aug";
        } else if (value ==8) {
            return "Sep";
        } else if (value ==9) {
            return "Oct";
        } else if (value ==10) {
            return "Nov";
        } else if (value ==11) {
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

    private String convertOldFormatDateintoNewFormat(String todaysdate) {
        String  CurrentDate ="";
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

}