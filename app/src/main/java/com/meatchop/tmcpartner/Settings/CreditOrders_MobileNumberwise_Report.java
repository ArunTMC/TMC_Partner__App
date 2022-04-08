package com.meatchop.tmcpartner.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.Mobile_NewOrders.NewOrderScreenFragment_mobile;
import com.meatchop.tmcpartner.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreditOrders_MobileNumberwise_Report extends AppCompatActivity {
EditText  customermobileno_editwidget;
String mobileno = "";
LinearLayout loadingpanelmask, loadingPanel;
Button fetchCreditDetails_buttonWidget,generateCreditTranscReport_button_widget,viewCreditTransactionList_button_widget;
TextView vendorName_textWidget, usermobileno_text_widget,lastupdatedtime_textwidget,totalCreditValue_text_widget,creditAmountPaidtill_text_widget,creditAmountYetTobePaid_text_widget;
    String vendorKey="",usermobileNo ="";
    String StoreAddressLine1 = "No 57, Rajendra Prasad Road,";
    String StoreAddressLine2 = "Hasthinapuram Chromepet";
    String StoreAddressLine3 = "Chennai - 600044";
    String StoreLanLine = "PH No :4445568499";
    String transactionType ="";
    Modal_CreditOrderDetails modal_creditOrderDetails ;

    List<Modal_CreditOrdersTransactionDetails> creditOrdersTransactionDetails_Array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_orders__mobile_numberwise__report);
        vendorName_textWidget = findViewById(R.id.vendorName_textWidget);
        fetchCreditDetails_buttonWidget = findViewById(R.id.fetchCreditDetails_buttonWidget);
        customermobileno_editwidget = findViewById(R.id.customermobileno_editwidget);
        usermobileno_text_widget = findViewById(R.id.usermobileno_text_widget);
        lastupdatedtime_textwidget = findViewById(R.id.lastupdatedtime_textwidget);
        totalCreditValue_text_widget = findViewById(R.id.totalCreditValue_text_widget);
        creditAmountPaidtill_text_widget = findViewById(R.id.creditAmountPaidtill_text_widget);
        creditAmountYetTobePaid_text_widget = findViewById(R.id.creditAmountYetTobePaid_text_widget);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);

        generateCreditTranscReport_button_widget = findViewById(R.id.generateCreditTranscReport_button_widget);
        viewCreditTransactionList_button_widget = findViewById(R.id.viewCreditTransactionList_button_widget);

        try{
            SharedPreferences shared = getSharedPreferences("VendorLoginData", MODE_PRIVATE);
            vendorKey = shared.getString("VendorKey","");
            usermobileNo = (shared.getString("UserPhoneNumber", "+91"));
            StoreAddressLine1 = (shared.getString("VendorAddressline1", ""));
            StoreAddressLine2 = (shared.getString("VendorAddressline2", ""));
            StoreAddressLine3 = (shared.getString("VendorPincode", ""));
            StoreLanLine = (shared.getString("VendorMobileNumber", ""));

        }
        catch(Exception e){
            e.printStackTrace();
        }
        modal_creditOrderDetails = new Modal_CreditOrderDetails();

        fetchCreditDetails_buttonWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    mobileno = "+91"+customermobileno_editwidget.getText().toString();
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
                generateCreditOrdersReport();
            }
        });


        viewCreditTransactionList_button_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();

                b.putSerializable("creditordertransactionlist", (Serializable) creditOrdersTransactionDetails_Array);
                Intent intent = new Intent(CreditOrders_MobileNumberwise_Report.this,CreditOrders_MobileNumberwiseTransactionScreen.class);
                intent.putExtras(b);
                startActivity(intent);



            }
        });




    }

    private void generateCreditOrdersReport() {
    }

    private void FetchCreditOrdersDetails() {
        Adjusting_Widgets_Visibility(true);
        try {
            mobileno = URLEncoder.encode(mobileno, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetCreditOrdersUsingMobilenoWithVendorkey +"?usermobileno="+mobileno+"&vendorkey="+vendorKey, null,
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


                                for(;i1<(arrayLength);i1++) {

                                    try {
                                        JSONObject json = JArray.getJSONObject(i1);
                                        try {
                                            if(json.has("totalamountincredit")) {
                                                modal_creditOrderDetails.setTotalamountincredit(json.getString("totalamountincredit"));
                                            }
                                            else{
                                                modal_creditOrderDetails.setTotalamountincredit("0");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no totalamountincredit", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrderDetails.setTotalamountincredit("0");
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

                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Adjusting_Widgets_Visibility(false);
                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


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





        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetCreditOrdersTransactionDetailsUsingMobilenoWithVendorkey +"?usermobileno="+mobileno+"&vendorkey="+vendorKey, null,
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
                                            }
                                            else{
                                                modal_creditOrdersTransactionDetails.setTransactiontime("-");
                                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"There is no transactiontime", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                        catch(Exception e){
                                            modal_creditOrdersTransactionDetails.setTransactiontime("-");
                                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get transactiontime", Toast.LENGTH_LONG).show();

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

                                Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Adjusting_Widgets_Visibility(false);

                            Toast.makeText(CreditOrders_MobileNumberwise_Report.this,"Can't get CreditOrder Details", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }


                    }

                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {


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
            totalCreditValue_text_widget.setText(modal_creditOrderDetails.getTotalamountincredit().toString());
        }
        catch (Exception e){
            totalCreditValue_text_widget.setText("---");
            e.printStackTrace();
        }

        try{
            creditAmountPaidtill_text_widget.setText(modal_creditOrderDetails.getTotalPaidAmount());
        }
        catch (Exception e){
            creditAmountPaidtill_text_widget.setText("---");
            e.printStackTrace();
        }

        try{
            String totalAmountPaid = "0";
            String  totalCreditAmount = "0";
            double totalAmountPaid_double = 0;
            double totalCreditAmount_double = 0;
            double amountShouldPay =0;

            totalAmountPaid = modal_creditOrderDetails.getTotalPaidAmount().toString();
            totalAmountPaid = totalAmountPaid.replaceAll("[^\\d.]", "");

            totalCreditAmount = modal_creditOrderDetails.getTotalamountincredit().toString();
            totalCreditAmount = totalCreditAmount.replaceAll("[^\\d.]", "");

            try {
                totalAmountPaid_double = Double.parseDouble(totalAmountPaid);
            }
            catch (Exception e){
                totalAmountPaid_double = 0;
                e.printStackTrace();
            }

            try {
                totalCreditAmount_double = Double.parseDouble(totalCreditAmount);
            }
            catch (Exception e){
                totalCreditAmount_double = 0;
                e.printStackTrace();
            }




            amountShouldPay =  totalCreditAmount_double - totalAmountPaid_double;


            try{
                creditAmountYetTobePaid_text_widget .setText(String.valueOf(amountShouldPay));
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

        Adjusting_Widgets_Visibility(false);





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


}