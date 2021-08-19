package com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.SignInResult;
import com.meatchop.tmcpartner.AlertDialogClass;
import com.meatchop.tmcpartner.NukeSSLCerts;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import java.util.HashMap;
import java.util.Map;

public class MobileScreen_OtpVerificationActivity extends AppCompatActivity {
    private EditText edOtp1, edOtp2, edOtp3, edOtp4, edOtp5, edOtp6;
    private static final String TAG = "TAG";
    private String passCode, userMobileString;
    Button verifyOtpButton;
    private TextView userPhoneNumberTextview;
    private static final int REQ_USER_CONSENT = 2;
    private LinearLayout loadingPanel_dailyItemWisereport,loadingpanelmask_dailyItemWisereport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_screen__otp_verification_activity);
        new NukeSSLCerts();
        NukeSSLCerts.nuke();
        edOtp1 = findViewById(R.id.otp_first_et);
        edOtp2 = findViewById(R.id.otp_second_et);
        edOtp3 = findViewById(R.id.otp_third_et);
        edOtp4 = findViewById(R.id.otp_fourth_et);
        edOtp6 = findViewById(R.id.otp_sixth_et);
        edOtp5 = findViewById(R.id.otp_fifth_et);

        verifyOtpButton = findViewById(R.id.verifyOtp_button);

        userPhoneNumberTextview = findViewById(R.id.userPhoneNumberTextview);
        loadingPanel_dailyItemWisereport = findViewById(R.id.loadingPanel_dailyItemWisereport);
        loadingpanelmask_dailyItemWisereport = findViewById(R.id.loadingpanelmask_dailyItemWisereport);
        loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
        loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);


        GetUserPhoneNumber();
        //RegisterBroadcastReceiver();
        //StartSmsUserConsent();


        edOtp1.addTextChangedListener(new GenericTextWatcher(edOtp1));
        edOtp2.addTextChangedListener(new GenericTextWatcher(edOtp2));
        edOtp3.addTextChangedListener(new GenericTextWatcher(edOtp3));
        edOtp4.addTextChangedListener(new GenericTextWatcher(edOtp4));
        edOtp5.addTextChangedListener(new GenericTextWatcher(edOtp5));
        edOtp6.addTextChangedListener(new GenericTextWatcher(edOtp6));


        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {

                    @Override
                    public void onResult(UserStateDetails userStateDetails) {
                        //Log.i("INIT", "onResult: " + userStateDetails.getUserState());
                    }

                    @Override
                    public void onError(Exception e) {
                        //Log.e("INIT", "Initialization error.", e);
                    }
                }
        );

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((edOtp1.getText().toString().length() != 0) && (edOtp2.getText().toString().length() != 0) && (edOtp3.getText().toString().length() != 0) && (edOtp4.getText().toString().length() != 0) && (edOtp5.getText().toString().length() != 0) && (edOtp6.getText().toString().length() != 0)) {
                    passCode = edOtp1.getText().toString().trim() + edOtp2.getText().toString().trim() + edOtp3.getText().toString().trim() + edOtp4.getText().toString().trim() + edOtp5.getText().toString().trim() + edOtp6.getText().toString().trim();
                    loadingPanel_dailyItemWisereport.setVisibility(View.VISIBLE);
                    loadingpanelmask_dailyItemWisereport.setVisibility(View.VISIBLE);

                    verifyotp();
                }
            }
        });


    }

    /*

        private void StartSmsUserConsent() {
            SmsRetrieverClient client = SmsRetriever.getClient(this);
            //We can add sender phone number or leave it blank
            // I'm adding null here
            client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "On Success", Toast.LENGTH_LONG).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "On OnFailure", Toast.LENGTH_LONG).show();
                }
            });
        }
        private void RegisterBroadcastReceiver() {
            otpBroadcastReceiver = new OtpBroadcastReceiver();
            otpBroadcastReceiver.smsBroadcastReceiverListener =
                    new OtpBroadcastReceiver.SmsBroadcastReceiverListener() {
                        @Override
                        public void onSuccess(Intent intent) {
                            startActivityForResult(intent, REQ_USER_CONSENT);
                        }

                        @Override
                        public void onFailure() {

                        }
                    };
            IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
            registerReceiver(otpBroadcastReceiver, intentFilter);
        }
       /* @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQ_USER_CONSENT) {
                if ((resultCode == RESULT_OK) && (data != null)) {
                    //That gives all message to us.
                    // We need to get the code from inside with regex
                    String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                    //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    // textViewMessage.setText(String.format("%s - %s", getString(R.string.received_message), message));

                    getOtpFromMessage(message);
                    unregisterReceiver(otpBroadcastReceiver);

                }
            }
        }


        private void getOtpFromMessage(String message) {
            // This will match any 6 digit number in the message
            Pattern pattern = Pattern.compile("(|^)\\d{6}");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                edOtp1.setText(matcher.group(0));

            }
        }

        */
    private void GetUserPhoneNumber() {
        userMobileString = getIntent().getStringExtra("phone");
        userPhoneNumberTextview.setText(userMobileString);
    }

    class GenericTextWatcher implements TextWatcher {
        private final View view;

        private GenericTextWatcher(View view) {
            this.view = view;
            //Log.i("Tag", "ONGeneric Text Watcher");
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();

            //Log.i("Tag", "ONAfterTextChanged");
            switch (view.getId()) {

                case R.id.otp_first_et:

                    if (text.length() == 2) {
                        edOtp1.setText(String.valueOf(text.charAt(0)));
                        edOtp2.setText(String.valueOf(text.charAt(1)));
                        edOtp2.requestFocus();
                        edOtp2.setSelection(edOtp2.getText().length());
                    } else if (text.length() == 6) {
                        edOtp1.setText(String.valueOf(text.charAt(0)));
                        edOtp2.setText(String.valueOf(text.charAt(1)));
                        edOtp3.setText(String.valueOf(text.charAt(2)));
                        edOtp4.setText(String.valueOf(text.charAt(3)));
                        edOtp5.setText(String.valueOf(text.charAt(4)));
                        edOtp6.setText(String.valueOf(text.charAt(5)));

                        edOtp6.requestFocus();
                        edOtp6.setSelection(edOtp3.getText().length());
                    }

                    break;
                case R.id.otp_second_et:

                    if (text.length() > 1) {
                        edOtp2.setText(String.valueOf(text.charAt(0)));
                        edOtp3.setText(String.valueOf(text.charAt(1)));
                        edOtp3.requestFocus();
                        edOtp3.setSelection(edOtp3.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp1.requestFocus();
                        edOtp1.setSelection(edOtp1.getText().length());
                    }
                    break;
                case R.id.otp_third_et:

                    if (text.length() > 1) {
                        edOtp3.setText(String.valueOf(text.charAt(0)));
                        edOtp4.setText(String.valueOf(text.charAt(1)));
                        edOtp4.requestFocus();
                        edOtp4.setSelection(edOtp4.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp2.requestFocus();
                        edOtp2.setSelection(edOtp2.getText().length());
                    }
                    break;

                case R.id.otp_fourth_et:

                    if (text.length() > 1) {
                        edOtp4.setText(String.valueOf(text.charAt(0)));
                        edOtp5.setText(String.valueOf(text.charAt(1)));
                        edOtp5.requestFocus();
                        edOtp5.setSelection(edOtp5.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp3.requestFocus();
                        edOtp3.setSelection(edOtp3.getText().length());
                    }
                    break;

                case R.id.otp_fifth_et:

                    if (text.length() > 1) {
                        edOtp5.setText(String.valueOf(text.charAt(0)));
                        edOtp6.setText(String.valueOf(text.charAt(1)));
                        edOtp6.requestFocus();
                        edOtp6.setSelection(edOtp6.getText().length());
                    }
                    if (text.length() == 0) {
                        edOtp4.requestFocus();
                        edOtp4.setSelection(edOtp4.getText().length());
                    }
                    break;

                case R.id.otp_sixth_et:
                    if (text.length() == 0) {
                        edOtp5.requestFocus();
                        edOtp5.setSelection(edOtp5.getText().length());
                    }
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            //Log.i("Tag", "ONbeforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            //Log.i("Tag", "ONTextChanged");
        }
    }


    private void verifyotp() {
        final Map<String, String> attributes = new HashMap<>();
        attributes.put("ANSWER", passCode);
        //Log.d(TAG, "Si " + passCode);

        AWSMobileClient.getInstance().confirmSignIn(attributes, new Callback<SignInResult>() {
            @Override
            public void onResult(SignInResult signInResult) {
                //Log.d(TAG, "UserName " + userMobileString + "  password " + passCode);

                //Log.d(TAG, "Log-in  state: " + signInResult.getSignInState());
                switch (signInResult.getSignInState()) {
                    case DONE:
                        //Log.d(TAG, "Log-in done");

                        saveUserDetails();
                        Intent i = new Intent(MobileScreen_OtpVerificationActivity.this, Mobile_Vendor_Selection_Screen.class);
                        startActivity(i);
                        finish();
                        loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                        loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);

                        break;
                    case SMS_MFA:
                        //Log.d(TAG, "Please confirm sign-in with SMS.");
                        break;
                    case NEW_PASSWORD_REQUIRED:
                        //Log.d(TAG, "Please confirm sign-in with new password.");
                        break;
                    case CUSTOM_CHALLENGE:
                        loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                        loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);

                        AlertDialogClass.showDialog(MobileScreen_OtpVerificationActivity.this, R.string.Enter_Correct_OTP);

                        //Log.d(TAG, " Custom challenge.");
                        break;
                    default:
                        //Log.d(TAG, "Unsupported Log-in confirmation: " + signInResult.getSignInState().toString());
                        break;
                }
            }

            @Override
            public void onError(final Exception e) {
                loadingPanel_dailyItemWisereport.setVisibility(View.INVISIBLE);
                loadingpanelmask_dailyItemWisereport.setVisibility(View.INVISIBLE);


                //Log.d(TAG, "UserNamexx" + userMobileString + "  passwordvv" + passCode);

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
                "UserPhoneNumber", userMobileString
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
