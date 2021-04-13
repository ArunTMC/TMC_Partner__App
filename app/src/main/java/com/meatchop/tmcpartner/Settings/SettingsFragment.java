package com.meatchop.tmcpartner.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.meatchop.tmcpartner.Constants;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.MobileScreen_Dashboard;
import com.meatchop.tmcpartner.MobileScreen_JavaClasses.OtherClasses.Mobile_Vendor_Selection_Screen;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.ManageOrders.TrackingOrderDetails_ServiceClass;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_Dashboard_Screen;
import com.meatchop.tmcpartner.PosScreen_JavaClasses.Other_javaClasses.Pos_LoginScreen;
import com.meatchop.tmcpartner.R;
import com.meatchop.tmcpartner.TMCAlertDialogClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.AbsListView.*;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
Button on,off;
Context mContext;
@SuppressLint("UseSwitchCompatOrMaterialCode")
Switch autoRefreshingSwitch;
LinearLayout changeMenuItemStatus,logout,consolidatedSalesReport,PosSalesReport,AppSalesReport,changeMenuItemVisibilityinTv
    ,changeMenuItemPrice,changeDeliverySlotdetails,deliveryPartnerSettlementReport,searchOrdersUsingMobileNumbers;
String UserRole, MenuItems,UserPhoneNumber,vendorkey,vendorName;
TextView userMobileNo,resetTokenNO_text,storeName,App_Sales_Report_text,Pos_Sales_Report_text;
LinearLayout resetTokenNoLayoutj,salesLinearLayout;
Button resetTokenNoButton;
ScrollView settings_scrollview;
BottomNavigationView bottomNavigationView;

double screenInches;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static SettingsFragment newInstance(String data) {
        Bundle args = new Bundle();
      //  args.putString("menuItem", data);

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getData() {

        return getArguments().getString("menuItem");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity().getWindow().getContext();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        deliveryPartnerSettlementReport = view.findViewById(R.id.deliveryPartnerSettlementReport);
        searchOrdersUsingMobileNumbers = view.findViewById(R.id.searchOrdersUsingMobileNumbers);
        userMobileNo = view.findViewById(R.id.userMobileNo);
        settings_scrollview = view.findViewById(R.id.settings_scrollview);
        autoRefreshingSwitch = view.findViewById(R.id.autoRefreshingSwitch);
        changeMenuItemStatus = view.findViewById(R.id.changeMenuItemStatus);
        consolidatedSalesReport = view.findViewById(R.id.consolidatedSalesReport);
        PosSalesReport = view.findViewById(R.id.PosSalesReport);
        AppSalesReport = view.findViewById(R.id.AppSalesReport);
        changeMenuItemVisibilityinTv = view.findViewById(R.id.changeMenuItemVisibilityinTv);
        changeMenuItemPrice = view.findViewById(R.id.changeMenuItemPrice);
        logout = view.findViewById(R.id.logout);
        resetTokenNoButton= view.findViewById(R.id.resetTokenNoButton);
        resetTokenNO_text = view.findViewById(R.id.resetTokenNO_text);
        changeDeliverySlotdetails = view.findViewById(R.id.changeDeliverySlotdetails);
        storeName = view.findViewById(R.id.storeName);
        salesLinearLayout = view.findViewById(R.id.salesLinearLayout);
      //  bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);

        //  final SharedPreferences sharedPreferencesMenuitem = requireContext().getSharedPreferences("MenuList", MODE_PRIVATE);
      //  MenuItems = sharedPreferencesMenuitem.getString("MenuList", "");

        SharedPreferences shared = requireContext().getSharedPreferences("VendorLoginData", MODE_PRIVATE);
        UserPhoneNumber = (shared.getString("UserPhoneNumber", "+91"));
        vendorkey = shared.getString("VendorKey","vendor_1");
        vendorName = shared.getString("VendorName","");
        UserRole = shared.getString("userrole","");
        userMobileNo.setText(UserPhoneNumber);
        storeName.setText(vendorName);

        getTokenNo(vendorkey);
      //  initializeCache();
        if(UserRole.equals(Constants.STOREMANAGER_ROLENAME)){
            changeDeliverySlotdetails.setVisibility(VISIBLE);
            changeMenuItemStatus.setVisibility(VISIBLE);
            changeMenuItemVisibilityinTv.setVisibility(VISIBLE);
            changeMenuItemPrice.setVisibility(VISIBLE);
            searchOrdersUsingMobileNumbers.setVisibility(VISIBLE);
            salesLinearLayout.setVisibility(VISIBLE);


        }

        else if(UserRole.equals(Constants.ASSISTANTSTOREMANAGER_ROLENAME)){
            changeDeliverySlotdetails.setVisibility(GONE);
            changeMenuItemStatus.setVisibility(GONE);
            changeMenuItemVisibilityinTv.setVisibility(GONE);
            changeMenuItemPrice.setVisibility(GONE);
            searchOrdersUsingMobileNumbers.setVisibility(VISIBLE);
            salesLinearLayout.setVisibility(GONE);


        }
        else{
            changeDeliverySlotdetails.setVisibility(GONE);
            changeMenuItemStatus.setVisibility(GONE);
            changeMenuItemVisibilityinTv.setVisibility(GONE);
            changeMenuItemPrice.setVisibility(GONE);
            searchOrdersUsingMobileNumbers.setVisibility(GONE);
            salesLinearLayout.setVisibility(GONE);
            Toast.makeText(mContext,"You Don't have any User Role Ask Admin to assign the Role",Toast.LENGTH_LONG).show();


        }



        DisplayMetrics dm = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        screenInches = Math.sqrt(x+y);
        if(screenInches<8){
            bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);
            settings_scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    int scrollY = settings_scrollview.getScrollY(); //for verticalScrollView
                    if (scrollY == 0){
                        if(screenInches<8){
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }
                    else{
//                        Toast.makeText(mContext,"Swipe down to make the Settings Button Visible",Toast.LENGTH_SHORT).show();
                        bottomNavigationView.setVisibility(View.GONE);

                    }

                }
            });
                }
        else{
           // bottomNavigationView = ((MobileScreen_Dashboard) Objects.requireNonNull(getActivity())).findViewById(R.id.bottomnav);

        }




        autoRefreshingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                 //  mContext.startService(new Intent(mContext, TrackingOrderDetails_ServiceClass.class));
                } else {
                   // mContext.stopService(new Intent(mContext, TrackingOrderDetails_ServiceClass.class));
                }
            }
        });
        deliveryPartnerSettlementReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DeliveryPartnerSettlementReport.class);
                startActivity(intent);
            }
        });


        changeDeliverySlotdetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChangeDelivery_Slot_Availability_Status.class);
                startActivity(intent);
            }
        });

        searchOrdersUsingMobileNumbers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, searchOrdersUsingMobileNumber.class);
                startActivity(intent);
            }
        });
        changeMenuItemVisibilityinTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //trimCache(mContext);
                Intent intent = new Intent(mContext, ChangeMenuItem_Availabilty_InTV_Settings.class);
                startActivity(intent);

            }
        });



        AppSalesReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AppSales_Report.class);
                mContext.startActivity(i);
            }
        });
//AppSales_Report
//App_Sales_Report_Subctgywise


        PosSalesReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext,PosSalesReport.class);
                mContext.startActivity(i);
            }
        });

        
        consolidatedSalesReport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               openConsolidatedSalesReportActivity();
            }
        });


        logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getConfirmationtoLogout();
            }
        });



        changeMenuItemStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, ChangeMenuItemStatus_Settings.class);
                startActivity(intent);


            }
        });
        changeMenuItemPrice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, MenuItem_List_Settings.class);
                startActivity(intent);


            }
        });
        resetTokenNoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getConfirmationtoResetTokenNo(vendorkey);
            }
        });




    }/*
    private void initializeCache() {
        long size = 0;
        size += getDirSize(mContext.getCacheDir());
        Log.i(Constants.TAG,"Cache Memory :1 "+size);

        size += getDirSize(mContext.getExternalCacheDir());
        Log.i(Constants.TAG,"Cache Memory :2 "+size);

    }*/


/*
    public static long getDirSize(File dir){
        long size = 0;
        long size = 0;
        File[] files = cacheDirectory.listFiles();
        for (File f:files) {
            size = size+f.length();
        }
       for (File file : dir.listFiles()) {

            if (file != null && file.isDirectory()) {
                size += getDirSize(file);
            } else if (file != null && file.isFile()) {
                size += file.length();
            }
        }


        return size;
    }

    */
    /*
    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
          long cacheSize =  getDirSize(dir);

            Log.i(Constants.TAG,"Cache Memory :"+cacheSize);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else {
            return false;
        }
    }

    private void getConfirmationtoLogout() {
        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.Exit_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        signOutfromAWSandClearSharedPref();

                    }

                    @Override
                    public void onNo() {

                    }
                });
    }

    private void getConfirmationtoResetTokenNo(String vendorkey) {
        new TMCAlertDialogClass(mContext, R.string.app_name, R.string.ResetToken_Instruction,
                R.string.Yes_Text, R.string.No_Text,
                new TMCAlertDialogClass.AlertListener() {
                    @Override
                    public void onYes() {
                        resetTokenNo(vendorkey);

                    }

                    @Override
                    public void onNo() {

                    }
                });
    }

    private void getTokenNo(String vendorkey) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.api_GetTokenNoUsingKey+vendorkey,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(@NonNull JSONObject response) {
                        Log.d(Constants.TAG, "Response: " + response);


                        Log.d(Constants.TAG, "Response: " + response);
                        try {

                            JSONObject result  = response.getJSONObject("content");
                            JSONObject result2  = result.getJSONObject("Item");
                            String tokenNumber = result2.getString("tokenNumber");
                            resetTokenNO_text.setText(tokenNumber);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);

    }

    private void resetTokenNo(String vendorkey) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,Constants.api_ResetTokenNo+vendorkey,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(@NonNull JSONObject response) {


                Log.d(Constants.TAG, "api: " + Constants.api_ResetTokenNo+vendorkey);

                Log.d(Constants.TAG, "Responsewwwww: " + response);
                try {
                    String tokenNo = response.getString("tokenNumber");
                    resetTokenNO_text.setText(tokenNo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError error) {
                Log.d(Constants.TAG, "Error1: " + error.getLocalizedMessage());
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
        Volley.newRequestQueue(mContext).add(jsonObjectRequest);


    }


    private void openConsolidatedSalesReportActivity() {
        SharedPreferences sharedPreferences
                = requireContext().getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        String VendorName  = sharedPreferences.getString("VendorName","");



        Intent i = new Intent(mContext, Consolidated_Sales_Report.class);
        i.putExtra("VendorName",VendorName);
        mContext.startActivity(i);



    }



    private void signOutfromAWSandClearSharedPref() {

        AWSMobileClient.getInstance().signOut();


        SharedPreferences sharedPreferences
                = requireContext().getSharedPreferences("VendorLoginData",
                MODE_PRIVATE);

        SharedPreferences.Editor myEdit
                = sharedPreferences.edit();

        myEdit.putBoolean(
                "VendorLoginStatus",
                false);
        myEdit.putString(
                "VendorKey",
                "");
        myEdit.putString(
                "VendorName",
                ""
        );
        myEdit.putString(
                "VendorAddressline1",
                ""
        );
        myEdit.putString(
                "VendorAddressline2",
                ""
        );
        myEdit.putString(
                "VendorPincode",
                ""
        );
        myEdit.putString(
                "VendorMobileNumber",
                ""
        );

        myEdit.putString(
                "VendorFssaino",
                ""
        );

        myEdit.apply();
        Intent i = new Intent(mContext, Pos_LoginScreen.class);
        startActivity(i);
        getActivity().finish();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.pos_settings_fragment, container, false);
       // MenuItems=getData();

        rootView.setTag("SettingsFragment");
        return rootView;
    }
}