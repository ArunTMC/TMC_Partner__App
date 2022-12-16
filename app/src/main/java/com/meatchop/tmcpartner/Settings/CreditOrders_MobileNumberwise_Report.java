package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Modal_ManageOrders_Pojo_Class;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

public class CreditOrders_MobileNumberwise_Report extends AppCompatActivity {
EditText  customermobileno_editwidget;
String mobileno = "",encodeMobileno ="";
LinearLayout loadingpanelmask, loadingPanel,cardView_parentLayout;
Button fetchCreditDetails_buttonWidget,generateCreditTranscReport_button_widget,viewCreditTransactionList_button_widget,addpaymentDetailsButton;
TextView vendorName_textWidget, usermobileno_text_widget,lastupdatedtime_textwidget,totalCreditValue_text_widget,creditAmountPaidtill_text_widget,creditAmountYetTobePaid_text_widget;
    String vendorKey="", vendormobileNo ="",vendorName = "";
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String transactionType ="";
    Modal_CreditOrderDetails modal_creditOrderDetails ;
    private static int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int OPENPDF_ACTIVITY_REQUEST_CODE = 2;

    List<Modal_CreditOrdersTransactionDetails> creditOrdersTransactionDetails_Array = new ArrayList<>();
    Boolean isUpdateCreditOrderDetailsIsCalled =false , isAddCreditOrdersTransactionDetailsIsCalled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustFontScale( getResources().getConfiguration());

        setContentView(R.layout.activity_credit_orders__mobile_numberwise__report);
        vendorName_textWidget = findViewById(R.id.vendorName_textWidget);
        fetchCreditDetails_buttonWidget = findViewById(R.id.fetchCreditDetails_buttonWidget);
        customermobileno_editwidget = findViewById(R.id.customermobileno_editwidget);
        usermobileno_text_widget = findViewById(R.id.usermobileno_text_widget);
        lastupdatedtime_textwidget = findViewById(R.id.lastupdatedtime_textwidget);
        totalCreditValue_text_widget = findViewById(R.id.totalCreditValue_text_widget);
        creditAmountPaidtill_text_widget = findViewById(R.id.creditAmountPaidtill_text_widget);
        creditAmountYetTobePaid_text_widget = findViewById(R.id.creditAmountYetTobePaid_text_widget);
        addpaymentDetailsButton = findViewById(R.id.addpaymentDetailsButton);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        cardView_parentLayout  = findViewById(R.id.cardView_parentLayout);
        generateCreditTranscReport_button_widget = findViewById(R.id.generateCreditTranscReport_button_widget);
        viewCreditTransactionList_button_widget = findViewById(R.id.viewCreditTransactionList_button_widget);

        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = shared.getString("VendorKey","");
            vendorName = shared.getString("VendorName","");

            vendormobileNo = (shared.getString("UserPhoneNumber", "+91"));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));

        }
        catch(Exception e){
            e.printStackTrace();
        }
        modal_creditOrderDetails = new Modal_CreditOrderDetails();
        creditOrdersTransactionDetails_Array.clear();
        vendorName_textWidget .setText(vendorName);
        cardView_parentLayout.setVisibility(View.GONE);

        addpaymentDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogForAddPaymentDetails();



            }
        });


        fetchCreditDetails_buttonWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    mobileno = customermobileno_editwidget.getText().toString();
                    encodeMobileno = "+91"+mobileno;
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    Objects.requireNonNull(imm).hideSoftInputFromWindow(customermobileno_editwidget.getWindowToken(), 0);

                    FetchCreditOrdersDetails();
                    
                }
                catch (Exception e){
                    e.printStackTrace();
                }



            }
        });


        generateCreditTranscReport_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(creditOrdersTransactionDetails_Array.size()>0 ){
                    generateCreditOrdersPDFReport();

                }
                else{
                    Toast.makeText(CreditOrders_MobileNumberwise_Report.this, "There is no Transaction Entry", Toast.LENGTH_SHORT).show();

                }
            }
        });


        viewCreditTransactionList_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(creditOrdersTransactionDetails_Array.size()>0) {
                    Bundle b = new Bundle();
                    b.putParcelable("modal_creditOrderDetails", modal_creditOrderDetails);
                    b.putSerializable("creditOrdersTransactionDetails_Array", (Serializable) creditOrdersTransactionDetails_Array);
                    Intent intent = new Intent(CreditOrders_MobileNumberwise_Report.this, CreditOrders_MobileNumberwiseTransactionScreen.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(CreditOrders_MobileNumberwise_Report.this, "There is no Transaction Entry", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }

    private void openDialogForAddPaymentDetails() {

        Dialog dialog = new Dialog(CreditOrders_MobileNumberwise_Report.this);
        dialog.setContentView(R.layout.addcreditpaymentdetails);
        dialog.setTitle("Enter the amount Paid");
        dialog.setCanceledOnTouchOutside(true);
        EditText payingAmountEditText = dialog.findViewById(R.id.payingAmountEditText);

        Button addCreditPayment_button = dialog.findViewById(R.id.addCreditPayment_button);
        addCreditPayment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payingAmountString = "";
                payingAmountString = payingAmountEditText.getText().toString();
                payingAmountString  = payingAmountString.replaceAll("[^\\d.]", "");
                double payingAmountdouble =0 ,totalCreditAmountYettobePaid_double =0;
                try{

                    payingAmountdouble = Double.parseDouble(payingAmountString);
                }
                catch (Exception e){
                    payingAmountdouble =0;
                    e.printStackTrace();

                }

                if(payingAmountdouble >0){
                    String totalCreditAmountYettobePaid= "";
                    try{

                        totalCreditAmountYettobePaid = modal_creditOrderDetails.getTotalamountincredityettobepaid().toString();
                        totalCreditAmountYettobePaid = totalCreditAmountYettobePaid.replaceAll("[^\\d.]", "");

                    }
                    catch (Exception e){
                        totalCreditAmountYettobePaid ="0";
                        e.printStackTrace();
                    }
                    try {
                        totalCreditAmountYettobePaid_double = Double.parseDouble(totalCreditAmountYettobePaid);
                    }
                    catch (Exception e){
                        totalCreditAmountYettobePaid_double = 0;
                        e.printStackTrace();
                    }

                    if(payingAmountdouble<=totalCreditAmountYettobePaid_double){
                        Adjusting_Widgets_Visibility(true);
                        dialog.cancel();
                        updateCreditAmountinCreditOrderDetails(totalCreditAmountYettobePaid_double,payingAmountdouble);

                    }
                    else{
                        AlertDialogClass.showDialog(CreditOrders_MobileNumberwise_Report.this, Constants.GivenAmountAmountShouldnotBeGreateThanCreditValue , 0);

                    }

                }
                else{
                    AlertDialogClass.showDialog(CreditOrders_MobileNumberwise_Report.this, Constants.AmountShouldBeGreaterThanZero , 0);

                }

            }
        });
        dialog.show();

    }

    private void updateCreditAmountinCreditOrderDetails(double totalCreditAmountYettobePaid_double, double payingAmountdouble) {

        if(isUpdateCreditOrderDetailsIsCalled){
            return;
        }
        isUpdateCreditOrderDetailsIsCalled =true;
        double newCreditAmountYettobePaid_double =0;
        String currentTime = getDate_and_time();
        try{
            newCreditAmountYettobePaid_double = totalCreditAmountYettobePaid_double - payingAmountdouble;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();

        try {


            jsonObject.put("usermobileno", modal_creditOrderDetails.getUsermobileno());
            jsonObject.put("lastupdatedtime",currentTime );
            jsonObject.put("totalamountincredit",newCreditAmountYettobePaid_double );
            jsonObject.put("vendorkey", vendorKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        double finalNewCreditAmountYettobePaid_double = newCreditAmountYettobePaid_double;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_UpdateCreditOrderDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    if (message.equals("success")) {
                        UpdateDatainLocal(currentTime,totalCreditAmountYettobePaid_double,payingAmountdouble);
                        isUpdateCreditOrderDetailsIsCalled =false;
                        Adjusting_Widgets_Visibility(false);
                        addCreditOrdersTransactionDetails(modal_creditOrderDetails.getUsermobileno(),vendorKey,totalCreditAmountYettobePaid_double,payingAmountdouble,String.valueOf(finalNewCreditAmountYettobePaid_double),currentTime,Constants.CREDIT_AMOUNT_PAID);
                    }
                    else{

                    }
                } catch (JSONException e) {
                    isUpdateCreditOrderDetailsIsCalled =false;

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                isUpdateCreditOrderDetailsIsCalled =false;

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

        Volley.newRequestQueue(CreditOrders_MobileNumberwise_Report.this).add(jsonObjectRequest);

    }

    private void UpdateDatainLocal(String currentTime, double totalCreditAmountYettobePaid_double, double payingAmountdouble) {
        Modal_CreditOrdersTransactionDetails modal_creditOrdersTransactionDetails = new Modal_CreditOrdersTransactionDetails();
        modal_creditOrdersTransactionDetails.setVendorkey(vendorKey);
        modal_creditOrdersTransactionDetails.setUsermobileno(modal_creditOrderDetails.getUsermobileno());
        modal_creditOrdersTransactionDetails.setTransactionvalue(String.valueOf(payingAmountdouble));
        modal_creditOrdersTransactionDetails.setTransactiontype(Constants.CREDIT_AMOUNT_PAID);
        modal_creditOrdersTransactionDetails.setTransactiontime(currentTime);
        modal_creditOrdersTransactionDetails.setOldamountincredit(String.valueOf(totalCreditAmountYettobePaid_double));
    modal_creditOrderDetails.setLastupdatedtime(currentTime);
    double creditYettobePaid = totalCreditAmountYettobePaid_double - payingAmountdouble;
    String creditYettobePaidString = String.valueOf(Math.round(creditYettobePaid));
    modal_creditOrderDetails.setTotalamountincredityettobepaid(creditYettobePaidString);
    modal_creditOrdersTransactionDetails.setNewamountincredit(creditYettobePaidString);

    String amountPaidTillNow = modal_creditOrderDetails.getTotalPaidAmount().toString();
    amountPaidTillNow = amountPaidTillNow.replaceAll("[^\\d.]", "");

    double amountPaidTillNowDouble = 0;
    try{
        amountPaidTillNowDouble = Double.parseDouble(amountPaidTillNow);

    }
    catch (Exception e){
        e.printStackTrace();
    }
    try{
        amountPaidTillNowDouble =  amountPaidTillNowDouble + payingAmountdouble;
    }
    catch (Exception e){
        e.printStackTrace();
    }

        try{
            amountPaidTillNowDouble =  Math.round(amountPaidTillNowDouble);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        modal_creditOrderDetails.setTotalPaidAmount(String.valueOf(amountPaidTillNowDouble));
        creditOrdersTransactionDetails_Array.add(modal_creditOrdersTransactionDetails);
        DisplayData();
    }





    private void addCreditOrdersTransactionDetails(String usermobileno, String vendorKey, double oldCreditAmountYettobePaid_double, double payingAmountdouble, String newCreditAmountYettobePaid, String orderplacedTime, String transactiontype) {
        if(isAddCreditOrdersTransactionDetailsIsCalled){
            return;
        }

        isAddCreditOrdersTransactionDetailsIsCalled =true;

        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("vendorkey", vendorKey);

            jsonObject.put("usermobileno", usermobileno);
            jsonObject.put("transactiontime", orderplacedTime);
            jsonObject.put("transactiontype", transactiontype);

            jsonObject.put("oldamountincredit", oldCreditAmountYettobePaid_double);
            jsonObject.put("transactionvalue", Math.round(payingAmountdouble));
            jsonObject.put("newamountincredit",Math.round( Double.parseDouble(newCreditAmountYettobePaid)));

        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_addCreditOrdersTransactionDetailsTable,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    String message = response.getString("message");
                    isAddCreditOrdersTransactionDetailsIsCalled =false;
                    Adjusting_Widgets_Visibility(false);

                    if (message.equals("success")) {

                    }
                    else{

                    }
                } catch (JSONException e) {
                    isAddCreditOrdersTransactionDetailsIsCalled =false;
                    Adjusting_Widgets_Visibility(false);

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                isAddCreditOrdersTransactionDetailsIsCalled =false;
                Adjusting_Widgets_Visibility(false);
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

        Volley.newRequestQueue(CreditOrders_MobileNumberwise_Report.this).add(jsonObjectRequest);




    }

    public  void adjustFontScale( Configuration configuration) {

        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);

    }
    private void generateCreditOrdersPDFReport() {


        if (SDK_INT >= Build.VERSION_CODES.R) {

            if(Environment.isExternalStorageManager()){
                try {
                    exportReport();

                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                }
            }
            else{
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }

        } else {


            int writeExternalStoragePermission = ContextCompat.checkSelfPermission(CreditOrders_MobileNumberwise_Report.this, WRITE_EXTERNAL_STORAGE);
            //Log.d("ExportInvoiceActivity", "writeExternalStoragePermission "+writeExternalStoragePermission);
            // If do not grant write external storage permission.
            if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
                // Request user to grant write external storage permission.
                ActivityCompat.requestPermissions(CreditOrders_MobileNumberwise_Report.this, new String[]{WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
            } else {
                Adjusting_Widgets_Visibility(true);
                try {
                    exportReport();

                } catch (Exception e) {
                    e.printStackTrace();
                    ;
                }
            }
        }



        }

    private void FetchCreditOrdersDetails() {
        modal_creditOrderDetails = new Modal_CreditOrderDetails();
        creditOrdersTransactionDetails_Array.clear();
        Adjusting_Widgets_Visibility(true);
        try {
            encodeMobileno = URLEncoder.encode(encodeMobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Adjusting_Widgets_Visibility(false);

        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetCreditOrdersUsingMobilenoWithVendorkey +"?usermobileno="+encodeMobileno+"&vendorkey="+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        modal_creditOrderDetails = new Modal_CreditOrderDetails();


                        try {

                            Log.d(Constants.TAG, " response: " + response);
                            try {
                                String jsonString =response.toString();
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray JArray  = jsonObject.getJSONArray("content");
                                int i1=0;
                                int arrayLength = JArray.length();

                                if(arrayLength<=0){
                                    Adjusting_Widgets_Visibility(false);
                                    return;
                                }
                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        try {
                                            if(json.has("totalamountincredit")) {
                                                modal_creditOrderDetails.setTotalamountincredityettobepaid(json.getString("totalamountincredit"));
                                            }
                                            else{
                                                modal_creditOrderDetails.setTotalamountincredityettobepaid("0");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no totalamountincredit", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrderDetails.setTotalamountincredityettobepaid("0");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get totalamountincredit", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("lastupdatedtime")) {
                                                modal_creditOrderDetails.setLastupdatedtime(json.getString("lastupdatedtime"));
                                            }
                                            else{
                                                modal_creditOrderDetails.setLastupdatedtime("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no lastupdatedtime", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrderDetails.setLastupdatedtime("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get lastupdatedtime", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }




                                        try {
                                            if(json.has("usermobileno")) {
                                                modal_creditOrderDetails.setUsermobileno(json.getString("usermobileno"));
                                            }
                                            else{
                                                modal_creditOrderDetails.setUsermobileno("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no usermobileno", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrderDetails.setUsermobileno("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get usermobileno", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("vendorkey")) {
                                                modal_creditOrderDetails.setVendorkey(json.getString("vendorkey"));
                                            }
                                            else{
                                                modal_creditOrderDetails.setVendorkey("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no vendorkey", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrderDetails.setVendorkey("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get vendorkey", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }



                                    } catch (Exception e) {
                                        Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                    if(arrayLength-i1 == 1){
                                        FetchCreditOrdersTransactionDetails();
                                    }
                                }
                            } catch (Exception e) {
                                Adjusting_Widgets_Visibility(false);
                                DisplayData();

                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            DisplayData();

                            Adjusting_Widgets_Visibility(false);
                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);


                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();

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
        Volley.newRequestQueue(CreditOrders_MobileNumberwise_Report.this).add(jsonObjectRequest);


    }

    private void FetchCreditOrdersTransactionDetails() {


        Adjusting_Widgets_Visibility(true);





        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetCreditOrdersTransactionDetailsUsingMobilenoWithVendorkey +"?usermobileno="+encodeMobileno+"&vendorkey="+vendorKey, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {


                        try {

                            Log.d(Constants.TAG, " response: " + response);
                            try {
                                String jsonString =response.toString();
                                JSONObject jsonObject = new JSONObject(jsonString);
                                JSONArray JArray  = jsonObject.getJSONArray("content");
                                int i1=0;
                                int arrayLength = JArray.length();


                                for(;i1<(arrayLength);i1++) {
                                    Modal_CreditOrdersTransactionDetails   modal_creditOrdersTransactionDetails = new Modal_CreditOrdersTransactionDetails();

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        try {
                                            if(json.has("key")) {
                                                modal_creditOrdersTransactionDetails.setKey(json.getString("key"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setKey("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no Key", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setKey("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get Key", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("newamountincredit")) {
                                                modal_creditOrdersTransactionDetails.setNewamountincredit(json.getString("newamountincredit"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setNewamountincredit("0");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no newamountincredit", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setNewamountincredit("0");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get newamountincredit", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("oldamountincredit")) {
                                                modal_creditOrdersTransactionDetails.setOldamountincredit(json.getString("oldamountincredit"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setOldamountincredit("0");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no oldamountincredit", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setOldamountincredit("0");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get oldamountincredit", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("orderid")) {
                                                modal_creditOrdersTransactionDetails.setOrderid(json.getString("orderid"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setOrderid("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no orderid", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setOrderid("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get orderid", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("transactiontime")) {
                                                modal_creditOrdersTransactionDetails.setTransactiontime(json.getString("transactiontime"));
                                                modal_creditOrdersTransactionDetails.setTransactiontimelong(getLongValuefortheDate(json.getString("transactiontime")));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setTransactiontime("-");
                                                modal_creditOrdersTransactionDetails.setTransactiontimelong("0");

                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no transactiontime", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setTransactiontime("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get transactiontime", Toast.LENGTH_LONG).show();
                                            modal_creditOrdersTransactionDetails.setTransactiontimelong("0");
                                            e.printStackTrace();
                                        }



                                        try {
                                            if(json.has("transactiontype")) {
                                                transactionType = json.getString("transactiontype");
                                                modal_creditOrdersTransactionDetails.setTransactiontype(json.getString("transactiontype"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setTransactiontype("-");
                                                transactionType ="-";
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no transactiontype", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setTransactiontype("-");
                                            transactionType ="-";
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get transactiontype", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }
                                        try {
                                            if(json.has("transactionvalue")) {
                                                String transactionValue = (json.getString("transactionvalue").toString());
                                                transactionValue = transactionValue.replaceAll("[^\\d.]", "");

                                                if(transactionType.toUpperCase().equals(Constants.CREDIT_AMOUNT_ADDED)){
                                                    String addedAmount = modal_creditOrderDetails.getTotalAddedAmount().toString();
                                                    addedAmount = addedAmount.replaceAll("[^\\d.]", "");
                                                    double addedAmountDouble = 0;
                                                    double transactionValueDouble =0;

                                                    if((!addedAmount.equals("")) &&(!addedAmount.equals("null"))){
                                                        addedAmountDouble = 0;
                                                        try{
                                                            addedAmountDouble = Double.parseDouble(addedAmount);

                                                        }
                                                        catch (Exception e){
                                                            addedAmountDouble = 0;
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                    else {
                                                        addedAmountDouble = 0;

                                                    }

                                                    if((!transactionValue.equals("")) &&(!transactionValue.equals("null"))) {
                                                        transactionValueDouble =0;
                                                        try {
                                                            transactionValueDouble = Double.parseDouble(transactionValue);

                                                        } catch (Exception e) {
                                                            transactionValueDouble = 0;
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    else{
                                                        transactionValueDouble = 0;

                                                    }

                                                    try{
                                                        addedAmountDouble = addedAmountDouble + transactionValueDouble;
                                                        modal_creditOrderDetails.setTotalAmountGivenAsCredit(String.valueOf(addedAmountDouble));

                                                        modal_creditOrderDetails.setTotalAddedAmount(String.valueOf(addedAmountDouble));
                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else  if(transactionType.toUpperCase().equals(Constants.CREDIT_AMOUNT_PAID)){
                                                    String paidAmount = modal_creditOrderDetails.getTotalPaidAmount().toString();
                                                    paidAmount = paidAmount.replaceAll("[^\\d.]", "");
                                                    double paidAmountDouble = 0;
                                                    double transactionValueDouble =0;

                                                    if((!paidAmount.equals("")) &&(!paidAmount.equals("null"))){
                                                        paidAmountDouble = 0;
                                                        try{
                                                            paidAmountDouble = Double.parseDouble(paidAmount);

                                                        }
                                                        catch (Exception e){
                                                            paidAmountDouble = 0;
                                                            e.printStackTrace();
                                                        }


                                                    }
                                                    else {
                                                        paidAmountDouble = 0;

                                                    }

                                                    if((!transactionValue.equals("")) &&(!transactionValue.equals("null"))) {
                                                        transactionValueDouble =0;
                                                        try {
                                                            transactionValueDouble = Double.parseDouble(transactionValue);

                                                        } catch (Exception e) {
                                                            transactionValueDouble = 0;
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    else{
                                                        transactionValueDouble = 0;

                                                    }

                                                    try{
                                                        paidAmountDouble = paidAmountDouble + transactionValueDouble;

                                                        modal_creditOrderDetails.setTotalPaidAmount(String.valueOf(paidAmountDouble));
                                                    }
                                                    catch (Exception e){
                                                        e.printStackTrace();
                                                    }

                                                }
                                                modal_creditOrdersTransactionDetails.setTransactionvalue(json.getString("transactionvalue"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setTransactionvalue("0");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no transactionvalue", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setTransactionvalue("0");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get transactionvalue", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("usermobileno")) {
                                                modal_creditOrdersTransactionDetails.setUsermobileno(json.getString("usermobileno"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setUsermobileno("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no usermobileno", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setUsermobileno("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get usermobileno", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("vendorkey")) {
                                                modal_creditOrdersTransactionDetails.setVendorkey(json.getString("vendorkey"));
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setVendorkey("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no vendorkey", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setVendorkey("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get vendorkey", Toast.LENGTH_LONG).show();

                                            e.printStackTrace();
                                        }

                                        creditOrdersTransactionDetails_Array.add(modal_creditOrdersTransactionDetails);
                                    } catch (Exception e) {
                                        Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                    if(arrayLength-i1 == 1){
                                        DisplayData();
                                    }
                                }
                            } catch (Exception e) {
                                Adjusting_Widgets_Visibility(false);
                                DisplayData();

                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Adjusting_Widgets_Visibility(false);
                            DisplayData();

                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Adjusting_Widgets_Visibility(false);
                DisplayData();

                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();

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
        Volley.newRequestQueue(CreditOrders_MobileNumberwise_Report.this).add(jsonObjectRequest);




    }

    private void DisplayData() {

        if(creditOrdersTransactionDetails_Array.size()>0) {
            Adjusting_Widgets_Visibility(true);

        cardView_parentLayout.setVisibility(View.VISIBLE);

        try{
            usermobileno_text_widget.setText(modal_creditOrderDetails.getUsermobileno().toString());
        }
        catch (Exception e){
            usermobileno_text_widget.setText("---");
            e.printStackTrace();
        }

        try{
            lastupdatedtime_textwidget.setText(modal_creditOrderDetails.getLastupdatedtime().toString());
        }
        catch (Exception e){
            lastupdatedtime_textwidget.setText("---");
            e.printStackTrace();
        }



        try{
            creditAmountPaidtill_text_widget.setText(modal_creditOrderDetails.getTotalPaidAmount()+" Rs");
        }
        catch (Exception e){
            creditAmountPaidtill_text_widget.setText("---");
            e.printStackTrace();
        }

        try{
            String totalAmountPaid = "0",totalAmountAdded = "0";
            String  totalCreditAmountYettobePaid = "0";
            double totalAmountPaid_double = 0;
            double totalAmountAdded_double = 0;
            double totalCreditAmountYettobePaid_double = 0;
            double totalAmountGivenAsCredit =0;
            try{

                totalAmountPaid = modal_creditOrderDetails.getTotalPaidAmount().toString();
                totalAmountPaid = totalAmountPaid.replaceAll("[^\\d.]", "");
            }
            catch (Exception e){
                totalAmountPaid ="0";
                e.printStackTrace();
            }

            try{
                totalAmountAdded = modal_creditOrderDetails.getTotalAddedAmount().toString();
                totalAmountAdded = totalAmountAdded.replaceAll("[^\\d.]", "");

            }
            catch (Exception e){
                totalAmountAdded ="0";
                e.printStackTrace();
            }
            try{

                totalCreditAmountYettobePaid = modal_creditOrderDetails.getTotalamountincredityettobepaid().toString();
                totalCreditAmountYettobePaid = totalCreditAmountYettobePaid.replaceAll("[^\\d.]", "");

            }
            catch (Exception e){
                totalCreditAmountYettobePaid ="0";
                e.printStackTrace();
            }



            try {
                totalAmountPaid_double = Double.parseDouble(totalAmountPaid);
            }
            catch (Exception e){
                totalAmountPaid_double = 0;
                e.printStackTrace();
            }

            try {
                totalAmountAdded_double = Double.parseDouble(totalAmountAdded);
            }
            catch (Exception e){
                totalAmountAdded_double = 0;
                e.printStackTrace();
            }

            try {
                totalCreditAmountYettobePaid_double = Double.parseDouble(totalCreditAmountYettobePaid);
            }
            catch (Exception e){
                totalCreditAmountYettobePaid_double = 0;
                e.printStackTrace();
            }




            totalAmountGivenAsCredit =  totalCreditAmountYettobePaid_double + totalAmountPaid_double;


            try{
                creditAmountYetTobePaid_text_widget .setText(String.valueOf(totalCreditAmountYettobePaid_double)+" Rs");
            }
            catch (Exception e){
                e.printStackTrace();
            }
                if(totalAmountGivenAsCredit!=totalAmountAdded_double){
                    Toast.makeText(this, String.valueOf(totalAmountGivenAsCredit)+" , "+ String.valueOf(totalAmountAdded_double), Toast.LENGTH_SHORT).show();
                }
            try{
                totalCreditValue_text_widget.setText(String.valueOf(totalAmountGivenAsCredit)+" Rs");
            }
            catch (Exception e){
                totalCreditValue_text_widget.setText("---");
                e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

            Collections.sort(creditOrdersTransactionDetails_Array, new Comparator<Modal_CreditOrdersTransactionDetails>() {
                public int compare(final Modal_CreditOrdersTransactionDetails object1, final Modal_CreditOrdersTransactionDetails object2) {
                    return object1.getTransactiontimelong().compareTo(object2.getTransactiontimelong());
                }
            });


            Adjusting_Widgets_Visibility(false);

        }
        else{
            Adjusting_Widgets_Visibility(false);
            cardView_parentLayout.setVisibility(View.GONE);

        }







    }

    public String getLongValuefortheDate(String orderplacedtime) {
        String longvalue = "";
        try {
            String time1 = orderplacedtime;
            //   Log.d(TAG, "time1long  "+orderplacedtime);

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy",Locale.ENGLISH);
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

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

    public String getDate_and_time()
    {

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => Sat, 9 Jan 2021 13:12:24 " + c);

        SimpleDateFormat day = new SimpleDateFormat("EEE",Locale.ENGLISH);
        day.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String  CurrentDay = day.format(c);

        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy",Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));



        String CurrentDatee = df.format(c);
        String CurrentDate = CurrentDay+", "+CurrentDatee;


        SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm:ss",Locale.ENGLISH);
        dfTime.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));


        String FormattedTime = dfTime.format(c);
        String  formattedDate = CurrentDay+", "+CurrentDatee+" "+FormattedTime;
        return formattedDate;
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
    public void exportReport() {
        if ((creditOrdersTransactionDetails_Array == null) || (creditOrdersTransactionDetails_Array.size() <= 0)) {
            return;
        }
        String extstoragedir = Environment.getExternalStorageDirectory().toString();
        String state = Environment.getExternalStorageState();
        //Log.d("PdfUtil", "external storage state " + state + " extstoragedir " + extstoragedir);
        String  path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/TMCPartner/Credit Orders Transaction Report /";

        //  File fol = new File(extstoragedir, path);
        File folder = new File(path);
        if (!folder.exists()) {
            boolean bool = folder.mkdirs();
        }
        try {
            String filename = "Credit Orders Transaction Report_" + System.currentTimeMillis() + ".pdf";
            final File file = new File(folder, filename);
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);

            Document layoutDocument = new Document();
            PdfWriter.getInstance(layoutDocument, fOut);
            layoutDocument.open();

            addVendorDetails(layoutDocument);
            addItemRows(layoutDocument);

            layoutDocument.close();
            Adjusting_Widgets_Visibility(false);

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
            pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                Adjusting_Widgets_Visibility(false);

                startActivityForResult(intent, OPENPDF_ACTIVITY_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
            // }
        } catch (IOException e) {
            Adjusting_Widgets_Visibility(false);

            //Log.i("error", e.getLocalizedMessage());
        } catch (Exception ex) {
            Adjusting_Widgets_Visibility(false);

            ex.printStackTrace();
        }
    }

    private void addVendorDetails(Document layoutDocument) {
        try {
            SharedPreferences sharedPreferences
                    = getSharedPreferences("VendorLoginData",
                    MODE_PRIVATE);

            String Vendorname = sharedPreferences.getString("VendorName", "");

            com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 22, Font.BOLDITALIC);
            com.itextpdf.text.Paragraph titlepara = new com.itextpdf.text.Paragraph("Credit Orders Transaction Report");
            titlepara.setSpacingBefore(5);
            titlepara.setFont(boldFont);
            titlepara.setAlignment(Element.ALIGN_CENTER);
            layoutDocument.add(titlepara);

            String vendorname = "Vendor: " + Vendorname;
            com.itextpdf.text.Paragraph vendorpara = new com.itextpdf.text.Paragraph(vendorname);
            vendorpara.setSpacingBefore(20);
            vendorpara.setAlignment(Element.ALIGN_LEFT);
            layoutDocument.add(vendorpara);

            com.itextpdf.text.Paragraph datepara = new com.itextpdf.text.Paragraph("Date: " + getDate_and_time());
            datepara.setAlignment(Element.ALIGN_LEFT);
            datepara.setSpacingBefore(5);
            datepara.setSpacingAfter(10);
            layoutDocument.add(datepara);

            com.itextpdf.text.Paragraph mobilenoCell = new com.itextpdf.text.Paragraph("Customer Mobile No : " + mobileno);
            mobilenoCell.setAlignment(Element.ALIGN_LEFT);
            mobilenoCell.setSpacingBefore(5);
            mobilenoCell.setSpacingAfter(20);
            layoutDocument.add(mobilenoCell);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addItemRows(Document layoutDocument) {
        try {
            String rsunit = "Rs.", tmcprice;
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
/*
            PdfPCell mobilenocell = new PdfPCell(new Phrase("Mobile no"));
            mobilenocell.setBorder(Rectangle.NO_BORDER);
            mobilenocell.setBackgroundColor(BaseColor.GRAY);
            mobilenocell.setHorizontalAlignment(Element.ALIGN_LEFT);
            mobilenocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            mobilenocell.setPaddingLeft(10);
            mobilenocell.setFixedHeight(30);
            table.addCell(mobilenocell);


 */

            PdfPCell dateCell = new PdfPCell(new Phrase("Transaction Date"));
            dateCell.setBorder(Rectangle.NO_BORDER);
            dateCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            dateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            dateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            dateCell.setFixedHeight(30);
            dateCell.setBorderWidthLeft(01);
            dateCell.setBorderWidthTop(01);
            dateCell.setBorderWidthBottom(01);
            dateCell.setBorderWidthRight(01);

            dateCell.setPaddingRight(10);
            table.addCell(dateCell);

            PdfPCell typecell = new PdfPCell(new Phrase("Transaction Type"));
            typecell.setBorder(Rectangle.NO_BORDER);
            typecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            typecell.setHorizontalAlignment(Element.ALIGN_CENTER);
            typecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            typecell.setFixedHeight(30);
            typecell.setBorderWidthTop(01);
            typecell.setBorderWidthBottom(01);
            typecell.setBorderWidthRight(01);
            table.addCell(typecell);

            PdfPCell pricecell = new PdfPCell(new Phrase("Amount"));
            pricecell.setBorder(Rectangle.NO_BORDER);
            pricecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pricecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            pricecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            pricecell.setFixedHeight(30);

            pricecell.setPaddingRight(10);
            pricecell.setBorderWidthTop(01);
            pricecell.setBorderWidthBottom(01);
            pricecell.setBorderWidthRight(01);

            table.addCell(pricecell);




            PdfPCell oldcreditAmountCell = new PdfPCell(new Phrase("Old Amount Before Transaction "));
            oldcreditAmountCell.setBorder(Rectangle.NO_BORDER);
            oldcreditAmountCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            oldcreditAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            oldcreditAmountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            oldcreditAmountCell.setFixedHeight(30);
            oldcreditAmountCell.setPaddingRight(10);
            oldcreditAmountCell.setBorderWidthTop(01);
            oldcreditAmountCell.setBorderWidthBottom(01);
            oldcreditAmountCell.setBorderWidthRight(01);
            table.addCell(oldcreditAmountCell);


            PdfPCell newcreditAmountCell = new PdfPCell(new Phrase("New Amount After Transaction "));
            newcreditAmountCell.setBorder(Rectangle.NO_BORDER);
            newcreditAmountCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            newcreditAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            newcreditAmountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            newcreditAmountCell.setFixedHeight(30);
            newcreditAmountCell.setPaddingRight(10);
            newcreditAmountCell.setBorderWidthTop(01);
            newcreditAmountCell.setBorderWidthBottom(01);
            newcreditAmountCell.setBorderWidthRight(01);
            table.addCell(newcreditAmountCell);


            PdfPCell itemmobilenocell = null;
            PdfPCell itemtypecell = null;
            PdfPCell itemamountcell = null;
            PdfPCell itemdateCell = null;
            PdfPCell itemoldcreditAmountCell = null;
            PdfPCell itemnewcreditAmountCell = null;



                for (int j = 0; j < creditOrdersTransactionDetails_Array.size(); j++) {
                    Modal_CreditOrdersTransactionDetails itemRow = creditOrdersTransactionDetails_Array.get(j);
                    String key =  itemRow.getKey();
                    String orderid_array = "",mobileno_array="",transactionType_array = "",transactionAmount_array = "",transactionDate_array="",oldCreditAmount_array = "", newCreditAmount_array= "";
                    try {
                        orderid_array = itemRow.getOrderid();
                    } catch (Exception e) {
                        e.printStackTrace();


                    }

                    try {
                        transactionDate_array = itemRow.getTransactiontime();
                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                    try {
                        mobileno_array = itemRow.getUsermobileno();
                    } catch (Exception e) {
                        e.printStackTrace();


                    }
                    try {
                        transactionType_array = itemRow.getTransactiontype();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }




                    try {
                        transactionAmount_array = itemRow.getTransactionvalue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        oldCreditAmount_array = itemRow.getOldamountincredit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        newCreditAmount_array = itemRow.getNewamountincredit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

/*
                    itemmobilenocell = new PdfPCell(new Phrase(mobileno_array));
                    itemmobilenocell.setBorder(Rectangle.BOTTOM);
                    itemmobilenocell.setBorderColor(BaseColor.LIGHT_GRAY);
                    itemmobilenocell.setMinimumHeight(30);
                    itemmobilenocell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    itemmobilenocell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemmobilenocell.setPaddingRight(10);
                    table.addCell(itemmobilenocell);

 */
                    itemdateCell = new PdfPCell(new Phrase(transactionDate_array));
                    itemdateCell.setBorder(Rectangle.NO_BORDER);
                    itemdateCell.setBorderColor(BaseColor.GRAY);
                    itemdateCell.setMinimumHeight(30);
                    itemdateCell.setBorderWidthRight(01);
                    itemdateCell.setBorderWidthBottom(01);

                    itemdateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    itemdateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemdateCell.setPaddingRight(10);
                    table.addCell(itemdateCell);

                    if(transactionType_array.equals(Constants.CREDIT_AMOUNT_ADDED)){
                        transactionType_array = "Order Placed";
                        itemtypecell = new PdfPCell(new Phrase(transactionType_array));
                        itemtypecell.setBorderColor(BaseColor.RED);
                       // itemtypecell.setBackgroundColor(BaseColor.GREEN);
                        itemtypecell.setMinimumHeight(30);
                        itemtypecell.setBorderWidthRight(01);
                        itemtypecell.setBorderWidthBottom(01);
                        itemtypecell.setBorderWidthTop(01);
                        itemtypecell.setBorderWidthLeft(01);
                        itemtypecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        itemtypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        itemtypecell.setPaddingRight(10);
                        table.addCell(itemtypecell);
                    }
                    else if (transactionType_array.equals(Constants.CREDIT_AMOUNT_PAID)){
                        transactionType_array = "Amount Paid";
                        itemtypecell = new PdfPCell(new Phrase(transactionType_array));
                        itemtypecell.setBorder(Rectangle.NO_BORDER);
                        itemtypecell.setBorderColor(BaseColor.GRAY);
                        itemtypecell.setMinimumHeight(30);
                        itemtypecell.setBorderWidthRight(01);
                        itemtypecell.setBorderWidthBottom(01);
                        itemtypecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        itemtypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        itemtypecell.setPaddingRight(10);
                        table.addCell(itemtypecell);
                    }
                    else{
                        transactionType_array ="-";
                        itemtypecell = new PdfPCell(new Phrase(transactionType_array));
                        itemtypecell.setBorder(Rectangle.NO_BORDER);
                        itemtypecell.setBorderColor(BaseColor.GRAY);
                        itemtypecell.setMinimumHeight(30);
                        itemtypecell.setBorderWidthRight(01);
                        itemtypecell.setBorderWidthBottom(01);

                        itemtypecell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        itemtypecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        itemtypecell.setPaddingRight(10);
                        table.addCell(itemtypecell);
                    }



                    itemamountcell = new PdfPCell(new Phrase(transactionAmount_array+" Rs"));
                    itemamountcell.setBorder(Rectangle.NO_BORDER);
                    itemamountcell.setBorderColor(BaseColor.GRAY);
                    itemamountcell.setMinimumHeight(30);
                    itemamountcell.setBorderWidthRight(01);
                    itemamountcell.setBorderWidthBottom(01);

                    itemamountcell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    itemamountcell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemamountcell.setPaddingRight(10);
                    table.addCell(itemamountcell);





                    itemoldcreditAmountCell = new PdfPCell(new Phrase(oldCreditAmount_array+" Rs"));
                    itemoldcreditAmountCell.setBorder(Rectangle.NO_BORDER);
                    itemoldcreditAmountCell.setBorderColor(BaseColor.GRAY);
                    itemoldcreditAmountCell.setMinimumHeight(30);
                    itemoldcreditAmountCell.setBorderWidthRight(01);
                    itemoldcreditAmountCell.setBorderWidthBottom(01);

                    itemoldcreditAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    itemoldcreditAmountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemoldcreditAmountCell.setPaddingRight(10);
                    table.addCell(itemoldcreditAmountCell);


                    itemnewcreditAmountCell = new PdfPCell(new Phrase(newCreditAmount_array+" Rs"));
                    itemnewcreditAmountCell.setBorder(Rectangle.NO_BORDER);
                    itemnewcreditAmountCell.setBorderColor(BaseColor.GRAY);
                    itemnewcreditAmountCell.setMinimumHeight(30);
                    itemnewcreditAmountCell.setBorderWidthBottom(01);
                    itemnewcreditAmountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    itemnewcreditAmountCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    itemnewcreditAmountCell.setPaddingRight(10);
                    table.addCell(itemnewcreditAmountCell);





                }



            //}
            layoutDocument.add(table);
            PdfPTable table1 = new PdfPTable(2);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(10);
            PdfPCell emptycell;
            PdfPCell totalBillAmount;
            PdfPCell totalBillAmountvaluecell;

            PdfPCell amountpaidtillnowcell;
            PdfPCell amountpaidtillnowvaluecell;
            PdfPCell amountpaidYettoPaycell;
            PdfPCell amountpaidYettoPayvaluecell;




            totalBillAmount = new PdfPCell(new Phrase(" Total Bill Amount For Credit Orders"));
            totalBillAmount.setBorderColor(BaseColor.LIGHT_GRAY);
            totalBillAmount.setBorder(Rectangle.NO_BORDER);
            totalBillAmount.setMinimumHeight(25);
            totalBillAmount.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalBillAmount.setVerticalAlignment(Element.ALIGN_LEFT);
            table1.addCell(totalBillAmount);

            totalBillAmountvaluecell = new PdfPCell(new Phrase(":   Rs. " + modal_creditOrderDetails.getTotalAmountGivenAsCredit()));
            totalBillAmountvaluecell.setBorder(Rectangle.NO_BORDER);
            totalBillAmountvaluecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalBillAmountvaluecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalBillAmountvaluecell.setFixedHeight(25);
            table1.addCell(totalBillAmountvaluecell);





            totalBillAmount = new PdfPCell(new Phrase("          Total Amount Paid Till Now "));
            totalBillAmount.setBorderColor(BaseColor.LIGHT_GRAY);
            totalBillAmount.setBorder(Rectangle.NO_BORDER);
            totalBillAmount.setMinimumHeight(25);
            totalBillAmount.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalBillAmount.setVerticalAlignment(Element.ALIGN_LEFT);
            table1.addCell(totalBillAmount);

            totalBillAmountvaluecell = new PdfPCell(new Phrase(":   Rs. " + modal_creditOrderDetails.getTotalPaidAmount()));
            totalBillAmountvaluecell.setBorder(Rectangle.NO_BORDER);
            totalBillAmountvaluecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalBillAmountvaluecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalBillAmountvaluecell.setFixedHeight(25);
            table1.addCell(totalBillAmountvaluecell);





            totalBillAmount = new PdfPCell(new Phrase("          Total Amount Yet to  PAY "));
            totalBillAmount.setBorderColor(BaseColor.LIGHT_GRAY);
            totalBillAmount.setBorder(Rectangle.NO_BORDER);
            totalBillAmount.setMinimumHeight(25);
            totalBillAmount.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalBillAmount.setVerticalAlignment(Element.ALIGN_LEFT);
            table1.addCell(totalBillAmount);


            totalBillAmountvaluecell = new PdfPCell(new Phrase(":   Rs. " +  modal_creditOrderDetails.getTotalamountincredityettobepaid()));
            totalBillAmountvaluecell.setBorder(Rectangle.NO_BORDER);
            totalBillAmountvaluecell.setHorizontalAlignment(Element.ALIGN_LEFT);
            totalBillAmountvaluecell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            totalBillAmountvaluecell.setFixedHeight(25);
            table1.addCell(totalBillAmountvaluecell);

          //  layoutDocument.add(table1);
            PdfPCell emptycellwithtable;
            PdfPTable emptytable = new PdfPTable(3);
            table1.setWidthPercentage(100);
            table1.setSpacingBefore(20);
            emptycellwithtable = new PdfPCell(new Phrase(""));
            emptycellwithtable.setBorder(Rectangle.NO_BORDER);
            emptycellwithtable.setHorizontalAlignment(Element.ALIGN_LEFT);
            emptycellwithtable.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emptycellwithtable.setFixedHeight(25);
            emptycellwithtable = new PdfPCell(new Phrase(""));
            emptycellwithtable.setBorder(Rectangle.NO_BORDER);
            emptycellwithtable.setHorizontalAlignment(Element.ALIGN_LEFT);
            emptycellwithtable.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emptycellwithtable.setFixedHeight(25);
            emptytable.addCell(emptycellwithtable);
            emptycellwithtable = new PdfPCell(new Phrase(""));
            emptycellwithtable.setBorder(Rectangle.NO_BORDER);
            emptycellwithtable.setHorizontalAlignment(Element.ALIGN_LEFT);
            emptycellwithtable.setVerticalAlignment(Element.ALIGN_MIDDLE);
            emptycellwithtable.setFixedHeight(25);
            emptytable.addCell(emptycellwithtable);
            emptytable.addCell(emptycellwithtable);
            layoutDocument.add(emptytable);

            try {
                RoundRectangle roundRectange = new RoundRectangle();
                PdfPTable outertable = new PdfPTable(1);
                PdfPCell  outercell = new PdfPCell(table1);
                outercell.setCellEvent(roundRectange);
                outercell.setHorizontalAlignment(Element.ALIGN_CENTER);
                outercell.setVerticalAlignment(Element.ALIGN_CENTER);
                outercell.setBorder(Rectangle.NO_BORDER);
                outercell.setPadding(1);
                outertable.addCell(outercell);


                layoutDocument.add(outertable);





            } catch (DocumentException e) {
                e.printStackTrace();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public class RoundRectangle implements PdfPCellEvent {
        public void cellLayout(PdfPCell cell, Rectangle rect,
                               PdfContentByte[] canvas) {
            PdfContentByte cb = canvas[PdfPTable.LINECANVAS];
            cb.roundRectangle(
                    rect.getLeft() + 1.5f, rect.getBottom() + 1.5f, rect.getWidth() - 3,
                    rect.getHeight() - 3, 4);
            cb.stroke();
        }
    }
}