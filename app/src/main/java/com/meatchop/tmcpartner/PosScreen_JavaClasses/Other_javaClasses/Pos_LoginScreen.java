package com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.Mobile_Vendor_Selection_Screen;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.Mobile_LoginScreen;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import java.util.HashMap;
import java.util.Map;

public class Pos_LoginScreen extends AppCompatActivity {
    private EditText pos_mobileNo_widget;
    private TextView enter_OtpInfo_widget,enter_MobileNo_widget;
    private String mobileNo_String;
    private  Button pos_SendOtp_widget,pos_VerifyOtp_widget;
    private LinearLayout loadingPanel_dailyItemWisereport,loadingpanelmask_dailyItemWisereport,pos_otp_Ed_Layout;
    private boolean vendorLoginStatusBoolean;
    private EditText pos_edOtp1, pos_edOtp2, pos_edOtp3, pos_edOtp4 ,pos_edOtp5, pos_edOtp6 ;
    private static final String TAG = "TAG";
    private String passCode, userMobileString, minimumScreenSizeForPos;
    double screenInches = Constants.default_mobileScreenSize ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        try {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
            screenInches = Math.sqrt(x + y);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if(screenInches < Constants.default_mobileScreenSize ){
            //Log.d("debug","Mobile device");
          // setContentView(R.layout.pos_login_activity);

            //Log.d("debug","Screen inches : " + screenInches);
            SharedPreferences sharedPreferences
                    = getSharedPreferences("CurrentSelectedStatus",
                    MODE_PRIVATE);

            SharedPreferences.Editor myEdit
                    = sharedPreferences.edit();


            myEdit.putString(
                    "currentstatus",
                    Constants.NEW_ORDER_STATUS);
            myEdit.apply();

           Intent i =new Intent(Pos_LoginScreen.this, Mobile_LoginScreen.class);
           startActivity(i);
            finish();


        }
        else
            {


                //Log.d("debug","Pos device");

                setContentView(R.layout.pos_login_activity);

                //Log.d("debug","Screen inches : " + screenInches);
                SharedPreferences sharedPreferences
                        = getSharedPreferences("CurrentSelectedStatus",
                        MODE_PRIVATE);

                SharedPreferences.Editor myEdit
                        = sharedPreferences.edit();


                myEdit.putString(
                        "currentstatus",
                        Constants.NEW_ORDER_STATUS);
                myEdit.apply();

            //
                 pos_mobileNo_widget = findViewById(R.id.pos_mobileNo_widget);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pos_mobileNo_widget.setFocusedByDefault(false);
                }


            pos_SendOtp_widget = findViewById(R.id.pos_SendOtp_widget);
            pos_VerifyOtp_widget = findViewById(R.id.pos_VerifyOtp_widget);
            enter_OtpInfo_widget = findViewById(R.id.enter_OtpInfo_widget);
            enter_MobileNo_widget = findViewById(R.id.enter_MobileNo_widget);

            //progressBar Layout
            loadingPanel_dailyItemWisereport = findViewById(R.id.loadingPanel_dailyItemWisereport);
            loadingpanelmask_dailyItemWisereport = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
            loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
            loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);

            //OTP Layout and its EditText
            pos_otp_Ed_Layout = findViewById(R.id.otp_Ed_Layout);
            pos_edOtp1 = findViewById(R.id.otp_first_et);
            pos_edOtp2 = findViewById(R.id.otp_second_et);
            pos_edOtp3 = findViewById(R.id.otp_third_et);
            pos_edOtp4 = findViewById(R.id.otp_fourth_et);
            pos_edOtp5 = findViewById(R.id.otp_fifth_et);
            pos_edOtp6 = findViewById(R.id.otp_sixth_et);


            pos_edOtp1.addTextChangedListener(new GenericTextWatcher(pos_edOtp1));
            pos_edOtp2.addTextChangedListener(new GenericTextWatcher(pos_edOtp2));
            pos_edOtp3.addTextChangedListener(new GenericTextWatcher(pos_edOtp3));
            pos_edOtp4.addTextChangedListener(new GenericTextWatcher(pos_edOtp4));
            pos_edOtp5.addTextChangedListener(new GenericTextWatcher(pos_edOtp5));
            pos_edOtp6.addTextChangedListener(new GenericTextWatcher(pos_edOtp6));




                pos_SendOtp_widget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mobileNo_String = pos_mobileNo_widget.getText().toString().trim();
                        if(mobileNo_String .length() == 10) {
                            mobileNo_String = "+91" + mobileNo_String;
                            loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                            loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);
                            //calling Login Method first
                            StartSignUp(mobileNo_String);
                        }
                        else{

                            // showDVAlert(R.string.Enter_Correct_No);

                            AlertDialogClass.showDialog(Pos_LoginScreen.this,R.string.Enter_Correct_No);
                        }
                    }
                });




                pos_VerifyOtp_widget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if((pos_edOtp1.getText().toString().length()!=0)&&(pos_edOtp2.getText().toString().length()!=0)&&(pos_edOtp3.getText().toString().length()!=0)&&(pos_edOtp4.getText().toString().length()!=0)&&(pos_edOtp5.getText().toString().length()!=0)&&(pos_edOtp6.getText().toString().length()!=0))
                        {
                            passCode = pos_edOtp1.getText().toString().trim() + pos_edOtp2.getText().toString().trim() + pos_edOtp3.getText().toString().trim() + pos_edOtp4.getText().toString().trim() + pos_edOtp5.getText().toString().trim() + pos_edOtp6.getText().toString().trim();

                            verifyotp();
                        }
                    }
                });






            }




    }


    @Override
    protected void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
        //Log.d("debug","Screen inches : " + screenInches);
        if(screenInches < Constants.default_mobileScreenSize ){
            Intent i =new Intent(Pos_LoginScreen.this, Mobile_LoginScreen.class);
            startActivity(i);
            finish();
        }else {
            AWSMobileIntialization();
        }

    }
    //It will intialize the AWS Mobile client in the dvice and check weather the user signedIn or ....
    // ......not on both Cognito and Vendor Login whenever he starts the app........
    //.......First it will check  cognito for Userdetails State for signin ......
    // ......if user have already signed in this device then ,it will check for the vendor Login details and navigate the
    //.......user screen accordingly.

    private void AWSMobileIntialization() {

                              AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                                          @Override
                                          public void onResult(UserStateDetails userStateDetails) {
                                              //Log.i("Tag", "Intialization Success");

                                              //Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                                              if (userStateDetails.getUserState() == UserState.SIGNED_IN) {
                                                  //Log.i("Tag", "Intialization Success 2");

                                                   loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                                                    loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);
                                                  SharedPreferences sh
                                                          = getSharedPreferences("VendorLoginData",
                                                          MODE_PRIVATE);

                                                  vendorLoginStatusBoolean = sh.getBoolean("VendorLoginStatus", false);
                                                  minimumScreenSizeForPos = sh.getString("MinimumScreenSizeForPos", String.valueOf(Constants.default_mobileScreenSize));
                                                  //Log.i("Tag", " Successfully got the vendor login status");
                                                  //Log.i("Tag", "VendorLoginStatus ee" + vendorLoginStatusBoolean);

                                                  runOnUiThread(new Runnable() {

                                                      @Override
                                                      public void run() {

                                                  Intent i;
                                                  if (vendorLoginStatusBoolean) {
                                                      //Log.i("Tag", " Navigate to dashboard according to vendor login status");
                                                      Constants.default_mobileScreenSize = Integer.parseInt(minimumScreenSizeForPos);

                                                      loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                                      loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                                      if(screenInches < Constants.default_mobileScreenSize ){
                                                          i =new Intent(Pos_LoginScreen.this, MobileScreen_Dashboard.class);

                                                      }else {


                                                          i = new Intent(Pos_LoginScreen.this, Pos_Dashboard_Screen.class);

                                                      }
                                                  } else {
                                                      //Log.i("Tag", " Navigate to vendor selection screen according to vendor login status");

                                                      loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                                      loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                                      if(screenInches < Constants.default_mobileScreenSize ){
                                                          i =new Intent(Pos_LoginScreen.this, Mobile_Vendor_Selection_Screen.class);

                                                      }else {


                                                          i = new Intent(Pos_LoginScreen.this, Pos_Vendor_Selection_Screen.class);

                                                      }
                                                    //  i = new Intent(Pos_LoginScreen.this, Pos_Vendor_Selection_Screen.class);
                                                  }
                                                  startActivity(i);
                                                  overridePendingTransition(0, 0);

                                                  finish();
                                                      }

                                                  });
                                              } else {
                                                  //Log.i("Tag", "First Signin bcz ur account is   " + userStateDetails.getUserState());
                                                  loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                                  loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                              }

                                          }

                                          @Override
                                          public void onError(Exception e) {
                                              //Log.i("Tag", "Intialization not Success");
                                              loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                              loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                              //Log.e("INIT", "Initialization error.", e.fillInStackTrace());
                                          }
                                      }

                              );

    }




    //LOGIN
    //check weather this user is already registered or not with his mobile number...
    // ......if not then it will call the Sign method  ...
    // ......otherwise it will send the Otp directly.


    private void StartSignUp(String mobileNo_String) {

        final Map<String, String> attributes = new HashMap<>();
        attributes.put("phone_number", mobileNo_String);
        attributes.put("name", "arun");
        attributes.put("email", "");
        AWSMobileClient.getInstance().signUp(mobileNo_String, "password", attributes, null, new Callback<SignUpResult>() {
            @Override
            public void onResult(final SignUpResult signUpResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d(TAG, "Sign-up callback state: " + signUpResult.getConfirmationState());
                        if (!signUpResult.getConfirmationState()) {

                            final UserCodeDeliveryDetails details = signUpResult.getUserCodeDeliveryDetails();
                            //Log.d(TAG, "Sign-up callback state: " + details.getDestination());
                        } else {

                            StartSignIN(mobileNo_String);
                            //Log.d(TAG, "Sign-up  " );
                        }
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {

                        //Call the SignIN method ..
                        // ......ie this user have not created their account in cognito


                        //AlertDialogClass.showDialog(Login.this, R.string.Enter_Correct_No);
                        StartSignIN(mobileNo_String);
                    }
                });
                //Log.e(TAG, "Sign-up error", e.fillInStackTrace());
            }
        });


    }


    //SIGNIN
    //We are using Custom challenge authentication so when signInResult was in that case it
    // ...... will adjust the progress bar and wigets accordingly.
    private void StartSignIN(final String mobileNo_String) {
        AWSMobileClient.getInstance().signIn(mobileNo_String, "password", null, new Callback<SignInResult>() {

            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d(TAG, "UserName"+mobileNo_String);

                        //Log.d(TAG, "Log-in callback state2 : " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                //Log.d(TAG,"Log-in done state.");
                                //Log.d(TAG,"Email: " + signInResult.getCodeDetails());
                                break;
                            case SMS_MFA:
                                //Log.d(TAG,"Please confirm sign-in with SMS.");
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                //Log.d(TAG,"Please confirm sign-in with new password.");
                                break;
                            case CUSTOM_CHALLENGE:
                                pos_otp_Ed_Layout.setVisibility(View.VISIBLE);
                                pos_SendOtp_widget.setVisibility(View.GONE);
                                pos_VerifyOtp_widget.setVisibility(View.VISIBLE);
                                loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                enter_MobileNo_widget.setVisibility(View.GONE);
                                enter_OtpInfo_widget.setVisibility(View.VISIBLE);

                                //Log.d(TAG," Custom challenge.");
                                break;
                            default:

                                //Log.d(TAG,"Unsupported Log-in confirmation: " + signInResult.getSignInState());
                                break;
                        }
                    }
                });
            }

            @Override
            public void onError(final Exception e) {
                //Log.e(TAG, "sign-in error", e.fillInStackTrace());
            }
        });
    }




    class GenericTextWatcher implements TextWatcher {
        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
            //Log.i("Tag","ONGeneric Text Watcher");
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            //Log.i("Tag","ONAfterTextChanged");
            switch (view.getId()) {

                case R.id.otp_first_et:

                    if (text.length() ==2) {
                        pos_edOtp1.setText(String.valueOf(text.charAt(0)));
                        pos_edOtp2.setText(String.valueOf(text.charAt(1)));
                        pos_edOtp2.requestFocus();
                        pos_edOtp2.setSelection(pos_edOtp2.getText().length());
                    }
                    else if (text.length() == 6){
                        pos_edOtp1.setText(String.valueOf(text.charAt(0)));
                        pos_edOtp2.setText(String.valueOf(text.charAt(1)));
                        pos_edOtp3.setText(String.valueOf(text.charAt(2)));
                        pos_edOtp4.setText(String.valueOf(text.charAt(3)));
                        pos_edOtp5.setText(String.valueOf(text.charAt(4)));
                        pos_edOtp6.setText(String.valueOf(text.charAt(5)));

                        pos_edOtp6.requestFocus();
                        pos_edOtp6.setSelection(pos_edOtp3.getText().length());
                    }

                    break;
                case R.id.otp_second_et:

                    if (text.length() > 1){
                        pos_edOtp2.setText(String.valueOf(text.charAt(0)));
                        pos_edOtp3.setText(String.valueOf(text.charAt(1)));
                        pos_edOtp3.requestFocus();
                        pos_edOtp3.setSelection(pos_edOtp3.getText().length());
                    }
                    if (text.length() == 0){
                        pos_edOtp1.requestFocus();
                        pos_edOtp1.setSelection(pos_edOtp1.getText().length());
                    }
                    break;
                case R.id.otp_third_et:

                    if (text.length() > 1){
                        pos_edOtp3.setText(String.valueOf(text.charAt(0)));
                        pos_edOtp4.setText(String.valueOf(text.charAt(1)));
                        pos_edOtp4.requestFocus();
                        pos_edOtp4.setSelection(pos_edOtp4.getText().length());
                    }
                    if (text.length() == 0){
                        pos_edOtp2.requestFocus();
                        pos_edOtp2.setSelection(pos_edOtp2.getText().length());
                    }
                    break;

                case R.id.otp_fourth_et:

                    if (text.length() > 1){
                        pos_edOtp4.setText(String.valueOf(text.charAt(0)));
                        pos_edOtp5.setText(String.valueOf(text.charAt(1)));
                        pos_edOtp5.requestFocus();
                        pos_edOtp5.setSelection(pos_edOtp5.getText().length());
                    }
                    if (text.length() == 0){
                        pos_edOtp3.requestFocus();
                        pos_edOtp3.setSelection(pos_edOtp3.getText().length());
                    }
                    break;

                case R.id.otp_fifth_et:

                    if (text.length() > 1){
                        pos_edOtp5.setText(String.valueOf(text.charAt(0)));
                        pos_edOtp6.setText(String.valueOf(text.charAt(1)));
                        pos_edOtp6.requestFocus();
                        pos_edOtp6.setSelection(pos_edOtp6.getText().length());
                    }
                    if (text.length() == 0){
                        pos_edOtp4.requestFocus();
                        pos_edOtp4.setSelection(pos_edOtp4.getText().length());
                    }
                    break;

                case R.id.otp_sixth_et:
                    if (text.length() == 0){
                        pos_edOtp5.requestFocus();
                        pos_edOtp5.setSelection(pos_edOtp5.getText().length());
                    }
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            //Log.i("Tag","ONbeforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            //Log.i("Tag","ONTextChanged");
        }
    }



    private void verifyotp() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put("ANSWER", passCode);
        //Log.d(TAG, "Si "+passCode);

        AWSMobileClient.getInstance().confirmSignIn(attributes, new Callback<SignInResult>() {
            @Override
            public void onResult(SignInResult signInResult) {
                //Log.d(TAG, "UserName "+ userMobileString +"  password "+passCode);

                //Log.d(TAG, "Log-in  state: " + signInResult.getSignInState());
                switch (signInResult.getSignInState()) {
                    case DONE:
                        //Log.d(TAG, "Log-in done");
                        saveUserDetails();
                        Intent i = new Intent(Pos_LoginScreen.this, Pos_Vendor_Selection_Screen.class);
                        startActivity(i);
                        finish();

                        break;
                    case SMS_MFA:
                        //Log.d(TAG, "Please confirm sign-in with SMS.");
                        break;
                    case NEW_PASSWORD_REQUIRED:
                        //Log.d(TAG, "Please confirm sign-in with new password.");
                        break;
                    case CUSTOM_CHALLENGE:
                        AlertDialogClass.showDialog(Pos_LoginScreen.this, R.string.Enter_Correct_OTP);

                        //Log.d(TAG," Custom challenge.");
                        break;
                    default:
                        //Log.d(TAG, "Unsupported Log-in confirmation: " + signInResult.getSignInState().toString());
                        break;
                }
            }

            @Override
            public void onError(final Exception e) {



                //Log.d(TAG, "UserNamexx"+ userMobileString +"  passwordvv"+passCode);

                //Log.e(TAG, "Log-in error", e);
            }
        });
    }

    private void saveUserDetails() {


        SharedPreferences sharedPreferences
                = getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();


        myEdit.putString(
                "UserPhoneNumber", mobileNo_String
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