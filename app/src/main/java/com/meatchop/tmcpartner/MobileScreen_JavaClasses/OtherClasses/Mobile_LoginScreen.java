package com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserState;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.amazonaws.mobile.client.results.SignUpResult;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class Mobile_LoginScreen extends AppCompatActivity {
    private EditText login_mobileNo_Text;
    private String mobileNo_String;
    private LinearLayout loadingPanel_dailyItemWisereport,loadingpanelmask_dailyItemWisereport;
    private boolean vendorLoginStatusBoolean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile__login_screen_activity);

        findViewById(R.id.sendOtp_button);
        Button sendOtp_button;
        login_mobileNo_Text = findViewById(R.id.login_mobileNo_Text);
        sendOtp_button = findViewById(R.id.sendOtp_button);
        loadingPanel_dailyItemWisereport = findViewById(R.id.loadingPanel_dailyItemWisereport);
        loadingpanelmask_dailyItemWisereport = findViewById(R.id.loadingpanelmask_dailyItemWisereport);

        loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
        loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);




        sendOtp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobileNo_String = login_mobileNo_Text.getText().toString().trim();
                if(mobileNo_String .length() == 10) {
                    mobileNo_String = "+91" + mobileNo_String;
                    loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                    loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);

                    StartSignUp(mobileNo_String);
                }
                else{

                    // showDVAlert(R.string.Enter_Correct_No);
                    loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                    loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);

                    AlertDialogClass.showDialog(Mobile_LoginScreen.this,R.string.Enter_Correct_No);
                }
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
        AWSMobileIntialization();
    }

    private void AWSMobileIntialization() {
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        //Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                        if(userStateDetails.getUserState() == UserState.SIGNED_IN){

                            SharedPreferences sh
                                    = getSharedPreferences("VendorLoginData",
                                    MODE_PRIVATE);

                            vendorLoginStatusBoolean = sh.getBoolean("VendorLoginStatus",false);
                            //Log.i("Tag","VendorLoginStatus"+vendorLoginStatusBoolean);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                            Intent i;
                            if (vendorLoginStatusBoolean) {
                                loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                                loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);

                                i = new Intent(Mobile_LoginScreen.this, MobileScreen_Dashboard.class);
                            } else {
                                loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                                loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);

                                i = new Intent(Mobile_LoginScreen.this, Mobile_Vendor_Selection_Screen.class);
                            }
                            startActivity(i);
                            finish();
                        }
                            });}
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("INIT", "Initialization error.", e);
                    }
                }
        );
    }



    private void StartSignUp(final String mobileNo_String) {

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
                        //AlertDialogClass.showDialog(Login.this, R.string.Enter_Correct_No);
                        StartSignIN(mobileNo_String);
                    }
                });
                //Log.e(TAG, "Sign-up error", e);
            }
        });

    }



    private void StartSignIN(final String mobileNo_String) {
        AWSMobileClient.getInstance().signIn(mobileNo_String, "password", null, new Callback<SignInResult>() {

            @Override
            public void onResult(final SignInResult signInResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Log.d(TAG, "UserName"+mobileNo_String);

                        //Log.d(TAG, "Log-in callback state: " + signInResult.getSignInState());
                        switch (signInResult.getSignInState()) {
                            case DONE:
                                //Log.d(TAG,"Log-in done.");
                                //Log.d(TAG,"Email: " + signInResult.getCodeDetails());
                                break;
                            case SMS_MFA:
                                //Log.d(TAG,"Please confirm sign-in with SMS.");
                                break;
                            case NEW_PASSWORD_REQUIRED:
                                //Log.d(TAG,"Please confirm sign-in with new password.");
                                break;
                            case CUSTOM_CHALLENGE:

                                Intent i = new Intent(Mobile_LoginScreen.this, MobileScreen_OtpVerificationActivity.class);
                                Bundle b = new Bundle();
                                b.putString("phone",mobileNo_String);
                                i.putExtras(b);
                                loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                                loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);

                                startActivity(i);
                                finish();
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
                //Log.e(TAG, "Log-in error", e);
            }
        });


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