package com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Mobile_Vendor_Selection_Screen extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    private EditText vendor_login_password_edittext;
    private String mobile_vendorNameString,UserRole;
    private String mobile_password;
    private String mobile_userPhoneNumber;
    private String mobile_vendorKey;
    private String mobile_vendorMobileNumber;
    private String mobile_vendorAddressline1,mobile_vendorAddressline2,mobile_vendorPincode;
    private String mobile_vendorStatus, mobile_vendorFssaino;
    private String mobile_vendorLatitude;
    private String mobile_vendorLongitude,newtoken="";
    private ArrayAdapter mobile_spinner_aAdapter;
    private Button mobile_vendorDetails_verification_button;
    private  Boolean mobile_vendorLogin = false;
    private  Boolean inventoryCheckBool = false;

    private Spinner mobile_vendor_selecting_spinner;
    private ArrayList<String> VendorName_arrayList;
    private JSONArray result;
    private LinearLayout loadingPanel_dailyItemWisereport,loadingpanelmask_dailyItemWisereport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile__vendor__selection__screen_activity);

        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        mobile_vendor_selecting_spinner = findViewById(R.id.pos_vendor_selection_widget);
        VendorName_arrayList = new ArrayList<String>();
        loadingPanel_dailyItemWisereport = findViewById(R.id.loadingPanel_dailyItemWisereport);
        loadingpanelmask_dailyItemWisereport = findViewById(R.id.loadingpanelmask_dailyItemWisereport);

        vendor_login_password_edittext = findViewById(R.id.pos_vendor_password_widget);
        mobile_vendorDetails_verification_button = findViewById(R.id.pos_vendor_verify_widget);
        getAreawiseVendorName();
        loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
        loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);

        SharedPreferences sh
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);


        mobile_userPhoneNumber = sh.getString("UserPhoneNumber","");
        mobile_vendor_selecting_spinner.setOnItemSelectedListener(this);
     //   checkAdminUserorNot(mobile_userPhoneNumber);
       /* mobile_vendor_selecting_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mobile_vendorNameString = mobile_vendor_selecting_spinner.getSelectedItem().toString();
                //Log.d(Constants.TAG, "VendorName: " + mobile_vendorNameString);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        */

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()) {
                    newtoken = task.getResult().getToken();
                    //Log.i("tag","token:  "+newtoken);
                }

            }
        });




        mobile_vendorDetails_verification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_password = vendor_login_password_edittext.getText().toString();
                if(mobile_password.length() !=0){
                    loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                    loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);
            //       String UserRole = checkAdminUserorNot(mobile_userPhoneNumber);

                    VerifyVendorPassword(newtoken);
                }
                else {
                    loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                    loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                    AlertDialogClass.showDialog(Mobile_Vendor_Selection_Screen.this,R.string.Enter_Password);

                }
            }
        });


    }




    private void getAreawiseVendorName() {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_getListofVendors +"?modulename=Store",null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {

                        //Log.d(Constants.TAG, "Response: " + response);
                        try {

                             result  = response.getJSONArray("content");
                            //Log.d(Constants.TAG, "Response: " + result);
                            int i1=0;
                            int arrayLength = result.length();
                            //Log.d("Constants.TAG", "Response: " + arrayLength);


                            for(;i1<=(arrayLength-1);i1++) {

                                try {
                                    JSONObject json = result.getJSONObject(i1);

                                    mobile_vendorNameString = String.valueOf(json.get("name"));
                                    mobile_vendorKey = String.valueOf(json.get("key"));
                                    //Log.d(Constants.TAG, "JsonName: " + mobile_vendorNameString);

                                    if (!VendorName_arrayList.contains(mobile_vendorNameString)) {
                                        VendorName_arrayList.add(mobile_vendorNameString);
                                        loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                        loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                    }

                                } catch (JSONException e) {
                                    loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                    loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                            loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                        }
                        mobile_spinner_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, VendorName_arrayList);
                        mobile_vendor_selecting_spinner.setAdapter(mobile_spinner_aAdapter);

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

    private void VerifyVendorPassword(String newtoken) {
        // String params = "?name="+VendorName+"&vendor_password="+password;
        //final String APIwithParameters = VerifyPasswordApi+params;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_verifyVendorPassword,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {

                try {
                    //Log.d(Constants.TAG, "response: " + response.get("body"));
                    String responseBody = String.valueOf(response.get("body"));

                    if(responseBody.equals("Success")){
                        mobile_vendorLogin = true;
                        UploadVendorUserDetailsInDB(newtoken);

                    }
                    else {
                        AlertDialogClass.showDialog(Mobile_Vendor_Selection_Screen.this,R.string.Enter_Correct_Password);
                        loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                        loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                    loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("vendorkey", mobile_vendorKey);
                params.put("vendor_password", mobile_password);

                return params;
            }


            @NonNull
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                params.put("vendorkey", mobile_vendorKey);
                params.put("vendor_password", mobile_password);

                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }




    private void UploadVendorUserDetailsInDB(String newtoken) {

        JSONObject  jsonObject = new JSONObject();
        try {
            jsonObject.put("vendorkey", mobile_vendorKey);
            jsonObject.put("Status", "Login");
            jsonObject.put("mobileno", mobile_userPhoneNumber);
            jsonObject.put("fcmtoken", newtoken);
            jsonObject.put("appname", "TMCPARTNERAPP");



        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.api_updateUserDetails,
                jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {
                //Log.d(Constants.TAG, "Response: " + response);

                try {
                    String statuscode=response.getString("statusCode");
                    if(statuscode .equals("200")) {
                         UserRole =response.getString("userRole");
                        if(UserRole.equals("")||UserRole.length()<1||UserRole.equals(null)){
                            Toast.makeText(Mobile_Vendor_Selection_Screen.this,"You Don't have any User Role Ask Admin to assign the Role",Toast.LENGTH_LONG).show();
                            loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                            loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);                        }
                        else {

                            saveVendorLoginStatus();

                            loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                            loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                            Intent i = new Intent(Mobile_Vendor_Selection_Screen.this, MobileScreen_Dashboard.class);
                            startActivity(i);
                            finish();
                        }
                    }
                    else{
                        String message = response.getString("message");
                        Toast.makeText(Mobile_Vendor_Selection_Screen.this,message,Toast.LENGTH_LONG).show();
                        loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                        loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);

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


        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(40000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Make the request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void saveVendorLoginStatus() {
        //Log.i(Constants.TAG,"VendorLoginStatus"+ mobile_vendorLogin);
        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();
        myEdit.putString(
                "userrole",
                UserRole);

        myEdit.putString(
                "userrole",
                UserRole);

        myEdit.putBoolean(
                "VendorLoginStatus",
                mobile_vendorLogin);
        myEdit.putString(
                "VendorKey",
                mobile_vendorKey);
        myEdit.putString(
                "VendorName",
                mobile_vendorNameString
        );
        myEdit.putString(
                "VendorAddressline1",
                mobile_vendorAddressline1
        );
        myEdit.putString(
                "VendorAddressline2",
                mobile_vendorAddressline2
        );
        myEdit.putString(
                "VendorPincode",
                mobile_vendorPincode
        );
        myEdit.putString(
                "VendorMobileNumber",
                mobile_vendorMobileNumber
        );

        myEdit.putString(
                "VendorFssaino",
                mobile_vendorFssaino
        );
        myEdit.putString(
                "VendorLatitude",
                mobile_vendorLatitude
        );


        myEdit.putString(
                "VendorLongitute",
                mobile_vendorLongitude
        );

        myEdit.putBoolean(
                "inventoryCheckBool",
                inventoryCheckBool
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        mobile_vendorNameString=getVendorData(position,"name");
        mobile_vendorKey=getVendorData(position,"key");
        mobile_vendorMobileNumber=getVendorData(position,"vendormobile");
        mobile_vendorAddressline1=getVendorData(position,"addressline1");
        mobile_vendorAddressline2=getVendorData(position,"addressline2");
        mobile_vendorPincode=getVendorData(position,"pincode");
        mobile_vendorStatus=getVendorData(position,"status");
        mobile_vendorFssaino =getVendorData(position,"vendorfssaino");
        mobile_vendorLatitude =getVendorData(position,"locationlat");
        mobile_vendorLongitude =getVendorData(position,"locationlong");
        try {
            inventoryCheckBool = Boolean.parseBoolean(getVendorData(position, "inventorycheckpos"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}