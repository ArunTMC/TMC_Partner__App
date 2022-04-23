package com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.google.gson.Gson;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.Mobile_LoginScreen;
import com.meatchop.tmcpartner.NukeSSLCerts;
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
    private String pos_vendorNameString,UserRole,pos_vendorKeyType;
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
    private String pos_vendorAddressline1,pos_vendorAddressline2,pos_vendorPincode,minimumscreensizeforpos;
    private String pos_vendorStatus, pos_vendorFssaino,pos_vendorLatitude,pos_vendorLongitude,defaultprintertype;
    LinearLayout loadingPanel,loadingpanelmask;
    private  Boolean inventoryCheckBool = false;
    double screenInches;

    List<Modal_vendor> vendorList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pos__vendor__selection_activity);
        pos_vendor_selecting_spinner = findViewById(R.id.pos_vendor_selection_widget);
        loadingpanelmask = findViewById(R.id.loadingpanelmask);
        loadingPanel = findViewById(R.id.loadingPanel);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();

        vendor_login_password_edittext = findViewById(R.id.pos_vendor_password_widget);
        pos_vendorDetails_verification_button = findViewById(R.id.pos_vendor_verify_widget);
        VendorName_arrayList = new ArrayList<String>();
        getAreawiseVendorName();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);
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
                pos_vendorKeyType =getVendorData(position,"vendortype");
                pos_vendorFssaino =getVendorData(position,"vendorfssaino");
                minimumscreensizeforpos = getVendorData(position,"minimumscreensizeforpos");
                defaultprintertype = getVendorData(position,"defaultprintertype");

                pos_vendorLatitude =getVendorData(position,"locationlat");
                pos_vendorLongitude =getVendorData(position,"locationlong");
                try {
                    inventoryCheckBool = Boolean.parseBoolean(getVendorData(position, "inventorycheckpos"));
                }
                catch (Exception e){
                    e.printStackTrace();
                }
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

                                    try{
                                        if(json.has("vendortype")) {
                                            pos_vendorKeyType = String.valueOf(json.get("vendortype")).toUpperCase();
                                        }
                                        else{
                                            pos_vendorKeyType = "";
                                        }
                                    }
                                    catch (Exception e){
                                        pos_vendorKeyType = "";
                                        e.printStackTrace();
                                    }
                                    if(!pos_vendorKeyType.toString().equals(Constants.Warehouse_VendorType)) {
                                        try {
                                            pos_vendorNameString = String.valueOf(json.get("name"));

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        //Log.d(Constants.TAG, "JsonName: " + pos_vendorNameString);

                                        if (!VendorName_arrayList.contains(pos_vendorNameString)) {
                                            VendorName_arrayList.add(pos_vendorNameString);

                                        }

                                        Modal_vendor modal_vendor = new Modal_vendor();


                                        try {

                                            if(json.has("name")){

                                                modal_vendor.setVendorname( String.valueOf(json.get("name")));

                                            }
                                            else{
                                                modal_vendor.setVendorname( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setVendorname( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {

                                            if(json.has("vendortype")){

                                                modal_vendor.setVendortype( String.valueOf(json.get("vendortype")));

                                            }
                                            else{
                                                modal_vendor.setVendortype( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setVendortype( String.valueOf(""));
                                            e.printStackTrace();
                                        }



                                        try {

                                            if(json.has("minimumscreensizeforpos")){

                                                modal_vendor.setMinimumscreensizeforpos( String.valueOf(json.get("minimumscreensizeforpos")));

                                            }
                                            else{
                                                modal_vendor.setMinimumscreensizeforpos( String.valueOf(Constants.default_mobileScreenSize));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setMinimumscreensizeforpos( String.valueOf(Constants.default_mobileScreenSize));
                                            e.printStackTrace();
                                        }


                                        try {

                                            if(json.has("vendormobile")){

                                                modal_vendor.setVendormobile( String.valueOf(json.get("vendormobile")));

                                            }
                                            else{
                                                modal_vendor.setVendormobile( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setVendormobile( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {

                                            if(json.has("status")){
                                                modal_vendor.setStatus( String.valueOf(json.get("status")));


                                            }
                                            else{
                                                modal_vendor.setStatus( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            modal_vendor.setStatus( String.valueOf(""));

                                        }


                                        try {
                                            if(json.has("key")){
                                                modal_vendor.setKey( String.valueOf(json.get("key")));


                                            }
                                            else{
                                                modal_vendor.setKey( String.valueOf(""));
                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setKey( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("inventorycheck")){
                                                modal_vendor.setInventorycheck( String.valueOf(json.get("inventorycheck")));


                                            }
                                            else{
                                                modal_vendor.setInventorycheck( String.valueOf(""));

                                            }



                                        } catch (Exception e) {
                                            modal_vendor.setInventorycheck( String.valueOf(""));

                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("istestvendor")){

                                                modal_vendor.setIstestvendor( String.valueOf(json.get("istestvendor")));

                                            }
                                            else{
                                                modal_vendor.setIstestvendor( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setIstestvendor( String.valueOf(""));


                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("pincode")){

                                                modal_vendor.setPincode( String.valueOf(json.get("pincode")));

                                            }
                                            else{
                                                modal_vendor.setPincode( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setPincode( String.valueOf(""));


                                            e.printStackTrace();
                                        }

///////////////////////////////////
                                        try {
                                            if(json.has("addressline1")){

                                                modal_vendor.setAddressline1( String.valueOf(json.get("addressline1")));

                                            }
                                            else{
                                                modal_vendor.setAddressline1( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setAddressline1( String.valueOf(""));


                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("addressline2")){

                                                modal_vendor.setAddressline2( String.valueOf(json.get("addressline2")));

                                            }
                                            else{
                                                modal_vendor.setAddressline2( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setAddressline2( String.valueOf(""));


                                            e.printStackTrace();
                                        }



                                        try {
                                            if(json.has("vendorfssaino")){

                                                modal_vendor.setVendorfssaino( String.valueOf(json.get("vendorfssaino")));

                                            }
                                            else{
                                                modal_vendor.setVendorfssaino( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setVendorfssaino( String.valueOf(""));


                                            e.printStackTrace();
                                        }

                                        try {
                                            if(json.has("defaultprintertype")){

                                                modal_vendor.setDefaultprintertype( String.valueOf(json.get("defaultprintertype")));

                                            }
                                            else{
                                                modal_vendor.setDefaultprintertype( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setDefaultprintertype( String.valueOf(""));


                                            e.printStackTrace();
                                        }


                                        try {
                                            if(json.has("locationlat")){

                                                modal_vendor.setLocationlat( String.valueOf(json.get("locationlat")));

                                            }
                                            else{
                                                modal_vendor.setLocationlat( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setLocationlat( String.valueOf(""));


                                            e.printStackTrace();
                                        }




                                        try {
                                            if(json.has("locationlong")){

                                                modal_vendor.setLocationlong( String.valueOf(json.get("locationlong")));

                                            }
                                            else{
                                                modal_vendor.setLocationlong( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setLocationlong( String.valueOf(""));


                                            e.printStackTrace();
                                        }



                                        try {
                                            if(json.has("inventorycheckpos")){

                                                modal_vendor.setInventorycheckpos( String.valueOf(json.get("inventorycheckpos")));

                                            }
                                            else{
                                                modal_vendor.setInventorycheckpos( String.valueOf(""));

                                            }

                                        } catch (Exception e) {
                                            modal_vendor.setInventorycheckpos( String.valueOf(""));


                                            e.printStackTrace();
                                        }



                                        vendorList.add(modal_vendor);


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //Log.d(Constants.TAG, "e: " + e.getLocalizedMessage());
                                    //Log.d(Constants.TAG, "e: " + e.getMessage());
                                    //Log.d(Constants.TAG, "e: " + e.toString());

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        saveVendorDetailsinSharedPreference(vendorList);
                        pos_spinner_aAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, VendorName_arrayList);
                        pos_vendor_selecting_spinner.setAdapter(pos_spinner_aAdapter);
                        Adjusting_Widgets_Visibility(false);

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
            Modal_vendor  vendor = vendorList.get(position);
            data = vendor.getGet(fieldName);
        } catch (Exception e) {
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
                    //Log.d(Constants.TAG, "response: " + response.get("body"));
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
                //Log.d(Constants.TAG, "Error: " + error.getLocalizedMessage());
                //Log.d(Constants.TAG, "Error: " + error.getMessage());
                //Log.d(Constants.TAG, "Error: " + error.toString());

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
        //Log.d(Constants.TAG, "Request Payload: " + jsonObject);

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
                            Intent i;
                            Constants.default_mobileScreenSize = Double.parseDouble(minimumscreensizeforpos);
                            if(screenInches < Constants.default_mobileScreenSize ){
                                 i =new Intent(Pos_Vendor_Selection_Screen.this, Mobile_LoginScreen.class);

                            }else {


                                 i = new Intent(Pos_Vendor_Selection_Screen.this, Pos_Dashboard_Screen.class);

                            }
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


                //Log.d(Constants.TAG, "Response: " + response);
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
        //Log.i(Constants.TAG,"VendorLoginStatus"+ pos_vendorLogin);


        SharedPreferences printerDatasharedPreferences
                = getSharedPreferences("PrinterConnectionData",
                MODE_PRIVATE);

        SharedPreferences.Editor printerDatamyEdit
                = printerDatasharedPreferences.edit();

        printerDatamyEdit.putString(
                "printerType",
                defaultprintertype);

        printerDatamyEdit.putString(
                "printerStatus",
                "Success");

        printerDatamyEdit.apply();


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
                "VendorType",
                pos_vendorKeyType);
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
                "MinimumScreenSizeForPos",
                minimumscreensizeforpos
        );
        myEdit.putString(
                "VendorLongitute",
                pos_vendorLongitude
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




    private void saveVendorDetailsinSharedPreference(List<Modal_vendor> vendorList) {
        try {
            final SharedPreferences sharedPreferences = getSharedPreferences("VendorList", MODE_PRIVATE);

            Gson gson = new Gson();
            String json = gson.toJson(vendorList);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("VendorList", json);
            editor.apply();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


}