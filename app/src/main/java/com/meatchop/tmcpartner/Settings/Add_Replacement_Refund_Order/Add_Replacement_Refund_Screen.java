package com.meatchop.tmcpartner.Settings.Add_Replacement_Refund_Order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_Replacement_Refund_Screen extends AppCompatActivity {
    Button fetchOrders_buttonWidget;
    EditText customermobileno_editwidget;
    TextView vendorName_textWidget,orderscount_textwidget;
    ListView orders_listview;
    String vendorName,ordertype,vendorkey,orderplaceddate ="";

    LinearLayout loadingpanelmask,loadingPanel;
    static List<Modal_ManageOrders_Pojo_Class> ordersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__refund__replacement__screen);


        loadingPanel = findViewById(R.id.loadingPanel);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        ordersList = new ArrayList<Modal_ManageOrders_Pojo_Class>();


        fetchOrders_buttonWidget = findViewById(R.id.fetchOrders_buttonWidget);
        customermobileno_editwidget = findViewById(R.id.customermobileno_editwidget);
        orders_listview = findViewById(R.id.orders_listview);
        vendorName_textWidget = findViewById(R.id.vendorName_textWidget);
        orderscount_textwidget= findViewById(R.id.orderscount_textwidget);
        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorName = shared.getString("VendorName","");
            vendorkey  = shared.getString("VendorKey","");
            vendorName_textWidget.setText(String.valueOf(vendorName));

        }
        catch(Exception e){
            e.printStackTrace();
        }

        fetchOrders_buttonWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileno = customermobileno_editwidget.getText().toString();
                if(mobileno.length()==10){
                    mobileno = "+91"+mobileno;
                    FetchOrdersFromDatabase(vendorName , mobileno);
                }
            }
        });



    }



    private void FetchOrdersFromDatabase(String vendorName, String mobileno) {
        final String[] Count = {"0"};


        String userMobileNumber ="";
        userMobileNumber = mobileno;
        if (userMobileNumber.length() == 13) {
            String userMobileNumberEncoded  = userMobileNumber;
            try {
                userMobileNumberEncoded = URLEncoder.encode(userMobileNumber, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            showProgressBar(true);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetOrderDetailsusingMobileno_vendorkey +"?mobileno="+userMobileNumberEncoded+"&vendorkey="+vendorkey, null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(@NonNull JSONObject response) {


                            try {
                                String jsonString = response.toString();
                                Log.d(Constants.TAG, " response: onMobileAppData " + response);
                                JSONObject jsonObject = new JSONObject(jsonString);

                                String message = jsonObject.getString("message").toString().toUpperCase();
                                JSONArray JArray = jsonObject.getJSONArray("content");

                                int i1 = 0;
                                int arrayLength = JArray.length();

                                if(message.equals("SUCCESS")){
                                    for (;i1<arrayLength;i1++){

                                        try {
                                            JSONObject json = JArray.getJSONObject(i1);
                                            Modal_ManageOrders_Pojo_Class manageOrdersPojoClass = new Modal_ManageOrders_Pojo_Class();
                                            //Log.d(Constants.TAG, "convertingJsonStringintoArray orderStatus: " + String.valueOf(json.get("orderStatus")));


                                            if (json.has("coupondiscount")) {
                                                manageOrdersPojoClass.coupondiscamount = String.valueOf(json.get("coupondiscount"));

                                            } else {
                                                manageOrdersPojoClass.coupondiscamount = "";
                                            }

                                            if (json.has("ordertype")) {
                                                manageOrdersPojoClass.ordertype = String.valueOf(json.get("ordertype"));

                                            } else {
                                                manageOrdersPojoClass.ordertype = "";
                                            }
                                            if (json.has("ordertype")) {
                                                manageOrdersPojoClass.orderType = String.valueOf(json.get("ordertype"));

                                            } else {
                                                manageOrdersPojoClass.orderType = "";
                                            }

                                            if (json.has("couponkey")) {
                                                manageOrdersPojoClass.couponkey = String.valueOf(json.get("couponkey"));

                                            } else {
                                                manageOrdersPojoClass.couponkey = "";
                                            }

                                            if (json.has("deliveryamount")) {
                                                manageOrdersPojoClass.deliveryamount = String.valueOf(json.get("deliveryamount"));

                                            } else {
                                                manageOrdersPojoClass.deliveryamount = "";
                                            }



                                            if (json.has("deliverytype")) {
                                                manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));
                                            } else {
                                                manageOrdersPojoClass.deliverytype = "";
                                            }



                                            if (json.has("orderplacedtime")) {
                                                manageOrdersPojoClass.orderplacedtime = String.valueOf(json.get("orderplacedtime"));

                                            } else {
                                                manageOrdersPojoClass.orderplacedtime = "";
                                            }


                                            if (json.has("orderplaceddate")) {
                                                manageOrdersPojoClass.orderplaceddate = String.valueOf(json.get("orderplaceddate"));
                                                orderplaceddate = String.valueOf(json.get("orderplaceddate"));
                                            } else {
                                                manageOrdersPojoClass.orderplaceddate = "";
                                            }


                                            if (json.has("orderid")) {
                                                manageOrdersPojoClass.orderid = String.valueOf(json.get("orderid"));

                                            } else {
                                                manageOrdersPojoClass.orderid = "";
                                            }


                                            if (json.has("payableamount")) {
                                                manageOrdersPojoClass.payableamount = String.valueOf(json.get("payableamount"));

                                            } else {
                                                manageOrdersPojoClass.payableamount = "";
                                            }


                                            if (json.has("paymentmode")) {
                                                manageOrdersPojoClass.paymentmode = String.valueOf(json.get("paymentmode"));

                                            } else {
                                                manageOrdersPojoClass.paymentmode = "";
                                            }


                                            if (json.has("tokenno")) {
                                                manageOrdersPojoClass.tokenno = String.valueOf(json.get("tokenno"));

                                            } else {
                                                manageOrdersPojoClass.tokenno = "";
                                            }


                                            try {
                                                if (json.has("itemdesp")) {
                                                    JSONArray itemdesp = json.getJSONArray("itemdesp");

                                                    manageOrdersPojoClass.itemdesp = itemdesp;

                                                } else {
                                                    manageOrdersPojoClass.itemdesp = new JSONArray();
                                                    //Log.i(Constants.TAG, "Can't Get itemDesp");
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            if (json.has("useraddress")) {
                                                manageOrdersPojoClass.useraddress = String.valueOf(json.get("useraddress"));

                                            } else {
                                                manageOrdersPojoClass.useraddress = "";
                                            }
                                            if (json.has("userkey")) {
                                                manageOrdersPojoClass.userkey = String.valueOf(json.get("userkey"));

                                            } else {
                                                manageOrdersPojoClass.userkey = "";
                                            }



                                            if (json.has("usermobile")) {
                                                manageOrdersPojoClass.usermobile = String.valueOf(json.get("usermobile"));

                                            } else {
                                                manageOrdersPojoClass.usermobile = "";
                                            }


                                            if (json.has("vendorkey")) {
                                                manageOrdersPojoClass.vendorkey = String.valueOf(json.get("vendorkey"));

                                            } else {
                                                manageOrdersPojoClass.vendorkey = "";
                                            }

                                            if (json.has("deliverytype")) {
                                                manageOrdersPojoClass.deliverytype = String.valueOf(json.get("deliverytype"));

                                            } else {
                                                manageOrdersPojoClass.deliverytype = "";
                                            }

                                            if (json.has("slotname")) {
                                                manageOrdersPojoClass.slotname = String.valueOf(json.get("slotname"));

                                            } else {
                                                manageOrdersPojoClass.slotname = "";
                                            }



                                            try {
                                                String slottime = "";
                                                slottime = String.valueOf(String.valueOf(json.get("slottimerange")));
                                                String estimated_Slottime = "";
                                                if (String.valueOf(String.valueOf(json.get("slotname"))).toUpperCase().equals(Constants.EXPRESSDELIVERY_SLOTNAME)) {
                                                    String orderPlacedTime = String.valueOf(json.get("orderplacedtime"));

                                                    estimated_Slottime = getSlotTime(slottime, orderPlacedTime);


                                                    try {
                                                        manageOrdersPojoClass.slottimerange = String.valueOf(estimated_Slottime);


                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }


                                                } else {


                                                    try {

                                                        manageOrdersPojoClass.slottimerange = String.valueOf(slottime);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                                try {
                                                    if (json.has("slottimerange")) {
                                                        manageOrdersPojoClass.slottimerange = String.valueOf(json.get("slottimerange"));

                                                    } else {
                                                        manageOrdersPojoClass.slottimerange = "";
                                                    }
                                                } catch (Exception e1) {
                                                    manageOrdersPojoClass.slottimerange = "";

                                                    e1.printStackTrace();
                                                }
                                            }



                                            if (json.has("slotdate")) {
                                                manageOrdersPojoClass.slotdate = String.valueOf(json.get("slotdate"));

                                            } else {
                                                manageOrdersPojoClass.slotdate = "";
                                            }





                                            if (json.has("vendorname")) {
                                                manageOrdersPojoClass.vendorname = String.valueOf(json.get("vendorname"));

                                            } else {
                                                manageOrdersPojoClass.vendorname = "";
                                            }


                                            //ordersList.add(manageOrdersPojoClass);
                                            try {
                                                String predicteddateanddayandtime = getDatewithNameofthePreviousDay();


                                                long predictedLongForDate = Long.parseLong(getLongValuefortheDate(predicteddateanddayandtime));


                                                long currentTimeLong = Long.parseLong(getLongValuefortheDate(orderplaceddate));
                                                if (predicteddateanddayandtime.length() > 0 && orderplaceddate.length() > 0) {


                                                    if (predictedLongForDate <= currentTimeLong) {

                                                        ordersList.add(manageOrdersPojoClass);
                                                    }
                                                } else {
                                                    ordersList.add(manageOrdersPojoClass);
                                                }
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            //Log.d(Constants.TAG, "convertingJsonStringintoArray ordersList: " + ordersList);

                                        } catch (JSONException e) {
                                            e.printStackTrace();

                                        }


                                    }

                                    Adapter_Refund_Replacement_Listview adapter_replacement_refundProcess = new Adapter_Refund_Replacement_Listview(Add_Replacement_Refund_Screen.this                      , ordersList, Add_Replacement_Refund_Screen.this);
                                    orders_listview.setAdapter(adapter_replacement_refundProcess);


                                    try{
                                        orderscount_textwidget.setText(String.valueOf(ordersList.size()));
                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
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
            Volley.newRequestQueue(Add_Replacement_Refund_Screen.this).add(jsonObjectRequest);


        } else {
            AlertDialogClass.showDialog(Add_Replacement_Refund_Screen.this, R.string.Enter_the_mobile_no_text);

        }
    }

    public String getLongValuefortheDate(String orderplacedtime) {
        String longvalue = "";
        try {
            String time1 = orderplacedtime;
            //   Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            Date date = sdf.parse(time1);
            long time1long = date.getTime() / 1000;
            longvalue = String.valueOf(time1long);

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                String time1 = orderplacedtime;
                //     Log.d(TAG, "time1long  "+orderplacedtime);

                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
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



    private String getSlotTime(String slottime, String orderplacedtime) {
        String result = "", lastFourDigits = "";
        //   Log.d(TAG, "slottime  "+slottime);
        if (slottime.contains("mins")) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

                final Date date = sdf.parse(orderplacedtime);
                final Calendar calendar = Calendar.getInstance();
                String timeoftheSlot ="";
                try {
                    timeoftheSlot = (slottime.replaceAll("[^\\d.]", ""));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                int timeoftheSlotDouble =0;
                try {
                    timeoftheSlotDouble = Integer.parseInt(timeoftheSlot);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                calendar.setTime(date);
                SimpleDateFormat sdff = new SimpleDateFormat("HH:mm");
                String placedtime = String.valueOf(sdff.format(calendar.getTime()));
                calendar.add(Calendar.MINUTE, timeoftheSlotDouble);

                System.out.println("Time here " + sdff.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + orderplacedtime);
                result = placedtime +" - "+String.valueOf(sdff.format(calendar.getTime()));
                System.out.println("Time here 90 mins" + result);

                result = result.replaceAll("GMT[+]05:30", "");

                //  System.out.println("Time here "+result);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (slottime.length() > 5) {
                lastFourDigits = slottime.substring(slottime.length() - 5);
            } else {
                lastFourDigits = slottime;
            }

            //  result = slotdate + " " + lastFourDigits + ":00";

        }
        return result;
    }


    private void showProgressBar(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);

        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
            //bottomNavigationView.setVisibility(View.VISIBLE);

        }

    }


    private String getDatewithNameofthePreviousDay() {
        Calendar calendar = Calendar.getInstance();



        calendar.add(Calendar.DATE,-14);



        Date c1 = calendar.getTime();

        SimpleDateFormat previousday = new SimpleDateFormat("EEE");
        String PreviousdayDay = previousday.format(c1);

        SimpleDateFormat df1 = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        String  PreviousdayDate = df1.format(c1);
        PreviousdayDate = PreviousdayDay+", "+PreviousdayDate;



        return PreviousdayDate;
    }




}