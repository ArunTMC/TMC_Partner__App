package com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.Mobile_Vendor_Selection_Screen;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.Pos_ManageOrderFragment;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pos_Vendor_Selection_Screen extends AppCompatActivity  {
    private EditText vendor_login_password_edittext;
    private String pos_vendorNameString,UserRole;
    private String pos_password;
    private String pos_userPhoneNumber;
    private String pos_vendorKey;
    private  ArrayAdapter pos_spinner_aAdapter;
    private  Button pos_vendorDetails_verification_button;
    private  Boolean pos_vendorLogin = false;
    private Spinner pos_vendor_selecting_spinner;
    private ArrayList<String> VendorName_arrayList;
    private JSONArray result;
    private String pos_vendorMobileNumber;
    private String pos_vendorAddressline1,pos_vendorAddressline2,pos_vendorPincode;
    private String pos_vendorStatus, pos_vendorFssaino,pos_vendorLatitude,pos_vendorLongitude;
    LinearLayout loadingPanel,loadingpanelmask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos__vendor__selection_activity);
        pos_vendor_selecting_spinner = findViewById(R.id.pos_vendor_selection_widget);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);

        vendor_login_password_edittext = findViewById(R.id.pos_vendor_password_widget);
        pos_vendorDetails_verification_button = findViewById(R.id.pos_vendor_verify_widget);
        VendorName_arrayList = new ArrayList<String>();
        getAreawiseVendorName();

        Adjusting_Widgets_Visibility(true);

        SharedPreferences sh
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);


        pos_userPhoneNumber = sh.getString("UserPhoneNumber","");

        pos_vendor_selecting_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                pos_vendorNameString=getVendorData(position,"name");
                pos_vendorKey=getVendorData(position,"key");
                pos_vendorMobileNumber=getVendorData(position,"vendormobile");
                pos_vendorAddressline1=getVendorData(position,"addressline1");
                pos_vendorAddressline2=getVendorData(position,"addressline2");
                pos_vendorPincode=getVendorData(position,"pincode");
                pos_vendorStatus=getVendorData(position,"status");
                pos_vendorFssaino =getVendorData(position,"vendorfssaino");

                pos_vendorLatitude =getVendorData(position,"locationlat");
                pos_vendorLongitude =getVendorData(position,"locationlong");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });









        pos_vendorDetails_verification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos_password = vendor_login_password_edittext.getText().toString();
                if(pos_password.length() !=0){
                    Adjusting_Widgets_Visibility(true);
                    VerifyPasswordandGetVendorKey();
                }
                else {
                    AlertDialogClass.showDialog(Pos_Vendor_Selection_Screen.this,R.string.Enter_Password);

                }
            }
        });


    }

    private void Adjusting_Widgets_Visibility(boolean show) {
        if(show) {
            loadingPanel.setVisibility(View.VISIBLE);
            loadingpanelmask.setVisibility(View.VISIBLE);
        }
        else{
            loadingPanel.setVisibility(View.GONE);
            loadingpanelmask.setVisibility(View.GONE);
        }

    }

    private void getAreawiseVendorName() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofVendors +"?modulename=Store",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "Response: " + response);
                        try {

                            result  = response.getJSONArray("content");
                            Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    pos_vendorNameString = String.valueOf(json.get("name"));
                                    Log.d(Constants.TAG, "JsonName: " + pos_vendorNameString);

                                    if (!VendorName_arrayList.contains(pos_vendorNameString)) {
                                        VendorName_arrayList.add(pos_vendorNameString);

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    Log.d(Constants.TAG, "e: " + e.getMessage());
                                    Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pos_spinner_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, VendorName_arrayList);
                        pos_vendor_selecting_spinner.setAdapter(pos_spinner_aAdapter);
                        Adjusting_Widgets_Visibility(false);

                    }

                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {

                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

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

    @Override
    protected void onResume() {
        super.onResume();

    }




    //Doing the same with this method as we did with getName()
    private String getVendorData(int position,String fieldName){
        String data="";
        try {
            JSONObject json = result.getJSONObject(position);
            data = json.getString(fieldName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }




    private void VerifyPasswordandGetVendorKey() {
        // String params = "?name="+VendorName+"&vendor_password="+password;
        //final String APIwithParameters = VerifyPasswordApi+params;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_verifyVendorPassword,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    Log.d(Constants.TAG, "response: " + response.get("body"));
                    String responseBody = String.valueOf(response.get("body"));

                    if(responseBody.equals("Success")){

                        pos_vendorLogin = true;
                        UploadVendorUserDetailsInDB();
                      //  Adjusting_Widgets_Visibility(false);

                    }
                    else {
                        Adjusting_Widgets_Visibility(false);

                        AlertDialogClass.showDialog(Pos_Vendor_Selection_Screen.this,R.string.Enter_Correct_Password);

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                Log.d(Constants.TAG, "Error: " + error.getMessage());
                Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", pos_vendorKey);
                params.put("vendor_password", pos_password);

                return params;
            }


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("vendorkey", pos_vendorKey);
                params.put("vendor_password", pos_password);

                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }




    private void UploadVendorUserDetailsInDB() {

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("vendorkey", pos_vendorKey);
            jsonObject.put("Status", "Login");
            jsonObject.put("mobileno", pos_userPhoneNumber);
            jsonObject.put("appname", "TMCPARTNERAPP");
            jsonObject.put("fcmtoken", "");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateUserDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                try {
                    String statuscode=response.getString("statusCode");
                    if(statuscode .equals("200")) {
                        UserRole =response.getString("userRole");
                        if(UserRole.equals("")||UserRole.length()<1||UserRole.equals(null)){
                            Toast.makeText(Pos_Vendor_Selection_Screen.this,"You Don't have any User Role Ask Admin to assign the Role",Toast.LENGTH_LONG).show();
                            Adjusting_Widgets_Visibility(false);
                        }
                        else {


                            Adjusting_Widgets_Visibility(false);
                            saveVendorLoginStatus();
                            Intent i = new Intent(Pos_Vendor_Selection_Screen.this, Pos_Dashboard_Screen.class);

                            startActivity(i);
                            finish();
                        }
                    }
                    else{
                        String message = response.getString("message");
                        Toast.makeText(Pos_Vendor_Selection_Screen.this,message,Toast.LENGTH_LONG).show();
                        Adjusting_Widgets_Visibility(false);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.d(Constants.TAG, "Response: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void saveVendorLoginStatus() {
        Log.i(Constants.TAG,"VendorLoginStatus"+ pos_vendorLogin);
        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "userrole",
                UserRole);
        myEdit.putBoolean(
                "VendorLoginStatus",
                pos_vendorLogin);
        myEdit.putString(
                "VendorKey",
                pos_vendorKey);
        myEdit.putString(
                "VendorName",
                pos_vendorNameString
        );
        myEdit.putString(
                "VendorAddressline1",
                pos_vendorAddressline1
        );
        myEdit.putString(
                "VendorAddressline2",
                pos_vendorAddressline2
        );
        myEdit.putString(
                "VendorPincode",
                pos_vendorPincode
        );
        myEdit.putString(
                "VendorMobileNumber",
                pos_vendorMobileNumber
        );

        myEdit.putString(
                "VendorFssaino",
                pos_vendorFssaino
        );
        myEdit.putString(
                "VendorLatitude",
                pos_vendorLatitude
        );


        myEdit.putString(
                "VendorLongitute",
                pos_vendorLongitude
        );

        myEdit.apply();

    }


    @Override
    public void onBackPressed() {
        new TMCAlertDialogClass(this, R.string.app_name, R.string.Exit_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        finish();
                    }

                    @Override
                    public void onNo() {

                    }
                });

    }







}